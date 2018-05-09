package com.grt.dto;
import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;

/**
 * The dto class for the PURCHASE_ORDER 
 * 
 * @author perficient
 */
public class PurchaseOrder  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String addressLine1;
	private String addressLine2;
	private String billName;
	private String billPlayerNo;
	private String city;
	private String contactName;
	private String contactPhone;
	private String phone; // Main Telephone
	private String purchaseOrderNo;
	private String registrationId;
	private String state;
	private String zip;
	private String bpsoldtoNo;
	private SortedMap<String,String> stateList;


    public String getBpsoldtoNo() {
		return bpsoldtoNo;
	}

	public void setBpsoldtoNo(String bpsoldtoNo) {
		this.bpsoldtoNo = bpsoldtoNo;
	}

	public PurchaseOrder() {
    }

	public String getOrderId() {
		return this.orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAddressLine1() {
		return this.addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getBillName() {
		return this.billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillPlayerNo() {
		return this.billPlayerNo;
	}
	public void setBillPlayerNo(String billPlayerNo) {
		this.billPlayerNo = billPlayerNo;
	}

	public String getCity() {
		return this.city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getContactName() {
		return this.contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPurchaseOrderNo() {
		return this.purchaseOrderNo;
	}
	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public String getRegistrationId() {
		return this.registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getState() {
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public SortedMap<String, String> getStateList() {
		return stateList;
	}

	public void setStateList(SortedMap<String, String> stateList) {
		this.stateList = stateList;
	}




}