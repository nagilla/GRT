package com.avaya.grt.service.account;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.avaya.grt.mappers.AccountCreation;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.FuzzySearchParam;
import com.avaya.grt.mappers.LogAccountUpdate;
import com.avaya.grt.mappers.TokenRedemption;
import com.avaya.v1.accountupdate.AccountUpdateResponse;
import com.avaya.v1.addresssearch.AddressSearchRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateResponseType;
import com.avaya.v1.soldtoschemas.SoldToCreateRequestType;
import com.avaya.v1.soldtoschemas.SoldToCreateResponseType;
import com.avaya.v1.tokenredemption.TokenRedemptionRequestType;
import com.grt.dto.Account;
import com.grt.dto.BusinessPartner;
// Added for gift card 2.0
import com.grt.dto.Contract;
import com.grt.dto.Result;
import com.grt.dto.TokenRedemptionResponse;
import com.grt.dto.TokenResponseTypeDto;
import com.grt.util.DataAccessException;
import com.grt.util.MedalLevelEnum;


public interface AccountService {
	 public AccountUpdateResponse createSSRForAccountUpdate(Account original, Account modified) throws Exception;
	 public void saveAccountUpdateLog(LogAccountUpdate logAccountUpdate) throws Exception;
	 public List<String> querySoldToListAccess(String bpLinkId, List<String> soldToIdList) throws DataAccessException;
	 public List<FuzzySearchParam> getFuzzySearchParams() throws DataAccessException;
	 public List<Result> getSearchResults (AddressSearchRequestType addressSearchRequestType) throws Exception;
	 public BPAccountTempAccess saveBPAccountTempAccess(BPAccountTempAccess bpAccountTempAccess, String serviceTerm, String accountType) throws DataAccessException;
	 public SoldToCreateResponseType sapSoldToAddressCall(SoldToCreateRequestType soldToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail) throws Exception;
	 public void saveAccountCreation(AccountCreation accountCreation) throws Exception;
	 public ShipToCreateResponseType sapShipToAddressCall (ShipToCreateRequestType shipToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail) throws Exception;
	 public String mailAccountCreationDetails(String soldToOrShipToId, String endCustomerName, String endCustomerStreetName, 
	    		String endCustomerCity, String endCustomerState, String endCustomerPostalCode, String endCustomerCountry, String bpLinkId, String email) throws Exception;
	 public String getSalesOrg(String countryISOCode) throws DataAccessException;
	 public Account queryAccountWithContacts(String soldToId) throws Exception;
	 public Account queryAccount(String soldToId) throws Exception;
	 
	 public TokenResponseTypeDto tokenVerification(String tokenNumber) throws Exception;
	 public Map<String, String> fetchRegions(String specification) throws Exception;
	 public Account getAccountInfo(String soldToId) throws DataAccessException;
	 public MedalLevelEnum getMedalLevel(String soldToId) throws DataAccessException;
	 public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException;
	 public List<BusinessPartner> getBusinessPartners(String bpLinkId) throws DataAccessException;
	 public void saveTokenRedemption(TokenRedemption tokenRedemption) throws Exception;
	 public String validateIPOLatestReleaseExistence(String accountId);
	 public TokenRedemptionResponse redeemToken(TokenRedemptionRequestType request) throws Exception;
	 public String fetchOEFCMailIds(String countryCode) throws DataAccessException;
	 public void sendMailNotification(TokenRedemptionRequestType request, String contractNumber, Calendar contractStartDate, String additionalInfo, TokenResponseTypeDto tokenDetails,String emails);
	 public BPAccountTempAccess getBPAccountTempAccess(String bpLinkId, String accountId) throws Exception;
// Added for gift card 2.0
	 public boolean isBPAuthorizedToRedeem(String bpLinkId,String credSolutionName);
	 public List<Contract> findContracts(String endCustomerShipTo, boolean superUser);
		
}
