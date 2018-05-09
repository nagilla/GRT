package com.avaya.grt.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class LoggerUtil {
	
	Logger logger = Logger.getLogger(LoggerUtil.class);
	
	private DateFormat formatter = new SimpleDateFormat("MMddyyyy HH:mm:ss");

	public void logMessageToFile(String message) {
		
		if(message != null && !message.equals("")) {
			if(message.indexOf("###") != -1) {
				String msgCodeWithModule = message.substring(0, message.indexOf("###"));
				String msgString = message.substring(message.indexOf("###") + 3);
				String msgType = getMsgType(msgCodeWithModule);
				if(!msgType.equals("")) {
					
					String formattedStr = getFormattedMessage(msgType, msgCodeWithModule, msgString);
					
					if(msgType.equals("ERROR")) {
						logger.error(formattedStr);
					} else if(msgType.equals("WARN")) {
						logger.warn(formattedStr);
					} else if(msgType.equals("INFO")) {
						logger.info(formattedStr);
					} else {
						logger.debug(formattedStr);
					}
				}
			} else {
				logger.debug(message);
			}
		}
	}
	
	public String getFormattedMessage(String msgType, String msgCode, String message) {
		
		String formattedString = "";
		String refNo = "";
		String module = "";
		
		if(msgCode != null && !msgCode.equals("")) {			
			if(msgCode.indexOf("_") != -1) {
				refNo = msgCode.substring(0, msgCode.indexOf("_"));
				module = msgCode.substring(msgCode.indexOf("_") + 1);
			}
		}
		formattedString = msgType + " - " + refNo + " : " + formatter.format(new Date()) + " : " + module + " : " + message;
		return formattedString;		
	}
	
	private String getMsgType(String msgCode) {
		
		String msgType = "";
		if(msgCode != null && !msgCode.equals("")) {
			if(msgCode.startsWith("ERR")) {
				msgType = "ERROR";
			} else if(msgCode.startsWith("WRN")) {
				msgType = "WARN";
			} else if(msgCode.startsWith("INF")) {
				msgType = "INFO";
			} else {
				msgType = "DEBUG";
			}
		}
		return msgType;
	}
}
