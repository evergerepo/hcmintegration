package com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.KeyManagerFactory;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.NotificationJobDtl;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatus;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.GetFlowTaskInstanceStatusResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlow;
import com.oracle.xmlns.apps.hcm.processflows.core.flowactionsservice.types.SubmitFlowResponse;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.FlowParameterNameValues;
import com.oracle.xmlns.apps.hcm.processflows.core.flowcontrollerservice.ObjectFactory;

public class BatchLoadTask extends WebServiceGatewaySupport {
	
	private static final Logger log = LoggerFactory.getLogger(BatchLoadTask.class);
	
	private String url;
	
	private String clientId;
	
	private ClientConfigHolder clientConfigurations;

	
	public BatchLoadTask() {
		//
	}
	
	public BatchLoadTask(String url) {
		this.url = url;
	}
		
	public void submitTransformationFormulaFlow(String flowName, String batchName, String flowInstanceName, String legislativeDataGroupName, boolean recurringFlag, String contentId) throws Exception {
		SubmitFlow submitFlowRequest = new SubmitFlow();
		submitFlowRequest.setFlowName(flowName);
		
		FlowParameterNameValues values1 = new FlowParameterNameValues();
		
		ObjectFactory factory = new ObjectFactory();
		
		JAXBElement<String> element1 = factory.createFlowParameterNameValuesParameterName("Content Id");
		JAXBElement<String> element2 = factory.createFlowParameterNameValuesParameterValue(contentId);
		values1.setParameterName(element1);
		values1.setParameterValue(element2);
		
		FlowParameterNameValues values2 = new FlowParameterNameValues();
		JAXBElement<String> element3 = factory.createFlowParameterNameValuesParameterName("Batch");
		JAXBElement<String> element4 = factory.createFlowParameterNameValuesParameterValue(batchName);
		values2.setParameterName(element3);
		values2.setParameterValue(element4);
		
		submitFlowRequest.getParameterValues().add(values1);
		submitFlowRequest.getParameterValues().add(values2);
		submitFlowRequest.setFlowInstanceName(flowInstanceName);
		submitFlowRequest.setLegislativeDataGroupName(legislativeDataGroupName);
		submitFlowRequest.setRecurringFlag(recurringFlag);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
		getWebServiceTemplate().setMarshaller(marshaller);
		getWebServiceTemplate().setUnmarshaller(marshaller);
		
		// Get the client configuration
		ClientConfiguration clientConfiguration = clientConfigurations.getSingleClientData(this.clientId);
		NotificationJobDtl config = clientConfiguration.getNotificationJobDtl();
		
		Resource keyStore = new ClassPathResource(config.getNotificationJobKeyStorePath());
		KeyStore ks = KeyStore.getInstance("JKS");
		
		String keyStorePwd = config.getNotificationJobKeyStorePwd();
		
		ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
		keyStore.getInputStream().close();
		
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
        
        AuthenticatingUrlConnectionMessageSender authenticatingUrlConnectionMessageSender = new AuthenticatingUrlConnectionMessageSender();
        authenticatingUrlConnectionMessageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
        authenticatingUrlConnectionMessageSender.setUsername(config.getNotificationJobUserName());
        authenticatingUrlConnectionMessageSender.setPassword(config.getNotificationJobPassword());
        authenticatingUrlConnectionMessageSender.afterPropertiesSet();
        getWebServiceTemplate().setMessageSender(authenticatingUrlConnectionMessageSender);
        authenticatingUrlConnectionMessageSender = null;
        System.out.println("URL is " + this.url);
        System.out.println("Username is " + config.getNotificationJobUserName());
        System.out.println("Password is " + config.getNotificationJobPassword());
        log.info("SubmitFlow webservice Start");
		SubmitFlowResponse flowResponse = (SubmitFlowResponse)getWebServiceTemplate().marshalSendAndReceive(this.url, submitFlowRequest);
		log.info("SubmitFlow webservice End");
	}
	
	public String getInstanceTaskStatus(String flowInstanceName, String flowTaskInstanceName, String legislativeDataGroupName) throws Exception {
		GetFlowTaskInstanceStatus getFlowTaskInstanceStatus = new GetFlowTaskInstanceStatus();
		getFlowTaskInstanceStatus.setFlowInstanceName(flowInstanceName);
		getFlowTaskInstanceStatus.setFlowTaskInstanceName(flowTaskInstanceName);
		getFlowTaskInstanceStatus.setLegislativeDataGroupName(legislativeDataGroupName);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("com.oracle.xmlns","sdo.commonj");
		getWebServiceTemplate().setMarshaller(marshaller);
		getWebServiceTemplate().setUnmarshaller(marshaller);
		
		// Get the client configuration
		ClientConfiguration clientConfiguration = clientConfigurations.getSingleClientData(this.clientId);
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
		
		GetFlowTaskInstanceStatusResponse response = (GetFlowTaskInstanceStatusResponse)getWebServiceTemplate().marshalSendAndReceive(this.url, getFlowTaskInstanceStatus);
		return response.getResult();
	}
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("//Users/abhisheksingh/ddrive/everge_ws/integration/src/main/webapp/WEB-INF/application.xml");
		System.out.println("Application Name "+applicationContext.getDisplayName());
		
		ClientConfigHolder clientConfigurations = (ClientConfigHolder) applicationContext.getBean(ClientConfigHolder.class);
		
		BatchLoadTask loadTask = new BatchLoadTask();
		loadTask.setClientId("adp1");
		loadTask.setClientConfigurations(clientConfigurations);
		
		String flowName = BatchLoadTaskConstants.ADP_FLOW_NAME;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(now);
		
		String flowInstanceName = flowName + formattedDate;
		flowInstanceName = "TODAYTEST14";
		String batchName = BatchLoadTaskConstants.ADP_BATCH_NAME + formattedDate;
		String legislativeDataGroupName = BatchLoadTaskConstants.LEGISLATIVE_DATA_GRP_NAME;
		boolean recurringFlag = false;
		String contentId = "UCM20170107110442";
		
		//https://hcwh-test.hcm.ap1.oraclecloud.com/hcmProcFlowCoreController/FlowActionsService?WSDl
		
		loadTask.url = "https://ecbfdev4-test.hcm.us8.oraclecloud.com/hcmProcFlowCoreController/FlowActionsService?WSDl";
		System.out.println("contentId is " + contentId);
		System.out.println("FlowInstanceName is " + flowInstanceName);
		System.out.println("BatchName is " + flowInstanceName);
		loadTask.submitTransformationFormulaFlow(flowName, batchName, flowInstanceName, legislativeDataGroupName, recurringFlag, contentId);
		System.out.println("Submit Flow Done. Moving forward.");
		Thread.sleep(5000);
		while(true) {
			String result = loadTask.getInstanceTaskStatus(flowInstanceName, BatchLoadTaskConstants.BATCHLOAD_FLOWTASKINSTANCENAME, legislativeDataGroupName);
			System.out.println("Batch Load Result is " + result);
			if(result.equalsIgnoreCase("COMPLETED")) {
				String result1 = loadTask.getInstanceTaskStatus(flowInstanceName, BatchLoadTaskConstants.TRANSFERBATCH_FLOWTASKINSTANCENAME, legislativeDataGroupName);
				System.out.println("Transfer Batch Result is " + result);
			}
			Thread.sleep(10000);
		}
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public ClientConfigHolder getClientConfigurations() {
		return clientConfigurations;
	}

	public void setClientConfigurations(ClientConfigHolder clientConfigurations) {
		this.clientConfigurations = clientConfigurations;
	}


}
