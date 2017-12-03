package com.adp.smartconnect.oraclefusion.compgarn.util;

import java.util.UUID;

public class CommonUtil {

	
	public static String generateId(){
		return  UUID.randomUUID().toString();
	}
	
	 public static String generateId(String transId){
	       return   transId+"_"+System.currentTimeMillis();
	 }
	 
}
