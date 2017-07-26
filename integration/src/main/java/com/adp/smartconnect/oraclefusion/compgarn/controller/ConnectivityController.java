package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.PqqFileDetails;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.pqq.PqqLoadTask;


@RestController
@RequestMapping("/connect")
public class ConnectivityController {
	
	
private static final Logger LOGGER = LoggerFactory.getLogger(PQQController.class);
	
	/**
	 * Test PQQ Report Service
	 */
	@RequestMapping(path = "/pqqReport", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String pqqReportService(@RequestBody Map<String, String> request) {
		String client = request.get("client");
		String response = null;
		
		try{
			LOGGER.info("pqqReportService Start. Clieny:"+client);
			String uuid = UUID.randomUUID().toString();
			Map<String, String> data = new HashMap<String, String>();
			data.put("SSN", "ssn");
			data.put("First Name", "firstName");
			data.put("Last Name", "lastName");
			data.put("DocId", uuid);
			data.put("TransId", uuid);
			
			 
			ClientConfiguration config = clientConfigurations.getSingleClientData(client);
			PqqFileDetails fileDetails=  config.getPqqFileDetails();
			PqqLoadTask pqqLoadTask = new PqqLoadTask();
			pqqLoadTask.setUrl(fileDetails.getPqqUrl());
			pqqLoadTask.setLocale(fileDetails.getPqqLocale());
			pqqLoadTask.setChunkSizeDownload(-1);
			
			 response = pqqLoadTask.callRunReport(fileDetails.getPqqReportPath(), data, fileDetails.getPqqUsername(),
						fileDetails.getPqqPassword(), fileDetails.getPqqFormat(), null);

			LOGGER.info("pqqReportService Complete. Response:"+response);
		}catch(Exception e){
			LOGGER.info("pqqReportService ERROR. Message:"+e.getMessage(), e);
		}
		return response;
	}
	
	
	@Autowired  ClientConfigHolder clientConfigurations;
	

}
