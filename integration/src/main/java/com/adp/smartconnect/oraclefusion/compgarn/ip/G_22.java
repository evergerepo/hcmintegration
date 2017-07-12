package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_22")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_22 {
	
	@XmlElement(name="REPORTING_NAME")
	private String REPORTING_NAME;
	
	@XmlElement(name="YTD")
	private String YTD;
	
	@XmlElement(name="PAYROLL_REL_ACTION_ID")
	private String PAYROLL_REL_ACTION_ID;
	
	@XmlElement(name="PAYROLL_RELATIONSHIP_NUMBER")
	private String PAYROLL_RELATIONSHIP_NUMBER;
	
	@XmlElement(name="PERSON_ID")
	private String PERSON_ID;
	
	@XmlElement(name="RUN")
	private String RUN;

	public String getRUN() {
		return RUN;
	}

	public void setRUN(String rUN) {
		RUN = rUN;
	}

	public String getREPORTING_NAME() {
		return REPORTING_NAME;
	}

	public void setREPORTING_NAME(String rEPORTING_NAME) {
		REPORTING_NAME = rEPORTING_NAME;
	}

	public String getYTD() {
		return YTD;
	}

	public void setYTD(String yTD) {
		YTD = yTD;
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

	public String getPERSON_ID() {
		return PERSON_ID;
	}

	public void setPERSON_ID(String pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}

}
