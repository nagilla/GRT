package com.avaya.grt.web.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.avaya.grt.web.util.LoggerUtil;

public class AvayaCommonUtilAction {

	private Logger logger = Logger.getLogger(AvayaCommonUtilAction.class);
	
	private String logMessage;
	private String searchUrl;
	private String jsonString;
	private String avayaSearchRedirectUrl;
		
	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	
    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
	
	public String getAvayaSearchRedirectUrl() {
		return avayaSearchRedirectUrl;
	}

	public void setAvayaSearchRedirectUrl(String avayaSearchRedirectUrl) {
		this.avayaSearchRedirectUrl = avayaSearchRedirectUrl;
	}
	
	public void addAlertLogs() throws Exception {
		if(logMessage != null && !logMessage.equals("")) {
			LoggerUtil logUtil = new LoggerUtil();
			logUtil.logMessageToFile(logMessage);
		}
	}
	
	public void avayaSearchList() {
		try {
			URL oracle = new URL(getSearchUrl());
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			jsonString = "";
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				jsonString += readLine;
			}
			in.close();
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.print(jsonString);
			setJsonString(jsonString);

		} catch (Exception e) {
			logger.error("Error in getting serch results.. "+e);
		}
	}
	
    public String redirectToAvayaSearch() {
    	logger.debug("URL Received to redirect: "+avayaSearchRedirectUrl);
    	ServletActionContext.getRequest().getSession().setAttribute("avayaSearchRedirectUrl", avayaSearchRedirectUrl);
    	return "success";
    }
}
