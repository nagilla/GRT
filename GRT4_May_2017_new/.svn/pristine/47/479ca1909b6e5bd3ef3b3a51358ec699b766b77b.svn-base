package com.avaya.grt.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.avaya.grt.mappers.ArtProductType;
import com.grt.util.DataAccessException;

public class TechnicalRegistrationArtDao extends BaseHibernateDao {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationArtDao.class);
	
		
	public ArtProductType getProductTypeByTypeCode(String productTypeCode) throws DataAccessException {
        logger.debug("Entering TechnicalRegistrationArtDao : getProductTypeByTypeCode");
        ArtProductType productType = null;
        try {
            Criteria criteria = getSessionForGRT().createCriteria(ArtProductType.class);
            criteria.add(Restrictions.eq("productTypeCode", productTypeCode));
            productType = (ArtProductType)criteria.uniqueResult();
        } catch (HibernateException hibEx) {
            throw new DataAccessException(TechnicalRegistrationArtDao.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(TechnicalRegistrationArtDao.class, ex
                    .getMessage(), ex);
        }

        logger.debug("Entering TechnicalRegistrationArtDao : getProductTypeByTypeCode");

        return productType;
    }
	
	 
}
