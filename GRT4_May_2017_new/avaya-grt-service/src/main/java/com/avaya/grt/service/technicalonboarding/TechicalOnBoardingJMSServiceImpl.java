package com.avaya.grt.service.technicalonboarding;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.jms.avaya.v2.techregistration.ResponseType;
import com.avaya.grt.jms.avaya.v2.techregistration.TRResponseResultType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespHeader;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespType;
import com.avaya.grt.jms.avaya.v4.techregistration.ResponseAlarmType;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegResponse;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.service.TechnicalRegistrationAlarmAsyncService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.TechRegDataResponseDto;
import com.grt.dto.TechRegResultResponseDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;



public class TechicalOnBoardingJMSServiceImpl extends TechnicalRegistrationAlarmAsyncService implements TechnicalOnBoardingJMSService {
	private static final Logger logger = Logger.getLogger(TechicalOnBoardingJMSServiceImpl.class);		
	
	public TRResponseResultType processReceivedTechReg(TechRegRespHeader techRegRespHeader, ResponseType responseType) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering into TechicalOnBoardingJMSServiceImpl.processReceivedTechReg... ");
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if ( (techRegRespHeader != null) && (StringUtils.isNotEmpty(techRegRespHeader.getTransactionId())) &&
					(StringUtils.isNotEmpty(techRegRespHeader.getErrorId())))
		{
			txtTransactionId = techRegRespHeader.getTransactionId();
			txtErrorId = techRegRespHeader.getErrorId();
			sb.append("Inside If TransactionId- "+txtTransactionId+", ErrorId-"+txtErrorId+"... ");
		} else {
			txtTransactionId = responseType.getTransactionId();
			txtErrorId = responseType.getErrorId();
			sb.append("Inside else TransactionId- "+txtTransactionId+", ErrorId-"+txtErrorId+"... ");
		}
		sb.append("Going to prepare Tech Reg DTO List... ");
		List<TechRegDataResponseDto> techRegResponseDtoList = TechnicalRegistrationUtil.populateServiceRequestDtoList(responseType,txtTransactionId,txtErrorId);
		sb.append("Tech Reg DTO List prepared, size - "+techRegResponseDtoList.size()+" ... ");
		TRResponseResultType techRegResponse = new TRResponseResultType();
		try {
			techRegResponse = this.processReceivedTechRegReqNew(techRegResponseDtoList);
			sb.append(techRegResponse.getRoutingInfo());
			techRegResponse.setRoutingInfo(sb.toString());
			if(StringUtils.isEmpty(techRegResponse.getCode()) && StringUtils.isEmpty(techRegResponse.getDescription())) {
				techRegResponse.setCode("SUCCESS");
				techRegResponse.setDescription("SUCCESS");
			}
		} catch (Throwable e) {
			techRegResponseDtoList.get(0).setErrorId(e.toString());
			techRegResponse.setCode("FAILURE");
			//techRegResponse.setDesc("ERROR : " + e.hashCode() + "   " + e.toString());
		}
		
		return techRegResponse;
	}	
	
	public TechRegResultResponseDto processReceivedTechRegRetry(TechRegRespHeader techRegRespHeader, TechRegRespType techRegRespType) {
		String txtTransactionId = "";
		String txtErrorId = "";
		String textSystemId = "";
		
		if (techRegRespHeader != null) {
			txtTransactionId = techRegRespHeader.getTransactionId();
			txtErrorId = techRegRespHeader.getErrorId();
			textSystemId = techRegRespHeader.getSystemId();
		}
		
		TechRegResultResponseDto techRegResultResponseDto = 
				TechnicalRegistrationUtil.populateServiceRequestDto(techRegRespType,textSystemId,txtTransactionId,txtErrorId);
		try {
			this.processReceivedTechRegRetryReq(techRegResultResponseDto);
		} catch (Throwable e) {
			techRegResultResponseDto.setErrorId(e.toString());
		}
		
		return techRegResultResponseDto;
	}
	
	public TechRegResponse processReceivedAlarmTechReg(com.avaya.grt.jms.avaya.v4.techregistration.TechRegRespHeader techRegRespHeader, 
																ResponseAlarmType responseAlarmType) {
		TechRegResponse techRegResponse = new TechRegResponse();
		StringBuffer sb = new StringBuffer();
		sb.append("Entering into TechicalOnBoardingJMSServiceImpl.processReceivedAlarmTechReg... ");
		
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if ( (techRegRespHeader != null) && (StringUtils.isNotEmpty(techRegRespHeader.getTransactionId())) &&
				(StringUtils.isNotEmpty(techRegRespHeader.getErrorId()))	
		   ) {
			txtTransactionId = techRegRespHeader.getTransactionId();
			txtErrorId = techRegRespHeader.getErrorId();
			sb.append("Inside If TransactionId- "+txtTransactionId+", ErrorId-"+txtErrorId+"... ");
		} else {
			techRegResponse.setErrorId("FAILURE");
			techRegResponse.setErrorDesc("ERROR : Header data is incorrect");
			sb.append("Inside else TransactionId- "+txtTransactionId+", ErrorId-"+txtErrorId+"... ");
		}
		sb.append("Going to prepare Tech Reg DTO List... ");
		List<TechRegDataResponseDto> techRegResponseDtoList = TechnicalRegistrationUtil.populateServiceRequestDtoList(responseAlarmType,txtTransactionId,txtErrorId);	
		sb.append("Tech Reg DTO List prepared, size - "+techRegResponseDtoList.size()+" ... ");
		
		//TechRegResponse response = new TechRegResponse();
		try {
			techRegResponse = this.processReceivedAlarmTechReg(techRegResponseDtoList);
			sb.append(techRegResponse.getRoutingInfo());
			techRegResponse.setRoutingInfo(sb.toString());
			if(StringUtils.isEmpty(techRegResponse.getErrorId()) && StringUtils.isEmpty(techRegResponse.getErrorDesc())) {
				techRegResponse.setErrorId("SUCCESS");
				techRegResponse.setErrorDesc("SUCCESS");
			}
		}catch (Throwable e) {
			techRegResponseDtoList.get(0).setErrorId(e.toString());
			/*techRegResponse.setErrorId("FAILURE");
			techRegResponse.setErrorDesc("ERROR : " + e.hashCode() + "   " + e.toString());*/
		}
		return techRegResponse;
	}
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");	
		TechnicalOnBoardingJMSService techRegJMSService = (TechnicalOnBoardingJMSService) context.getBean("techRegJMSService");
		
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
			 siteRegistration = techRegJMSService.getSiteRegistrationOnRegId(registrationId);
			 System.out.println("Site Registration Sold To:" +siteRegistration.getSoldToId());
			 
			 String productTypeCode="DEFTY";
			 String aorig = techRegJMSService.getAorig(productTypeCode,"");
		     System.out.println("Art Registration Type is " + aorig);
		     
		     List<TechnicalOrderDetail> list =  techRegJMSService.getTechnicalOrderByType(registrationId,"IB");
		     System.out.println("List size is " + list);
		 } catch (DataAccessException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		
	}

}
