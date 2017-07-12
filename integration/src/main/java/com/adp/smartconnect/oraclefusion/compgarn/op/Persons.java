package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Persons")
@XmlAccessorType (XmlAccessType.FIELD)
public class Persons {
	
	@XmlElement(name="Person")
	private List<Person> person;

	public List<Person> getPerson() {
		return person;
	}

	public void setPerson(List<Person> person) {
		this.person = person;
	}

}
