package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EmployeeGarnishments")
@XmlAccessorType (XmlAccessType.FIELD)
public class EmployeeGarnishmentsResp {
	
	@XmlElement(name="PQQ")
	private PQQ pqq;

	public PQQ getPqq() {
		return pqq;
	}

	public void setPqq(PQQ pqq) {
		this.pqq = pqq;
	}
	
	

}
