package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dozer.DozerBeanMapper;

public class NotificationFormatChangeTest {

	public static void main(String[] args) throws Exception {
		

		
//		Object record = null;
//		
		DozerBeanMapper mapper = new DozerBeanMapper();
		List<String> mappingFileUrls = new ArrayList<String>();
		mappingFileUrls.add("com/everge/mapping/person-to-outputperson-mapping.xml");
		mapper.setMappingFiles(mappingFileUrls);
//		
//		
//		
		JAXBContext jaxbContext1 = JAXBContext.newInstance(OutputPersons.class);
		Marshaller jaxbMarshaller = jaxbContext1.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		
//		List<OutputPerson> outputPersons = new ArrayList<>();
//		File outputFile = new File("/Users/abhisheksingh/ddrive/everge_ws/notifFile/2.xml");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Persons.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		Persons p = (Persons)jaxbUnMarshaller.unmarshal(new File("/Users/abhisheksingh/ddrive/everge_ws/notifFile/1.xml"));
		
		List<Person> personLst = p.getPerson();
		File f = new File("/Users/abhisheksingh/ddrive/everge_ws/notifFile/2.xml");
		List<OutputPerson> ops = new ArrayList<>();
		for(Person p1 : personLst) {
			OutputPerson op = new OutputPerson();
			mapper.map(p1, op);
			ops.add(op);
		}
		OutputPersons persons = new OutputPersons();
		persons.setOutputPerson(ops);
		jaxbMarshaller.marshal(persons, f);
				
	
		
	}

}
