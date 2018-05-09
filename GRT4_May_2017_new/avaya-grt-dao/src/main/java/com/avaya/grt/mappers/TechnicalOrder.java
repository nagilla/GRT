package com.avaya.grt.mappers;
import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * The persistent class for the TECHNICAL_ORDER database table.
 *
 * @author BEA Workshop
 */
public class TechnicalOrder implements Serializable {
	//default serial version id, required for serializable classes.
	private final static Logger logger = Logger.getLogger(TechnicalOrder.class);
	private static final long serialVersionUID = 1L;
	private String orderId;
	private java.util.Date createdDate;
	private java.util.Date completedDate;
	private String deleted;
	private String description;
	/*  [AVAYA]: 08-15-2011 Add SerialNumber Column (Start) */
	private String serialNumber;
	/*  [AVAYA]: 08-15-2011 Add SerialNumber Column (End) */
	private Long initialQuantity;
	private String materialCode;
	private String orderType;
	private Long remainingQuantity;
	private String solutionElementCode;
	private SiteRegistration siteRegistration;
	private String completedBysiteRegId;
	private java.util.Set<TechnicalRegistration> technicalRegistrations;
	
	private java.util.Set<SiebelAsset> siebelAsset;
	private String updatedBy;
	private java.util.Date updatedDate;
	private String createdBy;

	private String isBaseUnit;
	private String isSourceIPO;
	private String releaseString;
	private String materialExclusion;
	private Long openQuantity;
	private String isIPOEligible;
	private String sr_Created;
	private String error_Desc;
	private String isSalesOut;
	
	private Long isSelected;	
	private Long hasActiveEquipmentContract;
	private Long hasActiveSiteContract;
	private String equipmentNumber;
	private String summaryEquipmentNumber;
	private String productLine;
	private String seid;
	private String sid;
	private String mid;
	private String salSeIdPrimarySecondary;
	private boolean autoTR;
	private String assetPK;
	private boolean processed;
	private String groupId;
	private boolean salGateway;	
	private String actionType;
	
	//GRT 4.0 Change
		private String alarmId;
	
	public String getIsSalesOut() {
		return isSalesOut;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public void setIsSalesOut(String isSalesOut) {
		this.isSalesOut = isSalesOut;
	}

	public String getError_Desc() {
		return error_Desc;
	}

	public void setError_Desc(String error_Desc) {
		this.error_Desc = error_Desc;
	}

	public String getSr_Created() {
		return sr_Created;
	}

	public void setSr_Created(String sr_Created) {
		this.sr_Created = sr_Created;
	}

	public String getIsIPOEligible() {
		return isIPOEligible;
	}

	public void setIsIPOEligible(String isIPOEligible) {
		this.isIPOEligible = isIPOEligible;
	}

    public Long getOpenQuantity() {
		return openQuantity;
	}

	public void setOpenQuantity(Long openQuantity) {
		this.openQuantity = openQuantity;
	}

	public String getMaterialExclusion() {
		return materialExclusion;
	}

	public void setMaterialExclusion(String materialExclusion) {
		this.materialExclusion = materialExclusion;
	}

	public String getReleaseString() {
		return releaseString;
	}

	public void setReleaseString(String releaseString) {
		this.releaseString = releaseString;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public TechnicalOrder() {
    }

	public String getOrderId() {
		return this.orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDeleted() {
		return this.deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getInitialQuantity() {
		return this.initialQuantity;
	}
	public void setInitialQuantity(Long initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public String getMaterialCode() {
		return this.materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getOrderType() {
		return this.orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Long getRemainingQuantity() {
		return this.remainingQuantity;
	}
	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public String getSolutionElementCode() {
		return this.solutionElementCode;
	}
	public void setSolutionElementCode(String solutionElementCode) {
		this.solutionElementCode = solutionElementCode;
	}

	//bi-directional many-to-one association to SiteRegistration
	public SiteRegistration getSiteRegistration() {
		return this.siteRegistration;
	}
	public void setSiteRegistration(SiteRegistration siteRegistration) {
		this.siteRegistration = siteRegistration;
	}

	//bi-directional many-to-one association to TechnicalRegistration
	public java.util.Set<TechnicalRegistration> getTechnicalRegistrations() {
		return this.technicalRegistrations;
	}
	public void setTechnicalRegistrations(java.util.Set<TechnicalRegistration> technicalRegistrations) {
		this.technicalRegistrations = technicalRegistrations;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TechnicalOrder)) {
			return false;
		}
		TechnicalOrder castOther = (TechnicalOrder)other;
		if(this.getOrderId() == null || castOther.getOrderId() == null) {
			return false;
		}
		return new EqualsBuilder()
			.append(this.getOrderId(), castOther.getOrderId())
			.isEquals();
    }

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getOrderId())
			.toHashCode();
    }

	public String toString() {
		return new ToStringBuilder(this)
			.append("orderId", getOrderId())
			.toString();
	}

	/**  [AVAYA]: 08-15-2011 SerialNumber getter and setter (Start) **/
	/**  [AVAYA]: 09-15-2011 SerialNumber getter updated on line 178 'this' (Start) **/
	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**  [AVAYA]: 08-15-2011 SerialNumber getter and setter (End) **/

	public boolean isIPO(){
		 boolean returnVal = false;
		 if((this.isSourceIPO!=null)&& this.isSourceIPO.equalsIgnoreCase("y")){
			 return true;
		 }

		 return returnVal;
	}

	public java.util.Set<SiebelAsset> getSiebelAsset() {
		return siebelAsset;
	}

	public void setSiebelAsset(java.util.Set<SiebelAsset> siebelAsset) {
		this.siebelAsset = siebelAsset;
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

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
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

	public boolean isAutoTR() {
		return autoTR;
	}

	public void setAutoTR(boolean autoTR) {
		this.autoTR = autoTR;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSalSeIdPrimarySecondary() {
		return salSeIdPrimarySecondary;
	}

	public void setSalSeIdPrimarySecondary(String salSeIdPrimarySecondary) {
		this.salSeIdPrimarySecondary = salSeIdPrimarySecondary;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isSalGateway() {
		return salGateway;
	}

	public void setSalGateway(boolean salGateway) {
		this.salGateway = salGateway;
	}

	public java.util.Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(java.util.Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getCompletedBysiteRegId() {
		return completedBysiteRegId;
	}

	public void setCompletedBysiteRegId(String completedBysiteRegId) {
		this.completedBysiteRegId = completedBysiteRegId;
	}


	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	
}