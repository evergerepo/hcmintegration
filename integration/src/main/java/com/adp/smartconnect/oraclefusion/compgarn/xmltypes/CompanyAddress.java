package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CompanyAddress")
@XmlAccessorType (XmlAccessType.FIELD)
public class CompanyAddress {
	
	@XmlElement
	private String AddressLine1;
	@XmlElement
	private String City;
	@XmlElement
	private String State;
	@XmlElement
	private String ZipCode;
	@XmlElement
	private String PersonNumber;
	
	public String getAddressLine1() {
		return AddressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		AddressLine1 = addressLine1;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	public String getPersonNumber() {
		return PersonNumber;
	}
	public void setPersonNumber(String personNumber) {
		PersonNumber = personNumber;
	}

}
