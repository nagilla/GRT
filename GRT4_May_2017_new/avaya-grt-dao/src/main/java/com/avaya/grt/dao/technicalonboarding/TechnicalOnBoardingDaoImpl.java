package com.avaya.grt.dao.technicalonboarding;

import java.math.BigDecimal;
import java.text.DateFormat;
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
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.mappers.ArtErrorCode;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.HardwareServer;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.AUXMCMain;
import com.grt.dto.CMMain;
import com.grt.dto.Product;
import com.grt.dto.SALGateway;
import com.grt.dto.TRConfig;
import com.grt.util.AccessTypeEnum;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.OrderBySqlExp;
import com.grt.util.SearchParam;
import com.grt.util.StatusEnum;
import com.grt.util.StatusLikeExpression;
import com.grt.util.TechnicalRegistrationUIUtil;


public class TechnicalOnBoardingDaoImpl extends BaseHibernateDao implements TechnicalOnBoardingDao{
	private static final Logger logger = Logger.getLogger(TechnicalOnBoardingDaoImpl.class);
	
	public static Map<String, Set<String>> mc2GroupIdMappings = new HashMap<String, Set<String>>();
	 
	
	 /**
     * Method to get the registration summary based on type.
     *
     * @param registrationId
     * @return
     */
    public int getTechnicalRegistrationSummaryListCount(String registrationId, String registrationType, List<SearchParam> searchParams) {
        int technicalRegistrationSummaryListSize = 0;

        try {
            Criteria criteria = getSessionForGRT().createCriteria(TechnicalOrder.class);
            criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
            criteria.add(Restrictions.eq("orderType", registrationType));

            criteria = enhanceCriteriaWithSearchParams(criteria, searchParams);

            //The default order must be put under enhanceCriteriaWithSearchParams within which it may also have addOrder clause with the same field name
            criteria.addOrder(Order.asc("materialCode"));

            criteria.setProjection(Projections.rowCount());
            technicalRegistrationSummaryListSize = new Integer(criteria.uniqueResult().toString());
        } catch (Exception ex) {
            logger.error("Error in TechnicalOnBoardingDao : getTechnicalRegistrationSummaryListCount : " + ex.getMessage());
        }

        return technicalRegistrationSummaryListSize;
    }
    
    private Criteria enhanceCriteriaWithSearchParams(Criteria criteria, List<SearchParam> searchParams) {
		if(searchParams == null) {
			return criteria;
		}
		//sort the parameter by orderByWeight for adding orderBy by sequence
		Collections.sort(searchParams);
		for(SearchParam searchParam : searchParams) {
	    	if(searchParam != null &&
	    			(searchParam.getDisabled() == null || !searchParam.getDisabled())) {
	    		Criterion oneCriterion = null;
	    		if(searchParam.getIsNull() != null) {
		    		if(searchParam.getIsNull()) {
		    			oneCriterion = Restrictions.isNull(searchParam.getFieldName());
		    		}
		    		else {
		    			oneCriterion = Restrictions.isNotNull(searchParam.getFieldName());
		    		}
	    		}
	    		else if(StringUtils.isNotEmpty(searchParam.getFieldValue())) {
		    		if(searchParam.getFieldType() != null && SearchParam.FIELD_TYPE_NUMERIC.equalsIgnoreCase(searchParam.getFieldType())) {
		    			Long fieldValue = Long.parseLong(searchParam.getFieldValue());
		    			if(SearchParam.NUMERIC_OPR_LESS_THAN.equalsIgnoreCase(searchParam.getNumericOpr())) {
		    				oneCriterion = Restrictions.lt(searchParam.getFieldName(), fieldValue);
		    			}
		    			else if(SearchParam.NUMERIC_OPR_LESS_EQUAL_THAN.equalsIgnoreCase(searchParam.getNumericOpr())) {
		    				oneCriterion = Restrictions.le(searchParam.getFieldName(), fieldValue);
		    			}
		    			else if(SearchParam.NUMERIC_OPR_GREATER_THAN.equalsIgnoreCase(searchParam.getNumericOpr())) {
		    				oneCriterion = Restrictions.gt(searchParam.getFieldName(), fieldValue);
		    			}
		    			else if(SearchParam.NUMERIC_OPR_GREATER_EQUAL_THAN.equalsIgnoreCase(searchParam.getNumericOpr())) {
		    				oneCriterion = Restrictions.ge(searchParam.getFieldName(), fieldValue);
		    			}
		    			else {
		    				oneCriterion = Restrictions.eq(searchParam.getFieldName(), fieldValue);
		    			}
		    		}
		    		else {
		    			oneCriterion = Restrictions.ilike(searchParam.getFieldName(), searchParam.getFieldValue(), MatchMode.ANYWHERE);
		    		}
	    		}
	    		if(oneCriterion != null && (searchParam.getReverseMatch() == null || !searchParam.getReverseMatch())) {
	    			criteria.add(oneCriterion);
	    		}
	    		else {
	    			criteria.add(Restrictions.not(oneCriterion));
	    		}

	    		if(StringUtils.isNotEmpty(searchParam.getOrderByDirection())) {
		    		if(SearchParam.ORDER_BY_DESC.equalsIgnoreCase(searchParam.getOrderByDirection())) {
		    			criteria.addOrder(Order.desc(searchParam.getFieldName()));
		    		}
		    		else {
		    			criteria.addOrder(Order.asc(searchParam.getFieldName()));
		    		}
	    		}

	    	}
	    }
		String sql = "to_number(regexp_substr(MATERIAL_CODE,'^[0-9]+'))" ;
		criteria.addOrder(OrderBySqlExp.sqlFormula(sql));
		return criteria;
	}

    public List<TechnicalOrder> getTechnicalRegistrationSummaryList(String registrationId, String[] registrationType) {
        List<TechnicalOrder> technicalRegistrationSummaryList = null;

        try {
            logger.debug("getTechnicalRegistrationSummaryList with registrationId: " + registrationId);
            Criteria criteria = getSessionForGRT().createCriteria(TechnicalOrder.class);
            criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
            criteria.add(Restrictions.in("orderType", registrationType));

            criteria.addOrder(Order.asc("materialCode"));

            technicalRegistrationSummaryList = criteria.list();
        } catch (Throwable throwable) {
            logger.error("Error in getTechnicalRegistrationSummaryList", throwable);
        }

        return technicalRegistrationSummaryList;
    }
    
    /**
     * Method to get the Solution for the Technical Registration List.
     *
     * @param soldToNumber String
     * @return productList contains Product
     */
    public Collection<Product> getTechnicallyRegisterableRecordsNew(String soldToNumber) throws DataAccessException {
    	logger.debug("Entering  getTechnicallyRegisterableRecordsNew method call");
    	Set<String> equipmentNumbers = new HashSet<String>();
    	StringBuffer sqlQuery = new StringBuffer();
    	
		Map<String, Product> productMap = new HashMap<String, Product>();
		List<Product> productList = new ArrayList<Product>();
		Query query=getSessionForSiebel().getNamedQuery("getTechnicallyRegisterableRecordsNew");
		query.setParameter("soldToNumberParam", soldToNumber);
		try{
			query.setFetchSize(500);
		List queryResult = query.list();
		Product product = null;
		mc2GroupIdMappings=this.initializeTRConfigData();
		if (!mc2GroupIdMappings.isEmpty()) {
			logger.debug("Inside getTechnicallyRegisterableRecords - mc2GroupIdMappingsSize :::" + mc2GroupIdMappings.size());
		}
		final int size = queryResult.size();
		for (int i = 0; i < size; i++) {
			Object[] listItem = (Object[]) queryResult.get(i);
			if (mc2GroupIdMappings.containsKey((String) listItem[0])) {
				product = new Product();
				product.setMaterialCode((String) listItem[0]);
				//The Material code description must be fetched from SAP i.e. MATERIAL_MASTER 
				product.setShortDescription((String) listItem[1]);
				product.setQuantity(((BigDecimal) listItem[2]).intValue());
				product.setCreatedDate((String) listItem[3]);
				if (listItem[4] != null) {
					product.setSolutionElement(listItem[4].toString());
				}
				product.setProductLine((String) listItem[6]);
				
				String equipmentNumber = (String) listItem[5];
				product.setTempEquipmentNumber(equipmentNumber);
				if(equipmentNumber != null){
					equipmentNumbers.add(equipmentNumber);
				}
				productList.add(product);

			}
		}
		
		List<String> listOfEquipmentNumbers = new ArrayList<String>(equipmentNumbers);
		List<String> dbEqipmentsList = null;
		try{
			dbEqipmentsList = this.validateEquipmentNoFromTechnicalRegistrationNew(listOfEquipmentNumbers, soldToNumber);
		} catch(Throwable error){
			dbEqipmentsList = null;
			logger.error("Exception while validating Equipments from TR for SoldTo:"+soldToNumber);
		}
    	for(Product pt: productList){
    		String eqptNumbr = pt.getTempEquipmentNumber();
			Product existingProduct = null;
    		if(dbEqipmentsList!= null && dbEqipmentsList.contains(eqptNumbr)){
    			logger.debug("dbEqipmentsList"+dbEqipmentsList.size());
    			// Not Empty Logic
        		logger.debug("Equipment Number :::" + eqptNumbr +  " and ValidatedEquipmentNumber is : " + eqptNumbr);
        		logger.debug("Material Code :::" + pt.getMaterialCode() +  " and Product Qty is : " + pt.getQuantity());
        		//EQN:QTY~EQN:QTY-EQN:QTY~EQN:QTY
        		if (pt.getQuantity() == 1) {
        			continue;
        		}
        		pt.setEquipmentNumber(eqptNumbr + ":" + (pt.getQuantity()-1));
				if((existingProduct = productMap.get(pt.getMaterialCode())) != null ) {
					existingProduct.setQuantity(existingProduct.getQuantity() + (pt.getQuantity()-1));
					existingProduct.setEquipmentNumber(existingProduct.getEquipmentNumber() + "~" + pt.getEquipmentNumber());
				} else {
					productMap.put(pt.getMaterialCode(), pt);
				}    			
    		}else{
    			// Empty Logic
				//EQN:QTY~EQN:QTY-EQN:QTY~EQN:QTY
				pt.setEquipmentNumber(eqptNumbr + ":" + pt.getQuantity());
				if((existingProduct = productMap.get(pt.getMaterialCode())) != null ) {
					existingProduct.setQuantity(existingProduct.getQuantity() + pt.getQuantity());
					existingProduct.setEquipmentNumber(existingProduct.getEquipmentNumber() + "~" + pt.getEquipmentNumber());
				} else {
					productMap.put(pt.getMaterialCode(), pt);
				}    			
    		}
    	}
		
		
		}catch(Throwable error){
			logger.debug("Exiting  getTechnicallyRegisterableRecordsNew method after Exception");
			throw new DataAccessException(TechnicalOnBoardingDao.class, error.getMessage(), error);
		}
		logger.debug("Exiting  getTechnicallyRegisterableRecordsNew() method");
		return productMap.values();
	}    
    
    /**
    *
    * @param equipmentNumbers
    * @param soldTo
    * @return
    */
   public List<String> validateEquipmentNoFromTechnicalRegistrationNew(List<String> equipmentNumbers, String soldTo)throws DataAccessException {
   	logger.debug("Entering RegistrationDao : validateEquipmentNoFromTechnicalRegistrationNew");
   	ArrayList listOfList = getSubLists(equipmentNumbers);
   	StringBuilder inClause = new StringBuilder();
   	if(listOfList != null && listOfList.size()>0){
       	for(int i=0; i<listOfList.size(); i++){
       		String param = "equipmentNumberParam"+i;
       		inClause.append(" tr.equipment_number IN ( :").append(param).append(" ) ");
       		if(i< (listOfList.size()-1) ){
       			inClause.append("OR ");
       		}
       	}
   	}else{
   		return null;
   	}
   	String sqlQuery = "Select tr.equipment_number from technical_registration tr, technical_order techo, site_registration sr "
   		+ " where SR.REGISTRATION_ID = TECHO.SITE_REGISTRATION_ID "
   		+ " and TECHO.ORDER_ID=TR.TECHNICAL_ORDER_ID "
   		+ " and sr.sold_to_id=:soldToNumberParam "
   		+ " and techo.order_type='TR' "
   		+ " and tr.equipment_number is not null "
   		+ " and ( "
   		+ inClause.toString()
   		+ " ) "
   		+ " and tr.status_id='1003'";

   	Query query = getSessionForGRT().createSQLQuery(sqlQuery);
   	query.setParameter("soldToNumberParam", soldTo);
   	//query.setParameterList("equipmentNumberParam", equipmentNumbers);
   	if(listOfList != null && listOfList.size()>0){
       	for(int i=0; i<listOfList.size(); i++){
       		String param = "equipmentNumberParam"+i;
       		List<String> subList = (List<String>)listOfList.get(i);
       		query.setParameterList(param, subList);
       	}
   	}else{
   		return null;
   	}

   	List<String> queryResult = null;
   	try{
   		queryResult = query.list();
   	}catch(Throwable th){
   		throw new DataAccessException(TechnicalOnBoardingDao.class, th.getMessage(), th);
   	}
   	   logger.debug("Existing TechnicalOnBoardingDao : validateEquipmentNoFromTechnicalRegistrationNew");
       return queryResult;
   }
   private ArrayList getSubLists(List<String> eqipmentNumbers){

		logger.debug("Entering TechnicalOnBoardingDao : getSubLists()");
		if(eqipmentNumbers != null ){
       	int size = eqipmentNumbers.size();
       	if(size > 0){
	        	int numberOfLists = size/1000;
	        	int reminder = size % 1000;
	        	logger.debug("THe Number of Lists:::"+numberOfLists);
	        	if(reminder > 0){
	        		numberOfLists = numberOfLists+1;
	        		logger.debug("Remider Case: Totla Sub Lists required are::"+ numberOfLists);
	        	} else {
	        		logger.debug("No Reminder Case: Totla Sub Lists required are::"+ numberOfLists);
	        	}
	        	ArrayList listOfLists = new ArrayList();
	        	int startIndex =0; int endIndex = 1000;
	        	if(size <= endIndex){
	        		listOfLists.add(eqipmentNumbers);
	        	}else{

	    	    	for(int i=0; i<(numberOfLists-1); i++ ){
	    	    		List<String> subList = eqipmentNumbers.subList(startIndex, endIndex);
	    	    		listOfLists.add(subList);
	    	    		startIndex = startIndex+1000; endIndex = endIndex +1000;
	        			if(endIndex > size){
	        				endIndex = size;
	        			}
	    	    	}
	        	}
	        	logger.debug("Exiting TechnicalOnBoardingDao : getSubLists():: listOfLists.size():"+ listOfLists.size());
	        	return listOfLists;
       	} else {
       		logger.debug("Exiting TechnicalOnBoardingDao : getSubLists():: eqipmentNumbers.size():0" );
       		return null;
       	}
		} else {
			logger.debug("Exiting TechnicalOnBoardingDao : getSubLists():: eqipmentNumbers: null");
			return null;
		}
	}
   
   /**
    * API to get all Pipeline SAP transactions.
    *
    *
    * @throws DataAccessException Exception
    */
   public List<PipelineSapTransactions> getPipelineTechnicallyRegisterableRecords(String soldToId) throws DataAccessException {
       logger.debug("Entering TechnicalOnBoardingDao : getAllPipelineSapTransactions");
       List<PipelineSapTransactions> pipelineTransactions = null;
       try {
       	Criteria criteria =  getSessionForGRT().createCriteria(PipelineSapTransactions.class);
           criteria.add(Restrictions.eq("technicallyRegisterable", true));
           criteria.add(Restrictions.eq("processed", false));
           criteria.add(Restrictions.eq("shipTo", soldToId));
           criteria.addOrder(Order.desc("dateTime"));
           pipelineTransactions = criteria.list();

       } catch (HibernateException hibEx) {
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       } catch (Exception ex){
           getSessionForGRT().getTransaction().rollback();
           ex.printStackTrace();
           logger.error("Error in TechnicalOnBoardingDao : getAllPipelineSapTransactions : " +  ex.getMessage());
       }

       logger.debug("Exiting TechnicalOnBoardingDao : getAllPipelineSapTransactions");

       return pipelineTransactions;
   }
   
   /**
    * Method to get the Solution for the Technical Registered List.
    *
    * @param soldToNumber String
    * @return productList contains Product
    */
   public List<Product> getTechnicallyRegisteredRecords(String soldToNumber) throws DataAccessException {
	   
	   logger.debug("getTechnicallyRegisteredRecords"+soldToNumber);
		
   	StringBuffer sqlQuery = new StringBuffer();
   		List<Product> productList = new ArrayList<Product>();
		Query query=getSessionForSiebel().getNamedQuery("getTechnicallyRegisteredRecords");
		query.setCacheable(true);
		query.setFetchSize(500);
		query.setParameter("soldToNumberParam", soldToNumber);
		List queryResult = query.list();
		logger.debug("queryResult"+queryResult.size());
		Product product = null;
		mc2GroupIdMappings=this.initializeTRConfigData();
		trConfigs=this.fetchTRConfigs();
		if (!mc2GroupIdMappings.isEmpty()) {
			logger.debug("Inside getTechnicallyRegisteredRecords - mc2GroupIdMappingsSize :::" + mc2GroupIdMappings.size());
		}	
		final int size = queryResult.size();
		for (int i = 0; i < size; i++) {
			Object[] listItem = (Object[]) queryResult.get(i);
			
			String materialCode = (String) listItem[0];
			String seCode = "";
			boolean flag = false;
			if (listItem[4] != null) {
				seCode = listItem[4].toString();
			}
			if (mc2GroupIdMappings.containsKey(materialCode)) {
				// Cross check the SE Code
				Set<String> groupIds = mc2GroupIdMappings.get(materialCode);
				for(String groupId : groupIds){
					
					List<TRConfig> trConfigList = trConfigs.get(groupId);
					for(TRConfig trConfig : trConfigList){
						if(trConfig.getSeCode() != null && trConfig.getSeCode().equalsIgnoreCase(seCode)){
							flag = true;
							break;
						} else {
							logger.debug("MaterialCode "+materialCode+" with SE Code "+seCode+" doesnot exist in siebel view.");
						}
					}
				}
				if(flag){

					product = new Product();
					product.setMaterialCode(materialCode);
					//The Material code description must be fetched from SAP i.e. MATERIAL_MASTER
					product.setShortDescription((String) listItem[1]);
					product.setQuantity(((BigDecimal) listItem[2]).intValue());
					product.setCreatedDate((String) listItem[3]);
					if (StringUtils.isNotEmpty(seCode)) {
						product.setSolutionElement(seCode);
					}
					if (listItem[5] != null) {
						product.setSeId(listItem[5].toString());
					}
					product.setEquipmentNumber((String) listItem[6]);
					product.setSId((String) listItem[7]);
					product.setMId((String) listItem[8]);
					product.setProductLine(listItem[9] != null ? listItem[9].toString():"");
					product.setSalSeIdPrimarySecondary(listItem[10] != null ? listItem[10].toString():"");
					product.setGroupId(listItem[11] != null ? listItem[11].toString():"");
					product.setAlarmId(listItem[12] != null ? listItem[12].toString():"");
					productList.add(product);
				
				}
			}
		}
		return productList;
	}
   
   /**
	 * Change the ownership for the give siteRegistration
	 *
	 * @param siteRegistration siteRegistration
	 * @return registration siteRegistration
	 * @throws DataAccessException
	 */
   public SiteRegistration changeOwnership(SiteRegistration siteRegistration) throws DataAccessException {
		logger.debug("Entering TechnicalOnBoardingDao : changeOwnership()");
		SiteRegistration registration = null;
		try {
			Session session = getSessionForGRT();
			session.beginTransaction();

			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());


			registration.setFirstName(siteRegistration.getFirstName());
			registration.setLastName(siteRegistration.getLastName());
			registration.setUserName(siteRegistration.getUserName());
			registration.setReportEmailId(siteRegistration.getReportEmailId());
			registration.setReportPhone(siteRegistration.getReportPhone());

			session.saveOrUpdate(registration);
			session.getTransaction().commit();

		} catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
					.getMessage(), hibEx);
		}
		logger.debug("Exiting TechnicalOnBoardingDao : changeOwnership()");

   	return registration;
   }
   
   /**
	 * API to get unique SeCodes for a paramterized groupId.
	 * 
	 * @param groupId
	 * 
	 * @return comma seperated seCodes
	 * @throws DataAccessException
	 */
   public String getGroupSecodes(String groupId) throws DataAccessException {
       logger.debug("Entering getGroupSecodes for groupId:" + groupId);
       List<String> result = new ArrayList<String>();
       String seCodes = "";
   	Query query = null;
   	try{
   		if(StringUtils.isNotEmpty(groupId)) {
		  query=getSessionForSiebel().getNamedQuery("getGroupSecodes");
		    	query.setParameter("GROUPID", groupId);
		    	result = query.list();
		    	if(result != null && result.size() > 0)  {
			    	StringBuilder seCodesSF = new StringBuilder("  ");
		    		for (String seCode : result) {
		    			seCodesSF.append(seCode + ", ");
					}
		    		seCodes = seCodesSF.substring(0, seCodesSF.length()-2);
		    	}
	    		
   		}
	    }  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex.getMessage(), ex);
		} finally {
			logger.debug("Exiting getGroupSecodes for groupId:" + groupId);
		}
   	return seCodes;
   }
   
   /**
    * API to get SAL Gateways
    *
    * @param soldToId String
    * @param salSEID String
    * @return SALGateway DTO List<SiteList>
    */
   public List<SALGateway> getSALGateways(String soldToId, String salSEID, String salFlag) throws DataAccessException {
   	logger.debug("Entering TechnicalOnBoardingDao : getSALGateways");
   	List<SALGateway> salGateways = new ArrayList<SALGateway>();
		Query query = null;
		try {
			//non-merged SAL gateway records will show V00328?
			//To pick the SALGateways where any of the asset on a given soldTo is migrated - is Pending
	        String sqlQuery = "select orgext.loc, prodint.name material_code, prodint.alias_name material_description,"
	        				+ " sasset.x_se_cd, x_seid, sasset.created, nvl(dev_count, 0) device_num"
	        				+ " from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_prod_int prodint,"
	        				+ " (select x_ip_address, count(par_row_id) dev_count from siebel.s_asset_xm sasset_xm,"
	        				+ " (select sasset.row_id from siebel.s_asset sasset where sasset.status_cd <> 'Inactive' and sasset.x_seid is not null) asset"
	                        + " where par_row_id = asset.row_id and x_ip_address is not null group by x_ip_address) device_count"
	                        + " where orgext.row_id = sasset.owner_accnt_id and sasset.prod_id = prodint.row_id"
	                        + " and x_seid = device_count.x_ip_address(+)"
	                        + " and orgext.loc like 'SOLDTOID_PLACEHOLDER' and  sasset.status_cd <> 'Inactive' and sasset.x_seid is not null";
	        if(salSEID != null && StringUtils.isNotEmpty(salSEID)){
	        	sqlQuery += " and sasset.x_seid in ('SAL_GATEWAY_SEID_PLACEHOLDER')";
	        }
	        if(salFlag != null && StringUtils.isNotEmpty(salFlag)){
	        	sqlQuery += " and sasset.x_se_cd in ('SALGW', 'VSALGW')";
	        }

	        if(StringUtils.isNotEmpty(soldToId)){
	        	sqlQuery = sqlQuery.replaceFirst("SOLDTOID_PLACEHOLDER", soldToId);
	        } else {
	        	sqlQuery = sqlQuery.replaceFirst("SOLDTOID_PLACEHOLDER", "%");
	        }
	        if(salSEID != null && StringUtils.isNotEmpty(salSEID)){
	        	StringTokenizer strTokens = new StringTokenizer(salSEID, "+");
	    		String result = "";
	    		int i=0;
	    		while(strTokens.hasMoreTokens()){
	    			if(i != 0)
	    				result = result + "','";
	    			result = result + strTokens.nextToken();
	    			i++;
	    		}
	    		sqlQuery = sqlQuery.replaceFirst("SAL_GATEWAY_SEID_PLACEHOLDER", result);
	        }

	        query = getSessionForSiebel().createSQLQuery(sqlQuery);

	    	List<Object[]> resultSet = query.list();
	    	//Iterate through the result set
	    	if (null != resultSet && resultSet.size()>0) {
	    	for (Object[] object : resultSet) {
	    		SALGateway salGateway = new SALGateway();
	    		if (object != null){
	    				if(object[0] != null) {
	    					salGateway.setSoldTo(object[0].toString());
	    				}
	    				if(object[1] != null) {
	    					salGateway.setMaterialCode(object[1].toString());
	    				}
	    				if(object[2] != null) {
	    					salGateway.setMaterialDescription(object[2].toString());
	    				}
	    				if(object[3] != null) {
	    					salGateway.setSeCode(object[3].toString());
	    				}
	    				if (object[4] != null) {
							salGateway.setSeid(object[4].toString());
						}
						if (object[5] != null) {
							String strDate = object[5].toString();

							DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							Date date = (Date)formatter.parse(strDate);
							SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
							String mmddyyyy = newFormat.format(date);
							salGateway.setGatewayCreatedDate(mmddyyyy);
						}
						if (object[6] != null) {
							salGateway.setDevicesBehindGateway(Long.parseLong(object[6].toString()));
						}
						salGateways.add(salGateway);
	    		}
	    	}
	    	} else {
   	    	logger.debug("NO SAL Gateways exists for soldToId ::"+ soldToId);
	    	}
		}  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex
					.getMessage(), ex);
		}
   	logger.debug("Exiting SiebelDao : getSALGateways");
   	return salGateways;
   }
   
   public List<String> getSPVersions(String productType, String template, String releaseNumber) throws DataAccessException {
   	logger.debug("Entering getSPVersions with productType:" + productType + " template:" + template + " releaseNumber:" + releaseNumber);
   	List<String> spReleases = new ArrayList<String>();
   	try {
   		if(StringUtils.isNotEmpty(productType) && StringUtils.isNotEmpty(releaseNumber)) {
        	String queryString = "Select productRelease from ProductRelease productRelease where upper(productRelease.productType)='" + productType.toUpperCase() + "' and productRelease.releaseNumber='" + releaseNumber + "' and productRelease.systemPlatform=1";
               Query query = getSessionForGRT().createQuery(queryString);
               List<ProductRelease> productReleases = query.list();
               if (productReleases != null || productReleases.size() > 0) {
               	boolean process = true;
               	if(StringUtils.isNotEmpty(template)) {
               		process = false;
               		for (ProductRelease release : productReleases) {
							if(StringUtils.isEmpty(release.getTemplate()) || (StringUtils.isNotEmpty(release.getTemplate()) && release.getTemplate().equalsIgnoreCase(template))) {
								process = true;
								break;
							}
						}
               	}
               	if(process){
			    		String sqlQuery = "SELECT RELEASE_NUMBER FROM SYSTEM_PLATFORM_RELEASES";
			            Query query1 = getSessionForGRT().createSQLQuery(sqlQuery);
			            List queryResult1 = query1.list();
			            for (int i = 0; i < queryResult1.size(); i++) {
			            	Object lstItem = queryResult1.get(i);
			            	if(lstItem instanceof String) {
			            		spReleases.add((String)lstItem);
			            	}
			            }
               	}
               }
			 } else {
			 	throw new DataAccessException("Manadatory data is missing");
			 }
   	} catch(Throwable throwable) {
   		logger.error("", throwable);
   	} finally {
   		logger.debug("Exiting getSPVersions with productType:" + productType + " template:" + template + " releaseNumber:" + releaseNumber);
   	}
   	return spReleases;
   }

   public List<HardwareServer> getHardwareServersTemplate(String template) throws DataAccessException {
   	logger.debug("Entering TechnicalOnBoardingDao : getHardwareServersTemplate");
   	List<HardwareServer> hardwareServers = null;
       try {
           if(StringUtils.isNotEmpty(template)) {
           	String queryString = "Select hardwareServer from HardwareServer hardwareServer where (upper(hardwareServer.template)='" + template.toUpperCase() + "') ";
           	Query query = getSessionForGRT().createQuery(queryString);
           	hardwareServers = query.list();
               if (hardwareServers == null || hardwareServers.size() <= 0) {
                   return null;
               }
           }

       } catch (HibernateException hibEx) {
       	logger.error("", hibEx);
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }
       logger.debug("Exiting TechnicalOnBoardingDao : getHardwareServersTemplate");
       return hardwareServers;

   }
   
   public SiteRegistration getTechnicalRegistrationDetails(String registrationId, boolean isSuperUser) throws DataAccessException {
       logger.debug("Entering TechnicalOnBoardingDao : getTechnicalRegistrationDetailList()");
          	   logger.debug("RegistrationId---------->"+registrationId +"<-------------------");

	   SiteRegistration siteRegistration = null;
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           logger.debug(session.isOpen());
           siteRegistration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
           siteRegistration.setSuperUser(isSuperUser);
           RegistrationType registrationType = siteRegistration.getRegistrationType();
           siteRegistration.setStrRegistrationType(registrationType.getRegistrationId());
           
           if (siteRegistration != null) {
        	    session.refresh(siteRegistration);
        	    List<TechnicalRegistration> nonSalTrs =new ArrayList<TechnicalRegistration>();
        	    try{
        	    if(siteRegistration.getNonSalTrs()!=null){
        	    	nonSalTrs = siteRegistration.getNonSalTrs();
	            logger.debug("The SIZE OF nonSalTrs IS ===========================> :: "+ nonSalTrs.size());
        	    }
        	    }catch(NullPointerException ex){
        	    	
        	    }
	            /* Temporary fix as lazy loading is not working --Starts */
        	    try{
	            if (nonSalTrs.isEmpty()) {
	            	nonSalTrs = fetchExplictTechRegs(siteRegistration, isSuperUser, "");
	            }
        	    }catch(NullPointerException eX){}
	            /* Temporary fix as lazy loading is not working --Ends */
	               
	           	siteRegistration.setTechnicalRegistrationDetailList(nonSalTrs);
	           	logger.debug("siteRegistration getTechnicalRegistrationDetailList"+siteRegistration.getTechnicalRegistrationDetailList());
	           	logger.debug("getTechnicalRegistrationDetailList:::::"+siteRegistration.getTechnicalRegistrationDetailList().size());
	           	List<TechnicalRegistration> salTrs =new ArrayList<TechnicalRegistration>();
	           	try{
	           	salTrs =siteRegistration.getSalTrs();
	           	}catch(NullPointerException Ex){}
	           	siteRegistration.setSalRegistrationSummaryList(salTrs);
	          logger.debug("The SIZE OF SalTrs IS ==============================> :: "+ salTrs.size());
	       	try{
	           /* Temporary fix as lazy loading is not working --Starts */
	            if (salTrs.isEmpty()) {
	            	salTrs = fetchExplictTechRegs(siteRegistration, isSuperUser, GRTConstants.ACCESS_TYPE_SAL);
	            }
	            /* Temporary fix as lazy loading is not working --Ends */
	    	}catch(NullPointerException Ex){}
	           List<SiteList> siteLists = siteRegistration.getAssets();
	           logger.debug("siteLists"+siteRegistration.getAssets());
	           siteRegistration.setSalMigrationSummaryList(siteLists);
	           boolean isEligibleForStepB =false;
	           try{
	           isEligibleForStepB =siteRegistration.isEligibleForStepB();
	           }catch(NullPointerException Ex){}
	           if (isEligibleForStepB){
	           	siteRegistration.setAlarmAndConnectivityDisabled(false);
	           } else {
	           	siteRegistration.setAlarmAndConnectivityDisabled(true);
	           }
	           if( StatusEnum.COMPLETED.getStatusId().equals(siteRegistration.getTechRegStatus().getStatusId()) ){
	           	siteRegistration.setAlarmAndConnectivityDisabled(true);
	           }

	           //GRT 4.0 Retest Records-BRS Requirement-BR-F.006
	           
	           List<TechnicalRegistration> retestTrs =new ArrayList<TechnicalRegistration>();
	           try{
	        	   retestTrs =fetchRetestRecords(siteRegistration, isSuperUser);
	        	   siteRegistration.setRetestTRList(retestTrs);
	           }catch(NullPointerException Ex){}
	           logger.debug("The SIZE OF ASSENTS IS =============================> :: "+ siteLists.size());

           }
           logger.debug("BEFORE COMMIT THE TRANSACTION BEFORE COMMIT THE TRANSACTION BEFORE COMMIT THE TRANSACTION " );
           session.getTransaction().commit();
       } catch (HibernateException hibEx) {
       	logger.error("The hibernate error========>", hibEx);
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }

   	logger.debug("siteRegistration content before return...  SIZE of setSalMigrationSummaryList() ==========@@@@@@@@@=========================>:: "+siteRegistration.getSalMigrationSummaryList().size() +"             ");
       logger.debug("Entering TechnicalOnBoardingDao : getTechnicalRegistrationDetailList()");
       return siteRegistration;
   }
   
   /* Temporary method as lazy loading is not fetching TechnicalRegistration records from TechnicalOrder */
   public List<TechnicalRegistration> fetchExplictTechRegs(SiteRegistration siteRegistration, 
		   boolean isSuperUser, String accessType) throws DataAccessException {
	   
	   List<TechnicalRegistration> resultObject = new ArrayList<TechnicalRegistration>();
       try {
           Session session = getSessionForGRT();
           
           List<String> orderList = new ArrayList<String>();
           Set<TechnicalOrder> technicalOrdersSet = siteRegistration.getTechnicalOrders();
           
           if(technicalOrdersSet != null && technicalOrdersSet.size() >0) {
	           for (TechnicalOrder technicalOrder : technicalOrdersSet) {
	        	   if (!GRTConstants.TECH_ORDER_TYPE_TR_RETEST.equalsIgnoreCase(technicalOrder.getOrderType()))
	        		   orderList.add(technicalOrder.getOrderId());
	           }
	           if (!orderList.isEmpty()) {
	        	 
		           Criterion queryFilterMap = prepareSubList(orderList, "technicalOrder.orderId"); 
		           Criteria criteria =  session.createCriteria(TechnicalRegistration.class);           
		           criteria.add(queryFilterMap);
		           List<TechnicalRegistration> objectList = criteria.list();
		           
		           if(objectList != null && objectList.size() > 0)  {
			    		for (TechnicalRegistration tr : objectList) {
			    			if (tr != null ){
								tr.getTechnicalRegistrationId();
								
								//For Non-Sal TRs
								if ( (accessType.isEmpty()) && 
										(!tr.getAccessType().equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType())) ){
									tr = TechnicalRegistrationUIUtil.getNonSalTRs(tr, isSuperUser, siteRegistration.getTechRegStatus().getStatusId());						    							    	 
									resultObject.add(tr);
								}
								
								//For Sal TRs
								if ( (accessType.equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType())) && 
										(tr.getAccessType().equalsIgnoreCase(AccessTypeEnum.SAL.getDbAccessType())) ){
									tr = TechnicalRegistrationUIUtil.getSalTRs(tr, isSuperUser, siteRegistration.getTechRegStatus().getStatusId());						    							    	 
									resultObject.add(tr);
								}
			    			}
			    		}
		           }
	           }
           }
       }
       catch (HibernateException hibEx) {
    	   throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
       }

       return resultObject;
   }
   /* Temporary method as lazy loading is not fetching TechnicalRegistration records from TechnicalOrder */
   
   /**
    * Method to get the technical order by the given technicalOrderId.
    *
    * @param technicalOrderId string
    * @return technicalOrder TechnicalOrder
    */
   public TechnicalOrder getTechnicalOrderByOrderId(String technicalOrderId) throws DataAccessException {
       logger.debug("Entering TechnicalOnBoardingDao : getTechnicalRegistration()");
       TechnicalOrder technicalOrder = null;
       try {
           Criteria criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class);
           criteria.add(Restrictions.eq("orderId", technicalOrderId));
           technicalOrder = (TechnicalOrder)  criteria.uniqueResult();
       } catch (HibernateException hibEx) {
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }
       logger.debug("Exiting TechnicalOnBoardingDao : getTechnicalRegistration()");

       return technicalOrder;

   }
   
   public void updatePipelineTransactionsAlreadyTRedQty(PipelineSapTransactions pipelineTransaction) throws DataAccessException{
       logger.debug("Entering TechnicalOnBoardingDao : updatePipelineTransactionsAlreadyTRedQty");
       logger.debug("PipeTransactionId :::" + pipelineTransaction.getPlSapTransactionId() + "::: RegistrationId :::" + pipelineTransaction.getRegistrationId());
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           session.saveOrUpdate(pipelineTransaction);
           session.getTransaction().commit();
           logger.debug("Exiting TechnicalOnBoardingDao : updatePipelineTransactionsAlreadyTRedQty");

       } catch (HibernateException hibEx) {
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }
   }
   
   public boolean isSIDValidForSeCode(String sid) throws DataAccessException {
       logger.debug("Entering isSIDValidForSeCode for sid:" + sid);
       boolean result = false;
       StringBuilder sb = new StringBuilder();
   	Query query = null;
   	try{
   		if(StringUtils.isNotEmpty(sid)) {
   			query=getSessionForSiebel().getNamedQuery("isSIDValidForSeCode");
		    	query.setParameter("SID", sid);

		    	List<String> resultSet = query.list();
		    	if(resultSet != null && resultSet.size() > 0) {
		    		result = true;
		    	}
   		}
	    }  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex.getMessage(), ex);
		}
		logger.debug("Exiting isSIDValidForSeCode for sid:" + sid + " result :" + result);
   	return result;
   }
   public List<String> getSeCodesForSid(String sid) throws DataAccessException {
       logger.debug("Entering getSeCodesForSid for sid:" + sid);
       List<String> result = new ArrayList<String>();
       StringBuilder sb = new StringBuilder();
   	Query query = null;
   	try{
   		if(StringUtils.isNotEmpty(sid)) {
   			query=getSessionForSiebel().getNamedQuery("getSeCodesForSid");
		    	query.setParameter("SID", sid);
		    	result = query.list();
   		}
	    }  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex.getMessage(), ex);
		} finally {
			logger.debug("Entering getSeCodesForSid for sid:" + sid);
		}
   	return result;
   }
   
   public List<String> validateSIDAndFL(String SID) throws DataAccessException{
   	logger.debug("Entering validateSIDAndFL:" + SID);
   	List<String> fls = new ArrayList<String>();
   	StringBuilder sb = new StringBuilder();
   	Query query = null;
   	try{
   			query=getSessionForSiebel().getNamedQuery("validateSIDAndFL");
	    	query.setParameter("SID", SID);
	    	List<String> resultSet = query.list();
	    	for (String fl : resultSet) {
	    		if(StringUtils.isNotEmpty(fl)) {
	    			logger.debug("FL:" + fl + ":");
	    			fls.add(fl);
	    		}
	    	}
	    }  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex
					.getMessage(), ex);
		}
		logger.debug("Exiting validateSIDAndFL:" + fls);
   	return fls;
   }
   
   public List<String> validateSIDAndMID(String SID) throws DataAccessException{
   	logger.debug("Entering validateSIDAndMID:" + SID);
   	List<String> mids = new ArrayList<String>();
   	StringBuilder sb = new StringBuilder();
   	Query query = null;
   	try{
   		query= getSessionForSiebel().getNamedQuery("validateSIDAndMID");
	    	query.setParameter("SID", SID);
	    	List<String> resultSet = query.list();
	    	for (String mid : resultSet) {
	    		if(StringUtils.isNotEmpty(mid)) {
	    			logger.debug("MID:" + mid + ":");
	    			mids.add(mid);
	    		}
	    	}
	    }  catch (HibernateException hibEx) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(TechnicalOnBoardingDao.class, ex
					.getMessage(), ex);
		}
		logger.debug("Exiting validateSIDAndMID:" + SID);
   	return mids;
}
   
   /**
    * Update SiteList Status with the given status
    *
    * @param siteRegistration
    * @param status
    * @return siteRegistration SiteRegistration
    * @throws DataAccessException
    */
   public SiteList updateSiteListStatus(SiteList siteList, StatusEnum status) throws DataAccessException {
       logger.debug("Entering RegistrationDao : updateSiteListStatus()");
       SiteList resultObject = null;
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           Criteria criteria =  session.createCriteria(SiteList.class);
           criteria.add(Restrictions.eq("id", siteList.getId()));
           resultObject = (SiteList)  criteria.uniqueResult();
           criteria =  session.createCriteria(Status.class);
           criteria.add(Restrictions.eq("statusId", status.getStatusId()));
           Status s = (Status)  criteria.uniqueResult();
           resultObject.setStatus(s);
           session.saveOrUpdate(resultObject);
           Status st = resultObject.getStatus();
           session.getTransaction().commit();
           logger.debug("The Site List satus=====================>"+st.getStatusId()+" Description is ::"+st.getStatusDescription());
           if (st != null && st.getStatusId()!= null) {
           	resultObject.setStatus(st);
           }
       } catch (HibernateException hibEx) {
       	logger.error("", hibEx);
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }
       logger.debug("Exiting TechnicalOnBoardingDao : updateSiteListStatus()");

       return resultObject;
   }
   
   public List<ExpandedSolutionElement> getAssetsWithSameSIDandMID(String SID, String MID, String SEID) throws DataAccessException{
   	logger.debug("Entering getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
   	List<ExpandedSolutionElement> expandedSolutionElements = new ArrayList<ExpandedSolutionElement>();
   	try {
	   		if(StringUtils.isEmpty(SID) || StringUtils.isEmpty(MID) || StringUtils.isEmpty(SEID)) {
	   			return expandedSolutionElements;
	   		}
	   		
	   		Query query=getSessionForSiebel().getNamedQuery("getAssetsWithSameSIDandMID");
	    	query.setParameter("sid", SID);
	    	query.setParameter("mid", MID);
	    	query.setParameter("seid", SEID);
	    	List<Object[]> resultSet = query.list();
	    	// Iterate through the result set
	    	for (Object[] object : resultSet) {
	    		ExpandedSolutionElement expandedSolutionElement = new ExpandedSolutionElement();
	    		expandedSolutionElement.setSeCode(object[0]!=null?object[0].toString():"");
	    		expandedSolutionElement.setSeID(object[1]!=null?object[1].toString():"");
	    		expandedSolutionElement.setAlarmId(object[2]!=null?object[2].toString():"");
	    		expandedSolutionElement.setIpAddress(object[3]!=null?object[3].toString():"");
	    		expandedSolutionElements.add(expandedSolutionElement);
	    	}
	    	logger.debug("After - Query Execution:"+expandedSolutionElements.size());
	    }  catch (HibernateException hibEx) {
	    	logger.error("", hibEx);
			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			logger.error("", ex);
			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
		} finally {
			logger.debug("Exiting getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
		}
   		return expandedSolutionElements;
   }
   
   /***
    * GRT 4.0 changes
    *  Get Assets from seibel by same SID,MID and SEID
    * @param SID
    * @param MID
    * @param SEID
    * @return
    * @throws DataAccessException
    */
   
   public List<ExpandedSolutionElement> getAssetsWithSameSIDandMIDandSEID(String SID, String MID, String SEID) throws DataAccessException{
	   	logger.debug("Entering getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
	   	List<ExpandedSolutionElement> expandedSolutionElements = new ArrayList<ExpandedSolutionElement>();
	   	try {
		   		if(StringUtils.isEmpty(SID) || StringUtils.isEmpty(MID) || StringUtils.isEmpty(SEID)) {
		   			return expandedSolutionElements;
		   		}
		   		
		   		Query query=getSessionForSiebel().getNamedQuery("getAssetsWithSameSIDandMIDandSEID");
		    	query.setParameter("sid", SID);
		    	query.setParameter("mid", MID);
		    	query.setParameter("seid", SEID);
		    	List<Object[]> resultSet = query.list();
		    	// Iterate through the result set
		    	for (Object[] object : resultSet) {
		    		ExpandedSolutionElement expandedSolutionElement = new ExpandedSolutionElement();
		    		expandedSolutionElement.setSeCode(object[0]!=null?object[0].toString():"");
		    		expandedSolutionElement.setSeID(object[1]!=null?object[1].toString():"");
		    		expandedSolutionElement.setAlarmId(object[2]!=null?object[2].toString():"");
		    		expandedSolutionElement.setIpAddress(object[3]!=null?object[3].toString():"");
		    		expandedSolutionElements.add(expandedSolutionElement);
		    	}
		    	logger.debug("After - Query Execution:"+expandedSolutionElements.size());
		    }  catch (HibernateException hibEx) {
		    	logger.error("", hibEx);
				throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
			} catch (Exception ex) {
				logger.error("", ex);
				throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
			} finally {
				logger.debug("Exiting getAssetsWithSameSIDandMID for SID:" + SID + " and MID:" + MID);
			}
	   		return expandedSolutionElements;
	   }
   
   public List<ExpandedSolutionElement> getSALGWDetails(String SEID) throws DataAccessException{
	   	logger.debug("Entering getSALGWDetails for SEID:" + SEID);
	   	List<ExpandedSolutionElement> expandedSolutionElements = new ArrayList<ExpandedSolutionElement>();
	   	try {
		   		if(StringUtils.isEmpty(SEID)) {
		   			return expandedSolutionElements;
		   		}
		   		
		   		Query query=getSessionForSiebel().getNamedQuery("getSALGWDetails");
		    	query.setParameter("seid", SEID);
		    	List<Object[]> resultSet = query.list();
		    	// Iterate through the result set
		    	for (Object[] object : resultSet) {
		    		ExpandedSolutionElement expandedSolutionElement = new ExpandedSolutionElement();
		    		expandedSolutionElement.setSeCode(object[0]!=null?object[0].toString():"");
		    		expandedSolutionElement.setSeID(object[1]!=null?object[1].toString():"");
		    		expandedSolutionElement.setAlarmId(object[2]!=null?object[2].toString():"");
		    		expandedSolutionElement.setIpAddress(object[3]!=null?object[3].toString():"");
		    		expandedSolutionElements.add(expandedSolutionElement);
		    	}
		    	logger.debug("After - Query Execution:"+expandedSolutionElements.size());
		    }  catch (HibernateException hibEx) {
		    	logger.error("", hibEx);
				throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
			} catch (Exception ex) {
				logger.error("", ex);
				throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
			} finally {
				logger.debug("Exiting getSALGWDetails for SEID:" + SEID);
			}
	   		return expandedSolutionElements;
	   }
   
   
   public String checkEntitlementBySeid(String soldToId, String seId) throws DataAccessException {
  		logger.debug("Entering checkEntitlementBySeid(String soldToId, String seId)");
	   	String entitlementId = "";
	   
	   	try {
	   		Query query=getSessionForSiebel().getNamedQuery("checkEntitlementBySeid");
  			query.setParameter("soldToId", soldToId);
  			query.setParameter("seId", seId);
  			List<String> resultSet = query.list();
  			
  			if (!resultSet.isEmpty()) entitlementId = resultSet.get(0); 
  	   		
  			logger.debug("After - Query Execution:"+resultSet.size());
  		}  catch (HibernateException hibEx) {
  			logger.error("", hibEx);
  			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
  		} catch (Exception ex) {
  			logger.error("", ex);
  			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
  		} finally {
  			logger.debug("Exiting checkEntitlementBySeid(String soldToId, String seId)");
  		}
	   
	   return entitlementId;
  }
   
   /**
    * Update SiteRegistration Status with the given status
    *
    * @param siteRegistration
    * @param status
    * @return siteRegistration SiteRegistration
    * @throws DataAccessException
    */
   public TechnicalRegistration updateTechnicalRegistrationStepBStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
       logger.debug("Entering TechnicalOnBoardingDaoImpl : updateTechnicalRegistrationStatus()");
       TechnicalRegistration resultObject = null;
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           Criteria criteria =  session.createCriteria(TechnicalRegistration.class);
           criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistration.getTechnicalRegistrationId()));
           resultObject = (TechnicalRegistration)  criteria.uniqueResult();
           criteria =  session.createCriteria(Status.class);
           criteria.add(Restrictions.eq("statusId", status.getStatusId()));
           Status s = (Status)  criteria.uniqueResult();
           resultObject.setStepBStatus(s);
           if(technicalRegistration.getStepBSRRequest() != null) {
           		resultObject.setStepBSRRequest(technicalRegistration.getStepBSRRequest());
           		logger.debug("technicalRegistration.getArtSrNo():" + technicalRegistration.getArtSrNo());
           		resultObject.setArtSrNo(technicalRegistration.getArtSrNo());
           		if(StringUtils.isNotEmpty(technicalRegistration.getDeviceLastAlarmReceivedDate())) {
           			resultObject.setDeviceLastAlarmReceivedDate(technicalRegistration.getDeviceLastAlarmReceivedDate());
           		}
           		if(StringUtils.isNotEmpty(technicalRegistration.getDeviceStatus())) {
           			resultObject.setDeviceStatus(technicalRegistration.getDeviceStatus());
           		}
           }
           
           if(GRTConstants.TRUE.equals(technicalRegistration.getCancelled())){
           		if(resultObject.getStepBSRRequest() != null){
           			session.delete(resultObject.getStepBSRRequest());
           		}
           		resultObject.setStepBSRRequest(null);
           }
           
           resultObject.setSelectForRemoteAccess(technicalRegistration.isSelectForRemoteAccess());
           resultObject.setSelectForAlarming(technicalRegistration.isSelectForAlarming());
           java.util.Set<ExpandedSolutionElement> expSolutionElements = getExpSolutionElementsSet(technicalRegistration.getExpSolutionElements());
           if(!expSolutionElements.isEmpty()){
           	resultObject.setExplodedSolutionElements(expSolutionElements);
           }
           
           if(technicalRegistration.getStepBSubmittedDate() != null){
           	resultObject.setStepBSubmittedDate(technicalRegistration.getStepBSubmittedDate());
           }
           
           //persisting SAL GW fetched from Siebel while Step B submission..
           if(technicalRegistration.getPrimarySalGWSeid() != null){
              	resultObject.setPrimarySalGWSeid(technicalRegistration.getPrimarySalGWSeid());
              }
           
           if(technicalRegistration.getStepBCompletedDate() != null){
           	resultObject.setStepBCompletedDate(technicalRegistration.getStepBCompletedDate());
           }
           if( technicalRegistration.getNumberOfSubmit()!=null ){
        	   resultObject.setNumberOfSubmit(technicalRegistration.getNumberOfSubmit());
           }
           session.saveOrUpdate(resultObject);
           session.getTransaction().commit();

       } catch (HibernateException hibEx) {
           throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
       }
       logger.debug("Exiting TechnicalOnBoardingDaoImpl : updateTechnicalRegistrationStatus()");

       return resultObject;
   }
   
   private java.util.Set<ExpandedSolutionElement> getExpSolutionElementsSet(List<ExpandedSolutionElement> expandedSolutionElements){
   		java.util.Set<ExpandedSolutionElement> resultsSet = new HashSet<ExpandedSolutionElement>();
   		if(!expandedSolutionElements.isEmpty()){
	    	for(ExpandedSolutionElement expandedSolutionElement : expandedSolutionElements){
	    		resultsSet.add(expandedSolutionElement);
	    	}
   		}
   		return resultsSet;
   }
   
   /**
    * Update SiteList StepB Status with the given status
    *
    * @param siteRegistration
    * @param status
    * @return siteRegistration SiteRegistration
    * @throws DataAccessException
    */
   public SiteList updateSiteListStepBStatus(SiteList siteList, StatusEnum status) throws DataAccessException {
       logger.debug("Entering TechnicalOnBoardingDaoImpl : updateSiteListStatus()");
       SiteList resultObject = null;
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           Criteria criteria =  session.createCriteria(SiteList.class);
           criteria.add(Restrictions.eq("id", siteList.getId()));
           resultObject = (SiteList)  criteria.uniqueResult();
           criteria =  session.createCriteria(Status.class);
           criteria.add(Restrictions.eq("statusId", status.getStatusId()));
           Status s = (Status)  criteria.uniqueResult();
           logger.debug("StepBStatus before update:" + s.getStatusDescription());
           resultObject.setStepBStatus(s);
           if(siteList.getStepBSRRequest() != null) {
           		resultObject.setStepBSRRequest(siteList.getStepBSRRequest());
	           	logger.debug("siteList.getArtSrNo():" + siteList.getArtSrNo());
	           	resultObject.setArtSrNo(siteList.getArtSrNo());
	           	if(StringUtils.isNotEmpty(siteList.getDeviceLastAlarmReceivedDate())) {
	           		resultObject.setDeviceLastAlarmReceivedDate(siteList.getDeviceLastAlarmReceivedDate());
	           	}
           	
	           	if(StringUtils.isNotEmpty(siteList.getDeviceStatus())) {
	           		resultObject.setDeviceStatus(siteList.getDeviceStatus());
	           	}
           	}
           
           if(GRTConstants.TRUE.equals(siteList.getCancelled())){
           		if(resultObject.getStepBSRRequest() != null){
           			session.delete(resultObject.getStepBSRRequest());
           		}
           		resultObject.setStepBSRRequest(null);
           }
           
           resultObject.setSelectForRemoteAccess(siteList.isSelectForRemoteAccess());
           resultObject.setSelectForAlarming(siteList.isSelectForAlarming());
           java.util.Set<ExpandedSolutionElement> expSolutionElements = getExpSolutionElementsSetForSalMigration(resultObject, siteList.getExpSolutionElements());
           
           if(!expSolutionElements.isEmpty()){
           		resultObject.setExplodedSolutionElements(expSolutionElements);
           }
           
           if(siteList.getStepBSubmittedDate() != null){
           	resultObject.setStepBSubmittedDate(siteList.getStepBSubmittedDate());
           }

           if(siteList.getStepBCompletedDate() != null){
           	resultObject.setStepBCompletedDate(siteList.getStepBCompletedDate());
           }
           
           //Defect Fix
           if(siteList.getNumberOfSubmit()!=null){
        	   resultObject.setNumberOfSubmit(siteList.getNumberOfSubmit());
           }
           
           session.saveOrUpdate(resultObject);
           Status stepBstatus = resultObject.getStepBStatus();
           logger.debug("After update StepBStatus:" + stepBstatus.getStatusId()+" Description is ::"+stepBstatus.getStatusDescription());
           Status st = resultObject.getStatus();
           session.getTransaction().commit();
           logger.debug("The Site List satus=====================>"+st.getStatusId()+" Description is ::"+st.getStatusDescription());
           if (st != null && st.getStatusId()!= null) {
           	resultObject.setStatus(st);
           }
       } catch (HibernateException hibEx) {
       		logger.error("", hibEx);
           throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
       }
       logger.debug("Exiting TechnicalOnBoardingDaoImpl : updateSiteListStatus()");

       return resultObject;
   }

   public TechnicalRegistration updateStepAResubmittedDate(TechnicalRegistration technicalRegistration) throws DataAccessException {
       logger.debug("Entering RegistrationDao : updateTechnicalRegistrationStatus()");
       TechnicalRegistration resultObject = null;
       try {
           Session session = getSessionForGRT();
           session.beginTransaction();
           Criteria criteria =  session.createCriteria(TechnicalRegistration.class);
           criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistration.getTechnicalRegistrationId()));
           resultObject = (TechnicalRegistration)  criteria.uniqueResult();

           resultObject.setStepAReSubmittedDate(technicalRegistration.getStepAReSubmittedDate());
           session.saveOrUpdate(resultObject);
           session.getTransaction().commit();

       } catch (HibernateException hibEx) {
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       }
       logger.debug("Exiting RegistrationDao : updateTechnicalRegistrationStatus()");

       return resultObject;
   }
   /**
    * Method to return the ART Error for the given error code key.
    *
    * @param errorCode String
    * @return artErrorCode ArtErrorCode
    * @throws DataAccessException custom exception
    */
   public ArtErrorCode getArtErrorCode(String errorCode) throws DataAccessException {
   	ArtErrorCode artErrorCode = null;
   	logger.debug("Entering TechnicalOnBoardingDao : getArtErrorCode");
   	try {
   		artErrorCode = (ArtErrorCode) getSessionForGRT().get(ArtErrorCode.class, errorCode);
   	} catch (HibernateException hibEx) {
   		logger.debug("No error code Found !");
           throw new DataAccessException(TechnicalOnBoardingDao.class, hibEx
                   .getMessage(), hibEx);
       } catch (Exception ex) {
       	logger.debug("No error code Found !");
           throw new DataAccessException(TechnicalOnBoardingDao.class, ex
                   .getMessage(), ex);
       }
       logger.debug("Exiting TechnicalOnBoardingDao : getArtErrorCode");

       return artErrorCode;
   }
   
   @Override
   public TechnicalRegistration getConnectivityDetailsBySeid(String seid)
   		throws Exception {
	   logger.debug("Entering TechnicalOnBoardingDao : getConnectivityDetailsBySeid");
	   TechnicalRegistration tr = new TechnicalRegistration();
	   	Query query = getSessionForGRT().getNamedQuery("getConnectivityInfo");
		query.setParameter("seId", seid);
		try{
			query.setFetchSize(500);
			List<Object[]> queryResult = query.list();
			
			if( queryResult!=null && queryResult.size() > 0 ){
				Object[] object = queryResult.get(0);
				if( object[0]!=null ){
					tr.setAccessType(object[0].toString());
				}
				if( object[1]!=null ){
					tr.setConnectivity(object[1].toString());
				}
				if( object[2]!=null ){
					tr.setTechnicalRegistrationId(object[2].toString());
				}
				
			}
		}catch(Exception ex){
			logger.debug("Error getting connectivity details");
	           throw new DataAccessException(TechnicalOnBoardingDao.class, ex
	                   .getMessage(), ex);
		}
	   logger.debug("Exiting TechnicalOnBoardingDao : getConnectivityDetailsBySeid");
	   return tr;
   }
   
   @Override
   public int deleteTechOrderByRegId(String regId) {
	   int flag = 1;
	   Query sqlQuery = null;
	   try {
		   sqlQuery = getSessionForGRT().getNamedQuery("deleteTechOrderByRegId");
		   sqlQuery.setString("registrationId", regId);
		   deleteSQLQuery(sqlQuery);
	   } catch (DataAccessException e) {
		   flag = 0;
		   // TODO Auto-generated catch block
		   logger.debug("deleteTechOrderByRegId : Error in deleting technical order - "+e.getMessage());
	   }
	    
   	   return flag;
   }
   
   public int deleteTechRegByOrderIds(List<String> orderIds) {
	   int flag = 1;
	   Query sqlQuery = null;
	   try {
		   sqlQuery = getSessionForGRT().getNamedQuery("deleteTechRegByOrderIds");
		   sqlQuery.setParameterList("orderIds", orderIds);
		   deleteSQLQuery(sqlQuery);
	   } catch (DataAccessException e) {
		   flag = 0;
		   // TODO Auto-generated catch block
		   logger.debug("deleteTechRegByOrderIds : Error in deleting technical order - "+e.getMessage());
	   }
	   return flag;
	}
   
   @Override
   public CMMain validateCMMain(String cmMainSeid, List<String> piePollableSeCodes) throws DataAccessException {
	   CMMain cmMain = new CMMain();
	   try {
		   if(StringUtils.isNotEmpty(cmMainSeid)) {
			   Query query = getSessionForSiebel().getNamedQuery("validateCMMain");
			   query.setParameter("CM_SEID", cmMainSeid);
			   query.setParameterList("seCodeInClause", piePollableSeCodes);
			   List<Object[]> resultSet = query.list();
			   if (resultSet != null && resultSet.size()>0) {
				   logger.debug("Match found in Siebel for CM Main SEID:" + cmMainSeid);
				   Object[] object = resultSet.get(0);
				   if (object != null) {
					   if(object[0] != null) {
						   cmMain.setMaterialCode(object[0].toString());
					   }
					   if(object[1] != null) {

						   cmMain.setMaterialDescription(object[1].toString());
					   }
					   if(object[2] != null) {
						   cmMain.setSoldToId(object[2].toString());
					   }
					   if(object[3] != null) {
						   cmMain.setSeCode(object[3].toString());
					   }
					   if (object[4] != null) {
						   cmMain.setSeid(object[4].toString());
					   }
					   if (object[5] != null) {
						   cmMain.setSid(object[5].toString());
					   }
				   }
			   }
		   }
	   }catch(Throwable throwable) {
		   logger.error("", throwable);
	   } finally {
		   logger.debug("Exiting validateCMMainSEID for cmMainSeid:" + cmMainSeid);
	   }
	   return cmMain;
   }
   
   @Override
   public AUXMCMain validateAUXMCMainSEID(String auxMCMainSeid) throws DataAccessException {
	   AUXMCMain auxMCMain = new AUXMCMain();
	   try {
		   if(StringUtils.isNotEmpty(auxMCMainSeid)) {
			   logger.debug("Entering validateAUXMCMainSEID for auxMCMainSeid:" + auxMCMainSeid);
			   Query query = getSessionForSiebel().getNamedQuery("validateAUXMCMainSEID");
			   query.setParameter("auxMCMainSeid", auxMCMainSeid);
			   List<Object[]> resultSet = query.list();
			   if (resultSet != null && resultSet.size()>0) {
				   logger.debug("Match found in Siebel for AUXMCMain SEID:" + auxMCMainSeid);
				   Object[] object = resultSet.get(0);
				   if (object != null) {
					   if(object[0] != null) {
						   auxMCMain.setSeid(object[0].toString());
					   }
					   if(object[1] != null) {
						   auxMCMain.setSid(object[1].toString());
					   }
					   if(object[2] != null) {
						   auxMCMain.setMid(object[2].toString());
					   }
					   if(object[3] != null) {
						   auxMCMain.setStatus(object[3].toString());
					   }
				   }
			   }
		   }
	   }catch(Throwable throwable) {
		   logger.error("", throwable);
	   } finally {
		   logger.debug("Exiting validateAUXMCMainSEID for auxMCMainSeid:" + auxMCMainSeid);
	   }
	   return auxMCMain;
   }

 
}
