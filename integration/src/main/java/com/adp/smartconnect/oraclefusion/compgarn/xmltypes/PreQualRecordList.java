package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class PreQualRecordList {
	
	@XmlElement(name="PreQualRecord")
	private List<PreQualRecord> qualRecords;

	public List<PreQualRecord> getQualRecords() {
		return qualRecords;
	}

	public void setQualRecords(List<PreQualRecord> qualRecords) {
		this.qualRecords = qualRecords;
	}

}
