package com.grt.util;

public class GrtSiebelException extends Exception {
	public GrtSiebelException(Class sourceClass, String message, Throwable throwableCause) {	
		super(sourceClass.getPackage()+""+sourceClass.getName() + ":"+ message+" had a exception", throwableCause);
	}
	
	public GrtSiebelException(String message, Throwable throwableCause) {
		super(message, throwableCause);
	}

	public GrtSiebelException(String message) {
		super(message);
	}

	public GrtSiebelException(Throwable throwableCause) {
		super(throwableCause);
	}
}
