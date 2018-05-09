package com.avaya.grt.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.dao.TechnicalRegistrationSRDao;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.ServiceRequest;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.SkipDateTimeEscalator;
import com.grt.util.StatusEnum;



public class TechnicalRegistrationSRService extends TechnicalRegistrationService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationSRService.class);
	private List<ServiceRequest> SRsInProcess = new ArrayList<ServiceRequest>();
	private TechnicalRegistrationSRDao technicalRegistrationSRDao;

	
	/**
     * Process the Received SiebelSR
     *
     * @param serviceRequestDto ServiceRequest
	 * @return 
     * @throws Exception
     */
	public ServiceRequest processReceivedSiebelSR(
			ServiceRequest serviceRequestDto) throws Throwable {
		StringBuffer sb = new StringBuffer();
		ServiceRequest serviceRequestDtofromUpdate = new ServiceRequest();
		sb.append(serviceRequestDto.getRoutingInfo().toString());
		sb.append("Entering into TechnicalRegistrationSRService.processReceivedSiebelSR...");
		logger.debug("<=======================================Entering into TechnicalRegistrationSRService.processReceivedSiebelSR()============>");
		try {
			 logger.debug("Processing incoming message to GRT back-end service for the following: \n"
            + "SR Number [" + serviceRequestDto.getSrNumber() + "]\n"
            + "SR Status [" + serviceRequestDto.getStatus() + "]\n"
            + "Resolution Action [" + serviceRequestDto.getResolutionAction() + "]"); 
			 
			SkipDateTimeEscalator.set(Boolean.TRUE);
			String incomingRequestDetails = "SR Number ["
					+ serviceRequestDto.getSrNumber() + "], " + "SR ID ["
					+ serviceRequestDto.getSrId() + "], " + "SR Type ["
					+ serviceRequestDto.getSrType() + "], " + "SR SubType ["
					+ serviceRequestDto.getSrSubType() + "], " + "SR Status ["
					+ serviceRequestDto.getStatus() + "] \n" + "SR SubStatus ["
					+ serviceRequestDto.getSubStatus() + "]";
			logger.debug("Starting with incoming request: "
					+ incomingRequestDetails);
			sb.append("Starting with incoming request with all the details...");
			int numOfTotalRetry = 3;
			for (int i = 0; i < numOfTotalRetry; i++) {
				try {
					logger.debug("processReceivedSiebelSR --> Resolution Action:"
							+ serviceRequestDto.getResolutionAction());

					if (GRTConstants.SIEBEL_SR_STATUS_PENDING
							.equalsIgnoreCase(serviceRequestDto.getStatus())
							&& GRTConstants.SIEBEL_SR_SUB_STATUS_CUSTOMER
									.equalsIgnoreCase(serviceRequestDto
											.getSubStatus())) {
						sb.append("Update Registration status for - PENDING AND CUSTOMER BLOCK...");
						// GRT Receives SR Update from Siebel Use Case
						logger.debug("<======== PENDING AND CUSTOMER BLOCK ===========>");
						serviceRequestDtofromUpdate = updateRegistrationStatus(serviceRequestDto);
						sb.append(serviceRequestDtofromUpdate.getRoutingInfo().toString());
					} else if ((GRTConstants.SIEBEL_SR_STATUS_COMPLETED.equalsIgnoreCase(serviceRequestDto.getStatus()) 
							|| GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus()))
							&& GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_CONNECTIVITY_AND_ALARM_TESTING
									.equalsIgnoreCase(serviceRequestDto
											.getSrSubType())
							&& StringUtils.isNotEmpty(serviceRequestDto
									.getResolutionAction())
							&& GRTConstants.SIEBEL_SR_STATUS_CANCELLED
									.equalsIgnoreCase(serviceRequestDto
											.getResolutionAction())) {
						logger.debug("<======= COMPLETED With resolution action as Cancelled ===>");
						sb.append(" COMPLETED With resolution action as Cancelled - Undo Step B Process...");
						undoStepBProcess(serviceRequestDto);
					} else if (GRTConstants.SIEBEL_SR_STATUS_COMPLETED
							.equalsIgnoreCase(serviceRequestDto.getStatus())
							|| GRTConstants.SIEBEL_SR_STATUS_CANCELLED
									.equalsIgnoreCase(serviceRequestDto
											.getStatus())
							|| GRTConstants.SIEBEL_SR_STATUS_RESOLVED
									.equalsIgnoreCase(serviceRequestDto
											.getStatus())
							|| GRTConstants.SIEBEL_SR_STATUS_RESTORE
									.equalsIgnoreCase(serviceRequestDto
											.getStatus())) {
						logger.debug("<======= COMPLETED OR CANCELLED OR RESOLVED OR RESTORE BLOCK ===>");
						sb.append(" COMPLETED OR CANCELLED OR RESOLVED OR RESTORE BLOCK - Complete Registration status...");
						// GRT Receives SR Closure from Siebel Use Case
						completeRegistrationStatus(serviceRequestDto);
					}
					logger.debug("Completed.");
					sb.append("SR completed...");
					serviceRequestDto.setRoutingInfo(sb.toString());
					return serviceRequestDto;
				} catch (Exception e) {
					logger.debug("Error in SiebelService : processReceivedSiebelSR "
							+ getStackTrace(e));
					logger.error("ERROR: EJB calling failure while connecting to remote EJB with error: "
							+ e.getMessage()
							+ "\n"
							+ "Detail info: SrNumber ["
							+ serviceRequestDto.getSrNumber()
							+ "], current retry ["
							+ (i + 1)
							+ "] out of maximum retry ["
							+ numOfTotalRetry
							+ "]");
					if (i == numOfTotalRetry - 1) {
						// Connection retry expired
						e.printStackTrace();
						logger.error("ERROR while processing. \n"
								+ "Original error message: " + e.getMessage()
								+ "\n");
						throw e;
					}
				}
			}
		} finally {
			SkipDateTimeEscalator.unset();
			logger.debug("Exiting TechnicalRegistrationSRService.processReceivedSiebelSR");
		}
		sb.append("Exiting TechnicalRegistrationSRService.processReceivedSiebelSR...");
		serviceRequestDto.setRoutingInfo(sb.toString());
		return serviceRequestDto;
	}
    
    private void undoStepBProcess(ServiceRequest serviceRequestDto){
    	logger.debug("Entering TechnicalRegistrationSRService.undoStepBProcess");
    	try{
    		String srNumber = serviceRequestDto.getSrNumber();
        	logger.debug("Load the SL based on StepBSrRequest");
        	
        	SiteRegistration siteRegistration = null;
        	 
          	List<SiteList> siteLists = getTechnicalRegistrationSRDao().getSiteListByArtSR(srNumber);
        	
          	if(siteLists!=null && siteLists.size()>0) {
        		for(SiteList siteList : siteLists) {
        			logger.debug("Once laoded complete the StepB Status");
		        	logger.debug("Send mail at line item level");
	        		logger.debug("Before NOTINITIATED call");
	        		 
	        		getTechnicalRegistrationSRDao().undoStepBForSiteList(siteList, StatusEnum.NOTINITIATED);
	        		getTechnicalRegistrationSRDao().deleteExpandedSolutionElements(siteList.getId());
	        		
	        		//Defect - 228 - New Changes
	        		logger.debug("siteList Id before: " + siteList.getId());
	        		
	        		String newSiteListId = null;
	        		newSiteListId = getRegistrationId();
	        		
	        		if(newSiteListId != null){
	        			//method call to update old object with site_registration as null
		        		getTechnicalRegistrationSRDao().updateSiteListId(siteList.getId(), newSiteListId);
		        		
		        		siteList = getTechnicalRegistrationSRDao().getSiteList(newSiteListId);
		        		
		        		logger.debug("siteList Id after: " + siteList.getId());
	        		}
	        		
	        		siteRegistration =siteList.getSiteRegistration();
	        		
	        		logger.debug("siteList siteRegistration Id: " + siteList.getSiteRegistration().getRegistrationId());
	        		
        		}
	        } else {
		          	logger.debug("Load the TR based on StepBSrRequest");
		            // Implemented this method in TechnicalRegistrationSRDao 
		          	List<TechnicalRegistration> techRegList = getTechnicalRegistrationSRDao().getTechnicalRegistrationByArtSRForStepB(srNumber);
		          
		        	if(techRegList!=null && techRegList.size()>0) {
		        		for(TechnicalRegistration techncialRegistration : techRegList) {
	        				if(!techncialRegistration.getExpSolutionElements().isEmpty()){
	        					for(ExpandedSolutionElement exp: techncialRegistration.getExpSolutionElements()){
	        						exp.setSelectForAlarming(false);
	        						exp.setSelectForRemoteAccess(false);
	        						//Defect #784 : Empty Error Details When SR is cancelled
	        						exp.setRetestStatus("");
	        						exp.setArtRespCode("");
	        						exp.setArtRespMsg("");
	        					}
	        					 
	        					getTechnicalRegistrationSRDao().updateExpandedSolutionElements(techncialRegistration.getExpSolutionElements());
	        				}
	        				//Implement this method in TechnicalRegistrationSRDao 
	        				getTechnicalRegistrationSRDao().undoStepBForTechnicalRegistration(techncialRegistration, StatusEnum.NOTINITIATED);
	        				
	        				//Defect - 228 - New Changes
	        				
	        				logger.debug("techncialRegistration Id after: " + techncialRegistration.getTechnicalRegistrationId());
	        				
	        				String oldtechncialRegistrationId = null;
	        				oldtechncialRegistrationId = techncialRegistration.getTechnicalRegistrationId();
	        				
	        				String newtechncialRegistrationId = null;
	        				newtechncialRegistrationId = getRegistrationId();
	        				
	        				if(oldtechncialRegistrationId != null && newtechncialRegistrationId != null){
	        					getTechnicalRegistrationSRDao().updateTechnicalRegistrationId(oldtechncialRegistrationId, newtechncialRegistrationId);
	        					
	        					//update exp soln
	        					if(!techncialRegistration.getExpSolutionElements().isEmpty()){
	        						for(ExpandedSolutionElement exp: techncialRegistration.getExpSolutionElements()){
	        							String oldExpSolnElemntId = null;
	        							oldExpSolnElemntId = exp.getExpSolnElemntId();
	        							String newExpSolnElemntId = null;
	        							newExpSolnElemntId = getRegistrationId();
	        							if(oldExpSolnElemntId != null && newExpSolnElemntId != null){
	        								getTechnicalRegistrationSRDao().updateExpandedSolutionElementIdTechRegId(oldExpSolnElemntId, newExpSolnElemntId, newtechncialRegistrationId);
	        							}
	        						}
	        					}
	        					
	        					techncialRegistration = getTechnicalRegistrationSRDao().getTechnicalRegistration(newtechncialRegistrationId);
	        					logger.debug("techncialRegistration Id after: " + techncialRegistration.getTechnicalRegistrationId());
	        				}
	        				
	        				siteRegistration = techncialRegistration.getTechnicalOrder().getSiteRegistration();
	        				
	        				logger.debug("techncialRegistration siteRegistration Id: " + techncialRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId());
	        				
		        		}
		        	}
		        }
          	
          	if(siteRegistration != null ) {
                try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
                	 
                	getTechnicalRegistrationSRDao().refresh(siteRegistration);
                	StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
                	if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
                		SiteRegistration siteReg = getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
                	}
                	sendRegistrationRequestAlertForStepBCancellation(siteRegistration);
                } catch(Throwable throwable) {
                      logger.error("", throwable);
                }
        	}           	
          	
          	
		} catch(Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting TechnicalRegistrationSRService.undoStepBProcess");
		}
    	logger.debug("END * UNDO step B process");
    }

    private ServiceRequest updateRegistrationStatus(ServiceRequest serviceRequestDto) throws Exception {
    	StringBuffer sb = new StringBuffer();
		sb.append(serviceRequestDto.getRoutingInfo().toString());
		sb.append("Entering into TechnicalRegistrationSRService.updateRegistrationStatus...");
    	
    	String srNumber = serviceRequestDto.getSrNumber();
        logger.debug("Starting TechnicalRegistrationSRService.updateRegistrationStatus for SR Number [" + srNumber + "] ... ");

        //Grt 4.0 Additional checking of sr with subtype = "Equipment Move"
        if ( (GRTConstants.SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())) ||
        	 (GRTConstants.SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())) ||
        	 (GRTConstants.SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE.equalsIgnoreCase(serviceRequestDto.getSrSubType()))
           ){
        	//update site resitration
        	sb.append("Updating the site resitration for EQM or IB or RV...");
        	SiteRegistration siteRegistration = getTechnicalRegistrationSRDao().getSiteRegistrationBySiebelSR(srNumber,serviceRequestDto.getSrSubType());
        
        	if(siteRegistration != null) {
                	if(siteRegistration.getInstallBaseStatus() == null || StringUtils.isEmpty(siteRegistration.getInstallBaseStatus().getStatusId())) {
                    logger.error("ERROR: Process is aborted due to GRT Registration data has EMPTY status! \n"
                            + "Registration data: SR Number [" + srNumber
                            + "] and Registration ID [" + siteRegistration.getRegistrationId() + "]");
                    
                    serviceRequestDto.setRoutingInfo(sb.toString());
					return serviceRequestDto;
                }
    				String regStatusId = "";
    				if(siteRegistration.getInstallBaseSrRequest() != null && StringUtils.isNotEmpty(siteRegistration.getInstallBaseSrRequest().getSiebelSRNo()) && (srNumber.equals(siteRegistration.getInstallBaseSrRequest().getSiebelSRNo()))){
    					regStatusId = siteRegistration.getInstallBaseStatus().getStatusId();
    				}else{
    					regStatusId = siteRegistration.getFinalValidationStatus().getStatusId();
    				}
                if(StatusEnum.INPROCESS.getStatusId().equalsIgnoreCase(regStatusId)) {
                	sb.append("Updating Site Registration with SR Number [" + srNumber + "] for Inprocess status..." );
                    logger.debug("Updating Site Registration with SR Number [" + srNumber + "]");
                    ProcessStepEnum processStep = null;
                    if(GRTConstants.SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
                    	sb.append("Entering ino updateIsSrCompletedSiteRegistration for SR Sub type as IB..." );
                    	processStep = ProcessStepEnum.INSTALL_BASE_CREATION;
                    	if(siteRegistration.getIsSrCompleted() != null){
                    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, GRTConstants.NO);
                    	}
                    } else if(GRTConstants.SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
                    	sb.append("Entering ino updateIsSrCompletedSiteRegistration for SR Sub type as RV..." );
                    	processStep = ProcessStepEnum.FINAL_RECORD_VALIDATION;
                    	if(siteRegistration.getIsEQRSrCompleted() != null){
                    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, GRTConstants.NO);
                    	}
                   	} 
                    //GRT 4.0 - Additional condition of sr with subtype = "Equipment Move"
                    else if(GRTConstants.SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
                    	sb.append("Entering ino updateIsSrCompletedSiteRegistration for SR Sub type as EQM..." );
                    	processStep = ProcessStepEnum.EQUIPMENT_MOVE;
                    	if(siteRegistration.getIsEQRSrCompleted() != null){
                    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.EQUIPMENT_MOVE, GRTConstants.NO);
                    	}
                   	}
                    getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, processStep, false);
                    sendRegistrationRequestAlert(siteRegistration, processStep, null);
                    
                    serviceRequestDto.setRoutingInfo(sb.toString());
					return serviceRequestDto;
                }

        }
        }
        else if(GRTConstants.SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
        	logger.debug("Updating Technical Registration with SR Number [" + srNumber + "]");
        	sb.append("Updating Technical Registration with SR Number [" + srNumber + "]...");
        	logger.debug("Currenly, no logic for that...");
        	serviceRequestDto.setRoutingInfo(sb.toString());
			return serviceRequestDto;
        }
        else if(GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_RECORDS_BUILDING.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
        	sb.append("Entering into updateSalRegistrationStatusBySiebelSR if SR Sub type is SAL Records Building ...");
                updateSalRegistrationStatusBySiebelSR(srNumber);
                serviceRequestDto.setRoutingInfo(sb.toString());
				return serviceRequestDto;
        }
        logger.warn("No GRT Registration update is done for SR Number [" + srNumber + "] at this time!");
        sb.append("No GRT Registration update is done for SR Number [" + srNumber + "] at this time!...");
        logger.warn("Exiting from TechnicalRegistrationSRService.updateRegistrationStatus...");
        serviceRequestDto.setRoutingInfo(sb.toString());
		return serviceRequestDto;
    }

    private void completeRegistrationStatus(ServiceRequest serviceRequestDto) throws Exception {
    	StringBuffer sb = new StringBuffer();
		sb.append(serviceRequestDto.getRoutingInfo().toString());
    	String srNumber = serviceRequestDto.getSrNumber();
    	String isSRCompleted = GRTConstants.NO;
    	StatusEnum statusEnum = null;
	    logger.debug("Starting TechnicalRegistrationSRService.completeRegistrationStatus for SR Number [" + srNumber + "] for closure ... ");
	    sb.append("Starting TechnicalRegistrationSRService.completeRegistrationStatus for SR Number [" + srNumber + "] for closure ... ");
    	try {
	    	if(SRsInProcess.contains(serviceRequestDto)) {
	    		logger.warn(srNumber + " is already getting processed, Ignoring duplicate event.");
	    	    sb.append(srNumber + " is already getting processed, Ignoring duplicate event.");
	    		return;
	    	} else {
	    		SRsInProcess.add(serviceRequestDto);
	    	}

	        if(GRTConstants.SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
	        	logger.debug("Install Base Creation SR Completed----");
	        	ProcessStepEnum processStep = ProcessStepEnum.INSTALL_BASE_CREATION;
	        	 
	        	SiteRegistration siteRegistration = getTechnicalRegistrationSRDao().getSiteRegistrationBySiebelSR(srNumber,serviceRequestDto.getSrSubType());
	        	sb.append("Install base siteRegistration - "+siteRegistration);
	        	if(siteRegistration != null){
	        		if (siteRegistration.getInstallBaseStatus() == null || StringUtils.isEmpty(siteRegistration.getInstallBaseStatus().getStatusId())) {
						logger.error("ERROR: Process is aborted due to GRT Registration data has EMPTY status! \n"
										+ "Registration data: SR Number [" + srNumber + "] and Registration ID [" + siteRegistration.getRegistrationId() + "]");
						sb.append("Error due to null status");
						return;
					}
	        		// IS SR Completed flag update
	        		isSRCompleted = GRTConstants.YES;
	        		statusEnum = StatusEnum.INPROCESS;
	        		if(siteRegistration.getIsSrCompleted() != null){
	        			logger.debug("New Site Registration for Install Base Creation");
	        			if(checkAllTechnicalOrdersProcessed(siteRegistration, GRTConstants.TECH_ORDER_TYPE_IB)){
	        				statusEnum = StatusEnum.COMPLETED;
	        				sb.append("Status statusEnum : completed");
	        			}
	        		}else{
	        			sb.append("Old Site Registration for Install Base creation");
	        			logger.debug("Old Site Registration for Install Base creation");
	    				logger.debug("Registration ID [" + siteRegistration.getRegistrationId() + "] is ready to set to complete ...");
	    				statusEnum = StatusEnum.COMPLETED;
	        		}
	        		 
	        		sb.append("Updating site registration table for SR update");
	        		getTechnicalRegistrationSRDao().updateSiteRegistrationOnSRUpdate(siteRegistration.getRegistrationId(), processStep, statusEnum, isSRCompleted);
	        		sendRegistrationRequestAlert(siteRegistration, processStep, null);
	        		sb.append("Sent mail for the request");
	        		logger.debug("siteRegistration installBaseStatus value for registrationId"+siteRegistration.getRegistrationId()+" is : "
	        				+siteRegistration.getInstallBaseStatus().getStatusDescription());
	        		logger.debug("siteRegistration isSrCompleted value for registrationId"+siteRegistration.getRegistrationId()+" is : "
	        				+siteRegistration.getIsSrCompleted());
	                return;
	          }
	        } else if(GRTConstants.SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
	        	//complete install base creation--site registration
	        	logger.debug("Starting.....FinalRecordValidation.....SR Closure");
	        	 
	        	SiteRegistration siteRegistration = getTechnicalRegistrationSRDao().getSiteRegistrationBySiebelSR(srNumber,serviceRequestDto.getSrSubType());
	          
	        	if(siteRegistration != null) {
	            	if(siteRegistration.getFinalValidationStatus() == null || StringUtils.isEmpty(siteRegistration.getFinalValidationStatus().getStatusId())) {
	    					logger.error("ERROR: Process is aborted due to GRT Registration data has EMPTY status! \n"
	                            + "Registration data: SR Number [" + srNumber + "] and Registration ID [" + siteRegistration.getRegistrationId() + "]");
	                    return;
	                }
	            	ProcessStepEnum processStep = ProcessStepEnum.FINAL_RECORD_VALIDATION;
	            	// IS EQR SR Completed flag update
	        		isSRCompleted = GRTConstants.YES;
                    statusEnum = StatusEnum.INPROCESS;
					String regStatusId = "";
					if(siteRegistration.getInstallBaseSrRequest() != null 
							&& StringUtils.isNotEmpty(siteRegistration.getInstallBaseSrRequest().getSiebelSRNo()) 
							&& (srNumber.equals(siteRegistration.getInstallBaseSrRequest().getSiebelSRNo()))){
						regStatusId = siteRegistration.getInstallBaseStatus().getStatusId();
					}else{
						regStatusId = siteRegistration.getFinalValidationStatus().getStatusId();
					}
	                if(StatusEnum.INPROCESS.getStatusId().equalsIgnoreCase(regStatusId)
	                        || StatusEnum.AWAITINGINFO.getStatusId().equalsIgnoreCase(regStatusId)) {
	                    logger.debug("Updating Site Registration with SR Number [" + srNumber + "] for closure.");
	                    if(siteRegistration.getIsEQRSrCompleted() != null){
		        			logger.debug("New Site Registration for Equipment Removal");
		        			if(checkAllTechnicalOrdersProcessed(siteRegistration, GRTConstants.TECH_ORDER_TYPE_FV)){
		        				statusEnum = StatusEnum.COMPLETED;
		        			}
		        		}else{
		        			logger.debug("Old Site Registration for Equipment Removal");
		    				logger.debug("Registration ID [" + siteRegistration.getRegistrationId() + "] is ready to set to complete ...");
		    				statusEnum = StatusEnum.COMPLETED;
		        		}
	                     
	                    getTechnicalRegistrationSRDao().updateSiteRegistrationOnSRUpdate(siteRegistration.getRegistrationId(), processStep, statusEnum, isSRCompleted);
		        		sendRegistrationRequestAlert(siteRegistration, processStep, null);
		        		logger.debug("siteRegistration installBaseStatus value for registrationId"+siteRegistration.getRegistrationId()+" is : "
		        				+siteRegistration.getInstallBaseStatus().getStatusDescription());
		        		logger.debug("siteRegistration isSrCompleted value for registrationId"+siteRegistration.getRegistrationId()+" is : "
		        				+siteRegistration.getIsEQRSrCompleted());
	                    return;
	                }
	            }
	            logger.debug("Completed.....FinalRecordValidation.....SR Closure");
	        } 
	        
	        /* GRT 4.0 - Start of Equipment Move changes for completing registrations */
	        else if(GRTConstants.SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE.equalsIgnoreCase(serviceRequestDto.getSrSubType())){
	        	//complete install base creation--site registration
	        	logger.debug("Starting.....EQUIPMENTMOVE.....SR Closure");
	        	 
	        	SiteRegistration siteRegistration = getTechnicalRegistrationSRDao().getSiteRegistrationBySiebelSR(srNumber,serviceRequestDto.getSrSubType());
	          
	        	if(siteRegistration != null) {
	            	if(siteRegistration.getFinalValidationStatus() == null || StringUtils.isEmpty(siteRegistration.getFinalValidationStatus().getStatusId())) {
	    					logger.error("ERROR: Process is aborted due to GRT Registration data has EMPTY status! \n"
	                            + "Registration data: SR Number [" + srNumber + "] and Registration ID [" + siteRegistration.getRegistrationId() + "]");
	                    return;
	                }
	            	ProcessStepEnum processStep = ProcessStepEnum.EQUIPMENT_MOVE;
	            	// IS EQR SR Completed flag update
	        		isSRCompleted = GRTConstants.YES;
                    statusEnum = StatusEnum.INPROCESS;
					String regStatusId = "";
					if(siteRegistration.getEqrMoveSrRequest() != null 
							&& StringUtils.isNotEmpty(siteRegistration.getEqrMoveSrRequest().getSiebelSRNo()) 
							&& (srNumber.equals(siteRegistration.getEqrMoveSrRequest().getSiebelSRNo()))){
						regStatusId = siteRegistration.getEqrMoveStatus().getStatusId();
					}
	                if(StatusEnum.INPROCESS.getStatusId().equalsIgnoreCase(regStatusId)
	                        || StatusEnum.AWAITINGINFO.getStatusId().equalsIgnoreCase(regStatusId)) {
	                    logger.debug("Updating Site Registration with SR Number [" + srNumber + "] for closure.");
	                    if(siteRegistration.getIsEQMSrCompleted() != null){
		        			logger.debug("New Site Registration for Equipment Removal");
		        			if(checkAllTechnicalOrdersProcessed(siteRegistration, GRTConstants.TECH_ORDER_TYPE_EM)){
		        				statusEnum = StatusEnum.COMPLETED;
		        			}
		        		}else{
		        			logger.debug("Old Site Registration for Equipment Removal");
		    				logger.debug("Registration ID [" + siteRegistration.getRegistrationId() + "] is ready to set to complete ...");
		    				statusEnum = StatusEnum.COMPLETED;
		        		}
	                     
	                    getTechnicalRegistrationSRDao().updateSiteRegistrationOnSRUpdate(siteRegistration.getRegistrationId(), processStep, statusEnum, isSRCompleted);
		        		sendRegistrationRequestAlert(siteRegistration, processStep, null);
		        		logger.debug("siteRegistration equipment move status value for registrationId"+siteRegistration.getRegistrationId()+" is : "
		        				+siteRegistration.getEqrMoveStatus().getStatusDescription());
		        		logger.debug("siteRegistration equipment move isSrCompleted value for registrationId"+siteRegistration.getRegistrationId()+" is : "
		        				+siteRegistration.getIsEQMSrCompleted());
	                    return;
	                }
	            }
	            logger.debug("Completed.....EQUIPMENTMOVE.....SR Closure");
	        }	        
	        /* GRT 4.0 - End of Equipment Move changes for completing registrations */
	        
	        else if(GRTConstants.SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION.equalsIgnoreCase(serviceRequestDto.getSrSubType())) {
	        	logger.debug("Sieble Updating TechnicalRegistration Registration with SR Number [" + srNumber + "] for closure.");
	        	 
	        	List<TechnicalRegistration> techncialRegistrations = getTechnicalRegistrationSRDao().getTechnicalRegistrationByArtSR(srNumber);
	        	
	        	SiteRegistration siteRegistration = null;
	        		if(techncialRegistrations != null && techncialRegistrations.size() > 0) {
	        			for (TechnicalRegistration technicalRegistration : techncialRegistrations) {
	        				if(technicalRegistration!=null&&(technicalRegistration.getStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())|| technicalRegistration.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId()))) {
	                			logger.debug("TechnicalRegistration Registration ID:" + technicalRegistration.getTechnicalRegistrationId() + " for SR Number [" + srNumber + "] closure.");
	                			 
	                			getTechnicalRegistrationSRDao().updateTechnicalRegistrationStatus(technicalRegistration, StatusEnum.COMPLETED);
	                			
	                			//GRT 4.0 Changes
	                			if(technicalRegistration.getStepBStatus()!=null && !StatusEnum.NOTINITIATED.equals(technicalRegistration.getStepBStatus()))
	                			{
	                				getBaseHibernateDao().updateTechnicalRegistration(technicalRegistration.getTechnicalRegistrationId());
	                			}
	                			
	                			if(siteRegistration == null) {
	                				siteRegistration =technicalRegistration.getTechnicalOrder().getSiteRegistration();
	                			}
			    				
			    				if(technicalRegistration.getNumberOfSubmitAsInt() > 2) {
			    					sendEmailNoticeForTOBLine(technicalRegistration, null, siteRegistration, StatusEnum.COMPLETED);
			    				}
	                		}
						}
	        			if(siteRegistration != null) {
	    	    			try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
	    	    				 
	    	    				getTechnicalRegistrationSRDao().refresh(siteRegistration);
	    	    				StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
	    		    			if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
	    		    				getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
	    		    				sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, newStatus);
	    		    			}
	    	    			} catch(Throwable throwable) {
	    	    				logger.error("", throwable);
	    	    			}
	    	    		}
	        		} else {
	        			logger.debug("No Technical Registration record found for SR Number [" + srNumber + "]");
	        		}
	        } else if (GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_RECORDS_BUILDING.equalsIgnoreCase(serviceRequestDto.getSrSubType())) {
	        	logger.debug("Sieble Updating Site List with SR Number [" + srNumber + "] for closure.");
	        	 
	        	List<SiteList> siteLists = getTechnicalRegistrationSRDao().getSiteListsByArtSr(srNumber);
	        	
	        	SiteRegistration siteRegistration = null;
        		if(siteLists != null && siteLists.size() > 0) {
        			for (SiteList siteList : siteLists) {
        				if(siteList != null &&(siteList.getStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId()) || siteList.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId()))) {
                			logger.debug("Site List ID:" + siteList.getId() + " for SR Number [" + srNumber + "] closure.");
                			Status status = new Status();
                			status.setStatusId(StatusEnum.COMPLETED.getStatusId());
                			siteList.setStatus(status);
                			getTechnicalRegistrationDao().saveSalSiteList(siteList);
                			if(siteRegistration == null) {
                				siteRegistration =siteList.getSiteRegistration();
                			}
                			
		    				if(siteList.getNumberOfSubmitAsInt() > 2) {
		    					sendEmailNoticeForTOBLine(null, siteList, siteRegistration, StatusEnum.COMPLETED);
		    				}
                		}
					}
        			if(siteRegistration != null) {
    	    			try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
    	    				 
    	    				getTechnicalRegistrationSRDao().refresh(siteRegistration);
    	    				StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
    		    			if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
    		    				getTechnicalRegistrationSRDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
    		    				sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, newStatus);
    		    			}
    	    			} catch(Throwable throwable) {
    	    				logger.error("", throwable);
    	    			}
    	    		}
        		} else {
        			logger.debug("No Site List record found for SR Number [" + srNumber + "]");
        			logger.debug("Sieble Updating TechnicalRegistration with SR Number [" + srNumber + "] for closure.");
        			 
        			List<TechnicalRegistration> techncialRegistrations = getTechnicalRegistrationSRDao().getTechnicalRegistrationByArtSR(srNumber);
        			
	        		if(techncialRegistrations != null && techncialRegistrations.size() > 0) {
	        			for (TechnicalRegistration technicalRegistration : techncialRegistrations) {
	        				if(technicalRegistration!=null&&(technicalRegistration.getStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())|| technicalRegistration.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId()))) {
	                			logger.debug("TechnicalRegistration Registration ID:" + technicalRegistration.getTechnicalRegistrationId() + " for SR Number [" + srNumber + "] closure.");
	                			 //
	                			getTechnicalRegistrationSRDao().updateTechnicalRegistrationStatus(technicalRegistration, StatusEnum.COMPLETED);
	                			if(siteRegistration == null) {
	                				siteRegistration =technicalRegistration.getTechnicalOrder().getSiteRegistration();
	                			}
			    				
			    				if(technicalRegistration.getNumberOfSubmitAsInt() > 2) {
			    					sendEmailNoticeForTOBLine(technicalRegistration, null, siteRegistration, StatusEnum.COMPLETED);
			    				}
	                		}
						}
	        			if(siteRegistration != null) {
	    	    			try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
	    	    				 
	    	    				getTechnicalRegistrationSRDao().refresh(siteRegistration);
	    	    				StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
	    		    			if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
	    		    				getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
	    		    				sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, newStatus);
	    		    			}
	    	    			} catch(Throwable throwable) {
	    	    				logger.error("", throwable);
	    	    			}
	    	    		}
	        		} else {
	        			logger.debug("No Technical Registration record found for SR Number [" + srNumber + "]");
	        		}
        		}
	        } else if (GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_CONNECTIVITY_AND_ALARM_TESTING.equalsIgnoreCase(serviceRequestDto.getSrSubType())) {
	        	//complete the Alarm
	        	logger.debug("Load the SL based on StepBSrRequest");
	        	 
	          	List<SiteList> siteLists = getTechnicalRegistrationSRDao().getSiteListByArtSR(srNumber);
	        	
	          	SiteRegistration siteRegistration = null;
	          	if(siteLists!=null && siteLists.size()>0) {
	        		for(SiteList siteList : siteLists) {
	        			logger.debug("Once laoded complete the StepB Status");
			        	logger.debug("Send mail at line item level");
			        	if(GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())){
			        		logger.debug("Before NOTINITIATED call");
			        		siteList.setSelectForAlarming(false);
			        		siteList.setSelectForRemoteAccess(false);
			        		siteList.setCancelled(GRTConstants.TRUE);
			        		//Defect #784 : Empty Error Details When SR is cancelled
			        		siteList.setErrorCode("");
			        		siteList.setTroubleShootURL("");
			        		 
			        		getTechnicalRegistrationSRDao().updateSiteListStepBStatus(siteList, StatusEnum.NOTINITIATED);
			        		getTechnicalRegistrationSRDao().deleteExpandedSolutionElements(siteList.getId());
			        		siteRegistration = siteList.getSiteRegistration();
			        	} else {
			        		siteList.setStepBCompletedDate(new Date());
			        		
			        		//Defect #784 : Empty Error Details When SR is completed
	        				if(!siteList.getExpSolutionElements().isEmpty()){
	        					for(ExpandedSolutionElement exp: siteList.getExpSolutionElements()){
	        						exp.setRetestStatus("");
	        						exp.setArtRespCode("");
	        						exp.setArtRespMsg("");
	        					}
	        				}
			        		getTechnicalRegistrationSRDao().updateSiteListStepBStatus(siteList, StatusEnum.COMPLETED);
			        	}
			        	//TODO:Send mail at line item level
			        	logger.debug("If header status also needs to be completed. - SiteRegistration.computeTRHeaderStatus");
			        	siteRegistration =siteList.getSiteRegistration();
	        		}
	        		if(siteRegistration != null && !GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())) {
		        		try {
		        			//Reload the SiteRegistration from DB, By-passing 1st level cache.
		        			 
		        			getTechnicalRegistrationSRDao().refresh(siteRegistration);
	                    	StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
	                    	if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
	                    		SiteRegistration siteReg =   getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
	                    	}
							sendRegistrationRequestAlertForStepBCompletion(siteRegistration, null, siteLists, srNumber);
		        		} catch(Throwable throwable) {
	                      logger.error("", throwable);
		        		}
		        	} else if(siteRegistration != null && GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())) {
	    				sendRegistrationRequestAlertForStepBCancellation(siteRegistration);
		        	}
		        } else {
			          	logger.debug("Load the TR based on StepBSrRequest");
			             
			        	List<TechnicalRegistration> techRegList = getTechnicalRegistrationSRDao().getTechnicalRegistrationByArtSRForStepB(srNumber);
			        
			        	if(techRegList!=null && techRegList.size()>0) {
			        		for(TechnicalRegistration techncialRegistration : techRegList) {
			        			if(GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())){
			        				techncialRegistration.setSelectForAlarming(false);
			        				techncialRegistration.setSelectForRemoteAccess(false);
			        				techncialRegistration.setCancelled(GRTConstants.TRUE);
			        				//Defect #784 : Empty Error Details When SR is cancelled
			        				techncialRegistration.setErrorCode("");
			        				techncialRegistration.setTroubleShootURL("");
			        				if(!techncialRegistration.getExpSolutionElements().isEmpty()){
			        					for(ExpandedSolutionElement exp: techncialRegistration.getExpSolutionElements()){
			        						exp.setSelectForAlarming(false);
			        						exp.setSelectForRemoteAccess(false);
			        						//Defect #784 : Empty Error Details When SR is cancelled
			        						exp.setRetestStatus("");
			        						exp.setArtRespCode("");
			        						exp.setArtRespMsg("");
			        					}
			        				}
			        				 
			        				getTechnicalRegistrationSRDao().updateTechnicalRegistrationStepBStatusCancelled(techncialRegistration, StatusEnum.CANCELLED);
			        				siteRegistration =techncialRegistration.getTechnicalOrder().getSiteRegistration();
			        				
			        			try{
			           				boolean flag=true;
				       				List<TechnicalRegistration> TechRegReTestlist=getBaseHibernateDao().getTechnicalRetestRegistrationListByregistrationId(siteRegistration.getRegistrationId(), "TR_RETEST");
				       				for(TechnicalRegistration registration: TechRegReTestlist)
				       				{
				       					if(registration.getStepBStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId()))
				       					{
				       						flag=true;
				       					}else{
				       						flag=false;
				       						break;
				        				}
				       				}
				       				List<TechnicalRegistration> TechReglist=getBaseHibernateDao().getTechnicalRetestRegistrationListByregistrationId(siteRegistration.getRegistrationId(), "TR");
				       				for(TechnicalRegistration registration:TechReglist)
				       				{
				       					if(registration.getStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId()) && registration.getStepBStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())){
				   						flag=true;
				       					}else{
				    						flag=false;
				       						break;
				        				}
				       				}
					        				
					        		if(flag){
					        			getBaseHibernateDao().updateSiteRegistrationCompleteStatus(siteRegistration.getRegistrationId(), StatusEnum.COMPLETED);
					        		}
			        				
				        			} catch (HibernateException hibEx) {
				        				logger.debug("Error update site registration staus completed: "+hibEx.getMessage());
				    		            throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
				    		        }
			        				
			        				
			        			} else {
			        				techncialRegistration.setStepBCompletedDate(new Date());
			        				 
			        				//Defect #784 : Empty Error Details When SR is completed
			        				if(!techncialRegistration.getExpSolutionElements().isEmpty()){
			        					for(ExpandedSolutionElement exp: techncialRegistration.getExpSolutionElements()){
			        						exp.setRetestStatus("");
			        						exp.setArtRespCode("");
			        						exp.setArtRespMsg("");
			        					}
			        				}
			        				
			        				getTechnicalRegistrationSRDao().updateTechnicalRegistrationStepBStatus(techncialRegistration, StatusEnum.COMPLETED);
								}
					        	//TODO:Send mail at line item level
					        	logger.debug("Send mail at line item level");
					        	//If CMProduct initiate polling
					        	if(techncialRegistration.isCmProduct() && (techncialRegistration.isCmMain() ||  (!techncialRegistration.isCmMain() && techncialRegistration.getRemoteDeviceType().equalsIgnoreCase(GRTConstants.REMOTE_DEVICE_TYPE_LSP)))) {
									logger.debug("Initiating CM Polling for TR Id:" + techncialRegistration.getTechnicalRegistrationId());
					        	logger.debug("If CMProduct initiate polling");
								try {
							        	List<TechnicalRegistration> techList = new ArrayList<TechnicalRegistration>();
							        	techList.add(techncialRegistration);
							        	initiateCMPolling(techList);
								   } catch(Throwable throwable) {
									//TODO: Design GAP:
									logger.error("", throwable);
								   }
					        	}
					        	siteRegistration =techncialRegistration.getTechnicalOrder().getSiteRegistration();
			        		}


			        		if(siteRegistration != null && !GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())) {
				                try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
				                	 
				                	getTechnicalRegistrationSRDao().refresh(siteRegistration);
				                	StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
				                	if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
				                		SiteRegistration siteReg =    getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
				                	}
				                	sendRegistrationRequestAlertForStepBCompletion(siteRegistration, techRegList, null, srNumber);
				                } catch(Throwable throwable) {
				                      logger.error("", throwable);
				                }
				        	} else if(siteRegistration != null && GRTConstants.SIEBEL_SR_STATUS_CANCELLED.equalsIgnoreCase(serviceRequestDto.getStatus())) {
		        				sendRegistrationRequestAlertForStepBCancellation(siteRegistration);
				        	}
			        	}
			    		return;
			        }
	        }
    	} finally {
    		SRsInProcess.remove(serviceRequestDto);
    	}
        logger.warn("No GRT Registration update is done for SR Number [" + srNumber + "] at this time!");
    }
    
    
    /**
	 * Method to print the StackTrace to String.
	 * 
	 * @param aThrowable
	 *            Throwable
	 * @return result String
	 */
	private String getStackTrace(Exception exception) {
		try {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			exception.printStackTrace(printWriter);
			return result.toString();
		} catch (Exception ex) {
			return "Bad stack trace...";
		}
	}
	
	private void updateSalRegistrationStatusBySiebelSR(String srNumber) {
    	logger.debug("Starting.... TechnicalRegistrationSRService.updateSalRegistrationStatusBySiebelSR ");
    	 
    	
    	try {
			SiteList siteList = null;			
			if ((siteList = getTechnicalRegistrationSRDao().getSiteListByArtSr(srNumber)) != null) {
			if (siteList != null) { 
				logger.debug("Found Sr Number in Sal UI List:");
				updateSalMigrationList(siteList);
			} else {
				logger.debug("Sr Number is not found in Sal Registration");
			}
		}
    	}catch (DataAccessException dae) {
			logger.error("Error! while retrieving Sal Details", dae);
		}
		
    	logger.debug("Starting.... updateSalRegistrationStatusBySiebelSR ");
    }
	
    private void updateSalMigrationList(SiteList siteList) {
    	logger.debug("Starting....updateSalMigrationList");
    	try {
    		Status s = new Status();
    		if (StatusEnum.INPROCESS.getStatusId().equalsIgnoreCase(siteList.getStatus().getStatusId())) {
    			s.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
        		siteList.setStatus(s);
        		getTechnicalRegistrationDao().saveSalSiteList(siteList);
    		} else if(StatusEnum.AWAITINGINFO.getStatusId().equalsIgnoreCase(siteList.getStatus().getStatusId())) {
    			s.setStatusId(StatusEnum.COMPLETED.getStatusId());
        		siteList.setStatus(s);
        		if (StringUtils.isNotEmpty(siteList.getArtSrNo())) siteList.setArtSrNo(new String("  "));
        		if (StringUtils.isNotEmpty(siteList.getErrorDesc())) siteList.setErrorDesc(new String("   "));
        		if (StringUtils.isNotEmpty(siteList.getErrorCode())) siteList.setErrorCode(new String("   "));
        		getTechnicalRegistrationDao().saveSalSiteList(siteList);
        		try {
        	    	SiteRegistration siteRegistration = siteList.getSiteRegistration();
        	    	if (siteRegistration != null && !siteRegistration.isAnyTechRegAwaiting()){
        	    		getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
        	    	}
        	    } catch (Exception e){
        	    		logger.error("Error! while updating updateSalMigrationList Status",e);
        	    }
    		}
    	}catch(Exception e) {
    		logger.error("Error! while updating updateSalMigrationList Status",e);
    	}
    	logger.debug("Completed....TechnicalRegistrationSRService.updateSalMigrationList");
    }
    
    private Boolean checkAllTechnicalOrdersProcessed(SiteRegistration siteRegistration, String orderType) throws DataAccessException {
    	logger.debug("Starting...TechnicalRegistrationSRService.checkAllInstallBaseRegStatusCompleted ");
    	siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(siteRegistration.getRegistrationId());
    	Set<TechnicalOrder> tos = siteRegistration.getTechnicalOrders();
    	if(tos == null || tos.isEmpty()) {
    		return true;
    	}
    	Iterator<TechnicalOrder> ite = tos.iterator();
    	while(ite.hasNext()) {
    		TechnicalOrder technicalOrder = ite.next();
    		if(technicalOrder.getOrderType() != null) {
	    		if((technicalOrder.getOpenQuantity() != null && technicalOrder.getOpenQuantity()==0
	    				&& technicalOrder.getOrderType().equalsIgnoreCase(orderType))
	    				|| !technicalOrder.getOrderType().equalsIgnoreCase(orderType)) {
	    			logger.debug("continuing for techOrder Id:" + technicalOrder.getOrderId());
	    			continue;
	    		} else if(technicalOrder.getSr_Created() != null && GRTConstants.YES.equalsIgnoreCase(technicalOrder.getSr_Created())){
	    			logger.debug("continuing for techOrder - IB/FV if SR Created, Id:" + technicalOrder.getOrderId());
	    			continue;
	    		} else if((GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType) || GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType)) && GRTConstants.NO.equalsIgnoreCase(technicalOrder.getDeleted())){
	    			logger.debug("continuing for techOrder - FV not selected for Removal, Id:" + technicalOrder.getOrderId());
	    			continue;
	    		} else {
	    			logger.debug("returning false due to techOrder Id:" + technicalOrder.getOrderId());
	    		}
    		}
    		return false;
    	}
    	logger.debug("Completed...TechnicalRegistrationSRService.checkAllTechnicalOrdersProcessed ");
    	return true;
    }

	public TechnicalRegistrationSRDao getTechnicalRegistrationSRDao() {
		return technicalRegistrationSRDao;
	}

	public void setTechnicalRegistrationSRDao(
			TechnicalRegistrationSRDao technicalRegistrationSRDao) {
		this.technicalRegistrationSRDao = technicalRegistrationSRDao;
	}

	
	
}
