package com.grt.dto;

import java.io.Serializable;
import java.util.Date;

public class Contract implements Serializable,Comparable<Contract>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1323232L;

	private String endCustomerShipTo,
	               contractNumber, 
	               contractValue,
	               contractType,
	               returnCode;
	private Date contractStartDate,contractEndDate;
	private String contractStartDateStr, contractEndDateStr;

	private boolean isCodelivary;
	
	private boolean isWholeSale;
	private boolean isDirect;
	private boolean isRetail;
	
	private boolean allowReplace;
	
	public boolean isAllowReplace() {
		return allowReplace;
	}

	public void setAllowReplace(boolean allowReplace) {
		this.allowReplace = allowReplace;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
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

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractValue() {
		return contractValue;
	}

	public void setContractValue(String contractValue) {
		this.contractValue = contractValue;
	}

	public String getEndCustomerShipTo() {
		return endCustomerShipTo;
	}

	public void setEndCustomerShipTo(String endCustomerShipTo) {
		this.endCustomerShipTo = endCustomerShipTo;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public Contract() {
		
	}
	public Contract(String contractNumber, Date contractStartDate,
			Date contractEndDate, String contractValue, String contractType) {
		super();
		this.contractNumber = contractNumber;
		this.contractStartDate = contractStartDate;
		this.contractEndDate = contractEndDate;
		this.contractValue = contractValue;
		this.contractType = contractType;
	}

	public boolean isCodelivary() {
		return isCodelivary;
	}

	public void setCodelivary(boolean isCodelivary) {
		this.isCodelivary = isCodelivary;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	public boolean isRetail() {
		return isRetail;
	}

	public void setRetail(boolean isRetail) {
		this.isRetail = isRetail;
	}

	public boolean isWholeSale() {
		return isWholeSale;
	}

	public void setWholeSale(boolean isWholeSale) {
		this.isWholeSale = isWholeSale;
	}

    public int compareTo(Contract o) {		  		
		return (o!=null && o.getContractEndDate()!=null ) ?o.getContractEndDate().compareTo(getContractEndDate()):0;
	}

	public String getContractEndDateStr() {
		return contractEndDateStr;
	}

	public void setContractEndDateStr(String contractEndDateStr) {
		this.contractEndDateStr = contractEndDateStr;
	}

	public String getContractStartDateStr() {
		return contractStartDateStr;
	}

	public void setContractStartDateStr(String contractStartDateStr) {
		this.contractStartDateStr = contractStartDateStr;
	}
	
	
	
}
