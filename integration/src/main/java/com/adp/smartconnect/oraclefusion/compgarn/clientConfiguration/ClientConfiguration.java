package com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;

@XmlRootElement(name="ClientConfiguration")
@XmlAccessorType (XmlAccessType.FIELD)
public class ClientConfiguration {
	
	@XmlElement(name="ClientName")
	private String ClientName;
	
	@XmlElement(name="ClientId")
	private String ClientId;
	
	@XmlElement(name="WebContentUploadDetails")
	private WebContentUploadDetails webContentUploadDtl;
	
	@XmlElement(name="PqqFileDetails")
	private PqqFileDetails pqqFileDetails;
	
	@XmlElement(name="NotificationJobDtl")
	private NotificationJobDtl notificationJobDtl;
	
	public String getClientName() {
		return ClientName;
	}
	
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
		
	public void setPqqFileDetails(PqqFileDetails pqqFileDetails) {
		this.pqqFileDetails = pqqFileDetails;
	}

	public WebContentUploadDetails getWebContentUploadDtl() {
		return webContentUploadDtl;
	}

	public void setWebContentUploadDtl(WebContentUploadDetails webContentUploadDtl) {
		this.webContentUploadDtl = webContentUploadDtl;
	}

	public String getClientId() {
		return ClientId;
	}

	public void setClientId(String clientId) {
		ClientId = clientId;
	}

	public PqqFileDetails getPqqFileDetails() {
		return pqqFileDetails;
	}
	
	public static void main(String[] args) {
		try {
			final File file = new File("/Users/abhisheksingh/ddrive/everge_ws/integration/src/main/resources/com/everge/clientConfiguration");
			JAXBContext jaxbContext = JAXBContext.newInstance(ClientConfiguration.class);
			Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			for (final File child : file.listFiles()) {
				ClientConfiguration configuration = (ClientConfiguration)jaxbUnMarshaller.unmarshal(child);
				ClientConfigHolder.addClientConfiguration(configuration.getClientName(), configuration);
				System.out.println("Client Name is " + configuration.getClientName());
			}
		} catch (Exception exc) {
			System.out.println("Error in loadXmlFileData ");
			exc.printStackTrace();
		}
	}

	public NotificationJobDtl getNotificationJobDtl() {
		return notificationJobDtl;
	}

	public void setNotificationJobDtl(NotificationJobDtl notificationJobDtl) {
		this.notificationJobDtl = notificationJobDtl;
	}

}
