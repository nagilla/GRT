package com.avaya.grt.dao.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.dao.equipmentremoval.EQRDaoImpl;
import com.grt.dto.Contact;
import com.grt.util.DataAccessException;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class AccountDaoImpl extends BaseHibernateDao implements AccountDao {

	private static final Logger logger = Logger.getLogger(AccountDaoImpl.class);
	
	/**
	 * method to fetch account details using account id from siebel.
	 * 
	 * @param accountId String
	 * @return contacts List
	 * @throws Exception
	 */
	@Override
	public List<Contact> queryAccountContacts(String accountId) throws DataAccessException{

    	logger.debug("Entring queryAccountContacts for accountId:" + accountId);
    	List<Contact> contacts = new ArrayList<Contact>();
    	try {
    		if(StringUtils.isNotEmpty(accountId)) {
    			
		    	Query query = getSessionForSiebel().getNamedQuery("queryAccountContacts");
		    	query.setParameter("accountId", accountId);
		    	List<Object[]> resultSet = query.list();
		    	if (resultSet != null && resultSet.size()>0) {
		    		logger.debug("Match found in Siebel for AccountId:" + accountId);
		    		Object[] object = resultSet.get(0);
		    		if (object != null) {
		    			Contact contact = new Contact();
						if(object[2] != null) {
							contact.setEmail(object[2].toString());
						}
						if(object[3] != null) {
							contact.setFax(object[3].toString());
						}
						if(object[4] != null) {
							contact.setFirstName(object[4].toString());
						}
						if(object[5] != null) { 
							contact.setLastName(object[5].toString());
						}
						if (object[6] != null) {
							contact.setMiddleName(object[6].toString());
						}
						if (object[9] != null) {
							contact.setPhone(object[9].toString());
						}
						contacts.add(contact);
		    		}
		    	}
    		}
    	} catch (HibernateException hibEx) {
    		getSessionForSiebel().getTransaction().rollback();
            throw new DataAccessException(EQRDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
        	getSessionForSiebel().getTransaction().rollback();
            throw new DataAccessException(EQRDaoImpl.class, ex
                    .getMessage(), ex);
        } finally {
    		logger.debug("Exiting queryAccountContacts for accountId:" + accountId);
    	}
    	return contacts;
	}
	 
}
