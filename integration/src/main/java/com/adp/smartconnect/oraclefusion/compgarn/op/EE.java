package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EE")
@XmlAccessorType (XmlAccessType.FIELD)
public class EE {
	
	@XmlElement(name="LastName")
	private String LastName;
	
	@XmlElement(name="FirstName")
	private String FirstName;
	
	@XmlElement(name="MiddleInitial")
	private String MiddleInitial;
	
	@XmlElement(name="SSN")
	private String SSN;
	
	@XmlElement(name="ShortUserName")
	private String ShortUserName;
	
	@XmlElement(name="UTLIndicator")
	private String UTLIndicator;
	
	@XmlElement(name="EEAddress")
	private EEAddress address;
	
	@XmlElement(name="EEStatus")
	private String EEStatus;
	
	@XmlElement(name="Employed")
	private String Employed;
	
	@XmlElement(name="StatusDate")
	private String StatusDate;
	
	@XmlElement(name="GrossWages")
	private String GrossWages;
	
	@XmlElement(name="DisposableWages")
	private String DisposableWages;
	
	@XmlElement(name="PayFrequency")
	private String PayFrequency;
	
	@XmlElement(name="MandatoryPension")
	private String MandatoryPension;
	
	@XmlElement(name="EEPayType")
	private String EEPayType;
	
	@XmlElement(name="EELastCheckDate")
	private String EELastCheckDate;
	
	@XmlElement(name="PayPeriodBeginDate")
	private String PayPeriodBeginDate;
	
	@XmlElement(name="PayPeriodEndDate")
	private String PayPeriodEndDate;
	
	@XmlElement(name="FutureCheckDates")
	private FutureCheckDates futureCheckDates;
	
	@XmlElement(name="DependentCount")
	private String DependentCount;
	
	@XmlElement(name="DisabilityEarnings")
	private String DisabilityEarnings;
	
	@XmlElement(name="RetirementEarnings")
	private String RetirementEarnings;
	
	@XmlElement(name="AnnualSalary")
	private String AnnualSalary;
	
	@XmlElement(name="TotalPayrollTaxes")
	private String TotalPayrollTaxes;
	
	@XmlElement(name="FederalIncomeTaxes")
	private String FederalIncomeTaxes;
	
	@XmlElement(name="StateIncomeTaxes")
	private String StateIncomeTaxes;
	
	@XmlElement(name="FICATax")
	private String FICATax;
	
	@XmlElement(name="Company")
	private Company company;

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getMiddleInitial() {
		return MiddleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		MiddleInitial = middleInitial;
	}

	public String getSSN() {
		return SSN;
	}

	public void setSSN(String sSN) {
		SSN = sSN;
	}

	public String getShortUserName() {
		return ShortUserName;
	}

	public void setShortUserName(String shortUserName) {
		ShortUserName = shortUserName;
	}

	public String getUTLIndicator() {
		return UTLIndicator;
	}

	public void setUTLIndicator(String uTLIndicator) {
		UTLIndicator = uTLIndicator;
	}

	public EEAddress getAddress() {
		return address;
	}

	public void setAddress(EEAddress address) {
		this.address = address;
	}

	public String getEEStatus() {
		return EEStatus;
	}

	public void setEEStatus(String eEStatus) {
		EEStatus = eEStatus;
	}

	public String getEmployed() {
		return Employed;
	}

	public void setEmployed(String employed) {
		Employed = employed;
	}

	public String getStatusDate() {
		return StatusDate;
	}

	public void setStatusDate(String statusDate) {
		StatusDate = statusDate;
	}

	public String getGrossWages() {
		return GrossWages;
	}

	public void setGrossWages(String grossWages) {
		GrossWages = grossWages;
	}

	public String getDisposableWages() {
		return DisposableWages;
	}

	public void setDisposableWages(String disposableWages) {
		DisposableWages = disposableWages;
	}

	public String getPayFrequency() {
		return PayFrequency;
	}

	public void setPayFrequency(String payFrequency) {
		PayFrequency = payFrequency;
	}

	public String getMandatoryPension() {
		return MandatoryPension;
	}

	public void setMandatoryPension(String mandatoryPension) {
		MandatoryPension = mandatoryPension;
	}

	public String getEEPayType() {
		return EEPayType;
	}

	public void setEEPayType(String eEPayType) {
		EEPayType = eEPayType;
	}

	public String getEELastCheckDate() {
		return EELastCheckDate;
	}

	public void setEELastCheckDate(String eELastCheckDate) {
		EELastCheckDate = eELastCheckDate;
	}

	public String getPayPeriodBeginDate() {
		return PayPeriodBeginDate;
	}

	public void setPayPeriodBeginDate(String payPeriodBeginDate) {
		PayPeriodBeginDate = payPeriodBeginDate;
	}

	public String getPayPeriodEndDate() {
		return PayPeriodEndDate;
	}

	public void setPayPeriodEndDate(String payPeriodEndDate) {
		PayPeriodEndDate = payPeriodEndDate;
	}

	public FutureCheckDates getFutureCheckDates() {
		return futureCheckDates;
	}

	public void setFutureCheckDates(FutureCheckDates futureCheckDates) {
		this.futureCheckDates = futureCheckDates;
	}

	public String getDependentCount() {
		return DependentCount;
	}

	public void setDependentCount(String dependentCount) {
		DependentCount = dependentCount;
	}

	public String getDisabilityEarnings() {
		return DisabilityEarnings;
	}

	public void setDisabilityEarnings(String disabilityEarnings) {
		DisabilityEarnings = disabilityEarnings;
	}

	public String getRetirementEarnings() {
		return RetirementEarnings;
	}

	public void setRetirementEarnings(String retirementEarnings) {
		RetirementEarnings = retirementEarnings;
	}

	public String getAnnualSalary() {
		return AnnualSalary;
	}

	public void setAnnualSalary(String annualSalary) {
		AnnualSalary = annualSalary;
	}

	public String getTotalPayrollTaxes() {
		return TotalPayrollTaxes;
	}

	public void setTotalPayrollTaxes(String totalPayrollTaxes) {
		TotalPayrollTaxes = totalPayrollTaxes;
	}

	public String getFederalIncomeTaxes() {
		return FederalIncomeTaxes;
	}

	public void setFederalIncomeTaxes(String federalIncomeTaxes) {
		FederalIncomeTaxes = federalIncomeTaxes;
	}

	public String getStateIncomeTaxes() {
		return StateIncomeTaxes;
	}

	public void setStateIncomeTaxes(String stateIncomeTaxes) {
		StateIncomeTaxes = stateIncomeTaxes;
	}

	public String getFICATax() {
		return FICATax;
	}

	public void setFICATax(String fICATax) {
		FICATax = fICATax;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
