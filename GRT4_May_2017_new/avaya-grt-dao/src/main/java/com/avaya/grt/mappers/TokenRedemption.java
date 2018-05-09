package com.avaya.grt.mappers;

import java.io.Serializable;
import java.util.Date;

public class TokenRedemption implements Serializable{
	private static final long serialVersionUID = 1L;
	private String tokenRedemptionId;
	private String tokenNumber;
	private Date redemptionDate;
	private String redeemerBPLinkId;
	private String redeemerName;
	private String poNumber;
	private String offerName;
	private String distiSoldToId;
	private String distiBPLinkId;
	private String medalLevel;
	private String requesterSalesOrg;
	private String requesterDistributionChannel;
	private String requesterDivision;
	private String payer;
	private String serviceTerm;
	private String serviceType;
	private String salesOrderNo;
	private String sourceSystem;
	private String sqrn;
	private String status;
	private String endCustomerShipTo;
	private String endCustomerEmail;
	private String endCustomerSalesOrg;
	private String distributorAccount;
	private String activationEmail;
	private String codelPartnerAccountNo;
	private String dealerPartnerAccountNo;
	private boolean governmentUsage;
	private String country;
	private String region;
	private String state;
	private String stateFull;
	private String street;
	private String zip;
	private String responseCode;
	private String responseDescription;
	private String contractNumber;
	private Date contractStartDate;
	private boolean replaceExistingContract;
	private String selectedRegion;
	private String additionalInformation;
	private String cancelledContractNumber;
	private Date cancelledContractStartDate;
	private Date cancelledContractEndDate;
	private java.util.Set<LogServiceDetails> serviceDetails;
	public String getActivationEmail() {
		return activationEmail;
	}
	public void setActivationEmail(String activationEmail) {
		this.activationEmail = activationEmail;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getCodelPartnerAccountNo() {
		return codelPartnerAccountNo;
	}
	public void setCodelPartnerAccountNo(String codelPartnerAccountNo) {
		this.codelPartnerAccountNo = codelPartnerAccountNo;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Date getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDealerPartnerAccountNo() {
		return dealerPartnerAccountNo;
	}
	public void setDealerPartnerAccountNo(String dealerPartnerAccountNo) {
		this.dealerPartnerAccountNo = dealerPartnerAccountNo;
	}
	public String getDistiBPLinkId() {
		return distiBPLinkId;
	}
	public void setDistiBPLinkId(String distiBPLinkId) {
		this.distiBPLinkId = distiBPLinkId;
	}
	public String getDistiSoldToId() {
		return distiSoldToId;
	}
	public void setDistiSoldToId(String distiSoldToId) {
		this.distiSoldToId = distiSoldToId;
	}
	public String getDistributorAccount() {
		return distributorAccount;
	}
	public void setDistributorAccount(String distributorAccount) {
		this.distributorAccount = distributorAccount;
	}
	public String getEndCustomerEmail() {
		return endCustomerEmail;
	}
	public void setEndCustomerEmail(String endCustomerEmail) {
		this.endCustomerEmail = endCustomerEmail;
	}
	public String getEndCustomerSalesOrg() {
		return endCustomerSalesOrg;
	}
	public void setEndCustomerSalesOrg(String endCustomerSalesOrg) {
		this.endCustomerSalesOrg = endCustomerSalesOrg;
	}
	public String getEndCustomerShipTo() {
		return endCustomerShipTo;
	}
	public void setEndCustomerShipTo(String endCustomerShipTo) {
		this.endCustomerShipTo = endCustomerShipTo;
	}
	public boolean isGovernmentUsage() {
		return governmentUsage;
	}
	public void setGovernmentUsage(boolean governmentUsage) {
		this.governmentUsage = governmentUsage;
	}
	public String getMedalLevel() {
		return medalLevel;
	}
	public void setMedalLevel(String medalLevel) {
		this.medalLevel = medalLevel;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getRedeemerBPLinkId() {
		return redeemerBPLinkId;
	}
	public void setRedeemerBPLinkId(String redeemerBPLinkId) {
		this.redeemerBPLinkId = redeemerBPLinkId;
	}
	public String getRedeemerName() {
		return redeemerName;
	}
	public void setRedeemerName(String redeemerName) {
		this.redeemerName = redeemerName;
	}
	public Date getRedemptionDate() {
		return redemptionDate;
	}
	public void setRedemptionDate(Date redemptionDate) {
		this.redemptionDate = redemptionDate;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public boolean isReplaceExistingContract() {
		return replaceExistingContract;
	}
	public void setReplaceExistingContract(boolean replaceExistingContract) {
		this.replaceExistingContract = replaceExistingContract;
	}
	public String getRequesterDistributionChannel() {
		return requesterDistributionChannel;
	}
	public void setRequesterDistributionChannel(String requesterDistributionChannel) {
		this.requesterDistributionChannel = requesterDistributionChannel;
	}
	public String getRequesterDivision() {
		return requesterDivision;
	}
	public void setRequesterDivision(String requesterDivision) {
		this.requesterDivision = requesterDivision;
	}
	public String getRequesterSalesOrg() {
		return requesterSalesOrg;
	}
	public void setRequesterSalesOrg(String requesterSalesOrg) {
		this.requesterSalesOrg = requesterSalesOrg;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getSalesOrderNo() {
		return salesOrderNo;
	}
	public void setSalesOrderNo(String salesOrderNo) {
		this.salesOrderNo = salesOrderNo;
	}
	public String getServiceTerm() {
		return serviceTerm;
	}
	public void setServiceTerm(String serviceTerm) {
		this.serviceTerm = serviceTerm;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getSqrn() {
		return sqrn;
	}
	public void setSqrn(String sqrn) {
		this.sqrn = sqrn;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getTokenNumber() {
		return tokenNumber;
	}
	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}
	public String getTokenRedemptionId() {
		return tokenRedemptionId;
	}
	public void setTokenRedemptionId(String tokenRedemptionId) {
		this.tokenRedemptionId = tokenRedemptionId;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getSelectedRegion() {
		return selectedRegion;
	}
	public void setSelectedRegion(String selectedRegion) {
		this.selectedRegion = selectedRegion;
	}
	public String getStateFull() {
		return stateFull;
	}
	public void setStateFull(String stateFull) {
		this.stateFull = stateFull;
	}
	public java.util.Set<LogServiceDetails> getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(java.util.Set<LogServiceDetails> serviceDetails) {
		this.serviceDetails = serviceDetails;
	} 
	public Date getCancelledContractEndDate() {
		return cancelledContractEndDate;
	}
	public void setCancelledContractEndDate(Date cancelledContractEndDate) {
		this.cancelledContractEndDate = cancelledContractEndDate;
	}
	public String getCancelledContractNumber() {
		return cancelledContractNumber;
	}
	public void setCancelledContractNumber(String cancelledContractNumber) {
		this.cancelledContractNumber = cancelledContractNumber;
	}
	public Date getCancelledContractStartDate() {
		return cancelledContractStartDate;
	}
	public void setCancelledContractStartDate(Date cancelledContractStartDate) {
		this.cancelledContractStartDate = cancelledContractStartDate;
	}
}

