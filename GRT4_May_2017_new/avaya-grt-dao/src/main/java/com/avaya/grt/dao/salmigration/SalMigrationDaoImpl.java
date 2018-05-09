package com.avaya.grt.dao.salmigration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.util.DataAccessException;


public class SalMigrationDaoImpl extends BaseHibernateDao implements SalMigrationDao{
	private static final Logger logger = Logger.getLogger(SalMigrationDaoImpl.class);
	private Session siebelSession = null;
	private static Map<String, CompatibilityMatrix> compatibilityMatrixMap = null;
	
    public List<Object[]> getSALMigrationEligibleAssets(String soldToId) throws DataAccessException {
    	logger.debug("Entering SiebelDao : getSALMigrationEligibleAssets");
    	List<Object[]> resultSet;
		try {
			Query query = getSessionForSiebel().getNamedQuery("getSALMigrationEligibleAssets");
			//TODO: Shall we discard assets with V00328?
	       /* String sqlQuery ="select * from (select prodint.name material_code, prodint.alias_name material_description, prod_ln.name product_line,"
	        		+ " sasset.x_se_cd, sasset.x_seid, sasset.X_ALRM_ID, SASSET.X_RFA_SID, SASSET.X_RFA_MID, s_asset_xm.x_ip_address"
	        		+ " from siebel.s_org_ext orgext, siebel.s_asset sasset, siebel.s_prod_int prodint, siebel.s_prod_ln prod_ln,"
	        		+ " (select par_row_id, x_ip_address from siebel.s_asset_xm where type='IPAddr' and x_subtype='Customer') s_asset_xm"
	        		+ " where orgext.loc = :soldToId and orgext.row_id = sasset.owner_accnt_id and sasset.prod_id = prodint.row_id"
	        		+ " and prodint.pr_prod_ln_id = prod_ln.row_id(+) and sasset.row_id = s_asset_xm.par_row_id(+)"
	        		+ " and sasset.status_cd <> 'Inactive' and sasset.x_seid is not null) where x_ip_address is null";
	        */
	        	//query = getSiebelSession().createSQLQuery(sqlQuery);
		    	query.setParameter("soldToId", soldToId);
		    	//query.setFetchSize(GRTPropertiesUtil.getProperty("fetch_size_smeassets", 500));
		    	resultSet = query.list();
		    	
	        	//added to getSALMigrationEligibleAssets SalMigrationServiceImpl
				
		    	//Iterate through the result set
/*				if(resultSet != null) {
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
								}

								siteArrayList.add(siteList);
			    			//}
							//Commented the above line for demo on 05/24/2013.
			    		}
			    	}
				}*///added from daoimpl

		}  catch (HibernateException hibEx) {
			throw new DataAccessException(SalMigrationDaoImpl.class, hibEx
					.getMessage(), hibEx);
		} catch (Exception ex) {
			throw new DataAccessException(SalMigrationDaoImpl.class, ex
					.getMessage(), ex);
		}
    	logger.debug("Exiting SiebelDao : getSALMigrationEligibleAssets");

    	return resultSet;
    }
    
    public String saveTechnicalOrderForSALGateway (TechnicalOrder technicalOrder) throws DataAccessException {
		try {
			Session session = getSessionForGRT();
			session.beginTransaction();
	        session.saveOrUpdate(technicalOrder);
            session.getTransaction().commit();
		} catch (HibernateException hibEx) {
			logger.error("Error in RegistrationDao : saveTechnicalOrderForSALGateway : "
							+ hibEx.getMessage());
			throw new DataAccessException(SalMigrationDaoImpl.class, hibEx.getMessage(), hibEx);
		}
        return technicalOrder.getOrderId();
    }
    

  //30042015
	/**
	 * Method to save the Site List from SAL Migration .
	 *
	 * @param siteList
	 * @return salSiteListId Long
	 * @throws DataAccessException
	 *             Custom Exception
	 */
	public void saveSalSiteListForSALMigration(SiteList siteList)
			throws DataAccessException {
		logger.debug("Entering RegistrationDao : saveSalSiteListForSALMigration:" + siteList.getId());

		try {
			saveOrUpdate(siteList);
		} catch (HibernateException hibEx) {
            logger.debug("@@@@@@@@@ IDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINT IDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINTIDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINT");
        	logger.error(hibEx);
			throw new DataAccessException(SalMigrationDao.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
            logger.debug("@@@@@@@@@ IDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINT IDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINTIDENTIFY THE ERROR LOG.......JUST BEFORE ERROR PRINT");
        	logger.error(ex);
			throw new DataAccessException(SalMigrationDao.class, ex.getMessage(), ex);
		}
		logger.debug("Exiting RegistrationDao : saveSalSiteListForSALMigration");
	}
	
	 //30042015
	public void updateHasTRSubmittedManually(String registrationId) throws DataAccessException{
        logger.debug("Entering RegistrationDao : updateHasTRSubmittedManually" + registrationId);
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
            registration.setHasSubmittedTrManually(1L);
            session.saveOrUpdate(registration);
            session.getTransaction().commit();
            logger.debug("Exiting RegistrationDao : updateHasTRSubmittedManually");

        } catch (HibernateException hibEx) {
           hibEx.printStackTrace();
        }
    }
	

	public Session getSiebelSession() {
		return siebelSession;
	}

	public void setSiebelSession(Session siebelSession) {
		this.siebelSession = siebelSession;
	}
    
}
