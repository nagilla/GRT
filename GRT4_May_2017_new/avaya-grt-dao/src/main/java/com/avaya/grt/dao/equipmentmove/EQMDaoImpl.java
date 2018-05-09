package com.avaya.grt.dao.equipmentmove;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationErrorDao;
import com.grt.util.DataAccessException;



public class EQMDaoImpl extends TechnicalRegistrationErrorDao implements EQMDao{
	private static final Logger logger = Logger.getLogger(EQMDaoImpl.class);
	
	/**
	 * Method returns true if countries for both the soldTos are same
	 * and returns false if countries for both the soldTos are different 
	 * @param fromSoldTo
	 * @param toSoldTo
	 * @return result
	 */
	public boolean isSameCountry(String fromSoldTo, String toSoldTo) {
		boolean result = false;
		logger.debug("Entering EQMDaoImpl.isSameCountry");
		
		List<String> soldToList = new ArrayList<String>();
		soldToList.add(fromSoldTo.trim());
		soldToList.add(toSoldTo.trim());
		
		List<Object[]> cxpList = null;
		
		//First check in CXP - countries for both the soldTos
		try {
			Query query = getSessionForCXP().getNamedQuery("getSoldToCountry");
			query.setParameterList("soldToList", soldToList);
			cxpList = query.list();
			
			if ( (cxpList!= null) && (!cxpList.isEmpty()) ) {
				result = parseCountryRecords(cxpList);
			} 
			
			cxpList = null;
			
		} catch (HibernateException hibEx) {
			logger.error("Hibernate Exception occured in EQMDaoImpl.isSameCountry while CXP checking : " + hibEx.getMessage());
			result = false;
		} catch (Exception ex) {
			logger.error("Exception occured in EQMDaoImpl.isSameCountry while CXP checking : " + ex.getMessage());
			result = false;
		} 
		
		//Second check in Siebel - countries for both the soldTos 
		if ( (cxpList == null) || (cxpList.isEmpty()) ) {
			try {
				Query query = getSessionForSiebel().getNamedQuery("getSoldToCountrySiebel");
				query.setParameterList("soldToList", soldToList);
				List<Object[]> list = query.list();
				
				if ( (list!= null) && (!list.isEmpty()) ) {
					result = parseCountryRecords(list);
				} 
				
			} catch (HibernateException hibEx) {
				logger.error("Hibernate Exception occured in EQMDaoImpl.isSameCountry while Siebel checking : " + hibEx.getMessage());
				result = true;
			} catch (Exception ex) {
				logger.error("Exception occured in EQMDaoImpl.isSameCountry while Siebel checking : " + ex.getMessage());
				result = true;
			}
		}
		
		logger.debug("Exiting EQMDaoImpl.isSameCountry");
		return result;
	}
	
	/**
	 * method to parse country records
	 * @param list
	 * @return
	 */
	private boolean parseCountryRecords(List<Object[]> list) {
		boolean result = false;
		String country = "";

		//Check if two records returned from list are same
		for (Object[] object : list) {
			String tempCountry = (String) object[1];
			
			if (!tempCountry.isEmpty() && tempCountry.equalsIgnoreCase(country)) {
				result = true;
				break;
			}
			
			country = tempCountry;
			
		}
		
		return result;
	}
	
	/*public static void main(String[] args) throws DataAccessException {		   
	   	ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
	   	EQMDao eqmDao = (EQMDao) context.getBean("eqmDao");
	   	
	   	String fromSoldTo = "0001237354";
	   	//same country
	   	String toSoldTo = "0005046456";
	   	//different country
	   	//String toSoldTo = "0002879167";
	   	boolean result = eqmDao.isSameCountry(fromSoldTo, toSoldTo);
	   	System.out.println("result = " + result);
	}*/

}
