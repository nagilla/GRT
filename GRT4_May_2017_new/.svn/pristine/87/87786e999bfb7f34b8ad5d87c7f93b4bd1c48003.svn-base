package com.avaya.grt.dao.cxp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.dao.cat.CatSoldToDaoImpl;
import com.grt.dto.Account;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;

public class CXPSoldToDaoImpl extends BaseHibernateDao implements CXPSoldToDao{
	
	private static final Logger logger = Logger.getLogger(CXPSoldToDaoImpl.class);
	
	String cxpUserName;
	/**
     * An API to get account Info from CXP (Originally designed for B box Distributors)
     *
     * @param soldToId
     * @return sodlTo Account Info
     * @throws DataAccessException
     */
    public Account getAccountInfo(String soldToId) throws DataAccessException {
        logger.debug("Entering getAccountInfo for soldToId:" + soldToId);
        Account account = null;
        
		try {
			String schema = getCxpUserName();
			
        	String sqlQuery = "select  cb.name as customer_name, street1, street2, city, state, zipcode,ctry.name as country, cb.account_number " + 
        					  " from " + schema +".CUSTOMER cb  join " + schema +".address a on a.customer_id = cb.customer_id " +  
        					  " join " + schema +".country ctry on ctry.COUNTRY_ID=a.country_id " + 
        					  " and account_number ='" + soldToId +"'";	 

        	SQLQuery query = getSessionForCXP().createSQLQuery(sqlQuery);
        	List queryResult = query.list();
            for (int i = 0; i < queryResult.size(); i++) {
                Object[] listItem = (Object[]) queryResult.get(i);
                account = new Account();
                if(StringUtils.isNotEmpty((String)listItem[0])) {
                	account.setName((String)listItem[0]);
                }
                if(StringUtils.isNotEmpty((String)listItem[1])) {
                	account.setPrimaryAccountStreetAddress((String)listItem[1]);
                }
                if(StringUtils.isNotEmpty((String)listItem[2])) {
                	account.setPrimaryAccountStreetAddress2((String)listItem[2]);
                }
                if(StringUtils.isNotEmpty((String)listItem[3])) {
                	account.setPrimaryAccountCity((String)listItem[3]);
                }
                if(StringUtils.isNotEmpty((String)listItem[4])) {
                	account.setPrimaryAccountState((String)listItem[4]);
                }
                if(StringUtils.isNotEmpty((String)listItem[5])) {
                	account.setPrimaryAccountPostalCode((String)listItem[5]);
                }
                if(StringUtils.isNotEmpty((String)listItem[6])) {
                	account.setPrimaryAccountCountry((String)listItem[6]);
                }
                if(StringUtils.isNotEmpty((String)listItem[7])) {
                	account.setSoldToNumber((String)listItem[7]);
                }
            }
        } catch (Throwable throwable){
            logger.error("", throwable);
        } finally {
        	logger.debug("Exiting getAccountInfo for soldToId:" + soldToId);
        }
        return account;
    }
	public String getCxpUserName() {
		return cxpUserName;
	}
	public void setCxpUserName(String cxpUserName) {
		this.cxpUserName = cxpUserName;
	}
	
    

}
