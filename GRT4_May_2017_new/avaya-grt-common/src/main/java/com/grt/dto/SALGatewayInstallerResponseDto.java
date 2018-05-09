package com.grt.dto;

import java.io.Serializable;

public class SALGatewayInstallerResponseDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String registrationId;
	private String errorDescription;
	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}
	/**
	 * @return the registrationId
	 */
	public String getRegistrationId() {
		return registrationId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	/**
	 * @param registrationId the registrationId to set
	 */
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
