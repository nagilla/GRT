package com.avaya.grt.web.action.accountcreation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.AccountCreation;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.FuzzySearchParam;
import com.avaya.grt.mappers.LogAccountUpdate;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.avaya.grt.web.util.PageCache;
import com.avaya.v1.accountupdate.AccountUpdateResponse;
import com.avaya.v1.address.AddressType;
import com.avaya.v1.addresssearch.AddressSearchRequestType;
import com.avaya.v1.addresssearch.Request;
import com.avaya.v1.shiptoschemas.ShipToCreateRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateResponseType;
import com.avaya.v1.soldtoschemas.CustomerGroupType;
import com.avaya.v1.soldtoschemas.SoldToCreateRequestType;
import com.avaya.v1.soldtoschemas.SoldToCreateResponseType;
import com.grt.dto.Account;
import com.grt.dto.BusinessPartner;
import com.grt.dto.Contact;
import com.grt.dto.Result;
import com.grt.dto.TokenResponseTypeDto;
import com.grt.util.GRTConstants;
import com.grt.util.GRTUtil;
import com.opensymphony.xwork2.Action;
public class AccountCreationAction  extends TechnicalRegistrationAction {
	
	private static final Logger logger = Logger.getLogger(AccountCreationAction.class);
	
	//For BFOWARNING
	private static final int SOLDTOID_MAX_LENGTH=50;
	/**
	 * Method to populate country state lists.
	 * 
	 * @throws Throwable
	 */
	public void begin() throws Throwable {
		
		SortedMap<String, String>  provinceCode = getInstallBaseService().getProvinceCode();
		PageCache.stateSelectMap = getInstallBaseService().getStateCode();
		PageCache.countrySelectMap = getInstallBaseService().getCountryCode();
		PageCache.countryMap = getInstallBaseService().getCountryValues();
		PageCache.provinceSelectMap = provinceCode;
        PageCache.regionMap = getInstallBaseService().getRegionMap();
	}
	
	/**
	 * account update page load
	 * 
	 * @return
	 * @throws Exception
	 */
	public String accountUpdatePageLoad() throws Exception {
		validateSession();
		logger.debug("In account update in AccountCreationAction");
		try {
			begin();
			CSSPortalUser userProfile = getUserFromSession();
             if ("B".equals(getUserFromSession().getUserType())) {
                 actionForm.getAccountFormBean().setAcountCreationFlag("none");
                 actionForm.getAccountFormBean().setShipToCreationFlagForBP("block");
                 actionForm.getAccountFormBean().setShipToCreationFlagForNonBP("none");
                 actionForm.getAccountFormBean().setTokenNumberFlag("none");
                 actionForm.getAccountFormBean().setAccountAddressFlag("none");
                 actionForm.getAccountFormBean().setFuzzySearchFlag("none");
                 actionForm.getAccountFormBean().setSearchDataFlag("none");
                 actionForm.getAccountFormBean().setSearchFlag("none");
                 actionForm.getAccountFormBean().setShipTosoldToFlag("none");
                 actionForm.getAccountFormBean().setConfirmationFlag("none");
                 actionForm.getAccountFormBean().setBplinkId(userProfile.getBpLinkId());
                 logger.debug(" username: "+userProfile.getFirstName()+", "+userProfile.getLastName());
                 actionForm.getAccountFormBean().setBpName(userProfile.getFirstName()+" "+userProfile.getLastName());
             } else {
                 actionForm.getAccountFormBean().setAcountCreationFlag("none");
                 actionForm.getAccountFormBean().setTokenNumberFlag("none");
                 actionForm.getAccountFormBean().setAccountAddressFlag("none");
                 actionForm.getAccountFormBean().setFuzzySearchFlag("none");
                 actionForm.getAccountFormBean().setShipTosoldToFlag("none");
                 actionForm.getAccountFormBean().setConfirmationFlag("none");
                 actionForm.getAccountFormBean().setSearchDataFlag("none");
                 actionForm.getAccountFormBean().setSearchFlag("none");
                 actionForm.getAccountFormBean().setSearchFlag("none");
                 actionForm.getAccountFormBean().setShipToCreationFlagForBP("none");
                 actionForm.getAccountFormBean().setShipToCreationFlagForNonBP("block");
             }
		logger.debug("registrationDelegate.accountUpdate()");
			
		} catch(Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting accountUpdate");
		}
		return Action.SUCCESS;
	}
	
	
	/**
	 * validate solto for account update
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validateSoldtoIdForUpdate() throws Exception {
        validateSession();	
        String soldtoId = actionForm.getAccountFormBean().getSoldToId();
        logger.debug("Sold TO ID ########## " + soldtoId);
        CSSPortalUser userProfile = getUserFromSession();
        Account account = null;
        try  {
            account = getAccountService().queryAccountWithContacts(soldtoId);
            if (account == null) {
               this.getRequest().setAttribute("soldToError", grtConfig.getEwiMessageCodeMap().get("soldToNotValidMsg") + "###" + grtConfig.getSoldToNotValidMsg());
                return showAccountUpdate();
            }
            if (GRTConstants.USER_TYPE_BP.equalsIgnoreCase(getUserFromSession().getUserType())) {
                if (!this.getInstallBaseService().isSoldToValidForCurrentUser(soldtoId, userProfile.getUserId(), userProfile.getBpLinkId())) {
                    this.getRequest().setAttribute("soldToError", grtConfig.getEwiMessageCodeMap().get("bpNotAuthorizedForSoldToErrMsg") + "###" + grtConfig.getBpNotAuthorizedForSoldToErrMsg());
                    return showAccountUpdate();
                } 
            } 
        } catch (Exception exception) {
            logger.error("", exception);
            this.getRequest().setAttribute("soldToError", grtConfig.getEwiMessageCodeMap().get("validateSoldToErrMsg") + "###" + grtConfig.getValidateSoldToErrMsg());
            return showAccountUpdate();
        }
        actionForm.getAccountFormBean().setOriginalAccount(account);
        actionForm.getAccountFormBean().setUpdatedAccount(account.clone());
        return populateAccountForUpdate();
    }
	
	
	/**
	 * show account update page, set paramenters
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showAccountUpdate() throws Exception  {
     		validateSession();
         actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
         actionForm.getAccountFormBean().setSearchFlag(GRTConstants.NONE);
         actionForm.getAccountFormBean().setContactsFlag(GRTConstants.NONE);
         logger.debug("In account update in AccountCreationAction");
             CSSPortalUser userProfile = getUserFromSession();
             if (GRTConstants.BBoxDestination.equals(getUserFromSession().getUserType())) {
                 actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
                 actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setSearchFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
                 logger.debug(" username: "+userProfile.getFirstName()+", "+userProfile.getLastName());
                 actionForm.getAccountFormBean().setBpName(userProfile.getFirstName()+" "+userProfile.getLastName());
             } else {
                 actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setSearchFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setSearchFlag(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
                 actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.BLOCK);
             }
         return Action.SUCCESS;
	 }
	 
	
	/**
	 * Populate account for update
	 * 
	 * @return
	 * @throws Exception
	 */
	public String populateAccountForUpdate() throws Exception  {
		 logger.debug("entering populateAccountForUpdate");
		 validateSession();
		 setAllFlagNone();
		 List<Contact> contacts = actionForm.getAccountFormBean().getOriginalAccount().getContacts();
		 if (contacts != null && contacts.size() > 0 )
		 {
			 Contact contact = contacts.get(0);
			 if (StringUtils.isEmpty(actionForm.getAccountFormBean().getOriginalAccount().getEmailId()))
			 {
				 actionForm.getAccountFormBean().getOriginalAccount().setEmailId(contact.getEmail());
			 }	
			 if (StringUtils.isEmpty(actionForm.getAccountFormBean().getOriginalAccount().getPhoneNumber()))
			 {
				 Account account = actionForm.getAccountFormBean().getOriginalAccount();
				 account.setPhoneNumber(contact.getPhone());
				 
				 if (account.getPhoneNumber()!=null && ( account.getPrimaryAccountCountry().equalsIgnoreCase(GRTConstants.COUNTRY_US) 
						 || account.getPrimaryAccountCountry().equalsIgnoreCase(GRTConstants.COUNTRY_USA)))
				 {
					 account.setPhoneNumber(account.getPhoneNumber().replaceAll("[^0-9]", ""));
				 }
			 }
		 }
	     String twocharcountry = getTwoCharISOCode(actionForm.getAccountFormBean().getOriginalAccount().getPrimaryAccountCountry());
	     actionForm.getAccountFormBean().setCurrentCountryRegion(null);
	     actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
	     Map <Long,Region> countryRegions = actionForm.getAccountFormBean().getRegionMap().get(twocharcountry);
	     if (countryRegions !=null) 
	     {
	         actionForm.getAccountFormBean().setCurrentCountryRegion(countryRegions);
	     }
	     actionForm.getAccountFormBean().setRegionValue(null);
	     actionForm.getAccountFormBean().setUpdatedAccount(actionForm.getAccountFormBean().getOriginalAccount().clone());
	     actionForm.getAccountFormBean().getUpdatedAccount().setContactName(actionForm.getAccountFormBean().getOriginalAccount().getContactNameForDisplay());
	     if (countryRegions != null){
	         Account account = actionForm.getAccountFormBean().getOriginalAccount();
	         for (Map.Entry<Long, Region> region : countryRegions.entrySet())
	         {
	             String siebelValue = region.getValue().getSiebeldescription();
	             String sapValue = region.getValue().getDescription();
	             String id = region.getValue().getRegion();
	             logger.debug(siebelValue);
	             logger.debug(id);
	               
	             if (siebelValue != null && siebelValue.equalsIgnoreCase(account.getPrimaryAccountState()))
	             {
	                 actionForm.getAccountFormBean().setSelectedRegionId(region.getValue().getRegionId());
	                 actionForm.getAccountFormBean().getUpdatedAccount().setPrimaryAccountState(account.getPrimaryAccountState()+"##"+sapValue);
	             }
	                
	         }
	     }  
	     actionForm.getAccountFormBean().setContactsFlag(GRTConstants.BLOCK);
	     return Action.SUCCESS;
	 }
	
	 /**
	 * set all flags to none
	 */
	private void setAllFlagNone() {
			actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setSearchFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setContactsFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
		}
	
	 /**
	 * Generate two char iso code
	 * 
	 * @param countryName
	 * @return
	 */
	private String getTwoCharISOCode(String countryName){
         String country2Char = null;
         if (checkNonEmpty(countryName))
         {
             List<Country> countryList = PageCache.countryMap;
             logger.debug("countryName : "
                 + countryName);
             for (Country country : countryList)
             {
                 if (country.getName().trim().equalsIgnoreCase(countryName.trim()))
                 {
                     country2Char = country.getCode();
                     logger.debug("Country Name "
                         + countryName + " country2Char : " + country2Char);
                     return country2Char;
                 }
             }
         }
         return country2Char;
     }
	 
	 /**
	 * Check Non empty string
	 * 
	 * @param string
	 * @return boolean
	 */
	private boolean checkNonEmpty(String string)
     {
         if ((string != null)
             && (string.trim().length() > 0))
         {
             return true;
         }
         return false;
     }
	
	 
	 /**
	 * to update account
	 * 
	 * @return success flag
	 */
	public String updateAccount() {
	     try
	     {
	     	validateSession();
	     	String state = actionForm.getAccountFormBean().getUpdatedAccount().getPrimaryAccountState();
	         if (StringUtils.isNotEmpty(state))
	         {
	             actionForm.getAccountFormBean().getUpdatedAccount().setPrimaryAccountState(state.split("##")[0]);
	         }
	         String summury = actionForm.getAccountFormBean().getOriginalAccount().getModificationSummary(
	             actionForm.getAccountFormBean().getUpdatedAccount());
	         if (StringUtils.isEmpty(summury))
	         {
	             setAllFlagNone();
	             actionForm.getAccountFormBean().setContactsFlag(GRTConstants.BLOCK);
	             getRequest().setAttribute("updateResponse", grtConfig.getEwiMessageCodeMap().get("noValForUpdateErrMsg") + "###" + grtConfig.getNoValForUpdateErrMsg());
	         }
	         else
	         {
	             actionForm.getAccountFormBean().getUpdatedAccount().setPrimaryAccountState(state);
	             Account originalAccount = actionForm.getAccountFormBean().getOriginalAccount();
	             logger.debug("Country Code 2 digit = "
	                 + originalAccount.getCountryCode());
	             
	             String threeDigitCode = getThreeCharISOCode(originalAccount.getCountryCode());
	             if (StringUtils.isNotEmpty(threeDigitCode)){
	             originalAccount
	                 .setCountryCode(threeDigitCode);
	             }
	             logger.debug("Country Code 3 digit = "
	                 + originalAccount.getCountryCode());
	             AccountUpdateResponse response = getAccountService().createSSRForAccountUpdate(
	                 actionForm.getAccountFormBean().getOriginalAccount(), actionForm.getAccountFormBean().getUpdatedAccount());
	             if(response != null){
		                try{
		                	getAccountService().saveAccountUpdateLog(contructAccountUpdateObject(response));
		            	}catch (Throwable throwable)
		                {
		                    logger.error("Exception Occured " + throwable.getMessage(), throwable);
		                }
	             }
	
	             if (StringUtils.isNotEmpty(response.getStatus())
	                 && response.getStatus().equals("Success")
	                 && StringUtils.isNotEmpty(response.getSSRNumber()))
	             {
	            	 
	            	 getRequest()
	                     .setAttribute(
	                         "updateResponse",	                         
	                         grtConfig.getEwiMessageCodeMap().get("accUpdateSuccessMsgPart1") + "###" + grtConfig.getAccUpdateSuccessMsgPart1() 
	                             + " " + response.getSSRNumber() + " " 
	                             + grtConfig.getAccUpdateSuccessMsgPart2());
	                 getRequest().setAttribute("accountUpdated", true);
	             }
	             else
	             {
	                 if (response.getErrorMsg() != null
	                     && response.getErrorMsg().length > 0)
	                	 getRequest().setAttribute("updateResponse",
	                			 grtConfig.getEwiMessageCodeMap().get("accUpdateErrMsg") + "###" + grtConfig.getAccUpdateErrMsg()
		                             + response.getErrorMsg()[0] + "]");
	             }
	
	             setAllFlagNone();
	             actionForm.getAccountFormBean().setContactsFlag(GRTConstants.BLOCK);
	         }
	     }
	     catch (Exception exception) {
	         logger.debug("Exception "
	             + exception);
	         getRequest().setAttribute("updateerror", grtConfig.getEwiMessageCodeMap().get("accUpdateErrMsg2") + "###" + grtConfig.getAccUpdateErrMsg2());
	     }
	     return Action.SUCCESS;					
     }
	 
	 
	 /**
	 * Construct account update object
	 * 
	 * @param response
	 * @return
	 */
	private LogAccountUpdate contructAccountUpdateObject(AccountUpdateResponse response){
    	 LogAccountUpdate logAccountUpdate = new LogAccountUpdate();
    	 CSSPortalUser userProfile = this.getUserFromSession();
    	 logAccountUpdate.setTransactionDate(new Date());
    	 logAccountUpdate.setTransactedBy(GRTUtil.trimPropertytoMaxLength(userProfile.getLastName()+", "+userProfile.getFirstName(), 255));
    	 logAccountUpdate.setLoggedInBPLinkId(actionForm.getAccountFormBean().getBplinkId()!=null?actionForm.getAccountFormBean().getBplinkId():userProfile.getBpLinkId());
    	 Account original = actionForm.getAccountFormBean().getOriginalAccount();
    	 if(original != null){
    		 logAccountUpdate.setAccountId(original.getSoldToNumber());
        	 logAccountUpdate.setOldAccountName(GRTUtil.trimPropertytoMaxLength(original.getName(), 255));
        	 logAccountUpdate.setOldCustomerContactName(GRTUtil.trimPropertytoMaxLength(original.getContactNameForDisplay(), 255));
        	 logAccountUpdate.setOldCity(GRTUtil.trimPropertytoMaxLength(original.getPrimaryAccountCity(), 255));
        	 if(original.getPrimaryAccountState() != null){
        		 logAccountUpdate.setOldStateProvince(GRTUtil.trimPropertytoMaxLength(
							original.getPrimaryAccountState().split("##").length > 1 ? original.getPrimaryAccountState().split("##")[1]: original.getPrimaryAccountState(), 255));
        	 }
        	 logAccountUpdate.setOldCountryISOCode(original.getCountryCode());
        	 logAccountUpdate.setOldStreetAddressLine1(GRTUtil.trimPropertytoMaxLength(original.getPrimaryAccountStreetAddress(), 255));
        	 logAccountUpdate.setOldStreetAddressLine2(GRTUtil.trimPropertytoMaxLength(original.getPrimaryAccountStreetAddress2(), 255));
        	 logAccountUpdate.setOldMainPhoneNumber(GRTUtil.trimPropertytoMaxLength(original.getPhoneNumber(), 255));
        	 logAccountUpdate.setOldPostalCode(original.getPrimaryAccountPostalCode());
    	 }
    	 Account modified = actionForm.getAccountFormBean().getUpdatedAccount();
    	 if(modified != null){
        	 logAccountUpdate.setNewAccountName(GRTUtil.trimPropertytoMaxLength(modified.getName(), 255));
        	 logAccountUpdate.setNewCustomerContactName(GRTUtil.trimPropertytoMaxLength(modified.getContactName(), 255));
        	 logAccountUpdate.setNewCity(GRTUtil.trimPropertytoMaxLength(modified.getPrimaryAccountCity(), 255));
        	 if(modified.getPrimaryAccountState() != null){
        		 logAccountUpdate.setNewStateProvince(GRTUtil.trimPropertytoMaxLength(
        				 modified.getPrimaryAccountState().split("##").length>1?modified.getPrimaryAccountState().split("##")[1]:modified.getPrimaryAccountState(), 255));
        	 }
        	 logAccountUpdate.setNewCountryISOCode(original.getCountryCode());
        	 logAccountUpdate.setNewStreetAddressLine1(GRTUtil.trimPropertytoMaxLength(modified.getPrimaryAccountStreetAddress(), 255));
        	 logAccountUpdate.setNewStreetAddressLine2(GRTUtil.trimPropertytoMaxLength(modified.getPrimaryAccountStreetAddress2(),255));
        	 logAccountUpdate.setNewMainPhoneNumber(GRTUtil.trimPropertytoMaxLength(modified.getPhoneNumber(), 255));
        	 logAccountUpdate.setNewPostalCode(modified.getPrimaryAccountPostalCode());
    	 }
    	 if(original != null && modified != null){
    		 logAccountUpdate.setDescribeDeliverables(original.getModificationSummary(modified));
    	 }
    	 logAccountUpdate.setStatus(response.getStatus());
    	 logAccountUpdate.setErrorMessage(GRTUtil.trimPropertytoMaxLength(response.getErrorMsg()!= null?response.getErrorMsg()[0]:"", 255));
    	 logAccountUpdate.setSsrNumber(GRTUtil.trimPropertytoMaxLength(response.getSSRNumber(), 255));
    	 return logAccountUpdate;
     }
	 
	 /**
	 * Construct theree char ISO code
	 * 
	 * @param countryName
	 * @return 3 char iso code string
	 */
	private String getThreeCharISOCode(String countryName){
         List<Country> countryList = PageCache.countryMap;
         String country3Char = null;
         logger.debug("twoCharISOCode : "+countryName);
         for (Country country : countryList) {
             if(country.getName().trim().equalsIgnoreCase(countryName.trim())) {
                 country3Char =   country.getThreeCharISOCode();
                 logger.debug("twoCharISOCode "+ countryName +" country3Char : "+ country3Char);
                 return country3Char;
             }
         }
         return null;
     }
	
	/**
	 * account creation begin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String accountCreation() throws Exception {
		logger.debug("In account creation in AccountCreationAction");
		validateSession();
		try {
			begin();
			actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
			actionForm.getAccountFormBean().setTokenNumberFlag("none");
			actionForm.getAccountFormBean().setShipToCreationFlagForBP("none");
			actionForm.getAccountFormBean().setShipToCreationFlagForNonBP("none");
			actionForm.getAccountFormBean().setAccountAddressFlag("none");
			actionForm.getAccountFormBean().setFuzzySearchFlag("none");
			actionForm.getAccountFormBean().setShipTosoldToFlag("none");
			actionForm.getAccountFormBean().setConfirmationFlag("none");
			actionForm.getAccountFormBean().setAcountCreationFlag("block");
			actionForm.getAccountFormBean().setCountry(GRTConstants.COUNTRY_US);
			actionForm.getAccountFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
			CSSPortalUser userProfile = getUserFromSession();
			if ("B".equals(getUserFromSession().getUserType())) {
				actionForm.getAccountFormBean().setBplinkId(userProfile.getBpLinkId());
				actionForm.getAccountFormBean().setUserType("B");
				logger.debug(" username: " + userProfile.getFirstName() + ", "
						+ userProfile.getLastName());
				actionForm.getAccountFormBean().setBpName(userProfile.getFirstName() + " "
						+ userProfile.getLastName());
			} else {
				actionForm.getAccountFormBean().setUserType("C");
			}
			actionForm.getAccountFormBean().setCountryList(PageCache.countrySelectMap);
			logger.debug("registrationDelegate.accountCreation() country list: "
					+ actionForm.getAccountFormBean().getCountryList());

		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting accountCreation");
		}
		return Action.SUCCESS;
	}
	
	/**
	 * show fuzzy search fields.
	 *  
	 * @return
	 * @throws Exception
	 */
	//For BFOWARNING
	public boolean validateBFOWarning() {
		if(actionForm !=null && actionForm.getAccountFormBean().getSoldToId()!= null && actionForm.getAccountFormBean().getSoldToId().length()>SOLDTOID_MAX_LENGTH){
			return false;
		}
		return true;
	}
	
	public String showFuzzySearch() throws Exception {
		logger.debug("AccountCreationAction.showFuzzySearch()");
		validateSession();
		
		//For BFOWARNING
		if(!(validateBFOWarning())){
			return Action.ERROR;
		}
		
        actionForm.getAccountFormBean().setEndCustomerCity(null);
        actionForm.getAccountFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
        actionForm.getAccountFormBean().setEndCustomerName(null);
        actionForm.getAccountFormBean().setEndCustomerOtherProvince(null);
        actionForm.getAccountFormBean().setEndCustomerPostalCode(null);
        actionForm.getAccountFormBean().setEndCustomerProvince(null);
        actionForm.getAccountFormBean().setEndCustomerState(null);
        actionForm.getAccountFormBean().setEndCustomerStreetName(null);
        actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
        actionForm.getAccountFormBean().setSelectedSAPId(null);
        actionForm.getAccountFormBean().setOriginalAccount(null);
        actionForm.getAccountFormBean().setUpdatedAccount(null);
        logger.debug("actionForm.getAccountFormBean().getShipTo() : "
            + actionForm.getAccountFormBean().getSoldToId());
        if (checkNonEmpty(actionForm.getAccountFormBean().getSoldToId()))
        {
            Account account = getAccountService().queryAccount(actionForm.getAccountFormBean().getSoldToId().trim());
            if (account != null)
            {
                actionForm.getAccountFormBean().setEndCustomerName(account.getName());
                actionForm.getAccountFormBean().setEndCustomerStreetName(account.getPrimaryAccountStreetAddress());
                actionForm.getAccountFormBean().setEndCustomerCity(account.getPrimaryAccountCity());
                actionForm.getAccountFormBean().setEndCustomerPostalCode(account.getPrimaryAccountPostalCode());
                actionForm.getAccountFormBean().setEndCustomerCountry(account.getPrimaryAccountCountry());
                logger.debug(account.getPrimaryAccountCountry());
                String twochar = getTwoCharISOCode(actionForm.getAccountFormBean().getEndCustomerCountry());
                if (checkNonEmpty(twochar))
                {
                    actionForm.getAccountFormBean().setEndCustomerCountry(twochar);
                }
                Map<Long,Region> countryRegion = PageCache.regionMap.get(actionForm.getAccountFormBean().getEndCustomerCountry());
                
                if (countryRegion != null){
                    for (Map.Entry<Long, Region> region : countryRegion.entrySet())
                    {
                        String value = region.getValue().getDescription();
                        String desc = region.getValue().getRegion();
                        logger.debug(value);
                        logger.debug(desc);
                        if ( (value != null && value.equalsIgnoreCase(account.getPrimaryAccountState()) )  || (desc!=null && desc.equalsIgnoreCase(account.getPrimaryAccountState())))
                        {
                            actionForm.getAccountFormBean().setSelectedRegionId(region.getValue().getRegionId());
                        }
                    }
                }
                else 
                {
                 actionForm.getAccountFormBean().setRegionValue(account.getPrimaryAccountState());   
                }
                
                logger.debug(actionForm.getAccountFormBean().getEndCustomerCountry());
                logger.debug(account.getPrimaryAccountState());
            }
            else 
            {
                getRequest().setAttribute("soldtosearcherror",actionForm.getAccountFormBean().getSoldToId().trim() + " : is not a valid Sold To/Functional Location." );
            }
        }
        setAllFlagNone();
		actionForm.getAccountFormBean().setSearchFlag(GRTConstants.BLOCK);
		actionForm.getAccountFormBean().setCountryList(PageCache.countrySelectMap);
		actionForm.getAccountFormBean().setStateList(PageCache.stateSelectMap);
		actionForm.getAccountFormBean().setProvinceList(PageCache.provinceSelectMap);
        actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
        if (actionForm.getAccountFormBean().getEndCustomerCountry()== null)
        {
            actionForm.getAccountFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
        }
        actionForm.getAccountFormBean().setCurrentCountryRegion(actionForm.getAccountFormBean().getRegionMap().get(actionForm.getAccountFormBean().getEndCustomerCountry()));
        actionForm.getAccountFormBean().setRegionValue(null);
		return Action.SUCCESS;
	}

	/**
	 * method to perform fuzzy search for account update
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String doFuzzySearchForUpdate() throws Throwable {
		validateSession();
        updateRegion();
        actionForm.getAccountFormBean().setSelectedSAPId(null);
        actionForm.getAccountFormBean().setOriginalAccount(null);
        actionForm.getAccountFormBean().setUpdatedAccount(null);
        List<Result> results = null;
        List<String> soldToList = new ArrayList<String>();
        List<String> accessList = new ArrayList<String>();
		List<Result> finalResults = new ArrayList<Result>();
		boolean error = false;
		try {
			results = getFuzzySearchResult();
			// Validating BP account access
			CSSPortalUser userProfile = getUserFromSession();
			if (GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userProfile.getUserType())) {
				if(results != null && results.size() > 0){
					for(Result result : results){
						soldToList.add(result.getAccountNumber());
					}
					accessList = getAccountService().querySoldToListAccess(userProfile.getBpLinkId(), soldToList);
					logger.debug("BPLinkId have access to "+accessList.size()+" accounts.");
					for(Result result : results){
						if(accessList != null && accessList.size() > 0 
								&& accessList.contains(result.getAccountNumber())){
							finalResults.add(result);
						}
					}
					logger.debug("FinalList->BPLinkId have access to "+accessList.size()+" accounts.");
				}
				actionForm.getAccountFormBean().setSearchResult(finalResults);
            } else {
            	actionForm.getAccountFormBean().setSearchResult(results);
            }
		} catch (Exception exception) {
			error = true;
			exception.printStackTrace();
			logger.debug("Error occur in calling address search service");
		}
	
        if (actionForm.getAccountFormBean().getEndCustomerCountry()== null)
        {
            actionForm.getAccountFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
        }
        actionForm.getAccountFormBean().setCurrentCountryRegion(actionForm.getAccountFormBean().getRegionMap().get(actionForm.getAccountFormBean().getEndCustomerCountry()));
        
		if (actionForm.getAccountFormBean().getSearchResult() != null) {
			if (actionForm.getAccountFormBean().getSearchResult().size() == 0) {
                setAllFlagNone();
                actionForm.getAccountFormBean().setSearchFlag(GRTConstants.BLOCK);
                actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
                getRequest().setAttribute("noresult", true);
			} else {
				actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
				actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
				actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
				actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
				actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.BLOCK);
				actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.BLOCK);
				actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
				actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);

			}
		} else if (error) {
			actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
			getRequest().setAttribute("searchError", grtConfig.getSearchAccErrMsg());			
		}
		return Action.SUCCESS;
	}
	
	 /**
	 * method to update region.
	 */
	private void updateRegion (){
         logger.debug("Country  : "+ actionForm.getAccountFormBean().getEndCustomerCountry());
         logger.debug("RegionId : "+ actionForm.getAccountFormBean().getSelectedRegionId());
         Map<Long,Region> regionmap = actionForm.getAccountFormBean().getRegionMap().get(actionForm.getAccountFormBean().getEndCustomerCountry());
         actionForm.getAccountFormBean().setRegionValue(null);
         if (actionForm.getAccountFormBean().getSelectedRegionId() == 0L && regionmap != null)
         {
             actionForm.getAccountFormBean().setRegionId(null);
             actionForm.getAccountFormBean().setRegionValue(null);
         }
         if ( actionForm.getAccountFormBean().getSelectedRegionId() == 0L && StringUtils.isEmpty(actionForm.getAccountFormBean().getRegionValue()))
         {
             actionForm.getAccountFormBean().setRegionValue(null);
         }
         if ( regionmap != null && regionmap.get(actionForm.getAccountFormBean().getSelectedRegionId()) != null )
         {
             Region region = regionmap.get(actionForm.getAccountFormBean().getSelectedRegionId());
             actionForm.getAccountFormBean().setRegionId(region.getRegion());
             actionForm.getAccountFormBean().setRegionValue(region.getDescription());
             if (region.getSiebeldescription() != null && region.getSiebeldescription().length() > 0) {
            	 actionForm.getAccountFormBean().setSiebelRegionValue(region.getSiebeldescription());
             } else {
            	 actionForm.getAccountFormBean().setSiebelRegionValue(region.getDescription());
             }
         }
         else 
         {
             logger.debug("RegionValue   : " + actionForm.getAccountFormBean().getRegionValue());
             actionForm.getAccountFormBean().setRegionId(actionForm.getAccountFormBean().getRegionValue());
         }
         logger.debug("RegionId      : " + actionForm.getAccountFormBean().getRegionId());
         logger.debug("RegionValue   : " + actionForm.getAccountFormBean().getRegionValue());

     }
	 
	 /**
	 * generate fuzzy search result
	 * 
	 * @return List of accounts
	 * @throws Throwable
	 */
	private List<Result> getFuzzySearchResult()
	            throws Throwable {
	        AddressSearchRequestType addressSearchRequestType = new AddressSearchRequestType();
	        List<Request> requestList = new ArrayList<Request>();
	        List<Result> resultList = new ArrayList<Result>();
	        try {
	            List<FuzzySearchParam> fuzzySearchParamList = getAccountService().getFuzzySearchParams();
	            for (FuzzySearchParam fuzzySearchParam : fuzzySearchParamList) {        
	                Request request = new Request();
	                if (fuzzySearchParam.getProperty().equals(GRTConstants.ACCOUNT_NAME) && (actionForm.getAccountFormBean().getEndCustomerName() != null) && (actionForm.getAccountFormBean().getEndCustomerName().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getEndCustomerName().trim());                 
	                    request.setName(GRTConstants.SEARCH_NAME);
	                } else if  (fuzzySearchParam.getProperty().equals(GRTConstants.STREET) && (actionForm.getAccountFormBean().getEndCustomerStreetName() != null) && (actionForm.getAccountFormBean().getEndCustomerStreetName().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getEndCustomerStreetName().trim());   
	                    request.setName(GRTConstants.SEARCH_STREET);
	                } else if (fuzzySearchParam.getProperty().equals(GRTConstants.CITY) && (actionForm.getAccountFormBean().getEndCustomerCity() != null) && (actionForm.getAccountFormBean().getEndCustomerCity().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getEndCustomerCity().trim());
	                    request.setName(GRTConstants.SEARCH_CITY);
	                } else if ((fuzzySearchParam.getProperty().equals(GRTConstants.REGION)) && (actionForm.getAccountFormBean().getRegionId() != null) && (actionForm.getAccountFormBean().getRegionId().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getRegionId().trim());
	                    request.setName(GRTConstants.SEARCH_REGION);
	                } else if  (fuzzySearchParam.getProperty().equals(GRTConstants.COUNTRY) && (actionForm.getAccountFormBean().getEndCustomerCountry() != null) && (actionForm.getAccountFormBean().getEndCustomerCountry().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getEndCustomerCountry().trim());  
	                    request.setName(GRTConstants.SEARCH_COUNTRY);
	                } else if (fuzzySearchParam.getProperty().equals(GRTConstants.ZIP) && (actionForm.getAccountFormBean().getEndCustomerPostalCode() != null) && (actionForm.getAccountFormBean().getEndCustomerPostalCode().trim().length() > 0)) {
	                    request.setValue(actionForm.getAccountFormBean().getEndCustomerPostalCode().trim());   
	                    request.setName(GRTConstants.SEARCH_ZIP);
	                }
	                if (request.getValue() != null) {
	                    request.setNameThreshhold(new BigDecimal(fuzzySearchParam.getThreshold()).setScale(Integer.parseInt(GRTConstants.THRESOLD_SCALE), RoundingMode.CEILING));
	                    request.setNameWeight(new BigDecimal(fuzzySearchParam.getWeightage()));
	                    requestList.add(request);
	                }                
	                logger.debug("fuzzySearchParam: fuzzySearchParam.getProperty()"+fuzzySearchParam.getProperty() + " fuzzySearchParam.getThreshold() : "+ fuzzySearchParam.getThreshold() + " fuzzySearchParam.getWeightage():"+ fuzzySearchParam.getWeightage() );               
	            }
	            addressSearchRequestType.setRequest(requestList.toArray(new Request[requestList.size()]));
	            addressSearchRequestType.setMinThreshhold(new BigDecimal(grtConfig.getMinThreshhold().trim()));
	            addressSearchRequestType.setTopRecords(new BigInteger(grtConfig.getTopRecords().trim()));
	            resultList = getAccountService().getSearchResults(addressSearchRequestType);
	        } catch (NullPointerException nex) {
	            logger.error(nex.getMessage());
	            nex.printStackTrace();
	            throw nex;
	        } catch (RemoteException rex) {
	            logger.error(rex.getMessage());
	            rex.printStackTrace();
	            throw rex;
	        } catch (Throwable e) {
	            logger.error(e.getMessage());
	            e.printStackTrace();
	            throw e;
	        }
	        return resultList;
	    }
	 
	 /**
	 * method to validate account for account update
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validateAccountForUpdate() throws Exception {
	        validateSession();
	        String soldtoId = actionForm.getAccountFormBean().getSelectedSAPId();
	        logger.debug("###### SoldToID  validateAccountForUpdate " + soldtoId);
	        try
	        {
	            Account account = getAccountService().queryAccountWithContacts(soldtoId);
	            logger.debug("###### account  validateAccountForUpdate " + account);
	           if (account == null || account.getName() == null || account.getAccountId() == null){
	               actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.BLOCK);
	               actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
	               getRequest().setAttribute("searchError", grtConfig.getNoRecFoundAccErrMsg());
	               return Action.SUCCESS;
	           }else {
	               logger.debug("###### account  validateAccountForUpdate " + account);
	                actionForm.getAccountFormBean().setOriginalAccount(account);
	                actionForm.getAccountFormBean().setUpdatedAccount(account.clone());
	                return populateAccountForUpdate();
	            }
	        }
	        catch (Exception exception)
	        {
	            actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.BLOCK);
	            actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.NONE);
	            getRequest().setAttribute("searchError", grtConfig.getNoRecFoundAccErrMsg());
	        }
	        return Action.SUCCESS;
	}
	 
	 
	 
	/*Account creation */
	
	 /**
	 * Prepare account creation form elements.
	 * 
	 * @return success String
	 * @throws Exception
	 */
	public String nextAccountCreation() throws Exception {
 		  validateSession();
         actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
         actionForm.getAccountFormBean().setAccountCreationReason(null);
         if (actionForm.getAccountFormBean().getCountry()== null)
         {
             actionForm.getAccountFormBean().setCountry(GRTConstants.COUNTRY_US);
         }
         actionForm.getAccountFormBean().setCurrentCountryRegion(actionForm.getAccountFormBean().getRegionMap().get(actionForm.getAccountFormBean().getCountry()));
 		actionForm.getAccountFormBean().setEUFlag("False");
		if (actionForm.getAccountFormBean().getBplinkId() != null && actionForm.getAccountFormBean().getBplinkId().length() > 0) {
			
			try {
				BusinessPartner bp = getInstallBaseService().getBusinessPartner(actionForm.getAccountFormBean().getBplinkId());
				if (bp != null) {					
					logger.debug(" BP Name: bp.getSoldToName(): "+bp.getSoldToName());
					actionForm.getAccountFormBean().setBpName(bp.getSoldToName());	
					logger.debug(" bp is not null : bp.getBpLinkId()"+ bp.getBpLinkId());
				}
		
				if (bp == null) {
					logger.debug(" bp is null");
				}
				
				if (bp == null) {
					
					actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
					actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
					actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
					actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
					actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
					actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
					actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
					
					String bpLinkId = actionForm.getAccountFormBean().getBplinkId();
					actionForm.getAccountFormBean().setCountryList(PageCache.countrySelectMap);
					logger.debug("Invalid BP Link Id : "
							+ bpLinkId);
					actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.BLOCK);
					return backToAccountCreationPage(grtConfig.getEwiMessageCodeMap().get("accCrtInvalidBPLinkIdErrMsg") + "###" + grtConfig.getAccCrtInvalidBPLinkIdErrMsg().replace("<BPKLINKID>", bpLinkId));
					
				}
			} catch (Throwable throwable){
	            logger.error("", throwable);
	        }	
			
		} 
		if (actionForm.getAccountFormBean().getTokenNumberSubmissionFlag() != null && actionForm.getAccountFormBean().getTokenNumberSubmissionFlag().length() > 0) {
			actionForm.getAccountFormBean().setTokenNumberSubmissionFlag("");
		}
		
		logger.debug("actionForm.getAccountFormBean().getCountry: " + actionForm.getAccountFormBean().getCountry());
		logger.debug("actionForm.getAccountFormBean().redeem:  " + actionForm.getAccountFormBean().getRedeem());
		logger.debug("In AccountController :  nextAccountCreation");
		actionForm.getAccountFormBean().setCountryValue(PageCache.countrySelectMap.get(actionForm.getAccountFormBean().getCountry()));
		actionForm.getAccountFormBean().setEndCustomerCountry(actionForm.getAccountFormBean().getCountry());

		if (GRTConstants.COUNTRY_US.equals(actionForm.getAccountFormBean().getCountry())) {
			actionForm.getAccountFormBean().setSapBox(GRTConstants.BBoxDestination);

		} else {
			actionForm.getAccountFormBean().setSapBox(GRTConstants.IBoxDestination);
		}
		actionForm.getAccountFormBean().setCountry(actionForm.getAccountFormBean().getCountry());
		actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
		actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.BLOCK);
		actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setBpName(actionForm.getAccountFormBean().getBpName());
		actionForm.getAccountFormBean().setCountryList(PageCache.countrySelectMap);
		actionForm.getAccountFormBean().setProvinceList(PageCache.provinceSelectMap);
		actionForm.getAccountFormBean().setStateList(PageCache.stateSelectMap);
		logger.debug("actionForm.getAccountFormBean().getProvinceList:  " + actionForm.getAccountFormBean().getProvinceList());
		actionForm.getAccountFormBean().setRegionValue(null);
       return Action.SUCCESS;
	}
	 
	 /**
	 * calls fuzzy search service and generate search result
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String doFuzzySearch() throws Throwable {
			validateSession();
	        actionForm.getAccountFormBean().setRegionMap(PageCache.regionMap);
	        actionForm.getAccountFormBean().setAccountCreationReason(null);
	        actionForm.getAccountFormBean().setSelectedAccountNumber(null);
	        actionForm.getAccountFormBean().setCurrentCountryRegion(actionForm.getAccountFormBean().getRegionMap().get(actionForm.getAccountFormBean().getEndCustomerCountry()));
	        actionForm.getAccountFormBean().setSelectedSAPId(null);
	        actionForm.getAccountFormBean().setOriginalAccount(null);
	        actionForm.getAccountFormBean().setUpdatedAccount(null);
	        logger.debug("getSelectedRegionId  : "+ actionForm.getAccountFormBean().getSelectedRegionId());
			if (actionForm.getAccountFormBean().getEndCustomerCountry().equalsIgnoreCase("US"))
	        {
				actionForm.getAccountFormBean().setSapBox(GRTConstants.BBoxDestination);
	        } else {
	        	actionForm.getAccountFormBean().setSapBox(GRTConstants.IBoxDestination);
	        }

	        updateRegion();

	        List<Result> results = null;
	        boolean error = false;
	        try {
	            results = getFuzzySearchResult();
	            actionForm.getAccountFormBean().setSearchResult(results);
	        } catch (Exception exception) {
	            error = true;
	            exception.printStackTrace();
	            logger.debug("Error occur in calling address search service");
	        }
	    
	        if (actionForm.getAccountFormBean().getSearchResult() != null) {
	            if (actionForm.getAccountFormBean().getSearchResult().size() == 0) {
	            	actionForm.getAccountFormBean().setSelectedAccountNumber(GRTConstants.NONE_OF_THE_ABOVE);
	            }
	            actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
	            actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
	            actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
	            actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
	            actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.BLOCK);
	            actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.BLOCK);
	            actionForm.getAccountFormBean().setSearchDataFlag(GRTConstants.BLOCK);
	            actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
	            actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
	       } else if (error) {
	            actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
	            getRequest().setAttribute("searchError", grtConfig.getSearchAccErrMsg());
	        }
	        logger.debug("RegionId      : " + actionForm.getAccountFormBean().getRegionId());
	        logger.debug("RegionValue   : " + actionForm.getAccountFormBean().getRegionValue());
	        return Action.SUCCESS;
	    }
	 
	 /**
	 * back to account creation page with error message
	 * 
	 * @param errMsg
	 * @return
	 */
	private String backToAccountCreationPage(String errMsg) {
			this.getRequest().setAttribute("bpLinkIdError", errMsg);
			return Action.SUCCESS;
	}
	 
	public String showShipToSoldTo() throws Exception {
	    	validateSession();
	        logger.debug("Account Creation reason : " + actionForm.getAccountFormBean().getAccountCreationReason());
	        logger.debug("actionForm.getAccountFormBean().getCountry(): "+ actionForm.getAccountFormBean().getCountry() +", actionForm.getAccountFormBean().getCountryValue(): "+actionForm.getAccountFormBean().getCountryValue()+", actionForm.getAccountFormBean().getEndCustomerCountry()"+actionForm.getAccountFormBean().getEndCustomerCountry());
			actionForm.getAccountFormBean().setCountryValue(PageCache.countrySelectMap.get(actionForm.getAccountFormBean().getEndCustomerCountry()));
			logger.debug("actionForm.getAccountFormBean().getCountryValue(): "+actionForm.getAccountFormBean().getCountryValue());
	        
	        if (actionForm.getAccountFormBean().getCountryValue() != null) {
	            boolean isEuCountry = checkEUCountry(actionForm.getAccountFormBean().getCountryValue());
	            logger.debug("actionForm.getAccountFormBean().getCountryValue() eu flag:" + isEuCountry);
	            getRequest().setAttribute("euFlag", new Boolean(isEuCountry));
	            if ((Boolean)getRequest().getAttribute("euFlag")) {
	                actionForm.getAccountFormBean().setEUFlag("True");
	                logger.debug("actionForm.getAccountFormBean().getCountryValue() eu flag in boolean check:" + isEuCountry);
	            }
	        }
	        updateRegion();
	        
	        actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
	        actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.BLOCK);
	        actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);

	        if (GRTConstants.YES.equals(actionForm.getAccountFormBean().getRedeem())) {
	            actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
	        } else if (GRTConstants.NO.equals(actionForm.getAccountFormBean().getRedeem())
	                && actionForm.getAccountFormBean().getSapBox().equals(GRTConstants.IBoxDestination)) {
	            if ("B".equals(getUserFromSession().getUserType())) {
	                actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
	            } else {
	                actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.BLOCK);
	            }
	        }
	        logger.debug("RegionId      : " + actionForm.getAccountFormBean().getRegionId());
	        logger.debug("RegionValue   : " + actionForm.getAccountFormBean().getRegionValue());
	        return Action.SUCCESS;
	    }
	
	 
	 /**
	 * Check for country EU
	 * 
	 * @param countryName
	 * @return
	 */
	private boolean checkEUCountry(String countryName) {
			List<Country> countryList = PageCache.countryMap;
			for (Country country : countryList) {
				if(country.getName().equalsIgnoreCase(countryName)) {
					return country.isEuflag();
				}
			}
			return Boolean.FALSE;
		}
	 
	 
	 /**
	 * Method to verify token
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String tokenVerification() throws Throwable {
			validateSession();
			TokenResponseTypeDto tokenResponse = null;
			logger
					.debug("................ Starting for tokenVerification records fetching ................");
			logger.debug("................ token number ................: "
					+ actionForm.getAccountFormBean().getTokenNumber());
			String tokenNumber = null;
			if (actionForm.getAccountFormBean().getTokenNumber() != null) {
				tokenNumber = actionForm.getAccountFormBean().getTokenNumber();
			}
			if (tokenNumber != null) {
				logger.debug("................ token number ................: "
						+ tokenNumber);

				try {
					tokenResponse = getAccountService().tokenVerification(
							tokenNumber);
					actionForm.getAccountFormBean().setSoldToId(tokenResponse.getDistiSoldTo());

					logger.debug("................ SoldToId ................: "
							+ actionForm.getAccountFormBean().getSoldToId());

					if (GRTConstants.TOKEN_INVALID_CODE.equals(tokenResponse.getCode())) {
						logger
								.debug("................ Invalid Token ................:"
										+ tokenNumber);						
						return backToTokenSearchErrorPage(grtConfig.getTokenIvalidError().replace("<TOKEN>", tokenNumber));
					} else if (GRTConstants.TOKEN_SUCESS_CODE.equals(tokenResponse.getCode())) {
						if (GRTConstants.TOKEN_ACTIVE.equals(tokenResponse.getStatus())) {
							logger
							.debug("................ Valid Token ................: "
									+ tokenNumber);
						if (tokenResponse.getRequesterSalesOrg().equals(GRTConstants.B_BOX_SALES_ORG) && actionForm.getAccountFormBean().getSapBox().equals(GRTConstants.IBoxDestination)) {
							return backToTokenSearchErrorPage(grtConfig.getAccCrtInvalidTokenNonUSErrMsg().replace("<TOKEN>", tokenNumber));
						} else if (!(tokenResponse.getRequesterSalesOrg().equals(GRTConstants.B_BOX_SALES_ORG)) && GRTConstants.BBoxDestination.equals(actionForm.getAccountFormBean().getSapBox())) {
							return backToTokenSearchErrorPage(grtConfig.getAccCrtInvalidTokenUSErrMsg().replace("<TOKEN>", tokenNumber));
						}
						actionForm.getAccountFormBean().setTokenNumberSubmissionFlag(tokenNumber);
						actionForm.getAccountFormBean().setSoldToId(tokenResponse.getDistiSoldTo());
						actionForm.getAccountFormBean().setSalesOrg(tokenResponse.getRequesterSalesOrg());
						actionForm.getAccountFormBean().setDistributionChannel(tokenResponse.getRequesterDistributionChannel());
						actionForm.getAccountFormBean().setDivision(tokenResponse.getRequesterDivision());
						logger.debug("ServiceTerm:"+tokenResponse.getServiceTerm());
						actionForm.getAccountFormBean().setServiceTerm(tokenResponse.getServiceTerm());
						return backToTokenSearchMsgPage(grtConfig.getAccCrtValidTokenMsg());
						
					} else {
							logger
							.debug(tokenResponse.getStatus()+ "  "+ tokenNumber);
							return backToTokenSearchErrorPage(tokenResponse.getStatus()	+" : "+ tokenNumber);
							
					}
				}
			} catch (NullPointerException nex) {
				logger.error(nex.getMessage());
				nex.printStackTrace();
			} catch (RemoteException rex) {
				logger.error(rex.getMessage());
				rex.printStackTrace();
			} catch (Throwable e) {
				actionForm.getAccountFormBean().setSapBox("");
				logger.error(e.getMessage());
				e.printStackTrace();
				throw e;
			}
			logger.debug("tokenResponse data: : " + tokenResponse);
		}
		return Action.SUCCESS;
	}
	 
	 /**
	 * Go back to token search with error message
	 * 
	 * @param errMsg
	 * @return
	 */
	private String backToTokenSearchErrorPage(String errMsg) {
			this.getRequest().setAttribute("tokenSearchError", errMsg);
			return Action.SUCCESS;
	}
	
	/**
	 * set token search message
	 * 
	 * @param msg
	 * @return
	 */
	private String backToTokenSearchMsgPage(String msg) {
			this.getRequest().setAttribute("tokenSearchMsg", msg);
			return Action.SUCCESS;
	}
	
	
	/**
	 * Method for creating account.
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String accountCreationConfirmation() throws Throwable {
		validateSession();
		logger.debug("RegionId      : " + actionForm.getAccountFormBean().getRegionId());
        logger.debug("RegionValue   : " + actionForm.getAccountFormBean().getRegionValue());
		logger.debug("In accountCreationConfirmation() usertype: "+getUserFromSession().getUserType());
		String accountType = null;
		updateRegion();
		if (actionForm.getAccountFormBean().getSelectedAccountNumber() != null && (!actionForm.getAccountFormBean().getSelectedAccountNumber().equals(GRTConstants.NONE_OF_THE_ABOVE))) {
			accountType = GRTConstants.ACCOUNT_UPDATE;
			if ("B".equals(getUserFromSession().getUserType())) {
				logger.debug("BP Link Id:" +getUserFromSession().getBpLinkId());
				saveToBpAccountSaveAccess(accountType);
			}
            else 
            {
                actionForm.getAccountFormBean().setAccountIdForUpdate(actionForm.getAccountFormBean().getSelectedAccountNumber().split(":")[0]);
                actionForm.getAccountFormBean().setResponseCode("1000");
                if (actionForm.getAccountFormBean().getBplinkId() != null && actionForm.getAccountFormBean().getBplinkId().trim().length() > 0){
                    saveToBpAccountSaveAccess(accountType);
                }
            }
		} else {
			List<Country> countryList = PageCache.countryMap;
	        String country3Char = null;
	        logger.debug("actionForm.getAccountFormBean().getEndCustomerCountry(): "+actionForm.getAccountFormBean().getEndCustomerCountry());
	        for (Country country : countryList) {
	        	if(country.getCode().equalsIgnoreCase(actionForm.getAccountFormBean().getEndCustomerCountry())) {
	    	        logger.debug("country.getCode() "+country.getCode() +" for country: "+actionForm.getAccountFormBean().getEndCustomerCountry());
	        		country3Char =   country.getThreeCharISOCode();
	        		actionForm.getAccountFormBean().setCountry3Char(country3Char);
	        	}
	        }
	        
	        logger.debug("actionForm.getAccountFormBean().getCountry3Char: "+actionForm.getAccountFormBean().getCountry3Char() +" for country: "+actionForm.getAccountFormBean().getEndCustomerCountry());
			
			if (actionForm.getAccountFormBean().getSapBox().equals(GRTConstants.IBoxDestination)) {
				ShipToCreateResponseType shipToCreateResponseType = sapShipToAddressCall();
				if (shipToCreateResponseType != null && shipToCreateResponseType.getShipTo().length() > 0) {
					actionForm.getAccountFormBean().setShipToId(shipToCreateResponseType.getShipTo());
				}
				if (shipToCreateResponseType.getCode() != null) {
					actionForm.getAccountFormBean().setResponseCode(shipToCreateResponseType.getCode());
				}
				if (shipToCreateResponseType.getShipTo() != null) {
					actionForm.getAccountFormBean().setResponseUniqueId(shipToCreateResponseType.getShipTo());
				}
				if (shipToCreateResponseType.getDescription() != null && shipToCreateResponseType.getCode() != null && (!shipToCreateResponseType.getCode().equals("1000"))) {
					actionForm.getAccountFormBean().setResponseDescription(shipToCreateResponseType.getDescription());
				}
				if (shipToCreateResponseType.getAccountId() != null) {
					actionForm.getAccountFormBean().setResponseAccountId(shipToCreateResponseType.getAccountId());
				}
			} else if (actionForm.getAccountFormBean().getSapBox().equals(GRTConstants.BBoxDestination)) {
				SoldToCreateResponseType soldToCreateResponseType = sapSoldToAddressCall();
				if (soldToCreateResponseType != null && soldToCreateResponseType.getSoldTo().length() > 0) {
					actionForm.getAccountFormBean().setSoldToId(soldToCreateResponseType.getSoldTo());
				}
				if (soldToCreateResponseType.getCode() != null) {
					actionForm.getAccountFormBean().setResponseCode(soldToCreateResponseType.getCode());
				}
				if (soldToCreateResponseType.getSoldTo() != null) {
					actionForm.getAccountFormBean().setResponseUniqueId(soldToCreateResponseType.getSoldTo());
				}
				if (soldToCreateResponseType.getDescription() != null && soldToCreateResponseType.getCode() != null && (!soldToCreateResponseType.getCode().equals("1000"))) {
					actionForm.getAccountFormBean().setResponseDescription(soldToCreateResponseType.getDescription());
				}
				if (soldToCreateResponseType.getAccountId() != null) {
					actionForm.getAccountFormBean().setResponseAccountId(soldToCreateResponseType.getAccountId());
				}
			}
			actionForm.getAccountFormBean().setEmailNotificationFlag(GRTConstants.NONE);
			if (actionForm.getAccountFormBean().getResponseCode().equals("1000")) {
				if (StringUtils.isNotEmpty(actionForm.getAccountFormBean().getBplinkId())) {
					logger.debug("BP Link Id:" + actionForm.getAccountFormBean().getBplinkId());
					saveToBpAccountSaveAccess(accountType);
				}				
			} else {
				return backToAccountCreationErrorPage(actionForm.getAccountFormBean().getResponseDescription());
			}
		}
		
		actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
		actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
		actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.BLOCK);

		return Action.SUCCESS;
	}
	
	/**
	 * save 
	 * 
	 * @param accountType
	 * @throws Throwable
	 */
	private void saveToBpAccountSaveAccess(String accountType) throws Throwable {
		
		BPAccountTempAccess bPAccountTempAccess = new BPAccountTempAccess();
		if (actionForm.getAccountFormBean().getSelectedAccountNumber() != null && (!actionForm.getAccountFormBean().getSelectedAccountNumber().equals(GRTConstants.NONE_OF_THE_ABOVE))) {
			String[] account = actionForm.getAccountFormBean().getSelectedAccountNumber().split(":");
			bPAccountTempAccess.setAccountId(account[0]);
			bPAccountTempAccess.setAccountName(account[1]);
			
			actionForm.getAccountFormBean().setAccountIdForUpdate(account[0]);
			actionForm.getAccountFormBean().setEndCustomerName(account[1]);
			actionForm.getAccountFormBean().setEndCustomerStreetName(account[2]);
			actionForm.getAccountFormBean().setEndCustomerCity(account[3]);
			actionForm.getAccountFormBean().setEndCustomerState(account[4]);
			actionForm.getAccountFormBean().setEndCustomerPostalCode(account[5]);
			actionForm.getAccountFormBean().setEndCustomerCountry(account[6]);
			
			if (actionForm.getAccountFormBean().getResponseCode() != null) {
				actionForm.getAccountFormBean().setResponseCode("1000");
			}
			logger.debug("In saveToBpAccountSaveAccess method: setEndCustomerName(): "+actionForm.getAccountFormBean().getEndCustomerName() +
				      ", Street : "+actionForm.getAccountFormBean().getEndCustomerStreetName() + ", city : "+actionForm.getAccountFormBean().getEndCustomerCity()+ " , zip: "+actionForm.getAccountFormBean().getEndCustomerPostalCode());
			
			
		} else if (actionForm.getAccountFormBean().getResponseAccountId() != null) {
			bPAccountTempAccess.setSiebelId(actionForm.getAccountFormBean().getResponseAccountId());
			bPAccountTempAccess.setAccountId(actionForm.getAccountFormBean().getResponseUniqueId());
			bPAccountTempAccess.setAccountName(actionForm.getAccountFormBean().getEndCustomerName());
		}
		bPAccountTempAccess.setBpLinkId(actionForm.getAccountFormBean().getBplinkId());
		bPAccountTempAccess.setCreatedBy(this.getUserFromSession().getUserId());
		
		logger.debug("In accountCreationConfirmation method: accountId(): "+bPAccountTempAccess.getAccountId() +
			      ", accountName : "+bPAccountTempAccess.getAccountName() + ", bpLinkId : "+actionForm.getAccountFormBean().getBplinkId() + 
			      " , createdBy: "+this.getUserFromSession().getUserId() + ", serviceTerm : "+actionForm.getAccountFormBean().getServiceTerm());
		
		try {
			getAccountService().saveBPAccountTempAccess(bPAccountTempAccess, actionForm.getAccountFormBean().getServiceTerm(), accountType);
			logger.debug("Added in DB successfully, actionForm.getAccountFormBean().getResponseCode(): "+actionForm.getAccountFormBean().getResponseCode()+ " ,actionForm.getAccountFormBean().getSearchResult().size(): "+actionForm.getAccountFormBean().getSearchResult().size());
			
			if (actionForm.getAccountFormBean().getResponseCode() == null) {
				actionForm.getAccountFormBean().setResponseCode("1000");
			}
			if (actionForm.getAccountFormBean().getSearchResult().size() == 0) {
				actionForm.getAccountFormBean().setSelectedAccountNumber(GRTConstants.NONE_OF_THE_ABOVE);
			}
			actionForm.getAccountFormBean().setEmailNotificationFlag(GRTConstants.BLOCK);
		} catch (NullPointerException nex) {
			logger.error(nex.getMessage());
			nex.printStackTrace();
			throw nex;
		} catch (Throwable e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	 /**
	 * method to create account
	 * 
	 * @return
	 * @throws Throwable
	 */
	private SoldToCreateResponseType sapSoldToAddressCall() throws Throwable {
	    	logger.debug("AccountFormBean:" + actionForm.getAccountFormBean());
	        logger.debug("RegionId  in SoldToCreateResponseType    : " + actionForm.getAccountFormBean().getRegionId());
	        logger.debug("RegionValue in SoldToCreateResponseType  : " + actionForm.getAccountFormBean().getRegionValue());
	        SoldToCreateRequestType  soldToCreateRequestType = new SoldToCreateRequestType();
	        SoldToCreateResponseType soldToCreateResponseType = new SoldToCreateResponseType();
	        
	        soldToCreateRequestType.setSoldToName1(actionForm.getAccountFormBean().getEndCustomerName().trim());
	        soldToCreateRequestType.setSalesOrg(GRTConstants.B_BOX_SALES_ORG);
	        
	        AddressType addressType = new AddressType();
	        addressType.setStreet1(actionForm.getAccountFormBean().getEndCustomerStreetName().trim());
	        addressType.setCity(actionForm.getAccountFormBean().getEndCustomerCity().trim());
	        if (StringUtils.isNotEmpty(actionForm.getAccountFormBean().getRegionId()))
	        {
	            addressType.setRegion(actionForm.getAccountFormBean().getRegionId().trim());
	        }
	        addressType.setCountry(actionForm.getAccountFormBean().getEndCustomerCountry().trim());
	        addressType.setZip(actionForm.getAccountFormBean().getEndCustomerPostalCode().trim());
	       if (StringUtils.isNotEmpty(actionForm.getAccountFormBean().getSiebelRegionValue()))
	       {
	          addressType.setState_Full(actionForm.getAccountFormBean().getSiebelRegionValue().trim());
	       }
	        
	        if (actionForm.getAccountFormBean().getCountry3Char() != null) {
	            addressType.setCountry3Char(actionForm.getAccountFormBean().getCountry3Char().trim());
	        }
	        soldToCreateRequestType.setSoldToAddress(addressType);
	        
	        soldToCreateRequestType.setContactFirstName(actionForm.getAccountFormBean().getContactFirstName().trim());
	        soldToCreateRequestType.setContactLastName(actionForm.getAccountFormBean().getContactLastName().trim());
	        soldToCreateRequestType.setContactEmail(actionForm.getAccountFormBean().getContactEmail().trim());
	        soldToCreateRequestType.setContactPhone(actionForm.getAccountFormBean().getContactPhone().trim());
	        
	        soldToCreateRequestType.setEmail(actionForm.getAccountFormBean().getContactEmail().trim());
	        soldToCreateRequestType.setPhone(actionForm.getAccountFormBean().getContactPhone().trim());
	        
	        if (actionForm.getAccountFormBean().getTokenNumber() != null) {
	            soldToCreateRequestType.setTokenNumber(actionForm.getAccountFormBean().getTokenNumber());
	        }
	        
	        String expectedDeilvery = "*Please set up hierarchy"; 
	        
	        if (actionForm.getAccountFormBean().getUsGovernmentAcount() != null) {
	            CustomerGroupType customerGroupType = null;
	            if (actionForm.getAccountFormBean().getUsGovernmentAcount().contains(GRTConstants.COUNTRY_US)) {
	                customerGroupType = CustomerGroupType.usGovt;
	                expectedDeilvery += " *Please validate US government.";
	            } else if (actionForm.getAccountFormBean().getUsGovernmentAcount().contains(GRTConstants.ACCOUNT_SI)) {
	                customerGroupType = CustomerGroupType.si;
	                expectedDeilvery += " *Please validate SI account.";
	            } else if (actionForm.getAccountFormBean().getUsGovernmentAcount().contains(GRTConstants.ACCOUNT_SP)) {
	                customerGroupType = CustomerGroupType.sp;
	                expectedDeilvery += " *Please validate SP account.";
	            } else {
	                customerGroupType = CustomerGroupType.others;
	            }
	            soldToCreateRequestType.setCustomerGroup(customerGroupType);
	        }
	        logger.debug("DescribeDeliverables  : " + expectedDeilvery);
	        soldToCreateRequestType.setDescribeDeliverables(expectedDeilvery);
	        
	        logger.debug("soldToCreateRequestType.getGovermentFlag(): "+actionForm.getAccountFormBean().getUsGovernmentAcount()
	                + "soldToCreateRequestType"+soldToCreateRequestType.getCustomerGroup().toString());
	        logger.debug("actionForm.getAccountFormBean().getAccountCreationReason():" + actionForm.getAccountFormBean().getAccountCreationReason());
	        CSSPortalUser user = this.getUserFromSession();
	        String loginName = null;
	        String loginEmail = null;
	        if(user != null) {
	        	loginName= user.getFirstName() + ", " + user.getLastName();
	        	loginEmail = user.getEmailAddress();
	        }
	        soldToCreateResponseType = getAccountService().sapSoldToAddressCall(soldToCreateRequestType, actionForm.getAccountFormBean().getAccountCreationReason(), actionForm.getAccountFormBean().getBplinkId(), actionForm.getAccountFormBean().getBpName(), loginName, loginEmail);
	        if(soldToCreateResponseType != null){
	        	try{
	        		getAccountService().saveAccountCreation(contructSoldToAccountCreation(soldToCreateRequestType, soldToCreateResponseType, loginName));
	        	}catch (Throwable throwable)
	            {
	                logger.error("Exception Occured " + throwable.getMessage(), throwable);
	            }
	        }
	        return soldToCreateResponseType;
	        
	    }
	
	 /**
	 * construct AccountCreation Object
	 * 
	 * @param soldToCreateRequestType
	 * @param soldToCreateResponseType
	 * @param userName
	 * @return
	 */
	private AccountCreation contructSoldToAccountCreation(SoldToCreateRequestType soldToCreateRequestType, 
	    		SoldToCreateResponseType soldToCreateResponseType, String userName){
	    	AccountCreation accountCreation = new AccountCreation();
	    	if(soldToCreateRequestType != null){
	    		accountCreation.setUserName(GRTUtil.trimPropertytoMaxLength(userName, 255));
	    		accountCreation.setCreatedDate(new Date());
	    		accountCreation.setAccountType(GRTConstants.ACCOUNT_TYPE_SOLDTO);
	    		accountCreation.setAccountName(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToName1(), 255));
	    		accountCreation.setCity(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getCity(), 255));
	    		accountCreation.setStreet(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getStreet1(), 255));
	    		accountCreation.setZip(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getZip(), 255));
	    		accountCreation.setRegion(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getRegion(), 255));
	    		accountCreation.setStateFull(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getState_Full(), 255));
	    		accountCreation.setDistributionChannel(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getDistributionChannelDefault(), 255));
	    		accountCreation.setDivision(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getDivisionDefault(), 255));
	    		accountCreation.setCountry(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSoldToAddress().getCountry(), 255));
	    		accountCreation.setCountryThreeChar(soldToCreateRequestType.getSoldToAddress().getCountry3Char());
	    		accountCreation.setFirstName(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getContactFirstName(), 255));
	    		accountCreation.setLastName(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getContactLastName(), 255));
	    		accountCreation.setPhone(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getPhone(), 255));
	    		accountCreation.setEmail(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getEmail(), 255));
	    		accountCreation.setSalesOrg(GRTUtil.trimPropertytoMaxLength(soldToCreateRequestType.getSalesOrg(), 255));
	    		accountCreation.setTokenNumber(soldToCreateRequestType.getTokenNumber());
	    		accountCreation.setDuplicateAccountReason(GRTUtil.trimPropertytoMaxLength(actionForm.getAccountFormBean().getAccountCreationReason(), 255));
	    		accountCreation.setBpLinkId(actionForm.getAccountFormBean().getBplinkId());
	    		accountCreation.setBpName(GRTUtil.trimPropertytoMaxLength(actionForm.getAccountFormBean().getBpName(), 255));
	    		accountCreation.setResponseCode(soldToCreateResponseType.getCode());
	    		accountCreation.setResponseDescription(GRTUtil.trimPropertytoMaxLength(soldToCreateResponseType.getDescription(), 255));
	    		accountCreation.setShipToId(soldToCreateResponseType.getSoldTo());
	    		accountCreation.setAccountId(soldToCreateResponseType.getAccountId());
	    	}
	    	return accountCreation;
	}
	
	/**
	 * set account cration error message.
	 * 
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	private String backToAccountCreationErrorPage(String errMsg) throws Exception {
			if (errMsg.contains(GRTConstants.JURISDICTION)) {
				this.getRequest().setAttribute("jurisdictionError", "1");
				return nextAccountCreation();
			}
			this.getRequest().setAttribute("accountCreationError", errMsg);
			return Action.SUCCESS;
	}
	
	 /**
	 * account creation service call
	 * 
	 * @return
	 * @throws Throwable
	 */
	private ShipToCreateResponseType sapShipToAddressCall() throws Throwable {
	    	logger.debug("AccountFormBean:" + actionForm.getAccountFormBean());
	        logger.debug("RegionId in ShipToCreateResponseType : " + actionForm.getAccountFormBean().getRegionId());
	        logger.debug("RegionValue in ShipToCreateResponseType : " + actionForm.getAccountFormBean().getRegionValue());
	        
	        ShipToCreateRequestType shipToCreateRequestType = new ShipToCreateRequestType();
	        ShipToCreateResponseType shipToCreateResponseType = new ShipToCreateResponseType();
	        
	        logger.debug("salesOrg :"+ actionForm.getAccountFormBean().getSalesOrg());
	        if (actionForm.getAccountFormBean().getSalesOrg() != null) {
	            shipToCreateRequestType.setSalesOrg(actionForm.getAccountFormBean().getSalesOrg());
	        } 
	        if (actionForm.getAccountFormBean().getSoldToId() != null) {
	            shipToCreateRequestType.setDistiSoldTo(actionForm.getAccountFormBean().getSoldToId());
	        }
	        shipToCreateRequestType.setShipToName1(actionForm.getAccountFormBean().getEndCustomerName().trim());
	        logger.debug("actionForm.getAccountFormBean().getEndCustomerName(): "+actionForm.getAccountFormBean().getEndCustomerName());
	        AddressType addressType = new AddressType();
	        addressType.setStreet1(actionForm.getAccountFormBean().getEndCustomerStreetName().trim());
	        addressType.setCity(actionForm.getAccountFormBean().getEndCustomerCity().trim());
	        if (actionForm.getAccountFormBean().getRegionId() != null) {
	            logger.debug("actionForm.getAccountFormBean().getRegionId()"+actionForm.getAccountFormBean().getRegionId().trim());
	            addressType.setRegion(actionForm.getAccountFormBean().getRegionId().trim());
	        }
	        addressType.setCountry(actionForm.getAccountFormBean().getEndCustomerCountry().trim());
	        addressType.setZip(actionForm.getAccountFormBean().getEndCustomerPostalCode().trim());
	        

	        if (StringUtils.isNotEmpty(actionForm.getAccountFormBean().getSiebelRegionValue()))
	        {
	            addressType.setState_Full(actionForm.getAccountFormBean().getSiebelRegionValue().trim());
	        }
	        if (actionForm.getAccountFormBean().getCountry3Char() != null) {
	            addressType.setCountry3Char(actionForm.getAccountFormBean().getCountry3Char().trim());
	        }
	        logger.debug("actionForm.getAccountFormBean().getEndCustomerPostalCode(): "+actionForm.getAccountFormBean().getEndCustomerPostalCode());
	        shipToCreateRequestType.setShipToAddress(addressType);
	        logger.debug("actionForm.getAccountFormBean().getSoldToId(): "+actionForm.getAccountFormBean().getSoldToId());        
	        shipToCreateRequestType.setContactFirstName(actionForm.getAccountFormBean().getContactFirstName().trim());
	        shipToCreateRequestType.setContactLastName(actionForm.getAccountFormBean().getContactLastName().trim());
	        shipToCreateRequestType.setContactEmail(actionForm.getAccountFormBean().getContactEmail().trim());
	        shipToCreateRequestType.setContactPhone(actionForm.getAccountFormBean().getContactPhone().trim());
	        logger.debug("actionForm.getAccountFormBean().getContactPhone(): "+actionForm.getAccountFormBean().getContactPhone());
	        shipToCreateRequestType.setEmail(actionForm.getAccountFormBean().getContactEmail().trim());
	        shipToCreateRequestType.setPhone(actionForm.getAccountFormBean().getContactPhone().trim());
	        
	        if (actionForm.getAccountFormBean().getDistributionChannel() != null) {
	        	logger.debug("actionForm.getAccountFormBean().getDistributionChannel() from token response: "+actionForm.getAccountFormBean().getDistributionChannel());
	        	shipToCreateRequestType.setDistributionChannelDefault(actionForm.getAccountFormBean().getDistributionChannel());
	        } else {
	        	logger.debug("Default actionForm.getAccountFormBean().getDistributionChannel(): 02");
	        	shipToCreateRequestType.setDistributionChannelDefault("02");
	        }
	        
	        if (actionForm.getAccountFormBean().getDivision() != null) {
	        	logger.debug("actionForm.getAccountFormBean().getDivision() from token response: "+actionForm.getAccountFormBean().getDivision());
	        	shipToCreateRequestType.setDivisionDefault(actionForm.getAccountFormBean().getDivision());
	        } else {
	        	logger.debug("Default actionForm.getAccountFormBean().getDivision(): 01");
	        	shipToCreateRequestType.setDivisionDefault("01");
	        }
	                
	        if (actionForm.getAccountFormBean().getVatNumber() != null) {
	            shipToCreateRequestType.setVATNumber(actionForm.getAccountFormBean().getVatNumber());
	        }
	        
	        if (actionForm.getAccountFormBean().getTokenNumber() != null) {
	            shipToCreateRequestType.setTokenNumber(actionForm.getAccountFormBean().getTokenNumber());
	        }
	        logger.debug("actionForm.getAccountFormBean().getContactPhone(): "+actionForm.getAccountFormBean().getContactPhone());
	        logger.debug("actionForm.getAccountFormBean().getAccountCreationReason():" + actionForm.getAccountFormBean().getAccountCreationReason());
	        CSSPortalUser user = this.getUserFromSession();
	        String loginName = null;
	        String loginEmail = null;
	        if(user != null) {
	        	loginName= user.getFirstName() + ", " + user.getLastName();
	        	loginEmail = user.getEmailAddress();
	        }
	        shipToCreateResponseType = getAccountService().sapShipToAddressCall(shipToCreateRequestType, actionForm.getAccountFormBean().getAccountCreationReason(), actionForm.getAccountFormBean().getBplinkId(), actionForm.getAccountFormBean().getBpName(), loginName, loginEmail);
	        if(shipToCreateResponseType != null){
	        	try{
	        		getAccountService().saveAccountCreation(contructShipToAccountCreation(shipToCreateRequestType, shipToCreateResponseType, loginName));
	        	}catch (Throwable throwable)
	            {
	                logger.error("Exception Occured " + throwable.getMessage(), throwable);
	            }
	        }
	        return shipToCreateResponseType;        
	}
	    
	 private AccountCreation contructShipToAccountCreation(ShipToCreateRequestType shipToCreateRequestType, 
	    		ShipToCreateResponseType shipToCreateResponseType, String userName){
	    	AccountCreation accountCreation = new AccountCreation();
	    	if(shipToCreateRequestType != null){
	    		accountCreation.setUserName(GRTUtil.trimPropertytoMaxLength(userName, 255));
	    		accountCreation.setCreatedDate(new Date());
	    		accountCreation.setAccountType(GRTConstants.ACCOUNT_TYPE_SHIPTO);
	    		accountCreation.setDistiSoldToId(shipToCreateRequestType.getDistiSoldTo());
	    		accountCreation.setAccountName(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToName1(), 255));
	    		accountCreation.setCity(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getCity(), 255));
	    		accountCreation.setStreet(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getStreet1(), 255));
	    		accountCreation.setZip(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getZip(), 255));
	    		accountCreation.setRegion(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getRegion(), 255));
	    		accountCreation.setStateFull(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getState_Full(), 255));
	    		accountCreation.setDistributionChannel(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getDistributionChannelDefault(), 255));
	    		accountCreation.setDivision(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getDivisionDefault(), 255));
	    		accountCreation.setCountry(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getShipToAddress().getCountry(), 255));
	    		accountCreation.setCountryThreeChar(shipToCreateRequestType.getShipToAddress().getCountry3Char());
	    		accountCreation.setFirstName(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getContactFirstName(), 255));
	    		accountCreation.setLastName(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getContactLastName(), 255));
	    		accountCreation.setPhone(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getPhone(), 255));
	    		accountCreation.setEmail(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getEmail(), 255));
	    		accountCreation.setVatNumber(shipToCreateRequestType.getVATNumber());
	    		accountCreation.setSalesOrg(GRTUtil.trimPropertytoMaxLength(shipToCreateRequestType.getSalesOrg(), 255));
	    		accountCreation.setTokenNumber(shipToCreateRequestType.getTokenNumber());
	    		accountCreation.setDuplicateAccountReason(GRTUtil.trimPropertytoMaxLength(actionForm.getAccountFormBean().getAccountCreationReason(), 255));
	    		accountCreation.setBpLinkId(actionForm.getAccountFormBean().getBplinkId());
	    		accountCreation.setBpName(GRTUtil.trimPropertytoMaxLength(actionForm.getAccountFormBean().getBpName(), 255));
	    		accountCreation.setResponseCode(shipToCreateResponseType.getCode());
	    		accountCreation.setResponseDescription(GRTUtil.trimPropertytoMaxLength(shipToCreateResponseType.getDescription(), 255));
	    		accountCreation.setShipToId(shipToCreateResponseType.getShipTo());
	    		accountCreation.setAccountId(shipToCreateResponseType.getAccountId());
	    	}
	    	return accountCreation;
	}
	 
	public String sendEmailConfirmation() throws Throwable {
			logger.debug("Starting sendEmailConfirmation...");
			validateSession();
			String soldToOrShipToId = null;
			String endCustomerName = actionForm.getAccountFormBean().getEndCustomerName();
			String endCustomerStreetName = actionForm.getAccountFormBean().getEndCustomerStreetName();
			String endCustomerCity = actionForm.getAccountFormBean().getEndCustomerCity();
			String endCustomerState = actionForm.getAccountFormBean().getRegionValue();
			String endCustomerPostalCode = actionForm.getAccountFormBean().getEndCustomerPostalCode();
			String endCustomerCountry = actionForm.getAccountFormBean().getEndCustomerCountry();
			String destination = actionForm.getAccountFormBean().getSapBox();
			String response = null;
	
			if (GRTConstants.BBoxDestination.equalsIgnoreCase(destination) && StringUtils.isNotEmpty(actionForm.getAccountFormBean().getSoldToId()))  {
				soldToOrShipToId = actionForm.getAccountFormBean().getSoldToId();
			} else if (GRTConstants.IBoxDestination.equalsIgnoreCase(destination) && StringUtils.isNotEmpty(actionForm.getAccountFormBean().getShipToId()))  {
				soldToOrShipToId = actionForm.getAccountFormBean().getShipToId();
			} else {
				soldToOrShipToId = actionForm.getAccountFormBean().getAccountIdForUpdate();
			}
			String bpLinkId = actionForm.getAccountFormBean().getBplinkId();
			String email = actionForm.getAccountFormBean().getEmailNotification();
			response = getAccountService().mailAccountCreationDetails(soldToOrShipToId, endCustomerName, endCustomerStreetName, 
					endCustomerCity, endCustomerState, endCustomerPostalCode, endCustomerCountry, bpLinkId, email);
			actionForm.getAccountFormBean().setEmailNotificationStatus(response);
			if (response.equals(GRTConstants.YES)) {
				actionForm.getAccountFormBean().setResponseDescription(grtConfig.getAccCreateEmailNotifMsg());
			} else {
				actionForm.getAccountFormBean().setResponseDescription(grtConfig.getAccCrtEmailNotiErrMsg());				
				actionForm.getAccountFormBean().setResponseCode(GRTConstants.exception_errorcode);
			}
			logger.debug("Ending sendEmailConfirmation.");
			return Action.SUCCESS;
	}
	
	/**
	 * populate form flags
	 * 
	 * @return
	 */
	public String tokenBackButton() {
		try {
			validateSession();
			
			actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.BLOCK);
			actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
			logger.debug("In AccountController.tokenBackButton() account bean ");
			actionForm.getAccountFormBean().setCountryList(PageCache.countrySelectMap);
			logger.debug("AccountController.tokenBackButton() country list: "
					+ actionForm.getAccountFormBean().getCountryList());

		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting accountCreation");
		}

		return Action.SUCCESS;
	}
	    
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
		return null;
	}
	
	/**
	 * validate ship to id.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validateShipToId() throws Exception {
 		validateSession();
 		logger.debug("In validateShipToId: actionForm.getAccountFormBean().getSoldToId():"+actionForm.getAccountFormBean().getSoldToId());
 		Account account = getAccountService().queryAccount(actionForm.getAccountFormBean().getSoldToId());
        if(account != null &&  StringUtils.isNotEmpty(account.getType()) && !GRTConstants.SOLD_TO_ACCOUNT_TYPE.equalsIgnoreCase(account.getType())) { 
        	return backToShipToIdErrorPage(grtConfig.getAccCrtInvalidSoldToErrMsg().replace("<SOLDTOID>", actionForm.getAccountFormBean().getSoldToId()) );        	
        }
        CSSPortalUser user = getUserFromSession();
        boolean isBP = false;
        if(user != null && StringUtils.isNotEmpty(user.getUserType()) && user.getUserType().equalsIgnoreCase(GRTConstants.USER_TYPE_BP)) {
        	isBP = true;
        }
        if (account != null) {
        	logger.debug("In validateShipToId: account.getName():"+account.getName());
        }
        
        if(isBP) {
        	if (account == null) {
				return backToShipToIdErrorPage(GRTConstants.INVALID_SOLD_TO);
            }
        	
            if (!this.getInstallBaseService().isSoldToValidForCurrentUser(actionForm.getAccountFormBean().getSoldToId(),
                user.getUserId(), actionForm.getAccountFormBean().getBplinkId())) {
				return backToShipToIdErrorPage(GRTConstants.BP_NOT_AUTHORIZED_FOR_SOLD_TO);
            }  
        }
        else {
        	if (account == null) {		                
        		return backToShipToIdErrorPage(grtConfig.getAccCrtSoldToNotExistsErrMsg().replace("<SOLDTO>", actionForm.getAccountFormBean().getSoldToId()));
            }		            
        }
        logger.debug("account.getPrimaryAccountCountry(): "+account.getPrimaryAccountCountry()+" , account.getCountryCode(): "+account.getCountryCode()
        		+ " , account.getCountryForDisplay(): "+account.getCountryForDisplay());
        
        
        List<Country> countryList = PageCache.countryMap;
        String countryCode = account.getPrimaryAccountCountry();
        for (Country country : countryList) {
        	if(country.getName().equalsIgnoreCase(countryCode)) {
        		countryCode =   country.getCode();
        	}
        }
        if (countryCode.equalsIgnoreCase(GRTConstants.COUNTRY_US)) {
        	return backToShipToIdErrorPage(grtConfig.getAccCrtInvalidSoldToNonUSErrMsg().replace("<SOLDTOID>", actionForm.getAccountFormBean().getSoldToId()));
		} 
        
        String salesOrg = getAccountService().getSalesOrg(countryCode);		
        logger.debug("SalesOrg : "+salesOrg+" for country: "+countryCode);
        actionForm.getAccountFormBean().setSalesOrg(salesOrg);
        actionForm.getAccountFormBean().setSoldToIdSubmissionFlag(actionForm.getAccountFormBean().getSoldToId());
        return backToShipToIdMsgPage(grtConfig.getAccCrtSoldToValidatedMsg().replace("<SOLDTOID>", actionForm.getAccountFormBean().getSoldToId()));
    }
	
	/**
	 * set ship to message.
	 * 
	 * @param msg
	 * @return
	 */
	private String backToShipToIdMsgPage(String msg) {
		this.getRequest().setAttribute("shipToMsg", msg);
		return Action.SUCCESS;
	}
	
	/**
	 * set ship to error message.
	 * 
	 * @param errMsg
	 * @return
	 */
	private String backToShipToIdErrorPage(String errMsg) {
		this.getRequest().setAttribute("shipToError", errMsg);
		return Action.SUCCESS;
	}
	
	/**
	 * set form flags for show sold to.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showSoldToId() throws Exception {
		validateSession();
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
			actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
		} else {
			actionForm.getAccountFormBean().setAcountCreationFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForBP(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipToCreationFlagForNonBP(GRTConstants.BLOCK);
			actionForm.getAccountFormBean().setTokenNumberFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setAccountAddressFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setFuzzySearchFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setShipTosoldToFlag(GRTConstants.NONE);
			actionForm.getAccountFormBean().setConfirmationFlag(GRTConstants.NONE);
		}
		return Action.SUCCESS;
	}
	
}
