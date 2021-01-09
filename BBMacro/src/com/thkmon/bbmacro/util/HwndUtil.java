package com.thkmon.bbmacro.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public class HwndUtil {

	public static String getHandleText(HWND hWnd) {
		char[] windowText = new char[512];
		User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
		String wText = Native.toString(windowText);
		return wText;
	}

	public static String getHandleClassName(HWND hWnd) {
		// 핸들의 클래스 네임 얻기
		char[] c = new char[512];
		User32.INSTANCE.GetClassName(hWnd, c, 512);
		String clsName = String.valueOf(c).trim();
		return clsName;
	}

	public static void setFocusHandle(HWND hWnd) {
		// 최소화 되어있을 경우 복원
		if (isMinimizedHandle(hWnd)) {
			User32.INSTANCE.ShowWindow(hWnd, 9);
		}
		
		User32.INSTANCE.SetForegroundWindow(hWnd);
	}
	
	/**
	 * 최소화되어 있는 창인지 검사한다. rectangle.left값이 -32000일 경우 최소화되어 있는 창이다.
	 * 
	 * @param hWnd
	 */
	public static boolean isMinimizedHandle(HWND hWnd) {
		if (hWnd == null) {
			return false;
		}
		
		RECT rectangle = new RECT();
		User32.INSTANCE.GetWindowRect(hWnd, rectangle);
		if (rectangle.left <= -32000) {
			return true;
		}
		
		return false;
	}
}