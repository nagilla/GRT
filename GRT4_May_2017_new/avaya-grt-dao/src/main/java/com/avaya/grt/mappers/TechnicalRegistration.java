package com.avaya.grt.mappers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.grt.util.StatusEnum;

/**
 * The persistent class for the TECHNICAL_REGISTRATION database table.
 *
 * @author BEA Workshop
 */
public class TechnicalRegistration  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String technicalRegistrationId;
	private String accessType;
	private String uiAccessType;
	private String authorizationFileId;
	private String connectivity;
	private java.util.Date createdDate;
	private String dialInNumber;
	private String mid;

	private String errorCode;

	private String model;
	//Remove
	private String moduleCode;

	//System generated
	private String nickname;

	private String productCode;
	private String productId;
	private String remoteAccess;
	private String salGateway;
	private String serialNumber;
	private String sid;
	private String softwareRelease;
	private String systemProductRelease;
	//remove softwareVersion;
	private String softwareVersion;
	private String soldToId;
	//SECODE
	private String solutionElement;
	//SEID
	private String solutionElementId;
	private String transportAlarm;
	private String updatedBy;
	private String artSrNo;
	//Remove
	private String numberOfSubmit;
	private String outboundCallingPrefix;
	private String ipAddress;
	private String randomPassword;
	//Make the length of privateIpAddress samne as ipAddress column.
	private String privateIpAddress;
	//Make the length of privateIpAddress samne as seid column.
	private String seidOfVoice;
	//Is SAMP board upgraded to 2.2.3?
	private String checkReleaseHigher;
	//Check if SES Edge will Use Core Router Feature: DOCR
	private String checkSesEdge;
	private java.util.Date updatedDate;
	private TechnicalOrder technicalOrder;
	private Status status;
	private SRRequest srRequest;
	private String createdBy;
	private String onboarding;
	private String alarmId;
	private String ipoUserEmail;
	private String errorDesc;
	//Remove artCreatedSrNo.
	private String artCreatedSrNo;

	private String groupId;

	//CM Main related fields:
	private boolean cmProduct, cmMain, sendEPNSurveyNotification;
	private String cmMainSeid, cmMainSoldToId, cmMainSid;
	private String cmPollingInitiationStatus, cmPollingInitiationDescription;

	//SAL related fields:
	private String primarySalGWSeid, secondarySalGWSeid, hardwareServer;
	private String salGateWaySeid;
	//TR or UPDATE
	private String action;

	private String connectionDetail;
	private boolean overRideButtonStatus = false;
	private boolean updateButtonStatus = false;
	private boolean submitButtonStatus = false;
	private boolean detailButtonStatus = false;
	private String operationType;
	private String artId;
	private String installScript;
	private String transactionDetails;
	//Map them
	private String equipmentNumber, summaryEquipmentNumber;

	private String failedSeid;

	private int submissionCount;
	//Map it
	private String subErrorCode;

	private Status stepBStatus;

	private boolean selected = true;

	private SRRequest stepBSRRequest;
	private String strSiebelSRNo;
	private boolean selectForAlarmAndConnectivity;

	private String troubleShootURL;
	private boolean checkBoxDisabled;
	private String spRelease, template;

	private String deviceStatus, deviceLastAlarmReceivedDate;
	private String strStepBSRNo;
	private java.util.Set<ExpandedSolutionElement> explodedSolutionElements = new HashSet<ExpandedSolutionElement>();
	private String remoteDeviceType;
	private Status epnSurveyStatus;
	private String serviceName;
	
	private boolean selectForRemoteAccess;
	private boolean remoteAccessCheckBoxDisabled;
	private boolean selectForAlarming;
	private boolean alarmingCheckBoxDisabled;
	private List<ExpandedSolutionElement> expSolutionElements = new ArrayList<ExpandedSolutionElement>();
	
	private String primarySoldToId;
	private String secondarySoldToId;
	
	private int index = -1;
	private String cancelled;
	
	private Date stepASubmittedDate;
	private Date stepACompletedDate;
	private Date stepBSubmittedDate;
	private Date stepBCompletedDate;
	private Date stepAReSubmittedDate;
	
	private String username, password;
	
	private String alarmOrigination;
	
	private String deviceOnboardOp;
	private String ossNumber;
	
	//GRT 4.0 Changes
		private String popUpHiddenValue;	
		private boolean process; 
		private boolean equipmentMove;
		private String entitledForAlarming;
		
		//For Equipmove ASCBI
		private String toSoldToId;
		
	private String existence;
	
	private boolean auxMC, auxMCRegType;
	private String auxMCMainSeid;
	
    public boolean isAuxMC() {
		return auxMC;
	}

	public void setAuxMC(boolean auxMC) {
		this.auxMC = auxMC;
	}

	public boolean isAuxMCRegType() {
		return auxMCRegType;
	}

	public void setAuxMCRegType(boolean auxMCRegType) {
		this.auxMCRegType = auxMCRegType;
	}

	public String getAuxMCMainSeid() {
		return auxMCMainSeid;
	}

	public void setAuxMCMainSeid(String auxMCMainSeid) {
		this.auxMCMainSeid = auxMCMainSeid;
	}

	public String getExistence() {
		return existence;
	}

	public void setExistence(String existence) {
		this.existence = existence;
	}
		
    public String getAlarmOrigination() {
		return alarmOrigination;
	}

	public void setAlarmOrigination(String alarmOrigination) {
		this.alarmOrigination = alarmOrigination;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpRelease() {
		return spRelease;
	}

	public void setSpRelease(String spRelease) {
		this.spRelease = spRelease;
	}

	public Status getStepBStatus() {
		return stepBStatus;
	}

	public void setStepBStatus(Status stepBStatus) {
		this.stepBStatus = stepBStatus;
	}

	public String getFailedSeid() {
		return failedSeid;
	}

	public void setFailedSeid(String failedSeid) {
		this.failedSeid = failedSeid;
	}

	public int getSubmissionCount() {
		return submissionCount;
	}

	public void setSubmissionCount(int submissionCount) {
		this.submissionCount = submissionCount;
	}

	public String getEquipmentNumber() {
		return equipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public TechnicalRegistration() {
    }

	public String getTechnicalRegistrationId() {
		return this.technicalRegistrationId;
	}
	public void setTechnicalRegistrationId(String technicalRegistrationId) {
		this.technicalRegistrationId = technicalRegistrationId;
	}

	public String getAccessType() {
		return this.accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAuthorizationFileId() {
		return this.authorizationFileId;
	}
	public void setAuthorizationFileId(String authorizationFileId) {
		this.authorizationFileId = authorizationFileId;
	}

	public String getConnectivity() {
		return this.connectivity;
	}
	public void setConnectivity(String connectivity) {
		this.connectivity = connectivity;
	}

	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDialInNumber() {
		return this.dialInNumber;
	}
	public void setDialInNumber(String dialInNumber) {
		this.dialInNumber = dialInNumber;
	}

	public String getErrorCode() {
		return this.errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMid() {
		return this.mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getModel() {
		return this.model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getModuleCode() {
		return this.moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getNickname() {
		return this.nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProductCode() {
		return this.productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductId() {
		return this.productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRemoteAccess() {
		return this.remoteAccess;
	}
	public void setRemoteAccess(String remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	public String getSalGateway() {
		return this.salGateway;
	}
	public void setSalGateway(String salGateway) {
		this.salGateway = salGateway;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSid() {
		return this.sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSoftwareRelease() {
		return this.softwareRelease;
	}
	public void setSoftwareRelease(String softwareRelease) {
		this.softwareRelease = softwareRelease;
	}

	public String getSoftwareVersion() {
		return this.softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getSoldToId() {
		return this.soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public String getSolutionElement() {
		return this.solutionElement;
	}
	public void setSolutionElement(String solutionElement) {
		this.solutionElement = solutionElement;
	}

	public String getTransportAlarm() {
		return this.transportAlarm;
	}
	public void setTransportAlarm(String transportAlarm) {
		this.transportAlarm = transportAlarm;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public java.util.Date getUpdatedDate() {
		return this.updatedDate;
	}
	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	//bi-directional many-to-one association to TechnicalOrder
	public TechnicalOrder getTechnicalOrder() {
		return this.technicalOrder;
	}
	public void setTechnicalOrder(TechnicalOrder technicalOrder) {
		this.technicalOrder = technicalOrder;
	}

	//bi-directional many-to-one association to Status
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

    public SRRequest getSrRequest() {
		return srRequest;
	}

	public void setSrRequest(SRRequest srRequest) {
		this.srRequest = srRequest;
	}

	public String getArtSrNo() {
		return artSrNo;
	}

	public void setArtSrNo(String artSrNo) {
		this.artSrNo = artSrNo;
	}

	public String getNumberOfSubmit() {
		if(this.numberOfSubmit==null){
			numberOfSubmit = "0";
		}
		return numberOfSubmit;
	}

	public void setNumberOfSubmit(String numberOfSubmit) {
		this.numberOfSubmit = numberOfSubmit;
	}

	public String getSolutionElementId() {
		return solutionElementId;
	}

	public void setSolutionElementId(String solutionElementId) {
		this.solutionElementId = solutionElementId;
	}

	public String getCheckReleaseHigher() {
		return checkReleaseHigher;
	}

	public void setCheckReleaseHigher(String checkReleaseHigher) {
		this.checkReleaseHigher = checkReleaseHigher;
	}

	public String getCheckSesEdge() {
		return checkSesEdge;
	}

	public void setCheckSesEdge(String checkSesEdge) {
		this.checkSesEdge = checkSesEdge;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOutboundCallingPrefix() {
		return outboundCallingPrefix;
	}

	public void setOutboundCallingPrefix(String outboundCallingPrefix) {
		this.outboundCallingPrefix = outboundCallingPrefix;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getRandomPassword() {
		return randomPassword;
	}

	public void setRandomPassword(String randomPassword) {
		this.randomPassword = randomPassword;
	}

	public String getSeidOfVoice() {
		return seidOfVoice;
	}

	public void setSeidOfVoice(String seidOfVoice) {
		this.seidOfVoice = seidOfVoice;
	}

	public String getOnboarding() {
		return onboarding;
	}

	public void setOnboarding(String onboarding) {
		this.onboarding = onboarding;
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

	public String getIpoUserEmail() {
		return ipoUserEmail;
	}

	public void setIpoUserEmail(String ipoUserEmail) {
		this.ipoUserEmail = ipoUserEmail;
	}

	public boolean isIPOProduct(){
		if(this.getTechnicalOrder()!= null){
			String ipoEligible = this.getTechnicalOrder().getIsIPOEligible();
			return (StringUtils.isNotEmpty(ipoEligible) && "TRUE".equalsIgnoreCase(ipoEligible));
		}
		return false;
	}

	public String getSRNumber() {
		if(StringUtils.isNotEmpty(this.artSrNo)){
			return this.artSrNo;
		} else if(this.srRequest != null && StringUtils.isNotEmpty(this.srRequest.getSiebelSRNo())){
			return this.srRequest.getSiebelSRNo();
		}
		return null;
	}

	public String getArtCreatedSrNo() {
		return artCreatedSrNo;
	}

	public void setArtCreatedSrNo(String artCreatedSrNo) {
		this.artCreatedSrNo = artCreatedSrNo;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the cmMain
	 */
	public boolean isCmMain() {
		return cmMain;
	}

	/**
	 * @param cmMain the cmMain to set
	 */
	public void setCmMain(boolean cmMain) {
		this.cmMain = cmMain;
	}

	/**
	 * @return the cmMainSeid
	 */
	public String getCmMainSeid() {
		return cmMainSeid;
	}

	/**
	 * @param cmMainSeid the cmMainSeid to set
	 */
	public void setCmMainSeid(String cmMainSeid) {
		this.cmMainSeid = cmMainSeid;
	}

	/**
	 * @return the cmMainSid
	 */
	public String getCmMainSid() {
		return cmMainSid;
	}

	/**
	 * @param cmMainSid the cmMainSid to set
	 */
	public void setCmMainSid(String cmMainSid) {
		this.cmMainSid = cmMainSid;
	}

	/**
	 * @return the cmMainSoldToId
	 */
	public String getCmMainSoldToId() {
		return cmMainSoldToId;
	}

	/**
	 * @param cmMainSoldToId the cmMainSoldToId to set
	 */
	public void setCmMainSoldToId(String cmMainSoldToId) {
		this.cmMainSoldToId = cmMainSoldToId;
	}

	/**
	 * @return the cmProduct
	 */
	public boolean isCmProduct() {
		return cmProduct;
	}

	/**
	 * @param cmProduct the cmProduct to set
	 */
	public void setCmProduct(boolean cmProduct) {
		this.cmProduct = cmProduct;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the hardwareServer
	 */
	public String getHardwareServer() {
		return hardwareServer;
	}

	/**
	 * @param hardwareServer the hardwareServer to set
	 */
	public void setHardwareServer(String hardwareServer) {
		this.hardwareServer = hardwareServer;
	}

	/**
	 * @return the primarySalGWSeid
	 */
	public String getPrimarySalGWSeid() {
		return primarySalGWSeid;
	}

	/**
	 * @param primarySalGWSeid the primarySalGWSeid to set
	 */
	public void setPrimarySalGWSeid(String primarySalGWSeid) {
		this.primarySalGWSeid = primarySalGWSeid;
	}

	/**
	 * @return the secondarySalGWSeid
	 */
	public String getSecondarySalGWSeid() {
		return secondarySalGWSeid;
	}

	/**
	 * @param secondarySalGWSeid the secondarySalGWSeid to set
	 */
	public void setSecondarySalGWSeid(String secondarySalGWSeid) {
		this.secondarySalGWSeid = secondarySalGWSeid;
	}

	/**
	 * @return the sendEPNSurveyNotification
	 */
	public boolean isSendEPNSurveyNotification() {
		return sendEPNSurveyNotification;
	}

	/**
	 * @param sendEPNSurveyNotification the sendEPNSurveyNotification to set
	 */
	public void setSendEPNSurveyNotification(boolean sendEPNSurveyNotification) {
		this.sendEPNSurveyNotification = sendEPNSurveyNotification;
	}

	public String getConnectionDetail() {
		return connectionDetail;
	}

	public void setConnectionDetail(String connectionDetail) {
		this.connectionDetail = connectionDetail;
	}

	public String getSummaryEquipmentNumber() {
		return summaryEquipmentNumber;
	}

	public void setSummaryEquipmentNumber(String summaryEquipmentNumber) {
		this.summaryEquipmentNumber = summaryEquipmentNumber;
	}

	public String getSubErrorCode() {
		return subErrorCode;
	}

	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}

	public boolean isOverRideButtonStatus() {
		return overRideButtonStatus;
	}

	public void setOverRideButtonStatus(boolean overRideButtonStatus) {
		this.overRideButtonStatus = overRideButtonStatus;
	}

	public boolean isSubmitButtonStatus() {
		return submitButtonStatus;
	}

	public void setSubmitButtonStatus(boolean submitButtonStatus) {
		this.submitButtonStatus = submitButtonStatus;
	}

	public boolean isUpdateButtonStatus() {
		return updateButtonStatus;
	}

	public void setUpdateButtonStatus(boolean updateButtonStatus) {
		this.updateButtonStatus = updateButtonStatus;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getArtId() {
		return artId;
	}

	public void setArtId(String artId) {
		this.artId = artId;
	}

	public String getInstallScript() {
		return installScript;
	}

	public void setInstallScript(String installScript) {
		this.installScript = installScript;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public String getCmPollingInitiationDescription() {
		return cmPollingInitiationDescription;
	}

	public void setCmPollingInitiationDescription(
			String cmPollingInitiationDescription) {
		this.cmPollingInitiationDescription = cmPollingInitiationDescription;
	}

	public String getCmPollingInitiationStatus() {
		return cmPollingInitiationStatus;
	}

	public void setCmPollingInitiationStatus(String cmPollingInitiationStatus) {
		this.cmPollingInitiationStatus = cmPollingInitiationStatus;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public SRRequest getStepBSRRequest() {
		return stepBSRRequest;
	}

	public void setStepBSRRequest(SRRequest stepBSRRequest) {
		this.stepBSRRequest = stepBSRRequest;
	}

	public boolean isSelectForAlarmAndConnectivity() {
		return selectForAlarmAndConnectivity;
	}

	public void setSelectForAlarmAndConnectivity(
			boolean selectForAlarmAndConnectivity) {
		this.selectForAlarmAndConnectivity = selectForAlarmAndConnectivity;
	}

	public String getStrSiebelSRNo() {
		return strSiebelSRNo;
	}

	public void setStrSiebelSRNo(String strSiebelSRNo) {
		this.strSiebelSRNo = strSiebelSRNo;
	}

	public String getTroubleShootURL() {
		return troubleShootURL;
	}

	public void setTroubleShootURL(String troubleShootURL) {
		this.troubleShootURL = troubleShootURL;
	}

	public boolean isCheckBoxDisabled() {
		return checkBoxDisabled;
	}

	public void setCheckBoxDisabled(boolean checkBoxDisabled) {
		this.checkBoxDisabled = checkBoxDisabled;
	}

	public String getSystemProductRelease() {
		return systemProductRelease;
	}

	public void setSystemProductRelease(String systemProductRelease) {
		this.systemProductRelease = systemProductRelease;
	}
	
	public String getSalGateWaySeids() {
        return this.getPrimarySalGWSeid() + ((StringUtils.isNotEmpty(this.getSecondarySalGWSeid()))?("+" + this.getSecondarySalGWSeid()):"");
	}

	/**
	 * @return the salGateWaySeid
	 */
	public String getSalGateWaySeid() {
		return salGateWaySeid;
	}

	/**
	 * @param salGateWaySeid the salGateWaySeid to set
	 */
	public void setSalGateWaySeid(String salGateWaySeid) {
		this.salGateWaySeid = salGateWaySeid;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getDeviceLastAlarmReceivedDate() {
		return deviceLastAlarmReceivedDate;
	}

	public void setDeviceLastAlarmReceivedDate(String deviceLastAlarmReceivedDate) {
		this.deviceLastAlarmReceivedDate = deviceLastAlarmReceivedDate;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	/**
	 * @return the strStepBSRNo
	 */
	public String getStrStepBSRNo() {
		return strStepBSRNo;
	}

	/**
	 * @param strStepBSRNo the strStepBSRNo to set
	 */
	public void setStrStepBSRNo(String strStepBSRNo) {
		this.strStepBSRNo = strStepBSRNo;
	}

	public java.util.Set<ExpandedSolutionElement> getExplodedSolutionElements() {
		return explodedSolutionElements;
	}

	public void setExplodedSolutionElements(
			java.util.Set<ExpandedSolutionElement> explodedSolutionElements) {
		this.explodedSolutionElements = explodedSolutionElements;
	}

	public String getRemoteDeviceType() {
		return remoteDeviceType;
	}

	public void setRemoteDeviceType(String remoteDeviceType) {
		this.remoteDeviceType = remoteDeviceType;
	}

	/**
	 * @return the epnSurveyStatus
	 */
	public Status getEpnSurveyStatus() {
		return epnSurveyStatus;
	}

	/**
	 * @param epnSurveyStatus the epnSurveyStatus to set
	 */
	public void setEpnSurveyStatus(Status epnSurveyStatus) {
		this.epnSurveyStatus = epnSurveyStatus;
	}

	public String getCompletionDetails() {
			if(this.getStatus() != null) {
				if(StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(this.getStatus().getStatusId())) {
					if(StringUtils.isNotEmpty(this.getInstallScript())) {
						return this.getInstallScript();
					}
				}
			}
			return "";
	}

	/**
	 * @return the uiAccessType
	 */
	public String getUiAccessType() {
		return uiAccessType;
	}

	/**
	 * @param uiAccessType the uiAccessType to set
	 */
	public void setUiAccessType(String uiAccessType) {
		this.uiAccessType = uiAccessType;
	}
	
	public int getNumberOfSubmitAsInt() {
		int numberOfSubmit = 0;
		try {
			if(StringUtils.isNotEmpty(this.getNumberOfSubmit())) {
				numberOfSubmit = Integer.valueOf(this.getNumberOfSubmit());
			}
		}catch (Throwable throwable){
			//Eat it, We are defaulting it to 0.
		}
		return numberOfSubmit;
	}
	
	public String getFullGatewaySeid() {
		String gatewayseid = this.getPrimarySalGWSeid();
		if(StringUtils.isNotEmpty(this.getSecondarySalGWSeid())) {
			gatewayseid += "+" + this.getSecondarySalGWSeid();
		}
		return gatewayseid;
	}

	/**
	 * @return the alarmingCheckBoxDisabled
	 */
	public boolean isAlarmingCheckBoxDisabled() {
		return alarmingCheckBoxDisabled;
	}

	/**
	 * @param alarmingCheckBoxDisabled the alarmingCheckBoxDisabled to set
	 */
	public void setAlarmingCheckBoxDisabled(boolean alarmingCheckBoxDisabled) {
		this.alarmingCheckBoxDisabled = alarmingCheckBoxDisabled;
	}

	/**
	 * @return the remoteAccessCheckBoxDisabled
	 */
	public boolean isRemoteAccessCheckBoxDisabled() {
		return remoteAccessCheckBoxDisabled;
	}

	/**
	 * @param remoteAccessCheckBoxDisabled the remoteAccessCheckBoxDisabled to set
	 */
	public void setRemoteAccessCheckBoxDisabled(boolean remoteAccessCheckBoxDisabled) {
		this.remoteAccessCheckBoxDisabled = remoteAccessCheckBoxDisabled;
	}

	/**
	 * @return the selectForAlarming
	 */
	public boolean isSelectForAlarming() {
		return selectForAlarming;
	}

	/**
	 * @param selectForAlarming the selectForAlarming to set
	 */
	public void setSelectForAlarming(boolean selectForAlarming) {
		this.selectForAlarming = selectForAlarming;
	}

	/**
	 * @return the selectForRemoteAccess
	 */
	public boolean isSelectForRemoteAccess() {
		return selectForRemoteAccess;
	}

	/**
	 * @param selectForRemoteAccess the selectForRemoteAccess to set
	 */
	public void setSelectForRemoteAccess(boolean selectForRemoteAccess) {
		this.selectForRemoteAccess = selectForRemoteAccess;
	}

	/**
	 * @return the expSolutionElements
	 */
	public List<ExpandedSolutionElement> getExpSolutionElements() {
		return expSolutionElements;
	}

	/**
	 * @param expSolutionElements the expSolutionElements to set
	 */
	public void setExpSolutionElements(
			List<ExpandedSolutionElement> expSolutionElements) {
		this.expSolutionElements = expSolutionElements;
	}

	/**
	 * @return the primarySoldToId
	 */
	public String getPrimarySoldToId() {
		return primarySoldToId;
	}

	/**
	 * @param primarySoldToId the primarySoldToId to set
	 */
	public void setPrimarySoldToId(String primarySoldToId) {
		this.primarySoldToId = primarySoldToId;
	}

	/**
	 * @return the secondarySoldToId
	 */
	public String getSecondarySoldToId() {
		return secondarySoldToId;
	}

	/**
	 * @param secondarySoldToId the secondarySoldToId to set
	 */
	public void setSecondarySoldToId(String secondarySoldToId) {
		this.secondarySoldToId = secondarySoldToId;
	}

	/**
	 * @return the detailButtonStatus
	 */
	public boolean isDetailButtonStatus() {
		return detailButtonStatus;
	}

	/**
	 * @param detailButtonStatus the detailButtonStatus to set
	 */
	public void setDetailButtonStatus(boolean detailButtonStatus) {
		this.detailButtonStatus = detailButtonStatus;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the cancelled
	 */
	public String getCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @return the stepACompletedDate
	 */
	public Date getStepACompletedDate() {
		return stepACompletedDate;
	}

	/**
	 * @param stepACompletedDate the stepACompletedDate to set
	 */
	public void setStepACompletedDate(Date stepACompletedDate) {
		this.stepACompletedDate = stepACompletedDate;
	}

	/**
	 * @return the stepAReSubmittedDate
	 */
	public Date getStepAReSubmittedDate() {
		return stepAReSubmittedDate;
	}

	/**
	 * @param stepAReSubmittedDate the stepAReSubmittedDate to set
	 */
	public void setStepAReSubmittedDate(Date stepAReSubmittedDate) {
		this.stepAReSubmittedDate = stepAReSubmittedDate;
	}

	/**
	 * @return the stepASubmittedDate
	 */
	public Date getStepASubmittedDate() {
		return stepASubmittedDate;
	}

	/**
	 * @param stepASubmittedDate the stepASubmittedDate to set
	 */
	public void setStepASubmittedDate(Date stepASubmittedDate) {
		this.stepASubmittedDate = stepASubmittedDate;
	}

	/**
	 * @return the stepBCompletedDate
	 */
	public Date getStepBCompletedDate() {
		return stepBCompletedDate;
	}

	/**
	 * @param stepBCompletedDate the stepBCompletedDate to set
	 */
	public void setStepBCompletedDate(Date stepBCompletedDate) {
		this.stepBCompletedDate = stepBCompletedDate;
	}

	/**
	 * @return the stepBSubmittedDate
	 */
	public Date getStepBSubmittedDate() {
		return stepBSubmittedDate;
	}

	/**
	 * @param stepBSubmittedDate the stepBSubmittedDate to set
	 */
	public void setStepBSubmittedDate(Date stepBSubmittedDate) {
		this.stepBSubmittedDate = stepBSubmittedDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the deviceOnboardOp
	 */
	public String getDeviceOnboardOp() {
		return deviceOnboardOp;
	}

	/**
	 * @param deviceOnboardOp the deviceOnboardOp to set
	 */
	public void setDeviceOnboardOp(String deviceOnboardOp) {
		this.deviceOnboardOp = deviceOnboardOp;
	}

	/**
	 * @return the ossNumber
	 */
	public String getOssNumber() {
		return ossNumber;
	}

	/**
	 * @param ossNumber the ossNumber to set
	 */
	public void setOssNumber(String ossNumber) {
		this.ossNumber = ossNumber;
	}

	public String getPopUpHiddenValue() {
		return popUpHiddenValue;
	}

	public void setPopUpHiddenValue(String popUpHiddenValue) {
		this.popUpHiddenValue = popUpHiddenValue;
	}

	public boolean isProcess() {
		return process;
	}

	public void setProcess(boolean process) {
		this.process = process;
	}

	public boolean isEquipmentMove() {
		return equipmentMove;
	}

	public void setEquipmentMove(boolean equipmentMove) {
		this.equipmentMove = equipmentMove;
	}

	public String getEntitledForAlarming() {
		return entitledForAlarming;
	}

	public void setEntitledForAlarming(String entitledForAlarming) {
		this.entitledForAlarming = entitledForAlarming;
	}

	public String getToSoldToId() {
		return toSoldToId;
	}

	public void setToSoldToId(String toSoldToId) {
		this.toSoldToId = toSoldToId;
	}
}