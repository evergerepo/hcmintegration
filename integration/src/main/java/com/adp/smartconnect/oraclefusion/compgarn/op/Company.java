package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Company")
@XmlAccessorType (XmlAccessType.FIELD)
public class Company {
	
	@XmlElement(name="EECompanyName")
	private String EECompanyName;
	
	@XmlElement(name="EECompanyAddress")
	private EECompanyAddress eeCompanyAddress;

	public String getEECompanyName() {
		return EECompanyName;
	}

	public void setEECompanyName(String eECompanyName) {
		EECompanyName = eECompanyName;
	}

	public EECompanyAddress getEeCompanyAddress() {
		return eeCompanyAddress;
	}

	public void setEeCompanyAddress(EECompanyAddress eeCompanyAddress) {
		this.eeCompanyAddress = eeCompanyAddress;
	}

}
