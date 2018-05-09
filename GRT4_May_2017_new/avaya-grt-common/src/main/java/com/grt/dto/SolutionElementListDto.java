package com.grt.dto;

import java.io.Serializable;

public class SolutionElementListDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String newSEID;
	private String seCode;
	private String ipAddess;
	private String alarmId;
	private String recordNum;
	private String failedSeId, failedRecordNum, failedSeCode;
	
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getIpAddess() {
		return ipAddess;
	}
	public void setIpAddess(String ipAddess) {
		this.ipAddess = ipAddess;
	}
	public String getNewSEID() {
		return newSEID;
	}
	public void setNewSEID(String newSEID) {
		this.newSEID = newSEID;
	}
	public String getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
	public String getFailedRecordNum() {
		return failedRecordNum;
	}
	public void setFailedRecordNum(String failedRecordNum) {
		this.failedRecordNum = failedRecordNum;
	}
	public String getFailedSeCode() {
		return failedSeCode;
	}
	public void setFailedSeCode(String failedSeCode) {
		this.failedSeCode = failedSeCode;
	}
	public String getFailedSeId() {
		return failedSeId;
	}
	public void setFailedSeId(String failedSeId) {
		this.failedSeId = failedSeId;
	}

}
