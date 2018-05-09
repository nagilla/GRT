package com.grt.dto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.SearchParam;



/**
 * Form bean for the traditional registration.
 *
 */
public class RegistrationFormBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String businessPartnerName = new String("");
	private String registrationId;
	private String soldToId;
	private String accessType;
	private boolean readOnly = false;
	private boolean readOnlyForBlankGrpId = false;
	private String processStepId;
	private String statusId;
	private String address1;
	private String address2;
	private String FirstName;
	private String LastName;
	private String city;
	private String state;
	private String zip;
	private String siteCountry;
	private String reportedEmail;
	private String reportedPhone;
	private String expedaite;
	private String onsiteFirstname;
	private String onsiteLastname;
	private String onsitePhone;
	private String onsiteEmail;
	private String companyPhone;
	private String useSameAddress;
	private String cuttimendate;
	private String company;
	private String soldToType;
	private String Process_Step_Id;
	private String Status_Id;
	private String expediteRefNo;
	private String locale;
	private String userId;
	private String technicalOrderId;
	private String userAccessType;
	private String uploadFilePath;
	private String saveType;
	private String rowCount;
	private String region;
	private String solutionElementId;
	private String salRegistrationId;
	private String noAddtionalProductToRegister;
	private boolean superUser = false;
	private boolean skipInstallBaseCreation;
	private String salMigrationOnly;
    private String alarmid;
    private String implementationType;
    private String ipoUserEmail;
    
    private boolean alarmEligible;

    private String alarmOriginationFlag;
    private List<String> alarmOriginations;
    
	private List<TechnicalOrderDetail> installBaseDetail = new ArrayList<TechnicalOrderDetail>();
	private List<TechnicalOrderDetail> existingInstallBaseList = new ArrayList<TechnicalOrderDetail>();
	private List<TechnicalOrderDetail> pendingOrders = new ArrayList<TechnicalOrderDetail>();
	private List<TechnicalOrderDetail> materialEntryList = new ArrayList<TechnicalOrderDetail>();
	private List<TechnicalOrderDetail> manuallyAddedMaterialEntryList = new ArrayList<TechnicalOrderDetail>();
	private List<TechnicalOrderDetail> materialsAfterExclusionList = new ArrayList<TechnicalOrderDetail>();
	private List<RegistrationSummary> registrationList = new ArrayList<RegistrationSummary>();
	private List<TechnicalRegistrationSummary> registrationSummaryList = new ArrayList<TechnicalRegistrationSummary>();
	private List<TechnicalRegistrationSummary> registrationSummaryListTRUpdate = new ArrayList<TechnicalRegistrationSummary>();
	private List<TechnicalRegistrationSummary> registrationSummaryListRegistrable = new ArrayList<TechnicalRegistrationSummary>();
	List<TechnicalRegistrationSummary> registrationSummaryListRetest = new ArrayList<TechnicalRegistrationSummary>();
	private List<TechnicalProductRegistration> productRegistrationList = new ArrayList<TechnicalProductRegistration>();
	private List<SALRegistrationDetail> salRegistrationDetailList = new ArrayList<SALRegistrationDetail>();
	private SALRegistrationDetail salRegistrationDetail = new SALRegistrationDetail();
	private List<AlertDto> alertsList = new ArrayList<AlertDto>();
	
	//Added for Summarized View
	Map<RVSummaryRecord,ArrayList<TechnicalOrderDetail>> summaryMap = new HashMap<RVSummaryRecord, ArrayList<TechnicalOrderDetail>>();

	private PurchaseOrder purchaseOrder = new PurchaseOrder();

	/*private FormFile theFile;*/
	private File theFile;
	private String theFileContentType;
	private String theFileFileName;
	
	private boolean saveSiteReg = false;
	private String typeOfImp;
	private String sendMail;
	private Boolean changeOwnership;
	private String filter_registrationId;
	private String poentered;
	private String resetGatewayList;
	private boolean ibSrCreated;
	private Pager finalRecordValidationPager;
	private Pager existingIBPager;
	private Pager pendingOrderPager;
	private Pager technicalRegistrationPager;
	private Pager technicalRegistrationSummaryPager;

	private SearchParam materialCode = new SearchParam("materialCode", "");
	private SearchParam description = new SearchParam("description", "");
	private SearchParam remainingQty = new SearchParam("remainingQuantity", "");

	private String materialCodeDescription;
	private String alarmID;
	private String operationType;
	private String serviceName;

	//[AVAYA] Password Reset Added new property
	private String message;
	private boolean hiddenFlag = false;

	// Alert properties
	private String currentTime;
	private String alertId;
	private String alert;
	private String startDate;
	private String endDate;
	private String megaUser = "0";
	
	//GRT 4.0
	private String srDescription;
	private String srOwnerName;
	private String srCompletionNarrative;

	//Addded 01/19/2012 for IPO install
	
/*	 * typeofinstallation = Type of Installation
industry = Industry (Verticals)
employeesthislocation = Total number of Company Employees (this Location)
totallocations = Total number of Company Locations
remoteconnectivity = Avaya Remote Connectivity Required Now
setscovered = Sets Covered Under Total Care(APR)*/
	 

	private String typeOfInstallation;
	private String industry;
	private String employeesAllLocation;
	private String employeesThisLocation;
	private String totalLocations;
	private String remoteConnectivity;
	private String ipoAccessType;
	private String setsCovered;
	private List<TechnicalOrderDetail> ipoList = new ArrayList<TechnicalOrderDetail>();
/*	private FormFile ipoFile;*/
	private Pager ipoPager;
	private List<RegistrationQuestionsDetail> regQstnsDtl = new ArrayList<RegistrationQuestionsDetail>();
	private String submitted;
	private String returnCode;
	private String userRole;
	private boolean selectAndUnselectAllMeterial;
	private boolean selectAndUnselectAllPending;
	private String ibaseStatus;
	private String isSrCompleted;
	private String registrationNotes;
	private String registrationIdentifier;

	private boolean fullRegistrationOnly;
	private boolean installbaseRegistrationOnly;
	private boolean technicalRegistrationOnly;
	private boolean equipmentRegistrationOnly;
	private boolean ipoRegistration;
	private boolean isSalMigrationOnly;
	private Boolean salMigration;
	private boolean viewInstallbaseOnly;
	private boolean recordValidationOnly;
	private List<SALGateway> salGatewayMigrationList = new ArrayList<SALGateway>();
	private String soldTo;
	private String salSeId;
	private List<SALGateway> activeSalGatewayList  = new ArrayList<SALGateway>();
	private List<SiteList> existingRegisteredAssetsList = new ArrayList<SiteList>();
	private String primary;
	//GRT 4.0 Changes
	private String popUpHiddenValue;
	private String noConnectivityAccType;
	private String secondary;
	private String gatewaySeid;

	private String status;
	private String count;

	//Begin Technical Registration Detail View
	List<TechnicalRegistration> technicalRegistrationDetailList = new ArrayList<TechnicalRegistration>();
	List<TechnicalRegistration> salRegistrationSummaryList = new ArrayList<TechnicalRegistration>();
	List<SiteList> salMigrationSummaryList = new ArrayList<SiteList>();
	private Boolean nonSalList;
	private Boolean salList;
	private Boolean salMigrationList;
	private Boolean seidCreationFailureFlag;
	//End Technical Registration Details View
	private List<String> materialEntryVersion;
	// TR Config Update
	private List<TRConfig> trConfigList;
	private Map<String, String> groupIds;
	private TRConfig trConfig;
	private String groupId;
	private Asset asset;
	private Map<String, String> accessTypes;
	private Map<String, String> releasesMap;
	private String ipAddrFlag;
	private String authFileIDFlag, authFileIDFlagOrigTRED, templateFlag, accessTypeFlag;
	private String dialInFlag, salMigrationFlag, swReleaseFlag;
	private TechnicalRegistrationSummary technicalRegistrationSummary;

	// TR Config
	private String isMainCM;
	private String initiateEPNSurvery;
	private String releaseNumber;
	private String isSAMPBoardUpgraded;
	private String checkIfSESEdge;
	private String doYouWantRP;
	private String randomPasswordFlag;
	private String connectivity, connectivityFlag;
	private List<String> connectivities;
	private Set<String> eligibleAccessType;
	private List<String> eligibleAccessTypesList;
	private List<String> softwareRelease;
	private String avayaIPAddress;
	private String privateIpEligibleFlag;
	private String dialInNumber;
	private String seidOfVoicePortalFlag;
	private String sampBoardUpgradedFlag;
	private String checkIfSESEdgeFlag;
	private String cmMainFlag;
	private String isMainSeidFlag;
	private String softwareReleaseFlag;
	private String spReleaseFlag;
	private String hardwareServerFlag;
	private List<String> spRelease;
	private List<String> hardwareServer;
	private String ipAddressFlag;
	private String outboundPrefixFlag;
	private String dialInNumberFlag;
	private String template;
	private Long remainingQuantity;
	private String sidMidErrorFlag, sidMidValidatedFlag, mainSeIdFlag, cmMainErrorFlag;
	private String sidMidShowFlag, sidMidRequiredFlag;
	private String tempSid, tempMid;
	private List<String> sidMid = new ArrayList<String>();
	private String duplicateSidMidFlag;
	private String productConfigurationFlag;
	private boolean highAvailablity;
	
	private String trdSeid;
	private String index;
	//For NonSal and SAL below field "technicalRegistrationId" is TechnicalRegistrationId
	//and for SalMigration it is SiteList id.
	private String technicalRegistrationId;
	private String trDetailType;
	private String uiOperation;

	//SAL alarm and connectivity
	private List<TechnicalRegistration> selectedSALRegSummaryList = new ArrayList<TechnicalRegistration>();
	private List<SiteList> selectedSALMigrationSummaryList = new ArrayList<SiteList>();

	//TR Detail Update Operation
	private TechnicalRegistration selectedTechRegForUpdate;
	private SiteList selectedSalMigrationForUpdate;
	private String serialNumber;
	private String errorMessage;

	private String ipoInventoryNotLoaded;

	private Boolean firstColumnOnly;
	private Boolean actveSRColumnOnly;
	private boolean alarmAndConnectivityDisabled = false;

	private String bannerStatus;
	private String bannerSubStatus;
	private String bannerSrNumber;
	private String bannerSrLabel;
	private String bannerActiveContractSrNumber;
	private String bannerSubmittedDate;
	private String bannerCompletedDate;
	private String sortBy;
	private int sortByCount;
	private String sortByScreen;
	private String screenName;

	private String sessionAttr;
	private String isCoreUnit;
	private String readyForTRDivFlag, tredListDivFlag, tredProdDivFlag;
	private boolean eqrFileDownloaded;
	private boolean eqrFileTemplateDownloaded;
	private boolean eqrMoveFileDownloaded;
	private String lssOrESSLowerVersionFlag;
	private String upgradedMainCMuseRFASIDFlag;

	private String groupIdChnaged;
	private String specialNote;	
	private String userNamePasswordFlag;
	
	private Account account;
	
	private File ipoFile;
	private String ipoFileContentType;
	private String ipoFileFileName;
	private int ipoFileFileSize;
	
	/* Newly Added Member variables */
	private String soldToError;
	private String navigateTo;
	
		//Added for install base creation flow
	private Boolean installBaseCreationReadOnlyFlag;
	private boolean installBaseEnableSubmit;
	private String uploadMessage;
	private String IBBack;
	private boolean isFirstTime = true;
	private String IPOR;
	private String rbselect;
	private boolean manuallyAddMaterial;
	
		//Added for Technical On-Boarding Flow
	private Boolean failedSeidIsNotNull = false;
	private Boolean disableCmRemote = false;
	
	private String tobDashboardDataError = "";
	private String sidMidStatus, sidMidValidated = "";
	private String skipStepB = "none";
	private boolean tobResubmitFlag = false;
	private String trCancel;
	private String scvToTRDashboard;
	private String fromTrObDetail;
	private String needEPN;
	private String primarySEIDReq;
	private String secondarySEIDReq;
	
	//Added for View Install Base Flow
	private boolean IBViewFileDownloaded;
	private String IBViewFileName;
	
	//GRT 4.0 24-March
	private String srNote;
	private String accountLocation;
	private String srNumber;
	private List<String> srSummaryInfoList;
	private String completeStatusId;
	private String source;
	private boolean listSizeFlag;
	private boolean trRetestDivFlag;
	private boolean validateDisabled;
	protected Map<String, List<String>> errorMessageMap = new HashMap<String, List<String>>();
	private boolean equipmentMoveOnly;
	private String toSoldToId;
	//GRT 4.0 :Retest 
	private List<TechnicalRegistration> technicalRegRetest = new ArrayList<TechnicalRegistration>();
	private List<TechnicalRegistration> retestTRList = new ArrayList<TechnicalRegistration>();
	private boolean retestListFlag;
	//GRT 4.0: Eqp Move
	private String eqmFromSoldTo;
	private String eqmToSoldTo;
	private String bannerAccessType;
	//IPOSS
	
	private AccountFormBean accountFormBean = new AccountFormBean();
	private TokenFormBean tokenFormBean = new TokenFormBean();
	
	private boolean redirectionFlag;
	
	private String auxMCMainSEIDShowFlag;
	private String auxMCMainSEIDRequiredFlag;
	
	private String auxMCMainSEIDErrorFlag;
	private String auxMCMainSEIDErrorMsg;
	
	public String getAuxMCMainSEIDErrorMsg() {
		return auxMCMainSEIDErrorMsg;
	}

	public void setAuxMCMainSEIDErrorMsg(String auxMCMainSEIDErrorMsg) {
		this.auxMCMainSEIDErrorMsg = auxMCMainSEIDErrorMsg;
	}

	private String temp_auxMCMainSEID;

	public String getTemp_auxMCMainSEID() {
		return temp_auxMCMainSEID;
	}

	public void setTemp_auxMCMainSEID(String temp_auxMCMainSEID) {
		this.temp_auxMCMainSEID = temp_auxMCMainSEID;
	}

	public String getAuxMCMainSEIDErrorFlag() {
		return auxMCMainSEIDErrorFlag;
	}

	public void setAuxMCMainSEIDErrorFlag(String auxMCMainSEIDErrorFlag) {
		this.auxMCMainSEIDErrorFlag = auxMCMainSEIDErrorFlag;
	}
	
	public String getAuxMCMainSEIDShowFlag() {
		return auxMCMainSEIDShowFlag;
	}

	public void setAuxMCMainSEIDShowFlag(String auxMCMainSEIDShowFlag) {
		this.auxMCMainSEIDShowFlag = auxMCMainSEIDShowFlag;
	}

	public String getAuxMCMainSEIDRequiredFlag() {
		return auxMCMainSEIDRequiredFlag;
	}

	public void setAuxMCMainSEIDRequiredFlag(String auxMCMainSEIDRequiredFlag) {
		this.auxMCMainSEIDRequiredFlag = auxMCMainSEIDRequiredFlag;
	}

	public boolean isRedirectionFlag() {
		return redirectionFlag;
	}

	public void setRedirectionFlag(boolean redirectionFlag) {
		this.redirectionFlag = redirectionFlag;
	}

	public File getIpoFile() {
		return ipoFile;
	}

	public void setIpoFile(File ipoFile) {
		this.ipoFile = ipoFile;
	}

	public String getIpoInventoryNotLoaded() {
		return ipoInventoryNotLoaded;
	}

	public void setIpoInventoryNotLoaded(String ipoInventoryNotLoaded) {
		this.ipoInventoryNotLoaded = ipoInventoryNotLoaded;
	}

	public String getIsSrCompleted() {
		return isSrCompleted;
	}

	public void setIsSrCompleted(String isSrCompleted) {
		this.isSrCompleted = isSrCompleted;
	}

	public String getIbaseStatus() {
		return ibaseStatus;
	}

	public void setIbaseStatus(String ibaseStatus) {
		this.ibaseStatus = ibaseStatus;
	}

	public String getReturnCode() {
		return StringEscapeUtils.unescapeHtml(returnCode);
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getSubmitted() {
		return submitted;
	}

	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}

	public List<RegistrationQuestionsDetail> getRegQstnsDtl() {
		return regQstnsDtl;
	}

	public void setRegQstnsDtl(List<RegistrationQuestionsDetail> regQstnsDtl) {
		this.regQstnsDtl = regQstnsDtl;
	}

/*	public FormFile getIpoFile() {
		return ipoFile;
	}

	public void setIpoFile(FormFile ipoFile) {
		this.ipoFile = ipoFile;
	}*/

	public List<TechnicalOrderDetail> getIpoList() {
		return ipoList;
	}

	public void setIpoList(List<TechnicalOrderDetail> ipoList) {
		this.ipoList = ipoList;
	}

	public String getEmployeesAllLocation() {
		return employeesAllLocation;
	}

	public void setEmployeesAllLocation(String employeesAllLocation) {
		this.employeesAllLocation = employeesAllLocation;
	}

	public String getEmployeesThisLocation() {
		return employeesThisLocation;
	}

	public void setEmployeesThisLocation(String employeesThisLocation) {
		this.employeesThisLocation = employeesThisLocation;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRemoteConnectivity() {
		return remoteConnectivity;
	}

	public void setRemoteConnectivity(String remoteConnectivity) {
		this.remoteConnectivity = remoteConnectivity;
	}

	public String getSetsCovered() {
		return setsCovered;
	}

	public void setSetsCovered(String setsCovered) {
		this.setsCovered = setsCovered;
	}

	public String getTotalLocations() {
		return totalLocations;
	}

	public void setTotalLocations(String totalLocations) {
		this.totalLocations = totalLocations;
	}

	public String getTypeOfInstallation() {
		return typeOfInstallation;
	}

	public void setTypeOfInstallation(String typeOfInstallation) {
		this.typeOfInstallation = typeOfInstallation;
	}


	//changes on 01/19/2012


	public SearchParam getDescription() {
		return description;
	}

	public void setDescription(SearchParam description) {
		this.description = description;
	}

	public SearchParam getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(SearchParam materialCode) {
		this.materialCode = materialCode;
	}

	public String getResetGatewayList() {
		return resetGatewayList;
	}

	public void setResetGatewayList(String resetGatewayList) {
		this.resetGatewayList = resetGatewayList;
	}

	public String getPoentered() {
		return poentered;
	}

	public void setPoentered(String poentered) {
		this.poentered = poentered;
	}

	public String getFilter_registrationId() {
		return StringEscapeUtils.unescapeHtml(filter_registrationId);
	}

	public void setFilter_registrationId(String filter_registrationId) {
		this.filter_registrationId = filter_registrationId;
	}

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public String getTypeOfImp() {
		return typeOfImp;
	}

	public void setTypeOfImp(String typeOfImp) {
		this.typeOfImp = typeOfImp;
	}

/*	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}*/

	public String getBusinessPartnerName() {
		return businessPartnerName;
	}

	public void setBusinessPartnerName(String businessPartnerName) {
		this.businessPartnerName = "";
	}

	public String getSoldToId() {
		return StringEscapeUtils.unescapeHtml(soldToId);
	}

	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public List<RegistrationSummary> getRegistrationList() {
		return registrationList;
	}

	public void setRegistrationList(List<RegistrationSummary> registrationList) {
		this.registrationList = registrationList;
	}

	public String getRegistrationId() {
		return StringEscapeUtils.unescapeHtml(registrationId);
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public List<TechnicalRegistrationSummary> getRegistrationSummaryList() {
		return registrationSummaryList;
	}

	public void setRegistrationSummaryList(
			List<TechnicalRegistrationSummary> registrationSummaryList) {
		this.registrationSummaryList = registrationSummaryList;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public List<TechnicalProductRegistration> getProductRegistrationList() {
		return productRegistrationList;
	}

	public void setProductRegistrationList(
			List<TechnicalProductRegistration> productRegistrationList) {
		this.productRegistrationList = productRegistrationList;
	}

	public SALRegistrationDetail getSalRegistrationDetail() {
		return salRegistrationDetail;
	}

	public void setSalRegistrationDetail(SALRegistrationDetail salRegistrationDetail) {
		this.salRegistrationDetail = salRegistrationDetail;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<TechnicalOrderDetail> getInstallBaseDetail() {
		return installBaseDetail;
	}

	public void setInstallBaseDetail(List<TechnicalOrderDetail> installBaseDetail) {
		this.installBaseDetail = installBaseDetail;
	}

	public String getProcessStepId() {
		return processStepId;
	}

	public void setProcessStepId(String processStepId) {
		this.processStepId = processStepId;
	}

	public String getStatusId() {
		return fixCSS(statusId);
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}





	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getTechnicalOrderId() {
		return StringEscapeUtils.unescapeHtml(technicalOrderId);
	}

	public void setTechnicalOrderId(String technicalOrderId) {
		this.technicalOrderId = technicalOrderId;
	}
	public List<TechnicalOrderDetail> getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(List<TechnicalOrderDetail> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public List<TechnicalOrderDetail> getMaterialEntryList() {
		return materialEntryList;
	}

	public void setMaterialEntryList(List<TechnicalOrderDetail> materialEntryList) {
		this.materialEntryList = materialEntryList;
	}

	public List<TechnicalOrderDetail> getManuallyAddedMaterialEntryList() {
		return manuallyAddedMaterialEntryList;
	}

	public void setManuallyAddedMaterialEntryList(
			List<TechnicalOrderDetail> manuallyAddedMaterialEntryList) {
		this.manuallyAddedMaterialEntryList = manuallyAddedMaterialEntryList;
	}

	public List<TechnicalOrderDetail> getMaterialsAfterExclusionList() {
		return materialsAfterExclusionList;
	}

	public void setMaterialsAfterExclusionList(List<TechnicalOrderDetail> materialsAfterExclusionList) {
		this.materialsAfterExclusionList = materialsAfterExclusionList;
	}

	public List<TechnicalOrderDetail> getExistingInstallBaseList() {
		return existingInstallBaseList;
	}

	public void setExistingInstallBaseList(
			List<TechnicalOrderDetail> existingInstallBaseList) {
		this.existingInstallBaseList = existingInstallBaseList;
	}

		public List<AlertDto> getAlertsList() {
		return alertsList;
	}

	public void setAlertsList(List<AlertDto> alertsList) {
		this.alertsList = alertsList;
	}

		public static long getSerialVersionUID() {
			return serialVersionUID;
		}

		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getExpedaite() {
			return expedaite;
		}

		public void setExpedaite(String expedaite) {
			this.expedaite = expedaite;
		}



		public String getFirstName() {
			return FirstName;
		}

		public void setFirstName(String firstName) {
			FirstName = firstName;
		}

		public String getLastName() {
			return LastName;
		}

		public void setLastName(String lastName) {
			LastName = lastName;
		}

		public String getOnsiteemail() {
			return onsiteEmail;
		}

		public void setOnsiteemail(String onsiteemail) {
			this.onsiteEmail = onsiteemail;
		}

		public String getOnsiteFirstname() {
			return onsiteFirstname;
		}

		public void setOnsiteFirstname(String onsiteFirstname) {
			this.onsiteFirstname = onsiteFirstname;
		}

		public String getOnsiteLsatname() {
			return onsiteLastname;
		}

		public void setOnsiteLsatname(String onsiteLsatname) {
			this.onsiteLastname = onsiteLsatname;
		}

		public String getOnsitephone() {
			return onsitePhone;
		}

		public void setOnsitephone(String onsitephone) {
			this.onsitePhone = onsitephone;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public String getCompanyPhone() {
			return companyPhone;
		}

		public void setCompanyPhone(String company) {
			this.companyPhone = company;
		}

		public String getOnsiteEmail() {
			return onsiteEmail;
		}

		public void setOnsiteEmail(String onsiteEmail) {
			this.onsiteEmail = onsiteEmail;
		}

		public String getOnsiteLastname() {
			return onsiteLastname;
		}

		public void setOnsiteLastname(String onsiteLastname) {
			this.onsiteLastname = onsiteLastname;
		}

		public String getOnsitePhone() {
			return onsitePhone;
		}

		public void setOnsitePhone(String onsitePhone) {
			this.onsitePhone = onsitePhone;
		}

		public String getUseSameAddress() {
			return useSameAddress;
		}

		public void setUseSameAddress(String useSameAddress) {
			this.useSameAddress = useSameAddress;
		}

		public String getCuttimendate() {
			return cuttimendate;
		}

		public void setCuttimendate(String cuttimendate) {
			this.cuttimendate = cuttimendate;
		}

		public String getCompany() {
			//return company;
			return StringEscapeUtils.unescapeHtml(company);
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getSoldToType() {
			return soldToType;
		}

		public void setSoldToType(String soldTOtype) {
			this.soldToType = soldTOtype;
		}

		public String getProcess_Step_Id() {
			return Process_Step_Id;
		}

		public void setProcess_Step_Id(String process_Step_Id) {
			Process_Step_Id = process_Step_Id;
		}

		public String getStatus_Id() {
			return Status_Id;
		}

		public void setStatus_Id(String status_Id) {
			Status_Id = status_Id;
		}

		public String getExpediteRefNo() {
			return expediteRefNo;
		}

		public void setExpediteRefNo(String expediteRefNo) {
			this.expediteRefNo = expediteRefNo;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public String getReportedEmail() {
			return reportedEmail;
		}

		public void setReportedEmail(String reportedEmail) {
			this.reportedEmail = reportedEmail;
		}

		public String getUserAccessType() {
			return userAccessType;
		}

		public void setUserAccessType(String userAccessType) {
			this.userAccessType = userAccessType;
		}

		public String getUploadFilePath() {
			return uploadFilePath;
		}

		public void setUploadFilePath(String uploadFilePath) {
			this.uploadFilePath = uploadFilePath;
		}

		public String getSaveType() {
			return StringEscapeUtils.unescapeHtml(saveType);
		}

		public void setSaveType(String saveType) {
			this.saveType = saveType;
		}

		public String getRowCount() {
			return rowCount;
		}

		public void setRowCount(String rowCount) {
			this.rowCount = rowCount;
		}
		public boolean getSaveSiteReg() {
			return saveSiteReg;
		}

		public void setSaveSiteReg(boolean saveSiteReg) {
			this.saveSiteReg = saveSiteReg;
		}

		public String getReportedPhone() {
			return reportedPhone;
		}

		public void setReportedPhone(String reportedPhone) {
			this.reportedPhone = reportedPhone;
		}

		public String getSiteCountry() {
			return siteCountry;
		}

		public void setSiteCountry(String siteCountry) {
			this.siteCountry = siteCountry;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public boolean getSkipInstallBaseCreation() {
			return skipInstallBaseCreation;
		}

		public void setSkipInstallBaseCreation(boolean skipInstallBaseCreation) {
			this.skipInstallBaseCreation = skipInstallBaseCreation;
		}

		public boolean isSuperUser() {
			return superUser;
		}

		public void setSuperUser(boolean superUser) {
			this.superUser = superUser;
		}

		public String getSolutionElementId() {
			return solutionElementId;
		}

		public void setSolutionElementId(String solutionElementId) {
			this.solutionElementId = solutionElementId;
		}

		public Boolean getChangeOwnership() {
			return changeOwnership;
		}

		public void setChangeOwnership(Boolean changeOwnership) {
			this.changeOwnership = changeOwnership;
		}

		public String getUserId() {
			return StringEscapeUtils.unescapeHtml(userId);
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getSalRegistrationId() {
			return salRegistrationId;
		}

		public void setSalRegistrationId(String salRegistrationId) {
			this.salRegistrationId = salRegistrationId;
		}

		public String getNoAddtionalProductToRegister() {
			return noAddtionalProductToRegister;
		}

		public void setNoAddtionalProductToRegister(String noAddtionalProductToRegister) {
			this.noAddtionalProductToRegister = noAddtionalProductToRegister;
		}

		public List<SALRegistrationDetail> getSalRegistrationDetailList() {
			return salRegistrationDetailList;
		}

		public void setSalRegistrationDetailList(
				List<SALRegistrationDetail> salRegistrationDetailList) {
			this.salRegistrationDetailList = salRegistrationDetailList;
		}

		public String getSalMigrationOnly() {
			return StringEscapeUtils.unescapeHtml(salMigrationOnly);
		}

		public void setSalMigrationOnly(String salMigrationOnly) {
			this.salMigrationOnly = salMigrationOnly;
		}


              public String getAlarmid() {
			return alarmid;
		}

		public void setAlarmid(String alarmid) {
			this.alarmid= alarmid;
		}

		public Pager getFinalRecordValidationPager() {
			return finalRecordValidationPager;
		}

		public void setFinalRecordValidationPager(Pager finalRecordValidationPager) {
			this.finalRecordValidationPager = finalRecordValidationPager;
		}

		public Pager getExistingIBPager() {
			return existingIBPager;
		}

		public void setExistingIBPager(Pager existingIBPager) {
			this.existingIBPager = existingIBPager;
		}

		public Pager getPendingOrderPager() {
			return pendingOrderPager;
		}

		public void setPendingOrderPager(Pager pendingOrderPager) {
			this.pendingOrderPager = pendingOrderPager;
		}

		public Pager getTechnicalRegistrationPager() {
			return technicalRegistrationPager;
		}

		public void setTechnicalRegistrationPager(Pager technicalRegistrationPager) {
			this.technicalRegistrationPager = technicalRegistrationPager;
		}

		public SearchParam getRemainingQty() {
			if(remainingQty==null){
				remainingQty.setFieldValue("0");
			}
			return remainingQty;
		}

		public void setRemainingQty(SearchParam remainingQty) {
			this.remainingQty = remainingQty;
		}

		public boolean getIbSrCreated() {
			return ibSrCreated;
		}

		public void setIbSrCreated(boolean ibSrCreated) {
			this.ibSrCreated = ibSrCreated;
		}

		public Pager getTechnicalRegistrationSummaryPager() {
			return technicalRegistrationSummaryPager;
		}

		public void setTechnicalRegistrationSummaryPager(
				Pager technicalRegistrationSummaryPager) {
			this.technicalRegistrationSummaryPager = technicalRegistrationSummaryPager;
		}

		public Pager getIpoPager() {
			return ipoPager;
		}

		public void setIpoPager(Pager ipoPager) {
			this.ipoPager = ipoPager;
		}

		public String getAlarmID() {
			return alarmID;
		}

		public void setAlarmID(String alarmID) {
			this.alarmID = alarmID;
		}

		public String getMessage() {
			return StringEscapeUtils.unescapeHtml(message);
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getOperationType() {
			return operationType;
		}

		public void setOperationType(String operationType) {
			this.operationType = operationType;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public boolean isHiddenFlag() {
			return hiddenFlag;
		}

		public void setHiddenFlag(boolean hiddenFlag) {
			this.hiddenFlag = hiddenFlag;
		}

		public String getImplementationType() {
			return implementationType;
		}

		public void setImplementationType(String implementationType) {
			this.implementationType = implementationType;
		}

		public String getIpoUserEmail() {
			return ipoUserEmail;
		}

		public void setIpoUserEmail(String ipoUserEmail) {
			this.ipoUserEmail = ipoUserEmail;
		}

		public String getUserRole() {
			return fixCSS(userRole);
		}

		public void setUserRole(String userRole) {
			this.userRole = userRole;
		}

		public boolean getSelectAndUnselectAllMeterial() {
			return selectAndUnselectAllMeterial;
		}

		public void setSelectAndUnselectAllMeterial(boolean selectAndUnselectAllMeterial) {
			this.selectAndUnselectAllMeterial = selectAndUnselectAllMeterial;
		}

		public boolean getSelectAndUnselectAllPending() {
			return selectAndUnselectAllPending;
		}

		public void setSelectAndUnselectAllPending(boolean selectAndUnselectAllPending) {
			this.selectAndUnselectAllPending = selectAndUnselectAllPending;
		}

		/**
		 * @return the alertId
		 */
		public String getAlertId() {
			return alertId;
		}

		/**
		 * @param alertId the alertId to set
		 */
		public void setAlertId(String alertId) {
			this.alertId = alertId;
		}

		/**
		 * @return the alert
		 */
		public String getAlert() {
			return alert;
		}

		/**
		 * @param alert the alert to set
		 */
		public void setAlert(String alert) {
			this.alert = alert;
		}

		/**
		 * @return the endDate
		 */
		public String getEndDate() {
			return endDate;
		}

		/**
		 * @param endDate the endDate to set
		 */
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		/**
		 * @return the startDate
		 */
		public String getStartDate() {
			return startDate;
		}

		/**
		 * @param startDate the startDate to set
		 */
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		/**
		 * @return the megaUser
		 */
		public String getMegaUser() {
			return megaUser;
		}

		/**
		 * @param megaUser the megaUser to set
		 */
		public void setMegaUser(String megaUser) {
			this.megaUser = megaUser;
		}

		public boolean isEquipmentRegistrationOnly() {
			return equipmentRegistrationOnly;
		}

		public void setEquipmentRegistrationOnly(boolean equipmentRegistrationOnly) {
			this.equipmentRegistrationOnly = equipmentRegistrationOnly;
		}

		public boolean isFullRegistrationOnly() {
			return fullRegistrationOnly;
		}

		public void setFullRegistrationOnly(boolean fullRegistrationOnly) {
			this.fullRegistrationOnly = fullRegistrationOnly;
		}

		public boolean getInstallbaseRegistrationOnly() {
			return installbaseRegistrationOnly;
		}

		public void setInstallbaseRegistrationOnly(boolean installbaseRegistrationOnly) {
			this.installbaseRegistrationOnly = installbaseRegistrationOnly;
		}

		public boolean isIpoRegistration() {
			return ipoRegistration;
		}

		public void setIpoRegistration(boolean ipoRegistration) {
			this.ipoRegistration = ipoRegistration;
		}

		public boolean isTechnicalRegistrationOnly() {
			return technicalRegistrationOnly;
		}

		public void setTechnicalRegistrationOnly(boolean technicalRegistrationOnly) {
			this.technicalRegistrationOnly = technicalRegistrationOnly;
		}

		public String getRegistrationNotes() {
			//return registrationNotes;
			return StringEscapeUtils.unescapeHtml(registrationNotes);
		}

		public void setRegistrationNotes(String registrationNotes) {
			this.registrationNotes = registrationNotes;
		}

		public String getRegistrationIdentifier() {
			//return registrationIdentifier;
			return StringEscapeUtils.unescapeHtml(registrationIdentifier);
		}

		public void setRegistrationIdentifier(String registrationIdentifier) {
			this.registrationIdentifier = registrationIdentifier;
		}

		public boolean isSalMigrationOnly() {
			return isSalMigrationOnly;
		}

		public void setSalMigrationOnly(boolean isSalMigrationOnly) {
			this.isSalMigrationOnly = isSalMigrationOnly;
		}

		public List<SALGateway> getSalGatewayMigrationList() {
			return salGatewayMigrationList;
		}

		public void setSalGatewayMigrationList(List<SALGateway> salGatewayMigrationList) {
			this.salGatewayMigrationList = salGatewayMigrationList;
		}

		public String getSalSeId() {
			return salSeId;
		}

		public void setSalSeId(String salSeId) {
			this.salSeId = salSeId;
		}

		public String getSoldTo() {
			return soldTo;
		}

		public void setSoldTo(String soldTo) {
			this.soldTo = soldTo;
		}

		public List<SALGateway> getActiveSalGatewayList() {
			return activeSalGatewayList;
		}

		public void setActiveSalGatewayList(List<SALGateway> activeSalGatewayList) {
			this.activeSalGatewayList = activeSalGatewayList;
		}

		public List<SiteList> getExistingRegisteredAssetsList() {
			return existingRegisteredAssetsList;
		}

		public void setExistingRegisteredAssetsList(
				List<SiteList> existingRegisteredAssetsList) {
			this.existingRegisteredAssetsList = existingRegisteredAssetsList;
		}

		public String getPrimary() {
			return primary;
		}

		public void setPrimary(String primary) {
			this.primary = primary;
		}

		public String getSecondary() {
			return secondary;
		}

		public void setSecondary(String secondary) {
			this.secondary = secondary;
		}

		public String getGatewaySeid() {
			return gatewaySeid;
		}

		public void setGatewaySeid(String gatewaySeid) {
			this.gatewaySeid = gatewaySeid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public List<TechnicalRegistrationSummary> getRegistrationSummaryListTRUpdate() {
			return registrationSummaryListTRUpdate;
		}

		public void setRegistrationSummaryListTRUpdate(
				List<TechnicalRegistrationSummary> registrationSummaryListTRUpdate) {
			this.registrationSummaryListTRUpdate = registrationSummaryListTRUpdate;
		}

		public List<SiteList> getSalMigrationSummaryList() {
			return salMigrationSummaryList;
		}

		public void setSalMigrationSummaryList(List<SiteList> salMigrationSummaryList) {
			this.salMigrationSummaryList = salMigrationSummaryList;
		}

		public List<String> getMaterialEntryVersion() {
			return materialEntryVersion;
		}

		public void setMaterialEntryVersion(List<String> materialEntryVersion) {
			this.materialEntryVersion = materialEntryVersion;
		}

		public List<TechnicalRegistration> getSalRegistrationSummaryList() {
			return salRegistrationSummaryList;
		}

		public void setSalRegistrationSummaryList(
				List<TechnicalRegistration> salRegistrationSummaryList) {
			this.salRegistrationSummaryList = salRegistrationSummaryList;
		}

		public List<TechnicalRegistration> getTechnicalRegistrationDetailList() {
			return technicalRegistrationDetailList;
		}

		public void setTechnicalRegistrationDetailList(
				List<TechnicalRegistration> technicalRegistrationDetailList) {
			this.technicalRegistrationDetailList = technicalRegistrationDetailList;
		}

		/**
		 * @return the materialCodeDescription
		 */
		public String getMaterialCodeDescription() {
			return materialCodeDescription;
		}

		/**
		 * @param materialCodeDescription the materialCodeDescription to set
		 */
		public void setMaterialCodeDescription(String materialCodeDescription) {
			this.materialCodeDescription = materialCodeDescription;
		}

		/**
		 * @return the trConfigList
		 */
		public List<TRConfig> getTrConfigList() {
			return trConfigList;
		}

		/**
		 * @param trConfigList the trConfigList to set
		 */
		public void setTrConfigList(List<TRConfig> trConfigList) {
			this.trConfigList = trConfigList;
		}

		/**
		 * @return the groupIds
		 */
		public Map<String, String> getGroupIds() {
			return groupIds;
		}

		/**
		 * @param groupIds the groupIds to set
		 */
		public void setGroupIds(Map<String, String> groupIds) {
			this.groupIds = groupIds;
		}

		/**
		 * @return the trConfig
		 */
		public TRConfig getTrConfig() {
			return trConfig;
		}

		/**
		 * @param trConfig the trConfig to set
		 */
		public void setTrConfig(TRConfig trConfig) {
			this.trConfig = trConfig;
		}

		public List<TechnicalRegistrationSummary> getRegistrationSummaryListRegistrable() {
			return registrationSummaryListRegistrable;
		}

		public void setRegistrationSummaryListRegistrable(
				List<TechnicalRegistrationSummary> registrationSummaryListRegistrable) {
			this.registrationSummaryListRegistrable = registrationSummaryListRegistrable;
		}


		/**
		 * @return the groupId
		 */
		public String getGroupId() {
			return groupId;
		}

		/**
		 * @param groupId the groupId to set
		 */
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		/**
		 * @return the asset
		 */
		public Asset getAsset() {
			return asset;
		}

		/**
		 * @param asset the asset to set
		 */
		public void setAsset(Asset asset) {
			this.asset = asset;
		}

		public String getIndex() {
			return index;
		}
		public void setIndex(String index) {
			this.index = index;
		}

		public String getTrdSeid() {
			return trdSeid;
		}

		public void setTrdSeid(String trdSeid) {
			this.trdSeid = trdSeid;
		}

		public String getTechnicalRegistrationId() {
			return technicalRegistrationId;
		}

		public void setTechnicalRegistrationId(String technicalRegistrationId) {
			this.technicalRegistrationId = technicalRegistrationId;
		}

		public String getTrDetailType() {
			return trDetailType;
		}

		public void setTrDetailType(String trDetailType) {
			this.trDetailType = trDetailType;
		}

		public List<SiteList> getSelectedSALMigrationSummaryList() {
			return selectedSALMigrationSummaryList;
		}

		public void setSelectedSALMigrationSummaryList(
				List<SiteList> selectedSALMigrationSummaryList) {
			this.selectedSALMigrationSummaryList = selectedSALMigrationSummaryList;
		}

		public List<TechnicalRegistration> getSelectedSALRegSummaryList() {
			return selectedSALRegSummaryList;
		}

		public void setSelectedSALRegSummaryList(
				List<TechnicalRegistration> selectedSALRegSummaryList) {
			this.selectedSALRegSummaryList = selectedSALRegSummaryList;
		}

		/**
		 * @return the accessTypes
		 */
		public Map<String, String> getAccessTypes() {
			return accessTypes;
		}

		/**
		 * @param accessTypes the accessTypes to set
		 */
		public void setAccessTypes(Map<String, String> accessTypes) {
			this.accessTypes = accessTypes;
		}

		/**
		 * @return the releasesMap
		 */
		public Map<String, String> getReleasesMap() {
			return releasesMap;
		}

		/**
		 * @param releasesMap the releasesMap to set
		 */
		public void setReleasesMap(Map<String, String> releasesMap) {
			this.releasesMap = releasesMap;
		}

		public String getUiOperation() {
			return uiOperation;
		}

		public void setUiOperation(String uiOperation) {
			this.uiOperation = uiOperation;
		}

		public TechnicalRegistration getSelectedTechRegForUpdate() {
			return selectedTechRegForUpdate;
		}

		public void setSelectedTechRegForUpdate(
				TechnicalRegistration selectedTechRegForUpdate) {
			this.selectedTechRegForUpdate = selectedTechRegForUpdate;
		}

		public SiteList getSelectedSalMigrationForUpdate() {
			return selectedSalMigrationForUpdate;
		}

		public void setSelectedSalMigrationForUpdate(
				SiteList selectedSalMigrationForUpdate) {
			this.selectedSalMigrationForUpdate = selectedSalMigrationForUpdate;
		}

		/**
		 * @return the ipAddrFlag
		 */
		public String getIpAddrFlag() {
			return ipAddrFlag;
		}

		/**
		 * @param ipAddrFlag the ipAddrFlag to set
		 */
		public void setIpAddrFlag(String ipAddrFlag) {
			this.ipAddrFlag = ipAddrFlag;
		}

		/**
		 * @return the initiateEPNSurvery
		 */
		public String getInitiateEPNSurvery() {
			return initiateEPNSurvery;
		}

		/**
		 * @param initiateEPNSurvery the initiateEPNSurvery to set
		 */
		public void setInitiateEPNSurvery(String initiateEPNSurvery) {
			this.initiateEPNSurvery = initiateEPNSurvery;
		}

		/**
		 * @return the isMainCM
		 */
		public String getIsMainCM() {
			return isMainCM;
		}

		/**
		 * @param isMainCM the isMainCM to set
		 */
		public void setIsMainCM(String isMainCM) {
			this.isMainCM = isMainCM;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		/**
		 * @return the releaseNumber
		 */
		public String getReleaseNumber() {
			return releaseNumber;
		}

		/**
		 * @param releaseNumber the releaseNumber to set
		 */
		public void setReleaseNumber(String releaseNumber) {
			this.releaseNumber = releaseNumber;
		}

		public Boolean getSalMigration() {
			return salMigration;
		}

		public void setSalMigration(Boolean salMigration) {
			this.salMigration = salMigration;
		}
		
		public boolean isViewInstallbaseOnly() {
			return viewInstallbaseOnly;
		}

		public void setViewInstallbaseOnly(boolean viewInstallbaseOnly) {
			this.viewInstallbaseOnly = viewInstallbaseOnly;
		}

		public boolean isRecordValidationOnly() {
			return recordValidationOnly;
		}

		public void setRecordValidationOnly(boolean recordValidationOnly) {
			this.recordValidationOnly = recordValidationOnly;
		}
		public Boolean getFirstColumnOnly() {
			return firstColumnOnly;
		}

		public void setFirstColumnOnly(Boolean firstColumnOnly) {
			this.firstColumnOnly = firstColumnOnly;
		}

		public String getAuthFileIDFlag() {
			return authFileIDFlag;
		}

		public void setAuthFileIDFlag(String authFileIDFlag) {
			this.authFileIDFlag = authFileIDFlag;
		}

		public boolean isAlarmAndConnectivityDisabled() {
			return alarmAndConnectivityDisabled;
		}

		public void setAlarmAndConnectivityDisabled(boolean alarmAndConnectivityDisabled) {
			this.alarmAndConnectivityDisabled = alarmAndConnectivityDisabled;
		}

		public String getTemplateFlag() {
			return templateFlag;
		}

		public void setTemplateFlag(String templateFlag) {
			this.templateFlag = templateFlag;
		}

		public String getIsSAMPBoardUpgraded() {
			return isSAMPBoardUpgraded;
		}

		public void setIsSAMPBoardUpgraded(String isSAMPBoardUpgraded) {
			this.isSAMPBoardUpgraded = isSAMPBoardUpgraded;
		}

		public String getCheckIfSESEdge() {
			return checkIfSESEdge;
		}

		public void setCheckIfSESEdge(String checkIfSESEdge) {
			this.checkIfSESEdge = checkIfSESEdge;
		}

		public String getConnectivity() {
			return connectivity;
		}

		public void setConnectivity(String connectivity) {
			this.connectivity = connectivity;
		}

		public String getDoYouWantRP() {
			return doYouWantRP;
		}

		public void setDoYouWantRP(String doYouWantRP) {
			this.doYouWantRP = doYouWantRP;
		}

		public List<String> getConnectivities() {
			return connectivities;
		}

		public void setConnectivities(List<String> connectivities) {
			this.connectivities = connectivities;
		}

		public List<String> getSoftwareRelease() {
			return softwareRelease;
		}

		public void setSoftwareRelease(List<String> softwareRelease) {
			this.softwareRelease = softwareRelease;
		}

		public String getAccessTypeFlag() {
			return accessTypeFlag;
		}

		public void setAccessTypeFlag(String accessTypeFlag) {
			this.accessTypeFlag = accessTypeFlag;
		}

		public String getDialInFlag() {
			return dialInFlag;
		}

		public void setDialInFlag(String dialInFlag) {
			this.dialInFlag = dialInFlag;
		}

		public String getSalMigrationFlag() {
			return salMigrationFlag;
		}

		public void setSalMigrationFlag(String salMigrationFlag) {
			this.salMigrationFlag = salMigrationFlag;
		}

		public String getAvayaIPAddress() {
			return avayaIPAddress;
		}

		public void setAvayaIPAddress(String avayaIPAddress) {
			this.avayaIPAddress = avayaIPAddress;
		}

		public String getDialInNumber() {
			return dialInNumber;
		}

		public void setDialInNumber(String dialInNumber) {
			this.dialInNumber = dialInNumber;
		}

		public String getBannerStatus() {
			return bannerStatus;
		}

		public void setBannerStatus(String bannerStatus) {
			this.bannerStatus = bannerStatus;
		}

		public String getBannerSubStatus() {
			return bannerSubStatus;
		}

		public void setBannerSubStatus(String bannerSubStatus) {
			this.bannerSubStatus = bannerSubStatus;
		}

		public String getSwReleaseFlag() {
			return swReleaseFlag;
		}

		public void setSwReleaseFlag(String swReleaseFlag) {
			this.swReleaseFlag = swReleaseFlag;
		}

		public String getRandomPasswordFlag() {
			return randomPasswordFlag;
		}

		public void setRandomPasswordFlag(String randomPasswordFlag) {
			this.randomPasswordFlag = randomPasswordFlag;
		}

		public String getPrivateIpEligibleFlag() {
			return privateIpEligibleFlag;
		}

		public void setPrivateIpEligibleFlag(String privateIpEligibleFlag) {
			this.privateIpEligibleFlag = privateIpEligibleFlag;
		}

		public String getSeidOfVoicePortalFlag() {
			return seidOfVoicePortalFlag;
		}

		public void setSeidOfVoicePortalFlag(String seidOfVoicePortalFlag) {
			this.seidOfVoicePortalFlag = seidOfVoicePortalFlag;
		}

		public String getSampBoardUpgradedFlag() {
			return sampBoardUpgradedFlag;
		}

		public void setSampBoardUpgradedFlag(String sampBoardUpgradedFlag) {
			this.sampBoardUpgradedFlag = sampBoardUpgradedFlag;
		}

		public String getCheckIfSESEdgeFlag() {
			return checkIfSESEdgeFlag;
		}

		public void setCheckIfSESEdgeFlag(String checkIfSESEdgeFlag) {
			this.checkIfSESEdgeFlag = checkIfSESEdgeFlag;
		}

		public String getCmMainFlag() {
			return cmMainFlag;
		}

		public void setCmMainFlag(String cmMainFlag) {
			this.cmMainFlag = cmMainFlag;
		}

		public String getSoftwareReleaseFlag() {
			return softwareReleaseFlag;
		}

		public void setSoftwareReleaseFlag(String softwareReleaseFlag) {
			this.softwareReleaseFlag = softwareReleaseFlag;
		}

		public String getHardwareServerFlag() {
			return hardwareServerFlag;
		}

		public void setHardwareServerFlag(String hardwareServerFlag) {
			this.hardwareServerFlag = hardwareServerFlag;
		}

		public String getSpReleaseFlag() {
			return spReleaseFlag;
		}

		public void setSpReleaseFlag(String spReleaseFlag) {
			this.spReleaseFlag = spReleaseFlag;
		}

		public TechnicalRegistrationSummary getTechnicalRegistrationSummary() {
			return technicalRegistrationSummary;
		}

		public void setTechnicalRegistrationSummary(
				TechnicalRegistrationSummary technicalRegistrationSummary) {
			this.technicalRegistrationSummary = technicalRegistrationSummary;
		}

		/**
		 * @return the bannerSrNumber
		 */
		public String getBannerSrNumber() {
			return bannerSrNumber;
		}

		/**
		 * @param bannerSrNumber the bannerSrNumber to set
		 */
		public void setBannerSrNumber(String bannerSrNumber) {
			this.bannerSrNumber = bannerSrNumber;
		}

		public List<String> getSpRelease() {
			return spRelease;
		}

		public void setSpRelease(List<String> spRelease) {
			this.spRelease = spRelease;
		}

		public List<String> getHardwareServer() {
			return hardwareServer;
		}

		public void setHardwareServer(List<String> hardwareServer) {
			this.hardwareServer = hardwareServer;
		}

		public String getDialInNumberFlag() {
			return dialInNumberFlag;
		}

		public void setDialInNumberFlag(String dialInNumberFlag) {
			this.dialInNumberFlag = dialInNumberFlag;
		}

		public String getIpAddressFlag() {
			return ipAddressFlag;
		}

		public void setIpAddressFlag(String ipAddressFlag) {
			this.ipAddressFlag = ipAddressFlag;
		}

		public String getOutboundPrefixFlag() {
			return outboundPrefixFlag;
		}

		public void setOutboundPrefixFlag(String outboundPrefixFlag) {
			this.outboundPrefixFlag = outboundPrefixFlag;
		}

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public Long getRemainingQuantity() {
			return remainingQuantity;
		}

		public void setRemainingQuantity(Long remainingQuantity) {
			this.remainingQuantity = remainingQuantity;
		}



		public String getSortBy() {
			return StringEscapeUtils.unescapeHtml(sortBy);
		}

		public void setSortBy(String sortBy) {
			this.sortBy = sortBy;
		}

		public int getSortByCount() {
			return sortByCount;
		}

		public void setSortByCount(int sortByCount) {
			this.sortByCount = sortByCount;
		}

		public String getSortByScreen() {
			return StringEscapeUtils.unescapeHtml(sortByScreen);
		}

		public void setSortByScreen(String sortByScreen) {
			this.sortByScreen = sortByScreen;
		}

		public String getSessionAttr() {
			return sessionAttr;
		}

		public void setSessionAttr(String sessionAttr) {
			this.sessionAttr = sessionAttr;
		}

		public String getIsMainSeidFlag() {
			return isMainSeidFlag;
		}

		public void setIsMainSeidFlag(String isMainSeidFlag) {
			this.isMainSeidFlag = isMainSeidFlag;
		}

		public String getScreenName() {
			return screenName;
		}

		public void setScreenName(String screenName) {
			this.screenName = screenName;
		}

		/**
		 * @return the nonSalList
		 */
		public Boolean getNonSalList() {
			return nonSalList;
		}
		public String getIsCoreUnit() {
			return isCoreUnit;
		}

		/**
		 * @param nonSalList the nonSalList to set
		 */
		public void setNonSalList(Boolean nonSalList) {
			this.nonSalList = nonSalList;
		}

		/**
		 * @return the salList
		 */
		public Boolean getSalList() {
			return salList;
		}

		/**
		 * @param salList the salList to set
		 */
		public void setSalList(Boolean salList) {
			this.salList = salList;
		}

		/**
		 * @return the salMigrationList
		 */
		public Boolean getSalMigrationList() {
			return salMigrationList;
		}

		/**
		 * @param salMigrationList the salMigrationList to set
		 */
		public void setSalMigrationList(Boolean salMigrationList) {
			this.salMigrationList = salMigrationList;
		}

		public void setIsCoreUnit(String isCoreUnit) {
			this.isCoreUnit = isCoreUnit;
		}

		public String getMainSeIdFlag() {
			return mainSeIdFlag;
		}

		public void setMainSeIdFlag(String mainSeIdFlag) {
			this.mainSeIdFlag = mainSeIdFlag;
		}

		public String getCmMainErrorFlag() {
			return cmMainErrorFlag;
		}

		public void setCmMainErrorFlag(String cmMainErrorFlag) {
			this.cmMainErrorFlag = cmMainErrorFlag;
		}

		public String getSidMidRequiredFlag() {
			return sidMidRequiredFlag;
		}

		public void setSidMidRequiredFlag(String sidMidRequiredFlag) {
			this.sidMidRequiredFlag = sidMidRequiredFlag;
		}

		public String getSidMidErrorFlag() {
			return sidMidErrorFlag;
		}

		public void setSidMidErrorFlag(String sidMidErrorFlag) {
			this.sidMidErrorFlag = sidMidErrorFlag;
		}

		public String getSidMidShowFlag() {
			return sidMidShowFlag;
		}

		public void setSidMidShowFlag(String sidMidShowFlag) {
			this.sidMidShowFlag = sidMidShowFlag;
		}

		public String getTempMid() {
			return tempMid;
		}

		public void setTempMid(String tempMid) {
			this.tempMid = tempMid;
		}

		public String getTempSid() {
			return tempSid;
		}

		public void setTempSid(String tempSid) {
			this.tempSid = tempSid;
		}

		public Set<String> getEligibleAccessType() {
			return eligibleAccessType;
		}

		public void setEligibleAccessType(Set<String> eligibleAccessType) {
			this.eligibleAccessType = eligibleAccessType;
		}

		public String getReadyForTRDivFlag() {
			return readyForTRDivFlag;
		}

		public void setReadyForTRDivFlag(String readyForTRDivFlag) {
			this.readyForTRDivFlag = readyForTRDivFlag;
		}

		public String getTredListDivFlag() {
			return tredListDivFlag;
		}

		public void setTredListDivFlag(String tredListDivFlag) {
			this.tredListDivFlag = tredListDivFlag;
		}

		public String getTredProdDivFlag() {
			return tredProdDivFlag;
		}

		public void setTredProdDivFlag(String tredProdDivFlag) {
			this.tredProdDivFlag = tredProdDivFlag;
		}


		/*public String getTrRetestDivFlag() {
			return trRetestDivFlag;
		}

		public void setTrRetestDivFlag(String trRetestDivFlag) {
			this.trRetestDivFlag = trRetestDivFlag;
		}*/

		public boolean isEqrFileDownloaded() {
			return eqrFileDownloaded;
		}

		public void setEqrFileDownloaded(boolean eqrFileDownloaded) {
			this.eqrFileDownloaded = eqrFileDownloaded;
		}
		
		public boolean isEqrFileTemplateDownloaded() {
			return eqrFileTemplateDownloaded;
		}

		public void setEqrFileTemplateDownloaded(boolean eqrFileTemplateDownloaded) {
			this.eqrFileTemplateDownloaded = eqrFileTemplateDownloaded;
		}

		public String getSidMidValidatedFlag() {
			return sidMidValidatedFlag;
		}

		public void setSidMidValidatedFlag(String sidMidValidatedFlag) {
			this.sidMidValidatedFlag = sidMidValidatedFlag;
		}

		public String getCurrentTime() {
			return currentTime;
		}

		public void setCurrentTime(String currentTime) {
			this.currentTime = currentTime;
		}

		public Boolean getActveSRColumnOnly() {
			return actveSRColumnOnly;
		}
		public List<String> getSidMid() {
			return sidMid;
		}

		public void setActveSRColumnOnly(Boolean actveSRColumnOnly) {
			this.actveSRColumnOnly = actveSRColumnOnly;
		}

		public String getBannerActiveContractSrNumber() {
			return bannerActiveContractSrNumber;
		}

		public void setBannerActiveContractSrNumber(String bannerActiveContractSrNumber) {
			this.bannerActiveContractSrNumber = bannerActiveContractSrNumber;
		}


		public void setSidMid(List<String> sidMid) {
			this.sidMid = sidMid;
		}

		public String getConnectivityFlag() {
			return connectivityFlag;
		}

		public void setConnectivityFlag(String connectivityFlag) {
			this.connectivityFlag = connectivityFlag;
		}

		public String getDuplicateSidMidFlag() {
			return duplicateSidMidFlag;
		}

		public void setDuplicateSidMidFlag(String duplicateSidMidFlag) {
			this.duplicateSidMidFlag = duplicateSidMidFlag;
		}

		/**
		 * @return the bannerSrLabel
		 */
		public String getBannerSrLabel() {
			return bannerSrLabel;
		}

		/**
		 * @param bannerSrLabel the bannerSrLabel to set
		 */
		public void setBannerSrLabel(String bannerSrLabel) {
			this.bannerSrLabel = bannerSrLabel;
		}

		/**
		 * @return the groupIdChnaged
		 */
		public String getGroupIdChnaged() {
			return groupIdChnaged;
		}

		/**
		 * @param groupIdChnaged the groupIdChnaged to set
		 */
		public void setGroupIdChnaged(String groupIdChnaged) {
			this.groupIdChnaged = groupIdChnaged;
		}

		/**
		 * @return the eligibleAccessTypesList
		 */
		public List<String> getEligibleAccessTypesList() {
			return eligibleAccessTypesList;
		}

		/**
		 * @param eligibleAccessTypesList the eligibleAccessTypesList to set
		 */
		public void setEligibleAccessTypesList(List<String> eligibleAccessTypesList) {
			this.eligibleAccessTypesList = eligibleAccessTypesList;
		}

		public String getProductConfigurationFlag() {
			return productConfigurationFlag;
		}

		public void setProductConfigurationFlag(String productConfigurationFlag) {
			this.productConfigurationFlag = productConfigurationFlag;
		}

		public boolean isHighAvailablity() {
			return highAvailablity;
		}

		public void setHighAvailablity(boolean highAvailablity) {
			this.highAvailablity = highAvailablity;
		}

		public String getSpecialNote() {
			return specialNote;
		}

		public void setSpecialNote(String specialNote) {
			this.specialNote = specialNote;
		}

		public boolean isReadOnlyForBlankGrpId() {
			return readOnlyForBlankGrpId;
		}

		public void setReadOnlyForBlankGrpId(boolean readOnlyForBlankGrpId) {
			this.readOnlyForBlankGrpId = readOnlyForBlankGrpId;
		}

		public String getAuthFileIDFlagOrigTRED() {
			return authFileIDFlagOrigTRED;
		}

		public void setAuthFileIDFlagOrigTRED(String authFileIDFlagOrigTRED) {
			this.authFileIDFlagOrigTRED = authFileIDFlagOrigTRED;
		}

		public String getUserNamePasswordFlag() {
			return userNamePasswordFlag;
		}

		public void setUserNamePasswordFlag(String userNamePasswordFlag) {
			this.userNamePasswordFlag = userNamePasswordFlag;
		}

		public String getBannerCompletedDate() {
			return bannerCompletedDate;
		}

		public void setBannerCompletedDate(String bannerCompletedDate) {
			this.bannerCompletedDate = bannerCompletedDate;
		}

		public String getBannerSubmittedDate() {
			return bannerSubmittedDate;
		}

		public void setBannerSubmittedDate(String bannerSubmittedDate) {
			this.bannerSubmittedDate = bannerSubmittedDate;
		}

		public Boolean getSeidCreationFailureFlag() {
			return seidCreationFailureFlag;
		}

		public void setSeidCreationFailureFlag(Boolean seidCreationFailureFlag) {
			this.seidCreationFailureFlag = seidCreationFailureFlag;
		}

		public String getAlarmOriginationFlag() {
			return alarmOriginationFlag;
		}

		public void setAlarmOriginationFlag(String alarmOriginationFlag) {
			this.alarmOriginationFlag = alarmOriginationFlag;
		}

		public List<String> getAlarmOriginations() {
			return alarmOriginations;
		}

		public void setAlarmOriginations(List<String> alarmOriginations) {
			this.alarmOriginations = alarmOriginations;
		}

		public boolean isAlarmEligible() {
			return alarmEligible;
		}

		public void setAlarmEligible(boolean alarmEligible) {
			this.alarmEligible = alarmEligible;
		}

		public String getIpoAccessType() {
			return ipoAccessType;
		}

		public void setIpoAccessType(String ipoAccessType) {
			this.ipoAccessType = ipoAccessType;
		}
	
		public Account getAccount() {
			return account;
		}

		public void setAccount(Account account) {
			this.account = account;
		}

		public File getTheFile() {
			return theFile;
		}

		public void setTheFile(File theFile) {
			this.theFile = theFile;
		}

		public String getTheFileContentType() {
			return theFileContentType;
		}

		public void setTheFileContentType(String theFileContentType) {
			this.theFileContentType = theFileContentType;
		}

		public String getTheFileFileName() {
			return theFileFileName;
		}

		public void setTheFileFileName(String theFileFileName) {
			this.theFileFileName = theFileFileName;
		}

		public String getSoldToError() {
			return soldToError;
		}

		public void setSoldToError(String soldToError) {
			this.soldToError = soldToError;
		}

		public Boolean getInstallBaseCreationReadOnlyFlag() {
			return installBaseCreationReadOnlyFlag;
		}

		public void setInstallBaseCreationReadOnlyFlag(
				Boolean installBaseCreationReadOnlyFlag) {
			this.installBaseCreationReadOnlyFlag = installBaseCreationReadOnlyFlag;
		}

		public Boolean getFailedSeidIsNotNull() {
			return failedSeidIsNotNull;
		}

		public void setFailedSeidIsNotNull(Boolean failedSeidIsNotNull) {
			this.failedSeidIsNotNull = failedSeidIsNotNull;
		}

		public Boolean getDisableCmRemote() {
			return disableCmRemote;
		}

		public void setDisableCmRemote(Boolean disableCmRemote) {
			this.disableCmRemote = disableCmRemote;
		}

		public String getTobDashboardDataError() {
			return tobDashboardDataError;
		}

		public void setTobDashboardDataError(String tobDashboardDataError) {
			this.tobDashboardDataError = tobDashboardDataError;
		}

		public String getSidMidStatus() {
			return sidMidStatus;
		}

		public void setSidMidStatus(String sidMidStatus) {
			this.sidMidStatus = sidMidStatus;
		}

		public String getSidMidValidated() {
			return sidMidValidated;
		}

		public void setSidMidValidated(String sidMidValidated) {
			this.sidMidValidated = sidMidValidated;
		}

		public String getNavigateTo() {
			return navigateTo;
		}

		public void setNavigateTo(String navigateTo) {
			this.navigateTo = navigateTo;
		}

		public boolean isInstallBaseEnableSubmit() {
			return installBaseEnableSubmit;
		}

		public void setInstallBaseEnableSubmit(boolean installBaseEnableSubmit) {
			this.installBaseEnableSubmit = installBaseEnableSubmit;
		}

		public String getUploadMessage() {
			return uploadMessage;
		}

		public void setUploadMessage(String uploadMessage) {
			this.uploadMessage = uploadMessage;
		}

		public String getIBBack() {
			return IBBack;
		}

		public void setIBBack(String iBBack) {
			IBBack = iBBack;
		}

		public boolean isFirstTime() {
			return isFirstTime;
		}

		public void setFirstTime(boolean isFirstTime) {
			this.isFirstTime = isFirstTime;
		}

		public String getIPOR() {
			return IPOR;
		}

		public void setIPOR(String iPOR) {
			IPOR = iPOR;
		}

		public String getRbselect() {
			return rbselect;
		}

		public void setRbselect(String rbselect) {
			this.rbselect = rbselect;
		}

		public boolean isManuallyAddMaterial() {
			return manuallyAddMaterial;
		}

		public void setManuallyAddMaterial(boolean manuallyAddMaterial) {
			this.manuallyAddMaterial = manuallyAddMaterial;
		}
		
		public String getLssOrESSLowerVersionFlag() {
			return lssOrESSLowerVersionFlag;
		}

		public void setLssOrESSLowerVersionFlag(String lssOrESSLowerVersionFlag) {
			this.lssOrESSLowerVersionFlag = lssOrESSLowerVersionFlag;
		}

		public String getUpgradedMainCMuseRFASIDFlag() {
			return upgradedMainCMuseRFASIDFlag;
		}

		public void setUpgradedMainCMuseRFASIDFlag(String upgradedMainCMuseRFASIDFlag) {
			this.upgradedMainCMuseRFASIDFlag = upgradedMainCMuseRFASIDFlag;
		}

		public boolean isIBViewFileDownloaded() {
			return IBViewFileDownloaded;
		}

		public void setIBViewFileDownloaded(boolean iBViewFileDownloaded) {
			IBViewFileDownloaded = iBViewFileDownloaded;
		}

		public String getIBViewFileName() {
			return IBViewFileName;
		}

		public void setIBViewFileName(String iBViewFileName) {
			IBViewFileName = iBViewFileName;
		}

		public String getPopUpHiddenValue() {
			return popUpHiddenValue;
		}

		public void setPopUpHiddenValue(String popUpHiddenValue) {
			this.popUpHiddenValue = popUpHiddenValue;
		}

		public String getSkipStepB() {
			return skipStepB;
		}

		public void setSkipStepB(String skipStepB) {
			this.skipStepB = skipStepB;
		}

		public boolean isTobResubmitFlag() {
			return tobResubmitFlag;
		}

		public void setTobResubmitFlag(boolean tobResubmitFlag) {
			this.tobResubmitFlag = tobResubmitFlag;
		}

		public String getTrCancel() {
			return trCancel;
		}

		public void setTrCancel(String trCancel) {
			this.trCancel = trCancel;
		}

		public String getScvToTRDashboard() {
			return scvToTRDashboard;
		}

		public void setScvToTRDashboard(String scvToTRDashboard) {
			this.scvToTRDashboard = scvToTRDashboard;
		}

		public String getFromTrObDetail() {
			return fromTrObDetail;
		}

		public void setFromTrObDetail(String fromTrObDetail) {
			this.fromTrObDetail = fromTrObDetail;
		}

		public String getSrNote() {
			return srNote;
		}

		public void setSrNote(String srNote) {
			this.srNote = srNote;
		}

		public String getAccountLocation() {
			return accountLocation;
		}

		public void setAccountLocation(String accountLocation) {
			this.accountLocation = accountLocation;
		}

		public String getSrNumber() {
			return srNumber;
		}

		public void setSrNumber(String srNumber) {
			this.srNumber = srNumber;
		}

		public List<String> getSrSummaryInfoList() {
			return srSummaryInfoList;
		}

		public void setSrSummaryInfoList(List<String> srSummaryInfoList) {
			this.srSummaryInfoList = srSummaryInfoList;
		}

		public String getCompleteStatusId() {
			return completeStatusId;
		}

		public void setCompleteStatusId(String completeStatusId) {
			this.completeStatusId = completeStatusId;
		}
		
		public String getNeedEPN() {
			return needEPN;
		}

		public void setNeedEPN(String needEPN) {
			this.needEPN = needEPN;
		}

		public String getPrimarySEIDReq() {
			return primarySEIDReq;
		}

		public void setPrimarySEIDReq(String primarySEIDReq) {
			this.primarySEIDReq = primarySEIDReq;
		}

		public String getSecondarySEIDReq() {
			return secondarySEIDReq;
		}

		public void setSecondarySEIDReq(String secondarySEIDReq) {
			this.secondarySEIDReq = secondarySEIDReq;
		}

		public String getSrDescription() {
			return srDescription;
		}

		public void setSrDescription(String srDescription) {
			this.srDescription = srDescription;
		}

		public String getSrOwnerName() {
			return srOwnerName;
		}

		public void setSrOwnerName(String srOwnerName) {
			this.srOwnerName = srOwnerName;
		}

		public String getSrCompletionNarrative() {
			return srCompletionNarrative;
		}

		public void setSrCompletionNarrative(String srCompletionNarrative) {
			this.srCompletionNarrative = srCompletionNarrative;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public boolean isListSizeFlag() {
			return listSizeFlag;
		}

		public void setListSizeFlag(boolean listSizeFlag) {
			this.listSizeFlag = listSizeFlag;
		}

		public boolean isTrRetestDivFlag() {
			return trRetestDivFlag;
		}

		public void setTrRetestDivFlag(boolean trRetestDivFlag) {
			this.trRetestDivFlag = trRetestDivFlag;
		}

		public List<TechnicalRegistrationSummary> getRegistrationSummaryListRetest() {
			return registrationSummaryListRetest;
		}

		public void setRegistrationSummaryListRetest(
				List<TechnicalRegistrationSummary> registrationSummaryListRetest) {
			this.registrationSummaryListRetest = registrationSummaryListRetest;
		}

		public boolean isValidateDisabled() {
			return validateDisabled;
		}

		public void setValidateDisabled(boolean validateDisabled) {
			this.validateDisabled = validateDisabled;
		}

		public Map<String, List<String>> getErrorMessageMap() {
			return errorMessageMap;
		}

		public void setErrorMessageMap(Map<String, List<String>> errorMessageMap) {
			this.errorMessageMap = errorMessageMap;
		}

		public boolean isEquipmentMoveOnly() {
			return equipmentMoveOnly;
		}

		public void setEquipmentMoveOnly(boolean equipmentMoveOnly) {
			this.equipmentMoveOnly = equipmentMoveOnly;
		}

		public boolean isEqrMoveFileDownloaded() {
			return eqrMoveFileDownloaded;
		}

		public void setEqrMoveFileDownloaded(boolean eqrMoveFileDownloaded) {
			this.eqrMoveFileDownloaded = eqrMoveFileDownloaded;
		}

		public String getToSoldToId() {
			//return toSoldToId;
			return StringEscapeUtils.unescapeHtml(toSoldToId);
		}

		public void setToSoldToId(String toSoldToId) {
			this.toSoldToId = toSoldToId;
		}

		public List<TechnicalRegistration> getTechnicalRegRetest() {
			return technicalRegRetest;
		}

		public void setTechnicalRegRetest(List<TechnicalRegistration> technicalRegRetest) {
			this.technicalRegRetest = technicalRegRetest;
		}

		public List<TechnicalRegistration> getRetestTRList() {
			return retestTRList;
		}

		public void setRetestTRList(List<TechnicalRegistration> retestTRList) {
			this.retestTRList = retestTRList;
		}

		public boolean isRetestListFlag() {
			return retestListFlag;
		}

		public void setRetestListFlag(boolean retestListFlag) {
			this.retestListFlag = retestListFlag;
		}

		public String getNoConnectivityAccType() {
			return noConnectivityAccType;
		}

		public void setNoConnectivityAccType(String noConnectivityAccType) {
			this.noConnectivityAccType = noConnectivityAccType;
		}

		public String getEqmFromSoldTo() {
			return eqmFromSoldTo;
			//return StringEscapeUtils.unescapeHtml(eqmFromSoldTo);
		}

		public void setEqmFromSoldTo(String eqmFromSoldTo) {
			this.eqmFromSoldTo = eqmFromSoldTo;
		}

		public String getEqmToSoldTo() {
			return eqmToSoldTo;
			//return StringEscapeUtils.unescapeHtml(eqmToSoldTo);
		}

		public void setEqmToSoldTo(String eqmToSoldTo) {
			this.eqmToSoldTo = eqmToSoldTo;
		}

		public String getBannerAccessType() {
			return bannerAccessType;
		}

		public void setBannerAccessType(String bannerAccessType) {
			this.bannerAccessType = bannerAccessType;
		}


		public AccountFormBean getAccountFormBean() {
			return accountFormBean;
		}

		public void setAccountFormBean(AccountFormBean accountFormBean) {
			this.accountFormBean = accountFormBean;
		}

		public String getIpoFileFileName() {
			return ipoFileFileName;
		}

		public void setIpoFileFileName(String ipoFileFileName) {
			this.ipoFileFileName = ipoFileFileName;
		}

		public TokenFormBean getTokenFormBean() {
			return tokenFormBean;
		}

		public void setTokenFormBean(TokenFormBean tokenFormBean) {
			this.tokenFormBean = tokenFormBean;
		}

		public String getIpoFileContentType() {
			return ipoFileContentType;
		}

		public void setIpoFileContentType(String ipoFileContentType) {
			this.ipoFileContentType = ipoFileContentType;
		}

		public Map<RVSummaryRecord, ArrayList<TechnicalOrderDetail>> getSummaryMap() {
			return summaryMap;
		}

		public void setSummaryMap(
				Map<RVSummaryRecord, ArrayList<TechnicalOrderDetail>> summaryMap) {
			this.summaryMap = summaryMap;
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
}


