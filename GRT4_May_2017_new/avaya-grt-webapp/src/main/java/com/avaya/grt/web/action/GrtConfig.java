package com.avaya.grt.web.action;

import java.util.HashMap;
import java.util.List;

public class GrtConfig {

	private String service_name;
	private String grtSuggestionsUrl;
	private String grtHelpUrl;
	private String rssOrEpnServeyUrl;
	private String duplicateSidSeCode;
	private String sidMidValidated;
	private String invalidSid;
	private String sidStandByAlreadyExists;
	private String midConsumed;
	private String flNotMatching1;
	private String flNotMatching2;
	private String flNotMatching3;
	private String aaceSidMissing;
	private String missingMain;
	
	private int registrationListRowNum;

	/* UI related Config */
	//Links for Support menu
	private List<HashMap<String,String>> supportMenu;
	//announcements title
	private String announcementsTitle;
	//Links for main menu
	private List<HashMap<String,String>> mainMenu;
	//Footer Links
	private List<HashMap<String,String>> footerLinks;
	//Recent Activity Images
	private List<String> recentActivities;
	//New registration Links
	private List<HashMap<String,String>> createNewRegistrationLinks;
	
	private String ibTitle;	
	private String ipossTokenRedemptionTitle;
	private String accountCreation;	
	private String tokenRedemption;
	private String ipossRegistration;
	private String adminLinkTitle;
	
	//registration management Links
	private List<HashMap<String,String>> registrationManagementLinks;
	private String remoteSiteSurveyOrEPNSurvey;
	private HashMap<String,Object> trainings;
	private HashMap<String,Object> helps;
	private HashMap<String,Object> tools;
	
	//Other properties
	private String address;
	private String copyRight;
	
	private String exclusionSourcePLDS;
	private String exclusionSourceKMAT;
	
	//Properties for Validation And Information Messages
	private String onboardFileMsg;
	private String onboardFileErrMsg;
	private String updateEmailMsg;
	private String soldToExcludedMsg;
	private String soldToNotAuthMsg;
	private String soldToNotEligibleMsg;
	private String soldToNotValidMsg;
	private String soldToEmptyMsg;
	private String soldToAutoNoteMsg;
	private String agentUserTypeMsg;
	private String agentCustUserTypeMsg;
	private String agentBPUserTypeMsg;
	private String exclPldsErrMsg;
	private String offerCodeErrMsg;
	private String defectiveCodeErrMsg;
	private String blueCodeErrMsg;
	private String vsalgwErrMsg;
	private String EQR_successCode;
	private String EQR_errorCode;
	private String sapDown_errorCode;
	private String fmw_errorCode;
	private String exception_errorcode;
	private String sapdestination_notfound;
	private String prepareTechnicalOrderMaterialExclusionError1;
	private String prepareTechnicalOrderMaterialExclusionError2;
	private String materialEmptyListRC;
	private String BaseUnitError2;
	private String BaseUnitError;
	private String validBaseUunit;
	private String continueRegistration;
	private String uploadedFileSize;
	private String materialCodeExcluded;
	private String quantitySize;
	private String invalidSerialNumber;
	private String invalidSerialNumber18Char;
	private String validQuantity;
	private String quantityNotEmpty;
	private String invalidMaterialCode;
	private String invalidQuantity;
	private String formaterialcodes;
	private String materialCodeNotMatchWithSECode;
	
	private String tobTechnicalError1;
	private String tobDataError1;
	private String tobTechnicalError2;
	private String invalidSidMain;
	private String invalidAfid;
	private String updateRecordTitle;
	private String duplicateSalMsg1;
	private String duplicateSalMsg2;
	
	private String InstallBaseListSize;
	private String InstallBaseListSizeMsg;

	private String leadingZeroesNote;
	
	//Error Messages for Record Validation
	private String RVInvalidMC;
	private String RVDuplicateSN;
	private String RVInvalidSN;
	private String RVInvalidQty;
	private String RVEmptyQty;
	private String RVInvalidQtyWithSN;
	private String recordValidationError7;
	private String RVInvalidOption;
	//Error Messages for Material Code,  Record Validation 
	private String RVMCNotPresentInDB;
	private String RVExcludedMC;
	private String RVErrorMessage;
	private String RVSuccessMessage;
	
	private String stepBSuccessMsg;
	private String stepBFailureMsg;
	
	//GRT 4.0 Chnages
	private String managedElementsRemainder;
	private String managedElementsRemainderKB;
	private String noConnectivityMessage;
	
	
	//New arrival
	
	private String emptyRevisedQuantityMsg;
	private String selectOneEquipmentMsg;
	private String mcWithActiveRemovalWarning;
	private String submitConfirmMsg;
	private String saveLoadingTextMsg;
	private String submitLoadingTextMsg;
	private String confirmValidateTextMsg;
	private String cancelConfirmMsgHome;
	private String cancelConfirmMsgRegList;
	private String downloadTemplateMsg;
	private String validOptionMsg;
	private String largeQuantityErrorMsg;
	private String validatedStatusMsg;
	private String materialCodeLabel;
	private String blankSerialNumberErrorMsg;
	private String invalidMaterialCodeErrorMsg;
	private String validateSuccessMsg;
	private String validateErrorMsg;
	
	
	private String seibelTimeoutErrorMsg;
	private String noRecordErrorMsg;
	private String incorrectFileFormatErrMsg;
	//GRT 4.0 Changes
	private String incorrectHeaderErrMsg;
	private String hugeUpdateErrorMessage;
	private String noActionImportErrorMsg;
	private String saveErrorMsg;
	private String errorWhileProcessingErrMsg;
	private String sapDownErrMsg;
	private String processingFailedErrMsg;
	private String emptyMaterialListErrorMsg;
	private String unableToMapSoldToErrMsg;
	//Account creation
	private String minThreshhold;
	private String topRecords;
	
	//GRT 4.0 changes
	private String remainingQtyLessThanOne;
	private String noConnectivityPopUpMessage;
	private String connectivityPopUpMessage;
	private String cancelActionMsg;
	private String selectRecord;
	private String submitTOBData;
	
	//Defect 126
	private String equipmentMoveSaveMessage;
	private String equipmentMoveSaveMessage2;

	private String serchMidRange;
	private String searchLowRange;
	
	//salgw installer
	private String salgwInstDefaultVal;
	private String salgwInstEmailId;
	private String salgwInstErrMsg;
	private String salgwInstMandValidMsg;
	private String salgwInstDownMsg;
	
	//SalMigration
	private String salGatewayMaterialCode;
	private String salRequired;
	private String duplicateGateway;
	private String emptySTSEID;
	private String invalidST;
	private String invalidSEID;
	private String validSTSEID;
	private String gatewayAdded;
	private String selectAsset;
	
	//IPOfficeWithOnboarding
	private String ipoFileSize;
	
	
	//Account Update
	private String pleaseSelectARecordMsg;

	private String emptySoldToError;
	private String requiredNameCityCountryError;
	private String accountNameLengthError;
	private String cityLengthError;
	private String mandatoryAccNameStreetCityError;
	private String mandatorPhoneEmailNameError;
	private String streetLengthError;

	private String zipCodeLengthError;
	private String phoneNumLengthError;
	private String invalidPhoneNumberError;
	private String faxLengthError;
	private String emailLengthError;
	private String stateLengthError;
	private String cancelProcessMsg;
	private String waitWhileFetchingMsg;

	private String stateOrProvince;
	private String street2LengthError;
	private String phoneNoMax10Error;
	private String waitAccountUpdateMsg;	
		
		
	//Account Create		
	private String mandatoryBPLinkId;
	private String tokenLengthErrMsg;
	private String errorPleaseInsert;
	private String upto;
	private String chars;
	private String vatLengthErrMsg;
	private String tokenValidateAgainErrMsg;
	private String invalidSoldToErrMsg;
	private String mandatoryFnLnEmailPhone;
	private String fnLnLengthErrMsg;
	private String emailLengthErrMsg;
	private String invalidEmailErrMsg;
	private String phoneLength16ErrMsg;
	private String internationalPhNoErrMsg;
	private String fnLengthErrMsg;
	private String lnLengthErrMsg;
	private String phoneLength10ErrMsg;
	private String mandatoryNameStreetCityCountryErrMsg;
	private String accountNameLengthErrMsg;
	private String cityLengthErrMsg;
	private String nameAlphaNumnericErrMsg;
	private String streetAlphaNumericErrMsg;
	private String potsalAlphaNumericErrMsg;
	private String mandatoryAccNameStreetCityPostalErrMsg;
	private String streetLengthErrMsg;

	private String postalLengthErrMsg;
	private String mandatoryReasonForAccCreation;
	private String reasonForAccCreateMinLengthErrMsg;
	private String reasonForAccCreateMaxLengthErrMsg;
	private String selectAtlestOneRecord;
	private String enterAtlestOneEmailMsg;
	private String waitAccountCreateMsg;
	private String waitSendEmailMsg;
	private String noneOfabove;
	private String existingAccount;
	
	private String validationEmailInvalid;
	private String mandatoryToken;
	private String tokenAlphaNumericErrMsg;
	
	private String waitValidateTokenMsg;
	
	private String lssOrESSLowerVersionNote;
	private String upgradedMainCMuseRFASIDNote;
	
	//GRT 4.0 Changes
	private String noConnectityReTestMsg;
	
	private String sameMaterialCodeQuantity;
	private String serialMaterialQuantity;
	private String serviceUnitMaterialQuantity;

	public String getSameMaterialCodeQuantity() {
		return sameMaterialCodeQuantity;
	}
	public void setSameMaterialCodeQuantity(String sameMaterialCodeQuantity) {
		this.sameMaterialCodeQuantity = sameMaterialCodeQuantity;
	}
	public String getSerialMaterialQuantity() {
		return serialMaterialQuantity;
	}
	public void setSerialMaterialQuantity(String serialMaterialQuantity) {
		this.serialMaterialQuantity = serialMaterialQuantity;
	}
	public String getServiceUnitMaterialQuantity() {
		return serviceUnitMaterialQuantity;
	}
	public void setServiceUnitMaterialQuantity(String serviceUnitMaterialQuantity) {
		this.serviceUnitMaterialQuantity = serviceUnitMaterialQuantity;
	}
	public String getDuplicateSalMsg1() {
		return duplicateSalMsg1;
	}
	public void setDuplicateSalMsg1(String duplicateSalMsg1) {
		this.duplicateSalMsg1 = duplicateSalMsg1;
	}
	public String getDuplicateSalMsg2() {
		return duplicateSalMsg2;
	}
	public void setDuplicateSalMsg2(String duplicateSalMsg2) {
		this.duplicateSalMsg2 = duplicateSalMsg2;
	}
	public String getUpdateRecordTitle() {
		return updateRecordTitle;
	}
	public void setUpdateRecordTitle(String updateRecordTitle) {
		this.updateRecordTitle = updateRecordTitle;
	}
	public String getInvalidAfid() {
		return invalidAfid;
	}
	public void setInvalidAfid(String invalidAfid) {
		this.invalidAfid = invalidAfid;
	}
	public String getInvalidSidMain() {
		return invalidSidMain;
	}
	public void setInvalidSidMain(String invalidSidMain) {
		this.invalidSidMain = invalidSidMain;
	}
	public String getTobTechnicalError1() {
		return tobTechnicalError1;
	}
	public void setTobTechnicalError1(String tobTechnicalError1) {
		this.tobTechnicalError1 = tobTechnicalError1;
	}
	public String getTobDataError1() {
		return tobDataError1;
	}
	public void setTobDataError1(String tobDataError1) {
		this.tobDataError1 = tobDataError1;
	}
	public String getTobTechnicalError2() {
		return tobTechnicalError2;
	}
	public void setTobTechnicalError2(String tobTechnicalError2) {
		this.tobTechnicalError2 = tobTechnicalError2;
	}
	//iposs token redemption Links
	private List<HashMap<String,String>>ipossTokenRedemptionLinks;
	
	public String getExclPldsErrMsg() {
		return exclPldsErrMsg;
	}
	public void setExclPldsErrMsg(String exclPldsErrMsg) {
		this.exclPldsErrMsg = exclPldsErrMsg;
	}
	public String getAgentBPUserTypeMsg() {
		return agentBPUserTypeMsg;
	}
	public void setAgentBPUserTypeMsg(String agentBPUserTypeMsg) {
		this.agentBPUserTypeMsg = agentBPUserTypeMsg;
	}
	public String getOfferCodeErrMsg() {
		return offerCodeErrMsg;
	}
	public void setOfferCodeErrMsg(String offerCodeErrMsg) {
		this.offerCodeErrMsg = offerCodeErrMsg;
	}
	public String getDefectiveCodeErrMsg() {
		return defectiveCodeErrMsg;
	}
	public void setDefectiveCodeErrMsg(String defectiveCodeErrMsg) {
		this.defectiveCodeErrMsg = defectiveCodeErrMsg;
	}
	public String getBlueCodeErrMsg() {
		return blueCodeErrMsg;
	}
	public void setBlueCodeErrMsg(String blueCodeErrMsg) {
		this.blueCodeErrMsg = blueCodeErrMsg;
	}
	public String getAgentCustUserTypeMsg() {
		return agentCustUserTypeMsg;
	}
	public void setAgentCustUserTypeMsg(String agentCustUserTypeMsg) {
		this.agentCustUserTypeMsg = agentCustUserTypeMsg;
	}
	public String getAgentUserTypeMsg() {
		return agentUserTypeMsg;
	}
	public void setAgentUserTypeMsg(String agentUserTypeMsg) {
		this.agentUserTypeMsg = agentUserTypeMsg;
	}
	public String getSoldToAutoNoteMsg() {
		return soldToAutoNoteMsg;
	}
	public void setSoldToAutoNoteMsg(String soldToAutoNoteMsg) {
		this.soldToAutoNoteMsg = soldToAutoNoteMsg;
	}
	public String getSoldToEmptyMsg() {
		return soldToEmptyMsg;
	}
	public void setSoldToEmptyMsg(String soldToEmptyMsg) {
		this.soldToEmptyMsg = soldToEmptyMsg;
	}
	public String getSoldToNotValidMsg() {
		return soldToNotValidMsg;
	}
	public void setSoldToNotValidMsg(String soldToNotValidMsg) {
		this.soldToNotValidMsg = soldToNotValidMsg;
	}
	public String getSoldToNotEligibleMsg() {
		return soldToNotEligibleMsg;
	}
	public void setSoldToNotEligibleMsg(String soldToNotEligibleMsg) {
		this.soldToNotEligibleMsg = soldToNotEligibleMsg;
	}
	public String getSoldToNotAuthMsg() {
		return soldToNotAuthMsg;
	}
	public void setSoldToNotAuthMsg(String soldToNotAuthMsg) {
		this.soldToNotAuthMsg = soldToNotAuthMsg;
	}
	public String getSoldToExcludedMsg() {
		return soldToExcludedMsg;
	}
	public void setSoldToExcludedMsg(String soldToExcludedMsg) {
		this.soldToExcludedMsg = soldToExcludedMsg;
	}
	public String getUpdateEmailMsg() {
		return updateEmailMsg;
	}
	public void setUpdateEmailMsg(String updateEmailMsg) {
		this.updateEmailMsg = updateEmailMsg;
	}
	public String getOnboardFileErrMsg() {
		return onboardFileErrMsg;
	}
	public void setOnboardFileErrMsg(String onboardFileErrMsg) {
		this.onboardFileErrMsg = onboardFileErrMsg;
	}
	public String getOnboardFileMsg() {
		return onboardFileMsg;
	}
	public void setOnboardFileMsg(String onboardFileMsg) {
		this.onboardFileMsg = onboardFileMsg;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public String getGrtSuggestionsUrl() {
		return grtSuggestionsUrl;
	}
	public void setGrtSuggestionsUrl(String grtSuggestionsUrl) {
		this.grtSuggestionsUrl = grtSuggestionsUrl;
	}
	public String getGrtHelpUrl() {
		return grtHelpUrl;
	}
	public void setGrtHelpUrl(String grtHelpUrl) {
		this.grtHelpUrl = grtHelpUrl;
	}
	public String getRssOrEpnServeyUrl() {
		return rssOrEpnServeyUrl;
	}
	public void setRssOrEpnServeyUrl(String rssOrEpnServeyUrl) {
		this.rssOrEpnServeyUrl = rssOrEpnServeyUrl;
	}
	public String getDuplicateSidSeCode() {
		return duplicateSidSeCode;
	}
	public void setDuplicateSidSeCode(String duplicateSidSeCode) {
		this.duplicateSidSeCode = duplicateSidSeCode;
	}
	public String getSidMidValidated() {
		return sidMidValidated;
	}
	public void setSidMidValidated(String sidMidValidated) {
		this.sidMidValidated = sidMidValidated;
	}
	public String getInvalidSid() {
		return invalidSid;
	}
	public void setInvalidSid(String invalidSid) {
		this.invalidSid = invalidSid;
	}
	public String getSidStandByAlreadyExists() {
		return sidStandByAlreadyExists;
	}
	public void setSidStandByAlreadyExists(String sidStandByAlreadyExists) {
		this.sidStandByAlreadyExists = sidStandByAlreadyExists;
	}
	public String getMidConsumed() {
		return midConsumed;
	}
	public void setMidConsumed(String midConsumed) {
		this.midConsumed = midConsumed;
	}
	public String getFlNotMatching1() {
		return flNotMatching1;
	}
	public void setFlNotMatching1(String flNotMatching1) {
		this.flNotMatching1 = flNotMatching1;
	}
	public String getFlNotMatching2() {
		return flNotMatching2;
	}
	public void setFlNotMatching2(String flNotMatching2) {
		this.flNotMatching2 = flNotMatching2;
	}
	public String getFlNotMatching3() {
		return flNotMatching3;
	}
	public void setFlNotMatching3(String flNotMatching3) {
		this.flNotMatching3 = flNotMatching3;
	}
	public String getAaceSidMissing() {
		return aaceSidMissing;
	}
	public void setAaceSidMissing(String aaceSidMissing) {
		this.aaceSidMissing = aaceSidMissing;
	}
	public String getMissingMain() {
		return missingMain;
	}
	public void setMissingMain(String missingMain) {
		this.missingMain = missingMain;
	}
	public List<HashMap<String, String>> getSupportMenu() {
		return supportMenu;
	}
	public void setSupportMenu(List<HashMap<String, String>> supportMenu) {
		this.supportMenu = supportMenu;
	}
	public String getAnnouncementsTitle() {
		return announcementsTitle;
	}
	public void setAnnouncementsTitle(String announcementsTitle) {
		this.announcementsTitle = announcementsTitle;
	}
	public List<HashMap<String, String>> getMainMenu() {
		return mainMenu;
	}
	public void setMainMenu(List<HashMap<String, String>> mainMenu) {
		this.mainMenu = mainMenu;
	}
	public List<HashMap<String, String>> getFooterLinks() {
		return footerLinks;
	}
	public void setFooterLinks(List<HashMap<String, String>> footerLinks) {
		this.footerLinks = footerLinks;
	}
	public List<String> getRecentActivities() {
		return recentActivities;
	}
	public void setRecentActivities(List<String> recentActivities) {
		this.recentActivities = recentActivities;
	}
	public List<HashMap<String, String>> getCreateNewRegistrationLinks() {
		return createNewRegistrationLinks;
	}
	public void setCreateNewRegistrationLinks(
			List<HashMap<String, String>> createNewRegistrationLinks) {
		this.createNewRegistrationLinks = createNewRegistrationLinks;
	}
	public String getIbTitle() {
		return ibTitle;
	}
	public void setIbTitle(String ibTitle) {
		this.ibTitle = ibTitle;
	}
	public String getIpossTokenRedemptionTitle() {
		return ipossTokenRedemptionTitle;
	}
	public void setIpossTokenRedemptionTitle(String ipossTokenRedemptionTitle) {
		this.ipossTokenRedemptionTitle = ipossTokenRedemptionTitle;
	}
	public String getAccountCreation() {
		return accountCreation;
	}
	public void setAccountCreation(String accountCreation) {
		this.accountCreation = accountCreation;
	}
	public String getTokenRedemption() {
		return tokenRedemption;
	}
	public void setTokenRedemption(String tokenRedemption) {
		this.tokenRedemption = tokenRedemption;
	}
	public String getIpossRegistration() {
		return ipossRegistration;
	}
	public void setIpossRegistration(String ipossRegistration) {
		this.ipossRegistration = ipossRegistration;
	}
	public String getAdminLinkTitle() {
		return adminLinkTitle;
	}
	public void setAdminLinkTitle(String adminLinkTitle) {
		this.adminLinkTitle = adminLinkTitle;
	}
	public List<HashMap<String, String>> getRegistrationManagementLinks() {
		return registrationManagementLinks;
	}
	public void setRegistrationManagementLinks(
			List<HashMap<String, String>> registrationManagementLinks) {
		this.registrationManagementLinks = registrationManagementLinks;
	}
	public String getRemoteSiteSurveyOrEPNSurvey() {
		return remoteSiteSurveyOrEPNSurvey;
	}
	public void setRemoteSiteSurveyOrEPNSurvey(String remoteSiteSurveyOrEPNSurvey) {
		this.remoteSiteSurveyOrEPNSurvey = remoteSiteSurveyOrEPNSurvey;
	}
	public HashMap<String, Object> getTrainings() {
		return trainings;
	}
	public void setTrainings(HashMap<String, Object> trainings) {
		this.trainings = trainings;
	}
	public HashMap<String, Object> getHelps() {
		return helps;
	}
	public void setHelps(HashMap<String, Object> helps) {
		this.helps = helps;
	}
	public HashMap<String, Object> getTools() {
		return tools;
	}
	public void setTools(HashMap<String, Object> tools) {
		this.tools = tools;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCopyRight() {
		return copyRight;
	}
	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}
	public int getRegistrationListRowNum() {
		return registrationListRowNum;
	}
	public void setRegistrationListRowNum(int registrationListRowNum) {
		this.registrationListRowNum = registrationListRowNum;
	}
	public String getExclusionSourcePLDS() {
		return exclusionSourcePLDS;
	}
	public void setExclusionSourcePLDS(String exclusionSourcePLDS) {
		this.exclusionSourcePLDS = exclusionSourcePLDS;
	}
	public String getExclusionSourceKMAT() {
		return exclusionSourceKMAT;
	}
	public void setExclusionSourceKMAT(String exclusionSourceKMAT) {
		this.exclusionSourceKMAT = exclusionSourceKMAT;
	}
	public String getEQR_successCode() {
		return EQR_successCode;
	}
	public void setEQR_successCode(String eQR_successCode) {
		EQR_successCode = eQR_successCode;
	}
	public String getEQR_errorCode() {
		return EQR_errorCode;
	}
	public void setEQR_errorCode(String eQR_errorCode) {
		EQR_errorCode = eQR_errorCode;
	}
	public String getSapDown_errorCode() {
		return sapDown_errorCode;
	}
	public void setSapDown_errorCode(String sapDown_errorCode) {
		this.sapDown_errorCode = sapDown_errorCode;
	}
	public String getFmw_errorCode() {
		return fmw_errorCode;
	}
	public void setFmw_errorCode(String fmw_errorCode) {
		this.fmw_errorCode = fmw_errorCode;
	}
	public String getException_errorcode() {
		return exception_errorcode;
	}
	public void setException_errorcode(String exception_errorcode) {
		this.exception_errorcode = exception_errorcode;
	}
	public String getSapdestination_notfound() {
		return sapdestination_notfound;
	}
	public void setSapdestination_notfound(String sapdestination_notfound) {
		this.sapdestination_notfound = sapdestination_notfound;
	}
	public String getPrepareTechnicalOrderMaterialExclusionError1() {
		return prepareTechnicalOrderMaterialExclusionError1;
	}
	public void setPrepareTechnicalOrderMaterialExclusionError1(
			String prepareTechnicalOrderMaterialExclusionError1) {
		this.prepareTechnicalOrderMaterialExclusionError1 = prepareTechnicalOrderMaterialExclusionError1;
	}
	public String getPrepareTechnicalOrderMaterialExclusionError2() {
		return prepareTechnicalOrderMaterialExclusionError2;
	}
	public void setPrepareTechnicalOrderMaterialExclusionError2(
			String prepareTechnicalOrderMaterialExclusionError2) {
		this.prepareTechnicalOrderMaterialExclusionError2 = prepareTechnicalOrderMaterialExclusionError2;
	}
	public String getMaterialEmptyListRC() {
		return materialEmptyListRC;
	}
	public void setMaterialEmptyListRC(String materialEmptyListRC) {
		this.materialEmptyListRC = materialEmptyListRC;
	}
	public String getBaseUnitError2() {
		return BaseUnitError2;
	}
	public void setBaseUnitError2(String baseUnitError2) {
		BaseUnitError2 = baseUnitError2;
	}
	public String getBaseUnitError() {
		return BaseUnitError;
	}
	public void setBaseUnitError(String baseUnitError) {
		BaseUnitError = baseUnitError;
	}
	public String getValidBaseUunit() {
		return validBaseUunit;
	}
	public void setValidBaseUunit(String validBaseUunit) {
		this.validBaseUunit = validBaseUunit;
	}
	public String getContinueRegistration() {
		return continueRegistration;
	}
	public void setContinueRegistration(String continueRegistration) {
		this.continueRegistration = continueRegistration;
	}
	public String getUploadedFileSize() {
		return uploadedFileSize;
	}
	public void setUploadedFileSize(String uploadedFileSize) {
		this.uploadedFileSize = uploadedFileSize;
	}
	public String getMaterialCodeExcluded() {
		return materialCodeExcluded;
	}
	public void setMaterialCodeExcluded(String materialCodeExcluded) {
		this.materialCodeExcluded = materialCodeExcluded;
	}
	public String getQuantitySize() {
		return quantitySize;
	}
	public void setQuantitySize(String quantitySize) {
		this.quantitySize = quantitySize;
	}
	public String getInvalidSerialNumber() {
		return invalidSerialNumber;
	}
	public void setInvalidSerialNumber(String invalidSerialNumber) {
		this.invalidSerialNumber = invalidSerialNumber;
	}
	public String getInvalidSerialNumber18Char() {
		return invalidSerialNumber18Char;
	}
	public void setInvalidSerialNumber18Char(String invalidSerialNumber18Char) {
		this.invalidSerialNumber18Char = invalidSerialNumber18Char;
	}
	public String getValidQuantity() {
		return validQuantity;
	}
	public void setValidQuantity(String validQuantity) {
		this.validQuantity = validQuantity;
	}
	public String getQuantityNotEmpty() {
		return quantityNotEmpty;
	}
	public void setQuantityNotEmpty(String quantityNotEmpty) {
		this.quantityNotEmpty = quantityNotEmpty;
	}
	public String getInvalidMaterialCode() {
		return invalidMaterialCode;
	}
	public void setInvalidMaterialCode(String invalidMaterialCode) {
		this.invalidMaterialCode = invalidMaterialCode;
	}
	public String getInvalidQuantity() {
		return invalidQuantity;
	}
	public void setInvalidQuantity(String invalidQuantity) {
		this.invalidQuantity = invalidQuantity;
	}
	public String getFormaterialcodes() {
		return formaterialcodes;
	}
	public void setFormaterialcodes(String formaterialcodes) {
		this.formaterialcodes = formaterialcodes;
	}
	public String getMaterialCodeNotMatchWithSECode() {
		return materialCodeNotMatchWithSECode;
	}
	public void setMaterialCodeNotMatchWithSECode(
			String materialCodeNotMatchWithSECode) {
		this.materialCodeNotMatchWithSECode = materialCodeNotMatchWithSECode;
	}
	public List<HashMap<String, String>> getIpossTokenRedemptionLinks() {
		return ipossTokenRedemptionLinks;
	}
	public void setIpossTokenRedemptionLinks(
			List<HashMap<String, String>> ipossTokenRedemptionLinks) {
		this.ipossTokenRedemptionLinks = ipossTokenRedemptionLinks;
	}
	public String getInstallBaseListSize() {
		return InstallBaseListSize;
	}
	public void setInstallBaseListSize(String installBaseListSize) {
		InstallBaseListSize = installBaseListSize;
	}
	public String getInstallBaseListSizeMsg() {
		return InstallBaseListSizeMsg;
	}
	public void setInstallBaseListSizeMsg(String installBaseListSizeMsg) {
		InstallBaseListSizeMsg = installBaseListSizeMsg;
	}
	public String getLeadingZeroesNote() {
		return leadingZeroesNote;
	}
	public void setLeadingZeroesNote(String leadingZeroesNote) {
		this.leadingZeroesNote = leadingZeroesNote;
	}
	public String getRVInvalidMC() {
		return RVInvalidMC;
	}
	public void setRVInvalidMC(String rVInvalidMC) {
		RVInvalidMC = rVInvalidMC;
	}
	public String getRVDuplicateSN() {
		return RVDuplicateSN;
	}
	public void setRVDuplicateSN(String rVDuplicateSN) {
		RVDuplicateSN = rVDuplicateSN;
	}
	public String getRVInvalidSN() {
		return RVInvalidSN;
	}
	public void setRVInvalidSN(String rVInvalidSN) {
		RVInvalidSN = rVInvalidSN;
	}
	public String getRVInvalidQty() {
		return RVInvalidQty;
	}
	public void setRVInvalidQty(String rVInvalidQty) {
		RVInvalidQty = rVInvalidQty;
	}
	public String getRVEmptyQty() {
		return RVEmptyQty;
	}
	public void setRVEmptyQty(String rVEmptyQty) {
		RVEmptyQty = rVEmptyQty;
	}
	public String getRVInvalidQtyWithSN() {
		return RVInvalidQtyWithSN;
	}
	public void setRVInvalidQtyWithSN(String rVInvalidQtyWithSN) {
		RVInvalidQtyWithSN = rVInvalidQtyWithSN;
	}
	public String getRecordValidationError7() {
		return recordValidationError7;
	}
	public void setRecordValidationError7(String recordValidationError7) {
		this.recordValidationError7 = recordValidationError7;
	}
	public String getRVInvalidOption() {
		return RVInvalidOption;
	}
	public void setRVInvalidOption(String rVInvalidOption) {
		RVInvalidOption = rVInvalidOption;
	}
	public String getManagedElementsRemainder() {
		return managedElementsRemainder;
	}
	public void setManagedElementsRemainder(String managedElementsRemainder) {
		this.managedElementsRemainder = managedElementsRemainder;
	}
	public String getManagedElementsRemainderKB() {
		return managedElementsRemainderKB;
	}
	public void setManagedElementsRemainderKB(String managedElementsRemainderKB) {
		this.managedElementsRemainderKB = managedElementsRemainderKB;
	}
	public String getStepBSuccessMsg() {
		return stepBSuccessMsg;
	}
	public void setStepBSuccessMsg(String stepBSuccessMsg) {
		this.stepBSuccessMsg = stepBSuccessMsg;
	}
	public String getStepBFailureMsg() {
		return stepBFailureMsg;
	}
	public void setStepBFailureMsg(String stepBFailureMsg) {
		this.stepBFailureMsg = stepBFailureMsg;
	}
	public String getRVMCNotPresentInDB() {
		return RVMCNotPresentInDB;
	}
	public void setRVMCNotPresentInDB(String rVMCNotPresentInDB) {
		RVMCNotPresentInDB = rVMCNotPresentInDB;
	}
	public String getRVExcludedMC() {
		return RVExcludedMC;
	}
	public void setRVExcludedMC(String rVExcludedMC) {
		RVExcludedMC = rVExcludedMC;
	}
	public String getNoConnectivityMessage() {
		return noConnectivityMessage;
	}
	public void setNoConnectivityMessage(String noConnectivityMessage) {
		this.noConnectivityMessage = noConnectivityMessage;
	}
	public String getRVErrorMessage() {
		return RVErrorMessage;
	}
	public void setRVErrorMessage(String rVErrorMessage) {
		RVErrorMessage = rVErrorMessage;
	}
	public String getRVSuccessMessage() {
		return RVSuccessMessage;
	}
	public void setRVSuccessMessage(String rVSuccessMessage) {
		RVSuccessMessage = rVSuccessMessage;
	}
	public String getEmptyRevisedQuantityMsg() {
		return emptyRevisedQuantityMsg;
	}
	public void setEmptyRevisedQuantityMsg(String emptyRevisedQuantityMsg) {
		this.emptyRevisedQuantityMsg = emptyRevisedQuantityMsg;
	}
	public String getSelectOneEquipmentMsg() {
		return selectOneEquipmentMsg;
	}
	public void setSelectOneEquipmentMsg(String selectOneEquipmentMsg) {
		this.selectOneEquipmentMsg = selectOneEquipmentMsg;
	}
	public String getMcWithActiveRemovalWarning() {
		return mcWithActiveRemovalWarning;
	}
	public void setMcWithActiveRemovalWarning(String mcWithActiveRemovalWarning) {
		this.mcWithActiveRemovalWarning = mcWithActiveRemovalWarning;
	}
	public String getSubmitConfirmMsg() {
		return submitConfirmMsg;
	}
	public void setSubmitConfirmMsg(String submitConfirmMsg) {
		this.submitConfirmMsg = submitConfirmMsg;
	}
	public String getSaveLoadingTextMsg() {
		return saveLoadingTextMsg;
	}
	public void setSaveLoadingTextMsg(String saveLoadingTextMsg) {
		this.saveLoadingTextMsg = saveLoadingTextMsg;
	}
	public String getSubmitLoadingTextMsg() {
		return submitLoadingTextMsg;
	}
	public void setSubmitLoadingTextMsg(String submitLoadingTextMsg) {
		this.submitLoadingTextMsg = submitLoadingTextMsg;
	}
	public String getConfirmValidateTextMsg() {
		return confirmValidateTextMsg;
	}
	public void setConfirmValidateTextMsg(String confirmValidateTextMsg) {
		this.confirmValidateTextMsg = confirmValidateTextMsg;
	}
	public String getCancelConfirmMsgHome() {
		return cancelConfirmMsgHome;
	}
	public void setCancelConfirmMsgHome(String cancelConfirmMsgHome) {
		this.cancelConfirmMsgHome = cancelConfirmMsgHome;
	}
	public String getCancelConfirmMsgRegList() {
		return cancelConfirmMsgRegList;
	}
	public void setCancelConfirmMsgRegList(String cancelConfirmMsgRegList) {
		this.cancelConfirmMsgRegList = cancelConfirmMsgRegList;
	}
	public String getDownloadTemplateMsg() {
		return downloadTemplateMsg;
	}
	public void setDownloadTemplateMsg(String downloadTemplateMsg) {
		this.downloadTemplateMsg = downloadTemplateMsg;
	}
	public String getValidOptionMsg() {
		return validOptionMsg;
	}
	public void setValidOptionMsg(String validOptionMsg) {
		this.validOptionMsg = validOptionMsg;
	}
	public String getLargeQuantityErrorMsg() {
		return largeQuantityErrorMsg;
	}
	public void setLargeQuantityErrorMsg(String largeQuantityErrorMsg) {
		this.largeQuantityErrorMsg = largeQuantityErrorMsg;
	}
	public String getValidatedStatusMsg() {
		return validatedStatusMsg;
	}
	public void setValidatedStatusMsg(String validatedStatusMsg) {
		this.validatedStatusMsg = validatedStatusMsg;
	}
	public String getMaterialCodeLabel() {
		return materialCodeLabel;
	}
	public void setMaterialCodeLabel(String materialCodeLabel) {
		this.materialCodeLabel = materialCodeLabel;
	}
	public String getBlankSerialNumberErrorMsg() {
		return blankSerialNumberErrorMsg;
	}
	public void setBlankSerialNumberErrorMsg(String blankSerialNumberErrorMsg) {
		this.blankSerialNumberErrorMsg = blankSerialNumberErrorMsg;
	}
	public String getInvalidMaterialCodeErrorMsg() {
		return invalidMaterialCodeErrorMsg;
	}
	public void setInvalidMaterialCodeErrorMsg(String invalidMaterialCodeErrorMsg) {
		this.invalidMaterialCodeErrorMsg = invalidMaterialCodeErrorMsg;
	}
	public String getSeibelTimeoutErrorMsg() {
		return seibelTimeoutErrorMsg;
	}
	public void setSeibelTimeoutErrorMsg(String seibelTimeoutErrorMsg) {
		this.seibelTimeoutErrorMsg = seibelTimeoutErrorMsg;
	}
	public String getNoRecordErrorMsg() {
		return noRecordErrorMsg;
	}
	public void setNoRecordErrorMsg(String noRecordErrorMsg) {
		this.noRecordErrorMsg = noRecordErrorMsg;
	}
	public String getIncorrectFileFormatErrMsg() {
		return incorrectFileFormatErrMsg;
	}
	public void setIncorrectFileFormatErrMsg(String incorrectFileFormatErrMsg) {
		this.incorrectFileFormatErrMsg = incorrectFileFormatErrMsg;
	}
	public String getHugeUpdateErrorMessage() {
		return hugeUpdateErrorMessage;
	}
	public void setHugeUpdateErrorMessage(String hugeUpdateErrorMessage) {
		this.hugeUpdateErrorMessage = hugeUpdateErrorMessage;
	}
	public String getNoActionImportErrorMsg() {
		return noActionImportErrorMsg;
	}
	public void setNoActionImportErrorMsg(String noActionImportErrorMsg) {
		this.noActionImportErrorMsg = noActionImportErrorMsg;
	}
	public String getSaveErrorMsg() {
		return saveErrorMsg;
	}
	public void setSaveErrorMsg(String saveErrorMsg) {
		this.saveErrorMsg = saveErrorMsg;
	}
	public String getErrorWhileProcessingErrMsg() {
		return errorWhileProcessingErrMsg;
	}
	public void setErrorWhileProcessingErrMsg(String errorWhileProcessingErrMsg) {
		this.errorWhileProcessingErrMsg = errorWhileProcessingErrMsg;
	}
	public String getSapDownErrMsg() {
		return sapDownErrMsg;
	}
	public void setSapDownErrMsg(String sapDownErrMsg) {
		this.sapDownErrMsg = sapDownErrMsg;
	}
	public String getProcessingFailedErrMsg() {
		return processingFailedErrMsg;
	}
	public void setProcessingFailedErrMsg(String processingFailedErrMsg) {
		this.processingFailedErrMsg = processingFailedErrMsg;
	}
	public String getEmptyMaterialListErrorMsg() {
		return emptyMaterialListErrorMsg;
	}
	public void setEmptyMaterialListErrorMsg(String emptyMaterialListErrorMsg) {
		this.emptyMaterialListErrorMsg = emptyMaterialListErrorMsg;
	}
	public String getUnableToMapSoldToErrMsg() {
		return unableToMapSoldToErrMsg;
	}
	public void setUnableToMapSoldToErrMsg(String unableToMapSoldToErrMsg) {
		this.unableToMapSoldToErrMsg = unableToMapSoldToErrMsg;
	}
	public String getRemainingQtyLessThanOne() {
		return remainingQtyLessThanOne;
	}
	public void setRemainingQtyLessThanOne(String remainingQtyLessThanOne) {
		this.remainingQtyLessThanOne = remainingQtyLessThanOne;
	}
	public String getNoConnectivityPopUpMessage() {
		return noConnectivityPopUpMessage;
	}
	public void setNoConnectivityPopUpMessage(String noConnectivityPopUpMessage) {
		this.noConnectivityPopUpMessage = noConnectivityPopUpMessage;
	}
	public String getConnectivityPopUpMessage() {
		return connectivityPopUpMessage;
	}
	public void setConnectivityPopUpMessage(String connectivityPopUpMessage) {
		this.connectivityPopUpMessage = connectivityPopUpMessage;
	}
	public String getCancelActionMsg() {
		return cancelActionMsg;
	}
	public void setCancelActionMsg(String cancelActionMsg) {
		this.cancelActionMsg = cancelActionMsg;
	}
	public String getSelectRecord() {
		return selectRecord;
	}
	public void setSelectRecord(String selectRecord) {
		this.selectRecord = selectRecord;
	}
	public String getSubmitTOBData() {
		return submitTOBData;
	}
	public void setSubmitTOBData(String submitTOBData) {
		this.submitTOBData = submitTOBData;
	}
	public String getIncorrectHeaderErrMsg() {
		return incorrectHeaderErrMsg;
	}
	public void setIncorrectHeaderErrMsg(String incorrectHeaderErrMsg) {
		this.incorrectHeaderErrMsg = incorrectHeaderErrMsg;
	}
	public String getEquipmentMoveSaveMessage() {
		return equipmentMoveSaveMessage;
	}
	public void setEquipmentMoveSaveMessage(String equipmentMoveSaveMessage) {
		this.equipmentMoveSaveMessage = equipmentMoveSaveMessage;
	}
	public String getEquipmentMoveSaveMessage2() {
		return equipmentMoveSaveMessage2;
	}
	public void setEquipmentMoveSaveMessage2(String equipmentMoveSaveMessage2) {
		this.equipmentMoveSaveMessage2 = equipmentMoveSaveMessage2;
	}
	public String getValidateSuccessMsg() {
		return validateSuccessMsg;
	}
	public void setValidateSuccessMsg(String validateSuccessMsg) {
		this.validateSuccessMsg = validateSuccessMsg;
	}
	public String getValidateErrorMsg() {
		return validateErrorMsg;
	}
	public void setValidateErrorMsg(String validateErrorMsg) {
		this.validateErrorMsg = validateErrorMsg;
	}
	
	public String getMinThreshhold() {
		return minThreshhold;
	}
	public void setMinThreshhold(String minThreshhold) {
		this.minThreshhold = minThreshhold;
	}
	public String getTopRecords() {
		return topRecords;
	}
	public void setTopRecords(String topRecords) {
		this.topRecords = topRecords;
	}
	public String getSerchMidRange() {
		return serchMidRange;
	}
	public void setSerchMidRange(String serchMidRange) {
		this.serchMidRange = serchMidRange;
	}
	public String getSearchLowRange() {
		return searchLowRange;
	}
	public void setSearchLowRange(String searchLowRange) {
		this.searchLowRange = searchLowRange;
	}
		public String getSalgwInstDefaultVal() {
		return salgwInstDefaultVal;
	}
	public void setSalgwInstDefaultVal(String salgwInstDefaultVal) {
		this.salgwInstDefaultVal = salgwInstDefaultVal;
	}
	public String getSalgwInstEmailId() {
		return salgwInstEmailId;
	}
	public void setSalgwInstEmailId(String salgwInstEmailId) {
		this.salgwInstEmailId = salgwInstEmailId;
	}
	public String getSalgwInstErrMsg() {
		return salgwInstErrMsg;
	}
	public void setSalgwInstErrMsg(String salgwInstErrMsg) {
		this.salgwInstErrMsg = salgwInstErrMsg;
	}
	public String getSalgwInstMandValidMsg() {
		return salgwInstMandValidMsg;
	}
	public void setSalgwInstMandValidMsg(String salgwInstMandValidMsg) {
		this.salgwInstMandValidMsg = salgwInstMandValidMsg;
	}
	public String getSalGatewayMaterialCode() {
		return salGatewayMaterialCode;
	}
	public void setSalGatewayMaterialCode(String salGatewayMaterialCode) {
		this.salGatewayMaterialCode = salGatewayMaterialCode;
	}
	public String getIpoFileSize() {
		return ipoFileSize;
	}
	public void setIpoFileSize(String ipoFileSize) {
		this.ipoFileSize = ipoFileSize;
	}
	public String getSalRequired() {
		return salRequired;
	}
	public void setSalRequired(String salRequired) {
		this.salRequired = salRequired;
	}
	public String getDuplicateGateway() {
		return duplicateGateway;
	}
	public void setDuplicateGateway(String duplicateGateway) {
		this.duplicateGateway = duplicateGateway;
	}
	public String getEmptySTSEID() {
		return emptySTSEID;
	}
	public void setEmptySTSEID(String emptySTSEID) {
		this.emptySTSEID = emptySTSEID;
	}
	public String getInvalidST() {
		return invalidST;
	}
	public void setInvalidST(String invalidST) {
		this.invalidST = invalidST;
	}
	public String getInvalidSEID() {
		return invalidSEID;
	}
	public void setInvalidSEID(String invalidSEID) {
		this.invalidSEID = invalidSEID;
	}
	public String getValidSTSEID() {
		return validSTSEID;
	}
	public void setValidSTSEID(String validSTSEID) {
		this.validSTSEID = validSTSEID;
	}
	public String getGatewayAdded() {
		return gatewayAdded;
	}
	public void setGatewayAdded(String gatewayAdded) {
		this.gatewayAdded = gatewayAdded;
	}
	public String getSelectAsset() {
		return selectAsset;
	}
	public void setSelectAsset(String selectAsset) {
		this.selectAsset = selectAsset;
	}
	public String getVsalgwErrMsg() {
		return vsalgwErrMsg;
	}
	public void setVsalgwErrMsg(String vsalgwErrMsg) {
		this.vsalgwErrMsg = vsalgwErrMsg;
	}
	public String getPleaseSelectARecordMsg() {
		return pleaseSelectARecordMsg;
	}
	public void setPleaseSelectARecordMsg(String pleaseSelectARecordMsg) {
		this.pleaseSelectARecordMsg = pleaseSelectARecordMsg;
	}
	public String getEmptySoldToError() {
		return emptySoldToError;
	}
	public void setEmptySoldToError(String emptySoldToError) {
		this.emptySoldToError = emptySoldToError;
	}
	public String getRequiredNameCityCountryError() {
		return requiredNameCityCountryError;
	}
	public void setRequiredNameCityCountryError(String requiredNameCityCountryError) {
		this.requiredNameCityCountryError = requiredNameCityCountryError;
	}
	public String getAccountNameLengthError() {
		return accountNameLengthError;
	}
	public void setAccountNameLengthError(String accountNameLengthError) {
		this.accountNameLengthError = accountNameLengthError;
	}
	public String getCityLengthError() {
		return cityLengthError;
	}
	public void setCityLengthError(String cityLengthError) {
		this.cityLengthError = cityLengthError;
	}
	public String getMandatoryAccNameStreetCityError() {
		return mandatoryAccNameStreetCityError;
	}
	public void setMandatoryAccNameStreetCityError(
			String mandatoryAccNameStreetCityError) {
		this.mandatoryAccNameStreetCityError = mandatoryAccNameStreetCityError;
	}
	public String getMandatorPhoneEmailNameError() {
		return mandatorPhoneEmailNameError;
	}
	public void setMandatorPhoneEmailNameError(String mandatorPhoneEmailNameError) {
		this.mandatorPhoneEmailNameError = mandatorPhoneEmailNameError;
	}
	public String getStreetLengthError() {
		return streetLengthError;
	}
	public void setStreetLengthError(String streetLengthError) {
		this.streetLengthError = streetLengthError;
	}
	public String getZipCodeLengthError() {
		return zipCodeLengthError;
	}
	public void setZipCodeLengthError(String zipCodeLengthError) {
		this.zipCodeLengthError = zipCodeLengthError;
	}
	public String getPhoneNumLengthError() {
		return phoneNumLengthError;
	}
	public void setPhoneNumLengthError(String phoneNumLengthError) {
		this.phoneNumLengthError = phoneNumLengthError;
	}
	public String getInvalidPhoneNumberError() {
		return invalidPhoneNumberError;
	}
	public void setInvalidPhoneNumberError(String invalidPhoneNumberError) {
		this.invalidPhoneNumberError = invalidPhoneNumberError;
	}
	public String getFaxLengthError() {
		return faxLengthError;
	}
	public void setFaxLengthError(String faxLengthError) {
		this.faxLengthError = faxLengthError;
	}
	public String getEmailLengthError() {
		return emailLengthError;
	}
	public void setEmailLengthError(String emailLengthError) {
		this.emailLengthError = emailLengthError;
	}
	public String getStateLengthError() {
		return stateLengthError;
	}
	public void setStateLengthError(String stateLengthError) {
		this.stateLengthError = stateLengthError;
	}
	public String getCancelProcessMsg() {
		return cancelProcessMsg;
	}
	public void setCancelProcessMsg(String cancelProcessMsg) {
		this.cancelProcessMsg = cancelProcessMsg;
	}
	public String getWaitWhileFetchingMsg() {
		return waitWhileFetchingMsg;
	}
	public void setWaitWhileFetchingMsg(String waitWhileFetchingMsg) {
		this.waitWhileFetchingMsg = waitWhileFetchingMsg;
	}
	public String getStateOrProvince() {
		return stateOrProvince;
	}
	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}
	public String getStreet2LengthError() {
		return street2LengthError;
	}
	public void setStreet2LengthError(String street2LengthError) {
		this.street2LengthError = street2LengthError;
	}
	public String getPhoneNoMax10Error() {
		return phoneNoMax10Error;
	}
	public void setPhoneNoMax10Error(String phoneNoMax10Error) {
		this.phoneNoMax10Error = phoneNoMax10Error;
	}
	public String getWaitAccountUpdateMsg() {
		return waitAccountUpdateMsg;
	}
	public void setWaitAccountUpdateMsg(String waitAccountUpdateMsg) {
		this.waitAccountUpdateMsg = waitAccountUpdateMsg;
	}
	public String getMandatoryBPLinkId() {
		return mandatoryBPLinkId;
	}
	public void setMandatoryBPLinkId(String mandatoryBPLinkId) {
		this.mandatoryBPLinkId = mandatoryBPLinkId;
	}
	public String getTokenLengthErrMsg() {
		return tokenLengthErrMsg;
	}
	public void setTokenLengthErrMsg(String tokenLengthErrMsg) {
		this.tokenLengthErrMsg = tokenLengthErrMsg;
	}
	public String getErrorPleaseInsert() {
		return errorPleaseInsert;
	}
	public void setErrorPleaseInsert(String errorPleaseInsert) {
		this.errorPleaseInsert = errorPleaseInsert;
	}
	public String getUpto() {
		return upto;
	}
	public void setUpto(String upto) {
		this.upto = upto;
	}
	public String getChars() {
		return chars;
	}
	public void setChars(String chars) {
		this.chars = chars;
	}
	public String getVatLengthErrMsg() {
		return vatLengthErrMsg;
	}
	public void setVatLengthErrMsg(String vatLengthErrMsg) {
		this.vatLengthErrMsg = vatLengthErrMsg;
	}
	public String getTokenValidateAgainErrMsg() {
		return tokenValidateAgainErrMsg;
	}
	public void setTokenValidateAgainErrMsg(String tokenValidateAgainErrMsg) {
		this.tokenValidateAgainErrMsg = tokenValidateAgainErrMsg;
	}
	public String getInvalidSoldToErrMsg() {
		return invalidSoldToErrMsg;
	}
	public void setInvalidSoldToErrMsg(String invalidSoldToErrMsg) {
		this.invalidSoldToErrMsg = invalidSoldToErrMsg;
	}
	public String getMandatoryFnLnEmailPhone() {
		return mandatoryFnLnEmailPhone;
	}
	public void setMandatoryFnLnEmailPhone(String mandatoryFnLnEmailPhone) {
		this.mandatoryFnLnEmailPhone = mandatoryFnLnEmailPhone;
	}
	public String getFnLnLengthErrMsg() {
		return fnLnLengthErrMsg;
	}
	public void setFnLnLengthErrMsg(String fnLnLengthErrMsg) {
		this.fnLnLengthErrMsg = fnLnLengthErrMsg;
	}
	public String getEmailLengthErrMsg() {
		return emailLengthErrMsg;
	}
	public void setEmailLengthErrMsg(String emailLengthErrMsg) {
		this.emailLengthErrMsg = emailLengthErrMsg;
	}
	public String getInvalidEmailErrMsg() {
		return invalidEmailErrMsg;
	}
	public void setInvalidEmailErrMsg(String invalidEmailErrMsg) {
		this.invalidEmailErrMsg = invalidEmailErrMsg;
	}
	public String getPhoneLength16ErrMsg() {
		return phoneLength16ErrMsg;
	}
	public void setPhoneLength16ErrMsg(String phoneLength16ErrMsg) {
		this.phoneLength16ErrMsg = phoneLength16ErrMsg;
	}
	public String getInternationalPhNoErrMsg() {
		return internationalPhNoErrMsg;
	}
	public void setInternationalPhNoErrMsg(String internationalPhNoErrMsg) {
		this.internationalPhNoErrMsg = internationalPhNoErrMsg;
	}
	public String getFnLengthErrMsg() {
		return fnLengthErrMsg;
	}
	public void setFnLengthErrMsg(String fnLengthErrMsg) {
		this.fnLengthErrMsg = fnLengthErrMsg;
	}
	public String getLnLengthErrMsg() {
		return lnLengthErrMsg;
	}
	public void setLnLengthErrMsg(String lnLengthErrMsg) {
		this.lnLengthErrMsg = lnLengthErrMsg;
	}
	public String getPhoneLength10ErrMsg() {
		return phoneLength10ErrMsg;
	}
	public void setPhoneLength10ErrMsg(String phoneLength10ErrMsg) {
		this.phoneLength10ErrMsg = phoneLength10ErrMsg;
	}
	public String getMandatoryNameStreetCityCountryErrMsg() {
		return mandatoryNameStreetCityCountryErrMsg;
	}
	public void setMandatoryNameStreetCityCountryErrMsg(
			String mandatoryNameStreetCityCountryErrMsg) {
		this.mandatoryNameStreetCityCountryErrMsg = mandatoryNameStreetCityCountryErrMsg;
	}
	public String getAccountNameLengthErrMsg() {
		return accountNameLengthErrMsg;
	}
	public void setAccountNameLengthErrMsg(String accountNameLengthErrMsg) {
		this.accountNameLengthErrMsg = accountNameLengthErrMsg;
	}
	public String getCityLengthErrMsg() {
		return cityLengthErrMsg;
	}
	public void setCityLengthErrMsg(String cityLengthErrMsg) {
		this.cityLengthErrMsg = cityLengthErrMsg;
	}
	public String getNameAlphaNumnericErrMsg() {
		return nameAlphaNumnericErrMsg;
	}
	public void setNameAlphaNumnericErrMsg(String nameAlphaNumnericErrMsg) {
		this.nameAlphaNumnericErrMsg = nameAlphaNumnericErrMsg;
	}
	public String getStreetAlphaNumericErrMsg() {
		return streetAlphaNumericErrMsg;
	}
	public void setStreetAlphaNumericErrMsg(String streetAlphaNumericErrMsg) {
		this.streetAlphaNumericErrMsg = streetAlphaNumericErrMsg;
	}
	public String getPotsalAlphaNumericErrMsg() {
		return potsalAlphaNumericErrMsg;
	}
	public void setPotsalAlphaNumericErrMsg(String potsalAlphaNumericErrMsg) {
		this.potsalAlphaNumericErrMsg = potsalAlphaNumericErrMsg;
	}
	public String getMandatoryAccNameStreetCityPostalErrMsg() {
		return mandatoryAccNameStreetCityPostalErrMsg;
	}
	public void setMandatoryAccNameStreetCityPostalErrMsg(
			String mandatoryAccNameStreetCityPostalErrMsg) {
		this.mandatoryAccNameStreetCityPostalErrMsg = mandatoryAccNameStreetCityPostalErrMsg;
	}
	public String getStreetLengthErrMsg() {
		return streetLengthErrMsg;
	}
	public void setStreetLengthErrMsg(String streetLengthErrMsg) {
		this.streetLengthErrMsg = streetLengthErrMsg;
	}
	public String getPostalLengthErrMsg() {
		return postalLengthErrMsg;
	}
	public void setPostalLengthErrMsg(String postalLengthErrMsg) {
		this.postalLengthErrMsg = postalLengthErrMsg;
	}
	public String getMandatoryReasonForAccCreation() {
		return mandatoryReasonForAccCreation;
	}
	public void setMandatoryReasonForAccCreation(
			String mandatoryReasonForAccCreation) {
		this.mandatoryReasonForAccCreation = mandatoryReasonForAccCreation;
	}
	public String getReasonForAccCreateMinLengthErrMsg() {
		return reasonForAccCreateMinLengthErrMsg;
	}
	public void setReasonForAccCreateMinLengthErrMsg(
			String reasonForAccCreateMinLengthErrMsg) {
		this.reasonForAccCreateMinLengthErrMsg = reasonForAccCreateMinLengthErrMsg;
	}
	public String getReasonForAccCreateMaxLengthErrMsg() {
		return reasonForAccCreateMaxLengthErrMsg;
	}
	public void setReasonForAccCreateMaxLengthErrMsg(
			String reasonForAccCreateMaxLengthErrMsg) {
		this.reasonForAccCreateMaxLengthErrMsg = reasonForAccCreateMaxLengthErrMsg;
	}
	public String getSelectAtlestOneRecord() {
		return selectAtlestOneRecord;
	}
	public void setSelectAtlestOneRecord(String selectAtlestOneRecord) {
		this.selectAtlestOneRecord = selectAtlestOneRecord;
	}
	public String getEnterAtlestOneEmailMsg() {
		return enterAtlestOneEmailMsg;
	}
	public void setEnterAtlestOneEmailMsg(String enterAtlestOneEmailMsg) {
		this.enterAtlestOneEmailMsg = enterAtlestOneEmailMsg;
	}
	public String getWaitAccountCreateMsg() {
		return waitAccountCreateMsg;
	}
	public void setWaitAccountCreateMsg(String waitAccountCreateMsg) {
		this.waitAccountCreateMsg = waitAccountCreateMsg;
	}
	public String getWaitSendEmailMsg() {
		return waitSendEmailMsg;
	}
	public void setWaitSendEmailMsg(String waitSendEmailMsg) {
		this.waitSendEmailMsg = waitSendEmailMsg;
	}
	public String getNoneOfabove() {
		return noneOfabove;
	}
	public void setNoneOfabove(String noneOfabove) {
		this.noneOfabove = noneOfabove;
	}
	public String getExistingAccount() {
		return existingAccount;
	}
	public void setExistingAccount(String existingAccount) {
		this.existingAccount = existingAccount;
	}
	public String getValidationEmailInvalid() {
		return validationEmailInvalid;
	}
	public void setValidationEmailInvalid(String validationEmailInvalid) {
		this.validationEmailInvalid = validationEmailInvalid;
	}
	public String getMandatoryToken() {
		return mandatoryToken;
	}
	public void setMandatoryToken(String mandatoryToken) {
		this.mandatoryToken = mandatoryToken;
	}
	public String getTokenAlphaNumericErrMsg() {
		return tokenAlphaNumericErrMsg;
	}
	public void setTokenAlphaNumericErrMsg(String tokenAlphaNumericErrMsg) {
		this.tokenAlphaNumericErrMsg = tokenAlphaNumericErrMsg;
	}
	public String getWaitValidateTokenMsg() {
		return waitValidateTokenMsg;
	}
	public void setWaitValidateTokenMsg(String waitValidateTokenMsg) {
		this.waitValidateTokenMsg = waitValidateTokenMsg;
	}	
	
	private HashMap<String,String> ewiMessageCodeMap = new HashMap<String, String>();
	
	public HashMap<String,String> getEwiMessageCodeMap() {
		return ewiMessageCodeMap;
	}
	public void setEwiMessageCodeMap(HashMap<String,String> ewiMessageCodeMap) {
		this.ewiMessageCodeMap = ewiMessageCodeMap;
	}
	
	// Entries added by Bhupendra
		//added for update announcements
		private String alertMessageBlankMsg;
		private String alertMessageLenghtOverMsg;
		private String alertStartDateBlankMsg;
		private String alertEndDateBlankMsg;
		private String alertDatesNotValidMsg;
		private String alertStartDateLessCurrDateMsg;
		private String alertStartDateGreaterEndDateMsg;
		private String alertDeleteConfirmationMsg;
		private String alertInvalidTimeFormatMsg;

		public String getAlertMessageBlankMsg() {
			return alertMessageBlankMsg;
		}
		public void setAlertMessageBlankMsg(String alertMessageBlankMsg) {
			this.alertMessageBlankMsg = alertMessageBlankMsg;
		}
		public String getAlertMessageLenghtOverMsg() {
			return alertMessageLenghtOverMsg;
		}
		public void setAlertMessageLenghtOverMsg(String alertMessageLenghtOverMsg) {
			this.alertMessageLenghtOverMsg = alertMessageLenghtOverMsg;
		}
		public String getAlertStartDateBlankMsg() {
			return alertStartDateBlankMsg;
		}
		public void setAlertStartDateBlankMsg(String alertStartDateBlankMsg) {
			this.alertStartDateBlankMsg = alertStartDateBlankMsg;
		}
		public String getAlertEndDateBlankMsg() {
			return alertEndDateBlankMsg;
		}
		public void setAlertEndDateBlankMsg(String alertEndDateBlankMsg) {
			this.alertEndDateBlankMsg = alertEndDateBlankMsg;
		}
		public String getAlertDatesNotValidMsg() {
			return alertDatesNotValidMsg;
		}
		public void setAlertDatesNotValidMsg(String alertDatesNotValidMsg) {
			this.alertDatesNotValidMsg = alertDatesNotValidMsg;
		}
		public String getAlertStartDateLessCurrDateMsg() {
			return alertStartDateLessCurrDateMsg;
		}
		public void setAlertStartDateLessCurrDateMsg(
				String alertStartDateLessCurrDateMsg) {
			this.alertStartDateLessCurrDateMsg = alertStartDateLessCurrDateMsg;
		}
		public String getAlertStartDateGreaterEndDateMsg() {
			return alertStartDateGreaterEndDateMsg;
		}
		public void setAlertStartDateGreaterEndDateMsg(
				String alertStartDateGreaterEndDateMsg) {
			this.alertStartDateGreaterEndDateMsg = alertStartDateGreaterEndDateMsg;
		}
		public String getAlertDeleteConfirmationMsg() {
			return alertDeleteConfirmationMsg;
		}
		public void setAlertDeleteConfirmationMsg(String alertDeleteConfirmationMsg) {
			this.alertDeleteConfirmationMsg = alertDeleteConfirmationMsg;
		}
		public String getAlertInvalidTimeFormatMsg() {
			return alertInvalidTimeFormatMsg;
		}
		public void setAlertInvalidTimeFormatMsg(String alertInvalidTimeFormatMsg) {
			this.alertInvalidTimeFormatMsg = alertInvalidTimeFormatMsg;
		}
		//End entries added for update announcements
		
		//added for token redemption
		private String tokenIvalidError;
		private String tokenExpiredError;
		private String tokenRedeemedError;
		private String tokenUnauthorizeToRedeemError;
		private String tokenAdditionInfoLengthError;
		private String tokenCancelWarningMsg;
		private String tokenSelectAccountError;
		private String tokenSelectRecordError;
		private String tokenReqNameCityCountryError;
		private String tokenBlankError;
		private String tokenAlphaNumericError;
		private String tokenExcessLengthError;
		private String tokenValidObjectLengthError;
		private String tokenBPLinkIdError;
		private String tokenEndCustomerAccNumberError;
		private String tokenValidatingError;
		private String tokenBPLinkReqError;
		private String tokenBPLinkInvalidError;
		private String tokenBPLinkValidatingExcp;
		private String tokenShipToInvalidError;
		private String tokenRedeemException;
		private String tokenSoldToNotFoundError;
		private String tokenBpUnauthorizedForSoldToError;
		private String tokenCountryUSError;
		private String tokenCountryNonUSError;
		private String tokenFuzzySearchError;

		public String getTokenIvalidError() {
			return tokenIvalidError;
		}
		public void setTokenIvalidError(String tokenIvalidError) {
			this.tokenIvalidError = tokenIvalidError;
		}
		public String getTokenExpiredError() {
			return tokenExpiredError;
		}
		public void setTokenExpiredError(String tokenExpiredError) {
			this.tokenExpiredError = tokenExpiredError;
		}
		public String getTokenRedeemedError() {
			return tokenRedeemedError;
		}
		public void setTokenRedeemedError(String tokenRedeemedError) {
			this.tokenRedeemedError = tokenRedeemedError;
		}
		public String getTokenUnauthorizeToRedeemError() {
			return tokenUnauthorizeToRedeemError;
		}
		public void setTokenUnauthorizeToRedeemError(
				String tokenUnauthorizeToRedeemError) {
			this.tokenUnauthorizeToRedeemError = tokenUnauthorizeToRedeemError;
		}
		public String getTokenAdditionInfoLengthError() {
			return tokenAdditionInfoLengthError;
		}
		public void setTokenAdditionInfoLengthError(String tokenAdditionInfoLengthError) {
			this.tokenAdditionInfoLengthError = tokenAdditionInfoLengthError;
		}
		public String getTokenCancelWarningMsg() {
			return tokenCancelWarningMsg;
		}
		public void setTokenCancelWarningMsg(String tokenCancelWarningMsg) {
			this.tokenCancelWarningMsg = tokenCancelWarningMsg;
		}
		public String getTokenSelectAccountError() {
			return tokenSelectAccountError;
		}
		public void setTokenSelectAccountError(String tokenSelectAccountError) {
			this.tokenSelectAccountError = tokenSelectAccountError;
		}
		public String getTokenSelectRecordError() {
			return tokenSelectRecordError;
		}
		public void setTokenSelectRecordError(String tokenSelectRecordError) {
			this.tokenSelectRecordError = tokenSelectRecordError;
		}
		public String getTokenReqNameCityCountryError() {
			return tokenReqNameCityCountryError;
		}
		public void setTokenReqNameCityCountryError(String tokenReqNameCityCountryError) {
			this.tokenReqNameCityCountryError = tokenReqNameCityCountryError;
		}
		public String getTokenBlankError() {
			return tokenBlankError;
		}
		public void setTokenBlankError(String tokenBlankError) {
			this.tokenBlankError = tokenBlankError;
		}
		public String getTokenAlphaNumericError() {
			return tokenAlphaNumericError;
		}
		public void setTokenAlphaNumericError(String tokenAlphaNumericError) {
			this.tokenAlphaNumericError = tokenAlphaNumericError;
		}
		public String getTokenExcessLengthError() {
			return tokenExcessLengthError;
		}
		public void setTokenExcessLengthError(String tokenExcessLengthError) {
			this.tokenExcessLengthError = tokenExcessLengthError;
		}
		public String getTokenValidObjectLengthError() {
			return tokenValidObjectLengthError;
		}
		public void setTokenValidObjectLengthError(String tokenValidObjectLengthError) {
			this.tokenValidObjectLengthError = tokenValidObjectLengthError;
		}
		public String getTokenBPLinkIdError() {
			return tokenBPLinkIdError;
		}
		public void setTokenBPLinkIdError(String tokenBPLinkIdError) {
			this.tokenBPLinkIdError = tokenBPLinkIdError;
		}
		public String getTokenEndCustomerAccNumberError() {
			return tokenEndCustomerAccNumberError;
		}
		public void setTokenEndCustomerAccNumberError(
				String tokenEndCustomerAccNumberError) {
			this.tokenEndCustomerAccNumberError = tokenEndCustomerAccNumberError;
		}
		public String getTokenValidatingError() {
			return tokenValidatingError;
		}
		public void setTokenValidatingError(String tokenValidatingError) {
			this.tokenValidatingError = tokenValidatingError;
		}
		public String getTokenBPLinkReqError() {
			return tokenBPLinkReqError;
		}
		public void setTokenBPLinkReqError(String tokenBPLinkReqError) {
			this.tokenBPLinkReqError = tokenBPLinkReqError;
		}
		public String getTokenBPLinkInvalidError() {
			return tokenBPLinkInvalidError;
		}
		public void setTokenBPLinkInvalidError(String tokenBPLinkInvalidError) {
			this.tokenBPLinkInvalidError = tokenBPLinkInvalidError;
		}
		public String getTokenBPLinkValidatingExcp() {
			return tokenBPLinkValidatingExcp;
		}
		public void setTokenBPLinkValidatingExcp(String tokenBPLinkValidatingExcp) {
			this.tokenBPLinkValidatingExcp = tokenBPLinkValidatingExcp;
		}
		public String getTokenShipToInvalidError() {
			return tokenShipToInvalidError;
		}
		public void setTokenShipToInvalidError(String tokenShipToInvalidError) {
			this.tokenShipToInvalidError = tokenShipToInvalidError;
		}
		public String getTokenRedeemException() {
			return tokenRedeemException;
		}
		public void setTokenRedeemException(String tokenRedeemException) {
			this.tokenRedeemException = tokenRedeemException;
		}
		public String getTokenSoldToNotFoundError() {
			return tokenSoldToNotFoundError;
		}
		public void setTokenSoldToNotFoundError(String tokenSoldToNotFoundError) {
			this.tokenSoldToNotFoundError = tokenSoldToNotFoundError;
		}
		public String getTokenBpUnauthorizedForSoldToError() {
			return tokenBpUnauthorizedForSoldToError;
		}
		public void setTokenBpUnauthorizedForSoldToError(
				String tokenBpUnauthorizedForSoldToError) {
			this.tokenBpUnauthorizedForSoldToError = tokenBpUnauthorizedForSoldToError;
		}
		public String getTokenCountryUSError() {
			return tokenCountryUSError;
		}
		public void setTokenCountryUSError(String tokenCountryUSError) {
			this.tokenCountryUSError = tokenCountryUSError;
		}
		public String getTokenCountryNonUSError() {
			return tokenCountryNonUSError;
		}
		public void setTokenCountryNonUSError(String tokenCountryNonUSError) {
			this.tokenCountryNonUSError = tokenCountryNonUSError;
		}
		public String getTokenFuzzySearchError() {
			return tokenFuzzySearchError;
		}
		public void setTokenFuzzySearchError(String tokenFuzzySearchError) {
			this.tokenFuzzySearchError = tokenFuzzySearchError;
		}
		//End entries added for token redemption
		// End entries added by Bhupendra
		
		// Entries added by Vipin
		/*New Property for IB_Creation hard coded msgs - mittal6.*/	
		private String alertSomethingWrng;
		private String incorrectFormat;
		private String selMatEntry;
		private String selectAtlestOneQuality;
		private String notAllValForReg;
		private String selectAtlestOneMatCode;
		private String selCorrectOptions;
		private String enterValidMatCode;
		private String uncheckLabelForReg;
		private String notAllValforSelReg;
		private String selectAtlestOneSrNo;
		private String selectAtlestOneCoreUnit;
		private String selAtlestOneMatCodeForSubmit;
		private String hugeUploadQuantity;
		private String incorrectFormateForFile;
		private String closeFunNotConfigured;
		
		private String errorSystemAlert;
		private String infInfMCodeInProcess;
		private String infIBFedAutomatic;
		private String errOffrTCNotQual;
		private String infIncOrgTrancNo;
		private String infSendWrtnReq;
		private String errRdyToSalMig;
		private String infCnfrmRegOne;
		private String infCnfrmRegTwo;
		private String infCnfrmRegThree;
		private String errLineItemMaxLimCross;
		private String errSOIRLineItemMaxLimCross;
		private String cnfrmBckToGrt;
		private String cnfrmBckToRegList;
		private String noRowsFoundForUpdateErrMsg;	
		//ESNA changes
		private String errEsnaCode;
		private String exclEsnaErrMsg;
				
		
		public String getAlertSomethingWrng() {
			return alertSomethingWrng;
		}
		public void setAlertSomethingWrng(String alertSomethingWrng) {
			this.alertSomethingWrng = alertSomethingWrng;
		}
		public String getIncorrectFormat() {
			return incorrectFormat;
		}
		public void setIncorrectFormat(String incorrectFormat) {
			this.incorrectFormat = incorrectFormat;
		}
		public String getSelMatEntry() {
			return selMatEntry;
		}
		public void setSelMatEntry(String selMatEntry) {
			this.selMatEntry = selMatEntry;
		}
		public String getSelectAtlestOneQuality() {
			return selectAtlestOneQuality;
		}
		public void setSelectAtlestOneQuality(String selectAtlestOneQuality) {
			this.selectAtlestOneQuality = selectAtlestOneQuality;
		}
		public String getNotAllValForReg() {
			return notAllValForReg;
		}
		public void setNotAllValForReg(String notAllValForReg) {
			this.notAllValForReg = notAllValForReg;
		}
		public String getSelectAtlestOneMatCode() {
			return selectAtlestOneMatCode;
		}
		public void setSelectAtlestOneMatCode(String selectAtlestOneMatCode) {
			this.selectAtlestOneMatCode = selectAtlestOneMatCode;
		}
		public String getSelCorrectOptions() {
			return selCorrectOptions;
		}
		public void setSelCorrectOptions(String selCorrectOptions) {
			this.selCorrectOptions = selCorrectOptions;
		}
		public String getEnterValidMatCode() {
			return enterValidMatCode;
		}
		public void setEnterValidMatCode(String enterValidMatCode) {
			this.enterValidMatCode = enterValidMatCode;
		}
		public String getUncheckLabelForReg() {
			return uncheckLabelForReg;
		}
		public void setUncheckLabelForReg(String uncheckLabelForReg) {
			this.uncheckLabelForReg = uncheckLabelForReg;
		}
		public String getNotAllValforSelReg() {
			return notAllValforSelReg;
		}
		public void setNotAllValforSelReg(String notAllValforSelReg) {
			this.notAllValforSelReg = notAllValforSelReg;
		}
		public String getSelectAtlestOneSrNo() {
			return selectAtlestOneSrNo;
		}
		public void setSelectAtlestOneSrNo(String selectAtlestOneSrNo) {
			this.selectAtlestOneSrNo = selectAtlestOneSrNo;
		}
		public String getSelectAtlestOneCoreUnit() {
			return selectAtlestOneCoreUnit;
		}
		public void setSelectAtlestOneCoreUnit(String selectAtlestOneCoreUnit) {
			this.selectAtlestOneCoreUnit = selectAtlestOneCoreUnit;
		}
		public String getSelAtlestOneMatCodeForSubmit() {
			return selAtlestOneMatCodeForSubmit;
		}
		public void setSelAtlestOneMatCodeForSubmit(String selAtlestOneMatCodeForSubmit) {
			this.selAtlestOneMatCodeForSubmit = selAtlestOneMatCodeForSubmit;
		}
		public String getHugeUploadQuantity() {
			return hugeUploadQuantity;
		}
		public void setHugeUploadQuantity(String hugeUploadQuantity) {
			this.hugeUploadQuantity = hugeUploadQuantity;
		}
		public String getIncorrectFormateForFile() {
			return incorrectFormateForFile;
		}
		public void setIncorrectFormateForFile(String incorrectFormateForFile) {
			this.incorrectFormateForFile = incorrectFormateForFile;
		}
		public String getCloseFunNotConfigured() {
			return closeFunNotConfigured;
		}
		public void setCloseFunNotConfigured(String closeFunNotConfigured) {
			this.closeFunNotConfigured = closeFunNotConfigured;
		}
		
		
		
		public String getErrorSystemAlert() {
			return errorSystemAlert;
		}
		public void setErrorSystemAlert(String errorSystemAlert) {
			this.errorSystemAlert = errorSystemAlert;
		}
		public String getInfInfMCodeInProcess() {
			return infInfMCodeInProcess;
		}
		public void setInfInfMCodeInProcess(String infInfMCodeInProcess) {
			this.infInfMCodeInProcess = infInfMCodeInProcess;
		}
		public String getInfIBFedAutomatic() {
			return infIBFedAutomatic;
		}
		public void setInfIBFedAutomatic(String infIBFedAutomatic) {
			this.infIBFedAutomatic = infIBFedAutomatic;
		}
		public String getErrOffrTCNotQual() {
			return errOffrTCNotQual;
		}
		public void setErrOffrTCNotQual(String errOffrTCNotQual) {
			this.errOffrTCNotQual = errOffrTCNotQual;
		}
		public String getInfIncOrgTrancNo() {
			return infIncOrgTrancNo;
		}
		public void setInfIncOrgTrancNo(String infIncOrgTrancNo) {
			this.infIncOrgTrancNo = infIncOrgTrancNo;
		}
		public String getInfSendWrtnReq() {
			return infSendWrtnReq;
		}
		public void setInfSendWrtnReq(String infSendWrtnReq) {
			this.infSendWrtnReq = infSendWrtnReq;
		}
		public String getErrRdyToSalMig() {
			return errRdyToSalMig;
		}
		public void setErrRdyToSalMig(String errRdyToSalMig) {
			this.errRdyToSalMig = errRdyToSalMig;
		}
		public String getInfCnfrmRegOne() {
			return infCnfrmRegOne;
		}
		public void setInfCnfrmRegOne(String infCnfrmRegOne) {
			this.infCnfrmRegOne = infCnfrmRegOne;
		}
		public String getInfCnfrmRegTwo() {
			return infCnfrmRegTwo;
		}
		public void setInfCnfrmRegTwo(String infCnfrmRegTwo) {
			this.infCnfrmRegTwo = infCnfrmRegTwo;
		}
		public String getInfCnfrmRegThree() {
			return infCnfrmRegThree;
		}
		public void setInfCnfrmRegThree(String infCnfrmRegThree) {
			this.infCnfrmRegThree = infCnfrmRegThree;
		}
		public String getErrLineItemMaxLimCross() {
			return errLineItemMaxLimCross;
		}
		public void setErrLineItemMaxLimCross(String errLineItemMaxLimCross) {
			this.errLineItemMaxLimCross = errLineItemMaxLimCross;
		}
		public String getErrSOIRLineItemMaxLimCross() {
			return errSOIRLineItemMaxLimCross;
		}
		public void setErrSOIRLineItemMaxLimCross(String errSOIRLineItemMaxLimCross) {
			this.errSOIRLineItemMaxLimCross = errSOIRLineItemMaxLimCross;
		}
		public String getCnfrmBckToGrt() {
			return cnfrmBckToGrt;
		}
		public void setCnfrmBckToGrt(String cnfrmBckToGrt) {
			this.cnfrmBckToGrt = cnfrmBckToGrt;
		}
		public String getCnfrmBckToRegList() {
			return cnfrmBckToRegList;
		}
		public void setCnfrmBckToRegList(String cnfrmBckToRegList) {
			this.cnfrmBckToRegList = cnfrmBckToRegList;
		}
		
		/*Technical_Onboarding*/
		private String alertEnterSId;
		private String alertEnterMId;
		private String alertEnterMidAsOneForHA;
		private String alertEnterMidAsOneForRadvision;
		private String aletEnterMidForRemSid;
		private String aletEnterMidForMain;
		private String alertClrCmErr;
		private String alertEnterValMainSeid;
		private String alertEnterSidMid;
		private String alertValSidMid;
		private String alertClrError;
		private String alertSeIdNotEmpForVP;
		private String alertPvtIPNotEmpty;
		private String alertEntCorrPvtIP;
		private String alertIpNotEmpForAccTypIP;
		private String alertInvIpAddr;
		private String alertEnterDialInForModem;
		private String alertInvalidDialIn;
		private String alertEnterSEIdForCaas;
		private String alertEnterSEIdForNonSysPrd;
		private String alertEnterAccType;
		private String alertEntUsrNamPwd;
		private String alertSameGtwayNtAllForPriAndSec;
		private String alertSelPriThanSecGtway;
		private String alertInvalidSoldTo;
		private String alertInvalidSeIdFormat;
		private String alertEnterValidSoldToOrSeId;
		private String alertInvalidSeIdAsInput;
		private String alertInvalidSIdFormat;
		private String alertAFIDMaxTenDig;
		private String alertMidNotZero;
		private String alertInvalidMId;
		private String alertRemQuanMinOne;
		private String alertEnterAtlOneRec;
		private String alertSellOneRecForSALMig;
		private String alertInfoRetCode;
		private String alertNoAccessToSoldTo;
		private String alertIPNotEmpWhenAccTypRASIP;
		private String alertPswrdRetypePwdSame;
		private String alertNoCommonGateway;
		private String cnfrmResetGateway;
		private String cnfrmRetestAlalrmConn;
		private String cnfrmCancWillDelete;
		private String cnfrmSubmitForTOB;
		private String cnfrmNavigationToSALAlarm;
		private String alertSoftwareReleaseErrorMsg;
		private String alertValAUXMCMainSEID;
		private String alertEnterAUXMCMainSEID;
		private String alertSelectAUXMCType;
		
		
		private String liveChatDisabled;


		public String getLiveChatDisabled() {
			return liveChatDisabled;
		}
		public void setLiveChatDisabled(String liveChatDisabled) {
			this.liveChatDisabled = liveChatDisabled;
		}
		public String getAlertSelectAUXMCType() {
			return alertSelectAUXMCType;
		}
		public void setAlertSelectAUXMCType(String alertSelectAUXMCType) {
			this.alertSelectAUXMCType = alertSelectAUXMCType;
		}
		public String getAlertValAUXMCMainSEID() {
			return alertValAUXMCMainSEID;
		}
		public void setAlertValAUXMCMainSEID(String alertValAUXMCMainSEID) {
			this.alertValAUXMCMainSEID = alertValAUXMCMainSEID;
		}
		public String getAlertEnterAUXMCMainSEID() {
			return alertEnterAUXMCMainSEID;
		}
		public void setAlertEnterAUXMCMainSEID(String alertEnterAUXMCMainSEID) {
			this.alertEnterAUXMCMainSEID = alertEnterAUXMCMainSEID;
		}
		public String getAlertSoftwareReleaseErrorMsg() {
			return alertSoftwareReleaseErrorMsg;
		}
		public void setAlertSoftwareReleaseErrorMsg(String alertSoftwareReleaseErrorMsg) {
			this.alertSoftwareReleaseErrorMsg = alertSoftwareReleaseErrorMsg;
		}
		public String getAlertEnterSId() {
			return alertEnterSId;
		}
		public void setAlertEnterSId(String alertEnterSId) {
			this.alertEnterSId = alertEnterSId;
		}
		public String getAlertEnterMId() {
			return alertEnterMId;
		}
		public void setAlertEnterMId(String alertEnterMId) {
			this.alertEnterMId = alertEnterMId;
		}
		public String getAlertEnterMidAsOneForHA() {
			return alertEnterMidAsOneForHA;
		}
		public void setAlertEnterMidAsOneForHA(String alertEnterMidAsOneForHA) {
			this.alertEnterMidAsOneForHA = alertEnterMidAsOneForHA;
		}
		public String getAlertEnterMidAsOneForRadvision() {
			return alertEnterMidAsOneForRadvision;
		}
		public void setAlertEnterMidAsOneForRadvision(
				String alertEnterMidAsOneForRadvision) {
			this.alertEnterMidAsOneForRadvision = alertEnterMidAsOneForRadvision;
		}
		public String getAletEnterMidForRemSid() {
			return aletEnterMidForRemSid;
		}
		public void setAletEnterMidForRemSid(String aletEnterMidForRemSid) {
			this.aletEnterMidForRemSid = aletEnterMidForRemSid;
		}
		public String getAletEnterMidForMain() {
			return aletEnterMidForMain;
		}
		public void setAletEnterMidForMain(String aletEnterMidForMain) {
			this.aletEnterMidForMain = aletEnterMidForMain;
		}
		public String getAlertClrCmErr() {
			return alertClrCmErr;
		}
		public void setAlertClrCmErr(String alertClrCmErr) {
			this.alertClrCmErr = alertClrCmErr;
		}
		public String getAlertEnterValMainSeid() {
			return alertEnterValMainSeid;
		}
		public void setAlertEnterValMainSeid(String alertEnterValMainSeid) {
			this.alertEnterValMainSeid = alertEnterValMainSeid;
		}
		public String getAlertEnterSidMid() {
			return alertEnterSidMid;
		}
		public void setAlertEnterSidMid(String alertEnterSidMid) {
			this.alertEnterSidMid = alertEnterSidMid;
		}
		public String getAlertValSidMid() {
			return alertValSidMid;
		}
		public void setAlertValSidMid(String alertValSidMid) {
			this.alertValSidMid = alertValSidMid;
		}
		public String getAlertClrError() {
			return alertClrError;
		}
		public void setAlertClrError(String alertClrError) {
			this.alertClrError = alertClrError;
		}
		public String getAlertSeIdNotEmpForVP() {
			return alertSeIdNotEmpForVP;
		}
		public void setAlertSeIdNotEmpForVP(String alertSeIdNotEmpForVP) {
			this.alertSeIdNotEmpForVP = alertSeIdNotEmpForVP;
		}
		public String getAlertPvtIPNotEmpty() {
			return alertPvtIPNotEmpty;
		}
		public void setAlertPvtIPNotEmpty(String alertPvtIPNotEmpty) {
			this.alertPvtIPNotEmpty = alertPvtIPNotEmpty;
		}
		public String getAlertEntCorrPvtIP() {
			return alertEntCorrPvtIP;
		}
		public void setAlertEntCorrPvtIP(String alertEntCorrPvtIP) {
			this.alertEntCorrPvtIP = alertEntCorrPvtIP;
		}
		public String getAlertIpNotEmpForAccTypIP() {
			return alertIpNotEmpForAccTypIP;
		}
		public void setAlertIpNotEmpForAccTypIP(String alertIpNotEmpForAccTypIP) {
			this.alertIpNotEmpForAccTypIP = alertIpNotEmpForAccTypIP;
		}
		public String getAlertInvIpAddr() {
			return alertInvIpAddr;
		}
		public void setAlertInvIpAddr(String alertInvIpAddr) {
			this.alertInvIpAddr = alertInvIpAddr;
		}
		public String getAlertEnterDialInForModem() {
			return alertEnterDialInForModem;
		}
		public void setAlertEnterDialInForModem(String alertEnterDialInForModem) {
			this.alertEnterDialInForModem = alertEnterDialInForModem;
		}
		public String getAlertInvalidDialIn() {
			return alertInvalidDialIn;
		}
		public void setAlertInvalidDialIn(String alertInvalidDialIn) {
			this.alertInvalidDialIn = alertInvalidDialIn;
		}
		public String getAlertEnterSEIdForCaas() {
			return alertEnterSEIdForCaas;
		}
		public void setAlertEnterSEIdForCaas(String alertEnterSEIdForCaas) {
			this.alertEnterSEIdForCaas = alertEnterSEIdForCaas;
		}
		public String getAlertEnterSEIdForNonSysPrd() {
			return alertEnterSEIdForNonSysPrd;
		}
		public void setAlertEnterSEIdForNonSysPrd(String alertEnterSEIdForNonSysPrd) {
			this.alertEnterSEIdForNonSysPrd = alertEnterSEIdForNonSysPrd;
		}
		public String getAlertEnterAccType() {
			return alertEnterAccType;
		}
		public void setAlertEnterAccType(String alertEnterAccType) {
			this.alertEnterAccType = alertEnterAccType;
		}
		public String getAlertEntUsrNamPwd() {
			return alertEntUsrNamPwd;
		}
		public void setAlertEntUsrNamPwd(String alertEntUsrNamPwd) {
			this.alertEntUsrNamPwd = alertEntUsrNamPwd;
		}
		public String getAlertSameGtwayNtAllForPriAndSec() {
			return alertSameGtwayNtAllForPriAndSec;
		}
		public void setAlertSameGtwayNtAllForPriAndSec(
				String alertSameGtwayNtAllForPriAndSec) {
			this.alertSameGtwayNtAllForPriAndSec = alertSameGtwayNtAllForPriAndSec;
		}
		public String getAlertSelPriThanSecGtway() {
			return alertSelPriThanSecGtway;
		}
		public void setAlertSelPriThanSecGtway(String alertSelPriThanSecGtway) {
			this.alertSelPriThanSecGtway = alertSelPriThanSecGtway;
		}
		public String getAlertInvalidSoldTo() {
			return alertInvalidSoldTo;
		}
		public void setAlertInvalidSoldTo(String alertInvalidSoldTo) {
			this.alertInvalidSoldTo = alertInvalidSoldTo;
		}
		public String getAlertInvalidSeIdFormat() {
			return alertInvalidSeIdFormat;
		}
		public void setAlertInvalidSeIdFormat(String alertInvalidSeIdFormat) {
			this.alertInvalidSeIdFormat = alertInvalidSeIdFormat;
		}
		public String getAlertEnterValidSoldToOrSeId() {
			return alertEnterValidSoldToOrSeId;
		}
		public void setAlertEnterValidSoldToOrSeId(String alertEnterValidSoldToOrSeId) {
			this.alertEnterValidSoldToOrSeId = alertEnterValidSoldToOrSeId;
		}
		public String getAlertInvalidSeIdAsInput() {
			return alertInvalidSeIdAsInput;
		}
		public void setAlertInvalidSeIdAsInput(String alertInvalidSeIdAsInput) {
			this.alertInvalidSeIdAsInput = alertInvalidSeIdAsInput;
		}
		public String getAlertInvalidSIdFormat() {
			return alertInvalidSIdFormat;
		}
		public void setAlertInvalidSIdFormat(String alertInvalidSIdFormat) {
			this.alertInvalidSIdFormat = alertInvalidSIdFormat;
		}
		public String getAlertAFIDMaxTenDig() {
			return alertAFIDMaxTenDig;
		}
		public void setAlertAFIDMaxTenDig(String alertAFIDMaxTenDig) {
			this.alertAFIDMaxTenDig = alertAFIDMaxTenDig;
		}
		public String getAlertMidNotZero() {
			return alertMidNotZero;
		}
		public void setAlertMidNotZero(String alertMidNotZero) {
			this.alertMidNotZero = alertMidNotZero;
		}
		public String getAlertInvalidMId() {
			return alertInvalidMId;
		}
		public void setAlertInvalidMId(String alertInvalidMId) {
			this.alertInvalidMId = alertInvalidMId;
		}
		public String getAlertRemQuanMinOne() {
			return alertRemQuanMinOne;
		}
		public void setAlertRemQuanMinOne(String alertRemQuanMinOne) {
			this.alertRemQuanMinOne = alertRemQuanMinOne;
		}
		public String getAlertEnterAtlOneRec() {
			return alertEnterAtlOneRec;
		}
		public void setAlertEnterAtlOneRec(String alertEnterAtlOneRec) {
			this.alertEnterAtlOneRec = alertEnterAtlOneRec;
		}
		public String getAlertSellOneRecForSALMig() {
			return alertSellOneRecForSALMig;
		}
		public void setAlertSellOneRecForSALMig(String alertSellOneRecForSALMig) {
			this.alertSellOneRecForSALMig = alertSellOneRecForSALMig;
		}
		public String getAlertInfoRetCode() {
			return alertInfoRetCode;
		}
		public void setAlertInfoRetCode(String alertInfoRetCode) {
			this.alertInfoRetCode = alertInfoRetCode;
		}
		public String getAlertNoAccessToSoldTo() {
			return alertNoAccessToSoldTo;
		}
		public void setAlertNoAccessToSoldTo(String alertNoAccessToSoldTo) {
			this.alertNoAccessToSoldTo = alertNoAccessToSoldTo;
		}
		public String getAlertIPNotEmpWhenAccTypRASIP() {
			return alertIPNotEmpWhenAccTypRASIP;
		}
		public void setAlertIPNotEmpWhenAccTypRASIP(
				String alertIPNotEmpWhenAccTypRASIP) {
			this.alertIPNotEmpWhenAccTypRASIP = alertIPNotEmpWhenAccTypRASIP;
		}
		public String getAlertPswrdRetypePwdSame() {
			return alertPswrdRetypePwdSame;
		}
		public void setAlertPswrdRetypePwdSame(String alertPswrdRetypePwdSame) {
			this.alertPswrdRetypePwdSame = alertPswrdRetypePwdSame;
		}
		public String getAlertNoCommonGateway() {
			return alertNoCommonGateway;
		}
		public void setAlertNoCommonGateway(String alertNoCommonGateway) {
			this.alertNoCommonGateway = alertNoCommonGateway;
		}
		public String getCnfrmResetGateway() {
			return cnfrmResetGateway;
		}
		public void setCnfrmResetGateway(String cnfrmResetGateway) {
			this.cnfrmResetGateway = cnfrmResetGateway;
		}
		public String getCnfrmRetestAlalrmConn() {
			return cnfrmRetestAlalrmConn;
		}
		public void setCnfrmRetestAlalrmConn(String cnfrmRetestAlalrmConn) {
			this.cnfrmRetestAlalrmConn = cnfrmRetestAlalrmConn;
		}
		
		public String getCnfrmCancWillDelete() {
			return cnfrmCancWillDelete;
		}
		public void setCnfrmCancWillDelete(String cnfrmCancWillDelete) {
			this.cnfrmCancWillDelete = cnfrmCancWillDelete;
		}
		public String getCnfrmSubmitForTOB() {
			return cnfrmSubmitForTOB;
		}
		public void setCnfrmSubmitForTOB(String cnfrmSubmitForTOB) {
			this.cnfrmSubmitForTOB = cnfrmSubmitForTOB;
		}
		public String getCnfrmNavigationToSALAlarm() {
			return cnfrmNavigationToSALAlarm;
		}
		public void setCnfrmNavigationToSALAlarm(String cnfrmNavigationToSALAlarm) {
			this.cnfrmNavigationToSALAlarm = cnfrmNavigationToSALAlarm;
		}
		/* Common Validation for Data table.Column Filter */
		private String alertToDateGrtFromDate;
		private String alertEnterToDate;
		private String alertEnterFromDate;
		private String alertEnterToFromDate;

		public String getAlertToDateGrtFromDate() {
			return alertToDateGrtFromDate;
		}
		public void setAlertToDateGrtFromDate(String alertToDateGrtFromDate) {
			this.alertToDateGrtFromDate = alertToDateGrtFromDate;
		}
		public String getAlertEnterToDate() {
			return alertEnterToDate;
		}
		public void setAlertEnterToDate(String alertEnterToDate) {
			this.alertEnterToDate = alertEnterToDate;
		}
		public String getAlertEnterToFromDate() {
			return alertEnterToFromDate;
		}
		public void setAlertEnterToFromDate(String alertEnterToFromDate) {
			this.alertEnterToFromDate = alertEnterToFromDate;
		}
		public String getAlertEnterFromDate() {
			return alertEnterFromDate;
		}
		public void setAlertEnterFromDate(String alertEnterFromDate) {
			this.alertEnterFromDate = alertEnterFromDate;
		}
		// End entries added by Vipin
		// Entries added by Rajesh
		private String revisedQtyErrForSerializedAsset; 
		
		private String revisedQtyErrForTechOnBorded;
		
		private String revisedQtyNoNegativeErrMsg;
		
		private String noValForUpdateErrMsg;
		private String accUpdateSuccessMsgPart1;
		private String accUpdateSuccessMsgPart2;
		private String accUpdateErrMsg;
		
		private String invalidCountryCityPostalAccErrMsg;
		private String searchAccErrMsg;
		
		private String invalidSoldToAccErrMsg;
		private String invalidTokenAccErrMsg;	
		private String noRecFoundAccErrMsg;
		
		private String accUpdateErrMsg2;
		private String bpNotAuthorizedForSoldToErrMsg;
		private String validateSoldToErrMsg;
		

		private String eqmIncorrectFromSoldToErrMsg;
		private String eqmIncorrectToSoldToErrMsg;
		private String eqmIncorrectFromAndToSoldToErrMsg;
		private String eqmUnauthorizedSoldToErrMsg;
		private String eqmSameFromAndToSoldToErrMsg;  
		
		private String eqmAssignedQtyErrMsg;
		private String eqmSeletOneEqpErrMsg1; 
		private String eqmSeletOneEqpErrMsg2;
		private String eqmActiveContrsctSelErrMsg;
		private String eqmRequestSuccessMsg;
		private String eqmOutageMsg; 
		private String eqmCancelToHomePageErrMsg;
		private String eqmCancelToRegPageErrMsg;
		private String eqmReqSubmitConfMsg;
		private String eqmQtyLessThanZeroErrMsg; 
		private String eqmQtyGreaterThanExisErrMsg; 
		private String eqmZeroQtyErrMsg;
		private String eqmSaveSuccMsg;
		
		private String eqmUnableToSaveErrMsg; 
		private String eqmProcessErrMsg;
		private String eqmSapDownErrMsg;
		private String eqmProcessFailErrMsg;
		private String eqmBackendSysErrMsg;
		private String eqmActTrbTickErrMsg;
		private String eqmMatListEmptyerrMsg;
		private String eqmfailedWhileProcessErrMsg;
		
		private String salNoGateWayOnSoldToErrMsg;
		private String salNoAccessToSolToErrMsg;
		private String salFetchErrMsg;	
		private String salEndToEndRegConfirmMsg;
		private String salConfirmAssetMigrationMsg;
		
		private String salSomethingWrongErrMsg;
		private String salRemoteAccessReqErrMsg;
		private String salSeleSeidRequiedMsg;
		private String salValidateConnectivityConfirmMsg;
		
		private String regSelectedCancRegErrMsg;
		private String regInvalidEmailErrMsg;
		
		private String siteRegSoldToNotExistErrMsg;

		private String scvInvalidPhoneNoErrMsg;
		private String scvPurchaseOrderReqErrMsg;
		private String scvEmptyFirstNameErrMsg;
		private String scvEmptyFirstLastErrMsg;
		private String scvInvalidEmailErrMsg;
		private String scvEmptyNotifEmailErrMsg;
		private String scvInvalidDate;
		private String scvCutoverDateErrMsg;
		private String scvMaxCharErrMsg;
		
		private String scvTypedTextMsg;
		
		
		private String scvZipLengthErrMsg;
		private String scvInvalidCharInZipErrMsg;
		private String scvZipCodeFormaterrMsg;
		private String scvEmptyGrtNotiPhNoErrMsg;
		private String scvEmptyPhNoErrMsg;
		private String scvInvalidGrtNotiPhNoErrMsg;
		private String scvInvalidPhNoErrMsg;
		
		
		private String scvBufferOvFlwMsgMiddle;
		private String scvBufferOvFlwMsgRegName;
		private String scvBufferOvFlwMsgRegId;
		private String scvBufferOvFlwMsgCompany;
		private String scvBufferOvFlwMsgSoldTo;
		
		private String nofileChosenForUploadErrMsg;
		private String revisedQutyEqlExistQtyErrMsg;
		
		public String getRevisedQtyErrForSerializedAsset() {
			return revisedQtyErrForSerializedAsset;
		}
		public void setRevisedQtyErrForSerializedAsset(
				String revisedQtyErrForSerializedAsset) {
			this.revisedQtyErrForSerializedAsset = revisedQtyErrForSerializedAsset;
		}
		public String getRevisedQtyErrForTechOnBorded() {
			return revisedQtyErrForTechOnBorded;
		}
		public void setRevisedQtyErrForTechOnBorded(String revisedQtyErrForTechOnBorded) {
			this.revisedQtyErrForTechOnBorded = revisedQtyErrForTechOnBorded;
		}
		public String getRevisedQtyNoNegativeErrMsg() {
			return revisedQtyNoNegativeErrMsg;
		}
		public void setRevisedQtyNoNegativeErrMsg(String revisedQtyNoNegativeErrMsg) {
			this.revisedQtyNoNegativeErrMsg = revisedQtyNoNegativeErrMsg;
		}
		public String getNoValForUpdateErrMsg() {
			return noValForUpdateErrMsg;
		}
		public void setNoValForUpdateErrMsg(String noValForUpdateErrMsg) {
			this.noValForUpdateErrMsg = noValForUpdateErrMsg;
		}
		public String getAccUpdateSuccessMsgPart1() {
			return accUpdateSuccessMsgPart1;
		}
		public void setAccUpdateSuccessMsgPart1(String accUpdateSuccessMsgPart1) {
			this.accUpdateSuccessMsgPart1 = accUpdateSuccessMsgPart1;
		}
		public String getAccUpdateSuccessMsgPart2() {
			return accUpdateSuccessMsgPart2;
		}
		public void setAccUpdateSuccessMsgPart2(String accUpdateSuccessMsgPart2) {
			this.accUpdateSuccessMsgPart2 = accUpdateSuccessMsgPart2;
		}
		public String getAccUpdateErrMsg() {
			return accUpdateErrMsg;
		}
		public void setAccUpdateErrMsg(String accUpdateErrMsg) {
			this.accUpdateErrMsg = accUpdateErrMsg;
		}
		public String getInvalidCountryCityPostalAccErrMsg() {
			return invalidCountryCityPostalAccErrMsg;
		}
		public void setInvalidCountryCityPostalAccErrMsg(
				String invalidCountryCityPostalAccErrMsg) {
			this.invalidCountryCityPostalAccErrMsg = invalidCountryCityPostalAccErrMsg;
		}
		public String getSearchAccErrMsg() {
			return searchAccErrMsg;
		}
		public void setSearchAccErrMsg(String searchAccErrMsg) {
			this.searchAccErrMsg = searchAccErrMsg;
		}
		public String getInvalidSoldToAccErrMsg() {
			return invalidSoldToAccErrMsg;
		}
		public void setInvalidSoldToAccErrMsg(String invalidSoldToAccErrMsg) {
			this.invalidSoldToAccErrMsg = invalidSoldToAccErrMsg;
		}
		public String getInvalidTokenAccErrMsg() {
			return invalidTokenAccErrMsg;
		}
		public void setInvalidTokenAccErrMsg(String invalidTokenAccErrMsg) {
			this.invalidTokenAccErrMsg = invalidTokenAccErrMsg;
		}
		public String getNoRecFoundAccErrMsg() {
			return noRecFoundAccErrMsg;
		}
		public void setNoRecFoundAccErrMsg(String noRecFoundAccErrMsg) {
			this.noRecFoundAccErrMsg = noRecFoundAccErrMsg;
		}
		public String getAccUpdateErrMsg2() {
			return accUpdateErrMsg2;
		}
		public void setAccUpdateErrMsg2(String accUpdateErrMsg2) {
			this.accUpdateErrMsg2 = accUpdateErrMsg2;
		}
		public String getBpNotAuthorizedForSoldToErrMsg() {
			return bpNotAuthorizedForSoldToErrMsg;
		}
		public void setBpNotAuthorizedForSoldToErrMsg(
				String bpNotAuthorizedForSoldToErrMsg) {
			this.bpNotAuthorizedForSoldToErrMsg = bpNotAuthorizedForSoldToErrMsg;
		}
		public String getValidateSoldToErrMsg() {
			return validateSoldToErrMsg;
		}
		public void setValidateSoldToErrMsg(String validateSoldToErrMsg) {
			this.validateSoldToErrMsg = validateSoldToErrMsg;
		}
		public String getEqmIncorrectFromSoldToErrMsg() {
			return eqmIncorrectFromSoldToErrMsg;
		}
		public void setEqmIncorrectFromSoldToErrMsg(String eqmIncorrectFromSoldToErrMsg) {
			this.eqmIncorrectFromSoldToErrMsg = eqmIncorrectFromSoldToErrMsg;
		}
		public String getEqmIncorrectToSoldToErrMsg() {
			return eqmIncorrectToSoldToErrMsg;
		}
		public void setEqmIncorrectToSoldToErrMsg(String eqmIncorrectToSoldToErrMsg) {
			this.eqmIncorrectToSoldToErrMsg = eqmIncorrectToSoldToErrMsg;
		}
		public String getEqmIncorrectFromAndToSoldToErrMsg() {
			return eqmIncorrectFromAndToSoldToErrMsg;
		}
		public void setEqmIncorrectFromAndToSoldToErrMsg(
				String eqmIncorrectFromAndToSoldToErrMsg) {
			this.eqmIncorrectFromAndToSoldToErrMsg = eqmIncorrectFromAndToSoldToErrMsg;
		}
		public String getEqmUnauthorizedSoldToErrMsg() {
			return eqmUnauthorizedSoldToErrMsg;
		}
		public void setEqmUnauthorizedSoldToErrMsg(String eqmUnauthorizedSoldToErrMsg) {
			this.eqmUnauthorizedSoldToErrMsg = eqmUnauthorizedSoldToErrMsg;
		}
		public String getEqmSameFromAndToSoldToErrMsg() {
			return eqmSameFromAndToSoldToErrMsg;
		}
		public void setEqmSameFromAndToSoldToErrMsg(String eqmSameFromAndToSoldToErrMsg) {
			this.eqmSameFromAndToSoldToErrMsg = eqmSameFromAndToSoldToErrMsg;
		}
		public String getEqmAssignedQtyErrMsg() {
			return eqmAssignedQtyErrMsg;
		}
		public void setEqmAssignedQtyErrMsg(String eqmAssignedQtyErrMsg) {
			this.eqmAssignedQtyErrMsg = eqmAssignedQtyErrMsg;
		}
		public String getEqmSeletOneEqpErrMsg1() {
			return eqmSeletOneEqpErrMsg1;
		}
		public void setEqmSeletOneEqpErrMsg1(String eqmSeletOneEqpErrMsg1) {
			this.eqmSeletOneEqpErrMsg1 = eqmSeletOneEqpErrMsg1;
		}
		public String getEqmSeletOneEqpErrMsg2() {
			return eqmSeletOneEqpErrMsg2;
		}
		public void setEqmSeletOneEqpErrMsg2(String eqmSeletOneEqpErrMsg2) {
			this.eqmSeletOneEqpErrMsg2 = eqmSeletOneEqpErrMsg2;
		}
		public String getEqmActiveContrsctSelErrMsg() {
			return eqmActiveContrsctSelErrMsg;
		}
		public void setEqmActiveContrsctSelErrMsg(String eqmActiveContrsctSelErrMsg) {
			this.eqmActiveContrsctSelErrMsg = eqmActiveContrsctSelErrMsg;
		}
		public String getEqmRequestSuccessMsg() {
			return eqmRequestSuccessMsg;
		}
		public void setEqmRequestSuccessMsg(String eqmRequestSuccessMsg) {
			this.eqmRequestSuccessMsg = eqmRequestSuccessMsg;
		}
		public String getEqmOutageMsg() {
			return eqmOutageMsg;
		}
		public void setEqmOutageMsg(String eqmOutageMsg) {
			this.eqmOutageMsg = eqmOutageMsg;
		}
		public String getEqmCancelToHomePageErrMsg() {
			return eqmCancelToHomePageErrMsg;
		}
		public void setEqmCancelToHomePageErrMsg(String eqmCancelToHomePageErrMsg) {
			this.eqmCancelToHomePageErrMsg = eqmCancelToHomePageErrMsg;
		}
		public String getEqmCancelToRegPageErrMsg() {
			return eqmCancelToRegPageErrMsg;
		}
		public void setEqmCancelToRegPageErrMsg(String eqmCancelToRegPageErrMsg) {
			this.eqmCancelToRegPageErrMsg = eqmCancelToRegPageErrMsg;
		}
		public String getEqmReqSubmitConfMsg() {
			return eqmReqSubmitConfMsg;
		}
		public void setEqmReqSubmitConfMsg(String eqmReqSubmitConfMsg) {
			this.eqmReqSubmitConfMsg = eqmReqSubmitConfMsg;
		}
		public String getEqmQtyLessThanZeroErrMsg() {
			return eqmQtyLessThanZeroErrMsg;
		}
		public void setEqmQtyLessThanZeroErrMsg(String eqmQtyLessThanZeroErrMsg) {
			this.eqmQtyLessThanZeroErrMsg = eqmQtyLessThanZeroErrMsg;
		}
		public String getEqmQtyGreaterThanExisErrMsg() {
			return eqmQtyGreaterThanExisErrMsg;
		}
		public void setEqmQtyGreaterThanExisErrMsg(String eqmQtyGreaterThanExisErrMsg) {
			this.eqmQtyGreaterThanExisErrMsg = eqmQtyGreaterThanExisErrMsg;
		}
		public String getEqmZeroQtyErrMsg() {
			return eqmZeroQtyErrMsg;
		}
		public void setEqmZeroQtyErrMsg(String eqmZeroQtyErrMsg) {
			this.eqmZeroQtyErrMsg = eqmZeroQtyErrMsg;
		}
		public String getEqmSaveSuccMsg() {
			return eqmSaveSuccMsg;
		}
		public void setEqmSaveSuccMsg(String eqmSaveSuccMsg) {
			this.eqmSaveSuccMsg = eqmSaveSuccMsg;
		}
		public String getEqmUnableToSaveErrMsg() {
			return eqmUnableToSaveErrMsg;
		}
		public void setEqmUnableToSaveErrMsg(String eqmUnableToSaveErrMsg) {
			this.eqmUnableToSaveErrMsg = eqmUnableToSaveErrMsg;
		}
		public String getEqmProcessErrMsg() {
			return eqmProcessErrMsg;
		}
		public void setEqmProcessErrMsg(String eqmProcessErrMsg) {
			this.eqmProcessErrMsg = eqmProcessErrMsg;
		}
		public String getEqmSapDownErrMsg() {
			return eqmSapDownErrMsg;
		}
		public void setEqmSapDownErrMsg(String eqmSapDownErrMsg) {
			this.eqmSapDownErrMsg = eqmSapDownErrMsg;
		}
		public String getEqmProcessFailErrMsg() {
			return eqmProcessFailErrMsg;
		}
		public void setEqmProcessFailErrMsg(String eqmProcessFailErrMsg) {
			this.eqmProcessFailErrMsg = eqmProcessFailErrMsg;
		}
		public String getEqmBackendSysErrMsg() {
			return eqmBackendSysErrMsg;
		}
		public void setEqmBackendSysErrMsg(String eqmBackendSysErrMsg) {
			this.eqmBackendSysErrMsg = eqmBackendSysErrMsg;
		}
		public String getEqmActTrbTickErrMsg() {
			return eqmActTrbTickErrMsg;
		}
		public void setEqmActTrbTickErrMsg(String eqmActTrbTickErrMsg) {
			this.eqmActTrbTickErrMsg = eqmActTrbTickErrMsg;
		}
		public String getEqmMatListEmptyerrMsg() {
			return eqmMatListEmptyerrMsg;
		}
		public void setEqmMatListEmptyerrMsg(String eqmMatListEmptyerrMsg) {
			this.eqmMatListEmptyerrMsg = eqmMatListEmptyerrMsg;
		}
		public String getEqmfailedWhileProcessErrMsg() {
			return eqmfailedWhileProcessErrMsg;
		}
		public void setEqmfailedWhileProcessErrMsg(String eqmfailedWhileProcessErrMsg) {
			this.eqmfailedWhileProcessErrMsg = eqmfailedWhileProcessErrMsg;
		}
		public String getSalNoGateWayOnSoldToErrMsg() {
			return salNoGateWayOnSoldToErrMsg;
		}
		public void setSalNoGateWayOnSoldToErrMsg(String salNoGateWayOnSoldToErrMsg) {
			this.salNoGateWayOnSoldToErrMsg = salNoGateWayOnSoldToErrMsg;
		}
		public String getSalNoAccessToSolToErrMsg() {
			return salNoAccessToSolToErrMsg;
		}
		public void setSalNoAccessToSolToErrMsg(String salNoAccessToSolToErrMsg) {
			this.salNoAccessToSolToErrMsg = salNoAccessToSolToErrMsg;
		}
		public String getSalFetchErrMsg() {
			return salFetchErrMsg;
		}
		public void setSalFetchErrMsg(String salFetchErrMsg) {
			this.salFetchErrMsg = salFetchErrMsg;
		}
		public String getSalEndToEndRegConfirmMsg() {
			return salEndToEndRegConfirmMsg;
		}
		public void setSalEndToEndRegConfirmMsg(String salEndToEndRegConfirmMsg) {
			this.salEndToEndRegConfirmMsg = salEndToEndRegConfirmMsg;
		}
		public String getSalConfirmAssetMigrationMsg() {
			return salConfirmAssetMigrationMsg;
		}
		public void setSalConfirmAssetMigrationMsg(
				String salConfirmAssetMigrationMsg) {
			this.salConfirmAssetMigrationMsg = salConfirmAssetMigrationMsg;
		}
		public String getSalSomethingWrongErrMsg() {
			return salSomethingWrongErrMsg;
		}
		public void setSalSomethingWrongErrMsg(String salSomethingWrongErrMsg) {
			this.salSomethingWrongErrMsg = salSomethingWrongErrMsg;
		}
		public String getSalRemoteAccessReqErrMsg() {
			return salRemoteAccessReqErrMsg;
		}
		public void setSalRemoteAccessReqErrMsg(String salRemoteAccessReqErrMsg) {
			this.salRemoteAccessReqErrMsg = salRemoteAccessReqErrMsg;
		}
		public String getSalSeleSeidRequiedMsg() {
			return salSeleSeidRequiedMsg;
		}
		public void setSalSeleSeidRequiedMsg(String salSeleSeidRequiedMsg) {
			this.salSeleSeidRequiedMsg = salSeleSeidRequiedMsg;
		}
		public String getSalValidateConnectivityConfirmMsg() {
			return salValidateConnectivityConfirmMsg;
		}
		public void setSalValidateConnectivityConfirmMsg(
				String salValidateConnectivityConfirmMsg) {
			this.salValidateConnectivityConfirmMsg = salValidateConnectivityConfirmMsg;
		}
		public String getRegSelectedCancRegErrMsg() {
			return regSelectedCancRegErrMsg;
		}
		public void setRegSelectedCancRegErrMsg(String regSelectedCancRegErrMsg) {
			this.regSelectedCancRegErrMsg = regSelectedCancRegErrMsg;
		}
		public String getRegInvalidEmailErrMsg() {
			return regInvalidEmailErrMsg;
		}
		public void setRegInvalidEmailErrMsg(String regInvalidEmailErrMsg) {
			this.regInvalidEmailErrMsg = regInvalidEmailErrMsg;
		}
		public String getSiteRegSoldToNotExistErrMsg() {
			return siteRegSoldToNotExistErrMsg;
		}
		public void setSiteRegSoldToNotExistErrMsg(
				String siteRegSoldToNotExistErrMsg) {
			this.siteRegSoldToNotExistErrMsg = siteRegSoldToNotExistErrMsg;
		}
		public String getScvInvalidPhoneNoErrMsg() {
			return scvInvalidPhoneNoErrMsg;
		}
		public void setScvInvalidPhoneNoErrMsg(String scvInvalidPhoneNoErrMsg) {
			this.scvInvalidPhoneNoErrMsg = scvInvalidPhoneNoErrMsg;
		}
		public String getScvPurchaseOrderReqErrMsg() {
			return scvPurchaseOrderReqErrMsg;
		}
		public void setScvPurchaseOrderReqErrMsg(String scvPurchaseOrderReqErrMsg) {
			this.scvPurchaseOrderReqErrMsg = scvPurchaseOrderReqErrMsg;
		}
		public String getScvEmptyFirstNameErrMsg() {
			return scvEmptyFirstNameErrMsg;
		}
		public void setScvEmptyFirstNameErrMsg(String scvEmptyFirstNameErrMsg) {
			this.scvEmptyFirstNameErrMsg = scvEmptyFirstNameErrMsg;
		}
		public String getScvEmptyFirstLastErrMsg() {
			return scvEmptyFirstLastErrMsg;
		}
		public void setScvEmptyFirstLastErrMsg(String scvEmptyFirstLastErrMsg) {
			this.scvEmptyFirstLastErrMsg = scvEmptyFirstLastErrMsg;
		}
		public String getScvInvalidEmailErrMsg() {
			return scvInvalidEmailErrMsg;
		}
		public void setScvInvalidEmailErrMsg(String scvInvalidEmailErrMsg) {
			this.scvInvalidEmailErrMsg = scvInvalidEmailErrMsg;
		}
		public String getScvEmptyNotifEmailErrMsg() {
			return scvEmptyNotifEmailErrMsg;
		}
		public void setScvEmptyNotifEmailErrMsg(String scvEmptyNotifEmailErrMsg) {
			this.scvEmptyNotifEmailErrMsg = scvEmptyNotifEmailErrMsg;
		}
		public String getScvInvalidDate() {
			return scvInvalidDate;
		}
		public void setScvInvalidDate(String scvInvalidDate) {
			this.scvInvalidDate = scvInvalidDate;
		}
		public String getScvCutoverDateErrMsg() {
			return scvCutoverDateErrMsg;
		}
		public void setScvCutoverDateErrMsg(String scvCutoverDateErrMsg) {
			this.scvCutoverDateErrMsg = scvCutoverDateErrMsg;
		}
		public String getScvMaxCharErrMsg() {
			return scvMaxCharErrMsg;
		}
		public void setScvMaxCharErrMsg(String scvMaxCharErrMsg) {
			this.scvMaxCharErrMsg = scvMaxCharErrMsg;
		}
		public String getScvTypedTextMsg() {
			return scvTypedTextMsg;
		}
		public void setScvTypedTextMsg(String scvTypedTextMsg) {
			this.scvTypedTextMsg = scvTypedTextMsg;
		}
		public String getScvZipLengthErrMsg() {
			return scvZipLengthErrMsg;
		}
		public void setScvZipLengthErrMsg(String scvZipLengthErrMsg) {
			this.scvZipLengthErrMsg = scvZipLengthErrMsg;
		}
		public String getScvInvalidCharInZipErrMsg() {
			return scvInvalidCharInZipErrMsg;
		}
		public void setScvInvalidCharInZipErrMsg(String scvInvalidCharInZipErrMsg) {
			this.scvInvalidCharInZipErrMsg = scvInvalidCharInZipErrMsg;
		}
		public String getScvZipCodeFormaterrMsg() {
			return scvZipCodeFormaterrMsg;
		}
		public void setScvZipCodeFormaterrMsg(String scvZipCodeFormaterrMsg) {
			this.scvZipCodeFormaterrMsg = scvZipCodeFormaterrMsg;
		}
		public String getScvEmptyGrtNotiPhNoErrMsg() {
			return scvEmptyGrtNotiPhNoErrMsg;
		}
		public void setScvEmptyGrtNotiPhNoErrMsg(String scvEmptyGrtNotiPhNoErrMsg) {
			this.scvEmptyGrtNotiPhNoErrMsg = scvEmptyGrtNotiPhNoErrMsg;
		}
		public String getScvEmptyPhNoErrMsg() {
			return scvEmptyPhNoErrMsg;
		}
		public void setScvEmptyPhNoErrMsg(String scvEmptyPhNoErrMsg) {
			this.scvEmptyPhNoErrMsg = scvEmptyPhNoErrMsg;
		}
		public String getScvInvalidGrtNotiPhNoErrMsg() {
			return scvInvalidGrtNotiPhNoErrMsg;
		}
		public void setScvInvalidGrtNotiPhNoErrMsg(String scvInvalidGrtNotiPhNoErrMsg) {
			this.scvInvalidGrtNotiPhNoErrMsg = scvInvalidGrtNotiPhNoErrMsg;
		}
		public String getScvInvalidPhNoErrMsg() {
			return scvInvalidPhNoErrMsg;
		}
		public void setScvInvalidPhNoErrMsg(String scvInvalidPhNoErrMsg) {
			this.scvInvalidPhNoErrMsg = scvInvalidPhNoErrMsg;
		}
		public String getScvBufferOvFlwMsgMiddle() {
			return scvBufferOvFlwMsgMiddle;
		}
		public void setScvBufferOvFlwMsgMiddle(String scvBufferOvFlwMsgMiddle) {
			this.scvBufferOvFlwMsgMiddle = scvBufferOvFlwMsgMiddle;
		}
		public String getScvBufferOvFlwMsgRegName() {
			return scvBufferOvFlwMsgRegName;
		}
		public void setScvBufferOvFlwMsgRegName(String scvBufferOvFlwMsgRegName) {
			this.scvBufferOvFlwMsgRegName = scvBufferOvFlwMsgRegName;
		}
		public String getScvBufferOvFlwMsgRegId() {
			return scvBufferOvFlwMsgRegId;
		}
		public void setScvBufferOvFlwMsgRegId(String scvBufferOvFlwMsgRegId) {
			this.scvBufferOvFlwMsgRegId = scvBufferOvFlwMsgRegId;
		}
		public String getScvBufferOvFlwMsgCompany() {
			return scvBufferOvFlwMsgCompany;
		}
		public void setScvBufferOvFlwMsgCompany(String scvBufferOvFlwMsgCompany) {
			this.scvBufferOvFlwMsgCompany = scvBufferOvFlwMsgCompany;
		}
		public String getScvBufferOvFlwMsgSoldTo() {
			return scvBufferOvFlwMsgSoldTo;
		}
		public void setScvBufferOvFlwMsgSoldTo(String scvBufferOvFlwMsgSoldTo) {
			this.scvBufferOvFlwMsgSoldTo = scvBufferOvFlwMsgSoldTo;
		}
		/* ESNA changes */
		public String getErrEsnaCode() {
			return errEsnaCode;
		}
		public void setErrEsnaCode(String errEsnaCode) {
			this.errEsnaCode = errEsnaCode;
		}
		
		public String getExclEsnaErrMsg() {
			return exclEsnaErrMsg;
		}
		public void setExclEsnaErrMsg(String exclEsnaErrMsg) {
			this.exclEsnaErrMsg = exclEsnaErrMsg;
		}
		// End entries added by Rajesh		
		
		private String scvBufferOvFlwInvalidSoldToFmt;
		private String accCreateEmailNotifMsg;
		private String fileUploadInProcessMsg;
		private String pleaseTryAgainErrMsg;
		private String accCrtInvalidBPLinkIdErrMsg;
		private String accCrtInvalidTokenNonUSErrMsg;
		private String accCrtInvalidTokenUSErrMsg;
		private String accCrtValidTokenMsg;
		private String accCrtSetUpHierarchyMsg;
		private String accCrtValidateUSGovtMsg;
		private String accCrtValidateSIAccMsg;
		private String accCrtValidateSPAccMsg;
		private String accCrtEmailNotiErrMsg;
		private String accCrtInvalidSoldToErrMsg;
		private String accCrtSoldToNotExistsErrMsg;
		private String accCrtInvalidSoldToNonUSErrMsg;
		private String accCrtSoldToValidatedMsg;
		private String tobSslVpnNotAllowedErrMsg;
		private String tobExceptionInFindinfSRErrMsg;
		private String tobRegSetpBFoundForSeidErrMsg;
		private String tobExceptionWhileFindingAlarmErrMsg;
		private String tobSubmitRegPrdtErrMsg;
		private String tobSubmitRedayToProcesstErrMsg;
		private String tobGrtProblemErrMsg;
		private String tobRetrieveErrMsg;
		private String tobErrWhileProcessingErrMsg;
		private String rvNotAllowdedNortelMaestroErrMsg;
		
		private String rvNortelMaestroWithContractErrMsg;
		
		public String getScvBufferOvFlwInvalidSoldToFmt() {
			return scvBufferOvFlwInvalidSoldToFmt;
		}
		public void setScvBufferOvFlwInvalidSoldToFmt(
				String scvBufferOvFlwInvalidSoldToFmt) {
			this.scvBufferOvFlwInvalidSoldToFmt = scvBufferOvFlwInvalidSoldToFmt;
		}
		public String getAccCreateEmailNotifMsg() {
			return accCreateEmailNotifMsg;
		}
		public void setAccCreateEmailNotifMsg(String accCreateEmailNotifMsg) {
			this.accCreateEmailNotifMsg = accCreateEmailNotifMsg;
		}
		public String getFileUploadInProcessMsg() {
			return fileUploadInProcessMsg;
		}
		public void setFileUploadInProcessMsg(String fileUploadInProcessMsg) {
			this.fileUploadInProcessMsg = fileUploadInProcessMsg;
		}
		public String getPleaseTryAgainErrMsg() {
			return pleaseTryAgainErrMsg;
		}
		public void setPleaseTryAgainErrMsg(String pleaseTryAgainErrMsg) {
			this.pleaseTryAgainErrMsg = pleaseTryAgainErrMsg;
		}
		public String getAccCrtInvalidBPLinkIdErrMsg() {
			return accCrtInvalidBPLinkIdErrMsg;
		}
		public void setAccCrtInvalidBPLinkIdErrMsg(String accCrtInvalidBPLinkIdErrMsg) {
			this.accCrtInvalidBPLinkIdErrMsg = accCrtInvalidBPLinkIdErrMsg;
		}
		public String getAccCrtInvalidTokenNonUSErrMsg() {
			return accCrtInvalidTokenNonUSErrMsg;
		}
		public void setAccCrtInvalidTokenNonUSErrMsg(
				String accCrtInvalidTokenNonUSErrMsg) {
			this.accCrtInvalidTokenNonUSErrMsg = accCrtInvalidTokenNonUSErrMsg;
		}
		public String getAccCrtInvalidTokenUSErrMsg() {
			return accCrtInvalidTokenUSErrMsg;
		}
		public void setAccCrtInvalidTokenUSErrMsg(String accCrtInvalidTokenUSErrMsg) {
			this.accCrtInvalidTokenUSErrMsg = accCrtInvalidTokenUSErrMsg;
		}
		public String getAccCrtValidTokenMsg() {
			return accCrtValidTokenMsg;
		}
		public void setAccCrtValidTokenMsg(String accCrtValidTokenMsg) {
			this.accCrtValidTokenMsg = accCrtValidTokenMsg;
		}
		public String getAccCrtSetUpHierarchyMsg() {
			return accCrtSetUpHierarchyMsg;
		}
		public void setAccCrtSetUpHierarchyMsg(String accCrtSetUpHierarchyMsg) {
			this.accCrtSetUpHierarchyMsg = accCrtSetUpHierarchyMsg;
		}
		public String getAccCrtValidateUSGovtMsg() {
			return accCrtValidateUSGovtMsg;
		}
		public void setAccCrtValidateUSGovtMsg(String accCrtValidateUSGovtMsg) {
			this.accCrtValidateUSGovtMsg = accCrtValidateUSGovtMsg;
		}
		public String getAccCrtValidateSIAccMsg() {
			return accCrtValidateSIAccMsg;
		}
		public void setAccCrtValidateSIAccMsg(String accCrtValidateSIAccMsg) {
			this.accCrtValidateSIAccMsg = accCrtValidateSIAccMsg;
		}
		public String getAccCrtValidateSPAccMsg() {
			return accCrtValidateSPAccMsg;
		}
		public void setAccCrtValidateSPAccMsg(String accCrtValidateSPAccMsg) {
			this.accCrtValidateSPAccMsg = accCrtValidateSPAccMsg;
		}
		public String getAccCrtEmailNotiErrMsg() {
			return accCrtEmailNotiErrMsg;
		}
		public void setAccCrtEmailNotiErrMsg(String accCrtEmailNotiErrMsg) {
			this.accCrtEmailNotiErrMsg = accCrtEmailNotiErrMsg;
		}
		public String getAccCrtInvalidSoldToErrMsg() {
			return accCrtInvalidSoldToErrMsg;
		}
		public void setAccCrtInvalidSoldToErrMsg(String accCrtInvalidSoldToErrMsg) {
			this.accCrtInvalidSoldToErrMsg = accCrtInvalidSoldToErrMsg;
		}
		public String getAccCrtSoldToNotExistsErrMsg() {
			return accCrtSoldToNotExistsErrMsg;
		}
		public void setAccCrtSoldToNotExistsErrMsg(String accCrtSoldToNotExistsErrMsg) {
			this.accCrtSoldToNotExistsErrMsg = accCrtSoldToNotExistsErrMsg;
		}
		public String getAccCrtInvalidSoldToNonUSErrMsg() {
			return accCrtInvalidSoldToNonUSErrMsg;
		}
		public void setAccCrtInvalidSoldToNonUSErrMsg(
				String accCrtInvalidSoldToNonUSErrMsg) {
			this.accCrtInvalidSoldToNonUSErrMsg = accCrtInvalidSoldToNonUSErrMsg;
		}
		public String getAccCrtSoldToValidatedMsg() {
			return accCrtSoldToValidatedMsg;
		}
		public void setAccCrtSoldToValidatedMsg(String accCrtSoldToValidatedMsg) {
			this.accCrtSoldToValidatedMsg = accCrtSoldToValidatedMsg;
		}
		public String getTobSslVpnNotAllowedErrMsg() {
			return tobSslVpnNotAllowedErrMsg;
		}
		public void setTobSslVpnNotAllowedErrMsg(String tobSslVpnNotAllowedErrMsg) {
			this.tobSslVpnNotAllowedErrMsg = tobSslVpnNotAllowedErrMsg;
		}
		public String getTobExceptionInFindinfSRErrMsg() {
			return tobExceptionInFindinfSRErrMsg;
		}
		public void setTobExceptionInFindinfSRErrMsg(
				String tobExceptionInFindinfSRErrMsg) {
			this.tobExceptionInFindinfSRErrMsg = tobExceptionInFindinfSRErrMsg;
		}
		public String getTobRegSetpBFoundForSeidErrMsg() {
			return tobRegSetpBFoundForSeidErrMsg;
		}
		public void setTobRegSetpBFoundForSeidErrMsg(
				String tobRegSetpBFoundForSeidErrMsg) {
			this.tobRegSetpBFoundForSeidErrMsg = tobRegSetpBFoundForSeidErrMsg;
		}
		public String getTobExceptionWhileFindingAlarmErrMsg() {
			return tobExceptionWhileFindingAlarmErrMsg;
		}
		public void setTobExceptionWhileFindingAlarmErrMsg(
				String tobExceptionWhileFindingAlarmErrMsg) {
			this.tobExceptionWhileFindingAlarmErrMsg = tobExceptionWhileFindingAlarmErrMsg;
		}
		public String getTobSubmitRegPrdtErrMsg() {
			return tobSubmitRegPrdtErrMsg;
		}
		public void setTobSubmitRegPrdtErrMsg(String tobSubmitRegPrdtErrMsg) {
			this.tobSubmitRegPrdtErrMsg = tobSubmitRegPrdtErrMsg;
		}
		public String getTobSubmitRedayToProcesstErrMsg() {
			return tobSubmitRedayToProcesstErrMsg;
		}
		public void setTobSubmitRedayToProcesstErrMsg(
				String tobSubmitRedayToProcesstErrMsg) {
			this.tobSubmitRedayToProcesstErrMsg = tobSubmitRedayToProcesstErrMsg;
		}
		public String getTobGrtProblemErrMsg() {
			return tobGrtProblemErrMsg;
		}
		public void setTobGrtProblemErrMsg(String tobGrtProblemErrMsg) {
			this.tobGrtProblemErrMsg = tobGrtProblemErrMsg;
		}
		public String getTobRetrieveErrMsg() {
			return tobRetrieveErrMsg;
		}
		public void setTobRetrieveErrMsg(String tobRetrieveErrMsg) {
			this.tobRetrieveErrMsg = tobRetrieveErrMsg;
		}
		public String getTobErrWhileProcessingErrMsg() {
			return tobErrWhileProcessingErrMsg;
		}
		public void setTobErrWhileProcessingErrMsg(
				String tobErrWhileProcessingErrMsg) {
			this.tobErrWhileProcessingErrMsg = tobErrWhileProcessingErrMsg;
		}
		public String getRvNotAllowdedNortelMaestroErrMsg() {
			return rvNotAllowdedNortelMaestroErrMsg;
		}
		public void setRvNotAllowdedNortelMaestroErrMsg(
				String rvNotAllowdedNortelMaestroErrMsg) {
			this.rvNotAllowdedNortelMaestroErrMsg = rvNotAllowdedNortelMaestroErrMsg;
		}
		
		private String ibFailedInProcessingErrMsg;
        
        public String getIbFailedInProcessingErrMsg() {
               return ibFailedInProcessingErrMsg;
        }
        public void setIbFailedInProcessingErrMsg(
                     String ibFailedInProcessingErrMsg) {
               this.ibFailedInProcessingErrMsg = ibFailedInProcessingErrMsg;
        }
		public String getSalgwInstDownMsg() {
			return salgwInstDownMsg;
		}
		public void setSalgwInstDownMsg(String salgwInstDownMsg) {
			this.salgwInstDownMsg = salgwInstDownMsg;
		}
		public String getLssOrESSLowerVersionNote() {
			return lssOrESSLowerVersionNote;
		}
		public void setLssOrESSLowerVersionNote(String lssOrESSLowerVersionNote) {
			this.lssOrESSLowerVersionNote = lssOrESSLowerVersionNote;
		}
		public String getUpgradedMainCMuseRFASIDNote() {
			return upgradedMainCMuseRFASIDNote;
		}
		public void setUpgradedMainCMuseRFASIDNote(String upgradedMainCMuseRFASIDNote) {
			this.upgradedMainCMuseRFASIDNote = upgradedMainCMuseRFASIDNote;
		}
		public String getNoConnectityReTestMsg() {
			return noConnectityReTestMsg;
		}
		public void setNoConnectityReTestMsg(String noConnectityReTestMsg) {
			this.noConnectityReTestMsg = noConnectityReTestMsg;
		}
		
		private String alertSpecialCharErrorMsg;
		
		public String getAlertSpecialCharErrorMsg() {
			return alertSpecialCharErrorMsg;
		}
		public void setAlertSpecialCharErrorMsg(String alertSpecialCharErrorMsg) {
			this.alertSpecialCharErrorMsg = alertSpecialCharErrorMsg;
		}
		public String getRvNortelMaestroWithContractErrMsg() {
			return rvNortelMaestroWithContractErrMsg;
		}
		public void setRvNortelMaestroWithContractErrMsg(
				String rvNortelMaestroWithContractErrMsg) {
			this.rvNortelMaestroWithContractErrMsg = rvNortelMaestroWithContractErrMsg;
		}
		public String getNoRowsFoundForUpdateErrMsg() {
			return noRowsFoundForUpdateErrMsg;
		}
		public void setNoRowsFoundForUpdateErrMsg(
				String noRowsFoundForUpdateErrMsg) {
			this.noRowsFoundForUpdateErrMsg = noRowsFoundForUpdateErrMsg;
		}
		public String getNofileChosenForUploadErrMsg() {
			return nofileChosenForUploadErrMsg;
		}
		public void setNofileChosenForUploadErrMsg(
				String nofileChosenForUploadErrMsg) {
			this.nofileChosenForUploadErrMsg = nofileChosenForUploadErrMsg;
		}
		public String getRevisedQutyEqlExistQtyErrMsg() {
			return revisedQutyEqlExistQtyErrMsg;
		}
		public void setRevisedQutyEqlExistQtyErrMsg(
				String revisedQutyEqlExistQtyErrMsg) {
			this.revisedQutyEqlExistQtyErrMsg = revisedQutyEqlExistQtyErrMsg;
		}
		
		private String tokenBPNotAuthorisedToRedeemErrMsg;
		private String tokenContractNotReplacableErrMsg;
		private String tokenExcpFindingContractsErrMsg;
		private String tokenNoContractsFoundErrMsg;
		private String tokenContractReplaceNoConfirmMsg;
		private String tokenAccountNotEligibleErrMsg;
		private String tokenIpOfficeVersionErrMsg;
		private String tokenNoIPOEquipmentErrMsg;
		
		public String getTokenBPNotAuthorisedToRedeemErrMsg() {
			return tokenBPNotAuthorisedToRedeemErrMsg;
		}
		public void setTokenBPNotAuthorisedToRedeemErrMsg(String tokenBPNotAuthorisedToRedeemErrMsg) {
			this.tokenBPNotAuthorisedToRedeemErrMsg = tokenBPNotAuthorisedToRedeemErrMsg;
		}
		public String getTokenContractNotReplacableErrMsg() {
			return tokenContractNotReplacableErrMsg;
		}
		public void setTokenContractNotReplacableErrMsg(String tokenContractNotReplacableErrMsg) {
			this.tokenContractNotReplacableErrMsg = tokenContractNotReplacableErrMsg;
		}
		public String getTokenExcpFindingContractsErrMsg() {
			return tokenExcpFindingContractsErrMsg;
		}
		public void setTokenExcpFindingContractsErrMsg(String tokenExcpFindingContractsErrMsg) {
			this.tokenExcpFindingContractsErrMsg = tokenExcpFindingContractsErrMsg;
		}
		public String getTokenNoContractsFoundErrMsg() {
			return tokenNoContractsFoundErrMsg;
		}
		public void setTokenNoContractsFoundErrMsg(String tokenNoContractsFoundErrMsg) {
			this.tokenNoContractsFoundErrMsg = tokenNoContractsFoundErrMsg;
		}
		public String getTokenContractReplaceNoConfirmMsg() {
			return tokenContractReplaceNoConfirmMsg;
		}
		public void setTokenContractReplaceNoConfirmMsg(String tokenContractReplaceNoConfirmMsg) {
			this.tokenContractReplaceNoConfirmMsg = tokenContractReplaceNoConfirmMsg;
		}
		public String getTokenAccountNotEligibleErrMsg() {
			return tokenAccountNotEligibleErrMsg;
		}
		public void setTokenAccountNotEligibleErrMsg(String tokenAccountNotEligibleErrMsg) {
			this.tokenAccountNotEligibleErrMsg = tokenAccountNotEligibleErrMsg;
		}
		public String getTokenIpOfficeVersionErrMsg() {
			return tokenIpOfficeVersionErrMsg;
		}
		public void setTokenIpOfficeVersionErrMsg(String tokenIpOfficeVersionErrMsg) {
			this.tokenIpOfficeVersionErrMsg = tokenIpOfficeVersionErrMsg;
		}
		public String getTokenNoIPOEquipmentErrMsg() {
			return tokenNoIPOEquipmentErrMsg;
		}
		public void setTokenNoIPOEquipmentErrMsg(String tokenNoIPOEquipmentErrMsg) {
			this.tokenNoIPOEquipmentErrMsg = tokenNoIPOEquipmentErrMsg;
		}
}
