package com.grt.integration.iposs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
// Added for gift card 2.0
import org.example.bpauthorizationcheck.BPAuthorizationCheckRequest;
import org.example.bpauthorizationcheck.BPAuthorizationCheckResponse;
import org.example.contractlookup.ContractLookUpRequest;
import org.example.contractlookup.ContractLookUpResponse;
import org.example.contractlookup.ContractLookUpResponseType;

import com.avaya.v1.accountupdate.AccountUpdateRequest;
import com.avaya.v1.accountupdate.AccountUpdateResponse;
import com.avaya.v1.addresssearch.AddressSearchRequestType;
import com.avaya.v1.addresssearch.AddressSearchResponseType;
import com.avaya.v1.addresssearch.Request;
import com.avaya.v1.addresssearch.ResultType;
import com.avaya.v1.esrpermissionlist.LOAUpsertRequestType;
import com.avaya.v1.esrpermissionlist.LOAUpsertResponseType;
import com.avaya.v1.esrpermissionlist.PermissionStatus;
import com.avaya.v1.esrpermissionlist.PermissionType;
import com.avaya.v1.shiptoschemas.ShipToCreateRequestType;
import com.avaya.v1.shiptoschemas.ShipToCreateResponseType;
import com.avaya.v1.soldtoschemas.SoldToCreateRequestType;
import com.avaya.v1.soldtoschemas.SoldToCreateResponseType;
import com.avaya.v1.tokenredemption.TokenRedemptionRequestType;
import com.avaya.v1.tokenredemption.TokenRedemptionResponseType;
import com.avaya.v1.tokenschemas.ServiceDetailsType;
import com.avaya.v1.tokenschemas.TokenDetailsByTokenNumberRequestType;
import com.avaya.v1.tokenschemas.TokenDetailsType;
import com.avaya.v1.tokenschemas.TokenResponseType;
// Added for gift card 2.0
import com.grt.dto.Contract;
import com.grt.dto.Result;
import com.grt.dto.ServiceDetails;
import com.grt.dto.TokenDetails;
import com.grt.dto.TokenRedemptionResponse;
import com.grt.dto.TokenResponseTypeDto;
// Added for gift card 2.0
import com.grt.integration.BPAuthorizationService.fmwclientproxy.BPAuthorizationCheck;
import com.grt.integration.BPAuthorizationService.fmwclientproxy.BPAuthorizationCheckSOAPQSService_Impl;
import com.grt.integration.ContractViewSIService.fmwclientproxy.ContractLookUpSOAP;
import com.grt.integration.ContractViewSIService.fmwclientproxy.ContractLookUpSOAPQSService_Impl;

import com.grt.integration.accountcreation.fmwclientproxy.AddressSearchWSDL_PortType;
import com.grt.integration.accountcreation.fmwclientproxy.AddressSearchWSDL_Service_Impl;
import com.grt.integration.accountcreation.fmwclientproxy.TokenSearch;
import com.grt.integration.accountcreation.fmwclientproxy.TokenSearchByTokenByTokenNumber_Impl;
import com.grt.integration.accountupdate.fmwclientproxy.AccountUpdateServicePortType;
import com.grt.integration.accountupdate.fmwclientproxy.AccountUpdateServiceService_Impl;
import com.grt.integration.loabinding.fmwclientproxy.LOA;
import com.grt.integration.loabinding.fmwclientproxy.LOABindingQSService_Impl;
import com.grt.integration.sap.SapClient;
import com.grt.integration.shiptocreate.fmwclientproxy.ShipToCreateWSDL_PortType;
import com.grt.integration.shiptocreate.fmwclientproxy.ShipToCreateWSDL_Service_Impl;
import com.grt.dto.Account;
import com.grt.integration.soldtocreate.fmwclientproxy.SoldToCreateWSDL_PortType;
import com.grt.integration.soldtocreate.fmwclientproxy.SoldToCreateWSDL_Service_Impl;
import com.grt.integration.tokenredemption.fmwclientproxy.TokenRedemption_PortType;
import com.grt.integration.tokenredemption.fmwclientproxy.TokenRedemption_Service_Impl;
import com.grt.util.GRTConstants;
import com.grt.util.GrtSapException;
import com.grt.util.MedalLevelEnum;
import com.grt.util.PasswordUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class IpossClient {
	private static final Logger logger = Logger.getLogger(IpossClient.class);
	
	private int maximumRetry;
	private int connectionTimeOut;
    private String alsbUser;
    private String alsbPasswd;
    private String tokenSearchEndPoint;
	private String tokenRedemptionEndPoint;
	private String addressSearchEndPoint;
	private String shipToAddressEndPoint;
	private String soldToAddressEndPoint;
	private String sfdcSSRCreationEndPoint;
	private String loaEndPoint;
	// Added for gift card 2.0
	private String bpAuthEndPoint;
	private String contractLookupEndPoint;
	private PasswordUtil passwordUtils;
	private String artConnectionTimeOut;
	
    
	TokenResponseTypeDto tokenResponseTypeDto = new TokenResponseTypeDto();

	private void setRemoteEndPoint(Stub stub, String url) throws Exception {
		logger.debug("Setting for URL [" + url + "]");
		logger.debug("Stub:" + stub);
		

		stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
		stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, getAlsbUser());
		stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, decryptAlsbPasswd());
	}
	
	public TokenResponseTypeDto tokenVerification(String tokenNumber)
	throws Exception {
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger
				.debug("................ Starting for Token Verification ................");
		String tokenSearchURL = null;
		TokenResponseType tokenResponseType = null;
		TokenSearch port = null;
		TokenDetailsByTokenNumberRequestType tokenDetailsByTokenNumberRequestType = null;
		
		int retry_count = 0;
		String returnCode = null;
		try {
			port = new TokenSearchByTokenByTokenNumber_Impl()
					.getTokenSearchPort();
			logger.debug("----------PorT---------" + port.toString());
		} catch (ServiceException serviceException) {
			// TODO Auto-generated catch block
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw new GrtSapException(errMsg);
		}
		try {
			//tokenSearchURL = GRTPropertiesUtil.getProperty("tokensearch_endpoint_url");
			tokenSearchURL = getTokenSearchEndPoint();
		//	tokenSearchURL = GRTPropertiesUtil
		//			.getProperty("tokensearchuat_endpoint_url");
			setRemoteEndPoint((Stub) port, tokenSearchURL);
		} catch (Exception e) {
			String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
					+ e.getMessage();
			logger.error("ERROR: " + errMsg, e);
			throw new GrtSapException(errMsg);
		}
		// Prepare inputs and calling sap
		if (tokenNumber != null) {
			try {
				logger.debug("Prepare inputs: tokenNumber: " + tokenNumber);
				tokenDetailsByTokenNumberRequestType = new TokenDetailsByTokenNumberRequestType();
				tokenDetailsByTokenNumberRequestType
						.setTokenNumber(tokenNumber);
			} catch (Exception e) {
				logger.debug("Exception while preparing the input parameters",
						e);
				throw e;
			}
			logger.debug("Retrying logic");
			try {
				retry_count = getMaximumRetry();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR: While getting the property retry_count");
				e.printStackTrace();
				throw new GrtSapException(e);
			}
			for (int i = 0; i < retry_count; i++) {
				try {
					long ca1 = Calendar.getInstance().getTimeInMillis();
					logger.debug("Calling SAP for Token Number");
					tokenResponseType = port
							.tokenSearchByTokenNumberOperation(tokenDetailsByTokenNumberRequestType);
					if (tokenResponseType != null) {
						logger
								.debug("Fine till fetching tokenResponseType, tokencode: "
										+ tokenResponseType.getCode());
						logger
								.debug("Fine till fetching tokenResponseType, soldto: "
										+ tokenResponseType.getDistiSoldTo());
		
						tokenResponseTypeDto = convertTokenResponse(tokenResponseType);
		
						tokenResponseTypeDto.setDistiSoldTo(tokenResponseType
								.getDistiSoldTo());
						// tokenResponseTypeDto.setUserName(tokenResponseType.getTokenData()[0].getRedeemerUserName());
						tokenResponseTypeDto.setCode(tokenResponseType
								.getCode());
		
						if ("0".equals(tokenResponseType.getCode())) {
							TokenDetailsType[] tokenDetailsType = tokenResponseType
									.getTokenData();
							tokenResponseTypeDto.setStatus(tokenDetailsType[0]
									.getStatus());
						}
		
						returnCode = tokenResponseType.getCode();
					} else {
						returnCode = "1110";
					}
					long ca2 = Calendar.getInstance().getTimeInMillis();
					logger.debug("TIMER:" + (ca2 - ca1) + " milliseconds");
					if (returnCode != null) {
						logger
								.debug("Token Verification call ended and return code determined, Total time taken:");
						// return returnCode;
					}
					i=retry_count;
				} catch (IOException ioe) {
					// TODO: handle exception
					String errMsg = "Failed to connect FMW for Record Validation";
					logger.error(errMsg);
					if (i == retry_count - 1) {
						logger.error(errMsg, ioe);
						// returnCode =GRTConstants.fmw_errorCode;
					}
				} catch (SOAPFaultException e) {
					String errMsg = "Failed when sending data to SAP";
					logger.error(errMsg, e);
					throw e;
				}
			}
		}
		logger.debug("tokenResponseTypeDto.getUserName() in IpossClient :"
				+ tokenResponseTypeDto.getUserName());
		logger.debug("tokenResponseTypeDto.getDistiSoldTo() in IpossClient :"
				+ tokenResponseTypeDto.getDistiSoldTo());
		logger.debug("tokenResponseTypeDto.getCode() in IpossClient :"
				+ tokenResponseTypeDto.getCode());
		logger.debug("tokenResponseTypeDto.getStatus() in IpossClient :"
				+ tokenResponseTypeDto.getStatus());
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger
				.debug("TIMER for SAP transaction for token verification for Tokenid :"
						+ tokenNumber + " time in milliseconds :" + (c2 - c1));
		logger.debug("------ReturnCode------" + returnCode);
		logger
				.debug("................ Exiting Token Search in SAP client Total time ................ ");
		if (returnCode == null) {
			returnCode = GRTConstants.fmw_errorCode;
		}
		return tokenResponseTypeDto;
	}

	private TokenResponseTypeDto convertTokenResponse(TokenResponseType response){

		TokenResponseTypeDto responTypeDto = null;

		if (response!=null){
			responTypeDto = new TokenResponseTypeDto();
			responTypeDto.setCode(response.getCode());
			responTypeDto.setDescription(response.getDescription());
			responTypeDto.setDistiBPEmail(response.getDistiBPEmail());
			responTypeDto.setDistiBPLinkId(response.getDistiBPLinkId());
			responTypeDto.setDistiBPName(response.getDistiBPName());
			responTypeDto.setDistiSoldTo(response.getDistiSoldTo());
			responTypeDto.setEmail(response.getEmail());
			responTypeDto.setMaintenancePartnerBPLinkId(response.getMaintenancePartnerBPLinkId());
			responTypeDto.setOfferName(response.getOfferName());
			responTypeDto.setPayer(response.getPayer());
			responTypeDto.setPOFile(response.getPOFile());
			responTypeDto.setPONumber(response.getPONumber());
			responTypeDto.setRequesterDistributionChannel(response.getRequesterDistributionChannel());
			responTypeDto.setRequesterDivision(response.getRequesterDivision());
			responTypeDto.setRequesterSalesOrg(response.getRequesterSalesOrg());
			responTypeDto.setSalesOrderNo(response.getSalesOrderNo());
			responTypeDto.setServiceTerm(response.getServiceTerm());
			responTypeDto.setServiceType(response.getServiceType());
			responTypeDto.setSourceSystem(response.getSourceSystem());
			responTypeDto.setSQRN(response.getSQRN());
			responTypeDto.setTokendata(convertTokenDetails(response.getTokenData()));
			responTypeDto.setTransactionId(response.getTransactionId());
			responTypeDto.setUserName(response.getUserName());
            responTypeDto.setMedaLevel(MedalLevelEnum.getMedalLevelById(response.getMedalLevel()));

		}
		return responTypeDto;

	}
	
	private TokenDetails [] convertTokenDetails (TokenDetailsType[] tokenDetailsTypes)
	{
		TokenDetails [] tokenDetails = null;
		if (tokenDetailsTypes!=null){
			tokenDetails = new TokenDetails [tokenDetailsTypes.length];
			for (int i = 0;i< tokenDetailsTypes.length;i++){

				TokenDetailsType detailsType =	tokenDetailsTypes[i];
				TokenDetails tokDetails = new TokenDetails();

				tokDetails.setActivationEmail(detailsType.getActivationEmail());
				tokDetails.setAutoRenew(detailsType.getAutoRenew());
				tokDetails.setConfigDate(detailsType.getConfigDate());
				tokDetails.setConfirmationEmail(detailsType.getConfirmationEmail());
				tokDetails.setContractNumber(detailsType.getContractNumber());
				tokDetails.setContractStartDate(detailsType.getContractStartDate());
				tokDetails.setEndCustomerEmail(detailsType.getEndCustomerEmail());
				tokDetails.setEndCustomerId(detailsType.getEndCustomerId());
				tokDetails.setEndCustomerShipTo(detailsType.getEndCustomerShipTo());
				tokDetails.setEndCustomerSoldTo(detailsType.getEndCustomerSoldTo());
				tokDetails.setGovernmentUsage(detailsType.getGovernmentUsage());
				tokDetails.setRedeemerBPLinkId(detailsType.getRedeemerBPLinkId());
				tokDetails.setRedeemerBPName(detailsType.getRedeemerBPName());
				tokDetails.setRedeemerEmail(detailsType.getRedeemerEmail());
				tokDetails.setRedeemerName(detailsType.getRedeemerName());
				tokDetails.setRedeemerUserName(detailsType.getRedeemerUserName());
				tokDetails.setRedeemingPartnerShipTo(detailsType.getRedeemingPartnerShipTo());
				tokDetails.setRedeemingPartnerSoldTo(detailsType.getRedeemingPartnerSoldTo());
				tokDetails.setRedemptionDate(detailsType.getRedemptionDate());
				tokDetails.setSerialNumber(detailsType.getSerialNumber());
				tokDetails.setServiceDetails(convertServiceDetailsType(detailsType.getServiceDetails()));
				tokDetails.setShipTo(detailsType.getShipTo());
				tokDetails.setStatus(detailsType.getStatus());
				tokDetails.setTokenNumber(detailsType.getTokenNumber());
				tokenDetails[i] = tokDetails;
			}
		}


		return tokenDetails;
	}
	
	private ServiceDetails [] convertServiceDetailsType(ServiceDetailsType [] detailsTypes){


		ServiceDetails [] serviceDetails = null;
		if (detailsTypes !=null){
			serviceDetails = new ServiceDetails [detailsTypes.length];

			for (int i = 0;i<detailsTypes.length;i++){
				ServiceDetails seDetails = new ServiceDetails();
				seDetails.setDiscountPrice(detailsTypes[i].getDiscountPrice());
				seDetails.setItemNumber(detailsTypes[i].getItemNumber());
				seDetails.setListPrice(detailsTypes[i].getListPrice());
				seDetails.setNetPrice(detailsTypes[i].getNetPrice());
				seDetails.setQuantity(detailsTypes[i].getQuantity());
				seDetails.setServiceCode(detailsTypes[i].getServiceCode());
				seDetails.setMaterialCodeDescription(detailsTypes[i].getMaterialCodeDescription());
				seDetails.setServiceCodeDescription(detailsTypes[i].getServiceCodeDescription());
				seDetails.setMaterialCode(detailsTypes[i].getMaterialCode());
				serviceDetails[i] = seDetails;

			}

		}


		return serviceDetails;
	}
	
	public TokenRedemptionResponse redeemToken(TokenRedemptionRequestType request) throws Exception
    {
		logger.debug("START IPOSSClient redeemToken:"+request.getTokenNumber());
        long c1 = Calendar.getInstance().getTimeInMillis();

        TokenRedemption_PortType port = getTokenRedemptionPort();
        String tokenRedemptionURL = null;
        TokenRedemptionResponse tokenResponse = null;
        int retry_count = 0;

        try
        {
            //tokenRedemptionURL = GRTPropertiesUtil.getProperty("tokenredemption_endpoint_url").trim();
        	tokenRedemptionURL = getTokenRedemptionEndPoint();
            setRemoteEndPoint((Stub) port, tokenRedemptionURL);

        }
        catch (Exception e)
        {
            String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
                + e.getMessage();
            logger.error("ERROR: "+ errMsg, e);
            throw new GrtSapException(errMsg);
        }

        try
        {
            retry_count = getMaximumRetry();
        }
        catch (Exception e)
        {
            logger.error("ERROR: While getting the property retry_count");
            e.printStackTrace();
            throw new GrtSapException(e);
        }
		// Commnted Retry mechanism
		int i = 0;
        //for (int i = 0; i < retry_count; i++) {
        try {

            logger.debug("Calling FMW for Token redemption:"+request.getTokenNumber());

            TokenRedemptionResponseType response = port.tokenRedemptionOperation(request);
            tokenResponse = convertTokenRedemptionResponse(response);

            logger.debug("Token:"+request.getTokenNumber()+"   Response Code : "+tokenResponse.getCode());
            logger.debug("Token:"+request.getTokenNumber()+"   Response ContractNumber : "+tokenResponse.getContractNumber());
            logger.debug("Token:"+request.getTokenNumber()+"   Response Description : "+tokenResponse.getDescription());
            logger.debug("Token:"+request.getTokenNumber()+"   Response ContractStartDate :"+ tokenResponse.getContractStartDate());

            i=retry_count;
        } catch (IOException ioe) {
            // TODO: handle exception
            String errMsg = "Failed to connect FMW for redeemToken";
			logger.error("IOException in IPOSSClient redeemToken:"+request.getTokenNumber());
            logger.error(errMsg);
            if (i == retry_count - 1) {
                logger.error(errMsg, ioe);
                throw ioe;
            }
        } catch (SOAPFaultException e) {
            String errMsg = "Failed when sending data to redeemToken FMW Service";
            logger.error("SOAPFaultException in IPOSSClient redeemToken:"+request.getTokenNumber()+"  e.getMessage() :"+e.getMessage());
            logger.error(errMsg, e);
            throw e;
        } catch (Exception e) {
            logger.error("e.getMessage() :"+e.getMessage());
            throw e;
        }

        //}

        logger.debug("TIMER for fmw transaction for redeemToken:"+request.getTokenNumber()+"  time in milliseconds :" + (Calendar.getInstance().getTimeInMillis() - c1));
		logger.debug("END IPOSSClient redeemToken:"+request.getTokenNumber());
        return tokenResponse;
    }

	private TokenRedemption_PortType getTokenRedemptionPort() throws Exception {

        try{
        return  new TokenRedemption_Service_Impl().getTokenRedemptionPort();
        }catch (Exception serviceException) {
            String errMsg = "Failure when getting the port type from the service locator";
            logger.error("ERROR:" + errMsg, serviceException);
            throw new GrtSapException(serviceException);
        }
    }
	
	private TokenRedemptionResponse convertTokenRedemptionResponse(TokenRedemptionResponseType response){
        logger.debug("Entering convertTokenRedemptionResponse");
        TokenRedemptionResponse  response1 = null;
        if (response != null){
            response1 = new TokenRedemptionResponse();
            response1.setCode(response.getCode());
            response1.setContractNumber(response.getContractNumber());
            response1.setContractStartDate(response1.getContractStartDate());
            response1.setDescription(response.getDescription());
        }
        logger.debug("Exiting convertTokenRedemptionResponse");
        return response1;
    }
	
	public List<Result> addressSearch(AddressSearchRequestType addressSearchRequestType)
		throws Exception {
	
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger
				.debug("................ Starting Address Search ...............");
		
		AddressSearchWSDL_PortType addressSearchPort = null;
		String addressSerachURL = null;
		AddressSearchResponseType addressSearchResponseType = null;
		List <Result> resultList = new ArrayList<Result>();
		
		int retry_count = 0;
		String returnCode = null;
		
		Request[] reqArray = addressSearchRequestType.getRequest();
		for (Request req : reqArray) {
			logger.debug(req.getName() +" : " + req.getValue() + " : "+ req.getNameThreshhold() +" : "+ req.getNameWeight());
		}
		logger.debug(" addressSearchRequestType.getMinThreshhold()"  +" : " +addressSearchRequestType.getMinThreshhold());
		logger.debug(" addressSearchRequestType.getTopRecords()"  +" : " +addressSearchRequestType.getTopRecords());
		
		try {
			addressSearchPort = new AddressSearchWSDL_Service_Impl()
					.getAddressSearchWSDLSOAP();
			logger.debug("----------PorT---------"
					+ addressSearchPort.toString());
		} catch (ServiceException serviceException) {
			// TODO Auto-generated catch block
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw new GrtSapException(errMsg);
		}
		
		try {
			//addressSerachURL = GRTPropertiesUtil.getProperty("addressearch_endpoint_url").trim();
			addressSerachURL = getAddressSearchEndPoint();
			setRemoteEndPoint((Stub) addressSearchPort, addressSerachURL);
		
		} catch (Exception e) {
			String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
					+ e.getMessage();
			logger.error("ERROR: " + errMsg, e);
			throw new GrtSapException(errMsg);
		}
		
		logger.debug("Retrying logic");
		
		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: While getting the property retry_count");
			e.printStackTrace();
			throw new GrtSapException(e);
		}
		
		for (int i = 0; i < retry_count; i++) {
			try {
		
				logger.debug("Calling FMW with these addressSearchRequestType : ##############");
		
				logger.debug("Calling FMW for Address Search");
				long ca1 = Calendar.getInstance().getTimeInMillis();
				addressSearchResponseType = addressSearchPort.addressSearch(addressSearchRequestType);
		
				if (addressSearchResponseType != null) {
		
					if (addressSearchResponseType.getTransactionResponse() != null){
						returnCode = addressSearchResponseType.getTransactionResponse()[0].getCode();
						logger.debug("TransactionResponse Code "+ addressSearchResponseType.getTransactionResponse()[0].getCode());
		
						logger.debug("TransactionResponse Description "+ addressSearchResponseType.getTransactionResponse()[0].getDescription());
					}
		
					if (addressSearchResponseType.getResult() != null) {
						logger.debug("Number of result found "+ addressSearchResponseType.getResult().length);
						resultList = convertResponse(addressSearchResponseType.getResult());
					} else {
						logger.debug("No Results found ########");
						logger.debug("addressSearchResponseType.getTransactionResponse()[0].getDescription()"+addressSearchResponseType.getTransactionResponse()[0].getDescription());
					}
		
				} else {
					returnCode = "1110";
				}
				long ca2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("TIMER:" + (ca2 - ca1) + " milliseconds");
		
				i=retry_count;
			} catch (IOException ioe) {
				// TODO: handle exception
				String errMsg = "Failed to connect FMW for address search";
				logger.error(errMsg);
				if (i == retry_count - 1) {
					logger.error(errMsg, ioe);
					throw ioe;
				}
			} catch (SOAPFaultException e) {
				String errMsg = "Failed when sending data to FMW Service";
				logger.error("e.getMessage() :"+e.getMessage());
				logger.error(errMsg, e);
				throw e;
			} catch (Exception e) {
				logger.error("e.getMessage() :"+e.getMessage());
				throw e;
			}
		}
		
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for fmw transaction for addresssearch  time in milliseconds :" + (c2 - c1));
		logger.debug("------ReturnCode------" + returnCode);
		logger.debug("................ Exiting Address Search in generic client Total time ................ ");
		if (returnCode == null) {
			returnCode = GRTConstants.fmw_errorCode;
		}
		
		return resultList;
	}

	private List<Result> convertResponse (ResultType[] resultTypes){
		List <Result> resuList = new ArrayList<Result>();

		for (ResultType resultType : resultTypes){
			Result result = new Result();
			result.setCity(resultType.getCity());
			result.setCountry(resultType.getCountry());
			result.setName(resultType.getName());
			result.setRegion(resultType.getRegion());
			result.setZip(resultType.getZip());
			result.setSAPId(resultType.getSAPId());
			result.setScore(resultType.getScore().doubleValue());
			result.setSource(resultType.getSource());
			result.setStreet(resultType.getStreet());
			result.setAccountNumber(resultType.getSAPId());
			resuList.add(result);
		}

		return resuList;

	}
	
	public ShipToCreateResponseType sapShipToAddressCall(ShipToCreateRequestType shipToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail)
		throws Exception {

		long c1 = Calendar.getInstance().getTimeInMillis();
		logger
				.debug("................ Starting Address Search ...............");

		ShipToCreateWSDL_PortType shipToCreatePort = null;
		ShipToCreateResponseType shipToCreateResponseType = null;
		String shipToCreateURL = null;

		int retry_count = 0;
		String returnCode = null;

		logger.debug("shipToCreateRequestType : shipToCreateRequestType.getDistiSoldTo(): "+shipToCreateRequestType.getDistiSoldTo()
				+ ", shipToCreateRequestType.getShipToName1: "+ shipToCreateRequestType.getShipToName1()
				+ ", shipToCreateRequestType.getShipToAddress.getCity(): "+shipToCreateRequestType.getShipToAddress().getCity()
				+ ", shipToCreateRequestType.getShipToAddress.getStreet1(): "+shipToCreateRequestType.getShipToAddress().getStreet1()
				+ ", shipToCreateRequestType.getShipToAddress.getZip(): "+shipToCreateRequestType.getShipToAddress().getZip()
				+ ", shipToCreateRequestType.getShipToAddress.getRegion(): "+shipToCreateRequestType.getShipToAddress().getRegion()
				+ ", shipToCreateRequestType.getShipToAddress().getState_Full(): "+shipToCreateRequestType.getShipToAddress().getState_Full()
				+ ", shipToCreateRequestType.getDistributionChannelDefault(): "+shipToCreateRequestType.getDistributionChannelDefault()
				+ ", shipToCreateRequestType.getDivisionDefault(): "+shipToCreateRequestType.getDivisionDefault()
				+ ", shipToCreateRequestType.getShipToAddress.getCountry(): "+shipToCreateRequestType.getShipToAddress().getCountry()
				+ ", shipToCreateRequestType.getShipToAddress.getCountry3Char(): "+shipToCreateRequestType.getShipToAddress().getCountry3Char()
				+ ", shipToCreateRequestType.getgetContactFirstName(): "+shipToCreateRequestType.getContactFirstName()
				+ ", shipToCreateRequestType.getContactLastName(): "+shipToCreateRequestType.getContactLastName()
				+ ", shipToCreateRequestType.getContactEmail(): "+shipToCreateRequestType.getContactEmail()
				+ ", shipToCreateRequestType.getPhone(): "+shipToCreateRequestType.getPhone()
				+ ", shipToCreateRequestType.getVATNumber(): "+shipToCreateRequestType.getVATNumber()
				+ ", shipToCreateRequestType.getSalesOrg(): "+shipToCreateRequestType.getSalesOrg()
				+ ", shipToCreateRequestType.getTokenNumber(): "+shipToCreateRequestType.getTokenNumber()
				+ ", duplicateAccountReason:" + duplicateAccountReason
				+ ", bpLinkId:" + bpLinkId
				+ ", bpName:" + bpName
				+ ", loginName:" + loginName
				+ ", loginEmail:" + loginEmail);


		try {
			shipToCreatePort = new ShipToCreateWSDL_Service_Impl().getShipToCreateWSDLSOAP();
			logger.debug("----------PorT---------"
					+ shipToCreatePort.toString());
		} catch (ServiceException serviceException) {
			// TODO Auto-generated catch block
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw new GrtSapException(errMsg);
		}

		try {
			//shipToCreateURL = GRTPropertiesUtil.getProperty("shiptoaddress_endpoint_url").trim();
			shipToCreateURL = getShipToAddressEndPoint();
			setRemoteEndPoint((Stub) shipToCreatePort, shipToCreateURL);

		} catch (Exception e) {
			String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
					+ e.getMessage();
			logger.error("ERROR: " + errMsg, e);
			throw new GrtSapException(errMsg);
		}

		logger.debug("Retrying logic");

		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: While getting the property retry_count");
			e.printStackTrace();
			throw new GrtSapException(e);
		}

		for (int i = 0; i < retry_count; i++) {
			try {

				logger.debug("Calling FMW with these shipToCreateRequestType : ##############");

				logger.debug("Calling FMW for Ship To Create");
				long ca1 = Calendar.getInstance().getTimeInMillis();
				shipToCreateResponseType = shipToCreatePort.shipToCreate(shipToCreateRequestType);

				if (shipToCreateResponseType != null) {

					if (shipToCreateResponseType.getCode() != null){
						returnCode = shipToCreateResponseType.getCode();
						logger.debug("TransactionResponse Code "+ shipToCreateResponseType.getCode());
						logger.debug("TransactionResponse Description "+ shipToCreateResponseType.getDescription());
						logger.debug("TransactionResponse ShipTo "+ shipToCreateResponseType.getShipTo());
						logger.debug("TransactionResponse Account Id "+ shipToCreateResponseType.getAccountId());
						try {
							if(returnCode.equalsIgnoreCase("1000") && StringUtils.isNotEmpty(duplicateAccountReason)) {
								//Gautam commented below line
								//MailUtil.getInstance().sendMailNotification(null, shipToCreateRequestType, duplicateAccountReason, shipToCreateResponseType.getShipTo(), bpLinkId, bpName, loginName, loginEmail);
							}
						} catch(Throwable throwable) {
							logger.error("error while sending mail to CMD", throwable);
						}
					}
				} else {
					returnCode = "1110";
				}
				long ca2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("TIMER:" + (ca2 - ca1) + " milliseconds");

				i=retry_count;
			} catch (IOException ioe) {
				// TODO: handle exception
				String errMsg = "Failed to connect FMW for address search";
				logger.error(errMsg);
				if (i == retry_count - 1) {
					logger.error(errMsg, ioe);
					throw ioe;
				}
			} catch (SOAPFaultException e) {
				String errMsg = "Failed when sending data to FMW Service";
				logger.error("e.getMessage() :"+e.getMessage());
				logger.error(errMsg, e);
				throw e;
			} catch (Exception e) {
				logger.error("e.getMessage() :"+e.getMessage());
				throw e;
			}
		}

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for fmw transaction for addresssearch  time in milliseconds :" + (c2 - c1));
		logger.debug("------ReturnCode------" + returnCode);
		logger.debug("................ Exiting Address Search in generic client Total time ................ ");
		if (returnCode == null) {
			returnCode = GRTConstants.fmw_errorCode;
		}

		return shipToCreateResponseType;
	}

	public SoldToCreateResponseType sapSoldToAddressCall(SoldToCreateRequestType soldToCreateRequestType, String duplicateAccountReason, String bpLinkId, String bpName, String loginName, String loginEmail)
		throws Exception {

		long c1 = Calendar.getInstance().getTimeInMillis();
		logger
				.debug("................ Starting Address Search ...............");

		SoldToCreateWSDL_PortType soldToCreatePort = null;
		SoldToCreateResponseType soldToCreateResponseType = null;
		String soldToCreateURL = null;

		int retry_count = 0;
		String returnCode = null;

		logger.debug("SoldToCreateRequestType : soldToCreateRequestType.getShipToName1: "+ soldToCreateRequestType.getSoldToName1()
				+ ", soldToCreateRequestType.getShipToAddress.getCity(): "+soldToCreateRequestType.getSoldToAddress().getCity()
				+ ", soldToCreateRequestType.getShipToAddress.getStreet1(): "+soldToCreateRequestType.getSoldToAddress().getStreet1()
				+ ", soldToCreateRequestType.getShipToAddress.getZip(): "+soldToCreateRequestType.getSoldToAddress().getZip()
				+ ", soldToCreateRequestType.getShipToAddress.getRegion(): "+soldToCreateRequestType.getSoldToAddress().getRegion()
				+ ", soldToCreateRequestType.getShipToAddress.getState_Full(): "+soldToCreateRequestType.getSoldToAddress().getState_Full()
				+ ", soldToCreateRequestType.getShipToAddress.getCountry(): "+soldToCreateRequestType.getSoldToAddress().getCountry()
				+ ", soldToCreateRequestType.getShipToAddress.getCountry3Char(): "+soldToCreateRequestType.getSoldToAddress().getCountry3Char()
				+ ", soldToCreateRequestType.getContactFirstName(): "+soldToCreateRequestType.getContactFirstName()
				+ ", soldToCreateRequestType.getContactLastName(): "+soldToCreateRequestType.getContactLastName()
				+ ", soldToCreateRequestType.getContactEmail(): "+soldToCreateRequestType.getContactEmail()
				+ ", soldToCreateRequestType.getPhone(): "+soldToCreateRequestType.getPhone()
				+ ", duplicateAccountReason:" + duplicateAccountReason
				+ ", bpLinkId:" + bpLinkId
				+ ", bpName:" + bpName
				+ ", loginName:" + loginName
				+ ", loginEmail:" + loginEmail);


		try {
			soldToCreatePort = new SoldToCreateWSDL_Service_Impl().getSoldToCreateWSDLSOAP();
			logger.debug("----------PorT---------"
					+ soldToCreatePort.toString());
		} catch (ServiceException serviceException) {
			// TODO Auto-generated catch block
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw new GrtSapException(errMsg);
		}

		try {
			//soldToCreateURL = GRTPropertiesUtil.getProperty("soldtoaddress_endpoint_url").trim();
			soldToCreateURL = getSoldToAddressEndPoint();
			setRemoteEndPoint((Stub) soldToCreatePort, soldToCreateURL);

		} catch (Exception e) {
			String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
					+ e.getMessage();
			logger.error("ERROR: " + errMsg, e);
			throw new GrtSapException(errMsg);
		}

		logger.debug("Retrying logic");

		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: While getting the property retry_count");
			e.printStackTrace();
			throw new GrtSapException(e);
		}

		for (int i = 0; i < retry_count; i++) {
			try {

				logger.debug("Calling FMW with these soldToCreateRequestType : ##############");

				long ca1 = Calendar.getInstance().getTimeInMillis();
				soldToCreateResponseType = soldToCreatePort.soldToCreate(soldToCreateRequestType);

				if (soldToCreateResponseType != null) {

					if (soldToCreateResponseType.getCode() != null){
						returnCode = soldToCreateResponseType.getCode();
						logger.debug("TransactionResponse Code "+ returnCode);
						logger.debug("TransactionResponse Description "+ soldToCreateResponseType.getDescription());
						logger.debug("TransactionResponse SoldTo "+ soldToCreateResponseType.getSoldTo());
						logger.debug("TransactionResponse Account No "+ soldToCreateResponseType.getAccountId());
						try {
							if(returnCode.equalsIgnoreCase("1000") && StringUtils.isNotEmpty(duplicateAccountReason)) {
								//Gautam commented this line
								//MailUtil.getInstance().sendMailNotification(soldToCreateRequestType, null, duplicateAccountReason, soldToCreateResponseType.getSoldTo(), bpLinkId, bpName, loginName, loginEmail);
							}
						} catch(Throwable throwable) {
							logger.error("error while sending mail to CMD", throwable);
						}
					}
				} else {
					returnCode = "1110";
				}
				long ca2 = Calendar.getInstance().getTimeInMillis();
				logger.debug("TIMER:" + (ca2 - ca1) + " milliseconds");

				i=retry_count;
			} catch (IOException ioe) {
				// TODO: handle exception
				String errMsg = "Failed to connect FMW for SoldTo Address";
				logger.error(errMsg);
				if (i == retry_count - 1) {
					logger.error(errMsg, ioe);
					throw ioe;
				}
			} catch (SOAPFaultException e) {
				String errMsg = "Failed when sending data to FMW Service";
				logger.error("e.getMessage() :"+e.getMessage());
				logger.error(errMsg, e);
				throw e;
			} catch (Exception e) {
				logger.error("e.getMessage() :"+e.getMessage());
				throw e;
			}
		}

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for fmw transaction for addresssearch  time in milliseconds :" + (c2 - c1));
		logger.debug("------ReturnCode------" + returnCode);
		logger.debug("................ Exiting Address Search in generic client Total time ................ ");
		if (returnCode == null) {
			returnCode = GRTConstants.fmw_errorCode;
		}

		return soldToCreateResponseType;
	}

	public AccountUpdateResponse createSSRForAccountUpdate(Account original, Account modified) throws Exception {
        long c1 = Calendar.getInstance().getTimeInMillis();
        AccountUpdateServicePortType port = getAccountUpdatePort();
        AccountUpdateResponse response = null;
        String sfdcSSRCreationUrl = null;
        AccountUpdateRequest request = null;
        int retry_count = 0;
        try {
        	//sfdcSSRCreationUrl = GRTPropertiesUtil.getProperty("sfdc_ssr_creattion_endpoint_url").trim();
        	sfdcSSRCreationUrl = getSfdcSSRCreationEndPoint();
            logger.debug("URL  :"+sfdcSSRCreationUrl);
            setRemoteEndPoint((Stub) port, sfdcSSRCreationUrl);
        } catch (Exception e) {
            String errMsg = "Failure when trying to read SFDC SSR endpoint properties for setup. Original error: "
                + e.getMessage();
            logger.error("ERROR: "+ errMsg, e);
            throw new GrtSapException(errMsg);
        }

        try {
            retry_count = getMaximumRetry();
        } catch (Exception e) {
            logger.error("ERROR: While getting the property retry_count");
            e.printStackTrace();
            throw new GrtSapException(e);
        }

        for (int i = 0; i < retry_count; i++) {
        	try {
	            logger.debug("Calling FMW for SFDC-SSR creation for Account update");
	            request = getAccountUpdateRequest(original, modified);
                logger.debug("Request Created sucessfully ################################");
	            response = port.accountUpdateinterface(request);
	            logger.debug("Response : "+response);
	            if(response != null) {
	                logger.debug("Status :" + response.getStatus());
	                logger.debug("SSRNumber :"+response.getSSRNumber());
	                logger.debug("ErrorMessage :"+response.getErrorMsg());
	             }
	            i = retry_count;
	        } catch (IOException ioe) {
                ioe.printStackTrace();
	            String errMsg = "Failed to connect FMW for SFDC SSR creation";
                logger.error("ioe.getMessage() :"+ioe.getMessage());
                logger.error("ioe.getCause() :"+ioe.getCause());
                logger.error("ioe.getLocalizedMessage() :"+ioe.getLocalizedMessage());
	            logger.error(errMsg,ioe);
	            if (i == retry_count - 1) {
	                logger.error(errMsg, ioe);
	                throw ioe;
	            }
	        } catch (SOAPFaultException e) {
                e.printStackTrace();
	            String errMsg = "Failed when sending data to FMW Service";
	            logger.error("e.getMessage() :"+e.getMessage());
	            logger.error(errMsg, e);
	            throw e;
	        } catch (Exception e) {
                e.printStackTrace();
	            logger.error("e.getMessage() :"+e.getMessage());
	            throw e;
	        }
        }
        logger.debug("TIMER for fmw transaction for addresssearch  time in milliseconds :" + (Calendar.getInstance().getTimeInMillis() - c1));
        return response;
    }
	
	private AccountUpdateServicePortType getAccountUpdatePort() throws Exception {
        try{
        	return  new AccountUpdateServiceService_Impl().getAccountUpdateService();
        } catch (Exception serviceException) {
            String errMsg = "Failure when getting the port type from the service locator";
            logger.error("ERROR:" + errMsg, serviceException);
            throw new GrtSapException(serviceException);
        }
    }
	
	private AccountUpdateRequest getAccountUpdateRequest(Account original, Account modified) {
		AccountUpdateRequest request = new AccountUpdateRequest();
		request.setAccountNumber(original.getSoldToNumber());
		request.setAccountName(modified.getName());
        request.setCustomerContactName(modified.getContactName());
		request.setCity(modified.getPrimaryAccountCity());
		request.setCompanyName(modified.getName());

        if(StringUtils.isNotEmpty(modified.getPrimaryAccountState())) {
            //request.setState_Province(modified.getPrimaryAccountState().split("##")[1]); -- Commented to fox ArrayIndexOutofBoundException
        	request.setState_Province(modified.getPrimaryAccountState().split("##").length>1?modified.getPrimaryAccountState().split("##")[1]:modified.getPrimaryAccountState());
        }


        //TODO: Query local Country table and load the 3-alpha ISO Code.
		//request.setCountryISOCode3Char("USA");
        //SFDC SSR service is working fine, even with 2-alpha ISO Code for country.
        request.setCountryISOCode3Char(original.getCountryCode());
		//request.setCompanyName(modified.getContactName());
		request.setMainPhoneNumber(modified.getPhoneNumber());
		request.setPostalCode(modified.getPrimaryAccountPostalCode());
		request.setRequestName(GRTConstants.GRT_SSR_REQUEST);
		//TODO:Pending for I box
		//request.setSalesDistrictCode(GRTConstants.B_BOX_SALES_DISTRICT_CODE_INTERNAL);
		request.setStreetAddressLine1(modified.getPrimaryAccountStreetAddress());
		if(StringUtils.isNotEmpty(modified.getPrimaryAccountStreetAddress2())) {
			request.setStreetAddressLine2(modified.getPrimaryAccountStreetAddress2());
		}
		//Set 2-alpha ISO code for state & full text If country is not US.
		String deliverables = original.getModificationSummary(modified);
		if(StringUtils.isNotEmpty(deliverables)) {
			request.setDescribeDeliverables(original.getModificationSummary(modified));
		}
        logger.debug("AccountNumber :" +request.getAccountNumber());
        logger.debug("AccountName :"+ request.getAccountName());
        logger.debug("CustomerContactName :"+request.getCustomerContactName());
        logger.debug("City :"+request.getCity());
        logger.debug("CompanyName :"+request.getCompanyName());
        logger.debug("State_Province (SapStateID):"+request.getState_Province());
        logger.debug("CountryISOCode3Char :" +request.getCountryISOCode3Char());
        logger.debug("MainPhoneNumber :"+request.getMainPhoneNumber());
        logger.debug("PostalCode :"+request.getPostalCode());
        logger.debug("RequestName :"+request.getRequestName());
        logger.debug("StreetAddressLine1 :"+request.getStreetAddressLine1());
       	logger.debug("StreetAddressLine2 :"+ request.getStreetAddressLine2());
        logger.debug("DescribeDeliverables:"+request.getDescribeDeliverables());

		return request;
	}
	
	public boolean assignAccess(String bpLinkId, String soldToId, String soldToName, String serviceTerm, String accountType) throws Exception {
		logger.debug("Entering assignAccess for bpLinkId:" + bpLinkId + ", soldToId:" + soldToId + ", soldToName:" + soldToName
				+ "for accountType ::" + accountType);
		String loaEndUrl = null;
		int retry_count = 0;
		int timeout = 0;
		boolean success = false;
		LOA portType = null;

		try {
			portType = new LOABindingQSService_Impl().getLOABindingQSPort();
			logger.debug("----------PorType---------" + portType.toString());
		} catch (ServiceException serviceException) {
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw serviceException;
		}

		try {
			//loaEndUrl = GRTPropertiesUtil.getProperty("loa_endpoint_url").trim();
			loaEndUrl = getLoaEndPoint();
			timeout = getConnectionTimeOut();			
			retry_count = getMaximumRetry();
			logger.debug("endURL:" + loaEndUrl);
			logger.debug("timeout:" + timeout);
			logger.debug("retry_count:" + retry_count);
			setRemoteEndPoint((Stub) portType, loaEndUrl);
		} catch (Exception e) {
			logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
		}

		LOAUpsertRequestType request = new LOAUpsertRequestType();
		request.setBPLinkId(bpLinkId);
		request.setSoldTo(soldToId);
		request.setSoldToName(soldToName);
		request.setSource(GRTConstants.LOA_SOURCE);
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(new Date());
		request.setStartDate(startDate);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(new Date());
		if(StringUtils.isNotEmpty(serviceTerm)){
			endDate.add(Calendar.MONTH, Integer.parseInt(serviceTerm));
		} else {
			endDate.add(Calendar.DATE, 15);
		}
		request.setEndDate(endDate);
		request.setPermissionStatus(PermissionStatus.active);
		request.setPermissionType(PermissionType.registration);

		for (int i = 0; i < retry_count; i++) {
			try {
				logger.debug("Entering 1st call to LOA Binding service with setting permission type as registration");
				LOAUpsertResponseType response = portType.lOAUpsertOperation(request);
				if(response != null && response.getCode() != null && StringUtils.isNotEmpty(response.getCode().getValue()) && response.getCode().getValue().equals("0")) {
					success = true;
					logger.info("Success for upsert on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId);
					break;
				} else {
					logger.info("Error for upsert on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId );
					if(response != null) {
						if(null != response.getCode()) {
							logger.info(" code" + response.getCode().getValue());
						} else {
							logger.info(" code" + response.getCode());
						} 
						
						logger.info(" description:" + response.getDescription());
					}
				}
			} catch (IOException ioe) {
				logger.error("Failed to connect FMW for LOA", ioe);
				if(i == retry_count - 1) {
					if(ioe.getCause() instanceof SOAPFaultException) {
						if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
							logger.error("MONITOR:[Outage:LOA-UPSERT][ENDPOINT:"+ loaEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						} else {
							logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ loaEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						}
					} else {
						logger.error("FMW is down");
					}
		        }
			} catch (SOAPFaultException e) {
				logger.error("Failed while sending data to FMW for LOA",  e);
				throw e;
			}
		}
		
		if (!GRTConstants.ACCOUNT_UPDATE.equalsIgnoreCase(accountType)) {
			request.setPermissionType(PermissionType.supportServices);
			for (int i = 0; i < retry_count; i++) {
				try {
					logger.debug("Entering 2nd call to LOA Binding service with setting permission type as Support Services");
					LOAUpsertResponseType response = portType.lOAUpsertOperation(request);
					if(response != null && response.getCode() != null && StringUtils.isNotEmpty(response.getCode().getValue()) && response.getCode().getValue().equals("0")) {
						success = true;
						logger.info("Success for upsert on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId);
						break;
					} else {
						logger.info("Error for upsert on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId + " code" + response.getCode().getValue() + " description:" + response.getDescription());
					}
				} catch (IOException ioe) {
					logger.error("Failed to connect FMW for LOA", ioe);
					if(i == retry_count - 1) {
						if(ioe.getCause() instanceof SOAPFaultException) {
							if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
								logger.error("MONITOR:[Outage:LOA-UPSERT][ENDPOINT:"+ loaEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
							} else {
								logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ loaEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
							}
						} else {
							logger.error("FMW is down");
						}
			        }
				} catch (SOAPFaultException e) {
					logger.error("Failed while sending data to FMW for LOA",  e);
					throw e;
				}
			}
		}
		logger.debug("Exiting assignAccess for bpLinkId:" + bpLinkId + ", soldToId:" + soldToId + ", soldToName:" + soldToName);
		return success;
	}

	public int getMaximumRetry() {
		return maximumRetry;
	}

	public void setMaximumRetry(int maximumRetry) {
		this.maximumRetry = maximumRetry;
	}

	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public String getAlsbUser() {
		return alsbUser;
	}

	public void setAlsbUser(String alsbUser) {
		this.alsbUser = alsbUser;
	}

	public String getAlsbPasswd() {
		return alsbPasswd;
	}

	public void setAlsbPasswd(String alsbPasswd) {
		this.alsbPasswd = alsbPasswd;
	}

	public String getTokenSearchEndPoint() {
		return tokenSearchEndPoint;
	}

	public void setTokenSearchEndPoint(String tokenSearchEndPoint) {
		this.tokenSearchEndPoint = tokenSearchEndPoint;
	}

	public String getTokenRedemptionEndPoint() {
		return tokenRedemptionEndPoint;
	}

	public void setTokenRedemptionEndPoint(String tokenRedemptionEndPoint) {
		this.tokenRedemptionEndPoint = tokenRedemptionEndPoint;
	}

	public String getAddressSearchEndPoint() {
		return addressSearchEndPoint;
	}

	public void setAddressSearchEndPoint(String addressSearchEndPoint) {
		this.addressSearchEndPoint = addressSearchEndPoint;
	}

	public String getShipToAddressEndPoint() {
		return shipToAddressEndPoint;
	}

	public void setShipToAddressEndPoint(String shipToAddressEndPoint) {
		this.shipToAddressEndPoint = shipToAddressEndPoint;
	}

	public String getSoldToAddressEndPoint() {
		return soldToAddressEndPoint;
	}

	public void setSoldToAddressEndPoint(String soldToAddressEndPoint) {
		this.soldToAddressEndPoint = soldToAddressEndPoint;
	}

	public String getSfdcSSRCreationEndPoint() {
		return sfdcSSRCreationEndPoint;
	}

	public void setSfdcSSRCreationEndPoint(String sfdcSSRCreationEndPoint) {
		this.sfdcSSRCreationEndPoint = sfdcSSRCreationEndPoint;
	}

	public String getLoaEndPoint() {
		return loaEndPoint;
	}

	public void setLoaEndPoint(String loaEndPoint) {
		this.loaEndPoint = loaEndPoint;
	}
	
	public PasswordUtil getPasswordUtils() {
		return passwordUtils;
	}

	public void setPasswordUtils(PasswordUtil passwordUtils) {
		this.passwordUtils = passwordUtils;
	}

	private String decryptAlsbPasswd() {
		logger.debug("inside decryptAlsbPasswd:"+getAlsbPasswd());		
	//	return getPasswordUtils() == null ? getAlsbPasswd() : getPasswordUtils().decrypt(getAlsbPasswd());
		return PasswordUtil.decrypt(getAlsbPasswd());
		
		
	}
	// Added for gift card 2.0

	public String getBpAuthEndPoint() {
		return bpAuthEndPoint;
	}

	public void setBpAuthEndPoint(String bpAuthEndPoint) {
		this.bpAuthEndPoint = bpAuthEndPoint;
	}

	public String getContractLookupEndPoint() {
		return contractLookupEndPoint;
	}

	public void setContractLookupEndPoint(String contractLookupEndPoint) {
		this.contractLookupEndPoint = contractLookupEndPoint;
	}

	
	public String getArtConnectionTimeOut() {
		return artConnectionTimeOut;
	}

	public void setArtConnectionTimeOut(String artConnectionTimeOut) {
		this.artConnectionTimeOut = artConnectionTimeOut;
	}

	public static void main(String [] args) throws Exception {	
		/*ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IpossClient ipossClient = (IpossClient) context.getBean("ipossClient");*/
		
		IpossClient ipossClient = new IpossClient();
		
		//String accountLocation = "0002726514";
		String accountLocation ="0005371253";
		int maximumRetry = 1;
		
		ipossClient.setAlsbUser("GRTWS");
		//ipossClient.setAlsbPasswd("GRTWSuat2010");
		ipossClient.setAlsbPasswd("zsjYeGD0KqK6gF9c9GL8aA==");
		
		ipossClient.setMaximumRetry(maximumRetry);
		
		ipossClient.setTokenSearchEndPoint("https://fmwuat1.avaya.com:8102/TokenServices/v1/Proxy_Services/TokenSearch_PS");
		TokenResponseTypeDto token = ipossClient.tokenVerification("12345");
		System.out.println("Token Details : " + token.getCode() + "  " + token.getDescription());
		
		
	}
	
	// Added for gift card 2.0
	public boolean bpAuthorizationCheck(String bpLinkId, String credSolutionName) throws Exception {
		logger.debug("Entering bpAuthorizationCheck for bpLinkId:" + bpLinkId + ", credSolutionName:" + credSolutionName);
		String bpAuthEndUrl = null;
		int retry_count = 0;
		int timeout = 0;
		boolean success = false;
		BPAuthorizationCheck portType = null;

		try {
			portType = new BPAuthorizationCheckSOAPQSService_Impl().getBPAuthorizationCheckSOAPQSPort();
			logger.debug("----------PorType---------" + portType.toString());
		} catch (ServiceException serviceException) {
			String errMsg = "Failure when getting the port type from the BP Authorization";
			logger.error("ERROR:" + errMsg, serviceException);
			throw serviceException;
		}

		try {
			//bpAuthEndUrl = GRTPropertiesUtil.getProperty("bp_auth_endpoint_url").trim();
			bpAuthEndUrl = getBpAuthEndPoint();
			//timeout = Integer.parseInt(GRTPropertiesUtil.getProperty("art_connection_timeout").trim());
			timeout = Integer.parseInt(getArtConnectionTimeOut());
			//retry_count = Integer.parseInt(GRTPropertiesUtil.getProperty("retry_count").trim());
			retry_count = getMaximumRetry();
			logger.debug("endURL:" + bpAuthEndUrl);
			logger.debug("timeout:" + timeout);
			logger.debug("retry_count:" + retry_count);
			setRemoteEndPoint((Stub) portType, bpAuthEndUrl);
		} catch (Exception e) {
			logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
		}

		BPAuthorizationCheckRequest request = new BPAuthorizationCheckRequest();
		request.setBPLinkId(bpLinkId);
		request.setCredSolutionName(credSolutionName);

		for (int i = 0; i < retry_count; i++) {
			try {
				logger.debug("Entering 1st call to BP Authorization service with setting permission type as registration");
				BPAuthorizationCheckResponse response = portType.bPAuthorizationCheck(request);
				if(response != null && response.getAuthorized() != null && GRTConstants.YES.equalsIgnoreCase(response.getAuthorized())) {
					success = true;
					logger.info("Success for upsert on bpLinkId:" + bpLinkId + ", credSolutionName:" + credSolutionName + " Returncode" + response.getReturncode() + " Authorization:" + response.getAuthorized());
					break;
				} else {
					logger.info("Error for upsert on bpLinkId:" + bpLinkId + ", credSolutionName:" + credSolutionName + " Returncode" + response.getReturncode() + " Authorization:" + response.getAuthorized());
				}
			} catch (IOException ioe) {
				logger.error("Failed to connect FMW for BP Authorization", ioe);
				if(i == retry_count - 1) {
					if(ioe.getCause() instanceof SOAPFaultException) {
						if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
							logger.error("MONITOR:[Outage:BP_AUTHORIZATION_CHECK][ENDPOINT:"+ bpAuthEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						} else {
							logger.error("MONITOR:[Outage:BP_AUTHORIZATION_CHECK:FMW][ENDPOINT:"+ bpAuthEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						}
					} else {
						logger.error("FMW is down");
					}
		        }
			} catch (SOAPFaultException e) {
				logger.error("Failed while sending data to FMW for BP Authorization Check",  e);
				throw e;
			}
		}
		return success;
	}

	public List<Contract> contractLookup(String soldToId) throws Exception {
		logger.debug("Entering contractLookup for Sold To:" + soldToId);
		String contractLookupEndUrl = null;
		int retry_count = 0;
		int timeout = 0;
		ContractLookUpSOAP portType = null;
		Contract contractObj = null;
		List<Contract> contractList = null;
		try {
			portType = new ContractLookUpSOAPQSService_Impl().getContractLookUpSOAPQSPort();
			logger.debug("----------PorType---------" + portType.toString());
		} catch (ServiceException serviceException) {
			String errMsg = "Failure when getting the port type from the Contract Lookup";
			logger.error("ERROR:" + errMsg, serviceException);
			throw serviceException;
		}

		try {
			//contractLookupEndUrl = GRTPropertiesUtil.getProperty("contract_lookup_endpoint_url").trim();
			contractLookupEndUrl = getContractLookupEndPoint();
			//timeout = Integer.parseInt(GRTPropertiesUtil.getProperty("art_connection_timeout").trim());
			timeout = Integer.parseInt(getArtConnectionTimeOut());
			//retry_count = Integer.parseInt(GRTPropertiesUtil.getProperty("retry_count").trim());
			retry_count = getMaximumRetry();
			logger.debug("endURL:" + contractLookupEndUrl);
			logger.debug("timeout:" + timeout);
			logger.debug("retry_count:" + retry_count);
			setRemoteEndPoint((Stub) portType, contractLookupEndUrl);
		} catch (Exception e) {
			logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
		}
		ContractLookUpRequest request = new ContractLookUpRequest();
		request.setEndCustomerShipTo(soldToId);

		for (int i = 0; i < retry_count; i++) {
			try {
				logger.debug("Entering 1st call to CONTRACT LOOKUP service with setting permission type as registration");
				ContractLookUpResponse response = portType.contractLookUp(request);
				contractList = new ArrayList<Contract>();
				if(response != null && response.getContractLookUpResponseDetails() != null) {
					if(response.getContractLookUpResponseDetails().length > 0){
						for (ContractLookUpResponseType contract: response.getContractLookUpResponseDetails()) {
							logger.debug("Contract Start Date:"+contract.getContractStartDate());
							logger.debug("Contract End Date:"+contract.getContractEndDate());
							logger.debug("Contract Number:"+contract.getContractNumber());
							logger.debug("Contract Type:"+contract.getContractType());
							logger.debug("Contract Value:"+contract.getContractValue());
							contractObj = new Contract();
							if(contract.getContractStartDate() != null){
								contractObj.setContractStartDate(contract.getContractStartDate().getTime());
							}
							if(contract.getContractEndDate() != null){
								contractObj.setContractEndDate(contract.getContractEndDate().getTime());
							}
							if(contract.getContractNumber() != null){
								contractObj.setContractNumber(contract.getContractNumber());
							}
							if(contract.getContractType() != null){
								contractObj.setContractType(contract.getContractType().getValue());
							}
							if(contract.getContractValue() != null){
								contractObj.setContractValue(contract.getContractValue().toString());
							}
							contractList.add(contractObj);
						}
					}
					logger.info("Success for upsert on Sold To:" + soldToId);
					//break;
				} else {
					logger.info("Error for upsert on Sold To:" + soldToId+"   ReturnCode:"+response.getReturnCode());
					contractObj = new Contract();
					contractObj.setReturnCode(response.getReturnCode());
					contractList.add(contractObj);
				}
			} catch (IOException ioe) {
				logger.error("Failed to connect FMW for CONTRACT LOOKUP", ioe);
				if(i == retry_count - 1) {
					if(ioe.getCause() instanceof SOAPFaultException) {
						if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
							logger.error("MONITOR:[Outage:CONTRACT_LOOKUP][ENDPOINT:"+ contractLookupEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						} else {
							logger.error("MONITOR:[Outage:CONTRACT_LOOKUP:FMW][ENDPOINT:"+ contractLookupEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
						}
					} else {
						logger.error("FMW is down");
					}
		        }
			} catch (SOAPFaultException e) {
				logger.error("Failed while sending data to FMW for CONTRACT LOOKUP",  e);
				throw e;
			}
		}
		return contractList;
	}

}