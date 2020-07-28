package com.thkmon.bbmacro.prototype.var;

import com.sun.jna.platform.win32.WinDef.HWND;

public class WindowVariable extends Variable {
	
	
	protected WindowVariable(String name) {
		super(name);
	}
	
	
	protected void init(HWND windowHwnd, String windowTextToFind) {
		this.windowHwnd = windowHwnd;
		this.windowTextToFind = windowTextToFind;
	}

	
	/**
	 * WINDOW
	 */
	private HWND windowHwnd = null;
	private String windowTextToFind = "";
	
	
	public HWND getWindowHwnd() {
		return windowHwnd;
	}


	public void setWindowHwnd(HWND windowHwnd) {
		this.windowHwnd = windowHwnd;
	}


	public String getWindowTextToFind() {
		return windowTextToFind;
	}


	public void setWindowTextToFind(String windowTextToFind) {
		this.windowTextToFind = windowTextToFind;
	}
}
