package com.grt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InstallBaseSapResponseDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String registrationId;
	private String errorId;
	private String routingInfo;
	private String returnCode;
	private List<InstallBaseRespDataDto> iBaseRespData = new ArrayList<InstallBaseRespDataDto>();
	
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	public String getRoutingInfo() {
		return routingInfo;
	}
	public void setRoutingInfo(String routingInfo) {
		this.routingInfo = routingInfo;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public List<InstallBaseRespDataDto> getIBaseRespData() {
		return iBaseRespData;
	}
	public void setIBaseRespData(List<InstallBaseRespDataDto> baseRespData) {
		iBaseRespData = baseRespData;
	}
}
