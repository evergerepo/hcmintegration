package com.adp.smartconnect.oraclefusion.compgarn.op;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Payment")
@XmlAccessorType (XmlAccessType.FIELD)
public class Payment {
	
	@XmlElement(name="ExemptAmount")
	private String ExemptAmount;
	
	@XmlElement(name="Frequency")
	private String Frequency;
	
	@XmlElement(name="DependentOrExemptionCount")
	private String DependentOrExemptionCount;
	
	@XmlElement(name="MemorandumLine")
	private List<String> MemorandumLine;
	
	@XmlElement(name="PaymentSchedule")
	private String PaymentSchedule;
	
	@XmlElement(name="AccruingPeriod")
	private String AccruingPeriod;
	
	@XmlElement(name="AccruingAmount")
	private String AccruingAmount;
	
	@XmlElement(name="ERFeeAmount")
	private String ERFeeAmount;
	
	@XmlElement(name="ERFeePercentage")
	private String ERFeePercentage;

	public String getExemptAmount() {
		return ExemptAmount;
	}

	public void setExemptAmount(String exemptAmount) {
		ExemptAmount = exemptAmount;
	}

	public String getFrequency() {
		return Frequency;
	}

	public void setFrequency(String frequency) {
		Frequency = frequency;
	}

	public String getDependentOrExemptionCount() {
		return DependentOrExemptionCount;
	}

	public void setDependentOrExemptionCount(String dependentOrExemptionCount) {
		DependentOrExemptionCount = dependentOrExemptionCount;
	}

	public List<String> getMemorandumLine() {
		return MemorandumLine;
	}

	public void setMemorandumLine(List<String> memorandumLine) {
		MemorandumLine = memorandumLine;
	}

	public String getPaymentSchedule() {
		return PaymentSchedule;
	}

	public void setPaymentSchedule(String paymentSchedule) {
		PaymentSchedule = paymentSchedule;
	}

	public String getAccruingPeriod() {
		return AccruingPeriod;
	}

	public void setAccruingPeriod(String accruingPeriod) {
		AccruingPeriod = accruingPeriod;
	}

	public String getAccruingAmount() {
		return AccruingAmount;
	}

	public void setAccruingAmount(String accruingAmount) {
		AccruingAmount = accruingAmount;
	}

	public String getERFeeAmount() {
		return ERFeeAmount;
	}

	public void setERFeeAmount(String eRFeeAmount) {
		ERFeeAmount = eRFeeAmount;
	}

	public String getERFeePercentage() {
		return ERFeePercentage;
	}

	public void setERFeePercentage(String eRFeePercentage) {
		ERFeePercentage = eRFeePercentage;
	}
	

}
