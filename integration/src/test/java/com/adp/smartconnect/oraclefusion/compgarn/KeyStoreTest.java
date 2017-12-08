package com.adp.smartconnect.oraclefusion.compgarn;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
public class KeyStoreTest {

	

	public static void main(String[] args){
		try{
			//Load JKS Key into KeyStore
			Resource keyStore = new ClassPathResource("/everge.jks");
			System.out.println("Resource==>"+keyStore.contentLength()+":"+keyStore.getInputStream());
			String keyStorePwd = "welcome";
			
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(keyStore.getInputStream(), keyStorePwd.toCharArray());
			keyStore.getInputStream().close();
			
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        keyManagerFactory.init(ks, keyStorePwd.toCharArray());
	        System.out.println("Key is LOADED");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
		
}
