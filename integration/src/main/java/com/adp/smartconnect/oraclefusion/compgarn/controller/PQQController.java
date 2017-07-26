package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.io.File;
import java.util.Map;

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

import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.pqq.PqqFileHandler;

@RestController
@RequestMapping("/pqq")
public class PQQController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PQQController.class);
	
	/**
	 * This service is used to re-loading client profiles
	 */
	@RequestMapping(path = "/pqqProcess", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean pqqProcess(@RequestBody Map<String, String> files) {
		String fileName = files.get("fileName");
		Boolean processed = false;
		try{
			LOGGER.info("pqqProcess Start. File Name:"+fileName);
			File file = new File(fileName);
			MDC.put("transId", file.getName());
			
			processed= pqqFileHandler.processFile(file);
					
			LOGGER.info("pqqProcess Complete. Processed:"+processed);
		}catch(Exception e){
			LOGGER.info("pqqProcess ERROR. Message:"+e.getMessage(), e);
		}
		MDC.clear();
		return processed;
	}
	
	
	
	@Autowired  ClientConfigHolder clientConfigurations;
	@Autowired  PqqFileHandler pqqFileHandler;
	
}
