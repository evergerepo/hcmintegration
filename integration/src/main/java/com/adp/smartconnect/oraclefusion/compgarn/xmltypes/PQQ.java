package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PQQ")
@XmlAccessorType (XmlAccessType.FIELD)
public class PQQ {
	
	@XmlElement(name="PreQualReponse")
	private PreQualResponse preQualResponse;

	public PreQualResponse getPreQualResponse() {
		return preQualResponse;
	}

	public void setPreQualResponse(PreQualResponse preQualResponse) {
		this.preQualResponse = preQualResponse;
	}
	
	

}
