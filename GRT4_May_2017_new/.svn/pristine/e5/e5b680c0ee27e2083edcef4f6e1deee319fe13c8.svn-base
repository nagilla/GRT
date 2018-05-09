package com.grt.integration.siebel;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.rpc.Stub;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.grt.dto.Account;
import com.grt.dto.Activity;
import com.grt.dto.AlarmIdAccount;
import com.grt.dto.Asset;
import com.grt.dto.Attachment;
import com.grt.dto.ServiceRequest;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.integration.eqm.siebel.assetupdate.BOC;
import com.grt.integration.eqm.siebel.assetupdate.GRT_SpcEquipment_SpcMove_Impl;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAccount_PortType;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAccount_Service_Impl;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAction_PortType;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAction_Service_Impl;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAsset_SpcManagement_PortType;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcAsset_SpcManagement_Service_Impl;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcService_SpcRequest_PortType;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcService_SpcRequest_Service_Impl;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcService_SpcRequest_SpcAttachment_PortType;
import com.grt.integration.siebel.clientproxy.AVAYA_SpcService_SpcRequest_SpcAttachment_Service_Impl;
import com.grt.util.GRTConstants;
import com.grt.util.GrtSiebelException;
import com.grt.util.PasswordUtil;
import com.siebel.customui.AVAYAAccountQueryPage_Input;
import com.siebel.customui.AVAYAAccountQueryPage_Output;
import com.siebel.customui.AVAYAActionInsert_Input;
import com.siebel.customui.AVAYAActionInsert_Output;
import com.siebel.customui.AVAYAActionQueryPage_Input;
import com.siebel.customui.AVAYAActionQueryPage_Output;
import com.siebel.customui.AVAYAAssetManagementASIUpdate_Output;
import com.siebel.customui.AVAYAAssetManagementQueryPage_Input;
import com.siebel.customui.AVAYAAssetManagementQueryPage_Output;
import com.siebel.customui.AVAYAAssetManagementUpdate_Input;
import com.siebel.customui.AVAYAServiceRequestASIInsert_Output;
import com.siebel.customui.AVAYAServiceRequestASIUpdate_Output;
import com.siebel.customui.AVAYAServiceRequestAttachmentInsert_Input;
import com.siebel.customui.AVAYAServiceRequestAttachmentInsert_Output;
import com.siebel.customui.AVAYAServiceRequestAttachmentQueryPage_Input;
import com.siebel.customui.AVAYAServiceRequestAttachmentQueryPage_Output;
import com.siebel.customui.AVAYAServiceRequestInsert_Input;
import com.siebel.customui.AVAYAServiceRequestQueryPage_Input;
import com.siebel.customui.AVAYAServiceRequestQueryPage_Output;
import com.siebel.customui.AVAYAServiceRequestUpdate_Input;
import com.siebel.xml.avaya20account.data.AccountData;
import com.siebel.xml.avaya20account.data.BusinessAddressData;
import com.siebel.xml.avaya20account.data.ListOfBusinessAddressData;
import com.siebel.xml.avaya20account.query.AccountQuery;
import com.siebel.xml.avaya20account.query.BusinessAddressQuery;
import com.siebel.xml.avaya20account.query.ListOfAvayaAccountQuery;
import com.siebel.xml.avaya20account.query.ListOfBusinessAddressQuery;
import com.siebel.xml.avaya20account.query.QueryType;
import com.siebel.xml.avaya20action.data.ActionData;
import com.siebel.xml.avaya20action.id.ActionId;
import com.siebel.xml.avaya20action.id.ListOfAvayaActionId;
import com.siebel.xml.avaya20action.insert.ActionInsert;
import com.siebel.xml.avaya20action.insert.ListOfAvayaActionInsert;
import com.siebel.xml.avaya20action.query.ActionQuery;
import com.siebel.xml.avaya20action.query.ListOfAvayaActionQuery;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.AssetMgmtAssetId;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.ListOfAvayaAssetMgmtAssetId;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.data.AVAYAAssetMgmtAssetXMData;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.data.AssetMgmtAssetData;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.data.ListOfAVAYAAssetMgmtAssetXMData;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.data.ListOfAvayaAssetMgmtAssetData;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.AVAYAAssetMgmtAssetXMQuery;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.AssetMgmtAssetQuery;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.ListOfAVAYAAssetMgmtAssetXMQuery;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.ListOfAvayaAssetMgmtAssetQuery;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.update.AssetMgmtAssetUpdate;
import com.siebel.xml.avaya20asset20mgmt2020asset2020test.update.ListOfAvayaAssetMgmtAssetUpdate;
import com.siebel.xml.avaya20service20request20attachment.data.ServiceRequestAttachmentData;
import com.siebel.xml.avaya20service20request20attachment.id.ServiceRequestAttachmentId;
import com.siebel.xml.avaya20service20request20attachment.insert.ListOfAvayaServiceRequestAttachmentInsert;
import com.siebel.xml.avaya20service20request20attachment.insert.ServiceRequestAttachmentInsert;
import com.siebel.xml.avaya20service20request20attachment.query.ListOfAvayaServiceRequestAttachmentQuery;
import com.siebel.xml.avaya20service20request20attachment.query.ServiceRequestAttachmentQuery;
import com.siebel.xml.avayaassetgrtrequest.AssetMgmtAsset;
import com.siebel.xml.avayaassetgrtrequest.AssetRequest;
import com.siebel.xml.avayaassetgrtrequest.AssetRequestTopElmt;
import com.siebel.xml.avayaassetgrtresponse.Ack;
import com.siebel.xml.avayaassetgrtresponse.AssetResponse;
import com.siebel.xml.avayaassetgrtresponse.AssetResponseTopElmt;
import com.siebel.xml.avayaassetgrtresponse.Body;
import com.siebel.xml.service20request_2.ServiceRequestId;
import com.siebel.xml.service20request_2.data.ServiceRequestData;
import com.siebel.xml.service20request_2.insert.ListOfAvayaService_RequestInsert;
import com.siebel.xml.service20request_2.insert.ServiceRequestInsert;
import com.siebel.xml.service20request_2.query.ListOfAvayaService_RequestQuery;
import com.siebel.xml.service20request_2.query.ServiceRequestQuery;
import com.siebel.xml.service20request_2.update.ListOfAvayaService_RequestUpdate;
import com.siebel.xml.service20request_2.update.ServiceRequestUpdate;

public class SiebelClient {
    private static final Logger logger = Logger.getLogger(SiebelClient.class);

    private static final String ACTIVITY_DEFAULT_STATUS = "Done";
    private static final String ACTIVITY_DEFAULT_TYPE = "SR Note";
    private static final String SR_SERVICE_DESK_SUBSTATUS = "Service Desk";
    private static final String SR_DEFAULT_SUBSTATUS = "System";
    private static final String SR_DEFAULT_SOURCE = "System Generated";
    private static final String SR_DEFAULT_SEVERITY = "4-Low";
    private static final String SR_DEFAULT_TYPE = "Internal";
    private static final String SR_DEFAULT_PRODUCT = "V00328";
    private static final String VIEW_MODE = "All";
    private static final String CONTACT_TYPE="Customer";
    private static final String AVAYA_CONTACT_PREF_LANG_NAME="English";
    private static final String LOV_LANGUAGE_MODE_LIC = "LIC";
    private static final String LOV_LANGUAGE_MODE_LDC = "LDC";
    private static final int DEFAULT_RETRY_COUNT = 1;
    private int maximumRetry;
    private String alsbUser;
    private String alsbPasswd;
    private String accountEndPoint;
    private String serviceEndPoint;
    private String activityEndPoint;
    private String attachmentEndPoint;
    private String assetEndPoint;
    private String eqmAssetUpdateEndPoint;
    private PasswordUtil passwordUtils;

    /**
     * @deprecated: TODO - Replace this global (class) variable to method var
     */
    private  int index = 0;

        /**
     * Calls Siebel GCT Service to query account.
     * @param soldToNumber
     */
    public Account queryAccount(String soldToNumber) throws Exception {
    	long c1 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Starting for SoldToNumber [" + soldToNumber + "] ...");
        logger.debug("query soldToNumber in Siebel: "+soldToNumber);
        Account retVal = null;

        //Prepare remote end point:
//        int pageSize = 1000;
        int pageSize = 100;
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcAccount_PortType portType = new AVAYA_SpcAccount_Service_Impl().getAVAYA_SpcAccount();
        try {
            gctSvcURL = getAccountEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        logger.debug("Preparing input params ...");
        AVAYAAccountQueryPage_Input input = new AVAYAAccountQueryPage_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LDC);
        ListOfAvayaAccountQuery listOfAccount = new ListOfAvayaAccountQuery();
        listOfAccount.setPagesize(BigInteger.valueOf(1));
        listOfAccount.setRecordcountneeded(false);
        listOfAccount.setStartrownum(BigInteger.valueOf(0));

        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setSearchspec("[Location] = '" + soldToNumber + "'");
        QueryType qt = new QueryType();
        qt.set_value("");
        accountQuery.setId(qt);
        accountQuery.setPrimaryAddressId(qt);
        accountQuery.setLocation(qt);
        accountQuery.setPrimaryAccountCountry(qt);
        accountQuery.setRegion(qt);
        accountQuery.setName(qt);
        accountQuery.setType(qt);
        accountQuery.setMainPhoneNumber(qt);

        ListOfBusinessAddressQuery listOfBusinessAddressQuery = new ListOfBusinessAddressQuery();
        listOfBusinessAddressQuery.setPagesize(BigInteger.valueOf(pageSize));
        listOfBusinessAddressQuery.setRecordcountneeded(false);
        listOfBusinessAddressQuery.setStartrownum(BigInteger.valueOf(0));

        BusinessAddressQuery baq = new BusinessAddressQuery();
        QueryType baqt = new QueryType();
        baqt.set_value("");
        baq.setId(baqt);
        baq.setCountry(baqt);
        baq.setCity(baqt);
        baq.setState(baqt);
        baq.setPostalCode(baqt);
        baq.setStreetAddress(baqt);
        baq.setStreetAddress2(baqt);
        //baq.setPhoneNumber(baqt);
        baq.setFaxNumber(baqt);

        listOfBusinessAddressQuery.setBusinessAddress(baq);
        accountQuery.setListOfBusinessAddress(listOfBusinessAddressQuery);
        listOfAccount.setAccount(accountQuery);
        input.setListOfAvayaAccount(listOfAccount);

        //Call webservice:
        AVAYAAccountQueryPage_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAAccountQueryPage(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "SoldTo Number [" + soldToNumber + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaAccount() != null) {
                if(output.getListOfAvayaAccount().getAccount() != null
                        && output.getListOfAvayaAccount().getAccount().length > 0) {
                    AccountData accountData = output.getListOfAvayaAccount().getAccount()[0];
                    retVal = copyAccount(accountData);
                } else {
                    //Warning ONLY
                    logger.warn("WARNING: Response from GCT service indicates EMPTY account(s) for SoldTo Number [" + soldToNumber + "]");
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);

            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken in queryAccount:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    private Account copyAccount(AccountData srData) {
        Account retVal;
        retVal = new Account();
        retVal.setAccountId(srData.getId());
        retVal.setSoldToNumber(srData.getLocation());
        retVal.setPrimaryAccountCountry(srData.getPrimaryAccountCountry());
        retVal.setRegion(srData.getRegion());
        retVal.setName(srData.getName());
        retVal.setType(srData.getType());
		retVal.setPhoneNumber(srData.getMainPhoneNumber());
        ListOfBusinessAddressData ba = srData.getListOfBusinessAddress();
        if (ba == null || ba.getBusinessAddress() == null || ba.getBusinessAddress().length == 0) {
            return retVal;
        }
        for(BusinessAddressData bad : ba.getBusinessAddress()) {
                if(bad.getId().equalsIgnoreCase(srData.getPrimaryAddressId())) {
                    retVal.setPrimaryAccountCountry(bad.getCountry());
                	retVal.setCountryCode(bad.getCountry());
                    retVal.setPrimaryAccountCity(bad.getCity());
                    retVal.setPrimaryAccountState(bad.getState());
                    retVal.setPrimaryAccountPostalCode(bad.getPostalCode());
                    retVal.setPrimaryAccountStreetAddress(bad.getStreetAddress());
                    retVal.setPrimaryAccountStreetAddress2(bad.getStreetAddress2());
                    //retVal.setPhoneNumber(bad.getPhoneNumber());
                    retVal.setFaxNumber(bad.getFaxNumber());
                }
        }
        return retVal;
    }
    
    /**
     * Calls Siebel GCT Service to query ServiceRequest
     * @param srNumber
     */
    public ServiceRequest querySR(String srNumber) throws Exception {
    	long c1 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Starting for SR Number [" + srNumber + "]");
        ServiceRequest retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_PortType portType = new AVAYA_SpcService_SpcRequest_Service_Impl().getAVAYA_SpcService_SpcRequest();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_sr_endpoint_url").trim();
        	gctSvcURL = getServiceEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAServiceRequestQueryPage_Input input = new AVAYAServiceRequestQueryPage_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

        ListOfAvayaService_RequestQuery listOfServiceRequest = new ListOfAvayaService_RequestQuery();
        listOfServiceRequest.setPagesize(BigInteger.valueOf(1));
        listOfServiceRequest.setRecordcountneeded(false);
        listOfServiceRequest.setStartrownum(BigInteger.valueOf(0));

        ServiceRequestQuery srQuery = new ServiceRequestQuery();
        srQuery.setSearchspec("[SRNumber] = '" + srNumber + "'");
        com.siebel.xml.service20request_2.query.QueryType qt = new com.siebel.xml.service20request_2.query.QueryType();
        qt.set_value("");
        srQuery.setId(qt);
        srQuery.setSRNumber(qt);
        srQuery.setStatus(qt);
        srQuery.setDescription(qt);
        srQuery.setOwnerFirstName(qt);
        srQuery.setOwnerLastName(qt);
        srQuery.setAVAYACompletionNarrative(qt);
        listOfServiceRequest.setServiceRequest(srQuery);
        input.setListOfAvayaService_Request(listOfServiceRequest);

        //Call webservice:
        AVAYAServiceRequestQueryPage_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestQueryPage(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "SR Number [" + srNumber + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaService_Request() != null) {
                if(output.getListOfAvayaService_Request().getServiceRequest() != null
                        && output.getListOfAvayaService_Request().getServiceRequest().length > 0) {
                    ServiceRequestData srData = output.getListOfAvayaService_Request().getServiceRequest()[0];
                    retVal = copyServiceRequest(srData);
                } else {
                    //Warning ONLY
                    logger.warn("WARNING: Response from GCT service indicates EMPTY Service Request for SR Number [" + srNumber + "]");
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by querySR:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    private ServiceRequest copyServiceRequest(ServiceRequestData srData) {
        ServiceRequest retVal;
        retVal = new ServiceRequest();
        retVal.setSrId(srData.getId());
        retVal.setSrNumber(srData.getSRNumber());
        retVal.setStatus(srData.getStatus());
        retVal.setDescription(srData.getDescription());
        retVal.setOwnerFirstName(srData.getOwnerFirstName());
        retVal.setOwnerLastName(srData.getOwnerLastName());
        logger.debug("srData.getAVAYACompletionNarrative():" + srData.getAVAYACompletionNarrative() + ":");
        retVal.setCompletionNarrative(srData.getAVAYACompletionNarrative());

        return retVal;
    }

    /**
     * Calls Siebel GCT service to create a new SR.
     * @param serviceRequest
     */
    public ServiceRequest createSR(ServiceRequest serviceRequest) throws Exception {
        logger.debug("Starting for Account Location [" + serviceRequest.getAccountLocation() + "]");
        long c1 = Calendar.getInstance().getTimeInMillis();
        logger.debug("createSR:");
        ServiceRequest retVal = null;
        String accountLocation = serviceRequest.getAccountLocation();
        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_PortType portType = new AVAYA_SpcService_SpcRequest_Service_Impl().getAVAYA_SpcService_SpcRequest();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_sr_endpoint_url").trim();
        	gctSvcURL = getServiceEndPoint();
            logger.debug("siebel_sr_endpoint_url:"+gctSvcURL);
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        //Prepare input:
        logger.info("==================PREPARE INPUT===============");
        AVAYAServiceRequestInsert_Input input = new AVAYAServiceRequestInsert_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);
        ListOfAvayaService_RequestInsert listOfServiceRequest = new ListOfAvayaService_RequestInsert();
        ServiceRequestInsert srInsert = new ServiceRequestInsert();
        srInsert.setAccountLocation(accountLocation);
        logger.debug("AccountLocation:"+accountLocation);
        logger.info("setAccountLocation:"+accountLocation);
        srInsert.setContactFirstName(serviceRequest.getContactFirstName());
        logger.debug("ContactFirstName:"+serviceRequest.getContactFirstName());
        logger.info("setContactFirstName:"+serviceRequest.getContactFirstName());
        srInsert.setContactLastName(serviceRequest.getContactLastName());
        logger.debug("ContactLastName:"+serviceRequest.getContactLastName());
        logger.info("setContactLastName:"+serviceRequest.getContactLastName());
        srInsert.setContactBusinessPhone(serviceRequest.getContactBusinessPhone());
        logger.debug("ContactBusinessPhone:"+serviceRequest.getContactBusinessPhone());
        logger.info("setContactBusinessPhone:"+serviceRequest.getContactBusinessPhone());
        srInsert.setAVAYAContactEmail2(serviceRequest.getContactEmail());
        logger.debug("ContactEmail:"+serviceRequest.getContactEmail());
        logger.info("setAVAYAContactEmail2:"+serviceRequest.getContactEmail());
        srInsert.setAVAYAContactPrefLangName(AVAYA_CONTACT_PREF_LANG_NAME);
        logger.info("Const: setAVAYAContactPrefLangName:"+AVAYA_CONTACT_PREF_LANG_NAME);
        srInsert.setAVAYAContactType(CONTACT_TYPE);
        logger.info("Const: setAVAYAContactType:"+CONTACT_TYPE);
        srInsert.setDescription(serviceRequest.getDescription());
        logger.info("setDescription:"+serviceRequest.getDescription());
        srInsert.setProduct(SR_DEFAULT_PRODUCT);
        logger.info("Const: setProduct:"+SR_DEFAULT_PRODUCT);
        srInsert.setSRType(SR_DEFAULT_TYPE);
        logger.info("Const: setSRType:"+SR_DEFAULT_TYPE);
        srInsert.setAVAYASRSubtypeDetail(serviceRequest.getSrSubType());
        logger.debug("SrSubType:"+serviceRequest.getSrSubType());
        logger.info("setAVAYASRSubtypeDetail:"+serviceRequest.getSrSubType());
        srInsert.setSRUrgency(serviceRequest.getUrgency());
        logger.info("setSRUrgency:"+serviceRequest.getUrgency());
        srInsert.setSeverity(SR_DEFAULT_SEVERITY);
        logger.info("Const: setSeverity:"+SR_DEFAULT_SEVERITY);
        srInsert.setSource(SR_DEFAULT_SOURCE);
        logger.info("Const: setSource:"+SR_DEFAULT_SOURCE);
        srInsert.setStatus(GRTConstants.SIEBEL_SR_STATUS_NEW);
        logger.info("Const: setStatus:"+GRTConstants.SIEBEL_SR_STATUS_NEW);
        srInsert.setSubStatus(SR_DEFAULT_SUBSTATUS);
        logger.info("Const: setSubStatus:"+SR_DEFAULT_SUBSTATUS);
        srInsert.setAVAYACustomerTicketNumber(serviceRequest.getCustomerTicketNumber());
        logger.debug("CustomerTicketNumber:"+serviceRequest.getCustomerTicketNumber());
        logger.info("setAVAYACustomerTicketNumber:"+serviceRequest.getCustomerTicketNumber());
        listOfServiceRequest.setServiceRequest(new ServiceRequestInsert[] { srInsert });
        logger.info("==================PREPARE INPUT EXIT===============");
        input.setListOfAvayaService_Request(listOfServiceRequest);

        //Call webservice:
        AVAYAServiceRequestASIInsert_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
            	logger.info("INPUT REQUEST START" + "\n" + input.toString() + "\n" + "INPUT REQUEST END");
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestInsert(input);
                break;
            } catch(IOException ioe) {
            	ioe.printStackTrace();
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "Account Location [" + accountLocation + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }
        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaService_Request() != null
                    && output.getListOfAvayaService_Request().getServiceRequest() != null
                    && output.getListOfAvayaService_Request().getServiceRequest().length > 0) {
                ServiceRequestId[] serviceRequestIds = output.getListOfAvayaService_Request().getServiceRequest();
                ServiceRequestId srId = serviceRequestIds[0];
                retVal = new ServiceRequest();
                retVal.setSrId(srId.getId());
                retVal.setSrNumber(srId.getSRNumber());
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        if(retVal != null) {
            logger.debug("Completed. Returned SR Number [" + retVal.getSrNumber() + "], SR ID [" + retVal.getSrId() + "]");
        } else {
            logger.warn("WARNING: Completed but Siebel returned EMPTY SR Number. Could be an failure. Please check Siebel GCT login.");
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by CreateSR API:"+(c2-c1) +" milliseconds");
        return retVal;
    }

    /**
     * Calls Siebel GCT Service to update an SR
     * @param srNumber
     */
    public ServiceRequest updateSR(String srNumber) throws Exception {
        logger.debug("Starting for SR Number [" + srNumber + "]");
        long c1 = Calendar.getInstance().getTimeInMillis();
        logger.debug("updateSR:"+srNumber);
        ServiceRequest retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_PortType portType = new AVAYA_SpcService_SpcRequest_Service_Impl().getAVAYA_SpcService_SpcRequest();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_sr_endpoint_url").trim();
        	gctSvcURL = getServiceEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAServiceRequestUpdate_Input input = new AVAYAServiceRequestUpdate_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);
        input.setViewMode(VIEW_MODE);

        ListOfAvayaService_RequestUpdate listOfServiceRequest = new ListOfAvayaService_RequestUpdate();
        ServiceRequestUpdate srUpdate = new ServiceRequestUpdate();
        srUpdate.setSRNumber(srNumber);
        srUpdate.setStatus(GRTConstants.SIEBEL_SR_STATUS_UPDATED);
        srUpdate.setSubStatus(SR_SERVICE_DESK_SUBSTATUS);
        listOfServiceRequest.setServiceRequest(new ServiceRequestUpdate[] {srUpdate});
        input.setListOfAvayaService_Request(listOfServiceRequest);

        //Call webservice:
        AVAYAServiceRequestASIUpdate_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestUpdate(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "SR Number [" + srNumber + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaService_Request() != null) {
                ServiceRequestId[] serviceRequestIds = output.getListOfAvayaService_Request().getServiceRequest();
                if (serviceRequestIds != null) {
                    ServiceRequestId srId = serviceRequestIds[0];
                    retVal = new ServiceRequest();
                    retVal.setSrId(srId.getId());
                    retVal.setSrNumber(srId.getSRNumber());
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by UpdateSR:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    /**
     * Calls Siebel GCT service to assign SR.
     * @param srNumber
     */
    public ServiceRequest assignSR(String srNumber) throws Exception {
        logger.debug("Starting for SR Number [" + srNumber + "]");
        logger.debug("assignSR:"+srNumber);
        long c1 = Calendar.getInstance().getTimeInMillis();
        ServiceRequest retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_PortType portType = new AVAYA_SpcService_SpcRequest_Service_Impl().getAVAYA_SpcService_SpcRequest();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_sr_endpoint_url").trim();
        	gctSvcURL = getServiceEndPoint();            
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAServiceRequestUpdate_Input input = new AVAYAServiceRequestUpdate_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);
        input.setViewMode(VIEW_MODE);

        ListOfAvayaService_RequestUpdate listOfServiceRequest = new ListOfAvayaService_RequestUpdate();
        ServiceRequestUpdate srUpdate = new ServiceRequestUpdate();
        srUpdate.setSRNumber(srNumber);
        srUpdate.setStatus(GRTConstants.SIEBEL_SR_STATUS_UNASSIGNED);
        srUpdate.setSubStatus(SR_SERVICE_DESK_SUBSTATUS);
        listOfServiceRequest.setServiceRequest(new ServiceRequestUpdate[] {srUpdate});
        input.setListOfAvayaService_Request(listOfServiceRequest);

        //Call webservice:
        AVAYAServiceRequestASIUpdate_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestUpdate(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "SR Number [" + srNumber + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaService_Request() != null) {
                ServiceRequestId[] serviceRequestIds = output.getListOfAvayaService_Request().getServiceRequest();
                if (serviceRequestIds != null) {
                    ServiceRequestId srId = serviceRequestIds[0];
                    retVal = new ServiceRequest();
                    retVal.setSrId(srId.getId());
                    retVal.setSrNumber(srId.getSRNumber());
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken By assignSR:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    /**
     * Calls Siebel GCT Service to create a new activity (SR Note)
     * @param accountLocation
     * @param srNumber
     * @param description
     * @param comments
     */
    public Activity createSRNoteActivity(String accountLocation, String srNumber, String description, String comments) throws Exception {

    	 long c1 = Calendar.getInstance().getTimeInMillis();
        String inputNote = "AccountLocation [" + accountLocation + "], SR Number [" + srNumber
                + "], Description [" + description + "], Comments [" + comments + "]";
        logger.debug("Starting for the following data: " + inputNote);
        logger.debug("createSRNoteActivity:");
        logger.debug("accountLocation:"+accountLocation);
        logger.debug("srNumber:"+srNumber);
        logger.debug("description:"+description);
        logger.debug("comments:"+comments);
        Activity retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcAction_PortType portType = new AVAYA_SpcAction_Service_Impl().getAVAYA_SpcAction();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_activity_endpoint_url").trim();
        	gctSvcURL = getActivityEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAActionInsert_Input input = new AVAYAActionInsert_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);
        input.setViewMode(VIEW_MODE);

        ListOfAvayaActionInsert listOfActions = new ListOfAvayaActionInsert();
        ActionInsert actionInsert = new ActionInsert();
        actionInsert.setAccountLocation(accountLocation);
        actionInsert.setSRNumber(srNumber);
        actionInsert.setDescription(description);
        actionInsert.setComment(comments);
        actionInsert.setType(ACTIVITY_DEFAULT_TYPE);
        actionInsert.setStatus(ACTIVITY_DEFAULT_STATUS);
        actionInsert.setBillableFlag(false);
        listOfActions.setAction(new ActionInsert[] {actionInsert});
        input.setListOfAvayaAction(listOfActions);

        //Call webservice:
        AVAYAActionInsert_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAActionInsert(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "Input SR Note [" + inputNote + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaAction() != null) {
                ListOfAvayaActionId listRequestId = output.getListOfAvayaAction();
                ActionId[] activities = listRequestId.getAction();
                if (activities != null) {
                    ActionId activityId = activities[0];
                    retVal = new Activity();
                    retVal.setActivityId(activityId.getId());
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by createSRNoteActivity :"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

   
    /**
     * Calls Siebel GCT Service to create a new attachment to the given Activity.
     * @param srId
     * @param fileName
     * @param fileExtension
     * @param fileContents
     */
    public Attachment createAttachment(String srId, String fileName, String fileExtension, byte[] fileContents) throws Exception {
        String inputDetail = "Activity ID [" + srId + "], Filename [" + fileName + "], File Extension [" + fileExtension + "]";
        long c1 = Calendar.getInstance().getTimeInMillis();
        logger.debug("createAttachment:");
        logger.debug("srId:"+srId);
        logger.debug("fileName:"+fileName);
        logger.debug("Starting with input data: " + inputDetail);
        Attachment retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_SpcAttachment_PortType portType = new AVAYA_SpcService_SpcRequest_SpcAttachment_Service_Impl().getAVAYA_SpcService_SpcRequest_SpcAttachment();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_attachment_endpoint_url").trim();
        	gctSvcURL = getAttachmentEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAServiceRequestAttachmentInsert_Input input = new AVAYAServiceRequestAttachmentInsert_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

        ListOfAvayaServiceRequestAttachmentInsert listOfAttachments = new ListOfAvayaServiceRequestAttachmentInsert();
        ServiceRequestAttachmentInsert attachmentInsert = new ServiceRequestAttachmentInsert();
        attachmentInsert.setActivityId(srId);
        attachmentInsert.setActivityFileName(fileName);
        attachmentInsert.setActivityFileExt(fileExtension);

        //byte[] encodedFile = Base64.encodeBase64(fileContents);
        byte[] encodedFile = fileContents;
        attachmentInsert.setActivityFileBuffer(encodedFile);
        attachmentInsert.setActivityFileSize(BigDecimal.valueOf(encodedFile.length));

        listOfAttachments.setServiceRequestAttachment(new ServiceRequestAttachmentInsert[] {attachmentInsert});
        input.setListOfAvayaServiceRequestAttachment(listOfAttachments);

        //Call webservice:
        AVAYAServiceRequestAttachmentInsert_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestAttachmentInsert(input);
                break;
            } catch(IOException ioe) {
                String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "Input detail [" + inputDetail + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaServiceRequestAttachment() != null
                    && output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment() != null
                    && output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment().length > 0) {
                ServiceRequestAttachmentId attachmentId = output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment()[0];
                retVal = new Attachment();
                retVal.setAttachmentId(attachmentId.getId());
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by createAttachment:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    /**
     * Calls Siebel GCT service to query an attachment given the SR.
     * @param srId
     * @param pageSize
     * @param recordCountNeeded
     * @param startRowNum
     */
    public List<Attachment> queryAttachments(String srId, int pageSize, boolean recordCountNeeded, int startRowNum) throws Exception {
        logger.debug("Starting for Activity ID [" + srId + "]");
        long c1 = Calendar.getInstance().getTimeInMillis();
        List<Attachment> retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcService_SpcRequest_SpcAttachment_PortType portType = new AVAYA_SpcService_SpcRequest_SpcAttachment_Service_Impl().getAVAYA_SpcService_SpcRequest_SpcAttachment();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_attachment_endpoint_url").trim();
        	gctSvcURL = getAttachmentEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAServiceRequestAttachmentQueryPage_Input input = new AVAYAServiceRequestAttachmentQueryPage_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

        ListOfAvayaServiceRequestAttachmentQuery listOfAttachments = new ListOfAvayaServiceRequestAttachmentQuery();
        ServiceRequestAttachmentQuery attachmentQuery = new ServiceRequestAttachmentQuery();
        listOfAttachments.setPagesize(BigInteger.valueOf(pageSize));
        listOfAttachments.setRecordcountneeded(recordCountNeeded);
        listOfAttachments.setStartrownum(BigInteger.valueOf(startRowNum));

        attachmentQuery.setSearchspec("[ActivityId]='" + srId + "'");
        com.siebel.xml.avaya20service20request20attachment.query.QueryType qt = new com.siebel.xml.avaya20service20request20attachment.query.QueryType();
        qt.set_value("");
        attachmentQuery.setActivityFileName(qt);
        attachmentQuery.setActivityFileExt(qt);
        attachmentQuery.setActivityFileSize(qt);
        attachmentQuery.setActivityFileBuffer("");

        listOfAttachments.setServiceRequestAttachment(attachmentQuery);
        input.setListOfAvayaServiceRequestAttachment(listOfAttachments);

        //Call webservice:
        AVAYAServiceRequestAttachmentQueryPage_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAServiceRequestAttachmentQueryPage(input);
                break;
            } catch(IOException ioe) {
                String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "Activity ID [" + srId + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaServiceRequestAttachment() != null) {

                if(output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment() != null
                        && output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment().length > 0) {
                    ServiceRequestAttachmentData[] attachments =
                            output.getListOfAvayaServiceRequestAttachment().getServiceRequestAttachment();
                    retVal = new ArrayList<Attachment>();
                    for (ServiceRequestAttachmentData attachmentData : attachments) {
                        Attachment attachment = new Attachment();
                        attachment.setAttachmentId(attachmentData.getId());
                        attachment.setFileContents(attachmentData.getActivityFileBuffer());
                        attachment.setFileName(attachmentData.getActivityFileName());
                        attachment.setFileExtension(attachmentData.getActivityFileExt());
                        attachment.setFileSize(attachmentData.getActivityFileSize().longValue());
                        retVal.add(attachment);
                    }
                } else {
                    //Warning ONLY:
                    logger.warn("WARNING: Returned response contains EMPTY attachment given Activity ID [" + srId + "]");
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by queryAttachments:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    /***********************************************
     * TODO (noval 12/12/2010): THis one has not been cleaned up.
     **/
    public List<Asset> queryActiveSolutionElements(String soldToNumber, int pageSize, boolean recordCountNeeded, int startRowNum) throws Exception {
        logger.debug("Entering queryActiveSolutionElements");
        List<Asset> retVal = null;
        logger.debug("Inside queryActiveSolutionElements input parameter print : soldToNumber : "+soldToNumber+", pageSize : "+pageSize+", recordCountNeeded : "+recordCountNeeded+", startRowNum : "+startRowNum);
        try {
            AVAYA_SpcAsset_SpcManagement_PortType portType = new AVAYA_SpcAsset_SpcManagement_Service_Impl().getAVAYA_SpcAsset_SpcManagement();
            AVAYAAssetManagementQueryPage_Input input = new AVAYAAssetManagementQueryPage_Input();
            input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

            ListOfAvayaAssetMgmtAssetQuery listOfAssetQueries = new ListOfAvayaAssetMgmtAssetQuery();
            listOfAssetQueries.setPagesize(BigInteger.valueOf(pageSize));
            listOfAssetQueries.setRecordcountneeded(recordCountNeeded);
            listOfAssetQueries.setStartrownum(BigInteger.valueOf(startRowNum));

            AssetMgmtAssetQuery assetQuery = new AssetMgmtAssetQuery();
            assetQuery.setSearchspec("[AccountLocation]='" + soldToNumber + "' AND [AVAYASECode] IS NOT NULL AND [LifecycleStatus] <> 'Inactive'");
            com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType qt = new com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType();
            qt.set_value("");
            assetQuery.setAccountLocation(qt);
            assetQuery.setAssetNumber(qt);
            assetQuery.setProductName(qt);
            assetQuery.setSerialNumber(qt);
            assetQuery.setLifecycleStatus(qt);
            assetQuery.setAVAYAShortDescription(qt);
            assetQuery.setAVAYASECode(qt);
            assetQuery.setQuantity(qt);
            assetQuery.setAssetDescription(qt);
            assetQuery.setProductId(qt);
            assetQuery.setAVAYASEID(qt);
            assetQuery.setAVAYAAlarmID(qt);
            listOfAssetQueries.setAssetMgmtAsset(assetQuery);
            input.setListOfAvayaAssetMgmtAsset(listOfAssetQueries);

            //setRemoteEndPoint((Stub) portType, GRTPropertiesUtil.getProperty("siebel_asset_endpoint_url"));
            setRemoteEndPoint((Stub) portType, getAssetEndPoint());

            AVAYAAssetManagementQueryPage_Output output = portType.aVAYAAssetManagementQueryPage(input);
            ListOfAvayaAssetMgmtAssetData listOfAssets = output.getListOfAvayaAssetMgmtAsset();
            AssetMgmtAssetData[] assetArray = listOfAssets.getAssetMgmtAsset();
            if ((assetArray != null) && (assetArray.length > 0)) {
                retVal = copyAssets(assetArray, listOfAssets.getLastpage());
            }
        } catch (Exception ex) {

            if(recallMethod(ex)){
                queryActiveSolutionElements(soldToNumber, pageSize, recordCountNeeded, startRowNum);
            } else {
                throw new GrtSiebelException(this.getClass(), "queryActiveSolutionElements", ex);
            }
        }
        logger.debug("Exiting queryActiveSolutionElements");
        return retVal;
    }

    /***********************************************
     * TODO (noval 12/12/2010): THis one has not been cleaned up.
     **/
    public List<Asset> queryProductRegistrationData(String SEID, int pageSize, boolean recordCountNeeded, int startRowNum) throws Exception {
        logger.debug("Entering queryActiveSolutionElements");
        List<Asset> retVal = null;
        logger.debug("Inside queryProductRegistrationData input parameter print : SEID : "+SEID+", pageSize : "+pageSize+", recordCountNeeded : "+recordCountNeeded+", startRowNum : "+startRowNum);
        try {
            AVAYA_SpcAsset_SpcManagement_PortType portType = new AVAYA_SpcAsset_SpcManagement_Service_Impl().getAVAYA_SpcAsset_SpcManagement();
            AVAYAAssetManagementQueryPage_Input input = new AVAYAAssetManagementQueryPage_Input();
            input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

            ListOfAvayaAssetMgmtAssetQuery listOfAssetQueries = new ListOfAvayaAssetMgmtAssetQuery();
            listOfAssetQueries.setPagesize(BigInteger.valueOf(pageSize));
            listOfAssetQueries.setRecordcountneeded(recordCountNeeded);
            listOfAssetQueries.setStartrownum(BigInteger.valueOf(startRowNum));

            AssetMgmtAssetQuery assetQuery = new AssetMgmtAssetQuery();
            assetQuery.setSearchspec("[AVAYASEID]='"+SEID+"' AND [LifecycleStatus]='Active'");
            com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType qt = new com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType();
            qt.set_value("");
            assetQuery.setId(qt);
            assetQuery.setAVAYAAlarmID(qt);
            assetQuery.setAVAYAAFID(qt);
            assetQuery.setAVAYARelease(qt);
            assetQuery.setSerialNumber(qt);
            assetQuery.setName(qt);

            ListOfAVAYAAssetMgmtAssetXMQuery listOfAVAYAAssetMgmtAssetXMQuery = new ListOfAVAYAAssetMgmtAssetXMQuery();
            listOfAVAYAAssetMgmtAssetXMQuery.setPagesize(BigInteger.valueOf(100));
            listOfAVAYAAssetMgmtAssetXMQuery.setRecordcountneeded(recordCountNeeded);

            AVAYAAssetMgmtAssetXMQuery assetMgmtAssetXMQuery = new AVAYAAssetMgmtAssetXMQuery();
            assetMgmtAssetXMQuery.setSearchspec("[AVAYAEndDate] IS NULL");
            assetMgmtAssetXMQuery.setId(qt);
            assetMgmtAssetXMQuery.setType(qt);
            assetMgmtAssetXMQuery.setAVAYASubType(qt);
            assetMgmtAssetXMQuery.setAVAYAIPAddress(qt);
            assetMgmtAssetXMQuery.setAVAYAPhone(qt);
            assetMgmtAssetXMQuery.setAVAYALoginId(qt);
            assetMgmtAssetXMQuery.setAVAYAPassword(qt);
            listOfAVAYAAssetMgmtAssetXMQuery.setAVAYAAssetMgmtAssetXM(assetMgmtAssetXMQuery);

            assetQuery.setListOfAVAYAAssetMgmtAssetXM(listOfAVAYAAssetMgmtAssetXMQuery);
            listOfAssetQueries.setAssetMgmtAsset(assetQuery);

            input.setListOfAvayaAssetMgmtAsset(listOfAssetQueries);

            //setRemoteEndPoint((Stub) portType, GRTPropertiesUtil.getProperty("siebel_asset_endpoint_url"));
            setRemoteEndPoint((Stub) portType, getAssetEndPoint());

            AVAYAAssetManagementQueryPage_Output output = portType.aVAYAAssetManagementQueryPage(input);
            ListOfAvayaAssetMgmtAssetData listOfAssets = output.getListOfAvayaAssetMgmtAsset();
            AssetMgmtAssetData[] assetArray = listOfAssets.getAssetMgmtAsset();
            if ((assetArray != null) && (assetArray.length > 0)) {
                retVal = fetchAssetInfo(assetArray, listOfAssets.getLastpage());//copyAssets(assetArray, listOfAssets.getLastpage());
            }
        } catch (Exception ex) {

            if(recallMethod(ex)){
            	queryProductRegistrationData(SEID, pageSize, recordCountNeeded, startRowNum);
            } else {
                throw new GrtSiebelException(this.getClass(), "queryActiveSolutionElements", ex);
            }
        }
        logger.debug("Exiting queryActiveSolutionElements");
        return retVal;
    }

    private List<Asset> fetchAssetInfo(AssetMgmtAssetData[] assetArray, boolean lastPage) {
        List<Asset> retVal = new ArrayList<Asset>();
        for (AssetMgmtAssetData assetData : assetArray) {
            retVal.add(fetchAssetInfo(assetData, lastPage));
        }
        return retVal;
    }

    private Asset fetchAssetInfo(AssetMgmtAssetData assetData, boolean lastPage) {
        Asset retVal = new Asset();
        retVal.setAvayaFID(assetData.getAVAYAAFID());
        logger.debug("AvayaFID:"+retVal.getAvayaFID());
        retVal.setNickName(assetData.getName());
        logger.debug("NickName:"+retVal.getNickName());
        retVal.setAvayaReleaseNumber(assetData.getAVAYARelease());
        logger.debug("AvayaReleaseNumber:"+retVal.getAvayaReleaseNumber());

        ListOfAVAYAAssetMgmtAssetXMData listOfAVAYAAssetMgmtAssetXMData = assetData.getListOfAVAYAAssetMgmtAssetXM();
        AVAYAAssetMgmtAssetXMData[] avayAssetMgmtAssetXMDataArr = listOfAVAYAAssetMgmtAssetXMData.getAVAYAAssetMgmtAssetXM();
        if(avayAssetMgmtAssetXMDataArr != null){
	        for(AVAYAAssetMgmtAssetXMData avayAssetMgmtAssetXMData: avayAssetMgmtAssetXMDataArr){
		        if(avayAssetMgmtAssetXMData != null){
		        	if(avayAssetMgmtAssetXMData.getType().equalsIgnoreCase("IPAddr")
		        			&& avayAssetMgmtAssetXMData.getAVAYASubType().equalsIgnoreCase("Avaya")){
		        		retVal.setAvayaIPAddrress(avayAssetMgmtAssetXMData.getAVAYAIPAddress());
		        		logger.debug("AvayaIPAddrress:"+retVal.getAvayaIPAddrress());
		        	} else if(avayAssetMgmtAssetXMData.getType().equalsIgnoreCase("IPAddr")
		        			&& avayAssetMgmtAssetXMData.getAVAYASubType().equalsIgnoreCase("Customer")){
		        		retVal.setCustumorIPAddress(avayAssetMgmtAssetXMData.getAVAYAIPAddress());
		        		logger.debug("CustumorIPAddress:"+retVal.getCustumorIPAddress());
		        	} else if(avayAssetMgmtAssetXMData.getType().equalsIgnoreCase("Phone")
		        			&& avayAssetMgmtAssetXMData.getAVAYASubType().equalsIgnoreCase("Async")){
		        		retVal.setDialInNumber(avayAssetMgmtAssetXMData.getAVAYAPhone());
		        		logger.debug("DialInNumber:"+retVal.getDialInNumber());
		        	}
		        	if (StringUtils.isNotEmpty(avayAssetMgmtAssetXMData.getAVAYALoginId())) {
		        		retVal.setUsername(avayAssetMgmtAssetXMData.getAVAYALoginId());
		        	}
		        	if (StringUtils.isNotEmpty(avayAssetMgmtAssetXMData.getAVAYAPassword())) {
		        		retVal.setPassword(avayAssetMgmtAssetXMData.getAVAYAPassword());
		        	}
		        }
	        }
        }
        return retVal;
    }

    /**
     * To deactivate the assets in siebel directly
     **/
    public List<List<TechnicalOrderDetail>> deActivateSolutionElements(List<TechnicalOrderDetail> technicalOrderDetailList) throws Exception {
        logger.debug("Entering deActivateSolutionElements");
        List<List<TechnicalOrderDetail>> combinedList = new ArrayList<List<TechnicalOrderDetail>>();
        List<TechnicalOrderDetail> successList = new ArrayList<TechnicalOrderDetail>();
        List<TechnicalOrderDetail> failureList = new ArrayList<TechnicalOrderDetail>();
        try {
        	AVAYA_SpcAsset_SpcManagement_PortType portType = new AVAYA_SpcAsset_SpcManagement_Service_Impl().getAVAYA_SpcAsset_SpcManagement();
        	AVAYAAssetManagementUpdate_Input manageInput = new AVAYAAssetManagementUpdate_Input();
        	ListOfAvayaAssetMgmtAssetUpdate listOfAvayaAssetMgmtAssetUpdate = new ListOfAvayaAssetMgmtAssetUpdate();
        	//AssetMgmtAssetUpdate assetMgmtAssetUpdateArr[] = null;
        	AssetMgmtAssetUpdate assetMgmtAssetUpdateArr[] = new AssetMgmtAssetUpdate[technicalOrderDetailList.size()];
        	AssetMgmtAssetUpdate assetMgmtAssetUpdate = null;
        	int varIndex = 0;
        	for(TechnicalOrderDetail technicalOrderDetail: technicalOrderDetailList){
        		assetMgmtAssetUpdate = new AssetMgmtAssetUpdate();
        		logger.debug("Asset PK:"+technicalOrderDetail.getAssetPK());
        		if(StringUtils.isEmpty(technicalOrderDetail.getAssetPK())){
        			// Process the pipeline record and log it
        			logger.debug("********************************* Registration Id:"+technicalOrderDetail.getRegistrationId()+" ********************************************");
        			logger.debug("**** Material Code:"+technicalOrderDetail.getMaterialCode()+" ****  Equipment Number:"+technicalOrderDetail.getSummaryEquipmentNumber()+" ****");
        			logger.debug("*******************************************************************************************************");
        			failureList.add(technicalOrderDetail);
        			continue;
        		}
        		assetMgmtAssetUpdate.setId(technicalOrderDetail.getAssetPK());
        		logger.debug("Id:" + assetMgmtAssetUpdate.getId());
        		assetMgmtAssetUpdate.setAVAYAEquipmentNumber(technicalOrderDetail.getEquipmentNumber());
        		logger.debug("Equip No:" + assetMgmtAssetUpdate.getAVAYAEquipmentNumber());
        		if(technicalOrderDetail != null && technicalOrderDetail.getRemainingQuantity() != null){
	        		if(technicalOrderDetail.getRemainingQuantity() == 0){
	        			assetMgmtAssetUpdate.setLifecycleStatus("Inactive");
	        			//assetMgmtAssetUpdate.setAVAYASEID("");
	        			assetMgmtAssetUpdate.setAVAYASummaryEquipmentNumber("");
	            		assetMgmtAssetUpdate.setAVAYAEquipmentNumber("");
	        		}
	        		assetMgmtAssetUpdate.setQuantity(technicalOrderDetail.getRemainingQuantity().intValue());
        		}
        		//TODO
        		assetMgmtAssetUpdateArr[varIndex++] = assetMgmtAssetUpdate;
        	}
        	if(assetMgmtAssetUpdateArr != null && assetMgmtAssetUpdateArr.length > 0){
	        	listOfAvayaAssetMgmtAssetUpdate.setAssetMgmtAsset(assetMgmtAssetUpdateArr);
	        	//listOfAvayaAssetMgmtAssetUpdate.setRecordcount(arg0);
	        	manageInput.setListOfAvayaAssetMgmtAsset(listOfAvayaAssetMgmtAssetUpdate);
	        	manageInput.setLOVLanguageMode("LIC");
	        	manageInput.setViewMode("ALL");
	        	//setRemoteEndPoint((Stub) portType, GRTPropertiesUtil.getProperty("siebel_asset_endpoint_url"));
	        	setRemoteEndPoint((Stub) portType, getAssetEndPoint());
	        	logger.debug("Before Asset update call");
	        	AVAYAAssetManagementASIUpdate_Output output = portType.aVAYAAssetManagementUpdate(manageInput);
	        	logger.debug("After Asset update call");
	        	ListOfAvayaAssetMgmtAssetId listOfAssets = output.getListOfAvayaAssetMgmtAsset();
	        	if(listOfAssets != null){
		        	logger.debug("After Asset update call"+listOfAssets.getAssetMgmtAsset().length);
		        	AssetMgmtAssetId[] assetArray = listOfAssets.getAssetMgmtAsset();
		        	for(AssetMgmtAssetId assetMgmtAssetId:assetArray){
		        		logger.debug("Response Id:"+assetMgmtAssetId.getId()+"  Asset Number:"+assetMgmtAssetId.getAssetNumber());
		        		for(TechnicalOrderDetail tod: technicalOrderDetailList){
		        			if(tod.getAssetPK().equalsIgnoreCase(assetMgmtAssetId.getId())){
		        				successList.add(tod);
		        			}
		        		}
		        	}
	        	}
        	} else {
        		logger.debug("No Assets to deactivate in Siebel. Probably Assets set for deactivation have no AssetId associted.");
        	}
        } catch (Throwable throwable) {
        	logger.debug("Error in deActivateSolutionElements:"+throwable);
        }
        logger.debug("Exiting deActivateSolutionElements with SuccessList Size:"+successList.size());
        logger.debug("Exiting deActivateSolutionElements with FailureList Size:"+failureList.size());
        combinedList.add(successList);
        combinedList.add(failureList);
        return combinedList;
    }

    /**
     * To deactivate the assets in siebel directly
     **/
    public List<TechnicalOrderDetail> deactivatingTRAssetsAfterRemovalSuccess(List<TechnicalOrderDetail> technicalOrderDetailList) throws Exception {
        logger.debug("Entering deactivatingTRAssetsAfterRemovalSuccess: List Size:"+technicalOrderDetailList.size());
        List<TechnicalOrderDetail> successList = new ArrayList<TechnicalOrderDetail>();
        try {
        	AVAYA_SpcAsset_SpcManagement_PortType portType = new AVAYA_SpcAsset_SpcManagement_Service_Impl().getAVAYA_SpcAsset_SpcManagement();
        	AVAYAAssetManagementUpdate_Input manageInput = new AVAYAAssetManagementUpdate_Input();
        	ListOfAvayaAssetMgmtAssetUpdate listOfAvayaAssetMgmtAssetUpdate = new ListOfAvayaAssetMgmtAssetUpdate();
        	//AssetMgmtAssetUpdate assetMgmtAssetUpdateArr[] = null;
        	AssetMgmtAssetUpdate assetMgmtAssetUpdateArr[] = new AssetMgmtAssetUpdate[technicalOrderDetailList.size()];
        	AssetMgmtAssetUpdate assetMgmtAssetUpdate = null;
        	int varIndex = 0;
        	for(TechnicalOrderDetail technicalOrderDetail: technicalOrderDetailList){
        		assetMgmtAssetUpdate = new AssetMgmtAssetUpdate();
        		assetMgmtAssetUpdate.setId(technicalOrderDetail.getAssetPK());
        		logger.debug("Id:" + assetMgmtAssetUpdate.getId());
        		assetMgmtAssetUpdate.setAVAYAEquipmentNumber(technicalOrderDetail.getEquipmentNumber());
        		logger.debug("Equip No:" + assetMgmtAssetUpdate.getAVAYAEquipmentNumber());
        		assetMgmtAssetUpdate.setAVAYASummaryEquipmentNumber(technicalOrderDetail.getSummaryEquipmentNumber());
        		assetMgmtAssetUpdate.setLifecycleStatus(technicalOrderDetail.getStatusId());
        		logger.debug("LifecycleStatus :" + assetMgmtAssetUpdate.getLifecycleStatus());
        		if(technicalOrderDetail.getRemainingQuantity() != null){
        			assetMgmtAssetUpdate.setQuantity(technicalOrderDetail.getRemainingQuantity().intValue());
        			logger.debug("Quantity :" + assetMgmtAssetUpdate.getQuantity());
        		}
        		//assetMgmtAssetUpdate.setAVAYASEID(technicalOrderDetail.getSolutionElementId());
        		//assetMgmtAssetUpdate.setAVAYARFASID(technicalOrderDetail.getSid());
        		//assetMgmtAssetUpdate.setAVAYARFAMID(technicalOrderDetail.getMid());
        		assetMgmtAssetUpdateArr[varIndex++] = assetMgmtAssetUpdate;
        	}
        	listOfAvayaAssetMgmtAssetUpdate.setAssetMgmtAsset(assetMgmtAssetUpdateArr);
        	//listOfAvayaAssetMgmtAssetUpdate.setRecordcount(arg0);
        	manageInput.setListOfAvayaAssetMgmtAsset(listOfAvayaAssetMgmtAssetUpdate);
        	manageInput.setLOVLanguageMode("LIC");
        	manageInput.setViewMode("ALL");
        	//setRemoteEndPoint((Stub) portType, GRTPropertiesUtil.getProperty("siebel_asset_endpoint_url"));
        	setRemoteEndPoint((Stub) portType, getAssetEndPoint());
        	logger.debug("Before Asset update call");
        	AVAYAAssetManagementASIUpdate_Output output = portType.aVAYAAssetManagementUpdate(manageInput);
        	logger.debug("After Asset update call");
        	ListOfAvayaAssetMgmtAssetId listOfAssets = output.getListOfAvayaAssetMgmtAsset();
        	if(listOfAssets != null){
	        	logger.debug("After Asset update call"+listOfAssets.getAssetMgmtAsset().length);
	        	AssetMgmtAssetId[] assetArray = listOfAssets.getAssetMgmtAsset();
	        	for(AssetMgmtAssetId assetMgmtAssetId:assetArray){
	        		logger.debug("Response Id:"+assetMgmtAssetId.getId()+"  Asset Number:"+assetMgmtAssetId.getAssetNumber());
	        		for(TechnicalOrderDetail tod: technicalOrderDetailList){
	        			if(tod.getAssetPK().equalsIgnoreCase(assetMgmtAssetId.getId())){
	        				successList.add(tod);
	        			}
	        		}
	        	}
        	}
        } catch (Throwable throwable) {
        	logger.debug("Error in deActivateSolutionElements:"+throwable);
        } finally {
            logger.debug("Exiting deactivatingTRAssetsAfterRemovalSuccess with SuccessList Size:"+successList.size());
        	return successList;
        }
    }

    private List<Asset> copyAssets(AssetMgmtAssetData[] assetArray, boolean lastPage) {
        List<Asset> retVal = new ArrayList<Asset>();
        for (AssetMgmtAssetData assetData : assetArray) {
            retVal.add(copyAsset(assetData, lastPage));
        }
        return retVal;
    }

    private Asset copyAsset(AssetMgmtAssetData assetData, boolean lastPage) {
        Asset retVal = new Asset();
        retVal.setAccountLocation(assetData.getAccountLocation());
        retVal.setProductName(assetData.getProductName());
        retVal.setSerialNumber(assetData.getSerialNumber());
        retVal.setLifecycleStatus(assetData.getLifecycleStatus());
        retVal.setShortDescription(assetData.getAVAYAShortDescription());
        retVal.setSeCode(assetData.getAVAYASECode());
        retVal.setLastPage(lastPage);
        retVal.setAssetDescription(assetData.getAssetDescription());
        retVal.setQuantity(assetData.getQuantity());
        //retVal.setProductId(assetData.getProductId());
        logger.debug(" Inside copyAsset AVAYASEID--->AVAYAAlarmID print : "+assetData.getAVAYASEID()+"---->"+assetData.getAVAYAAlarmID());
        retVal.setProductId(assetData.getAVAYAAlarmID());//FIX for product ID
        retVal.setSolutionElementId(assetData.getAVAYASEID());

        return retVal;
    }
    
    /**
     * Calls Siebel GCT Service to query Alaram ID account.
     * @param soldToNumber
     */
    public AlarmIdAccount queryAlaramIdAccount(String alaramId) throws Exception {
        logger.debug("Starting for Alaram ID : [" + alaramId + "] ...");
        logger.debug("queryAlaramIdAccount:"+alaramId);
        long c1 = Calendar.getInstance().getTimeInMillis();
        AlarmIdAccount retVal = new AlarmIdAccount();

        //Prepare remote end point:
        //int pageSize = 1000;
        int pageSize = 100;
        Boolean recordCountNeeded = false;
        int startRowNum = 0;

        String gctSvcURL = null;
        AVAYA_SpcAsset_SpcManagement_PortType portType = new AVAYA_SpcAsset_SpcManagement_Service_Impl().getAVAYA_SpcAsset_SpcManagement();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_asset_endpoint_url").trim();
        	gctSvcURL = getAssetEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        try {
        //Prepare input:
        logger.debug("Preparing input params ...");
        AVAYAAssetManagementQueryPage_Input input = new AVAYAAssetManagementQueryPage_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LDC);
        logger.debug("LOV_LANGUAGE_MODE_LDC"+input.getLOVLanguageMode());

        ListOfAvayaAssetMgmtAssetQuery listOfAssetQueries = new ListOfAvayaAssetMgmtAssetQuery();
        listOfAssetQueries.setPagesize(BigInteger.valueOf(pageSize));
        listOfAssetQueries.setRecordcountneeded(recordCountNeeded);
        listOfAssetQueries.setStartrownum(BigInteger.valueOf(startRowNum));

        AssetMgmtAssetQuery assetQuery = new AssetMgmtAssetQuery();
        com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType qt = new com.siebel.xml.avaya20asset20mgmt2020asset2020test.query.QueryType();
        assetQuery.setSearchspec("[AVAYAAlarmID]='"+ alaramId +"'");
        logger.debug("Avaya AlarmId----"+assetQuery.getSearchspec());
        qt.set_value("");
        assetQuery.setAccountLocation(qt);
        assetQuery.setAssetNumber(qt);
        assetQuery.setLifecycleStatus(qt);
        listOfAssetQueries.setAssetMgmtAsset(assetQuery);
        input.setListOfAvayaAssetMgmtAsset(listOfAssetQueries);

        //Call webservice:

		logger.debug("Calling GCT service ... ");

        AVAYAAssetManagementQueryPage_Output output = portType.aVAYAAssetManagementQueryPage(input);


                ListOfAvayaAssetMgmtAssetData listOfAssets = output.getListOfAvayaAssetMgmtAsset();
                AssetMgmtAssetData[] assetArray = listOfAssets.getAssetMgmtAsset();
                if ((assetArray != null) && (assetArray.length > 0)) {
                   // retVal = copyAlaramIDAccount(assetArray[0]);
                    retVal.setAccountLocation(assetArray[0].getAccountLocation());
                    retVal.setAVAYAalarmID(assetArray[0].getAVAYAAlarmID());
                    retVal.setLifeCycleStatus(assetArray[0].getLifecycleStatus());
                } else {
                    //Warning ONLY
                    logger.warn("WARNING: Response from GCT service indicates EMPTY account(s) for Alaram ID Number [" + alaramId + "]");
                    retVal.setAccountLocation("");
                }
        } catch (Exception ex) {
            //Must be Siebel SOAP Fault error:
           //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
           String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
           logger.error("ERROR: " + ex);
           throw new GrtSiebelException(errMsg);
        }
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("Total Time taken by queryAlaramIdAccount:"+(c2-c1) +" milliseconds");
        logger.debug("Completed.");
        return retVal;
    }

    private AlarmIdAccount copyAlaramIDAccount(AssetMgmtAssetData srData) {
    	AlarmIdAccount retVal;
        retVal = new AlarmIdAccount();
        retVal.setAccountLocation(srData.getAccountLocation());
        retVal.setAVAYAalarmID(srData.getAVAYAAlarmID());

        return retVal;
    }

    private  boolean recallMethod(Exception e) throws Exception{
        boolean ret = false;
        if (index < maximumRetry) {

            if (e instanceof IOException) {
                ret = true;
                logger
                        .error("IOException caught while calling Siebel system, ...Retry to call the "
                                + (index + 1)
                                + " time.");

                if (index == maximumRetry - 1) {
                    logger.error("Send email for notification.");
                    ret = false;
                    throw e;
                }
                index++;
            } else {
                index = maximumRetry;
                throw e;
            }
        }
        return ret;
    }
    
    private void setRemoteEndPoint(Stub stub, String url) throws Exception {
        logger.debug("Setting for URL [" + url + "]");
        stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, getAlsbUser());
        stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, decryptAlsbPasswd());
    }

	public int getMaximumRetry() {
		return maximumRetry;
	}

	public void setMaximumRetry(int maximumRetry) {
		this.maximumRetry = maximumRetry;
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

	public String getAccountEndPoint() {
		return accountEndPoint;
	}

	public void setAccountEndPoint(String accountEndPoint) {
		this.accountEndPoint = accountEndPoint;
	}

	public String getServiceEndPoint() {
		return serviceEndPoint;
	}

	public void setServiceEndPoint(String serviceEndPoint) {
		this.serviceEndPoint = serviceEndPoint;
	}

	public String getActivityEndPoint() {
		return activityEndPoint;
	}

	public void setActivityEndPoint(String activityEndPoint) {
		this.activityEndPoint = activityEndPoint;
	}

	public String getAttachmentEndPoint() {
		return attachmentEndPoint;
	}

	public void setAttachmentEndPoint(String attachmentEndPoint) {
		this.attachmentEndPoint = attachmentEndPoint;
	}

	public String getAssetEndPoint() {
		return assetEndPoint;
	}

	public void setAssetEndPoint(String assetEndPoint) {
		this.assetEndPoint = assetEndPoint;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static void main(String[] args) throws Exception {
		SiebelClient sc = new SiebelClient();
		
		String accountLocation = "0002726514";
		int maximumRetry = 1;
		int pageSize = 10;
		boolean recordCountNeeded = true;
		int startRowNum = 0;
		
		sc.setAlsbUser("GRTWS");
		//sc.setAlsbPasswd("GRTWSuat2010");
		sc.setAlsbPasswd("zsjYeGD0KqK6gF9c9GL8aA==");
		sc.setMaximumRetry(maximumRetry);
		
		
		sc.setEqmAssetUpdateEndPoint("http://fmwuat3.avaya.com:8101/GCT_Ticketing/EquipmentMove/v4/ProxyServices/EquipmentMove_PS");
		
		List<TechnicalOrderDetail> list = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail tech = new TechnicalOrderDetail();
		/*
		tech.setAssetPK("1-7ZY5-2");
		tech.setToSoldToId("0002701803");
		tech.setSoldToId("0051407876");
		tech.setSummaryEquipmentNumber("000001000525594236");
		list.add(tech);
		tech = new TechnicalOrderDetail();
		tech.setAssetPK("1-80AV-5");
		tech.setToSoldToId("0001052431");
		tech.setSoldToId("0002787501");
		tech.setSummaryEquipmentNumber("000001000525594255");
		list.add(tech);
		*/
		
		tech.setAssetPK("1-7ZY5-2");
		tech.setToSoldToId("0002701803");
		tech.setSoldToId("0051407876");
		tech.setSummaryEquipmentNumber("000001000525594236");
		list.add(tech);
		
		//tech.setSoldToId("0002787501");
		//List<TechnicalOrderDetail> techList = sc.eqmSiebelAssetUpdate(list);
		//System.out.println("Account ID : " + techList);
		
		
		/*
		sc.setServiceEndPoint("https://fmwstagec3.avaya.com:8502/GCT_Ticketing_UAT/ServiceRequest/SRCommon/v1");
		ServiceRequest sr = sc.querySR("1-527160621");
		System.out.println(sr.getSrId());
		*/
		
		/*sc.setAssetEndPoint("https://fmwstagec3.avaya.com:8502/GCT_Ticketing_UAT/Asset/v1");
		List assetList = sc.queryActiveSolutionElements(accountLocation, pageSize, recordCountNeeded, startRowNum);
		
		for (int i = 0; i < assetList.size(); i++) {
			Asset asset = (Asset) assetList.get(i);
			
			System.out.println("Asset Details : " + asset.getAssetNumber() + "   " + asset.getAssetDescription());
		}*/
	}
	/**
     * Calls Siebel GCT service to query the the most recent public SR Note.
     *
     */
    public Activity queryLatestPublicSRNoteActivity(String srNumber) throws Exception {
        logger.debug("Starting for SR Number [" + srNumber + "]");
        Activity retVal = null;

        //Prepare end point:
        int currentRetry = 0;
        String gctSvcURL = null;
        AVAYA_SpcAction_PortType portType = new AVAYA_SpcAction_Service_Impl().getAVAYA_SpcAction();
        try {
            //gctSvcURL = GRTPropertiesUtil.getProperty("siebel_activity_endpoint_url").trim();
        	gctSvcURL = getActivityEndPoint();
            setRemoteEndPoint((Stub)portType, gctSvcURL);
        } catch(Exception e) {
            String errMsg = "Failure when trying to read Siebel GCT properties for setup. Original error: " + e.getMessage();
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }

        //Prepare input:
        AVAYAActionQueryPage_Input input = new AVAYAActionQueryPage_Input();
        input.setLOVLanguageMode(LOV_LANGUAGE_MODE_LIC);

        ListOfAvayaActionQuery listOfActions = new ListOfAvayaActionQuery();
        listOfActions.setPagesize(BigInteger.valueOf(1));
        listOfActions.setRecordcountneeded(false);
        listOfActions.setStartrownum(BigInteger.valueOf(0));

        ActionQuery actionQuery = new ActionQuery();
        actionQuery.setSearchspec("[SRNumber]='" + srNumber + "' AND [Type] = 'SR Note' AND [Private] = 'N'");

        com.siebel.xml.avaya20action.query.QueryType qt = new com.siebel.xml.avaya20action.query.QueryType();
        qt.set_value("");
        actionQuery.setActivityId(qt);
        actionQuery.setActivitySRId(qt);
        actionQuery.setAccountLocation(qt);
        actionQuery.setSRNumber(qt);
        actionQuery.setDescription(qt);
        actionQuery.setComment(qt);
        actionQuery.setType(qt);
        actionQuery.setStatus(qt);
        actionQuery.setPrivate(qt);

        com.siebel.xml.avaya20action.query.QueryType createdQueryType = new com.siebel.xml.avaya20action.query.QueryType();
        createdQueryType.set_value("");
        createdQueryType.setSortorder("DESC");
        createdQueryType.setSortsequence("1");

        actionQuery.setCreated(createdQueryType);
        listOfActions.setAction(actionQuery);
        input.setListOfAvayaAction(listOfActions);

        //Call webservice:
        AVAYAActionQueryPage_Output output = null;
        while(currentRetry < maximumRetry) {
            currentRetry = currentRetry + 1;
            try {
                logger.debug("Calling GCT service ... ");
                output = portType.aVAYAActionQueryPage(input);
                break;
            } catch(IOException ioe) {
            	String errMsg = "IO Exception when connecting to GCT Service in attempt " + currentRetry + " with error message: " + ioe.getMessage();
                logger.error(errMsg);
                if(currentRetry == maximumRetry) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SIEBEL][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ gctSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        			throw new GrtSiebelException(errMsg);
                }
                /*if(currentRetry <= maximumRetry) {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry
                            + "]. Processing for another retry call.");
                } else {
                    logger.error("ERROR: " + errMsg + "\n"
                            + "Current retry [" + currentRetry + "] out of max. retry [" + maximumRetry + "]. No more retry.\n"
                            + "SR Number [" + srNumber + "]\n"
                            + "GCT Service URL [" + gctSvcURL + "]\n");
                    throw new GrtSiebelException(errMsg);
                }*/
            }
        }

        //Parse response:
        if (output != null) {
            logger.debug("Received response from GCT service. Parsing the response ...");
            if(output.getListOfAvayaAction() != null) {
                if(output.getListOfAvayaAction().getAction() != null
                        && output.getListOfAvayaAction().getAction().length > 0) {
                    ActionData[] actionArray = output.getListOfAvayaAction().getAction();
                    retVal = new Activity();
                    retVal.setActivityId(actionArray[0].getActivityId());
                    retVal.setAccountLocation(actionArray[0].getAccountLocation());
                    retVal.setSrNumber(actionArray[0].getSRNumber());
                    retVal.setDescription(actionArray[0].getDescription());
                    retVal.setComment(actionArray[0].getComment());
                    retVal.setType(actionArray[0].getType());
                    retVal.setStatus(actionArray[0].getStatus());
                    retVal.setPrivateActivity(actionArray[0].getPrivate());
                } else {
                    //Warning ONLY:
                    logger.warn("WARNING: Returned response contains EMPTY activity for SR Number [" + srNumber + "]");
                }
            } else {
                //Must be Siebel SOAP Fault error:
                //TODO (noval 12/12/2010): GRT needs to parse Siebel response for SOAP Fault exception.
                String errMsg = "Failed to find expected response element from Siebel GCT response; could be SOAP Fault Exception!";
                logger.error("ERROR: " + errMsg);
                throw new GrtSiebelException(errMsg);
            }
        } else {
            String errMsg = "Received NULL response from GCTTKT service. Cannot continue!";
            logger.error("ERROR: " + errMsg);
            throw new GrtSiebelException(errMsg);
        }
        if(retVal != null) {
            logger.debug("Completed. Returned response: Activity ID [" + retVal.getActivityId() + "], Activity Comment [" + retVal.getComment() + "]");
        } else {
            logger.debug("Completed. Siebel GCTTKT returned empty activity.");
        }
        return retVal;
    }
    
    /* GRT 4.0 Change : New Siebel Asset Update service for Equipment Move : update the asset in siebel after success response 
     * 					from SAP
     *  */
    public List<TechnicalOrderDetail> eqmSiebelAssetUpdate(List<TechnicalOrderDetail> technicalOrderDetailList, StringBuffer sb) throws Exception {
        logger.debug("Entering eqmSiebelAssetUpdate: List Size:"+technicalOrderDetailList.size());
        sb.append("Entering eqmSiebelAssetUpdate: List Size:"+technicalOrderDetailList.size()+"... ");
        List<TechnicalOrderDetail> successList = new ArrayList<TechnicalOrderDetail>();
        try {
        	  BOC portType = new GRT_SpcEquipment_SpcMove_Impl().getBOC(); 
        	  AssetRequestTopElmt input = new AssetRequestTopElmt();
        	  AssetRequest  assetRequestList = new AssetRequest();
        	  AssetMgmtAsset assetMgmtAssetArr[] = new AssetMgmtAsset[technicalOrderDetailList.size()];
        	  AssetMgmtAsset assetMgmtAsset = null;
        	int varIndex = 0;
        	//Create the input for the webservice
        	logger.debug("Printing values in to be sent..");
        	sb.append("Printing values in to be sent..");
        	for(TechnicalOrderDetail technicalOrderDetail: technicalOrderDetailList){
        		assetMgmtAsset = new AssetMgmtAsset();
        		
        		assetMgmtAsset.setAssetId(technicalOrderDetail.getAssetPK());
        		logger.debug("AssetId:" + assetMgmtAsset.getAssetId());
        		sb.append("AssetId:" + assetMgmtAsset.getAssetId() + "... ");
        		//assetMgmtAsset.setSEQN(technicalOrderDetail.getSummaryEquipmentNumber());
        		if(StringUtils.isNotEmpty(technicalOrderDetail.getToEquipmentNumber())) {
        			assetMgmtAsset.setSEQN(technicalOrderDetail.getToEquipmentNumber());
        		} else if(StringUtils.isNotEmpty(technicalOrderDetail.getSummaryEquipmentNumber())) {
        			assetMgmtAsset.setSEQN(technicalOrderDetail.getSummaryEquipmentNumber());
        		} else {
        			assetMgmtAsset.setSEQN(technicalOrderDetail.getEquipmentNumber());
        		}
        		
        		logger.debug("ToEquipmentNumber - " + technicalOrderDetail.getToEquipmentNumber());
        		sb.append("ToEquipmentNumber - " + technicalOrderDetail.getToEquipmentNumber() + "... ");
        		
        		logger.debug("EquipmentNumber - " + technicalOrderDetail.getEquipmentNumber());
        		sb.append("EquipmentNumber - " + technicalOrderDetail.getEquipmentNumber() + "... ");
        		
        		logger.debug("TOD SummaryEquipmentNumber - " + technicalOrderDetail.getSummaryEquipmentNumber());
        		sb.append("TOD SummaryEquipmentNumber - " + technicalOrderDetail.getSummaryEquipmentNumber() + "... ");
        		
        		logger.debug("SummaryEquipmentNumber:" + assetMgmtAsset.getSEQN());
        		sb.append("SummaryEquipmentNumber:" + assetMgmtAsset.getSEQN() + "... ");
        		//check for TO SoldTOID
        		assetMgmtAsset.setFL_NEW(technicalOrderDetail.getToSoldToId());
        		logger.debug("To Sold-to (New SoldTo):" + assetMgmtAsset.getFL_NEW());
        				
        		//From SoldTo
        		//assetMgmtAsset.setFL(technicalOrderDetail.getSoldToId());
        		assetMgmtAsset.setFL(technicalOrderDetail.getSoldToId());
        		logger.debug("From Sold-to (Old SoldTo):" + assetMgmtAsset.getFL());
        		
        		assetMgmtAssetArr[varIndex++] = assetMgmtAsset;
        	}
        	assetRequestList.setAssetMgmtAsset(assetMgmtAssetArr);
        	input.setAssetRequest(assetRequestList);
            setRemoteEndPoint((Stub)portType,  getEqmAssetUpdateEndPoint());
        	logger.debug("Before Asset update call");
        	AssetResponseTopElmt output = portType.move(input);
        	logger.debug("After Asset update call");
        	AssetResponse assetResponse = output.getAssetResponse();
        	//Generate TO object from resonse if sucess
        	if(assetResponse != null){
	        	logger.debug("After Asset update call"+assetResponse.getBody());
	        	Body body = assetResponse.getBody();
	        	Ack ack = body.getAck();
	        	com.siebel.xml.avayaassetgrtresponse.Asset assets[] = body.getAsset();
	        	//Proceed only if success
	        	if( ack.getStatus().equalsIgnoreCase(GRTConstants.SUCCESS) && assets.length > 0 ){
	        		for(com.siebel.xml.avayaassetgrtresponse.Asset assetMgmtAssetId:assets){
		        		logger.debug("Response Id:"+assetMgmtAssetId.getAssetId()+"  Asset Number:"+assetMgmtAssetId.getAssetId());
		        		for(TechnicalOrderDetail tod: technicalOrderDetailList){
		        			if(tod.getAssetPK().equalsIgnoreCase(assetMgmtAssetId.getAssetId())){
		        				successList.add(tod);
		        			}
		        		}
		        	}
	        	}else{
	        		logger.debug("Asset update call failed.. Status - "+ack.getStatus());
	        	}
        	}
        } catch (Throwable throwable) {
        	logger.debug("Error in eqmSiebelAssetUpdate:"+throwable);
        } finally {
            logger.debug("Exiting eqmSiebelAssetUpdate with SuccessList Size:"+successList.size());
        }
        return successList;
    }
    
    public PasswordUtil getPasswordUtils() {
		return passwordUtils;
	}

	public void setPasswordUtils(PasswordUtil passwordUtils) {
		this.passwordUtils = passwordUtils;
	}

	private String decryptAlsbPasswd() {
		return PasswordUtil.decrypt(getAlsbPasswd());
	}

	public String getEqmAssetUpdateEndPoint() {
		return eqmAssetUpdateEndPoint;
	}

	public void setEqmAssetUpdateEndPoint(String eqmAssetUpdateEndPoint) {
		this.eqmAssetUpdateEndPoint = eqmAssetUpdateEndPoint;
	}
	
}
