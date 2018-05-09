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

import com.avaya.grt.service.TechnicalRegistrationUtil;
import com.avaya.grt.service.assetupdate.AssetUpdateJMSService;
import com.avaya.grt.service.srupdate.SRUpdateJMSService;
import com.grt.dto.ServiceRequest;
import com.grt.util.GRTConstants;

public class JmsSRUpdateListener extends BaseJmsListener implements MessageListener{
	
	private static final Logger logger = Logger.getLogger(JmsSRUpdateListener.class);
	private String principal;
	private String credential;
	private Context ctx = null;
	private String systemIdentifier;
	private String jmsxDeliveryCount = "1";
	private SRUpdateJMSService srUpdateJMSService;
	private TechnicalRegistrationUtil technicalRegistrationUtil = new TechnicalRegistrationUtil();
	
	
	static {
        logger.debug("From Static block of JmsSRUpdateListener");
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
                
                if (textMsg == null || textMsg.equals("")) {
                    String errMsg = "Received NULL or EMPTY text message from JMS. Cannot continue.";
                    logger.error(errMsg);
                    throw new Exception(errMsg);
                }
                logger.debug("Received text message from JMS: \n" + textMsg);                
                logger.debug("Received Header TransactionId, After trimming SystemIdentifier:" + txtTransactionId);             
                logger.debug("Received Header Error Id from JMS:" + txtErrorId);
                
                com.avaya.grt.jms.siebel.xml.service20request_2.jms.ServiceRequest msgServiceRequest = parseXMLJms(textMsg);                
        		String srType = technicalRegistrationUtil.trimText(msgServiceRequest.getSRType());
                String srSubType = technicalRegistrationUtil.trimText(msgServiceRequest.getAVAYASRSubtypeDetail());
        		if (isGRTMessage(srType, srSubType)) {
                    ServiceRequest serviceRequestDto = 
                            populateServiceRequestDto(msgServiceRequest);
                    txtTransactionId = serviceRequestDto.getSrNumber();
                    logger.debug("Processing incoming message to GRT back-end service for the following: \n"
                            + "SR Number [" + serviceRequestDto.getSrNumber() + "]\n"
                            + "SR Status [" + serviceRequestDto.getStatus() + "]\n"
                            + "Resolution Action [" + serviceRequestDto.getResolutionAction() + "]");  
                    getSrUpdateJMSService().processReceivedSiebelSR(serviceRequestDto);
        		} 
             
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
	
	
	private ServiceRequest populateServiceRequestDto(com.avaya.grt.jms.siebel.xml.service20request_2.jms.ServiceRequest msgServiceRequest) {
		ServiceRequest serviceRequestDto = new ServiceRequest();
		serviceRequestDto.setSrNumber(msgServiceRequest.getSRNumber().trim());
		serviceRequestDto.setSrType(msgServiceRequest.getSRType().trim());
		serviceRequestDto.setSrSubType(msgServiceRequest.getAVAYASRSubtypeDetail().trim());
		serviceRequestDto.setStatus(msgServiceRequest.getStatus().trim());
		serviceRequestDto.setSubStatus(msgServiceRequest.getSubStatus().trim());
		serviceRequestDto.setResolutionAction(msgServiceRequest.getResolutionAction());
		logger.debug("populateServiceRequestDto --> Resolution Action:"+serviceRequestDto.getResolutionAction());
		return serviceRequestDto;
	}
	
	private boolean isGRTMessage(String srType, String srSubType) {
        logger.debug("Starting with incoming message srType [" + srType + "] and srSubType [" + srSubType + "]");
        boolean isIt = false;
        if (srType == null || srType.equals("")||!srType.equals(GRTConstants.SIEBEL_SR_TYPE_INTERNAL)) {
            logger.debug("Completed. Returning [" + isIt + "]");
            return isIt;
        }
        if (srSubType != null) {
            if (srSubType.equals(GRTConstants.INSTALL_BASE_CREATION)
                    || srSubType.equals(GRTConstants.TECHNICAL_REGISTRATION)
                    || srSubType.equals(GRTConstants.SAL_RECORDS_BUILDING)
                    || srSubType.equals(GRTConstants.SAL_CONNECTIVITY_AND_ALARM_TESTING)
                    || srSubType.equals(GRTConstants.FINAL_RECORD_VALIDATION)) {
                isIt = true;
            }
        }
        logger.debug("Completed. Returning [" + isIt + "], and the srSubType value is ["+srSubType+"]");
        return isIt;
    }
	
	@SuppressWarnings("unchecked")
	private com.avaya.grt.jms.siebel.xml.service20request_2.jms.ServiceRequest parseXMLJms(String jmsMessage) throws JAXBException {
        logger.debug("Parsing incoming message into Siebel Service Request ...");
		ByteArrayInputStream xmlContentBytes = 
                new ByteArrayInputStream(jmsMessage.getBytes());
		ClassLoader classLoader = 
				com.avaya.grt.jms.siebel.xml.service20request_2.jms.ObjectFactory.class.getClassLoader();
		JAXBContext jaxbContext = 
                JAXBContext.newInstance("com.avaya.grt.jms.siebel.xml.service20request_2.jms", classLoader);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		JAXBElement<com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage> siebelMessageObj = 
                (JAXBElement<com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage>) unmarshaller.unmarshal(xmlContentBytes);
		com.avaya.grt.jms.siebel.xml.service20request_2.jms.SiebelMessage siebelMessage = siebelMessageObj.getValue();
		com.avaya.grt.jms.siebel.xml.service20request_2.jms.ServiceRequest msgServiceRequest = 
                siebelMessage.getListOfAvayaServiceRequestJms().getServiceRequest().get(0);
        logger.debug("Completed.");
		return msgServiceRequest;
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

	public SRUpdateJMSService getSrUpdateJMSService() {
		return srUpdateJMSService;
	}

	public void setSrUpdateJMSService(SRUpdateJMSService srUpdateJMSService) {
		this.srUpdateJMSService = srUpdateJMSService;
	}
}
