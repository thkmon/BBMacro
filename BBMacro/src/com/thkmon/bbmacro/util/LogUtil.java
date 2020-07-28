package com.thkmon.bbmacro.util;

import com.thkmon.bbmacro.common.CommonConst;
import com.thkmon.bblogger.BBLogger;

public class LogUtil {
	
	
	public static BBLogger logger = null;
	
	
	public static void debug(Object obj) {
		String str = "";
		if (obj == null) {
			str = "null";
		} else {
			str = obj.toString();
		}
		
		if (CommonConst.logger != null) {
			CommonConst.logger.debug(str);
		} else {
			System.out.println("[debug]" + str);
		}
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
		
		if (CommonConst.logger != null) {
			CommonConst.logger.error(str);
		} else {
			System.err.println("[error]" + str);
		}
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