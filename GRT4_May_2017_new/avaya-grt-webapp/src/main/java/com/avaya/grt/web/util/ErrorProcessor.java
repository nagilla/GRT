package com.avaya.grt.web.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class ErrorProcessor extends ActionSupport {

	private static final Logger LOGGER = Logger.getLogger(ErrorProcessor.class);

	private static final long serialVersionUID = 1L;
	private Exception exception;
	private String stackTrace;

	public String execute() {
		if (exception == null) {
			LOGGER.error("Exception caught but was null. How does that make sense?");
		} else {
			LOGGER.error(new Date(), exception);
			stackTrace = getExceptionStackTraceAsString(exception);
		}
		return "error";
	}

	private static String getExceptionStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
