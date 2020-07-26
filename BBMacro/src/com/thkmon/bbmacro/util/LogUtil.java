package com.thkmon.bbmacro.util;

public class LogUtil {
	
	public static void debug(Object obj) {
		String str = "";
		if (obj == null) {
			str = "null";
		} else {
			str = obj.toString();
		}
		
		System.out.println("DEBUG : " + str);
	}
	
	
	public static void debug(Exception e) {
		String str = getExceptionString(e);
		if (str != null && str.length() > 0) {
			debug(str);
		}
	}
	
	
	public static void error(Object obj) {
		String str = "";
		if (obj == null) {
			str = "null";
		} else {
			str = obj.toString();
		}
		
		System.err.println("ERROR : " + str);
	}
	
	
	public static void error(Exception e) {
		String str = getExceptionString(e);
		if (str != null && str.length() > 0) {
			error(str);
		}
	}
	
	
	private static String getExceptionString(Exception e) {
		if (e == null) {
			return "";
		}
		
		StringBuffer buff = new StringBuffer();
		
		StackTraceElement[] elems = e.getStackTrace();
		if (elems != null && elems.length > 0) {
			int count = elems.length;
			for (int i=0; i<count; i++) {
				if (buff.length() > 0) {
					buff.append("\n");
				}
				
				buff.append(elems[i].toString());
			}
		}
		
		return buff.toString();
	}
}