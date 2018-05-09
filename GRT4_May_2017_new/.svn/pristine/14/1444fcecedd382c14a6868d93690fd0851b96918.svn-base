package com.avaya.grt.web.action.technicalonboarding;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.HardwareServer;
import com.avaya.grt.mappers.LocalTRConfig;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.service.technicalonboarding.TechnicalOnBoardingService;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.AUXMCMain;
import com.grt.dto.Account;
import com.grt.dto.Asset;
import com.grt.dto.CMMain;
import com.grt.dto.ManagedDevice;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.SALGateway;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.util.ARTOperationType;
import com.grt.util.AccessTypeEnum;
import com.grt.util.GRTConstants;
import com.grt.util.GenericSort;
import com.grt.util.ProcessStepEnum;
import com.grt.util.SearchParam;
import com.grt.util.StatusEnum;
import com.opensymphony.xwork2.Action;

/**
 * @description This class contains methods related to Technical On-Boarding Flow
 *
 */
public class TechnicalOnBoardingAction extends TechnicalRegistrationAction{

	private static final Logger logger = Logger
			.getLogger(TechnicalOnBoardingAction.class);

	private static final int TECH_REG_SUM_LIST_FETCH_SIZE = 500;

	private List<String> accessTypes = new ArrayList<String>();
	private List<String> accessTypesList=new ArrayList<String>();

	public RegistrationFormBean registrationFormBean = new RegistrationFormBean();

	List <TechnicalRegistrationSummary>  techRegistrationList = new ArrayList<TechnicalRegistrationSummary>();
	List<TechnicalRegistrationSummary> registrableProductList =new ArrayList<TechnicalRegistrationSummary>();

	private String soldToNumber;

	private String registrationId;

	private String materialCode;

	private String gatewaySeid; 

	private String soldToId;

	private String htmlContentStr;

	private TechnicalOnBoardingService technicalOnBoardingService;
	
	private Boolean tobResubmitFlag = false;
	
	private String skipStepB = "none";
	private String popUpValue="";
	private String popUpAccessType="";

	private String message;
	private String tobDashboardDataError;
	private Boolean stepBResult = false;
	
	private Boolean retestLoadStepb=false;
	private InputStream inputStream;
	
	private String hostName;
	
	/**
	 * Action to open the Location selection for technical Onboarding
	 * @return String
	 */
	public String technicalRegistrationOnly() throws Exception{
		validateSession();
		actionForm.setTechnicalRegistrationOnly(true); //TOB
		actionForm.setFirstColumnOnly(true); //TOB
		
		actionForm.setRecordValidationOnly(false); //record validation
		actionForm.setViewInstallbaseOnly(false); //view installbase
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setInstallbaseRegistrationOnly(false); //IB
		actionForm.setFullRegistrationOnly(false);
		actionForm.setSalMigrationOnly(false);		
		
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			actionForm.setNavigateTo("toLocationSelection");
			return "toLocationSelection";
		} else {
			actionForm.setNavigateTo("toAgentLocationSelection");
			return "toAgentLocationSelection";
		}
	}

	/**
	 * GRT 4.0 Changes 
	 * Get Completion Details
	 * @return
	 */
	public String getCompletionDetails() {
		logger.debug("Entering into getCompletionDetails");
		TechnicalRegistration techReg = null;
		String techRegId=actionForm.getTechnicalRegistrationId();
		try {
			techReg = technicalOnBoardingService.fetchTechnicalRegistrationByID(techRegId);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		if (null != techReg){
			StringBuilder sb = new StringBuilder();
			sb.append("<table class=\"basic-table\" width=\"100%\"  border=\"1\" >");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th><b>SECode</b></th> <b><th>SEID</th></b> <b><th>AlarmId</th></b> <b><th>IPAddress</th></b> <b><th>SID</th></b> <b><th>MID</th></b>");
			sb.append("</tr>");
			sb.append("</thead>");
			List<ExpandedSolutionElement> expSolElements = techReg.getExpSolutionElements();
			if(!expSolElements.isEmpty()){
				GenericSort gs = new GenericSort("seID", true);
				Collections.sort(expSolElements, gs);
				for(ExpandedSolutionElement expSolEle : expSolElements){
					sb.append("<tbody>");
					sb.append("<tr>");
					sb.append("<td>").append(expSolEle.getSeCode()).append("</td>");
					sb.append("<td>").append(expSolEle.getSeID()).append("</td>");
					sb.append("<td>").append(expSolEle.getAlarmId()).append("</td>");
					sb.append("<td>").append(expSolEle.getIpAddress()).append("</td>");
					sb.append("<td>").append(expSolEle.getSid()).append("</td>");
					sb.append("<td>").append(expSolEle.getMid()).append("</td>");

					sb.append("</tr>");
					sb.append("</tbody>");
				}
			}
			sb.append("</table>");

			inputStream =new StringBufferInputStream(sb.toString());
		}
		logger.debug("The content of the installScript ==>::"+inputStream);
		logger.debug("Exiting from getCompletionDetails");
		return Action.SUCCESS;
	}
	
	
	/****
	 * GRT 4.0 Changes
	 * Registration Summary List
	 * 
	 * ****/
	/**
	 * Method for get registration summary Detail
	 * 
	 * For New SEID creation
	 * 
	 * @return
	 */
	public String getRegistrationSummaryListDetail() {
		
	try{
		List<SearchParam> searchList = getSearchCondition(actionForm);
		int totalNum = getTechnicalOnBoardingService().getTechnicalRegistrationSummaryListCount(actionForm.getRegistrationId(), searchList);
		if (actionForm.getTechnicalRegistrationPager() == null) {
			actionForm.setTechnicalRegistrationPager(initPager(TECH_REG_SUM_LIST_FETCH_SIZE));
		}
		actionForm.getTechnicalRegistrationPager().setTotalNum(totalNum);
		logger.debug("getTechnicalRegistrationSummaryListCount size: "
				+ totalNum);

		List<TechnicalRegistrationSummary> technicalRegistrationSummaryList = null;
			technicalRegistrationSummaryList = new ArrayList<TechnicalRegistrationSummary>();
			
			//QA Defect 454: Fetch the data only when action form is empty
			if( actionForm.getRegistrationSummaryList()==null ||  actionForm.getRegistrationSummaryList().isEmpty() ){
				registrableProductList = new ArrayList<TechnicalRegistrationSummary>();
				try{
					//Service Call
					technicalRegistrationSummaryList = getTechnicalOnBoardingService().getTechnicalRegistrationSummaryListSearch(actionForm.getSoldToId(),
							actionForm.getRegistrationId(),
							actionForm.getTechnicalRegistrationPager().getOffSet(),
							actionForm.getTechnicalRegistrationPager().getFetchSize(),
							searchList, getUserFromSession().getUserId());
				} catch (Throwable th){
					th.printStackTrace();
					this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobGrtProblemErrMsg") + "###" + grtConfig.getTobGrtProblemErrMsg());				
					this.actionForm.setTobDashboardDataError(grtConfig.getTobTechnicalError1());
				}
				int trListSize = technicalRegistrationSummaryList.size();
				if (trListSize == 0) {
					this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobDataError1") + "###" + grtConfig.getTobDataError1());
					this.actionForm.setTobDashboardDataError(grtConfig.getTobDataError1());
				}
				for (int i=0; i < trListSize; i++) {
					if (!technicalRegistrationSummaryList.get(i).getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_TR_UPDATE)) {

						registrableProductList.add(technicalRegistrationSummaryList.get(i));
					}
				}
				actionForm.setRegistrationSummaryList(registrableProductList);
			}else{
				//Take the data from action form
				registrableProductList = actionForm.getRegistrationSummaryList();
			}
		}catch(Exception Ex){
			Ex.printStackTrace();
		}
		return Action.SUCCESS;
	}
	

	/**
	 * Technical On-boarding Dashboard
	 * @param actionForm RegistrationFormBean
	 * @return
	 * @throws Exception
	 */
	@Override
	public String technicalRegistrationDashboard()
	{

		try {
			validateSession();
			logger.debug(" +++++ Entering Technical Registration Dashboard +++++ ");
			long c1 = Calendar.getInstance().getTimeInMillis();
			this.actionForm.setFailedSeidIsNotNull( false );
			//Feb Release
			this.skipStepB = "none";
			this.tobResubmitFlag = false;
			
			//Update the property in the action Form
			actionForm.setSkipStepB("none");
			actionForm.setTobResubmitFlag(false);
			
			this.setTobDashboardDataError("");
			this.actionForm.setTobDashboardDataError("");
			actionForm.setTrCancel(null);
			String registrationId = actionForm.getRegistrationId();
			String soldToId = actionForm.getSoldToId();
			String changeOwnership = "";
			if (getRequest().getParameter("registrationId") != null) {
				registrationId = getRequest().getParameter("registrationId")
						.toString();
				actionForm.setRegistrationId(registrationId);
			}

			if (getRequest().getParameter("soldToId") != null) {
				soldToId = getRequest().getParameter("soldToId").toString();
				actionForm.setSoldToId(soldToId);
			}

			if (getRequest().getParameter("changeOwnership") != null) {
				changeOwnership = getRequest().getParameter("changeOwnership")
						.toString();
			}

			if (StringUtils.isNotEmpty(changeOwnership)
					&& GRTConstants.TRUE.equalsIgnoreCase(changeOwnership)) {
				changeOwnership(registrationId, getUserFromSession());
			}
			List<SearchParam> searchList = getSearchCondition(actionForm);

			//Service Call
			int totalNum = getTechnicalOnBoardingService().getTechnicalRegistrationSummaryListCount(actionForm.getRegistrationId(), searchList);
			if (actionForm.getTechnicalRegistrationPager() == null) {
				actionForm.setTechnicalRegistrationPager(initPager(TECH_REG_SUM_LIST_FETCH_SIZE));
			}
			actionForm.getTechnicalRegistrationPager().setTotalNum(totalNum);
			logger.debug("getTechnicalRegistrationSummaryListCount size: "
					+ totalNum);
			
			//Service Call
			SiteRegistration siteReg = getTechnicalOnBoardingService().getSiteRegistrationOnRegId(registrationId);
			Status techRegstatus = siteReg.getTechRegStatus();
			String techRegstatusId = techRegstatus.getStatusId();
			if (techRegstatusId.equalsIgnoreCase(StatusEnum.SAVED.getStatusId())){
				actionForm.setTechnicalRegistrationOnly(true);
				actionForm.setBannerStatus(StatusEnum.SAVED.getStatusShortDescription());
				actionForm.setBannerSubStatus("");
				actionForm.setBannerSrNumber("");
				actionForm.setBannerActiveContractSrNumber("");
			}
			if(techRegstatusId.equalsIgnoreCase(StatusEnum.CANCELLED.getStatusId())){
				actionForm.setTrCancel("1");
			}else if (techRegstatusId.equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId())) {
				actionForm.setTechnicalRegistrationOnly(true);
				actionForm.setBannerStatus(StatusEnum.NOTINITIATED.getStatusShortDescription());
				actionForm.setBannerSubStatus("");
				actionForm.setBannerSrNumber("");
				actionForm.setBannerActiveContractSrNumber("");
				//service Call
				getTechnicalOnBoardingService().updateSiteRegistrationProcessStepAndStatus(actionForm.getRegistrationId(), ProcessStepEnum.TECHNICAL_REGISTRATION, StatusEnum.SAVED);
			}
			
			//Defect #768-Registration Name created is not visible on the Registration Site Summary 
			if( siteReg.getRegistrationIdentifier()!=null ){
				actionForm.setRegistrationIdentifier(siteReg.getRegistrationIdentifier());
			}
			
			if( siteReg.getRegistrationNotes()!=null ){
				actionForm.setRegistrationNotes(siteReg.getRegistrationNotes());		
			}
			
			// Set div flags for user selection
			if(actionForm.getReadyForTRDivFlag() == null || StringUtils.isEmpty(actionForm.getReadyForTRDivFlag())){
				actionForm.setReadyForTRDivFlag("none");
			}
			if(actionForm.getTredListDivFlag() == null || StringUtils.isEmpty(actionForm.getTredListDivFlag())){
				actionForm.setTredListDivFlag("none");
			}
			if(actionForm.getTredProdDivFlag() == null || StringUtils.isEmpty(actionForm.getTredProdDivFlag())){
				actionForm.setTredProdDivFlag("none");
			}
			if(logger.isDebugEnabled())
				logger.debug(" +++++ Exiting Technical Registration Dashboard +++++ ");

		} catch(Exception ex){
			ex.printStackTrace();
		}
		//Begin: Clear the Error Messages
		actionForm.setCount(null);
		actionForm.setErrorMessage(null);
		actionForm.setSoldTo(null);
		actionForm.setSalSeId(null);
		//End: Clear the Error Messages
		String isFromTrObDetail = (String) getRequest().getSession().getAttribute(GRTConstants.FROM_TR_OB_DETAIL);
		if(GRTConstants.TRUE.equalsIgnoreCase(isFromTrObDetail)){
		String result = "";
			try {
				result = getTechnicalOrderDetails();
				actionForm.setNavigateTo( result );
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			return result;
		} else {
			//Begin To fix the Cancel button issue in TR Config.
			List<TechnicalRegistrationSummary> summaryListRegistrable =actionForm.getRegistrationSummaryListRegistrable();
			List<TechnicalRegistration> summaryListReTest =actionForm.getTechnicalRegRetest();
			if(summaryListRegistrable != null && summaryListRegistrable.size() > 0)
			{
				actionForm.setTredProdDivFlag("block");
				logger.debug("REG SUMMARY Size Registrable :::" + summaryListRegistrable.size() + "::: TO STRING :::" + summaryListRegistrable.toString());
								
			}else{
			
				List<TechnicalRegistrationSummary>  summaryList =getBaseRegistrationService().getTechnicalRegistrationListByregistrationId(actionForm.getRegistrationId(),"TR"); //actionForm.getRegistrationSummaryListRegistrable();
				
				actionForm.setRegistrationSummaryListRegistrable(summaryList);
				
				actionForm.setTredProdDivFlag("block");
			}
			
			if(summaryListReTest.size()<=0)
			{
				List<TechnicalRegistration>  summaryList =getBaseRegistrationService().getTechnicalRetestRegistrationListByregistrationId(actionForm.getRegistrationId(),"TR_RETEST");
				actionForm.setTechnicalRegRetest(summaryList);
				actionForm.setSelectedSALRegSummaryList(summaryList);
			}

			//End To fix the Cancel button issue in TR Config.
			return "success";
		}
	}
	
/**
 * 
 * @param registrationId
 * @param user
 * @throws Exception
 */
	private void changeOwnership(String registrationId, CSSPortalUser user)
			throws Exception {
		//Service call
		RegistrationSummary registrationSummary = getTechnicalOnBoardingService()
				.getRegistrationSummary(registrationId);
		if (StringUtils.isEmpty(registrationSummary.getUserName())
				|| StringUtils.isNotEmpty(user.getUserId())
				&& !registrationSummary.getUserName().equalsIgnoreCase(
						user.getUserId())) {
			SiteRegistration siteRegistration = new SiteRegistration();
			siteRegistration.setRegistrationId(registrationId);
			siteRegistration.setFirstName(user.getFirstName());
			siteRegistration.setLastName(user.getLastName());
			siteRegistration.setUserName(user.getUserId());
			siteRegistration.setEmail(user.getEmailAddress());
			siteRegistration.setPhone(user.getPhoneNumber());

			//service call
			getTechnicalOnBoardingService().changeOwnership(siteRegistration,
					getUserFromSession().getUserId());
		}
	}
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	private List<SearchParam> getSearchCondition(RegistrationFormBean form) {
		List<SearchParam> searchList = new ArrayList<SearchParam>();
		SearchParam materialCode = form.getMaterialCode();
		SearchParam description = form.getDescription();
		SearchParam remainingQty = form.getRemainingQty();
		if (materialCode.getFieldValue() == null
				|| "".equals(materialCode.getFieldValue())) {
			materialCode.setDisabled(true);
		} else {
			materialCode.setDisabled(false);
		}
		if (description.getFieldValue() == null
				|| "".equals(description.getFieldValue())) {
			description.setDisabled(true);
		} else {
			description.setDisabled(false);
		}
		if (remainingQty.getNumericOpr() == null
				|| "".equals(remainingQty.getNumericOpr())) {
			remainingQty.setDisabled(true);
		} else if (SearchParam.NUMERIC_OPR_LESS_EQUAL_THAN.equals(remainingQty
				.getNumericOpr())) {
			remainingQty.setFieldType(SearchParam.FIELD_TYPE_NUMERIC);
			remainingQty.setDisabled(false);
			remainingQty.setFieldValue("0");
		} else if (SearchParam.NUMERIC_OPR_GREATER_THAN.equals(remainingQty
				.getNumericOpr())) {
			remainingQty.setFieldType(SearchParam.FIELD_TYPE_NUMERIC);
			remainingQty.setFieldValue("0");
			remainingQty.setDisabled(false);
		} else {
			remainingQty.setDisabled(false);
		}
		searchList.add(materialCode);
		searchList.add(description);
		searchList.add(remainingQty);

		return searchList;
	}

	/**
	 * @param materialCode
	 * @return List<String>
	 * @description Get Eligible access types for a particular material code 
	 */
	public String getEligibleAccessTypes(){
		accessTypes = new ArrayList<String>();
		accessTypes.add(materialCode);
		try {	
			Set<String> eligibleAccessTypes = new HashSet<String>();
			//Service Call
			
			eligibleAccessTypes.addAll(getTechnicalOnBoardingService().getEligibleAccessTypes(accessTypes).get(materialCode));
			
			
			if (eligibleAccessTypes.remove(AccessTypeEnum.IP.getDbAccessType())) 
				eligibleAccessTypes.add(AccessTypeEnum.IP.getUiAccessType());

			if (eligibleAccessTypes.remove(AccessTypeEnum.IPO.getDbAccessType())) 
				eligibleAccessTypes.add(AccessTypeEnum.IPO.getUiAccessType());			
			accessTypes.clear();
			accessTypes.add("");
			accessTypes.addAll(eligibleAccessTypes);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Action.SUCCESS;
	}
	
	/**
	 * 
	 * @return RegistrationFormBean
	 * @description Action to show the list of Materials under each registration.
	 * 				
	 */
	public String technicalRegistrationConfig ()
			throws Exception {

		validateSession();
		//Clear Error message
		actionForm.setErrorMessage("");
		//Set Navigation For UI
		actionForm.setNavigateTo(GRTConstants.TOB_CONFIG_PAGE);
		try {
			logger.debug("Entering technicalRegistrationConfig.");
			actionForm.setIsMainSeidFlag("none");
			actionForm.setSolutionElementId("");
			actionForm.setLssOrESSLowerVersionFlag("");
			actionForm.setUpgradedMainCMuseRFASIDFlag("");
			actionForm.setGroupId(null);
			actionForm.setPrimary("");
			actionForm.setSecondary("");
			getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
			getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);
			//Removing the value from actionform
			actionForm.setPrimarySEIDReq(null);
			actionForm.setSecondarySEIDReq(null);
			
			String materialCode = actionForm.getMaterialCodeDescription();
			actionForm.setRedirectionFlag(true);
			//GRT 4.0 Changes-BRS Requirement No-BR-F.011
			if(!GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(actionForm.getPopUpHiddenValue()))
				actionForm.setPopUpHiddenValue("");
			
			popUpValue="";
			popUpAccessType="";
			actionForm.setPopUpHiddenValue("");
			actionForm.setNoConnectivityAccType("");
			
			//GRT 4.0 Changes-BRS Requirement No-BR-F.011
			if(materialCode.contains(GRTConstants.NO_CONNECTIVITY)){
			String[] code=materialCode.split(",");
				materialCode=code[0];
				popUpValue=code[1];
				popUpAccessType=code[2];
				actionForm.setPopUpHiddenValue(popUpValue);
				actionForm.setNoConnectivityAccType(popUpAccessType);
			}
			logger.debug("Material Code :"+materialCode+" Pop Up Value: "+popUpValue);
			String accessType = actionForm.getAccessType();
			if (accessType != null && StringUtils.isNotEmpty(accessType)){
				actionForm.getTechnicalRegistrationSummary().setAccessType(accessType);
			}
			logger.debug("ACCESS TYPE :::"+ accessType);

			String fromTRDetails = (String)getRequest().getSession().getAttribute(GRTConstants.FROM_TR_OB_DETAIL);
			if(!GRTConstants.TRUE.equals(fromTRDetails)) {
				List<TechnicalRegistrationSummary> summaryList = actionForm.getRegistrationSummaryList();
				TechnicalRegistrationSummary techSummary = null;
				for(TechnicalRegistrationSummary technicalRegistrationSummary : summaryList){
					if(materialCode.equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
						techSummary = constructTechRegSummaryCloneObject(technicalRegistrationSummary);
						techSummary.setAddFlag(true);
						actionForm.setRemainingQuantity(technicalRegistrationSummary.getRemainingQty());
						actionForm.setTechnicalRegistrationSummary(techSummary);
						break;
					}
				}
			}		   	
			String bannerAccessType=actionForm.getTechnicalRegistrationSummary().getAccessType();
			
			if(" ".equalsIgnoreCase(popUpAccessType))
				actionForm.setBannerAccessType(popUpValue);
			else
				actionForm.setBannerAccessType(bannerAccessType);
			
			long start = System.currentTimeMillis();
			applyRulesOnTRConfiguration(actionForm);
			long end = System.currentTimeMillis();
			logger.debug("Exiting technicalRegistrationConfig."+start+" : "+end);
		}catch (Exception ex){
			setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobErrWhileProcessingErrMsg") + "###" + grtConfig.getTobErrWhileProcessingErrMsg());
			ex.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	/**
	 * update existing registration details
	 * @return
	 * @throws Exception
	 */
	public String updateTechnicalRegistrationConfig () throws Exception {
		logger.debug("Enter  RegistrationController.updateTechnicalRegistrationConfig()");
		validateSession();
		//Begin: To restrict Refresh Operation
		String trDetailsActive = (String)getRequest().getSession().getAttribute(GRTConstants.TR_DETAILS_ACTIVE);
		if(GRTConstants.TRUE.equals(trDetailsActive)){
			return getTechnicalOrderDetails();
		}
		actionForm.setFailedSeidIsNotNull(false);
		this.tobResubmitFlag = false;
		//End:  To restrict Refresh Operation		
		SiteRegistration siteRegistration   = (SiteRegistration) (getRequest().getSession().getAttribute(GRTConstants.SITE_REGISTRATION));
		TechnicalRegistrationSummary techRegSummary = actionForm.getTechnicalRegistrationSummary();
		TechnicalRegistration techReg = constructTechnicalRegistrationFromTechSum(techRegSummary, actionForm);
		if (techReg == null) {
			return "failure";
		}
		techReg.getTechnicalOrder().setSiteRegistration(siteRegistration);
		List<TechnicalRegistration> trList = new ArrayList<TechnicalRegistration>();
		trList.add(techReg);

		getBaseRegistrationService().saveTechnicalRegistration(techReg);
		//Begin: Clear the Error Messages
		actionForm.setCount(null);
		actionForm.setErrorMessage(null);
		actionForm.setSoldTo(null);
		actionForm.setSalSeId(null);
		//End: Clear the Error Messages
		if(techReg.getOperationType() != null){
			ARTOperationType operationType = getOperationType(techReg.getOperationType());
			try{
				if (null != operationType){
					getBaseRegistrationService().doTechnicalRegistration(siteRegistration, trList, null, operationType);
				}
				return getTechnicalOrderDetails();
			} catch (Exception ex) {
				logger.error("Exception when update TR and submitted TR >> details to ART"+ex.getMessage());
				return "failure";
			}
		} else {
			logger.error("Operation Type is Empty or Null");
			return "failure";
		}
	}

	private void applyRulesOnTRConfiguration(RegistrationFormBean form) throws Exception {
		try {
			logger.debug("Entering applyRulesOnTRConfiguration...");
			
			//Changes for AUXMC
			logger.debug("auxMCShowFlag: " + form.getTechnicalRegistrationSummary().getAuxMCShowFlag());
			logger.debug("isAuxMCSEIDFlag: " + form.getTechnicalRegistrationSummary().getIsAuxMCSEIDFlag());
			logger.debug("auxMCMainSEID: " + form.getTechnicalRegistrationSummary().getAuxMCMainSEID());
			
			form.getTechnicalRegistrationSummary().setAuxMCShowFlag(GRTConstants.NONE);
			actionForm.setAuxMCMainSEIDShowFlag(GRTConstants.NONE);
			actionForm.setAuxMCMainSEIDRequiredFlag(GRTConstants.FALSE);
			actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.NONE);			
			
			List<TRConfig> trConfigList = null;
			Map<String, String> groupIds = new LinkedHashMap<String, String>();
			String seCode = null;
			String materialCode = form.getTechnicalRegistrationSummary()!=null?form.getTechnicalRegistrationSummary().getMaterialCode():"";
			if(materialCode != null && StringUtils.isNotEmpty(materialCode)){
				//Service Call
				trConfigList = getTechnicalOnBoardingService().fetchTRConfigs(materialCode);
			}
			if(trConfigList != null && trConfigList.size() > 0){
				// sorting TRConfigs on description
				GenericSort gs = new GenericSort("groupDescription", true);
				Collections.sort(trConfigList, gs);
				logger.debug("trConfigList Size:  "+trConfigList.size());
				String mainNo = form.getTechnicalRegistrationSummary().getMainCM();
				String AuxMCSEIDFlag = form.getTechnicalRegistrationSummary().getIsAuxMCSEIDFlag();
				for(TRConfig trConfig : trConfigList){
					if (!groupIds.containsValue("") && trConfigList.size() > 1 && !GRTConstants.YES.equalsIgnoreCase(form.getGroupIdChnaged())
							&& mainNo == null && AuxMCSEIDFlag == null) {
						groupIds.put("", "");
					}
					groupIds.put(trConfig.getGroupId(), trConfig.getGroupDescription());
				}
				logger.debug("groupIds Size:  "+groupIds.size());
				form.setTrConfig(trConfigList.get(0)!=null ?trConfigList.get(0):new TRConfig());
				form.setGroupIds(groupIds);
				form.setTrConfigList(trConfigList);
			}
			//BEGIN: NEW CODE : TO FIX THE INTERNAL DEFECT.
			String fromTRDetails = (String)getRequest().getSession().getAttribute(GRTConstants.FROM_TR_OB_DETAIL);
			logger.debug("Is fromTRDetails? - "+fromTRDetails);
			if(StringUtils.isNotEmpty(fromTRDetails) && GRTConstants.TRUE.equals(fromTRDetails)){
				groupIds.remove("");
			}
			String groupId = form.getTechnicalRegistrationSummary().getGroupId();
			logger.debug("Group Id :::" + groupId + "-----------");
			Set<String> eligibleAccessTypes = new HashSet<String>();
			form.setProductConfigurationFlag(GRTConstants.BLOCK);
			form.setReadOnlyForBlankGrpId(false);
			String selectedAccessType = form.getTechnicalRegistrationSummary().getAccessType();
			if (groupIds.containsKey("")) {
				form.getTechnicalRegistrationSummary().setGroupId("");
				form.getTrConfig().setProductTypeDescription("");
				form.getTrConfig().setSeCodeDescription("");
				form.setTemplateFlag(GRTConstants.NONE);
				form.setProductConfigurationFlag(GRTConstants.NONE);
				form.setCmMainFlag(GRTConstants.NONE);
				form.setSalMigrationFlag(GRTConstants.NONE);
				form.setReadOnlyForBlankGrpId(true);
				return;
			} else if(groupId != null){
				//Service Call
				eligibleAccessTypes = getTechnicalOnBoardingService().getEligibleAccessTypesByGroupId(groupId);
				eligibleAccessTypes = eligibleAccessTypeDbToUiCoversion(eligibleAccessTypes);
				//GRT 4.0 Changes-BRS Requirement No-BR-F.011
				if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(selectedAccessType)){
					actionForm.setNoConnectivityAccType(" ");
					popUpAccessType=" ";
					popUpValue=selectedAccessType;
				}else{
					if(!eligibleAccessTypes.contains(selectedAccessType))
						eligibleAccessTypes.add(selectedAccessType);
				}
				
				//GRT 4.0 changes-BRS Requirement No-BR-F.011
				if(popUpValue.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) && popUpAccessType.equalsIgnoreCase(" "))
					eligibleAccessTypes.add("");
				groupIds.remove("");
			} else if (groupId == null){
				//Initial Condition i.e. when launch the TRConfig from TRDashboard / when the AccessType Selected.
				List<TRConfig> trConfList = form.getTrConfigList();
				for(TRConfig trConfig : trConfList){
					//Service Call
					eligibleAccessTypes = getTechnicalOnBoardingService().getEligibleAccessTypesByGroupId(trConfig.getGroupId());
					eligibleAccessTypes = eligibleAccessTypeDbToUiCoversion(eligibleAccessTypes);
					//GRT 4.0 changes-BRS Requirement No-BR-F.011
					if(popUpValue.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) && popUpAccessType.equals(" "))
						eligibleAccessTypes.add("");
					
					if(eligibleAccessTypes.contains(selectedAccessType)){
						groupId = trConfig.getGroupId();
						break;
					}
				}
			}
			
			//END: NEW CODE: TO FIX THE INTERNAL DEFECT.
			logger.debug("populateTRConfig GroupID :"+groupId);
			if(groupId != null){
				List<TRConfig> trConfList = form.getTrConfigList();
				for(TRConfig trConfig : trConfList){
					if(groupId.equalsIgnoreCase(trConfig.getGroupId())){
						TechnicalRegistrationSummary summary = form.getTechnicalRegistrationSummary();
						summary.setGroupId(groupId);
						form.setTrConfig(trConfig);
						form.setSpecialNote(trConfig.getSpecial_note());
						//Service Call
						summary.setSeCodePreview(getTechnicalOnBoardingService().getGroupSeCodes(groupId));
						break;
					}
				}
			}
			form.setGroupIds(form.getGroupIds());

			if(form.getTrConfig() != null) {
				seCode = form.getTrConfig().getSeCode();
			}

			form.setEligibleAccessType(eligibleAccessTypes);
			//Begin: If user changed the groupId then reset the AccessTypes
			List<String> eligibleAccessTypesList = new ArrayList<String>();
			if(eligibleAccessTypes != null && eligibleAccessTypes.size()>0 ){
				eligibleAccessTypesList = new ArrayList<String>(eligibleAccessTypes);
				form.setEligibleAccessTypesList(eligibleAccessTypesList);
			}else{
				form.setEligibleAccessTypesList(eligibleAccessTypesList); 
			}
			if(GRTConstants.YES.equals(form.getGroupIdChnaged())){
				if(form.getEligibleAccessTypesList() != null && form.getEligibleAccessTypesList().size()>0 && selectedAccessType == null){
					form.getTechnicalRegistrationSummary().setAccessType(eligibleAccessTypesList.get(0));
				}
				if(GRTConstants.SAL.equalsIgnoreCase(form.getTechnicalRegistrationSummary().getAccessType())){
					getRequest().getSession().setAttribute("salGWFlag", "true");
				}
				form.setGroupIdChnaged(null);
			}
			//End: If user changed the groupId then reset the AccessTypes
			String accessType = (form.getTechnicalRegistrationSummary().getAccessType() != null) ? form.getTechnicalRegistrationSummary().getAccessType().trim():"";
			
			//GRT 4.0 Changes-BRS Requirement No-BR-F.011
			if(popUpValue.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) && actionForm.getNoConnectivityAccType().equalsIgnoreCase(" ")){
				accessType=popUpValue;
				form.getTechnicalRegistrationSummary().setAccessType(accessType);
			}
			
			logger.debug("++++++++++++++ACCESS TYPE  :::" + accessType + "---" + accessType.length());
			if (accessType.length() ==0){
				form.getTechnicalRegistrationSummary().setAccessType(form.getTechnicalRegistrationSummary().getAccessTypes());
			}
			logger.debug("++++++++++++++ACCESS TYPE1  :::"+form.getEligibleAccessType().toString());
			form.setSalMigrationFlag(GRTConstants.NONE);
			form.setIpAddressFlag(GRTConstants.NONE);
			form.setOutboundPrefixFlag(GRTConstants.NONE);
			form.setDialInFlag(GRTConstants.NONE);
			form.setSidMidErrorFlag(GRTConstants.NONE);
			form.setMainSeIdFlag(GRTConstants.NONE);
			if(form.getFailedSeidIsNotNull()){
				form.setReadOnly(true); 
			} else {
				form.setReadOnly(false);
			}
			form.setSidMidValidatedFlag(GRTConstants.NONE);
			form.setDuplicateSidMidFlag(GRTConstants.NONE);
			List<SALGateway> salGatewayMigrationList = null;
			if(form.getTechnicalRegistrationSummary().getAccessType() != null
					&& form.getTechnicalRegistrationSummary().getAccessType().equalsIgnoreCase(GRTConstants.SAL)){
				// Set Primary and Secondary SAL SEIDs
				if(form.getTechnicalRegistrationSummary().getPrimarySalgwSeid() != null){
					getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID, form.getTechnicalRegistrationSummary().getPrimarySalgwSeid());
				}
				if(form.getTechnicalRegistrationSummary().getSecondarySalgwSeid() != null){
					getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID, form.getTechnicalRegistrationSummary().getSecondarySalgwSeid());
				}

				salGatewayMigrationList = getTechnicalOnBoardingService().getSALGateways(form.getSoldToId(), null, GRTConstants.SAL);
				for(SALGateway sgw : salGatewayMigrationList){
					sgw.setPrimaryChecked(false);
					sgw.setSecondaryChecked(false);
				}
				form.setSalGatewayMigrationList(salGatewayMigrationList);
				form.setSalMigrationFlag(GRTConstants.BLOCK);
				if(StringUtils.isNotEmpty(seCode) && (seCode.equalsIgnoreCase(GRTConstants.SAL_GATEWAY) || seCode.equalsIgnoreCase(GRTConstants.SAL_VIRTUAL_GATEWAY))) {
					form.setSalMigrationFlag(GRTConstants.NONE);
				}
				
			} else if(form.getTechnicalRegistrationSummary().getAccessType() != null
					&& form.getTechnicalRegistrationSummary().getAccessType().equalsIgnoreCase(AccessTypeEnum.MODEM.getUiAccessType())){
				form.setOutboundPrefixFlag(GRTConstants.BLOCK);
				form.setDialInFlag(GRTConstants.BLOCK);
			} else if(form.getTechnicalRegistrationSummary().getAccessType() != null
					&& form.getTechnicalRegistrationSummary().getAccessType().equalsIgnoreCase(AccessTypeEnum.IP.getUiAccessType())){
				form.setIpAddressFlag(GRTConstants.BLOCK);
			}
			String template = form.getTrConfig().getTemplate();
			logger.debug("TEMPLATE is :::" + template);
			if (template == null) {
				form.setTemplateFlag(GRTConstants.NONE);
			} else {
				form.setTemplateFlag(GRTConstants.BLOCK);
				form.setTemplate(template);
			}
			form.setSoftwareReleaseFlag(GRTConstants.NONE);
			form.setSpReleaseFlag(GRTConstants.NONE);
			form.setHardwareServerFlag(GRTConstants.NONE);
			List<String> softwareReleases = new ArrayList<String>();
			boolean sslVpn = false;
			if (accessType.equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SSLVPN)) {
				sslVpn = true;
			}
			if (groupId != null) {
				//Service Call
				softwareReleases = getTechnicalOnBoardingService().getReleasesByGroupId(groupId, sslVpn);
			}
			
			if (!softwareReleases.isEmpty()){
				//Service Call
				List<String> spRelease = getTechnicalOnBoardingService().getSPVersionsByGroupId (groupId, softwareReleases.get(0));
				if (spRelease != null && !spRelease.isEmpty() && template != null){
					form.setSpRelease(spRelease);
					form.setSpReleaseFlag(GRTConstants.BLOCK);
					//Defect - 190 - A gateway dropdown is present on the TOB Configuration screen where it should not be
					//Commenting below for above defect fix
					//Service call
					//List<HardwareServer> hardwareServers = getTechnicalOnBoardingService().getHardwareServersTemplate(template);
					//if (hardwareServers != null && !hardwareServers.isEmpty()){
					//	List<String> hardwareServerList = new ArrayList<String>();
					//	for (HardwareServer server : hardwareServers) {
					//		hardwareServerList.add(server.getHardwareServerDescription());
					//	}
					//	form.setHardwareServer(hardwareServerList);
					//	form.setHardwareServerFlag(GRTConstants.BLOCK);
					//}
				}
				else {
					form.setSpReleaseFlag(GRTConstants.NONE);
				}
				
				//Add default empty to Software Release..
				softwareReleases.add(0, "Choose");
				form.setSoftwareRelease(softwareReleases);
				form.setSoftwareReleaseFlag(GRTConstants.BLOCK);
			}
			//Service Call
			List<LocalTRConfig> localTRConfigList = getTechnicalOnBoardingService().getLocalTRConfig(groupId);
			if(localTRConfigList != null && localTRConfigList.size() > 0) {
				LocalTRConfig config = localTRConfigList.get(0);
				logger.debug("localTRConfigList.size() is :::" + localTRConfigList.size());
				boolean cmProduct = config.isCmProduct();
				if (cmProduct){
					logger.debug("Value of cmProduct is  :::" + cmProduct);
					form.setCmMainFlag(GRTConstants.BLOCK);
					form.getTechnicalRegistrationSummary().setCmProduct(true);
				
				} else {
					form.getTechnicalRegistrationSummary().setCmProduct(false);
					form.getTechnicalRegistrationSummary().setRemoteDeviceType("");
					form.setCmMainFlag(GRTConstants.NONE);
				}
				String connectivity = config.getConnectivityType()!=null?config.getConnectivityType():"";
				if(logger.isDebugEnabled())
					logger.debug("connectivityTpe is :::"+ connectivity+" localTR ConfigId = "+config.getLocalTRConfigId()+" alarmEligible = "+config.isAlarmOriginationEligible());
				
				List<String> connectivities = new ArrayList<String>();
				form.setConnectivityFlag(GRTConstants.BLOCK); 
				List<String> alarmOriginations = new ArrayList<String>();

				boolean doesProductSupportAlarm = config.isAlarmOriginationEligible();	
				form.setAlarmOriginationFlag(doesProductSupportAlarm?GRTConstants.BLOCK:GRTConstants.NONE);
				form.setAlarmEligible(doesProductSupportAlarm);
				if (connectivity.equalsIgnoreCase(GRTConstants.DEFAULT_CONNECTIVITY_TYPE) && !accessType.equalsIgnoreCase(GRTConstants.SAL) && !accessType.equalsIgnoreCase("No Connectivity")){
					connectivities.clear();
					connectivities.add(GRTConstants.NO);
					form.getTechnicalRegistrationSummary().setConnectivity(null);
					form.setConnectivities(connectivities);				
					form.setAlarmOriginationFlag(GRTConstants.NONE);
				}
				else if (connectivity.equalsIgnoreCase(GRTConstants.FULL) && !accessType.equalsIgnoreCase(GRTConstants.SAL)){
					connectivities.clear();
					connectivities.add(GRTConstants.YES);
					form.getTechnicalRegistrationSummary().setConnectivity(null);
					form.setConnectivities(connectivities);				 
					alarmOriginations.add(GRTConstants.YES);
				}
				else if (connectivity.equalsIgnoreCase(GRTConstants.BOTH) && !accessType.equalsIgnoreCase(GRTConstants.SAL)){
					connectivities.clear();
					connectivities.add(GRTConstants.YES);
					connectivities.add(GRTConstants.NO);
					form.getTechnicalRegistrationSummary().setConnectivity(null);
					form.setConnectivities(connectivities);
					alarmOriginations.add(GRTConstants.YES);
					alarmOriginations.add(GRTConstants.NO);
				} else if ( accessType.equalsIgnoreCase(GRTConstants.SAL)) {
					connectivities.clear();
					connectivities.add(GRTConstants.NO);
					form.getTechnicalRegistrationSummary().setConnectivity(null);
					form.setConnectivities(connectivities);
					form.setConnectivityFlag(GRTConstants.NONE);
					form.setAlarmOriginationFlag(GRTConstants.NONE);
				}
				
				if(accessType.equalsIgnoreCase("No Connectivity"))
				{
					connectivities.clear();
					connectivities.add(GRTConstants.NO);
					form.getTechnicalRegistrationSummary().setConnectivity(null);
					form.setConnectivities(connectivities);
					form.setSoftwareReleaseFlag(GRTConstants.NONE);
					form.setAlarmOriginationFlag(GRTConstants.NONE);
					form.setPrivateIpEligibleFlag(GRTConstants.NONE);
					form.setRandomPasswordFlag(GRTConstants.NONE);
					alarmOriginations.clear();
					alarmOriginations.add(GRTConstants.NO);
				}
				if(popUpValue.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY)){
					connectivities.clear();
					connectivities.add(GRTConstants.NO);
					alarmOriginations.clear();
					alarmOriginations.add(GRTConstants.NO);
				}
				if(actionForm.getPopUpHiddenValue().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) || actionForm.getNoConnectivityAccType().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY))
				{
					connectivities.clear();
					connectivities.add(GRTConstants.NO);
					actionForm.setPopUpHiddenValue(GRTConstants.NO_CONNECTIVITY);
					alarmOriginations.clear();
					alarmOriginations.add(GRTConstants.NO);
				}
				form.setAlarmOriginations(alarmOriginations);
				form.setUserNamePasswordFlag(GRTConstants.NONE);
				form.setSidMidShowFlag(GRTConstants.NONE);
				form.setSidMidRequiredFlag(GRTConstants.FALSE);
				String productType = form.getTrConfig().getProductType();
				logger.debug("Product Type :::" + productType);
				seCode = form.getTrConfig().getSeCode();
				logger.debug("SE Code :::" + seCode);
				form.getTechnicalRegistrationSummary().setSolutionElement(seCode);
				String mainNo = form.getTechnicalRegistrationSummary().getMainCM();
				logger.debug("CmMain selection :::" + mainNo);
				boolean highAvailablity = false;
				String specialNote = form.getSpecialNote();
				logger.debug("### Special Note :" + specialNote);
				if (GRTConstants.CAAS.equalsIgnoreCase(specialNote) && !seCode.equalsIgnoreCase(GRTConstants.SECODE.VCM) ) {
					form.setSidMidShowFlag(GRTConstants.BLOCK);
					form.setSidMidRequiredFlag(GRTConstants.TRUE);
				} else if ((productType.equalsIgnoreCase(GRTConstants.AACC) && template.equalsIgnoreCase(GRTConstants.AACC_TEMPLATE))
						|| (cmProduct && (!productType.equalsIgnoreCase(GRTConstants.SPTEM) && !productType.equalsIgnoreCase(GRTConstants.MBT) ))
						|| (productType.equalsIgnoreCase(GRTConstants.COLENV) && (seCode.equals(GRTConstants.SECODE.AACEMS) 
								|| seCode.equals(GRTConstants.SECODE.CNTXSR) || seCode.equals(GRTConstants.SECODE.ACDBA) 
								|| seCode.equalsIgnoreCase(GRTConstants.SECODE.WAECE) || seCode.equalsIgnoreCase(GRTConstants.SECODE.WEBRTC) 
								|| seCode.equalsIgnoreCase(GRTConstants.SECODE.RTSCE)
								|| seCode.equalsIgnoreCase(GRTConstants.SECODE.PSEDP) ))) {
					form.setSidMidShowFlag(GRTConstants.BLOCK);
					form.setSidMidRequiredFlag(GRTConstants.TRUE);
				} else if (productType.equalsIgnoreCase(GRTConstants.RADVSN)
						|| (productType.equalsIgnoreCase(GRTConstants.MX) && (seCode.equalsIgnoreCase(GRTConstants.SECODE_MXCB) || seCode.equalsIgnoreCase(GRTConstants.SECODE_MXCV) || seCode.equalsIgnoreCase(GRTConstants.SECODE_MXMSI)))) {
					form.setSidMidShowFlag(GRTConstants.BLOCK);
					form.setSidMidRequiredFlag(GRTConstants.FALSE);
					if (productType.equalsIgnoreCase(GRTConstants.RADVSN)) {
						form.getTechnicalRegistrationSummary().setMid(GRTConstants.NUMBER_ONE);
					}
				} else if (mainNo != null && cmProduct && (productType.equalsIgnoreCase(GRTConstants.SPTEM) || productType.equalsIgnoreCase(GRTConstants.MBT)) 
						&& mainNo.equalsIgnoreCase(GRTConstants.NO)){
					form.setSidMidShowFlag(GRTConstants.BLOCK);
					form.setSidMidRequiredFlag(GRTConstants.TRUE);
				} else if ((seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCWSW) && GRTConstants.HIGH_AVAILABILITY.equalsIgnoreCase(specialNote))
						|| (seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCMSL) && GRTConstants.MEDIA_SERVER.equalsIgnoreCase(specialNote))
						|| (seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCMST) && GRTConstants.BASE_SERVER.equalsIgnoreCase(specialNote))) {
					form.setSidMidShowFlag(GRTConstants.BLOCK);
					form.setSidMidRequiredFlag(GRTConstants.TRUE);
					form.getTechnicalRegistrationSummary().setMid(GRTConstants.NUMBER_ONE);
					highAvailablity = true;
				} else if(!productType.equalsIgnoreCase(GRTConstants.SPTEM)){
					form.getTechnicalRegistrationSummary().setSid(null);
					form.getTechnicalRegistrationSummary().setMid(null);
				}
				form.setHighAvailablity(highAvailablity);
				if (GRTConstants.CAAS.equalsIgnoreCase(specialNote) && seCode.equalsIgnoreCase(GRTConstants.SECODE.VCM)) {
					this.actionForm.setDisableCmRemote(true);
				} else {
					this.actionForm.setDisableCmRemote(false);
				}
				if (GRTConstants.CAAS.equalsIgnoreCase(specialNote) && seCode.equalsIgnoreCase(GRTConstants.SECODE.ACCCM)) {
					form.setUserNamePasswordFlag(GRTConstants.BLOCK);
				}
				boolean randomPasswordEligible = localTRConfigList.get(0).isRandomPasswordEligible();
				logger.debug("randomPassword is :::"+ randomPasswordEligible);
				if (randomPasswordEligible){
					form.setRandomPasswordFlag(GRTConstants.BLOCK);
				} else {
					form.setRandomPasswordFlag(GRTConstants.NONE);
				}
				boolean privateIpEligible = localTRConfigList.get(0).isPrivateIpEligible();
				logger.debug("privateIP is :::"+ privateIpEligible);
				if (privateIpEligible){
					form.setPrivateIpEligibleFlag(GRTConstants.BLOCK);
					if (form.getTechnicalRegistrationSummary().getAccessType().equalsIgnoreCase(AccessTypeEnum.IPO.getUiAccessType())){
						form.setPrivateIpEligibleFlag(GRTConstants.NONE);
					}
				} else {
					form.setPrivateIpEligibleFlag(GRTConstants.NONE);
				}
				
				 if(popUpValue.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) || actionForm.getPopUpHiddenValue().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY))
				 {
					 form.setPrivateIpEligibleFlag(GRTConstants.NONE);
					 form.setRandomPasswordFlag(GRTConstants.NONE);
				 }
				boolean seidOfVoicePortal = localTRConfigList.get(0).isVpmsldnEligibleSeid();
				logger.debug("vpmEligible is :::"+ seidOfVoicePortal);
				if (seidOfVoicePortal){
					form.setSeidOfVoicePortalFlag(GRTConstants.BLOCK);
				} else {
					form.setSeidOfVoicePortalFlag(GRTConstants.NONE);
				}
				boolean sampBoardUpgraded = localTRConfigList.get(0).isFoptionEligible();
				logger.debug("foOption is :::"+ sampBoardUpgraded);
				if (sampBoardUpgraded){
					form.setSampBoardUpgradedFlag(GRTConstants.BLOCK);
				} else {
					form.setSampBoardUpgradedFlag(GRTConstants.NONE);
				}
				boolean checkIfSESEdge = localTRConfigList.get(0).isDocrEligible();
				logger.debug("docEligible is :::"+ checkIfSESEdge);
				if (checkIfSESEdge){
					form.setCheckIfSESEdgeFlag(GRTConstants.BLOCK);
				} else {
					form.setCheckIfSESEdgeFlag(GRTConstants.NONE);
				}
				boolean authFile = localTRConfigList.get(0).isAuthFileIdEligible();
				logger.debug("authFile is :::"+ authFile);
				if (authFile){
					form.setAuthFileIDFlag(GRTConstants.BLOCK);
				} else {
					form.setAuthFileIDFlag(GRTConstants.NONE);
				}
				
				//Changes for AUXMC
				if(specialNote != null && GRTConstants.AUXMC.equalsIgnoreCase(specialNote)){
					form.getTechnicalRegistrationSummary().setAuxMCShowFlag(GRTConstants.BLOCK);
					if(form.getTechnicalRegistrationSummary().getIsAuxMCSEIDFlag() != null && GRTConstants.YES.equalsIgnoreCase(form.getTechnicalRegistrationSummary().getIsAuxMCSEIDFlag())){
						form.setAuxMCMainSEIDShowFlag(GRTConstants.BLOCK);
						form.setAuxMCMainSEIDRequiredFlag(GRTConstants.TRUE);
					}
				}
				
			}

			logger.debug("Exiting applyRulesOnTRConfiguration.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private Set<String> eligibleAccessTypeDbToUiCoversion(Set<String> eligibleAccessTypes) throws Exception {
		if(eligibleAccessTypes != null){
			if (eligibleAccessTypes.contains(AccessTypeEnum.IP.getDbAccessType())) {
				eligibleAccessTypes.remove(AccessTypeEnum.IP.getDbAccessType());
				eligibleAccessTypes.add(AccessTypeEnum.IP.getUiAccessType());
			}
			if (eligibleAccessTypes.contains(AccessTypeEnum.IPO.getDbAccessType())) {
				eligibleAccessTypes.remove(AccessTypeEnum.IPO.getDbAccessType());
				eligibleAccessTypes.add(AccessTypeEnum.IPO.getUiAccessType());
			}
		}
		return eligibleAccessTypes;
	}

	private TechnicalRegistrationSummary constructTechRegSummaryCloneObject(TechnicalRegistrationSummary techRegSummary){
		TechnicalRegistrationSummary technicalRegistrationSummary = new TechnicalRegistrationSummary();
		if(techRegSummary != null){
			technicalRegistrationSummary.setGroupId(techRegSummary.getGroupId());
			technicalRegistrationSummary.setRegistrationId(techRegSummary.getRegistrationId());
			technicalRegistrationSummary.setOrderId(techRegSummary.getOrderId());
			technicalRegistrationSummary.setMaterialCode(techRegSummary.getMaterialCode());
			technicalRegistrationSummary.setMaterialCodeDescription(techRegSummary.getMaterialCodeDescription());
			technicalRegistrationSummary.setSolutionElement(techRegSummary.getSolutionElement());
			technicalRegistrationSummary.setSolutionElementId(techRegSummary.getSolutionElementId());
			technicalRegistrationSummary.setSerialNo(techRegSummary.getSerialNo());
			technicalRegistrationSummary.setInitialQty(techRegSummary.getInitialQty());
			technicalRegistrationSummary.setRemainingQty(techRegSummary.getRemainingQty());
			technicalRegistrationSummary.setCreatedDate(techRegSummary.getCreatedDate());
			technicalRegistrationSummary.setProcessStep(techRegSummary.getProcessStep());
			technicalRegistrationSummary.setStatusId(techRegSummary.getStatusId());
			technicalRegistrationSummary.setBaseUnit(techRegSummary.isBaseUnit());
			technicalRegistrationSummary.setInstallBaseStatusId(techRegSummary.getInstallBaseStatusId());
			technicalRegistrationSummary.setTechRegStatusId(techRegSummary.getTechRegStatusId());
			technicalRegistrationSummary.setFinalValidationStatusId(techRegSummary.getFinalValidationStatusId());
			technicalRegistrationSummary.setIPOEligible(techRegSummary.isIPOEligible());
			technicalRegistrationSummary.setProductLine(techRegSummary.getProductLine());
			technicalRegistrationSummary.setOrderType(techRegSummary.getOrderType());
			technicalRegistrationSummary.setSeId(techRegSummary.getSeId());
			technicalRegistrationSummary.setSid(techRegSummary.getSid());
			technicalRegistrationSummary.setMid(techRegSummary.getMid());
			technicalRegistrationSummary.setSalSeIdPrimarySecondary(techRegSummary.getSalSeIdPrimarySecondary());
			technicalRegistrationSummary.setPrimarySalgwSeid(techRegSummary.getPrimarySalgwSeid());
			technicalRegistrationSummary.setSecondarySalgwSeid(techRegSummary.getSecondarySalgwSeid());
			technicalRegistrationSummary.setEligibleAccessTypes(techRegSummary.getEligibleAccessTypes());
			technicalRegistrationSummary.setAccessType(techRegSummary.getAccessType());
			technicalRegistrationSummary.setAccessTypes(techRegSummary.getAccessTypes());
			// ToB Config Update specific fields
			technicalRegistrationSummary.setIpAddress(techRegSummary.getIpAddress());
			technicalRegistrationSummary.setDialInNumber(techRegSummary.getDialInNumber());
			technicalRegistrationSummary.setNickName(techRegSummary.getNickName());
			technicalRegistrationSummary.setAuthFileID(techRegSummary.getAuthFileID());
			technicalRegistrationSummary.setSoftwareRelease(techRegSummary.getSoftwareRelease());
			//ToB Config specific fields
			technicalRegistrationSummary.setCmProduct(techRegSummary.isCmProduct());
			technicalRegistrationSummary.setCmMainMaterialCodeDesc(techRegSummary.getCmMainMaterialCodeDesc());
			technicalRegistrationSummary.setCmMainSID(techRegSummary.getCmMainSID());
			technicalRegistrationSummary.setCmMainsoldTo(techRegSummary.getCmMainsoldTo());
			technicalRegistrationSummary.setEligibleAccessTypes(techRegSummary.getEligibleAccessTypes());
			technicalRegistrationSummary.setMainCM(techRegSummary.getMainCM());
			technicalRegistrationSummary.setConnectivity(techRegSummary.getConnectivity());
			technicalRegistrationSummary.setAlarmOrigination(techRegSummary.getAlarmOrigination());
			technicalRegistrationSummary.setTrConfig(techRegSummary.getTrConfig());
			technicalRegistrationSummary.setAction(techRegSummary.getAction());
			technicalRegistrationSummary.setCheckIfSESEdge(techRegSummary.getCheckIfSESEdge());
			technicalRegistrationSummary.setOutboundPrefix(techRegSummary.getOutboundPrefix());
			technicalRegistrationSummary.setRandomPassword(techRegSummary.getRandomPassword());
			technicalRegistrationSummary.setRandomPassworddb(techRegSummary.getRandomPassworddb());
			technicalRegistrationSummary.setPrivateIP(techRegSummary.getPrivateIP());
			technicalRegistrationSummary.setHardwareServer(techRegSummary.getHardwareServer());
			technicalRegistrationSummary.setOperationType(techRegSummary.getOperationType());
			technicalRegistrationSummary.setSampBoardUpgraded(techRegSummary.getSampBoardUpgraded());
			technicalRegistrationSummary.setSeidOfVoicePortal(techRegSummary.getSeidOfVoicePortal());
			technicalRegistrationSummary.setUserName(techRegSummary.getUserName());
			technicalRegistrationSummary.setPassword(techRegSummary.getPassword());
			technicalRegistrationSummary.setEquipmentNumber(techRegSummary.getEquipmentNumber());
			technicalRegistrationSummary.setAlarmId(techRegSummary.getAlarmId());
			technicalRegistrationSummary.setAuxMCShowFlag(techRegSummary.getAuxMCShowFlag());
			technicalRegistrationSummary.setIsAuxMCSEIDFlag(techRegSummary.getIsAuxMCSEIDFlag());
			technicalRegistrationSummary.setAuxMCMainSEID(techRegSummary.getAuxMCMainSEID());
			technicalRegistrationSummary.setAuxMCSid(techRegSummary.getAuxMCSid());
			technicalRegistrationSummary.setAuxMCMid(techRegSummary.getAuxMCMid());
		}
		return technicalRegistrationSummary;
	}

	/**
	 * get existing technical registration record
	 * @return
	 */
	public String getExistingTOBRecords() {
		try {
			if(actionForm.getRegistrationSummaryListTRUpdate()!=null &&
					!actionForm.getRegistrationSummaryListTRUpdate().isEmpty()) {
				techRegistrationList =  actionForm.getRegistrationSummaryListTRUpdate();
			}else{
				//Service Call
				techRegistrationList = getTechnicalOnBoardingService().getExistingTOBRecords(soldToNumber,registrationId);
				if(actionForm!=null){
					registrationFormBean.setRegistrationSummaryListTRUpdate(techRegistrationList);
					actionForm.setRegistrationSummaryListTRUpdate(techRegistrationList);
					//saveFormBeanInSession(actionForm);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error retrieving existing TOB records "+e);			
			setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobRetrieveErrMsg") + "###" + grtConfig.getTobRetrieveErrMsg());
		}
		return Action.SUCCESS;
	}

	/**
	 * Action.
	 * update existing registration 
	 * @return
	 * @throws Exception
	 */
	public String technicalRegistrationUpdate() throws Exception {
		logger.debug("Entering technicalRegistrationUpdate.");

		validateSession();
		try{
			actionForm.setSidMidErrorFlag(GRTConstants.NONE);
			getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
			getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);
			TechnicalRegistrationSummary technicalRegistrationSummary = null;
			String fromTRDetails = (String)getRequest().getSession().getAttribute(GRTConstants.FROM_TR_OB_DETAIL);
			if(!GRTConstants.TRUE.equals(fromTRDetails)) {
				List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
				for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
					if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
						if(tecRegSummary != null && tecRegSummary.getSeId().equals(actionForm.getSolutionElementId())
								&& tecRegSummary.getMaterialCode().equalsIgnoreCase(actionForm.getMaterialCodeDescription())){
							technicalRegistrationSummary = constructTechRegSummaryCloneObject(tecRegSummary);
							break;
						}
					}
				}
			} else {
				technicalRegistrationSummary = actionForm.getTechnicalRegistrationSummary();
			}
			if(technicalRegistrationSummary != null){
				logger.debug("Material Code :"+technicalRegistrationSummary.getMaterialCode());
				logger.debug("Material Code Description :"+technicalRegistrationSummary.getMaterialCodeDescription());
				logger.debug("SE Code :"+technicalRegistrationSummary.getSolutionElement());
				
			}

			// Preparing accessType List
			Map<String, String> accessTypeMap = new HashMap<String, String>();
			accessTypeMap.put("", "Choose");			
			
			Set<String> eligibleAccessTypes = getTechnicalOnBoardingService().getEligibleAccessTypes(technicalRegistrationSummary.getMaterialCode());
						
			if(eligibleAccessTypes != null && eligibleAccessTypes.size() > 0){
				if(eligibleAccessTypes != null && eligibleAccessTypes.contains(AccessTypeEnum.MODEM.getDbAccessType())){
					accessTypeMap.put(AccessTypeEnum.MODEM.getDbAccessType(), AccessTypeEnum.MODEM.getDbAccessType());
				}
				if(eligibleAccessTypes != null && eligibleAccessTypes.contains(AccessTypeEnum.IPO.getDbAccessType())){
					accessTypeMap.put(AccessTypeEnum.IPO.getDbAccessType(), AccessTypeEnum.IPO.getUiAccessType());
				}
				if(eligibleAccessTypes != null && eligibleAccessTypes.contains(AccessTypeEnum.SAL.getDbAccessType())){
					accessTypeMap.put(AccessTypeEnum.SAL.getDbAccessType(), AccessTypeEnum.SAL.getUiAccessType());
				}
				if(eligibleAccessTypes != null && eligibleAccessTypes.contains(AccessTypeEnum.IP.getDbAccessType())){
					accessTypeMap.put(AccessTypeEnum.IP.getUiAccessType(), AccessTypeEnum.IP.getUiAccessType());
				}
			}else{
				accessTypeMap.put(AccessTypeEnum.MODEM.getDbAccessType(), AccessTypeEnum.MODEM.getDbAccessType());
				accessTypeMap.put(AccessTypeEnum.IP.getUiAccessType(), AccessTypeEnum.IP.getUiAccessType());
			}
			
			actionForm.setAccessTypes(accessTypeMap);

			// START - Fetch Product registration data
			logger.debug("SEID :"+technicalRegistrationSummary.getSeId());
			//Service Call
			List<Asset> assetList = getTechnicalOnBoardingService().queryProductRegistrationData(technicalRegistrationSummary.getSeId(), 200, true, 0);
			if(assetList != null && assetList.size() > 0){
				Asset asset = assetList.get(0);
				
				// Setting asset data into the TechnicalRegistration domain object
				technicalRegistrationSummary.setIpAddress(asset.getAvayaIPAddrress());
				technicalRegistrationSummary.setDialInNumber(asset.getDialInNumber());
				technicalRegistrationSummary.setAuthFileID(asset.getAvayaFID());
				actionForm.setAuthFileIDFlagOrigTRED(asset.getAvayaFID());
				technicalRegistrationSummary.setSoftwareRelease(asset.getAvayaReleaseNumber());
				technicalRegistrationSummary.setNickName(asset.getNickName());
				technicalRegistrationSummary.setUserName(asset.getUsername());
				
			}
			technicalRegistrationSummary.setAddFlag(true);
			actionForm.setTechnicalRegistrationSummary(technicalRegistrationSummary);
			// Applying rules in a seperate method
			applyRulesOnTRConfigUpdate(actionForm);
			// END - Fetch Product registration data
			logger.debug("Exiting technicalRegistrationUpdate.");

			return "techRegUpdate";
		} catch(Exception e){
			e.printStackTrace();
			logger.debug("Exception in technicalRegistrationUpdate."+e.getMessage());
			//return new Forward("except", form);
			return "except";
		}
	}
	
	/**
	 * GRT4.00-BRS Requirement-BR-F.006
	 * 
	 *  Call this method when re-test button is clicked
	 * 
	 * @return
	 * @throws Exception
	 */
	/*	 -*/
	public String technicalRegistrationRetest() throws Exception {
		logger.debug("Entering technicalRegistrationRetest()");
		validateSession();
		try {			
			actionForm.setSidMidErrorFlag(GRTConstants.NONE);
			actionForm.setErrorMessage(null);
			getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
			getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);
			TechnicalRegistrationSummary technicalRegistrationSummary = null;
			String fromTRDetails = (String)getRequest().getSession().getAttribute(GRTConstants.FROM_TR_OB_DETAIL);

			if(!GRTConstants.TRUE.equals(fromTRDetails)) {
				List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
				for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
					if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
						if(tecRegSummary != null && tecRegSummary.getSeId().equals(actionForm.getSolutionElementId())
								&& tecRegSummary.getMaterialCode().equalsIgnoreCase(actionForm.getMaterialCodeDescription())){
							technicalRegistrationSummary = constructTechRegSummaryCloneObject(tecRegSummary);
							break;
						}
					}
				}
			} else {
				technicalRegistrationSummary = actionForm.getTechnicalRegistrationSummary();
			}
		boolean reTestRedirectionFlag=false;	
		for(TechnicalRegistration td:actionForm.getTechnicalRegRetest())
		{
			if(technicalRegistrationSummary.getSeId().equals(td.getSolutionElementId())){
				reTestRedirectionFlag=true;
				break;
			}
				
		}
		
		if(reTestRedirectionFlag)
			return "error";
			
			if(technicalRegistrationSummary != null){
				logger.debug("Material Code :"+technicalRegistrationSummary.getMaterialCode());
				logger.debug("Material Code Description :"+technicalRegistrationSummary.getMaterialCodeDescription());
				logger.debug("SE Code :"+technicalRegistrationSummary.getSolutionElement());
				
				//fetching remote access & transport alarm value from compatibility matrix table
				if(technicalRegistrationSummary.getSolutionElement() != null){
					CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(technicalRegistrationSummary.getSolutionElement());
					if(compatibilityMatrix != null){
						technicalRegistrationSummary.setModel(compatibilityMatrix.getModel());
						technicalRegistrationSummary.setRemoteAccess(compatibilityMatrix.getRemoteAccess());
						technicalRegistrationSummary.setTransportAlarm(compatibilityMatrix.getTransportAlarm());
					}
				}
			}
	
			// Preparing accessType List
			Map<String, String> accessTypeMap = new HashMap<String, String>();
			accessTypeMap.put("", "Choose");
			accessTypeMap.put(AccessTypeEnum.MODEM.getDbAccessType(), AccessTypeEnum.MODEM.getDbAccessType());
			accessTypeMap.put(AccessTypeEnum.IP.getUiAccessType(), AccessTypeEnum.IP.getUiAccessType());
			actionForm.setAccessTypes(accessTypeMap);
	
			// START - Fetch Product registration data
			logger.debug("SEID :"+technicalRegistrationSummary.getSeId());
			
			List<Asset> assetList = getTechnicalOnBoardingService().queryProductRegistrationData(technicalRegistrationSummary.getSeId(), 200, true, 0);
			if(assetList != null && assetList.size() > 0){
				Asset asset = assetList.get(0);
			
				// Setting asset data into the TechnicalRegistration domain object
				technicalRegistrationSummary.setIpAddress(asset.getAvayaIPAddrress());
				technicalRegistrationSummary.setDialInNumber(asset.getDialInNumber());
				technicalRegistrationSummary.setAuthFileID(asset.getAvayaFID());
				actionForm.setAuthFileIDFlagOrigTRED(asset.getAvayaFID());
				technicalRegistrationSummary.setSoftwareRelease(asset.getAvayaReleaseNumber());
				technicalRegistrationSummary.setNickName(asset.getNickName());
				technicalRegistrationSummary.setUserName(asset.getUsername());
			
			}
			
			technicalRegistrationSummary.setAddFlag(true);
			actionForm.setTechnicalRegistrationSummary(technicalRegistrationSummary);
			// END - Fetch Product registration data
			
			// Applying rules in a seperate method			
			applyRulesOnTRRetest(actionForm);
			
			if(technicalRegistrationSummary != null){
				logger.debug("Material Code :"+technicalRegistrationSummary.getMaterialCode());
				logger.debug("Material Code Description :"+technicalRegistrationSummary.getMaterialCodeDescription());
				logger.debug("SE Code :"+technicalRegistrationSummary.getSolutionElement());
				// Setting AccessType to Modem - by default
			
				//fetching remote access & transport alarm value from compatibility matrix table
				if(technicalRegistrationSummary.getSolutionElement() != null && technicalRegistrationSummary.getAccessType()!=null 
						&& technicalRegistrationSummary.getAccessType().equalsIgnoreCase(GRTConstants.SAL)){
					CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(technicalRegistrationSummary.getSolutionElement());
					if(compatibilityMatrix != null){
						technicalRegistrationSummary.setModel(compatibilityMatrix.getModel());
						technicalRegistrationSummary.setRemoteAccess(compatibilityMatrix.getRemoteAccess());
						technicalRegistrationSummary.setTransportAlarm(compatibilityMatrix.getTransportAlarm());
					}
				}else {
					
					//For Non-SAL access type
					List<LocalTRConfig> localTRConfigList = getTechnicalOnBoardingService().getLocalTRConfig(technicalRegistrationSummary.getTrConfig());
					if( localTRConfigList!=null && !localTRConfigList.isEmpty() ){
						LocalTRConfig localTr = localTRConfigList.get(0);
						String alarmEligible  = localTr.isAlarmOriginationEligible() ? GRTConstants.Y : GRTConstants.N;
						String remoteAcess = GRTConstants.N;
						if( localTr.getConnectivityType()!=null && (localTr.getConnectivityType().equalsIgnoreCase(GRTConstants.BOTH) 
								|| localTr.getConnectivityType().equalsIgnoreCase(GRTConstants.FULL))){
								remoteAcess = GRTConstants.Y;
						}
						technicalRegistrationSummary.setRemoteAccess(remoteAcess );
						technicalRegistrationSummary.setTransportAlarm( alarmEligible );
					}
				}
			}
				
			//Add both sitelist and technicalregistration in technicalregistrationsummary - differentiate the field "isTechReg", "isSal"
			//so that JSP can render only technicalregistrationsummarylist
			List<TechnicalRegistrationSummary> registrationSummaryListRetest = new ArrayList<TechnicalRegistrationSummary>();
			TechnicalRegistration registrationList = getTechnicalOnBoardingService().getConnectivityDetailsBySeid(technicalRegistrationSummary.getSeId());
			String response ="", connectivityRep="";
			if(GRTConstants.ACCESS_TYPE_IPO.equalsIgnoreCase(registrationList.getAccessType()))
				technicalRegistrationSummary.setAccessType(GRTConstants.ACCESS_TYPE_SSLVPN);
			
			if(GRTConstants.NO.equalsIgnoreCase(registrationList.getConnectivity()) || StringUtils.isEmpty(technicalRegistrationSummary.getAccessType())|| " ".equalsIgnoreCase(technicalRegistrationSummary.getAccessType()) )
			{
				if(!checkIsNoConnectivity(technicalRegistrationSummary))
				{
					connectivityRep=GRTConstants.NO_CONNECTIVITY;
				}
				
				if(GRTConstants.NO.equalsIgnoreCase(registrationList.getConnectivity()) || StringUtils.isEmpty(technicalRegistrationSummary.getAccessType()) || GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(technicalRegistrationSummary.getAccessType())|| " ".equalsIgnoreCase(technicalRegistrationSummary.getAccessType()))
				{
					connectivityRep=GRTConstants.NO_CONNECTIVITY;
				}
			}
			
			//First check if there is an active SR/siteregistration for the seid then show error message to user
			//DEFECT #621 : Show the Reg-Id with which the SR is associated in the msg
			Map<String, String> respMap = getTechnicalOnBoardingService().checkExistSRAndEntlForRetest(actionForm.getSoldToId(), 
					technicalRegistrationSummary.getSeId(), technicalRegistrationSummary.getAccessType());
			
			response = respMap.get("RESPONSE");
			String regId = respMap.get("REGID");
			
			if(connectivityRep.equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY)){
				if (GRTConstants.SUCCESS.equalsIgnoreCase(response))
				{
					response=GRTConstants.NO_CONNECTIVITY;
				}
			}
			
			if (!GRTConstants.SUCCESS.equalsIgnoreCase(response)) {
				
				//GRT 4.0 message model change
				if(GRTConstants.TOB_SSL_VPN_NOT_ALLOWED_CODE.equalsIgnoreCase(response)) {
					actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobSslVpnNotAllowedErrMsg") + "###" + grtConfig.getTobSslVpnNotAllowedErrMsg());
					
					List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
					for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
						if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
							if(tecRegSummary.getSeId().equals(technicalRegistrationSummary.getSeId())
									&& tecRegSummary.getMaterialCode().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
								tecRegSummary.setDisableUpdateFlag(false);
								tecRegSummary.setDisableRetestFlag(false);
								break;
							}
						}
					}
				
				} else if(GRTConstants.TOB_EXCEPTION_FINDING_SR_CODE.equalsIgnoreCase(response)) {
					actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobExceptionInFindinfSRErrMsg") + "###" + grtConfig.getTobExceptionInFindinfSRErrMsg());
				} else if(GRTConstants.TOB_STEP_B_STATUS_FOUND_CODE.equalsIgnoreCase(response)) {
					//DEFECT #621 : Show the Reg-Id with which the SR is associated in the msg
					if( regId!=null && regId.trim().length() > 0 ){
						actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobRegSetpBFoundForSeidErrMsg") + "###" + grtConfig.getTobRegSetpBFoundForSeidErrMsg().replace("<regid>", regId));
					}else{
						actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobRegSetpBFoundForSeidErrMsg") + "###" + grtConfig.getTobRegSetpBFoundForSeidErrMsg());
					}
					
					List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
					for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
						if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
							if(tecRegSummary.getSeId().equals(technicalRegistrationSummary.getSeId())
									&& tecRegSummary.getMaterialCode().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
								tecRegSummary.setDisableUpdateFlag(false);
								tecRegSummary.setDisableRetestFlag(false);
								break;
							}
						}
					}
				} else if(GRTConstants.TOB_EXCEPTION_FINDING_ALARMING_CODE.equalsIgnoreCase(response)) {
					actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("tobExceptionWhileFindingAlarmErrMsg") + "###" + grtConfig.getTobExceptionWhileFindingAlarmErrMsg());
				} else if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(response))
				{
					actionForm.setErrorMessage(grtConfig.getNoConnectityReTestMsg());
					
					List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
					for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
						if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
							if(tecRegSummary.getSeId().equals(technicalRegistrationSummary.getSeId())
									&& tecRegSummary.getMaterialCode().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
								tecRegSummary.setDisableUpdateFlag(false);
								tecRegSummary.setDisableRetestFlag(false);
								tecRegSummary.setUpdateButtonTitle(grtConfig.getUpdateRecordTitle());
								break;
							}
						}
					}
				}
				
			} else {
				List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
				TechnicalRegistration newTr = this.constructTechnicalRegistrationFromTechSum(technicalRegistrationSummary, actionForm);
				newTr.setSolutionElementId(technicalRegistrationSummary.getSeId());
				trs.add(newTr);
				actionForm.setSalRegistrationSummaryList(trs);	
				//Conditions on SAL access type - salRegistrationSummaryList would be referred to in loadSalStepBDetails 
				if(GRTConstants.SAL.equalsIgnoreCase(actionForm.getTechnicalRegistrationSummary().getAccessType())) {
					//Load all child records for SAL 
					try {
						//device alarm date and device statuis would be set by sal concentrator service in items of loadSalStepBDetails
						retestLoadStepb=true;
						String responseCode = loadSalStepBDetails();
						if( responseCode!=null && responseCode.equalsIgnoreCase("failure") ){
							logger.debug("responseCode from loadSalStepBDetails - "+responseCode);
							//actionForm.setErrorMessage("Error fetching details. Please try again later.");
							actionForm.setErrorMessage(grtConfig.getEwiMessageCodeMap().get("salFetchErrMsg") + "###" + grtConfig.getSalFetchErrMsg());
							return "techRegRetest";
						} 
						//device alarm date and device statuis updated in the below list, add it to the list that needs to be shown on retest section
						List<TechnicalRegistration> updatedTRs = actionForm.getSelectedSALRegSummaryList();
						trs = updatedTRs;
					} catch(Throwable e) {
						logger.error("Exception in technicalRegistrationRetest."+e.getMessage());
					}
				}
				//DEFECT # 701 added check for alarming for non sal access type 
				else {
					List<String> materialList = new ArrayList<String>();
					for ( TechnicalRegistration tr : trs ) {
						materialList.add(tr.getTechnicalOrder().getMaterialCode());
					}
					
					for(TechnicalRegistration tr: trs) {
						tr.setEntitledForAlarming("No");
						tr.setAlarmingCheckBoxDisabled(true);
						tr.setTransportAlarm(GRTConstants.N);
					}
					List<String> alarmingList = getBaseRegistrationService().getEntitledForAlarming(materialList, actionForm.getSoldToId());

					for(TechnicalRegistration tr : trs) {
						for(String s : alarmingList) {
							if(tr.getTechnicalOrder().getMaterialCode().equalsIgnoreCase(s)) {
								tr.setEntitledForAlarming("Yes");
								tr.setAlarmingCheckBoxDisabled(false);
								tr.setTransportAlarm(GRTConstants.Y);
							}
						}
					}
				}
				
				//Set the process flag to true
				for ( TechnicalRegistration tr : trs ){
					if( !tr.isCheckBoxDisabled() ){
						tr.setProcess(true);
					}
				}
				
				//Add it to final list in action form
				List<TechnicalRegistration> techRegList= actionForm.getTechnicalRegRetest();
				if( techRegList != null ){
					techRegList.addAll(trs);
				}else{
					techRegList = trs;
				}
				actionForm.setTechnicalRegRetest(techRegList);
			}			
			
			logger.debug("Exiting technicalRegistrationRetest()");
			return "techRegRetest";
		} catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in technicalRegistrationRetest."+e.getMessage());
			return "except";
		}
	}


	private void applyRulesOnTRConfigUpdate(RegistrationFormBean form) throws Exception {
		logger.debug("Entering applyRulesOnTRConfigUpdate");
		TechnicalRegistrationSummary technicalRegistrationSummary = form.getTechnicalRegistrationSummary();
		List<SALGateway> salGatewayMigrationList = null;
		form.setSidMidErrorFlag(GRTConstants.NONE);
		form.setPrimary("");
		form.setSecondary("");
		form.setTemplateFlag("none");
		form.setDialInFlag("none");
		form.setIpAddrFlag("none");
		form.setAuthFileIDFlag("none");
		form.setSalMigrationFlag("none");
		form.setSwReleaseFlag("block");
		form.setAccessTypeFlag("block");
		technicalRegistrationSummary.setAccessType("");
		technicalRegistrationSummary.setPrimarySalgwSeid("");
		technicalRegistrationSummary.setSecondarySalgwSeid("");
		if(technicalRegistrationSummary != null){
			Map<String, String> groupIds = new LinkedHashMap<String, String>();
			String materialCode = technicalRegistrationSummary.getMaterialCode();
			//Service Call
			List<TRConfig> trConfigList = getTechnicalOnBoardingService().fetchTRConfigs(materialCode);
			Iterator<TRConfig> trConfigIter = trConfigList.iterator();
			TRConfig trConfigDto = null;
			while(trConfigIter.hasNext()){
				trConfigDto = trConfigIter.next();
				if (technicalRegistrationSummary.getSolutionElement()!= null && technicalRegistrationSummary.getSolutionElement().equalsIgnoreCase(trConfigDto.getSeCode())) {
					// No Action
				} else {
					trConfigIter.remove();
				}
			}
			if(trConfigList != null && trConfigList.size() > 0){
				// sorting TRConfigs on description
				GenericSort gs = new GenericSort("groupDescription", true);
				Collections.sort(trConfigList, gs);
				logger.debug("trConfigList Size:  "+trConfigList.size());
				String groupId = "";
				for(TRConfig trConfig : trConfigList){
					if(technicalRegistrationSummary.getGroupId() != null
							&& StringUtils.isNotEmpty(technicalRegistrationSummary.getGroupId())
							&& trConfig.getGroupId().equalsIgnoreCase(technicalRegistrationSummary.getGroupId())) {
						groupId = trConfig.getGroupId();
					}
					groupIds.put(trConfig.getGroupId(), trConfig.getGroupDescription());
					form.setSpecialNote(trConfig.getSpecial_note());
				}
				form.setUserNamePasswordFlag(GRTConstants.NONE);
				String specialNote = form.getSpecialNote();
				String seCode = technicalRegistrationSummary.getSolutionElement();
				if (GRTConstants.CAAS.equalsIgnoreCase(specialNote) && seCode.equalsIgnoreCase(GRTConstants.SECODE.ACCCM)) {
					form.setUserNamePasswordFlag(GRTConstants.BLOCK);
				}
				logger.debug("groupIds Size:  "+groupIds.size());
				if(groupId != null && StringUtils.isNotEmpty(groupId)){
					technicalRegistrationSummary.setGroupId(groupId);
				} else {
					technicalRegistrationSummary.setGroupId(trConfigList.get(0).getGroupId());
				}
				technicalRegistrationSummary.setTrConfig(trConfigList.get(0));
				if(trConfigList.get(0) != null) {
					technicalRegistrationSummary.setProductTemplateDesc(trConfigList.get(0).getTemplateDescription());
					technicalRegistrationSummary.setSolutionElementDesc(trConfigList.get(0).getSeCodeDescription());
				}
				form.setGroupIds(groupIds);
				form.setTrConfigList(trConfigList);
			}
			String salSeids = technicalRegistrationSummary != null ? technicalRegistrationSummary.getSalSeIdPrimarySecondary(): "";
			if(salSeids != null && salSeids.length() > 0){
				// START - Fetching SAL Gateway Migration List
				//Service Call
				salGatewayMigrationList = getTechnicalOnBoardingService().getSALGateways(form.getSoldToId(), salSeids, GRTConstants.SAL);
				//	Set Primary and Secondary SAL SEIDs
				logger.debug("SAL SEIDs:"+salSeids);
				String seidArray[] = salSeids.split("\\+");
				if(seidArray != null && seidArray.length > 0){
					technicalRegistrationSummary.setPrimarySalgwSeid(seidArray[0]);
					if(seidArray.length == 2){
						technicalRegistrationSummary.setSecondarySalgwSeid(seidArray[1]);
					}
				}
				if(technicalRegistrationSummary.getPrimarySalgwSeid() != null){
					getRequest().getSession().setAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID, technicalRegistrationSummary.getPrimarySalgwSeid());
				}
				if(technicalRegistrationSummary.getSecondarySalgwSeid() != null){
					getRequest().getSession().setAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID, technicalRegistrationSummary.getSecondarySalgwSeid());
				}
				logger.debug("THE CONTENT OF salGatewayMigrationList IS =======> "+ salGatewayMigrationList);
				if (null != salGatewayMigrationList && salGatewayMigrationList.size()> 0) {
					logger.debug("DATA RETRIEVED FROM   getRegistrationDelegate().getSALGateways(form.getSoldToId(), null); METHOD IS WORKING");
				} else{
					logger.debug("NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND");
				}
				form.setSalGatewayMigrationList(salGatewayMigrationList);
				form.setSalMigrationFlag("block");
				//Custom Code : for showing SAL access type in case update
					Map<String, String> accessTypeMap = form.getAccessTypes();
					accessTypeMap.put(AccessTypeEnum.SAL.getDbAccessType(), AccessTypeEnum.SAL.getDbAccessType());
					actionForm.setAccessTypes(accessTypeMap);
				//form.setAccessType(AccessTypeEnum.SAL.getDbAccessType());
				technicalRegistrationSummary.setAccessType(AccessTypeEnum.SAL.getDbAccessType());
				form.setAccessTypeFlag("none");
				// END - Fetching SAL Gateway Migration List
			}

			//GRT 4.0 Changes-Change sort default ascending
			Map<String, String> accessTypeMap = form.getAccessTypes();
			
			if(accessTypeMap.size()>0){
				Map<String, String> treeMap = new TreeMap<String, String>(accessTypeMap);
				actionForm.setAccessTypes(treeMap);
			}
			
			//GRT 4.0 Changes
			List<String> connectivities = new ArrayList<String>();
			connectivities.clear();
			connectivities.add(GRTConstants.YES);
			connectivities.add(GRTConstants.NO);
			form.getTechnicalRegistrationSummary().setConnectivity(null);
			form.setConnectivities(connectivities);
			
			// Fetching releases based on group Id
			//Service Call
			List<String> releasesList = getTechnicalOnBoardingService().getReleasesByGroupId(technicalRegistrationSummary.getGroupId());
			Map<String, String> releasesMap = new HashMap<String, String>();
			if(releasesList != null && releasesList.size() > 0){
				for(String release : releasesList){
					releasesMap.put(release, release);
				}
				form.setReleasesMap(releasesMap);
			} else {
				form.setSwReleaseFlag("none");
			}
			//Added null check on TrConfig object
			if(technicalRegistrationSummary.getTrConfig() != null){
				if(technicalRegistrationSummary.getTrConfig().getTemplate() != null && technicalRegistrationSummary.getTrConfig().getTemplate().length() > 0){
					form.setTemplateFlag("block");
				}
			}
			// Rules for Asset fields
			if(!(technicalRegistrationSummary.getAccessType() != null && technicalRegistrationSummary.getAccessType().equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType()))){
				if(technicalRegistrationSummary.getDialInNumber() != null && technicalRegistrationSummary.getDialInNumber().trim().length() > 0){
					// Show dial-in number and access type
					technicalRegistrationSummary.setAccessType(AccessTypeEnum.MODEM.getDbAccessType());
					form.setDialInFlag("block");
				} else if(technicalRegistrationSummary.getIpAddress() != null && technicalRegistrationSummary.getIpAddress().trim().length() > 0){
					// Logic to display/hide IP Address Flag. By Default set to hidden.
					technicalRegistrationSummary.setAccessType(AccessTypeEnum.IP.getUiAccessType());
					form.setIpAddrFlag("block");
					form.setAccessTypeFlag("none");
				}
			}
			// Logic to display/hide AuthFileID Flag. By Default set to hidden.
			if(technicalRegistrationSummary.getAuthFileID() != null && technicalRegistrationSummary.getAuthFileID().trim().length() > 0){
				form.setAuthFileIDFlag("block");
			} else {
				// Fetch LocalTR Config for the groupId
				if(technicalRegistrationSummary.getTrConfig() != null){
					//Service Call
					List<LocalTRConfig> localTRConfigList = getTechnicalOnBoardingService().getLocalTRConfig(technicalRegistrationSummary.getTrConfig());
					if(localTRConfigList != null && localTRConfigList.size() > 0
							&& localTRConfigList.get(0).isAuthFileIdEligible()){
						form.setAuthFileIDFlag("block");
					}
				}
			}
		}
		form.setTechnicalRegistrationSummary(technicalRegistrationSummary);
		logger.debug("Exiting applyRulesOnTRConfigUpdate");
	}

	private void applyRulesOnTRRetest(RegistrationFormBean form) throws Exception {
		logger.debug("Entering applyRulesOnTRRetest");
		TechnicalRegistrationSummary technicalRegistrationSummary = form.getTechnicalRegistrationSummary();
		//Defect 315 : Made same change as it was for applyrulesonupdate
		technicalRegistrationSummary.setAccessType(" ");
		//Populate TRConfig 
		if( technicalRegistrationSummary!=null ){
			String materialCode = technicalRegistrationSummary.getMaterialCode();
			//Service Call
			List<TRConfig> trConfigList = getTechnicalOnBoardingService().fetchTRConfigs(materialCode);
			Iterator<TRConfig> trConfigIter = trConfigList.iterator();
			TRConfig trConfigDto = null;
			while(trConfigIter.hasNext()){
				trConfigDto = trConfigIter.next();
				if (technicalRegistrationSummary.getSolutionElement()!= null && technicalRegistrationSummary.getSolutionElement().equalsIgnoreCase(trConfigDto.getSeCode())) {
					// No Action
				} else {
					trConfigIter.remove();
				}
			}
			if(trConfigList != null && trConfigList.size() > 0){
				// sorting TRConfigs on description
				GenericSort gs = new GenericSort("groupDescription", true);
				Collections.sort(trConfigList, gs);
				logger.debug("trConfigList Size:  "+trConfigList.size());
				String groupId = "";
				for(TRConfig trConfig : trConfigList){
					if(technicalRegistrationSummary.getGroupId() != null
							&& StringUtils.isNotEmpty(technicalRegistrationSummary.getGroupId())
							&& trConfig.getGroupId().equalsIgnoreCase(technicalRegistrationSummary.getGroupId())) {
						groupId = trConfig.getGroupId();
					}
				}
				if(groupId != null && StringUtils.isNotEmpty(groupId)){
					technicalRegistrationSummary.setGroupId(groupId);
				} else {
					technicalRegistrationSummary.setGroupId(trConfigList.get(0).getGroupId());
				}
				technicalRegistrationSummary.setTrConfig(trConfigList.get(0));
				if(trConfigList.get(0) != null) {
					technicalRegistrationSummary.setProductTemplateDesc(trConfigList.get(0).getTemplateDescription());
					technicalRegistrationSummary.setSolutionElementDesc(trConfigList.get(0).getSeCodeDescription());
				}
			}
		}
		
		String salSeids = technicalRegistrationSummary != null ? technicalRegistrationSummary.getSalSeIdPrimarySecondary(): "";
		if(salSeids != null && salSeids.length() > 0){
			technicalRegistrationSummary.setAccessType(AccessTypeEnum.SAL.getDbAccessType());
			form.setAccessTypeFlag("none");
		} else if(technicalRegistrationSummary.getDialInNumber() != null && technicalRegistrationSummary.getDialInNumber().trim().length() > 0){
		// Rules for Asset fields
		//if(technicalRegistrationSummary.getDialInNumber() != null && technicalRegistrationSummary.getDialInNumber().trim().length() > 0){
			// Show dial-in number and access type
			technicalRegistrationSummary.setAccessType(AccessTypeEnum.MODEM.getDbAccessType());
			form.setDialInFlag("block");
		} else if(technicalRegistrationSummary.getIpAddress() != null && technicalRegistrationSummary.getIpAddress().trim().length() > 0){
			// Logic to display/hide IP Address Flag. By Default set to hidden.
			technicalRegistrationSummary.setAccessType(AccessTypeEnum.IP.getUiAccessType());
			form.setIpAddrFlag("block");
			form.setAccessTypeFlag("none");
		}
		
		//GRT 4.0 Change : Disable ReTest button & update also for the Line Item if Update is clicked
		List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
		for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
			if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
				if(tecRegSummary.getSeId().equals(technicalRegistrationSummary.getSeId())
						&& tecRegSummary.getMaterialCode().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
					tecRegSummary.setDisableUpdateFlag(true);
					tecRegSummary.setDisableRetestFlag(true);
					tecRegSummary.setUpdateButtonTitle(grtConfig.getUpdateRecordTitle());
					break;
				}
			}
		}
		
		//Set default values for Non-SAL records - Defect #200
		if( technicalRegistrationSummary.getAccessType()!=null && 
				!technicalRegistrationSummary.getAccessType().equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType()) ){
			technicalRegistrationSummary.setModel(GRTConstants.NA);
			technicalRegistrationSummary.setDeviceLastAlarmReceivedDate(GRTConstants.NA);
			technicalRegistrationSummary.setPrimarySalgwSeid(GRTConstants.NA);
			technicalRegistrationSummary.setSecondarySalgwSeid("");
			technicalRegistrationSummary.setDeviceStatus(GRTConstants.UNKNOWN);
		}	
		if( technicalRegistrationSummary.getAccessType()!=null && 
				technicalRegistrationSummary.getAccessType().equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType()) ){
			technicalRegistrationSummary.setModel(technicalRegistrationSummary.getModel());
			technicalRegistrationSummary.setDeviceLastAlarmReceivedDate(technicalRegistrationSummary.getDeviceLastAlarmReceivedDate());
			technicalRegistrationSummary.setPrimarySalgwSeid(technicalRegistrationSummary.getSalSeIdPrimarySecondary());
			technicalRegistrationSummary.setSecondarySalgwSeid(technicalRegistrationSummary.getSecondarySalgwSeid());
			technicalRegistrationSummary.setDeviceStatus(GRTConstants.UNKNOWN);
			
		}
		
		//Check whether the record is eligible for RETEST or not by checking connectivity
		if( technicalRegistrationSummary.getSeId()!=null ){
			TechnicalRegistration tr = getTechnicalOnBoardingService().getConnectivityDetailsBySeid(technicalRegistrationSummary.getSeId());
			if( tr!=null && GRTConstants.NO.equalsIgnoreCase(tr.getConnectivity()) ){
				technicalRegistrationSummary.setDisableRetestFlag(true);
			}
		}
		
		//Don't allow to retest if access type is blank
		if( technicalRegistrationSummary.getAccessType()==null || technicalRegistrationSummary.getAccessType().trim().isEmpty() ){
			technicalRegistrationSummary.setDisableRetestFlag(true);
		}
		
		logger.debug("Exiting applyRulesOnTRRetest");
	}
	
	/**
	 * Populate configuration detail
	 * Action Dashboard to config page
	 * @return
	 * @throws Exception
	 */
	public String populateTRConfiguration() throws Exception {
		logger.debug("Entering populateTRConfiguration...");
		validateSession();
		actionForm.setCmMainErrorFlag("none");
		applyRulesOnTRConfiguration(actionForm);
		logger.debug("Exiting populateTRConfiguration.");
		String selectedAccessType = actionForm.getTechnicalRegistrationSummary().getAccessType();
		return "techRegUpdates";
	}
	
    public String populateTRConfig() throws Exception {
   	 logger.debug("Entering populateTRConfig.");
   	 validateSession();
   	 String groupId = actionForm.getTechnicalRegistrationSummary().getGroupId();
   	 logger.debug("populateTRConfig GroupID :"+groupId);
   	 if(groupId != null){
	    	 List<TRConfig> trConfList = actionForm.getTrConfigList();
	    	 for(TRConfig trConfig : trConfList){
	    		 if(groupId.equalsIgnoreCase(trConfig.getGroupId())){
	    			 actionForm.getTechnicalRegistrationSummary().setGroupId(groupId);
	    			 actionForm.getTechnicalRegistrationSummary().setTrConfig(trConfig);
	    			 actionForm.setTrConfig(trConfig);
	    			 break;
	    		 }
	    	 }
   	 }
   	 actionForm.setGroupIds(actionForm.getGroupIds());
   	 logger.debug("populateTRConfig Access Type :"+actionForm.getTechnicalRegistrationSummary().getAccessType());
   	 if(actionForm.getTechnicalRegistrationSummary().getAccessType().equalsIgnoreCase(GRTConstants.SAL)){
   		 getRequest().getSession().setAttribute("salGWFlag", "true");
   	 }
   	 logger.debug("Entering populateTRConfig.");
	  	return "techRegUpdate";
    }

    /**
     * Registration update action from Technical detail page
     * @return
     * @throws Exception
     */
	public String navigateToTR() throws Exception {
		logger.debug("Entering navigateToTR");
		validateSession();
		String navigate = "samePage";
		String index = actionForm.getIndex();
		TechnicalRegistrationSummary technicalRegistrationSummary = null;
		List<TechnicalRegistrationSummary> summaryList = actionForm.getRegistrationSummaryListRegistrable();
		if(summaryList != null && summaryList.size() > 0){
			technicalRegistrationSummary = constructTechRegSummaryCloneObject(summaryList.get(Integer.parseInt(index)));
			actionForm.setSolutionElementId(technicalRegistrationSummary!=null?technicalRegistrationSummary.getSolutionElementId():"");
		}
		if(technicalRegistrationSummary != null){
			technicalRegistrationSummary.setAddFlag(false);
			actionForm.setTechnicalRegistrationSummary(technicalRegistrationSummary);
			if(technicalRegistrationSummary.getOperationType() != null
					&& technicalRegistrationSummary.getOperationType().equalsIgnoreCase("DU")){
				//technicalRegistrationSummary.setGroupId(technicalRegistrationSummary.getTrConfig().getGroupId());
				applyRulesOnTRConfigUpdate(actionForm);
				navigate = "techRegUpdate";
			} else {
				actionForm.setPopUpHiddenValue("");
				if(technicalRegistrationSummary.getConnectivity().equalsIgnoreCase(GRTConstants.NO)||technicalRegistrationSummary.getConnectivity()==null || technicalRegistrationSummary.getConnectivity()=="")
				actionForm.setPopUpHiddenValue(GRTConstants.NO_CONNECTIVITY);
				applyRulesOnTRConfiguration(actionForm);
				
				navigate = "techRegConfig";
			}
		}
		logger.debug("Exiting navigateToTR");
		return navigate;
	}

	/***
	 * Cancel the registration
	 * Cancel button action
	 * @return
	 * @throws Exception
	 */
	public String cancelTechnicalRegistrationSummary () throws Exception {
		logger.debug("Entering cancelTechnicalRegistrationSummary : RegId: "+actionForm.getRegistrationId());
		validateSession();
		actionForm.getSidMid().clear();
		//Service Call
		getTechnicalOnBoardingService().updateSiteRegistrationProcessStepAndStatus(actionForm.getRegistrationId(), ProcessStepEnum.TECHNICAL_REGISTRATION, StatusEnum.CANCELLED);
		logger.debug("Exiting cancelTechnicalRegistrationSummary");
		return registrationList();
	}
	
	public String cancelRegistrationHome() throws Exception {
		logger.debug("Entering cancelTechnicalRegistrationSummary : RegId: "+actionForm.getRegistrationId());
		validateSession();
		actionForm.getSidMid().clear();
		//Service Call
		getTechnicalOnBoardingService().updateSiteRegistrationProcessStepAndStatus(actionForm.getRegistrationId(), ProcessStepEnum.TECHNICAL_REGISTRATION, StatusEnum.CANCELLED);
		
		//added to fix https redirect issue in co-browse iframe
		hostName = getRequest().getHeader("host");
		
		logger.debug("Exiting cancelTechnicalRegistrationSummary");
		return "success";
	}

	/***
	 * submit Alarming step B
	 * Action
	 * @return
	 * @throws Exception
	 */
	public String submitSalAlarmConnectivityDetails() throws Exception {
		validateSession();
		logger.debug("Entering submitSalAlarmConnectivityDetails");
		
		//Begin: To restrict Refresh Operation
		String trDetailsActive = (String)getRequest().getSession().getAttribute(GRTConstants.TR_DETAILS_ACTIVE);
		if(GRTConstants.TRUE.equals(trDetailsActive)){
			return getTechnicalOrderDetails();
		}
		//End:  To restrict Refresh Operation
	
		try{
			//List that would selected by user using checkbox
			List<TechnicalRegistration> trs = actionForm.getSelectedSALRegSummaryList();
			List<SiteList> sls = actionForm.getSelectedSALMigrationSummaryList();
		    
			/* GRT 4.0 - Moved the entire code to service method submitAlarmTechnicalRegistrationSummary */
			this.stepBResult = getTechnicalOnBoardingService().submitAlarmTechnicalRegistrationSummary(trs, sls, actionForm.getRegistrationId());
			if( this.stepBResult ){
				message = getGrtConfig().getStepBSuccessMsg(); //Success Message
			}else{
				message = getGrtConfig().getStepBFailureMsg(); //Failure Message
			}
			/* GRT 4.0 - Moved the entire code to service method submitAlarmTechnicalRegistrationSummary */
			
		} catch(Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting submitSalAlarmConnectivityDetails");
		}
		return "success";
	}

	/***
	 * Submit technical Registration 
	 * Submit action from TOB
	 * @return
	 * @throws Exception
	 */
	public String submitTechnicalRegistrationSummary () throws Exception {
		logger.debug("Entering submitTechnicalRegistrationSummary : RegId: "+actionForm.getRegistrationId());

		validateSession();
		//Begin: To restrict Refresh Operation
		actionForm.getSidMid().clear();
		String trDetailsActive = (String)getRequest().getSession().getAttribute(GRTConstants.TR_DETAILS_ACTIVE);
		if(GRTConstants.TRUE.equals(trDetailsActive)){
			//return new Forward("submit", actionForm);
			return getTechnicalOrderDetails();
		}
		//End:  To restrict Refresh Operation
		Boolean trResult = false;
		Boolean techSave=true;
		/* StepA submission starts */
		List<TechnicalRegistrationSummary> regSumList = actionForm.getRegistrationSummaryListRegistrable();
		if( regSumList!=null && !regSumList.isEmpty() ){
			logger.debug("TOTAL :::" + regSumList.size() + "toString () :::" + regSumList);
			List<TechnicalRegistrationSummary> trSummaryList = new ArrayList<TechnicalRegistrationSummary>();
			for (TechnicalRegistrationSummary summary : regSumList) {
				if (summary.isProcess()) {
					if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(summary.getAccessType())){
						summary.setConnectivity(GRTConstants.NO);}
					trSummaryList.add(summary);
				}
			}
			//Service Call
			trResult = getTechnicalOnBoardingService().submitTechnicalRegistrationSummary(trSummaryList);
			
		}
		
		/* StepA submission ends */
		
		
		/* StepB submission starts
		Get Retest records to be submitted */
		List<TechnicalRegistration> techRegList = actionForm.getTechnicalRegRetest();
		if( techRegList!=null && !techRegList.isEmpty() ){
			List<TechnicalRegistration> trNonSals = new ArrayList<TechnicalRegistration>();
			List<SiteList> trSals = new ArrayList<SiteList>();
			SiteRegistration siteRegistration = getTechnicalOnBoardingService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
		
			List<TechnicalRegistrationSummary> trRetest = new ArrayList<TechnicalRegistrationSummary>();
			if( techRegList!=null ){
				for( TechnicalRegistration techObj :  techRegList ){
					List<TechnicalRegistrationSummary> techRegSumList = constructTechnRegSummaryList(techObj, actionForm.getRegistrationId());
					if( techRegSumList!=null && !techRegSumList.isEmpty() ){
						TechnicalRegistrationSummary trsObj = techRegSumList.get(0);
						//Explicitly add the missing value in techRegSummary Obj
						trsObj.setAccessType(techObj.getAccessType());
						trsObj.setSelectForRemoteAccess(techObj.isSelectForRemoteAccess());
						trsObj.setSelectForAlarming(techObj.isSelectForAlarming());
						trRetest.add(trsObj);
					}
				}
			}
		
			if (!trRetest.isEmpty()) {
				for (TechnicalRegistrationSummary trSummary : trRetest) {
					if ( (trSummary.isProcess()) && (trSummary.isSiteList()) ) {
						SiteList sl = this.constructSiteListTechSum(trSummary, siteRegistration);
						String slId = getTechnicalOnBoardingService().saveSalSiteListOnly(sl);
						sl.setId(slId);
						trSals.add(sl);
					} else if ((trSummary.isProcess()) && (trSummary.isTechReg())) {
						/** START Defect 331 - Delete the records before saving **/
							List<String> orderIds = new ArrayList<String>();
							Set<TechnicalOrder> techOrd = siteRegistration.getTechnicalOrders();
							if( techOrd!=null && !techOrd.isEmpty() ){
								//Prepare the list of orderid's for which technical_registration has to be deleted
								for( TechnicalOrder Obj : techOrd ){
									if( GRTConstants.TECH_ORDER_TYPE_TR_RETEST.equalsIgnoreCase(Obj.getOrderType()) ){
										orderIds.add( Obj.getOrderId() );
									}
								}
								if( !orderIds.isEmpty() && actionForm.getRegistrationId()!=null ){
									//Delete technical registrations records
									getTechnicalOnBoardingService().deleteTechRegByOrderIds( orderIds );
									
									//Delete Technical order records
									getTechnicalOnBoardingService().deleteTechOrderByRegId( actionForm.getRegistrationId() );
								}
								
							}
							
						/** END Defect 331 - Delete the records before saving **/
						trSummary.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR_RETEST);
						TechnicalRegistrationSummary newTrSummary = getTechnicalOnBoardingService().addTechnicalOrder(trSummary);
						if(newTrSummary != null) {						
							try{
								//Defect #635
								List<ExpandedSolutionElement> listExp = newTrSummary.getExpSolutionElements();
								if( listExp!=null && !listExp.isEmpty() ){
									//Initialize with Empty List
									newTrSummary.setExpSolutionElements(new ArrayList<ExpandedSolutionElement>());
									newTrSummary.setExplodedSolutionElements(new HashSet<ExpandedSolutionElement>());
								}
								
								TechnicalRegistration tr = this.constructTechnicalRegistrationFromTechSum(newTrSummary, actionForm);
								//Defect 440 : Make the Step-A status as completed for Retest records
								Status status = new Status();
			    				status.setStatusId(StatusEnum.COMPLETED.getStatusId());
			    				tr.setStatus(status);
			    				//Save without Expanded solution list
			    				getTechnicalOnBoardingService().saveTechnicalRegistration(tr);
			    				
			    				//Defect #635
			    				//Save with expanded solution list
			    				java.util.Set<ExpandedSolutionElement> resultsSet = new HashSet<ExpandedSolutionElement>();
			    		    	if(!listExp.isEmpty()){
			    			    	for(ExpandedSolutionElement expandedSolutionElement : listExp){
			    			    		expandedSolutionElement.setTechnicalRegistration(tr);
			    			    		resultsSet.add(expandedSolutionElement);
			    			    	}
			    		    	}
			    		    	tr.setExpSolutionElements(listExp);
			    		    	tr.setExplodedSolutionElements(resultsSet);
			    				getTechnicalOnBoardingService().saveTechnicalRegistration(tr);
			    				
			    				trNonSals.add(tr);
							}catch(Exception Ex){
								techSave = false;
								break;
							}
						}					
					}
				}
			
				//Set it in the list which is required in method submitSalAlarmConnectivityDetails
				actionForm.setSelectedSALRegSummaryList(trNonSals);
				actionForm.setSelectedSALMigrationSummaryList(trSals);	
				
				//Call method submitSalAlarmConnectivityDetails
				this.submitSalAlarmConnectivityDetails();
				if(techSave==true && stepBResult == false )
				{
					List <TechnicalRegistration> trs =actionForm.getSelectedSALRegSummaryList();
					for(TechnicalRegistration tr: trs){
		    			TechnicalRegistration technicalRegistration = new TechnicalRegistration();
		    			
		    			technicalRegistration.setTechnicalRegistrationId(tr.getTechnicalRegistrationId());
		    			try{
		    				getTechnicalOnBoardingService().updateTechRegStatus(technicalRegistration, StatusEnum.SAVED, siteRegistration);
		    			}catch(Exception e){
		    				logger.error("Error updating Technical registration status "+e.getMessage());
		    			}
		    		}
				}
			}
		}
		
		//Make null all the actionForm variables which are used for stepB submission
		actionForm.setSelectedSALRegSummaryList(null);
		actionForm.setSelectedSALMigrationSummaryList(null);
		/* StepB submission ends */
		//Set the error
		//Defect 237
		if(techSave==false)
		{
			this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobTechnicalError2") + "###" + grtConfig.getTobTechnicalError2());
			return "current";
		}
		if( trResult == false && stepBResult == false ){
			this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobTechnicalError2") + "###" + grtConfig.getTobTechnicalError2());
			return "current";
		}else if (trResult == false &&  regSumList!=null && !regSumList.isEmpty() && stepBResult ) {
			this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobSubmitRegPrdtErrMsg") + "###" + grtConfig.getTobSubmitRegPrdtErrMsg());
			return "current";
		}else if( stepBResult == false &&  techRegList!=null && !techRegList.isEmpty() && trResult ){
			this.setTobDashboardDataError(grtConfig.getEwiMessageCodeMap().get("tobSubmitRedayToProcesstErrMsg") + "###" + grtConfig.getTobSubmitRedayToProcesstErrMsg());
			return "current";
		}
		logger.debug("Exiting submitTechnicalRegistrationSummary");
		return getTechnicalOrderDetails();
	}

	/***
	 * Back to registration list 
	 * @return
	 * @throws Exception
	 */
	public String backFromTechnicalRegistrationSummary () throws Exception {
		logger.debug("Entering backFromTechnicalRegistrationSummary");
		validateSession();
		String result = GRTConstants.REGISTRATION_LIST;
		String tobDashboardError  = this.actionForm.getTobDashboardDataError();
		boolean sidmidFlg = true;
		if((getRequest().getSession().getAttribute("scvToTRDashboard"))!=null && StringUtils.isEmpty(tobDashboardError)){
			getRequest().getSession().removeAttribute("scvToTRDashboard");
			sidmidFlg = false;
			return newRegistration();
		} else if (actionForm.getTrCancel()!=null) {
			actionForm.setTrCancel(null);
		}
		if( sidmidFlg ){
			actionForm.getSidMid().clear();
		}
		logger.debug("Exiting backFromTechnicalRegistrationSummary");
		registrationList();
		return result;
	}

	/***
	 * get message for 
	 * SAL Concentrator Service
	 * failed
	 * @return
	 * @throws Exception
	 */
	public String getSALGatewayDetails () throws Exception {
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Entering ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK");
		boolean condition = false;
		SALGatewayIntrospection gateway = null;
		try{
			//Service Call
			gateway = getTechnicalOnBoardingService().introspectSALGateway(gatewaySeid, soldToId);

			logger.debug("The gateway  returned from Service is manage devices size is  ========================"+ gateway.getManagedDevices().size());
		} catch (Exception ex){
			condition = true;
			logger.error("Exception While calling the SAL Concentrator Webservice ", ex);
		}
		if (! condition  ){
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Timer Fetch the SAL Concentrator Data From ART >> Time in milliseconds"+ (c2-c1));
			gateway.setGatewaySEID(gatewaySeid);
			this.htmlContentStr = getHtmlDocument(gateway);
			return "success";
		} else {
			StringBuilder htmlBuilder = new StringBuilder();
			htmlBuilder.append("<html>");
			htmlBuilder.append("<head>");
			htmlBuilder.append("<title>Recent Devices</title>");
			htmlBuilder.append("</head>");
			htmlBuilder.append("<body>");
			//Code inside body
			htmlBuilder.append("<table width=\"100%\">");
			htmlBuilder.append("<tr align=\"center\"> <td><font color=\"red\" ><b>Error while fetching the data from SAL Concentrator Service</b></font></td> </tr>");
			htmlBuilder.append("</table>");

			htmlBuilder.append("</body>");
			htmlBuilder.append("</html>");
			this.htmlContentStr = htmlBuilder.toString();
			return "success";
		}
	}

	private String getHtmlDocument(SALGatewayIntrospection sgwIntrospection ) {
		long c1 = Calendar.getInstance().getTimeInMillis();
		StringBuilder htmlBuilder = new StringBuilder();
		Account account = sgwIntrospection.getAccount();
		if (account == null){
			account = new Account();
			account.setName("");
			account.setSoldToNumber("");
			account.setPrimaryAccountStreetAddress("");
			account.setPrimaryAccountStreetAddress2("");
			account.setPrimaryAccountCity("");
			account.setPrimaryAccountState("");
			account.setPrimaryAccountCountry("");
			account.setPrimaryAccountPostalCode("");
		}

		if(!StringUtils.isNotEmpty(sgwIntrospection.getStatus())){
			sgwIntrospection.setStatus("");
		}

		if(sgwIntrospection.getRegistrationDate() != null){
			sgwIntrospection.setStrRegDate(sgwIntrospection.getRegistrationDate().toString());
		} else {
			sgwIntrospection.setStrRegDate("");
		}

		if(sgwIntrospection.getLastContact() != null){
			sgwIntrospection.setStrLastContact(sgwIntrospection.getLastContact().toString());
		} else {
			sgwIntrospection.setStrLastContact("");
		}

		List<ManagedDevice> managedDevices = sgwIntrospection.getManagedDevices();

		htmlBuilder.append("<html>");
		htmlBuilder.append("<head>");
		htmlBuilder.append("<title>Recent Devices</title>");
		htmlBuilder.append("</head>");
		htmlBuilder.append("<body>");
		//Code inside body
		htmlBuilder.append("<table width=\"100%\">");

		htmlBuilder.append("<tr bgcolor =\"#cccccc\">")
		.append("<th><b>&nbsp;Location :</b></th><td>").append(account.getName())
		.append("</td><th><b>&nbsp;Gateway SEID :</b></th><td>").append(sgwIntrospection.getGatewaySEID())
		.append("</td>").append("</tr>");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>Account</b></td><td>:&nbsp;").append(account.getName()).append("</td><td>&nbsp;</td><td>&nbsp;</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>FL</b></td><td>:&nbsp;").append(account.getSoldToNumber())
		.append("</td><td><b>Product Family</th><td>:&nbsp;").append(sgwIntrospection.getProductFamily()).append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>Address1</b></td><td>:&nbsp;").append(account.getPrimaryAccountStreetAddress())
		.append("</td><td><b>Status</b></td><td>:&nbsp;").append(sgwIntrospection.getStatus()).append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>Address2</b></td><td>:&nbsp;").append(account.getPrimaryAccountStreetAddress2())
		.append("</td><td><b>Registration</b></td><td>:&nbsp;").append(sgwIntrospection.getStrRegDate()).append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>City</b></td><td>:&nbsp;").append(account.getPrimaryAccountCity())
		.append("</td><td><b>Last Contact</b></td><td>:&nbsp;").append(sgwIntrospection.getStrLastContact()).append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>State</b></td><td>:&nbsp;").append(account.getPrimaryAccountState())
		.append("</td><td><b>Ping rate</b></td><td>:&nbsp;").append(sgwIntrospection.getPingRate()).append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>Country</b></td><td>:&nbsp;").append(account.getPrimaryAccountCountry())
		.append("</td><td colspan=\"2\">&nbsp;</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td><b>Zip</b></td><td>:&nbsp;").append(account.getPrimaryAccountPostalCode())
		.append("</td><td colspan=\"2\">*All timings are in GMT</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("<tr>").append("<td colspan=\"4\"><b>Managed Devices</b></td>").append("</tr>");

		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td colspan=\"4\">");
		htmlBuilder.append("<div id=\"salConcentration\"  style=\"height: 225px;width: 100%;overflow-y: scroll;float:left;\">");
		htmlBuilder.append("<table width=\"100%\"  border=\"1\">");
		htmlBuilder.append("<tr bgcolor =\"#cccccc\">");
		htmlBuilder.append("<th>&nbsp;&nbsp;&nbsp;SEID&nbsp;&nbsp;&nbsp;</th><th>Status</th><th>SeCode</th><th>Last Alarm Received</th><th>Last Contact to Gateway</th>");
		htmlBuilder.append("</tr>");

		if(managedDevices != null && managedDevices.size() >0 ){
			for (ManagedDevice managedDevice : managedDevices) {
				htmlBuilder.append("<tr>");
				if(managedDevice.getSeid() != null){
					htmlBuilder.append("<td>").append(managedDevice.getSeid()).append("</td>");
				} else {
					htmlBuilder.append("<td>").append("&nbsp;").append("</td>");	
				}
				if(managedDevice.getStatus() != null){
					htmlBuilder.append("<td>").append(managedDevice.getStatus()).append("</td>");
				} else {
					htmlBuilder.append("<td>").append("&nbsp;").append("</td>");	
				}
				if(managedDevice.getSeCode() != null){
					htmlBuilder.append("<td>").append(managedDevice.getSeCode()).append("</td>");
				} else{
					htmlBuilder.append("<td>").append("&nbsp;").append("</td>");
				}
				if(managedDevice.getLastAlarmReceivedDate() != null){
					htmlBuilder.append("<td>").append(managedDevice.getLastAlarmReceivedDate()).append("</td>");
				} else{
					htmlBuilder.append("<td>").append("&nbsp;").append("</td>");
				}
				if(managedDevice.getLastContact() != null){
					htmlBuilder.append("<td>").append(managedDevice.getLastContact()).append("</td>");
				} else {
					htmlBuilder.append("<td>").append("&nbsp;").append("</td>");	
				}
				htmlBuilder.append("</tr>");
			}
		} else {
			htmlBuilder.append("<tr>").append("<td colspan=\"5\" align=\"center\">No Manage Devices Found</td>").append("</tr>");
		}

		htmlBuilder.append("</table>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</td>");
		htmlBuilder.append("</tr>");

		htmlBuilder.append("</table>");
		//Code inside body
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Timer Construct the HTML document required to render in Sal Concentrator popup >> Time in milliseconds"+ (c2-c1));
		return htmlBuilder.toString();
	}


	public String callAFIDService(RegistrationFormBean form, String afidVal) throws Exception {
		logger.debug( "Calling AFID service :::" );
		String returnVal = null;
		//Service Call
		String afIdResponse = getTechnicalOnBoardingService().AFIDService(afidVal);
		
		logger.debug( "AFID service response :::" + afIdResponse);
		if (!afIdResponse.startsWith(GRTConstants.HTTP_200)) {
			form.setSidMidStatus(afIdResponse);
			form.setSidMidErrorFlag(GRTConstants.BLOCK);
			form.setSidMidValidatedFlag(GRTConstants.NONE);
			logger.debug( "Exiting Call AFID service with error" );
			return GRTConstants.NOT_NULL;
		}
		logger.debug( "Exiting Call AFID service" );
		return returnVal;
	}

	/***
	 * add technical configuration detail
	 * action TOB Config page.
	 * @return
	 * @throws Exception
	 */
	public String addTechnicalRegistrationConfig()
			throws Exception {
		logger.debug("Entering addTechnicalRegistrationConfig");
		String salSeIdPrimarySecondary = "";
		validateSession();
		
		if(actionForm.isRedirectionFlag())
		{
			actionForm.setRedirectionFlag(false);
		}else{
			return "error";
		}
		try {
			String afidVal = actionForm.getTechnicalRegistrationSummary().getAuthFileID();
			if (actionForm.getAuthFileIDFlag().equalsIgnoreCase(GRTConstants.BLOCK) && !StringUtils.isEmpty(afidVal)) {
				String afIdResponse = callAFIDService(actionForm, afidVal);
				if (afIdResponse != null) {
					//return new Forward("except");
					return "except";
				}
			}	
			TechnicalRegistrationSummary technicalRegistrationSummary = actionForm.getTechnicalRegistrationSummary();
						
			String primarySeId = actionForm.getPrimary();
			getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID,primarySeId);
			String secondarySeId = actionForm.getSecondary();
			getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID,secondarySeId);
			String primarySoldToId = null;
			String secondarySoldToId = null;
			List<SALGateway> gateWays = actionForm.getSalGatewayMigrationList();
			if(gateWays != null && gateWays.size()>0){
				logger.debug("<======================@==========================>");
				for(SALGateway salGateway : gateWays){
					if(salGateway.getSeid().equals(primarySeId) ){
						primarySoldToId = salGateway.getSoldTo();
					} else if(salGateway.getSeid().equals(secondarySeId) ){
						secondarySoldToId = salGateway.getSoldTo();
					}
				}
			}
			technicalRegistrationSummary.setPrimarySalgwSeid(primarySeId);
			technicalRegistrationSummary.setPrimarySoldToId(primarySoldToId);
			logger.debug("addTechnicalRegistrationConfig - PRIMARY SOLD TO:"+technicalRegistrationSummary.getPrimarySoldToId());
			technicalRegistrationSummary.setSecondarySalgwSeid(secondarySeId);
			technicalRegistrationSummary.setSecondarySoldToId(secondarySoldToId);
			logger.debug("addTechnicalRegistrationConfig - SECONDARY SOLD TO:"+technicalRegistrationSummary.getSecondarySoldToId());
			
			if(technicalRegistrationSummary.getAccessType().equalsIgnoreCase(GRTConstants.SAL)){
				salSeIdPrimarySecondary = actionForm.getPrimary();
				technicalRegistrationSummary.setPrimarySalgwSeid(actionForm.getPrimary());
				technicalRegistrationSummary.setSecondarySalgwSeid(actionForm.getSecondary());
				if(salSeIdPrimarySecondary != null && salSeIdPrimarySecondary.length() > 0){
					if(actionForm.getSecondary() != null && actionForm.getSecondary().length() > 0){
						salSeIdPrimarySecondary = salSeIdPrimarySecondary + "+" + actionForm.getSecondary();
					}
				} else {
					salSeIdPrimarySecondary = actionForm.getSecondary();
				}
			}
			technicalRegistrationSummary.setSalSeIdPrimarySecondary(salSeIdPrimarySecondary);

			//logger.debug("technicalRegistrationSummary.toString() :::" + technicalRegistrationSummary.toString());
			List <TechnicalRegistrationSummary> summaryList = actionForm.getRegistrationSummaryList();
			List <TechnicalRegistrationSummary> summaryListNew = new ArrayList<TechnicalRegistrationSummary>();
			List<TechnicalRegistrationSummary> regSumList = actionForm.getRegistrationSummaryListRegistrable();
			for (TechnicalRegistrationSummary summary : summaryList) {
				if (summary.getMaterialCode().trim().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())) {
					technicalRegistrationSummary.setAction(GRTConstants.TECH_REG);
					technicalRegistrationSummary.setProcess(true);
					//Calling the setTRDerivables for saving convertable values in DB.
					setTRDerivables(technicalRegistrationSummary);
					technicalRegistrationSummary.setStatusId(StatusEnum.INPROCESS.getStatusId());
					logger.debug("addTechnicalRegistrationConfig - SEID:"+technicalRegistrationSummary.getSolutionElementId());
					if(technicalRegistrationSummary.getSolutionElementId() != null && StringUtils.isNotEmpty(technicalRegistrationSummary.getSolutionElementId())){
						technicalRegistrationSummary.setSolutionElementId(actionForm.getSolutionElementId());
					}
					if(actionForm.getTrConfig() != null) {
						technicalRegistrationSummary.setSolutionElement(actionForm.getTrConfig().getSeCode());
						technicalRegistrationSummary.setSolutionElementDesc(actionForm.getTrConfig().getSeCodeDescription());
						technicalRegistrationSummary.setProductTemplate(actionForm.getTrConfig().getTemplate());
						technicalRegistrationSummary.setProductTemplateDesc(actionForm.getTrConfig().getTemplateDescription());
						technicalRegistrationSummary.setProductType(actionForm.getTrConfig().getProductType()); 
						
					} else {
						logger.warn("[[TRConfig is NULL]]");
					}
					if (!actionForm.getTechnicalRegistrationSummary().isCmProduct()) {
						actionForm.getTechnicalRegistrationSummary().setMainCM(GRTConstants.NO);
					}
					String accessType = technicalRegistrationSummary.getAccessType();
					technicalRegistrationSummary.setAccessTypes(accessType);
					//GRT 4.0 Changes-BRS Requirement-BR-F.006
					if(actionForm.getConnectivityFlag().equalsIgnoreCase("none"))
						technicalRegistrationSummary.setConnectivity(null);
					if(actionForm.getConnectivityFlag().equalsIgnoreCase("block"))
						technicalRegistrationSummary.setConnectivity(actionForm.getTechnicalRegistrationSummary().getConnectivity());
					
					//GRT 4.0 Changes-BRS Requirement-BR-F.006
					technicalRegistrationSummary.setPopUpHiddenValue(actionForm.getPopUpHiddenValue());
					if(actionForm.getPopUpHiddenValue().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY)){
						technicalRegistrationSummary.setAccessTypes(GRTConstants.NO_CONNECTIVITY);
						technicalRegistrationSummary.setConnectivity(GRTConstants.NO);
					}else{
						//GRT 4.0 Changes-BRS Requirement-BR-F.006
						if(StringUtils.isEmpty(technicalRegistrationSummary.getConnectivity()))
						technicalRegistrationSummary.setConnectivity(GRTConstants.YES);
					}
					if(actionForm.getPopUpHiddenValue().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY) && !actionForm.getNoConnectivityAccType().equalsIgnoreCase(" ")){
						technicalRegistrationSummary.setAccessTypes(actionForm.getNoConnectivityAccType());
						technicalRegistrationSummary.setConnectivity(GRTConstants.NO);
					}else{
						//GRT 4.0 Changes-BRS Requirement-BR-F.006
						if(StringUtils.isEmpty(technicalRegistrationSummary.getConnectivity()))
							technicalRegistrationSummary.setConnectivity(GRTConstants.YES);
					}
					
					if (GRTConstants.YES.equalsIgnoreCase(actionForm.getTechnicalRegistrationSummary().getMainCM())){
						actionForm.getTechnicalRegistrationSummary().setRemoteDeviceType("");
						if(GRTConstants.SPTEM.equalsIgnoreCase(actionForm.getTechnicalRegistrationSummary().getProductType()) ) {
							actionForm.getTechnicalRegistrationSummary().setMid(GRTConstants.NUMBER_ONE);
						}
					}
					if(technicalRegistrationSummary.isAddFlag()) {
						if (StringUtils.isNotEmpty(actionForm.getTechnicalRegistrationSummary().getSid()) 
								&& StringUtils.isNotEmpty(actionForm.getTechnicalRegistrationSummary().getMid())) {
							String specialNote = actionForm.getSpecialNote();
							if (!actionForm.getSidMid().isEmpty() && !GRTConstants.CAAS.equalsIgnoreCase(specialNote) 
									&& !GRTConstants.SECODE.AACEMS.equalsIgnoreCase(actionForm.getTrConfig().getSeCode()) 
									&& !GRTConstants.SECODE.CNTXSR.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& !GRTConstants.SECODE.ACDBA.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& !GRTConstants.SECODE.WAECE.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& !GRTConstants.SECODE.WEBRTC.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& !GRTConstants.SECODE.RTSCE.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& !GRTConstants.SECODE.PSEDP.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())
									&& actionForm.getSidMid().contains(actionForm.getTechnicalRegistrationSummary().getSid() + ":" + actionForm.getTechnicalRegistrationSummary().getMid())) {
								actionForm.setDuplicateSidMidFlag("block");
								actionForm.setSidMidValidatedFlag("none");
								return "except";
							} else {
								actionForm.getSidMid().add(actionForm.getTechnicalRegistrationSummary().getSid() + ":" + actionForm.getTechnicalRegistrationSummary().getMid());  
							}
						}	  
						summary.setRemainingQty(technicalRegistrationSummary.getRemainingQty()- 1);
						summary.setEquipmentNumber(technicalRegistrationSummary.getEquipmentNumber());
						summary.setOperationType(technicalRegistrationSummary.getOperationType());
						if(regSumList == null){
							regSumList = new ArrayList<TechnicalRegistrationSummary>();
						}
						regSumList.add(technicalRegistrationSummary);
					} else if(actionForm.getIndex() != null) {
						regSumList.remove(Integer.parseInt(actionForm.getIndex()));
						regSumList.add(Integer.parseInt(actionForm.getIndex()), technicalRegistrationSummary);
					}
				}
				summaryListNew.add(summary);
			}
			actionForm.setRegistrationSummaryList(summaryListNew);
			actionForm.setRegistrationSummaryListRegistrable(regSumList);
		} catch(Exception exception) {
			logger.error("", exception);
			throw exception;
		} finally {
			logger.debug("Exiting addTechnicalRegistrationConfig");
		}
		//Begin: Clear the Error Messages
		actionForm.setCount(null);
		actionForm.setErrorMessage(null);
		actionForm.setSoldTo(null);
		actionForm.setSalSeId(null);
		//End: Clear the Error Messages		
		return technicalRegistrationDashboard();
	}

	public void setTRDerivables(TechnicalRegistrationSummary technicalRegistrationSummary) throws Exception {
		String connectivity = technicalRegistrationSummary.getConnectivity();
		if (GRTConstants.YES.equalsIgnoreCase(connectivity)) {
			technicalRegistrationSummary.setOperationType(ARTOperationType.FULLNEW.getOperationKey());
		} else if (GRTConstants.NO.equalsIgnoreCase(connectivity)) {
			technicalRegistrationSummary.setOperationType(ARTOperationType.DATABASENEW.getOperationKey());
		}
		logger.debug("RANDOM PASS :::" + technicalRegistrationSummary.getRandomPassword());
		if (GRTConstants.YES.equalsIgnoreCase(technicalRegistrationSummary.getRandomPassword())){
			technicalRegistrationSummary.setRandomPassworddb(GRTConstants.Y);
		} else if (GRTConstants.NO.equalsIgnoreCase(technicalRegistrationSummary.getRandomPassword())) {
			technicalRegistrationSummary.setRandomPassworddb(GRTConstants.N);
		} else if (GRTConstants.DEFAULT.equalsIgnoreCase(technicalRegistrationSummary.getRandomPassword())) {
			technicalRegistrationSummary.setRandomPassworddb(GRTConstants.D);
		}
		logger.debug("CHECK SES EDGE :::" + technicalRegistrationSummary.getCheckIfSESEdge());
		if (GRTConstants.YES.equalsIgnoreCase(technicalRegistrationSummary.getCheckIfSESEdge())){
			technicalRegistrationSummary.setCheckIfSESEdgeDb(GRTConstants.ON);
		} else if (GRTConstants.NO.equalsIgnoreCase(technicalRegistrationSummary.getCheckIfSESEdge())) {
			technicalRegistrationSummary.setCheckIfSESEdgeDb(GRTConstants.OFF);
		}
		logger.debug("SAMPBOARD UPGRADED :::" + technicalRegistrationSummary.getSampBoardUpgraded());
		if (GRTConstants.YES.equalsIgnoreCase(technicalRegistrationSummary.getSampBoardUpgraded())){
			technicalRegistrationSummary.setSampBoardUpgradedDb(GRTConstants.BOOLEAN_TRUE);
		} else if (GRTConstants.NO.equalsIgnoreCase(technicalRegistrationSummary.getSampBoardUpgraded())) {
			technicalRegistrationSummary.setSampBoardUpgradedDb(GRTConstants.BOOLEAN_FALSE);
		}

	}

	/****
	 * update existing registration
	 * @return
	 * @throws Exception
	 */
	public String populateTRConfigForConfig() throws Exception {
		logger.debug("Entering populateTRConfigForConfig.");
		validateSession();
		this.resetTRConfigForm(actionForm);
		actionForm.setLssOrESSLowerVersionFlag("");
		actionForm.setUpgradedMainCMuseRFASIDFlag("");
		//AUX MC Changes
		actionForm.getTechnicalRegistrationSummary().setAuxMCShowFlag(null);
		actionForm.getTechnicalRegistrationSummary().setAuxMCMainSEID(null);
		actionForm.getTechnicalRegistrationSummary().setIsAuxMCSEIDFlag(null);		
		//Begin: Clear the AccesssType and set GroupId Changed flag to YES
		actionForm.setGroupIdChnaged(GRTConstants.YES);
		//End: Clear the AccesssType and set GroupId Changed flag to YES
		String groupId = actionForm.getTechnicalRegistrationSummary().getGroupId();
		logger.debug("populateTRConfig GroupID :"+groupId);
		if(groupId != null){
			List<TRConfig> trConfList = actionForm.getTrConfigList();
			for(TRConfig trConfig : trConfList){
				if(groupId.equalsIgnoreCase(trConfig.getGroupId())){
					TechnicalRegistrationSummary techRegSummary = actionForm.getTechnicalRegistrationSummary();
					techRegSummary.setGroupId(groupId);
					techRegSummary.setTrConfig(trConfig);
					techRegSummary.setSeCodePreview(getSeCodePreview(groupId));
					actionForm.setTrConfig(trConfig);
					break;
				}
			}
		}

		actionForm.setGroupIds(actionForm.getGroupIds());
		logger.debug("populateTRConfig Access Type :"+actionForm.getTechnicalRegistrationSummary().getAccessType());
		if(GRTConstants.SAL.equalsIgnoreCase(actionForm.getTechnicalRegistrationSummary().getAccessType())){
			getRequest().getSession().setAttribute("salGWFlag", "true");
		}
		this.applyRulesOnTRConfiguration(actionForm);
		logger.debug("Exiting populateTRConfigForConfig.");

		return "success";
	}

	private void resetTRConfigForm(RegistrationFormBean form) {
		form.getTechnicalRegistrationSummary().setSid(null);
		form.getTechnicalRegistrationSummary().setMid(null);
		form.getTechnicalRegistrationSummary().setAuthFileID(null);
		form.getTechnicalRegistrationSummary().setCmMainMaterialCodeDesc(null);
		form.getTechnicalRegistrationSummary().setCmMainsoldTo(null);
		form.getTechnicalRegistrationSummary().setCmMainSID(null);
		form.getTechnicalRegistrationSummary().setCheckIfSESEdge(null);
		form.getTechnicalRegistrationSummary().setCheckIfSESEdgeDb(null);
		form.setConnectivities(null);
		form.getTechnicalRegistrationSummary().setConnectivity(null);
		form.getTechnicalRegistrationSummary().setAlarmOrigination(null);
		form.getTechnicalRegistrationSummary().setDialInNumber(null);
		form.setHardwareServer(null);
		form.getTechnicalRegistrationSummary().setHardwareServer(null);
		form.getTechnicalRegistrationSummary().setIpAddress(null);
		form.getTechnicalRegistrationSummary().setOutboundPrefix(null);
		form.getTechnicalRegistrationSummary().setPrimarySalgwSeid(null);
		form.getTechnicalRegistrationSummary().setPrivateIP(null);
		form.getTechnicalRegistrationSummary().setRandomPassword(null);
		form.getTechnicalRegistrationSummary().setRandomPassworddb(null);
		form.getTechnicalRegistrationSummary().setSampBoardUpgraded(null);
		form.getTechnicalRegistrationSummary().setSampBoardUpgradedDb(null);
		form.getTechnicalRegistrationSummary().setSecondarySalgwSeid(null);
		form.getTechnicalRegistrationSummary().setSeidOfVoicePortal(null);
		form.setSoftwareRelease(null);
		form.getTechnicalRegistrationSummary().setSoftwareRelease(null);
		form.getTechnicalRegistrationSummary().setSolutionElementId(null);
		form.setSpRelease(null);
		form.getTechnicalRegistrationSummary().setSystemProductRelease(null);
		
		//GRT 4.0 Changes
				String materialCode = actionForm.getMaterialCodeDescription();
				
				//GRT 4.0 Changes
				if(!actionForm.getPopUpHiddenValue().equalsIgnoreCase(GRTConstants.NO_CONNECTIVITY))
					actionForm.setPopUpHiddenValue("");
				
				if(materialCode.contains(GRTConstants.NO_CONNECTIVITY)){
				String[] code=materialCode.split(",");
					materialCode=code[0];
					popUpValue=code[1];
					popUpAccessType=code[2];
					actionForm.setPopUpHiddenValue(popUpValue);
					actionForm.setNoConnectivityAccType(popUpAccessType);
				}
	}

	/***
	 * Get solution element code details
	 * for preview page
	 * @param groupId
	 * @return
	 */
	public String getSeCodePreview(String groupId) {
		String preview = "";
		try {
			//Service Call
			preview = getTechnicalOnBoardingService().getGroupSeCodes(groupId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("error getting preview string for group Id "+groupId+e);
		}    	 
		return preview;

	}
	
	public String validateAUXMCMainSEID() throws Exception {
		logger.debug("Entering validateAUXMCMainSEID...");
		validateSession();
		String auxMCMainSEID = actionForm.getTechnicalRegistrationSummary().getAuxMCMainSEID();
		actionForm.setTemp_auxMCMainSEID(auxMCMainSEID);
		
		logger.debug("Entered AUX MC Main SEID ::: " + auxMCMainSEID);
		actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.BLOCK);
		actionForm.setAuxMCMainSEIDErrorMsg("The SEID entered is not valid.");
		//logic for SEID validation..		
		
		if(StringUtils.isNotEmpty(auxMCMainSEID)) {
			//Check SEID is Active and SID/MID are not null in Siebel
			
			AUXMCMain auxMCMain = getTechnicalOnBoardingService().validateAUXMCMainSEID(auxMCMainSEID);
			
			if(auxMCMain != null && auxMCMain.getSeid() != null && auxMCMainSEID.equalsIgnoreCase(auxMCMain.getSeid())){
				if(auxMCMain.getStatus() != null && StringUtils.isNotEmpty(auxMCMain.getStatus()) && auxMCMain.getStatus().equalsIgnoreCase("ACTIVE")){
					if(StringUtils.isNotEmpty(auxMCMain.getSid()) && StringUtils.isNotEmpty(auxMCMain.getMid())){
						//if all good
						actionForm.getTechnicalRegistrationSummary().setAuxMCSid(auxMCMain.getSid());
						actionForm.getTechnicalRegistrationSummary().setAuxMCMid(auxMCMain.getMid());
						
						actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.NONE);
						actionForm.setAuxMCMainSEIDErrorMsg("");						
					}else{
						actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.BLOCK);
				        actionForm.setAuxMCMainSEIDErrorMsg("The SEID entered does not have valid SID/MID.");
					}
				}else{
					actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.BLOCK);
			        actionForm.setAuxMCMainSEIDErrorMsg("The SEID entered is not active.");
				}				
			}else{
				actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.BLOCK);
		        actionForm.setAuxMCMainSEIDErrorMsg("The SEID entered is not valid.");
			}			
		} else {
			actionForm.setAuxMCMainSEIDErrorFlag(GRTConstants.BLOCK);
	        actionForm.setAuxMCMainSEIDErrorMsg("The SEID entered is not valid.");	        
		}
		
		logger.debug("Exiting validateAUXMCMainSEID...");
		return "validate";
	}

	/***
	 * After entering SID MID validate action
	 * for validate entered SID/MID 
	 * @return
	 * @throws Exception
	 */
	public String validateSIDMIDQuery() throws Exception {
		logger.debug("Entering validateSIDMIDQuery...");
		validateSession();
		String sid = actionForm.getTechnicalRegistrationSummary().getSid();
		String mid = actionForm.getTechnicalRegistrationSummary().getMid();
		actionForm.setTempSid(sid);
		actionForm.setTempMid(mid);
		actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
		actionForm.setDuplicateSidMidFlag(GRTConstants.NONE);
		actionForm.setSidMidErrorFlag(GRTConstants.NONE);
		String productType = actionForm.getTrConfig().getProductType();
		String seCode = actionForm.getTrConfig().getSeCode();
		String cmMain = actionForm.getTechnicalRegistrationSummary().getMainCM();
		String soldToId = actionForm.getSoldToId();
		List<TechnicalRegistrationSummary> regSumList = null;

		String primarySeId = actionForm.getPrimary();
		getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID,primarySeId);
		String secondarySeId = actionForm.getSecondary();
		getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID,secondarySeId);

		try {
			String specialNote = actionForm.getSpecialNote();
			logger.debug("$$$Special Note  :" + specialNote);
			if ((seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCWSW) && GRTConstants.HIGH_AVAILABILITY.equalsIgnoreCase(specialNote))
					|| (seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCMSL) && GRTConstants.MEDIA_SERVER.equalsIgnoreCase(specialNote))
					|| (seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCMST) && GRTConstants.BASE_SERVER.equalsIgnoreCase(specialNote))) {

				regSumList = actionForm.getRegistrationSummaryListRegistrable();
				if (regSumList != null) {
					for (TechnicalRegistrationSummary summary : regSumList) {
						if (summary.getSid().equalsIgnoreCase(actionForm.getTechnicalRegistrationSummary().getSid()) 
								&& summary.getSolutionElement().equalsIgnoreCase(seCode)) {
							actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
							this.actionForm.setSidMidStatus(grtConfig.getDuplicateSidSeCode());
							return "validate";
						}
					}
				}	
				//Service Call
				Integer i = getTechnicalOnBoardingService().validateSeCodeAndSid(sid, seCode);
				logger.debug("validateSeCodeAndSid for SID:" + sid +", Secode:" + seCode +", result of i:" +i );
				if (i == 1) {
					this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
					actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
				} else if (i == 0) {
					actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
					this.actionForm.setSidMidStatus(grtConfig.getInvalidSid());
				} else if (i == -1) {
					actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
					this.actionForm.setSidMidStatus(grtConfig.getSidStandByAlreadyExists());
				}
			} else {
				if (GRTConstants.PRODUCTTYPE.RADVSN.equalsIgnoreCase(productType)) {
					actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
					this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
					return "validate";
				}
				if (seCode.equalsIgnoreCase(GRTConstants.SECODE.CNTXSR) || seCode.equalsIgnoreCase(GRTConstants.SECODE.AACEMS) 
						|| seCode.equalsIgnoreCase(GRTConstants.SECODE.ACDBA) || seCode.equalsIgnoreCase(GRTConstants.SECODE.WAECE)
						|| seCode.equalsIgnoreCase(GRTConstants.SECODE.WEBRTC) || seCode.equalsIgnoreCase(GRTConstants.SECODE.RTSCE)
						|| seCode.equalsIgnoreCase(GRTConstants.SECODE.PSEDP)) {
					//Service Call
					List<String> fls = getTechnicalOnBoardingService().validateSIDAndFL(sid, soldToId, seCode);
					logger.debug("Value Returned :::" + fls);
					if (fls.get(0).equals(GRTConstants.NUMBER_ZERO)) {
						this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
						actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
					} else if (fls.get(0).equals(GRTConstants.NUMBER_ONE)) {
						actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
						String flDoesNotMatch = grtConfig.getFlNotMatching1()+ fls.get(1) + grtConfig.getFlNotMatching2()
								+ soldToId + grtConfig.getFlNotMatching3();
						this.actionForm.setSidMidStatus(flDoesNotMatch);
					} else if (fls.get(0).equals(GRTConstants.NUMBER_TWO)) {
						actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
						this.actionForm.setSidMidStatus(grtConfig.getAaceSidMissing());
					} 
					return "validate";
				}
				//Service Call
				List<String> mids = getTechnicalOnBoardingService().validateSIDMID(sid);
				logger.debug("LIST MIDs ::" + mids.toString());
				if (mids.contains(mid)){
					logger.debug("CONTAINS MID ::" + mid);
					if ((GRTConstants.CAAS.equalsIgnoreCase(specialNote) && !seCode.equalsIgnoreCase(GRTConstants.SECODE.VCM))){ 
						actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
						this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
					} else {
						if (cmMain.equalsIgnoreCase(GRTConstants.NO)) {
							if (!mids.contains(GRTConstants.NUMBER_ONE)) {
								this.actionForm.setSidMidStatus(grtConfig.getInvalidSidMain());
								actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
								actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
								logger.debug( "Error1a - Entered SID does not have a valid main" );
								return "validate";
							} 
						}
						actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
						this.actionForm.setSidMidStatus(grtConfig.getMidConsumed());
					}
				} else {
					if (GRTConstants.CAAS.equalsIgnoreCase(specialNote) && !seCode.equalsIgnoreCase(GRTConstants.SECODE.VCM)) {
						actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
						this.actionForm.setSidMidStatus(grtConfig.getMissingMain());
					} else {
						if (cmMain.equalsIgnoreCase(GRTConstants.YES) && (productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.IP600) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.DFONE) 
								|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.CMEXP) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.BLADE)
								|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.G700) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S84IP)
								|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S85IP) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S87MC)
								|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S88IP))) {
							//Service Call
							String sidMidResponse = getTechnicalOnBoardingService().SIDMIDService(sid, mid);
							logger.debug("SID MID SERVICE RESPONSE from main:::" + sidMidResponse);
							if (!sidMidResponse.startsWith(GRTConstants.HTTP_200)) {
								this.actionForm.setSidMidStatus(sidMidResponse);
								actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
								actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
								logger.debug( "Exiting RFA SIDMID service with error for main" );
								return "validate";
							}
							logger.debug( "Exiting RFA SIDMID service with NO error for main" ); 
						} else if (cmMain.equalsIgnoreCase(GRTConstants.NO)) {
							if (mids.isEmpty() || !mids.contains(GRTConstants.NUMBER_ONE)) {
								this.actionForm.setSidMidStatus(grtConfig.getInvalidSidMain());
								actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
								actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
								logger.debug( "Error1b - Entered SID does not have a valid main" );

								return "validate";
							} else {
								if (productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.IP600) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.DFONE) 
										|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.CMEXP) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.BLADE)
										|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.G700) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S84IP)
										|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S85IP) || productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S87MC)
										|| productType.equalsIgnoreCase(GRTConstants.PRODUCTTYPE.S88IP)){
									String sidMidResponse = getTechnicalOnBoardingService().SIDMIDService(sid, mid);
									logger.debug("SID MID SERVICE RESPONSE from remote :::" + sidMidResponse);
									if (sidMidResponse.startsWith(GRTConstants.HTTP_200)) {
										actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
										this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
										return "validate";
									} else {
										this.actionForm.setSidMidStatus(sidMidResponse);
										actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
										actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
										logger.debug( "Exiting RFA SIDMID service with error for remote" );
										return "validate";
									}
								}

							}
						}
						actionForm.setSidMidValidatedFlag(GRTConstants.BLOCK);
						this.actionForm.setSidMidValidated(grtConfig.getSidMidValidated());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.debug("Exiting validateSIDMIDQuery...");
		return "validate";
	}

	/****
	 * Re-Submit registration from technical registration detail
	 * if seid creation failed.
	 * Action
	 * @return
	 * @throws Exception
	 */
	public String submitTrDetails() throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug(" Entering  RegistrationController.submitTrDetail()");
			if (actionForm.getTrDetailType().equals(GRTConstants.NONSAL)){
			String result = processTrDetail(actionForm);
			if (result.equals("submitSuccess") || result.equals("overrideSuccess")) {
				long c2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("Timer to Submit NON_SAL > TR Details to ART for Technical Registration OR to Override the Status >> Time in milliseconds"+ (c2-c1));
				return getTechnicalOrderDetails();
			} else if (result.equals("loadedTrForUpdate")) {
				this.tobResubmitFlag = true;
				actionForm.setTobResubmitFlag(true);
				return technicalRegistrationUpdate();
			} else if (result.equals("loadedTrForConfig")) {
				this.tobResubmitFlag = true;
				actionForm.setTobResubmitFlag(true);
				actionForm.setPopUpHiddenValue("");
				if(actionForm.getTechnicalRegistrationSummary().getConnectivity().equalsIgnoreCase(GRTConstants.NO)||actionForm.getTechnicalRegistrationSummary().getConnectivity()==null || actionForm.getTechnicalRegistrationSummary().getConnectivity()=="")
				actionForm.setPopUpHiddenValue(GRTConstants.NO_CONNECTIVITY);
				return technicalRegistrationConfig();
			} else {
				return "failure";
			}


		} else if (actionForm.getTrDetailType().equals(GRTConstants.SAL)) {
			String result = processSalRegistrationSummary(actionForm);
			if (result.equals("submitSuccess") || result.equals("overrideSuccess")) {
				long c2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("Timer to Submit SAL > TR Details to ART for Technical Registration OR to Override the Status >> Time in milliseconds"+ (c2-c1));
				return getTechnicalOrderDetails();
			} else if (result.equals("loadedTrForUpdate")) {
				this.tobResubmitFlag = true;
				actionForm.setTobResubmitFlag(true);
				return technicalRegistrationUpdate();
			} else if (result.equals("loadedTrForConfig")) {
				this.tobResubmitFlag = true;
				actionForm.setTobResubmitFlag(true);
				if(actionForm.getTechnicalRegistrationSummary().getConnectivity().equalsIgnoreCase(GRTConstants.NO)||actionForm.getTechnicalRegistrationSummary().getConnectivity()==null || actionForm.getTechnicalRegistrationSummary().getConnectivity()=="")
					actionForm.setPopUpHiddenValue(GRTConstants.NO_CONNECTIVITY);
				return technicalRegistrationConfig();
			} else if (result.equals("stepBDetail")) {
				return "stepB";
			} else {
				return "failure";
			}
		} else if (actionForm.getTrDetailType().equals(GRTConstants.SAL_MIGRATION)){
			String result = processSalMigrationSummary(actionForm);
			if (result.equals("submitSuccess") || result.equals("overrideSuccess")) {
				long c2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("Timer to Submit SAL Migration Details to ART for Technical Registration OR to Override the Status >> Time in milliseconds"+ (c2-c1));
				return getTechnicalOrderDetails();
			} else if (result.equals("loadedTrForUpdate")) {
				this.tobResubmitFlag = true;
				actionForm.setTobResubmitFlag(true);
				return technicalRegistrationUpdate();
			} else if (result.equals("stepBDetail")) {
				return "stepB";
			}  else {
				return "failure";
			}
		}

		logger.debug("NO CONDITION SATISFIED ......  NO CONDITION SATISFIEL");
		logger.debug(" Exiting  RegistrationController.submitTrDetail()");
		return "failure";

	}

	private String processTrDetail(RegistrationFormBean form) throws Exception {
		String result = null;
		TechnicalRegistration selectedTr = null;
		String uiOperation = form.getUiOperation();

		List<TechnicalRegistration> trDetailList = form.getTechnicalRegistrationDetailList();
		if(null != trDetailList && trDetailList.size() >0) {
			for(int i=0; i<trDetailList.size(); i++){
				TechnicalRegistration tr = trDetailList.get(i);
				if(tr != null){
					if (tr.getTechnicalRegistrationId().equals(form.getTechnicalRegistrationId())  && i == Integer.parseInt(form.getIndex()) ) {
						selectedTr = tr;
						break;
					}
				}
			}
		}

		if (selectedTr != null) {
			SiteRegistration siteRegistration   = (SiteRegistration) (getRequest().getSession().getAttribute(GRTConstants.SITE_REGISTRATION));
			if (uiOperation != null ) {
				if (uiOperation.equals("Submit")){
					//Submit for Technical Registration
					logger.debug("The siteRegistration.getRegistrationId() =======> " + siteRegistration.getRegistrationId() + "<========");
					List<TechnicalRegistration> techRegList = new ArrayList<TechnicalRegistration>();
					selectedTr.setStepAReSubmittedDate(new Date());
					techRegList.add(selectedTr);
					ARTOperationType operation = getOperationType(selectedTr.getOperationType());
					if(operation==null)
					 operation = getOperationType("ON");
					if (operation != null) {
						//Service calls 
						getTechnicalOnBoardingService().updateStepAResubmittedDate(selectedTr);
						getTechnicalOnBoardingService().doTechnicalRegistration(siteRegistration, techRegList, null, operation);
					}
					result = "submitSuccess";

				} else if(uiOperation.equals("Update")){
					String primarySeId = selectedTr.getPrimarySalGWSeid();
					getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID,primarySeId);
					String secondarySeId = selectedTr.getSecondarySalGWSeid();
					getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID,secondarySeId);
					List<TechnicalRegistrationSummary> techRegSummaryList = constructTechnRegSummaryList(selectedTr, siteRegistration.getRegistrationId());
					//TR-Update screen when SEID of that record is not null
					String seid = selectedTr.getSolutionElementId();
					if (seid != null ) {
						form.setTechnicalRegistrationSummary(techRegSummaryList.get(0));
						getRequest().getSession().setAttribute(GRTConstants.FROM_TR_OB_DETAIL, GRTConstants.TRUE);
						actionForm.setFromTrObDetail( GRTConstants.TRUE);
						result =  "loadedTrForUpdate";
					} else {
						form.setTechnicalRegistrationSummary(techRegSummaryList.get(0));
						form.setMaterialCodeDescription(techRegSummaryList.get(0).getMaterialCode());
						form.setAccessType(techRegSummaryList.get(0).getAccessType());
						getRequest().getSession().setAttribute(GRTConstants.FROM_TR_OB_DETAIL, GRTConstants.TRUE);
						actionForm.setFromTrObDetail( GRTConstants.TRUE);
						getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
						if(selectedTr.getFailedSeid() != null){
							this.actionForm.setFailedSeidIsNotNull(true);
						} else {
							this.actionForm.setFailedSeidIsNotNull(false);
						}
						result =  "loadedTrForConfig";
					}

				} else if(uiOperation.equals("Override")){
					// Change the status to Update the TR record
					//Service Call
					TechnicalRegistration technicalRegistration = getTechnicalOnBoardingService().updateTechRegStatus(selectedTr, StatusEnum.COMPLETED,siteRegistration );
					for(int i=0; i<trDetailList.size(); i++){
						TechnicalRegistration tr = trDetailList.get(i);
						if(tr != null){
							if (tr.getTechnicalRegistrationId().equals(technicalRegistration.getTechnicalRegistrationId())  ) {
								trDetailList.get(i).setStatus(tr.getStatus());
								break;
							}
						}
					}
					result =  "overrideSuccess";
				}
			}

		} else {
			result = "noTrSelected";
		}

		return result;

	}

	private String processSalRegistrationSummary(RegistrationFormBean form) throws Exception {
		String result = null;
		TechnicalRegistration selectedTr = null;
		String uiOperation = form.getUiOperation();
		List<TechnicalRegistration> trDetailList = form.getSalRegistrationSummaryList();
		if(null != trDetailList && trDetailList.size() >0) {
			for(int i=0; i<trDetailList.size(); i++){
				TechnicalRegistration tr = trDetailList.get(i);
				if(tr != null){
					if (tr.getTechnicalRegistrationId().equals(form.getTechnicalRegistrationId())  && i == Integer.parseInt(form.getIndex()) ) {
						selectedTr = tr;
						break;
					}
				}
			}
		}

		if (selectedTr != null) {
			SiteRegistration siteRegistration   = (SiteRegistration) (getRequest().getSession().getAttribute(GRTConstants.SITE_REGISTRATION));
			if (uiOperation != null ) {
				if (uiOperation.equals("Submit")){
					//Submit for Technical Registration
					logger.debug("The siteRegistration.getRegistrationId() =======>" + siteRegistration.getRegistrationId() + "<========");
					List<TechnicalRegistration> techRegList = new ArrayList<TechnicalRegistration>();
					selectedTr.setStepAReSubmittedDate(new Date());
					techRegList.add(selectedTr);
					ARTOperationType operation = getOperationType(selectedTr.getOperationType());
					if(operation==null &&selectedTr.getConnectivity().equalsIgnoreCase(GRTConstants.NO) )
					{
						operation=ARTOperationType.DATABASENEW;
						selectedTr.setOperationType(ARTOperationType.DATABASENEW.getOperationKey());
					}
					if (operation != null) {
						//Service Call
						getTechnicalOnBoardingService().updateStepAResubmittedDate(selectedTr);
						getTechnicalOnBoardingService().doTechnicalRegistration(siteRegistration, techRegList, null, ARTOperationType.DATABASENEW);
					}
					result = "submitSuccess";

				} else if(uiOperation.equals("Update")){
					String primarySeId = selectedTr.getPrimarySalGWSeid();
					getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID,primarySeId);
					String secondarySeId = selectedTr.getSecondarySalGWSeid();
					getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID,secondarySeId);
					List<TechnicalRegistrationSummary> techRegSummaryList = constructTechnRegSummaryList(selectedTr, siteRegistration.getRegistrationId());

					//TR-Update screen when SEID of that record is not null
					String seid = selectedTr.getSolutionElementId();

					if (seid != null ) {
						//TRUpdate.
						form.setTechnicalRegistrationSummary(techRegSummaryList.get(0));
						getRequest().getSession().setAttribute(GRTConstants.FROM_TR_OB_DETAIL, GRTConstants.TRUE);
						actionForm.setFromTrObDetail( GRTConstants.TRUE);
						result =  "loadedTrForUpdate";
					} else {
						//TR Config
						form.setTechnicalRegistrationSummary(techRegSummaryList.get(0));
						form.setMaterialCodeDescription(techRegSummaryList.get(0).getMaterialCode());
						form.setAccessType(techRegSummaryList.get(0).getAccessType());
						getRequest().getSession().setAttribute(GRTConstants.FROM_TR_OB_DETAIL, GRTConstants.TRUE);
						actionForm.setFromTrObDetail( GRTConstants.TRUE );
						getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
						if(selectedTr.getFailedSeid() != null){
							this.actionForm.setFailedSeidIsNotNull(true);
						} else {
							this.actionForm.setFailedSeidIsNotNull(false);
						}
						result =  "loadedTrForConfig";
					}

				} else if(uiOperation.equals("Override")){
					// Change the status to Update the TR record
					//Service Call
					TechnicalRegistration technicalRegistration = getTechnicalOnBoardingService().updateTechRegStatus(selectedTr, StatusEnum.COMPLETED, siteRegistration);
					for(int i=0; i<trDetailList.size(); i++){
						TechnicalRegistration tr = trDetailList.get(i);
						if(tr != null){
							if (tr.getTechnicalRegistrationId().equals(technicalRegistration.getTechnicalRegistrationId())  ) {
								trDetailList.get(i).setStatus(tr.getStatus());
								break;
							}
						}
					}
					result =  "overrideSuccess";
				} else if(uiOperation.equals("Detail")){
					List<TechnicalRegistration> selectedList = new ArrayList<TechnicalRegistration>();
					processChildForDetailView(selectedTr, null, form);
					selectedTr.setRemoteAccessCheckBoxDisabled(true);
					selectedTr.setAlarmingCheckBoxDisabled(true);
					
					
					//GRT 4.0 Changes-Defect 709-Entitled For Alarming
					List<String> materialList=new ArrayList<String>();
					materialList.add(selectedTr.getTechnicalOrder().getMaterialCode());
					List<String> alarmingList=getBaseRegistrationService().getEntitledForAlarming(materialList, form.getSoldToId());
					
					selectedTr.setEntitledForAlarming("No");
					selectedTr.setAlarmingCheckBoxDisabled(true);
					for(ExpandedSolutionElement s: selectedTr.getExpSolutionElements()){
							s.setAlarmingCheckBoxDisabled(true);
					}
					
					for(String s:alarmingList){
					if(selectedTr.getTechnicalOrder().getMaterialCode().equalsIgnoreCase(s)){
						selectedTr.setEntitledForAlarming("Yes");
						
						}
					}
					
					List<ExpandedSolutionElement> expSolEleListParent = getTechnicalOnBoardingService().getAssetsWithSameSIDandMIDandSEID(selectedTr.getSid(), selectedTr.getMid(), selectedTr.getSolutionElementId());
					for(ExpandedSolutionElement element:expSolEleListParent)
					{
						if(selectedTr.getSolutionElementId().equalsIgnoreCase(element.getSeID()))
						{
							selectedTr.setPrimarySalGWSeid(element.getIpAddress());
						}
					}
					
					for(ExpandedSolutionElement exS : selectedTr.getExplodedSolutionElements())
					{
						
						CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(exS.getSeCode());
						if(compatibilityMatrix != null){
							exS.setTransportAlarmEligible(compatibilityMatrix.getTransportAlarm());
							
						}
						
						List<ExpandedSolutionElement> expSolEleList = getTechnicalOnBoardingService().getAssetsWithSameSIDandMID(selectedTr.getSid(), selectedTr.getMid(), selectedTr.getSolutionElementId());
						for(ExpandedSolutionElement element:expSolEleList)
						{
							if(exS.getSeID().equalsIgnoreCase(element.getSeID()))
							{
								exS.setPrimarySalGWSeid(element.getIpAddress());
							}
							
						}
						
						
						
					}
					
					
						
										
					selectedList.add(selectedTr);
					form.setSelectedSALRegSummaryList(selectedList);
					getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
					getRequest().getSession().setAttribute(GRTConstants.STEPB_DETAIL_VIEW, GRTConstants.TRUE);
					result = "stepBDetail";
				}
			}

		} else {
			result = "noTrSelected";

		}

		return result;

	}

	private String processSalMigrationSummary(RegistrationFormBean form) throws Exception {

		logger.debug("<------------------------------------------Entering processSalMigrationSummary()   -------------------------------------------------------------->");
		//Submit for Technical Registration
		String result = null;
		SiteList selectedSl = null;
		String uiOperation = form.getUiOperation();
		List<SiteList> siteListObject = form.getSalMigrationSummaryList();
		if(null != siteListObject && siteListObject.size() >0) {
			for(int i=0; i<siteListObject.size(); i++){
				SiteList sl = siteListObject.get(i);
				if(sl != null){
					if (sl.getId().equals(form.getTechnicalRegistrationId())  && i == Integer.parseInt(form.getIndex()) ) {
						selectedSl = sl;
						break;
					}
				}
			}
		}

		if (selectedSl != null) {
			if (uiOperation != null ) {
				if (uiOperation.equals("Submit")){
					//Submit for Technical Registration
					SiteRegistration siteRegistration   = (SiteRegistration) (getRequest().getSession().getAttribute(GRTConstants.SITE_REGISTRATION));
					logger.debug("The siteRegistration.getRegistrationId() =======>" + siteRegistration.getRegistrationId() + "<======");

					List<SiteList> siteListData = new ArrayList<SiteList>();
					siteListData.add(selectedSl);
					//Service Call
					getTechnicalOnBoardingService().doTechnicalRegistration(siteRegistration, null, siteListData, ARTOperationType.DATABASEUPDATE);

					result = "submitSuccess";

				} else if(uiOperation.equals("Update")){
					form.setSelectedSalMigrationForUpdate(selectedSl);
					result =  "loadedSlForUpdate";
				} else if(uiOperation.equals("Override")){
					// Change the status to Update the TR record
					//Service Call
					SiteList siteList = getTechnicalOnBoardingService().updateSiteListStatus(selectedSl, StatusEnum.COMPLETED);
				    //Populate the Status
					for(int i=0; i<siteListObject.size(); i++){
						SiteList sl = siteListObject.get(i);
						if(sl != null){
							if (sl.getId().equals(siteList.getId())  ) {
								siteListObject.get(i).setStatus(sl.getStatus());
								break;
							}
						}
					}
					//Populate the Status
					result =  "overrideSuccess";
				} else if(uiOperation.equals("Detail")){

					
					processChildForDetailView(null, selectedSl, form);
					selectedSl.setRemoteAccessCheckBoxDisabled(true);
					selectedSl.setAlarmingCheckBoxDisabled(true);
					List<SiteList> selectedList = new ArrayList<SiteList>();
					selectedList.add(selectedSl);
					form.setSelectedSALMigrationSummaryList(selectedList);
					getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
					getRequest().getSession().setAttribute(GRTConstants.STEPB_DETAIL_VIEW, GRTConstants.TRUE);
					result = "stepBDetail";
				}
			}

		} else {
			result = "noSiteListSelected";
		}

		logger.debug("<------------------------------------------Exiting processSalMigrationSummary()   -------------------------------------------------------------->");
		return result;

	}

	private ARTOperationType getOperationType(String operation){
		if(operation!=null && operation.equals("DU")){
			return ARTOperationType.DATABASEUPDATE;
		} else if(operation!=null && operation.equals("DN")){
			return ARTOperationType.DATABASENEW;
		} else if(operation!=null && operation.equals("RN")){
			return ARTOperationType.REGENONBOARDXML;
		} else if(operation!=null && operation.equals("PR")){
			return ARTOperationType.PASSWORDRESET;
		} else if(operation!=null && operation.equals("FN")){
			return ARTOperationType.FULLNEW;
		} else if(operation!=null && operation.equals("ON")){
			return ARTOperationType.ONBOARDXML;
		} else {
			return null;
		}
	}

	private List<TechnicalRegistrationSummary> constructTechnRegSummaryList (TechnicalRegistration tr, String registrationId){
		List<TechnicalRegistrationSummary> techRegSummaryList = new ArrayList<TechnicalRegistrationSummary>();
		TechnicalRegistrationSummary techRegSummary = new TechnicalRegistrationSummary();
		techRegSummary.setTechnicalRegistrationId(tr.getTechnicalRegistrationId());
		logger.debug("TechnicalRegistration ID From TR Details==========>"+techRegSummary.getTechnicalRegistrationId());
		techRegSummary.setMaterialCode(tr.getTechnicalOrder().getMaterialCode());
		techRegSummary.setMaterialCodeDescription(tr.getTechnicalOrder().getDescription());
		techRegSummary.setOrderId(tr.getTechnicalOrder().getOrderId());
		techRegSummary.setInitialQty(new Long(0));
		techRegSummary.setGroupId(tr.getGroupId());
		techRegSummary.setSolutionElementId(tr.getSolutionElementId());
		techRegSummary.setSid(tr.getSid());
		techRegSummary.setMid(tr.getMid());
		//techRegSummary.setAccessType(tr.getAccessType());
		logger.debug("Access type in constructTechnRegSummaryList:UI:"+tr.getUiAccessType()+"  DB:"+tr.getAccessType());
		techRegSummary.setAccessType(tr.getUiAccessType());
		techRegSummary.setConnectivity(tr.getConnectivity());
		techRegSummary.setAlarmOrigination(tr.getAlarmOrigination());
		techRegSummary.setConnectionDetail(tr.getConnectionDetail());
		if(tr.getStatus() != null){
			techRegSummary.setStatus(tr.getStatus().getStatusDescription());
			techRegSummary.setStatusId(tr.getStatus().getStatusId());
		} else {
			logger.debug("Status is null");
		}
		techRegSummary.setStatus(tr.getStatus().getStatusId());
		techRegSummary.setRegistrationId(registrationId);
		techRegSummary.setSolutionElement(tr.getSolutionElement());
		techRegSummary.setOperationType(tr.getOperationType());
		techRegSummary.setSeId(tr.getSolutionElementId());
		techRegSummary.setStepBStaus(tr.getStepBStatus());

		techRegSummary.setPrimarySalgwSeid(tr.getPrimarySalGWSeid());
		techRegSummary.setSecondarySalgwSeid(tr.getSecondarySalGWSeid());
		techRegSummary.setOutboundPrefix(tr.getOutboundCallingPrefix());

		techRegSummary.setDialInNumber(tr.getDialInNumber());
		techRegSummary.setIpAddress(tr.getIpAddress());

		techRegSummary.setModel(tr.getModel());
		techRegSummary.setRemoteAccess(tr.getRemoteAccess());
		techRegSummary.setTransportAlarm(tr.getTransportAlarm());

		techRegSummary.setFailedSeid(tr.getFailedSeid());
		techRegSummary.setNumberOfSubmit(tr.getNumberOfSubmit());
		techRegSummary.setAuthFileID(tr.getAuthorizationFileId());
		techRegSummary.setUserName(tr.getUsername());
		techRegSummary.setTechReg(true);
		techRegSummary.setExpSolutionElements(tr.getExpSolutionElements());
		techRegSummary.setExplodedSolutionElements(tr.getExplodedSolutionElements());
		techRegSummary.setDeviceLastAlarmReceivedDate(tr.getDeviceLastAlarmReceivedDate());
		techRegSummary.setDeviceStatus(tr.getDeviceStatus());
		techRegSummary.setProcess(tr.isProcess());
		
		//UAT Defect : Alarming Fixed
		techRegSummary.setAlarmId(tr.getAlarmId());
		
		techRegSummaryList.add(techRegSummary);
		return techRegSummaryList;
	}
	
	
	private List<TechnicalRegistrationSummary> constructSLSummaryList (SiteList sl, String registrationId){
		List<TechnicalRegistrationSummary> techRegSummaryList = new ArrayList<TechnicalRegistrationSummary>();
		TechnicalRegistrationSummary techRegSummary = new TechnicalRegistrationSummary();
		techRegSummary.setTechnicalRegistrationId(sl.getSiteId());
		logger.debug("TechnicalRegistration ID From TR Details==========>"+techRegSummary.getTechnicalRegistrationId());
		techRegSummary.setMaterialCode(sl.getMaterialCode());
		techRegSummary.setMaterialCodeDescription(sl.getMaterialCodeDescription());		
		techRegSummary.setGroupId(sl.getGroupId());
		techRegSummary.setSolutionElementId(sl.getSolutionElementId());
		techRegSummary.setSid(sl.getSid());
		techRegSummary.setMid(sl.getMid());		
		techRegSummary.setAccessType(GRTConstants.SAL);		
		techRegSummary.setAlarmOrigination(sl.getAlarmId());		
		if(sl.getStatus() != null){
			techRegSummary.setStatus(sl.getStatus().getStatusDescription());
			techRegSummary.setStatusId(sl.getStatus().getStatusId());
		} 
		techRegSummary.setRegistrationId(registrationId);
		techRegSummary.setSolutionElement(sl.getSolutionElementId());
		techRegSummary.setSeId(sl.getSolutionElementId());
		techRegSummary.setStepBStaus(sl.getStepBStatus());

		techRegSummary.setPrimarySalgwSeid(sl.getPrimarySALGatewaySEID());
		techRegSummary.setSecondarySalgwSeid(sl.getSecondarySALGatewaySEID());

		techRegSummary.setModel(sl.getModel());
		techRegSummary.setRemoteAccess(sl.getRemoteAccess());
		techRegSummary.setTransportAlarm(sl.getTransportAlarm());

		techRegSummary.setFailedSeid(sl.getFailedSeid());
		techRegSummary.setNumberOfSubmit(sl.getNumberOfSubmit());
		techRegSummary.setSiteList(true);
		techRegSummary.setExpSolutionElements(sl.getExpSolutionElements());
		
		techRegSummaryList.add(techRegSummary);		
		return techRegSummaryList;
	}

	private void processChildForDetailView(TechnicalRegistration technicalRegistration, SiteList siteList, RegistrationFormBean form){

		List<ExpandedSolutionElement> explodedSolutionElements = null;
		if(technicalRegistration != null){
			explodedSolutionElements = technicalRegistration.getExpSolutionElements();
		} else if(siteList != null){
			explodedSolutionElements = siteList.getExpSolutionElements();
		}

		if(!explodedSolutionElements.isEmpty()){
			for(ExpandedSolutionElement esElement : explodedSolutionElements){

				try {
					//Service Call
					CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(esElement.getSeCode());
					if(compatibilityMatrix != null ){
						esElement.setModel(compatibilityMatrix.getModel());
						esElement.setRemoteAccessEligible(compatibilityMatrix.getRemoteAccess());
						esElement.setTransportAlarmEligible(compatibilityMatrix.getTransportAlarm());
					} else {
						esElement.setRemoteAccessEligible("N");
						esElement.setTransportAlarmEligible("N");						
					}
					esElement.setRemoteAccessCheckBoxDisabled(true);
					esElement.setAlarmingCheckBoxDisabled(true);

				} 
				catch( Exception e ){
					e.printStackTrace();
				}

			}
		}
	}

	/***
	 * action step b creation
	 * 
	 * Alarming and connectivity
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String loadSalStepBDetails () throws Throwable {
		validateSession();
		
		logger.debug("Entering RegistrationController----------------------loadSalStepBDetails");
		try{
			long c1 = Calendar.getInstance().getTimeInMillis();
			List<String> seIdsList = new ArrayList<String>();
			String registrationType = null;
			if(getRequest().getSession().getAttribute(GRTConstants.REGISTRATION_TYPE) !=null ){
				registrationType = (String) getRequest().getSession().getAttribute(GRTConstants.REGISTRATION_TYPE);
			}
			logger.debug("<-----------------------------------REGISTRATION TYPE IS  ---------------------------->"+ registrationType);
			logger.debug("<-----------------------------------REGISTRATION TYPE IS SALMIGRATION ---------------------------->");

			List<SiteList> salMigrationList = actionForm.getSalMigrationSummaryList();
			List<SiteList> selectedList = new ArrayList<SiteList>();
			
			if(salMigrationList != null  && salMigrationList.size() > 0){


				for(SiteList siteList : salMigrationList) {
					if (siteList!=null
							&& siteList.getSolutionElementId()!=null
							&& siteList.isSelectForAlarmAndConnectivity()) {
						seIdsList.add(siteList.getSolutionElementId());
					}
				}
				
				//Service Call
				SALGatewayIntrospection salGatewayIntrospection =	getTechnicalOnBoardingService().getSalAlarmConnectivityDetails(seIdsList);
				if(salGatewayIntrospection.getReturnCode()!=null
						&& (salGatewayIntrospection.getReturnCode()).toString().equalsIgnoreCase("Failed")){
					actionForm.setReturnCode("Fail");
					actionForm.setMessage("SAL Concentrator Webservice is down");
					//return new Forward("failure", actionForm);
					return "failure";
				}else{
					actionForm.setReturnCode("success");
				}
				
				int siteListcount = 0;
				for(SiteList siteList : salMigrationList) {
					//Default existence to N
					siteList.setExistence(GRTConstants.N);
					if (siteList.isSelectForAlarmAndConnectivity()) {
						if(siteList.getSid()!= null && siteList.getMid()!=null && siteList.getSolutionElementId()!=null){
							List<ExpandedSolutionElement> expSolEleList = getTechnicalOnBoardingService().getAssetsWithSameSIDandMID(siteList.getSid(), siteList.getMid(), siteList.getSolutionElementId());
							List<ExpandedSolutionElement> processedList = new ArrayList<ExpandedSolutionElement>();
							if(!expSolEleList.isEmpty()){
								for(ExpandedSolutionElement ese : expSolEleList){
									if(!siteList.getSolutionElementId().equals(ese.getSeID())){
										ese.setSid(siteList.getSid());
										ese.setMid(siteList.getMid());
										ese.setPrimarySalGWSeid(siteList.getPrimarySALGatewaySEID());
										ese.setSecondarySalGWSeid(siteList.getSecondarySALGatewaySEID());
										processedList.add(ese);
									}
								}
							}
							siteList.setExpSolutionElements(expSolEleList);
						}
						siteList.setIndex(siteListcount++);
						processChildDetails(null, siteList, actionForm);
						if(actionForm.getReturnCode() != null && actionForm.getReturnCode().equals("Fail")){
							//return new Forward("failure", actionForm);	
							return "failure";
						}
						logger.debug("<-------------- Site List values ----------------------------->");
						logger.debug("siteList.getId()"+ siteList.getId());
						logger.debug("siteList.getMaterialCode()"+ siteList.getMaterialCode());
						logger.debug("siteList.getMaterialCodeDescription()"+ siteList.getMaterialCodeDescription());
						logger.debug("siteList.getSeCode()"+ siteList.getSeCode());
						logger.debug("siteList.getAlarmId()"+ siteList.getAlarmId());
						logger.debug("siteList.getRemoteAccess()"+ siteList.getRemoteAccess());
						logger.debug("siteList.getTransportAlarm()"+ siteList.getTransportAlarm());
						logger.debug("siteList.getSolutionElementId()"+ siteList.getSolutionElementId());
						logger.debug("siteList.getModel()"+ siteList.getModel());
						if(siteList!=null && siteList.getSolutionElementId()!=null){
							if(salGatewayIntrospection!=null
									&& salGatewayIntrospection.getManagedDevices()!= null
									&& (salGatewayIntrospection.getManagedDevices().size())>0 ){
								for(int i=0;i < salGatewayIntrospection.getManagedDevices().size();i++){
									if(salGatewayIntrospection.getManagedDevices().get(i).getSeid() != null 
											&& salGatewayIntrospection.getManagedDevices().get(i).getSeid().equals(siteList.getSolutionElementId())){
										if(salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate()!= null
												&& (salGatewayIntrospection.getManagedDevices().get(i).getStatus())!= null){

											String lastAlarmReceivedDate = salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate();
											String status = salGatewayIntrospection.getManagedDevices().get(i).getStatus();

											if(lastAlarmReceivedDate != null) {
												siteList.setDeviceLastAlarmReceivedDate(lastAlarmReceivedDate);
											}
											else{
												siteList.setDeviceLastAlarmReceivedDate("");
											}

											if(status != null) {
												siteList.setDeviceStatus(status);
											}else{
												siteList.setDeviceStatus("Never");
											}

										} else {
											siteList.setDeviceLastAlarmReceivedDate("");
											siteList.setDeviceStatus("Never");
										}
										if(salGatewayIntrospection.getManagedDevices().get(i).getExistence() != null 
												&& StringUtils.isNotEmpty(salGatewayIntrospection.getManagedDevices().get(i).getExistence()) 
												&& salGatewayIntrospection.getManagedDevices().get(i).getExistence().equalsIgnoreCase(GRTConstants.SAL_DEVICE_FOUND)){
											siteList.setExistence(GRTConstants.Y);
										}else{
											siteList.setExistence(GRTConstants.N);
										}
										break;
									}
								}
							}else {
								siteList.setDeviceLastAlarmReceivedDate("");
								siteList.setDeviceStatus("Never");
							}
						}else {
							siteList.setDeviceLastAlarmReceivedDate("");
							siteList.setDeviceStatus("");
						}
						//Begin: Fetch the hyperliked SalGatewaySeid
						//Service Call
						String salGatewaySeid = getTechnicalOnBoardingService().getHyperLinkedSalGatewaySeid(siteList.getPrimarySALGatewaySEID(), siteList.getSecondarySALGatewaySEID(),actionForm.getSoldToId() );
						if(!StringUtils.isEmpty(salGatewaySeid)){
							//salGateWaySeid
							siteList.setSalGateWaySeid(salGatewaySeid);
						}
						//End: Fetch the hyperliked SalGatewaySeid

						//Begin: Populate the checkBox status
						if("Y".equalsIgnoreCase(siteList.getRemoteAccess())){
							siteList.setRemoteAccessCheckBoxDisabled(false);
						} else{
							siteList.setRemoteAccessCheckBoxDisabled(true);
							siteList.setRemoteAccess("N");
						}

						if("Y".equalsIgnoreCase(siteList.getTransportAlarm())){
							siteList.setAlarmingCheckBoxDisabled(false);
						} else{
							siteList.setAlarmingCheckBoxDisabled(true);
							siteList.setTransportAlarm("N");
						}
						
						
						//Disable check boxes according to existence
						if("N".equalsIgnoreCase(siteList.getExistence())){
							siteList.setRemoteAccessCheckBoxDisabled(true);
							siteList.setAlarmingCheckBoxDisabled(true);
						}
						
						//End: Populate the checkBox status

						selectedList.add(siteList);
					}

				}
				actionForm.setSelectedSALMigrationSummaryList(selectedList);
				getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
			}
			logger.debug("Size of the Sal Migration List in Form  ==================================>"+actionForm.getSalMigrationSummaryList() +"  ");
			//} else {

			logger.debug("<-----------------------------------REGISTRATION TYPE IS SALREGISTRATION ---------------------------->");

			List<TechnicalRegistration> salRegistrationList = actionForm.getSalRegistrationSummaryList();
			List<TechnicalRegistration> selectedTobList = new ArrayList<TechnicalRegistration>();
			
			List<String> materialList=new ArrayList<String>();
			
			if(salRegistrationList != null  && salRegistrationList.size() > 0){

				int ij=0;
				for(TechnicalRegistration technicalRegistration : salRegistrationList) {
					if(ij==0 && retestLoadStepb==true){
						technicalRegistration.setSelectForAlarmAndConnectivity(true);
						ij++;
					}
					if (technicalRegistration!=null && technicalRegistration.isSelectForAlarmAndConnectivity()) {
						if(technicalRegistration.getSolutionElementId()!= null 
								&& StringUtils.isNotEmpty(technicalRegistration.getSolutionElementId().trim())){
							seIdsList.add(technicalRegistration.getSolutionElementId());
						} 
					}
				}
				
				//Service Call
				SALGatewayIntrospection salGatewayIntrospection = getTechnicalOnBoardingService().getSalAlarmConnectivityDetails(seIdsList);

				if(salGatewayIntrospection.getReturnCode()!=null
						&& (salGatewayIntrospection.getReturnCode()).toString().equalsIgnoreCase("Failed")){
					actionForm.setReturnCode("Fail");
					actionForm.setMessage("SAL Concentrator Webservice is down");
					return "failure";
				}
				
				int count = 0;	ij=0;					
				for(TechnicalRegistration technicalRegistration : salRegistrationList) {
					//default existence to N
					technicalRegistration.setExistence(GRTConstants.N);
					if(ij==0 && retestLoadStepb==true){
						technicalRegistration.setSelectForAlarmAndConnectivity(true);
						ij++;
					}
					if (technicalRegistration.isSelectForAlarmAndConnectivity()) {
						technicalRegistration.setIndex(count++);
						
						/* GRT 4.0 changes ; fetch exploded solution elements if does not exist starts */
						
						//Loading details from Siebel - fix for missing SAL GW when submitting for step B ATOM & SR creation 
						
						
						
						if(technicalRegistration.getSolutionElementId()!=null){
							
							//Change below query to get SAL GW using only SEID and no SID/MID
							List<ExpandedSolutionElement> expSolEleListParent = getTechnicalOnBoardingService().getSALGWDetails(technicalRegistration.getSolutionElementId());
							
							if(!expSolEleListParent.isEmpty()){
								for(ExpandedSolutionElement element:expSolEleListParent){
									if(technicalRegistration.getSolutionElementId().equalsIgnoreCase(element.getSeID())){
										technicalRegistration.setPrimarySalGWSeid(element.getIpAddress());
									}
								}
							}
						}
						
						if (technicalRegistration.getExpSolutionElements() == null || technicalRegistration.getExpSolutionElements().isEmpty()) {
							if(technicalRegistration.getSid()!= null && technicalRegistration.getMid()!=null && technicalRegistration.getSolutionElementId()!=null){
								
								logger.debug("Loading Child Elements from Siebel..");
								
								List<ExpandedSolutionElement> expSolEleList = getTechnicalOnBoardingService().getAssetsWithSameSIDandMID(technicalRegistration.getSid(), technicalRegistration.getMid(), technicalRegistration.getSolutionElementId());
								Set<ExpandedSolutionElement> expSolEleSet = new HashSet<ExpandedSolutionElement>();
								List<ExpandedSolutionElement> processedList = new ArrayList<ExpandedSolutionElement>();
								if(!expSolEleList.isEmpty()){
									for(ExpandedSolutionElement ese : expSolEleList){
										if(!technicalRegistration.getSolutionElementId().equals(ese.getSeID())){
											ese.setSid(technicalRegistration.getSid());
											ese.setMid(technicalRegistration.getMid());
											ese.setPrimarySalGWSeid(ese.getIpAddress());
											processedList.add(ese);
											expSolEleSet.add(ese);
										}
									}
								}
								technicalRegistration.setExpSolutionElements(expSolEleList);
								technicalRegistration.setExplodedSolutionElements(expSolEleSet);
							}
						}else{
							//Loading details from Siebel - fix for missing SAL GW when submitting for step B ATOM & SR creation
							
							logger.debug("Child Elements exist getting details from Siebel..");
							
							List<ExpandedSolutionElement> expSolEleList = technicalRegistration.getExpSolutionElements();
							if(!expSolEleList.isEmpty()){
								for(ExpandedSolutionElement ese : expSolEleList){
									if(ese.getSeID()!=null){
										
										//Change below query to get SAL GW using only SEID and no SID/MID
										List<ExpandedSolutionElement> expSolEleListChild = getTechnicalOnBoardingService().getSALGWDetails(ese.getSeID());
										
										if(!expSolEleListChild.isEmpty()){
											for(ExpandedSolutionElement element:expSolEleListChild){
												if(ese.getSeID().equalsIgnoreCase(element.getSeID())){
													ese.setPrimarySalGWSeid(element.getIpAddress());
													ese.setIpAddress(element.getIpAddress());
												}
											}
										}
									}
								}
								technicalRegistration.setExpSolutionElements(expSolEleList);
							}
						}
						/* GRT 4.0 changes ends */
						
						processChildDetails(technicalRegistration, null, actionForm);
						if( actionForm.getReturnCode()!=null && actionForm.getReturnCode().equals("Fail")){
							return "failure";
						}
						logger.debug("<-------------- SAL Registration values ----------------------------->");
						logger.debug("technicalRegistration.getTechnicalOrder().getSid()"+ technicalRegistration.getTechnicalOrder().getSid());
						logger.debug("technicalRegistration.getTechnicalOrder().getMaterialCode()"+ technicalRegistration.getTechnicalOrder().getMaterialCode());
						logger.debug("technicalRegistration.getTechnicalOrder().getDescription()"+ technicalRegistration.getTechnicalOrder().getDescription());
						logger.debug("technicalRegistration.getTechnicalOrder().getSeCode()"+ technicalRegistration.getTechnicalOrder().getSolutionElementCode());
						logger.debug("technicalRegistration.getAlarmId()"+ technicalRegistration.getAlarmId());
						logger.debug("technicalRegistration.getRemoteAccess()"+ technicalRegistration.getRemoteAccess());
						logger.debug("technicalRegistration.getTransportAlarm()"+ technicalRegistration.getTransportAlarm());
						logger.debug("technicalRegistration.getSolutionElementId()"+ technicalRegistration.getSolutionElementId());
						logger.debug("technicalRegistration.getModel()"+ technicalRegistration.getModel());
						
						materialList.add(technicalRegistration.getTechnicalOrder().getMaterialCode());
						
						if(technicalRegistration!=null
								&& technicalRegistration.getSolutionElementId()!=null){
							
							if(salGatewayIntrospection!=null
									&& salGatewayIntrospection.getManagedDevices()!= null
									&& (salGatewayIntrospection.getManagedDevices().size())>0 ){
								for(int i=0;i < salGatewayIntrospection.getManagedDevices().size();i++){
									if(salGatewayIntrospection.getManagedDevices().get(i).getSeid() != null 
											&& salGatewayIntrospection.getManagedDevices().get(i).getSeid().equals(technicalRegistration.getSolutionElementId())){
										if(salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate()!= null
												&& (salGatewayIntrospection.getManagedDevices().get(i).getStatus())!= null){

											String lastAlarmReceivedDate = salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate();

											String status = salGatewayIntrospection.getManagedDevices().get(i).getStatus();

											if(lastAlarmReceivedDate != null) {
												technicalRegistration.setDeviceLastAlarmReceivedDate(lastAlarmReceivedDate);
											}
											else{
												technicalRegistration.setDeviceLastAlarmReceivedDate("");
											}

											if(status != null) {
												technicalRegistration.setDeviceStatus(status);
											}
											else{
												technicalRegistration.setDeviceLastAlarmReceivedDate("");
											}

										} else {
											technicalRegistration.setDeviceLastAlarmReceivedDate("");
											technicalRegistration.setDeviceStatus("Never");
										}
										if(salGatewayIntrospection.getManagedDevices().get(i).getExistence() != null 
												&& StringUtils.isNotEmpty(salGatewayIntrospection.getManagedDevices().get(i).getExistence()) 
												&& salGatewayIntrospection.getManagedDevices().get(i).getExistence().equalsIgnoreCase(GRTConstants.SAL_DEVICE_FOUND)){
											technicalRegistration.setExistence(GRTConstants.Y);
										}else{
											technicalRegistration.setExistence(GRTConstants.N);
										}
										break;
									}									
								}
							}else {
								technicalRegistration.setDeviceLastAlarmReceivedDate("");
								technicalRegistration.setDeviceStatus("Never");
							}
						}else {
							technicalRegistration.setDeviceLastAlarmReceivedDate("");
							technicalRegistration.setDeviceStatus("");
						}
						//Begin: Fetch the hyperliked SalGatewaySeid
						//Service Call
						String salGatewaySeid = getTechnicalOnBoardingService().getHyperLinkedSalGatewaySeid(technicalRegistration.getPrimarySalGWSeid(), technicalRegistration.getSecondarySalGWSeid(),actionForm.getSoldToId() );
						if(!StringUtils.isEmpty(salGatewaySeid)){
							//salGateWaySeid
							technicalRegistration.setSalGateWaySeid(salGatewaySeid);
						}
						//End: Fetch the hyperliked SalGatewaySeid

						//Begin: Populate the checkBox status
						if("N".equalsIgnoreCase(technicalRegistration.getRemoteAccess())){
							technicalRegistration.setRemoteAccessCheckBoxDisabled(true);
						} else{
							technicalRegistration.setRemoteAccessCheckBoxDisabled(false);
						}

						if("N".equalsIgnoreCase(technicalRegistration.getTransportAlarm())){
							technicalRegistration.setAlarmingCheckBoxDisabled(true);
						} else{
							technicalRegistration.setAlarmingCheckBoxDisabled(false);
						}
						//End: Populate the checkBox status

						selectedTobList.add(technicalRegistration);
					}

				}
				
				retestLoadStepb=false;
				List<String> alarmingList=getBaseRegistrationService().getEntitledForAlarming(materialList, actionForm.getSoldToId());
				
				for(TechnicalRegistration tr: selectedTobList)
				{
					tr.setEntitledForAlarming("No");
					tr.setAlarmingCheckBoxDisabled(true);
					for(ExpandedSolutionElement s: tr.getExpSolutionElements()){
						s.setAlarmingCheckBoxDisabled(true);
					}
				
				}
				
				for(TechnicalRegistration tr: selectedTobList)
				{
					for(String s:alarmingList)
					{
						if(tr.getTechnicalOrder().getMaterialCode().equalsIgnoreCase(s))
						{
							tr.setEntitledForAlarming("Yes");
							if(tr.getTransportAlarm().equals("Y"))
								tr.setAlarmingCheckBoxDisabled(false);
							
							for(ExpandedSolutionElement sol: tr.getExpSolutionElements()){
								if(sol.getTransportAlarmEligible().equals("Y"))
									sol.setAlarmingCheckBoxDisabled(false);
								}
						}
					}
					
				}
				
				//Disable check boxes according to existence
				for(TechnicalRegistration tr: selectedTobList){
					if("N".equalsIgnoreCase(tr.getExistence())){
						tr.setRemoteAccessCheckBoxDisabled(true);
						tr.setAlarmingCheckBoxDisabled(true);
					}
					for(ExpandedSolutionElement sol: tr.getExpSolutionElements()){
						if("N".equalsIgnoreCase(sol.getExistence())){
							sol.setRemoteAccessCheckBoxDisabled(true);
							sol.setAlarmingCheckBoxDisabled(true);
						}
					}
				}
				
				actionForm.setSelectedSALRegSummaryList(selectedTobList);
				logger.debug("Size of the Sal Registration List in Form  ==================================>"+actionForm.getSalRegistrationSummaryList() +"  ");
				getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
				
			}
			logger.debug("Exiting RegistrationController :  loadSalStepBDetails");

			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Timer to Load Sal StepBDetails >> Time in milliseconds"+ (c2-c1));
			if(!selectedTobList.isEmpty()  || !selectedList.isEmpty()){
				return "success";
			}else{
				return "failure";
			}

		} catch(Throwable throwable) {
			logger.error("", throwable);
			throwable.printStackTrace();
			throw throwable;
		}


	}


	private void processChildDetails(TechnicalRegistration technicalRegistration, SiteList siteList, RegistrationFormBean form){
		int parentIndex = -1;
		List<ExpandedSolutionElement> explodedSolutionElements = null;
		List<String> childSeIdsList = new ArrayList<String>();
		if(technicalRegistration != null){
			explodedSolutionElements = technicalRegistration.getExpSolutionElements();
			parentIndex = technicalRegistration.getIndex();
		} else if(siteList != null){
			explodedSolutionElements = siteList.getExpSolutionElements();
			parentIndex = siteList.getIndex();
		}
		if(!explodedSolutionElements.isEmpty()){
			for(ExpandedSolutionElement esElement : explodedSolutionElements){
				esElement.setParentIndex(parentIndex);
				try {
					//Service Call
					CompatibilityMatrix compatibilityMatrix = getTechnicalOnBoardingService().getCompatibilityMatrix(esElement.getSeCode());
					if(compatibilityMatrix != null ){
						esElement.setModel(compatibilityMatrix.getModel());
						esElement.setRemoteAccessEligible(compatibilityMatrix.getRemoteAccess());
						esElement.setTransportAlarmEligible(compatibilityMatrix.getTransportAlarm());
					} else {
						esElement.setRemoteAccessEligible("N");
						esElement.setTransportAlarmEligible("N");
					}

					if("N".equalsIgnoreCase(esElement.getRemoteAccessEligible()) || "N/A".equalsIgnoreCase(esElement.getRemoteAccessEligible())){
						esElement.setRemoteAccessCheckBoxDisabled(true);
					} else{
						esElement.setRemoteAccessCheckBoxDisabled(false);
					}
					if("N".equalsIgnoreCase(esElement.getTransportAlarmEligible()) || "N/A".equalsIgnoreCase(esElement.getTransportAlarmEligible())){
						esElement.setAlarmingCheckBoxDisabled(true);
					} else{
						esElement.setAlarmingCheckBoxDisabled(false);
					}

				} 
				catch (Exception e) {
					
					e.printStackTrace();
				}

				if(esElement!=null && esElement.getSeID()!=null){
					childSeIdsList.add(esElement.getSeID());
				} else {
					esElement.setDeviceLastAlarmReceivedDate("");
					esElement.setDeviceStatus("");
				}
			}
			//SAL Concentrator call..
			SALGatewayIntrospection salGatewayIntrospection = null;
			try {
				if(childSeIdsList != null && childSeIdsList.size() > 0){
					salGatewayIntrospection = getTechnicalOnBoardingService().getSalAlarmConnectivityDetails(childSeIdsList);
				}				
			}catch (Exception e) {
				e.printStackTrace();
			}catch (Throwable e) {
				e.printStackTrace();
			}

			if(salGatewayIntrospection != null && salGatewayIntrospection.getReturnCode()!=null
					&& (salGatewayIntrospection.getReturnCode()).toString().equalsIgnoreCase("Failed")){
				form.setReturnCode("Fail");
				form.setMessage("SAL Concentrator Webservice is down");
				return;
			}
			
			for(ExpandedSolutionElement esElement : explodedSolutionElements){
				//default existence to N
				esElement.setExistence(GRTConstants.N);
				if(salGatewayIntrospection!=null
						&& salGatewayIntrospection.getManagedDevices()!= null
						&& (salGatewayIntrospection.getManagedDevices().size())>0 ){
					for(int i=0;i < salGatewayIntrospection.getManagedDevices().size();i++){
						if(salGatewayIntrospection.getManagedDevices().get(i).getSeid() != null && esElement.getSeID() != null 
								&& salGatewayIntrospection.getManagedDevices().get(i).getSeid().equals(esElement.getSeID())){
							
							if(salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate()!= null
									&& (salGatewayIntrospection.getManagedDevices().get(i).getStatus())!= null){

								String lastAlarmReceivedDate = salGatewayIntrospection.getManagedDevices().get(i).getLastAlarmReceivedDate();

								String status = salGatewayIntrospection.getManagedDevices().get(i).getStatus();

								if(lastAlarmReceivedDate != null) {
									esElement.setDeviceLastAlarmReceivedDate(lastAlarmReceivedDate);
								}
								else{
									esElement.setDeviceLastAlarmReceivedDate("");
								}

								if(status != null) {
									esElement.setDeviceStatus(status);
								}
								else{
									esElement.setDeviceStatus("");
								}

							} else {
								esElement.setDeviceLastAlarmReceivedDate("");
								esElement.setDeviceStatus("Never");
							}
							if(salGatewayIntrospection.getManagedDevices().get(i).getExistence() != null 
									&& StringUtils.isNotEmpty(salGatewayIntrospection.getManagedDevices().get(i).getExistence()) 
									&& salGatewayIntrospection.getManagedDevices().get(i).getExistence().equalsIgnoreCase(GRTConstants.SAL_DEVICE_FOUND)){
								esElement.setExistence(GRTConstants.Y);
							}else{
								esElement.setExistence(GRTConstants.N);
							}
							break;
						}
					}
				} else {
					esElement.setDeviceLastAlarmReceivedDate("");
					esElement.setDeviceStatus("Never");
				}
				
				//Disable check boxes according to existence
				if("N".equalsIgnoreCase(esElement.getExistence())){
					esElement.setRemoteAccessCheckBoxDisabled(true);
					esElement.setAlarmingCheckBoxDisabled(true);
				}
				
			}
		}
	}

	public String backFromTRDetails () throws Exception {
		validateSession();
		getRequest().getSession().removeAttribute(GRTConstants.REGISTRATION_TYPE);
		getRequest().getSession().removeAttribute(GRTConstants.TR_DETAILS_ACTIVE);
		getRequest().getSession().removeAttribute(GRTConstants.FROM_TR_OB_DETAIL);
		actionForm.setFromTrObDetail( null );
		//Clear retest saved data
		actionForm.setRetestTRList( null );
		return registrationList();
	}
	public String addRegistrableProductsReadyToProcess () throws Exception {
		logger.debug("+++ addRegistrableProductsReadyToProcess. +++");
		validateSession();
		String salSeIdPrimarySecondary = "";
		try {
			String afidVal = actionForm.getTechnicalRegistrationSummary().getAuthFileID();
			String authFileIDFlagOrigTRED = actionForm.getAuthFileIDFlagOrigTRED();
			if (StringUtils.isNotEmpty(authFileIDFlagOrigTRED) && StringUtils.isEmpty(afidVal)) {
				actionForm.setSidMidStatus(grtConfig.getInvalidAfid());
				actionForm.setSidMidErrorFlag(GRTConstants.BLOCK);
				actionForm.setSidMidValidatedFlag(GRTConstants.NONE);
				return "failure";
			}
			if ((actionForm.getAuthFileIDFlag().equalsIgnoreCase(GRTConstants.BLOCK) && !StringUtils.isEmpty(afidVal)) 
					&& !afidVal.equals(authFileIDFlagOrigTRED)) {
				String afIdResponse = null;
				afIdResponse = callAFIDService(actionForm, afidVal);
				logger.debug("AFID Response :::" + afIdResponse);
				if (afIdResponse != null) {
					return "failure";
				}
			}
			List<TechnicalRegistrationSummary> regSumList = actionForm.getRegistrationSummaryListRegistrable();
			if(regSumList == null){
				regSumList = new ArrayList<TechnicalRegistrationSummary>();
			}
			TechnicalRegistrationSummary technicalRegistrationSummary = actionForm.getTechnicalRegistrationSummary();
			if(technicalRegistrationSummary == null){
				logger.debug("TechnicalRegistrationSummary not matched...");
				technicalRegistrationSummary = new TechnicalRegistrationSummary();
			}
			if(technicalRegistrationSummary!=null){
				technicalRegistrationSummary.setOperationType("DU");
				technicalRegistrationSummary.setProductTemplate("Secure Access Link");
				technicalRegistrationSummary.setAction(GRTConstants.UPDATE);
				technicalRegistrationSummary.setAccessTypes(technicalRegistrationSummary.getAccessType());
				technicalRegistrationSummary.setEquipmentNumber(technicalRegistrationSummary.getEquipmentNumber());
				technicalRegistrationSummary.setPopUpHiddenValue("Update");
				if(technicalRegistrationSummary.getAccessType().equalsIgnoreCase(GRTConstants.SAL)){
					salSeIdPrimarySecondary = actionForm.getPrimary();
					technicalRegistrationSummary.setPrimarySalgwSeid(actionForm.getPrimary());
					technicalRegistrationSummary.setSecondarySalgwSeid(actionForm.getSecondary());
					if(salSeIdPrimarySecondary != null && salSeIdPrimarySecondary.length() > 0){
						if(actionForm.getSecondary() != null && actionForm.getSecondary().length() > 0){
							salSeIdPrimarySecondary = salSeIdPrimarySecondary + "+" + actionForm.getSecondary();
						}
					} else {
						salSeIdPrimarySecondary = actionForm.getSecondary();
					}
				}
				technicalRegistrationSummary.setSalSeIdPrimarySecondary(salSeIdPrimarySecondary);
				technicalRegistrationSummary.setProcess(true);
				// Assign TR Config data
				if(technicalRegistrationSummary.getTrConfig() != null){
					technicalRegistrationSummary.setProductTemplate(technicalRegistrationSummary.getTrConfig().getTemplate());
					technicalRegistrationSummary.setSolutionElement(technicalRegistrationSummary.getTrConfig().getSeCode());
				}
				// Disabling the update button of the LineItem in Technically Registered list of TOB Dashboard
				//GRT 4.0 Change : Disable ReTest button also for the Line Item if Update is clicked
				List<TechnicalRegistrationSummary> techSummaryList = actionForm.getRegistrationSummaryListTRUpdate();
				for(TechnicalRegistrationSummary tecRegSummary: techSummaryList){
					if(tecRegSummary != null && tecRegSummary.getSeId()!= null && tecRegSummary.getMaterialCode()!= null){
						if(tecRegSummary.getSeId().equals(technicalRegistrationSummary.getSeId())
								&& tecRegSummary.getMaterialCode().equalsIgnoreCase(technicalRegistrationSummary.getMaterialCode())){
							tecRegSummary.setDisableUpdateFlag(true);
							tecRegSummary.setDisableRetestFlag(true);
							tecRegSummary.setUpdateButtonTitle(grtConfig.getUpdateRecordTitle());
							break;
						}
					}
				}
			}
			if(technicalRegistrationSummary.isAddFlag()){
				regSumList.add(technicalRegistrationSummary);
			} else if(actionForm.getIndex() != null) {
				regSumList.remove(Integer.parseInt(actionForm.getIndex()));
				regSumList.add(Integer.parseInt(actionForm.getIndex()), technicalRegistrationSummary);
			}
			actionForm.setRegistrationSummaryListRegistrable(regSumList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in addRegistrableProductsReadyToProcess:"+e.getMessage());
		}
		logger.debug("+++ Exiting addRegistrableProductsReadyToProcess. +++");
		return technicalRegistrationDashboard();
	}

	public String addToList()  throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Entering into addToList()");
		String forward = "salgw";
		CSSPortalUser userProfile = getUserFromSession();
		String userId = userProfile.getUserId();
		String userType = userProfile.getUserType();
		String soldToFromUser = actionForm.getSoldTo();
		String seidFromUser = actionForm.getSalSeId();
		List<String> errorSoldToList = new ArrayList<String>();
		logger.debug("soldTo============================@================================>"+soldToFromUser);
		logger.debug("salSeId===========================@=================================>"+ seidFromUser);
		List<SALGateway> salGatewayMigrationList = actionForm.getSalGatewayMigrationList(); // This is existing list
		int count = 0;
		int errorCount = 0;
		int duplicateCOunt = 0;
	
		if(StringUtils.isNotEmpty(seidFromUser) &&  StringUtils.isEmpty(soldToFromUser)){
			List<SALGateway> salGatewayMigrationListFromDb = getTechnicalOnBoardingService().getSALGateways(soldToFromUser, seidFromUser, GRTConstants.SAL);
			if(salGatewayMigrationListFromDb != null && salGatewayMigrationListFromDb.size() > 0) {
				//Check If logged-In user is BP/Customer: If yes check against CXP+SOIR for user access to returned soldTo by the API.
				for (SALGateway sALGatewayFromDb : salGatewayMigrationListFromDb) {
					String soldToFromDb = sALGatewayFromDb.getSoldTo();
					boolean hasAccess = false;
					if ("B".equals(userType)
							|| "C".equals(userType)) {
						hasAccess = this.getTechnicalOnBoardingService().isSoldToValidForCurrentUser(soldToFromDb, userProfile.getUserId(), userProfile.getBpLinkId());
						//hasAccess = userHasAccessToSoldTo(soldToFromDb, GRTConstants.SOIR);
					}else {
						hasAccess = true;
					}
					if (hasAccess) {
						//errorMessage = GRTConstants.SAL_GATEWAY_EMPTY_MESG;
						boolean isDuplicate = false;
						for (SALGateway sALGateway : salGatewayMigrationList) {
							if ( sALGatewayFromDb.getSeid().equals(sALGateway.getSeid()) ){
								isDuplicate = true;
							}
						}
						/// NEW CONDTION
						if (!isDuplicate ){
							salGatewayMigrationList.add(sALGatewayFromDb);
							count = count+1;
						} else {
							duplicateCOunt = duplicateCOunt+1;
						}
					} else {
						if(StringUtils.isNotEmpty(soldToFromDb) ){
							errorSoldToList.add(soldToFromDb);
							errorCount = errorCount+1;
						}
					}

				}


			}

		} else if (StringUtils.isNotEmpty(soldToFromUser) ){
			if ("B".equals(userType)
					|| "C".equals(userType)) {
				boolean hasAccess = this.getTechnicalOnBoardingService().isSoldToValidForCurrentUser(soldToFromUser, userProfile.getUserId(), userProfile.getBpLinkId());
				//boolean hasAccess = userHasAccessToSoldTo(soldToFromUser, GRTConstants.SOIR);
				if(hasAccess){
					//Call API
					List<SALGateway> salGatewayMigrationListFromDb = getTechnicalOnBoardingService().getSALGateways(soldToFromUser, seidFromUser, GRTConstants.SAL);
		
					if(salGatewayMigrationListFromDb != null && salGatewayMigrationListFromDb.size() > 0) {
						for (SALGateway sALGatewayFromDb : salGatewayMigrationListFromDb) {
							boolean isDuplicate = false;
							for (SALGateway sALGateway : salGatewayMigrationList) {
								if ( sALGatewayFromDb.getSeid().equals(sALGateway.getSeid()) ){
									isDuplicate = true;
								}
							}
							/// NEW CONDTION
							if (!isDuplicate ){
								salGatewayMigrationList.add(sALGatewayFromDb);
								count = count+1;
							} else {
								duplicateCOunt = duplicateCOunt+1;
							}
						}
					}
			
				}else {
					errorSoldToList.add(soldToFromUser);
					errorCount = errorCount+1;
				}
			} else {
				List<SALGateway> salGatewayMigrationListFromDb = getTechnicalOnBoardingService().getSALGateways(soldToFromUser, seidFromUser, GRTConstants.SAL);
				if(salGatewayMigrationListFromDb != null && salGatewayMigrationListFromDb.size() > 0) {
					for (SALGateway sALGatewayFromDb : salGatewayMigrationListFromDb) {
						boolean isDuplicate = false;
						for (SALGateway sALGateway : salGatewayMigrationList) {
							if ( sALGatewayFromDb.getSeid().equals(sALGateway.getSeid()) ){
								isDuplicate = true;
							}
						}
						/// NEW CONDTION
						if (!isDuplicate ){
							salGatewayMigrationList.add(sALGatewayFromDb);
							count = count+1;
						} else {
							duplicateCOunt = duplicateCOunt+1;
						}
					}
				}
			}
		}
		if (count == 0){
			actionForm.setCount(grtConfig.getSalNoGateWayOnSoldToErrMsg());
		} else {
			actionForm.setCount(String.valueOf(count));
		}
		//Begin Display Logic
		StringBuilder sb = new StringBuilder();
		if(errorCount > 0){
			int size = errorSoldToList.size();
			for(int i=0; i<size; i++){
				sb.append(errorSoldToList.get(i));
				if(i < (size-1)){
					sb.append(",");
				}
			}

			String errorMessage = grtConfig.getEwiMessageCodeMap().get("salNoAccessToSolToErrMsg") + "###" + grtConfig.getSalNoAccessToSolToErrMsg() + sb.toString();
			
			String duplicateMesg = "";
			if(duplicateCOunt > 0){
				duplicateMesg = grtConfig.getDuplicateSalMsg1();
			}
			errorMessage = errorMessage + duplicateMesg;
			actionForm.setErrorMessage(errorMessage);
		} else {
			actionForm.setErrorMessage("");
			String duplicateMesg1 = "";
			if(duplicateCOunt > 0){
				duplicateMesg1 = grtConfig.getEwiMessageCodeMap().get("duplicateSalMsg2") + "###" + grtConfig.getDuplicateSalMsg2();
			}
			String errorMessage = duplicateMesg1;
			actionForm.setErrorMessage(errorMessage);
		}
		//End Display Logic
		if(actionForm.getScreenName() != null && actionForm.getScreenName().equalsIgnoreCase("TOBCONFIG")){
			forward = "tobConfig";
		} else if(actionForm.getScreenName() != null && actionForm.getScreenName().equalsIgnoreCase("TOBCONFIGUPDATE")){
			forward = "tobConfigUpdate";
		}

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Timer to add the SAL Gateways to Existing SAL Gateways >> Time in milliseconds"+ (c2-c1));
		logger.debug("Exiting from addToList()");
		List<SALGateway> t = actionForm.getSalGatewayMigrationList(); // This is existing list
		
		//GRT 4.0 Changes
		TechnicalRegistrationSummary technicalRegistrationSummary = actionForm.getTechnicalRegistrationSummary();
				
		if(technicalRegistrationSummary!=null && technicalRegistrationSummary.getPrimarySalgwSeid() != null){
			getRequest().getSession().setAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID, technicalRegistrationSummary.getPrimarySalgwSeid());
		}
		if(technicalRegistrationSummary!=null && technicalRegistrationSummary.getSecondarySalgwSeid() != null){
			getRequest().getSession().setAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID, technicalRegistrationSummary.getSecondarySalgwSeid());
		}
				
		return forward;
	}


	/****
	 * Selected registration add to process
	 * after configuration action
	 * @return
	 * @throws Exception
	 */
	public String updateRegistrableProductsReadyToProcess () throws Exception {
		logger.debug("Enter TechnicalOnboardingAction.updateRegistrableProductsReadyToProcess()");
		validateSession();
		try{
			SiteRegistration siteRegistration   = (SiteRegistration) (getRequest().getSession().getAttribute(GRTConstants.SITE_REGISTRATION));
			TechnicalRegistrationSummary techRegSummaryObject = actionForm.getTechnicalRegistrationSummary();
			TechnicalRegistration techReg = this.constructTechnicalRegistrationFromTechSum(techRegSummaryObject, actionForm);
			if (techReg == null) {
				return "failure";
			}
			techReg.getTechnicalOrder().setSiteRegistration(siteRegistration);
			List<TechnicalRegistration> trList = new ArrayList<TechnicalRegistration>();
			trList.add(techReg);
			//DONE Service Call
			getTechnicalOnBoardingService().saveTechnicalRegistration(techReg);

			getTechnicalOnBoardingService().doTechnicalRegistration(siteRegistration, trList, null, ARTOperationType.DATABASEUPDATE);
			logger.debug("Exit TechnicalOnboardingAction.updateRegistrableProductsReadyToProcess()");
			return getTechnicalOrderDetails();
		} catch (Exception ex) {
			logger.error("Exception when submitted TR >> DU operation to ART");
			ex.printStackTrace();
			return "failure";
		}
	}

	private TechnicalRegistration constructTechnicalRegistrationFromTechSum(TechnicalRegistrationSummary trSummary, RegistrationFormBean form ){

		TechnicalRegistration techReg = new TechnicalRegistration();
		//TechnicalRegistration techReg = null;
		try {
			//Defect 541 : Check null condition
			if( trSummary.getTechnicalRegistrationId()!=null ){
				//Done Service Call
				techReg = getTechnicalOnBoardingService().fetchTechnicalRegistrationByID(trSummary.getTechnicalRegistrationId());
			}
		} catch (Exception ex){
			//ex.printStackTrace();
			techReg = new TechnicalRegistration();
		}
		String afidVal = form.getTechnicalRegistrationSummary().getAuthFileID();
		if (form.getAuthFileIDFlag()!=null && form.getAuthFileIDFlag().equalsIgnoreCase(GRTConstants.BLOCK) && !StringUtils.isEmpty(afidVal)
				&& !afidVal.equals(techReg.getAuthorizationFileId())) {
			String afIdResponse = null;
			try {
				afIdResponse = callAFIDService(form, afidVal);
				logger.debug("AFID Response :::" + afIdResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (afIdResponse != null) {
				return null;
			}
		}

		TechnicalOrder to = new TechnicalOrder();
		to.setMaterialCode(trSummary.getMaterialCode());
		to.setDescription(trSummary.getMaterialCodeDescription());
		to.setOrderId(trSummary.getOrderId());
		techReg.setTechnicalOrder(to);
		techReg.setTechnicalRegistrationId(trSummary.getTechnicalRegistrationId());

		techReg.setGroupId(trSummary.getGroupId());
		techReg.setAlarmId(trSummary.getAlarmId());
		techReg.setCmMainSeid(trSummary.getSolutionElementId());
		techReg.setSid(trSummary.getSid());
		techReg.setMid(trSummary.getMid());
		logger.debug(">>>>>>>>>>>>>..."+trSummary.getSid());
    	logger.debug(">>>>>>>>>>>>>..."+trSummary.getMid());
		if(trSummary.getAccessType() != null){
			if(trSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_RSAIP)){
				techReg.setAccessType(AccessTypeEnum.IP.getDbAccessType());
			} else if (trSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_SSLVPN)){
				techReg.setAccessType(AccessTypeEnum.IPO.getDbAccessType());
			} else {
				techReg.setAccessType(trSummary.getAccessType());
			}
		} else {
			logger.warn("Access Type is null");
		}
		techReg.setConnectivity(trSummary.getConnectivity());
		// Calling the setTRDerivables for saving convertable values in DB.
		try {
			setTRDerivables(trSummary);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		techReg.setConnectionDetail(trSummary.getConnectionDetail());
		techReg.setNumberOfSubmit(trSummary.getNumberOfSubmit());
		if (form.getTrConfig() != null) {
			techReg.setProductCode(form.getTrConfig().getProductType());
		}
		Status status = new Status();
		status.setStatusId(trSummary.getStatusId());
		status.setStatusDescription(trSummary.getStatus());
		techReg.setStatus(status);
		techReg.setSolutionElement(trSummary.getSolutionElement());
		techReg.setStepBStatus(trSummary.getStepBStaus());
		techReg.setOperationType(trSummary.getOperationType());
		if(trSummary.getOperationType()==null)
		{
			if(form.getNoConnectivityAccType()!=null)
			{
				techReg.setOperationType(ARTOperationType.DATABASENEW.getOperationKey());
			}
		}

		techReg.setPrimarySalGWSeid(trSummary.getPrimarySalgwSeid());
		techReg.setSecondarySalGWSeid(trSummary.getSecondarySalgwSeid());
		techReg.setOutboundCallingPrefix(trSummary.getOutboundPrefix());

		techReg.setDialInNumber(trSummary.getDialInNumber());
		techReg.setIpAddress(trSummary.getIpAddress());
		techReg.setRandomPassword(trSummary.getRandomPassworddb());
		techReg.setCheckSesEdge(trSummary.getCheckIfSESEdgeDb());
		techReg.setCheckReleaseHigher(trSummary.getSampBoardUpgradedDb());

		techReg.setSolutionElementId(trSummary.getSolutionElementId());
		techReg.setModel(trSummary.getModel());
		techReg.setRemoteAccess(trSummary.getRemoteAccess());
		techReg.setTransportAlarm(trSummary.getTransportAlarm());
		techReg.setFailedSeid(trSummary.getFailedSeid());
		techReg.setAuthorizationFileId(trSummary.getAuthFileID());
		techReg.setUsername(trSummary.getUserName());
		techReg.setPassword(trSummary.getPassword());
		techReg.setExpSolutionElements(trSummary.getExpSolutionElements());
		techReg.setExplodedSolutionElements(trSummary.getExplodedSolutionElements());
		techReg.setEquipmentNumber(trSummary.getEquipmentNumber());
		techReg.setDeviceLastAlarmReceivedDate(trSummary.getDeviceLastAlarmReceivedDate());
		techReg.setDeviceStatus(trSummary.getDeviceStatus());
		techReg.setSelectForRemoteAccess(trSummary.isSelectForRemoteAccess());
		techReg.setSelectForAlarming(trSummary.isSelectForAlarming());
		techReg.setCheckBoxDisabled(trSummary.isDisableRetestFlag());
		
		return techReg;
	}
	
	private SiteList constructSiteListTechSum(TechnicalRegistrationSummary technicalRegistrationSummary, SiteRegistration siteRegistration) {
		SiteList sl = new SiteList();
		
		sl.setSiteRegistration(siteRegistration);
		sl.setSid(technicalRegistrationSummary.getSid());
		sl.setMid(technicalRegistrationSummary.getMid());
		sl.setSelectForAlarmAndConnectivity(true);
		sl.setSolutionElementId(technicalRegistrationSummary.getSolutionElement());
		sl.setPrimarySALGatewaySEID(technicalRegistrationSummary.getPrimarySalgwSeid());
		sl.setSecondarySALGatewaySEID(technicalRegistrationSummary.getSecondarySalgwSeid());
		sl.setModel(technicalRegistrationSummary.getModel());
		sl.setFailedSeid(technicalRegistrationSummary.getFailedSeid());
		sl.setEquipmentNumber(technicalRegistrationSummary.getEquipmentNumber());
		sl.setGroupId(technicalRegistrationSummary.getGroupId());
		sl.setSelected(true);
		sl.setSoldToId(siteRegistration.getSoldToId());
		sl.setSelectForAlarmAndConnectivity(true);
		sl.setSelectForAlarming(true);
		sl.setSelectForRemoteAccess(true);
		sl.setMaterialCode(technicalRegistrationSummary.getMaterialCode());
		Status status = new Status();
		status.setStatusId(technicalRegistrationSummary.getStatusId());
		status.setStatusDescription(technicalRegistrationSummary.getStatus());
		sl.setStatus(status);
		sl.setExpSolutionElements(technicalRegistrationSummary.getExpSolutionElements());
		//GRT 4.0 UAT : Alarming fix
		sl.setAlarmId(technicalRegistrationSummary.getAlarmId());
		return sl;
	}

	/****
	 * Validate CM main SEID 
	 * Validate button click action
	 * Sold to and Seid Validate
	 * @return
	 * @throws Exception
	 */
	 public String validateCMMain() throws Exception {
	       logger.debug("Entering validateCMMain...");
	       validateSession();
	       String cmMainSeid = actionForm.getSolutionElementId();
	       actionForm.setMessage("");
	       actionForm.setReadOnly(false);
	       actionForm.setMainSeIdFlag("none");
	       actionForm.setCmMainErrorFlag("block");
	       actionForm.setSidMidValidatedFlag("none");
	       actionForm.setSidMidErrorFlag("none");
	       actionForm.setLssOrESSLowerVersionFlag("");
	  	   actionForm.setUpgradedMainCMuseRFASIDFlag("");
	       logger.debug("Enter main SEID ::: " + cmMainSeid);
	       if(StringUtils.isNotEmpty(cmMainSeid)) {
	             CMMain cmMain = getTechnicalOnBoardingService().validateCMMain(cmMainSeid);
	             logger.debug("Response from CmMain :::" + cmMain);
	             if (cmMain != null && cmMain.getSoldToId() != null) {
	                   logger.debug("CMMAIN SolDTO :::" + cmMain.getSoldToId());
	                   CSSPortalUser cssPortalUser = getUserFromSession();
	                   String userType  = cssPortalUser.getUserType();
	                   boolean hasAccess = false;
	                        if ("B".equals(userType) || "C".equals(userType)) {
	                        	hasAccess = this.getTechnicalOnBoardingService().isSoldToValidForCurrentUser(cmMain.getSoldToId(), cssPortalUser.getUserId(), cssPortalUser.getBpLinkId());
	                        } else {
	                              hasAccess = true;
	                        }
	                   if(hasAccess){ 
	                	   logger.debug("GRTConstant:"+GRTConstants.SECODE.VCM);
	                	   logger.debug("TRConfig SE Code:"+actionForm.getTrConfig().getSeCode());
	                	   logger.debug("CM Main SE Code:"+cmMain.getSeCode());
	                	   logger.debug("CM Main SID:"+cmMain.getSid());
	                	   	if(GRTConstants.SECODE.VCM.equalsIgnoreCase(actionForm.getTrConfig().getSeCode())) {
	                	   		
	                	   		if(GRTConstants.SECODE.VCM.equalsIgnoreCase(cmMain.getSeCode()) 
	                	   				&& cmMain.getSid() != null && cmMain.getSid().length() < 10){
	                	   			actionForm.setUpgradedMainCMuseRFASIDFlag("skipTR, upgraded CM Main still use RFA SID.");
	                	   			return Action.SUCCESS;
	                	   		}
	                	   		
	                	   	} else if(GRTConstants.SECODE.VCM.equalsIgnoreCase(cmMain.getSeCode())){
	                	   		actionForm.setLssOrESSLowerVersionFlag("skipTR, cannot onboard an LSP or ESS with CM5 or lower.");
	                	   		return Action.SUCCESS;
	                	   	}
	                         actionForm.getTechnicalRegistrationSummary().setCmMainsoldTo(cmMain.getSoldToId());
	                         logger.debug("CmMainMaterialCodeDesc :::" + cmMain.getMaterialDescription());
	                         actionForm.getTechnicalRegistrationSummary().setCmMainMaterialCodeDesc(cmMain.getMaterialDescription());
	                         logger.debug("CmMainSID :::" + cmMain.getSid());
	                         actionForm.getTechnicalRegistrationSummary().setCmMainSID(cmMain.getSid());
	                         actionForm.getTechnicalRegistrationSummary().setSolutionElementId(cmMain.getSeid());
	                         if(cmMain.getSid() != null){
	                               actionForm.getTechnicalRegistrationSummary().setSid(cmMain.getSid());
	                               actionForm.setReadOnly(true);
	                         }
	                         actionForm.setMainSeIdFlag("block");
	                         actionForm.setCmMainErrorFlag("none");
	                   } else {
	                              actionForm.setMessage("The SEID entered is not accessible.");
	                              actionForm.getTechnicalRegistrationSummary().setSid("");
	                              actionForm.setReadOnly(false);
	                        }
	               } else {
	                   actionForm.setMessage("The SEID entered is not a valid CM Main.");
	                   actionForm.getTechnicalRegistrationSummary().setSid("");
	                   actionForm.setReadOnly(false);
	               }
	       } else {
	             actionForm.setMessage("The SEID entered is not valid.");
	             actionForm.getTechnicalRegistrationSummary().setSid("");
	             actionForm.setReadOnly(false);
	       }
	       logger.debug("Exiting validateCMMain...");
	       return Action.SUCCESS;
	     }

	
	//Already implemented inside InstallBaseCreationAction.java
	@Override
	public String installBaseCreation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* START :: Dummy code for JSON-P */

	public String getInstallBaseJsopResponse(){
		message = "This is a Installbase Action";
		return Action.SUCCESS;
	}

	public String getTOBJsonpResponse(){
		message = "This is a Technical Onboarding Action";
		return Action.SUCCESS;
	}

	/* END ::  */


	/* GETTERS & SETTERS */
	public List<String> getAccessTypes() {
		return accessTypes;
	}

	public void setAccessTypes(List<String> accessTypes) {
		this.accessTypes = accessTypes;
	}

	public RegistrationFormBean getRegistrationFormBean() {
		return registrationFormBean;
	}

	public void setRegistrationFormBean(RegistrationFormBean registrationFormBean) {
		this.registrationFormBean = registrationFormBean;
	}

	public List<TechnicalRegistrationSummary> getReturnList() {
		return techRegistrationList;
	}

	public void setReturnList(List<TechnicalRegistrationSummary> returnList) {
		this.techRegistrationList = returnList;
	}

	public String getSoldToNumber() {
		return soldToNumber;
	}

	public void setSoldToNumber(String soldToNumber) {
		this.soldToNumber = soldToNumber;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getGatewaySeid() {
		return gatewaySeid;
	}

	public void setGatewaySeid(String gatewaySeid) {
		this.gatewaySeid = gatewaySeid;
	}

	public String getSoldToId() {
		return soldToId;
	}

	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public String getHtmlContentStr() {
		return htmlContentStr;
	}

	public void setHtmlContentStr(String htmlContentStr) {
		this.htmlContentStr = htmlContentStr;
	}

	public TechnicalOnBoardingService getTechnicalOnBoardingService() {
		return technicalOnBoardingService;
	}

	public void setTechnicalOnBoardingService(
			TechnicalOnBoardingService technicalOnBoardingService) {
		this.technicalOnBoardingService = technicalOnBoardingService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<TechnicalRegistrationSummary> getTechRegistrationList() {
		return techRegistrationList;
	}

	public void setTechRegistrationList(
			List<TechnicalRegistrationSummary> techRegistrationList) {
		this.techRegistrationList = techRegistrationList;
	}


	public Boolean getTobResubmitFlag() {
		return tobResubmitFlag;
	}


	public void setTobResubmitFlag(Boolean tobResubmitFlag) {
		this.tobResubmitFlag = tobResubmitFlag;
	}


	public String getSkipStepB() {
		return skipStepB;
	}


	public void setSkipStepB(String skipStepB) {
		this.skipStepB = skipStepB;
	}


	public String getPopUpValue() {
		return popUpValue;
	}


	public void setPopUpValue(String popUpValue) {
		this.popUpValue = popUpValue;
	}


	public List<String> getAccessTypesList() {
		return accessTypesList;
	}


	public void setAccessTypesList(List<String> accessTypesList) {
		this.accessTypesList = accessTypesList;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<TechnicalRegistrationSummary> getRegistrableProductList() {
		return registrableProductList;
	}

	public void setRegistrableProductList(
			List<TechnicalRegistrationSummary> registrableProductList) {
		this.registrableProductList = registrableProductList;
	}

	public String getTobDashboardDataError() {
		return tobDashboardDataError;
	}

	public void setTobDashboardDataError(String tobDashboardDataError) {
		this.tobDashboardDataError = tobDashboardDataError;
	}

	public Boolean getStepBResult() {
		return stepBResult;
	}

	public void setStepBResult(Boolean stepBResult) {
		this.stepBResult = stepBResult;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public String salGatewayMigrationList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * GRT 4.0 Changes.GRT4.00-BRS Requirement-BR-F.006
	 * And
	 * GRT 4.0 Changes-BRS Requirement No-BR-F.011
	 * 
	 * @param technicalRegistrationSummary
	 * @return
	 */
	private boolean checkIsNoConnectivity(TechnicalRegistrationSummary technicalRegistrationSummary)
	{
		boolean flag=true;
		
		if(technicalRegistrationSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_MODEM)) {
			
			if(StringUtils.isEmpty(technicalRegistrationSummary.getDialInNumber())) {
				flag=false;
			}
				
			if(StringUtils.isEmpty(technicalRegistrationSummary.getOutboundPrefix())) {
				flag=false;
			}
			if(StringUtils.isEmpty(technicalRegistrationSummary.getDialInNumber())) {
				flag=false;
			}
		} else if(technicalRegistrationSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_IP) || technicalRegistrationSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_RSAIP)) {
			
			
			String ossno_ip = technicalRegistrationSummary.getIpAddress();
			if(ossno_ip==null){
				flag=false;
			}
			
		} else if (technicalRegistrationSummary.getAccessType().equals(GRTConstants.ACCESS_TYPE_SAL)) {
			
			
			if(StringUtils.isEmpty(technicalRegistrationSummary.getPrimarySalgwSeid())) {
				flag=false;
			}
			
			
		} 
				
		return flag;
		
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	
}
