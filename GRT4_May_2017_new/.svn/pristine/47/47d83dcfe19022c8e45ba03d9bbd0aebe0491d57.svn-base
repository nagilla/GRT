package com.grt.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.grt.dto.Contract;
import com.grt.dto.RegistrationRequestAlert;
import com.grt.dto.ServiceDetails;
import com.grt.dto.TokenDetails;
import com.grt.dto.TokenRedemptionEmailDto;
import com.grt.dto.TokenResponseTypeDto;
import com.avaya.v1.address.AddressType;
import com.avaya.v1.tokenredemption.TokenRedemptionRequestType;
/**
 * Mail Utility
 *
 * @author Perficient
 *
 */
public class MailUtil {
    private final Logger logger = Logger.getLogger(MailUtil.class);


    private static final String REGISTRATION_ID_KEY = "registrationID";
    private static final String SIEBEL_SRNUMBER_KEY = "siebelSRNumber";
    private static final String DATE_REPORTED_KEY = "dateReported";
    private static final String DATE_COMPLETED_KEY = "dateCompleted";
    private static final String REQUESTOR_NAME_KEY = "requestorName";
    private static final String REQUESTOR_EMAIL_KEY = "requestorEmail";
    private static final String CUSTOMER_NAME_KEY = "customerName";
    private static final String ACTION_REQUIRED_KEY = "actionRequired";
    private static final String PROCESS_STEP_KEY = "processStep";
    private static final String STATUS_KEY = "status";
    private static final String SUBJECT_KEY = "subject";
    private static final String DYNAMICDATA_KEY = "dynamicData";
    private static final String DESTINATION = "destination";
    private static final String SOLUTION_ELEMENT_CODE = "SECode";
    private static final String TECHNICAL_REGISTRATION_ID = "technicalRegID";
    private static final String ONBOARDING_XML_FILE_NAME = "IPO-Onboarding.xml";
    private static final String NOT_AVAILABLE = "N/A";
    private static final String MATERIAL_CODE = "materialCode";
    private static final String MATERIAL_CODE_DESC = "materialCodeDescription";
    private static final String ALARM_ID = "alarmId";
    private static final String SOLD_TO = "soldTo";
    private static final String NOTES = "notes";
    private static final String REGISTRATION_NAME = "registrationName";
    private static final String ERROR_DESC = "errorDescription";
    private static final String ACSBI_EPN_Survey_URL = "ACSBIEPNSurveyURL";
    private static final String END_CUSTOMER_SHIP_TO_ID_KEY = "endCustomerShipToId";
    private static final String END_CUSTOMER_NAME_KEY = "customerName";
    private static final String END_CUSTOMER_ADDRESS_KEY = "customerAddress";
    private static final String END_CUSTOMER_CONTACT_KEY = "customerContact";
    private static final String END_CUSTOMER_CITY_KEY = "customerCity";
    private static final String END_CUSTOMER_CITY_STATE_KEY = "customerState";
    private static final String END_CUSTOMER_CITY_POSTALCODE_KEY = "customerPostalCode";
    private static final String END_CUSTOMER_CITY_COUNTRY_KEY = "customerCountry";
    private static final String CONTRACT_NUMBER_KEY = "contractNumber";
    private static final String CONTRACT_START_DATE_KEY = "contractStartDate";
    private static final String TOKEN_NUMBER_KEY = "TokenNumber";
    private static final String DATE_REDEEMED_KEY = "DateRedeemed";
    private static final String RESELLER_NAME_KEY = "ResellerName";
    private static final String BP_LINK_ID_KEY = "bpLinkId";
    private static final String REDEEMER_NAME_KEY = "redeemerName";
    private static final String REDEEMER_CONTACT_KEY = "redeemerContact";
    private static final String TOKEN_SERVICE_DESCRIPTION_KEY = "tokenServiceDescription";
    private static final String TOKEN_HARDWARE_DETAILS_KEY = "tokenHardwareDetails";
    private static final String ADDITIONAL_INFO_KEY = "additionalInfo";
    private static final String ACSBI_EPN_Survey_Pollable_Secode_Seid = "ACSBIEPNSURVEYPOLLABLESECODESEID";
    private static final String LOGO_NAME_KEY = "logo";
    private static final String DATE_CREATED_KEY = "dateCreated";
    private static final String ACCOUNT_CREATION_REASON_KEY = "accountCreateReason";
    private static final String ACCOUNT_CREATED_BY_KEY = "accountCreatedBy";
    private static final String ACCOUNT_CREATED_BY_CONTACT_KEY = "accountCreatedByContact";
    
    private static final String CANCEL_CONTRACT_NUMBER_KEY = "cancelContractNumber";
    private static final String CANCEL_CONTRACT_START_DATE_KEY = "cancelContractStartDate";
    private static final String CANCEL_CONTRACT_END_DATE_KEY = "cancelContractEndDate";
    
    //EQM 
    private static final String TO_CUSTOMER_SOLD_TO_KEY = "toCustomerSoldTo";
    private static final String TO_CUSTOMER_NAME_KEY = "toCustomerName";
    
    private JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
    private String sendFrom;
    private Resource logoResource;
    private String awaitingInfoTemplate;
    private String completedTemplate;
    private String inProcessTemplate;
    private String systemTemplate;
    private String mailSendToSystemList;
    private String ipoTemplate;
    private String onBoardingTemplate;
    private String cmPollingCompletionTemplate;
    private String inProcessIbEqrTemplate;
    private String inProcessSALMigrationTemplate;
    private String awaitingInfoIbEqrTemplate;
    private String validatedInfoRecordValidationTemplate;
    private String completedIbEqrTemplate;
    private String awaitingInfoTobFirstAttemptTemplate;
    private String awaitingInfoTobSecondAttemptTemplate;
    private String completedTobTemplate;
    private String completedSALMigrationTemplate;
    private String completedSALStepBTemplate;
    private String completedEqrTemplate;
    private String stepBSRCancellation;    
	private String tokenRedemptionOefcTemplate;
    private String tokenRedemptionOefcEmailList;
    private String duplicateAccountCMDTemplate;
    private String duplicateAccountCreationEmailList;
    private String accountCreationTemplate;
    private String acsbiEpnSurveyUrl;
    
    private String inProcessEqmTemplate;
    private String completedEqmTemplate;
    //GRt 4.0 : Added for alarming 
    private String awaitingInfoTemplateForAlarm;
    
    private static final String IPO_FILE_PATH = "ipoFilePath";
    
    public static MailUtil getInstance1() {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    	MailUtil mailUtil = (MailUtil) applicationContext.getBean("mailUtil");
        return mailUtil;
    }

    public static void main(String[] args) {
    	MailUtil mailUtil = MailUtil.getInstance1();
    	
        RegistrationRequestAlert rra = new RegistrationRequestAlert();
        rra.setRegistrationId("1192923196");
        rra.setSiebelSRNumber("1-527160621");
        rra.setReportedDate(new Date());
        rra.setCompletedDate(new Date());
        rra.setFullName("Darrell Wunderlich");
        rra.setRequestorEmail("thomas59@avaya.com");
        rra.setCustomerName("Avaya Registered Product");
        rra.setProcessStepId("P0001");
        rra.setProcessStep(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepShortDescription());
        rra.setStatusId("1003");
        rra.setStatus(StatusEnum.INPROCESS.getStatusShortDescription());        
        rra.setActionRequired("Please update quantity for material code 123456 for install base creation and resubmit the registration request.");
        rra.setSiebelSRStatus(GRTConstants.SIEBEL_SR_STATUS_IN_PROCESS);
        rra.setSendMail(true);
        
        mailUtil.sendMailNotification(rra);
        
        
        
        /*
        rra.setStatusId("1004");
        rra.setStatus("Completed");
        rra.setSiebelSRStatus(GRTConstants.SIEBEL_SR_STATUS_COMPLETED);
        rra.setSalStatus(GRTConstants.COMPLETED_DESCRIPTION);
        rra.setIsSystemMail(true);
        rra.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_SIEBEL);
        MailUtil.getInstance().sendMailNotification(rra);
        */
    }


    public void sendMailNotification(RegistrationRequestAlert rra) {
    	logger.debug("Entering MailUtil : sendMailNotification - Step 1");
    	try {
			Map<String, Object> mailItems = new HashMap<String, Object>();

			// Compose body details:
			mailItems.put(REGISTRATION_ID_KEY, rra.getRegistrationId());
			mailItems.put(PROCESS_STEP_KEY, rra.getProcessStep());
			mailItems.put(STATUS_KEY, rra.getStatus());

			if (rra.getSiebelSRNumber() != null
					&& rra.getSiebelSRNumber().trim().length() > 0) {
				mailItems.put(SIEBEL_SRNUMBER_KEY, rra.getSiebelSRNumber());
			} else {
				mailItems.put(SIEBEL_SRNUMBER_KEY, NOT_AVAILABLE);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if (rra.getReportedDate() != null) {
				mailItems.put(DATE_REPORTED_KEY, sdf.format(rra
						.getReportedDate()));
			}
			if (rra.getCompletedDate() != null) {
				mailItems.put(DATE_COMPLETED_KEY, sdf.format(rra
						.getCompletedDate()));
			} else {
				mailItems.put(DATE_COMPLETED_KEY, NOT_AVAILABLE);
			}
			//EQM
			
			if(StringUtils.isNotEmpty(rra.getToCustomerName())){
				mailItems.put(TO_CUSTOMER_NAME_KEY, rra.getToCustomerName());
			} else {
				mailItems.put(TO_CUSTOMER_NAME_KEY, "");
			}			
			if(StringUtils.isNotEmpty(rra.getToCustomerSoldTo())){
				mailItems.put(TO_CUSTOMER_SOLD_TO_KEY, rra.getToCustomerSoldTo());
			} else {
				mailItems.put(TO_CUSTOMER_SOLD_TO_KEY, "");
			}
			//EQM End

			mailItems.put(REQUESTOR_NAME_KEY, rra.getFullName());
			mailItems.put(REQUESTOR_EMAIL_KEY, rra.getRequestorEmail());
			mailItems.put(ACTION_REQUIRED_KEY, rra.getActionRequired());
			mailItems.put(DESTINATION, rra.getDestination());
			if(StringUtils.isNotEmpty(rra.getNotes())){
				mailItems.put(NOTES, rra.getNotes());
			}
			else{
				mailItems.put(NOTES, " ");
			}
			if(StringUtils.isNotEmpty(rra.getRegistrationIdentifier())){
				mailItems.put(REGISTRATION_NAME, rra.getRegistrationIdentifier());
			}
			else{
				mailItems.put(REGISTRATION_NAME, " ");
			}
			if(StringUtils.isNotEmpty(rra.getErrorDescription())){
				mailItems.put(ERROR_DESC, rra.getErrorDescription());
			} else {
				mailItems.put(ERROR_DESC, " ");
			}
			if (rra.getCustomerName() != null
					&& rra.getCustomerName().trim().length() > 0) {
				mailItems.put(CUSTOMER_NAME_KEY, rra.getCustomerName());
			} else {
				mailItems.put(CUSTOMER_NAME_KEY, NOT_AVAILABLE);
			}

			if (rra.getSoldTo() != null
					&& rra.getSoldTo().trim().length() > 0) {
				mailItems.put(SOLD_TO, rra.getSoldTo());
			} else {
				mailItems.put(SOLD_TO, NOT_AVAILABLE);
			}

			if (rra.getSeCode() != null && rra.getSeCode().trim().length() > 0) {
				mailItems.put(SOLUTION_ELEMENT_CODE, rra.getSeCode());
			} else {
				mailItems.put(SOLUTION_ELEMENT_CODE, NOT_AVAILABLE);
			}

			if (rra.getTechnicalRegistrationId() != null
					&& rra.getTechnicalRegistrationId().trim().length() > 0) {
				mailItems.put(TECHNICAL_REGISTRATION_ID, rra
						.getTechnicalRegistrationId());
			} else {
				mailItems.put(TECHNICAL_REGISTRATION_ID, NOT_AVAILABLE);
			}

			if (rra.getIpoFilePath() != null
					&& rra.getIpoFilePath().trim().length() > 0) {
				mailItems.put(IPO_FILE_PATH, rra.getIpoFilePath());
			} else {
				mailItems.put(IPO_FILE_PATH, NOT_AVAILABLE);
			}

			if (rra.getMaterialCode() != null
					&& rra.getMaterialCode().trim().length() > 0) {
				mailItems.put(MATERIAL_CODE, rra.getMaterialCode());
			} else {
				mailItems.put(MATERIAL_CODE, NOT_AVAILABLE);
			}

			if (rra.getMaterialCodeDescription() != null
					&& rra.getMaterialCodeDescription().trim().length() > 0) {
				mailItems.put(MATERIAL_CODE_DESC, rra.getMaterialCodeDescription());
			} else {
				mailItems.put(MATERIAL_CODE_DESC, NOT_AVAILABLE);
			}

			if (rra.getAlarmId() != null
					&& rra.getAlarmId().trim().length() > 0) {
				mailItems.put(ALARM_ID, rra.getAlarmId());
			} else {
				mailItems.put(ALARM_ID, NOT_AVAILABLE);
			}

			if(rra.isEpnSurveyCompletionNotification()) {
				String acsbi_epn_survey_url = getAcsbiEpnSurveyUrl();
				if(StringUtils.isNotEmpty(acsbi_epn_survey_url) && StringUtils.isNotEmpty(rra.getCmMainPPNSoldtoId())) {
					mailItems.put(ACSBI_EPN_Survey_URL, acsbi_epn_survey_url+rra.getCmMainPPNSoldtoId());
				} else {
					mailItems.put(ACSBI_EPN_Survey_URL, " ");
					logger.warn("Not sufficient data for rendering ACSBI URL");
				}
			}
			if(StringUtils.isNotEmpty(rra.getCmPollableSeCodeSeid())) {
				mailItems.put(ACSBI_EPN_Survey_Pollable_Secode_Seid, rra.getCmPollableSeCodeSeid());
			}


			mailItems.put(DYNAMICDATA_KEY, rra.getDynamicData());
			if(StringUtils.isNotEmpty(rra.getDynamicData()) && rra.getDynamicData().trim().length() > 0) {
				rra.setHeaderLevel(false);
			} else {
				rra.setHeaderLevel(true);
			}
			String subject = prepareSubject(rra);
			mailItems.put(SUBJECT_KEY, subject);

			// Select template:
			List<String> tos = new ArrayList<String>();

			logger.debug("RequsterEmail:"+rra.getRequestorEmail());
			if(rra.isEpnSurveyCompletionNotification()) {
				String emails = rra.getRequestorEmail();
				String[] emailsArray = emails.split(";");
				logger.debug("EPN Survey Completion Mail.................");

				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, cmPollingCompletionTemplate);
			} else if (rra.getIpoFilePath() != null) {
				mailItems.put("attatchment", rra.getAttatchmentContents());
				String emails = rra.getRequestorEmail();
				String[] emailsArray = emails.split(";");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				if(rra.isOnBoarding()){
					logger.debug("Sending-OnBoarding Mail.................");
					sendIPOMailNotification(tos, mailItems, onBoardingTemplate);
				}
				else if (StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(rra.getStatus()) || StatusEnum.INPROCESS.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())
						|| StatusEnum.AWAITINGINFO.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())) {
					logger.debug("Sending-IPO Mail.................");
					sendIPOMailNotification(tos, mailItems, ipoTemplate);
				}
			} else if (rra.getIsSystemMail() != null && rra.getIsSystemMail()) {
				logger.debug("System Mail.................");
				String[] systemToList = mailSendToSystemList.split(",");
				for (int i = 0; i < systemToList.length; i++) {
					tos.add(systemToList[i].trim());
				}
				sendMailNotification(tos, mailItems, systemTemplate);

			} else if (rra.getSendMail() != null && rra.getSendMail()) {
				String emails = rra.getRequestorEmail();
				String[] emailsArray = emails.split(";");
				logger.debug("Non - IPO Mail.................");

				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				if (StatusEnum.AWAITINGINFO.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())) {
					if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep()) || ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())
							//GRT 4.0 : defect Fix 375
							|| ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())
					) {
						sendMailNotification(tos, mailItems, awaitingInfoIbEqrTemplate);
					} else {
						if(rra.isSecondAttempt()) {
							sendMailNotification(tos, mailItems, awaitingInfoTobSecondAttemptTemplate);
						} else if(rra.isTOBSRCreated()) {
							sendMailNotification(tos, mailItems, awaitingInfoTemplate);
						} else if(rra.isStepBCancellation()) {
							sendMailNotification(tos, mailItems, stepBSRCancellation);
						} 
						//Defect UAT #865 : Send registration level mail for Alarming & Connectivity
						else if(rra.isSalAlarmConMail()) {
							sendMailNotification(tos, mailItems, awaitingInfoTemplateForAlarm);
						}
						else {
							sendMailNotification(tos, mailItems, awaitingInfoTobFirstAttemptTemplate);
						}
					}
				} else if (StatusEnum.COMPLETED.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())) {
					if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) {
						sendMailNotification(tos, mailItems, completedIbEqrTemplate);
					} else if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) {
						sendMailNotification(tos, mailItems, completedEqrTemplate);
					} else if(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) { 
						sendMailNotification(tos, mailItems, completedEqmTemplate);
					} 
					else {
						if(rra.isStepB()) {
							sendMailNotification(tos, mailItems, completedSALStepBTemplate);
						} else if(rra.getRegistrationTypeId().equalsIgnoreCase(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
							sendMailNotification(tos, mailItems, completedSALMigrationTemplate);
						} else if(rra.isHeaderLevel()) {
							sendMailNotification(tos, mailItems, completedTemplate);
						} else {
							sendMailNotification(tos, mailItems, completedTobTemplate);
						}
					}
				} else if (StatusEnum.INPROCESS.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())) {
					if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep()) || ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) {
						sendMailNotification(tos, mailItems, inProcessIbEqrTemplate);
					} else if(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) { 
						sendMailNotification(tos, mailItems, inProcessEqmTemplate);
					}
					else {
						if(!rra.isStepB() && rra.getRegistrationTypeId().equalsIgnoreCase(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
							sendMailNotification(tos, mailItems, inProcessSALMigrationTemplate);
						} else if(rra.isStepBCancellation()) {
							sendMailNotification(tos, mailItems, stepBSRCancellation);
						} else {
							sendMailNotification(tos, mailItems, inProcessTemplate);
						}
					}
				} else if (StatusEnum.VALIDATED.getStatusShortDescription().equalsIgnoreCase(rra.getStatus())) {
					if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepShortDescription().equalsIgnoreCase(rra.getProcessStep())) {
						sendMailNotification(tos, mailItems, validatedInfoRecordValidationTemplate);
					} 
				} else {
					logger
							.error("ERROR: Cannot send email since no template found for Registration Siebel SR Status ["
									+ rra.getSiebelSRStatus() + "]");
				}
			}
		} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }

    public void sendMailNotification(TokenRedemptionRequestType request, String contractNumber, 
    		Calendar contractStartDate, String additionalInfo, TokenResponseTypeDto tokenDetails,String emails, Contract contractToCancel) {
    	logger.debug("Entering sendMailNotification for OEFC");
    	try {
			Map<String, Object> mailItems = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(contractNumber)) {
				mailItems.put(CONTRACT_NUMBER_KEY, contractNumber);
			} else {
				mailItems.put(CONTRACT_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(request.getTokenNumber())){
				mailItems.put(TOKEN_NUMBER_KEY, request.getTokenNumber());
			} else {
				mailItems.put(TOKEN_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(request.getRedeemerBPName())){
				mailItems.put(RESELLER_NAME_KEY, request.getRedeemerBPName());
			} else {
				mailItems.put(RESELLER_NAME_KEY, " ");
			}
			if(StringUtils.isNotEmpty(tokenDetails.getDistiBPLinkId())){
				mailItems.put(BP_LINK_ID_KEY, tokenDetails.getDistiBPLinkId());
			} if(StringUtils.isNotEmpty(request.getRedeemerBPLinkID())){
				mailItems.put(BP_LINK_ID_KEY, request.getRedeemerBPLinkID());
			} else {
				mailItems.put(BP_LINK_ID_KEY, " ");
			}
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date today = new Date();
				logger.debug("today:" + today);
				mailItems.put(DATE_REDEEMED_KEY, sdf.format(today));
				logger.debug("contractStartDate:" + contractStartDate);
				if(contractStartDate != null) {
					logger.debug("contractStartDate.getTime():" + contractStartDate.getTime());
					mailItems.put(CONTRACT_START_DATE_KEY, sdf.format(contractStartDate.getTime()));
				}
			} catch (Throwable throwable) {
				mailItems.put(CONTRACT_START_DATE_KEY, " ");
				mailItems.put(DATE_REDEEMED_KEY, " ");
				logger.error("error while parsing Contract Start Date and/or Date redeemed", throwable);
			}
			if(tokenDetails != null) {
				if(StringUtils.isNotEmpty(tokenDetails.getServiceType()) ) {
					mailItems.put(TOKEN_SERVICE_DESCRIPTION_KEY, tokenDetails.getServiceType());
				} else {
					mailItems.put(TOKEN_SERVICE_DESCRIPTION_KEY, " ");
				}
				if(tokenDetails.getTokendata() != null && tokenDetails.getTokendata().length > 0) {
					StringBuffer hardwareDetails = new StringBuffer("<table border=\"1pt\"><tr><th>Service Description</th><th>Service-Material/ Material-Code</th><th>Quantity</th></tr>");
					for (TokenDetails hardware : tokenDetails.getTokendata()) {
						if(hardware != null) {
							for (ServiceDetails serviceDetail : hardware.getServiceDetails()) {
								hardwareDetails.append("<tr><td>").append(serviceDetail.getServiceCodeDescription()).append("</td>");
								hardwareDetails.append("<td>");
								if(StringUtils.isNotEmpty(serviceDetail.getServiceCode())) { 
									hardwareDetails.append(serviceDetail.getServiceCode());
								}
								if(StringUtils.isNotEmpty(serviceDetail.getMaterialCode())) { 
									hardwareDetails.append("/").append(serviceDetail.getMaterialCode());
								}
								hardwareDetails.append("</td>");
								hardwareDetails.append("<td>").append(serviceDetail.getQuantity()).append("</td></tr>");
							}
						}
					}
					hardwareDetails.append("</table>");
					mailItems.put(TOKEN_HARDWARE_DETAILS_KEY, hardwareDetails.toString());
				} else {
					mailItems.put(TOKEN_HARDWARE_DETAILS_KEY, " ");
					
				}
			}
            if(StringUtils.isNotEmpty(additionalInfo)) {
            	mailItems.put(ADDITIONAL_INFO_KEY, additionalInfo);
            } else {
            	mailItems.put(ADDITIONAL_INFO_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getEndCustomerShipTo())) {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, request.getEndCustomerShipTo());
            } else {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
            }
            AddressType address = request.getEndCustomerShipToAddress();
            if(StringUtils.isNotEmpty(request.getEndCustomerName())) {
            	mailItems.put(END_CUSTOMER_NAME_KEY, request.getEndCustomerName());
            } else {
            	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
            }
            if(address != null) {
            	String addressDetails = address.getStreet1() + ",<BR>";
            	if(StringUtils.isNotEmpty(address.getStreet2())) {
            		addressDetails += address.getStreet2() + ",<BR>";
            	}
            	addressDetails += address.getCity() + ", " + (StringUtils.isNotEmpty(address.getState_Full())? address.getState_Full():(StringUtils.isNotEmpty(address.getRegion())? address.getRegion():"")) + ",<BR>";
            	addressDetails += address.getCountry() +", " + address.getZip();
            	logger.debug("addressDetails:" +addressDetails);
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, addressDetails);
            } else {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getEndCustomerEmail())) {
            	mailItems.put(END_CUSTOMER_CONTACT_KEY, request.getEndCustomerEmail());
            } else {
            	mailItems.put(END_CUSTOMER_CONTACT_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getRedeemerName())) {
            	mailItems.put(REDEEMER_NAME_KEY, request.getRedeemerName());
            } else {
            	mailItems.put(REDEEMER_NAME_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getRedeemerEmail())) {
            	mailItems.put(REDEEMER_CONTACT_KEY, request.getRedeemerEmail());
            } else {
            	mailItems.put(REDEEMER_CONTACT_KEY, " ");
            }
			mailItems.put(SUBJECT_KEY, "OEFC : Notice of Contract actions required with Token Redemption");
			
			
			String contractToCancelNum = "";
			String cancelledContractStartDate = "";
			String cancelledContractEndDate = "";
			if(contractToCancel != null ){		
				contractToCancelNum = contractToCancel.getContractNumber();
				logger.debug("Contract Number:"+contractToCancelNum);
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					cancelledContractStartDate = sdf.format(contractToCancel.getContractStartDate());
					cancelledContractEndDate = sdf.format(contractToCancel.getContractEndDate());
					logger.debug("Contract Start Date:"+cancelledContractStartDate);
	            	logger.debug("Contract End Date:"+cancelledContractEndDate);
				} catch (Throwable throwable) {					
					logger.error("error while parsing Contract Date Created", throwable);
				}
			}
			mailItems.put(CANCEL_CONTRACT_NUMBER_KEY, contractToCancelNum );	
			mailItems.put(CANCEL_CONTRACT_START_DATE_KEY, cancelledContractStartDate);					
			mailItems.put(CANCEL_CONTRACT_END_DATE_KEY, cancelledContractEndDate);
			
			if(StringUtils.isNotEmpty(this.tokenRedemptionOefcEmailList)) {
				List<String> tos = new ArrayList<String>();
				//String emails = this.tokenRedemptionOefcEmailList;
				String[] emailsArray = emails.split(",");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, tokenRedemptionOefcTemplate);
			} else {
				logger.warn("Not Sending mail, As no OEFC emails ids configured.");
			}
		} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }

    /*public void sendMailNotificationAccountCreation(String soldToOrShipToId, String endCustomerName, String endCustomerStreetName, 
    		String endCustomerCity, String endCustomerState, String endCustomerPostalCode, String endCustomerCountry, String bpLinkId, String email) {
    	logger.debug("Entering sendMailNotification for Account Creation");
    	try {
    		Map<String, String> mailItems = new HashMap<String, String>();
    		if(StringUtils.isNotEmpty(soldToOrShipToId)) {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, soldToOrShipToId);
            } else {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerName)) {
            	mailItems.put(END_CUSTOMER_NAME_KEY, endCustomerName);
            } else {
            	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerStreetName)) {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, endCustomerStreetName);
            } else {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerCity)) {
            	mailItems.put(END_CUSTOMER_CITY_KEY, endCustomerCity);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerState)) {
            	mailItems.put(END_CUSTOMER_CITY_STATE_KEY, endCustomerState);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_STATE_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerPostalCode)) {
            	mailItems.put(END_CUSTOMER_CITY_POSTALCODE_KEY, endCustomerPostalCode);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_POSTALCODE_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerCountry)) {
            	mailItems.put(END_CUSTOMER_CITY_COUNTRY_KEY, endCustomerCountry);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_COUNTRY_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(bpLinkId)) {
            	mailItems.put(BP_LINK_ID_KEY, bpLinkId);
            } else {
            	mailItems.put(BP_LINK_ID_KEY, " ");
            }
    		mailItems.put(SUBJECT_KEY, "Confirmation of Customer Account : " + soldToOrShipToId);
			if(StringUtils.isNotEmpty(email)) {
				List<String> tos = new ArrayList<String>();
				String emails = email;
				String[] emailsArray = emails.split(";");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, this.accountCreationTemplate);
    		
			} 
    	} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
    	logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }
    
    private Map<String, String> getMapForDuplicateAccountCMD(SoldToCreateRequestType soldRequest, ShipToCreateRequestType shipRequest, String duplicateAccountReason, String accountNumber, String bpLinkId, String bpName, String loginName, String loginEmail){
    	Map<String, String> mailItems = new HashMap<String, String>();
    	if(soldRequest != null) {
    		if(StringUtils.isNotEmpty(accountNumber)){
				mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, accountNumber);
			} else {
				mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
			}
    		
    		AddressType address = soldRequest.getSoldToAddress();
	        if(StringUtils.isNotEmpty(soldRequest.getSoldToName1())) {
	        	mailItems.put(END_CUSTOMER_NAME_KEY, soldRequest.getSoldToName1());
	        } else {
	        	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
	        }
	        if(address != null) {
	        	String addressDetails = address.getStreet1() + ",<BR>";
	        	if(StringUtils.isNotEmpty(address.getStreet2())) {
	        		addressDetails += address.getStreet2() + ",<BR>";
	        	}
	        	addressDetails += address.getCity() + ", " + (StringUtils.isNotEmpty(address.getState_Full())? address.getState_Full():(StringUtils.isNotEmpty(address.getRegion())? address.getRegion():"")) + ",<BR>";
	        	addressDetails += address.getCountry() +", " + address.getZip();
	        	logger.debug("addressDetails:" +addressDetails);
	        	mailItems.put(END_CUSTOMER_ADDRESS_KEY, addressDetails);
	        } else {
	        	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
	        }
	        
	        if(StringUtils.isNotEmpty(soldRequest.getContactEmail())) {
	        	String contactDetails = soldRequest.getContactFirstName() + ", " + soldRequest.getContactLastName();
	        	if(StringUtils.isNotEmpty(soldRequest.getContactEmail())) {
	        		contactDetails += "<br>" + soldRequest.getContactEmail();
	        	}
	        	if(StringUtils.isNotEmpty(soldRequest.getContactPhone())) {
	        		contactDetails += "<br>" + soldRequest.getContactPhone();
	        	}
	        	mailItems.put(END_CUSTOMER_CONTACT_KEY, contactDetails);
	        } else {
	        	mailItems.put(END_CUSTOMER_CONTACT_KEY, " ");
	        }
	        
	        try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date today = new Date();
				logger.debug("today:" + today);
				mailItems.put(DATE_CREATED_KEY, sdf.format(today));
			} catch (Throwable throwable) {
				mailItems.put(DATE_CREATED_KEY, " ");
				logger.error("error while parsing Contract Date Created", throwable);
			}
			
			mailItems.put(ACCOUNT_CREATION_REASON_KEY, duplicateAccountReason);
			
			if(StringUtils.isNotEmpty(soldRequest.getTokenNumber())){
				mailItems.put(TOKEN_NUMBER_KEY, soldRequest.getTokenNumber());
			} else {
				mailItems.put(TOKEN_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(bpLinkId)) {
				mailItems.put(BP_LINK_ID_KEY, bpLinkId);
			} else {
				mailItems.put(BP_LINK_ID_KEY, " ");
			}
			if(StringUtils.isNotEmpty(bpName)) {
				mailItems.put(RESELLER_NAME_KEY, bpName);
			} else {
				mailItems.put(RESELLER_NAME_KEY, " ");
			}
			
			if(StringUtils.isNotEmpty(loginName)) {
				mailItems.put(ACCOUNT_CREATED_BY_KEY, loginName);
			} else {
				mailItems.put(ACCOUNT_CREATED_BY_KEY, " ");
			}
			if(StringUtils.isNotEmpty(loginEmail)) {
				mailItems.put(ACCOUNT_CREATED_BY_CONTACT_KEY, loginEmail);
			} else {
				mailItems.put(ACCOUNT_CREATED_BY_CONTACT_KEY, " ");
			}
    	} else {
    		if(StringUtils.isNotEmpty(accountNumber)){
				mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, accountNumber);
			} else {
				mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
			}
    		
    		AddressType address = shipRequest.getShipToAddress();
	        if(StringUtils.isNotEmpty(shipRequest.getShipToName1())) {
	        	mailItems.put(END_CUSTOMER_NAME_KEY, shipRequest.getShipToName1());
	        } else {
	        	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
	        }
	        if(address != null) {
	        	String addressDetails = address.getStreet1() + ",<BR>";
	        	if(StringUtils.isNotEmpty(address.getStreet2())) {
	        		addressDetails += address.getStreet2() + ",<BR>";
	        	}
	        	addressDetails += address.getCity() + ", " + (StringUtils.isNotEmpty(address.getState_Full())? address.getState_Full():(StringUtils.isNotEmpty(address.getRegion())? address.getRegion():"")) + ",<BR>";
	        	addressDetails += address.getCountry() +", " + address.getZip();
	        	logger.debug("addressDetails:" +addressDetails);
	        	mailItems.put(END_CUSTOMER_ADDRESS_KEY, addressDetails);
	        } else {
	        	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
	        }
	        
	        if(StringUtils.isNotEmpty(shipRequest.getContactEmail())) {
	        	String contactDetails = shipRequest.getContactFirstName() + ", " + shipRequest.getContactLastName();
	        	if(StringUtils.isNotEmpty(shipRequest.getContactEmail())) {
	        		contactDetails += "<br>" + shipRequest.getContactEmail();
	        	}
	        	if(StringUtils.isNotEmpty(shipRequest.getContactPhone())) {
	        		contactDetails += "<br>" + shipRequest.getContactPhone();
	        	}
	        	mailItems.put(END_CUSTOMER_CONTACT_KEY, contactDetails);
	        } else {
	        	mailItems.put(END_CUSTOMER_CONTACT_KEY, " ");
	        }
	        
	        try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date today = new Date();
				logger.debug("today:" + today);
				mailItems.put(DATE_CREATED_KEY, sdf.format(today));
			} catch (Throwable throwable) {
				mailItems.put(DATE_CREATED_KEY, " ");
				logger.error("error while parsing Contract Date Created", throwable);
			}
			
			mailItems.put(ACCOUNT_CREATION_REASON_KEY, duplicateAccountReason);
			
			if(StringUtils.isNotEmpty(shipRequest.getTokenNumber())){
				mailItems.put(TOKEN_NUMBER_KEY, shipRequest.getTokenNumber());
			} else {
				mailItems.put(TOKEN_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(bpLinkId)) {
				mailItems.put(BP_LINK_ID_KEY, bpLinkId);
			} else {
				mailItems.put(BP_LINK_ID_KEY, " ");
			}
			if(StringUtils.isNotEmpty(bpName)) {
				mailItems.put(RESELLER_NAME_KEY, bpName);
			} else {
				mailItems.put(RESELLER_NAME_KEY, " ");
			}
			
			if(StringUtils.isNotEmpty(loginName)) {
				mailItems.put(ACCOUNT_CREATED_BY_KEY, loginName);
			} else {
				mailItems.put(ACCOUNT_CREATED_BY_KEY, " ");
			}
			if(StringUtils.isNotEmpty(loginEmail)) {
				mailItems.put(ACCOUNT_CREATED_BY_CONTACT_KEY, loginEmail);
			} else {
				mailItems.put(ACCOUNT_CREATED_BY_CONTACT_KEY, " ");
			}
    	}
    	mailItems.put(SUBJECT_KEY, "CMD NOTICE: Review for Possible Duplicate Customer Account");
    	return mailItems;
    }
    
    public void sendMailNotification(SoldToCreateRequestType soldToRequest, ShipToCreateRequestType shipToRequest, String duplicateAccountReason, String accountNumber, String bpLinkId, String bpName, String loginName, String loginEmail) {
    	logger.debug("Entering sendMailNotification for CMD");
    	try {
    		Map<String, String> mailItems = this.getMapForDuplicateAccountCMD(soldToRequest, shipToRequest, duplicateAccountReason, accountNumber, bpLinkId, bpName, loginName, loginEmail);
    		if(mailItems != null) {
    			for (String key : mailItems.keySet()) {
					logger.debug("key:" + key + ", value:" + mailItems.get(key));
				}
    		}
			if(StringUtils.isNotEmpty(this.duplicateAccountCreationEmailList)) {
				List<String> tos = new ArrayList<String>();
				String emails = this.duplicateAccountCreationEmailList;
				String[] emailsArray = emails.split(",");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, this.duplicateAccountCMDTemplate);
			} else {
				logger.warn("Not Sending mail, As no CMD emails ids configured.");
			}
		} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }
    */
    private String prepareSubject(RegistrationRequestAlert rra) {
    	String subject = null;
    	if(rra.isOnBoarding()) {
    		subject = "GRT Registration ID: " + rra.getRegistrationId() + ", Request for IPO On-Boarding File";
    	} else if (rra.getIpoFilePath() != null ) {
    		subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", Status: " + rra.getStatus();
    	} else if (rra.isEpnSurveyCompletionNotification()) {
    		subject = "GRT Registration ID: " + rra.getRegistrationId() + ", Remote Site Survey Polling, Status: Completed";
    	} else {
    		if(StringUtils.isNotEmpty(rra.getProcessStep()) && rra.getProcessStep().equalsIgnoreCase(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription())) {
    			if(rra.isHeaderLevel()) {
    				subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", Registration Level, Status: " + rra.getStatus();
    			} else {
    				subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", Product Detail Level, Status: " + rra.getStatus();
    			}
    		} else {
    			subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", Status: " + rra.getStatus();
    		}
    		if(rra.getTechnicalRegistrationId() != null) {
	            subject += ", Technical On-Boarding Id: " + rra.getTechnicalRegistrationId();
	        }
	        if(rra.getSalRegistrationType() != null) {
	            subject += ", SAL Type: " + rra.getSalRegistrationType();
	        }
	        if(rra.isStepBCancellation()) {
	        	subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", SAL Alarm and Connectivity Testing, Status: Awaiting Information";
	        } else  if(rra.isStepB()) {
	        	subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", SAL Alarm and Connectivity Testing, Status: " + rra.getStatus();
	        } else if(StringUtils.isNotEmpty(rra.getRegistrationTypeId()) && rra.getRegistrationTypeId().equalsIgnoreCase(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
	        	subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", SAL Migration, Status: " + rra.getStatus();
	        } else if(rra.isTobUpdate()) {
	        	subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", Update, Status: " + rra.getStatus();
	        } else if(rra.isSAL()) {
	        	subject = "GRT Registration ID: " + rra.getRegistrationId() + ", " + rra.getProcessStep() + ", SAL Records Building, Status: " + rra.getStatus();
	        }
	        if(rra.getSeCode() != null) {
	            subject += ", Solution Element Code: " + rra.getSeCode();
	        }

	        if(rra.getSrRequestStatusId() != null) {
	            String processStatus;
	            if(SRRequestStatusEnum.INITIATION.getStatusId().equalsIgnoreCase(rra.getSrRequestStatusId())) {
	                processStatus = SRRequestStatusEnum.INITIATION.getStatusShortDescription();
	            }
	            else if(SRRequestStatusEnum.SR_CREATED.getStatusId().equalsIgnoreCase(rra.getSrRequestStatusId())) {
	                processStatus = SRRequestStatusEnum.SR_CREATED.getStatusShortDescription();
	            }
	            else if(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId().equalsIgnoreCase(rra.getSrRequestStatusId())) {
	                processStatus = SRRequestStatusEnum.ATTACHMENT_SENT.getStatusShortDescription();
	            }
	            else {
	                processStatus = SRRequestStatusEnum.SUCCESS.getStatusShortDescription();
	            }
	            subject += ", SR Creation Process Status: " + processStatus;
	        }
	        //Defect #905 : "Registration Identifier" in the subject line and that should  be changed to "Registration Name"
	        if(StringUtils.isNotEmpty(rra.getRegistrationIdentifier())) {
	        	subject += ", Registration Name: " + rra.getRegistrationIdentifier();
	        }
    	}
    	return subject;
    }

    public void sendMailNotification(final List<String> sendTos, final Map<String, Object> mailItems, final String template) {
   	 	logger.debug("Entering MailUtil : sendMailNotification - Step 2" );
        logger.debug("sendMailNotification using template: " + template);
        for (String string : sendTos) {
        	logger.debug("send Tos:"+string);
		}


        try {
			MimeMessagePreparator mimepreparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					try {
						MimeMessageHelper message = new MimeMessageHelper(
								mimeMessage, true);
						for (String sendTo : sendTos) {
							message.addTo(sendTo);
						}
						message.setFrom(sendFrom);
						message.setSubject((String) mailItems.get(SUBJECT_KEY));
						String mailContent = VelocityEngineUtils
								.mergeTemplateIntoString(velocityEngine, template,
										mailItems);
						message.setText(mailContent, true);
						message.addInline(LOGO_NAME_KEY, logoResource);
						//Commented below line for Defect #339 
						/*logger.debug("sendMailNotification adding " + LOGO_NAME_KEY
								+ " in " + logoResource.getURI().toString());*/
					} catch (Exception ex) {
						logger.debug("Error in sendMailNotification : prepare :" + ex.getMessage());
					}
				}
			};
			getJavaMailSender().send(mimepreparator);
		} catch (Exception ex) {
			logger.debug("Error in sendMailNotification - Step 2 - " + ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 2" );
    }

    public void sendIPOMailNotification(final List<String> sendTos, final Map<String, Object> mailItems, final String template) {
   	 	logger.debug("Entering MailUtil : sendIPOMailNotification " );
        logger.debug("sendMailNotification using template: " + template);

        for (String string : sendTos) {
        	logger.debug("IPO---send Tos:"+string);
		}

        try {
			MimeMessagePreparator mimepreparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					try {
						MimeMessageHelper message = new MimeMessageHelper(
								mimeMessage, true);
						for (String sendTo : sendTos) {
							message.addTo(sendTo);
						}
						message.setFrom(sendFrom);
						message.setSubject((String) mailItems.get(SUBJECT_KEY));
						String mailContent = VelocityEngineUtils
								.mergeTemplateIntoString(velocityEngine, template,
										mailItems);
						//logger.debug("mailContent:"+mailContent);
						if(mailItems.get("attatchment") != null) {
							ByteArrayResource bar = new ByteArrayResource(((String)mailItems.get("attatchment")).getBytes());
							message.addAttachment(ONBOARDING_XML_FILE_NAME, bar, "text/xml");
						}
						message.setText(mailContent, true);
						message.addInline(LOGO_NAME_KEY, logoResource);
						logger.debug("sendMailNotification adding " + LOGO_NAME_KEY
								+ " in " + logoResource.getURI().toString());
					} catch (Exception ex) {
						logger.debug("Error in sendMailNotification : prepare :" + ex.getMessage());
					}
				}
			};
			getJavaMailSender().send(mimepreparator);
		} catch (Exception ex) {
			logger.debug("Error in sendMailNotification - Step 2 - " + ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 2" );
    }

    public void sendMailNotificationAccountCreation(String soldToOrShipToId, String endCustomerName, String endCustomerStreetName, 
    		String endCustomerCity, String endCustomerState, String endCustomerPostalCode, String endCustomerCountry, String bpLinkId, String email) {
    	logger.debug("Entering sendMailNotification for Account Creation");
    	try {
    		Map<String, Object> mailItems = new HashMap<String, Object>();
    		if(StringUtils.isNotEmpty(soldToOrShipToId)) {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, soldToOrShipToId);
            } else {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerName)) {
            	mailItems.put(END_CUSTOMER_NAME_KEY, endCustomerName);
            } else {
            	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerStreetName)) {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, endCustomerStreetName);
            } else {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerCity)) {
            	mailItems.put(END_CUSTOMER_CITY_KEY, endCustomerCity);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerState)) {
            	mailItems.put(END_CUSTOMER_CITY_STATE_KEY, endCustomerState);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_STATE_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerPostalCode)) {
            	mailItems.put(END_CUSTOMER_CITY_POSTALCODE_KEY, endCustomerPostalCode);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_POSTALCODE_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(endCustomerCountry)) {
            	mailItems.put(END_CUSTOMER_CITY_COUNTRY_KEY, endCustomerCountry);
            } else {
            	mailItems.put(END_CUSTOMER_CITY_COUNTRY_KEY, " ");
            }
    		if(StringUtils.isNotEmpty(bpLinkId)) {
            	mailItems.put(BP_LINK_ID_KEY, bpLinkId);
            } else {
            	mailItems.put(BP_LINK_ID_KEY, " ");
            }
    		mailItems.put(SUBJECT_KEY, "Confirmation of Customer Account : " + soldToOrShipToId);
			if(StringUtils.isNotEmpty(email)) {
				List<String> tos = new ArrayList<String>();
				String emails = email;
				String[] emailsArray = emails.split(";");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, this.accountCreationTemplate);
    		
			} 
    	} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
    	logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }
    
    public void sendMailNotification(TokenRedemptionEmailDto request, String contractNumber, Calendar contractStartDate, String additionalInfo, TokenResponseTypeDto tokenDetails,String emails) {
    	logger.debug("Entering sendMailNotification for OEFC");
    	try {
			Map<String, Object> mailItems = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(contractNumber)) {
				mailItems.put(CONTRACT_NUMBER_KEY, contractNumber);
			} else {
				mailItems.put(CONTRACT_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(request.getTokenNumber())){
				mailItems.put(TOKEN_NUMBER_KEY, request.getTokenNumber());
			} else {
				mailItems.put(TOKEN_NUMBER_KEY, " ");
			}
			if(StringUtils.isNotEmpty(request.getRedeemerBPName())){
				mailItems.put(RESELLER_NAME_KEY, request.getRedeemerBPName());
			} else {
				mailItems.put(RESELLER_NAME_KEY, " ");
			}
			if(StringUtils.isNotEmpty(tokenDetails.getDistiBPLinkId())){
				mailItems.put(BP_LINK_ID_KEY, tokenDetails.getDistiBPLinkId());
			} if(StringUtils.isNotEmpty(request.getRedeemerBPLinkID())){
				mailItems.put(BP_LINK_ID_KEY, request.getRedeemerBPLinkID());
			} else {
				mailItems.put(BP_LINK_ID_KEY, " ");
			}
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date today = new Date();
				logger.debug("today:" + today);
				mailItems.put(DATE_REDEEMED_KEY, sdf.format(today));
				logger.debug("contractStartDate:" + contractStartDate);
				if(contractStartDate != null) {
					logger.debug("contractStartDate.getTime():" + contractStartDate.getTime());
					mailItems.put(CONTRACT_START_DATE_KEY, sdf.format(contractStartDate.getTime()));
				}
			} catch (Throwable throwable) {
				mailItems.put(CONTRACT_START_DATE_KEY, " ");
				mailItems.put(DATE_REDEEMED_KEY, " ");
				logger.error("error while parsing Contract Start Date and/or Date redeemed", throwable);
			}
			if(tokenDetails != null) {
				if(StringUtils.isNotEmpty(tokenDetails.getServiceType()) ) {
					mailItems.put(TOKEN_SERVICE_DESCRIPTION_KEY, tokenDetails.getServiceType());
				} else {
					mailItems.put(TOKEN_SERVICE_DESCRIPTION_KEY, " ");
				}
				if(tokenDetails.getTokendata() != null && tokenDetails.getTokendata().length > 0) {
					StringBuffer hardwareDetails = new StringBuffer("<table border=\"1pt\"><tr><th>Service Description</th><th>Service-Material/ Material-Code</th><th>Quantity</th></tr>");
					for (TokenDetails hardware : tokenDetails.getTokendata()) {
						if(hardware != null) {
							for (ServiceDetails serviceDetail : hardware.getServiceDetails()) {
								hardwareDetails.append("<tr><td>").append(serviceDetail.getServiceCodeDescription()).append("</td>");
								hardwareDetails.append("<td>");
								if(StringUtils.isNotEmpty(serviceDetail.getServiceCode())) { 
									hardwareDetails.append(serviceDetail.getServiceCode());
								}
								if(StringUtils.isNotEmpty(serviceDetail.getMaterialCode())) { 
									hardwareDetails.append("/").append(serviceDetail.getMaterialCode());
								}
								hardwareDetails.append("</td>");
								hardwareDetails.append("<td>").append(serviceDetail.getQuantity()).append("</td></tr>");
							}
						}
					}
					hardwareDetails.append("</table>");
					mailItems.put(TOKEN_HARDWARE_DETAILS_KEY, hardwareDetails.toString());
				} else {
					mailItems.put(TOKEN_HARDWARE_DETAILS_KEY, " ");
					
				}
			}
            if(StringUtils.isNotEmpty(additionalInfo)) {
            	mailItems.put(ADDITIONAL_INFO_KEY, additionalInfo);
            } else {
            	mailItems.put(ADDITIONAL_INFO_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getEndCustomerShipTo())) {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, request.getEndCustomerShipTo());
            } else {
            	mailItems.put(END_CUSTOMER_SHIP_TO_ID_KEY, " ");
            }
            //AddressType address = request.getEndCustomerShipToAddress();
            if(StringUtils.isNotEmpty(request.getEndCustomerName())) {
            	mailItems.put(END_CUSTOMER_NAME_KEY, request.getEndCustomerName());
            } else {
            	mailItems.put(END_CUSTOMER_NAME_KEY, " ");
            }

            String addressDetails = request.getStreet1() + ",<BR>";
            if(StringUtils.isNotEmpty(request.getStreet2())) {
            	addressDetails += request.getStreet2() + ",<BR>";
            }
            addressDetails += request.getCity() + ", " + (StringUtils.isNotEmpty(request.getState_Full())? request.getState_Full():(StringUtils.isNotEmpty(request.getRegion())? request.getRegion():"")) + ",<BR>";
            addressDetails += request.getCountry() +", " + request.getZip();
            logger.debug("addressDetails:" +addressDetails);
            if(StringUtils.isNotEmpty(addressDetails)) {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, addressDetails);
            } else {
            	mailItems.put(END_CUSTOMER_ADDRESS_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getEndCustomerEmail())) {
            	mailItems.put(END_CUSTOMER_CONTACT_KEY, request.getEndCustomerEmail());
            } else {
            	mailItems.put(END_CUSTOMER_CONTACT_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getRedeemerName())) {
            	mailItems.put(REDEEMER_NAME_KEY, request.getRedeemerName());
            } else {
            	mailItems.put(REDEEMER_NAME_KEY, " ");
            }
            if(StringUtils.isNotEmpty(request.getRedeemerEmail())) {
            	mailItems.put(REDEEMER_CONTACT_KEY, request.getRedeemerEmail());
            } else {
            	mailItems.put(REDEEMER_CONTACT_KEY, " ");
            }
			mailItems.put(SUBJECT_KEY, "OEFC : Notice of Contract actions required with Token Redemption");
			if(StringUtils.isNotEmpty(this.tokenRedemptionOefcEmailList)) {
				List<String> tos = new ArrayList<String>();
				//String emails = this.tokenRedemptionOefcEmailList;
				String[] emailsArray = emails.split(",");
				for (int i = 0; i < emailsArray.length; i++) {
					if (!"".equals(emailsArray[i].replaceAll(" ", ""))) {
						tos.add(emailsArray[i]);
					}
				}
				sendMailNotification(tos, mailItems, this.tokenRedemptionOefcTemplate);
			} else {
				logger.warn("Not Sending mail, As no OEFC emails ids configured.");
			}
		} catch (Exception ex) {
			logger.debug("Error in MailUtil : sendMailNotification - Step 1 "
					+ ex.getMessage());
		}
        logger.debug("Exiting MailUtil : sendMailNotification - Step 1");
    }
    
    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public Resource getLogoResource() {
        return logoResource;
    }

    public void setLogoResource(Resource logoResource) {
        this.logoResource = logoResource;
    }

    public String getAwaitingInfoTemplate() {
        return awaitingInfoTemplate;
    }

    public void setAwaitingInfoTemplate(String awaitingInfoTemplate) {
        this.awaitingInfoTemplate = awaitingInfoTemplate;
    }

    public String getCompletedTemplate() {
        return completedTemplate;
    }

    public void setCompletedTemplate(String completedTemplate) {
        this.completedTemplate = completedTemplate;
    }

    public String getInProcessTemplate() {
        return inProcessTemplate;
    }

    public void setInProcessTemplate(String inProcessTemplate) {
        this.inProcessTemplate = inProcessTemplate;
    }

    public String getMailSendToSystemList() {
        return mailSendToSystemList;
    }

    public void setMailSendToSystemList(String mailSendToSystemList) {
        this.mailSendToSystemList = mailSendToSystemList;
    }

    public String getSystemTemplate() {
        return systemTemplate;
    }

    public void setSystemTemplate(String systemTemplate) {
        this.systemTemplate = systemTemplate;
    }

	/**
	 * @return the ipoTemplate
	 */
	public String getIpoTemplate() {
		return ipoTemplate;
	}

	/**
	 * @param ipoTemplate the ipoTemplate to set
	 */
	public void setIpoTemplate(String ipoTemplate) {
		this.ipoTemplate = ipoTemplate;
	}

	public String getOnBoardingTemplate() {
		return onBoardingTemplate;
	}

	public void setOnBoardingTemplate(String onBoardingTemplate) {
		this.onBoardingTemplate = onBoardingTemplate;
	}

	public String getCmPollingCompletionTemplate() {
		return cmPollingCompletionTemplate;
	}

	public void setCmPollingCompletionTemplate(String cmPollingCompletionTemplate) {
		this.cmPollingCompletionTemplate = cmPollingCompletionTemplate;
	}

	public String getInProcessIbEqrTemplate() {
		return inProcessIbEqrTemplate;
	}

	public void setInProcessIbEqrTemplate(String inProcessIbEqrTemplate) {
		this.inProcessIbEqrTemplate = inProcessIbEqrTemplate;
	}

	public String getAwaitingInfoIbEqrTemplate() {
		return awaitingInfoIbEqrTemplate;
	}

	public void setAwaitingInfoIbEqrTemplate(String awaitingInfoIbEqrTemplate) {
		this.awaitingInfoIbEqrTemplate = awaitingInfoIbEqrTemplate;
	}

	public String getValidatedInfoRecordValidationTemplate() {
		return validatedInfoRecordValidationTemplate;
	}

	public void setValidatedInfoRecordValidationTemplate(
			String validatedInfoRecordValidationTemplate) {
		this.validatedInfoRecordValidationTemplate = validatedInfoRecordValidationTemplate;
	}

	public String getCompletedIbEqrTemplate() {
		return completedIbEqrTemplate;
	}

	public void setCompletedIbEqrTemplate(String completedIbEqrTemplate) {
		this.completedIbEqrTemplate = completedIbEqrTemplate;
	}

	public String getAwaitingInfoTobFirstAttemptTemplate() {
		return awaitingInfoTobFirstAttemptTemplate;
	}

	public void setAwaitingInfoTobFirstAttemptTemplate(
			String awaitingInfoTobFirstAttemptTemplate) {
		this.awaitingInfoTobFirstAttemptTemplate = awaitingInfoTobFirstAttemptTemplate;
	}

	public String getAwaitingInfoTobSecondAttemptTemplate() {
		return awaitingInfoTobSecondAttemptTemplate;
	}

	public void setAwaitingInfoTobSecondAttemptTemplate(
			String awaitingInfoTobSecondAttemptTemplate) {
		this.awaitingInfoTobSecondAttemptTemplate = awaitingInfoTobSecondAttemptTemplate;
	}

	public String getCompletedTobTemplate() {
		return completedTobTemplate;
	}

	public void setCompletedTobTemplate(String completedTobTemplate) {
		this.completedTobTemplate = completedTobTemplate;
	}

	public String getCompletedSALMigrationTemplate() {
		return completedSALMigrationTemplate;
	}

	public void setCompletedSALMigrationTemplate(
			String completedSALMigrationTemplate) {
		this.completedSALMigrationTemplate = completedSALMigrationTemplate;
	}

	public String getInProcessSALMigrationTemplate() {
		return inProcessSALMigrationTemplate;
	}

	public void setInProcessSALMigrationTemplate(
			String inProcessSALMigrationTemplate) {
		this.inProcessSALMigrationTemplate = inProcessSALMigrationTemplate;
	}

	public String getCompletedSALStepBTemplate() {
		return completedSALStepBTemplate;
	}

	public void setCompletedSALStepBTemplate(String completedSALStepBTemplate) {
		this.completedSALStepBTemplate = completedSALStepBTemplate;
	}

	public String getCompletedEqrTemplate() {
		return completedEqrTemplate;
	}

	public void setCompletedEqrTemplate(String completedEqrTemplate) {
		this.completedEqrTemplate = completedEqrTemplate;
	}

	public String getStepBSRCancellation() {
		return stepBSRCancellation;
	}


	public void setStepBSRCancellation(String stepBSRCancellation) {
		this.stepBSRCancellation = stepBSRCancellation;
	}

	public String getTokenRedemptionOefcTemplate() {
		return tokenRedemptionOefcTemplate;
	}

	public void setTokenRedemptionOefcTemplate(String tokenRedemptionOefcTemplate) {
		this.tokenRedemptionOefcTemplate = tokenRedemptionOefcTemplate;
	}

	public String getTokenRedemptionOefcEmailList() {
		return tokenRedemptionOefcEmailList;
	}

	public void setTokenRedemptionOefcEmailList(String tokenRedemptionOefcEmailList) {
		this.tokenRedemptionOefcEmailList = tokenRedemptionOefcEmailList;
	}

	public String getDuplicateAccountCMDTemplate() {
		return duplicateAccountCMDTemplate;
	}

	public void setDuplicateAccountCMDTemplate(String duplicateAccountCMDTemplate) {
		this.duplicateAccountCMDTemplate = duplicateAccountCMDTemplate;
	}

	public String getDuplicateAccountCreationEmailList() {
		return duplicateAccountCreationEmailList;
	}

	public void setDuplicateAccountCreationEmailList(
			String duplicateAccountCreationEmailList) {
		this.duplicateAccountCreationEmailList = duplicateAccountCreationEmailList;
	}

	public String getAccountCreationTemplate() {
		return accountCreationTemplate;
	}

	public void setAccountCreationTemplate(String accountCreationTemplate) {
		this.accountCreationTemplate = accountCreationTemplate;
	}

	public String getAcsbiEpnSurveyUrl() {
		return acsbiEpnSurveyUrl;
	}

	public void setAcsbiEpnSurveyUrl(String acsbiEpnSurveyUrl) {
		this.acsbiEpnSurveyUrl = acsbiEpnSurveyUrl;
	}

	public String getInProcessEqmTemplate() {
		return inProcessEqmTemplate;
	}

	public void setInProcessEqmTemplate(String inProcessEqmTemplate) {
		this.inProcessEqmTemplate = inProcessEqmTemplate;
	}

	public String getCompletedEqmTemplate() {
		return completedEqmTemplate;
	}

	public void setCompletedEqmTemplate(String completedEqmTemplate) {
		this.completedEqmTemplate = completedEqmTemplate;
	}

	public String getAwaitingInfoTemplateForAlarm() {
		return awaitingInfoTemplateForAlarm;
	}

	public void setAwaitingInfoTemplateForAlarm(String awaitingInfoTemplateForAlarm) {
		this.awaitingInfoTemplateForAlarm = awaitingInfoTemplateForAlarm;
	}
	
}
