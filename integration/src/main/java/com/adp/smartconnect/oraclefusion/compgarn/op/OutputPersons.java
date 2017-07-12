package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OutputPersons")
@XmlAccessorType (XmlAccessType.FIELD)
public class OutputPersons {
	
	@XmlElement(name="OutputPerson")
	private List<OutputPerson> outputPerson;

	public List<OutputPerson> getOutputPerson() {
		return outputPerson;
	}

	public void setOutputPerson(List<OutputPerson> outputPerson) {
		this.outputPerson = outputPerson;
	}

}
