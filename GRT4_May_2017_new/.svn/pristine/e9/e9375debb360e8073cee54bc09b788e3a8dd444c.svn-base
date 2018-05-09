package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveResponse;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.grt.service.equipmentmove.EQMJMSService;
import com.grt.dto.EquipmentMoveDataDTO;
import com.grt.dto.ErrorDto;

@WebService(targetNamespace = "http://avaya.com/v4/equipmentmove")
public class EquipmentMoveWS {
	
	//DI via Spring
	private EQMJMSService eqmJMSService;
	
	@WebMethod(exclude=true)
	public EQMJMSService getEqmJMSService() {
		return eqmJMSService;
	}

	@WebMethod(exclude=true)
	public void setEqmJMSService(EQMJMSService eqmJMSService) {
		this.eqmJMSService = eqmJMSService;
	}

	@WebMethod(operationName="AsynchResponse")
	@WebResult(name="AsynchResponseResult", targetNamespace = "http://avaya.com/v4/equipmentmove")
	public EquipmentMoveResponse asynchEQMResponse(@WebParam(name="EqrHeader", header= true, targetNamespace = "http://avaya.com/v4/equipmentmove") 
															EquipmentMoveRespHeader equipmentMoveRespHeader,
												   @WebParam(name="EqrResponse", targetNamespace = "http://avaya.com/v4/equipmentmove") 
															EquipmentMoveRespType equipmentMoveRespType) {
		EquipmentMoveResponse equipmentMoveResponse = eqmJMSService.equipmentMove(equipmentMoveRespHeader, equipmentMoveRespType);		
		//Populate the response based upon the dto
		return equipmentMoveResponse;
	}
	
	@WebMethod(operationName="RetryRequestConfirmation")
	@WebResult(name="RetryRequestConfirmationResult", targetNamespace = "http://avaya.com/v4/equipmentmove")
	public EquipmentMoveResponse retryRequestEQMConfirmation(@WebParam(name="EqrHeader", header= true, targetNamespace = "http://avaya.com/v4/equipmentmove") 
																		EquipmentMoveRespHeader equipmentMoveRespHeader, 
																 @WebParam(name="EqrResponse", targetNamespace = "http://avaya.com/v4/equipmentmove") 
																		EquipmentMoveRespType equipmentMoveRespType) {
		EquipmentMoveDataDTO equipmentMoveDataDTO = eqmJMSService.equipmentMoveRetry(equipmentMoveRespHeader, equipmentMoveRespType);	
		EquipmentMoveResponse equipmentMoveResponse = new EquipmentMoveResponse(); 
		//Populate the response based upon the dto
		return equipmentMoveResponse;
	}
	
	@WebMethod(operationName="RetryError")
	@WebResult(name="RetryErrorResult", targetNamespace = "http://avaya.com/v4/equipmentmove")
	public EquipmentMoveResponse retryEQMError(@WebParam(name="ErrorHeader", header= true, targetNamespace = "http://avaya.com/v4/equipmentmove") 
														EquipmentMoveRespHeader equipmentMoveRespHeader, 
												   @WebParam(name="ErrorMessage", targetNamespace = "http://avaya.com/v4/equipmentmove") 
														EquipmentMoveRespType equipmentMoveRespType) {
		String txtTransactionId = equipmentMoveRespHeader.getTransactionId();
		String txtErrorId = equipmentMoveRespHeader.getReturnCode();
		ErrorDto errorDto = TechnicalRegistrationUtil.populateServiceRequestDto(equipmentMoveRespType, txtTransactionId, txtErrorId);		
		EquipmentMoveResponse equipmentMoveResponse = new EquipmentMoveResponse(); 
		//Populate the response based upon the dto
		return equipmentMoveResponse;
	}
	
	
	
}