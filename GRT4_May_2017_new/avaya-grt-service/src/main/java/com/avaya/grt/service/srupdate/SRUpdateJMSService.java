package com.avaya.grt.service.srupdate;

import com.avaya.grt.jms.siebel.xml.service20request_2.jms.ListOfAvayaServiceResponse;
import com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage;
import com.grt.dto.ServiceRequest;


public interface SRUpdateJMSService {
	public ServiceRequest processReceivedSiebelSR(ServiceRequest serviceRequestDto) throws Throwable;	
	public ListOfAvayaServiceResponse srUpdate(SiebelMessage siebelMessage);
}
