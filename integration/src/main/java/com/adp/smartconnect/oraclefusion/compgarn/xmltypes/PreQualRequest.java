package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PreQualRequest")
@XmlAccessorType (XmlAccessType.FIELD)
public class PreQualRequest {
	
	@XmlAttribute(name="TransID")
	private String TransID;
	
	@XmlElement(name="PreQualRecord")
	private List<PreQualRecord> preQualRecords;

	public String getTransID() {
		return TransID;
	}

	public void setTransID(String transID) {
		TransID = transID;
	}

	public List<PreQualRecord> getPreQualRecords() {
		return preQualRecords;
	}

	public void setPreQualRecords(List<PreQualRecord> preQualRecords) {
		this.preQualRecords = preQualRecords;
	}

}
