package com.grt.integration.ws;



import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.avaya.v2.techregistration.ResponseType;
import com.avaya.grt.jms.avaya.v2.techregistration.TRResponseResultType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegErrorRespType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespHeader;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespType;
import com.avaya.grt.service.error.ErrorJMSService;
import com.avaya.grt.service.technicalonboarding.TechnicalOnBoardingJMSService;
import com.grt.dto.ErrorDto;


@WebService(targetNamespace = "http://avaya.com/v2/techregistration")
public class TechnicalRegistrationWS {
	
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
	@WebResult(name="TechRegResultResponse", targetNamespace = "http://avaya.com/v2/techregistration")
	public TRResponseResultType asynchTRResponse(@WebParam(name="TechRegHeader", header= true, targetNamespace = "http://avaya.com/v2/techregistration") 
													TechRegRespHeader techRegRespHeader,
												 @WebParam(name="TechRegResultRequest", targetNamespace = "http://avaya.com/v2/techregistration") 
													ResponseType responseType) {
		TRResponseResultType techRegResponse = techRegJMSService.processReceivedTechReg(techRegRespHeader,responseType);
		return techRegResponse;
	}
	
	@WebMethod(operationName="RetryRequestConfirmation")
	@WebResult(name="TechRegResponse", targetNamespace = "http://avaya.com/v2/techregistration")
	public TRResponseResultType retryRequestTRConfirmation(@WebParam(name="TechRegHeader", header= true, targetNamespace = "http://avaya.com/v2/techregistration") 
																TechRegRespHeader techRegRespHeader, 
														   @WebParam(name="TechRegRequest", targetNamespace = "http://avaya.com/v2/techregistration") 
																TechRegRespType techRegRespType) {
		//TODO : Check what needs to be done for retry operation
		TRResponseResultType techRegResponse = new TRResponseResultType(); 
		return techRegResponse;
	}
	
	@WebMethod(operationName="RetryError")
	@WebResult(name="TechRegResponse", targetNamespace = "http://avaya.com/v2/techregistration")
	public TRResponseResultType retryTRError(@WebParam(name="ErrorHeader", header= true, targetNamespace = "http://avaya.com/v2/techregistration") 
													TechRegRespHeader techRegRespHeader, 
											 @WebParam(name="ErrorMessage", targetNamespace = "http://avaya.com/v2/techregistration") 
													TechRegErrorRespType techRegErrorRespType) {
		String errCode = techRegErrorRespType.getCode();
		String errDesc = techRegErrorRespType.getDescription();
		String txtTransactionId = techRegRespHeader.getTransactionId();
		String txtTransactionType = null; 
		String txtErrorId = techRegRespHeader.getErrorId();
		ErrorDto errorDto = errorJMSService.processErrorAlarmTR(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		TRResponseResultType techRegResponse = new TRResponseResultType(); 
		techRegResponse.setCode(errorDto.getErrorCode());
		techRegResponse.setDescription(errorDto.getErrorDesc());
		return techRegResponse;
	}
	
}