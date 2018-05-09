package com.grt.integration.jms;
import java.io.ByteArrayInputStream;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.grt.service.assetupdate.AssetUpdateJMSService;
import com.avaya.grt.service.installbase.InstallBaseJMSService;
import com.grt.dto.InstallBaseAsset;

public class JmsAssetUpdateListener extends BaseJmsListener implements MessageListener{
	
	private static final Logger logger = Logger.getLogger(JmsAssetUpdateListener.class);
	private String principal;
	private String credential;
	private Context ctx = null;
	private String systemIdentifier;
	private String jmsxDeliveryCount = "1";
	private AssetUpdateJMSService assetUpdateJMSService;
	private TechnicalRegistrationUtil technicalRegistrationUtil = new TechnicalRegistrationUtil();
	
	static {
        logger.debug("From Static block of JmsAssetUpdateListener");
    }
	
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		logger.debug("inside onMessage");
        if(message == null) {
            String errMsg = "Received NULL message from JMS. Cannot continue.";
            logger.error(errMsg);
        } else if(!(message instanceof TextMessage)) {
            String errMsg = "Received invalid message - not a type of TextMessage. Cannot continue.";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        } else {
        	jmsxDeliveryCount = printCustomJMSHeaders(message, logger);
            String textMsg = null;
            String txtTransactionId = null;
            String txtErrorId= null;
            try {
                textMsg = ((TextMessage)message).getText();
                txtTransactionId = message.getStringProperty("RegistrationId");
                
                if (textMsg == null || textMsg.equals("")) {
                    String errMsg = "Received NULL or EMPTY text message from JMS. Cannot continue.";
                    logger.error(errMsg);
                    throw new Exception(errMsg);
                }
                logger.debug("Received text message from JMS: \n" + textMsg);                
                logger.debug("Received Header TransactionId, After trimming SystemIdentifier:" + txtTransactionId);             
                logger.debug("Received Header Error Id from JMS:" + txtErrorId);
                
                SiebelMessage msgAssetUpdate = parseXMLJms(textMsg);                 
                InstallBaseAsset installBaseAssetDto = technicalRegistrationUtil.populateServiceRequestDto(msgAssetUpdate, txtTransactionId);
                assetUpdateJMSService.processReceivedSiebelAsset(installBaseAssetDto);
            } catch(NameNotFoundException nameNotFoundException) {
                logger.error("TRACKER[ ID:" + txtTransactionId + ":DeliveryCount:" + jmsxDeliveryCount +":NameNotFoundException]", nameNotFoundException);
                throw new RuntimeException(nameNotFoundException);
            } catch(JAXBException jaxbe) {
                logger.error("TRACKER[ ID:" + txtTransactionId + ":DeliveryCount:" + jmsxDeliveryCount +":JAXBException]", jaxbe);
                throw new RuntimeException(jaxbe);
            } catch (SecurityException  securityException) {
                logger.error("TRACKER[ ID:" + txtTransactionId  + ":DeliveryCount:" + jmsxDeliveryCount +":SecurityException:" + this.principal + "/" + this.credential + ":" + securityException.getMessage() + "]", securityException);
                throw new RuntimeException(securityException);
            } catch (AuthenticationException authenticationException) {
            	logger.error("TRACKER[ ID:" + txtTransactionId + ":DeliveryCount:" + jmsxDeliveryCount +":AuthenticationException:" + this.principal + "/" + this.credential + ":" + authenticationException.getMessage() + "]", authenticationException);
                throw new RuntimeException(authenticationException);
            } catch (Throwable throwable) {
                logger.error("TRACKER[ ID:" + txtTransactionId  + ":DeliveryCount:" + jmsxDeliveryCount +":" + throwable.getMessage() + "]", throwable);
            } finally  {
            	logger.debug("MESSAGE PROCESSED FOR ID:" + txtTransactionId);
            	if(ctx != null) {
            		try {
            			ctx.close();
            		} catch(Throwable throwable){
            			logger.warn("Error while closing the Context", throwable);
            		}
            	}
            	logger.debug("Completed OnMessage");
            }
        }
    }
	
	@SuppressWarnings("unchecked")
	private SiebelMessage parseXMLJms(String jmsMessage) throws JAXBException {
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

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getSystemIdentifier() {
		return systemIdentifier;
	}

	public void setSystemIdentifier(String systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}

	public String getJmsxDeliveryCount() {
		return jmsxDeliveryCount;
	}

	public void setJmsxDeliveryCount(String jmsxDeliveryCount) {
		this.jmsxDeliveryCount = jmsxDeliveryCount;
	}

	public AssetUpdateJMSService getAssetUpdateJMSService() {
		return assetUpdateJMSService;
	}

	public void setAssetUpdateJMSService(
			AssetUpdateJMSService assetUpdateJMSService) {
		this.assetUpdateJMSService = assetUpdateJMSService;
	}
}
