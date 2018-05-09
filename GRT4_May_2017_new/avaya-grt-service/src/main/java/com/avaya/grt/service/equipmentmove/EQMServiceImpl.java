package com.avaya.grt.service.equipmentmove;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.equipmentmove.EQMDao;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.grt.service.equipmentremoval.EQRServiceImpl;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;


public class EQMServiceImpl extends EQRServiceImpl implements EQMService{
	private static final Logger logger = Logger.getLogger(EQMServiceImpl.class);
	public EQMDao eqmDao;
	
	
	public EQMDao getEqmDao() {
		return eqmDao;
	}


	public void setEqmDao(EQMDao eqmDao) {
		this.eqmDao = eqmDao;
	}

	/**
	 * Method returns true if countries for both the soldTos are same
	 * and returns false if countries for both the soldTos are different 
	 * @param fromSoldTo
	 * @param toSoldTo
	 * @return result
	 */
	public boolean isSameCountry(String fromSoldTo, String toSoldTo) {
		logger.debug("Entering EQMServiceImpl.isSameCountry");
		
		boolean result = false;
		if ( (!fromSoldTo.isEmpty()) && (!toSoldTo.isEmpty()) ) {
			result = getEqmDao().isSameCountry(fromSoldTo, toSoldTo);
		}
		
		logger.debug("Exiting EQMServiceImpl.isSameCountry");
		return result;
	}
	/**
	 * Method to process equipment move request
	 * @param siteRegistration
	 * @param regId
	 * @param soldTo
	 * @param toSoldTo
	 * @param actualList
	 * @param mcList
	 * @param userId
	 * @param uploadFilePath
	 * @return 
	 * @throws Exception
	 */	
	 public String equipmentMove(SiteRegistration siteRegistration, String regId, 
			 String soldTo, String toSoldTo, List<TechnicalOrderDetail> actualList, List<TechnicalOrderDetail> mcList, String userId, String uploadFilePath) throws Exception{
	    	logger.debug("Entering EQMServiceImpl.equipmentMove");
	    	String returnCode = null;
	    	String errMsg = "";
	    	boolean completeFlag = false;
	    	List<TechnicalOrderDetail> combinedQtyList = null;
	    	List<TechnicalOrderDetail> pipelineCheckList = null;
	    	List<TechnicalOrderDetail> isSALGWOrVSALGWList = new ArrayList<TechnicalOrderDetail>();
	    	
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

			//Save all the selected records in Technical_Order table
			logger.debug("MoveAssets size:   "+selectedTOs.size());
	    	returnCode = saveSelectedEquipmentsForEQR(regId, soldTo, mcList, userId, GRTConstants.TECH_ORDER_TYPE_EM);
			if(returnCode.equalsIgnoreCase(GRTConstants.isSubmitted_false)){
				logger.debug("Unable to save the records selected for Equipment Move");
			}
			
			// Equipments to be processed with contracts logged in the log file
	    	logRemovalRecords(siteRegistration, regId, selectedTOs, errMsg);
	    	
	    	//Find the list of assets from the main asset list received from siebel that are present in sap_pipeline_transactions table 
	    	List<TechnicalOrderDetail> techList = prepareSapRemovalAssetsList(actualList);
	    	
	    	// Filter pipeline records
			pipelineCheckList = filterTechnicalOrderOnPipeline(techList, soldTo, "SAP");
			
			// To persist the Equipments selected for move at Equipment Level
	    	List<TechnicalOrderDetail> moveAssets = new ArrayList<TechnicalOrderDetail>();
	    	for(TechnicalOrderDetail technicalOrderDetail: selectedTOs){
	    		if(!technicalOrderDetail.isSalGateway()) {
	    			if(technicalOrderDetail.getTechnicallyRegisterable().equalsIgnoreCase(GRTConstants.YES)
	    					|| (technicalOrderDetail.getSerialNumber() != null && StringUtils.isNotEmpty(technicalOrderDetail.getSerialNumber()))){
	    				moveAssets.add(technicalOrderDetail);
	    			} else {
	    				moveAssets.addAll(sapRemovalListOnMC(pipelineCheckList, technicalOrderDetail.getMaterialCode(), technicalOrderDetail.getRemovedQuantity(),null,false));
	    			}
	    		} else {
	    			isSALGWOrVSALGWList.add(technicalOrderDetail);
	    		}
			}
			
	    	if(moveAssets != null && moveAssets.size() > 0) {
				logger.debug("Actual List size :"+actualList.size());
				logger.debug("Equipments List Size sent for SAP move :"+moveAssets.size());
				combinedQtyList = combineEquipmentsWithSimilarEquipmentNumber(moveAssets);
				long quantity = 0;
				if(combinedQtyList != null && combinedQtyList.size() > 0){
					for(TechnicalOrderDetail technicalOrderDetail: pipelineCheckList){
						for(TechnicalOrderDetail combinedDto:combinedQtyList){
							if(technicalOrderDetail.getMaterialCode().equalsIgnoreCase(combinedDto.getMaterialCode())
									&& technicalOrderDetail.getSummaryEquipmentNumber().equalsIgnoreCase(combinedDto.getSummaryEquipmentNumber())){
								combinedDto.setInitialQuantity(technicalOrderDetail.getInitialQuantity());
								quantity = technicalOrderDetail.getInitialQuantity() - combinedDto.getRemovedQuantity();
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
	    		//GRT 4.0 : Commented for Defect#262 duplicate record entered in technical order table
				returnCode = this.getSapClient().equipmentMove(regId, soldTo, combinedQtyList, userId, toSoldTo);
			} else if(isSALGWOrVSALGWList.size() > 0){
				// Updating EQR sub status to SAP Completed
				getEqrDao().updateSiteRegistrationSubStatus(siteRegistration, StatusEnum.SAPCOMPLETED, ProcessStepEnum.EQUIPMENT_MOVE);
				completeFlag = true;
				returnCode = GRTConstants.EQR_MOVE_successCode;
			}
	    	logger.debug("Return Code:"+returnCode);
	    	
	    	if (returnCode == null) {
				returnCode = GRTConstants.exception_errorcode;
			}else if(returnCode.equals(GRTConstants.EQR_MOVE_successCode) || returnCode.equals(GRTConstants.sapDown_errorCode)){
				// Begin "Response Details Received from SAP"
		    	List<PipelineSapTransactions> pipelineSapTransactionsList = new ArrayList<PipelineSapTransactions>();
		    	Date dateTime = new Date();
		    	for(TechnicalOrderDetail technicalOrderDetail: moveAssets){
		    		technicalOrderDetail.setRegistrationId(regId);
		    		technicalOrderDetail.setShipToId(soldTo);
					PipelineSapTransactions pipelineSapTransactions = constructSapTransactionDetailsForEQR(technicalOrderDetail, dateTime);
					pipelineSapTransactions.setAction(GRTConstants.TECH_ORDER_TYPE_EM);
					pipelineSapTransactionsList.add(pipelineSapTransactions);
		    	}
				//End "Response Details Received from SAP"
		    	// Save the PipelineSapTransactions into Database.
		    	if(pipelineSapTransactionsList != null && pipelineSapTransactionsList.size() >0) {
		    		getEqrDao().savePipelineSapTransactionsList(pipelineSapTransactionsList);
		    	}
		    	// End Save the PipelineSapTransactions into Database
				// Updating registration status to In Process
				ProcessStepEnum processStep = ProcessStepEnum.EQUIPMENT_MOVE;
				getEqrDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, processStep, true);
				updateSiteRegistrationSubmittedFlag(regId, GRTConstants.isSubmitted_true); 
				sendRegistrationRequestAlert(regId, ProcessStepEnum.EQUIPMENT_MOVE, null);
			}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
				errMsg="GRT unable to reach FMW";
				logger.debug(errMsg);
				//Updating the IsSubmitted to false
				getEqrDao().updateSiteRegistrationSubmittedFlag(regId, GRTConstants.isSubmitted_false);
				//Mail to System Admin
				this.sendEmailToSystemAdmin(regId, errMsg, ProcessStepEnum.EQUIPMENT_MOVE);
			}
	    	
	    	//Defect #408 : Directly deactivate salgw records in siebel
	    	if(completeFlag){
	    		// Fetch TechnicalOrders with SECode as SALGW(MC:V00328)/VSALGW and deactivate in Siebel
				logger.debug("Only assets with SE Code SALGW(MC:V00328)/VSALGW are set for move.");
	    		this.getTechnicalOrderOnRegIdOrderTypeIsSALGatewaysEQM(regId, GRTConstants.TECH_ORDER_TYPE_EM, true, siteRegistration.getSoldToId(), siteRegistration.getToSoldToId());
				// Completing the registrations status to COMPLETED
	    		getEqrDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.COMPLETED, ProcessStepEnum.EQUIPMENT_MOVE, false);
	        	sendRegistrationRequestAlert(siteRegistration.getRegistrationId(), ProcessStepEnum.EQUIPMENT_MOVE, StatusEnum.COMPLETED);
	    	}
	    	
	    	logger.debug("Exiting EQMServiceImpl.equipmentMove");
	    	return returnCode;
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
	        List<TechnicalOrder> technicalOrderDetail = getEqrDao()
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
	        		getSiebelClient().eqmSiebelAssetUpdate(technicalOrderDtoList, sb);
					for(TechnicalOrderDetail technicalOrderDetail2:technicalOrderDtoList){
						mcsToBeProcessed.add(technicalOrderDetail2.getMaterialCode());
					}
					if(!mcsToBeProcessed.isEmpty()){
						getEqrDao().updateTODOpenQuantity(registrationId, registrationId, mcsToBeProcessed, GRTConstants.TECH_ORDER_TYPE_EM);
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
	    
	   
	 /**
	  * Method to get get existing stepB Registration by seid.
	  *
	  * @param seId
	  *            String
	  * @return regId String
	  */
	 public String getExistingStepBRegBySeid(String seId) throws DataAccessException {
		 logger.debug("Exiting EQMServiceImpl.getExistingStepBRegBySeid");
		 
		 String regId = getEqmDao().getExistingRegistrationBySeid(seId);
		 
		 logger.debug("Exiting EQMServiceImpl.getExistingStepBRegBySeid");
		 
		 return regId;
	 }
	 
	 /*public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		EQMService eqmService = (EQMService) context.getBean("eqmService");
		
	 }*/
    
}
