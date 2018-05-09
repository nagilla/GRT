package com.avaya.grt.service.technicalonboarding;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.technicalonboarding.TechnicalOnBoardingDao;
import com.avaya.grt.dao.technicalonboarding.TechnicalOnBoardingDaoImpl;
import com.avaya.grt.mappers.ArtErrorCode;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.HardwareServer;
import com.avaya.grt.mappers.LocalTRConfig;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.RegistrationQuestions;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.service.BaseRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.registration.DeviceTOBResponseType;
import com.avaya.registration.ResponseHeaderType;
import com.grt.dto.AUXMCMain;
import com.grt.dto.Account;
import com.grt.dto.Asset;
import com.grt.dto.CMMain;
import com.grt.dto.Product;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.SALGateway;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.SRRequestDto;
import com.grt.dto.TRConfig;
import com.grt.dto.TechRegAlarmInputDto;
import com.grt.dto.TechRegInputDto;
import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.util.ARTOperationType;
import com.grt.util.AccessTypeEnum;
import com.grt.util.DataAccessException;
import com.grt.util.DateUtil;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.SearchParam;
import com.grt.util.StatusEnum;
import com.grt.util.TechRecordEnum;



/**
 * @author naganath_pawar
 *
 */


public class TechnicalOnBoardingServiceImpl extends BaseRegistrationService implements TechnicalOnBoardingService{
	private static final Logger logger = Logger.getLogger(TechnicalOnBoardingServiceImpl.class);
	public TechnicalOnBoardingDao technicalOnBoardingDao;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	public TechnicalOnBoardingDao getTechnicalOnBoardingDao() {
		return technicalOnBoardingDao;
	}

	public void setTechnicalOnBoardingDao(
			TechnicalOnBoardingDao technicalOnBoardingDao) {
		this.technicalOnBoardingDao = technicalOnBoardingDao;
	}

	
	/**
     * Method to get the technical registration summary list size for the given registration id.
     *
     * @param registrationId as string
     * @return size int
     */
    public int getTechnicalRegistrationSummaryListCount(String registrationId, List<SearchParam> searchParams) throws DataAccessException {
    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getTechnicalRegistrationSummaryListCount");
    	int result = getTechnicalOnBoardingDao().getTechnicalRegistrationSummaryListCount(registrationId, GRTConstants.TECH_ORDER_TYPE_TR, searchParams);
    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getTechnicalRegistrationSummaryListCount");
    	return result;
    }

    public List<TechnicalRegistrationSummary> getTechnicalRegistrationSummaryList(String soldToId, String registrationId, int offSet, 
    		int fetchSize, List<SearchParam> searchParams, String userId) throws DataAccessException{
    	if(logger.isDebugEnabled())
        logger.debug("Starting for RegistrationID [" + registrationId + "] and soldToID [" + soldToId + "]");

    	TechnicalOnBoardingDao regDao = getTechnicalOnBoardingDao(userId);
        
        SiteRegistration siteRegistration = regDao.getSiteRegistration(registrationId);
        String statusId = siteRegistration.getTechRegStatus().getStatusId();
        List<TechnicalOrder> registrationList = null;
        if (!statusId.equalsIgnoreCase(GRTConstants.SAVED_DESCRIPTION) && !statusId.equalsIgnoreCase(GRTConstants.CANCELLED)) {
        	try{
        		registrationList = generateDataFromSiebel(soldToId, siteRegistration);
        	} catch(Exception exp){
        		throw new DataAccessException(TechnicalOnBoardingService.class, exp.getMessage(), exp);
        	}
        }
        
    	if(registrationList==null) {
          registrationList = regDao.getTechnicalRegistrationSummaryList(registrationId, new String[]{GRTConstants.TECH_ORDER_TYPE_TR});
    	}
        List<TechnicalRegistrationSummary> technicalRegistrationSummaryList = new ArrayList<TechnicalRegistrationSummary>();
        TechnicalRegistrationSummary technicalSummary = null;

        if(registrationList != null) {
	        for (TechnicalOrder technicalOrder : registrationList) {
	        	technicalSummary = convertTechOrderToTechSummary(technicalOrder);
	            technicalSummary.setRegistrationId(registrationId);
	            technicalRegistrationSummaryList.add(technicalSummary);
	        	}
	    }

        return technicalRegistrationSummaryList;
    }
    
    public List<TechnicalOrder> generateDataFromSiebel(String soldToNumber, SiteRegistration siteRegistration) throws DataAccessException{
        logger.debug("Entering TechnicalOnBoardingServiceImpl.generateDataFromSiebel for soldTo:" + soldToNumber + " registration Id:" + siteRegistration.getRegistrationId());
        TechnicalOrder technicalOrder = null;
        Collection<Product> productList = new ArrayList<Product>();
        List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();        
        try {
        	List<TechnicalOrder> listToSave = new ArrayList<TechnicalOrder>();
            	productList = getTechnicalOnBoardingDao().getTechnicallyRegisterableRecordsNew(soldToNumber);
            	Map<String, Product> pipelineRecords = this.getPipelineTechnicallyRegisterableRecords(soldToNumber);
                if ((productList != null) && productList.size() > 0) {
                    logger.debug("Returned asset size: " + productList.size());
                    for (Product product : productList) {
                        technicalOrder = new TechnicalOrder();
                        technicalOrder.setSiteRegistration(siteRegistration);
                        technicalOrder.setMaterialCode(product.getMaterialCode());
                        int quantity = product.getQuantity();
                        String equipmentNumber = product.getEquipmentNumber();
                        if(pipelineRecords != null) {
                        	Product pipelineProduct = pipelineRecords.get(product.getMaterialCode());
                        	if(pipelineProduct != null) {
                        		quantity += pipelineProduct.getQuantity();
                        		equipmentNumber += pipelineProduct.getEquipmentNumber();
                        		logger.debug("Found common MC b/w Siebel and pipeline for MC:" + technicalOrder.getMaterialCode());
                        		pipelineRecords.remove(product.getMaterialCode());
                        	}
                        }
                        technicalOrder.setInitialQuantity(new Long(quantity));
                        technicalOrder.setRemainingQuantity(new Long(quantity));
                        technicalOrder.setEquipmentNumber(equipmentNumber);
                        technicalOrder.setCreatedDate(getDateFromDateStr(product.getCreatedDate()));
                        technicalOrder.setDescription(product.getShortDescription());
                        technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR);
                        technicalOrder.setSolutionElementCode(product.getSolutionElement());

                        technicalOrder.setSeid(product.getSeId());
                        technicalOrder.setProductLine(product.getProductLine());
                        technicalOrderList.add(technicalOrder);
                    }
                }
                if(pipelineRecords != null && pipelineRecords.size()>0) {
                	logger.debug("Processing unconsumed pipeline records ::" + pipelineRecords.size());
                	List<String> mcs = new ArrayList<String>();
                	mcs.addAll(pipelineRecords.keySet());
                	Map<String, String> mcData = this.getTechnicalOnBoardingDao().getMaterialCodeDesc(mcs);
                	for (Product product : pipelineRecords.values()) {
                        technicalOrder = new TechnicalOrder();
                        technicalOrder.setSiteRegistration(siteRegistration);
                        technicalOrder.setMaterialCode(product.getMaterialCode());
                        int quantity = product.getQuantity();
                        String equipmentNumber = product.getEquipmentNumber();
                        technicalOrder.setInitialQuantity(new Long(quantity));
                        technicalOrder.setRemainingQuantity(new Long(quantity));
                        technicalOrder.setEquipmentNumber(equipmentNumber);
                        technicalOrder.setDescription(mcData.get(technicalOrder.getMaterialCode()));
                        technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR);
                        technicalOrder.setSolutionElementCode(product.getSolutionElement());
                        technicalOrder.setSeid(product.getSeId());
                        technicalOrder.setProductLine(product.getProductLine());
                        technicalOrderList.add(technicalOrder);
					}
                }                
                if(!technicalOrderList.isEmpty())  {
                	listToSave.addAll(technicalOrderList);
                }
                
 

        } catch (Exception ex) {
            throw new DataAccessException(TechnicalOnBoardingServiceImpl.class, ex.getMessage(), ex);
        } finally {
            logger.debug("Exiting TechnicalOnBoardingServiceImpl.generateDataFromSiebel for soldTo:" + soldToNumber + " registration Id:" + siteRegistration.getRegistrationId());
        }
        return technicalOrderList;
    }
    public static Date getDateFromDateStr(String dateStr) {
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}
    /**
     * Method to generate the data from Siebel and store in GRT.
     *
     * @param registrationId
     * @param soldToNumber
     * @throws DataAccessException
     */
    public List<TechnicalOrder> generateDataFromSiebel(String soldToNumber, String registrationId) throws DataAccessException{
        logger.debug("Entering TechnicalOnBoardingServiceImpl.generateDataFromSiebel for soldTo:" + soldToNumber + " registration Id:" + registrationId);
        TechnicalOrder technicalOrder = null;
        Collection<Product> productList = new ArrayList<Product>();
        List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();
        List<Product> techicallyRegisteredProductList = new ArrayList<Product>();
        List<TechnicalOrder> technicalOrderTechicallyRegisteredList = new ArrayList<TechnicalOrder>();
        try {
            SiteRegistration siteRegistration = getTechnicalOnBoardingDao().getSiteRegistration(registrationId);
                //Get the latest materials from Siebel
            	productList = getTechnicalOnBoardingDao().getTechnicallyRegisterableRecordsNew(soldToNumber);
            	Map<String, Product> pipelineRecords = this.getPipelineTechnicallyRegisterableRecords(soldToNumber);
                if ((productList != null) && productList.size() > 0) {
                    logger.debug("Returned asset size: " + productList.size());
                    for (Product product : productList) {
                        technicalOrder = new TechnicalOrder();
                        technicalOrder.setSiteRegistration(siteRegistration);
                        technicalOrder.setMaterialCode(product.getMaterialCode());
                        int quantity = product.getQuantity();
                        String equipmentNumber = product.getEquipmentNumber();
                        if(pipelineRecords != null) {
                        	Product pipelineProduct = pipelineRecords.get(product.getMaterialCode());
                        	if(pipelineProduct != null) {
                        		quantity += pipelineProduct.getQuantity();
                        		equipmentNumber += pipelineProduct.getEquipmentNumber();
                        		logger.debug("Found common MC b/w Siebel and pipeline for MC:" + technicalOrder.getMaterialCode());
                        		pipelineRecords.remove(product.getMaterialCode());
                        	}
                        }
                        technicalOrder.setInitialQuantity(new Long(quantity));
                        technicalOrder.setRemainingQuantity(new Long(quantity));
                        technicalOrder.setEquipmentNumber(equipmentNumber);
                        technicalOrder.setCreatedDate(DateUtil.getDateFromDateStr(product.getCreatedDate(), "MM-dd-yyyy"));
                        technicalOrder.setDescription(product.getShortDescription());
                        technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR);
                        technicalOrder.setSolutionElementCode(product.getSolutionElement());

                        technicalOrder.setSeid(product.getSeId());
                        technicalOrder.setProductLine(product.getProductLine());
                        technicalOrderList.add(technicalOrder);
                    }
                }
                if(pipelineRecords != null && pipelineRecords.size()>0) {
                	logger.debug("Processing unconsumed pipeline records ::" + pipelineRecords.size());
                	List<String> mcs = new ArrayList<String>();
                	mcs.addAll(pipelineRecords.keySet());
                	Map<String, String> mcData = this.getTechnicalOnBoardingDao().getMaterialCodeDesc(mcs);
                	for (Product product : pipelineRecords.values()) {
                        technicalOrder = new TechnicalOrder();
                        technicalOrder.setSiteRegistration(siteRegistration);
                        technicalOrder.setMaterialCode(product.getMaterialCode());
                        int quantity = product.getQuantity();
                        String equipmentNumber = product.getEquipmentNumber();
                        technicalOrder.setInitialQuantity(new Long(quantity));
                        technicalOrder.setRemainingQuantity(new Long(quantity));
                        technicalOrder.setEquipmentNumber(equipmentNumber);
                        technicalOrder.setDescription(mcData.get(technicalOrder.getMaterialCode()));
                        technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR);
                        technicalOrder.setSolutionElementCode(product.getSolutionElement());
                        technicalOrder.setSeid(product.getSeId());
                        technicalOrder.setProductLine(product.getProductLine());
                        technicalOrderList.add(technicalOrder);
					}
                }
                // End GRT3.0 - Siebel sync is no longer required.

                // Get the technically registered materials from Siebel
                techicallyRegisteredProductList = getTechnicalOnBoardingDao().getTechnicallyRegisteredRecords(soldToNumber);
                if ((techicallyRegisteredProductList != null) && techicallyRegisteredProductList.size() > 0) {
                    logger.debug("Returned technically registered asset size: " + techicallyRegisteredProductList.size());
                    for (Product product : techicallyRegisteredProductList) {
                        technicalOrder = new TechnicalOrder();
                        technicalOrder.setSiteRegistration(siteRegistration);
                        technicalOrder.setMaterialCode(product.getMaterialCode());
                        technicalOrder.setInitialQuantity(new Long(product.getQuantity()));
                        //technicalOrder.setRemainingQuantity(new Long(product.getRemainingQuantity()));
                        technicalOrder.setRemainingQuantity(new Long(product.getQuantity()));
                        technicalOrder.setCreatedDate(DateUtil.getDateFromDateStr(product.getCreatedDate(), "MM-dd-yyyy"));
                        technicalOrder.setDescription(product.getShortDescription());
                        technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR_UPDATE);
                        technicalOrder.setSolutionElementCode(product.getSolutionElement());
                        technicalOrder.setEquipmentNumber(product.getEquipmentNumber());
                        technicalOrder.setSeid(product.getSeId());
                        technicalOrder.setSid(product.getSId());
                        technicalOrder.setMid(product.getMId());
                        technicalOrder.setProductLine(product.getProductLine());
                        technicalOrder.setSalSeIdPrimarySecondary(product.getSalSeIdPrimarySecondary());
                        technicalOrder.setGroupId(product.getGroupId());
                        technicalOrderTechicallyRegisteredList.add(technicalOrder);
                    }
                }

        } catch (Exception ex) {
            throw new DataAccessException(TechnicalOnBoardingServiceImpl.class, ex.getMessage(), ex);
        } finally {
            logger.debug("Exiting TechnicalOnBoardingServiceImpl.generateDataFromSiebel for soldTo:" + soldToNumber + " registration Id:" + registrationId);
        }
        return technicalOrderList;
    }
    
    /**
     * Method to get all Pipeline SAP transactions.
     * @return
     * @throws DataAccessException
     */
    public Map<String, Product> getPipelineTechnicallyRegisterableRecords(String soldToId) throws DataAccessException {
        Map<String, Product> trPipelineRecords = new HashMap<String, Product>();
        try {
        	if(StringUtils.isNotEmpty(soldToId)) {
        		List<PipelineSapTransactions> pipelineTransactions = this.getTechnicalOnBoardingDao().getPipelineTechnicallyRegisterableRecords(soldToId);
        		if(pipelineTransactions != null && pipelineTransactions.size() > 0) {
        			for (PipelineSapTransactions transaction : pipelineTransactions) {
        				if(GRTConstants.TECH_ORDER_TYPE_IB.equals(transaction.getAction()) && ((int)transaction.getAlreadyTRedQty() < (int)transaction.getQuantity())) {
	        				Product product = trPipelineRecords.get(transaction.getMaterialCode());
	        				if(product == null) {
	        					product = new Product();
	        					product.setMaterialCode(transaction.getMaterialCode());
	        					product.setQuantity((int)transaction.getQuantity() - (int)transaction.getAlreadyTRedQty());
	        					product.setEquipmentNumber("-" + transaction.getEquipmentNumber() + ":" + transaction.getQuantity());
	        					trPipelineRecords.put(product.getMaterialCode(), product);
	        				} else {
	        					product.setQuantity(product.getQuantity() + ((int)transaction.getQuantity() - (int)transaction.getAlreadyTRedQty()));
	        					product.setEquipmentNumber(product.getEquipmentNumber() + "~" + transaction.getEquipmentNumber() + ":" + transaction.getQuantity());
	        				}
	        				logger.debug("Added quantity:" + product.getQuantity() + " for MC:" + product.getMaterialCode() + " EQN:" + product.getEquipmentNumber());
        				} else if(GRTConstants.TECH_ORDER_TYPE_IB.equals(transaction.getAction())) {
        					//TODO: Handle EQR
        				}
					}
        		}
        	}
        } catch (Throwable throwable) {
        	logger.error("", throwable);
        } finally {
	    	
        }
        return trPipelineRecords;
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
    	logger.debug("Entering TechnicalOnBoardingServiceImpl: getRegistrationSummary");
        RegistrationSummary result = new RegistrationSummary();
        SiteRegistration siteRegistration = getTechnicalOnBoardingDao()
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
    
    			}
    		}
         }
        result.setImpl(siteRegistration.getTypeOfImplementation());

        result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());

        logger.debug("SEtting the submitted flag after getting Ibase"+siteRegistration.getSubmitted());
        result.setSubmitted(siteRegistration.getSubmitted());
        result.setSrCompleted(siteRegistration.getIsSrCompleted());
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
        result.setRequestingCompany(siteRegistration.getCompany());
        result.setInstallBaseSrNo(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getInstallBaseSrRequest()));
        result.setFinalValidationSrNo(TechnicalRegistrationUtil.fetchSRNumber(siteRegistration.getFinalValidationSrRequest()));
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
        result.setTobCompletedDate(siteRegistration.getTobCompletedDate());
        result.setCreatedDate(siteRegistration.getCreatedDate());
        }
        logger.debug("Exiting TechnicalOnBoardingServiceImpl : getRegistrationSummary");

        return result;
    }
    
    /**
     * Change the ownership for the give siteRegistration
     *
     * @param siteRegistration siteRegistration
     * @return registration siteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration changeOwnership(SiteRegistration siteRegistration, String userId) throws DataAccessException {
        logger.debug("Entering TechnicalOnBoardingServiceImpl : changeOwnership");
        setUserId(userId);
        SiteRegistration siteRegistrationDomain = new SiteRegistration();
        siteRegistrationDomain.setRegistrationId(siteRegistration.getRegistrationId());
        siteRegistrationDomain.setFirstName(siteRegistration.getFirstName());
        siteRegistrationDomain.setLastName(siteRegistration.getLastName());
        siteRegistrationDomain.setUserName(siteRegistration.getUserName());
        siteRegistrationDomain.setReportEmailId(siteRegistration.getEmail());
        siteRegistrationDomain.setReportPhone(siteRegistration.getPhone());
        siteRegistrationDomain = getTechnicalOnBoardingDao().changeOwnership(siteRegistrationDomain);
        logger.debug("Exiting TechnicalOnBoardingServiceImpl : changeOwnership");
        return siteRegistration;
    }
    
    public List<TRConfig> fetchTRConfigs(String materialCode) {
        logger.debug("Entering TechnicalOnBoardingServiceImpl.getTRConfigData for materialCode:" + materialCode);
          List<TRConfig> configData = new ArrayList<TRConfig>();
          try {
                if(StringUtils.isNotEmpty(materialCode)) {
                	Map<String, Set<String>> mc2GroupIdMappings=  getTechnicalOnBoardingDao().initializeTRConfigData();
                   logger.debug("mc2GroupIdMappings size" + mc2GroupIdMappings.size());
                   
                          Set<String> mc2GroupId = mc2GroupIdMappings.get(materialCode); 
                          if(mc2GroupId != null && mc2GroupId.size() > 0) {
                        	  logger.debug("mc2GroupId Size:"+mc2GroupIdMappings.get(materialCode));
                                logger.debug("mc2GroupId Size:"+mc2GroupId.size());
                                for (String groupId : mc2GroupId) {
                                      List<TRConfig> trConfigs = getTechnicalOnBoardingDao().fetchTRConfigs().get(groupId);
                                      logger.debug("trConfigs Size:"+trConfigs.size());
                                      if(trConfigs!= null) {
                                            for (TRConfig config : trConfigs) {
                                                              configData.add(config);
                                                              break;
                                                  }
                                      }
                                }
                                logger.debug("configData Size:"+configData.size());
                          }
                    }
                Collections.sort(configData);
          }catch (Throwable throwable) {
                logger.error("", throwable);
          } finally {
              logger.debug("Existing TechnicalOnBoardingServiceImpl.getTRConfigData");
          }
          return configData;
      }

    public Set<String> getEligibleAccessTypesByGroupId(String groupId)  throws DataAccessException {
    	logger.debug("Entering TechnicalOnBoardingServiceImpl : getEligibleAccessTypesByGroupId for groupId:" + groupId);
    	Set<String> accessTypes = null;
    	try {
	    	TRConfig trConfig = this.loadTRConfig(groupId);
	    	List<TRConfig> trConfigs = new ArrayList<TRConfig>();
	    	boolean flag = false;
	    	trConfigs.add(trConfig);
	    	accessTypes = this.getTechnicalOnBoardingDao().getEligibleAccessTypes(trConfigs, this.initializeCompatibilityMatrixData());
	    	List<ProductRelease> productReleases = this.getTechnicalOnBoardingDao().getSSLVPNProductReleases();
	    	if(trConfigs != null && productReleases != null) {
	    		for (TRConfig config : trConfigs) {
	    			for (ProductRelease release : productReleases) {
	    				if(release != null 
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
	    				logger.debug("GroupId:"+groupId+" eligible for Accesstype: SSL/VPN based on Special note of Siebel View.");
	    			}
				}
	    	}
    	} catch (Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting TechnicalOnBoardingServiceImpl : getEligibleAccessTypesByGroupId for groupId:" + groupId);
    	}
    	return accessTypes;
    }
    
    /**
	 * API to get unique SeCodes for a paramterized groupId.
	 * 
	 * @param groupId
	 * 
	 * @return comma seperated seCodes
	 * @throws DataAccessException
	 */
	public String getGroupSeCodes(String groupId) throws DataAccessException {
		if(logger.isDebugEnabled())
		   logger.debug("Start TechnicalOnBoardingServiceImpl.getGroupSeCodes with groupId :" + groupId);
		String seCodes = "";
		try {
    			seCodes  = getTechnicalOnBoardingDao().getGroupSecodes(groupId);
    	} catch (Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		if(logger.isDebugEnabled())
    		   logger.debug("End TechnicalOnBoardingServiceImpl.getGroupSeCodes with groupId :" + groupId+" return "+seCodes);
    	}
    	return seCodes;
	}
	
	 public List<SALGateway> getSALGateways(String soldToId, String salSEID, String salFlag) {
	    	logger.debug("Starting...TechnicalOnBoardingServiceImpl.getSALGateways");
	    	List<SALGateway> gateways = new ArrayList<SALGateway>();
	    	try {
	    		gateways = getTechnicalOnBoardingDao().getSALGateways(soldToId, salSEID, salFlag);
			} catch (DataAccessException dae) {
				logger.error("Error! while retrieving SALGateways", dae);
			}
	    	logger.debug("Completed...TechnicalOnBoardingServiceImpl.getSALGateways");
	    	return gateways;
	    }

	 public List<String> getSPVersionsByGroupId(String groupId, String releaseNumber) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getSPVersionsByGroupId with groupId:" + groupId + " releaseNumber:" + releaseNumber);
	    	try {
	    		TRConfig trConfig = this.loadTRConfig(groupId);
	    		return this.getTechnicalOnBoardingDao().getSPVersions(trConfig.getProductType(), trConfig.getTemplate(), releaseNumber);
	    	} finally {
	    		logger.debug("Exiting TechnicalOnBoardingServiceImpl.getSPVersionsByGroupId with groupId:" + groupId + " releaseNumber:" + releaseNumber);
	    	}
	    }
	 
	 /**
	     * API returns collection of domain HardwareServer objects from template
	     *
	     * @param template templateCode
	     * @return
	     * @throws DataAccessException
	     */
	    public List<HardwareServer> getHardwareServersTemplate(String template) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getHardwareServersTemplate");
	    	List<HardwareServer> hardwareServers = new ArrayList<HardwareServer>();
	    	try {
		    	if(StringUtils.isNotEmpty(template)) {
		    		hardwareServers = this.getTechnicalOnBoardingDao().getHardwareServersTemplate(template);
		    	}
	    	} catch(Throwable throwable) {
	    		logger.error("", throwable);
	    	}
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getHardwareServersTemplate");
	    	return hardwareServers;
	    }
	    
	    public List<TechnicalRegistrationSummary> getExistingTOBRecords(String soldToNumber,String registrationId) {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getExistingTOBRecords");
	    	List<Product> techicallyRegisteredProductList = new ArrayList<Product>();
	    	List<TechnicalRegistrationSummary> returnList = new ArrayList<TechnicalRegistrationSummary>();
	    	try {
	    		techicallyRegisteredProductList = getTechnicalOnBoardingDao().getTechnicallyRegisteredRecords(soldToNumber);
	    		TechnicalOrder technicalOrder;                
	        	SiteRegistration siteRegistration = getTechnicalOnBoardingDao().getSiteRegistration(registrationId);
	        	List<TechnicalOrder> listToSave = new ArrayList<TechnicalOrder>();
	            for (Product product : techicallyRegisteredProductList) {
	                technicalOrder = new TechnicalOrder();
	                technicalOrder.setMaterialCode(product.getMaterialCode());
	                technicalOrder.setInitialQuantity(new Long(product.getQuantity()));
	                technicalOrder.setRemainingQuantity(new Long(product.getQuantity()));
	                technicalOrder.setCreatedDate(TechnicalRegistrationUtil.getDateFromDateStr(product.getCreatedDate()));
	                technicalOrder.setDescription(product.getShortDescription());
	                technicalOrder.setOrderType(GRTConstants.TECH_ORDER_TYPE_TR_UPDATE);
	                technicalOrder.setSolutionElementCode(product.getSolutionElement());
	                technicalOrder.setEquipmentNumber(product.getEquipmentNumber());
	                technicalOrder.setSeid(product.getSeId());
	                technicalOrder.setSid(product.getSId());
	                technicalOrder.setMid(product.getMId());
	                technicalOrder.setProductLine(product.getProductLine());
	                technicalOrder.setSalSeIdPrimarySecondary(product.getSalSeIdPrimarySecondary());
	                technicalOrder.setGroupId(product.getGroupId());
	                technicalOrder.setSiteRegistration(siteRegistration);
	                technicalOrder.setAlarmId(product.getAlarmId());
	                listToSave.add(technicalOrder);
	               
	            }
	            if(!listToSave.isEmpty()) {
	            	for(TechnicalOrder t:listToSave){
	            		TechnicalRegistrationSummary technicalSummary= convertTechOrderToTechSummary(t);
	            		technicalSummary.setRegistrationId(registrationId);
	            		returnList.add(technicalSummary);
	            	   
	            	}
	               
	            }
	    	}catch(Exception e) {
	    		logger.error("Error retrieving existing TOB recors for sold to "+soldToNumber+e);
	    		return returnList;
	    	}    	
	    	
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getExistingTOBRecords");
	        return returnList;
	    }
	    
	    private List<TechnicalOrder> convertTechSummaryToTechOrder(List<TechnicalRegistrationSummary> trSummary) { 
	    	String registrationId = "";
	    	List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();  
	    	
	    	try { 
		    	if (!trSummary.isEmpty()) {
		    		registrationId = trSummary.get(0).getRegistrationId();
		    		SiteRegistration siteRegistration = getTechnicalOnBoardingDao().getSiteRegistration(registrationId);
			    	
			    	for (TechnicalRegistrationSummary ts:trSummary) {
			    		TechnicalOrder technicalOrder = new TechnicalOrder();
				        technicalOrder.setSiteRegistration(siteRegistration);
				        technicalOrder.setMaterialCode(ts.getMaterialCode());
				        technicalOrder.setInitialQuantity(ts.getInitialQty());
				        technicalOrder.setRemainingQuantity(ts.getRemainingQty());
				        technicalOrder.setEquipmentNumber(ts.getEquipmentNumber());
				        technicalOrder.setDescription(ts.getMaterialCodeDescription());
				        technicalOrder.setOrderType(ts.getOrderType());
				        technicalOrder.setSolutionElementCode(ts.getSolutionElement());
				        technicalOrder.setSeid(ts.getSeId());
				        technicalOrder.setProductLine(ts.getProductLine());
				        technicalOrder.setOrderId(ts.getOrderId());
				        technicalOrder.setSalSeIdPrimarySecondary(ts.getSalSeIdPrimarySecondary());
				        technicalOrderList.add(technicalOrder);
			    	}
		    	}
	    	} catch (DataAccessException de) {
	    		logger.debug("Data Access Exception occured : " + de.getMessage());
	    	} catch (Exception de) {
	    		logger.debug("Exception occured : " + de.getMessage());
	    	} 
	    	
	    	return technicalOrderList;
	    }
	    
	    private TechnicalRegistrationSummary convertTechOrderToTechSummary(TechnicalOrder technicalOrder) {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.convertTechOrderToTechSummary");
	    	TechnicalRegistrationSummary technicalSummary = new TechnicalRegistrationSummary();
	        technicalSummary.setOrderId(technicalOrder.getOrderId());
	        technicalSummary.setMaterialCode(technicalOrder.getMaterialCode());
	        technicalSummary.setMaterialCodeDescription(technicalOrder.getDescription());
	        technicalSummary.setSolutionElement(technicalOrder.getSolutionElementCode());
	        technicalSummary.setSerialNo(technicalOrder.getSerialNumber());
	        technicalSummary.setInitialQty(technicalOrder.getInitialQuantity());	           
	        technicalSummary.setRemainingQty(technicalOrder.getRemainingQuantity()==null?0:technicalOrder.getRemainingQuantity());
	        technicalSummary.setCreatedDate(DateUtil.formatDate(technicalOrder.getCreatedDate()));
	        technicalSummary.setProcessStep(technicalOrder.getSiteRegistration().getProcessStep().getProcessStepId());
	        technicalSummary.setStatusId(technicalOrder.getSiteRegistration().getStatus().getStatusId());	          
	        if(StringUtils.isNotEmpty(technicalOrder.getIsBaseUnit()) && technicalOrder.getIsBaseUnit().equalsIgnoreCase("y")){
	        	technicalSummary.setBaseUnit(true);
	        }
	      if(technicalOrder.getSiteRegistration()!=null) {
	        technicalSummary.setInstallBaseStatusId(technicalOrder.getSiteRegistration().getInstallBaseStatus().getStatusId());
	        technicalSummary.setTechRegStatusId(technicalOrder.getSiteRegistration().getTechRegStatus().getStatusId());
	        technicalSummary.setFinalValidationStatusId(technicalOrder.getSiteRegistration().getFinalValidationStatus().getStatusId());
	      }
	        if(StringUtils.isNotEmpty(technicalOrder.getIsIPOEligible())&& (technicalOrder.getIsIPOEligible().equalsIgnoreCase("true"))){
	        	technicalSummary.setIPOEligible(true);
	        }else{
	        	technicalSummary.setIPOEligible(false);
	        }
	        technicalSummary.setProductLine(technicalOrder.getProductLine());
	        technicalSummary.setOrderType(technicalOrder.getOrderType());
	        technicalSummary.setSeId(technicalOrder.getSeid());
	        technicalSummary.setSid(technicalOrder.getSid());
	        technicalSummary.setMid(technicalOrder.getMid());
	        technicalSummary.setSalSeIdPrimarySecondary(technicalOrder.getSalSeIdPrimarySecondary());
	        technicalSummary.setGroupId(technicalOrder.getGroupId());
	        
	        /* Grt 4.0 change starts*/
	        technicalSummary.setEquipmentNumber(technicalOrder.getEquipmentNumber());
	        technicalSummary.setAlarmId(technicalOrder.getAlarmId());
	        /* Grt 4.0 change ends*/
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.convertTechOrderToTechSummary");
	    	return technicalSummary;
	    }
	    
	    /**
	     * Fetch Product Registration Data on SEID
	     */
	    public List<Asset> queryProductRegistrationData(String SEID, int pageSize, boolean recordCountNeeded, int startRowNum) throws Exception{
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.queryProductRegistrationData");
	        List<Asset> assetsList =  getSiebelClient().queryProductRegistrationData(SEID, pageSize, recordCountNeeded, startRowNum);
	        logger.debug("Exiting TechnicalOnBoardingServiceImpl.queryProductRegistrationData");
	        return assetsList;
	    }
	    
	    /**
	     * API returns collection releaseNumbers for passed GroupId.
	     *
	     * @param groupId
	     * @return
	     * @throws DataAccessException
	     */
	    public List<String> getReleasesByGroupId(String groupId) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getReleasesByGroupId for groupId:" + groupId);
	    	List<String> result = null;
	    	try {
		    	TRConfig trConfig = this.loadTRConfig(groupId);
		    	if(trConfig != null) {
		    		logger.debug("Found trConfig:" + trConfig);
		    		result = this.getTechnicalOnBoardingDao().getReleases(trConfig.getSeCode(), trConfig.getProductType(), trConfig.getTemplate(), trConfig.getSpecial_note(), false);
		    	} else {
		    		logger.debug("NOT Found trConfig :" + trConfig);
		    	}
	    	} catch(Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting TechnicalOnBoardingServiceImpl.getReleasesByGroupId");
	    	}
	    	return result;
	    }
	    
	    public List<LocalTRConfig> getLocalTRConfig(TRConfig trConfig) throws DataAccessException {
	    	 logger.debug("Entering TechnicalOnBoardingServiceImpl :getLocalTRConfig");
	    	List<LocalTRConfig> localTRConfigs =   this.getTechnicalOnBoardingDao().getLocalTRConfig(trConfig);
	    	if(localTRConfigs != null && localTRConfigs.size()>0) {
	    		this.modifiedLocalTRConfigs(localTRConfigs);
	    	}
	    	 logger.debug("Exiting TechnicalOnBoardingServiceImpl :getLocalTRConfig");
	    	return localTRConfigs;
	    }
	    
	    public SiteRegistration getTechnicalRegistrationDetails(String registrationId) throws DataAccessException {
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : getTechnicalRegistrationDetailList()");
	        SiteRegistration siteRegistration =  this.getTechnicalOnBoardingDao().getTechnicalRegistrationDetails(registrationId, false);
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : getTechnicalRegistrationDetailList()");
	        return siteRegistration;
	    }
	    
	    public SiteRegistration getTechnicalRegistrationDetails(String registrationId, boolean isSuperUser) throws DataAccessException {
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : getTechnicalRegistrationDetailList()");
	        String soldTo = "";
	        int seidCreationFailureFlag = 0;
	        SiteRegistration siteRegistration =  this.getTechnicalOnBoardingDao().getTechnicalRegistrationDetails(registrationId, isSuperUser);
            //Begin to set the Trouble Shooting URL
        	List<TechnicalRegistration> nonSalTrs = siteRegistration.getTechnicalRegistrationDetailList();
            if (null != nonSalTrs && nonSalTrs.size() > 0){
                for (TechnicalRegistration tr : nonSalTrs) {
                	if (tr.getErrorCode() != null){
                		tr.setTroubleShootURL(getARTErrorCodeURL(tr.getErrorCode()));
                	}
                	if(StringUtils.isEmpty(tr.getSolutionElementId()) && StringUtils.isEmpty(tr.getArtSrNo()) && StringUtils.isNotEmpty(tr.getErrorCode())){
            			//&& StringUtils.isNotEmpty(salTr.getNumberOfSubmit()) && GRTConstants.NUMBER_ONE.equals(salTr.getNumberOfSubmit())){ - NumberOfSubmit check relaxed
	            		seidCreationFailureFlag = 1;
	            	}
                }
            }

        	List<TechnicalRegistration> salTrs = siteRegistration.getSalRegistrationSummaryList();
            if (null != salTrs && salTrs.size() > 0){
                for (TechnicalRegistration salTr : salTrs) {
                	if (salTr.getErrorCode() != null){
                		salTr.setTroubleShootURL(getARTErrorCodeURL(salTr.getErrorCode()));
                	}
                	logger.debug(">>>>>>>>>>>>>..."+salTr.getSid());
			    	logger.debug(">>>>>>>>>>>>>..."+salTr.getMid());
                	StringBuilder sb = new StringBuilder();
                	if(salTr.getSoldToId() != null && StringUtils.isNotEmpty(salTr.getSoldToId())){
                		soldTo = salTr.getSoldToId();
                	} else {
                		soldTo = siteRegistration.getSoldToId();
                	}
                	if(salTr.getPrimarySalGWSeid() != null){
                		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+salTr.getPrimarySalGWSeid()+"','"+salTr.getPrimarySoldToId()+"')\" >"+salTr.getPrimarySalGWSeid()+"</a>" );
                	}
                	if(salTr.getSecondarySalGWSeid() != null){
                		sb.append("&nbsp;+&nbsp;");
                		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+salTr.getSecondarySalGWSeid()+"','"+salTr.getSecondarySoldToId()+"')\" >"+salTr.getSecondarySalGWSeid()+"</a>" );
                	}
                	if(sb != null &&  !StringUtils.isEmpty(sb.toString()) ){
                		salTr.setSalGateWaySeid(sb.toString());
                	}
                	if(StringUtils.isEmpty(salTr.getSolutionElementId()) && StringUtils.isEmpty(salTr.getArtSrNo()) && StringUtils.isNotEmpty(salTr.getErrorCode())){
                		
                		seidCreationFailureFlag = 1;
                	}
                	
                	//GRT 4.0 Change : Step B checkbox should be disabled for NO Connectivity(Defect #306)
                	if( !StringUtils.isEmpty(salTr.getConnectivity()) && salTr.getConnectivity().equalsIgnoreCase(GRTConstants.NO) ){
                		salTr.setCheckBoxDisabled(true);
                	}
                	
                	//End to populate the salGateWaySeid
                }
            }

        	List<SiteList> salMigrationSummaryList = siteRegistration.getSalMigrationSummaryList();
            if (null != salMigrationSummaryList && salMigrationSummaryList.size() > 0){
                for (SiteList siteList : salMigrationSummaryList) {
                	if (siteList.getErrorCode() != null){
                		siteList.setTroubleShootURL(getARTErrorCodeURL(siteList.getErrorCode()));
                	}
                	//Begin to populate the salGateWaySeid
                	StringBuilder sb = new StringBuilder();
                	if(siteList.getSoldToId() != null && StringUtils.isNotEmpty(siteList.getSoldToId())){
                		soldTo = siteList.getSoldToId();
                	} else {
                		soldTo = siteRegistration.getSoldToId();
                	}
                	if(siteList.getPrimarySALGatewaySEID() != null){
                		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+siteList.getPrimarySALGatewaySEID()+"','"+soldTo+"')\" >"+siteList.getPrimarySALGatewaySEID()+"</a>" );
                	}
                	if(siteList.getSecondarySALGatewaySEID() != null){
                		sb.append("&nbsp;+&nbsp;");
                		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+siteList.getSecondarySALGatewaySEID()+"','"+soldTo+"')\" >"+siteList.getSecondarySALGatewaySEID()+"</a>" );
                	}
                	if(sb != null &&  !StringUtils.isEmpty(sb.toString()) ){
                		siteList.setSalGateWaySeid(sb.toString());
                	}
                	//End to populate the salGateWaySeid
                	
                	if(StringUtils.isEmpty(siteList.getSolutionElementId()) && StringUtils.isEmpty(siteList.getArtSrNo()) && StringUtils.isNotEmpty(siteList.getErrorCode())){
	            		seidCreationFailureFlag = 1;
	            	}
                }
            }
            if(seidCreationFailureFlag == 1){
            	siteRegistration.setSeidCreationFailureFlag(true);
            }
            //End to set the Trouble Shooting URL
	        logger.debug("Exiting TechnicalOnBoardingServiceImpl : getTechnicalRegistrationDetailList()");
	        return siteRegistration;
	    }
	    private String getARTErrorCodeURL(String errorCode) throws DataAccessException {
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : getARTErrorCodeURL");
	        ArtErrorCode artErrorCode = getTechnicalOnBoardingDao().getArtErrorCode(errorCode);
	        String artErrorURL = "";

	        if (artErrorCode != null) {
	            artErrorURL = artErrorCode.getInsiteUrl();
	        }
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : getARTErrorCodeURL");

	        return artErrorURL;
	    }
	    
	    
	    /* GRT 4.0 changes starts */
	    /** 
	     * GRT 4.0 changes
	     * API to save submitAlarmTechnicalRegistrationSummary.
	     *
	     * @param trSummary List<TechnicalRegistrationSummary>
	     * @return void
	     */
	    public Boolean submitAlarmTechnicalRegistrationSummary(List<TechnicalRegistration> trs, List<SiteList> sls, String registrationId) 
	    																	throws DataAccessException, Exception {
	    	String siebelSrNumber = " ";	
	    	SiteRegistration siteRegistration = this.getSiteRegistrationOnRegId(registrationId);
	    	boolean trResult = false;
	    	SRRequest srRequest = null;
	    	boolean returnFlag = true;
			if(trs!=null && trs.size()>0) {
				trResult = this.doTechnicalAlarmRegistration(siteRegistration, trs, null, null);
				
				if (!trResult) {
					//Nothing
				}
				if( trResult ){
					for(TechnicalRegistration techReg : trs) {
						if(srRequest != null) {
							techReg.setStepBSRRequest(srRequest);
							logger.debug("Setting srRequest.getSiebelSRNo():" + srRequest.getSiebelSRNo());
							//techReg.setArtSrNo(srRequest.getSiebelSRNo());
							siebelSrNumber = srRequest.getSiebelSRNo();
						}
						Status status = new Status();
						status.setStatusId(StatusEnum.INPROCESS.getStatusId());
						status.setStatusShortDescription(StatusEnum.INPROCESS.getStatusShortDescription());
						techReg.setStepBStatus(status);
						techReg.setStepBSubmittedDate(new Date());
						//GRT 4.0 : Update number of sumbit
						techReg.setNumberOfSubmit("1");
						
						//Defect #790-Make the submitted child element to In-Process
						if( techReg.getExpSolutionElements() != null ){
							for( ExpandedSolutionElement expSE : techReg.getExpSolutionElements() ){
								if( expSE.isSelectForRemoteAccess() || expSE.isSelectForAlarming() ){
									if(expSE.getExpSolnElemntId()!=null)
									{
											expSE.setRetestStatus(StatusEnum.INPROCESS.getStatusShortDescription());
									}
								}
							}
						}
						
						this.updateTechnicalRegistrationStepBStatus(techReg, StatusEnum.INPROCESS);
						
						//Update the status in Site registration table
						this.getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.INPROCESS, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
						this.sendStepBEmail(siteRegistration.getRegistrationId(), StatusEnum.INPROCESS, siebelSrNumber);
					}
				}
			} else if(sls!=null && sls.size()>0) {
				
				trResult = this.doTechnicalAlarmRegistration(siteRegistration, null, sls, null);
				
				if (!trResult) {

					//Nothing
				}
				if( trResult ){
					for(SiteList sl : sls) {
						Status status = new Status();
						status.setStatusId(StatusEnum.INPROCESS.getStatusId());
						status.setStatusShortDescription(StatusEnum.INPROCESS.getStatusShortDescription());
						sl.setStepBStatus(status);
						if(srRequest != null) {
							sl.setStepBSRRequest(srRequest);
							logger.debug("Setting srRequest.getSiebelSRNo():" + srRequest.getSiebelSRNo());
							//sl.setArtSrNo(srRequest.getSiebelSRNo());
							siebelSrNumber = srRequest.getSiebelSRNo();
						}
						sl.setStepBSubmittedDate(new Date());
						//GRT 4.0 : Update number of sumbit
						sl.setNumberOfSubmit("1");
						//Defect #790-Make the submitted child element to In-Process
						if( sl.getExpSolutionElements() != null ){
							for( ExpandedSolutionElement expSE : sl.getExpSolutionElements() ){
								if( expSE.isSelectForRemoteAccess() || expSE.isSelectForAlarming() ){
									if(expSE.getExpSolnElemntId()!=null)
									{
											expSE.setRetestStatus(StatusEnum.INPROCESS.getStatusShortDescription());
									}
								}
							}
						}
						this.updateSiteListStepBStatus(sl, StatusEnum.INPROCESS);
						this.sendStepBEmail(siteRegistration.getRegistrationId(), StatusEnum.INPROCESS, siebelSrNumber);
					}
				}
			}
				    	
			return returnFlag;
	    }
	    
	    public TechnicalRegistration updateTechnicalRegistrationStepBStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	    	 logger.debug("Entering RegistrationService : updateTechnicalRegistrationStepBStatus");
	    	 TechnicalRegistration tr = null;
	    	 try {
	    		 tr = getTechnicalOnBoardingDao().updateTechnicalRegistrationStepBStatus(technicalRegistration, status);
	    	 } catch(Throwable throwable) {
	    		 logger.error("", throwable);
	    	 } finally {
	    	 logger.debug("Exiting RegistrationService : updateTechnicalRegistrationStepBStatus");
	    	 }
	    	return tr;
	    }
	    
	    public SiteList updateSiteListStepBStatus(SiteList siteList, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering RegistrationService : updateSiteListStepBStatus");
	        SiteList resultObject = null;
	        try {
	        resultObject = getTechnicalOnBoardingDao().updateSiteListStepBStatus(siteList, status);
		   	 } catch(Throwable throwable) {
		   		 logger.error("", throwable);
		   	 } finally {
	        logger.debug("Exiting RegistrationService : updateSiteListStepBStatus");
		   	 }
	        return resultObject;
	    }
	    
	    public boolean doTechnicalAlarmRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, 
	    																					ARTOperationType operationType) throws Exception {
			logger.debug("Entering TechnicalOnBoardingServiceImpl.doTechnicalAlarmRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
			logger.debug("Entering doTechnicalAlarmRegistration:" + sr.getRegistrationId());
			try {
				if(sr != null && ((trs != null && trs.size() > 0) || (sls != null && sls.size() > 0))) {
					if( trs != null && trs.size() > 0) {
						return handleAlarmTRs(sr, trs, operationType);
					} else if(sls != null && sls.size() > 0) {
						return handleAlarmSLs(sr, sls);
					}
				}
			} catch(Throwable throwable) {
				logger.error("Exception in TechnicalOnBoardingServiceImpl.doTechnicalAlarmRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType) : ", throwable);
				return false;
			} finally {
				logger.debug("Exiting doTechnicalAlarmRegistration:" + sr.getRegistrationId());
				logger.debug("Exiting TechnicalOnBoardingServiceImpl.doTechnicalAlarmRegistration(SiteRegistration sr, List<TechnicalRegistration> trs, List<SiteList> sls, ARTOperationType operationType)");
			}
			return true;
		}
	    
	    public boolean handleAlarmTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)throws Exception {
			logger.debug("Entering TechnicalOnBoardingServiceImpl.handleAlarmTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
			logger.debug("Entering handleTRs for RegistrationId:" + sr.getRegistrationId());
			try {
				
				if(sr != null && trs != null && trs.size() > 0) {
					List<TechRegInputDto> dtos = new ArrayList<TechRegInputDto>();
					
					for (TechnicalRegistration tr : trs) {
						if(tr != null) {
							TechRegInputDto dto = new TechRegInputDto();
							
							//dto.setTechRegId(tr.getTechnicalOrder().getOrderId());
							dto.setTechRegId(tr.getTechnicalRegistrationId());
							dto.setMainSeid(tr.getSolutionElementId());
							dto.setGatewaySEID(tr.getPrimarySalGWSeid());
							dto.setAlmid(tr.getAlarmId());
							dto.setAccessType(tr.getAccessType());
							//Get the operation type
							String opType = null;
							opType = getOperationType(tr.getAccessType(), tr.isSelectForAlarming(), tr.isSelectForRemoteAccess());
							dto.setOptype(opType);
							

							//ATOM Defect(736&776) - Add only if operationType is not empty
							if( opType!=null && !opType.trim().isEmpty() ){
								dtos.add(dto);
							}
							
							//After TOB Update SAL Alarm & Connectivity not storing/sending Child Elements
							boolean save_flag = false;
							if(!tr.getExpSolutionElements().isEmpty()){
					    		for(ExpandedSolutionElement expandedSolutionElement : tr.getExpSolutionElements()){
					    			expandedSolutionElement.setTechnicalRegistration(tr);
					    			if(expandedSolutionElement.getExpSolnElemntId() == null){
					    				save_flag = true;
					    			}
					    		}
					    	}
							
							if(save_flag){
								logger.debug("TOB Update case saving Exp Soln elements..");
								if(tr.getTechnicalOrder() != null && tr.getTechnicalOrder().getOrderType() != null){
									logger.debug("Order Type: " + tr.getTechnicalOrder().getOrderType());
								}								
								saveTechnicalRegistration(tr);
							}
							
							
							//Iterate over expanded solution list
							if( tr.getExpSolutionElements() != null ){
								for( ExpandedSolutionElement expSE : tr.getExpSolutionElements() ){
									if( expSE.isSelectForRemoteAccess() || expSE.isSelectForAlarming() ){
										if(expSE.getExpSolnElemntId()!=null)
										{
											TechRegInputDto childDto = new TechRegInputDto();
											childDto.setTechRegId(expSE.getExpSolnElemntId());
											childDto.setMainSeid(expSE.getSeID());
											childDto.setGatewaySEID(expSE.getPrimarySalGWSeid());
											childDto.setAlmid(expSE.getAlarmId());
											childDto.setAccessType(tr.getAccessType());
											
											//Get the operation type
											opType = null;
											opType = getOperationType(tr.getAccessType(), expSE.isSelectForAlarming(), expSE.isSelectForRemoteAccess());
											childDto.setOptype(opType);

											if( opType!=null && !opType.trim().isEmpty() ){
												dtos.add(childDto);
											}
										}
									}
								}
							}
						}
					}
					
					TechRegAlarmInputDto techRegAlarmInputDto = new TechRegAlarmInputDto();
					techRegAlarmInputDto.setGrtId(sr.getRegistrationId());
					techRegAlarmInputDto.setSoldTo(sr.getSoldToId());
					techRegAlarmInputDto.setTechRegInputDtoList(dtos);
					DeviceTOBResponseType response = getArtClient().technicalAlamRegistration(techRegAlarmInputDto);
					
					if(response!= null) {					
						ResponseHeaderType responseHeader = response.getHeader();
						
						String resultCode = responseHeader.getStatus();
						if ( (StringUtils.isNotEmpty(resultCode)) && (GRTConstants.SUCCESS.equalsIgnoreCase(resultCode)) ) {
							return true;
						}else{
							return false;
						}
					} else {
						return false;
					}
						
				}
				
				
			}  catch (Throwable throwable) {
				logger.error("Exception occured in TechnicalOnBoardingServiceImpl.handleAlarmTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType) : ", throwable);
				logger.debug("Exiting TechnicalOnBoardingServiceImpl.handleAlarmTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
				throwable.printStackTrace();
				return false;
			} finally {
				logger.debug("Exiting handleAlarmTRs for RegistrationId:" + sr.getRegistrationId());
			}
			
			logger.debug("Exiting TechnicalOnBoardingServiceImpl.handleAlarmTRs(SiteRegistration sr, List<TechnicalRegistration> trs, ARTOperationType operationType)");
			return true;
	    }
	    
	    
	    public boolean handleAlarmSLs(SiteRegistration sr, List<SiteList> sls) throws Exception {
			logger.debug("Entering TechnicalOnBoardingServiceImpl.handleAlarmSLs(SiteRegistration sr, List<SiteList> sls)");
			logger.debug("Entering handleAlarmSLs for SiteRegistration Id:" + sr.getRegistrationId());
			try {
				List<TechRegInputDto> dtos = new ArrayList<TechRegInputDto>();		
				if(sr != null && sls != null && sls.size() > 0) {
					for (SiteList sl : sls) {

						if(sl != null) {
							TechRegInputDto dto = new TechRegInputDto();
							
							dto.setTechRegId(sl.getId());
							dto.setMainSeid(sl.getSolutionElementId());
							dto.setGatewaySEID(sl.getPrimarySALGatewaySEID());
							dto.setAlmid(sl.getAlarmId());
							dto.setAccessType(GRTConstants.SAL);
							//Get the operation type
							String opType = null; 
							opType = getOperationType(dto.getAccessType(), sl.isSelectForAlarming(), sl.isSelectForRemoteAccess());
							dto.setOptype(opType);
							

							//ATOM Defect(736&776) - Add only if operationType is not empty
							if( opType!=null && !opType.trim().isEmpty() ){
								dtos.add(dto);
							}
							
							java.util.Set<ExpandedSolutionElement> resultsSet = new HashSet<ExpandedSolutionElement>();
					    	if(!sl.getExpSolutionElements().isEmpty()){
					    		for(ExpandedSolutionElement expandedSolutionElement : sl.getExpSolutionElements()){
					    			expandedSolutionElement.setSiteList(sl);
					    			resultsSet.add(expandedSolutionElement);
					    		}
					    	}
					    	sl.setExplodedSolutionElements(resultsSet);					    	
					    	
							//Save the site list
							getTechnicalOnBoardingDao().saveSiteListWithExpElem(sl);
							//Iterate over expanded solution list
							if( sl.getExplodedSolutionElements() != null ){
								for( ExpandedSolutionElement expSE : sl.getExplodedSolutionElements() ){
									if( expSE.isSelectForRemoteAccess() || expSE.isSelectForAlarming() ){
										if(expSE.getExpSolnElemntId()!=null)
										{
											TechRegInputDto childDto = new TechRegInputDto();
											childDto.setTechRegId(expSE.getExpSolnElemntId());
											childDto.setMainSeid(expSE.getSeID());
											childDto.setGatewaySEID(expSE.getPrimarySalGWSeid());
											childDto.setAlmid(expSE.getAlarmId());
											childDto.setAccessType(AccessTypeEnum.SAL.getDbAccessType());
											
											//Get the operation type
											opType = null;
											opType = getOperationType(dto.getAccessType(), expSE.isSelectForAlarming(), expSE.isSelectForRemoteAccess());
											childDto.setOptype(opType);
											
											if( opType!=null && !opType.trim().isEmpty() ){
												dtos.add(childDto);
											}											
										}
									}
								}
							}
						}
					}
					
					TechRegAlarmInputDto techRegAlarmInputDto = new TechRegAlarmInputDto();
					techRegAlarmInputDto.setGrtId(sr.getRegistrationId());
					techRegAlarmInputDto.setSoldTo(sr.getSoldToId());
					techRegAlarmInputDto.setTechRegInputDtoList(dtos);
					DeviceTOBResponseType response = getArtClient().technicalAlamRegistration(techRegAlarmInputDto);
					
					if(response!= null) {					
						ResponseHeaderType responseHeader = response.getHeader();
						
						String resultCode = responseHeader.getStatus();
						if ( (StringUtils.isNotEmpty(resultCode)) && (GRTConstants.SUCCESS.equalsIgnoreCase(resultCode)) ) {
							return true;
						}
					} else {
						return false;
					}
				}
			} catch (Throwable throwable) {
				logger.error("Exception occured in TechnicalOnBoardingServiceImpl.handleAlarmSLs(SiteRegistration sr, List<SiteList> sls) : ", throwable);
				logger.debug("Exiting TechnicalOnBoardingServiceImpl.handleAlarmSLs(SiteRegistration sr, List<SiteList> sls)");
				return false;
			} finally {
				logger.debug("Exiting handleAlarmTRs for RegistrationId:" + sr.getRegistrationId());
			}
			
			logger.debug("Exiting TechnicalOnBoardingServiceImpl.handleAlarmSLs(SiteRegistration sr, List<SiteList> sls)");
			return true;
	    }
	    
	    /**
	     * @param accessType
	     * @param testAlarm
	     * @param testConn
	     * @return String
	     * 	Get the operation type for retest functionality
	     */
	    public String getOperationType(String accessType, boolean testAlarm, boolean testConn){
	    	String opType = "";
	    	if( accessType!=null && accessType.equalsIgnoreCase(GRTConstants.SAL) ){
	    		if( testAlarm && testConn || testAlarm ){
	    			opType = ARTOperationType.TEST_ALARM.getOperationKey();
				} else if( testConn ){
					opType = ARTOperationType.TEST_CONNECTION.getOperationKey();
				}
	    	}else{
	    		opType = ARTOperationType.FULL_ONBOARD.getOperationKey();
	    	}
	    	return opType;
	    }
	    
	    public TechnicalRegistrationSummary addTechnicalOrder(TechnicalRegistrationSummary trSummaryOld) {
	    	TechnicalRegistrationSummary technicalRegistrationSummary = null; 
	    	
	    	try
	    	{
		    	List<TechnicalRegistrationSummary> trSummaryList = new ArrayList<TechnicalRegistrationSummary>();
		    	trSummaryList.add(trSummaryOld);	    	
		    	List<TechnicalOrder> listToSave = convertTechSummaryToTechOrder(trSummaryList);
		    	List<TechnicalOrder> savedList = getTechnicalOnBoardingDao().saveTechnicalOrderList(listToSave) ;	    	
		    	
		    	int index = 0;
		    	for (TechnicalRegistrationSummary ts:trSummaryList) {
		    		technicalRegistrationSummary = ts;
		    		TechnicalOrder t = savedList.get(index);
		    		technicalRegistrationSummary.setOrderId(t.getOrderId());
		    		index++;
		    	}
	    	} catch (DataAccessException die) {
				logger.error(die);
			} catch (Exception e) {
				logger.error(e);
			}
	    	
	    	return technicalRegistrationSummary;
	    }
	    
	    public Map<String, String> checkExistSRAndEntlForRetest(String soldToId, String seId, String accessType){
	    	checkExistSRAndEntitlement(soldToId, seId, accessType);
	    	return respMap;
	    }
	    Map<String, String> respMap = new HashMap<String, String>();
	    
	    
	    /***
	     * GRT4.00-BRS Requirement-BR-F.006
	     * 
	     * Check Re-test button click
	     */
	    public String checkExistSRAndEntitlement(String soldToId, String seId, String accessType) {
	    	respMap = new HashMap<String, String>();
	    	String response = GRTConstants.SUCCESS;
	    	String regId = "";
	    	String ent = "";
	    	//SSL VPN is not allowed for testing connectivity
	    	if(GRTConstants.ACCESS_TYPE_SSLVPN.equalsIgnoreCase(accessType) && accessType!=null ) {
	    		//response = "SSL VPN is not allowed for testing connectivity";
	    		response = GRTConstants.TOB_SSL_VPN_NOT_ALLOWED_CODE;
	    		respMap.put("RESPONSE", response);
	    		return response;
	    	} else {
	    		//Check if there is a SR/registration already existing in GRT DB for this seid
	    		try {
	    			regId = getTechnicalOnBoardingDao().getExistingRegistrationBySeid(seId);
	    			respMap.put("REGID", regId);
	    		} catch (DataAccessException die) {
	    			//response = "Exception occured while finding existing SR for this SEID";
	    			response = GRTConstants.TOB_EXCEPTION_FINDING_SR_CODE;
	    			respMap.put("RESPONSE", response);
	    			return response;
	    		}

	    		//Check for Alarming entitlement for seid
	    		try {
	    			if (regId.isEmpty()) {
	    				respMap.put("RESPONSE", response);
	    				return response;

	    			} else {
	    				response = GRTConstants.TOB_STEP_B_STATUS_FOUND_CODE;
	    				respMap.put("RESPONSE", response);
	    				return response;
	    			}

	    		} catch (Exception die) {
	    			response = GRTConstants.TOB_EXCEPTION_FINDING_ALARMING_CODE;
	    			respMap.put("RESPONSE", response);
	    			return response;
	    		}
	    	}
	    }
	    
	    /* GRT 4.0 changes ends */
	    
	    /**
	     * API to save submitTechnicalRegistrationSummary.
	     *
	     * @param trSummary List<TechnicalRegistrationSummary>
	     * @return void
	     */
	    public Boolean submitTechnicalRegistrationSummary(List<TechnicalRegistrationSummary> trSummary) throws DataAccessException, Exception {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.submitTechnicalRegistrationSummary");
	    	
	    	/* GRT4.0 change - entire technical order list saving is moved to submit process starts*/
	    	List<TechnicalOrder> listToSave = convertTechSummaryToTechOrder(trSummary);
	    	List<TechnicalOrder> savedList = getTechnicalOnBoardingDao().saveTechnicalOrderList(listToSave) ;	    	
	    	int index = 0;
	    	for (TechnicalRegistrationSummary ts:trSummary) {
	    		TechnicalOrder t = savedList.get(index);
	    		ts.setOrderId(t.getOrderId());
	    		index++;
	    	}
	    	/* GRT4.0 change - entire technical order list saving is moved to submit process ends*/
	    	
	    	boolean trResult = false;
	    	try {
		    	//logger.debug("Printing complete List:" + trSummary.toString());
		    	List <TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
		    	SiteRegistration siteRegistration = null;
		    	Map<String, List<Map<String, Integer>>> siebelEQNs = new HashMap<String, List<Map<String, Integer>>>();
		    	Map<String, List<Map<String, Integer>>> pipelineEQNs = new HashMap<String, List<Map<String, Integer>>>();
		    	String regId = null;
		    	for (TechnicalRegistrationSummary summary : trSummary) {
					logger.debug("INSIDE Submit:::" + summary.getMaterialCode());
					logger.debug("INSIDE Submit12345:::" + summary.getOrderId());
					regId = summary.getRegistrationId();
					TechnicalRegistration technicalRegistration = new TechnicalRegistration();
					
					TechnicalOrder technicalOrder = getTechnicalOnBoardingDao().getTechnicalOrderByOrderId(summary.getOrderId());
					technicalRegistration.setTechnicalOrder(technicalOrder);
					try {
						if(technicalOrder != null && summary.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_TR)) {
							if(StringUtils.isNotEmpty(technicalOrder.getEquipmentNumber())) {
								List<Map<String, Integer>> siebel = siebelEQNs.get(summary.getMaterialCode());
								List<Map<String, Integer>> pipeline = pipelineEQNs.get(summary.getMaterialCode());
								if(siebel == null && pipeline == null) {
									String metaString = technicalOrder.getEquipmentNumber();
									logger.debug("Starting parsing EquipmentNumber:" + metaString);
									String[] eqns = metaString.split("-");
									if(eqns != null) {
										if(eqns.length == 1) {
											String siebelMetaEqns = eqns[0];
											String [] eqnQtyPair = siebelMetaEqns.split("~");
											List<Map<String, Integer>> listData = new ArrayList<Map<String, Integer>>();
											for (String token : eqnQtyPair) {
												String [] data = token.split(":");
												Map<String, Integer> mapEntry = new HashMap<String, Integer>();
												if(data.length>1)
													mapEntry.put(data[0].trim(), Integer.valueOf(data[1].trim()));
												else
													mapEntry.put(data[0].trim(), 1);
												
												listData.add(mapEntry);
											}
											siebelEQNs.put(summary.getMaterialCode(), listData);
										} else if(eqns.length == 2) {
											String siebelMetaEqns = eqns[0];
											if(StringUtils.isNotEmpty(siebelMetaEqns)) {
												String [] siebelEQNQtyPair = siebelMetaEqns.split("~");
												List<Map<String, Integer>> siebelListData = new ArrayList<Map<String, Integer>>();
												for (String token : siebelEQNQtyPair) {
													String [] data = token.split(":");
													Map<String, Integer> mapEntry = new HashMap<String, Integer>();
													mapEntry.put(data[0].trim(), Integer.valueOf(data[1].trim()));
													siebelListData.add(mapEntry);
												}
												siebelEQNs.put(summary.getMaterialCode(), siebelListData);
											}
											String pipelineMetaEqns = eqns[1];
											String [] pipelineEQNQtyPair = pipelineMetaEqns.split("~");
											List<Map<String, Integer>> pipelineListData = new ArrayList<Map<String, Integer>>();
											for (String token : pipelineEQNQtyPair) {
												String [] data = token.split(":");
												Map<String, Integer> mapEntry = new HashMap<String, Integer>();
												mapEntry.put(data[0].trim(), Integer.valueOf(data[1].trim()));
												pipelineListData.add(mapEntry);
											}
											pipelineEQNs.put(summary.getMaterialCode(), pipelineListData);
										}
									}
									logger.debug("Siebel meta:" + siebelEQNs);
									logger.debug("Pipeline meta:" + pipelineEQNs);
								}

								if(siebelEQNs.containsKey(summary.getMaterialCode())) {
									List<Map<String, Integer>> listData = siebelEQNs.get(summary.getMaterialCode());
									logger.debug("processing Siebel data for MC:" + summary.getMaterialCode() + " listData:" + listData);
									if(listData != null) {
										for (Map<String, Integer> mapEntry : listData) {
											if(mapEntry != null) {
												String key = mapEntry.keySet().iterator().next();
												int quantity = mapEntry.get(key);
												if(quantity >= 1) {
													mapEntry.put(key, quantity - 1);
												} else {
													continue;
												}
												logger.debug("Setting EQN:" + key + " from Siebel.");
												technicalRegistration.setEquipmentNumber(key);
												technicalRegistration.setSummaryEquipmentNumber(null);
												break;
											}
										}
										if(StringUtils.isEmpty(technicalRegistration.getEquipmentNumber())) {
											siebelEQNs.remove(summary.getMaterialCode());
										}

									}
								}
								if(StringUtils.isEmpty(technicalRegistration.getEquipmentNumber()) && pipelineEQNs.containsKey(summary.getMaterialCode())) {
									List<Map<String, Integer>> listData = pipelineEQNs.get(summary.getMaterialCode());
									logger.debug("processing pipeline data for MC:" + summary.getMaterialCode() + " listData:" + listData);
									if(listData != null) {
										for (Map<String, Integer> mapEntry : listData) {
											if(mapEntry != null) {
												String key = mapEntry.keySet().iterator().next();
												int quantity = mapEntry.get(key);
												if(quantity >= 1) {
													mapEntry.put(key, quantity - 1);
												} else {
													continue;
												}
												logger.debug("Setting SummaryEQN:" + key + " from pipeline.");
												technicalRegistration.setSummaryEquipmentNumber(key);
												technicalRegistration.setEquipmentNumber(null);
												break;
											}
										}
									}
								}
							}
						} else if(summary.getOrderType().equals(GRTConstants.TECH_ORDER_TYPE_TR_UPDATE)) {
							technicalRegistration.setEquipmentNumber(technicalOrder.getEquipmentNumber());
							technicalRegistration.setSummaryEquipmentNumber(technicalOrder.getSummaryEquipmentNumber());
						}
					}catch (Throwable throwable) {
						logger.error("", throwable);
					}

					if(StringUtils.isEmpty(technicalRegistration.getEquipmentNumber()) && StringUtils.isNotEmpty(technicalOrder.getEquipmentNumber())) {
						if(technicalOrder.getEquipmentNumber().indexOf("~") < 0 && technicalOrder.getEquipmentNumber().indexOf(":") < 0) {
							logger.debug("Overriding EQN from TO:" + technicalOrder.getEquipmentNumber());
							technicalRegistration.setEquipmentNumber(technicalOrder.getEquipmentNumber());
						}
					}
					if(StringUtils.isEmpty(technicalRegistration.getSummaryEquipmentNumber()) && StringUtils.isNotEmpty(technicalOrder.getSummaryEquipmentNumber())) {
						if(technicalOrder.getSummaryEquipmentNumber().indexOf("~") < 0 && technicalOrder.getSummaryEquipmentNumber().indexOf(":") < 0) {
							logger.debug("Overriding SummaryEQN from TO:" + technicalOrder.getSummaryEquipmentNumber());
							technicalRegistration.setSummaryEquipmentNumber(technicalOrder.getSummaryEquipmentNumber());
						}
					}
					String accessType = summary.getAccessTypes();
					technicalRegistration.setAccessType(accessType);
					if (accessType.equalsIgnoreCase(AccessTypeEnum.IP.getUiAccessType())) {
						technicalRegistration.setAccessType(AccessTypeEnum.IP.getDbAccessType());
					} else if (accessType.equalsIgnoreCase(AccessTypeEnum.IPO.getUiAccessType())) {
						technicalRegistration.setAccessType(AccessTypeEnum.IPO.getDbAccessType());
					}
					technicalRegistration.setGroupId(summary.getGroupId());
					technicalRegistration.setOperationType(summary.getOperationType());
					technicalRegistration.setSolutionElementId(summary.getSeId());
					technicalRegistration.setRandomPassword(summary.getRandomPassworddb());
					technicalRegistration.setAuthorizationFileId(summary.getAuthFileID());
					technicalRegistration.setConnectivity(summary.getConnectivity());
					technicalRegistration.setCreatedDate(new Date());
					technicalRegistration.setDialInNumber(summary.getDialInNumber());
					technicalRegistration.setMid(summary.getMid());
					technicalRegistration.setSid(summary.getSid());
					technicalRegistration.setSoftwareRelease(summary.getSoftwareRelease());
					technicalRegistration.setSolutionElement(summary.getSolutionElement());
					technicalRegistration.setIpAddress(summary.getIpAddress());
					technicalRegistration.setSeidOfVoice(summary.getSeidOfVoicePortal());
					technicalRegistration.setCheckSesEdge(summary.getCheckIfSESEdgeDb());
					technicalRegistration.setCheckReleaseHigher(summary.getSampBoardUpgradedDb());
					technicalRegistration.setPrivateIpAddress(summary.getPrivateIP());
					technicalRegistration.setCmProduct(summary.isCmProduct());
					technicalRegistration.setCmMain(false);
					if(summary.getMainCM() != null && summary.getMainCM().equalsIgnoreCase(GRTConstants.YES)){
						technicalRegistration.setCmMain(true);
					}
					technicalRegistration.setSystemProductRelease(summary.getSystemProductRelease());
					Status s = new Status();
					s.setStatusId(summary.getStatusId());
					technicalRegistration.setStatus(s);
					Status stepBStatus = new Status();
					stepBStatus.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
					stepBStatus.setStatusDescription(StatusEnum.NOTINITIATED.getStatusDescription());
					technicalRegistration.setStepBStatus(stepBStatus);
					technicalRegistration.setTemplate(summary.getProductTemplate());
					technicalRegistration.setProductCode(summary.getProductType());
					technicalRegistration.setRemoteDeviceType(summary.getRemoteDeviceType());
					technicalRegistration.setCmMainSoldToId(summary.getCmMainsoldTo());
					technicalRegistration.setCmMainSid(summary.getCmMainSID());
					technicalRegistration.setCmMainSeid(summary.getSolutionElementId());
					technicalRegistration.setPrimarySalGWSeid(summary.getPrimarySalgwSeid());
					technicalRegistration.setSecondarySalGWSeid(summary.getSecondarySalgwSeid());
					technicalRegistration.setPrimarySoldToId(summary.getPrimarySoldToId());
					technicalRegistration.setSecondarySoldToId(summary.getSecondarySoldToId());
					technicalRegistration.setOutboundCallingPrefix(summary.getOutboundPrefix());
					technicalRegistration.setHardwareServer(summary.getHardwareServer());
					technicalRegistration.setAlarmOrigination(summary.getAlarmOrigination());
					
					if(summary.getSolutionElement() != null){
						CompatibilityMatrix compatibilityMatrix = getCompatibilityMatrix(summary.getSolutionElement());
						if(compatibilityMatrix != null){
							technicalRegistration.setModel(compatibilityMatrix.getModel());
							technicalRegistration.setRemoteAccess(compatibilityMatrix.getRemoteAccess());
							technicalRegistration.setTransportAlarm(compatibilityMatrix.getTransportAlarm());
						}
					}
					technicalRegistration.setStepASubmittedDate(new Date());
					technicalRegistration.setUsername(summary.getUserName());
					technicalRegistration.setPassword(summary.getPassword());
					//GRT 4.0 Change : Save Nick Name in Technical Registration
					technicalRegistration.setNickname(summary.getNickName());
					//GRT 4.0 Changes
					technicalRegistration.setPopUpHiddenValue(summary.getPopUpHiddenValue());
					//technicalRegistration.setAction(summary.getAction());
					
					//AUX MC Changes
					if(summary.getAuxMCShowFlag() != null && summary.getAuxMCShowFlag().equalsIgnoreCase(GRTConstants.BLOCK)){
						technicalRegistration.setAuxMC(true);
						if(summary.getIsAuxMCSEIDFlag() != null && summary.getIsAuxMCSEIDFlag().equalsIgnoreCase(GRTConstants.YES)){
							technicalRegistration.setAuxMCRegType(true);
							if(summary.getAuxMCMainSEID() != null){
								logger.debug("Setting AUX MC details for RegistrationId: " + technicalRegistration.getTechnicalOrder().getSiteRegistration().getRegistrationId());
								logger.debug("Setting AUX MC details for AuxMCMainSEID: " + summary.getAuxMCMainSEID());
								logger.debug("Setting AUX MC details for AuxMCMid: " + summary.getAuxMCMid());
								logger.debug("Setting AUX MC details for AuxMCSid: " + summary.getAuxMCSid());
								technicalRegistration.setAuxMCMainSeid(summary.getAuxMCMainSEID());	
								//setting SID/MID from AUX MC Main SEID
								technicalRegistration.setMid(summary.getAuxMCMid());
								technicalRegistration.setSid(summary.getAuxMCSid());
							}
						}else if(summary.getIsAuxMCSEIDFlag() != null && summary.getIsAuxMCSEIDFlag().equalsIgnoreCase(GRTConstants.NO)){
							technicalRegistration.setAuxMCRegType(false);
						}
					}else{
						technicalRegistration.setAuxMC(false);
					}
					
					if(summary.getTechnicalRegistrationId()==null)
						saveTechnicalRegistration(technicalRegistration);
					else
						technicalRegistration.setTechnicalRegistrationId(summary.getTechnicalRegistrationId());
					
					if(GRTConstants.UPDATE.equalsIgnoreCase(summary.getAction()) && GRTConstants.YES.equalsIgnoreCase(summary.getConnectivity())){
						//getAccessTypeForAssets( ).updateTechnicalRegistrationBySEID(summary.getSeId());
						getBaseHibernateDao().updateTechnicalRegistrationBySEID(summary.getSeId());
					}
					trs.add(technicalRegistration);
				}
		    	//technicalOrder.setTechnicalRegistrations(trs);
		    	siteRegistration = getTechnicalOnBoardingDao().getSiteRegistration(regId);
		    	trResult = this.doTechnicalRegistration(siteRegistration, trs, null, null);
		    	//Defect 238-If failed Art call
		    	if(trResult==false)
		    	{
		    		for(TechnicalRegistration tr: trs){
		    			TechnicalRegistration technicalRegistration = new TechnicalRegistration();
		    			
		    			technicalRegistration.setTechnicalRegistrationId(tr.getTechnicalRegistrationId());
		    			updateTechnicalRegistrationStatus(technicalRegistration, StatusEnum.SAVED, siteRegistration);
		    			//Defect #282
		    			//Update the status in Site registration table
						this.getBaseHibernateDao().updateSiteRegistrationStatus(siteRegistration, StatusEnum.SAVED, ProcessStepEnum.TECHNICAL_REGISTRATION, false);
		    		}
		    	}
		    	String soldToId = siteRegistration.getSoldToId();
				logger.debug("The Registration Id is :::" +  regId + "::: for SoldToId :::" + soldToId);
				List<PipelineSapTransactions> pipelineTransactions = this.getTechnicalOnBoardingDao().getPipelineTechnicallyRegisterableRecords(soldToId);
				if(pipelineTransactions != null && pipelineTransactions.size() > 0){
			    	logger.debug("Pipeline List Size ::: " + pipelineTransactions.size());
			    	for (TechnicalRegistration tr : trs) {
			    	TechnicalRegistration technicalRegistration = this.getTechnicalOnBoardingDao().getTechnicalRegistration(tr.getTechnicalRegistrationId());
			    		
			    		logger.debug("Technical Registration Id:::" + technicalRegistration.getTechnicalRegistrationId() + ":: technicalRegistration.getErrorCode()::" + technicalRegistration.getErrorCode());
			    		if(technicalRegistration.getErrorCode() == null || technicalRegistration.getErrorCode().equals("0")) {
			    			if(technicalRegistration.getTechnicalOrder().getEquipmentNumber().contains("-")) {
			    				if (technicalRegistration.getSummaryEquipmentNumber() != null && technicalRegistration.getEquipmentNumber() == null) {
				    				for (PipelineSapTransactions transaction : pipelineTransactions) {
				        				logger.debug("TRANSACTION ::: " + transaction.getAction() + "EQN # ::" + transaction.getEquipmentNumber());
				        				if(technicalRegistration.getTechnicalOrder().getMaterialCode().equals(transaction.getMaterialCode()) && GRTConstants.TECH_ORDER_TYPE_IB.equals(transaction.getAction()) && ((int)transaction.getAlreadyTRedQty() < (int)transaction.getQuantity())) {
				        					transaction.setAlreadyTRedQty(transaction.getAlreadyTRedQty() +1);
				        					this.getTechnicalOnBoardingDao().updatePipelineTransactionsAlreadyTRedQty(transaction);
				        					break;
						        		}
				        			}
			    				}
			        		}
						}
					}
				}
			} catch(Throwable throwable) {
	    		logger.error("", throwable);
	    	} finally {
	    		logger.debug("Exiting TechnicalOnBoardingServiceImpl.submitTechnicalRegistrationSummary");
	    	}
	    	return trResult;
	   }
	    
	   /* API to introspect SALGateway by SalConcentrator.
	     *
	     * @param gatewaySeid String
	     * @return DTO SALGatewayIntrospection
	     */
	   public SALGatewayIntrospection introspectSALGateway(String gatewaySeid, String soldToId) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.introspectSALGateway");
	    	SALGatewayIntrospection salGateway;
	        try {
	        	salGateway = getArtClient().introspectSALGateway(gatewaySeid);
	        	if(salGateway.getAccount() == null) {
	        		Account account = this.getSiebelClient().queryAccount(soldToId);
	        		if ( account != null) {
	        			salGateway.setAccount(account);
	        		}
	        	}
	        } catch (Throwable throwable) {
	        	logger.error("Error getSALMigrationEligibleAssets", throwable);
	            throw new DataAccessException(TechnicalOnBoardingService.class, throwable .getMessage(), throwable);
	        }
	        logger.debug("Exiting TechnicalOnBoardingServiceImpl.introspectSALGateway");
	        return salGateway;
	    }
	    
	    public Integer validateSeCodeAndSid(String sid, String seCode) throws DataAccessException {
	     	logger.debug("Entering TechnicalOnBoardingServiceImpl.validateSeCodeAndSid for sid:" + sid + ", secode:" + seCode +":");
	     	int result = 0;
	     	try {
	     		if(StringUtils.isNotEmpty(sid) && StringUtils.isNotEmpty(seCode)) {
	           		 if(this.getTechnicalOnBoardingDao().isSIDValidForSeCode(sid)) {
	           			 if(seCode.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCWSW)) {
	           				List<String> seCodes = getTechnicalOnBoardingDao().getSeCodesForSid(sid);
	           				int ACCWSW=0;
	           				int ACCWTK = 0;
	           				if(seCodes != null && seCodes.size() > 0) {
	           					for (String seCodeData : seCodes) {
									if(seCodeData.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCWSW)) {
										if((++ACCWSW >= 1 && ACCWTK >= 1) || (ACCWSW >= 2)) {
											return result = -1;
										}
									} else if(seCodeData.equalsIgnoreCase(GRTConstants.SECODE.ACC_ACCWTK)) {
										if(ACCWSW >= 1 && ++ACCWTK >= 1) {
											return result = -1;
										}
									}
								}
	           					result = 1;
	           				}
	           			 } else {
	           				 result = 1;
	           			 }
	           		 }
	     		}
	 	    } catch (Throwable throwable) {
	 			logger.error("", throwable);
	 			throw new DataAccessException(TechnicalOnBoardingServiceImpl.class, throwable.getMessage(), throwable);
	 		} finally {
	 			logger.debug("Exiting TechnicalOnBoardingServiceImpl.validateSeCodeAndSid for sid:" + sid + ", secode:" + seCode);
	 		}
	 		return result;
	    }
	    
	    public List<String> validateSIDAndFL(String sid, String fl, String seCode) throws Exception {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.validateSIDAndFL with SID:" + sid + " :: and FL :" + fl);
	    	List<String> l1 = new ArrayList<String>();
	    	try {
	    		List<String> fls = this.getTechnicalOnBoardingDao().validateSIDAndFL(sid);
		    	if (fls.isEmpty()) {
		    		l1.add("2");
		    	} else {
		    		if (fls.contains(fl)) {
		    			l1.add("0");
		    		} else {
		    			l1.add("1");
		    			l1.add(fls.get(0));
		    		}
		    	}
	    	} catch(Throwable throwable){
	    		l1 = null;
	    		logger.error("", throwable);
		    } finally {
	    		logger.debug("Exiting TechnicalOnBoardingServiceImpl.validateSIDAndFL");
		    }
	    	return l1;
	    }
	    
	    public List<String> validateSIDMID(String sid) throws Exception {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.validateSIDMID with SID:" + sid);
	    	List<String> mids = new ArrayList<String>();;
	    	try {
	    		mids = this.getTechnicalOnBoardingDao().validateSIDAndMID(sid);
	    	} catch(Throwable throwable){
	    		mids = null;
	    		logger.error("", throwable);
		    } finally {
	    		logger.debug("Exiting TechnicalOnBoardingServiceImpl.validateSIDMID");
		    }
	    	return mids;
	    }
	    
	    public List<ExpandedSolutionElement> getAssetsWithSameSIDandMID(String SID, String MID, String SEID) throws DataAccessException{
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
	    	List<ExpandedSolutionElement> expandedSolutionElements = this.getTechnicalOnBoardingDao().getAssetsWithSameSIDandMID(SID, MID, SEID);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
	    	return expandedSolutionElements;
	    }
	    
	    public List<ExpandedSolutionElement> getAssetsWithSameSIDandMIDandSEID(String SID, String MID, String SEID) throws DataAccessException{
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
	    	List<ExpandedSolutionElement> expandedSolutionElements = this.getTechnicalOnBoardingDao().getAssetsWithSameSIDandMIDandSEID(SID, MID, SEID);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
	    	return expandedSolutionElements;
	    }
	    
	    public List<ExpandedSolutionElement> getSALGWDetails(String SEID) throws DataAccessException{
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.getSALGWDetails for SEID:" + SEID);
	    	List<ExpandedSolutionElement> expandedSolutionElements = this.getTechnicalOnBoardingDao().getSALGWDetails(SEID);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.getSALGWDetails for SEID:" + SEID);
	    	return expandedSolutionElements;
	    }
	    
	    public void saveSalSiteList(SiteList siteList) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl.saveSalSiteList");
	    	this.getTechnicalOnBoardingDao().saveSalSiteList(siteList);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl.saveSalSiteList");
	    }
	    
	    public String saveSalSiteListOnly(SiteList siteList) throws DataAccessException {
	    	return this.getTechnicalOnBoardingDao().saveSalSiteListOnly(siteList);	    	
	    }
	    
	    /**
	     * ART AFID validation service
	     * @param afId
	     * @return
	     * @throws Exception
	     */
	    public String AFIDService(String afId) throws Exception {
			return this.getArtClient().AFIDService(afId);

	    }

	    /**
	     * ART SID MID validation service
	     * @param sid
	     * @param mid
	     * @return
	     * @throws Exception
	     */
	    public String SIDMIDService(String sid, String mid) throws Exception {
			return this.getArtClient().SIDMIDService(sid, mid);

	    }
	    
	    public String getHyperLinkedSalGatewaySeid(String primarySalGatewaySeid, String secondarySalGatewatSeid, String soldToId) {
        	//Begin to populate the salGateWaySeid
        	StringBuilder sb = new StringBuilder();
        	if(primarySalGatewaySeid != null){
        		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+primarySalGatewaySeid+"','"+soldToId+"')\" >"+primarySalGatewaySeid+"</a>" );
        	}
        	if(secondarySalGatewatSeid != null){
        		sb.append("&nbsp;+&nbsp;");
        		sb.append("<a href=\"#\" onclick=\"showSALGatewayDetails('"+secondarySalGatewatSeid+"','"+soldToId+"')\" >"+secondarySalGatewatSeid+"</a>" );
        	}
        	if(sb != null &&  !StringUtils.isEmpty(sb.toString()) ){
        		return sb.toString();
        	} else {
        		return null;
        	}
        	//End to populate the salGateWaySeid
	    }
	    
	    public SiteList updateSiteListStatus(SiteList siteList, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalOnBoardingServiceImpl : updateSiteListStatus()");
	        SiteList resultObject = null;
	        resultObject = getTechnicalOnBoardingDao().updateSiteListStatus(siteList, status);
	        logger.debug("Exiting TechnicalOnBoardingServiceImpl : updateSiteListStatus()");
	        return resultObject;
	    }
	    
	    public TechnicalRegistration updateTechRegStatus(TechnicalRegistration technicalRegistration, StatusEnum status, SiteRegistration siteRegistration) throws DataAccessException {
	    	return this.updateTechnicalRegistrationStatus(technicalRegistration, status, siteRegistration);
	    }
	    public TechnicalRegistration updateTechnicalRegistrationStatus(TechnicalRegistration technicalRegistration, StatusEnum status, SiteRegistration siteRegistration) throws DataAccessException {

	    	TechnicalRegistration tr = getTechnicalOnBoardingDao().updateTechnicalRegistrationStatus(technicalRegistration, status);
	    	
	    	return tr;
	    }
	    public void updateStepAResubmittedDate(TechnicalRegistration technicalRegistration) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl : updateStepAResubmittedDate");
	    	getTechnicalOnBoardingDao().updateStepAResubmittedDate(technicalRegistration);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl : updateStepAResubmittedDate");
	    }
	
	 public TechnicalOnBoardingDao getTechnicalOnBoardingDao(final String userId) {
	        if (technicalOnBoardingDao == null) {
	        	technicalOnBoardingDao = new TechnicalOnBoardingDaoImpl();            
	        }
	        return technicalOnBoardingDao;
	    }
	 
	 public TechnicalRegistration fetchTechnicalRegistrationByID(String techRegId) throws DataAccessException {
	    	logger.debug("Entering TechnicalOnBoardingServiceImpl : fetchTechnicalRegistrationByID");
	    	TechnicalRegistration technicalRegistration = getTechnicalOnBoardingDao().getTechnicalRegistration(techRegId);
	    	logger.debug("Exiting TechnicalOnBoardingServiceImpl : deleteTechnicalOrderForRegId");
	    	return technicalRegistration;
	    }
	

	@Override
	public List<TechnicalRegistrationSummary> getTechnicalRegistrationSummaryListSearch(
			String soldToId, String registrationId, int offSet, int fetchSize,
			List<SearchParam> searchParams, String userId)
			throws DataAccessException {
		if(logger.isDebugEnabled())
	        logger.debug("Starting for RegistrationID [" + registrationId + "] and soldToID [" + soldToId + "]");

	    	TechnicalOnBoardingDao regDao = getTechnicalOnBoardingDao(userId);
	        
	        SiteRegistration siteRegistration = regDao.getSiteRegistration(registrationId);
	        String statusId = siteRegistration.getTechRegStatus().getStatusId();
	        List<TechnicalOrder> registrationList = null;
	       
	        	try{
	        		registrationList = generateDataFromSiebel(soldToId, siteRegistration);
	        	} catch(Exception exp){
	        		throw new DataAccessException(TechnicalOnBoardingService.class, exp.getMessage(), exp);
	        	}
	        
	        
	    	if(registrationList==null) {
	          registrationList = regDao.getTechnicalRegistrationSummaryList(registrationId, new String[]{GRTConstants.TECH_ORDER_TYPE_TR});
	    	}
	        List<TechnicalRegistrationSummary> technicalRegistrationSummaryList = new ArrayList<TechnicalRegistrationSummary>();
	        TechnicalRegistrationSummary technicalSummary = null;

	        if(registrationList != null) {
		        for (TechnicalOrder technicalOrder : registrationList) {
		        	technicalSummary = convertTechOrderToTechSummary(technicalOrder);
		            technicalSummary.setRegistrationId(registrationId);
		            technicalRegistrationSummaryList.add(technicalSummary);
		        	}
		    }

	        return technicalRegistrationSummaryList;
	}

	@Override
	public CMMain validateCMMain(String cmMainSeid) {
		logger.debug("Entering TechnicalOnBoardingServiceImpl : validateCMMain");
		CMMain cmMain = null;
		try{
			List<String> piePollableSeCodes = this.getBaseHibernateDao().getPIEPollableSeCodes();
			cmMain = getTechnicalOnBoardingDao().validateCMMain(cmMainSeid,piePollableSeCodes);
		}catch(Exception e){
			logger.error("Error validating CM Main - "+e.getMessage());
		}
		logger.debug("Exiting TechnicalOnBoardingServiceImpl : validateCMMain");
		return cmMain;
	}
	
	@Override
	public AUXMCMain validateAUXMCMainSEID(String auxMCMainSeid) {
		logger.debug("Entering TechnicalOnBoardingServiceImpl : validateAUXMCMainSEID");
		AUXMCMain auxMCMain = null;
		try{
			auxMCMain = getTechnicalOnBoardingDao().validateAUXMCMainSEID(auxMCMainSeid);
		}catch(Exception e){
			logger.error("Error validating AUX MC Main - "+e.getMessage());
		}
		logger.debug("Exiting TechnicalOnBoardingServiceImpl : validateAUXMCMainSEID");
		return auxMCMain;
	}
	
	@Override
	public TechnicalRegistration getConnectivityDetailsBySeid(String seid)
			throws Exception {
		logger.debug("Entering TechnicalOnBoardingServiceImpl : getConnectivityDetailsBySeid");
			TechnicalRegistration tr = getTechnicalOnBoardingDao().getConnectivityDetailsBySeid(seid);
		logger.debug("Entering TechnicalOnBoardingServiceImpl : getConnectivityDetailsBySeid");
		return tr;
	}

	@Override
	public int deleteTechOrderByRegId(String regId) {
		int flag = getTechnicalOnBoardingDao().deleteTechOrderByRegId(regId);
		return flag;
	}

	@Override
	public int deleteTechRegByOrderIds(List<String> orderIds) {
		int flag = getTechnicalOnBoardingDao().deleteTechRegByOrderIds(orderIds);
		return flag;
	}
}
