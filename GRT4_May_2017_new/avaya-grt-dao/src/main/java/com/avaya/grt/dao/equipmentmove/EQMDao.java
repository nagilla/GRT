package com.avaya.grt.dao.equipmentmove;

import java.util.List;

import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.util.DataAccessException;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public interface EQMDao {
	  public List<TechnicalOrder> saveTechnicalOrderList(List<TechnicalOrder> technicalOrderList) throws DataAccessException;	  
	  public SiteRegistration updateSiteRegistrationSubStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep, boolean processDateUpdateFlag) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationSubmittedFlag(String registrationId, String submitted) throws DataAccessException;
	  public List<PipelineSapTransactions> savePipelineSapTransactionsList(
	   			List<PipelineSapTransactions> pipelineSapTransactionsList)
	   			throws DataAccessException;
	  public List<TechnicalOrder> getTechnicalOrderOnRegIdOrderTypeIsSALGateways(String registrationId, String orderType, boolean isSALGateways) throws DataAccessException;
	  public void updateTODOpenQuantity(String registrationId, String completedByRegId, List<String> materialCodes, String orderType) throws DataAccessException;
	  public List<PipelineSapTransactions> fetchPipelineSapTransactionOnSoldTo(String soldTo) throws DataAccessException;
	  public String getExistingRegistrationBySeid(String seId) throws DataAccessException;
	  
	  /* New methods starts */
	  public boolean isSameCountry(String fromSoldTo, String toSoldTo);
	  /* New methods starts */
	  
}
