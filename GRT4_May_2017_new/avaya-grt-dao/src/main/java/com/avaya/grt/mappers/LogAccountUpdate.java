package com.avaya.grt.mappers;

import java.io.Serializable;
import java.util.Date;

public class LogAccountUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	private String transactionId;
	private Date transactionDate;
	private String transactedBy;
	private String accountId;
	private String loggedInBPLinkId;
	private String oldAccountName;
	private String newAccountName;
	private String oldCustomerContactName;
	private String newCustomerContactName;
	private String oldCity;
	private String newCity;
	private String oldStateProvince;
	private String newStateProvince;
	private String oldCountryISOCode;
	private String newCountryISOCode;
	private String oldMainPhoneNumber;
	private String newMainPhoneNumber;
	private String oldPostalCode;
	private String newPostalCode;
	private String oldStreetAddressLine1;
	private String newStreetAddressLine1;
	private String oldStreetAddressLine2;
	private String newStreetAddressLine2;;
	private String describeDeliverables;
	private String status;
	private String errorMessage;
	private String ssrNumber;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getLoggedInBPLinkId() {
		return loggedInBPLinkId;
	}
	public void setLoggedInBPLinkId(String loggedInBPLinkId) {
		this.loggedInBPLinkId = loggedInBPLinkId;
	}
	public String getNewAccountName() {
		return newAccountName;
	}
	public void setNewAccountName(String newAccountName) {
		this.newAccountName = newAccountName;
	}
	public String getNewCity() {
		return newCity;
	}
	public void setNewCity(String newCity) {
		this.newCity = newCity;
	}
	public String getNewCountryISOCode() {
		return newCountryISOCode;
	}
	public void setNewCountryISOCode(String newCountryISOCode) {
		this.newCountryISOCode = newCountryISOCode;
	}
	public String getNewCustomerContactName() {
		return newCustomerContactName;
	}
	public void setNewCustomerContactName(String newCustomerContactName) {
		this.newCustomerContactName = newCustomerContactName;
	}
	public String getNewMainPhoneNumber() {
		return newMainPhoneNumber;
	}
	public void setNewMainPhoneNumber(String newMainPhoneNumber) {
		this.newMainPhoneNumber = newMainPhoneNumber;
	}
	public String getNewPostalCode() {
		return newPostalCode;
	}
	public void setNewPostalCode(String newPostalCode) {
		this.newPostalCode = newPostalCode;
	}
	public String getNewStateProvince() {
		return newStateProvince;
	}
	public void setNewStateProvince(String newStateProvince) {
		this.newStateProvince = newStateProvince;
	}
	public String getNewStreetAddressLine1() {
		return newStreetAddressLine1;
	}
	public void setNewStreetAddressLine1(String newStreetAddressLine1) {
		this.newStreetAddressLine1 = newStreetAddressLine1;
	}
	public String getNewStreetAddressLine2() {
		return newStreetAddressLine2;
	}
	public void setNewStreetAddressLine2(String newStreetAddressLine2) {
		this.newStreetAddressLine2 = newStreetAddressLine2;
	}
	public String getOldAccountName() {
		return oldAccountName;
	}
	public void setOldAccountName(String oldAccountName) {
		this.oldAccountName = oldAccountName;
	}
	public String getOldCity() {
		return oldCity;
	}
	public void setOldCity(String oldCity) {
		this.oldCity = oldCity;
	}
	public String getOldCountryISOCode() {
		return oldCountryISOCode;
	}
	public void setOldCountryISOCode(String oldCountryISOCode) {
		this.oldCountryISOCode = oldCountryISOCode;
	}
	public String getOldCustomerContactName() {
		return oldCustomerContactName;
	}
	public void setOldCustomerContactName(String oldCustomerContactName) {
		this.oldCustomerContactName = oldCustomerContactName;
	}
	public String getDescribeDeliverables() {
		return describeDeliverables;
	}
	public void setDescribeDeliverables(String describeDeliverables) {
		this.describeDeliverables = describeDeliverables;
	}
	public String getOldMainPhoneNumber() {
		return oldMainPhoneNumber;
	}
	public void setOldMainPhoneNumber(String oldMainPhoneNumber) {
		this.oldMainPhoneNumber = oldMainPhoneNumber;
	}
	public String getOldPostalCode() {
		return oldPostalCode;
	}
	public void setOldPostalCode(String oldPostalCode) {
		this.oldPostalCode = oldPostalCode;
	}
	public String getOldStateProvince() {
		return oldStateProvince;
	}
	public void setOldStateProvince(String oldStateProvince) {
		this.oldStateProvince = oldStateProvince;
	}
	public String getOldStreetAddressLine1() {
		return oldStreetAddressLine1;
	}
	public void setOldStreetAddressLine1(String oldStreetAddressLine1) {
		this.oldStreetAddressLine1 = oldStreetAddressLine1;
	}
	public String getOldStreetAddressLine2() {
		return oldStreetAddressLine2;
	}
	public void setOldStreetAddressLine2(String oldStreetAddressLine2) {
		this.oldStreetAddressLine2 = oldStreetAddressLine2;
	}
	public String getSsrNumber() {
		return ssrNumber;
	}
	public void setSsrNumber(String ssrNumber) {
		this.ssrNumber = ssrNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactedBy() {
		return transactedBy;
	}
	public void setTransactedBy(String transactedBy) {
		this.transactedBy = transactedBy;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
