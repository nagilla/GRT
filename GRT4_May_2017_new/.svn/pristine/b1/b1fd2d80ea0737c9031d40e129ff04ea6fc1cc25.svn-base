package com.grt.util;

public class GrtSapException extends Exception {
		
	private static final long serialVersionUID = 1L;

		public GrtSapException(Class sourceClass, String message, Throwable throwableCause) {	
			super(sourceClass.getPackage()+""+sourceClass.getName() + ":"+ message+" had a exception", throwableCause);
		}
		
		public GrtSapException(String message, Throwable throwableCause) {
			super(message, throwableCause);
		}

		public GrtSapException(String message) {
			super(message);
		}

		public GrtSapException(Throwable throwableCause) {
			super(throwableCause);
		}
}

