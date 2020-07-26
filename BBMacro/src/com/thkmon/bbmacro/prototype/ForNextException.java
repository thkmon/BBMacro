package com.thkmon.bbmacro.prototype;

public class ForNextException extends Exception {
	
	
	public ForNextException(int newLineNumber) {
		super();
		this.forLineNumber = newLineNumber;
	}
	
	
	private int forLineNumber = 0;
	
	
	public int getForLineNumber() {
		return forLineNumber;
	}
}
