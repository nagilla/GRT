package com.avaya.grt.service.equipmentmove;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveResponse;
import com.avaya.grt.service.TechnicalRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.EquipmentMoveDataDTO;



public class EQMJMSServiceImpl extends TechnicalRegistrationService implements EQMJMSService {
	private static final Logger logger = Logger.getLogger(EQMJMSServiceImpl.class);		
	
	public EquipmentMoveResponse equipmentMove(EquipmentMoveRespHeader equipmentMoveRespHeader, EquipmentMoveRespType equipmentMoveRespType) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering EQMJMSServiceImpl.equipmentMove... ");
		
		EquipmentMoveDataDTO equipmentMoveDataDTO = TechnicalRegistrationUtil.populateServiceRequestDto(equipmentMoveRespHeader, equipmentMoveRespType);
		sb.append(equipmentMoveDataDTO.getRoutingInfo().toString());
		equipmentMoveDataDTO.setRoutingInfo(sb.toString());
		EquipmentMoveResponse response = null;
		try {
			response = processReceivedEquipmentMoveReq(equipmentMoveDataDTO);
			//Update the response object from other information e.g. to-soldto, fromSoldTo
			response = TechnicalRegistrationUtil.createEqpMoveResponseObj(equipmentMoveDataDTO,response);
			equipmentMoveDataDTO.setErrorDesc("SUCCESS");
		} catch (Throwable e) {
			equipmentMoveDataDTO.setErrorDesc("ERROR : " + e.hashCode() + "   " + e.toString());
		}
		return response;
	}
	
	public EquipmentMoveDataDTO equipmentMoveRetry(EquipmentMoveRespHeader equipmentMoveRespHeader, EquipmentMoveRespType equipmentMoveRespType) {
		EquipmentMoveDataDTO equipmentMoveDataDTO = TechnicalRegistrationUtil.populateServiceRequestDto(equipmentMoveRespHeader, equipmentMoveRespType);
		
		return equipmentMoveDataDTO;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");	
		EQMJMSService eqrJMSService = (EQMJMSService) context.getBean("eqmJMSService");
		
	}

}
