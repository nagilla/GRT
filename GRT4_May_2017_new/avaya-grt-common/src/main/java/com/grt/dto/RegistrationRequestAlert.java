package com.grt.dto;

import org.apache.commons.lang.StringUtils
;

import com.grt.util.RegistrationTypeEnum;

public class RegistrationRequestAlert implements java.io.Serializable{
	/**
	 *  RegistrationRequestAlert dto
	 */
	private static final long serialVersionUID = 1L;
	private String registrationId;
	private String fullName;
	private String requestorEmail;
	private java.util.Date reportedDate;
	private java.util.Date completedDate;
	private String status;
	private String statusId;
	private String processStepId;
	private String processStep; 
	private String customerName;
	private String siebelSRNumber;	
	private String siebelSRStatus;
	private String actionRequired;
	private String registrationIdentifier;
	private String notes;
	private String errorDescription;
	private String salRegistrationType;
	private String salStatus;
	private String alarmStatus;
	private String seCode;
	
	private String srRequestStatusId;
	private Boolean isSystemMail = false;
	private Boolean sendMail = false;
	private String destination; //ART or Siebel	
	private String technicalRegistrationId;
	private String materialCode;
	private String materialCodeDescription;
	private String alarmId;
	private String downloadLink;
	private String ipoFilePath;
	private String attatchmentContents;
	private String soldTo;
	private String customerAddress;
	private boolean onBoarding;
	private boolean epnSurveyCompletionNotification;
	private String cmMainPPNSoldtoId;
	private String cmPollableSeCodeSeid;
	private String dynamicData;
	private String registrationTypeId;
	private boolean secondAttempt;
	private boolean stepB;
	private boolean isTOBSRCreated;
	private boolean SAL;
	private boolean tobUpdate;
	private boolean headerLevel;
	private boolean stepBCancellation;
	
	//EQM
	private String toCustomerSoldTo;
	private String toCustomerName;
	
	//GRT 4.0 : Added for sending sal alarm & connectivity mail
	private boolean salAlarmConMail;
	
	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public String getSeCode() {
		return seCode;
	}

	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public String getSiebelSRNumber() {
		return siebelSRNumber;
	}

	public void setSiebelSRNumber(String siebelSRNumber) {
		this.siebelSRNumber = siebelSRNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProcessStep() {
		return processStep;
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessStepId() {
		return processStepId;
	}

	public void setProcessStepId(String processStepId) {
		this.processStepId = processStepId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getRequestorEmail() {
		return requestorEmail;
	}

	public void setRequestorEmail(String requestorEmail) {
		this.requestorEmail = requestorEmail;
	}

	public java.util.Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(java.util.Date completedDate) {
		this.completedDate = completedDate;
	}

	public java.util.Date getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(java.util.Date reportedDate) {
		this.reportedDate = reportedDate;
	}

	public String getActionRequired() {
		return actionRequired;
	}

	public void setActionRequired(String actionRequired) {
		this.actionRequired = actionRequired;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getSalRegistrationType() {
		return salRegistrationType;
	}

	public void setSalRegistrationType(String salRegistrationType) {
		this.salRegistrationType = salRegistrationType;
	}

	public String getSalStatus() {
		return salStatus;
	}

	public void setSalStatus(String salStatus) {
		this.salStatus = salStatus;
	}

	public String getSiebelSRStatus() {
		return siebelSRStatus;
	}

	public void setSiebelSRStatus(String siebelSRStatus) {
		this.siebelSRStatus = siebelSRStatus;
	}

	public String getSrRequestStatusId() {
		return srRequestStatusId;
	}

	public void setSrRequestStatusId(String srRequestStatusId) {
		this.srRequestStatusId = srRequestStatusId;
	}

	public Boolean getIsSystemMail() {
		return isSystemMail;
	}

	public void setIsSystemMail(Boolean isSystemMail) {
		this.isSystemMail = isSystemMail;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTechnicalRegistrationId() {
		return technicalRegistrationId;
	}

	public void setTechnicalRegistrationId(String technicalRegistrationId) {
		this.technicalRegistrationId = technicalRegistrationId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public String getIpoFilePath() {
		return ipoFilePath;
	}

	public void setIpoFilePath(String ipoFilePath) {
		this.ipoFilePath = ipoFilePath;
	}

	/**
	 * @return the materialCodeDescription
	 */
	public String getMaterialCodeDescription() {
		return materialCodeDescription;
	}

	/**
	 * @param materialCodeDescription the materialCodeDescription to set
	 */
	public void setMaterialCodeDescription(String materialCodeDescription) {
		this.materialCodeDescription = materialCodeDescription;
	}

	/**
	 * @return the alarmId
	 */
	public String getAlarmId() {
		return alarmId;
	}

	/**
	 * @param alarmId the alarmId to set
	 */
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getAttatchmentContents() {
		return attatchmentContents;
	}

	public void setAttatchmentContents(String attatchmentContents) {
		this.attatchmentContents = attatchmentContents;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	public boolean isOnBoarding() {
		return onBoarding;
	}

	public void setOnBoarding(boolean onBoarding) {
		this.onBoarding = onBoarding;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRegistrationIdentifier() {
		return registrationIdentifier;
	}

	public void setRegistrationIdentifier(String registrationIdentifier) {
		this.registrationIdentifier = registrationIdentifier;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public boolean isEpnSurveyCompletionNotification() {
		return epnSurveyCompletionNotification;
	}

	public void setEpnSurveyCompletionNotification(
			boolean epnSurveyCompletionNotification) {
		this.epnSurveyCompletionNotification = epnSurveyCompletionNotification;
	}

	public String getCmMainPPNSoldtoId() {
		return cmMainPPNSoldtoId;
	}

	public void setCmMainPPNSoldtoId(String cmMainPPNSoldtoId) {
		this.cmMainPPNSoldtoId = cmMainPPNSoldtoId;
	}

	public String getDynamicData() {
		if(StringUtils.isNotEmpty(this.dynamicData)) {
			return dynamicData;
		} else {
			return " ";
		}
	}

	public void setDynamicData(String dynamicData) {
		this.dynamicData = dynamicData;
	}

	public String getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(String registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public boolean isSecondAttempt() {
		return secondAttempt;
	}

	public void setSecondAttempt(boolean secondAttempt) {
		this.secondAttempt = secondAttempt;
	}

	public boolean isStepB() {
		return stepB;
	}

	public void setStepB(boolean stepB) {
		this.stepB = stepB;
	}

	public boolean isTOBSRCreated() {
		return isTOBSRCreated;
	}

	public void setTOBSRCreated(boolean isTOBSRCreated) {
		this.isTOBSRCreated = isTOBSRCreated;
	}

	public boolean isSAL() {
		return SAL;
	}

	public void setSAL(boolean sal) {
		SAL = sal;
	}

	public boolean isTobUpdate() {
		return tobUpdate;
	}

	public void setTobUpdate(boolean tobUpdate) {
		this.tobUpdate = tobUpdate;
	}

	public boolean isHeaderLevel() {
		return headerLevel;
	}

	public void setHeaderLevel(boolean headerLevel) {
		this.headerLevel = headerLevel;
	}

	public String getCmPollableSeCodeSeid() {
		return cmPollableSeCodeSeid;
	}

	public void setCmPollableSeCodeSeid(String cmPollableSeCodeSeid) {
		this.cmPollableSeCodeSeid = cmPollableSeCodeSeid;
	}

	public boolean isStepBCancellation() {
		return stepBCancellation;
	}

	public void setStepBCancellation(boolean stepBCancellation) {
		this.stepBCancellation = stepBCancellation;
	}

	public String getToCustomerSoldTo() {
		return toCustomerSoldTo;
	}

	public void setToCustomerSoldTo(String toCustomerSoldTo) {
		this.toCustomerSoldTo = toCustomerSoldTo;
	}

	public String getToCustomerName() {
		return toCustomerName;
	}

	public void setToCustomerName(String toCustomerName) {
		this.toCustomerName = toCustomerName;
	}

	public boolean isSalAlarmConMail() {
		return salAlarmConMail;
	}

	public void setSalAlarmConMail(boolean salAlarmConMail) {
		this.salAlarmConMail = salAlarmConMail;
	}
}
