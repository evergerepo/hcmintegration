package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Payee")
@XmlAccessorType (XmlAccessType.FIELD)
public class Payee {
	
	@XmlElement(name="Name")
	private String Name;
	
	@XmlElement(name="Address")
	private PayeeAddress address;
	
	@XmlElement(name="PayeeCode")
	private String PayeeCode;
	
	@XmlElement(name="ContactPhone")
	private String ContactPhone;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public PayeeAddress getAddress() {
		return address;
	}

	public void setAddress(PayeeAddress address) {
		this.address = address;
	}

	public String getPayeeCode() {
		return PayeeCode;
	}

	public void setPayeeCode(String payeeCode) {
		PayeeCode = payeeCode;
	}

	public String getContactPhone() {
		return ContactPhone;
	}

	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}

}
