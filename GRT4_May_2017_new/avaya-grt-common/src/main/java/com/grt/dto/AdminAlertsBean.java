package com.grt.dto;

import java.io.Serializable;
import java.util.List;

import com.grt.dto.AlertDto;

/**
 * @author Bhupendra_Singh04
 *
 */
public class AdminAlertsBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String alertId;
	public String sDate;
	public String sTime;
	public String eDate;
	public String eTime;
	public String message;
	public List<AlertDto> alertsList;
	
	public String requiredAction;
	
	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	
	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<AlertDto> getAlertsList() {
		return alertsList;
	}

	public void setAlertsList(List<AlertDto> alertsList) {
		this.alertsList = alertsList;
	}
	
	public String getRequiredAction() {
		return requiredAction;
	}

	public void setRequiredAction(String requiredAction) {
		this.requiredAction = requiredAction;
	}
}
