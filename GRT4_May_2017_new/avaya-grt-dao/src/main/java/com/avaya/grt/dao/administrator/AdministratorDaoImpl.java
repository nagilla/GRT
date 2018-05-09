/**
 * 
 */
package com.avaya.grt.dao.administrator;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.mappers.Alert;
import com.grt.util.DataAccessException;

/**
 * @author deepa_vadakkath
 *
 */
public class AdministratorDaoImpl extends TechnicalRegistrationDao implements AdministratorDao{

	private static final Logger logger = Logger.getLogger(AdministratorDaoImpl.class);
	
	 /**
     * API to get all admin alerts.
     *
     * @throws DataAccessException Exception
     */
    public List<Alert> getAdminAlerts() throws DataAccessException {
        logger.debug("Entering AdministratorDaoImpl : getAdminAlerts");
        List<Alert> alerts = null;
        try {
          //  String  queryString = "Select alert from Alert alert where alert.endDate >= sysdate and alert.isExist = '1' order by alert.createdDate desc";
           // Query query = getSession().createQuery(queryString);
        	Query query = getSessionForGRT().getNamedQuery("getAdminAlerts");
            alerts = query.list();
            if (alerts != null) {
                logger.debug("active alert count: " + alerts.size());
            }
        } catch (HibernateException hibEx) {
            throw new DataAccessException(AdministratorDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting AdministratorDaoImpl : getAdminAlerts");
        return alerts;
    }
	
    
    /**
     * API to save or Update alert details.
     *
     *
     * @throws DataAccessException Exception
     */
    public int saveOrUpdateAlertDetails(Alert alert) throws DataAccessException {
    	int successFlag = 0;
    	Alert alertObj = null;
    	try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            logger.debug("AdministratorDaoImpl : saveOrUpdateAlertDetails() alertId: "+alert.getAlertId());
            if(alert != null && alert.getAlertId() != null && !"".equals(alert.getAlertId())){
            	logger.debug("Entered AdministratorDaoImpl : saveOrUpdateAlertDetails() Update ");
            	Criteria criteria =  session.createCriteria(Alert.class);
                criteria.add(Restrictions.eq("alertId", alert.getAlertId()));
                alertObj = (Alert) criteria.uniqueResult();
                alertObj.setEndDate(alert.getEndDate());
                alertObj.setStartDate(alert.getStartDate());
                alertObj.setMessage(alert.getMessage());
                alertObj.setLastModifiedBy(alert.getCreatedBy());
                alertObj.setLastModifiedDate(new Date());
                session.saveOrUpdate(alertObj);
            }else{
            	logger.debug("Entered AdministratorDaoImpl : saveOrUpdateAlertDetails() Save ");
            	alert.setIsExist("1");
            	alert.setCreatedDate(new Date());
            	session.saveOrUpdate(alert);
            }
            session.getTransaction().commit();
            successFlag = 1;
        } catch (HibernateException hibEx) {
            logger.error("Error in AdministratorDaoImpl : saveOrUpdateAlertDetails : " +  hibEx.getMessage());
            throw new DataAccessException(AdministratorDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex){
            getSessionForGRT().getTransaction().rollback();
            ex.printStackTrace();
            logger.error("Error in AdministratorDaoImpl : saveOrUpdateAlertDetails : " +  ex.getMessage());
        }
        return successFlag;
    }
    
    /**
     * API to delete Alert.
     *
     *
     * @throws DataAccessException Exception
     */
    public int deleteAlert(Alert alert) throws DataAccessException {
    	int successFlag = 0;
    	Alert alertObj = null;
    	try {
            Session session = getSessionForGRT();
            session.beginTransaction();
            logger.debug("AdministratorDaoImpl : deleteAlert() alertId: "+alert.getAlertId());
            Criteria criteria =  session.createCriteria(Alert.class);
            criteria.add(Restrictions.eq("alertId", alert.getAlertId()));
            alertObj = (Alert) criteria.uniqueResult();
            alertObj.setIsExist(alert.getIsExist());
            alertObj.setLastModifiedBy(alert.getLastModifiedBy());
            alertObj.setLastModifiedDate(alert.getLastModifiedDate());
        	session.saveOrUpdate(alertObj);
            session.getTransaction().commit();
            successFlag = 1;
        } catch (HibernateException hibEx) {
            logger.error("Error in AdministratorDaoImpl : saveOrUpdateAlertDetails : " +  hibEx.getMessage());
            throw new DataAccessException(AdministratorDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex){
            getSessionForGRT().getTransaction().rollback();
            ex.printStackTrace();
            logger.error("Error in AdministratorDaoImpl : saveOrUpdateAlertDetails : " +  ex.getMessage());
        }
        return successFlag;
    }
	
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		AdministratorDao administratorDao = (AdministratorDao) context.getBean("administratorDao");

	}

}
