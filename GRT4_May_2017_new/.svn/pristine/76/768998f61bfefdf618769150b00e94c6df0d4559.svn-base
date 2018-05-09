package com.avaya.grt.web.action.salmigration;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.dao.installbase.InstallBaseDao;
import com.avaya.grt.dao.salmigration.SalMigrationDao;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.service.salmigration.SalMigrationService;
import com.avaya.grt.web.action.GrtConfig;
import com.avaya.grt.web.action.technicalonboarding.TechnicalOnBoardingAction;
import com.grt.dto.Account;
import com.grt.dto.InstallBaseCreationDto;
import com.grt.dto.ManagedDevice;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.SALGateway;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.opensymphony.xwork2.Action;

@SuppressWarnings("deprecation")
public class SalMigrationAction<fullRegistrationOnly> extends TechnicalOnBoardingAction {
	
	private static final Logger logger = Logger
			.getLogger(SalMigrationAction.class);
	private SalMigrationService salMigrationService;
	public BaseHibernateDao baseHibernateDao;
	public SalMigrationDao salMigrationDao;
	public InstallBaseDao installBaseDao;
	private InputStream inputStream;
	
	private String hostName;

	/**
	 * This method redirects to the enter sold-to page depends upon the type of
	 * user
	 * 
	 * @return
	 */
	public String salMigration(){
		
		try {
			validateSession();
		} catch (Exception e1) {
			e1.printStackTrace();	
		}
		
		actionForm.setTechnicalRegistrationOnly(false); //TOB		
		actionForm.setRecordValidationOnly(false); //record validation
		actionForm.setViewInstallbaseOnly(false); //view installbase
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setInstallbaseRegistrationOnly(false); //IB
		actionForm.setFullRegistrationOnly(false);
		
		salMigrationOnlyFlag = true;
		//installBaseCreationReadOnlyFlag = true;
		actionForm.setSalMigrationOnly(true);
		actionForm.setFirstColumnOnly(true);
		
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}
	
	public String salGatewayMigrationList() {
		try {
			validateSession();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Entering into salGatewayMigrationList()");
		//TO BE CHANGED
		List<SALGateway> salGatewayMigrationList = getTechnicalOnBoardingService().getSALGateways(actionForm.getSoldToId(), null, GRTConstants.SAL);
		logger.debug("THE CONTENT OF salGatewayMigrationList IS =======> "+ salGatewayMigrationList);
		logger.debug("THE CONTENT OF salGatewayMigrationList IS =======> "+ salGatewayMigrationList);
		if (null != salGatewayMigrationList && salGatewayMigrationList.size()> 0) {
			logger.debug("DATA RETRIEVED FROM   getRegistrationDelegate().getSALGateways(form.getSoldToId(), null); METHOD IS WORKING");
		} else{
			logger.debug("NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND NO DATA FOUND");
		}
		for(SALGateway sgw : salGatewayMigrationList){
			logger.debug("<------------INITIALIZING THE VALUES TO FALSE I.E. NOT CHECKED STATE ------------->");
			sgw.setPrimaryChecked(false);
			sgw.setSecondaryChecked(false);
		}
		actionForm.setSalGatewayMigrationList(salGatewayMigrationList);
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Timer to fetch SAL Gateways from Siebel >> Time in milliseconds"+ (c2-c1));
		logger.debug("Exiting from salGatewayMigrationList()");
		
		
		return Action.SUCCESS;
	}

	
	
	public SalMigrationDao getSalMigrationDao() {
		return salMigrationDao;
	}

	public void setSalMigrationDao(SalMigrationDao salMigrationDao) {
		this.salMigrationDao = salMigrationDao;
	}

	public String nextForSal() throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Selected PRIMARY SEID ========> "+actionForm.getPrimary());
		logger.debug("Selected SECONDARY  SEID ========>> "+actionForm.getSecondary());
		String primarySeId = actionForm.getPrimary();
		getRequest().getSession().setAttribute(GRTConstants.PRIMARY_SE_ID,primarySeId);
		String secondarySeId = actionForm.getSecondary();
		getRequest().getSession().setAttribute(GRTConstants.SECONDARY_SE_ID,secondarySeId);
		List<SALGateway> activeSalGatewayList  = new ArrayList<SALGateway>();
		List<SALGateway> salGatewayMigrationList = actionForm.getSalGatewayMigrationList();
		for (SALGateway sgw : salGatewayMigrationList) {
			if (sgw.getSeid().equals(actionForm.getPrimary())) {
				SALGateway sALGateway = sgw;
				sALGateway.setPrimaryOrSecondary("PRIMARY");
				//Begin to populate the Radio Button status
				sgw.setPrimaryChecked(true);
				//Begin to populate the Radio Button status
				activeSalGatewayList.add(sALGateway);
			} else if (sgw.getSeid().equals(actionForm.getSecondary())){
				SALGateway sALGateway = sgw;
				sALGateway.setPrimaryOrSecondary("SECONDARY");
				//Begin to populate the Radio Button status
				sgw.setSecondaryChecked(true);
				//Begin to populate the Radio Button status
				activeSalGatewayList.add(sALGateway);
			} else {
				sgw.setPrimaryChecked(false);
				sgw.setSecondaryChecked(false);
			}
		}
		actionForm.setActiveSalGatewayList(activeSalGatewayList);
		//Begin to populate the Radio Button status
		actionForm.setSalGatewayMigrationList(salGatewayMigrationList);
		//End to populate the Radio Button status
		List<SiteList> existingRegisteredAssetsList = new ArrayList<SiteList>();
		existingRegisteredAssetsList = getSalMigrationService().getSALMigrationEligibleAssets(actionForm.getSoldToId());
		//Begin Set primary and secondary seid to siteList record

		if (existingRegisteredAssetsList.size() > 0) {
			for (SiteList siteList : existingRegisteredAssetsList ){
				siteList.setPrimarySALGatewaySEID(primarySeId);
				siteList.setSecondarySALGatewaySEID(secondarySeId);

			}
		}
		//End Set primary and secondary seid to siteList record
		actionForm.setExistingRegisteredAssetsList(existingRegisteredAssetsList);
		//populate the dummy data
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Timer to Navigate to Sal Migration Details view >> Time in milliseconds"+ (c2-c1));
		return Action.SUCCESS;
	}

	//@Jpf.Action(useFormBean = "registrationFormBean", forwards = { @Jpf.Forward(name = "back", path = "/pages/registration/newRegistration.jsp") })
	public String backFromSALGatewayMigrationList () throws Exception{
		validateSession();
		return Action.SUCCESS;
	}
	
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "success", path = "/pages/salMigration/salGatewayMigrationList.jsp")})*/
	public String backFromSALMigrationDetails () throws Exception {
		validateSession();
		actionForm.setPrimary(null);
		actionForm.setSecondary(null);
		return Action.SUCCESS;
	}
	
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "cancel", action = "begin")})*/
	public String cancelSALMigrationDetails () throws Exception {
		validateSession();
		//getSession().invalidate();
		getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
		getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);
		//added to fix https redirect issue in co-browse iframe
		hostName = getRequest().getHeader("host");
		return Action.SUCCESS;
	}
	
	public String getSALGatewayDetails () throws Exception {
		String gatewaySeid = actionForm.getGatewaySeid(); 
		String soldToId = actionForm.getSoldToId();
		
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Entering ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK  ACTION CORRESPONDING TO HYPERLINK");
		boolean condition = false;
		SALGatewayIntrospection gateway = null;
		try{
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
		return getHtmlDocument(gateway);
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
			
			inputStream =new StringBufferInputStream(htmlBuilder.toString());
			
		}return Action.SUCCESS;
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
		
		inputStream =new StringBufferInputStream(htmlBuilder.toString());
		
		return Action.SUCCESS;
		
		//return htmlBuilder.toString();
	}
	
	
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = { @Jpf.Forward(name = "success", action = "registrationList"),
			@Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage) })*/
			public String createNewSALGateway () throws Exception {
				validateSession();
				long c1 = Calendar.getInstance().getTimeInMillis();
				logger.debug(" Entering into  RegistrationController.createNewSALGateway()");
				try{
				/*String salGatewayMaterialCode = GRTPropertiesUtil.getProperty(GRTConstants.SALGW_MC).trim();*/
				String salGatewayMaterialCode = grtConfig.getSalGatewayMaterialCode();
				logger.debug("The content of  salGatewayMaterialCode ====> "+ salGatewayMaterialCode);
				List <TRConfig> tRConfig = getSalMigrationService().isTREligible(salGatewayMaterialCode);
				SiteRegistration siteRegistration = getSiteRegistrationForNewSALGateway(actionForm);
				TechnicalOrderDetail technicalOrderDetail = constructTechnicalOrderDetail(actionForm.getSoldToId(), tRConfig.get(0));
				List<TechnicalOrderDetail> installBaseData = new ArrayList<TechnicalOrderDetail>();
				installBaseData.add(technicalOrderDetail);
				getSalMigrationService().createNewSALGateway (siteRegistration, installBaseData, tRConfig.get(0), this.getUserFromSession().getUserId());
				logger.debug(" Exiting from  RegistrationController.createNewSALGateway()");
				long c2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("Timer Create New SAL Gateway >> Time in milliseconds"+ (c2-c1));
				return Action.SUCCESS;
				} catch (Exception exception) {
					logger.error("Exception while creating the New SAL Gateway", exception);
					return Action.ERROR;
				}
			}
			
			
			private SiteRegistration getSiteRegistrationForNewSALGateway(RegistrationFormBean form) throws Exception {
				logger.debug(" Entering into  saveSiteRegistrationForNewSALGateway");
				SiteRegistration siteRegistration = null;
				siteRegistration = constructSiteRegistration(form, RegistrationTypeEnum.FULLREGISTRATION, null);
				Status ibStatus = new Status();
				ibStatus.setStatusId(StatusEnum.SAVED.getStatusId());
				siteRegistration.setInstallBaseStatus(ibStatus);
				Status notInitiated = new Status();
				notInitiated.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
				Status inProcess = new Status();
				inProcess.setStatusId(StatusEnum.INPROCESS.getStatusId());
				siteRegistration.setTechRegStatus(notInitiated);
				siteRegistration.setFinalValidationStatus(notInitiated);
				logger.debug(" Exiting from  saveSiteRegistrationForNewSALGateway");
				return siteRegistration;
			}
			
			
			private TechnicalOrderDetail constructTechnicalOrderDetail(String soldToId, TRConfig tRConfig) throws Exception{
				logger.debug(" Entering into  constructTechnicalOrderDetail");
				TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
				/*String salGatewayMaterialCode = GRTPropertiesUtil.getProperty(GRTConstants.SALGW_MC).trim();*/
				String salGatewayMaterialCode = grtConfig.getSalGatewayMaterialCode();
				/*if (salGatewayMaterialCode == null) {
				salGatewayMaterialCode = "227272";
				}*/
				List<String> materialCodes = new ArrayList<String>();
				materialCodes.add(salGatewayMaterialCode);
				Map<String, String> mDescMap = getBaseHibernateDao().getMaterialCodeDesc(materialCodes);
				String materialCodeDesc = mDescMap.get(salGatewayMaterialCode);
				technicalOrderDetail.setMaterialCode(salGatewayMaterialCode);
				technicalOrderDetail.setInitialQuantity(new Long(1));
				technicalOrderDetail.setRemainingQuantity(new Long(1));
				technicalOrderDetail.setOrderType(GRTConstants.TECH_ORDER_TYPE_IB);
				technicalOrderDetail.setMaterialExclusion(GRTConstants.FALSE);
				technicalOrderDetail.setSolutionElementCode(tRConfig.getSeCode());
				technicalOrderDetail.setAutoTR(true);
				//technicalOrderDetail.setCutOverDate(Calendar.getInstance());
				technicalOrderDetail.setSoldToId(soldToId);
				technicalOrderDetail.setShipToId(soldToId);
				logger.debug(" Exiting from  constructTechnicalOrderDetail");
				return technicalOrderDetail;
			}
			
			
			
			

	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "success", action = "getTechnicalOrderDetails" ),
			@Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage)})*/
	public String saveExistingRegisteredAssetsList() throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("++++++++++++++++++++++++++ Entering into saveExistingRegisteredAssetsList() +++++++++++++++++++++++++++++++");
		//Begin Fetch data from RegistrationFormBean to save into Site Registration tabel.
		SiteRegistration siteRegistration = null;
		siteRegistration = constructSiteRegistration(actionForm, RegistrationTypeEnum.SALMIGRATION, null);
		int save = getBaseRegistrationService().saveSiteRegistrationDetails(siteRegistration);
		if (save == 1) {
			logger.debug("@@@@@@@@@@@@@@@@@@ === Data Saved  Data Saved  Data Saved  Data Saved ================================@@@@@@@@@@");
		} else {
			logger.error("@@@@@@@@@@@@@@@@@@ ==== SAVE FAILED  SAVE FAILED   SAVE FAILED ================================@@@@@@@@@@");
			return Action.ERROR;
		}
		//End Fetch data from RegistrationFormBean to save into Site Registration tabel.
		List<SiteList> existingRegisteredAssetsList = actionForm.getExistingRegisteredAssetsList();
		logger.debug("@@@@@@@@@@@@@@@@@@ ==== existingRegisteredAssetsList from the RegistrationFormBean is ===============================>"+existingRegisteredAssetsList);
		List<SiteList> selectedAssetsList = new ArrayList<SiteList>();

		if (null != existingRegisteredAssetsList && existingRegisteredAssetsList.size()>0){
			for (SiteList siteList : existingRegisteredAssetsList) {
				if (siteList.isSelected()) {
					siteList.setSiteRegistration(siteRegistration);
					siteList.setStatus(siteRegistration.getStatus());
					siteList.setNumberOfSubmit("0");
					Status notInitiated = new Status();
					notInitiated.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
					notInitiated.setStatusDescription(StatusEnum.NOTINITIATED.getStatusDescription());
					siteList.setStepBStatus(notInitiated);
					selectedAssetsList.add(siteList);
				}
			}
		}
		//
		///Testing of the SiteRegistration details
		if (null != selectedAssetsList && selectedAssetsList.size()>0){
			for (SiteList siteList : selectedAssetsList) {
				logger.debug("SITE REGISTRATION DATA TESTING  getRegistrationId=====>::: "+siteList.getGroupId());
			}
		}
		//
		if (null != selectedAssetsList && selectedAssetsList.size()>0){
			try{
				getSalMigrationService().saveExistingRegisteredAssetsList(selectedAssetsList, siteRegistration);
			}catch (Exception ex){
				logger.error("Exception While Saving ExistingRegisteredAssets :: ", ex);
				return Action.ERROR;
				//struts mapping
			}
		}
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Timer to Save Existing Registered Assets List >> Time in milliseconds"+ (c2-c1));

		logger.debug("++++++++++++++++++++++++++++++ Exiting from saveExistingRegisteredAssetsList() ++++++++++++++++++++++++++++++++");
		getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
		getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);

		return getTechnicalOrderDetails();
	}
	
	
	public BaseHibernateDao getBaseHibernateDao() {
		return baseHibernateDao;
	}

	public void setBaseHibernateDao(BaseHibernateDao baseHibernateDao) {
		this.baseHibernateDao = baseHibernateDao;
	}

	public String technicalRegistrationDashboard() {
		return null;
	}

	public String installBaseCreation() {
		return null;
	}

	public SalMigrationService getSalMigrationService() {
		return salMigrationService;
	}

	public void setSalMigrationService(SalMigrationService salMigrationService) {
		this.salMigrationService = salMigrationService;
	}

	public InstallBaseDao getInstallBaseDao() {
		return installBaseDao;
	}

	public void setInstallBaseDao(InstallBaseDao installBaseDao) {
		this.installBaseDao = installBaseDao;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String getHostName() {
		return hostName;
	}


	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
}
