package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Issuer")
public class Issuer {
	
	@XmlElement
	private String CourtName;
	
	@XmlElement
	private String CountyName;

	public String getCourtName() {
		return CourtName;
	}

	public void setCourtName(String courtName) {
		CourtName = courtName;
	}

	public String getCountyName() {
		return CountyName;
	}

	public void setCountyName(String countyName) {
		CountyName = countyName;
	}

}
