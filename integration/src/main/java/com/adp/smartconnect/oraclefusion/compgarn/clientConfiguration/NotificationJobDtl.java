package com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NotificationJobDtl")
@XmlAccessorType (XmlAccessType.FIELD)
public class NotificationJobDtl {
	
	@XmlElement(name="NotificationJobUserName")
	private String NotificationJobUserName;
	
	@XmlElement(name="NotificationJobPassword")
	private String NotificationJobPassword;
	
	@XmlElement(name="NotificationJobSubmitFlowUrl")
	private String NotificationJobSubmitFlowUrl;
	
	@XmlElement(name="NotificationJobGetFlowStatusUrl")
	private String NotificationJobGetFlowStatusUrl;
	
	@XmlElement(name="NotificationJobKeyStorePath")
	private String NotificationJobKeyStorePath;
	
	@XmlElement(name="NotificationJobKeyStorePwd")
	private String NotificationJobKeyStorePwd;
	
	@XmlElement(name="NotificationReportPath")
	private String NotificationReportPath;
	
	@XmlElement(name="ReportServiceUrl")
	private String ReportServiceUrl;

	@XmlElement(name="LegislativeDataGroupName")
	private String LegislativeDataGroupName;
	
	@XmlElement(name="LienInfoTransformationFormula")
	private String LienInfoTransformationFormula;
	
	@XmlElement(name="LienAddntlInfoTransformationFormula")
	private String LienAddntlInfoTransformationFormula;
	
	@XmlElement(name="LienEndDtTransformationFormula")
	private String LienEndDtTransformationFormula;
	
	@XmlElement(name="ThirdPartyTransformationFormula")
	private String thirdPartyTransformationFormula;
	
	@XmlElement(name="NotificationOutputFormat")
	private String NotificationOutputFormat;

	public String getNotificationJobUserName() {
		return NotificationJobUserName;
	}

	public void setNotificationJobUserName(String notificationJobUserName) {
		NotificationJobUserName = notificationJobUserName;
	}

	public String getNotificationJobPassword() {
		return NotificationJobPassword;
	}

	public void setNotificationJobPassword(String notificationJobPassword) {
		NotificationJobPassword = notificationJobPassword;
	}

	public String getNotificationJobSubmitFlowUrl() {
		return NotificationJobSubmitFlowUrl;
	}

	public void setNotificationJobSubmitFlowUrl(String notificationJobSubmitFlowUrl) {
		NotificationJobSubmitFlowUrl = notificationJobSubmitFlowUrl;
	}

	public String getNotificationJobGetFlowStatusUrl() {
		return NotificationJobGetFlowStatusUrl;
	}

	public void setNotificationJobGetFlowStatusUrl(String notificationJobGetFlowStatusUrl) {
		NotificationJobGetFlowStatusUrl = notificationJobGetFlowStatusUrl;
	}

	public String getNotificationJobKeyStorePath() {
		return NotificationJobKeyStorePath;
	}

	public void setNotificationJobKeyStorePath(String notificationJobKeyStorePath) {
		NotificationJobKeyStorePath = notificationJobKeyStorePath;
	}

	public String getNotificationJobKeyStorePwd() {
		return NotificationJobKeyStorePwd;
	}

	public void setNotificationJobKeyStorePwd(String notificationJobKeyStorePwd) {
		NotificationJobKeyStorePwd = notificationJobKeyStorePwd;
	}

	public String getNotificationReportPath() {
		return NotificationReportPath;
	}

	public void setNotificationReportPath(String notificationReportPath) {
		NotificationReportPath = notificationReportPath;
	}

	public String getLegislativeDataGroupName() {
		return LegislativeDataGroupName;
	}

	public void setLegislativeDataGroupName(String legislativeDataGroupName) {
		LegislativeDataGroupName = legislativeDataGroupName;
	}

	public String getLienInfoTransformationFormula() {
		return LienInfoTransformationFormula;
	}

	public void setLienInfoTransformationFormula(String lienInfoTransformationFormula) {
		LienInfoTransformationFormula = lienInfoTransformationFormula;
	}

	public String getLienAddntlInfoTransformationFormula() {
		return LienAddntlInfoTransformationFormula;
	}

	public void setLienAddntlInfoTransformationFormula(String lienAddntlInfoTransformationFormula) {
		LienAddntlInfoTransformationFormula = lienAddntlInfoTransformationFormula;
	}

	public String getLienEndDtTransformationFormula() {
		return LienEndDtTransformationFormula;
	}

	public void setLienEndDtTransformationFormula(String lienEndDtTransformationFormula) {
		LienEndDtTransformationFormula = lienEndDtTransformationFormula;
	}

	public String getNotificationOutputFormat() {
		return NotificationOutputFormat;
	}

	public void setNotificationOutputFormat(String notificationOutputFormat) {
		NotificationOutputFormat = notificationOutputFormat;
	}

	public String getReportServiceUrl() {
		return ReportServiceUrl;
	}

	public void setReportServiceUrl(String reportServiceUrl) {
		ReportServiceUrl = reportServiceUrl;
	}

	public String getThirdPartyTransformationFormula() {
		return thirdPartyTransformationFormula;
	}

	public void setThirdPartyTransformationFormula(String thirdPartyTransformationFormula) {
		this.thirdPartyTransformationFormula = thirdPartyTransformationFormula;
	}

}
