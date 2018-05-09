package com.grt.util;

public class SkipDateTimeEscalator {
	public static final ThreadLocal skipDateTimeEscalation = new ThreadLocal();

	public static void set(Boolean predicate) {
		skipDateTimeEscalation.set(predicate);
	}

	public static void unset() {
		skipDateTimeEscalation.remove();
	}

	public static Boolean get() {
		Object obj = skipDateTimeEscalation.get();
		if(obj != null){
			return (Boolean)obj;
		}
		return false;
	}
}
