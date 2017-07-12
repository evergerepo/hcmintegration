package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_18")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_18 {
	
	@XmlElement(name="EFFECTIVE_DATE")
	private String EFFECTIVE_DATE;
	
	@XmlElement(name="PERSON_ID")
	private String PERSON_ID;
	
	@XmlElement(name="PAYROLL_REL_ACTION_ID")
	private String PAYROLL_REL_ACTION_ID;
	
	@XmlElement(name="PAYROLL_RELATIONSHIP_NUMBER")
	private String PAYROLL_RELATIONSHIP_NUMBER;
	
	@XmlElement(name="ALLOWANCE")
	private String ALLOWANCE;
	
	public String getEFFECTIVE_DATE() {
		return EFFECTIVE_DATE;
	}
	public void setEFFECTIVE_DATE(String eFFECTIVE_DATE) {
		EFFECTIVE_DATE = eFFECTIVE_DATE;
	}
	public String getPERSON_ID() {
		return PERSON_ID;
	}
	public void setPERSON_ID(String pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}
	public String getPAYROLL_REL_ACTION_ID() {
		return PAYROLL_REL_ACTION_ID;
	}
	public void setPAYROLL_REL_ACTION_ID(String pAYROLL_REL_ACTION_ID) {
		PAYROLL_REL_ACTION_ID = pAYROLL_REL_ACTION_ID;
	}
	public String getPAYROLL_RELATIONSHIP_NUMBER() {
		return PAYROLL_RELATIONSHIP_NUMBER;
	}
	public void setPAYROLL_RELATIONSHIP_NUMBER(String pAYROLL_RELATIONSHIP_NUMBER) {
		PAYROLL_RELATIONSHIP_NUMBER = pAYROLL_RELATIONSHIP_NUMBER;
	}
	public String getALLOWANCE() {
		return ALLOWANCE;
	}
	public void setALLOWANCE(String aLLOWANCE) {
		ALLOWANCE = aLLOWANCE;
	}

}
