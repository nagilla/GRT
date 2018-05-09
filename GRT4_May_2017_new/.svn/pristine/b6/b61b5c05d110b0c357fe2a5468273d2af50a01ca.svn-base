package com.grt.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.grt.util.StatusEnum;

/**
 * The persistent class for the UNIVERSAL_PRODUCT_REGISTRATION database table.
 * 
 * @author BEA Workshop
 */
public class UniversalProductRegistration  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String productInstallId;
	private String alarmId;
	private java.util.Date createdDate;
	private String productRegistrationListId;
	private String soldToId;
	private String statusId;
	private String statusDescription;
	private String updatedBy;
	private java.util.Date updatedDate;
	private String alarmRegistrationId;
	private String status;
	private String materialCode;
	private String solutionElement;
	private String software;
	private String softwareVersion;
	private String accessType;
	private String dialNumber;
	private String connectivity;
	private String systemId;
	private String moduleId;
	private String serialNo;
	private String alarmStatusId;
	private String alarmStatusDescription;
	private String salRegistrationId;
	private String siebelSrNo;
	private String alarmSrNumber = new String("");
	
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
	public String getProductInstallId() {
		return productInstallId;
	}
	public void setProductInstallId(String productInstallId) {
		this.productInstallId = productInstallId;
	}
	public String getProductRegistrationListId() {
		return productRegistrationListId;
	}
	public void setProductRegistrationListId(String productRegistrationListId) {
		this.productRegistrationListId = productRegistrationListId;
	}
	public String getSoldToId() {
		return soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDialNumber() {
		return dialNumber;
	}
	public void setDialNumber(String dialNumber) {
		this.dialNumber = dialNumber;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getSoftware() {
		return software;
	}
	public void setSoftware(String software) {
		this.software = software;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getSolutionElement() {
		return solutionElement;
	}
	public void setSolutionElement(String solutionElement) {
		this.solutionElement = solutionElement;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
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
	public String getSalRegistrationId() {
		return salRegistrationId;
	}
	public void setSalRegistrationId(String salRegistrationId) {
		this.salRegistrationId = salRegistrationId;
	}
	public String getSiebelSrNo() {
		return siebelSrNo;
	}
	public void setSiebelSrNo(String siebelSrNo) {
		this.siebelSrNo = siebelSrNo;
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
}