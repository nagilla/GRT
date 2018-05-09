package com.avaya.grt.web.action.recordvalidation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.service.equipmentremoval.EQRService;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.MaterialCodeListDto;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.GenericSort;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;
import com.opensymphony.xwork2.Action;
import org.apache.commons.lang.StringEscapeUtils;


public class RecordValidationAction extends TechnicalRegistrationAction {

	private static final Logger logger = Logger
			.getLogger(RecordValidationAction.class);
	
	private static final String EQUIPMENT_REMOVAL_ATTACHMENT_NAME = "EquipmentRemovalAttachment";	
	
	private static final String DATE_FORMAT1 = "MM/dd/yyyy HH:mm:ss";
	
	private static final String SERIAL_NUMBER_FORMAT = "[a-zA-Z0-9]+";
	
	private boolean manuallyAddMaterial;
	
	private Boolean isFirstTime = true;
	
	private EQRService eqrService;
	
	private String dataError = "";
	
	private InputStream inputStream;
	
	private List<String> defectMCs = new ArrayList<String>();
	
	protected List<MaterialCodeListDto> materialCodeList = new ArrayList<MaterialCodeListDto>();
	
	protected String uploadMessage = "";
	
	private boolean isProcessed;
	
	public String getDataError() {
		return dataError;
	}

	public void setDataError(String dataError) {
		this.dataError = dataError;
	}
	
	public EQRService getEqrService() {
		return eqrService;
	}

	public void setEqrService(EQRService eqrService) {
		this.eqrService = eqrService;
	}
	
	public List<MaterialCodeListDto> getMaterialCodeList() {
		return materialCodeList;
	}

	public void setMaterialCodeList(List<MaterialCodeListDto> materialCodeList) {
		this.materialCodeList = materialCodeList;
	}

	public String getUploadMessage() {
		return uploadMessage;
	}

	public void setUploadMessage(String uploadMessage) {
		this.uploadMessage = uploadMessage;
	}
	
	/**
	 * executes this method on click of record validation link on home page
	 * @return
	 * @throws Exception
	 */
	public String recordValidationOnly() throws Exception {
		logger.debug("Entering RecordValidationAction.recordValidationOnly()");
		validateSession();
		//reset other flags
		actionForm.setInstallbaseRegistrationOnly(false);//IB
		actionForm.setViewInstallbaseOnly(false); //view installbase
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setTechnicalRegistrationOnly(false); //tob
		actionForm.setFirstColumnOnly(false); //tob
		actionForm.setFullRegistrationOnly(false);
		actionForm.setSalMigrationOnly(false);
		
		//Set record validation flag
		actionForm.setRecordValidationOnly(true);
		if ("B".equals(getUserFromSession().getUserType()) || "C".equals(getUserFromSession().getUserType())) {
			logger.debug("Exiting RecordValidationAction.recordValidationOnly()");
			return "toLocationSelection";
		} else {
			logger.debug("Exiting RecordValidationAction.recordValidationOnly()");
			return "toAgentLocationSelection";
		}
	}

	/**
	 * method to save site registration details and fetch equipments from siebel
	 * @return
	 * @throws Exception
	 */
	public String saveSiteRegistration() throws Exception {
		logger.debug("Entering RecordValidationAction.saveSiteRegistration()");
		validateSession();
		//Defect #225 : Check for buffer overflow validation
		if( checkForBufferOverFlow( actionForm ) ){
			return "except";
		}
		getRequest().getSession().removeAttribute("eqrFlag");
		actionForm.setSaveSiteReg(true);
		long c1 = Calendar.getInstance().getTimeInMillis();
		String cause = "";
		actionForm.setMessage("");
		actionForm.setActveSRColumnOnly(true);
		actionForm.setBannerSrLabel(GRTConstants.TRUE);
		// User comes from Home screen->EQR only
		actionForm.setEqrFileDownloaded(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		actionForm.getManuallyAddedMaterialEntryList().clear();
		
		logger.debug("................ Starting for recordValidationProcess records fetching ................");
		String soldToId = null;
		try {
			List<TechnicalOrderDetail> technicalOrderDetailList = null;
			if (actionForm.getSoldToId() != null) {
				soldToId = actionForm.getSoldToId();
			} else if (getRequest().getParameter("soldToId") != null) {
				soldToId = getRequest().getParameter("soldToId");
			}

			if (soldToId != null) {
				// Fetching equipments from siebel and validating material codes existence against Material Exclusion table
				try{
					technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, true);
				}catch(DataAccessException dataExcep){
					cause = "DataAccessException";
				}
				logger.debug("equipmentRemovalProcess MaterialList after MaterialExclusion:  size: " + technicalOrderDetailList.size());
				actionForm.setMaterialsAfterExclusionList(technicalOrderDetailList);
				logger.debug("SaveSite Flag:  "+actionForm.getSaveSiteReg());
				if(actionForm.getSaveSiteReg()){
					// Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 0);
					logger.debug("Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					// Filter pipeline records
					technicalOrderDetailList = getInstallBaseService().filterTechnicalOrderOnPipeline(technicalOrderDetailList, soldToId, "DISP");
					
					logger.debug("Filtered pipeline records:"+technicalOrderDetailList.size());
					// Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 1);
					logger.debug("Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					actionForm.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED);
					actionForm.setBannerSubStatus("");
					actionForm.setBannerSrNumber("");
					actionForm.setBannerActiveContractSrNumber("");
					logger.debug("Size 1.5 in IBRecordsCommonAction "+technicalOrderDetailList.size());
				} else {
					// Fetching the saved EQR records from technical order
					technicalOrderDetailList = getInstallBaseService().getTechnicalOrderByType(actionForm.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV);
					RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
					actionForm.setBannerStatus(summary.getFinalValidationStatus());
					actionForm.setBannerSubStatus(summary.getFinalValidationSubStatus());
					actionForm.setBannerSrNumber(summary.getFinalValidationSrNo());
					actionForm.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
					actionForm.setCompany(summary.getRequestingCompany());
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
					if(summary.getEqrSubmittedDate() != null){
						actionForm.setBannerSubmittedDate(sdf.format(summary.getEqrSubmittedDate()));
					} else {
						actionForm.setBannerSubmittedDate("");
					}
					if(summary.getEqrCompletedDate() != null){
						actionForm.setBannerCompletedDate(sdf.format(summary.getEqrCompletedDate()));
					} else {
						actionForm.setBannerCompletedDate("");
					}
				}
				// Set all the Technical Order records as readonly based on form:readonly
				for(TechnicalOrderDetail techDto : technicalOrderDetailList){
					if(actionForm.isReadOnly()){
						techDto.setExclusionFlag(true);
					}
				}
				// Fetch pipeline IB and EQR records
				List<PipelineSapTransactions> pipelineList = getEqrService().getConsolidatedPipelineRecords(soldToId);
				if(pipelineList != null && pipelineList.size() > 0){
					logger.debug("Consolidated pipeline List size:"+pipelineList.size());
					for(TechnicalOrderDetail techDto : technicalOrderDetailList){
						for(PipelineSapTransactions pipelineDto : pipelineList){
							if(techDto.getMaterialCode().equalsIgnoreCase(pipelineDto.getMaterialCode())){
								if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV)){
									techDto.setPipelineEQRQuantity(pipelineDto.getQuantity());
								} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
									techDto.setPipelineIBQuantity(pipelineDto.getQuantity());
								}
							}
						}
					}
				} else {
					logger.debug("Consolidated pipeline List size:0");
				}
				CSSPortalUser cssPortalUser = getUserFromSession();
				String userType  = cssPortalUser.getUserType();
				if(!StringUtils.isEmpty(userType)){
					if (!userType.equals("B") && !userType.equals("C")){
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.TRUE);
					} else {
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.FALSE);
					}
				}
				RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
				if (summary.getFinalValidationStatusId() !=null
						&& !summary.getFinalValidationStatusId().equalsIgnoreCase(GRTConstants.AWAITNG_INFORMATION)) {
					for (TechnicalOrderDetail detail : technicalOrderDetailList) {
						if(StringUtils.isEmpty(detail.getExclusionSource())){
							detail.setErrorDescription("");
						}
					}
				}
				try {
					logger.debug("recordValidationProcess MaterialList after exclusion:  size: " + technicalOrderDetailList.size());
					GenericSort gs = new GenericSort("productLine", true);
					Collections.sort(technicalOrderDetailList, gs);
				}catch(Exception e){
					logger.error("Exception in View Install Base while sorting data : "+e.getMessage());

				}
				for(TechnicalOrderDetail tod : technicalOrderDetailList) {
					if(null == tod.getOriginalSerialNumber() && null != tod.getSerialNumber()) {
						String serialNumber = tod.getSerialNumber();
						tod.setOriginalSerialNumber(serialNumber);
					}
				}
				//Defect#406
				for(TechnicalOrderDetail orderDetail : technicalOrderDetailList) {
					if(!StringUtils.isEmpty(orderDetail.getIsMaestro()) || !StringUtils.isEmpty(orderDetail.getIsNortel()))
					{
						if(!getBaseRegistrationService().isIorBSoldToSAPMapping(soldToId))
						{
							if(GRTConstants.YES.equalsIgnoreCase(orderDetail.getActiveContractExist())) {
								orderDetail.setErrorDescription(grtConfig.getRvNortelMaestroWithContractErrMsg());
								orderDetail.setExcludedMaestroOrNortel(true);
							}
						}
					}
					
					//GRT 4.0 changes
					 if(StringUtils.isEmpty(orderDetail.getProductLine()))
		                {
		                	String productLine=getBaseRegistrationService().getProductLineByMaterialCode(orderDetail.getMaterialCode());
		                	logger.debug(" Product Line bank "+orderDetail.getMaterialCode());
		                	if(!StringUtils.isEmpty(productLine))
		                	{
		                		orderDetail.setProductLine(productLine);
		                	}
		                }
				}
				
				actionForm.setMaterialEntryList(technicalOrderDetailList);
				
			}
			//Processing the materialentrylist for displaying in Summarized View
			processMaterilaEntryListForSummarizedView(actionForm.getMaterialEntryList());	
			getRequest().setAttribute("materialList", actionForm.getMaterialEntryList());
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("................ Ending for recordValidationProcess records fetching ................");
			logger.debug("TIMER for Record Validation rendering for RegID:"+actionForm.getRegistrationId()+" time in milliseconds :" + (c2-c1));
			if("DataAccessException".equals(cause)){
				this.setDataError(grtConfig.getSeibelTimeoutErrorMsg());
			}else{
				this.setDataError(grtConfig.getNoRecordErrorMsg());
			}
		} catch(DataAccessException daexception){
			logger.error("Exception in RecordValidationAction.saveSiteRegistration() : ", daexception);
			throw new DataAccessException(RecordValidationAction.class, daexception
					.getMessage(), daexception);
		} catch (Exception e) {
			logger.error("Exception in RecordValidationAction.saveSiteRegistration() : ", e);
		}
		logger.debug("Exiting RecordValidationAction.saveSiteRegistration()");
		return Action.SUCCESS;
	}

	/**
	 * method to export record validation records to excel
	 * @return
	 * @throws Exception
	 */
	public String exportRecordValidationProcess() throws Exception {
		logger.debug("Entering RecordValidationAction.exportRecordValidationProcess()");

		validateSession();
		actionForm.setValidateDisabled(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		//Clear messages
		actionForm.setUploadMessage(null);
		actionForm.setMessage(null);
		String soldToIdVal = actionForm.getSoldToId();
		soldToIdVal = StringEscapeUtils.unescapeHtml(soldToIdVal);
		logger.debug("RecordValidationAction.exportRecordValidationProcess() soldToIdVal ******"+soldToIdVal);
				
		String filename = "RecordValidation_"+soldToIdVal+".xls";
		String filepath = "/tmp/"+filename;
		logger.debug("Filepath value*****************in Export method" + filepath);
		//String filepath = "/tmp/RecordValidation.xls";
		//String filepath = "RecordValidation.xls";*/
				//Set Original serial number to serial Number
		
		//String filepath = "/tmp/RecordValidation.xls";
		for (TechnicalOrderDetail tod : actionForm.getMaterialEntryList()) {
			tod.setSerialNumber(tod.getOriginalSerialNumber());
		}
		TechnicalOrderDetailWorsheetProcessor workSheetProcessor = new TechnicalOrderDetailWorsheetProcessor();
		workSheetProcessor.generateRVWorksheet(actionForm.getMaterialEntryList(), filepath);
		//actionForm.setEQRViewFileName(filename);
		actionForm.setEqrFileDownloaded(true);

		logger.debug("Exiting RecordValidationAction.exportRecordValidationProcess()");
		return Action.SUCCESS;
	}
	
	
	
	/**
	 * This method is used for downloading template excel file.
	 * @return
	 * @throws FileNotFoundException
	 */
	public String execute() throws FileNotFoundException {
		try{
			actionForm.setEqrFileDownloaded(false);
			//Clear messages
			actionForm.setUploadMessage(null);
			actionForm.setMessage(null);
			logger.debug("Execute method called>>>>>>>>>>>>>>>>>>");
			//String filePath = ServletActionContext.getServletContext().getRealPath("/others");
			//String filePath = request.getContextPath()+"/others";
			String filepath = "/tmp/RecordValidationTemplate.xls";
			//String filepath = "RecordValidationTemplate.xls";
			//logger.debug("File download path is:>>/>>>>>"+filePath);
			TechnicalOrderDetailWorsheetProcessor workSheetProcessor = new TechnicalOrderDetailWorsheetProcessor();
			workSheetProcessor.generateRVTemplateWorksheet(filepath);
			actionForm.setEqrFileTemplateDownloaded(true);
		}catch(Exception Ex){
			logger.error("File Download Error:>>>>>>", Ex);
		}
		return Action.SUCCESS;
	}
	
	/**
	 * This method is responsible for parsing and validating the imported excel
	 * @return
	 * @throws Exception
	 */
	public String importRecordValidationProcess() throws Exception {
		logger.debug("Entering RecordValidationAction.importRecordValidationProcess()");
		validateSession();
		actionForm.setUploadMessage(null);
		actionForm.setMessage(null);
		actionForm.setEqrFileDownloaded(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		actionForm.getErrorMessageMap().clear();
		//Discard manually added material in case of import
		if(null != actionForm.getManuallyAddedMaterialEntryList()) {
			actionForm.getManuallyAddedMaterialEntryList().clear();
		}
		
		String registrationId;

		if (actionForm.getRegistrationId() != null) {
			registrationId = actionForm.getRegistrationId();
		} else if (getRequest().getParameter("registrationId") != null) {
			registrationId = getRequest().getParameter("registrationId");
		} else {
			registrationId = "1001";
		}

		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		this.uploadMessage = "";
		if (!actionForm.getMaterialEntryList().isEmpty()) {
			logger.debug("RegistrationFormBean.getMaterialEntryList().size(): "+ actionForm.getMaterialEntryList().size());
		}

		try {
			InputStream uploadFile = new FileInputStream(actionForm.getTheFile().toString());
			String theFile = actionForm.getTheFileFileName();

			logger.debug("RecordValidationAction upload File information:: theFile=" + theFile);
			List<TechnicalOrderDetail> result = null;

			String fileExtn = getFileExtension(theFile);
			logger.debug("RegistrationController upload File information::theFile:" + theFile + ":fileExtn:" + fileExtn);
			if (fileExtn != null && fileExtn.equalsIgnoreCase("xlsx")) {
				result = getInstallBaseService().parseRecordValidationWorksheetXLSX(uploadFile);
			} else if (fileExtn != null && fileExtn.equalsIgnoreCase("xls")) {
				result = getInstallBaseService().parseRecordValidationWorksheet(uploadFile);
			} else {
				actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("incorrectFileFormatErrMsg") + "###" + grtConfig.getIncorrectFileFormatErrMsg());
				uploadFile.close();
				return Action.SUCCESS;
			}		
			
			logger.debug("RecordValidationAction upload Filessss information::theFile=" + theFile);
			logger.debug("registrationDelegate.parseWorksheet(filePath).size(): " + result.size());
			uploadFile.close();
			if (result.size() >= 1000) {
				logger.debug("Uploaded more than 1000 records ");
				this.uploadMessage = "display";
				actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("hugeUpdateErrorMessage") + "###" + grtConfig.getHugeUpdateErrorMessage());
				result = result.subList(0, 999);
			} if(result.size() > 0) {
				isProcessed = true;
				result = validateImportedData(result);

				result = validateMaterialCodes(result);
				if(null != actionForm.getManuallyAddedMaterialEntryList() && actionForm.getManuallyAddedMaterialEntryList().size() >0) {
					validateMaterialCodes(actionForm.getManuallyAddedMaterialEntryList());
				}
				
				for (TechnicalOrderDetail detail : result) {
					detail.setRegistrationId(registrationId);
					detail.setSelectforRegistration(true);
				}

				if (!result.isEmpty()) {
					logger.debug("getInstallBaseService.parseWorksheet(filePath).size(): " + result.size());
				}
				
				List<TechnicalOrderDetail> materialEntryList = actionForm.getMaterialEntryList();
				List<TechnicalOrderDetail> updatedAssetsList = new ArrayList<TechnicalOrderDetail>();
				
				//Find updated/added entries and make it available on top in ui
				ListIterator<TechnicalOrderDetail> iterator =  materialEntryList.listIterator();
				while(iterator.hasNext()){
					TechnicalOrderDetail todetail = iterator.next();
					if(null != todetail.getActionType()) {
						TechnicalOrderDetail todetailNew = todetail;
						iterator.remove();
						updatedAssetsList.add(todetailNew);
						todetailNew.setDeleted(true);
					}
				}
				
				materialEntryList.addAll(0, updatedAssetsList);
				GenericSort gs = new GenericSort("materialCode", true);
				Collections.sort(result, gs);
				
				actionForm.setValidateDisabled(true);
			}else {
				actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("noActionImportErrorMsg") + "###" + grtConfig.getNoActionImportErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception in RecordValidationAction.saveSiteRegistration() : ", e);
			e.printStackTrace();
			actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("incorrectHeaderErrMsg") + "###" + grtConfig.getIncorrectHeaderErrMsg());
		}

		if (!actionForm.getMaterialEntryList().isEmpty()) {
			actionForm.setSelectAndUnselectAllMeterial(true);
			logger.debug("RegistrationFormBean.getMaterialEntryList().size(): " + actionForm.getMaterialEntryList().size());
		}
		logger.debug("Exiting RecordValidationAction.importRecordValidationProcess()");
		return Action.SUCCESS;
	}
	
	/**
	 * method to get file extension
	 * @param fName
	 * @return
	 */
	private String getFileExtension(String fName){
		logger.debug("Entering RecordValidationAction.getFileExtension(String fName)");
		int mid= fName.lastIndexOf(".");
		String ext=fName.substring(mid+1,fName.length());
		logger.debug("Exiting RecordValidationAction.getFileExtension(String fName)");
		return ext;
	}
	
	/**
	 * method to add manual entry
	 * @return
	 * @throws Exception
	 */
	public String addMaterialEntry() throws Exception {
		logger.debug("Entering RecordValidationAction.addMaterialEntry()");
		logger.debug("isFirstTime :" + isFirstTime);
		validateSession();
		actionForm.setEqrFileDownloaded(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		actionForm.setValidateDisabled(true);
		actionForm.setUploadMessage(null);
		actionForm.setMessage(null);
		if (actionForm.isFirstTime()) {
			actionForm.setSelectAndUnselectAllMeterial(actionForm.isFirstTime());
			actionForm.setFirstTime(false);
		} else {
			logger.debug("actionForm.getSelectAndUnselectAllMeterial()"
					+ actionForm.getSelectAndUnselectAllMeterial());
			actionForm.setSelectAndUnselectAllMeterial(actionForm
					.getSelectAndUnselectAllMeterial());
		}
		TechnicalOrderDetail detail = new TechnicalOrderDetail();
		detail.setRegistrationId(actionForm.getRegistrationId());
		detail.setSelectforRegistration(true);
		detail.setActionType("A");
		boolean isAutoTR = false;
		if ((actionForm.getRemoteConnectivity() != null)
				&& ((actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes")) || (actionForm
						.getRemoteConnectivity().equalsIgnoreCase("No"))))	 {
			if (actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes")) {
				isAutoTR = true;
			}
			detail.setAutoTR(isAutoTR);
			getRequest().getSession().setAttribute("IPOR", "IPOR");
			getRequest().getSession().setAttribute("IBBack", "ipreg");
			//Update the corresponding values of session attributes in action Form
			actionForm.setIPOR("IPOR");
			actionForm.setIBBack("ipreg");
		}
		getRequest().getSession().removeAttribute("rbselect");
		actionForm.setRbselect(null);
		actionForm.getManuallyAddedMaterialEntryList().add(0,detail);
		//If errors are present in the list update the error msg flag
		for (int i=0; i< actionForm.getManuallyAddedMaterialEntryList().size(); i++) {
			if(actionForm.getManuallyAddedMaterialEntryList().get(i).getErrorDescription() != null && !actionForm.getManuallyAddedMaterialEntryList().get(i).getErrorDescription().isEmpty()) {
				actionForm.getManuallyAddedMaterialEntryList().get(i).setErrorFlag(true);
			} else {
				actionForm.getManuallyAddedMaterialEntryList().get(i).setErrorFlag(false);
			}
		}
				
		actionForm.setReturnCode("");
		actionForm.setMessage("");
		this.uploadMessage = "";
		// Begin: Implement the validation
		List<TechnicalOrderDetail> result = actionForm.getManuallyAddedMaterialEntryList();
		boolean mcexisting = false;
		boolean snexisting = false;
		if (result != null & result.size() > 0) {
			result = validateMaterialCodes(result);
			for(TechnicalOrderDetail importedDetail : result) {
				for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
					if(materialEntryList.getMaterialCode().equalsIgnoreCase(importedDetail.getMaterialCode())) {
						mcexisting = true;
						break;
					}
				} 
				for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
					if(materialEntryList.getSerialNumber().equalsIgnoreCase(importedDetail.getSerialNumber())) {
						snexisting = true;
						break;
					}
				} 
			}
		}

		manuallyAddMaterial = true;
		actionForm.setManuallyAddMaterial(true);
		logger.debug("Entering RecordValidationAction.addMaterialEntry()");
		return Action.SUCCESS;
	}
	
	/**
	 * add one manual entry on click of manual entry button
	 * @return
	 * @throws Exception
	 */
	public String addManuallyAddedMaterialEntry() throws Exception {
		
		actionForm.setUploadMessage(null);
		actionForm.setEqrFileDownloaded(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		actionForm.getErrorMessageMap().clear();
		
		List<TechnicalOrderDetail> result = actionForm.getManuallyAddedMaterialEntryList();
		if(result.size() > 0) {
			result = validateImportedData(result);
			//Clear any Upload Message
			actionForm.setUploadMessage(null);
		}
		actionForm.getMaterialEntryList().addAll (0, result);
		actionForm.getManuallyAddedMaterialEntryList().clear();
		return Action.SUCCESS;
	}
	/**
	 * Validating material code
	 * @param result
	 * @return
	 * @throws Exception
	 */
	private List<TechnicalOrderDetail> validateMaterialCodes(List<TechnicalOrderDetail> result) throws Exception {
		logger.debug("Entering RecordValidationAction.validateMaterialCodes(List<TechnicalOrderDetail> result)");
		List<String> materialCodes = new ArrayList<String>();
		List<TechnicalOrderDetail> mcValidatedTODs = new ArrayList<TechnicalOrderDetail>();
		for (TechnicalOrderDetail detail : result) {
			if(detail.getMaterialCode()!= null && StringUtils.isNotEmpty(detail.getMaterialCode()) ){
				materialCodes.add(detail.getMaterialCode());
			}
		}
		if (!materialCodes.isEmpty()) {
			Map<String, String> mDescMap = getInstallBaseService().getMaterialCodeDesc(materialCodes);
			Map<String, Object> map = getInstallBaseService().validateMaterialExclusion(materialCodes);
			for (TechnicalOrderDetail detail : result) {
				
				String desc = mDescMap.get(detail.getMaterialCode());
				if(null != desc) {
					logger.debug("DECRIPTION" + desc);
					detail.setDescription(desc);
					
					//Is TR Eligible
					if(! GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(detail.getActionType())) {
						List<TRConfig> trEligibleList = getInstallBaseService().isTREligible(detail.getMaterialCode());
						if(trEligibleList != null && trEligibleList.size() > 0){
							detail.setTechnicallyRegisterable(GRTConstants.YES);
						} else {
							detail.setTechnicallyRegisterable("");
						}
					}
					//For Newly added row Technically Registerable is always No
					if( GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(detail.getActionType())) {
						detail.setTechnicallyRegisterable("");
					}
					logger.debug(detail.getMaterialExclusion());
					if (map != null && map.size() > 0) {
						if (map.containsKey(detail.getMaterialCode())) {
							detail.setMaterialExclusion(GRTConstants.TRUE);
							logger.debug("MaterialExclusion" + detail.getMaterialExclusion());
							String exclusionSource = (String) map.get(detail.getMaterialCode());
							detail.setExclusionSource(exclusionSource);
							String warning="";
							if(GRTConstants.EXCLUSION_SOURCE_PLDS.equalsIgnoreCase(exclusionSource)){	
								warning = getGrtConfig().getExclusionSourcePLDS().trim();
							} else if(GRTConstants.EXCLUSION_SOURCE_KMAT.equalsIgnoreCase(exclusionSource)) {
								warning =  getGrtConfig().getExclusionSourceKMAT().trim();
							} else if (GRTConstants.EXCLUSION_SOURCE_DEFECTIVE.equalsIgnoreCase(exclusionSource)) {
								warning =  getGrtConfig().getExclusionSourceKMAT().trim();
							}
							//skip validation if already having error Defect#217
							if(detail.getErrorDescription() == null || detail.getErrorDescription().isEmpty()) {
								detail.setErrorDescription(warning);
							}
							//Empty description if material code is invalid ONLY IN CASE OF NEW ENTRY
							if(GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(detail.getActionType()) && (!isMaterialCodeExisting( detail.getMaterialCode()) || !warning.isEmpty()) ) {
								detail.setDescription("");
							}			
							
							mcValidatedTODs.add(constructTechnicalOrderDetailClone(detail));
						} else {
							detail.setMaterialExclusion(GRTConstants.FALSE);
							logger.debug("MaterialExclusion" + detail.getMaterialExclusion());
							detail.setExclusionSource(null);
							//Add to validatedTODs
							mcValidatedTODs.add(constructTechnicalOrderDetailClone(detail));
						}
					}else{
						detail.setMaterialExclusion(GRTConstants.FALSE);
						logger.debug("Material Excluded is zero"+detail.getMaterialExclusion());
						//Add to validatedTODs
						mcValidatedTODs.add(constructTechnicalOrderDetailClone(detail));
					}
					
				} else {
					//error message: Entered Material code not present in DB / Invalid
					populateErrorMessages(grtConfig.getRVMCNotPresentInDB(), detail.getMaterialCode());
				}
			}
		}
		logger.debug("Result size after validating Material code" + result.size());
		logger.debug("Exiting RecordValidationAction.validateMaterialCodes(List<TechnicalOrderDetail> result)");
		return mcValidatedTODs;
	}
	
	private List<TechnicalOrderDetail> validateImportedData(List<TechnicalOrderDetail> result) throws Exception{
		logger.debug("Entering RecordValidationAction.validateImportedData(List<TechnicalOrderDetail> result)");
		//Make sure that no duplicate serial number is entered.
		//Make sure that quantity is 1, for an asset with serial number. 
		List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
		List<String> processedSNs = new ArrayList<String>(); 
		int totalRecoedsToUpdate = 0;
		int totalUpdatedRecoeds = 0;
		
		//Defect#367- checking whether the imported mc is serialized and store info in map.
		List<String> materialCodes = new ArrayList<String>();
		for(TechnicalOrderDetail detail: result){
			materialCodes.add(detail.getMaterialCode());				
		}
		materialMasterMap = getInstallBaseService().populateMaterialSerialize(materialCodes);
		
		// Make sure material code entered is a valid one and some other row exists with same material code.
		for(TechnicalOrderDetail detail : result) {
			if(detail != null){
				boolean mcExisting, snExisting = false;
				Long initialQuantity, remainingQuantity = 0L;

				String materialCode = detail.getMaterialCode();
				String serialNumber = detail.getSerialNumber();
				
				//Exclude entries which are excluded
				boolean materailEntryExcluded = false;
				
				//Clear previously existing errors before validating
				if(!"A".equalsIgnoreCase(detail.getActionType())) {	
					if(null == detail.getErrorDescription() || StringUtils.isEmpty(detail.getErrorDescription())) {
						for(TechnicalOrderDetail techOrder : actionForm.getMaterialEntryList()) {
							if(techOrder.getMaterialCode().equalsIgnoreCase(detail.getMaterialCode()) && 
									techOrder.getSerialNumber().equalsIgnoreCase(detail.getSerialNumber()) ) {
								if(!techOrder.isExclusionFlag()) {
									techOrder.setErrorDescription(detail.getErrorDescription());
								} else {
									materailEntryExcluded = true;
								}
							}
						}
					}
					//Increase count of Record having Action type U or S
					totalRecoedsToUpdate++;
				}
				//Exclude entries which are excluded
				if(materailEntryExcluded) {
					continue;
				}
				mcExisting = isMaterialCodeExisting(materialCode);
				if(!mcExisting) {
					detail.setErrorDescription(grtConfig.getRVInvalidMC());					
				} 
				
				
				//If the material code is not serialized, clear serial number field  
				if("A".equalsIgnoreCase(detail.getActionType())) {		
					//set copy revised quantity to initial Quantity
					detail.setInitialQuantity(detail.getRemainingQuantity());
					if(isMaterialCodeSerialized(materialCode)) {						
						detail.setSerialized(GRTConstants.Y);				
						if(detail.getInitialQuantity() == null || detail.getInitialQuantity() != 1 ) {
							detail.setSerialNumber(null);
							serialNumber = null;
						}
					} else {
						detail.setSerialized(GRTConstants.N);
						detail.setSerialNumber(null);
						serialNumber = null;
					}
										
				}

				if(null != serialNumber && StringUtils.isNotEmpty(serialNumber)) {
					if("A".equalsIgnoreCase(detail.getActionType())) {					
						
						
						snExisting = isSerialNumberExisting(serialNumber);
						if(snExisting) {
							detail.setErrorDescription(grtConfig.getRVDuplicateSN());
						} 
						//Check for Duplicate serial number in new records
						if(processedSNs.contains(serialNumber)) {
							detail.setErrorDescription(grtConfig.getRVDuplicateSN());
						} else {
							processedSNs.add(serialNumber);
						}
					}else {
						String newSerialNumber = detail.getNewSerialNumber();
						if(null != newSerialNumber) {
							snExisting = isSerialNumberExisting(newSerialNumber);
							if(snExisting) {
								detail.setErrorDescription(grtConfig.getRVDuplicateSN());	
							} if(newSerialNumber.length() > 18 || !Pattern.compile(SERIAL_NUMBER_FORMAT).matcher(newSerialNumber).matches()) {
								detail.setErrorDescription(grtConfig.getRVInvalidSN());
							}
							//Check for Duplicate serial number in update records
							if(processedSNs.contains(newSerialNumber)) {
								detail.setErrorDescription(grtConfig.getRVDuplicateSN());
							} else {
								processedSNs.add(newSerialNumber);
							}
							
						} else {
							snExisting = isSerialNumberExistingForUpdateAssets(serialNumber);
							if(snExisting && "A".equalsIgnoreCase(detail.getActionType())) {
								detail.setErrorDescription(grtConfig.getRVDuplicateSN());	
							}
						}
					}
						//If the material is Technically registerable quantity should not be greater than 1
						if(GRTConstants.YES.equalsIgnoreCase(detail.getTechnicallyRegisterable())) {
							if(null != detail.getRemainingQuantity() && detail.getRemainingQuantity() > 1) {
								detail.setErrorDescription(grtConfig.getRevisedQtyErrForTechOnBorded());
								
							}
						}
						
						if(GRTConstants.Y.equalsIgnoreCase(detail.getSerialized())) {
							if(null != detail.getRemainingQuantity() && detail.getRemainingQuantity() > detail.getInitialQuantity()) {
								detail.setErrorDescription(grtConfig.getRevisedQtyErrForSerializedAsset());
							}
						}
						
					
					if(serialNumber.length() > 18 || !Pattern.compile(SERIAL_NUMBER_FORMAT).matcher(serialNumber).matches()) {
						if("A".equalsIgnoreCase(detail.getActionType())) {
							detail.setErrorDescription(grtConfig.getRVInvalidSN());
						} else {
							detail.setErrorDescription(grtConfig.getRVInvalidSN());
						}
					}

					initialQuantity = detail.getInitialQuantity();
					
					
					if("A".equalsIgnoreCase(detail.getActionType()) && (detail.getErrorDescription() == null || detail.getErrorDescription().isEmpty())) {
						if(null == initialQuantity || initialQuantity < 0) {
							detail.setErrorDescription(grtConfig.getRVInvalidQty());
						}
						
						if(null != initialQuantity && initialQuantity == 0) {
							detail.setErrorDescription(grtConfig.getRVEmptyQty());
						}
					}

					if("A".equalsIgnoreCase(detail.getActionType())) {
						if(null != initialQuantity && initialQuantity > 1) {
							if("A".equalsIgnoreCase(detail.getActionType())) {
								detail.setErrorDescription(grtConfig.getRVInvalidQtyWithSN());
							}
						}
					}

					if(! "A".equalsIgnoreCase(detail.getActionType())) {

						remainingQuantity = detail.getRemainingQuantity();
						if(null != remainingQuantity && -1 == remainingQuantity) {
							detail.setErrorDescription(grtConfig.getRVInvalidQty());
						}
						boolean updated = false;
						for(TechnicalOrderDetail originalTDO : actionForm.getMaterialEntryList()) {							
							if(!originalTDO.isExcludedMaestroOrNortel() && originalTDO.getMaterialCode().equalsIgnoreCase(detail.getMaterialCode()) && 
									originalTDO.getSerialNumber().equalsIgnoreCase(detail.getSerialNumber()) && isSameSeid(originalTDO.getSolutionElementId(),detail.getSolutionElementId())) {
								if(null == detail.getErrorDescription() || StringUtils.isEmpty(detail.getErrorDescription())) {
									originalTDO.setNewSerialNumber(detail.getNewSerialNumber());
									if(null != detail.getNewSerialNumber() && StringUtils.isNotEmpty(detail.getNewSerialNumber())) {
										updated = true;
										originalTDO.setSerialNumber(detail.getNewSerialNumber());
									}
									if(null != detail.getRemainingQuantity() && originalTDO.getInitialQuantity() != null && detail.getRemainingQuantity() == originalTDO.getInitialQuantity() ) {
										detail.setErrorDescription(grtConfig.getRevisedQutyEqlExistQtyErrMsg());
									}
									else if(null != detail.getRemainingQuantity() && detail.getRemainingQuantity() != -1) {
										updated = true;
										originalTDO.setRemainingQuantity(detail.getRemainingQuantity());
										//Calculate Removed Quantity
										Long removedQty = Math.abs(originalTDO.getInitialQuantity() - originalTDO.getRemainingQuantity());
										originalTDO.setRemovedQuantity(removedQty);
									}
									originalTDO.setActionType(detail.getActionType());
								}
								if(null != detail.getErrorDescription() && StringUtils.isNotEmpty(detail.getErrorDescription())) {
									updated = true;
									originalTDO.setErrorDescription(detail.getErrorDescription());
									originalTDO.setActionType(detail.getActionType());
									originalTDO.setErrorFlag(true);
								} else {
									originalTDO.setErrorFlag(false);
								}
								break;
							}
						}
						if(updated) {
							totalUpdatedRecoeds++;
						}
					}
				} else {

					String newSerialNumber = detail.getNewSerialNumber();
					initialQuantity = detail.getInitialQuantity();
					remainingQuantity = detail.getRemainingQuantity();

					if("U".equalsIgnoreCase(detail.getActionType())) {						
						
						if(newSerialNumber != null && !newSerialNumber.isEmpty()) {
							snExisting = false;
							snExisting = isSerialNumberExisting(newSerialNumber);
							if(snExisting) {
								detail.setErrorDescription(grtConfig.getRVDuplicateSN());	
							} if(newSerialNumber.length() > 18 || !Pattern.compile(SERIAL_NUMBER_FORMAT).matcher(newSerialNumber).matches()) {
								detail.setErrorDescription(grtConfig.getRVInvalidSN());
							}
							//Check for Duplicate serial number in update records
							if(processedSNs.contains(newSerialNumber)) {
								detail.setErrorDescription(grtConfig.getRVDuplicateSN());
							} else {
								processedSNs.add(newSerialNumber);
							}
							
							//Validate length
							if(newSerialNumber.length() > 18) {
								detail.setErrorDescription(grtConfig.getRVInvalidSN());
							}
						} 
						//If the material is Technically registerable quantity should not be greater than 1
						if(GRTConstants.YES.equalsIgnoreCase(detail.getTechnicallyRegisterable())) {
							if(null != remainingQuantity && remainingQuantity > 1) {
								detail.setErrorDescription(grtConfig.getRevisedQtyErrForTechOnBorded());
							}
						}						
						
					}
					
					//Check for negative quantity 
					
					if("A".equalsIgnoreCase(detail.getActionType()) && (detail.getErrorDescription() == null || detail.getErrorDescription().isEmpty())) {
						if(initialQuantity == null || initialQuantity < 1) {
							detail.setErrorDescription(grtConfig.getRVInvalidQty());
						}
					} 
					if("U".equalsIgnoreCase(detail.getActionType())) {
						
						if(null != remainingQuantity && remainingQuantity < 0) {
							detail.setErrorDescription(grtConfig.getRVInvalidQty());
						}
						boolean updated = false;
						//Find Original Record to be updated						
						for(TechnicalOrderDetail originalTDO : actionForm.getMaterialEntryList()) {
							// To match with Original TOD :: Material code should be same, Serial number should be null or empty, and SEID should be same if exists.
							if(!originalTDO.isExcludedMaestroOrNortel() && originalTDO.getMaterialCode().equalsIgnoreCase(detail.getMaterialCode())  
									&& ( originalTDO.getSerialNumber() == null || originalTDO.getSerialNumber().isEmpty() ) 
									&& isSameSeid(originalTDO.getSolutionElementId(), detail.getSolutionElementId()) ){
								
								if(null == detail.getErrorDescription() || StringUtils.isEmpty(detail.getErrorDescription())) {			
									if(null != detail.getNewSerialNumber() && StringUtils.isNotEmpty(detail.getNewSerialNumber())) {
										originalTDO.setNewSerialNumber(detail.getNewSerialNumber());										
										originalTDO.setSerialNumber(detail.getNewSerialNumber());
										updated = true;
									}																		
									if(null != detail.getRemainingQuantity() && originalTDO.getInitialQuantity() != null 
											&& detail.getRemainingQuantity() != -1
											&& detail.getRemainingQuantity() == originalTDO.getInitialQuantity() ) {
										//If no change in quantity 
										detail.setErrorDescription(grtConfig.getRevisedQutyEqlExistQtyErrMsg());
									}
									else if(null != detail.getRemainingQuantity() && detail.getRemainingQuantity() != -1) {
										originalTDO.setRemainingQuantity(detail.getRemainingQuantity());
										//Calculate Removed Quantity
										Long removedQty = Math.abs(originalTDO.getInitialQuantity() - originalTDO.getRemainingQuantity());
										originalTDO.setRemovedQuantity(removedQty);
										updated = true;
									}
									originalTDO.setActionType(detail.getActionType());
								}  
								if(null != detail.getErrorDescription() && StringUtils.isNotEmpty(detail.getErrorDescription())) {
									originalTDO.setErrorDescription(detail.getErrorDescription());
									originalTDO.setActionType(detail.getActionType());
									originalTDO.setErrorFlag(true);
									updated = true;
								} else {
									originalTDO.setErrorFlag(false);
								}
								break;
							}
							
						}	
						if(updated) {
							totalUpdatedRecoeds++;
						}
					}	
				}
				if(null != detail.getErrorDescription() && StringUtils.isNotEmpty(detail.getErrorDescription())) {
					detail.setErrorFlag(true);
				} else {
					detail.setErrorFlag(false);
				}
				if( !"A".equalsIgnoreCase(detail.getActionType()) && (null == detail.getErrorDescription() || StringUtils.isEmpty(detail.getErrorDescription()))) {
					todList.add(detail);
				} else if(isProcessed && "A".equalsIgnoreCase(detail.getActionType())){
					actionForm.getManuallyAddedMaterialEntryList().add(detail);
				} else if(! isProcessed && "A".equalsIgnoreCase(detail.getActionType()) && (null != detail.getErrorDescription() || StringUtils.isNotEmpty(detail.getErrorDescription()))){
					todList.add(detail);
				}
			}
			logger.debug("Exiting RecordValidationAction.validateImportedData(List<TechnicalOrderDetail> result)");
		}
		
		if(totalUpdatedRecoeds < totalRecoedsToUpdate) {
			actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("noRowsFoundForUpdateErrMsg") + "###" + grtConfig.getNoRowsFoundForUpdateErrMsg());
		}
		// Mare sure serial number validation rules are same, as it exists on install base creation screen.
		// If Material code entered is PLDS/KMAT/ defective or blue material code, show warning on UI and do not import such row.
		return todList;
	}
		
	/**
	 * Method to check the seid
	 * @param originalSeid
	 * @param importedSeid
	 * @return
	 */
	private boolean isSameSeid(String originalSeid, String importedSeid) {
		
		if(null == originalSeid) {
			originalSeid = "";
		}
		if(null == importedSeid) {
			importedSeid = "";
		}
		if(originalSeid.equalsIgnoreCase(importedSeid)) {
			return true;
		} 
		
		return false;
	}
	
	/**
	 * validate record validation details save in grt and submit to sap.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveEquipmentRemovalProcess() throws Exception {
		logger.debug("Entering RecordValidationAction.saveEquipmentRemovalProcess()");
		//Clear All error messages
		actionForm.getErrorMessageMap().clear();
		actionForm.setUploadMessage(null);
				
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		String forwardName = "success";
		SiteRegistration siteRegistration = null;
		// User comes from Home screen->EQR only
		actionForm.getMaterialEntryList();
		actionForm.setEqrFileDownloaded(false);
		actionForm.setEqrFileTemplateDownloaded(false);
		if(actionForm.getSaveSiteReg()){
			siteRegistration = getInstallBaseService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
			if(siteRegistration != null && siteRegistration.getRegistrationId() != null){
				Status status = new Status();
				if(actionForm.getSaveType().equalsIgnoreCase(GRTConstants.VALIDATE)) {
					status.setStatusId(StatusEnum.VALIDATED.getStatusId());
					siteRegistration.setFinalValidationStatus(status);
					getEqrService().updateSiteRegistrationProcessStepAndStatus(actionForm.getRegistrationId(), ProcessStepEnum.FINAL_RECORD_VALIDATION, StatusEnum.VALIDATED);
				} else {
					status.setStatusId(StatusEnum.SAVED.getStatusId());
					siteRegistration.setFinalValidationStatus(status);
				}
			} else {
				siteRegistration = constructSiteRegistration( actionForm, RegistrationTypeEnum.EQUIPMENTREMOVALONLY, null);
				// Saving Site Registration for Save/Submit
				getInstallBaseService().saveSiteRegistrationDetails(siteRegistration);
			}
		} else {
			siteRegistration = getInstallBaseService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
		}
		// Forming the selected equipment list for removal
		List<TechnicalOrderDetail> selectedEquipments = new ArrayList<TechnicalOrderDetail>();
		if(StringUtils.isNotEmpty(actionForm.getSaveType()) && !actionForm.getSaveType().equalsIgnoreCase(GRTConstants.VALIDATE)) {
			
			//START :: Code for adding the Manually Added Material List to Material Entry List
			if(null != actionForm.getManuallyAddedMaterialEntryList() && actionForm.getManuallyAddedMaterialEntryList().size() > 0) {
				List<TechnicalOrderDetail> result = new ArrayList<TechnicalOrderDetail>();
				for(TechnicalOrderDetail techOrderDetail : actionForm.getManuallyAddedMaterialEntryList()) {
					if(null != techOrderDetail.getDeleted() && techOrderDetail.getDeleted()) {
						result.add(techOrderDetail);
					}
				}
				if(result.size() > 0) {
					for (TechnicalOrderDetail tod : result) {
						if(tod.getRemainingQuantity() != null && GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(tod.getActionType())) {
							//As there is no Initial Quantity, get the remaining quantity and set it as initial Quantity.
							tod.setInitialQuantity(tod.getRemainingQuantity());
						}
					}
					isProcessed = false;
					result = validateImportedData(result);
					//Clear Upload Error Message if Any
					actionForm.setUploadMessage(null);
					result = validateMaterialCodes(result);
				}
				if(result.size() > 0) {
					actionForm.getMaterialEntryList().addAll (0, result);
				}
				actionForm.getManuallyAddedMaterialEntryList().clear();
			}
			//END :: Code for adding the Manually Added Material List to Material Entry List
			
			for(TechnicalOrderDetail technicalOrderDetail : actionForm.getMaterialEntryList()){
				if(technicalOrderDetail.getDeleted() != null && technicalOrderDetail.getDeleted()){
					//Code for setting the ActionType for existing records modified on screen
					if(null == technicalOrderDetail.getActionType() || StringUtils.isEmpty(technicalOrderDetail.getActionType()) || 
						! GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(technicalOrderDetail.getActionType())) {
						if(null != technicalOrderDetail.getSerialNumber()) {
							if(! technicalOrderDetail.getSerialNumber().equalsIgnoreCase(technicalOrderDetail.getOriginalSerialNumber())) {
								technicalOrderDetail.setActionType(GRTConstants.ACTION_TYPE_S);
								//If Revised quantity is null, set initial quantity as revised quantity in case of serial number update
								if(technicalOrderDetail.getRemainingQuantity() == null) {
									if(null != technicalOrderDetail.getInitialQuantity()) {
										technicalOrderDetail.setRemainingQuantity(technicalOrderDetail.getInitialQuantity());
										//Calculate removed quantity and set 
										technicalOrderDetail.setRemovedQuantity(Math.abs(technicalOrderDetail.getRemainingQuantity() - technicalOrderDetail.getInitialQuantity() ));
									}
									
								}
							}
						} 
						if(null != technicalOrderDetail.getInitialQuantity()) {
							if(! technicalOrderDetail.getInitialQuantity().equals(technicalOrderDetail.getRemainingQuantity())) {
								technicalOrderDetail.setActionType(GRTConstants.ACTION_TYPE_U);
							}
						}
					}
					selectedEquipments.add(constructTechnicalOrderDetailClone(technicalOrderDetail));
				} else {
					technicalOrderDetail.setDeleted(false);
					if(null != technicalOrderDetail.getSerialNumber()) {
						if(! technicalOrderDetail.getSerialNumber().equalsIgnoreCase(technicalOrderDetail.getOriginalSerialNumber())) {
							String serialNumber = technicalOrderDetail.getOriginalSerialNumber();
							technicalOrderDetail.setSerialNumber(serialNumber);
						}
					} 
					technicalOrderDetail.setRemainingQuantity(null);
					technicalOrderDetail.setRemovedQuantity(null);
				}
			}
		} else if(StringUtils.isNotEmpty(actionForm.getSaveType()) && actionForm.getSaveType().equalsIgnoreCase(GRTConstants.VALIDATE)){
			for(TechnicalOrderDetail technicalOrderDetail : actionForm.getMaterialEntryList()){
				technicalOrderDetail.setDeleted(false);
			}
		}

		// Calling SAPClient for Equipment Removal - Get called when User clicks on submit
		String returnCode = null;
		String message = null;
		try{
			if(actionForm.getSaveType().equalsIgnoreCase(GRTConstants.VALIDATE)) {
				 getEqrService().deleteTechnicalOrderForRegId(actionForm.getRegistrationId(), ProcessStepEnum.FINAL_RECORD_VALIDATION);
				 returnCode = getEqrService().saveSelectedEquipmentsForEQR(actionForm.getRegistrationId(), actionForm.getSoldToId(), actionForm.getMaterialEntryList(), this.getUserFromSession().getUserId(), GRTConstants.TECH_ORDER_TYPE_FV);
				 if(returnCode.equalsIgnoreCase(GRTConstants.isSubmitted_false)){					 
					 message = grtConfig.getEwiMessageCodeMap().get("validateErrorMsg") + "###" + grtConfig.getValidateErrorMsg();
					 returnCode = GRTConstants.EQR_validation_errorCode;
					 forwardName = "failure";
				 } else {
					//Defect#876 updating EQRCompleteDate for validation completion
					 getEqrService().updateSiteRegistrationCompleteDateForValidate(actionForm.getRegistrationId(), ProcessStepEnum.FINAL_RECORD_VALIDATION, StatusEnum.VALIDATED);
					 //Code for sending the email.
					 baseRegistrationService.sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.FINAL_RECORD_VALIDATION, StatusEnum.VALIDATED);
					 message = grtConfig.getEwiMessageCodeMap().get("validateSuccessMsg") + "###" + grtConfig.getValidateSuccessMsg();
					 forwardName = "failure";
					 returnCode = GRTConstants.EQR_validation_successCode;
				 }
			}
			else if(selectedEquipments != null && selectedEquipments.size() > 0){
				logger.debug("In RegistrationController : saveEquipmentRemovalProcess: "+selectedEquipments.size());
				logger.debug("EQR Status :"+actionForm.getStatus());
				 /** 
				  * Deleting all the Technical Orders (saved on click of SAVE) before Submit/Save EQR
				  */
				 if((actionForm.getStatus() != null && actionForm.getStatus().equalsIgnoreCase(StatusEnum.SAVED.getStatusShortDescription()))
						 || (StringUtils.isNotEmpty(actionForm.getSaveType()) 
								 && (actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SAVE) || actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SUBMIT)))) {
					 getEqrService().deleteTechnicalOrderForRegId(actionForm.getRegistrationId(), ProcessStepEnum.FINAL_RECORD_VALIDATION);
					//populate filters
					toRegistrationList();
				 }
				 if(StringUtils.isNotEmpty(actionForm.getSaveType()) && actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SAVE)) {
					 // On EQR Save will save all the records selected for removal in the Technical Order table
					 returnCode = getEqrService().saveSelectedEquipmentsForEQR(actionForm.getRegistrationId(), actionForm.getSoldToId(), actionForm.getMaterialEntryList(), this.getUserFromSession().getUserId(), GRTConstants.TECH_ORDER_TYPE_FV);
					 if(returnCode.equalsIgnoreCase(GRTConstants.isSubmitted_false)){
						 message = grtConfig.getEwiMessageCodeMap().get("saveErrorMsg") + "###" + grtConfig.getSaveErrorMsg();
						 forwardName = "failure";
					 }
				 } else if(StringUtils.isNotEmpty(actionForm.getSaveType()) && actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SUBMIT) ){
					// Fetching Equipment data from Siebel
					 if(!actionForm.getSaveSiteReg()){
						 String soldToId = null;
						 if (actionForm.getSoldToId() != null) {
							soldToId = actionForm.getSoldToId();
						 } else if (getRequest().getParameter("soldToId") != null) {
							soldToId = getRequest().getParameter("soldToId");
						 }
						 logger.debug("SoldTo:  "+soldToId);
						 List<TechnicalOrderDetail> technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, true);
						 actionForm.setMaterialsAfterExclusionList(technicalOrderDetailList);
					 }
					String generateAttachmentInPath = autoGenAttachmentPath(EQUIPMENT_REMOVAL_ATTACHMENT_NAME);
					
					//Added for security defect # 229
					boolean quantCheck = true;
					for(TechnicalOrderDetail technicalOrderDetail : actionForm.getMaterialEntryList()){
						if(technicalOrderDetail.getRemainingQuantity()!=null && (technicalOrderDetail.getRemainingQuantity() < 0)){
							quantCheck = false;
							returnCode = "error";
							message = grtConfig.getEwiMessageCodeMap().get("revisedQtyNoNegativeErrMsg") + "###" + grtConfig.getRevisedQtyNoNegativeErrMsg();
							forwardName="failure";
						}
					}//Added for security defect # 229
					if(quantCheck){				
				    returnCode = getEqrService().equipmentRemoval(siteRegistration, actionForm.getRegistrationId(), actionForm.getSoldToId(),actionForm.getMaterialsAfterExclusionList(), actionForm.getMaterialEntryList(), this.getUserFromSession().getUserId(), generateAttachmentInPath);
					logger.debug("Return Code:   "+returnCode);
					if(returnCode.equals(GRTConstants.EQR_successCode)){
						message = GRTConstants.EQR_successCode;
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.EQR_errorCode)){
						message = grtConfig.getEwiMessageCodeMap().get("errorWhileProcessingErrMsg") + "###" + grtConfig.getErrorWhileProcessingErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.sapDown_errorCode)){
						message = grtConfig.getEwiMessageCodeMap().get("sapDownErrMsg") + "###" + grtConfig.getSapDownErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.fmw_errorCode)){
						message = GRTConstants.fmw_errorCode;
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.exception_errorcode)){
						message = grtConfig.getEwiMessageCodeMap().get("processingFailedErrMsg") + "###" + grtConfig.getProcessingFailedErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.sapdestination_notfound)){						
						message = grtConfig.getEwiMessageCodeMap().get("unableToMapSoldToErrMsg") + "###" + grtConfig.getUnableToMapSoldToErrMsg().replace(":soldToId", actionForm.getSoldToId());
						forwardName = "failure";
					} else {
						message = grtConfig.getEwiMessageCodeMap().get("errorWhileProcessingErrMsg") + "###" + grtConfig.getErrorWhileProcessingErrMsg();
						forwardName = "failure";
					}
					}
				}
			} else {
				message = grtConfig.getEwiMessageCodeMap().get("emptyMaterialListErrorMsg") + "###" + grtConfig.getEmptyMaterialListErrorMsg();
				returnCode = GRTConstants.materialEmptyListRC;
				forwardName = "failure";
			}
			actionForm.setMessage(message);
			actionForm.setReturnCode(returnCode);
			logger.debug("Exiting RegistrationController : saveEquipmentRemovalProcess" + returnCode);
			
			//=========================Banner Data Changes Start====================================
			
			
				
				RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
				actionForm.setBannerStatus(summary.getFinalValidationStatus());
				actionForm.setBannerSubStatus(summary.getFinalValidationSubStatus());
				actionForm.setBannerSrNumber(summary.getFinalValidationSrNo());
				actionForm.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
				actionForm.setCompany(summary.getRequestingCompany());
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
				if(summary.getEqrSubmittedDate() != null){
					actionForm.setBannerSubmittedDate(sdf.format(summary.getEqrSubmittedDate()));
				} else {
					actionForm.setBannerSubmittedDate("");
				}
				if(summary.getEqrCompletedDate() != null){
					actionForm.setBannerCompletedDate(sdf.format(summary.getEqrCompletedDate()));
				} else {
					actionForm.setBannerCompletedDate("");
				}
			
			
			//========================End=========================================================
			
			return forwardName;
		} catch(Exception e){
			logger.debug("Error happened while calling Submit Equipment Removal" + e.getMessage());
			logger.error("Error in submit",e);
			logger.debug("Error in submit" +e.getMessage());
			e.printStackTrace();
			message = grtConfig.getEwiMessageCodeMap().get("processingFailedErrMsg") + "###" + grtConfig.getProcessingFailedErrMsg();
			actionForm.setMessage(message);
			getEqrService().updateSiteRegistrationSubmittedFlag(actionForm.getRegistrationId(), GRTConstants.isSubmitted_false);
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Exiting Save Equipment Removal in RegistrationController Due to an Error");
	        logger.debug("TIMER:" + (c2-c1) +" milliseconds");
	        return "failure";
		} finally {
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("TIMER for SAVE/SUBMIT Equipment removal for RegID:" +actionForm.getRegistrationId()+" time in milliseconds:"+ (c2-c1));
			logger.debug("Exiting RecordValidationAction.saveEquipmentRemovalProcess()");
		}
	}
	
	/**
	 * populate registration list filters
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toRegistrationList() throws Exception {
		logger.debug("Entering and Exiting registrationList : Forwarding flow to /pages/registrationList/registrationList.jsp");
		validateSession();
		actionForm.setErrorMessage(null);
		actionForm.setReturnCode(null);
		actionForm.setNavigateTo(GRTConstants.REGISTRATION_LIST_PAGE);
		CSSPortalUser user = getUserFromSession();
		boolean ipoFilter = false;
		try {
			requesterName = user.getFirstName()+" "+user.getLastName();
			if(null!=prFlag && prFlag.equals("PR")){
				ipoFilter=true;
			}
			//Get the lists for registration list page multiselect
			statusList = getBaseRegistrationService().getRegistrationStatus();
			regTypeList = getBaseRegistrationService().getRegistrationTypes();
			//Prepare JSON for UI functionality
			regListMap = new HashMap<String, Object>();
			regListMap.put("statusList", statusList);
			regListMap.put("registrationTypeList", regTypeList);
		} catch (Exception e) {
			logger.debug("Error in registrationList : " + e.getMessage());
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}

		return Action.SUCCESS;
	}
	
	/**
	 * method to populate only manually added entries.
	 * 
	 * @param details
	 * @return
	 */
	public List<TechnicalOrderDetail> removeUpdateRecords(List<TechnicalOrderDetail> details) {
		List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
		for(TechnicalOrderDetail originalTDO : details) {
			if("A".equalsIgnoreCase(originalTDO.getActionType())) {
				todList.add(originalTDO);
			}
		}
		return todList;
	}
	
	/*Back Button Change Defect 253*/
	public String backFromRecordValidation () throws Exception{
		validateSession();
		return Action.SUCCESS;
	}
	/*Back Button Change Defect 253*/

	@Override
	public String technicalRegistrationDashboard() {
		return null;
	}

	@Override
	public String installBaseCreation() {
		return null;
	}
	
	private boolean isSerialNumberExisting(String serialNumber) {
		for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
			if(serialNumber.equalsIgnoreCase(materialEntryList.getSerialNumber())) {
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Method to check for duplicate serial number
	 * 
	 * @param serialNumber
	 * @return
	 */
	private boolean isSerialNumberExistingForUpdateAssets(String serialNumber) {
		int count = 0;
		for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
			if(serialNumber.equalsIgnoreCase(materialEntryList.getSerialNumber())) {
				count ++;
			}
		}
		if(count > 1) {
			return true;
		} else { 
			return false;
		}
	}
	
	/**
	 * Check for existing material code	 * 
	 * @param materialCode
	 * @return
	 */
	private boolean isMaterialCodeExisting(String materialCode) {
		for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
			if(materialEntryList.getMaterialCode().equalsIgnoreCase(materialCode)) {
				return true;
			}
		} 
		return false;
	}
	
	
	
	/** code to populate error message
	 * @param errorMessage
	 * @param materialCode
	 */
	private void populateErrorMessages(String errorMessage, String materialCode){
		if(null != actionForm.getErrorMessageMap().get(errorMessage)) {
			List<String> mcList = actionForm.getErrorMessageMap().get(errorMessage);
			mcList.add(materialCode);
		} else {
			List<String> mcList = new ArrayList<String>();
			mcList.add(materialCode);
			actionForm.getErrorMessageMap().put(errorMessage, mcList);
		}
	}
	
	/**
	 * check for maestro / nortel from sapox I or B
	 * @param materialCode
	 * @return
	 */
	private boolean isMCMaestroOrNortelAndNotFromI(String materialCode) {
		try{
		for(TechnicalOrderDetail materialEntryList : actionForm.getMaterialEntryList()) {
			if(!StringUtils.isEmpty(materialEntryList.getIsMaestro()) || !StringUtils.isEmpty(materialEntryList.getIsNortel()))
			{
				if(!getBaseRegistrationService().isIorBSoldToSAPMapping(actionForm.getSoldToId()))
				{
					if(GRTConstants.YES.equalsIgnoreCase(materialEntryList.getActiveContractExist())) {
						return true; 
					}					
				}
			}
		} 
		}catch(Exception e){
			logger.error("Error while checking for isMCMaestroOrNortelAndNotFromI - "+e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public String salGatewayMigrationList() {
		return null;
	}
}
