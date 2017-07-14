package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.WebContentUploadDetails;
import com.adp.smartconnect.oraclefusion.compgarn.contentupload.WebContentUpload;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

@RestController
@RequestMapping("/lien")
public class LienNotificationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LienNotificationController.class);
	
	/**
	 * This service is used to re-loading client profiles
	 */
	@RequestMapping(path = "/uploadLienContent", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String uploadLienContent(@RequestBody Map<String, String> files) {
		String fileName = files.get("fileName");
		String contentId = null;
		try{
			LOGGER.info("uploadLienContent Start. File Name:"+fileName);
			File file = new File(fileName);
			MDC.put("transId", file.getName());
			
			String clientId = file.getName().substring(0, 4).toLowerCase();
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
			LOGGER.info("Client Id:"+clientId+", ClientConfiguration:"+config);
			if(config==null){
				return "Client Profile not found for Client:"+clientId;
			}
			
			WebContentUploadDetails uploadDtl = config.getWebContentUploadDtl();
			WebContentUpload upload = new WebContentUpload();
			
			contentId = upload.uploadContent(uploadDtl.getWebContentUrl(), uploadDtl.getWebContentUserName(), uploadDtl.getWebContentPwd(), fileName);
			
			LOGGER.info("uploadLienContent Complete. Response:"+contentId);
		}catch(Exception e){
			LOGGER.info("uploadLienContent ERROR. Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		MDC.clear();
		return contentId;
	}
	
	
	
	/**
	 * This service is used to re-loading client profiles
	 */
	@RequestMapping(path = "/invokeSubmitFlow", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String invokeSubmitFlow(@RequestBody Map<String, String> data) {
		String contentId = data.get("contentId");
		String clientId  = data.get("clientId");
		String flowName  = data.get("flowName");
		try{
			LOGGER.info("invokeSubmitFlow Start. Request:"+ReflectionToStringBuilder.toString(data));
			MDC.put("transId", contentId);
			
			LOGGER.info("invokeSubmitFlow Complete. Response:"+contentId);
		}catch(Exception e){
			LOGGER.info("invokeSubmitFlow ERROR. Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		MDC.clear();
		return contentId;
	}
	
	
	@Autowired private ClientConfigHolder clientConfigurations;
	
}
