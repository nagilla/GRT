package com.grt.integration.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

/*
 * This simple logical Handler will output the payload of incoming
 * and outgoing messages.
 */

public class GenericWSHandler implements SOAPHandler<SOAPMessageContext> {
    // change this to redirect output if desired
    //private static final PrintStream out = System.out;
    private static final Logger logger = Logger.getLogger(GenericWSHandler.class);


	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return processMessage(context);
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		return processMessage(context);
	}
	
	@Override
	public Set<QName> getHeaders() {	
		return null;	
	}

    public void close(MessageContext context) {
        // Clean up Resources
    }

    private boolean processMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean)
                context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage message = context.getMessage();
        
        if (outboundProperty) {
        	logger.debug("\nOutbound message:");
        } else {
        	logger.debug("\nInbound message:");
        }
        

        // Process Payload Source
        logger.debug(fetchPayload(message));
        // ....

        // If the payload is modified, Do lm.setPayload(source) to be safe,
        // Without it, behavior may vary on the kind of source returned in lm.getPayload().
        // See LogicalMessage JavaDoc for more details.
        // lm.setPayload(modifiedPayload);
        return true;

    }

    private String fetchPayload(SOAPMessage msg) {
        try {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();        	
        	msg.writeTo(baos);        	        	
            return baos.toString(getMessageEncoding(msg));
        } catch (SOAPException e) {
        	logger.error("SOAPEXception : " + e);
        } catch (IOException ie) {
        	logger.error("SOAPEXception : " + ie);
        }
        
        return "";
    }


    private String getMessageEncoding(SOAPMessage msg) throws SOAPException {    	
    	String encoding = "utf-8";
    	
    	if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {    
    		encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();    	
    	}
    	
    	return encoding;
    	
    }
}