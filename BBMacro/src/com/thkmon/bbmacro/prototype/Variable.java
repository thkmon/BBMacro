package com.thkmon.bbmacro.prototype;

import com.sun.jna.platform.win32.WinDef.HWND;

public class Variable {

	private String name = "";
	private String type = "";
	
	/**
	 * TEXT
	 */
	private String textValue = "";
	
	
	/**
	 * NUMBER
	 */
	private int numberValue = 0;
	
	
	/**
	 * FILE
	 */
	private FileContent fileContent = null;
	
	
	/**
	 * FOR
	 */
	private int forCurrentValue = -1;
	private int forBeginValue = -1;
	private int forEndValue = -1;
	private int forLineNumber = -1;
	
	
	/**
	 * WINDOW
	 */
	private HWND windowHwnd = null;
	private String windowTextToFind = "";
	
	
	/**
	 * TEXT
	 * 
	 * @param name
	 * @param value
	 */
	public Variable(String name, String value) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		if (value == null) {
			value = "";
		}
		
		this.name = name;
		this.type = "TEXT";
		
		this.textValue = value;
	}
	
	
	/**
	 * NUMBER
	 * 
	 * @param name
	 * @param value
	 */
	public Variable(String name, int value) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		this.name = name;
		this.type = "NUMBER";
		
		this.numberValue = value;
	}
	
	
	/**
	 * FILE
	 * 
	 * @param name
	 * @param fileContent
	 */
	public Variable(String name, FileContent fileContent) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		this.name = name;
		this.type = "FILE";
		
		this.fileContent = fileContent;
	}
	
	
	/**
	 * FOR
	 * 
	 * @param name
	 * @param forCurrentValue
	 * @param forBeginValue
	 * @param forEndValue
	 * @param forLineNumber
	 */
	public Variable(String name, int forCurrentValue, int forBeginValue, int forEndValue, int forLineNumber) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		this.name = name;
		this.type = "FOR";
		
		this.forCurrentValue = forCurrentValue;
		this.forBeginValue = forBeginValue;
		this.forEndValue = forEndValue;
		this.forLineNumber = forLineNumber;
	}
	
	
	
	public Variable(String name, HWND windowHwnd, String windowTextToFind) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		this.name = name;
		this.type = "WINDOW";
		
		this.windowHwnd = windowHwnd;
		this.windowTextToFind = windowTextToFind;
	}
	
	
	/**
	 * TEXT
	 * 
	 * @return
	 */
	public boolean checkTypeIsText() {
		if (this.getType() != null && this.getType().equalsIgnoreCase("TEXT")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * NUMBER
	 * 
	 * @return
	 */
	public boolean checkTypeIsNumber() {
		if (this.getType() != null && this.getType().equalsIgnoreCase("NUMBER")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * FILE
	 * 
	 * @return
	 */
	public boolean checkTypeIsFile() {
		if (this.getType() != null && this.getType().equalsIgnoreCase("FILE")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * FOR
	 * 
	 * @return
	 */
	public boolean checkTypeIsFor() {
		if (this.getType() != null && this.getType().equalsIgnoreCase("FOR")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * WINDOW
	 * @return
	 */
	public boolean checkTypeIsWindow() {
		if (this.getType() != null && this.getType().equalsIgnoreCase("WINDOW")) {
			return true;
		}
		
		return false;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTextValue() {
		return textValue;
	}


	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}


	public int getNumberValue() {
		return numberValue;
	}


	public void setNumberValue(int numberValue) {
		this.numberValue = numberValue;
	}


	public FileContent getFileContent() {
		return fileContent;
	}


	public void setFileContent(FileContent fileContent) {
		this.fileContent = fileContent;
	}
	
	
	public int getForCurrentValue() {
		return forCurrentValue;
	}
	
	
	public void setForCurrentValue(int forCurrentValue) {
		this.forCurrentValue = forCurrentValue;
	}


	public int getForBeginValue() {
		return forBeginValue;
	}


	public void setForBeginValue(int forBeginValue) {
		this.forBeginValue = forBeginValue;
	}


	public int getForEndValue() {
		return forEndValue;
	}


	public void setForEndValue(int forEndValue) {
		this.forEndValue = forEndValue;
	}


	public int getForLineNumber() {
		return forLineNumber;
	}


	public void setForLineNumber(int forLineNumber) {
		this.forLineNumber = forLineNumber;
	}


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