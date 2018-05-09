package com.avaya.grt.mappers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.grt.util.StatusEnum;

/**
 * The persistent class for the SITE_LIST database table.
 *
 * @author BEA Workshop
 */
public class SiteList  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String id;
	private String materialCode;
	private String model;
	private String remoteAccess;
	private String seCode;
	private String siteId;
	private String soldToId;
	private String transportAlarm;
	private String productId;
	private String solutionElementId;
	private String productCode;
	private String artSrNo;
	private Status status;
	private String errorCode;
	private String isEligible;
	private String errorDesc;
	private SRRequest srRequest;
	private String strSiebelSRNo;
	private String primarySALGatewaySEID, secondarySALGatewaySEID;
	private String sid, mid;
	private String materialCodeDescription, productLine;
	private SiteRegistration siteRegistration;
	//A non-persistable property, Will be used to represent the user selections on the UI.
	private boolean selected;
	//Map them
	private String equipmentNumber, summaryEquipmentNumber, release;

	private String seId;
	private String alarmId;

	private String groupId;

	private SRRequest stepBSRRequest;

	private Status stepBStatus;

	private String deviceStatus, deviceLastAlarmReceivedDate;
	//Map It
	private String subErrorCode;

	private boolean overRideButtonStatus = false;
	private boolean updateButtonStatus = false;
	private boolean submitButtonStatus = false;
	private boolean detailButtonStatus = false;	
	private boolean selectForAlarmAndConnectivity;

	private String numberOfSubmit;
	private String troubleShootURL;
	private boolean checkBoxDisabled;
	private String salGateWaySeid;
	private String artId;

	private String strStepBSRNo;
	private String installScript;
	private java.util.Set<ExpandedSolutionElement> explodedSolutionElements = new HashSet<ExpandedSolutionElement>();
	
	private boolean selectForRemoteAccess;
	private boolean remoteAccessCheckBoxDisabled;
	private boolean selectForAlarming;
	private boolean alarmingCheckBoxDisabled;
	private List<ExpandedSolutionElement> expSolutionElements = new ArrayList<ExpandedSolutionElement>();
	
	private int index = -1;
	private String cancelled;
	private SRRequest tempSrRequest;
	
	private Date stepASubmittedDate;
	private Date stepACompletedDate;
	private Date stepBSubmittedDate;
	private Date stepBCompletedDate;
	
	private String deviceOnboardOp;
	private String transactionDetails;
	private String failedSeid;
	
	private String existence;
	
    public String getExistence() {
		return existence;
	}

	public void setExistence(String existence) {
		this.existence = existence;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public SRRequest getSrRequest() {
		return srRequest;
	}

	public void setSrRequest(SRRequest srRequest) {
		this.srRequest = srRequest;
		if(srRequest != null){
			this.setArtSrNo(srRequest.getSiebelSRNo());
		}
	}

	public SiteList() {
    }

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getMaterialCode() {
		return this.materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getModel() {
		return this.model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getRemoteAccess() {
		return this.remoteAccess;
	}
	public void setRemoteAccess(String remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	public String getSeCode() {
		return this.seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public String getSiteId() {
		return this.siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSoldToId() {
		return this.soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public String getTransportAlarm() {
		return this.transportAlarm;
	}
	public void setTransportAlarm(String transportAlarm) {
		this.transportAlarm = transportAlarm;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSolutionElementId() {
		return solutionElementId;
	}

	public void setSolutionElementId(String solutionElementId) {
		this.solutionElementId = solutionElementId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getArtSrNo() {
			return artSrNo;
	}

	public void setArtSrNo(String artSrNo) {
		this.artSrNo = artSrNo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getIsEligible() {
		return isEligible;
	}

	public void setIsEligible(String isEligible) {
		this.isEligible = isEligible;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getMaterialCodeDescription() {
		return materialCodeDescription;
	}

	public void setMaterialCodeDescription(String materialCodeDescription) {
		this.materialCodeDescription = materialCodeDescription;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPrimarySALGatewaySEID() {
		return primarySALGatewaySEID;
	}

	public void setPrimarySALGatewaySEID(String primarySALGatewaySEID) {
		this.primarySALGatewaySEID = primarySALGatewaySEID;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getSecondarySALGatewaySEID() {
		return secondarySALGatewaySEID;
	}

	public void setSecondarySALGatewaySEID(String secondarySALGatewaySEID) {
		this.secondarySALGatewaySEID = secondarySALGatewaySEID;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public SiteRegistration getSiteRegistration() {
		return siteRegistration;
	}

	public void setSiteRegistration(SiteRegistration siteRegistration) {
		this.siteRegistration = siteRegistration;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
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

	public String getEquipmentNumber() {
		return equipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public SRRequest getStepBSRRequest() {
		return stepBSRRequest;
	}

	public void setStepBSRRequest(SRRequest stepBSRRequest) {
		this.stepBSRRequest = stepBSRRequest;
	}

	public String getSummaryEquipmentNumber() {
		return summaryEquipmentNumber;
	}

	public void setSummaryEquipmentNumber(String summaryEquipmentNumber) {
		this.summaryEquipmentNumber = summaryEquipmentNumber;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getSubErrorCode() {
		return subErrorCode;
	}

	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}

	public Status getStepBStatus() {
		return stepBStatus;
	}

	public void setStepBStatus(Status stepBStatus) {
		this.stepBStatus = stepBStatus;
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

	public boolean isSelectForAlarmAndConnectivity() {
		return selectForAlarmAndConnectivity;
	}

	public void setSelectForAlarmAndConnectivity(
			boolean selectForAlarmAndConnectivity) {
		this.selectForAlarmAndConnectivity = selectForAlarmAndConnectivity;
	}

	public String getNumberOfSubmit() {
		return numberOfSubmit;
	}

	public void setNumberOfSubmit(String numberOfSubmit) {
		this.numberOfSubmit = numberOfSubmit;
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

	/**
	 * @return the salGateWaySeid
	 */
	public String getSalGateWaySeid() {
		return salGateWaySeid;
	}
	
	public String getSalGatewaySeids() {
        return this.getPrimarySALGatewaySEID() + ((StringUtils.isNotEmpty(this.getSecondarySALGatewaySEID()))?("+" + this.getSecondarySALGatewaySEID()):"");
  }


	/**
	 * @param salGateWaySeid the salGateWaySeid to set
	 */
	public void setSalGateWaySeid(String salGateWaySeid) {
		this.salGateWaySeid = salGateWaySeid;
	}

	public String getArtId() {
		return artId;
	}

	public void setArtId(String artId) {
		this.artId = artId;
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

	public String getInstallScript() {
		return installScript;
	}

	public void setInstallScript(String installScript) {
		this.installScript = installScript;
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
		String gatewayseid = this.getPrimarySALGatewaySEID();
		if(StringUtils.isNotEmpty(this.getSecondarySALGatewaySEID())) {
			gatewayseid += "+" + this.getSecondarySALGatewaySEID();
		}
		return gatewayseid;
	}

	public java.util.Set<ExpandedSolutionElement> getExplodedSolutionElements() {
		return explodedSolutionElements;
	}

	public void setExplodedSolutionElements(
			java.util.Set<ExpandedSolutionElement> explodedSolutionElements) {
		this.explodedSolutionElements = explodedSolutionElements;
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
	 * @return the tempSrRequest
	 */
	public SRRequest getTempSrRequest() {
		return tempSrRequest;
	}

	/**
	 * @param tempSrRequest the tempSrRequest to set
	 */
	public void setTempSrRequest(SRRequest tempSrRequest) {
		this.tempSrRequest = tempSrRequest;
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

	public String getDeviceOnboardOp() {
		return deviceOnboardOp;
	}

	public void setDeviceOnboardOp(String deviceOnboardOp) {
		this.deviceOnboardOp = deviceOnboardOp;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public String getFailedSeid() {
		return failedSeid;
	}

	public void setFailedSeid(String failedSeid) {
		this.failedSeid = failedSeid;
	}
	
	
}