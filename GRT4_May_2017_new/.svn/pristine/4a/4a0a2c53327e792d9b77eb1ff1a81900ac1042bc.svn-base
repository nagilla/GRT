package com.avaya.grt.service.salmigration;

import java.util.List;

import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface SalMigrationService {

	public List<SiteList> getSALMigrationEligibleAssets(String soldToId) throws DataAccessException;

	public void saveExistingRegisteredAssetsList(List<SiteList> siteList, SiteRegistration siteRegistration) throws DataAccessException, Exception;
	/*public SALGatewayIntrospection introspectSALGateway(String gatewaySeid,
			String soldToId) throws DataAccessException;*/

	public void createNewSALGateway(SiteRegistration siteRegistration,
			List<TechnicalOrderDetail> installBaseData, TRConfig trConfig,
			String userId) throws Exception;
	public List<TRConfig> isTREligible(String materialCode);
}
