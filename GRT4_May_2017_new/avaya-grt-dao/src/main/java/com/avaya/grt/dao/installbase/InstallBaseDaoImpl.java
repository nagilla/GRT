package com.avaya.grt.dao.installbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.MaterialCode;
import com.avaya.grt.mappers.Province;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.mappers.SalesOut;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.SoldToSAPMapping;
import com.avaya.grt.mappers.State;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.SuperUser;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.dto.Product;
import com.grt.dto.TRConfig;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public class InstallBaseDaoImpl extends TechnicalRegistrationDao implements InstallBaseDao {
	private final Logger logger = Logger.getLogger(InstallBaseDaoImpl.class);
	static List<State> stateList ;
    static List<Country> countryList;
    static List<Province> provinceList;
    static List<Region> regionList;
    public static Map<String, Set<String>> mc2GroupIdMappings = new HashMap<String, Set<String>>();
    public static Map<String, List<TRConfig>> trConfigs = new HashMap<String, List<TRConfig>>();
    public static Long lastFetch = System.currentTimeMillis();
   

	/**
     * Get the siebel assets for Material Code Validation.
     *
     * @param soldToNumber String
     * @return productList contains Product
     */
    public Map<String, MaterialCode> getMaterialCodeForValidation(List<String> materialCodes) {
        logger.debug("Entering InstallBaseDao.getMaterialCodeForValidation");
        Map<String, MaterialCode> retVal = new HashMap<String, MaterialCode>();
        if(materialCodes == null) {
        	return retVal;
        }
        
        Query query = getSessionForSiebel().getNamedQuery("getMaterialCodes");
       
        query.setParameterList("materialCodes", materialCodes);
        List queryResult = query.list();
        for (int i = 0; i < queryResult.size(); i++) {
            Object[] listItem = (Object[]) queryResult.get(i);
            MaterialCode materialCode = copyMaterialCode(listItem);
            retVal.put(materialCode.getMaterialCode(), materialCode);
        }
        logger.debug("Existing InstallBaseDao.getMaterialCodeForValidation");
        return retVal;
    }

    /**
     * Method to copy material code
     * @param listItem
     * @return materialCode
     */
    private MaterialCode copyMaterialCode(Object[] listItem) {
    	MaterialCode materialCode = new MaterialCode();
    	materialCode.setMaterialCode((String) listItem[0]);
    	materialCode.setDescription((String) listItem[1]);
        return materialCode;
    }
    
    /**
     * Method to get the contract code. ContractCode/Material Code both are same.
     *
     * @return contractCodeList ArrayList
     */
    public List<State> getStateList() throws DataAccessException {
    	List<State> stateList = new ArrayList();
        
    	logger.debug("Entering InstallBaseDao : getStateList");
        try {
            Criteria criteria = getSessionForGRT().createCriteria(State.class);
            criteria.setCacheable(true);
            criteria.addOrder(Order.asc("state"));
            stateList = criteria.list();
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(InstallBaseDao.class, ex
                    .getMessage(), ex);
        }
        logger.debug("Exiting InstallBaseDao : getStateList");

        return stateList;
    }
    
    /**
     * Method to get the country code. 
     *
     * @return contractCodeList ArrayList
     */
   
	public List<Country> getCountryList() throws DataAccessException {
      
        if( countryList == null) {
        	  logger.debug("Entering InstallBaseDao : getCountryList");
	        try {	           
	            countryList = getSessionForGRT().createQuery("from " + Country.class.getName()).setFetchSize(500).list();
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(InstallBaseDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	            throw new DataAccessException(InstallBaseDao.class, ex
	                    .getMessage(), ex);
	        }
	        logger.debug("Exiting InstallBaseDao : getCountryList");
        }
        

        return countryList;
    }
	
	/**
     * Method to get the province code. 
     *
     * @return province ArrayList
     */
    public List<Province> getProvinceList() throws DataAccessException {
        
       if(provinceList == null) {
    	   logger.debug("Entering InstallBaseDao : getProvinceList");
	        try {
	            Criteria criteria = getSessionForGRT().createCriteria(Province.class);
	            criteria.setCacheable(true);
	            criteria.addOrder(Order.asc("name"));
	            provinceList = criteria.list();
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(InstallBaseDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	            throw new DataAccessException(InstallBaseDao.class, ex
	                    .getMessage(), ex);
	        }
            logger.debug("Exiting InstallBaseDao : getProvinceList");
       }
        return provinceList;
    }
    /**
     * Method to get the region List. 
     *
     * @return regionList ArrayList
     */
 public List<Region> getRegionList() throws DataAccessException {
        
        if(regionList == null) {
        	logger.debug("Entering InstallBaseDao : getRegionList");
	        try {
	        	//Dec release item - State Drop down should be in Alphabetical Order.
	        	regionList = getSessionForGRT().createQuery("from " + Region.class.getName() + " order by DESCRIPTION").setFetchSize(1000).list();
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(InstallBaseDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	            throw new DataAccessException(InstallBaseDao.class, ex
	                    .getMessage(), ex);
	        }
	        logger.debug("Exiting InstallBaseDao : getRegionList");
        }
        return regionList;
    }
 


	
    
    /**
     * Method to get the SuperUser for the given user id.
     *
     * @param userId String
     * @return superUser SuperUser
     * @throws DataAccessException custom exception
     */
    public SuperUser getSuperUser(String userId) throws DataAccessException {
        SuperUser superUser = null;
        logger.debug("Entering InstallBaseDao : getSuperUser");
        try {
        	if (userId != null) {
				superUser = (SuperUser) getSessionForGRT().get(SuperUser.class,
						userId);
			}
        	
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(InstallBaseDao.class, ex
                    .getMessage(), ex);
        }
        logger.debug("Entering InstallBaseDao : getSuperUser");

        return superUser;
    }
    
    /**
     * Get the siebel assets count for paging stuff of Install Base.
     *
     * @param soldToNumber String
     * @return productList contains Product
     */
    public int getAllSolutionElementsForAccountCountNew(String soldToNumber, String vcodes) {
       
    	logger.debug("Entering getAllSolutionElementsForAccountCountNew");
        logger.debug("soldToNumber = " + soldToNumber);
       
       Query query=getSessionForSiebel().getNamedQuery("getSolutionElementsForAccountCount");
        query.setParameter("soldToNumberParam", soldToNumber);
        query.setParameter("vcodes", vcodes);
        int retVal = Integer.parseInt(query.uniqueResult().toString());

        logger.debug("Exiting getSolutionElementsForAccountCount with size = " + retVal);
        return retVal;
    	}
    
    /**
     * Get the siebel assets subset for paging stuff of Install Base.
     *
     * @param soldToNumber String
     * @return productList contains Product
     */
    public List<Product> getAllSolutionElementsForAccountNew(String soldToNumber, int offSet, int fetchSize, String vcodes) {
    	
    	        logger.debug("Entering getAllSolutionElementsForAccountNew with offSet : " + offSet + " and fetchSize : " + fetchSize);
    	        logger.debug("soldToNumber = " + soldToNumber);
    	        List<Product> retVal = new ArrayList<Product>();
    	        Query query=getSessionForSiebel().getNamedQuery("getSolutionElementsForAccountSubset");
    	        query.setParameter("soldToNumberParam", soldToNumber);
    	        query.setParameter("vcodes", vcodes);
    	        List queryResult = query.list();
    	        for (int i = 0; i < queryResult.size(); i++) {
    	            Object[] listItem = (Object[]) queryResult.get(i);
    	            Product product = copyProduct(listItem);
    	            retVal.add(product);
    	        }

    	        logger.debug("Exiting getAllSolutionElementsForAccountNew with size = " + retVal.size());
    	        return retVal;
    	
    }   
    /**
     * Method to copy product
     * @param listItem
     * @return product
     */
    private Product copyProduct(Object[] listItem) {
        Product product = new Product();
        product.setMaterialCode((String) listItem[0]);
        product.setShortDescription((String) listItem[1]);
        product.setProductLine((String) listItem[2]);
        product.setQuantity(Integer.parseInt(listItem[3].toString()));
        return product;
    }
    
    /**
     * Get the siebel assets for Install Base.
     *
     * @param soldToNumber String
     * @return productList contains Product
     */
    public List<Product> getAllSolutionElementsForAccount(String soldToNumber) {
        
    	logger.debug("Entering getSolutionElementsForAccount");
        logger.debug("soldToNumber = " + soldToNumber);
        List<Product> retVal = new ArrayList<Product>();
        Query query=getSessionForSiebel().getNamedQuery("getSolutionElementsForAccount");
        query.setParameter("soldToNumberParam", soldToNumber);
        List queryResult = query.list();
        for (int i = 0; i < queryResult.size(); i++) {
            Object[] listItem = (Object[]) queryResult.get(i);
            Product product = copyProduct(listItem);
            retVal.add(product);
        }

        logger.debug("Exiting getSolutionElementsForAccount");
        return retVal;	
    }
    
    /**
     * Update Sales_Out deleted column to yes
     * @throws DataAccessException
     *
     */
    public void updateDeletedSalesOut(List<String> materialCodes,String soldToId) throws DataAccessException{
    	logger.debug("Entering InstallBaseDao : updateDeletedSalesOut()");
    	List<SalesOut> salesOutList = null;
    	try{
    		Session session = getSessionForGRT();
    		
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(SalesOut.class);
            criteria.add(Restrictions.eq("soldToId", soldToId));
            criteria.add(Restrictions.in("materialCode", materialCodes.toArray()));
            salesOutList = criteria.list();
            for(SalesOut salesOut:salesOutList){
            	salesOut.setDeleted(GRTConstants.YES);
            	session.saveOrUpdate(salesOut);
            }
            session.getTransaction().commit();
    	}catch (HibernateException hibEx) {
    		throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
		}
    	logger.debug("Exiting InstallBaseDao : updateDeletedSalesOut()");
    }
    
    
    
    /**
     * Set siteRegistration skipInstallBaseCreation field
     * @param registrationId
     * @param skipInstallBaseCreation
     * @return SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateSiteRegistrationSkipInstallBaseCreation(String registrationId, Boolean skipInstallBaseCreation) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : updateSiteRegistrationSkipInstallBaseCreation()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);

			if(skipInstallBaseCreation) {
				registration.setSkipInstallBaseCreation(GRTConstants.YES);
			}
			else {
				registration.setSkipInstallBaseCreation(GRTConstants.NO);
			}

            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : updateSiteRegistrationSkipInstallBaseCreation()");

        return registration;

    }
    /**
     * Set siteRegistration salMigrationOnlyFlag field
     * @param registrationId
     * @param salMigrationOnlyFlag
     * @return SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateSiteRegistrationSalMigrationOnlyFlag(String registrationId, Boolean salMigrationOnlyFlag) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : updateSiteRegistrationSalMigrationOnlyFlag()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);

			if(salMigrationOnlyFlag) {
				registration.setSalMigrationOnly(GRTConstants.YES);
			}
			else {
				registration.setSalMigrationOnly(GRTConstants.NO);
			}

            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : updateSiteRegistrationSalMigrationOnlyFlag()");

        return registration;
    }
    /**
     * method to update SiteRegistration status for direct user
     * @param regId
     * @param status
     * @throws DataAccessException
     */
    public void updateSiteRegistrationStatusForDIrectUser(String regId, Status status) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : updateSiteRegistrationStatusForDIrectUser regId"+regId+"status"+status);
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(SiteRegistration.class);
            criteria.add(Restrictions.eq("registrationId", regId));
            registration = (SiteRegistration)  criteria.uniqueResult();


            if (status != null) {
            	registration.setInstallBaseStatus(status);
            }

            logger.debug("updateSiteRegistrationStatusForDIrectUser Is status changed to completed ? --> " + (status != null));

            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : updateSiteRegistrationStatusForDIrectUser");
    }
    
    /**
     * Get SAP Box based on Sold To
     *
     * @param materialCode string
     * @return materialCodeDesc string
     */
    public String getSapBox(String soldTo) throws DataAccessException {
    	logger.debug("Entering InstallBaseDao : getSapBox() ");
    	String sapBox = null;
    	SoldToSAPMapping soldToSapMapping = null;
         try {
        	 Criteria criteria =  getSessionForGRT().createCriteria(SoldToSAPMapping.class);
        	 criteria.add(Restrictions.eq("soldTo", soldTo));
             soldToSapMapping =(SoldToSAPMapping) criteria.uniqueResult();
             if(soldToSapMapping != null){
             sapBox = soldToSapMapping.getSapBox();
             }
         }catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
		if(sapBox == null){
             if(soldTo.startsWith(GRTConstants.BBoxSoldPrefix)){
                   return GRTConstants.BBoxDestination;
             }else{
                   return GRTConstants.IBoxDestination;
             }
         }

        logger.debug("Exiting InstallBaseDao : getSapBox() ");
        return sapBox;
    }
    /**
     * method to update SiteRegistration Status on RegId
     * @param registrationId
     * @param status
     * @param processStep
     * @return flag successo or failure
     */
    public int updateSiteRegistrationStatusOnRegId(String registrationId, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException {
		logger.debug("Entering InstallBaseDao : updateSiteRegistrationStatusOnRegId");
		int flag = 0;
		SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
			Criteria criteria =  session.createCriteria(Status.class);
            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
            Status s = (Status)  criteria.uniqueResult();
            if (processStep !=null) {
        		if (processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
        			registration.setInstallBaseStatus(s);
        			if(StatusEnum.INPROCESS.getStatusId().equals(s.getStatusId())){
        				registration.setIbSubmittedDate(new Date());
        			} else if(StatusEnum.COMPLETED.getStatusId().equals(s.getStatusId())){
        				registration.setIbCompletedDate(new Date());
        			}
        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId())){
        			registration.setTechRegStatus(s);
        			if(StatusEnum.COMPLETED.getStatusId().equals(s.getStatusId())){
        				registration.setTobCompletedDate(new Date());
        			}
        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
        			registration.setFinalValidationStatus(s);
        			if(StatusEnum.INPROCESS.getStatusId().equals(s.getStatusId())){
        				registration.setEqrSubmittedDate(new Date());
        			} else if(StatusEnum.COMPLETED.getStatusId().equals(s.getStatusId())){
        				registration.setEqrCompletedDate(new Date());
        			}
        		}
            }
            session.saveOrUpdate(registration);
            session.getTransaction().commit();
            flag = 1;
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : updateSiteRegistrationStatusOnRegId");
		return flag;
	}
    
    /**
     * Set siteRegistration salMigrationOnlyFlag field
     * @param registrationId
     * @param salMigrationOnlyFlag
     * @return SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateSiteRegistrationSubmittedFlag(String registrationId, String submitted) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : updateSiteRegistrationSalMigrationOnlyFlag()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
			registration.setSubmitted(submitted);
            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : updateSiteRegistrationSalMigrationOnlyFlag()");

        return registration;
    }
    
    /**
     * This API find matching groups which contains all passed seCodes in it.
     * 
     * @param seCodes uppercase SeCodes
     * @return List of TRConfig objects.
     * @throws DataAccessException
     */
    public List<TRConfig> getEligibleGroups(List<String> seCodes) throws DataAccessException {
    	logger.debug("Entering getEligibleGroups");
    	Query query = null;
    	List<TRConfig> trConfigList = new ArrayList<TRConfig>();
    	if(seCodes != null && seCodes.size() > 0) {
    		StringBuilder inClause = new StringBuilder("(");
    		for (String string : seCodes) {
    			inClause.append("'" + string + "', ");
    		}
    		String seCodesInClause = inClause.substring(0, inClause.length()-2);
    		seCodesInClause += ")";
    		
    		StringBuilder queryStr = new StringBuilder();
    		
    		query=getSessionForSiebel().getNamedQuery("getEligibleGroups");
    		query.setParameterList("seCodesInClause", seCodes);
    		query.setInteger("seCodes", seCodes.size());
    		List<Object[]> resultSet = query.list(); 
    		try{
    			if(resultSet != null && resultSet.size() > 0){
		    		for (Object[] object : resultSet) {
			    		if (object != null) {
			    			TRConfig trConfig = new TRConfig();
			    			trConfig.setGroupId(object[0] != null ? object[0].toString():"");
			    			trConfig.setGroupDescription(object[1] != null ? object[1].toString():"");
			    			trConfig.setParentMaterialCode(object[2] != null? object[2].toString():"");
			    			trConfig.setParentMaterialCodeDescription(object[3] != null? object[3].toString():"");
			    			trConfig.setSeCode(object[4] != null? object[4].toString():"");
			    			if(object[5] != null) {
			    				String childMC = object[5].toString();
			    				if(childMC.indexOf("<") < 0 && !childMC.equalsIgnoreCase(trConfig.getParentMaterialCode())) { 
			    					trConfig.setParentMaterialCode(object[5] != null? object[5].toString():"");
			    				}
			    			}
			    			if(object[6] != null) {
			    				if(GRTConstants.Y.equalsIgnoreCase(object[6].toString())) {
			    					trConfig.setMainMaterialCode(true);
			    				} else {
			    					trConfig.setMainMaterialCode(false);;
			    				}
			    			}
			    			trConfigList.add(trConfig);
			    		}
		    		}
    			}
    		} catch(Exception e){
    			logger.error("Error while getEligibleGroups", e);
    		}
    	}
    	logger.debug("Exiting fetchTRConfigs");
    	return trConfigList;
    }
   
    
    /**
     * Method to push the data from site contact validation into the
     * site_registration . save site contact validation.
     *
     * @param siteRegistration SiteRegistration
     * @return siteRegistrationId Long
     * @throws DataAccessException custom exception
     */
    public Long saveSiteContactValidation(SiteRegistration siteRegistration)
        throws DataAccessException {
    Long siteRegistrationId = null;
    try {
    	 Session session = getSessionForGRT();
         session.beginTransaction();
        
    	handleStatuses(siteRegistration);
        session.saveOrUpdate(siteRegistration);
        session.getTransaction().commit();
        siteRegistrationId = new Long(siteRegistration.getRegistrationId());
        logger.debug("**************************saveSiteContactValidation is SUCCESS ******************************************");
    } catch (HibernateException hibEx) {
        logger.error("Error WHILE SAVING SiteContactValidation:"+ hibEx.getMessage());
        throw new DataAccessException(InstallBaseDao.class, hibEx
                .getMessage(), hibEx);
    } catch (Exception ex) {
        getSessionForGRT().getTransaction().rollback();
        throw new DataAccessException(InstallBaseDao.class, ex
                .getMessage(), ex);
    }

    return siteRegistrationId;
}
    
   
    /**
     * Delete Tecchnical Order record
     * @throws DataAccessException
     *
     */
    public void deleteTechnicalOrders(List<String> orderIds) throws DataAccessException{
    	logger.debug("Entering InstallBaseDao : deleteTechnicalOrders()");
    	TechnicalOrder techOrder = null;
    	try{

    		Session session = getSessionForGRT();
            session.beginTransaction();
            for(String orderId : orderIds){
            	techOrder = (TechnicalOrder) getEntity(session, TechnicalOrder.class, "orderId",orderId);
            	if(techOrder != null){
            		logger.debug("Deleting Technical Order Id"+orderId);
            		session.delete(techOrder);
            	}
            }
            session.getTransaction().commit();
    	}catch (HibernateException hibEx) {
    		throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
		}
    	logger.debug("Exiting InstallBaseDao : deleteTechnicalOrders()");
    }
    
    /**
     * Method to get the SALES_OUT for the given soldToNumber, and then sum QUANTITY_SOLD based on MATERIAL_CODE before left join Material_Code to get description.
     *
     * @param soldToNumber String
     * @return salesOutList List
     * @throws DataAccessException Exception
     */
    public List<Object[]> getPendingOrders(String soldToNumber, String bpLinkId)
            throws DataAccessException {
        logger.debug("Entering getPendingOrders for soldTo:" + soldToNumber + " BPLinkId:" + bpLinkId);
        List<Object[]> salesOutList = null;
        try {
        	if(StringUtils.isNotEmpty(bpLinkId)  && !bpLinkId.equalsIgnoreCase("na")) {
        		bpLinkId = getValidBpLinkId(bpLinkId);
        	}
           
            Query query = null;
            if (StringUtils.isNotEmpty(bpLinkId)  && !bpLinkId.equalsIgnoreCase("na")) {
           
           		query=getSessionForGRT().getNamedQuery("getPendingOrdersWithBPLinkId");
            query.setParameter("soldToId", soldToNumber);
            query.setParameter("bpLink", bpLinkId);
            } else {
            	query=getSessionForGRT().getNamedQuery("getPendingOrdersWithoutBPLinkId");
                query.setParameter("soldToId", soldToNumber);
            }
            salesOutList = query.list();
            if (salesOutList != null) {
                logger.debug("Pending Orders List size:"+ salesOutList.size());
            }
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        } finally {
        	logger.debug("Exiting getPendingOrders for soldTo:" + soldToNumber + " BPLinkId:" + bpLinkId);
        }
        return salesOutList;
    }
    
    
    /**
     * IB Status to Cancelled in Registration List UI.
     *
     * @param regId String
     * @param status
     * @throws DataAccessException Exception
     */
    public void cancelSiteRegistrationIBStatus(String regId, Status status) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : cancelSiteRegistrationStatus  regId"+regId+"status"+status);
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(SiteRegistration.class);
            criteria.add(Restrictions.eq("registrationId", regId));
            registration = (SiteRegistration)  criteria.uniqueResult();


            if (status != null) {
            	registration.setInstallBaseStatus(status);
            }
            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting InstallBaseDao : cancelSiteRegistrationStatus");
    }
    /**
     * Method to check whether BP soldTo exists.
     *
     * @param soldTo String
     * @return isExist true or false
     * @throws DataAccessException Exception
     */
    public boolean isBpSoldToExist(String soldTo) throws DataAccessException {
    	logger.debug("Entering InstallBaseDao : isBpSoldToExist");
		boolean isExist = false;
		try {
        	
        	Query query =  getSessionForGRT().getNamedQuery("getBpSoldToExist");
            query.setString("soldTo", soldTo);
            isExist = !"0".equals(query.uniqueResult().toString());
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(InstallBaseDao.class, ex
                    .getMessage(), ex);
        }
		logger.debug("Exiting InstallBaseDao : isBpSoldToExist");

		return isExist;
    }
    
    /**
     * Method to check whether soldTo exists.
     *
     * @param soldTo String
     * @return isExist true or false
     * @throws DataAccessException Exception
     */
    public boolean isSoldToExist(String soldTo) throws DataAccessException {
        logger.debug("Entering InstallBaseDao : isSoldToExist");
        boolean isExist = false;
        try {
        	Query query=getSessionForCXP().getNamedQuery("isSoldToExist");
            query.setString("soldTo", soldTo);            
            isExist = !"0".equals(query.uniqueResult().toString());
            logger.debug(" isExist PRINT : "+query.uniqueResult().toString());
        } catch (HibernateException hibEx) {
            throw new DataAccessException(InstallBaseDao.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(InstallBaseDao.class, ex
                    .getMessage(), ex);
        } 
        logger.debug("Exiting InstallBaseDao : isSoldToExist");
        
        return isExist;

	}
    
    /**
     * Method to check whether the soldTo is valid.
     *
     * @param soldTo String
     * @return isExist true or false
     * @throws DataAccessException Exception
     */
    public boolean isSoldToValid(String soldTo) throws DataAccessException {
		boolean isExist = false;
		try {
			Query query=getSessionForCXP().getNamedQuery("isSoldToValid");
			query.setString("soldTo", soldTo);
			isExist = !"0".equals(query.uniqueResult().toString());
		} catch (HibernateException hibEx) {
			throw new DataAccessException(InstallBaseDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(InstallBaseDao.class, ex
					.getMessage(), ex);
		} 
		return isExist;
	}
    
    /**
     * Method to Fetch Tr configs by product identifier.
     *
     * @param productIdentifier String
     * @return trConfigList list of TRConfig
     */
    @Override
	public List<TRConfig> fetchTRConfigsForSalGWInstaller(
			String productIdentifier) {
    	logger.debug("Entering InstallBaseDao.fetchTRConfigs");
    	String groupId = "";
    	TRConfig trConfig = null;
    	Set<String> groupIds = null;
    	List<TRConfig> trConfigList = new ArrayList<TRConfig>();;
    	
    	Query query=getSessionForSiebel().getNamedQuery("fetchTRConfigsForSalGWInstaller");
    	query.setString("productIdentifier", productIdentifier);
    	List<Object[]> resultSet = query.list();
    	try{
    		
	    	if(resultSet != null && resultSet.size() > 0){
	    		for (Object[] object : resultSet) {
		    		if (object != null) {
		    			trConfig = new TRConfig();
		    			groupId = object[0] != null ? object[0].toString():null;
		    			trConfig.setGroupId(groupId);
		    			trConfig.setGroupDescription(object[1] != null ? object[1].toString():"");
		    			trConfig.setParentMaterialCode(object[2] != null? object[2].toString():null);
		    			trConfig.setParentMaterialCodeDescription(object[3] != null? object[3].toString():"");
		    			trConfig.setSeCode(object[4] != null? object[4].toString():null);
		    			trConfig.setSeCodeDescription(object[5] != null? object[5].toString():"");
		    			trConfig.setProductType(object[6] != null? object[6].toString():null);
		    			trConfig.setProductTypeDescription(object[7] != null? object[7].toString():"");
		    			trConfig.setTemplate(object[8] != null? object[8].toString():null);
		    			trConfig.setTemplateDescription(object[9] != null? object[9].toString():"");
		    			trConfig.setChildMaterialCode(object[10] != null? object[10].toString():null);
		    			trConfig.setMainMaterialCode(true);
		    			trConfig.setSpecial_note(object[11] != null? object[11].toString():null);
		    			trConfigList.add(trConfig);
		    		}
	    		}
	    	}
	    	logger.debug("trConfigList-outside"+trConfigList.size());
    	} catch(Exception e){
    		logger.error("Error while populating TR config: "+e);
    	}
    	logger.debug("trConfigs"+trConfigs.size());
    	return trConfigList; 
	}
    
    
    /**
     * Check for duplicate request for system to system salgw installer.
     *
     * @param materialCode String
     * @param serialNumber String
     * @return technicalOrder
     */
    @Override
	public TechnicalOrder getTechnicalOrderByFilter(String materialCode, String serialNumber) throws DataAccessException{
		logger.debug("Entering InstallBaseDao : getTechnicalOrderByFilter()");
    	TechnicalOrder technicalOrder = null;
    	try{
    		  Criteria criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class);
              criteria.add(Restrictions.eq("materialCode", materialCode));
              criteria.add(Restrictions.eq("serialNumber", serialNumber));
              
              technicalOrder = (TechnicalOrder)  criteria.uniqueResult();
              if( technicalOrder!=null ){
            	  technicalOrder.getSiteRegistration();
              }
    	}catch (HibernateException hibEx) {
    		throw new DataAccessException(InstallBaseDaoImpl.class, hibEx
                    .getMessage(), hibEx);
    	}
		logger.debug("Exiting InstallBaseDao : getTechnicalOrderByFilter()");
		return technicalOrder;
	}
    
    /*public static void main(String[] args) throws DataAccessException {
    	ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
    	InstallBaseDao installBaseDao = (InstallBaseDao) context.getBean("installBaseDao");
    	
    	 List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();
         TechnicalOrder o = new TechnicalOrder();
         o.setOrderId("390813");
         o.setMaterialCode("207944");
         o.setInitialQuantity(1l);
         o.setDescription("AVAYA AURATM R5 S87XX UPG SW LIC");
         SiteRegistration s = new SiteRegistration();
         s.setRegistrationId("390812");
         o.setSiteRegistration(s);
         o.setOrderType("IB");
         technicalOrderList.add(o);
        
               
	    technicalOrderList =  installBaseDao.saveTechnicalOrderList(technicalOrderList);
	    for(TechnicalOrder items:technicalOrderList)
	    {
	    	 System.out.println("MaterialCode"+items.getMaterialCode());
	    }

	    
	    String registrationId = "284877";
    	String orderType = "FV";
    	
	    String registrationId = "401678";
    	String orderType = "IB";

    	List<TechnicalOrder> technicalOrders = installBaseDao.getTechnicalOrderByType(registrationId, orderType);
	    		  
    	 for (TechnicalOrder to : technicalOrders) {
    		 System.out.println("Technical order group id : " + to.getDescription());
    	 }
    	 
    	 System.out.println("Ended");
	    		  
    }*/

}
