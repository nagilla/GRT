package com.grt.util;

/**
 * Custom exception to handle the data base.
 * 
 * @author Perficient
 * 
 */
public class DataAccessException extends Exception {
	
	public DataAccessException(Class sourceClass, String message, Throwable throwableCause) {	
		super(sourceClass.getPackage()+""+sourceClass.getPackage() + ":"+ message, throwableCause);
	}
	
	public DataAccessException(String message, Throwable throwableCause) {
		super(message, throwableCause);
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable throwableCause) {
		super(throwableCause);
	}
}
