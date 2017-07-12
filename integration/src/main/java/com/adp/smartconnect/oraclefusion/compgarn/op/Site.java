package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Site")
@XmlAccessorType (XmlAccessType.FIELD)
public class Site {
	
	@XmlElement(name="SiteID")
	private String SiteID;
	@XmlElement(name="EELiens")
	private EELiens eeLiens;
	
	public String getSiteID() {
		return SiteID;
	}
	public void setSiteID(String siteID) {
		SiteID = siteID;
	}
	public EELiens getEeLiens() {
		return eeLiens;
	}
	public void setEeLiens(EELiens eeLiens) {
		this.eeLiens = eeLiens;
	}

}
