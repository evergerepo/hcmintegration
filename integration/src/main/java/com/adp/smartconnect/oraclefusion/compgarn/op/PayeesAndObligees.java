package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PayeesAndObligees")
@XmlAccessorType (XmlAccessType.FIELD)
public class PayeesAndObligees {
	
	@XmlElement
	private String PayableTo;
	
	@XmlElement(name="Payee")
	private Payee payee;
	
	@XmlElement
	private Obligee obligee;

	public String getPayableTo() {
		return PayableTo;
	}

	public void setPayableTo(String payableTo) {
		PayableTo = payableTo;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	public Obligee getObligee() {
		return obligee;
	}

	public void setObligee(Obligee obligee) {
		this.obligee = obligee;
	}

}
