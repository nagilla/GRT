package com.grt.integration.art;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.v1.esrpermissionlist.LOAQueryRequestType;
import com.avaya.v1.esrpermissionlist.LOAQueryResponseType;
import com.avaya.v1.esrpermissionlist.PermissionType;
import com.avaya.v1.esrpermissionlist.QueryResponseType;
import com.avaya.v1.offboarddevice.OffboardDeviceRequest;
import com.avaya.v1.offboarddevice.OffboardDeviceResponse;
import com.avaya.v1.offboarddevice.clientproxy.OffboardDeviceWSDL_PortType;
import com.avaya.v1.offboarddevice.clientproxy.OffboardDeviceWSDL_Service_Impl;
import com.grt.integration.loabinding.fmwclientproxy.LOA;
import com.grt.integration.loabinding.fmwclientproxy.LOABindingQSService_Impl;
import com.grt.util.GRTConstants;
import com.grt.util.PasswordUtil;

public class GenericClient {
	private static final Logger logger = Logger.getLogger(GenericClient.class);
	
	private String alsbUser;
    private String alsbPasswd;
    private String offboardEndPoint;
    private int artConnectionTimeOut;
    private int maximumRetry;
    private String loaEndPoint;
    private PasswordUtil passwordUtils;
    

	private void setRemoteEndPoint(Stub stub, String url) throws Exception {
		logger.debug("Setting for URL [" + url + "]");
		logger.debug("Stub:" + stub);

		stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
		stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, getAlsbUser());
		stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, decryptAlsbPasswd());
	}

	public void offboardDevices(String registrationId, List<String> seids)
			throws Throwable {
    	logger.debug("Entering offboardDevices for registrationId:" + registrationId);
		String offBoardEndUrl = null;
		int retry_count = 0;
		int timeout = 0;
		OffboardDeviceWSDL_PortType portType = null;

		try {
			portType = new OffboardDeviceWSDL_Service_Impl()
					.getOffboardDeviceWSDLSOAP();
			logger.debug("----------PorType---------" + portType.toString());
		} catch (ServiceException serviceException) {
			// TODO Auto-generated catch block
			String errMsg = "Failure when getting the port type from the service locator";
			logger.error("ERROR:" + errMsg, serviceException);
			throw serviceException;
		}

		try {
			offBoardEndUrl = getOffboardEndPoint();
			timeout = getArtConnectionTimeOut();
			// systemIdentifier =
			// GRTPropertiesUtil.getProperty("system_identifier").trim();
			retry_count = getMaximumRetry();
			logger.debug("endURL:" + offBoardEndUrl);
			logger.debug("timeout:" + timeout);
			logger.debug("retry_count:" + retry_count);
			setRemoteEndPoint((Stub) portType, offBoardEndUrl);
		} catch (Exception e) {
			logger
					.error(
							"Failure while trying to read FMW endpoint properties for setup.",
							e);
		}

		OffboardDeviceRequest request = new OffboardDeviceRequest();
		request.setRequestDescription("GRT Registration ID:" + registrationId);
		request.setServiceLevel(GRTConstants.OFFBOARD_DEVICE_SERVICE_LEVEL);
		request.setServiceType(GRTConstants.OFFBOARD_DEVICE_SERVICE_TYPE);
		String productIdentifier = "";
		if (seids != null && seids.size() > 0) {
			for (String seid : seids) {
				productIdentifier += seid + "|";
			}
		} else {
			throw new Exception("Seids can not be blank");
		}
        request.setProductIdentifier(productIdentifier.substring(0, productIdentifier.length()-1));
        logger.debug("ProductIdentifier:" + request.getProductIdentifier());
        for(int i = 0;i < retry_count; i++) {
    		try{
    			OffboardDeviceResponse  response = portType.offboardDevice(request);
    			if(response != null && StringUtils.isNotEmpty(response.getRequest_ID())) { 
    				logger.info("Registration ID:" + registrationId + " is submitted successfully for OffBoarding:" + request.getProductIdentifier() + " with requestID:" + response.getRequest_ID());
    			}
    		} catch (IOException ioe ) {
        		logger.error("Failed to connect FMW for Offboard device alarm", ioe);
        		if(i == retry_count - 1) {
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:ART-OFFBOARD][ENDPOINT:"+ offBoardEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ offBoardEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
                }
	        }catch(SOAPFaultException e){
    			logger.error("Failed while sending data to FMW for Offboard device alarm", e);
    			throw e;
	        }
	        
    	}
        logger.debug("Exiting offboardDevices for registrationId:" + registrationId);
	}

	public List<BPAccountTempAccess> queryAccess(String bpLinkId, String soldToId) throws Exception {
		logger.debug("Entering queryAccess for bpLinkId:" + bpLinkId + ", soldToId:" + soldToId);
		String loaEndUrl = null;
		int retry_count = 0;
		int timeout = 0;
		List<BPAccountTempAccess> bps = new ArrayList<BPAccountTempAccess>();
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
			loaEndUrl = getLoaEndPoint();
			timeout = getArtConnectionTimeOut();
			retry_count = getMaximumRetry();
			logger.debug("endURL:" + loaEndUrl);
			logger.debug("timeout:" + timeout);
			logger.debug("retry_count:" + retry_count);
			setRemoteEndPoint((Stub) portType, loaEndUrl);
		} catch (Exception e) {
			logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
		}

		LOAQueryRequestType request = new LOAQueryRequestType();
		request.setBPLinkId(bpLinkId);
		if(StringUtils.isNotEmpty(soldToId)) {
			request.setSoldTo(soldToId);
		}
		request.setPermissionType(PermissionType.registration);
		
		for (int i = 0; i < retry_count; i++) {
			try {
				LOAQueryResponseType response = portType.lOAQueryOperation(request);
				if(response != null && response.getStatus()!= null && response.getStatus().getCode() != null && StringUtils.isNotEmpty(response.getStatus().getCode()) && response.getStatus().getCode().equals("0")) {
					logger.info("Success for queryAccess on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId);
					if(response.getQueryResponse() != null) {
						for (QueryResponseType queryResponse : response.getQueryResponse()) {
							if(queryResponse != null) {
								BPAccountTempAccess bp = new BPAccountTempAccess();
								bp.setBpLinkId(bpLinkId);
								bp.setAccountId(queryResponse.getSoldTo());
								bp.setAccountName(queryResponse.getSoldToName());
								bps.add(bp);
							}
						}
					}
				} else {
					logger.info("Error for upsert on bpLinkId:" + bpLinkId + ", soldToId:" + soldToId + " code" + response.getStatus().getCode() + " description:" + response.getStatus().getDescription());
				}
			} catch (IOException ioe) {
				logger.error("Failed to connect FMW for LOA", ioe);
				if(i == retry_count - 1) {
					if(ioe.getCause() instanceof SOAPFaultException) {
						if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
							logger.error("MONITOR:[Outage:LOA-QUERY][ENDPOINT:"+ loaEndUrl + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
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
		logger.debug("Exiting queryAccess for bpLinkId:" + bpLinkId + ", soldToId:" + soldToId);
		return bps;
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

	public String getOffboardEndPoint() {
		return offboardEndPoint;
	}

	public void setOffboardEndPoint(String offboardEndPoint) {
		this.offboardEndPoint = offboardEndPoint;
	}

	public int getArtConnectionTimeOut() {
		return artConnectionTimeOut;
	}

	public void setArtConnectionTimeOut(int artConnectionTimeOut) {
		this.artConnectionTimeOut = artConnectionTimeOut;
	}

	public int getMaximumRetry() {
		return maximumRetry;
	}

	public void setMaximumRetry(int maximumRetry) {
		this.maximumRetry = maximumRetry;
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
		return PasswordUtil.decrypt(getAlsbPasswd());
	}
}
