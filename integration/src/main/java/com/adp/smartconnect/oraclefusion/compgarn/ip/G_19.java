package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_19")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_19 {
	
	@XmlElement(name="PAY_TYPE")
	private String PAY_TYPE;
	
	@XmlElement(name="PAYROLL_NAME")
	private String PAYROLL_NAME;
	
	@XmlElement(name="START_DATE")
	private String START_DATE;
	
	@XmlElement(name="END_DATE")
	private String END_DATE;
	
	@XmlElement(name="CHECK_DATE")
	private String CHECK_DATE;
	
	@XmlElement(name="STAT_NUM")
	private String STAT_NUM;
	
	@XmlElement(name="START_DATE1")
	private String START_DATE1;
	
	@XmlElement(name="PERSON_ID")
	private String PERSON_ID;
	
	@XmlElement(name="PAYROLL_REL_ACTION_ID")
	private String PAYROLL_REL_ACTION_ID;
	
	@XmlElement(name="EFFECTIVE_DATE")
	private String EFFECTIVE_DATE;
	
	public String getPAY_TYPE() {
		return PAY_TYPE;
	}
	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}
	public String getPAYROLL_NAME() {
		return PAYROLL_NAME;
	}
	public void setPAYROLL_NAME(String pAYROLL_NAME) {
		PAYROLL_NAME = pAYROLL_NAME;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}
	public String getCHECK_DATE() {
		return CHECK_DATE;
	}
	public void setCHECK_DATE(String cHECK_DATE) {
		CHECK_DATE = cHECK_DATE;
	}
	public String getSTAT_NUM() {
		return STAT_NUM;
	}
	public void setSTAT_NUM(String sTAT_NUM) {
		STAT_NUM = sTAT_NUM;
	}
	public String getSTART_DATE1() {
		return START_DATE1;
	}
	public void setSTART_DATE1(String sTART_DATE1) {
		START_DATE1 = sTART_DATE1;
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
	public String getEFFECTIVE_DATE() {
		return EFFECTIVE_DATE;
	}
	public void setEFFECTIVE_DATE(String eFFECTIVE_DATE) {
		EFFECTIVE_DATE = eFFECTIVE_DATE;
	}

}
