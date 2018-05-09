package com.avaya.grt.service;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.avaya.grt.jms.avaya.v2.techregistration.OutputType;
import com.avaya.grt.jms.avaya.v2.techregistration.ResponseType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegDataType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegResponseListType;
import com.avaya.grt.jms.avaya.v4.techregistration.OutputAlarmType;
import com.avaya.grt.jms.avaya.v4.techregistration.ResponseAlarmType;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegResponseAlarmListType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespDataType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveResponse;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespDataType;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespType;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespType;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateResponseType;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseRespDataType;
import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage.ListOfAvayaGrtRegistration.AvayaGrtRegistration;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.RegistrationQuestions;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.EquipmentMoveDataDTO;
import com.grt.dto.EquipmentMoveResponseDto;
import com.grt.dto.EquipmentRemovalDataDTO;
import com.grt.dto.EquipmentRemovalResponseDto;
import com.grt.dto.ErrorDto;
import com.grt.dto.InstallBaseAsset;
import com.grt.dto.InstallBaseAssetData;
import com.grt.dto.InstallBaseCreationResponseDto;
import com.grt.dto.InstallBaseRespDataDto;
import com.grt.dto.InstallBaseSapResponseDto;
import com.grt.dto.RegistrationSummary;
import com.grt.dto.SRRequestDto;
import com.grt.dto.SolutionElementListDto;
import com.grt.dto.TechRegDataResponseDto;
import com.grt.dto.TechRegResultResponseDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.GRTConstants;
import com.grt.util.TechRecordEnum;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;



public final class TechnicalRegistrationUtil {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationUtil.class);
	
	public TechnicalRegistrationUtil() {
		//Empty constructor
	}
	
	/**
	 * method to convert SR request to DTO
	 * @param srRequest
	 * @return
	 */
	public static SRRequestDto convertSRRequestDto(SRRequest srRequest) {
        if(srRequest == null) {
            return null;
        }
        SRRequestDto srRequestDto = new SRRequestDto();
        srRequestDto.setSrRequestId(srRequest.getSrRequestId());
        srRequestDto.setSiebelSRNo(srRequest.getSiebelSRNo());
        if(srRequest.getStatus() != null) {
            srRequestDto.setStatusId(srRequest.getStatus().getStatusId());
        }
        return srRequestDto;
    }

	
	/**
	 * method to convert SRRequestDto to SRRequest
	 * @param srRequestDto
	 * @return srRequest
	 */
	public static SRRequest convertSRRequest(SRRequestDto srRequestDto) {
        if(srRequestDto == null) {
            return null;
        }
        SRRequest srRequest = new SRRequest();
        srRequest.setSrRequestId(srRequestDto.getSrRequestId());
        srRequest.setSiebelSRNo(srRequestDto.getSiebelSRNo());

        if(StringUtils.isNotEmpty(srRequestDto.getStatusId())) {
            Status s = new Status();
            s.setStatusId(srRequestDto.getStatusId());
            srRequest.setStatus(s);
        }
        return srRequest;
    }

    /**
     * method to calculate no of hours
     * @param startDate
     * @return hours
     */
    public static long calculateNoOfHours(Date startDate){
    	long hours = 0;
    	try{
    	Date currentDate = new Date();
    	hours = (currentDate.getTime() - startDate.getTime())/(60*60*1000)%24;
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	return hours;
    }
    
    /**
     * method to parse jmsMessage xml
     * @param jmsMessage
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
	public static SiebelMessage parseXMLJms(String jmsMessage) throws JAXBException {
	    logger.debug("Entering TechnicalRegistrationUtil.parseXMLJms Parsing incoming message into Siebel Service Request ...");
		ByteArrayInputStream xmlContentBytes = 
	            new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
	            com.avaya.grt.jms.siebel.xml.assetupdate.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
	            JAXBContext.newInstance("com.avaya.grt.jms.siebel.xml.siebel.assetupdate.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		Object obj = unmarshaller.unmarshal(xmlContentBytes);
		logger.debug("<><><><><><><><><><><><><><><><><><>" + obj.getClass().getName());
		SiebelMessage msgAssetUpdate = (SiebelMessage) obj;
	    logger.debug("Completed TechnicalRegistrationUtil.parseXMLJms...");
		return msgAssetUpdate;
    }
    
    /**
     * method to populate ServiceRequestDto
     * @param msgServiceAsset
     * @param txtRegistrationId
     * @return
     */
    public static InstallBaseAsset populateServiceRequestDto(SiebelMessage msgServiceAsset, String txtRegistrationId) {
    	 logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDto");
    	 StringBuffer sb = new StringBuffer();
 		sb.append("Entering TechnicalRegistrationUtil.populateServiceRequestDto to create a Data transfer object from the request object... ");
		boolean gotHeader = false;
		InstallBaseAsset assetResDto = new InstallBaseAsset();
		
		List<AvayaGrtRegistration> avayaAssetList  = msgServiceAsset.getListOfAvayaGrtRegistration().getAvayaGrtRegistration();
		sb.append("Avaya asset list is recieved from Service asset coming from Siebel. ");
		if(StringUtils.isNotEmpty(txtRegistrationId)){
			assetResDto.setRegistrationId(txtRegistrationId);
			gotHeader = true;
		}
		sb.append("Set the Reg id for the asset in DTO and make complete DTO from Asset List ");
		for(AvayaGrtRegistration avayaAsset :avayaAssetList){
			InstallBaseAssetData installBaseAssetData = new InstallBaseAssetData();
			installBaseAssetData.setAssetNumber(avayaAsset.getAssetNumber());
			if(!gotHeader && StringUtils.isEmpty(assetResDto.getRegistrationId())){
				String regId = avayaAsset.getRegistrationId();
				
				if (StringUtils.isNotEmpty(regId)) {
					regId = regId.substring(regId.lastIndexOf("_")+1, regId.length());
					assetResDto.setRegistrationId(regId);
				}
			}
			logger.debug("Asset Number set to DTO::::" + installBaseAssetData.getAssetNumber());
			installBaseAssetData.setQuantity(avayaAsset.getQuantity());
			installBaseAssetData.setMaterialCode(avayaAsset.getMaterialCode());
			installBaseAssetData.setSerialNumber(avayaAsset.getSerialNumber());
			assetResDto.getInstallBaseAssetData().add(installBaseAssetData);
		}
		sb.append("Exiting from For loop after set the Asset DTO with the Payload Data");
		logger.debug("DTO populated for Asset Update... \n" );
		assetResDto.setRoutingInfo(sb.toString());
		 logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDto");
		return assetResDto;
	}
	
    /**
     * method to extract JMS body xml
     * @param xml
     * @return
     * @throws Exception
     */
    public static String extractJmsBodyXml(String xml) throws Exception{
    	 logger.debug("Entering TechnicalRegistrationUtil.extractJmsBodyXml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

		//Set up the transformer to write the output string
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);

		//Find the last child node(jmsBody) - this could be done with xpath as well
		NodeList nl = doc.getDocumentElement().getChildNodes();
		DOMSource source = null;
		if(nl.getLength() > 0) {
			Node e = nl.item(nl.getLength() - 1);
			if (e instanceof Element) {
				source = new DOMSource(e.getFirstChild());
			}
		}
		//Do the transformation and output
		transformer.transform(source, result);
		 logger.debug("Exiting TechnicalRegistrationUtil.extractJmsBodyXml");
		return sw.toString();
	}

    /**
     * method to parse XML JMS for install Base
     * @param jmsMessage
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
	public static IBaseCreateRespType parseXMLJmsIBase(String jmsMessage) throws JAXBException {
        logger.debug("Entering TechnicalRegistrationUtil.parseXMLJmsIBase Parsing incoming message into IBase Creation Request ...");
		ByteArrayInputStream xmlContentBytes = 
                new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
                com.avaya.grt.jms.fmw.xml.filePoller.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
                JAXBContext.newInstance("com.avaya.grt.jms.fmw.xml.filePoller.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		JAXBElement<IBaseCreateRespType> iBaseMessageObj = 
                (JAXBElement<IBaseCreateRespType>) unmarshaller.unmarshal(xmlContentBytes);
		IBaseCreateRespType iBaseRespMessage = iBaseMessageObj.getValue();
	    logger.debug("Completed TechnicalRegistrationUtil.parseXMLJmsIBase");
		return iBaseRespMessage;
	}
	
	/**
	 * method to parse XML JMS for equipment remove
	 * @param jmsMessage
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static EquipmentRemovalRespType parseXMLJmsEqr(String jmsMessage) throws JAXBException {
        logger.debug("Entering TechnicalRegistrationUtil.parseXMLJmsEqr Parsing incoming message into Equipment Removal response ...");
		ByteArrayInputStream xmlContentBytes = 
                new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
				com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
                JAXBContext.newInstance("com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		JAXBElement<EquipmentRemovalRespType> equipmentRemovalResponseMessageObj = 
                (JAXBElement<EquipmentRemovalRespType>) unmarshaller.unmarshal(xmlContentBytes);
		EquipmentRemovalRespType equipmentRemovalRespType = equipmentRemovalResponseMessageObj.getValue();
	    logger.debug("Completed TechnicalRegistrationUtil.parseXMLJmsEqr");
		return equipmentRemovalRespType;
	}
	

	
	/**
	 * method to populate ServiceRequestDto for IB
	 * @param msgFilePoller
	 * @param txtTransactionId
	 * @param txtErrorId
	 * @return
	 */
	public static InstallBaseSapResponseDto populateServiceRequestDto(IBaseCreateRespType msgFilePoller, String txtTransactionId, String txtErrorId) {
		logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDto");
		StringBuffer sb = new StringBuffer();
		sb.append("Entering TechnicalRegistrationUtil.populateServiceRequestDto to create a Data transfer object from the request object... ");
		InstallBaseSapResponseDto installBaseDTO = new InstallBaseSapResponseDto();
		List<IBaseRespDataType> installBaseList = msgFilePoller.getIBaseRespData();

		List<InstallBaseRespDataDto> IbaseDtoList = installBaseDTO.getIBaseRespData();
		sb.append("Entering into For loop to set the DTO with the Payload Data... ");
		for (IBaseRespDataType iBaseRespDataType : installBaseList) {
			InstallBaseRespDataDto respDto = new InstallBaseRespDataDto();
			respDto.setAssetNumber(iBaseRespDataType.getAsset());
			logger.debug("Asset Number set to DTO::::" + respDto.getAssetNumber());
			respDto.setMaterialCode(iBaseRespDataType.getMaterialCode());
			respDto.setQuantity(iBaseRespDataType.getQuantity());
			respDto.setShipToId(iBaseRespDataType.getShipTo());
			respDto.setSoldToId(iBaseRespDataType.getSoldTo());
			respDto.setSerialNumber(iBaseRespDataType.getSerialNumber());
			respDto.setErrorDesc(iBaseRespDataType.getErrorDescription());
			
			BigDecimal defaultQuantity = new BigDecimal("0.0");			
			if(StringUtils.isNotEmpty(iBaseRespDataType.getBeforeQuantity()))
				respDto.setBeforeQuantity(new BigDecimal(iBaseRespDataType.getBeforeQuantity()));
			else
				respDto.setBeforeQuantity(defaultQuantity);
			
			if(StringUtils.isNotEmpty(iBaseRespDataType.getAfterQuantity()))
				respDto.setAfterQuantity(new BigDecimal(iBaseRespDataType.getAfterQuantity()));
			else
				respDto.setAfterQuantity(defaultQuantity);
			
			IbaseDtoList.add(respDto);
			logger.debug("Error Description ::::" + respDto.getErrorDesc());
		}
		sb.append("Exiting from For loop after set the DTO with the Payload Data");
		logger.debug("IbaseDtoList Sizeeeee::::" + IbaseDtoList.size());
		sb.append("DTO List Size is :"+ IbaseDtoList.size());
		// set Header values
		installBaseDTO.setIBaseRespData(IbaseDtoList);
		installBaseDTO.setErrorId(txtErrorId);
		installBaseDTO.setRegistrationId(txtTransactionId);
		
		logger.debug("Registration Id set to DTO::::\n" + installBaseDTO.getRegistrationId());
		logger.debug("Completed Set message to DTO for FilePoller... \n");
		sb.append("Exiting TechnicalRegistrationUtil.populateServiceRequestDto after creating a Data transfer object from the request payload... ");
		logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDto");
		installBaseDTO.setRoutingInfo(sb.toString());
		
		return installBaseDTO;
	}
	
	
	
	/**
	 * method to populate ServiceRequestDto for EQR
	 * @param msgEquipmentRemovalResponseType
	 * @param txtTransactionId
	 * @param txtErrorId
	 * @return
	 */
	public static EquipmentRemovalDataDTO populateServiceRequestDto(
    		EquipmentRemovalRespType msgEquipmentRemovalResponseType, String txtTransactionId, String txtErrorId) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering TechnicalRegistrationUtil.populateServiceRequestDto to create a Data transfer object from the request object for EQR...");
		 logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDto");
    	EquipmentRemovalDataDTO equipmentRemovalDataDTO = new EquipmentRemovalDataDTO();
    	List<EquipmentRemovalRespDataType> equipmentRemovalRespDataTypeList= msgEquipmentRemovalResponseType.getEquipmentRemovalRespData();
    	List<EquipmentRemovalResponseDto> equipmentRemovalResponseDtoList = new ArrayList<EquipmentRemovalResponseDto>();
    	sb.append("Entering into For loop to set the DTO with the Payload Data... ");
    	EquipmentRemovalResponseDto respDto = null;
		for (EquipmentRemovalRespDataType equipmentRemovalRespDataType:equipmentRemovalRespDataTypeList) {
			respDto = new EquipmentRemovalResponseDto();
			if (equipmentRemovalRespDataType.getAction() != null) {
				respDto.setAction(equipmentRemovalRespDataType.getAction().toString());
			}
			respDto.setEquipmentNumber(equipmentRemovalRespDataType.getEquipmentNumber());
			logger.debug("populateServiceRequestDto: Equipment Number:  "+equipmentRemovalRespDataType.getEquipmentNumber());
	    	respDto.setInitialQuantity(equipmentRemovalRespDataType.getInitialQuantity());
	    	respDto.setFinalQuantity(equipmentRemovalRespDataType.getFinalQuantity());
	    	respDto.setShipTo(equipmentRemovalRespDataType.getShipTo());
	    	logger.debug("populateServiceRequestDto: Sold To:  "+equipmentRemovalRespDataType.getShipTo());
	    	respDto.setErrorDescription(equipmentRemovalRespDataType.getErrorDescription());
	    	
	    	BigDecimal defaultQuantity = new BigDecimal("0.0");			
			if(StringUtils.isNotEmpty(equipmentRemovalRespDataType.getBeforeQuantity()))
				respDto.setBeforeQuantity(new BigDecimal(equipmentRemovalRespDataType.getBeforeQuantity()));
			else
				respDto.setBeforeQuantity(defaultQuantity);
			
			if(StringUtils.isNotEmpty(equipmentRemovalRespDataType.getAfterQuantity()))
				respDto.setAfterQuantity(new BigDecimal(equipmentRemovalRespDataType.getAfterQuantity()));
			else
				respDto.setAfterQuantity(defaultQuantity);
	    	
	    	equipmentRemovalResponseDtoList.add(respDto);
		}
		sb.append("Exiting from For loop after set the DTO with the Payload Data... ");
		logger.debug("IbaseDtoList Sizeeeee::::" + equipmentRemovalResponseDtoList.size());
		// set Header values
		equipmentRemovalDataDTO.setErrorId(txtErrorId);
		equipmentRemovalDataDTO.setRegistrationId(txtTransactionId);
		equipmentRemovalDataDTO.setEquipmentRemovalResponseDto(equipmentRemovalResponseDtoList);

		logger.debug("Registration Id set to DTO::::\n" + equipmentRemovalDataDTO.getRegistrationId());
		logger.debug("Completed Set message to DTO for FilePoller... \n");
		sb.append("Exiting TechnicalRegistrationUtil.populateServiceRequestDto after creating a Data transfer object from the request payload... ");
		logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDto");
		equipmentRemovalDataDTO.setRoutingInfo(sb.toString());
		return equipmentRemovalDataDTO;
	}
	
	/**
	 * method to populate ServiceRequestDto for install base creation response
	 * @param msgIbaseCreationResp
	 * @param txtTransactionId
	 * @param txtErrorId
	 * @return
	 */
	public static InstallBaseCreationResponseDto populateServiceRequestDto(IBaseCreateResponseType msgIbaseCreationResp, String txtTransactionId, String txtErrorId) {
		logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDto");
		InstallBaseCreationResponseDto iBaseCreateRespDto = new InstallBaseCreationResponseDto();
		
		iBaseCreateRespDto.setCode(msgIbaseCreationResp.getCode());
		iBaseCreateRespDto.setDescription(msgIbaseCreationResp.getDescription());
		logger.debug("Code set to DTO::::" + iBaseCreateRespDto.getCode());
		//Set Header
		iBaseCreateRespDto.setTransactioId(txtTransactionId);
		iBaseCreateRespDto.setErrorId(txtErrorId);
		logger.debug("Completed Set message to DTO for IBase Create Response... \n" );	
		logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDto");
		return iBaseCreateRespDto;
	}
	
	/**
	 * method to truncate leading zeroes.
	 * @param data
	 * @return
	 */
	public static String truncateLeadingZeros(String data){
		return data!=null ? data.replaceFirst("^0+(?!$)", ""):"0";
	}

    public static String filterErrorDescription(String errorDesc){
    	if(errorDesc != null
				&& (errorDesc.equalsIgnoreCase("Equipment is not installed at any location")
					|| errorDesc.equalsIgnoreCase("Invalid Equipment nr")
    				|| errorDesc.equalsIgnoreCase("Functional location doesn't exists in SAP")
    				|| errorDesc.equalsIgnoreCase("Material doesn't exists in SAP")
    				|| errorDesc.contains("Equipment is installed at a different FL"))){
			return null;
		} else {
			return errorDesc;
		}
    }
    
    /**
     * method to remove duplicate string from List
     * @param objectList
     * @return
     */
    public static List<String> removeDuplicateStringFromList(List<String> objectList){
    	List<String> refinedList = new ArrayList<String>();
    	if(objectList != null && objectList.size() > 0){
    		for(String string : objectList){
    			if(!refinedList.contains(string)){
    				refinedList.add(string);
    			}
    		}
    	}
    	return refinedList;
    }
    
    /**
     * method to prepare TechnicalOrderDetailList
     * @param technicalOrderDetailList
     * @param orderType
     * @return
     */
    public static List<TechnicalOrderDetail> prepareTechnicalOrderDetailList(List<TechnicalOrder> technicalOrderDetailList, String orderType){
    	logger.debug("Entering TechnicalRegistrationUtil.prepareTechnicalOrderDetailList");
    	List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
        TechnicalOrderDetail technicalOrderDto = null;

        if (technicalOrderDetailList != null) {
            for (TechnicalOrder technicalOrder : technicalOrderDetailList) {
                technicalOrderDto = new TechnicalOrderDetail();
                technicalOrderDto.setRegistrationId(technicalOrder
                        .getSiteRegistration().getRegistrationId());
                technicalOrderDto.setMaterialCode(technicalOrder
                        .getMaterialCode());
                technicalOrderDto.setInitialQuantity(technicalOrder
                        .getInitialQuantity());
                /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (Start) **/
                logger.debug("Setting Serial Number to: getTechnicalOrderByType "+technicalOrder.getSerialNumber());
                technicalOrderDto.setSerialNumber(technicalOrder.getSerialNumber());
                /**  [AVAYA]: 09-13-2011 Setting Serial Number to Save in Registration Service (End) **/
                technicalOrderDto.setRemainingQuantity(technicalOrder
                        .getRemainingQuantity());
                technicalOrderDto.setOrderId(technicalOrder.getOrderId());
                technicalOrderDto.setProcessStep(technicalOrder
                        .getSiteRegistration().getProcessStep()
                        .getProcessStepId());
                technicalOrderDto.setStatusId(technicalOrder
                        .getSiteRegistration().getStatus().getStatusId());
                technicalOrderDto.setDescription(technicalOrder
                        .getDescription());
                technicalOrderDto.setOrderType(technicalOrder
                        .getOrderType());
                technicalOrderDto.setIsBaseUnit(technicalOrder.getIsBaseUnit());
                technicalOrderDto.setIsSourceIPO(technicalOrder.getIsSourceIPO());
                technicalOrderDto.setIpoFlagIbaseJsp(technicalOrder.isIPO());
                technicalOrderDto.setReleaseString(technicalOrder.getReleaseString());
                technicalOrderDto.setErrorDescription(technicalOrder.getError_Desc());
                if(StringUtils.isNotEmpty(technicalOrder.getDeleted()) &&
                        GRTConstants.YES.equalsIgnoreCase(technicalOrder.getDeleted())) {
                    technicalOrderDto.setDeleted(true);
                }
                else {
                    technicalOrderDto.setDeleted(false);
                }
                technicalOrderDto.setIsSalesOut(technicalOrder.getIsSalesOut());
                if( technicalOrder.getRemainingQuantity() != null && StringUtils.isNotEmpty(technicalOrder.getRemainingQuantity().toString())){
                	technicalOrderDto.setRemovedQuantity(Math.abs(technicalOrder.getInitialQuantity() - technicalOrder.getRemainingQuantity()));
                }
                technicalOrderDto.setEquipmentNumber(technicalOrder.getEquipmentNumber());
                
                technicalOrderDto.setSummaryEquipmentNumber(technicalOrder.getSummaryEquipmentNumber());
                technicalOrderDto.setSolutionElementCode(technicalOrder.getSolutionElementCode());
                technicalOrderDto.setSolutionElementId(technicalOrder.getSeid());
                technicalOrderDto.setProductLine(technicalOrder.getProductLine());
                technicalOrderDto.setSerialNumber(technicalOrder.getSerialNumber());
                if(technicalOrder.getHasActiveEquipmentContract() != null && technicalOrder.getHasActiveEquipmentContract() == 1){
                	technicalOrderDto.setTechnicallyRegisterable(GRTConstants.YES);
                } else {
                	technicalOrderDto.setTechnicallyRegisterable("");
                }
                if(technicalOrder.getHasActiveSiteContract() != null && technicalOrder.getHasActiveSiteContract() == 1){
                	technicalOrderDto.setActiveContractExist(GRTConstants.YES);
                } else {
                	technicalOrderDto.setActiveContractExist("");
                }

                technicalOrderDto.setAssetPK(technicalOrder.getAssetPK());
                technicalOrderDto.setSid(technicalOrder.getSid());
                technicalOrderDto.setMid(technicalOrder.getMid());
                technicalOrderDto.setPipelineIBQuantity(0L);
                technicalOrderDto.setPipelineEQRQuantity(0L);
                technicalOrderDto.setActionType(technicalOrder.getActionType());
                /* GRT4.0 changes starts */
                
                if( (GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)) || (GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType)) ){
	                if(StringUtils.isNotEmpty(technicalOrder.getMaterialExclusion()) 
	                		&& !GRTConstants.EXCLUSION_SOURCE_NMPC.equalsIgnoreCase(technicalOrder.getMaterialExclusion())){
	                	
	                	technicalOrderDto.setExclusionSource(technicalOrder.getMaterialExclusion());
	                	technicalOrderDto.setExclusionFlag(true);
	                	if(StringUtils.isNotEmpty(technicalOrderDto.getExclusionSource())
								&& GRTConstants.EXCLUSION_SOURCE_PLDS.equalsIgnoreCase(technicalOrderDto.getExclusionSource())){
	                				technicalOrderDto.setErrorDescription("Install Base automatically fed from PLDS; move to technical onboarding or manage in PLDS.");
						} else if (StringUtils.isNotEmpty(technicalOrderDto.getExclusionSource())
								&& GRTConstants.EXCLUSION_SOURCE_ESNA.equalsIgnoreCase(technicalOrderDto.getExclusionSource())) {
							technicalOrderDto.setErrorDescription("Install Base automatically fed from ESNA; move to technical onboarding or manage in ESNA.");
						}
	                	else if (StringUtils.isNotEmpty(technicalOrderDto.getExclusionSource())
								&& GRTConstants.EXCLUSION_SOURCE_DEFECTIVE.equalsIgnoreCase(technicalOrderDto.getExclusionSource())) {
							technicalOrderDto.setErrorDescription("Defective material code thus cannot be registered.");
						} else if (StringUtils.isNotEmpty(technicalOrderDto.getExclusionSource())
								&& GRTConstants.EXCLUSION_SOURCE_BLUE.equalsIgnoreCase(technicalOrderDto.getExclusionSource())) {
							technicalOrderDto.setErrorDescription("Blue/Nortel material code thus cannot be registered.");
						} else {
							technicalOrderDto.setErrorDescription("Offer/Tracking Codes do not qualify for Services Install Base and thus cannot be registered.");
						}
	                } else {
	                	technicalOrderDto.setExclusionFlag(false);
	                	technicalOrderDto.setErrorDescription(technicalOrder.getError_Desc());
	                }
                }
                /* GRT4.0 changes ends */
                technicalOrderDto.setSalGateway(technicalOrder.isSalGateway());
                technicalOrderDtoList.add(technicalOrderDto);
            }
        }
        logger.debug("Exiting TechnicalRegistrationUtil.prepareTechnicalOrderDetailList");
        return technicalOrderDtoList;
    }
    
    /**
     * method to construct copy of TechnicalOrderDetail object
     * @param todDto
     * @return
     */
    public static TechnicalOrderDetail constructTechnicalOrderDetailClone(TechnicalOrderDetail todDto){
    	 logger.debug("Entering TechnicalRegistrationUtil.constructTechnicalOrderDetailClone");
		TechnicalOrderDetail technicalOrderDetail = null;
		if(todDto != null){
			technicalOrderDetail = new TechnicalOrderDetail();
			technicalOrderDetail.setDeleted(todDto.getDeleted());
			technicalOrderDetail.setAssetPK(todDto.getAssetPK());
			technicalOrderDetail.setAssetNumber(todDto.getAssetNumber());
			technicalOrderDetail.setEquipmentNumber(todDto.getEquipmentNumber());
			technicalOrderDetail.setSummaryEquipmentNumber(todDto.getSummaryEquipmentNumber());
			technicalOrderDetail.setToSoldToId(todDto.getToSoldToId());
			technicalOrderDetail.setToEquipmentNumber(todDto.getToEquipmentNumber());
			technicalOrderDetail.setMaterialCode(todDto.getMaterialCode());
			technicalOrderDetail.setDescription(todDto.getDescription());
    		technicalOrderDetail.setInitialQuantity(todDto.getInitialQuantity());
    		if(todDto.getRemainingQuantity() != null)
    			technicalOrderDetail.setRemainingQuantity(todDto.getRemainingQuantity());
    		if(todDto.getRemovedQuantity() != null)
    			technicalOrderDetail.setRemovedQuantity(todDto.getRemovedQuantity());
    		if(todDto.getPipelineEQRQuantity() != null) {
    			technicalOrderDetail.setPipelineEQRQuantity(todDto.getPipelineEQRQuantity());
    		} else {
    			technicalOrderDetail.setPipelineEQRQuantity(0L);
    		}
    		if(todDto.getPipelineIBQuantity() != null) {
    			technicalOrderDetail.setPipelineIBQuantity(todDto.getPipelineIBQuantity());
    		} else {
    			technicalOrderDetail.setPipelineIBQuantity(0L);
    		}
    		if(todDto.getPipelineEQRMoveQuantity() != null) {
    			technicalOrderDetail.setPipelineEQRMoveQuantity(todDto.getPipelineEQRMoveQuantity());
    		} else {
    			technicalOrderDetail.setPipelineEQRMoveQuantity(0L);
    		}
    		technicalOrderDetail.setSolutionElementId(todDto.getSolutionElementId());
    		technicalOrderDetail.setSolutionElementCode(todDto.getSolutionElementCode());
    		technicalOrderDetail.setTechnicallyRegisterable(todDto.getTechnicallyRegisterable());
    		technicalOrderDetail.setActiveContractExist(todDto.getActiveContractExist());
    		technicalOrderDetail.setProductLine(todDto.getProductLine());
    		technicalOrderDetail.setSerialNumber(todDto.getSerialNumber());
    		technicalOrderDetail.setSid(todDto.getSid());
    		technicalOrderDetail.setMid(todDto.getMid());
    		technicalOrderDetail.setGroupId(todDto.getGroupId());
    		technicalOrderDetail.setExclusionSource(todDto.getExclusionSource());
    		technicalOrderDetail.setExclusionFlag(todDto.isExclusionFlag());
    		technicalOrderDetail.setErrorDescription(todDto.getErrorDescription());
    		technicalOrderDetail.setSoldToId(todDto.getSoldToId());
    		technicalOrderDetail.setSalGateway(todDto.isSalGateway());
    		technicalOrderDetail.setBeforeQuantity(todDto.getBeforeQuantity());
    		technicalOrderDetail.setAfterQuantity(todDto.getAfterQuantity());
    		technicalOrderDetail.setSerialized(todDto.getSerialized()); 
    		technicalOrderDetail.setActionType(todDto.getActionType()); 
    		technicalOrderDetail.setRegStatus(todDto.getRegStatus());
			technicalOrderDetail.setNickName(todDto.getNickName());
			technicalOrderDetail.setOriginalSerialNumber(todDto.getOriginalSerialNumber());
			
			//GRT 4.0 Changes Defect 327-328
			technicalOrderDetail.setIsSAP(todDto.getIsSAP());
			technicalOrderDetail.setIsNortel(todDto.getIsNortel());
			technicalOrderDetail.setIsMaestro(todDto.getIsMaestro());
			technicalOrderDetail.setRegistrationId(todDto.getRegistrationId());
			
		}
		 logger.debug("Exiting TechnicalRegistrationUtil.constructTechnicalOrderDetailClone");
		return technicalOrderDetail;
	}

    
    /**
     * method to get RegistrationSummary SR Creation
     * @param salProductRegistration
     * @param type
     * @return
     */
    public static RegistrationSummary getRegistrationSummarySRCreation(SalProductRegistration salProductRegistration, TechRecordEnum type) {
		logger.debug("Entering TechnicalRegistrationUtil.getRegistrationSummarySRCreation"+ " Type " +type );
		String registrationId = "";
		RegistrationSummary result = new RegistrationSummary();
		SiteRegistration siteRegistration = null;

		  if(TechRecordEnum.IP_MODEM.getSalType().equals(type.getSalType()) || TechRecordEnum.IPO.getSalType().equals(type.getSalType())) {
	        	TechnicalRegistration technicalRegistration = salProductRegistration.getTechnicalRegistration();
	        	siteRegistration = technicalRegistration.getTechnicalOrder().getSiteRegistration();
	        	logger.debug(" getRegistrationSummarySRCreation : IPO - IP - Modem ");
	        }

		result.setRegistrationId(siteRegistration.getRegistrationId());
		result.setExpedite(siteRegistration.getExpedite());
		SRRequestDto ibSrRequestDto = convertSRRequestDto(siteRegistration.getInstallBaseSrRequest());
		result.setInstallBaseSrRequest(ibSrRequestDto);
		SRRequestDto fvSrRequestDto = convertSRRequestDto(siteRegistration.getFinalValidationSrRequest());
		result.setFinalValidationSrRequest(fvSrRequestDto);
		result.setOnsiteEmail(siteRegistration.getOnsiteEmail());
		result.setOnsiteFirstName(siteRegistration.getOnsiteFirstName());
		result.setOnsiteLastName(siteRegistration.getOnsiteLastName());
		result.setOnsitePhone(siteRegistration.getOnsitePhone());
		result.setSoldTo(siteRegistration.getSoldToId());
		result.setProcessStepId(siteRegistration.getProcessStep()
		        .getProcessStepId());
		result.setProcessStep(siteRegistration.getProcessStep()
		        .getProcessStepShortDescription());

		result.setInstallBaseStatusId(siteRegistration.getInstallBaseStatus().getStatusId());
		result.setInstallBaseStatus(siteRegistration.getInstallBaseStatus().getStatusDescription());
		result.setTechRegStatusId(siteRegistration.getTechRegStatus().getStatusId());
		result.setTechRegStatus(siteRegistration.getTechRegStatus().getStatusDescription());
		result.setFinalValidationStatusId(siteRegistration.getFinalValidationStatus().getStatusId());
		result.setFinalValidationStatus(siteRegistration.getFinalValidationStatus().getStatusDescription());

		result.setSiteCountry(siteRegistration.getSiteCountry());
		result.setReportEmailId(siteRegistration.getReportEmailId());
		result.setUserName(siteRegistration.getUserName());
		result.setNoAdditionalProductFlag(siteRegistration.getNoAdditionalProductFlag());
		result.setSkipInstallBaseCreation(siteRegistration.getSkipInstallBaseCreation());
		result.setSalMigrationOnly(siteRegistration.getSalMigrationOnly());
		result.setOnBoarding(siteRegistration.getOnBoardingFileExisting());
		if(siteRegistration.getRegistrationQuestions()!=null){
			logger.debug("Got registration Questons");
			java.util.Set<RegistrationQuestions> getter = siteRegistration.getRegistrationQuestions();
			for(RegistrationQuestions it:getter){
				if(it.getQuestionKey().equalsIgnoreCase("remoteconnctivity")){
					result.setRemoteConnectivity(it.getAnswerKey());
					//logger.debug("Got remote connectivity"+it.getAnswerKey());

				}
			}
		 }

        result.setRegistrationTypeId(siteRegistration.getRegistrationType().getRegistrationId());

		result.setImpl(siteRegistration.getTypeOfImplementation());
		result.setSubmitted(siteRegistration.getSubmitted());
		logger.debug("get type of IMPL"+siteRegistration.getTypeOfImplementation());
		logger.debug("Exiting TechnicalRegistrationUtil.getRegistrationSummarySRCreation");

		return result;
	}
    
    /**
     * method to populate SR request DTO list
     * @param msgTechRegResponse
     * @param txtTransactionId
     * @param txtErrorId
     * @return
     */
    @SuppressWarnings("restriction")
	public static List<TechRegDataResponseDto> populateServiceRequestDtoList(ResponseType msgTechRegResponse, String txtTransactionId, String txtErrorId) {
		 logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDtoList()");
		List<TechRegDataResponseDto> techRegRespDtoList = new ArrayList<TechRegDataResponseDto>();           
	
		// Set Input
		List<TechRegResponseListType> techRegResponseList = msgTechRegResponse.getTechRegResponseList();
		if (techRegResponseList != null && techRegResponseList.size()>0) {
			for (TechRegResponseListType techRegResponseListType : techRegResponseList) {
				TechRegDataType techRegDataType = techRegResponseListType.getRegInput();
				TechRegDataResponseDto techRegRespDto = new TechRegDataResponseDto();
				//Set  Header
				techRegRespDto.setRegistrationId(txtTransactionId);
				 logger.debug("Transaction ID===========>"+txtTransactionId+"     ");
				 logger.debug("techRegDataType.getTechRegId()===========>"+techRegDataType.getTechRegId()+"     ");
				techRegRespDto.setTransactionId(techRegDataType.getTechRegId());
				techRegRespDto.setTechRegDetail(techRegDataType.getTechRegDetail());
				techRegRespDto.setErrorId(txtErrorId);
				
				techRegRespDto.setAfid(techRegDataType.getAfid());
				techRegRespDto.setAlmid(techRegDataType.getAlmid());
				techRegRespDto.setAorig(techRegDataType.getAorig());
				 logger.debug("techRegDataType.getCustipaddress()===========>"+techRegDataType.getCustipaddress()+"     ");
				techRegRespDto.setCustipaddress(techRegDataType.getCustipaddress());
				techRegRespDto.setFl(techRegDataType.getFL());
				logger.debug("techRegDataType.getFL()===========>"+techRegDataType.getFL()+"     ");
				techRegRespDto.setFoption(techRegDataType.getFoption());
				 logger.debug("Grtid ID===========>"+techRegDataType.getTechRegId()+"     ");
				techRegRespDto.setGrtid(techRegDataType.getTechRegId());//TechRegId mapped to GRT ID
				
				techRegRespDto.setHwsvr(techRegDataType.getHwsvr());
				techRegRespDto.setInads(techRegDataType.getInads());
				techRegRespDto.setIpadd(techRegDataType.getIpadd());
				techRegRespDto.setIsDOCR(techRegDataType.getIsDOCR());
				techRegRespDto.setMid(techRegDataType.getMid());
				techRegRespDto.setMcode(techRegDataType.getMcode());
				//techRegRespDto.setOalid(techRegDataType.getOalid());//Not available
				 logger.debug("Optype ===========>"+techRegDataType.getOptype()+"     ");
				techRegRespDto.setOptype(techRegDataType.getOptype());
				techRegRespDto.setOssno(techRegDataType.getOssno());
				techRegRespDto.setPrvip(techRegDataType.getPrvip());
				techRegRespDto.setRelno(techRegDataType.getRelno());
				techRegRespDto.setRndpwd(techRegDataType.getRndpwd());
				techRegRespDto.setScode(techRegDataType.getScode());
				techRegRespDto.setSid(techRegDataType.getSid());
				
				techRegRespDto.setVpmsldn(techRegDataType.getVpmsldn());
				techRegRespDto.setSvrName(techRegDataType.getSvrname());
				techRegRespDto.setNickName(techRegDataType.getNickname());
				techRegRespDto.setUserId(techRegDataType.getUsrid());
				
				OutputType outputType =  techRegResponseListType.getRegOutput();
				
				techRegRespDto.setSid(outputType.getSid());
				techRegRespDto.setMid(outputType.getMid());
				techRegRespDto.setInstallScript(outputType.getInstallScript());
				
				if(outputType.getOnBoardingXML() instanceof ElementNSImpl) {
					@SuppressWarnings("restriction")
					ElementNSImpl onBoardingElement = (ElementNSImpl)outputType.getOnBoardingXML();
					if(onBoardingElement != null) {
						techRegRespDto.setOnBoardingXML(onBoardingElement.getFirstChild().getNodeValue());
					}
				}
				
				techRegRespDto.setTransactionDetails(outputType.getTransactionDetails());
				TechRegResultResponseDto techRegResultResponseDto = new TechRegResultResponseDto(); 
				techRegResultResponseDto.setArtid(outputType.getRegResult().getArtid());
				techRegResultResponseDto.setDescription(outputType.getRegResult().getDescription());
				techRegResultResponseDto.setReturnCode(outputType.getRegResult().getReturnCode());
				techRegResultResponseDto.setErrorSubCode(outputType.getRegResult().getReturnSubCode());
				techRegRespDto.getTechRegResult().add(techRegResultResponseDto);
				
				//	Tech Reg Input request
				for(int i=0; i < outputType.getSolutionElements().getNewSEID().size(); i++)
				{
					SolutionElementListDto solutionElementListDto = new SolutionElementListDto();
					solutionElementListDto.setAlarmId(outputType.getSolutionElements().getNewSEID().get(i).getAlarmID());
					solutionElementListDto.setIpAddess(outputType.getSolutionElements().getNewSEID().get(i).getIpAddress());
					solutionElementListDto.setNewSEID(outputType.getSolutionElements().getNewSEID().get(i).getValue());
					solutionElementListDto.setSeCode(outputType.getSolutionElements().getNewSEID().get(i).getSeCode());
					if(outputType.getSolutionElements().getFailedSEID() != null) {
						techRegRespDto.setFailedSeid(outputType.getSolutionElements().getFailedSEID().getValue());
						solutionElementListDto.setFailedRecordNum(outputType.getSolutionElements().getFailedSEID().getRecordNum());
						solutionElementListDto.setFailedSeCode(outputType.getSolutionElements().getFailedSEID().getSeCode());
					}
					logger.debug("SE ID set to DTO for Output::::" + solutionElementListDto.getNewSEID());
					techRegRespDto.getSolutionElementList().add(solutionElementListDto);
				}

				techRegRespDtoList.add(techRegRespDto);
			}
		}
		
		logger.debug("Completed Set message to DTO for Technical Registration... \n" );	
		 logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDtoList()");
		return techRegRespDtoList;
	}
    
    /**
     * method to fetch SR Number
     * @param srRequest
     * @return
     */
    public static String fetchSRNumber(SRRequest srRequest){
    	String activeSR = null;
        if(srRequest != null) {
            if(StringUtils.isNotEmpty(srRequest.getSiebelSRNo())) {
                activeSR = srRequest.getSiebelSRNo();
            } else {
                activeSR = GRTConstants.SIEBEL_SR_CREATION_ERROR;
            }
        }
        return activeSR;
    }
    
    /**
     * method to populate service request DTO
     * @param msgTechRegTypeResponse
     * @param textSystemId
     * @param txtTransactionId
     * @param txtErrorId
     * @return
     */
    public static TechRegResultResponseDto populateServiceRequestDto(TechRegRespType msgTechRegTypeResponse, String textSystemId, 
    																						String txtTransactionId, String txtErrorId) {
		TechRegResultResponseDto techRegRespTypeDto = new TechRegResultResponseDto();

		
		techRegRespTypeDto.setArtid(msgTechRegTypeResponse.getArtid());
		techRegRespTypeDto.setArtsr(msgTechRegTypeResponse.getArtsr());
		techRegRespTypeDto.setGrtid(msgTechRegTypeResponse.getGrtid());
		techRegRespTypeDto.setReturnCode(msgTechRegTypeResponse.getCode());
		techRegRespTypeDto.setDescription(msgTechRegTypeResponse.getDescription());
		logger.debug("ARTID set to DTO::::" + techRegRespTypeDto.getArtid());
		//Set Header 
		
		techRegRespTypeDto.setSystemId(textSystemId);
		techRegRespTypeDto.setGrtid(txtTransactionId);
		techRegRespTypeDto.setErrorId(txtErrorId);
		logger.debug("Completed Set message to DTO for Tech Reg Retry Response... \n" );	
		return techRegRespTypeDto;
	}
    
    /**
     * method to get Date object from date string 
     * @param dateStr
     * @return
     */
    public static Date getDateFromDateStr(String dateStr) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}
    
    /**
     * method to trim text
     * @param text
     * @return
     */
    public static String trimText(String text) {
        if(text != null) {
            return text.trim();
        }
        else return null;
    }
    
    /**
     * construct technical order details from pipelineSapTransactions
     * @param pipelineSapTransactions
     * @return
     */
    public static TechnicalOrderDetail constructTODFromPST(PipelineSapTransactions pipelineSapTransactions){
    	logger.debug("Entering TechnicalRegistrationUtil.constructTODFromPST for MC:"+pipelineSapTransactions.getMaterialCode());
    	TechnicalOrderDetail technicalOrderDetail = new TechnicalOrderDetail();
    	technicalOrderDetail.setMaterialCode(pipelineSapTransactions.getMaterialCode());
    	technicalOrderDetail.setSerialNumber(pipelineSapTransactions.getSerialNumber());
    	technicalOrderDetail.setTechnicallyRegisterable("");
    	technicalOrderDetail.setEquipmentNumber(pipelineSapTransactions.getEquipmentNumber());
    	technicalOrderDetail.setSummaryEquipmentNumber(pipelineSapTransactions.getEquipmentNumber());
    	if(pipelineSapTransactions.getAction() == GRTConstants.TECH_ORDER_TYPE_FV){
    		technicalOrderDetail.setInitialQuantity(0-pipelineSapTransactions.getQuantity());
    	} else {
    		technicalOrderDetail.setInitialQuantity(pipelineSapTransactions.getQuantity());
    	}
    	
		technicalOrderDetail.setProductLine(pipelineSapTransactions.getProductLine());
		
    	logger.debug("Exiting TechnicalRegistrationUtil.constructTODFromPST for MC:"+technicalOrderDetail.getMaterialCode()+"   Qty:"+technicalOrderDetail.getInitialQuantity());
    	return technicalOrderDetail;
    }
    
    /**
     * method to populate ServiceRequestDtoList
     * @param msgTechRegResponse
     * @param txtTransactionId
     * @param txtErrorId
     * @return
     */
    public static List<TechRegDataResponseDto> populateServiceRequestDtoList(ResponseAlarmType msgTechRegResponse, String txtTransactionId, String txtErrorId) {
    	logger.debug("Entering TechnicalRegistrationUtil.populateServiceRequestDtoList");
		 List<TechRegDataResponseDto> techRegRespDtoList = new ArrayList<TechRegDataResponseDto>();           
	
		List<TechRegResponseAlarmListType> techRegResponseList = msgTechRegResponse.getTechRegResponseAlarmList();
		if (techRegResponseList != null && techRegResponseList.size()>0) {
			for (TechRegResponseAlarmListType techRegResponseListType : techRegResponseList) {
				OutputAlarmType techRegDataType = techRegResponseListType.getRegAlarmOutput();
				TechRegDataResponseDto techRegRespDto = new TechRegDataResponseDto();
				
				techRegRespDto.setRegistrationId(txtTransactionId);
				techRegRespDto.setTransactionId(techRegDataType.getTechRegId());
				techRegRespDto.setErrorId(txtErrorId);
				techRegRespDto.setSname(techRegDataType.getSeid());
				techRegRespDto.setTechRegDetail(techRegDataType.getAccessType());
				techRegRespDto.setOnBoardingXML(techRegDataType.getOnBoardingOp());				
				techRegRespDto.setTransactionDetails(techRegDataType.getActivityLog());
				techRegRespDto.setReturnCode(techRegDataType.getReturnCode());
				techRegRespDto.setErrorDesc(techRegDataType.getDescription());
				
				techRegRespDtoList.add(techRegRespDto);
			}
		}
		logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDtoList");
		return techRegRespDtoList;
	}
    
	/**
	 * method to populate ServiceRequestDto
	 * @param msgErrorType
	 * @param txtTransactionId
	 * @param txtErrorId
	 * @return
	 */
	public static ErrorDto populateServiceRequestDto(EquipmentMoveRespType msgErrorType, String txtTransactionId, String txtErrorId) {
		ErrorDto errorTypeDto = new ErrorDto();
		if( (msgErrorType != null) && (msgErrorType.getHeader() != null) ){
			errorTypeDto.setErrorCode(msgErrorType.getHeader().getReturnCode());
			errorTypeDto.setErrorDesc(msgErrorType.getHeader().getErrorDesc());
		}		
		errorTypeDto.setErrorId(txtErrorId);
		errorTypeDto.setRegistrationId(txtTransactionId);
		return errorTypeDto;
	}
	
	/**
	 * method to populate ServiceRequestDto
	 * @param equipmentMoveRespHeader
	 * @param equipmentMoveRespType
	 * @return
	 */
	public static EquipmentMoveDataDTO populateServiceRequestDto(EquipmentMoveRespHeader equipmentMoveRespHeader, EquipmentMoveRespType equipmentMoveRespType) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering TechnicalRegistrationUtil.populateServiceRequestDto to create a Data transfer object from the request object for EQM... ");
		
		EquipmentMoveDataDTO equipmentMoveDataDTO = new EquipmentMoveDataDTO();
    	
		//Set Header values
		EquipmentMoveRespHeader bodyHeader = equipmentMoveRespType.getHeader();
		if (equipmentMoveRespHeader != null) {
			sb.append("Setting the header values if header available in Payload... ");
			equipmentMoveDataDTO.setReturnCode(equipmentMoveRespHeader.getReturnCode());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getReturnCode())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setReturnCode(bodyHeader.getReturnCode());
			}
			
			equipmentMoveDataDTO.setErrorDesc(equipmentMoveRespHeader.getErrorDesc());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getErrorDesc())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setErrorDesc(bodyHeader.getErrorDesc());
			}
			
			equipmentMoveDataDTO.setSystemId(equipmentMoveRespHeader.getSystemId());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getSystemId())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setSystemId(bodyHeader.getSystemId());
			}
			
			equipmentMoveDataDTO.setTransactionId(equipmentMoveRespHeader.getTransactionId());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getTransactionId())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setTransactionId(bodyHeader.getTransactionId());
			}
			
			equipmentMoveDataDTO.setFromSoldToId(equipmentMoveRespHeader.getFromSoldToId());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getFromSoldToId())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setFromSoldToId(bodyHeader.getFromSoldToId());
			}
		
			equipmentMoveDataDTO.setToSoldToId(equipmentMoveRespHeader.getToSoldToId());
			if( (StringUtils.isEmpty(equipmentMoveDataDTO.getToSoldToId())) && (bodyHeader != null) ) {
				equipmentMoveDataDTO.setToSoldToId(bodyHeader.getToSoldToId());
			}
		}
		List<EquipmentMoveRespDataType> equipmentMoveRespDataTypeList= equipmentMoveRespType.getEquipmentMoveRespData();
    	List<EquipmentMoveResponseDto> equipmentMoveResponseDtoList = new ArrayList<EquipmentMoveResponseDto>();
    	
    	EquipmentMoveResponseDto respDto = null;
		for (EquipmentMoveRespDataType equipmentMoveRespDataType:equipmentMoveRespDataTypeList) {
			respDto = new EquipmentMoveResponseDto();
			
			respDto.setReturnCode(equipmentMoveRespDataType.getReturnCode());
			respDto.setErrorDesc(equipmentMoveRespDataType.getErrorDesc());			
			respDto.setFromBeforeQuantity(equipmentMoveRespDataType.getFromBeforeQuantity());
			respDto.setFromAfterQuantity(equipmentMoveRespDataType.getFromAfterQuantity());
			respDto.setFromEquipmentNumber(equipmentMoveRespDataType.getFromequipmentNumber());
			respDto.setToBeforeQuantity(equipmentMoveRespDataType.getToBeforeQuantity());
			respDto.setToAfterQuantity(equipmentMoveRespDataType.getToAfterQuantity());
			respDto.setToEquipmentNumber(equipmentMoveRespDataType.getToequipmentNumber());
			respDto.setMovedQuantity(equipmentMoveRespDataType.getMovedQuantity());
	    	
			equipmentMoveResponseDtoList.add(respDto);
		}
		equipmentMoveDataDTO.setEquipmentMoveResponseData(equipmentMoveResponseDtoList);
		
		sb.append("Exiting TechnicalRegistrationUtil.populateServiceRequestDto after creating a DTO from payload for EQM... ");
		logger.debug("Exiting TechnicalRegistrationUtil.populateServiceRequestDto");
		equipmentMoveDataDTO.setRoutingInfo(sb.toString());
		
		return equipmentMoveDataDTO;
	}
	
	/**
	 * method to create equipment move rsesponse Object 
	 * @param equipmentMoveDataDTO
	 * @return EquipmentMoveResponse
	 * Populate response object for equipment move
	 */
	public static EquipmentMoveResponse createEqpMoveResponseObj(EquipmentMoveDataDTO equipmentMoveDataDTO, EquipmentMoveResponse response){

		if( equipmentMoveDataDTO!=null && response!=null ){
			response.setFromSoldToId(equipmentMoveDataDTO.getFromSoldToId());
			response.setSystemId(equipmentMoveDataDTO.getSystemId());
			response.setToSoldToId(equipmentMoveDataDTO.getToSoldToId());
			response.setTransactionId(equipmentMoveDataDTO.getTransactionId());
		}
		return response;
	}
}
