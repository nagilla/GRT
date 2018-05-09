package com.avaya.grt.web.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class CSSPortalUser implements Serializable{
	private String firstName;
	private String lastName;
	private String middleName;
	private String phoneNumber;
	private String faxNumber;
	private String emailAddress;
	private long lastUpdateTime;
	private String streetAddress1;
	private String streetAddress2;
	private String streetAddress3;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	private String userType;
	private String company;
	private String bpLinkId;
	private String userId = null;
	private boolean admin = false;
	private boolean superUser = false;
	
	/*Oceana Chat Phase 1*/
	private String customerNumber = null;
	private String linkId = null;
	
	private Map<String,String> chatConfigurations;
	
	public CSSPortalUser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor for testing purpose.
	 * 
	 * @param userId
	 * @param firstName
	 * @param lastName
	 * @param company
	 * @param userType
	 * @param bpLinkId
	 */
	public CSSPortalUser(String userId, String firstName, String lastName, String company,
			String userType, String bpLinkId, String emailAddress) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.userType = userType;
		this.bpLinkId = bpLinkId;
		this.emailAddress = emailAddress;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStreetAddress1() {
		return streetAddress1;
	}
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	public String getStreetAddress2() {
		return streetAddress2;
	}
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	public String getStreetAddress3() {
		return streetAddress3;
	}
	public void setStreetAddress3(String streetAddress3) {
		this.streetAddress3 = streetAddress3;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public boolean getAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBpLinkId() {
		return bpLinkId;
	}
	public void setBpLinkId(String bpLinkId) {
		this.bpLinkId = bpLinkId;
	}

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}
	
	@Override
	public String toString() {
		return "User Id : " + this.userId + " - " + "User Type : " + this.userType +
				" - " + "First Name : " + this.firstName + " - " + "Last Name : " + this.lastName + "BpLinkID: "+this.bpLinkId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Map<String,String> getChatConfigurations() {
		return chatConfigurations;
	}

	public void setChatConfigurations(Map<String,String> chatConfigurations) {
		this.chatConfigurations = chatConfigurations;
	}
}
