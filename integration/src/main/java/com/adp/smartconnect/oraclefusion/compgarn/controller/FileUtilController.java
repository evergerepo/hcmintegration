package com.adp.smartconnect.oraclefusion.compgarn.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

@RestController
@RequestMapping("/file")
public class FileUtilController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilController.class);
	private static final String SUCESS = "Sucess";
	
	/**
	 * This service is used to copy file from source to destination.
	 */
	@RequestMapping(path = "/copyFile", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String copyFile(@RequestBody Map<String, String> files) {
		String srcFile = files.get("srcFile");
		String destFile = files.get("destFile");
		
		LOGGER.info("copyFile Start. srcFile:"+srcFile+", destFile:"+destFile);
		try{
			FileUtils.copyFile(new File(srcFile), new File(destFile));
			LOGGER.info("copyFile Complete. srcFile:"+srcFile+", destFile:"+destFile);
		}catch(Exception e){
			LOGGER.info("Error copyFile.Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		return SUCESS;
	}

	/**
	 * This service is used to delete file.
	 */
	
	@RequestMapping(path = "/deleteFile", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String deleteFile(@RequestBody Map<String, String> files) {
		String fileName = files.get("fileName");
		LOGGER.info("deleteFile Start. File:"+fileName);
		try{
			FileUtils.forceDeleteOnExit(new File(fileName));
			LOGGER.info("deleteFile Complete. File:"+fileName);
		}catch(Exception e){
			LOGGER.info("deleteFile Error. Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		return SUCESS;
	}
	

	/**
	 * This service is used to re-loading client profiles
	 */
	@RequestMapping(path = "/loadClientProfile", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String loadClientProfile() {
		try{
			LOGGER.info("loadClientProfile Start");
			clientConfigurations.loadClientProfileXml();
			LOGGER.info("loadClientProfile Complete");
		}catch(Exception e){
			LOGGER.info("loadClientProfile) ERROR. Message:"+e.getMessage(), e);
			return e.getMessage();
		}
		return SUCESS;
	}
	

	@Autowired private ClientConfigHolder clientConfigurations;
	
}
