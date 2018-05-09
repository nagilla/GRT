package com.grt.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;


public class InstallBaseCreationDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String registrationId;
	private String destination;
	private List<TechnicalOrderDetail> installBaseData;
	
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public List<TechnicalOrderDetail> getInstallBaseData() {
		return installBaseData;
	}
	public void setInstallBaseData(List<TechnicalOrderDetail> installBaseData) {
		this.installBaseData = installBaseData;
	}
}
