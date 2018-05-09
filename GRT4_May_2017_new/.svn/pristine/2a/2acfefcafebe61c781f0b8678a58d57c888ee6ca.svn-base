package com.grt.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName, lastName, middleName, phone, fax, email;
	
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Contact clone() { 
		Contact contact = new Contact();
		contact.setFirstName(this.firstName);
		contact.setLastName(this.lastName);
		contact.setMiddleName(this.middleName);
		contact.setEmail(this.email);
		contact.setFax(this.fax);
		contact.setPhone(this.phone);
		return contact;
	}
	
	public String getModificationSummary(Contact contact) {
		StringBuffer summary = new StringBuffer("");
			String originalFullName = this.firstName + ", " + this.middleName + ", " + this.lastName;
			boolean nameChanged = false;
			if(!this.getFirstName().equalsIgnoreCase(contact.getFirstName()) || (StringUtils.isNotEmpty(this.getMiddleName()) && !this.getMiddleName().equalsIgnoreCase(contact.getMiddleName())) || !this.getLastName().equalsIgnoreCase(contact.getLastName())) {
                summary.append("\n Contact");
				summary.append("* Modify First, Middle, last Name from[" + originalFullName + "] to [" + contact.getFirstName() + ", " + contact.getMiddleName() + ", " + contact.getLastName() + "]");
				nameChanged = true;
			}
			if(contact.getPhone() != null) {
				if(!contact.getPhone().equalsIgnoreCase(phone)) {
                    if (summary.toString().length() == 0){
                        summary.append("\n Contact");}
					summary.append("* Modify Phone " + ((nameChanged)?"":" for contact " + originalFullName) + " from[" + this.phone + "] to [" + contact.getPhone() + "]");
				}
			}
			if(contact.getEmail() != null) {
				if(!contact.getEmail().equalsIgnoreCase(email)) {
                    if (summary.toString().length() == 0){
                        summary.append("\n Contact");}
					summary.append("* Modify Email " + ((nameChanged)?"":" for contact " + originalFullName) + " from[" + this.email + "] to [" + contact.getEmail() + "]");
				}
			}
			if(contact.getFax()!=null) {
				if(!contact.getFax().equalsIgnoreCase(fax)) {
                    if (summary.toString().length() == 0){
                        summary.append("\n Contact");}
					summary.append("* Modify Fax " + ((nameChanged)?"":" for contact " + originalFullName) + " from[" + this.fax + "] to [" + contact.getFax() + "]");
				}
			}
		return summary.toString();
	}
}
