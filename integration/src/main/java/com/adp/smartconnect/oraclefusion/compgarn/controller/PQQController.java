package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.pqq.PqqFileHandler;
import com.adp.smartconnect.oraclefusion.compgarn.util.CommonUtil;

@RestController
@RequestMapping("/pqq")
public class PQQController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PQQController.class);
	
	/**
	 * This service is used to re-loading client profiles
	 */
	@PostMapping(path = "/pqqProcess")
	public  String pqqProcess(@RequestBody Map<String, String> files) {
		String fileName = files.get("fileName");
		Boolean processed = false;
		String transId = null;
		try{
			LOGGER.info("pqqProcess Start. File Name:{}", fileName);
			transId = CommonUtil.generateId();
			String jobStepId = CommonUtil.generateId(transId);
			
			File file = new File(fileName);
			MDC.put("transId", transId);
			MDC.put("fileName", file.getName());
			
			processed= pqqFileHandler.processFile(file, transId,jobStepId );
					
			LOGGER.info("pqqProcess Complete. Processed:{}", processed);
		}catch(Exception e){
			LOGGER.info("pqqProcess ERROR. Message:"+e.getMessage(), e);
		}
		MDC.clear();
		return transId;
	}
	
	
	
	@Autowired  ClientConfigHolder clientConfigurations;
	@Autowired  PqqFileHandler pqqFileHandler;
	
}
