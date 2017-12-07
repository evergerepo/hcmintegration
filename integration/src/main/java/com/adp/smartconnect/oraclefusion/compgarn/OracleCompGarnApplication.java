package com.adp.smartconnect.oraclefusion.compgarn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan("com.adp.smartconnect.oraclefusion.compgarn")
@Slf4j
@ImportResource("/spring/integrations.xml")
public class OracleCompGarnApplication extends SpringBootServletInitializer {
	
	
	public static void main(String[] args) {
		log.info("Starting OracleCompGarnApplication application");
        SpringApplication.run(OracleCompGarnApplication.class, args);
       
    }
	

}
