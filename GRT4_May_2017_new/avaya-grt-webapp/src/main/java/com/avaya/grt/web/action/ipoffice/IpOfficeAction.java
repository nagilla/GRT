package com.avaya.grt.web.action.ipoffice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.avaya.grt.dao.BaseHibernateDao;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.service.iposs.Response;
import com.avaya.grt.web.action.installbase.InstallBaseCreationAction;
import com.avaya.grt.web.util.IPOSchemaUtil;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.RegistrationQuestionsDetail;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;
import com.opensymphony.xwork2.Action;


public class IpOfficeAction<fullRegistrationOnly> extends InstallBaseCreationAction {
	
	private static final Logger logger = Logger
			.getLogger(IpOfficeAction.class);
	private Boolean ipoContinueInstallBase = false;
	private Boolean saveAssistIPO = false;
	private IPOSchemaUtil ipoSchemaUtil;
	private List<TechnicalOrderDetail> pendingOrders;

	private File prcsFile;
	private String theFileContentType;
	private String theFileFileName;
	
	private String hostName;
	/**
	 * This method redirects to the enter sold-to page depends upon the type of
	 * user
	 * 
	 * @return
	 */
	public String ipoRegistration() throws Exception{
		try {
			validateSession();
		} catch (Exception e1) {
			e1.printStackTrace();	
		}		
		actionForm.setTechnicalRegistrationOnly(false); //TOB		
		actionForm.setRecordValidationOnly(false); //record validation
		actionForm.setViewInstallbaseOnly(false); //view installbase
		actionForm.setEquipmentMoveOnly(false); //eqp move
		actionForm.setInstallbaseRegistrationOnly(false); //IB
		actionForm.setFullRegistrationOnly(false);
		actionForm.setSalMigrationOnly(false);
		actionForm.setFirstColumnOnly(true);		
		
		actionForm.setIpoRegistration(true);
		actionForm.setFirstColumnOnly(true);
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}
	
	
    /*
	 * Action to direct to IPO Install
	 *
	 *
	 */
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "success", action = "installBaseCreation"),
			@Jpf.Forward(name = "stay", navigateTo = Jpf.NavigateTo.currentPage) })*/
	public String ipoInstall() throws Exception{
		validateSession();
		// form.getIpoFile().getInputStream().
		//FormFile prcsFile = null;
		File prcsFile = null;
		ipoContinueInstallBase = false;
		saveAssistIPO = false;
		Source schemaFile = null;
		String version = "";
		logger.debug("Type of Instl" + actionForm.getTypeOfInstallation());
		logger.debug("Type of indust" + actionForm.getIndustry());
		logger.debug("Type of emplythis" + actionForm.getEmployeesThisLocation());
		logger.debug("Type of eplyall" + actionForm.getEmployeesAllLocation());
		logger.debug("Type of totlloc" + actionForm.getTotalLocations());
		logger.debug("Type of remotecon" + actionForm.getRemoteConnectivity());
		logger.debug("IPO Acess Type" + actionForm.getIpoAccessType());
		logger.debug("Type of setscvrd" + actionForm.getSetsCovered());
		List<RegistrationQuestionsDetail> ListtoSet = assembleRegQstnsListFormBean(actionForm);
		System.out.println("Past the assembler for answers");
		actionForm.setRegQstnsDtl(ListtoSet);
		prcsFile = actionForm.getIpoFile();
		if(prcsFile!=null){
			if(prcsFile.length()>0){
				/*logger.debug("File size" + prcsFile.getFileSize());*/
				actionForm.setIpoInventoryNotLoaded(GRTConstants.NO);
				logger.debug("contenttype NOT found" + actionForm.getIpoFileContentType());
				String fileName = actionForm.getIpoFileFileName();
				String fileExtn  = getFileExtension(fileName);
				if (fileExtn != null && !fileExtn.equalsIgnoreCase("xml")){
					this.getRequest().setAttribute("schemaError","Invalid Format,xml format Expected");
					return Action.ERROR;
				}

				/*String allowedSize = grtConfig.getProperty(GRTConstants.IPO_FILE_SIZE).trim();
		 		String allowedSize = GRTPropertiesUtil.getProperty("ipo_file_size");
		 		String salGatewayMaterialCode = grtConfig.getSalGatewayMaterialCode();*/
				String allowedSize = grtConfig.getIpoFileSize();
				logger.debug("Fetched new size from grtproperties"+allowedSize);
				/*if (prcsFile.getFileSize() > Integer.parseInt(allowedSize))  {*/
				/*if (1000000 > Integer.parseInt(allowedSize))  {*/
				if (prcsFile.length() > Integer.parseInt(allowedSize)){
					this.getRequest().setAttribute("schemaError",
							"Files greater than 10 MB cannot be processed");
					return Action.ERROR;
				}
				/*logger.debug("File size" + prcsFile.getFileSize());*/

				logger.debug("contenttype NOT found" + actionForm.getIpoFileContentType());
				final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
				DocumentBuilder parser;
		try {
			logger.debug("Trying to parse file");
			logger.debug("directory" + System.getProperty("user.dir"));

			logger.debug("dir1"
					+ getClass().getProtectionDomain().getCodeSource()
							.getLocation().getPath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false); // we will use schema instead of DTD
			dbf.setNamespaceAware(true);
			parser = dbf.newDocumentBuilder();
			//InputStream is = prcsFile.getInputStream();
			InputStream is = new FileInputStream(prcsFile.toString());
			logger.debug("Got input stream");
			InputStream is1 = null;
			InputStream is2 = null;
			InputStream is3 = null;
			InputStream is4 = null;

			Document document = parser.parse(is);
			version = getIpoSchemaUtil().getVersionFromInventoryXML(document);
			logger.debug("Parsed Stream..... Inventory File version:"+version);
			SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			if(version.equals("10.0")){
				is4 = getClass().getClassLoader().getResourceAsStream("sslvpn_inventory_10_0_0.xsd");
				schemaFile = new StreamSource(is4);
			} else if(version.equals("9.1")){
				is3 = getClass().getClassLoader().getResourceAsStream("sslvpn_inventory_9_1_0.xsd");
				schemaFile = new StreamSource(is3);
			} else if(version.equals("9.0")){
				is2 = getClass().getClassLoader().getResourceAsStream("sslvpn_inventory_9_0_0.xsd");
				schemaFile = new StreamSource(is2);
			} else {
				is1 = getClass().getClassLoader().getResourceAsStream("sslvpn_inventory_8_1_0.xsd");
				schemaFile = new StreamSource(is1);
			}
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			logger.debug("Before validation");
			validator.validate(new DOMSource(document));
			logger.debug("After validation");
			boolean isAutoTR = false;
			if(StringUtils.isNotEmpty(actionForm.getRemoteConnectivity()) && actionForm.getRemoteConnectivity().equalsIgnoreCase(GRTConstants.YES)) {
				isAutoTR = true;
				if (actionForm.getIpoAccessType() != null && GRTConstants.IPO_ACCESS_TYPE_OTHER.equalsIgnoreCase(actionForm.getIpoAccessType())) {
					actionForm.setRemoteConnectivity(GRTConstants.NO);
					isAutoTR = false;
				}
				logger.debug("IPO Access Type:"+actionForm.getIpoAccessType()+"    isAutoTR:"+isAutoTR);
			}
			// InputStream is1 = new
			// FileInputStream(ClassPath.getResource(\sslvpn_inventory.xsd));

			// if(getRegistrationDelegate().validateInventory(is)){
			logger.debug("before JAXB");
			JAXBContext jc = null;
			Unmarshaller unmarshaller = null;
			if(version.equals("10.0")){
				jc = JAXBContext.newInstance("com.avaya.grt.service.iposs.ipoInventory10");
				logger.debug("before JAXB validation version 10.0");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.ipoInventory10.Response lib = (com.avaya.grt.service.iposs.ipoInventory10.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			} else if(version.equals("9.1")){
				jc = JAXBContext.newInstance("com.avaya.grt.service.iposs.ipoInventory91");
				logger.debug("before JAXB validation version 9.1");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.ipoInventory91.Response lib = (com.avaya.grt.service.iposs.ipoInventory91.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			} else if(version.equals("9.0")){
				jc = JAXBContext.newInstance("com.avaya.grt.service.iposs.ipoInventory9");
				logger.debug("before JAXB validation version 9.0");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.ipoInventory9.Response lib = (com.avaya.grt.service.iposs.ipoInventory9.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			} else {
				jc = JAXBContext.newInstance("com.avaya.grt.service.iposs");
				logger.debug("before JAXB validation");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				Response lib = (Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			}
			logger.debug("Finished parse file got lib");



			logger.debug("Finished populatiing XML details"
					+ actionForm.getMaterialEntryList().size());
			try {
				actionForm.setMaterialEntryList(validateMaterialCodes(actionForm.getMaterialEntryList()));
				if(!actionForm.getMaterialEntryList().isEmpty()){
					actionForm.setSelectAndUnselectAllMeterial(true);
				}
				logger.debug("Finished getting desc"
						+ actionForm.getMaterialEntryList().size());
			} catch (Exception e) {				
				e.printStackTrace();
			}
			logger.debug("size" + actionForm.getMaterialEntryList().size());
			// }
		} catch (ParserConfigurationException e) {
			logger.debug("ParserConfigurationException");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.debug("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("IOException");
			e.printStackTrace();
		} catch (SAXException e) {
			logger.error("SAX Exception validating schema",e);
			this.getRequest().setAttribute("schemaError",
					"Invalid Format,Please Upload the Correct Inventory File");
			return Action.ERROR;
		} catch (JAXBException e) {
			logger.debug("JAXB Exception Exception validating schema");
			e.printStackTrace();
			
			 /* } catch (DataAccessException e) {  Auto-generated catch
			  block e.printStackTrace(); } catch (CreateException e) { 
			  Auto-generated catch block e.printStackTrace(); }
			 
			  */
		}
		}} else if(actionForm.getIpoInventoryNotLoaded() == null || !GRTConstants.NO.equalsIgnoreCase(actionForm.getIpoInventoryNotLoaded())){
			actionForm.setIpoInventoryNotLoaded(GRTConstants.YES);
		}
		// file.
		getRequest().getSession().setAttribute("IPOR","IPOR");

		getRequest().getSession().setAttribute("IBBack","ipreg");
		actionForm.setTypeOfImp("5");
		actionForm.setSaveSiteReg(true);
		return installBaseCreation();
		
		}
	
	private String getFileExtension(String fName){
        int mid= fName.lastIndexOf(".");
        String ext=fName.substring(mid+1,fName.length());
        return ext;
    }
	
	/**
	 * Assembler the SiteRegistrationDTO from the RegistrationFormBean.
	 *
	 * @param form RegistrationFormBean
	 * @return
	 * @return form RegistrationFormBean
	 * @throws FileNotFoundException
	 */
	public List<RegistrationQuestionsDetail> assembleRegQstnsListFormBean(RegistrationFormBean form){

		RegistrationQuestionsDetail regDtl = null;
		List<RegistrationQuestionsDetail>regQstnsDtl = new ArrayList<RegistrationQuestionsDetail>();

		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getTypeOfInstallation());
		regDtl.setQuestionKey("typeofinstll");
		regQstnsDtl.add( regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getIndustry());
		regDtl.setQuestionKey("industry");
		regQstnsDtl.add(regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getEmployeesAllLocation());
		regDtl.setQuestionKey("numofcompemployeesallloc");
		regQstnsDtl.add( regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getEmployeesThisLocation());
		regDtl.setQuestionKey("numofcompemployeesthisloc");
		regQstnsDtl.add( regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getTotalLocations());
		regDtl.setQuestionKey("numoflocations");
		regQstnsDtl.add( regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getRemoteConnectivity());
		regDtl.setQuestionKey("remoteconnctivity");
		regQstnsDtl.add( regDtl);
		regDtl =new RegistrationQuestionsDetail();
		regDtl.setAnswerKey(form.getSetsCovered());
		regDtl.setQuestionKey("setscovered");
		regQstnsDtl.add( regDtl);
		System.out.println("details"+regQstnsDtl.size());
		//form.setRegQstnsDtl(regQstnsDtl);
		return regQstnsDtl;


	}
	
	
	/**
	 * Action to continue IPO
	 *
	 * Flow
	 */
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "default", action = "registrationList"),
			@Jpf.Forward(name = "samepage", navigateTo = Jpf.NavigateTo.currentPage) })*/
	public String ipoOnlyIbase() throws Exception {
		validateSession();
		String message = null;
		try {
			logger.debug("action to complete IPO flow");

			this.ipoContinueInstallBase = false;
			if(actionForm.getMaterialEntryList().size()>0 || pendingOrders.size()>0 ||salMigrationOnlyFlag || actionForm.getSkipInstallBaseCreation()){
				actionForm.setSubmitted(GRTConstants.isSubmitted_true);
				actionForm.setIsSrCompleted(GRTConstants.YES);
				actionForm = saveSiteRegWithSumMaterialCode(actionForm);
		}
			collectRequestParam(actionForm);
			List<TechnicalOrderDetail> result = new ArrayList<TechnicalOrderDetail>();
			for (TechnicalOrderDetail detail : actionForm.getMaterialEntryList()) {
				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					result.add(detail);
				}
			}
			for (TechnicalOrderDetail detail : pendingOrders) {
				if (detail.getSelectforRegistration() != null
						&& detail.getSelectforRegistration()) {
					result.add(detail);
				}
			}
			if(pendingOrders.size()>0){
				List<String> materialCodes = new ArrayList<String>();
				for (TechnicalOrderDetail detail : pendingOrders) {
					if (detail.getSelectforRegistration() != null
							&& detail.getSelectforRegistration()) {
					materialCodes.add(detail.getMaterialCode());
					}
				}
				getInstallBaseService().updateDeletedSalesOut(materialCodes, actionForm.getSoldToId());
			}
			if (!actionForm.getMaterialEntryList().isEmpty()) {
				logger
						.debug("RegistrationFormBean.getMaterialEntryList().size(): "
								+ actionForm.getMaterialEntryList().size());
			}

			String generateAttachmentInPath = autoGenAttachmentPath(ATTACHMENT_NAME);
			String returnCode = null;

			logger.debug("List Result----"+result);
			logger.debug("Before checking form.getUserRole()"+actionForm.getUserRole());
			if(result.size()>0 || salMigrationOnlyFlag ||  actionForm.getSkipInstallBaseCreation()){
				if(actionForm.getUserRole() != null && actionForm.getUserRole().equals("C")){
					logger.debug("inside checking form.getUserRole()"+actionForm.getUserRole());
					returnCode="D";
					Status	status = new Status();
		    		status.setStatusId(StatusEnum.COMPLETED.getStatusId());

		    		logger.debug("INSIDE direct regID...."+actionForm.getRegistrationId());
		    		String statusafter=getInstallBaseService().updateSiteRegistrationStatusForDIrectUser(actionForm.getRegistrationId());
		    		logger.debug("After completing the status in DB....statusafter"+statusafter);
		    		//form.setStatusId(statusafter);
				}
				else{
					this.deleteTechnicalOrders(actionForm, result);
					returnCode= getInstallBaseService().submitRegistration(
							actionForm.getRegistrationId(), actionForm.getSoldToId(), result,
							generateAttachmentInPath, salMigrationOnlyFlag,
							actionForm.getSkipInstallBaseCreation());
					/*,this.getUserFromSession().getUserId()*/
				}
				if(returnCode.equals("D")){
					logger.debug("Return code when he is a direct user"+returnCode);
					message="Install base will be auto completed for direct customer(s).";
				}
			if(returnCode.equals(GRTConstants.installBase_successCode)){
				message = "Your Install Base request is submitted, you will receive notification email(s).";
			}else if(returnCode.equals(GRTConstants.installBase_errorCode)){
				getInstallBaseService().updateSiteRegistrationSubmittedFlag(actionForm.getRegistrationId(), GRTConstants.isSubmitted_false);
				message = "Error while processing the request";
			}else if(returnCode.equals(GRTConstants.sapDown_errorCode)){
				message = "GRT is facing some technical issues in processing your request. Please retry in sometime";
			}else if(returnCode.equals(GRTConstants.fmw_errorCode)){
				message = "Middleware is down.Mail is sent to System Admin.";
			}else if(returnCode.equals(GRTConstants.exception_errorcode)){
				message = "Failed to process the request.";
			}else if(returnCode.equals(GRTConstants.sapdestination_notfound)){
				message = "Unable to map backend system for SoldTo: " +actionForm.getSoldToId()+" , Please contact system administration.";
			}

			}else{
				message = "Material list is empty";
				returnCode = GRTConstants.materialEmptyListRC;
				StatusEnum status = StatusEnum.INPROCESS;
	    		baseRegistrationService.updateSiteRegistrationProcessStepAndStatus(
	    				actionForm.getRegistrationId(), ProcessStepEnum.INSTALL_BASE_CREATION,
	    				status);
	    		installBaseService.updateSiteRegistrationSubmittedFlag(actionForm.getRegistrationId(), GRTConstants.isSubmitted_false);

			}
			actionForm.setMessage(message);
			actionForm.setReturnCode(returnCode);
			logger.debug("----Message shown to user----"+message);
			logger.debug("----Return Code----"+returnCode);
			//logger.debug("type of IMP" + form.getTypeOfImp());
			logger.debug("type of IMP" + actionForm.getRemoteConnectivity());

			String forwardName = "samepage";
			//saveToken(getRequest());
			//return new Forward(forwardName, form);
			return Action.ERROR;
		} catch (Exception e) {
			logger.debug("Error happened while calling ipoonlyIbase"
					+ e.getMessage());
			message = "Failed while processing the request";
			actionForm.setMessage(message);
			getInstallBaseService().updateSiteRegistrationSubmittedFlag(actionForm.getRegistrationId(), GRTConstants.isSubmitted_false);
			//saveToken(getRequest());
			return Action.ERROR;
		}

	}
	
	/*@Jpf.Action(useFormBean = "registrationFormBean", forwards = {
			@Jpf.Forward(name = "samepage", navigateTo = Jpf.NavigateTo.currentPage) })*/
	 public String getMaterialVersion() throws Exception
	 {
		logger.debug("Entering RegistrationController-----------------getMaterialVersion");
		validateSession();
		if(actionForm.getCount()!= null){
			int index = Integer.parseInt(actionForm.getCount());
			TechnicalOrderDetail technicalOrderDetail = actionForm.getMaterialEntryList().get(index);

			for(int i = 0; i < actionForm.getMaterialEntryList().size(); i++){
				if(i == index){
					actionForm.getMaterialEntryList().get(i).setCoreUnitSelected("TRUE");
				} else {
					actionForm.getMaterialEntryList().get(i).setCoreUnitSelected("FALSE");
				}
			}

	    	List<String> materialEntryVersionList = new ArrayList<String>();
	    	List<String> validVersionsList = getInstallBaseService().getValidVersionsForIPOCoreUnit(technicalOrderDetail.getMaterialCode());
	    	if(validVersionsList.size()>0){
	    		materialEntryVersionList.addAll(validVersionsList);
	    		actionForm.setIsCoreUnit("true");
	    	}
	    	else{
	    		actionForm.setIsCoreUnit("false");
	    		}

	    	technicalOrderDetail.setMaterialEntryVersion(materialEntryVersionList);
	    	technicalOrderDetail.setChecked(true);
	    	if(actionForm != null && (actionForm.getRemoteConnectivity()!=null )
					&& (actionForm.getRemoteConnectivity()!=null )
					&& ((actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes"))
						|| (actionForm.getRemoteConnectivity().equalsIgnoreCase("No")))){

	    		if(((actionForm.getRemoteConnectivity().equalsIgnoreCase("Yes"))))
		    	{
		    		technicalOrderDetail.setIsBaseUnit("Y");
		    		technicalOrderDetail.setAutoTR(true);
		    		for(int i=0; i<actionForm.getMaterialEntryList().size(); i++){
						if(index != i){
							actionForm.getMaterialEntryList().get(i).setIsBaseUnit("N");
						}
					}
		    	}
		    	else{
		    			technicalOrderDetail.setAutoTR(false);
		    			technicalOrderDetail.setIsBaseUnit("Y");
			    		for(int i=0; i<actionForm.getMaterialEntryList().size(); i++){
							if(index != i){
								actionForm.getMaterialEntryList().get(i).setIsBaseUnit("N");
							}
						}

		    		}
				getRequest().getSession().setAttribute("IPOR","IPOR");
				getRequest().getSession().setAttribute("IBBack","ipreg");
				actionForm.setSessionAttr("ipreg");
			}

	    	String indexVal = actionForm.getCount();
	    	getRequest().getSession().setAttribute("rbselect",indexVal);

		}
		/*Forward fwd = new Forward("samepage", actionForm);
		fwd.addActionOutput("matIndex", actionForm.getCount());*/
		getRequest().getSession().setAttribute("IPOR","IPOR");
		logger.debug("Exiting RegistrationController-----------------getMaterialVersion");
		return "samepage";
	 }
	
	
	
	public String cancelIPOffice () throws Exception {
		validateSession();
		//getSession().invalidate();
		getRequest().getSession().removeAttribute(GRTConstants.PRIMARY_SE_ID);
		getRequest().getSession().removeAttribute(GRTConstants.SECONDARY_SE_ID);
		
		//added to fix https redirect issue in co-browse iframe
		hostName = getRequest().getHeader("host");
		return Action.SUCCESS;
	}
	
	public String backFromIPOffice () throws Exception {
		validateSession();
		actionForm.setPrimary(null);
		actionForm.setSecondary(null);
		return Action.SUCCESS;
	}
		
	/*@Jpf.Action(useFormBean = "registrationFormBean",
			   forwards = {@Jpf.Forward(name = "IPO", path = "ipoInstall.jsp") })*/
	public String ipoReg() throws Exception {
		validateSession();
		//GRT 4.0 : Clearing the materialEntry list
		actionForm.setMaterialEntryList(null);
		try{
			logger.debug("INVENTORY FILE:"+actionForm.getIpoInventoryNotLoaded());
			if(actionForm.getRegistrationId() != null){
				RegistrationSummary summary = baseRegistrationService.getRegistrationSummary(actionForm.getRegistrationId());
				if(StringUtils.isEmpty(actionForm.getRemoteConnectivity()) ){
					actionForm.setRemoteConnectivity(summary.getRemoteConnectivity());
				}

				if("1002".equals(summary.getInstallBaseStatusId())&&((StringUtils.isNotEmpty(summary.getSrCompleted()))&& (summary.getSrCompleted().equalsIgnoreCase("no")))){
					//TODO code for adding Error
					logger.debug("Install Base in site InProcess and also the SR is not completed so showing Technical Order errors");
					actionForm.setIbaseStatus("1003");
				}
				logger.debug("Status ID "+summary.getInstallBaseStatusId()+"and Get submitted flag"+summary.getSubmitted());
				if("1002".equals(summary.getInstallBaseStatusId()) && (StringUtils.isNotEmpty(summary.getSubmitted())&& summary.getSubmitted().equals("1"))){
					logger.debug("Making Ibase readonly false because IBase is in process and submitted Flag is 1");
					this.installBaseCreationReadOnlyFlag = false;
				}
				if ((StringUtils.isNotEmpty(summary.getSubmitted())&& summary.getSubmitted().equals("0"))
						||("1008".equals(summary.getInstallBaseStatusId())) ){
					logger.debug("Setting the IBase creation to readonly because found a Submit already occured for this reg ID");
					this.installBaseCreationReadOnlyFlag = true;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
			getRequest().getSession().setAttribute("IPO","IPO");

			getRequest().getSession().setAttribute("IBBack","ipreg");
			actionForm.setSessionAttr("ipreg");
      return Action.SUCCESS;
}


	public String technicalRegistrationDashboard() {
		return null;
	}

	public String salGatewayMigrationList() {
		return null;
	}

	public Boolean getIpoContinueInstallBase() {
		return ipoContinueInstallBase;
	}

	public void setIpoContinueInstallBase(Boolean ipoContinueInstallBase) {
		this.ipoContinueInstallBase = ipoContinueInstallBase;
	}

	public Boolean getSaveAssistIPO() {
		return saveAssistIPO;
	}

	public void setSaveAssistIPO(Boolean saveAssistIPO) {
		this.saveAssistIPO = saveAssistIPO;
	}

	public IPOSchemaUtil getIpoSchemaUtil() {
		return ipoSchemaUtil;
	}

	public void setIpoSchemaUtil(IPOSchemaUtil ipoSchemaUtil) {
		this.ipoSchemaUtil = ipoSchemaUtil;
	}


	public List<TechnicalOrderDetail> getPendingOrders() {
		return pendingOrders;
	}


	public void setPendingOrders(List<TechnicalOrderDetail> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}


	public File getPrcsFile() {
		return prcsFile;
	}


	public void setPrcsFile(File prcsFile) {
		this.prcsFile = prcsFile;
	}


	public String getTheFileContentType() {
		return theFileContentType;
	}


	public void setTheFileContentType(String theFileContentType) {
		this.theFileContentType = theFileContentType;
	}


	public String getTheFileFileName() {
		return theFileFileName;
	}


	public void setTheFileFileName(String theFileFileName) {
		this.theFileFileName = theFileFileName;
	}


	public String getHostName() {
		return hostName;
	}


	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	
	
}
