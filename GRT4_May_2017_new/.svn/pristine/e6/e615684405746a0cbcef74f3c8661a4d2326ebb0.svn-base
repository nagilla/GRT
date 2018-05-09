package com.avaya.grt.service.account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.dao.account.AccountDao;
import com.avaya.grt.dao.cat.CatSoldToDao;
import com.avaya.grt.dao.cxp.CXPSoldToDao;
import com.avaya.grt.mappers.AccountCreation;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.FuzzySearchParam;
import com.avaya.grt.mappers.LogAccountUpdate;
import com.avaya.grt.mappers.TokenRedemption;
import com.avaya.grt.service.BaseRegistrationService;
import com.avaya.v1.accountupdate.AccountUpdateResponse;
import com.avaya.v1.address.AddressType;
import com.avaya.v1.addresssearch.AddressSearchRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateResponseType;
import com.avaya.v1.soldtoschemas.SoldToCreateRequestType;
import com.avaya.v1.soldtoschemas.SoldToCreateResponseType;
import com.avaya.v1.tokenredemption.TokenRedemptionRequestType;
import com.grt.dto.Account;
import com.grt.dto.BusinessPartner;
import com.grt.dto.Contact;
// Added for gift card 2.0
import com.grt.dto.Contract;
import com.grt.dto.Result;
import com.grt.dto.TokenRedemptionEmailDto;
import com.grt.dto.TokenRedemptionResponse;
import com.grt.dto.TokenResponseTypeDto;
import com.grt.integration.iposs.IpossClient;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.MedalLevelEnum;

public class AccountServiceImpl  extends BaseRegistrationService implements AccountService{

	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);
	
	private IpossClient ipossClient;
	private TechnicalRegistrationDao technicalRegistrationDao;
	
	private CatSoldToDao catSoldToDao;
	private CXPSoldToDao cxpSoldToDao;
	
	private String defaultGrantPeriodInDays;
	
	public AccountDao accountDao;
	
	
	public IpossClient getIpossClient() {
		return ipossClient;
	}

	public void setIpossClient(IpossClient ipossClient) {
		this.ipossClient = ipossClient;
	}
	
	
	public TechnicalRegistrationDao getTechnicalRegistrationDao() {
		return technicalRegistrationDao;
	}

	public void setTechnicalRegistrationDao(
			TechnicalRegistrationDao technicalRegistrationDao) {
		this.technicalRegistrationDao = technicalRegistrationDao;
	}

	public CatSoldToDao getCatSoldToDao() {
		return catSoldToDao;
	}

	public void setCatSoldToDao(CatSoldToDao catSoldToDao) {
		this.catSoldToDao = catSoldToDao;
	}

	public CXPSoldToDao getCxpSoldToDao() {
		return cxpSoldToDao;
	}

	public void setCxpSoldToDao(CXPSoldToDao cxpSoldToDao) {
		this.cxpSoldToDao = cxpSoldToDao;
	}
	
	public String getDefaultGrantPeriodInDays() {
		return defaultGrantPeriodInDays;
	}

	public void setDefaultGrantPeriodInDays(String defaultGrantPeriodInDays) {
		this.defaultGrantPeriodInDays = defaultGrantPeriodInDays;
	}	

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	/**
     * Method to query account along with its contacts information from Siebel
     *
     * @param soldToId
     * @return account Account
     * @throws Exception
     */
	@Override
    public Account queryAccountWithContacts(String soldToId) throws Exception {
        Account account = getSiebelClient().queryAccount(soldToId);
        if(account != null) {
        	List<Contact> contacts = getAccountDao().queryAccountContacts(soldToId);
        	if(contacts != null && contacts.size() > 0) {
        		account.setContacts(contacts);
        	}
        }
        return account;
    }
    
    /**
     * Method to query account information from Siebel
     *
     * @param soldToId
     * @return account Account
     * @throws Exception
     */
	@Override
    public Account queryAccount(String soldToId) throws Exception {
        Account account = getSiebelClient().queryAccount(soldToId);
        return account;
    }

	@Override
	 public AccountUpdateResponse createSSRForAccountUpdate(Account original, Account modified) throws Exception {
	    	return this.getIpossClient().createSSRForAccountUpdate(original,modified);
	 }
	 
	 /**
	  * Method to persist Account Update Log
	     * @param logAccountUpdate
	     * @throws Exception
	     */
	@Override
	public void saveAccountUpdateLog(LogAccountUpdate logAccountUpdate) throws Exception {
	    	logger.debug("Entering saveAccountUpdateLog");
	    	try {
	    		getTechnicalRegistrationDao().saveAccountUpdateLog(logAccountUpdate);
	    		if(!"Success".equalsIgnoreCase(logAccountUpdate.getStatus())) {
	    			logger.error("MONITOR:[Transaction Failure:AccountUpdate][AcountId:"+ logAccountUpdate.getAccountId() + "][Error:" + logAccountUpdate.getErrorMessage() + "][Log_Account_Update.Transaction_Id:" + logAccountUpdate.getTransactionId() + "]");
	    		}
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting saveAccountUpdateLog");
	    	}
	}
	
	@Override
	public List<String> querySoldToListAccess(String bpLinkId, List<String> soldToIdList) throws DataAccessException {
		logger.debug("Entering querySoldToListAccess : bpLinkId:" + bpLinkId);
		List<String> accessList = new ArrayList<String>();
    	try {
    		if(getCatSoldToDao() != null) {
    			accessList = getCatSoldToDao().querySoldToListAccess(bpLinkId, soldToIdList);
    		}
    	} catch (Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting querySoldToListAccess : bpLinkId:" + bpLinkId);
    	}
    	return accessList;
	}
	
	/**
	 * Method to get the FuzzySearchParam code. 
	 * 
	 * @return FuzzySearchParamList HashMap
	 */
	@Override
	public List<FuzzySearchParam> getFuzzySearchParams() throws DataAccessException{
		
		List<FuzzySearchParam> fuzzySearchParamList = getTechnicalRegistrationDao().getFuzzySearchParams();
		return fuzzySearchParamList;
	}
	 @Override   
	 public List<Result> getSearchResults (AddressSearchRequestType addressSearchRequestType) throws Exception{
	    	List<Result> resultList = getIpossClient().addressSearch(addressSearchRequestType);
	    	logger.debug("Total result found are " + resultList.size());
	    	return resultList;
	    }
	 
	/**
	 * Method to verify token. 
	 * 
	 * @param tokenNumber
	 * @return tokenResponseTypeDto TokenResponseTypeDto
	 * @throws Exception
	 */
	public TokenResponseTypeDto tokenVerification(String tokenNumber) throws Exception {
		 String returnCode = null;
		 TokenResponseTypeDto tokenResponseTypeDto = null ;
		 if(StringUtils.isNotEmpty(tokenNumber)) {
			 tokenResponseTypeDto = this.getIpossClient().tokenVerification(tokenNumber);
		 } else {
			 logger.warn("Passed tokenNumber is not valid.");
		 }
		 returnCode = tokenResponseTypeDto.getCode();
		 if(logger.isDebugEnabled())
			 logger.debug("In AccountService:tokenVerification:code: "+returnCode+" SoldTo Number: "
					 +tokenResponseTypeDto.getDistiSoldTo()+" SoldTo Name: "+tokenResponseTypeDto.getUserName()
					 +" service Code : "+tokenResponseTypeDto.getServiceType());

		 if (StringUtils.isNotEmpty(returnCode)) {
			 returnCode = GRTConstants.exception_errorcode;
		 }
		 return tokenResponseTypeDto;
	 }
	 
	/**
	 * Method to fetch regions. 
	 * 
	 * @param specification String
	 * @return regionList Map
	 * @throws Exception
	 */ 
	public Map<String, String> fetchRegions(String specification) throws Exception {
	    	logger.debug("Entering fetchRegions : specification:" + specification);
	    	Map<String, String> regionList = new TreeMap<String, String>();
	    	try {
	    		
	    		regionList = this.getTechnicalRegistrationDao().fetchRegions(specification);
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting fetchRegions : specification:" + specification);
	    	}
	    	return regionList;
	    }
	 
	/**
	 * Method to fetch account information for a given soldTo. 
	 * 
	 * @param soldToId String
	 * @return account Account
	 * @throws DataAccessException
	 */ 
	 public Account getAccountInfo(String soldToId) throws DataAccessException {
		 logger.debug("Entering getAccountInfo [soldTo:" + soldToId + "]");
		 Account account = null;
		 try {
			 account = getCxpSoldToDao().getAccountInfo(soldToId); 
		 } catch(DataAccessException dataAccessException) {
			 logger.error("", dataAccessException);
			 throw dataAccessException;
		 } finally {
			 logger.debug("Exiting getAccountInfo[soldTo:" + soldToId + "]");
		 }
		 return account;
	 }

	 /**
	  * Method to fetch medal Level. 
	  * 
	  * @param soldToId String
	  * @return medalLevel MedalLevelEnum
	  * @throws DataAccessException
	  */ 
	 public MedalLevelEnum getMedalLevel(String soldToId) throws DataAccessException {
		 logger.debug("Entering getMedalLevel for soldToId:" + soldToId);
		 MedalLevelEnum medalLevel = null;
		 try {
			 medalLevel = this.getTechnicalRegistrationDao().getMedalLevel(soldToId);
		 } catch (Throwable throwable) {
			 logger.error("", throwable);
			 throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
		 } finally {
			 logger.debug("Exiting getMedalLevel:" + medalLevel + " for soldToId:" + soldToId);
		 }
		 return medalLevel;
	 }
	 
	 /**
	  * Method to fetch single business partner for a given bpLinkId. 
	  * 
	  * @param bpLinkId String
	  * @return bp BusinessPartner
	  * @throws DataAccessException
	  */ 
	 public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException {
		 logger.debug("Entering getBusinessPartner in AccountService for bpLinkId:" + bpLinkId);
		 BusinessPartner bp = getBaseHibernateDao().getBusinessPartner(bpLinkId);
		 return bp;
	 }    

	 /**
	  * Method to fetch a List of business partners for a given bpLinkId. 
	  * 
	  * @param bpLinkId String
	  * @return bp List<BusinessPartner>
	  * @throws DataAccessException
	  */ 
	 public List<BusinessPartner> getBusinessPartners(String bpLinkId) throws DataAccessException {
		 logger.debug("Entering getBusinessPartner in AccountService for bpLinkId:" + bpLinkId);
		 List<BusinessPartner> bp = getBaseHibernateDao().getBusinessPartners(bpLinkId);
		 return bp;
	 }
	 
	 /**
	  * Method to persist Token Redemption Information
	  * @param tokenRedemption
	  * @throws Exception
	  */
	 public void saveTokenRedemption(TokenRedemption tokenRedemption) throws Exception {
		 logger.debug("Entering saveTokenRedemption");
		 try {
			 
			 this.getTechnicalRegistrationDao().saveTokenRedemption(tokenRedemption);
			 if(StringUtils.isEmpty(tokenRedemption.getContractNumber()) || tokenRedemption.getContractNumber().equals("-1")){
				 logger.error("MONITOR:[Transaction Failure:TokenRedemption][TokenNumber:"+ tokenRedemption.getTokenNumber() + "][Error:" + tokenRedemption.getResponseDescription() + "][Log_Token_Redemption.Token_Redemption_Id:" + tokenRedemption.getTokenRedemptionId()+ "]");
			 }
		 } catch (Throwable throwable) {
			 logger.error("", throwable);
		 } finally {
			 logger.debug("Exiting saveTokenRedemption");
		 }
	 }
	 
	 /**
	  * Method to validate ipo latest existance. 
	  * 
	  * @param accountId String
	  * @return bp List<BusinessPartner>
	  */ 
	 public String validateIPOLatestReleaseExistence(String accountId) {
	    	logger.debug("Entering validateIPOLatestReleaseExistence for accountId:" + accountId);
	    	String result = null;
	    	try {
	    		List<String> seCodeList = this.getTechnicalRegistrationDao().getRedemptionPrerequisiteSeCodes();
				if(seCodeList != null && seCodeList.size() > 0) {
					logger.debug("seCodeList: " + seCodeList);
					String maxRelease = this.getTechnicalRegistrationDao().getIPOAssetsMaximumRelease(accountId, seCodeList);
					logger.debug("Max Release :" + maxRelease);
					
					if(StringUtils.isNotEmpty(maxRelease)){											
						if(!maxRelease.startsWith("9.1")){
							result = GRTConstants.IP_OFFICE_OLDER_RELEASE;
						}						
					}
					else {
						result = GRTConstants.IP_OFFICE_NO_EQUIPMENT_ONBOARDED;
					}
				}
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting validateIPOLatestReleaseExistence for accountId:" + accountId);
	    	}
	    	return result;
	 }
	 
	 /**
	  * Method to save BP account. 
	  * 
	  * @param accountId BPAccountTempAccess
	  * @param serviceTerm String
	  * @param accountType String
	  * @return bpAccountTempAccess BPAccountTempAccess
	  * @throws DataAccessException
	  */ 
	 @Override
	 public BPAccountTempAccess saveBPAccountTempAccess(BPAccountTempAccess bpAccountTempAccess, String serviceTerm, String accountType) throws DataAccessException {
	    	logger.debug("Entering saveBPAccountTempAccess, serviceTerm:"+serviceTerm);
	    	try {
		        if(bpAccountTempAccess != null) {
		        	if(bpAccountTempAccess.getCreatedDate() == null) {
		        		bpAccountTempAccess.setCreatedDate(new Date());
		        	}
		        	if(bpAccountTempAccess.getExpiryDate() == null) {
		        		int grantDays = 15;
		        		try {
		        			String numberOfDays = defaultGrantPeriodInDays;
		        			if(StringUtils.isNotEmpty(numberOfDays)){
		        				grantDays = Integer.parseInt(numberOfDays);
		        			}
		        		} catch(Throwable throwable) {
		        			//Do nothing, default the grant for 15 days.
		        		}
		        		Calendar calendar = Calendar.getInstance();
		        		calendar.setTime(new Date());
		        		if(StringUtils.isNotEmpty(serviceTerm)){
		        			calendar.add(Calendar.MONTH, Integer.parseInt(serviceTerm));
		        		} else {
		        			calendar.add(Calendar.DATE, grantDays);
		        		}
		        		bpAccountTempAccess.setExpiryDate(calendar.getTime());
		        	}
		        	if(StringUtils.isEmpty(bpAccountTempAccess.getCreatedBy()) && this.getCurrentUserService() != null && StringUtils.isNotEmpty(this.getCurrentUserService().getUserId())) {
		        		bpAccountTempAccess.setCreatedBy(this.getCurrentUserService().getUserId());
		        	}
		        	getTechnicalRegistrationDao().saveBPAccountTempAccess(bpAccountTempAccess);
		        	try{
		        		List<String> soldToIdList = new ArrayList<String>();
		        		soldToIdList.add(bpAccountTempAccess.getAccountId());
		        		List<String> accessList = getCatSoldToDao().querySoldToListAccess(bpAccountTempAccess.getBpLinkId(), soldToIdList);
		        		boolean result = false;
		        		if (accessList.isEmpty() || accessList == null) {
			        		result = this.getIpossClient().assignAccess(bpAccountTempAccess.getBpLinkId(), bpAccountTempAccess.getAccountId(), bpAccountTempAccess.getAccountName(), serviceTerm, accountType);
		        		}	
		        		logger.debug("LOA upsert result:" + result + " for bpLinkId:" + bpAccountTempAccess.getBpLinkId() + ", soldToId:" + bpAccountTempAccess.getAccountId());
		        	}catch(Throwable throwable) { 
		        		logger.error("Error while upsert operation in CAT/LOA", throwable);
		        	}
		        }
	    	} catch(Throwable throwable) {
	    		throw new DataAccessException(AccountServiceImpl.class, throwable.getMessage(), throwable);
	    	} finally {
	    		logger.debug("Exiting saveBPAccountTempAccess");
	    	}
	        
	        return bpAccountTempAccess;
	 }
	 
	 /**
	  * Method to save BP account. 
	  * 
	  * @param soldToCreateRequestType SoldToCreateRequestType
	  * @param duplicateAccountReason String
	  * @param bpLinkId String
	  * @param bpName String
	  * @param loginName String
	  * @param loginEmail String
	  * @return soldToCreateResponseType SoldToCreateResponseType
	  * @throws Exception
	  */ 	 
	 @Override
	 public SoldToCreateResponseType sapSoldToAddressCall(SoldToCreateRequestType soldToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail) throws Exception{
	    	SoldToCreateResponseType soldToCreateResponseType = getIpossClient().sapSoldToAddressCall(soldToCreateRequestType, duplicateAccountReason, bpLinkId, bpName, loginName, loginEmail);
	    	logger.debug("Total result found are " + soldToCreateResponseType.getCode());
	    	return soldToCreateResponseType;
	  }
	 
	 /**
	  * Method to Save Account Creation Information - SoldTo/ShipTo
	  * @param accountCreation
	  * @throws Exception
	  */
	
	 @Override
	 public void saveAccountCreation(AccountCreation accountCreation) throws Exception {
	    	logger.debug("Entering saveAccountCreation");
	    	try {
	    		getTechnicalRegistrationDao().saveAccountCreation(accountCreation);
	    		if(StringUtils.isEmpty(accountCreation.getResponseCode()) || !accountCreation.getResponseCode().equals("1000")) {
	    			String logString = "MONITOR:[Transaction Failure:AccountCreation][TokenNumber:"+ accountCreation.getTokenNumber() + "][Error:" + accountCreation.getResponseDescription() + "][TARGET:TARGET_][Account Details:" + ToStringBuilder.reflectionToString(accountCreation,ToStringStyle.MULTI_LINE_STYLE) + "]";
	    			if(StringUtils.isNotEmpty(accountCreation.getResponseDescription())) {
	    				if(accountCreation.getResponseDescription().toUpperCase().contains("In Postal code a number must".toUpperCase()) || 
	    					accountCreation.getResponseDescription().toUpperCase().contains("Ship To does not exist in SAP".toUpperCase())) {
	    					logString=logString.replaceAll("TARGET_", "SIEBEL");
	    				} else if(accountCreation.getResponseDescription().toUpperCase().contains("No jurisdiction code could be determined".toUpperCase()) || 
	    					accountCreation.getResponseDescription().toUpperCase().contains("not correct in the VAT registration number".toUpperCase())) {
	    					logString=logString.replaceAll("TARGET_", "SAP");
	    				} else if(accountCreation.getResponseDescription().toUpperCase().contains("is not defined for country".toUpperCase())) {
	    					logString=logString.replaceAll("TARGET_", "GRT");
	    				} else if(accountCreation.getResponseDescription().toUpperCase().contains("Unknown Exception".toUpperCase())) {
	    					logString=logString.replaceAll("TARGET_", "FMW");
	    				} else {
	    					logString=logString.replaceAll("TARGET_", "GRT-PROJECT");
	    				}
	    			}
	    			logger.error(logString);
	    		}
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting saveAccountCreation");
	    	}
	  }
	 
	 /**
	  * Method to send Account Creation email notification. 
	  * 
	  * @param soldToOrShipToId String
	  * @param endCustomerName String
	  * @param endCustomerStreetName String
	  * @param endCustomerCity String
	  * @param endCustomerState String
	  * @param endCustomerPostalCode String
	  * @param endCustomerCountry String
	  * @param bpLinkId String
	  * @param email String
	  * @return email success response.
	  * @throws Exception
	  */ 	 
	 @Override
	 public String mailAccountCreationDetails(String soldToOrShipToId, String endCustomerName, String endCustomerStreetName, 
	    		String endCustomerCity, String endCustomerState, String endCustomerPostalCode, String endCustomerCountry, String bpLinkId, String email) throws Exception {
	    	logger.debug("Entering mailAccountCreationDetails..." + soldToOrShipToId);
	    	try {
	        	getMailUtil().sendMailNotificationAccountCreation(soldToOrShipToId, endCustomerName, endCustomerStreetName, 
	            		endCustomerCity, endCustomerState, endCustomerPostalCode, endCustomerCountry, bpLinkId, email);
		    } catch(Throwable throwable) {
	        	logger.error("Error while sending mail account creation", throwable);
	        	return GRTConstants.NO;
	        }
	    	logger.debug("Exiting mailAccountCreationDetails");
	    	return GRTConstants.YES;
	 }
	 
	 /**
	  * Method to save BP account. 
	  * 
	  * @param shipToCreateRequestType ShipToCreateRequestType
	  * @param duplicateAccountReason String
	  * @param bpLinkId String
	  * @param bpName String
	  * @param loginName String
	  * @param loginEmail String
	  * @return shipToCreateResponseType ShipToCreateResponseType
	  * @throws Exception
	  */ 	 
	 
	 @Override
	 public ShipToCreateResponseType sapShipToAddressCall (ShipToCreateRequestType shipToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail) throws Exception {
	    	//ShipToCreateResponseType shipToCreateResponseType = getGenericClient().sapShipToAddressCall(shipToCreateRequestType, duplicateAccountReason, bpLinkId, bpName, loginName, loginEmail);
	    	ShipToCreateResponseType shipToCreateResponseType = getIpossClient().sapShipToAddressCall(shipToCreateRequestType, duplicateAccountReason, bpLinkId, bpName, loginName, loginEmail);
	    	logger.debug("Total result found are " + shipToCreateResponseType.getCode());
	    	return shipToCreateResponseType;
	 }
	 
	 /**
	  * Method to get sales org. 
	  * 
	  * @param countryISOCode String
	  * @return salesOrg String
	  * @throws DataAccessException
	  */ 	 
	 @Override
	 public String getSalesOrg(String countryISOCode) throws DataAccessException {
	    	logger.debug("Entering getSalesOrg in AccountService");
	    	return getTechnicalRegistrationDao().getSalesOrg(countryISOCode);
	  }
	 
	 /**
	  * Method to redeem token. 
	  * 
	  * @param request TokenRedemptionRequestType
	  * @return tokenRedemptionResponse TokenRedemptionResponse
	  * @throws Exception
	  */ 
	 public TokenRedemptionResponse redeemToken(TokenRedemptionRequestType request) throws Exception {
	        return this.getIpossClient().redeemToken(request);
	    }
	 public String fetchOEFCMailIds(String countryCode) throws DataAccessException{
		 return this.getTechnicalRegistrationDao().fetchOEFCMailIds(countryCode);
	 }
	 
	 /**
	  * Method to get BP Account. 
	  * 
	  * @param bpLinkId String
	  * @return accountId String
	  * @throws Exception
	  */ 
	 public BPAccountTempAccess getBPAccountTempAccess(String bpLinkId, String accountId) throws Exception {
	    	
	    	List<BPAccountTempAccess> bpFromLOA =  this.queryAccess(bpLinkId, accountId);
	    	if(bpFromLOA != null && bpFromLOA.size() > 0) {
	    		return bpFromLOA.get(0);
	    	}
	        return null;
	    }
	 /**
	  * method to send email notification for account creation.
	  */
	 public void sendMailNotification(TokenRedemptionRequestType request, String contractNumber, Calendar contractStartDate, String additionalInfo, TokenResponseTypeDto tokenDetails,String emails) {
		 logger.debug("Entering sendMailNotification in AccountService");
		 TokenRedemptionEmailDto tokenRedemptionEmailDto = new TokenRedemptionEmailDto();
		 tokenRedemptionEmailDto.setTokenNumber(request.getTokenNumber());
		 tokenRedemptionEmailDto.setRedeemerBPName(request.getRedeemerBPName());
		 tokenRedemptionEmailDto.setRedeemerBPLinkID(request.getRedeemerBPLinkID());
		 tokenRedemptionEmailDto.setEndCustomerShipTo(request.getEndCustomerShipTo());
		 tokenRedemptionEmailDto.setEndCustomerName(request.getEndCustomerName());
		 tokenRedemptionEmailDto.setEndCustomerEmail(request.getEndCustomerEmail());
		 tokenRedemptionEmailDto.setRedeemerEmail(request.getRedeemerEmail());
		 tokenRedemptionEmailDto.setRedeemerName(request.getRedeemerName());

		 AddressType address = request.getEndCustomerShipToAddress();

		 tokenRedemptionEmailDto.setStreet1(address.getStreet1());
		 tokenRedemptionEmailDto.setStreet2(address.getStreet2());
		 tokenRedemptionEmailDto.setCountry(address.getCountry());
		 tokenRedemptionEmailDto.setCity(address.getCity());
		 tokenRedemptionEmailDto.setState(address.getState());
		 tokenRedemptionEmailDto.setZip(address.getZip());
		 tokenRedemptionEmailDto.setRegion(address.getRegion());
		 tokenRedemptionEmailDto.setState_Full(address.getState_Full());

		 try {
			 getMailUtil().sendMailNotification(tokenRedemptionEmailDto, contractNumber, contractStartDate, additionalInfo, tokenDetails, emails);
			 
			
		 } catch(Throwable throwable) {
			 logger.error("Error while sending mail account creation", throwable);
		 }
		 logger.debug("Exiting mailAccountCreationDetails");
	 }
	
	 /**
	  *  Added for gift card 2.0.
	  *  Method to find contracts. 
	  * 
	  * @param bpLinkId String
	  * @return credSolutionName String
	  */ 
		public boolean isBPAuthorizedToRedeem(String bpLinkId, String credSolutionName) {
			boolean bpAuthorized = false;
			try {
				bpAuthorized = this.getIpossClient().bpAuthorizationCheck(bpLinkId, credSolutionName);
			} catch (Exception e) {
				logger.error("Exception in isBPAuthorizedToRedeem.");
				e.printStackTrace();
			}
		    return bpAuthorized;//Integer.parseInt(bpLinkId)>2000;	    	
		}

		 /**
		  * Method to find contracts. 
		  * 
		  * @param endCustomerShipTo String
		  * @return superUser boolean
		  */ 
		
		public List<Contract> findContracts(String endCustomerShipTo, boolean superUser) {
			List<Contract> contracts = null;
			try {
				contracts = this.getIpossClient().contractLookup(endCustomerShipTo);
			} catch (Exception e) {
				logger.error("Exception in findContracts.");
				e.printStackTrace();
			}
			if(contracts != null){
				if(contracts.get(0) != null && StringUtils.isNotEmpty(contracts.get(0).getReturnCode())){
					logger.debug("Return Code:"+contracts.get(0).getReturnCode());
				} else if(!superUser){
					for(Contract contract:contracts) {
						String contractType = contract.getContractType()!=null?contract.getContractType().toUpperCase():null;
						if(StringUtils.isNotEmpty(contractType)) {
						     contract.setCodelivary("CDS".equals(contractType) || "CDM".equals(contractType));
						     contract.setWholeSale("WSS".equals(contractType) || "WSM".equals(contractType));
						     contract.setRetail("RTS".equals(contractType) || "RTM".equals(contractType));
						     contract.setDirect("DRS".equals(contractType) || "DRM".equals(contractType));
						}
					}
				}
				Collections.sort(contracts);
			}
			return contracts;
		}
		
}
