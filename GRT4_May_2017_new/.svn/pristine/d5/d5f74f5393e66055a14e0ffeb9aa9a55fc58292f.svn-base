package com.avaya.grt.service.equipmentmove;

import java.util.List;

import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveRespType;
import com.avaya.grt.jms.fmw.xml.equipmentMove.jms.EquipmentMoveResponse;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.EquipmentMoveDataDTO;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface EQMJMSService {
	public EquipmentMoveResponse processReceivedEquipmentMoveReq(EquipmentMoveDataDTO equipmentMoveDataDTO) throws Throwable;
	public EquipmentMoveResponse equipmentMove(EquipmentMoveRespHeader equipmentMoveRespHeader, EquipmentMoveRespType equipmentMoveRespType);
	public EquipmentMoveDataDTO equipmentMoveRetry(EquipmentMoveRespHeader equipmentMoveRespHeader, EquipmentMoveRespType equipmentMoveRespType);
	public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	public String getAorig(String productCode, String connectivity) throws DataAccessException;
	public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType) throws DataAccessException;
}
