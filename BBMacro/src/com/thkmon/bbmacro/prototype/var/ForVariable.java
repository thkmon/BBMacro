package com.thkmon.bbmacro.prototype.var;

public class ForVariable extends Variable {
	
	
	protected ForVariable(String name) {
		super(name);
	}
	
	
	protected void init(int forCurrentValue, int forBeginValue, int forEndValue, int forLineNumber) {
		
		this.forCurrentValue = forCurrentValue;
		this.forBeginValue = forBeginValue;
		this.forEndValue = forEndValue;
		this.forLineNumber = forLineNumber;
	}
	
	
	/**
	 * FOR
	 */
	private int forCurrentValue = -1;
	private int forBeginValue = -1;
	private int forEndValue = -1;
	private int forLineNumber = -1;
	
	
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
}