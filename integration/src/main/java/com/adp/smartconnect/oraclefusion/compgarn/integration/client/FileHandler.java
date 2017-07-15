package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTask;
import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTaskConstants;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.WebContentUploadDetails;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.contentupload.WebContentUpload;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

public class FileHandler  {

	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
	private Configuration configuration;
	private ClientConfigHolder clientConfigurations;
	private WebContentUpload webContentUpload;
	private NotificationEngine notifEngine;
	private BatchLoadTask batchLoadTask;
	


	/*
	 * Handle Input Lien Notification File
	 * 1. Strip Header/Footer in file
	 * 2. Upload Lien file to Content Server
	 * 3. Trigger Run flow using Content Id and BatchName
	 * 4. Check 'Load Batch' Status
	 * 5. Check 'Transfer Batch' status
	 * Repeat 3,4 and 5 steps for multiple events.
	 * 6. Run Notification Report
	 * 
	 */
	public File handleFile(File inputFile) throws Exception {
		
		// Input File Name
		String fileName = getFileName(inputFile);
		MDC.put("transId", fileName);
		logger.info("Lien Notification File processing STARTED. File Name: " + inputFile.getAbsolutePath());
		
		
		String clientId = getClientId(fileName);
		logger.info("The client id in Lien  is :" + clientId);
		
		
		// Retrieve Client Configuration
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		if(config==null){
			logger.error("ClientConfiguration set-up is missing. Client Id:"+clientId);
			return null;
		}
		
		// Create Processing Directory
		String prcsngDir = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienProcessedDir();
		
		// Create .done and .processing files
		String dirName = FilenameUtils.getBaseName(fileName);
		File inProcessingFile = new File(prcsngDir + dirName+".processing");
		File doneFile = new File(prcsngDir + dirName+".done");
		File errorFile = new File(prcsngDir + dirName+".error");
		
		// Check if the file is getting processed or is already processed.
		if(inProcessingFile.exists() || doneFile.exists() || errorFile.exists()) {
			logger.warn("Processing/Done/Error file found, stop processing file.");
			return null;
		}
		
		// Create in processing file
		inProcessingFile.createNewFile();

		try {
			
			//Step1. Remove header and footer and copy file to work dir
			String directory = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienWorkDir();
			createStrippedFile(inputFile, fileName, directory);
			String absoluteStrippedFileName = directory.concat(fileName);
			final File strippedWorkFile = new File(absoluteStrippedFileName);
			logger.info("STEP1: Lien file stripping completed. File Name: "+ strippedWorkFile.getAbsolutePath());
			
			
			// Step2 : Upload the file to the content server
			WebContentUploadDetails uploadDtl = config.getWebContentUploadDtl();
			String contentId  = webContentUpload.uploadContent(uploadDtl.getWebContentUrl(), uploadDtl.getWebContentUserName(), 
					uploadDtl.getWebContentPwd(), strippedWorkFile.getAbsolutePath());
			logger.info("STEP2: Upload Lien file to Content Server completd. ContentId: "+contentId);
			
			
			//Step 3,4,5:Trigger Run flow and Check status for 'Load Batch' and 'Transfer Batch'
			List<String> batchNames = processSubmitFlowAndCheckOnStatus(contentId, config, clientId);
			
			
			
			// Step 6: Invoke the notification flow (Run Notification Report)
			if(!batchNames.isEmpty()) {			
				notifEngine.invokeBatchNotificationFlow(dirName, batchNames, clientId);		
			}
					
			doneFile.createNewFile();
			
		} catch (Exception exc) {
			logger.error(" Error in handleFile ", exc);
			errorFile.createNewFile();
		} finally {	
			inProcessingFile.delete();
		}
			
	    
	    
	    cleanUpFile(inputFile);
		
		MDC.clear();
		return null;
	}
	
	
	
	

	/**
	 * Before sending the file over to Oracle Content Server, header and footer from the file 
	 * has to be stripped of. This is what this method does.
	 * @param inputFile
	 * @param fileName
	 * @param directory
	 * @throws IOException
	 */
	public void createStrippedFile(File inputFile, String fileName, String directory) throws IOException {
		// Strip off header and footer.
		Scanner fileScanner = new Scanner(inputFile);
		
		logger.info("Strip off header and footer in Lien File");
		
		new File(directory).mkdirs();
		java.io.FileWriter fileStream = new java.io.FileWriter(directory + fileName);
		BufferedWriter out = new BufferedWriter(fileStream);
		int i = 0;
		
		while(fileScanner.hasNextLine()) {
		    String next = fileScanner.nextLine();
		    logger.debug("next is " + next);
		    if(i == 0){
		    	i++;
		    	continue;
		    }
		    
		    if(next.equals("\n")) {
		    	out.newLine();
		    }else if (fileScanner.hasNextLine()) {
		    	logger.debug("Writing next is " + next);
		    	 out.write(next);
		    	 out.newLine(); 
		    }   
		}

		out.close();
		fileStream.close();
		fileScanner.close();
		
	}
	

	/*
	 * Trigger Run flow using Content Id and BatchName, and invoke Check Load and Transfer Batch status 
	 * Repeat this for 'LienInfoTransformation' and 'LienAddntlInfoTransformation' steps
	 */
	private List<String> processSubmitFlowAndCheckOnStatus(String contentId, ClientConfiguration config,  String clientId) throws Exception {
		logger.info("invokeSubmitFlowAndCheckOnStatus START. contentId:"+contentId);
		
		List<String> batchNames = new ArrayList<>();
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();

	
		//Invoke Submit Flow for 'Lien Info Transformation'
		Map<String, String> batchData = batchLoadTask.invokeSubmitFlow(contentId, jobDtl.getLienInfoTransformationFormula(), clientId);
		logger.info("Lien Info Transformation Submit Flow Completed");
		
		//Get  Flow Status for 'Batch Load' and "Transfer Batch'
		String flowInstanceName = batchData.get("flowInstanceName");
		String batchName = batchData.get("batchName");
		Map<String, String> flowStatusResponse = getFlowTaskInstanceStatusForBatches(flowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId);
		logger.info("Lien Info Transformation batch status:"+ReflectionToStringBuilder.toString(flowStatusResponse));
		
		
		
		//Repeat process for  'Lien Additional Info Transformation'
		String batchLoadStatus = flowStatusResponse.get("batchload");
		if(batchLoadStatus.equalsIgnoreCase("SUCCESS")) {
			
			//Invoke Submit Flow for Lien Additional Info Transformation
			Map<String, String> addntlInfoBatchData = batchLoadTask.invokeSubmitFlow(contentId, jobDtl.getLienAddntlInfoTransformationFormula(), clientId);
			logger.info("Lien Additional Info Transformation Submit Flow Response:"+ ReflectionToStringBuilder.toString(batchData));
			
			String addntlInfoFlowInstanceName = addntlInfoBatchData.get("flowInstanceName");
			Map<String, String> addntlInfoFlowStatusResponse = getFlowTaskInstanceStatusForBatches(addntlInfoFlowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId);
			logger.info("Lien Additional Info Transformation batch status:"+ReflectionToStringBuilder.toString(addntlInfoFlowStatusResponse));
			
			batchNames.add(batchName);
		}
		logger.info("invokeSubmitFlowAndCheckOnStatus END. Response:"+ReflectionToStringBuilder.toString(batchNames));
		
		return batchNames;

	}
	
	
	private String getFileName(File input) {
		return input.getName();
	}
	
	private String getClientId(String fileName) {
		return fileName.substring(0, 4).toLowerCase();
	}

	
	/*
	 * Cleanup files
	 */
	private void cleanUpFile(File inputFile) throws IOException {
		// Move the file to archive folder and remove the file from the inbound directory
		String archivalDirectory = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienArchiveDir();
		FileMover.handleFile(inputFile, archivalDirectory);
		FileMover.removeFile(inputFile);
	}

	
	

	/*
	 * Get Submit Flow Status for 'Load Batch' and 'Transfer Batch' using Flow Name
	 */
	private Map<String, String> getFlowTaskInstanceStatusForBatches(String flowInstanceName, String legislativeDataGroupName, String clientId) throws Exception {
		Map<String, String> finaResult = new HashMap<>();
		finaResult.put("batchload", "FAILED");
		finaResult.put("transferbatch", "FAILED");
		
		//Get 'Batch Load' task status
		String batchLoadResult = batchLoadTask.getFlowTaskInstanceStatus(flowInstanceName, legislativeDataGroupName, BatchLoadTaskConstants.BATCHLOAD_FLOWTASKINSTANCENAME, clientId);
		logger.info("Batch Load Flow status :" + batchLoadResult);
		
		//Check 'Batch Load' status is completed or Not
		if (batchLoadResult.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
			finaResult.put("batchload", "SUCCESS");
			
			// Lets Check for TransferBatch Status
			String transferBatchResult = batchLoadTask.getFlowTaskInstanceStatus(flowInstanceName, legislativeDataGroupName, BatchLoadTaskConstants.TRANSFERBATCH_FLOWTASKINSTANCENAME, clientId);
			logger.info("Transfer Batch Flow Status: " +transferBatchResult);
			
			if (transferBatchResult.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
				finaResult.put("transferbatch", "SUCCESS");
			}
		}

		return finaResult;
	}

	
	
	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ClientConfigHolder getClientConfigurations() {
		return clientConfigurations;
	}

	public void setClientConfigurations(ClientConfigHolder clientConfigurations) {
		this.clientConfigurations = clientConfigurations;
	}

	public NotificationEngine getNotifEngine() {
		return notifEngine;
	}

	public void setNotifEngine(NotificationEngine notifEngine) {
		this.notifEngine = notifEngine;
	}

	
	public void setWebContentUpload(WebContentUpload webContentUpload) {
		this.webContentUpload = webContentUpload;
	}

	public WebContentUpload getWebContentUpload() {
		return webContentUpload;
	}
	

	public BatchLoadTask getBatchLoadTask() {
		return batchLoadTask;
	}

	public void setBatchLoadTask(BatchLoadTask batchLoadTask) {
		this.batchLoadTask = batchLoadTask;
	}
	
}