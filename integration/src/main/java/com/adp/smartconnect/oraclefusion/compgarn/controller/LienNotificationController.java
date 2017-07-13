package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final String SUCESS = "Sucess";
	
	
	
	/**
	 * This service is used to re-loading client profiles
	 */
	@RequestMapping(path = "/ridcFileUpload", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String ridcFileUpload(@RequestBody Map<String, String> files) {
		String fileName = files.get("fileName");
		try{
			LOGGER.info("ridcFileUpload Start. File Name:"+fileName);
			File file = new File(fileName);
			String clientId = file.getName().substring(0, 4).toLowerCase();
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientId);
			LOGGER.info("Client Id:"+clientId+", ClientConfiguration:"+config);
			if(config==null){
				return "Client Profile not found for Client:"+clientId;
			}
			
			WebContentUploadDetails uploadDtl = config.getWebContentUploadDtl();
			WebContentUpload upload = new WebContentUpload(uploadDtl.getWebContentUrl(), uploadDtl.getWebContentUserName(), uploadDtl.getWebContentPwd());
			
			String response = upload.uploadContent(fileName);
			LOGGER.info("ridcFileUpload Complete. Response:"+response);
		}catch(Exception e){
			LOGGER.info("ridcFileUpload) ERROR. Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		return SUCESS;
	}
	
	
	
	@Autowired private ClientConfigHolder clientConfigurations;
	
}
