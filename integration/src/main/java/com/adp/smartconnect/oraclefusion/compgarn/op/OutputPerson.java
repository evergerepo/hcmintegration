package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OutputPerson")
@XmlAccessorType (XmlAccessType.FIELD)
public class OutputPerson {
	
	@XmlElement
	private String nm;
	
	@XmlElement
	private String st;
	
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	
	

}
