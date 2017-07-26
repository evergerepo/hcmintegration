package com.adp.smartconnect.oraclefusion.compgarn;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.adp.smartconnect.oraclefusion.compgarn.controller.FileUtilController;


public class FileUtilControllerTest {

	FileUtilController fileUtilController =new FileUtilController();
	
	@BeforeClass
	public static void before() {
		
	}
	
	
	@Test
	public void copyFile() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("srcFile", "/mule/smartconnect/data/arff/compgarn/pqq/adp1.wgps.test.20170621132733.pqq");
		map.put("destFile", "/mule/smartconnect/data/arff/compgarn/pqq/inbound/adp1.wgps.test.20170621132733.pqq.copy");
		fileUtilController.copyFile(map);
	}
	
	@Test
	public void deleteFile() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileName", "/mule/smartconnect/data/arff/compgarn/pqq/adp1.wgps.test.20170621132733.pqq");
		fileUtilController.deleteFile(map);
	}
	
	@Test
	public void cleanDirectory() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dirName", "/mule/smartconnect/data/arff/compgarn/pqq/archive");
		fileUtilController.deleteFile(map);
	}
}