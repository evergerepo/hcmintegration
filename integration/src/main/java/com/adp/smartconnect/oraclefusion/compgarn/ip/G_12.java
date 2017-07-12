package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_12")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_12 {
	
	@XmlElement(name="SOURCE_ID")
	private String SOURCE_ID;
	
	@XmlElement(name="OVERRIDE_VALUE")
	private String OVERRIDE_VALUE;
	
	@XmlElement(name="BASE_NAME")
	private String BASE_NAME;
	
	@XmlElement(name="PROMPT")
	private String PROMPT;
	
	@XmlElement(name="TYPE")
	private String TYPE;

	public String getSOURCE_ID() {
		return SOURCE_ID;
	}

	public void setSOURCE_ID(String sOURCE_ID) {
		SOURCE_ID = sOURCE_ID;
	}

	public String getOVERRIDE_VALUE() {
		return OVERRIDE_VALUE;
	}

	public void setOVERRIDE_VALUE(String oVERRIDE_VALUE) {
		OVERRIDE_VALUE = oVERRIDE_VALUE;
	}

	public String getBASE_NAME() {
		return BASE_NAME;
	}

	public void setBASE_NAME(String bASE_NAME) {
		BASE_NAME = bASE_NAME;
	}

	public String getPROMPT() {
		return PROMPT;
	}

	public void setPROMPT(String pROMPT) {
		PROMPT = pROMPT;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

}
