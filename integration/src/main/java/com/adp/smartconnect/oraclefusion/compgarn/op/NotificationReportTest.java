package com.adp.smartconnect.oraclefusion.compgarn.op;

import com.adp.smartconnect.oraclefusion.compgarn.integration.client.ReportServiceClient;

import bip_webservice.proxy.types.ArrayOfParamNameValue;
import bip_webservice.proxy.types.ArrayOfString;
import bip_webservice.proxy.types.ParamNameValue;

public class NotificationReportTest {
	
	public static void main(String[] args) throws Exception {
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "true");
		

		ArrayOfString value = new ArrayOfString();
        value.getItem().add("MyBatch20170201064719");
    
        ParamNameValue reportPrompt = new ParamNameValue();
        reportPrompt.setName("batch_name");
        reportPrompt.setValues(value);
        
        ArrayOfParamNameValue paramNameValue = new ArrayOfParamNameValue();
        paramNameValue.getItem().add(reportPrompt);
        ReportServiceClient ServiceClient = new ReportServiceClient();
        ServiceClient.callRunReport("/Custom/Human Capital Management/Reports/Payroll/ADP_Notification_Report.xdo", paramNameValue, "HCMUser", "Cloud2World!", "text", null, "/Users/abhisheksingh/ddrive/everge_ws/details/BIP/NotificationReport.xml");

	}

}
