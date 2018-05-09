package com.grt.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.avaya.grt.mappers.Region;
import com.grt.dto.Result;
import com.grt.dto.Account;
import com.grt.util.GRTConstants;

/**
 * Form bean for the Account creation.
 * 
 * 
 */

public class AccountFormBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String locale;

	private String country;

	private String countryValue;

	private String stateValue;

	private String salesOrg;

	private String bplinkId;

	private String bpName;

	private String redeem;

	private String tokenNumber;

	private String verifyTokenNumber;

	private String soldToName;

	private String soldToId;

	private String shipToId;

	private String sapBox;

	private String endCustomerName;

	private String endCustomerStreetName;

	private String endCustomerCity;

	private String endCustomerProvince;

	private String endCustomerOtherProvince;

	private String endCustomerPostalCode;

	private String endCustomerState;

	private String endCustomerCountry;

	private String country3Char;

	private String endCustomerEmail;

	private String endCustomerPhone;

	private String contactFirstName;

	private String contactLastName;

	private String contactEmail;

	private String contactPhone;

	private List<Result> searchResult;

	private String searchDataFlag;

	private String tokenNumberSubmissionFlag;

	private String soldToIdSubmissionFlag;

	private String govtAccountFlag;

	private String tokenNumberFlag;

	private String acountCreationFlag;

	private String shipToCreationFlagForBP;

	private String shipToCreationFlagForNonBP;

	private String accountAddressFlag;

	private String fuzzySearchFlag;

	private String shipTosoldToFlag;

	private String confirmationFlag;

	private String selectedAccountNumber;

	private String vatNumber;

	private String usGovernmentAcount;

	private String responseCode;

	private String responseDescription;

	private String responseAccountId;

	private String responseUniqueId;

	private String accountIdForUpdate;

	private SortedMap<String, String> countryList;

	private SortedMap<String, String> stateList;

	private SortedMap<String, String> provinceList;

	private String EUFlag;

	private String searchFlag = "none";

	private String contactsFlag = "none";

	private String selectedSAPId;

	private Account originalAccount, updatedAccount;

	private String contactFax;

	private boolean bplinkidRequired;

	private String selectedAccount;

	private String bplinkdetailsFlag;

	private String tokenDetailsFlag;

	private List<String> cxpSoldToList;

	private Map<Long, Region> currentCountryRegion;

	private Map<String, Map<Long, Region>> regionMap;

	private long selectedRegionId;

	private String siebelRegionValue;

	private String regionValue;

	private String regionId;

	private Region selectedRegion;

	private String distributionChannel;

	private String division;

	private String userType;

	private String accountCreationReason;

	private String emailNotification;

	private String emailNotificationFlag;

	private String emailNotificationStatus;
	private String serviceTerm;

	public String getEmailNotificationStatus() {
		return emailNotificationStatus;
	}

	public void setEmailNotificationStatus(String emailNotificationStatus) {
		this.emailNotificationStatus = emailNotificationStatus;
	}

	public String getEmailNotificationFlag() {
		return emailNotificationFlag;
	}

	public void setEmailNotificationFlag(String emailNotificationFlag) {
		this.emailNotificationFlag = emailNotificationFlag;
	}

	public String getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(String emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public long getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(long selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
	}

	public Map<Long, Region> getCurrentCountryRegion() {
		return currentCountryRegion;
	}

	public void setCurrentCountryRegion(Map<Long, Region> currentCountryRegion) {
		this.currentCountryRegion = currentCountryRegion;
	}

	public Map<String, Map<Long, Region>> getRegionMap() {
		return regionMap;
	}

	public void setRegionMap(Map<String, Map<Long, Region>> regionMap) {
		this.regionMap = regionMap;
	}

	public String getCountry3Char() {
		return country3Char;
	}

	public void setCountry3Char(String country3Char) {
		this.country3Char = country3Char;
	}

	public String getResponseUniqueId() {
		return responseUniqueId;
	}

	public void setResponseUniqueId(String responseUniqueId) {
		this.responseUniqueId = responseUniqueId;
	}

	public String getBplinkdetailsFlag() {
		return bplinkdetailsFlag;
	}

	public void setBplinkdetailsFlag(String bplinkdetailsFlag) {
		this.bplinkdetailsFlag = bplinkdetailsFlag;
	}

	public boolean isBplinkidRequired() {
		return bplinkidRequired;
	}

	public void setBplinkidRequired(boolean bplinkidRequired) {
		this.bplinkidRequired = bplinkidRequired;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(String selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public String getTokenDetailsFlag() {
		return tokenDetailsFlag;
	}

	public void setTokenDetailsFlag(String tokenDetailsFlag) {
		this.tokenDetailsFlag = tokenDetailsFlag;
	}

	public String getSoldToIdSubmissionFlag() {
		return soldToIdSubmissionFlag;
	}

	public void setSoldToIdSubmissionFlag(String soldToIdSubmissionFlag) {
		this.soldToIdSubmissionFlag = soldToIdSubmissionFlag;
	}

	public String getResponseAccountId() {
		return responseAccountId;
	}

	public void setResponseAccountId(String responseAccountId) {
		this.responseAccountId = responseAccountId;
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

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getConfirmationFlag() {
		return confirmationFlag;
	}

	public void setConfirmationFlag(String confirmationFlag) {
		this.confirmationFlag = confirmationFlag;
	}

	public String getEUFlag() {
		return EUFlag;
	}

	public void setEUFlag(String flag) {
		EUFlag = flag;
	}

	public String getAccountAddressFlag() {
		return accountAddressFlag;
	}

	public void setAccountAddressFlag(String accountAddressFlag) {
		this.accountAddressFlag = accountAddressFlag;
	}

	public String getEndCustomerCity() {
		return endCustomerCity;
	}

	public void setEndCustomerCity(String endCustomerCity) {
		this.endCustomerCity = endCustomerCity;
	}

	public String getEndCustomerCountry() {
		return endCustomerCountry;
	}

	public void setEndCustomerCountry(String endCustomerCountry) {
		this.endCustomerCountry = endCustomerCountry;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public String getEndCustomerPostalCode() {
		return endCustomerPostalCode;
	}

	public void setEndCustomerPostalCode(String endCustomerPostalCode) {
		this.endCustomerPostalCode = endCustomerPostalCode;
	}

	public String getEndCustomerState() {
		return endCustomerState;
	}

	public void setEndCustomerState(String endCustomerState) {
		this.endCustomerState = endCustomerState;
	}

	public String getEndCustomerStreetName() {
		return endCustomerStreetName;
	}

	public void setEndCustomerStreetName(String endCustomerStreetName) {
		this.endCustomerStreetName = endCustomerStreetName;
	}

	public String getSapBox() {
		return sapBox;
	}

	public void setSapBox(String sapBox) {
		this.sapBox = sapBox;
	}

	public String getSoldToId() {
		return soldToId;
	}

	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public String getShipToId() {
		return shipToId;
	}

	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}

	public String getSoldToName() {
		return soldToName;
	}

	public void setSoldToName(String soldToName) {
		this.soldToName = soldToName;
	}

	public String getVerifyTokenNumber() {
		return verifyTokenNumber;
	}

	public void setVerifyTokenNumber(String verifyTokenNumber) {
		this.verifyTokenNumber = verifyTokenNumber;
	}

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getRedeem() {
		return GRTConstants.YES;
		// return redeem;
	}

	public void setRedeem(String redeem) {
		this.redeem = redeem;
	}

	public String getAcountCreationFlag() {
		return acountCreationFlag;
	}

	public void setAcountCreationFlag(String acountCreationFlag) {
		this.acountCreationFlag = acountCreationFlag;
	}

	public String getShipToCreationFlagForBP() {
		return shipToCreationFlagForBP;
	}

	public void setShipToCreationFlagForBP(String shipToCreationFlag) {
		this.shipToCreationFlagForBP = shipToCreationFlag;
	}

	public String getTokenNumberFlag() {
		return tokenNumberFlag;
	}

	public void setTokenNumberFlag(String tokenNumberFlag) {
		this.tokenNumberFlag = tokenNumberFlag;
	}

	public String getGovtAccountFlag() {
		return govtAccountFlag;
	}

	public void setGovtAccountFlag(String govtAccountFlag) {
		this.govtAccountFlag = govtAccountFlag;
	}

	public String getTokenNumberSubmissionFlag() {
		return tokenNumberSubmissionFlag;
	}

	public void setTokenNumberSubmissionFlag(String tokenNumberSubmissionFlag) {
		this.tokenNumberSubmissionFlag = tokenNumberSubmissionFlag;
	}

	public SortedMap<String, String> getCountryList() {
		return countryList;
	}

	public void setCountryList(SortedMap<String, String> countryList) {
		this.countryList = countryList;
	}

	public SortedMap<String, String> getStateList() {
		return stateList;
	}

	public void setStateList(SortedMap<String, String> stateList) {
		this.stateList = stateList;
	}

	public SortedMap<String, String> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(SortedMap<String, String> provinceList) {
		this.provinceList = provinceList;
	}

	public String getBplinkId() {
		return bplinkId;
	}

	public void setBplinkId(String bplinkId) {
		this.bplinkId = bplinkId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFuzzySearchFlag() {
		return fuzzySearchFlag;
	}

	public void setFuzzySearchFlag(String fuzzySearchFlag) {
		this.fuzzySearchFlag = fuzzySearchFlag;
	}

	public List<Result> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(List<Result> searchResult) {
		this.searchResult = searchResult;
	}

	public String getSelectedAccountNumber() {
		return selectedAccountNumber;
	}

	public void setSelectedAccountNumber(String selectedAccountNumber) {
		this.selectedAccountNumber = selectedAccountNumber;
	}

	public String getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(String countryValue) {
		this.countryValue = countryValue;
	}

	public String getStateValue() {
		return stateValue;
	}

	public void setStateValue(String stateValue) {
		this.stateValue = stateValue;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getEndCustomerEmail() {
		return endCustomerEmail;
	}

	public void setEndCustomerEmail(String endCustomerEmail) {
		this.endCustomerEmail = endCustomerEmail;
	}

	public String getEndCustomerPhone() {
		return endCustomerPhone;
	}

	public void setEndCustomerPhone(String endCustomerPhone) {
		this.endCustomerPhone = endCustomerPhone;
	}

	public String getUsGovernmentAcount() {
		return usGovernmentAcount;
	}

	public void setUsGovernmentAcount(String usGovernmentAcount) {
		this.usGovernmentAcount = usGovernmentAcount;
	}

	public String getShipTosoldToFlag() {
		return shipTosoldToFlag;
	}

	public void setShipTosoldToFlag(String shipTosoldToFlag) {
		this.shipTosoldToFlag = shipTosoldToFlag;
	}

	public String getEndCustomerProvince() {
		return endCustomerProvince;
	}

	public void setEndCustomerProvince(String endCustomerProvince) {
		this.endCustomerProvince = endCustomerProvince;
	}

	public String getEndCustomerOtherProvince() {
		return endCustomerOtherProvince;
	}

	public void setEndCustomerOtherProvince(String endCustomerOtherProvince) {
		this.endCustomerOtherProvince = endCustomerOtherProvince;
	}

	public String getShipToCreationFlagForNonBP() {
		return shipToCreationFlagForNonBP;
	}

	public void setShipToCreationFlagForNonBP(String shipToCreationFlagForNonBP) {
		this.shipToCreationFlagForNonBP = shipToCreationFlagForNonBP;
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

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Account getOriginalAccount() {
		return originalAccount;
	}

	public void setOriginalAccount(Account originalAccount) {
		this.originalAccount = originalAccount;
	}

	public Account getUpdatedAccount() {
		return updatedAccount;
	}

	public void setUpdatedAccount(Account updatedAccount) {
		this.updatedAccount = updatedAccount;
	}

	public String getAccountIdForUpdate() {
		return accountIdForUpdate;
	}

	public void setAccountIdForUpdate(String accountIdForUpdate) {
		this.accountIdForUpdate = accountIdForUpdate;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getSearchDataFlag() {
		return searchDataFlag;
	}

	public void setSearchDataFlag(String searchDataFlag) {
		this.searchDataFlag = searchDataFlag;
	}

	public String getContactsFlag() {
		return contactsFlag;
	}

	public void setContactsFlag(String contactsFlag) {
		this.contactsFlag = contactsFlag;
	}

	public String getSelectedSAPId() {
		return selectedSAPId;
	}

	public void setSelectedSAPId(String selectedSAPId) {
		this.selectedSAPId = selectedSAPId;
	}

	public List<String> getCxpSoldToList() {
		return cxpSoldToList;
	}

	public void setCxpSoldToList(List<String> cxpSoldToList) {
		this.cxpSoldToList = cxpSoldToList;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion = selectedRegion;
	}

	public String getSiebelRegionValue() {
		return siebelRegionValue;
	}

	public void setSiebelRegionValue(String siebelRegionValue) {
		this.siebelRegionValue = siebelRegionValue;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionValue() {
		return regionValue;
	}

	public void setRegionValue(String regionValue) {
		this.regionValue = regionValue;
	}

	public String getAccountCreationReason() {
		return accountCreationReason;
	}

	public void setAccountCreationReason(String accountCreationReason) {
		this.accountCreationReason = accountCreationReason;
	}

	public String getServiceTerm() {
		return serviceTerm;
	}

	public void setServiceTerm(String serviceTerm) {
		this.serviceTerm = serviceTerm;
	}
}
