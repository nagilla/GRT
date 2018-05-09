package com.grt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accountId;
	private String soldToNumber;
	private String primaryAccountCountry;	
	private String primaryAccountCity;
	private String primaryAccountState;
	private String primaryAccountPostalCode;
	private String primaryAccountStreetAddress;	
	private String primaryAccountStreetAddress2;
	private String countryCode;
	private String phoneNumber;
	private String emailId;
	private String faxNumber;
	private String region;
	private String name;
	private List<Contact> contacts = new ArrayList<Contact>();
	private String type;
    private String contactName;
    private String errorCode;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPrimaryAccountCountry() {
		return primaryAccountCountry;
	}
	public void setPrimaryAccountCountry(String primaryAccountCountry) {
		this.primaryAccountCountry = primaryAccountCountry;
	}
	public String getSoldToNumber() {
		return soldToNumber;
	}
	public void setSoldToNumber(String soldToNumber) {
		this.soldToNumber = soldToNumber;
	}
	public String getPrimaryAccountCity() {
		return primaryAccountCity;
	}
	public void setPrimaryAccountCity(String primaryAccountCity) {
		this.primaryAccountCity = primaryAccountCity;
	}
	public String getPrimaryAccountPostalCode() {
		return primaryAccountPostalCode;
	}
	public void setPrimaryAccountPostalCode(String primaryAccountPostalCode) {
		this.primaryAccountPostalCode = primaryAccountPostalCode;
	}
	public String getPrimaryAccountState() {
		return primaryAccountState;
	}
	public void setPrimaryAccountState(String primaryAccountState) {
		this.primaryAccountState = primaryAccountState;
	}
	public String getPrimaryAccountStreetAddress() {
		return primaryAccountStreetAddress;
	}
	public void setPrimaryAccountStreetAddress(String primaryAccountStreetAddress) {
		this.primaryAccountStreetAddress = primaryAccountStreetAddress;
	}
	public String getPrimaryAccountStreetAddress2() {
		return primaryAccountStreetAddress2;
	}
	public void setPrimaryAccountStreetAddress2(String primaryAccountStreetAddress2) {
		this.primaryAccountStreetAddress2 = primaryAccountStreetAddress2;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public Account clone() {
		Account account = new Account();
		account.setAccountId(this.accountId);
		account.setName(this.name);
		account.setEmailId(this.emailId);
		account.setPhoneNumber(this.phoneNumber);
		account.setPrimaryAccountCity(this.primaryAccountCity);
		account.setPrimaryAccountCountry(this.primaryAccountCountry);
		account.setPrimaryAccountPostalCode(this.primaryAccountPostalCode);
		account.setPrimaryAccountState(this.primaryAccountState);
		account.setPrimaryAccountStreetAddress(this.primaryAccountStreetAddress);
		account.setPrimaryAccountStreetAddress2(this.primaryAccountStreetAddress2);
		account.setRegion(this.region);
		account.setSoldToNumber(this.soldToNumber);
		if(this.contacts != null && this.contacts.size() > 0) {
			List<Contact> contacts = new ArrayList<Contact>();
			for (Contact contact : this.contacts) {
				contacts.add(contact.clone());
			}
			account.setContacts(contacts);
		}
		return account;
	}
	
	public String getContactNameForDisplay() {
		if(this.contacts != null && this.contacts.size() > 0) {
			return contacts.get(0).getFirstName() + " " + contacts.get(0).getLastName();
		}
		return "";
	}
	
	public String getModificationSummary(Account account) {
		StringBuffer summary = new StringBuffer();
			
        
            if(account.getName() != null ) {
				if(!account.getName().equalsIgnoreCase(name)) {
					summary.append("* Modify Account Name from[" + this.name + "] to [" + account.getName() + "]");
				}
			}
            
			String originalAddress = this.getAddress();
			String modifiedAddress = account.getAddress();
            
			if(modifiedAddress != null) {
				if(!modifiedAddress.equalsIgnoreCase(originalAddress)) {
					summary.append("* Modify Address from[" + originalAddress + "] to [" + modifiedAddress + "]");
				}
			}
            
			if(account.getEmailId() != null ) {
				if(!account.getEmailId().equalsIgnoreCase(emailId)) {
					summary.append("* Modify Email from[" + this.emailId + "] to [" + account.getEmailId() + "]");
				}
			}
			if(account.getPhoneNumber() != null) {
				if(!account.getPhoneNumber().equalsIgnoreCase(phoneNumber)) {
					summary.append("* Modify Phone from[" + this.phoneNumber + "] to [" + account.getPhoneNumber() + "]");
				}
			}
			if(account.getFaxNumber() !=null) {
				if(!account.getFaxNumber().equalsIgnoreCase(faxNumber)) {
					summary.append("* Modify Fax from[" + this.faxNumber + "] to [" + account.getFaxNumber() + "]");
				}
			}
            
		return summary.toString();
	}
	
	private String getAddress() {
		StringBuffer address = new StringBuffer();
		
		address.append(this.primaryAccountStreetAddress);
		if(StringUtils.isNotEmpty(this.primaryAccountStreetAddress2)) {
			address.append(", " + this.primaryAccountStreetAddress2);
		}
		address.append(", " + this.primaryAccountCity);
		address.append(", " + this.primaryAccountState);
		address.append(", " + this.primaryAccountCountry);
		address.append(", " + this.primaryAccountPostalCode);
		return address.toString();
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	
	public String getCountryForDisplay() { 
		String countryDiaplay = this.primaryAccountCountry;
		/*if(StringUtils.isNotEmpty(this.countryCode)) { 
			countryDiaplay += " (" + this.countryCode + ")";
		}*/
		return countryDiaplay;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
    public String getContactName()
    {
        return contactName;
    }
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }
    
    public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
