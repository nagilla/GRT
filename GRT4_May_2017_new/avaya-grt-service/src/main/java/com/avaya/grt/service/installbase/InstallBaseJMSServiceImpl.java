package com.avaya.grt.service.installbase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespHeader;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespType;
import com.avaya.grt.service.TechnicalRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.InstallBaseSapResponseDto;



public class InstallBaseJMSServiceImpl extends TechnicalRegistrationService implements InstallBaseJMSService {
	private static final Logger logger = Logger.getLogger(InstallBaseJMSServiceImpl.class);		
	
	public InstallBaseSapResponseDto installBaseCreate(IBaseCreateRespHeader iBaseCreateRespHeader, IBaseCreateRespType iBaseCreateRespType) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering InstallBaseJMSServiceImpl.installBaseCreate... ");
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if ( (iBaseCreateRespHeader != null) && (StringUtils.isNotEmpty(iBaseCreateRespHeader.getTransactionId())) &&
				(StringUtils.isNotEmpty(iBaseCreateRespHeader.getErrorId()))) {
			sb.append("Inside If - Transaction Id and Error Id is not empty... ");
			txtTransactionId = iBaseCreateRespHeader.getTransactionId();
			txtErrorId = iBaseCreateRespHeader.getErrorId();
		} else if (iBaseCreateRespType.getIBaseheader() != null) {
			sb.append("Inside else If - Transaction Id or Error Id or both are empty... ");
			txtTransactionId = iBaseCreateRespType.getIBaseheader().getRegId();
		}
		
		InstallBaseSapResponseDto installBaseDto = TechnicalRegistrationUtil.populateServiceRequestDto(iBaseCreateRespType, txtTransactionId, txtErrorId);
		sb.append(installBaseDto.getRoutingInfo());
		installBaseDto.setRoutingInfo(sb.toString());
		//InstallBaseSapResponseDto response = null;
		try {
			installBaseDto = processReceivedInstallBase(installBaseDto);
		} catch (Throwable e) {
			installBaseDto.setErrorId("ERROR : " + e.hashCode() + "   " + e.toString());
		}
		return installBaseDto;
	}
	
	public InstallBaseSapResponseDto installBaseCreateRetry(IBaseCreateRespHeader iBaseCreateRespHeader, IBaseCreateRespType iBaseCreateRespType) {
		String txtTransactionId = "";
		String txtErrorId = "";
		
		if (iBaseCreateRespHeader != null) {
			txtTransactionId = iBaseCreateRespHeader.getTransactionId();
			txtErrorId = iBaseCreateRespHeader.getErrorId();
		} else if (iBaseCreateRespType.getIBaseheader() != null) {
			txtTransactionId = iBaseCreateRespType.getIBaseheader().getRegId();
		}
		InstallBaseSapResponseDto installBaseDto = TechnicalRegistrationUtil.populateServiceRequestDto(iBaseCreateRespType, txtTransactionId, txtErrorId);	
		//TODO : Check what needs to be done for retry
		installBaseDto.setErrorId("SUCCESS");
		return installBaseDto;
	}
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		InstallBaseJMSService installBaseService = (InstallBaseJMSService) context.getBean("installBaseJMSService");
		
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
		
		
		 String registrationId="6879538";
		 SiteRegistration siteRegistration=new SiteRegistration();
		 try {
			 siteRegistration = installBaseService.getSiteRegistrationOnRegId(registrationId);
			 System.out.println("Site Registration Sold To:" +siteRegistration.getSoldToId());
			 
			 String productTypeCode="DEFTY";
			 String aorig = installBaseService.getAorig(productTypeCode,"");
		     System.out.println("Art Registration Type is " + aorig);
		     
		     List<TechnicalOrderDetail> list =  installBaseService.getTechnicalOrderByType(registrationId,"IB");
		     System.out.println("List size is " + list);
		 } catch (DataAccessException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 
		 */
		
		 
		BufferedReader br = null;
		StringBuffer strBuf = new StringBuffer(); 
		try {
			
			final InputStream inputStream = InstallBaseJMSServiceImpl.class.getResourceAsStream("/IB_StandAlone.xml");
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while (bufferedReader.ready()) {
            	strBuf.append(bufferedReader.readLine());
            }
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		String textMsg = strBuf.toString();		
		IBaseCreateRespType msgFilePoller = parseXMLJms(textMsg); 
		
		String txtTransactionId = "6879686";
		String txtErrorId= "0";
		InstallBaseSapResponseDto installBaseDto = TechnicalRegistrationUtil.populateServiceRequestDto(msgFilePoller, txtTransactionId, txtErrorId);
		
		try {
			installBaseService.processReceivedInstallBase(installBaseDto);
			
			System.out.println("working");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static IBaseCreateRespType parseXMLJms(String jmsMessage) throws JAXBException {
      logger.debug("Parsing incoming message into IBase Creation Request ...");
		ByteArrayInputStream xmlContentBytes = 
              new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
              com.avaya.grt.jms.fmw.xml.filePoller.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
              JAXBContext.newInstance("com.avaya.grt.jms.fmw.xml.filePoller.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		JAXBElement<IBaseCreateRespType> iBaseMessageObj = 
              (JAXBElement<IBaseCreateRespType>) unmarshaller.unmarshal(xmlContentBytes);
		IBaseCreateRespType iBaseRespMessage = iBaseMessageObj.getValue();
	    logger.debug("Completed.");
		return iBaseRespMessage;
	}


}
