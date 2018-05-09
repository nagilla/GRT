package com.avaya.grt.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.util.GRTConstants;
import com.grt.util.GRTUtil;
import com.opensymphony.xwork2.Action;

public class TechRegCommonJsonAction extends TechRegCommonAction{

	private Map<String, Object> siteContactMap;
	@Override
	public String newRegistrationWithSoldToValidation() throws Exception {
		siteContactMap = new HashMap<String, Object>();
		//Update the Action Form Object From Reg Object
		actionForm.setSoldToId(regObj.getSoldToId());
		actionForm.setInstallbaseRegistrationOnly(regObj.getInstallbaseRegistrationOnly());
		
		//Call the existing service
		super.newRegistrationWithSoldToValidation();
		if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
			//Create the Map only with required values from actionForm object
			getJsonForContactPage();	
		}else{
			siteContactMap = GRTUtil.setErrorMsgInMap(actionForm.getSoldToError());
		}
			
		return Action.SUCCESS;
	}
	
	@Override
	public String newRegistrationWithSoldToValidationForAgent() throws Exception {
		siteContactMap = new HashMap<String, Object>();
		//Update the Action Form Object From Reg Object
		actionForm.setSoldToId(regObj.getSoldToId());
		actionForm.setInstallbaseRegistrationOnly(regObj.getInstallbaseRegistrationOnly());
		actionForm.setUserRole(regObj.getUserRole());
				
		//Call the existing service
		super.newRegistrationWithSoldToValidationForAgent();
		if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){		
			//Create the Map only with required values from actionForm object
			getJsonForContactPage();
		}else{
			siteContactMap = GRTUtil.setErrorMsgInMap(actionForm.getSoldToError());
		}
		return Action.SUCCESS;
	}
	
	private void getJsonForContactPage()
	{
		siteContactMap = new HashMap<String, Object>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put(GRTConstants.REGISTRATIONID, actionForm.getRegistrationId());
		tempMap.put(GRTConstants.SOLDTOID, actionForm.getSoldToId());
		tempMap.put(GRTConstants.COMPANY, actionForm.getCompany());
		tempMap.put(GRTConstants.COMPANYPHONE, actionForm.getCompanyPhone());
		tempMap.put(GRTConstants.SITECOUNTRY, actionForm.getSiteCountry());
		tempMap.put(GRTConstants.ADDRESS1, actionForm.getAddress1());
		tempMap.put(GRTConstants.ADDRESS2, actionForm.getAddress2());
		tempMap.put(GRTConstants.CITY1, actionForm.getCity());
		tempMap.put(GRTConstants.ZIP1, actionForm.getZip());
		tempMap.put(GRTConstants.REGISTRATIONIDENTIFIER, actionForm.getRegistrationIdentifier());
		
		tempMap.put(GRTConstants.FIRSTNAME, actionForm.getFirstName());
		tempMap.put(GRTConstants.LASTNAME, actionForm.getLastName());
		tempMap.put(GRTConstants.REPORTEDPHONE, actionForm.getReportedPhone());
		tempMap.put(GRTConstants.REPORTEDEMAIL, actionForm.getReportedEmail());
		
		tempMap.put(GRTConstants.ONSITEFIRSTNAME, actionForm.getOnsiteFirstname());
		tempMap.put(GRTConstants.ONSITELASTNAME, actionForm.getOnsiteLastname());
		tempMap.put(GRTConstants.ONSITEPHONE, actionForm.getOnsitePhone());
		tempMap.put(GRTConstants.ONSITEEMAIL, actionForm.getOnsiteEmail());
		tempMap.put(GRTConstants.REGISTRATIONNOTES, actionForm.getRegistrationNotes());
		tempMap.put(GRTConstants.SOLDTOERROR, actionForm.getSoldToError());
		
		siteContactMap.put(GRTConstants.JSON_WRAPPER, tempMap);
	}

	/*
	 * Method for logout functionality
	 */
	public String logout(){
		
		try {
			super.logout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Action.SUCCESS;
	}
	
	/*
	 * Method for Back button on Site Contact Validation Page
	 */
	public String locationFinder(){
		String response = "";
		try {
			response = super.locationFinder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

	/*
	 * Method for getting sold to list for autocomplete
	 */
	public String getCxpSoldToList(){
		
		super.getCxpSoldToList();
		
		return Action.SUCCESS;
	}

	/** START :: Registration List  **/
	/*
	 * Update the email field in registration list page
	 */
	public String updateEmail(){
		
		super.updateEmail();
		
		return Action.SUCCESS;
	}
	
	/*
	 * Method for initializing the registration list page
	 */
	public String registrationList(){
		
		super.registrationList();
		
		return Action.SUCCESS;
	}
	
	/*
	 * Method for initializing the registration list page
	 */
	public String registrationListNew(){
		siteContactMap = new HashMap<String, Object>();
		
		super.registrationList();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		
		//Create the Map only with required values from actionForm object
		if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
			ArrayList<Object> list = new ArrayList<Object>();
			list.addAll(registrationSummaries);
			tempMap.put("statusList", statusList);
			tempMap.put("statusList", regTypeList);
			list.add( tempMap );
			siteContactMap.put(GRTConstants.JSON_WRAPPER, list);
		}else{
			siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
		}
		
		return Action.SUCCESS;
	}
	
	/*
	 * Method for getting data for lazy loading
	 */
	public String registrationListData(){
		
		super.registrationListData();
		
		return Action.SUCCESS;
	}
	
	/*
	 * Method to email the onboarding file for a given registration id
	 */
	public String emailOnBoardingFile(){
		
		super.emailOnBoardingFile();
		
		return Action.SUCCESS;
	}
	
	/**
	 * 
	 * **/
	@Override
	public void getSrSummaryInfo()
	{
		siteContactMap = new HashMap<String, Object>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			actionForm.setSrNumber(regObj.getSrNumber());
			
			super.getSrSummaryInfo();
			if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
				
			tempMap.put(GRTConstants.SRSUMMARYINFO, actionForm.getSrSummaryInfoList());
			siteContactMap.put(GRTConstants.JSON_WRAPPER, tempMap);
			
			}else{
				siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
			}

		} catch (Exception eX) {
			siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
			eX.printStackTrace();
		}
		//return Action.SUCCESS;
	}
	
	/**
	 * 
	 * 
	 * **/
	@Override
	public String saveSrSummaryInfo()
	{
		siteContactMap = new HashMap<String, Object>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try{
		
		actionForm.setSrNumber(regObj.getSrNumber());
		actionForm.setSrNote(regObj.getSrNote());
		actionForm.setAccountLocation(regObj.getAccountLocation());
		
		super.saveSrSummaryInfo();
		
		if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
			
			tempMap.put(GRTConstants.SUCCESS, "SUCCESS");
			siteContactMap.put(GRTConstants.JSON_WRAPPER, tempMap);
		}
		else{
			siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
		}
		}catch(Exception eX){
			
			siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
			
			eX.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	/**
	 * *
	 * 
	 * ***/
	public String displayRegistration()
	{
		try{
			actionForm.setRegistrationId(regObj.getRegistrationId());
			actionForm.setTechnicalRegistrationOnly(regObj.isTechnicalRegistrationOnly());
			actionForm.setStatus(regObj.getStatus());
			actionForm.setSoldToId(regObj.getSoldToId());
			actionForm.setCompany(regObj.getCompany());
			super.displayRegistration();
			if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
					siteContactMap = new HashMap<String, Object>();
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put(GRTConstants.ERRORMESSAGE, actionForm.getErrorMessage());
					
					tempMap.put(GRTConstants.MESSAGE,actionForm.getMessage());
					tempMap.put(GRTConstants.ACTVESRCOLUMNONLY,actionForm.getActveSRColumnOnly());
					tempMap.put(GRTConstants.COMPANY, actionForm.getCompany());
					tempMap.put(GRTConstants.REG_SUMMARY_LIST_REGISTRABLE,actionForm.getRegistrationSummaryListRegistrable());
					tempMap.put(GRTConstants.READ_ONLY,actionForm.isReadOnly());
					tempMap.put(GRTConstants.REG_SUMMARY_LIST,actionForm.getRegistrationSummaryList());
					tempMap.put(GRTConstants.REGISTRATIONSUMMARYLISTTRUPDATE,actionForm.getRegistrationSummaryListTRUpdate());
					
					tempMap.put(GRTConstants.SAVESITEREG,actionForm.getSaveSiteReg());
					tempMap.put(GRTConstants.MATERIALENTRYLIST,actionForm.getMaterialEntryList());
					tempMap.put(GRTConstants.IPOINVENTORYNOTLOADED,actionForm.getIpoInventoryNotLoaded());
					
					siteContactMap.put(GRTConstants.JSON_WRAPPER, tempMap);
			}else{
				siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
			}
		}catch(Exception ex){
			siteContactMap = GRTUtil.setErrorMsgInMap(ex.getMessage());
			ex.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String updateSiteRegistrationBySuperUser()
	{
		siteContactMap = new HashMap<String, Object>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		
		try{
		actionForm.setRegistrationId(regObj.getRegistrationId());
		
		super.updateSiteRegistrationBySuperUser();
		if( getErrorMessage() == null || getErrorMessage().trim().length() == 0 ){
			
			tempMap.put(GRTConstants.SUCCESS, "SUCCESS");
			siteContactMap.put(GRTConstants.JSON_WRAPPER, tempMap);
			
		}else{
			siteContactMap = GRTUtil.setErrorMsgInMap(getErrorMessage());
		}
		}catch(Exception ex){
			siteContactMap = GRTUtil.setErrorMsgInMap(ex.getMessage());
			ex.printStackTrace();
		}
		return Action.SUCCESS;
		
	}
	
	/** END :: Registration List  **/ 
	
	//Getters And Setters
	public Map<String, Object> getSiteContactMap() {
		return siteContactMap;
	}

	public void setSiteContactMap(Map<String, Object> siteContactMap) {
		this.siteContactMap = siteContactMap;
	}
}
