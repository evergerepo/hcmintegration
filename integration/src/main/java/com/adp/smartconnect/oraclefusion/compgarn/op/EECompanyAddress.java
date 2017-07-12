package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EECompanyAddress")
public class EECompanyAddress {
	
	@XmlElement(name="City")
	private String City;
	
	@XmlElement(name="State")
	private String State;
	
	@XmlElement(name="Zip")
	private String Zip;
	
	@XmlElement(name="AddressLine")
	private List<String> AddressLine;

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

	public String getZip() {
		return Zip;
	}

	public void setZip(String zip) {
		Zip = zip;
	}

	public List<String> getAddressLine() {
		return AddressLine;
	}

	public void setAddressLine(List<String> addressLine) {
		AddressLine = addressLine;
	} 

}
