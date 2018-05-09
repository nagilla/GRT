package com.avaya.grt.service.equipmentremoval;

import java.util.List;

import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespType;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.EquipmentRemovalDataDTO;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface EQRJMSService {
	public EquipmentRemovalDataDTO processReceivedEquipmentRemovalReq(EquipmentRemovalDataDTO equipmentRemovalDataDTO) throws Throwable;
	public EquipmentRemovalDataDTO equipmentRemove(EquipmentRemovalRespHeader equipmentRemovalRespHeader, EquipmentRemovalRespType equipmentRemovalRespType);
	public EquipmentRemovalDataDTO equipmentRemoveRetry(EquipmentRemovalRespHeader equipmentRemovalRespHeader, EquipmentRemovalRespType equipmentRemovalRespType);
	public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	public String getAorig(String productCode, String connectivity) throws DataAccessException;
	public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType) throws DataAccessException;
}
