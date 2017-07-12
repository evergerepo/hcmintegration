package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CompanyInfo")
@XmlAccessorType (XmlAccessType.FIELD)
public class CompanyInfo {
	
	@XmlElement
	private String CompanyName1;
	
	@XmlElement
	private String PersonNumber;
	
	@XmlElement(name="CompanyAddress")
	private CompanyAddress companyAddress;

	public String getCompanyName1() {
		return CompanyName1;
	}

	public void setCompanyName1(String companyName1) {
		CompanyName1 = companyName1;
	}

	public String getPersonNumber() {
		return PersonNumber;
	}

	public void setPersonNumber(String personNumber) {
		PersonNumber = personNumber;
	}

	public CompanyAddress getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(CompanyAddress companyAddress) {
		this.companyAddress = companyAddress;
	}

}
