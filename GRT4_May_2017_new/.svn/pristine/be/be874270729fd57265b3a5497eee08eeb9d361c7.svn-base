package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.avaya.v4.techregistration.ResponseAlarmType;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegErrorRespType;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegRespHeader;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegResponse;
import com.avaya.grt.service.error.ErrorJMSService;
import com.avaya.grt.service.technicalonboarding.TechnicalOnBoardingJMSService;
import com.grt.dto.ErrorDto;

@WebService(targetNamespace = "http://avaya.com/v4/techregistration")
public class TechnicalRegistrationAlarmWS {
	
	//DI via Spring
	private TechnicalOnBoardingJMSService techRegJMSService;
	
	//DI via Spring
	private ErrorJMSService errorJMSService;

	@WebMethod(exclude=true)
	public TechnicalOnBoardingJMSService getTechRegJMSService() {
		return techRegJMSService;
	}

	@WebMethod(exclude=true)
	public void setTechRegJMSService(TechnicalOnBoardingJMSService techRegJMSService) {
		this.techRegJMSService = techRegJMSService;
	}
	
	@WebMethod(exclude=true)
	public ErrorJMSService getErrorJMSService() {
		return errorJMSService;
	}

	@WebMethod(exclude=true)
	public void setErrorJMSService(ErrorJMSService errorJMSService) {
		this.errorJMSService = errorJMSService;
	}
	
	
	@WebMethod(operationName="AsynchResponse")
	@WebResult(name="TechRegResponse", targetNamespace = "http://avaya.com/v4/techregistration")
	public TechRegResponse asynchTRAlarmResponse(@WebParam(name="TechRegHeader", header= true, targetNamespace = "http://avaya.com/v4/techregistration") 
													TechRegRespHeader techRegRespHeader,
												 @WebParam(name="TechRegRequest", targetNamespace = "http://avaya.com/v4/techregistration") 
													ResponseAlarmType responseAlarmType) {
		TechRegResponse techRegResponse = techRegJMSService.processReceivedAlarmTechReg(techRegRespHeader, responseAlarmType); 
		return techRegResponse;
	}
	
	@WebMethod(operationName="RetryRequestConfirmation")
	@WebResult(name="TechRegResponse", targetNamespace = "http://avaya.com/v4/techregistration")
	public TechRegResponse retryRequestTRAlarmConfirmation(@WebParam(name="TechRegHeader", header= true, targetNamespace = "http://avaya.com/v4/techregistration") 
															  TechRegRespHeader techRegRespHeader, 
														   @WebParam(name="TechRegRequest", targetNamespace = "http://avaya.com/v4/techregistration") 
															  ResponseAlarmType responseAlarmType) {
		//TODO : Check what needs to be done for retry operation	
		TechRegResponse techRegResponse = new TechRegResponse(); 
		return techRegResponse;
	}
	
	@WebMethod(operationName="RetryError")
	@WebResult(name="TechRegResponse", targetNamespace = "http://avaya.com/v4/techregistration")
	public TechRegResponse retryTRAlarmError(@WebParam(name="TechRegHeader", header= true, targetNamespace = "http://avaya.com/v4/techregistration") 
												TechRegRespHeader techRegRespHeader, 
							                 @WebParam(name="TechRegRequest", targetNamespace = "http://avaya.com/v4/techregistration") 
												TechRegErrorRespType techRegErrorRespType) {
		String errCode = techRegErrorRespType.getCode();
		String errDesc = techRegErrorRespType.getDescription();
		String txtTransactionId = techRegRespHeader.getTransactionId();
		String txtTransactionType = null; 
		String txtErrorId = techRegRespHeader.getErrorId();
		ErrorDto errorDto = errorJMSService.processErrorAlarmTR(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		TechRegResponse techRegResponse = new TechRegResponse();
		techRegResponse.setErrorId(errorDto.getErrorCode());
		techRegResponse.setErrorDesc(errorDto.getErrorDesc());
		return techRegResponse;
		
	}
	
	
	
}