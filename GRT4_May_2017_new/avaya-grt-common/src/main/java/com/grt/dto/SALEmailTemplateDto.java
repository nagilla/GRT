package com.grt.dto;

import java.util.ArrayList;
import java.util.List;

public class SALEmailTemplateDto {
	private String soldToId;
	private String seCode;
	private String materialCode;
	private String serverType;
	private String gatewayType;
	private String modem;
	private String alarmOrientation;
	private String nickName;
	private String cmVersion;
	private String mid;
	private String sid;
	private String model;
	private String mediaService;
	private String server;
	private String hardWareName;
	private String lspQuantity;
	private String aacStandard;
	private String version;
	private String rootPassword;
	private String rasPassword;
	private String os;
	private String cmTemplate;
	private boolean lspOnly = false;
	private List<List<String>> cmSoftwareSolutionList = new ArrayList<List<String>>();
	private List<List<String>> cmHardwareElementList = new ArrayList<List<String>>();
	private List<List<String>> essSoftwareSolutionList = new ArrayList<List<String>>();
	private List<List<String>> essHardwareElementList = new ArrayList<List<String>>();
	private List<List<String>> lspSoftwareElementList = new ArrayList<List<String>>();
	private List<List<String>> lspHardwareElementList = new ArrayList<List<String>>();
	private List<List<String>> aacSoftwareElementList = new ArrayList<List<String>>();
	private List<List<String>> aacHardwareElementList = new ArrayList<List<String>>();
	private List<LSPRegistrationDto> lspRegistrationList = new ArrayList<LSPRegistrationDto>();

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
	public String getGatewayType() {
		return gatewayType;
	}
	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}
	public boolean isLspOnly() {
		return lspOnly;
	}
	public void setLspOnly(boolean lspOnly) {
		this.lspOnly = lspOnly;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
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
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
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
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMediaService() {
		return mediaService;
	}
	public void setMediaService(String mediaService) {
		this.mediaService = mediaService;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getHardWareName() {
		return hardWareName;
	}
	public void setHardWareName(String hardWareName) {
		this.hardWareName = hardWareName;
	}
	public List<List<String>> getCmSoftwareSolutionList() {
		return cmSoftwareSolutionList;
	}
	public void setCmSoftwareSolutionList(List<List<String>> cmSoftwareSolutionList) {
		this.cmSoftwareSolutionList = cmSoftwareSolutionList;
	}
	public List<List<String>> getCmHardwareElementList() {
		return cmHardwareElementList;
	}
	public void setCmHardwareElementList(List<List<String>> cmHardwareElementList) {
		this.cmHardwareElementList = cmHardwareElementList;
	}
	public List<List<String>> getEssHardwareElementList() {
		return essHardwareElementList;
	}
	public void setEssHardwareElementList(List<List<String>> essHardwareElementList) {
		this.essHardwareElementList = essHardwareElementList;
	}
	public List<List<String>> getEssSoftwareSolutionList() {
		return essSoftwareSolutionList;
	}
	public void setEssSoftwareSolutionList(
			List<List<String>> essSoftwareSolutionList) {
		this.essSoftwareSolutionList = essSoftwareSolutionList;
	}
	public List<List<String>> getLspHardwareElementList() {
		return lspHardwareElementList;
	}
	public void setLspHardwareElementList(List<List<String>> lspHardwareElementList) {
		this.lspHardwareElementList = lspHardwareElementList;
	}
	public List<List<String>> getLspSoftwareElementList() {
		return lspSoftwareElementList;
	}
	public void setLspSoftwareElementList(List<List<String>> lspSoftwareElementList) {
		this.lspSoftwareElementList = lspSoftwareElementList;
	}
	public List<List<String>> getAacHardwareElementList() {
		return aacHardwareElementList;
	}
	public void setAacHardwareElementList(List<List<String>> aacHardwareElementList) {
		this.aacHardwareElementList = aacHardwareElementList;
	}
	public List<List<String>> getAacSoftwareElementList() {
		return aacSoftwareElementList;
	}
	public void setAacSoftwareElementList(List<List<String>> aacSoftwareElementList) {
		this.aacSoftwareElementList = aacSoftwareElementList;
	}
	public String getLspQuantity() {
		return lspQuantity;
	}
	public void setLspQuantity(String lspQuantity) {
		this.lspQuantity = lspQuantity;
	}
	public String getAacStandard() {
		return aacStandard;
	}
	public void setAacStandard(String aacStandard) {
		this.aacStandard = aacStandard;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getRasPassword() {
		return rasPassword;
	}
	public void setRasPassword(String rasPassword) {
		this.rasPassword = rasPassword;
	}
	public String getRootPassword() {
		return rootPassword;
	}
	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<LSPRegistrationDto> getLspRegistrationList() {
		return lspRegistrationList;
	}
	public void setLspRegistrationList(List<LSPRegistrationDto> lspRegistrationList) {
		this.lspRegistrationList = lspRegistrationList;
	}
	public String getCmTemplate() {
		return cmTemplate;
	}
	public void setCmTemplate(String cmTemplate) {
		this.cmTemplate = cmTemplate;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
}
