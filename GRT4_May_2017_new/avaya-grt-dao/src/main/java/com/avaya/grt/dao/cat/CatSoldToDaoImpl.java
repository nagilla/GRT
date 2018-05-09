package com.avaya.grt.dao.cat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import com.avaya.grt.dao.BaseHibernateDao;
import com.grt.util.DataAccessException;

public class CatSoldToDaoImpl extends BaseHibernateDao implements CatSoldToDao {

	private static final Logger logger = Logger.getLogger(CatSoldToDaoImpl.class);
	
	private String catUserName;
	
	/**
	 * 
	 * @param bpLinkId
	 * @param soldToIdList
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public List<String> querySoldToListAccess(String bpLinkId, List<String> soldToIdList) throws DataAccessException {
		logger.debug("Entering querySoldToListAccess : bpLinkId:" + bpLinkId);
		long time1 = System.currentTimeMillis();
		List<String> accessList = new ArrayList<String>();
		try {
			if(StringUtils.isNotEmpty(bpLinkId) && soldToIdList != null && soldToIdList.size() > 0) {
				StringBuffer sf = new StringBuffer("(");
				for (String soldTo : soldToIdList) {
					sf.append("'" + soldTo + "', ");
				}
				String soldToIdInClause = sf.substring(0, sf.length()-2);
				soldToIdInClause += ")";
				
				String queryStr = "SELECT SOLD_TO FROM " + catUserName.trim() +".APPROVED_PERMISSIONS WHERE BP_LINK_ID = :bpLinkId " 
					+ "AND SOLD_TO IN " +soldToIdInClause+ " AND PERMISSION_TYPE = 'Registration' AND PERMISSION_STATUS='Active'";
				SQLQuery query =  getSessionForCAT().createSQLQuery(queryStr);
				
				query.setString("bpLinkId", bpLinkId);
				query.addScalar("SOLD_TO", Hibernate.STRING);
				logger.debug("queryStr : "+query.toString());
				List<String> list = query.list();
				if(list != null && list.size() > 0) {
					for (String data : list) {
						if(data != null){
							accessList.add(data);
						}
					}
				}
			}
		} catch (HibernateException hibEx) {
			throw new DataAccessException(CatSoldToDaoImpl.class, hibEx.getMessage(), hibEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DataAccessException(CatSoldToDaoImpl.class, ex.getMessage(), ex);
		} finally {
			logger.debug("Exiting querySoldToListAccess : bpLinkId:" + bpLinkId);
		}
		return accessList;
	}

	public String getCatUserName() {
		return catUserName;
	}

	public void setCatUserName(String catUserName) {
		this.catUserName = catUserName;
	}

	
}
