package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="LienType")
@XmlAccessorType (XmlAccessType.FIELD)
public class LienType {
	
	@XmlElement(name="WritOfGarnishment")
	private WritOfGarnishment writOfGarnishment;

	public WritOfGarnishment getWritOfGarnishment() {
		return writOfGarnishment;
	}

	public void setWritOfGarnishment(WritOfGarnishment writOfGarnishment) {
		this.writOfGarnishment = writOfGarnishment;
	}

}
