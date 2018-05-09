package com.grt.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.Status;



/**
 * This DTO having the detail of Technical Registration Summary.
 * 
 * @author Perficient
 * 
 */
public class TechnicalRegistrationSummary implements java.io.Serializable{
	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;

	private String technicalRegistrationId;
	private String registrationId;
	private String orderId;
	private String materialCode;
	private String materialCodeDescription;
	private String solutionElement;
	private String createdDate;
	private Long initialQty;
	private Long remainingQty;	
	private String processStep;	
	private String statusId;
	private String software;
	private String sid;
	private String mid;
	private String accessType, accessTypes;
	private String dialInNumber;
	private String connectivity;
	private String serialNo;
	private String status;
	private String troubleShootId;
	private String version;
	private String requesterName;
	private boolean baseUnit;
	private boolean isIPOEligible;
	
	private String alarmOrigination;
	
	
	private String installBaseStatusId;
	private String techRegStatusId;
	private String finalValidationStatusId;
	private String productLine;
	private String orderType;
	private String seId;
	private String productTemplate, action, groupId, salSeIdPrimarySecondary;
	private String softwareRelease, ipAddress, authFileID, nickName;
	private boolean process; 
	private String systemProductRelease;
	private String operationType;
	private TRConfig trConfig;
	private String solutionElementId;
	private String connectionDetail;
	private boolean addFlag;
	private String mainCM, hardwareServer, seidOfVoicePortal, privateIP, checkIfSESEdge, checkIfSESEdgeDb;
	private String sampBoardUpgraded, sampBoardUpgradedDb, randomPassword, randomPassworddb, outboundPrefix, initiateEPNSurvery;
	private Status stepBStaus;
	private Set<String> eligibleAccessTypes;
	private String productTemplateDesc, productType, solutionElementDesc;
	private String cmMainsoldTo, cmMainMaterialCodeDesc, cmMainSID;
	private String remoteDeviceType;
	private boolean cmProduct;
	private boolean disableUpdateFlag;
	private String updateButtonTitle;
	
	private String primarySalgwSeid;
	private String secondarySalgwSeid;
	
	private String model;
	private String remoteAccess;
	private String transportAlarm;
	
	private String primarySoldToId;
	private String secondarySoldToId;
	
	private String failedSeid;
	private String numberOfSubmit;
	
	private String userName;
	private String password;
	
	private String seCodePreview ;
	/* GRT 4.0 changes */
	private String equipmentNumber;
	private boolean isSiteList;
	private boolean isTechReg;
	private List<ExpandedSolutionElement> expSolutionElements = new ArrayList<ExpandedSolutionElement>();
	private Set<ExpandedSolutionElement> explodedSolutionElements = new HashSet<ExpandedSolutionElement>();
	private String deviceStatus = "";
	private String deviceLastAlarmReceivedDate = "";
	
	//GRT 4.0 Changes
		private String popUpHiddenValue;
	//For Retest	
		private boolean selectForAlarming;
		private boolean selectForRemoteAccess;
		private boolean disableRetestFlag;
		
		//GRT 4.0 Change
		private String alarmId;
		
	private String auxMCShowFlag;
	private String auxMCMainSEID;
	private String isAuxMCSEIDFlag;
	private String auxMCSid;
	private String auxMCMid;	
	
	public String getAuxMCSid() {
		return auxMCSid;
	}

	public void setAuxMCSid(String auxMCSid) {
		this.auxMCSid = auxMCSid;
	}

	public String getAuxMCMid() {
		return auxMCMid;
	}

	public void setAuxMCMid(String auxMCMid) {
		this.auxMCMid = auxMCMid;
	}

	public String getIsAuxMCSEIDFlag() {
		return isAuxMCSEIDFlag;
	}

	public void setIsAuxMCSEIDFlag(String isAuxMCSEIDFlag) {
		this.isAuxMCSEIDFlag = isAuxMCSEIDFlag;
	}

	public String getAuxMCShowFlag() {
		return auxMCShowFlag;
	}

	public void setAuxMCShowFlag(String auxMCShowFlag) {
		this.auxMCShowFlag = auxMCShowFlag;
	}
		
	public String getAuxMCMainSEID() {
		return auxMCMainSEID;
	}

	public void setAuxMCMainSEID(String auxMCMainSEID) {
		this.auxMCMainSEID = auxMCMainSEID;
	}

	public Set<String> getEligibleAccessTypes() {
		return eligibleAccessTypes;
	}

	public void setEligibleAccessTypes(Set<String> eligibleAccessTypes) {
			this.eligibleAccessTypes = eligibleAccessTypes;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
	}

	public Long getInitialQty() {
		return initialQty;
	}

	public void setInitialQty(Long initialQty) {
		this.initialQty = initialQty;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Long getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(Long remainingQty) {
		this.remainingQty = remainingQty;
	}

	public String getSolutionElement() {
		return solutionElement;
	}

	public void setSolutionElement(String solutionElement) {
		this.solutionElement = solutionElement;
	}

	public String getProcessStep() {
		return processStep;
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(String connectivity) {
		this.connectivity = connectivity;
	}

	public String getDialInNumber() {
		return dialInNumber;
	}

	public void setDialInNumber(String dialInNumber) {
		this.dialInNumber = dialInNumber;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTroubleShootId() {
		return troubleShootId;
	}

	public void setTroubleShootId(String troubleShootId) {
		this.troubleShootId = troubleShootId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getMaterialCodeDescription() {
		return materialCodeDescription;
	}

	public void setMaterialCodeDescription(String materialCodeDescription) {
		this.materialCodeDescription = materialCodeDescription;
	}

	public boolean isBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(boolean baseUnit) {
		this.baseUnit = baseUnit;
	}

	/**
	 * @return the finalValidationStatusId
	 */
	public String getFinalValidationStatusId() {
		return finalValidationStatusId;
	}

	/**
	 * @param finalValidationStatusId the finalValidationStatusId to set
	 */
	public void setFinalValidationStatusId(String finalValidationStatusId) {
		this.finalValidationStatusId = finalValidationStatusId;
	}

	/**
	 * @return the techRegStatusId
	 */
	public String getTechRegStatusId() {
		return techRegStatusId;
	}

	/**
	 * @param techRegStatusId the techRegStatusId to set
	 */
	public void setTechRegStatusId(String techRegStatusId) {
		this.techRegStatusId = techRegStatusId;
	}

	/**
	 * @return the installBaseStatusId
	 */
	public String getInstallBaseStatusId() {
		return installBaseStatusId;
	}

	/**
	 * @param installBaseStatusId the installBaseStatusId to set
	 */
	public void setInstallBaseStatusId(String installBaseStatusId) {
		this.installBaseStatusId = installBaseStatusId;
	}

	public boolean isIPOEligible() {
		return isIPOEligible;
	}

	public void setIPOEligible(boolean isIPOEligible) {
		this.isIPOEligible = isIPOEligible;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProductTemplate() {
		return productTemplate;
	}

	public void setProductTemplate(String productTemplate) {
		this.productTemplate = productTemplate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getSalSeIdPrimarySecondary() {
		return salSeIdPrimarySecondary;
	}

	public void setSalSeIdPrimarySecondary(String salSeIdPrimarySecondary) {
		this.salSeIdPrimarySecondary = salSeIdPrimarySecondary;
	}
	
	public String getAuthFileID() {
		return authFileID;
	}

	public void setAuthFileID(String authFileID) {
		this.authFileID = authFileID;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSoftwareRelease() {
		return softwareRelease;
	}

	public void setSoftwareRelease(String softwareRelease) {
		this.softwareRelease = softwareRelease;
	}

	public boolean isProcess() {
		return process;
	}

	public void setProcess(boolean process) {
		this.process = process;
	}

	public String getSystemProductRelease() {
		return systemProductRelease;
	}

	public void setSystemProductRelease(String systemProductRelease) {
		this.systemProductRelease = systemProductRelease;
	}
	
	/**
	 * @return the solutionElementId
	 */
	public String getSolutionElementId() {
		return solutionElementId;
	}

	/**
	 * @param solutionElementId the solutionElementId to set
	 */
	public void setSolutionElementId(String solutionElementId) {
		this.solutionElementId = solutionElementId;
	}

	/**
	 * @return the technicalRegistrationId
	 */
	public String getTechnicalRegistrationId() {
		return technicalRegistrationId;
	}

	/**
	 * @param technicalRegistrationId the technicalRegistrationId to set
	 */
	public void setTechnicalRegistrationId(String technicalRegistrationId) {
		this.technicalRegistrationId = technicalRegistrationId;
	}
	
	/**
	 * @return the connectionDetail
	 */
	public String getConnectionDetail() {
		return connectionDetail;
	}

	/**
	 * @param connectionDetail the connectionDetail to set
	 */
	public void setConnectionDetail(String connectionDetail) {
		this.connectionDetail = connectionDetail;
	}

	public TRConfig getTrConfig() {
		return trConfig;
	}

	public void setTrConfig(TRConfig trConfig) {
		this.trConfig = trConfig;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public boolean isAddFlag() {
		return addFlag;
	}

	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
	}

	public String getHardwareServer() {
		return hardwareServer;
	}

	public void setHardwareServer(String hardwareServer) {
		this.hardwareServer = hardwareServer;
	}

	public String getSeidOfVoicePortal() {
		return seidOfVoicePortal;
	}

	public void setSeidOfVoicePortal(String seidOfVoicePortal) {
		this.seidOfVoicePortal = seidOfVoicePortal;
	}

	public String getPrivateIP() {
		return privateIP;
	}

	public void setPrivateIP(String privateIP) {
		this.privateIP = privateIP;
	}

	public String getCheckIfSESEdge() {
		return checkIfSESEdge;
	}

	public void setCheckIfSESEdge(String checkIfSESEdge) {
		this.checkIfSESEdge = checkIfSESEdge;
	}

	public String getMainCM() {
		return mainCM;
	}

	public void setMainCM(String mainCM) {
		this.mainCM = mainCM;
	}

	public String getRandomPassword() {
		return randomPassword;
	}

	public void setRandomPassword(String randomPassword) {
		this.randomPassword = randomPassword;
	}

	public String getSampBoardUpgraded() {
		return sampBoardUpgraded;
	}

	public void setSampBoardUpgraded(String sampBoardUpgraded) {
		this.sampBoardUpgraded = sampBoardUpgraded;
	}

	public String getOutboundPrefix() {
		return outboundPrefix;
	}

	public void setOutboundPrefix(String outboundPrefix) {
		this.outboundPrefix = outboundPrefix;
	}

	public String getInitiateEPNSurvery() {
		return initiateEPNSurvery;
	}

	public void setInitiateEPNSurvery(String initiateEPNSurvery) {
		this.initiateEPNSurvery = initiateEPNSurvery;
	}
	
	/**
	 * @return the stepBStaus
	 */
	public Status getStepBStaus() {
		return stepBStaus;
	}

	/**
	 * @param stepBStaus the stepBStaus to set
	 */
	public void setStepBStaus(Status stepBStaus) {
		this.stepBStaus = stepBStaus;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSolutionElementDesc() {
		return solutionElementDesc;
	}

	public void setSolutionElementDesc(String solutionElementDesc) {
		this.solutionElementDesc = solutionElementDesc;
	}

	public String getProductTemplateDesc() {
		return productTemplateDesc;
	}

	public void setProductTemplateDesc(String productTemplateDesc) {
		this.productTemplateDesc = productTemplateDesc;
	}
	
	public String getCmMainMaterialCodeDesc() {
		return cmMainMaterialCodeDesc;
	}

	public void setCmMainMaterialCodeDesc(String cmMainMaterialCodeDesc) {
		this.cmMainMaterialCodeDesc = cmMainMaterialCodeDesc;
	}

	public String getCmMainSID() {
		return cmMainSID;
	}

	public void setCmMainSID(String cmMainSID) {
		this.cmMainSID = cmMainSID;
	}

	public String getCmMainsoldTo() {
		return cmMainsoldTo;
	}

	public void setCmMainsoldTo(String cmMainsoldTo) {
		this.cmMainsoldTo = cmMainsoldTo;
	}

	public String getAccessTypes() {
		return accessTypes;
	}

	public void setAccessTypes(String accessTypes) {
		this.accessTypes = accessTypes;
	}

	public String getRemoteDeviceType() {
		return remoteDeviceType;
	}

	public void setRemoteDeviceType(String remoteDeviceType) {
		this.remoteDeviceType = remoteDeviceType;
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

	public String getRandomPassworddb() {
		return randomPassworddb;
	}

	public void setRandomPassworddb(String randomPassworddb) {
		this.randomPassworddb = randomPassworddb;
	}
	
	public boolean isDisableUpdateFlag() {
		return disableUpdateFlag;
	}

	public void setDisableUpdateFlag(boolean disableUpdateFlag) {
		this.disableUpdateFlag = disableUpdateFlag;
	}

	public String getUpdateButtonTitle() {
		return updateButtonTitle;
	}

	public void setUpdateButtonTitle(String updateButtonTitle) {
		this.updateButtonTitle = updateButtonTitle;
	}

	/**
	 * @return the primarySalgwSeid
	 */
	public String getPrimarySalgwSeid() {
		return primarySalgwSeid;
	}

	/**
	 * @param primarySalgwSeid the primarySalgwSeid to set
	 */
	public void setPrimarySalgwSeid(String primarySalgwSeid) {
		this.primarySalgwSeid = primarySalgwSeid;
	}

	/**
	 * @return the secondarySalgwSeid
	 */
	public String getSecondarySalgwSeid() {
		return secondarySalgwSeid;
	}

	/**
	 * @param secondarySalgwSeid the secondarySalgwSeid to set
	 */
	public void setSecondarySalgwSeid(String secondarySalgwSeid) {
		this.secondarySalgwSeid = secondarySalgwSeid;
	}
	
	public String getCheckIfSESEdgeDb() {
		return checkIfSESEdgeDb;
	}

	public void setCheckIfSESEdgeDb(String checkIfSESEdgeDb) {
		this.checkIfSESEdgeDb = checkIfSESEdgeDb;
	}

	public String getSampBoardUpgradedDb() {
		return sampBoardUpgradedDb;
	}

	public void setSampBoardUpgradedDb(String sampBoardUpgradedDb) {
		this.sampBoardUpgradedDb = sampBoardUpgradedDb;
	}
	
	

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the remoteAccess
	 */
	public String getRemoteAccess() {
		return remoteAccess;
	}

	/**
	 * @param remoteAccess the remoteAccess to set
	 */
	public void setRemoteAccess(String remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	/**
	 * @return the transportAlarm
	 */
	public String getTransportAlarm() {
		return transportAlarm;
	}

	/**
	 * @param transportAlarm the transportAlarm to set
	 */
	public void setTransportAlarm(String transportAlarm) {
		this.transportAlarm = transportAlarm;
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
	 * @return the failedSeid
	 */
	public String getFailedSeid() {
		return failedSeid;
	}

	/**
	 * @param failedSeid the failedSeid to set
	 */
	public void setFailedSeid(String failedSeid) {
		this.failedSeid = failedSeid;
	}

	/**
	 * @return the numberOfSubmit
	 */
	public String getNumberOfSubmit() {
		return numberOfSubmit;
	}

	/**
	 * @param numberOfSubmit the numberOfSubmit to set
	 */
	public void setNumberOfSubmit(String numberOfSubmit) {
		this.numberOfSubmit = numberOfSubmit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "TechnicalRegistrationSummary [technicalRegistrationId="
				+ technicalRegistrationId + ", registrationId="
				+ registrationId + ", orderId=" + orderId + ", materialCode="
				+ materialCode + ", materialCodeDescription="
				+ materialCodeDescription + ", solutionElement="
				+ solutionElement + ", createdDate=" + createdDate
				+ ", initialQty=" + initialQty + ", remainingQty="
				+ remainingQty + ", processStep=" + processStep + ", statusId="
				+ statusId + ", software=" + software + ", sid=" + sid
				+ ", mid=" + mid + ", accessType=" + accessType
				+ ", accessTypes=" + accessTypes + ", dialInNumber="
				+ dialInNumber + ", connectivity=" + connectivity
				+ ", serialNo=" + serialNo + ", status=" + status
				+ ", troubleShootId=" + troubleShootId + ", version=" + version
				+ ", requesterName=" + requesterName + ", baseUnit=" + baseUnit
				+ ", isIPOEligible=" + isIPOEligible + ", installBaseStatusId="
				+ installBaseStatusId + ", techRegStatusId=" + techRegStatusId
				+ ", finalValidationStatusId=" + finalValidationStatusId
				+ ", productLine=" + productLine + ", orderType=" + orderType
				+ ", seId=" + seId + ", productTemplate=" + productTemplate
				+ ", action=" + action + ", groupId=" + groupId
				+ ", salSeIdPrimarySecondary=" + salSeIdPrimarySecondary
				+ ", softwareRelease=" + softwareRelease + ", ipAddress="
				+ ipAddress + ", authFileID=" + authFileID + ", nickName="
				+ nickName + ", process=" + process + ", systemProductRelease="
				+ systemProductRelease + ", operationType=" + operationType
				+ ", solutionElementId=" + solutionElementId
				+ ", connectionDetail=" + connectionDetail + ", addFlag="
				+ addFlag + ", mainCM=" + mainCM + ", hardwareServer="
				+ hardwareServer + ", seidOfVoicePortal=" + seidOfVoicePortal
				+ ", privateIP=" + privateIP + ", checkIfSESEdge="
				+ checkIfSESEdge + ", checkIfSESEdgeDb=" + checkIfSESEdgeDb
				+ ", sampBoardUpgraded=" + sampBoardUpgraded
				+ ", sampBoardUpgradedDb=" + sampBoardUpgradedDb
				+ ", randomPassword=" + randomPassword + ", randomPassworddb="
				+ randomPassworddb + ", outboundPrefix=" + outboundPrefix
				+ ", initiateEPNSurvery=" + initiateEPNSurvery
				+ ", eligibleAccessTypes=" + eligibleAccessTypes
				+ ", productTemplateDesc=" + productTemplateDesc
				+ ", productType=" + productType + ", solutionElementDesc="
				+ solutionElementDesc + ", cmMainsoldTo=" + cmMainsoldTo
				+ ", cmMainMaterialCodeDesc=" + cmMainMaterialCodeDesc
				+ ", cmMainSID=" + cmMainSID + ", remoteDeviceType="
				+ remoteDeviceType + ", cmProduct=" + cmProduct
				+ ", disableUpdateFlag=" + disableUpdateFlag
				+ ", updateButtonTitle=" + updateButtonTitle
				+ ", primarySalgwSeid=" + primarySalgwSeid
				+ ", secondarySalgwSeid=" + secondarySalgwSeid + ", model="
				+ model + ", remoteAccess=" + remoteAccess
				+ ", transportAlarm=" + transportAlarm + ", primarySoldToId="
				+ primarySoldToId + ", secondarySoldToId=" + secondarySoldToId
				+ ", failedSeid=" + failedSeid + ", numberOfSubmit="
				+ numberOfSubmit + ", userName=" + userName + ",popUpHiddenValue="+popUpHiddenValue+"]";
	}

	public String getAlarmOrigination() {
		return alarmOrigination;
	}

	public void setAlarmOrigination(String alarmOrigination) {
		this.alarmOrigination = alarmOrigination;
	}

	public String getSeCodePreview() {
		return seCodePreview;
	}

	public void setSeCodePreview(String seCodePreview) {
		this.seCodePreview = seCodePreview;
	}

	public String getPopUpHiddenValue() {
		return popUpHiddenValue;
	}

	public void setPopUpHiddenValue(String popUpHiddenValue) {
		this.popUpHiddenValue = popUpHiddenValue;
	}

	public String getEquipmentNumber() {
		return equipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}

	public boolean isSiteList() {
		return isSiteList;
	}

	public void setSiteList(boolean isSiteList) {
		this.isSiteList = isSiteList;
	}

	public boolean isTechReg() {
		return isTechReg;
	}

	public void setTechReg(boolean isTechReg) {
		this.isTechReg = isTechReg;
	}

	public List<ExpandedSolutionElement> getExpSolutionElements() {
		return expSolutionElements;
	}

	public void setExpSolutionElements(
			List<ExpandedSolutionElement> expSolutionElements) {
		this.expSolutionElements = expSolutionElements;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getDeviceLastAlarmReceivedDate() {
		return deviceLastAlarmReceivedDate;
	}

	public void setDeviceLastAlarmReceivedDate(String deviceLastAlarmReceivedDate) {
		this.deviceLastAlarmReceivedDate = deviceLastAlarmReceivedDate;
	}

	public Set<ExpandedSolutionElement> getExplodedSolutionElements() {
		return explodedSolutionElements;
	}

	public void setExplodedSolutionElements(
			Set<ExpandedSolutionElement> explodedSolutionElements) {
		this.explodedSolutionElements = explodedSolutionElements;
	}

	public boolean isSelectForAlarming() {
		return selectForAlarming;
	}

	public void setSelectForAlarming(boolean selectForAlarming) {
		this.selectForAlarming = selectForAlarming;
	}

	public boolean isSelectForRemoteAccess() {
		return selectForRemoteAccess;
	}

	public void setSelectForRemoteAccess(boolean selectForRemoteAccess) {
		this.selectForRemoteAccess = selectForRemoteAccess;
	}

	public boolean isDisableRetestFlag() {
		return disableRetestFlag;
	}

	public void setDisableRetestFlag(boolean disableRetestFlag) {
		this.disableRetestFlag = disableRetestFlag;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	
}
