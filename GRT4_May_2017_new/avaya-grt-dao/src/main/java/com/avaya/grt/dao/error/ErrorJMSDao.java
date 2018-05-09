package com.avaya.grt.dao.error;

import com.avaya.grt.mappers.SiteRegistration;
import com.grt.util.DataAccessException;

public interface ErrorJMSDao {

	public SiteRegistration updateSiteRegistrationSubmittedFlag(String registrationId, String submitted) throws DataAccessException;
}
