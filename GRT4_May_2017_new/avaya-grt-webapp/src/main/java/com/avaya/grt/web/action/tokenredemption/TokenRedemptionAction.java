package com.avaya.grt.web.action.tokenredemption;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.FuzzySearchParam;
import com.avaya.grt.mappers.LogServiceDetails;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.mappers.TokenRedemption;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.avaya.grt.web.util.PageCache;
import com.avaya.v1.address.AddressType;
import com.avaya.v1.addresssearch.AddressSearchRequestType;
import com.avaya.v1.addresssearch.Request;
import com.avaya.v1.tokenredemption.TokenRedemptionRequestType;
import com.google.gson.Gson;
import com.grt.dto.Account;
import com.grt.dto.BusinessPartner;
import com.grt.dto.Contract;
import com.grt.dto.Result;
import com.grt.dto.ServiceDetails;
import com.grt.dto.TokenFormBean;
import com.grt.dto.TokenRedemptionResponse;
import com.grt.dto.TokenResponseTypeDto;
//import com.grt.dto.Contact;
import com.grt.util.GRTConstants;
import com.grt.util.GRTUtil;
import com.grt.util.MailUtil;
import com.grt.util.MedalLevelEnum;
//import com.avaya.grt.service.account.AccountServiceImpl;
//import com.avaya.grt.service.account.AccountService;
import com.opensymphony.xwork2.Action;

public class TokenRedemptionAction extends TechnicalRegistrationAction {

	private static final Logger logger = Logger.getLogger(TokenRedemptionAction.class);
	//private AccountService accountService;
	//private TokenFormBean tokenForm;
	private static final String DATE_FORMAT1 = "dd-MMM-yyyy";
	//private static final String DATE_FORMAT = "MM/dd/yyyy";

	private MailUtil mailUtil;
	
	public MailUtil getMailUtil() {
		return mailUtil;
	}

	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}
	
	public String tokenRedemption() throws Exception {
		validateSession();
		//this.tokenForm = new TokenFormBean();
		//form = this.tokenForm;
		if (!getUserFromSession().getBpLinkId().equalsIgnoreCase("na"))
		{
			actionForm.getTokenFormBean().setBpLinkID(getUserFromSession().getBpLinkId());
		}
		actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
		actionForm.getTokenFormBean().setBplinkdetailsFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setContractDateFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setSearchDataFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setSearchFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setTokenRedemptionFinalFlag(GRTConstants.NONE);
		actionForm.getTokenFormBean().setFinalScreenFlag(GRTConstants.NONE);
		getRequest().getSession().setAttribute("sapbox", null);
		return  Action.SUCCESS;
	}


	public String validateToken() throws Exception
	{
		long start = System.currentTimeMillis();
		validateSession();
		CSSPortalUser user = getUserFromSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
		String tokenNumber = actionForm.getTokenFormBean().getTokenNumber();
		TokenResponseTypeDto tokenResponse = null;
		actionForm.getTokenFormBean().setSelectedBp(null);
		try {
			actionForm.getTokenFormBean().setRegionList(getAccountService().fetchRegions(GRTConstants.OEFC_TOKEN_REDEMPTION));
		}  catch (Exception e) {
			e.printStackTrace();
		}
		if (!GRTConstants.BBoxDestination.equals(getUserFromSession().getUserType()))
		{
			actionForm.getTokenFormBean().setBpLinkID(null);
		}
		try
		{
			tokenResponse = getAccountService().tokenVerification(actionForm.getTokenFormBean().getTokenNumber());
			//tokenResponse.setCode(GRTConstants.TOKEN_INVALID_CODE);
			logger.debug(tokenResponse.getStatus());
			if (GRTConstants.TOKEN_INVALID_CODE.equals(tokenResponse.getCode()))
			{	String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenIvalidError");
				String errorMessage = grtConfig.getTokenIvalidError();
				errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
				actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
				//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, "Invalid Token : "+ tokenNumber);
			}
			else if (GRTConstants.TOKEN_SUCESS_CODE.equals(tokenResponse.getCode()))
			{
				String tokenStatus = tokenResponse.getTokendata()[0].getStatus();
				if (GRTConstants.TOKEN_EXPIRED.equals(tokenStatus))
				{
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenExpiredError");
					String errorMessage = grtConfig.getTokenExpiredError();
					errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
					errorMessage = errorCode + "###" + errorMessage;
					getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
					//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, "Expired Token :  "+ tokenNumber);
					actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
				}
				else if (GRTConstants.TOKEN_REDEEMED.equals(tokenStatus))
				{
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenRedeemedError");
					String errorMessage = grtConfig.getTokenRedeemedError();
					errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
					errorMessage = errorCode + "###" + errorMessage;
					getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
					//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, "Already Redeemed  Token :  "+ tokenNumber);
					actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
				}
				else if (GRTConstants.TOKEN_ACTIVE.equals(tokenStatus))
				{
					String distiSoldToId = null;
					try {
						distiSoldToId = getValidId(tokenResponse.getDistiSoldTo());
						Account account = getAccountService().getAccountInfo(distiSoldToId);
						if(account != null) {
							logger.debug("Disti account found in CXP for[" + distiSoldToId +"]");
						} else {
							logger.debug("Disti account NOT found in CXP for[" + distiSoldToId +"], Trying in Siebel");
							account = getAccountService().queryAccount(distiSoldToId);
							if(account != null) {
								logger.debug("Disti account found in Siebel for[" + distiSoldToId +"]");
							}
						}
						if (account != null) {
							actionForm.getTokenFormBean().setDistributorAccount(account);
							actionForm.getTokenFormBean().setDistiSoldToName(account.getName());
						}
					}
					catch (Throwable throwable) {
						logger.error("[" + distiSoldToId + "] Not found", throwable);
						actionForm.getTokenFormBean().setDistiSoldToName("Account Not Found:" + tokenResponse.getDistiSoldTo());
					}
					actionForm.getTokenFormBean().setTokenDetails(tokenResponse);
					actionForm.getTokenFormBean().setServiceTerm(tokenResponse.getServiceTerm()+ " Months");
					actionForm.getTokenFormBean().setTokenServiceDetails(Arrays.asList(tokenResponse.getTokendata()[0].getServiceDetails()));
					actionForm.getTokenFormBean().setBplinkidRequired(true);                  
					if (user.getUserType().equals(GRTConstants.USER_TYPE_BP) && !validateMedalLevelForBP(actionForm.getTokenFormBean()))
					{
						BusinessPartner partner =  getInstallBaseService().getBusinessPartner(user.getBpLinkId());
						String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenUnauthorizeToRedeemError");
						String errorMessage = grtConfig.getTokenUnauthorizeToRedeemError();
						errorMessage = errorMessage.replace("<SOLDTONAME>", partner.getSoldToName());
						errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
						errorMessage = errorCode + "###" + errorMessage;
						getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
						//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, partner.getSoldToName()+ " is not authorized to redeem "  + tokenNumber);
						actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
					}
					else
					{
						actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
					}

				}
				else
				{
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenIvalidError");
					String errorMessage = grtConfig.getTokenIvalidError();
					errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
					errorMessage = errorCode + "###" + errorMessage;
					getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
					//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, "Invalid  Token :  "+ tokenNumber);
					actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("", e);
			String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenValidatingError");
			String errorMessage = grtConfig.getTokenValidatingError();
			errorMessage = errorMessage.replace("<TOKEN>", tokenNumber);
			//errorMessage = errorMessage.replace("<ERRORMESSAGE>", e.toString());
			errorMessage = errorCode + "###" + errorMessage;
			getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, errorMessage);
			//getRequest().setAttribute(GRTConstants.TOKEN_SEARCH_ERROR, "Error occured while validating token");
			actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
		}

		long end = System.currentTimeMillis();
        System.out.println("time for validate token "+(end-start));
        
		return Action.SUCCESS;
	}
	
	private String jsonContractString;
	
	public String getJsonContractString() {
		return jsonContractString;
	}

	public void setJsonContractString(String jsonContractString) {
		this.jsonContractString = jsonContractString;
	}
	
	public void findContracts() throws Exception {
		
		String shipTo = actionForm.getTokenFormBean().getShipTo();
		CSSPortalUser cssUser = getUserFromSession();
		List<Contract> contracts = getAccountService().findContracts(shipTo.trim(), cssUser.isSuperUser());
		if(contracts != null && contracts.get(0) != null && StringUtils.isNotEmpty(contracts.get(0).getReturnCode())) {
			logger.debug("Return Code:"+contracts.get(0).getReturnCode());
		} else {
			CollectionUtils.filter(contracts, new Predicate(){
				public boolean evaluate(Object in) {
					return StringUtils.isNotEmpty(((Contract)in).getContractType());
				}
			});
			final TokenResponseTypeDto token = actionForm.getTokenFormBean().getTokenDetails();
			if(token!=null)
				CollectionUtils.forAllDo(contracts, new Closure() {
					public void execute(Object in) {
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
						Contract contract = ((Contract)in);
						if(contract!= null && contract.getContractStartDate() != null){
							contract.setContractStartDateStr(sdf.format(contract.getContractStartDate()));
						}
						if(contract!= null && contract.getContractEndDate() != null){
							contract.setContractEndDateStr(sdf.format(contract.getContractEndDate()));
						}
						if(token.isCodelivary()) {
							contract.setAllowReplace(contract.isCodelivary()?true:false);
						} else if(token.isWholesale()) {
							// treat it  as wholesale
							contract.setAllowReplace(true);
						}
					}
				});
		}
		logger.debug("Contracts List Size - "+contracts.size());
		logger.debug("Contracts List - "+contracts);
		if(actionForm.getTokenFormBean()!=null)
			actionForm.getTokenFormBean().setContracts(contracts);
		
		Gson gson = new Gson();
		jsonContractString = gson.toJson(contracts);
		logger.debug("Contracts String : "+jsonContractString);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.getWriter().write(jsonContractString);

		//return contracts;
	}
	
	public String begin()
	{

		if (!getUserFromSession().getBpLinkId().equalsIgnoreCase("na"))
		{
			actionForm.getTokenFormBean().setBpLinkID(getUserFromSession().getBpLinkId());
		}

		setAllFlagNone(actionForm.getTokenFormBean());
		setSearchCriterialNull(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setTokenNumberFlag(GRTConstants.BLOCK);
		getRequest().getSession().setAttribute("sapbox", null);
		return Action.SUCCESS;
	}

	public String validateBpLinkID() throws Exception
	{   
		validateSession();
		actionForm.getTokenFormBean().setBpName(null);
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setSelectedAccount(null);
		actionForm.getTokenFormBean().setSelectedBp(null);
		CSSPortalUser user=getUserFromSession();
		if (actionForm.getTokenFormBean().isBplinkidRequired()
				&& (StringUtils.isEmpty(actionForm.getTokenFormBean().getBpLinkID()) || actionForm.getTokenFormBean().getBpLinkID().trim().length() == 0))
		{
			actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
			
			String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenBPLinkReqError");
			String errorMessage = grtConfig.getTokenBPLinkReqError();
			errorMessage = errorCode + "###" + errorMessage;
			getRequest().setAttribute("bplinkerror", errorMessage);
			//getRequest().setAttribute("bplinkerror", "BPLink ID Required");
			return Action.SUCCESS;
		}
		else if (!actionForm.getTokenFormBean().isBplinkidRequired()
				&& (actionForm.getTokenFormBean().getBpLinkID() == null || actionForm.getTokenFormBean().getBpLinkID().trim().length() == 0))
		{
			actionForm.getTokenFormBean().setBpLinkID(null);
			actionForm.getTokenFormBean().setSelectedBp(null);
			return showSoldToId();
		}
		else
		{
			try
			{
				// if co-delivary and bp does n't have permission show error .
            	TokenResponseTypeDto token =actionForm.getTokenFormBean().getTokenDetails();
            	
				if(token.isCodelivary() && (user != null && !user.isSuperUser())){
					if(!getAccountService().isBPAuthorizedToRedeem(actionForm.getTokenFormBean().getBpLinkID(), GRTConstants.SME_COMMUNICATIONS)) {
						actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
						String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenBPNotAuthorisedToRedeemErrMsg");
						String errorMessage = grtConfig.getTokenBPNotAuthorisedToRedeemErrMsg();
						errorMessage = errorMessage.replace("<BPLINKID>", actionForm.getTokenFormBean().getBpLinkID());
						errorMessage = errorCode + "###" + errorMessage;
						getRequest().setAttribute("bplinkerror", errorMessage);
						//getRequest().setAttribute("bplinkerror", "BP Link: "+actionForm.getTokenFormBean().getBpLinkID()+" is not authorized to redeemed this token");
						return Action.SUCCESS;
					}
            	}
				List<BusinessPartner> bs = getAccountService().getBusinessPartners(
						actionForm.getTokenFormBean().getBpLinkID());
				if (bs != null)
				{
					logger.debug("No OF attaced account found : "
							+ bs.size());
				}
				if (bs != null
						&& bs.size() == 1)
				{
					actionForm.getTokenFormBean().setSelectedAccount(bs.get(0).getSoldToId());
					actionForm.getTokenFormBean().setAttachedBp(bs);
					return "showShipToSoldTo";
				}
				else if (bs != null
						&& bs.size() > 0)
				{
					actionForm.getTokenFormBean().setBplinkdetailsFlag(GRTConstants.BLOCK);
					actionForm.getTokenFormBean().setAttachedBp(bs);
				}
				else
				{
					actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenBPLinkInvalidError");
					String errorMessage = grtConfig.getTokenBPLinkInvalidError();
					errorMessage = errorCode + "###" + errorMessage;
					getRequest().setAttribute("bplinkerror", errorMessage);
					//getRequest().setAttribute("bplinkerror", "Invalid BPLink ID");
				}

			}
			catch (Exception exception)
			{
				logger.debug("Exception in getting associate bp", exception);
				actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
				
				String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenBPLinkValidatingExcp");
				String errorMessage = grtConfig.getTokenBPLinkValidatingExcp();
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute("bplinkerror", errorMessage);
				//getRequest().setAttribute("bplinkerror", "Error in validating BPLink ID");
			}
		}

		return Action.SUCCESS;
	}

	public String showSoldToId() throws Exception
	{
		validateSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setBpName(null);
		CSSPortalUser user = getUserFromSession();



		if (actionForm.getTokenFormBean().getSelectedAccount() != null)
		{
			actionForm.getTokenFormBean().setSelectedBp(getBusinessPartner(actionForm.getTokenFormBean().getAttachedBp(), actionForm.getTokenFormBean().getSelectedAccount()));
		}
		if (StringUtils.isNotEmpty(actionForm.getTokenFormBean().getBpLinkID()))
		{
			BusinessPartner businessPartner = getAccountService().getBusinessPartner(actionForm.getTokenFormBean().getBpLinkID());
			if (businessPartner != null)
			{
				actionForm.getTokenFormBean().setBpName(businessPartner.getSoldToName() + "-" + actionForm.getTokenFormBean().getBpLinkID());
			}
		}
		else if (actionForm.getTokenFormBean().getSelectedBp() == null && StringUtils.isNotEmpty(actionForm.getTokenFormBean().getTokenDetails().getDistiBPLinkId()))
		{
			logger.debug("fetching information from siebel for DistiBPLinkId " +actionForm.getTokenFormBean().getTokenDetails().getDistiBPLinkId());
			BusinessPartner businessPartner = getAccountService().getBusinessPartner(actionForm.getTokenFormBean().getTokenDetails().getDistiBPLinkId());
			actionForm.getTokenFormBean().setSelectedBp(businessPartner);
			logger.debug("bussiness partner = "+businessPartner);
			if (businessPartner != null)
			{
				actionForm.getTokenFormBean().setBpName(businessPartner.getSoldToName() + "-" + actionForm.getTokenFormBean().getTokenDetails().getDistiBPLinkId());
				actionForm.getTokenFormBean().setBpLinkID(actionForm.getTokenFormBean().getTokenDetails().getDistiBPLinkId());
			}
		}
		else if (actionForm.getTokenFormBean().getSelectedBp() == null  && GRTConstants.USER_TYPE_BP.equals(user.getUserType()))
		{
			logger.debug("fetching information from siebel for bplinkID " +user.getBpLinkId());
			BusinessPartner businessPartner = getAccountService().getBusinessPartner(user.getBpLinkId());
			actionForm.getTokenFormBean().setSelectedBp(businessPartner);
			logger.debug("bussiness partner = "+businessPartner);
			if (businessPartner != null)
			{
				actionForm.getTokenFormBean().setBpName(businessPartner.getSoldToName() + "-" + user.getBpLinkId());
				actionForm.getTokenFormBean().setBpLinkID(user.getBpLinkId());
			}
		}

		if (user.getUserType().equals(GRTConstants.AGENT) && !validateMedalLevelForAssociate(actionForm.getTokenFormBean()))
		{
			actionForm.getTokenFormBean().setBplinkdetailsFlag(GRTConstants.BLOCK);
			
			String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenUnauthorizeToRedeemError");
			String errorMessage = grtConfig.getTokenUnauthorizeToRedeemError();
			errorMessage = errorMessage.replace("<SOLDTONAME>", actionForm.getTokenFormBean().getSelectedBp().getSoldToName());
			errorMessage = errorMessage.replace("<TOKEN>", actionForm.getTokenFormBean().getTokenNumber());
			errorMessage = errorCode + "###" + errorMessage;
			getRequest().setAttribute("medallevelerror", errorMessage);
			//getRequest().setAttribute("medallevelerror",actionForm.getTokenFormBean().getSelectedBp().getSoldToName() +" is not authorized to redeem " + actionForm.getTokenFormBean().getTokenNumber());
			return Action.SUCCESS;
		}

		String endCoustomerAccount = null;
		if (GRTConstants.B_BOX_SALES_ORG.equals(actionForm.getTokenFormBean().getTokenDetails().getRequesterSalesOrg()))
		{
			endCoustomerAccount = actionForm.getTokenFormBean().getTokenDetails().getTokendata()[0].getEndCustomerSoldTo();
		}
		else
		{
			endCoustomerAccount = actionForm.getTokenFormBean().getTokenDetails().getTokendata()[0].getEndCustomerShipTo();
		}
		logger.debug("endCoustomerAccount : "
				+ endCoustomerAccount);
		if (GRTConstants.USER_TYPE_BP.equals(user.getUserType()))
		{
			if (checkNonEmpty(endCoustomerAccount))
			{
				Account account = getAccountService().queryAccount(getValidId(endCoustomerAccount));
				if (account != null
						&& account.getName() != null)
				{
					actionForm.getTokenFormBean().setEndCustomerShipto(account.getSoldToNumber()
							+ " " + account.getName());
				}
			}
			//actionForm.getTokenFormBean().setCxpSoldToList(getCxpSoldToList());
			actionForm.getTokenFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
		}
		else
		{
			if (checkNonEmpty(endCoustomerAccount))
			{
				actionForm.getTokenFormBean().setShipTo(getValidId(endCoustomerAccount));
			}
			actionForm.getTokenFormBean().setShipToCreationFlagForNonBP(GRTConstants.BLOCK);
		}
		logger.debug("endCoustomerAccount : "
				+ actionForm.getTokenFormBean().getEndCustomerShipto());
		return Action.SUCCESS;
	}

	public String showFuzzySearch() throws Exception
	{
		SortedMap<String, String>  provinceCode = getInstallBaseService().getProvinceCode();
		PageCache.stateSelectMap = getInstallBaseService().getStateCode();
		PageCache.countrySelectMap = getInstallBaseService().getCountryCode();
		PageCache.countryMap = getInstallBaseService().getCountryValues();
		PageCache.provinceSelectMap = provinceCode;
		PageCache.regionMap = getInstallBaseService().getRegionMap();

		validateSession();
		setSearchCriterialNull(actionForm.getTokenFormBean());
		logger.debug("TokenController.showFuzzySearch()");
		logger.debug("form.getShipTo() : "   + actionForm.getTokenFormBean().getShipTo());

		if (checkNonEmpty(actionForm.getTokenFormBean().getShipTo()))
		{
			Account account = getAccountService().queryAccount(actionForm.getTokenFormBean().getShipTo().trim());

			if (account != null)
			{
				actionForm.getTokenFormBean().setEndCustomerName(account.getName());
				actionForm.getTokenFormBean().setEndCustomerStreetName(account.getPrimaryAccountStreetAddress());
				actionForm.getTokenFormBean().setEndCustomerCity(account.getPrimaryAccountCity());
				actionForm.getTokenFormBean().setEndCustomerPostalCode(account.getPrimaryAccountPostalCode());
				actionForm.getTokenFormBean().setEndCustomerCountry(account.getPrimaryAccountCountry());
				logger.debug(account.getPrimaryAccountCountry());
				String twochar = getTwoCharISOCode(actionForm.getTokenFormBean().getEndCustomerCountry());
				if (checkNonEmpty(twochar))
				{
					actionForm.getTokenFormBean().setEndCustomerCountry(twochar);
				}

				logger.debug(actionForm.getTokenFormBean().getEndCustomerCountry());
				logger.debug(account.getPrimaryAccountState());

				Map<Long,Region> countryRegion = PageCache.regionMap.get(actionForm.getTokenFormBean().getEndCustomerCountry());

				if (countryRegion != null){
					for (Map.Entry<Long, Region> region : countryRegion.entrySet())
					{
						String value = region.getValue().getDescription();
						String desc = region.getValue().getRegion();
						logger.debug(value);
						logger.debug(desc);
						if ( (value != null && value.equalsIgnoreCase(account.getPrimaryAccountState()) )  || (desc!=null && desc.equalsIgnoreCase(account.getPrimaryAccountState())))
						{
							actionForm.getTokenFormBean().setSelectedRegionId(region.getValue().getRegionId());
						}
					}
				}
				else
				{
					actionForm.getTokenFormBean().setRegionValue(account.getPrimaryAccountState());
				}

				logger.debug(actionForm.getTokenFormBean().getEndCustomerCountry());
				logger.debug(account.getPrimaryAccountState());

			}
			else
			{
				String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenShipToInvalidError");
				String errorMessage = grtConfig.getTokenShipToInvalidError();
				errorMessage = errorMessage.replace("<SHIPTONAME>", actionForm.getTokenFormBean().getShipTo().trim());
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute("soldtosearcherror", errorMessage);
				//getRequest().setAttribute("soldtosearcherror",actionForm.getTokenFormBean().getShipTo().trim() + " : is not a valid Sold To/Functional Location." );
			}
		}
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setSearchFlag(GRTConstants.BLOCK);
		actionForm.getTokenFormBean().setCountryList(PageCache.countrySelectMap);
		actionForm.getTokenFormBean().setStateList(PageCache.stateSelectMap);
		actionForm.getTokenFormBean().setProvinceList(PageCache.provinceSelectMap);
		actionForm.getTokenFormBean().setRegionMap(PageCache.regionMap);

		if (actionForm.getTokenFormBean().getEndCustomerCountry()== null)
		{
			actionForm.getTokenFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
		}
		actionForm.getTokenFormBean().setCurrentCountryRegion(actionForm.getTokenFormBean().getRegionMap().get(actionForm.getTokenFormBean().getEndCustomerCountry()));
		actionForm.getTokenFormBean().setRegionValue(null);
		actionForm.getTokenFormBean().setSelectedAccountNumber(null);
		return Action.SUCCESS;
	}

	public String lookupBackButton() throws Exception
	{
		validateSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		if (actionForm.getTokenFormBean().getAttachedBp() == null
				|| actionForm.getTokenFormBean().getAttachedBp().size() > 0)
		{
			actionForm.getTokenFormBean().setTokenDetailsFlag(GRTConstants.BLOCK);
		}
		else
		{
			actionForm.getTokenFormBean().setBplinkdetailsFlag(GRTConstants.BLOCK);
		}

		return Action.SUCCESS;
	}

	//@Jpf.Action(forwards = {
	//@Jpf.Forward(name = "registrationHome", returnAction = "begin")})
	public String cancelTokenRedemption() throws Exception
	{
		validateSession();
		logger.debug("In TokenController.cancelTokenRedemption() ");
		getRequest().getSession().setAttribute("sapbox", null);
		return "registrationHome";
	}

	public String showContractdate() throws Exception
	{
		validateSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setContractStartDate(new SimpleDateFormat("MM/dd/yyyy").format(getFirstOfNextMonth()
				.getTime()));
		actionForm.getTokenFormBean().setContractDateFlag(GRTConstants.BLOCK);
		actionForm.getTokenFormBean().setAdditionalInfo(null);
		actionForm.getTokenFormBean().setReplaceContract("No");
		return Action.SUCCESS;
	}


	public String redeemToken() throws Exception{
		validateSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		TokenRedemptionRequestType request = getTokenRedemptionRequest(actionForm.getTokenFormBean());
		try {
			String result = getAccountService().validateIPOLatestReleaseExistence(
					actionForm.getTokenFormBean().getEndCoustomerAccount().getSoldToNumber());

			if (result != null
					&& result.trim().length() > 0) {
				if(result.equalsIgnoreCase(GRTConstants.IP_OFFICE_OLDER_RELEASE)) {
					result = grtConfig.getTokenIpOfficeVersionErrMsg().replace("<RELEASE>", "9.0");
				} else if(result.equalsIgnoreCase(GRTConstants.IP_OFFICE_NO_EQUIPMENT_ONBOARDED)) {
					result = grtConfig.getTokenNoIPOEquipmentErrMsg().replace("<ACCOUNTID>", actionForm.getTokenFormBean().getEndCoustomerAccount().getSoldToNumber());
				}
				throw new Exception(result);
			}
			boolean sendOEFCNotification = false;
			/*if(StringUtils.isNotEmpty(actionForm.getTokenFormBean().getReplaceContract()) && actionForm.getTokenFormBean().getReplaceContract().equalsIgnoreCase(GRTConstants.YES)) {
				sendOEFCNotification = true;
			}*/
			
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
			Contract contract = null;
			if(GRTConstants.YES.equalsIgnoreCase(actionForm.getTokenFormBean().getReplaceContract()) ){
				sendOEFCNotification = true;
				contract = new Contract();
				String conNum = actionForm.getTokenFormBean().getSelectedContractNumber();
				logger.debug("Contract Number Selected to Cancel: "+conNum);
				contract.setContractNumber(conNum);
				try {
					if(StringUtils.isNotEmpty(actionForm.getTokenFormBean().getCancelledContractStartDate())){
						contract.setContractStartDateStr(actionForm.getTokenFormBean().getCancelledContractStartDate());
						contract.setContractStartDate(sdf.parse(actionForm.getTokenFormBean().getCancelledContractStartDate()));
					}
					if(StringUtils.isNotEmpty(actionForm.getTokenFormBean().getCancelledContractEndDate())){
						contract.setContractEndDateStr(actionForm.getTokenFormBean().getCancelledContractEndDate());
						contract.setContractEndDate(sdf.parse(actionForm.getTokenFormBean().getCancelledContractEndDate()));
					}
				} catch(ParseException pe){
					logger.error("Parse Exception while parsing Cancelled Contract start and end dates."+pe.getMessage());
				} catch(Exception e){
					logger.error("Exception while parsing Cancelled Contract start and end dates."+e.getMessage());
				}
             	/*List<Contract> contractList =form.getContracts();
             	if(contractList!=null ) {
             		for (int i=0; i<contractList.size(); i++){
             			if (contractList.get(i).getContractNumber().equals(conNum)){             				
             				contract = contractList.get(i);             				
             			}
             		}
             	}*/
			}
	            
			logger.debug("Region:"+actionForm.getTokenFormBean().getRegion());
			logger.debug("UserId:" + getUserFromSession().getUserId());            
			
			logger.debug("UserId:" + getUserFromSession().getUserId());   
            if(contract != null){
            	logger.debug("Cancel Number:"+contract.getContractNumber());
            	logger.debug("Cancel Contract Start Date:"+contract.getContractStartDateStr());
            	logger.debug("Cancel Contract End Date:"+contract.getContractEndDateStr());
            }
	        TokenRedemptionResponse response = redeemToken(request, sendOEFCNotification, actionForm.getTokenFormBean().getAdditionalInfo(), actionForm.getTokenFormBean().getTokenDetails(), actionForm.getTokenFormBean().getRegion(), getUserFromSession().getUserId(), contract);
	            
			if(response != null){
				try{
					getAccountService().saveTokenRedemption(contructTokenRedemption(request, response, actionForm.getTokenFormBean(), contract));
				}catch (Throwable throwable)
				{
					logger.error("Exception occured in saving TokenRedemption:" + throwable.getMessage(), throwable);
				}
			}
			if (response.getContractNumber() == null || !checkNonEmpty(response.getContractNumber().toString())) {
				throw new Exception(response.getDescription());
			}
			logger.debug("Code :" + response.getCode());
			logger.debug("Description :" + response.getDescription());
			logger.debug("ContractNumber :" + response.getContractNumber());
			logger.debug("ContractStartDate :" + response.getContractStartDate());

			if (response.getContractNumber().intValue() == -1) {
				String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenRedeemException");
				String errorMessage = grtConfig.getTokenRedeemException();
				errorMessage = errorCode + "###" + errorMessage + response.getDescription();
				getRequest().setAttribute("redeemTokenError", errorMessage);
				
				//getRequest().setAttribute("redeemTokenError", response.getDescription());
				throw new Exception(response.getDescription());
			}
			sdf = new SimpleDateFormat("MM/dd/yyyy");

			actionForm.getTokenFormBean().setContractStartDate(sdf.format(getFirstOfNextMonth().getTime()));
			actionForm.getTokenFormBean().setContractSubmitedDate(sdf.format(new Date()));

			getRequest().setAttribute("username", getUserFromSession().getFirstName()
					+ " " + getUserFromSession().getLastName());
			getRequest().setAttribute("usertype", getUserFromSession().getUserType());
			actionForm.getTokenFormBean().setTokenResponse(response);
			actionForm.getTokenFormBean().setFinalScreenFlag(GRTConstants.BLOCK);

		}
		catch (Exception exception)
		{
			logger.debug(exception);
			
			String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenRedeemException");
			String errorMessage = grtConfig.getTokenRedeemException();
			errorMessage = errorCode + "###" + errorMessage;
			getRequest().setAttribute("redeemTokenError", errorMessage);
			
			//getRequest().setAttribute("redeemTokenError", "Error occured while redeeming token");
			if (exception instanceof Exception)
			{
				errorMessage = errorMessage + " - " + exception.getMessage();
				getRequest().setAttribute("redeemTokenError", errorMessage);
				//getRequest().setAttribute("redeemTokenError", "Error occured while redeeming token : " + exception.getMessage());
			}
			actionForm.getTokenFormBean().setContractDateFlag(GRTConstants.BLOCK);
		}

		return Action.SUCCESS;
	}

	public TokenRedemptionResponse redeemToken(TokenRedemptionRequestType request, boolean sendOEFCNotification, String additionalInfo, TokenResponseTypeDto tokenDetails, String region, String userId, Contract contractToCancel) throws Exception {
		logger.debug("Entering redeemToken :");
		try{
			//Populate EndCustomer Email Id:
			if(StringUtils.isNotEmpty(request.getEndCustomerShipTo()) && StringUtils.isEmpty(request.getEndCustomerEmail())) {
				Account endCustomerAccount = this.getAccountService().queryAccountWithContacts(request.getEndCustomerShipTo());
				if(endCustomerAccount != null) {
					if(StringUtils.isNotEmpty(endCustomerAccount.getEmailId())) {
						request.setEndCustomerEmail(endCustomerAccount.getEmailId());
					} else if (endCustomerAccount.getContacts() != null && endCustomerAccount.getContacts().size() > 0) {
						String endCustomerEmail = endCustomerAccount.getContacts().get(0).getEmail();
						if(StringUtils.isNotEmpty(endCustomerEmail)) {
							request.setEndCustomerEmail(endCustomerEmail);
						}
					}
				} else {
					logger.debug("No contact found for endCustomer ShipTo:" + request.getEndCustomerShipTo());
				}
			}

			//Populate Redeemer Email Id:
			if(StringUtils.isNotEmpty(request.getRedeemerBPLinkID()) && StringUtils.isEmpty(request.getRedeemerEmail())) {
				BusinessPartner businessPartner = getInstallBaseService().getBusinessPartner(request.getRedeemerBPLinkID());
				if(businessPartner != null && StringUtils.isNotEmpty(businessPartner.getSoldToId())) {
					logger.debug("Picked default AccountId:" + businessPartner.getSoldToId() + " for BPLinkId:" + request.getRedeemerBPLinkID());
					Account reddemerAccount = this.getAccountService().queryAccountWithContacts(businessPartner.getSoldToId());
					if(reddemerAccount != null){
						if(StringUtils.isNotEmpty(reddemerAccount.getEmailId())) {
							request.setRedeemerEmail(reddemerAccount.getEmailId());
						} else if(reddemerAccount.getContacts() != null && reddemerAccount.getContacts().size() > 0) {
							String redeemerEmail = reddemerAccount.getContacts().get(0).getEmail();
							if(StringUtils.isNotEmpty(redeemerEmail)) {
								request.setRedeemerEmail(redeemerEmail);
							}
						}
					} else {
						logger.debug("No contact found for redeemer ShipTo:" + businessPartner.getSoldToId());
					}
				} else {
					logger.debug("No soldTo found for redeemer BPLinkId:" + request.getRedeemerBPLinkID());  
				}
			}
		} catch(Throwable throwable) {
			logger.error("Error while getting EndCustomer and Redeemer Email IDs", throwable);
		}
		logger.debug("ActivationEmail : "+request.getActivationEmail());
		logger.debug("CoDeliveryPartnerAccountNumber : "+request.getCoDeliveryPartnerAccountNumber());
		Calendar contractStartDate = request.getContractStartDate();
		logger.debug("ContractStartDate : "+request.getContractStartDate());
		logger.debug("DealerPartnerAccountNumber : "+request.getDealerPartnerAccountNumber());
		logger.debug("DistiSoldTo : "+request.getDistiSoldTo());
		logger.debug("EndCustomerEmail : "+request.getEndCustomerEmail());
		logger.debug("EndCustomerSalesOrg : "+request.getEndCustomerSalesOrg());
		logger.debug("EndCustomerShipTo : "+request.getEndCustomerShipTo());
		logger.debug("USGovernment : " + request.getGovernmentUsage());

		AddressType address = request.getEndCustomerShipToAddress();
		logger.debug("City : "+address.getCity());
		logger.debug("Country : "+address.getCountry());
		logger.debug("Name : "+address.getName());
		logger.debug("Region : "+address.getRegion());
		logger.debug("State : "+address.getState());
		logger.debug("Street1 : "+address.getStreet1());
		logger.debug("Street2 : "+address.getStreet2());
		logger.debug("Zip : "+address.getZip());

		logger.debug("RedeemerBPLinkID : "+request.getRedeemerBPLinkID());
		logger.debug("RedeemerBPName : "+request.getRedeemerBPName());
		logger.debug("RedeemerEmail : "+ request.getRedeemerEmail());
		logger.debug("RedeemerName : "+request.getRedeemerName());
		logger.debug("RedeemerUserName : "+request.getRedeemerUserName());
		logger.debug("TokenNumber : "+request.getTokenNumber());
		logger.debug("DistiSoldToName : "+request.getDistiSoldToName());
		logger.debug("DistributorAccount : "+request.getDistributorAccount());

		//TokenRedemptionResponse response =  this.getGenericClient().redeemToken(request);
		TokenRedemptionResponse response =  getAccountService().redeemToken(request);

		try {
			String contractNumber = response.getContractNumber()!=null?response.getContractNumber().toString():"";
			try {
				if(StringUtils.isEmpty(contractNumber) || contractNumber.equals("-1")){
					String logString = "MONITOR:[Transaction Failure:TokenRedemption][TokenNumber:"+ request.getTokenNumber() + "][Error:" + response.getDescription() + "][TARGET:TARGET_][TOKEN Details:" + ToStringBuilder.reflectionToString(request, ToStringStyle.MULTI_LINE_STYLE) + "]";
					if(StringUtils.isNotEmpty(response.getDescription())) {
						if(response.getDescription().toUpperCase().contains("Ship To does not exist in SAP".toUpperCase())) {
							logString=logString.replaceAll("TARGET_", "SIEBEL");
						} else if(response.getDescription().toUpperCase().contains("Address validation failed".toUpperCase())) {
							logString=logString.replaceAll("TARGET_", "CMD-PL");
						}  else if(response.getDescription().toUpperCase().contains("Customer PO number must not be greater than 20 characters".toUpperCase()) || 
								response.getDescription().toUpperCase().contains("Unknown Exception".toUpperCase())) {
							logString=logString.replaceAll("TARGET_", "FMW");
						} else if(!response.getDescription().toUpperCase().contains("Ship to  is blocked in SAP".toUpperCase())){
							logString=logString.replaceAll("TARGET_", "GRT-PROJECT");
						}
					}
					logger.error(logString);	        		
				}
			} catch(Throwable throwable){}

			/*if (StringUtils.isNotEmpty(response.getDescription()) && response.getDescription().contains(GRTConstants.PART_TOOL_ERROR_MSG)) {
	            	logger.debug("OVO-MONITORING: Token redemption for #" + request.getTokenNumber() + ", has failed shipTo:" + request.getEndCustomerShipTo()
	            			+ " for user :" + userId 
	            			+ " and address validation for address: " + address.getName() + ", " + address.getStreet1() + ", " + address.getCity() + ", " 
	            			+ address.getState() + ", " + address.getCountry() + " " + address.getZip());
	            }*/

			logger.debug("sendOEFCNotification:" + sendOEFCNotification);
			if(response != null && StringUtils.isNotEmpty(contractNumber) 
					&& !"-1".equalsIgnoreCase(contractNumber) && sendOEFCNotification) {
				logger.debug("Initiating mail to OEFC");
				logger.debug("EndCustomerCoutryCode to fetch OEFC email Ids:"+region);
				String emailIds = getAccountService().fetchOEFCMailIds(region);
				logger.debug("OEFC Mail Ids:"+emailIds);
				//request.setRedeemerEmail(request.getRedeemerEmail()+";"+emailIds.replaceAll(",",";"));
				//getAccountService().sendMailNotification(request, contractNumber, contractStartDate, additionalInfo, tokenDetails, emailIds);
				mailUtil.sendMailNotification(request, contractNumber, contractStartDate, additionalInfo, tokenDetails, emailIds, contractToCancel);
			}
		} catch(Throwable throwable) {
			logger.error("Error while sending mail to OEFC for contract replacement", throwable);
		}
		return response;
	}


	private void setAllFlagNone(TokenFormBean form)
	{
		form.setTokenDetailsFlag(GRTConstants.NONE);
		form.setTokenNumberFlag(GRTConstants.NONE);
		form.setBplinkdetailsFlag(GRTConstants.NONE);
		form.setShipToCreationFlagForBP(GRTConstants.NONE);
		form.setShipToCreationFlagForNonBP(GRTConstants.NONE);
		form.setContractDateFlag(GRTConstants.NONE);
		form.setSearchDataFlag(GRTConstants.NONE);
		form.setSearchFlag(GRTConstants.NONE);
		form.setTokenRedemptionFinalFlag(GRTConstants.NONE);
		form.setFinalScreenFlag(GRTConstants.NONE);
	}

	private void setSearchCriterialNull(TokenFormBean form)
	{
		form.setRegionMap(PageCache.regionMap);
		form.setEndCustomerCity(null);
		form.setEndCustomerCountry(GRTConstants.COUNTRY_US);
		form.setEndCustomerName(null);
		form.setEndCustomerOtherProvince(null);
		form.setEndCustomerPostalCode(null);
		form.setEndCustomerProvince(null);
		form.setEndCustomerState(null);
		form.setEndCustomerStreetName(null);
		form.setCurrentCountryRegion(form.getRegionMap().get(form.getEndCustomerCountry()));
		form.setRegionValue(null);
		form.setSelectedRegionId(0L);
		form.setRegionId(null);
		form.setRegionValue(null);
	}


	private String getValidId(String accountId)
	{
		String modifiedAccountId = null;
		if (StringUtils.isNotEmpty(accountId))
		{
			if (accountId.length() == 10)
			{
				modifiedAccountId = accountId;
			}
			else
			{
				int lenthOfString = accountId.length();
				if (lenthOfString < 10)
				{
					int numOfZeros = 10 - lenthOfString;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < numOfZeros; i++)
					{
						sb.append("0");
					}
					sb.append(accountId);
					modifiedAccountId = sb.toString();
				}
			}
		}
		else
		{
			modifiedAccountId = accountId;
		}
		return modifiedAccountId;
	}

	private boolean validateMedalLevelForBP(TokenFormBean form) throws Exception {
		CSSPortalUser user = getUserFromSession();
		BusinessPartner bp = getInstallBaseService().getBusinessPartner(user.getBpLinkId());
		MedalLevelEnum bpMedalLevel = null;
		MedalLevelEnum tokenMedalLevel = null;
		if (bp != null) {
			bpMedalLevel = getAccountService().getMedalLevel(bp.getSoldToId());
		}
		if (form.getTokenDetails() != null) {
			tokenMedalLevel = form.getTokenDetails().getMedaLevel();
		}

		logger.debug("BP Login : Meadal level of BP : " + bpMedalLevel);
		logger.debug("BP Login : Meadal level of Token : " + tokenMedalLevel);

		if (bpMedalLevel == MedalLevelEnum.NOTAUTHORIZED) {
			return false;
		} else if (bpMedalLevel != null && tokenMedalLevel != null) {
			return validateMedalLevel(bpMedalLevel, tokenMedalLevel);
		} else {
			return true;
		}
	}

	private boolean validateMedalLevel(MedalLevelEnum bpMedalLevel, MedalLevelEnum tokenMedalLevel) {
		if (bpMedalLevel.compareTo(tokenMedalLevel) >= 0) {
			return true;
		}
		return false;
	}

	private BusinessPartner getBusinessPartner(List<BusinessPartner> businessPartners,
			String soldToID)
	{

		for (BusinessPartner businessPartner : businessPartners)
		{
			if (businessPartner.getSoldToId().equals(soldToID))
			{
				return businessPartner;
			}
		}
		return null;
	}

	private boolean validateMedalLevelForAssociate(TokenFormBean form) throws Exception {
		MedalLevelEnum bpMedalLevel = null;
		if (form.getSelectedBp() != null) {
			bpMedalLevel = getAccountService().getMedalLevel(form.getSelectedBp().getSoldToId());
			MedalLevelEnum tokenMedalLevel = form.getTokenDetails().getMedaLevel();
			logger.debug("AvayaLogin : Meadal level of BP : " + bpMedalLevel);
			logger.debug("AvayaLogin : Meadal level of Token : " + tokenMedalLevel);

			if (bpMedalLevel == MedalLevelEnum.NOTAUTHORIZED) {
				return false;
			} else if (bpMedalLevel != null && tokenMedalLevel != null) {
				return validateMedalLevel(bpMedalLevel, tokenMedalLevel);
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private boolean checkNonEmpty(String string)
	{
		if ((string != null)
				&& (string.trim().length() > 0))
		{
			return true;
		}
		return false;
	}

	private String getTwoCharISOCode(String countryName)
	{
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

	private Calendar getFirstOfNextMonth()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DATE, 1);
		return c;
	}

	private TokenRedemption contructTokenRedemption(TokenRedemptionRequestType tokenRedemptionRequestType, TokenRedemptionResponse tokenRedemptionResponse, TokenFormBean form, Contract contract){
		TokenRedemption tokenRedemption = new TokenRedemption();
		CSSPortalUser userProfile = this.getUserFromSession();
		tokenRedemption.setTokenNumber(tokenRedemptionRequestType.getTokenNumber());
		tokenRedemption.setRedemptionDate(new Date());
		tokenRedemption.setRedeemerBPLinkId(tokenRedemptionRequestType.getRedeemerBPLinkID());
		tokenRedemption.setRedeemerName(GRTUtil.trimPropertytoMaxLength(userProfile.getLastName()+", "+userProfile.getFirstName(), 255));

		TokenResponseTypeDto tokenResponse = form.getTokenDetails();
		if(tokenResponse != null) {
			tokenRedemption.setPoNumber(GRTUtil.trimPropertytoMaxLength(tokenResponse.getPONumber(), 255));
			tokenRedemption.setOfferName(GRTUtil.trimPropertytoMaxLength(tokenResponse.getOfferName(), 255));
			tokenRedemption.setDistiSoldToId(tokenResponse.getDistiSoldTo());
			tokenRedemption.setDistiBPLinkId(tokenResponse.getDistiBPLinkId());
			tokenRedemption.setMedalLevel(GRTUtil.trimPropertytoMaxLength(tokenResponse.getMedaLevel() != null?tokenResponse.getMedaLevel().getMedalLevelDescription():"", 255));
			tokenRedemption.setRequesterSalesOrg(GRTUtil.trimPropertytoMaxLength(tokenResponse.getRequesterSalesOrg(), 255));
			tokenRedemption.setRequesterDistributionChannel(GRTUtil.trimPropertytoMaxLength(tokenResponse.getRequesterDistributionChannel(), 255));
			tokenRedemption.setRequesterDivision(GRTUtil.trimPropertytoMaxLength(tokenResponse.getRequesterDivision(), 255));
			tokenRedemption.setPayer(GRTUtil.trimPropertytoMaxLength(tokenResponse.getPayer(), 255));
			tokenRedemption.setServiceTerm(GRTUtil.trimPropertytoMaxLength(tokenResponse.getServiceTerm(), 255));
			tokenRedemption.setServiceType(GRTUtil.trimPropertytoMaxLength(tokenResponse.getServiceType(), 255));
			tokenRedemption.setSalesOrderNo(GRTUtil.trimPropertytoMaxLength(tokenResponse.getSalesOrderNo(), 255));
			tokenRedemption.setSourceSystem(GRTUtil.trimPropertytoMaxLength(tokenResponse.getSourceSystem(), 255));
			tokenRedemption.setSqrn(GRTUtil.trimPropertytoMaxLength(tokenResponse.getSQRN(), 255));
			tokenRedemption.setStatus(tokenResponse.getStatus());
		}
		tokenRedemption.setEndCustomerShipTo(tokenRedemptionRequestType.getEndCustomerShipTo());
		tokenRedemption.setEndCustomerEmail(GRTUtil.trimPropertytoMaxLength(tokenRedemptionRequestType.getEndCustomerEmail(), 255));
		tokenRedemption.setEndCustomerSalesOrg(GRTUtil.trimPropertytoMaxLength(tokenRedemptionRequestType.getEndCustomerSalesOrg(), 255));
		tokenRedemption.setDistributorAccount(tokenRedemptionRequestType.getDistributorAccount());
		tokenRedemption.setActivationEmail(GRTUtil.trimPropertytoMaxLength(tokenRedemptionRequestType.getActivationEmail(), 255));
		tokenRedemption.setCodelPartnerAccountNo(tokenRedemptionRequestType.getCoDeliveryPartnerAccountNumber());
		tokenRedemption.setDealerPartnerAccountNo(tokenRedemptionRequestType.getDealerPartnerAccountNumber());
		tokenRedemption.setGovernmentUsage(GRTConstants.YES.equals(form.getUsStore())?true:false);

		AddressType address = tokenRedemptionRequestType.getEndCustomerShipToAddress();
		if(address != null){
			tokenRedemption.setCountry(GRTUtil.trimPropertytoMaxLength(address.getCountry(), 255));
			tokenRedemption.setRegion(GRTUtil.trimPropertytoMaxLength(address.getRegion(), 255));
			tokenRedemption.setState(GRTUtil.trimPropertytoMaxLength(address.getState(), 255));
			tokenRedemption.setStateFull(GRTUtil.trimPropertytoMaxLength(address.getState_Full(), 255));
			tokenRedemption.setStreet(GRTUtil.trimPropertytoMaxLength(address.getStreet1(), 255));
			tokenRedemption.setZip(address.getZip());
		}
		tokenRedemption.setResponseCode(tokenRedemptionResponse.getCode());
		tokenRedemption.setResponseDescription(GRTUtil.trimPropertytoMaxLength(tokenRedemptionResponse.getDescription(), 255));
		if(tokenRedemptionResponse.getContractNumber() != null){
			tokenRedemption.setContractNumber(GRTUtil.trimPropertytoMaxLength(tokenRedemptionResponse.getContractNumber().toString(), 255));
		}
		if(tokenRedemptionResponse.getContractStartDate() != null) {
			tokenRedemption.setContractStartDate(tokenRedemptionResponse.getContractStartDate().getTime());
		} else if(tokenRedemptionRequestType.getContractStartDate() != null) {
			tokenRedemption.setContractStartDate(tokenRedemptionRequestType.getContractStartDate().getTime());
		}
		tokenRedemption.setSelectedRegion(GRTUtil.trimPropertytoMaxLength(form.getRegion(), 255));
		if(StringUtils.isNotEmpty(form.getReplaceContract()) && form.getReplaceContract().equalsIgnoreCase(GRTConstants.YES)) {
			tokenRedemption.setReplaceExistingContract(true);
		}
		tokenRedemption.setAdditionalInformation(GRTUtil.trimPropertytoMaxLength(form.getAdditionalInfo(), 500));
		Set<LogServiceDetails> serviceDetailSet = new HashSet<LogServiceDetails>();
		LogServiceDetails logServiceDetails = null;
		List<ServiceDetails> serviceDetailsList = form.getTokenServiceDetails();
		if(serviceDetailsList != null && serviceDetailsList.size() > 0){
			for(ServiceDetails serviceDetails : serviceDetailsList){
				logServiceDetails = new LogServiceDetails();
				logServiceDetails.setTokenRedemption(tokenRedemption);
				logServiceDetails.setServiceCode(serviceDetails.getServiceCode());
				logServiceDetails.setMaterialCode(serviceDetails.getMaterialCode());
				if(serviceDetails.getQuantity()!= null){
					logServiceDetails.setQuantity(serviceDetails.getQuantity().doubleValue());
				}
				logServiceDetails.setListPrice(serviceDetails.getListPrice());
				logServiceDetails.setDiscountPrice(serviceDetails.getDiscountPrice());
				logServiceDetails.setNetPrice(serviceDetails.getNetPrice());
				logServiceDetails.setItemNumber(serviceDetails.getItemNumber());
				logServiceDetails.setServiceCodeDescription(serviceDetails.getServiceCodeDescription());
				logServiceDetails.setMaterialCodeDescription(serviceDetails.getMaterialCodeDescription());
				serviceDetailSet.add(logServiceDetails);
			}
			tokenRedemption.setServiceDetails(serviceDetailSet);
		}
		// Cancelled contract details
		if(contract != null){
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
			logger.debug("Cancel Number:"+contract.getContractNumber());
        	logger.debug("Cancel Contract Start Date:"+contract.getContractStartDateStr());
        	logger.debug("Cancel Contract End Date:"+contract.getContractEndDateStr());
        	if(contract.getContractNumber() != null){
        		tokenRedemption.setCancelledContractNumber(GRTUtil.trimPropertytoMaxLength(contract.getContractNumber().toString(), 255));
        	}
			try{
				if(contract.getContractStartDateStr() != null) {
					tokenRedemption.setCancelledContractStartDate(sdf.parse(contract.getContractStartDateStr()));
				}
				if(contract.getContractEndDateStr() != null) {
					tokenRedemption.setCancelledContractEndDate(sdf.parse(contract.getContractEndDateStr()));
				}
			} catch(ParseException pe){
				logger.error("Error while parsing Cancelled Token start and end dates."+ pe.getMessage());
			}
		}
		return tokenRedemption;
	}

	private TokenRedemptionRequestType getTokenRedemptionRequest(TokenFormBean form) throws Exception
	{
		logger.debug("Replace Contract : "
				+ form.getReplaceContract());
		logger.debug("Additional Information : "
				+ form.getAdditionalInfo());

		TokenRedemptionRequestType request = new TokenRedemptionRequestType();
		request.setTokenNumber(form.getTokenNumber());
		request.setDistiSoldTo(form.getTokenDetails().getDistiSoldTo());
		request.setEndCustomerShipTo(form.getEndCoustomerAccount().getSoldToNumber());
		request.setContractStartDate(getFirstOfNextMonth());
		request.setEndCustomerSalesOrg(form.getTokenDetails().getRequesterSalesOrg());
		CSSPortalUser user = getUserFromSession();
		StringBuilder name = new StringBuilder(user.getFirstName());
		request.setRedeemerEmail(user.getEmailAddress());
		if(checkNonEmpty(form.getAdditionalEmailIds())){
			request.setAdditional_Token_Redemption_Emails(form.getAdditionalEmailIds());
		}
		if (user.getMiddleName() != null)
		{
			name.append(" "
					+ user.getMiddleName());
		}
		if (user.getLastName() != null)
		{
			name.append(" "
					+ user.getLastName());
		}
		request.setRedeemerUserName(name.toString());
		request.setRedeemerName(name.toString());
		if (GRTConstants.USER_TYPE_BP.equalsIgnoreCase(user.getUserType()))
		{
			BusinessPartner businessPartner = getInstallBaseService().getBusinessPartner(user.getBpLinkId());
			if (businessPartner != null)
			{
				request.setRedeemerBPName(businessPartner.getSoldToName());
			}
			request.setRedeemerBPLinkID(user.getBpLinkId());
		}
		/*else if (StringUtils.isEmpty(form.getTokenDetails().getDistiBPLinkId()))
	        {
	            request.setRedeemerBPLinkID(form.getBpLinkID());
	        }*/
		else {
			String bpLinkId = form.getBpLinkID();
			request.setRedeemerBPLinkID(bpLinkId);
		}
		if (!GRTConstants.USER_TYPE_BP.equalsIgnoreCase(user.getUserType()) && form.getSelectedBp() != null) {
			request.setRedeemerBPName(form.getSelectedBp().getSoldToName());
		}
		if (GRTConstants.YES.equals(form.getUsStore()))
		{
			request.setGovernmentUsage(true);
		}
		else
		{
			request.setGovernmentUsage(false);
		}

		AddressType address = new AddressType();
		Account account = form.getEndCoustomerAccount();

		if (form.getTokenDetails().getServiceType().contains(GRTConstants.TOKEN_CODELIVERY) || form.getTokenDetails().getServiceType().toUpperCase().contains(GRTConstants.TOKEN_CODELIVERY_SAP))
		{
			logger.debug("CODELIVERY TOKEN");
			request.setCoDeliveryPartnerAccountNumber(form.getSelectedBp().getSoldToId());
			//request.setDealerPartnerAccountNumber(form.getSelectedBp().getSoldToId());
		}
		// Dealer Partner should be sent in all the cases. Co-Del and WholeSale(includes Wholesale/Retail/Direct)
		request.setDealerPartnerAccountNumber(form.getSelectedBp().getSoldToId());
		
		request.setEndCustomerName(account.getName());
		address.setCity(account.getPrimaryAccountCity());

		address.setCountry(account.getPrimaryAccountCountry());
		String twocharcode = getTwoCharISOCode(account.getPrimaryAccountCountry());

		if (twocharcode != null)
		{
			address.setCountry(twocharcode);
		}
		address.setName(account.getName());
		if (checkNonEmpty(account.getRegion()))
		{
			address.setRegion(account.getRegion());
		}
		if (checkNonEmpty(account.getPrimaryAccountState()))
		{
			address.setState(account.getPrimaryAccountState());
		}
		if (checkNonEmpty(account.getPrimaryAccountStreetAddress()))
		{
			address.setStreet1(account.getPrimaryAccountStreetAddress());
		}
		if (checkNonEmpty(account.getPrimaryAccountStreetAddress2()))
		{
			address.setStreet2(account.getPrimaryAccountStreetAddress2());
		}
		address.setZip(account.getPrimaryAccountPostalCode());
		request.setEndCustomerShipToAddress(address);
		if (form.getDistributorAccount()!=null)
		{
			request.setDistiSoldToName(form.getDistributorAccount().getName());
			request.setDistributorAccount(form.getDistributorAccount().getSoldToNumber());
		}
		return request;
	}


	public String validateShipToId() throws Exception
	{
		validateSession();
		Account account = null;
		String actType = null;
		try {
			logger.debug("ShipTO  : "
					+ actionForm.getTokenFormBean().getShipTo());
			account = getAccountService().queryAccount(actionForm.getTokenFormBean().getShipTo());;
			setAllFlagNone(actionForm.getTokenFormBean());
			CSSPortalUser user = getUserFromSession();
			actionForm.getTokenFormBean().setSuperUser(user.isSuperUser());
			boolean isUSsalesOrg = GRTConstants.B_BOX_SALES_ORG
					.equals(actionForm.getTokenFormBean().getTokenDetails().getRequesterSalesOrg());

			logger.debug("isUSsalesOrg  : "
					+ isUSsalesOrg);
			actType = account!=null?account.getType():null;
	        logger.debug("Account Type: "+actType);
	        if (actType != null && !(GRTConstants.ACCOUNT_TYPE_SHIPTO_PARTY.equalsIgnoreCase(actType)
					|| GRTConstants.ACCOUNT_TYPE_SOLDTO_PARTY.equalsIgnoreCase(actType))) {
	        	String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenAccountNotEligibleErrMsg");
				String errorMessage = grtConfig.getTokenAccountNotEligibleErrMsg();
				errorMessage = errorCode + "###" + errorMessage;
				this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, errorMessage);
				//this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, "The account number you entered is not eligible for registration activity in GRT, please enter a different account number.");
	            return showSoldToId();
			}
			if (GRTConstants.USER_TYPE_BP.equals(getUserFromSession().getUserType()))
			{
				actionForm.getTokenFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
				if (account == null)
				{
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenSoldToNotFoundError");
					String errorMessage = grtConfig.getTokenSoldToNotFoundError();
					errorMessage = errorCode + "###" + errorMessage;
					this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, errorMessage);
					//this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, GRTConstants.SOLD_TO_NOT_FOUND);
					return showSoldToId();
				}
				//else if (getAccountDelegate().getBPAccountTempAccess(actionForm.getTokenFormBean().getBpLinkID(), actionForm.getTokenFormBean().getShipTo()) != null)
				//TODO
				else if (getAccountService().getBPAccountTempAccess(user.getBpLinkId(), actionForm.getTokenFormBean().getShipTo()) != null)
				{
					logger.debug("Country  : "
							+ account.getPrimaryAccountCountry());
					logger.debug("Country  : "
							+ account.getCountryCode());
					actionForm.getTokenFormBean().setShipToCreationFlagForBP(GRTConstants.BLOCK);
				}
				//TODO
				else if (!getBaseRegistrationService().isSoldToValidForCurrentUser(actionForm.getTokenFormBean().getShipTo(),
						user.getUserId(), user.getBpLinkId()))
				{
					logger.debug("Country  : " + account.getPrimaryAccountCountry());
					
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenBpUnauthorizedForSoldToError");
					String errorMessage = grtConfig.getTokenBpUnauthorizedForSoldToError();
					errorMessage = errorCode + "###" + errorMessage;
					this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, errorMessage);
					//this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, GRTConstants.BP_NOT_AUTHORIZED_FOR_SOLD_TO);
					return showSoldToId();
				}
			}
			else
			{
				actionForm.getTokenFormBean().setShipToCreationFlagForNonBP(GRTConstants.BLOCK);
				if (account == null)
				{
					String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenSoldToNotFoundError");
					String errorMessage = grtConfig.getTokenSoldToNotFoundError();
					errorMessage = errorCode + "###" + errorMessage;
					this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, errorMessage);
					//this.getRequest().setAttribute(GRTConstants.SOLD_TO_ERROR, GRTConstants.SOLD_TO_NOT_FOUND);
					return showSoldToId();
				}
			}
			logger.debug("Country PrimaryAccountCountry : "
					+ account.getPrimaryAccountCountry());
			logger.debug("Country CountryCode : "
					+ account.getCountryCode());
			if (isUSsalesOrg
					&& (!account.getPrimaryAccountCountry().trim().equals(GRTConstants.COUNTRY_US) && !account
							.getPrimaryAccountCountry().trim().equals(GRTConstants.COUNTRY_USA)))
			{
				String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenCountryUSError");
				String errorMessage = grtConfig.getTokenCountryUSError();
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute(GRTConstants.COUNTRY_US_ERROR, errorMessage);
				
				//getRequest().setAttribute(GRTConstants.COUNTRY_US_ERROR, true);
				return showSoldToId();

			}
			else if (!isUSsalesOrg
					&& (account.getPrimaryAccountCountry().trim().equals(GRTConstants.COUNTRY_US) || account
							.getPrimaryAccountCountry().trim().equals(GRTConstants.COUNTRY_USA)))
			{
				String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenCountryNonUSError");
				String errorMessage = grtConfig.getTokenCountryNonUSError();
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute(GRTConstants.COUNTRY_NON_US_ERROR, errorMessage);
				//getRequest().setAttribute(GRTConstants.COUNTRY_NON_US_ERROR, true);
				return showSoldToId();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}  
		actionForm.getTokenFormBean().setEndCoustomerAccount(account);
		return showContractdate();

	}

	public String doFuzzySearch() throws Exception
	{
		logger.debug("TokenController.doFuzzySearch()");
		validateSession();
		setAllFlagNone(actionForm.getTokenFormBean());
		actionForm.getTokenFormBean().setSearchFlag(GRTConstants.BLOCK);
		actionForm.getTokenFormBean().setSearchResult(null);
		updateRegion(actionForm.getTokenFormBean());
		if (actionForm.getTokenFormBean().getEndCustomerCountry()== null)
		{
			actionForm.getTokenFormBean().setEndCustomerCountry(GRTConstants.COUNTRY_US);
		}
		actionForm.getTokenFormBean().setCurrentCountryRegion(actionForm.getTokenFormBean().getRegionMap().get(actionForm.getTokenFormBean().getEndCustomerCountry()));

		if (actionForm.getTokenFormBean().getEndCustomerCountry().equalsIgnoreCase(GRTConstants.COUNTRY_US))
		{
			getRequest().getSession().setAttribute("sapbox", GRTConstants.BBoxDestination);
		}
		else
		{
			getRequest().getSession().setAttribute("sapbox", GRTConstants.IBoxDestination);
		}

		List<Result> results = null;
		List<String> soldToList = new ArrayList<String>();
		List<String> accessList = new ArrayList<String>();
		List<Result> finalResults = new ArrayList<Result>();
		try {
			results = getFuzzySearchResult(actionForm.getTokenFormBean());
			if(results != null && results.size() > 0){
				CSSPortalUser userProfile = getUserFromSession();
				if (GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userProfile.getUserType())) {
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
					actionForm.getTokenFormBean().setSearchResult(finalResults);
				} else {
					actionForm.getTokenFormBean().setSearchResult(results);
				}
				actionForm.getTokenFormBean().setSearchDataFlag(GRTConstants.BLOCK);
			}
			else
			{
				getRequest().setAttribute("noresult", true);

			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			
			String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenFuzzySearchError");
			String errorMessage = grtConfig.getTokenFuzzySearchError();
			errorMessage = errorCode + "###" + errorMessage;
			getRequest().setAttribute("searchError", errorMessage);
			//getRequest().setAttribute("searchError", "Error occured in search.");
			logger.debug("Error occur in calling address search service");
		}
		return Action.SUCCESS;
	}
	
	private List<Result> getFuzzySearchResult(TokenFormBean form) throws Exception
    {

        AddressSearchRequestType addressSearchRequestType = new AddressSearchRequestType();
        List<Request> requestList = new ArrayList<Request>();

        List<Result> resultList = new ArrayList<Result>();

        try
        {

            List<FuzzySearchParam> fuzzySearchParamList = getAccountService().getFuzzySearchParams();

            for (FuzzySearchParam fuzzySearchParam : fuzzySearchParamList)
            {
                Request request = new Request();

                if (fuzzySearchParam.getProperty().equals(GRTConstants.ACCOUNT_NAME)
                    && checkNonEmpty(form.getEndCustomerName()))
                {

                    logger.debug("Name : "
                        + form.getEndCustomerName().trim());
                    request.setValue(form.getEndCustomerName().trim());
                    request.setName(GRTConstants.SEARCH_NAME);
                }
                else if (fuzzySearchParam.getProperty().equals(GRTConstants.STREET)
                    && checkNonEmpty(form.getEndCustomerStreetName()))
                {
                    logger.debug("Street : "
                        + form.getEndCustomerStreetName().trim());
                    request.setValue(form.getEndCustomerStreetName().trim());
                    request.setName(GRTConstants.SEARCH_STREET);
                }
                else if (fuzzySearchParam.getProperty().equals(GRTConstants.CITY)
                    && checkNonEmpty(form.getEndCustomerCity()))
                {
                    logger.debug("CITY : "
                        + form.getEndCustomerCity().trim());
                    request.setValue(form.getEndCustomerCity().trim());
                    request.setName(GRTConstants.SEARCH_CITY);
                }
                else if ((fuzzySearchParam.getProperty().equals(GRTConstants.REGION)) && checkNonEmpty(form.getRegionId()))
                {
                    logger.debug("REGION : " +form.getRegionId());
                    request.setValue(form.getRegionId().trim());
                    request.setName(GRTConstants.SEARCH_REGION);

                }
                else if (fuzzySearchParam.getProperty().equals(GRTConstants.COUNTRY)
                    && checkNonEmpty(form.getEndCustomerCountry()))
                {
                    logger.debug("Country  : "
                        + form.getEndCustomerCountry().trim());
                    request.setValue(form.getEndCustomerCountry().trim());
                    request.setName(GRTConstants.SEARCH_COUNTRY);
                }
                else if (fuzzySearchParam.getProperty().equals(GRTConstants.ZIP)
                    && checkNonEmpty(form.getEndCustomerPostalCode()))
                {
                    logger.debug("Zipcode  : "
                        + form.getEndCustomerPostalCode().trim());
                    request.setValue(form.getEndCustomerPostalCode().trim());
                    request.setName(GRTConstants.SEARCH_ZIP);
                }

                if (checkNonEmpty(request.getValue()))
                {
                    request.setNameThreshhold(new BigDecimal(fuzzySearchParam.getThreshold()));
                    request.setNameWeight(new BigDecimal(fuzzySearchParam.getWeightage()));
                    requestList.add(request);
                }

                logger.debug("fuzzySearchParam: fuzzySearchParam.getProperty()"
                    + fuzzySearchParam.getProperty() + " fuzzySearchParam.getThreshold() : "
                    + fuzzySearchParam.getThreshold() + " fuzzySearchParam.getWeightage():"
                    + fuzzySearchParam.getWeightage());
            }

            addressSearchRequestType.setRequest(requestList
                .toArray(new Request[requestList.size()]));
            addressSearchRequestType.setMinThreshhold(new BigDecimal(grtConfig.getMinThreshhold().trim()));
            addressSearchRequestType.setTopRecords(new BigInteger(grtConfig.getTopRecords().trim()));

            resultList = getAccountService().getSearchResults(addressSearchRequestType);

        }
        catch (NullPointerException nex)
        {
            logger.error(nex.getMessage());
            nex.printStackTrace();
            throw nex;
        }
        catch (RemoteException rex)
        {
            logger.error(rex.getMessage());
            rex.printStackTrace();
            throw rex;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return resultList;

    }

	private void updateRegion (TokenFormBean form){
		logger.debug("Country  : "+ form.getEndCustomerCountry());
		logger.debug("RegionId : "+ form.getSelectedRegionId());
		Map<Long,Region> regionmap = form.getRegionMap().get(form.getEndCustomerCountry());
		form.setRegionValue(null);

		if (form.getSelectedRegionId() == 0L && regionmap != null)
		{
			form.setRegionId(null);
			form.setRegionValue(null);
		}

		if ( form.getSelectedRegionId() == 0L && StringUtils.isEmpty(form.getRegionValue()))
		{
			form.setRegionValue(null);
		}

		if ( regionmap != null && regionmap.get(form.getSelectedRegionId()) != null )
		{
			Region region = regionmap.get(form.getSelectedRegionId());
			form.setRegionId(region.getRegion());
			form.setRegionValue(region.getDescription());
		}
		else
		{
			logger.debug("RegionValue   : " + form.getRegionValue());
			form.setRegionId(form.getRegionValue());
		}

		logger.debug("RegionId      : " + form.getRegionId());
		logger.debug("RegionValue   : " + form.getRegionValue());

	}
	
	public String validateSelectedRecord() throws Exception {
	    	validateSession();
	        Result result = getSelectedRecord(actionForm.getTokenFormBean().getSelectedAccountNumber(), actionForm.getTokenFormBean().getSearchResult());
	        String accountType = null;
	        if (GRTConstants.B_BOX_SALES_ORG.equals(actionForm.getTokenFormBean().getTokenDetails().getRequesterSalesOrg())
	            && !result.getCountry().equals(GRTConstants.COUNTRY_US))
	        {
	        	String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenCountryUSError");
				String errorMessage = grtConfig.getTokenCountryUSError();
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute(GRTConstants.COUNTRY_US_ERROR, errorMessage);
	            //getRequest().setAttribute(GRTConstants.COUNTRY_US_ERROR, true);
	            setSearchCriterialNull(actionForm.getTokenFormBean());
	            setAllFlagNone(actionForm.getTokenFormBean());
	            actionForm.getTokenFormBean().setSearchFlag(GRTConstants.BLOCK);
	            actionForm.getTokenFormBean().setCountryList(PageCache.countrySelectMap);
	            actionForm.getTokenFormBean().setStateList(PageCache.stateSelectMap);
	            actionForm.getTokenFormBean().setProvinceList(PageCache.provinceSelectMap);

	            return Action.SUCCESS;

	        }
	        else if (!GRTConstants.B_BOX_SALES_ORG.equals(actionForm.getTokenFormBean().getTokenDetails().getRequesterSalesOrg())
	            && result.getCountry().equals(GRTConstants.COUNTRY_US))
	        {
	        	String errorCode = grtConfig.getEwiMessageCodeMap().get("tokenCountryNonUSError");
				String errorMessage = grtConfig.getTokenCountryNonUSError();
				errorMessage = errorCode + "###" + errorMessage;
				getRequest().setAttribute(GRTConstants.COUNTRY_NON_US_ERROR, errorMessage);
	            //getRequest().setAttribute(GRTConstants.COUNTRY_NON_US_ERROR, true);
	            setSearchCriterialNull(actionForm.getTokenFormBean());
	            setAllFlagNone(actionForm.getTokenFormBean());
	            actionForm.getTokenFormBean().setSearchFlag(GRTConstants.BLOCK);
	            actionForm.getTokenFormBean().setCountryList(PageCache.countrySelectMap);
	            actionForm.getTokenFormBean().setStateList(PageCache.stateSelectMap);
	            actionForm.getTokenFormBean().setProvinceList(PageCache.provinceSelectMap);

	            return Action.SUCCESS;
	        }

	        Account account = new Account();
	        account.setAccountId(result.getSAPId());
	        account.setName(result.getName());
	        account.setPrimaryAccountCity(result.getCity());
	        account.setPrimaryAccountCountry(result.getCountry());
	        account.setPrimaryAccountPostalCode(result.getZip());
	        account.setPrimaryAccountState(result.getRegion());
	        account.setPrimaryAccountStreetAddress(result.getStreet());
	        account.setRegion(result.getRegion());
	        account.setSoldToNumber(result.getAccountNumber());
	        actionForm.getTokenFormBean().setEndCoustomerAccount(account);

	        CSSPortalUser user = getUserFromSession();
	        if (GRTConstants.USER_TYPE_BP.equalsIgnoreCase(user.getUserType())){

	            BPAccountTempAccess bPAccountTempAccess = new BPAccountTempAccess();
	            bPAccountTempAccess.setAccountId(account.getAccountId());
	            bPAccountTempAccess.setAccountName(account.getName());


	            bPAccountTempAccess.setBpLinkId(user.getBpLinkId());
	            bPAccountTempAccess.setCreatedBy(user.getUserId());

	            try{
	                getAccountService().saveBPAccountTempAccess(bPAccountTempAccess, "", accountType);
	            }
	            catch (Exception exception)
	            {
	                logger.debug("Exception in granting tempaccess in token " + exception);
	            }
	        }
	        return showContractdate(actionForm.getTokenFormBean());
	    }
	 
	 private Result getSelectedRecord(String sapId, List<Result> results)
	 {

		 for (Result result1 : results)
		 {
			 if (result1.getSAPId().equals(sapId))
			 {
				 return result1;
			 }

		 }
		 return null;
	 }
	 
	 public String showContractdate(TokenFormBean form) throws Exception
	 {
		 validateSession();
		 setAllFlagNone(form);
		 form.setContractStartDate(new SimpleDateFormat("MM/dd/yyyy").format(getFirstOfNextMonth()
				 .getTime()));
		 form.setContractDateFlag(GRTConstants.BLOCK);
		 form.setAdditionalInfo(null);
		 form.setReplaceContract("No");
		 return Action.SUCCESS;
	 }

	@Override
	public String technicalRegistrationDashboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String installBaseCreation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String salGatewayMigrationList() {
		// TODO Auto-generated method stub
		return null;
	}

}
