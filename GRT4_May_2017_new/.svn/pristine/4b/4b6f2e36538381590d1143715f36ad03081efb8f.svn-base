package com.avaya.grt.mappers;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;



public class SalProductRegistration implements Serializable {

	private static final long serialVersionUID = -617827225788847238L; 
	
//	Required from all APIs.
	private String soldTo;
	private String filePath;

	private TechnicalOrder technicalOrder;
	
	private SiteList siteList;
	private TechnicalRegistration technicalRegistration;
	private String onsiteContactEmail, onsiteContactPhone, onsiteContactFirstName, onsiteContactLastName;
	private String requesterContactEmail , requesterContactPhone , requesterContactFirstName , requesterContactLastName;
	
	//Required for SR creation.
	private String materialCode;
	private String serialNumber;
	
	// Return values.
	private String errorCode;
	private String errorDescription;
	
	private String optType;
	private String aorig;
	
	public String getAorig() {
		return aorig;
	}
	public void setAorig(String aorig) {
		this.aorig = aorig;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public SiteList getSiteList() {
		return siteList;
	}
	public void setSiteList(SiteList siteList) {
		this.siteList = siteList;
		if(this.siteList != null) {
			SiteRegistration siteRegistration = this.siteList.getSiteRegistration();
			if(siteRegistration != null) {
				this.onsiteContactFirstName = siteRegistration.getOnsiteFirstName();
				this.onsiteContactLastName = siteRegistration.getOnsiteLastName();
				this.onsiteContactPhone = siteRegistration.getOnsitePhone();
				this.onsiteContactEmail = siteRegistration.getOnsiteEmail();
				setRequesterContact(siteRegistration);
			}
		}
	}
	
	private void setRequesterContact(SiteRegistration siteRegistration) {
		if (siteRegistration.getFirstName()!= null) 
			this.requesterContactFirstName = siteRegistration.getFirstName();
		else 
			this.requesterContactFirstName = "";
		if (siteRegistration.getLastName()!= null) 
			this.requesterContactLastName = siteRegistration.getLastName();
		else 
			this.requesterContactLastName = "";
		if (siteRegistration.getReportEmailId()!= null) 
			this.requesterContactEmail = siteRegistration.getReportEmailId();
		else 
			this.requesterContactEmail = "";
		if (siteRegistration.getReportPhone()!= null) 
			this.requesterContactPhone = siteRegistration.getReportPhone();
		else 
			this.requesterContactPhone = "";
	}
	public TechnicalOrder getTechnicalOrder() {
		return technicalOrder;
	}
	public void setTechnicalOrder(TechnicalOrder technicalOrder) {
		this.technicalOrder = technicalOrder;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSoldTo() {
		return soldTo;
	}
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	public TechnicalRegistration getTechnicalRegistration() {
		return technicalRegistration;
	}
	public void setTechnicalRegistration(TechnicalRegistration technicalRegistration) {
		this.technicalRegistration = technicalRegistration;
		if(this.technicalRegistration != null) {
			SiteRegistration siteRegistration = this.technicalRegistration.getTechnicalOrder().getSiteRegistration();
			if(siteRegistration != null) {
				this.onsiteContactFirstName = siteRegistration.getOnsiteFirstName();
				this.onsiteContactLastName = siteRegistration.getOnsiteLastName();
				this.onsiteContactPhone = siteRegistration.getOnsitePhone();
				this.onsiteContactEmail = siteRegistration.getOnsiteEmail();
				setRequesterContact(siteRegistration);
			}
		}
	}
	public String getOnsiteContactEmail() {
		return onsiteContactEmail;
	}
	public void setOnsiteContactEmail(String onsiteContactEmail) {
		this.onsiteContactEmail = onsiteContactEmail;
	}
	public String getOnsiteContactFirstName() {
		return onsiteContactFirstName;
	}
	public void setOnsiteContactFirstName(String onsiteContactFirstName) {
		this.onsiteContactFirstName = onsiteContactFirstName;
	}
	public String getOnsiteContactLastName() {
		return onsiteContactLastName;
	}
	public void setOnsiteContactLastName(String onsiteContactLastName) {
		this.onsiteContactLastName = onsiteContactLastName;
	}
	public String getOnsiteContactPhone() {
		return onsiteContactPhone;
	}
	public void setOnsiteContactPhone(String onsiteContactPhone) {
		this.onsiteContactPhone = onsiteContactPhone;
	}
	public String getRequesterContactEmail() {
		return requesterContactEmail;
	}
	public void setRequesterContactEmail(String requesterContactEmail) {
		this.requesterContactEmail = requesterContactEmail;
	}
	public String getRequesterContactFirstName() {
		return requesterContactFirstName;
	}
	public void setRequesterContactFirstName(String requesterContactFirstName) {
		this.requesterContactFirstName = requesterContactFirstName;
	}
	public String getRequesterContactLastName() {
		return requesterContactLastName;
	}
	public void setRequesterContactLastName(String requesterContactLastName) {
		this.requesterContactLastName = requesterContactLastName;
	}
	public String getRequesterContactPhone() {
		return requesterContactPhone;
	}
	public void setRequesterContactPhone(String requesterContactPhone) {
		this.requesterContactPhone = requesterContactPhone;
	}
	

}
