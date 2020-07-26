package com.thkmon.bbmacro.handle;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class HandleFinder {
	
	private HWND handle = null;
	
	
	public HWND getWindowHandle(String nameOrClass) {
		handle = null;
		
		setWindowHandle(nameOrClass, true);
		if (handle != null) {
			return handle;
		}
		
		setWindowHandle(nameOrClass, false);
		if (handle != null) {
			return handle;
		}
		
		return null;
	}
	
	
	private void setWindowHandle(final String nameOrClass, final boolean bEquals) {
		
		try {
			User32.INSTANCE.EnumWindows(new WNDENUMPROC() {
				public boolean callback(HWND hWnd, Pointer arg1) {
					char[] windowText = new char[512];
					User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
					String wText = Native.toString(windowText);
					
					RECT rectangle = new RECT();
					User32.INSTANCE.GetWindowRect(hWnd, rectangle);
					
					// 숨겨져 있는 창은 제외하고 찾는다. 최소화 되어있는 창은 포함한다.
					// rectangle.left값이 -32000일 경우 최소화되어 있는 창이다.
					// if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hWnd) && rectangle.left > -32000)) {
					if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hWnd))) {
						return true;
					}

					// 핸들의 클래스 네임 얻기
					char[] c = new char[512];
					User32.INSTANCE.GetClassName(hWnd, c, 512);
					String clsName = String.valueOf(c).trim();

					// int count = 0;
					// System.out.println(
					// 		// "hwnd:"+hWnd+","+
					// 		"번호:" + (++count) + ",텍스트:" + wText + "," + "위치:(" + rectangle.left + "," + rectangle.top
					// 				+ ")~(" + rectangle.right + "," + rectangle.bottom + ")," + "클래스네임:" + clsName);
					
					if (bEquals) {
						if (clsName != null && clsName.equals(nameOrClass)) {
							handle = hWnd;
						}
						
						if (wText != null && wText.equals(nameOrClass)) {
							handle = hWnd;
						}
					} else {
						if (clsName != null && clsName.indexOf(nameOrClass) > -1) {
							handle = hWnd;
						}
						
						if (wText != null && wText.indexOf(nameOrClass) > -1) {
							handle = hWnd;
						}
					}

					return true;
				}
			}, null);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}