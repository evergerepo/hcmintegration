package com.adp.smartconnect.oraclefusion.compgarn.pqq;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adp.smartconnect.oraclefusion.compgarn.listeners.APPConfigHolder;

import bip_webservice.proxy.ReportService;
import bip_webservice.proxy.ReportService_Service;
import bip_webservice.proxy.types.ArrayOfParamNameValue;
import bip_webservice.proxy.types.ArrayOfString;
import bip_webservice.proxy.types.ParamNameValue;
import bip_webservice.proxy.types.ParamNameValues;
import bip_webservice.proxy.types.ReportRequest;
import bip_webservice.proxy.types.ReportResponse;

public class PqqLoadTask {
	
	private static final Logger log = LoggerFactory.getLogger(PqqLoadTask.class);
	
	private String url;
	
	private String locale;
	
	private int chunkSizeDownload;
		
	public PqqLoadTask() {
		//
	}
	
	public PqqLoadTask(String url) {
		this.url = url;
	}
	
	@SuppressWarnings("rawtypes")
	public String callRunReport(String reportPath ,Map data,String username ,String password ,String format ,String template ) throws Exception{

		String reportResponse =null;
        try {
        		log.debug("callRunReport() :"+reportPath+","+data+","+username+","+password+", URL:"+url); 
               
                ReportService_Service reportServiceService = new ReportService_Service(new URL(url), new QName("http://xmlns.oracle.com/oxp/service/v2", "ReportService"));
                ReportService reportService =  reportServiceService.getReportService();
                BindingProvider bp = (BindingProvider)reportService;
                Map<String, Object> ctx = bp.getRequestContext();
                
                ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
                
                if(APPConfigHolder.isProxyEnabled()){
                	log.info("Proxy configured. :"+APPConfigHolder.getProxyHost()+","+APPConfigHolder.getProxyPort());
                	ctx.put(BindingProvider.USERNAME_PROPERTY, APPConfigHolder.getProxyHost());
                	ctx.put(BindingProvider.PASSWORD_PROPERTY, APPConfigHolder.getProxyPort());
                }
                
                
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
                    log.debug("keyData "+keyData+", value "+(String)data.get(keyData));
                    paramNameValue.getItem().add(reportPrompt);
                }
                
                //set Parameters
                paramNameValues.setListOfParamNameValues(paramNameValue);
                reportRequest.setParameterNameValues(paramNameValues);
                ReportResponse repResponse = new ReportResponse();
                
                //Call Report Service
                log.info("Invoking Report Service. Request:"+reportRequest);
                repResponse = reportService.runReport(reportRequest, username, password);
            
                byte[] baReport = repResponse.getReportBytes();
                reportResponse = new String( baReport);
                log.info("Report service response: "+ new String( baReport));
        } catch (Exception ex) {
        	log.error("callRunReport() Error:"+ex.getMessage(), ex);
        	throw ex;
        	
        }
        return reportResponse;
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
	
}
