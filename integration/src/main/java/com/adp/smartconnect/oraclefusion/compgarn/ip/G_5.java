package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_5")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_5 {
	
	@XmlElement(name="BATCH_NAME")
	private String BATCH_NAME;
	
	@XmlElement(name="BATCH_STATUS")
	private String BATCH_STATUS;
	
	@XmlElement(name="LN_STATUS")
	private String LN_STATUS;
	
	@XmlElement(name="COMPONENT_NAME")
	private String COMPONENT_NAME;
	
	@XmlElement(name="PAY_NUMBER")
	private String PAY_NUMBER;
	
	@XmlElement(name="PAY_REL_NUMBER")
	private String PAY_REL_NUMBER;
	
	@XmlElement(name="MESSAGE_TEXT")
	private String MESSAGE_TEXT;
	
	@XmlElement(name="G_1")
	private G_1 g1;

	public String getBATCH_NAME() {
		return BATCH_NAME;
	}

	public void setBATCH_NAME(String bATCH_NAME) {
		BATCH_NAME = bATCH_NAME;
	}

	public String getBATCH_STATUS() {
		return BATCH_STATUS;
	}

	public void setBATCH_STATUS(String bATCH_STATUS) {
		BATCH_STATUS = bATCH_STATUS;
	}

	public String getLN_STATUS() {
		return LN_STATUS;
	}

	public void setLN_STATUS(String lN_STATUS) {
		LN_STATUS = lN_STATUS;
	}

	public String getCOMPONENT_NAME() {
		return COMPONENT_NAME;
	}

	public void setCOMPONENT_NAME(String cOMPONENT_NAME) {
		COMPONENT_NAME = cOMPONENT_NAME;
	}

	public String getPAY_NUMBER() {
		return PAY_NUMBER;
	}

	public void setPAY_NUMBER(String pAY_NUMBER) {
		PAY_NUMBER = pAY_NUMBER;
	}

	public String getPAY_REL_NUMBER() {
		return PAY_REL_NUMBER;
	}

	public void setPAY_REL_NUMBER(String pAY_REL_NUMBER) {
		PAY_REL_NUMBER = pAY_REL_NUMBER;
	}

	public G_1 getG1() {
		return g1;
	}

	public void setG1(G_1 g1) {
		this.g1 = g1;
	}

	public String getMESSAGE_TEXT() {
		return MESSAGE_TEXT;
	}

	public void setMESSAGE_TEXT(String mESSAGE_TEXT) {
		MESSAGE_TEXT = mESSAGE_TEXT;
	}
	

}
