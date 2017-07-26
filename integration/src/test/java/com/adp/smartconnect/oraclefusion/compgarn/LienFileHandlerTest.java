package com.adp.smartconnect.oraclefusion.compgarn;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.integration.client.FileHandler;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

public class LienFileHandlerTest {
	
	public static void main(String[] args) throws Exception {
		String path = FileHandler.class.getClassLoader().getResource("").getPath();
		final File file = new File(path + "client");
		JAXBContext jaxbContext = JAXBContext.newInstance(ClientConfiguration.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		for (final File child : file.listFiles()) {
			ClientConfiguration configuration = (ClientConfiguration)jaxbUnMarshaller.unmarshal(child);
			ClientConfigHolder.addClientConfiguration(configuration.getClientName(), configuration);
		}
		
		FileHandler handler = new FileHandler();
		handler.handleFile(new File("/Users/abhisheksingh/ddrive/everge_ws/files/childSupportRecords-Write.txt"));
	}

	
	//@Test
	public void createStrippedFile() throws Exception{
		FileHandler fileHandler = new FileHandler();
		File inputFile = new File("/mule/smartconnect/data/arff/compgarn/lien/adp1.06062017_v1.lien.grn");
		
		fileHandler.createStrippedFile(inputFile, inputFile.getName(), "/mule/smartconnect/data/arff/compgarn/lien/work/");
	}

}
