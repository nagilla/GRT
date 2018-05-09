package com.avaya.grt.service.equipmentremoval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.equipmentremoval.EQRDao;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.service.BaseRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;


public class EQRServiceImpl extends BaseRegistrationService implements EQRService{
	private static final Logger logger = Logger.getLogger(EQRServiceImpl.class);
	public EQRDao eqrDao;
	
	public EQRDao getEqrDao() {
		return eqrDao;
	}
	public void setEqrDao(EQRDao eqrDao) {
		this.eqrDao = eqrDao;
	}
	
	/**
     * Method to delete technical orders from technical order table for a given registration id.
     * 
     * @param registrationId
     * @param processStep
     * @return flag
     * @throws DataAccessException
     */
	public int deleteTechnicalOrderForRegId(String registrationId, ProcessStepEnum processStep) throws DataAccessException{
	    	logger.debug("Entering EQRServiceImpl.deleteTechnicalOrderForRegId");
	    	int flag = getEqrDao().deleteTechnicalOrderForRegId(registrationId, processStep);
	    	logger.debug("Exiting EQRServiceImpl.deleteTechnicalOrderForRegId");
	    	return flag;
	    }
    /**
     * Method to save the equipments selected for EQR-SAVE
     * 
     */
    public String saveSelectedEquipmentsForEQR(String regId, String soldTo, java.util.List<TechnicalOrderDetail> selectedTOs, String userId, String orderType) throws Exception{
    	logger.debug("Entering EQRServiceImpl.saveSelectedEquipmentsForEQR:  " + regId);
    	String returnCode = "1";
    	List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();
    	TechnicalOrder techOrder = null;
    	SiteRegistration siteRegistration = null;
    	for(TechnicalOrderDetail technicalOrderDetail:selectedTOs){
    		techOrder = new TechnicalOrder();
    		siteRegistration = new SiteRegistration();
    		siteRegistration.setRegistrationId(regId);
    		siteRegistration.setSoldToId(soldTo);
    		techOrder.setSiteRegistration(siteRegistration);
    		if(technicalOrderDetail.getDeleted() != null && technicalOrderDetail.getDeleted()){
    			techOrder.setDeleted(GRTConstants.YES);
    		} else {
    			techOrder.setDeleted(GRTConstants.NO);
    		}
    		techOrder.setInitialQuantity(technicalOrderDetail.getInitialQuantity());
    		techOrder.setRemainingQuantity(technicalOrderDetail.getRemainingQuantity());
    		techOrder.setOpenQuantity(technicalOrderDetail.getRemovedQuantity());
    		techOrder.setEquipmentNumber(technicalOrderDetail.getEquipmentNumber());
    		techOrder.setSummaryEquipmentNumber(technicalOrderDetail.getSummaryEquipmentNumber());
    		techOrder.setMaterialCode(technicalOrderDetail.getMaterialCode());
    		techOrder.setOrderType(orderType);
    		techOrder.setDescription(technicalOrderDetail.getDescription());
    		techOrder.setProductLine(technicalOrderDetail.getProductLine());
    		techOrder.setSerialNumber(technicalOrderDetail.getSerialNumber());
    		techOrder.setSolutionElementCode(technicalOrderDetail.getSolutionElementCode());
    		techOrder.setSeid(technicalOrderDetail.getSolutionElementId());
    		if(GRTConstants.YES.equalsIgnoreCase(technicalOrderDetail.getTechnicallyRegisterable())) {
    			techOrder.setHasActiveEquipmentContract(new Long(1));
    		} else {
    			techOrder.setHasActiveEquipmentContract(new Long(0));
    		}
    		if(GRTConstants.YES.equalsIgnoreCase(technicalOrderDetail.getActiveContractExist())) {
    			techOrder.setHasActiveSiteContract(new Long(1));
    		} else {
    			techOrder.setHasActiveSiteContract(new Long(0));
    		}
    		techOrder.setCreatedBy(userId);
    		techOrder.setCreatedDate(new Date());
    		techOrder.setUpdatedBy(userId);
    		techOrder.setUpdatedDate(new Date());
    		techOrder.setAssetPK(technicalOrderDetail.getAssetPK());
    		techOrder.setSid(technicalOrderDetail.getSid());
    		techOrder.setMid(technicalOrderDetail.getMid());
    		techOrder.setMaterialExclusion(technicalOrderDetail.getExclusionSource());
    		techOrder.setSalGateway(technicalOrderDetail.isSalGateway());
    		techOrder.setActionType(technicalOrderDetail.getActionType());
    		technicalOrderList.add(techOrder);
    		
    	}
    	List<TechnicalOrder> list = getEqrDao().saveTechnicalOrderList(technicalOrderList);
    	if(list != null && list.size() > 0){
    		returnCode = "0";
    	}
        logger.debug("Exiting EQRServiceImpl.saveSelectedEquipmentsForEQR");
        return returnCode;
    }
    
    /**
     * Equipment removal method
     */
    public String equipmentRemoval(SiteRegistration siteRegistration, String regId, String soldTo, List<TechnicalOrderDetail> actualList, List<TechnicalOrderDetail> mcList, String userId, String uploadFilePath) throws Exception{
    	logger.debug("Entering EQRServiceImpl.equipmentRemoval");
    	String returnCode = null;
    	List<TechnicalOrderDetail> combinedQtyList = null;
    	List<TechnicalOrderDetail> pipelineCheckList = null;
    	List<TechnicalOrderDetail> isSALGWOrVSALGWList = new ArrayList<TechnicalOrderDetail>();
    	String errMsg = "";
    	boolean completeFlag = false;
    	int flag = 0;
    	// Forming the selected equipment list for removal
		List<TechnicalOrderDetail> selectedTOs = new ArrayList<TechnicalOrderDetail>();
		for(TechnicalOrderDetail technicalOrderDetail : mcList){
			if(technicalOrderDetail.getDeleted() != null && technicalOrderDetail.getDeleted()){
				selectedTOs.add(TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(technicalOrderDetail));
			} else {
				technicalOrderDetail.setDeleted(false);
			}
		}
    	if(actualList != null)
    		logger.debug("Actual Material Code List before clubbing based on MC+EQN Size: "+actualList.size());

    	logger.debug("RemovalAssets size:   "+selectedTOs.size());
    	returnCode = saveSelectedEquipmentsForEQR(regId, soldTo, mcList, userId, GRTConstants.TECH_ORDER_TYPE_FV);
		if(returnCode.equalsIgnoreCase(GRTConstants.isSubmitted_false)){
			logger.debug("Unable to save the records selected for Equipment Removal");
		}

    	// Equipments to be processed with contracts
    	logRemovalRecords(siteRegistration, regId, selectedTOs, errMsg);
    	List<TechnicalOrderDetail> techList = prepareSapRemovalAssetsList(actualList);
    	// Filter pipeline records
		pipelineCheckList = this.filterTechnicalOrderOnPipeline(techList, soldTo, "SAP");
    	// To persist the Equipments selected for removal at Equipment Level
    	List<TechnicalOrderDetail> removalAssets = new ArrayList<TechnicalOrderDetail>();
    	for(TechnicalOrderDetail technicalOrderDetail: selectedTOs){
    		boolean add = false;
    		if(null != technicalOrderDetail.getRemainingQuantity() && null !=technicalOrderDetail.getInitialQuantity() ) {
    			if(technicalOrderDetail.getRemainingQuantity() > technicalOrderDetail.getInitialQuantity()) {
    				add = true;
    			}
    		}
    		if(!technicalOrderDetail.isSalGateway()) {
    			if(null != technicalOrderDetail.getActionType() && 
    					("A".equalsIgnoreCase(technicalOrderDetail.getActionType()) || "S".equalsIgnoreCase(technicalOrderDetail.getActionType()))) {
    				removalAssets.add(technicalOrderDetail);
    			}else if(null != technicalOrderDetail.getTechnicallyRegisterable() && technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
    					|| (technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber()))){
    				removalAssets.add(technicalOrderDetail);
    			} else {
    				removalAssets.addAll(sapRemovalListOnMC(pipelineCheckList, technicalOrderDetail.getMaterialCode(), technicalOrderDetail.getRemovedQuantity(), technicalOrderDetail.getActionType(), add));
    			}
    		} else {
    			isSALGWOrVSALGWList.add(technicalOrderDetail);
    		}
		}
    	
		if(removalAssets != null && removalAssets.size() > 0){
			logger.debug("Actual List size :"+actualList.size());
			logger.debug("Equipments List Size sent for SAP removal :"+removalAssets.size());
			combinedQtyList = combineEquipmentsWithSimilarEquipmentNumber(removalAssets);
			long quantity = 0;
			if(combinedQtyList != null && combinedQtyList.size() > 0){
				for(TechnicalOrderDetail technicalOrderDetail: pipelineCheckList){
					for(TechnicalOrderDetail combinedDto:combinedQtyList){
						if(technicalOrderDetail.getMaterialCode().equalsIgnoreCase(combinedDto.getMaterialCode())
								&& technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(combinedDto.getSummaryEquipmentNumber())){
							combinedDto.setInitialQuantity(technicalOrderDetail.getInitialQuantity());
							if(null != combinedDto.getRemovedQuantity()) {
								if(combinedDto.getInitialQuantity() < combinedDto.getRemainingQuantity()) {
									quantity = technicalOrderDetail.getInitialQuantity() + combinedDto.getRemovedQuantity();
								}else {
									quantity = technicalOrderDetail.getInitialQuantity() - combinedDto.getRemovedQuantity();
								}
							}
							if(quantity > 0){
								combinedDto.setRemainingQuantity(quantity);
							} else {
								combinedDto.setRemainingQuantity(0L);
							}
						}
					}
				}
			}
		}
		if(combinedQtyList != null && combinedQtyList.size() > 0) {
			saveSelectedEquipmentsForEQR(regId, soldTo, combinedQtyList, userId, GRTConstants.TECH_ORDER_TYPE_EQR);
			returnCode = this.getSapClient().equipmentRemoval(regId, soldTo, combinedQtyList, userId, getEqrDao().getSapBox(soldTo));
		} else if(isSALGWOrVSALGWList.size() > 0){
			// Updating EQR sub status to SAP Completed
			getEqrDao().updateSiteRegistrationSubStatus(siteRegistration, StatusEnum.SAPCOMPLETED, ProcessStepEnum.FINAL_RECORD_VALIDATION);
			completeFlag = true;
			returnCode = GRTConstants.EQR_successCode;
		}
    	logger.debug("Retrun Code:"+returnCode);
    	if (returnCode == null) {
			returnCode = GRTConstants.exception_errorcode;
		}else if(returnCode.equals(GRTConstants.EQR_successCode) || returnCode.equals(GRTConstants.sapDown_errorCode)){
			// Begin "Response Details Received from SAP"
	    	List<PipelineSapTransactions> pipelineSapTransactionsList = new ArrayList<PipelineSapTransactions>();
	    	Date dateTime = new Date();
	    	for(TechnicalOrderDetail technicalOrderDetail: removalAssets){
	    		technicalOrderDetail.setRegistrationId(regId);
	    		technicalOrderDetail.setShipToId(soldTo);
				PipelineSapTransactions pipelineSapTransactions = constructSapTransactionDetailsForEQR(technicalOrderDetail, dateTime);
				pipelineSapTransactionsList.add(pipelineSapTransactions);
	    	}
			//End "Response Details Received from SAP"
	    	// Save the PipelineSapTransactions into Database.
	    	if(pipelineSapTransactionsList != null && pipelineSapTransactionsList.size() >0) {
	    		getEqrDao().savePipelineSapTransactionsList(pipelineSapTransactionsList);
	    	}
	    	// End Save the PipelineSapTransactions into Database
			// Updating registration status to In Process
			ProcessStepEnum processStep = ProcessStepEnum.FINAL_RECORD_VALIDATION;
			getEqrDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, processStep, true);
			this.updateSiteRegistrationSubmittedFlag(regId, GRTConstants.isSubmitted_true); 
			sendRegistrationRequestAlert(regId, ProcessStepEnum.FINAL_RECORD_VALIDATION, null);
		}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
			errMsg="GRT unable to reach FMW";
			logger.debug(errMsg);
			//Updating the IsSubmitted to false
			getEqrDao().updateSiteRegistrationSubmittedFlag(regId, GRTConstants.isSubmitted_false);
			//Mail to System Admin
			this.sendEmailToSystemAdmin(regId, errMsg, ProcessStepEnum.FINAL_RECORD_VALIDATION);
		}
    	if(completeFlag){
    		// Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel
			logger.debug("Only assets with SE Code SALGW(MC:V00328)/VSALGW are set for removal.");
    		this.getTechnicalOrderOnRegIdOrderTypeIsSALGateways(regId, GRTConstants.TECH_ORDER_TYPE_FV, true);
			// Completing the registrations status to COMPLETED
    		getEqrDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.FINAL_RECORD_VALIDATION, false);
        	sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), ProcessStepEnum.FINAL_RECORD_VALIDATION, StatusEnum.COMPLETED);
    	}
    	logger.debug("Exiting EQRServiceImpl.equipmentRemoval");
    	return returnCode;
    }
    
    /**
     * Logging for the Technically registered asset
     * 
     * @param siteRegistration
     * @param regId
     * @param selectedTOs
     * @param errMsg
     * @throws Exception
     */
    public void logRemovalRecords(SiteRegistration siteRegistration, String regId, List<TechnicalOrderDetail> selectedTOs, String errMsg) throws Exception{
    	logger.debug("Entering EQRServiceImpl.logRemovalRecords");
    	
    	for(TechnicalOrderDetail technicalOrderDetail:selectedTOs){
			// Logging for the Technically registered asset
			if(StringUtils.isNotEmpty(technicalOrderDetail.getTechnicallyRegisterable())
					&& technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)){
				logger.debug("Technical Registered Asset removed with GRTID# "+regId
						+", material code/SE code as "+technicalOrderDetail.getMaterialCode()+"/"+technicalOrderDetail.getSolutionElementCode()
						+", data/time: "+new Date());
			}
		}
    	logger.debug("Exiting EQRServiceImpl.logRemovalRecords");
    }
    
    /**
     * prepare sap removal asset list
     * @param technicalOrderDetailList
     * @return
     */
    public List<TechnicalOrderDetail> prepareSapRemovalAssetsList (List<TechnicalOrderDetail> technicalOrderDetailList){
    	logger.debug("Entering EQRServiceImpl.prepareSapRemovalAssetsList");
    	Map<String, TechnicalOrderDetail> map = new HashMap<String, TechnicalOrderDetail>();
		String key = null;
		long quantity = 0;
		TechnicalOrderDetail tempDto = null;
		TechnicalOrderDetail technicalOrderDetail = null;
		for(int index = 0; index < technicalOrderDetailList.size(); index++){
			
    		if(technicalOrderDetailList.get(index) instanceof TechnicalOrderDetail){
    			technicalOrderDetail = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(technicalOrderDetailList.get(index));
    			    			
    			logger.debug("MC:"+technicalOrderDetail.getMaterialCode()+" EQN:"+technicalOrderDetail.getEquipmentNumber()+"  TR:"+technicalOrderDetail.getTechnicallyRegisterable()+"  SR:"+technicalOrderDetail.getSerialNumber());
				key = technicalOrderDetail.getMaterialCode()+":"+technicalOrderDetail.getSummaryEquipmentNumber();
				if(map.size() > 0 && map.containsKey(key)){
					tempDto = map.get(key);
					quantity = tempDto.getInitialQuantity() + technicalOrderDetail.getInitialQuantity();
					logger.debug("Actual:"+tempDto.getInitialQuantity()+"   Added:" + technicalOrderDetail.getInitialQuantity());
					logger.debug("Already existing record in MAP:"+quantity);
					tempDto.setInitialQuantity(quantity);
					map.put(key, tempDto);
				} else {
					map.put(key, technicalOrderDetail);
				}
    		}
		}
		List<TechnicalOrderDetail> processedList = new ArrayList<TechnicalOrderDetail>();
		for(Map.Entry<String, TechnicalOrderDetail> dto: map.entrySet()){
			TechnicalOrderDetail tdo = dto.getValue();
			logger.debug("Material Code:"+tdo.getMaterialCode()+"    EQN:"+tdo.getSummaryEquipmentNumber()
					+"    Initial Qty:"+tdo.getInitialQuantity());
			processedList.add(dto.getValue());
		}
		logger.debug("Exiting EQRServiceImpl.prepareSapRemovalAssetsList");
		return processedList;
    }
    
    /**
     * prepare sap removal list on mc
	 * @param technicalOrderDetailList
	 * @param materialCode
	 * @param totalQuantity
	 * @param actionType
	 * @param add
	 * @return
	 */
public List<TechnicalOrderDetail> sapRemovalListOnMC(List<TechnicalOrderDetail> technicalOrderDetailList,
    		String materialCode, long totalQuantity, String actionType, boolean add){
	    logger.debug("Entering EQRServiceImpl.sapRemovalListOnMC");
    	TechnicalOrderDetail technicalOrderDetail = null;
    	List<TechnicalOrderDetail> processedList = new ArrayList<TechnicalOrderDetail>();
    	for(TechnicalOrderDetail todDto: technicalOrderDetailList){
    		technicalOrderDetail = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(todDto);
    		if(technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
    				|| (technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber()))){
    			// Dont add to the list
    		} else if(totalQuantity > 0 && technicalOrderDetail.getMaterialCode().equals(materialCode)){
    			if(add) {
    				technicalOrderDetail.setRemainingQuantity(technicalOrderDetail.getInitialQuantity() + totalQuantity);
    				technicalOrderDetail.setRemovedQuantity(totalQuantity);
    				technicalOrderDetail.setActionType(actionType);
    				processedList.add(technicalOrderDetail);
    				break;
    			}
    			else {
    				if(technicalOrderDetail.getInitialQuantity() > totalQuantity){
    					technicalOrderDetail.setRemainingQuantity(technicalOrderDetail.getInitialQuantity() - totalQuantity);
    					technicalOrderDetail.setRemovedQuantity(totalQuantity);
    					technicalOrderDetail.setActionType(actionType);
    					processedList.add(technicalOrderDetail);
    					break;
    				} else if(technicalOrderDetail.getInitialQuantity() <= totalQuantity) {
    					technicalOrderDetail.setRemainingQuantity(new Long(0));
    					technicalOrderDetail.setRemovedQuantity(technicalOrderDetail.getInitialQuantity());
    					technicalOrderDetail.setActionType(actionType);
    					processedList.add(technicalOrderDetail);
    					totalQuantity = totalQuantity - technicalOrderDetail.getInitialQuantity();
    				}
    			}
    		}
    	}
		logger.debug("Exiting EQRServiceImpl.sapRemovalListOnMC");
    	return processedList;
    }
    
    /**
     * method to combine equipments with similar equipment number
     * @param selectedTOs
     * @return
     */
    public List<TechnicalOrderDetail> combineEquipmentsWithSimilarEquipmentNumber(List<TechnicalOrderDetail> selectedTOs){
    	logger.debug("Entering EQRServiceImpl.combineEquipmentsWithSimilarEquipmentNumber with selectedTOs list Size: "+selectedTOs.size());
    	List<TechnicalOrderDetail> techOrderList = null;
    	//TechnicalOrderDetail technicalOrderDetail = null;
    	TechnicalOrderDetail outerObject = null;
    	int checkFlag = 0;
    	if(selectedTOs != null && selectedTOs.size() > 0){
    		techOrderList = new ArrayList<TechnicalOrderDetail>();
	    	for(TechnicalOrderDetail selectedTODObject:selectedTOs){
	    		outerObject = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(selectedTODObject);
	    		logger.debug("MC :"+outerObject.getMaterialCode()+"   EQN :"+outerObject.getEquipmentNumber()+"   SUM EQN :"+outerObject.getSummaryEquipmentNumber()+"   Qty :"+outerObject.getInitialQuantity());
	    		checkFlag = 0;
	    		if(techOrderList.size() > 0){
	    			for(TechnicalOrderDetail technicalOrderDetail:techOrderList){
	    				if(null != technicalOrderDetail.getSummaryEquipmentNumber()) {
	    					if(technicalOrderDetail.getMaterialCode().equalsIgnoreCase(outerObject.getMaterialCode())
	    							&& technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(outerObject.getSummaryEquipmentNumber())){
	    						technicalOrderDetail.setRemovedQuantity(technicalOrderDetail.getRemovedQuantity() + outerObject.getRemovedQuantity());
	    						technicalOrderDetail.setMovedQuantity(technicalOrderDetail.getRemovedQuantity() + outerObject.getRemovedQuantity());
	    						checkFlag = 1;
	    					}
	    				}
	    			}
	    		}
	    		if(checkFlag == 0){
	    			techOrderList.add(outerObject);
	    			checkFlag = 0;
	    			logger.debug("Added to list **** Material Code:"+outerObject.getMaterialCode()+"    EquipmentNumber:"+outerObject.getSummaryEquipmentNumber());
	    		}
	    	}
    	}
    	logger.debug("Exiting EQRServiceImpl.combineEquipmentsWithSimilarEquipmentNumber with Equipments to be sent for SAP, Final list Size: "+techOrderList.size());
    	return techOrderList;
    }
    
    /**
     * method to construct Sap Transaction Details For EQR
     * @param technicalOrderDetail
     * @param dateTime
     * @return
     */
    public PipelineSapTransactions constructSapTransactionDetailsForEQR(TechnicalOrderDetail technicalOrderDetail, Date dateTime){
		logger.debug("Entering EQRServiceImpl.constructSapTransactionDetailsForEQR()");
    	PipelineSapTransactions pipelineSapTransactions = new PipelineSapTransactions();
    	pipelineSapTransactions.setRegistrationId(technicalOrderDetail.getRegistrationId());
    	pipelineSapTransactions.setShipTo(technicalOrderDetail.getShipToId());
    	pipelineSapTransactions.setMaterialCode(technicalOrderDetail.getMaterialCode());
    	pipelineSapTransactions.setSerialNumber(technicalOrderDetail.getSerialNumber());
    	logger.debug("constructSapTransactionDetailsForEQR EQN:"+technicalOrderDetail.getSummaryEquipmentNumber());
    	pipelineSapTransactions.setEquipmentNumber(technicalOrderDetail.getSummaryEquipmentNumber());
    	logger.debug("constructSapTransactionDetailsForEQR Quantity:"+technicalOrderDetail.getRemovedQuantity());
    	pipelineSapTransactions.setQuantity(technicalOrderDetail.getRemovedQuantity());
    	pipelineSapTransactions.setAction(GRTConstants.TECH_ORDER_TYPE_FV);
    	pipelineSapTransactions.setDateTime(dateTime);
    	pipelineSapTransactions.setProcessed(false);
    	if(technicalOrderDetail.getTechnicallyRegisterable() != null
    			&& technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)){
    		pipelineSapTransactions.setTechnicallyRegisterable(true);
    	}
    	pipelineSapTransactions.setSapCompleted(false);
    	logger.debug("Exiting EQRServiceImpl.constructSapTransactionDetailsForEQR()");
    	return pipelineSapTransactions;
	}
    /**
     * method to update SiteRegistration Submitted Flag
     * @param registrationId
     * @param submitted
     */
    public void updateSiteRegistrationSubmittedFlag(String registrationId,String submitted){
    	try {
    		logger.debug("Entering EQRServiceImpl.updateSiteRegistrationSubmittedFlag");
    		
    		getEqrDao().updateSiteRegistrationSubmittedFlag(registrationId, submitted);
		} catch (DataAccessException e) {
			
			logger.debug("Exception while updating the submitted field",e);
		}
    	logger.debug("Exiting EQRServiceImpl.updateSiteRegistrationSubmittedFlag");
    }
    
    /**
     * Method to get the technical order details for the given registration id and orderType.
     *
     * @param registrationId
     *            String
     * @return technicalOrderDtoList List
     */
    public List<TechnicalOrderDetail> getTechnicalOrderOnRegIdOrderTypeIsSALGateways(
    		String registrationId, String orderType, boolean isSALGateways) throws DataAccessException {
    	logger.debug("Entering EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGateways");
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	List<String> mcsToBeProcessed = new ArrayList<String>();
        List<TechnicalOrder> technicalOrderDetail = getEqrDao()
                .getTechnicalOrderOnRegIdOrderTypeIsSALGateways(registrationId, orderType, isSALGateways);
        List<TechnicalOrderDetail> technicalOrderDtoList = TechnicalRegistrationUtil.prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
        try {
        	if(!technicalOrderDtoList.isEmpty()){
				getSiebelClient().deActivateSolutionElements(technicalOrderDtoList);
				for(TechnicalOrderDetail technicalOrderDetail2:technicalOrderDtoList){
					mcsToBeProcessed.add(technicalOrderDetail2.getMaterialCode());
				}
				if(!mcsToBeProcessed.isEmpty()){
					getEqrDao().updateTODOpenQuantity(registrationId, registrationId, mcsToBeProcessed, GRTConstants.TECH_ORDER_TYPE_FV);
				}
        	} else {
        		logger.debug("getTechnicalOrderOnRegIdOrderTypeIsSALGateways have empty list to process.. RegId:"+registrationId);
        	}
		} catch (Exception e) {
			logger.debug("Exception in EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGateways: "+e.getMessage());
			e.printStackTrace();
		}
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug(c2-c1 +" milliseconds");
        logger.debug("Exiting EQRServiceImpl.getTechnicalOrderOnRegIdOrderTypeIsSALGateways");
        return technicalOrderDtoList;
    }  
    
    /**
     * Filter technical order pipeline records for recod validation
     * @param filteredTODList
     * @param soldTo
     * @param process
     */
    public List<TechnicalOrderDetail> filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) throws DataAccessException{
    	 logger.debug("Entering EQRServiceImpl.filterTechnicalOrderOnPipeline");
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
			List<PipelineSapTransactions> pipelineSapTransactionsList = getEqrDao().fetchPipelineSapTransactionOnSoldTo(soldTo);
			if(pipelineSapTransactionsList != null && pipelineSapTransactionsList.size() > 0){
				logger.debug("SAP Pipeline List Size:"+pipelineSapTransactionsList.size());
				while(iter.hasNext()){
					technicalOrderDetail = TechnicalRegistrationUtil.constructTechnicalOrderDetailClone(iter.next());
					quantity = technicalOrderDetail.getInitialQuantity();
					for(PipelineSapTransactions pipelineSapTransactions:pipelineSapTransactionsList){
						included = false;
						if(pipelineSapTransactions.getMaterialCode().equalsIgnoreCase(technicalOrderDetail.getMaterialCode())
								&& pipelineSapTransactions.getEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
							if((technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
									|| (technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber())))
									&& !process.equalsIgnoreCase("SAP")  && pipelineSapTransactions.getAction() != null
									//GRT 4.0 Change : Added condition for Equipment Move
									&& ( pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV) || pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM) )){
								included = true;
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
								if(pipelineSapTransactions.getAction() !=null && pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV) || pipelineSapTransactions.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_EM)){
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
			throw new DataAccessException(EQRServiceImpl.class, daexception
					.getMessage(), daexception);
		} catch (Exception e) {
			logger.debug("Exception in filterTechnicalOrderOnPipeline :"+e.getMessage());
			throw new DataAccessException(EQRServiceImpl.class, e.getMessage(), e);
		}
		logger.debug("Exiting EQRServiceImpl.filterTechnicalOrderOnPipeline after SAP Pipeline check technicalOrderDetailList Size:"+filteredTODList.size());
		return techList;
    }
    /**
     * Method to get the technical order details for the given registration id and orderType.
     *
     * @param registrationId
     *            String
     * @return technicalOrderDtoList List
     */
    public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType, boolean allTOs) throws DataAccessException {
    	logger.debug("Entering EQRServiceImpl.getTechnicalOrderByType");
    	long c1 = Calendar.getInstance().getTimeInMillis();
        List<TechnicalOrder> technicalOrderDetail = getEqrDao()
                .getTechnicalOrderByType(registrationId, orderType, allTOs);
        List<TechnicalOrderDetail> technicalOrderDtoList = TechnicalRegistrationUtil.prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug(c2-c1 +" milliseconds");
        logger.debug("Exiting EQRServiceImpl.getTechnicalOrderByType");
        return technicalOrderDtoList;
    }
    
    /**
     * method to generate consolidated pipeline records. 
     *
     * @param soldToId
     *            String
     * @return pipelineList List
     */
    public List<PipelineSapTransactions> getConsolidatedPipelineRecords(String soldToId) throws DataAccessException {
    	logger.debug("Entering EQRServiceImpl.getConsolidatedPipelineRecords");
    	List<PipelineSapTransactions> pipelineList = this.getEqrDao().getConsolidatedPipelineRecords(soldToId);
    	logger.debug("Exiting EQRServiceImpl.getConsolidatedPipelineRecords");
    	return pipelineList;
    }

    /*public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		EQRService eqrService = (EQRService) context.getBean("eqrService");
		//----------------------------------------------------------------------------
		
		/*String registrationId ="6951384";
		SiteRegistration sitereg=eqrService.getSiteRegistrationOnRegId(registrationId );
		System.out.println("sitereg" +sitereg.getSoldToId());*/
		
		//----------------------------------------------------------------------------
		
		/*String vCode="";
		String soldTo="0004180153";
		List<TechnicalOrderDetail> techOrderList=eqrService.fetchEquipmentRemovalRecords(soldTo,vCode);
		for(TechnicalOrderDetail items:techOrderList )
		{
		System.out.println("ProductLine:"+items.getProductLine());
		}
		System.out.println("Size:"+techOrderList.size());*/
		
		//----------------------------------------------------------------------------
		
				/*List<String> materialCodes=new ArrayList<String>();
				materialCodes.add("190795");
				Map<String, String> map=eqrService.validateMaterialExclusion(materialCodes);
				if(map!=null){
					System.out.println("map size"+map.size());
				}
				
				else
				{
					System.out.println("No Exclusion");
				}*/
		
				//----------------------------------------------------------------------------
				
				/*String soldTo="0004180153";
				List<PipelineSapTransactions> pipelineSapTransList=eqrService.getConsolidatedPipelineRecords(soldTo);
				for(PipelineSapTransactions items:pipelineSapTransList )
				{
				System.out.println("Material Code:"+items.getMaterialCode());
				}
				System.out.println("Size:"+pipelineSapTransList.size());*/
				
				//----------------------------------------------------------------------------
		
		/*String registrationId = "6951384";
		String orderType = "IB";
		boolean allTOs = false;
		List<TechnicalOrderDetail> techOrderList=eqrService.getTechnicalOrderByType(registrationId, orderType, allTOs);
		for(TechnicalOrderDetail items:techOrderList )
		{
		System.out.println("Material Code:"+items.getMaterialCode());
		}
		System.out.println("Size:"+techOrderList.size());*/
		
		//-----------------------------------------------------------------------------
		
		/*String registrationId = "6951384";
		RegistrationSummary regList=eqrService.getRegistrationSummary(registrationId);
		if(regList!=null)
		{
			System.out.println("SoldTo:"+regList.getSoldTo());
		}
		else
		{
			System.out.println("List is empty");
		}*/
		
		/*String registrationId = null;
		ProcessStepEnum processStep = null;
		int value=eqrService.deleteTechnicalOrderForRegId(registrationId, processStep);
		System.out.println("Return value"+value);*/
    /*}*/
    
}
