package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FutureCheckDates {
	
	@XmlElement(name="CheckDate")
	private List<String> CheckDate;

	public List<String> getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(List<String> checkDate) {
		CheckDate = checkDate;
	}
	
}
