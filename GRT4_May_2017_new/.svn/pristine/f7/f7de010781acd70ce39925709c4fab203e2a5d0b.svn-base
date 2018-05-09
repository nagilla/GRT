package com.grt.dto;

import java.io.Serializable;

public class ServiceRequest implements Serializable {
	 private static final long serialVersionUID = 1L;

	 private String srId;
	 private String srNumber;
	 private String description;
	 private String ownerFirstName;
	 private String ownerLastName;
	 private String srType;
	 private String srSubType;
	 private String status;
	 private String subStatus;
	 private String accountLocation;
	 private String urgency;
	 private String customerTicketNumber;
	 private String contactFirstName;
	 private String contactLastName;
	 private String contactBusinessPhone;
	 private String contactEmail;
	 private String completionNarrative;
	 private String resolutionAction;
	 private String routingInfo;
	 
	public String getRoutingInfo() {
		return routingInfo;
	}
	public void setRoutingInfo(String routingInfo) {
		this.routingInfo = routingInfo;
	}
	public String getContactBusinessPhone() {
		return contactBusinessPhone;
	}
	public void setContactBusinessPhone(String contactBusinessPhone) {
		this.contactBusinessPhone = contactBusinessPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactFirstName() {
		return contactFirstName;
	}
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}
	public String getContactLastName() {
		return contactLastName;
	}
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}
	public String getAccountLocation() {
		return accountLocation;
	}
	public void setAccountLocation(String accountLocation) {
		this.accountLocation = accountLocation;
	}
	public String getCustomerTicketNumber() {
		return customerTicketNumber;
	}
	public void setCustomerTicketNumber(String customerTicketNumber) {
		this.customerTicketNumber = customerTicketNumber;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getSrId() {
		return srId;
	}
	public void setSrId(String srId) {
		this.srId = srId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public String getSrNumber() {
		return srNumber;
	}
	public void setSrNumber(String srNumber) {
		this.srNumber = srNumber;
	}
	public String getSrType() {
		return srType;
	}
	public void setSrType(String type) {
		srType = type;
	}
	public String getSrSubType() {
		return srSubType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public void setSrSubType(String subtypeDetail) {
		srSubType = subtypeDetail;
	}
	public String getCompletionNarrative() {
		return completionNarrative;
	}
	public void setCompletionNarrative(String completionNarrative) {
		this.completionNarrative = completionNarrative;
	}
	 
	public boolean equals(Object serviceRequest){
		try {
			if(serviceRequest instanceof ServiceRequest) {
				ServiceRequest sr = (ServiceRequest)serviceRequest;
				return (this.srNumber.equalsIgnoreCase(sr.getSrNumber()) && this.srType.equalsIgnoreCase(sr.getSrType()) && this.srSubType.equalsIgnoreCase(sr.getSrSubType()) && this.status.equalsIgnoreCase(sr.getStatus()));
			}
		} catch(Throwable throwable) {
			//eat it.
		}
		return false;
	} 
	public String getResolutionAction() {
		return resolutionAction;
	}
	public void setResolutionAction(String resolutionAction) {
		this.resolutionAction = resolutionAction;
	}
}
