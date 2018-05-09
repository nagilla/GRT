package com.avaya.grt.web.action.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.avaya.grt.mappers.Alert;
import com.avaya.grt.service.graph.RegistrationGraphService;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.web.action.GrtConfig;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.AbstractSiteMinderAwareAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.avaya.grt.web.util.GRTGraphUtil;
import com.grt.dto.Account;
import com.grt.dto.AlertDto;
import com.grt.dto.BusinessPartner;
import com.grt.dto.RegistrationCount;
import com.grt.dto.RegistrationFormBean;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.opensymphony.xwork2.Action;

@SuppressWarnings("unused")
public class RegistrationHomeAction extends TechnicalRegistrationAction {

	private static final Logger logger = Logger
			.getLogger(RegistrationHomeAction.class);

	private Boolean passwordResetFlag = true;

	private String materialExclusionFlag = "";

	private Boolean salMigrationOnlyFlag = false;

	private Boolean ipoContinueInstallBase = false;

	private Boolean validateMaterialCode = false;

	private Boolean saveAssistIPO = false;

	private Boolean installBaseEnableSubmit = false;

	private Boolean hideBackButton = true;

	private static final String DATE_FORMAT = "MMddyyyy HH:mm:ss";

	protected RegistrationGraphService registrationGraphService;
	
	private Map<String, Object> graphImages = new HashMap<String, Object>();

	public String registrationHome() throws DataAccessException{

		//Remove Form Bean from session
		actionForm = new RegistrationFormBean();
		
		//GRT 4.0 Change : Remove Session Attributes
		removeSessionAttributes();

		System.out.println("Inside Registration Home....");

		long time1 = System.currentTimeMillis();
		String passwordResetParam;
		String outageFlag = "";	
		RegistrationFormBean form = new RegistrationFormBean();
		CSSPortalUser cssPortalUser = null;
		getRequest().getSession().removeAttribute("prFlag");
		clearSessionVars();
		try {
			//TODO
			//outageFlag = GRTPropertiesUtil.getProperty("grt_outage_flag").trim();
			if(GRTConstants.NUMBER_ONE.equalsIgnoreCase(outageFlag)){
				logger.debug("Navigating to GRT Outage screen."+outageFlag);
				return "grtOutage";
			}
			try {
				System.out.println("Inside css portal user TRY");
				cssPortalUser = getCSSUserProfile();
			} catch (Exception e) {
				System.out.println("Inside css portal user CATCH");
				e.printStackTrace();
			} finally {
				System.out.println("Inside css portal user FINALLY");
				if (cssPortalUser == null) {
					System.out.println("Inside css portal user FINALL * cssPortalUser - "+cssPortalUser);
					cssPortalUser = getDummyCssUserProfile();
				}
			}

			if (cssPortalUser == null) {
				return "login";
			}

			//TODO remove the delegate with service call
			//registrationDelegate.create();

			form.setUserId(cssPortalUser.getUserId());
			logger.debug("Begin  ***  User Id: ***********  "+cssPortalUser.getUserId());

			form.setMegaUser(getBaseRegistrationService().isMegaUser(cssPortalUser.getUserId()));

			getRequest().setAttribute(GRTConstants.GRT_MEGA_USER, form.getMegaUser());
			//installBaseCreationReadOnlyFlag = false;
			materialExclusionFlag = "";
			salMigrationOnlyFlag = false;
			form.setSuperUser(cssPortalUser.isSuperUser());


			/* * [AVAYA] GRT PASSWORD RESET Changed made by Lavanya (Start)
			 * Description: New page handling (Password Reset)*/

			HashMap<String, String> map = null;
			passwordResetParam = getRequest().getQueryString();
			logger.debug("Query String: " + passwordResetParam);
			if (passwordResetParam != null && !passwordResetParam.equals("")) {
				//TLFILTER.set("PR");
				String serviceName = null;
				try{
					serviceName = getGrtConfig().getService_name().trim();
					System.out.println("Service Name:::::"+serviceName);
				}
				catch(Exception e){

				}
				form.setServiceName(serviceName);
				getRequest().setAttribute("SERVICE_NAME", serviceName);
				logger.debug("serviceName from Properties File  -->"+serviceName);
				map = new HashMap<String, String>();
				String[] param = passwordResetParam.split("&");
				for (int i = 0; i < param.length; i++) {
					String[] param1 = param[i].split("=");
					if (param1.length == 1) {
						map.put(param1[0], " ");
					} else {
						map.put(param1[0], param1[1]);
					}
				}
				if (map.get("AppName") != null) {
					logger.debug("*****AppName:"+map.get("AppName"));
					if (map.get("AppName").equals("PR")
							&& map.get("source") == null
							&& map.get("action") == null) {
						logger.debug("Params AppName exist");
						getRequest().getSession().setAttribute("prFlag", "PR");
						this.passwordResetFlag = true;
						//return new Forward("passwordReset",form);
						return "login";
					} else if (map.get("source") != null
							&& map.get("action") != null) {
						if (map.get("AppName").equals("PR")
								&& map.get("source").equals("IPO")
								&& map.get("action").equals("Modify")) {
							getRequest().getSession().setAttribute("prFlag", "PR");
							logger.debug("Params source and action exist1");
							this.passwordResetFlag = false;
							//return new Forward("passwordReset",form);
							return "login";
						} else if (map.get("AppName").equals("PR")
								&& map.get("source").equals("IPO")
								&& map.get("action").equals(
										"EstablishConnectivity")) {
							logger.debug("Params source and action exist2");
							getRequest().getSession().setAttribute("prFlag", "PR");
							this.passwordResetFlag = true;
							this.hideBackButton = false;
							//return new Forward("registrationlist");
							return "login";
						}
					} else if (map.get("AppName").equals("EQRO")){
						form.setSoldToId(map.get("soldTo"));
						//return new Forward("EQRO", form);
						return "login";
					}
				}
			} else {
				getRequest().getSession().removeAttribute("prFlag");
				//TLFILTER.set(null);
			}
			// [AVAYA] GRT PASSWORD RESET Changed made by Lavanya (End)
			// Begin Suggestions Link
			//String grtSuggestionsUrl = getGrtSuggestionsUrl().trim();
			getRequest().getSession().setAttribute(
					GRTConstants.GRT_SUGGESTIONS_URL_FOR_UI, getGrtConfig().getGrtSuggestionsUrl().trim());
			System.out.println("GrtSuggestionUrl"+getGrtConfig().getGrtSuggestionsUrl());
			// End Suggetions Link
			//Begin Help Link
			String grtHelpUrl = getGrtConfig().getGrtHelpUrl().trim();
			getRequest().getSession().setAttribute(GRTConstants.GRT_HELP_URL, grtHelpUrl);
			//Begin Help Link
			// picking Alerts list - Start
			//service call
			form.setAlertsList(constructAlertDtoList(getBaseRegistrationService().getActiveAlerts()));
			// picking Alerts list - End

			//Begin Remote Site Survey (EPN Survey) Link
			String rssOrEPNServeyUrl = getGrtConfig().getRssOrEpnServeyUrl().trim();
			getRequest().getSession().setAttribute(
					GRTConstants.GRT_RSS_OR_EPN_SERVEY_URL_FOR_UI, rssOrEPNServeyUrl);
			//End Remote Site Survey (EPN Survey) Link
			//Begin Direct Customer/Business Partner Check
			String userType  = cssPortalUser.getUserType();
			if(userType != null && userType.equalsIgnoreCase("C")){
				form.setUserRole("C");
			} else if(userType != null && userType.equalsIgnoreCase("B")){
				form.setUserRole("BP");
			}
			//End Direct Customer/Business Partner Check

			// FOR ASK AVA 
			getRequest().getSession().setAttribute("askavauser",cssPortalUser);
			if (cssPortalUser != null && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(cssPortalUser.getUserType()))
			{
				//TODO remove the delegate with service call
				BusinessPartner businessPartner = getBaseRegistrationService().getBusinessPartner(cssPortalUser.getBpLinkId());
				getRequest().getSession().setAttribute("askavabpuser",businessPartner);
			}
			else if (cssPortalUser != null && GRTConstants.DIRECT_CUSTOMER.equalsIgnoreCase(cssPortalUser.getUserType()))
			{
				//TODO remove the delegate with service call
				Account account = getBaseRegistrationService().getCustomerAccountDetails(cssPortalUser.getUserId());
				getRequest().getSession().setAttribute("askavacoustomeruser",account);
			}

		} catch (Exception e) {
			getRequest().getSession().removeAttribute("prFlag");
			logger.error(e.getMessage());
			e.printStackTrace();
			//throw e;
		} finally {
			long time2 = System.currentTimeMillis();
			logger.debug("time taken in seconds:" + ((time2-time1)/1000));
		}

		actionForm = form;
		
		//Populate Graphs
		getHomePageGraphs();
		
		return Action.SUCCESS;
	}
	
	public void clearSessionVars (){
		getRequest().getSession().removeAttribute(GRTConstants.REGISTRATION_TYPE);
		getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
		getRequest().getSession().removeAttribute(GRTConstants.FROM_TR_OB_DETAIL);
		actionForm.setFromTrObDetail( null );
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

	private void getHomePageGraphs(){
		
		CSSPortalUser cssPortalUser = getLoggedInUserDetail();
		
		List<RegistrationCount> registrationsCompletedList = null;
	
		try {
	registrationsCompletedList = getRegistrationGraphService().getRegistrationsCompletedList(cssPortalUser.getUserId());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// c) Get the details for Technical Registrations InComplete Status


		String technicalRegistrationsIncompleteStatus = null;
		if(null != registrationsCompletedList) {
			technicalRegistrationsIncompleteStatus = GRTGraphUtil.createPlotRingStackedGraphForTOBRegistrations(registrationsCompletedList,GRTConstants.registrationsTOB);
		}
		graphImages.put("AllTechnicalRegistrationsIncompleteChart", technicalRegistrationsIncompleteStatus);

		/* End logic for getting the Graph Images */
	}

	public void removeSessionAttributes(){
		getRequest().getSession().removeAttribute(GRTConstants.FROM_TR_OB_DETAIL);
	}
	
	public RegistrationGraphService getRegistrationGraphService() {
		return registrationGraphService;
	}

	public void setRegistrationGraphService(
			RegistrationGraphService registrationGraphService) {
		this.registrationGraphService = registrationGraphService;
	}
	
	public Map<String, Object> getGraphImages() {
		return graphImages;
	}

	public void setGraphImages(Map<String, Object> graphImages) {
		this.graphImages = graphImages;
	}

	/* Methods below are implemented in their corresponding implementation classes */
	@Override
	public String technicalRegistrationDashboard() {
		return null;
	}

	@Override
	public String installBaseCreation() {
		return null;
	}

	@Override
	public String salGatewayMigrationList() {
		// TODO Auto-generated method stub
		return null;
	}

}
