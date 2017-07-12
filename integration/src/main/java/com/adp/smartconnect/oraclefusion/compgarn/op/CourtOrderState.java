package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CourtOrderState")
@XmlAccessorType (XmlAccessType.FIELD)
public class CourtOrderState {
	
	@XmlElement
	private String Code;
	
	@XmlElement
	private String FIPSCode;
	
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getFIPSCode() {
		return FIPSCode;
	}
	public void setFIPSCode(String fIPSCode) {
		FIPSCode = fIPSCode;
	}
	
	

}
