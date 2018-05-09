package com.avaya.grt.dao.graph;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.engine.SessionFactoryImplementor;

import com.avaya.grt.dao.BaseHibernateDao;
import com.grt.util.DataAccessException;



public class RegistrationGraphDaoImpl extends BaseHibernateDao implements RegistrationGraphDao {
	private static final Logger logger = Logger.getLogger(RegistrationGraphDaoImpl.class);
	
	public Iterator getRegistrationsCompleted(String userId) throws DataAccessException {
		 Iterator resultSetList = null;
		 logger.debug("Entering RegistrationGraphDaoImpl : getRegistrationsCompleted");
		 try
		 {
			
			Query  query=getSessionForGRT().getNamedQuery("getRegistrationsCompleted");
	         query.setString("userName", userId);
	       
	         resultSetList = query.list().iterator();
	        
	     } catch (HibernateException hibEx) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, hibEx
	                 .getMessage(), hibEx);
	     } catch (Exception ex) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, ex
	                 .getMessage(), ex);
	     }
		 logger.debug("Exiting RegistrationGraphDaoImpl : getRegistrationsCompleted");		 
		 return resultSetList;		
	}

	public Iterator getRegistrationsSaved(String userId) throws DataAccessException {
		 Iterator resultSetList = null;
		 logger.debug("Entering RegistrationGraphDaoImpl : getRegistrationsSaved");
		 try
		 {		 
			 Query  query=getSessionForGRT().getNamedQuery("getRegistrationsSaved");
			 query.setString("userName", userId);
			 resultSetList = query.list().iterator();
        
	     } catch (HibernateException hibEx) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, hibEx
	                 .getMessage(), hibEx);
	     } catch (Exception ex) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, ex
	                 .getMessage(), ex);
	     }
		 logger.debug("Exiting RegistrationGraphDaoImpl : getRegistrationsSaved");		 
		 return resultSetList;		
	}
	
	public Iterator getRegistrationsNotCompleted(String userId) throws DataAccessException {
		 Iterator resultSetList = null;
		 logger.debug("Entering RegistrationGraphDaoImpl : getRegistrationsNotCompleted");
		 try
		 {
		 
			 Query  query=getSessionForGRT().getNamedQuery("getRegistrationsNotCompleted");
			 query.setString("userName", userId);
			 resultSetList = query.list().iterator();
        
	     } catch (HibernateException hibEx) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, hibEx
	                 .getMessage(), hibEx);
	     } catch (Exception ex) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, ex
	                 .getMessage(), ex);
	     }

		 logger.debug("Exiting RegistrationGraphDaoImpl : getRegistrationsNotResubmitted");
		 
		 return resultSetList;
		
	}
	
	
//GRT 4.0 Changes
	@Override
	public Iterator getRegistrationsCompletedList(String userId)
			throws DataAccessException {
		Iterator resultSetList = null;
		 logger.debug("Entering RegistrationGraphDaoImpl : getRegistrationsComplteList");
		 try
		 {
			
			Query  query=getSessionForGRT().getNamedQuery("getRegistrationsCompletedList");
	         query.setString("userName", userId);
	      	
	         resultSetList = query.list().iterator();
	        
	     } catch (HibernateException hibEx) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, hibEx
	                 .getMessage(), hibEx);
	     } catch (Exception ex) {
	         throw new DataAccessException(RegistrationGraphDaoImpl.class, ex
	                 .getMessage(), ex);
	     }
		 logger.debug("Exiting RegistrationGraphDaoImpl : getRegistrationsComplteList");	
		 
		 return resultSetList;	
		
	}
	
	
}
