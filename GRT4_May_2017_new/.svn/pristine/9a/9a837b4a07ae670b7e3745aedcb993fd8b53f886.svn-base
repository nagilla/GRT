package com.avaya.grt.service.installbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.avaya.grt.mappers.Alert;
import com.avaya.grt.mappers.Country;
import com.avaya.grt.mappers.MaterialCode;
import com.avaya.grt.mappers.MaterialExclusion;
import com.avaya.grt.mappers.Province;
import com.avaya.grt.mappers.Region;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.State;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.dto.Account;
import com.grt.dto.BusinessPartner;
import com.grt.dto.PaginationForSiteRegistration;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationList;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.SALGatewayInstallerDto;
import com.grt.dto.SALGatewayInstallerResponseDto;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface InstallBaseService {
	
	public Map<String, MaterialCode> getMaterialCodeForValidation(List<String> materialCodes);
	public SortedMap<String, String> getStateCode() throws DataAccessException;
	public SortedMap<String, String> getCountryCode() throws DataAccessException;
	public SortedMap<String, String> getProvinceCode() throws DataAccessException;
	 public Map<String,Map<Long,Region>> getRegionMap() throws DataAccessException;
	 public List<Alert> getActiveAlerts() throws DataAccessException;
	 public boolean isSoldToValid(String soldTo) throws DataAccessException;
	 public SiteRegistration getSiteRegistration(String registrationId) throws DataAccessException;
	 public List<Country> getCountryValues() throws DataAccessException;
	 public String isMegaUser(String userId) throws DataAccessException;
	 public Account getCustomerAccountDetails(String userName);
	 public BusinessPartner getBusinessPartner(String bpLinkId) throws DataAccessException;
	 public List<String> getCxpSoldToList(String userId, String bpLinkId) throws DataAccessException;
	 public boolean isSoldToValidForCurrentUser(String soldTo, String userId, String bpLinkId) throws DataAccessException;
	 public RegistrationSummary getRegistrationSummary(String registrationId) throws DataAccessException;
	 public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	 public void updateAccountInformation(SiteRegistration siteRegistration) throws DataAccessException;
	 public int saveSiteRegistrationDetails(SiteRegistration siteRegistration) throws DataAccessException;
	 public String getRegistrationId() throws DataAccessException;
	 public RegistrationList getRegistrationDetailFromCXPOrSalesOut(String soldToId) throws DataAccessException;
	 public int getExistingInstallBaseListCount (String soldToNumber) throws Exception;
	 public List<TechnicalOrderDetail> getExistingInstallBaseList(
	            String soldToNumber, int offSet, int fetchSize) throws Exception;
	 public List<TechnicalOrderDetail> getExistingInstallBaseList(
	            String soldToNumber) throws Exception;
	 public List<TechnicalOrderDetail> filterTechnicalOrderOnPipeline(List<TechnicalOrderDetail> filteredTODList, String soldTo, String process) throws DataAccessException;
	 public List<TechnicalOrderDetail> constructTechnicalOrderDetailListOnMC(List<TechnicalOrderDetail> technicalOrderDetailList) throws Exception;
	 public Object[] getLocalTechRegData(String materialCode, String releaseString)throws DataAccessException;
	 public List<TechnicalOrderDetail> fetchEquipmentRemovalRecords(String soldTo, String vCode, boolean fetchSALGWRecords) throws DataAccessException;
	 public Map<String, Object> validateMaterialExclusion(
	            List<String> materialCodes) throws DataAccessException;
	 public void updateDeletedSalesOut(List<String> materialCodes,String soldToId);
	 public String submitRegistration(String registrationId, String soldToId,
	    		List<TechnicalOrderDetail> result, String generateFileInPath,
	    		Boolean salMigrationOnlyFlag, Boolean skipInstallBaseCreation )
	    throws Exception;
	 public Map<String,String> getMaterialCodeDesc(List<String> materialCodes) throws DataAccessException;
	 public Map<String,List<String>> getAgreementsForAssets(List<String> materialCodes) throws DataAccessException;
	 public Map<String,List<String>> getAccessTypeForAssets(List<String> materialCodes) throws DataAccessException;
	 public List<TRConfig> isTREligible(String materialCode);
	 public List<TRConfig> isTREligible(List<String> materialCodes);
	 public void updateSiteRegistrationSubmittedFlag(String registrationId,String submitted);
	 public List<TRConfig> getEligibleGroups(List<String> seCodes) throws DataAccessException;
	 public String updateSiteRegistrationStatusForDIrectUser(String registrationId)  throws Exception;
	 public List<TechnicalOrderDetail> saveMaterialEntryList(List<TechnicalOrderDetail> materialEntryList) throws DataAccessException;
	 public SiteRegistration saveSiteRegistration(SiteRegistration siteRegistration, String userId) throws DataAccessException;
	 public List<TechnicalOrderDetail> getTechnicalOrderByType(
	            String registrationId, String orderType) throws DataAccessException;
	 public void deleteTechnicalOrders(List<String> orderIdList);
	// public List<MaterialExclusion> validateMaterialExclusion(List<String> materialCodes);
	 public SiteRegistration assemblerSiteRegistrationFromFormBean(RegistrationFormBean form) throws IOException;
	 public void setCXPRegistrationDataIntoFormBean(RegistrationList registrationList, RegistrationFormBean form);
	 public List<TechnicalOrderDetail> getPendingOrders(String soldToNumber, String bpLinkId) throws Exception;
	 public List<TechnicalOrderDetail> parseWorksheetXLSX(InputStream fileToParse)
	            throws Exception;
	 public List<TechnicalOrderDetail> parseWorksheet(InputStream fileToParse)
	            throws Exception;
	 public List<TechnicalOrderDetail> parseRecordValidationWorksheetXLSX(InputStream fileToParse)
	            throws Exception;
	 public List<TechnicalOrderDetail> parseRecordValidationWorksheet(InputStream fileToParse)
	            throws Exception;
	 //public List<RegistrationSummary> getRegistrationSummaryList(String userId) throws DataAccessException;
	 public void cancelSiteRegistrationIBStatus(String registrationId)  throws Exception;
	 public boolean isSoldToExcluded(String soldTo) throws DataAccessException;
	 //public PaginationForSiteRegistration getRegListFromDB(Map map, String userId, String requesterFullName, String userType, String bpLinkId, boolean ipoFilter) throws DataAccessException;
	 
	 //System to System Installer SALGW
	 public SALGatewayInstallerResponseDto processSALGatewayIBCreation(SALGatewayInstallerDto salGatewayInstallerDto);
	 public List<TRConfig> fetchTRConfigsForSalGWInstaller(String productIdentifier);
	 public TechnicalOrder getTechnicalOrderByFilter(String materialCode,String serialNumber)  throws DataAccessException;
	 //Added for IPOSS
	public List<String> getValidVersionsForIPOCoreUnit(String materialCode) throws DataAccessException;
	//Added for checking material code serialization
	public Map<String, Object> populateMaterialSerialize(List<String> materialCodes) throws DataAccessException;
	public Map<String, Object> populateMaterialMasterDetails(List<String> materialCodes) throws DataAccessException;

}
