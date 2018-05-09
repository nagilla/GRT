package com.avaya.grt.web.action;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProcessStep;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.service.BaseRegistrationService;
import com.avaya.grt.service.account.AccountService;
import com.avaya.grt.service.equipmentremoval.EQRService;
import com.avaya.grt.service.equipmentremoval.EQRServiceImpl;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.service.technicalonboarding.TechnicalOnBoardingService;
import com.avaya.grt.web.action.installbase.InstallBaseCreationAction;
import com.avaya.grt.web.security.AbstractSiteMinderAwareAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.Account;
import com.grt.dto.Activity;
import com.grt.dto.Pager;
import com.grt.dto.PaginationForSiteRegistration;
import com.grt.dto.PurchaseOrder;
import com.grt.dto.RVSummaryRecord;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationList;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.ServiceRequest;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.integration.siebel.SiebelClient;
import com.grt.util.AutoCompleteCache;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.GenericSort;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.opensymphony.xwork2.Action;

/**
 * @description This class contains actions that are common in the flow for IB, Tech On-boarding
 *
 */
public abstract class TechnicalRegistrationAction extends AbstractSiteMinderAwareAction{

	private static final Logger logger = Logger
			.getLogger(TechnicalRegistrationAction.class);

	public RegistrationFormBean actionForm = new RegistrationFormBean();

	public RegistrationFormBean regObj = new RegistrationFormBean();
	
	protected BaseRegistrationService baseRegistrationService;
	//GRT 4.0 Changes
	protected EQRService eqrService;
	private static final String DATE_FORMAT1 = "MM/dd/yyyy HH:mm:ss";
	private static final String DATE_FORMAT2 = "MM/dd/yyyy";
	private InstallBaseService installBaseService;

	protected SiebelClient siebelClient;

	protected List<String> soldToList;

	protected String soldTo;

	protected AutoCompleteCache cxpSoldToCache = new AutoCompleteCache();

	protected List<RegistrationSummary> registrationSummaries;
	
	private static final String DATE_FORMAT_APPEND_ATTACHMENT_NAME = "yyyyMMddHHmmss";
	
	private static final String ATTACHMENT_NAME_EXTENSION = ".xls";
	
	private static final String DEFAULT_UPLOAD_DIR = "upload";
	
	private static final int SOLDTOID_MAX_LENGTH=50;
	private static final int USERID_MAX_LENGTH=30;
	private static final int USERROLE_MAX_LENGTH=10;

	//GRT 4.0 Changes
	//protected SiebelService siebelService;
	protected AccountService accountService;
	

	private TechnicalOnBoardingService technicalOnBoardingService;
	private String skipStepB = "none";

	protected String navigateTo = "";

	protected String callback;

	protected Boolean installBaseCreationReadOnlyFlag = false;
	
	//GRT 4.0 24 March
	protected Boolean salMigrationOnlyFlag=false;
	
	protected GrtConfig grtConfig;
	
	protected Map<String, Object> regListMap;

	private int pageNo;
	
	private int maxRegListSize;
	
	private int regListRowNum;
	
	protected String statusList;
	
	protected String regTypeList;
	
	private int startIndex;
	
	private int maxResults;
	
	private boolean allRegData;
	
	protected String registrationId;
	
	protected String onsiteEmail;
	
	protected String serviceMsg;
	
	protected String emailId;
	
	protected String errorMessage;
	
	/* Registration List Variables */
	protected Map<String,Object> regListParameterMap;
	
	protected String prFlag;
	
	protected int draw;
	protected int start;
	protected int length;

	protected Map<String, Object> search;

	protected List<Map<String, Object>> columns;

	protected List<Map<String, Object>> order;
	
	protected Map<String, Object> dataTableParams;
	
	protected String requesterName;
	
	protected String bufferOverFlowMsg;
	
	protected Map<String, Object> materialMasterMap;

	/**
	 * Used to validation sold to and forward the user to new registration page
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String newRegistrationWithSoldToValidation() throws Exception {
		validateSession();
		try {

			actionForm.setSoldToError(null);
			System.out.println("Inside New Registration...");
			this.getRequest().removeAttribute("soldToError");
			// TODO remove hardcoded actionForm take this from Request object
			// RegistrationFormBean form = new RegistrationFormBean();
			if (logger.isDebugEnabled())
				logger.debug("Entering RegistrationController : newRegistrationWithSoldToValidation");
			String soldToIdVal = actionForm.getSoldToId();
			soldToIdVal = StringEscapeUtils.unescapeHtml(soldToIdVal);

			// Check for sold to assocation
			if (getBaseRegistrationService().isSoldToExcluded( soldToIdVal )) {
				navigateTo = backToSoldToSelectPage(grtConfig.getEwiMessageCodeMap().get("soldToExcludedMsg") + "###" + grtConfig.getSoldToExcludedMsg());
				actionForm.setNavigateTo(navigateTo);
				return navigateTo;
			}
			CSSPortalUser user = getUserFromSession();

			actionForm.setAccount(null);
			Account account = null;

			// TODO webservice Call
			
			if(!(soldToIdVal.equalsIgnoreCase("Null") || soldToIdVal.equalsIgnoreCase(""))){
				account = getSiebelClient().queryAccount( soldToIdVal );
			}
			/* account = new Account(); */

			actionForm.setAccount(account);
			if (account == null) {
				/*navigateTo =  backToSoldToSelectPage("Sold To " + soldToIdVal
						+ " does not exist.");*/				
				navigateTo =  backToSoldToSelectPage(grtConfig.getEwiMessageCodeMap().get("siteRegSoldToNotExistErrMsg") + "###" + grtConfig.getSiteRegSoldToNotExistErrMsg().replace(":soldTo" , soldToIdVal) );
				
				actionForm.setNavigateTo(navigateTo);
				return navigateTo;
			}
			String actType = account != null ? account.getType() : null;
			/*if (GRTConstants.ACCOUNT_TYPE_HIERARCHY.equalsIgnoreCase(actType)
					|| GRTConstants.ACCOUNT_TYPE_UCM_AVAYA_HN
					.equalsIgnoreCase(actType)
					|| GRTConstants.ACCOUNT_TYPE_UCM_GDU_HN
					.equalsIgnoreCase(actType)) {*/
			if (!(GRTConstants.ACCOUNT_TYPE_SHIPTO_PARTY.equalsIgnoreCase(actType)
					|| GRTConstants.ACCOUNT_TYPE_SOLDTO_PARTY.equalsIgnoreCase(actType))){
				logger.debug("Account Type:" + actType);
				navigateTo = backToSoldToSelectPage(grtConfig.getEwiMessageCodeMap().get("soldToNotEligibleMsg") + "###" + grtConfig.getSoldToNotEligibleMsg());
				actionForm.setNavigateTo(navigateTo);
				return navigateTo;
			}

			if (this.getBaseRegistrationService().isSoldToValidForCurrentUser(
					soldToIdVal, user.getUserId(), user.getBpLinkId())) {
				navigateTo = newRegistration();
				actionForm.setNavigateTo(navigateTo);
				return navigateTo;
			} else {
				navigateTo = backToSoldToSelectPage(grtConfig.getEwiMessageCodeMap().get("soldToNotAuthMsg") + "###" + grtConfig.getSoldToNotAuthMsg());
				actionForm.setNavigateTo(navigateTo);
				return navigateTo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return backToSoldToSelectPage(GRTConstants.NOT_AUTHORIZED_FOR_SOLD_TO);
			navigateTo =  backToSoldToSelectPage(grtConfig.getEwiMessageCodeMap().get("soldToNotAuthMsg") + "###" + grtConfig.getSoldToNotAuthMsg());
			actionForm.setNavigateTo(navigateTo);
			return navigateTo;
		}

	}

	/**
	 * @param errMsg
	 * @return String
	 * @description redirects the user to the sold-to selection 
	 */
	public String backToSoldToSelectPage(String errMsg) {
		String result = "";
		try {
			this.getRequest().setAttribute("soldToError", errMsg);
			actionForm.setSoldToError(errMsg);
			logger.debug("Invalid sold to, back to current page");
			result = locationFinder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String locationFinder(){
		try {
			validateSession();
			if (this.actionForm.getRegistrationId() != null) {
				// TODO WebService Call
				RegistrationSummary summary = getBaseRegistrationService()
						.getRegistrationSummary(actionForm.getRegistrationId());

				if ("1002".equals(summary.getInstallBaseStatusId())
						&& ((StringUtils.isNotEmpty(summary.getSrCompleted())) && (summary
								.getSrCompleted().equalsIgnoreCase("no")))) {
					// TODO code for adding Error
					logger.debug("Install Base in site InProcess and also the SR is not completed so showing Technical Order errors");
					this.actionForm.setIbaseStatus("1003");
				}
				logger.debug("Status ID " + summary.getInstallBaseStatusId()
						+ "and Get submitted flag" + summary.getSubmitted());
				if ("1002".equals(summary.getInstallBaseStatusId())
						&& (StringUtils.isNotEmpty(summary.getSubmitted()) && summary
								.getSubmitted().equals("1"))) {
					logger.debug("Making Ibase readonly false because IBase is in process and submitted Flag is 1");
					this.installBaseCreationReadOnlyFlag = false;
					//Added For UI
					actionForm.setInstallBaseCreationReadOnlyFlag(false);
				}

				if ((StringUtils.isNotEmpty(summary.getSubmitted()) && summary
						.getSubmitted().equals("0"))
						|| ("1008".equals(summary.getInstallBaseStatusId()))) {
					logger.debug("Setting the IBase creation to readonly because found a Submit already occured for this reg ID");
					this.installBaseCreationReadOnlyFlag = true;

				}
				if (summary != null
						&& summary.getRegistrationId() != null
						&& StringUtils.isNotEmpty(summary.getRegistrationId())
						&& this.actionForm.getRegistrationId().equals(
								summary.getRegistrationId())) {
					logger.info("Registration ID is saved into SITE_REGISTRATION table. Hence do nto clear the data from SCV screen");
				} else {
					// Begin: Clear the User entered values
					logger.info("Begin: CLEARING THE DATA FROM form wehn click on back button in SCV screen");
					this.actionForm.setRegistrationIdentifier(null);
					this.actionForm.setReportedPhone(null);
					this.actionForm.setOnsiteFirstname(null);
					this.actionForm.setOnsiteLastname(null);
					this.actionForm.setOnsitePhone(null);
					this.actionForm.setOnsiteemail(null);
					this.actionForm.setRegistrationNotes(null);
					this.actionForm.setSendMail("Y");
					this.actionForm.setUseSameAddress("Enter Manually");
					logger.info("End: CLEARING THE DATA FROM form wehn click on back button in SCV screen");
					// End: Clear the User entered values
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		actionForm.setFullRegistrationOnly(true);
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return ("toLocationSelection");
		} else {
			return ("toAgentLocationSelection");
		}
	}

	/**
	 * @return String
	 * @description Action to initiate the new registration.
	 */
	public String newRegistration() throws Exception {
		logger.debug("Entering RegistrationController : newRegistration");
		validateSession();
		//Set Navigation For UI
		actionForm.setNavigateTo(GRTConstants.SITE_CONTACT_PAGE);
		try {
			if (this.actionForm.getRegistrationId() != null) {
				// TODO Webservice Call
				RegistrationSummary summary = getBaseRegistrationService()
						.getRegistrationSummary(actionForm.getRegistrationId());

				if ("1002".equals(summary.getInstallBaseStatusId())
						&& ((StringUtils.isNotEmpty(summary.getSrCompleted())) && (summary
								.getSrCompleted().equalsIgnoreCase("no")))) {
					// TODO code for adding Error
					logger.debug("Install Base in site InProcess and also the SR is not completed so showing Technical Order errors");
					this.actionForm.setIbaseStatus("1003");
				}
				logger.debug("Status ID " + summary.getInstallBaseStatusId()
						+ "and Get submitted flag" + summary.getSubmitted());
				if ("1002".equals(summary.getInstallBaseStatusId())
						&& (StringUtils.isNotEmpty(summary.getSubmitted()) && summary
								.getSubmitted().equals("1"))) {
					logger.debug("Making Ibase readonly false because IBase is in process and submitted Flag is 1");
					actionForm.setInstallBaseCreationReadOnlyFlag(false);
				}

				if ((StringUtils.isNotEmpty(summary.getSubmitted()) && summary
						.getSubmitted().equals("0"))
						|| ("1008".equals(summary.getInstallBaseStatusId()))) {
					logger.debug("Setting the IBase creation to readonly because found a Submit already occured for this reg ID");
					//this.installBaseCreationReadOnlyFlag = true;
					actionForm.setInstallBaseCreationReadOnlyFlag(true);
				}

				// Begin: To populate the User Onsite Details for existing
				// Registration ID
				if (StringUtils.isEmpty(this.actionForm.getOnsiteFirstname())) {
					this.actionForm.setOnsiteFirstname(summary
							.getOnsiteFirstName());
				}
				if (StringUtils.isEmpty(this.actionForm.getOnsiteLastname())) {
					this.actionForm.setOnsiteLastname(summary
							.getOnsiteLastName());
				}
				if (StringUtils.isEmpty(this.actionForm.getOnsitephone())) {
					this.actionForm.setOnsitephone(summary.getOnsitePhone());
				}
				if (StringUtils.isEmpty(this.actionForm.getOnsiteemail())) {
					this.actionForm.setOnsiteemail(summary.getOnsiteEmail());
				}
				if (StringUtils.isEmpty(this.actionForm.getRegistrationNotes())) {
					this.actionForm.setRegistrationNotes(summary
							.getRegistrationNotes());
				}
				if (StringUtils.isEmpty(this.actionForm.getReportedPhone())) {
					this.actionForm.setReportedPhone(summary.getReportPhone());
				}
				if (StringUtils.isEmpty(this.actionForm
						.getRegistrationIdentifier())) {
					this.actionForm.setRegistrationIdentifier(summary
							.getRegistrationIdentifier());
				}
				// End: To populate the User Onsite Details for existing
				// Registration ID
			}

			if (actionForm != null) {
				PurchaseOrder po = actionForm.getPurchaseOrder();
				if (po.getPurchaseOrderNo() != null
						&& !"".equals(po.getPurchaseOrderNo())) {
					actionForm.setPoentered("true");
				}

				// if it is from skip EPNSurvey , gateway list should be
				// cleared.
				if ("true".equals(actionForm.getResetGatewayList())) {
					actionForm.setResetGatewayList("false");
					// this.as = new Assembler_EPN();
				}
				actionForm.setUserId(StringEscapeUtils.unescapeHtml(actionForm.getUserId()));
				actionForm.setSoldToId(StringEscapeUtils.unescapeHtml(actionForm.getSoldToId()));
			}
			Account account = actionForm.getAccount();
			actionForm = clearAddressInfo(actionForm);
			try {
				// Get the grt sequence
				if (StringUtils.isEmpty(actionForm.getRegistrationId())) {
					// TODO Service Call
					actionForm.setRegistrationId(getBaseRegistrationService()
							.getRegistrationId());
				}

				if (actionForm != null
						&& actionForm.getSoldToId() != null
						&& (account == null || !actionForm.getSoldToId()
						.equals(account.getSoldToNumber()))) {
					// TODO Service Call
					account = getSiebelClient().queryAccount(
							actionForm.getSoldToId());

				}
				actionForm = populateAddressInfo(account, actionForm);
				if (actionForm != null && actionForm.getSoldToId() != null
						&& StringUtils.isEmpty(actionForm.getCompany())) {
					// TODO Service Call

					RegistrationList registrationList = getBaseRegistrationService()
							.getRegistrationDetailFromCXPOrSalesOut(
									actionForm.getSoldToId());
					// TODO
					getBaseRegistrationService().setCXPRegistrationDataIntoFormBean(
							registrationList, actionForm);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error happens " + e.getMessage());
			} finally {
				if (actionForm.getCompany() == null && account != null) {
					actionForm.setCompany(account.getName());
				}
				CSSPortalUser userProfile = getUserFromSession();
				actionForm.setFirstName(userProfile.getFirstName());
				actionForm.setLastName(userProfile.getLastName());
				actionForm.setReportedEmail(userProfile.getEmailAddress());
				if (StringUtils.isEmpty(actionForm.getOnsiteFirstname())) {
					actionForm.setOnsiteFirstname(userProfile.getFirstName());
				}
				if (StringUtils.isEmpty(actionForm.getOnsiteLastname())) {
					actionForm.setOnsiteLastname(userProfile.getLastName());
				}
				if (StringUtils.isEmpty(actionForm.getOnsiteemail())) {
					actionForm.setOnsiteemail(userProfile.getEmailAddress());
				}
				//saveFormBeanInSession(actionForm);
			}

			logger.debug("Exiting RegistrationController : newRegistration");
			logger.debug("beforereturn  RegistrationController : form.getUserRole()"
					+ actionForm.getUserRole());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Action.SUCCESS;
	}

	/**
	 * @param account
	 * @param form
	 * @return RegistrationFormBean
	 * @description Populates Formbean with user address info from Account Object
	 */
	private RegistrationFormBean populateAddressInfo(Account account,
			RegistrationFormBean form) {
		if (account == null) {
			return form;
		}
		form.setCompany(account.getName());
		form.setAddress1(account.getPrimaryAccountStreetAddress());
		form.setAddress2(account.getPrimaryAccountStreetAddress2());
		form.setCity(account.getPrimaryAccountCity());
		form.setState(account.getPrimaryAccountState());
		form.setZip(account.getPrimaryAccountPostalCode());
		form.setSiteCountry(account.getPrimaryAccountCountry());
		form.setCompanyPhone(account.getPhoneNumber());
		form.setRegion(account.getRegion());
		return form;
	}

	/**
	 * @param text
	 * @return String
	 * @description fix cross site scripting by using StringEscapeUtils::escapeHTML
	 */
	protected String fixCSS(String text) {
		return (text == null || text == "") ? text : StringEscapeUtils
				.escapeHtml(text);
	}

	/**
	 * @param form
	 * @return RegistrationFormBean
	 * @description Reset address information
	 */
	private RegistrationFormBean clearAddressInfo(RegistrationFormBean form) {
		form.setCompany("");
		form.setAddress1("");
		form.setAddress2("");
		form.setCity("");
		form.setState("");
		form.setZip("");
		form.setSiteCountry("");
		form.setCompanyPhone("");
		form.setRegion("");
		return form;
	}

	/**
	 * @return String
	 * @description  Sold To AutoComplete Action
	 * 
	 */
	public String getCxpSoldToList() {
		//Get Logged In User Info
		CSSPortalUser user = getUserFromSession();
		try {
			// get the user id
			String userId = user.getUserId();
			String userType = user.getUserType();
			if (GRTConstants.AGENT.equalsIgnoreCase(userType)) {
				userId = "";
			}
			String bpLinkId = user.getBpLinkId();
			soldToList = getBaseRegistrationService().getCxpSoldToList(userId,
					bpLinkId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cxpSoldToCache.setCache(soldToList);
		soldToList = cxpSoldToCache.getFilteredDataList(soldTo);
		return Action.SUCCESS;
	}

	/* 
	 *  START :: Save registration information
	 * 
	 * */

	/**
	 * @return String
	 * @throws Exception
	 * @description Save registration information and redirect user to appropriate page
	 */
	public String saveSiteRegistration() throws Exception {
		logger.debug("Entering saveSiteRegistration()");
		validateSession();

		//Defect #225 : Check for buffer overflow validation
		if( checkForBufferOverFlow( actionForm ) ){
			return "back";
		}
		
		logger.debug("Entering saveSiteRegistration()");
		validateSession();
		actionForm.setActveSRColumnOnly(false);
		/*for defect 267*/
		actionForm.setBannerSrLabel(GRTConstants.TRUE);
		/*for defect 267*/
		//boolean flag=false;
		if (actionForm.isIpoRegistration()) {
			//actionForm.setIpoInventoryNotLoaded(GRTConstants.YES);
			actionForm.setRemoteConnectivity(GRTConstants.YES);
			return Action.SUCCESS;
			//TODO
			//return new Forward("IPO", actionForm); actionForm.isTechnicalRegistrationOnly()

		} else if(actionForm.isTechnicalRegistrationOnly()){
			
			logger.debug("RegistrationController : saveRegistration*****************************************TR only");
			SiteRegistration siteRegistration = null;
			if(StringUtils.isNotEmpty(fixCSS(actionForm.getRegistrationId()))) {
				try {
					actionForm.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED); /*For defect 267*/
					siteRegistration = getBaseRegistrationService().getSiteRegistrationOnRegId(fixCSS(actionForm.getRegistrationId()));
				} catch(Throwable throwable) {
					logger.error("Fetch SiteRegistration details on registrationId", throwable);
				}
				if(siteRegistration != null) {
					if(siteRegistration.getTechRegStatus() != null && fixCSS(siteRegistration.getTechRegStatus().getStatusId()).equals(StatusEnum.SAVED.getStatusId())) {
						try {
							//Update the account information : Defect#335
							siteRegistration.setSoldToId(fixCSS(actionForm.getSoldToId()));
							siteRegistration.setAddress(actionForm.getAddress1());
							siteRegistration.setAddress2(actionForm.getAddress2());
							siteRegistration.setCity(actionForm.getCity());
							siteRegistration.setState(actionForm.getState());
							siteRegistration.setZip(actionForm.getZip());
							siteRegistration.setSiteCountry(actionForm.getSiteCountry());
							siteRegistration.setCompany(actionForm.getCompany());
							siteRegistration.setCompanyPhone(actionForm.getCompanyPhone());
							siteRegistration.setRegion(actionForm.getCompanyPhone());
							getBaseRegistrationService().updateAccountInformation(siteRegistration);
						} catch(Throwable throwable) {
							logger.error("Update Site Contact Validation Information.", throwable);
							setErrorMessage(throwable.getMessage());
						}
						getRequest().getSession().setAttribute("scvToTRDashboard","1");
						//actionForm.setScvToTRDashboard("1");
						return technicalRegistrationDashboard();
					}
				}

			}
			siteRegistration = constructSiteRegistration( actionForm, RegistrationTypeEnum.TECHNICALREGISTRATIONONLY, siteRegistration);
			try {
				logger.debug("Defect #335 - before calling saveSiteRegistrationDetails: siteRegistration Sold To : "+fixCSS(siteRegistration.getSoldToId()));
				getBaseRegistrationService().saveSiteRegistrationDetails(siteRegistration);
				getRequest().getSession().setAttribute("scvToTRDashboard","1");
				//actionForm.setScvToTRDashboard("1");
				return technicalRegistrationDashboard();
			} catch(Throwable throwable) {
				throwable.getStackTrace();
				logger.error("Error while handling TR only flow", throwable);
				setErrorMessage(throwable.getMessage());
				return backToNewRegistration( actionForm );
			}
			//}
		} else if (actionForm.isEquipmentRegistrationOnly()) {
			getRequest().getSession().removeAttribute("eqrFlag");
			actionForm.setSaveSiteReg(true);
			//TODO
			//return new Forward("eqrOnly", actionForm);
		}else if(actionForm.isEquipmentMoveOnly()){
				actionForm.setSaveSiteReg(true);
		//TODO
		//return new Forward("eqrOnly", actionForm);
		}else if (actionForm.isSalMigrationOnly()) {
			return salGatewayMigrationList();
			//TODO
			//return new Forward("salMigrationOnly", actionForm);
		} else if (actionForm.getInstallbaseRegistrationOnly()){
			//This else block is added to set the SaveSiteReg to true to keep the Full Registration and InstallBaseOnly in sync.
			actionForm.setSaveSiteReg(true);
		} else if (actionForm.isFullRegistrationOnly()){
			actionForm.setSaveSiteReg(true);
			//Direct Customer Check
			CSSPortalUser cssPortalUser = getUserFromSession();
			String userType  = cssPortalUser.getUserType();
			//logger.debug("userType =====>:"+userType+"   userRole =====>:"+actionForm.getUserRole());
			if (userType != null && (userType.equalsIgnoreCase("C"))) { 
				//|| (actionForm.getUserRole() != null && actionForm.getUserRole().equals("C")
				// && !(userType.equalsIgnoreCase("C") && userType.equalsIgnoreCase("B"))))){
				logger.debug("RegistrationController : saveRegistration*****************************************Full Registration only");
				actionForm.setSaveSiteReg(false);
				SiteRegistration siteRegistration = null;
				siteRegistration = constructSiteRegistration( actionForm, RegistrationTypeEnum.FULLREGISTRATION, null);
				//Begin: Populate the TechRegStatus
				Status status = new Status();
				status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
				siteRegistration.setTechRegStatus(status);
				//End: Populate the TechRegStatus
				try {
					getBaseRegistrationService().saveSiteRegistrationDetails(siteRegistration);
					logger.debug("*****************************FORWARDED TO TECHNICAL REGISTRATION DASH BOARD**********************");
					getRequest().getSession().setAttribute("scvToTRDashboard","1");
					//actionForm.setScvToTRDashboard("1");
					//TODO 
					return technicalRegistrationDashboard();
				} catch(Exception exception) {
					logger.error("Error while handling Full Registration only flow", exception);
					setErrorMessage(exception.getMessage());
					return backToNewRegistration( actionForm );
				}
			}
		}
		getRequest().getSession().setAttribute("IBBack","scv");
		//Saving the same in action form for UI
		actionForm.setIBBack("scv");
		
		actionForm.setSessionAttr("scv");
		//logger.debug("BEFORE RETURN TO installBaseCreation USRETYPE"+actionForm.getUserRole());
		logger.debug("Sending to installbase creation from action saveSiteRegistration "+actionForm.getInstallBaseCreationReadOnlyFlag());
		logger.debug("Exiting saveSiteRegistration()");
		return installBaseCreation();
	}

	/* START ::  Abstract Methods*/

	public abstract  String technicalRegistrationDashboard();

	public abstract String installBaseCreation();
	
	public abstract String salGatewayMigrationList();

	/* END ::  Abstract Methods*/

	/**
	 * @param form
	 * @param registrationType
	 * @return SiteRegistration
	 * @description Create site registration object from form bean
	 */
	protected SiteRegistration constructSiteRegistration(RegistrationFormBean form, RegistrationTypeEnum registrationType, SiteRegistration siteRegistration){
		if( siteRegistration == null ){
			siteRegistration = new SiteRegistration();
		}
		siteRegistration.setRegistrationId(form.getRegistrationId());
		logger.debug("constructSiteRegistration: action Form Sold To : "+form.getSoldToId());
		siteRegistration.setSoldToId(form.getSoldToId());
		siteRegistration.setAddress(form.getAddress1());
		siteRegistration.setAddress2(form.getAddress2());
		siteRegistration.setCity(form.getCity());
		siteRegistration.setState(form.getState());
		siteRegistration.setZip(form.getZip());
		siteRegistration.setSiteCountry(form.getSiteCountry());
		siteRegistration.setFirstName(form.getFirstName());
		siteRegistration.setLastName(form.getLastName());
		siteRegistration.setReportEmailId(form.getReportedEmail());
		siteRegistration.setReportPhone(form.getReportedPhone());
		siteRegistration.setOnsiteFirstName(form.getOnsiteFirstname());
		siteRegistration.setOnsiteLastName(form.getOnsiteLastname());
		siteRegistration.setOnsitePhone(form.getOnsitePhone());
		siteRegistration.setCompanyPhone(form.getCompanyPhone());
		siteRegistration.setRegion(form.getRegion());
		siteRegistration.setCompany(form.getCompany());
		siteRegistration.setOnsiteEmail(form.getOnsiteEmail());
		siteRegistration.setSendMail(form.getSendMail());
		siteRegistration.setUserName(form.getUserId());
		siteRegistration.setExpedite(form.getExpedaite());
		//To-Do get the sold to location from sales out table.
		siteRegistration.setSoldToLocation("SoldToLocationTesting");
		//To-Do get the sold to Type from sales out table.
		siteRegistration.setSoldToType("SoldToTypeTesting");

		siteRegistration.setSubmitted(form.getSubmitted());
		siteRegistration.setUserRole(form.getUserRole());
		siteRegistration.setIsSrCompleted(form.getIsSrCompleted());
		siteRegistration.setRegistrationIdentifier(form.getRegistrationIdentifier());
		siteRegistration.setRegistrationNotes(fixCSS((form.getRegistrationNotes())));
		// Update Created date and created by / Updated date and updated by
		siteRegistration.setCreatedDate(new Date());
		siteRegistration.setUpdatedDate(new Date());
		CSSPortalUser user = getUserFromSession();
		siteRegistration.setCreatedBy(user.getUserId());
		siteRegistration.setUpdatedBy(user.getUserId());

		ProcessStep    processStep = new ProcessStep();
		Status status = new Status();
		RegistrationType regType = new RegistrationType();
		if (registrationType.equals(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY)) {
			processStep.setProcessStepId(GRTConstants.TECHNICAL_REGISTRATION);
			status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
			regType.setRegistrationId(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY.getRegistrationID());
		} else if (registrationType.equals(RegistrationTypeEnum.EQUIPMENTREMOVALONLY)) {
			processStep.setProcessStepId(GRTConstants.FINAL_RECORD_VALIDATION);
			// Update the Status
			logger.debug("SaveType:  "+form.getSaveType());
			/* Commented as we are going to save status as "SAVED" and
			 * for submitted request if request successful status is updated to "In Process"
			 * if(StringUtils.isNotEmpty(form.getSaveType()) && form.getSaveType().equalsIgnoreCase(GRTConstants.SAVE)){
				status.setStatusId(StatusEnum.SAVED.getStatusId());
			} else {
				status.setStatusId(StatusEnum.INPROCESS.getStatusId());
			}*/
			if(StringUtils.isNotEmpty(form.getSaveType()) && form.getSaveType().equalsIgnoreCase(GRTConstants.VALIDATE)){
				status.setStatusId(StatusEnum.VALIDATED.getStatusId());
				siteRegistration.setFinalValidationStatus(status);
			} else {

				status.setStatusId(StatusEnum.SAVED.getStatusId());
				siteRegistration.setFinalValidationStatus(status);
			}
			regType.setRegistrationId(RegistrationTypeEnum.EQUIPMENTREMOVALONLY.getRegistrationID());
		} else if (registrationType.equals(RegistrationTypeEnum.SALMIGRATION)) {
            status.setStatusId(GRTConstants.IN_PROCESS);
			//status.setStatusId(StatusEnum.SAVED.getStatusId());
            regType.setRegistrationId(RegistrationTypeEnum.SALMIGRATION.getRegistrationID());
            processStep.setProcessStepId(GRTConstants.TECHNICAL_REGISTRATION);
        } else if (registrationType.equals(RegistrationTypeEnum.FULLREGISTRATION)) {
            status.setStatusId(GRTConstants.IN_PROCESS);
            regType.setRegistrationId(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID());
            processStep.setProcessStepId(GRTConstants.TECHNICAL_REGISTRATION);
			siteRegistration.setCreatedBy(GRTConstants.USER_ID_SYSTEM);
			siteRegistration.setUpdatedBy(GRTConstants.USER_ID_SYSTEM);
        }else if (registrationType.equals(RegistrationTypeEnum.EQUIPMENTMOVEONLY)) { //Added for GRT 4.0
			processStep.setProcessStepId(GRTConstants.EQUIPMENT_MOVE);
			// Update the Status
			logger.debug("SaveType:  "+form.getSaveType());
			status.setStatusId(StatusEnum.SAVED.getStatusId());
			siteRegistration.setEqrMoveStatus(status);
			regType.setRegistrationId(RegistrationTypeEnum.EQUIPMENTMOVEONLY.getRegistrationID());
			
			//If ToSoldTo is present, fetch the To Customer Name and set it in Site Registration
			if(form.getEqmToSoldTo() != null && !form.getEqmToSoldTo().trim().isEmpty()) {
				String toSoldToIdVal = form.getEqmToSoldTo();
				toSoldToIdVal = StringEscapeUtils.unescapeHtml(toSoldToIdVal);
				try {
					Account account = getSiebelClient().queryAccount(toSoldToIdVal);
					if(account != null) {
						logger.debug(" To Account Name -- For sold to : " + toSoldToIdVal +" -->  " + account.getName()) ;
						siteRegistration.setToCustomerName(account.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		} 

		siteRegistration.setProcessStep(processStep);
		siteRegistration.setStatus(status);
		siteRegistration.setRegistrationType(regType);
		//GRT 4.0 Change: Add TO-SOLDTO for Equipment Move
		siteRegistration.setToSoldToId(form.getEqmToSoldTo());
		logger.debug("constructSiteRegistration: siteRegistration Sold To : "+siteRegistration.getSoldToId());
		return siteRegistration;
	}
	
	protected List<TechnicalOrderDetail> prepareTechnicalOrderMaterialExclusion(
			String soldToId, boolean fetchSALGWRecords) throws Exception, DataAccessException {
		List<TechnicalOrderDetail> technicalOrderDetailList = null;
		// Fetching Equipment data from Siebel
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Fetch View Install Base records from Siebel and run over Material Exclusion");
		try {
			// TODO service call
			technicalOrderDetailList = getInstallBaseService()
					.fetchEquipmentRemovalRecords(soldToId, "", fetchSALGWRecords);
		} catch (Exception daexception) {
			throw new DataAccessException(InstallBaseCreationAction.class,
					daexception.getMessage(), daexception);
		}
		logger.debug("View Install Base equipmentRemovalProcessList size: "
				+ technicalOrderDetailList.size());
		if (technicalOrderDetailList != null
				&& technicalOrderDetailList.size() > 0) {
			List<String> materialCodeList = new ArrayList<String>();
			for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
				if (!materialCodeList.contains(technicalOrderDetail
						.getMaterialCode()))
					materialCodeList
					.add(technicalOrderDetail.getMaterialCode());
			}
			// Check for the existence of the material codes in Exclusion table
			if (materialCodeList != null && materialCodeList.size() > 0) {
				//Defect#367 : ODS feed code : need to be uncommented when master_material table update successfully
				/*Map<String, Object> materialMasterCodeMap = getBaseRegistrationService()
						.populateMaterialSerialize(materialCodeList);*/
				
				Map<String, Object> materialCodeMap = getInstallBaseService()
						.validateMaterialExclusion(materialCodeList);
				// Removing the Technical orders from the technical order list
				// which are in the exclusion table
				// if(materialCodeList != null && materialCodeList.size() > 0){
				if (materialCodeMap != null && materialCodeMap.size() > 0) {
					logger.debug("View Install Base MaterialCodes to be excluded size: "
							+ materialCodeList.size());
					/*
					 * Iterator todIter = technicalOrderDetailList.iterator();
					 * while(todIter.hasNext()){
					 */
					for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
						//Defect#367 : ODS feed code : need to be uncommented when master_material table update successfully
						/*if (materialMasterCodeMap.containsKey(technicalOrderDetail
								.getMaterialCode())) {
							if (materialMasterCodeMap.get(technicalOrderDetail.getMaterialCode()) != null) {
								technicalOrderDetail.setSerialized("Y");
							} else {
								technicalOrderDetail.setSerialized("N");
							}
						}*/
						
						
						if (materialCodeMap.containsKey(technicalOrderDetail
								.getMaterialCode())) {
							// todIter.remove();
							technicalOrderDetail
							.setExclusionSource((String) materialCodeMap
									.get(technicalOrderDetail
											.getMaterialCode()));
							technicalOrderDetail.setExclusionFlag(true);
							if (StringUtils.isNotEmpty(technicalOrderDetail
									.getExclusionSource())
									&& GRTConstants.EXCLUSION_SOURCE_PLDS
									.equalsIgnoreCase(technicalOrderDetail
											.getExclusionSource())) {
								technicalOrderDetail
								.setErrorDescription( grtConfig.getExclPldsErrMsg() );
							} 
							
							/*GRT4.0 changes for ESNA code --starts */
							else if (StringUtils.isNotEmpty(technicalOrderDetail
									.getExclusionSource())
									&& GRTConstants.EXCLUSION_SOURCE_ESNA
									.equalsIgnoreCase(technicalOrderDetail
											.getExclusionSource())) {
								logger.debug("***Enter ESNA condition***"+grtConfig.getExclEsnaErrMsg());
								technicalOrderDetail
								.setErrorDescription( grtConfig.getExclEsnaErrMsg() );
							} 
							/*GRT4.0 changes for ESNA Code--ends */
							
							
							
							/*GRT4.0 changes for defective and blue code --starts */
							else if (StringUtils.isNotEmpty(technicalOrderDetail
									.getExclusionSource())
									&& GRTConstants.EXCLUSION_SOURCE_DEFECTIVE
									.equalsIgnoreCase(technicalOrderDetail
											.getExclusionSource())) {
								technicalOrderDetail
								.setErrorDescription( grtConfig.getDefectiveCodeErrMsg() );
							} 
							else if (StringUtils.isNotEmpty(technicalOrderDetail
									.getExclusionSource())
									&& GRTConstants.EXCLUSION_SOURCE_BLUE
									.equalsIgnoreCase(technicalOrderDetail
											.getExclusionSource())) {
								technicalOrderDetail
								.setErrorDescription( grtConfig.getBlueCodeErrMsg() );
							} 
							/*GRT4.0 changes for defective and blue code --ends */
							else {
								technicalOrderDetail
								.setErrorDescription(  grtConfig.getOfferCodeErrMsg() );
							}
							
							if(GRTConstants.EXCLUSION_SOURCE_NMPC
									.equalsIgnoreCase(technicalOrderDetail
											.getExclusionSource())){
								technicalOrderDetail.setErrorDescription("");
								technicalOrderDetail.setExclusionFlag(false);
							}
							// technicalOrderDetail.setErrorDescription("Material Code is excluded because of exclusion source :"+technicalOrderDetail.getExclusionSource());
						}
					}
				}
			}
		}
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for Fetch View Install Base records from Siebel and run over Material Exclusion, time in milliseconds :"
				+ (c2 - c1));
		return technicalOrderDetailList;
	}
	
	protected boolean isMaterialCodeSerialized(String materialCode) {
		if(materialMasterMap != null) {
			String serialized = (String) materialMasterMap.get(materialCode);			
			if(GRTConstants.SERIALIZED_STATUS_LIST.contains(serialized)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to club the quantity of the equipments having same
	 * materialCode/materialCode+equipmentNumber conditionally
	 */
	protected List<TechnicalOrderDetail> constructTechnicalOrderDetailList(
			List<TechnicalOrderDetail> technicalOrderDetailList, int flag)
					throws Exception {
		Map<String, TechnicalOrderDetail> map = new HashMap<String, TechnicalOrderDetail>();
		String key = null;
		TechnicalOrderDetail tempDto = null;
		long qty = 0;
		
		//Defect#311-Check the material code against material master table to find the whether the mc serialized			
		
		List<String> materialCodes = new ArrayList<String>();
		for(TechnicalOrderDetail detail: technicalOrderDetailList){
			materialCodes.add(detail.getMaterialCode());	
		}
		if( materialCodes!=null && !materialCodes.isEmpty() ){
			materialMasterMap = getInstallBaseService().populateMaterialSerialize(materialCodes);
		}
		
		for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
			if (flag == 0) {
				key = technicalOrderDetail.getMaterialCode() + ":"
						+ technicalOrderDetail.getSummaryEquipmentNumber();
			} else {
				key = technicalOrderDetail.getMaterialCode();
			}			
			//GRT 4.0 change
			if(isMaterialCodeSerialized(technicalOrderDetail.getMaterialCode())) {
				technicalOrderDetail.setSerialized(GRTConstants.Y);
			} else {
				technicalOrderDetail.setSerialized(GRTConstants.N);
			}
			
			// logger.debug("MC:"+technicalOrderDetail.getMaterialCode()+" EQN:"+technicalOrderDetail.getEquipmentNumber()+"  TR:"+technicalOrderDetail.getTechnicallyRegisterable()+"  SR:"+technicalOrderDetail.getSerialNumber()+"  IQ:"+technicalOrderDetail.getInitialQuantity());
			if (technicalOrderDetail.getTechnicallyRegisterable()
					.equalsIgnoreCase(GRTConstants.YES)) {
				// logger.debug("TRed or Record with Serial Number");
				key = technicalOrderDetail.getMaterialCode() + ":"
						+ technicalOrderDetail.getEquipmentNumber() + ":"
						+ technicalOrderDetail.getSolutionElementId();
				map.put(key, constructTechnicalOrderDetailClone(technicalOrderDetail));
			} else if ((technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber())) 
					|| (GRTConstants.Y.equalsIgnoreCase(technicalOrderDetail.getSerialized()))){
				key = technicalOrderDetail.getMaterialCode() + ":"
						+ technicalOrderDetail.getEquipmentNumber() + ":"
						+ technicalOrderDetail.getSerialNumber();
				map.put(key, constructTechnicalOrderDetailClone(technicalOrderDetail));
			} else if (map.size() > 0 && map.containsKey(key)) {
				// logger.debug("Already existing record in MAP");
				tempDto = constructTechnicalOrderDetailClone(map.get(key));
				qty = tempDto.getInitialQuantity()
						+ technicalOrderDetail.getInitialQuantity();
				tempDto.setInitialQuantity(qty);
				
				if(null != tempDto.getRegStatus() &&  StringUtils.isEmpty(tempDto.getRegStatus()) &&
						null != technicalOrderDetail.getRegStatus() &&  StringUtils.isNotEmpty(technicalOrderDetail.getRegStatus()))
				{
					tempDto.setRegStatus(technicalOrderDetail.getRegStatus());
				}
				if(null != tempDto.getNickName() && StringUtils.isEmpty(tempDto.getNickName()) && 
						null != technicalOrderDetail.getNickName() &&  StringUtils.isNotEmpty(technicalOrderDetail.getNickName()))
				{
					tempDto.setNickName(technicalOrderDetail.getNickName());
				}
				tempDto.setSerialized(technicalOrderDetail.getSerialized());
				
				//GRT 4.0 Changes Defect 327-328
				tempDto.setIsSAP(technicalOrderDetail.getIsSAP());
				tempDto.setIsNortel(technicalOrderDetail.getIsNortel());
				tempDto.setIsMaestro(technicalOrderDetail.getIsMaestro());
				/*
				 * tempDto.setPipelineEQRQuantity((tempDto.getPipelineEQRQuantity
				 * ()!= null? tempDto.getPipelineEQRQuantity() : 0L) +
				 * (technicalOrderDetail
				 * .getPipelineEQRQuantity()!=null?technicalOrderDetail
				 * .getPipelineEQRQuantity() : 0L));
				 * tempDto.setPipelineIBQuantity
				 * ((tempDto.getPipelineIBQuantity() != null ?
				 * tempDto.getPipelineIBQuantity() : 0L) +
				 * (technicalOrderDetail.getPipelineIBQuantity() != null ?
				 * technicalOrderDetail.getPipelineIBQuantity() : 0L));
				 */
				map.put(key, tempDto);
			} else {
				// logger.debug("Plain record");
				map.put(key,
						constructTechnicalOrderDetailClone(technicalOrderDetail));
			}
		}
		List<TechnicalOrderDetail> processedList = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail techDto = null;
		for (Map.Entry<String, TechnicalOrderDetail> dto : map.entrySet()) {
			techDto = dto.getValue();
			techDto.setPipelineEQRQuantity(0L);
			techDto.setPipelineIBQuantity(0L);
			processedList.add(techDto);
		}
		return processedList;
	}
	
	protected TechnicalOrderDetail constructTechnicalOrderDetailClone(
			TechnicalOrderDetail todDto) {
		TechnicalOrderDetail technicalOrderDetail = null;
		if (todDto != null) {
			technicalOrderDetail = new TechnicalOrderDetail();
			technicalOrderDetail.setDeleted(todDto.getDeleted());
			technicalOrderDetail.setAssetPK(todDto.getAssetPK());
			technicalOrderDetail.setAssetNumber(todDto.getAssetNumber());
			technicalOrderDetail
			.setEquipmentNumber(todDto.getEquipmentNumber());
			technicalOrderDetail.setSummaryEquipmentNumber(todDto
					.getSummaryEquipmentNumber());
			technicalOrderDetail.setMaterialCode(todDto.getMaterialCode());
			technicalOrderDetail.setDescription(todDto.getDescription());
			technicalOrderDetail
			.setInitialQuantity(todDto.getInitialQuantity());
			if (todDto.getRemainingQuantity() != null)
				technicalOrderDetail.setRemainingQuantity(todDto
						.getRemainingQuantity());
			if (todDto.getRemovedQuantity() != null)
				technicalOrderDetail.setRemovedQuantity(todDto
						.getRemovedQuantity());
			if (todDto.getPipelineEQRQuantity() != null) {
				technicalOrderDetail.setPipelineEQRQuantity(todDto
						.getPipelineEQRQuantity());
			} else {
				technicalOrderDetail.setPipelineEQRQuantity(0L);
			}
			if (todDto.getPipelineIBQuantity() != null) {
				technicalOrderDetail.setPipelineIBQuantity(todDto
						.getPipelineIBQuantity());
			} else {
				technicalOrderDetail.setPipelineIBQuantity(0L);
			}
			technicalOrderDetail.setSolutionElementId(todDto
					.getSolutionElementId());
			technicalOrderDetail.setSolutionElementCode(todDto
					.getSolutionElementCode());
			technicalOrderDetail.setTechnicallyRegisterable(todDto
					.getTechnicallyRegisterable());
			technicalOrderDetail.setActiveContractExist(todDto
					.getActiveContractExist());
			technicalOrderDetail.setProductLine(todDto.getProductLine());
			technicalOrderDetail.setSerialNumber(todDto.getSerialNumber());
			technicalOrderDetail.setSid(todDto.getSid());
			technicalOrderDetail.setMid(todDto.getMid());
			technicalOrderDetail
			.setExclusionSource(todDto.getExclusionSource());
			technicalOrderDetail.setExclusionFlag(todDto.isExclusionFlag());
			technicalOrderDetail.setErrorDescription(todDto
					.getErrorDescription());
			technicalOrderDetail.setSalGateway(todDto.isSalGateway());
			technicalOrderDetail.setSerialized(todDto.getSerialized());
			technicalOrderDetail.setActionType(todDto.getActionType());
			technicalOrderDetail.setNewSerialNumber(todDto.getNewSerialNumber());
			technicalOrderDetail.setRegStatus(todDto.getRegStatus());
			technicalOrderDetail.setNickName(todDto.getNickName());
			technicalOrderDetail.setErrorFlag(todDto.isErrorFlag());
			technicalOrderDetail.setOriginalSerialNumber(todDto.getOriginalSerialNumber());
			
			//GRT 4.0 Changes Defect 327-328
			technicalOrderDetail.setIsSAP(todDto.getIsSAP());
			technicalOrderDetail.setIsNortel(todDto.getIsNortel());
			technicalOrderDetail.setIsMaestro(todDto.getIsMaestro());
		}
		return technicalOrderDetail;
	}

	public String backToNewRegistration(RegistrationFormBean form) throws Exception {
		validateSession();
		getRequest().setAttribute("needEPN", "Y");
		actionForm.setNeedEPN("Y");
		actionForm.setNavigateTo("newRegistration");
		return "back";
	}

	public Pager initPager(int fetchSize) {
		logger.debug("initPager set offSet = 0 and fetchSize = " + fetchSize);
		Pager pager = new Pager();
		pager.setOffSet(0);
		pager.setFetchSize(fetchSize);
		return pager;
	}

	/* START :: Service for registration page */
	/**
	 * @return String
	 * Get few records for registration list to be shown at page load
	 */
	public String registrationList() {
		//Updating actionForm message values
		actionForm.setErrorMessage(null);
		actionForm.setReturnCode(null);
		actionForm.setNavigateTo(GRTConstants.REGISTRATION_LIST_PAGE);
		CSSPortalUser user = getUserFromSession();
		boolean ipoFilter = false;
		try {
			requesterName = user.getFirstName()+" "+user.getLastName();
			if(null!=prFlag && prFlag.equals("PR")){
				//ipoFilter = "PR".equals((String)filter);
				ipoFilter=true;
			}
			
			//Get the lists for registration list page multiselect
			statusList = getBaseRegistrationService().getRegistrationStatus();
			
			regTypeList = getBaseRegistrationService().getRegistrationTypes();
			
			//Prepare JSON for UI functionality
			regListMap = new HashMap<String, Object>();
			//regListMap.put("registrationList", registrationSummaries);
			regListMap.put("statusList", statusList);
			regListMap.put("registrationTypeList", regTypeList);
			
		} catch (Exception e) {
			logger.debug("Error in registrationList : " + e.getMessage());
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}

		return Action.SUCCESS;
	}
	
	/**
	 * @return String
	 * Get all the data for registration list
	 */
	public String registrationListData(){
		CSSPortalUser user = getUserFromSession();
		registrationSummaries = new ArrayList<RegistrationSummary>();
		boolean ipoFilter = false;
		try {
			requesterName = user.getFirstName()+" "+user.getLastName();
			if(null!=prFlag && prFlag.equals("PR")){
				//ipoFilter = "PR".equals((String)filter);
				ipoFilter=true;
			}
			createMapFromDataTableJson();
			//check whether any filter condition is there or not
			boolean makeDbCall = checkForFilterCondition(regListParameterMap, user, requesterName);
			PaginationForSiteRegistration dto = new PaginationForSiteRegistration();
			if( makeDbCall ){
				//Get the registration list count
				//int startIndex = pageNo * grtConfig.getRegistrationListRowNum();
				dto = getBaseRegistrationService().getRegListFromDB(regListParameterMap, user.getUserId(), requesterName, 
						user.getUserType(), user.getBpLinkId(), ipoFilter, maxResults,  startIndex, allRegData);
				registrationSummaries = dto.getSummaryList();
			}
			//Build the registration list table
			constructRegistrationListTable( registrationSummaries, user.getUserType() );
			if( registrationSummaries!=null ){
				System.out.println("List Created - "+registrationSummaries.size());
			}
			
			//Prepare JSON for UI functionality
			regListMap = new HashMap<String, Object>();
			
			regListMap.put("sEcho", regListParameterMap.get("sEcho"));
			regListMap.put("iTotalRecords", dto.getMaxSize());
			regListMap.put("iTotalDisplayRecords", dto.getMaxSize());
			regListMap.put("aaData", registrationSummaries);
			
		} catch (Exception e) {
			logger.debug("Error in registrationListData : " + e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	/**
	 * @param parameterMap
	 * @param userDetail
	 * @param requesterFullName
	 * @return boolean
	 * Check whether there are any filters present or not
	 */
	public boolean checkForFilterCondition(Map<String, Object> parameterMap, CSSPortalUser userDetail, String requesterFullName){
		boolean filterFound = false;
		String userType = userDetail.getUserType();
		if(parameterMap != null){
			System.out.println("parameterMap:" + parameterMap);
			if(parameterMap.size() > 0) {
				try {
					boolean clearClicked = false;
					String filterOrClearBtn = (String) parameterMap.get(GRTConstants.FILTER_OR_CLEAR_BTN);
							if(StringUtils.isNotEmpty(filterOrClearBtn) && filterOrClearBtn.equalsIgnoreCase("clear")) {
									clearClicked = true;
							}
					System.out.println("clearClicked:" + clearClicked);
					System.out.println("userType.equalsIgnoreCase(GRTConstants.AGENT):" + userType.equalsIgnoreCase(GRTConstants.AGENT));
					if((!clearClicked && userType.equalsIgnoreCase(GRTConstants.AGENT)) || !userType.equalsIgnoreCase(GRTConstants.AGENT)) {
						System.out.println("inside");
						for (Object key : parameterMap.keySet()) {
							if(((String)key).endsWith("_f") && !((String)key).equalsIgnoreCase(GRTConstants.FILTER_OR_CLEAR_BTN)) {
								String value = (String) parameterMap.get(key);
									if(StringUtils.isNotEmpty(value)) {
											filterFound = true;
											break;
									} 
							 }
						}
					}
					System.out.println("filterFound:" + filterFound);
					if(StringUtils.isNotEmpty(userType) && userType.equalsIgnoreCase(GRTConstants.AGENT)) {
						if(!filterFound) {
							return false; //Return empty list if no filter selected
						}
					}
					if(StringUtils.isNotEmpty(userType) && userType.equalsIgnoreCase(GRTConstants.USER_TYPE_BP) && (!filterFound || clearClicked)) {
						if(clearClicked){
							parameterMap.put(GRTConstants.REGISTRATION_ID_FILTER, "");
							//parameterMap.put("assembler_f_RequesterName", "");
							parameterMap.put(GRTConstants.GRT_NOTIFICATION_NAME_FILTER, "");
							parameterMap.put(GRTConstants.GRT_NOTIFICATION_NAME_FILTER, "");
							parameterMap.put(GRTConstants.CREATED_DATE_FILTER, "");
							parameterMap.put(GRTConstants.LAST_UPDATED_DATE_FILTER, "");
							parameterMap.put(GRTConstants.LAST_UPDATED_BY_FILTER, "");
							parameterMap.put(GRTConstants.REGISTRATION_TYPE_FILTER, "");
							parameterMap.put(GRTConstants.IB_STATUS_FILTER, "");
							parameterMap.put(GRTConstants.TOB_STATUS_FILTER, "");
							parameterMap.put(GRTConstants.EQR_STATUS_FILTER, "");
							parameterMap.put(GRTConstants.SOLD_TO_FILTER, "");
							parameterMap.put(GRTConstants.CUST_NAME_FILTER, "");
							parameterMap.put(GRTConstants.ACTIVE_REL_SR_FILTER, "");
						}
						parameterMap.put(GRTConstants.REQUESTER_NAME_FILTER, requesterFullName);
						parameterMap.put(GRTConstants.FILTER_OR_CLEAR_BTN, "");
					}
					return true; //Make db call for getting registration list data
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * Process the filter/sorting/pagination pagination object
	 * created by datatable and convert it to corresponding map
	 */
	public void createMapFromDataTableJson(){
		regListParameterMap = new HashMap<String, Object>();
		boolean noFilters = true;
		if( dataTableParams != null ){
			/*//Handle for 1st Time table load for default filters
			String firstTimeLoad = (String) dataTableParams.get("firstTimeLoad");
			if( (firstTimeLoad!=null && !StringUtils.isEmpty(firstTimeLoad)) && actionForm.getRegistrationId()!=null ){
				regListParameterMap.put( GRTConstants.REGISTRATION_ID_FILTER, actionForm.getRegistrationId() );
				noFilters = false;
			} */
			
			//Populate Map with Filtering data
			for( int index = 0; index < dataTableParams.size(); index++ ){
				String colName = (String) dataTableParams.get("mDataProp_"+index);
				String filterVal = (String) dataTableParams.get("sSearch_"+index);
				if( !StringUtils.isEmpty(colName) && !StringUtils.isEmpty(filterVal) ){
					regListParameterMap.put( colName+"_f", filterVal );
					noFilters = false;
				}
			}
			//Populate Map with Sorting data
			Long colIndex = (Long) dataTableParams.get("iSortCol_0");
			int colIndexInt = colIndex.intValue();
			String order = (String) dataTableParams.get("sSortDir_0");
			String sortCol = (String) dataTableParams.get("mDataProp_"+colIndexInt);
			regListParameterMap.put( sortCol+"_s", order );
			
			//Populate Map with Pagination data
			int start = ((Long) dataTableParams.get("iDisplayStart")).intValue();
			int length = ((Long) dataTableParams.get("iDisplayLength")).intValue();
			regListParameterMap.put( GRTConstants.REC_TO_DISPLAY, length );
			regListParameterMap.put( GRTConstants.CURRENT_PAGE_NO, start );
			regListParameterMap.put( GRTConstants.FILTER_OR_CLEAR_BTN, "" );
			regListParameterMap.put( "sEcho", dataTableParams.get("sEcho") );
			regListParameterMap.put( "filterValue", dataTableParams.get("filterValue") );
			
			if( noFilters == false ){//only add the cancelled filter if there are other filters selected
				//Add Exclude Cancelled Registration filter value
				regListParameterMap.put( GRTConstants.EX_CANCELLED_STATUS_FILTER, dataTableParams.get(GRTConstants.EX_CANCELLED_STATUS_FILTER) );
			}
		}
	}
	
	/**
	 * @return String
	 * Update the email field for the registration list page
	 */
	public String updateEmail(){
		
		try {
			RegistrationSummary rs = getBaseRegistrationService().updateEmail(registrationId, onsiteEmail);
			setServiceMsg(grtConfig.getEwiMessageCodeMap().get("updateEmailMsg") + "###" + grtConfig.getUpdateEmailMsg()); //set the success messsage
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return Action.SUCCESS;
	}
	
	public String emailOnBoardingFile(){
		serviceMsg = grtConfig.getEwiMessageCodeMap().get("onboardFileMsg") + "###" + grtConfig.getOnboardFileMsg() + emailId;
		try {
			logger.debug("+++++++++registrationId+++++" + registrationId);
			logger.debug("+++++++++emailId+++++" + emailId);

			boolean messageSent = getBaseRegistrationService().emailOnBoardingFile(registrationId, emailId, grtConfig.getService_name());
			if(!messageSent){
				//serviceMsg = "Error while sending On-boarding file request and instructions to:" + emailId + ", Please try again.";
				serviceMsg = grtConfig.getEwiMessageCodeMap().get("onboardFileErrMsg") + "###" + grtConfig.getOnboardFileErrMsg() + emailId + grtConfig.getPleaseTryAgainErrMsg();
			}
		} catch(Throwable throwable) {
			 logger.error("Error while sending OnBoarding XML file", throwable);
			 //serviceMsg = "Error while On-boarding file request and instructions to:" + emailId + ", Please try again.";
			 serviceMsg = grtConfig.getEwiMessageCodeMap().get("onboardFileErrMsg") + "###" + grtConfig.getOnboardFileErrMsg() + emailId + grtConfig.getPleaseTryAgainErrMsg();
		 }
		logger.debug("Message to display in popUp : " + serviceMsg);
		
		return Action.SUCCESS;
	}
	
	/* END :: Service for registration page */
	
	/**
	 * @param registrationSummaryList
	 * This methods updates the registration summary objects to be displayed on  
	 *  registration list page
	 */
	public void constructRegistrationListTable( List<RegistrationSummary> registrationSummaryList, String userType){
		if (registrationSummaryList != null && registrationSummaryList.size() > 0) {
			long time3 = System.currentTimeMillis();
			for (RegistrationSummary registrationSummary : registrationSummaryList) {
				// Flags introduced for override button display
				boolean flagIB = false;
				boolean flagTOB = false;
				boolean flagEQR = false;
				boolean flagEQM = false;
				Map<String, Object> map = new HashMap<String, Object>();
				String registrationId = registrationSummary.getRegistrationId();
				String processStepId = null;
				String statusId = null;
				String soldToId = registrationSummary.getSoldTo();
				soldToId = StringEscapeUtils.escapeHtml(soldToId);
				registrationSummary.setSoldTo(soldToId);
				String installBaseStatus = registrationSummary.getInstallBaseStatus();
				String techRegStatus = registrationSummary.getTechRegStatus();
				String finalValidStatus = registrationSummary.getFinalValidationStatus();
				String buttonStatus = ""; // Register button status
				String ipoButtonStatus = ""; // IPO button status
				String disabled = "disabled='true'";
				String regButton = "";
				String superButton = "";
				String ipoButton ="";
				String spanString = "<span style='display:none;'>";
				String spanEndString = "</span>";
				
				Boolean ownedByCurrentUserFlag = registrationSummary.getOwnedByCurrentUser() == null? false : registrationSummary.getOwnedByCurrentUser();			
				//[AVAYA] GRT2.1 changes by Lavanya
				if (installBaseStatus != null && techRegStatus != null) {
				if(installBaseStatus.equals(StatusEnum.AWAITINGINFO.getStatusShortDescription())){
					processStepId = GRTConstants.INSTALL_BASE_CREATION;
					statusId = registrationSummary.getInstallBaseStatusId();
				}else if (installBaseStatus.equals(StatusEnum.INPROCESS.getStatusShortDescription())
						&& (techRegStatus.equals(StatusEnum.COMPLETED.getStatusShortDescription())||
								techRegStatus.equals(StatusEnum.NOTINITIATED.getStatusShortDescription()))) {
					processStepId = GRTConstants.INSTALL_BASE_CREATION;
					statusId = registrationSummary.getInstallBaseStatusId();
				} else if (installBaseStatus.equals(StatusEnum.INPROCESS.getStatusShortDescription())
						    && (techRegStatus.equals(StatusEnum.INPROCESS.getStatusShortDescription())
								|| (techRegStatus.equals(StatusEnum.AWAITINGINFO.getStatusShortDescription())))) {
					processStepId = GRTConstants.TECHNICAL_REGISTRATION;
					statusId = registrationSummary.getTechRegStatusId();
				} else if (installBaseStatus.equals(StatusEnum.COMPLETED.getStatusShortDescription())) {
					if(techRegStatus.equals(StatusEnum.COMPLETED.getStatusShortDescription())){
						processStepId = GRTConstants.FINAL_RECORD_VALIDATION;
						statusId = registrationSummary.getFinalValidationStatusId();
					}else{
					processStepId = GRTConstants.TECHNICAL_REGISTRATION;
					statusId = registrationSummary.getTechRegStatusId();
					}
				} else if (installBaseStatus.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusShortDescription())) {
					if(!techRegStatus.equalsIgnoreCase(StatusEnum.COMPLETED.getStatusShortDescription())){
						processStepId = GRTConstants.TECHNICAL_REGISTRATION;
						statusId = registrationSummary.getTechRegStatusId();
					}
				} else if (finalValidStatus != null) {
					if (!finalValidStatus.equals(StatusEnum.NOTINITIATED.getStatusShortDescription())) {
						processStepId = GRTConstants.FINAL_RECORD_VALIDATION;
						statusId = registrationSummary.getFinalValidationStatusId();
					}
				}
			}
				String reqEmailElem = "<input type='text' value='"+registrationSummary.getOnsiteEmail()+"' id='email_"+registrationSummary.getRegistrationId()+"' /> <a href='#' class='updateEmailLink' ><img alt='Update Email' title='Click to Update Email' width='19px' height='19px' src='"+getRequest().getContextPath()+"/images/table/green_check.jpg' /></a>"+spanString+registrationSummary.getOnsiteEmail()+spanEndString; 
				registrationSummary.setRequestEmailUIElement(reqEmailElem);
				
				if(installBaseStatus != null)
				{
					getRequest().getSession().setAttribute("IBBack", "RegList");

				}
				// Added for GRT 3.0 creating hyperlinks for IB, TR, EQR status links to navigate to corresponding UI. - START
				if(registrationSummary.getRegistrationTypeDesc()!=null
						&& ((installBaseStatus!=null) && (installBaseStatus.equals(StatusEnum.INPROCESS.getStatusShortDescription()))
						||(installBaseStatus.equals(StatusEnum.AWAITINGINFO.getStatusShortDescription()))
							|| (installBaseStatus.equals(StatusEnum.COMPLETED.getStatusShortDescription()))
								||(installBaseStatus.equals(StatusEnum.SAVED.getStatusShortDescription()))
									||(installBaseStatus.equals(StatusEnum.CANCELLED.getStatusShortDescription())))
										&&(userType !=null && !GRTConstants.DIRECT_CUSTOMER.equalsIgnoreCase(userType))) {								//&&(userType.equalsIgnoreCase(GRTConstants.USER_TYPE_BP) || userType.equalsIgnoreCase(GRTConstants.AGENT))) {
					flagIB = true;
					String ibElem = "<a href='#' onClick=\"navigate('IB', '"+ installBaseStatus +"', '"+registrationId+"', '"+soldToId+"' ); \">"+ 
							registrationSummary.getInstallBaseStatus() + "</a>";
					registrationSummary.setInstallBaseStatusUIElement(ibElem);
					
				}
				else{
						registrationSummary.setInstallBaseStatusUIElement((StringUtils.isNotEmpty(registrationSummary.getInstallBaseStatus())?registrationSummary.getInstallBaseStatus():""));
				    }
				if((registrationSummary.getRegistrationTypeDesc())!=null && (techRegStatus!=null)
                        && (techRegStatus.equals(StatusEnum.INPROCESS.getStatusShortDescription())
                                || (techRegStatus.equals(StatusEnum.AWAITINGINFO.getStatusShortDescription()))
                                || (techRegStatus.equals(StatusEnum.COMPLETED.getStatusShortDescription()))
                                || (techRegStatus.equals(StatusEnum.SAVED.getStatusShortDescription()))
                                || (techRegStatus.equals(StatusEnum.CANCELLED.getStatusShortDescription()))/*
                                || (techRegStatus.equals(StatusEnum.NOTINITIATED.getStatusShortDescription()) && registrationSummary.getRegistrationTypeDesc().equals(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY.getRegistrationType()))*/
                                || ((registrationSummary.getRegistrationTypeDesc().equals(RegistrationTypeEnum.IPOFFICE.getRegistrationType())
                                              || registrationSummary.getRegistrationTypeDesc().equals(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationType()))
                                               /*&& (techRegStatus.equals(StatusEnum.NOTINITIATED.getStatusShortDescription()))*/
                                              && (installBaseStatus.equalsIgnoreCase(StatusEnum.COMPLETED.getStatusShortDescription())
                                                           || (registrationSummary.getInstallBaseSubStatus() != null && registrationSummary.getInstallBaseSubStatus().equalsIgnoreCase(StatusEnum.SAPCOMPLETED.getStatusShortDescription())))))) 
				 {
					flagTOB = true;
					String tobElem = "<a href='#' onClick=\"navigate('TR', '"+ techRegStatus +"', '"+registrationId+"', '"+soldToId+"' ); \">"+ registrationSummary.getTechRegStatus() + "</a>";
					registrationSummary.setTechRegStatusUIElement(tobElem);
				 }
				 else{
					 registrationSummary.setTechRegStatusUIElement((StringUtils.isNotEmpty(registrationSummary.getTechRegStatus())? registrationSummary.getTechRegStatus():"" ));
				 }
				finalValidStatus = registrationSummary.getFinalValidationStatus();
				if((registrationSummary.getRegistrationTypeDesc())!=null && ((finalValidStatus!=null) 
					&& ((finalValidStatus.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusShortDescription()))
					|| (finalValidStatus.equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusShortDescription()))
					|| (finalValidStatus.equalsIgnoreCase(StatusEnum.COMPLETED.getStatusShortDescription()))
					|| (finalValidStatus.equalsIgnoreCase(StatusEnum.SAVED.getStatusShortDescription()))
					|| (finalValidStatus.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusShortDescription()))
					|| (finalValidStatus.equalsIgnoreCase(StatusEnum.VALIDATED.getStatusShortDescription()))
					|| ((registrationSummary.getRegistrationTypeDesc().equalsIgnoreCase(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationType())) 
							&& (finalValidStatus.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusShortDescription())))))) {
					flagEQR = true;
					getRequest().getSession().setAttribute("eqrFlag", "0");
					String finalRvElem = "<a href='#' onClick=\"navigate('EQR', '"+ finalValidStatus +"', '"+registrationId+"', '"+soldToId+"'); \">"+ registrationSummary.getFinalValidationStatus() + "</a>";
					registrationSummary.setFinalRVStatusUIElement(finalRvElem);
				}
				else{
					registrationSummary.setFinalRVStatusUIElement((StringUtils.isNotEmpty(registrationSummary.getFinalValidationStatus())?registrationSummary.getFinalValidationStatus():"" ));
					
					//GRT 4.0 Changes
					if(finalValidStatus!=null && !finalValidStatus.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusShortDescription())){
						flagEQR = true;
						getRequest().getSession().setAttribute("eqrFlag", "0");
						String finalRvElem = "<a href='#' onClick=\"navigate('EQR', '"+ finalValidStatus +"', '"+registrationId+"', '"+soldToId+"'); \">"+ registrationSummary.getFinalValidationStatus() + "</a>";
						registrationSummary.setFinalRVStatusUIElement(finalRvElem);
					}
				}
				// Ended for GRT 3.0 creating hyperlinks for IB, TR, EQR status links to navigate to corresponding UI. - END
				
				//GRT 4.0 Change : Registration List
				String eqpMoveStatus = registrationSummary.getEqrMoveStatus();
				if((registrationSummary.getRegistrationTypeDesc())!=null && ((eqpMoveStatus!=null) 
					&& ((eqpMoveStatus.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusShortDescription()))
					|| (eqpMoveStatus.equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusShortDescription()))
					|| (eqpMoveStatus.equalsIgnoreCase(StatusEnum.COMPLETED.getStatusShortDescription()))
					|| (eqpMoveStatus.equalsIgnoreCase(StatusEnum.SAVED.getStatusShortDescription()))
					|| (eqpMoveStatus.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusShortDescription()))
					/*|| ((registrationSummary.getRegistrationTypeDesc().equalsIgnoreCase(RegistrationTypeEnum.EQUIPMENTMOVEONLY.getRegistrationType())) 
							&& (eqpMoveStatus.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusShortDescription())))*/))) {
					flagEQM = true;
					getRequest().getSession().setAttribute("eqrMoveFlag", "0");
					String eqrRvElem = "<a href='#' onClick=\"navigate('EQR_MOVE', '"+ eqpMoveStatus +"', '"+registrationId+"', '"+soldToId+"'); \">"+ registrationSummary.getEqrMoveStatus() + "</a>";
					registrationSummary.setEqrMoveUIElement(eqrRvElem);
				}
				else{
					registrationSummary.setEqrMoveUIElement((StringUtils.isNotEmpty(registrationSummary.getEqrMoveStatus())?registrationSummary.getEqrMoveStatus():"" ));
					
					//GRT 4.0 Changes
					if(eqpMoveStatus!=null && !eqpMoveStatus.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusShortDescription())){
						flagEQM = true;
						getRequest().getSession().setAttribute("eqrMoveFlag", "0");
						String eqrRvElemText = "<a href='#' onClick=\"navigate('EQR_MOVE', '"+ eqpMoveStatus +"', '"+registrationId+"', '"+soldToId+"'); \">"+ registrationSummary.getEqrMoveStatus() + "</a>";
						registrationSummary.setEqrMoveUIElement(eqrRvElemText);
					}
				
				}
				
				
				String activeSR = registrationSummary.getActiveSR();
				if(activeSR == null) {
					activeSR = "";
				}
				logger.debug("ActiveSR-regID:" + registrationSummary.getRegistrationId());
				logger.debug("ActiveSR-techRegStatus:" + techRegStatus);
				logger.debug("ActiveSR-installBaseStatus:" + installBaseStatus);
				logger.debug("ActiveSR-finalValidStatus:" + finalValidStatus);
				if(StringUtils.isNotEmpty(activeSR)){
					logger.debug("ActiveSR-activeSR:" + activeSR);
				}
				if(StringUtils.isNotEmpty(registrationSummary.getSrCompleted())){
					logger.debug("ActiveSR-IsSRCompleted:" + registrationSummary.getSrCompleted());
				}
				String activeSRArray[] = activeSR.split(",");
				if(activeSRArray.length == 2){
					if(constructActiveSRLink(registrationSummary, activeSRArray[0]) != null
							&& StringUtils.isNotEmpty(constructActiveSRLink(registrationSummary, activeSRArray[0]))){
						activeSR = constructActiveSRLink(registrationSummary, activeSRArray[0]);
					}
					if(constructActiveSRLink(registrationSummary, activeSRArray[1]) != null
							&& StringUtils.isNotEmpty(constructActiveSRLink(registrationSummary, activeSRArray[1]))){
						activeSR += (activeSR!=null && StringUtils.isNotEmpty(activeSR)?", ":"")
										+ constructActiveSRLink(registrationSummary, activeSRArray[1]);
					}
				} else if(activeSRArray.length == 3){
					if(constructActiveSRLink(registrationSummary, activeSRArray[0]) != null
							&& StringUtils.isNotEmpty(constructActiveSRLink(registrationSummary, activeSRArray[0]))){
						activeSR = constructActiveSRLink(registrationSummary, activeSRArray[0]);
					}
					if(constructActiveSRLink(registrationSummary, activeSRArray[1]) != null
							&& StringUtils.isNotEmpty(constructActiveSRLink(registrationSummary, activeSRArray[1]))){
						activeSR += (activeSR!=null && StringUtils.isNotEmpty(activeSR)?", ":"")
										+ constructActiveSRLink(registrationSummary, activeSRArray[1]);
					}
					if(constructActiveSRLink(registrationSummary, activeSRArray[2]) != null
							&& StringUtils.isNotEmpty(constructActiveSRLink(registrationSummary, activeSRArray[2]))){
						activeSR += (activeSR!=null && StringUtils.isNotEmpty(activeSR)?", ":"")
										+ constructActiveSRLink(registrationSummary, activeSRArray[2]);
					}
				} else{
					activeSR = constructActiveSRLink(registrationSummary, activeSR)!=null ? constructActiveSRLink(registrationSummary, activeSR):"";
				}
				registrationSummary.setActiveSRUIElement(activeSR);
				logger.debug("ActiveSR-Finally:" + activeSR);

				if(registrationSummary.getRegistrationTypeDesc() != null && (registrationSummary.getRegistrationTypeDesc()).equals(RegistrationTypeEnum.IPOFFICE.getRegistrationType())) {

					if(registrationSummary.getTechRegStatusId().equals(StatusEnum.COMPLETED.getStatusId()) || registrationSummary.getFinalValidationStatusId().equals(StatusEnum.COMPLETED.getStatusId())){
						buttonStatus = disabled;
					}
                }
			else {
                	if(StringUtils.isNotEmpty(registrationSummary.getInstallBaseStatusId()) && !registrationSummary.getInstallBaseStatusId().equals(StatusEnum.COMPLETED.getStatusId())){
                		buttonStatus = disabled;
                	} else if(StringUtils.isNotEmpty(registrationSummary.getTechRegStatusId()) && registrationSummary.getTechRegStatusId().equals(StatusEnum.COMPLETED.getStatusId())){
                		buttonStatus = disabled;
                	}
                }

				if(GRTConstants.YES.equalsIgnoreCase(registrationSummary.getSalMigrationOnly())){
					buttonStatus = disabled;
				}
				if (getLoggedInUserDetail().isSuperUser() == true) {
					if ((flagEQR && finalValidStatus != null 
								&& !(StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(finalValidStatus) 
								|| StatusEnum.CANCELLED.getStatusShortDescription().equalsIgnoreCase(finalValidStatus)))
						|| (flagTOB && techRegStatus != null 
								&& !(StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(techRegStatus) 
								|| StatusEnum.CANCELLED.getStatusShortDescription().equalsIgnoreCase(techRegStatus)))
						|| (flagIB && installBaseStatus != null 
								&& !(StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(installBaseStatus) 
								|| StatusEnum.CANCELLED.getStatusShortDescription().equalsIgnoreCase(installBaseStatus)))
						|| (flagEQM && eqpMoveStatus != null 
								&& !(StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(eqpMoveStatus) 
								|| StatusEnum.CANCELLED.getStatusShortDescription().equalsIgnoreCase(eqpMoveStatus)))		
							) {
						superButton = "<input type='button' class='osm_normal_button' value='Override'  onClick=\"superUpdateStatus('"+registrationId+"', '"+processStepId+"', '"+statusId+"', '"+soldToId+"')\" />";
					}
				}

				if(registrationSummary.getIsOnBoardingExist()){
					ipoButton = "<input type='button' "+ipoButtonStatus+" class='osm_normal_button emailOnBoardFileBtn' value='OnBoarding File'/>";
					registrationSummary.setAllButtonUIElement(regButton+superButton+ipoButton);
				} else {
					registrationSummary.setAllButtonUIElement(regButton+superButton);
				}
				
			}
		}
	}
		
		private String constructActiveSRLink(RegistrationSummary registrationSummary, String activeSR){
			if (activeSR == null) {
				activeSR = "";
			}
			else if(GRTConstants.SIEBEL_SR_CREATION_ERROR.equalsIgnoreCase(activeSR)){
				activeSR = "<a href='#' title='" + GRTConstants.SIEBEL_SR_CREATION_ERROR_TIP + "'>" + activeSR + "</a>";
			}
			else {
				if(StringUtils.isNotEmpty(activeSR)){
					activeSR = "<a href='#' onClick='showPopup(\"" + registrationSummary.getSoldTo() + "\", \"" + activeSR +"\")'>" + activeSR + "</a>";
				}
			}
			return activeSR;
		}

		
		
		

	/*  START :: Employee Flow  */

		//For BFOWARNING
		public boolean	validateBFOWarning(){
			if(actionForm !=null && actionForm.getSoldToId() != null && actionForm.getSoldToId().length() > SOLDTOID_MAX_LENGTH){
				return false;
			}
			if(actionForm !=null && actionForm.getUserRole() != null && actionForm.getUserRole().length() > USERROLE_MAX_LENGTH){
				return false;
			}
			if(actionForm !=null && actionForm.getUserId() != null && actionForm.getUserId().length() > USERID_MAX_LENGTH){
				return false;
			}
			
			
			return true;
		}
		
		
		
		
	public String newRegistrationWithSoldToValidationForAgent() throws Exception {
		
		//For BFOWARNING
		if(!(validateBFOWarning())){
			return Action.ERROR;
		}
		
		
		actionForm.setSoldToError(null);
		if(logger.isDebugEnabled())
			logger.debug("Entering RegistrationController : newRegistrationWithSoldToValidationForAgent");
		validateSession();
		navigateTo = "";

		this.getRequest().removeAttribute("soldToError");
		String soldTo= actionForm.getSoldToId();
		soldTo = StringEscapeUtils.unescapeHtml(soldTo);
		
		actionForm.setAccount(null);
		Account account = getSiebelClient().queryAccount(soldTo);
		actionForm.setAccount(account);
		if (account == null ) {
			navigateTo = backToSoldToSelectPageForAgent( grtConfig.getEwiMessageCodeMap().get("soldToNotValidMsg") + "###" + grtConfig.getSoldToNotValidMsg() );
			actionForm.setNavigateTo(navigateTo);
			return navigateTo;
		} 
		if (StringUtils.isEmpty(actionForm.getSoldToId().trim())) {
			navigateTo = backToSoldToSelectPageForAgent( grtConfig.getEwiMessageCodeMap().get("soldToEmptyMsg") + "###" + grtConfig.getSoldToEmptyMsg() );
			actionForm.setNavigateTo(navigateTo);
			return navigateTo;
		}
		if(getBaseRegistrationService().isSoldToExcluded(actionForm.getSoldToId())){
			navigateTo = backToSoldToSelectPageForAgent(grtConfig.getEwiMessageCodeMap().get("soldToExcludedMsg") + "###" + grtConfig.getSoldToExcludedMsg());
			actionForm.setNavigateTo(navigateTo);
			return navigateTo;
		}

		String actType = account!=null?account.getType():null;
		logger.debug(">>>>>>>>>>>>>>>>>>>Account Type:" + account.getType());
		/*if (GRTConstants.ACCOUNT_TYPE_HIERARCHY.equalsIgnoreCase(account.getType())
				|| GRTConstants.ACCOUNT_TYPE_UCM_AVAYA_HN.equalsIgnoreCase(account.getType()) 
				|| GRTConstants.ACCOUNT_TYPE_UCM_GDU_HN.equalsIgnoreCase(account.getType())) {*/
		if (!(GRTConstants.ACCOUNT_TYPE_SHIPTO_PARTY.equalsIgnoreCase(actType)
				|| GRTConstants.ACCOUNT_TYPE_SOLDTO_PARTY.equalsIgnoreCase(actType))){
			logger.debug("Account Type:" + account.getType()); 
			navigateTo = backToSoldToSelectPageForAgent( grtConfig.getEwiMessageCodeMap().get("soldToNotEligibleMsg") + "###" + grtConfig.getSoldToNotEligibleMsg() );
			actionForm.setNavigateTo(navigateTo);
			return navigateTo;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Exiting RegistrationController : newRegistrationWithSoldToValidationForAgent");
			logger.debug("BEFORE GOING TO THE newRegistration FORM form.getUserType()"+fixCSS(actionForm.getUserRole()));
		}
		navigateTo = newRegistration();
		actionForm.setNavigateTo(navigateTo);
		return navigateTo;
	}

	/**
	 * @param errMsg
	 * @return String
	 * @description redirects the user to the sold-to selection 
	 */
	public String backToSoldToSelectPageForAgent(String errMsg) {
		this.getRequest().setAttribute("soldToError", errMsg);
		actionForm.setSoldToError(errMsg);
		logger.debug("Invalid sold to, back to current page");
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}
	
	public List<TechnicalOrderDetail> createPartList(List<TechnicalOrderDetail> originalList) {
		List<TechnicalOrderDetail> newList = new ArrayList<TechnicalOrderDetail>();
		
		if( maxResults == 0 ){ //condition for ui part
			startIndex = pageNo * grtConfig.getRegistrationListRowNum();
			maxResults = grtConfig.getRegistrationListRowNum();
		}
		
		int countNoElement = 0;
		for (int i = 0 ; i < originalList.size(); i++) {
			if ( i >= startIndex ) {
				newList.add(originalList.get(i));
				countNoElement++;
				if (countNoElement == maxResults) break;				
			}
			
		}
		
		return newList;
	}

	/*  END  :: Employee Flow */
	
	
	//GRT 4.0-24-March
	
	public void getSrSummaryInfo() {
		
		String summaryInfo = "";
		String desc = "";
		String owner = "";
		String completionNarrative = "";
		try {
			boolean activityFound = false;
			ServiceRequest sr = getSiebelClient().querySR(actionForm.getSrNumber());
			actionForm.setSrDescription(sr.getDescription());
			//srSummaryInfo.add(sr.getDescription());
			desc =  sr.getDescription();
			actionForm.setSrOwnerName(sr.getOwnerFirstName() + " "+ sr.getOwnerLastName());
			owner = sr.getOwnerFirstName() + " "+ sr.getOwnerLastName();
			
			Activity activity = getSiebelClient()
					.queryLatestPublicSRNoteActivity(actionForm.getSrNumber());
			if (activity != null && activity.getDescription() != null) {
				//srSummaryInfo.add("2" + activity.getDescription());
				actionForm.setSrDescription("2" + activity.getDescription());
				activityFound = true;
				desc = activity.getDescription();
			}
			logger.debug("sr.getCompletionNarrative():" + sr.getCompletionNarrative() + ": for SR#" + actionForm.getSrNumber());
			if(StringUtils.isNotEmpty(sr.getCompletionNarrative())) {
				if(!activityFound) {
					//srSummaryInfo.add("3" + sr.getCompletionNarrative());
					actionForm.setSrCompletionNarrative("3" + sr.getCompletionNarrative());
					completionNarrative = sr.getCompletionNarrative();
				} else {
					//	srSummaryInfo.add(sr.getCompletionNarrative());
					actionForm.setSrCompletionNarrative(sr.getCompletionNarrative());
					completionNarrative = sr.getCompletionNarrative();
				}
			} else {
				actionForm.setSrCompletionNarrative("");
				completionNarrative = "";
			}
			//actionForm.setSrSummaryInfoList(srSummaryInfo);
			
			summaryInfo += desc+" :: ";
			summaryInfo += owner+" :: ";
			summaryInfo += completionNarrative + " ";
			
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.print(summaryInfo);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			logger.error("Error query siebel sr or sr note " + e.getMessage());
		}

		//return Action.SUCCESS;
	}
	
	
	public String saveSrSummaryInfo() {
		try {
			logger.debug("Entering saveSrSummaryInfo with srNumber:" + actionForm.getSrNumber() + " srNote:" + actionForm.getSrNote() + " accountLocation:" + actionForm.getAccountLocation());
			if (!("").equals( actionForm.getSrNote() )) {
				//Below 4 lines added to fix issue with adding notes in EQM SR
				String soldTo = actionForm.getAccountLocation() != null ? actionForm.getAccountLocation() : "";
				if(soldTo.length() > 10) {
					soldTo = soldTo.substring(0, 10);
				}
				getSiebelClient().createSRNoteActivity(soldTo, actionForm.getSrNumber(), actionForm.getSrNote(), "");
			}
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			logger.error("Error create siebel sr", e);
		} finally {
			logger.debug("Exiting saveSrSummaryInfo with srNumber:" + actionForm.getSrNumber() + " srNote:" +  actionForm.getSrNote()  + " accountLocation:" + actionForm.getAccountLocation());
		}
		
		return Action.SUCCESS;

	}
	
	
	public String  getTechnicalOrderDetails() throws Exception {
		validateSession();
		logger.debug("Entering getTechnicalOrderDetails");
		long c1 =  System.currentTimeMillis();
		actionForm.setSalList(false);
		actionForm.setNonSalList(false);
		actionForm.setSalMigrationList(false);
		actionForm.setSeidCreationFailureFlag(false);
		actionForm.setRetestListFlag(false);

		this.skipStepB = "none";
		actionForm.setSkipStepB("none");
		CSSPortalUser cssPortalUser = null;
		try {
			cssPortalUser = getCSSUserProfile();
		} catch (Exception e) {

		} finally {
			if (cssPortalUser == null) {
				cssPortalUser = getDummyCssUserProfile();
			}
		}
		boolean superUser = cssPortalUser.isSuperUser();
		System.out.println("superUser"+superUser);

		logger.debug("BEFORE CALLING getRegistrationDelegate().getTechnicalRegistrationDetails(form.getRegistrationId());");
		logger.debug("THE Registration Id which we are sending to DAO to get the Data is "+ actionForm.getRegistrationId());

		//Service Call 
		SiteRegistration siteRegistration  = getTechnicalOnBoardingService().getTechnicalRegistrationDetails(actionForm.getRegistrationId(), superUser);
		
		System.out.println("AlarmAndConnectivity:"+siteRegistration.isAlarmAndConnectivityDisabled());
		logger.debug("AFTER CALLING getRegistrationDelegate().getTechnicalRegistrationDetails(form.getRegistrationId());");
		String registrationType = siteRegistration.getStrRegistrationType();
		getRequest().getSession().setAttribute(GRTConstants.REGISTRATION_TYPE, registrationType);

		logger.debug("<-----------------------------------REGISTRATION TYPE IS  ---------------------------->"+ registrationType);
		//Below line commented to remove the constraint based on Registration Type
		//if ( registrationType.equals(RegistrationTypeEnum.SALMIGRATION.getRegistrationID()) ){

		logger.debug("<-----------------------------------REGISTRATION TYPE IS SALMIGRATION ---------------------------->");

		actionForm.setSalMigrationSummaryList(siteRegistration.getSalMigrationSummaryList());
		if( actionForm.getSalMigrationSummaryList() != null &&  actionForm.getSalMigrationSummaryList().size()>0){
			actionForm.setSalMigrationList(true);
		}

		logger.debug("Size of the Sal Migration List in Form  ==================================>"+actionForm.getSalMigrationSummaryList() +"  ");
		//Below line commented to remove the constraint based on Registration Type
		//} else {
		logger.debug("<-------------------------------REGISTRATION TYPE IS NOT  NOT NOT NOT NOT SALMIGRATION ---------------------->");
		actionForm.setTechnicalRegistrationDetailList(siteRegistration.getTechnicalRegistrationDetailList());
		actionForm.setSalRegistrationSummaryList(siteRegistration.getSalRegistrationSummaryList());
		
		//GRT 4.0 Retest Records
		RegistrationSummary summary = getTechnicalOnBoardingService().getRegistrationSummary(siteRegistration.getRegistrationId());
		
		actionForm.setRetestTRList(siteRegistration.getRetestTRList());
		if( actionForm.getRetestTRList()!=null && actionForm.getRetestTRList().size() > 0 ){
			/* for(TechnicalRegistration techReg:actionForm.getRetestTRList())
			 {
				 techReg.getStepBStatus().setStatusShortDescription(summary.getTechRegStatus());
			 }*/
			for(TechnicalRegistration tR: actionForm.getRetestTRList())
			{
				
				for(ExpandedSolutionElement exS : tR.getExplodedSolutionElements())
				{
					
					CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(exS.getSeCode());
					if(compatibilityMatrix != null){
						exS.setTransportAlarmEligible(compatibilityMatrix.getTransportAlarm());
						
					}
					
					List<ExpandedSolutionElement> expSolEleList = getTechnicalOnBoardingService().getAssetsWithSameSIDandMID(tR.getSid(), tR.getMid(), tR.getSolutionElementId());
					for(ExpandedSolutionElement element:expSolEleList)
					{
						if(exS.getSeID().equalsIgnoreCase(element.getSeID()))
						{
							exS.setPrimarySalGWSeid(element.getIpAddress());
						}
					}
					
					
					
				}
				
				
			}
			
			
			actionForm.setRetestListFlag(true);
		}
		
		//Begin Set the size of the Lists to controll the display of Non-Sal, SAL and SalMigration sections
		if(actionForm.getTechnicalRegistrationDetailList() != null  && actionForm.getTechnicalRegistrationDetailList().size()>0){
			actionForm.setNonSalList(true);
		}
		if(actionForm.getSalRegistrationSummaryList() != null && actionForm.getSalRegistrationSummaryList().size() > 0){
			actionForm.setSalList(true);
			for(TechnicalRegistration techReg: actionForm.getSalRegistrationSummaryList()){
				if(techReg != null && StringUtils.isEmpty(techReg.getSolutionElementId())) {
					this.skipStepB = "block";
					actionForm.setSkipStepB("block");
				}
			}
		}
		actionForm.setSeidCreationFailureFlag(siteRegistration.isSeidCreationFailureFlag());
		//End Begin Set the size of the Lists to controll the display of Non-Sal, SAL and SalMigration sections
		//Below line commented to remove the constraint based on Registration Type			
		//}

		logger.debug("The siteRegistration.getRegistrationId() is  ==============>"+ siteRegistration.getRegistrationId());
		logger.debug("<-------------------The value of AlarmAndConnectivityDisabled in domain object ==============>"+ siteRegistration.isAlarmAndConnectivityDisabled()+"------------>");
		getRequest().getSession().setAttribute(GRTConstants.SITE_REGISTRATION, siteRegistration);

		long c2 = System.currentTimeMillis();
		logger.debug("Timer to render the Technical Registration Details Screen for Registration Id::"+actionForm.getRegistrationId()+ ">> Time in milliseconds"+ (c2-c1));
		logger.debug(" Exiting from  TechnicalOnBoardingAction.getTechnicalOrderDetails()");
		//Begin: Set the Banner Details
		//Service Call
		
		actionForm.setFirstColumnOnly(null);
		actionForm.setBannerStatus(summary.getTechRegStatus());
		actionForm.setBannerSubStatus(GRTConstants.NA);
		actionForm.setBannerSrNumber("");
		actionForm.setBannerSrLabel(GRTConstants.FALSE);
		actionForm.setCompany(siteRegistration.getCompany());
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
		if(summary.getTobCompletedDate()!=null && summary.getTechRegStatus().equalsIgnoreCase("Completed"))
		actionForm.setBannerCompletedDate(sdf.format(summary.getTobCompletedDate()));
		
		actionForm.setBannerSubmittedDate(sdf.format(summary.getCreatedDate()));
		for( TechnicalRegistration tg: siteRegistration.getTechnicalRegistrationDetailList()){
			if(tg.getStepASubmittedDate()!=null)
				actionForm.setBannerSubmittedDate(sdf.format(tg.getStepASubmittedDate()));
			if(tg.getStepAReSubmittedDate()!=null)
				actionForm.setBannerSubmittedDate(sdf.format(tg.getStepAReSubmittedDate()));
		}
		
		for( TechnicalRegistration tg: siteRegistration.getSalRegistrationSummaryList()){
			if(tg.getStepASubmittedDate()!=null)
				actionForm.setBannerSubmittedDate(sdf.format(tg.getStepASubmittedDate()));
			if(tg.getStepAReSubmittedDate()!=null)
				actionForm.setBannerSubmittedDate(sdf.format(tg.getStepAReSubmittedDate()));
		}
		
		//Defect #768
		if( summary.getRegistrationIdentifier()!=null ){
			actionForm.setRegistrationIdentifier(summary.getRegistrationIdentifier());
		}
		
		if( summary.getRegistrationNotes()!=null ){
			actionForm.setRegistrationNotes(summary.getRegistrationNotes());		
		}
					
		//End: Set the Banner Details
		if(siteRegistration.isAlarmAndConnectivityDisabled()){
			getRequest().getSession().setAttribute(GRTConstants.ALARM_AND_CONNECTIVITY_DISABLED, GRTConstants.TRUE);
		}else {
			getRequest().getSession().setAttribute(GRTConstants.ALARM_AND_CONNECTIVITY_DISABLED, GRTConstants.FALSE);
		}
		getRequest().getSession().setAttribute(GRTConstants.TR_DETAILS_ACTIVE, GRTConstants.TRUE);
		//saveFormBeanInSession(registrationFormBean);
		//return new Forward("success", form);
		actionForm.setNavigateTo("technicalRegistrationDetails");//For Ui Navigation
		return "successTechOrder";
	}
	
	//GRT 4.0 chnages
	public String equipmentRemovalProcess(RegistrationFormBean form)throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		String cause = "";
		form.setMessage("");
		form.setActveSRColumnOnly(true);
		form.setBannerSrLabel(GRTConstants.TRUE);
		// User comes from Home screen->EQR only
		form.setEqrFileDownloaded(false);
        logger.debug("................ Starting for equipmentRemovalProcess records fetching ................");
		String soldToId = null;
		try {
			List<TechnicalOrderDetail> technicalOrderDetailList = null;
			if (form.getSoldToId() != null) {
				soldToId = form.getSoldToId();
			} else if (getRequest().getParameter("soldToId") != null) {
				soldToId = getRequest().getParameter("soldToId");
			}

			if (soldToId != null) {
				// Fetching equipments from siebel and validating material codes existence against Material Exclusion table
				try{
					technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, true);
				}catch(DataAccessException dataExcep){
					cause = "DataAccessException";
				}
				logger.debug("equipmentRemovalProcess MaterialList after MaterialExclusion:  size: " + technicalOrderDetailList.size());
				form.setMaterialsAfterExclusionList(technicalOrderDetailList);
				logger.debug("SaveSite Flag:  "+form.getSaveSiteReg());
				if(form.getSaveSiteReg()){
					// Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 0);
					logger.debug("Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					// Filter pipeline records
					technicalOrderDetailList = getBaseRegistrationService().filterTechnicalOrderOnPipeline(technicalOrderDetailList, soldToId, "DISP");
					logger.debug("Filtered pipeline records:"+technicalOrderDetailList.size());
					// Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 1);
					logger.debug("Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					form.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED);
					form.setBannerSubStatus("");
					form.setBannerSrNumber("");
					form.setBannerActiveContractSrNumber("");
					
				} else {
					// Fetching the saved EQR records from technical order
					if(null != actionForm.getManuallyAddedMaterialEntryList()) {
						actionForm.getManuallyAddedMaterialEntryList().clear();
					}
				
					technicalOrderDetailList = getEqrService().getTechnicalOrderByType(form.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV, true);
					
					//GRT 4.0 changes - populate mc serialized against matertial master
					//Prepare list of material codes
					List<String> materialCodes = new ArrayList<String>();
					for(TechnicalOrderDetail tod: technicalOrderDetailList){
						materialCodes.add(tod.getMaterialCode());	
					}
					materialMasterMap = getInstallBaseService().populateMaterialSerialize(materialCodes);
					for(TechnicalOrderDetail tod: technicalOrderDetailList){
						if(isMaterialCodeSerialized(tod.getMaterialCode())) {
							tod.setSerialized(GRTConstants.Y);
						} else {
							tod.setSerialized(GRTConstants.N);
						}	
						//set original serial number for export 
						if(null == tod.getOriginalSerialNumber() && null != tod.getSerialNumber()) {
							String serialNumber = tod.getSerialNumber();
							tod.setOriginalSerialNumber(serialNumber);
						}
					}
					
					//GRT 4.0 changes - populate mc serialized against matertial master ends
					
					/*if(!(technicalOrderDetailList != null && technicalOrderDetailList.size() > 0)){
						technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList);
					}*/
					RegistrationSummary summary = getBaseRegistrationService().getRegistrationSummary(form.getRegistrationId());
					form.setBannerStatus(summary.getFinalValidationStatus());
					form.setBannerSubStatus(summary.getFinalValidationSubStatus());
					form.setBannerSrNumber(summary.getFinalValidationSrNo());
					form.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
					form.setCompany(summary.getRequestingCompany());
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
					if(summary.getEqrSubmittedDate() != null){
						form.setBannerSubmittedDate(sdf.format(summary.getEqrSubmittedDate()));
					} else {
						form.setBannerSubmittedDate("");
					}
					if(summary.getEqrCompletedDate() != null){
						form.setBannerCompletedDate(sdf.format(summary.getEqrCompletedDate()));
					} else {
						form.setBannerCompletedDate("");
					}
					
					//Defect #876 - if status is 'Validated' generate submitted and completed date.
					if(StatusEnum.VALIDATED.getStatusShortDescription().equalsIgnoreCase(summary.getFinalValidationStatus()) && summary.getCreatedDate() != null) {
						form.setBannerSubmittedDate(sdf.format(summary.getCreatedDate()));
						//form.setBannerCompletedDate(sdf.format(summary.getCreatedDate()));
					}
					//Defect #768
					if( summary.getRegistrationIdentifier()!=null ){
						actionForm.setRegistrationIdentifier(summary.getRegistrationIdentifier());
					}
					
					if( summary.getRegistrationNotes()!=null ){
						actionForm.setRegistrationNotes(summary.getRegistrationNotes());		
					}
				}
				// Set all the Technical Order records as readonly based on form:readonly
				for(TechnicalOrderDetail techDto : technicalOrderDetailList){
					if(form.isReadOnly()){
						techDto.setExclusionFlag(true);
					}
					if(GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(techDto.getActionType())) {
						techDto.setInitialQuantity(0L);
						techDto.setRemovedQuantity(techDto.getRemainingQuantity());
					}
				}
				// Fetch pipeline IB and EQR records
				List<PipelineSapTransactions> pipelineList = getEqrService().getConsolidatedPipelineRecords(soldToId);
				if(pipelineList != null && pipelineList.size() > 0){
					logger.debug("Consolidated pipeline List size:"+pipelineList.size());
					for(TechnicalOrderDetail techDto : technicalOrderDetailList){
						for(PipelineSapTransactions pipelineDto : pipelineList){
							if(techDto.getMaterialCode().equalsIgnoreCase(pipelineDto.getMaterialCode())){
								if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV)){
									techDto.setPipelineEQRQuantity(pipelineDto.getQuantity());
								} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
									techDto.setPipelineIBQuantity(pipelineDto.getQuantity());
								}else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM)){
									techDto.setPipelineEQRMoveQuantity(pipelineDto.getQuantity());
								}
							}
						}
					}
				} else {
					logger.debug("Consolidated pipeline List size:0");
				}
				CSSPortalUser cssPortalUser = getUserFromSession();
				String userType  = cssPortalUser.getUserType();
				if(!StringUtils.isEmpty(userType)){
					if (!userType.equals("B")
							&& !userType.equals("C")){
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.TRUE);
					} else {
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.FALSE);
					}
				}
				RegistrationSummary summary = getBaseRegistrationService().getRegistrationSummary(form.getRegistrationId());
				if (summary.getFinalValidationStatusId() !=null
						&& !summary.getFinalValidationStatusId().equalsIgnoreCase(GRTConstants.AWAITNG_INFORMATION)) {
					for (TechnicalOrderDetail detail : technicalOrderDetailList) {
						if(StringUtils.isEmpty(detail.getExclusionSource())){
							detail.setErrorDescription("");
						}
						
						//GRT 4.0 changes
						 if(StringUtils.isEmpty(detail.getProductLine()))
			                {
			                	String productLine=getBaseRegistrationService().getProductLineByMaterialCode(detail.getMaterialCode());
			                	System.out.println(" Product Line bank "+detail.getMaterialCode());
			                	if(!StringUtils.isEmpty(productLine))
			                	{
			                		detail.setProductLine(productLine);
			                	}
			                }
						
					}
				}
				logger.debug("equipmentRemovalProcess MaterialList after exclusion:  size: " + technicalOrderDetailList.size());
				GenericSort gs = new GenericSort("productLine", true);
				Collections.sort(technicalOrderDetailList, gs);
				form.setMaterialEntryList(technicalOrderDetailList);
				
				processMaterilaEntryListForSummarizedView(actionForm.getMaterialEntryList());
			}
			getRequest().setAttribute("materialList", form.getMaterialEntryList());
			long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Ending for equipmentRemovalProcess records fetching ................");
	        logger.debug("TIMER for Equipment Removal rendering for RegID:"+form.getRegistrationId()+" time in milliseconds :" + (c2-c1));
	        if("DataAccessException".equals(cause)){
	        	//this.setDataError("Transaction timed out while fetching the data from Siebel");
	        	//TODO
	        }else{
	        	//this.setDataError("No Results Found.");
	        	//TODO
	        }
		} catch(DataAccessException daexception){
			logger.error("DataAccessException in EquipmentRemovalProcess"+daexception.getMessage());
			daexception.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in EquipmentRemovalProcess"+e.getMessage());
		}
		
		
		return "successRegValidation";
	}
	
	public void processMaterilaEntryListForSummarizedView(List<TechnicalOrderDetail> materialEntryList){
		Map<RVSummaryRecord,ArrayList<TechnicalOrderDetail>> summaryMap = new HashMap<RVSummaryRecord, ArrayList<TechnicalOrderDetail>>();
		for(TechnicalOrderDetail todetail : materialEntryList) {
			boolean add = false;
			for (Map.Entry<RVSummaryRecord, ArrayList<TechnicalOrderDetail>> entry : summaryMap.entrySet()) {
				RVSummaryRecord tod = entry.getKey();
				if(tod.getMaterialCode().equalsIgnoreCase(todetail.getMaterialCode())) {
					long quantity = todetail.getInitialQuantity() + tod.getQuantity();
					tod.setQuantity(quantity);
					summaryMap.get(tod).add(todetail);
					add = true;
					break;
				}
			}
			if(! add) {
				RVSummaryRecord rvSummaryKey = new RVSummaryRecord();
				rvSummaryKey.setMaterialCode(todetail.getMaterialCode());
				rvSummaryKey.setQuantity(todetail.getInitialQuantity());
				rvSummaryKey.setDescription(todetail.getDescription());
				
				ArrayList<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
				todList.add(todetail);
				summaryMap.put(rvSummaryKey, todList);
			}
		}
		actionForm.setSummaryMap(summaryMap);
	}
	
	
	//GRT 4.0 chnages
		public String equipmentRemovalMoveProcess(RegistrationFormBean form)throws Exception {
			validateSession();
			long c1 = Calendar.getInstance().getTimeInMillis();
			String cause = "";
			form.setMessage("");
			form.setActveSRColumnOnly(true);
			form.setBannerSrLabel(GRTConstants.TRUE);
			// User comes from Home screen->EQR only
			form.setEqrFileDownloaded(false);
	        logger.debug("................ Starting for equipmentRemovalProcess records fetching ................");
			String soldToId = null;
			try {
				List<TechnicalOrderDetail> technicalOrderDetailList = null;
				if (form.getSoldToId() != null) {
					soldToId = form.getSoldToId();
					if(soldToId.contains("/"))
					{
						String[] finalSoldTo=soldToId.split("/");
						soldToId=finalSoldTo[0];
					}
				} else if (getRequest().getParameter("soldToId") != null) {
					soldToId = getRequest().getParameter("soldToId");
				}

				if (soldToId != null) {
					// Fetching equipments from siebel and validating material codes existence against Material Exclusion table
					try{
						technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, true);
					}catch(DataAccessException dataExcep){
						cause = "DataAccessException";
					}
					logger.debug("equipmentRemovalProcess MaterialList after MaterialExclusion:  size: " + technicalOrderDetailList.size());
					form.setMaterialsAfterExclusionList(technicalOrderDetailList);
					logger.debug("SaveSite Flag:  "+form.getSaveSiteReg());
					if(form.getSaveSiteReg()){
						// Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately
						technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 0);
						logger.debug("Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
						// Filter pipeline records
						technicalOrderDetailList = getBaseRegistrationService().filterTechnicalOrderOnPipeline(technicalOrderDetailList, soldToId, "DISP");
						logger.debug("Filtered pipeline records:"+technicalOrderDetailList.size());
						// Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately
						technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 1);
						logger.debug("Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
						form.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED);
						form.setBannerSubStatus("");
						form.setBannerSrNumber("");
						form.setBannerActiveContractSrNumber("");
						//form.setEqmFromSoldTo(eqmFromSoldTo);
					} else {
						// Fetching the saved EQR records from technical order
						if(null != actionForm.getManuallyAddedMaterialEntryList()) {
							actionForm.getManuallyAddedMaterialEntryList().clear();
						}
						
						technicalOrderDetailList = getEqrService().getTechnicalOrderByType(form.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_EM, true);
						/*if(!(technicalOrderDetailList != null && technicalOrderDetailList.size() > 0)){
							technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList);
						}*/
						RegistrationSummary summary = getBaseRegistrationService().getRegistrationSummary(form.getRegistrationId());
						form.setBannerStatus(summary.getEqrMoveStatus());
						form.setBannerSubStatus(summary.getEqrMoveSubStatus());
						form.setBannerSrNumber(summary.getEqrMoveSrNo());
						form.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
						form.setCompany(summary.getRequestingCompany());
						form.setEqmFromSoldTo(summary.getSoldTo());
						form.setEqmToSoldTo(summary.getToSoldToId());
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
						if(summary.getEqrMoveSubmittedDate() != null){
							form.setBannerSubmittedDate(sdf.format(summary.getEqrMoveSubmittedDate()));
						} else {
							form.setBannerSubmittedDate("");
						}
						if(summary.getEqrMoveCompletedDate() != null){
							form.setBannerCompletedDate(sdf.format(summary.getEqrMoveCompletedDate()));
						} else {
							form.setBannerCompletedDate("");
						}
						
						//Defect #768
						if( summary.getRegistrationIdentifier()!=null ){
							actionForm.setRegistrationIdentifier(summary.getRegistrationIdentifier());
						}
						
						if( summary.getRegistrationNotes()!=null ){
							actionForm.setRegistrationNotes(summary.getRegistrationNotes());		
						}
					}
					// Set all the Technical Order records as readonly based on form:readonly
					for(TechnicalOrderDetail techDto : technicalOrderDetailList){
						if(form.isReadOnly()){
							techDto.setExclusionFlag(true);
						}
					}
					// Fetch pipeline IB and EQR records
					List<PipelineSapTransactions> pipelineList = getEqrService().getConsolidatedPipelineRecords(soldToId);
					if(pipelineList != null && pipelineList.size() > 0){
						logger.debug("Consolidated pipeline List size:"+pipelineList.size());
						for(TechnicalOrderDetail techDto : technicalOrderDetailList){
							for(PipelineSapTransactions pipelineDto : pipelineList){
								if(techDto.getMaterialCode().equalsIgnoreCase(pipelineDto.getMaterialCode())){
									if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV)){
										techDto.setPipelineEQRQuantity(pipelineDto.getQuantity());
									} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
										techDto.setPipelineIBQuantity(pipelineDto.getQuantity());
									}else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM)){
										techDto.setPipelineEQRMoveQuantity(pipelineDto.getQuantity());
									}
								}
							}
						}
					} else {
						logger.debug("Consolidated pipeline List size:0");
					}
					CSSPortalUser cssPortalUser = getUserFromSession();
					String userType  = cssPortalUser.getUserType();
					if(!StringUtils.isEmpty(userType)){
						if (!userType.equals("B")
								&& !userType.equals("C")){
							getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.TRUE);
						} else {
							getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.FALSE);
						}
					}
					RegistrationSummary summary = getBaseRegistrationService().getRegistrationSummary(form.getRegistrationId());
					if (summary.getEqrMoveStatusId() !=null
							&& !summary.getEqrMoveStatusId().equalsIgnoreCase(GRTConstants.AWAITNG_INFORMATION)) {
						for (TechnicalOrderDetail detail : technicalOrderDetailList) {
							if(StringUtils.isEmpty(detail.getExclusionSource())){
								detail.setErrorDescription("");
							}
						}
					}
					logger.debug("equipmentRemovalProcess MaterialList after exclusion:  size: " + technicalOrderDetailList.size());
					for(TechnicalOrderDetail Td:technicalOrderDetailList)
					{
						if(Td.getActiveContractExist()==null || Td.getActiveContractExist().isEmpty())
							Td.setActiveContractExist("No");
						if(Td.getTechnicallyRegisterable().isEmpty() || Td.getTechnicallyRegisterable()==null)
							Td.setTechnicallyRegisterable("No");
						//GRT 4.0 : Defect#280 Disable Vsalgw Records
						if(GRTConstants.SAL_VIRTUAL_GATEWAY.equalsIgnoreCase(Td.getSolutionElementCode())){
							Td
							.setErrorDescription(grtConfig.getVsalgwErrMsg());
						}
					}
					GenericSort gs = new GenericSort("productLine", true);
					Collections.sort(technicalOrderDetailList, gs);
					form.setMaterialEntryList(technicalOrderDetailList);
				}
				getRequest().setAttribute("materialList", form.getMaterialEntryList());
				long c2 = Calendar.getInstance().getTimeInMillis();
		        logger.debug("................ Ending for equipmentRemovalProcess records fetching ................");
		        logger.debug("TIMER for Equipment Removal rendering for RegID:"+form.getRegistrationId()+" time in milliseconds :" + (c2-c1));
		        if("DataAccessException".equals(cause)){
		        	//this.setDataError("Transaction timed out while fetching the data from Siebel");
		        	//TODO
		        }else{
		        	//this.setDataError("No Results Found.");
		        	//TODO
		        }
			} catch(DataAccessException daexception){
				logger.error("DataAccessException in EquipmentRemovalProcess"+daexception.getMessage());
				daexception.printStackTrace();
			} catch (Exception e) {
				logger.error("Exception in EquipmentRemovalProcess"+e.getMessage());
			}
			
			
			return  "successEqMove";
		}
		
	
	public String displayRegistration() throws Exception
	{
		validateSession();
		logger.debug("Entering RegistrationController : displayRegistration: RegID:"+actionForm.getRegistrationId());
		String soldTo = actionForm.getSoldToId();
		actionForm.setErrorMessage("");
		logger.debug(actionForm.getRegistrationId()+"	SoldTo:"+soldTo);
		String returnStr = "";
		if(null != soldTo) {
			String[] soldToIdArr = soldTo.split("/");
			for(String soldToId:soldToIdArr){
				logger.debug("RegID:"+actionForm.getRegistrationId()+"	soldToId:"+soldToId);
				if(StringUtils.isNotEmpty(soldToId) && getCSSUserProfile() != null && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(getCSSUserProfile().getUserType())){
					if(!getBaseRegistrationService().validateBpHasAccessToCatSoldTo(getCSSUserProfile().getBpLinkId(), soldToId)
							&& !getBaseRegistrationService().isBpSoldToExistForTheUser(getCSSUserProfile().getBpLinkId(), soldToId)){
						logger.debug("RegID:"+actionForm.getRegistrationId()+"  Error Message:"+actionForm.getErrorMessage());
						actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("alertNoAccessToSoldTo") + "###" + grtConfig.getAlertNoAccessToSoldTo());
						this.getRequest().setAttribute("regListBPLinkIdError", grtConfig.getEwiMessageCodeMap().get("alertNoAccessToSoldTo") + "###" + grtConfig.getAlertNoAccessToSoldTo());
						//returnStr = registrationList();
						return "successRegistrationList";
					}
				}
			}
		}
		this.setInstallBaseCreationReadOnlyFlag(false);
		actionForm.setMessage("");
		actionForm.setActveSRColumnOnly(false);
		SiteRegistration siteReg = getBaseRegistrationService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
		String regTypeId = siteReg.getRegistrationType().getRegistrationId();
		actionForm.setCompany(siteReg.getCompany());
		logger.debug("RegistrationController : Company Name displayRegistration(): form.getCompany() " + actionForm.getCompany()+"	RegID:"+actionForm.getRegistrationId());

		Status techRegstatus = siteReg.getTechRegStatus();
		String techRegstatusId = techRegstatus.getStatusId();

		//long hasSubmitted = siteReg.getHasSubmittedTrManually();

		if (actionForm.isTechnicalRegistrationOnly()) {
			actionForm.setRegistrationSummaryListRegistrable(null);
			if ((actionForm.getStatus() != null)
					&& (actionForm.getStatus().equals(StatusEnum.CANCELLED.getStatusShortDescription())
					 || actionForm.getStatus().equals(StatusEnum.INPROCESS.getStatusShortDescription()))) {
				actionForm.setReadOnly(true);
			} else {
				actionForm.setReadOnly(false);
			}

			if (regTypeId != null
					&& (regTypeId.equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID()))) {

				if (techRegstatusId != null
						&& ( // (techRegstatusId.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusId()) && hasSubmitted == 0)
							(techRegstatusId.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusId()))
							  || (techRegstatusId.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId()))
							  || (techRegstatusId.equalsIgnoreCase(StatusEnum.SAVED.getStatusId())) )) {
								
									actionForm.setRegistrationSummaryList(new ArrayList<TechnicalRegistrationSummary>());
									actionForm.setRegistrationSummaryListTRUpdate(new ArrayList<TechnicalRegistrationSummary>());
								  	return technicalRegistrationDashboard();// TO do - techRegDashBoard
								  //TODO
								  } else{
									return getTechnicalOrderDetails(); //"techRegDetail");
									//TODO
								  }
			}

			if(regTypeId != null && (regTypeId.equalsIgnoreCase(RegistrationTypeEnum.SALMIGRATION.getRegistrationID()))) {
				if(techRegstatusId != null) {
						/*&& ((techRegstatusId.equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId()))
									|| (techRegstatusId.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusId()) && hasSubmitted == 1)
									  ||(techRegstatusId.equalsIgnoreCase(StatusEnum.COMPLETED.getStatusId())))) {
											return new Forward("techRegDetail");
									} else {
										return new Forward("techRegDashBoard");
									}*/
					return  getTechnicalOrderDetails();//"techRegDetail";
				}
			}

			if(regTypeId != null && (regTypeId.equalsIgnoreCase(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY.getRegistrationID())))
			{
				/*
				 *	TR Completed status takes the user to TR dash board.
				 */
			if (techRegstatusId != null
						&& ((techRegstatusId.equalsIgnoreCase(StatusEnum.SAVED.getStatusId()))
							//|| (techRegstatusId.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusId()) && hasSubmitted == 0)
							||(techRegstatusId.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId()))
							||(techRegstatusId.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusId())))) {
									actionForm.setRegistrationSummaryList(new ArrayList<TechnicalRegistrationSummary>());
									actionForm.setRegistrationSummaryListTRUpdate(new ArrayList<TechnicalRegistrationSummary>());
									return technicalRegistrationDashboard();// new Forward("techRegDashBoard");
									//TODO
				                } else {
				                	
									return  getTechnicalOrderDetails();// new Forward("techRegDetail");
								}

			}
			if(regTypeId !=null && (regTypeId.equalsIgnoreCase(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID()))) {
						if (techRegstatusId != null
							&& ((techRegstatusId.equalsIgnoreCase(StatusEnum.SAVED.getStatusId()))
								||(techRegstatusId.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId()))
								  //||(techRegstatusId.equalsIgnoreCase(StatusEnum.INPROCESS.getStatusId()) && hasSubmitted == 0)
									 ||(techRegstatusId.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusId())))) {
											actionForm.setRegistrationSummaryList(new ArrayList<TechnicalRegistrationSummary>());
											actionForm.setRegistrationSummaryListTRUpdate(new ArrayList<TechnicalRegistrationSummary>());
											return technicalRegistrationDashboard(); //new Forward("techRegDashBoard");
											//TODO
						                } else {
						                	
											return  getTechnicalOrderDetails();// new Forward("techRegDetail");
										}
			}


		} else if (actionForm.isEquipmentRegistrationOnly()) {
			// When user comes from Registration List screen.
			if (((actionForm.getStatus() != null) && ((actionForm.getStatus()).equals(StatusEnum.INPROCESS.getStatusShortDescription())) || ((actionForm.getStatus()).equals(StatusEnum.AWAITINGINFO.getStatusShortDescription())))
					|| ((actionForm.getStatus()).equals(StatusEnum.COMPLETED.getStatusShortDescription()))
					|| ((actionForm.getStatus()).equals(StatusEnum.CANCELLED.getStatusShortDescription()))
					|| ((actionForm.getStatus()).equals(StatusEnum.VALIDATED.getStatusShortDescription()))) {
				getRequest().getSession().setAttribute("EQRStatus", "inprocess");
				actionForm.setReadOnly(true);
				actionForm.setValidateDisabled(true);
				actionForm.setSaveSiteReg(false);
			} else if (actionForm.getStatus() != null && actionForm.getStatus().equals(StatusEnum.NOTINITIATED.getStatusShortDescription())) {
				getRequest().getSession().removeAttribute("EQRStatus");
				actionForm.setReadOnly(false);
				actionForm.setValidateDisabled(false);
				actionForm.setSaveSiteReg(true);
			} else {
				if (actionForm.getStatus() != null && actionForm.getStatus().equals(
								StatusEnum.SAVED.getStatusShortDescription())) {
					getRequest().getSession().setAttribute("EQRStatus", "saved");
					actionForm.setValidateDisabled(true);
				} else {
					getRequest().getSession().removeAttribute("EQRStatus");
				}
				actionForm.setReadOnly(false);
				actionForm.setValidateDisabled(false);
				actionForm.setSaveSiteReg(false);
			}
			// GRT 4.0 Changes
			if ((actionForm.getStatus()).equals(StatusEnum.VALIDATED.getStatusShortDescription())) {
				actionForm.setValidateDisabled(false);
			}
			if ((actionForm.getStatus()).equals(StatusEnum.SAVED.getStatusShortDescription())) {
				actionForm.setValidateDisabled(true);
			}
			return equipmentRemovalProcess(actionForm); // new
														// Forward("eqrOnly",
														// form);
	   }else if(actionForm.isEquipmentMoveOnly())//Equipment Move Start
		{
			
			if (((actionForm.getStatus() != null) && ((actionForm.getStatus()).equals(StatusEnum.INPROCESS.getStatusShortDescription())) || ((actionForm.getStatus()).equals(StatusEnum.AWAITINGINFO.getStatusShortDescription())))
					|| ((actionForm.getStatus()).equals(StatusEnum.COMPLETED.getStatusShortDescription()))
					|| ((actionForm.getStatus()).equals(StatusEnum.CANCELLED.getStatusShortDescription()))
					|| ((actionForm.getStatus()).equals(StatusEnum.VALIDATED.getStatusShortDescription()))) {
				getRequest().getSession().setAttribute("EQR_MOVEStatus", "inprocess");
				actionForm.setReadOnly(true);
				actionForm.setValidateDisabled(true);
				actionForm.setSaveSiteReg(false);
			} else if (actionForm.getStatus() != null && actionForm.getStatus().equals(StatusEnum.NOTINITIATED.getStatusShortDescription())) {
				getRequest().getSession().removeAttribute("EQR_MOVEStatus");
				actionForm.setReadOnly(false);
				actionForm.setValidateDisabled(false);
				actionForm.setSaveSiteReg(true);
			} else {
				if (actionForm.getStatus() != null && actionForm.getStatus().equals(StatusEnum.SAVED.getStatusShortDescription())) {
					getRequest().getSession().setAttribute("EQR_MOVEStatus", "saved");
					actionForm.setValidateDisabled(true);
				} else {
					getRequest().getSession().removeAttribute("EQR_MOVEStatus");
				}
				actionForm.setReadOnly(false);
				actionForm.setValidateDisabled(false);
				actionForm.setSaveSiteReg(false);
			}
			
			
		return equipmentRemovalMoveProcess(actionForm); 
	}//Equipment Move end
		
		
		// When user comes from Registration List screen.
		if ((actionForm.getStatus() != null)
				&& (actionForm.getStatus().equals(StatusEnum.CANCELLED.getStatusShortDescription())
				 || actionForm.getStatus().equals(StatusEnum.INPROCESS.getStatusShortDescription())))
		{
			actionForm.setReadOnly(true);
		} else {
			actionForm.setReadOnly(false);
		}
		logger.debug("RegID:"+actionForm.getRegistrationId()+"  InstallBaseStatus StatusId:"+siteReg.getInstallBaseStatus().getStatusId());
		if(siteReg != null && siteReg.getInstallBaseStatus() != null && siteReg.getInstallBaseStatus().getStatusId() != null
				&& (siteReg.getInstallBaseStatus().getStatusId().equalsIgnoreCase(StatusEnum.INPROCESS.getStatusId())
						|| siteReg.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())
						|| siteReg.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()))) {
			logger.debug("RegID:"+actionForm.getRegistrationId()+"  InstallBaseStatus Status:INPROCESS/CANCELLED/COMPLETED");
			getRequest().getSession().setAttribute("installStatus","disable");
			this.setInstallBaseCreationReadOnlyFlag(true);
			this.salMigrationOnlyFlag = false;
		} else if(siteReg.getInstallBaseStatus() != null && siteReg.getInstallBaseStatus().getStatusId().equals(StatusEnum.SAVED.getStatusId())) {
			logger.debug("RegID:"+actionForm.getRegistrationId()+"  InstallBaseStatus Status:SAVED");
			getRequest().getSession().setAttribute("installStatus","saved");
		} else {
			logger.debug("RegID:"+actionForm.getRegistrationId()+"  InstallBaseStatus Status:ELSE");
			getRequest().getSession().removeAttribute("installStatus");
		}
		actionForm.setSaveSiteReg(false);
		if (regTypeId != null
				&& (regTypeId.equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID()))) {
			getRequest().getSession().setAttribute("IPOR","IPOR");
			// Fetching MaterialEntryList from DB
			List<TechnicalOrderDetail> technicalOrderDetailList = getBaseRegistrationService().getTechnicalOrderByType(actionForm.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_IB);
			for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
				technicalOrderDetail.setSelectforRegistration(true);
			}
			actionForm.setMaterialEntryList(technicalOrderDetailList);
			if(StringUtils.isNotEmpty(siteReg.getInventoryFile())){
				actionForm.setIpoInventoryNotLoaded(GRTConstants.NO);
			} else {
				actionForm.setIpoInventoryNotLoaded(GRTConstants.YES);
			}
		}

		logger.debug("<========Leaving the displayRegistration() method... ============> RegID:"+actionForm.getRegistrationId());
		
		return installBaseCreation();
	}

	
	public String updateSiteRegistrationBySuperUser()
			throws Exception {
		validateSession();
		// start-- get parameter registrationId and soldToId from request for
		// action that initiated from extremtable only
		logger.debug("Entering RegistrationController : updateSiteRegistrationBySuperUser");
		
		
		String completeStatusId = "1004";
		getBaseRegistrationService().updateSiteRegistrationBySuperUser(actionForm.getRegistrationId(),completeStatusId, getUserFromSession().getUserId());
		
		//Defect # 765 - Get the lists for registration list page multiselect
		statusList = getBaseRegistrationService().getRegistrationStatus();
		
		regTypeList = getBaseRegistrationService().getRegistrationTypes();
		
		//Prepare JSON for UI functionality
		regListMap = new HashMap<String, Object>();
		//regListMap.put("registrationList", registrationSummaries);
		regListMap.put("statusList", statusList);
		regListMap.put("registrationTypeList", regTypeList);
		
		logger.debug("Exiting RegistrationController : updateSiteRegistrationBySuperUser");

		return Action.SUCCESS;
	}
	
	public String autoGenAttachmentPath(String attachmentName) {
		logger.debug("Entering TechnicalRegistrationAction.autoGenAttachmentPath(String attachmentName)");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_APPEND_ATTACHMENT_NAME);
		String generateAttachmentInPath = getUploadPath(attachmentName + sdf.format(date) + ATTACHMENT_NAME_EXTENSION);
		logger.debug("Exiting TechnicalRegistration.autoGenAttachmentPath(String attachmentName)");
		return generateAttachmentInPath;
	}
	
	public String getUploadPath(String fileName) {
		logger.debug("Entering TechnicalRegistration.getUploadPath(String fileName)");
		String uploadDir = "";
		try {
			uploadDir = getRequest().getSession().getServletContext().getResource("/").getPath();
		} catch (MalformedURLException e) {
			logger.error("Can't find getSession().getServletContext().getResource(\"/\").getPath() in RegistrationController.getUploadPath()");
		} catch (Throwable throwable) {
			try {
				uploadDir = TechnicalRegistration.class.getClassLoader().getResource("/").getPath();
			} catch (Throwable throwable2) {
				// Eat it.
			}
		}
		if (uploadDir != null) {
			uploadDir += DEFAULT_UPLOAD_DIR;
			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			logger.debug("Exiting TechnicalRegistration.getUploadPath(String fileName)");
			return uploadDir + "/" + fileName;
		}
		logger.debug("Exiting TechnicalRegistration.getUploadPath(String fileName)");
		return fileName;
	}
	
	//Buffer OverFlow Server Side Check
	public boolean checkForBufferOverFlow( RegistrationFormBean form ){
		//String middleMsg = "can not be greater than";
		String middleMsg = grtConfig.getScvBufferOvFlwMsgMiddle();
		
		if( form!=null ){
			if( form.getRegistrationIdentifier()!=null && form.getRegistrationIdentifier().length() > 50 ){
				//bufferOverFlowMsg = "Registration Name "+middleMsg+" 50";
				bufferOverFlowMsg = grtConfig.getEwiMessageCodeMap().get("scvBufferOvFlwMsgRegName") + "###" + grtConfig.getScvBufferOvFlwMsgRegName() + " "+middleMsg+" 50";
				return true;
			}

			if( form.getRegistrationId()!=null && form.getRegistrationId().length() > 50 ){
				//bufferOverFlowMsg = "Registration Id "+middleMsg+" 50";
				bufferOverFlowMsg = grtConfig.getEwiMessageCodeMap().get("scvBufferOvFlwMsgRegId") + "###" + grtConfig.getScvBufferOvFlwMsgRegId() + " "+middleMsg+" 50";
				return true;
			}

			if( form.getCompany()!=null && form.getCompany().length() > 150 ){
				//bufferOverFlowMsg = "Company "+middleMsg+" 150";
				bufferOverFlowMsg = grtConfig.getEwiMessageCodeMap().get("scvBufferOvFlwMsgCompany") + "###" + grtConfig.getScvBufferOvFlwMsgCompany() +" " +middleMsg+" 150";
				return true;
			}

			if( form.getSoldToId()!=null && form.getSoldToId().length() > 50 ){
				//bufferOverFlowMsg = "Sold To "+middleMsg+" 150";
				bufferOverFlowMsg = grtConfig.getEwiMessageCodeMap().get("scvBufferOvFlwMsgSoldTo") + "###" + grtConfig.getScvBufferOvFlwMsgSoldTo() + " "+middleMsg+" 150";
				return true;
			}

			Pattern pattern = Pattern.compile("\\d{10}");
			Matcher matcher = pattern.matcher(form.getSoldToId());
			if (!matcher.matches()) {
				//bufferOverFlowMsg = "Invalid SoldTo format"+form.getSoldToId();
				bufferOverFlowMsg = grtConfig.getEwiMessageCodeMap().get("scvBufferOvFlwInvalidSoldToFmt") + "###" + grtConfig.getScvBufferOvFlwInvalidSoldToFmt() +  form.getSoldToId();
				return true;
			}
		}
		return false;
	}
	
	/* GETTERS & SETTERS */
	
	public SiebelClient getSiebelClient() {
		return siebelClient;
	}

	public BaseRegistrationService getBaseRegistrationService() {
		return baseRegistrationService;
	}

	public void setBaseRegistrationService(
			BaseRegistrationService baseRegistrationService) {
		this.baseRegistrationService = baseRegistrationService;
	}

	public void setSiebelClient(SiebelClient siebelClient) {
		this.siebelClient = siebelClient;
	}

	public RegistrationFormBean getActionForm() {
		return actionForm;
	}

	public void setActionForm(RegistrationFormBean actionForm) {
		this.actionForm = actionForm;
	}

	public List<String> getSoldToList() {
		return soldToList;
	}

	public void setSoldToList(List<String> soldToList) {
		this.soldToList = soldToList;
	}

	public String getSoldTo() {
		return StringEscapeUtils.unescapeHtml(soldTo);
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	public AutoCompleteCache getCxpSoldToCache() {
		return cxpSoldToCache;
	}

	public void setCxpSoldToCache(AutoCompleteCache cxpSoldToCache) {
		this.cxpSoldToCache = cxpSoldToCache;
	}

	public List<RegistrationSummary> getRegistrationSummaries() {
		return registrationSummaries;
	}

	public void setRegistrationSummaries(
			List<RegistrationSummary> registrationSummaries) {
		this.registrationSummaries = registrationSummaries;
	}

	public String getNavigateTo() {
		return navigateTo;
	}
	public void setNavigateTo(String navigateTo) {
		this.navigateTo = navigateTo;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public Boolean getInstallBaseCreationReadOnlyFlag() {
		return installBaseCreationReadOnlyFlag;
	}

	public void setInstallBaseCreationReadOnlyFlag(
			Boolean installBaseCreationReadOnlyFlag) {
		this.installBaseCreationReadOnlyFlag = installBaseCreationReadOnlyFlag;
	}

	public GrtConfig getGrtConfig() {
		return grtConfig;
	}

	public void setGrtConfig(GrtConfig grtConfig) {
		this.grtConfig = grtConfig;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getMaxRegListSize() {
		return maxRegListSize;
	}

	public void setMaxRegListSize(int maxRegListSize) {
		this.maxRegListSize = maxRegListSize;
	}

	public int getRegListRowNum() {
		return regListRowNum;
	}

	public void setRegListRowNum(int regListRowNum) {
		this.regListRowNum = regListRowNum;
	}

	public String getStatusList() {
		return statusList;
	}

	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}

	public String getRegTypeList() {
		return regTypeList;
	}

	public void setRegTypeList(String regTypeList) {
		this.regTypeList = regTypeList;
	}

	public Map<String, Object> getRegListMap() {
		return regListMap;
	}

	public void setRegListMap(Map<String, Object> regListMap) {
		this.regListMap = regListMap;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public boolean isAllRegData() {
		return allRegData;
	}

	public void setAllRegData(boolean allRegData) {
		this.allRegData = allRegData;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public RegistrationFormBean getRegObj() {
		return regObj;
	}

	public void setRegObj(RegistrationFormBean regObj) {
		this.regObj = regObj;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getOnsiteEmail() {
		return onsiteEmail;
	}

	public void setOnsiteEmail(String onsiteEmail) {
		this.onsiteEmail = onsiteEmail;
	}

	public String getServiceMsg() {
		return serviceMsg;
	}

	public void setServiceMsg(String serviceMsg) {
		this.serviceMsg = serviceMsg;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getSalMigrationOnlyFlag() {
		return salMigrationOnlyFlag;
	}

	public void setSalMigrationOnlyFlag(Boolean salMigrationOnlyFlag) {
		this.salMigrationOnlyFlag = salMigrationOnlyFlag;
	}
	
	public InstallBaseService getInstallBaseService() {
		return installBaseService;
	}

	public void setInstallBaseService(InstallBaseService installBaseService) {
		this.installBaseService = installBaseService;
	}

	public Map<String, Object> getRegListParameterMap() {
		return regListParameterMap;
	}

	public void setRegListParameterMap(Map<String, Object> regListParameterMap) {
		this.regListParameterMap = regListParameterMap;
	}

	public String getPrFlag() {
		return prFlag;
	}

	public void setPrFlag(String prFlag) {
		this.prFlag = prFlag;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public Map<String, Object> getSearch() {
		return search;
	}

	public void setSearch(Map<String, Object> search) {
		this.search = search;
	}

	public List<Map<String, Object>> getColumns() {
		return columns;
	}

	public void setColumns(List<Map<String, Object>> columns) {
		this.columns = columns;
	}

	public List<Map<String, Object>> getOrder() {
		return order;
	}

	public void setOrder(List<Map<String, Object>> order) {
		this.order = order;
	}

	public Map<String, Object> getDataTableParams() {
		return dataTableParams;
	}

	public void setDataTableParams(Map<String, Object> dataTableParams) {
		this.dataTableParams = dataTableParams;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public TechnicalOnBoardingService getTechnicalOnBoardingService() {
		return technicalOnBoardingService;
	}

	public void setTechnicalOnBoardingService(
			TechnicalOnBoardingService technicalOnBoardingService) {
		this.technicalOnBoardingService = technicalOnBoardingService;
	}

	public String getSkipStepB() {
		return skipStepB;
	}

	public void setSkipStepB(String skipStepB) {
		this.skipStepB = skipStepB;
	}

	public EQRService getEqrService() {
		return eqrService;
	}

	public void setEqrService(EQRService eqrService) {
		this.eqrService = eqrService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public String getBufferOverFlowMsg() {
		return bufferOverFlowMsg;
	}

	public void setBufferOverFlowMsg(String bufferOverFlowMsg) {
		this.bufferOverFlowMsg = bufferOverFlowMsg;
	}
}
