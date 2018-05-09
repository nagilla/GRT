package com.avaya.grt.service.installbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.installbase.InstallBaseDao;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.MaterialCode;
import com.avaya.grt.mappers.ProcessStep;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.Province;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.mappers.RegistrationQuestions;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.State;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.service.BaseRegistrationService;
import com.grt.dto.Account;
import com.grt.dto.Activity;
import com.grt.dto.InstallBaseCreationDto;
import com.grt.dto.Product;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationQuestionsDetail;
import com.grt.dto.RegistrationRequestAlert;
import com.grt.dto.SALGatewayInstallerDto;
import com.grt.dto.SALGatewayInstallerResponseDto;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;

public class InstallBaseServiceImpl extends BaseRegistrationService implements InstallBaseService {
	private static final Logger logger = Logger.getLogger(InstallBaseServiceImpl.class);
	public InstallBaseDao installBaseDao;

	private TechnicalOrderDetailWorsheetProcessor worksheetProcessor;
	 
	/**
	 * setter method for worksheetProcessor.
	 *  
	 * @param worksheetProcessor TechnicalOrderDetailWorsheetProcessor
	 */
	public void setWorksheetProcessor(
			TechnicalOrderDetailWorsheetProcessor worksheetProcessor) {
		this.worksheetProcessor = worksheetProcessor;
	}

	public static Map<String, Set<String>> mc2GroupIdMappings = new HashMap<String, Set<String>>();
	private String userId = "System";

	/**
	 * Getter method for userId.
	 *  
	 * @return userId String
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * Setter method for userId.
	 *  
	 * @param userId String
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter method for installBaseDao.
	 *  
	 * @return installBaseDao InstallBaseDao
	 */
	public InstallBaseDao getInstallBaseDao() {
		return installBaseDao;
	}
	
	/**
	 * Setter method for installBaseDao.
	 *  
	 * @param installBaseDao InstallBaseDao
	 */
	public void setInstallBaseDao(InstallBaseDao installBaseDao) {
		this.installBaseDao = installBaseDao;
	}

	/**
	 * Method to get material code details for validation.
	 *  
	 * @param materialCodes list
	 * @return retVal Map
	 */
	public Map<String, MaterialCode> getMaterialCodeForValidation(List<String> materialCodes) {		
		logger.debug("Entering InstallBaseServiceImpl.getMaterialCodeForValidation");
		
        Map<String, MaterialCode> retVal = getInstallBaseDao().getMaterialCodeForValidation(materialCodes);
        
        logger.debug("Entering InstallBaseServiceImpl.getMaterialCodeForValidation");
        return retVal;
	}
	
	/**
	 * Method to get state Code.
	 *  
	 * @return codeMap Map
	 * @throws DataAccessException
	 */
	public SortedMap<String, String> getStateCode() throws DataAccessException {
		
		logger.debug("Entering InstallBaseServiceImpl.getStateCode");
		SortedMap<String, String> codeMap = new TreeMap<String, String>();
		
		List<State> stateList = getInstallBaseDao().getStateList();
        
		/**
		 * Traverse the list and translate into map.
		 */
		for (State state : stateList) {							
				codeMap.put(state.getStateId(),state.getState());
		}
		
		return codeMap;
	}
	

	/**
	 * Method to get Country code.
	 *  
	 * @return codeMap Map
	 * @throws DataAccessException
	 */
	public SortedMap<String, String> getCountryCode() throws DataAccessException{	
		logger.debug("Entering InstallBaseServiceImpl.getCountryCode");
		logger.debug("START :: InstallBaseServiceImpl.getCountryList");
		List<Country> countryList = getInstallBaseDao().getCountryList();
		Map<String,String> unsortedData = new HashMap<String, String>();
		unsortedData.put("", "");
		/**
		 * Traverse the list and translate into map.
		 */
		for (Country country : countryList) {							
			unsortedData.put(country.getCode(),country.getName());
		}
		SortedMap<String, String> codeMap = new TreeMap<String, String>(new InstallBaseServiceImpl.ValueComparer(unsortedData));
		codeMap.putAll(unsortedData);
		
		return codeMap;
	}

	/**
	 * Method to get Province Code.
	 *   
	 * @return codeMap Map
	 * @throws DataAccessException
	 */
	public SortedMap<String, String> getProvinceCode() throws DataAccessException{
		SortedMap<String, String> codeMap = new TreeMap<String, String>();
		logger.debug("Entering InstallBaseServiceImpl.getProvinceCode");
		List<Province> provinceList = getInstallBaseDao().getProvinceList();
        
		/**
		 * Traverse the list and translate into map.
		 */
		for (Province province : provinceList) {							
				codeMap.put(province.getId(),province.getName());
		}
		return codeMap;
	}

	/**
	 * Method to get Province Code.
	 *   
	 * @return regionmap Map
	 * @throws DataAccessException
	 */
	public Map<String,Map<Long,Region>> getRegionMap() throws DataAccessException {
		 Map<String,Map<Long,Region>> regionmap = new LinkedHashMap<String, Map<Long,Region>>();
		logger.debug("Entering InstallBaseServiceImpl.getRegionMap");

		List<Region> regionList = getInstallBaseDao().getRegionList();
        
		logger.debug("Total Regions " +regionList.size() );
        for (Region region : regionList){
            Map<Long, Region> region1 = regionmap.get(region.getCountryCode());
            if (region1 == null)
            {
                region1 = new LinkedHashMap<Long, Region>();
                regionmap.put(region.getCountryCode(), region1);
            }
            region1.put(region.getRegionId(), region);
        }
        logger.debug("Total Regions in map" +regionmap.size() );
        return regionmap;
	}

	
	
	 /**
	 * Check given soldTo exist in Excluded SoldTo table
	 * @param soldTo
	 * @return isValid
	 * @throws DataAccessException
	 */
	 public boolean isSoldToValid(String soldTo) throws DataAccessException {
	    	logger.debug("Entering InstallBaseServiceImpl : isSoldToValid");
	    	boolean isValid = false;

	    	if(getInstallBaseDao().isBpSoldToExist(soldTo)
	    			|| getInstallBaseDao().isSoldToExist(soldTo)) {
	    		isValid = true;
	    	}

	    	logger.debug("Existing InstallBaseServiceImpl : isSoldToValid");

	    	return isValid;
	    }
	 
	
	 /**
	  * Method to get Site Registration.
	  * @param registrationId String
	  * @return registration SiteRegistration
	  * @throws DataAccessException
	  */
	 public SiteRegistration getSiteRegistration(String registrationId) throws DataAccessException {
		 logger.debug("Entering InstallBaseServiceImpl : getSiteRegistration() for registration Id : " + registrationId);
		 SiteRegistration registration = getInstallBaseDao().getSiteRegistration(registrationId);
		return registration;
	 }
		/** inner class to do soring of the map **/
		private static class ValueComparer implements Comparator<Object>,Serializable {
			//default serial version id, required for serializable classes.
			private static final long serialVersionUID = 1L;
			private Map<String, String>  _data = null;
			public ValueComparer (Map<String, String> data){
				super();
				_data = data;
			}

	         public int compare(Object o1, Object o2) {
	        	 String e1 = (String) _data.get(o1);
	             String e2 = (String) _data.get(o2);
	             return e1.compareTo(e2);
	         }
		}
		
		/**
		 * Method to get the country code. 
		 * 
		 * @return contractCodeList HashMap
		 */
		public List<Country> getCountryValues() throws DataAccessException{
			
			List<Country> countryList = getInstallBaseDao().getCountryList();
			return countryList;
		}
		
	    /**
	     * Query Active Solution Elements total count from Siebel by soldToNumber
	     *
	     * @param String
	     *            soldToNumber
	     * @return total count
	     * @throws Exception
	     */
	    public int getExistingInstallBaseListCount (
	            String soldToNumber) throws Exception {
	    	long c1 = Calendar.getInstance().getTimeInMillis();
	        logger
	                .debug("Entering InstallBaseServiceImpl : getExistingInstallBaseList");
	        logger.debug("soldToNumber: " + soldToNumber);
	        int result = getInstallBaseDao().getAllSolutionElementsForAccountCountNew(soldToNumber, getvCodesList());
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug(c2-c1 +" milliseconds");
	        return result;
	    }
	    
	    /**
	     * Query Active Solution Elements subset from Siebel by soldToNumber and convert it
	     * to Technical Order Detail List
	     *
	     * @param String
	     *            soldToNumber
	     * @return List<TechnicalOrderDetail>
	     * @throws Exception
	     */
	    public List<TechnicalOrderDetail> getExistingInstallBaseList(
	            String soldToNumber, int offSet, int fetchSize) throws Exception {
	    	long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("Entering InstallBaseServiceImpl : getExistingInstallBaseList with offSet : " + offSet + " and fetchSize : " + fetchSize);
	        logger.debug("soldToNumber: " + soldToNumber);
	        List<TechnicalOrderDetail> technicalOrderList = new ArrayList<TechnicalOrderDetail>();
	        List<Product> productList = getInstallBaseDao().getAllSolutionElementsForAccountNew(soldToNumber, offSet, fetchSize, getvCodesList());
			//Begin to fetch the trEligibleList
	        List<String> materialCodes = new ArrayList<String>();
	        if (productList != null && productList.size() > 0){
	        	for (Product prodcut : productList){
	        		materialCodes.add(prodcut.getMaterialCode());
	        	}
	        }
	        List <TRConfig> trEligibleList = new ArrayList<TRConfig>();
			if (materialCodes != null && materialCodes.size()>0) {
				trEligibleList = this.isTREligible(materialCodes);
			}
			//End to fetch the trEligibleList

	        if ((productList != null) && productList.size() > 0) {
	            logger.debug("return asset size: " + productList.size());
	            for (Product product : productList) {
	                TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
	                technicalOrderDetail.setMaterialCode(product.getMaterialCode());
	                technicalOrderDetail.setInitialQuantity(new Long(product.getQuantity()));
	                technicalOrderDetail.setDescription(product.getShortDescription());
	                technicalOrderDetail.setProductLine(product.getProductLine());
	                if (trEligibleList != null && trEligibleList.size() >0){
		    			int count = 0;
		    			List <TRConfig> matchingTRConfigs = new ArrayList<TRConfig>();
						for (TRConfig trConfig : trEligibleList) {
							if (product.getMaterialCode().equals(trConfig.getParentMaterialCode())) {
								count++;
								matchingTRConfigs.add(trConfig);
							}
						}

						if(matchingTRConfigs != null && matchingTRConfigs.size() > 0){
							technicalOrderDetail.setTechnicallyRegisterable(GRTConstants.Y);
						} else {
							technicalOrderDetail.setTechnicallyRegisterable("");//empty for no
						}

	                }
	                technicalOrderList.add(technicalOrderDetail);
	            }
	        }
	        logger
	                .debug("Exiting InstallBaseServiceImpl : getExistingInstallBaseList");
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug(c2-c1 +" milliseconds");
	        return technicalOrderList;
	    }
	    /**
	     * API to get the technical registration configuration data
	     *
	     * @param String materialCode
	     * @return TRConfig TRConfig for eligible materialCode
	     */
	    public List<TRConfig> isTREligible(String materialCode) {
	    	if(StringUtils.isNotEmpty(materialCode)) {
				return this.getInstallBaseDao().getTRConfigData(materialCode);
	    	}
	    	return null;
	    }
	    /**
	     * API to get the technical registration configuration data
	     *
	     * @param String materialCode
	     * @return List<TRConfig> List of all eligible materialCodes
	     */
	    public List<TRConfig> isTREligible(List<String> materialCodes) {
	    	logger.debug("Entering isTREligible");
	    	List<TRConfig> retVal = new ArrayList<TRConfig>();
	    	try {
		    	if(materialCodes != null && materialCodes.size() > 0) {
		    		for (String materialCode : materialCodes) {
		    			if(StringUtils.isNotEmpty(materialCode)) {
		   					retVal.addAll(this.installBaseDao.getTRConfigData(materialCode));
		    			}
					}
		    	}
	    	} finally{
	    		logger.debug("Exiting InstallBaseServiceImpl.isTREligible");
	    	}
	    	return retVal;
	    }
	    
	    
	    /**
	     * Query Active Solution Elements from Siebel by soldToNumber and convert it
	     * to Technical Order Detail List
	     *
	     * @param String
	     *            soldToNumber
	     * @return List<TechnicalOrderDetail>
	     * @throws Exception
	     */
	    public List<TechnicalOrderDetail> getExistingInstallBaseList(
	            String soldToNumber) throws Exception {
	    	long c1 = Calendar.getInstance().getTimeInMillis();
	        logger
	                .debug("Entering InstallBaseServiceImpl : getExistingInstallBaseList");
	        logger.debug("soldToNumber: " + soldToNumber);
	        List<TechnicalOrderDetail> technicalOrderList = new ArrayList<TechnicalOrderDetail>();
	        List<Product> productList = getInstallBaseDao().getAllSolutionElementsForAccount(soldToNumber);

	        if ((productList != null) && productList.size() > 0) {
	            logger.debug("return asset size: " + productList.size());
	            for (Product product : productList) {
	                TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
	                technicalOrderDetail.setMaterialCode(product.getMaterialCode());
	                technicalOrderDetail.setInitialQuantity(new Long(product.getQuantity()));
	                technicalOrderDetail.setDescription(product.getShortDescription());
	                technicalOrderList.add(technicalOrderDetail);
	            }
	        }
	        logger
	                .debug("Exiting InstallBaseServiceImpl : getExistingInstallBaseList");
	        long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug(c2-c1 +" milliseconds");

	        return technicalOrderList;
	    }
	    
	   

	    /**
	     * method to clone TechnicalOrderDetail object.
	     * @param todDto
	     * @return technicalOrderDetail
	     */
	    private TechnicalOrderDetail constructTechnicalOrderDetailClone(TechnicalOrderDetail todDto){
			TechnicalOrderDetail technicalOrderDetail = null;
			if(todDto != null){
				technicalOrderDetail = new TechnicalOrderDetail();
				technicalOrderDetail.setDeleted(todDto.getDeleted());
				technicalOrderDetail.setAssetPK(todDto.getAssetPK());
				technicalOrderDetail.setAssetNumber(todDto.getAssetNumber());
				technicalOrderDetail.setEquipmentNumber(todDto.getEquipmentNumber());
				technicalOrderDetail.setSummaryEquipmentNumber(todDto.getSummaryEquipmentNumber());
				technicalOrderDetail.setMaterialCode(todDto.getMaterialCode());
				technicalOrderDetail.setDescription(todDto.getDescription());
	    		technicalOrderDetail.setInitialQuantity(todDto.getInitialQuantity());
	    		if(todDto.getRemainingQuantity() != null)
	    			technicalOrderDetail.setRemainingQuantity(todDto.getRemainingQuantity());
	    		if(todDto.getRemovedQuantity() != null)
	    			technicalOrderDetail.setRemovedQuantity(todDto.getRemovedQuantity());
	    		if(todDto.getPipelineEQRQuantity() != null) {
	    			technicalOrderDetail.setPipelineEQRQuantity(todDto.getPipelineEQRQuantity());
	    		} else {
	    			technicalOrderDetail.setPipelineEQRQuantity(0L);
	    		}
	    		if(todDto.getPipelineIBQuantity() != null) {
	    			technicalOrderDetail.setPipelineIBQuantity(todDto.getPipelineIBQuantity());
	    		} else {
	    			technicalOrderDetail.setPipelineIBQuantity(0L);
	    		}
	    		technicalOrderDetail.setSolutionElementId(todDto.getSolutionElementId());
	    		technicalOrderDetail.setSolutionElementCode(todDto.getSolutionElementCode());
	    		technicalOrderDetail.setTechnicallyRegisterable(todDto.getTechnicallyRegisterable());
	    		technicalOrderDetail.setActiveContractExist(todDto.getActiveContractExist());
	    		technicalOrderDetail.setProductLine(todDto.getProductLine());
	    		technicalOrderDetail.setSerialNumber(todDto.getSerialNumber());
	    		technicalOrderDetail.setSid(todDto.getSid());
	    		technicalOrderDetail.setMid(todDto.getMid());
	    		technicalOrderDetail.setGroupId(todDto.getGroupId());
	    		technicalOrderDetail.setExclusionSource(todDto.getExclusionSource());
	    		technicalOrderDetail.setExclusionFlag(todDto.isExclusionFlag());
	    		technicalOrderDetail.setErrorDescription(todDto.getErrorDescription());
	    		technicalOrderDetail.setSoldToId(todDto.getSoldToId());
	    		technicalOrderDetail.setSalGateway(todDto.isSalGateway());
			}
			return technicalOrderDetail;
		}
	    
	    /**
	     * Club TechnicalOrderDetail List based on MC
	     * @param technicalOrderDetailList
	     * @return processedList list of technical order details.
	     * @throws Exception
	     */
	    public List<TechnicalOrderDetail> constructTechnicalOrderDetailListOnMC(List<TechnicalOrderDetail> technicalOrderDetailList) throws Exception{
			Map<String, TechnicalOrderDetail> map = new HashMap<String, TechnicalOrderDetail>();
			List<String> materialCodes = new ArrayList<String>();
			String key = null;
			TechnicalOrderDetail tempDto = null;
			long qty = 0;
			for(TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList){
				materialCodes.add(technicalOrderDetail.getMaterialCode());
				key = technicalOrderDetail.getMaterialCode();
				if(map.size() > 0 && map.containsKey(key)){
					tempDto = constructTechnicalOrderDetailClone(map.get(key));
					qty = tempDto.getInitialQuantity() + technicalOrderDetail.getInitialQuantity();
					tempDto.setInitialQuantity(qty);
					map.put(key, tempDto);
				} else {
					map.put(key, constructTechnicalOrderDetailClone(technicalOrderDetail));
				}
			}
			//Fetch TRConfigs for List of MCs to determine TOB Eligible assets
			List <TRConfig> trEligibleList = new ArrayList<TRConfig>();
			if (materialCodes != null && materialCodes.size()>0) {
				trEligibleList = this.isTREligible(materialCodes);
			}
			List<TechnicalOrderDetail> processedList = new ArrayList<TechnicalOrderDetail>();
			TechnicalOrderDetail techDto = null;
			for(Map.Entry<String, TechnicalOrderDetail> dto: map.entrySet()){
				techDto = dto.getValue();
				//Set TOB Flag Yes for eligible assets
				if (trEligibleList != null && trEligibleList.size() >0){
	    			int count = 0;
	    			List <TRConfig> matchingTRConfigs = new ArrayList<TRConfig>();
					for (TRConfig trConfig : trEligibleList) {
						if (techDto.getMaterialCode().equals(trConfig.getParentMaterialCode())) {
							count++;
							matchingTRConfigs.add(trConfig);
						}
					}

					if(matchingTRConfigs != null && matchingTRConfigs.size() > 0){
						techDto.setTechnicallyRegisterable(GRTConstants.Y);
					} else {
						techDto.setTechnicallyRegisterable("");//empty for no
					}
	            }
				processedList.add(techDto);
			}
			return processedList;
		}
	    
	    /**
	     * Method to fetch technical registration Data.
	     * @param materialCode
	     * @param releaseString
	     * @return result Object[]
	     * @throws DataAccessException
	     */
	    public Object[] getLocalTechRegData(String materialCode, String releaseString)throws DataAccessException {
	    	logger.debug("Entering InstallBaseServiceImpl.getLocalTechRegData for MC:" + materialCode + " and releaseString:" + releaseString);
		    	try {
		    	List<TRConfig> trConfigs = this.getInstallBaseDao().getTRConfigData(materialCode);
		    	logger.debug("trConfigs:" + trConfigs);
		    	String productType = null;
		    	String seCode = null;
		    	if(trConfigs != null) {
		    		for (TRConfig config : trConfigs) {
		    			if(config != null) {
		    				logger.debug("config:" + config);
		        			List<String> releases = getReleasesByGroupId(config.getGroupId(), true);
		        			logger.debug("releases:" + releases);
		        			if(releases != null) {
		        				if(releases.contains(releaseString)) {
		        					logger.debug("release match Found");
		        					productType = config.getProductType();
		        					seCode = config.getSeCode();
		        					break;
		        				} else {
		        					logger.debug("release match NOT Found");
		        				}
		        			}
		        		}
					}
		    		if(StringUtils.isNotEmpty(productType)) {
		    			Object [] result = {materialCode, seCode, productType, releaseString};
		    			return result;
		    		} else {//Hack-remove it, once TR APIs are stable.
		    			Object [] result = {materialCode, "IPOLNX", "ipofc", "8.1"};
		    			return result;
		    		}
		    	}
	    	} finally {
	    		logger.debug("Exiting InstallBaseServiceImpl.getLocalTechRegData");
	    	}
	    	return null;
	    }
	    
	    public TRConfig loadTRConfig(String groupId) throws DataAccessException {
	    	TRConfig trConfig = null;
	    	try {
	    		mc2GroupIdMappings=this.getInstallBaseDao().initializeTRConfigData();
		    	trConfig = this.getInstallBaseDao().loadTRConfig(groupId);
	    	} catch(Throwable throwable) {
	    		logger.error("", throwable);
	    	}
	    	return trConfig;
	    }
	    
	    /**
	     * Method to update deleted sales out
	     * @param materialCodes List of material codes
	     * @param soldToId String
	     */
	    public void updateDeletedSalesOut(List<String> materialCodes,String soldToId){
	    	try {
				getInstallBaseDao().updateDeletedSalesOut(materialCodes, soldToId);
			} catch (DataAccessException e) {
				logger.debug("Exception while updating the deleted in Sales Out",e);
			}
	    }
	    
	    /**
	     * Submit Registration asynchronously, create siebel SR, generate attachment and attach it
	     * to the created SR
	     *
	     * @param registrationId
	     *            String
	     * @param soldToNumber
	     *            String
	     * @param result
	     *            List<TechnicalOrderDetail>
	     * @param generateFileInPath
	     *            String
	     * @param salMigrationOnlyFlag
	     *            Boolean
	     * @param skipInstallBaseCreation
	     *            Boolean
	     * @throws Exception
	     */

	    public String submitRegistration(String registrationId, String soldToId,
	    		List<TechnicalOrderDetail> result, String generateFileInPath,
	    		Boolean salMigrationOnlyFlag, Boolean skipInstallBaseCreation )
	    throws Exception {
	    	logger.debug("Entering InstallBaseServiceImpl.submitRegistration");
	    	StatusEnum status;
	    	String returnCode = null;
	    	if (skipInstallBaseCreation) {
	    	
	    		logger.debug("skipInstallBaseCreation : submitRegistration");
	    		status = StatusEnum.AWAITINGINFO;
	    		getInstallBaseDao().updateSiteRegistrationProcessStepAndStatus(
	    				registrationId, ProcessStepEnum.INSTALL_BASE_CREATION,
	    				status);
	    	
	    		getInstallBaseDao().updateSiteRegistrationSkipInstallBaseCreation(registrationId, skipInstallBaseCreation);
	    		
	    		logger.debug(" Past updateSiteRegistrationSkipInstallBaseCreation in reg controller : submitRegistration");
	    		sendRegistrationRequestAlert(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION, null);
	    		returnCode =GRTConstants.installBase_successCode;
	    	} else if (salMigrationOnlyFlag) {
	    		logger.debug("salMigrationOnlyFlag: submitRegistration");
	    		status = StatusEnum.COMPLETED;
	    		getInstallBaseDao().updateSiteRegistrationProcessStepAndStatus(
	    				registrationId, ProcessStepEnum.INSTALL_BASE_CREATION,
	    				status);
	    		getInstallBaseDao().updateSiteRegistrationSalMigrationOnlyFlag(registrationId, salMigrationOnlyFlag);
	    		//just a parts fix to st Inase to compete for sal migration
	    		Status toComplete = new Status();
	    		toComplete.setStatusId(StatusEnum.COMPLETED.getStatusId());
	    		getInstallBaseDao().updateSiteRegistrationStatusForDIrectUser(registrationId,toComplete);
	    		logger.debug(" Past updateSiteRegistrationSalMigrationOnlyFlag in reg controller : submitRegistration");
	    		sendRegistrationRequestAlert(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION, null);
	    		returnCode =GRTConstants.installBase_successCode;
	    	} else {
	    		logger.debug("Inprocess else case flag : submitRegistration");
	    		status = StatusEnum.INPROCESS;
	    		
	    		getInstallBaseDao().updateSiteRegistrationProcessStepAndStatus(
	    				registrationId, ProcessStepEnum.INSTALL_BASE_CREATION,
	    				status);
	    		logger.debug(" Past updateSiteRegistrationProcessStepAndStatus in reg controller : submitRegistration");
	    		
	        if(!salMigrationOnlyFlag){
	    		for (TechnicalOrderDetail detail : result) {
	    			detail.setSoldToId(soldToId);
	    			detail.setShipToId(soldToId);
	    		}
	    		InstallBaseCreationDto installBaseDto = new InstallBaseCreationDto();
	    		installBaseDto.setRegistrationId(registrationId);
	    		logger.debug("RegistrationId"+registrationId);
	    		installBaseDto.setDestination(getInstallBaseDao().getSapBox(soldToId));
	    		if(installBaseDto.getDestination()==null){
	    			returnCode = GRTConstants.sapdestination_notfound;
	    			return returnCode;
	    		}
	    		installBaseDto.setInstallBaseData(result);
	    		logger.debug("List size"+result.size());
	    	
	    		returnCode = this.getSapClient().installBaseCreation(installBaseDto);
	    	
	    		if (returnCode == null) {
	    			
	    			returnCode = GRTConstants.exception_errorcode;
	    		}else if(returnCode.equals(GRTConstants.installBase_successCode) || returnCode.equals(GRTConstants.sapDown_errorCode)){
	    			getInstallBaseDao().updateSiteRegistrationStatusOnRegId(registrationId, StatusEnum.INPROCESS, ProcessStepEnum.INSTALL_BASE_CREATION);
	    			this.updateSiteRegistrationSubmittedFlag(registrationId, GRTConstants.isSubmitted_true);
	    			sendRegistrationRequestAlert(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION, null);
	    		}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
	    			String errMsg="GRT unable to reach FMW";
	    			logger.debug(errMsg);
	    			// Updating the IB status to Saved
	    			status = StatusEnum.SAVED;
	    			getInstallBaseDao().updateSiteRegistrationProcessStepAndStatus(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION, status);
	    			//Mail to System Admin
	    			this.sendEmailToSystemAdmin(registrationId, errMsg, ProcessStepEnum.INSTALL_BASE_CREATION);
	    		}
	           }
	    	}
	    	logger.debug("Exiting InstallBaseServiceImpl : submitRegistration");
	    	return returnCode;
	    }
	    
	    /**
	     * Method to update SiteRegistration Submitted Flag
	     * @param registrationId String
	     * @param submitted String
	     */
	    public void updateSiteRegistrationSubmittedFlag(String registrationId,String submitted){
	    	try {
	    		getInstallBaseDao().updateSiteRegistrationSubmittedFlag(registrationId, submitted);
			} catch (DataAccessException e) {
				logger.debug("Exception while updating the submitted field",e);
			}
	    }
	    
	    /**
	     * Method to send email to system admin
	     * @param registrationId String
	     * @param errMsg String
	     * @param processStep ProcessStepEnum
	     */
	    public void sendEmailToSystemAdmin(String registrationId,String errMsg,ProcessStepEnum processStep){
	    	logger.debug("Entering InstallBaseServiceImpl.sendEmailToSystemAdmin.");
			 RegistrationRequestAlert result = null;
			 SiteRegistration siteRegistration = null;
			try {
				siteRegistration = getInstallBaseDao().getSiteRegistration(registrationId);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
			 if(siteRegistration !=null){
				 result = this.convert(siteRegistration, processStep);
				 result.setDestination(GRTConstants.SYSTEM_MAIL_DESTINATION_FMW);
				 result.setSendMail(false);
		        result.setIsSystemMail(true);
		        result.setActionRequired(errMsg);
		        try {
		        	this.getMailUtil().sendMailNotification(result);
		            logger.debug("Exiting InstallBaseServiceImpl.sendEmailToSystemAdmin.");
		        } catch(Exception e) {
		            logger.error("ERROR: Unexpected failure while trying send email with error: "
		                    + e.getMessage());
		        }
			 }
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
	        logger.debug("Entering InstallBaseServiceImpl.sendRegistrationRequestAlert starting for Registration ID [" 
	                + registrationId + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");
			SiteRegistration siteRegistration = getInstallBaseDao()
			        .getSiteRegistration(registrationId);
			RegistrationRequestAlert result = this
			        .convert(siteRegistration, processStep);
			if(status != null) {
				result.setStatus(status.getStatusShortDescription());
			}
	        result = populateActionRequired(result);
	        this.getMailUtil().sendMailNotification(result);
	        logger.debug("Exiting InstallBaseServiceImpl.sendRegistrationRequestAlert exiting for Registration ID [" 
	                + siteRegistration.getRegistrationId() + "] with status [" + (status == null? "null" : status.getStatusShortDescription()) + "]");

		}	
		/**
		 * method to convert SiteRegistration data to RegistrationRequestAlert for sending email.
		 * @param siteRegistration
		 * @param targetProcessStep
		 * @return result RegistrationRequestAlert
		 */
		public static RegistrationRequestAlert convert(SiteRegistration siteRegistration, ProcessStepEnum targetProcessStep) {
			logger.debug("Entering InstallBaseServiceImpl : convert SiteRegistration");		
			RegistrationRequestAlert result = new RegistrationRequestAlert();
			try {		
				result.setRegistrationId(siteRegistration.getRegistrationId());
				result.setSoldTo(siteRegistration.getSoldToId());
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
				else if(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId()
						.equalsIgnoreCase(processStepId)) {
					srRequest = siteRegistration.getEqrMoveSrRequest();
					result.setCompletedDate(siteRegistration.getEqrMoveCompletedDate());
				}
				
				result.setSiebelSRNumber(getSiebelSRNo(srRequest));
				result.setReportedDate(siteRegistration.getCreatedDate());
			
				if(siteRegistration.getRegistrationType() != null) {
					result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());
				}
			
				if(StringUtils.isNotEmpty(siteRegistration.getRegistrationIdentifier())) {
					result.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
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
				
				logger.debug("Exiting InstallBaseServiceImpl : convert SiteRegistration");
			} catch (Exception ex) {
				logger.debug("Error in InstallBaseServiceImpl : convert SiteRegistration : " + ex.getMessage());
				
			}
			return result;
		}
		
		/**
		 * method to populate action required
		 * @param result RegistrationRequestAlert
		 * @return result RegistrationRequestAlert
		 */
		private RegistrationRequestAlert populateActionRequired(RegistrationRequestAlert result) {
		        logger.debug("Entering InstallBaseServiceImpl : populateActionRequired()");
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
			            logger.error("InstallBaseServiceImpl.queryLatestPublicSRNoteActivity failure", e);
			        }
		        }
		        result.setActionRequired(actionRequired);
		        logger.debug("Exiting InstallBaseServiceImpl : populateActionRequired()");
		        return result;
		    }
		 	
			/**
			 * Method to get the Siebel SR Number.
			 * @param srRequest SRRequest
			 * @return siebelSRNo String
			 */
			private static String getSiebelSRNo(SRRequest srRequest) {
				logger.debug("Entering InstallBaseServiceImpl : getSiebelSRNo");
				if(srRequest == null || StringUtils.isEmpty(srRequest.getSiebelSRNo())) {
					logger.debug("Exiting RegistrationRequestAlertConvertor : getSiebelSRNo - SR Number Not Found");
					return GRTConstants.SIEBEL_SR_NUMBER_NOT_FOUND;
				}
				else {
					logger.debug("Exiting InstallBaseServiceImpl : getSiebelSRNo - SR Number Found");
					return srRequest.getSiebelSRNo();
				}
			}
		
			/**
			 * Method to get eligible groups.
			 * @param seCodes list of seCodes
			 * @return List of TRConfig
			 */
			public List<TRConfig> getEligibleGroups(List<String> seCodes) throws DataAccessException {
				logger.debug("Entering InstallBaseServiceImpl.getEligibleGroups");
				List<TRConfig> trconfigList = getInstallBaseDao().getEligibleGroups(seCodes);
				return groupTRConfigsOnGroupIdAndMC(trconfigList);
			}
			
			/**
			 * Method to group TRConfigs based on groupId and MC
			 * @param trConfigList
			 * @return List of TRConfig
			 */
			private List<TRConfig> groupTRConfigsOnGroupIdAndMC(List<TRConfig> trConfigList){
				logger.debug("Entering InstallBaseServiceImpl.groupTRConfigsOnGroupIdAndMC");
				TRConfig trConfigObj = null;
				// Return List object
				List<TRConfig> retList = new ArrayList<TRConfig>();
				// Map to hold TRConfig objects with group of SE Codes
				Map<String, TRConfig> mcGroupIdMap = new HashMap<String, TRConfig>();
				String mcGroupID = null;
				if(trConfigList != null && !trConfigList.isEmpty()){
					for(TRConfig trConfig:trConfigList){
						if(trConfig != null){
							// Framing Key Value
							mcGroupID = trConfig.getGroupId();
							if(mcGroupIdMap.size() > 0 && mcGroupIdMap.containsKey(mcGroupID)){
								trConfigObj = mcGroupIdMap.get(mcGroupID);
								// Setting Parental Material Code to show on UI
								if(trConfig.isMainMaterialCode()){
									trConfigObj.setParentMaterialCode(trConfig.getParentMaterialCode());
									trConfigObj.setParentMaterialCodeDescription(trConfig.getParentMaterialCodeDescription());
								}
								// Clubbing SE Codes seperated with comma
								trConfigObj.setSeCode(trConfigObj.getSeCode()+", "+trConfig.getSeCode());
								mcGroupIdMap.put(mcGroupID, trConfigObj);
							} else {
								mcGroupIdMap.put(mcGroupID, trConfig);
							}
						}
					}
				}
				// Constructing list to be displayed on the screen
				Set<String> seCodeSet = null;
				String tempSeCode = null;
				if(mcGroupIdMap != null && mcGroupIdMap.size() > 0){
					for(Map.Entry<String, TRConfig> entry : mcGroupIdMap.entrySet()){
						trConfigObj = entry.getValue();
						tempSeCode = "";
						if(trConfigObj.getSeCode() != null){
							seCodeSet = new HashSet<String>();
							String [] data = trConfigObj.getSeCode().split(", ");
							for(String seCode : data){
								if(!seCodeSet.isEmpty() && seCodeSet.contains(seCode)){
									// Duplicates
								} else {
									seCodeSet.add(seCode);
									tempSeCode += seCode + ", ";
								}
							}
						}
						trConfigObj.setSeCode(tempSeCode.substring(0, tempSeCode.length()-2));
						retList.add(trConfigObj);
					}
				}
				logger.debug("Exiting InstallBaseServiceImpl.groupTRConfigsOnGroupIdAndMC");
				return retList;
			}
			 
			/**
			 * Method to update SiteRegistration status for direct user
			 * @param registrationId
			 * @return changedStatus
			 */
			public String updateSiteRegistrationStatusForDIrectUser(String registrationId)  throws Exception{
			    	logger.debug("InstallBaseServiceImpl.updateSiteRegistrationStatusForDIrectUser");
			    	String changedStatus=null;
			    	 Status status = new Status();
			    	 status.setStatusId(StatusEnum.COMPLETED.getStatusId());
					 getInstallBaseDao().updateSiteRegistrationStatusForDIrectUser(registrationId, status);
					 SiteRegistration siteRegistration = getInstallBaseDao().getSiteRegistration(registrationId);
					 if(null!=siteRegistration){
					 status=siteRegistration.getInstallBaseStatus();
					 changedStatus=status.getStatusId();
					 }
					 sendRegistrationRequestAlert(siteRegistration, ProcessStepEnum.INSTALL_BASE_CREATION, StatusEnum.COMPLETED);
					 logger.debug("Exit InstallBaseServiceImpl.updateSiteRegistrationStatusForDIrectUser changedStatus"+changedStatus);
			    	return changedStatus;
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
			     * Save the Technical Order Detail List for fields MaterialCode,
			     * InitialQuantity and Description.
			     *
			     * @param materialEntryList
			     *            List<TechnicalOrderDetail>
			     * @return technicalOrderDetailList List<TechnicalOrderDetail>
			     * @throws DataAccessException
			     */
			    public List<TechnicalOrderDetail> saveMaterialEntryList(
			            List<TechnicalOrderDetail> materialEntryList)
			            throws DataAccessException {
			    	long c1 = Calendar.getInstance().getTimeInMillis();
			        List<TechnicalOrder> technicalOrderList = technicalOrderListConvertor(materialEntryList);
			        technicalOrderList = getInstallBaseDao().saveTechnicalOrderList(technicalOrderList);
			        List<TechnicalOrderDetail> list = technicalOrderDetailConvertor(technicalOrderList);
			        long c2 = Calendar.getInstance().getTimeInMillis();
			        logger.debug(c2-c1 +" milliseconds");
			        return list;
			    }
			    /**
			     * Method to convert technical orders
			     * @param materialEntryList
			     * @return technicalOrderList
			     */
			    private List<TechnicalOrder> technicalOrderListConvertor(
			            List<TechnicalOrderDetail> materialEntryList) {
			        List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();
			        for (TechnicalOrderDetail detail : materialEntryList) {
			            TechnicalOrder technicalOrder = technicalOrderConvertor(detail, detail.getRegistrationId());
			            technicalOrderList.add(technicalOrder);
			        }
			        return technicalOrderList;
			    }
			    /**
			     * TechnicalOrder Convertor implementation
			     * @param technicalOrderDetail
			     * @param registrationId
			     * @return technicalOrder
			     */
			    private TechnicalOrder technicalOrderConvertor(
			            TechnicalOrderDetail technicalOrderDetail, String registrationId) {
			        TechnicalOrder technicalOrder = new TechnicalOrder();
			        SiteRegistration siteRegistration = new SiteRegistration();
			        siteRegistration.setRegistrationId(registrationId);
			        technicalOrder.setOrderId(technicalOrderDetail.getOrderId());
			        technicalOrder.setSiteRegistration(siteRegistration);
			        technicalOrder.setMaterialCode(technicalOrderDetail.getMaterialCode());
			        /**  [AVAYA]: 09-15-2011 Setting Serial Number to retrieve in Registration Service (Start) **/
			        technicalOrder.setSerialNumber(technicalOrderDetail.getSerialNumber());
			        /**  [AVAYA]: 09-15-2011 Setting Serial Number to retrieve in Registration Service (End) **/
			        technicalOrder.setInitialQuantity(technicalOrderDetail.getInitialQuantity());
			        technicalOrder.setRemainingQuantity(technicalOrderDetail.getRemainingQuantity());
			        technicalOrder.setDescription(technicalOrderDetail.getDescription());
			        technicalOrder.setSolutionElementCode(technicalOrderDetail
			                .getSolutionElementCode());
			        technicalOrder.setOrderType(technicalOrderDetail.getOrderType());
			        if(technicalOrderDetail.getDeleted() != null && technicalOrderDetail.getDeleted()) {
			            technicalOrder.setDeleted(GRTConstants.YES);
			        }
			        else  {
			            technicalOrder.setDeleted(GRTConstants.NO);
			        }
			        technicalOrder.setMaterialExclusion(technicalOrderDetail.getMaterialExclusion());
			        technicalOrder.setIsSalesOut(technicalOrderDetail.getIsSalesOut());
			        //Begini line added for Serial Number in SOIR section
			        technicalOrder.setSerialNumber(technicalOrderDetail.getSerialNumber());
			        //End line added for Serial Number in SOIR section
			        technicalOrder.setReleaseString(technicalOrderDetail.getReleaseString());
			        technicalOrder.setIsBaseUnit(technicalOrderDetail.getIsBaseUnit());
			        technicalOrder.setAutoTR(technicalOrderDetail.isAutoTR());
			        return technicalOrder;
			    }
			    private List<TechnicalOrderDetail> technicalOrderDetailConvertor(
			            List<TechnicalOrder> technicalOrderList) {
			        List<TechnicalOrderDetail> technicalOrderDetailList = new ArrayList<TechnicalOrderDetail>();
			        for (TechnicalOrder to : technicalOrderList) {
			                TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
			                technicalOrderDetail.setOrderId(to.getOrderId());
			                technicalOrderDetail.setMaterialCode(to.getMaterialCode());
			                /**  [AVAYA]: 09-15-2011 Setting Serial Number to retrieve in Registration Service (Start) **/
			                technicalOrderDetail.setSerialNumber(to.getSerialNumber());
			                /**  [AVAYA]: 09-15-2011 Setting Serial Number to retrieve in Registration Service (End) **/
			                technicalOrderDetail.setInitialQuantity(to.getInitialQuantity());
			                technicalOrderDetail.setRemainingQuantity(to.getRemainingQuantity());
			                technicalOrderDetail.setDescription(to.getDescription());
			                technicalOrderDetail.setSolutionElementCode(to
			                        .getSolutionElementCode());
			                technicalOrderDetail.setOrderType(to.getOrderType());
			                if(StringUtils.isNotEmpty(to.getDeleted()) && GRTConstants.YES.equalsIgnoreCase(to.getDeleted())) {
			                    technicalOrderDetail.setDeleted(true);
			                }
			                else  {
			                    technicalOrderDetail.setDeleted(false);
			                }
			                technicalOrderDetail.setMaterialExclusion(to.getMaterialExclusion());
			                technicalOrderDetail.setIsSalesOut(to.getIsSalesOut());
			                technicalOrderDetail.setIsBaseUnit(to.getIsBaseUnit());
							technicalOrderDetailList.add(technicalOrderDetail);
			        }
			        return technicalOrderDetailList;
			    }
			    
			    /**
			     * Save the Site Registration.
			     *
			     * @param siteRegistration
			     * @param userId
			     * @return siteRegistration DTO
			     * @throws DataAccessException
			     */
			    public SiteRegistration saveSiteRegistration(SiteRegistration siteRegistration, String userId) throws DataAccessException {
			        logger.debug("Enetering InstallBaseServiceImpl: saveSiteRegistration");
			        try {
			        	setUserId(userId);
			            siteRegistration = this.saveSiteContactValidation(siteRegistration);
			        } catch(Throwable throwable) {
						logger.error("", throwable);
					} finally {
						logger.debug("Exiting InstallBaseServiceImpl.saveSiteRegistration");
					}

			        logger.debug("Exiting InstallBaseServiceImpl.saveSiteRegistration");
			        return siteRegistration;
			    }
			    /**
			     * Save the Site Registration.
			     *
			     * @param siteRegistration
			     * @return siteRegistration DTO
			     * @throws DataAccessException
			     */
			    public SiteRegistration saveSiteContactValidation(
			            SiteRegistration siteRegistration)
			            throws DataAccessException {
			        logger
			                .debug("Entering InstallBaseServiceImpl : saveSiteContactValidation");
			        SiteRegistration siteRegistrationDomain = new SiteRegistration();
			        TechnicalOrder technicalOrder = null;
			        RegistrationQuestions regQstns = null;
			        Set<TechnicalOrder> technicalOrderSet = new HashSet<TechnicalOrder>();
			        Set<RegistrationQuestions> regQuestionsSet = new HashSet<RegistrationQuestions>();
			       
			        List<TechnicalOrderDetail> materialEntryList = null;
			        List<RegistrationQuestionsDetail> regQstnsList =null;

			        if(StringUtils.isNotEmpty(siteRegistration.getRegistrationId())) {
			            siteRegistrationDomain.setRegistrationId(siteRegistration
			                .getRegistrationId());
			        }
			        siteRegistrationDomain.setSiteCountry(siteRegistration.getSiteCountry());
			        siteRegistrationDomain.setAddress(siteRegistration.getAddress());
			        siteRegistrationDomain.setAddress2(siteRegistration.getAddress2());
			        siteRegistrationDomain.setCity(siteRegistration.getCity());
			        siteRegistrationDomain.setState(siteRegistration.getState());
			        siteRegistrationDomain.setZip(siteRegistration.getZip());
			        siteRegistrationDomain.setFirstName(siteRegistration.getFirstName());
			        siteRegistrationDomain.setLastName(siteRegistration.getLastName());
			        siteRegistrationDomain.setReportPhone(siteRegistration.getPhone());
			        siteRegistrationDomain.setReportEmailId(siteRegistration.getEmail());
			        siteRegistrationDomain.setCreatedDate(new Date());
			        siteRegistrationDomain
			                .setOnsitePhone(siteRegistration.getOnsiteEmail());
			        siteRegistrationDomain.setCompany(siteRegistration.getCompany());
			        siteRegistrationDomain
			                .setOnsiteEmail(siteRegistration.getOnsiteEmail());
			        siteRegistrationDomain.setOnsiteFirstName(siteRegistration
			                .getOnsiteFirstName());
			        siteRegistrationDomain.setOnsiteLastName(siteRegistration
			                .getOnsiteLastName());
			        siteRegistrationDomain
			                .setOnsitePhone(siteRegistration.getOnsitePhone());
			        siteRegistrationDomain.setSoldToId(siteRegistration.getSoldToId());
			        siteRegistrationDomain.setCompanyPhone(siteRegistration
			                .getCompanyPhone());
			        siteRegistrationDomain.setRegion(siteRegistration.getRegion());
			        siteRegistrationDomain.setSoldToLocation(siteRegistration
			                .getSoldToLocation());
			        siteRegistrationDomain.setSoldToType(siteRegistration.getSoldToType());
			        siteRegistrationDomain.setUserName(siteRegistration
			                .getUserName());
			        siteRegistrationDomain.setSalMigrationOnly(GRTConstants.NO);
			        siteRegistrationDomain.setUserRole(siteRegistration.getUserRole());
			        ProcessStep processStep = new ProcessStep();
			        processStep.setProcessStepId(siteRegistration.getProcess_Step_Id());
			        siteRegistrationDomain.setProcessStep(processStep);
			        siteRegistrationDomain.setTypeOfImplementation(siteRegistration.getTypeOfImpl());

			        RegistrationType registrationType = new RegistrationType();
			        registrationType.setRegistrationId(siteRegistration.getRegistrationType().getRegistrationId());
			        siteRegistrationDomain.setRegistrationType(registrationType);

			        Status status = new Status();
			        status.setStatusId(siteRegistration.getStatus_id());
			        siteRegistrationDomain.setStatus(status);
			        siteRegistrationDomain.setExpedite(siteRegistration.getExpedite());
			        // send mail flag
			        siteRegistrationDomain.setSendMail(siteRegistration.getSendMail());
			        siteRegistrationDomain.setInventoryFile(siteRegistration.getInventoryXML());
			        siteRegistrationDomain.setIpoAccessType(siteRegistration.getIpoAccessType());
			        //[AVAYA] GRT2.0 Setting cut over date  (Start)
			       /* Date cutOverDate = DateUtil.getDateFromDateStr(siteRegistration.getCuttimendate(),"MM/dd/yyyy");
			        siteRegistrationDomain.setCut_over_date(cutOverDate);*/
			        //[AVAYA] GRT2.0 Setting cut over date  (End)
			        siteRegistrationDomain.setSubmitted(siteRegistration.getSubmitted());
			        siteRegistrationDomain.setIsSrCompleted(siteRegistration.getIsSrCompleted());
			        materialEntryList = siteRegistration.getMaterialEntryList();

			        for (TechnicalOrderDetail detail : materialEntryList) {
			            technicalOrder = new TechnicalOrder();
			            if (StringUtils.isNotEmpty(detail.getOrderId())) {
			                technicalOrder.setOrderId(detail.getOrderId());
			            }
			            technicalOrder.setSiteRegistration(siteRegistrationDomain);
			            technicalOrder.setMaterialCode(detail.getMaterialCode());
			            technicalOrder.setInitialQuantity(detail.getInitialQuantity());
			            /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (Start) **/
			            logger.debug("Setting Serial Number to: saveSiteContactValidation "+detail.getSerialNumber());
			            technicalOrder.setSerialNumber(detail.getSerialNumber());
			            /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (End) **/
			            technicalOrder.setRemainingQuantity(detail.getInitialQuantity());
			            technicalOrder.setDescription(detail.getDescription());
			            technicalOrder.setMaterialCode(detail.getMaterialCode());
			            //[LAVANYA K] code chage start here for Dropdown Meterial code issue
			          
			            logger.debug("1 ***************** befoer inside the meretila code dropdoem issue detail.getIsSourceIPO()");
			            	 if(siteRegistration.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.IPOFFICE.getRegistrationID())){
			            	 logger.debug("inside check of typeimpl 5  ***************** "+detail.getIsSourceIPO());
			            	 List<TRConfig> trConfigs = this.getInstallBaseDao().getTRConfigData(detail.getMaterialCode());
			            	 String seCode = null;
			            	 if(trConfigs != null && trConfigs.size() > 0 && trConfigs.get(0) != null) {
			            		 seCode = trConfigs.get(0).getSeCode();
			            	 }

			            	logger.debug("  *****************  seCode "+seCode);
			                if(StringUtils.isNotEmpty(seCode)){
			                	logger.debug(" ******************* seCode is not null "+seCode);
			                	technicalOrder.setSolutionElementCode(seCode);
			            	}else{
			            		logger.debug(" ************************* seCode is null"+seCode);
			            		technicalOrder.setRemainingQuantity(Long.valueOf(0));
			            }
			            }else{
			            	logger.debug("*******************else nota typeimp IPO ");
			           	 	technicalOrder.setSolutionElementCode(detail.getSolutionElementCode());
			            }
			            Object[] TechregData = this.getLocalTechRegData(detail.getMaterialCode(), "8.1");
			            if(TechregData!=null){
			            	logger.debug("Setting IPO Eligible to true");
			         	   technicalOrder.setIsIPOEligible("true");
			            }else{
			            	logger.debug("Setting IPO Eligible to true");
			            	technicalOrder.setIsIPOEligible("false");
			            }

			            //[LAVANYA K] code chage ends here for Dropdown Meterial code issue
			            technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_IB);
			            technicalOrder.setDeleted(GRTConstants.NO);
			            technicalOrder.setIsBaseUnit(detail.getIsBaseUnit());
			            technicalOrder.setIsSourceIPO(detail.getIsSourceIPO());
			            technicalOrder.setReleaseString(detail.getReleaseString());
			            technicalOrder.setMaterialExclusion(detail.getMaterialExclusion());
			            technicalOrder.setIsSalesOut(detail.getIsSalesOut());
			            technicalOrder.setAutoTR(detail.isAutoTR());
			            if(detail.getTechnicallyRegisterable() != null && detail.getTechnicallyRegisterable().equalsIgnoreCase("Y")){
			            	technicalOrder.setHasActiveEquipmentContract(1L);
			            }
			            technicalOrderSet.add(technicalOrder);
			        }
			        siteRegistrationDomain.setTechnicalOrders(technicalOrderSet);

			        regQstnsList = siteRegistration.getRegQuestnList();

			        for (RegistrationQuestionsDetail Qstndetail : regQstnsList) {
			        	regQstns = new RegistrationQuestions();
			            if (StringUtils.isNotEmpty(Qstndetail.getRegId())) {
			            	regQstns.setRegId(Qstndetail.getRegId());
			            }
			            regQstns.setSiteRegistration(siteRegistrationDomain);
			            regQstns.setQuestionKey(Qstndetail.getQuestionKey());
			            regQstns.setAnswerKey(Qstndetail.getAnswerKey());
			            regQuestionsSet.add(regQstns);
			        }
			        siteRegistrationDomain.setRegistrationQuestions(regQuestionsSet);

			        siteRegistrationDomain.setUpdatedBy(siteRegistration.getUpdatedBy());
			        siteRegistrationDomain.setUpdatedDate(siteRegistration.getUpdatedDate());
			        siteRegistrationDomain.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
			        siteRegistrationDomain.setRegistrationNotes(siteRegistration.getNotes());
			        

			        // Save the Site Registration
			        Long registrationId = getInstallBaseDao()
			                .saveSiteContactValidation(siteRegistrationDomain);

			        siteRegistration.setRegistrationId(registrationId.toString());
			        logger.debug("Exiting InstallBaseServiceImpl : saveSiteContactValidation");
			        return siteRegistration;
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
			    	long c1 = Calendar.getInstance().getTimeInMillis();
			        List<TechnicalOrder> technicalOrderDetail = getInstallBaseDao()
			                .getTechnicalOrderByType(registrationId, orderType);
			        List<TechnicalOrderDetail> technicalOrderDtoList = prepareTechnicalOrderDetailList(technicalOrderDetail, orderType);
			        long c2 = Calendar.getInstance().getTimeInMillis();
			        logger.debug(c2-c1 +" milliseconds");
			        return technicalOrderDtoList;
			    }
			    private List<TechnicalOrderDetail> prepareTechnicalOrderDetailList(List<TechnicalOrder> technicalOrderDetailList, String orderType){
			    	List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
			        TechnicalOrderDetail technicalOrderDto = null;

			        if (technicalOrderDetailList != null) {
			            for (TechnicalOrder technicalOrder : technicalOrderDetailList) {
			                technicalOrderDto = new TechnicalOrderDetail();
			                technicalOrderDto.setRegistrationId(technicalOrder
			                        .getSiteRegistration().getRegistrationId());
			                technicalOrderDto.setMaterialCode(technicalOrder
			                        .getMaterialCode().trim());
			                technicalOrderDto.setInitialQuantity(technicalOrder
			                        .getInitialQuantity());
			                /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (Start) **/
			                logger.debug("Setting Serial Number to: getTechnicalOrderByType "+technicalOrder.getSerialNumber());
			                technicalOrderDto.setSerialNumber(technicalOrder.getSerialNumber());
			                /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (End) **/
			                technicalOrderDto.setRemainingQuantity(technicalOrder
			                        .getRemainingQuantity());
			                technicalOrderDto.setOrderId(technicalOrder.getOrderId());
			                technicalOrderDto.setProcessStep(technicalOrder
			                        .getSiteRegistration().getProcessStep()
			                        .getProcessStepId());
			                technicalOrderDto.setStatusId(technicalOrder
			                        .getSiteRegistration().getStatus().getStatusId());
			                technicalOrderDto.setDescription(technicalOrder
			                        .getDescription());
			                technicalOrderDto.setOrderType(technicalOrder
			                        .getOrderType());
			                technicalOrderDto.setIsBaseUnit(technicalOrder.getIsBaseUnit());
			                technicalOrderDto.setIsSourceIPO(technicalOrder.getIsSourceIPO());
			                technicalOrderDto.setIpoFlagIbaseJsp(technicalOrder.isIPO());
			                technicalOrderDto.setReleaseString(technicalOrder.getReleaseString());
			                technicalOrderDto.setErrorDescription(technicalOrder.getError_Desc());
			                if(StringUtils.isNotEmpty(technicalOrder.getDeleted()) &&
			                        GRTConstants.YES.equalsIgnoreCase(technicalOrder.getDeleted())) {
			                    technicalOrderDto.setDeleted(true);
			                }
			                else {
			                    technicalOrderDto.setDeleted(false);
			                }
			                technicalOrderDto.setIsSalesOut(technicalOrder.getIsSalesOut());
			                if(technicalOrder.getRemainingQuantity() != null && StringUtils.isNotEmpty(technicalOrder.getRemainingQuantity().toString())){
			                	technicalOrderDto.setRemovedQuantity(technicalOrder.getInitialQuantity() - technicalOrder.getRemainingQuantity());
			                }
			                technicalOrderDto.setEquipmentNumber(technicalOrder.getEquipmentNumber());
			                technicalOrderDto.setSummaryEquipmentNumber(technicalOrder.getSummaryEquipmentNumber());
			                technicalOrderDto.setSolutionElementCode(technicalOrder.getSolutionElementCode());
			                technicalOrderDto.setSolutionElementId(technicalOrder.getSeid());
			                technicalOrderDto.setProductLine(technicalOrder.getProductLine());
			                technicalOrderDto.setSerialNumber(technicalOrder.getSerialNumber());
			                if(technicalOrder.getHasActiveEquipmentContract() != null && technicalOrder.getHasActiveEquipmentContract() == 1){
			                	technicalOrderDto.setTechnicallyRegisterable(GRTConstants.YES);
			                } else {
			                	technicalOrderDto.setTechnicallyRegisterable("");
			                }
			                if(technicalOrder.getHasActiveSiteContract() != null && technicalOrder.getHasActiveSiteContract() == 1){
			                	technicalOrderDto.setActiveContractExist(GRTConstants.YES);
			                } else {
			                	technicalOrderDto.setActiveContractExist("");
			                }

			                technicalOrderDto.setAssetPK(technicalOrder.getAssetPK());
			                technicalOrderDto.setSid(technicalOrder.getSid());
			                technicalOrderDto.setMid(technicalOrder.getMid());
			                technicalOrderDto.setPipelineIBQuantity(0L);
			                technicalOrderDto.setPipelineEQRQuantity(0L);
			                if(GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)){
				                if(StringUtils.isNotEmpty(technicalOrder.getMaterialExclusion()) 
				                		&& !GRTConstants.EXCLUSION_SOURCE_NMPC.equalsIgnoreCase(technicalOrderDto.getExclusionSource())){
				                	technicalOrderDto.setExclusionSource(technicalOrder.getMaterialExclusion());
				                	technicalOrderDto.setExclusionFlag(true);
				                	if(StringUtils.isNotEmpty(technicalOrderDto.getExclusionSource()) 
											&& GRTConstants.EXCLUSION_SOURCE_PLDS.equalsIgnoreCase(technicalOrderDto.getExclusionSource())){
				                		technicalOrderDto.setErrorDescription("Install Base automatically fed from PLDS; move to technical onboarding or manage in PLDS.");
									} else {
										technicalOrderDto.setErrorDescription("Offer/Tracking Codes do not qualify for Services Install Base and thus cannot be registered.");
									}
				                } else {
				                	technicalOrderDto.setExclusionFlag(false);
				                	technicalOrderDto.setErrorDescription(technicalOrder.getError_Desc());
				                }
			                }
			                technicalOrderDto.setSalGateway(technicalOrder.isSalGateway());
			                technicalOrderDtoList.add(technicalOrderDto);
			            }
			        }
			        return technicalOrderDtoList;
			    }
			    
			    /**
			     * method to delete TechnicalOrders
			     * @param orderIdList
			     */
			    public void deleteTechnicalOrders(List<String> orderIdList){
			    	try {
			    			getInstallBaseDao().deleteTechnicalOrders(orderIdList);
					} catch (DataAccessException e) {
						logger.debug("Exception while deleting the technical order",e);
					}
			    }
			    
			    /**
			     * method to construct SiteRegistration from from data.
			     * @param form RegistrationFormBean
			     * @return siteRegistrationDto
			     * @throws IOException
			     */
			    public SiteRegistration assemblerSiteRegistrationFromFormBean(RegistrationFormBean form) throws IOException {
					SiteRegistration siteRegistrationDto = new SiteRegistration();
					siteRegistrationDto.setRegistrationId(form.getRegistrationId());
					siteRegistrationDto.setSoldToId(form.getSoldToId());
					siteRegistrationDto.setAddress(form.getAddress1());
					siteRegistrationDto.setAddress2(form.getAddress2());
					siteRegistrationDto.setCity(form.getCity());
					siteRegistrationDto.setState(form.getState());
					siteRegistrationDto.setZip(form.getZip());
					siteRegistrationDto.setSiteCountry(form.getSiteCountry());
					siteRegistrationDto.setFirstName(form.getFirstName());
					siteRegistrationDto.setLastName(form.getLastName());
					siteRegistrationDto.setPhone(form.getReportedPhone());
					siteRegistrationDto.setEmail(form.getReportedEmail());
					siteRegistrationDto.setOnsiteFirstName(form.getOnsiteFirstname());
					siteRegistrationDto.setOnsiteLastName(form.getOnsiteLastname());
					siteRegistrationDto.setOnsitePhone(form.getOnsitePhone());
					siteRegistrationDto.setCompanyPhone(form.getCompanyPhone());
					siteRegistrationDto.setRegion(form.getRegion());
					siteRegistrationDto.setCompany(form.getCompany());
					siteRegistrationDto.setOnsiteEmail(form.getOnsiteEmail());
					siteRegistrationDto.setSendMail(form.getSendMail());
					siteRegistrationDto.setUserName(form.getUserId());
					siteRegistrationDto.setExpedite(form.getExpedaite());
					//To-Do get the sold to location from sales out table.
					siteRegistrationDto.setSoldToLocation("SoldToLocationTesting");
					//To-Do get the sold to Type from sales out table.
					siteRegistrationDto.setSoldToType("SoldToTypeTesting");
					// To-Do get the Process Step Id from sales out table.
					siteRegistrationDto.setProcess_Step_Id(GRTConstants.INSTALL_BASE_CREATION);
					//To-Do get the Status Id from sales out table
					siteRegistrationDto.setStatus_id(GRTConstants.IN_PROCESS);
					siteRegistrationDto.setMaterialEntryList(form.getMaterialEntryList());
					siteRegistrationDto.setRegQuestnList(form.getRegQstnsDtl());
					siteRegistrationDto.setSubmitted(form.getSubmitted());
					siteRegistrationDto.setUserRole(form.getUserRole());
					siteRegistrationDto.setIsSrCompleted(form.getIsSrCompleted());
					if(form.isIpoRegistration()){
						siteRegistrationDto.setIpoAccessType(form.getIpoAccessType());
						if(form.getIpoFile()!=null){
						siteRegistrationDto.setInventoryXML(readFile(form.getIpoFile().toString()));
						}
					}
					return siteRegistrationDto;
				}
			    /**
			     * method to read from file.
			     * @param file
			     * @return file content
			     * @throws IOException
			     */
			    @SuppressWarnings("resource")
				public static String readFile(String file)
			    		throws IOException {
			    	StringBuilder sb= new StringBuilder();
			    try
			    {
			    	File f=new File(file);
			    	if(f.exists()){
			    		
				    	FileInputStream  fileInputStream = new FileInputStream(file);
				    		int line;
				    		while ((line = (fileInputStream).read()) != -1) {
				    		sb.append((char)line + "\n");
				    		}	
			    	}	
			    		
			    } catch (IOException e) {
					e.printStackTrace();
				}
			    		return sb.toString();
			    	}
			    
			  
			    
			    /**
			     * Get SALES_OUT based on soldToNumber and convert it to Technical Order
			     * Detail List
			     *
			     * @param soldToNumber
			     *            String
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> getPendingOrders(String soldToNumber, String bpLinkId) throws Exception {
			    	logger.debug("Entering InstallBaseServiceImpl.getPendingOrders for soldToId:" + soldToNumber + " BPLinkId:" + bpLinkId);
			    	List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
			    	try {
				        List<Object[]> pendingOrders = this.getInstallBaseDao().getPendingOrders(soldToNumber, bpLinkId);
				        TechnicalOrderDetail technicalOrderDto = null;
				        if (pendingOrders != null) {
				            for (Object[] salesOut : pendingOrders) {
				                technicalOrderDto = new TechnicalOrderDetail();
				                String materialCode = removeLeadingZeros((String) salesOut[0]);
				                technicalOrderDto.setMaterialCode(materialCode);
								List<TRConfig> trEligibleList = this.isTREligible(technicalOrderDto.getMaterialCode());
								if(trEligibleList != null && trEligibleList.size() > 0){
									technicalOrderDto.setTechnicallyRegisterable(GRTConstants.Y);
								} else {
									technicalOrderDto.setTechnicallyRegisterable("");//empty for no
								}
				                technicalOrderDto.setProductLine((String) salesOut[2]);
				                technicalOrderDto.setSelectforRegistration(false);
				                technicalOrderDtoList.add(technicalOrderDto);
				            }
				        }
			    	} catch(Throwable throwable) {
			    		logger.error("", throwable);
			    	} finally {
			    		logger.debug("Exiting InstallBaseServiceImpl.getPendingOrders for soldToId:" + soldToNumber + " BPLinkId:" + bpLinkId);
			    	}
			        return technicalOrderDtoList;
			    }
			    
			    private String removeLeadingZeros(String mcWithZeros){
			    	String materialCode = mcWithZeros;
			    	int indexOfNum = -1;
			    	if(null != materialCode){
			    		for(int i=0; i < materialCode.length(); i++ ){
			    			if( materialCode.charAt(i) == '0'){
			    				continue;
			    			} else {
			    				indexOfNum = i;
			    				break;
			    			}
			    		}
			    		if(indexOfNum != -1){
			    			materialCode = materialCode.substring(indexOfNum);
			    		}
			    	}
			    	return materialCode;
			    }
			    /**
			     * Get the TechnicalOrderDetail list based on the uploaded Material Entry
			     * Excel worksheet
			     *
			     * @param fileToParse
			     *            InputStream
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> parseWorksheetXLSX(InputStream fileToParse)
			            throws Exception {
			        return getWorksheetProcessor().parseWorksheetXLSX(fileToParse);
			    }
			    public TechnicalOrderDetailWorsheetProcessor getWorksheetProcessor() {
			        if (worksheetProcessor == null) {
			            worksheetProcessor = new TechnicalOrderDetailWorsheetProcessor();
			        }
			        return worksheetProcessor;
			    }
			    /**
			     * Get the TechnicalOrderDetail list based on the uploaded Material Entry
			     * Excel worksheet
			     *
			     * @param fileToParse
			     *            InputStream
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> parseWorksheet(InputStream fileToParse)
			            throws Exception {
			        return getWorksheetProcessor().parseWorksheet(fileToParse);
			    }
			    
			    /**
			     * Get the TechnicalOrderDetail list based on the uploaded Material Entry
			     * Excel worksheet
			     *
			     * @param fileToParse
			     *            InputStream
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> parseRecordValidationWorksheetXLSX(InputStream fileToParse)
			            throws Exception {
			        return getWorksheetProcessor().parseRecordValidationWorksheetXLSX(fileToParse);
			    }
			   
			    /**
			     * Get the TechnicalOrderDetail list based on the uploaded Material Entry
			     * Excel worksheet
			     *
			     * @param fileToParse
			     *            InputStream
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> parseRecordValidationWorksheet(InputStream fileToParse)
			            throws Exception {
			        return getWorksheetProcessor().parseRecordValidationWorksheet(fileToParse);
			    }
			    
			    /**
			     * Get the TechnicalOrderDetail list based on the uploaded Material Entry
			     * Excel worksheet
			     *
			     * @param filePath
			     *            String
			     * @return List<TechnicalOrderDetail>
			     * @throws Exception
			     */
			    public List<TechnicalOrderDetail> parseWorksheet(String filePath)
			            throws Exception {
			        return getWorksheetProcessor().parseWorksheet(filePath);
			    }
			   
			    /**
			     * method to cancel SiteRegistration IB Status
			     * @param registrationId
			     * @throws Exception
			     */
			    public void cancelSiteRegistrationIBStatus(String registrationId)  throws Exception{
			    	logger.debug("Entering InstallBaseServiceImpl.cancelSiteRegistrationStatus");

			    	 	Status status = new Status();
					    status.setStatusId(StatusEnum.CANCELLED.getStatusId());
					    status.setStatusDescription(StatusEnum.CANCELLED.getStatusDescription());
					    getInstallBaseDao().cancelSiteRegistrationIBStatus(registrationId, status);
					    logger.debug("Exiting InstallBaseServiceImpl.cancelSiteRegistrationStatus");
			    }
			    
	/*public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		InstallBaseService installBaseService = (InstallBaseService) context.getBean("installBaseService");
		
		/*List<String> materialCodes = new ArrayList<String>();
		materialCodes.add("270393");
		Map<String, MaterialCode> retVal = installBaseService.getMaterialCodeForValidation(materialCodes);
				
        for (String key : retVal.keySet()) {
            System.out.println("Key = " + key + "      Value = " + retVal.get(key).getDescription());
        }     */
        
  
		/* String registrationId="6879538";
	    	SiteRegistration siteRegistration=new SiteRegistration();
			try {
				siteRegistration = installBaseService.getSiteRegistrationOnRegId(registrationId);
				System.out.println("Site Registration Sold To:" +siteRegistration.getSoldToId());
			} catch (DataAccessException e) {
				e.printStackTrace();
			}*/
		
	/*	List<RegistrationSummary> list=installBaseService.getRegistrationSummaryList("partn14");
	    	System.out.println("list size"+list.size());*/
		
	/*Map parameterMap = new HashMap();
	String requesterFullName="John Smith";
	parameterMap.put("assembler_f_RegistrationId", "");
	//parameterMap.put("assembler_f_RequesterName", "");
	parameterMap.put("assembler_f_GrtNotificationName", "");
	parameterMap.put("assembler_f_GrtNotificationEmail", "");
	parameterMap.put("assembler_f_CreateDate", "");
	parameterMap.put("assembler_f_LastUpdatedDate", "");
	parameterMap.put("assembler_f_LastUpdatedBy", "");
	parameterMap.put("assembler_f_RegistrationType", "");
	parameterMap.put("assembler_f_InstallBaseStatus", "");
	parameterMap.put("assembler_f_TechnicalOnboardingStatus", "");
	parameterMap.put("assembler_f_EquipmentRemovalStatus", "");
	parameterMap.put("assembler_f_SoldTO", "");
	parameterMap.put("assembler_f_CustomerName", "");
	parameterMap.put("assembler_f_ActiveRelatedSR", "");
	parameterMap.put("assembler_f_RequesterName", requesterFullName);
	parameterMap.put("assembler_f_a", "");
	
	String userType ="B";
	String bpLinkId="0000000435";
	boolean ipoFilter=false; */
	/*PaginationForSiteRegistration dto=	installBaseService.getRegListFromDB(parameterMap, "partn14", requesterFullName, userType, bpLinkId, ipoFilter);
	System.out.println("PaginationForSiteRegistration::"+dto.getMaxSize());
	}*/

	/* START :: System to System Installer SALGW */
	
	/**
	 * method to process SALGateway IB Creation
	 * @param salInstallerDto
	 * @return SALGatewayInstallerResponseDto
	 */
	@Override
	//Create the install base for the Async System to System SALGW installer 
	public SALGatewayInstallerResponseDto processSALGatewayIBCreation(
			SALGatewayInstallerDto salInstallerDto) {
		SALGatewayInstallerResponseDto response = new SALGatewayInstallerResponseDto();
		String registrationId = null;
    	int flag = 0;
    	try {
	    	if(salInstallerDto!= null) {
	    		String soldToId = salInstallerDto.getSoldToId();
	    		String userId = salInstallerDto.getUserId();
	    		logger.debug("soldToId:"+soldToId+"    userId:"+userId);
		    	// Framing and inserting SiteRegistration details
		    	registrationId = this.getRegistrationId();
		    	logger.debug("RegistrationId created for SAL Gateway IB creation process: "+registrationId);
		    	// Getting Account information for the soldTo
		    	SiteRegistration siteRegistration = populateAccountInformationForSoldTo(soldToId);

		    	siteRegistration.setRegistrationId(registrationId);
		    	siteRegistration.setSoldToId(soldToId);
		    	//To-Do get the sold to location from sales out table.
				siteRegistration.setSoldToLocation("SoldToLocationTesting");
				//To-Do get the sold to Type from sales out table.
				siteRegistration.setSoldToType("SoldToTypeTesting");
		    	siteRegistration.setOnsiteEmail(salInstallerDto.getEmailID());
		    	// Setting IB Status
		    	Status status = new Status();
		    	status.setStatusId(StatusEnum.INPROCESS.getStatusId());
		    	siteRegistration.setStatus(status);
		    	siteRegistration.setInstallBaseStatus(status);
		    	// Setting process step as IB
		    	ProcessStep processStep = new ProcessStep();
		    	processStep.setProcessStepId(GRTConstants.INSTALL_BASE_CREATION);
		    	siteRegistration.setProcessStep(processStep);

		    	// Setting registration type as IB
		    	RegistrationType regType = new RegistrationType();
		    	//TODO check registration type
		    	regType.setRegistrationId(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID());
		    	siteRegistration.setRegistrationType(regType);

		    	siteRegistration.setCreatedBy(GRTConstants.USER_ID_SYSTEM);
		    	siteRegistration.setCreatedDate(new Date());
		    	siteRegistration.setUpdatedBy(GRTConstants.USER_ID_SYSTEM);
		    	siteRegistration.setUpdatedDate(new Date());
		    	siteRegistration.setFirstName(GRTConstants.USER_ID_SYSTEM);
		    	siteRegistration.setLastName("");
		    	siteRegistration.setOnsiteFirstName(GRTConstants.USER_ID_SYSTEM);
		    	siteRegistration.setOnsiteLastName("");
		    	flag = this.saveSiteRegistrationDetails(siteRegistration);

		    	// Fetch the material code details from input and submit for IB creation
		    	if(flag == 1){
		    		logger.debug("Successfully created Site Registration.");
		    		// Constructing TechnicalOrderDetail
		    		TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
		    		technicalOrderDetail.setSoldToId(soldToId);
		    		technicalOrderDetail.setShipToId(soldToId);
		    		technicalOrderDetail.setInitialQuantity(new Long(1));
		    		//TODO check order type
		    		technicalOrderDetail.setOrderType(GRTConstants.TECH_ORDER_TYPE_IB);
		    		technicalOrderDetail.setMaterialCode(salInstallerDto.getMaterialCode());
		    		technicalOrderDetail.setSerialNumber(salInstallerDto.getSerialNumber());
		    		technicalOrderDetail.setReleaseString(salInstallerDto.getReleaseNumber());
		    		technicalOrderDetail.setSolutionElementCode(salInstallerDto.getSeCode());
		    		technicalOrderDetail.setProductId(salInstallerDto.getProductIdentifier());

		    		// Save TechnicalOrderDetails
					TechnicalOrder technicalOrder = saveTechnicalOrderDetail(technicalOrderDetail, siteRegistration);
					if(technicalOrder != null)
						logger.debug("TechnicalOrder creation successful.");
					else
						logger.debug("TechnicalOrder creation failed.");
					//Begin IB Creation as part of the Full Registration
					InstallBaseCreationDto installBaseDto = new InstallBaseCreationDto();
					installBaseDto.setRegistrationId(siteRegistration.getRegistrationId());
					String destination = getInstallBaseDao().getSapBox(siteRegistration.getSoldToId());
					installBaseDto.setDestination(destination);
					List<TechnicalOrderDetail> toList = new ArrayList<TechnicalOrderDetail>();
					toList.add(technicalOrderDetail);
					installBaseDto.setInstallBaseData(toList);
					this.installBaseCreationAsync(installBaseDto);
					if( isIBSuccessful ){
						response.setStatus(GRTConstants.installBase_successCode);
			    		response.setRegistrationId(registrationId);
					}else{
						response.setStatus(GRTConstants.exception_errorcode);
			    		response.setRegistrationId(registrationId);
					}
		    	} else {
		    		logger.debug("Error while creating Site Registration in SAL Gateway IB Creation.");
		    	}
	    	}
    	} catch(Throwable throwable) {
    		logger.error("Error while creating SAL Gateway Install Base.", throwable);
    		response.setStatus(GRTConstants.installBase_errorCode);
    		response.setErrorDescription("Unknown Error.");
    	}  
    	logger.debug("Exiting RegistrationService >>> processSALGatewayIBCreation");
    	return response;
	}

	/**
	 * method to save TechnicalOrderDetail
	 * @param technicalOrderDetail
	 * @param siteRegistration
	 * @return technicalOrder
	 * @throws Exception
	 */
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
		technicalOrder.setSerialNumber(technicalOrderDetail.getSerialNumber());
		technicalOrder.setSiteRegistration(siteRegistration);
		//Ask what are the values to be populated for these three columns
		technicalOrder.setIsSelected(1L);
		technicalOrder.setHasActiveEquipmentContract(0L);
		technicalOrder.setHasActiveSiteContract(0L);
		logger.debug("@@@@@@@@@@@@@@@@@@@@@@  BEFORE SAVING THE TECHNICAL ORDER FOR SAL GATEWAY"  );
		String orderId = getBaseHibernateDao().saveTechnicalOrderForSALGateway(technicalOrder);
		logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@ AFTER SAVING THE TECHNICAL ORDER FOR SAL GATEWAY");
		logger.debug(" Exiting from  saveTechnicalOrderDetail");
		return technicalOrder;
	}
	boolean isIBSuccessful = false;
	
	/**
	 * installBase creation async
	 * @param installBaseDto
	 * @throws Exception
	 */
	public void installBaseCreationAsync(InstallBaseCreationDto installBaseDto) throws Exception {
		String returnCode = this.getSapClient().installBaseCreation(installBaseDto);
		isIBSuccessful = false;
		if (returnCode == null) {
			returnCode = GRTConstants.exception_errorcode;
			isIBSuccessful = false;
		}else if(returnCode.equals(GRTConstants.installBase_successCode)){
			//Update Site Registration status to In Process
			getInstallBaseDao().updateSiteRegistrationStatusOnRegId(installBaseDto.getRegistrationId(), StatusEnum.INPROCESS, ProcessStepEnum.INSTALL_BASE_CREATION);
			sendRegistrationRequestAlert(installBaseDto.getRegistrationId(), ProcessStepEnum.INSTALL_BASE_CREATION, null);
			isIBSuccessful = true;
		}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
			String errMsg="GRT unable to reach FMW";
			logger.debug(errMsg);
			
			//Mail to System Admin
			this.sendEmailToSystemAdmin(installBaseDto.getRegistrationId(), errMsg, ProcessStepEnum.INSTALL_BASE_CREATION);
			isIBSuccessful = false;
		}
	}
	
	/**
	 * method to populate account information for the given soldTo
	 * @param soldTo
	 * @return siteRegistration
	 * @throws Exception
	 */
	private SiteRegistration populateAccountInformationForSoldTo(String soldTo) throws Exception{
    	logger.debug("Entering: populateAccountInformationForSoldTo");
    	Account account = getSiebelClient().queryAccount(soldTo);
    	SiteRegistration siteRegistration = new SiteRegistration();
    	if(account != null){
    		siteRegistration.setAddress(account.getPrimaryAccountStreetAddress());
    		siteRegistration.setAddress2(account.getPrimaryAccountStreetAddress2());
    		siteRegistration.setCity(account.getPrimaryAccountCity());
    		siteRegistration.setState(account.getPrimaryAccountState());
    		siteRegistration.setZip(account.getPrimaryAccountPostalCode());
    		siteRegistration.setSiteCountry(account.getPrimaryAccountCountry());
    		siteRegistration.setCompanyPhone(account.getPhoneNumber());
    		siteRegistration.setRegion(account.getRegion());
    		siteRegistration.setCompany(account.getName());
    	}
    	logger.debug("Exiting: populateAccountInformationForSoldTo");
    	return siteRegistration;
    }
	
	/**
	 * method to fetch TRConfigs for salGW installer
	 * @param productIdentifier
	 * @return trConfigList
	 */
	@Override
	public List<TRConfig> fetchTRConfigsForSalGWInstaller(
			String productIdentifier) {
		List<TRConfig> trConfigList = new ArrayList<TRConfig>();
			trConfigList = getInstallBaseDao().fetchTRConfigsForSalGWInstaller(productIdentifier);
		return trConfigList;
	}
	/* END :: System to System Installer SALGW */

	/**
     * To get technical order by material code and serialNumber
     * @param materialCode
     * @param serialNumber
     * @return technicalOrder
     * @throws DataAccessException
     */
	@Override
	public TechnicalOrder getTechnicalOrderByFilter(String materialCode,
			String serialNumber) throws DataAccessException {
		TechnicalOrder technicalOrder = null;
		technicalOrder = getInstallBaseDao().getTechnicalOrderByFilter(materialCode, serialNumber);
		return technicalOrder;
	}

	
    /**
     * Method to get Valid Versions For IPOCoreUnit.
     * @return
     * @throws DataAccessException
     */
    public List<String> getValidVersionsForIPOCoreUnit(String materialCode) throws DataAccessException {
    	logger.debug("Entering getValidVersionsForIPOCoreUnit for MC:" + materialCode);
        List<String> releaseNumbers = new ArrayList<String>();
        Set<String> uniqueReleaseNumbers = new HashSet<String>();
        boolean isIpo = false;
        try {
            if (StringUtils.isNotEmpty(materialCode)) {
               List<TRConfig> configs = baseHibernateDao.getTRConfigData(materialCode);
	               if(configs != null) {
	            	   List<ProductRelease> sslVPNProductRelease = baseHibernateDao.getSSLVPNProductReleases();
	                   for (TRConfig config : configs) {
	                	   logger.debug("productType:" + config.getProductType());
	                       if(config.getProductType().equalsIgnoreCase(GRTConstants.PRODUCT_TYPE_IPO)) {
	                    	   isIpo = false;
	                    	   if(sslVPNProductRelease != null) {
	                    		   for (ProductRelease productRelease : sslVPNProductRelease) {
	                    			   uniqueReleaseNumbers.add(productRelease.getReleaseNumber());
	                    		   }
	                    	   }
	                           break;
	                         }
	                     }
		             }
                  }
            if(uniqueReleaseNumbers.size() > 0) {
            	releaseNumbers.addAll(uniqueReleaseNumbers);
	    	}
        } catch(Throwable throwable) {
    		logger.error("", throwable);
    	} finally {//Hack
        	if(isIpo && uniqueReleaseNumbers.size() == 0){
        		releaseNumbers.add("8.1");
        	}
    	}

    	return releaseNumbers;
     }

}
