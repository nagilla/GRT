/**
 * 
 */
package com.avaya.grt.mappers;

import java.io.Serializable;
import java.util.Date;


public class BPAccountTempAccess implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tempAccessId, bpLinkId, accountId, siebelId, accountName, createdBy;
	private Date createdDate, expiryDate;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getSiebelId() {
		return siebelId;
	}
	public void setSiebelId(String siebelId) {
		this.siebelId = siebelId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBpLinkId() {
		return bpLinkId;
	}
	public void setBpLinkId(String bpLinkId) {
		this.bpLinkId = bpLinkId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getTempAccessId() {
		return tempAccessId;
	}
	public void setTempAccessId(String tempAccessId) {
		this.tempAccessId = tempAccessId;
	}
	
	
}
