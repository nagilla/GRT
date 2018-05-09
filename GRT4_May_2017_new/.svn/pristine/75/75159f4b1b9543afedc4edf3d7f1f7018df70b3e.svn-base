package com.grt.integration.jms;
import java.io.ByteArrayInputStream;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.EquipmentRemovalRespType;
import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.grt.service.equipmentremoval.EQRJMSService;
import com.grt.dto.EquipmentRemovalDataDTO;

public class JmsEquipmentRemovalResponseListener extends BaseJmsListener implements MessageListener{
	
	private static final Logger logger = Logger.getLogger(JmsEquipmentRemovalResponseListener.class);
	private String principal;
	private String credential;
	private Context ctx = null;
	private String systemIdentifier;
	private String jmsxDeliveryCount = "1";
	private EQRJMSService eqrJMSService;
	private TechnicalRegistrationUtil technicalRegistrationUtil = new TechnicalRegistrationUtil();
	
	static {
        logger.debug("From Static block of JmsEquipmentRemovalResponseListener");
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
                
                EquipmentRemovalRespType msgEquipmentRemovalResponseType = parseXMLJms(textMsg);
                try {
                	txtTransactionId = msgEquipmentRemovalResponseType.getHeader().getTransactionId();
                } catch (NullPointerException nie) {
        			logger.warn(nie);
        		}
                txtErrorId = message.getStringProperty("ERROR_ID");
                if (textMsg == null || textMsg.equals("")) {
                    String errMsg = "Received NULL or EMPTY text message from JMS. Cannot continue.";
                    logger.error(errMsg);
                    throw new Exception(errMsg);
                }
                logger.debug("Received text message from JMS: \n" + textMsg);                
                logger.debug("Received Header TransactionId, After trimming SystemIdentifier:" + txtTransactionId);             
                logger.debug("Received Header Error Id from JMS:" + txtErrorId);                
                
        		EquipmentRemovalDataDTO equipmentRemovalDataDTO = technicalRegistrationUtil.populateServiceRequestDto
        				(msgEquipmentRemovalResponseType, txtTransactionId, txtErrorId);
        		eqrJMSService.processReceivedEquipmentRemovalReq(equipmentRemovalDataDTO);
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
	private EquipmentRemovalRespType parseXMLJms(String jmsMessage) throws JAXBException {
        logger.debug("Parsing incoming message into Equipment Removal response ...");
		ByteArrayInputStream xmlContentBytes = 
                new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
				com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
                JAXBContext.newInstance("com.avaya.grt.jms.fmw.xml.equipmentRemoval.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		JAXBElement<EquipmentRemovalRespType> equipmentRemovalResponseMessageObj = 
                (JAXBElement<EquipmentRemovalRespType>) unmarshaller.unmarshal(xmlContentBytes);
		EquipmentRemovalRespType equipmentRemovalRespType = equipmentRemovalResponseMessageObj.getValue();
	    logger.debug("Completed.");
		return equipmentRemovalRespType;
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

	public EQRJMSService getEqrJMSService() {
		return eqrJMSService;
	}

	public void setEqrJMSService(EQRJMSService eqrJMSService) {
		this.eqrJMSService = eqrJMSService;
	}
}
