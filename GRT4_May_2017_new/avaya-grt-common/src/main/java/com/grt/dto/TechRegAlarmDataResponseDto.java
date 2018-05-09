package com.grt.dto;

import java.io.Serializable;
import java.util.List;


public class TechRegAlarmDataResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	
	protected String techRegId;
    protected String onBoardingOp;
    protected String seid;
    protected String accessType;
    protected String activityLog;
    protected String returnCode;
    protected String description;
    protected List<ParametersType> keyValueList;

    public String getTechRegId() {
		return techRegId;
	}

	public void setTechRegId(String techRegId) {
		this.techRegId = techRegId;
	}

	public String getOnBoardingOp() {
		return onBoardingOp;
	}

	public void setOnBoardingOp(String onBoardingOp) {
		this.onBoardingOp = onBoardingOp;
	}

	public String getSeid() {
		return seid;
	}

	public void setSeid(String seid) {
		this.seid = seid;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getActivityLog() {
		return activityLog;
	}

	public void setActivityLog(String activityLog) {
		this.activityLog = activityLog;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ParametersType> getKeyValueList() {
		return keyValueList;
	}

	public void setKeyValueList(List<ParametersType> keyValueList) {
		this.keyValueList = keyValueList;
	}

	public class ParametersType {

        protected String key;
        protected String value;
        
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

        
    }
	
}
