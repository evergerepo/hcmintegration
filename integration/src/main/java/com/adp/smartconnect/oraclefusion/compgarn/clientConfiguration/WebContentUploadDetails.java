package com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WebContentUploadDetails")
@XmlAccessorType (XmlAccessType.FIELD)
public class WebContentUploadDetails {
	
	@XmlElement(name="WebContentUrl")
	private String WebContentUrl;
	
	@XmlElement(name="WebContentUserName")
	private String WebContentUserName;
	
	@XmlElement(name="WebContentPwd")
	private String WebContentPwd;

	public String getWebContentUrl() {
		return WebContentUrl;
	}

	public void setWebContentUrl(String webContentUrl) {
		WebContentUrl = webContentUrl;
	}

	public String getWebContentUserName() {
		return WebContentUserName;
	}

	public void setWebContentUserName(String webContentUserName) {
		WebContentUserName = webContentUserName;
	}

	public String getWebContentPwd() {
		return WebContentPwd;
	}

	public void setWebContentPwd(String webContentPwd) {
		WebContentPwd = webContentPwd;
	}

}
