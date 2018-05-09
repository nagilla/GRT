package com.avaya.grt.service.error;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.service.TechnicalRegistrationErrorService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.ErrorDto;



public class ErrorJMSServiceImpl extends TechnicalRegistrationErrorService implements ErrorJMSService {
	private static final Logger logger = Logger.getLogger(ErrorJMSServiceImpl.class);		
	
	public ErrorDto processErrorIB(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId) {
			logger.debug("Entering ErrorJMSServiceImpl.processErrorIB ");
		ErrorDto errorDto = populateServiceRequestDto(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		try {
			processErrorIB(errorDto);
			errorDto.setErrorDesc("SUCCESS");
		}  catch (Throwable t) {
			errorDto.setErrorDesc(t.getMessage());
		}
		logger.debug("Exiting ErrorJMSServiceImpl.processErrorIB ");
		return errorDto;
	}
	
	public ErrorDto processErrorEQR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId) {
		logger.debug("Entering ErrorJMSServiceImpl.processErrorEQR ");
		ErrorDto errorDto = populateServiceRequestDto(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		try {
			processErrorTR(errorDto);
			errorDto.setErrorDesc("SUCCESS");
		}  catch (Throwable t) {
			errorDto.setErrorDesc(t.getMessage());
		}
		logger.debug("Exiting ErrorJMSServiceImpl.processErrorEQR ");
		return errorDto;
	}
	
	public ErrorDto processErrorTR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId) {
		logger.debug("Entering ErrorJMSServiceImpl.processErrorTR ");
		ErrorDto errorDto = populateServiceRequestDto(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		try {
			processErrorTR(errorDto);
			errorDto.setErrorDesc("SUCCESS");
		}  catch (Throwable t) {
			errorDto.setErrorDesc(t.getMessage());
		}
		logger.debug("Exiting ErrorJMSServiceImpl.processErrorTR ");
		return errorDto;
	}
	
	public ErrorDto processErrorAlarmTR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId) {
		logger.debug("Entering ErrorJMSServiceImpl.processErrorAlarmTR ");
		ErrorDto errorDto = populateServiceRequestDto(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		try {
			processErrorTR(errorDto);
			errorDto.setErrorDesc("SUCCESS");
		}  catch (Throwable t) {
			errorDto.setErrorDesc(t.getMessage());
		}
		logger.debug("Exiting ErrorJMSServiceImpl.processErrorAlarmTR ");
		return errorDto;
	}
	
	private ErrorDto populateServiceRequestDto(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId) {
		logger.debug("Entering ErrorJMSServiceImpl.populateServiceRequestDto ");
		ErrorDto errorTypeDto = new ErrorDto();
		
		errorTypeDto.setErrorCode(errCode);
		errorTypeDto.setErrorDesc(errDesc);
		errorTypeDto.setErrorId(txtErrorId);
		errorTypeDto.setRegistrationId(txtTransactionId);
		errorTypeDto.setTransactionType(txtTransactionType);
		logger.debug("Exiting ErrorJMSServiceImpl.populateServiceRequestDto ");
		return errorTypeDto;
	}
	
	
	public static void main(String[] args) throws Exception {		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
		/*ErrorJMSService errorJMSService = (ErrorJMSService) context.getBean("errorJMSService");
		ErrorMessageHeader errorMessageHeader =new ErrorMessageHeader();
		errorMessageHeader.setErrorId("1");
		errorMessageHeader.setFileName("error.txt");
		errorMessageHeader.setTransactionId("3010460");
		errorMessageHeader.setTransactionType("TR");
		ErrorMessageType errorMessageType = new ErrorMessageType();
		errorMessageType.setCode("100");
		errorMessageType.setDescription("Dummy");
		try {
			errorJMSService.processError(errorMessageHeader, errorMessageType);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		TechnicalRegistrationErrorService technicalRegistrationErrorService = (TechnicalRegistrationErrorService) context.getBean("technicalRegistrationErrorService");
		ErrorDto err=new ErrorDto();
		err.setErrorCode("1");
		err.setRegistrationId("3010460");
		err.setTransactionType("TR");
		err.setErrorDesc("Testing");
		
		try {
			technicalRegistrationErrorService.processErrorIB(err);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
