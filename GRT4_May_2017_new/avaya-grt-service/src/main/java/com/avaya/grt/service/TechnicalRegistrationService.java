package com.avaya.grt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.lob.SerializableClob;

import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveResponse;
import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.Attachment;
import com.grt.dto.EquipmentMoveDataDTO;
import com.grt.dto.EquipmentMoveResponseDto;
import com.grt.dto.EquipmentRemovalDataDTO;
import com.grt.dto.EquipmentRemovalResponseDto;
import com.grt.dto.InstallBaseAsset;
import com.grt.dto.InstallBaseAssetData;
import com.grt.dto.InstallBaseRespDataDto;
import com.grt.dto.InstallBaseSapResponseDto;
import com.grt.dto.SRRequestDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.ARTOperationType;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.SkipDateTimeEscalator;
import com.grt.util.StatusEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;



public class TechnicalRegistrationService extends TechnicalRegistrationArtService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationService.class);
	
	//Property - salGatewayMaterialCode from properties file to be injected from spring
	public String salgwMC;
	//Property - ibaseQueue from properties file to be injected from spring
	public String ibaseQueue;
	//Property - eqrQueue from properties file to be injected from spring
	public String eqrQueue;
	//Property - ib_intiated_before_hours from properties file to be injected from spring
	public String ibIntiatedBeforeHours;
	private static final String ATTACHMENT_NAME = "InstallBaseCreationAttachment";
	
	public TechnicalRegistrationDao technicalRegistrationDao;
	
	/*
     * [AVAYA] GRT 2.0  Process Install Base SAP response
     */
    public InstallBaseSapResponseDto processReceivedInstallBase(InstallBaseSapResponseDto installBaseSapResponseDto) throws Throwable {
		StringBuffer sb = new StringBuffer();
		sb.append(installBaseSapResponseDto.getRoutingInfo().toString());
		try {
			logger.debug("Entering TechnicalRegistrationService.processReceivedInstallBase");
    		sb.append("Entering into TechnicalRegistrationService.processReceivedInstallBase Method to process Install Base Payload... ");
    		SkipDateTimeEscalator.set(Boolean.TRUE);
	    	Set<String> dups = new HashSet<String>();
	    	List<InstallBaseRespDataDto> errorList = new ArrayList<InstallBaseRespDataDto>();
	    	List<TechnicalOrder> techOrderList = new ArrayList<TechnicalOrder>();
	    	//Begin "Response Details Received from SAP"
	    	List<PipelineSapTransactions> pipelineSapTransactionsList = new ArrayList<PipelineSapTransactions>();
	    	//End "Response Details Received from SAP"
	    	SiteRegistration siteRegistration = null;
	    	Set<String> errors = new HashSet<String>();
	    	try {
	    	logger.debug("-RegistrationId "+installBaseSapResponseDto.getRegistrationId());
			siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(installBaseSapResponseDto.getRegistrationId());
			sb.append("Site Registration Object Received... ");
			if(siteRegistration != null && !siteRegistration.getInstallBaseStatus().getStatusId().equalsIgnoreCase(GRTConstants.COMPLETED)) {
				sb.append("Inside first if block, Processing ahead as its Install base status is not completed... ");
				//String destination = this.customerLookup(siteRegistration.getSoldToId());
				if(installBaseSapResponseDto.getErrorId()!= null) {
					sb.append("Inside second If when Error ID is not null in base response... ");
					if(installBaseSapResponseDto.getErrorId().equals(GRTConstants.installBasefile_successCode)) {
						logger.debug("ErrorId - "+installBaseSapResponseDto.getErrorId());
						sb.append("Inside third if Processing ahead when error Id is coming as 0 which means Success code... ");
						if(installBaseSapResponseDto.getIBaseRespData() != null) {
							sb.append("Inside forth if, Ibase Data from the Payload is processed and Error Key will be generated if there is error description available in Payload Data - It means Handling failure case -");
							for(InstallBaseRespDataDto installBaseRespData: installBaseSapResponseDto.getIBaseRespData()) {
								logger.debug("Error desccription"+installBaseRespData.getErrorDesc());
								if(StringUtils.isNotEmpty(installBaseRespData.getErrorDesc())) {
									errors.add(installBaseRespData.getMaterialCode() + "-" + installBaseRespData.getErrorDesc());
									String key = "";
									try {
										String materialCode = installBaseRespData.getMaterialCode();
										String serialNumber = ((StringUtils.isEmpty(installBaseRespData.getSerialNumber()))?"-":installBaseRespData.getSerialNumber());
										String error = installBaseRespData.getErrorDesc();
										key = materialCode + ":" + serialNumber + ":" + error;
										key = key.toUpperCase();
										if(dups.contains(key)) {
											continue;
										} else {
											dups.add(key);
										}

									} catch(Throwable throwable){
										logger.error("error while finding dups for key:" + key);
										sb.append("error while finding dups for key:" + key+"... ");
									}
									/*logger.debug("Before appending zeros to material code"+installBaseRespData.getMaterialCode());
									installBaseRespData.setMaterialCode(GRTUtil.appendZerosToMaterialCode(installBaseRespData.getMaterialCode()));
									logger.debug("After appending zeros to material code"+installBaseRespData.getMaterialCode());*/
				    				logger.debug("Storing install base data in TechnicalOrderDetail table");
				    				TechnicalOrder techOrder = siteRegistration.getTechnicalOrderByMaterialCode(installBaseRespData.getMaterialCode(), installBaseRespData.getSerialNumber());
				    					if(techOrder!=null ){
				    						sb.append("Technical Order for the Material Code is found and its not null... ");
				    						techOrder.setError_Desc(installBaseRespData.getErrorDesc());
				    						//Updating openquantity
				    						Long openQuantity = techOrder.getOpenQuantity();
				    						Long initialQuantity = techOrder.getInitialQuantity();
				    						if(StringUtils.isNotEmpty(installBaseRespData.getQuantity())){
				    							Long IbaseErrorQuantity = Long.parseLong(installBaseRespData.getQuantity());
				    							if(openQuantity == null){
				    								if(initialQuantity >= IbaseErrorQuantity){
				    									openQuantity = initialQuantity - IbaseErrorQuantity;
				    								}
				    							} else if(openQuantity != 0){
				    								if(openQuantity >= IbaseErrorQuantity){
				    									openQuantity = openQuantity - IbaseErrorQuantity;
				    								}else{
				    									openQuantity = 0L;
				    								}
				    							}
				    						}
				    						techOrder.setOpenQuantity(openQuantity);
				    						logger.debug("Technical Order Open Quantity after reducing error records - "+techOrder.getOpenQuantity());
				    						sb.append("Technical Order Open Quantity after reducing error records - "+techOrder.getOpenQuantity()+"... ");
				    						techOrderList.add(techOrder);
				    	    				errorList.add(installBaseRespData);
				    					}
									} else {
										logger.debug("Handling success case");
										sb.append("Process the success case when Error Description is empty in Payload... ");
										//Begin "Response Details Received from SAP"
										String serialNumber = ((StringUtils.isEmpty(installBaseRespData.getSerialNumber()))?"-":installBaseRespData.getSerialNumber());
										Date dateTime = new Date();
										if(siteRegistration != null) {
											installBaseRespData.setShipToId(siteRegistration.getSoldToId());
											installBaseRespData.setSoldToId(siteRegistration.getSoldToId());
										}
										PipelineSapTransactions pipelineSapTransactions = populateSapTransactionDetails(installBaseSapResponseDto.getRegistrationId(), serialNumber,
																installBaseRespData, dateTime, siteRegistration.getIbSubmittedDate());
										pipelineSapTransactionsList.add(pipelineSapTransactions);
										sb.append("Pipeline SAP Transaction list is updated with the Payload Data-Response Details Received from SAP... ");
										//End "Response Details Received from SAP"
										TechnicalOrder techOrder = siteRegistration.getTechnicalOrderByMaterialCode(installBaseRespData.getMaterialCode(), installBaseRespData.getSerialNumber());
										if(techOrder != null) {
											
											logger.debug("TechOrder found id:" + techOrder.getOrderId() + " registrationType:" + siteRegistration.getRegistrationType().getRegistrationType());
											sb.append("TechOrder found id:" + techOrder.getOrderId() + " registrationType:" + siteRegistration.getRegistrationType().getRegistrationType()+"... ");
											if(siteRegistration.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID())) {
												sb.append("In case of End To End Registeration... ");
												if(StringUtils.isNotEmpty(siteRegistration.getCreatedBy())
														&& GRTConstants.USER_ID_SYSTEM.equals(siteRegistration.getCreatedBy())
														&& StringUtils.isNotEmpty(techOrder.getMaterialCode())
														&& techOrder.getMaterialCode().equalsIgnoreCase(getSalgwMC())) {
													//Dispathc a call to ART for SAL gateway creation.
													//1. Create a TR from TO
													sb.append("Saving Technical Registeration table with the TR details... ");
									    			TechnicalRegistration technicalRegistration = new TechnicalRegistration();
									    			technicalRegistration.setTechnicalOrder(techOrder);
									    			technicalRegistration.setAccessType(GRTConstants.SAL);
									    			technicalRegistration.setProductCode(GRTConstants.SALGW_PRODUCT_TYPE);
									    			technicalRegistration.setSolutionElement(techOrder.getSolutionElementCode());
									    			//technicalRegistration.setNumberOfSubmit("1");
									    			Status status = new Status();
										    		status.setStatusId(StatusEnum.INITIATE.getStatusId());
										    		technicalRegistration.setStatus(status);
									    			technicalRegistration.setGroupId(getGroupId(techOrder.getMaterialCode(), techOrder.getSolutionElementCode()));
									    			technicalRegistration.setSummaryEquipmentNumber(installBaseRespData.getAssetNumber());
									    			technicalRegistration.setOperationType(GRTConstants.DN);
									    			technicalRegistration.setStepASubmittedDate(new Date());
									    			saveTechnicalRegistration(technicalRegistration);
									    			//2. Make a call to ART
									    			List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
									    			trs.add(technicalRegistration);
													//this.getSalRegistrationService().createGatewayNew(siteRegistration.getSoldToId(), siteRegistration.getRegistrationId() , GRTConstants.DEFAULT_ART_USER, null, GRTConstants.FN);
									    			logger.debug("<-BEFORE TR ASYNCHRONOUS CALL > ");
									    			if ( siteRegistration.getInstallBaseStatus() != null && StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusId())
									    					&& !siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
									    					&& !siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())	){	
									    				sb.append(" Handling the TRs if Install base status is Completed or Cancelled... ");
									    				doTechnicalRegistration(siteRegistration, trs, null, ARTOperationType.DATABASENEW );
									    				sb.append("Completed Technical Registeration for ART Operation as Database New... ");
									    			}
													logger.debug("<-AFTER TR ASYNCHRONOUS CALL > ");
												}
											} else if(siteRegistration.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.IPOFFICE.getRegistrationID())) {
												sb.append("Preparing for IP Office Handling... ");
											logger.debug("Preparing for IP Office Handling");
											Set<String> eligibleAccessTypes = new LinkedHashSet<String>();
											eligibleAccessTypes.add(" ");
											eligibleAccessTypes.addAll(getEligibleAccessTypes(techOrder.getMaterialCode()));
											logger.debug("<ElligibleAccesType->"+eligibleAccessTypes.contains(GRTConstants.ACCESS_TYPE_IPO));  
											if(eligibleAccessTypes.contains(GRTConstants.ACCESS_TYPE_IPO) && StringUtils.isNotEmpty(techOrder.getIsBaseUnit()) && techOrder.getIsBaseUnit().equalsIgnoreCase(GRTConstants.Y) && techOrder.isAutoTR()) {
												logger.debug("Its is a BaseUnit");
												try {
													sb.append("Create a TR from TO for Base Unit IP Office only... ");
													logger.debug("**********************************************");
													logger.debug("Create a TR from TO");
													logger.debug("**********************************************");
													TechnicalRegistration technicalRegistration = new TechnicalRegistration();
										    		technicalRegistration.setSoldToId(techOrder.getSiteRegistration().getSoldToId());
										    		technicalRegistration.setSolutionElement(techOrder.getSolutionElementCode());
										    		String groupId = this.getGroupId(techOrder.getMaterialCode(), techOrder.getSolutionElementCode());
										    		technicalRegistration.setGroupId(groupId);
										    		//Need to verify, this IPO is a right value for AccesType.
										    		technicalRegistration.setAccessType(GRTConstants.ACCESS_TYPE_IPO);
										    		technicalRegistration.setSoftwareRelease(techOrder.getReleaseString());
										    		//technicalRegistration.setNumberOfSubmit("1");
										    		technicalRegistration.setCreatedDate(new Date());
										    		Status status = new Status();
										    		status.setStatusId(StatusEnum.INITIATE.getStatusId());
										    		technicalRegistration.setStatus(status);
										    		technicalRegistration.setOperationType(ARTOperationType.ONBOARDXML.getOperationKey());
										    		technicalRegistration.setSummaryEquipmentNumber(installBaseRespData.getAssetNumber());
										    		technicalRegistration.setTechnicalOrder(techOrder);
										    		//technicalRegistration.setEquipmentNumber(installBaseRespData.getAssetNumber());
										    		technicalRegistration.setStepASubmittedDate(new Date());
										    		//Implemented this method in TechnicalRegistrationDao
										    	 	String trId = this.getTechnicalRegistrationDao().createTR(technicalRegistration);										    		
										    		if(StringUtils.isEmpty(technicalRegistration.getTechnicalRegistrationId())) {
										    			technicalRegistration.setTechnicalRegistrationId(trId);
										    		}
										    		List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
										    		trs.add(technicalRegistration);
										    		if ( siteRegistration.getInstallBaseStatus() != null && StringUtils.isNotEmpty(siteRegistration.getInstallBaseStatus().getStatusId())
									    					&& !siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
									    					&& !siteRegistration.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())	){
										    			sb.append(" Handling the TRs if Install base status is Completed or Cancelled For IP Office... ");
										    			doTechnicalRegistration(siteRegistration, trs, null, ARTOperationType.ONBOARDXML);
										    			sb.append("Completed Technical Registeration for ART Operation as OnBoarded XML... ");
										    		}
												//logger.debug("ART Response to IPO API "+ ARTResponseToIPOReg);
												} catch (Throwable throwable) {
													logger.debug("Error in submit"+throwable.getMessage());
													logger.error("Error in submit 2", throwable);
													throwable.printStackTrace();
												}
											}
										}
									}
								}
							}
				    	} else if(installBaseSapResponseDto.getErrorId().equals(GRTConstants.installBasefile_baddata_errorCode)){
				    	
				    		logger.error("Incomplete data or bad data in file send by SAP"+installBaseSapResponseDto.getIBaseRespData().get(0).getErrorDesc());
				    		sb.append("Incomplete data or bad data in file send by SAP"+installBaseSapResponseDto.getIBaseRespData().get(0).getErrorDesc()+"... ");
				    		installBaseSapResponseDto.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
				    	} else{
				    		logger.error("Error while processing the install base"+installBaseSapResponseDto.getIBaseRespData().get(0).getErrorDesc());
				    		sb.append("Error while processing the install base"+installBaseSapResponseDto.getIBaseRespData().get(0).getErrorDesc()+"... ");
				    		installBaseSapResponseDto.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
				    	}
					} else {
						logger.debug("Error id code is not '0' which means unsuccess");
						sb.append("Error id code is not '0' which means failure... ");
					}
					} else {
						logger.debug("Error id is coming null");
						sb.append("Error id is coming null... ");
						
					}
				} else {
					logger.debug("SiteRegistration is coming null (OR) InstallBase status is completed before asynchronous response come from SAP.");
					sb.append("SiteRegistration is coming null (OR) InstallBase status is completed before asynchronous response come from SAP... ");
				}
	    	} catch (DataAccessException e) {
				logger.debug("Error while getting the Site Registration for the registrationId-"+installBaseSapResponseDto.getRegistrationId());
				sb.append("Site Registration for the registrationId " +installBaseSapResponseDto.getRegistrationId()+ " is coming null... ");
				installBaseSapResponseDto.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
			}
	    	logger.debug("++++++errorList.size()+++++++++"+errorList.size());
	    	if (errorList.size() > 0) {
	    		logger.debug("Create SR");
	    		sb.append("Creating Service request in case of the errorlist is available... ");
	    		SRRequestDto srRequestDto = null;
	    		//Generating file path
	    		String filePath = autoGenAttachmentPath(ATTACHMENT_NAME);
	    		// Generating worksheet
	    		TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
	    		List<Attachment> attList = new ArrayList<Attachment>();
	    		
	    		//implemented method in TechnicalOrderDetailWorksheetProcessor
	    		
	    		Attachment attachment = worksheet.generateWorksheetIBaseErrors(errorList,
	    																	   installBaseSapResponseDto.getRegistrationId(),
	    																	   filePath,
	    																	   siteRegistration);	    		
	    		attList.add(attachment);
	    		//
	    		SRRequest srRequest = null;
	    		//Implemented this method in TechnicalRegistrationDao
	    		
	    		try {
	    			sb.append("Initiating SR Request... ");
	    			srRequest = getTechnicalRegistrationDao().initSRRequest(
	    					siteRegistration,ProcessStepEnum.INSTALL_BASE_CREATION);	    			
	    		} catch (DataAccessException e) {
	    			logger.error("Unable to create an SR Request Id on table", e);
	    			sb.append("Exception in initiation SR Request - "+e.getMessage()+"... ");
	    			throw e;
	    		}
	    		
	    		if (srRequest != null) {
	    			srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
	    			this.processSRRequest(installBaseSapResponseDto
	    					.getRegistrationId(), siteRegistration.getSoldToId(), attList, srRequestDto, ProcessStepEnum.INSTALL_BASE_CREATION);
	    			sb.append("SR request is processed successfully... ");
	    		}
	    		if(techOrderList != null){
	    			if(techOrderList.size() > 0){
	    				logger.debug("techOrderList.size() in InstallBaseRequest"+techOrderList.size());
	    				for(TechnicalOrder techOrder : techOrderList){
	    					techOrder.setSr_Created("yes");
	    				}
	    				getTechnicalRegistrationDao().saveTechnicalOrderList(techOrderList);
	    				sb.append(" Technical order list is saved successfully for ID: "+getTechnicalRegistrationArtDao().getRegistrationId()+"... ");
	    			}
	    		}
	    		logger.debug("Setting IsSrCompleted to no on SiteRegistration");
	    		//Implemented this method in TechnicalRegistrationDao
	    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, GRTConstants.NO);
	    		logger.debug("Sending mail For IBase waiting Info coz error code returned from SAP"+siteRegistration.getRegistrationId());
	    		sb.append("Sending mail For IBase waiting Info coz error code returned from SAP while updating the SR Completed... ");
	    		
	    		sb.append("Sending mail For IBase waiting Info coz error code returned from SAP- "+siteRegistration.getRegistrationId()+"... ");
	    		ProcessStepEnum processStep = ProcessStepEnum.INSTALL_BASE_CREATION;
	    		//Implemented this method in TechnicalRegistrationDao
	    		getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.AWAITINGINFO, processStep, false);
	    		sb.append("IB Status is updated for ID: "+siteRegistration.getRegistrationId()+"... ");
	    		sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), processStep, StatusEnum.AWAITINGINFO, errors);
	    	}
	    	//Begin "Response Details Received from SAP"
	    	//Save the PipelineSapTransactions into Database.
	    	if(pipelineSapTransactionsList != null && pipelineSapTransactionsList.size() > 0) {
	    		sb.append("Saving pipelineSapTransactionsList... ");
	    		//Implemented this method in TechnicalRegistrationDao
	    		getTechnicalRegistrationDao().savePipelineSapTransactionsList(pipelineSapTransactionsList);
	    	}
	    	//Updating IB sub status to SAP Completion
	    	//Implemented this method in TechnicalRegistrationDao
	    	if(siteRegistration != null) {
		    	getTechnicalRegistrationDao().updateSiteRegistrationSubStatus(siteRegistration, StatusEnum.SAPCOMPLETED, ProcessStepEnum.INSTALL_BASE_CREATION);
		    	sb.append(" Updating the SiteRegisteration status as SAP Completed for Install Base Creation... ");
				// Check for AssetUpdate
				try {
					long hours = TechnicalRegistrationUtil.calculateNoOfHours(siteRegistration.getIbSubmittedDate());
					if(hours > Long.parseLong(getIbIntiatedBeforeHours())){
						processSiebelUpdateResponse(siteRegistration.getRegistrationId());
					}
				} catch (Exception e) {
					logger.debug("Exception while processing Asset Update. Exception:"+e.getMessage());
				}
	    	}
	    	//End "Response Details Received from SAP"
    	} catch(Throwable throwable) {
    		sb.append("Error while processing SAP Asynch response... ");
    		logger.error("Error while processing SAP Asynch response", throwable);
    		throw new Exception(throwable);
    	} finally {
    		SkipDateTimeEscalator.unset();
    		logger.debug("Exiting TechnicalRegistrationService.processReceivedInstallBase");
    	}
		sb.append("Exiting TechnicalRegistrationService.processReceivedInstallBase... ");
		installBaseSapResponseDto.setRoutingInfo(sb.toString());
		return installBaseSapResponseDto;
    }
    
    /*
     * [AVAYA] GRT 3.0  Process Equipment Removal response
     *
     * handling of GRT.EquipmentRemoval.Response.Queue
     *
     */
    public EquipmentRemovalDataDTO processReceivedEquipmentRemovalReq(EquipmentRemovalDataDTO equipmentRemovalDataDTO) throws Exception {
    	logger.debug("Entering TechnicalRegistrationService.processReceivedEquipmentRemovalReq");
    	StringBuffer sb = new StringBuffer();
		sb.append(equipmentRemovalDataDTO.getRoutingInfo().toString());
		sb.append("Entering TechnicalRegistrationService.processReceivedEquipmentRemovalReq to process EQR payload...");
    	try {
    		long c1, c2;
    		SkipDateTimeEscalator.set(Boolean.TRUE);
    		StatusEnum statusEnum = null;
    		ProcessStepEnum eqrProcessStep = ProcessStepEnum.FINAL_RECORD_VALIDATION;
    		List<EquipmentRemovalResponseDto> errorList = new ArrayList<EquipmentRemovalResponseDto>();
    		List<TechnicalOrderDetail> errorTodList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> successTodList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> IBTodList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> techOrderDetailList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> equipmentsWithActiveContract = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> technicallyRegisteredAssets = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> eqrListForSRCreation = new ArrayList<TechnicalOrderDetail>();
    		Set<String> errors = new HashSet<String>();
    		List<String> assetsToBeProcessed = new ArrayList<String>();
	    	SiteRegistration siteRegistration = null;
	    	int flag = 0;
	    	try {
		    	logger.debug("---------RegistrationId------"+equipmentRemovalDataDTO.getRegistrationId());
				siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(equipmentRemovalDataDTO.getRegistrationId());
				sb.append("Site Registration Object Received...");
				if(siteRegistration != null && !siteRegistration.getFinalValidationStatus().getStatusId().equalsIgnoreCase(GRTConstants.COMPLETED)) {
					sb.append("Inside IF - Site Reg is not null & Install base status incomplete... ");
					if(equipmentRemovalDataDTO.getErrorId()!= null) {
						sb.append("Inside IF - Error ID is not null in base response... ");
						if(equipmentRemovalDataDTO.getErrorId().equals(GRTConstants.EQR_file_successCode)) {
							sb.append("Inside If - Error id equals to 0 which is Success code... ");
							logger.debug("ErrorId: "+equipmentRemovalDataDTO.getErrorId());
							// Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel
							getTechnicalOrderOnRegIdOrderTypeIsSALGateways(equipmentRemovalDataDTO.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV, true);
							sb.append("Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel for Order Type as FV ... ");
							// Fetching technicalOrder details for registration Id
							techOrderDetailList = getTechnicalOrderByType(equipmentRemovalDataDTO.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV);
							sb.append("Fetch Tech Order details for all Reg Ids for Order Type as FV ... ");
							if(equipmentRemovalDataDTO.getEquipmentRemovalResponseDto()!=null
									&& equipmentRemovalDataDTO.getEquipmentRemovalResponseDto().size() > 0) {
								logger.debug("EQR Async Response List:"+equipmentRemovalDataDTO.getEquipmentRemovalResponseDto().size());
								for(EquipmentRemovalResponseDto equipmentRemovalResponseDto: equipmentRemovalDataDTO.getEquipmentRemovalResponseDto()) {
									logger.debug("Error desccription: "+equipmentRemovalResponseDto.getErrorDescription());
									if(StringUtils.isNotEmpty(equipmentRemovalResponseDto.getErrorDescription())
											&& !equipmentRemovalResponseDto.getErrorDescription().equalsIgnoreCase("Success")) {
										//errors.add(equipmentRemovalResponseDto.getMaterialCode() + "-" + equipmentRemovalResponseDto.getErrorDescription());
										sb.append("Create the error list if there is any error desc in payload or no Success ... ");
										errorList.add(equipmentRemovalResponseDto);
									} else { // ********* Success case for the specific equipment
										// *** START - List equipments with Active Contract set for removal.
										sb.append(" Success case for the specific equipment... ");
										sb.append(" START - List equipments with Active Contract set for removal... ");
										sb.append(" For all success case, Rows with action_type = S should be ignored and not added in the success list... ");
										for(TechnicalOrderDetail technicalOrderDetail:techOrderDetailList){
											// START : For all success case, Rows with action_type="S" should be ignored and not added in the success list 
											if(null != technicalOrderDetail.getActionType() && StringUtils.isNotEmpty(technicalOrderDetail.getActionType()) &&
													GRTConstants.ACTION_TYPE_S.equalsIgnoreCase(technicalOrderDetail.getActionType())) {
												continue;
											}
											// END : For all success case, Rows with action_type="S" should be ignored and not added in the success list 
											//Added for Processing the IB records with the EQR
											
											if(null != technicalOrderDetail.getActionType() && StringUtils.isNotEmpty(technicalOrderDetail.getActionType()) &&
													GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(technicalOrderDetail.getActionType())) {
												TechnicalOrderDetail technicalOrderDetailNew = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(technicalOrderDetail);
												technicalOrderDetailNew.setSoldToId(siteRegistration.getSoldToId());
												technicalOrderDetailNew.setBeforeQuantity(technicalOrderDetail.getBeforeQuantity());
												technicalOrderDetailNew.setAfterQuantity(technicalOrderDetail.getAfterQuantity());
												technicalOrderDetailNew.setActionType(GRTConstants.TECH_ORDER_TYPE_FV);
												IBTodList.add(technicalOrderDetailNew);
												sb.append("Update the Tech Order list DTO with Material code, Added quantity & final quantity for action type A ... ");
											}
											// Preparing list of assets to check if IB pipeline have the same quantity set for removal
											if(StringUtils.isNotEmpty(technicalOrderDetail.getSummaryEquipmentNumber()) 
													&& technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(equipmentRemovalResponseDto.getEquipmentNumber())
													&& !technicalOrderDetail.isSalGateway()){
												technicalOrderDetail.setSoldToId(siteRegistration.getSoldToId());
												technicalOrderDetail.setBeforeQuantity(technicalOrderDetail.getBeforeQuantity());
												technicalOrderDetail.setAfterQuantity(technicalOrderDetail.getAfterQuantity());
												flag = 0;
												if(successTodList.size() > 0){
													for(TechnicalOrderDetail successTOD:successTodList){
														if (successTOD.getMaterialCode() != null
																&& successTOD.getMaterialCode().equalsIgnoreCase(technicalOrderDetail.getMaterialCode())
																&& (successTOD.getSummaryEquipmentNumber() != null 
																			&& successTOD.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber()))) {
															successTOD.setRemovedQuantity(successTOD.getRemovedQuantity()+technicalOrderDetail.getRemovedQuantity());
															logger.debug("Update successTodList Dto:  MC:"+technicalOrderDetail.getMaterialCode()+"   Added Qty:"+technicalOrderDetail.getRemovedQuantity()+"    Final Qty:"+successTOD.getRemovedQuantity());
															sb.append("Update the successTodList DTO with Material code, Added quantity & final quantity... ");
															flag = 1;
														}
													}
												}
												if(flag == 0){
													logger.debug("successTodList Dto:  MC:"+technicalOrderDetail.getMaterialCode()+"    Quantity:"+technicalOrderDetail.getRemovedQuantity());
													TechnicalOrderDetail technicalOrderDetailNew = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(technicalOrderDetail);
													//technicalOrderDetailNew.setActionType(GRTConstants.TECH_ORDER_TYPE_FV);
													successTodList.add(technicalOrderDetailNew);
												}
												if(StringUtils.isNotEmpty(technicalOrderDetail.getActiveContractExist())
														&& technicalOrderDetail.getActiveContractExist().equalsIgnoreCase(GRTConstants.YES)
														&& equipmentRemovalResponseDto.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
													sb.append("Add equipments with active contract into the list... ");
													equipmentsWithActiveContract.add(technicalOrderDetail);
												}
												if(StringUtils.isNotEmpty(technicalOrderDetail.getTechnicallyRegisterable())
														&& technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
														&& equipmentRemovalResponseDto.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
													technicallyRegisteredAssets.add(technicalOrderDetail);
													logger.debug("Technical Registered Asset removed with GRTID# "+equipmentRemovalDataDTO.getRegistrationId()
															+", material code/SE code as "+technicalOrderDetail.getMaterialCode()+"/"+technicalOrderDetail.getSolutionElementCode()
															+", data/time: "+new Date());
												}
											}
										}
										sb.append("END - List equipments with Active Contract set for removal... ");
										// *** END - List equipments with Active Contract set for removal.
									}
								}
							}
							// START ** SR Creation for the equipments with Active Contract on successful removal
							if(equipmentsWithActiveContract != null && equipmentsWithActiveContract.size() > 0){
								logger.debug("Create Siebel SR for Equipments with Active Contract");
								sb.append(" SR Creation for the equipments with Active Contract on successful removal... ");
					    		srCreationForEquipmentsWithActiveContract(siteRegistration, equipmentsWithActiveContract, GRTConstants.TECH_ORDER_TYPE_FV);
							}
							// END ** SR Creation for the equipments with Active Contract on successful removal

							//START ** Deactivating TRed assets which are successfully removed
							if(technicallyRegisteredAssets != null && technicallyRegisteredAssets.size() > 0){
								sb.append("Inside If - Deactivating TRed assets which are successfully removed... ");
								logger.debug("Create Siebel SR for Equipments with Active Contract");
								sb.append("Create Siebel SR for Equipments with Active Contract... ");
								List<String> offboardSeids = new ArrayList<String>();
								try {
									c1 = Calendar.getInstance().getTimeInMillis();
									List<TechnicalOrderDetail> technicalOrderDetailListForOffboarding = getSiebelClient().deactivatingTRAssetsAfterRemovalSuccess
											(prepareAssetsTobeRemovedFromSiebel(technicallyRegisteredAssets));
						    		c2 = Calendar.getInstance().getTimeInMillis();
						    		logger.debug("TIMER for TR record removals Success Case of EQR for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :"+ (c2-c1));
						    		//OffBoard Technically Registered Assets
					    			logger.debug("OffBoard Technicall Registered Equipments:");
					    			if(technicalOrderDetailListForOffboarding != null && technicalOrderDetailListForOffboarding.size() > 0) {
					    				sb.append("Inside If - OffBoarding Technicall Registered Equipments... ");
						    			for (TechnicalOrderDetail tod : technicalOrderDetailListForOffboarding) {
						    				if(StringUtils.isNotEmpty(tod.getSolutionElementId())) {
						    					offboardSeids.add(tod.getSolutionElementId());
						    				}
						    				assetsToBeProcessed.add(tod.getMaterialCode());
										}
					    			}
					    			long c3 = Calendar.getInstance().getTimeInMillis();
					    			getGenericClient().offboardDevices(siteRegistration.getRegistrationId(), offboardSeids);
					    			long c4  = Calendar.getInstance().getTimeInMillis();
					    			logger.debug("TIMER for OffBoarding Technically Registered Equipments for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds:"+ (c4-c3));
					    		} catch (DataAccessException dae) {
					    			sb.append("Unable to Process data - Data Access exception... ");
									logger.error("DataAccessException:"+dae.getMessage());
									throw new DataAccessException(dae.getMessage());
								} catch (Exception e) {
									sb.append("Unable to Process payload due to exception... ");
									logger.error("Exception:"+e.getMessage());
									throw new DataAccessException(e.getMessage());
					    		} catch (Throwable throwable) {
					    			logger.error("Error while offboarding devices.", throwable);
					    		}
							}
							// END ** Deactivating TRed assets which are successfully removed
							
							// START ** Processing the IB Records in Asynch Response
							if(IBTodList.size() > 0){
								sb.append("Processing the IB Records in Asynch Response if IB TEchOrder list size is more than 0... ");
								//Implemented this method in TechnicalRegistrationDao
								Map<String,List<String>> regIdMap = getTechnicalRegistrationDao().processPipelineBasedonEQRAndIBQuantity(IBTodList);
				
								if(regIdMap.size() > 0){
									sb.append("Processing the IB Registeration for EQR if Registeration ID Map size is more than 0... ");
									processIBRegistrationsForEQR(regIdMap);
									for(Entry<String, List<String>> entry : regIdMap.entrySet()){
										List<String> mcList = entry.getValue();
										assetsToBeProcessed.addAll(mcList);
									} 
								}
								
							}
							// END ** Processing the IB Records in Asynch Response
							
							if(successTodList.size() > 0){
								//Implemented this method in TechnicalRegistrationDao
								sb.append("processing pipeline based on EQR and IB quantity if successlist size is greater than 0... ");
								Map<String,List<String>> regIdMap = getTechnicalRegistrationDao().processPipelineBasedonEQRAndIBQuantity(successTodList);
				
								if(regIdMap.size() > 0){
									sb.append("Processing the IB Registeration for EQR if Registeration ID Map size is more than 0... ");
									processIBRegistrations(regIdMap);
									for(Entry<String, List<String>> entry : regIdMap.entrySet()){
										List<String> mcList = entry.getValue();
										assetsToBeProcessed.addAll(mcList);
									}
								}
								
							}
							// Updating EQR sub status to SAP Completion
							//Implemented this method in TechnicalRegistrationDao
							sb.append("Updating Site Registration sub status to SAP Completion when Process step is Final Record Validation for EQR... ");
							getTechnicalRegistrationDao().updateSiteRegistrationSubStatus(siteRegistration, StatusEnum.SAPCOMPLETED, ProcessStepEnum.FINAL_RECORD_VALIDATION);
							// Update PIPELINE_SAP_TRANSACTIONS with IS_SAP_COMPLETED to true
							//Implemented this method in TechnicalRegistrationDao
							sb.append("Updating SAP pipeline transactions with SAP completed flag when Tech Order Type is FV for EQR... ");
							getTechnicalRegistrationDao().updatePipeLineSAPTransactionsSAPCompletedFlag(siteRegistration.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV);
							// ProcessRegistration
				    		if(assetsToBeProcessed.size() > 0){
				    			sb.append("Process the registration if there is any asset to proceed... ");
				    			processRegistration(siteRegistration, equipmentRemovalDataDTO.getRegistrationId(), assetsToBeProcessed, eqrProcessStep, false);
				    		}
						} else if(equipmentRemovalDataDTO.getErrorId().equals(GRTConstants.EQR_baddata_errorCode)){
							sb.append("Incomplete data or bad data in file send by SAP... ");
			    			logger.error("Incomplete data or bad data in file send by SAP: "+equipmentRemovalDataDTO.getEquipmentRemovalResponseDto().get(0).getDescription());
				    	} else{
				    		sb.append("Error while processing the EQR... ");
				    		logger.error("Error while processing the EQR:  "+equipmentRemovalDataDTO.getEquipmentRemovalResponseDto().get(0).getDescription());
				    	}
					}
				} else {
					sb.append("Site Registration is coming null (OR) Equipment removal status is completed before asynchronous response come from SAP...");
					logger.debug("Site Registration is coming null (OR) Equipment removal status is completed before asynchronous response come from SAP.");
				}
	    	} catch (DataAccessException e) {
				logger.debug("Error while getting the Site Registration for the registrationId---"+equipmentRemovalDataDTO.getRegistrationId());
				throw new DataAccessException(e.getMessage());
			}
	    	logger.debug("++++++EQR Error list size+++++++++ :"+errorList.size());
    		List<String> deactivationEQNList = new ArrayList<String>();
    		List<TechnicalOrderDetail> erroredTechnicallyRegisteredAssets = new ArrayList<TechnicalOrderDetail>();
	    	if (errorList.size() > 0) {
	    		sb.append("Inside if - when error list size is more than 0...");
	    		// Update warnings/errors for the equipments
	    		logger.debug("Update warnings/errors for the equipments *** STARTS");
	    		logger.debug("techOrderDetailList size:"+techOrderDetailList.size());
	    		sb.append("getting inside For loop to process the Technical User details should be more than 0...");
	    		for(TechnicalOrderDetail technicalOrderDetail : techOrderDetailList){
	    			for(EquipmentRemovalResponseDto equipmentRemovalResponseDto: errorList){
	    				if(TechnicalRegistrationUtil.truncateLeadingZeros(equipmentRemovalResponseDto.getEquipmentNumber()).equalsIgnoreCase
	    						(TechnicalRegistrationUtil.truncateLeadingZeros(technicalOrderDetail.getSummaryEquipmentNumber()))){
	    					// Set Error description to update in Technical Order table when Error Description is not from 1 to 5 types
	    					technicalOrderDetail.setErrorDescription(equipmentRemovalResponseDto.getErrorDescription());
	    					if(TechnicalRegistrationUtil.filterErrorDescription(equipmentRemovalResponseDto.getErrorDescription()) != null){
	    						errors.add(technicalOrderDetail.getMaterialCode() + "-" + equipmentRemovalResponseDto.getErrorDescription());
	    					}
	    					// technicalOrderDetail.setSr_created("yes");
	    					// errors.add(technicalOrderDetail.getMaterialCode() + "-" + equipmentRemovalResponseDto.getErrorDescription());
	    					errorTodList.add(technicalOrderDetail);
	    					if(equipmentRemovalResponseDto.getInitialQuantity() == null)
	    						equipmentRemovalResponseDto.setInitialQuantity(new BigInteger(technicalOrderDetail.getInitialQuantity()!=null?technicalOrderDetail.getInitialQuantity().toString():"0"));
	    					logger.debug("EQR Dto QTY: "+equipmentRemovalResponseDto.getInitialQuantity()+"   TOD Dto QTY:"+technicalOrderDetail.getInitialQuantity());
	    					// Assign few properties to send as an attachemnt to the SR
	    					equipmentRemovalResponseDto.setMaterialCode(technicalOrderDetail.getMaterialCode());
	    					logger.debug("EQR Dto MC: "+equipmentRemovalResponseDto.getMaterialCode()+"   TOD Dto MC:"+technicalOrderDetail.getMaterialCode());
	    					equipmentRemovalResponseDto.setMaterialCodeDescription(technicalOrderDetail.getDescription());
	    					logger.debug("EQR Dto MCD: "+equipmentRemovalResponseDto.getMaterialCodeDescription()+"   TOD Dto MCD:"+technicalOrderDetail.getDescription());
	    					equipmentRemovalResponseDto.setSeid(technicalOrderDetail.getSolutionElementId());
	    					logger.debug("EQR Dto SEID: "+equipmentRemovalResponseDto.getSeid()+"   TOD Dto SEID:"+technicalOrderDetail.getSolutionElementId());
	    					equipmentRemovalResponseDto.setIsActiveContract(technicalOrderDetail.getActiveContractExist());
	    					logger.debug("EQR Dto IAC: "+equipmentRemovalResponseDto.getIsActiveContract()+"   TOD Dto IAC:"+technicalOrderDetail.getActiveContractExist());
	    				}
	    			}
	    		}
	    		logger.debug("Update warnings/errors for the equipments *** ENDS");
	    		logger.debug("Create Siebel SR");
	    		// ***** SR - creation logic starts for the equipments with Error Description
	    		c1 = Calendar.getInstance().getTimeInMillis();
	    		sb.append("Create Siebel SR - creation logic starts for the equipments with Error Description...");
	    		for(TechnicalOrderDetail technicalOrderDetail:errorTodList){
	    			/** Restrict SR Creartion when error description is 1,2,3,5,9 in the following
	    			 *  1. Equipment is not installed at any location
	    			 *  2. Invalid Equipment nr
	    			 *  3. Equipment is installed at a different FL XXXXXX
	    			 *  4. Material doesn't exists in SAP
	    			 *  5. Functional location doesn't exists in SAP
	    			 *  ...
	    			 *  9. SAP Equip No XXXX is not valid for Material Code YYYY
	    			 *  For other categories it just creates SR and deactivation/updation is Siebel wont be done
	    			 */
	    			if(technicalOrderDetail.getErrorDescription() != null
	    					&& !(technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Equipment is not installed at any location")
	    					|| technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Functional location doesn't exists in SAP")
	    					|| technicalOrderDetail.getErrorDescription().contains("Equipment is installed at a different FL")
	    					|| technicalOrderDetail.getErrorDescription().contains("is not valid for Material Code")
	    					|| technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Invalid Equipment nr"))){
	    				technicalOrderDetail.setSr_created("yes");
	    				eqrListForSRCreation.add(technicalOrderDetail);
	    			}
	    		}
	    		if(errorTodList.size() > 0){
	    			sb.append("Update Error Desc for Tech Order which is inside this list...");
	    			//Implemented this method in TechnicalRegistrationDao
	    			getTechnicalRegistrationDao().updateTechnicalOrderErrorDescription(errorTodList);
	    		}
	    		// Updating the technicalOrders with Error Description - 1,3,5
	    		/*if(updateTechOrderList.size() > 0){
	    			getRegistrationDao().updateTechnicalOrderErrorDescription(updateTechOrderList);
	    		}*/

	    		if(eqrListForSRCreation != null && eqrListForSRCreation.size() > 0){
		    		SRRequestDto srRequestDto = null;
		    		sb.append("Inside If - Creating SR for complete list...");
		    		//Generating file path
		    		String filePath = autoGenAttachmentPath(EQR_ATTACHMENT_NAME);
		    		// Generating worksheet
		    		TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
		    		List<Attachment> attList = new ArrayList<Attachment>();
		    		//Implemented this method in TechnicalOrderDetailWorksheetProcessor
		    		Attachment attachment = worksheet.generateFinalValidationErrorWorksheet(eqrListForSRCreation, filePath, siteRegistration, GRTConstants.TECH_ORDER_TYPE_FV);
		    		attList.add(attachment);
		    		logger.debug("Siebel SR Creation : RegId :"+equipmentRemovalDataDTO.getRegistrationId()+"   soldTo :"+siteRegistration.getSoldToId());
		    		SRRequest srRequest = null;
		    		// SiteRegistration siteRegistration = getRegistrationDao().getSiteRegistration(equipmentRemovalResponseDto.getTransactionId());
		    		//Implemented this method in TechnicalRegistrationDao
		    		
		    		try {		
		    			sb.append("Initalize SR request...");
		    			srRequest = getTechnicalRegistrationDao().initSRRequest(siteRegistration, eqrProcessStep);
		    		} catch (DataAccessException e) {
		    			logger.error("----Unable to create an SR Request Id on table----", e);
		    			throw e;
		    		}
		    		
		    		if (srRequest != null) {
		    			sb.append("Processing the SR request if its not null...");
		    			srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
		    			this.processSRRequest(equipmentRemovalDataDTO.getRegistrationId(), siteRegistration.getSoldToId(), attList, srRequestDto, eqrProcessStep);
		    		}
		    		logger.debug("Before updating error description and SR_Created flag");
		    		if(errorTodList.size() > 0){
		    			/*for(TechnicalOrderDetail technicalOrderDetail:errorTodList){
		    				if(filterErrorDescription(technicalOrderDetail.getErrorDescription()) == null){
		    					technicalOrderDetail.setErrorDescription("");
		    				}
		    			}*/
		    			
		    			getTechnicalRegistrationDao().updateTechnicalOrderErrorDescription(errorTodList);
		    		}
		    		logger.debug("Setting IsSrCompleted to no on SiteRegistration");
		    	
		    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, GRTConstants.NO);

		    		c2 = Calendar.getInstance().getTimeInMillis();
		    		logger.debug("TIMER for SR creation for Errored Equipments of EQR for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :"+ (c2-c1));
	    		}
	    		// ***** SR - creation logic Ends

	    		// Fetching the records from GRT DB with the equipments numbers from response
	    		String eqnListStr = "";
	    		//assetsToBeProcessed = new ArrayList<String>(); --> related code is commnted as Error Desc#9 now joined with desc#6,7,8
	    		for(EquipmentRemovalResponseDto equipmentRemovalResponseDto : errorList){
	    			/** Deactivate assets from Siebel when error description is one among the following
	    			 *  1. Equipment is not installed at any location
	    			 *  2. Invalid Equipment nr
	    			 *  3. Equipment is installed at a different FL XXXXXX
	    			 *  4. Material doesn't exists in SAP
	    			 *  5. Functional location doesn't exists in SAP
	    			 *  #. 6,7,8 and 9 also comes under this case now
	    			 *  For other categories it just creates SR and deactivation/updation is Siebel wont be done
	    			 */
	    			if(equipmentRemovalResponseDto.getErrorDescription() != null ){
	    				if(!deactivationEQNList.contains(equipmentRemovalResponseDto.getEquipmentNumber())){
	    					sb.append("Deactivate assets from Siebel when error description is there...");
    						deactivationEQNList.add(equipmentRemovalResponseDto.getEquipmentNumber());
    						eqnListStr += "'"+equipmentRemovalResponseDto.getEquipmentNumber()+"',";
	    				}
	    			}
	    		}
	    		// ProcessRegistration for Error Description No.9(with not valid for Material Code)
	    		/*if(assetsToBeProcessed.size() > 0){
	    			getSiebelService().processRegistration(siteRegistration, assetsToBeProcessed, eqrProcessStep, false);
				}*/
    			logger.error("Equipment List Size: "+errorList.size());
    			
    			//GRT 4.0 change
    			List<TechnicalOrder> technicalOrderList = getTechnicalRegistrationDao().getTechnicalOrderListForEuipmentNumberList(equipmentRemovalDataDTO.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_EQR, deactivationEQNList);
    			sb.append("Technical Order list fetched for all the deactivate equipment comes in EQR... ");
	    		List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
	    		TechnicalOrderDetail technicalOrderDetail= null;
	    		for(TechnicalOrder technicalOrder:technicalOrderList){
	    			for(String summaryEQN : deactivationEQNList){
		    			if(TechnicalRegistrationUtil.truncateLeadingZeros(technicalOrder.getSummaryEquipmentNumber()).equalsIgnoreCase(
		    					TechnicalRegistrationUtil.truncateLeadingZeros(summaryEQN))){
			    			technicalOrderDetail = new TechnicalOrderDetail();
			    			technicalOrderDetail.setMaterialCode(technicalOrder.getMaterialCode());
			    			technicalOrderDetail.setSoldToId(siteRegistration.getSoldToId());
			    			technicalOrderDetail.setAssetPK(technicalOrder.getAssetPK());
			    			technicalOrderDetail.setRemainingQuantity(technicalOrder.getRemainingQuantity());
			    			technicalOrderDetail.setEquipmentNumber(technicalOrder.getEquipmentNumber());
			    			technicalOrderDetail.setSummaryEquipmentNumber(technicalOrder.getSummaryEquipmentNumber());
			    			technicalOrderDetail.setActionType(GRTConstants.TECH_ORDER_TYPE_FV);
			    			logger.debug("ERRORED ASSET ---> SID:"+technicalOrderDetail.getSid()+"     MID:"+technicalOrderDetail.getMid());
			    			if(StringUtils.isNotEmpty(technicalOrder.getSid()) && StringUtils.isNotEmpty(technicalOrder.getMid())){
			    				technicalOrderDetail.setSid(technicalOrder.getSid());
			    				technicalOrderDetail.setMid(technicalOrder.getMid());
			    				erroredTechnicallyRegisteredAssets.add(technicalOrderDetail);
			    			} else {
			    				todList.add(technicalOrderDetail);
			    			}
		    			}
		    		}
	    		}
	    		// Deactivating Assets with SID and MID
	    		List<TechnicalOrderDetail> successfulDeactivationList = new ArrayList<TechnicalOrderDetail>();
	    		if(erroredTechnicallyRegisteredAssets != null && erroredTechnicallyRegisteredAssets.size() > 0){
	    			logger.debug("START - Deactivation of errored out assets with SID and MID");
	    			successfulDeactivationList = getSiebelClient().deactivatingTRAssetsAfterRemovalSuccess(prepareAssetsTobeRemovedFromSiebel(erroredTechnicallyRegisteredAssets));
	    			logger.debug("END - Deactivation of errored out assets with SID and MID");
	    		}
	    		// Fetch Equipments from Siebel to deactivate
	    		/*if(eqnListStr.length() > 0){
	    			List<TechnicalOrderDetail> technicalOrderDetailList = getSiebelDao().fetchEquipmentRemovalRecordsOnEQN(siteRegistration.getSoldToId(), GRTPropertiesUtil.getProperty(GRTConstants.VCODES_STR), eqnListStr.substring(0,eqnListStr.length()-1));
	    			logger.debug("TOD List size:"+(technicalOrderDetailList!=null?technicalOrderDetailList.size():0));
	    		}*/
	    		// De-activate equipments which are errored out
	    		if(todList.size() > 0){
	    			logger.debug("Deactivating errored out assets directly from siebel");
					c1 = Calendar.getInstance().getTimeInMillis();
					List<List<TechnicalOrderDetail>> combinedList = getSiebelClient().deActivateSolutionElements(todList);
					if(combinedList != null){ 
						List<TechnicalOrderDetail> successList = combinedList.get(0);
						if(successList != null && successList.size() > 0){
							successfulDeactivationList.addAll(successList);
						}
						List<TechnicalOrderDetail> failureList = combinedList.get(1);
						if(failureList != null && failureList.size() > 0){
							// Process pipeline records
							//Implemented this method in TechnicalRegistrationDao
							Map<String,List<String>> regIdMap = getTechnicalRegistrationDao().updatePipeLineSAPTransactions(failureList);
							sb.append("Update all SAP transactions in Pipeline for deactivate equipments...");
							this.processIBRegistrations(regIdMap);
							sb.append("Process the IB registerations for all deactivate equipments...");
							successfulDeactivationList.addAll(failureList);
						}
					}
					
		    		c2 = Calendar.getInstance().getTimeInMillis();
	    			logger.debug("TIMER for deactivating errored out assets directly from siebel - EQR for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :"+ (c2-c1));
	    		}

	    		if(successfulDeactivationList.size() > 0){
	    			assetsToBeProcessed = new ArrayList<String>();
		    		for(TechnicalOrderDetail techOrderDetail: successfulDeactivationList){
		    			assetsToBeProcessed.add(techOrderDetail.getMaterialCode());
		    		}
		    		// ProcessRegistration after Assets Successully deactivated/updated in Siebel
	    			processRegistration(siteRegistration, equipmentRemovalDataDTO.getRegistrationId(), assetsToBeProcessed, eqrProcessStep, false);
	    			sb.append("Process the Asset registrations for all the assets to be processed...");
	    		}
	    	}
	    	// Fetching SiteRegistration again to check for the status
	    	sb.append("Fetching SiteRegistration again to check for the status...");
	    	siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(equipmentRemovalDataDTO.getRegistrationId());
	    	// Updating registration status to Awaiting Info
	    	if(eqrListForSRCreation.size() > 0 && !StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(siteRegistration.getFinalValidationStatus().getStatusId())){
				statusEnum = StatusEnum.AWAITINGINFO;
				//Implemented this method in TechnicalRegistrationDao
				sb.append("Updating registration status to Awaiting Info for EQR list used in SR creation...");
				getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, statusEnum, eqrProcessStep, false);
    			sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), eqrProcessStep, statusEnum, errors);
	    	}
	    	
	    	// Check for AssetUpdate
	    	/*try {
				long hours = TechnicalRegistrationUtil.calculateNoOfHours(siteRegistration.getEqrSubmittedDate());
				if(hours > Long.parseLong(getIbIntiatedBeforeHours())){
					processSiebelUpdateResponse(siteRegistration.getRegistrationId());
				}
			} catch (Exception e) {
				sb.append("Exception while processing Asset Update....");
				logger.debug("Exception while processing Asset Update. Exception:"+e.getMessage());
			}*/
    	} catch (DataAccessException e) {
			logger.debug("Error while processing the registration:"+equipmentRemovalDataDTO.getRegistrationId());
			sb.append("Error while processing the registration...");
			throw new DataAccessException(e.getMessage());
		} finally {
    		SkipDateTimeEscalator.unset();
    	}
    	logger.debug("Exiting TechnicalRegistrationService.processReceivedEquipmentRemovalReq");
    	equipmentRemovalDataDTO.setRoutingInfo(sb.toString());
		return equipmentRemovalDataDTO;
    }
    
    
    /*
     * [AVAYA] GRT 4.0  Process Equipment Move response
     *
     * handling of GRT.EquipmentMove.Response.Queue
     *
     */
    public EquipmentMoveResponse processReceivedEquipmentMoveReq(EquipmentMoveDataDTO equipmentMoveDataDTO) throws Exception {
    	logger.debug("Entering TechnicalRegistrationService.processReceivedEquipmentRemovalReq");
    	StringBuffer sb = new StringBuffer();
		sb.append(equipmentMoveDataDTO.getRoutingInfo().toString());
		sb.append("Entering into TechnicalRegistrationService.processReceivedEquipmentRemovalReq Method to process the EQM payload... ");
    	//Initialize the response object
    	EquipmentMoveResponse equipmentMoveResponse = new EquipmentMoveResponse(); 
    	equipmentMoveResponse.setErrorDesc("Request Processed Successfully");
    	equipmentMoveResponse.setReturnCode(GRTConstants.WS_SUCCESS_CODE);
    	try {
    		long c1, c2;
    		SkipDateTimeEscalator.set(Boolean.TRUE);
    		StatusEnum statusEnum = null;
    		ProcessStepEnum eqmProcessStep = ProcessStepEnum.EQUIPMENT_MOVE;
    		List<EquipmentMoveResponseDto> errorList = new ArrayList<EquipmentMoveResponseDto>();
    		List<TechnicalOrderDetail> errorTodList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> successTodList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> techOrderDetailList = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> equipmentsWithActiveContract = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> technicallyRegisteredAssets = new ArrayList<TechnicalOrderDetail>();
    		List<TechnicalOrderDetail> eqmListForSRCreation = new ArrayList<TechnicalOrderDetail>();
    		Set<String> errors = new HashSet<String>();
    		List<String> assetsToBeProcessed = new ArrayList<String>();
	    	SiteRegistration siteRegistration = null;
	    	int flag = 0;
	    	try {
		    	logger.debug("---------RegistrationId------"+equipmentMoveDataDTO.getTransactionId());
				siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(equipmentMoveDataDTO.getTransactionId());
				sb.append("Site Registration Object Received for Transaction ID - "+equipmentMoveDataDTO.getTransactionId()+"... ");
				if(siteRegistration != null && !siteRegistration.getEqrMoveStatus().getStatusId().equalsIgnoreCase(GRTConstants.COMPLETED)) {
					sb.append("Inside first if block, Processing ahead as its Install base status is incomplete... ");
					if(equipmentMoveDataDTO.getReturnCode()!= null) {
						sb.append("Inside second If when Return Code is not null in response... ");
						if(equipmentMoveDataDTO.getReturnCode().equals(GRTConstants.EQR_file_successCode)) {
							logger.debug("ErrorId: "+equipmentMoveDataDTO.getReturnCode());
							sb.append("Inside third if Processing ahead when Return Code is coming as 0 which means Success code... ");
							// Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel
							sb.append("Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel for Order Type as EM ... ");
							//Commenting below line as de-activation is not required for equipment move
							getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM(equipmentMoveDataDTO.getTransactionId(), GRTConstants.TECH_ORDER_TYPE_EM, true,siteRegistration.getSoldToId(),siteRegistration.getToSoldToId());
							
							// Fetching technicalOrder details for registration Id
							sb.append("Fetch Tech Order details for all Reg Ids for Order Type as EM ... ");
							techOrderDetailList = getTechnicalOrderByType(equipmentMoveDataDTO.getTransactionId(), GRTConstants.TECH_ORDER_TYPE_EM);
							if(equipmentMoveDataDTO.getEquipmentMoveResponseData()!=null
									&& equipmentMoveDataDTO.getEquipmentMoveResponseData().size() > 0) {
								logger.debug("EQM Async Response List:"+equipmentMoveDataDTO.getEquipmentMoveResponseData().size());
								sb.append("EQM Async Response List Size... "+equipmentMoveDataDTO.getEquipmentMoveResponseData().size());
								
								for(EquipmentMoveResponseDto equipmentMoveResponseDto: equipmentMoveDataDTO.getEquipmentMoveResponseData()) {
									logger.debug("Error desccription: "+equipmentMoveResponseDto.getErrorDesc());
									if(StringUtils.isNotEmpty(equipmentMoveResponseDto.getErrorDesc())
											&& !equipmentMoveResponseDto.getErrorDesc().equalsIgnoreCase("Success")) {
										//errors.add(equipmentRemovalResponseDto.getMaterialCode() + "-" + equipmentRemovalResponseDto.getErrorDescription());
										sb.append("Create the error list if there is any error desc in payload or no success ... ");
										errorList.add(equipmentMoveResponseDto);
									} else { // ********* Success case for the specific equipment
										// *** START - List equipments with Active Contract set for removal.
										sb.append("Handle success case... ");
										sb.append(" START - List equipments with Active Contract set for removal... ");
										sb.append(" Preparing list of assets to check if IB pipeline have the same quantity set for removal - Entering in FOR loop... ");
										for(TechnicalOrderDetail technicalOrderDetail:techOrderDetailList){
											// Preparing list of assets to check if IB pipeline have the same quantity set for removal
											if(StringUtils.isNotEmpty(technicalOrderDetail.getSummaryEquipmentNumber()) 
													&& technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(equipmentMoveResponseDto.getFromEquipmentNumber())){
												technicalOrderDetail.setSoldToId(siteRegistration.getSoldToId());
												technicalOrderDetail.setBeforeQuantity(technicalOrderDetail.getBeforeQuantity());
												technicalOrderDetail.setAfterQuantity(technicalOrderDetail.getAfterQuantity());
												technicalOrderDetail.setToEquipmentNumber(equipmentMoveResponseDto.getToEquipmentNumber());
												//GRT 4.0 Change : Add TO_Sold_To
												technicalOrderDetail.setToSoldToId(siteRegistration.getToSoldToId());
												
												flag = 0;
												if(successTodList.size() > 0){
													for(TechnicalOrderDetail successTOD:successTodList){
														if (successTOD.getMaterialCode() != null
																&& successTOD.getMaterialCode().equalsIgnoreCase(technicalOrderDetail.getMaterialCode())
																&& (successTOD.getSummaryEquipmentNumber() != null 
																			&& successTOD.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber()))) {
															if( successTOD.getRemovedQuantity()!=null && technicalOrderDetail.getRemovedQuantity()!=null ){
																successTOD.setRemovedQuantity(successTOD.getRemovedQuantity()+technicalOrderDetail.getRemovedQuantity());
															}
															logger.debug("Update successTodList Dto:  MC:"+technicalOrderDetail.getMaterialCode()+"   Added Qty:"+technicalOrderDetail.getRemovedQuantity()+"    Final Qty:"+successTOD.getRemovedQuantity());
															sb.append("Update the Tech Order list DTO with Material code, Added quantity & final quantity... ");
															flag = 1;
														}
													}
												}
												if(flag == 0){
													logger.debug("successTodList Dto:  MC:"+technicalOrderDetail.getMaterialCode()+"    Quantity:"+technicalOrderDetail.getRemovedQuantity());
													TechnicalOrderDetail technicalOrderDetailNew = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(technicalOrderDetail);
													technicalOrderDetailNew.setActionType(GRTConstants.TECH_ORDER_TYPE_EM);
													successTodList.add(technicalOrderDetailNew);
												}
												if(StringUtils.isNotEmpty(technicalOrderDetail.getActiveContractExist())
														&& technicalOrderDetail.getActiveContractExist().equalsIgnoreCase(GRTConstants.YES)
														&& equipmentMoveResponseDto.getFromEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
													equipmentsWithActiveContract.add(technicalOrderDetail);
												}
												if(StringUtils.isNotEmpty(technicalOrderDetail.getTechnicallyRegisterable())
														&& technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
														&& equipmentMoveResponseDto.getFromEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
													technicallyRegisteredAssets.add(technicalOrderDetail);
													sb.append("Technical Registered Asset removed with GRTID# "+equipmentMoveDataDTO.getTransactionId()
															+", material code/SE code as "+technicalOrderDetail.getMaterialCode()+"/"+technicalOrderDetail.getSolutionElementCode()
															+", data/time: "+new Date());
													logger.debug("Technical Registered Asset removed with GRTID# "+equipmentMoveDataDTO.getTransactionId()
															+", material code/SE code as "+technicalOrderDetail.getMaterialCode()+"/"+technicalOrderDetail.getSolutionElementCode()
															+", data/time: "+new Date());
												}
											}
										}
										// *** END - List equipments with Active Contract set for removal.
									}
								}
							}
							//START ** Moving TRed assets using new siebel asset update service which are successfully moved
							//TODO : Below code has to be updated for siebel asset update and EPN survey
							if(technicallyRegisteredAssets != null && technicallyRegisteredAssets.size() > 0) {
								logger.debug("Create Siebel SR for Equipments with Active Contract");
								sb.append("Create Siebel SR for Equipments with Active Contract... ");
								List<TechnicalOrderDetail> technicalOrderDetailListForEQM = null;
								try {
									c1 = Calendar.getInstance().getTimeInMillis();
									
									//GRT 4.0 : Update the technically registrable assets in the siebel
									//technicalOrderDetailListForEQM = getSiebelClient().eqmSiebelAssetUpdate(prepareAssetsTobeRemovedFromSiebel(technicallyRegisteredAssets), sb);
									technicalOrderDetailListForEQM = getSiebelClient().eqmSiebelAssetUpdate(prepareAssetsTobeMovedFromSiebel(technicallyRegisteredAssets), sb);
						    		c2 = Calendar.getInstance().getTimeInMillis();
						    		logger.debug("TIMER for TR record move Success Case of EQM for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :"+ (c2-c1));
					    			if(technicalOrderDetailListForEQM != null && technicalOrderDetailListForEQM.size() > 0) {
					    				sb.append("Adding assets to be processed...");
						    			for (TechnicalOrderDetail tod : technicalOrderDetailListForEQM) {
						    				assetsToBeProcessed.add(tod.getMaterialCode());
										}
					    			}
					    		} catch (DataAccessException dae) {
									logger.error("DataAccessException:"+dae.getMessage());
									equipmentMoveResponse.setErrorDesc("Internal Server Error - "+dae.getMessage());
									equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
									//throw new DataAccessException(dae.getMessage());
								} catch (Exception e) {
									logger.error("Exception:"+e.getMessage());
									equipmentMoveResponse.setErrorDesc("Internal Server Error - "+e.getMessage());
									equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
									//throw new DataAccessException(e.getMessage());
					    		} catch (Throwable throwable) {
					    			logger.error("Error while moving TRed assets. ", throwable);
					    			equipmentMoveResponse.setErrorDesc("Internal Server Error - "+throwable.getMessage());
									equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
					    		}
								
								//GRT 4.0 : Handling of CM assets: EPN survey
								if( technicalOrderDetailListForEQM!=null && !technicalOrderDetailListForEQM.isEmpty() ){
									sb.append("Handling of CM assets: EPN survey... ");
					    			//List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
									for( TechnicalOrderDetail techOrderD : technicalOrderDetailListForEQM ){
										if( !StringUtils.isEmpty(techOrderD.getSolutionElementId()) &&  !StringUtils.isEmpty(techOrderD.getMaterialCode())
												&& !StringUtils.isEmpty(siteRegistration.getSoldToId()) ){
											TechnicalRegistration tr  = getBaseHibernateDao().getExistingTechReg(techOrderD.getSolutionElementId(),
														siteRegistration.getSoldToId(), techOrderD.getMaterialCode());
											String seCodeSeId =  null;
												if( !StringUtils.isEmpty(tr.getSolutionElement()) ){
													seCodeSeId = tr.getSolutionElement()+":"+tr.getSolutionElementId();
												}
											
											sb.append("Inside EPN servey if case..");
											//Add To Sold To(New Sold To)
											tr.setSoldToId(siteRegistration.getToSoldToId());
											//tr.setTechnicalOrder(techOrderD);
											if( tr!=null && tr.getTechnicalRegistrationId()!=null ){
												//Check whether the product is CM main or not
												//if( tr.isCmProduct() && tr.isCmMain() && !GRTConstants.ACCESS_TYPE_SAL.equals(tr.getAccessType())) {
												if(tr.isCmProduct() && !tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_SAL)
							    						&& (tr.isCmMain() || (!tr.isCmMain() && tr.getRemoteDeviceType().equalsIgnoreCase(GRTConstants.REMOTE_DEVICE_TYPE_LSP)))) {
													//Set the equipment move flag to true
													tr.setEquipmentMove(true);
													tr.setToSoldToId(techOrderD.getToSoldToId());
													sb.append("Calling Epn Servey..");
													String returnCode = getArtClient().initiateCMPolling(tr, seCodeSeId);
													sb.append("Return Code for Epn Servey - "+returnCode);
													if (returnCode == GRTConstants.fmw_errorCode || returnCode == GRTConstants.acsbi_errorCode) {
														String errMsg = null;
														if (returnCode == GRTConstants.fmw_errorCode) {
															errMsg = "Failed to connect to FMW for ACSBI Polling";
														} else {
															errMsg = "Failed to connect to ACSBI for Polling";
														}
														String regId = siteRegistration.getRegistrationId();
														sendEmailToSystemAdmin(regId, errMsg, ProcessStepEnum.TECHNICAL_REGISTRATION);
														equipmentMoveResponse.setErrorDesc(errMsg);
												    	equipmentMoveResponse.setReturnCode(GRTConstants.WS_ERROR_CODE);
													}else{
														TechnicalOrder to = new TechnicalOrder();
														to.setOrderId(techOrderD.getOrderId());
														tr.setTechnicalOrder(to);
														if( tr.getTechnicalRegistrationId()!=null ){
															//Save Technical Registration
															getBaseHibernateDao().saveTechnicalRegistration(tr);
															//Update EPN Survey Status
															getTechnicalRegistrationDao().updateTechnicalRegistrationEPNSurveyStatus(tr, StatusEnum.INPROCESS);
														}
													}
												}
												//trs.add(tr);
											}
										}
									} 
								}
								
							}
							// END ** Moving TRed assets using new siebel asset update service which are successfully moved
							
							// START ** SR Creation for the equipments with Active Contract on successful move
							if(equipmentsWithActiveContract != null && equipmentsWithActiveContract.size() > 0){
								sb.append(" SR Creation for the equipments with Active Contract on successful move... ");
								logger.debug("Create Siebel SR for Equipments with Active Contract");
					    		srCreationForEquipmentsWithActiveContractEM(siteRegistration, equipmentsWithActiveContract, GRTConstants.TECH_ORDER_TYPE_EM);
							}
							// END ** SR Creation for the equipments with Active Contract on successful move
							
							//Defect#291 : IB getting processed for Equipment move submit
							/*if(successTodList.size() > 0){			
								sb.append("processing pipeline based on EQR and IB quantity if successlist size is greater than 0... ");
								Map<String,List<String>> regIdMap = getTechnicalRegistrationDao().processPipelineBasedonEQRAndIBQuantity(successTodList);
				
								if(regIdMap.size() > 0){
									processIBRegistrations(regIdMap);
									for(Entry<String, List<String>> entry : regIdMap.entrySet()){
										List<String> mcList = entry.getValue();
										assetsToBeProcessed.addAll(mcList);
									}
								}
								
							}*/
							// Updating EQR sub status to SAP Completion
							//Implemented this method in TechnicalRegistrationDao
							sb.append("Updating Site Registration sub status to SAP Completion for EQM... ");
							getTechnicalRegistrationDao().updateSiteRegistrationSubStatus(siteRegistration, StatusEnum.SAPCOMPLETED, ProcessStepEnum.EQUIPMENT_MOVE);
							// Update PIPELINE_SAP_TRANSACTIONS with IS_SAP_COMPLETED to true
							//Implemented this method in TechnicalRegistrationDao
							sb.append("Updating SAP pipeline transactions with SAP completed flag for EQM... ");
							getTechnicalRegistrationDao().updatePipeLineSAPTransactionsSAPCompletedFlag(siteRegistration.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_EM);
							// ProcessRegistration
				    		if(assetsToBeProcessed.size() > 0){
				    			sb.append("Process the registeration if there is any asset to proceed... ");
				    			processRegistration(siteRegistration, equipmentMoveDataDTO.getTransactionId(), assetsToBeProcessed, eqmProcessStep, false);
				    		}
						} else if(equipmentMoveDataDTO.getReturnCode().equals(GRTConstants.EQR_baddata_errorCode)){
			    			logger.error("Incomplete data or bad data in file send by SAP: "+equipmentMoveDataDTO.getErrorDesc());
			    			sb.append("Incomplete data or bad data in file send by SAP: "+equipmentMoveDataDTO.getErrorDesc());
			    			equipmentMoveResponse.setErrorDesc("Incomplete data or bad data in file send by SAP: ");
			    	    	equipmentMoveResponse.setReturnCode(GRTConstants.WS_ERROR_CODE);
				    	} else{
				    		logger.error("Error while processing the EQR:  "+equipmentMoveDataDTO.getErrorDesc());
				    		sb.append("Error while processing the EQR...");
				    		equipmentMoveResponse.setErrorDesc("Internal Server Error");
				        	equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
				    	}
					}
				} else {
					logger.debug("Site Registration is coming null (OR) Equipment removal status is completed before asynchronous response come from SAP.");
					sb.append("Site Registration is coming null (OR) Equipment removal status is completed before asynchronous response come from SAP...");
					equipmentMoveResponse.setErrorDesc("Site Registration is coming null (OR) Equipment removal status is completed before asynchronous response come from SAP.");
		        	equipmentMoveResponse.setReturnCode(GRTConstants.WS_ERROR_CODE);
				}
	    	} catch (DataAccessException e) {
				logger.debug("Error while getting the Site Registration for the registrationId---"+equipmentMoveDataDTO.getTransactionId());
				sb.append("Error while getting the Site Registration for the registrationId...");
				equipmentMoveResponse.setErrorDesc("Internal Server Error - "+e.getMessage());
				equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
				//throw new DataAccessException(e.getMessage());
			}
	    	logger.debug("++++++EQR Error list size+++++++++ :"+errorList.size());
    		List<String> deactivationEQNList = new ArrayList<String>();
    		List<TechnicalOrderDetail> erroredTechnicallyRegisteredAssets = new ArrayList<TechnicalOrderDetail>();
	    	if (errorList.size() > 0) {
	    		sb.append("Inside if when error list size is more than 0...");
	    		// Update warnings/errors for the equipments
	    		logger.debug("Update warnings/errors for the equipments *** STARTS");
	    		logger.debug("techOrderDetailList size:"+techOrderDetailList.size());
	    		for(TechnicalOrderDetail technicalOrderDetail : techOrderDetailList){
	    			for(EquipmentMoveResponseDto equipmentMoveResponseDto: errorList){
	    				if(TechnicalRegistrationUtil.truncateLeadingZeros(equipmentMoveResponseDto.getFromEquipmentNumber()).equalsIgnoreCase
	    						(TechnicalRegistrationUtil.truncateLeadingZeros(technicalOrderDetail.getSummaryEquipmentNumber()))){
	    					// Set Error description to update in Technical Order table when Error Description is not from 1 to 5 types
	    					technicalOrderDetail.setErrorDescription(equipmentMoveResponseDto.getErrorDesc());
	    					if(TechnicalRegistrationUtil.filterErrorDescription(equipmentMoveResponseDto.getErrorDesc()) != null){
	    						errors.add(technicalOrderDetail.getMaterialCode() + "-" + equipmentMoveResponseDto.getErrorDesc());
	    					}
	    					// technicalOrderDetail.setSr_created("yes");
	    					// errors.add(technicalOrderDetail.getMaterialCode() + "-" + equipmentRemovalResponseDto.getErrorDescription());
	    					errorTodList.add(technicalOrderDetail);
	    					// Assign few properties to send as an attachemnt to the SR
	    					equipmentMoveResponseDto.setMaterialCode(technicalOrderDetail.getMaterialCode());
	    					logger.debug("EQM Dto MC: "+equipmentMoveResponseDto.getMaterialCode()+"   TOD Dto MC:"+technicalOrderDetail.getMaterialCode());
	    					equipmentMoveResponseDto.setMaterialCodeDescription(technicalOrderDetail.getDescription());
	    					logger.debug("EQR Dto MCD: "+equipmentMoveResponseDto.getMaterialCodeDescription()+"   TOD Dto MCD:"+technicalOrderDetail.getDescription());
	    					equipmentMoveResponseDto.setSeid(technicalOrderDetail.getSolutionElementId());
	    					logger.debug("EQR Dto SEID: "+equipmentMoveResponseDto.getSeid()+"   TOD Dto SEID:"+technicalOrderDetail.getSolutionElementId());
	    					equipmentMoveResponseDto.setIsActiveContract(technicalOrderDetail.getActiveContractExist());
	    					logger.debug("EQR Dto IAC: "+equipmentMoveResponseDto.getIsActiveContract()+"   TOD Dto IAC:"+technicalOrderDetail.getActiveContractExist());
	    				}
	    			}
	    		}
	    		sb.append("Update warnings/errors for the equipments...");
	    		logger.debug("Update warnings/errors for the equipments *** ENDS");
	    		
	    		logger.debug("Create Siebel SR");
	    		sb.append("Create Siebel SR...");
	    		// ***** SR - creation logic starts for the equipments with Error Description
	    		c1 = Calendar.getInstance().getTimeInMillis();
	    		for(TechnicalOrderDetail technicalOrderDetail:errorTodList){
	    			/** Restrict SR Creation when error description is 1,2,3,5,9 in the following
	    			 *  1. Equipment is not installed at any location
	    			 *  2. Invalid Equipment nr
	    			 *  3. Equipment is installed at a different FL XXXXXX
	    			 *  4. Material doesn't exists in SAP
	    			 *  5. Functional location doesn't exists in SAP
	    			 *  ...
	    			 *  9. SAP Equip No XXXX is not valid for Material Code YYYY
	    			 *  For other categories it just creates SR and deactivation/updation is Siebel wont be done
	    			 */
	    			/*if(technicalOrderDetail.getErrorDescription() != null
	    					&& !(technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Equipment is not installed at any location")
	    					|| technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Functional location doesn't exists in SAP")
	    					|| technicalOrderDetail.getErrorDescription().contains("Equipment is installed at a different FL")
	    					|| technicalOrderDetail.getErrorDescription().contains("is not valid for Material Code")
	    					|| technicalOrderDetail.getErrorDescription().equalsIgnoreCase("Invalid Equipment nr"))){*/
	    			//Commented code as it not applicable for Equipment Move : no requirement
	    			if(technicalOrderDetail.getErrorDescription() != null){
	    				technicalOrderDetail.setSr_created("yes");
	    				eqmListForSRCreation.add(technicalOrderDetail);
	    			}
	    		}
	    		sb.append("Create Siebel SR with actual error description & update the error desc to Technical_Order table...");
	    		if(errorTodList.size() > 0){
	    			//Implemented this method in TechnicalRegistrationDao
	    			getTechnicalRegistrationDao().updateTechnicalOrderErrorDescription(errorTodList);
	    		}
	    		// Updating the technicalOrders with Error Description - 1,3,5
	    		/*if(updateTechOrderList.size() > 0){
	    			getRegistrationDao().updateTechnicalOrderErrorDescription(updateTechOrderList);
	    		}*/

	    		if(eqmListForSRCreation != null && eqmListForSRCreation.size() > 0){
	    			sb.append("Creating the SR for complete EQM list on given file path");
		    		SRRequestDto srRequestDto = null;
		    		//Generating file path
		    		String filePath = autoGenAttachmentPath(EQM_ATTACHMENT_NAME);
		    		// Generating worksheet
		    		TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
		    		List<Attachment> attList = new ArrayList<Attachment>();
		    		//Implemented this method in TechnicalOrderDetailWorksheetProcessor
		    		Attachment attachment = worksheet.generateFinalValidationErrorWorksheet(eqmListForSRCreation, filePath, siteRegistration, GRTConstants.TECH_ORDER_TYPE_EM);
		    		attList.add(attachment);
		    		logger.debug("Siebel SR Creation : RegId :"+equipmentMoveDataDTO.getTransactionId()+"   soldTo :"+siteRegistration.getSoldToId());
		    		SRRequest srRequest = null;
		    		// SiteRegistration siteRegistration = getRegistrationDao().getSiteRegistration(equipmentRemovalResponseDto.getTransactionId());
		    		//Implemented this method in TechnicalRegistrationDao
		    		
		    		try {		    			
		    			srRequest = getTechnicalRegistrationDao().initSRRequest(siteRegistration, eqmProcessStep);
		    		} catch (DataAccessException e) {
		    			logger.error("----Unable to create an SR Request Id on table----", e);
		    			//throw e;
		    		}
		    		
		    		if (srRequest != null) {
		    			sb.append("Processing the SR request if its not null...");
		    			srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
		    			this.processSRRequest(siteRegistration.getRegistrationId(), siteRegistration.getSoldToId(), attList, srRequestDto, eqmProcessStep);
		    		}
		    		logger.debug("Before updating error description and SR_Created flag");
		    		if(errorTodList.size() > 0){		    			
		    			getTechnicalRegistrationDao().updateTechnicalOrderErrorDescription(errorTodList);
		    		}
		    		logger.debug("Setting IsSrCompleted to no on SiteRegistration");
		    	
		    		getTechnicalRegistrationDao().updateIsSrCompletedSiteRegistration(siteRegistration, eqmProcessStep, GRTConstants.NO);

		    		c2 = Calendar.getInstance().getTimeInMillis();
		    		logger.debug("TIMER for SR creation for Errored Equipments of EQR for RegID:"+siteRegistration.getRegistrationId()+" time in milliseconds :"+ (c2-c1));
	    		}
	    		// ***** SR - creation logic Ends

	    	}
	    	// Fetching SiteRegistration again to check for the status
	    	sb.append("Fetching SiteRegistration again to check for the status...");
	    	siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(equipmentMoveDataDTO.getTransactionId());
	    	// Updating registration status to Awaiting Info
	    	if(eqmListForSRCreation.size() > 0 && !StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(siteRegistration.getEqrMoveStatus().getStatusId())){
				statusEnum = StatusEnum.AWAITINGINFO;
				//Implemented this method in TechnicalRegistrationDao
				sb.append("Updating registration status to Awaiting Info for EQM list used in SR creation...");
				getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, statusEnum, eqmProcessStep, false);
    			sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), eqmProcessStep, statusEnum, errors);
	    	}
	    	
	    	// Check for AssetUpdate
	    	/*try {
				long hours = TechnicalRegistrationUtil.calculateNoOfHours(siteRegistration.getEqrMoveSubmittedDate());
				if(hours > Long.parseLong(getIbIntiatedBeforeHours())){
					processSiebelUpdateResponse(siteRegistration.getRegistrationId());
				}
			} catch (Exception e) {
				logger.debug("Exception while processing Asset Update. Exception:"+e.getMessage());
				sb.append("Exception while processing Asset Update....");
			}*/
    	} catch (Exception e) {
    		e.printStackTrace();
			logger.debug("Error while processing the registration:"+equipmentMoveDataDTO.getTransactionId());
			sb.append("Error while processing the registration...");
			equipmentMoveResponse.setErrorDesc("Internal Server Error - "+e.getMessage());
			equipmentMoveResponse.setReturnCode(GRTConstants.WS_INTERNAL_ERROR_CODE);
			//throw new DataAccessException(e.getMessage());
		} finally {
    		SkipDateTimeEscalator.unset();
    	}
    	logger.debug("Exiting TechnicalRegistrationService.processReceivedEquipmentRemovalReq");
    	
    	equipmentMoveResponse.setRoutingInfo(sb.toString());
    	return equipmentMoveResponse;
    }
    
    
    /**
     * Method to get the technical order details for the given registration id and orderType.
     *
     * @param registrationId
     *            String
     * @return technicalOrderDtoList List
     */
    private List<TechnicalOrderDetail> getTechnicalOrderOnRegIdOrderTypeIsSALGateways(
    		String registrationId, String orderType, boolean isSALGateways) throws DataAccessException {
    	logger.debug("Enetering TechnicalRegistrationService.getTechnicalOrderOnRegIdOrderTypeIsSALGateways with registrationId"+registrationId);
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	List<String> mcsToBeProcessed = new ArrayList<String>();
        List<TechnicalOrder> technicalOrderDetail = getTechnicalRegistrationDao()
                .getTechnicalOrderOnRegIdOrderTypeIsSALGateways(registrationId, orderType, isSALGateways);
        
        List<TechnicalOrderDetail> technicalOrderDtoList = TechnicalRegistrationUtil.prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
        try {
        	if(!technicalOrderDtoList.isEmpty()){
				getSiebelClient().deActivateSolutionElements(technicalOrderDtoList);
				for(TechnicalOrderDetail technicalOrderDetail2:technicalOrderDtoList){
					mcsToBeProcessed.add(technicalOrderDetail2.getMaterialCode());
				}
				if(!mcsToBeProcessed.isEmpty()){
					getTechnicalRegistrationDao().updateTODOpenQuantity(registrationId, registrationId, mcsToBeProcessed, GRTConstants.TECH_ORDER_TYPE_FV);
				}
        	} else {
        		logger.debug("getTechnicalOrderOnRegIdOrderTypeIsSALGateways have empty list to process.. RegId:"+registrationId);
        	}

		} catch (Exception e) {
			logger.debug("Exception in TechnicalRegistrationService.getTechnicalOrderOnRegIdOrderTypeIsSALGateways: "+e.getMessage());
			e.printStackTrace();
		}
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug(c2-c1 +" milliseconds");
        logger.debug("Exiting TechnicalRegistrationService.getTechnicalOrderOnRegIdOrderTypeIsSALGateways");
        return technicalOrderDtoList;
    }
    
    /**
     * Method to get the technical order details for the given registration id and orderType.
     *
     * @param registrationId
     *            String
     * @return technicalOrderDtoList List
     */
    public List<TechnicalOrderDetail> getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM(
    		String registrationId, String orderType, boolean isSALGateways, String fromSoldTo, String toSoldTo) throws DataAccessException {
    	logger.debug("Entering EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM");
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	List<String> mcsToBeProcessed = new ArrayList<String>();
        List<TechnicalOrder> technicalOrderDetail = getTechnicalRegistrationDao()
                .getTechnicalOrderOnRegIdOrderTypeIsSALGateways(registrationId, orderType, isSALGateways);
        List<TechnicalOrderDetail> technicalOrderDtoList = TechnicalRegistrationUtil.prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
        
        //GRT 4.0 Change : Add From and To Sold to for move
        for(TechnicalOrderDetail techD:technicalOrderDtoList){
        	techD.setSoldToId(fromSoldTo);
        	techD.setToSoldToId(toSoldTo);
		}
        
        try {
        	if(!technicalOrderDtoList.isEmpty()){
        		StringBuffer sb = new StringBuffer();
				//getSiebelClient().eqmSiebelAssetUpdate(prepareAssetsTobeMovedFromSiebel(technicalOrderDtoList), sb);
        		getSiebelClient().eqmSiebelAssetUpdate(technicalOrderDtoList, sb);
				for(TechnicalOrderDetail technicalOrderDetail2:technicalOrderDtoList){
					mcsToBeProcessed.add(technicalOrderDetail2.getMaterialCode());
				}
				if(!mcsToBeProcessed.isEmpty()){
					getTechnicalRegistrationDao().updateTODOpenQuantity(registrationId, registrationId, mcsToBeProcessed, GRTConstants.TECH_ORDER_TYPE_EM);
				}
        	} else {
        		logger.debug("getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM have empty list to process.. RegId:"+registrationId);
        	}
		} catch (Exception e) {
			logger.debug("Exception in EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM: "+e.getMessage());
			e.printStackTrace();
		}
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug(c2-c1 +" milliseconds");
        logger.debug("Exiting EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM");
        return technicalOrderDtoList;
    }  
   
    
	public void processSiebelUpdateResponse(String registrationId) throws Exception{
		logger.debug("Enetering TechnicalRegistrationService.processSiebelUpdateResponse with registrationId"+registrationId);
		//boolean queryFlag = false;
		String jmsMessage = null;
		//if(queryFlag){
			List<Object[]> list = getTechnicalRegistrationDao().queryResponseOnRegistrationId(registrationId, GRTConstants.SIEBEL_ASSET_UPDATE);

			logger.debug("Records in FMW DB: "+list.size()+"   for RegistrationId:"+registrationId);
			if(list != null && !list.isEmpty()){
				for(Object[] objArr:list){
					if(objArr != null && objArr.length == 4){
						String sourceQueue = objArr[2] != null ? objArr[2].toString():""; 
						logger.debug("Source Queue Name:"+sourceQueue);
						SerializableClob data = (SerializableClob)objArr[3];
						BufferedReader reader = new BufferedReader(new InputStreamReader(data.getAsciiStream()));
				        StringBuilder sb = new StringBuilder();
				        String line = null;

				        while ((line = reader.readLine()) != null) {
				        sb.append(line);
				        }
				        data.getAsciiStream().close();
						logger.debug("XML:\n"+sb.toString());
						if(StringUtils.isNotEmpty(sourceQueue) && getSiebelUpdateQueue().equalsIgnoreCase(sourceQueue)){
							//Process IB SAP Response
							jmsMessage = TechnicalRegistrationUtil.extractJmsBodyXml(sb.toString());
							logger.debug("Extracted JMS Body from XML:\n"+sb.toString());
							SiebelMessage msgAssetUpdate = TechnicalRegistrationUtil.parseXMLJms(jmsMessage);
			                InstallBaseAsset instalBaseDto = TechnicalRegistrationUtil.populateServiceRequestDto(msgAssetUpdate,registrationId);
							this.processReceivedSiebelAsset(instalBaseDto);
						}
					}
				}
			} else {
				logger.debug("***************************************************************************************************************");
				logger.error("Registration Id :" + registrationId + " does not have entry for ASSET UPDATE in FMW:MESSAGE_AUDIT_LOG table.");
				logger.debug("***************************************************************************************************************");
			}
			logger.debug("Exiting TechnicalRegistrationService.processSiebelUpdateResponse");
		}
	//}
	
	// Renamed from processReceivedSiebelAssetNew to processReceivedSiebelAsset
	public InstallBaseAsset processReceivedSiebelAsset(InstallBaseAsset installBaseAssetDto) {
		StringBuffer sb = new StringBuffer();
		sb.append(installBaseAssetDto.getRoutingInfo().toString());
	    	try {
	    		logger.debug("Entering TechnicalRegistrationService.processReceivedSiebelAsset");
				sb.append("Entering into TechnicalRegistrationService.processReceivedSiebelAsset Method to process Asset Update...");
	    		SkipDateTimeEscalator.set(Boolean.TRUE);
		    	logger.debug("Entering processReceivedSiebelAssetNew");
		    	List<String> processedRegistrations = new ArrayList<String>();
		    	List<String> mcList = new ArrayList<String>();
		    	try {
		    		String ibRegistrationId = installBaseAssetDto.getRegistrationId();
		    		if(StringUtils.isNotEmpty(ibRegistrationId)){
					sb.append("Creating the Asset data list if Registration id is not empty...");
		    			List<InstallBaseAssetData> assetDataList = installBaseAssetDto.getInstallBaseAssetData();
		    			try {
		    				getTechnicalRegistrationDao().saveSiebelAssetDataList(assetDataList, ibRegistrationId);
							sb.append("Save the Siebel Asset Data for each asset from the list...");
		    			} catch (Exception e) {
		    				logger.error("Exception while persisting Asset Update data..." + e.getMessage());
		    			}
						sb.append("Going inside for loop to create Material code list...");
		    			for(InstallBaseAssetData ibAssetData : assetDataList){
		    				if(ibAssetData != null && ibAssetData.getMaterialCode() != null && !mcList.contains(ibAssetData.getMaterialCode())){
		    					mcList.add(ibAssetData.getMaterialCode());
		    				}
		    			}
		    			//Check for Registration SAP Completedness
						sb.append("Check for Registration SAP Completeness...");
		    			processIBAndEQRSAPResponse(ibRegistrationId);
						sb.append("Processed IB & EQR Sap responses...");
		    			//Implemented this method in TechnicalRegistrationDao
		    			List<String[]> dataToProcess = getTechnicalRegistrationDao().getPipelineForProcessing(ibRegistrationId, mcList);
						sb.append("Getting all the asset from the pipeline to process the data...");
		    			if(dataToProcess != null && dataToProcess.size()>0) {
		    				logger.debug("Registrations List Size:"+dataToProcess.size());
							sb.append("Registrations List Size:"+dataToProcess.size()+"...");
							int count = 1;
		    				for (String[] data : dataToProcess) {
		    					int flag = 0;
		    					String registrationId = data[0];
		    					String action = data[1];
		    					logger.debug("RegistartionId:"+registrationId+"     Action:"+action);
								if(data != null && StringUtils.isNotEmpty(registrationId)) {
									//Implemented this method in TechnicalRegistrationDao
									SiteRegistration siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(registrationId);
									sb.append("Getting the siteRegistration for the "+ count +"data to be processed...");
									count = count+1;
									sb.append("Process the data based on action which can be IB , FV & EM...");
									if(StringUtils.isNotEmpty(action)) {
										if(GRTConstants.TECH_ORDER_TYPE_IB.equals(action)) {
											processedRegistrations.add(registrationId);
											//Implemented locally
											List<Set<TechnicalOrder>> techOrderList =this.fetchTechnicalOrders(registrationId, GRTConstants.TECH_ORDER_TYPE_IB, mcList);
											techOrderList = new ArrayList();
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
																	sb.append("Update technical order list based on site reg install base status - which is Not Complete and not Cancelled - only those who are in Awaitin Info status...");
																	to.setOpenQuantity(0L);
																	to.setRemainingQuantity(0L);
																	to.setCompletedDate(new Date());
																	to.setCompletedBysiteRegId(ibRegistrationId);
																	updateTOs.add(to);
																}
															}
														}
														if(updateTOs.size() > 0) {
															sb.append("when technical order list is not empty, Save all new technical order in the table...");
															getTechnicalRegistrationDao().saveTechnicalOrderList(updateTOs);
														}
													}
												} else {
													List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
													for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
														if(to.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_IB)) {
															to.setOpenQuantity(0L);
															to.setRemainingQuantity(0L);
															to.setCompletedDate(new Date());
															to.setCompletedBysiteRegId(ibRegistrationId);
															updateTOs.add(to);
														}
													}
													if(updateTOs.size() > 0) {
														getTechnicalRegistrationDao().saveTechnicalOrderList(updateTOs);
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
														//Implemented this method in TechnicalRegistrationDao
														getTechnicalRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.INSTALL_BASE_CREATION, false);
									    				sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, StatusEnum.COMPLETED);
													}
												}
											}
										} else if(GRTConstants.TECH_ORDER_TYPE_FV.equals(action)) {
											List<String> mcsToBeProcessed = new ArrayList<String>();
											List<Set<TechnicalOrder>> techOrderList = fetchTechnicalOrders(registrationId, GRTConstants.TECH_ORDER_TYPE_FV, mcList);
											if(techOrderList != null && techOrderList.size() > 0){
												siteRegistration.setTechnicalOrders(techOrderList.get(0));
											}
											if(!siteRegistration.getFinalValidationStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) &&
												!siteRegistration.getFinalValidationStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())) {
												if(siteRegistration.getFinalValidationStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())){
													if(siteRegistration.getTechnicalOrders() != null && siteRegistration.getTechnicalOrders().size() > 0) {
														List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
														for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
															if(to.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_FV)) {
																if(StringUtils.isEmpty(to.getSr_Created()) || (!to.getSr_Created().equalsIgnoreCase(GRTConstants.YES))) {
																	sb.append("Update technical order list for Tech Order type Final Validation, based on site reg install base status - which is Not Complete and not Cancelled & only those who are in Awaiting Info status...");
																	to.setOpenQuantity(0L);																	
																	to.setRemainingQuantity(0L);
																	to.setCompletedDate(new Date());
																	to.setCompletedBysiteRegId(ibRegistrationId);
																	//to.setDescription("");
																	updateTOs.add(to);
																	sb.append("update the Technical Order table with open quantity, remaining quantity, completed date and registration id...");
																	mcsToBeProcessed.add(to.getMaterialCode());
																}
															}
														}
														if(updateTOs.size() > 0) {
															getTechnicalRegistrationDao().saveTechnicalOrderList(updateTOs);
														}
													}
												} else {
													mcsToBeProcessed.addAll(mcList);
												}
												/*else {
													if(techOrderList.get(1) != null && techOrderList.get(1).size() > 0){
														for(TechnicalOrder to:techOrderList.get(1)){
															if(to.getOpenQuantity() != 0){
																flag = 1;
															}
														}
													}
													if(flag == 0){
														this.getRegistrationDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.FINAL_RECORD_VALIDATION);
									    				this.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, StatusEnum.COMPLETED);
													}
												}*/											
												processRegistration(siteRegistration, ibRegistrationId, mcsToBeProcessed, ProcessStepEnum.FINAL_RECORD_VALIDATION, false);
												sb.append("Process the registeration further for Final Record Validation...");
											}

										}
										//GRT 4.0 Start of Asset update for EM - Equipment Move
										else if(GRTConstants.TECH_ORDER_TYPE_EM.equals(action)) {
											sb.append("Asset update for EM - Equipment Move");
											List<String> mcsToBeProcessed = new ArrayList<String>();
											List<Set<TechnicalOrder>> techOrderList = fetchTechnicalOrders(registrationId, GRTConstants.TECH_ORDER_TYPE_EM, mcList);
											if(techOrderList != null && techOrderList.size() > 0){
												siteRegistration.setTechnicalOrders(techOrderList.get(0));
											}
											if(!siteRegistration.getEqrMoveStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) &&
												!siteRegistration.getEqrMoveStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())) {
												if(siteRegistration.getEqrMoveStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())){
													if(siteRegistration.getTechnicalOrders() != null && siteRegistration.getTechnicalOrders().size() > 0) {
														List<TechnicalOrder> updateTOs = new ArrayList<TechnicalOrder>();
														for (TechnicalOrder to : siteRegistration.getTechnicalOrders()) {
															if(to.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_EM)) {
																if(StringUtils.isEmpty(to.getSr_Created()) || (!to.getSr_Created().equalsIgnoreCase(GRTConstants.YES))) {
																	sb.append("Update technical order list for Tech Order type EM, based on site reg install base status - which is Not Complete and not Cancelled & also for only those who are in Awaitin Info status...");
																	to.setOpenQuantity(0L);
																	to.setRemainingQuantity(0L);
																	to.setCompletedDate(new Date());
																	to.setCompletedBysiteRegId(ibRegistrationId);
																	//to.setDescription("");
																	updateTOs.add(to);
																	mcsToBeProcessed.add(to.getMaterialCode());
																}
															}
														}
														if(updateTOs.size() > 0) {
															getTechnicalRegistrationDao().saveTechnicalOrderList(updateTOs);
														}
													}
												} else {
													mcsToBeProcessed.addAll(mcList);
												}
												//Process the registration											
												processRegistration(siteRegistration, ibRegistrationId, mcsToBeProcessed, ProcessStepEnum.EQUIPMENT_MOVE, false);
												sb.append("Process the registeration further for Equipment Move...");
											}

										}
										//GRT4.0 - End of Asset update for EM - Equipment Move
										
									}
								}
							}
		    				if(processedRegistrations.size() > 0) {
		    					sb.append("Update the piprline for the processed transactions for action type IB...");
		    				
		    					getTechnicalRegistrationDao().updateProcessedPipelineTransactions(processedRegistrations, mcList, GRTConstants.TECH_ORDER_TYPE_IB);
		    				}
		    			}
		    		}
		    	} catch (LockAcquisitionException lockAcquisitionException) {
		    		sb.append("Catch exception for LockAcquisition...");
		    		throw lockAcquisitionException;
				} catch (StaleObjectStateException staleObjectStateException) {
					throw staleObjectStateException;
				} catch (GenericJDBCException genericJDBCException) {
					sb.append("JDBC connection exception...");
					throw genericJDBCException;
				} catch (Throwable throwable) {
					logger.error("", throwable);
				}
	    	} finally {
	    		SkipDateTimeEscalator.unset();
	    		logger.debug("Exiting TechnicalRegistrationService.processReceivedSiebelAsset");
	    	}
	    	sb.append("Exiting TechnicalRegistrationService.processReceivedSiebelAsset...");
	    	installBaseAssetDto.setRoutingInfo(sb.toString());
		return installBaseAssetDto;
    }

	private void processIBAndEQRSAPResponse(String ibRegistrationId) throws Exception{
		logger.debug("Entering TechnicalRegistrationService.processIBAndEQRSAPResponse");
		String jmsMessage = null;
    	List<Object[]> list = new ArrayList<Object[]>();
    	Object[] objectArr = null;
    	try {
			SiteRegistration siteRegistration = getTechnicalRegistrationDao().getSiteRegistration(ibRegistrationId);
			if(siteRegistration != null){
				String registrationType = siteRegistration.getRegistrationType().getRegistrationId();
				if((RegistrationTypeEnum.EQUIPMENTREMOVALONLY.getRegistrationID().equalsIgnoreCase(registrationType)
						|| RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID().equalsIgnoreCase(registrationType)) 
						&& (siteRegistration.getFinalValidationSubStatus() == null 
								|| !StatusEnum.SAPCOMPLETED.getStatusId().equals(siteRegistration.getFinalValidationSubStatus().getStatusId()))){
					////Implemented this method in TechnicalRegistrationDao
					objectArr = getTechnicalRegistrationDao().querySAPResponseOnRegistrationId(ibRegistrationId, false, true);
					if(objectArr != null){
						list.add(objectArr);
					}
				}
				if((RegistrationTypeEnum.INSTALLBASEONLY.getRegistrationID().equalsIgnoreCase(registrationType) 
						|| RegistrationTypeEnum.IPOFFICE.getRegistrationID().equalsIgnoreCase(registrationType)
						|| RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID().equalsIgnoreCase(registrationType))
						&& (siteRegistration.getInstallBaseSubStatus() == null 
								|| !StatusEnum.SAPCOMPLETED.getStatusId().equals(siteRegistration.getInstallBaseSubStatus().getStatusId()))){
					////Implemented this method in TechnicalRegistrationDao
					objectArr = getTechnicalRegistrationDao().querySAPResponseOnRegistrationId(ibRegistrationId, true, false);
					if(objectArr != null){
						list.add(objectArr);
					}
				}
			}
			logger.debug("SAP Response records in FMW DB: "+list.size()+"   for RegistrationId:"+ibRegistrationId);
			if(list != null && !list.isEmpty()){
				for(Object[] objArr:list){
					if(objArr.length == 4){
						String sourceQueue = objArr[2] != null ? objArr[2].toString():""; 
						logger.debug("Source Queue Name:"+sourceQueue);
						org.hibernate.lob.SerializableClob data = (org.hibernate.lob.SerializableClob)objArr[3];
						BufferedReader reader = new BufferedReader(new InputStreamReader(data.getAsciiStream()));
				        StringBuilder sb = new StringBuilder();
				        String line = null;

				        while ((line = reader.readLine()) != null) {
				        sb.append(line);
				        }
				        data.getAsciiStream().close();
						logger.debug("XML:\n"+sb.toString());
						if(StringUtils.isNotEmpty(sourceQueue) && getIbaseQueue().trim().equalsIgnoreCase(sourceQueue)){
							//Process IB SAP Response
							jmsMessage = TechnicalRegistrationUtil.extractJmsBodyXml(sb.toString());
							logger.debug("Extracted JMS Body from XML:\n"+sb.toString());
							InstallBaseSapResponseDto installBaseSapResponseDto = TechnicalRegistrationUtil.populateServiceRequestDto(
									TechnicalRegistrationUtil.parseXMLJmsIBase(jmsMessage),ibRegistrationId,GRTConstants.NUMBER_ZERO);
							processReceivedInstallBase(installBaseSapResponseDto);
						}
						if(StringUtils.isNotEmpty(sourceQueue) && getEqrQueue().equalsIgnoreCase(sourceQueue)){
							//Process EQR SAP Response
							jmsMessage = TechnicalRegistrationUtil.extractJmsBodyXml(sb.toString());
							EquipmentRemovalDataDTO equipmentRemovalDataDTO = TechnicalRegistrationUtil.populateServiceRequestDto(
									TechnicalRegistrationUtil.parseXMLJmsEqr(jmsMessage),ibRegistrationId,GRTConstants.NUMBER_ZERO);
							processReceivedEquipmentRemovalReq(equipmentRemovalDataDTO);
						}
					}
				}
			} else {
				logger.debug("***************************************************************************************************************");
				logger.error("Registration Id :" + ibRegistrationId + " not SAP completed and does not have entry in FMW:MESSAGE_AUDIT_LOG table.");
				logger.debug("***************************************************************************************************************");
			}
		} catch (Exception e) {
			logger.error("Exception:"+e.getMessage());
			throw new DataAccessException(e.getMessage());
		} catch (Throwable throwable) {
			logger.error("Error while processing SAP response before Asset Update.", throwable);
			throw new DataAccessException(throwable.getMessage());
		}
    	logger.debug("Exiting TechnicalRegistrationService.processIBAndEQRSAPResponse");
	}

	public String getPIEPollableData(TechnicalRegistration tr) throws DataAccessException {
    	String seCodeSeId = null;
    	logger.debug("Entering TechnicalRegistrationService.getPIEPollableData");
    	try {
    		if(tr != null && tr.getExplodedSolutionElements()!= null && tr.getExplodedSolutionElements().size() > 0) {
    			//Implemented this method in TechnicalRegistrationDao
    			List<String> piePollableSeCodes = getTechnicalRegistrationDao().getPIEPollableSeCodes();
    			
    			if(piePollableSeCodes != null && piePollableSeCodes.size() > 0) {
	    			for (ExpandedSolutionElement expandedSolutionElement : tr.getExplodedSolutionElements()) {
						if(expandedSolutionElement != null && StringUtils.isNotEmpty(expandedSolutionElement.getSeCode()) && StringUtils.isNotEmpty(expandedSolutionElement.getSeID())) {
							if(piePollableSeCodes.contains(expandedSolutionElement.getSeCode())) {
								return expandedSolutionElement.getSeCode() + ":" + expandedSolutionElement.getSeID();
							}
						}
					}
    			}
    		} else {
    			logger.warn("Not sufficient data stored for PIE Polling.");
    		}
    	} catch (Throwable throwable) {
			logger.error("Error while getting PIE pollable data.", throwable);
		} finally {
			logger.debug("Exiting TechnicalRegistrationService.getPIEPollableData");
		}
		return seCodeSeId;
    }
	
	/**
     * API to initiate CMPollings.
     *
     * @param List<TechnicalRegistration> trs
     * @return
     * @throws DataAccessException
     */
    public void initiateCMPolling(List<TechnicalRegistration> trs) throws Throwable {
    	logger.debug("Entering TechnicalRegistrationService.initiateCMPolling");
    	try {
    		if(trs != null) {
	    		for (TechnicalRegistration tr : trs) {
	    			try {
	    				if(tr != null && tr.getEpnSurveyStatus() == null) {
		    				getArtClient().initiateCMPolling(tr, getPIEPollableData(tr));
		    				//Implemented this method in TechnicalRegistrationSRDao 
		    				getTechnicalRegistrationDao().updateTechnicalRegistrationEPNSurveyStatus(tr, StatusEnum.INPROCESS);
	    				}
	    			} catch (Throwable throwable) {
	    				logger.error("Error while initatingPolling, continuing...", throwable);
	    			}
	    		}
    		}
    	} catch(Throwable throwable) {
    		logger.error("", throwable);
    	}
    	logger.debug("Exiting TechnicalRegistrationService.initiateCMPolling");
    }
    
	public String getSalgwMC() {
		return salgwMC;
	}

	public void setSalgwMC(String salgwMC) {
		this.salgwMC = salgwMC;
	}

	public String getIbIntiatedBeforeHours() {
		return ibIntiatedBeforeHours;
	}

	public void setIbIntiatedBeforeHours(String ibIntiatedBeforeHours) {
		this.ibIntiatedBeforeHours = ibIntiatedBeforeHours;
	}

	public String getIbaseQueue() {
		return ibaseQueue;
	}

	public void setIbaseQueue(String ibaseQueue) {
		this.ibaseQueue = ibaseQueue;
	}

	public String getEqrQueue() {
		return eqrQueue;
	}

	public void setEqrQueue(String eqrQueue) {
		this.eqrQueue = eqrQueue;
	}

	public TechnicalRegistrationDao getTechnicalRegistrationDao() {
		return technicalRegistrationDao;
	}

	public void setTechnicalRegistrationDao(
			TechnicalRegistrationDao technicalRegistrationDao) {
		this.technicalRegistrationDao = technicalRegistrationDao;
	}
	
}
