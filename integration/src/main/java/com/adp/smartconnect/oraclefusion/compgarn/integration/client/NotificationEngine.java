package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.service.JobTrackingService;

import bip_webservice.proxy.types.ArrayOfParamNameValue;
import bip_webservice.proxy.types.ArrayOfString;
import bip_webservice.proxy.types.ParamNameValue;

public class NotificationEngine {
	
	private String format = "text";
	
	private Configuration configuration;
		
	private ClientConfigHolder clientConfigurations;
	@Autowired  JobTrackingService jobTrackingService;
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationEngine.class);

	/*
	 * Batch Notification Flow
	 */
	public void invokeBatchNotificationFlow(String dirName, List<String> batchNames, String clientName, String transId, String jobStepId) throws Exception {

		try{
			// Retrieve Client Configuration
			ClientConfiguration clientConfiguration = clientConfigurations.getSingleClientData(clientName);
			NotificationJobDtl config = clientConfiguration.getNotificationJobDtl();
	
			String notificationInpDir = configuration.getFileProcessingDir() + configuration.getLienDr()+ configuration.getLienOutboundDir();
			logger.info("Notification Output Directory is :" + notificationInpDir);
			String notificationReportLocation = config.getNotificationReportPath();
			
			for (String name : batchNames) {
				invokeNotificationFlow(clientConfiguration, name, notificationInpDir, notificationReportLocation, clientName,  transId,  jobStepId);
			}
		}catch(Exception exc){
			logger.error("Error Generating Notification Report. Message:"+exc.getMessage(), exc);
			jobTrackingService.trackException(transId,  jobStepId, "Notification Report Error", "Notification Report Error. Exception: "+exc.getMessage());
			throw exc;
		}
	}
	
	private void invokeNotificationFlow(ClientConfiguration clientConfiguration, String batchName, String notificationInpDir, 
			String notificationReportLocation, String clientName, String transId, String jobStepId) throws Exception {
		
		logger.info("Start:invokeNotificationFlow for batch name is " + batchName);
		ArrayOfString value = new ArrayOfString();
        value.getItem().add(batchName);
    
        ParamNameValue reportPrompt = new ParamNameValue();
        reportPrompt.setName("batch_name");
        reportPrompt.setValues(value);
        
        ArrayOfParamNameValue paramNameValue = new ArrayOfParamNameValue();
        paramNameValue.getItem().add(reportPrompt);
        ReportServiceClient ServiceClient = new ReportServiceClient();
        ServiceClient.setUrl(clientConfiguration.getNotificationJobDtl().getReportServiceUrl());
        
		// Retrieve Client Configuration
		NotificationJobDtl config = clientConfiguration.getNotificationJobDtl();
        
		// Create Notification File Name
		String outputFormat = config.getNotificationOutputFormat();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		java.util.Date now = calendar.getTime();
		String formattedDate = sdf.format(now);
		String outputFile = outputFormat.replaceAll("xxx", clientName).replaceAll("yyyymmddhhmmss", formattedDate);
        
        String reportPath = notificationInpDir + "/"+ outputFile;
        ServiceClient.callRunReport(notificationReportLocation, paramNameValue, config.getNotificationJobUserName(), config.getNotificationJobPassword(), format, null, reportPath);

        jobTrackingService.trackActivity(transId, jobStepId, "Lien Notification Response","Lien Notification Response File Generated. File Name:["+outputFile+"]");
        
        //Keep Response file as backup in Archive Folder
        String archiveDir = configuration.getFileProcessingDir() + configuration.getLienDr()+configuration.getLienArchiveDir();
		FileMover.copyFile(notificationInpDir, archiveDir, outputFile);
		logger.info("Archive Lien Notification File: {}", archiveDir+"/"+outputFile);
		 
        logger.info("End:Report Path is {}", reportPath);
		
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
	
}
