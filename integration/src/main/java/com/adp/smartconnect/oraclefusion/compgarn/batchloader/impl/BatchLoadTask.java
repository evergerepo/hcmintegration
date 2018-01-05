package com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.APPConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatus;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatusResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlow;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlowResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.FlowParameterNameValues;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.ObjectFactory;



/*
 * https://docs.oracle.com/cloud/farel9/globalcs_gs/OESWH/Flow_Actions_Service_FlowActionsService_svc_4.htm#oracle.apps.hcm.processFlows.core.flowActionsService.FlowActionsService_OpSect
 * Flow Actions Service
 */
public class BatchLoadTask extends WebServiceGatewaySupport {
	
	private static final Logger log = LoggerFactory.getLogger(BatchLoadTask.class);
	
	public Map<String, String> invokeSubmitFlow(String contentId, String flowName, String clientId, String batchName) throws Exception {

		
		if(StringUtils.isNotBlank(contentId)) {
			log.info("invokeSubmitFlow Start. FlowName:"+flowName+", contentId:"+contentId);
		}else {
			log.info("invokeSubmitFlow Start. FlowName:"+flowName+", contentId is blank.");
		}
		
		Map<String, String> response = new HashMap<>();
		
		try{
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
			NotificationJobDtl jobDtl = config.getNotificationJobDtl();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String formattedDate = sdf.format(Calendar.getInstance().getTime());
			
			String flowInstanceName = flowName + formattedDate;
			if(StringUtils.isBlank(batchName)) {
				batchName = BatchLoadTaskConstants.ADP_BATCH_NAME + formattedDate;
			}

			response.put("batchName", batchName);
			response.put("flowInstanceName", flowInstanceName);
			
			
			//Build SUbmit Flow Request
			SubmitFlow submitFlowRequest = buildSubmitFlowRequest(flowName, batchName, flowInstanceName, jobDtl.getLegislativeDataGroupName(),false, contentId);
			
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
			getWebServiceTemplate().setMarshaller(marshaller);
			getWebServiceTemplate().setUnmarshaller(marshaller);
			
			log.info(">>>>>>>NotificationJobDtl :{}", ReflectionToStringBuilder.toString(jobDtl));

			//Load JKS Key into KeyStore
			Resource keyStore = new ClassPathResource(jobDtl.getNotificationJobKeyStorePath());
			String keyStorePwd = jobDtl.getNotificationJobKeyStorePwd();
			
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
			keyStore.getInputStream().close();
			
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
	        
	        //Build AuthenticatingUrlConnectionMessageSender request
	        AuthenticatingUrlConnectionMessageSender authenticatingUrlConnectionMessageSender = new AuthenticatingUrlConnectionMessageSender();
	        authenticatingUrlConnectionMessageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
	        authenticatingUrlConnectionMessageSender.setUsername(jobDtl.getNotificationJobUserName());
	        authenticatingUrlConnectionMessageSender.setPassword(jobDtl.getNotificationJobPassword());
	        authenticatingUrlConnectionMessageSender.afterPropertiesSet();
	        getWebServiceTemplate().setMessageSender(authenticatingUrlConnectionMessageSender);
	        authenticatingUrlConnectionMessageSender = null;

	        log.debug("Invoking SubmitFlow WebService");
        	SubmitFlowResponse flowResponse = (SubmitFlowResponse)getWebServiceTemplate().marshalSendAndReceive(jobDtl.getNotificationJobSubmitFlowUrl(), submitFlowRequest);
        	logger.info("invokeSubmitFlow End. FlowInstanceName:"+flowInstanceName+", BatchName:"+batchName+", Response:"+flowResponse.isResult());
        }catch(Exception e){
        	logger.error("invokeSubmitFlow Error. Message:"+e.getMessage(), e);
        	throw e;
        }
		return response;
		
	}
	

	/*
	 * Submits a flow instance.
	 * Invoke  Submit Flow using Content ID, Batch Name and Flow Name.
	 */
	public Map<String, String> invokeSubmitFlow(String contentId, String flowName, String clientId) throws Exception {
		return invokeSubmitFlow(contentId, flowName, clientId, null);
	}
	
	
	/*
	 * Build Submit Flow Request
	 */
	 private SubmitFlow buildSubmitFlowRequest(String flowName, String batchName, String flowInstanceName, 
				String legislativeDataGroupName, boolean recurringFlag, String contentId){
		 
		 	SubmitFlow submitFlowRequest = new SubmitFlow();
			submitFlowRequest.setFlowName(flowName);
			
			ObjectFactory factory = new ObjectFactory();
			
			if(StringUtils.isNotBlank(contentId)) {
				FlowParameterNameValues values1 = new FlowParameterNameValues();
				values1.setParameterName(factory.createFlowParameterNameValuesParameterName("Content Id"));
				values1.setParameterValue(factory.createFlowParameterNameValuesParameterValue(contentId));
				submitFlowRequest.getParameterValues().add(values1);
			}
			
			FlowParameterNameValues values2 = new FlowParameterNameValues();
			values2.setParameterName(factory.createFlowParameterNameValuesParameterName("Batch"));
			values2.setParameterValue(factory.createFlowParameterNameValuesParameterValue(batchName));
			submitFlowRequest.getParameterValues().add(values2);
			
			submitFlowRequest.setFlowInstanceName(flowInstanceName);
			submitFlowRequest.setLegislativeDataGroupName(legislativeDataGroupName);
			submitFlowRequest.setRecurringFlag(recurringFlag);
			
			return submitFlowRequest;
	 }
	
	 
	
	 /*
	  * Retrieves the flow task instance status.
	  * Get Flow Task Instance Task using FlowTaskInstanceName
	  * 
	  */
	public String  getFlowTaskInstanceStatus(String flowInstanceName, String legislativeDataGroupName, String flowTaskInstanceName, String clientId) throws Exception {
		 
		String finalResult = "FAILED";
		int totalWaitTime = 0;
		
		try{
			logger.info("Invoke SubmitFlowStatus for flowInstanceName [" +flowInstanceName+"] ,  flowTaskInstanceName["+ flowTaskInstanceName+"]");
			
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
			NotificationJobDtl jobDtl = config.getNotificationJobDtl();
			
			//Build GetFlowTaskInstanceStatus request
			GetFlowTaskInstanceStatus getFlowTaskInstanceStatus = new GetFlowTaskInstanceStatus();
			getFlowTaskInstanceStatus.setFlowInstanceName(flowInstanceName);
			getFlowTaskInstanceStatus.setFlowTaskInstanceName(flowTaskInstanceName);
			getFlowTaskInstanceStatus.setLegislativeDataGroupName(legislativeDataGroupName);
			
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
			getWebServiceTemplate().setMarshaller(marshaller);
			getWebServiceTemplate().setUnmarshaller(marshaller);
			
			//Secure WebService Client	
			Resource keyStore = new ClassPathResource(jobDtl.getNotificationJobKeyStorePath());
			KeyStore ks = KeyStore.getInstance("JKS");
			
			String keyStorePwd = jobDtl.getNotificationJobKeyStorePwd();
			ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
			keyStore.getInputStream().close();
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
	        
	        AuthenticatingUrlConnectionMessageSender messageSender = new AuthenticatingUrlConnectionMessageSender();
	        messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
	        messageSender.setUsername(jobDtl.getNotificationJobUserName());
	        messageSender.setPassword(jobDtl.getNotificationJobPassword());
	        messageSender.afterPropertiesSet();
	        getWebServiceTemplate().setMessageSender(messageSender);
			

	        //Invoke Web Service
	        while (true) {
	        	 GetFlowTaskInstanceStatusResponse response  = (GetFlowTaskInstanceStatusResponse)getWebServiceTemplate().marshalSendAndReceive(jobDtl.getNotificationJobGetFlowStatusUrl(), getFlowTaskInstanceStatus);
	        	 String result  = response.getResult();
				
	        	 if(result.equalsIgnoreCase(BatchLoadTaskConstants.ESS_PARENT_JOB_SUB_ERROR)) {
					break;
				}else if (result.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_ERROR_STATUS)) {
					finalResult = result;
					break;
				} else if (result.equalsIgnoreCase(BatchLoadTaskConstants.BATCHLOAD_COMPLETETD_STATUS)) {
					finalResult = result;
					break;
				} else {
					logger.info("Lets retry to check status for " + flowInstanceName + "because the status received is [" +result+"]");
					Thread.sleep(Integer.parseInt(APPConfigHolder.getWaitTime()));
					totalWaitTime = totalWaitTime + Integer.parseInt(APPConfigHolder.getWaitTime());
				}

				if (totalWaitTime >= (Integer.parseInt(APPConfigHolder.getMaxWaitTime()))) {
					break;
				}
				
			}

	        logger.info("SubmitFlowStatus for flowInstanceName [" +flowInstanceName+"] ,  flowTaskInstanceName["+ flowTaskInstanceName+"], Response:["+finalResult+"]");
        }catch(Exception e){
        	logger.error("GetFlowTaskInstanceStatus webservice error. Message:"+e.getMessage(), e);
        }
		return finalResult;
	}
	

	
	private ClientConfigHolder clientConfigurations;
	private Configuration configuration;
	
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
