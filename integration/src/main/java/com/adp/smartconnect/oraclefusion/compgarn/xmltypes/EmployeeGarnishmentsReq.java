package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EmployeeGarnishments")
@XmlAccessorType (XmlAccessType.FIELD)
public class EmployeeGarnishmentsReq {
	
	@XmlElement(name="PQQ")
	private PQQReq pqqReq;

	public PQQReq getPqqReq() {
		return pqqReq;
	}

	public void setPqqReq(PQQReq pqqReq) {
		this.pqqReq = pqqReq;
	}
	
	



}
