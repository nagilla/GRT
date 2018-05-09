
package com.grt.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * DTO class to have the details about the Technical Order.
 */
public class TechnicalOrderDetail implements Serializable{
	/**
	 * Default version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String registrationId;
	private String materialCode;
	/**  [AVAYA]: 08-16-2011 Add serialNumber field (Start) **/
	private String serialNumber;	
	/**  [AVAYA]: 08-16-2011 Add serialNumber field (End) **/
	private String solutionElementCode;
	private Long initialQuantity;
	private Long remainingQuantity;
	private Long removedQuantity;
	private Long movedQuantity;
	private String processStep;
	private String statusId;
	private Boolean deleted;
	private String description;
	private Boolean selectforRegistration;
	private Boolean isMaterialCodeInValid;
	private String soldToId;
	private String toSoldToId;
	private String productId;
	private String orderType;
	private String solutionElementId;
	private String productCode;
	private String productType;
	//Added to facilitate fields in IPO XML
	private String isBaseUnit;
	private String isSourceIPO;
	private String releaseString;
	private Boolean ipoFlagIbaseJsp;
	private Boolean materialEntryFlag;
	
	/*AVAYA GRT2.1 Added new property (Start)
	 */
	private String materialExclusion = "";
	 
	/*AVAYA GRT2.1 Added new property (End)
	 */
	
	private String installBaseStatusId;
	private String techRegStatusId;
	private String finalValidationStatusId;
	
	private boolean autoTR; 
	
	/*
	 * [AVAYA] GRT2.0 Added new properties Ship To and cut over date
	 */
	private String shipToId;
	private Calendar cutOverDate;
	
	//Pipeline quantity place holders
	private Long pipelineIBQuantity;
	private Long pipelineEQRQuantity;
	private Long pipelineEQRMoveQuantity;
	
	private String serialValidateFlag;
	private String selectAndUselectAll;
	
	//DTO for error_desc SAP Error in technicalOrdertables
	private String errorDescription;
	
	private String isSalesOut;
	
	private String productLine;
	private String technicallyRegisterable;

	private Long isSelected;	
	private Long hasActiveEquipmentContract;
	private Long hasActiveSiteContract;
	private String equipmentNumber;
	private String toEquipmentNumber;
	private String summaryEquipmentNumber;
	private String seid;
	// EQR - properties added
	private String activeContractExist;
	private String assetNumber;
	private String assetPK;
	private String sid;
	private String mid;
	private String sr_created;
	private String groupId;
	
	private String coreUnitSelected;
	
	private List<String> materialEntryVersion;
	
	private boolean checked = false;
	
	private String exclusionSource;
	private boolean exclusionFlag;
	private boolean salGateway;
	
	private List<String> agreementsList;
	private String agreements;
	private String accessType;
	private BigDecimal beforeQuantity = new BigDecimal("0.0");
	private BigDecimal afterQuantity = new BigDecimal("0.0");
	
	private String actionType;
	private String newSerialNumber;
	private String serialized;
	private String nickName;
	private String regStatus;
	private boolean errorFlag;
	
	private boolean  toggleMore;
	private List<String> toggleAgreementsList;
	private String originalSerialNumber;
	
	private String isSAP;
	private String isMaestro;
	private String isNortel;
	
	private boolean excludedMaestroOrNortel; 
	
	private String serviceUsage;
	
	public String getServiceUsage() {
		return serviceUsage;
	}
	public void setServiceUsage(String serviceUsage) {
		this.serviceUsage = serviceUsage;
	}
	public boolean isToggleMore() {
		return toggleMore;
	}
	public void setToggleMore(boolean toggleMore) {
		this.toggleMore = toggleMore;
	}
	
	/**
	 * @return the assetPK
	 */
	public String getAssetPK() {
		return assetPK;
	}
	/**
	 * @param assetPK the assetPK to set
	 */
	public void setAssetPK(String assetPK) {
		this.assetPK = assetPK;
	}
	/**
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}
	/**
	 * @param assetNumber the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	/**
	 * @return the activeContractExist
	 */
	public String getActiveContractExist() {
		return activeContractExist;
	}
	/**
	 * @param activeContractExist the activeContractExist to set
	 */
	public void setActiveContractExist(String activeContractExist) {
		this.activeContractExist = activeContractExist;
	}
	public String getIsSalesOut() {
		return isSalesOut;
	}
	public void setIsSalesOut(String isSalesOut) {
		this.isSalesOut = isSalesOut;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public Calendar getCutOverDate() {
		return cutOverDate;
	}
	public void setCutOverDate(Calendar cutOverDate) {
		this.cutOverDate = cutOverDate;
	}
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public Boolean getIpoFlagIbaseJsp() {
		return ipoFlagIbaseJsp;
	}
	public void setIpoFlagIbaseJsp(Boolean ipoFlagIbaseJsp) {
		this.ipoFlagIbaseJsp = ipoFlagIbaseJsp;
	}
	public String getIsBaseUnit() {
		return isBaseUnit;
	}
	public void setIsBaseUnit(String isBaseUnit) {
		this.isBaseUnit = isBaseUnit;
	}
	public String getIsSourceIPO() {
		return isSourceIPO;
	}
	public void setIsSourceIPO(String isSourceIPO) {
		this.isSourceIPO = isSourceIPO;
	}
	public String getReleaseString() {
		return releaseString;
	}
	public void setReleaseString(String releaseString) {
		this.releaseString = releaseString;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Long getInitialQuantity() {
		return initialQuantity;
	}
	public void setInitialQuantity(Long initialQuantity) {
		this.initialQuantity = initialQuantity;
	}
	public String getMaterialCode() {
		return StringEscapeUtils.escapeHtml(materialCode);
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
	public Long getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	public Long getMovedQuantity() {
		return movedQuantity;
	}
	public void setMovedQuantity(Long movedQuantity) {
		this.movedQuantity = movedQuantity;
	}
	public String getSolutionElementCode() {
		return solutionElementCode;
	}
	public void setSolutionElementCode(String solutionElementCode) {
		this.solutionElementCode = solutionElementCode;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getSelectforRegistration() {
		return selectforRegistration;
	}
	public void setSelectforRegistration(Boolean selectforRegistration) {
		this.selectforRegistration = selectforRegistration;
	}
	public Boolean getIsMaterialCodeInValid() {
		return isMaterialCodeInValid;
	}
	public void setIsMaterialCodeInValid(Boolean isMaterialCodeInValid) {
		this.isMaterialCodeInValid = isMaterialCodeInValid;
	}
	public String getSoldToId() {
		return soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	public String getToSoldToId() {
		return toSoldToId;
	}
	public void setToSoldToId(String toSoldToId) {
		this.toSoldToId = toSoldToId;
	}
	public Long getRemovedQuantity() {
		return removedQuantity;
	}
	public void setRemovedQuantity(Long removedQuantity) {
		this.removedQuantity = removedQuantity;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	/**  [AVAYA]: 08-16-2011 serialNumber getter and setter (Start) **/
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**  [AVAYA]: 08-16-2011  serialNumber getter and setter (Start) **/

	/** AVAYA : GRT 2.1 materialExclusion getter and setter (Start)**/
	public String getMaterialExclusion() {
		return materialExclusion;
	}
	public void setMaterialExclusion(String materialExclusion) {
		this.materialExclusion = materialExclusion;
	}
	/** AVAYA : GRT 2.1 materialExclusion getter and setter (End)**/
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
	public String getSerialValidateFlag() {
		return serialValidateFlag;
	}
	public void setSerialValidateFlag(String serialValidateFlag) {
		this.serialValidateFlag = serialValidateFlag;
	}
	public String getSelectAndUselectAll() {
		return selectAndUselectAll;
	}
	public void setSelectAndUselectAll(String selectAndUselectAll) {
		this.selectAndUselectAll = selectAndUselectAll;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getTechnicallyRegisterable() {
		return technicallyRegisterable;
	}
	public void setTechnicallyRegisterable(String technicallyRegisterable) {
		this.technicallyRegisterable = technicallyRegisterable;
	}
	public String getEquipmentNumber() {
		return equipmentNumber;
	}
	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}
	public Long getHasActiveEquipmentContract() {
		return hasActiveEquipmentContract;
	}
	public void setHasActiveEquipmentContract(Long hasActiveEquipmentContract) {
		this.hasActiveEquipmentContract = hasActiveEquipmentContract;
	}
	public Long getHasActiveSiteContract() {
		return hasActiveSiteContract;
	}
	public void setHasActiveSiteContract(Long hasActiveSiteContract) {
		this.hasActiveSiteContract = hasActiveSiteContract;
	}
	public Long getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Long isSelected) {
		this.isSelected = isSelected;
	}
	public String getSeid() {
		return seid;
	}
	public void setSeid(String seid) {
		this.seid = seid;
	}
	public String getSummaryEquipmentNumber() {
		return summaryEquipmentNumber;
	}
	public void setSummaryEquipmentNumber(String summaryEquipmentNumber) {
		this.summaryEquipmentNumber = summaryEquipmentNumber;
	}
	public String getToEquipmentNumber() {
		return toEquipmentNumber;
	}
	public void setToEquipmentNumber(String toEquipmentNumber) {
		this.toEquipmentNumber = toEquipmentNumber;
	}
	public boolean isAutoTR() {
		return autoTR;
	}
	public void setAutoTR(boolean autoTR) {
		this.autoTR = autoTR;
	}
	public List<String> getMaterialEntryVersion() {
		return materialEntryVersion;
	}
	public void setMaterialEntryVersion(List<String> materialEntryVersion) {
		this.materialEntryVersion = materialEntryVersion;
	}
	public Boolean getMaterialEntryFlag() {
		return materialEntryFlag;
	}
	public void setMaterialEntryFlag(Boolean materialEntryFlag) {
		this.materialEntryFlag = materialEntryFlag;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSr_created() {
		return sr_created;
	}
	public void setSr_created(String sr_created) {
		this.sr_created = sr_created;
	}
	/**
	 * @return the coreUnitSelected
	 */
	public String getCoreUnitSelected() {
		return coreUnitSelected;
	}
	/**
	 * @param coreUnitSelected the coreUnitSelected to set
	 */
	public void setCoreUnitSelected(String coreUnitSelected) {
		this.coreUnitSelected = coreUnitSelected;
	}
	public String getExclusionSource() {
		return exclusionSource;
	}
	public void setExclusionSource(String exclusionSource) {
		this.exclusionSource = exclusionSource;
	}
	public Long getPipelineEQRQuantity() {
		return pipelineEQRQuantity;
	}
	public void setPipelineEQRQuantity(Long pipelineEQRQuantity) {
		this.pipelineEQRQuantity = pipelineEQRQuantity;
	}
	public Long getPipelineIBQuantity() {
		return pipelineIBQuantity;
	}
	public void setPipelineIBQuantity(Long pipelineIBQuantity) {
		this.pipelineIBQuantity = pipelineIBQuantity;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public boolean isExclusionFlag() {
		return exclusionFlag;
	}
	public void setExclusionFlag(boolean exclusionFlag) {
		this.exclusionFlag = exclusionFlag;
	}
	public boolean isSalGateway() {
		return salGateway;
	}
	public void setSalGateway(boolean salGateway) {
		this.salGateway = salGateway;
	}
	public List<String> getAgreementsList() {
		return agreementsList;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getAgreements() {
		return agreements;
	}
	public void setAgreements(String agreements) {
		this.agreements = agreements;
	}
	public void setAgreementsList(List<String> agreementsList) {
		this.agreementsList = agreementsList;
	}
	public BigDecimal getBeforeQuantity() {
		return beforeQuantity;
	}
	public void setBeforeQuantity(BigDecimal beforeQuantity) {
		this.beforeQuantity = beforeQuantity;
	}
	public BigDecimal getAfterQuantity() {
		return afterQuantity;
	}
	public void setAfterQuantity(BigDecimal afterQuantity) {
		this.afterQuantity = afterQuantity;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getNewSerialNumber() {
		return newSerialNumber;
	}
	public void setNewSerialNumber(String newSerialNumber) {
		this.newSerialNumber = newSerialNumber;
	}
	public String getSerialized() {
		//If serialized field is present then return the value
		if(null != this.serialized && !this.serialized.isEmpty()) {
			return this.serialized;
		}
		//If serialized is null then return Y if serial number is present
		return (null != serialNumber && StringUtils.isNotEmpty(serialNumber)) ? "Y" :"N";
	}
	public void setSerialized(String serialized) {
		this.serialized = serialized;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	public Long getPipelineEQRMoveQuantity() {
		return pipelineEQRMoveQuantity;
	}
	public void setPipelineEQRMoveQuantity(Long pipelineEQRMoveQuantity) {
		this.pipelineEQRMoveQuantity = pipelineEQRMoveQuantity;
	}
	public boolean isErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}
	
	public List<String> getToggleAgreementsList() {
		return toggleAgreementsList;
	}
	public void setToggleAgreementsList(List<String> toggleAgreementsList) {
		this.toggleAgreementsList = toggleAgreementsList;
	}
	public String getOriginalSerialNumber() {
		return originalSerialNumber;
	}
	public void setOriginalSerialNumber(String originalSerialNumber) {
		this.originalSerialNumber = originalSerialNumber;
	}
	public String getIsSAP() {
		return isSAP;
	}
	public void setIsSAP(String isSAP) {
		this.isSAP = isSAP;
	}
	public String getIsMaestro() {
		return isMaestro;
	}
	public void setIsMaestro(String isMaestro) {
		this.isMaestro = isMaestro;
	}
	public String getIsNortel() {
		return isNortel;
	}
	public void setIsNortel(String isNortel) {
		this.isNortel = isNortel;
	}
	public boolean isExcludedMaestroOrNortel() {
		return excludedMaestroOrNortel;
	}
	public void setExcludedMaestroOrNortel(boolean excludedMaestroOrNortel) {
		this.excludedMaestroOrNortel = excludedMaestroOrNortel;
	}
}
