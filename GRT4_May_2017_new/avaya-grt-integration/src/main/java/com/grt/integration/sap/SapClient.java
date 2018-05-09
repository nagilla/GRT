package com.grt.integration.sap;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.pi.qtc.customer.lookup.CustomerLookupRequest;
import com.avaya.pi.qtc.customer.lookup.CustomerLookupResponse;
import com.avaya.v1.equipmentremoval.ActionType;
import com.avaya.v1.equipmentremoval.DestinationType;
import com.avaya.v1.equipmentremoval.EquipmentRemovalAckType;
import com.avaya.v1.equipmentremoval.EquipmentRemovalDataType;
import com.avaya.v1.equipmentremoval.EquipmentRemovalRequestType;
import com.avaya.v1.installbase.HeaderType;
import com.avaya.v1.installbase.IBaseCreateRequestType;
import com.avaya.v1.installbase.IBaseCreateResponseType;
import com.avaya.v1.installbase.IBaseDataType;
import com.avaya.v4.equipmentmove.EquipmentMoveAckType;
import com.avaya.v4.equipmentmove.EquipmentMoveDataType;
import com.avaya.v4.equipmentmove.EquipmentMoveRequestType;
import com.grt.dto.InstallBaseCreationDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.integration.customerLookupService.fmwclientproxy.CustomerLookup;
import com.grt.integration.customerLookupService.fmwclientproxy.CustomerLookupService_Impl;
import com.grt.integration.eqm.sap.clientproxy.EquipmentMoveWSDL_PortType;
import com.grt.integration.eqm.sap.clientproxy.EquipmentMoveWSDL_Service_Impl;
import com.grt.integration.eqr.sap.clientproxy.EquipmentRemovalWSDL_PortType;
import com.grt.integration.eqr.sap.clientproxy.EquipmentRemovalWSDL_Service_Impl;
import com.grt.integration.sap.clientproxy.ExternaliBaseCreateWSDL_PortType;
import com.grt.integration.sap.clientproxy.ExternaliBaseCreateWSDL_Service_Impl;
import com.grt.util.GRTConstants;
import com.grt.util.GrtSapException;
import com.grt.util.PasswordUtil;


public class SapClient {
	private static final Logger logger = Logger.getLogger(SapClient.class);
	
	private int maximumRetry;
	private String alsbUser;
    private String alsbPasswd;
    private String sapEndPoint;
    private String eqmEndPoint;
    private String eqrEndPoint;
    private String customerLookupEndPoint;
    //system_identifier in grt.properties
    private String systemIdentifier;
    //mode in grt.properties
    private String applicationMode;
    private PasswordUtil passwordUtils;
    
	private void setRemoteEndPoint(Stub stub, String url) throws Exception {
	        logger.debug("Setting for URL [" + url + "]");
	        stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
	        stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, getAlsbUser());
	        stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, decryptAlsbPasswd());
	        /*logger.debug("FMW Authentication : UserName"+stub._getProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY));
	        logger.debug("FMW Authentication : password"+stub._getProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY));*/
	        
	    }
	 
	 public String installBaseCreation(InstallBaseCreationDto installBaseDto) throws Exception {
		    long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Starting for Install Base creation ................");
	        String sapSvcURL = null;
	        IBaseCreateResponseType iBaseCreateResponse = null;
	        ExternaliBaseCreateWSDL_PortType portType = null;
	        SiteRegistration siteRegistration = null;
	        IBaseCreateRequestType iBaseCreateRequest =null;
	        
	        int retry_count = 0;
	        String returnCode = null;
			try {
				portType = new ExternaliBaseCreateWSDL_Service_Impl().getExternaliBaseCreateWSDLSOAP();
				logger.debug("----------PorType---------"+portType.toString());
			} catch (ServiceException serviceException) {
				// TODO Auto-generated catch block
				String errMsg = "Failure when getting the port type from the service locator";
				logger.error("ERROR:" + errMsg, serviceException);
				throw new GrtSapException(errMsg);
			}
	        try {
	        	//sapSvcURL = GRTPropertiesUtil.getProperty("sap_endpoint_url").trim();
	        	sapSvcURL = getSapEndPoint();
	            setRemoteEndPoint((Stub)portType, sapSvcURL);
	        } catch(Exception e) {
	            String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: " + e.getMessage();
	            logger.error("ERROR: " + errMsg, e);
	            throw new GrtSapException(errMsg);
	        }
	        //Prepare inputs and calling sap
	        if(installBaseDto != null){
	        	if(installBaseDto.getInstallBaseData().size() > 0){
	        		try{
	        		logger.debug("Prepare inputs");
	        		iBaseCreateRequest = new IBaseCreateRequestType();
	        		HeaderType header = new HeaderType();
	        		//header.setDestination(installBaseDto.getDestination());
	        		
	        		logger.debug("Original Destination:" + installBaseDto.getDestination());
	        		header.setDestination(GRTConstants.BBoxDestination);
	        		logger.debug("New Destination:" + header.getDestination());
	        		
	        		/*String systemPrefix = "";
	        		try {
	        			systemPrefix = GRTPropertiesUtil.getProperty("system_identifier");
	        		} catch (Throwable throwable) {
	        			logger.error("Skipping adding SystemPrefix");
	        		}*/
	        		/*
	        		String systemPrefix = getSystemIdentifier();
	        		String applicationMode = getApplicationMode();
	        		if (StringUtils.isNotEmpty(applicationMode) && !GRTConstants.APPLICATION_MODE_PROD.equalsIgnoreCase(applicationMode) && StringUtils.isNotEmpty(systemPrefix)) {
	        			header.setRegId(systemPrefix + installBaseDto.getRegistrationId());
	        		} else {
	        			header.setRegId(installBaseDto.getRegistrationId());
	        		}
	        		*/
	        		
	        		//Added field in GRT 4.0 - To be uncommented when the final client jar is checked in
	        		String systemPrefix = getSystemIdentifier();
	        		header.setSystemIdentifier(systemPrefix);
	        		header.setRegId(installBaseDto.getRegistrationId());
	        		
	        		//header.setRegId(installBaseDto.getRegistrationId());
	        		logger.debug("Sending install-base creation request for RegistrationId:" + header.getRegId());
	        		iBaseCreateRequest.setHeader(header);
	        		IBaseDataType[] iBaseDataType = new IBaseDataType[installBaseDto.getInstallBaseData().size()];
	        		logger.debug("Material Count:"+iBaseDataType.length);
	        		for(int i=0; i < iBaseDataType.length;i++ ){
	        			logger.debug("Material entry number:" + (i + 1));
	        			TechnicalOrderDetail iBaseData = installBaseDto.getInstallBaseData().get(i);
	        			logger.debug("Material Code:"+ iBaseData.getMaterialCode());
	        			iBaseDataType[i] = new IBaseDataType();
	        			iBaseDataType[i].setMaterialCode(iBaseData.getMaterialCode());
	        			logger.debug("Quantity:"+ iBaseData.getInitialQuantity());
	        			iBaseDataType[i].setQuantity(iBaseData.getInitialQuantity().toString());
	        			/*Blocking the SerailNumber:
	        			As B box serialized items with serialNumber, Do not get exported to Siebel.*/
	        			/* FMW will ignore SerialNumber for B box accounts:
	        			 * if(installBaseDto.getDestination().equalsIgnoreCase(GRTConstants.IBoxDestination)) {
	        				logger.debug("Serial Number:"+ iBaseData.getSerialNumber());
	        				iBaseDataType[i].setSerialNumber(iBaseData.getSerialNumber());
	        			}*/
        				logger.debug("Serial Number:"+ iBaseData.getSerialNumber());
        				iBaseDataType[i].setSerialNumber(iBaseData.getSerialNumber());
	        			logger.debug("Sold To:"+ iBaseData.getSoldToId());
	        			iBaseDataType[i].setSoldTo(iBaseData.getSoldToId());
	        			logger.debug("Ship To:"+ iBaseData.getShipToId());
	        			iBaseDataType[i].setShipTo(iBaseData.getShipToId());
	        			//logger.debug("Cut-Over Date:"+ iBaseData.getCutOverDate().getTime());
	        			iBaseDataType[i].setCutOverDate(iBaseData.getCutOverDate());
	        		}
	        		iBaseCreateRequest.setIBaseData(iBaseDataType);
	        		}catch (Exception e) {
	        			logger.debug("Exception while preparing the input parameters",e);
	        			throw e;
					}
	        		logger.debug("Retrying logic");
					try {
						//retry_count = Integer.parseInt(GRTPropertiesUtil.getProperty("retry_count").trim());
						retry_count = getMaximumRetry();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("ERROR: While getting the property retry_count");
						e.printStackTrace();
						throw new GrtSapException(e);
					} 
		        	for(int i = 0;i < retry_count; i++) {
		        		try{
		        			long ca1 = Calendar.getInstance().getTimeInMillis();
		        			logger.debug("Calling SAP for Install Base Creation");
		        			Stub stub = (Stub)portType;
		        			iBaseCreateResponse = portType.iBaseCreate(iBaseCreateRequest);
		        			if(iBaseCreateResponse != null){
		        				returnCode = this.processSAPResponse(iBaseCreateResponse.getCode());
		        			}
		        			long ca2 = Calendar.getInstance().getTimeInMillis();
		        	        logger.debug("TIMER:" + (ca2-ca1) +" milliseconds");
		        	        if(returnCode != null) {
			        			logger.debug("SAP call ended and return code determined, Total time taken:");
		        	        	return returnCode;
		        	        }
		        		}catch (IOException ioe ) {
		        			String errMsg ="Failed to connect FMW for Install Base Creation";
		        			logger.error(errMsg);
		        			if(i == retry_count - 1) {
			        			if(ioe.getCause() instanceof SOAPFaultException) {
			        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
			        					logger.error("MONITOR:[Outage:SAP][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				} else {
			        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				}
			        			} else {
			        				logger.error("FMW is down");
			        			}
			        			returnCode = GRTConstants.fmw_errorCode;
		                    }
		        		}catch(SOAPFaultException e){
		        			String errMsg = "Failed when sending data to SAP";
		        			logger.error(errMsg,e);
		        			throw e;
		        		}
		        	}
	        	}
	        }
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("TIMER:" + (c2-c1) +" milliseconds");
	        logger.debug("------ReturnCode------"+returnCode);
	        logger.debug("................ Exiting InstallBasecreation in SAP client Total time ................ ");
	        if(returnCode == null) {
	        	returnCode =GRTConstants.fmw_errorCode;
	        }
	        return returnCode;
	 }
	 
	 public String equipmentRemoval(String regId, String soldTo, List<TechnicalOrderDetail> selectedEquipments, String userId, String destination) throws Exception {
		    long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Starting for Equipment Removal ................");
	        String sapSvcURL = null;
	        EquipmentRemovalAckType equipmentRemovalAckType = null;
	        EquipmentRemovalWSDL_PortType portType = null;
	        EquipmentRemovalRequestType equipmentRemovalRequestType =null;
	        
	        int retry_count = 0;
	        String returnCode = null;
			try {
				portType = new EquipmentRemovalWSDL_Service_Impl().getEquipmentRemovalWSDLSOAP();
				logger.debug("----------PorType---------"+portType.toString());
			} catch (ServiceException serviceException) {
				// TODO Auto-generated catch block
				String errMsg = "Failure when getting the port type from the service locator";
				logger.error("ERROR:" + errMsg, serviceException);
				throw new GrtSapException(errMsg);
			}
	        try {
	        	//sapSvcURL = GRTPropertiesUtil.getProperty("eqr_endpoint_url").trim();
	        	sapSvcURL = getEqrEndPoint();
	        	setRemoteEndPoint((Stub)portType, sapSvcURL);
	        } catch(Exception e) {
	            String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: " + e.getMessage();
	            logger.error("ERROR: " + errMsg, e);
	            throw new GrtSapException(errMsg);
	        }
	        //Prepare inputs and calling sap
	        if(selectedEquipments != null){
	        	if(selectedEquipments.size() > 0){
	        		try{
		        		logger.debug("Prepare inputs");
		        		equipmentRemovalRequestType = new EquipmentRemovalRequestType();
		        		com.avaya.v1.equipmentremoval.HeaderType header = new com.avaya.v1.equipmentremoval.HeaderType();
		        		logger.debug("Original Destination:" + destination);
		        		destination = GRTConstants.BBoxDestination;
		        		header.setDestination(DestinationType.fromString(destination));
		        		//logger.debug("New Destination:"+DestinationType.fromString(destination));
		        		//header.setDestination(DestinationType.fromString(destination));
		        		logger.debug("RegistrationId:"+regId);
		        		header.setShipTo(soldTo);
		        		//logger.debug("RegistrationID:" + regId + ", Sold To :" + header.getShipTo());
		        		/*String systemPrefix = "";
		        		try {
		        			systemPrefix = GRTPropertiesUtil.getProperty("system_identifier");
		        		} catch (Throwable throwable) {
		        			logger.error("Skipping adding SystemPrefix");
		        		}*/
		        		
		        		/*
		        		String systemPrefix = getSystemIdentifier();
		        		String applicationMode = getApplicationMode();
		        		//if(StringUtils.isNotEmpty(systemPrefix)) {
		        		if(StringUtils.isNotEmpty(applicationMode) && !GRTConstants.APPLICATION_MODE_PROD.equalsIgnoreCase(applicationMode) && StringUtils.isNotEmpty(systemPrefix)) {
		        			header.setTransactionId(getSystemIdentifier() + regId);
		        		} else {
		        			header.setTransactionId(regId);
		        		}
		        		//header.setTransactionId(regId);
		        		*/
		        		
		        		//Added field in GRT 4.0 - To be uncommented when the final client jar is checked in
		        		String systemPrefix = getSystemIdentifier();
		        		header.setSystemIdentifier(systemPrefix);
		        		header.setTransactionId(regId);
		        		
		        		logger.debug("RegistrationID:" + header.getTransactionId() + ", Sold To :" + header.getShipTo() + ", New Destination:" +header.getDestination());
		        		equipmentRemovalRequestType.setHeader(header);
		        		EquipmentRemovalDataType[] equipmentRemovalDataType = new EquipmentRemovalDataType[selectedEquipments.size()];
		        		logger.debug("Material Count:"+equipmentRemovalDataType.length);
		        		for(int i=0; i < equipmentRemovalDataType.length;i++ ){
		        			logger.debug("Material entry number:" + (i + 1));
		        			TechnicalOrderDetail equipmentRemoval = selectedEquipments.get(i);
		        			equipmentRemovalDataType[i] = new EquipmentRemovalDataType();
		        			equipmentRemovalDataType[i].setEquipmentNumber(equipmentRemoval.getSummaryEquipmentNumber());
		        			logger.debug("Technical Order ID:"+ equipmentRemoval.getOrderId());
		        			logger.debug("Material Code:"+ equipmentRemoval.getMaterialCode());
		        			equipmentRemovalDataType[i].setMaterialCode(equipmentRemoval.getMaterialCode());
		        			logger.debug("Equipment Number:"+ equipmentRemoval.getSummaryEquipmentNumber());
		        			equipmentRemovalDataType[i].setQuantity(new BigInteger(equipmentRemoval.getRemainingQuantity().toString()));
		        			logger.debug("Quantity:"+ equipmentRemoval.getRemainingQuantity());
		        			if(null != equipmentRemoval.getActionType() && StringUtils.isNotEmpty(equipmentRemoval.getActionType())) {
		        				if(!GRTConstants.ACTION_TYPE_U.equalsIgnoreCase(equipmentRemoval.getActionType())) {
		        					equipmentRemovalDataType[i].setSerialNumber(equipmentRemoval.getSerialNumber());
		        					logger.debug("Serial Number:"+ equipmentRemoval.getSerialNumber());
		        				}
		        			}
		        			//equipmentRemovalDataType[i].setAction(ActionType.fromString(GRTConstants.ACTION_TYPE_U));
		        			if(null != equipmentRemoval.getActionType() && StringUtils.isNotEmpty(equipmentRemoval.getActionType())) {
		        				equipmentRemovalDataType[i].setAction(ActionType.fromString(equipmentRemoval.getActionType()));
		        				logger.debug("Action Type:"+ActionType.fromString(equipmentRemoval.getActionType()));
		        			} else {
		        				equipmentRemovalDataType[i].setAction(ActionType.fromString(GRTConstants.ACTION_TYPE_U));
		        				logger.debug("Action Type:"+ActionType.fromString(GRTConstants.ACTION_TYPE_U));
		        			}
		        			
		        			//System.out.println("Action Type:"+ActionType.fromString(equipmentRemoval.getActionType()));
		        		}
		        		equipmentRemovalRequestType.setEquipmentRemovalData(equipmentRemovalDataType);
	        		}catch (Exception e) {
	        			logger.debug("Exception while preparing the input parameters",e);
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
		        	for(int i = 0;i < retry_count; i++) {
		        		try{
		        			long ca1 = Calendar.getInstance().getTimeInMillis();
		        			logger.debug("Calling SAP for Equipment Removal");
		        			Stub stub = (Stub)portType;
		        			equipmentRemovalAckType = portType.equipmentRemoval(equipmentRemovalRequestType);
		        			if(equipmentRemovalAckType != null){
		        				returnCode = this.processSAPResponse(equipmentRemovalAckType.getCode());
		        			}
		        			long ca2 = Calendar.getInstance().getTimeInMillis();
		        	        logger.debug("TIMER:" + (ca2-ca1) +" milliseconds");
		        	        if(returnCode != null) {
			        			logger.debug("SAP call ended and return code determined, Total time taken:");
		        	        	return returnCode;
		        	        }
		        		}catch (IOException ioe ) {
		        			String errMsg ="Failed to connect FMW for Equipment Removal";
		        			logger.error(errMsg);
		        			if(i == retry_count - 1) {
			        			if(ioe.getCause() instanceof SOAPFaultException) {
			        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
			        					logger.error("MONITOR:[Outage:SAP][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				} else {
			        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				}
			        			} else {
			        				logger.error("FMW is down");
			        			}
			        			returnCode = GRTConstants.fmw_errorCode;
		                    }
		        		}catch(SOAPFaultException e){
		        			String errMsg = "Failed when sending data to SAP";
		        			logger.error(errMsg,e);
		        			throw e;
		        		}
		        	}
	        	}
	        }
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("TIMER for SAP transaction for Equipment Removal for RegID :" + regId +" time in milliseconds :"+(c2-c1));
	        logger.debug("------ReturnCode------"+returnCode);
	        logger.debug("................ Exiting Equipment Removal in SAP client Total time ................ ");
	        if(returnCode == null) {
	        	returnCode =GRTConstants.fmw_errorCode;
	        }
	        return returnCode;
	 }
	 
	 
	 public String equipmentMove(String regId, String fromSoldTo, List<TechnicalOrderDetail> selectedEquipments, String userId, String toSoldTo) throws Exception {
		    long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Starting for Equipment Move ................");
	        String sapSvcURL = null;
	        EquipmentMoveAckType equipmentMoveAckType = null;
	        EquipmentMoveWSDL_PortType portType = null;
	        EquipmentMoveRequestType equipmentMoveRequestType =null;
	        
	        int retry_count = 0;
	        String returnCode = null;
			try {
				portType = new EquipmentMoveWSDL_Service_Impl().getEquipmentMoveWSDLSOAP();
				logger.debug("----------PorType---------"+portType.toString());
			} catch (ServiceException serviceException) {
				// TODO Auto-generated catch block
				String errMsg = "Failure when getting the port type from the service locator";
				logger.error("ERROR:" + errMsg, serviceException);
				throw new GrtSapException(errMsg);
			}
	        try {
	        	//sapSvcURL = GRTPropertiesUtil.getProperty("eqm_endpoint_url").trim();
	        	sapSvcURL = getEqmEndPoint();
	        	setRemoteEndPoint((Stub)portType, sapSvcURL);
	        } catch(Exception e) {
	            String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: " + e.getMessage();
	            logger.error("ERROR: " + errMsg, e);
	            throw new GrtSapException(errMsg);
	        }
	        //Prepare inputs and calling sap
	        if(selectedEquipments != null){
	        	if(selectedEquipments.size() > 0){
	        		try{
		        		logger.debug("Prepare inputs");
		        		equipmentMoveRequestType = new EquipmentMoveRequestType();
		        		
		        		com.avaya.v4.equipmentmove.HeaderType header = new com.avaya.v4.equipmentmove.HeaderType();
		        		logger.debug("From soldTo:" + fromSoldTo);
		        		header.setFromSoldTo(fromSoldTo);
		        		logger.debug("To soldTo:" + toSoldTo);
		        		header.setToSoldTo(toSoldTo);
		        		String systemPrefix = getSystemIdentifier();
		        		header.setSystemIdentifier(systemPrefix);
		        		/*String applicationMode = getApplicationMode();
		        		if(StringUtils.isNotEmpty(applicationMode) && !GRTConstants.APPLICATION_MODE_PROD.equalsIgnoreCase(applicationMode) && StringUtils.isNotEmpty(systemPrefix)) {
		        			header.setTransactionId(getSystemIdentifier() + regId);
		        		} else {
		        			header.setTransactionId(regId);
		        		}	*/	        		
		        		header.setTransactionId(regId);
		        		logger.debug("RegistrationID:" + header.getTransactionId());
		        		
		        		equipmentMoveRequestType.setHeader(header);
		        		
		        		
		        		EquipmentMoveDataType[] equipmentMoveDataType = new EquipmentMoveDataType[selectedEquipments.size()];
		        		logger.debug("Material Count:"+equipmentMoveDataType.length);
		        		for(int i=0; i < equipmentMoveDataType.length;i++ ){		        			
		        			equipmentMoveDataType[i] = new EquipmentMoveDataType();
		        			TechnicalOrderDetail equipmentMove = selectedEquipments.get(i);
		        			
		        			logger.debug("Material entry number:" + (i + 1));
		        			logger.debug("Technical Order ID:"+ equipmentMove.getOrderId());
		        			
		        			logger.debug("Equipment Number:"+ equipmentMove.getSummaryEquipmentNumber());
		        			equipmentMoveDataType[i].setEquipmentNumber(equipmentMove.getSummaryEquipmentNumber());		        			
		        			logger.debug("Material Code:"+ equipmentMove.getMaterialCode());
		        			equipmentMoveDataType[i].setMaterialCode(equipmentMove.getMaterialCode());
		        			logger.debug("Quantity:"+ equipmentMove.getRemovedQuantity());
		        			equipmentMoveDataType[i].setQuantityToMove(new BigInteger(equipmentMove.getRemovedQuantity().toString()));		        			
		        			logger.debug("Action Type:"+ActionType.fromString(GRTConstants.ACTION_TYPE_U));
		        			equipmentMoveDataType[i].setAction(com.avaya.v4.equipmentmove.ActionType.fromString(GRTConstants.ACTION_TYPE_U));
		        			
		        		}
		        		equipmentMoveRequestType.setEquipmentMoveData(equipmentMoveDataType);
	        		}catch (Exception e) {
	        			logger.debug("Exception while preparing the input parameters",e);
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
		        	for(int i = 0;i < retry_count; i++) {
		        		try{
		        			long ca1 = Calendar.getInstance().getTimeInMillis();
		        			logger.debug("Calling SAP for Equipment Move");
		        			Stub stub = (Stub)portType;
		        			equipmentMoveAckType = portType.equipmentMove(equipmentMoveRequestType);
		        			if(equipmentMoveAckType != null){
		        				returnCode = this.processSAPResponse(equipmentMoveAckType.getCode());
		        			}
		        			long ca2 = Calendar.getInstance().getTimeInMillis();
		        	        logger.debug("TIMER:" + (ca2-ca1) +" milliseconds");
		        	        if(returnCode != null) {
			        			logger.debug("SAP call ended and return code determined, Total time taken:" + (ca2-ca1) +" milliseconds");
		        	        	return returnCode;
		        	        }
		        		}catch (IOException ioe ) {
		        			String errMsg ="Failed to connect FMW for Equipment Removal";
		        			logger.error(errMsg);
		        			if(i == retry_count - 1) {
			        			if(ioe.getCause() instanceof SOAPFaultException) {
			        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
			        					logger.error("MONITOR:[Outage:SAP][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				} else {
			        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ sapSvcURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				}
			        			} else {
			        				logger.error("FMW is down");
			        			}
			        			returnCode = GRTConstants.fmw_errorCode;
		                    }
		        		}catch(SOAPFaultException e){
		        			String errMsg = "Failed when sending data to SAP";
		        			logger.error(errMsg,e);
		        			throw e;
		        		}
		        	}
	        	}
	        }
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("TIMER for SAP transaction for Equipment Move for RegID :" + regId +" time in milliseconds :"+(c2-c1));
	        logger.debug("------ReturnCode------"+returnCode);
	        logger.debug("................ Exiting Equipment Move in SAP client Total time ................ ");
	        if(returnCode == null) {
	        	returnCode = GRTConstants.fmw_errorCode;
	        }
	        return returnCode;
	 }
	 
	 @Deprecated
	 public String customerLookup (String customerNumber) throws Exception {
		 logger.debug("Entering customerLookup, customerNumber:" + customerNumber);
        long c1 = Calendar.getInstance().getTimeInMillis();
        CustomerLookup port;
        String destination = null;
        String lookupURL = null;
        int retry_count = 0;

        try {
            port = new CustomerLookupService_Impl().getCustomerLookupPort();
        } catch (Exception serviceException) {
            String errMsg = "Failure when getting the port type from the service locator";
            logger.error("ERROR:"+ errMsg, serviceException);
            throw new GrtSapException(serviceException);
        }

        try {
            //lookupURL = GRTPropertiesUtil.getProperty("customerlookup_endpoint_url").trim();
        	lookupURL = getCustomerLookupEndPoint();
            setRemoteEndPoint((Stub) port, lookupURL);
        } catch (Exception e) {
            String errMsg = "Failure when trying to read Sap endpoint properties for setup. Original error: "
                + e.getMessage();
            logger.error("ERROR: " + errMsg, e);
            throw new GrtSapException(errMsg);
        }

        try{
            //retry_count = Integer.parseInt(GRTPropertiesUtil.getProperty("retry_count").trim());
        	retry_count = getMaximumRetry();
        } catch (Exception e) {
            logger.error("ERROR: While getting the property retry_count", e);
            throw new GrtSapException(e);
        }

        CustomerLookupRequest request = new CustomerLookupRequest();
        request.setCustomerNumber(customerNumber);

        for (int i = 0; i < retry_count; i++) {
            try {
                logger.debug("Calling FMW for Customer Lookup");
                CustomerLookupResponse response = port.customerLookup(request);
                logger.debug("Message  		: " + response.getMessage());
                logger.debug("Destination 	: " + response.getDestination());
                destination = response.getDestination();
                if (!GRTConstants.BBoxDestination.equalsIgnoreCase(destination) && !GRTConstants.IBoxDestination.equalsIgnoreCase(destination))
                {
                    destination = GRTConstants.BBoxDestination;
                }
                i = retry_count;
            } catch (IOException ioe) {
                String errMsg = "Failed to connect FMW for Customer Lookup";
                logger.error(errMsg);
                if (i == retry_count - 1) {
                    logger.error(errMsg, ioe);
                    throw ioe;
                }
            } catch (SOAPFaultException e) {
                String errMsg = "Failed when sending data to FMW Service";
                logger.error(errMsg, e);
                throw e;
            } catch (Exception e) {
                logger.error("e.getMessage() :" + e.getMessage());
                throw e;
            }
        }

        logger.debug("TIMER for fmw transaction for Customer Lookup  time in milliseconds :"
            + (Calendar.getInstance().getTimeInMillis() - c1));
        return destination;
    }
	 
	 /*
	 private void sendEmailToSystemAdmin(SiteRegistration siteRegistration,String errMsg){
		 RegistrationRequestAlert result = null;
		 result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION);
		 result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_FMW);
		 result.setSendMail(false);
         result.setIsSystemMail(true);
         result.setActionRequired(errMsg);
         try {
             MailUtil.getInstance().sendMailNotification(result);
             logger.debug("Completed.");
         } catch(Exception e) {
             logger.error("ERROR: Unexpected failure while trying send email with error: " 
                     + e.getMessage());
         }
	 }
	 */
	 
	 private String processSAPResponse(String code) {
		 String returnCode=null;
		if(StringUtils.isNotEmpty(code)) {
			 if(code.equals("S")) {
				 returnCode = GRTConstants.installBase_successCode;
			 } else if(code.equals("E")) {
				 returnCode = GRTConstants.installBase_errorCode;
			 } else if(code.equals("1000")) {
				 returnCode = GRTConstants.sapDown_errorCode;
			 } else {
				 returnCode = code;
			 }
		 }
		 logger.debug("Response Code: "+code);
		 return returnCode;
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

	public String getSapEndPoint() {
		return sapEndPoint;
	}

	public void setSapEndPoint(String sapEndPoint) {
		this.sapEndPoint = sapEndPoint;
	}

	public String getEqrEndPoint() {
		return eqrEndPoint;
	}

	public void setEqrEndPoint(String eqrEndPoint) {
		this.eqrEndPoint = eqrEndPoint;
	}

	public String getCustomerLookupEndPoint() {
		return customerLookupEndPoint;
	}

	public void setCustomerLookupEndPoint(String customerLookupEndPoint) {
		this.customerLookupEndPoint = customerLookupEndPoint;
	}

	public int getMaximumRetry() {
		return maximumRetry;
	}

	public void setMaximumRetry(int maximumRetry) {
		this.maximumRetry = maximumRetry;
	}

	public String getSystemIdentifier() {
		return systemIdentifier;
	}

	public void setSystemIdentifier(String systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}

	public String getApplicationMode() {
		return applicationMode;
	}

	public void setApplicationMode(String applicationMode) {
		this.applicationMode = applicationMode;
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
    

	public String getEqmEndPoint() {
		return eqmEndPoint;
	}

	public void setEqmEndPoint(String eqmEndPoint) {
		this.eqmEndPoint = eqmEndPoint;
	}

	public static void main(String [] args)  throws Exception {		
		SapClient sc = new SapClient();
		
		String accountLocation = "0002726514";
		int maximumRetry = 1;
				
		sc.setAlsbUser("GRTWS");
		//sc.setAlsbPasswd("GRTWSuat2010");
		sc.setAlsbPasswd("zsjYeGD0KqK6gF9c9GL8aA==");
		sc.setMaximumRetry(maximumRetry);
		
		
		sc.setSapEndPoint("https://fmwuat3.avaya.com:8102/GRT2/v1/Proxy_Services/iBaseCreate_PS");		
		
				
		//Have to check
	}
	
}
