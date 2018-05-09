package com.avaya.grt.service.salmigration;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.salmigration.SalMigrationDao;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.service.BaseRegistrationService;
import com.grt.dto.InstallBaseCreationDto;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.ARTOperationType;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public class SalMigrationServiceImpl extends BaseRegistrationService implements SalMigrationService{
	
	private static final Logger logger = Logger.getLogger(SalMigrationServiceImpl.class);
	public SalMigrationDao salMigrationDao;
	//private SalMigrationService salMigrationService;
	/*private BaseRegistrationService baseRegistrationService;*/
	private static Map<String, CompatibilityMatrix> seCodeCompatibilityMap = new HashMap<String, CompatibilityMatrix>();
	private static Map<String, CompatibilityMatrix> compatibilityMatrixMap = null;	
	   

	public List<SiteList> getSALMigrationEligibleAssets(String soldToId)
			throws DataAccessException {
    	logger.debug("Entering getSALMigrationEligibleAssets in Impl for soldToId:" + soldToId);
    	List<SiteList> matchedAssets = new ArrayList<SiteList>();
    	CompatibilityMatrix compatibilityMatrix = null; 
    	List<SiteList> siteArrayList = new ArrayList<SiteList>();
        try {   	
        	//initializeCompatibilityMatrixData();
        	//CompatibilityMatrix compatibilityMatrix = null;
        	List<Object[]> resultSet = getSalMigrationDao().getSALMigrationEligibleAssets(soldToId);
        	
        	//added from getSiebelDao.getSALMigrationEligibleAssets
			if (compatibilityMatrixMap == null) {
				//compatibilityMatrixMap = salRegistrationService.getSECodeFromCompatibilityMatrix();
				compatibilityMatrixMap = initializeCompatibilityMatrixData();
			}
	    	//Iterate through the result set
			if(resultSet != null) {
		    	for (Object[] object : resultSet) {
		    		SiteList siteList = new SiteList();
		    		if (object[1] != null){
		    	    	logger.debug("<--------------object[1] != null  is true------->");
		    	    	if(object[3] != null) {
		    	    		compatibilityMatrix = compatibilityMatrixMap.get(object[3].toString());
		    	    	} else {
		    	    		logger.warn("SeCode for SEID:" + object[4] + " skipping this row.");
		    	    		continue;
		    	    	}
		    			//Commented the below line for demo on 05/24/2013
		    			//if (compatibilityMatrix != null) {
		    				siteList.setSoldToId(soldToId);
		    				if(object[0] != null) {// If not filtered out, this might show V00328
		    					siteList.setMaterialCode(object[0].toString());
		    				}
		    				if(object[1] != null) {// If not filtered out, this might show Registration for V00328
		    					siteList.setMaterialCodeDescription(object[1].toString());
		    				}
		    				if(object[2] != null) {
		    					siteList.setProductLine(object[2].toString());
		    				}
		    				//This column should be renamed to SeCode.
		    				if(object[3] != null) {
		    					siteList.setSeCode(object[3].toString());
		    				}
		    				if (object[4] != null) {
								siteList.setSolutionElementId(object[4].toString());
							}
		    				//Wrong ColumnName, this actually should be renamed to alarmId
							if (object[5] != null) {
								siteList.setAlarmId(object[5].toString());
							}
							if (object[6] != null) {
								siteList.setSid(object[6].toString());
							}
							if (object[7] != null) {
								siteList.setMid(object[7].toString());
							}
							if (compatibilityMatrix != null){
							siteList.setModel(compatibilityMatrix.getModel());
							siteList.setRemoteAccess(compatibilityMatrix.getRemoteAccess());
							siteList.setTransportAlarm(compatibilityMatrix.getTransportAlarm());
							}else{
								siteList.setRemoteAccess("N");
								siteList.setTransportAlarm("N");
							}

							siteArrayList.add(siteList);
		    			//}
						//Commented the above line for demo on 05/24/2013.
		    		}
		    	}
			}
			//added from getSiebelDao.getSALMigrationEligibleAssets
			
	    	//Iterate through the result set
    		if (siteArrayList.size() > 0) {
    			List <TRConfig> matchingTRConfigs = new ArrayList<TRConfig>();
    			for (SiteList siteList : siteArrayList ){
    				String groupId = this.getGroupId(siteList.getMaterialCode(), siteList.getSeCode());
    				if(StringUtils.isNotEmpty(groupId)) {
    					siteList.setGroupId(groupId);
    					matchedAssets.add(siteList);
    				}
    			}
    		}
        } catch (Exception ex) {
        	ex.printStackTrace();
        	logger.error("Error getSALMigrationEligibleAssets", ex);
            throw new DataAccessException(SalMigrationServiceImpl.class, ex
                    .getMessage(), ex);
        } finally {
	    	logger.debug("Exiting getSALMigrationEligibleAssets for soldToId:" + soldToId);
        }
        return matchedAssets;
    }
	
	//30042015
	 public void saveExistingRegisteredAssetsList(List<SiteList> siteList, SiteRegistration siteRegistration) throws DataAccessException, Exception{
		 logger.debug("Entering RegistrationService.existingRegisteredAssetsList()");
    	if (siteList.size() > 0) {
    		List<SiteList> siteListAfterSave = new ArrayList<SiteList>();
    		for (SiteList s : siteList ){
    			s.setStepASubmittedDate(new Date());
    			getSalMigrationDao().saveSalSiteListForSALMigration(s);
    			siteListAfterSave.add(s);
    			//getSalRegistrationDao().saveSalSiteList(s);
    		}

    		//getSalRegistrationService().createGatewayNew(null, null, GRTConstants.DEFAULT_ART_USER, siteListAfterSave, GRTConstants.DU);
    		logger.debug("<========================== BEFORE CALLING THE TECHNICAL REGISTRATION FOR DB UPDATE =========> " );
    		logger.debug("TEH SIZE OF THE siteList Records are ============> :: "+ siteList.size());
    		logger.debug("The value of Registration ID before calling the SUBMIT for ART ============> :: "+ siteRegistration.getRegistrationId());
    		this.doTechnicalRegistration(siteRegistration, null, siteList, ARTOperationType.DATABASEUPDATE);
    		getSalMigrationDao().updateHasTRSubmittedManually(siteRegistration.getRegistrationId());
    	}

		logger.debug("Exiting RegistrationService.existingRegisteredAssetsList()");
    }
	 
	 
	 public void createNewSALGateway (SiteRegistration siteRegistration, List<TechnicalOrderDetail> installBaseData, TRConfig tRConfig, String userId) throws Exception{
	        logger.debug("Entering RegistrationService : createNewSALGateway()");
	    	//Save SiteRegistration
			int save = getBaseHibernateDao().saveSiteRegistrationDetails(siteRegistration);
			if(save == 1){
				logger.debug("SiteRegistration For NewSALGateway SAVED  ");
			} else {
				logger.error(" Error while saving the SiteRegistration For NewSALGateway ");
			}

			//Save TechnicalOrderDetails
			saveTechnicalOrderDetail (installBaseData.get(0), siteRegistration );
			logger.error("<---------------------- BEGIN IB CALL ------------------------------------->");
			//Begin IB Creation as part of the Full Registration
			InstallBaseCreationDto installBaseDto = new InstallBaseCreationDto();
			installBaseDto.setRegistrationId(siteRegistration.getRegistrationId());
			installBaseDto.setInstallBaseData(installBaseData);
			String destination = getBaseHibernateDao().getSapBox(siteRegistration.getSoldToId());

			installBaseDto.setDestination(destination);
			//returnCode = this.getSapClient().installBaseCreation(installBaseDto);
			this.installBaseCreationAsync(installBaseDto);
	        logger.debug("Exiting RegistrationService : createNewSALGateway()");
	    }
	 
		private TechnicalOrder saveTechnicalOrderDetail (TechnicalOrderDetail technicalOrderDetail, SiteRegistration siteRegistration) throws  Exception {
			logger.debug(" Entering into  saveTechnicalOrderDetail");
			TechnicalOrder technicalOrder = new TechnicalOrder();
			technicalOrder.setMaterialCode(technicalOrderDetail.getMaterialCode());
			technicalOrder.setInitialQuantity(technicalOrderDetail.getInitialQuantity());
			technicalOrder.setRemainingQuantity(technicalOrderDetail.getRemainingQuantity());
			technicalOrder.setOrderType(technicalOrderDetail.getOrderType());
			technicalOrder.setMaterialExclusion(technicalOrderDetail.getMaterialExclusion());
			technicalOrder.setSolutionElementCode(technicalOrderDetail.getSolutionElementCode());
			technicalOrder.setAutoTR(technicalOrderDetail.isAutoTR());
			technicalOrder.setCreatedBy(GRTConstants.USER_ID_SYSTEM);
			technicalOrder.setCreatedDate(new Date());
			technicalOrder.setUpdatedBy(GRTConstants.USER_ID_SYSTEM);
			technicalOrder.setUpdatedDate(new Date());
			technicalOrder.setSiteRegistration(siteRegistration);
			//Ask what are the values to be populated for these three columns
			technicalOrder.setIsSelected(1L);
			technicalOrder.setHasActiveEquipmentContract(0L);
			technicalOrder.setHasActiveSiteContract(0L);
			logger.debug("@@@@@@@@@@@@@@@@@@@@@@  BEFORE SAVING THE TECHNICAL ORDER FOR SAL GATEWAY"  );
			String orderId = getSalMigrationDao().saveTechnicalOrderForSALGateway(technicalOrder);
			logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@ AFTER SAVING THE TECHNICAL ORDER FOR SAL GATEWAY");
			logger.debug(" Exiting from  saveTechnicalOrderDetail");
			return technicalOrder;
		}
		
		
		public void installBaseCreationAsync(InstallBaseCreationDto installBaseDto) throws Exception {
			String returnCode = getSapClient().installBaseCreation(installBaseDto);
			if (returnCode == null) {
				returnCode = GRTConstants.exception_errorcode;
			}else if(returnCode.equals(GRTConstants.installBase_successCode)){
				//Update Site Registration status to In Process
				getBaseHibernateDao().updateSiteRegistrationStatusOnRegId(installBaseDto.getRegistrationId(), StatusEnum.INPROCESS, ProcessStepEnum.INSTALL_BASE_CREATION);
				//this.updateSiteRegistrationSubmittedFlag(installBaseDto.getRegistrationId(), GRTConstants.isSubmitted_true);
				sendRegistrationRequestAlert(installBaseDto.getRegistrationId(), ProcessStepEnum.INSTALL_BASE_CREATION, null);
			}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
				String errMsg="GRT unable to reach FMW";
				logger.debug(errMsg);
				//Updating the IsSubmitted to false
				//getRegistrationDao().updateSiteRegistrationSubmittedFlag(installBaseDto.getRegistrationId(), GRTConstants.isSubmitted_false);
				//Mail to System Admin
				sendEmailToSystemAdmin(installBaseDto.getRegistrationId(), errMsg, ProcessStepEnum.INSTALL_BASE_CREATION);
			}
		}
		
		/**
	     * API to get the technical registration configuration data
	     *
	     * @param String materialCode
	     * @return TRConfig TRConfig for eligible materialCode
	     */
	    public List<TRConfig> isTREligible(String materialCode) {
	    	if(StringUtils.isNotEmpty(materialCode)) {
				return baseHibernateDao.getTRConfigData(materialCode);
	    	}
	    	return null;
	    }
		
	public SalMigrationDao getSalMigrationDao() {
		return salMigrationDao;
	}

	public void setSalMigrationDao(SalMigrationDao salMigrationDao) {
		this.salMigrationDao = salMigrationDao;
	}

/*	public SalMigrationService getSalMigrationService() {
		return salMigrationService;
	}

	public void setSalMigrationService(SalMigrationService salMigrationService) {
		this.salMigrationService = salMigrationService;
	}*/

	public static Map<String, CompatibilityMatrix> getSeCodeCompatibilityMap() {
		return seCodeCompatibilityMap;
	}

	public static void setSeCodeCompatibilityMap(
			Map<String, CompatibilityMatrix> seCodeCompatibilityMap) {
		SalMigrationServiceImpl.seCodeCompatibilityMap = seCodeCompatibilityMap;
	}

	public static Map<String, CompatibilityMatrix> getCompatibilityMatrixMap() {
		return compatibilityMatrixMap;
	}

	public static void setCompatibilityMatrixMap(
			Map<String, CompatibilityMatrix> compatibilityMatrixMap) {
		SalMigrationServiceImpl.compatibilityMatrixMap = compatibilityMatrixMap;
	}

/*	public BaseRegistrationService getBaseRegistrationService() {
		return baseRegistrationService;
	}

	public void setBaseRegistrationService(
			BaseRegistrationService baseRegistrationService) {
		this.baseRegistrationService = baseRegistrationService;
	}*/

	
}
