package com.avaya.grt.web.action.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avaya.grt.mappers.Alert;
import com.avaya.grt.service.administrator.AdministratorService;
import com.avaya.grt.web.action.GrtConfig;
import com.avaya.grt.web.security.AbstractSiteMinderAwareAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.AdminAlertsBean;
import com.grt.dto.AlertDto;
import com.opensymphony.xwork2.Action;

public class AdministratorAction extends AbstractSiteMinderAwareAction {
	
	private static final Logger logger = Logger.getLogger(AdministratorAction.class);
	
	private static final String DATE_FORMAT = "MMddyyyy HH:mm:ss";
	
	protected GrtConfig grtConfig;
	
	private AdministratorService administratorService;
	
	public GrtConfig getGrtConfig() {
		return grtConfig;
	}

	public void setGrtConfig(GrtConfig grtConfig) {
		this.grtConfig = grtConfig;
	}
	
	public AdministratorService getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(AdministratorService administratorService) {
		this.administratorService = administratorService;
	}
	
	public AdminAlertsBean adminAlertForm = new AdminAlertsBean();
	
	public String viewAnnouncements() throws Exception {
		logger.debug("In AdministratorAction in viewAnnouncements action");
		
		//validate session
		validateSession();
		
		try {
			adminAlertForm.setAlertsList(constructAlertDtoList(getAdministratorService().getAdminAlerts()));
			logger.debug("AdministratorAction.viewAnnouncements(), List Size - " + adminAlertForm.getAlertsList().size());

		} catch (Throwable throwable) {
			logger.error("Exception in viewAnnouncements: ", throwable);
		} finally {
			logger.debug("Exiting AdministratorAction - viewAnnouncements");
		}
		return Action.SUCCESS;
	}
	
	public String updateAnnouncement() throws Exception {
		logger.debug("In AdministratorAction in addAnnouncement action");
		validateSession();
		
		try {
			CSSPortalUser userProfile = getUserFromSession();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			Alert alert = new Alert();
			String reqAction = adminAlertForm.getRequiredAction() != null ? adminAlertForm.getRequiredAction() : "";
			if(reqAction.equalsIgnoreCase("delete")) {
				//set required properties to delete
				alert.setAlertId(adminAlertForm.getAlertId());
				alert.setLastModifiedBy(userProfile.getUserId());
				alert.setIsExist("0");
				//call delete method of service
				getAdministratorService().deleteAlert(alert);
			} else if(reqAction.equalsIgnoreCase("add") 
					|| reqAction.equalsIgnoreCase("update")) {
				//set specific properties for add & update accordingly
				if (reqAction.equalsIgnoreCase("add")) {					
					alert.setCreatedBy(userProfile.getUserId());
				} else if (reqAction.equalsIgnoreCase("update")) {
					alert.setAlertId(adminAlertForm.getAlertId());					
					alert.setLastModifiedBy(userProfile.getUserId());
					alert.setCreatedBy(userProfile.getUserId());
				}
				//set common properties for add & update
				alert.setMessage(adminAlertForm.getMessage());
				alert.setStartDate(formatter.parse(adminAlertForm.getsDate() + " " + adminAlertForm.getsTime()));
				alert.setEndDate(formatter.parse(adminAlertForm.geteDate() + " " + adminAlertForm.geteTime()));
				
				//call the update method of service
				getAdministratorService().saveOrUpdateAlertDetails(alert);
			} else {
				throw new RuntimeException("Action value is not set in the form");
			}
			//added to fetch the new details
			adminAlertForm.setAlertsList(constructAlertDtoList(getAdministratorService().getAdminAlerts()));
			//logger.debug("AdministratorAction.updateAnnouncement");
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting AdministratorAction - updateAnnouncement");
		}
		return Action.SUCCESS;
	}
	
	private List<AlertDto> constructAlertDtoList(List<Alert> alertsList){
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		List<AlertDto> alerts = new ArrayList<AlertDto>();
		AlertDto alertDto = null;
		for(Alert alert : alertsList){
			alertDto = new AlertDto();
			alertDto.setAlertId(alert.getAlertId());
			alertDto.setCreatedBy(alert.getCreatedBy());
			alertDto.setCreatedDate(alert.getCreatedDate());
			alertDto.setEndDate(formatter.format(alert.getEndDate()));
			alertDto.setLastModifiedBy(alert.getLastModifiedBy());
			alertDto.setLastModifiedDate(alert.getLastModifiedDate());
			alertDto.setMessage(alert.getMessage());
			alertDto.setStartDate(formatter.format(alert.getStartDate()));
			alerts.add(alertDto);
		}
		return alerts;
	}
}
