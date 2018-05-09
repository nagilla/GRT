package com.grt.util;

public enum SRRequestStatusEnum {    

	INITIATION ("2001", "", "Initiation"),
	SR_CREATED ("2002", "", "SR Created"),
	ATTACHMENT_SENT ("2003", "", "Attachment Sent"),
	SUCCESS ("2004", "", "Success");
	
	private final String statusId;
	private final String statusDescription;
	private final String statusShortDescription;

	SRRequestStatusEnum(String statusId, String statusDescription, String statusShortDescription){
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.statusShortDescription = statusShortDescription;
	  }
	
	public String getStatusId() {
		return statusId;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusShortDescription() {
		return statusShortDescription;
	}

}
