package com.avaya.grt.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.installbase.InstallBaseDao;
import com.avaya.grt.dao.technicalonboarding.TechnicalOnBoardingDaoImpl;
import com.avaya.grt.mappers.Alert;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.CountryAlarmReceiver;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.LocalTRConfig;
import com.avaya.grt.mappers.MasterMaterial;
import com.avaya.grt.mappers.MaterialExclusion;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProcessStep;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.SoldToSAPMapping;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.SuperUser;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.BusinessPartner;
import com.grt.dto.PaginationForSiteRegistration;
import com.grt.dto.SoldTo;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.dto.TechnicalRegistrationSummary;
import com.grt.util.DataAccessException;
import com.grt.util.DateLikeExpression;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.SRRequestStatusEnum;
import com.grt.util.StatusEnum;
import com.grt.util.StatusLikeExpression;
import com.grt.util.TechRecordEnum;


public class BaseHibernateDao {
	private final Logger logger = Logger.getLogger(BaseHibernateDao.class);
	
	public SessionFactory sqlSessionFactorySiebel;

	public SessionFactory sqlSessionFactoryGRT;
	
	public SessionFactory sqlSessionFactoryCXP;
	
	public SessionFactory sqlSessionFactoryCAT;
	
	public SessionFactory sqlSessionFactoryFMW;
	
	
	public Map<String, Set<String>> mc2GroupIdMappings = new HashMap<String, Set<String>>();
	public Map<String, List<TRConfig>> trConfigs = new HashMap<String, List<TRConfig>>();
	
	
	private String siebelUpdateQueue;
	
	 public String salGatewayMaterialCode;
	 
		
	    public String getSalGatewayMaterialCode() {
			return salGatewayMaterialCode;
		}

		public void setSalGatewayMaterialCode(String salGatewayMaterialCode) {
			this.salGatewayMaterialCode = salGatewayMaterialCode;
		}

	public SessionFactory getSqlSessionFactoryFMW() {
		return sqlSessionFactoryFMW;
	}

	public void setSqlSessionFactoryFMW(SessionFactory sqlSessionFactoryFMW) {
		this.sqlSessionFactoryFMW = sqlSessionFactoryFMW;
	}

   
	public String getSiebelUpdateQueue() {
		return siebelUpdateQueue;
	}

	public void setSiebelUpdateQueue(String siebelUpdateQueue) {
		this.siebelUpdateQueue = siebelUpdateQueue;
	}

	public static Long lastFetch = System.currentTimeMillis();
    
	public SessionFactory getSqlSessionFactoryCAT() {
		return sqlSessionFactoryCAT;
	}

	public void setSqlSessionFactoryCAT(SessionFactory sqlSessionFactoryCAT) {
		this.sqlSessionFactoryCAT = sqlSessionFactoryCAT;
	}

	
	public SessionFactory getSqlSessionFactoryCXP() {
		return sqlSessionFactoryCXP;
	}

	public void setSqlSessionFactoryCXP(SessionFactory sqlSessionFactoryCXP) {
		this.sqlSessionFactoryCXP = sqlSessionFactoryCXP;
	}
	
	protected final Session getSessionForFMW() {
		/*
		if (fmwSession == null) {
			fmwSession = getSqlSessionFactoryFMW().openSession();
		} 
		return fmwSession;
		*/
		
		return getSqlSessionFactoryFMW().openSession();
	}

	protected final Session getSessionForSiebel() {
		/*
		if (siebelSession == null) {
			siebelSession = getSqlSessionFactorySiebel().openSession();
		} 
		return siebelSession;
		*/
		
		return getSqlSessionFactorySiebel().openSession();
	}
	
	protected final Session getSessionForCXP() {
		/*
		if (cxpSession == null) {
			cxpSession = getSqlSessionFactoryCXP().openSession();
		} 
		return cxpSession;
		*/
		
		return getSqlSessionFactoryCXP().openSession();
	}
	protected final Session getSessionForGRT() {
		/*
		if (grtSession == null) {
			grtSession = getSqlSessionFactoryGRT().openSession();
		} 
		return grtSession;
		*/
		
		return getSqlSessionFactoryGRT().openSession();
	}
	
	protected final Session getSessionForCAT() {
		/*
		if (catSession == null) {
			catSession = getSqlSessionFactoryCAT().openSession();
		} 
		return catSession;
		*/
		
		return getSqlSessionFactoryCAT().openSession();
	}
	
	public SessionFactory getSqlSessionFactorySiebel() {
		return sqlSessionFactorySiebel;
	}

	public void setSqlSessionFactorySiebel(SessionFactory sqlSessionFactorySiebel) {
		this.sqlSessionFactorySiebel = sqlSessionFactorySiebel;
	}

	public SessionFactory getSqlSessionFactoryGRT() {
		return sqlSessionFactoryGRT;
	}

	public void setSqlSessionFactoryGRT(SessionFactory sqlSessionFactoryGRT) {
		this.sqlSessionFactoryGRT = sqlSessionFactoryGRT;
	}
	
	public Map<String, Set<String>> getMc2GroupIdMappings() {
		return mc2GroupIdMappings;
	}

	public void setMc2GroupIdMappings(
			Map<String, Set<String>> mc2GroupIdMappings) {
		this.mc2GroupIdMappings = mc2GroupIdMappings;
	}

	public Map<String, List<TRConfig>> getTrConfigs() {
		return trConfigs;
	}

	public void setTrConfigs(Map<String, List<TRConfig>> trConfigs) {
		this.trConfigs = trConfigs;
	}

	protected Object getEntity(Session session, Class clazz, String entityKeyName, String entityId) {
		Criteria criteria =  session.createCriteria(clazz);
		criteria.add(Restrictions.eq(entityKeyName, entityId));
		return criteria.uniqueResult();
	}
	
	/**
     * API to get the technical registration configuration data
     *
     * @param String materialCode
     * @return TRConfig DTO
     */
    @SuppressWarnings("unchecked")
      public List<TRConfig> getTRConfigData(String materialCode) {
      logger.debug("Entering BaseHibernateDao.getTRConfigData for materialCode:" + materialCode);
        List<TRConfig> configData = new ArrayList<TRConfig>();
        try {
              if(StringUtils.isNotEmpty(materialCode)) {
            	  mc2GroupIdMappings=this.initializeTRConfigData();
	                Set<String> mc2GroupId = mc2GroupIdMappings.get(materialCode);
	                if(mc2GroupId != null && mc2GroupId.size() > 0) {
	                      //logger.debug("mc2GroupId Size:"+mc2GroupId.size());
	                      for (String groupId : mc2GroupId) {
	                    	 
	                            List<TRConfig> trConfig = trConfigs.get(groupId);
	                            //logger.debug("trConfigs Size:"+trConfigs.size());
	                            if(trConfig!= null) {
	                                  for (TRConfig config : trConfig) {
                                            configData.add(config);
                                            break;
	                                  }
	                            }
	                      }
	                      //logger.debug("configData Size:"+configData.size());
	                }
                  }
              Collections.sort(configData);
        }catch (Throwable throwable) {
              logger.error("", throwable);
        } finally {
            logger.debug("Existing BaseHibernateDao.getTRConfigData");
        }
        return configData;
    }
    
    /**
     * API to get the technical registration configuration data
     *
     * @return Map<materialCode, TRConfig-DTO>
     */
    public Map<String, Set<String>> initializeTRConfigData() {
        logger.debug("Entering BaseHibernateDao.initializeTRConfigData");
        try {
        	if(mc2GroupIdMappings.size() <= 0) {
        		synchronized(mc2GroupIdMappings) {
        			logger.debug("Initializing data from Siebel view");
        			trConfigs=this.fetchTRConfigs();
        			lastFetch = System.currentTimeMillis();
        		}
        	}
        	synchronized(mc2GroupIdMappings) {
    			if (System.currentTimeMillis() - lastFetch > 30*60*1000) {
    				logger.debug("Re-initializing data from Siebel view, cached data is more than 30 minutes stale.");
    				 mc2GroupIdMappings.clear();
    				 trConfigs.clear();
    				 trConfigs=this.fetchTRConfigs();
    				lastFetch = System.currentTimeMillis();
    			}
    		}
        } catch (DataAccessException dataAccessException) {
        	logger.error("Error in fetching mc2GroupIdMappings and trConfigs", dataAccessException);
		} finally {
			logger.debug("mc2GroupIdMappings"+mc2GroupIdMappings.size());
			logger.debug("Existing BaseHibernateDao.initializeTRConfigData");
		}
        return mc2GroupIdMappings;
    }
    
    /**
     * Method to fetch TRConfigs
     * @throws DataAccessException
     */
    public Map<String, List<TRConfig>> fetchTRConfigs() throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.fetchTRConfigs");
    	
    	if (!trConfigs.isEmpty()) 
    		return trConfigs;
    	
    	String groupId = "";
    	TRConfig trConfig = null;
    	Set<String> groupIds = null;
    	List<TRConfig> trConfigList = null;
    
    	/*queryStr.append("select GROUP_ID, GROUP_ID_DESC, PARENT_MATERIAL_CODE, MATERIAL_CODE_DESC, SE_CODE, ")
    	        .append("SE_CODE_DESC, PRODUCT_TYPE, PRODUCT_TYPE_DESC, TEMPLATE, TEMPLATE_NAME, CHILD_MATERIAL_CODE, SPECIAL_NOTE ")
    	        .append("from SIEBEL.CX_VIEW_MC_TO_SE_MAPPING WHERE IS_MAIN_SE_CODE='Y' ");
    	
    	query = getSession().createSQLQuery(queryStr.toString()).setFetchSize(1000);*/
    	Query query=getSessionForSiebel().getNamedQuery("fetchTRConfigs");
    	query.setFetchSize(1000);
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
		    			// adding groupId to set
		    			
		    			if(mc2GroupIdMappings != null && mc2GroupIdMappings.containsKey(trConfig.getParentMaterialCode())){
		    				groupIds = mc2GroupIdMappings.get(trConfig.getParentMaterialCode());
		    			} else {
		    				groupIds = new HashSet<String>();
		    			}
		    			groupIds.add(groupId);
	    				mc2GroupIdMappings.put(trConfig.getParentMaterialCode(), groupIds);
	    				// adding TRConfig to list
		    			if(trConfigs.size() > 0 && trConfigs.containsKey(groupId)){
		    				trConfigList = trConfigs.get(groupId);
		    				logger.debug("trConfigList -inside"+trConfigList.size());
		    			} else {
		    				trConfigList = new ArrayList<TRConfig>();
		    				
		    			}
		    			trConfigList.add(trConfig);
		    			//logger.debug("trConfigList-outside"+trConfigList.size());
		    			trConfigs.put(groupId, trConfigList);
		    		}
	    		}
	    	}
    	} catch(Exception e){
    		logger.error("Error while populating TR config: "+e);
    	}
    	logger.debug("trConfigs"+trConfigs.size());
    	logger.debug("Exiting BaseHibernateDao.fetchTRConfigs");
    	return trConfigs; 
    	
    }
    
  
    
    public Set<String> getEligibleAccessTypes(List<TRConfig> trConfigs, Map<String, CompatibilityMatrix> salCompatibilityMatrix)  throws DataAccessException {
        Set<String> accessTypes = new HashSet<String>();
        accessTypes.add(GRTConstants.ACCESS_TYPE_IP);
        boolean eligible = false;
        boolean defaultSetting = true;
        if(trConfigs != null) {
              for (TRConfig config : trConfigs) {
                    List<LocalTRConfig> localTRConfig = this.getLocalTRConfig(config);
                    if(localTRConfig != null) {
                          for (LocalTRConfig localConfig : localTRConfig) {
                                defaultSetting = false;
                                if(localConfig.isInadsEligible()) {
                                      eligible = true;
                                      break;
                                }
                          }
                    }
                    if(StringUtils.isNotEmpty(config.getSeCode())) {
                          if(salCompatibilityMatrix.containsKey((config.getSeCode()))) {
                        	  accessTypes.add(GRTConstants.ACCESS_TYPE_SAL);
                        	  logger.debug("config.getSeCode()" + config.getSeCode());
                        	  CompatibilityMatrix matrix = salCompatibilityMatrix.get(config.getSeCode());
                        	  logger.debug("matrix record for " +config.getSeCode() + " " + matrix);
                        	  if(matrix != null && matrix.isSalOnly()) {
                        		  logger.debug("matrix.isSalOnly():" + matrix.isSalOnly());
                        		  accessTypes.clear();
                        		  accessTypes.add(GRTConstants.ACCESS_TYPE_SAL);
                        		  return accessTypes;
                        	  }

                          }
                    }
              }
        }
        if(eligible || defaultSetting){
              accessTypes.add(GRTConstants.ACCESS_TYPE_MODEM);
        }
	  return accessTypes;
}
    
    @SuppressWarnings("finally")
	public List<LocalTRConfig> getLocalTRConfig(TRConfig trConfig) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.getLocalTRConfig");
        List<LocalTRConfig> localTRConfigsByProduct = new ArrayList<LocalTRConfig>();
        List<LocalTRConfig> localTRConfigsByTemplate = new ArrayList<LocalTRConfig>();
        List<LocalTRConfig> localTRConfigsBySeCode = new ArrayList<LocalTRConfig>();
        String productType=null;
        if (trConfig != null) {
	        try {
	           // String queryString = "Select localTRConfig from LocalTRConfig localTRConfig where ";
	            if(StringUtils.isNotEmpty(trConfig.getProductType())) {
	            	//queryString += " (upper(localTRConfig.productType)='" + trConfig.getProductType().toUpperCase() + "') ";
	            	productType=trConfig.getProductType().toUpperCase();
	            }
	           
	         //   Query query = getSessionForGRT().createQuery(queryString);
	        	
	        	Query query=getSessionForGRT().getNamedQuery("getLocalTRConfig");
	        	query.setParameter("productType", productType);
	            localTRConfigsByProduct = query.list();
	            if (localTRConfigsByProduct != null && localTRConfigsByProduct.size() > 0) {
	            	logger.debug("Size of localTRConfigsByProduct:" + localTRConfigsByProduct.size());
	                for (LocalTRConfig config : localTRConfigsByProduct) {
	                	logger.debug("LocalTRConfigId:" + config.getLocalTRConfigId());
                		if(StringUtils.isNotEmpty(config.getTemplate()) && config.getTemplate().equalsIgnoreCase(trConfig.getTemplate())) {
                			logger.debug("Match Found for template:" + trConfig.getTemplate());
                			localTRConfigsByTemplate.add(config);
                		} else if(StringUtils.isNotEmpty(config.getSeCode()) && config.getSeCode().equalsIgnoreCase(trConfig.getSeCode())) {
                			logger.debug("Match Found for SeCode:" + trConfig.getSeCode());
                			localTRConfigsBySeCode.add(config);
                		}
					}
	            }
	        } catch (HibernateException hibEx) {
	        	logger.error("", hibEx);
	            throw new DataAccessException(BaseHibernateDao.class, hibEx
	                    .getMessage(), hibEx);
	        } finally {
	        	if(localTRConfigsByProduct.size() <=0 && localTRConfigsByTemplate.size() <=0 && localTRConfigsBySeCode.size() <=0) {
	        		List<LocalTRConfig> localTRConfigs = new ArrayList<LocalTRConfig>();
	        		localTRConfigs.add(this.getDefaultLocalTRConfig(trConfig));
	        		return localTRConfigs;
	        	} else if(localTRConfigsByTemplate.size() >0) {
	        		return localTRConfigsByTemplate;
	        	} else if(localTRConfigsBySeCode.size() >0) {
	        		return localTRConfigsBySeCode;
	        	} else {
	        		return localTRConfigsByProduct;
	        	}
	        }
        }
        logger.debug("Exiting BaseHibernateDao.getLocalTRConfig");
        return null;
    }
    private LocalTRConfig getDefaultLocalTRConfig(TRConfig trConfig) {
    	LocalTRConfig localTRConfig = new LocalTRConfig();
    	if(trConfig != null) {
    		localTRConfig.setProductType(trConfig.getProductType());
    		localTRConfig.setTemplate(trConfig.getTemplate());
    		localTRConfig.setSeCode(trConfig.getSeCode());
    	}
    	localTRConfig.setConnectivityType(GRTConstants.DEFAULT_CONNECTIVITY_TYPE);
    	localTRConfig.setInadsEligible(true);
    	return localTRConfig;
    }
    
    public List<ProductRelease> getSSLVPNProductReleases() throws DataAccessException {
		logger.debug("Entering BaseHibernateDao.getSSLVPNProductReleases");
		List<ProductRelease> releaseList = null;
		try {
		   /* Query query = getSession().createQuery("Select productRelease from ProductRelease as productRelease" +
		                " where productRelease.sslVpnEligible = 1");*/
			Query query=getSessionForGRT().getNamedQuery("getSSLVPNProductReleases");
		    releaseList = query.list();
		} catch (HibernateException hibEx) {
		    throw new DataAccessException(BaseHibernateDao.class, hibEx
		            .getMessage(), hibEx);
		} catch (Exception ex) {
		    throw new DataAccessException(BaseHibernateDao.class, ex
		            .getMessage(), ex);
		}
		logger.debug("Entering BaseHibernateDao.getSSLVPNProductReleases");

		return releaseList;
    }
    
    /**
	 * Get the material code from compatibility matrix.
	 *
	 * @return compatibilityMatrixList contains CompatibilityMatrix
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<CompatibilityMatrix> getSECodeFromCompatibilityMatrix() throws DataAccessException{
		logger.debug("Entering BaseHibernateDao.getSECodeFromCompatibilityMatrix");
		List<CompatibilityMatrix> compatibilityMatrixList = null;
		try {
			Criteria criteria = getSessionForGRT().createCriteria(CompatibilityMatrix.class);
			criteria.setCacheable(true).setFetchSize(500);
			compatibilityMatrixList = criteria.list();
		} catch (HibernateException hibEx) {
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(BaseHibernateDao.class, ex
					.getMessage(), ex);
		}
		logger.debug("Exiting BaseHibernateDao.getSECodeFromCompatibilityMatrix");

		return compatibilityMatrixList;
	}
	
	/**
	 * Save the technical Registration for ART.
	 *
	 * @param technicalRegistration TechnicalRegistration
	 * @throws DataAccessException
	 */

	public String saveTechnicalRegistrationForSALGateway(TechnicalRegistration technicalRegistration) throws DataAccessException {
		Session session =null;
		try {
			session = getSessionForGRT();
			session.beginTransaction();
			session.saveOrUpdate(technicalRegistration);
	      
	        session.getTransaction().commit();
	        	        
		} catch (HibernateException hibEx) {
			
			session.close();
			
			session = getSessionForGRT();
			
			session.beginTransaction();
			
	        session.merge(technicalRegistration);
	      
	        session.getTransaction().commit();
		}

		return technicalRegistration.getTechnicalRegistrationId();
	}
	 /**
     * Update SRRequest with siebelSR number/status
     *
     * @param srRequest
     * @param status
     * @return request SRRequest
     * @throws DataAccessException
     */
    public SRRequest updateSRRequest(SRRequest srRequest) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.updateSRRequest()");
        SRRequest request = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(SRRequest.class);
            criteria.add(Restrictions.eq("srRequestId", srRequest.getSrRequestId()));
            request = (SRRequest) criteria.uniqueResult();
            request.setUpdatedDate(new Date());

            if(StringUtils.isEmpty(request.getSiebelSRNo()) && StringUtils.isNotEmpty(srRequest.getSiebelSRNo())) {
                request.setSiebelSRNo(srRequest.getSiebelSRNo());
            }

            if(srRequest.getStatus() != null) {
                criteria =  session.createCriteria(Status.class);
                criteria.add(Restrictions.eq("statusId", srRequest.getStatus().getStatusId()));
                Status s = (Status) criteria.uniqueResult();
                request.setStatus(s);
            }

            session.saveOrUpdate(request);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.updateSRRequest()");

        return request;
    }
   
	/**
     * Method to get the site registration by the given registrationId.
     *
     * @param registrationId string
     * @return registration SiteRegistration
     */
    /*public SiteRegistration getSiteRegistration(String registrationId) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.getSiteRegistration() for registration Id : " + registrationId);
        SiteRegistration registration = null;
        try {
        	//getSessionForGRT().flush();
        	String regId=registrationId.trim();
        	Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", regId);
        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }  catch (Exception ex) {
            throw new DataAccessException(BaseHibernateDao.class, ex
                    .getMessage(), ex);
        }
        logger.debug("Exiting BaseHibernateDao.getSiteRegistration()");

        return registration;
    }*/
    
    public SiteRegistration getSiteRegistration(String registrationId) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.getSiteRegistration() for registration Id : " + registrationId);
        SiteRegistration registration = null;
        try {
        	//getSessionForGRT().flush();
        	String regId=registrationId.trim();
            Criteria criteria =  getSessionForGRT().createCriteria(SiteRegistration.class);
            criteria.add(Restrictions.eq("registrationId", regId));
            registration = (SiteRegistration)  criteria.uniqueResult();
            /*if(null != registration) {
            	getSessionForGRT().refresh(registration);
            }*/
            //System.out.println("registration------------>>"+registration);
        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }  catch (Exception ex) {
            throw new DataAccessException(BaseHibernateDao.class, ex
                    .getMessage(), ex);
        }
        logger.debug("Exiting BaseHibernateDao.getSiteRegistration()");

        return registration;
    }
    
    public TRConfig loadTRConfig(String groupId) {
    	logger.debug("Entering BaseHibernateDao.loadTRConfig for groupId:" + groupId + " and size of trConfigs" + trConfigs.size());
    	List<TRConfig> configs = trConfigs.get(groupId);
    	if(configs != null) {
    		logger.debug("Size of configs for groupId:" + configs.size());
    		for (TRConfig config : configs) {
				if(config.isMainMaterialCode()) {
					return config;
				}
			}
    	} else {
    		logger.debug("groupId:" + groupId + ": NOT Found");
    	}
    	return null;
    }
    
    /**
     * Ceate SRRequest for SiteRegistration with the given process step
     *
     * @param reg SiteRegistration
     * @return srRequest SRRequest
     * @throws DataAccessException
     */
    public SRRequest initSRRequest(SiteRegistration reg, ProcessStepEnum processStep) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.initSRRequest(SiteRegistration reg)");
        Session session = getSessionForGRT();
        
        Criteria criteria =  session.createCriteria(SiteRegistration.class);
        criteria.add(Restrictions.eq("registrationId", reg.getRegistrationId()));
        SiteRegistration registration = (SiteRegistration)  criteria.uniqueResult();
        if(registration == null) {
            throw new DataAccessException(BaseHibernateDao.class, "SiteRegistration with the registrationId not found", null);
        }
        SRRequest srRequest = null;
        if(ProcessStepEnum.INSTALL_BASE_CREATION == processStep) {
            if(registration.getInstallBaseSrRequest() == null) {
                registration.setInstallBaseSrRequest(createSRRequest());
            }
            srRequest = registration.getInstallBaseSrRequest();

        }
        else if(ProcessStepEnum.FINAL_RECORD_VALIDATION == processStep) {
            if(registration.getFinalValidationSrRequest() == null) {
                registration.setFinalValidationSrRequest(createSRRequest());
            }
            srRequest = registration.getFinalValidationSrRequest();
        }
        else if(ProcessStepEnum.EQUIPMENT_MOVE == processStep) {
            if(registration.getEqrMoveSrRequest() == null) {
                registration.setEqrMoveSrRequest(createSRRequest());
            }
            srRequest = registration.getEqrMoveSrRequest();
        }

        session.beginTransaction();
        session.saveOrUpdate(registration);
        session.getTransaction().commit();
        logger.debug("Exiting BaseHibernateDao.initSRRequest(SiteRegistration reg)");
        return srRequest;
    }
    /**
     * Create SRRequest with Initiation status
     *
     * @return request SRRequest
     * @throws DataAccessException
     */
    public SRRequest createSRRequest() throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.createSRRequest()");
        SRRequest srRequest = new SRRequest();
        srRequest.setCreatedDate(new Date());
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(Status.class);
            criteria.add(Restrictions.eq("statusId", SRRequestStatusEnum.INITIATION.getStatusId()));
            Status s = (Status) criteria.uniqueResult();
            srRequest.setStatus(s);

            session.saveOrUpdate(srRequest);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.createSRRequest()");

        return srRequest;
    }
    
    /**
     * Ceate SRRequest for SiteRegistration for Active Contracted Assets
     *
     * @param reg SiteRegistration
     * @return srRequest SRRequest
     * @throws DataAccessException
     */
    public SRRequest initSRRequestForActiveContracts(SiteRegistration reg) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.initSRRequestForActiveContracts(SiteRegistration reg)");
        Session session = getSessionForGRT();
        session.beginTransaction();
        Criteria criteria =  session.createCriteria(SiteRegistration.class);
        criteria.add(Restrictions.eq("registrationId", reg.getRegistrationId()));
        SiteRegistration registration = (SiteRegistration)  criteria.uniqueResult();
        if(registration == null) {
            throw new DataAccessException(BaseHibernateDao.class, "SiteRegistration with the registrationId not found", null);
        }
        SRRequest srRequest = null;
        if(registration.getEqrActiveContractsSrRequest() == null) {
            registration.setEqrActiveContractsSrRequest(createSRRequest());
        }
        srRequest = registration.getEqrActiveContractsSrRequest();

        session.saveOrUpdate(registration);
        session.getTransaction().commit();
        logger.debug("Exiting BaseHibernateDao.initSRRequestForActiveContracts(SiteRegistration reg)");
        return srRequest;
    }
    
    public SRRequest initSRRequest(SalProductRegistration salProductRegistration, TechRecordEnum type) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.initSRRequest");
        Session session = getSessionForGRT();
        //session.beginTransaction();
        SRRequest srRequest = null;

        if(TechRecordEnum.IP_MODEM.getSalType().equals(type.getSalType()) || TechRecordEnum.IPO.getSalType().equals(type.getSalType())) {
        	TechnicalRegistration technicalRegistration = salProductRegistration.getTechnicalRegistration();
        	 if(technicalRegistration.getSrRequest() == null) {
        		 technicalRegistration.setSrRequest(createSRRequest());
             }
             srRequest = technicalRegistration.getSrRequest();
             //session.saveOrUpdate(technicalRegistration);
             logger.debug(" SRRequest created for technicalRegistration ");
        }

        if( TechRecordEnum.SAL_SITE_LIST.getSalType().equals(type.getSalType())){
        	SiteList siteList = salProductRegistration.getSiteList();
        	if(siteList.getSrRequest() == null){
        		siteList.setSrRequest(createSRRequest());
        	}
        	srRequest = siteList.getSrRequest();
        	//session.saveOrUpdate(siteList);
        	logger.debug(" SRRequest created for siteList");
        }

        //session.getTransaction().commit();
        if(srRequest==null){
			logger.debug("SRRequest is Null in SalBaseHibernateDao----CreateSR---");
		}
        logger.debug("Exiting BaseHibernateDao.initSRRequest(SalMigration reg)");
        return srRequest;
    }
    /**
     * Method to get the Technical Order for the given registration id and order type.
     *
     * @param registrationId String
     * @param orderType String
     * @return technicalOrderList List
     * @throws DataAccessException Exception
     */
    public List<TechnicalOrder> getTechnicalOrderByType(String registrationId, String orderType)
            throws DataAccessException {
        List<TechnicalOrder> technicalOrderList = null;

        logger.debug("Entering BaseHibernateDao.getTechnicalOrderByType");

        try {
            logger.debug("Technical Order Request ID : " + registrationId);
            
            /*
            StringBuffer sb = new StringBuffer();
            sb.append("Select technicalOrder from TechnicalOrder technicalOrder where technicalOrder.siteRegistration.registrationId=:registrationId ")
            .append(" and technicalOrder.orderType =:orderType ");
            if(GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)){
            	sb.append(" and upper(technicalOrder.deleted) = 'YES' ");
            }
            sb.append(" order by technicalOrder.productLine, to_number(regexp_substr(technicalOrder.materialCode,'^[0-9]+'))");
            String  queryString = sb.toString();
            Query query = getSessionForGRT().createQuery(queryString);
            */
            Query query = null;
            if(GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)) {
            	query = getSessionForGRT().getNamedQuery("fetchTechnicalOrderByTypeD");
            }  
            //GRT 4.0 Change : Defect #372
            else if( GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType) ){
            	query = getSessionForGRT().getNamedQuery("fetchTechnicalOrderByTypeEM");
            }else{
            	query = getSessionForGRT().getNamedQuery("fetchTechnicalOrderByType");
            }
            
            query.setString("registrationId", registrationId);
            if(StringUtils.isNotEmpty(orderType)) {
            	query.setString("orderType", orderType);
            }
            technicalOrderList = query.list();
            if (technicalOrderList != null) {
                logger.debug("technicalOrderList size : "
                        + technicalOrderList.size());
            }
        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }

        logger.debug("Exiting BaseHibernateDao.getTechnicalOrderByType");

        return technicalOrderList;
    }
    
    public String updateTechnicalOrderErrorDescription(List<TechnicalOrderDetail> technicalOrderList) throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.updateTechnicalOrderErrorDescription()");
    	try {
    		Criteria criteria = null;
    		Session session = getSessionForGRT();
            session.beginTransaction();

            for(TechnicalOrderDetail to : technicalOrderList){
                TechnicalOrder technicalOrder;
                if(!StringUtils.isEmpty(to.getOrderId())) {
                	criteria =  session.createCriteria(TechnicalOrder.class);
                    criteria.add(Restrictions.eq("orderId", to.getOrderId()));
                    technicalOrder = (TechnicalOrder)  criteria.uniqueResult();
                    if(to.getErrorDescription() != null){
                    	technicalOrder.setError_Desc(to.getErrorDescription());
                    }
                    if(to.getSr_created() != null && GRTConstants.YES.equalsIgnoreCase(to.getSr_created())){
                    	technicalOrder.setSr_Created(to.getSr_created());
                    }
                    session.saveOrUpdate(technicalOrder);
                }
            }
            session.getTransaction().commit();
        } catch (HibernateException hibEx) {
        	logger.error("", hibEx);
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
    	logger.debug("Exiting BaseHibernateDao.updateTechnicalOrderErrorDescription()");
    	return null;
    }
    
    /**
     * Update SiteRegistration Status with the given status
     *
     * @param siteRegistration
     * @param status
     * @return siteRegistration SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateSiteRegistrationStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep, boolean processDateUpdateFlag) throws DataAccessException {
    	logger.debug("Starting for BaseHibernateDao.updateSiteRegistrationStatus Registration ID [" + siteRegistration.getRegistrationId() + "] and status ID to update [" + status.getStatusId() + "]");
        Transaction transaction = null;
 		SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
 			transaction = session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());
			//session.refresh(registration);
			Criteria criteria =  session.createCriteria(Status.class);
            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
            Status s = (Status)  criteria.uniqueResult();
            if (processStep !=null) {
            		if (processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
            			registration.setInstallBaseStatus(s);
            			if(StatusEnum.INPROCESS.getStatusId().equals(s.getStatusId()) && processDateUpdateFlag){
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
            			if(StatusEnum.INPROCESS.getStatusId().equals(s.getStatusId()) && processDateUpdateFlag){
            				registration.setEqrSubmittedDate(new Date());
            			} else if(StatusEnum.COMPLETED.getStatusId().equals(s.getStatusId())){
            				registration.setEqrCompletedDate(new Date());
            			}
            		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())){
            			registration.setEqrMoveStatus(s);
            			if(StatusEnum.INPROCESS.getStatusId().equals(s.getStatusId()) && processDateUpdateFlag){
            				registration.setEqrMoveSubmittedDate(new Date());
            			} else if(StatusEnum.COMPLETED.getStatusId().equals(s.getStatusId())){
            				registration.setEqrMoveCompletedDate(new Date());
            			}
            		}
            }

            session.saveOrUpdate(registration);
            logger.debug("Committing change into database ...");
            //session.getTransaction().commit();
            transaction.commit();
        } catch (HibernateException hibEx) {
			transaction.rollback();
			logger.debug("Error in updateSiteRegistrationStatus");
			hibEx.printStackTrace();
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			transaction.rollback();
			logger.debug("Error in updateSiteRegistrationStatus");
			ex.printStackTrace();
			throw new DataAccessException(BaseHibernateDao.class, ex
					.getMessage(), ex);
		}
        logger.debug("Exiting BaseHibernateDao.updateSiteRegistrationStatus");
        return registration;
    }
    
    public List<TechnicalOrderDetail> fetchEquipmentsWithSameSIDnMID(TechnicalOrderDetail technicalOrderDetail) throws DataAccessException{
    	logger.debug("Entering BaseHibernateDao.fetchEquipmentsWithSameSIDnMID");
    	List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
    	TechnicalOrderDetail techOrder = null;
    	StringBuilder sb = new StringBuilder();
    	Query query = null;
    	try{
	    	/*sb.append("select sasset.row_id as asset_pk,asset_num, sasset.x_se_cd, sasset.x_seid, orgext.loc, sasset.x_summary_equipment_num, ")
	    			 .append("sasset.x_equipment_num, sasset.x_rfa_sid, sasset.x_rfa_mid, prodint.name as material_code ")
	    			 .append("from  siebel.s_asset sasset, siebel.s_org_ext orgext, siebel.s_prod_int prodint ")
	    			 .append("where sasset.x_seid IS NOT NULL and orgext.row_id = sasset.owner_accnt_id and sasset.status_cd <> 'Inactive' ")
	    			 .append("and orgext.loc = :soldToParam and sasset.prod_id = prodint.row_id  and sasset.x_rfa_sid = :sid and sasset.x_rfa_mid = :mid");
	    	query = getSession().createSQLQuery(sb.toString());*/
    		query=getSessionForSiebel().getNamedQuery("fetchEquipmentsWithSameSIDnMID");
	    	query.setParameter("soldToParam", technicalOrderDetail.getSoldToId());
	    	query.setParameter("sid", technicalOrderDetail.getSid());
	    	query.setParameter("mid", technicalOrderDetail.getMid());
	    	List<Object[]> resultSet = query.list();
	    	// Iterate through the result set
	    	for (Object[] object : resultSet) {
	    		techOrder = new TechnicalOrderDetail();
	    		techOrder.setAssetPK(object[0]!=null?object[0].toString():"");
	    		techOrder.setAssetNumber(object[1]!=null?object[1].toString():"");
	    		techOrder.setSolutionElementCode(object[2]!=null?object[2].toString():"");
	    		techOrder.setSolutionElementId(object[3]!=null?object[3].toString():"");
	    		techOrder.setSummaryEquipmentNumber(object[5]!=null?object[5].toString():"");
	    		techOrder.setEquipmentNumber(object[6]!=null?object[6].toString():"");
	    		techOrder.setSid(object[7]!=null?object[7].toString():"");
	    		techOrder.setMid(object[8]!=null?object[8].toString():"");
	    		techOrder.setMaterialCode(object[9]!=null?object[9].toString():"");
	    		//GRT 4.0 Change : Update the sold-to info for Equipment Move
	    		techOrder.setSoldToId(technicalOrderDetail.getSoldToId());
	    		techOrder.setToSoldToId(technicalOrderDetail.getToSoldToId());
	    		
	    		todList.add(techOrder);
		    	/*if(techOrder.getSummaryEquipmentNumber() != null && technicalOrderDetail.getSummaryEquipmentNumber() != null
		    			&& techOrder.getSummaryEquipmentNumber().equalsIgnoreCase(technicalOrderDetail.getSummaryEquipmentNumber())){
		    		todList.add(techOrder);
	    		}*/
	    	}
	    	logger.debug("****************************After - Query Execution **************************************"+todList.size());
	    }  catch (HibernateException hibEx) {
	    	logger.error("HibernateException in fetchEquipmentsWithSameSIDnMID:"+hibEx.getMessage());
			throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			logger.error("Exception in fetchEquipmentsWithSameSIDnMID:"+ex.getMessage());
			throw new DataAccessException(BaseHibernateDao.class, ex.getMessage(), ex);
		}
		logger.debug("Exiting BaseHibernateDao.fetchEquipmentsWithSameSIDnMID");
    	return todList;
    }
    /**
     * Save the Technical Order List for fields MaterialCode, InitialQuantity and Description.
     *
     * @param technicalOrderList List<TechnicalOrder>
     * @throws DataAccessException
     */
    public List<TechnicalOrder> saveTechnicalOrderList(List<TechnicalOrder> technicalOrderList) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.saveTechnicalOrderList()");
        try {
        	Session session = getSessionForGRT();
            session.beginTransaction();
            SiteRegistration siteRegistration = (SiteRegistration)  getEntity(session,SiteRegistration.class,"registrationId", technicalOrderList.get(0).getSiteRegistration().getRegistrationId());
            for(TechnicalOrder to : technicalOrderList){
                TechnicalOrder technicalOrder;
                if(!StringUtils.isEmpty(to.getOrderId())) {
                    technicalOrder = (TechnicalOrder) getEntity(session,TechnicalOrder.class,"orderId", to.getOrderId());
                } else {
                    technicalOrder = new TechnicalOrder();
                    logger.debug("saveTechnicalOrderList -- Registration Id:  "+to.getSiteRegistration().getRegistrationId());                    
                    technicalOrder.setSiteRegistration(siteRegistration);
                    technicalOrder.setSolutionElementCode(to.getSolutionElementCode());
                }
                technicalOrder.setMaterialCode(to.getMaterialCode());
                technicalOrder.setInitialQuantity(to.getInitialQuantity());
                technicalOrder.setRemainingQuantity(to.getRemainingQuantity());
                technicalOrder.setCreatedDate(to.getCreatedDate());
                technicalOrder.setDescription(to.getDescription());
                technicalOrder.setDeleted(to.getDeleted());
                technicalOrder.setOrderType(to.getOrderType());
                technicalOrder.setIsBaseUnit(to.getIsBaseUnit());
                technicalOrder.setAutoTR(to.isAutoTR());
                if(to.getOpenQuantity() != null){
                    technicalOrder.setOpenQuantity(to.getOpenQuantity());
                 }
                if(to.getError_Desc() != null){
                	technicalOrder.setError_Desc(to.getError_Desc());
                }
                if(to.getSr_Created() != null){
                	technicalOrder.setSr_Created(to.getSr_Created());
                }
                /**  [AVAYA]: 09-12-2011 set SerialNumber to save(Start) **/
                technicalOrder.setSerialNumber(to.getSerialNumber());
                /**  [AVAYA]: 09-12-2011 set SerialNumber to save (End) **/
                technicalOrder.setIsSalesOut(to.getIsSalesOut());
                /** GRT3.0 04-15-2013  */
                technicalOrder.setProductLine(to.getProductLine());
                technicalOrder.setSolutionElementCode(to.getSolutionElementCode());
                technicalOrder.setSeid(to.getSeid());
                /** Added to persist SAL Primary and Secondary SEIDs */
                technicalOrder.setSalSeIdPrimarySecondary(to.getSalSeIdPrimarySecondary());
                technicalOrder.setHasActiveEquipmentContract(to.getHasActiveEquipmentContract());
                technicalOrder.setHasActiveSiteContract(to.getHasActiveSiteContract());
                technicalOrder.setEquipmentNumber(to.getEquipmentNumber());
                technicalOrder.setSummaryEquipmentNumber(to.getSummaryEquipmentNumber());
                technicalOrder.setSid(to.getSid());
                technicalOrder.setMid(to.getMid());
                technicalOrder.setAssetPK(to.getAssetPK());
                technicalOrder.setMaterialExclusion(to.getMaterialExclusion());
                technicalOrder.setSalGateway(to.isSalGateway());
                technicalOrder.setActionType(to.getActionType());
                technicalOrder.setCompletedDate(to.getCompletedDate());
                technicalOrder.setCompletedBysiteRegId(to.getCompletedBysiteRegId());

                //GRT 4.0 Changes
                if(technicalOrder.getOrderId()==null){
                  session.saveOrUpdate(technicalOrder);
                  to.setOrderId(technicalOrder.getOrderId());//Brought back the orderId after save the TechnicalOrder
                }
                
            }
            session.getTransaction().commit();
            return technicalOrderList;
        } catch (HibernateException hibEx) {
            logger.error("Error in BaseHibernateDao.saveTechnicalOrderList : " +  hibEx.getMessage());
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }

    }
    
    public void updateTODOpenQuantity(String registrationId, String completedByRegId, List<String> materialCodes, String orderType) throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.updateTODOpenQuantity");
 		Transaction transaction = null;
 		try {
 			if(registrationId != null) {
 				StringBuffer sf = null;
				// Framing materialCodes IN clause
				sf = new StringBuffer("(");
				for (String string : materialCodes) {
					sf.append("'" + string + "',");
				}
				String mcInClause = sf.substring(0, sf.length()-1);
			    //	mcInClause += ")";
	 			transaction = getSessionForGRT().beginTransaction();
				//SQLQuery sqlQuery = getSession().createSQLQuery("UPDATE TECHNICAL_ORDER SET OPEN_QUANTITY = 0 WHERE ORDER_TYPE='"+orderType+"' AND SITE_REGISTRATION_ID = '"+registrationId+"' and MATERIAL_CODE IN " + mcInClause);
	 			Query sqlQuery = getSessionForGRT().getNamedQuery("updateTODOpenQuantity");
	 			sqlQuery.setParameter("completedByRegId", completedByRegId);
	 			sqlQuery.setParameter("orderType",orderType);
	 			sqlQuery.setParameter("registrationId",registrationId);
	 			//sqlQuery.setParameter("mcInClause",mcInClause);
	 			sqlQuery.setParameterList("mcInClause", materialCodes);
	 			logger.debug("Query : "+sqlQuery.toString());
				sqlQuery.executeUpdate();
				transaction.commit();
 			}
		} catch (HibernateException hibEx) {
			logger.error("", hibEx);
			transaction.rollback();
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Throwable throwable) {
			logger.error("", throwable);
			transaction.rollback();
			throw new DataAccessException(BaseHibernateDao.class, throwable
					.getMessage(), throwable);
		} finally {
			logger.debug("Exiting BaseHibernateDao.updateTODOpenQuantity");
		}
	}
    
    
    public void updateProcessedPipelineTransactions(List<String> registrationIds, List<String> materialCodes, String orderType) throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.updateProcessedPipelineTransactions");
 		Transaction transaction = null;
 		try {
 			if(registrationIds != null && registrationIds.size() > 0) {
 				StringBuffer sf = null;
				sf = new StringBuffer("(");
				for (String string : registrationIds) {
					sf.append("'" + string + "', ");
				}
				String registrationInClause = sf.substring(0, sf.length()-2);
				//registrationInClause += ")";
				// Framing materialCodes IN clause
				sf = new StringBuffer("(");
				for (String string : materialCodes) {
					sf.append("'" + string + "', ");
				}
				String mcInClause = sf.substring(0, sf.length()-2);
				//mcInClause += ")";
	 			transaction = getSessionForGRT().beginTransaction();
				Query sqlQuery = getSessionForGRT().getNamedQuery("updateProcessedPipelineTransactions");
						//createSQLQuery("UPDATE PIPELINE_SAP_TRANSACTIONS SET PROCESSED=1,PROCESSED_DATE=SYSDATE WHERE REGISTRATION_ID IN " + registrationInClause + " AND ACTION = '"+orderType+"' AND MATERIAL_CODE IN " + mcInClause);
				sqlQuery.setParameterList("registrationInClause", registrationIds);
				sqlQuery.setParameter("orderType",orderType);
				//sqlQuery.setParameter("mcInClause", mcInClause);
				sqlQuery.setParameterList("mcInClause", materialCodes);
				logger.debug("Query : "+sqlQuery.toString());
				sqlQuery.executeUpdate();
				transaction.commit();
				/*getSession().connection().commit();
				getSession().flush();*/
 			}
		} catch (HibernateException hibEx) {
			logger.error("", hibEx);
			transaction.rollback();
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Throwable throwable) {
			logger.error("", throwable);
			transaction.rollback();
			throw new DataAccessException(BaseHibernateDao.class, throwable
					.getMessage(), throwable);
		} finally {
			logger.debug("Exiting BaseHibernateDao.updateProcessedPipelineTransactions");
		}
	}
    
    /**
     * Save/Update siteRegistration
     * @param siteRegistration
     * @return SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateIsSrCompletedSiteRegistration(SiteRegistration siteRegistration, ProcessStepEnum processStep,String isSrCompleted) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.updateSiteRegistration()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());
			if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())){
				registration.setIsSrCompleted(isSrCompleted);
			} else if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
				registration.setIsEQRSrCompleted(isSrCompleted);
			} else if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())){
				registration.setIsEQMSrCompleted(isSrCompleted);
			}
            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.updateSiteRegistration()");

        return registration;
    }
    
    /**
     * Get material code description
     *
     * @param materialCode string
     * @return materialCodeDesc string
     */
    public Map<String,String> getMaterialCodeDesc(List<String> materialCodes) throws DataAccessException {
    	logger.debug("BaseHibernateDao.getMaterialCodeDesc");
    	Map<String,String> masterMaterialsMap = new HashMap<String, String>();;
         try {
        	// String sqlQuery = "select * from material_master m where m.material_code in ( :materialCodes )";

          //   Query query = getSessionForGRT().createSQLQuery(sqlQuery);
        	 Query query=getSessionForGRT().getNamedQuery("getMaterialCodeDesc");
             query.setParameterList("materialCodes", materialCodes);
             List queryResult = query.list();
             for (int i = 0; i < queryResult.size(); i++) {
                 Object[] listItem = (Object[]) queryResult.get(i);
                 masterMaterialsMap.put((String) listItem[0], (String) listItem[1]);
             }
         }catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        return masterMaterialsMap;
    }
    
    
    
    /**
     * Get Agreements for Assets
     *
     * @param materialCodes List<String>
     * @return materialAgreemnetsMap Map<String,List<String>
     */
    public Map<String,List<String>> getAgreementsForAssets(List<String> assetIds) throws DataAccessException {
    	logger.debug("BaseHibernateDao.getAgreementsForAssets");
    	Map<String,List<String>> assetAgreementsMap = new HashMap<String, List<String>>();
    	List<String> trimmedAassetIds = null;
    	int listSize = assetIds.size();
    	try {
    		if(listSize > 0) {
    			for(int index = 0; index <= listSize / 1000 ; index ++  ) {
    				int startIndex = 0;
    				int endIndex = 0; 		    
    				if(index == 0) {       		    
    					startIndex = 0;
    				} else {
    					startIndex = index * 1000;
    				}
    				endIndex = startIndex + 999;
    				if(endIndex >= listSize ) {
    					endIndex = listSize;
    				}
    				trimmedAassetIds = assetIds.subList(startIndex, endIndex);
    				// String sqlQuery = "select * from material_master m where m.material_code in ( :materialCodes )";

    				//   Query query = getSessionForGRT().createSQLQuery(sqlQuery);
    				Query query=getSessionForSiebel().getNamedQuery("getAgreementsForAssets");
    				query.setParameterList("assetIds", trimmedAassetIds);
    				List queryResult = query.list();
    				//TODO Change 0 and 1 to the corresponding result index after running the query.
    				for (int i = 0; i < queryResult.size(); i++) {
    					Object[] listItem = (Object[]) queryResult.get(i);
    					if(null != assetAgreementsMap.get((String) listItem[0])) {
    						List<String> agreementsList = assetAgreementsMap.get((String) listItem[0]);
    						agreementsList.add((String) listItem[1]);
    						assetAgreementsMap.put((String) listItem[0], agreementsList);
    					}else {
    						List<String> agreementsList = new ArrayList<String>();
    						agreementsList.add((String) listItem[1]);
    						assetAgreementsMap.put((String) listItem[0], agreementsList); 
    					}
    				}
    			}
    		}
    	}catch (HibernateException hibEx) {
    		throw new DataAccessException(BaseHibernateDao.class, hibEx
    				.getMessage(), hibEx);
    	}
    	return assetAgreementsMap;
    }
    
    /**
     * Get Agreements for Assets
     *
     * @param materialCodes List<String>
     * @return materialAgreemnetsMap Map<String,List<String>
     */
    public Map<String,List<String>> getAccessTypesForAssets(List<String> assetIds) throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.getAccessTypesForAssets");
    	Map<String,List<String>> assetAccessTypeMap = new HashMap<String, List<String>>();
    	List<String> trimmedAassetIds = null;
    	int listSize = assetIds.size();
    	try {
    		// String sqlQuery = "select * from material_master m where m.material_code in ( :materialCodes )";
    		if(listSize > 0) {
    			for(int index = 0; index <= listSize / 1000 ; index ++  ) {
    				int startIndex = 0;
    				int endIndex = 0; 		    
    				if(index == 0) {       		    
    					startIndex = 0;
    				} else {
    					startIndex = index * 1000;
    				}
    				endIndex = startIndex + 999;
    				if(endIndex >= listSize ) {
    					endIndex = listSize;
    				}
    				trimmedAassetIds = assetIds.subList(startIndex, endIndex);
    				//   Query query = getSessionForGRT().createSQLQuery(sqlQuery);
    				Query query=getSessionForSiebel().getNamedQuery("getAccessTypesForAssets");
    				//Query query=getSessionForSiebel().getNamedQuery("getMaterialCodeDesc");
    				query.setParameterList("assetIds", trimmedAassetIds);
    				List queryResult = query.list();
    				//TODO Change 0 and 1 to the corresponding result index after running the query.
    				for (int i = 0; i < queryResult.size(); i++) {
    					Object[] listItem = (Object[]) queryResult.get(i);
    					if(null != assetAccessTypeMap.get((String) listItem[0])) {
    						List<String> agreementsList = assetAccessTypeMap.get((String) listItem[0]);
    						agreementsList.add((String) listItem[1]);
    						agreementsList.add((String) listItem[2]);
    						agreementsList.add((String) listItem[3]);
    						agreementsList.add((String) listItem[4]);
    						//assetAccessTypeMap.put((String) listItem[0], agreementsList);
    					}else {
    						List<String> agreementsList = new ArrayList<String>();
    						agreementsList.add((String) listItem[1]);
    						agreementsList.add((String) listItem[2]);
    						agreementsList.add((String) listItem[3]);
    						agreementsList.add((String) listItem[4]);
    						assetAccessTypeMap.put((String) listItem[0], agreementsList); 
    					}
    				}
    			}
    		}
    	}catch (HibernateException hibEx) {
    		throw new DataAccessException(BaseHibernateDao.class, hibEx
    				.getMessage(), hibEx);
    	}
    	return assetAccessTypeMap;
    }
    
    /**
     * Method to update the site registration processStep and status by the given registrationId, processStepId and statusId.
     *
     * @param registrationId string
     * @param processStepId
     * @param statusId
     * @return registration SiteRegistration
     */
    public SiteRegistration updateSiteRegistrationProcessStepAndStatus(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.updateSiteRegistrationProcessStepAndStatus()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();

			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
			//session.refresh(registration);
			
			Criteria criteria =  session.createCriteria(ProcessStep.class);
            criteria.add(Restrictions.eq("processStepId", step.getProcessStepId()));
            ProcessStep processStep = (ProcessStep)  criteria.uniqueResult();
            registration.setProcessStep(processStep);

            criteria =  session.createCriteria(Status.class);
            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
            Status s = (Status)  criteria.uniqueResult();
            registration.setStatus(s);
            
            if(StatusEnum.SAVED.getStatusId().equals(status.getStatusId())){
            	registration.setSubmitted(GRTConstants.isSubmitted_false);
            }

            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.updateSiteRegistrationProcessStepAndStatus()");

        return registration;

    }
    
    
    /**
     * Method to update the site registration completed date for Validated registrations by the given registrationId, processStepId and statusId.
     *
     * @param registrationId string
     * @param processStepId
     * @param statusId
     * @return registration SiteRegistration
     */
    public SiteRegistration updateSiteRegistrationCompleteDateForValidate(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.updateSiteRegistrationProcessStepAndStatus()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();

                                                registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
                                                //session.refresh(registration);
                                                
                                                Criteria criteria =  session.createCriteria(ProcessStep.class);
            criteria.add(Restrictions.eq("processStepId", step.getProcessStepId()));
            ProcessStep processStep = (ProcessStep)  criteria.uniqueResult();
            registration.setProcessStep(processStep);

            criteria =  session.createCriteria(Status.class);
            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
            Status s = (Status)  criteria.uniqueResult();
            registration.setStatus(s);
            
            if(StatusEnum.SAVED.getStatusId().equals(status.getStatusId())){
                registration.setSubmitted(GRTConstants.isSubmitted_false);
            }

            if(StatusEnum.VALIDATED.getStatusId().equals(status.getStatusId())){
                registration.setEqrCompletedDate(new Date());;
            }
            
            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.updateSiteRegistrationProcessStepAndStatus()");

        return registration;

    }

    
    public List<String> getReleases(String seCode, String productType, String template, String specialNote, boolean sslvpn) throws DataAccessException {
    	logger.debug("Entering BaseHibernateDao.getReleases for seCode:" + seCode + " productType:" + productType + " template:" + template + " sslvpn:" + sslvpn);
    	try {
	    	Set<String> prodReleases = new HashSet<String>();
	    	Set<String> prodSeCodeReleases = new HashSet<String>();
	    	Set<String> prodTemplateReleases = new HashSet<String>();
	    	if(StringUtils.isNotEmpty(productType)) {

	    		try {
	              /*  String queryString = "Select productRelease from ProductRelease productRelease where upper(productRelease.productType)='" + productType.toUpperCase() + "'";
	                if(sslvpn && !GRTConstants.SPECIAL_NOTE_SSLVPN.equalsIgnoreCase(specialNote)) {
	                	queryString += " and productRelease.sslVpnEligible=1";
	                } else {
	                	queryString += " and productRelease.sslVpnEligible=0";
	                }
	                Query query = getSessionForGRT().createQuery(queryString);*/
	    			
	    			productType=productType.toUpperCase();
	    			Query query=getSessionForGRT().getNamedQuery("getReleasesWithoutSSL");
	    			if(sslvpn && !GRTConstants.SPECIAL_NOTE_SSLVPN.equalsIgnoreCase(specialNote)) {
	    				query=getSessionForGRT().getNamedQuery("getReleasesWithSSL");
	                } 
	    			query.setParameter("productType", productType);
	                List<ProductRelease> productReleases = query.list();
	                if (productReleases != null && productReleases.size() > 0) {
		                logger.debug("Size of productReleases:" + productReleases.size());
	                	for (ProductRelease release : productReleases) {
	                		logger.debug("release:" + release.getProductType()+" Template:"+release.getTemplate()+" SeCode:"+release.getSeCode());
	                		if(StringUtils.isNotEmpty(release.getTemplate()) && release.getTemplate().equalsIgnoreCase(template)) {
	                			logger.debug("Match Found for template:" + template);
	                			prodTemplateReleases.add(release.getReleaseNumber());
	                		} else if(StringUtils.isNotEmpty(release.getSeCode()) && release.getSeCode().equalsIgnoreCase(seCode)) {
	                			logger.debug("Match Found for SeCode:" + seCode);
	                			prodSeCodeReleases.add(release.getReleaseNumber());
	                		}
	                		prodReleases.add(release.getReleaseNumber());
						}

	                }
	                if(prodTemplateReleases.size() > 0) {
	                	logger.debug("returning release for ProductType and Template");
	                	List<String> prodTemplateReleasesL = new ArrayList<String>();
	                	prodTemplateReleasesL.addAll(prodTemplateReleases);
	                	return prodTemplateReleasesL;
	                } else if(prodSeCodeReleases.size() > 0) {
	                	logger.debug("returning release for ProductType and SeCode");
	                	List<String> prodSeCodeReleasesL = new ArrayList<String>();
	                	prodSeCodeReleasesL.addAll(prodSeCodeReleases);
	                	return prodSeCodeReleasesL;
	                } else {
	                	logger.debug("returning release for ONLY ProductType");
	                	List<String> prodReleasesL = new ArrayList<String>();
	                	prodReleasesL.addAll(prodReleases);
	                	return prodReleasesL;
	                }
	            } catch (HibernateException hibEx) {
	            	logger.error("", hibEx);
	                throw new DataAccessException(BaseHibernateDao.class, hibEx
	                        .getMessage(), hibEx);
	            }
	    	}
    	} finally {
    		logger.debug("Exiting BaseHibernateDao.getReleases");
    	}
    	logger.debug("returning NULL from getReleases");
    	return null;
    }
    
    public String getPhoneNoByCountry(String countryCode) throws DataAccessException {
        logger.debug("Starting for country to get phone no. [" + countryCode + "]");
        CountryAlarmReceiver countryAlarmReceiver = null;
        String phoneNo=null;
        try {
        	
        	Criteria criteria =  getSessionForGRT().createCriteria(CountryAlarmReceiver.class);
            criteria.add(Restrictions.eq("countryCode", countryCode));
            countryAlarmReceiver = (CountryAlarmReceiver) criteria.uniqueResult();

        } catch (HibernateException hibEx) {
            logger.error("ERROR: While processing data access for countryCode ["
                    + countryCode + "]. Error message: " + hibEx.getMessage());
            throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
        }

        if(countryAlarmReceiver != null) {
            logger.debug("Completed. Returning found record with Phone No [" + countryAlarmReceiver.getPhoneNo() + "]");
            phoneNo=countryAlarmReceiver.getPhoneNo();
        } else {
            logger.debug("Completed. No qualified record found by given countryCode [" + countryCode + "]");
        }
        return phoneNo;
    }
    
    /**
     * Method to get the technical registration by the given technicalRegistrationId.
     *
     * @param registrationId string
     * @return registration SiteRegistration
     */
    public TechnicalRegistration getTechnicalRegistration(String technicalRegistrationId) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.getTechnicalRegistration()");
        TechnicalRegistration technicalRegistration = null;
        try {
            Criteria criteria =  getSessionForGRT().createCriteria(TechnicalRegistration.class);
            criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistrationId));
            technicalRegistration = (TechnicalRegistration)  criteria.uniqueResult();
            System.out.println("technicalRegistration"+technicalRegistration);
            if( technicalRegistration!=null ){
            	java.util.Set<ExpandedSolutionElement> explodedSolutionElements = technicalRegistration.getExplodedSolutionElements();
            	populateExpSolutionElementsList(explodedSolutionElements, technicalRegistration);
            }
        } catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.getTechnicalRegistration()");

        return technicalRegistration;

    }
    
    
    
    
    /**
     * Get Technical registration List by registration Id and Type
     * 
     * **/
    public List<TechnicalRegistration> getTechnicalRetestRegistrationListByregistrationId(String registrationId ,String Type)
    {
    	List<TechnicalRegistration> regSumList = new ArrayList<TechnicalRegistration>();
    	try{
    		
    		  Criteria criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class).createAlias("siteRegistration", "siteRegistration");
              criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
              criteria.add(Restrictions.eq("orderType", Type));
              List<TechnicalOrder> technicalOrders=(List<TechnicalOrder>)criteria.list();
              
              for(TechnicalOrder order: technicalOrders )
              {
            	  
            	  
            	  Criteria cri =  getSessionForGRT().createCriteria(TechnicalRegistration.class).createAlias("technicalOrder", "technicalOrder");
                  cri.add(Restrictions.eq("technicalOrder.orderId", order.getOrderId()));
                  List<TechnicalRegistration> techniList=(List<TechnicalRegistration>)cri.list();
                  regSumList.addAll(techniList);
              }
    	}catch(Exception e){
    		
    	}
    	
    	return regSumList;
    }
    
    /**
     * Get Technical registration List by registration Id
     * 
     * **/
    public List<TechnicalRegistration> getTechnicalRetestRegistrationListByregistrationId(String registrationId )
    {
    	List<TechnicalRegistration> regSumList = new ArrayList<TechnicalRegistration>();
    	try{
    		
    		  Criteria criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class).createAlias("siteRegistration", "siteRegistration");
              criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
            
              List<TechnicalOrder> technicalOrders=(List<TechnicalOrder>)criteria.list();
              
              for(TechnicalOrder order: technicalOrders )
              {
               	  Criteria cri =  getSessionForGRT().createCriteria(TechnicalRegistration.class).createAlias("technicalOrder", "technicalOrder");
                  cri.add(Restrictions.eq("technicalOrder.orderId", order.getOrderId()));
                  List<TechnicalRegistration> techniList=(List<TechnicalRegistration>)cri.list();
                  regSumList.addAll(techniList);
              }
    	}catch(Exception e){
    		
    	}
    	
    	return regSumList;
    }
    
    
    public List<TechnicalRegistrationSummary> getTechnicalRegistrationListByregistrationId(String registrationId ,String Type)
    {
    	List<TechnicalRegistrationSummary> regSumList = new ArrayList<TechnicalRegistrationSummary>();
    	try{
    		
    		  Criteria criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class).createAlias("siteRegistration", "siteRegistration");
              criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
              criteria.add(Restrictions.eq("orderType", Type));
                    
              List<TechnicalOrder> technicalOrders=(List<TechnicalOrder>)criteria.list();
              
              if(Type.equalsIgnoreCase("TR")){
            	  criteria =  getSessionForGRT().createCriteria(TechnicalOrder.class).createAlias("siteRegistration", "siteRegistration");
                  criteria.add(Restrictions.eq("siteRegistration.registrationId", registrationId));
                  criteria.add(Restrictions.eq("orderType", "TR_UPDATE"));
                  
                  List<TechnicalOrder> technicalOrdersUpdate=(List<TechnicalOrder>)criteria.list();
                  technicalOrders.addAll(technicalOrdersUpdate);
              }
              for(TechnicalOrder order: technicalOrders )
              {
            	  
            	  
            	  Criteria cri =  getSessionForGRT().createCriteria(TechnicalRegistration.class).createAlias("technicalOrder", "technicalOrder");
                  cri.add(Restrictions.eq("technicalOrder.orderId", order.getOrderId()));
                  List<TechnicalRegistration> techniList=(List<TechnicalRegistration>)cri.list();
                  
                 TechnicalRegistrationSummary registrationSummaries=new TechnicalRegistrationSummary();
                 for(TechnicalRegistration techReg: techniList){
                
		                 registrationSummaries.setTechnicalRegistrationId(techReg.getTechnicalRegistrationId());
		                 registrationSummaries.setRegistrationId(order.getSiteRegistration().getRegistrationId());
		                 registrationSummaries.setOrderId(order.getOrderId());
		                 registrationSummaries.setMaterialCode(order.getMaterialCode());
		                 registrationSummaries.setMaterialCodeDescription(order.getDescription());
		                 registrationSummaries.setSolutionElement(order.getEquipmentNumber());
		                 //registrationSummaries.setCreatedDate(techReg.getCreatedDate());
		                 registrationSummaries.setInitialQty(order.getInitialQuantity());
		                 registrationSummaries.setRemainingQty(order.getRemainingQuantity());	
		                 //registrationSummaries.setProcessStep(techReg.getpr);	
		                 //registrationSummaries.setStatusId(techReg.getStatus());
		                 registrationSummaries.setSoftwareRelease(techReg.getSoftwareRelease());
		                 registrationSummaries.setSid(order.getSid());
		                 registrationSummaries.setMid(order.getMid());
		                 registrationSummaries.setAccessType(techReg.getAccessType());
		                 registrationSummaries.setConnectivity(techReg.getConnectivity());
		                 registrationSummaries.setAccessTypes(techReg.getAccessType());
		                 if(GRTConstants.NO_CONNECTIVITY.equalsIgnoreCase(techReg.getAccessType())|| techReg.getAccessType()==null)
		                 {
		                	 registrationSummaries.setConnectivity(GRTConstants.NO);
		                	 registrationSummaries.setPopUpHiddenValue(techReg.getAccessType());
		                	 if(techReg.getAccessType()==null){
		                		 registrationSummaries.setPopUpHiddenValue(GRTConstants.NO_CONNECTIVITY);
		                		 registrationSummaries.setAccessTypes(" ");
		                	 }
		                 }
		                 
		                 registrationSummaries.setDialInNumber(techReg.getDialInNumber());
		                
		                 registrationSummaries.setSerialNo(order.getSerialNumber());
		             	//status;
		                 registrationSummaries.setTroubleShootId(techReg.getTroubleShootURL());
		             	//version;
		                 //registrationSummaries.setRequesterName(order.getr);
		                 //registrationSummaries.setBaseUnit(order.getb);
		                 if(order.getIsIPOEligible()!=null){
		                	 boolean ipo=Boolean.parseBoolean(order.getIsIPOEligible());
		                	 registrationSummaries.setIPOEligible(ipo);
		                 }
		                 
		             	
		                 registrationSummaries.setAlarmOrigination(techReg.getAlarmOrigination());
		             	
		             	
		                // registrationSummaries.setInstallBaseStatusId(techReg.geti);
		               //  registrationSummaries.setTechRegStatusId(techRegStatusId);
		             	//finalValidationStatusId;
		                 registrationSummaries.setProductLine(order.getProductLine());
		                 registrationSummaries.setOrderType(order.getOrderType());
		                 registrationSummaries.setSeId(order.getSeid());
		                 registrationSummaries.setProductTemplate(techReg.getTemplate());
		                 registrationSummaries.setAction(techReg.getAction());
		                 registrationSummaries.setGroupId(techReg.getGroupId());
		                 registrationSummaries.setSalSeIdPrimarySecondary(order.getSalSeIdPrimarySecondary());
		                 registrationSummaries.setSoftwareRelease(techReg.getSoftwareRelease()); 
		                 registrationSummaries.setIpAddress(techReg.getIpAddress());
		                 registrationSummaries.setAuthFileID(techReg.getAuthorizationFileId());
		                 registrationSummaries.setNickName(techReg.getNickname());
		                 //registrationSummaries.setProcess(order.getpr); 
		                 registrationSummaries.setSystemProductRelease(techReg.getSystemProductRelease());
		                 registrationSummaries.setOperationType(techReg.getOperationType());
		                 //registrationSummaries.setTrConfig(order.gett);
		                 registrationSummaries.setSolutionElementId(techReg.getSolutionElementId());
		                 registrationSummaries.setConnectionDetail(techReg.getConnectionDetail());
		                 //registrationSummaries.setAddFlag(order.i);
		                 //registrationSummaries.setMainCM(order.getm); 
		                 registrationSummaries.setHardwareServer(techReg.getHardwareServer());
		                 registrationSummaries.setSeidOfVoicePortal(techReg.getSeidOfVoice()); 
		                 registrationSummaries.setPrivateIP(techReg.getPrivateIpAddress());
		                 registrationSummaries.setCheckIfSESEdge(techReg.getCheckSesEdge());
		                 registrationSummaries.setSampBoardUpgradedDb(techReg.getCheckReleaseHigher());
		                // registrationSummaries.setSampBoardUpgraded(techReg.getsa); sampBoardUpgradedDb, 
		                 registrationSummaries.setRandomPassword(techReg.getRandomPassword());
		                 //registrationSummaries.setRandomPassworddb(techReg.getr);, 
		                 registrationSummaries.setOutboundPrefix(techReg.getOutboundCallingPrefix());
		                 //registrationSummaries.setInitiateEPNSurvery(techReg.gete);
		                 registrationSummaries.setStepBStaus(techReg.getStepBStatus());
		             	//private Set<String> eligibleAccessTypes;
		                 registrationSummaries.setProductTemplate(techReg.getTemplate());
		              //   registrationSummaries.setProductType(order.getpr);, 
		                 registrationSummaries.setSolutionElement(techReg.getSolutionElement());
		                 registrationSummaries.setCmMainsoldTo(techReg.getCmMainSoldToId()); 
		                 //registrationSummaries.setCmMainMaterialCodeDesc(order.getc);,
		                 registrationSummaries.setCmMainSID(techReg.getCmMainSeid());
		                 registrationSummaries.setRemoteDeviceType(techReg.getRemoteDeviceType());;
		                 registrationSummaries.setCmProduct(techReg.isCmProduct());;
		                 registrationSummaries.setDisableUpdateFlag(techReg.isDetailButtonStatus());
		                 //registrationSummaries.setUpdateButtonTitle(techReg.getu);
		             	
		                 registrationSummaries.setPrimarySalgwSeid(techReg.getPrimarySalGWSeid());
		                 registrationSummaries.setSecondarySalgwSeid(techReg.getSecondarySalGWSeid());;
		                 registrationSummaries.setModel(techReg.getModel());
		                 registrationSummaries.setRemoteAccess(techReg.getRemoteAccess());;
		                 registrationSummaries.setTransportAlarm(techReg.getTransportAlarm());
		                 registrationSummaries.setPrimarySoldToId(techReg.getPrimarySoldToId());
		                 registrationSummaries.setSecondarySoldToId(techReg.getSecondarySoldToId());
		                 registrationSummaries.setFailedSeid(techReg.getFailedSeid());;
		                 registrationSummaries.setNumberOfSubmit(techReg.getNumberOfSubmit());
		                 registrationSummaries.setUserName(techReg.getUsername());
		                 registrationSummaries.setPassword(techReg.getPassword());
		             	
		                 ///registrationSummaries.setSeCodePreview(techReg.getse); ;
		             	/* GRT 4.0 changes */
		                 registrationSummaries.setEquipmentNumber(techReg.getEquipmentNumber());;
		                // registrationSummaries.setisSiteList;
		               //  registrationSummaries.setisTechReg;
		             	//private List<ExpandedSolutionElement> expSolutionElements = new ArrayList<ExpandedSolutionElement>();
		             	//private Set<ExpandedSolutionElement> explodedSolutionElements = new HashSet<ExpandedSolutionElement>();
		                 registrationSummaries.setDeviceStatus(techReg.getDeviceStatus());;
		                 registrationSummaries.setDeviceLastAlarmReceivedDate(techReg.getDeviceLastAlarmReceivedDate());
		                 registrationSummaries.setProcess(true);
		             	
		             	//GRT 4.0 Changes
		             	//	popUpHiddenValue;
		             	//For Retest	
		                 //registrationSummaries.setSelectForAlarming(techReg.getsel);;
		             	//	private boolean selectForRemoteAccess;
		             	//	private boolean disableRetestFlag;
		                  
		                  
		                 regSumList.add(registrationSummaries) ;
                 }
                 
            	  
              }
    		
	       	
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return regSumList;
    }
    
    public void populateExpSolutionElementsList(java.util.Set<ExpandedSolutionElement> explodedSolutionElements, TechnicalRegistration tr){
    	//Begin: To fetch the ExpandedSolutionElement
    	List<ExpandedSolutionElement> expSolElements = new ArrayList<ExpandedSolutionElement>();

    	if(explodedSolutionElements != null && explodedSolutionElements.size()>0){
	    	for(ExpandedSolutionElement expSolEle : explodedSolutionElements) {
	    		if(expSolEle.getAlarmId() == null){
	    			expSolEle.setAlarmId("");
	    		}

	    		if(expSolEle.getSeCode() == null){
	    			expSolEle.setSeCode("");
	    		}
	    		if(expSolEle.getSeID() == null){
	    			expSolEle.setSeID("");
	    		}

	    		if(expSolEle.getIpAddress() == null){
	    			expSolEle.setIpAddress("");
	    		}

	    		expSolEle.getTechnicalRegistration();
	    		if(tr.getSid() != null){
	    			expSolEle.setSid(tr.getSid());
	    		}else{
	    			expSolEle.setSid("");
	    		}
	    		if(tr.getMid() != null){
	    			expSolEle.setMid(tr.getMid());
	    		}else{
	    			expSolEle.setMid("");
	    		}

				expSolElements.add(expSolEle);

	    	}
    	}
    	tr.setExpSolutionElements(expSolElements);
    	//End: To fetch the ExpandedSolutionElement
    }
    
    /**
     * Save the technical Registration for ART.
     *
     * @param technicalRegistration TechnicalRegistration
     */
    public TechnicalRegistration saveTechnicalRegistration(TechnicalRegistration technicalRegistrationToPersist) {
    	Session session = getSessionForGRT();
    	TechnicalRegistration technicalRegistration = null;
        try {
            session.beginTransaction();
            Criteria criteria =  session.createCriteria(TechnicalRegistration.class);
            criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistrationToPersist.getTechnicalRegistrationId()));
            technicalRegistration = (TechnicalRegistration)  criteria.uniqueResult();

          	technicalRegistration.setArtSrNo(technicalRegistrationToPersist.getArtSrNo());
          	technicalRegistration.setErrorCode(technicalRegistrationToPersist.getErrorCode());
          	technicalRegistration.setSubErrorCode(technicalRegistrationToPersist.getSubErrorCode());
          	technicalRegistration.setErrorDesc(technicalRegistrationToPersist.getErrorDesc());
          	technicalRegistration.setFailedSeid(technicalRegistrationToPersist.getFailedSeid());
          	technicalRegistration.setArtId(technicalRegistrationToPersist.getArtId());
          	if(technicalRegistrationToPersist.getStatus() != null) {
                criteria =  session.createCriteria(Status.class);
                criteria.add(Restrictions.eq("statusId", technicalRegistrationToPersist.getStatus().getStatusId()));
                Status s = (Status)  criteria.uniqueResult();
            	technicalRegistration.setStatus(s);
            }
          	//Defect 440 : update the stepb status
          	if(technicalRegistrationToPersist.getStepBStatus() != null) {
                criteria =  session.createCriteria(Status.class);
                criteria.add(Restrictions.eq("statusId", technicalRegistrationToPersist.getStepBStatus().getStatusId()));
                Status s = (Status)  criteria.uniqueResult();
            	technicalRegistration.setStepBStatus(s);
            	if( technicalRegistrationToPersist.getStepBSRRequest()!=null ){
            		technicalRegistration.setStepBSRRequest(technicalRegistrationToPersist.getStepBSRRequest());
            	}
            	
            	//Prod Defect - Update the completed date
            	if( technicalRegistrationToPersist.getStepBCompletedDate() !=null ){
            		technicalRegistration.setStepBCompletedDate(technicalRegistrationToPersist.getStepBCompletedDate());
            	}
            }
          	technicalRegistration.setGroupId(technicalRegistrationToPersist.getGroupId());
            technicalRegistration.setSolutionElementId(technicalRegistrationToPersist.getSolutionElementId());
            technicalRegistration.setNumberOfSubmit(technicalRegistrationToPersist.getNumberOfSubmit());
            technicalRegistration.setOnboarding(technicalRegistrationToPersist.getOnboarding());
            technicalRegistration.setAlarmId(technicalRegistrationToPersist.getAlarmId());
            technicalRegistration.setProductId(technicalRegistrationToPersist.getProductId());
            technicalRegistration.setInstallScript(technicalRegistrationToPersist.getInstallScript());
            technicalRegistration.setSid(technicalRegistrationToPersist.getSid());
            technicalRegistration.setMid(technicalRegistrationToPersist.getMid());
            technicalRegistration.setTransactionDetails(technicalRegistrationToPersist.getTransactionDetails());
            technicalRegistration.setArtId(technicalRegistrationToPersist.getArtId());

            technicalRegistration.setSummaryEquipmentNumber(technicalRegistrationToPersist.getSummaryEquipmentNumber());
            technicalRegistration.setEquipmentNumber(technicalRegistrationToPersist.getEquipmentNumber());
            if(technicalRegistrationToPersist.getSrRequest()!=null){
            	technicalRegistration.setSrRequest(technicalRegistrationToPersist.getSrRequest());
            	technicalRegistration.setArtSrNo(technicalRegistrationToPersist.getArtSrNo());
            }
            if(StringUtils.isNotEmpty(technicalRegistrationToPersist.getArtCreatedSrNo())) {
            	technicalRegistration.setArtCreatedSrNo(technicalRegistrationToPersist.getArtCreatedSrNo());
            }
            if(technicalRegistrationToPersist.getTechnicalOrder() != null) {
            	technicalRegistration.getTechnicalOrder().setRemainingQuantity(technicalRegistrationToPersist.getTechnicalOrder().getRemainingQuantity());
            }
            if(StringUtils.isNotEmpty(technicalRegistrationToPersist.getIpoUserEmail())) {
            	technicalRegistration.setIpoUserEmail(technicalRegistrationToPersist.getIpoUserEmail());
            }

            if(technicalRegistrationToPersist.getStepACompletedDate() != null){
            	technicalRegistration.setStepACompletedDate(technicalRegistrationToPersist.getStepACompletedDate());
            }
            
            session.saveOrUpdate(technicalRegistration);
            session.getTransaction().commit();
            //added to enter status for exploded elements Defect #635
            saveExploadedElements(technicalRegistrationToPersist);
        } catch (Exception ex){
        	//session.getTransaction().rollback();
            logger.error("ERROR: Unexpected failure when updating Technical Registration to DB!\n"
                    + "Original error: " +  ex.getMessage() + "\n"
                    + "Technical Registration ID [" + technicalRegistrationToPersist.getTechnicalRegistrationId() + "]");
            ex.printStackTrace();
            logger.error("", ex);
        }
        return technicalRegistration;
    }
    
    /**
     * Save Exploded elements ART response to database.
     * @param technicalRegistrationToPersist
     */
    private void saveExploadedElements(TechnicalRegistration technicalRegistrationToPersist) {
    	logger.info("Inside BaseHibernateDao.saveExploadedElements");
    	if(technicalRegistrationToPersist.getExplodedSolutionElements() != null && technicalRegistrationToPersist.getExplodedSolutionElements().size() > 0) {
    		Session session = getSessionForGRT();
    		Transaction tx = session.beginTransaction();
    		for(ExpandedSolutionElement expElement : technicalRegistrationToPersist.getExplodedSolutionElements()) {
    			try {
    				logger.debug("Updating Expanded Solution Element Id record: "+expElement.getExpSolnElemntId());
    				logger.debug("Values to update - Retest Status - "+expElement.getRetestStatus()+", Art Response Code - "+expElement.getArtRespCode() + ", Art Resp Msg - "+expElement.getArtRespMsg());
    				Criteria criteria = session.createCriteria(ExpandedSolutionElement.class);
	    			criteria.add(Restrictions.eq("expSolnElemntId", expElement.getExpSolnElemntId()));
	    			ExpandedSolutionElement element = (ExpandedSolutionElement)  criteria.uniqueResult();
	    			element.setRetestStatus(expElement.getRetestStatus());
	    			element.setArtRespCode(expElement.getArtRespCode());
	    			if(expElement.getArtRespMsg() != null && expElement.getArtRespMsg().length() > 200) {
	    				element.setArtRespMsg(expElement.getArtRespMsg().substring(0, 199));
	    			} else {
	    				element.setArtRespMsg(expElement.getArtRespMsg());
	    			}
	    			session.saveOrUpdate(element);
    			} catch(Exception e) {
    				logger.error("Exception during updating Expanded solution element "+e);
    			}
    		}
    		tx.commit();
    	}
    	logger.info("Exiting BaseHibernateDao.saveExploadedElements");
    }
    
    /**
	 * Save the technical Registration for ART.
	 *
	 * @param technicalRegistration TechnicalRegistration
	 * @throws DataAccessException
	 */
	public String saveSalSiteListOnly(SiteList siteList) throws DataAccessException {
		try {
			Session session = getSessionForGRT();
			session.beginTransaction();
			
	        session.saveOrUpdate(siteList);
	      
	        session.getTransaction().commit();
	        	        
		} catch (HibernateException hibEx) {
			logger.error("Error in BaseHibernateDao.saveTechnicalRegistrationForSALGateway : "
							+ hibEx.getMessage());
			throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
		}

		return siteList.getId();
	}
    
    /**
   	 * Method to save the Site List.
   	 *
   	 * @param siteList
   	 * @return salSiteListId Long
   	 * @throws DataAccessException
   	 *             Custom Exception
   	 */
   	public void saveSalSiteList(SiteList siteList)
   			throws DataAccessException {
   		logger.debug("Entering BaseHibernateDao.saveSiteList:" + siteList.getId());

   		SiteList siteListLocal = null;
   		try {
   			Session session = getSessionForGRT();
   			Transaction transaction = session.beginTransaction();
   			Criteria criteria =  session.createCriteria(SiteList.class);
   			criteria.add(Restrictions.eq("id", siteList.getId()));
   			siteListLocal = (SiteList)  criteria.uniqueResult();
   			if(StringUtils.isNotEmpty(siteList.getErrorCode()) && !"0".equals(siteList.getErrorCode())) {
   				if (StringUtils.isNotEmpty(siteList.getArtSrNo())){
   					siteListLocal.setArtSrNo(siteList.getArtSrNo());
   				}
   				if (StringUtils.isNotEmpty(siteList.getErrorCode())){
   					siteListLocal.setErrorCode(siteList.getErrorCode());
   				}
   				if (StringUtils.isNotEmpty(siteList.getErrorDesc())){
   					siteListLocal.setErrorDesc(siteList.getErrorDesc());
   				}
   				if (StringUtils.isNotEmpty(siteList.getSubErrorCode())){
   					siteListLocal.setSubErrorCode(siteList.getSubErrorCode());
   				}
   				if (StringUtils.isNotEmpty(siteList.getInstallScript())){
   					siteListLocal.setInstallScript(siteList.getInstallScript());
   				}
   				if (StringUtils.isNotEmpty(siteList.getNumberOfSubmit())){
   					siteListLocal.setNumberOfSubmit(siteList.getNumberOfSubmit());
   				}
   			}
   			if (siteList.getStatus()  != null) {
   	            siteListLocal.setStatus(siteList.getStatus());
   			}
   			 if(siteList.getSrRequest()!= null){
   				 siteListLocal.setSrRequest(siteList.getSrRequest());
                }
                if(siteList.getStepACompletedDate() != null){
               	 siteListLocal.setStepACompletedDate(siteList.getStepACompletedDate());
                }
                if(siteList.getArtId() != null){
               	 siteListLocal.setArtId(siteList.getArtId());
                }
                
                //Defect 785 : update the stepb status
              	if(siteList.getStepBStatus() != null) {
                    criteria =  session.createCriteria(Status.class);
                    criteria.add(Restrictions.eq("statusId", siteList.getStepBStatus().getStatusId()));
                    Status s = (Status)  criteria.uniqueResult();
                    siteListLocal.setStepBStatus(s);
                	if( siteList.getStepBSRRequest()!=null ){
                		siteListLocal.setStepBSRRequest(siteList.getStepBSRRequest());
                	}
                	
                	//Prod Defect - Update the completed date
                	if( siteList.getStepBCompletedDate() !=null ){
                		siteListLocal.setStepBCompletedDate(siteList.getStepBCompletedDate());
                	}
                }
                
   			//saveOrUpdate(siteListLocal);
   			session.merge(siteListLocal);
   			transaction.commit();
   			
   			//added to enter status for exploded elements Defect #635
   			saveExploadedElementsForSL(siteList);
   		} catch (HibernateException hibEx) {
   			logger.error(hibEx);
   			throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
   		} catch (Exception ex) {
           	logger.error(ex);
   			throw new DataAccessException(BaseHibernateDao.class, ex.getMessage(), ex);
   		} finally {
   			logger.debug("Exiting BaseHibernateDao.saveSiteList");
   		}
   	}
   	

    
    
    
    
    
    
    public SiteList getSiteList(String siteListId) throws DataAccessException {
        logger.debug("Entering BaseHibernateDao.getSiteList()");
        SiteList siteList = null;
        try {
            Criteria criteria =  getSessionForGRT().createCriteria(SiteList.class);
            criteria.add(Restrictions.eq("id", siteListId));
            siteList = (SiteList)  criteria.uniqueResult();
        } catch (HibernateException hibEx) {
        	logger.error("", hibEx);
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting BaseHibernateDao.getSiteList()");

        return siteList;

    }
    
    /**
	 * delete the hibernate object.
	 * 
	 * @param query
	 *            SQLQuery
	 * @throws DataAccessException
	 *             custom exception
	 */
    protected final void deleteSQLQuery(Query query) throws DataAccessException {
		Session session = getSessionForGRT();
		try {
			session.beginTransaction();
			query.executeUpdate();
			session.beginTransaction().commit();
		} catch (HibernateException hibEx) {
			session.beginTransaction().rollback();
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			session.beginTransaction().rollback();
			throw new DataAccessException(BaseHibernateDao.class, ex
					.getMessage(), ex);
		}

	}

	/**
	    * Update SiteRegistration Status with the given status
	    *
	    * @param siteRegistration
	    * @param status
	    * @return siteRegistration SiteRegistration
	    * @throws DataAccessException
	    */
	   public TechnicalRegistration updateTechnicalRegistrationStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	       logger.debug("Entering BaseHibernateDao.updateTechnicalRegistrationStatus()");
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
	           resultObject.setStatus(s);
	           session.saveOrUpdate(resultObject);
	           session.getTransaction().commit();

	       } catch (HibernateException hibEx) {
	           throw new DataAccessException(BaseHibernateDao.class, hibEx
	                   .getMessage(), hibEx);
	       }
	       logger.debug("Exiting BaseHibernateDao.updateTechnicalRegistrationStatus()");

	       return resultObject;
	   }
	   
	   /**
		 * Method to get the SoldToId access for a given bpLinkId from CAT/LOA/ESR database.
		 * @param BPLinkId Mandatory
		 * @param soldtoId optional
		 * @return List<BPAccountTempAccess> List of bean populated with BPLinkid, SoldToIs and SoldToName
		 * @throws DataAccessException
		 */
		public List<BPAccountTempAccess> queryAccess(String bpLinkId, String soldToId) throws DataAccessException {
			if(logger.isDebugEnabled())
			logger.debug("Entering queryAccess : bpLinkId:" + bpLinkId + " soldToId:" + soldToId);
			Query query=null;
			List<BPAccountTempAccess> access = new ArrayList<BPAccountTempAccess>();
			/*final String queryStr_SOLD_TO_NOT_EMPTY = "SELECT SOLD_TO, SOLD_TO_NAME FROM " + getCatSchema()        
				    +".APPROVED_PERMISSIONS WHERE BP_LINK_ID = :bpLinkId " 
					+ "AND SOLD_TO= :soldToId AND PERMISSION_TYPE = 'Registration' AND PERMISSION_STATUS='Active'";
				    
				    final String queryStr_SOLD_TO_EMPTY = "SELECT SOLD_TO, SOLD_TO_NAME FROM " + getCatSchema() +".APPROVED_PERMISSIONS WHERE BP_LINK_ID = :bpLinkId " 
					+ " AND PERMISSION_TYPE = 'Registration' AND PERMISSION_STATUS='Active'";*/
			try {
				if(StringUtils.isNotEmpty(bpLinkId)) {
					String queryStr = null;
					if(StringUtils.isNotEmpty(soldToId)) {
						 query=getSessionForCAT().getNamedQuery("getSoldToNotEmpty");
						//queryStr = queryStr_SOLD_TO_NOT_EMPTY;
					} else {
						 query=getSessionForCAT().getNamedQuery("getSoldToEmpty");
						//queryStr = queryStr_SOLD_TO_EMPTY;
					}
					//SQLQuery query =  getSession(GRTConstants.CAT_DATA_BASE).createSQLQuery(queryStr);
					
					query.setString("bpLinkId", bpLinkId);
					if(StringUtils.isNotEmpty(soldToId)) {
						query.setString("soldToId", soldToId);
					}
					//query.addScalar("SOLD_TO", Hibernate.STRING);
					//query.addScalar("SOLD_TO_NAME", Hibernate.STRING);
					if(StringUtils.isNotEmpty(bpLinkId) && StringUtils.isEmpty(soldToId)) {
						query.setFetchSize(500);
					}
					List<Object[]> list = query.list();
					if(list != null && list.size() > 0) {
						for (Object[] data : list) {
							if(data != null){
								BPAccountTempAccess bpAccountTempAccess = new BPAccountTempAccess();
								bpAccountTempAccess.setBpLinkId(bpLinkId);
								bpAccountTempAccess.setAccountId((String)data[0]);
								if(data[1] instanceof String) {
									bpAccountTempAccess.setAccountName((String)data[1]);
								}
								access.add(bpAccountTempAccess);
							}
						}
						
					}
				}
			} catch (HibernateException hibEx) {
				throw new DataAccessException(BaseHibernateDao.class, hibEx
						.getMessage(), hibEx);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new DataAccessException(BaseHibernateDao.class, ex
						.getMessage(), ex);
			} finally {
				
				logger.debug("Exiting queryAccess : bpLinkId:" + bpLinkId + " soldToId:" + soldToId);
			}
			return access;
		}
		
		public boolean isSoldToExistForTheUser(String soldTo, String userId) throws DataAccessException {
			logger.debug("Entering BaseHibernateDao.isSoldToExistForTheUser");
			boolean isExist = false;
			try {
				/*String queryStr = "select count(*) from "
					+ GRTPropertiesUtil.getProperty(GRTConstants.CXP_USER_NAME)
							.trim()
					+ ".v_css_user_access_list where account_number = :soldTo and user_logon = :user_logon";
				SQLQuery query =  getSession(GRTConstants.CXP_DATA_BASE).createSQLQuery(queryStr);*/
				Query query=getSessionForCXP().getNamedQuery("getSoldToExistForTheUser");
				query.setString("soldTo", soldTo);
				query.setString("user_logon", userId);
				isExist = !"0".equals(query.uniqueResult().toString());
			} catch (HibernateException hibEx) {
				throw new DataAccessException(BaseHibernateDao.class, hibEx
						.getMessage(), hibEx);
			} catch (Exception ex) {
				throw new DataAccessException(BaseHibernateDao.class, ex
						.getMessage(), ex);
			} 
			logger.debug("Exiting BaseHibernateDao.isSoldToExistForTheUser");
			
			return isExist;
		}
		
		public boolean isBpSoldToExistForTheUser(String soldTo, String bpLinkId) throws DataAccessException {
			logger.debug("Entering BaseHibernateDao.isBpSoldToExistForTheUser SoldTo:"+soldTo+"   BPLinkId:"+bpLinkId);
			boolean isExist = false;
			try {
				if(StringUtils.isNotEmpty(bpLinkId)) {
					bpLinkId = this.getValidBpLinkId(bpLinkId);
				}
				/*String queryStr = "select count(*) from SALES_OUT where SOLD_TO_ID = :soldTo and BP_LINK_ID = :bpLinkId";
				SQLQuery query =  getSession().createSQLQuery(queryStr);
				query.setString("soldTo", soldTo);
				query.setString("bpLinkId", bpLinkId);*/
				Query query=getSessionForGRT().getNamedQuery("getBpSoldToExistForTheUser");
				query.setString("soldTo", soldTo);
				query.setString("bpLinkId", bpLinkId);
				isExist = !"0".equals(query.uniqueResult().toString());
			} catch (HibernateException hibEx) {
				throw new DataAccessException(BaseHibernateDao.class, hibEx
						.getMessage(), hibEx);
			} catch (Exception ex) {
				throw new DataAccessException(BaseHibernateDao.class, ex
						.getMessage(), ex);
			}
			logger.debug("Exiting BaseHibernateDao.isBpSoldToExistForTheUser isExist:"+isExist);

			return isExist;
		}
		protected String getValidBpLinkId(String bpLinkId) {
			String modifiedBpLinkId = null;
			if(StringUtils.isNotEmpty(bpLinkId)){
				if(bpLinkId.length() == 10){
					modifiedBpLinkId = bpLinkId;
				} else {
					int lenthOfString = bpLinkId.length();
					if(lenthOfString < 10){
						int numOfZeros = 10 - lenthOfString;
						StringBuilder sb = new StringBuilder();
						for(int i=0; i<numOfZeros; i++){
							sb.append("0");
						}
						sb.append(bpLinkId);
						modifiedBpLinkId = sb.toString();
					} else {
						//TODO
					}
				}
			} else{
				modifiedBpLinkId = bpLinkId;
			}
			return modifiedBpLinkId;
	}
		
		 public int saveSiteRegistrationDetails(SiteRegistration siteRegistration) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.saveSiteRegistrationDetails");
		        int flag = 0;
		        SiteRegistration registration = null;
		        Session session=null;
		        try {
		        	 logger.debug("Defect #335- Inside Try");
		         	 session = getSessionForGRT();
		             session.beginTransaction();
		           // registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());
					//session.refresh(registration);
		            handleStatuses(siteRegistration);
		            logger.debug("Save Site - "+siteRegistration.getSoldToId());
		            session.saveOrUpdate(siteRegistration);
		            Long siteRegistrationId = new Long(siteRegistration.getRegistrationId());
		            logger.debug("In BaseHibernateDao.siteRegistrationId****************"+siteRegistrationId);
		            //getTransaction().commit();
		            session.getTransaction().commit();
		            logger.debug("Exiting BaseHibernateDao.saveSiteRegistration(SiteRegistration siteRegistration)");
		            flag = 1;
		        } catch (HibernateException hibEx) {
		        	// hibEx.printStackTrace();
		        	 logger.debug("Defect #335- Inside Catch - "+hibEx.getMessage());
		        	 session.close();
	        		 session = getSessionForGRT();	 
	        		 session.beginTransaction();
	        		 logger.debug("Save Site - "+siteRegistration.getSoldToId());
	        		 handleStatuses(siteRegistration);
	        		 session.merge(siteRegistration);
	        		 Long siteRegistrationId = new Long(siteRegistration.getRegistrationId());
	        		 logger.debug("In BaseHibernateDao.siteRegistrationId****************"+siteRegistrationId);
	            	
	        		 session.getTransaction().commit();
	        		 logger.debug("Exiting BaseHibernateDao.saveSiteRegistration(SiteRegistration siteRegistration)");
	        		 flag = 1;
		            
		        }
		        catch (Exception ex) {
		        	logger.error(ex);
		            getSessionForGRT().getTransaction().rollback();
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        }
		        return flag;
		    }
		 
		 protected void handleStatuses(SiteRegistration siteRegistration){
	     	if(siteRegistration != null){

	     		if(siteRegistration.getInstallBaseStatus()==null){
	     			Status status = new Status();
	         		status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
	         		siteRegistration.setInstallBaseStatus(status);
	     		}

	     		if(siteRegistration.getTechRegStatus()==null){
	     			Status status = new Status();
	         		status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
	         		siteRegistration.setTechRegStatus(status);
	     		}

	     		if(siteRegistration.getFinalValidationStatus()==null){
	     			Status status = new Status();
	         		status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
	         		siteRegistration.setFinalValidationStatus(status);
	     		}
	     	}
	     }
		 public List<PipelineSapTransactions> fetchPipelineSapTransactionOnSoldTo(String soldTo) throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao.fetchPipelineSapTransactionOnSoldTo :" + soldTo);
		    	List<PipelineSapTransactions> pipelineSapTransactionsList = new ArrayList<PipelineSapTransactions>();
		    	try {
		    		/*StringBuffer sb = new StringBuffer();
		    		sb.append("select material_code, equipment_number, sum(quantity) quantity, action, serial_number, technically_registerable from pipeline_sap_transactions ")
		    			.append("where processed = '0' and action IN ('IB','FV') and ship_to = :soldToParam ")
		    			.append("group by material_code, serial_number, technically_registerable, equipment_number, action order by action desc");*/
		            Query query = getSessionForGRT().getNamedQuery("fetchPipelineSapTransactionOnSoldTo");
		            logger.debug("Query: "+query.toString());
		            query.setParameter("soldToParam", soldTo);
		            List queryResult = query.list();
		            PipelineSapTransactions pipelineSapTransactions = null;
		            for (int i = 0; (queryResult != null) && (i < queryResult.size()); i++) {
		                Object[] listItem = (Object[]) queryResult.get(i);
		                pipelineSapTransactions = new PipelineSapTransactions();
		                pipelineSapTransactions.setMaterialCode(listItem[0]!=null ? listItem[0].toString():"");
		                pipelineSapTransactions.setEquipmentNumber(listItem[1]!=null ? listItem[1].toString():"");
		                /*String trFlag = listItem[2] != null ? listItem[2].toString():"0";
		                if(trFlag.equals("1")){
		                	pipelineSapTransactions.setTechnicallyRegisterable(true);
		                }*/
		                pipelineSapTransactions.setQuantity(Long.parseLong(listItem[2]!=null ? listItem[2].toString():"0"));
		                pipelineSapTransactions.setAction(listItem[3]!=null ? listItem[3].toString():"");
		                pipelineSapTransactions.setSerialNumber(listItem[4]!=null ? listItem[4].toString():"");
		                pipelineSapTransactions.setTechnicallyRegisterable((listItem[5]!=null && listItem[5].toString().equalsIgnoreCase("0"))? true:false);
		                pipelineSapTransactionsList.add(pipelineSapTransactions);
		            }
		        } catch (HibernateException hibEx) {
		        	logger.error("", hibEx);
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
		    	logger.debug("Exiting BaseHibernateDao.fetchPipelineSapTransactionOnSoldTo");
		    	return pipelineSapTransactionsList;
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
		        logger.debug("Entering BaseHibernateDao.getSuperUser");
		        try {
		        	if (userId != null) {
						superUser = (SuperUser) getSessionForGRT().get(SuperUser.class,
								userId);
					}
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        } catch (Exception ex) {
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        }
		        logger.debug("Entering BaseHibernateDao.getSuperUser");

		        return superUser;
		    }
		    
		    /**
			 * Check given soldTo exist in Excluded SoldTo table
			 * @return
			 * @throws DataAccessException
			 */
			public boolean isSoldToExcluded(String soldTo) throws DataAccessException {
				logger.debug("Entering BaseHibernateDao.isSoldToExcluded");
				boolean flag = false;
				try {
					//Query query = getSessionForGRT().createSQLQuery("SELECT COUNT(*) FROM EXCLUDED_SOLD_TO_LIST WHERE SOLD_TO_ID =:soldToId");
					Query query = getSessionForGRT().getNamedQuery("getIsSoldToExcluded");
					query.setString("soldToId", soldTo);
					flag = !"0".equals(query.uniqueResult().toString());
				} catch (HibernateException hibEx) {
					//getSessionForGRT().getTransaction().rollback();
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					getSessionForGRT().getTransaction().rollback();
					throw new DataAccessException(BaseHibernateDao.class, ex
							.getMessage(), ex);
				}
				logger.debug("Entering BaseHibernateDao.isSoldToExcluded");
				return flag;
			}
			
			/**
		     *
		     * Method to get the registrattinId from sequence
		     *
		     * @param registrationId
		     *              string
		     *
		     */
		    public String getRegistrationId()
		    {
		        String registrationId=null;

		        try {
		            Query query= getSessionForGRT().createSQLQuery("select grtsequence.nextval from dual");
		            registrationId = query.uniqueResult().toString();

		        }catch(Exception ex)
		        {
		          logger.error("Error in  BaseHibernateDao: getRegistrationId:"
		                + ex.getMessage());
		                 }

		        return registrationId;
		}
		    /**
		     * Method to get the registration detail from CXP DB for the given sold to id.
		     *
		     * @param accountNumber String
		     * @return RegistrationList
		     */
		    public List getRegistrationDetailFromCXP(String accountNumber) throws DataAccessException{
		        List resultSetList = null;
		        logger.debug("Entering BaseHibernateDao.getRegistrationDetailFromCXP");
		        try {
		        	/*String sqlQuery = "select distinct name from "
							+ GRTPropertiesUtil.getProperty(GRTConstants.CXP_USER_NAME)
									.trim()
							+ ".customer "
							+ "where trim(account_number) = :accountNumber";
		        	logger.debug("getRegistrationDetailFromCXP : the query print : \n"+sqlQuery);
		            SQLQuery query = getSession(GRTConstants.CXP_DATA_BASE).createSQLQuery(sqlQuery);*/
		        	Query query=getSessionForCXP().getNamedQuery("getCustomerName");
		            query.setString("accountNumber", accountNumber);
		            resultSetList = query.list();
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        } catch (Exception ex) {
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        }

		        logger.debug("Exiting BaseHibernateDao.getRegistrationDetailFromCXP");

		        return resultSetList;
		    }
		    
		    public List getRegistrationDetailFromSalesOut(String accountNumber) throws DataAccessException{
		        List resultSetList = null;
		        logger.debug("Entering BaseHibernateDao.getRegistrationDetailFromSalesOut");
		        try {
		        	//String sqlQuery = "select distinct COMPANY_NAME from SALES_OUT where SOLD_TO_ID = :accountNumber";
		            Query query = getSessionForGRT().getNamedQuery("getRegistrationDetailFromSalesOut");
		            query.setString("accountNumber", accountNumber);
		            resultSetList = query.list();
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        } catch (Exception ex) {
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        }

		        logger.debug("Exiting BaseHibernateDao.getRegistrationDetailFromSalesOut");

		        return resultSetList;
		    }
		    
		    /**
		     * An API to get List of accounts which are temporarily available to passed BPLinkId.
		     *
		     * @param bpLinkId
		     * @return
		     * @throws DataAccessException
		     */
		    public List<BPAccountTempAccess> getBPAccountTempAccess(String bpLinkId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.getBPAccountTempAccess for BPLinkId:" + bpLinkId);
		        List<BPAccountTempAccess> accounts = null;
		        try {
		            Session session = getSessionForGRT();
		            Criteria criteria =  session.createCriteria(BPAccountTempAccess.class);
		            criteria.add(Restrictions.eq("bpLinkId", bpLinkId));
		            criteria.add(Restrictions.gt("expiryDate", new Date()));
		            accounts = criteria.list();

		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
		        } finally {
		        	logger.debug("Exiting BaseHibernateDao.getBPAccountTempAccess for BPLinkId:" + bpLinkId);
		        }
		        return accounts;
		    }
		    
		    //GRT 4.0 change
		    
		    public void updateTechnicalRegistrationBySEID(String seid) throws DataAccessException {
		    	logger.debug("Entering TechnicalRegistrationDao.undoStepBForTechnicalRegistration()");
		    	Query sqlQuery = null;
		    	Transaction transaction = null;
		    	try {
			    	
		    		transaction = getSessionForGRT().beginTransaction();
			    
			    	sqlQuery = getSessionForGRT().getNamedQuery("updateTechnicalRegistrationBySEID");
			        sqlQuery.setString("SEID", seid);
			        sqlQuery.executeUpdate();
			        transaction.commit();
			    } catch (HibernateException hibEx) {
					logger.error("", hibEx);
					transaction.rollback();
					throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx.getMessage(), hibEx);
				} catch (Throwable throwable) {
					logger.error("", throwable);
					transaction.rollback();
					throw new DataAccessException(TechnicalRegistrationSRDao.class, throwable.getMessage(), throwable);
				} finally {
					logger.debug("Exiting TechnicalRegistrationDao.undoStepBForTechnicalRegistration()");
				}
		    }
		    
		   
		    
		    public List<SoldTo> getSoldToObjectList(String userName) throws DataAccessException {
				logger.debug("Entering BaseHibernateDao.getSoldToObjectList");
				List<SoldTo> soldToObjectList = new LinkedList<SoldTo>();
				Query query =null;
				try {
						if (userName != null && userName.length() > 0) {
								query = getSessionForCXP().getNamedQuery("getSoldToObjectList1");
								query.setString("user_logon", userName);
							}else{
								query = getSessionForCXP().getNamedQuery("getSoldToObjectList2");
							}

					List<Object[]> list = query.list();
					for (Object[] object : list) {
						soldToObjectList.add(new SoldTo(object));
					}
				} catch (HibernateException hibEx) {
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new DataAccessException(BaseHibernateDao.class, ex
							.getMessage(), ex);
				} 
				logger.debug("Exiting BaseHibernateDao.getSoldToObjectList");
				
				return soldToObjectList;
			}
		    public List<SoldTo> getSoldToObjectListFromSalesOut(String bpLinkId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.getSoldToObjectListFromSalesOut for BPLinkId:" + bpLinkId);
		        List<SoldTo> soldToObjectList = new LinkedList<SoldTo>();
		        try {
		        	if(StringUtils.isNotEmpty(bpLinkId)) {
		        		bpLinkId = this.getValidBpLinkId(bpLinkId);
		        	}
		        	//String queryStr = "select distinct SOLD_TO_ID, COMPANY_NAME from SALES_OUT where BP_LINK_ID = :bpLinkId order by SOLD_TO_ID";
		        	//SQLQuery query =  getSession().createSQLQuery(queryStr);
		        	Query query=getSessionForGRT().getNamedQuery("getSoldToObjectListFromSalesOut");
		            query.setString("bpLinkId", bpLinkId);
		           // query.addScalar("SOLD_TO_ID", Hibernate.STRING);
		           // query.addScalar("COMPANY_NAME", Hibernate.STRING);
		            List<Object[]> list = query.list();
					for (Object[] object : list) {
						soldToObjectList.add(new SoldTo(object));
					}
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        } catch (Exception ex) {
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        } finally {
		            logger.debug("Exiting BaseHibernateDao.getSoldToObjectListFromSalesOut for BPLinkId:" + bpLinkId);
		        }
		        return soldToObjectList;
		}
		    public void updateAccountInformation(SiteRegistration siteRegistration) throws DataAccessException {
				logger.debug("Entering BaseHibernateDao.updateAccountInformation");
				SiteRegistration resultObject = null;
				try {
					Session session = getSessionForGRT();
		            session.beginTransaction();
					Criteria criteria =  session.createCriteria(SiteRegistration.class);
		            criteria.add(Restrictions.eq("registrationId", siteRegistration.getRegistrationId()));
		            resultObject = (SiteRegistration) criteria.uniqueResult();
		            //Defect #335 : update sold to value 
		            resultObject.setSoldToId(siteRegistration.getSoldToId());
		            resultObject.setAddress(siteRegistration.getAddress());
		            resultObject.setAddress2(siteRegistration.getAddress2());
		            resultObject.setCity(siteRegistration.getCity());
		            resultObject.setState(siteRegistration.getState());
		            resultObject.setZip(siteRegistration.getZip());
		            resultObject.setSiteCountry(siteRegistration.getSiteCountry());
		            resultObject.setCompany(siteRegistration.getCompany());
		            resultObject.setCompanyPhone(siteRegistration.getCompanyPhone());
		            resultObject.setRegion(siteRegistration.getCompanyPhone());
		            
		            // Set SiteContactValidation Screen updatable data
		            resultObject.setRegistrationIdentifier(siteRegistration.getRegistrationIdentifier());
		            resultObject.setReportPhone(siteRegistration.getReportPhone());
		            resultObject.setSendMail(siteRegistration.getSendMail());
		            resultObject.setOnsiteFirstName(siteRegistration.getOnsiteFirstName());
		            resultObject.setOnsiteLastName(siteRegistration.getOnsiteLastName());
		            resultObject.setOnsitePhone(siteRegistration.getOnsitePhone());
		            resultObject.setOnsiteEmail(siteRegistration.getOnsiteEmail());
		            resultObject.setRegistrationNotes(siteRegistration.getRegistrationNotes());
		            session.saveOrUpdate(resultObject);
		            session.getTransaction().commit();
				} catch (HibernateException ejbEx) {
					logger.debug("Error in BaseHibernateDao.updateAccountInformation : " + ejbEx.getMessage());
					throw new DataAccessException(BaseHibernateDao.class, ejbEx.getMessage(), ejbEx);
				} catch (Exception ex) {
					logger.debug("Error in BaseHibernateDao.updateAccountInformation : " + ex.getMessage());
					throw new DataAccessException(BaseHibernateDao.class, ex.getMessage(), ex);
				}
				logger.debug("Exiting BaseHibernateDao.updateAccountInformation");
		    }
		    
		    /**
		     * Method to get the registration summary list.
		     *
		     * @param soldToId string
		     * @return List contains list of registration summary
		     */
		    
			public List<SiteRegistration> getRegistrationSummaryList(String[] soldToIdList) throws DataAccessException{
		        List<SiteRegistration> registrationSummaryList = null;
		        System.out.println("getRegistrationSummaryList dao------- ");
		        logger.debug("entering Method BaseHibernateDao.getRegistrationSummaryList");
		        try {
		            Criteria criteria = getSessionForGRT().createCriteria(SiteRegistration.class);
		            criteria.add(Restrictions.in("soldToId", soldToIdList));
		            registrationSummaryList = criteria.list();
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }catch (Exception hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }

		        logger.debug("exiting Method BaseHibernateDao.getRegistrationSummaryList");
		        return registrationSummaryList;
		    }
			
			/***
			 * GRT 4.0 Changes
			 * * Data for sold to sap mapping
			 * *
			 * Checking I or B
			 * @throws DataAccessException 
			 * ***/
			public boolean isIorBSoldToSAPMapping(String soldTo) throws DataAccessException
			{
				 boolean sapMappings=false;
				try{
							
			         Criteria criteria = getSessionForGRT().createCriteria(SoldToSAPMapping.class);
			         criteria.add(Restrictions.eq("soldTo", soldTo));
			         List<SoldToSAPMapping> sapMappingsList = criteria.list();
			         for(SoldToSAPMapping sm:sapMappingsList){
			        	 if(sm.getSapBox().equalsIgnoreCase("I") || sm.getSapBox().equalsIgnoreCase("B")){
			        		 sapMappings=true;
			        	 }
			        }
			        	 
					
				} catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }catch (Exception hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
				
				return sapMappings;
			}
			
			  /**
		     * Method to fetch the Equipment Removal records.
		     * 
		     * @return
		     * @throws DataAccessException
		     */
		    public String getProductLineByMaterialCode(String materialCode) throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao : getEquipmentRemovalRecords");
		    	String productLine="";
		    
		    	Query query = null;
		    	try{
			    	
		    		query=getSessionForSiebel().getNamedQuery("getProductLineByMaterialCode");
			    	query.setParameter("materialCode", materialCode);
			    	query.setFetchSize(500);
			   
			    	List<Object[]> resultSet = query.list();
			    	
			    	for (Object[] object : resultSet) {
			    	
			    		productLine=(object[1]!=null?object[1].toString():"");
			    	}
			    	
			    	
		    	 }  catch (HibernateException hibEx) {
				    	logger.debug("HibernateException: "+hibEx);
						throw new DataAccessException(BaseHibernateDao.class, hibEx
								.getMessage(), hibEx);
					} catch (Exception ex) {
						logger.debug("Exception: "+ex);
						throw new DataAccessException(BaseHibernateDao.class, ex
								.getMessage(), ex);
					}
					logger.debug("Exiting BaseHibernateDao.getEquipmentRemovalRecords");
			    	return productLine;
		    }

			
			
		    
		    /**
		     * Method to fetch the Equipment Removal records.
		     * 
		     * @return
		     * @throws DataAccessException
		     */
		    public List<TechnicalOrderDetail> getEquipmentRemovalRecords(String soldTo, String vcodes) throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao : getEquipmentRemovalRecords");
		    	List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
		    	TechnicalOrderDetail techOrder = null;
		    	StringBuilder sb = new StringBuilder();
		    	Query query = null;
		    	try{
			    	/*sb.append("select sasset.row_id as asset_pk,asset_num, sasset.x_equipment_num as equipment_num, ")
			    	// x_summary_equipment_num -> SAP equipment, ref_number_2 -> Maestro equipment, ref_number_3 -> Nortel equipment
			    	.append("NVL(sasset.x_summary_equipment_num, NVL(sasset.ref_number_2, sasset.ref_number_3)) as summary_equipment_num, ")
			    	.append("prodint.name as material_code, prodint.alias_name as material_description, ")
			    	.append("sasset.qty as existing_quantity, Decode(NVL(x.attrib_03, 'N'), 'N', '', x_seid) seid, sasset.x_se_cd se_code, ")
			    	//.append("Decode(NVL(x_seid, 'N'), 'N', '', 'Yes') TECH_REG, ") -- commented as TRed status is running on attrib_03 column of s_asset_x
			    	.append("Decode(NVL(x.attrib_03, 'N'), 'N', '', 'Yes') TECH_REG, ")
			    	.append("Decode(NVL(x_asset_entitled, 'N'),  'Y', 'Yes', DECODE(NVL(x_site_entitled, 'N'), 'Y', 'Yes', '')) ACTIVE_CONTRACT, ")
			    	.append("x_asset_entitled as asset_entitled, x_site_entitled as site_entitled, prod_ln.name product_line, ")
			    	.append("NVL(x_serial_num, serial_num) as serial_number, sasset.X_RFA_SID, sasset.X_RFA_MID, x.attrib_07 GROUP_ID ")
			    	.append("from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_prod_int prodint, siebel.s_prod_ln prod_ln, siebel.s_asset_x x ")
			    	//.append("where orgext.loc = :soldToParam and prodint.name not in (VCODE_PARAM) ")
			    	.append("where prodint.pr_prod_ln_id = prod_ln.row_id(+) and sasset.prod_id = prodint.row_id and sasset.row_id = x.par_row_id(+) ")
			    	.append("and orgext.row_id = sasset.owner_accnt_id ")
			    	.append("and NVL(sasset.x_summary_equipment_num, NVL(sasset.ref_number_2, sasset.ref_number_3)) IS NOT NULL ")
			    	.append("and sasset.qty > 0 and sasset.status_cd = 'Active' and orgext.loc = :soldToParam ") ;
			    	//.append("order by TECH_REG, equipment_num, se_code, GROUP_ID");

			    	//query = getSession().createSQLQuery(sb.toString().replace("VCODE_PARAM", vcodes));
			    	//query = getSession().createSQLQuery(sb.toString());
		*/	    	  query=getSessionForSiebel().getNamedQuery("getEquipmentRemovalRecords");
			    	query.setParameter("soldToParam", soldTo);
			    	query.setFetchSize(500);
			    //	query.setParameter("vcodes", vcodes);
			    	logger.debug("vcodes :"+vcodes);
			    	String[] vcodesArray = vcodes.replace("'", "").split(",");
			    	List<String> vcodesList = Arrays.asList(vcodesArray);
			    	
			    	//query.setParameter("vcodeParam", vcodes);
			    	List<Object[]> resultSet = query.list();
			    	// Iterate through the result set
			    	
			    	for (Object[] object : resultSet) {
			    		//logger.debug("Material Code:"+object[4].toString().trim());
			    		if(!vcodesList.contains(object[4].toString().trim())){
				    		techOrder = new TechnicalOrderDetail();
				    		techOrder.setAssetPK(object[0]!=null?object[0].toString():"");
				    		techOrder.setAssetNumber(object[1]!=null?object[1].toString():"");
				    		techOrder.setEquipmentNumber(object[2]!=null?object[2].toString():"");
				    		techOrder.setSummaryEquipmentNumber(object[3]!=null?object[3].toString():"");
				    		techOrder.setMaterialCode(object[4]!=null?object[4].toString():"");
				    		techOrder.setSerialized((object[15]!=null && StringUtils.isNotEmpty(object[15].toString()))?"Y":"N");
				    		techOrder.setDescription(object[6]!=null?object[6].toString():"");
				    		if(null != object[7])
				    			techOrder.setInitialQuantity(Long.parseLong(object[7].toString()));
				    		techOrder.setSolutionElementId(object[8]!=null?object[8].toString():"");
				    		techOrder.setSolutionElementCode(object[9]!=null?object[9].toString():"");
				    		techOrder.setTechnicallyRegisterable(object[10]!=null?object[10].toString():"");
				    		techOrder.setActiveContractExist(object[11]!=null?object[11].toString():"");
				    		techOrder.setProductLine(object[14]!=null?object[14].toString():"");
				    		techOrder.setSerialNumber(object[15]!=null?object[15].toString():"");
				    		techOrder.setSid(object[16]!=null?object[16].toString():"");
				    		techOrder.setMid(object[17]!=null?object[17].toString():"");
				    		techOrder.setGroupId(object[18]!=null?object[18].toString():"");
				    		techOrder.setNickName(object[19]!=null?object[19].toString():"");
				    		techOrder.setRegStatus(object[20]!=null?object[20].toString():"");
				    		techOrder.setOriginalSerialNumber(object[15]!=null?object[15].toString():"");
				    		
				    		techOrder.setIsSAP(object[22]!=null?object[22].toString():"");
				    		techOrder.setIsMaestro(object[21]!=null?object[21].toString():"");
				    		techOrder.setIsNortel(object[23]!=null?object[23].toString():"");
				    		
				    		todList.add(techOrder);
			    		} else {
			    			logger.debug("Filtered Code:"+object[4].toString().trim());
			    		}
			    		
			    	}
			    	logger.debug("****************************After - Query Execution **************************************"+todList.size());
			    }  catch (HibernateException hibEx) {
			    	logger.debug("HibernateException: "+hibEx);
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					logger.debug("Exception: "+ex);
					throw new DataAccessException(BaseHibernateDao.class, ex
							.getMessage(), ex);
				}
				logger.debug("Exiting BaseHibernateDao.getEquipmentRemovalRecords");
		    	return todList;
		    }
		    
		    /**
		     * Method to fetch the Equipment Removal records.
		     *
		     * @return
		     * @throws DataAccessException
		     */
		    public List<TechnicalOrderDetail> fetchEquipmentRemovalRecordsForSALGWAndVSALGW(String soldTo, String vcodes) throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao : fetchEquipmentRemovalRecordsForSALGWAndVSALGW");
		    	List<TechnicalOrderDetail> todList = new ArrayList<TechnicalOrderDetail>();
		    	TechnicalOrderDetail techOrder = null;
		    	StringBuilder sb = new StringBuilder();
		    	Query query = null;
		    	try{
			    	/*sb.append("select sasset.row_id as asset_pk,asset_num, sasset.x_equipment_num as equipment_num, ")
			    	// x_summary_equipment_num -> SAP equipment, ref_number_2 -> Maestro equipment, ref_number_3 -> Nortel equipment
			    	.append("NVL(sasset.x_summary_equipment_num, NVL(sasset.ref_number_2, sasset.ref_number_3)) as summary_equipment_num, ")
			    	.append("prodint.name as material_code, prodint.alias_name as material_description, ")
			    	.append("sasset.qty as existing_quantity, Decode(NVL(x.attrib_03, 'N'), 'N', '', x_seid) seid, sasset.x_se_cd se_code, ")
			    	//.append("Decode(NVL(x_seid, 'N'), 'N', '', 'Yes') TECH_REG, ") -- commented as TRed status is running on attrib_03 column of s_asset_x
			    	.append("Decode(NVL(x.attrib_03, 'N'), 'N', '', 'Yes') TECH_REG, ")
			    	.append("Decode(NVL(x_asset_entitled, 'N'),  'Y', 'Yes', DECODE(NVL(x_site_entitled, 'N'), 'Y', 'Yes', '')) ACTIVE_CONTRACT, ")
			    	.append("x_asset_entitled as asset_entitled, x_site_entitled as site_entitled, prod_ln.name product_line, ")
			    	.append("NVL(x_serial_num, serial_num) as serial_number, sasset.X_RFA_SID, sasset.X_RFA_MID, x.attrib_07 GROUP_ID ")
			    	.append("from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_prod_int prodint, siebel.s_prod_ln prod_ln, siebel.s_asset_x x ")
			    	//.append("where orgext.loc = :soldToParam and prodint.name not in (VCODE_PARAM) ")
			    	.append("where prodint.pr_prod_ln_id = prod_ln.row_id(+) and sasset.prod_id = prodint.row_id and sasset.row_id = x.par_row_id(+) ")
			    	.append("and orgext.row_id = sasset.owner_accnt_id ")
			    	//.append("and NVL(sasset.x_summary_equipment_num, NVL(sasset.ref_number_2, sasset.ref_number_3)) IS NOT NULL ")
			    	.append("and ((prodint.name = 'V00328' and sasset.x_se_cd = 'SALGW') OR sasset.x_se_cd = 'VSALGW') ")
			    	.append("and sasset.qty > 0 and sasset.status_cd = 'Active' and orgext.loc = :soldToParam ") ;
			    	//.append("order by TECH_REG, equipment_num, se_code, GROUP_ID");

			    	//query = getSession().createSQLQuery(sb.toString().replace("VCODE_PARAM", vcodes));
			    	query = getSession().createSQLQuery(sb.toString());*/
			    	
			    	 query=getSessionForSiebel().getNamedQuery("fetchEquipmentRemovalRecordsForSALGWAndVSALGW");
			    	query.setParameter("soldToParam", soldTo);
			    	List<Object[]> resultSet = query.list();
			    	// Iterate through the result set
			    	for (Object[] object : resultSet) {
			    		//logger.debug("Material Code:"+object[4].toString().trim());
			    		techOrder = new TechnicalOrderDetail();
			    		techOrder.setAssetPK(object[0]!=null?object[0].toString():"");
			    		techOrder.setAssetNumber(object[1]!=null?object[1].toString():"");
			    		techOrder.setEquipmentNumber(object[2]!=null?object[2].toString():"");
			    		techOrder.setSummaryEquipmentNumber(object[3]!=null?object[3].toString():"");
			    		String mc = object[4]!=null?object[4].toString():"";
			    		String seCode = object[8]!=null?object[8].toString():"";
			    		techOrder.setSolutionElementCode(seCode);
			    		if(GRTConstants.SAL_GATEWAY.equalsIgnoreCase(seCode) && GRTConstants.VCODE_V00328.equalsIgnoreCase(mc)){
			    			//Defect #408 : updated the material code to SALGW for orphaned records
			    			techOrder.setMaterialCode(GRTConstants.SAL_GATEWAY);
			    			//techOrder.setMaterialCode(getSalGatewayMaterialCode().trim());
			    			techOrder.setDescription(object[5]!=null?object[5].toString():"");
			    		} else if(GRTConstants.SAL_VIRTUAL_GATEWAY.equalsIgnoreCase(seCode)){
			    			techOrder.setMaterialCode(GRTConstants.SAL_VIRTUAL_GATEWAY);
			    			techOrder.setDescription(GRTConstants.VSALGW_DESCRIPTION);
			    		}
			    		if(null != object[6]) {
			    			techOrder.setInitialQuantity(Long.parseLong(object[6].toString()));
			    		}
			    		techOrder.setSolutionElementId(object[7]!=null?object[7].toString():"");
			    		// Default we need to show all of them as seperate line items on EQR screen. So marking them as TOBed assets.
			    		//techOrder.setTechnicallyRegisterable(object[9]!=null?object[9].toString():"");
			    		techOrder.setTechnicallyRegisterable(GRTConstants.YES);
			    		techOrder.setActiveContractExist(object[10]!=null?object[10].toString():"");
			    		techOrder.setProductLine(object[13]!=null?object[13].toString():"");
			    		techOrder.setSerialNumber(object[14]!=null?object[14].toString():"");
			    		techOrder.setSid(object[15]!=null?object[15].toString():"");
			    		techOrder.setMid(object[16]!=null?object[16].toString():"");
			    		techOrder.setGroupId(object[17]!=null?object[17].toString():"");
			    		techOrder.setSalGateway(true);
			    		
			    		todList.add(techOrder);
			    	}
			    	logger.debug("****************************After - Query Execution **************************************"+todList.size());
			    }  catch (HibernateException hibEx) {
			    	logger.debug("HibernateException: "+hibEx);
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					logger.debug("Exception: "+ex);
					throw new DataAccessException(BaseHibernateDao.class, ex
							.getMessage(), ex);
				}
				logger.debug("Exiting BaseHibernateDao.fetchEquipmentRemovalRecordsForSALGWAndVSALGW");
		    	return todList;
		    }
		    
		    /**
		     *[AVAYA]:GRT2.1 Validate Material Code against MATERIAL_EXCLUSION table .
		     * GRT4.0 Support for MATERIAL_INCUSION
		     * @param material codes List<String>
		     * @return List<String> contains material codes
		     */
		    public List<MaterialExclusion> validateMaterialExclusion(List<String> materialCodes) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.validateMaterialExclusion");
		        
		        /*GRT4.0 - If material code is present in material_inclusion, filter them from the earlier list
		          and do not even consider them for material_exclusion
		         */
		        materialCodes = filterMaterialIncusion(materialCodes);
	        	List<MaterialExclusion> materialExcludedList = null;
	        	//Defect #518 UAT : Don't check for material exclusion if material list is empty( all r from material inclusion) 
		        if( materialCodes!=null && !materialCodes.isEmpty() ){
		        	Criterion queryFilterMap = null;
		        	Criteria criteria =  getSessionForGRT().createCriteria(MaterialExclusion.class);
		        	queryFilterMap = prepareSubList(materialCodes, "materialCode");
		        	criteria.add(queryFilterMap);
		        	//criteria.add(Restrictions.in("materialCode", artSrNo));
		        	materialExcludedList = criteria.list();

		        	/*GRT4.0 - Add material code exclusion from material_master for defective and blue codes */
		        	materialExcludedList.addAll(getDAndBlueMaterialCode(materialCodes));
		        	logger.debug("list.size()"+ materialExcludedList.size());
		        }
		        logger.debug("Exiting BaseHibernateDao.validateMaterialExclusion");
		        return materialExcludedList;
		    }
		    
		    
		    /**
		     *[AVAYA]:GRT4 Fetch the serialized flag from MATERIAL_MASTER table .
		     * GRT4.0 Support for MATERIAL_INCUSION
		     * @param material codes List<String>
		     * @return List<String> contains material codes
		     */
		    public List<MasterMaterial> populateMaterialSerialize(List<String> materialCodes) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.populateMaterialSerialize");
		        
		       
		        Criterion queryFilterMap = null;
		        List<MasterMaterial> masterMaterialList = null;
		        Criteria criteria =  getSessionForGRT().createCriteria(MasterMaterial.class);
		        queryFilterMap = prepareSubList(materialCodes, "materialCode");
		        criteria.add(queryFilterMap);
		        //criteria.add(Restrictions.in("materialCode", artSrNo));
		        masterMaterialList = criteria.list();		        
		        logger.debug("list.size()"+ masterMaterialList.size());
		        
		        logger.debug("Exiting BaseHibernateDao.populateMaterialSerialize");
		        return masterMaterialList;
		    }
		    
		    
		    /* GRT 4.0 Changes starts */
		    public List<String> filterMaterialIncusion(List<String> materialCodes) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.filterMaterialIncusion");
		        
		        Query query = getSessionForGRT().getNamedQuery("getMaterialInclusion");
				List<String> materialInclusionList = query.list();
		        
		        if ((materialInclusionList != null) && (materialInclusionList.size() > 0)) {
			        List<String> newMaterialCodes = new ArrayList<String>(); 
			        for (String mc : materialCodes) {
			        	if (!materialInclusionList.contains(mc)) 
			        		newMaterialCodes.add(mc);
			        }
			        materialCodes = newMaterialCodes;
		        }
		        logger.debug("Exiting BaseHibernateDao.filterMaterialIncusion");
		        return materialCodes;
		    }
		     
		    
		    /**
		     * Get defective and blue material code list
		     *
		     */
		    public List<MaterialExclusion> getDAndBlueMaterialCode(List<String> materialCodes) throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao.getDAndBlueMaterialCode");
		    	List<MaterialExclusion> materialExcludedList = new ArrayList<MaterialExclusion>();
		    	
		    	Criterion queryFilterMap = null;
		    	Criteria criteria =  getSessionForGRT().createCriteria(MasterMaterial.class);
		        queryFilterMap = prepareSubList(materialCodes, "materialCode");
		        criteria.add(queryFilterMap);
		        List<MasterMaterial> masterMaterialList = criteria.list();
		        	 
	        	for (int i = 0; i < masterMaterialList.size(); i++) {
	        		 MasterMaterial masterMaterial = masterMaterialList.get(i);
	        		 MaterialExclusion materialExclusion = new MaterialExclusion(); 
	                 
	        		 String materialCode = masterMaterial.getMaterialCode();
	        		 boolean isBlue = masterMaterial.isBlueCde();
	        		 
	        		 if ( (StringUtils.isNotEmpty(materialCode)) && (materialCode.charAt(materialCode.length()-1)  == 'D') ) {
	        			 materialExclusion.setMaterialCode(materialCode);
		                 materialExclusion.setExclusionSource(GRTConstants.EXCLUSION_SOURCE_DEFECTIVE);
		                 materialExclusion.setExclusionOrder(1);
		                 materialExcludedList.add(materialExclusion);
	        		 } else if (isBlue) {
	        			 materialExclusion.setMaterialCode(materialCode);
		                 materialExclusion.setExclusionSource(GRTConstants.EXCLUSION_SOURCE_BLUE);
		                 materialExclusion.setExclusionOrder(1);
		                 materialExcludedList.add(materialExclusion);
	        		 }	                 
	            }
		        	 
		         
		        logger.debug("Exiting BaseHibernateDao.getDAndBlueMaterialCode");
		        return materialExcludedList;
		    }
		    
		    /* GRT 4.0 Changes ends */
		    
		    protected static Criterion prepareSubList(List<String> parameterList, String columnName) {
				//FIX for INExpression issue handling more than 1000 records at a time
				int psize = parameterList.size();
				int fromIndex = 0;
				int toIndex = (psize>1000)?1000:psize;
				List subList = parameterList.subList(fromIndex, toIndex);
				toIndex = subList.size();
				psize = psize-toIndex;
				//query.setParameterList("subList1",subList);
				Criterion lhs = Restrictions.in(columnName, subList);

				int index=2;
				while(psize>0) {
					fromIndex=toIndex;
					toIndex += (psize>1000)?1000:psize; // 12000
					subList = parameterList.subList(fromIndex, toIndex);
					//query.setParameterList("subList"+index,subList);
					Criterion rhs = Restrictions.in(columnName, subList);
					lhs = Restrictions.or(lhs, rhs);
					index++;
					psize = psize-subList.size();
				}
				return lhs;
			}
		    
		    public List<String> getSoldToList(String userName) throws DataAccessException {
		   		logger.debug("Entering BaseHibernateDao.getSoldToList");
		   		List<String> soldToIdList = null;
		   		try {
		   			/** String queryStr = "select distinct account_number from "
		   				+ GRTPropertiesUtil.getProperty(GRTConstants.CXP_USER_NAME)
		   						.trim()
		   				+ ".v_css_user_access_list where user_logon = :user_logon";
		   			SQLQuery query =  getSession(GRTConstants.CXP_DATA_BASE).createSQLQuery(queryStr); */
		   			Query query = getSessionForCXP().getNamedQuery("getSoldToList");
		   			query.setString("user_logon", userName);
		   			
		   			soldToIdList = query.list();
		   		} catch (HibernateException hibEx) {
		   			throw new DataAccessException(BaseHibernateDao.class, hibEx
		   					.getMessage(), hibEx);
		   		} catch (Exception ex) {
		   			ex.printStackTrace();
		   			throw new DataAccessException(BaseHibernateDao.class, ex
		   					.getMessage(), ex);
		   		} 
		   		logger.debug("Exiting BaseHibernateDao.getSoldToList");
		   		
		   		return soldToIdList;
		   	}
		    /**
		     * Method to get the sold to id from Sales_Out for the matching BP Link ID.
		     *
		     * @param BPLinkId String
		     * @return soldToIdList contains soldToId
		     */
		    public List<String> getSoldToIdFromSalesOut(String bpLinkId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.getSoldToIdFromSalesOut");
		        List<String> salesOutList = null;
		        try {
					if(StringUtils.isNotEmpty(bpLinkId)) {
						bpLinkId = getValidBpLinkId(bpLinkId);
		        	}
		        //	String queryStr = "select distinct SOLD_TO_ID from SALES_OUT where BP_LINK_ID = :bpLinkId";

		        	//SQLQuery query =  getSession().createSQLQuery(queryStr);
		        	Query query=getSessionForGRT().getNamedQuery("getSoldToIdFromSalesOut");
		            query.setString("bpLinkId", bpLinkId);
		            salesOutList = query.list();
		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        } catch (Exception ex) {
		            throw new DataAccessException(BaseHibernateDao.class, ex
		                    .getMessage(), ex);
		        }

		        logger.debug("Entering BaseHibernateDao.getSoldToIdFromSalesOut");

		        return salesOutList;
		    }
		    
		    /**
		     * Method to filter the registration summary list.
		     *
		     * @param soldToId string
		     * @return List contains list of registration summary
		     */
		    public PaginationForSiteRegistration getRegListFromDB(Map map, String[] soldToIdList, boolean ipoFilter, String userType, String bpLinkId, 
					String requesterFullName, int endIndex, int startIndex, boolean fetchAllData) throws DataAccessException{
				PaginationForSiteRegistration dto = new PaginationForSiteRegistration();
				List<SiteRegistration> registrationSummaryList = null;
				List<BPAccountTempAccess> bpAccess = null;
				int size = 0;
				logger.debug("entering Method BaseHibernateDao.getRegListFromDB");
				try {

					List<String> parameterList = new ArrayList<String>();
					Criterion queryFilterMap = null;
					Criteria  criteriaList = getSessionForGRT().createCriteria(SiteRegistration.class);
					String reqNameFilterStr = "";
					if(StringUtils.isNotEmpty(userType) && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userType)){
						reqNameFilterStr = requesterFullName;
					}

					//Add the filter to the criteria list
					criteriaList = filterByRegistration(map, ipoFilter, criteriaList, reqNameFilterStr);

					if(StringUtils.isEmpty(listToString(map.get(GRTConstants.REGISTRATION_ID_FILTER)))) {
						if (soldToIdList!=null && soldToIdList.length > 0) {
							logger.debug("DEBUG : method getRegListFromDB"+" checking soldToIdList size is >0");
							logger.debug("Sold To ID List ============================"+ soldToIdList.length);
							for(int i=0;i<soldToIdList.length;i++) {
								parameterList.add(soldToIdList[i]);
							}
							queryFilterMap = prepareSubList(parameterList, "soldToId");
							criteriaList.add(queryFilterMap);
						}
					}

					//	Cast to CriteriaImpl to get access to the Projection and ResultTransformer
					Projection originalProjection = null;
					ResultTransformer originalResultTransformer = null;
					CriteriaImpl cImpl = (CriteriaImpl) criteriaList;

					//	Save original Projection and ResultTransformer (if any, could be null).
					originalProjection = cImpl.getProjection();
					originalResultTransformer = cImpl.getResultTransformer();

					size = ((Number)criteriaList.setProjection(Projections.rowCount()).uniqueResult()).intValue();
					logger.debug("Query Return Size========================"+size);

					// Restore original Projection and ResultTransformer
					criteriaList.setProjection(originalProjection);
					criteriaList.setResultTransformer(originalResultTransformer);
					// Sorting
					criteriaList = sortByRegistration(map, ipoFilter, criteriaList);

					/*if(size > 0 && map.get(GRTConstants.CURRENT_PAGE_NO)!=null && !isBlank(listToString(map.get(GRTConstants.CURRENT_PAGE_NO)))
							&& size > Integer.parseInt(listToString(map.get(GRTConstants.CURRENT_PAGE_NO)))){
						logger.debug(GRTConstants.CURRENT_PAGE_NO+": "+listToString(map.get(GRTConstants.CURRENT_PAGE_NO)));
						startIndex = 0;
						if(map.get(GRTConstants.REC_TO_DISPLAY)!=null && !isBlank(listToString(map.get(GRTConstants.REC_TO_DISPLAY))) ){
							logger.debug(GRTConstants.REC_TO_DISPLAY+": "+listToString(map.get(GRTConstants.REC_TO_DISPLAY)));
							startIndex = Integer.parseInt(listToString(map.get(GRTConstants.REC_TO_DISPLAY)))*(Integer.parseInt(listToString(map.get(GRTConstants.CURRENT_PAGE_NO)))-1);
						}
					}*/
					startIndex = (Integer)map.get(GRTConstants.CURRENT_PAGE_NO);
					criteriaList.setFirstResult(startIndex);
					int recToDispInt = (Integer) map.get(GRTConstants.REC_TO_DISPLAY);
					criteriaList.setMaxResults(recToDispInt);

					List<SiteRegistration> objectList = criteriaList.list();
					dto.setMaxSize(size);
					Iterator iter = objectList.iterator();
					registrationSummaryList = new ArrayList<SiteRegistration>();
					int count =0;
					while(iter.hasNext()){
						Object obj =  iter.next();
						count++;
						if (obj.getClass()!=null)
							if (obj instanceof SiteRegistration) {
								registrationSummaryList.add((SiteRegistration)obj);
							} else if(obj instanceof Object[]){
								registrationSummaryList.add((SiteRegistration)((Object[])obj)[0]);
							}
					}
					logger.debug("Count......................."+count);
					
					if(StringUtils.isNotEmpty(listToString(map.get(GRTConstants.REGISTRATION_ID_FILTER)))) {
						if (registrationSummaryList != null && registrationSummaryList.size() > 0 && (soldToIdList == null || soldToIdList.length ==0) && (StringUtils.isNotEmpty(userType) && GRTConstants.USER_TYPE_BP.equalsIgnoreCase(userType))) {
							String soldTo = registrationSummaryList.get(0).getSoldToId();
							bpAccess = this.queryAccess(bpLinkId, soldTo);
							if((bpAccess == null || bpAccess.size() == 0) && !this.isBpSoldToExistForTheUser(soldTo, bpLinkId)){
								return new PaginationForSiteRegistration();
							}
						}
					}
					
				} catch (HibernateException hibEx) {
					logger.error(" DEBUG : method getRegListFromDB : hibEx stacktrace print : ",hibEx);
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception hibEx) {
					logger.error(" DEBUG : method getRegListFromDB : exc stacktrace print : ",hibEx);
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				}
				logger.debug("exiting Method BaseHibernateDao.getRegListFromDB");
				dto.setList(registrationSummaryList);
				return dto;
			}

		    /**
			 * @param map
			 * @param ipoFilter
			 * @param criteria
			 * @param requesterFullName
			 * @return Criteria
			 *  Method for adding filter for registration list
			 */
			private Criteria filterByRegistration(Map map, boolean ipoFilter, Criteria criteria, String requesterFullName) {
				boolean filterFlag = false;
				boolean ibStatusCriteria = false;
				boolean eqrCriteria = false;
				boolean eqpMoveCriteria = false;
				boolean trStatusCriteria = false;
				
				if(!"filter".equals(listToString(map.get(GRTConstants.FILTER_OR_CLEAR_BTN)))){
					/* GRT 4.0 : New Requirement For Registration List */
					if(map.get(GRTConstants.REGISTRATION_IDENTIFIER_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_IDENTIFIER_FILTER))) ){
						criteria.add(Restrictions.ilike("registrationIdentifier", "%"+listToString(map.get(GRTConstants.REGISTRATION_IDENTIFIER_FILTER))+"%"));
						filterFlag = true;
					}
					if(map.get(GRTConstants.REGISTRATION_ID_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_ID_FILTER))) ){
						criteria.add(Restrictions.like("registrationId", "%"+listToString(map.get(GRTConstants.REGISTRATION_ID_FILTER))+"%"));
						filterFlag = true;
					}
					logger.debug("map.get('requesterName'):" + map.get(GRTConstants.REQUESTER_NAME_FILTER));
					if(map.get(GRTConstants.REQUESTER_NAME_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REQUESTER_NAME_FILTER))) ){
						logger.debug("requester condition met");
						String requesterName = listToString(map.get(GRTConstants.REQUESTER_NAME_FILTER));
						String[] strArr = requesterName.split(" ");
						if(strArr.length>1){
							int position = requesterName.lastIndexOf(" ");
							if(StringUtils.isNotEmpty(requesterFullName) && requesterName.equalsIgnoreCase(requesterFullName)){
								criteria.add(Restrictions.and(
										Restrictions.ilike("firstName", requesterFullName.substring(0, position)),
										Restrictions.ilike("lastName", requesterFullName.substring(position+1))));
							} else {
								criteria.add(Restrictions.and(
										Restrictions.ilike("firstName", "%"+requesterName.substring(0, position)+"%"),
										Restrictions.ilike("lastName", "%"+requesterName.substring(position+1)+"%")));
							}
						} else {
							criteria.add(Restrictions.or(
									Restrictions.ilike("firstName", "%"+listToString(map.get(GRTConstants.REQUESTER_NAME_FILTER))+"%"),
									Restrictions.ilike("lastName", "%"+listToString(map.get(GRTConstants.REQUESTER_NAME_FILTER))+"%")));
						}
						filterFlag = true;
					}

					if(map.get(GRTConstants.GRT_NOTIFICATION_NAME_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_FILTER))) ){
						String grtNotificationName = listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_FILTER));
						String[] strArr = grtNotificationName.split(" ");
						if(strArr.length>1){
							int position = grtNotificationName.lastIndexOf(" ");
							criteria.add(Restrictions.and(
									Restrictions.ilike("onsiteFirstName", "%"+grtNotificationName.substring(0, position)+"%"),
									Restrictions.ilike("onsiteLastName", "%"+grtNotificationName.substring(position+1)+"%")));
						} else {
							criteria.add(Restrictions.or(
									Restrictions.ilike("onsiteFirstName", "%"+listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_FILTER))+"%"),
									Restrictions.ilike("onsiteLastName", "%"+listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_FILTER))+"%")));
						}
						filterFlag = true;
					}

					if(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_FILTER))) ){
						criteria.add(Restrictions.ilike("onsiteEmail", "%"+listToString(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_FILTER))+"%"));
						filterFlag = true;
					}

					if(map.get(GRTConstants.CREATED_DATE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.CREATED_DATE_FILTER))) ){
						String dateString=  listToString(map.get(GRTConstants.CREATED_DATE_FILTER));
						logger.debug("CreateDate:"+dateString);
						String[] strArr = dateString.split("~");
						if(strArr.length>1){
							if(formatDate(strArr[0]) != null && StringUtils.isNotEmpty(formatDate(strArr[0]))
									&& formatDate(strArr[1]) != null && StringUtils.isNotEmpty(formatDate(strArr[1]))){
								criteria.add(Restrictions.sqlRestriction("this_.CREATED_DATE >= to_date('"+formatDate(strArr[0])+" 12:00:00 AM', 'MM/dd/yyyy HH:mi:ss AM')"));
								criteria.add(Restrictions.sqlRestriction("this_.CREATED_DATE <= to_date('"+formatDate(strArr[1])+" 11:59:59 PM', 'MM/dd/yyyy HH:mi:ss PM')"));
							}
						} else {
							dateString = formatDate(dateString);
							if(dateString != null && StringUtils.isNotEmpty(dateString)){
								criteria.add(new DateLikeExpression("createdDate",formLikeExpression(dateString)));
							} else {
								map.put(GRTConstants.CREATED_DATE_FILTER, "");
							}
						}
						filterFlag = true;
					}

					if(map.get(GRTConstants.LAST_UPDATED_DATE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.LAST_UPDATED_DATE_FILTER))) ){
						String dateString=  listToString(map.get(GRTConstants.LAST_UPDATED_DATE_FILTER));
						logger.debug("LastUpdatedDate:"+dateString);
						String[] strArr = dateString.split("~");
						if(strArr.length>1){
							if(formatDate(strArr[0]) != null && StringUtils.isNotEmpty(formatDate(strArr[0]))
									&& formatDate(strArr[1]) != null && StringUtils.isNotEmpty(formatDate(strArr[1]))){
								criteria.add(Restrictions.sqlRestriction("this_.UPDATED_DATE >= to_date('"+formatDate(strArr[0])+" 12:00:00 AM', 'MM/dd/yyyy HH:mi:ss AM')"));
								criteria.add(Restrictions.sqlRestriction("this_.UPDATED_DATE <= to_date('"+formatDate(strArr[1])+" 11:59:59 PM', 'MM/dd/yyyy HH:mi:ss PM')"));
							}
						} else {
							dateString = formatDate(dateString);
							if(dateString != null && StringUtils.isNotEmpty(dateString)){
								criteria.add(new DateLikeExpression("updatedDate",formLikeExpression(dateString)));
							} else {
								map.put(GRTConstants.LAST_UPDATED_DATE_FILTER, "");
							}
						}
						filterFlag = true;
					}

					if(map.get(GRTConstants.LAST_UPDATED_BY_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.LAST_UPDATED_BY_FILTER))) ){
						String updatedBy=  listToString(map.get(GRTConstants.LAST_UPDATED_BY_FILTER));
						criteria.add(Restrictions.ilike("updatedBy", "%"+updatedBy.toLowerCase()+"%"));
						filterFlag = true;
					}

					if(map.get(GRTConstants.REGISTRATION_TYPE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER))) ){
						String registrationType= listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER));
						String [] regTypeArr = registrationType.split("\\|");
						//GRT 4.0 Change : New Requirement
						List<String> regTypeIds = RegistrationTypeEnum.getIdValues(regTypeArr);
						 
						criteria.createAlias("registrationType", "registrationType")
						.add(Restrictions.in("registrationType.registrationId", regTypeIds));
						filterFlag = true;
					}

					if(map.get(GRTConstants.IB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.IB_STATUS_FILTER))) ){
						logger.debug("InstallBaseStatus ============================"+listToString(map.get(GRTConstants.IB_STATUS_FILTER)));
						String installBaseStatus= listToString(map.get(GRTConstants.IB_STATUS_FILTER));
						Object [] ibStatusArr = installBaseStatus.split("\\|");
						//GRT 4.0 Change : New Requirement
						criteria.createAlias("installBaseStatus", "installBaseStatus").add(Restrictions.in("installBaseStatus.statusShortDescription", ibStatusArr));
						ibStatusCriteria = true;
						filterFlag = true;
					}

					if(map.get(GRTConstants.TOB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.TOB_STATUS_FILTER))) ){
						logger.debug("techRegStatus ============================"+listToString(map.get(GRTConstants.TOB_STATUS_FILTER)));
						String TechRegStatus = listToString(map.get(GRTConstants.TOB_STATUS_FILTER));
						Object [] trStatusArr = TechRegStatus.split("\\|");
						//GRT 4.0 Change : New Requirement
						criteria.createAlias("techRegStatus", "techRegStatus")
						.add(Restrictions.in("techRegStatus.statusShortDescription", trStatusArr));
						trStatusCriteria = true;
						filterFlag = true;
					}

					if(map.get(GRTConstants.EQR_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQR_STATUS_FILTER))) ){
						logger.debug("finalRecordStatus ============================"+listToString(map.get(GRTConstants.EQR_STATUS_FILTER)));
						String finalValidationStatus = listToString(map.get(GRTConstants.EQR_STATUS_FILTER));
						Object [] fvStatusArr = finalValidationStatus.split("\\|");
						//GRT 4.0 Change : New Requirement
						 criteria.createAlias("finalValidationStatus", "finalValidationStatus")
						.add(Restrictions.in("finalValidationStatus.statusShortDescription", fvStatusArr));
						eqrCriteria = true;
						filterFlag = true;
					}
					
					//GRT 4.0 Change - Added Equipment move filter
					if(map.get(GRTConstants.EQP_MOVE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQP_MOVE_FILTER))) ){
						logger.debug("eqpmovestatus ============================"+listToString(map.get(GRTConstants.EQP_MOVE_FILTER)));
						String eqpMoveStatus = listToString(map.get(GRTConstants.EQP_MOVE_FILTER));
						Object [] eqpMoveStatusArr = eqpMoveStatus.split("\\|");
						//GRT 4.0 Change : New Requirement
						criteria.createAlias("eqrMoveStatus", "eqrMoveStatus")
						.add(Restrictions.in("eqrMoveStatus.statusShortDescription", eqpMoveStatusArr));
						eqpMoveCriteria = true;
						filterFlag = true;
					}

					if(map.get(GRTConstants.SOLD_TO_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.SOLD_TO_FILTER))) ){
						criteria.add( Restrictions.like("soldToId", this.formLikeExpression(listToString(map.get(GRTConstants.SOLD_TO_FILTER)))));
						filterFlag = true;
					}

					if(map.get(GRTConstants.CUST_NAME_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.CUST_NAME_FILTER))) ){
						String CustomerName =   "%"+listToString(map.get(GRTConstants.CUST_NAME_FILTER)).toLowerCase()+"%";
						logger.debug("Filter Customer Name " + CustomerName );
						criteria.add(new StatusLikeExpression ("company", CustomerName, true));
						filterFlag = true;
					}

					if(map.get(GRTConstants.ACTIVE_REL_SR_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.ACTIVE_REL_SR_FILTER))) ){
						criteria.add( Restrictions.like("activeSR", this.formLikeExpression(listToString(map.get(GRTConstants.ACTIVE_REL_SR_FILTER)))));
						filterFlag = true;
					}
					
					//GRT 4.0 Change - Exclude Cancelled Status
					if(map.get(GRTConstants.EX_CANCELLED_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EX_CANCELLED_STATUS_FILTER))) ){
						logger.debug("Exclude Cancelled Status ============================"+listToString(map.get(GRTConstants.EX_CANCELLED_STATUS_FILTER)));
						//GRT 4.0 Change : New Requirement
						
						if ( !ibStatusCriteria ) criteria.createAlias("installBaseStatus", "installBaseStatus");
						
						Criterion ibCriteria1 = new StatusLikeExpression("installBaseStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), true);
						Criterion ibCriteria2 = new StatusLikeExpression("installBaseStatus.statusShortDescription", this.formLikeExpression(StatusEnum.NOTINITIATED.getStatusShortDescription().toLowerCase()), true);
						Criterion ibCriteria = Restrictions.or(ibCriteria1, ibCriteria2);
						
						//criteria.add(new StatusLikeExpression("installBaseStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), false));
						 
						if( !trStatusCriteria )  criteria.createAlias("techRegStatus", "techRegStatus");
						//criteria.add(new StatusLikeExpression("techRegStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), false));
						
						Criterion tobCriteria1 = new StatusLikeExpression("techRegStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), true);
						Criterion tobCriteria2 = new StatusLikeExpression("techRegStatus.statusShortDescription", this.formLikeExpression(StatusEnum.NOTINITIATED.getStatusShortDescription().toLowerCase()), true);
						Criterion tobCriteria = Restrictions.or(tobCriteria1, tobCriteria2);
						
						if( !eqrCriteria )  criteria.createAlias("finalValidationStatus", "finalValidationStatus");
						//criteria.add(new StatusLikeExpression("finalValidationStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), false));
						
						Criterion fvCrieteria1 = new StatusLikeExpression("finalValidationStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), true);
						Criterion fvCrieteria2 = new StatusLikeExpression("finalValidationStatus.statusShortDescription", this.formLikeExpression(StatusEnum.NOTINITIATED.getStatusShortDescription().toLowerCase()), true);
						Criterion fvCrieteria = Restrictions.or(fvCrieteria1, fvCrieteria2);
						
						if( !eqpMoveCriteria ) criteria.createAlias("eqrMoveStatus", "eqrMoveStatus");
						//criteria.add(new StatusLikeExpression("eqrMoveStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), false));
						 
						Criterion eqmCrieteria1 = new StatusLikeExpression("eqrMoveStatus.statusShortDescription", this.formLikeExpression(StatusEnum.CANCELLED.getStatusShortDescription().toLowerCase()), true);
						Criterion eqmCrieteria2 = new StatusLikeExpression("eqrMoveStatus.statusShortDescription", this.formLikeExpression(StatusEnum.NOTINITIATED.getStatusShortDescription().toLowerCase()), true);
						Criterion eqmCrieteria = Restrictions.or(eqmCrieteria1, eqmCrieteria2);
						
						Criterion finalCancelledCrieteria = Restrictions.not(Restrictions.and(Restrictions.and(Restrictions.and(ibCriteria, tobCriteria), fvCrieteria), eqmCrieteria));
						criteria.add(finalCancelledCrieteria);
						
						filterFlag = true;
					}
				}
				logger.debug("filterFlag:" + filterFlag);
				logger.debug("StringUtils.isNotEmpty(requesterFullName):" + StringUtils.isNotEmpty(requesterFullName));
				if(!filterFlag && StringUtils.isNotEmpty(requesterFullName)){
					logger.debug("Condition met for requesterName");
					map.put(GRTConstants.REQUESTER_NAME_FILTER, requesterFullName);
					String[] strArr = requesterFullName.split(" ");
					if(strArr.length>1){
						int position = requesterFullName.lastIndexOf(" ");
						criteria.add(Restrictions.and(
								Restrictions.ilike("firstName", requesterFullName.substring(0, position)),
								Restrictions.ilike("lastName", requesterFullName.substring(position+1))));
					}
				}
				if(ipoFilter){
					criteria.add(Restrictions.like("typeOfImplementation","5"));
					System.out.println("***************Inside IPO filter   ****************  ");
				}
				
				if(map.get("filterValue").toString().equalsIgnoreCase("DESC")){
					criteria.addOrder(Order.desc("createdDate"));
				}
				
				return criteria;
			}

			private String formLikeExpression(String expression){
				return "%" + expression + "%";
			}
   
			/**
			 * 	parse data from list
			 * */
			private static String listToString(Object obj){
				 if (obj instanceof List) {
					if(((List)obj).size()>0){
						return ((List)obj).get(0).toString().trim().toLowerCase();
					}
				} else if (obj instanceof String) {
					return (String)obj;
				}
				return "";
			}
			
			/**
			 * @param map
			 * @param ipoFilter
			 * @param criteria
			 * @return Criteria
			 * Method to add criteria for sorting in registration list functionality
			 */
				String orderElement = "asc";
				private Criteria sortByRegistration(Map map, boolean ipoFilter, Criteria criteria){
				boolean sortFlag = false;
				boolean excludeCancelledFlag  = map.get(GRTConstants.EX_CANCELLED_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EX_CANCELLED_STATUS_FILTER)));
				if(map.get(GRTConstants.REGISTRATION_ID_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_ID_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.REGISTRATION_ID_SORT));
					logger.debug("registrationId_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						
						criteria.addOrder(new org.hibernate.criterion.Order("registrationId", true) {
						      @Override
						       public String toSqlString(Criteria criteria,CriteriaQuery criteriaQuery) throws HibernateException {
						            return "to_number(this_.REGISTRATION_ID) asc";
						        }
						   });
						//criteria.addOrder(new org.hibernate.criterion.Order(Order.asc("registrationId")));
					} else {
						
						criteria.addOrder(new org.hibernate.criterion.Order("registrationId", true) {
						      @Override
						       public String toSqlString(Criteria criteria,CriteriaQuery criteriaQuery) throws HibernateException {
						            return "to_number(this_.REGISTRATION_ID) desc";
						        }
						   });
						//criteria.addOrder(Order.desc("registrationId"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.REQUESTER_NAME_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.REQUESTER_NAME_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.REQUESTER_NAME_SORT));
					logger.debug("requesterName_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("firstName"));
					} else {
						criteria.addOrder(Order.desc("firstName"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.GRT_NOTIFICATION_NAME_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.GRT_NOTIFICATION_NAME_SORT));
					logger.debug("grtNotificationName_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("onsiteFirstName"));
					} else {
						criteria.addOrder(Order.desc("onsiteFirstName"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.GRT_NOTIFICATION_EMAIL_SORT));
					logger.debug("grtNotificationEmail_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("onsiteEmail"));
					} else {
						criteria.addOrder(Order.desc("onsiteEmail"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.CREATED_DATE_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.CREATED_DATE_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.CREATED_DATE_SORT));
					logger.debug("assembler_s_CreateDate:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("createdDate"));
					} else {
						criteria.addOrder(Order.desc("createdDate"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.LAST_UPDATED_DATE_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.LAST_UPDATED_DATE_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.LAST_UPDATED_DATE_SORT));
					logger.debug("lastUpdatedDate_sort: "+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("updatedDate"));
					} else {
						criteria.addOrder(Order.desc("updatedDate"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.LAST_UPDATED_BY_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.LAST_UPDATED_BY_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.LAST_UPDATED_BY_SORT));
					logger.debug("lastUpdatedBy_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("updatedBy"));
					
					} else {
						criteria.addOrder(Order.desc("updatedBy"));
					}
					sortFlag = true;
				}
				/*if(map.get(GRTConstants.LAST_UPDATED_BY_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.LAST_UPDATED_BY_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.LAST_UPDATED_BY_SORT));
					logger.debug("registrationType_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.REGISTRATION_TYPE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER))) ){
							criteria.addOrder(Order.asc("registrationType.registrationType"));
						} else {
							criteria.createAlias("registrationType", "registrationType").addOrder(Order.asc("registrationType.registrationType"));
						}
					} else {
						if(map.get(GRTConstants.REGISTRATION_TYPE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER))) ){
							criteria.addOrder(Order.desc("registrationType.registrationType"));
						} else {
							criteria.createAlias("registrationType", "registrationType").addOrder(Order.desc("registrationType.registrationType"));
						}
					}
					sortFlag = true;
				}*/
				if(map.get(GRTConstants.IB_STATUS_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.IB_STATUS_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.IB_STATUS_SORT));
					logger.debug("installBaseStatus_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.IB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.IB_STATUS_FILTER))) 
								|| excludeCancelledFlag	){
							criteria.addOrder(Order.asc("installBaseStatus.statusShortDescription"));
						} else {
							criteria.createAlias("installBaseStatus", "installBaseStatus").addOrder(Order.asc("installBaseStatus.statusShortDescription"));
						}
					} else {
						if(map.get(GRTConstants.IB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.IB_STATUS_FILTER))) 
								|| excludeCancelledFlag ){
							criteria.addOrder(Order.desc("installBaseStatus.statusShortDescription"));
						} else {
							criteria.createAlias("installBaseStatus", "installBaseStatus").addOrder(Order.desc("installBaseStatus.statusShortDescription"));
						}
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.TOB_STATUS_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.TOB_STATUS_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.TOB_STATUS_SORT));
					logger.debug("technicalOnboardingStatus_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.TOB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.TOB_STATUS_FILTER)))
								|| excludeCancelledFlag	) {
							criteria.addOrder(Order.asc("techRegStatus.statusShortDescription"));
						} else {
							criteria.createAlias("techRegStatus", "techRegStatus").addOrder(Order.asc("techRegStatus.statusShortDescription"));
						}
					} else {
						if(map.get(GRTConstants.TOB_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.TOB_STATUS_FILTER)))
								|| excludeCancelledFlag ) {
							criteria.addOrder(Order.desc("techRegStatus.statusShortDescription"));
						} else {
							criteria.createAlias("techRegStatus", "techRegStatus").addOrder(Order.desc("techRegStatus.statusShortDescription"));
						}
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.EQR_STATUS_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.EQR_STATUS_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.EQR_STATUS_SORT));
					logger.debug("equipmentRemovalStatus_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.EQR_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQR_STATUS_FILTER)))
								|| excludeCancelledFlag	) {
							criteria.addOrder(Order.asc("finalValidationStatus.statusShortDescription"));
						} else {
							criteria.createAlias("finalValidationStatus", "finalValidationStatus").addOrder(Order.asc("finalValidationStatus.statusShortDescription"));
						}
					} else {
						if(map.get(GRTConstants.EQR_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQR_STATUS_FILTER)))
								|| excludeCancelledFlag ) {
							criteria.addOrder(Order.desc("finalValidationStatus.statusShortDescription"));
						} else {
							criteria.createAlias("finalValidationStatus", "finalValidationStatus").addOrder(Order.desc("finalValidationStatus.statusShortDescription"));
						}
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.SOLD_TO_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.SOLD_TO_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.SOLD_TO_SORT));
					logger.debug("soldTO_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						//criteria.addOrder(Order.asc("soldToId"));
						criteria.addOrder(new org.hibernate.criterion.Order("soldToId", true) {
						      @Override
						       public String toSqlString(Criteria criteria,CriteriaQuery criteriaQuery) throws HibernateException {
						            return "to_number(sold_To_Id) asc";
						        }
						   });
						
					} else {
						//criteria.addOrder(Order.desc("soldToId"));
						criteria.addOrder(new org.hibernate.criterion.Order("soldToId", true) {
						      @Override
						       public String toSqlString(Criteria criteria,CriteriaQuery criteriaQuery) throws HibernateException {
						            return "to_number(sold_To_Id) desc";
						        }
						   });
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.CUST_NAME_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.CUST_NAME_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.CUST_NAME_SORT));
					logger.debug("customerName_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("company"));
					} else {
						criteria.addOrder(Order.desc("company"));
					}
					sortFlag = true;
				}
				if(map.get(GRTConstants.ACTIVE_REL_SR_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.ACTIVE_REL_SR_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.ACTIVE_REL_SR_SORT));
					logger.debug("activeRelSR_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("activeSR"));
					} else {
						criteria.addOrder(Order.desc("activeSR"));
					}
					sortFlag = true;
				}
				
				//GRT 4.0 Change For Sorting
				//Added Sorting func for registration identifier
				if(map.get(GRTConstants.REGISTRATION_IDENTIFIER_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_IDENTIFIER_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.REGISTRATION_IDENTIFIER_SORT));
					logger.debug("registrationIdentifier_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						criteria.addOrder(Order.asc("registrationIdentifier"));
					} else {
						criteria.addOrder(Order.desc("registrationIdentifier"));
					}
					sortFlag = true;
				}
				
				//Added Sorting func for Equipment Status
				if(map.get(GRTConstants.EQP_MOVE_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.EQP_MOVE_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.EQP_MOVE_SORT));
					logger.debug("equipmentmoveStatus_sort:"+orderElement);
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.EQP_MOVE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQP_MOVE_FILTER)))
								|| excludeCancelledFlag ) {
							criteria.addOrder(Order.asc("eqrMoveStatus.statusShortDescription"));
						} else {
							criteria.createAlias("eqrMoveStatus", "eqrMoveStatus").addOrder(Order.asc("eqrMoveStatus.statusShortDescription"));
						}
					} else {
						if(map.get(GRTConstants.EQR_STATUS_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.EQP_MOVE_FILTER)))
								|| excludeCancelledFlag ) {
							criteria.addOrder(Order.desc("eqrMoveStatus.statusShortDescription"));
						} else {
							criteria.createAlias("eqrMoveStatus", "eqrMoveStatus").addOrder(Order.desc("eqrMoveStatus.statusShortDescription"));
						}
					}
					sortFlag = true;
				}
				
				
				//Defect #391
				if(map.get(GRTConstants.REGISTRATION_TYPE_SORT)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_SORT))) ){
					orderElement = listToString(map.get(GRTConstants.REGISTRATION_TYPE_SORT));
					
					/*if(orderElement.equalsIgnoreCase("ASC")){
						criteria.createAlias("registrationType", "registrationType").addOrder(Order.asc("registrationType.registrationType"));
					} else {
						criteria.createAlias("registrationType", "registrationType").addOrder(Order.desc("registrationType.registrationType"));
					}*/
					//To avoid creating multiple Alias for registrationType
					if(orderElement.equalsIgnoreCase("ASC")){
						if(map.get(GRTConstants.REGISTRATION_TYPE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER))) ){
							criteria.addOrder(Order.asc("registrationType.registrationType"));
						} else {
							criteria.createAlias("registrationType", "registrationType").addOrder(Order.asc("registrationType.registrationType"));
						}
					} else {
						if(map.get(GRTConstants.REGISTRATION_TYPE_FILTER)!=null && !isBlank(listToString(map.get(GRTConstants.REGISTRATION_TYPE_FILTER))) ){
							criteria.addOrder(Order.desc("registrationType.registrationType"));
						} else {
							criteria.createAlias("registrationType", "registrationType").addOrder(Order.desc("registrationType.registrationType"));
						}
					}
					
					sortFlag = true;
				}
				
				if(!sortFlag){
					criteria.addOrder(Order.desc("createdDate"));
					map.put(GRTConstants.CREATED_DATE_SORT, "desc");
				}
				return criteria;
			}

			
			/**
			 *
			 * */
			private boolean isBlank(Object obj){
				boolean isBlank = false;
				if(obj == null || "".equals(obj.toString())){
					isBlank = true;
				}
				return isBlank;
			}
			
			private String formatDate(String date){
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				try {
					//dateFormat.parse(date);
					return dateFormat.format(dateFormat.parse(date));
				} catch (ParseException e) {
					e.printStackTrace();
					logger.debug("Invalid Date Format. Please correct the inputs.");
					return "";
				}
			}
			
			 /**
			 * API to get all active alerts.
			 *
			 *
			 * @throws DataAccessException Exception
			 */
			public List<Alert> getActiveAlerts() throws DataAccessException {
			    logger.debug("Entering BaseHibernateDao.getActiveAlerts");
			    List<Alert> alerts = null;
			    try {
			        //String  queryString = "Select alert from Alert alert where alert.endDate >= sysdate and alert.startDate <= sysdate and alert.isExist = '1' order by alert.createdDate desc";
			     
			        Query query = getSessionForGRT().getNamedQuery("getActiveAlerts");

			        alerts = query.list();
			        if (alerts != null) {
			            logger.debug("active alert count: " + alerts.size());
			        }
			    } catch (HibernateException hibEx) {
			        throw new DataAccessException(BaseHibernateDao.class, hibEx
			                .getMessage(), hibEx);
			    }

			    logger.debug("Exiting BaseHibernateDao.getActiveAlerts");

			    return alerts;
			}
			/**
		     * An API to validate passed BPLinkIds and its associated data.
		     *
		     * @param bpLinkId
		     * @return Null, If passed BpLinkId is not valid.
		     * @throws DataAccessException
		     */
		    public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.getBusinessPartner for bpLinkId:" + bpLinkId);
		        BusinessPartner businessPartner = null;

		        try {
		            List<BusinessPartner> bps = this.getBusinessPartners(bpLinkId);
		            if(bps != null && bps.size() > 0) {
		            	businessPartner = bps.get(0);
		            }
		        } catch (Throwable throwable){
		            logger.error("", throwable);
		        } finally {
		        	logger.debug("Exiting BaseHibernateDao.getBusinessPartner for bpLinkId:" + bpLinkId);
		        }
		        return businessPartner;
		    }
		    
		    /**
		     * An API to validate passed BPLinkIds and its associated data.
		     *
		     * @param bpLinkId
		     * @return List of all BP accounts.
		     * @throws DataAccessException
		     */
		    public List<BusinessPartner> getBusinessPartners(String bpLinkId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.getBusinessPartners for bpLinkId:" + bpLinkId);
		        List<BusinessPartner> businessPartners = new ArrayList<BusinessPartner>();
		        String sqlQuery = "";
		        Query query = null;
		        List queryResult = null;
				try {
					//String schema = getCxpUserName().trim();
		        	/**sqlQuery = "select distinct prm_linkid, cb.account_number, bp.name as business_partner_name, street1, city, state, zipcode, ctry.name as country " + 
		        	   				  " from " + schema +".BUSINESS_PARTNER bp join " + schema + ".CUSTOMER_BP cbp on BP.BUSINESS_PARTNER_ID=CBP.BUSINESS_PARTNER_ID " +
		        	   				  " join " + schema +".CUSTOMER c on C.CUSTOMER_ID=cbp.customer_id " +  
		        	   				  " join " + schema +".CUSTOMER cb on c.root_account_id =cb.parent_account_id and C.ACCOUNT_TYPE ='PHN' " + 
		        	   				  " join " + schema +".address a on a.customer_id = cb.customer_id " +   
		        	   				  " join " + schema +".country ctry on ctry.COUNTRY_ID=a.country_id " +
		        	   				  " where CB.STATUS='A' and C.STATUS ='A' and prm_linkid ='" + bpLinkId + "'";
		        	query = getSession(GRTConstants.CXP_DATA_BASE).createSQLQuery(sqlQuery); */
					
		        	query = getSessionForCXP().getNamedQuery("getBusinessPartners1");
		        	query.setString("bpLinkId", bpLinkId);
		        	queryResult = query.list();
		        	logger.debug("1 --> No of rows returned:" + queryResult.size() + " for BPLink Id:" + bpLinkId);
		        	if(queryResult == null || queryResult.size() == 0 ) {
			        /**	sqlQuery = "select distinct prm_linkid, account_number, bp.name as business_partner_name, street1, city, state, zipcode, ctry.name as country " +
			        					  " from " + schema +".BUSINESS_PARTNER bp join " + schema +".CUSTOMER_BP cbp on BP.BUSINESS_PARTNER_ID=CBP.BUSINESS_PARTNER_ID " +
			        					  " join " + schema +".CUSTOMER c on C.CUSTOMER_ID=cbp.customer_id " +
			        					  " left outer join " + schema +".address a on a.customer_id = c.customer_id " +
			        					  " join " + schema +".country ctry on ctry.COUNTRY_ID=a.country_id " +
			        					  " where prm_linkid = '" + bpLinkId + "' and C.ACCOUNT_TYPE ='PHN' and c.STATUS='A' ";
			        	query = getSession(GRTConstants.CXP_DATA_BASE).createSQLQuery(sqlQuery); */
			        	query = getSessionForCXP().getNamedQuery("getBusinessPartners2");
			        	query.setString("bpLinkId", bpLinkId);
			        	queryResult = query.list();
			        	logger.debug("2 --> No of rows returned:" + queryResult.size() + " for BPLink Id:" + bpLinkId);
		        	}
		            for (int i = 0; (queryResult!= null && i < queryResult.size()); i++) {
		                Object[] listItem = (Object[]) queryResult.get(i);
		                BusinessPartner businessPartner = new BusinessPartner();
		                if(StringUtils.isNotEmpty((String)listItem[0])) {
		                	businessPartner.setBpLinkId((String)listItem[0]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[1])) {
		                	businessPartner.setSoldToId((String)listItem[1]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[2])) {
		                	businessPartner.setSoldToName((String)listItem[2]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[3])) {
		                	businessPartner.setStreetAddress((String)listItem[3]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[4])) {
		                	businessPartner.setCity((String)listItem[4]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[5])) {
		                	businessPartner.setState((String)listItem[5]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[6])) {
		                	businessPartner.setZip((String)listItem[6]);
		                }
		                if(StringUtils.isNotEmpty((String)listItem[7])) {
		                	businessPartner.setCountry((String)listItem[7]);
		                }
		                businessPartners.add(businessPartner);
		            }
		        } catch (Throwable throwable){
		            logger.error("", throwable);
		        } finally {
		        	logger.debug("Exiting BaseHibernateDao.getBusinessPartners for bpLinkId:" + bpLinkId);
		        }
		        return businessPartners;
		    }
		    
		    /* Methods for Registration List */
		    /**
		     * @return
		     * Method to get all the registration status available
		     */
		    public List<Status> getRegistrationStatus(){
		    	logger.debug("START :: BaseHibernateDao.getRegistrationStatus");

		    	List<Status> statusList = new ArrayList<Status>();
		    	try{
		    		Criteria  criteriaList = getSessionForGRT().createCriteria(Status.class);
		    		
		    		List<String> list = new ArrayList<String>();
		    		list.add("1002");
		    		list.add("1003");
		    		list.add("1004");
		    		list.add("1005");
		    		list.add("1007");
		    		list.add("1011");
		    		list.add("1008");
		    		
		    		criteriaList.add(Restrictions.in("statusId", list.toArray()));
		    		
		    		criteriaList.addOrder(Order.asc("statusDescription"));
		    		

		    		List<Status> objectList = criteriaList.list();
		    		
		    		Iterator iter = objectList.iterator();
		    		int count =0;
		    		while(iter.hasNext()){
		    			Object obj =  iter.next();
		    			count++;
		    			if (obj.getClass()!=null)
		    				if (obj instanceof Status) {
		    					statusList.add((Status)obj);
		    				} 
		    		}
		    	}catch(Exception e){
		    		logger.debug("ERROR :: getRegistrationStatus");
		    	}
		    	logger.debug("END :: BaseHibernateDao.getRegistrationStatus");
		    	return statusList;
		    }
		    
		    /**
		     * @return
		     *  Method to get the registration types
		     */
		    public List<RegistrationType> getRegistrationTypes(){
		    	logger.debug("START :: BaseHibernateDao.getRegistrationTypes");
		    	List<RegistrationType> regTypesList = new ArrayList<RegistrationType>();
		    	
		    	Criteria  criteriaList = getSessionForGRT().createCriteria(RegistrationType.class);
		    	criteriaList.addOrder(Order.asc("registrationType"));
		    	
		    	List<RegistrationType> objectList = criteriaList.list();
	    		
	    		Iterator iter = objectList.iterator();
	    		int count =0;
	    		while(iter.hasNext()){
	    			Object obj =  iter.next();
	    			count++;
	    			if (obj.getClass()!=null)
	    				if (obj instanceof RegistrationType) {
	    					regTypesList.add((RegistrationType)obj);
	    				} 
	    		}
		    	
	    		logger.debug("END :: BaseHibernateDao.getRegistrationTypes");
		    	return regTypesList;
		    }
		    
		    public SiteRegistration updateEmail(String registrationId, String onsiteEmail) throws DataAccessException{
		        logger.debug("Entering BaseHibernateDao.updateEmail");
		        SiteRegistration registration = null;
		        try {
		        	Session session = getSessionForGRT();
		            session.beginTransaction();
		            registration = (SiteRegistration) getEntity( session, SiteRegistration.class, "registrationId", registrationId);
		            registration.setOnsiteEmail(onsiteEmail);
		            session.saveOrUpdate(registration);
		            session.getTransaction().commit();
		            logger.debug("Exiting BaseHibernateDao.updateEmail");

		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
		        return registration;
		    }
		    
		    /****
		     * *
		     * *Update Technical Registration
		     * *
		     * *GRT 4.0 Changes
		     * *****/
		    public void updateTechnicalRegistration(String technicalRegistrationId)throws DataAccessException
		    {
		    	try{
		    		Session session = getSessionForGRT();
		            session.beginTransaction();
		            
	                TechnicalRegistration registration = (TechnicalRegistration) getEntity(session, TechnicalRegistration.class, "technicalRegistrationId", technicalRegistrationId);
	                Status status = new Status();
                    status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	                registration.setStepBStatus(status);
	                  
	                session.update(registration);
		            session.getTransaction().commit();
		    		
		    		
		    	} catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
		    }
		    
		    
		    /****
		     * *
		     * *Update Technical Registration
		     * *
		     * *GRT 4.0 Changes
		     * *****/
		    public void updateSiteRegistrationCompleteStatus(String registrationId,StatusEnum status)throws DataAccessException
		    {
		    	try{
		    		Session session = getSessionForGRT();
		            session.beginTransaction();
		            
	                SiteRegistration registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
	                Criteria criteria =  session.createCriteria(Status.class);
		            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
		            Status s = (Status)  criteria.uniqueResult();
		          
	                registration.setTechRegStatus(s);
	                  
	                session.update(registration);
		            session.getTransaction().commit();
		    		
		    		
		    	} catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
		    }
		    
		    /**
		     * Method to update the site registration  status by the given registrationId, processStepId and statusId.
		     *
		     * @param registrationId string
		     * @param statusId
		     * @return registration SiteRegistration
		     */
		    public SiteRegistration updateSiteRegistrationBySuperUser(String registrationId, String statusId, String userId) throws DataAccessException {
		        logger.debug("Entering BaseHibernateDao.updateSiteRegistrationBySuperUser()");
		        SiteRegistration registration = null;
		        try {

		            Session session = getSessionForGRT();
		            session.beginTransaction();
	                  registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
	                  //logger.debug("Loaded siteregistration"+registration.getFinalValidationStatus().getStatusId());
	                  //&& StringUtils.isNotEmpty(registration.getTypeOfImplementation())&& registration.getTypeOfImplementation().equals("5")
	                  logger.debug("IB:"+registration.getInstallBaseStatus()+"    TOB:"+registration.getTechRegStatus()+"   EQR:"+registration.getFinalValidationStatus());
	                  logger.debug("IB Sub Status:"+registration.getInstallBaseSubStatus());
	                  if(registration.getRegistrationType() != null){
	                	  logger.debug("Registration Type:"+registration.getRegistrationType().getRegistrationType());
	                  }
	                  if(registration != null ){
	                        if(registration.getInstallBaseStatus() != null && StringUtils.isNotEmpty(registration.getInstallBaseStatus().getStatusId())
	                        		&& !registration.getInstallBaseStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
	                        		&& !registration.getInstallBaseStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())
	                        		&& !registration.getInstallBaseStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())   ){
	                              Status status = new Status();
	                              status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	                              registration.setInstallBaseStatus(status);
	                              // Added to capture overridden Date and User
	                              registration.setIbCompletedDate(new Date());
	                              registration.setIbOverriddenBy(userId);
	                              registration.setTempProcessStep(ProcessStepEnum.INSTALL_BASE_CREATION);
	                              this.updatePipelineTransactionOnRegistrationOverride(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION);
	                              logger.debug("IN first loop ");
	                        } else if(registration.getTechRegStatus() != null && StringUtils.isNotEmpty(registration.getTechRegStatus().getStatusId())
	                        		&& !registration.getTechRegStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
	                        		//&& !registration.getTechRegStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())
	                        		&& !registration.getTechRegStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())
	                        		&& (StatusEnum.SAVED.getStatusId().equals(registration.getTechRegStatus().getStatusId())
	                        				|| StatusEnum.AWAITINGINFO.getStatusId().equals(registration.getTechRegStatus().getStatusId())
	                        				|| StatusEnum.INPROCESS.getStatusId().equals(registration.getTechRegStatus().getStatusId())
	                        				|| (StatusEnum.NOTINITIATED.getStatusId().equals(registration.getTechRegStatus().getStatusId())
	                        			&& (registration.getRegistrationType() != null
	                        					&& (RegistrationTypeEnum.IPOFFICE.getRegistrationID().equalsIgnoreCase(registration.getRegistrationType().getRegistrationId())
	                        					|| RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID().equalsIgnoreCase(registration.getRegistrationType().getRegistrationId())))
	                        			&& ((registration.getInstallBaseStatus() != null && StatusEnum.COMPLETED.getStatusId().equalsIgnoreCase(registration.getInstallBaseStatus().getStatusId()))
	                        					|| (registration.getInstallBaseSubStatus() != null && StatusEnum.SAPCOMPLETED.getStatusId().equalsIgnoreCase(registration.getInstallBaseSubStatus().getStatusId())))))){
	                              Status status = new Status();
	                              status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	                              registration.setTechRegStatus(status);
	                              // Added to capture overridden Date and User
	                              registration.setTobCompletedDate(new Date());
	                              registration.setTobOverriddenBy(userId);
	                              registration.setTempProcessStep(ProcessStepEnum.TECHNICAL_REGISTRATION);
	                              //this.updatePipelineTransactionOnRegistrationOverride(registrationId, ProcessStepEnum.TECHNICAL_REGISTRATION);
	                              
	                              //GRT 4.0 Changes
		                            List<TechnicalRegistration> technicalRegistrations=this.getTechnicalRetestRegistrationListByregistrationId(registrationId);
		                      		for(TechnicalRegistration techReg:technicalRegistrations)
		                      		{
		                      			if(techReg.getStepBStatus()!=null)
		                      			{
		                      				this.updateTechnicalRegistration(techReg.getTechnicalRegistrationId());
		                      			}
		                      		}
	                      		
	                      		
	                              logger.debug("IN second loop ");
	                        } else if(registration.getFinalValidationStatus() != null && StringUtils.isNotEmpty(registration.getFinalValidationStatus().getStatusId())
	                        		&& !registration.getFinalValidationStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
	                        		//&& !registration.getFinalValidationStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())
	                        		&& !registration.getFinalValidationStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())
	                        		&& (StatusEnum.SAVED.getStatusId().equals(registration.getFinalValidationStatus().getStatusId())
		                				|| StatusEnum.AWAITINGINFO.getStatusId().equals(registration.getFinalValidationStatus().getStatusId())
		                				|| StatusEnum.INPROCESS.getStatusId().equals(registration.getFinalValidationStatus().getStatusId())
		                				|| (StatusEnum.NOTINITIATED.getStatusId().equals(registration.getFinalValidationStatus().getStatusId())
	                        				&& registration.getRegistrationType() != null
	                        				&& RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID().equalsIgnoreCase(registration.getRegistrationType().getRegistrationId())))){
	                              Status status = new Status();
	                              status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	                              registration.setFinalValidationStatus(status);
	                              // Added to capture overridden Date and User
	                              registration.setEqrCompletedDate(new Date());
	                              registration.setEqrOverriddenBy(userId);
	                              registration.setTempProcessStep(ProcessStepEnum.FINAL_RECORD_VALIDATION);
	                              this.updatePipelineTransactionOnRegistrationOverride(registrationId, ProcessStepEnum.FINAL_RECORD_VALIDATION);
	                              logger.debug("IN third loop ");
	                        } else if(registration.getEqrMoveStatus() != null && StringUtils.isNotEmpty(registration.getEqrMoveStatus().getStatusId())
	                        		&& !registration.getFinalValidationStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())
	                        		//&& !registration.getFinalValidationStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())
	                        		&& !registration.getEqrMoveStatus().getStatusId().equals(StatusEnum.CANCELLED.getStatusId())
	                        		&& (StatusEnum.SAVED.getStatusId().equals(registration.getEqrMoveStatus().getStatusId())
		                				|| StatusEnum.AWAITINGINFO.getStatusId().equals(registration.getEqrMoveStatus().getStatusId())
		                				|| StatusEnum.INPROCESS.getStatusId().equals(registration.getEqrMoveStatus().getStatusId())
		                				|| (StatusEnum.NOTINITIATED.getStatusId().equals(registration.getEqrMoveStatus().getStatusId())
	                        				&& registration.getRegistrationType() != null
	                        				&& RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID().equalsIgnoreCase(registration.getRegistrationType().getRegistrationId())))){
	                              Status status = new Status();
	                              status.setStatusId(StatusEnum.COMPLETED.getStatusId());
	                              registration.setEqrMoveStatus(status);
	                              // Added to capture overridden Date and User
	                              registration.setEqrMoveCompletedDate(new Date());
	                              //registration.setEqrOverriddenBy(userId);
	                              registration.setTempProcessStep(ProcessStepEnum.EQUIPMENT_MOVE);
	                              this.updatePipelineTransactionOnRegistrationOverride(registrationId, ProcessStepEnum.EQUIPMENT_MOVE);
	                              logger.debug("IN fourth loop ");
	                        }
	                  } else {
	                        Criteria criteria =  session.createCriteria(Status.class);
	                        criteria.add(Restrictions.eq("statusId", statusId));
	                        Status s = (Status)  criteria.uniqueResult();
	                        registration.setStatus(s);
	                        logger.debug("IN else loop ");
	                  }
		            session.update(registration);
		            session.getTransaction().commit();

		        } catch (HibernateException hibEx) {
		            throw new DataAccessException(BaseHibernateDao.class, hibEx
		                    .getMessage(), hibEx);
		        }
		        logger.debug("Exiting BaseHibernateDao.updateSiteRegistrationBySuperUser()");

		        return registration;
		    }
		    
		    public void updatePipelineTransactionOnRegistrationOverride(String registrationId, ProcessStepEnum processStep) throws DataAccessException{
		    	logger.debug("Starting BaseHibernateDao.updatePipelineTransactionOnRegistrationOverride");
		    	Query sqlQuery = null;
		    	Transaction transaction = null;
		    	try {
			    //	String query = "UPDATE PIPELINE_SAP_TRANSACTIONS SET PROCESSED=1 WHERE REGISTRATION_ID = :registartionId and ACTION = :orderType";
			    	transaction = getSessionForGRT().beginTransaction();
			    	sqlQuery = getSessionForGRT().getNamedQuery("updatePipelineTransactionOnRegistrationOverride");
			        sqlQuery.setString("registartionId", registrationId);
			        if (processStep !=null) {
		        		if (processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
		        			sqlQuery.setString("orderType", GRTConstants.TECH_ORDER_TYPE_IB);
		        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
		        			sqlQuery.setString("orderType", GRTConstants.TECH_ORDER_TYPE_FV);
		        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())){
		        			sqlQuery.setString("orderType", GRTConstants.TECH_ORDER_TYPE_EM);
		        		}
		        		/*else if (processStep.getProcessStepId().equals(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId())){
		        			sqlQuery.setString("orderType", GRTConstants.TECH_ORDER_TYPE_TR);
		        		}*/
		            } else {
		            	sqlQuery.setString("orderType", "");
		            }
			        sqlQuery.executeUpdate();
			        transaction.commit();
			    } catch (HibernateException hibEx) {
					logger.error("", hibEx);
					transaction.rollback();
					throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
				} catch (Throwable throwable) {
					logger.error("", throwable);
					transaction.rollback();
					throw new DataAccessException(BaseHibernateDao.class, throwable.getMessage(), throwable);
				} finally {
					logger.debug("Ending BaseHibernateDao.updatePipelineTransactionOnRegistrationOverride");
				}
		    }
		    

		    public String getExistingRegistrationBySeid(String seId) throws DataAccessException {
		    		logger.debug("Entering getExistingRegistration(String soldToId, String seId)");
		 	   	String regId = "";
		 	   
		 	   	try {
		 	   		Query query=getSessionForGRT().getNamedQuery("getExistingRegistration");
		    			query.setParameter("seId", seId);
		    			List<String> resultSet = query.list();
		    			
		    			if (!resultSet.isEmpty()) regId = resultSet.get(0); 
		    	   		
		    			logger.debug("After - Query Execution:"+resultSet.size());
		    		}  catch (HibernateException hibEx) {
		    			logger.error("", hibEx);
		    			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
		    		} catch (Exception ex) {
		    			logger.error("", ex);
		    			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
		    		} finally {
		    			logger.debug("Exiting getExistingRegistration(String soldToId, String seId)");
		    		}
		 	   
		 	   return regId;
		    }
		    
	 public TechnicalRegistration getExistingTechReg(String seId, String soldTo, String materialCode) throws DataAccessException {
	    		logger.debug("Entering getExistingTechReg(String soldToId, String seId)");
	    	TechnicalRegistration technicalRegistration = new TechnicalRegistration();
	 	   	try {
	 	   			Query query=getSessionForGRT().getNamedQuery("getExistingTechReg");
	    			query.setParameter("seId", seId);
	    			query.setParameter("soldTo", soldTo);
	    			query.setParameter("materialCode", materialCode);
	    			List<Object[]> list = query.list();
	    			
	    			if( list!=null && list.size() > 0 ){
	    				Object [] objArr = list.get(0);
	    				if( objArr[0]!=null ){
	    					String techRegId = objArr[0].toString();
	    					Criteria criteria =  getSessionForGRT().createCriteria(TechnicalRegistration.class);
	    					criteria.add(Restrictions.eq("technicalRegistrationId", techRegId));
	    					technicalRegistration = (TechnicalRegistration)  criteria.uniqueResult();
	    					if( technicalRegistration!=null ){
	    						logger.debug("Technical Registration is present : "+technicalRegistration.getTechnicalRegistrationId());
	    					}else{
	    						logger.debug("Technical Registration is null");
	    					}
	    				}
	    			 /* Object [] objArr = list.get(0);
	    			  //Set Sold To
	    			  if( objArr[0]!=null ){
			    		   technicalRegistration.setSoldToId(objArr[0].toString());
			    		   logger.debug(objArr[0].toString());
			    	   }
	    			   //Set Cm Main SEID
			    	   if( objArr[1]!=null ){
			    		   technicalRegistration.setCmMainSeid(objArr[1].toString());
			    		   logger.debug(objArr[1].toString());
			    	   }
			    	   //Set SID
			    	   if( objArr[2]!=null ){
			    		   technicalRegistration.setSid(objArr[2].toString());
			    		   logger.debug(objArr[2].toString());
			    	   }
			    	   //Set MID
			    	   if( objArr[3]!=null ){
			    		   technicalRegistration.setMid(objArr[3].toString());
			    		   logger.debug(objArr[3].toString());
			    	   }
			    	   //Set Remote Device Type
			    	   if( objArr[4]!=null ){
			    		   technicalRegistration.setRemoteDeviceType(objArr[4].toString());
			    		   logger.debug(objArr[4].toString());
			    	   }
			    	   //Set Technical Registration Id
			    	   if( objArr[5]!=null ){
			    		   technicalRegistration.setTechnicalRegistrationId(objArr[5].toString());
			    		   logger.debug(objArr[5].toString());
			    	   }
			    	   //Set CM Main
			    	   if( objArr[6]!=null ){
			    		   if( objArr[6].toString().equals("0") ){
			    			   technicalRegistration.setCmMain(false);
			    		   }else{
			    			   technicalRegistration.setCmMain(true);
			    		   }
			    	   }
			    	   //Set CM Product
			    	   if( objArr[7]!=null ){
			    		   if( objArr[7].toString().equals("0") ){
			    			   technicalRegistration.setCmProduct(false);
			    		   }else{
			    			   technicalRegistration.setCmProduct(true);
			    		   }
			    	   }
			    	   //Set CM Sold-To
			    	   if( objArr[8]!=null ){
			    		   technicalRegistration.setCmMainSoldToId(objArr[8].toString());
			    	   }
			    	   //Set CM Main Sid
			    	   if( objArr[9]!=null ){
			    		   technicalRegistration.setCmMainSid(objArr[9].toString());
			    	   }*/
	    		       logger.debug("After - Query Execution:"+list.size());
	    			}
	    		}  catch (HibernateException hibEx) {
	    			logger.error("", hibEx);
	    			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
	    		} catch (Exception ex) {
	    			logger.error("", ex);
	    			throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, ex.getMessage(), ex);
	    		} finally {
	    			logger.debug("Exiting getExistingTechReg(String soldToId, String seId)");
	    		}
	 	   
	 	   return technicalRegistration;
	    }
	    
	 
	 public String saveTechnicalOrderForSALGateway (TechnicalOrder technicalOrder) throws DataAccessException {
		 try {
			 Session session = getSessionForGRT();
			 session.beginTransaction();
			 session.saveOrUpdate(technicalOrder);
			 session.getTransaction().commit();
		 } catch (HibernateException hibEx) {
			 logger.error("Error in BaseHibernateDao : saveTechnicalOrderForSALGateway : "
					 + hibEx.getMessage());
			 throw new DataAccessException(BaseHibernateDao.class, hibEx.getMessage(), hibEx);
		 }
		 return technicalOrder.getOrderId();
	 }
		    
		    public static void main(String[] args) throws DataAccessException {
		    	ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		    	BaseHibernateDao baseHibernateDao = (BaseHibernateDao) context.getBean("baseHibernateDao");
		    	List<String> materialCodes = new ArrayList<String>();
		    	
		    	baseHibernateDao.getExistingTechReg("(628)085-1717", "0003427474", "700476005");
		    	
		    	/*
		    	materialCodes.add("408278943D");
				List<MaterialExclusion> list = baseHibernateDao.getDAndBlueMaterialCode(materialCodes);
		    	System.out.println("List size = " + list.size());
		    	*/
		    	
		    	/*
		  		    	materialCodes.add("408278943D");
		    	Map<String,String> map = baseHibernateDao.getMaterialCodeDesc(materialCodes);
		    	System.out.println("Map size = " + map.size());
		    	*/
		    	
		    	//boolean b = true;
		    	//System.out.println(BooleanUtils.toBoolean(1));
		    	//System.out.println(StringUtils.isEmpty(null)+" "+StringUtils.isEmpty(" "));
		    	/*materialCodes = new ArrayList<String>();
		    	materialCodes.add("167391");
		    	materialCodes.add("216840");*/
		    	//baseHibernateDao.getExistingTechReg("(628)008-7779","0002726514");
		    	/*List<TechnicalRegistration> resultSet = new ArrayList<TechnicalRegistration>();
		    	TechnicalRegistration technicalRegistration = new TechnicalRegistration();
		    	while(it.hasNext()) {
			    	   
			    	   Object[] objArr = (Object[]) it.next();
			    	   technicalRegistration = new TechnicalRegistration();
			    	   epnSurveyStr = portType.startEPNSurveyProcess(cmSoldTo, cmMainSEID,
		    					"grt@avaya.com", technicalRegistration.getTechnicalRegistrationId(),
		    					systemIdentifier,
		    					cmMainSID, cmRemoteSEID, cmRemoteMID, cmRemoteDeviceType, move);
			    	   if( objArr[0]!=null ){
			    		   technicalRegistration.setSoldToId(objArr[0].toString());
			    		   System.out.println(objArr[0].toString());
			    	   }
			    	   if( objArr[1]!=null ){
			    		   technicalRegistration.setCmMainSeid(objArr[1].toString());
			    		   System.out.println(objArr[1].toString());
			    	   }
			    	   if( objArr[2]!=null ){
			    		   technicalRegistration.setSid(objArr[2].toString());
			    		   System.out.println(objArr[2].toString());
			    	   }
			    	   if( objArr[3]!=null ){
			    		   technicalRegistration.setMid(objArr[3].toString());
			    		   System.out.println(objArr[3].toString());
			    	   }
			    	   if( objArr[4]!=null ){
			    		   technicalRegistration.setRemoteDeviceType(objArr[4].toString());
			    		   System.out.println(objArr[4].toString());
			    	   }
			    	   if( objArr[5]!=null ){
			    		   technicalRegistration.setTechnicalRegistrationId(objArr[4].toString());
			    		   System.out.println(objArr[5].toString());
			    	   }
			    	   resultSet.add(technicalRegistration);
			    }*/
		    	//TechnicalRegistration tr = (TechnicalRegistration)list.get(0);
		    	//System.out.println("New List size = " + list.size()+" "+ tr.getTechnicalRegistrationId());
			}
		    
		    public SiteRegistration refreshSiteRegistration(SiteRegistration siteRegistration) throws DataAccessException {
		    	logger.debug("Starting for BaseHibernateDao.updateSiteRegistrationStatus Registration ID [" + siteRegistration.getRegistrationId() + "] and status ID to update [");
		        Transaction transaction = null;
		 		SiteRegistration registration = null;
		        try {
		            Session session = getSessionForGRT();
		 			transaction = session.beginTransaction();
					registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());
					//session.refresh(registration);
					session.saveOrUpdate(registration);
		            logger.debug("Committing change into database ...");
		            transaction.commit();
		        } catch (HibernateException hibEx) {
					transaction.rollback();
					logger.debug("Error in updateSiteRegistrationStatus");
					hibEx.printStackTrace();
					throw new DataAccessException(BaseHibernateDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					transaction.rollback();
					logger.debug("Error in updateSiteRegistrationStatus");
					ex.printStackTrace();
					throw new DataAccessException(BaseHibernateDao.class, ex
							.getMessage(), ex);
				}
		        logger.debug("Exiting BaseHibernateDao.updateSiteRegistrationStatus");
		        return registration;
		    }
		    
		    /**
			 * Save or update the hibernate object.
			 * 
			 * @param object
			 *            Object
			 * @throws DataAccessException
			 *             custom exception
			 */
			protected final void saveOrUpdate(Object object) throws DataAccessException {
				Session session = getSessionForGRT();
				try {
					session.beginTransaction();
					session.saveOrUpdate(object);
					session.beginTransaction().commit();
				} catch (HibernateException hibEx) {
					session.beginTransaction().rollback();
					
				} catch (Exception ex) {
					session.beginTransaction().rollback();
					
				}

			}
			
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
		    
		    
		    public int updateSiteRegistrationStatusOnRegId(String registrationId, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException {
				logger.debug("Entering InstallBaseDao : updateSiteRegistrationStatusOnRegId");
				int flag = 0;
				SiteRegistration registration = null;
		        try {
		            Session session = getSessionForGRT();
		            session.beginTransaction();
					registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
		            //registration.setSoldToBox(soldToBox);
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
		     * @param siteRegistration
		     * @param isSuperUser
		     * @return List<TechnicalRegistration>
		     * @throws DataAccessException
		     * GRT 4.0 Retest Functionality : Method to fetch data for Retest records
		     */
		    public List<TechnicalRegistration> fetchRetestRecords(SiteRegistration siteRegistration, 
		    		boolean isSuperUser) throws DataAccessException {
		    	List<TechnicalRegistration> objectList = new ArrayList<TechnicalRegistration>();
		    	try {
		    		Session session = getSessionForGRT();

		    		List<String> orderList = new ArrayList<String>();
		    		Set<TechnicalOrder> technicalOrdersSet = siteRegistration.getTechnicalOrders();

		    		if(technicalOrdersSet != null && technicalOrdersSet.size() >0) {
		    			for (TechnicalOrder technicalOrder : technicalOrdersSet) {
		    				if (GRTConstants.TECH_ORDER_TYPE_TR_RETEST.equalsIgnoreCase(technicalOrder.getOrderType()))
		    					orderList.add(technicalOrder.getOrderId());
		    			}

		    			if (!orderList.isEmpty()) {
		    				Criterion queryFilterMap = Restrictions.in("technicalOrder.orderId", orderList);			   

		    				Criteria criteria =  session.createCriteria(TechnicalRegistration.class);           
		    				criteria.add(queryFilterMap);
		    				objectList = criteria.list();
		    			}
		    		}
		    	}
		    	catch (HibernateException hibEx) {
		    		throw new DataAccessException(TechnicalOnBoardingDaoImpl.class, hibEx.getMessage(), hibEx);
		    	}
		    	return objectList;
		    }
		    
		   /**
		    * *•	Automating test alarming and connectivity (BR-F.009)
		    * *
		    * *******/
		    public List<String> getEntitledForAlarming(List<String> lst,String soldTOId)
		    {
		    	List<String> materialCodeList=new ArrayList<String>();
		    	Query query=null;
		    	query=getSessionForSiebel().getNamedQuery("getEntitledForAlarming");
	    		query.setParameterList("materialCodes", lst);
	    		query.setString("soldToParam", soldTOId);
	    		List<Object[]> resultSet = query.list(); 
	    		
	    		try{
	    			if(resultSet != null && resultSet.size() > 0){
			    		for (Object[] object : resultSet) {
			    			materialCodeList.add(object[0].toString());
			    		}
	    			}
	    		}catch(Exception eX){
	    			
	    		}
		    	
		    	return materialCodeList;
		    }

		    public List<String> getPIEPollableSeCodes() throws DataAccessException {
		    	logger.debug("Entering BaseHibernateDao.getPIEPollableSeCodes");
		    	List<String> piePollableSeCodes = new ArrayList<String>();
		         try {
		        //	 String sqlQuery = "SELECT SE_CODE FROM PIE_POLLABLE_SOLUTIONS";
		        //     Query query = getSessionForGRT().createSQLQuery(sqlQuery);
		             Query query = getSessionForGRT().getNamedQuery("getPIEPollableSeCodes");
		             piePollableSeCodes = query.list();
		         }catch (HibernateException hibEx) {
		        	 logger.error("", hibEx);
		            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
		                    .getMessage(), hibEx);
		        } finally {
		        	logger.debug("Entering BaseHibernateDao.getPIEPollableSeCodes");
		        }
		        return piePollableSeCodes;
		    }
		    
		    public int saveSiteListWithExpElem(SiteList siteList){

		  		logger.debug("Entering BaseHibernateDao.saveSiteListWithExpElem:" + siteList.getId());

		  		//SiteList siteListLocal = null;
		  		/*try {
		  			Session session = getSessionForGRT();
		  			Transaction transaction = session.beginTransaction();
		  			Criteria criteria =  session.createCriteria(SiteList.class);
		  			criteria.add(Restrictions.eq("id", siteList.getId()));
		  			siteListLocal = (SiteList)  criteria.uniqueResult();
		  			
		  			if( siteListLocal.getExplodedSolutionElements()==null ){
		  				//Save the child elements
		  				java.util.Set<ExpandedSolutionElement> resultsSet = getExpSolutionElementsSetForSalMigration(siteList, siteList.getExpSolutionElements());
		  				if( siteList.getExpSolutionElements()!=null ){
		  					List<ExpandedSolutionElement> listExp = siteList.getExpSolutionElements();
		  					if(!listExp.isEmpty()){
		  						for(ExpandedSolutionElement expandedSolutionElement : listExp){
		  							expandedSolutionElement.setSiteList(siteList);
		  							resultsSet.add(expandedSolutionElement);
		  						}
		  					}
		  				}
		  				siteListLocal.setExplodedSolutionElements(resultsSet);
		  			}
		  			
		  			session.saveOrUpdate(siteListLocal);
		  			transaction.commit();
		  		} catch (HibernateException hibEx) {
		  			logger.error(hibEx);
		  			return  0;
		  		} catch (Exception ex) {
		          	logger.error(ex);
		          	return  0;
		  		} finally {
		  			logger.debug("Exiting BaseHibernateDao.saveSiteListWithExpElem");
		  		}*/
		  		
		  		Session session =null;
				try {
					session = getSessionForGRT();
					session.beginTransaction();
					session.saveOrUpdate(siteList);
			        session.getTransaction().commit();
				} catch (HibernateException hibEx) {
					session.close();
					session = getSessionForGRT();
					session.beginTransaction();
			        session.merge(siteList);
			        session.getTransaction().commit();
				}
		  	
			   return  1;
		   }
		    
		    protected java.util.Set<ExpandedSolutionElement> getExpSolutionElementsSetForSalMigration(SiteList siteList, List<ExpandedSolutionElement> expandedSolutionElements){
		    	java.util.Set<ExpandedSolutionElement> resultsSet = new HashSet<ExpandedSolutionElement>();
		    	if(!expandedSolutionElements.isEmpty()){
		    		for(ExpandedSolutionElement expandedSolutionElement : expandedSolutionElements){
		    			expandedSolutionElement.setSiteList(siteList);
		    			resultsSet.add(expandedSolutionElement);
		    		}
		    	}
		    	return resultsSet;
		    }
		    
		    /**
		     * Save Exploded elements ART response to database.
		     * @param technicalRegistrationToPersist
		     */
		    private void saveExploadedElementsForSL(SiteList siteList) {
		    	logger.info("Inside BaseHibernateDao.saveExploadedElementsForSL");
		    	if(siteList.getExplodedSolutionElements() != null && siteList.getExplodedSolutionElements().size() > 0) {
		    		Session session = getSessionForGRT();
		    		Transaction tx = session.beginTransaction();
		    		for(ExpandedSolutionElement expElement : siteList.getExplodedSolutionElements()) {
		    			try {
		    				logger.debug("Updating Expanded Solution Element Id record: "+expElement.getExpSolnElemntId());
		    				logger.debug("Values to update - Retest Status - "+expElement.getRetestStatus()+", Art Response Code - "+expElement.getArtRespCode() + ", Art Resp Msg - "+expElement.getArtRespMsg());
		    				Criteria criteria = session.createCriteria(ExpandedSolutionElement.class);
			    			criteria.add(Restrictions.eq("expSolnElemntId", expElement.getExpSolnElemntId()));
			    			ExpandedSolutionElement element = (ExpandedSolutionElement)  criteria.uniqueResult();
			    			element.setRetestStatus(expElement.getRetestStatus());
			    			element.setArtRespCode(expElement.getArtRespCode());
			    			if(expElement.getArtRespMsg() != null && expElement.getArtRespMsg().length() > 200) {
			    				element.setArtRespMsg(expElement.getArtRespMsg().substring(0, 199));
			    			} else {
			    				element.setArtRespMsg(expElement.getArtRespMsg());
			    			}
			    			session.saveOrUpdate(element);
		    			} catch(Exception e) {
		    				logger.error("Exception during updating Expanded solution element "+e);
		    			}
		    		}
		    		tx.commit();
		    	}
		    	logger.info("Exiting BaseHibernateDao.saveExploadedElementsForSL");
		    }
	
    public String getIPOAssetsMaximumRelease(String soldToNumber, List<String> inClause) throws DataAccessException {
    	logger.debug("Entering getIPOAssetsMaximumRelease for soldToNumber:" + soldToNumber + " and inClause:" + inClause);
    	String release = "";
    	try {
	    	/*String sqlQuery = "select max(X_release) from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_asset_x x " 
	    					  + " where orgext.loc = :soldToNumberParam and orgext.row_id = sasset.owner_accnt_id" 
	    					  + " and sasset.row_id = x.par_row_id and sasset.status_cd <> 'Inactive' and sasset.x_se_cd in " 
	    					  + inClause +" and length(X_release) = 3 and sasset.X_release is not null and x.attrib_03 is not null";*/
    		//String sqlQuery = "select max(X_release) from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_asset_x x where orgext.loc = :soldToNumberParam and orgext.row_id = sasset.owner_accnt_id and sasset.row_id = x.par_row_id and sasset.status_cd <> 'Inactive' and sasset.x_se_cd in (:seCodeInClause) and length(X_release) = 3 and sasset.X_release is not null and x.attrib_03 is not null";
			
			Query query = getSessionForSiebel().getNamedQuery("getReleaseForSEIdsOnAccount");//.setCacheable(true);
			query.setString("soldToNumberParam", soldToNumber);
			query.setParameterList("seCodeInClause", inClause);
			release = (String)query.uniqueResult();
    	} catch(Throwable throwable) {
    		logger.error("", throwable);
    	} finally {
    		logger.debug("Exiting getIPOAssetsMaximumRelease:" + release + " for soldToNumber:" + soldToNumber + " and inClause:" + inClause);
    	}
		return release;
	}
    
    //Method for fetching registration details on graph click
    @SuppressWarnings("unchecked")
	public PaginationForSiteRegistration getRegListForGraph(String userId, String graphView) throws DataAccessException{
		PaginationForSiteRegistration dto = new PaginationForSiteRegistration();
		List<SiteRegistration> registrationSummaryList = new ArrayList<SiteRegistration>();
		List<BPAccountTempAccess> bpAccess = null;
		int size = 0;
		logger.debug("entering Method BaseHibernateDao.getRegListFromDB");
		try {
			
			List<SiteRegistration> list = new ArrayList<SiteRegistration>();
			if( GRTConstants.NOTINITIATED_VIEW.equalsIgnoreCase(graphView) ){
				Criteria criteria = getSessionForGRT().createCriteria(SiteRegistration.class); 
				Junction conditionGroup = Restrictions.disjunction();
				conditionGroup.add(Restrictions.eq("finalValidationStatus.statusId","1005")).add(Restrictions.eq("installBaseStatus.statusId", "1005")).add(Restrictions.eq("techRegStatus.statusId", "1005"));
				criteria.add(conditionGroup);
				criteria.add(Restrictions.eq("registrationType.registrationId","RTO6"));
				criteria.add(Restrictions.eq("userName",userId));
				list = criteria.list();
				registrationSummaryList.addAll(list);
			}
			
			List<SiteRegistration> list1 = new ArrayList<SiteRegistration>();
			if( GRTConstants.INPROCESS_VIEW.equalsIgnoreCase(graphView) ){
				Criteria criteria1 = getSessionForGRT().createCriteria(SiteRegistration.class); 
				Junction conditionGroup1 = Restrictions.disjunction();
				conditionGroup1.add(Restrictions.eq("finalValidationStatus.statusId","1002")).add(Restrictions.eq("installBaseStatus.statusId", "1002")).add(Restrictions.eq("techRegStatus.statusId", "1002"))
				.add(Restrictions.eq("eqrMoveStatus.statusId", "1002"));
				criteria1.add(conditionGroup1);
				criteria1.add(Restrictions.eq("userName",userId));
				list1 = criteria1.list();
				registrationSummaryList.addAll(list1);
			}
			
			List<SiteRegistration> list2 = new ArrayList<SiteRegistration>();
			if( GRTConstants.AWAITING_VIEW.equalsIgnoreCase(graphView) ){
				Criteria criteria2 = getSessionForGRT().createCriteria(SiteRegistration.class); 
				Junction conditionGroup2 = Restrictions.disjunction();
				conditionGroup2.add(Restrictions.eq("finalValidationStatus.statusId","1003")).add(Restrictions.eq("installBaseStatus.statusId", "1003")).add(Restrictions.eq("techRegStatus.statusId", "1003"))
				.add(Restrictions.eq("eqrMoveStatus.statusId", "1003"));
				criteria2.add(conditionGroup2);
				criteria2.add(Restrictions.eq("userName",userId));
				list2 = criteria2.list();
				registrationSummaryList.addAll(list2);
			}
			
			List<SiteRegistration> list3 = new ArrayList<SiteRegistration>();
			if( GRTConstants.SAVED_VIEW.equalsIgnoreCase(graphView) ){
				Criteria criteria3 = getSessionForGRT().createCriteria(SiteRegistration.class); 
				Junction conditionGroup3 = Restrictions.disjunction();
				conditionGroup3.add(Restrictions.eq("finalValidationStatus.statusId","1007")).add(Restrictions.eq("installBaseStatus.statusId", "1007")).add(Restrictions.eq("techRegStatus.statusId", "1007"))
				.add(Restrictions.eq("eqrMoveStatus.statusId", "1007"));
				criteria3.add(conditionGroup3);
				criteria3.add(Restrictions.eq("userName",userId));
				list3 = criteria3.list();
				registrationSummaryList.addAll(list3);
			}
			dto.setMaxSize(registrationSummaryList.size());
		} catch (HibernateException hibEx) {
			logger.error(" DEBUG : method getRegListFromDB : hibEx stacktrace print : ",hibEx);
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception hibEx) {
			logger.error(" DEBUG : method getRegListFromDB : exc stacktrace print : ",hibEx);
			throw new DataAccessException(BaseHibernateDao.class, hibEx
					.getMessage(), hibEx);
		}
		logger.debug("exiting Method BaseHibernateDao.getRegListFromDB");
		dto.setList(registrationSummaryList);
		return dto;
	
    }
    
    /**
     * Get Chat Configuration : Chat Phase 1 : Chat on/off configuration
     * @return Configuration Map
     */
    public Map<String,String> getChatConfiguration() throws DataAccessException {
    	logger.debug("BaseHibernateDao.getChatConfiguration()");
    	Map<String,String> chatConfigurationMap = new HashMap<String, String>();
         try {
        	//String sqlQuery = "select NAME,VALUE from GRT_CONFIGURATION where name like 'ENABLE_CHAT_FOR%'";

        	//Query query = getSessionForGRT().createSQLQuery(sqlQuery);
        	 Query query=getSessionForGRT().getNamedQuery("getGrtChatConfig");
             List queryResult = query.list();
             for (int i = 0; i < queryResult.size(); i++) {
                 Object[] listItem = (Object[]) queryResult.get(i);
                 chatConfigurationMap.put((String) listItem[0], (String) listItem[1]);
             }
         }catch (HibernateException hibEx) {
            throw new DataAccessException(BaseHibernateDao.class, hibEx
                    .getMessage(), hibEx);
        }
        return chatConfigurationMap;
    }
}
