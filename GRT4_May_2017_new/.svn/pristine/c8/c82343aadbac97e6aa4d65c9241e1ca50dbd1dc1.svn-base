package com.avaya.grt.web.action.viewinstallbase;

import org.apache.log4j.Logger;

import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.service.equipmentremoval.EQRService;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.action.installbase.InstallBaseCreationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GenericSort;
import com.grt.util.GRTConstants;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;
import com.opensymphony.xwork2.Action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class ViewInstallBaseAction extends TechnicalRegistrationAction {
	
	private static final Logger logger = Logger
			.getLogger(ViewInstallBaseAction.class);
	
	private static final String DATE_FORMAT1 = "MM/dd/yyyy HH:mm:ss";
	//FOR BFOWARNING
	private static final int SOLDTOID_MAX_LENGTH=50;

	
	private String dataError = "";
	private InstallBaseService installBaseService;
	private EQRService eqrService;
	private String dtSearchVal;
	private boolean toggleMore = false;
	
	public String getDataError() {
		return dataError;
	}

	public void setDataError(String dataError) {
		this.dataError = dataError;
	}

	public InstallBaseService getInstallBaseService() {
		return installBaseService;
	}

	public void setInstallBaseService(InstallBaseService installBaseService) {
		this.installBaseService = installBaseService;
	}
	
	public EQRService getEqrService() {
		return eqrService;
	}

	public void setEqrService(EQRService eqrService) {
		this.eqrService = eqrService;
	}

	/**
	 * This method redirects to the enter sold-to page depends upon the type of
	 * user
	 * 
	 * @return
	 */
	public String viewInstallBaseOnly() {
		try {
			validateSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		actionForm.setTechnicalRegistrationOnly(false); //TOB		
		actionForm.setRecordValidationOnly(false); //record validation
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setFullRegistrationOnly(false);
		actionForm.setSalMigrationOnly(false);
		
		actionForm.setInstallbaseRegistrationOnly(false);
		actionForm.setViewInstallbaseOnly(true);
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}
	
	public String viewInstallBaseProcess()
			throws Exception {
		
		//For BFOWARNING	
		if(!(validateBFOWarning())){
			return	Action.ERROR;
		}
		
		/*String soldToId = null;
		soldToId = getRequest().getParameter("soldToId");
		actionForm.setSoldTo(soldToId);
		actionForm.setSoldToId(soldToId);*/
		logger.debug("Entering viewInstallBaseProcess() method in ViewInstallBaseAction");
		validateSession();
		String strReturn = "";
   	 	if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
   	 		strReturn = newRegistrationWithSoldToValidation();
		} else {
			strReturn = newRegistrationWithSoldToValidationForAgent();
		}
   	 	
   	 	actionForm.setActveSRColumnOnly(false);
   	 	
   	 	//getRequest().getSession().removeAttribute("eqrFlag");
   	 	actionForm.setSaveSiteReg(true);
   	 	
   	 	//validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		String cause = "";
		actionForm.setMessage("");
		actionForm.setActveSRColumnOnly(true);
		actionForm.setBannerSrLabel(GRTConstants.TRUE);
		// User comes from Home screen->EQR only
		actionForm.setIBViewFileDownloaded(false);
		logger.debug("................ Starting for View Install Base records fetching ................");
		String soldToId = null;
		try {
			List<TechnicalOrderDetail> technicalOrderDetailList = null;
			if (StringEscapeUtils.unescapeHtml(actionForm.getSoldToId()) != null) {
				soldToId = StringEscapeUtils.unescapeHtml(actionForm.getSoldToId());
			} else if (getRequest().getParameter("soldToId") != null) {
				soldToId = getRequest().getParameter("soldToId");
				//actionForm.setSoldTo(soldToId);
			}
	
			if (!(soldToId.equalsIgnoreCase("Null") || soldToId.equalsIgnoreCase(""))) {
				
				// Fetching equipments from siebel and validating material codes existence against Material Exclusion table
				try{
					technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldToId, true);
					}catch(DataAccessException dataExcep){
					cause = "DataAccessException";
				}
				logger.debug("View Install Base MaterialList after MaterialExclusion:  size: " + technicalOrderDetailList.size());
				actionForm.setMaterialsAfterExclusionList(technicalOrderDetailList);
				logger.debug("SaveSite Flag:  "+actionForm.getSaveSiteReg());
				if(actionForm.getSaveSiteReg()){
					// Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 0);
					logger.debug("Club equipmennts based on MC + Summary EQN and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					// Filter pipeline records
					technicalOrderDetailList = getInstallBaseService().filterTechnicalOrderOnPipeline(technicalOrderDetailList, soldToId, "DISP");
					logger.debug("Filtered pipeline records:"+technicalOrderDetailList.size());
					// Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately
					technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 1);
					logger.debug("Club equipmennts based on MC and maitain TRed/Serial Numbered records seperately:"+technicalOrderDetailList.size());
					actionForm.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED);
					actionForm.setBannerSubStatus("");
					actionForm.setBannerSrNumber("");
					actionForm.setBannerActiveContractSrNumber("");
				} else {
					// Fetching the saved EQR records from technical order
					//Doubt here whether to include the boolean parameter at the end 
					//technicalOrderDetailList = getInstallBaseService().getTechnicalOrderByType(form.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV, true);
					technicalOrderDetailList = getInstallBaseService().getTechnicalOrderByType(actionForm.getRegistrationId(), GRTConstants.TECH_ORDER_TYPE_FV);
					/*if(!(technicalOrderDetailList != null && technicalOrderDetailList.size() > 0)){
						technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList);
					}*/
					RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
					actionForm.setBannerStatus(summary.getFinalValidationStatus());
					actionForm.setBannerSubStatus(summary.getFinalValidationSubStatus());
					actionForm.setBannerSrNumber(summary.getFinalValidationSrNo());
					actionForm.setBannerActiveContractSrNumber(StringUtils.isNotEmpty(summary.getActiveSR())?summary.getActiveSR():GRTConstants.NA);
					actionForm.setCompany(summary.getRequestingCompany());
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
					if(summary.getEqrSubmittedDate() != null){
						actionForm.setBannerSubmittedDate(sdf.format(summary.getEqrSubmittedDate()));
					} else {
						actionForm.setBannerSubmittedDate("");
					}
					if(summary.getEqrCompletedDate() != null){
						actionForm.setBannerCompletedDate(sdf.format(summary.getEqrCompletedDate()));
					} else {
						actionForm.setBannerCompletedDate("");
					}
				}
				// Set all the Technical Order records as readonly based on form:readonly
				for(TechnicalOrderDetail techDto : technicalOrderDetailList){
					if(actionForm.isReadOnly()){
						techDto.setExclusionFlag(true);
					}
				}
				// Fetch pipeline IB and EQR records
				List<PipelineSapTransactions> pipelineList = getEqrService().getConsolidatedPipelineRecords(soldToId);
				if(pipelineList != null && pipelineList.size() > 0){
					logger.debug("Consolidated pipeline List size:"+pipelineList.size());
					for(TechnicalOrderDetail techDto : technicalOrderDetailList){
						for(PipelineSapTransactions pipelineDto : pipelineList){
							if(techDto.getMaterialCode().equalsIgnoreCase(pipelineDto.getMaterialCode())){
								if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_FV)){
									techDto.setPipelineEQRQuantity(pipelineDto.getQuantity());
								} else if(pipelineDto.getAction().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_IB)){
									techDto.setPipelineIBQuantity(pipelineDto.getQuantity());
								}
							}
						}
					}
				} else {
					logger.debug("Consolidated pipeline List size:0");
				}
				CSSPortalUser cssPortalUser = getUserFromSession();
				String userType  = cssPortalUser.getUserType();
				if(!StringUtils.isEmpty(userType)){
					if (!userType.equals("B")
							&& !userType.equals("C")){
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.TRUE);
					} else {
						getRequest().getSession().setAttribute(GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.FALSE);
					}
				}
				
				if(!StringUtils.isEmpty(actionForm.getRegistrationId())){
					RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
					if (summary.getFinalValidationStatusId() !=null
							&& !summary.getFinalValidationStatusId().equalsIgnoreCase(GRTConstants.AWAITNG_INFORMATION)) {
						for (TechnicalOrderDetail detail : technicalOrderDetailList) {
							if(StringUtils.isEmpty(detail.getExclusionSource())){
								detail.setErrorDescription("");
							}
						}
					}
				}
				
				
				try{
					logger.debug("View Install Base MaterialList after exclusion:  size: " + technicalOrderDetailList.size());
					GenericSort gs = new GenericSort("productLine", true);
					Collections.sort(technicalOrderDetailList, gs);
				}catch(Exception e){
					logger.error("Exception in View Install Base while sorting data : "+e.getMessage());

				}
				
				//Integration with Siebel Query to get the Contarct Numbers.
				getAgreementsForAssets(technicalOrderDetailList);
				
				//Integration with ART logic to get the Asset Types.
				getAccessTypeForAssets(technicalOrderDetailList);
				
				
				int size=Integer.parseInt(grtConfig.getInstallBaseListSize());
				actionForm.setListSizeFlag(false);
				
				if(technicalOrderDetailList.size()>size){
					actionForm.setListSizeFlag(true);
					technicalOrderDetailList = new ArrayList<TechnicalOrderDetail>(technicalOrderDetailList.subList(0, size));
				}
				
				
				actionForm.setMaterialEntryList(technicalOrderDetailList);
				
			}
			getRequest().setAttribute("materialList", actionForm.getMaterialEntryList());
			long c2 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Ending for View Install Base records fetching ................");
	        logger.debug("TIMER for View Install Base rendering for RegID:"+actionForm.getRegistrationId()+" time in milliseconds :" + (c2-c1));
	        if("DataAccessException".equals(cause)){
	        	this.setDataError("Transaction timed out while fetching the data from Siebel");
	        }else{
	        	this.setDataError("No Results Found.");
	        }
		} catch(DataAccessException daexception){
			logger.error("DataAccessException in View Install Base"+daexception.getMessage());
			throw new DataAccessException(ViewInstallBaseAction.class, daexception
					.getMessage(), daexception);
		} catch (Exception e) {
			logger.error("Exception in View Install Base"+e.getMessage());
		}
		
		actionForm.setSoldTo("");
		
		if(strReturn.equalsIgnoreCase("toLocationSelection")){
			return "toLocationSelection";
		}else if(strReturn.equalsIgnoreCase("toAgentLocationSelection")){
			return "toAgentLocationSelection";
		}
		else{
			return "success";
		}
	}
	//For BFOWARNING
	public boolean	validateBFOWarning(){
		if(actionForm !=null && actionForm.getSoldToId() != null && actionForm.getSoldToId().length() >SOLDTOID_MAX_LENGTH){
			return false;
		}
		return true;
	}
	
	public String generateIBWorkSheet() throws Exception {
		validateSession();
		//For BFOWARNING	
		if(!(validateBFOWarning())){
			return	Action.ERROR;
		}
		//String filepath = "../portaldomain/tmp/EQR.xls";
		//String filepath = "/EQR.xls";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		DateFormat dateFormatForExcel = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//get current date time with Date()
		Date date = new Date();
		String dateString = dateFormat.format(date);
		String dateStringForExcel = dateFormatForExcel.format(date);
		
		String soldToIdVal = actionForm.getSoldToId();
		soldToIdVal = StringEscapeUtils.unescapeHtml(soldToIdVal);
		
		String filename = "IB_data_"+soldToIdVal+"_"+dateString+".xls";
		String filepath = "/tmp/"+filename;
		//String filepath = filename;
		
		TechnicalOrderDetailWorsheetProcessor workSheetProcessor = new TechnicalOrderDetailWorsheetProcessor();
		workSheetProcessor.generateIBWorksheet(actionForm, filepath, dateStringForExcel);
		
		//actionForm.setEqrFileDownloaded(true);
		actionForm.setIBViewFileName(filename);
		actionForm.setIBViewFileDownloaded(true);
		
		return "success";
	}
	
	//Method for getting the agreements associated with a particular asset.
	private List<TechnicalOrderDetail> getAgreementsForAssets(List<TechnicalOrderDetail> result) throws Exception {
		List<String> assetIds = new ArrayList<String>();
		for (TechnicalOrderDetail detail : result) {
			if (detail.getAssetPK() != null
					&& StringUtils.isNotEmpty(detail.getMaterialCode())) {
				assetIds.add(detail.getAssetPK());
			}
		}
		if (!assetIds.isEmpty()) {
			/*
			 * Map<String, String> mDescMap = getRegistrationDelegate()
			 * .getMaterialCodeDescription(materialCodes);
			 */
			Map<String, List<String>> materialAgreementsMap = getInstallBaseService()
					.getAgreementsForAssets(assetIds);

			//TODO Modify the logic to create the agreements for the asset ids.
			for (TechnicalOrderDetail detail : result) {
				List<String> agreementsList = materialAgreementsMap.get(detail.getAssetPK());
				if(null != agreementsList) {
					detail.setAgreements(agreementsList.toString());
					//More/Less Issue
						if(agreementsList.size()>3){
							detail.setToggleMore(true);
							detail.setToggleAgreementsList(agreementsList.subList(0,3));
						}else{
							detail.setToggleMore(false);
						}
				}
			}
		}
		logger.debug("size after validating code" + result.size());
		return result;
	}
	
	//Method for getting the access type associated with a particular asset.
	private List<TechnicalOrderDetail> getAccessTypeForAssets(List<TechnicalOrderDetail> result) throws Exception {
			List<String> assetIds = new ArrayList<String>();
			for (TechnicalOrderDetail detail : result) {
				if (detail.getAssetPK() != null
						&& StringUtils.isNotEmpty(detail.getMaterialCode())) {
					assetIds.add(detail.getAssetPK());
				}
			}
			if (!assetIds.isEmpty()) {
				/*
				 * Map<String, String> mDescMap = getRegistrationDelegate()
				 * .getMaterialCodeDescription(materialCodes);
				 */
				Map<String, List<String>> accessTypeMap = getInstallBaseService()
						.getAccessTypeForAssets(assetIds);
				
				//TODO Modify the logic to create the agreements for the asset ids.
				for (TechnicalOrderDetail detail : result) {
					List<String> accessTypeList = accessTypeMap.get(detail.getAssetPK());
					
					if(null != accessTypeList) {
						String accessType = getAssetAccessType(accessTypeList, detail.getAssetPK());
						detail.setAccessType(accessType);
					}else {
						String accessType = "No Data";
						detail.setAccessType(accessType);
					}
				}
			}
			logger.debug("size after validating code" + result.size());
			return result;
		}
	
	//TODO Implement the method to get the access type from the list
	//List can have multiple values in the order [TYPE, X_SUBTYPE, X_IP_ADDRESS, X_PHONE, TYPE, X_SUBTYPE, X_IP_ADDRESS, X_PHONE]
	private String getAssetAccessType(List<String> accessTypeList, String assetId) {
		if(isValidCustomerIPAddress(accessTypeList, assetId)) {
			if(isValidServiceIPAddress(accessTypeList)) {
				if(isValidSSGSECode(assetId)) {
					return "SSG/SIG";
				} else if(isValidSALSECode(assetId)) {
					return "SAL";
				}
			}
			return "Bad Data";
		}else if(isValidCLANIPAddress(accessTypeList)) {
			return "SSLVPN";
		}else if(isValidPhone(accessTypeList)) {
			if(isValidRASIPAddress(accessTypeList)) {
		    	return "PPP";
			}
			return "Modem";
		}else if(isValidIPAddressPresent(accessTypeList) || isValidPhoneNumberPresent(accessTypeList)) {
			return "Bad Data";
		} else {
			return "No Data";
		}
	}
	
	private boolean isValidCustomerIPAddress(List<String> accessTypeList, String assetId) {
		//Prepare a list with TYPE=IPAddr, X_SUBTYPE=Customer, X_IP_ADDRESS = (628)084-7511, X_PHONE = *****, TYPE=IPAddr, X_SUBTYPE=Avaya, X_IP_ADDRESS=172.18.10.198, X_PHONE=*****
		//In siebel.s_asset_xm, Type = 'IPAddr', X_SUBTYPE = 'Customer',and X_IP_ADDRESS is a valid and active SE ID  
		//with the SE code of (V)SALGW for SAL or SSGA, SSGS, SSGR SE code for SSG/SIG
		boolean hasCustomerSubType = false;
		boolean hasAvayaSubType = false;
		for(int index=0; index < accessTypeList.size()-1; index++) {
			if(null != accessTypeList.get(index) && null != accessTypeList.get(index+1)) {
				if(("IPAddr").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("Customer").equalsIgnoreCase(accessTypeList.get(index+1).trim())) {
					String seid = accessTypeList.get(index+2);
					if(validSEID(seid)) {
						hasCustomerSubType = true;
					} else {
						hasCustomerSubType = false;
					}
				}else if(("IPAddr").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("Avaya").equalsIgnoreCase(accessTypeList.get(index+1).trim())) {
					hasAvayaSubType = true;
				}
			}
		}
		if(hasCustomerSubType && hasAvayaSubType) {
			//TODO EL for the seid in IP_Address column.
			if(validSECode(assetId)) {			
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean validSEID(String seid) {
		String PATTERN = "(?:(?:(\\([0-9]{3}\\)))(?:([0-9]{3}))(?:([-]([0-9]{4}$))))";
		Pattern pattern = Pattern.compile(PATTERN);
		if(null != seid && StringUtils.isNotEmpty(seid)) {
			//String testString = "(123)124-25211+(123)124-2521+(987)124-25211+(123)124-2521";
			//String testString = "(123)124-2521";
			String[] parts = seid.split("\\+");
			if(null != parts && parts.length > 3) {
				return false;
			}
			if(null != parts) {
				for(String sid : parts) {
					Matcher matcher = pattern.matcher(sid);
					if (! matcher.find()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidSSGSECode(String assetId) {
		for(TechnicalOrderDetail tod : actionForm.getMaterialEntryList()) {
			if(tod.getAssetPK() != null) {
				if(tod.getAssetPK().equalsIgnoreCase(assetId)) {
					if(tod.getSolutionElementCode().equalsIgnoreCase("SSGA") || tod.getSolutionElementCode().equalsIgnoreCase("SSGS") ||
						tod.getSolutionElementCode().equalsIgnoreCase("SSGR")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isValidSALSECode(String assetId) {
		for(TechnicalOrderDetail tod : actionForm.getMaterialEntryList()) {
			if(tod.getAssetPK() != null) {
				if(tod.getAssetPK().equalsIgnoreCase(assetId)) {
					if(tod.getSolutionElementCode().equalsIgnoreCase("VSALGW") || tod.getSolutionElementCode().equalsIgnoreCase("SALGW")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean validSECode(String assetId) {
		for(TechnicalOrderDetail tod : actionForm.getMaterialEntryList()) {
			if(tod.getAssetPK() != null) {
				if(tod.getAssetPK().equalsIgnoreCase(assetId)) {
					if(tod.getSolutionElementCode().equalsIgnoreCase("VSALGW") || tod.getSolutionElementCode().equalsIgnoreCase("SALGW") || 
							tod.getSolutionElementCode().equalsIgnoreCase("SSGA") || tod.getSolutionElementCode().equalsIgnoreCase("SSGS") ||
							tod.getSolutionElementCode().equalsIgnoreCase("SSGR")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isValidServiceIPAddress(List<String> accessTypeList) {
		//Type = 'IPAddr' and X_SUBTYPE = 'Avaya' and X_IP_ADDRESS is in 172.[18, 20, 21].x.x format where x is in [0..255]
		Pattern pattern = null;
		Matcher matcher = null;
		final String SIPADDRESS_PATTERN = 
				"^(172)\\." +
						"(18|20|21)\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		/*final String SIPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";*/
		for(int index=0; index < accessTypeList.size()-1; index++) {
			if(null != accessTypeList.get(index) && null != accessTypeList.get(index+1)) {
				if(("IPAddr").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("Avaya").equalsIgnoreCase(accessTypeList.get(index+1).trim())) {
					String ip = accessTypeList.get(index+2);
					if(null != ip) {
						pattern = Pattern.compile(SIPADDRESS_PATTERN);
						matcher = pattern.matcher(ip);
						return matcher.matches();	
					}
				}
			}
		}
		return false;
	}
	
	private boolean isValidCLANIPAddress(List<String> accessTypeList) {
		//Type = 'IPAddr' and X_SUBTYPE  = 'CLAN' and X_IP_ADDRESS is in 192(x or 168).x.x or 172.(x or 23).x.x. 
		Pattern pattern = null;
	    Matcher matcher = null;
		final String CLANIPADDRESS_PATTERN = 
				"^(192|172)\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	    /*final String CLANIPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";*/
		
	    for(int index=0; index < accessTypeList.size()-1; index++) {
	    	if(null != accessTypeList.get(index) && null != accessTypeList.get(index + 1)) {
	    		if(("IPAddr").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("CLAN").equalsIgnoreCase(accessTypeList.get(index + 1).trim())) {
	    			String ip = accessTypeList.get(index + 2);
	    			if(null != ip) {
	    				pattern = Pattern.compile(CLANIPADDRESS_PATTERN);
	    				matcher = pattern.matcher(ip);
	    				return matcher.matches();
	    			}
	    		}
	    	}
	    }
		return false;
	}
	
	private boolean isValidPhone(List<String> accessTypeList) {
		//Type = 'Phone' and X_SUBTYPE = 'Async' and X_PHONE is not null.  
		//US: 10 digit numbers (/^\d{10,10}$/)
		//International: ‘+’ + <country code> + <one space> + <area code> +  {<one space> } + <rest of numbers>.  
		//One space between <area code> and <rest of numbers> is optional depending on country.  (/^\+\d \d+ ?\d+$/)
		for(int index=0; index < accessTypeList.size()-3; index++) {
			if(null != accessTypeList.get(index) && null != accessTypeList.get(index + 1)) {
				if(("Phone").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("Async").equalsIgnoreCase(accessTypeList.get(index + 1).trim()) && null != accessTypeList.get(index + 3)) {
					String phoneNumber = accessTypeList.get(index + 3);
					if(phoneNumber.length() == 10) {
						Pattern pattern = Pattern.compile("\\d{10}");
						Matcher matcher = null;
						matcher = pattern.matcher(phoneNumber);
						return matcher.matches();	
					}else {
						Pattern pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
						Matcher matcher = null;
						matcher = pattern.matcher(phoneNumber);
						return matcher.matches();	
					}
				}
			}
		}
		return false;
	}
	
	private boolean isValidRASIPAddress(List<String> accessTypeList) {
		//Type = 'IPAddr' and X_SUBTYPE = 'Avaya' and X_IP_ADDRESS is in 10.x.x.x format
		Pattern pattern = null;
		Matcher matcher = null;
		final String RASIPADDRESS_PATTERN = 
				"^(1[0])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		/*final String RASIPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";*/		
		for(int index=0; index < accessTypeList.size()-2; index++) {
			if(null != accessTypeList.get(index) && null != accessTypeList.get(index + 1)) {
				if(("IPAddr").equalsIgnoreCase(accessTypeList.get(index).trim()) && ("Avaya").equalsIgnoreCase(accessTypeList.get(index + 1).trim())) {
					String ip = accessTypeList.get(index + 2);
					if(null != ip) {
						pattern = Pattern.compile(RASIPADDRESS_PATTERN);
						matcher = pattern.matcher(ip);
						return matcher.matches();	
					}
				}
			}
		}
		return false;
	}
	
	private boolean isValidIPAddressPresent(List<String> accessTypeList) {
		//Type = 'IPAddr' and X_SUBTYPE = 'Avaya' and X_IP_ADDRESS is in 10.x.x.x format
		Pattern pattern = null;
		Matcher matcher = null;
		final String IPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		for(int index =0; index < accessTypeList.size(); index += 4) {
			String ip = accessTypeList.get(index + 2);
			pattern = Pattern.compile(IPADDRESS_PATTERN);
			if(null != ip) {
				matcher = pattern.matcher(ip);
				if( matcher.matches()) {
					return true;	
				}
			}
		}
		return false;
	}
	
	private boolean isValidPhoneNumberPresent(List<String> accessTypeList) {
		for(int index =0; index < accessTypeList.size(); index += 4) {
			String phoneNumber = accessTypeList.get(index + 3);
			if(null != phoneNumber) {
				if(phoneNumber.length() == 10) {
					Pattern pattern = Pattern.compile("\\d{10}");
					Matcher matcher = null;
					matcher = pattern.matcher(phoneNumber);
					if( matcher.matches()) {
						return true;	
					}
				}else {
					Pattern pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
					Matcher matcher = null;
					matcher = pattern.matcher(phoneNumber);
					if( matcher.matches()) {
						return true;	
					}	
				}
			}
		}
		return false;
	}
	
	@Override
	public String technicalRegistrationDashboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String installBaseCreation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String viewInstallBaseProcessNew() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String viewInstallBaseData() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDtSearchVal() {
		return StringEscapeUtils.unescapeHtml(dtSearchVal);
	}

	public void setDtSearchVal(String dtSearchVal) {
		this.dtSearchVal = dtSearchVal;
	}
	
	public boolean isToggleMore() {
		return toggleMore;
	}

	public void setToggleMore(boolean toggleMore) {
		this.toggleMore = toggleMore;
	}

	@Override
	public String salGatewayMigrationList() {
		// TODO Auto-generated method stub
		return null;
	}
}
