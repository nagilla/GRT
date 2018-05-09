package com.grt.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.SiteList;
import com.grt.util.StatusEnum;

/**
 * The persistent class for the SAL_MIGRATION database table.
 * 
 * @author BEA Workshop
 */
public class SalMigrationDto  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SalMigrationDto.class);
	private String migrationId;
	private String alarmId;
	private java.util.Date createdDate;
	private String existingGateway;
	private String gatewayCreated;
	private String newGateway;
	private String statusId;
	private String statusDescription;
	private String updatedBy;
	private java.util.Date updatedDate;
	private String alarmRegistrationId;
	private String salRegistrationId;
	private String alarmStatusId;
	private String alarmStatusDescription;
	private String soldToId;
	private String technicalOrderId;	
	private String siebelSrNo;
	private String alarmSrNumber = new String("");
	private String salType;
	private String siteRegistrationId;
	private String saveType;
	private String uploadFilePath;
	private String gateWaySoldToId;
	private String siebelSrRequestId;
	private String materialCode;
	private String primarySEID;
	private String secondarySEID;
	
	private List<TechnicalOrderDetail> siebelAssetList;
	private List<SiteList> siteList = new ArrayList<SiteList>();
	
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getAlarmRegistrationId() {
		return alarmRegistrationId;
	}
	public void setAlarmRegistrationId(String alarmRegistrationId) {
		this.alarmRegistrationId = alarmRegistrationId;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getExistingGateway() {
		return existingGateway;
	}
	public void setExistingGateway(String existingGateway) {
		this.existingGateway = existingGateway;
	}
	public String getGatewayCreated() {
		return gatewayCreated;
	}
	public void setGatewayCreated(String gatewayCreated) {
		this.gatewayCreated = gatewayCreated;
	}
	public String getMigrationId() {
		return migrationId;
	}
	public void setMigrationId(String migrationId) {
		this.migrationId = migrationId;
	}
	public String getNewGateway() {
		return newGateway;
	}
	public void setNewGateway(String newGateway) {
		this.newGateway = newGateway;
	}
	public String getSalRegistrationId() {
		return salRegistrationId;
	}
	public void setSalRegistrationId(String salRegistrationId) {
		this.salRegistrationId = salRegistrationId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDesc) {
		this.statusDescription = statusDesc;
	}
	public String getAlarmStatusDescription() {
		return alarmStatusDescription;
	}
	public void setAlarmStatusDescription(String alarmStatusDescription) {
		this.alarmStatusDescription = alarmStatusDescription;
	}
	public String getAlarmStatusId() {
		return alarmStatusId;
	}
	public void setAlarmStatusId(String alarmStatusId) {
		this.alarmStatusId = alarmStatusId;
	}
	public List<SiteList> getSiteList() {
		return siteList;
	}
	public void setSiteList(List<SiteList> siteList) {
		this.siteList = siteList;
	}
	public String getSoldToId() {
		return soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	public String getTechnicalOrderId() {
		return technicalOrderId;
	}
	public void setTechnicalOrderId(String technicalOrderId) {
		this.technicalOrderId = technicalOrderId;
	}
	public String getSiebelSrNo() {
		return siebelSrNo;
	}
	public void setSiebelSrNo(String siebelSrNo) {
		this.siebelSrNo = siebelSrNo;
	}
	public String getSalType() {
		return salType;
	}
	public void setSalType(String salType) {
		this.salType = salType;
	}
	public String getSiteRegistrationId() {
		return siteRegistrationId;
	}
	public void setSiteRegistrationId(String siteRegistrationId) {
		this.siteRegistrationId = siteRegistrationId;
	}
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public List<TechnicalOrderDetail> getSiebelAssetList() {
		return siebelAssetList;
	}
	public void setSiebelAssetList(List<TechnicalOrderDetail> siebelAssetList) {
		this.siebelAssetList = siebelAssetList;
	}
	public String getUploadFilePath() {
		return uploadFilePath;
	}
	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}
	public String getGateWaySoldToId() {
		return gateWaySoldToId;
	}
	public void setGateWaySoldToId(String gateWaySoldToId) {
		this.gateWaySoldToId = gateWaySoldToId;
	}
	public String getAlarmSrNumber() {
		if(StringUtils.isNotEmpty(this.alarmStatusId) && this.alarmStatusId.equals(StatusEnum.COMPLETED.getStatusId())){
			return "";
		} else {
			return alarmSrNumber;
		}
	}
	public void setAlarmSrNumber(String alarmSrNumber) {
		this.alarmSrNumber = alarmSrNumber;
	}
	public String getSiebelSrRequestId() {
		return siebelSrRequestId;
	}
	public void setSiebelSrRequestId(String siebelSrRequestId) {
		this.siebelSrRequestId = siebelSrRequestId;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getPrimarySEID() {
		return primarySEID;
	}
	public void setPrimarySEID(String primarySEID) {
		this.primarySEID = primarySEID;
	}
	public String getSecondarySEID() {
		return secondarySEID;
	}
	public void setSecondarySEID(String secondarySEID) {
		this.secondarySEID = secondarySEID;
	}
	
	}