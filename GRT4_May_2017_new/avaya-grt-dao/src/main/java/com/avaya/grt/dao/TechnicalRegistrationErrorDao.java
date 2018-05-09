package com.avaya.grt.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.avaya.grt.mappers.SiteRegistration;
import com.grt.util.DataAccessException;


public class TechnicalRegistrationErrorDao extends TechnicalRegistrationDao {
	
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationErrorDao.class);
	
	 /**
     * Set siteRegistration salMigrationOnlyFlag field
     * @param registrationId
     * @param salMigrationOnlyFlag
     * @return SiteRegistration
     * @throws DataAccessException
     */
    public SiteRegistration updateSiteRegistrationSubmittedFlag(String registrationId, String submitted) throws DataAccessException {
        logger.debug("Entering TechnicalRegistrationErrorDao : updateSiteRegistrationSalMigrationOnlyFlag()");
        SiteRegistration registration = null;
        try {
            Session session = getSessionForGRT();
            session.beginTransaction();
			registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
			registration.setSubmitted(submitted);
            session.saveOrUpdate(registration);
            session.getTransaction().commit();

        } catch (HibernateException hibEx) {
            throw new DataAccessException(TechnicalRegistrationErrorDao.class, hibEx
                    .getMessage(), hibEx);
        }
        logger.debug("Exiting TechnicalRegistrationErrorDao : updateSiteRegistrationSalMigrationOnlyFlag()");

        return registration;
    }

}
