package com.thkmon.bbmacro.prototype;

import java.util.HashMap;

import com.sun.jna.platform.win32.WinDef.HWND;

public class VariableMap extends HashMap<String, Variable> {
	
	
	/**
	 * TEXT 변수 추가
	 * 
	 * @param variableName
	 * @param pureText
	 * @return
	 */
	public boolean addTextVariable(String variableName, String pureText) {
		if (this.get(variableName) != null) {
			this.remove(variableName);
		}
		
		Variable var = this.put(variableName, new Variable(variableName, pureText));
		if (var != null) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * NUMBER 변수 추가
	 * 
	 * @param variableName
	 * @param pureNumber
	 * @return
	 */
	public boolean addNumberVariable(String variableName, int pureNumber) {
		if (this.get(variableName) != null) {
			this.remove(variableName);
		}
		
		Variable var = this.put(variableName, new Variable(variableName, pureNumber));
		if (var != null) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * FILE 변수 추가
	 * 
	 * @param variableName
	 * @param fileContent
	 * @return
	 */
	public boolean addFileVariable(String variableName, FileContent fileContent) {
		if (this.get(variableName) != null) {
			this.remove(variableName);
		}
		
		Variable var = this.put(variableName, new Variable(variableName, fileContent));
		if (var != null) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * FOR 변수 추가
	 * 
	 * @param variableName
	 * @param iCurrentValue
	 * @param iBeginValue
	 * @param iEndValue
	 * @param forLineNumber
	 * @return
	 */
	public boolean addForVariable(String variableName, int iCurrentValue, int iBeginValue, int iEndValue, int forLineNumber) {
		if (this.get(variableName) != null) {
			this.remove(variableName);
		}
		
		Variable var = this.put(variableName, new Variable(variableName, iCurrentValue, iBeginValue, iEndValue, forLineNumber));
		if (var != null) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * WINDOW 변수 추가
	 * 
	 * @param variableName
	 * @param hwnd
	 * @return
	 */
	public boolean addWindowVariable(String variableName, HWND hwnd, String windowTextToFind) {
		if (this.get(variableName) != null) {
			this.remove(variableName);
		}
		
		Variable var = this.put(variableName, new Variable(variableName, hwnd, windowTextToFind));
		if (var != null) {
			return true;
		}
		
		return false;
	}
}