package com.adp.smartconnect.oraclefusion.compgarn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTask;
import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTaskConstants;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

public class BatchLoadTaskTest {

public static void main(String[] args) throws Exception {
		
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("//Users/abhisheksingh/ddrive/everge_ws/integration/src/main/webapp/WEB-INF/application.xml");
		System.out.println("Application Name "+applicationContext.getDisplayName());
		
		ClientConfigHolder clientConfigurations = (ClientConfigHolder) applicationContext.getBean(ClientConfigHolder.class);
		
		BatchLoadTask loadTask = new BatchLoadTask();
		//loadTask.setClientId("adp1");
		//loadTask.setClientConfigurations(clientConfigurations);
		
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
		/*
		String url = "https://ecbfdev4-test.hcm.us8.oraclecloud.com/hcmProcFlowCoreController/FlowActionsService?WSDl";
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
		*/
	}
}
