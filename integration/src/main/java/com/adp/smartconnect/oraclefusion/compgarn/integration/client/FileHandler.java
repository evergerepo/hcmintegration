package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTask;
import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTaskConstants;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.WebContentUploadDetails;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.contentupload.WebContentUpload;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

import oracle.stellent.ridc.IdcClientException;

public class FileHandler implements ApplicationContextAware {
				
	private NotificationEngine notifEngine = null;
	
	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
	private Configuration configuration;
	
	private ClientConfigHolder clientConfigurations;
	
	private ApplicationContext appCtx;
	
	private FileMover fileMover;
	
	/**
	 * Before sending the file over to Oracle Content Server, header and footer from the file 
	 * has to be stripped of. This is what this method does.
	 * @param inputFile
	 * @param fileName
	 * @param directory
	 * @throws IOException
	 */
	private void createStrippedFile(File inputFile, String fileName, String directory) throws IOException {


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
	
	private String uploadFileToContentServer(File file, ClientConfiguration config) throws FileNotFoundException, IllegalArgumentException, IdcClientException {
		
		WebContentUploadDetails uploadDtl = config.getWebContentUploadDtl();
		WebContentUpload upload = (WebContentUpload)this.appCtx.getBean("webContentUpload");
		upload.setClientUrl(uploadDtl.getWebContentUrl());
		upload.setPassword(uploadDtl.getWebContentPwd());
		upload.setUserName(uploadDtl.getWebContentUserName());
		return upload.uploadContent(file.getAbsolutePath());
		
	}
	
	private List<String> invokeSubmitFlowAndCheckOnStatus(String contentId, ClientConfiguration config, File child, String clientId) throws Exception {
		
		List<String> batchNames = new ArrayList<>();
		Map<String, String> flowStatusResponse = null;
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();
		
		// Get the BatchLoad Task
		BatchLoadTask loadTask = (BatchLoadTask)this.appCtx.getBean("loadTask");
		
		Map<String, String> batchData = invokeSubmitFlow(contentId, jobDtl.getLienInfoTransformationFormula(), clientId, loadTask);
		String flowInstanceName = batchData.get("flowInstanceName");
		String batchName = batchData.get("batchName");
		flowStatusResponse = getSubmitFlowStatus(flowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId, loadTask);
		logger.info("The flow status response for file " + child.getName() + " and the batch name "+ batchName +" is " 
		+ flowStatusResponse);
		
		// Invoke notification flow for the files for which load is successful
		String batchLoadStatus = flowStatusResponse.get("batchload");
		
		if(batchLoadStatus.equalsIgnoreCase("SUCCESS")) {
			Map<String, String> addntlInfoBatchData = invokeSubmitFlow(contentId, jobDtl.getLienAddntlInfoTransformationFormula(), clientId, loadTask);
			String addntlInfoFlowInstanceName = addntlInfoBatchData.get("flowInstanceName");
			String addntlInfoBatchName = addntlInfoBatchData.get("batchName");
			Map<String, String> addntlInfoFlowStatusResponse = getSubmitFlowStatus(addntlInfoFlowInstanceName, jobDtl.getLegislativeDataGroupName(), clientId, loadTask);
			logger.info("addntlInfoFlowInstanceName:The flow status response for file" + child.getName() + "and the batch name"+ addntlInfoBatchName +" is " 
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

	public File handleFile(File inputFile) throws Exception {
		
		// Input File Name
		String fileName = getFileName(inputFile);
		MDC.put("transactionID", fileName);
		String clientId = getClientId(fileName);
		logger.info("The client name is " + clientId);
		
		// Retrieve Client Configuration
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		
		// Create Processing Directory
		String prcsngDir = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienProcessedDir();
		
		// Create .done and .processing files
		String dirName = FilenameUtils.getBaseName(fileName);
		File inProcessingFile = new File(prcsngDir + dirName+".processing");
		File doneFile = new File(prcsngDir + dirName+".done");
		File errorFile = new File(prcsngDir + dirName+".error");
		
		// Check if the file is getting processed or is already processed.
		if(inProcessingFile.exists() || doneFile.exists() || errorFile.exists()) {
			return null;
		}
		
		//Create directory
		String directory = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienWorkDir() ;
		
		//Remove header and footer
		createStrippedFile(inputFile, fileName, directory);
		String absoluteStrippedFileName = directory.concat(fileName);
		
		// Create in processing file
		inProcessingFile.createNewFile();
		
		// Loop over files in the directory
		final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("files", "txt","grn");
		
		final File child = new File(absoluteStrippedFileName);
		String batchName = null;
		List<String> batchNames = new ArrayList<>();

		logger.info("The child file is " + child.getAbsolutePath());
		
	    if(extensionFilter.accept(child)) {
			
			try {
				// Upload the file to the content server
				String contentId = uploadFileToContentServer(child, config);
				
				batchNames = invokeSubmitFlowAndCheckOnStatus(contentId, config, child, clientId);
				
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
			
	    }
	    
	    cleanUpFile(inputFile);
		
		MDC.clear();
		return null;
	}
	
	private void cleanUpFile(File inputFile) throws IOException {
		// Move the file to archive folder and remove the file from the inbound directory
		String archivalDirectory = configuration.getFileProcessingDir() + configuration.getLienDr() + configuration.getLienArchiveDir();
		fileMover.handleFile(inputFile, archivalDirectory);
		fileMover.removeFile(inputFile);
	}

	private Map<String, String> invokeSubmitFlow(String contentId, String flowName, String clientId, BatchLoadTask loadTask) throws Exception {
		Map<String, String> output = new HashMap<>();
		//String flowName = BatchLoadTaskConstants.ADP_FLOW_NAME;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(now);
		
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();
		logger.info("Client Name in invokeSubmitFlow is " + clientId);

		String flowInstanceName = flowName + formattedDate;
		String batchName = BatchLoadTaskConstants.ADP_BATCH_NAME + formattedDate;
		output.put("batchName", batchName);
		logger.info("Batch Name is " + batchName);
		
		boolean recurringFlag = false;
		logger.info("Start:Invoking SubmitFlow for contentId " + contentId + " and flowInstanceName is "
				+ flowInstanceName);
		loadTask.setUrl(jobDtl.getNotificationJobSubmitFlowUrl());
		loadTask.setClientId(clientId);
		loadTask.submitTransformationFormulaFlow(flowName, batchName, flowInstanceName, jobDtl.getLegislativeDataGroupName(), recurringFlag,
				contentId);
		output.put("flowInstanceName", flowInstanceName);
		logger.info("End:Invoking SubmitFlow for contentId " + contentId);
		return output;
	}

	private String getSubmitFlowStatus(String flowInstanceName, String legislativeDataGroupName,
			String flowTaskInstanceName, String clientId, BatchLoadTask loadTask) throws Exception {
		String finalResult = "FAILED";
		int totalWaitTime = 0;
		
		ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
		NotificationJobDtl jobDtl = config.getNotificationJobDtl();
		// TODO Need to handle time out scenario as well.
		while (true) {
			
			logger.info("getInstanceTaskStatus Start for flowInstanceName " + flowInstanceName
					+ " and flowTaskInstanceName " + flowTaskInstanceName);
			loadTask.setUrl(jobDtl.getNotificationJobGetFlowStatusUrl());
			loadTask.setClientId(clientId);
			String result = loadTask.getInstanceTaskStatus(flowInstanceName, flowTaskInstanceName, legislativeDataGroupName);
			logger.info("getInstanceTaskStatus End " + result);
			
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
				logger.info("Lets retry to check status for " + flowInstanceName + "because the status received is " + result);
				Thread.sleep(Integer.parseInt(configuration.getWaitTime()));
				totalWaitTime = totalWaitTime + Integer.parseInt(configuration.getWaitTime());
			}

			if (totalWaitTime >= (Integer.parseInt(configuration.getMaxWaitTime()))) {
				break;
			}
			
		}
		return finalResult;
	}

	private Map<String, String> getSubmitFlowStatus(String flowInstanceName, String legislativeDataGroupName, String clientId, BatchLoadTask loadTask) throws Exception {
		Map<String, String> finaResult = new HashMap<>();
		finaResult.put("batchload", "FAILED");
		finaResult.put("transferbatch", "FAILED");
		
		// TODO Need to handle time out scenario as well.
		String result = getSubmitFlowStatus(flowInstanceName, legislativeDataGroupName,
				BatchLoadTaskConstants.BATCHLOAD_FLOWTASKINSTANCENAME, clientId, loadTask);
		logger.info(" Result after getSubmitFlowStatus call for batchLoad task instance " + result);
		if (result.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
			finaResult.put("batchload", "SUCCESS");
			// Lets Check for TransferBatch Status
			String result1 = getSubmitFlowStatus(flowInstanceName, legislativeDataGroupName,
					BatchLoadTaskConstants.TRANSFERBATCH_FLOWTASKINSTANCENAME, clientId, loadTask);
			logger.info(" Result after getSubmitFlowStatus call for transferBatch task instance " + result1);
			if (result1.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
				finaResult.put("transferbatch", "SUCCESS");
			}
		}

		return finaResult;
	}

	public static void main(String[] args) throws Exception {
		String path = FileHandler.class.getClassLoader().getResource("").getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		final File file = new File(path + "client");
		JAXBContext jaxbContext = JAXBContext.newInstance(ClientConfiguration.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		for (final File child : file.listFiles()) {
			ClientConfiguration configuration = (ClientConfiguration)jaxbUnMarshaller.unmarshal(child);
			ClientConfigHolder.addClientConfiguration(configuration.getClientName(), configuration);
		}
		
		FileHandler handler = new FileHandler();
		handler.handleFile(new File("/Users/abhisheksingh/ddrive/everge_ws/files/childSupportRecords-Write.txt"));
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;	
	}

	public FileMover getFileMover() {
		return fileMover;
	}

	public void setFileMover(FileMover fileMover) {
		this.fileMover = fileMover;
	}

}