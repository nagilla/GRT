package com.grt.dto;

import java.io.Serializable;

/**
 * Method to get the LSP and MBT Details.
 * 
 * @author Perficient
 *
 */
public class LSPRegistrationDto implements Serializable{
	private String lspIndex;
	private String materialCodeId;
	private String lspId;
	private String soldToId;
	private String serverType;
	private String modem;
	private String alarmOrientation;;
	private String nickName;
	private String cmVersion;
	private String mid;
	private String gateway;
	private String seCode;
	
	public String getAlarmOrientation() {
		return alarmOrientation;
	}
	public void setAlarmOrientation(String alarmOrientation) {
		this.alarmOrientation = alarmOrientation;
	}
	public String getCmVersion() {
		return cmVersion;
	}
	public void setCmVersion(String cmVersion) {
		this.cmVersion = cmVersion;
	}
	public String getLspId() {
		return lspId;
	}
	public void setLspId(String lspId) {
		this.lspId = lspId;
	}
	public String getMaterialCodeId() {
		return materialCodeId;
	}
	public void setMaterialCodeId(String materialCodeId) {
		this.materialCodeId = materialCodeId;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getModem() {
		return modem;
	}
	public void setModem(String modem) {
		this.modem = modem;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getSoldToId() {
		return soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	public String getLspIndex() {
		return lspIndex;
	}
	public void setLspIndex(String lspIndex) {
		this.lspIndex = lspIndex;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
}
