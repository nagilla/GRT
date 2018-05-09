package com.grt.util;

public enum StatusEnum {    

	INITIATE ("1001", "", "Initiate"),
	INPROCESS ("1002", "", "In Process"),
	AWAITINGINFO ("1003", "", "Awaiting Information"),
	COMPLETED ("1004", "", "Completed"),
	NOTINITIATED ("1005", "", "Not Initiated"),
	SKIPPED ("1006", "", "Skipped"),
	SAVED ("1007", "", "Saved"),
	CANCELLED ("1008", "", "Cancelled"),
	SAPCOMPLETED ("1009", "", "SAP Completed"),
	OVERRIDEN ("1010", "", "Overriden"),
	VALIDATED ("1011", "", "Validated");

	
	private final String statusId;
	private final String statusDescription;
	private final String statusShortDescription;

	StatusEnum(String statusId, String statusDescription, String statusShortDescription){
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
	
	public static String getById(String id) {
	    for(StatusEnum e : values()) {
	        if(e.statusId.equals(id)) return e.getStatusShortDescription();
	    }
	    return null;
	 }
	
	public static void main(String args[])
	{
		System.out.println("=========="+StatusEnum.getById("1007"));
	}

}
