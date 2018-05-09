package com.avaya.grt.service.equipmentremoval;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespHeader;
import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespType;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.service.TechnicalRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.EquipmentRemovalDataDTO;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;



public class EQRJMSServiceImpl extends TechnicalRegistrationService implements EQRJMSService {
	private static final Logger logger = Logger.getLogger(EQRJMSServiceImpl.class);		
	
	public EquipmentRemovalDataDTO equipmentRemove(EquipmentRemovalRespHeader equipmentRemovalRespHeader, EquipmentRemovalRespType equipmentRemovalRespType) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering EQRJMSServiceImpl.equipmentRemove... ");
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if ( (equipmentRemovalRespHeader != null) && (StringUtils.isNotEmpty(equipmentRemovalRespHeader.getTransactionId())) &&
				(StringUtils.isNotEmpty(equipmentRemovalRespHeader.getErrorId()))) {
			sb.append("Inside If - Transaction Id and Error Id is not empty... ");
			txtTransactionId = equipmentRemovalRespHeader.getTransactionId();
			txtErrorId = equipmentRemovalRespHeader.getErrorId();
		} else if (equipmentRemovalRespType.getHeader() != null) {
			sb.append("Inside else If - Transaction Id or Error Id or both are empty... ");
			txtTransactionId = equipmentRemovalRespType.getHeader().getTransactionId();
		}
		
		EquipmentRemovalDataDTO equipmentRemovalDataDTO = TechnicalRegistrationUtil.populateServiceRequestDto(equipmentRemovalRespType, txtTransactionId, txtErrorId);
		sb.append(equipmentRemovalDataDTO.getRoutingInfo().toString());
		equipmentRemovalDataDTO.setRoutingInfo(sb.toString());
		try {
			equipmentRemovalDataDTO = processReceivedEquipmentRemovalReq(equipmentRemovalDataDTO);
			equipmentRemovalDataDTO.setErrorId("SUCCESS");
		} catch (Throwable e) {
			equipmentRemovalDataDTO.setErrorId("ERROR : " + e.hashCode() + "   " + e.toString() + " "+e.getStackTrace().toString());
		}
		return equipmentRemovalDataDTO;
	}
	
	public EquipmentRemovalDataDTO equipmentRemoveRetry(EquipmentRemovalRespHeader equipmentRemovalRespHeader, EquipmentRemovalRespType equipmentRemovalRespType) {
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if (equipmentRemovalRespHeader != null) {
			txtTransactionId = equipmentRemovalRespHeader.getTransactionId();
			txtErrorId = equipmentRemovalRespHeader.getErrorId();
		} else if (equipmentRemovalRespType.getHeader() != null) {
			txtTransactionId = equipmentRemovalRespType.getHeader().getTransactionId();
		}
		
		EquipmentRemovalDataDTO equipmentRemovalDataDTO = TechnicalRegistrationUtil.populateServiceRequestDto(equipmentRemovalRespType, txtTransactionId, txtErrorId);
		//TODO : Check what needs to be done for retry
		equipmentRemovalDataDTO.setErrorId("SUCCESS");
		return equipmentRemovalDataDTO;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");	
		EQRJMSService eqrJMSService = (EQRJMSService) context.getBean("eqrJMSService");
		
		/*
		InstallBaseSapResponseDto installBaseSapResponseDto =new InstallBaseSapResponseDto();
		InstallBaseRespDataDto iBase=new InstallBaseRespDataDto();
		
		iBase.setMaterialCode("307176");
		iBase.setQuantity("2");
		iBase.setSoldToId("0002964964");
		iBase.setSerialNumber("");
		iBase.setShipToId("");
		List<InstallBaseRespDataDto> iBaseRespData = new ArrayList<InstallBaseRespDataDto>();
	    iBaseRespData.add(iBase);
		installBaseSapResponseDto.setRegistrationId("6869488");
		installBaseSapResponseDto.setIBaseRespData(iBaseRespData);
		
		try {
			installBaseService.processReceivedInstallBase(installBaseSapResponseDto);
			
			System.out.println("working");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		 String registrationId="6879538";
		 SiteRegistration siteRegistration=new SiteRegistration();
		 try {
			 siteRegistration = eqrJMSService.getSiteRegistrationOnRegId(registrationId);
			 System.out.println("Site Registration Sold To:" +siteRegistration.getSoldToId());
			 
			 String productTypeCode="DEFTY";
			 String aorig = eqrJMSService.getAorig(productTypeCode,"");
		     System.out.println("Art Registration Type is " + aorig);
		     
		     List<TechnicalOrderDetail> list =  eqrJMSService.getTechnicalOrderByType(registrationId,"IB");
		     System.out.println("List size is " + list);
		 } catch (DataAccessException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		
	}

}
