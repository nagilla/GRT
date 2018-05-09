package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateErrorRespType;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespHeader;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespType;
import com.avaya.grt.service.error.ErrorJMSService;
import com.avaya.grt.service.installbase.InstallBaseJMSService;
import com.grt.dto.ErrorDto;
import com.grt.dto.InstallBaseSapResponseDto;

@WebService(targetNamespace = "http://avaya.com/v1/installbase")
public class InstallBaseWS {

	//DI via Spring
	private InstallBaseJMSService installBaseJMSService;

	//DI via Spring
	private ErrorJMSService errorJMSService;
	
	@WebMethod(exclude=true)
	public InstallBaseJMSService getInstallBaseJMSService() {
		return installBaseJMSService;
	}

	@WebMethod(exclude=true)
	public void setInstallBaseJMSService(InstallBaseJMSService installBaseJMSService) {
		this.installBaseJMSService = installBaseJMSService;
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
	@WebResult(name="AsynchResponseResult", targetNamespace = "http://avaya.com/v1/installbase")
	public InstallBaseSapResponseDto asynchIBResponse(@WebParam(name="iBaseHeader", header= true, targetNamespace = "http://avaya.com/v1/installbase") 
																				IBaseCreateRespHeader iBaseCreateRespHeader, 
													  @WebParam(name="iBaseResponse", targetNamespace = "http://avaya.com/v1/installbase") 
																			    IBaseCreateRespType iBaseCreateRespType) {
		InstallBaseSapResponseDto installBaseSapResponseDto = installBaseJMSService.installBaseCreate(iBaseCreateRespHeader, iBaseCreateRespType);		
		return installBaseSapResponseDto;
	}
	
	
	@WebMethod(operationName="RetryRequestConfirmation")
	@WebResult(name="RetryRequestConfirmationResult", targetNamespace = "http://avaya.com/v1/installbase")
	public InstallBaseSapResponseDto retryRequestIBConfirmation(@WebParam(name="iBaseHeader", header= true, targetNamespace = "http://avaya.com/v1/installbase") 
																				IBaseCreateRespHeader iBaseCreateRespHeader, 
															    @WebParam(name="iBaseResponse", targetNamespace = "http://avaya.com/v1/installbase") 
																			    IBaseCreateRespType iBaseCreateRespType) {
		InstallBaseSapResponseDto installBaseSapResponseDto = installBaseJMSService.installBaseCreateRetry(iBaseCreateRespHeader, iBaseCreateRespType);		
		return installBaseSapResponseDto;
	}
	
	@WebMethod(operationName="RetryError")
	@WebResult(name="RetryErrorResult", targetNamespace = "http://avaya.com/v1/installbase")
	public ErrorDto retryIBError(@WebParam(name="ErrorHeader", header= true, targetNamespace = "http://avaya.com/v1/installbase") 
												IBaseCreateRespHeader iBaseCreateRespHeader, 
							     @WebParam(name="ErrorMessage", targetNamespace = "http://avaya.com/v1/installbase") 
												IBaseCreateErrorRespType iBaseCreateErrorRespType) {		
		String errCode = iBaseCreateErrorRespType.getCode();
		String errDesc = iBaseCreateErrorRespType.getDescription();
		String txtTransactionId = iBaseCreateRespHeader.getTransactionId();
		String txtTransactionType = null; 
		String txtErrorId = iBaseCreateRespHeader.getErrorId();
		
		ErrorDto errorDto = errorJMSService.processErrorIB(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		return errorDto;
	}
	
}