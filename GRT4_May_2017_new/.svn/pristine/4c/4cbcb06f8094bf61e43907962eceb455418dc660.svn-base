package com.avaya.grt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationAsyncDao;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegResponse;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.ExpSolutionElemDto;
import com.grt.dto.TechRegDataResponseDto;
import com.grt.util.AccessTypeEnum;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.SkipDateTimeEscalator;
import com.grt.util.StatusEnum;
import com.grt.util.TechRecordEnum;



public class TechnicalRegistrationAlarmAsyncService extends TechnicalRegistrationAsyncService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationAlarmAsyncService.class);
	
	/*
     * [AVAYA] GRT 4.0  Process Technical Registration response for Step B automation
     *
     * 
     *
     */
	public TechRegResponse processReceivedAlarmTechReg(List<TechRegDataResponseDto> techRegResponseDtos) throws DataAccessException {
		logger.debug("Entering TechnicalRegistrationAlarmAsyncService.processReceivedAlarmTechReg");
		TechRegResponse responseResultType = new TechRegResponse();
		StringBuffer sb = new StringBuffer();
		sb.append("Entering TechnicalRegistrationAsyncService.processReceivedAlarmTechReg to process the TechReg request recieved... ");
    	if(techRegResponseDtos != null && techRegResponseDtos.size() >0){
    		sb.append("Start the processing when Data object is not null in the recieved ASYNC request Payload... ");
    		SiteRegistration siteRegistration = null;
    		List<TechnicalRegistration> failedTRs = new ArrayList<TechnicalRegistration>();
    		List<TechnicalRegistration> failedTRSALs = new ArrayList<TechnicalRegistration>();
    		List<SiteList> failedSLs = new ArrayList<SiteList>();
    		boolean createSR = false;
    		boolean sendAwaitingNotification = false;
    		boolean skipNotification = true;
    		
    		//Parse through all records received from ART 
    		techRegResponseDtos = arrangeParent(techRegResponseDtos);
    		sb.append("Parse through all records received from ART... ");
    		
    		//Parse through all records received from ART 
    		for (TechRegDataResponseDto techRegResponseDto : techRegResponseDtos ) {
		    	try {
		    		sb.append("Inside for loop processing data objects... ");
		    		//Processing each line item 
			    	logger.debug("Processing Record for RegistrationId:" + techRegResponseDto.getRegistrationId() + " GRT Transaction/Registration ID:" + techRegResponseDto.getTransactionId());
			    	SkipDateTimeEscalator.set(Boolean.TRUE);

			    	//Tech_Reg_Id and Transaction_id cannot be null or empty
		    		if (StringUtils.isNotEmpty(techRegResponseDto.getRegistrationId()) && StringUtils.isNotEmpty(techRegResponseDto.getTransactionId())) {
		    			sb.append("Inside If - Tech_Reg_Id and Transaction_id cannot be null or empty... ");
		    			try {
		    				
	    						//Fetch the GRT Technical Registration record based on tech_reg_id received from ART
	    						TechnicalRegistration techRegistration = getTechnicalRegistrationAsyncDao().getTechnicalRegistration(techRegResponseDto.getTransactionId());
	    						sb.append("Fetch the GRT Technical Registration record based on tech_reg_id received from ART... ");
	    						try {
	    							if (techRegistration != null) {
	    								
	    								//Fetch the parent GRT site registration id from the technical order record belonging to technical registration record
	    								
	    								sb.append("Fetch the parent GRT site registration id from the technical order record belonging to technical registration record - iF TECHNICAL rEG IS NOT NULL... ");
			    						if(siteRegistration == null) {
			    							siteRegistration = techRegistration.getTechnicalOrder().getSiteRegistration();
			    						}
			    						
			    						//If the status is awaitinginfo or completed, these records are already processed so skip them
			    						sb.append("If the status is awaitinginfo or completed, these records are already processed so skip them... ");
	    								if ( ((StatusEnum.AWAITINGINFO.getStatusId().equals(techRegistration.getStepBStatus().getStatusId())) ||
	    								     (StatusEnum.COMPLETED.getStatusId().equals(techRegistration.getStepBStatus().getStatusId()))) &&
	    								     !techRegistration.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)
	    								   ) {
	    									continue;
	    								}
	    								//For SAL don't process for NO Connectivity Records and if step b status is other than in-process
	    								else if(  techRegistration.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)){
	    										 if( GRTConstants.NO.equalsIgnoreCase(techRegistration.getConnectivity()) || 
	    												 	(techRegistration.getStepBStatus() != null && !techRegistration.getStepBStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId()))
	    										  ){
	    											 continue;
	    										 }
	    								} 								       
	    									    									
	    								//Check for sending notification
	    								skipNotification = false;	
	    								sb.append("Check If Notification is required or not... ");
	    								
			    						//Send the notification if number of submits = 1 and status = awaitinginfo
			    						if(techRegistration.getNumberOfSubmitAsInt() == 1 && siteRegistration.getTechRegStatus().getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId())) {
			    							sendAwaitingNotification = true;
			    							sb.append("Send the notification if number of submits = 1 and status = awaitinginfo... ");
			    						}
			    						
			    						//Compare/Update TechnicalRegistration record in GRT DB with the ART element received along with the request 
			    						TechnicalRegistration failedTR = handleTRAlarmResponse(techRegResponseDto, techRegistration);
			    						sb.append("Compare/Update TechnicalRegistration record in GRT DB with the ART element received along with the request... ");
			    						
			    						//Failed line item received from Art
			    						if(failedTR != null) {
			    							sb.append("Create SR if there is any failed TR for failed line item... ");
			    							//Create SR for failed line item
			    							if(failedTR.getNumberOfSubmitAsInt() >= 1 && failedTR.getStepBSRRequest() == null) {
			    								createSR = true;
			    							}
			    							
			    							//Add the failed TR's in separate lists as per access_types
			    							if(AccessTypeEnum.SAL.getDbAccessType().equalsIgnoreCase(failedTR.getAccessType())) {
			    								sb.append("Adding the failed TR's in separate lists if access type is SAL... ");
				    							failedTRSALs.add(failedTR);
				    						} else {
				    							sb.append("Adding the failed TR's in separate lists if access type is apart from SAL... ");
				    							failedTRs.add(failedTR);
				    						}
			    						}
	    							} else {
	    								sb.append("Record is not found in technical_registration, searching for the record in site_list... ");
	    								//If record is not found in technical_registration, search for the record in site_list
	    								//This is for SAL Migration Registrations - perform the same checks as for technical_registration
										SiteList sl = getTechnicalRegistrationAsyncDao().getSiteList(techRegResponseDto.getTransactionId());
										
										if(sl != null) {
											sb.append("Record found in site_list - Fetching the Site Reg Id from site_list record... ");
											//Fetch the parent GRT site registration id from site_list record
											if(siteRegistration == null) {
												
				    							siteRegistration = sl.getSiteRegistration();
				    						}
											
											//If the status is awaitinginfo or completed, these records are already processed so skip them
											sb.append("the status is awaitinginfo or completed, these records are already processed so skiping them... ");
											if ( (StatusEnum.AWAITINGINFO.getStatusId().equals(sl.getStepBStatus().getStatusId())) ||
		    									 (StatusEnum.COMPLETED.getStatusId().equals(sl.getStepBStatus().getStatusId()))
		    								   ) {
		    									continue;
		    								}
											
											//Check for sending notification
		    								skipNotification = false;
											
		    							
											//Send the notification if number of submits = 1 and status = awaitinginfo
											if(sl.getNumberOfSubmitAsInt() == 1 && siteRegistration.getTechRegStatus().getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId())) {
				    							sendAwaitingNotification = true;
				    							sb.append("Send the notification if number of submits = 1 and status = awaitinginfo... ");
				    						}
											
											//Compare/Update SiteList record in GRT DB with the ART element received along with the request
											SiteList failedSL = handleSLAlarmResponse(techRegResponseDto, sl);
											sb.append("Compare/Update SiteList record in GRT DB with the ART element received along with the request... ");
											
											//Failed line item received from Art
											if(failedSL != null) {
												//Create SR for failed line item
												sb.append("Create SR if there is any failed SL for failed line item... ");
												if(failedSL.getNumberOfSubmitAsInt() >= 1 && failedSL.getStepBSRRequest() == null) {
				    								createSR = true;
				    							}
												
												//All access_types in site_list would be "SAL" access type so simply add it in the list
												sb.append("Adding the failed SL's in separate lists for all access type is SAL... ");
												failedSLs.add(failedSL);
											}
										} else {
											sb.append("Site list is coming null... ");
											logger.warn("Record with ID:" + techRegResponseDto.getTransactionId() + " NOT found.");
										}
	    							}
								} catch (Exception e) {
									sb.append("Error!! while handling ART Asynch Response... ");
									logger.error("Error!! while handling ART Asynch Response", e);
								}
								
	    						
			    			} catch (DataAccessException dae) {
			    				logger.error("Error: while accessing the technical registration",dae);
			    			}
			    		} else {
			    			sb.append("Getting Null or blank grtId...");
			    			logger.error("Got Null/blank grtId");
			    		}
			    	} finally {
			    		SkipDateTimeEscalator.unset();
			    		logger.debug("Processed record for RegistrationId:" + techRegResponseDto.getRegistrationId() + " Type:" + techRegResponseDto.getTechRegDetail() + " Line ID:" + techRegResponseDto.getTransactionId());
			    	}
    			}//Processing for each element received from ART completed
    		
    		
    			try {
    				//For the failed TR's create SR and populate the stepB_SR_Request
    				sb.append("For the failed TR's creating SR and populate the stepB_SR_Request...");
    				if (siteRegistration != null) {
	    				if ( (failedTRs != null) || (failedTRSALs != null) || (failedSLs != null) ) {
	    					//List for IP, Modem access type
	    					if (failedTRs != null && failedTRs.size() > 0) {
		    					SRRequest srRequest = null;
		    					//Create SR as it was getting created for step A
		    					if (createSR){
		    						srRequest = this.createSR(siteRegistration, failedTRs, null, TechRecordEnum.ALARM);
		    						sb.append("SR created for the Alarm Testing...");
		    					}
		    					
		    					//Create SR as it was getting created for step A and save the SR in Technical_Registration table
		    					sb.append("Create SR as it was getting created for step A and save the SR in Technical_Registration table...");
		    					for (TechnicalRegistration tr : failedTRs) {
		    						if (srRequest != null) {
										//tr.setSrRequest(srRequest);
										tr.setStepBSRRequest(srRequest);
										//tr.setArtSrNo(srRequest.getSiebelSRNo());
									}
		    						getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(tr);
									if (tr.getNumberOfSubmitAsInt() >= 1) {
										sendEmailNoticeForTOBLine(tr, null, siteRegistration, StatusEnum.AWAITINGINFO);
										sb.append("Sending Email notification after creating SR as Awaiting Info to the TOB line for failed TRs...");
									}
								}
	    					}
	    					
	    					//List for SAL access type
	    					if (failedTRSALs != null && failedTRSALs.size() > 0) {
		    					SRRequest srRequest = null;
		    					//Create SR as it was getting created for step A
		    					if(createSR) {
		    						srRequest = this.createSR(siteRegistration, failedTRSALs, null, TechRecordEnum.ALARM);
		    						sb.append("Creating SR request for SAL access type...");
		    					}
		    					
		    					//Create SR as it was getting created for step A and save the SR in Technical_Registration table
		    					for (TechnicalRegistration tr : failedTRSALs) {
		    						if(srRequest != null) {
										//tr.setSrRequest(srRequest);
										tr.setStepBSRRequest(srRequest);
										//tr.setArtSrNo(srRequest.getSiebelSRNo());
									}
		    						getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(tr);
									if(tr.getNumberOfSubmitAsInt() >= 1) {
										sendEmailNoticeForTOBLine(tr, null, siteRegistration, StatusEnum.AWAITINGINFO);
										sb.append("Sending Email notification after creating SR as Awaiting Info to the TOB line for failed TRSALs...");
									}
								}
	    					}
	    					
	    					//List for SAL migration type
	    					if(failedSLs != null && failedSLs.size() > 0) {
		    					SRRequest srRequest = null;
		    					//Create SR as it was getting created for step A
		    					if(createSR) {
		    						srRequest = this.createSR(siteRegistration, null, failedSLs, TechRecordEnum.ALARM);
		    					}
		    					
		    					//Create SR as it was getting created for step A and save the SR in Site_List table
		    					for (SiteList sl : failedSLs) {
		    						if(srRequest != null) {
										//sl.setSrRequest(srRequest);
										sl.setStepBSRRequest(srRequest);
										//sl.setArtSrNo(srRequest.getSiebelSRNo());
									}
		    						getTechnicalRegistrationAsyncDao().saveSalSiteList(sl);

									if(sl.getNumberOfSubmitAsInt() >= 1) {
										sendEmailNoticeForTOBLine(null, sl, siteRegistration, StatusEnum.AWAITINGINFO);
										sb.append("Sending Email notification after creating SR as Awaiting Info to the TOB line for failed SLs...");
									}
								}
		    				}
	    					
	    				}  
    				}
    			} catch (Throwable throwable) {
    				logger.error("Error while creating SRs for failed transactions", throwable);
    			}
    			
    			if(siteRegistration != null && !skipNotification) {
	    			try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
	    				sb.append("Reloading the SiteRegistration from DB and preparing to sending the notification if its false... ");
	    				getTechnicalRegistrationAsyncDao().refresh(siteRegistration);
	    				
	    				//Set the current status of site_registration on the header level based on details from all line items
	    				StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
	    				sb.append("Got the new status for the TR Header from Site Registeration... ");
	    				logger.debug("Existing TechRegStatusId:" + siteRegistration.getTechRegStatus().getStatusId());
	    				logger.debug("New TechRegStatusId:" + ((newStatus!=null)?newStatus.getStatusId():""));
	    				logger.debug("sendAwaitingNotification:" + sendAwaitingNotification);
	    				
	    				//Update the header site_registration status and send mail to user
		    			if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
		    				getTechnicalRegistrationAsyncDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
		    				sb.append("Updating the SiteRegistration with new header status and Technical On-Boarding Completed date as current date...");
		    				this.sendRegistrationRequestAlertForAlarming(siteRegistration.getRegistrationId(), ProcessStepEnum.TECHNICAL_REGISTRATION, newStatus);
		    			} else if(sendAwaitingNotification) {
		    				sb.append("Sending the notification for the request alert where status as Awaiting Info for Technical Onboarding... ");
		    				this.sendRegistrationRequestAlertForAlarming(siteRegistration.getRegistrationId(), ProcessStepEnum.TECHNICAL_REGISTRATION, StatusEnum.AWAITINGINFO);
		    			}
	    			} catch(Throwable throwable) {
	    				logger.error("", throwable);
	    			}
	    		}
    		}
	    	logger.debug("Exiting TechnicalRegistrationAlarmAsyncService.processReceivedAlarmTechReg");
	    	sb.append("Exiting TechnicalRegistrationAlarmAsyncService.processReceivedAlarmTechReg...");
	    	responseResultType.setRoutingInfo(sb.toString());
	    	return responseResultType;
	}
	
	
	private SiteList handleSLAlarmResponse(TechRegDataResponseDto dto, SiteList sl) throws DataAccessException {
    	logger.debug("Entering TechnicalRegistrationAlarmAsyncService.handleSLResponse for SiteList Id:" + sl.getId());
    	SiteList failedSL = null;
    	try{
	    	if(dto != null && sl != null) {
	    		
	    		//Defect#635 - Set the Child elements in the sitelist from the dto with the update response
	    		if( dto.getExpSolElemList()!=null && !dto.getExpSolElemList().isEmpty() ){
	    			Set<ExpandedSolutionElement> explodedSEs = sl.getExplodedSolutionElements();
	    			for (ExpSolutionElemDto expDto : dto.getExpSolElemList() ) {
	    				if (explodedSEs != null) {
	    					for (ExpandedSolutionElement explodedSE : explodedSEs ) {
	    						if (explodedSE.getExpSolnElemntId().equals(expDto.getExpSolnElemntId())) {
	    							explodedSE.setRetestStatus(expDto.getRetestStatus());
	    							explodedSE.setArtRespCode(expDto.getArtRespCode());
	    							explodedSE.setArtRespMsg(expDto.getArtRespMsg());
	    						}
	    					}
	    				}	
	    			}
	    		}
	    		
	    		//set the childrens in the list
	    		List<ExpandedSolutionElement> explodedList = new ArrayList<ExpandedSolutionElement>();
	    		for (ExpandedSolutionElement explodedSE : sl.getExplodedSolutionElements() ) {
	    			explodedList.add(explodedSE);
				}
	    		sl.setExpSolutionElements(explodedList);
	    		
	    		if(dto.getReturnCode().equals(GRTConstants.TR_ALARM_successCode) && (processChildrenTRs(dto).equals(GRTConstants.TR_ALARM_successCode)) ) {//Success Case
	    			logger.debug("SL Id:" + sl.getId() + " succeeded with errorCode:" + dto.getReturnCode() + "." + " errorDesc:" + dto.getErrorDesc());
	    			
		    		//Populate data in new columns of Technical Registration table specifically created for step B
	    			//sl.setSolutionElementId(dto.getSname());
	    			sl.setDeviceOnboardOp(dto.getOnBoardingXML());
		    		
		    		if(StringUtils.isNotEmpty(dto.getTransactionDetails())) {
						if(dto.getTransactionDetails().length()>4000) {
							sl.setTransactionDetails(dto.getTransactionDetails().substring(0, 4000));
						} else {
							sl.setTransactionDetails(dto.getTransactionDetails());
						}
					}
		    		
		    		sl.setErrorCode(dto.getReturnCode());
		    		sl.setSubErrorCode(null);
		    		sl.setErrorDesc(dto.getErrorDesc());
    				Status status = new Status();
    				status.setStatusId(StatusEnum.COMPLETED.getStatusId());
    				//sl.setStatus(status);
    				sl.setStepBStatus(status);
    				sl.setStepBCompletedDate(new Date());
    				
    				getTechnicalRegistrationDao().saveSalSiteList(sl);
    				//Save the child elements
    				//getTechnicalRegistrationDao().saveSiteListWithExpElem(sl);
    				
    				sendEmailNoticeForTOBLine(null, sl, sl.getSiteRegistration(), StatusEnum.COMPLETED);
	    		} else {//Failure Case
	    			logger.debug("SL Id:" + sl.getId() + " failed with errorCode:" + dto.getReturnCode() + "." + " errorDesc:" + dto.getErrorDesc());
	    			//Defect ATOM Testing
	    			if( !dto.getReturnCode().equals(GRTConstants.TR_ALARM_successCode) )
	    				sl.setErrorCode(dto.getReturnCode());
	    			
	    			sl.setSubErrorCode(null);
	    			sl.setErrorDesc(dto.getErrorDesc());
    				if(StringUtils.isNotEmpty(dto.getSname())) {
    					logger.debug("Got FailedSEID:" + dto.getSname());
    					sl.setFailedSeid(dto.getSname());
    				}    				
    				Status status = new Status();
    				status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
    				//sl.setStatus(status);
    				sl.setStepBStatus(status);
    				
    				getTechnicalRegistrationDao().saveSalSiteList(sl);
    				
    				//Save the child elements
    				//getTechnicalRegistrationDao().saveSiteListWithExpElem(sl);
    				
    				failedSL = sl;
	    		}
	    		
	    	}
    	}catch (Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting TechnicalRegistrationAlarmAsyncService.handleSLResponse for SiteList ID:" + sl.getId());
    	}
    	return failedSL;
    }
	
	private TechnicalRegistration handleTRAlarmResponse(TechRegDataResponseDto dto, TechnicalRegistration tr) {
    	logger.debug("Entering TechnicalRegistrationAlarmAsyncService.handleTRResponse for TR ID:" + tr.getTechnicalRegistrationId());
    	
    	TechnicalRegistration failedTR = null;
    	
    	try {
	    	if(dto != null && tr != null) {
	    		//tr.setSolutionElementId(dto.getSname());
	    		tr.setTransactionDetails(dto.getTransactionDetails());
	    		//Defect#635 - Set the Child elements in the techreg from the dto with the update response
	    		if( dto.getExpSolElemList()!=null && !dto.getExpSolElemList().isEmpty() ){
	    			Set<ExpandedSolutionElement> explodedSEs = tr.getExplodedSolutionElements();
	    			for (ExpSolutionElemDto expDto : dto.getExpSolElemList() ) {
	    				if (explodedSEs != null) {
	    					for (ExpandedSolutionElement explodedSE : explodedSEs ) {
	    						if (explodedSE.getExpSolnElemntId().equals(expDto.getExpSolnElemntId())) {
	    							explodedSE.setRetestStatus(expDto.getRetestStatus());
	    							explodedSE.setArtRespCode(expDto.getArtRespCode());
	    							explodedSE.setArtRespMsg(expDto.getArtRespMsg());
	    						}
	    					}
	    				}	
	    			}
	    		}
	    		
	    		if( (dto.getReturnCode().equals(GRTConstants.TR_ALARM_successCode)) && (processChildrenTRs(dto).equals(GRTConstants.TR_ALARM_successCode)) ) {//Success Case
	    			logger.debug("TR Id:" + tr.getTechnicalRegistrationId() + " succeeded with errorCode:" + dto.getReturnCode() + "." + " errorDesc:" + dto.getErrorDesc());
		    		//Populate data in new columns of Technical Registration table specifically created for step B
		    		
		    		tr.setDeviceOnboardOp(dto.getOnBoardingXML());
		    		
		    		if(StringUtils.isNotEmpty(dto.getTransactionDetails())) {
						if(dto.getTransactionDetails().length()>4000) {
							tr.setTransactionDetails(dto.getTransactionDetails().substring(0, 4000));
						} else {
							tr.setTransactionDetails(dto.getTransactionDetails());
						}
					}
		    		
		    		tr.setErrorCode(dto.getReturnCode());
    				tr.setSubErrorCode(null);
    				tr.setErrorDesc(dto.getErrorDesc());
    				Status status = new Status();
    				status.setStatusId(StatusEnum.COMPLETED.getStatusId());
    				//Defect 440 : update the step b status
    				//tr.setStatus(status);
    				tr.setStepBStatus(status);
    				tr.setStepBCompletedDate(new Date());
    				
    				getTechnicalRegistrationDao().saveTechnicalRegistration(tr);
    				    				
    				sendEmailNoticeForTOBLine(tr, null, tr.getTechnicalOrder().getSiteRegistration(), StatusEnum.COMPLETED);
    				
	    		} else {//Failure Case
	    			logger.debug("TR Id:" + tr.getTechnicalRegistrationId() + " failed with errorCode:" + dto.getReturnCode() + "." + " errorDesc:" + dto.getErrorDesc());
	    			//Defect ATOM Testing
	    			if( !dto.getReturnCode().equals(GRTConstants.TR_ALARM_successCode) )
	    				tr.setErrorCode(dto.getReturnCode());
    				tr.setSubErrorCode(null);
    				tr.setErrorDesc(dto.getErrorDesc());
    				
    				if(StringUtils.isNotEmpty(dto.getSname())) {
    					logger.debug("Got FailedSEID:" + dto.getSname());
    					tr.setFailedSeid(dto.getSname());
    				}    				
    				Status status = new Status();
    				status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
    				//tr.setStatus(status);
    				tr.setStepBStatus(status);
    				failedTR = tr;
	    		}
	    	}	
	    		
	    } catch(Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting TechnicalRegistrationAlarmAsyncService.handleTRResponse for TR ID:" + tr.getTechnicalRegistrationId());
    	}
    	return failedTR;
    }
	
	//Create the parent child relationship
	private List<TechRegDataResponseDto> arrangeParent(List<TechRegDataResponseDto> techRegResponseDtos) {
		List<TechRegDataResponseDto> newTechRegResponseDtos = new ArrayList<TechRegDataResponseDto>();
		
		//ATOM Defects
		//List<String> techRegIds = getAllTechRegIdFromResp(techRegResponseDtos);
		
		//Parse through all records received from ART 
		for (TechRegDataResponseDto techRegResponseDto : techRegResponseDtos ) {
		
			if (StringUtils.isNotEmpty(techRegResponseDto.getRegistrationId()) && StringUtils.isNotEmpty(techRegResponseDto.getTransactionId())) {
				try {
					//Fetch the GRT Technical Registration record based on tech_reg_id received from ART
					TechnicalRegistration techRegistration = getTechnicalRegistrationAsyncDao().getTechnicalRegistration(techRegResponseDto.getTransactionId());
					
					//Defect# 785
					boolean isTechReg = false;
					
					if (techRegistration != null) {
						techRegResponseDto = addChildrenTRs(techRegistration, techRegResponseDto, techRegResponseDtos);
						isTechReg = true;
					}else 
						//ATOM Defect(736 * 776) : If only child is present in the response then find it's parent to complete the registration
						if (techRegistration == null) {
							techRegistration = getTechnicalRegistrationAsyncDao().getTechRegByExpSolId(techRegResponseDto.getTransactionId());
							//If technicalregistration registration id is already in the response from ART then don't make any changes
							if( techRegistration != null ){
								isTechReg = true;
								if( findTechRegIdInRespList(techRegistration.getTechnicalRegistrationId(),techRegResponseDtos) 
										|| findTechRegIdInRespList(techRegistration.getTechnicalRegistrationId(),newTechRegResponseDtos)	
										){
									techRegistration = null;
								}else{
									TechRegDataResponseDto techRegRespDto = new TechRegDataResponseDto();

									techRegRespDto.setRegistrationId(techRegResponseDto.getRegistrationId());
									techRegRespDto.setTransactionId(techRegistration.getTechnicalRegistrationId());
									techRegRespDto.setReturnCode(GRTConstants.TR_ALARM_successCode);
									techRegResponseDto = addChildrenTRs(techRegistration, techRegRespDto, techRegResponseDtos);
								}
							}
						}
					
					//If it is not a in technical registration, then it will be in Site List
					if( !isTechReg  ){
						techRegResponseDto = processSiteListRec( techRegResponseDto, techRegResponseDtos, newTechRegResponseDtos );
					}
					
				} catch (DataAccessException dae) {
    				logger.error("Error: while accessing the technical registration",dae);
    			}
				
				newTechRegResponseDtos.add(techRegResponseDto);
			}
		}
		return newTechRegResponseDtos;
	}
	
	/* START :: Method Specific to SITE LIST */
	
	//Process the site list records from Response sent by ART
	public TechRegDataResponseDto processSiteListRec( TechRegDataResponseDto techRegResponseDto, List<TechRegDataResponseDto> techRegResponseDtos,
														List<TechRegDataResponseDto> newTechRegResponseDtos ) throws DataAccessException{
		//Fetch the GRT Site List Registration record based on site_list_id received from ART
		SiteList siteList = getTechnicalRegistrationAsyncDao().getSiteList(techRegResponseDto.getTransactionId());
		
		if (siteList != null) {
			techRegResponseDto = addChildrenSLs(siteList, techRegResponseDto, techRegResponseDtos);
		}else 
			//ATOM Defect(736 * 776) : If only child is present in the response then find it's parent to complete the registration
			if (siteList == null) {
				siteList = getTechnicalRegistrationAsyncDao().getSiteListByExpSolId(techRegResponseDto.getTransactionId());
				if( siteList!=null ){
					//If site list id is already in the response from ART then don't make any changes
					if( siteList!=null && findTechRegIdInRespList(siteList.getId(),techRegResponseDtos) 
							|| findTechRegIdInRespList(siteList.getId(),newTechRegResponseDtos)	
							){
						siteList = null;
					}else{
						TechRegDataResponseDto techRegRespDto = new TechRegDataResponseDto();
						techRegRespDto.setRegistrationId(techRegResponseDto.getRegistrationId());
						techRegRespDto.setTransactionId(siteList.getId());
						techRegRespDto.setReturnCode(GRTConstants.TR_ALARM_successCode);
						techRegResponseDto = addChildrenSLs(siteList, techRegRespDto, techRegResponseDtos);
					}
				}
			}
		return techRegResponseDto;
	}
	
	//Get the childrens for the site list
	public TechRegDataResponseDto addChildrenSLs(SiteList siteList, TechRegDataResponseDto techRegResponseDto, List<TechRegDataResponseDto> techRegResponseDtos){
		List<TechRegDataResponseDto> childTechRegResponseDtos = new ArrayList<TechRegDataResponseDto>();
		List<ExpSolutionElemDto> expElemDtos = new ArrayList<ExpSolutionElemDto>();

		//Parse through all records received from ART 
		for (TechRegDataResponseDto techRegResponseDtoNew : techRegResponseDtos ) {
		
			if (StringUtils.isNotEmpty(techRegResponseDto.getRegistrationId()) && StringUtils.isNotEmpty(techRegResponseDto.getTransactionId())) {
				
				Set<ExpandedSolutionElement> explodedSEs = siteList.getExplodedSolutionElements();
				
				if (explodedSEs != null) {
					for (ExpandedSolutionElement explodedSE : explodedSEs ) {
						if (explodedSE.getExpSolnElemntId().equals(techRegResponseDtoNew.getTransactionId())) {
							childTechRegResponseDtos.add(techRegResponseDtoNew);
							//Defect#635 - Update the response from ART
							/*if (techRegResponseDtoNew.getReturnCode().equals(GRTConstants.TR_ALARM_successCode)){
								explodedSE.setRetestStatus(GRTConstants.SUCCESS);
							}else{
								explodedSE.setRetestStatus(GRTConstants.FAILURE);
							}
							explodedSE.setArtRespCode(techRegResponseDtoNew.getReturnCode());
							explodedSE.setArtRespMsg(techRegResponseDtoNew.getErrorDesc());*/
							ExpSolutionElemDto dto = new ExpSolutionElemDto();
							dto.setExpSolnElemntId(explodedSE.getExpSolnElemntId());
							if (techRegResponseDtoNew.getReturnCode().equals(GRTConstants.TR_ALARM_successCode)){
								dto.setRetestStatus(GRTConstants.SUCCESS);
							}else{
								dto.setRetestStatus(GRTConstants.FAILURE);
							}
							dto.setArtRespCode(techRegResponseDtoNew.getReturnCode());
							dto.setArtRespMsg(techRegResponseDtoNew.getErrorDesc());
							expElemDtos.add(dto);
						}
					}
				}				
			}
		}
		techRegResponseDto.setExpSolElemList(expElemDtos);
		techRegResponseDto.setChildren(childTechRegResponseDtos);
		
		return techRegResponseDto;
	}

	/* END :: Method Specific to SITE LIST */
	
	//Take all the techregid's(both Exp & techreg) from response
	public boolean findTechRegIdInRespList(String id, List<TechRegDataResponseDto> techRegResponseDtos){
		List<String> techRegIds = new ArrayList<String>();
		for( TechRegDataResponseDto techRegDto : techRegResponseDtos){
			if(techRegDto.getTransactionId().equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}
	
	private TechRegDataResponseDto addChildrenTRs(TechnicalRegistration techRegistration, TechRegDataResponseDto techRegResponseDto, List<TechRegDataResponseDto> techRegResponseDtos) {
		
		List<TechRegDataResponseDto> childTechRegResponseDtos = new ArrayList<TechRegDataResponseDto>();
		List<ExpSolutionElemDto> expElemDtos = new ArrayList<ExpSolutionElemDto>();

		//Parse through all records received from ART 
		for (TechRegDataResponseDto techRegResponseDtoNew : techRegResponseDtos ) {
		
			if (StringUtils.isNotEmpty(techRegResponseDto.getRegistrationId()) && StringUtils.isNotEmpty(techRegResponseDto.getTransactionId())) {
				
				Set<ExpandedSolutionElement> explodedSEs = techRegistration.getExplodedSolutionElements();
				
				if (explodedSEs != null) {
					for (ExpandedSolutionElement explodedSE : explodedSEs ) {
						if (explodedSE.getExpSolnElemntId().equals(techRegResponseDtoNew.getTransactionId())) {
							childTechRegResponseDtos.add(techRegResponseDtoNew);
							//Defect#635 - Update the response from ART
							/*if (techRegResponseDtoNew.getReturnCode().equals(GRTConstants.TR_ALARM_successCode)){
								explodedSE.setRetestStatus(GRTConstants.SUCCESS);
							}else{
								explodedSE.setRetestStatus(GRTConstants.FAILURE);
							}
							explodedSE.setArtRespCode(techRegResponseDtoNew.getReturnCode());
							explodedSE.setArtRespMsg(techRegResponseDtoNew.getErrorDesc());*/
							ExpSolutionElemDto dto = new ExpSolutionElemDto();
							dto.setExpSolnElemntId(explodedSE.getExpSolnElemntId());
							if (techRegResponseDtoNew.getReturnCode().equals(GRTConstants.TR_ALARM_successCode)){
								dto.setRetestStatus(GRTConstants.SUCCESS);
							}else{
								dto.setRetestStatus(GRTConstants.FAILURE);
							}
							dto.setArtRespCode(techRegResponseDtoNew.getReturnCode());
							dto.setArtRespMsg(techRegResponseDtoNew.getErrorDesc());
							expElemDtos.add(dto);
						}
					}
				}				
			}
		}
		techRegResponseDto.setExpSolElemList(expElemDtos);
		techRegResponseDto.setChildren(childTechRegResponseDtos);
		
		return techRegResponseDto;
	}
	
	private String processChildrenTRs(TechRegDataResponseDto dto) {
		String result = GRTConstants.TR_ALARM_successCode;
		List<TechRegDataResponseDto> childTechRegResponseDtos = dto.getChildren();

		//Parse through all children SEID's 
		if (childTechRegResponseDtos != null) {
			for (TechRegDataResponseDto techRegResponseDtoNew : childTechRegResponseDtos ) {
				if (!techRegResponseDtoNew.getReturnCode().equals(GRTConstants.TR_ALARM_successCode))
					return GRTConstants.TR_errorCode;
			
			}
		}		
		
		return result;
	}

	public TechnicalRegistrationAsyncDao getTechnicalRegistrationAsyncDao() {
		return technicalRegistrationAsyncDao;
	}

	public void setTechnicalRegistrationAsyncDao(
			TechnicalRegistrationAsyncDao technicalRegistrationAsyncDao) {
		this.technicalRegistrationAsyncDao = technicalRegistrationAsyncDao;
	}
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		TechnicalRegistrationAlarmAsyncService alarmService = (TechnicalRegistrationAlarmAsyncService) context.getBean("technicalRegistrationAlarmAsyncService");
		
		String techRegId = "7058626";
		List<TechnicalRegistration> failedTRs = new ArrayList<TechnicalRegistration>();
		
		TechnicalRegistration tr = alarmService.getTechnicalRegistrationAsyncDao().getTechnicalRegistration(techRegId);
		tr.setErrorCode("600");
		tr.setSubErrorCode(null);
		tr.setErrorDesc("e (628)084-9253 belongs to SECode VMPRO which is not supported for Connectivity and Alarming Test");
		Status status = new Status();
		status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
		tr.setStatus(status);
		
		SiteRegistration siteRegistration = tr.getTechnicalOrder().getSiteRegistration();
		
		failedTRs.add(tr);
		alarmService.createSR(siteRegistration, failedTRs, null, TechRecordEnum.ALARM);
		
	}	
	
}
