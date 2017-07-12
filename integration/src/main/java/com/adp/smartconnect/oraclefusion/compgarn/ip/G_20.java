package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_20")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_20 {
	
	@XmlElement(name="PAYROLL_RELATIONSHIP_NUMBER")
	private String PAYROLL_RELATIONSHIP_NUMBER;
	
	@XmlElement(name="PRIORITY")
	private String PRIORITY;
	
	@XmlElement(name="PAYMENT_AMOUNT_TYPE")
	private String PAYMENT_AMOUNT_TYPE;
	
	@XmlElement(name="AMOUNT")
	private String AMOUNT;
	
	@XmlElement(name="PERCENTAGE")
	private String PERCENTAGE;
	
	@XmlElement(name="BANK_ACCOUNT_NUM")
	private String BANK_ACCOUNT_NUM;
		
	@XmlElement(name="BANK_NAME")
	private String BANK_NAME;

	public String getBANK_NAME() {
		return BANK_NAME;
	}

	public void setBANK_NAME(String bANK_NAME) {
		BANK_NAME = bANK_NAME;
	}
	
}
