package com.avaya.grt.dao.salmigration;

import java.util.List;

import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.util.DataAccessException;


public interface SalMigrationDao {
	
	public List<Object[]> getSALMigrationEligibleAssets(String soldToId) throws DataAccessException;

	public String saveTechnicalOrderForSALGateway(TechnicalOrder technicalOrder) throws DataAccessException;
	public void saveSalSiteListForSALMigration(SiteList siteList)throws DataAccessException;
	public void updateHasTRSubmittedManually(String registrationId) throws DataAccessException;
}
