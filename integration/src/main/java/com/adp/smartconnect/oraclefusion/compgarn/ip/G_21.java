package com.adp.smartconnect.oraclefusion.compgarn.ip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="G_21")
@XmlAccessorType (XmlAccessType.FIELD)
public class G_21 {
	
	@XmlElement(name="REPORTING_NAME")
	private String REPORTING_NAME;
	
	@XmlElement(name="RUN")
	private String RUN;
	
	@XmlElement(name="YTD")
	private String YTD;
	
	@XmlElement(name="EFFECTIVE_DATE")
	private String EFFECTIVE_DATE;
	
	@XmlElement(name="PERSON_ID")
	private String PERSON_ID;
	
	@XmlElement(name="PAYROLL_REL_ACTION_ID")
	private String PAYROLL_REL_ACTION_ID;
	
	@XmlElement(name="PAYROLL_RELATIONSHIP_NUMBER")
	private String PAYROLL_RELATIONSHIP_NUMBER;

	public String getRUN() {
		return RUN;
	}

	public void setRUN(String rUN) {
		RUN = rUN;
	}
	
	

}
