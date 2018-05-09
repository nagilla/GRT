package com.grt.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class InstallBaseRespDataDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String soldToId;
	private String shipToId;
	private String materialCode;
	private String quantity;
	private String assetNumber;
	private String errorDesc;
	private String serialNumber;
	private BigDecimal beforeQuantity = new BigDecimal("0.0");
	private BigDecimal afterQuantity = new BigDecimal("0.0");
	public String getAssetNumber() {
		return assetNumber;
	}
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public String getSoldToId() {
		return soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	public BigDecimal getBeforeQuantity() {
		return beforeQuantity;
	}
	public void setBeforeQuantity(BigDecimal beforeQuantity) {
		this.beforeQuantity = beforeQuantity;
	}
	public BigDecimal getAfterQuantity() {
		return afterQuantity;
	}
	public void setAfterQuantity(BigDecimal afterQuantity) {
		this.afterQuantity = afterQuantity;
	}
	
	
}
