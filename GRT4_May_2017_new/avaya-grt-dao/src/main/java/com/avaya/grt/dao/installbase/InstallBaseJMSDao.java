package com.avaya.grt.dao.installbase;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.avaya.grt.mappers.ArtProductType;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public interface InstallBaseJMSDao {
	
	 public String createTR(TechnicalRegistration technicalRegistration) throws DataAccessException;
	  public SRRequest initSRRequest(SiteRegistration reg, ProcessStepEnum processStep) throws DataAccessException;
	  public SiteRegistration updateIsSrCompletedSiteRegistration(SiteRegistration siteRegistration, ProcessStepEnum processStep,String isSrCompleted) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep, boolean processDateUpdateFlag) throws DataAccessException;
	  public List<PipelineSapTransactions> savePipelineSapTransactionsList(
	   			List<PipelineSapTransactions> pipelineSapTransactionsList)
	   			throws DataAccessException;
	  
	  public SiteRegistration updateSiteRegistrationSubStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException;
	  public List<String[]> getPipelineForProcessing(String registrationId, List<String> materialCodes) throws DataAccessException;
	  public Set<String> getEligibleAccessTypes(List<TRConfig> trConfigs, Map<String, CompatibilityMatrix> salCompatibilityMatrix)  throws DataAccessException;
	  public List<ProductRelease> getSSLVPNProductReleases() throws DataAccessException;
	  public List<CompatibilityMatrix> getSECodeFromCompatibilityMatrix() throws DataAccessException;
	  public String saveTechnicalRegistrationForSALGateway(TechnicalRegistration technicalRegistration) throws DataAccessException;
	  public SRRequest updateSRRequest(SRRequest srRequest) throws DataAccessException;
	  public void updateProcessedPipelineTransactions(List<String> registrationIds, List<String> materialCodes, String orderType) throws DataAccessException;
	  public List<Object[]> queryResponseOnRegistrationId(String registrationId, String source) throws DataAccessException;
	  public Map<String,List<String>> processPipelineBasedonEQRAndIBQuantity(List<TechnicalOrderDetail> successList) throws DataAccessException;
	  public void updatePipeLineSAPTransactionsSAPCompletedFlag(String registrationId, String orderType) throws DataAccessException;
	  public String updateTechnicalOrderErrorDescription(List<TechnicalOrderDetail> technicalOrderList) throws DataAccessException;
	  public List<TechnicalOrder> getTechnicalOrderListForEuipmentNumberList(String regId, String orderType, List<String> equipNumList) throws DataAccessException;
	  public List<TechnicalOrder> getTechnicalOrderOnRegIdOrderTypeIsSALGateways(String registrationId, String orderType, boolean isSALGateways) throws DataAccessException;
	  public Map<String,List<String>> updatePipeLineSAPTransactions(List<TechnicalOrderDetail> errorList) throws DataAccessException;
	  public Object[] querySAPResponseOnRegistrationId(String registrationId, boolean ibFlag, boolean eqrFlag) throws DataAccessException;
	  public SRRequest initSRRequestForActiveContracts(SiteRegistration reg) throws DataAccessException;
	  public void updateTODOpenQuantity(String registrationId, String completedByRegId, List<String> materialCodes, String orderType) throws DataAccessException;
	  public List<TechnicalOrderDetail> fetchEquipmentsWithSameSIDnMID(TechnicalOrderDetail technicalOrderDetail) throws DataAccessException;
	  
	  public String getPhoneNoByCountry(String countryCode) throws DataAccessException;
	  public TechnicalRegistration getTechnicalRegistration(String technicalRegistrationId) throws DataAccessException;
	  public TechnicalRegistration saveTechnicalRegistration(TechnicalRegistration technicalRegistrationToPersist);
	  public SiteList getSiteList(String siteListId) throws DataAccessException;
	  public void saveSalSiteList(SiteList siteList) throws DataAccessException;
	  public ArtProductType getProductTypeByTypeCode(String productTypeCode) throws DataAccessException;
}
