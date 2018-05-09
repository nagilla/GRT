package com.grt.dto;

import java.io.Serializable;

public class SALGatewayInstallerDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String userId;
	private String soldToId;
	private String materialCode;
	private String seCode;
	private String serialNumber;
	private String releaseNumber;
	private String emailID;
	private String aorig;
	private String productIdentifier;
	private String groupId;
	private	String productType;
	private String grtId;
	private String nickName;
	
	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}
	/**
	 * @param emailID the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	/**
	 * @return the materialCode
	 */
	public String getMaterialCode() {
		return materialCode;
	}
	/**
	 * @param materialCode the materialCode to set
	 */
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	/**
	 * @return the releaseNumber
	 */
	public String getReleaseNumber() {
		return releaseNumber;
	}
	/**
	 * @param releaseNumber the releaseNumber to set
	 */
	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}
	/**
	 * @return the seCode
	 */
	public String getSeCode() {
		return seCode;
	}
	/**
	 * @param seCode the seCode to set
	 */
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the soldToId
	 */
	public String getSoldToId() {
		return soldToId;
	}
	/**
	 * @param soldToId the soldToId to set
	 */
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAorig() {
		return aorig;
	}
	public void setAorig(String aorig) {
		this.aorig = aorig;
	}
	public String getProductIdentifier() {
		return productIdentifier;
	}
	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getGrtId() {
		return grtId;
	}
	public void setGrtId(String grtId) {
		this.grtId = grtId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
