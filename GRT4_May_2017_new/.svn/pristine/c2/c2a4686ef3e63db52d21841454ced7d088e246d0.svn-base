package com.grt.dto;

import com.grt.util.MedalLevelEnum;

//changes for Gift card 2.0
import static com.grt.util.GRTConstants.TOKEN_CODELIVERY;
import static com.grt.util.GRTConstants.TOKEN_CODELIVERY_SAP;


/**
 * Generated from schema type
 * t=TokenResponseType@http://avaya.com/v1/tokenschemas
 */

public class TokenResponseTypeDto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String code;

	private java.math.BigInteger transactionId;

	private java.lang.String description;

	private java.lang.String sourceSystem;

	private java.lang.String userName;

	private java.lang.String offerName;

	private java.lang.String distiBPLinkId;

	private java.lang.String distiBPName;

	private java.lang.String pONumber;

	private java.lang.String requesterSalesOrg;

	private java.lang.String requesterDistributionChannel;

	private java.lang.String requesterDivision;

	private java.lang.String distiSoldTo;

	private java.lang.String payer;

	private java.lang.String serviceType;

	private java.lang.String serviceTerm;

	private java.lang.String salesOrderNo;

	private java.lang.String sQRN;

	private java.lang.String maintenancePartnerBPLinkId;

	private java.lang.String email;

	private java.lang.String distiBPEmail;

	private byte[] pOFile;

	private String status;
	
	private TokenDetails [] tokendata; 
    
    private MedalLevelEnum medaLevel;

	public MedalLevelEnum getMedaLevel()
    {
        return medaLevel;
    }

    public void setMedaLevel(MedalLevelEnum medaLevel)
    {
        this.medaLevel = medaLevel;
    }

    public TokenDetails[] getTokendata() {
		return tokendata;
	}

	public void setTokendata(TokenDetails[] tokendata) {
		this.tokendata = tokendata;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getDistiBPEmail() {
		return distiBPEmail;
	}

	public void setDistiBPEmail(java.lang.String distiBPEmail) {
		this.distiBPEmail = distiBPEmail;
	}

	public java.lang.String getDistiBPLinkId() {
		return distiBPLinkId;
	}

	public void setDistiBPLinkId(java.lang.String distiBPLinkId) {
		this.distiBPLinkId = distiBPLinkId;
	}

	public java.lang.String getDistiBPName() {
		return distiBPName;
	}

	public void setDistiBPName(java.lang.String distiBPName) {
		this.distiBPName = distiBPName;
	}

	public java.lang.String getDistiSoldTo() {
		return distiSoldTo;
	}

	public void setDistiSoldTo(java.lang.String distiSoldTo) {
		this.distiSoldTo = distiSoldTo;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getMaintenancePartnerBPLinkId() {
		return maintenancePartnerBPLinkId;
	}

	public void setMaintenancePartnerBPLinkId(
			java.lang.String maintenancePartnerBPLinkId) {
		this.maintenancePartnerBPLinkId = maintenancePartnerBPLinkId;
	}

	public java.lang.String getOfferName() {
		return offerName;
	}

	public void setOfferName(java.lang.String offerName) {
		this.offerName = offerName;
	}

	public java.lang.String getPayer() {
		return payer;
	}

	public void setPayer(java.lang.String payer) {
		this.payer = payer;
	}

	public byte[] getPOFile() {
		return pOFile;
	}

	public void setPOFile(byte[] file) {
		pOFile = file;
	}

	public java.lang.String getPONumber() {
		return pONumber;
	}

	public void setPONumber(java.lang.String number) {
		pONumber = number;
	}

	public java.lang.String getRequesterDistributionChannel() {
		return requesterDistributionChannel;
	}

	public void setRequesterDistributionChannel(
			java.lang.String requesterDistributionChannel) {
		this.requesterDistributionChannel = requesterDistributionChannel;
	}

	public java.lang.String getRequesterDivision() {
		return requesterDivision;
	}

	public void setRequesterDivision(java.lang.String requesterDivision) {
		this.requesterDivision = requesterDivision;
	}

	public java.lang.String getRequesterSalesOrg() {
		return requesterSalesOrg;
	}

	public void setRequesterSalesOrg(java.lang.String requesterSalesOrg) {
		this.requesterSalesOrg = requesterSalesOrg;
	}

	public java.lang.String getSalesOrderNo() {
		return salesOrderNo;
	}

	public void setSalesOrderNo(java.lang.String salesOrderNo) {
		this.salesOrderNo = salesOrderNo;
	}

	public java.lang.String getServiceTerm() {
		return serviceTerm;
	}

	public void setServiceTerm(java.lang.String serviceTerm) {
		this.serviceTerm = serviceTerm;
	}

	public java.lang.String getServiceType() {
		return serviceType;
	}

	public void setServiceType(java.lang.String serviceType) {
		this.serviceType = serviceType;
	}

	public java.lang.String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(java.lang.String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public java.lang.String getSQRN() {
		return sQRN;
	}

	public void setSQRN(java.lang.String sqrn) {
		sQRN = sqrn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.math.BigInteger getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(java.math.BigInteger transactionId) {
		this.transactionId = transactionId;
	}

	public java.lang.String getUserName() {
		return userName;
	}

	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	
	public boolean isCodelivary() {
		return serviceType.toUpperCase().contains(TOKEN_CODELIVERY) || serviceType.toUpperCase().contains(TOKEN_CODELIVERY_SAP);
	}
	
	public boolean isWholesale() {
		return !(isCodelivary());
	}
	/*
	 * private com.avaya.v1.tokenschemas.TokenDetailsType[] tokenData;
	 * 
	 * public com.avaya.v1.tokenschemas.TokenDetailsType[] getTokenData() {
	 * return this.tokenData; }
	 * 
	 * public void setTokenData(com.avaya.v1.tokenschemas.TokenDetailsType[]
	 * tokenData) { this.tokenData = tokenData; }
	 */

}
