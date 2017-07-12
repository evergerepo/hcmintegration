package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PQQ")
@XmlAccessorType (XmlAccessType.FIELD)
public class PQQReq {
	
	@XmlElement(name="PreQualRequest")
	private PreQualRequest preQualRequest;

	public PreQualRequest getPreQualRequest() {
		return preQualRequest;
	}

	public void setPreQualRequest(PreQualRequest preQualRequest) {
		this.preQualRequest = preQualRequest;
	}

}
