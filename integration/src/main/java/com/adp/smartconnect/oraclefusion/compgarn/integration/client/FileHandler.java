package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	 * Invoke Submit Flow, after that Status Check
	 */
	private List<String> invokeSubmitFlowAndCheckOnStatus(String contentId, ClientConfiguration config,  String clientId) throws Exception {
		
		List<String> batchNames = new ArrayList<>();
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();

		//Invoke Submit Flow
		Map<String, String> batchData = invokeSubmitFlow(contentId, jobDtl.getLienInfoTransformationFormula(), clientId);
		logger.info("Submit Flow Response:"+ ReflectionToStringBuilder.toString(batchData));
		
		//Get Submit FLow Status
		String flowInstanceName = batchData.get("flowInstanceName");
		String batchName = batchData.get("batchName");
		Map<String, String> flowStatusResponse = getSubmitFlowStatusForBatch(flowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId);
		logger.info("The flow status response for  contentId ["+contentId+"] and the batch name ["+ batchName +"] is " + flowStatusResponse);
		
		// Invoke notification flow for the files for which load is successful
		String batchLoadStatus = flowStatusResponse.get("batchload");
		if(batchLoadStatus.equalsIgnoreCase("SUCCESS")) {
			Map<String, String> addntlInfoBatchData = invokeSubmitFlow(contentId, jobDtl.getLienAddntlInfoTransformationFormula(), clientId);
			String addntlInfoFlowInstanceName = addntlInfoBatchData.get("flowInstanceName");
			String addntlInfoBatchName = addntlInfoBatchData.get("batchName");
			
			Map<String, String> addntlInfoFlowStatusResponse = getSubmitFlowStatusForBatch(addntlInfoFlowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId);
			logger.info("addntlInfoFlowInstanceName:The flow status response for contentId ["+contentId+"] and the batch name ["+ addntlInfoBatchName +"] is " 
												+ addntlInfoFlowStatusResponse);
			
			batchNames.add(batchName);
		}
		
		return batchNames;

	}
	
	private String getFileName(File input) {
		return input.getName();
	}
	
	private String getClientId(String fileName) {
		return fileName.substring(0, 4).toLowerCase();
	}

	
	/*
	 * Handle Input Lien Notification File
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
				
				
		//Remove header and footer and copy file to work dir
		String directory = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienWorkDir();
		createStrippedFile(inputFile, fileName, directory);
		String absoluteStrippedFileName = directory.concat(fileName);
		
		final File strippedWorkFile = new File(absoluteStrippedFileName);
		logger.info("Lien header stripped file name: "+ strippedWorkFile.getAbsolutePath());
		
	   
		try {
			// Upload the file to the content server
			WebContentUploadDetails uploadDtl = config.getWebContentUploadDtl();
			String contentId  = webContentUpload.uploadContent(uploadDtl.getWebContentUrl(), uploadDtl.getWebContentUserName(), 
					uploadDtl.getWebContentPwd(), strippedWorkFile.getAbsolutePath());
			
			//Check status for 'Load Batch' and 'Transfer Batch'
			List<String> batchNames = invokeSubmitFlowAndCheckOnStatus(contentId, config, clientId);
			
			// Invoke the notification flow
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
	 * Invoke Lien Submit File using Content ID and Flow Name.
	 */
	public Map<String, String> invokeSubmitFlow(String contentId, String flowName, String clientId) throws Exception {
		Map<String, String> response = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(Calendar.getInstance().getTime());
		
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();
		
		String flowInstanceName = flowName + formattedDate;
		String batchName = BatchLoadTaskConstants.ADP_BATCH_NAME + formattedDate;
		response.put("batchName", batchName);
		response.put("flowInstanceName", flowInstanceName);
		
		logger.info("Start:Invoking SubmitFlow. ClientId:"+ clientId+", FlowInstanceName:"+flowInstanceName+", ContentId:"+contentId+", BatchName:"+batchName);
		batchLoadTask.invokeSubmitFlow(flowName, batchName, flowInstanceName, jobDtl.getLegislativeDataGroupName(), false,
				contentId,  clientId,  jobDtl.getNotificationJobSubmitFlowUrl());
		logger.info("End: SubmitFlow. ClientId:"+ clientId+", FlowInstanceName:"+flowInstanceName+", ContentId:"+contentId+", BatchName:"+batchName);
		
		return response;
	}

	
	/*
	 * Get Submit Flow Status for given 'Flow Task'
	 */
	private String getSubmitFlowStatus(String flowInstanceName, String legislativeDataGroupName, String flowTaskInstanceName, String clientId) throws Exception {
		String finalResult = "FAILED";
		int totalWaitTime = 0;
		
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();
		
		logger.info("Invoke SubmitFlowStatus for flowInstanceName [" +flowInstanceName+"] ,  flowTaskInstanceName["+ flowTaskInstanceName+"]");
		
		// TODO Need to handle time out scenario as well.
		while (true) {
			
			String result = batchLoadTask.getInstanceTaskStatus(flowInstanceName, flowTaskInstanceName, legislativeDataGroupName, clientId,  jobDtl.getNotificationJobGetFlowStatusUrl());
			
			if(result.equalsIgnoreCase(BatchLoadTaskConstants.ESS_PARENT_JOB_SUB_ERROR)) {
				break;
			}

			if (result.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_ERROR_STATUS)) {
				finalResult = result;
				break;
			} else if (result.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
				finalResult = result;
				break;
			} else {
				logger.info("Lets retry to check status for " + flowInstanceName + "because the status received is [" +result+"]");
				Thread.sleep(Integer.parseInt(configuration.getWaitTime()));
				totalWaitTime = totalWaitTime + Integer.parseInt(configuration.getWaitTime());
			}

			if (totalWaitTime >= (Integer.parseInt(configuration.getMaxWaitTime()))) {
				break;
			}
			
		}
		
		logger.info("SubmitFlowStatus for flowInstanceName [" +flowInstanceName+"] ,  flowTaskInstanceName["+ flowTaskInstanceName+"], Response:["+finalResult+"]");
		return finalResult;
	}

	/*
	 * Get Submit Flow Status for 'Batch Load' and 'Transfer Batch'
	 */
	private Map<String, String> getSubmitFlowStatusForBatch(String flowInstanceName, String legislativeDataGroupName, String clientId) throws Exception {
		Map<String, String> finaResult = new HashMap<>();
		finaResult.put("batchload", "FAILED");
		finaResult.put("transferbatch", "FAILED");
		
		// TODO Need to handle time out scenario as well.
		String batchLoadResult = getSubmitFlowStatus(flowInstanceName, legislativeDataGroupName, BatchLoadTaskConstants.BATCHLOAD_FLOWTASKINSTANCENAME, clientId);
		logger.info(" Result after getSubmitFlowStatus call for batchLoad task instance " + batchLoadResult);
		
		//Check 'Batch Load' status is completed or Not
		if (batchLoadResult.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
			finaResult.put("batchload", "SUCCESS");
			
			// Lets Check for TransferBatch Status
			String transferBatchResult = getSubmitFlowStatus(flowInstanceName, legislativeDataGroupName, BatchLoadTaskConstants.TRANSFERBATCH_FLOWTASKINSTANCENAME, clientId);
			logger.info(" Result after getSubmitFlowStatus call for transferBatch task instance " + transferBatchResult);
			
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