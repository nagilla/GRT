package com.avaya.grt.ws.rest;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.service.BaseRegistrationService;
import com.avaya.grt.service.installbase.InstallBaseService;
import com.avaya.grt.service.installbase.InstallBaseServiceImpl;
import com.avaya.grt.web.action.GrtConfig;
import com.avaya.grt.web.security.AbstractSiteMinderAwareAction;
import com.avaya.grt.web.security.CSSPortalUser;
import com.avaya.registration.trsyncws.ARTResponseType;
import com.avaya.registration.trsyncws.OutputType;
import com.avaya.registration.trsyncws.RegResultType;
import com.avaya.registration.trsyncws.TRResponseType;
import com.grt.dto.SALGatewayInstallerDto;
import com.grt.dto.SALGatewayInstallerResponseDto;
import com.grt.dto.TRConfig;
import com.grt.integration.art.ARTClient;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;

@Path("/")
public class SalGwInstallerRest extends AbstractSiteMinderAwareAction {
	private static final Logger logger = Logger.getLogger(SalGwInstallerRest.class);

	private GrtConfig grtConfig;
	private InstallBaseService installBaseService;
	private ARTClient artClient;
	private CSSPortalUser cssPortalUser;

	@POST
	@Path("/salgwinstallerPOST")
	@Produces (MediaType.APPLICATION_XML)
	public String systemToSystemInstaller( 
			@FormParam("fl") String fl, 
			@FormParam("procserialno") String serialNumber, 
			@FormParam("aorig") String aorig,
			@FormParam("emailId") String emailId, 
			@FormParam("release") String release, 
			@FormParam("productIdentifier") String productIdentifier,
			@FormParam("prodtype") String productType,
			@FormParam("snick") String nickName,
			@Context HttpHeaders headers
			){
		logger.debug("Printing the parameters from Request...");
		logger.debug("Sold To : "+fl+"	serialNumber : "+serialNumber);
		logger.debug("aorig : "+aorig+"	 emailId : "+emailId);
		logger.debug("release : "+release+"	 productIdentifier : "+productIdentifier);
		logger.debug("prodtype : "+productType+"	 snick : "+nickName);
		logger.debug("Headers : "+headers);
		//initialize beans from application context
		initializeBeans();
		
		//Prepare user object from header
		getUserFromHeader(headers);

		//process the request
		Object responseObj = processSystemToSystemInstaller(fl, serialNumber, aorig, emailId, release, productIdentifier, nickName);

		//Generate xml response
		String responseXml = prepareXmlResponse(responseObj, productIdentifier);

		return responseXml;
	}

	@GET
	@Path("/salgwinstaller")
	@Produces (MediaType.APPLICATION_XML)
	public String systemToSystemInstallerGET( 
			@QueryParam("fl") String fl, 
			@QueryParam("procserialno") String serialNumber, 
			@QueryParam("aorig") String aorig,
			@QueryParam("emailId") String emailId, 
			@QueryParam("release") String release, 
			@QueryParam("productIdentifier") String productIdentifier,
			@QueryParam("prodtype") String productType,
			@QueryParam("snick") String nickName,
			@Context HttpHeaders headers){
		
		logger.debug("Printing the parameters from Request...");
		logger.debug("Sold To : "+fl+"	serialNumber : "+serialNumber);
		logger.debug("aorig : "+aorig+"	 emailId : "+emailId);
		logger.debug("release : "+release+"	 productIdentifier : "+productIdentifier);
		logger.debug("prodtype : "+productType+"	 snick : "+nickName);
		logger.debug("Headers : "+headers);
		//initialize beans from application context
		initializeBeans();
		
		//Prepare user object from header
		getUserFromHeader(headers);
		
		//Uncomment below code for local and comment getUserFromHeader
		/*try {
			cssPortalUser = getDummyCssUserProfile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//process the request
		Object responseObj = processSystemToSystemInstaller(fl, serialNumber, aorig, emailId, release, productIdentifier, nickName);

		//Generate xml response
		String responseXml = prepareXmlResponse(responseObj, productIdentifier);

		return responseXml;
	}


	/**
	 * @param fl
	 * @param serialNumber
	 * @param aorig
	 * @param emailId
	 * @param release
	 * @param productIdentifier
	 * Method for performing IB creation and technical registration
	 */
	public Object processSystemToSystemInstaller(String fl, String serialNumber, String aorig, String emailId, String release, String productIdentifier, String nickName){
		String groupId = "";
		String materialCode = "";
		String seCode = "";
		String productType = "";
		boolean existingInterface = false;
		Object responseObj = null;
		
		//Get the emailId of the user
		if( emailId == null || emailId.trim().length() == 0 ){
			emailId = getUserEmailId();
		}

		//New Interface Flow
		if( productIdentifier!=null && productIdentifier.trim().length() > 0 ){
			//Get the config values from db
			List<TRConfig> trConfigList = getInstallBaseService().fetchTRConfigsForSalGWInstaller(productIdentifier);
			if( trConfigList.size() > 0 ){ 
				logger.debug("Getting values for groupId/MC/SeCode/ProductType");
				TRConfig trConfig = trConfigList.get(0);
				groupId = trConfig.getGroupId();
				materialCode = trConfig.getParentMaterialCode();
				seCode = trConfig.getSeCode();
				productType = trConfig.getProductType();
			}else{
				logger.debug("No records found for the given Product Identifier");
				//Return Error 
				responseObj = prepareSalGwInstResponse(GRTConstants.INT_SERVER_STATUS_CODE, 
						 grtConfig.getSalgwInstErrMsg(), GRTConstants.INT_SERVER_STATUS_CODE);
				return responseObj;
			}
			
		}else{//Existing Iterface Flow
			//Get the config values from properties file
			String salGwDefaultVal = getGrtConfig().getSalgwInstDefaultVal();
			if( salGwDefaultVal!=null ){
				try{
					logger.debug("Getting values for groupId/MC/SeCode/ProductType");
					String defaultValARr[] = salGwDefaultVal.split("/");
					groupId = defaultValARr[0];
					materialCode = defaultValARr[1];
					seCode = defaultValARr[2];
					productType = defaultValARr[3];

					existingInterface = true;
				}catch(Exception e){
					logger.error("processSystemToSystemInstaller : while getting default value from properties file "+e.getMessage());
					//Return Error 
					responseObj = prepareSalGwInstResponse(GRTConstants.INT_SERVER_STATUS_CODE, 
							grtConfig.getSalgwInstErrMsg(), GRTConstants.INT_SERVER_STATUS_CODE);
					return responseObj;
				}
			}
		}
		//Prepare the dto object
		SALGatewayInstallerDto salInstallerDto = populateServiceRequestDto(fl,serialNumber,aorig,emailId,release,productIdentifier,
				groupId,materialCode,seCode,productType, nickName);
		if( checkForMandatoryFields(salInstallerDto) ) {
			//Call system to system to installer for salgw
			responseObj = doSystemToSytemInstallation(salInstallerDto, existingInterface);
		}else{
			logger.debug("Missing Mandatory parameters from request");
			//return error with mandatory message
			responseObj = prepareSalGwInstResponse(GRTConstants.MANDATORY_FIELD_ERR_CODE, 
					grtConfig.getSalgwInstMandValidMsg(), GRTConstants.MANDATORY_FIELD_STATUS_CODE);
		}
		return responseObj;
	}

	/**
	 * @param salInstallerDto
	 * @param existingInterface
	 * Perform the actual installation
	 */
	public Object doSystemToSytemInstallation(SALGatewayInstallerDto salInstallerDto, boolean existingInterface){
		SalGwInstallerResponse salInstallerResponse = null;
		Object responseObj = null;
		boolean skipIB = false;
		SALGatewayInstallerResponseDto ibResponse = null;
		String registrationId = null;
		//Create the Install Base
		try{
			//Check whether the Request is duplicate
			TechnicalOrder technicalOrder = getInstallBaseService().getTechnicalOrderByFilter(salInstallerDto.getMaterialCode(), salInstallerDto.getSerialNumber());
			
			//if( technicalOrder == null  ){
				logger.debug("Performing the system to system installation");
				
				//Duplicate Serial Number check 
				if( technicalOrder ==null ){
					logger.debug("Performing Installbase Creation...");
					ibResponse = getInstallBaseService().processSALGatewayIBCreation(salInstallerDto);
				}else{
					logger.debug("Skipping Installbase Creation as request is duplicate...");
					skipIB = true;//if request is duplicate skip install base creation and pass request to ART directly
				}
				
				if( skipIB || (ibResponse!=null && GRTConstants.installBase_successCode.equalsIgnoreCase(ibResponse.getStatus())) ){
					//Set the registration id
					if( skipIB ){
						salInstallerDto.setGrtId( technicalOrder.getSiteRegistration().getRegistrationId() );
					}else{
						salInstallerDto.setGrtId( ibResponse.getRegistrationId() );
					}

					SiteRegistration siteRegistration = null;
					try {
						siteRegistration = getBaseRegistrationService().getSiteRegistrationOnRegId(salInstallerDto.getGrtId());
						//Refresh the SiteRegistration Object
						siteRegistration = getBaseRegistrationService().refreshSiteRegistration(siteRegistration);
					} catch (DataAccessException e) {
						logger.error("Error: while fetching SiteRegistration object "+e.getMessage());
					}
					//Success : Perform the Technical Registration
					TechnicalOrder techOrder = siteRegistration.getTechnicalOrderByMaterialCode(salInstallerDto.getMaterialCode(), salInstallerDto.getSerialNumber());
					if(siteRegistration.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.FULLREGISTRATION.getRegistrationID())) {
						TechnicalRegistration technicalRegistration = new TechnicalRegistration();
						technicalRegistration.setTechnicalOrder(techOrder);
						technicalRegistration.setAccessType(GRTConstants.SAL);
						technicalRegistration.setProductCode(GRTConstants.SALGW_PRODUCT_TYPE);
						technicalRegistration.setSolutionElement(techOrder.getSolutionElementCode());
						Status status = new Status();
						status.setStatusId(StatusEnum.INITIATE.getStatusId());
						technicalRegistration.setStatus(status);

						technicalRegistration.setGroupId(salInstallerDto.getGroupId());
						technicalRegistration.setSerialNumber(salInstallerDto.getSerialNumber());
						technicalRegistration.setProductId(salInstallerDto.getProductIdentifier());
						technicalRegistration.setSoldToId(salInstallerDto.getSoldToId());
						technicalRegistration.setAlarmOrigination(salInstallerDto.getAorig());

						technicalRegistration.setOperationType(GRTConstants.DN);
						technicalRegistration.setStepASubmittedDate(new Date());
						//getBaseRegistrationService().saveTechnicalRegistration(technicalRegistration);
						
						ARTResponseType oldResponse = null;
						TRResponseType newResponse = null;
						salInstallerResponse = new SalGwInstallerResponse();
						String responseCode = "";
						String errMsg = "";
						Map<String, Object> responseMap = new HashMap<String, Object>();
						//Call the ART sync service
						if( existingInterface ){
							oldResponse = getArtClient().artSalGWTechRegOld(salInstallerDto);
							responseObj = oldResponse;
							/*salInstallerResponse.setArtResponseType(oldResponse);*/
							responseMap = getResponseCode(oldResponse);
						}else{
							newResponse = getArtClient().artSalGWTechRegNew(salInstallerDto);
							responseObj = newResponse;
							/*salInstallerResponse.setTrResponseType(newResponse);*/
							responseMap = getResponseCode(newResponse);
						}
						responseCode =  (String) responseMap.get(GRTConstants.RESPONSE_CODE);
							  errMsg =  (String) responseMap.get(GRTConstants.ERR_DETAILS);
						//technicalRegistration.setArtId(ack.getArtid());
						
						Status newStatus = new Status();
						boolean success = false;
						if( responseObj!=null ){
							//For success from ART side
							if( !StringUtils.isEmpty(responseCode) && (responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_SUCC_CODE_1) 
									|| responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_SUCC_CODE_2) || responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_SUCC_CODE_3))){
								newStatus.setStatusId(StatusEnum.COMPLETED.getStatusId());
								technicalRegistration.setStatus(newStatus);
								success = true;
							} else if( !StringUtils.isEmpty(responseCode) && !StringUtils.isEmpty(errMsg) 
									    && ( responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_ERR_CODE_1) ||  
											 responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_ERR_CODE_2) ||
											 responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_ERR_CODE_3) ||
											 responseCode.equalsIgnoreCase(GRTConstants.ART_SALGW_INST_ERR_CODE_4))
											 ){
								//Error from ART Side
								newStatus.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
								technicalRegistration.setStatus(newStatus);
							}else{
								newStatus.setStatusId(StatusEnum.INPROCESS.getStatusId());
								technicalRegistration.setStatus(newStatus);
							}
						}else{
							newStatus.setStatusId(StatusEnum.AWAITINGINFO.getStatusId());
							technicalRegistration.setStatus(newStatus);
							salInstallerResponse = prepareSalGwInstResponse(GRTConstants.INT_SERVER_STATUS_CODE, 
									GRTConstants.INT_SERVER_STATUS_CODE, grtConfig.getSalgwInstDownMsg());
							responseObj = salInstallerResponse;
						}
						
						//update the technicalregistration
						getBaseRegistrationService().saveTechnicalRegistration(technicalRegistration);
					}
				}
				else if(!GRTConstants.installBase_successCode.equalsIgnoreCase(ibResponse.getStatus())){
					logger.debug("Install base creation failed");
					salInstallerResponse = prepareSalGwInstResponse(GRTConstants.INT_SERVER_STATUS_CODE, 
												GRTConstants.INT_SERVER_ERR_CODE, grtConfig.getSalgwInstDownMsg());
					responseObj = salInstallerResponse;
				}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error while performing system to system integration for salgw : "+e.getMessage());
			salInstallerResponse = prepareSalGwInstResponse(GRTConstants.INT_SERVER_STATUS_CODE, 
									GRTConstants.INT_SERVER_STATUS_CODE,grtConfig.getSalgwInstErrMsg());
			
			responseObj = salInstallerResponse;
		}
		return responseObj;
	}

	/**
	 * @return String
	 * Get the emailid based on some conditions
	 */
	public String getUserEmailId(){
		//CSSPortalUser loggedInUser = getCSSUserProfile();
		CSSPortalUser loggedInUser = this.getCssPortalUser();
		//Uncomment below Code for Local
		/*CSSPortalUser loggedInUser = null;
		try {
			loggedInUser = getDummyCssUserProfile();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		String emailId = "";
		if( loggedInUser!=null ){
			//default email address from properties file
			String defaultServiceInBox = getGrtConfig().getSalgwInstEmailId();
			if( defaultServiceInBox == null && loggedInUser != null){
				emailId = loggedInUser.getUserId(); 
				if( loggedInUser.getUserType()!=null &&  loggedInUser.getUserType().equalsIgnoreCase("A") ){
					emailId+="@avaya.com";
				}
			}else{
				emailId = defaultServiceInBox;
			}
		}else{ 
			//TODO handle not logged in condition
		}
		return emailId;
	}

	/**
	 * @param fl
	 * @param serialNumber
	 * @param aorig
	 * @param emailId
	 * @param release
	 * @param productIdentifier
	 * @param groupId
	 * @param materialCode
	 * @param seCode
	 * @param productType
	 * @return SALGatewayInstallerDto3
	 * Prepare salgwdto object
	 */
	public SALGatewayInstallerDto populateServiceRequestDto(
			String fl, 
			String serialNumber, 
			String aorig,
			String emailId, 
			String release, 
			String productIdentifier,
			String groupId,
			String materialCode,
			String seCode,
			String productType,
			String nickName
			){
		SALGatewayInstallerDto dto = new SALGatewayInstallerDto();
		dto.setSoldToId(fl);
		dto.setSerialNumber(serialNumber);
		dto.setAorig(aorig);
		dto.setEmailID(emailId);
		dto.setReleaseNumber(release);
		dto.setProductIdentifier(productIdentifier);
		dto.setGroupId(groupId);
		dto.setMaterialCode(materialCode);
		dto.setSeCode(seCode);
		dto.setProductType(productType);
		dto.setNickName(nickName);

		return dto;
	}
	
	
	/**
	 * @param salInstallerDto
	 * @return boolean
	 * 	check whether all the mandatory fields are provided or not
	 *  if all fields are present send true else false
	 */
	public boolean checkForMandatoryFields( SALGatewayInstallerDto salInstallerDto ){
		logger.debug("checkForMandatoryFields() : Checking for mandatory fields in request");
		if( StringUtils.isEmpty(salInstallerDto.getSoldToId()) ){
			return false;
		}
		if( StringUtils.isEmpty(salInstallerDto.getSerialNumber()) ){
			return false;
		}
	 	
		return true;
	}
	
	/**
	 * Initialize beans from application context
	 */
	public void initializeBeans(){
		logger.debug("initializeBeans() : Initialize beans from application context");
		try{
			grtConfig = (GrtConfig) SpringApplicationContext.getBean("grtConfig");
			installBaseService = (InstallBaseServiceImpl) SpringApplicationContext.getBean("installBaseService");
			artClient =  (ARTClient) SpringApplicationContext.getBean("artClient");
			baseRegistrationService = (BaseRegistrationService) SpringApplicationContext.getBean("baseRegistrationService");
		}catch(Exception e){
			logger.error("initializeBeans() : Error while Initializing beans from application context - "+e.getMessage());
		}
	}
	
	/**
	 * @return SalGwInstallerResponse
	 * Generate response based on input fields
	 */
	public SalGwInstallerResponse prepareSalGwInstResponse(String returnCode, String status, String message){
		SalGwInstallerResponse salInstallerResponse = new SalGwInstallerResponse();
		salInstallerResponse.setReturnCode(returnCode);
		salInstallerResponse.setStatus(status);
		salInstallerResponse.setMessage(message);
		return salInstallerResponse;
	}

	/**
	 * @return String
	 * Get the response code from the object
	 */
	public Map<String, Object> getResponseCode( Object response ){
		String returnCode = "";
		String errorMsg = "";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		//Get the response code by the type of object
		if( response!=null ){
			if(  response instanceof ARTResponseType ){
				ARTResponseType artResponseType = (ARTResponseType) response;
				OutputType outputType = artResponseType.getOutput();
				RegResultType regType = outputType.getRegResult();
				returnCode = regType.getReturnCode();
				errorMsg = regType.getDescription();
			}else if( response instanceof TRResponseType ){
				TRResponseType trResponseType = (TRResponseType) response;
				returnCode = trResponseType.getStatus();
				errorMsg = trResponseType.getErrorMsg();
			}
		}
		responseMap.put(GRTConstants.RESPONSE_CODE, returnCode);
		responseMap.put(GRTConstants.ERR_DETAILS, errorMsg);
		return responseMap;
	}
	
	/**
	 * @param obj
	 * @return String
	 * Create xml response from the object
	 */
	public String prepareXmlResponse( Object responseObj, String productIdentifier ){
		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();
		boolean existingInterfaceRes = false;
		Object artResponse = responseObj;
		try {
			//Existing interface response
			/*if( responseObj instanceof ARTResponseType ){
				jaxbContext = JAXBContext.newInstance(ARTResponseType.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.marshal((ARTResponseType)responseObj, sw);
			//New Interface response
			}else if( responseObj instanceof TRResponseType ){
				jaxbContext = JAXBContext.newInstance(TRResponseType.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.marshal((TRResponseType)responseObj, sw);
			//Error or Validation response
			}else*/ 
			if( responseObj instanceof SalGwInstallerResponse ){
				SalGwInstallerResponse salgwResp = (SalGwInstallerResponse) responseObj;
				//New Interface Response
				if( productIdentifier!=null && !StringUtils.isEmpty(productIdentifier) ){
					TRResponseType trResponseType = new TRResponseType();
					trResponseType.setErrorMsg(salgwResp.getMessage());
					trResponseType.setStatus(salgwResp.getReturnCode());
					artResponse = trResponseType;
				}else{//Existing Interface Response
					ARTResponseType oldResp = new ARTResponseType();
					OutputType output = new OutputType();
					RegResultType regResult = new RegResultType();
					regResult.setDescription(salgwResp.getMessage());
					regResult.setReturnCode(salgwResp.getReturnCode());
					output.setRegResult(regResult);
					oldResp.setOutput(output);
					artResponse = oldResp;
					existingInterfaceRes = true;
				}
			}
			
			if( responseObj instanceof ARTResponseType || existingInterfaceRes ){
				jaxbContext = JAXBContext.newInstance(ARTResponseType.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.marshal((ARTResponseType)artResponse, sw);
			//New Interface response
			}else if( responseObj instanceof TRResponseType || !existingInterfaceRes ){
				jaxbContext = JAXBContext.newInstance(TRResponseType.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.marshal((TRResponseType)artResponse, sw);
			//Error or Validation response
			}
			
		} catch (JAXBException e) {
			logger.error("Error While creating XML Response : prepareXmlResponse() - "+e.getMessage());
		}
		return sw.toString();
	}
	
	/**
	 * @param headers
	 * @return CSSPortalUser
	 * Get Logged-In User details from header 
	 */
	public CSSPortalUser getUserFromHeader(HttpHeaders headers){
		logger.debug(" getUserFromRequest() : Printing User Info from Header ********* ");
		this.cssPortalUser = new CSSPortalUser();
		if( headers!=null ){
			String sm_user =  "";
			String firstName = "";
			String lastName = "";
			String smUserDN = "";
			String userDn = "";
			String proxyRemoteUser = "";
			if( headers.getRequestHeader("SM_USER")!=null ){
				sm_user =  headers.getRequestHeader("SM_USER").get(0);
				logger.debug(" headers.getRequestHeader('SM_USER') " + sm_user);
			}
			if( headers.getRequestHeader("first_name")!=null ){
				firstName =  headers.getRequestHeader("first_name").get(0);
				logger.debug(" headers.getRequestHeader('first_name') " + firstName);
			}
			if( headers.getRequestHeader("last_name")!=null ){
				lastName =  headers.getRequestHeader("last_name").get(0);
				logger.debug(" headers.getRequestHeader('last_name') " + lastName);
			}
			if( headers.getRequestHeader("SM_USERDN")!=null ){
				smUserDN =  headers.getRequestHeader("SM_USERDN").get(0);
				logger.debug(" headers.getRequestHeader('SM_USERDN') " + smUserDN);
			}
			if( headers.getRequestHeader("user_dn")!=null ){
				userDn =  headers.getRequestHeader("user_dn").get(0);
				logger.debug(" headers.getRequestHeader('user_dn') " + userDn);
			}
			if( headers.getRequestHeader("Proxy-Remote-User")!=null ){
				proxyRemoteUser =  headers.getRequestHeader("Proxy-Remote-User").get(0);
				logger.debug(" headers.getRequestHeader('Proxy-Remote-User') " + proxyRemoteUser);
			}

			cssPortalUser.setUserType(this.getUserType(smUserDN));
			logger.debug("After User Type....");
			cssPortalUser.setUserId(sm_user);
			logger.debug("After Getting SM_USER....");
			cssPortalUser.setFirstName(firstName);
			logger.debug("After Getting First Name....");
			cssPortalUser.setLastName(lastName);
			logger.debug("After Getting last_name....");
			
			if( !StringUtils.isEmpty(userDn) ){
				cssPortalUser.setBpLinkId(userDn);	
			}else if( !StringUtils.isEmpty(smUserDN) ){
				cssPortalUser.setBpLinkId(getBpLinkId(smUserDN));
			}
			
			logger.debug("After Getting user_dn....");
			cssPortalUser.setEmailAddress(proxyRemoteUser);
			logger.debug("After Getting email Address....");

			try {
				cssPortalUser.setSuperUser(getBaseRegistrationService().isSuperUser(
						cssPortalUser.getUserId()));
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		return cssPortalUser;
	}
	
	public GrtConfig getGrtConfig() {
		return grtConfig;
	}

	public void setGrtConfig(GrtConfig grtConfig) {
		this.grtConfig = grtConfig;
	}

	public InstallBaseService getInstallBaseService() {
		return installBaseService;
	}

	public void setInstallBaseService(InstallBaseService installBaseService) {
		this.installBaseService = installBaseService;
	}

	public ARTClient getArtClient() {
		return artClient;
	}

	public void setArtClient(ARTClient artClient) {
		this.artClient = artClient;
	}

	public CSSPortalUser getCssPortalUser() {
		return cssPortalUser;
	}

	public void setCssPortalUser(CSSPortalUser cssPortalUser) {
		this.cssPortalUser = cssPortalUser;
	}
	
	
}
