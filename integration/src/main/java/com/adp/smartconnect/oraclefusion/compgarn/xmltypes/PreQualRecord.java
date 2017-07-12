package com.adp.smartconnect.oraclefusion.compgarn.xmltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PreQualRecord")
@XmlAccessorType (XmlAccessType.FIELD)
public class PreQualRecord {
	
	@XmlElement
	private String DocID;
	
	@XmlElement
	private String EEFirstName;
	
	@XmlElement
	private String EEMiddleInitial;
	
	@XmlElement
	private String EELastName;
	
	@XmlElement
	private String SSN;
	
	@XmlElement
	private String SSNFormatted;
	
	@XmlElement
	private String State;
	
	@XmlElement
	private String ADPStatus;
	
	@XmlElement
	private String StatusDate;
	
	@XmlElement
	private String StatusDesc;
	
	@XmlElement
	private String TransID;
	
	@XmlElement(name="CompanyInfo")
	private CompanyInfo companyInfo;
	
	@XmlElement(name="EmployeeAddress")
	private EmployeeAddress employeeAddress;
	
	public String getDocID() {
		return DocID;
	}
	public void setDocID(String docID) {
		DocID = docID;
	}
	public String getEEFirstName() {
		return EEFirstName;
	}
	public void setEEFirstName(String eEFirstName) {
		EEFirstName = eEFirstName;
	}
	public String getEEMiddleInitial() {
		return EEMiddleInitial;
	}
	public void setEEMiddleInitial(String eEMiddleInitial) {
		EEMiddleInitial = eEMiddleInitial;
	}
	public String getEELastName() {
		return EELastName;
	}
	public void setEELastName(String eELastName) {
		EELastName = eELastName;
	}
	public String getSSN() {
		return SSN;
	}
	public void setSSN(String sSN) {
		SSN = sSN;
	}
	public String getTransID() {
		return TransID;
	}
	public void setTransID(String transID) {
		TransID = transID;
	}
	public String getSSNFormatted() {
		return SSNFormatted;
	}
	public void setSSNFormatted(String sSNFormatted) {
		SSNFormatted = sSNFormatted;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getADPStatus() {
		return ADPStatus;
	}
	public void setADPStatus(String aDPStatus) {
		ADPStatus = aDPStatus;
	}
	public String getStatusDate() {
		return StatusDate;
	}
	public void setStatusDate(String statusDate) {
		StatusDate = statusDate;
	}
	public String getStatusDesc() {
		return StatusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		StatusDesc = statusDesc;
	}

	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}
	public EmployeeAddress getEmployeeAddress() {
		return employeeAddress;
	}
	public void setEmployeeAddress(EmployeeAddress employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

}
