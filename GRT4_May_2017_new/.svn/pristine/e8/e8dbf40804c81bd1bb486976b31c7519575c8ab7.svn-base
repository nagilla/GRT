package com.avaya.grt.web.action.equipmentmove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.service.equipmentmove.EQMService;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.Account;
import com.grt.dto.MaterialCodeListDto;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.GenericSort;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;
import com.opensymphony.xwork2.Action;

public class EquipmentMoveAction extends TechnicalRegistrationAction {

	private static final Logger logger = Logger
			.getLogger(EquipmentMoveAction.class);
	
	private static final String EQUIPMENT_MOVE_ATTACHMENT_NAME = "EquipmentMoveAttachment";
		
	private static final String DATE_FORMAT1 = "MM/dd/yyyy HH:mm:ss";
	
	private EQMService eqmService;
	
	private String dataError = "";
	
	private InputStream inputStream;
		
	protected List<MaterialCodeListDto> materialCodeList = new ArrayList<MaterialCodeListDto>();
	
	private Map<String, Object> jsonMap = new HashMap<String, Object>();
	
	/**
	 * Getter method for eqmService
	 * @return eqmService
	 */
	public EQMService getEqmService() {
		return eqmService;
	}

	/**
	 * setter method for eqmService
	 * @param eqmService
	 */
	public void setEqmService(EQMService eqmService) {
		this.eqmService = eqmService;
	}
	
	/**
	 * Getter method for materialCodeList
	 * @return materialCodeList
	 */
	public List<MaterialCodeListDto> getMaterialCodeList() {
		return materialCodeList;
	}

	/**
	 * Setter method for materialCodeList
	 * @param materialCodeList
	 */
	public void setMaterialCodeList(List<MaterialCodeListDto> materialCodeList) {
		this.materialCodeList = materialCodeList;
	}
	private List<String> errors = new ArrayList<String>();
	
	/**
	 * executes this method on click of equipment move link on home page
	 * @return success
	 * @throws Exception
	 */
	public String equipmentMoveOnly() throws Exception {
		logger.debug("Entering EquipmentMoveAction.equipmentMove()");
		validateSession();
		actionForm.setEquipmentMoveOnly(true); //set the equipment move flag
		
		actionForm.setTechnicalRegistrationOnly(false); //TOB
		actionForm.setFirstColumnOnly(false); //TOB
		actionForm.setInstallbaseRegistrationOnly(false); //IB
		actionForm.setViewInstallbaseOnly(false); //View IB
		actionForm.setRecordValidationOnly(false); //Record validation
		logger.debug("Exiting EquipmentMoveAction.equipmentMove()");
		return Action.SUCCESS;
	}

	/**
	 * Check whether the from and to sold to's belong to same country or not.
	 * @return String
	 * 
	 */
	public String isSameCountry(){
		boolean result = false;
		actionForm.setAccount(null);
		Account account = null;
		String respMsg="Ok";
		CSSPortalUser user = getUserFromSession();
		if(!isFromValid()){
			respMsg="fromError";			
		}else if(!isToValid()){
			respMsg="toError";
		}else{
		//Validate Sold to Conditions-Start-Defect-80
		if(!( actionForm.getEqmFromSoldTo().equalsIgnoreCase("Null") ||  actionForm.getEqmFromSoldTo().equalsIgnoreCase(""))){
			try {
				account = getSiebelClient().queryAccount(  actionForm.getEqmFromSoldTo() );
				if(account==null)
					respMsg="From";
				if(!( actionForm.getEqmToSoldTo().equalsIgnoreCase("Null") ||  actionForm.getEqmToSoldTo().equalsIgnoreCase("")))
				{
					if(account!=null)
					{
						account = null;
						account = getSiebelClient().queryAccount(  actionForm.getEqmToSoldTo() );
						if(account==null)
							respMsg="To";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//Validate Sold to Conditions-End
		
		//Authorized Sold to Conditions-Start-Defect-82,84
		try {
			if (account != null && !this.getBaseRegistrationService().isSoldToValidForCurrentUser(actionForm.getEqmFromSoldTo(), user.getUserId(), user.getBpLinkId()) && !user.getUserType().equalsIgnoreCase("A")) {
				respMsg="FromAuth";
				account = null;
			}
			if (account != null && !this.getBaseRegistrationService().isSoldToValidForCurrentUser(actionForm.getEqmToSoldTo(), user.getUserId(), user.getBpLinkId()) && !user.getUserType().equalsIgnoreCase("A")) {
				respMsg="ToAuth";
				account = null;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		//Authorized Sold to Conditions-End
		
		if( account !=null ){
			result = getEqmService().isSameCountry(  actionForm.getEqmFromSoldTo(),  actionForm.getEqmToSoldTo());
			if(result==false)
				respMsg="FromTo";
		}
		
		//Added for defect 107
		
		if(actionForm.getEqmFromSoldTo().equalsIgnoreCase("")){
			respMsg="From";
		}
		if(actionForm.getEqmToSoldTo().equalsIgnoreCase("")){
			respMsg="To";
		}
		}
		
		//Added for defect 107
		jsonMap.put("isSameCountry", respMsg);
		
		return Action.SUCCESS;
	}
	
	/**
	 * method to save site registration details and fecth equipment details from siebel.
	 * @return String
	 * 
	 */
	public String saveSiteRegistration() throws Exception {
		logger.debug("Entering EquipmentMoveAction.saveSiteRegistration()");
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
		
		logger.debug("................ Starting for equipmentMoveProcess records fetching ................");
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
				logger.debug("equipmentMoveProcess MaterialList after MaterialExclusion:  size: " + technicalOrderDetailList.size());
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
					logger.debug("Size in EquipmentMoveAction "+technicalOrderDetailList.size());
				} else {
					// Fetching the saved EQM records from technical order
					technicalOrderDetailList = getInstallBaseService().getTechnicalOrderByType(actionForm.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_EM);					
					RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
					actionForm.setBannerStatus(summary.getEqrMoveStatus());
					actionForm.setBannerSubStatus(summary.getEqrMoveSubStatus());
					actionForm.setBannerSrNumber(summary.getEqrMoveSrNo());
					actionForm.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
					actionForm.setCompany(summary.getRequestingCompany());
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
					if(summary.getEqrMoveSubmittedDate() != null){
						actionForm.setBannerSubmittedDate(sdf.format(summary.getEqrMoveSubmittedDate()));
					} else {
						actionForm.setBannerSubmittedDate("");
					}
					if(summary.getEqrMoveCompletedDate() != null){
						actionForm.setBannerCompletedDate(sdf.format(summary.getEqrMoveCompletedDate()));
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
				List<PipelineSapTransactions> pipelineList = getEqmService().getConsolidatedPipelineRecords(soldToId);
				if(pipelineList != null && pipelineList.size() > 0){
					logger.debug("Consolidated pipeline List size:"+pipelineList.size());
					for(TechnicalOrderDetail techDto : technicalOrderDetailList){
						for(PipelineSapTransactions pipelineDto : pipelineList){
							if(techDto.getMaterialCode().equalsIgnoreCase(pipelineDto.getMaterialCode())){
								if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV)){
									techDto.setPipelineEQRQuantity(pipelineDto.getQuantity());
								} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
									techDto.setPipelineIBQuantity(pipelineDto.getQuantity());
								} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM)){
									techDto.setPipelineEQRMoveQuantity(pipelineDto.getQuantity());
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
				if (summary.getEqrMoveStatusId() !=null
						&& !summary.getEqrMoveStatusId().equalsIgnoreCase(GRTConstants.AWAITNG_INFORMATION)) {
					for (TechnicalOrderDetail detail : technicalOrderDetailList) {
						if(StringUtils.isEmpty(detail.getExclusionSource())){
							detail.setErrorDescription("");
						}
					}
				}
				try {
					logger.debug("EquipmentMoveProcess MaterialList after exclusion:  size: " + technicalOrderDetailList.size());
					GenericSort gs = new GenericSort("productLine", true);
					Collections.sort(technicalOrderDetailList, gs);
				}catch(Exception e){
					logger.error("Exception in Equipment Move while sorting data : "+e.getMessage());

				}
				for(TechnicalOrderDetail Td:technicalOrderDetailList)
				{
					if(Td.getActiveContractExist()==null || Td.getActiveContractExist().isEmpty())
						Td.setActiveContractExist("No");
					if(Td.getTechnicallyRegisterable().isEmpty() || Td.getTechnicallyRegisterable()==null)
						Td.setTechnicallyRegisterable("No");
					//GRT 4.0 : Defect#280 Disable Vsalgw Records (Updated the condition for Defect#408)
					if(GRTConstants.SAL_VIRTUAL_GATEWAY.equalsIgnoreCase(Td.getSolutionElementCode()) && Td.isSalGateway()){
						Td.setErrorDescription(grtConfig.getVsalgwErrMsg());
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
				}
				
				actionForm.setMaterialEntryList(technicalOrderDetailList);
			}
			getRequest().setAttribute("materialList", actionForm.getMaterialEntryList());
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("................ Ending for equipmentMoveProcess records fetching ................");
			
			logger.debug("TIMER for Equipment Move rendering for RegID:"+actionForm.getRegistrationId()+" time in milliseconds :" + (c2-c1));
			
			if("DataAccessException".equals(cause)){
				this.setDataError("Transaction timed out while fetching the data from Siebel");
			}else{
				this.setDataError("No Results Found.");
			}
		} catch(DataAccessException daexception){
			logger.error("Exception in EquipmentMoveAction.saveSiteRegistration() : ", daexception);
			throw new DataAccessException(EquipmentMoveAction.class, daexception.getMessage(), daexception);
		} catch (Exception e) {
			logger.error("Exception in EquipmentMoveAction.saveSiteRegistration() : ", e);
		}
		logger.debug("Exiting EquipmentMoveAction.saveSiteRegistration()");
		return Action.SUCCESS;
	}

	/**
	 * method to export record to excel
	 * @return
	 * @throws Exception
	 */
	public String exportEquipmentMoveProcess() throws Exception {
		logger.debug("Entering EquipmentMoveAction.exportEquipmentMoveProcess()");

		validateSession();
		String filename = "equipment_move_"+actionForm.getRegistrationId()+".xls";
		String filepath = "/tmp/"+filename;
		//String filepath = filename;
		
		TechnicalOrderDetailWorsheetProcessor workSheetProcessor = new TechnicalOrderDetailWorsheetProcessor();
		workSheetProcessor.generateEquipmentMoveWorksheet(actionForm.getMaterialEntryList(), filepath);
		actionForm.setEqrMoveFileDownloaded(true);

		logger.debug("Exiting EquipmentMoveAction.exportEquipmentMoveProcess()");
		return Action.SUCCESS;
	}
	
	/**
	 * method to save equipment move process.
	 * @return
	 * @throws Exception
	 */
	public String saveEquipmentMoveProcess() throws Exception {
		logger.debug("Exiting EquipmentMoveAction.saveEquipmentMoveProcess()");
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		String forwardName = "success";
		SiteRegistration siteRegistration = null;
		// User comes from Home screen->EQM only
		actionForm.setEqrMoveFileDownloaded(false);
		if(actionForm.getSaveSiteReg()){
			siteRegistration = getInstallBaseService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
			if(siteRegistration != null && siteRegistration.getRegistrationId() != null){
				Status status = new Status();
				if(actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SAVE)) {					
					status.setStatusId(StatusEnum.SAVED.getStatusId());
					siteRegistration.setEqrMoveStatus(status);
					getEqmService().updateSiteRegistrationProcessStepAndStatus(actionForm.getRegistrationId(), ProcessStepEnum.EQUIPMENT_MOVE, StatusEnum.SAVED);
				}
			} else {
				siteRegistration = constructSiteRegistration( actionForm, RegistrationTypeEnum.EQUIPMENTMOVEONLY, null);
				// Saving Site Registration for Save/Submit
				getInstallBaseService().saveSiteRegistrationDetails(siteRegistration);
			}
		} else {
			siteRegistration = getInstallBaseService().getSiteRegistrationOnRegId(actionForm.getRegistrationId());
		}
		
		// Forming the selected equipment list for removal
		List<TechnicalOrderDetail> selectedEquipments = new ArrayList<TechnicalOrderDetail>();
		for(TechnicalOrderDetail technicalOrderDetail : actionForm.getMaterialEntryList()){
			if(technicalOrderDetail.getDeleted() != null && technicalOrderDetail.getDeleted()){
				selectedEquipments.add(constructTechnicalOrderDetailClone(technicalOrderDetail));
			} else {
				technicalOrderDetail.setDeleted(false);
			}
		}
		
		// Calling SAPClient for Equipment Move - Get called when User clicks on submit
		String returnCode = null;
		String message = null;
		String regId = null;
		boolean srFoundFlag = false;
		for(TechnicalOrderDetail td:selectedEquipments){
			//returnCode = getTechnicalOnBoardingService().checkExistSRAndEntitlement(actionForm.getSoldToId(),td.getSolutionElementId(),null);
			//Defect #913 : Post production deferred defect 
			//When user attempts Equipment Move for a device that has an open Step B SR against it along with registration id
			Map<String, String> respMap = getTechnicalOnBoardingService().checkExistSRAndEntlForRetest(actionForm.getSoldToId(),td.getSolutionElementId(),null);
			returnCode = respMap.get(GRTConstants.RESPONSE);
			
			if(!returnCode.equalsIgnoreCase(GRTConstants.SUCCESS))
				srFoundFlag = true;   
			
			if( regId == null ){
				regId = "<br><br>"+td.getSolutionElementId()+", "+GRTConstants.REGIDNUM+respMap.get(GRTConstants.REGID);
			}else if( respMap.get(GRTConstants.REGID)!=null && !respMap.get(GRTConstants.REGID).isEmpty() ){
				regId += "<br>"+td.getSolutionElementId()+", "+GRTConstants.REGIDNUM+respMap.get(GRTConstants.REGID);
			}
		}
		//If SR is present in any of the selected record show error message
		if( srFoundFlag ){
			returnCode = GRTConstants.FAILURE;
		}
		try{
			if(selectedEquipments != null && selectedEquipments.size() > 0 && returnCode.equalsIgnoreCase(GRTConstants.SUCCESS)){
				logger.debug("In EquipmentMoveAction : saveEquipmentMoveProcess: "+selectedEquipments.size());
				logger.debug("EQR Move Status :"+actionForm.getStatus());
				 /** 
				  * Deleting all the Technical Orders (saved on click of SAVE) before Submit/Save EQR Move
				  */
				 if((actionForm.getStatus() != null && actionForm.getStatus().equalsIgnoreCase(StatusEnum.SAVED.getStatusShortDescription()))
						 || (StringUtils.isNotEmpty(actionForm.getSaveType()) 
								 && (actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SAVE) || actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SUBMIT)))) {
					 getEqmService().deleteTechnicalOrderForRegId(actionForm.getRegistrationId(), ProcessStepEnum.EQUIPMENT_MOVE);
				 }
				 if(StringUtils.isNotEmpty(actionForm.getSaveType()) && actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SAVE)) {
					 // On EQR Move Save will save all the records selected for removal in the Technical Order table
					 returnCode = getEqmService().saveSelectedEquipmentsForEQR(actionForm.getRegistrationId(), actionForm.getSoldToId(), actionForm.getMaterialEntryList(), 
							 this.getUserFromSession().getUserId(), GRTConstants.TECH_ORDER_TYPE_EM);
					 if(returnCode.equalsIgnoreCase(GRTConstants.isSubmitted_false)){
						 message = grtConfig.getEwiMessageCodeMap().get("eqmUnableToSaveErrMsg") + "###" + grtConfig.getEqmUnableToSaveErrMsg();
						 forwardName = "failure";
					 }
				 } else if(StringUtils.isNotEmpty(actionForm.getSaveType()) && actionForm.getSaveType().equalsIgnoreCase(GRTConstants.SUBMIT) ){
					// Fetching Equipment data from Siebel for from soldto
					 if(!actionForm.getSaveSiteReg()){
						 String soldToId = null;
						 if (actionForm.getSoldToId() != null) {
							soldToId = actionForm.getSoldToId();
						 } else if (getRequest().getParameter("soldToId") != null) {
							soldToId = getRequest().getParameter("soldToId");
						 }
						 logger.debug("SoldTo:  "+soldToId);
						 List<TechnicalOrderDetail> technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, false);
						 actionForm.setMaterialsAfterExclusionList(technicalOrderDetailList);
					 }
					String generateAttachmentInPath = autoGenAttachmentPath(EQUIPMENT_MOVE_ATTACHMENT_NAME);
					//Added for security defect # 229
					boolean quantCheck = true;
					for(TechnicalOrderDetail technicalOrderDetail : actionForm.getMaterialEntryList()){
						if(technicalOrderDetail.getRemovedQuantity()!=null && (technicalOrderDetail.getInitialQuantity() < technicalOrderDetail.getRemovedQuantity())){
							quantCheck = false;
							returnCode = "error";
							message = "Quantity to be moved cannot be greater than the Existing Quantity";
							forwardName="failure";
						}
					}//Added for security defect # 229
					if(quantCheck){
					//Actual SAP call
					returnCode = getEqmService().equipmentMove(siteRegistration, actionForm.getRegistrationId(), actionForm.getEqmFromSoldTo(), actionForm.getEqmToSoldTo(),
				    		actionForm.getMaterialsAfterExclusionList(), actionForm.getMaterialEntryList(), this.getUserFromSession().getUserId(), 
				    		generateAttachmentInPath);
					
					//Banner Details
					
					RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
					actionForm.setBannerStatus(summary.getEqrMoveStatus());
					actionForm.setBannerSubStatus(summary.getEqrMoveSubStatus());
					actionForm.setBannerSrNumber(summary.getEqrMoveSrNo());
					actionForm.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
					actionForm.setCompany(summary.getRequestingCompany());
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
					if(summary.getEqrMoveSubmittedDate() != null){
						actionForm.setBannerSubmittedDate(sdf.format(summary.getEqrMoveSubmittedDate()));
					} else {
						actionForm.setBannerSubmittedDate("");
					}
					if(summary.getEqrMoveCompletedDate() != null){
						actionForm.setBannerCompletedDate(sdf.format(summary.getEqrMoveCompletedDate()));
					} else {
						actionForm.setBannerCompletedDate("");
					}
					
					
					logger.debug("Return Code:   "+returnCode);
					if(returnCode.equals(GRTConstants.EQR_MOVE_successCode)){
						message = GRTConstants.EQR_successCode;
						forwardName = "success";
					} else if(returnCode.equals(GRTConstants.EQR_MOVE_errorCode)){
						message = grtConfig.getEwiMessageCodeMap().get("eqmProcessErrMsg") + "###" + grtConfig.getEqmProcessErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.sapDown_errorCode)){
						message = grtConfig.getEwiMessageCodeMap().get("eqmSapDownErrMsg") + "###" + grtConfig.getEqmSapDownErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.fmw_errorCode)){
						message = GRTConstants.fmw_errorCode;
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.exception_errorcode)){
						message = grtConfig.getEwiMessageCodeMap().get("eqmProcessFailErrMsg") + "###" + grtConfig.getEqmProcessFailErrMsg();
						forwardName = "failure";
					} else if(returnCode.equals(GRTConstants.sapdestination_notfound)){
						message = grtConfig.getEwiMessageCodeMap().get("eqmBackendSysErrMsg") + "###" + grtConfig.getEqmBackendSysErrMsg().replace(":soldTo", actionForm.getSoldToId());
						forwardName = "failure";
					} else {
						message = grtConfig.getEwiMessageCodeMap().get("eqmProcessErrMsg") + "###" + grtConfig.getEqmProcessErrMsg();
						forwardName = "failure";
					}
					}
				}
			} else {
				
				if(!returnCode.equalsIgnoreCase(GRTConstants.SUCCESS))
				{
					//message = grtConfig.getEwiMessageCodeMap().get("eqmActTrbTickErrMsg") + "###" + grtConfig.getEqmActTrbTickErrMsg();
					//Show the registration id's for the open sr (defect #913)
					message = grtConfig.getEwiMessageCodeMap().get("eqmActTrbTickErrMsg") + "###" + grtConfig.getEqmActTrbTickErrMsg().replace("<regid>", regId+"<br><br>");
					returnCode = GRTConstants.exception_errorcode;
					
				}else{
					message = grtConfig.getEwiMessageCodeMap().get("eqmMatListEmptyerrMsg") + "###" + grtConfig.getEqmMatListEmptyerrMsg();
					returnCode = GRTConstants.materialEmptyListRC;
				}
				
				forwardName = "failure";
			}
			actionForm.setMessage(message);
			actionForm.setReturnCode(returnCode);
			logger.debug("Exiting EquipmentMoveAction : saveEquipmentMoveProcess " + returnCode);
			return forwardName;
		} catch(Exception e){
			logger.debug("Error happened while calling Submit Equipment Move " + e.getMessage());
			logger.error("Error in submit",e);
			logger.debug("Error in submit" +e.getMessage());
			e.printStackTrace();
			message = grtConfig.getEwiMessageCodeMap().get("eqmfailedWhileProcessErrMsg") + "###" + grtConfig.getEqmfailedWhileProcessErrMsg();
			actionForm.setMessage(message);
			getEqmService().updateSiteRegistrationSubmittedFlag(actionForm.getRegistrationId(), GRTConstants.isSubmitted_false);
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Exiting Save EquipmentMoveAction : saveEquipmentMoveProcess due to an Error");
	        logger.debug("TIMER:" + (c2-c1) +" milliseconds");
	        return "failure";
	        
		} finally {
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("TIMER for SAVE/SUBMIT Equipment Move for RegID:" +actionForm.getRegistrationId()+" time in milliseconds:"+ (c2-c1));
			logger.debug("Exiting EquipmentMoveAction : saveEquipmentMoveProcess()");
		}
	}
	
	
	/**
	 * method to fetch existing StepB Registration By Seid
	 * @param seId
	 * @return
	 * @throws DataAccessException
	 */
	public String getExistingStepBRegBySeid(String seId) throws DataAccessException {
		 logger.debug("Exiting EquipmentMoveAction.getExistingStepBRegBySeid");
		 String returnCde = "";
		 
		 String regId = getEqmService().getExistingStepBRegBySeid(seId);
		 
		 if ( (regId != null) && (!regId.isEmpty()) ) {
			 returnCde = "This equipment is not allowed for Equipment Move as it already has step B SR in process ";
		 }
		 
		 logger.debug("Exiting EquipmentMoveAction.getExistingStepBRegBySeid");
		 
		 return returnCde;
	 }
	
	/**
	 * method to download excel template
	 * @return
	 * @throws FileNotFoundException
	 */
	public String execute() throws FileNotFoundException {
		try{
		logger.debug("Execute method called>>>>>>>>>>>>>>>>>>");
		HttpServletRequest request = ServletActionContext.getRequest();
	    
		HttpSession session=request.getSession(true);
		String filePath=(String) session.getAttribute("downloadFilePath");
	    
		logger.debug("Session File download path is:>>>>>>>"+filePath);
		
	    File f=new File(filePath);
	    logger.debug("Session File download path is Exits:>>>>>>>"+f.exists());
	    logger.debug(f.exists());
	    inputStream = new FileInputStream(f);
	    
	    logger.debug("inputStream Deails:>>>>>>>"+inputStream);
		}catch(Exception Ex){
			logger.error("File Download Error:>>>>>>", Ex);
		}
	    return Action.SUCCESS;
	}
	
	/**
	 * method to check from soldTo
	 * @return
	 */
	public boolean isFromValid() {
		if (actionForm.getEqmFromSoldTo() != null && actionForm.getEqmFromSoldTo().length()>10) {
			logger.info("From SoldTo :: "+actionForm.getEqmFromSoldTo());
			logger.debug("From SoldTo :: "+actionForm.getEqmFromSoldTo());
			addError("From SoldTo can not be more than 2800 characters.");
			return false;
		}
		return true;
	}
	
	/**
	 * method to check to solodTo Valid
	 * @return
	 */
	public boolean isToValid() {		
		if (actionForm.getEqmToSoldTo() != null && actionForm.getEqmToSoldTo().length()>10) {
			logger.info("To SoldTo :: "+actionForm.getEqmToSoldTo());
			logger.debug("To SoldTo :: "+actionForm.getEqmToSoldTo());
			addError("To SoldTo can not be more than 2800 characters.");
			return false;
		}
		return true;
	}
	
	@Override
	public String technicalRegistrationDashboard() {
		return null;
	}

	@Override
	public String installBaseCreation() {
		return null;
	}
	

	/**
	 * Getter method for dataError
	 * @return
	 */
	public String getDataError() {
		return dataError;
	}

	/** setter method for dataError
	 * @param dataError
	 */
	public void setDataError(String dataError) {
		this.dataError = dataError;
	}

	/**
	 * getter method for jsonMap
	 * @return
	 */
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	/**
	 * Setter method for jsonMap
	 * @param jsonMap
	 */
	public void setJsonMap(Map<String, Object> jsonMap) {
		this.jsonMap = jsonMap;
	}

	/**
	 * Getter method for inputStream
	 * @return
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Setter method for inputStream
	 * @param inputStream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	

	@Override
	public String salGatewayMigrationList() {
		return null;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	protected void addError(String error) {
		errors.add(error);
	}
}
