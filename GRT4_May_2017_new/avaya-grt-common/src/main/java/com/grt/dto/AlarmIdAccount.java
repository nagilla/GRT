package com.grt.dto;

import java.io.Serializable;

public class AlarmIdAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accountLocation; // site id 
	private String AVAYAalarmID;
	private String lifeCycleStatus;
	/**
	 * @return the accountLocation
	 */
	public String getAccountLocation() {
		return accountLocation;
	}
	/**
	 * @param accountLocation the accountLocation to set
	 */
	public void setAccountLocation(String accountLocation) {
		this.accountLocation = accountLocation;
	}
	/**
	 * @return the aVAYAalarmID
	 */
	public String getAVAYAalarmID() {
		return AVAYAalarmID;
	}
	/**
	 * @param aalarmID the aVAYAalarmID to set
	 */
	public void setAVAYAalarmID(String aalarmID) {
		AVAYAalarmID = aalarmID;
	}
	public String getLifeCycleStatus() {
		return lifeCycleStatus;
	}
	public void setLifeCycleStatus(String lifeCycleStatus) {
		this.lifeCycleStatus = lifeCycleStatus;
	}
	
}
