package com.grt.dto;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String materialCode;
	private String shortDescription;
	private int quantity = 0;
	private String createdDate;
	private int registeredQuantity = 0;
	private int remainingQuantity = 0;
	private String solutionElement;
	private String seId;
	private String productLine;
	private String equipmentNumber;
	private String sId;
	private String mId;
	private String salSeIdPrimarySecondary;
	private String tempEquipmentNumber;
	private String groupId;
	
	//GRT 4.0 Change
	private String alarmId;
	
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getRegisteredQuantity() {
		return registeredQuantity;
	}
	public void setRegisteredQuantity(int registeredQuantity) {
		this.registeredQuantity = registeredQuantity;
	}
	public int getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	public String getSolutionElement() {
		return solutionElement;
	}
	public void setSolutionElement(String solutionElement) {
		this.solutionElement = solutionElement;
	}
	public String getSeId() {
		return seId;
	}
	public void setSeId(String seId) {
		this.seId = seId;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getEquipmentNumber() {
		return equipmentNumber;
	}
	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}
	public String getMId() {
		return mId;
	}
	public void setMId(String id) {
		mId = id;
	}
	public String getSId() {
		return sId;
	}
	public void setSId(String id) {
		sId = id;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Product) {
			Product that = (Product)obj;
			return (StringUtils.isNotEmpty(that.getMaterialCode()) 
						&& StringUtils.isNotEmpty(this.getMaterialCode()) 
							&& this.getMaterialCode().equals(that.getMaterialCode()));
		}
		return false;
	}
	public String getSalSeIdPrimarySecondary() {
		return salSeIdPrimarySecondary;
	}
	public void setSalSeIdPrimarySecondary(String salSeIdPrimarySecondary) {
		this.salSeIdPrimarySecondary = salSeIdPrimarySecondary;
	}
	/**
	 * @return the tempEquipmentNumber
	 */
	public String getTempEquipmentNumber() {
		return tempEquipmentNumber;
	}
	/**
	 * @param tempEquipmentNumber the tempEquipmentNumber to set
	 */
	public void setTempEquipmentNumber(String tempEquipmentNumber) {
		this.tempEquipmentNumber = tempEquipmentNumber;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	
}
