package com.avaya.grt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.avaya.grt.dao.TechnicalRegistrationAsyncDao;
import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.dao.TechnicalRegistrationErrorDao;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.Activity;
import com.grt.dto.ErrorDto;
import com.grt.dto.RegistrationRequestAlert;
import com.grt.dto.SolutionElementListDto;
import com.grt.dto.TechRegDataResponseDto;
import com.grt.dto.TechRegResultResponseDto;
import com.grt.util.ARTOperationType;
import com.grt.util.AccessTypeEnum;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.OpTypeEnum;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationRequestAlertConvertor;
import com.grt.util.SkipDateTimeEscalator;
import com.grt.util.StatusEnum;
import com.grt.util.TechRecordEnum;



public class TechnicalRegistrationErrorService extends TechnicalRegistrationService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationErrorService.class);
	private TechnicalRegistrationErrorDao technicalRegistrationErrorDao;
	private static Integer numberOfSubmit=new Integer(0);

	public void processErrorIB(ErrorDto errorDto) throws Throwable {
		logger.debug("Entering TechnicalRegistrationErrorService.processErrorIB");
		String registrationId = errorDto.getRegistrationId();
		getTechnicalRegistrationErrorDao().updateSiteRegistrationSubmittedFlag(registrationId, GRTConstants.isSubmitted_false);
		
		//Sending mail to System Admin
		sendEmailToSystemAdmin(registrationId, errorDto.getErrorDesc(), ProcessStepEnum.INSTALL_BASE_CREATION);
		logger.debug("Exiting TechnicalRegistrationErrorService.processErrorIB");
	}
	
	public void processErrorTR(ErrorDto errorDto) throws Throwable {
		logger.debug("Entering TechnicalRegistrationErrorService.processErrorTR");
    	//TODO: Not sure, If returnCode will be 0.
    		if (errorDto.getErrorId() !=null && errorDto.getErrorId().equals("0") ) {
    			if (errorDto.getRegistrationId() != null ) {
    				try {
    					failureCase(errorDto.getRegistrationId(), null, errorDto.getErrorCode(), errorDto.getErrorDesc(), "NO-OPT");
    				} catch (Exception e) {
    					logger.error("Error!! while retrieving the techRegistration",e);
    				}
    			}
    		} else if (errorDto.getErrorId().equals("1000")) {
    			logger.debug("Backend System is down");
    			String errorMsg = "ART System is down";
    			//send a mail to system Admin.
    			try {
    				sendSystemAlert(errorDto.getRegistrationId(), GRTConstants.SYSTEM_MAIL_DESTINATION_ART, errorMsg);
    			} catch (Exception e) {
    				logger.error("Error!! while sending mail to system Admin",e);
    			}
    		}
    	logger.debug("Exiting TechnicalRegistrationErrorService.processErrorTR");
	}
	
	public void failureCase(String grtId, String artSR, String returnCode, String errorDesc, String optType) {
    	logger.debug("Starting TechnicalRegistrationErrorService.failureCase for GRTID:" + grtId);
    	String artCreatedSR = null;
    	if(StringUtils.isNotEmpty(artSR)) {
    		logger.debug("Ignoring ART created SR:" + artSR);
    		artCreatedSR = artSR;
    		artSR = null;
    	}
    	TechnicalRegistration technicalRegistration = null;
    	SiteRegistration siteRegistration = null;
    	SiteList siteList = null;
    	SalProductRegistration salProductRegistration = new SalProductRegistration();
    	try {
    	
    	if ((technicalRegistration = getTechnicalRegistrationErrorDao().getTechnicalRegistration(grtId)) != null) {
    		//TODO: Store returnCode and ARTSR.
    		//TODO: If ARTSR is null, create SR and update either SR No. or TR record with SRRequest.
    		//TODO: Update TR status to "Awaiting Info.
    		//TODO: Set siteRegistration TR status to Awaiting Info
    		
    		if(!(optType.equals(OpTypeEnum.PASSWORDRESET.getOpType()) || optType.equals(OpTypeEnum.ONBOARDINGXML.getOpType()))) {
    			Status status = new Status();
    			status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
    			technicalRegistration.setStatus(status);    			
    		}
    		if(optType.equals(TechRecordEnum.IP_MODEM.getSalType())) {
    			synchronized(numberOfSubmit) {
    				numberOfSubmit = Integer.valueOf(technicalRegistration.getNumberOfSubmit());
    				if(numberOfSubmit < 2 && !technicalRegistration.getStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
    					numberOfSubmit = numberOfSubmit + 1;
    					technicalRegistration.setNumberOfSubmit(String.valueOf(numberOfSubmit));
    					logger.debug("handle no of Submit for sucessCase:");
    				} else {
        				logger.debug("Not incrementing number of Submits, existing numbers of submit is:" + numberOfSubmit);    					
    				}
    				
    			}
    		}
    		
    		technicalRegistration.setErrorCode(returnCode);
    		if (StringUtils.isNotEmpty(returnCode)) {
    			technicalRegistration.setErrorCode(returnCode);
    		}
    		if (StringUtils.isNotEmpty(errorDesc)) {
    			technicalRegistration.setErrorDesc(errorDesc);  
    		}
    		
    		if(StringUtils.isNotEmpty(artCreatedSR)){
   				technicalRegistration.setArtCreatedSrNo(artCreatedSR);
    		}
    		if(StringUtils.isNotEmpty(technicalRegistration.getArtSrNo())) {
    			artSR = technicalRegistration.getArtSrNo();
    		}
        	if (StringUtils.isNotEmpty(artSR)) {
        		technicalRegistration.setArtSrNo(artSR);
        	} else {
        		logger.debug("SR is not found creating SR"); 
        		try{
        		SRRequest srRequest = null;
        		salProductRegistration.setTechnicalRegistration(technicalRegistration);
        		if(StringUtils.isNotEmpty(technicalRegistration.getAccessType()) && technicalRegistration.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO)){
        			
        			srRequest = this.createSR(technicalRegistration.getTechnicalRegistrationId(), TechRecordEnum.IPO, returnCode, errorDesc);
        		} else {
        			srRequest = this.createSR(technicalRegistration.getTechnicalRegistrationId(), TechRecordEnum.IP_MODEM, returnCode, errorDesc);
        		}
        		if(srRequest!= null && StringUtils.isNotEmpty(srRequest.getSiebelSRNo())){
        			logger.debug("SR is not Null and Siebel SR Number "+srRequest.getSiebelSRNo()); 
        			technicalRegistration.setSrRequest(srRequest);
        			technicalRegistration.setArtSrNo(srRequest.getSiebelSRNo());
				}
        		}catch (Exception e) {
					logger.error("Error! while creating SR for technicalRegistration for ID[Sync]: " +technicalRegistration.getTechnicalRegistrationId()+ "", e);
				}
        	}
        	
        	getTechnicalRegistrationErrorDao().saveTechnicalRegistration(technicalRegistration);
            String registrationID = technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId();
            siteRegistration = getTechnicalRegistrationErrorDao().getSiteRegistration(registrationID);
            if(!(optType.equals(OpTypeEnum.PASSWORDRESET.getOpType()) || optType.equals(OpTypeEnum.ONBOARDINGXML.getOpType()))) {
            	if(siteRegistration != null && !siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
            		getTechnicalRegistrationErrorDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
            	}
            }
            sendRegistrationRequestAlert(technicalRegistration, StatusEnum.AWAITINGINFO);
    		logger.debug("Completed failure case for IP/Modem/IPO TR.");
    		return;
    		
    	} else if ((siteList = getTechnicalRegistrationErrorDao().getSiteList(grtId)) !=null ) {
			logger.debug("found the grt id in SiteList"+siteList.getId());
			if (StringUtils.isNotEmpty(returnCode)){
				siteList.setErrorCode(returnCode);
			}
			if (StringUtils.isNotEmpty(errorDesc)){
				siteList.setErrorDesc(errorDesc);
			}
			Status s = new Status();
			s.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
			siteList.setStatus(s);
				if (StringUtils.isNotEmpty(artSR)) {
					siteList.setArtSrNo(artSR);
				} else {
					try {
						logger.debug("SR is not found creating SR");
						SRRequest srRequest = this.createSR(siteList.getId(), TechRecordEnum.SAL_SITE_LIST, returnCode, errorDesc);
						
						if(srRequest!=null && StringUtils.isNotEmpty(srRequest.getSiebelSRNo())){
							logger.debug("SR is not Null and Siebel SR Number "+srRequest.getSiebelSRNo()); 
							siteList.setSrRequest(srRequest);
						}
					}catch (Exception e) {
    					logger.error("Error! while creating SR for siteList for ID[Sync]: " +siteList.getId()+ "", e);
    				}
				}			
				getTechnicalRegistrationErrorDao().saveSalSiteList(siteList);
			siteRegistration = siteList.getSiteRegistration();
			if(siteRegistration != null && !siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())){
				getTechnicalRegistrationErrorDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
        	}
		}
    	}catch (DataAccessException dae){
    		logger.debug("Error while accessing dao objects:"+dae.getMessage());
    		logger.error("Error!",dae);
    	} catch (Exception e) {
    		logger.error("Error!",e);
    	}
    	logger.debug("Exiting TechnicalRegistrationErrorService.failureCase");
    	
    }

	public TechnicalRegistrationErrorDao getTechnicalRegistrationErrorDao() {
		return technicalRegistrationErrorDao;
	}

	public void setTechnicalRegistrationErrorDao(
			TechnicalRegistrationErrorDao technicalRegistrationErrorDao) {
		this.technicalRegistrationErrorDao = technicalRegistrationErrorDao;
	}

	
	
}
