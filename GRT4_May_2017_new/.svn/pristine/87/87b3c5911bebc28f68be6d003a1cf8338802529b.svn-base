package com.avaya.grt.dao.installbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.avaya.grt.mappers.Alert;
import com.avaya.grt.mappers.CompatibilityMatrix;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.MaterialCode;
import com.avaya.grt.mappers.MaterialExclusion;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.ProductRelease;
import com.avaya.grt.mappers.Province;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.State;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.SuperUser;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.BusinessPartner;
import com.grt.dto.Product;
import com.grt.dto.SoldTo;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;


public interface InstallBaseDao {
	 
    public Map<String, MaterialCode> getMaterialCodeForValidation(List<String> materialCodes);
          
    public List<State> getStateList() throws DataAccessException;
    public List<Country> getCountryList() throws DataAccessException;
    public List<Province> getProvinceList() throws DataAccessException;
    public List<Region> getRegionList() throws DataAccessException;
    public List<Alert> getActiveAlerts() throws DataAccessException;
    public boolean isSoldToExcluded(String soldTo) throws DataAccessException;
    public SiteRegistration getSiteRegistration(String registrationId) throws DataAccessException;
    public SuperUser getSuperUser(String userId) throws DataAccessException;
	public List<String> getSoldToList(String userName) throws DataAccessException;
	public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException;
	 public List<SoldTo> getSoldToObjectList(String userName) throws DataAccessException;
	 public List<SoldTo> getSoldToObjectListFromSalesOut(String bpLinkId) throws DataAccessException;
	 public List<BPAccountTempAccess> getBPAccountTempAccess(String bpLinkId) throws DataAccessException;
	 public List<BPAccountTempAccess> queryAccess(String bpLinkId, String soldToId) throws DataAccessException;
	 public boolean isSoldToExistForTheUser(String soldTo, String userId) throws DataAccessException;
	 public boolean isBpSoldToExistForTheUser(String soldTo, String bpLinkId) throws DataAccessException;
	 public void updateAccountInformation(SiteRegistration siteRegistration) throws DataAccessException;
	 public int saveSiteRegistrationDetails(SiteRegistration siteRegistration) throws DataAccessException;
	 public String getRegistrationId();
	 public List getRegistrationDetailFromCXP(String accountNumber) throws DataAccessException;
	 public List getRegistrationDetailFromSalesOut(String accountNumber) throws DataAccessException;
	public int getAllSolutionElementsForAccountCountNew(String soldToNumber,String property);
	public List<Product> getAllSolutionElementsForAccountNew(String soldToNumber, int offSet, int fetchSize, String vcodes);
	public List<TRConfig> getTRConfigData(String materialCode);
	 public Map<String, Set<String>> initializeTRConfigData();
	 public List<Product> getAllSolutionElementsForAccount(String soldToNumber);
	 public List<PipelineSapTransactions> fetchPipelineSapTransactionOnSoldTo(String soldTo) throws DataAccessException;
	 public Map<String,String> getMaterialCodeDesc(List<String> materialCodes) throws DataAccessException;
	 public TRConfig loadTRConfig(String groupId); 
	 public List<String> getReleases(String seCode, String productType, String template, String specialNote, boolean sslvpn) throws DataAccessException;
	  public List<TechnicalOrderDetail> getEquipmentRemovalRecords(String soldTo, String vcodes) throws DataAccessException;
	  public List<TechnicalOrderDetail> fetchEquipmentRemovalRecordsForSALGWAndVSALGW(String soldTo, String vcodes) throws DataAccessException;
	  public List<MaterialExclusion> validateMaterialExclusion(List<String> materialCodes) throws DataAccessException;
	  public void updateDeletedSalesOut(List<String> materialCodes,String soldToId) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationProcessStepAndStatus(String registrationId, ProcessStepEnum step, StatusEnum status) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationSkipInstallBaseCreation(String registrationId, Boolean skipInstallBaseCreation) throws DataAccessException;
	  public SiteRegistration updateSiteRegistrationSalMigrationOnlyFlag(String registrationId, Boolean salMigrationOnlyFlag) throws DataAccessException;
	  public void updateSiteRegistrationStatusForDIrectUser(String regId, Status status) throws DataAccessException;
	  public String getSapBox(String soldTo) throws DataAccessException;
	  public int updateSiteRegistrationStatusOnRegId(String registrationId, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException ;
	  public SiteRegistration updateSiteRegistrationSubmittedFlag(String registrationId, String submitted) throws DataAccessException;
	  public List<TRConfig> getEligibleGroups(List<String> seCodes) throws DataAccessException;
	  public List<TechnicalOrder> saveTechnicalOrderList(List<TechnicalOrder> technicalOrderList) throws DataAccessException;
	  public Long saveSiteContactValidation(SiteRegistration siteRegistration)
		        throws DataAccessException;
	  public List<TechnicalOrder> getTechnicalOrderByType(String registrationId, String orderType)
	            throws DataAccessException ;
	  public void deleteTechnicalOrders(List<String> orderIds) throws DataAccessException;
	  public List<Object[]> getPendingOrders(String soldToNumber, String bpLinkId)
	            throws DataAccessException;
	  public List<SiteRegistration> getRegistrationSummaryList(String[] soldToIdList) throws DataAccessException;
	  public void cancelSiteRegistrationIBStatus(String regId, Status status) throws DataAccessException;
	  public boolean isBpSoldToExist(String soldTo) throws DataAccessException;
	  public boolean isSoldToExist(String soldTo) throws DataAccessException;
	  public boolean isSoldToValid(String soldTo) throws DataAccessException;
	  public Map<String, Set<String>> getMc2GroupIdMappings();
	  
	  //System to System SALGW Installer
	  public List<TRConfig> fetchTRConfigsForSalGWInstaller(String productIdentifier);
	  public TechnicalOrder getTechnicalOrderByFilter(String materialCode,String serialNumber)  throws DataAccessException;
	 }