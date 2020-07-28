package com.thkmon.bbmacro.prototype.var;

import java.util.HashMap;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.thkmon.bbmacro.prototype.FileContent;

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
		
		TextVariable textVarObj = new TextVariable(variableName);
		textVarObj.init(pureText);
		
		Variable var = this.put(variableName, textVarObj);
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
		
		NumberVariable numberVarObj = new NumberVariable(variableName);
		numberVarObj.init(pureNumber);
		
		Variable var = this.put(variableName, numberVarObj);
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
		
		FileVariable fileVarObj = new FileVariable(variableName);
		fileVarObj.init(fileContent);
		
		Variable var = this.put(variableName, fileVarObj);
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
		
		ForVariable forVarObj = new ForVariable(variableName);
		forVarObj.init(iCurrentValue, iBeginValue, iEndValue, forLineNumber);
		
		Variable var = this.put(variableName, forVarObj);
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
		
		WindowVariable windowVarObj = new WindowVariable(variableName);
		windowVarObj.init(hwnd, windowTextToFind);
		
		Variable var = this.put(variableName, windowVarObj);
		if (var != null) {
			return true;
		}
		
		return false;
	}
}