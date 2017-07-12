package com.adp.smartconnect.oraclefusion.compgarn.pqq;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl.BatchLoadTask;

import bip_webservice.proxy.AccessDeniedException;
import bip_webservice.proxy.InvalidParametersException;
import bip_webservice.proxy.OperationFailedException;
import bip_webservice.proxy.ReportService;
import bip_webservice.proxy.ReportService_Service;
import bip_webservice.proxy.types.ArrayOfParamNameValue;
import bip_webservice.proxy.types.ArrayOfString;
import bip_webservice.proxy.types.ParamNameValue;
import bip_webservice.proxy.types.ParamNameValues;
import bip_webservice.proxy.types.ReportRequest;
import bip_webservice.proxy.types.ReportResponse;

public class PqqLoadTask {
	
	private static final Logger log = LoggerFactory.getLogger(BatchLoadTask.class);
	
	private String url;
	
	private String locale;
	
	private int chunkSizeDownload;
		
	public PqqLoadTask() {
		//
	}
	
	public PqqLoadTask(String url) {
		this.url = url;
	}
	
    public void callRunReport(String reportPath ,Map data,String username ,String password ,String format ,String template ,String outFile ) throws AccessDeniedException, InvalidParametersException, OperationFailedException, IOException {

    	java.net.URL url = new java.net.URL(this.getUrl());
    	ReportService_Service reportServiceService = new ReportService_Service(url, new QName("http://xmlns.oracle.com/oxp/service/v2", "ReportService"));
        ReportService reportService =  reportServiceService.getReportService();
        ParamNameValues paramNameValues = new ParamNameValues();
        // Calling runReport
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setReportAbsolutePath(reportPath);
        reportRequest.setAttributeTemplate(template);
        reportRequest.setAttributeFormat(format);
        reportRequest.setAttributeLocale(this.locale);
        reportRequest.setSizeOfDataChunkDownload(this.chunkSizeDownload);
        
        Set keySet = data.keySet();
        Iterator keySetItr = keySet.iterator();
        ArrayOfParamNameValue paramNameValue = new ArrayOfParamNameValue();
        
        while(keySetItr.hasNext()) {
        	String keyData = (String)keySetItr.next();
            ArrayOfString value = new ArrayOfString();
            value.getItem().add((String)data.get(keyData));
            ParamNameValue reportPrompt = new ParamNameValue();
            reportPrompt.setName(keyData);
            reportPrompt.setValues(value);
            log.info("keyData "+keyData);
            log.info("value "+(String)data.get(keyData));
            paramNameValue.getItem().add(reportPrompt);
        }
        
        //set Parameters
        paramNameValues.setListOfParamNameValues(paramNameValue);

        reportRequest.setParameterNameValues(paramNameValues);
        ReportResponse repResponse = new ReportResponse();
        repResponse = reportService.runReport(reportRequest, username, password);
    
        byte[] baReport = repResponse.getReportBytes();
        System.out.println("Response "+ new String( baReport));
        FileOutputStream fio = new FileOutputStream(outFile);
        fio.write(baReport);
        fio.close();

}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getChunkSizeDownload() {
		return chunkSizeDownload;
	}

	public void setChunkSizeDownload(int chunkSizeDownload) {
		this.chunkSizeDownload = chunkSizeDownload;
	}
	
	public static void main(String[] args) throws AccessDeniedException, InvalidParametersException, OperationFailedException, IOException {
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "999999");
		
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("SSN", "220237294");
		//data.put("SSNFormatted", "199-99-0013");
		data.put("First Name", "DENNIS WAYNE");
		data.put("Last Name", "BUCHANAN JR");
		data.put("DocId", "GS119486927");
		data.put("TransId", "1087222");

		PqqLoadTask task = new PqqLoadTask();
		//task.setUrl("https://hcwh-test.bi.ap1.oraclecloud.com/xmlpserver/services/v2/ReportService?wsdl");
		task.setUrl("https://ecbfdev4-test.fs.us8.oraclecloud.com/xmlpserver/services/v2/ReportService?wsdl");
		task.setChunkSizeDownload(-1);
		//task.callRunReport("/Custom/Human Capital Management/Reports/Payroll/PQQ/TEST/PQQv1.25 Report.xdo", data, "HCMUser", "Cloud2World!", "xml", null, "/Users/abhisheksingh/ddrive/everge_ws/details/BIP/Report_2.xml");

		//task.callRunReport("/Custom/ADP Comp Garn/PQQv1.25 Report.xdo", data, "ADP_Connect", "ah2lB}A8wj05", "xml", null, "/Users/abhisheksingh/ddrive/everge_ws/details/Report_2.xml");
		task.callRunReport("/Custom/ADP Comp Garn/PQQv1.25 Report.xdo", data, "pushparaj.dharmaraj", "Welcome123", "xml", null, "/Users/abhisheksingh/ddrive/everge_ws/details/Report_2.xml");

	}

}
