package com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatus;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatusResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlow;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlowResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.FlowParameterNameValues;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.ObjectFactory;

public class BatchLoadTask extends WebServiceGatewaySupport {
	
	private static final Logger log = LoggerFactory.getLogger(BatchLoadTask.class);
	
	
	private ClientConfigHolder clientConfigurations;

	

	/**
	 * submitTransformationFormulaFlow
	 * @param flowName
	 * @param batchName
	 * @param flowInstanceName
	 * @param legislativeDataGroupName
	 * @param recurringFlag
	 * @param contentId
	 * @throws Exception
	 */
	public void invokeSubmitFlow(String flowName, String batchName, String flowInstanceName, 
			String legislativeDataGroupName, boolean recurringFlag, String contentId, String clientId, String url) throws Exception {
		
		//Build SUbmit Flow Request
		SubmitFlow submitFlowRequest = buildSubmitFlowRequest(flowName, batchName, flowInstanceName, legislativeDataGroupName,recurringFlag, contentId);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
		getWebServiceTemplate().setMarshaller(marshaller);
		getWebServiceTemplate().setUnmarshaller(marshaller);
		
		// Get the client configuration
		ClientConfiguration clientConfiguration = clientConfigurations.getSingleClientData(clientId);
		NotificationJobDtl config = clientConfiguration.getNotificationJobDtl();
		
		//Load JKS Key into KeyStore
		Resource keyStore = new ClassPathResource(config.getNotificationJobKeyStorePath());
		String keyStorePwd = config.getNotificationJobKeyStorePwd();
		
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
		keyStore.getInputStream().close();
		
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
        
        //Build AuthenticatingUrlConnectionMessageSender request
        AuthenticatingUrlConnectionMessageSender authenticatingUrlConnectionMessageSender = new AuthenticatingUrlConnectionMessageSender();
        authenticatingUrlConnectionMessageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
        authenticatingUrlConnectionMessageSender.setUsername(config.getNotificationJobUserName());
        authenticatingUrlConnectionMessageSender.setPassword(config.getNotificationJobPassword());
        authenticatingUrlConnectionMessageSender.afterPropertiesSet();
        getWebServiceTemplate().setMessageSender(authenticatingUrlConnectionMessageSender);
        authenticatingUrlConnectionMessageSender = null;
        
        try{
        	log.info("SubmitFlow webservice Start");
        	SubmitFlowResponse flowResponse = (SubmitFlowResponse)getWebServiceTemplate().marshalSendAndReceive(url, submitFlowRequest);
        	log.info("SubmitFlow webservice End. Response:"+flowResponse.isResult());
        }catch(Exception e){
        	logger.error("SubmitFlow webservice error. Message:"+e.getMessage(), e);
        }
	}
	
	
	/*
	 * Build Submit Flow Request
	 */
	 private SubmitFlow buildSubmitFlowRequest(String flowName, String batchName, String flowInstanceName, 
				String legislativeDataGroupName, boolean recurringFlag, String contentId){
		 
		 	SubmitFlow submitFlowRequest = new SubmitFlow();
			submitFlowRequest.setFlowName(flowName);
			
			ObjectFactory factory = new ObjectFactory();
			
			FlowParameterNameValues values1 = new FlowParameterNameValues();
			values1.setParameterName(factory.createFlowParameterNameValuesParameterName("Content Id"));
			values1.setParameterValue(factory.createFlowParameterNameValuesParameterValue(contentId));
			
			FlowParameterNameValues values2 = new FlowParameterNameValues();
			values2.setParameterName(factory.createFlowParameterNameValuesParameterName("Batch"));
			values2.setParameterValue(factory.createFlowParameterNameValuesParameterValue(batchName));
			
			submitFlowRequest.getParameterValues().add(values1);
			submitFlowRequest.getParameterValues().add(values2);
			submitFlowRequest.setFlowInstanceName(flowInstanceName);
			submitFlowRequest.setLegislativeDataGroupName(legislativeDataGroupName);
			submitFlowRequest.setRecurringFlag(recurringFlag);
			
			return submitFlowRequest;
	 }
	
	
	 /*
	  * Get Job Task Status
	  */
	public String getInstanceTaskStatus(String flowInstanceName, String flowTaskInstanceName, String legislativeDataGroupName, String clientId, String url) throws Exception {
		
		GetFlowTaskInstanceStatusResponse response = null;
		try{
			log.info("GetFlowTaskInstanceStatus webservice Start");
			GetFlowTaskInstanceStatus getFlowTaskInstanceStatus = new GetFlowTaskInstanceStatus();
			getFlowTaskInstanceStatus.setFlowInstanceName(flowInstanceName);
			getFlowTaskInstanceStatus.setFlowTaskInstanceName(flowTaskInstanceName);
			getFlowTaskInstanceStatus.setLegislativeDataGroupName(legislativeDataGroupName);
			
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
			getWebServiceTemplate().setMarshaller(marshaller);
			getWebServiceTemplate().setUnmarshaller(marshaller);
			
			// Get the client configuration
			ClientConfiguration clientConfiguration = clientConfigurations.getSingleClientData(clientId);
			NotificationJobDtl config = clientConfiguration.getNotificationJobDtl();
			
			Resource keyStore = new ClassPathResource(config.getNotificationJobKeyStorePath());
			KeyStore ks = KeyStore.getInstance("JKS");
			
			String keyStorePwd = config.getNotificationJobKeyStorePwd();
			
			ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
			keyStore.getInputStream().close();
			
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
	        
	        AuthenticatingUrlConnectionMessageSender messageSender = new AuthenticatingUrlConnectionMessageSender();
	        messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
	
	        messageSender.setUsername(config.getNotificationJobUserName());
	        messageSender.setPassword(config.getNotificationJobPassword());
	        messageSender.afterPropertiesSet();
	        getWebServiceTemplate().setMessageSender(messageSender);
			
        
        	response = (GetFlowTaskInstanceStatusResponse)getWebServiceTemplate().marshalSendAndReceive(url, getFlowTaskInstanceStatus);
        	
        	log.info("GetFlowTaskInstanceStatus Sucess. Response:"+response.getResult());
        }catch(Exception e){
        	logger.error("GetFlowTaskInstanceStatus webservice error. Message:"+e.getMessage(), e);
        }
		return response.getResult();
	}
	

	public ClientConfigHolder getClientConfigurations() {
		return clientConfigurations;
	}

	public void setClientConfigurations(ClientConfigHolder clientConfigurations) {
		this.clientConfigurations = clientConfigurations;
	}


}
