package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.siebel.xml.service20request_2.jms.ListOfAvayaServiceResponse;
import com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage;
import com.avaya.grt.service.srupdate.SRUpdateJMSService;

@WebService(targetNamespace = "http://www.siebel.com/xml/AVAYA%20Service%20Request%20-%20JMS")
public class SRUpdateWS {

	// DI via Spring
	private SRUpdateJMSService srUpdateJMSService;

	@WebMethod(exclude = true)
	public SRUpdateJMSService getSrUpdateJMSService() {
		return srUpdateJMSService;
	}

	@WebMethod(exclude = true)
	public void setSrUpdateJMSService(SRUpdateJMSService srUpdateJMSService) {
		this.srUpdateJMSService = srUpdateJMSService;
	}

	@WebMethod(operationName = "SRUpdate")
	@WebResult(name = "srUpdateCreateResult", targetNamespace = "http://www.siebel.com/xml/AVAYA%20Service%20Request%20-%20JMS")
	public ListOfAvayaServiceResponse srUpdate(
			@WebParam(name = "SiebelMessage", targetNamespace = "http://www.siebel.com/xml/AVAYA%20Service%20Request%20-%20JMS") SiebelMessage siebelMessage) {
		ListOfAvayaServiceResponse srResponseList = srUpdateJMSService
				.srUpdate(siebelMessage);
		return srResponseList;
	}

}