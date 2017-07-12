package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PQFile")
@XmlAccessorType (XmlAccessType.FIELD)
public class PQFile {
	
	@XmlAttribute(name="TransmissionID")
	private String TransmissionID;
	
	@XmlElement(name="TransmissionID")
	private String TransId;
	
	@XmlElement(name="PreQualRecordList")
	private PreQualRecordList preQualRecordList;

	public String getTransmissionID() {
		return TransmissionID;
	}

	public PreQualRecordList getPreQualRecordList() {
		return preQualRecordList;
	}

	public void setPreQualRecordList(PreQualRecordList preQualRecordList) {
		this.preQualRecordList = preQualRecordList;
	}

	public void setTransmissionID(String transmissionID) {
		TransmissionID = transmissionID;
	}

	public String getTransId() {
		return TransId;
	}

	public void setTransId(String transId) {
		TransId = transId;
	}


}
