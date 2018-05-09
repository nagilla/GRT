package com.grt.util;

import java.util.Set;






import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.RegistrationRequestAlert;
import com.grt.util.AccessTypeEnum;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public class RegistrationRequestAlertConvertor {
	private static final Logger logger = Logger.getLogger(RegistrationRequestAlertConvertor.class);
	
	public static RegistrationRequestAlert convert(SiteRegistration siteRegistration, ProcessStepEnum targetProcessStep) {
		logger.debug("Entering RegistrationRequestAlertConvertor : convert SiteRegistration");		
		RegistrationRequestAlert result = new RegistrationRequestAlert();
		try {		
			result.setRegistrationId(siteRegistration.getRegistrationId());
			result.setSoldTo(siteRegistration.getSoldToId());
			/*String processStepId = siteRegistration.getProcessStep().getProcessStepId(); 
			String processStep = siteRegistration.getProcessStep().getProcessStepShortDescription();
			String status = siteRegistration.getStatus().getStatusShortDescription();
			String statusId = siteRegistration.getStatus().getStatusId();
			if(siteRegistration.getInstallBaseStatus()!= null && StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusId()) && 
					!siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
						processStepId = ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId();
						processStep = ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepShortDescription();
						status = siteRegistration.getInstallBaseStatus().getStatusShortDescription();
						statusId = siteRegistration.getInstallBaseStatus().getStatusId();
			} else if(siteRegistration.getTechRegStatus() !=null && StringUtils.isNotEmpty(siteRegistration.getTechRegStatus().getStatusId()) && 
					!siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId()) 
					&& !siteRegistration.getTechRegStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
				processStepId = ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId();
				processStep = ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription();
				status = siteRegistration.getTechRegStatus().getStatusShortDescription();
				statusId = siteRegistration.getTechRegStatus().getStatusId();				
			}else if(siteRegistration.getFinalValidationStatus() !=null  && StringUtils.isNotEmpty(siteRegistration.getFinalValidationStatus().getStatusId()) && 
					!siteRegistration.getFinalValidationStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId()) 
					&& !siteRegistration.getFinalValidationStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
				processStepId = ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId();
				processStep = ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepShortDescription();
				status = siteRegistration.getFinalValidationStatus().getStatusShortDescription();
				statusId = siteRegistration.getFinalValidationStatus().getStatusId();				
			}*/
			
			String processStepId = targetProcessStep.getProcessStepId(); 
			String processStep = targetProcessStep.getProcessStepShortDescription();
			String status = siteRegistration.getStatus().getStatusShortDescription();
			String statusId = siteRegistration.getStatus().getStatusId();
			if(targetProcessStep.equals(ProcessStepEnum.INSTALL_BASE_CREATION)) {
						status = siteRegistration.getInstallBaseStatus().getStatusShortDescription();
						statusId = siteRegistration.getInstallBaseStatus().getStatusId();
			} else if(targetProcessStep.equals(ProcessStepEnum.TECHNICAL_REGISTRATION)) {
				if(siteRegistration.isStepBInProcessAction()) {
					status = StatusEnum.INPROCESS.getStatusShortDescription();
					statusId = StatusEnum.INPROCESS.getStatusId();
					result.setStepB(true);
				} else if(siteRegistration.isStepBCompletedAction()) {
					status = StatusEnum.COMPLETED.getStatusShortDescription();
					statusId = StatusEnum.COMPLETED.getStatusId();
					result.setStepB(true);
				} else {
					status = siteRegistration.getTechRegStatus().getStatusShortDescription();
					statusId = siteRegistration.getTechRegStatus().getStatusId();
				}
			} else if(targetProcessStep.equals(ProcessStepEnum.FINAL_RECORD_VALIDATION)) {
				status = siteRegistration.getFinalValidationStatus().getStatusShortDescription();
				statusId = siteRegistration.getFinalValidationStatus().getStatusId();				
			} else if(targetProcessStep.equals(ProcessStepEnum.EQUIPMENT_MOVE)) {
				status = siteRegistration.getEqrMoveStatus().getStatusShortDescription();
				statusId = siteRegistration.getEqrMoveStatus().getStatusId();				
			}
			/* GRT 4.0 : change for sending mail for Equipment Move */
			
			logger.debug("****************In convert for mail**************************************");
			logger.debug("Data process step and process stepID"+processStep+processStepId+"Status and status ID"+status+statusId);
			logger.debug("Data for reg ID "+result.getRegistrationId());
			logger.debug("****************End convert for mail**************************************");
			result.setProcessStep(processStep);
			result.setProcessStepId(processStepId);
			result.setStatusId(statusId);
			result.setStatus(status);
			if("Y".equalsIgnoreCase(siteRegistration.getSendMail())){
				result.setSendMail(true);
			}
			SRRequest srRequest = null;
			if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId()
					.equalsIgnoreCase(processStepId)) {
				srRequest = siteRegistration.getInstallBaseSrRequest();
				result.setCompletedDate(siteRegistration.getIbCompletedDate());
			} 
			else if(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId()
						.equalsIgnoreCase(processStepId)) {
				//To-do Get the ART Sr No from technical registration.
				result.setCompletedDate(siteRegistration.getTobCompletedDate());
			}
			else if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId()
						.equalsIgnoreCase(processStepId)) {
				srRequest = siteRegistration.getFinalValidationSrRequest();
				result.setCompletedDate(siteRegistration.getEqrCompletedDate());
			}
			/* GRT 4.0 : change for sending mail for Equipment Move */
			else if(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId()
					.equalsIgnoreCase(processStepId)) {
				srRequest = siteRegistration.getEqrMoveSrRequest();
				result.setCompletedDate(siteRegistration.getEqrMoveCompletedDate());
				//TODO
				result.setToCustomerName(siteRegistration.getToCustomerName());				
				result.setToCustomerSoldTo(siteRegistration.getToSoldToId());
			}
			/*if (StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusId()) && 
					!(siteRegistration.getInstallBaseStatus().getStatusId()).equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId())){
				srRequest = siteRegistration.getInstallBaseSrRequest();
			} else if (StringUtils.isNotEmpty(siteRegistration.getFinalValidationStatus().getStatusId()) &&
					!(siteRegistration.getFinalValidationStatus().getStatusId()).equalsIgnoreCase(StatusEnum.NOTINITIATED.getStatusId())){
				srRequest = siteRegistration.getFinalValidationSrRequest();
			}*/
			
			result.setSiebelSRNumber(getSiebelSRNo(srRequest));
			result.setReportedDate(siteRegistration.getCreatedDate());
			/*result.setCompletedDate(siteRegistration.getUpdatedDate());*/
		
			if(siteRegistration.getRegistrationType() != null) {
				result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());
			}
		
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationIdentifier())) {
				result.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
			} else {
				result.setRegistrationIdentifier("");
			}
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationNotes())) {
				result.setNotes(siteRegistration.getRegistrationNotes());
			} else {
				result.setNotes("");
			}
			String fullName = "";
			if(siteRegistration.getFirstName() != null) {
				fullName += siteRegistration.getFirstName();
			}
			if(siteRegistration.getLastName() != null) {
				fullName += " " + siteRegistration.getLastName();
			}
			result.setFullName(fullName);
			result.setRequestorEmail(siteRegistration.getOnsiteEmail());
			result.setCustomerName(siteRegistration.getCompany());
			
			if(siteRegistration.isTOBSRCreated()) {
				result.setTOBSRCreated(true);
			}
			
			logger.debug("Exiting RegistrationRequestAlertConvertor : convert SiteRegistration");
		} catch (Exception ex) {
			logger.debug("Error in RegistrationRequestAlertConvertor : convert SiteRegistration : " + ex.getMessage());
		}
		return result;
	}

	/**
	 * Method to get the Siebel SR Number.
	 * 
	 * @param srRequest SRRequest
	 * @return siebelSRNo String
	 */
	private static String getSiebelSRNo(SRRequest srRequest) {
		logger.debug("Entering RegistrationRequestAlertConvertor : getSiebelSRNo");
		if(srRequest == null || StringUtils.isEmpty(srRequest.getSiebelSRNo())) {
			logger.debug("Exiting RegistrationRequestAlertConvertor : getSiebelSRNo - SR Number Not Found");
			return GRTConstants.SIEBEL_SR_NUMBER_NOT_FOUND;
		}
		else {
			logger.debug("Exiting RegistrationRequestAlertConvertor : getSiebelSRNo - SR Number Found");
			return srRequest.getSiebelSRNo();
		}
	}
	
	
	public static RegistrationRequestAlert convertForTOBLineItem(TechnicalRegistration technicalregistration, SiteList siteList, SiteRegistration siteRegistration, StatusEnum status) {
		logger.debug("Entering convertForTOBLineItem");		
		RegistrationRequestAlert result = new RegistrationRequestAlert();
		try {		
			result.setRegistrationId(siteRegistration.getRegistrationId());
			result.setSoldTo(siteRegistration.getSoldToId());
			result.setProcessStep(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription());
			result.setProcessStepId(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId());
			if(status != null) {
				result.setStatusId(status.getStatusId());
				result.setStatus(status.getStatusShortDescription());
			} else if(technicalregistration != null ){
				result.setStatusId(technicalregistration.getStatus().getStatusId());
				result.setStatus(technicalregistration.getStatus().getStatusShortDescription());
			} else if(siteList != null ){
				result.setStatusId(siteList.getStatus().getStatusId());
				result.setStatus(siteList.getStatus().getStatusShortDescription());
			}
			if("Y".equalsIgnoreCase(siteRegistration.getSendMail())){
				result.setSendMail(true);
			}

			if(technicalregistration != null && technicalregistration.getSrRequest() != null){
				result.setSiebelSRNumber(getSiebelSRNo(technicalregistration.getSrRequest()));
			} else if(siteList != null && siteList.getSrRequest() != null){
				result.setSiebelSRNumber(getSiebelSRNo(siteList.getSrRequest()));
			}
			//UAT Defect : Send SR number for Step B submission
			else if(technicalregistration != null && technicalregistration.getStepBSRRequest() != null){
				result.setSiebelSRNumber(getSiebelSRNo(technicalregistration.getStepBSRRequest()));
				//UAT Defect : 782
				result.setSecondAttempt(true);
				result.setStepB(true);
			}
			else if(siteList != null && siteList.getStepBSRRequest() != null){
				result.setSiebelSRNumber(getSiebelSRNo(siteList.getStepBSRRequest()));
				//UAT Defect : 782
				result.setSecondAttempt(true);
				//result.setStepB(true);
			}
			
			result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());
			result.setReportedDate(siteRegistration.getCreatedDate());
			result.setCompletedDate(siteRegistration.getUpdatedDate());
		
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationIdentifier())) {
				result.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
			}
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationNotes())) {
				result.setNotes(siteRegistration.getRegistrationNotes());
			} else {
				result.setNotes(" ");
			}
			String fullName = "";
			if(siteRegistration.getFirstName() != null) {
				fullName += siteRegistration.getFirstName();
			}
			if(siteRegistration.getLastName() != null) {
				fullName += " " + siteRegistration.getLastName();
			}
			result.setFullName(fullName);
			result.setRequestorEmail(siteRegistration.getOnsiteEmail());
			result.setCustomerName(siteRegistration.getCompany());
			if(status.getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId())) {
				if(technicalregistration != null) {
					if(technicalregistration.getNumberOfSubmitAsInt() > 1) {
						result.setSecondAttempt(true);
						result.setErrorDescription(technicalregistration.getErrorDesc());
					}
						
				} else if(siteList != null) {
					if(siteList.getNumberOfSubmitAsInt() > 1) {
						result.setSecondAttempt(true);
						result.setErrorDescription(siteList.getErrorDesc());
					}
				}
			}
			String dynamicData = "";
			if(siteList != null) {
				if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
					dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(siteList.getMaterialCode(), siteList.getSeCode(), result.getSiebelSRNumber());
				} else {
					dynamicData = DynamicMailContentRenderer.getDynamicDataForSALMigration(siteList.getMaterialCode(), siteList.getFullGatewaySeid(), siteList.getSeCode(), siteList.getSolutionElementId(), siteList.getInstallScript());
				}
			} else if(technicalregistration != null) {

				if(GRTConstants.TECH_ORDER_TYPE_TR_UPDATE.equalsIgnoreCase(technicalregistration.getTechnicalOrder().getOrderType())) {
					result.setTobUpdate(true);
					if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), result.getSiebelSRNumber());
					} else {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForTobUpdate(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), technicalregistration.getSolutionElementId(), technicalregistration.getInstallScript());
					}
				} else if(AccessTypeEnum.IP.getDbAccessType().equalsIgnoreCase(technicalregistration.getAccessType())) {
					if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), result.getSiebelSRNumber());
					} else { 
						dynamicData = DynamicMailContentRenderer.getDynamicDataForIP(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getIpAddress(), technicalregistration.getSid(), technicalregistration.getMid(), technicalregistration.getSolutionElement(), technicalregistration.getSolutionElementId(), technicalregistration.getExplodedSolutionElements(), technicalregistration.getInstallScript());
					}
				} else if(AccessTypeEnum.MODEM.getDbAccessType().equalsIgnoreCase(technicalregistration.getAccessType())) {
					if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), result.getSiebelSRNumber());
					} else { 
						String dialIn = (StringUtils.isNotEmpty(technicalregistration.getOutboundCallingPrefix()))?technicalregistration.getOutboundCallingPrefix()+technicalregistration.getDialInNumber(): "" + technicalregistration.getDialInNumber();
						dynamicData = DynamicMailContentRenderer.getDynamicDataForModem(technicalregistration.getTechnicalOrder().getMaterialCode(), dialIn, technicalregistration.getSid(), technicalregistration.getMid(), technicalregistration.getSolutionElement(), technicalregistration.getSolutionElementId(), technicalregistration.getExplodedSolutionElements(), technicalregistration.getInstallScript());
					}
				} else if(AccessTypeEnum.SAL.getDbAccessType().equalsIgnoreCase(technicalregistration.getAccessType())) {
					result.setSAL(true);
					if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), result.getSiebelSRNumber());
					} else { 
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSALRecordBuilding(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getFullGatewaySeid(), technicalregistration.getSid(), technicalregistration.getMid(), technicalregistration.getSolutionElement(), technicalregistration.getSolutionElementId(), technicalregistration.getExplodedSolutionElements(), technicalregistration.getInstallScript());
					}
				}else if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(technicalregistration.getAccessType())|| " ".equalsIgnoreCase(technicalregistration.getAccessType()) || technicalregistration.getAccessType()==null) {
					if(StringUtils.isNotEmpty(result.getSiebelSRNumber())) {
						dynamicData = DynamicMailContentRenderer.getDynamicDataForSRClosure(technicalregistration.getTechnicalOrder().getMaterialCode(), technicalregistration.getSolutionElement(), result.getSiebelSRNumber());
					} else { 
						String dialIn = (StringUtils.isNotEmpty(technicalregistration.getOutboundCallingPrefix()))?technicalregistration.getOutboundCallingPrefix()+technicalregistration.getDialInNumber(): "" + technicalregistration.getDialInNumber();
						dynamicData = DynamicMailContentRenderer.getDynamicDataForNoConnectivity(technicalregistration.getTechnicalOrder().getMaterialCode(), dialIn, technicalregistration.getSid(), technicalregistration.getMid(), technicalregistration.getSolutionElement(), technicalregistration.getSolutionElementId(), technicalregistration.getExplodedSolutionElements(), technicalregistration.getInstallScript());
					}
				} 
			}
			result.setDynamicData(dynamicData);
 		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting convertForTOBLineItem");	
		}
		return result;
	}
	
	public static RegistrationRequestAlert convertTechRegIPO(SiteRegistration siteRegistration, TechnicalRegistration technicalRegistration, 
			String operationType, String serviceName) {
		logger.debug("Entering RegistrationRequestAlertConvertor : convert SiteRegistration");		
		RegistrationRequestAlert result = new RegistrationRequestAlert();
		try {		
			result.setRegistrationId(siteRegistration.getRegistrationId());
			result.setSoldTo(siteRegistration.getSoldToId());
			result.setProcessStep(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription());
			String processStepId = ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId();
			result.setProcessStepId(processStepId);

			if("Y".equalsIgnoreCase(siteRegistration.getSendMail())){
				result.setSendMail(true);
			}
			SRRequest srRequest = null;
			String dynamicData = DynamicMailContentRenderer.getDynamicDataForSSLVPN(technicalRegistration.getTechnicalOrder().getMaterialCode(), technicalRegistration.getTechnicalOrder().getSerialNumber(), serviceName, technicalRegistration.getAlarmId(), technicalRegistration.getSolutionElementId());
			if(StringUtils.isNotEmpty(dynamicData)) { 
				result.setDynamicData(dynamicData);
			}
			
			result.setSiebelSRNumber(getSiebelSRNo(srRequest));
			result.setReportedDate(siteRegistration.getCreatedDate());
			result.setCompletedDate(siteRegistration.getUpdatedDate());
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationIdentifier())) {
				result.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
			}
			if(StringUtils.isNotEmpty(siteRegistration.getRegistrationNotes())) {
				result.setNotes(siteRegistration.getRegistrationNotes());
			}
			/*if(StringUtils.isNotEmpty(technicalRegistration.getErrorDesc())) {
				result.setErrorDescription(technicalRegistration.getErrorDesc());
			}*/
			String fullName = "";
			if(siteRegistration.getFirstName() != null) {
				fullName += siteRegistration.getFirstName();
			}
			if(siteRegistration.getLastName() != null) {
				fullName += " " + siteRegistration.getLastName();
			}
			result.setFullName(fullName);
			if(StringUtils.isNotEmpty(technicalRegistration.getIpoUserEmail())){
				result.setRequestorEmail(technicalRegistration.getIpoUserEmail());
			} else{
				result.setRequestorEmail(siteRegistration.getOnsiteEmail());
			}
			result.setCustomerName(siteRegistration.getCompany());
			if(siteRegistration.getTechRegStatus() != null){
				if((StringUtils.isNotEmpty(technicalRegistration.getErrorCode()) && !"0".equals(technicalRegistration.getErrorCode())) && StringUtils.isNotEmpty(operationType)&& (operationType.equals("PR") || operationType.equals("RN"))){					
					result.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
					result.setStatus(StatusEnum.AWAITINGINFO.getStatusShortDescription());
				}				
				else{
					/*result.setStatusId(siteRegistration.getTechRegStatus().getStatusId());
					result.setStatus(siteRegistration.getTechRegStatus().getStatusShortDescription());*/
					result.setStatusId(StatusEnum.COMPLETED.getStatusId());
					result.setStatus(StatusEnum.COMPLETED.getStatusShortDescription());
				}
			}
			logger.debug("Exiting RegistrationRequestAlertConvertor : convert SiteRegistration");
		} catch (Throwable throwable) {
			logger.error("", throwable);
		}
		return result;
	}
	
	public static RegistrationRequestAlert convert(TechnicalRegistration technicalRegistration) {
		SiteRegistration siteRegistration = technicalRegistration.getTechnicalOrder().getSiteRegistration();
		RegistrationRequestAlert result = convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);
		String artSrNo = technicalRegistration.getArtSrNo();
		if(artSrNo == null || StringUtils.isEmpty(artSrNo)) {
			artSrNo = GRTConstants.SIEBEL_SR_NUMBER_NOT_FOUND;
		}
		if(siteRegistration.isTOBSRCreated()) {
			result.setTOBSRCreated(true);
		}
		result.setSiebelSRNumber(artSrNo);
		return result;
	}
	
}
