package com.grt.dto;

import java.io.Serializable;

public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String accountLocation;
	private String activityId;
	private String srNumber;
	private String description;
	private String comment;
	private String type;
	private String status;
	private boolean privateActivity;
	
	public String getAccountLocation() {
		return accountLocation;
	}
	public void setAccountLocation(String accountLocation) {
		this.accountLocation = accountLocation;
	}
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public boolean isPrivateActivity() {
		return privateActivity;
	}
	public void setPrivateActivity(boolean isPrivate) {
		this.privateActivity = isPrivate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSrNumber() {
		return srNumber;
	}
	public void setSrNumber(String srNumber) {
		this.srNumber = srNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
