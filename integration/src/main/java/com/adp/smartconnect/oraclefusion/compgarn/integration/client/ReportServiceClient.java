package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

public class ReportServiceClient
{
    private String username;
    private String password;
    private String reportPath;
    private String format;
    private String template;
    private String locale;
    private int sizeOfDataChunkDownload;
    private String paramName;
    private String paramValue;
    private String outFile;
    private String url;
    
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceClient.class);
    
    
    public ReportServiceClient() throws Exception
    {
        username = null;
        password = null;
        reportPath = null;
        format = null;
        template = null;
        locale = "en-US";
        sizeOfDataChunkDownload = -1;
        paramName = null;
        paramValue = null;
        outFile = null;
        url = null;
    }
    
    public void callRunReport(String reportPath ,ArrayOfParamNameValue arrayOfParamNameValue,String username ,String password ,String format ,String template ,String outFile ) throws AccessDeniedException, InvalidParametersException, OperationFailedException, IOException{
    	
	    java.net.URL url = new java.net.URL(this.getUrl());
        ReportService_Service reportServiceService = new ReportService_Service(url, new QName("http://xmlns.oracle.com/oxp/service/v2", "ReportService"));
        ReportService reportService =  reportServiceService.getReportService();
        ParamNameValues paramNameValues = new ParamNameValues();
        // Calling runReport
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setReportAbsolutePath(reportPath);
        reportRequest.setAttributeTemplate(template);
        reportRequest.setAttributeFormat(format);
        reportRequest.setAttributeLocale(locale);
        reportRequest.setSizeOfDataChunkDownload(sizeOfDataChunkDownload);
        //set Parameters
        paramNameValues.setListOfParamNameValues(arrayOfParamNameValue);
        reportRequest.setParameterNameValues(paramNameValues);
        ReportResponse repResponse = new ReportResponse();
        repResponse = reportService.runReport(reportRequest, username, password);
    
         byte[] baReport = repResponse.getReportBytes();
         logger.debug("Response after calling the report "+ reportPath+" is " + new String( baReport));
         if(null == baReport || baReport.length == 0) {
        	 Calendar calendar = Calendar.getInstance();
        	 String message = calendar.getTime() + " No records found. Please review/validate the source data";
        	 baReport = message.getBytes();
         }
        FileOutputStream fio = new FileOutputStream(outFile);
        fio.write(baReport);
        fio.close();
    }
    
  public static void main(String [] args) throws AccessDeniedException, InvalidParametersException,
                                                  OperationFailedException, IOException {
      
  try{
         File fXmlFile = new File("/Users/abhisheksingh/ddrive/everge_ws/details/BIP/Request1.xml");
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document doc = dBuilder.parse(fXmlFile);

          //optional, but recommended
          //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
          doc.getDocumentElement().normalize();

          System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

          NodeList nList = doc.getElementsByTagName("PreQualRecord");

          System.out.println("----------------------------");

          for (int temp = 0; temp < nList.getLength(); temp++) {

                  Node nNode = nList.item(temp);

                  System.out.println("\nCurrent Element :" + nNode.getNodeName());

                  if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                          Element eElement = (Element) nNode;

                          System.out.println("DocID id : " + eElement.getElementsByTagName("DocID").item(0).getTextContent());
                          System.out.println("First Name : " + eElement.getElementsByTagName("EEFirstName").item(0).getTextContent());
                          System.out.println("Last Name : " + eElement.getElementsByTagName("EELastName").item(0).getTextContent());
                          System.out.println("Middle Name : " + eElement.getElementsByTagName("EEMiddleInitial").item(0).getTextContent());
                          System.out.println("SSN : " + eElement.getElementsByTagName("SSN").item(0).getTextContent());

                  }
          }
      
         
          ArrayOfString value = new ArrayOfString();
          value.getItem().add("TestThirteen LN");
      
          ParamNameValue reportPrompt = new ParamNameValue();
          reportPrompt.setName("L_NAME");
          reportPrompt.setValues(value);
          
          ArrayOfParamNameValue paramNameValue = new ArrayOfParamNameValue();
          paramNameValue.getItem().add(reportPrompt);
          ReportServiceClient ServiceClient = new ReportServiceClient();
          ServiceClient.callRunReport("/Custom/ADPAnalysis/Sample.xdo", paramNameValue, "HCMUser", "Cloud2World!", "xml", null, "/Users/abhisheksingh/ddrive/everge_ws/details/BIP/Report.xml");
       
      } catch(Exception ex) {
      ex.printStackTrace();
      }
  }

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getReportPath() {
	return reportPath;
}

public void setReportPath(String reportPath) {
	this.reportPath = reportPath;
}

public String getFormat() {
	return format;
}

public void setFormat(String format) {
	this.format = format;
}

public String getTemplate() {
	return template;
}

public void setTemplate(String template) {
	this.template = template;
}

public String getLocale() {
	return locale;
}

public void setLocale(String locale) {
	this.locale = locale;
}

public int getSizeOfDataChunkDownload() {
	return sizeOfDataChunkDownload;
}

public void setSizeOfDataChunkDownload(int sizeOfDataChunkDownload) {
	this.sizeOfDataChunkDownload = sizeOfDataChunkDownload;
}

public String getParamName() {
	return paramName;
}

public void setParamName(String paramName) {
	this.paramName = paramName;
}

public String getParamValue() {
	return paramValue;
}

public void setParamValue(String paramValue) {
	this.paramValue = paramValue;
}

public String getOutFile() {
	return outFile;
}

public void setOutFile(String outFile) {
	this.outFile = outFile;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}
  
   
}