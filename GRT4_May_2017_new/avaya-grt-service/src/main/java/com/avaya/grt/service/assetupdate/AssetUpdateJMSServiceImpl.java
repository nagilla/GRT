package com.avaya.grt.service.assetupdate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.service.TechnicalRegistrationService;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.grt.dto.InstallBaseAsset;



public class AssetUpdateJMSServiceImpl extends TechnicalRegistrationService implements AssetUpdateJMSService {
	private static final Logger logger = Logger.getLogger(AssetUpdateJMSServiceImpl.class);		
	
	public InstallBaseAsset assetUpdate(SiebelMessage msgAssetUpdate) {
		StringBuffer sb = new StringBuffer();
		sb.append("Entering AssetUpdateJMSServiceImpl.assetUpdate... ");
		//Add the below to soap header
		String txtTransactionId = "";
		InstallBaseAsset installBaseAssetDto = TechnicalRegistrationUtil.populateServiceRequestDto(msgAssetUpdate, txtTransactionId);
		sb.append(installBaseAssetDto.getRoutingInfo());
		installBaseAssetDto.setRoutingInfo(sb.toString());
		
		try {
			installBaseAssetDto = processReceivedSiebelAsset(installBaseAssetDto);
		} catch (Throwable e) {
			installBaseAssetDto.getInstallBaseAssetData().get(0).setAssetNumber(e.toString());
		}
		
		return installBaseAssetDto;
	}
	
	
	public static void main(String[] args) throws Exception {		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		AssetUpdateJMSService assetUpdateJMSService = (AssetUpdateJMSService) context.getBean("assetUpdateJMSService");
		
		BufferedReader br = null;
		StringBuffer strBuf = new StringBuffer(); 
		try {
			
			final InputStream inputStream = AssetUpdateJMSServiceImpl.class.getResourceAsStream("/AssetUpdate_1.xml");
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
		
		try {
			SiebelMessage msgAssetUpdate = parseXMLJms(textMsg); 
			assetUpdateJMSService.assetUpdate(msgAssetUpdate);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		/*
		BaseRegistrationService baseRegistrationService = (BaseRegistrationService) context.getBean("baseRegistrationService");
		String registrationId = "6949414";
		baseRegistrationService.sendRegistrationRequestAlert(registrationId, ProcessStepEnum.INSTALL_BASE_CREATION, StatusEnum.COMPLETED);
		*/
	}

	@SuppressWarnings("unchecked")
	private static SiebelMessage parseXMLJms(String jmsMessage) throws JAXBException {
		logger.debug("Parsing incoming message into Siebel Service Request ...");
		ByteArrayInputStream xmlContentBytes = 
            new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
				com.avaya.grt.jms.siebel.xml.assetupdate.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
            JAXBContext.newInstance("com.avaya.grt.jms.siebel.xml.assetupdate.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		Object obj = unmarshaller.unmarshal(xmlContentBytes);
		logger.debug("<><><><><><><><><><><><><><><><><><>" + obj.getClass().getName());
		SiebelMessage msgAssetUpdate = (SiebelMessage) obj;
		logger.debug("Parsing Completed...");
		return msgAssetUpdate;
	}
	
}
