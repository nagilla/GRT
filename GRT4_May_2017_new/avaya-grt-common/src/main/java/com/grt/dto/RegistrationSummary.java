package com.grt.dto;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.grt.util.GRTConstants;

public class RegistrationSummary implements java.io.Serializable {
	/**
	 *  RegistrationSummary dto
	 */
	private static final long serialVersionUID = -6755539153224134732L;
	private String registrationId;
	private String fullName;
	private String reportEmailId;// requester email
	private java.util.Date createdDate;
	private java.util.Date updatedDate;
	private String updatedBy;
	private String processStep; 
	private String status;
	private String statusId;
	private String processStepId;
	private String soldTo;
	private String customerName;
	private String requestingCompany;
	private String activeSR;
	private String formulaActiveSR;
	private String installBaseSrNo;
	private String finalValidationSrNo;
	private SRRequestDto installBaseSrRequest;
	private SRRequestDto finalValidationSrRequest;
	private String expedite;
	private String noAdditionalProductFlag; 
	private String onsiteEmail;
	private String onsiteFirstName;
	private String onsiteLastName;
	private String onsitePhone;
	private String siteCountry;
	private String userName;
	private Boolean ownedByCurrentUser;
	private String skipInstallBaseCreation;
	private String salMigrationOnly;
	private String implementationType;
	private String installBaseStatusId;
	private String techRegStatusId;
	private String finalValidationStatusId;
	//GRT 4.0 Change
	private String eqrMoveStatusId;
	
	private String installBaseStatus;
	private String techRegStatus;
	private String finalValidationStatus;
	//GRT 4.0 Change
	private String eqrMoveStatus;

	private String onBoarding;
	private Boolean isOnBoardingExist;
	//added to facilitate save and resubmit of ibase
	private String typeOfInstallation; 
	private String industry;
	private String employeesAllLocation;
	private String employeesThisLocation;
	private String totalLocations;
	private String remoteConnectivity;
	private String setsCovered;
	private String impl;
	private String submitted;
	private String srCompleted;
	private String cutOverDate;

	private String registrationTypeId;
	private String registrationTypeDesc;

	private String installBaseSubStatus;
	private String finalValidationSubStatus;
	//GRT 4.0 Change
	private String eqrMoveSubStatus;	
	private String company;

	private String registrationNotes;
	private String reportPhone;
	private String registrationIdentifier;
	private String createdBy;

	private java.util.Date ibSubmittedDate;
	private java.util.Date ibCompletedDate;
	private java.util.Date eqrSubmittedDate;
	private java.util.Date eqrCompletedDate;
	private java.util.Date tobCompletedDate;
	//GRT 4.0 Change
	private java.util.Date eqrMoveSubmittedDate;
	private java.util.Date eqrMoveCompletedDate;
	
	// GRT 4.0 New fields used to display Links on registrationList table
	private String requestEmailUIElement;
	private String installBaseStatusUIElement;
	private String techRegStatusUIElement;
	private String finalRVStatusUIElement;
	private String activeSRUIElement;
	private String buttonStatus;
	private String superButtonUIElement;
	private String allButtonUIElement;
	private String eqrMoveUIElement;
	private String grtNotificationName;
	private String toSoldToId;
	private String eqrMoveSrNo;
	

	public String getSrCompleted() {
		return srCompleted;
	}
	public void setSrCompleted(String srCompleted) {
		this.srCompleted = srCompleted;
	}
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}

	public String getImpl() {
		return impl;
	}

	public void setImpl(String impl) {
		this.impl = impl;
	}

	public String getEmployeesAllLocation() {
		return employeesAllLocation;
	}

	public void setEmployeesAllLocation(String employeesAllLocation) {
		this.employeesAllLocation = employeesAllLocation;
	}

	public String getEmployeesThisLocation() {
		return employeesThisLocation;
	}

	public void setEmployeesThisLocation(String employeesThisLocation) {
		this.employeesThisLocation = employeesThisLocation;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRemoteConnectivity() {
		return remoteConnectivity;
	}

	public void setRemoteConnectivity(String remoteConnectivity) {
		this.remoteConnectivity = remoteConnectivity;
	}

	public String getSetsCovered() {
		return setsCovered;
	}

	public void setSetsCovered(String setsCovered) {
		this.setsCovered = setsCovered;
	}

	public String getTotalLocations() {
		return totalLocations;
	}

	public void setTotalLocations(String totalLocations) {
		this.totalLocations = totalLocations;
	}

	public String getTypeOfInstallation() {
		return typeOfInstallation;
	}

	public void setTypeOfInstallation(String typeOfInstallation) {
		this.typeOfInstallation = typeOfInstallation;
	}

	public Boolean getIsOnBoardingExist() {
		return isOnBoardingExist;
	}

	public void setIsOnBoardingExist(Boolean isOnBoardingExist) {
		this.isOnBoardingExist = isOnBoardingExist;
	}

	public String getOnBoarding() {
		return onBoarding;
	}

	public void setOnBoarding(String onBoarding) {
		this.onBoarding = onBoarding;
	}

	public String getSkipInstallBaseCreation() {
		return skipInstallBaseCreation;
	}

	public void setSkipInstallBaseCreation(String skipInstallBaseCreation) {
		this.skipInstallBaseCreation = skipInstallBaseCreation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNoAdditionalProductFlag() {
		return noAdditionalProductFlag;
	}

	public void setNoAdditionalProductFlag(String noAdditionalProductFlag) {
		this.noAdditionalProductFlag = noAdditionalProductFlag;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getActiveSR() {
		return activeSR;
	}

	public void setActiveSR(String activeSR) {
		this.activeSR = activeSR;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
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

	public String getReportEmailId() {
		return reportEmailId;
	}

	public void setReportEmailId(String reportEmailId) {
		this.reportEmailId = reportEmailId;
	}

	public String getProcessStep() {
		return processStep;
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}

	public String getRegistrationId() {
		//return registrationId;
		return StringEscapeUtils.unescapeHtml(registrationId);
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getRequestingCompany() {
		return requestingCompany;
	}

	public void setRequestingCompany(String requestingCompany) {
		this.requestingCompany = requestingCompany;
	}

	public String getSoldTo() {
		//return soldTo;
		return StringEscapeUtils.unescapeHtml(soldTo);
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getExpedite() {
		return expedite;
	}

	public void setExpedite(String expedite) {
		this.expedite = expedite;
	}

	public String getInstallBaseSrNo() {
		return installBaseSrNo;
	}

	public void setInstallBaseSrNo(String installBaseSrNo) {
		this.installBaseSrNo = installBaseSrNo;
	}

	public String getOnsiteEmail() {
		return onsiteEmail;
	}

	public void setOnsiteEmail(String onsiteEmail) {
		this.onsiteEmail = onsiteEmail;
	}

	public String getOnsiteFirstName() {
		return onsiteFirstName;
	}

	public void setOnsiteFirstName(String onsiteFirstName) {
		this.onsiteFirstName = onsiteFirstName;
	}

	public String getOnsiteLastName() {
		return onsiteLastName;
	}

	public void setOnsiteLastName(String onsiteLastName) {
		this.onsiteLastName = onsiteLastName;
	}

	public String getOnsitePhone() {
		return onsitePhone;
	}

	public void setOnsitePhone(String onsitePhone) {
		this.onsitePhone = onsitePhone;
	}

	public SRRequestDto getInstallBaseSrRequest() {
		return installBaseSrRequest;
	}

	public void setInstallBaseSrRequest(SRRequestDto installBaseSrRequest) {
		this.installBaseSrRequest = installBaseSrRequest;
	}

	public String getSiteCountry() {
		return siteCountry;
	}

	public void setSiteCountry(String siteCountry) {
		this.siteCountry = siteCountry;
	}

	public Boolean getOwnedByCurrentUser() {
		return ownedByCurrentUser;
	}

	public void setOwnedByCurrentUser(Boolean ownedByCurrentUser) {
		this.ownedByCurrentUser = ownedByCurrentUser;
	}

	public SRRequestDto getFinalValidationSrRequest() {
		return finalValidationSrRequest;
	}

	public void setFinalValidationSrRequest(SRRequestDto finalValidationSrRequest) {
		this.finalValidationSrRequest = finalValidationSrRequest;
	}

	public String getSalMigrationOnly() {
		return salMigrationOnly;
	}

	public void setSalMigrationOnly(String salMigrationOnly) {
		this.salMigrationOnly = salMigrationOnly;
	}

	public String getFinalValidationStatus() {
		return finalValidationStatus;
	}

	public void setFinalValidationStatus(String finalValidationStatus) {
		this.finalValidationStatus = finalValidationStatus;
	}

	public String getFinalValidationStatusId() {
		return finalValidationStatusId;
	}

	public void setFinalValidationStatusId(String finalValidationStatusId) {
		this.finalValidationStatusId = finalValidationStatusId;
	}

	public String getInstallBaseStatus() {
		return installBaseStatus;
	}

	public void setInstallBaseStatus(String installBaseStatus) {
		this.installBaseStatus = installBaseStatus;
	}

	public String getInstallBaseStatusId() {
		return installBaseStatusId;
	}

	public void setInstallBaseStatusId(String installBaseStatusId) {
		this.installBaseStatusId = installBaseStatusId;
	}

	public String getTechRegStatus() {
		return techRegStatus;
	}

	public void setTechRegStatus(String techRegStatus) {
		this.techRegStatus = techRegStatus;
	}

	public String getTechRegStatusId() {
		return techRegStatusId;
	}

	public void setTechRegStatusId(String techRegStatusId) {
		this.techRegStatusId = techRegStatusId;
	}

	public String getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(String implementationType) {
		this.implementationType = implementationType;
	}
	public String getCutOverDate() {
		return cutOverDate;
	}
	public void setCutOverDate(String cutOverDate) {
		this.cutOverDate = cutOverDate;
	}

	public String getRegistrationTypeDesc() {
		return registrationTypeDesc;
	}
	public void setRegistrationTypeDesc(String registrationTypeDesc) {
		this.registrationTypeDesc = registrationTypeDesc;
	}
	public String getRegistrationTypeId() {
		return registrationTypeId;
	}
	public void setRegistrationTypeId(String registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}
	/**
	 * @return the installBaseSubStatus
	 */
	public String getInstallBaseSubStatus() {
		return installBaseSubStatus;
	}
	/**
	 * @param installBaseSubStatus the installBaseSubStatus to set
	 */
	public void setInstallBaseSubStatus(String installBaseSubStatus) {
		this.installBaseSubStatus = installBaseSubStatus;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	public String getFinalValidationSubStatus() {
		return finalValidationSubStatus;
	}
	public void setFinalValidationSubStatus(String finalValidationSubStatus) {
		this.finalValidationSubStatus = finalValidationSubStatus;
	}
	public String getFinalValidationSrNo() {
		return finalValidationSrNo;
	}
	public void setFinalValidationSrNo(String finalValidationSrNo) {
		this.finalValidationSrNo = finalValidationSrNo;
	}
	/**
	 * @return the registrationNotes
	 */
	public String getRegistrationNotes() {
		return registrationNotes;
	}
	/**
	 * @param registrationNotes the registrationNotes to set
	 */
	public void setRegistrationNotes(String registrationNotes) {
		this.registrationNotes = registrationNotes;
	}
	/**
	 * @return the reportPhone
	 */
	public String getReportPhone() {
		return reportPhone;
	}
	/**
	 * @param reportPhone the reportPhone to set
	 */
	public void setReportPhone(String reportPhone) {
		this.reportPhone = reportPhone;
	}
	/**
	 * @return the registrationIdentifier
	 */
	public String getRegistrationIdentifier() {
		return registrationIdentifier;
	}
	/**
	 * @param registrationIdentifier the registrationIdentifier to set
	 */
	public void setRegistrationIdentifier(String registrationIdentifier) {
		this.registrationIdentifier = registrationIdentifier;
	}
	public String getFormulaActiveSR() {
		return formulaActiveSR;
	}
	public void setFormulaActiveSR(String formulaActiveSR) {
		this.formulaActiveSR = formulaActiveSR;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getComputedUpdateBy() {
		String updatedBy = this.getUpdatedBy();
		if(StringUtils.isNotEmpty(this.getCreatedBy())) {
			if(StringUtils.isNotEmpty(this.getUpdatedBy())){
				if(!this.getCreatedBy().equalsIgnoreCase(this.getUpdatedBy()) && !this.getUpdatedBy().equalsIgnoreCase(GRTConstants.USER_ID_ART) && !this.getUpdatedBy().equalsIgnoreCase(GRTConstants.USER_ID_SIEBEL) && !this.getUpdatedBy().equalsIgnoreCase(GRTConstants.USER_ID_RF) && !this.getUpdatedBy().equalsIgnoreCase(GRTConstants.DEFAULT_ART_USER)){
					updatedBy = GRTConstants.USER_ID_SYSTEM;
				}
			} else {
				updatedBy = this.getCreatedBy();
			}
		} else {
			if(StringUtils.isEmpty(this.getUpdatedBy())) {
				updatedBy = GRTConstants.USER_ID_SYSTEM; 
			}
		}
		return updatedBy;
	}

	public Date getComputedUpdateDate() {
		Date updatedDate = this.updatedDate;
		if(updatedDate == null) {
			updatedDate = this.createdDate;
		}
		return updatedDate;
	}
	public java.util.Date getEqrCompletedDate() {
		return eqrCompletedDate;
	}
	public void setEqrCompletedDate(java.util.Date eqrCompletedDate) {
		this.eqrCompletedDate = eqrCompletedDate;
	}
	public java.util.Date getEqrSubmittedDate() {
		return eqrSubmittedDate;
	}
	public void setEqrSubmittedDate(java.util.Date eqrSubmittedDate) {
		this.eqrSubmittedDate = eqrSubmittedDate;
	}
	public java.util.Date getIbCompletedDate() {
		return ibCompletedDate;
	}
	public void setIbCompletedDate(java.util.Date ibCompletedDate) {
		this.ibCompletedDate = ibCompletedDate;
	}
	public java.util.Date getIbSubmittedDate() {
		return ibSubmittedDate;
	}
	public void setIbSubmittedDate(java.util.Date ibSubmittedDate) {
		this.ibSubmittedDate = ibSubmittedDate;
	}
	public java.util.Date getTobCompletedDate() {
		return tobCompletedDate;
	}
	public void setTobCompletedDate(java.util.Date tobCompletedDate) {
		this.tobCompletedDate = tobCompletedDate;
	}
	public String getRequestEmailUIElement() {
		return requestEmailUIElement;
	}
	public void setRequestEmailUIElement(String requestEmailUIElement) {
		this.requestEmailUIElement = requestEmailUIElement;
	}
	public String getInstallBaseStatusUIElement() {
		return installBaseStatusUIElement;
	}
	public void setInstallBaseStatusUIElement(String installBaseStatusUIElement) {
		this.installBaseStatusUIElement = installBaseStatusUIElement;
	}
	public String getTechRegStatusUIElement() {
		return techRegStatusUIElement;
	}
	public void setTechRegStatusUIElement(String techRegStatusUIElement) {
		this.techRegStatusUIElement = techRegStatusUIElement;
	}
	public String getFinalRVStatusUIElement() {
		return finalRVStatusUIElement;
	}
	public void setFinalRVStatusUIElement(String finalRVStatusUIElement) {
		this.finalRVStatusUIElement = finalRVStatusUIElement;
	}
	public String getActiveSRUIElement() {
		return activeSRUIElement;
	}
	public void setActiveSRUIElement(String activeSRUIElement) {
		this.activeSRUIElement = activeSRUIElement;
	}
	public String getButtonStatus() {
		return buttonStatus;
	}
	public void setButtonStatus(String buttonStatus) {
		this.buttonStatus = buttonStatus;
	}
	public String getSuperButtonUIElement() {
		return superButtonUIElement;
	}
	public void setSuperButtonUIElement(String superButtonUIElement) {
		this.superButtonUIElement = superButtonUIElement;
	}
	public String getAllButtonUIElement() {
		return allButtonUIElement;
	}
	public void setAllButtonUIElement(String allButtonUIElement) {
		this.allButtonUIElement = allButtonUIElement;
	}
	public String getGrtNotificationName() {
		return grtNotificationName;
	}
	public void setGrtNotificationName(String grtNotificationName) {
		this.grtNotificationName = grtNotificationName;
	}
	public String getEqrMoveStatus() {
		return eqrMoveStatus;
	}
	public void setEqrMoveStatus(String eqrMoveStatus) {
		this.eqrMoveStatus = eqrMoveStatus;
	}
	public String getEqrMoveSubStatus() {
		return eqrMoveSubStatus;
	}
	public void setEqrMoveSubStatus(String eqrMoveSubStatus) {
		this.eqrMoveSubStatus = eqrMoveSubStatus;
	}
	public java.util.Date getEqrMoveSubmittedDate() {
		return eqrMoveSubmittedDate;
	}
	public void setEqrMoveSubmittedDate(java.util.Date eqrMoveSubmittedDate) {
		this.eqrMoveSubmittedDate = eqrMoveSubmittedDate;
	}
	public java.util.Date getEqrMoveCompletedDate() {
		return eqrMoveCompletedDate;
	}
	public void setEqrMoveCompletedDate(java.util.Date eqrMoveCompletedDate) {
		this.eqrMoveCompletedDate = eqrMoveCompletedDate;
	}
	public String getEqrMoveUIElement() {
		return eqrMoveUIElement;
	}
	public void setEqrMoveUIElement(String eqrMoveUIElement) {
		this.eqrMoveUIElement = eqrMoveUIElement;
	}
	public String getEqrMoveStatusId() {
		return eqrMoveStatusId;
	}
	public void setEqrMoveStatusId(String eqrMoveStatusId) {
		this.eqrMoveStatusId = eqrMoveStatusId;
	}
	public String getToSoldToId() {
		return toSoldToId;
	}
	public void setToSoldToId(String toSoldToId) {
		this.toSoldToId = toSoldToId;
	}
	public String getEqrMoveSrNo() {
		return eqrMoveSrNo;
	}
	public void setEqrMoveSrNo(String eqrMoveSrNo) {
		this.eqrMoveSrNo = eqrMoveSrNo;
	}
}
