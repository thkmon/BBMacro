package com.thkmon.bbmacro.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

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

	public static void setFocusHandle(HWND hwnd) {
		// 최소화 되어있을 경우 복원
		User32.INSTANCE.ShowWindow(hwnd, 9);
		
		User32.INSTANCE.SetForegroundWindow(hwnd);
	}
}