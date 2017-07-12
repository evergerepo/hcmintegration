package com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PqqFileDetails")
@XmlAccessorType (XmlAccessType.FIELD)
public class PqqFileDetails {
	
	@XmlElement(name="PqqUrl")
	private String PqqUrl;
	
	@XmlElement(name="PqqUsername")
	private String PqqUsername;
	
	@XmlElement(name="PqqPassword")
	private String PqqPassword;
	
	@XmlElement(name="PqqLocale")
	private String PqqLocale;
	
	@XmlElement(name="PqqFormat")
	private String PqqFormat;
	
	@XmlElement(name="PqqReportPath")
	private String PqqReportPath;
	
	@XmlElement(name="PqqOutputFormat")
	private String PqqOutputFormat;

	public String getPqqUrl() {
		return PqqUrl;
	}

	public void setPqqUrl(String pqqUrl) {
		PqqUrl = pqqUrl;
	}

	public String getPqqUsername() {
		return PqqUsername;
	}

	public void setPqqUsername(String pqqUsername) {
		PqqUsername = pqqUsername;
	}

	public String getPqqPassword() {
		return PqqPassword;
	}

	public void setPqqPassword(String pqqPassword) {
		PqqPassword = pqqPassword;
	}

	public String getPqqLocale() {
		return PqqLocale;
	}

	public void setPqqLocale(String pqqLocale) {
		PqqLocale = pqqLocale;
	}

	public String getPqqFormat() {
		return PqqFormat;
	}

	public void setPqqFormat(String pqqFormat) {
		PqqFormat = pqqFormat;
	}

	public String getPqqReportPath() {
		return PqqReportPath;
	}

	public void setPqqReportPath(String pqqReportPath) {
		PqqReportPath = pqqReportPath;
	}

	public String getPqqOutputFormat() {
		return PqqOutputFormat;
	}

	public void setPqqOutputFormat(String pqqOutputFormat) {
		PqqOutputFormat = pqqOutputFormat;
	}

}
