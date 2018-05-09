package com.avaya.grt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.TechnicalRegistrationAsyncDao;
import com.avaya.grt.jms.avaya.v2.techregistration.TRResponseResultType;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.Activity;
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



public class TechnicalRegistrationAsyncService extends TechnicalRegistrationService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationAsyncService.class);
	public TechnicalRegistrationAsyncDao technicalRegistrationAsyncDao;
	
	private static Integer numberOfSubmit=new Integer(0);

	public void processReceivedTRAsynchResponse(List<TechRegDataResponseDto> techRegDataResponseDtos) throws Throwable {
		processReceivedTechRegReqNew(techRegDataResponseDtos);
	}
	
	/*
     * [AVAYA] GRT 4.0  Process Technical Registration response
     *
     * handling of GRT.TechReg.Response.Queue
     *
     */
	public TRResponseResultType processReceivedTechRegReqNew(List<TechRegDataResponseDto> techRegResponseDtos) throws DataAccessException {
		logger.debug("Entering TechnicalRegistrationAsyncService.processReceivedTechRegReqNew");
		TRResponseResultType responseResultType = new TRResponseResultType();
		StringBuffer sb = new StringBuffer();
		sb.append("Entering TechnicalRegistrationAsyncService.processReceivedTechRegReqNew to process the TechReg request recieved... ");
    	if(techRegResponseDtos != null && techRegResponseDtos.size() >0) {
    		sb.append("Start the processing when Data object is not null in the recieved ASYNC request Payload... ");
    		SiteRegistration siteRegistration = null;
    		List<TechnicalRegistration> failedTRs = new ArrayList<TechnicalRegistration>();
    		List<TechnicalRegistration> failedTRSALs = new ArrayList<TechnicalRegistration>();
    		List<SiteList> failedSLs = new ArrayList<SiteList>();
    		boolean createSR = false;
    		boolean sendAwaitingNotification = false;
    		boolean skipNotification = true;
    		    		
    		for (TechRegDataResponseDto techRegResponseDto : techRegResponseDtos ) {
		    	try {
			    	logger.debug("Processing Record for RegistrationId:" + techRegResponseDto.getRegistrationId() + " Type:" + techRegResponseDto.getTechRegDetail() + " Line ID:" + techRegResponseDto.getTransactionId());
			    	sb.append("Inside for loop processing data objects... ");
			    	SkipDateTimeEscalator.set(Boolean.TRUE);

		    		if (StringUtils.isNotEmpty(techRegResponseDto.getRegistrationId()) && StringUtils.isNotEmpty(techRegResponseDto.getTransactionId())) {
		    			sb.append("Inside if when Registeration & Transaction ID is not empty... ");
		    			try {
		    				if(techRegResponseDto.getTechRegDetail().equalsIgnoreCase(GRTConstants.TECHREGDETAIL_TR)) {
		    					sb.append("Inside if when Technical Registeration details equals to TR... ");
		    					TechnicalRegistration techRegistration = getTechnicalRegistrationAsyncDao().getTechnicalRegistration(techRegResponseDto.getTransactionId());
		    						try {
		    							if (techRegistration != null) {
		    								sb.append("Inside if when Technical Registeration for the Transaction ID is not null... ");
				    						//it is for Technical Registration, i.e., IP,Modem, IPO
		    								if(siteRegistration == null) {
				    							siteRegistration = techRegistration.getTechnicalOrder().getSiteRegistration();
				    							sb.append("Got the SiteRegistration from the techRegisteration... ");
				    						}
		    								if(StatusEnum.AWAITINGINFO.getStatusId().equals(techRegistration.getStatus().getStatusId())
		    										|| StatusEnum.COMPLETED.getStatusId().equals(techRegistration.getStatus().getStatusId())){
		    									sb.append("Continue and skip the notification if Status of the Registeration is either AWAITING INFO or COMPLETED... ");
		    									continue;
		    								}
		    								skipNotification = false;		    								
				    						
				    	    				if(techRegResponseDto.getOptype().equalsIgnoreCase(ARTOperationType.REGENONBOARDXML.getOperationKey()) || techRegResponseDto.getOptype().equalsIgnoreCase(ARTOperationType.PASSWORDRESET.getOperationKey())) {
				    	    					if(techRegResponseDto.getTechRegResult() != null && techRegResponseDto.getTechRegResult().get(0) != null && StringUtils.isNotEmpty(techRegResponseDto.getTechRegResult().get(0).getReturnCode()) && techRegResponseDto.getTechRegResult().get(0).getReturnCode().equals("0")) {
				    	    						sb.append("Send the email notification when ART Operation are in between Regenrate Onboarding XML or Password Reset... ");
				    	    						sendEmailNotice(techRegistration, true, techRegResponseDto.getOnBoardingXML(), ART_EMAIL_ALERT_TYPE_COMPLETED, techRegResponseDto.getOptype());
				    	    					}
				    	    					responseResultType.setRoutingInfo(sb.toString());
				    	    					return responseResultType;
				    	    				}
				    						if(techRegistration.getNumberOfSubmitAsInt() == 1 && siteRegistration.getTechRegStatus().getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId())) {
				    							sb.append("Send the email Awaiting notification when Site Registeration status is Awaiting Info & number of submit is equals to 1... ");
				    							sendAwaitingNotification = true;
				    						}
				    						TechnicalRegistration failedTR = handleTRResponse(techRegResponseDto, techRegistration);
				    						sb.append("getting all the TR details those who failed... ");
				    						if(failedTR != null) {
				    							if(failedTR.getNumberOfSubmitAsInt() > 1 && failedTR.getSrRequest() == null) {
				    								sb.append("Creating the SR for the failed TR & number of submittion is more than 1... ");
				    								createSR = true;
				    							}
				    							if(AccessTypeEnum.SAL.getDbAccessType().equalsIgnoreCase(failedTR.getAccessType())) {
					    							failedTRSALs.add(failedTR);
					    						} else {
					    							failedTRs.add(failedTR);
					    						}
				    						}
		    							} else {
											logger.warn("TR record with ID:" + techRegResponseDto.getTransactionId() + " NOT found.");
											sb.append("TR record with ID:" + techRegResponseDto.getTransactionId() + " NOT found... ");
										}
									} catch (Exception e) {
										sb.append("Error!! while handling IP/Modem/IPO Asynch Response... ");
										logger.error("Error!! while handling IP/Modem/IPO Asynch Response", e);
									}
								} else if (techRegResponseDto.getTechRegDetail().equalsIgnoreCase(GRTConstants.TECHREGDETAIL_SL)) {
									sb.append("Inside if when Technical Registeration details equals to SL");
									//it is for SAL Migration Registrations
									SiteList sl = getTechnicalRegistrationAsyncDao().getSiteList(techRegResponseDto.getTransactionId());
									sb.append("Extract site list from Tech Reg Async dao based on Transaction ID... ");
									try {
										if(sl != null) {
											sb.append("if Site list is not null get the siteRegisteration if that is NULL... ");
											if(siteRegistration == null) {
				    							siteRegistration = sl.getSiteRegistration();
				    						}
											if(StatusEnum.AWAITINGINFO.getStatusId().equals(sl.getStatus().getStatusId())
		    										|| StatusEnum.COMPLETED.getStatusId().equals(sl.getStatus().getStatusId())){
												sb.append("Continue and skip the notification if Status of the Registeration is either AWAITING INFO or COMPLETED for SL type... ");
		    									continue;
		    								}
		    								skipNotification = false;
											
											
											if(sl.getNumberOfSubmitAsInt() == 1 && siteRegistration.getTechRegStatus().getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId())) {
												sb.append("Send the email Awaiting notification when Site Registeration status is Awaiting Info & number of submit from site list is equals to 1... ");
												sendAwaitingNotification = true;
				    						}
											SiteList failedSL = handleSLResponse(techRegResponseDto, sl);
											if(failedSL != null) {
												if(failedSL.getNumberOfSubmitAsInt() > 1 && failedSL.getSrRequest() == null) {
				    								createSR = true;
				    								sb.append("Create SR for Failed SL ");
				    							}
												failedSLs.add(failedSL);
											}
										} else {
											sb.append("SiteList record with ID:" + techRegResponseDto.getTransactionId() + " NOT found... ");
											logger.warn("SiteList record with ID:" + techRegResponseDto.getTransactionId() + " NOT found.");
										}
									} catch (Exception e) {
										sb.append("Error!! while handling Sal Asynch Response... ");
										logger.error("Error!! while handling Sal Asynch Response",e);
									}
		    					}
			    			} catch (DataAccessException dae) {
			    				sb.append("Error!! DataAccessException while handling Sal Asynch Response... ");
			    				logger.error("Error: while accessing the techincal registration",dae);
			    			}
			    		} else {
			    			sb.append("Got Null/blank grtId... ");
			    			logger.error("Got Null/blank grtId");
			    			responseResultType.setCode("ERROR");
			    			responseResultType.setDescription("Got Null/blank grtId");
			    		}
			    	} finally {
			    		SkipDateTimeEscalator.unset();
			    		sb.append("Processed record for RegistrationId:" + techRegResponseDto.getRegistrationId() + " Type:" + techRegResponseDto.getTechRegDetail() + " Line ID:" + techRegResponseDto.getTransactionId()+"... ");
			    		logger.debug("Processed record for RegistrationId:" + techRegResponseDto.getRegistrationId() + " Type:" + techRegResponseDto.getTechRegDetail() + " Line ID:" + techRegResponseDto.getTransactionId());
			    	}
    			}
    			try {
    				if(siteRegistration != null) {
	    				if((failedTRs != null && failedTRs.size() > 0) || (failedTRSALs != null && failedTRSALs.size() > 0)) {
	    					if(failedTRs != null && failedTRs.size() > 0) {
		    					SRRequest srRequest = null;
		    					if(createSR){
		    						sb.append("Creating the Service Request for Failed TRs... ");
		    						srRequest = this.createSR(siteRegistration, failedTRs, null, TechRecordEnum.TR);
		    					}
		    					for (TechnicalRegistration tr : failedTRs) {
		    						if(srRequest != null) {
										tr.setSrRequest(srRequest);
										tr.setArtSrNo(srRequest.getSiebelSRNo());
									}
		    						getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(tr);
									if(tr.getNumberOfSubmitAsInt() > 1) {
										sendEmailNoticeForTOBLine(tr, null, siteRegistration, StatusEnum.AWAITINGINFO);
									}
								}
	    					}
	    					if(failedTRSALs != null && failedTRSALs.size() > 0) {
		    					SRRequest srRequest = null;
		    					if(createSR) {
		    						sb.append("Creating the Service Request for Failed SALs... ");
		    						srRequest = this.createSR(siteRegistration, failedTRSALs, null, TechRecordEnum.SAL_MIGRATION);
		    					}
		    					for (TechnicalRegistration tr : failedTRSALs) {
		    						if(srRequest != null) {
										tr.setSrRequest(srRequest);
										tr.setArtSrNo(srRequest.getSiebelSRNo());
									}
		    						getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(tr);
									if(tr.getNumberOfSubmitAsInt() > 1) {
										sendEmailNoticeForTOBLine(tr, null, siteRegistration, StatusEnum.AWAITINGINFO);
									}
								}
	    					}
	    				} else if(failedSLs != null && failedSLs.size() > 0) {
	    					SRRequest srRequest = null;
	    					if(createSR) {
	    						sb.append("Creating the Service Request for Failed SLs... ");
	    						srRequest = this.createSR(siteRegistration, null, failedSLs, TechRecordEnum.SAL_MIGRATION);
	    					}
	    					for (SiteList sl : failedSLs) {
	    						if(srRequest != null) {
									sl.setSrRequest(srRequest);
									sl.setArtSrNo(srRequest.getSiebelSRNo());
								}
	    						getTechnicalRegistrationAsyncDao().saveSalSiteList(sl);

								if(sl.getNumberOfSubmitAsInt() > 1) {
									sendEmailNoticeForTOBLine(null, sl, siteRegistration, StatusEnum.AWAITINGINFO);
								}
							}
	    				}
    				}
    			} catch (Throwable throwable) {
    				logger.error("Error while creating SRs for failed transactions", throwable);
    				responseResultType.setCode("ERROR");
	    			responseResultType.setDescription("Error while creating SRs for failed transactions");
    			}
    			if(siteRegistration != null && !skipNotification) {
	    			try {//Reload the SiteRegistration from DB, By-passing 1st level cache.
	    				sb.append("Reloading the SiteRegistration from DB and preparing to sending the notification if its false... ");
	    				getTechnicalRegistrationAsyncDao().refresh(siteRegistration);
	    				StatusEnum newStatus = siteRegistration.computeTRHeaderStatus();
	    				sb.append("Got the new status for the TR Header from Site Registeration... "+newStatus.getStatusId());
	    				logger.debug("Existing TechRegStatusId:" + siteRegistration.getTechRegStatus().getStatusId());
	    				logger.debug("New TechRegStatusId:" + ((newStatus!=null)?newStatus.getStatusId():""));
	    				logger.debug("sendAwaitingNotification:" + sendAwaitingNotification);
		    			if(newStatus != null && !siteRegistration.getTechRegStatus().getStatusId().equals(newStatus.getStatusId())) {
		    				getTechnicalRegistrationAsyncDao().updateSiteRegistrationStatus(siteRegistration, newStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
		    				sb.append("Updating the SiteRegistration with new header status and Technical On-Boarding Completed date as current date");
		    				this.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, newStatus);
		    			} else if(sendAwaitingNotification) {
		    				this.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, StatusEnum.AWAITINGINFO);
		    			}
	    			} catch(Throwable throwable) {
	    				logger.error("", throwable);
	    				logger.debug(throwable.getMessage());
	    				sb.append("Error while updating status/sending mail");
	    				throwable.printStackTrace();
	    			}
	    		}
    		}
	    	logger.debug("Exiting TechnicalRegistrationAsyncService.processReceivedTechRegReqNew");
	    	responseResultType.setRoutingInfo(sb.toString());
	    	return responseResultType;
	}
		
	/*
     * [AVAYA] GRT 2.0  Process Technical Registration response
     *
     * handling of GRT.TechReg.Request.Queue
     *
     */
    public void processReceivedTechRegRetryReq(TechRegResultResponseDto techRegResultResponseDto) throws DataAccessException {

    	try {
        	logger.debug("Entring TechnicalRegistrationAsyncService.processReceivedTechRegRetryReq for grtid:" + techRegResultResponseDto.getGrtid());
        	SkipDateTimeEscalator.set(Boolean.TRUE);
        	if(techRegResultResponseDto != null && StringUtils.isNotEmpty(techRegResultResponseDto.getArtsr())) {
				logger.debug("Ignoring ART created SR:" + techRegResultResponseDto.getArtsr());
				techRegResultResponseDto.setArtsr(null);
        	}
    		if (techRegResultResponseDto.getReturnCode().equals("0")) {
    			logger.debug("handle success for GRTID:" + techRegResultResponseDto.getGrtid());
    			sucessCase(techRegResultResponseDto.getGrtid(),"");
    		} else {
    			logger.debug("handle failure for GRTID:" + techRegResultResponseDto.getGrtid());
    			failureCase(techRegResultResponseDto.getGrtid(), techRegResultResponseDto.getArtsr(), techRegResultResponseDto.getReturnCode(), 
    																				techRegResultResponseDto.getDescription(), "NO-OPT");
    		}
    	} finally {
    		SkipDateTimeEscalator.unset();
        	logger.debug("Exiting TechnicalRegistrationAsyncService.processReceivedTechRegRetryReq.");
    	}
    }
    
    public void sucessCase(String grtid, String optType)  {
    	logger.debug("Entering TechnicalRegistrationAsyncService.SuscessCase for grtId:" + grtid + " for operation:" + optType);
    	
    	TechnicalRegistration technicalRegistration = null;
    	SiteRegistration siteRegistration = null;
    	SiteList siteList = null;
    	String techOrderId = null;
    	
    	try{
    		technicalRegistration = getTechnicalRegistrationAsyncDao().getTechnicalRegistration(grtid);	
    	if (technicalRegistration != null) {
    		logger.debug("found the id in Technical Registration...Starting");
    		techOrderId = technicalRegistration.getTechnicalOrder().getOrderId();
    		
    		if(!(optType.equals(OpTypeEnum.PASSWORDRESET.getOpType()) || optType.equals(OpTypeEnum.ONBOARDINGXML.getOpType()))) {
			    Status status = new Status();    			 
	        	status.setStatusId(StatusEnum.INPROCESS.getStatusId());
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
    		getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(technicalRegistration);
            String registrationID = technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId();
            siteRegistration = getTechnicalRegistrationAsyncDao().getSiteRegistration(registrationID);
        	/*if(siteRegistration != null && siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())){
            	getRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, ProcessStepEnum.TECHNICAL_REGISTRATION);
        	} */
            if(!(optType.equals(OpTypeEnum.PASSWORDRESET.getOpType()) || optType.equals(OpTypeEnum.ONBOARDINGXML.getOpType()))) {
            	if(siteRegistration != null &&  (siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())) || !siteRegistration.isAnyTechRegAwaiting()) {
            		getTechnicalRegistrationAsyncDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
            	}
            	try{
        	        	if(StringUtils.isNotEmpty(techOrderId))	{
        	        		//getRegistrationDao().technicalOrderQuantityUpdate(techOrderId);
        	        	}
        	       } catch(Exception e) {
        	            logger.error("ERROR: While trying to update remaining quantity after "
        	                    + "successful Technical Registration submit to ART.\n"
        	                    + "Original error: " + e.getMessage() + "\n"
        	                    + "Technical Order ID [" + techOrderId + "]\n"
        	                    + "Registration ID [" + registrationID + "]", e);
        	       }
            }
        	//sendRegistrationRequestAlert(technicalRegistration, StatusEnum.INPROCESS);
        	logger.debug("Completed IP/MODEM/IPO Technical Registration.");
        } else if ((siteList = getTechnicalRegistrationAsyncDao().getSiteList(grtid)) !=null ) {
				logger.debug("found the id in Site List....Starting");
				Status s = new Status();
				s.setStatusId(StatusEnum.INPROCESS.getStatusId());
				siteList.setStatus(s);
				getTechnicalRegistrationAsyncDao().saveSalSiteList(siteList);
				logger.debug("found the id in Site List....Completed");
			}
        } catch (DataAccessException dae){
    		logger.debug("Error while accessing dao objects", dae);
    		logger.error("Error!",dae);
    	} catch (Exception e) {
    		logger.error("Error",e);
    	}
    	logger.debug("Exiting TechnicalRegistrationAsyncService.SucessCase");
    }
	
    
    public void failureCase(String grtId, String artSR, String returnCode, String errorDesc, String optType) {
    	logger.debug("Entering TechnicalRegistrationAsyncService.failure Case for GRTID:" + grtId);
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
    	
    	if ((technicalRegistration = getTechnicalRegistrationAsyncDao().getTechnicalRegistration(grtId)) != null) {
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
        	
        	getTechnicalRegistrationAsyncDao().saveTechnicalRegistration(technicalRegistration);
            String registrationID = technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId();
            siteRegistration = getTechnicalRegistrationAsyncDao().getSiteRegistration(registrationID);
            if(!(optType.equals(OpTypeEnum.PASSWORDRESET.getOpType()) || optType.equals(OpTypeEnum.ONBOARDINGXML.getOpType()))) {
            	if(siteRegistration != null && !siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
            		getTechnicalRegistrationAsyncDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
            	}
            }
            sendRegistrationRequestAlert(technicalRegistration, StatusEnum.AWAITINGINFO);
    		logger.debug("Completed failure case for IP/Modem/IPO TR.");
    		return;
    		
    	} else if ((siteList = getTechnicalRegistrationAsyncDao().getSiteList(grtId)) !=null ) {
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
			getTechnicalRegistrationAsyncDao().saveSalSiteList(siteList);
			siteRegistration = siteList.getSiteRegistration();
			if(siteRegistration != null && !siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())){
				getTechnicalRegistrationAsyncDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
        	}
		}
    	}catch (DataAccessException dae){
    		logger.debug("Error while accessing dao objects:"+dae.getMessage());
    		logger.error("Error!",dae);
    	} catch (Exception e) {
    		logger.error("Error!",e);
    	}
    	logger.debug("Exiting TechnicalRegistrationAsyncService.failureCase");
    	
    }
	
	public void sendEmailNotice(TechnicalRegistration technicalRegistration, boolean isSuccessfulComplete, String onBoardXML, String alertErrorType, String operationType) {
		String someDetails = "Registration ID ["+ technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId() + "]\n"
				+ "Technical Registration ID ["	+ technicalRegistration.getTechnicalRegistrationId() + "]\n"+ "Is email for successful complete [" 
				+ isSuccessfulComplete+ "]\n";

		logger.debug("Entering TechnicalRegistrationAsyncService.sendEmailNotice with \n" + someDetails);
		
		RegistrationRequestAlert result = null;
		SiteRegistration siteRegistration = null;
		// String downloadlink = null;
		try {
//			siteRegistration = technicalRegistration.getTechnicalOrder().getSiteRegistration();
			siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId());
			if (StringUtils.isNotEmpty(onBoardXML) || (StringUtils.isNotEmpty(operationType)&& (operationType.equals("PR") || operationType.equals("RN")))) {
				result = RegistrationRequestAlertConvertor.convertTechRegIPO(siteRegistration, technicalRegistration, operationType, getArtServiceName());
			} else {
				result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);
			}
			
			// We will implement download link later.
			// downloadlink = downloadLink(technicalRegistrationID);
		} catch (Exception e) {
			logger.error("ERROR: Unexpected failure while trying retrieve Registration record from DB with error: "+ e.getMessage() + "\n" + someDetails);
			return;
		}

		if (isSuccessfulComplete) {
			// Compose email for successful complete:
			result.setSendMail(true);
			result.setActionRequired(GRTConstants.NO);
			// result.setDownloadLink(downloadlink);
			if (onBoardXML != null) {
				result.setAttatchmentContents(onBoardXML);
				result.setMaterialCode(technicalRegistration.getTechnicalOrder().getMaterialCode());
				result.setMaterialCodeDescription(technicalRegistration.getTechnicalOrder().getDescription());
				result.setAlarmId(technicalRegistration.getAlarmId());
				result.setIpoFilePath(onBoardXML);
				// result.setIpoFilePath(saveOnboardXMLFile(onBoardXML,technicalRegistration.getTechnicalRegistrationId()));
			}
		} else {
			// Compose email for ART failures:
			result.setSiebelSRStatus(GRTConstants.SIEBEL_SR_STATUS_PENDING);
			result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_ART);
			result.setActionRequired(GRTConstants.ART_FAILURE_ACTION_REQUIRED);
			if (alertErrorType.equals(ART_EMAIL_ALERT_TYPE_RESPONSE_FAILED)) {
				// If failure is due to ART errorCode
				result.setSendMail(true);
				result.setIsSystemMail(false);

				// Overide action required from latest note in Siebel SR:
				if (technicalRegistration != null && StringUtils.isNotEmpty(technicalRegistration.getArtSrNo())) {
					logger.debug("Retrieving latest SR Note from Siebel ... ");
					String siebelSRNumber = technicalRegistration.getArtSrNo();
					result.setSiebelSRNumber(siebelSRNumber);
					try {
						Activity latestPublicSRNoteActivity = getSiebelClient().queryLatestPublicSRNoteActivity(siebelSRNumber);
						if (latestPublicSRNoteActivity != null) {
							result.setActionRequired(latestPublicSRNoteActivity.getDescription());
						}
					} catch (Exception e) {
						// Don't fail but just log it and move on:
						logger.error("ERROR: Unexpected failure while trying retrieve latest Siebel activity note for SR Number ["+ siebelSRNumber
										+ "] with error: "+ e.getMessage() + "\n" + someDetails);
					}
				}
			} else {
				// Other unexpected exception:
				result.setSendMail(false);
				result.setIsSystemMail(true);
			}
		}

		try {
			
			getMailUtil().sendMailNotification(result);
			logger.debug("Exiting TechnicalRegistrationAsyncService.sendEmailNotice");
		} catch (Exception e) {
			logger
					.error("ERROR: Unexpected failure while trying send email with error: "
							+ e.getMessage() + "\n" + someDetails);
		}
	}
	
	private SiteList handleSLResponse(TechRegDataResponseDto dto, SiteList sl) throws DataAccessException {
    	logger.debug("Entering TechnicalRegistrationAsyncService.handleSLResponse for SiteList Id:" + sl.getId());
    	SiteList failedSL = null;
    	try{
	    	if(dto != null && sl != null) {
	    		TechRegResultResponseDto result = null;
	    		if(dto.getTechRegResult() != null ) {
	    			result = dto.getTechRegResult().get(0);
	    			if(result != null && result.getReturnCode().equals("0")) {//Success Case
	    				logger.debug("SiteList Id:" + sl.getId() + " processed successfully.");
	    				sl.setErrorCode(null);
	    				sl.setSubErrorCode(null);
	    				sl.setErrorDesc(null);
	    				sl.setInstallScript(dto.getInstallScript());
	    				Status status = new Status();
	    				status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	    				sl.setStatus(status);
	    				sl.setStepACompletedDate(new Date());
	    				getTechnicalRegistrationDao().saveSalSiteList(sl);
	    				sendEmailNoticeForTOBLine(null, sl, sl.getSiteRegistration(), StatusEnum.COMPLETED);
	    			} else { //Failure case
	    				logger.debug("SiteList Id:" + sl.getId() + " failed with errorCode:" + result.getReturnCode() + "." + " errorDesc:" + result.getDescription());
	    				sl.setErrorCode(result.getReturnCode());
	    				sl.setSubErrorCode(result.getErrorSubCode());
	    				sl.setErrorDesc(result.getDescription());
	    				Status status = new Status();
	    				status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
	    				sl.setStatus(status);
	    				failedSL = sl;
	    				getTechnicalRegistrationDao().saveSalSiteList(sl);
	    				//sendEmailNotice(s1., false, null,ART_EMAIL_ALERT_TYPE_RESPONSE_FAILED, dto.getOptype());
	    			}
	    		}
	    	}
    	}catch (Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting TechnicalRegistrationAsyncService.handleSLResponse for SiteList ID:" + sl.getId());
    	}
    	return failedSL;
    }
	
	private TechnicalRegistration handleTRResponse(TechRegDataResponseDto dto, TechnicalRegistration tr) {
    	logger.debug("Entering TechnicalRegistrationAsyncService.handleTRResponse for TR ID:" + tr.getTechnicalRegistrationId());
    	String returnCode = null;
    	TechnicalRegistration failedTR = null;
    	try {
	    	if(dto != null && tr != null) {

	    		TechRegResultResponseDto result = null;
	    		//initialize solutionElement
	    		SolutionElementListDto solutionElement = new SolutionElementListDto();
	    		// TODO: Shall we care about all exploded SEID records.
	    		if(dto.getSolutionElementList() != null && dto.getSolutionElementList().size() > 0 ) {
	    			solutionElement = dto.getSolutionElementList().get(0);
	    		}
	    		if(dto.getTechRegResult() != null ) {
	    			result = dto.getTechRegResult().get(0);
	    			if(result != null && result.getReturnCode().equals("0")) {//Success Case
	    				logger.debug("TR Id:" + tr.getTechnicalRegistrationId() + " processed successfully.");
	    				//TODO: Shall we store all exploded SEID records.
	    				if(StringUtils.isEmpty(tr.getSolutionElementId()) && StringUtils.isNotEmpty(solutionElement.getNewSEID())) {
	    					tr.setSolutionElementId(solutionElement.getNewSEID());
	    				}
	    				if(StringUtils.isEmpty(tr.getOnboarding()) && StringUtils.isNotEmpty(dto.getOnBoardingXML())) {
	    					tr.setOnboarding(dto.getOnBoardingXML());
	    				}
	    				if(StringUtils.isNotEmpty(dto.getInstallScript())) {
	    					tr.setInstallScript(dto.getInstallScript());
	    				}
	    				if(StringUtils.isEmpty(tr.getAlarmId()) && StringUtils.isNotEmpty(dto.getAlmid())) {
	    					tr.setAlarmId(dto.getAlmid());
	    				}
	    				if(StringUtils.isEmpty(tr.getSid()) && StringUtils.isNotEmpty(dto.getSid())) {
	    					tr.setSid(dto.getSid());
	    				}
	    				if(StringUtils.isEmpty(tr.getMid()) && StringUtils.isNotEmpty(dto.getMid())) {
	    					tr.setMid(dto.getMid());
	    				}
	    				if(StringUtils.isEmpty(tr.getTransactionDetails()) && StringUtils.isNotEmpty(dto.getTransactionDetails())) {
	    					if(dto.getTransactionDetails().length()>4000) {
	    						tr.setTransactionDetails(dto.getTransactionDetails().substring(0, 4000));
	    					} else {
	    						tr.setTransactionDetails(dto.getTransactionDetails());
	    					}
	    				}
	    				tr.setErrorCode(null);
	    				tr.setSubErrorCode(null);
	    				tr.setErrorDesc(null);
	    				Status status = new Status();
	    				status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	    				tr.setStatus(status);
	    				tr.setStepACompletedDate(new Date());
	    				Set<ExpandedSolutionElement> expandedSolutionElements = new HashSet<ExpandedSolutionElement>();
	    				if(!(dto.getOptype().equals(ARTOperationType.PASSWORDRESET.getOperationKey()) ||dto.getOptype().equals(ARTOperationType.REGENONBOARDXML.getOperationKey()))) {
	    					/*if(dto.getOptype().equals(ARTOperationType.ONBOARDXML.getOperationKey()) && dto.getSolutionElementList() != null && dto.getSolutionElementList().get(0) != null && StringUtils.isNotEmpty(dto.getSolutionElementList().get(0).getAlarmId())) {
	    						tr.setAlarmId(dto.getSolutionElementList().get(0).getAlarmId());
	    					}*/
	    					if(dto.getSolutionElementList() != null && dto.getSolutionElementList().size() > 0 ) {
		    					tr.setAlarmId(dto.getSolutionElementList().get(0).getAlarmId());
		    					tr.setProductId(tr.getAlarmId());
	    					}
	    					getTechnicalRegistrationDao().saveTechnicalRegistration(tr);
	    					try {
		    					if(dto.getSolutionElementList() != null && dto.getSolutionElementList().size() > 0 ) {
		    		    			for (SolutionElementListDto solutionElementListDto : dto.getSolutionElementList()) {
		    							if(solutionElementListDto!= null) {
		    								if(StringUtils.isNotEmpty(solutionElementListDto.getNewSEID())) {
		    									ExpandedSolutionElement expandedSolutionElement = new ExpandedSolutionElement();
		    									expandedSolutionElement.setSeID(solutionElementListDto.getNewSEID());
		    									expandedSolutionElement.setSeCode(solutionElementListDto.getSeCode());
		    									expandedSolutionElement.setIpAddress(solutionElementListDto.getIpAddess());
		    									expandedSolutionElement.setAlarmId(solutionElementListDto.getAlarmId());
		    									expandedSolutionElement.setTechnicalRegistration(tr);
		    									//DONE : Implement this method in TechnicalRegistrationSRDao and remove the extra variable defined below
		    									getTechnicalRegistrationAsyncDao().saveOrUpdateDomainObject(expandedSolutionElement);
		    									tr.getExplodedSolutionElements().add(expandedSolutionElement);
		    								}
		    							}
		    						}
		    		    		}
	    					} catch (Throwable throwable) {
								logger.error("Errow while iterating on ExpandedSolutionElements", throwable);
							}
	    				}
	    				
	    				if(dto.getOptype().equalsIgnoreCase(ARTOperationType.ONBOARDXML.getOperationKey()) || dto.getOptype().equalsIgnoreCase(ARTOperationType.REGENONBOARDXML.getOperationKey()) || dto.getOptype().equalsIgnoreCase(ARTOperationType.PASSWORDRESET.getOperationKey())) {
	    					sendEmailNotice(tr, true, dto.getOnBoardingXML(),ART_EMAIL_ALERT_TYPE_COMPLETED, dto.getOptype());
	    				} else {
	    					sendEmailNoticeForTOBLine(tr, null, tr.getTechnicalOrder().getSiteRegistration(), StatusEnum.COMPLETED);
	    				}
	    				if(tr.isCmProduct() && !tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_SAL)
	    						&& (tr.isCmMain() || (!tr.isCmMain() && tr.getRemoteDeviceType().equalsIgnoreCase(GRTConstants.REMOTE_DEVICE_TYPE_LSP)))) {
	    					logger.debug("Initiating CM Polling for TR Id:" + tr.getTechnicalRegistrationId());
	    					try {
	    						if(tr.getEpnSurveyStatus() == null) {
	    							returnCode = getArtClient().initiateCMPolling(tr, getPIEPollableData(tr));
	    							if (returnCode == GRTConstants.fmw_errorCode || returnCode == GRTConstants.acsbi_errorCode) {
	    								String errMsg = null;
	    								if (returnCode == GRTConstants.fmw_errorCode) {
	    									errMsg = "Failed to connect to FMW for ACSBI Polling";
		    							} else {
		    								errMsg = "Failed to connect to ACSBI for Polling";
	    								}
	    								String regId = dto.getRegistrationId();
	    								sendEmailToSystemAdmin(regId, errMsg, ProcessStepEnum.TECHNICAL_REGISTRATION);
	    				        	}
	    							//DONE : Implement this method in TechnicalRegistrationSRDao and remove the extra variable defined below
			    					getTechnicalRegistrationAsyncDao().updateTechnicalRegistrationEPNSurveyStatus(tr, StatusEnum.INPROCESS);
			    				}
	    					} catch(Throwable throwable) {
	    						//TODO: Design GAP:
	    						logger.error(throwable);
	    					}
	    				}
	    			} else { //Failure case
	    				logger.debug("TR Id:" + tr.getTechnicalRegistrationId() + " failed with errorCode:" + result.getReturnCode() + "." + " errorDesc:" + result.getDescription());
	    				tr.setErrorCode(result.getReturnCode());
	    				tr.setSubErrorCode(result.getErrorSubCode());
	    				tr.setErrorDesc(result.getDescription());
	    				if(StringUtils.isNotEmpty(dto.getFailedSeid())) {
	    					logger.debug("Got FailedSEID:" + dto.getFailedSeid());
	    					tr.setFailedSeid(dto.getFailedSeid());
	    				}
	    				//TODO: Shall we store all exploded SEID records.
	    				//Check solutionElement is null
	    				if(StringUtils.isEmpty(tr.getSolutionElementId()) && solutionElement!=null && StringUtils.isNotEmpty(solutionElement.getNewSEID())) {
	    					tr.setSolutionElementId(solutionElement.getNewSEID());
	    				}
	    				try {
	    					if(dto.getSolutionElementList() != null && dto.getSolutionElementList().size() > 0 ) {
	    		    			for (SolutionElementListDto solutionElementListDto : dto.getSolutionElementList()) {
	    							if(solutionElementListDto!= null) {
	    								if(StringUtils.isNotEmpty(solutionElementListDto.getNewSEID())) {
	    									ExpandedSolutionElement expandedSolutionElement = new ExpandedSolutionElement();
	    									expandedSolutionElement.setSeID(solutionElementListDto.getNewSEID());
	    									expandedSolutionElement.setSeCode(solutionElementListDto.getSeCode());
	    									expandedSolutionElement.setIpAddress(solutionElementListDto.getIpAddess());
	    									expandedSolutionElement.setAlarmId(solutionElementListDto.getAlarmId());
	    									expandedSolutionElement.setTechnicalRegistration(tr);
	    									//DONE : Implement this method in TechnicalRegistrationSRDao and remove the extra variable defined below
	    									getTechnicalRegistrationAsyncDao().saveOrUpdateDomainObject(expandedSolutionElement);
	    									tr.getExplodedSolutionElements().add(expandedSolutionElement);
	    								}
	    							}
	    						}
	    		    		}
    					} catch (Throwable throwable) {
							logger.error("Errow while iterating on ExpandedSolutionElements", throwable);
						}
	    				
	    				tr.setArtId(result.getArtid());
	    				logger.debug("Failed SID:" + dto.getSid());
	    				if(StringUtils.isNotEmpty(dto.getSid())) {
	    					tr.setSid(dto.getSid());
	    				}
	    				logger.debug("Failed MID:" + dto.getMid());
	    				if(StringUtils.isNotEmpty(dto.getMid())) {
	    					tr.setMid(dto.getMid());
	    				}
	    				Status status = new Status();
	    				status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
	    				tr.setStatus(status);
	    				failedTR =tr;
	    				/*this.getRegistrationDao().saveTechnicalRegistration(tr);
	    				//TODO: Relook at the sendMail API
	    				ARTAsycnResponseHandler asycnResponseHandler = new ARTAsycnResponseHandler();
	    				asycnResponseHandler.sendEmailNotice(tr, false, null,ARTAsycnResponseHandler.ART_EMAIL_ALERT_TYPE_RESPONSE_FAILED, dto.getOptype());*/
	    			}
	    		}
	    	}
    	} catch(Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting TechnicalRegistrationAsyncService.handleTRResponse for TR ID:" + tr.getTechnicalRegistrationId());
    	}
    	return failedTR;
    }

	public TechnicalRegistrationAsyncDao getTechnicalRegistrationAsyncDao() {
		return technicalRegistrationAsyncDao;
	}

	public void setTechnicalRegistrationAsyncDao(
			TechnicalRegistrationAsyncDao technicalRegistrationAsyncDao) {
		this.technicalRegistrationAsyncDao = technicalRegistrationAsyncDao;
	}
	
	
	
}
