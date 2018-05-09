package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalErrorRespType;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespType;
import com.avaya.grt.service.equipmentremoval.EQRJMSService;
import com.avaya.grt.service.error.ErrorJMSService;
import com.grt.dto.EquipmentRemovalDataDTO;
import com.grt.dto.ErrorDto;

@WebService(targetNamespace = "http://avaya.com/v1/equipmentremoval")
public class EquipmentRemovalWS {

	//DI via Spring
	private EQRJMSService eqrJMSService;
	
	//DI via Spring
	private ErrorJMSService errorJMSService;

	@WebMethod(exclude=true)
	public EQRJMSService getEqrJMSService() {
		return eqrJMSService;
	}

	@WebMethod(exclude=true)
	public void setEqrJMSService(EQRJMSService eqrJMSService) {
		this.eqrJMSService = eqrJMSService;
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
	@WebResult(name="AsynchResponseResult", targetNamespace = "http://avaya.com/v1/equipmentremoval")
	public EquipmentRemovalDataDTO asynchEQRResponse(@WebParam(name="EqrHeader", header= true, targetNamespace = "http://avaya.com/v1/equipmentremoval") 
																				EquipmentRemovalRespHeader equipmentRemovalRespHeader, 
												     @WebParam(name="EqrResponse", targetNamespace = "http://avaya.com/v1/equipmentremoval") 
																				EquipmentRemovalRespType equipmentRemovalRespType) {
		EquipmentRemovalDataDTO equipmentRemovalDataDTO = eqrJMSService.equipmentRemove(equipmentRemovalRespHeader, equipmentRemovalRespType);		
		return equipmentRemovalDataDTO;
	}
		
	@WebMethod(operationName="RetryRequestConfirmation")
	@WebResult(name="RetryRequestConfirmationResult", targetNamespace = "http://avaya.com/v1/equipmentremoval")
	public EquipmentRemovalDataDTO retryRequestEQRConfirmation(@WebParam(name="EqrHeader", header= true, targetNamespace = "http://avaya.com/v1/equipmentremoval") 
																				EquipmentRemovalRespHeader equipmentRemovalRespHeader, 
															   @WebParam(name="EqrResponse", targetNamespace = "http://avaya.com/v1/equipmentremoval") 
																				EquipmentRemovalRespType equipmentRemovalRespType) {
		EquipmentRemovalDataDTO equipmentRemovalDataDTO = eqrJMSService.equipmentRemoveRetry(equipmentRemovalRespHeader, equipmentRemovalRespType);		
		return equipmentRemovalDataDTO;
	}
	
	@WebMethod(operationName="RetryError")
	@WebResult(name="RetryErrorResult", targetNamespace = "http://avaya.com/v1/equipmentremoval")
	public ErrorDto retryEQRError(@WebParam(name="ErrorHeader", header= true, targetNamespace = "http://avaya.com/v1/equipmentremoval") 
												EquipmentRemovalRespHeader equipmentRemovalRespHeader, 
							      @WebParam(name="ErrorMessage", targetNamespace = "http://avaya.com/v1/equipmentremoval") 
												EquipmentRemovalErrorRespType equipmentRemovalErrorRespType) {
		String errCode = equipmentRemovalErrorRespType.getCode();
		String errDesc = equipmentRemovalErrorRespType.getDescription();
		String txtTransactionId = equipmentRemovalRespHeader.getTransactionId();
		String txtTransactionType = null; 
		String txtErrorId = equipmentRemovalRespHeader.getErrorId();
		
		ErrorDto errorDto = errorJMSService.processErrorEQR(errCode, errDesc, txtTransactionId, txtTransactionType, txtErrorId);		
		return errorDto;
	}
	
}