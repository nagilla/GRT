package com.grt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.grt.dto.Account;

public class SALGatewayIntrospection implements Serializable {

	private static final long serialVersionUID = 1L;
	private String gatewaySEID;
	private String productFamily = "SAL Gateway";
	private String status;
	private Date  registrationDate;
	private String strRegDate;
	private Date lastContact;
	private String strLastContact;
	private int pingRate;
	private String timeZone;
	private Account account;
	private String returnCode;
	private List<ManagedDevice> managedDevices = new ArrayList<ManagedDevice>();
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getGatewaySEID() {
		return gatewaySEID;
	}
	public void setGatewaySEID(String gatewaySEID) {
		this.gatewaySEID = gatewaySEID;
	}
	public Date getLastContact() {
		return lastContact;
	}
	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}
	public List<ManagedDevice> getManagedDevices() {
		return managedDevices;
	}
	public void setManagedDevices(List<ManagedDevice> managedDevices) {
		this.managedDevices = managedDevices;
	}
	public int getPingRate() {
		return pingRate;
	}
	public void setPingRate(int pingRate) {
		this.pingRate = pingRate;
	}
	public String getProductFamily() {
		return productFamily;
	}
	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the strLastContact
	 */
	public String getStrLastContact() {
		return strLastContact;
	}
	/**
	 * @param strLastContact the strLastContact to set
	 */
	public void setStrLastContact(String strLastContact) {
		this.strLastContact = strLastContact;
	}
	/**
	 * @return the strRegDate
	 */
	public String getStrRegDate() {
		return strRegDate;
	}
	/**
	 * @param strRegDate the strRegDate to set
	 */
	public void setStrRegDate(String strRegDate) {
		this.strRegDate = strRegDate;
	}
	
	
}
