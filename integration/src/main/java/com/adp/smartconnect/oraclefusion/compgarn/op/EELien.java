package com.adp.smartconnect.oraclefusion.compgarn.op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EELien")
@XmlAccessorType (XmlAccessType.FIELD)
public class EELien {
	
	@XmlAttribute
	private String DocID;
	@XmlElement(name="LienType")
	private LienType lienType;
	@XmlElement(name="CaseID")
	private String CaseID;
	@XmlElement(name="LienNumber")
	private String LienNumber;
	@XmlElement(name="LienStatus")
	private String LienStatus;
	@XmlElement(name="LienStartDate")
	private String LienStartDate;
	@XmlElement(name="LienEndDate")
	private String LienEndDate;
	@XmlElement(name="GoalAmount")
	private String GoalAmount;
	@XmlElement(name="DeductedAmount")
	private String DeductedAmount;
	@XmlElement(name="DeductionAmountFrequency")
	private String DeductionAmountFrequency;
	@XmlElement(name="DeductedPercentage")
	private String DeductedPercentage;
	@XmlElement(name="MonthlyLimit")
	private String MonthlyLimit;
	@XmlElement(name="DeductionCalc")
	private String DeductionCalc;
	@XmlElement(name="GarnishmentRule")
	private String GarnishmentRule;
	@XmlElement(name="GarnSupportType")
	private String GarnSupportType;
	@XmlElement(name="AgencyFeeAmount")
	private String AgencyFeeAmount;
	@XmlElement(name="AgencyFeePercentage")
	private String AgencyFeePercentage;
	@XmlElement(name="LetterInfo")
	private String LetterInfo;
	@XmlElement(name="Priority")
	private String Priority;
	@XmlElement(name="ProRationPercentage")
	private String ProRationPercentage;
	@XmlElement(name="ProRationAmount")
	private String ProRationAmount;
	@XmlElement(name="NotificationIndicator")
	private String NotificationIndicator;
	@XmlElement(name="InterrogatoryIndicator")
	private String InterrogatoryIndicator;
	@XmlElement(name="FinalAnswerBankruptcyIndicator")
	private String FinalAnswerBankruptcyIndicator;
	@XmlElement(name="AddressType")
	private String AddressType;
	@XmlElement(name="InternalField")
	private String InternalField;
	@XmlElement(name="CourtOrderState")
	private CourtOrderState courtOrderState;
	@XmlElement(name="PayeesAndObligees")
	private PayeesAndObligees payeesAndObligees;
	@XmlElement(name="Issuer")
	private Issuer issuer;
	@XmlElement(name="BankName")
	private String BankName;
	@XmlElement(name="EE")
	private EE ee;
	@XmlElement(name="Payment")
	private Payment payment;
	public String getDocID() {
		return DocID;
	}
	public void setDocID(String docID) {
		DocID = docID;
	}
	public LienType getLienType() {
		return lienType;
	}
	public void setLienType(LienType lienType) {
		this.lienType = lienType;
	}
	public String getCaseID() {
		return CaseID;
	}
	public void setCaseID(String caseID) {
		CaseID = caseID;
	}
	public String getLienNumber() {
		return LienNumber;
	}
	public void setLienNumber(String lienNumber) {
		LienNumber = lienNumber;
	}
	public String getLienStatus() {
		return LienStatus;
	}
	public void setLienStatus(String lienStatus) {
		LienStatus = lienStatus;
	}
	public String getLienStartDate() {
		return LienStartDate;
	}
	public void setLienStartDate(String lienStartDate) {
		LienStartDate = lienStartDate;
	}
	public String getLienEndDate() {
		return LienEndDate;
	}
	public void setLienEndDate(String lienEndDate) {
		LienEndDate = lienEndDate;
	}
	public String getGoalAmount() {
		return GoalAmount;
	}
	public void setGoalAmount(String goalAmount) {
		GoalAmount = goalAmount;
	}
	public String getDeductedAmount() {
		return DeductedAmount;
	}
	public void setDeductedAmount(String deductedAmount) {
		DeductedAmount = deductedAmount;
	}
	public String getDeductionAmountFrequency() {
		return DeductionAmountFrequency;
	}
	public void setDeductionAmountFrequency(String deductionAmountFrequency) {
		DeductionAmountFrequency = deductionAmountFrequency;
	}
	public String getDeductedPercentage() {
		return DeductedPercentage;
	}
	public void setDeductedPercentage(String deductedPercentage) {
		DeductedPercentage = deductedPercentage;
	}
	public String getMonthlyLimit() {
		return MonthlyLimit;
	}
	public void setMonthlyLimit(String monthlyLimit) {
		MonthlyLimit = monthlyLimit;
	}
	public String getDeductionCalc() {
		return DeductionCalc;
	}
	public void setDeductionCalc(String deductionCalc) {
		DeductionCalc = deductionCalc;
	}
	public String getGarnishmentRule() {
		return GarnishmentRule;
	}
	public void setGarnishmentRule(String garnishmentRule) {
		GarnishmentRule = garnishmentRule;
	}
	public String getGarnSupportType() {
		return GarnSupportType;
	}
	public void setGarnSupportType(String garnSupportType) {
		GarnSupportType = garnSupportType;
	}
	public String getAgencyFeeAmount() {
		return AgencyFeeAmount;
	}
	public void setAgencyFeeAmount(String agencyFeeAmount) {
		AgencyFeeAmount = agencyFeeAmount;
	}
	public String getAgencyFeePercentage() {
		return AgencyFeePercentage;
	}
	public void setAgencyFeePercentage(String agencyFeePercentage) {
		AgencyFeePercentage = agencyFeePercentage;
	}
	public String getLetterInfo() {
		return LetterInfo;
	}
	public void setLetterInfo(String letterInfo) {
		LetterInfo = letterInfo;
	}
	public String getPriority() {
		return Priority;
	}
	public void setPriority(String priority) {
		Priority = priority;
	}
	public String getProRationPercentage() {
		return ProRationPercentage;
	}
	public void setProRationPercentage(String proRationPercentage) {
		ProRationPercentage = proRationPercentage;
	}
	public String getProRationAmount() {
		return ProRationAmount;
	}
	public void setProRationAmount(String proRationAmount) {
		ProRationAmount = proRationAmount;
	}
	public String getNotificationIndicator() {
		return NotificationIndicator;
	}
	public void setNotificationIndicator(String notificationIndicator) {
		NotificationIndicator = notificationIndicator;
	}
	public String getInterrogatoryIndicator() {
		return InterrogatoryIndicator;
	}
	public void setInterrogatoryIndicator(String interrogatoryIndicator) {
		InterrogatoryIndicator = interrogatoryIndicator;
	}
	public String getFinalAnswerBankruptcyIndicator() {
		return FinalAnswerBankruptcyIndicator;
	}
	public void setFinalAnswerBankruptcyIndicator(String finalAnswerBankruptcyIndicator) {
		FinalAnswerBankruptcyIndicator = finalAnswerBankruptcyIndicator;
	}
	public String getAddressType() {
		return AddressType;
	}
	public void setAddressType(String addressType) {
		AddressType = addressType;
	}
	public String getInternalField() {
		return InternalField;
	}
	public void setInternalField(String internalField) {
		InternalField = internalField;
	}
	public CourtOrderState getCourtOrderState() {
		return courtOrderState;
	}
	public void setCourtOrderState(CourtOrderState courtOrderState) {
		this.courtOrderState = courtOrderState;
	}
	public PayeesAndObligees getPayeesAndObligees() {
		return payeesAndObligees;
	}
	public void setPayeesAndObligees(PayeesAndObligees payeesAndObligees) {
		this.payeesAndObligees = payeesAndObligees;
	}
	public Issuer getIssuer() {
		return issuer;
	}
	public void setIssuer(Issuer issuer) {
		this.issuer = issuer;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	public EE getEe() {
		return ee;
	}
	public void setEe(EE ee) {
		this.ee = ee;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
