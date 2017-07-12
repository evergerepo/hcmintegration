package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EELiens")
@XmlAccessorType (XmlAccessType.FIELD)
public class EELiens {
	
	@XmlElement(name="EELien")
	private List<EELien> eeliens;

	public List<EELien> getEeliens() {
		return eeliens;
	}

	public void setEeliens(List<EELien> eeliens) {
		this.eeliens = eeliens;
	}
}
