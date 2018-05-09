package com.avaya.grt.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.DataAccessException;
import com.grt.util.StatusEnum;



public class TechnicalRegistrationAsyncDao extends TechnicalRegistrationDao {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationAsyncDao.class);
	
	
	 public void saveOrUpdateDomainObject(Object persistableDomainObject) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationAsyncDao.saveOrUpdateDomainObject for :" + persistableDomainObject);
			try {
				if(persistableDomainObject != null) {
					Session session = getSessionForGRT();
					session.beginTransaction();
			        session.saveOrUpdate(persistableDomainObject);
			        session.getTransaction().commit();
				}
			} catch (HibernateException hibEx) {
				logger.error("Error while saving ExpandedSolutionElement", hibEx);
				throw new DataAccessException(TechnicalRegistrationAsyncDao.class, hibEx.getMessage(), hibEx);
			}
		}
	 
	 public TechnicalRegistration updateTechnicalRegistrationEPNSurveyStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationAsyncDao.updateTechnicalRegistrationEPNSurveyStatus()");
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
	            resultObject.setEpnSurveyStatus(s);
	            session.saveOrUpdate(resultObject);
	            session.getTransaction().commit();

	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationAsyncDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationAsyncDao.updateTechnicalRegistrationEPNSurveyStatus()");

	        return resultObject;
	    }
	 
	 public TechnicalRegistration getTechRegByExpSolId(String expandedSolElmId){
	        logger.debug("Entering TechnicalRegistrationAsyncDao.getTechRegByExpSolId()");

		 	Session session = getSessionForGRT();
		 	Criteria criteria = session.createCriteria(ExpandedSolutionElement.class);
			criteria.add(Restrictions.eq("expSolnElemntId", expandedSolElmId));
			ExpandedSolutionElement element = (ExpandedSolutionElement)  criteria.uniqueResult();
			
	        logger.debug("Exiting TechnicalRegistrationAsyncDao.getTechRegByExpSolId()");
	        if( element!=null ){
	        	return element.getTechnicalRegistration();
	        }else{
	        	return null;
	        }
	 }
	 
	 public SiteList getSiteListByExpSolId(String expandedSolElmId){
	        logger.debug("Entering TechnicalRegistrationAsyncDao.getSiteListByExpSolId()");

		 	Session session = getSessionForGRT();
		 	Criteria criteria = session.createCriteria(ExpandedSolutionElement.class);
			criteria.add(Restrictions.eq("expSolnElemntId", expandedSolElmId));
			ExpandedSolutionElement element = (ExpandedSolutionElement)  criteria.uniqueResult();
			
	        logger.debug("Exiting TechnicalRegistrationAsyncDao.getSiteListByExpSolId()");
	        if( element!=null ){
	        	return element.getSiteList();
	        }else{
	        	return null;
	        }
	 }
}
