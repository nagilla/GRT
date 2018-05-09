package com.grt.integration.jms;
import java.util.Enumeration;

import javax.jms.Message;
import javax.naming.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.grt.util.GRTConstants;

public class BaseJmsListener {
	private static final Logger logger = Logger.getLogger(JmsFilePollerListener.class);
	private String principal;
	private String credential;
	private Context ctx = null;
	private String systemIdentifier;
	private String jmsxDeliveryCount = "1";
	
	public static String printCustomJMSHeaders(Message message, Logger logger) {
		String JMSXDELIVERYCOUNT = "1";
		try {
			if(message != null) {
				Enumeration headers = message.getPropertyNames();
				if(headers != null){
					logger.debug("Starting header values for JMSMessageID:" + message.getJMSMessageID());
					while(headers.hasMoreElements()) {
						String header = (String) headers.nextElement();
						if(StringUtils.isNotEmpty(header)) {
							String headerValue = message.getStringProperty(header);
							if(header.equalsIgnoreCase(GRTConstants.JMSXDELIVERYCOUNT)) {
								JMSXDELIVERYCOUNT = headerValue;
							}
							logger.debug(header + ">" + headerValue + "<");
						}
					}
					logger.debug("Completed header values for JMSMessageID:" + message.getJMSMessageID());
				}
			}
		} catch(Throwable throwable){
			logger.error("Error while iterating JMS Headers", throwable);
		}
		return JMSXDELIVERYCOUNT;
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
}
