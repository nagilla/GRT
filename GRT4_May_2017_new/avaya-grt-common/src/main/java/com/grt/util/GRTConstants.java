package com.grt.util;

import java.util.Arrays;
import java.util.List;

/**
 * Constants for the GRT application.
 *
 * @author Perficient
 *
 */
public class GRTConstants {
	//SAL Types
	public final static String SALMigration = "1001";
	public final static String SALUniversalInstall = "1003";
	public final static String SALNonUniversalInstall = "1002";
	public final static String SALAlarmRegistration = "1004";
	
	//SAL Type Description
	public final static String SALMigration_Desc = "salMigration";
	public final static String SALUniversalInstall_Desc = "salUniversalInstallProductRegistration";
	public final static String SALNonUniversalInstall_Desc = "salNonUniversalProductRegistration";
	public final static String SALAlarmRegistration_Desc = "alarmRegistration";	
	
	public final static String SALUiProductRegistration_desc = "salUniversalProductRegistration";
	public final static String SALSiteList_desc = "salSiteList";
	public final static String SALNOnUiProductList_desc = "SALNOnUiProductList";
	public final static String SALUiProductList_desc = "SALUiProductList";

	//Status of Registration
	public final static String INITIATION = "1001";
	public final static String IN_PROCESS = "1002";
	public final static String AWAITNG_INFORMATION = "1003";
	public final static String COMPLETED = "1004";
	public final static String NOTINITIATED = "1005";
	public final static String SAVED_DESCRIPTION = "1007";
	public final static String CANCELLED = "1008";

	//Status Description of Registration
	public final static String INITIATION_DESCRIPTION = "Need Registration";
	public final static String IN_PROCESS_DESCRIPTION = "In Process";
	public final static String AWAITNG_INFORMATION_DESCRIPTION = "Awaiting Information";
	public final static String COMPLETED_DESCRIPTION = "Completed";
	public final static String YET_TO_SUBMIT = "Only Saved";
	public final static String STATUS_NOT_INITIATED = "Not Initiated";

	//Process Steps
	public final static String INSTALL_BASE_CREATION = "P0001";
	public final static String TECHNICAL_REGISTRATION = "P0002";
	public final static String FINAL_RECORD_VALIDATION = "P0003";
	//GRT 4.0 Eqp Move
	public final static String EQUIPMENT_MOVE = "P0004";
	
	public final static String SAL = "SAL";
	public final static String YES = "Yes";
	public final static String NO = "No";
    public final static String REQUIRED = "Required";
    public final static String DEFAULT = "Default";
    public static final String LDAP_CUST_BP_PLACEHOLDER="cust";
	
	public final static String TRUE = "true";
	public final static String FALSE = "false";

	public final static String Y = "Y";
	public final static String N = "N";
	public final static String D = "D";
	
	public final static String TECH_ORDER_TYPE_IB = "IB";
	public final static String TECH_ORDER_TYPE_TR = "TR";
	public final static String TECH_ORDER_TYPE_TR_UPDATE = "TR_UPDATE";
	public final static String TECH_ORDER_TYPE_TR_RETEST = "TR_RETEST";
	public final static String TECH_ORDER_TYPE_FV = "FV";
	//GRT4.0 : For order_type in Technical_Order and action in pipeline_sap_transactions
	public final static String TECH_ORDER_TYPE_EM = "EM";
	public final static String TECH_ORDER_TYPE_EQR = "EQR";
	
	public final static String SCREEN_IB = "IB";
	public final static String SCREEN_EQR = "EQR";
	public final static String SCREEN_TOB = "TOB";
	public final static String SCREEN_SGMD = "SGMD";
	public final static String SCREEN_SGML = "SGML";

	public final static String SAVE = "SAVE";
	public final static String SUBMIT = "SUBMIT";
	public final static String UPDATE = "Update";
	public final static String VALIDATE = "Validate";

	public final static String SIEBEL_SR_STATUS_COMPLETED = "Completed";
	public final static String SIEBEL_SR_STATUS_CANCELLED = "Cancelled";
	public final static String SIEBEL_SR_STATUS_RESOLVED = "Resolved";
	public final static String SIEBEL_SR_STATUS_RESTORE = "Restored";
	public final static String SIEBEL_SR_STATUS_PENDING = "Pending";
	public static final String SIEBEL_SR_STATUS_UPDATED = "Updated";
	public static final String SIEBEL_SR_STATUS_NEW = "New";
	public static final String SIEBEL_SR_STATUS_UNASSIGNED = "Unassigned";
	public final static String SIEBEL_SR_STATUS_IN_PROCESS = "InProcess";
	public final static String SIEBEL_SR_SUB_STATUS_CUSTOMER = "Customer";

	public static final String SIEBEL_SR_NUMBER_NOT_FOUND = "";
	
	public static final String SR_CREATION_GENERIC_CONTACT_FNAME="sr_creation_generic_contact_fname";
	public static final String SR_CREATION_GENERIC_CONTACT_LNAME="sr_creation_generic_contact_lname";
	public static final String SR_CREATION_GENERIC_CONTACT_PHONE="sr_creation_generic_contact_phone";
	public static final String SR_CREATION_GENERIC_CONTACT_EMAIL="sr_creation_generic_contact_email";
		
	public static final String ART_SR_NUMBER_NOT_FOUND = "Art SR number not found";
	public static final String ART_FAILURE_ACTION_REQUIRED= "Please login to the Global Registration Tool and check the registration ID error code or SR Note.";
	public final static String SIEBEL_SR_NO_NOTE_FOUND = "No action Required at this time";
	public final static String SIEBEL_SR_CREATION_ERROR = "Error";
	public final static String SIEBEL_SR_CREATION_ERROR_TIP = "An error was encountered in processing this request. No further action is required. Avaya will resubmit the request.";
	
	public final static String SIEBEL_SR_URGENCY_4_LOW = "4-Low";
	public final static String SIEBEL_SR_URGENCY_1_CRITICAL = "1-Critical";

	public static final String SIEBEL_SR_TYPE_INTERNAL = "Internal";
	
	public static final String SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION = "Install Base Creation";
	public static final String SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION = "Technical Registration";
	public static final String SIEBEL_SR_SUB_TYPE_SAL_RECORDS_BUILDING = "SAL Records Building";
	public static final String SIEBEL_SR_SUB_TYPE_SAL_CONNECTIVITY_AND_ALARM_TESTING = "SAL-Connectivity/Alarm Testing";
	public static final String SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION = "Final Record Validation";
	public static final String SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE = "Equip/Site Move Issue";
	//GRT 4.0 property for Equipment Move
	public static final String SIEBEL_SR_SUB_TYPE_EQM_ACTIVE_CONTRACTS = "Equip/Site Move Contract";
	public static final String SIEBEL_SR_SUB_TYPE_EQR_ACTIVE_CONTRACTS = "Equip Removal Active Contract";

	public final static String SYSTEM_MAIL_DESTINATION_SIEBEL = "Siebel";
	public final static String SYSTEM_MAIL_DESTINATION_ART = "ART";
	
	//Constants for Hibernate
	public static final String CXP_DATA_BASE = "hibernateCXPDB" ;
	public static final String CAT_DATA_BASE = "hibernateCATDB" ;
	public static final String GRT_DATA_BASE = "hibernateLocal";
	public static final String SIEBEL_DATA_BASE = "hibernateCXPDB";
	public static final String FMW_DATA_BASE = "hibernateFMWDB";
	
	//Constants used for ART Interface
    public static final int MAX_ALLOWABLE_TECHNICAL_REGISTRATION_SUBMIT = 2;    
	public static final String MBT = "MBT";
	public static final String MODEM = "MODEM";
	public static final String IP = "IP";
	public static final String OUTBOUND_PREFIX = "001";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String T = "T";
	public static final String OPT_TYPE = "optype";
	public static final String SID = "sid";
	public static final String MID = "mid";
	public static final String IN_ADS = "inads";
	public static final String OSSNO = "ossno";
	public static final String OSSNO_IP = "ossno_ip";
	public static final String IPADD = "ipadd";
	public static final String PVT_IP = "prvip";
	public static final String FOO_OPTION = "foption";
	public static final String FL = "fl";
	public static final String SE_CODE = "scode";
	public static final String SE_CODE_IPO = "scode";
	public static final String SE_ID="sname";
	public static final String PROD_TYPE = "prodtype";
	public static final String BP = "bp";
	public static final String DT_TYPE = "dtype";
	public static final String AFID = "afid";
	public static final String NICK_NAME = "nickname";
	public static final String AORIG = "aorig";
	public static final String RELEASE_NO = "relno";
	public static final String GRT_ID = "grtid";
	public static final String USRID = "usrid";
	public static final String EMAIL = "email";
	public static final String DN = "DN";
	public static final String FN = "FN";
	public static final String PR = "PR";
	public static final String RN = "RN";
	public static final String DU = "DU";
	
	public static final String TECHNICAL_REGISTRATION_ID = "techRegId";
	public static final String AGENT = "A";
	public static final String TECHNICAL_REGISTRATION_ERROR="-1";
	public static final String AAC = "AAC";
	public static final String CM = "CM";
	public static final String ART_SR_NO="artsr";
	public static final String STANDARD = "Standard";
	public static final String RNDPWD = "rndpwd";
	public static final String ISDOCR = "isDOCR";
	public static final String VPMSLDN="vpmsldn";
	
	
	// [AVAYA] 09-29-2011: Add constant for MBT Upgrade to ME (Start)
	public static final String MBT_UPGRADE_TO_ME = "MBT Upgrade to ME";
	// [AVAYA] 09-29-2011: Add constant for MBT Upgrade to ME (End)
	
	//Constants for hard code parameters.
	public static final String CSS_USER_PROFILE = "cssUserProfile";
	public static final String CSS_HOME_URL = "css_home_url";
	public static final String CXP_USER_NAME = "cxp_user_name";
	public static final String CAT_USER_NAME = "cat_user_name";
	//Environment variables
	public static final String SYSTEM_ENVIRONMENT_NAME = "grt.environment";
	public static final String DEV_ENVIRONMENT = "dev";
	public static final String TEST_ENVIRONMENT = "test";
	public static final String STAGING_ENVIRONMENT = "stag";
	public static final String PRODUCTION_ENVIRONMENT = "prod";
	
	//HTML variables
	public static final String VISIBLE = "visible";
	public static final String HIDDEN = "hidden";
	public static final String NONE = "none";
	public static final String BLOCK = "block";
	public static final String USER_ID_SYSTEM = "System";
	public static final String USER_ID_SIEBEL = "Siebel";
	public static final String USER_ID_ART = "ART";
	public static final String USER_ID_RF="RegistrationFacade";
	
	//EJB Variables
    public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
    public static final String SAL_REGISTRATION_REMOTE = "ejb.SalRegistrationFacadeRemoteHome";
    public static final String REGISTRATION_REMOTE = "ejb.RegistrationFacadeRemoteHome";
    public static final String EJB_JNDI = "ejb_jndi";
    
    //IPO Variables
    public static final String TECH_REG_REQ = "tech_reg_req";
    public static final String PASSWORD_RESET_REQ = "password_reset_req";
    public static final String ONBOARDING_XML_REQ = "onboarding_xml_req";
    public static final String PRODUCT_TYPE_IPO = "ipofc";
    public static final String ALARM_ID= "almid";
    public static final String SVR_NAME= "svrname";
    public static final String RELEASE_NO_IPO = "8.1";  
    public static final String SERVICE_NAME_IPO = "AVAYA_SUPPORT";
    public static final String ACCESS_TYPE_IPO = "IPO";
    public static final String ACCESS_TYPE_SSLVPN = "SSL/VPN";
    public static final String ACCESS_TYPE_RSAIP = "RASIP";
    public static final String ACCESS_TYPE_IP = "IP";
    public static final String ACCESS_TYPE_MODEM = "Modem";
    public static final String ACCESS_TYPE_SAL = "SAL";
    
    public static final String IMPLEMENTATION_TYPE_IPO = "IPO_INSTALL";
    public static final String ART_CALLBACK_PARAM = "callback_endpoint";
    public static final String DEFAULT_ART_SERVICE_NAME = "service_name";
    //Lavanya added
    public static final String GRT_REVISION = "grt_revision";
    public static final String DEFAULT_ART_USER = "grt";
    public static final String NUMBER_SUBMIT = "1";
    
    
    //Error Variables for IPO
    public static final String ART_ERROR_TYPE_RESPONSE_TIMEOUT = "timeoutError";
    public static final String ART_ERROR_TYPE_RESPONSE_CONNECTIVITY = "connectivityError";
    
    //Gateway choice variables for SAL Universal Product Registration
    public static final String STANDALONE_GATEWAY = "StandaloneGateway";
    public static final String EMBEDDED_GATEWAY = "EmbeddedGateway";
    public static final String SAL_PROD_TYPE = "salgw";
    public static final String SAL_SE_CODE = "SALGW";
    public static final String SAL_GATEWAY = "SALGW";
    public static final String SAL_VIRTUAL_GATEWAY = "VSALGW";
    public static final String SAL_TEMPLATE_NAME = "template";
    public static final String SAL_HARDWARE_SERVER_NAME = "hwsvrs";
    public static final String CUST_IP_ADDRESS = "custipaddress";
    
    
   //FMW DESTINATION
    public static final String SYSTEM_MAIL_DESTINATION_FMW = "FMW";
    
    // Syn call Error Codes
    public static final String fmw_errorCode ="100";
    public static final String acsbi_errorCode ="101";
    public static final String acsbi_errorDescription ="BEA-380000: Found";
    //public static final String art_error_380000 ="BEA-380000";
    //public static final String art_error_380002 ="BEA-380002";
    public static final String installBase_successCode="S";
    public static final String installBase_errorCode="E";
    public static final String sapDown_errorCode="1000";
    public static final String sapException_errorCode="1000";
    public static final String exception_errorcode="101";
    public static final String sapdestination_notfound="102";
    public static final String invalidrecords_selected="103";
    
    //  Syn call Error Codes
    public static final String EQR_successCode="S";
    public static final String EQR_errorCode="E";
    public static final String EQR_file_successCode="0";
    public static final String EQR_baddata_errorCode="1100";
    public static final String EQR_validation_errorCode="13";
    public static final String EQR_validation_successCode="14";
    
    // GRT 4.0 - Equipment Move
    public static final String EQR_MOVE_successCode="S";
    public static final String EQR_MOVE_errorCode="E";
    public static final String EQM_baddata_errorCode="1100";
    
    
    
    //File Poller return codes
    
    public static final String installBasefile_successCode="0";
    public static final String installBasefile_baddata_errorCode="1100";
    public static final String unKnownExcep_errorCode="1099";
    
    //Issubmitted constants
    public static final String isSubmitted_true="0";
    public static final String isSubmitted_false="1";
    
    // boolean equi-valent constants
    public static final String BOOLEAN_TRUE="1";
    public static final String BOOLEAN_FALSE="0";
    
    //Transaction log 
    public static final String GRT2ART = "GRT2ART";
    public static final String GRT2SAP = "GRT2SAP";
    
    public static final String GRT2Siebel = "GRT2Siebel";
	
	//SAP Destination Prefix
    public static final String IBoxSoldPrefix="00";
    public static final String BBoxSoldPrefix="000";
    
    public static final String IBoxDestination="I";
    public static final String BBoxDestination="B";
    
    //CM templates
    public static final String EMBD_CM_MAIN = "Embedded CM Main"; 
    public static final String SMPL_CM_MAIN_CORE = "Simplex CM Main w/ Survivable Core";
    public static final String DPLX_CM_MAIN_CORE = "Duplex CM Main w/ Survivable Core";
    public static final String EMBD_CM_SERVER_REMOTE = "Embedded CM Server w/ Survivable Remote";
    public static final String SMPL_CM_SERVER_REMOTE = "Simplex CM Server w/ Survivable  Remote";
    
    public static final String materialEmptyListRC ="ME";
    
    //Suggestions URL on GRT landing page
    public static final String GRT_SUGGESTIONS_URL = "grt_suggestions_url";
    public static final String GRT_SUGGESTIONS_URL_FOR_UI = "grt_suggestions_url_for_ui";
    
    // Administrator - Alerts
    public static final String GRT_MEGA_USER = "is_Mega_User";
    public static final String GRT_IS_MEGA_USER = "1";
    
    //Begin Existing & SOIR - IB Creation Pagination
    public static final String EIB_PAGINATION_BUTTON_CLICKED = "eibPaginationButtonClicked";
    public static final String SOIR_PAGINATION_BUTTON_CLICKED = "soirPaginationButtonClicked";    
    //End Existing & SOIR - IB Creation Pagination
    public static final String SALGW_MC = "salGatewayMaterialCode";
    public static final String SALGW_PRODUCT_TYPE = "salgw";
    
    //  SAP call Error Codes
    public static final String TR_successCode="S";
    public static final String TR_ALARM_successCode="200";
    public static final String TR_errorCode="E";
    public static final String SAL_CONCENTRATOR = "salConcentrator";
    
    // ACTION TYPES {U, R}
    public static final String ACTION_TYPE_U = "U";
    public static final String ACTION_TYPE_R = "R";
    
    public static final String TECHREGDETAIL_TR = "TR";
    public static final String TECHREGDETAIL_SL = "SL";
    
    public static final String PRIMARY_SE_ID = "primarySEID";
    public static final String SECONDARY_SE_ID = "secondarySEID";
    
    public static final String OFFBOARD_DEVICE_SERVICE_LEVEL = "ALARMING_OFF";
    public static final String OFFBOARD_DEVICE_SERVICE_TYPE = "MANAGE_SYSTEM_MOMO";
    
    public static final String SITE_REGISTRATION = "siteRegistration";
    
    public static final String SAL_GATEWAY_FOR_SAL_CONCENTRATOR = "SALGateway";
    public static final String DEVICE_FOR_SAL_CONCENTRATOR = "Device";
    
	public static final String NONSAL = "NONSAL";
	public static final String SAL_MIGRATION = "SALMigration";
	public static final String SAL_GATEWAY_FOR_TRD = "SALGateway";
	public static final String IP_OFFICE = "IPOffice";
	
	public static final String SAL_GATEWAY_ERROR_MESG = "User do not have access to one or more Sold Tos: ";
	public static final String INVALID_SEID_ERROR_MESG = "Invalid SEID";
	public static final String SAL_GATEWAY_EMPTY_MESG = "";
	//public static final String NO_GATEWAYS_MESG = "No Gateways added to list";
	public static final String NO_GATEWAYS_MESG = "No Gateway found on Sold To/SEID entered.";
	public static final String SOLD_TO_IS_EMPTY = "Sold To entered by you is empty";
	public static final String IS_FROM = "isFrom";
	public static final String REGISTRATION_TYPE = "registrationType";
	
	public static final String TECH_REG = "Tech On-Boarding";
	public static final String DEFAULT_CONNECTIVITY_TYPE = "db";
	public static final String FULL = "Full";
	public static final String BOTH = "Both";
	
	public static final String GRT_RSS_OR_EPN_SERVEY_URL = "rssOrEpnServey_url";
	public static final String GRT_RSS_OR_EPN_SERVEY_URL_FOR_UI = "epnServey_url_for_ui";
	
	public static final String DIRECT_CUSTOMER = "C";
	
	public static final String FROM_TR_OB_DETAIL = "fromTrObDetail";
	public static final String IS_AVAYA_ASSOCIATE = "isAvayaAssociate";
	
	public static final String SOIR = "SOIR";
	public static final String ALARM_AND_CONNECTIVITY_DISABLED = "alarmAndConnectivityDisabled";
	
	public static final String REMOTE_DEVICE_TYPE_ESS = "ESS";
	public static final String REMOTE_DEVICE_TYPE_LSP = "LSP";
	public static final String REMOTE_DEVICE_TYPE_GATEWAY = "GATEWAY";
	
	public static final String RESPONSE_CODE_W = "W";
	public static final String RESPONSE_CODE_S = "S";
	public static final String RESPONSE_CODE_E = "E";
	public static final String DEFAULT_ACCOUNT_GRANT_PERIOD = "default_grant_period_in_days";
	
	// GRT Variables
	public static final String NONE_OF_THE_ABOVE = "None of the above";
	public static final String GRT_SSR_REQUEST = "GRT-SSR Request";
	public static final String B_BOX_SALES_DISTRICT_CODE_INTERNAL = "000005";
	public static final String TR_DETAILS_ACTIVE = "trDetailsActive";

	
	public static final String PROPERTY_ACSBI_EPN_SURVEY_URL = "acsbi_epn_survey_url";
	
	public static final String MAIL_DATA_CMMAINPPNSOLDTOID = "CMMAINPPNSOLDTOID";
	public static final String VCODES_STR = "vCodesList";
	
	public static final String GRT_HELP_URL= "grt_help_url" ;
	
	// TR Config Product Type variables
	public static final String AACC = "aacc";
	public static final String AACC_TEMPLATE = "aacc_ms";
	public static final String SPTEM = "sptem";
	public static final String RADVSN = "radvsn";
	public static final String MX = "mx";
	public static final String COLENV = "colenv";
	
	public static final String SECODE_MXCB = "mxcb";
	public static final String SECODE_MXCV = "mxcv";
	public static final String SECODE_MXMSI = "mxmsi";
	
	public static final String NA = "N/A";
	public static final String NUMBER_ZERO = "0";
	public static final String NUMBER_ONE = "1";
	public static final String NUMBER_TWO = "2";
	public static final String SUCCESS = "success";
	public static final String REGISTRATION_LIST = "registrationList";
	
	public static final String STEPB_DETAIL_VIEW = "stepbDetailView";
	public static final String B_BOX_SALES_ORG                                       = "B001";
    public static final String COUNTRY_US                                            = "US";
    public static final String COUNTRY_CANADA                                        = "CA";
    public static final String COUNTRY_USA                                            = "USA";
    public static final String TOKEN_SUCESS_CODE                                     = "0";
    public static final String TOKEN_INVALID_CODE                                    = "1110";
    public static final String TOKEN_EXPIRED                                         = "EXPIRED";
    public static final String TOKEN_REDEEMED                                        = "REDEEMED";
    public static final String TOKEN_ACTIVE                                          = "ACTIVE";
    public static final String TOKEN_CODELIVERY                                      = "C/D";
    public static final String TOKEN_CODELIVERY_SAP                                  = "CO-DEL";

    public static final String TOKEN_FORM                                            = "tokenForm";
    public static final String CURRENT_PAGE                                          = "currentpage";
    public static final String SOLD_TO_ERROR                                         = "soldToError";
    public static final String ACCOUNT_NAME                                          = "ACCOUNT_NAME";
    public static final String STREET                                                = "STREET";
    public static final String CITY                                                  = "CITY";
    public static final String REGION                                                = "REGION";
    public static final String COUNTRY                                               = "COUNTRY";
    public static final String ZIP                                                   = "ZIP";
    public static final String MIN_THRESHOLD                                         = "minThreshhold";
    public static final String TOP_RECORDS                                           = "topRecords";
    public static final String SOLD_TO_NOT_FOUND                                     = "The Sold-To/Functional Location that you entered is invalid, please check that you have entered it correctly and try again.";
    public static final String SOLD_TO_FL_NOT_FOUND                                  = "Sold To/Functional location not found";
    public static final String COUNTRY_US_ERROR                                      = "countryUSError";
    public static final String COUNTRY_NON_US_ERROR                                  = "countryNonUSError";
    public static final String TOKEN_SEARCH_ERROR                                    = "tokenSearchError";
    
    public static final String ACCOUNT_FORM                                          = "accountForm";
    public static final String ACCOUNT_SI                                            = "SI";
    public static final String ACCOUNT_SP	                                         = "SP";    
    public static final String SEARCH_NAME                                           = "Name";
    public static final String SEARCH_STREET                                         = "Street";
    public static final String SEARCH_CITY                                           = "City";
    public static final String SEARCH_REGION                                         = "Region";
    public static final String SEARCH_COUNTRY                                        = "Country";
    public static final String SEARCH_ZIP                                            = "Zipcode";
    public static final String USER_TYPE_BP											 = "B";
	public static final String AISL_SALES_ORG 										 = "4a5b";
	public static final String SOLD_TO_ACCOUNT_TYPE		 							 = "Sold-to party";
	public static final String JURISDICTION                                          = "jurisdiction";
	public static final String LOA_SOURCE                                            = "GRT";
	public static final String ACCOUNT_UPDATE                                        = "AccountUpdate";
		
	public static final String THRESOLD_SCALE                                        = "2";
	public static final String MAIL_DATA_CMPOLLABLESECODESEID 						 = "CMPOLLABLESECODESEID";
	public static final String LOA_ENDPOINT											 = "loa_endpoint";
	
	public static class PRODUCTTYPE {
		public static final String IP600											 = "ip600";
		public static final String DFONE 											 = "dfone";
		public static final String CMEXP 											 = "cmexp";
		public static final String BLADE											 = "blade";
		public static final String G700											     = "g700";
		public static final String S84IP											 = "s84ip";
		public static final String S85IP 											 = "s85ip";
		public static final String S87MC 											 = "s87mc";
		public static final String S88IP											 = "s88ip";
		public static final String RADVSN											 = "radvsn";
	}
	
	public static class SECODE {
		public static final String ACC_ACCMST											 = "ACCMST";
		public static final String ACC_ACCMSL 											 = "ACCMSL";
		public static final String ACC_ACCWSW 											 = "ACCWSW";
		public static final String ACC_ACCWTK											 = "ACCWTK";
		public static final String VCM											         = "VCM";
		public static final String ACCCM											     = "ACCCM";
		public static final String AACEMS 											     = "AACEMS";
		public static final String CNTXSR 											     = "CNTXSR";
		public static final String ACDBA 											     = "ACDBA";
		public static final String WEBRTC 											     = "WEBRTC";
		public static final String WAECE 											     = "WAECE";
		public static final String RTSCE 											     = "RTSCE";
		public static final String PSEDP												 = "PSEDP";
	}
	
	public static final String DUPLICATE_SID_SECODE                                  = "duplicateSidSeCode";
	public static final String SID_MID_VALIDATED                                     = "sidMidValidated";
	public static final String INVALID_SID                                           = "invalidSid";
	public static final String INVALID_SID_MID                                       = "invalidSidMid";
	public static final String SID_STANDBY_EXISTS                                    = "sidStandByAlreadyExists";
	public static final String MID_CONSUMED                                          = "midConsumed";
	public static final String FL_NOTMATCHING1                                       = "flNotMatching1";
	public static final String FL_NOTMATCHING2                                       = "flNotMatching2";
	public static final String FL_NOTMATCHING3                                       = "flNotMatching3";
	public static final String AACE_SID_MISSING                                      = "aaceSidMissing";
	public static final String HIGH_AVAILABILITY                                     = "HA";
	public static final String BASE_SERVER                                           = "BASE SERVER";
	public static final String MEDIA_SERVER                                          = "MEDIA SERVER";
	
	public static final String HTTP_200                                              = "200";
	public static final String NOT_NULL                                              = "NotNull";
	
	public static final String CAAS                                                  = "Caas";
	public static final String AUXMC                                                 = "AUXMC";
	public static final String MISSING_MAIN                                          = "missingMain";
	
	public static final String emailSuccess                                          = "emailSuccess";
	public static final String emailError                                            = "emailError";
	public static final String OEFC_TOKEN_REDEMPTION                                 = "OEFC_TOKEN_REDEMPTION";
	
	public static final String EXCLUSION_SOURCE_KMAT								 = "KMAT";
	public static final String EXCLUSION_SOURCE_PLDS								 = "PLDS";
	public static final String EXCLUSION_SOURCE_DEFECTIVE							 = "DEFECTIVE";
	public static final String EXCLUSION_SOURCE_BLUE								 = "BLUE";
	public static final String EXCLUSION_SOURCE_NMPC								 = "NMPC";
	public static final String EXCLUSION_SOURCE_ESNA								 = "ESNA";
	
	public static final String ACCOUNT_TYPE_SOLDTO								     = "SOLDTO";
	public static final String ACCOUNT_TYPE_SHIPTO								     = "SHIPTO";
	public static final String VCODE_V00328								     		 = "V00328";
	public static final String PART_TOOL_ERROR_MSG								     = "Address validation failed";
	
	public static final String VSALGW_DESCRIPTION									 = "Virtual SAL Gateway";
	
	public static final String STATUS_NEVER											 = "Never";
	public static final String STATUS_BAD											 = "Bad";
	public static final String STATUS_NULL											 = "null";
	
	public static final String SPECIAL_NOTE_SSLVPN									 = "SSL-VPN";
	
	
	public static final String ACCOUNT_TYPE_HIERARCHY								 = "Hierarchy node";
	public static final String ACCOUNT_TYPE_UCM_AVAYA_HN							 = "UCM Avaya HN";
	public static final String ACCOUNT_TYPE_UCM_GDU_HN								 = "UCM GDU HN";
	public static final String ACCOUNT_TYPE_SHIPTO_PARTY							 = "Ship-to party";
	public static final String ACCOUNT_TYPE_SOLDTO_PARTY							 = "Sold-to party";
	
	public static final String IPO_ACCESS_TYPE_OTHER				 				 = "Other";
	public static final String SIEBEL_ASSET_UPDATE				 				 	 = "AssetUpdate";
	public static final String IPO_TOKEN_THRESHOLD_RELEASE							 = "IPO_TOKEN_THRESHOLD_RELEASE";
	
	public static final String IB_QUEUE							 					 = "ibaseQueue";
	public static final String EQR_QUEUE							 				 = "eqrQueue";
	public static final String SIEBEL_UPDATE_QUEUE							   		 = "siebelUpdateQueue";
	public static final String APPLICATION_MODE_PROD							   	 = "PROD";
// Added for gift card2.0
	public static final String SME_COMMUNICATIONS							   		 = "SME Communications";
	
	public static final String CAT_LINK = "<span><a target='_blank' href='https://support.avaya.com/cat'> <u>here</u>.</a></span>";
		
	public static final String BP_NOT_AUTHORIZED_FOR_SOLD_TO = "You do not have permissions to perform operations in GRT for the " +
			"Sold-To/Functional Location you entered, please check that you have entered it correctly and try again." + "<br style='inline'></br> Partners can request registration permissions from a customer by using the Customer Authorization Tool (CAT). " +
					"For more information on CAT, please visit the CAT support site by clicking" + CAT_LINK;
	public static final String INVALID_SOLD_TO ="The Sold-To/Functional Location that you entered is invalid, please check that you have entered it correctly and try again.";
	
	public static final String CUST_NOT_AUTHORIZED_FOR_SOLD_TO ="You do not have permissions to perform operations in GRT for the Sold-To/Functional Location you entered, please check that you have entered it correctly and try again.<br style='inline'></br>"+

                                            "Customers can manage Sold-To/Functional Location permissions at sso.avaya.com. Please log in to that website with your SSO,  select the tab for \"SOLD TO NUMBERS\" and proceed from there.";
	public static final String SIEBEL_ACC											 = "SIEBEL_ACC";
	public static final String SIEBEL_SR											 = "SIEBEL_SR";
	public static final String CONSTANT_1000										 = "1000";
	public static final String CONSTANT_1001										 = "1001";
	public static final String EMPTY_STRING										 	 = "";
	public static final String SAP_FAILURE_CODE = "1001";
	
	public static final String NOT_AUTHORIZED_FOR_SOLD_TO = "Please have authorization from the customer for the Sold To through CAT." +
	" Please check this "+CAT_LINK+" for further Information.";
	
	public static final String JMSXDELIVERYCOUNT="JMSXDeliveryCount";
	
    public static final String SAL_RECORDS_BUILDING = "SAL Records Building";
    public static final String SAL_CONNECTIVITY_AND_ALARM_TESTING = "SAL-Connectivity / Alarm Testing";

    //HomePage announcements
    public static final String ANN_DATE_CURRENT_FORMAT = "MMddyyyy";
    public static final String ANN_DATE_REQ_FORMAT = "dd MMM yyyy";
    
    //For UI data
    public static final String MAP_FOR_UI_DATA = "uiDataMap";
    
    //Properties for Graph Creation
    public static final String registrationsCompleted = "All Registrations Completed Status (last 3 months)";
    public static final String registrationsSaved = "All Registrations Saved Status (last 3 months)";
    public static final String registrationsTOB = "Technical Registrations InComplete Status (last 3 months)";
    
  //GRT 4.0 Changes
    public static final String NO_CONNECTIVITY="No Connectivity";
    public static final String REGISTRATIONID ="registrationId";
    public static final String SOLDTOID ="soldToId";
    public static final String COMPANY ="company";
    public static final String COMPANYPHONE="companyPhone";
    public static final String SITECOUNTRY="siteCountry";
    public static final String ADDRESS1="address1";
    public static final String ADDRESS2="address2";
    public static final String CITY1="city";
    public static final String STATE="state";
    public static final String ZIP1="zip";
    public static final String REGISTRATIONIDENTIFIER="registrationIdentifier";
    public static final String FIRSTNAME="firstName";
    public static final String LASTNAME="lastName";
    public static final String REPORTEDPHONE="reportedPhone";
    public static final String REPORTEDEMAIL="reportedEmail";

    public static final String ONSITEFIRSTNAME="onsiteFirstname";
    public static final String ONSITELASTNAME="onsiteLastname";

    public static final String ONSITEPHONE="onsitePhone";
    public static final String ONSITEEMAIL="onsiteemail";
    public static final String REGISTRATIONNOTES="registrationNotes";
    public static final String SOLDTOERROR="soldToError";
    public static final String MATERIALENTRYLIST_STRING="materialEntryList";
    public static final String EXISTINGINSTALLBASELIST="existingInstallBaseList";
    
    public static final String BACKEND_ERROR_MSG="error_msg";
    public static final String RET_LIST="retList";
    public static final String MATERIAL_CODE_MAP="materialCodeMap";
    
    //Install base Reg.
    public static final String BANNERSUBMITTEDDATE="bannerSubmittedDate";
    public static final String BANNERSRNUMBER="bannerSrNumber";
    public static final String BANNERSTATUS="bannerStatus";
    public static final String BANNERSUBSTATUS="bannerSubStatus";
    public static final String BANNERCOMPLETEDDATE="bannerCompletedDate";
    public static final String PENDINGORDERS="pendingOrders";
    public static final String MATERIALCODE="materialCode";
    public static final String INITIALQUANTITY="initialQuantity";
    public static final String DESCRIPTION="description";
    public static final String PRODUCTLINE="productLine";
    public static final String SERIALNUMBER="serialNumber";
    public static final String TECHNICALLYREGISTERABLE="technicallyRegisterable";
    public static final String SELECTFORREGISTRATION="selectforRegistration";
    public static final String ERRORDESCRIPTION="errorDescription";
    public static final String IPOFLAGIBASEJSP="ipoFlagIbaseJsp";
    public static final String MATERIALENTRYVERSION="materialEntryVersion";
    public static final String UPLOADMESSAGE="uploadMessage";
    public static final String IB_SUBMIT_MSG="installBaseMessage";
    public static final String IB_SUBMIT_RETURN_CODE="installBaseReturnCode"; 
    public static final String SELECT_UNSELECT_ALL_MATERIAL = "selectAndUnselectAllMeterial";
    
    //UI Navigation Related Properties
    public static final String SITE_CONTACT_PAGE="SITE_CONTACT_VALIDATION_PAGE";
    public static final String REGISTRATION_LIST_PAGE="REGISTRATION_LIST_PAGE";
    public static final String TOB_CONFIG_PAGE = "TOB_CONFIG_PAGE"; 
    
    public static final String SAVESITEREG="saveSiteReg";
    public static final String MESSAGE="message";
    public static final String ACTVESRCOLUMNONLY="actveSRColumnOnly";
    public static final String BANNERSRLABEL="bannerSrLabel";
    public static final String IBVIEWFILEDOWNLOADED="IBViewFileDownloaded";
    public static final String MATERIALSAFTEREXCLUSIONLIST="materialsAfterExclusionList";
    public static final String BANNERACTIVECONTRACTSRNUMBER="bannerActiveContractSrNumber";
    public static final String MATERIALENTRYLIST="materialEntryList";
    
    
  //Technical Onboarding Constants
    public static final String READY_FOR_TR_DIV_FLAG="readyForTRDivFlag";
    public static final String TRED_LIST_DIV_FLAG = "tredListDivFlag";
    public static final String TRED_PROD_DIV_FLAG = "tredProdDivFlag";
    public static final String REG_SUMMARY_LIST = "registrationSummaryList";
    public static final String REG_SUMMARY_LIST_REGISTRABLE = "registrationSummaryListRegistrable";
    public static final String NAVIGATE_TO = "navigateTo";
    public static final String EXIST_TOB_LIST = "existingTOBList"; 
    public static final String REGISTRATION_LIST_DATA = "registrationListData";
    
    
    public static final String PRIMARY = "primary";
    public static final String POPUP_HIDDEN_VALUE = "popUpHiddenValue";
    public static final String SECONDARY = "secondary";
    public static final String IS_MAIN_SEID_FLAG = "isMainSeidFlag";
    
    public static final String CM_MAIN_FLAG = "cmMainFlag"; 
    public static final String READ_ONLY = "readOnly";  
    
    public static final String ALARM_ELIGIBLE = "alarmEligible"; 
    public static final String SCREEN_NAME = "screenName"; 
    public static final String SID_MID_ERROR_FLAG = "sidMidErrorFlag"; 
    public static final String SID_MID_REQUIRED_FLAG = "sidMidRequiredFlag"; 
    public static final String TEMP_SID = "tempSid"; 
    public static final String SP_RELEASEFLAG = "spReleaseFlag"; 
    public static final String CM_MAIN_SOLDTO = "cmMainsoldTo"; 
    public static final String PRIVATE_IPELIGIBLE_FLAG = "privateIpEligibleFlag"; 
    public static final String SEID_OF_VOICE_PORTAL_FLAG = "seidOfVoicePortalFlag"; 
    
    public static final String TEMP_MID = "tempMid"; 
    public static final String PRODUCT_TYPE = "productType"; 
    public static final String CM_PRODUCT = "cmProduct"; 
    public static final String SPECIAL_NOTE = "specialNote"; 
    public static final String USERNAME_PASSWORD_FLAG = "userNamePasswordFlag"; 
    public static final String LSSORESS_LOWER_VERSION_FLAG = "lssOrESSLowerVersionFlag"; 
    public static final String UPGRADED_MAIN_CMUSER_FASID_FLAG = "upgradedMainCMuseRFASIDFlag"; 
    public static final String SOLDTO_ID = "soldToId"; 
    public static final String TECHNICAL_REGISTRATION_SUMMARY = "technicalRegistrationSummary"; 
    public static final String GROUP_IDS = "groupIds"; 
    public static final String TOB_RESUBMIT_FLAG = "tobResubmitFlag"; 
    public static final String TR_CONFIG = "trConfig"; 
    public static final String SOLUTION_ELEMENTID = "solutionElementId"; 
   
    public static final String FAILED_SEID_IS_NOTNULL = "failedSeidIsNotNull"; 
    public static final String MAIN_SEID_FLAG = "mainSeIdFlag"; 
    public static final String SALMIGRATION_FLAG = "salMigrationFlag"; 
    public static final String SOLDTO = "soldTo"; 
    public static final String SAL_SEID = "salSeId"; 
    public static final String COUNT = "count"; 
    public static final String SAL_GATEWAY_MIGRATIONLIST = "salGatewayMigrationList"; 
    public static final String ELIGIBLE_ACCESSTYPES_LIST = "eligibleAccessTypesList"; 
    public static final String PRODUCT_CONFIGURATION_FLAG = "productConfigurationFlag"; 
    public static final String SOFTWARE_RELEASE = "softwareRelease"; 
    public static final String SOFTWARE_RELEASE_FLAG = "softwareReleaseFlag"; 
    public static final String CONNECTIVITIES = "connectivities"; 
    public static final String ALARM_ORIGINATION_FLAG = "alarmOriginationFlag"; 
    public static final String ALARM_ORIGINATIONS = "alarmOriginations"; 
    public static final String SPRELEASE_FLAG = "spReleaseFlag"; 
    public static final String SPRELEASE = "spRelease";
    
    public static final String IPADDRESS_FLAG = "ipAddressFlag"; 
    public static final String OUTBOUNDPREFIX_FLAG = "outboundPrefixFlag"; 
    public static final String DIALIN_FLAG = "dialInFlag"; 
    public static final String SIDMID_SHOW_FLAG = "sidMidShowFlag"; 
    public static final String RANDOM_PASSWORD_FLAG = "randomPasswordFlag"; 
    
    public static final String SAMPBOARD_UPGRADED_FLAG = "sampBoardUpgradedFlag"; 
    public static final String CHECK_IF_SES_EDGE_FLAG = "checkIfSESEdgeFlag"; 
    public static final String AUTH_FILEID_FLAG = "authFileIDFlag"; 
    public static final String HARDWARE_SERVER_FLAG = "hardwareServerFlag"; 
    public static final String HARDWARE_SERVER = "hardwareServer"; 
    public static final String JSON_WRAPPER = "data";
    public static final String VIEW_IB_FILE_NAME = "IBViewFileName"; 
    
    public static final String ACCESS_TYPE_FLAG = "accessTypeFlag"; 
    public static final String TEMPLATE_FLAG = "templateFlag"; 
    public static final String RELEASES_MAP = "releasesMap";
    public static final String SID_MID_STATUS = "sidMidStatus";
    
    //GRT 4.0 23-June
    
    public static final String TOB_SSL_VPN_NOT_ALLOWED_CODE="301";
    public static final String TOB_EXCEPTION_FINDING_SR_CODE="302";
    public static final String TOB_STEP_B_STATUS_FOUND_CODE="303";
    public static final String TOB_EXCEPTION_FINDING_ALARMING_CODE="304";

    
    //GRT 4.0 24-March
    public static final String ERRORMESSAGE="errorMessage";
    public static final String REGISTRATIONSUMMARYLISTTRUPDATE="registrationSummaryListTRUpdate";
    public static final String IPOINVENTORYNOTLOADED="ipoInventoryNotLoaded";
    public static final String SRSUMMARYINFO="srSummaryInfo";
    
    // GRT 4.0 RV Action Types
    public static final String ACTION_TYPE_A="A";
    public static final String ACTION_TYPE_S="S";
    
    /*
     * START:: Registration List Constants
     * */    
        //Registration List Filter Keys
        public static final String REGISTRATION_ID_FILTER = "registrationId_f";
        public static final String REQUESTER_NAME_FILTER = "fullName_f"; //assembler_f_RequesterName
        public static final String REGISTRATION_IDENTIFIER_FILTER = "registrationIdentifier_f";// assembler_f
        public static final String GRT_NOTIFICATION_NAME_FILTER = "grtNotificationName_f"; //assembler_f_GrtNotificationName
        public static final String GRT_NOTIFICATION_EMAIL_FILTER = "requestEmailUIElement_f";//assembler_f_GrtNotificationEmail
        public static final String CREATED_DATE_FILTER = "createdDate_f";//assembler_f_CreateDate
        public static final String LAST_UPDATED_DATE_FILTER = "computedUpdateDate_f";// assembler_f_LastUpdatedDate
        public static final String LAST_UPDATED_BY_FILTER = "computedUpdateBy_f";//assembler_f_LastUpdatedBy
        public static final String REGISTRATION_TYPE_FILTER = "registrationTypeDesc_f";//assembler_f_RegistrationType
        public static final String IB_STATUS_FILTER = "installBaseStatusUIElement_f";//assembler_f_InstallBaseStatus
        public static final String TOB_STATUS_FILTER = "techRegStatusUIElement_f";// assembler_f_TechnicalOnboardingStatus 
        public static final String EQR_STATUS_FILTER = "finalRVStatusUIElement_f";// assembler_f_EquipmentRemovalStatus
        public static final String EQP_MOVE_FILTER = "eqrMoveUIElement_f";
        public static final String SOLD_TO_FILTER = "soldTo_f";// assembler_f_SoldTO
        public static final String CUST_NAME_FILTER = "requestingCompany_f";// assembler_f_CustomerName
        public static final String ACTIVE_REL_SR_FILTER = "activeSRUIElement_f";// assembler_f_ActiveRelatedSR
        public static final String EX_CANCELLED_STATUS_FILTER = "exCancelledStatus_f";
        
        //Sorting Keys
        public static final String REGISTRATION_ID_SORT = "registrationId_s";// assembler_s_RegistrationId
        public static final String REQUESTER_NAME_SORT = "fullName_s"; //assembler_s_RequesterName
        public static final String REGISTRATION_IDENTIFIER_SORT = "registrationIdentifier_s";// assembler_s
        public static final String GRT_NOTIFICATION_NAME_SORT = "grtNotificationName_s"; //assembler_s_GrtNotificationName
        public static final String GRT_NOTIFICATION_EMAIL_SORT = "requestEmailUIElement_s";//assembler_s_GrtNotificationEmail
        public static final String CREATED_DATE_SORT = "createdDate_s";//assembler_s_CreateDate
        public static final String LAST_UPDATED_DATE_SORT = "computedUpdateDate_s";// assembler_s_LastUpdatedDate
        public static final String LAST_UPDATED_BY_SORT = "computedUpdateBy_s";//assembler_s_LastUpdatedBy
        public static final String REGISTRATION_TYPE_SORT = "registrationTypeDesc_s";//assembler_s_RegistrationTypee
        public static final String IB_STATUS_SORT = "installBaseStatusUIElement_s";//assembler_s_InstallBaseStatus
        public static final String TOB_STATUS_SORT = "techRegStatusUIElement_s";// assembler_s_TechnicalOnboardingStatus 
        public static final String EQR_STATUS_SORT = "finalRVStatusUIElement_s";// assembler_s_EquipmentRemovalStatus
        public static final String EQP_MOVE_SORT = "eqrMoveUIElement_s";
        public static final String SOLD_TO_SORT = "soldTo_s";// assembler_s_SoldTO
        public static final String CUST_NAME_SORT = "requestingCompany_s";// assembler_s_CustomerName
        public static final String ACTIVE_REL_SR_SORT = "activeSRUIElement_s";//assembler_s_ActiveRelatedSR
        
        public static final String REC_TO_DISPLAY = "noOfRecToDisplay"; //assembler_crd
        public static final String CURRENT_PAGE_NO = "currentPageNo"; //assembler_p
        public static final String FILTER_OR_CLEAR_BTN = "filterOrClear";//assembler_f_a

    /*
     * END:: Registration List Constants
     * */
        
        //System to System SAL Gateway Installer Constants
        public static final String MANDATORY_FIELD_ERR_CODE = "400";
        public static final String MANDATORY_FIELD_STATUS_CODE = "E";
        public static final String INT_SERVER_ERR_CODE = "500";
        public static final String INT_SERVER_STATUS_CODE = "E";
        public static final String ART_SALGW_INST_SUCC_CODE_1 = "200";
        public static final String ART_SALGW_INST_SUCC_CODE_2 = "201";
        public static final String ART_SALGW_INST_SUCC_CODE_3 = "0";
        public static final String ART_SALGW_INST_ERR_CODE_1 = "600";
        public static final String ART_SALGW_INST_ERR_CODE_2 = "601";
        public static final String ART_SALGW_INST_ERR_CODE_3 = "602";
        public static final String ART_SALGW_INST_ERR_CODE_4 = "603";
        public static final String SNICK = "SNICK";
        public static final String RESPONSE_CODE = "RESPONSE_CODE";
        public static final String ERR_DETAILS = "ERR_DETAILS";
        
        //WebService Related Constants
        public static final String WS_SUCCESS_CODE = "200";
        public static final String WS_INTERNAL_ERROR_CODE = "500";
        public static final String WS_ERROR_CODE = "600";
        
        
        public static final String UNKNOWN = "Unknown";
        public static final String FAILURE = "failure";
        
        //GRT 4.0 SERIALIZED status flags
        public static final List<String> SERIALIZED_STATUS_LIST =  Arrays.asList("1000","Z003","1001","0001","Z001","Z002","0002");
		public static final String IP_OFFICE_OLDER_RELEASE = "older ipo version";
		public static final String IP_OFFICE_NO_EQUIPMENT_ONBOARDED = "no ipo equipment onboarded";
		
		//Code for Graph
		public final static String INPROCESS_VIEW = "INPROCESS_VIEW";
		public final static String AWAITING_VIEW = "AWAITING_VIEW";
		public final static String SAVED_VIEW = "SAVED_VIEW";
		public final static String NOTINITIATED_VIEW = "NOTINITIATED_VIEW";
		
		//GRT 4.0 : Registration List Page - Registration type to be shown on View
		public final static String FULLREGISTRATION = "End to End Registration";
		public final static String INSTALLBASEONLY = "Install Base Creation Only";
		public final static String TECHNICALREGISTRATIONONLY = "Technical Onboarding Only";
		public final static String EQUIPMENTREMOVALONLY = "Record Validation Only";
		public final static String EQUIPMENTMOVEONLY = "Equipment/Site Move Only";
		public final static String SALMIGRATION = "SAL Migration Only";
		public final static String IPOFFICE = "IP Office";

		//Open SR Popup
		public final static String RESPONSE = "RESPONSE";
		public final static String REGID = "REGID";
		public final static String REGIDNUM = "Reg ID#";
		
		public final static String SAL_DEVICE_NOT_FOUND = "DeviceNotFound";
		public final static String SAL_DEVICE_FOUND = "Found";
}
