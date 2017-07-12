package com.adp.smartconnect.oraclefusion.compgarn.listeners;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;

public class ClientConfigHolder {

	public static HashMap<String, ClientConfiguration> clientConfiguration = new HashMap<>();

	private Configuration configuration;

	private static final Logger logger = LoggerFactory.getLogger(ClientConfigHolder.class);

	public static HashMap<String, ClientConfiguration> getClientsConfigurations() {
		return clientConfiguration;
	}

	public static void setClientConfiguration(HashMap<String, ClientConfiguration> clientConfiguration) {
		ClientConfigHolder.clientConfiguration = clientConfiguration;
	}

	public static void addClientConfiguration(String clientName, ClientConfiguration configuration) {
		clientConfiguration.put(clientName, configuration);
	}

	public ClientConfiguration getSingleClientData(String client) {
		if (clientConfiguration.isEmpty()) {
			loadClientProfileXml();
		}
		return clientConfiguration.get(client);
	}

	public void loadClientProfileXml() {
		try {
			String notificationPath = configuration.getNotificationPath();
			final File file = new File(notificationPath);

			logger.info("Loading client profiles from :" + notificationPath);

			JAXBContext jaxbContext = JAXBContext.newInstance(ClientConfiguration.class);
			Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			for (final File child : file.listFiles()) {
				ClientConfiguration configuration = (ClientConfiguration) jaxbUnMarshaller.unmarshal(child);
				logger.info("Loading configuration for [" + configuration.getClientId() + "]");
				ClientConfigHolder.addClientConfiguration(configuration.getClientId(), configuration);
			}
		} catch (Exception exc) {
			logger.error("Error loading client profiles loadClientProfileXml() ", exc);
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
