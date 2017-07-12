package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PreQualResponse")
@XmlAccessorType (XmlAccessType.FIELD)
public class PreQualResponse {
	
	@XmlElement(name="TransID")
	private String TransID;
	
	@XmlAttribute(name="TransID")
	private String tID;
	
	@XmlElement(name="PreQualRecord")
	private List<PreQualRecordOp> qualRecords;

	public String getTransID() {
		return TransID;
	}

	public void setTransID(String transID) {
		TransID = transID;
	}

	public List<PreQualRecordOp> getQualRecords() {
		return qualRecords;
	}

	public void setQualRecords(List<PreQualRecordOp> qualRecords) {
		this.qualRecords = qualRecords;
	}

	public String gettID() {
		return tID;
	}

	public void settID(String tID) {
		this.tID = tID;
	}

}
