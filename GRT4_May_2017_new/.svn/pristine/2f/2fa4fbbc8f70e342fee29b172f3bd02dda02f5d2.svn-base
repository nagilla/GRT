package com.avaya.grt.service.srupdate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avaya.grt.jms.siebel.xml.service20request_2.jms.ListOfAvayaServiceResponse;
import com.avaya.grt.jms.siebel.xml.service20request_2.jms.ServiceRequest;
import com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage;
import com.avaya.grt.service.TechnicalRegistrationSRService;

//import com.grt.dto.ServiceRequest;

public class SRUpdateJMSServiceImpl extends TechnicalRegistrationSRService
		implements SRUpdateJMSService {
	private static final Logger logger = Logger
			.getLogger(SRUpdateJMSServiceImpl.class);

	public ListOfAvayaServiceResponse srUpdate(SiebelMessage siebelMessage) {
		logger.debug("Entering into the srUpdate to create SR...");

		List<com.grt.dto.ServiceRequest> serviceRequestDtoList = null;

		// creating response objects for the final response
		ListOfAvayaServiceResponse listOfAvayaServiceResponse = new ListOfAvayaServiceResponse();

		com.grt.dto.ServiceRequest srRespObj = new com.grt.dto.ServiceRequest();
		List<com.grt.dto.ServiceRequest> srResponceList = new ArrayList<com.grt.dto.ServiceRequest>();

		try {
			serviceRequestDtoList = populateServiceRequestDto(siebelMessage);
		} catch (Exception e) {
			logger.debug("not able to populate DTO...");
		}
		for (com.grt.dto.ServiceRequest serviceRequest : serviceRequestDtoList) {
			try {
				srRespObj = processReceivedSiebelSR(serviceRequest);
				srResponceList.add(srRespObj);
			} catch (Throwable e) {
				serviceRequest.setDescription(e.toString());
			}
		}
		listOfAvayaServiceResponse.setServiceRequest(srResponceList);
		return listOfAvayaServiceResponse;
	}

	private List<com.grt.dto.ServiceRequest> populateServiceRequestDto(
			SiebelMessage siebelMessage) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering into SRUpdateJMSServiceImpl.populateServiceRequestDto...");
		List<com.grt.dto.ServiceRequest> srDtoList = new ArrayList<com.grt.dto.ServiceRequest>();
		com.grt.dto.ServiceRequest serviceRequestDto = null;
		sb.append("Populating data object for the list of service requests...");
		List<ServiceRequest> serviceReqList = siebelMessage
				.getListOfAvayaServiceRequestJms().getServiceRequest();
		for (ServiceRequest sr : serviceReqList) {
			serviceRequestDto = new com.grt.dto.ServiceRequest();
			serviceRequestDto.setSrNumber(sr.getSRNumber().trim());
			serviceRequestDto.setSrId(sr.getId().trim());
			serviceRequestDto.setSrType(sr.getSRType().trim());
			serviceRequestDto.setSrSubType(sr.getAVAYASRSubtypeDetail().trim());
			serviceRequestDto.setStatus(sr.getStatus().trim());
			serviceRequestDto.setSubStatus(sr.getSubStatus().trim());
			serviceRequestDto.setResolutionAction(sr.getResolutionAction());
			serviceRequestDto.setRoutingInfo(sb.toString());
			srDtoList.add(serviceRequestDto);
			logger.debug("populateServiceRequestDto --> Resolution Action:"
					+ serviceRequestDto.getResolutionAction());
		}
		sb.append("Exiting from SRUpdateJMSServiceImpl.populateServiceRequestDto...");

		return srDtoList;
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * ApplicationContext context = new
	 * ClassPathXmlApplicationContext("bean.xml"); SRUpdateJMSService
	 * srUpdateJMSService = (SRUpdateJMSService)
	 * context.getBean("srUpdateJMSService");
	 * 
	 * ServiceRequest serviceRequestDto=new ServiceRequest();
	 * serviceRequestDto.setContactEmail("dvadakkath@avaya.com"); try {
	 * srUpdateJMSService.processReceivedSiebelSR(serviceRequestDto); } catch
	 * (Throwable e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 */

}
