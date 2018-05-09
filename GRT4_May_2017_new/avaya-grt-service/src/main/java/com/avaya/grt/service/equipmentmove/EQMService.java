package com.avaya.grt.service.equipmentmove;

import java.util.List;
import java.util.Map;

import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public interface EQMService {
	 public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	 public SiteRegistration updateSiteRegistrationProcessStepAndStatus(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException;
	 public int saveSiteRegistrationDetails(SiteRegistration siteRegistration) throws DataAccessException;	 
	 public List<TechnicalOrderDetail> filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) throws DataAccessException;
	 public RegistrationSummary getRegistrationSummary(String registrationId) throws DataAccessException;	 
	 public List<TechnicalOrderDetail> fetchEquipmentRemovalRecords(String soldTo, String vCode, boolean fetchSALGWRecords) throws DataAccessException;
	 public Map<String, Object> validateMaterialExclusion(List<String> materialCodes) throws DataAccessException;
	 public List<PipelineSapTransactions> getConsolidatedPipelineRecords(String soldToId) throws DataAccessException;
	 public int deleteTechnicalOrderForRegId(String registrationId, ProcessStepEnum processStep) throws DataAccessException;
	 public String saveSelectedEquipmentsForEQR(String regId, String soldTo, java.util.List<TechnicalOrderDetail> selectedTOs, String userId, String orderType) throws Exception;
	 public void updateSiteRegistrationSubmittedFlag(String registrationId,String submitted);	 
	 
	 /* New methods starts */
	 public boolean isSameCountry(String fromSoldTo, String toSoldTo);
	 public String equipmentMove(SiteRegistration siteRegistration, String regId, 
			 String soldTo, String toSoldTo, List<TechnicalOrderDetail> actualList, List<TechnicalOrderDetail> mcList, String userId, String uploadFilePath) throws Exception;
	 public String getExistingStepBRegBySeid(String seId) throws DataAccessException;
	 /* New methods starts */

}
