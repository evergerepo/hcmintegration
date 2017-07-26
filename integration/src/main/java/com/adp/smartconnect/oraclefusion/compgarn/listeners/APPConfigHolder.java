package com.adp.smartconnect.oraclefusion.compgarn.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;

import oracle.stellent.ridc.protocol.http.IdcHttpClientConfig;

@Service("appConfigHolder")
public class APPConfigHolder {
	
	private static final Logger logger = LoggerFactory.getLogger(APPConfigHolder.class);
	
	public static String APP_CONFIG= "app-config.properties";
	public static HashMap<String, String> appConfig = new HashMap<>();


	public static String getKeyStringValue(String key){
		if(StringUtils.isNoneBlank(key)){
			return appConfig.get(key);
		}
		return null;
	}
	
	public static boolean getKeyBooleanValue(String key){
		String data = getKeyStringValue(key);
		if("Y".equalsIgnoreCase(data)){
			return true;
		}
		return false;
	}
	
	public static boolean isProxyEnabled(){
		return getKeyBooleanValue("proxy.enabled");
	}
	
	public static String getProxyHost(){
		return getKeyStringValue("proxy.hostName");
	}
	
	public static String getProxyPort(){
		return getKeyStringValue("proxy.port");
	}
	
	/*
	 * Load application Config file
	 */
	@PostConstruct
	public void loadAppConfig() {
		FileInputStream fis = null;
		try {
			String confignPath = configuration.getConfigPath();
			final File file = new File(confignPath+"/"+APP_CONFIG);
			logger.info("Loading app config from :" + file.getAbsolutePath());
			fis = new FileInputStream(file);
			
			Properties properties = new Properties();
			properties.load(fis);
			
			for (String key : properties.stringPropertyNames()) {
				appConfig.put(key, properties.getProperty(key));
			}

			setupSystemProxy();
		} catch (Exception exc) {
			logger.error("Error loading app config file loadAppConfig() ", exc);
		}finally{
			if(fis!=null){
				try{
					fis.close();
				}catch(Exception e){}
			}
		}
	}

	//Set-up Proxy Congig
	public void setupSystemProxy(){
		if(APPConfigHolder.isProxyEnabled()){
			logger.info("Proxy configured. Setting System Param for Proxy."+
							APPConfigHolder.getProxyHost()+","+APPConfigHolder.getProxyPort());
			 System.setProperty("http.proxyHost", APPConfigHolder.getProxyHost());
			 System.setProperty("https.proxyHost", APPConfigHolder.getProxyHost());
			 System.setProperty("http.proxyPort", APPConfigHolder.getProxyPort());
			 System.setProperty("https.proxyPort", APPConfigHolder.getProxyPort());
		 }
	}
	
	@Autowired private Configuration configuration;
}
