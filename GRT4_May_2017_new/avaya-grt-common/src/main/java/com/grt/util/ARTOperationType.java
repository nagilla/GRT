package com.grt.util;

import java.io.Serializable;

public enum ARTOperationType implements Serializable {    

	DATABASENEW ("DN","Database New registration"),
	FULLNEW ("FN","FullNew registration"),
	DATABASEUPDATE ("DU","Database Update"),
	PASSWORDRESET ("PR","Password Reset"),
	REGENONBOARDXML ("RN","Regenrate Onboarding XML"),
	ONBOARDXML ("ON","SSL VPN"),
	/* GRT 4.0 */
	TEST_CONNECTION ("TEST_CONNECTION", "Test Connection"),
	TEST_ALARM ("TEST_ALARM", "Test Alarm"),
	FULL_ONBOARD ("FULL_ONBOARD", "Full Onboard"),
	ALARM_OFFBOARD ("ALARM_OFFBOARD", "ALARM_OFFBOARD");
	
	private final String operationKey;
	private final String operationDetail;
	

	ARTOperationType(String operationKey,String operationDetail){
		this.operationKey = operationKey;
		this.operationDetail = operationDetail;
	  }

	public String getOperationDetail() {
		return operationDetail;
	}

	public String getOperationKey() {
		return operationKey;
	}
}
