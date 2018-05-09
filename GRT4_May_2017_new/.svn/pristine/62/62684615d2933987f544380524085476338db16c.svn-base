package com.avaya.grt.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Array;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.mappers.Alert;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.LocalTRConfig;
import com.avaya.grt.mappers.MaterialExclusion;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.RegistrationQuestions;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.SoldToSAPMapping;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.SuperUser;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.registration.DeviceTOBResponseType;
import com.avaya.v2.techregistration.TechRegAcknowledgementType;
import com.avaya.v2.techregistration.TechRegRespType;
import com.grt.dto.Account;
import com.grt.dto.Activity;
import com.grt.dto.Attachment;
import com.grt.dto.BusinessPartner;
import com.grt.dto.InstallBaseRespDataDto;
import com.grt.dto.PaginationForSiteRegistration;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationList;
import com.grt.dto.RegistrationRequestAlert;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.SRRequestDto;
import com.grt.dto.ServiceRequest;
import com.grt.dto.SoldTo;
import com.grt.dto.TRConfig;
import com.grt.dto.TechRegAlarmInputDto;
import com.grt.dto.TechRegInputDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.integration.art.ARTClient;
import com.grt.integration.art.GenericClient;
import com.grt.integration.sap.SapClient;
import com.grt.integration.siebel.SiebelClient;
import com.grt.util.ARTOperationType;
import com.grt.util.CurrentUserService;
import com.grt.util.DataAccessException;
import com.grt.util.DynamicMailContentRenderer;
import com.grt.util.GRTConstants;
import com.grt.util.MailUtil;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationRequestAlertConvertor;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.SRRequestStatusEnum;
import com.grt.util.StatusEnum;
import com.grt.util.TechRecordEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;
import com.grt.util.RegistrationTypeEnum;



public class BaseRegistrationService {
	private static final Logger logger = Logger.getLogger(BaseRegistrationService.class);
	/**
	 * Injected properties in applicationContext.xml
	 */
	private SiebelClient siebelClient;
	private SapClient sapClient;
	private GenericClient genericClient; 
	private MailUtil mailUtil;
	public String vCodesList;
	public ARTClient artClient;
	public String ossnoIP;
	public String artServiceName;
	public String srCreationGenericContactEmail;
	public String srCreationGenericContactPhone;
	public String srCreationGenericContactFName;
	public String srCreationGenericContactLName;
	public String siebelUpdateQueue;
	private TechnicalOrderDetailWorsheetProcessor worksheetProcessor;
	private String userId = "System";
	private static Map<String, CompatibilityMatrix> seCodeCompatibilityMap = new HashMap<String, CompatibilityMatrix>();
	public static Long cmLastFetch = System.currentTimeMillis();
	public static final String DATE_FORMAT_APPEND_ATTACHMENT_NAME = "yyyyMMddHHmmss";
	public static final String TECHNCIAL_REGISTRATION_ATTACHMENT_NAME = "TechnicalRegistrationAttachment";
	public static final String ATTACHMENT_NAME_EXTENSION = ".xls";
	public final String attachmentName = "TechnicalRegistrationAttachment";
	public static final String DEFAULT_UPLOAD_DIR = "upload";
	public static final String REGISTRATION_REQUEST = "Registration Request - ";
	public static final String EQR_ATTACHMENT_NAME = "EquipmentRemovalAttachment";
	public static final String EQM_ATTACHMENT_NAME = "EquipmentMoveAttachment";
	public static final String EQR_ACTIVECONTRACT_NAME = "EquipmentsWithActiveContract";
	public static final String EQR_SR_DESCRIPTION = "SR for handling active contract for removal of equipments under GRT registrationId:";
	public static final String EQM_SR_DESCRIPTION1 = "SR handling active contract for  Equipment/Site move from  Sold To ";	
	public static final String EQM_SR_DESCRIPTION2 = " to Sold to ";
	public static final String EQM_SR_DESCRIPTION3 = " under GRT Reg ID: "; 
	public BaseHibernateDao baseHibernateDao;
	 private CurrentUserService currentUserService;

	public Map<String, Set<String>> getEligibleAccessTypes(List<String> materialCodes)  throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getEligibleAccessTypes(List<String> materialCodes) method");
		Map<String, Set<String>> eligibleAccessTypes = new HashMap<String, Set<String>>();
		try {
			for (String materialCode : materialCodes) {
				eligibleAccessTypes.put(materialCode, this.getEligibleAccessTypes(materialCode));
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.getEligibleAccessTypes(List<String> materialCodes) : ", throwable);
		}
		logger.debug("eligibleAccessTypes"+eligibleAccessTypes.size());
		logger.debug("Exiting BaseRegistrationService.getEligibleAccessTypes(List<String> materialCodes) method");
		return eligibleAccessTypes;
	}

	public Set<String> getEligibleAccessTypes(String materialCode)  throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getEligibleAccessTypes(String materialCode) method");
		List<TRConfig> trConfigs = getBaseHibernateDao().getTRConfigData(materialCode);
		logger.debug("trConfigs "+trConfigs.size());
		/**
		 * Method for getting eligible access types
		 */
		Set<String> accessTypes = getBaseHibernateDao().getEligibleAccessTypes(trConfigs, this.initializeCompatibilityMatrixData());
		logger.debug("eee accessTypes "+accessTypes.size());
		List<ProductRelease> productReleases = this.getBaseHibernateDao().getSSLVPNProductReleases();
		logger.debug("eee productReleases "+productReleases.size());

		boolean flag = false;
		if(trConfigs != null && productReleases != null) {
			for (TRConfig config : trConfigs) {
				for (ProductRelease release : productReleases) {
					if (release != null
							&& (release.getProductType() != null && release.getProductType().equalsIgnoreCase(config.getProductType()))
							&& (release.getSeCode() != null && release.getSeCode().equalsIgnoreCase(config.getSeCode()))) {
						if(accessTypes == null ) {
							accessTypes = new HashSet<String>();
						}
						accessTypes.add(GRTConstants.ACCESS_TYPE_IPO);
						flag = true;
						break;
					}
				}
				// SSL/VPN Access Type eligibility based on special note of siebel view mapping
				if(!flag && GRTConstants.SPECIAL_NOTE_SSLVPN.equalsIgnoreCase(config.getSpecial_note())){
					accessTypes.add(GRTConstants.ACCESS_TYPE_IPO);
					logger.debug("MC:"+materialCode+" eligible for Accesstype: SSL/VPN based on Special note of Siebel View.");
				}
			}
		}
		logger.debug("Exiting BaseRegistrationService.getEligibleAccessTypes(String materialCode) method");
		return accessTypes;
	}

	/**
	 * API to get the Compatibility Matrix data
	 *
	 * @return Map<String, CompatibilityMatrix>
	 */
	public Map<String, CompatibilityMatrix> initializeCompatibilityMatrixData() {
		logger.debug("Entering BaseRegistrationService.initializeCompatibilityMatrixData()");
		try {
			if(seCodeCompatibilityMap.size() <= 0) {
				synchronized(seCodeCompatibilityMap) {
					logger.debug("Initializing data from Compatibility Matrix");
					seCodeCompatibilityMap = this.getSECodeFromCompatibilityMatrix();
					cmLastFetch = System.currentTimeMillis();
				}
			}
			synchronized(seCodeCompatibilityMap) {
				if (System.currentTimeMillis() - cmLastFetch > 30*60*1000) {
					logger.debug("Re-initializing data from Compatibility Matrix, cached data is more than 30 minutes stale.");
					seCodeCompatibilityMap.clear();
					seCodeCompatibilityMap = this.getSECodeFromCompatibilityMatrix();
					cmLastFetch = System.currentTimeMillis();
				}
			}
		} catch (DataAccessException dataAccessException) {
			logger.error("Error in fetching BaseRegistrationService.initializeCompatibilityMatrixData", dataAccessException);
		} finally {
			logger.debug("Existing initializeCompatibilityMatrixData:"+seCodeCompatibilityMap.size());
		}
		logger.debug("Exiting BaseRegistrationService.initializeCompatibilityMatrixData()");
		return seCodeCompatibilityMap;
	}

	public CompatibilityMatrix getCompatibilityMatrix(String seCode) {
		logger.debug("Entering BaseRegistrationService.getCompatibilityMatrix(String seCode)");
		try {
			if(StringUtils.isNotEmpty(seCode)) {
				Map<String, CompatibilityMatrix> compatibilityMatrix  = initializeCompatibilityMatrixData();
				if(compatibilityMatrix != null && compatibilityMatrix.size() > 0) {
					logger.debug("Exiting BaseRegistrationService.getCompatibilityMatrix(String seCode)");
					return compatibilityMatrix.get(seCode);
				}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.getCompatibilityMatrix(String seCode) : ", throwable);
		}
		logger.debug("Exiting BaseRegistrationService.getCompatibilityMatrix(String seCode)");
		return null;
	} 

	/**
	 * Get the SECode from Compatibility Matrix.
	 *
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, CompatibilityMatrix> getSECodeFromCompatibilityMatrix () throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.getSECodeFromCompatibilityMatrix()");
		/**
		 * Method for getting SECODE from COMPATIBILITY_MATRIX table
		 */
		List<CompatibilityMatrix> compatibilityMatrixList = getBaseHibernateDao().getSECodeFromCompatibilityMatrix();

		for (CompatibilityMatrix compatibilityMatrix : compatibilityMatrixList) {
			seCodeCompatibilityMap.put(compatibilityMatrix.getSeCode(), compatibilityMatrix);
		}
		logger.debug("Exiting BaseRegistrationService.getSECodeFromCompatibilityMatrix()");

		return seCodeCompatibilityMap;
	}

	public String saveTechnicalRegistration (TechnicalRegistration technicalRegistration) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.saveTechnicalRegistration() ");
		/**
		 * Method for saving technical registration for SAL gateway
		 */
		return getBaseHibernateDao().saveTechnicalRegistrationForSALGateway (technicalRegistration);

	}

	public String getGroupId(String materialCode, String seCode) {
		logger.debug("Entering BaseRegistrationService.saveTechnicalRegistration() ");
		logger.debug("Entering getGroupId for materialCode:" + materialCode + " seCode:" + seCode);
		String groupId = null;
		try {
			List<TRConfig> tRConfigs = getBaseHibernateDao().getTRConfigData(materialCode);
			if(tRConfigs != null && tRConfigs.size() >0) {
				for(TRConfig trConfig : tRConfigs) {
					if(seCode.equals(trConfig.getSeCode()) && materialCode.equals(trConfig.getParentMaterialCode())) {
						if(StringUtils.isEmpty(groupId)) {
							groupId = trConfig.getGroupId();
							logger.debug("MaterialCode:" + materialCode + " and seCode:" + seCode + " matched with more than one groups");
						} else {
							logger.warn("MaterialCode:" + materialCode + " and seCode:" + seCode + " matched with more than one groups:[" + groupId + ", " + trConfig.getGroupId() +" ...]" );
							break;
						}
					}
				}
				if(StringUtils.isEmpty(groupId)) {
					logger.warn("BaseRegistrationService.saveTechnicalRegistration() : No Matching groupId found for MaterialCode:" + materialCode + " and seCode:" + seCode);
				}
			} else {
				logger.warn("BaseRegistrationService.saveTechnicalRegistration() : No Matching Group found for materialCode:" + materialCode);
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.saveTechnicalRegistration() : ", throwable);
		} finally {
			logger.debug("Exiting getGroupId for materialCode:" + materialCode + " seCode:" + seCode);
		}
		logger.debug("Exiting BaseRegistrationService.saveTechnicalRegistration() ");
		return groupId;
	}

	public String autoGenAttachmentPath(String attachmentName) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				DATE_FORMAT_APPEND_ATTACHMENT_NAME);
		String generateAttachmentInPath = getUploadPath(attachmentName
				+ sdf.format(date) + ATTACHMENT_NAME_EXTENSION);
		return generateAttachmentInPath;
	}

	public String getUploadPath(String filename) {
		String uploadDir="";
		try {
			uploadDir = BaseRegistrationService.class.getClassLoader().getResource("/").getPath();
		} catch (Throwable throwable2) {
			logger.error("BaseRegistrationService.getUploadPath(String filename) : " + throwable2);
		}
		if (uploadDir != null) {
			uploadDir += DEFAULT_UPLOAD_DIR;
			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			return uploadDir + "/" + filename;
		}
		return filename;
	}

	public void processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, ProcessStepEnum processStep) throws Exception {
		logger.debug("Entering BaseRegistrationService.processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, ProcessStepEnum processStep) ");		
		try {
			if(StringUtils.isNotEmpty(srRequestDto.getSiebelSRNo())) {
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				if (GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr
						.getStatus())) {
					srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
					SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
					/**
					 * Method for updating SR Request
					 */
					getBaseHibernateDao().updateSRRequest(srRequest);
				}
			}

			if(SRRequestStatusEnum.INITIATION.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				ServiceRequest sr = createSiebelSR(registrationId, soldToId, processStep);
				srRequestDto.setSiebelSRNo(sr.getSrNumber());
				srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.SR_CREATED.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				for (Attachment att:attachmentList){
					/**
					 * Method for creating attachment for Siebel SR request
					 */
					this.createAttachmentForSiebelSR(srRequestDto.getSiebelSRNo(), att);
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				if(GRTConstants.SIEBEL_SR_STATUS_NEW.equalsIgnoreCase(sr.getStatus())) {
					getSiebelClient().assignSR(srRequestDto.getSiebelSRNo());
				}
				else if(GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr.getStatus())) {
					getSiebelClient().updateSR(srRequestDto.getSiebelSRNo());
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.SUCCESS.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}
		}
		catch (Exception ex) {
			logger.error("Unexpected exception while processing registration: " + registrationId, ex);
			sendSystemAlert(registrationId, srRequestDto);
			throw ex;
		}
		logger.debug("Exiting BaseRegistrationService.processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, ProcessStepEnum processStep) ");
	}

	/**
	 * Send System Alert with the given SiteRegistration
	 *
	 * @param registrationId String
	 * @param srRequestDto SRRequestDto
	 * @throws Exception
	 */
	public void sendSystemAlert(String registrationId, SRRequestDto srRequestDto)
			throws Exception {
		logger.debug("Entering BaseRegistrationService.sendSystemAlert(String registrationId, SRRequestDto srRequestDto) ");
		logger.debug("Starting for Registration ID [" + registrationId + "]");
		SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);

		RegistrationRequestAlert result = RegistrationRequestAlertConvertor
				.convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);
		if(srRequestDto != null) {
			result.setSrRequestStatusId(srRequestDto.getStatusId());
		}
		result.setIsSystemMail(true);
		result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_SIEBEL);
		result = populateActionRequired(result);
		mailUtil.sendMailNotification(result);
		logger.debug("Completed for Registration ID [" + registrationId + "]");
		logger.debug("Exiting BaseRegistrationService.sendSystemAlert(String registrationId, SRRequestDto srRequestDto) ");
	}

	/**
	 * Send System Alert for a given Technical Registration
	 *
	 * @param registrationId String
	 * @param destination destination
	 * @param errorMsg errorMsg
	 * @throws Exception
	 */
	public void sendSystemAlert(String registrationId, String destination, String errorMsg)
			throws Exception {
		logger.debug("Entering BaseRegistrationService.sendSystemAlert(String registrationId, String destination, String errorMsg)");
		logger.debug("Sending System Alert for Registration ID [" + registrationId + "]");
		RegistrationRequestAlert result = null;
		TechnicalRegistration techRegistration = getBaseHibernateDao().getTechnicalRegistration(registrationId);
		SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);

		if (techRegistration != null) {
			result = RegistrationRequestAlertConvertor.convert(techRegistration);
		} else if (siteRegistration != null) {
			result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION);
		}

		result.setIsSystemMail(true);
		if (destination != null && destination.equals(GRTConstants.SYSTEM_MAIL_DESTINATION_SIEBEL)) {
			result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_SIEBEL);
		} else if (destination != null && destination.equals(GRTConstants.SYSTEM_MAIL_DESTINATION_FMW)) {
			result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_FMW);
		} else if (destination != null && destination.equals(GRTConstants.SYSTEM_MAIL_DESTINATION_ART)) {
			result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_ART);
		}
		result = populateActionRequired(result);
		result.setActionRequired(errorMsg);
		getMailUtil().sendMailNotification(result);
		logger.debug("Completed for Registration ID [" + registrationId + "]");
		logger.debug("Exiting BaseRegistrationService.sendSystemAlert(String registrationId, String destination, String errorMsg)");
	}

	private RegistrationRequestAlert populateActionRequired(RegistrationRequestAlert result) {
		logger.debug("Entering BaseRegistrationService.populateActionRequired(RegistrationRequestAlert result)");
		String siebelSRNumber = result.getSiebelSRNumber();
		String status = result.getStatus();
		String actionRequired = GRTConstants.SIEBEL_SR_NO_NOTE_FOUND;
		if(siebelSRNumber != null && 
				(result.getIsSystemMail() != null && result.getIsSystemMail() || 
				StatusEnum.AWAITINGINFO.getStatusShortDescription().equalsIgnoreCase(status))) {
			try {	
				Activity latestPublicSRNoteActivity = getSiebelClient().queryLatestPublicSRNoteActivity(siebelSRNumber);
				if(latestPublicSRNoteActivity != null) {
					actionRequired = latestPublicSRNoteActivity.getDescription();
				}
			} catch (Exception e) {
				logger.error("SiebelService.queryLatestPublicSRNoteActivity failure", e);
			}
		}
		result.setActionRequired(actionRequired);
		logger.debug("Exiting BaseRegistrationService.populateActionRequired(RegistrationRequestAlert result)");
		return result;
	}

	/**
	 * Create SiebelSR for the given soldToNumber
	 *
	 * @param registrationId
	 *            String
	 * @param soldToNumber
	 *            String
	 * @return ServiceRequest
	 * @throws Exception
	 */
	public ServiceRequest createSiebelSR(String registrationId,
			String soldToNumber, ProcessStepEnum processStepEnum
			) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.createSiebelSR(String registrationId, String soldToNumber, ProcessStepEnum processStepEnum)");
		//RegistrationSummary registrationSummary = getRegistrationSummary(registrationId);

		String description = REGISTRATION_REQUEST + processStepEnum.getProcessStepShortDescription();

		logger.debug("create SiebelSR for soldToNumber: " + soldToNumber);
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setAccountLocation(soldToNumber);

		String srSubType = null;
		if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId().equalsIgnoreCase(processStepEnum.getProcessStepId())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION;
		}
		else if(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId().equalsIgnoreCase(processStepEnum.getProcessStepId())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION;
		}
		else if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId().equalsIgnoreCase(processStepEnum.getProcessStepId())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION;
		}
		else if(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId().equalsIgnoreCase(processStepEnum.getProcessStepId())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE;
		}
		serviceRequest.setSrSubType(srSubType);
		serviceRequest.setDescription(description);
		serviceRequest.setUrgency(GRTConstants.SIEBEL_SR_URGENCY_4_LOW);
		serviceRequest.setCustomerTicketNumber(registrationId);

		/*String emails = registrationSummary.getOnsiteEmail();
        String contactEmail = getPrimaryEmail(emails);
        serviceRequest.setContactEmail(contactEmail);
        serviceRequest.setContactFirstName(registrationSummary.getOnsiteFirstName() == null ?
        		null : registrationSummary.getOnsiteFirstName().trim());
        serviceRequest.setContactLastName(registrationSummary.getOnsiteLastName() == null ?
        		null : registrationSummary.getOnsiteLastName().trim());
        serviceRequest.setContactBusinessPhone(registrationSummary.getOnsitePhone() == null ?
        		null : registrationSummary.getOnsitePhone().trim());*/

		try {
			//Use Generic Siebel user for SR creation.
			serviceRequest.setContactEmail(getSrCreationGenericContactEmail());
			serviceRequest.setContactFirstName(getSrCreationGenericContactFName());
			serviceRequest.setContactLastName(getSrCreationGenericContactLName());
			serviceRequest.setContactBusinessPhone(getSrCreationGenericContactPhone());
			serviceRequest = getSiebelClient().createSR(serviceRequest);
		} catch (Exception ex) {
			throw new DataAccessException(BaseRegistrationService.class, ex.getMessage(), ex);
		}

		logger.debug("Exiting BaseRegistrationService.createSiebelSR(String registrationId, String soldToNumber, ProcessStepEnum processStepEnum)");

		return serviceRequest;
	}

	public ServiceRequest createSiebelSR(String siteRegistrationId, String soldToNumber, TechRecordEnum type) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.createSiebelSR(String siteRegistrationId, String soldToNumber, TechRecordEnum type)");

		String description = REGISTRATION_REQUEST + ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription();
		logger.debug("create SiebelSR for soldToNumber: " + soldToNumber);

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setAccountLocation(soldToNumber);
		serviceRequest.setDescription(description);
		String srSubType = null;
		if (type.getSalType().equals(TechRecordEnum.TR.getType())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION;
		} else if (type.getSalType().equals(TechRecordEnum.ALARM.getSalType())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_CONNECTIVITY_AND_ALARM_TESTING;
		} else {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_RECORDS_BUILDING;
		}
		serviceRequest.setSrSubType(srSubType);
		serviceRequest.setUrgency(GRTConstants.SIEBEL_SR_URGENCY_4_LOW);
		serviceRequest.setCustomerTicketNumber(siteRegistrationId);
		try {
			//Use Generic Siebel user for SR creation.
			serviceRequest.setContactEmail(getSrCreationGenericContactEmail());
			serviceRequest.setContactFirstName(getSrCreationGenericContactFName());
			serviceRequest.setContactLastName(getSrCreationGenericContactLName());
			serviceRequest.setContactBusinessPhone(getSrCreationGenericContactPhone());
			serviceRequest = getSiebelClient().createSR(serviceRequest);
		} catch (Exception ex) {
			throw new DataAccessException(BaseRegistrationService.class, ex
					.getMessage(), ex);
		}

		logger.debug("Exiting BaseRegistrationService.createSiebelSR(String siteRegistrationId, String soldToNumber, TechRecordEnum type)");

		return serviceRequest;
	}


	public ServiceRequest createSiebelSRForSal(SalProductRegistration salProductRegistration, String soldToNumber, TechRecordEnum type) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.createSiebelSRForSal(SalProductRegistration salProductRegistration, String soldToNumber, TechRecordEnum type)");

		RegistrationSummary registrationSummary = TechnicalRegistrationUtil.getRegistrationSummarySRCreation(salProductRegistration, type);

		String description = REGISTRATION_REQUEST + ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepShortDescription();
		logger.debug("create SiebelSR for soldToNumber: " + soldToNumber);

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setAccountLocation(soldToNumber);
		serviceRequest.setDescription(description);
		String srSubType = null;
		if (type.getSalType().equals(TechRecordEnum.IP_MODEM.getSalType()) || type.getSalType().equals(TechRecordEnum.IPO.getSalType())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_TECHNICAL_REGISTRATION;
		} else if (type.getSalType().equals(TechRecordEnum.ALARM.getSalType())) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_CONNECTIVITY_AND_ALARM_TESTING;
		} else {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_SAL_RECORDS_BUILDING;
		}
		serviceRequest.setSrSubType(srSubType);

		serviceRequest.setUrgency(GRTConstants.SIEBEL_SR_URGENCY_4_LOW);
		serviceRequest.setCustomerTicketNumber(registrationSummary.getRegistrationId());

		/*String emails = registrationSummary.getOnsiteEmail();
        String contactEmail = getPrimaryEmail(emails);
        serviceRequest.setContactEmail(contactEmail);
        serviceRequest.setContactFirstName(registrationSummary.getOnsiteFirstName() == null ?
        		null : registrationSummary.getOnsiteFirstName().trim());
        serviceRequest.setContactLastName(registrationSummary.getOnsiteLastName() == null ?
        		null : registrationSummary.getOnsiteLastName().trim());
        serviceRequest.setContactBusinessPhone(registrationSummary.getOnsitePhone() == null ?
        		null : registrationSummary.getOnsitePhone().trim());*/

		try {
			//        	Use Generic Siebel user for SR creation.
			serviceRequest.setContactEmail(getSrCreationGenericContactEmail());
			serviceRequest.setContactFirstName(getSrCreationGenericContactFName());
			serviceRequest.setContactLastName(getSrCreationGenericContactLName());
			serviceRequest.setContactBusinessPhone(getSrCreationGenericContactPhone());
			serviceRequest = getSiebelClient().createSR(serviceRequest);
		} catch (Exception ex) {
			throw new DataAccessException(BaseRegistrationService.class, ex
					.getMessage(), ex);
		}

		logger.debug("Exitiing BaseRegistrationService.createSiebelSRForSal(SalProductRegistration salProductRegistration, String soldToNumber, TechRecordEnum type)");

		return serviceRequest;
	}


	public List<Set<TechnicalOrder>> fetchTechnicalOrders(String registrationId, String orderType, List<String> mcCodeList) {
		logger.debug("Entering BaseRegistrationService.fetchTechnicalOrders(String registrationId, String orderType, List<String> mcCodeList)");
		List<Set<TechnicalOrder>> techList = new ArrayList<Set<TechnicalOrder>>();
		Set<TechnicalOrder> techOrderSet = new HashSet<TechnicalOrder>();
		Set<TechnicalOrder> techOrderPendingSet = new HashSet<TechnicalOrder>();
		try {
			List<TechnicalOrder> techOrderList = getBaseHibernateDao().getTechnicalOrderByType(registrationId, orderType);
			if(techOrderList != null && techOrderList.size() > 0){
				for(TechnicalOrder techOrder : techOrderList){
					if(techOrder.getMaterialCode() != null && mcCodeList.contains(techOrder.getMaterialCode())){
						techOrderSet.add(techOrder);
					} else {
						techOrderPendingSet.add(techOrder);
					}
				} 
				techList.add(techOrderSet);
				techList.add(techOrderPendingSet);
			}
		} catch (DataAccessException e) {
			logger.debug("Exception in BaseRegistrationService.fetchTechnicalOrders(String registrationId, String orderType, List<String> mcCodeList):  "+e.getMessage());
			e.printStackTrace();
		}
		
		logger.debug("Exiting BaseRegistrationService.fetchTechnicalOrders(String registrationId, String orderType, List<String> mcCodeList)");
		return techList;
	}

	/**
	 * Send RegistrationRequestAlert for SiteRegistration
	 *
	 * @param registrationId String
	 * @param status StatusEnum
	 * @throws Exception
	 */
	public void sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status)
			throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status)");
		logger.debug("sendRegistrationRequestAlert starting for Registration ID [" 
				+ registrationId + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
		if(processStep.equals(ProcessStepEnum.FINAL_RECORD_VALIDATION)) {
			getBaseHibernateDao().refreshSiteRegistration(siteRegistration);
		}
		RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, processStep);
		if(status != null) {
			result.setStatus(status.getStatusShortDescription());
		}
		result = populateActionRequired(result);
		
		//START :: GRT 4.0 Change : Get SR number for Retest records(QA Defect)
		if(processStep.equals(ProcessStepEnum.TECHNICAL_REGISTRATION)) {
			List<TechnicalRegistration> techRegs = getBaseHibernateDao().fetchRetestRecords(siteRegistration, false);
			//If retest records are present
			if( techRegs!=null && !techRegs.isEmpty() ){
				TechnicalRegistration techReg = techRegs.get(0);
				SRRequest srRequest = techReg.getSrRequest();
				if( techReg.getSrRequest()!=null && !StringUtils.isEmpty(srRequest.getSiebelSRNo()) ){
					result.setSiebelSRNumber(srRequest.getSiebelSRNo());
				}else{
					result.setSiebelSRNumber(GRTConstants.SIEBEL_SR_NUMBER_NOT_FOUND);
				}
			}
		}
		//END :: 
		
		mailUtil.sendMailNotification(result);
		logger.debug("sendRegistrationRequestAlert exiting for Registration ID [" 
				+ siteRegistration.getRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");

		logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status)");
	}

	/**
	 * GRT 4.0 Changes : Added new method for sending mail for Alarming & Connectivity
	 * Send RegistrationRequestAlert for SiteRegistration
	 *
	 * @param registrationId String
	 * @param status StatusEnum
	 * @throws Exception
	 */
	public void sendRegistrationRequestAlertForAlarming(String registrationId, ProcessStepEnum processStep, StatusEnum status)
			throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status)");
		logger.debug("sendRegistrationRequestAlert starting for Registration ID [" 
				+ registrationId + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
		RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, processStep);
		if(status != null) {
			result.setStatus(status.getStatusShortDescription());
		}
		result = populateActionRequired(result);
		
		//START :: GRT 4.0 Change : Get SR number for Retest records(QA Defect)
		if(processStep.equals(ProcessStepEnum.TECHNICAL_REGISTRATION)) {
			List<TechnicalRegistration> techRegs = getBaseHibernateDao().fetchRetestRecords(siteRegistration, false);
			//If retest records are present
			if( techRegs!=null && !techRegs.isEmpty() ){
				TechnicalRegistration techReg = techRegs.get(0);
				SRRequest srRequest = techReg.getSrRequest();
				if( techReg.getSrRequest()!=null && !StringUtils.isEmpty(srRequest.getSiebelSRNo()) ){
					result.setSiebelSRNumber(srRequest.getSiebelSRNo());
				}else{
					result.setSiebelSRNumber(GRTConstants.SIEBEL_SR_NUMBER_NOT_FOUND);
				}
			}
		}
		//END :: 
		
		result.setSalAlarmConMail(true);
		
		mailUtil.sendMailNotification(result);
		logger.debug("sendRegistrationRequestAlert exiting for Registration ID [" 
				+ siteRegistration.getRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");

		logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status)");
	}
	
	
	/**
	 * Send RegistrationRequestAlert for SiteRegistration
	 *
	 * @param registrationId String
	 * @param status StatusEnum
	 * @throws Exception
	 */
	public void sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status, Set<String> errors)
			throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status, Set<String> errors)");
		logger.debug("sendRegistrationRequestAlert starting for Registration ID [" 
				+ registrationId + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
		if(processStep.equals(ProcessStepEnum.FINAL_RECORD_VALIDATION)) {
			getBaseHibernateDao().refreshSiteRegistration(siteRegistration);
		}
		RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, processStep);
		if(errors != null && errors.size() > 0) {
			String errorString = "";
			for (String error : errors) {
				errorString += error + "<br>";
			}
			result.setErrorDescription(errorString);
		} else {
			result.setErrorDescription(" ");
		}
		if(status != null) {
			result.setStatus(status.getStatusShortDescription());
		}
		result = populateActionRequired(result);
		mailUtil.sendMailNotification(result);
		logger.debug("sendRegistrationRequestAlert exiting for Registration ID [" 
				+ siteRegistration.getRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlert(String registrationId, ProcessStepEnum processStep, StatusEnum status, Set<String> errors)");
		
	}

	public void sendRegistrationRequestAlertForStepBCompletion(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, String siebelSRNumber) throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlertForStepBCompletion(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, String siebelSRNumber)");
		try {
			siteRegistration.setStepBCompletedAction(true);
			RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);

			String dynamicData = DynamicMailContentRenderer.getDynamicDataForStepBCompletion(trs, sls);
			result.setDynamicData(dynamicData);
			result.setSiebelSRNumber(siebelSRNumber);
			result = populateActionRequired(result);
			mailUtil.sendMailNotification(result);
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.sendRegistrationRequestAlertForStepBCompletion(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, String siebelSRNumber) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlertForStepBCompletion(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, String siebelSRNumber)");
		}
	}

	public void sendRegistrationRequestAlertForStepBCancellation(SiteRegistration siteRegistration) throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlertForStepBCancellation(SiteRegistration siteRegistration)");
		try {
			RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);
			result = populateActionRequired(result);
			result.setStepBCancellation(true);
			mailUtil.sendMailNotification(result);
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.sendRegistrationRequestAlertForStepBCancellation(SiteRegistration siteRegistration) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlertForStepBCancellation(SiteRegistration siteRegistration)");
		}
	}



	public PipelineSapTransactions populateSapTransactionDetails(String registrationId, String serialNumber, InstallBaseRespDataDto ibDto,
			Date dateTime, Date ibSubmittedDate){
		logger.debug("Entering BaseRegistrationService.populateSapTransactionDetails(String registrationId, String serialNumber, InstallBaseRespDataDto ibDto, Date dateTime, Date ibSubmittedDate)");
		PipelineSapTransactions pipelineSapTransactions = new PipelineSapTransactions();
		pipelineSapTransactions.setRegistrationId(registrationId);
		pipelineSapTransactions.setShipTo(ibDto.getShipToId());
		String materialCode = ibDto.getMaterialCode();
		pipelineSapTransactions.setMaterialCode(materialCode);
		Map<String, Set<String>> mc2GroupIdMappings=getBaseHibernateDao().initializeTRConfigData();
		logger.debug("SiebelDao.mc2GroupIdMappings.size()::: " + getBaseHibernateDao().getMc2GroupIdMappings().size());
		if (!getBaseHibernateDao().getMc2GroupIdMappings().isEmpty() && getBaseHibernateDao().getMc2GroupIdMappings().containsKey(materialCode)) {
			pipelineSapTransactions.setTechnicallyRegisterable(true);
		}
		pipelineSapTransactions.setSerialNumber(serialNumber);
		pipelineSapTransactions.setEquipmentNumber(ibDto.getAssetNumber());
		pipelineSapTransactions.setQuantity(Long.parseLong(ibDto.getQuantity()));
		pipelineSapTransactions.setAction(GRTConstants.TECH_ORDER_TYPE_IB);
		pipelineSapTransactions.setDateTime(dateTime);
		pipelineSapTransactions.setProcessed(false);
		pipelineSapTransactions.setIbSubmittedDate(ibSubmittedDate);
		pipelineSapTransactions.setSapCompleted(true);
		pipelineSapTransactions.setBeforeQuantity(ibDto.getBeforeQuantity());
		pipelineSapTransactions.setAfterQuantity(ibDto.getAfterQuantity());
		logger.debug("Exiting BaseRegistrationService.populateSapTransactionDetails(String registrationId, String serialNumber, InstallBaseRespDataDto ibDto, Date dateTime, Date ibSubmittedDate)");
		return pipelineSapTransactions;
	}

	public void srCreationForEquipmentsWithActiveContract(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract,
			String orderType) throws Exception{
		logger.debug("Entering BaseRegistrationService.srCreationForEquipmentsWithActiveContract(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract)");
		long c1 = Calendar.getInstance().getTimeInMillis();
		
		// SR - creation logic starts
		SRRequestDto srRequestDto = null;
		//Generating file path
		String filePath = autoGenAttachmentPath(EQR_ACTIVECONTRACT_NAME);
		// Generating worksheet
		TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
		List<Attachment> attList = new ArrayList<Attachment>();
		Attachment attachment = worksheet.generateActiveContractsWorksheet(equipmentsWithActiveContract, filePath, siteRegistration.getCompany(),
				siteRegistration.getLastName()+", "+siteRegistration.getFirstName(),siteRegistration.getReportEmailId(),
				siteRegistration.getOnsiteLastName()+", "+siteRegistration.getOnsiteFirstName(),siteRegistration.getOnsiteEmail());
		attList.add(attachment);

		SRRequest srRequest = null;	
		
		try {
			srRequest = getBaseHibernateDao().initSRRequestForActiveContracts(siteRegistration);
		} catch (DataAccessException e) {
			logger.error("Exception in BaseRegistrationService.srCreationForEquipmentsWithActiveContract(SiteRegistration siteRegistration, "
					+ "List<TechnicalOrderDetail> equipmentsWithActiveContract) ----Unable to create an SR Request Id on table----", e);
			throw e;
		}

		if (srRequest != null) {
			srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
			processSRRequest(siteRegistration.getRegistrationId(), siteRegistration.getSoldToId(), attList, srRequestDto, orderType);
		}
		logger.debug("Before updating error description and SR_Created flag");
		if(equipmentsWithActiveContract.size() > 0){
			//Implement this method in in BaseHibernateDao
			getBaseHibernateDao().updateTechnicalOrderErrorDescription(equipmentsWithActiveContract); 
		}
		logger.debug("Setting IsSrCompleted to no on SiteRegistration");
		//getRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, GRTConstants.NO);

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for SR creation of Equipments with Active contract for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :" + (c2-c1));
		logger.debug("Exiting BaseRegistrationService.srCreationForEquipmentsWithActiveContract(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract)");
	}

	public void srCreationForEquipmentsWithActiveContractEM(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract,
			String orderType) throws Exception{
		logger.debug("Entering BaseRegistrationService.srCreationForEquipmentsWithActiveContractEM(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract)");
		long c1 = Calendar.getInstance().getTimeInMillis();
		
		// SR - creation logic starts
		SRRequestDto srRequestDto = null;
		//Generating file path
		String filePath = autoGenAttachmentPath(EQR_ACTIVECONTRACT_NAME);
		// Generating worksheet
		TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
		List<Attachment> attList = new ArrayList<Attachment>();
		Attachment attachment = worksheet.generateActiveContractsWorksheetEM(equipmentsWithActiveContract, filePath, siteRegistration.getCompany(),
				siteRegistration.getLastName()+", "+siteRegistration.getFirstName(),siteRegistration.getReportEmailId(),
				siteRegistration.getOnsiteLastName()+", "+siteRegistration.getOnsiteFirstName(),siteRegistration.getOnsiteEmail());
		attList.add(attachment);

		SRRequest srRequest = null;	
		
		try {
			srRequest = getBaseHibernateDao().initSRRequestForActiveContracts(siteRegistration);
		} catch (DataAccessException e) {
			logger.error("Exception in BaseRegistrationService.srCreationForEquipmentsWithActiveContractEM(SiteRegistration siteRegistration, "
					+ "List<TechnicalOrderDetail> equipmentsWithActiveContract) ----Unable to create an SR Request Id on table----", e);
			throw e;
		}

		if (srRequest != null) {
			srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
			processSRRequest(siteRegistration.getRegistrationId(), siteRegistration.getSoldToId(), attList, srRequestDto, orderType);
		}
		logger.debug("Before updating error description and SR_Created flag");
		if(equipmentsWithActiveContract.size() > 0){
			//Implement this method in in BaseHibernateDao
			getBaseHibernateDao().updateTechnicalOrderErrorDescription(equipmentsWithActiveContract); 
		}
		logger.debug("Setting IsSrCompleted to no on SiteRegistration");
		//getRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, GRTConstants.NO);

		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER for SR creation of Equipments with Active contract for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :" + (c2-c1));
		logger.debug("Exiting BaseRegistrationService.srCreationForEquipmentsWithActiveContractEM(SiteRegistration siteRegistration, List<TechnicalOrderDetail> equipmentsWithActiveContract)");
	}

	
	
	
	public void processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, String orderType) throws Exception {
		logger.debug("Entering BaseRegistrationService.processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto)");
		try {
			if(StringUtils.isNotEmpty(srRequestDto.getSiebelSRNo())) {
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				if (GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr
						.getStatus())) {
					srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
					SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
					/**
					 * Method for updating SR Request
					 */
					getBaseHibernateDao().updateSRRequest(srRequest);
				}
			}

			if(SRRequestStatusEnum.INITIATION.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				ServiceRequest sr = createSiebelSRForActiveContracts(registrationId, soldToId, orderType);
				srRequestDto.setSiebelSRNo(sr.getSrNumber());
				srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.SR_CREATED.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				for (Attachment att:attachmentList){
					createAttachmentForSiebelSR(srRequestDto.getSiebelSRNo(), att);
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				if(GRTConstants.SIEBEL_SR_STATUS_NEW.equalsIgnoreCase(sr.getStatus())) {
					getSiebelClient().assignSR(srRequestDto.getSiebelSRNo());
				}
				else if(GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr.getStatus())) {
					getSiebelClient().updateSR(srRequestDto.getSiebelSRNo());
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.SUCCESS.getStatusId());
				SRRequest srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}
		}
		catch (Exception ex) {
			logger.error("Exception in BaseRegistrationService.processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto) : " + registrationId, ex);
			sendSystemAlert(registrationId, srRequestDto);
			throw ex;
		}
		logger.debug("Exiting BaseRegistrationService.processSRRequest(String registrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto)");
	}

	/**
	 * Create attachment for the given SiebelSR
	 *
	 * @param srId
	 *            String
	 * @param attachment
	 *            Attachment
	 * @throws Exception
	 */
	public Attachment createAttachmentForSiebelSR(String srNum,
			Attachment attachment) throws Exception {
		logger.debug("Entering BaseRegistrationService.createAttachmentForSiebelSR(String srNum, Attachment attachment)");
		ServiceRequest sr = getSiebelClient().querySR(srNum);
		logger.debug("SR Number:"+sr.getSrNumber());
		logger.debug("FileName:"+attachment.getFileName());
		logger.debug("FielSize:"+attachment.getFileSize());

		String srId = sr.getSrId();
		Attachment at = getSiebelClient().createAttachment(srId,
				attachment.getFileName(), attachment.getFileExtension(),
				attachment.getFileContents());

		logger.debug("Exiting BaseRegistrationService.createAttachmentForSiebelSR(String srNum, Attachment attachment)");
		return at;
	}

	public ServiceRequest createSiebelSRForActiveContracts(String registrationId, String soldToNumber, String orderType) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.createSiebelSRForActiveContracts(String registrationId, String soldToNumber)");
		String description = EQR_SR_DESCRIPTION + registrationId;
		String toSoldTo = null;
		logger.debug("create SiebelSR for soldToNumber: " + soldToNumber);
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setAccountLocation(soldToNumber);

		String srSubType = null;
		srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_EQR_ACTIVE_CONTRACTS;
		if (GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType)) {
			srSubType = GRTConstants.SIEBEL_SR_SUB_TYPE_EQM_ACTIVE_CONTRACTS;
			SiteRegistration siteRegistration  = getBaseHibernateDao().getSiteRegistration(registrationId);
			if( siteRegistration != null ){
				toSoldTo = siteRegistration.getToSoldToId();
			}
			description = EQM_SR_DESCRIPTION1+soldToNumber+EQM_SR_DESCRIPTION2+toSoldTo+EQM_SR_DESCRIPTION3+registrationId;
		}
		serviceRequest.setSrSubType(srSubType);
		serviceRequest.setDescription(description);
		serviceRequest.setUrgency(GRTConstants.SIEBEL_SR_URGENCY_4_LOW);
		serviceRequest.setCustomerTicketNumber(registrationId);

		try {
			// Use Generic Siebel user for SR creation.
			serviceRequest.setContactEmail(getSrCreationGenericContactEmail());
			serviceRequest.setContactFirstName(getSrCreationGenericContactFName());
			serviceRequest.setContactLastName(getSrCreationGenericContactLName());
			serviceRequest.setContactBusinessPhone(getSrCreationGenericContactPhone());
			serviceRequest = getSiebelClient().createSR(serviceRequest);
		} catch (Exception ex) {
			logger.error("Data Exception in BaseRegistrationService.createSiebelSRForActiveContracts(String registrationId, String soldToNumber) :"+ex.getMessage());
			throw new DataAccessException(BaseRegistrationService.class, ex.getMessage(), ex);
		}
		logger.debug("Exiting BaseRegistrationService.createSiebelSRForActiveContracts(String registrationId, String soldToNumber)");
		return serviceRequest;
	}

	public List<TechnicalOrderDetail> prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract)");
		List<TechnicalOrderDetail> mainTechList = new ArrayList<TechnicalOrderDetail>();
		List<TechnicalOrderDetail> childTechList = null;
		List<TechnicalOrderDetail> queriedTechList = null;
		try {
			for(TechnicalOrderDetail technicalOrderDetail : equipmentsWithActiveContract){
				childTechList = new ArrayList<TechnicalOrderDetail>();
				logger.debug("MC:"+technicalOrderDetail.getMaterialCode()+"    SID:"+technicalOrderDetail.getSid()+"     MID:"+technicalOrderDetail.getMid());
				if(StringUtils.isNotEmpty(technicalOrderDetail.getSid()) && StringUtils.isNotEmpty(technicalOrderDetail.getMid())){
					/**
					 * Method for getting equipment with same SID MID data
					 */
					queriedTechList = getBaseHibernateDao().fetchEquipmentsWithSameSIDnMID(technicalOrderDetail);
					if(queriedTechList != null && queriedTechList.size() > 0){
						childTechList.addAll(queriedTechList);
					} else {
						childTechList.add(technicalOrderDetail);
					}
				} else {
					childTechList.add(technicalOrderDetail);
				}
				for(TechnicalOrderDetail childTOD : childTechList){
					//if(childTOD.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
					logger.debug("Child MC:"+childTOD.getMaterialCode());
					String mainGroupId = technicalOrderDetail.getGroupId()!= null?technicalOrderDetail.getGroupId():"";
					String childGroupId = childTOD.getGroupId()!= null?childTOD.getGroupId():"";
					logger.debug("ChildGroupId:"+childGroupId + "    MainGroupId:"+mainGroupId);
					if(StringUtils.isEmpty(childTOD.getEquipmentNumber()) 
							|| childTOD.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getEquipmentNumber())
							|| (GRTConstants.VCODE_V00328.equalsIgnoreCase(childTOD.getMaterialCode()) 
									&& (StringUtils.isEmpty(childGroupId) || (mainGroupId.equalsIgnoreCase(childGroupId))))){
						logger.debug("Matching... Child EQN:"+childTOD.getEquipmentNumber());
						childTOD.setSummaryEquipmentNumber("");
						childTOD.setEquipmentNumber("");
						childTOD.setStatusId("Inactive");
						childTOD.setRemainingQuantity(0L);
					}
					/* else {
	    				childTOD.setStatusId("Active");
	    			}*/
					/* We dont have to blank out SEID, SID or MID in Siebel.
					 * childTOD.setSolutionElementId("");
	    			childTOD.setSid("");
	    			childTOD.setMid("");*/
				}
				mainTechList.addAll(childTechList);
			}
		} 

		catch (DataAccessException dae) {
			logger.error("Data Exception in BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) :"+dae.getMessage());
			throw new DataAccessException(BaseRegistrationService.class, dae.getMessage(), dae);
		}  catch (Exception e) {
			logger.error("Data Exception in BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) :"+e.getMessage());
			throw new DataAccessException(BaseRegistrationService.class, e.getMessage(), e);
		}
		logger.debug("Exiting BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract)");
		return mainTechList;
	}

		//GRT 4.0 : Added for Equipment Move (Copy of prepareAssetsTobeRemovedFromSiebel method)
		public List<TechnicalOrderDetail> prepareAssetsTobeMovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) throws DataAccessException{
			logger.debug("Entering BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract)");
			List<TechnicalOrderDetail> mainTechList = new ArrayList<TechnicalOrderDetail>();
			List<TechnicalOrderDetail> childTechList = null;
			List<TechnicalOrderDetail> queriedTechList = null;
			try {
				for(TechnicalOrderDetail technicalOrderDetail : equipmentsWithActiveContract){
					childTechList = new ArrayList<TechnicalOrderDetail>();
					logger.debug("MC:"+technicalOrderDetail.getMaterialCode()+"    SID:"+technicalOrderDetail.getSid()+"     MID:"+technicalOrderDetail.getMid());
					if(StringUtils.isNotEmpty(technicalOrderDetail.getSid()) && StringUtils.isNotEmpty(technicalOrderDetail.getMid())){
						/**
						 * Method for getting equipment with same SID MID data
						 */
						queriedTechList = getBaseHibernateDao().fetchEquipmentsWithSameSIDnMID(technicalOrderDetail);
						if(queriedTechList != null && queriedTechList.size() > 0){
							childTechList.addAll(queriedTechList);
						} else {
							childTechList.add(technicalOrderDetail);
						}
					} else {
						childTechList.add(technicalOrderDetail);
					}
					//GRT 4.0 Change
					//Check if new equipment number is generated or not
					boolean newEqnGen = true;
					if( technicalOrderDetail.getSummaryEquipmentNumber()!=null && technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getToEquipmentNumber()) ){
						newEqnGen = false;
					}
					
					for(TechnicalOrderDetail childTOD : childTechList){
						//GRT 4.0 Change
						if( newEqnGen ){
							childTOD.setToEquipmentNumber(technicalOrderDetail.getToEquipmentNumber());
						}
						//if(childTOD.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
						logger.debug("Child MC:"+childTOD.getMaterialCode());
						String mainGroupId = technicalOrderDetail.getGroupId()!= null?technicalOrderDetail.getGroupId():"";
						String childGroupId = childTOD.getGroupId()!= null?childTOD.getGroupId():"";
						logger.debug("ChildGroupId:"+childGroupId + "    MainGroupId:"+mainGroupId);
						if(StringUtils.isEmpty(childTOD.getEquipmentNumber()) 
								|| childTOD.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getEquipmentNumber())
								|| (GRTConstants.VCODE_V00328.equalsIgnoreCase(childTOD.getMaterialCode()) 
										&& (StringUtils.isEmpty(childGroupId) || (mainGroupId.equalsIgnoreCase(childGroupId))))){
							logger.debug("Matching... Child EQN:"+childTOD.getEquipmentNumber());
							//childTOD.setSummaryEquipmentNumber("");
							//childTOD.setEquipmentNumber("");
							childTOD.setStatusId("Inactive");
							//childTOD.setRemainingQuantity(0L);*/
							//Defect #832 : Only add records with SAME SID, MID & EQN as it is done in Equipment Removal
							mainTechList.add(childTOD);
						}
						/* else {
		    				childTOD.setStatusId("Active");
		    			}*/
						/* We dont have to blank out SEID, SID or MID in Siebel.
						 * childTOD.setSolutionElementId("");
		    			childTOD.setSid("");
		    			childTOD.setMid("");*/
					}
					//Defect #832 : Only add records with SAME SID, MID & EQN
					//mainTechList.addAll(childTechList);
				}
			} 

			catch (DataAccessException dae) {
				logger.error("Data Exception in BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) :"+dae.getMessage());
				throw new DataAccessException(BaseRegistrationService.class, dae.getMessage(), dae);
			}  catch (Exception e) {
				logger.error("Data Exception in BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract) :"+e.getMessage());
				throw new DataAccessException(BaseRegistrationService.class, e.getMessage(), e);
			}
			logger.debug("Exiting BaseRegistrationService.prepareAssetsTobeRemovedFromSiebel(List<TechnicalOrderDetail> equipmentsWithActiveContract)");
			return mainTechList;
		}
	
	// Duplicate code added from Siebel Service:processReceivedSiebelAsset
	public void processIBRegistrations(Map<String,List<String>> regIdMap) throws Exception{
		logger.debug("Entering BaseRegistrationService.processIBRegistrations(Map<String,List<String>> regIdMap)");
		try{
			if(regIdMap != null && regIdMap.size() > 0){
				for(Entry<String, List<String>> entry : regIdMap.entrySet()){
					int flag = 0;
					String registrationId = entry.getKey();
					List<String> mcList = entry.getValue();
					logger.debug("RegistartionId:"+registrationId);
					if(StringUtils.isNotEmpty(registrationId) && mcList != null && mcList.size() > 0) {
						SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
						List<Set<TechnicalOrder>> techOrderList = fetchTechnicalOrders(registrationId, GRTConstants.TECH_ORDER_TYPE_IB, mcList);
						if(techOrderList != null && techOrderList.size() > 0){
							siteRegistration.setTechnicalOrders(techOrderList.get(0));
						}
						if(!siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) &&
								!siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())) {
							if(siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
								if(siteRegistration.getTechnicalOrders() != null && siteRegistration.getTechnicalOrders().size() > 0) {
									List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
									for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
										if(to.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_IB)) {
											if(StringUtils.isEmpty(to.getSr_Created()) || (!to.getSr_Created().equalsIgnoreCase(GRTConstants.YES))) {
												to.setOpenQuantity(0L);
												to.setRemainingQuantity(0L);
												to.setCompletedDate(new Date());
												to.setCompletedBysiteRegId(registrationId);
												updateTOs.add(to);
											}
										}
									}
									if(updateTOs.size() > 0) {
										getBaseHibernateDao().saveTechnicalOrderList(updateTOs);
									}
								}
							} else {
								List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
								for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
									to.setOpenQuantity(0L);
									to.setRemainingQuantity(0L);
									to.setCompletedDate(new Date());
									to.setCompletedBysiteRegId(registrationId);
									updateTOs.add(to);
								}
								if(updateTOs.size() > 0) {
									getBaseHibernateDao().saveTechnicalOrderList(updateTOs);
								}
								if(techOrderList.size() > 1 && techOrderList.get(1) != null && techOrderList.get(1).size() > 0){
									for(TechnicalOrder to:techOrderList.get(1)){
										if(to != null && to instanceof TechnicalOrder
												&& (to.getOpenQuantity() == null || to.getOpenQuantity() != 0)){
											flag = 1;
										}
									}
								}
								if(flag == 0){
									/**
									 * Method for updating SiteRegistration 
									 * data status COMPLETED for IB creation
									 */
									getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.INSTALL_BASE_CREATION, false);
									this.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, StatusEnum.COMPLETED);
								}
							}
						}
					}
				}
			}
		} catch (DataAccessException e){
			logger.debug("Exception in BaseRegistrationService.processIBRegistrations(Map<String,List<String>> regIdMap) : "+e.getMessage());
			throw new DataAccessException(e.getMessage());
		}
		logger.debug("Exiting BaseRegistrationService.processIBRegistrations(Map<String,List<String>> regIdMap)");
	}
	
	// Method to process the IB records from EQR Asynch Service.
	public void processIBRegistrationsForEQR(Map<String,List<String>> regIdMap) throws Exception{
		logger.debug("Entering BaseRegistrationService.processIBRegistrationsForEQR(Map<String,List<String>> regIdMap)");
		try{
			if(regIdMap != null && regIdMap.size() > 0){
				for(Entry<String, List<String>> entry : regIdMap.entrySet()){
					int flag = 0;
					String registrationId = entry.getKey();
					List<String> mcList = entry.getValue();
					logger.debug("RegistartionId:"+registrationId);
					if(StringUtils.isNotEmpty(registrationId) && mcList != null && mcList.size() > 0) {
						SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
						List<Set<TechnicalOrder>> techOrderList = fetchTechnicalOrders(registrationId, GRTConstants.TECH_ORDER_TYPE_FV, mcList);
						if(techOrderList != null && techOrderList.size() > 0){
							siteRegistration.setTechnicalOrders(techOrderList.get(0));
						}
						if(!siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) &&
								!siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())) {
							if(siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())) {
								if(siteRegistration.getTechnicalOrders() != null && siteRegistration.getTechnicalOrders().size() > 0) {
									List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
									for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
										if(to.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_FV)) {
											if(StringUtils.isEmpty(to.getSr_Created()) || (!to.getSr_Created().equalsIgnoreCase(GRTConstants.YES))) {
												to.setOpenQuantity(0L);
												to.setRemainingQuantity(0L);
												to.setCompletedDate(new Date());
												to.setCompletedBysiteRegId(registrationId);
												updateTOs.add(to);
											}
										}
									}
									if(updateTOs.size() > 0) {
										getBaseHibernateDao().saveTechnicalOrderList(updateTOs);
									}
								}
							} else {
								List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
								for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
									to.setOpenQuantity(0L);
									to.setRemainingQuantity(0L);
									to.setCompletedDate(new Date());
									to.setCompletedBysiteRegId(registrationId);
									updateTOs.add(to);
								}
								if(updateTOs.size() > 0) {
									getBaseHibernateDao().saveTechnicalOrderList(updateTOs);
								}
								if(techOrderList.size() > 1 && techOrderList.get(1) != null && techOrderList.get(1).size() > 0){
									for(TechnicalOrder to:techOrderList.get(1)){
										if(to != null && to instanceof TechnicalOrder
												&& (to.getOpenQuantity() == null || to.getOpenQuantity() != 0)){
											flag = 1;
										}
									}
								}
								if(flag == 0){
									 //**
									 //* Method for updating SiteRegistration 
									 //* data status COMPLETED for IB creation
									 //**
									getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.INSTALL_BASE_CREATION, false);
									this.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, StatusEnum.COMPLETED);
								}
							}
						}
					}
				}
			}
		} catch (DataAccessException e){
			logger.debug("Exception in BaseRegistrationService.processIBRegistrationsForEQR(Map<String,List<String>> regIdMap) : "+e.getMessage());
			throw new DataAccessException(e.getMessage());
		}
		logger.debug("Exiting BaseRegistrationService.processIBRegistrationsForEQR(Map<String,List<String>> regIdMap)");
	}

	/**
	 *  Process the registration based on -> TechnicalOrderOpen quantity/Pipeline processed flag
	 * @throws Exception
	 */
	public void processRegistration(SiteRegistration siteRegistration, String completedByRegId, List<String> assetsToBeProcessed,
			ProcessStepEnum processStep, boolean srFlag) throws Exception {
		logger.debug("Entering BaseRegistrationService.processRegistration(SiteRegistration siteRegistration, String completedByRegId, "
				+ "List<String> assetsToBeProcessed, ProcessStepEnum processStep, boolean srFlag)");
		try{
			List<String> processedRegistrations = new ArrayList<String>();
			List<TechnicalOrder> technicalOrderList = null;
			boolean hasOpenQuantity = false;
			String orderType = "";
			if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId().equalsIgnoreCase(processStep.getProcessStepId())){
				orderType = GRTConstants.TECH_ORDER_TYPE_FV;
			} 
			//GRT 4.0 - Additional case added for Process step Equipment Move
			else if (ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId().equalsIgnoreCase(processStep.getProcessStepId())) {
				orderType = GRTConstants.TECH_ORDER_TYPE_EM;
			}
			else {
				orderType = GRTConstants.TECH_ORDER_TYPE_IB;
			}
			// Updating TechnicalOrder open Quantity/Pipeline processed flag to true
			logger.debug("AssetsToBeProcessed Size:"+assetsToBeProcessed.size());
			if(assetsToBeProcessed.size() > 0){
				// Removing duplicates
				assetsToBeProcessed = TechnicalRegistrationUtil.removeDuplicateStringFromList(assetsToBeProcessed);
				// Update Open quantity to zero
				getBaseHibernateDao().updateTODOpenQuantity(siteRegistration.getRegistrationId(), completedByRegId, assetsToBeProcessed, orderType);
				// Set pipeline processed flag to true
				processedRegistrations.add(siteRegistration.getRegistrationId());
				/**
				 * Method for updating processed pipelined transaction assets
				 */
				getBaseHibernateDao().updateProcessedPipelineTransactions(processedRegistrations, assetsToBeProcessed, orderType);
			} 
			// Making IS_EQR_SR_COMPLETED Flag to 'YES' when SR closure event has come
			if(srFlag){
				/**
				 * Method for updating completed status 
				 * in SiteRegistration table for Final Record Validation
				 * and in GRt4.0 for Equipment Move
				 */
				if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId().equalsIgnoreCase(processStep.getProcessStepId())) {
					getBaseHibernateDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, GRTConstants.YES);
				} else if (ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId().equalsIgnoreCase(processStep.getProcessStepId())) {
					getBaseHibernateDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.EQUIPMENT_MOVE, GRTConstants.YES);
				}
			}

			technicalOrderList = getBaseHibernateDao().getTechnicalOrderByType(siteRegistration.getRegistrationId(), orderType);

			if(technicalOrderList != null){
				for (TechnicalOrder order : technicalOrderList) {
					/*String srCreated = order.getSr_Created();
		        	if (srCreated != null && srCreated.equalsIgnoreCase(GRTConstants.YES)){
		        		continue;
		        	} else */
					//Added for GRT 4.0.
					//If there is an SR created and the status is not AWAITINGINFO, do not set the status of the registration to COMPLETED.
					
					if(null != order.getActionType() && "S".equalsIgnoreCase(order.getActionType())) {
						String srCreated = order.getSr_Created();
						if (srCreated != null && srCreated.equalsIgnoreCase(GRTConstants.YES)){
							if(!StatusEnum.AWAITINGINFO.getStatusId().equalsIgnoreCase(siteRegistration.getFinalValidationStatus().getStatusId())) {
								hasOpenQuantity = true;
							} 
						}
					} else if (order.getOpenQuantity() == null || order.getOpenQuantity() != 0){
						hasOpenQuantity = true;
					}
				}
			}
			logger.debug("SRFlag:"+srFlag+"    HasOpenQuantity:"+hasOpenQuantity+"  Status:"+siteRegistration.getFinalValidationStatus().getStatusId());
			//GRT 4.0 Change : 
			if( GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType) && !StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(siteRegistration.getFinalValidationStatus().getStatusId())){
				if (srFlag && hasOpenQuantity){
					/**
					 * Method for updating In Process status in SiteRegistration table
					 */
					getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, processStep, false);
					sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, StatusEnum.INPROCESS);
				} else if((srFlag && !hasOpenQuantity)
						|| (!hasOpenQuantity && !StatusEnum.AWAITINGINFO.getStatusId().equalsIgnoreCase(siteRegistration.getFinalValidationStatus().getStatusId()))){
					/**
					 * Method for updating Completed status in SiteRegistration table
					 */
					getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, processStep, false);
					sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, StatusEnum.COMPLETED);
				}
			}
			//GRT 4.0 Change : update status for Equipment move 
			else if(!StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(siteRegistration.getEqrMoveStatus().getStatusId())){
				if (srFlag){
					getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, processStep, false);
					sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, StatusEnum.INPROCESS);
				}else{
					getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, processStep, false);
					sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, StatusEnum.COMPLETED);
				}
			}
		} catch(DataAccessException e) {
			logger.error("Exception in : BaseRegistrationService.processRegistration(SiteRegistration siteRegistration, String completedByRegId, "
				+ "List<String> assetsToBeProcessed, ProcessStepEnum processStep, boolean srFlag)" + e.getMessage());
			throw e;
		}
		logger.debug("Exiting BaseRegistrationService.processRegistration(SiteRegistration siteRegistration, String completedByRegId, "
				+ "List<String> assetsToBeProcessed, ProcessStepEnum processStep, boolean srFlag)");
	}


	/**
	 * Send RegistrationRequestAlert for SiteRegistration
	 *
	 * @param siteRegistration SiteRegistration
	 * @param status StatusEnum
	 * @throws Exception
	 */
	public void sendRegistrationRequestAlert(SiteRegistration siteRegistration, ProcessStepEnum processStep, StatusEnum status) throws Exception {
		sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, status);
	}

	/**
	 * Send RegistrationRequestAlert for technicalRegistration
	 *
	 * @param technicalRegistration TechnicalRegistration
	 * @param siebelSRStatus String
	 * @throws Exception
	 */
	public void sendRegistrationRequestAlert(TechnicalRegistration technicalRegistration, StatusEnum status) throws Exception {
		logger.debug("Entering BaseRegistrationService.sendRegistrationRequestAlert(TechnicalRegistration technicalRegistration, StatusEnum status)");
		logger.debug("sendRegistrationRequestAlert starting for TechnicalRegistrationId [" 
				+ technicalRegistration.getTechnicalRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		RegistrationRequestAlert result = null;
		try {
			technicalRegistration = getBaseHibernateDao().getTechnicalRegistration(technicalRegistration.getTechnicalRegistrationId());
			result = RegistrationRequestAlertConvertor.convert(technicalRegistration);
			if(status != null) {
				result.setStatus(status.getStatusShortDescription());
			}
			result = populateActionRequired(result);
		} catch(Exception e) {
			logger.error("Exception in BaseRegistrationService.sendRegistrationRequestAlert(TechnicalRegistration technicalRegistration, StatusEnum status): "
					+ "While trying to collect data to send. Error message: " + e.getMessage());
			throw e;
		}

		getMailUtil().sendMailNotification(result);
		logger.debug("sendRegistrationRequestAlert exiting for TechnicalRegistrationId [" 
				+ technicalRegistration.getTechnicalRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
		logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestAlert(TechnicalRegistration technicalRegistration, StatusEnum status)");
	}

	/**
	 * Fetch Site Registration details based on registrationId
	 */
	public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.getSiteRegistrationOnRegId(String registrationId)");
		SiteRegistration siteRegistration ;		
		siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
		logger.debug("Exiting BaseRegistrationService.getSiteRegistrationOnRegId(String registrationId)");
		return siteRegistration;
	}

	public TRConfig loadTRConfig(String groupId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.loadTRConfig(String groupId)");
		TRConfig trConfig = null;
		try {
			Map<String, Set<String>> mc2GroupIdMappings=getBaseHibernateDao().initializeTRConfigData();
			trConfig = getBaseHibernateDao().loadTRConfig(groupId);
		} catch(Throwable throwable) {
			logger.error("Exception occured in BaseRegistrationService.loadTRConfig(String groupId) : ", throwable);
		}
		logger.debug("Exiting BaseRegistrationService.loadTRConfig(String groupId)");
		return trConfig;
	}

	/**
	 * API returns collection releaseNumbers for passed GroupId.
	 *
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<String> getReleasesByGroupId(String groupId, boolean sslvpn) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getReleasesByGroupId(String groupId, boolean sslvpn)");
		List<String> result = null;
		try {
			TRConfig trConfig = this.loadTRConfig(groupId);
			if(trConfig != null) {
				logger.debug("Found trConfig:" + trConfig);
				result = this.getBaseHibernateDao().getReleases(trConfig.getSeCode(), trConfig.getProductType(), trConfig.getTemplate(), trConfig.getSpecial_note(), sslvpn);
			} else {
				logger.debug("NOT Found trConfig :" + trConfig);
			}
		} catch(Throwable throwable) {
			logger.error("Exception occured in BaseRegistrationService.getReleasesByGroupId(String groupId, boolean sslvpn) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.getReleasesByGroupId(String groupId, boolean sslvpn)");
		}
		return result;
	}

	public List<LocalTRConfig> getLocalTRConfig(String groupId) throws DataAccessException {
		/**
		 * Method for getting LOCAL_TR_CONFIG data
		 */
		List<LocalTRConfig> localTRConfigs =  getBaseHibernateDao().getLocalTRConfig(loadTRConfig(groupId));
		if(localTRConfigs != null && localTRConfigs.size()>0) {
			modifiedLocalTRConfigs(localTRConfigs);
		}
		return localTRConfigs;
	}

	public void modifiedLocalTRConfigs(List<LocalTRConfig> localTRConfigs) {
		logger.debug("Entering BaseRegistrationService.modifiedLocalTRConfigs(List<LocalTRConfig> localTRConfigs)");
		try {
			if(localTRConfigs != null && localTRConfigs.size() > 0) {
				for (LocalTRConfig config : localTRConfigs) {
					modifiedLocalTRConfig(config);
				}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.modifiedLocalTRConfigs(List<LocalTRConfig> localTRConfigs) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.modifiedLocalTRConfigs(List<LocalTRConfig> localTRConfigs)");
		}
	}

	public void modifiedLocalTRConfig(LocalTRConfig localTRConfig) {
		logger.debug("Entering BaseRegistrationService.modifiedLocalTRConfig(LocalTRConfig localTRConfig)");
		try {
			if(localTRConfig != null && StringUtils.isNotEmpty(localTRConfig.getSeCode())) {
				CompatibilityMatrix compatibilityMatrix = getCompatibilityMatrix(localTRConfig.getSeCode());
				if(compatibilityMatrix != null) {
					localTRConfig.setAlarmOriginationEligible(GRTConstants.Y.equalsIgnoreCase(compatibilityMatrix.getTransportAlarm()));
				}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.modifiedLocalTRConfig(LocalTRConfig localTRConfig) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.modifiedLocalTRConfig(LocalTRConfig localTRConfig)");
		}
	}

	public String getvCodesList() {
		return vCodesList;
	}

	public void setvCodesList(String vCodesList) {
		this.vCodesList = vCodesList;
	}

	public void setWorksheetProcessor(
			TechnicalOrderDetailWorsheetProcessor worksheetProcessor) {
		this.worksheetProcessor = worksheetProcessor;
	}

	public SapClient getSapClient() {
		return sapClient;
	}

	public void setSapClient(SapClient sapClient) {
		this.sapClient = sapClient;
	}

	public MailUtil getMailUtil() {
		return mailUtil;
	}

	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public SiebelClient getSiebelClient() {
		return siebelClient;
	}

	public void setSiebelClient(SiebelClient siebelClient) {
		this.siebelClient = siebelClient;
	}

	public TechnicalOrderDetailWorsheetProcessor getWorksheetProcessor() {
		return worksheetProcessor;
	}

	public String getSrCreationGenericContactEmail() {
		return srCreationGenericContactEmail;
	}

	public void setSrCreationGenericContactEmail(
			String srCreationGenericContactEmail) {
		this.srCreationGenericContactEmail = srCreationGenericContactEmail;
	}

	public String getSrCreationGenericContactPhone() {
		return srCreationGenericContactPhone;
	}

	public void setSrCreationGenericContactPhone(
			String srCreationGenericContactPhone) {
		this.srCreationGenericContactPhone = srCreationGenericContactPhone;
	}

	public String getSrCreationGenericContactFName() {
		return srCreationGenericContactFName;
	}

	public void setSrCreationGenericContactFName(
			String srCreationGenericContactFName) {
		this.srCreationGenericContactFName = srCreationGenericContactFName;
	}

	public String getSrCreationGenericContactLName() {
		return srCreationGenericContactLName;
	}

	public void setSrCreationGenericContactLName(
			String srCreationGenericContactLName) {
		this.srCreationGenericContactLName = srCreationGenericContactLName;
	}

	public String getSiebelUpdateQueue() {
		return siebelUpdateQueue;
	}

	public void setSiebelUpdateQueue(String siebelUpdateQueue) {
		this.siebelUpdateQueue = siebelUpdateQueue;
	}

	public GenericClient getGenericClient() {
		return genericClient;
	}

	public void setGenericClient(GenericClient genericClient) {
		this.genericClient = genericClient;
	}



	public String getOssnoIP() {
		return ossnoIP;
	}

	public void setOssnoIP(String ossnoIP) {
		this.ossnoIP = ossnoIP;
	}

	public String getArtServiceName() {
		return artServiceName;
	}

	public void setArtServiceName(String artServiceName) {
		this.artServiceName = artServiceName;
	}

	public ARTClient getArtClient() {
		return artClient;
	}

	public void setArtClient(ARTClient artClient) {
		this.artClient = artClient;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public boolean doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType) throws Exception {
		logger.debug("Entering BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		logger.debug("Entering doTechnicalRegistration:" + sr.getRegistrationId());
		try {
			if(sr != null && ((trs != null && trs.size() > 0) || (sls != null && sls.size() > 0))) {
				if( trs != null && trs.size() > 0) {
					return handleTRs(sr, trs, operationType);
				} else if(sls != null && sls.size() > 0) {
					logger.debug("<===============================  SITE LIST SIZE IS >0 AND   BEFORE CALLING THE  ART Service ========================================== >                ");
					handleSLs(sr, sls);
				}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType) : ", throwable);
			return false;
		} finally {
			logger.debug("Exiting doTechnicalRegistration:" + sr.getRegistrationId());
			logger.debug("Exiting BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		}
		return true;
	}
	
	public DeviceTOBResponseType submitReTestList(List<TechnicalRegistration> techRegs) throws Exception {
		logger.debug("Entering BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		//logger.debug("Entering doTechnicalRegistration:" + sr.getRegistrationId());
		try {
			if( techRegs != null && techRegs.size() > 0 ) {
					return submitSiteRegsForReTest(techRegs);
				} 
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType) : ", throwable);	
		} finally {
			logger.debug("Exiting BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		}
		return null;
	}

	public boolean handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)throws Exception {
		logger.debug("Entering BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
		logger.debug("Entering handleTRs for RegistrationId:" + sr.getRegistrationId());
		try {
			List<TechRegInputDto> dtos = new ArrayList<TechRegInputDto>();
			String ipoUserEmail = null;
			if(sr != null && trs != null && trs.size() > 0) {
				for (TechnicalRegistration tr : trs) {
					if(tr != null) {

						TechRegInputDto dto = new TechRegInputDto();
						dto.setTechRegId(tr.getTechnicalRegistrationId());
						dto.setGrtid(sr.getRegistrationId());
						logger.debug("GRT ID:" + dto.getGrtid());
						logger.debug("TechRegId:" + dto.getTechRegId());
						dto.setTechRegDetail(GRTConstants.TECHREGDETAIL_TR);
						dto.setUsrid(GRTConstants.DEFAULT_ART_USER);
						dto.setFl(sr.getSoldToId());
						dto.setAorig(GRTConstants.N);
						String groupId = tr.getGroupId();
						LocalTRConfig localTRConfig = null;
						if(StringUtils.isNotEmpty(groupId)) {
							try {
								List<LocalTRConfig> localTRConfigs = getLocalTRConfig(groupId);
								if(localTRConfigs != null && localTRConfigs.size() > 0) {
									localTRConfig = localTRConfigs.get(0);
								}
							} catch (Throwable throwable) {
								logger.debug("Error while loading LocalTRConfig, consuming the error and continuing.", throwable);
							}
						}
						//GRT 4.0 Changes
						dto.setAccessType(tr.getAccessType());
						
						if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(tr.getPopUpHiddenValue()) || GRTConstants.NO.equalsIgnoreCase(tr.getConnectivity()) || GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(tr.getAccessType())){
							dto.setAccessType(GRTConstants.NO_CONNECTIVITY);
						}
						dto.setOptype(tr.getOperationType());
						/*if(operationType != null) {
							dto.setOptype(operationType.getOperationKey());
						}*/
						
						//TODO---Need to check
						if(GRTConstants.YES.equalsIgnoreCase(tr.getConnectivity()) && !GRTConstants.ACCESS_TYPE_SAL.equalsIgnoreCase(tr.getAccessType())){
							if(StringUtils.isNotEmpty(dto.getOptype())) {//Set the OperationType Only when OperationType is not saved in DB.
								dto.setOptype(ARTOperationType.FULLNEW.getOperationKey());
							}
						}
						
						if("Update".equalsIgnoreCase(tr.getPopUpHiddenValue()))
						{
							dto.setOptype(ARTOperationType.DATABASEUPDATE.getOperationKey());
						}
						
						if(localTRConfig != null && localTRConfig.isAlarmOriginationEligible()) {
							String orgination = (GRTConstants.YES.equalsIgnoreCase(tr.getAlarmOrigination()) || GRTConstants.Y.equalsIgnoreCase(tr.getAlarmOrigination()))
									?GRTConstants.Y:GRTConstants.N;
							logger.debug("alarm Origination to ART:"  +orgination);
							dto.setAorig(orgination);
						} else {
							dto.setAorig(GRTConstants.N);
						}
						
						if(tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO) || tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_SSLVPN)) {
							dto.setOptype(ARTOperationType.ONBOARDXML.getOperationKey());
						}
						
						if(StringUtils.isNotEmpty(tr.getSummaryEquipmentNumber())) {
							dto.setSummaryEquipmentNumber(tr.getSummaryEquipmentNumber());
						} else if(StringUtils.isNotEmpty(tr.getEquipmentNumber())) {
							//dto.setEquipmentNumber(tr.getEquipmentNumber());
							if(StringUtils.isEmpty(tr.getSummaryEquipmentNumber())) {
								String summaryEquipementNumber = (tr.getEquipmentNumber().indexOf(".") < 0)?tr.getEquipmentNumber():tr.getEquipmentNumber().substring(0, tr.getEquipmentNumber().indexOf("."));
								logger.debug("Derived SEQN:" + summaryEquipementNumber + " from EQN:" + tr.getEquipmentNumber());
								dto.setSummaryEquipmentNumber(summaryEquipementNumber);
							}
						}

						if(StringUtils.isNotEmpty(tr.getGroupId())) {
							dto.setGroupId(tr.getGroupId());
						} else {
							throw new Exception("GROUPID Can not be Null TRID:" + tr.getTechnicalRegistrationId());
						}
						dto.setMcode(tr.getTechnicalOrder().getMaterialCode());
						dto.setScode(tr.getSolutionElement());
						dto.setRelno(tr.getSoftwareRelease());
						if(tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_MODEM)) {
							
							if(StringUtils.isNotEmpty(tr.getDialInNumber())) {
							dto.setInads(tr.getDialInNumber());
							}
							String country=sr.getSiteCountry();
							/**
							 * Method for getting Phone number by country
							 */
							String phoneNo=getBaseHibernateDao().getPhoneNoByCountry(country); 
							//phoneNo="";
							String ossno = "";
							if(StringUtils.isNotEmpty(tr.getOutboundCallingPrefix())) {
								ossno += tr.getOutboundCallingPrefix().trim();
							}
							if(StringUtils.isNotEmpty(phoneNo)) {
								dto.setOssno(ossno + phoneNo);
							}
							
							
						} else if(tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IP) || tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_RSAIP)) {
							
							dto.setIpadd(tr.getIpAddress());
							String ossno_ip = getOssnoIP();
							if(ossno_ip!=null){
								dto.setOssno(ossno_ip);
							}
							
						} else if (tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_SAL)) {
							
							String custIpAddress = tr.getPrimarySalGWSeid();
							if(StringUtils.isNotEmpty(tr.getSecondarySalGWSeid())) {
								custIpAddress += "+" + tr.getSecondarySalGWSeid();
							}
							dto.setCustipaddress(custIpAddress);
							
						} else if(tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO) || tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_SSLVPN)) {
						
							try {
								String serviceName = getArtServiceName();
								dto.setSvrname(serviceName);
							} catch(Throwable throwable) {
								logger.error("BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType) : "
										+ "Error while reading service_name property, defaulting serviceName to:" + GRTConstants.SERVICE_NAME_IPO, throwable);
								dto.setSvrname(GRTConstants.SERVICE_NAME_IPO);
							}
							
						} 
						if(operationType != null && (operationType.getOperationKey().equals(ARTOperationType.PASSWORDRESET.getOperationKey()) || operationType.getOperationKey().equals(ARTOperationType.REGENONBOARDXML.getOperationKey()))){
							try {
								ipoUserEmail = tr.getIpoUserEmail();
								dto.setOptype(operationType.getOperationKey());
								if(StringUtils.isNotEmpty(tr.getServiceName())) {
									dto.setSvrname(tr.getServiceName());
								} else {
									String serviceName = getArtServiceName();
									dto.setSvrname(serviceName);
								}
							} catch(Throwable throwable) {
								logger.error("BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType) : "
										+ "Error while reading service_name property, defaulting serviceName to:" + GRTConstants.SERVICE_NAME_IPO, throwable);
								dto.setSvrname(GRTConstants.SERVICE_NAME_IPO);
							}
							if(StringUtils.isNotEmpty(tr.getAlarmId())) {
								dto.setAlmid(tr.getAlarmId());
							}
						}
						
						if("Update".equalsIgnoreCase(tr.getPopUpHiddenValue()))
						{
							dto.setOptype(ARTOperationType.DATABASEUPDATE.getOperationKey());
						}
						
						
						//GRT 4.0 Changes
						if(!tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO))
						{
							if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(tr.getPopUpHiddenValue()) || GRTConstants.NO.equalsIgnoreCase(tr.getConnectivity())|| GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(tr.getAccessType())){
								dto.setOptype(ARTOperationType.DATABASENEW.getOperationKey());
								//dto.setAccessType(GRTConstants.NO_CONNECTIVITY);
							}
						}
						if(StringUtils.isNotEmpty(tr.getSid())) {
							dto.setSid(tr.getSid()); 
						}
						if(StringUtils.isNotEmpty(tr.getMid())) {
							dto.setMid(tr.getMid());
						}
						dto.setRndpwd(tr.getRandomPassword());
						dto.setPrvip(tr.getPrivateIpAddress());
						dto.setVpmsldn(tr.getSeidOfVoice());
						if(StringUtils.isNotEmpty(tr.getCheckReleaseHigher())) {
							if(tr.getCheckReleaseHigher().equals("0")) {
								dto.setFoption(GRTConstants.OFF);
							} else {
								dto.setFoption(GRTConstants.ON);
							}
						}
						dto.setIsDOCR(tr.getCheckSesEdge());
						dto.setAfid(tr.getAuthorizationFileId());
						dto.setHwsvr(tr.getHardwareServer());

						if(StringUtils.isNotEmpty(tr.getSolutionElementId())) {
							dto.setMainSeid(tr.getSolutionElementId());
						}
						if(StringUtils.isNotEmpty(tr.getFailedSeid())) {
							dto.setFailedSeid(tr.getFailedSeid());
						}
						if(StringUtils.isNotEmpty(tr.getUsername())) {
							dto.setUsername(tr.getUsername());
						}
						if(StringUtils.isNotEmpty(tr.getPassword())) {
							dto.setPassword(tr.getPassword());
						}

						try {
							//Assign NickName
							/*if(StringUtils.isNotEmpty(tr.getOperationType()) && !tr.getOperationType().equalsIgnoreCase(GRTConstants.DU)) {
								if(StringUtils.isNotEmpty(tr.getProductCode()) && StringUtils.isNotEmpty(tr.getSolutionElement()) && StringUtils.isEmpty(tr.getNickname())) {
									String prod = tr.getProductCode().substring(0, (tr.getProductCode().length()<3)?tr.getProductCode().length():3);
									String seCode = tr.getSolutionElement().substring(0, (tr.getSolutionElement().length()<3)?tr.getSolutionElement().length():3);
									dto.setNickname(prod +"_" + seCode +"_MID " + ((StringUtils.isNotEmpty(tr.getMid()))?tr.getMid():""));
								}
							}*/
							
							//GRT 4.0 Change BR-F.007
							if(StringUtils.isNotEmpty(tr.getNickname())) {
								dto.setNickname(tr.getNickname());
							}
						} catch(Throwable throwable) {
							logger.error("BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType) : "
									+ "Error while computing NickName", throwable);
						}
						dtos.add(dto);
					}
				}
			}
			TechRegRespType response = getArtClient().technicalRegistration(dtos);
			if(response!= null) {
				StatusEnum newSRStatus = StatusEnum.INPROCESS;
				TechRegAcknowledgementType[] acknowledgements = response.getTechRegAcknowledgement();
				List<TechnicalRegistration> trsForSR = new ArrayList<TechnicalRegistration>();
				List<TechnicalRegistration> trsForStatusUpdate = new ArrayList<TechnicalRegistration>();
				SRRequest srRequest = null;
				boolean dataPostedToArt = false;
				if(acknowledgements != null) {
					for (TechRegAcknowledgementType ack : acknowledgements) {
						if (ack!= null){
							logger.debug("The Content of ack.code is New =================> ::"+ ack.getCode());
							logger.debug("The Content of ack.getArtid()is New =================> ::"+ ack.getArtid());
							logger.debug("The Content of ack.techRegId is New =================> ::"+ ack.getTechRegId());
							logger.debug("The Content of ack.Description is New=================> ::"+ ack.getDescription());					
						} else {
							logger.debug("The ack inside the response is null " );	
						}
						if(operationType != null && (operationType.getOperationKey().equalsIgnoreCase(ARTOperationType.PASSWORDRESET.getOperationKey()) || operationType.getOperationKey().equalsIgnoreCase(ARTOperationType.REGENONBOARDXML.getOperationKey()))) {
							if(ack.getCode().equals("0")) {
								/**
								 * Method for getting TR data for a TechRegId
								 */
								TechnicalRegistration tr = getBaseHibernateDao().getTechnicalRegistration(ack.getTechRegId());
								if(tr != null) {
									tr.setIpoUserEmail(ipoUserEmail);
									/**
									 * Method for saving TechnicalRegistration data
									 */
									getBaseHibernateDao().saveTechnicalRegistration(tr);
									logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
									return true;
								}
							} else {
								logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
								return false;
							}
						}
						if (ack.getTechRegId() == null) {
							logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
							return false;
						}
						/**
						 * Method for getting TR data for a TechRegId
						 */
						TechnicalRegistration tr = getBaseHibernateDao().getTechnicalRegistration(ack.getTechRegId());
						int numberOfSubmit = 0;
						if(StringUtils.isNotEmpty(tr.getNumberOfSubmit())) {
							numberOfSubmit = Integer.valueOf(tr.getNumberOfSubmit());
						}
						tr.setNumberOfSubmit("" + (numberOfSubmit + 1));
						if(ack.getCode().equals("0")) {
							//tr.setErrorCode(ack.getCode());
							tr.setArtId(ack.getArtid());
							Status status = new Status();
							status.setStatusId(StatusEnum.INPROCESS.getStatusId());
							tr.setStatus(status);
							//sendRegistrationRequestAlert(tr, StatusEnum.INPROCESS);
							/**
							 * Method for saving Technical Registration data 
							 */
							getBaseHibernateDao().saveTechnicalRegistration(tr);
							dataPostedToArt = true;
						} else {
							tr.setErrorCode(ack.getCode());
							Status status = new Status();
							status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
							tr.setStatus(status);
							tr.setArtId(ack.getArtid());
							tr.setSubErrorCode(ack.getSubCode());
							tr.setErrorDesc(ack.getDescription());
							if(tr.getNumberOfSubmitAsInt() == 2) {
								trsForSR.add(tr);
							}
							trsForStatusUpdate.add(tr);
						}
					}
					if(trsForStatusUpdate.size() > 0) {
						logger.debug("Found Errors, Initiating SR creation");
						newSRStatus = StatusEnum.AWAITINGINFO;
						if(trsForSR.size() > 0) {
							srRequest = createSR(sr, trsForSR, null, TechRecordEnum.TR);
						}
						for (TechnicalRegistration tr : trsForStatusUpdate) {
							if(srRequest != null && tr.getNumberOfSubmitAsInt() == 2) {
								tr.setSrRequest(srRequest);
								tr.setArtSrNo(srRequest.getSiebelSRNo());
							}
							/**
							 * Method for saving Technical Registration data 
							 */
							getBaseHibernateDao().saveTechnicalRegistration(tr);
							if(tr.getNumberOfSubmitAsInt() > 1) {
								//ARTAsycnResponseHandler asycnResponseHandler = new ARTAsycnResponseHandler();
								sendEmailNoticeForTOBLine(tr, null, sr, StatusEnum.AWAITINGINFO);
							}
						}
					}
					logger.debug("Existing TechnRegStatus:" + sr.getTechRegStatus().getStatusId());
					logger.debug("New TechnRegStatus:" + newSRStatus.getStatusId());
					logger.debug("dataPostedToArt:" + dataPostedToArt);
					if(!sr.getTechRegStatus().getStatusId().equalsIgnoreCase(newSRStatus.getStatusId())) {
						/**
						 * Method for updating SiteRegistration Status for Technical Registration
						 */
						this.getBaseHibernateDao().updateSiteRegistrationStatus(sr, newSRStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
						if(!newSRStatus.getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId()) || (!dataPostedToArt && newSRStatus.getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId()))) {
							sendRegistrationRequestAlert(sr, ProcessStepEnum.TECHNICAL_REGISTRATION, newSRStatus);
						}
					}
				} else {
					logger.debug("The TechRegAcknowledgementType array inside the response is null " );	
					logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
					return false;
				}
			}
		} catch (Throwable throwable) {
			logger.error("Exception occured in BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType) : ", throwable);
			logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
			return false;
		} finally {
			logger.debug("Exiting handleTRs for RegistrationId:" + sr.getRegistrationId());
		}
		
		logger.debug("Exiting BaseRegistrationService.handleTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
		return true;
	}

	public void handleSLs(SiteRegistration sr, List<SiteList> sls) throws Exception {
		logger.debug("Entering BaseRegistrationService.handleSLs(SiteRegistration sr, List<SiteList> sls)");
		logger.debug("Entering handleSLs for SiteRegistration Id:" + sr.getRegistrationId());
		try {
			List<TechRegInputDto> dtos = new ArrayList<TechRegInputDto>();		
			if(sr != null && sls != null && sls.size() > 0) {
				for (SiteList sl : sls) {

					if(sl != null) {
						TechRegInputDto dto = new TechRegInputDto();
						dto.setGrtid(sr.getRegistrationId());
						dto.setTechRegId(sl.getId());
						dto.setTechRegDetail(GRTConstants.TECHREGDETAIL_SL);
						dto.setUsrid(GRTConstants.DEFAULT_ART_USER);
						dto.setFl(sr.getSoldToId());
						dto.setOptype(ARTOperationType.DATABASEUPDATE.getOperationKey());
						dto.setSummaryEquipmentNumber(sl.getSummaryEquipmentNumber());
						dto.setEquipmentNumber(sl.getEquipmentNumber());
						if (StringUtils.isNotEmpty(sl.getGroupId())) {
							logger.debug("GroupId:" + sl.getGroupId());
							dto.setGroupId(sl.getGroupId());
						}
						dto.setMcode(sl.getMaterialCode());
						dto.setScode(sl.getSeCode());
						if(StringUtils.isNotEmpty(sl.getRelease())) {
							dto.setRelno(sl.getRelease());
						}
						String custIpAddress = sl.getPrimarySALGatewaySEID();
						if(StringUtils.isNotEmpty(sl.getSecondarySALGatewaySEID())) {
							custIpAddress += "+" + sl.getSecondarySALGatewaySEID();
						}
						dto.setCustipaddress(custIpAddress);
						dto.setMainSeid(sl.getSolutionElementId());
						dtos.add(dto);
					}
				}
			}

			//TechRegRespType response = new ARTClient().technicalRegistration(dtos);
			TechRegRespType response = getArtClient().technicalRegistration(dtos);
			if(response!= null) {
				StatusEnum newSRStatus = StatusEnum.INPROCESS;
				TechRegAcknowledgementType[] acknowledgements = response.getTechRegAcknowledgement();
				List<SiteList> slsForSR = new ArrayList<SiteList>();
				List<SiteList> slsForStatusUpdate = new ArrayList<SiteList>();
				SRRequest srRequest = null;
				boolean dataPostedToArt = false;
				if(acknowledgements != null){
					for (TechRegAcknowledgementType ack : acknowledgements) {
						if (ack!= null){
							logger.debug("The Content of ack.code is =================> ::"+ ack.getCode());
							logger.debug("The Content of ack.getArtid()is =================> ::"+ ack.getArtid());
							logger.debug("The Content of ack.techRegId is =================> ::"+ ack.getTechRegId());
							logger.debug("The Content of ack.Description is =================> ::"+ ack.getDescription());					
						} else {
							logger.debug("The ack inside the response is null " );	
						}
						/**
						 * Method for getting SITE_LIST data
						 */
						SiteList sl = this.getBaseHibernateDao().getSiteList(ack.getTechRegId());
						int numberOfSubmit = 0;
						if(StringUtils.isNotEmpty(sl.getNumberOfSubmit())) {
							numberOfSubmit = Integer.valueOf(sl.getNumberOfSubmit());
						}
						sl.setNumberOfSubmit("" + (numberOfSubmit + 1));
						sl.setArtId(ack.getArtid());
						if(sl != null) {
							if(ack.getCode().equals("0")) {
								//sl.setErrorCode(ack.getCode());
								Status status = new Status();
								status.setStatusId(StatusEnum.INPROCESS.getStatusId());
								sl.setStatus(status);
								//sendRegistrationRequestAlert(sl, StatusEnum.INPROCESS);
								
								sendEmailNoticeForTOBLine(null, sl, sl.getSiteRegistration(), StatusEnum.INPROCESS);
								
								/**
								 * Method for saving SITE_LIST data
								 */
								this.getBaseHibernateDao().saveSalSiteList(sl);
								dataPostedToArt = true;
							} else {
								newSRStatus = StatusEnum.AWAITINGINFO;
								sl.setErrorCode(ack.getCode());
								Status status = new Status();
								status.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
								sl.setStatus(status);
								sl.setSubErrorCode(ack.getSubCode());
								sl.setErrorDesc(ack.getDescription());
								if(sl.getNumberOfSubmitAsInt() == 2) {
									slsForSR.add(sl);
								}
								slsForStatusUpdate.add(sl);
							}
						}
					}
					if(slsForStatusUpdate.size() > 0) {
						logger.debug("Found Errors, Initiating SR creation");
						newSRStatus = StatusEnum.AWAITINGINFO;
						if(slsForSR.size() > 0) {
							srRequest = createSR(sr, null, slsForSR, TechRecordEnum.SAL_MIGRATION);
						}
						for (SiteList sl : slsForStatusUpdate) {
							if(srRequest != null && sl.getNumberOfSubmitAsInt() == 2) {
								sl.setSrRequest(srRequest);
								sl.setArtSrNo(srRequest.getSiebelSRNo());
							}
							/**
							 * Method for saving SITE_LIST data
							 */
							this.getBaseHibernateDao().saveSalSiteList(sl);
							if(sl.getNumberOfSubmitAsInt() > 1) {
								//ARTAsycnResponseHandler artAsycnResponseHandler = new ARTAsycnResponseHandler();
								sendEmailNoticeForTOBLine(null, sl, sl.getSiteRegistration(), newSRStatus);
							}
						}
					}
					logger.debug("Existing TechnRegStatus:" + sr.getTechRegStatus().getStatusId());
					logger.debug("New TechnRegStatus:" + newSRStatus.getStatusId());
					logger.debug("dataPostedToArt:" + dataPostedToArt);
					if(!sr.getTechRegStatus().getStatusId().equalsIgnoreCase(newSRStatus.getStatusId())) {
						/**
						 * Method for updating SiteRegistration Status for TR
						 */
						this.getBaseHibernateDao().updateSiteRegistrationStatus(sr, newSRStatus, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
						if(!newSRStatus.getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId()) || (!dataPostedToArt && newSRStatus.getStatusId().equalsIgnoreCase(StatusEnum.AWAITINGINFO.getStatusId()))) {
							sendRegistrationRequestAlert(sr, ProcessStepEnum.TECHNICAL_REGISTRATION, newSRStatus);
						}
					}
				} else {
					logger.debug("The TechRegAcknowledgementType array inside the response is null " );	
				}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.handleSLs(SiteRegistration sr, List<SiteList> sls) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.handleSLs(SiteRegistration sr, List<SiteList> sls)");
		}
	}
	
	public DeviceTOBResponseType submitSiteRegsForReTest(List<TechnicalRegistration> techRegs) throws Exception {
		logger.debug("Entering BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		//logger.debug("Entering doTechnicalRegistration:" + sr.getRegistrationId());
		try {
			//TODO 
			//Code to populate the TechRegAlarmInputDto from the TechnicalRegistration
			TechRegAlarmInputDto techRegAlarmInputDto = new TechRegAlarmInputDto();
			List<TechRegInputDto> techRegInputDtoList = new ArrayList<TechRegInputDto>();
			//Commenting this for now.
			//techRegAlarmInputDto.setSoldTo(soldTo);
			//techRegAlarmInputDto.setGrtId(grtId);
			for(TechnicalRegistration trs : techRegs) {
				TechRegInputDto techRegInputDto = new TechRegInputDto();
				techRegInputDto.setAccessType(trs.getAccessType());
				techRegInputDto.setEquipmentNumber(trs.getEquipmentNumber());
				techRegInputDto.setMid(trs.getMid());
				techRegInputDto.setGatewaySEID(trs.getSalGateWaySeid());
				techRegInputDto.setSid(trs.getSid());
				//The list of values are incomplete.
				techRegInputDtoList.add(techRegInputDto);
			}
			techRegAlarmInputDto.setTechRegInputDtoList(techRegInputDtoList);
			
			return (new ARTClient().technicalAlamRegistration(techRegAlarmInputDto));
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType) : ", throwable);	
		} finally {
			logger.debug("Exiting BaseRegistrationService.doTechnicalRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
		}
		return null;
	}
	
	public SRRequest createSR(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, TechRecordEnum type) throws Exception {
		logger.debug("Entering BaseRegistrationService.createSR(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, TechRecordEnum type)");
		logger.debug("Entering createSR for SiteRegistration Id:" + siteRegistration.getRegistrationId() + " type:" + type);
		SRRequest srRequest = null;
		try {
			if(siteRegistration != null) {
				/**
				 * Method for creating SR Request
				 */
				srRequest = getBaseHibernateDao().createSRRequest();
				String filePath = getUploadPath("TechnicalRegistrationAttachment" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls");
				List<Attachment> attachments = new ArrayList<Attachment>();
				if(trs != null && trs.size() > 0 && (type.getType().equals(TechRecordEnum.TR.getType()) || type.getType().equals(TechRecordEnum.SAL_MIGRATION.getType()))) {
					Attachment attachment = new TechnicalOrderDetailWorsheetProcessor().generateWSForTR(trs, siteRegistration, filePath, siteRegistration.getOnsiteEmail(),
							siteRegistration.getOnsitePhone(),
							siteRegistration.getOnsiteFirstName(),
							siteRegistration.getOnsiteLastName());
					attachments.add(attachment);
				} else if(sls != null && sls.size() > 0 && type.getType().equals(TechRecordEnum.SAL_MIGRATION.getType())) {
					Attachment attachment = new TechnicalOrderDetailWorsheetProcessor().generateWSForSL(sls, siteRegistration, filePath, siteRegistration.getOnsiteEmail(),
							siteRegistration.getOnsitePhone(),
							siteRegistration.getOnsiteFirstName(),
							siteRegistration.getOnsiteLastName());
					attachments.add(attachment);
				} else if(type.getType().equals(TechRecordEnum.ALARM.getType())) {
					logger.debug("inside attachment creation for type:" + type);
					if((trs != null && trs.size() > 0) || (sls != null && sls.size() > 0)) {
						Attachment attachment = new TechnicalOrderDetailWorsheetProcessor().generateWSForAlarmAndConnectivity(sls, trs, siteRegistration, filePath);
						attachments.add(attachment);
					}
				}
				if(srRequest != null) {
					SRRequestDto srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
					srRequest = processSRRequest(siteRegistration.getRegistrationId(), siteRegistration.getSoldToId(), attachments, srRequestDto, type);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in BaseRegistrationService.createSR(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, TechRecordEnum type) : ", e);
		} finally {
			logger.debug("Exiting createSR for SiteRegistration Id:" + siteRegistration.getRegistrationId());
			logger.debug("Exiting BaseRegistrationService.createSR(SiteRegistration siteRegistration, List<TechnicalRegistration> trs, List<SiteList> sls, TechRecordEnum type)");
		}
		return srRequest;
	}

	public void sendEmailNoticeForTOBLine(TechnicalRegistration technicalRegistration, SiteList siteList , SiteRegistration siteRegistration, StatusEnum status) {
		logger.debug("Entering BaseRegistrationService.sendEmailNoticeForTOBLine(TechnicalRegistration technicalRegistration, SiteList siteList , SiteRegistration siteRegistration, StatusEnum status)");

		try {
			RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convertForTOBLineItem(technicalRegistration, siteList, siteRegistration, status);
			getMailUtil().sendMailNotification(result);
		} catch (Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.sendEmailNoticeForTOBLine(TechnicalRegistration technicalRegistration, "
					+ "SiteList siteList , SiteRegistration siteRegistration, StatusEnum status) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.sendEmailNoticeForTOBLine(TechnicalRegistration technicalRegistration, SiteList siteList , SiteRegistration siteRegistration, StatusEnum status)");
		}
	}

	/*
	 * [AVAYA] GRT2.0 Send a mail to System Admin
	 */
	public void sendEmailToSystemAdmin(String registrationId,String errMsg,ProcessStepEnum processStep){
		logger.debug("Entering BaseRegistrationService.sendEmailToSystemAdmin(String registrationId,String errMsg,ProcessStepEnum processStep)");
		RegistrationRequestAlert result = null;
		SiteRegistration siteRegistration = null;
		try {
			siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
		} catch (DataAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(siteRegistration !=null){
			result = RegistrationRequestAlertConvertor.convert(siteRegistration, processStep);
			result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_FMW);
			result.setSendMail(false);
			result.setIsSystemMail(true);
			result.setActionRequired(errMsg);
			try {
				getMailUtil().sendMailNotification(result);
				logger.debug("Completed.");
			} catch(Exception e) {
				logger.error("BaseRegistrationService.sendEmailToSystemAdmin(String registrationId,String errMsg,ProcessStepEnum processStep) "
						+ "ERROR: Unexpected failure while trying send email with error: "
						+ e.getMessage());
			}
		}
		
		logger.debug("Exiting BaseRegistrationService.sendEmailToSystemAdmin(String registrationId,String errMsg,ProcessStepEnum processStep)");
	}

	public SRRequest processSRRequest(String siteRegistrationId, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, TechRecordEnum type) throws Exception {
		logger.debug("Entering BaseRegistrationService.processSRRequest(String siteRegistrationId, String soldToId, "
				+ "List<Attachment> attachmentList, SRRequestDto srRequestDto, TechRecordEnum type)");
		SRRequest srRequest =null;
		try {
			if(StringUtils.isNotEmpty(srRequestDto.getSiebelSRNo())) {
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				if (GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr
						.getStatus())) {
					logger.debug("Inside : "+sr.getStatus());
					logger.debug("SR_STATUS SIEBEL_SR_STATUS_PENDING : ");
					srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
					srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
					/**
					 * Method for updating SR Request
					 */
					getBaseHibernateDao().updateSRRequest(srRequest);
				}
			}

			if(SRRequestStatusEnum.INITIATION.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				logger.debug("SR_INITIATION : "+srRequestDto.getStatusId());
				ServiceRequest sr = createSiebelSR(siteRegistrationId, soldToId, type);
				srRequestDto.setSiebelSRNo(sr.getSrNumber());
				srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
				srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.SR_CREATED.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				logger.debug("SR_CREATED : "+srRequestDto.getStatusId());
				for (Attachment att:attachmentList){
					createAttachmentForSiebelSR(srRequestDto.getSiebelSRNo(), att);
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId());
				srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */ 
				getBaseHibernateDao().updateSRRequest(srRequest);
			}

			if(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
				logger.debug("ATTACHMENT_SENT : "+srRequestDto.getStatusId());
				ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
				logger.debug("Inside ATTACHMENT_SENT getSrNumber : "+sr.getSrNumber());
				logger.debug("Inside ATTACHMENT_SENT sr.getStatus() : "+sr.getStatus());
				if(GRTConstants.SIEBEL_SR_STATUS_NEW.equalsIgnoreCase(sr.getStatus())) {
					logger.debug("Inside ATTACHMENT_SENT SIEBEL_SR_STATUS_NEW. : "+sr.getStatus());
					getSiebelClient().assignSR(srRequestDto.getSiebelSRNo());
				}
				else if(GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr.getStatus())) {
					logger.debug("Inside ATTACHMENT_SENT SIEBEL_SR_STATUS_PENDING. : "+sr.getStatus());
					getSiebelClient().updateSR(srRequestDto.getSiebelSRNo());
				}

				srRequestDto.setStatusId(SRRequestStatusEnum.SUCCESS.getStatusId());
				srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
				/**
				 * Method for updating SR Request
				 */
				getBaseHibernateDao().updateSRRequest(srRequest);
			}
		}
		catch (Exception ex) {
			logger.error("BaseRegistrationService.processSRRequest(String siteRegistrationId, String soldToId, "
				+ "List<Attachment> attachmentList, SRRequestDto srRequestDto, TechRecordEnum type) "
				+ "Unexpected exception while processing registration: " , ex);
			//Neeraj Need to be implement this..
			//sendSystemAlert(Id, srRequestDto);
			throw ex;
		}
		logger.debug("Exiting BaseRegistrationService.processSRRequest(String siteRegistrationId, String soldToId, "
				+ "List<Attachment> attachmentList, SRRequestDto srRequestDto, TechRecordEnum type)");
		return srRequest;
	}

	/**
	 * API to get SalAlarm Connectivity Details SALGateway by SalConcentrator.
	 * 
	 * @param seids  List
	 * @return DTO SALGatewayIntrospection
	 */
	public SALGatewayIntrospection getSalAlarmConnectivityDetails(List<String> seids) throws Throwable {
		logger.debug("Entering BaseRegistrationService.getSalAlarmConnectivityDetails(List<String> seids)");
		logger.debug("Entering ARTService-----------------introspectSALGateway");
		SALGatewayIntrospection salGatewayIntrospection = null;
		try {
			salGatewayIntrospection = this.getArtClient().getSalAlarmConnectivityDetails(seids);
		} catch (Throwable throwable) {
			logger.error("BaseRegistrationService.getSalAlarmConnectivityDetails(List<String> seids) : "
					+ "Error while executing getSalAlarmConnectivityDetails", throwable);
			throw throwable;
		}
		logger.debug("Exiting ARTService--------------getSalAlarmConnectivityDetails");
		logger.debug("Exiting BaseRegistrationService.getSalAlarmConnectivityDetails(List<String> seids)");
		return salGatewayIntrospection;
	}

	public boolean isSoldToValidForCurrentUser(String soldTo, String userId, String bpLinkId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.isSoldToValidForCurrentUser(String soldTo, String userId, String bpLinkId)");
		boolean isValid = false;
		try {
			isValid = this.isSoldToValidForTheUser(soldTo, userId, bpLinkId);
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.isSoldToValidForCurrentUser(String soldTo, String userId, String bpLinkId) : ", throwable);
			throw new DataAccessException(
					InstallBaseService.class,
					throwable.getMessage(),
					throwable);
		}
		finally {
			logger.debug("Entering BaseRegistrationService.isSoldToValidForCurrentUser(String soldTo, String userId, String bpLinkId)");
		}
		return isValid;
	}


	public boolean isSoldToValidForTheUser(String soldTo, String userId, String bpLinkId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.isSoldToValidForTheUser(String soldTo, String userId, String bpLinkId)");
		logger.debug("Entering isSoldToValidForTheUser soldTo:" + soldTo + " userId:" + userId + " bpLinkId:" + bpLinkId);
		try{
			if((StringUtils.isEmpty(bpLinkId) || bpLinkId.equalsIgnoreCase(GRTConstants.LDAP_CUST_BP_PLACEHOLDER)) 
					&& getBaseHibernateDao().isSoldToExistForTheUser(soldTo, userId)) {
				return true;
			}
			if(StringUtils.isNotEmpty(bpLinkId) && StringUtils.isNotEmpty(soldTo)) {
				if(getBaseHibernateDao().isBpSoldToExistForTheUser(soldTo, bpLinkId)) {
					return true;
				}
				List<BPAccountTempAccess> bpAccountTempAccess = this.queryAccess(bpLinkId, soldTo);
				if(bpAccountTempAccess != null && bpAccountTempAccess.size() > 0) {
					logger.debug("LOA Found results for soldTo:" + soldTo + " bpLinkId:" + bpLinkId + " result count:" + bpAccountTempAccess.size());
					return true;
				}
			}
			logger.debug("Entering isSoldToValidForTheUser soldTo:" + soldTo + " userId:" + userId + " bpLinkId:" + bpLinkId + " returning false.");
		} catch(Throwable throwable) {
			logger.error("Exception occured in BaseRegistrationService.isSoldToValidForTheUser(String soldTo, String userId, String bpLinkId) : ", throwable);
			throw new DataAccessException(
					InstallBaseService.class,
					throwable.getMessage(),
					throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.isSoldToValidForTheUser(String soldTo, String userId, String bpLinkId)");
		}
		return false;
	}
	/**
	 * Method to get the SoldToId access for a given bpLinkId from CAT/LOA/ESR database.
	 * @param BPLinkId Mandatory
	 * @param soldtoId optional
	 * @return List<BPAccountTempAccess> List of bean populated with BPLinkid, SoldToIs and SoldToName
	 * @throws DataAccessException
	 */
	public List<BPAccountTempAccess> queryAccess(String bpLinkId, String soldToId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.queryAccess(String bpLinkId, String soldToId)");
		logger.debug("Entering queryAccess : bpLinkId:" + bpLinkId + " soldToId:" + soldToId);
		List<BPAccountTempAccess> access = new ArrayList<BPAccountTempAccess>();
		try {
			/*if(catSoldToDAO != null) {
    			access = catSoldToDAO.queryAccess(bpLinkId, soldToId);
    		}*/
			access=getBaseHibernateDao().queryAccess(bpLinkId, soldToId);
		} catch (Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.queryAccess(String bpLinkId, String soldToId) : ", throwable);
		} finally {
			logger.debug("Exiting queryAccess : bpLinkId:" + bpLinkId + " soldToId:" + soldToId);
			logger.debug("Exiting BaseRegistrationService.queryAccess(String bpLinkId, String soldToId)");
		}
		return access;
	}
	public SiteRegistration updateSiteRegistrationProcessStepAndStatus(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException {
		/* Changed for Issue where on validating RV(FV) status validated was changing for TOB[860/861]
		 * SiteRegistration siteRegistration = getBaseHibernateDao().updateSiteRegistrationProcessStepAndStatus(registrationId, ProcessStepEnum.TECHNICAL_REGISTRATION, status);*/
		SiteRegistration siteRegistration = getBaseHibernateDao().updateSiteRegistrationProcessStepAndStatus(registrationId, step, status);
		return siteRegistration;


	}
	
	//Method added for defect#876. Update complete status for Validated Registration.
	public SiteRegistration updateSiteRegistrationCompleteDateForValidate(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException {		 
		SiteRegistration siteRegistration = getBaseHibernateDao().updateSiteRegistrationCompleteDateForValidate(registrationId, step, status);
		return siteRegistration;
	}

	/**
	 * Method to save or update SiteRegistration details.
	 *
	 * @param SiteRegistration siteRegistration
	 * @throws DataAccessException exception
	 * @return SiteRegistration siteRegistration
	 */
	public int saveSiteRegistrationDetails(SiteRegistration siteRegistration) throws DataAccessException {
		return getBaseHibernateDao().saveSiteRegistrationDetails(siteRegistration);
	}

	public List<TechnicalOrderDetail> filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process)");
		List<TechnicalOrderDetail> techList = new ArrayList<TechnicalOrderDetail>();
		List<TechnicalOrderDetail> additionalTechList = new ArrayList<TechnicalOrderDetail>();
		List<String> mcList = new ArrayList<String>();
		Map<String, PipelineSapTransactions> additionalPipelineMap = new HashMap<String, PipelineSapTransactions>();
		TechnicalOrderDetail technicalOrderDetail = null;
		// Filtering against SAP Pipelines
		long quantity = 0;
		boolean addFlag = false;
		boolean included = false;
		try {
			Iterator<TechnicalOrderDetail> iter = filteredTODList.iterator();
			List<PipelineSapTransactions> pipelineSapTransactionsList = getBaseHibernateDao().fetchPipelineSapTransactionOnSoldTo(soldTo);
			if(pipelineSapTransactionsList != null && pipelineSapTransactionsList.size() > 0){
				logger.debug("SAP Pipeline List Size:"+pipelineSapTransactionsList.size());
				while(iter.hasNext()){
					technicalOrderDetail = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(iter.next());
					quantity = technicalOrderDetail.getInitialQuantity();
					for(PipelineSapTransactions pipelineSapTransactions:pipelineSapTransactionsList){
						included = false;
						if(pipelineSapTransactions.getMaterialCode().equalsIgnoreCase(technicalOrderDetail.getMaterialCode())
								&& pipelineSapTransactions.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
							//START : Setting the Reg Status and Nick Name for the pipeline transactions
							/*if(null != technicalOrderDetail.getRegStatus() && StringUtils.isNotEmpty(technicalOrderDetail.getRegStatus())) {
								pipelineSapTransactions.setRegStatus(technicalOrderDetail.getRegStatus());
							} if(null != technicalOrderDetail.getNickName() && StringUtils.isNotEmpty(technicalOrderDetail.getNickName())) {
								pipelineSapTransactions.setNickName(technicalOrderDetail.getNickName());
							} if(null != technicalOrderDetail.getSolutionElementId() && StringUtils.isNotEmpty(technicalOrderDetail.getSolutionElementId())) {
								pipelineSapTransactions.setSolutionElementId(technicalOrderDetail.getSolutionElementId());
							} if(null != technicalOrderDetail.getSolutionElementCode() && StringUtils.isNotEmpty(technicalOrderDetail.getSolutionElementCode())) {
								pipelineSapTransactions.setSolutionElementCode(technicalOrderDetail.getSolutionElementCode());
							} if(null != technicalOrderDetail.getActiveContractExist() && StringUtils.isNotEmpty(technicalOrderDetail.getActiveContractExist())) {
								pipelineSapTransactions.setActiveContractExist(technicalOrderDetail.getActiveContractExist());
							}*/ if(null != technicalOrderDetail.getProductLine() && StringUtils.isNotEmpty(technicalOrderDetail.getProductLine())) {
								pipelineSapTransactions.setProductLine(technicalOrderDetail.getProductLine());
							} /*if(null != technicalOrderDetail.getAssetPK() && StringUtils.isNotEmpty(technicalOrderDetail.getAssetPK())) {
								pipelineSapTransactions.setAssetPK(technicalOrderDetail.getAssetPK());
							}*/
							//END : Setting the Reg Status and Nick Name for the pipeline transactions
							if((technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
									|| (technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber())))
									&& !process.equalsIgnoreCase("SAP")  && pipelineSapTransactions.getAction() != null
									//GRT 4.0 Change : Added condition for Equipment Move
									&& ( pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV) || pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM) ) ){
								included = true;
								//logger.debug("TOD --> MC:"+technicalOrderDetail.getMaterialCode()+"  EQN:"+technicalOrderDetail.getSummaryEquipmentNumber()
								//		+"  Serial#"+technicalOrderDetail.getSerialNumber()+"  TOB:"+technicalOrderDetail.getTechnicallyRegisterable()+"  Qty:"+technicalOrderDetail.getInitialQuantity());
								if ((pipelineSapTransactions.isTechnicallyRegisterable() && technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)) 
										|| (technicalOrderDetail.getSerialNumber() != null && technicalOrderDetail.getSerialNumber().equalsIgnoreCase(pipelineSapTransactions.getSerialNumber()))) {
									logger.debug("PST --> MC:"+pipelineSapTransactions.getMaterialCode()+"  EQN:"+pipelineSapTransactions.getEquipmentNumber()
											+"  Serial#"+pipelineSapTransactions.getSerialNumber()+"  TOB:"+pipelineSapTransactions.isTechnicallyRegisterable()+"  Qty:"+pipelineSapTransactions.getQuantity());
									if(quantity > pipelineSapTransactions.getQuantity()){
										quantity = quantity - pipelineSapTransactions.getQuantity();
										pipelineSapTransactions.setQuantity(0);
									} else {
										pipelineSapTransactions.setQuantity(pipelineSapTransactions.getQuantity() - quantity);
										quantity = 0;
									}
								}
							} else {
								logger.debug("Order Type:"+pipelineSapTransactions.getAction()+"  MC : SAP Pipeline:"+pipelineSapTransactions.getMaterialCode()+"  TO:"+technicalOrderDetail.getMaterialCode());
								//GRT 4.0 Change : Added condition for Equipment Move
								if(pipelineSapTransactions.getAction() !=null &&  pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV) || pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM)){
									included = true;
									if(quantity > pipelineSapTransactions.getQuantity()){
										quantity = quantity - pipelineSapTransactions.getQuantity();
										pipelineSapTransactions.setQuantity(0);
									} else {
										pipelineSapTransactions.setQuantity(pipelineSapTransactions.getQuantity() - quantity);
										quantity = 0;
									}
								} else if (pipelineSapTransactions.getAction() !=null && pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
									if(!technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)){
										included = true;
										quantity = quantity + pipelineSapTransactions.getQuantity();
										pipelineSapTransactions.setQuantity(0);
									}
								}
							}
						}
						if(!included && pipelineSapTransactions.getAction() !=null && pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
							additionalPipelineMap.put(pipelineSapTransactions.getMaterialCode()+pipelineSapTransactions.getEquipmentNumber(), pipelineSapTransactions);
						} else {
							additionalPipelineMap.remove(pipelineSapTransactions.getMaterialCode()+pipelineSapTransactions.getEquipmentNumber());
						}
					}
					if(quantity >= 0 && technicalOrderDetail != null){
						logger.debug("MC:"+technicalOrderDetail.getMaterialCode()+"  Quantity :"+quantity);
						technicalOrderDetail.setInitialQuantity(quantity);
						if(process.equalsIgnoreCase("SAP")){
							technicalOrderDetail.setRemainingQuantity(quantity);
							techList.add(technicalOrderDetail);
						} else if(quantity > 0){
							techList.add(technicalOrderDetail);
						}
					}
				}
				// Additional Pipelines - When no Siebel data returned for this soldTo
				if(filteredTODList == null || filteredTODList.size() == 0){
					for(PipelineSapTransactions pipelineSapTransactions:pipelineSapTransactionsList){
						additionalPipelineMap.put(pipelineSapTransactions.getMaterialCode()+pipelineSapTransactions.getEquipmentNumber(), pipelineSapTransactions);
					}
				}

				if(additionalPipelineMap.size() > 0){
					logger.debug("additionalPipelineMap Size:"+additionalPipelineMap.size());
					for(PipelineSapTransactions pipeline:additionalPipelineMap.values()){
						addFlag = false;
						if(additionalTechList.size() > 0){
							for(TechnicalOrderDetail technicOrderDetailObj:additionalTechList){
								if(pipeline.getMaterialCode() != null 
										&& pipeline.getMaterialCode().equalsIgnoreCase(technicOrderDetailObj.getMaterialCode())
										&& pipeline.getEquipmentNumber()!= null 
										&& pipeline.getEquipmentNumber().equalsIgnoreCase(technicOrderDetailObj.getSummaryEquipmentNumber())){
									addFlag = false;
									if(pipeline.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
										technicOrderDetailObj.setInitialQuantity(technicOrderDetailObj.getInitialQuantity() + pipeline.getQuantity());
									} else {
										technicOrderDetailObj.setInitialQuantity(technicOrderDetailObj.getInitialQuantity() - pipeline.getQuantity());
									}
								} else {
									addFlag = true;
								}
							}
						} else {
							addFlag = true;
						}
						if(addFlag){
							logger.debug("Additional Pipeline in result:"+pipeline.getMaterialCode());
							if(mcList != null && !mcList.contains(pipeline.getMaterialCode())){
								mcList.add(pipeline.getMaterialCode());
							}
							additionalTechList.add(TechnicalRegistrationUtil.constructTODFromPST(pipeline));
						}
					}
				}

				logger.debug("Unprocessed pipelines:"+additionalTechList.size());
				if(mcList != null && mcList.size() > 0){
					Map<String,String> mcDescMap = getMaterialCodeDesc(mcList);
					for(TechnicalOrderDetail technicOrderDetailObj:additionalTechList){
						if(technicOrderDetailObj.getInitialQuantity() != null && technicOrderDetailObj.getInitialQuantity() > 0){
							technicOrderDetailObj.setDescription(mcDescMap != null?mcDescMap.get(technicOrderDetailObj.getMaterialCode()):"");
							techList.add(technicOrderDetailObj);
						}
					}
				}
			} else {// Adding the Siebel Records - When no pipeline records exists for this soldTo
				while(iter.hasNext()){
					technicalOrderDetail = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(iter.next());
					techList.add(technicalOrderDetail);
				}
			}
		} catch (DataAccessException daexception){
			logger.debug("Exception in BaseRegistrationService.filterTechnicalOrderOnPipeline"
					+ "(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) :"+daexception.getMessage());
			throw new DataAccessException(InstallBaseService.class, daexception
					.getMessage(), daexception);
		} catch (Exception e) {
			logger.debug("Exception in BaseRegistrationService.filterTechnicalOrderOnPipeline("
					+ "List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) :"+e.getMessage());
			throw new DataAccessException(InstallBaseService.class, e.getMessage(), e);
		}
		logger.debug("After SAP Pipeline check technicalOrderDetailList Size:"+filteredTODList.size());
		logger.debug("Exiting BaseRegistrationService.filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process)");
		return techList;
	}
	/*
	 * [AVAYA] GRT 2.0  Get material code description
	 */
	public Map<String,String> getMaterialCodeDesc(List<String> materialCodes) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.getMaterialCodeDesc(List<String> materialCodes)");
		Map<String,String> masterMaterial = getBaseHibernateDao().getMaterialCodeDesc(materialCodes);
		logger.debug("Exiting BaseRegistrationService.getMaterialCodeDesc(List<String> materialCodes)");
		return masterMaterial;
	}

	/**
	 * Method to get the Agreements associated with an Asset.
	 *
	 * @param materialCodes List<String>
	 * @return isSuperUser Map<String,String>
	 * @throws DataAccessException custom exception
	 */
	public Map<String,List<String>> getAgreementsForAssets(List<String> assetIds) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.getAgreementsForAssets(List<String> assetIds)");
		Map<String,List<String>> assetAgreementsList = getBaseHibernateDao().getAgreementsForAssets(assetIds);
		logger.debug("Exiting BaseRegistrationService.getAgreementsForAssets(List<String> assetIds)");
		return assetAgreementsList;
	}

	/**
	 * Method to get the Agreements associated with an Asset.
	 *
	 * @param materialCodes List<String>
	 * @return isSuperUser Map<String,String>
	 * @throws DataAccessException custom exception
	 */
	public Map<String,List<String>> getAccessTypeForAssets(List<String> assetIds) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.getAccessTypeForAssets(List<String> assetIds)");
		Map<String,List<String>> assetAccessTypeList = getBaseHibernateDao().getAccessTypesForAssets(assetIds);
		logger.debug("Exiting BaseRegistrationService.getAccessTypeForAssets(List<String> assetIds)");
		return assetAccessTypeList;
	}

	/**
	 * Method to verify super user or not.
	 *
	 * @param userId String
	 * @return isSuperUser boolean true/false
	 * @throws DataAccessException custom exception
	 */
	public boolean isSuperUser(String userId) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.isSuperUser(String userId)");
		SuperUser superUser = getBaseHibernateDao().getSuperUser(userId);

		boolean isSuperUser = false;

		if ((superUser != null) && GRTConstants.Y.equalsIgnoreCase(superUser.getIsSuperUser())) {
			isSuperUser = true;
		}
		logger.debug("Exiting BaseRegistrationService.isSuperUser(String userId)");
		return isSuperUser;
	}

	public boolean isSoldToExcluded(String soldTo) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.isSoldToExcluded(String soldTo)");
		boolean flag = false;

		if(getBaseHibernateDao().isSoldToExcluded(soldTo))
		{
			flag = true;
		}


		logger.debug("Exiting BaseRegistrationService.isSoldToExcluded(String soldTo)");
		return flag;
	}

	/**
	 * Method to get the Registration Summary with the given registrationId.
	 *
	 * @param registrationId
	 *            string
	 * @return result RegistrationSummary
	 */
	public RegistrationSummary getRegistrationSummary(String registrationId)
			throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getRegistrationSummary(String registrationId)");
		RegistrationSummary result = new RegistrationSummary();
		SiteRegistration siteRegistration = getBaseHibernateDao()
				.getSiteRegistration(registrationId);
		if(siteRegistration != null){
			result.setRegistrationId(siteRegistration.getRegistrationId());
			result.setExpedite(siteRegistration.getExpedite());
			SRRequestDto ibSrRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(siteRegistration.getInstallBaseSrRequest());
			result.setInstallBaseSrRequest(ibSrRequestDto);
			SRRequestDto fvSrRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(siteRegistration.getFinalValidationSrRequest());
			result.setFinalValidationSrRequest(fvSrRequestDto);
			result.setOnsiteEmail(siteRegistration.getOnsiteEmail());
			result.setOnsiteFirstName(siteRegistration.getOnsiteFirstName());
			result.setOnsiteLastName(siteRegistration.getOnsiteLastName());
			result.setOnsitePhone(siteRegistration.getOnsitePhone());
			result.setRegistrationNotes(siteRegistration.getRegistrationNotes());
			result.setSoldTo(siteRegistration.getSoldToId());
			result.setToSoldToId(siteRegistration.getToSoldToId());
			result.setProcessStepId(siteRegistration.getProcessStep()
					.getProcessStepId());
			result.setProcessStep(siteRegistration.getProcessStep()
					.getProcessStepShortDescription());

			result.setInstallBaseStatusId(siteRegistration.getInstallBaseStatus().getStatusId());
			result.setInstallBaseStatus(siteRegistration.getInstallBaseStatus().getStatusDescription());
			result.setTechRegStatusId(siteRegistration.getTechRegStatus().getStatusId());
			result.setTechRegStatus(siteRegistration.getTechRegStatus().getStatusDescription());
			result.setFinalValidationStatusId(siteRegistration.getFinalValidationStatus().getStatusId());
			result.setFinalValidationStatus(siteRegistration.getFinalValidationStatus().getStatusDescription());
			if (siteRegistration.getEqrMoveStatus() != null) {
				result.setEqrMoveStatusId(siteRegistration.getEqrMoveStatus().getStatusId());
				result.setEqrMoveStatus(siteRegistration.getEqrMoveStatus().getStatusDescription());
			}

			result.setSiteCountry(siteRegistration.getSiteCountry());
			result.setReportEmailId(siteRegistration.getReportEmailId());
			result.setUserName(siteRegistration.getUserName());
			result.setNoAdditionalProductFlag(siteRegistration.getNoAdditionalProductFlag());
			result.setSkipInstallBaseCreation(siteRegistration.getSkipInstallBaseCreation());
			result.setSalMigrationOnly(siteRegistration.getSalMigrationOnly());
			result.setOnBoarding(siteRegistration.getOnBoardingFileExisting());
			if(siteRegistration.getRegistrationQuestions()!=null){
				logger.debug("Got registration Questons");
				java.util.Set<RegistrationQuestions> getter = siteRegistration.getRegistrationQuestions();
				for(RegistrationQuestions it:getter){
					if(it.getQuestionKey().equalsIgnoreCase("remoteconnctivity")){
						result.setRemoteConnectivity(it.getAnswerKey());
						//logger.debug("Got remote connectivity"+it.getAnswerKey());

					}
				}
			}
			result.setImpl(siteRegistration.getTypeOfImplementation());

			result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());

			logger.debug("SEtting the submitted flag after getting Ibase"+siteRegistration.getSubmitted());
			result.setSubmitted(siteRegistration.getSubmitted());
			result.setSrCompleted(siteRegistration.getIsSrCompleted());
			/* if(siteRegistration.getCut_over_date()!=null){
            	String cutOverDate = DateUtil.formatDate(siteRegistration.getCut_over_date());
            	result.setCutOverDate(cutOverDate);
            }*/
			if(siteRegistration.getInstallBaseSubStatus() != null ){
				result.setInstallBaseSubStatus(siteRegistration.getInstallBaseSubStatus().getStatusDescription());
			} else {
				result.setInstallBaseSubStatus("");
			}
			if(siteRegistration.getFinalValidationSubStatus() != null ){
				result.setFinalValidationSubStatus(siteRegistration.getFinalValidationSubStatus().getStatusDescription());
			} else {
				result.setFinalValidationSubStatus("");
			}
			if(siteRegistration.getEqrMoveSubStatus() != null ){
				result.setEqrMoveSubStatus(siteRegistration.getEqrMoveSubStatus().getStatusDescription());
			} else {
				result.setEqrMoveSubStatus("");
			}
			result.setRequestingCompany(siteRegistration.getCompany());
			result.setInstallBaseSrNo(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getInstallBaseSrRequest()));
			result.setFinalValidationSrNo(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getFinalValidationSrRequest()));
			result.setEqrMoveSrNo(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getEqrMoveSrRequest()));
			result.setActiveSR(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getEqrActiveContractsSrRequest()));
			if( siteRegistration.isRemoteConnectivity() ){
				result.setRemoteConnectivity("Yes");
			} else {
				result.setRemoteConnectivity("No");
			}
			result.setReportPhone(siteRegistration.getReportPhone());
			result.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
			logger.debug("get type of IMPL"+siteRegistration.getTypeOfImplementation());
			result.setIbSubmittedDate(siteRegistration.getIbSubmittedDate());
			result.setIbCompletedDate(siteRegistration.getIbCompletedDate());
			result.setEqrSubmittedDate(siteRegistration.getEqrSubmittedDate());
			result.setEqrCompletedDate(siteRegistration.getEqrCompletedDate());
			result.setEqrMoveSubmittedDate(siteRegistration.getEqrMoveSubmittedDate());
			result.setEqrMoveCompletedDate(siteRegistration.getEqrMoveCompletedDate());
			result.setTobCompletedDate(siteRegistration.getTobCompletedDate());
			result.setCreatedDate(siteRegistration.getCreatedDate());
		}
		logger.debug("Exiting BaseRegistrationService.getRegistrationSummary(String registrationId)");

		return result;
	}

	/**
	 *
	 * get registration id for site contact validation
	 *
	 *
	 * @return
	 */

	public String getRegistrationId() throws DataAccessException {
		//RegistrationDao registrationDao = new RegistrationDao();
		return getBaseHibernateDao().getRegistrationId();
	}

	/**
	 * Method to get the registration detail from CXP DB or Sales Out for the given sold to id.
	 *
	 * @param soltToId String
	 * @return RegistrationList
	 */
	public RegistrationList getRegistrationDetailFromCXPOrSalesOut(String soldToId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getRegistrationDetailFromCXPOrSalesOut[soldTo:" + soldToId + "]");
		List resultSetList = getBaseHibernateDao().getRegistrationDetailFromCXP(soldToId);
		if (resultSetList == null || resultSetList.size() == 0) {
			logger.debug("Finding Customer Name in Sales Out");
			resultSetList = getBaseHibernateDao().getRegistrationDetailFromSalesOut(soldToId);
		}

		RegistrationList registrationList = null;

		if (resultSetList != null && !resultSetList.isEmpty()) {
			registrationList = new RegistrationList();
			if (resultSetList.get(0) != null) {
				registrationList.setCustomerName(resultSetList.get(0).toString());
			}
		}
		logger.debug("Exiting BaseRegistrationService.getRegistrationDetailFromCXPOrSalesOut(String soldToId)");

		return registrationList;
	}
	public void setCXPRegistrationDataIntoFormBean(RegistrationList registrationList, RegistrationFormBean form){
		if (registrationList != null) {
			form.setCompany(registrationList.getCustomerName());
		}
	}

	/**
	 * Method to get the SoldToList for the given user id.
	 */
	public List<String> getCxpSoldToList(String userId, String bpLinkId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getCxpSoldToList(String userId, String bpLinkId)");
		List<String> soldToStringList = new ArrayList<String>();

		try {

			if ((StringUtils.isEmpty(bpLinkId) || GRTConstants.LDAP_CUST_BP_PLACEHOLDER.equalsIgnoreCase(bpLinkId)) && StringUtils.isNotEmpty(userId)) {
				logger.debug(userId + " is a customer, getting soldTos from CXP.");
				List<SoldTo> soldToList = getBaseHibernateDao().getSoldToObjectList(userId);
				Set<String> cxpSoldTos = new HashSet<String>();
				for (SoldTo soldTo : soldToList) {
					cxpSoldTos.add(soldTo.getSoldToId());
				}
				soldToStringList = SoldTo.convertToStringList(soldToList);
				return soldToStringList;
			}
			List<SoldTo> soldToListFromSalesOut = getBaseHibernateDao().getSoldToObjectListFromSalesOut(bpLinkId);

			List<String> soldToStringListFromSalesOut = SoldTo.convertToStringList(soldToListFromSalesOut);
			if (soldToStringListFromSalesOut != null && soldToStringListFromSalesOut.size() > 0) {
				for (String soldTo : soldToStringListFromSalesOut) {
					String [] data = soldTo.split(" ");
					if(data != null & data.length > 0 && StringUtils.isNotEmpty(data[0])) {
						//if (!cxpSoldTos.contains(data[0])) {
						soldToStringList.add(soldTo);
						//}
					}
					/*if (! soldToStringList.contains(soldTo)) {
						soldToStringList.add(soldTo);
					}*/
				}
			}
			/*List<BPAccountTempAccess> tempAccess = this.getGenericClient().queryAccess(bpLinkId, null);
				if(tempAccess == null || tempAccess.size() == 0) {
					tempAccess = this.getRegistrationDao().getBPAccountTempAccess(bpLinkId);
				}*/
			List<BPAccountTempAccess> tempAccess = this.queryAccess(bpLinkId, null);
			if(tempAccess == null || tempAccess.size() == 0) {
				tempAccess = this.getBaseHibernateDao().getBPAccountTempAccess(bpLinkId);
			}
			//List<BPAccountTempAccess> tempAccess = this.getRegistrationDao().getBPAccountTempAccess(bpLinkId);
			List<SoldTo> temps = new ArrayList<SoldTo>();
			if(tempAccess != null & tempAccess.size() > 0) {
				for (BPAccountTempAccess access : tempAccess) {
					SoldTo temp = new SoldTo();
					temp.setCustomerName(access.getAccountName());
					temp.setSoldToId(access.getAccountId());
					temps.add(temp);
				}
				List<String> tempSoldToStringList = SoldTo.convertToStringList(temps);
				for (String soldTo : tempSoldToStringList) {
					if (! soldToStringList.contains(soldTo)) {
						soldToStringList.add(soldTo);
					}
				}
			}
		}catch (Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.getCxpSoldToList(String userId, String bpLinkId) : ", throwable);
			throw new DataAccessException(BaseRegistrationService.class, throwable.getMessage(), throwable);
		} finally {
			logger.debug("Entering BaseRegistrationService.getCxpSoldToList(String userId, String bpLinkId)");
		}
		return soldToStringList;
	}

	public void updateAccountInformation(SiteRegistration siteRegistration) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.updateAccountInformation(SiteRegistration siteRegistration)");
		try {
			getBaseHibernateDao().updateAccountInformation(siteRegistration);
		} catch (Exception ex) {
			logger.debug("Error in BaseRegistrationService.updateAccountInformation(SiteRegistration siteRegistration) : " + ex.getMessage());
			throw new DataAccessException(BaseRegistrationService.class, ex.getMessage(), ex);
		}
		logger.debug("Exiting BaseRegistrationService.updateAccountInformation(SiteRegistration siteRegistration)");
	}

	/**
	 * @param map
	 * @param userId
	 * @param requesterFullName
	 * @param userType
	 * @param bpLinkId
	 * @param ipoFilter
	 * @param endIndex
	 * @param startIndex
	 * @param fetchAllData
	 * @return PaginationForSiteRegistration
	 * @throws DataAccessException
	 * Get the details for showing registration list
	 */
	public PaginationForSiteRegistration getRegListFromDB(Map map, String userId, String requesterFullName, String userType, String bpLinkId, boolean ipoFilter, int endIndex, int startIndex, boolean fetchAllData) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.PaginationForSiteRegistration getRegistrationSummaryList(String userId, String requesterFullName, String userType, String bpLinkId, "
				+ "boolean ipoFilter, int endIndex, int startIndex, boolean fetchAllData)");
		long time1 = System.currentTimeMillis();
		try {
			List<String> cxpSoldToList = new ArrayList<String>();
			String[] soldToIdList = null;
			List<BPAccountTempAccess> bps = null;
			boolean skipSoldTosFetch = false;
			PaginationForSiteRegistration dto = null;
			String filterStr = listToString(map.get(GRTConstants.REQUESTER_NAME_FILTER));
			logger.debug("Current User Name:"+requesterFullName);

			// Compare Current User FullName and requesterName filter string
			if(StringUtils.isNotEmpty(filterStr) && StringUtils.isNotEmpty(requesterFullName) && filterStr.equalsIgnoreCase(requesterFullName)){
				skipSoldTosFetch = true;
			}
			
			//Get the CXP SoldToId for the Non-Agent user
			if (!GRTConstants.AGENT.equalsIgnoreCase(userType)) {
				// Bypass fetching SoldTos from CAT/CXP/SALESOUT when filter is on SoldTo/UserName/RegId
				if(StringUtils.isNotEmpty(userType) && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userType)){
					if(StringUtils.isNotEmpty(listToString(map.get(GRTConstants.REGISTRATION_ID_FILTER)))
							|| StringUtils.isNotEmpty(listToString(map.get(GRTConstants.SOLD_TO_FILTER)))){
						skipSoldTosFetch = true;
					}
					if(StringUtils.isNotEmpty(listToString(map.get(GRTConstants.SOLD_TO_FILTER)))){
						String soldTo = listToString(map.get(GRTConstants.SOLD_TO_FILTER));
						bps = getBaseHibernateDao().queryAccess(bpLinkId, soldTo);
						if((bps == null || bps.size() == 0) && !this.isBpSoldToExistForTheUser(bpLinkId, soldTo)){
							return new PaginationForSiteRegistration();
						}
					}
				}
				if(!skipSoldTosFetch){
					if(StringUtils.isNotEmpty(userType) && GRTConstants.DIRECT_CUSTOMER.equalsIgnoreCase(userType)) {
						long time4 = System.currentTimeMillis();
						cxpSoldToList.addAll(getBaseHibernateDao().getSoldToList(userId));
						long time5 = System.currentTimeMillis();
						logger.debug("time taken in loading CXP record for direct Customer in seconds:" + ((time5-time4)/1000));
					} else if(StringUtils.isNotEmpty(userType) && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userType)) {
						try {
							long time2 = System.currentTimeMillis();
							cxpSoldToList.addAll(getBaseHibernateDao().getSoldToIdFromSalesOut(bpLinkId));
							long time3 = System.currentTimeMillis();
							logger.debug("time taken in loading salesOut in seconds:" + ((time3-time2)/1000) + " for loading :" + cxpSoldToList.size() + " records.");
							//             Getting bpLinkId to soldToId associations ONLY from CAT/LOA and not from CXP
							long time6 = System.currentTimeMillis();
							bps = getBaseHibernateDao().queryAccess(bpLinkId, null);
							long time7 = System.currentTimeMillis();
							if(bps != null && bps.size() > 0) {
								logger.debug("time taken in loading CAT " + (bps.size() + 1) + " record for BPLinkId:" + bpLinkId + " in seconds:" + ((time7-time6)/1000));
							}
							if(bps != null && bps.size() > 0) {
								for (BPAccountTempAccess bp : bps) {
									if(bp != null && StringUtils.isNotEmpty(bp.getAccountId())) {
										cxpSoldToList.add(bp.getAccountId());
									}
								}
							}
						} catch (Throwable throwable) {
							logger.error("Error while getting LOA data", throwable);
						}
					}
					if(cxpSoldToList == null||cxpSoldToList.size()==0){
						return new PaginationForSiteRegistration();
					}
					soldToIdList = (String[]) cxpSoldToList.toArray(new String[0]);
				}
			}
			logger.debug("skipSoldTosFetch:" + skipSoldTosFetch);
			long time8 = System.currentTimeMillis();
			dto = (PaginationForSiteRegistration)getBaseHibernateDao().getRegListFromDB(map, soldToIdList, ipoFilter, userType, bpLinkId, requesterFullName, endIndex, startIndex, fetchAllData);
			long time9 = System.currentTimeMillis();
			logger.debug("time taken by DAO layer in seconds:" + ((time9-time8)/1000));
			List<SiteRegistration> registrationList = dto.getList();
			if( registrationList == null ){
				registrationList = new ArrayList<SiteRegistration>();
			}
			List<RegistrationSummary> registrationSummaryList = new ArrayList<RegistrationSummary>();
			RegistrationSummary registrationSummary = null;
			long time10 = System.currentTimeMillis();
			//Converting the Domain object into DTO.
			for (SiteRegistration siteRegistration : registrationList) {
				registrationSummary = new RegistrationSummary();
				if(StringUtils.isNotEmpty(siteRegistration.getUserName()) &&
						StringUtils.isNotEmpty(userId) &&
						siteRegistration.getUserName().equalsIgnoreCase(userId)) {
					registrationSummary.setOwnedByCurrentUser(true);
				}
				registrationSummary.setRegistrationId(siteRegistration.getRegistrationId());
				registrationSummary.setFullName(siteRegistration.getFirstName()
						+ " " + siteRegistration.getLastName());
				//registrationSummary.setOnsiteFirstName(siteRegistration.getOnsiteFirstName());
				//registrationSummary.setOnsiteLastName(siteRegistration.getOnsiteLastName());
				if (StringUtils.isNotEmpty(siteRegistration.getOnsiteFirstName()) || StringUtils.isNotEmpty(siteRegistration.getOnsiteLastName())) {
					
					registrationSummary.setGrtNotificationName(siteRegistration.getOnsiteFirstName() + " " +siteRegistration.getOnsiteLastName());
				}
				
				registrationSummary.setReportEmailId(siteRegistration.getReportEmailId());
				registrationSummary.setOnsiteEmail(siteRegistration.getOnsiteEmail());
				registrationSummary.setCreatedDate(siteRegistration.getCreatedDate());
				registrationSummary.setUpdatedDate(siteRegistration.getUpdatedDate());
				registrationSummary.setUpdatedBy(siteRegistration.getUpdatedBy());
				registrationSummary.setStatus(siteRegistration.getStatus().getStatusShortDescription());
				
				registrationSummary.setStatusId(siteRegistration.getStatus().getStatusId());
				registrationSummary.setCreatedBy(siteRegistration.getCreatedBy());
				String psId = siteRegistration.getProcessStep().getProcessStepId();
				registrationSummary.setProcessStepId(psId);
				registrationSummary.setProcessStep(siteRegistration.getProcessStep().getProcessStepShortDescription());

				if(siteRegistration.getInstallBaseStatus()!=null){
					registrationSummary.setInstallBaseStatusId(StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusId())? siteRegistration.getInstallBaseStatus().getStatusId():"");
					//registrationSummary.setInstallBaseStatus(StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusDescription())? siteRegistration.getInstallBaseStatus().getStatusDescription():"");
					String ibstatusText=siteRegistration.getInstallBaseStatus().getStatusDescription();
					if(ibstatusText==null || ibstatusText!=""){
						registrationSummary.setInstallBaseStatus(StringUtils.isNotEmpty(StatusEnum.getById(siteRegistration.getInstallBaseStatus().getStatusId()))?StatusEnum.getById(siteRegistration.getInstallBaseStatus().getStatusId()):"") ;
					}else{
						registrationSummary.setInstallBaseStatus(StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusDescription())?siteRegistration.getInstallBaseStatus().getStatusDescription():"") ;
					}
										
					if(siteRegistration.getInstallBaseSubStatus() != null && StringUtils.isNotEmpty(siteRegistration.getInstallBaseSubStatus().getStatusShortDescription())) {
						registrationSummary.setInstallBaseSubStatus(siteRegistration.getInstallBaseSubStatus().getStatusShortDescription());
					}				
				}
				if(siteRegistration.getTechRegStatus()!=null){
					registrationSummary.setTechRegStatusId(StringUtils.isNotEmpty(siteRegistration.getTechRegStatus().getStatusId())?siteRegistration.getTechRegStatus().getStatusId():"");
					//registrationSummary.setTechRegStatus(StringUtils.isNotEmpty(siteRegistration.getTechRegStatus().getStatusDescription())?siteRegistration.getTechRegStatus().getStatusDescription():"");
					String trstatusText=siteRegistration.getTechRegStatus().getStatusDescription();
					if(trstatusText==null || trstatusText!=""){
						registrationSummary.setTechRegStatus(StringUtils.isNotEmpty(StatusEnum.getById(siteRegistration.getTechRegStatus().getStatusId()))?StatusEnum.getById(siteRegistration.getTechRegStatus().getStatusId()):"") ;
					}else{
						registrationSummary.setTechRegStatus(StringUtils.isNotEmpty(siteRegistration.getTechRegStatus().getStatusDescription())?siteRegistration.getTechRegStatus().getStatusDescription():"") ;
					}
				}
				if(siteRegistration.getFinalValidationStatus()!=null){
					registrationSummary.setFinalValidationStatusId(StringUtils.isNotEmpty(siteRegistration.getFinalValidationStatus().getStatusId())? siteRegistration.getFinalValidationStatus().getStatusId():"");
					//GRT 4.0 Chnages
					String statusText=siteRegistration.getFinalValidationStatus().getStatusDescription();
					if(statusText==null || statusText!=""){
						registrationSummary.setFinalValidationStatus(StringUtils.isNotEmpty(StatusEnum.getById(siteRegistration.getFinalValidationStatus().getStatusId()))?StatusEnum.getById(siteRegistration.getFinalValidationStatus().getStatusId()):"") ;
					}else{
						registrationSummary.setFinalValidationStatus(StringUtils.isNotEmpty(siteRegistration.getFinalValidationStatus().getStatusDescription())?siteRegistration.getFinalValidationStatus().getStatusDescription():"") ;
					}
				}
				
				//registrationSummary.setToSoldToId(siteRegistration.getToSoldToId());
				registrationSummary.setSoldTo(siteRegistration.getSoldToId());
				
				if(siteRegistration.getEqrMoveStatus()!=null)
				{
					registrationSummary.setEqrMoveStatusId(StringUtils.isNotEmpty(siteRegistration.getEqrMoveStatus().getStatusId())? siteRegistration.getEqrMoveStatus().getStatusId():"");
					//GRT 4.0 Chnages
					String eqrStatusText=siteRegistration.getEqrMoveStatus().getStatusDescription();
					if(eqrStatusText==null || eqrStatusText!=""){
						registrationSummary.setEqrMoveStatus(StringUtils.isNotEmpty(StatusEnum.getById(siteRegistration.getEqrMoveStatus().getStatusId()))?StatusEnum.getById(siteRegistration.getEqrMoveStatus().getStatusId()):"") ;
					}else{
						registrationSummary.setEqrMoveStatus(StringUtils.isNotEmpty(siteRegistration.getEqrMoveStatus().getStatusDescription())?siteRegistration.getEqrMoveStatus().getStatusDescription():"") ;
					}
					//Defect 146
					if(siteRegistration.getToSoldToId()!=null)
					registrationSummary.setSoldTo(siteRegistration.getSoldToId()+"/"+siteRegistration.getToSoldToId());
				}
				
				
				registrationSummary.setCustomerName(siteRegistration.getCustomerName());
				registrationSummary.setRequestingCompany(siteRegistration.getCompany());
				registrationSummary.setUserName(siteRegistration.getUserName());
				registrationSummary.setImplementationType(siteRegistration.getTypeOfImplementation());
				if(siteRegistration!=null && siteRegistration.getRegistrationType()!=null && (siteRegistration.getRegistrationType().getRegistrationId())!=null){
					registrationSummary.setRegistrationTypeId(StringUtils.isNotEmpty(siteRegistration.getRegistrationType().getRegistrationId())?siteRegistration.getRegistrationType().getRegistrationId():"");
					//registrationSummary.setRegistrationTypeDesc(StringUtils.isNotEmpty(siteRegistration.getRegistrationType().getRegistrationType())?siteRegistration.getRegistrationType().getRegistrationType():"");
					 
					String registrationTypeDesc=siteRegistration.getRegistrationType().getRegistrationType();
					if(registrationTypeDesc==null || registrationTypeDesc!=""){
					registrationSummary.setRegistrationTypeDesc(StringUtils.isNotEmpty(RegistrationTypeEnum.getById(siteRegistration.getRegistrationType().getRegistrationId()))?RegistrationTypeEnum.getById(siteRegistration.getRegistrationType().getRegistrationId()):"") ;
					}else{
					registrationSummary.setRegistrationTypeDesc(StringUtils.isNotEmpty(siteRegistration.getRegistrationType().getRegistrationType())?siteRegistration.getRegistrationType().getRegistrationType():"") ;
					}
				}

				String activeSR = null;
				SRRequest srRequest = null;
				//if(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId().equalsIgnoreCase(psId)) {
				srRequest = siteRegistration.getInstallBaseSrRequest();
				activeSR = fetchSiebelSRNo(srRequest);
				//}
				//if(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId().equalsIgnoreCase(psId)) {
				srRequest = siteRegistration.getFinalValidationSrRequest();
				SRRequest activeContractSR = siteRegistration.getEqrActiveContractsSrRequest();
				if(activeSR != null){
					activeSR = activeSR + (fetchSiebelSRNo(srRequest)!=null?","+ fetchSiebelSRNo(srRequest):"")
							+ (fetchSiebelSRNo(activeContractSR)!=null?","+ fetchSiebelSRNo(activeContractSR):"");
				} else {
					activeSR = fetchSiebelSRNo(srRequest);
					if (activeSR != null) {
						activeSR += (fetchSiebelSRNo(activeContractSR)!=null?","+ fetchSiebelSRNo(activeContractSR):"");
					} else {
						activeSR = (fetchSiebelSRNo(activeContractSR));
					}
				}
				
				//GRT 4.0 Change : Get SR request for Equipment Move
				SRRequest eqmoveSRRequest = siteRegistration.getEqrMoveSrRequest();
				String eqmoveActiveSR  = fetchSiebelSRNo(eqmoveSRRequest);
				eqmoveActiveSR = eqmoveActiveSR == null ? "" : eqmoveActiveSR;
				if( activeSR != null && !StringUtils.isEmpty(activeSR) &&  eqmoveActiveSR != null && !StringUtils.isEmpty(eqmoveActiveSR)){
					activeSR += ","+ eqmoveActiveSR;
				}else if(eqmoveActiveSR != null && !StringUtils.isEmpty(eqmoveActiveSR)){
					activeSR = eqmoveActiveSR;
				}
				
				//}
				registrationSummary.setActiveSR(activeSR);
				registrationSummary.setFormulaActiveSR(siteRegistration.getActiveSR());
				registrationSummary.setNoAdditionalProductFlag(siteRegistration.getNoAdditionalProductFlag());
				registrationSummary.setSalMigrationOnly(siteRegistration.getSalMigrationOnly());
				if(siteRegistration.isOnBoardingFileExisting()){
					logger.debug("in file existing check"+siteRegistration.getRegistrationId());
					registrationSummary.setIsOnBoardingExist(true);
					if(siteRegistration.getOnBoardingFileExisting()!=null){
						//logger.debug("in getting on boarding XML"+siteRegistration.getOnBoardingFileExisting());
						registrationSummary.setOnBoarding(siteRegistration.getOnBoardingFileExisting());
					}
				}else{
					registrationSummary.setIsOnBoardingExist(false);
				}
				registrationSummary.setSrCompleted(siteRegistration.getIsSrCompleted());

				//GRT 4.0:: Registration Name field to be shown in list page
				registrationSummary.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());

				// Add the DTO into List
				registrationSummaryList.add(registrationSummary);
			}
			long time11 = System.currentTimeMillis();
			logger.debug("time taken by iterating over " + (registrationList.size()+1) + " records in seconds:" + ((time11-time10)/1000));
			logger.debug("Completed.");
			dto.setSummaryList(registrationSummaryList);
			logger.debug("Exiting PaginationForSiteRegistration getRegistrationSummaryList(String userId, String requesterFullName, String userType, String bpLinkId, "
					+ "boolean ipoFilter, int endIndex, int startIndex, boolean fetchAllData)");
			return dto;
		} finally {
			long time12 = System.currentTimeMillis();
			logger.debug("time taken by regListFromDB in seconds:" + ((time12-time1)/1000));
		}
	}

	/**
	 * Method to fetch the Equipment Removal records
	 * @param soldTo
	 * @param vCode
	 * @return
	 * @throws DataAccessException
	 */
	public List<TechnicalOrderDetail> fetchEquipmentRemovalRecords(String soldTo, String vCode, boolean fetchSALGWRecords) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.fetchEquipmentRemovalRecords(String soldTo, String vCode)");
		List<TechnicalOrderDetail> technicalOrderDetailList = null;
		TechnicalOrderDetail technicalOrderDetail = null;
		List<TechnicalOrderDetail> filteredTODList = new ArrayList<TechnicalOrderDetail>();
		// Fetching Equipment data from Siebel
		try {
			technicalOrderDetailList = getBaseHibernateDao().getEquipmentRemovalRecords(soldTo, getvCodesList());
			// If record is TRed, fetch only main record in the Siebel view
			if(technicalOrderDetailList != null && technicalOrderDetailList.size() > 0){
				logger.debug("technicalOrderDetailList Size:"+technicalOrderDetailList.size());
				List<String> materialAndSECodeCombinationList = new ArrayList<String>();
				List<TechnicalOrderDetail> techRegisteredRecords = new ArrayList<TechnicalOrderDetail>();
				for(TechnicalOrderDetail technicalOrderDetailRecord:technicalOrderDetailList){
					if(technicalOrderDetailRecord.getTechnicallyRegisterable() != null
							&& technicalOrderDetailRecord.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)){
						techRegisteredRecords.add(technicalOrderDetailRecord);
					} else {
						filteredTODList.add(technicalOrderDetailRecord);
					}
				}
				// New method added to prepare TOD list for filtering MAINs
				int count = 0;
				List<TechnicalOrderDetail> todForCheckingMains = new ArrayList<TechnicalOrderDetail>();
				List<TechnicalOrderDetail> filteredTODs = new ArrayList<TechnicalOrderDetail>();
				Map<String, List<TechnicalOrderDetail>> todMap = constructTRedRecordsListForMainCheck(techRegisteredRecords);
				materialAndSECodeCombinationList = new ArrayList<String>();
				if(todMap != null && todMap.size() > 0){
					for(Entry<String, List<TechnicalOrderDetail>> entry : todMap.entrySet()){
						count = (entry.getValue()!=null?entry.getValue().size():0);
						//logger.debug("Value:Count:"+count+"        Key:"+entry.getKey());
						//if(count > 1){
						for(TechnicalOrderDetail todElement:entry.getValue()){
							todForCheckingMains.add(todElement);
							//logger.debug("McSECode:"+todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode());
							/** Commented the below code to remove the GroupID from TR Main check as few SEIDs are missing on screen which are must
							 * if(!materialAndSECodeCombinationList.contains(todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode()+"#"+todElement.getGroupId())){
								//materialAndSECodeCombinationList.add(todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode());
								//Check for MAIN records based on SeCode & GroupId, not secode alone because
								//There is some material code e.g. VUS which is main in one group(SPTEM_CMUS_225144) but children in other group(SPTEM_CMSMP_225144)
								materialAndSECodeCombinationList.add(todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode()+"#"+todElement.getGroupId());
							}*/
							if(!materialAndSECodeCombinationList.contains(todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode())){
								materialAndSECodeCombinationList.add(todElement.getMaterialCode()+"#"+todElement.getSolutionElementCode());
							}
						}
					}
				}
				filteredTODList.addAll(filteredTODs);
				List<String> filterMCSECodes = null;
				if(materialAndSECodeCombinationList != null && materialAndSECodeCombinationList.size() > 0){
					logger.debug("materialAndSECodeCombinationList Size:"+materialAndSECodeCombinationList.size());
					filterMCSECodes = this.filterMainTRRecords(materialAndSECodeCombinationList);
				}
				String[] mcSe = null;
				int flag = 0;
				if(filterMCSECodes != null){
					logger.debug("filterMCSECodes Size:"+filterMCSECodes.size());
					// Filter Main TOBed records
					Iterator filteredTODIter =  todForCheckingMains.iterator();
					while(filteredTODIter.hasNext()){
						technicalOrderDetail = (TechnicalOrderDetail)filteredTODIter.next();
						for(String mcSecode:filterMCSECodes){
							flag = 0;
							mcSe = mcSecode.split("#");
							if(technicalOrderDetail.getMaterialCode().equalsIgnoreCase(mcSe[0])
									&& technicalOrderDetail.getSolutionElementCode().equalsIgnoreCase(mcSe[1])){
								//logger.debug("Main MC:"+technicalOrderDetail.getMaterialCode()+"   SE Code:"+technicalOrderDetail.getSolutionElementCode());
								if(filteredTODs.size() > 0){
									TechnicalOrderDetail tempTOD = null;
									for(int index = 0; index < filteredTODs.size(); index++){
										tempTOD = filteredTODs.get(index);
										if(StringUtils.isNotEmpty(tempTOD.getSid()) && StringUtils.isNotEmpty(tempTOD.getMid()) && StringUtils.isNotEmpty(tempTOD.getEquipmentNumber())
												&& tempTOD.getSid().equalsIgnoreCase(technicalOrderDetail.getSid())
												&& tempTOD.getMid().equalsIgnoreCase(technicalOrderDetail.getMid())
												&& tempTOD.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getEquipmentNumber())){
											if(StringUtils.isNotEmpty(tempTOD.getGroupId())){
												//filteredTODIter.remove();
											} else if(StringUtils.isNotEmpty(technicalOrderDetail.getGroupId())){
												filteredTODs.set(index, technicalOrderDetail);
											}
											flag = 1;
										} else {
											flag = 0;
										}
									}
								}
								if(flag == 0){
									filteredTODs.add(technicalOrderDetail);
								}
							}
						}
					}
					// Filter child Records which are having different EQN than Main MC+EQN which share same SID and MID
					List<TechnicalOrderDetail> chiildTODList = new ArrayList<TechnicalOrderDetail>();
					// Invalid SECODE issue Fix
					List<TechnicalOrderDetail> sidmidLessList = new ArrayList<TechnicalOrderDetail>();
					// Invalid SECODE issue Fix
					int outerFlag = 0;
					int innerFlag = 0;
					filteredTODIter =  todForCheckingMains.iterator();
					while(filteredTODIter.hasNext()) {
						technicalOrderDetail = (TechnicalOrderDetail)filteredTODIter.next();
						if(StringUtils.isNotEmpty(technicalOrderDetail.getSid()) && StringUtils.isNotEmpty(technicalOrderDetail.getMid())
								&& StringUtils.isNotEmpty(technicalOrderDetail.getEquipmentNumber())) {
							if(filteredTODs.size() > 0) {
								outerFlag = 0;
								for(TechnicalOrderDetail tempTechnicalOrderDetail : filteredTODs){
									if(technicalOrderDetail.getEquipmentNumber().equalsIgnoreCase(tempTechnicalOrderDetail.getEquipmentNumber())
											&& technicalOrderDetail.getSid().equalsIgnoreCase(tempTechnicalOrderDetail.getSid()) 
											&& technicalOrderDetail.getMid().equalsIgnoreCase(tempTechnicalOrderDetail.getMid())) {
										outerFlag = 1;
									}
								}
								if(outerFlag == 0) {
									if(chiildTODList.size() > 0) {
										innerFlag = 0;
										for(TechnicalOrderDetail childTOD : chiildTODList){
											if(technicalOrderDetail.getEquipmentNumber().equalsIgnoreCase(childTOD.getEquipmentNumber())
													&& technicalOrderDetail.getSid().equalsIgnoreCase(childTOD.getSid())
													&& technicalOrderDetail.getMid().equalsIgnoreCase(childTOD.getMid())) {
												innerFlag = 1;
											}
										}
										if(innerFlag == 0) {
											chiildTODList.add(technicalOrderDetail);
										}
									} else {
										chiildTODList.add(technicalOrderDetail);
									}
								}
							} else {
								chiildTODList.add(technicalOrderDetail);
							}
						}else {
							sidmidLessList.add(technicalOrderDetail);
						}

					}
					for(TechnicalOrderDetail childTODItem : chiildTODList){
						logger.debug("MC:"+childTODItem.getMaterialCode()+"  EQN:"+childTODItem.getEquipmentNumber()+"  SECODE:"+childTODItem.getSolutionElementCode()
								+"  SEID:"+childTODItem.getSolutionElementId()+"  SID:"+childTODItem.getSid()+"  MID:"+childTODItem.getMid());
					}
					if(chiildTODList.size() > 0){
						filteredTODList.addAll(chiildTODList);
					}
					
					// Invalid SECODE Change Fix-Added for assets which are not TOB mains nor having SID/MID
					try{
						if(sidmidLessList.size() > 0){
							for(TechnicalOrderDetail parentTOD : filteredTODList){
								for(TechnicalOrderDetail sidmidLessTOD : sidmidLessList){
									if(parentTOD.getSolutionElementId() != null && !parentTOD.getSolutionElementId().equals(sidmidLessTOD.getSolutionElementId())){
										filteredTODList.add(sidmidLessTOD);
									}
								}
							}
							//filteredTODList.addAll(sidmidLessList);
						}
						
					}catch(Exception Ex){
						logger.debug("Exception in BaseRegistrationService.fetchEquipmentRemovalRecords(String soldTo, String vCode) :"+Ex.getMessage());
					}
					// Invalid SECODE Change Fix-Added for assets which are not TOB mains nor having SID/MID
					
				}
				//filteredTODList.addAll(todForCheckingMains);
				filteredTODList.addAll(filteredTODs);
				
				//GRT 4.0 Change : 
				List<TechnicalOrderDetail> orphanRecs = getBaseHibernateDao().fetchEquipmentRemovalRecordsForSALGWAndVSALGW(soldTo, getvCodesList());
				if (fetchSALGWRecords) {
					// Adding assets with SECode SALGW(& MC:V00328) and VSALGW
					filteredTODList.addAll( orphanRecs );
				}else{
					//GRT 4.0 : (fetchSALGWRecords==false)This condition is for equipment move - VSALGW records should not be included in the list
					//Filter the VSALGW records
					List<TechnicalOrderDetail> orphanRecsSALGW = new ArrayList<TechnicalOrderDetail>(); 
					for( TechnicalOrderDetail techOrderObj : orphanRecs ){
						if( techOrderObj.getSolutionElementCode()!=null && !techOrderObj.getSolutionElementCode().equalsIgnoreCase(GRTConstants.SAL_VIRTUAL_GATEWAY)
									|| !techOrderObj.getSolutionElementCode().equalsIgnoreCase(GRTConstants.SAL_GATEWAY)){
							orphanRecsSALGW.add(techOrderObj); 
						}
					}
					//add the filtered list without VSALGW
					filteredTODList.addAll( orphanRecsSALGW );
				}
				
				logger.debug("technicalOrderDetailList Size:"+filteredTODList.size());
			}
		} catch (DataAccessException daexception){
			logger.debug("Data Exception in BaseRegistrationService.fetchEquipmentRemovalRecords(String soldTo, String vCode) :"+daexception.getMessage());
			throw new DataAccessException(BaseRegistrationService.class, daexception
					.getMessage(), daexception);
		}catch (Exception e) {
			logger.debug("Exception in BaseRegistrationService.fetchEquipmentRemovalRecords(String soldTo, String vCode) :"+e.getMessage());
			e.printStackTrace();
		}
		logger.debug("Exiting BaseRegistrationService.fetchEquipmentRemovalRecords(String soldTo, String vCode)");
		return filteredTODList;
	}
	
	private Map<String, List<TechnicalOrderDetail>> constructTRedRecordsListForMainCheck(List<TechnicalOrderDetail> filteredTODList){
		logger.debug("Entering BaseRegistrationService.constructTRedRecordsListForMainCheck(List<TechnicalOrderDetail> filteredTODList)");
		String key = "";
		List<TechnicalOrderDetail> todList = null;
		Map<String, List<TechnicalOrderDetail>> filterMap = new HashMap<String, List<TechnicalOrderDetail>>();
		for(TechnicalOrderDetail tod : filteredTODList){
			if(tod != null && StringUtils.isNotEmpty(tod.getSolutionElementCode())){
				key = (tod.getEquipmentNumber()!=null?tod.getEquipmentNumber():"") //+ ":" + (tod.getMaterialCode()!=null?tod.getMaterialCode():"")
						+ ":" + (tod.getSid()!=null ? tod.getSid():"")
						+ ":" + (tod.getMid()!=null ? tod.getMid():"");
				logger.debug("Key :" + key);
				if(filterMap.size() > 0 && filterMap.containsKey(key)){
					todList = filterMap.get(key);
					if(todList == null){
						todList = new ArrayList<TechnicalOrderDetail>();
					}
					todList.add(tod);
					filterMap.put(key, todList);
				} else {
					todList = new ArrayList<TechnicalOrderDetail>();
					todList.add(tod);
					filterMap.put(key, todList);
				}
			}
		}
		logger.debug("Exiting BaseRegistrationService.constructTRedRecordsListForMainCheck(List<TechnicalOrderDetail> filteredTODList)");
		return filterMap;
	}
	/**
	 * API returns collection of TR main MC+SeCodes
	 *
	 * @param mcSecodes "MC|SeCode"
	 * @return
	 * @throws DataAccessException
	 */
	public List<String> filterMainTRRecords(List<String> mcSecodes) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.filterMainTRRecords(List<String> mcSecodes)");
		List<String> results = new ArrayList<String>();
		String materialCode = "";
		String secode = "";
		//String groupId = "";
		try {
			if(mcSecodes!= null) {
				outerLoop:
					for (String mcSecode : mcSecodes) {
						String[] mcSe = mcSecode.split("#");
						materialCode = mcSe.length >=1? mcSe[0]:"";
						secode = mcSe.length >=2? mcSe[1]:"";
						//groupId = mcSe.length >=3? mcSe[2]:"";
						List<TRConfig> trConfigs = this.getBaseHibernateDao().getTRConfigData(materialCode);
						if(trConfigs != null) {
							innerLoop:
								for (TRConfig config : trConfigs) {
									//Defect #748 : Check for MAIN records based on SeCode & GroupId, not secode alone
									//if(config.getSeCode().equalsIgnoreCase(mcSe[1])) {
									/** Removed GroupID from MAIN check as few SEIDs are not being displayed which actually should be.
									if(config.getSeCode().equalsIgnoreCase(secode) && config.getGroupId().equalsIgnoreCase(groupId)) {*/
									if(config.getSeCode().equalsIgnoreCase(secode)) {
										results.add(mcSecode);
										break innerLoop;// outerLoop;
									}
								}
						}
					}
			}
		} catch(Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.filterMainTRRecords(List<String> mcSecodes) : ", throwable);
		}
		logger.debug("Exiting BaseRegistrationService.filterMainTRRecords(List<String> mcSecodes)");
		return results;
	}

	/**
	 *
	 * @param materialCodes
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> validateMaterialExclusion(
			List<String> materialCodes) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.validateMaterialExclusion(List<String> materialCodes)");
		Map<String, Object> excludedMap = null;
		List<com.avaya.grt.mappers.MaterialExclusion> meList = getBaseHibernateDao().validateMaterialExclusion(
				materialCodes);
		if(meList != null && meList.size() > 0){
			/**
			 * Added to fecth the Material Exclusion Source in the order PLDS/KMAT/DefectiveORBlue/NMPC
			 */
			Map<String, MaterialExclusion> map = new HashMap<String, MaterialExclusion>();
			MaterialExclusion mex = null;
			for(MaterialExclusion mtxc : meList){
				if(map.size() > 0 ){
					if(!map.containsKey(mtxc.getMaterialCode())){
						map.put(mtxc.getMaterialCode(), mtxc);
					} else {
						mex = map.get(mtxc.getMaterialCode());
						if(mex.getExclusionOrder() < mtxc.getExclusionOrder()){
							map.put(mtxc.getMaterialCode(), mtxc);
						}
					}
				} else {
					map.put(mtxc.getMaterialCode(), mtxc);
				}
			}
			excludedMap = new HashMap<String, Object>();
			for(String entry : map.keySet()){
				mex = map.get(entry);
				excludedMap.put(mex.getMaterialCode(), mex.getExclusionSource());
				logger.debug(mex.getMaterialCode()+"   "+mex.getExclusionSource());
			}
		}
		logger.debug("Exiting BaseRegistrationService.validateMaterialExclusion(List<String> materialCodes)");
		return excludedMap;
	}
	
	/**
	 *
	 * @param materialCodes
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> populateMaterialSerialize(
			List<String> materialCodes) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.populateMaterialSerialize(List<String> materialCodes)");
		Map<String, Object> materialMasterMap = null;
		List<com.avaya.grt.mappers.MasterMaterial> materialMasterList = getBaseHibernateDao().populateMaterialSerialize(
				materialCodes);
		if(materialMasterList != null && materialMasterList.size() > 0){
			materialMasterMap = new HashMap<String, Object>();
			for(com.avaya.grt.mappers.MasterMaterial material : materialMasterList){
				materialMasterMap.put(material.getMaterialCode(), material.getSerialized());
			}
		}
		logger.debug("Exiting BaseRegistrationService.populateMaterialSerialize(List<String> materialCodes)");
		return materialMasterMap;
	}
	
	public Map<String, Object> populateMaterialMasterDetails(
			List<String> materialCodes) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.populateMaterialMasterDetails(List<String> materialCodes)");
		Map<String, Object> materialMasterMap = null;
		List<com.avaya.grt.mappers.MasterMaterial> materialMasterList = getBaseHibernateDao().populateMaterialSerialize(
				materialCodes);
		if(materialMasterList != null && materialMasterList.size() > 0){
			materialMasterMap = new HashMap<String, Object>();
			for(com.avaya.grt.mappers.MasterMaterial material : materialMasterList){
				materialMasterMap.put(material.getMaterialCode(), material);
			}
		}
		logger.debug("Exiting BaseRegistrationService.populateMaterialMasterDetails(List<String> materialCodes)");
		return materialMasterMap;
	}
	

	/**
	 *
	 * @param bpLinkId
	 * @param soldToId
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isBpSoldToExistForTheUser(String bpLinkId, String soldToId) throws DataAccessException {		
		logger.debug("In isBpSoldToExistForTheUser for BPLinkId:"+bpLinkId+"   SoldToId:"+soldToId);
		return getBaseHibernateDao().isBpSoldToExistForTheUser(soldToId, bpLinkId);
	}

	/**
	 * 	parse data from list
	 * */
	private static String listToString(Object obj){
		if (obj instanceof List) {
			if(((List)obj).size()>0){
				return ((List)obj).get(0).toString().trim().toLowerCase();
			}
		} else if (obj instanceof String) {
			return (String)obj;
		}
		return "";
	}
	private String fetchSiebelSRNo(SRRequest srRequest){
		String activeSR = null;
		if(srRequest != null) {
			if(StringUtils.isNotEmpty(srRequest.getSiebelSRNo())) {
				activeSR = srRequest.getSiebelSRNo();
			} else {
				activeSR = GRTConstants.SIEBEL_SR_CREATION_ERROR;
			}
		}
		return activeSR;
	}
	/**
	 * Method to verify Mega user or not.
	 *
	 * @param userId String
	 * @return isSuperUser String
	 * @throws DataAccessException custom exception
	 */
	public String isMegaUser(String userId) throws DataAccessException{
		logger.debug("Entering BaseRegistrationService.isMegaUser(String userId)");
		SuperUser superUser = getBaseHibernateDao().getSuperUser(userId);
		String isMegaUser = "0";
		
		isMegaUser = superUser != null ? superUser.isMegaUser(): isMegaUser;
		logger.debug("Exiting BaseRegistrationService.isMegaUser(String userId)" + isMegaUser);

		return isMegaUser;
	}

	public List<Alert> getActiveAlerts() throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getActiveAlerts()");

		List<Alert> alertList = getBaseHibernateDao().getActiveAlerts();

		logger.debug("Exiting BaseRegistrationService.getActiveAlerts()");
		return alertList;
	}
	public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.getBusinessPartner(String bpLinkId) for bpLinkId:" + bpLinkId);
		//BusinessPartner bp = getRegistrationDao().getBusinessPartner(bpLinkId);
		BusinessPartner bp = this.getBaseHibernateDao().getBusinessPartner(bpLinkId);
		logger.debug("Exiting BaseRegistrationService.getBusinessPartner(String bpLinkId) for bpLinkId:" + bpLinkId);
		return bp;
	}

	public Account getCustomerAccountDetails(String userName) {
		logger.debug("Entering BaseRegistrationService.getCustomerAccountDetails(String userName) :" + userName);
		Account account = null;
		try {
			if(StringUtils.isNotEmpty(userName)) {
				List<String> accounts = this.getBaseHibernateDao().getSoldToList(userName);
				if(accounts != null && accounts.size() > 0) {
					logger.debug("Loading Siebel account details for :" + accounts.get(0));
					account = this.getSiebelClient().queryAccount(accounts.get(0));
				}
			}
		} catch (Throwable throwable) {
			logger.error("Exception in BaseRegistrationService.getCustomerAccountDetails(String userName) : ", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.getCustomerAccountDetails(String userName) :" + userName);
		}
		return account;
	}
	public BaseHibernateDao getBaseHibernateDao() {
		return baseHibernateDao;
	}

	public void setBaseHibernateDao(BaseHibernateDao baseHibernateDao) {
		this.baseHibernateDao = baseHibernateDao;
	}
	
	/* Service For Registration Page  */
	 /**
	 * @return List<Status>
	 * Method to get all the registration status available
	 */
	public String getRegistrationStatus(){
		logger.debug("Entering BaseRegistrationService.getRegistrationStatus()");
		List<Status> statusList = getBaseHibernateDao().getRegistrationStatus();
		Set<String> status = new TreeSet<String>();
		for(Status obj : statusList){
			status.add(obj.getStatusDescription().trim());
		}
		logger.debug("Exiting BaseRegistrationService.getRegistrationStatus()"); 
		return status.toString();
	}
	
	
	/**
	 * *
	 * *
	 * *
	 * *
	 * @throws DataAccessException 
	 * **/
	public boolean isIorBSoldToSAPMapping(String soldTo) throws DataAccessException
	{
		return getBaseHibernateDao().isIorBSoldToSAPMapping(soldTo);
	}
	
	
	  /**
     * @return
     *  Method to get the registration types
     */
    public String getRegistrationTypes(){
    	logger.debug("Entering BaseRegistrationService.getRegistrationTypes()"); 
    	List<RegistrationType> regTypesList = getBaseHibernateDao().getRegistrationTypes();
    	List<String> reglist = new ArrayList<String>();
    	
    	//Defect #674 : code to maintain order in registration list
		LinkedList<String> finalList = new LinkedList<String>();
		List<String> orderList = new ArrayList<String>(
				Arrays.asList(	RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID(),
								RegistrationTypeEnum.INSTALLBASEONLY.getRegistrationID(),
								RegistrationTypeEnum.TECHNICALREGISTRATIONONLY.getRegistrationID(),
								RegistrationTypeEnum.EQUIPMENTREMOVALONLY.getRegistrationID(),
								RegistrationTypeEnum.EQUIPMENTMOVEONLY.getRegistrationID(),
								RegistrationTypeEnum.SALMIGRATION.getRegistrationID(),
								RegistrationTypeEnum.IPOFFICE.getRegistrationID()));
		
		List<String> orderRegType = new ArrayList();
		for( String order : orderList ){
			for(RegistrationType obj : regTypesList){
				if( order.equalsIgnoreCase(obj.getRegistrationId()) ){
					finalList.add(obj.getRegistrationTypeDesc());
				}
			}
		}
    	
    	logger.debug("Exiting BaseRegistrationService.getRegistrationTypes()"); 
		return finalList.toString();
    }
    
    /**
     * @param registrationId
     * @param onsiteEmail
     * @return 
     * @throws DataAccessException
     */
    public RegistrationSummary updateEmail(String registrationId, String onsiteEmail) throws DataAccessException{
    	logger.debug("Entering BaseRegistrationService.updateEmail(String registrationId, String onsiteEmail)"); 
    	RegistrationSummary dto = new RegistrationSummary();
    	SiteRegistration siteRegistration = getBaseHibernateDao().updateEmail(registrationId, onsiteEmail);
    	dto.setUpdatedBy(siteRegistration.getUpdatedBy());
    	dto.setUpdatedDate(siteRegistration.getUpdatedDate());
    	logger.debug("Exiting BaseRegistrationService.updateEmail(String registrationId, String onsiteEmail)");
    	return dto;
    }
    
    /**
     * @param registrationId
     * @param emailId
     * @return boolean
     */
    public boolean emailOnBoardingFile(String registrationId, String emailId, String serviceName) {
    	logger.debug("Entering BaseRegistrationService.emailOnBoardingFile(String registrationId, String emailId, String serviceName)"); 
		 boolean messageSent = false;
		 logger.debug("emailOnBoardingFile>>>>>>>>>>>>>>>>>>>>>" + registrationId + ":" + emailId);
		 try {
			 if(StringUtils.isNotEmpty(registrationId)) {
				 SiteRegistration siteRegistration = getBaseHibernateDao().getSiteRegistration(registrationId);
				 if(siteRegistration != null) {
					 TechnicalRegistration technicalRegistration = siteRegistration.getIPORecord();
					 if(technicalRegistration != null) {
						 technicalRegistration.setIpoUserEmail(emailId);
						 sendRegistrationRequestOnBoarding(technicalRegistration, serviceName);
						 messageSent = true;
					 } else {
						 logger.debug("IPO TechnicalRegistration record not found");
					 }
				 } else {
					 logger.debug("registration not loaded");
				 }
			 } else {
				 logger.debug("registrationId is null");
			 }
		 } catch(Throwable throwable) {
			 logger.error("BaseRegistrationService.emailOnBoardingFile(String registrationId, String emailId, String serviceName) "
			 		+ "Error while sending OnBoarding XML file", throwable);
		 }
		 
		 logger.debug("Exiting BaseRegistrationService.emailOnBoardingFile(String registrationId, String emailId, String serviceName)"); 
		 return messageSent;
	 }
    
    public void sendRegistrationRequestOnBoarding(TechnicalRegistration technicalRegistration, String serviceName) throws Exception {
    	logger.debug("Entering BaseRegistrationService.sendRegistrationRequestOnBoarding(TechnicalRegistration "
    			+ "technicalRegistration, String serviceName) : ["       
                + technicalRegistration.getTechnicalRegistrationId() + "]");
        RegistrationRequestAlert result = null;
        SiteRegistration siteRegistration = null;
        try {
        	if( technicalRegistration.getTechnicalOrder() != null){
        	siteRegistration = technicalRegistration.getTechnicalOrder().getSiteRegistration();
        	}
            if(siteRegistration != null){
        	result = RegistrationRequestAlertConvertor.convertTechRegIPO(siteRegistration, technicalRegistration, "RN", serviceName);
            }
            
            result.setTechnicalRegistrationId(technicalRegistration.getTechnicalRegistrationId());
   			result.setStatus(StatusEnum.COMPLETED.getStatusShortDescription());
            result = populateActionRequired(result);
            result.setOnBoarding(true);
            result.setAlarmId(technicalRegistration.getAlarmId());
            result.setAttatchmentContents(technicalRegistration.getOnboarding());
            result.setIpoFilePath(technicalRegistration.getOnboarding());
            
        } catch(Exception e) {
            logger.error("BaseRegistrationService.sendRegistrationRequestOnBoarding(TechnicalRegistration "
    			+ "technicalRegistration, String serviceName) : ERROR: While trying to collect data to send. Error message: " + e.getMessage());
            throw e;
        }

        this.getMailUtil().sendMailNotification(result);
        logger.debug("Exiting BaseRegistrationService.sendRegistrationRequestOnBoarding(TechnicalRegistration "
    			+ "technicalRegistration, String serviceName) : ["       
                + technicalRegistration.getTechnicalRegistrationId() + "]");

    }
    
    public void sendStepBEmail(String registrationId, StatusEnum status, String siebelSrNumber) throws DataAccessException {
        logger.debug("Entering sendStepBEmail");
        try {
        	if(StringUtils.isNotEmpty(registrationId)) {
        		SiteRegistration siteRegistration = this.getBaseHibernateDao().getSiteRegistration(registrationId);
		        if(siteRegistration != null) {
		        	siteRegistration.setStepBInProcessAction(true);
		        	//sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION, status, siebelSrNumber);
		        	sendRegistrationRequestAlertForStepBInProcess(siteRegistration, siebelSrNumber);
		        }
        	}
        }catch(Throwable throwable) {
        	logger.error("", throwable);
        } finally {
        	logger.debug("Exiting sendStepBEmail");
        }
    }
    
    public void sendRegistrationRequestAlertForStepBInProcess(SiteRegistration siteRegistration, String siebelSRNumber) throws Exception {
		logger.debug("Entering sendRegistrationRequestAlertForStepBInProcess");
		try {
			RegistrationRequestAlert result = RegistrationRequestAlertConvertor.convert(siteRegistration, ProcessStepEnum.TECHNICAL_REGISTRATION);
			result.setSiebelSRNumber(siebelSRNumber);
	        result = populateActionRequired(result);
	        getMailUtil().sendMailNotification(result);
		} catch(Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting sendRegistrationRequestAlertForStepBInProcess");
		}
    }
    
    /**
	 *
	 * @param bpLinkId
	 * @param soldToId
	 * @return
	 * @throws DataAccessException
	 */
	public boolean validateBpHasAccessToCatSoldTo(String bpLinkId, String soldToId) throws DataAccessException {
		logger.debug("Entering BaseRegistrationService.validateBpHasAccessToCatSoldTo with BPLinkId :" + bpLinkId + " soldToId:" + soldToId);
		boolean accessFlag = false;
		try {
			/*if(catSoldToDAO != null) {
				List<BPAccountTempAccess> bpAccess = catSoldToDAO.queryAccess(bpLinkId, soldToId);
				if(bpAccess != null && bpAccess.size() > 0){
					accessFlag = true;
				}
			}*/
			
			List<BPAccountTempAccess> bpAccess = getBaseHibernateDao().queryAccess(bpLinkId, soldToId);
			if(bpAccess != null && bpAccess.size() > 0){
				accessFlag = true;
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting BaseRegistrationService.validateBpHasAccessToCatSoldTo(bpLinkId, soldToId) returning accessFlag ["+accessFlag +"]");
		}
		return accessFlag;
	}
	
	 /**
     * Method to get the technical order details for the given registration id and orderType.
     *
     * @param registrationId
     *            String
     * @return technicalOrderDtoList List
     */
    public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType) throws DataAccessException {
    	logger.debug("Entering BaseRegistrationService.getTechnicalOrderByType with registrationId :" + registrationId + " orderType:" + orderType);
    	long c1 = Calendar.getInstance().getTimeInMillis();
        List<TechnicalOrder> technicalOrderDetail = getBaseHibernateDao()
                .getTechnicalOrderByType(registrationId, orderType);
        List<TechnicalOrderDetail> technicalOrderDtoList = TechnicalRegistrationUtil.prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug(c2-c1 +" milliseconds");
        logger.debug("Exiting BaseRegistrationService.getTechnicalOrderByType(String registrationId, String orderType) returning technicalOrderDtoList ["+technicalOrderDtoList.size() +"]");
        return technicalOrderDtoList;
    }
    
    /**
     * Method to update the site registration  status by the given registrationId, processStepId and statusId.
     *
     * @param registrationId string
     * @param statusId
     * @return registration SiteRegistration
     */
    public SiteRegistration updateSiteRegistrationBySuperUser(String registrationId, String statusId, String userId) throws DataAccessException {
    	SiteRegistration siteRegistration = null;
    	logger.debug("Entering  BaseRegistrationService.updateSiteRegistrationBySuperUser");
    	try {
    		siteRegistration = getBaseHibernateDao().updateSiteRegistrationBySuperUser(registrationId, statusId, userId);
    		ProcessStepEnum processStep = null;
    		if(siteRegistration.getTempProcessStep() != null){
    			processStep = siteRegistration.getTempProcessStep();
    		}else{
    			processStep = siteRegistration.getProcessStep().getProcessStepEnum();
    		}

	        sendRegistrationRequestAlert(registrationId, processStep, StatusEnum.COMPLETED);
    	} catch (Exception ex) {
    		throw new DataAccessException(BaseRegistrationService.class, ex.getMessage(), ex);
    	}
    	logger.debug("Exiting BaseRegistrationService.updateSiteRegistrationBySuperUser");

    	return siteRegistration;
    }
    
    /**
     * @param siteRegistration
     * @return SiteRegistration
     * @throws DataAccessException
     * Refresh the site regisration object
     */
    public SiteRegistration refreshSiteRegistration( SiteRegistration siteRegistration ) throws DataAccessException{
    	logger.debug("Entering  BaseRegistrationService.refreshSiteRegistration");
    		siteRegistration = getBaseHibernateDao().refreshSiteRegistration(siteRegistration);
    	logger.debug("Exiting  BaseRegistrationService.refreshSiteRegistration");
    	return siteRegistration;
    }
    
    public CurrentUserService getCurrentUserService() {
        return currentUserService;
    }

    public void setCurrentUserService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }
    
    public List<TechnicalRegistrationSummary> getTechnicalRegistrationListByregistrationId(String registrationId,String Type)
    {
    	return getBaseHibernateDao().getTechnicalRegistrationListByregistrationId(registrationId,Type);
    }
    
    public List<TechnicalRegistration> getTechnicalRetestRegistrationListByregistrationId(String registrationId ,String Type)
    {
    	return getBaseHibernateDao().getTechnicalRetestRegistrationListByregistrationId(registrationId,Type);
    }
    
    public List<TechnicalRegistration> getTechnicalRetestRegistrationListByregistrationId(String registrationId)
    {
    	return getBaseHibernateDao().getTechnicalRetestRegistrationListByregistrationId(registrationId);
    }
    
    public void updateTechnicalRegistration(String technicalRegistrationId)throws DataAccessException
    {
    	getBaseHibernateDao().updateTechnicalRegistration(technicalRegistrationId);
    }
    
    /**
	    * *•	Automating test alarming and connectivity (BR-F.009)
	    * *
	    * *******/
	    public List<String> getEntitledForAlarming(List<String> lst,String soldTOId)
	    {
	    	return getBaseHibernateDao().getEntitledForAlarming(lst, soldTOId);
	    }
	    
	    public String getProductLineByMaterialCode(String materialCode) throws DataAccessException {
	    	return getBaseHibernateDao().getProductLineByMaterialCode(materialCode);
	    }
	    
	    //Method for fetching registration details on graph click
	    public PaginationForSiteRegistration getRegListForGraph(String userId, String graphView) throws DataAccessException{
	    	PaginationForSiteRegistration page = getBaseHibernateDao().getRegListForGraph(userId,graphView);
	    	return page;
	    }
	    
	    //Chat Phase 1 : Chat on/off configuration 
	    public Map<String,String> getChatConfiguration() throws DataAccessException{
			logger.debug("Entering BaseRegistrationService.getChatConfiguration()");
			Map<String,String> chatConfiguration = getBaseHibernateDao().getChatConfiguration();
			logger.debug("Exiting BaseRegistrationService.getChatConfiguration()");
			return chatConfiguration;
		}
}

