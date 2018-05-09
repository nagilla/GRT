package com.avaya.grt.web.action.installbase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.MasterMaterial;
import com.avaya.grt.mappers.RegistrationType;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.grt.dto.IPOProductRegistration;
import com.grt.dto.MaterialCodeListDto;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.integration.siebel.SiebelClient;
import com.grt.util.GRTConstants;
import com.grt.util.GenericSort;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
import com.grt.util.TechnicalOrderDetailUtil;
import com.opensymphony.xwork2.Action;

public class InstallBaseCreationAction extends TechnicalRegistrationAction {

	private static final Logger logger = Logger
			.getLogger(InstallBaseCreationAction.class);

	private String soldToError;

	protected Boolean salMigrationOnlyFlag = false;

	private List<MaterialCodeListDto> materialCodeList = new ArrayList<MaterialCodeListDto>();

	private Boolean ipoContinueInstallBase = false;

	private String uploadMessage = "";

	private List<TechnicalOrderDetail> pendingOrders = new ArrayList<TechnicalOrderDetail>();

	protected static final String ATTACHMENT_NAME = "InstallBaseCreationAttachment";

	private static final String ATTACHMENT_NAME_EXTENSION = ".xls";

	private static final String DATE_FORMAT_APPEND_ATTACHMENT_NAME = "yyyyMMddHHmmss";

	private static final String DEFAULT_UPLOAD_DIR = "upload";

	private List<TechnicalOrderDetail> technicalOrderDetailList;

	private String materialExclusionFlag = "";

	private Object[] materialCodeArray;

	private String materialCode;

	private Boolean isFirstTime = true;

	private boolean manuallyAddMaterial;

	private String[] materialCodes;

	private String[] srNumbers;

	private String[] orderIds;

	private String[] serialNumberFlag;

	protected List<TRConfig> retList;

	private String seCodes;

	private boolean installBaseEnableSubmit;

	private boolean saveAssistIPO;

	private static final int INSTALL_BASE_CREATION_PENDING_ORDER_FETCH_SIZE = 5;

	private static final String DATE_FORMAT1 = "MM/dd/yyyy HH:mm:ss";

	private Boolean isFirstTimePendingOrder = true;

	protected InstallBaseService installBaseService;

	protected Map<String, Object> materialCodeMap;
	
	private List<String> serializedContants =  Arrays.asList("1000","Z003","1001","0001","Z001","Z002","0002");



	/**
	 * This method redirects to the enter sold-to page depends upon the type of
	 * user
	 * 
	 * @return
	 */
	public String getInstallbaseRegistrationOnly() {
		try {
			validateSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
		actionForm.setRecordValidationOnly(false); //record validation
		actionForm.setViewInstallbaseOnly(false); //view installbase
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setTechnicalRegistrationOnly(false); //tob
		actionForm.setFirstColumnOnly(false); //tob
		actionForm.setFullRegistrationOnly(false);
		actionForm.setSalMigrationOnly(false);
		
		//set the install base flag
		actionForm.setInstallbaseRegistrationOnly(true);
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}

	/**
	 * Submit action for install base creation
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validateMaterialCodesAgainstSoldTo() throws Exception {

		validateSession();

		logger.debug("FINALLY GOT THE USER ROLE..." + actionForm.getUserRole());
		logger.debug("Starting Validations for Install Base creation");
		// Begin to implement the Expand and Collapse for Pagination
		getRequest().getSession().removeAttribute(
				GRTConstants.EIB_PAGINATION_BUTTON_CLICKED);
		getRequest().getSession().removeAttribute(
				GRTConstants.SOIR_PAGINATION_BUTTON_CLICKED);
		// End to implement the Expand and Collapse for Pagination
		long c1 = Calendar.getInstance().getTimeInMillis();
		
		logger.debug("validateMaterialCodesAgainstSoldTo: generatedToken valid");

		List<TechnicalOrderDetail> selected = new ArrayList<TechnicalOrderDetail>();
		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		this.ipoContinueInstallBase = false;
		this.uploadMessage = "";
		
		//Defect#679 Remove unselected material codes from  actionForm.materialEntryList
		List<TechnicalOrderDetail> finalSelected = new ArrayList<TechnicalOrderDetail>();
		for (TechnicalOrderDetail detail : actionForm.getMaterialEntryList()) {
			if (!"remove".equalsIgnoreCase(detail.getErrorDescription()) ) {
				finalSelected.add(detail);
			}
		}
		actionForm.setMaterialEntryList(finalSelected);
		//End Defect#679
		
		if (actionForm.getUserRole() != null
				&& actionForm.getUserRole().equalsIgnoreCase("C")) {
			logger.debug("No material code validation for direct users");
		} else {
			for (TechnicalOrderDetail detail : actionForm
					.getMaterialEntryList()) {
				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					selected.add(detail);
				}
			}
			if (actionForm.getPendingOrders() != null) {
				for (TechnicalOrderDetail detail : actionForm.getPendingOrders()) {
					if (detail.getSelectforRegistration() != null
							&& detail.getSelectforRegistration()) {
						selected.add(detail);
					}
				}
			}
			try {
				logger.debug("Validate Material Code against Sold To");
				if (!actionForm.isIpoRegistration()) {
					// Begin: To remove the validation for IB which does not
					// allow same soldTo+mc combination across multiple
					// Registrations
					// End: To remove the validation for IB which does not allow
					// same soldTo+mc combination across multiple Registrations
					if (this.materialCodeList.size() > 0) {
						logger.debug("Ended Validations for Install Base creation Found MCs with Registration in Process returning to same Page");
						long c3 = Calendar.getInstance().getTimeInMillis();
						logger.debug("TIMER:" + (c3 - c1) + " milliseconds");
						return "samepage";
					}
				}

			} catch (Exception e) {
				setErrorMessage(e.getMessage());
				e.printStackTrace();
				logger.error("Error while validating the material Code", e);
			}
			logger.debug("Ended Validations for Install Base creation will Continue to SAP");
		}
		long c4 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER:" + (c4 - c1) + " milliseconds");

		return submitRegistration(actionForm);
	}

	/**
	 * Process Install base creation request.
	 *  
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public String submitRegistration(RegistrationFormBean form)
			throws Exception {
		validateSession();
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("submitRegistration: generatedToken valid");

		List<TechnicalOrderDetail> selected = new ArrayList<TechnicalOrderDetail>();

		List<TechnicalOrderDetail> pendingSelected = new ArrayList<TechnicalOrderDetail>();

		String returnCode = null;
		String message = null;

		try {
			for (TechnicalOrderDetail detail : form.getMaterialEntryList()) {

				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					selected.add(detail);
				}
			}

			for (TechnicalOrderDetail detail : actionForm.getPendingOrders()) {

				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					pendingSelected.add(detail);
				}
			}

			logger.debug("materialCodeList" + materialCodeList.size());
			if (materialCodeList.size() > 0) {
				logger.debug(" Inside materialCodeList"
						+ materialCodeList.size());
				int size = selected.size();
				int pendingSize = pendingSelected.size();
				for (int i = 0; i < size; i++) {
					TechnicalOrderDetail techOrder = selected.get(i);
					String matCode = techOrder.getMaterialCode();
					for (MaterialCodeListDto materialCodeListDto : materialCodeList) {
						if (materialCodeListDto.getMaterialCode().equals(
								matCode)) {
							selected.remove(i);
							size--;
							i--;
						}
					}
				}
				for (int j = 0; j < pendingSize; j++) {
					TechnicalOrderDetail techOrder = pendingSelected.get(j);
					String matCode = techOrder.getMaterialCode();
					for (MaterialCodeListDto materialCodeListDto : materialCodeList) {
						if (materialCodeListDto.getMaterialCode().equals(
								matCode)) {
							pendingSelected.remove(j);
							pendingSize--;
							j--;
						}
					}
				}

				form.setMaterialEntryList(selected);
				form.setPendingOrders(pendingSelected);
				pendingOrders = form.getPendingOrders();
				logger.debug("form.getMaterialEntryList() Size"
						+ form.getMaterialEntryList().size());
				logger.debug("form.getPendingOrders() Size"
						+ actionForm.getPendingOrders().size());
				this.materialCodeList = new ArrayList<MaterialCodeListDto>();
			}

		} catch (Exception e) {
			logger.error("Exception while iterating", e);
		}
		try {
			String seCode = "";
			String prodType = "";
			IPOProductRegistration IPOData = new IPOProductRegistration();
			TechnicalOrderDetail toTechRegister = new TechnicalOrderDetail();

			if (form.isIpoRegistration()
					&& (form.getRemoteConnectivity().equalsIgnoreCase("Yes"))) {
				Object[] TechregData = null;
				logger.debug("In tech reg swamy NEW request");

				for (TechnicalOrderDetail ts : selected) {
					if (ts.getIsBaseUnit() != null
							&& ts.getIsBaseUnit().equalsIgnoreCase("Y")) {
						logger.debug("Found Basae unit with mc"
								+ ts.getMaterialCode());
						toTechRegister = ts;
						break;
					}
					logger.debug("Found Basae unit with mc after if "
							+ toTechRegister.getMaterialCode());
				}
				logger.debug("Found Basae unit with after for "
						+ toTechRegister.getMaterialCode());
				if (toTechRegister != null
						&& toTechRegister.getMaterialCode() != "") {
					logger.debug("tech register not null");
					String mcdtoReg = toTechRegister.getMaterialCode();
					String rlsStrngtoReg = toTechRegister.getReleaseString();
					try {

						TechregData = getInstallBaseService()
								.getLocalTechRegData(mcdtoReg, rlsStrngtoReg);

						if (TechregData != null) {
							logger.debug("Found techregistration SE Code and Product type");
							seCode = ((String) TechregData[1]);
							prodType = (String) TechregData[2];
						} else {
							this.getRequest()
							.setAttribute("BaseUnitError",
									grtConfig.getBaseUnitError());
							ipoContinueInstallBase = true;
							logger.debug("ipoContinueInstallBase set to true No secode prd type found");
							// saveToken(getRequest());
							long c2 = Calendar.getInstance().getTimeInMillis();
							logger.debug("Exiting Submit Registration in RegistrationController No SE code Prod Type Mapping Found");
							logger.debug("TIMER:" + (c2 - c1) + " milliseconds");
							return "samepage";
						}

					} catch (Throwable ex) {
						ex.printStackTrace();
						logger.debug("Error in getlocaltech reg");

					}

				} else {
					this.getRequest()
					.setAttribute("BaseUnitError",
							grtConfig.getBaseUnitError2());
					ipoContinueInstallBase = true;
					logger.debug("ipoContinueInstallBase set to true No secode prd type found");
					long c2 = Calendar.getInstance().getTimeInMillis();
					logger.debug("Exiting Submit Registration in RegistrationController No Base Unit Found IPO registration Cannot be done");
					logger.debug("TIMER:" + (c2 - c1) + " milliseconds");
					return "samepage";
				}

			}
			if (form.getMaterialEntryList().size() > 0
					|| actionForm.getPendingOrders().size() > 0 || salMigrationOnlyFlag
					|| form.getSkipInstallBaseCreation()) {
				form.setSubmitted(GRTConstants.isSubmitted_true);
				form.setIsSrCompleted(GRTConstants.YES);
				form = saveSiteRegWithSumMaterialCode(form);
			}

			collectRequestParam(form);

			List<TechnicalOrderDetail> result = new ArrayList<TechnicalOrderDetail>();

			for (TechnicalOrderDetail detail : form.getMaterialEntryList()) {
				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					result.add(detail);
				}
			}

			for (TechnicalOrderDetail detail : actionForm.getPendingOrders()) {
				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					result.add(detail);
				}
			}
			if ( actionForm.getPendingOrders().size() > 0) {
				List<String> materialCodes = new ArrayList<String>();
				for (TechnicalOrderDetail detail : actionForm.getPendingOrders()) {
					if (detail.getSelectforRegistration() != null
							&& detail.getSelectforRegistration()) {
						materialCodes.add(detail.getMaterialCode());
					}
				}

				getInstallBaseService().updateDeletedSalesOut(materialCodes,
						form.getSoldToId());

			}
			if (!form.getMaterialEntryList().isEmpty()) {
				logger.debug("RegistrationFormBean.getMaterialEntryList().size(): "
						+ form.getMaterialEntryList().size());
			}

			String generateAttachmentInPath = autoGenAttachmentPath(ATTACHMENT_NAME);
			logger.debug("List Result----" + result);
			logger.debug("Before checking form.getUserRole()"
					+ form.getUserRole());
			if (result.size() > 0 || salMigrationOnlyFlag
					|| form.getSkipInstallBaseCreation()) {

				this.deleteTechnicalOrders(form, result);

				returnCode = getInstallBaseService().submitRegistration(
						form.getRegistrationId(), form.getSoldToId(), result,
						generateAttachmentInPath, salMigrationOnlyFlag,
						form.getSkipInstallBaseCreation());

				if (returnCode.equals(GRTConstants.EQR_successCode)) {
					message = grtConfig.getEwiMessageCodeMap().get("EQR_successCode") + "###" + grtConfig.getEQR_successCode();
				} else if (returnCode.equals(GRTConstants.EQR_errorCode)) {
					message = grtConfig.getEwiMessageCodeMap().get("EQR_errorCode") + "###" + grtConfig.getEQR_errorCode();
				} else if (returnCode.equals(GRTConstants.sapDown_errorCode)) {
					message = grtConfig.getEwiMessageCodeMap().get("sapDown_errorCode") + "###" + grtConfig.getSapDown_errorCode();
				} else if (returnCode.equals(GRTConstants.fmw_errorCode)) {
					message = grtConfig.getEwiMessageCodeMap().get("fmw_errorCode") + "###" + grtConfig.getFmw_errorCode();
				} else if (returnCode.equals(GRTConstants.exception_errorcode)) {
					message = grtConfig.getEwiMessageCodeMap().get("exception_errorcode") + "###" + grtConfig.getException_errorcode();
				} else if (returnCode
						.equals(GRTConstants.sapdestination_notfound)) {
					message = grtConfig.getEwiMessageCodeMap().get("sapdestination_notfound") + "###" + grtConfig.getSapdestination_notfound().replace("<SOLDTOID>", form.getSoldToId());
							 
				}
			} else {
				message =  grtConfig.getEwiMessageCodeMap().get("materialEmptyListRC") + "###" + grtConfig.getMaterialEmptyListRC();
				returnCode = GRTConstants.materialEmptyListRC;
			}
			logger.debug("After  message populated :: the size of pending orders in the form are==============SR3=========================>"
					+ form.getPendingOrders().size());
			if (form.getMaterialEntryList() != null
					&& form.getMaterialEntryList().size() > 0) {
				for (TechnicalOrderDetail tod : form.getMaterialEntryList()) {
					logger.debug("The value of IS_BASEUNIT::"
							+ tod.getIsBaseUnit());
					if (tod.getIsBaseUnit() != null
							&& tod.getIsBaseUnit().equals("Y")) {
						tod.setCoreUnitSelected("TRUE");
					} else {
						tod.setCoreUnitSelected("FALSE");
					}
				}
			}

			form.setMessage(message);
			form.setReturnCode(returnCode);
			logger.debug("----Message shown to user----" + message);
			logger.debug("----Return Code----" + returnCode);
			logger.debug("type of IMP" + form.getRemoteConnectivity());
			RegistrationSummary summary = getInstallBaseService()
					.getRegistrationSummary(form.getRegistrationId());
			Boolean ipoSubmitRequired = false;

			if (summary != null && summary.getTechRegStatusId() != null
					&& "1005".equals(summary.getTechRegStatusId())) {
				logger.debug("REGID tech status" + summary.getTechRegStatusId());
				logger.debug("Setting IPO submit required to true");
				ipoSubmitRequired = true;
			}

			String forwardName;
			if (salMigrationOnlyFlag) {
				form.setSalMigrationOnly(GRTConstants.YES);
				forwardName = "salMigrationOnly";
			} else {
				logger.debug("Directing to same page");
				forwardName = "samepage";

			}
			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Exiting Submit Registration in RegistrationController After SAP Processing");
			logger.debug("TIMER:" + (c2 - c1) + " milliseconds");
			return forwardName;

		} catch (Exception e) {

			logger.debug("Error happened while calling Submit registration"
					+ e.getMessage());
			logger.error("Error in submit", e);
			logger.debug("Error in submit" + e.getMessage());
			e.printStackTrace();
			message = grtConfig.getEwiMessageCodeMap().get("ibFailedInProcessingErrMsg") + "###" + grtConfig.getIbFailedInProcessingErrMsg();
			form.setMessage(message);
			getInstallBaseService().updateSiteRegistrationSubmittedFlag(
					form.getRegistrationId(), GRTConstants.isSubmitted_false);

			long c2 = Calendar.getInstance().getTimeInMillis();
			logger.debug("Exiting Submit Registration in RegistrationController Due to an Error");
			logger.debug("TIMER:" + (c2 - c1) + " milliseconds");
			return "samepage";
		}

	}

	/**
	 * save Site Reg
	 * @param form
	 * @return
	 * @throws Exception
	 */
	protected RegistrationFormBean saveSiteRegWithSumMaterialCode(
			RegistrationFormBean form) throws Exception {
		logger.debug("Entering RegistrationController--------------saveSiteRegWithSumMaterialCode");
		logger.debug("Entering summingQuantityGoupedByMaterialcode"
				+ form.getMaterialEntryList().size());
		List<TechnicalOrderDetail> list = TechnicalOrderDetailUtil
				.summingQuantityGoupedByMaterialcode(form
						.getMaterialEntryList());
		logger.debug("Exiting summingQuantityGoupedByMaterialcode"
				+ form.getMaterialEntryList().size());

		List<TechnicalOrderDetail> materialEntryFilteredList = new ArrayList<TechnicalOrderDetail>();
		List<TechnicalOrderDetail> filteredList = new ArrayList<TechnicalOrderDetail>();
		List<TechnicalOrderDetail> pendingOrdersFilteredList = new ArrayList<TechnicalOrderDetail>();
		for (TechnicalOrderDetail technicalOrderDetail : list) {
			if (technicalOrderDetail.getSelectforRegistration() != null
					&& technicalOrderDetail.getSelectforRegistration()) {
				technicalOrderDetail
				.setOrderType(GRTConstants.TECH_ORDER_TYPE_IB);
				filteredList.add(technicalOrderDetail);
			}
		}

		// Adding pending order to filtered list to insert in Technical Order
		// table
		logger.debug("Before Setting IsSalesOut to yes.......   SSR2 ..the size of the pendingOrders not in form"
				+ actionForm.getPendingOrders().size());
		for (TechnicalOrderDetail technicalOrderDetail : actionForm.getPendingOrders()) {
			if (technicalOrderDetail.getSelectforRegistration() != null
					&& technicalOrderDetail.getSelectforRegistration()) {
				technicalOrderDetail
				.setOrderType(GRTConstants.TECH_ORDER_TYPE_IB);
				logger.debug("Setting IsSalesOut to yes");
				technicalOrderDetail.setIsSalesOut(GRTConstants.YES);
				filteredList.add(technicalOrderDetail);
			}
		}
		logger.debug("Exiting filtered list creator" + filteredList.size());

		if (!form.getSaveSiteReg()) {
			logger.debug("Entering saveSiteRegWithSumMaterialCode save materialentrylist : saveMaterialEntryList");

			filteredList = getInstallBaseService().saveMaterialEntryList(
					filteredList);

			for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
				technicalOrderDetail.setSelectforRegistration(true);
			}

			for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
				if (technicalOrderDetail.getIsSalesOut() != null
						&& technicalOrderDetail.getIsSalesOut()
						.equalsIgnoreCase(GRTConstants.YES)) {
					logger.debug("<===========================ADDING RECORDS TO pendingOrdersFilteredList =====   SSR2.5=====>");
					pendingOrdersFilteredList.add(technicalOrderDetail);
				} else if (technicalOrderDetail.getIsSalesOut() == null) {
					materialEntryFilteredList.add(technicalOrderDetail);
				}
			}

			logger.debug("The size of pendingOrdersFilteredList=====   SSR3=====>"
					+ pendingOrdersFilteredList.size());

			form.setPendingOrders(pendingOrdersFilteredList);
			pendingOrders = pendingOrdersFilteredList;
			form.setMaterialEntryList(materialEntryFilteredList);
			return form; // Don't save the registration again if user come
			// from registration list page, the only thing we
			// need to save is material entry list
		}

		form.setMaterialEntryList(filteredList);

		SiteRegistration siteRegistration = getInstallBaseService()
				.assemblerSiteRegistrationFromFormBean(form);

		siteRegistration.setUpdatedBy(GRTConstants.USER_ID_SYSTEM);
		siteRegistration.setUpdatedDate(new Date());
		siteRegistration.setRegistrationIdentifier(form
				.getRegistrationIdentifier());
		siteRegistration.setNotes(form.getRegistrationNotes());

		RegistrationType regType = new RegistrationType();
		if (form.getInstallbaseRegistrationOnly()) {
			regType.setRegistrationId(RegistrationTypeEnum.INSTALLBASEONLY
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.INSTALLBASEONLY
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		} else if (form.isFullRegistrationOnly()) {
			regType.setRegistrationId(RegistrationTypeEnum.FULLREGISTRATION
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.FULLREGISTRATION
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		} else if (form.isSalMigrationOnly()) {
			regType.setRegistrationId(RegistrationTypeEnum.SALMIGRATION
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.SALMIGRATION
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		} else if (form.isTechnicalRegistrationOnly()) {
			regType.setRegistrationId(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.TECHNICALREGISTRATIONONLY
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		} else if (form.isEquipmentRegistrationOnly()) {
			regType.setRegistrationId(RegistrationTypeEnum.EQUIPMENTREMOVALONLY
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.EQUIPMENTREMOVALONLY
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		} else if (form.isIpoRegistration()) {
			regType.setRegistrationId(RegistrationTypeEnum.IPOFFICE
					.getRegistrationID());
			regType.setRegistrationType(RegistrationTypeEnum.IPOFFICE
					.getRegistrationType());
			siteRegistration.setRegistrationType(regType);
		}
		siteRegistration.setStatus_id(StatusEnum.SAVED.getStatusId());
		if (form.getPurchaseOrder() != null) {
			siteRegistration.setPo(form.getPurchaseOrder());
		}

		siteRegistration = getInstallBaseService().saveSiteRegistration(
				siteRegistration, getUserFromSession().getUserId());

		filteredList = getInstallBaseService().getTechnicalOrderByType(
				siteRegistration.getRegistrationId(),
				GRTConstants.TECH_ORDER_TYPE_IB);

		logger.debug("<===========================size of the  filteredList =====   SSR3.1=====>"
				+ filteredList.size());
		for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
			technicalOrderDetail.setSelectforRegistration(true);
			if (technicalOrderDetail.getTechnicallyRegisterable() != null
					&& technicalOrderDetail.getTechnicallyRegisterable()
					.equalsIgnoreCase(GRTConstants.YES)) {
				technicalOrderDetail.setTechnicallyRegisterable("Y");
			}
		}
		for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
			if (technicalOrderDetail.getIsSalesOut() != null
					&& technicalOrderDetail.getIsSalesOut().equalsIgnoreCase(
							GRTConstants.YES)) {
				pendingOrdersFilteredList.add(technicalOrderDetail);
			} else if (technicalOrderDetail.getIsSalesOut() == null) {
				materialEntryFilteredList.add(technicalOrderDetail);
			}
		}
		form.setRegistrationId(siteRegistration.getRegistrationId());
		form.setMaterialEntryList(materialEntryFilteredList);
		form.setPendingOrders(pendingOrdersFilteredList);
		pendingOrders = pendingOrdersFilteredList;
		logger.debug("Exiting RegistrationController--------------saveSiteRegWithSumMaterialCode");
		return form;
	}

	/**
	 * method to delete technical orders.
	 * 
	 * @param form
	 * @param result
	 * @throws Exception
	 */
	public void deleteTechnicalOrders(RegistrationFormBean form,
			List<TechnicalOrderDetail> result) throws Exception {
		// Deleting the Technical Orders which are excluded in Submit
		logger.debug(" Before deleting the Technical Order records");
		List<String> removeTechnicalOrderList = null;

		List<TechnicalOrderDetail> technicalOrderDetailList = getInstallBaseService()
				.getTechnicalOrderByType(form.getRegistrationId(),
						GRTConstants.TECH_ORDER_TYPE_IB);

		List<String> orderIdDbList = null;
		List<String> orderIdUiList = null;
		if (technicalOrderDetailList != null
				&& technicalOrderDetailList.size() > 0) {
			orderIdDbList = new ArrayList<String>();

			for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
				orderIdDbList.add(technicalOrderDetail.getOrderId());
			}

			if (result.size() > 0) {
				orderIdUiList = new ArrayList<String>();
				for (TechnicalOrderDetail technicalOrderDetail : result) {
					logger.debug("deleteTechnicalOrders:technicalOrderDetail.getOrderId()"
							+ technicalOrderDetail.getOrderId());
					if (StringUtils.isNotEmpty(technicalOrderDetail
							.getOrderId())) {
						logger.debug("Technical Order Id is already exist");
						orderIdUiList.add(technicalOrderDetail.getOrderId());
					}
				}
			}

			if (orderIdDbList != null && orderIdUiList != null) {
				if (orderIdDbList.size() > 0) {
					removeTechnicalOrderList = new ArrayList<String>();
					for (String orderId : orderIdDbList) {
						if (!orderIdUiList.contains(orderId)) {
							logger.debug("Technical Order added to the removing list"
									+ orderId);
							removeTechnicalOrderList.add(orderId);
						}
					}
				}
			}

			if (removeTechnicalOrderList != null
					&& removeTechnicalOrderList.size() > 0) {
				getInstallBaseService().deleteTechnicalOrders(
						removeTechnicalOrderList);
			}
		}
		logger.debug("After deleting the Technical Order records");
	}

	/**
	 * Get parameters from Request
	 * 
	 * @param form
	 * @return
	 */
	protected RegistrationFormBean collectRequestParam(RegistrationFormBean form) {
		if (form.getSoldToId() == null
				&& getRequest().getParameter("soldToId") != null) {
			form.setSoldToId(getRequest().getParameter("soldToId"));
		}

		if (form.getRegistrationId() == null
				&& getRequest().getParameter("registrationId") != null) {
			form.setRegistrationId(getRequest().getParameter("registrationId"));
		}
		return form;
	}

	/**
	 * method to get Existing InstallBase Records
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getExistingInstallBaseRecords() throws Exception {

		if (soldTo != null) {
			try {
				
				technicalOrderDetailList = prepareTechnicalOrderMaterialExclusion(soldTo, true);

				technicalOrderDetailList = constructTechnicalOrderDetailList(technicalOrderDetailList, 0);

				technicalOrderDetailList = getInstallBaseService().filterTechnicalOrderOnPipeline(technicalOrderDetailList, soldTo, "DISP");

				technicalOrderDetailList = getInstallBaseService().constructTechnicalOrderDetailListOnMC(technicalOrderDetailList);
				int size=Integer.parseInt(grtConfig.getInstallBaseListSize());
				actionForm.setListSizeFlag(false);
				if(technicalOrderDetailList.size()>size){
					actionForm.setListSizeFlag(true);
					technicalOrderDetailList = new ArrayList<TechnicalOrderDetail>(technicalOrderDetailList.subList(0, size));
				}
				this.actionForm.setExistingInstallBaseList(technicalOrderDetailList);
				
				
			} catch (Exception e) {
				setErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		return Action.SUCCESS;
	}


	/**
	 * method to process installBase creation
	 * 
	 * @return
	 * @throws Exception
	 */
	public String installBaseCreation(){

		try {
			validateSession();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		this.installBaseEnableSubmit = false;
		this.uploadMessage = "";

		//actionForm properties for UI
		actionForm.setInstallBaseEnableSubmit(false);
		actionForm.setUploadMessage("");

		String company = actionForm.getCompany();
		if (actionForm.getExistingInstallBaseList() != null) {
			actionForm.getExistingInstallBaseList().clear();
		}
		actionForm = collectRequestParam(actionForm);
		if (actionForm.isIpoRegistration()) {
			logger.debug("materialentry size "
					+ actionForm.getMaterialEntryList().size());
		}

		//Setting userRole
		CSSPortalUser user = getUserFromSession();
		if(user.getUserType() != null && user.getUserType().equalsIgnoreCase("C")){
			actionForm.setUserRole("C");
		}

		if (actionForm != null) {
			actionForm.setSelectAndUnselectAllMeterial(actionForm.getSelectAndUnselectAllMeterial());
			if (actionForm.getMaterialEntryList() != null
					&& actionForm.getMaterialEntryList().size() > 0) {
				logger.debug("in form.getmatent not null " );
				TechnicalOrderDetail materialentry = actionForm
						.getMaterialEntryList().get(0);
				if (materialentry != null) {
					materialentry
					.setOrderId(fixCSS(materialentry.getOrderId()));
					materialentry.setDescription(fixCSS(materialentry
							.getDescription()));
					materialentry.setMaterialCode(fixCSS(materialentry
							.getMaterialCode()));
				}
			}
		}



		try {
			pendingOrders = null;
			actionForm = assemblePendingOrder(actionForm);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if ((actionForm.getRegistrationId() != null
					&& !actionForm.isIpoRegistration())||(actionForm.getRegistrationId() != null && saveAssistIPO)) {
				logger.debug("Changing materialentrylist");
				List<TechnicalOrderDetail> technicalOrderDetailList = getInstallBaseService()
						.getTechnicalOrderByType(actionForm.getRegistrationId(),
								GRTConstants.TECH_ORDER_TYPE_IB);
				List<TechnicalOrderDetail> filteredList = new ArrayList<TechnicalOrderDetail>();
				for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
					if(technicalOrderDetail.getIsSalesOut() == null){
						filteredList.add(technicalOrderDetail);
					}

				}
				for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
					technicalOrderDetail.setSelectforRegistration(true);

				}
				filteredList = validateMaterialCodes(filteredList);
				actionForm
				.setMaterialEntryList(TechnicalOrderDetailUtil
						.summingQuantityGoupedByMaterialcode(filteredList));
			}
			if(actionForm.getRegistrationId() != null){
				RegistrationSummary summary = getInstallBaseService().getRegistrationSummary(actionForm.getRegistrationId());
				if("1002".equals(summary.getInstallBaseStatusId())&&((StringUtils.isNotEmpty(summary.getSrCompleted()))&& (summary.getSrCompleted().equalsIgnoreCase("no")))){
					logger.debug("Install Base in site InProcess and also the SR is not completed so showing Technical Order errors");
					actionForm.setIbaseStatus("1003");
				} else {
					logger.debug("Site Registration InstallBase Status Id =====> "+summary.getInstallBaseStatusId() +" <====");
					actionForm.setIbaseStatus(summary.getInstallBaseStatusId());
				}
				logger.debug("Status ID "+summary.getInstallBaseStatusId()+"and Get submitted flag"+summary.getSubmitted());
				
				if ((StringUtils.isNotEmpty(summary.getSubmitted())&& summary.getSubmitted().equals("0"))
						||("1008".equals(summary.getInstallBaseStatusId())) ){
					logger.debug("Setting the IBase creation to readonly because found a Submit already occured for this reg ID");
					this.installBaseCreationReadOnlyFlag = true;

				}
				if(summary.getSubmitted()!= null){
					logger.debug("Install Base creation already exist ");
					actionForm = this.assemblePendingOrderInReadOnly(actionForm);
				}
				//Begin: Set the Banner Details
				actionForm.setFirstColumnOnly(null);
				
				/*For defect 267*/
				if(summary.getInstallBaseStatus() != null){
					actionForm.setBannerStatus(summary.getInstallBaseStatus());
				} else {
					actionForm.setBannerStatus(GRTConstants.STATUS_NOT_INITIATED);
				}/*For defect 267*/				
				CSSPortalUser cssPortalUser = getUserFromSession();
				String userType  = cssPortalUser.getUserType();
				if(!StringUtils.isEmpty(userType)){
					actionForm.setBannerSubStatus(summary.getInstallBaseSubStatus());
					getRequest().getSession().setAttribute(
							GRTConstants.IS_AVAYA_ASSOCIATE, GRTConstants.TRUE);
				}
				actionForm.setBannerSrNumber(summary.getInstallBaseSrNo());
				actionForm.setCompany(summary.getRequestingCompany());
				actionForm.setBannerSrLabel(GRTConstants.TRUE);
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
				if(summary.getIbSubmittedDate() != null){
					actionForm.setBannerSubmittedDate(sdf.format(summary.getIbSubmittedDate()));
				} else {
					actionForm.setBannerSubmittedDate("");
				}
				if(summary.getIbCompletedDate() != null){
					actionForm.setBannerCompletedDate(sdf.format(summary.getIbCompletedDate()));
				} else {
					actionForm.setBannerCompletedDate("");
				}
				
				//Defect #768
				if( summary.getRegistrationIdentifier()!=null ){
					actionForm.setRegistrationIdentifier(summary.getRegistrationIdentifier());
				}
				
				if( summary.getRegistrationNotes()!=null ){
					actionForm.setRegistrationNotes(summary.getRegistrationNotes());		
				}
				
				//End: Set the Banner Details
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (getRequest().getParameter("readOnly") != null) {
			logger.debug("In requestparemeter read only"+getRequest().getParameter("readOnly"));
			this.installBaseCreationReadOnlyFlag = getRequest().getParameter("readOnly").equals("true");
		}

		actionForm.setMessage("");
		actionForm.setReturnCode("");
		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		// Set the registration form in a request scope
		getRequest().setAttribute("registrationForm", actionForm);
		logger.debug("akdakjshdkjashdkjahsd Usertype"+actionForm.getUserRole());
		logger.debug("Exiting installBaseCreation&*&*&*&*&*&*&*"+this.installBaseCreationReadOnlyFlag);

		if((((getRequest().getSession().getAttribute("IBBack"))!= null)
				&& !((((getRequest().getSession().getAttribute("IBBack")).toString()).equalsIgnoreCase("ipreg"))
						||  (((getRequest().getSession().getAttribute("IBBack")).toString()).equalsIgnoreCase("RegList"))))
						||(actionForm.getSessionAttr()!=null && !((((actionForm.getSessionAttr()).toString()).equalsIgnoreCase("ipreg"))
								||  (((actionForm.getSessionAttr()).toString()).equalsIgnoreCase("RegList"))))){

			actionForm.setSessionAttr("scv");
		}

		if((getRequest().getSession().getAttribute("IBBack")!=null)
				&& ((getRequest().getSession().getAttribute("IBBack")).toString()).equalsIgnoreCase("ipreg")){
			getRequest().getSession().removeAttribute("IBBack");

			actionForm.setSessionAttr("ipreg");
		}
		if((getRequest().getSession().getAttribute("IBBack")!=null)
				&&(((getRequest().getSession().getAttribute("IBBack")).toString()).equalsIgnoreCase("RegList"))){
			getRequest().getSession().removeAttribute("IBBack");

			actionForm.setSessionAttr("RegList");
		}


		if(StringUtils.isEmpty(actionForm.getCompany())){
			actionForm.setCompany(company);
		}

		return Action.SUCCESS;
	}

	/**
	 * method to assemble pending order
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 */
	private RegistrationFormBean assemblePendingOrder(RegistrationFormBean form)
			throws Exception {
		if (pendingOrders == null) {
			String bpLinkId = this.getUserFromSession().getBpLinkId();
			logger.debug("The current bpLink Id is :" + bpLinkId);
			pendingOrders = getInstallBaseService().getPendingOrders(form.getSoldToId(), bpLinkId);
			pendingOrders = validateMaterialCodes(pendingOrders);
			int totalNum = 0;
			if (pendingOrders != null) {
				totalNum = pendingOrders.size();
				logger.debug("pendingOrders List size after Validating MaterialCodes:" + pendingOrders.size());
			}
			if (form.getPendingOrderPager() == null) {
				form.setPendingOrderPager(initPager(INSTALL_BASE_CREATION_PENDING_ORDER_FETCH_SIZE));
			}
			form.getPendingOrderPager().setTotalNum(totalNum);
			logger.debug("assemblePendingOrder size: " + totalNum);
		}
		logger.debug("assemblePendingOrder totalNum:" + form.getPendingOrderPager().getTotalNum());
		logger.debug("assemblePendingOrder offSet:" + form.getPendingOrderPager().getOffSet());
		logger.debug("assemblePendingOrder fetchSize:"  + form.getPendingOrderPager().getFetchSize());
		int endIndex = (form.getPendingOrderPager().getOffSet()
				+ form.getPendingOrderPager().getFetchSize() < form
				.getPendingOrderPager().getTotalNum()) ? form
						.getPendingOrderPager().getOffSet()
						+ form.getPendingOrderPager().getFetchSize() : form
						.getPendingOrderPager().getTotalNum();

						form.setPendingOrders(pendingOrders);
						if(!form.getPendingOrders().isEmpty()){
							if(isFirstTimePendingOrder){
								//form.setSelectAndUnselectAllPending(isFirstTimePendingOrder);
								//Above line commented to chaged from true to false to fix the pending issue 8.1
								//Default status of the check root checkbox is un checked.
								form.setSelectAndUnselectAllPending(false);
								isFirstTimePendingOrder = false;
							}else{
								logger.debug("form.getSelectAndUnselectAllMeterial()"+form.getSelectAndUnselectAllPending());
								form.setSelectAndUnselectAllPending(form.getSelectAndUnselectAllPending());
							}
						}
						return form;
	}

	private RegistrationFormBean assemblePendingOrderInReadOnly(RegistrationFormBean form)
			throws Exception {
		if(form.getRegistrationId()!= null){
			List<TechnicalOrderDetail> technicalOrderDetailList = getInstallBaseService()
					.getTechnicalOrderByType(form.getRegistrationId(),
							GRTConstants.TECH_ORDER_TYPE_IB);
			List<TechnicalOrderDetail> filteredList = new ArrayList<TechnicalOrderDetail>();
			if(installBaseCreationReadOnlyFlag){
				form.setSelectAndUnselectAllPending(false);
			}
			if(technicalOrderDetailList != null && technicalOrderDetailList.size()>0){
				for (TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
					if(technicalOrderDetail.getIsSalesOut() != null && technicalOrderDetail.getIsSalesOut().equals(GRTConstants.YES)){
						filteredList.add(technicalOrderDetail);
					}

				}
				for (TechnicalOrderDetail technicalOrderDetail : filteredList) {
					technicalOrderDetail.setSelectforRegistration(true);

				}
				filteredList = validateMaterialCodes(filteredList);
				pendingOrders = filteredList;
			}else{
				pendingOrders = filteredList;
			}
		}
		int totalNum = 0;
		if (pendingOrders != null) {
			totalNum = pendingOrders.size();
		}
		if (form.getPendingOrderPager() == null) {
			form
			.setPendingOrderPager(initPager(INSTALL_BASE_CREATION_PENDING_ORDER_FETCH_SIZE));
		}
		form.getPendingOrderPager().setTotalNum(totalNum);
		logger.debug("assemblePendingOrder size: " + totalNum);

		logger.debug("assemblePendingOrder totalNum: "
				+ form.getPendingOrderPager().getTotalNum());
		logger.debug("assemblePendingOrder offSet: "
				+ form.getPendingOrderPager().getOffSet());
		logger.debug("assemblePendingOrder fetchSize: "
				+ form.getPendingOrderPager().getFetchSize());

		int endIndex = (form.getPendingOrderPager().getOffSet()
				+ form.getPendingOrderPager().getFetchSize() < form
				.getPendingOrderPager().getTotalNum()) ? form
						.getPendingOrderPager().getOffSet()
						+ form.getPendingOrderPager().getFetchSize() : form
						.getPendingOrderPager().getTotalNum();
						form.setPendingOrders(pendingOrders);
						return form;
	}

	/**
	 * ajax call method to validate material code.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validateMaterialCode() throws Exception {
		logger.debug("Entering validateMaterialCode");
		List<String> materialCodes = new ArrayList<String>();
		Object[] mcArray = new Object[6];
		materialCodes.add(materialCode);
		Map<String, String> mDescMap = getInstallBaseService()
				.getMaterialCodeDesc(materialCodes);
		logger.debug(mDescMap.get(materialCode));
		mcArray[0] = mDescMap.get(materialCode);
		// [AVAYA] GRT2.1: Validation of material code against
		// MATERIAL_EXCLUSION table (Start)

		Map<String, Object> map = getInstallBaseService()
				.validateMaterialExclusion(materialCodes);
		if (map != null && map.size() > 0) {
			mcArray[1] = true;
			mcArray[3] =  map.get(materialCode);
			logger.debug("materialExclusionFlag value"
					+ this.materialExclusionFlag);
		} else {
			mcArray[1] = false;
			mcArray[3] = null;
		}
		// Is TR Eligible
		List<TRConfig> trEligibleList = getInstallBaseService().isTREligible(
				materialCode);
		if (trEligibleList != null && trEligibleList.size() > 0) {
			mcArray[2] = "Y";
			logger.debug("TREligible :" + trEligibleList.size() + "   flag:"
					+ mcArray[2]);
		} else {
			mcArray[2] = "";
			logger.debug("Flag :" + mcArray[2]);
		}
		
		//GRT4.0 Check whether the material code is serialized
		Map<String, Object> materialMasterMap = getInstallBaseService().populateMaterialMasterDetails(materialCodes);
		if(materialMasterMap != null) {
			MasterMaterial masterMaterial = (MasterMaterial) materialMasterMap.get(materialCode);			
			if(masterMaterial.getSerialized() != null && serializedContants.contains(masterMaterial.getSerialized())) {
				mcArray[4] = GRTConstants.Y;
			} else {
				mcArray[4] = GRTConstants.N;
			}
			if(masterMaterial.getServiceUsage() != null){
				mcArray[5] = GRTConstants.Y;
			}else{
				mcArray[5] = GRTConstants.N;
			}
		}
		
		logger.debug("Inside validateMaterialCode" + mDescMap.get(materialCode));
		setMaterialCodeArray(mcArray);

		Map<String, Object> mcMap =new HashMap<String, Object>();
		mcMap .put("materialDescription",mcArray[0]);
		mcMap.put("selectForRegistration",mcArray[1]);
		mcMap.put("techOnboardingEligible",mcArray[2]);
		mcMap.put("exclusionSource",mcArray[3]);
		mcMap.put("serialized",mcArray[4]);
		mcMap.put("serviceUsage",mcArray[5]);


		Object exclusionSource = mcArray[3];
		String warning="";
		if(exclusionSource == "PLDS"){	
			warning = getGrtConfig().getExclusionSourcePLDS().trim();
		} else if(exclusionSource == "KMAT") {

			warning =  getGrtConfig().getExclusionSourceKMAT().trim();
		}else if(exclusionSource=="DEFECTIVE")
		{
			warning=getGrtConfig().getDefectiveCodeErrMsg().trim();
		}else if(exclusionSource=="BLUE")
		{
			warning=getGrtConfig().getBlueCodeErrMsg().trim();
		}
		else if(exclusionSource != null && StringUtils.isNotEmpty(exclusionSource.toString()))
		{
			warning = "This material code is not eligible for install base creation.";
		}
		else
		{
			warning="";
		}
		mcMap.put("warningsAndErrors",warning);
		setMaterialCodeMap(mcMap);
		return Action.SUCCESS;
	}

	/**
	 * method to Handle Invalid Token
	 * 
	 * @return
	 */
	public String handleInvalidToken() {
		long c1 = Calendar.getInstance().getTimeInMillis();

		logger.error("validateMaterialCodesAgainstSoldTo: generatedToken invalid");
		String forwardName;
		if (salMigrationOnlyFlag) {
			forwardName = "salMigrationOnly";
		} else {
			forwardName = "default";
		}
		logger.debug("Ended Validations Returning");
		long c2 = Calendar.getInstance().getTimeInMillis();
		logger.debug("TIMER:" + (c2 - c1) + " milliseconds");
		return forwardName;
	}

	/**
	 * method to add new material entry.
	 *  
	 * @return
	 * @throws Exception
	 */
	public String addMaterialEntry() throws Exception {
		logger.debug("Inside : addMaterialEntry");
		logger.debug("In RegistrationController........addMaterialEntry");
		logger.debug("isFirstTime :" + isFirstTime);
		validateSession();

		if (actionForm.isFirstTime()) {
			actionForm.setSelectAndUnselectAllMeterial(actionForm.isFirstTime());
			actionForm.setFirstTime(false);
		} else {
			logger.debug("actionForm.getSelectAndUnselectAllMeterial()"
					+ actionForm.getSelectAndUnselectAllMeterial());
			actionForm.setSelectAndUnselectAllMeterial(actionForm
					.getSelectAndUnselectAllMeterial());
		}
		TechnicalOrderDetail detail = new TechnicalOrderDetail();
		detail.setRegistrationId(actionForm.getRegistrationId());
		detail.setSelectforRegistration(true);
		boolean isAutoTR = false;
		if ((actionForm.getRemoteConnectivity() != null)
				&& ((actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes")) || (actionForm
						.getRemoteConnectivity().equalsIgnoreCase("No")))) {
			if (actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes")) {
				isAutoTR = true;
			}
			detail.setAutoTR(isAutoTR);
			getRequest().getSession().setAttribute("IPOR", "IPOR");
			getRequest().getSession().setAttribute("IBBack", "ipreg");
			//Update the corresponding values of session attributes in action Form
			actionForm.setIPOR("IPOR");
			actionForm.setIBBack("ipreg");
		}
		getRequest().getSession().removeAttribute("rbselect");
		actionForm.setRbselect(null);

		actionForm.getMaterialEntryList().add(detail);
		actionForm.setReturnCode("");
		actionForm.setMessage("");
		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		this.ipoContinueInstallBase = false;
		this.uploadMessage = "";
		// Begin: Implement the validation
		List<TechnicalOrderDetail> result = actionForm.getMaterialEntryList();
		if (result != null & result.size() > 0) {
			result = validateMaterialCodes(result);
			actionForm.setMaterialEntryList(result);
		}

		logger.debug(" addMaterialEntry :: List Size - "
				+ actionForm.getMaterialEntryList().size());
		// End: Implement the validation
		manuallyAddMaterial = true;
		actionForm.setManuallyAddMaterial(true);

		return Action.SUCCESS;
	}

	/**
	 * method to validate Material Codes
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	protected List<TechnicalOrderDetail> validateMaterialCodes(
			List<TechnicalOrderDetail> result) throws Exception {
		List<String> materialCodes = new ArrayList<String>();
		for (TechnicalOrderDetail detail : result) {
			if (detail.getMaterialCode() != null
					&& StringUtils.isNotEmpty(detail.getMaterialCode())) {
				materialCodes.add(detail.getMaterialCode());
			}
		}
		if (!materialCodes.isEmpty()) {
			Map<String, String> mDescMap = getInstallBaseService()
					.getMaterialCodeDesc(materialCodes);

			// [AVAYA] GRT2.1: Validation of material code against
			// MATERIAL_EXCLUSION table (Start)

			Map<String, Object> map = getInstallBaseService()
					.validateMaterialExclusion(materialCodes);
			
			Map<String, Object> materialMasterMap = getInstallBaseService().populateMaterialMasterDetails(materialCodes);
			for (TechnicalOrderDetail detail : result) {
				String desc = mDescMap.get(detail.getMaterialCode());
				logger.debug("DECRIPTION" + desc);

				detail.setDescription(desc);

				// Is TR Eligible
				List<TRConfig> trEligibleList = getInstallBaseService()
						.isTREligible(detail.getMaterialCode());
				if (trEligibleList != null && trEligibleList.size() > 0) {
					detail.setTechnicallyRegisterable("Y");
				} else {
					detail.setTechnicallyRegisterable("");
				}				
				
				//GRT4.0 Check whether the material code is serialized				
				if(materialMasterMap != null) {
					MasterMaterial masterMaterial = (MasterMaterial) materialMasterMap.get(detail.getMaterialCode());			
					if(masterMaterial != null && serializedContants.contains(masterMaterial.getSerialized())) {
						detail.setSerialized(GRTConstants.Y);
						
						if(detail.getInitialQuantity() != null && detail.getInitialQuantity() == 1) {
							//do nothing . Keep serial number as such
						} else {
							//Clear serial number if mc is serialized but quantity not one.
							detail.setSerialNumber("");
						}			
						
					} else {						
						detail.setSerialized(GRTConstants.N);
						//Clear serial number if mc is not serialized.
						detail.setSerialNumber("");
					}
					if(masterMaterial != null && masterMaterial.getServiceUsage() != null) {
						detail.setServiceUsage(GRTConstants.Y);
					} else {
						detail.setServiceUsage(GRTConstants.N);
					}
				}
				
				
				logger.debug(detail.getMaterialExclusion());
				if (map != null && map.size() > 0) {
					if (map.containsKey(detail.getMaterialCode())) {
						detail.setMaterialExclusion("true");
						logger.debug("MaterialExclusion"
								+ detail.getMaterialExclusion());
						detail.setExclusionSource((String) map.get(detail
								.getMaterialCode()));
					} else {
						detail.setMaterialExclusion("false");
						logger.debug("MaterialExclusion"
								+ detail.getMaterialExclusion());
						detail.setExclusionSource(null);
					}
				} else {
					detail.setMaterialExclusion("false");
					logger.debug("Material Excluded is zero"
							+ detail.getMaterialExclusion());
				}
			}
		}
		logger.debug("size after validating code" + result.size());

		return result;
	}

	public String checkForvalidateSerialNumber() {
		if (srNumbers == null) {
			serialNumberFlag = new String[5];
			return Action.SUCCESS;
		}
		serialNumberFlag = new String[materialCodes.length];
		for (int i = 0; i < materialCodes.length; i++) {
			serialNumberFlag[i] = "";
		}
		logger.debug("Exiting  the checkForvalidateSerialNumber serialNumber"
				+ serialNumberFlag.length);

		return Action.SUCCESS;
	}

	public String getEligibleGroups() {
		logger.debug("In Registration Controller getEligibleGroups");
		retList = new ArrayList<TRConfig>();
		if (StringUtils.isEmpty(seCodes))
			return Action.SUCCESS;

		try {
			List<String> seList = Arrays.asList(StringUtils.split(seCodes
					.toUpperCase().trim(), ","));
			CollectionUtils.filter(seList, new Predicate() {
				public boolean evaluate(Object in) {
					return !StringUtils.isEmpty((String) in);
				}
			});

			List<String> inList = new ArrayList<String>();
			for (String s : seList)
				inList.add(s.trim());
			retList = getInstallBaseService().getEligibleGroups(inList);
			
			//Remove PLDS\KMAT based codes from list Defect#745
			List<TRConfig> retListAfterExclusion = new ArrayList<TRConfig>();			
			List<String> materialCodes = new ArrayList<String>();
			
			for (TRConfig trConfig : retList) {
				materialCodes.add(trConfig.getParentMaterialCode());				
			}
			
			Map<String, Object> map = getInstallBaseService().validateMaterialExclusion(materialCodes);
			for (TRConfig trConfig : retList) {
				if(!map.containsKey(trConfig.getParentMaterialCode())) {
					retListAfterExclusion.add(trConfig);					
				}
			}
			retList = retListAfterExclusion;			
			//Remove PLDS\KMAT based codes from list Defect#745 ends
			
			if (retList != null && !retList.isEmpty())
				Collections.sort(retList, new Comparator<TRConfig>() {
					public int compare(TRConfig f, TRConfig s) {
						return f.getGroupId().compareTo(s.getGroupId());
					}
				});

		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	/**
	 * Method to Save the registration.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveRegistration() throws Exception {
		logger.debug("Entering RegistrationController : saveRegistration");
		validateSession();

		// Begin to implement the Expand and Collapse for Pagination
		getRequest().getSession().removeAttribute(
				GRTConstants.EIB_PAGINATION_BUTTON_CLICKED);
		getRequest().getSession().removeAttribute(
				GRTConstants.SOIR_PAGINATION_BUTTON_CLICKED);
		// End to implement the Expand and Collapse for Pagination
		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		if (actionForm != null) {
			if (actionForm.getMaterialEntryList() != null
					&& !actionForm.getMaterialEntryList().isEmpty()) {
				TechnicalOrderDetail materialentry = actionForm
						.getMaterialEntryList().get(0);
				if (materialentry != null) {
					materialentry
					.setOrderId(fixCSS(materialentry.getOrderId()));
					materialentry.setDescription(fixCSS(materialentry
							.getDescription()));
					materialentry.setMaterialCode(fixCSS(materialentry
							.getMaterialCode()));
				}
			}

		}
		logger.debug("saveRegistration: generatedToken valid");
		actionForm.setSubmitted(GRTConstants.isSubmitted_false);
		actionForm.setIsSrCompleted(GRTConstants.YES);
		actionForm = saveSiteRegWithSumMaterialCode(actionForm);
		String forwardName;
		if (salMigrationOnlyFlag) {
			logger.debug("RegistrationController : saveRegistration*****************************************SAL MIGRATION");
			forwardName = "salMigrationOnly";
			actionForm.setNavigateTo("salMigrationOnly");
		} else {
			this.registrationList();
			forwardName = "default";
			actionForm.setNavigateTo("registrationList");
		}
		logger.debug("Exiting RegistrationController : saveRegistration");
		return forwardName;
	}

	/**
	 * Method to check for BP User.
	 * 
	 * @param user
	 * @return
	 */
	private boolean isBP(CSSPortalUser user) {
		boolean isBP = false;
		if (user != null
				&& StringUtils.isNotEmpty(user.getUserType())
				&& user.getUserType().equalsIgnoreCase(
						GRTConstants.USER_TYPE_BP)) {
			isBP = true;
		}
		return isBP;
	}

	/**
	 * method execcuted on Cancel Button Click action on IB creation page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cancelInstallStatus() throws Exception {
		validateSession();
		getInstallBaseService().cancelSiteRegistrationIBStatus(
				actionForm.getRegistrationId());
		return "success";
	}

	/**
	 * Action to upload the file
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadFile() throws Exception {
		validateSession();
		actionForm.setUploadMessage(null);

		String registrationId;

		if (actionForm.getRegistrationId() != null) {
			registrationId = actionForm.getRegistrationId();
		} else if (getRequest().getParameter("registrationId") != null) {
			registrationId = getRequest().getParameter("registrationId");
		} else {
			registrationId = "1001";
		}

		this.materialCodeList = new ArrayList<MaterialCodeListDto>();
		this.uploadMessage = "";
		if (!actionForm.getMaterialEntryList().isEmpty()) {
			logger.debug("RegistrationFormBean.getMaterialEntryList().size(): "
					+ actionForm.getMaterialEntryList().size());
		}

		try {

			InputStream uploadFile = new FileInputStream(actionForm.getTheFile().toString());

			String theFile = actionForm.getTheFileFileName();
			logger.debug("RegistrationController upload File information:: theFile="
					+ theFile);
			List<TechnicalOrderDetail> result = null;

			String fileExtn = getFileExtension(theFile);
			logger.debug("RegistrationController upload File information::theFile:"
					+ theFile + ":fileExtn:" + fileExtn);
			if (fileExtn != null && fileExtn.equalsIgnoreCase("xlsx")) {
				result = getInstallBaseService().parseWorksheetXLSX(
						uploadFile);
			} else if (fileExtn != null && fileExtn.equalsIgnoreCase("xls")) {
				result = getInstallBaseService().parseWorksheet(uploadFile);
			}
			logger.debug("RegistrationController upload Filessss information::theFile="
					+ theFile);
			logger.debug("registrationDelegate.parseWorksheet(filePath).size(): "
					+ result.size());
			uploadFile.close();
			if (result.size() >= 1000) {
				logger.debug("Uploaded more than 1000 records ");
				this.uploadMessage = "display";
				actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("hugeUploadQuantity") + "###" + grtConfig.getHugeUploadQuantity());
			} else {
				result = validateMaterialCodes(result);

				for (TechnicalOrderDetail detail : result) {
					detail.setRegistrationId(registrationId);
					detail.setSelectforRegistration(true);
				}
				if (!result.isEmpty()) {
					logger.debug("registrationDelegate.parseWorksheet(filePath).size(): "
							+ result.size());
				}

				actionForm.getMaterialEntryList().addAll(result);
				actionForm.setMaterialEntryList(TechnicalOrderDetailUtil
						.summingQuantityGoupedByMaterialcode(actionForm
								.getMaterialEntryList()));
				List<TechnicalOrderDetail> materialEntryList = actionForm
						.getMaterialEntryList();
				GenericSort gs = new GenericSort("materialCode", true);
				Collections.sort(materialEntryList, gs);
				actionForm.setMaterialEntryList(materialEntryList);
			}
		} catch (Exception e) {
			logger.debug("Error happened while calling RegistrationController.uploadFile "
					+ e.getMessage());
			//e.printStackTrace();
			this.uploadMessage = "display";
			actionForm.setUploadMessage(grtConfig.getEwiMessageCodeMap().get("incorrectFormateForFile") + "###" + grtConfig.getIncorrectFormateForFile());
		}

		if (!actionForm.getMaterialEntryList().isEmpty()) {
			actionForm.setSelectAndUnselectAllMeterial(true);
			logger.debug("RegistrationFormBean.getMaterialEntryList().size(): "
					+ actionForm.getMaterialEntryList().size());
		}
		return "success";
	}

	/**
	 * Method to extract file extension
	 * 
	 * @param fName
	 * @return
	 */
	private String getFileExtension(String fName){
		int mid= fName.lastIndexOf(".");
		String ext=fName.substring(mid+1,fName.length());
		return ext;
	}

	public String getRegistrationList() {

		return Action.SUCCESS;
	}

	public RegistrationFormBean getActionForm() {
		return actionForm;
	}

	public void setActionForm(RegistrationFormBean actionForm) {
		this.actionForm = actionForm;
	}

	public Boolean getInstallBaseCreationReadOnlyFlag() {
		return installBaseCreationReadOnlyFlag;
	}

	public void setInstallBaseCreationReadOnlyFlag(
			Boolean installBaseCreationReadOnlyFlag) {
		this.installBaseCreationReadOnlyFlag = installBaseCreationReadOnlyFlag;
	}

	public InstallBaseService getInstallBaseService() {
		return installBaseService;
	}

	public void setInstallBaseService(InstallBaseService installBaseService) {
		this.installBaseService = installBaseService;
	}

	public SiebelClient getSiebelClient() {
		return siebelClient;
	}

	public void setSiebelClient(SiebelClient siebelClient) {
		this.siebelClient = siebelClient;
	}

	public String getSoldToError() {
		return soldToError;
	}

	public void setSoldToError(String soldToError) {
		this.soldToError = soldToError;
	}

	public Boolean getSalMigrationOnlyFlag() {
		return salMigrationOnlyFlag;
	}

	public void setSalMigrationOnlyFlag(Boolean salMigrationOnlyFlag) {
		this.salMigrationOnlyFlag = salMigrationOnlyFlag;
	}

	public List<MaterialCodeListDto> getMaterialCodeList() {
		return materialCodeList;
	}

	public void setMaterialCodeList(List<MaterialCodeListDto> materialCodeList) {
		this.materialCodeList = materialCodeList;
	}

	public Boolean getIpoContinueInstallBase() {
		return ipoContinueInstallBase;
	}

	public void setIpoContinueInstallBase(Boolean ipoContinueInstallBase) {
		this.ipoContinueInstallBase = ipoContinueInstallBase;
	}

	public String getUploadMessage() {
		return uploadMessage;
	}

	public void setUploadMessage(String uploadMessage) {
		this.uploadMessage = uploadMessage;
	}

	public List<TechnicalOrderDetail> getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(List<TechnicalOrderDetail> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public List<TechnicalOrderDetail> getTechnicalOrderDetailList() {
		return technicalOrderDetailList;
	}

	public void setTechnicalOrderDetailList(
			List<TechnicalOrderDetail> technicalOrderDetailList) {
		this.technicalOrderDetailList = technicalOrderDetailList;
	}

	public int getMaterialEntryListSize() {
		return actionForm.getMaterialEntryList().size();
	}

	public int getPendingOrdersSize() {
		return actionForm.getPendingOrders().size();
	}

	public String getMaterialExclusionFlag() {
		return fixCSS(materialExclusionFlag);
	}

	public void setMaterialExclusionFlag(String materialExclusionFlag) {
		this.materialExclusionFlag = materialExclusionFlag;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public boolean isManuallyAddMaterial() {
		return manuallyAddMaterial;
	}

	public void setManuallyAddMaterial(boolean manuallyAddMaterial) {
		this.manuallyAddMaterial = manuallyAddMaterial;
	}

	public String[] getMaterialCodes() {
		return materialCodes;
	}

	public void setMaterialCodes(String[] materialCodes) {
		this.materialCodes = materialCodes;
	}

	public String[] getSrNumbers() {
		return srNumbers;
	}

	public void setSrNumbers(String[] srNumbers) {
		this.srNumbers = srNumbers;
	}

	public String[] getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String[] orderIds) {
		this.orderIds = orderIds;
	}

	public String[] getSerialNumberFlag() {
		return serialNumberFlag;
	}

	public void setSerialNumberFlag(String[] serialNumberFlag) {
		this.serialNumberFlag = serialNumberFlag;
	}

	public List<TRConfig> getRetList() {
		return retList;
	}

	public void setRetList(List<TRConfig> retList) {
		this.retList = retList;
	}

	public String getSeCodes() {
		return seCodes;
	}

	public void setSeCodes(String seCodes) {
		this.seCodes = seCodes;
	}

	public boolean isInstallBaseEnableSubmit() {
		return installBaseEnableSubmit;
	}

	public void setInstallBaseEnableSubmit(boolean installBaseEnableSubmit) {
		this.installBaseEnableSubmit = installBaseEnableSubmit;
	}

	public static int getInstallBaseCreationPendingOrderFetchSize() {
		return INSTALL_BASE_CREATION_PENDING_ORDER_FETCH_SIZE;
	}

	public boolean isSaveAssistIPO() {
		return saveAssistIPO;
	}

	public void setSaveAssistIPO(boolean saveAssistIPO) {
		this.saveAssistIPO = saveAssistIPO;
	}

	public Object[] getMaterialCodeArray() {
		return materialCodeArray;
	}

	public void setMaterialCodeArray(Object[] materialCodeArray) {
		this.materialCodeArray = materialCodeArray;
	}

	//Implementation Present inside TechnicalOnBoardinAction class
	@Override
	public String technicalRegistrationDashboard() {
		return null;
	}

	public Map<String, Object> getMaterialCodeMap() {
		return materialCodeMap;
	}

	public void setMaterialCodeMap(Map<String, Object> materialCodeMap) {
		this.materialCodeMap = materialCodeMap;
	}

	@Override
	public String salGatewayMigrationList() {
		return null;
	}
}
