package com.thkmon.bbmacro.prototype.var;

public class NumberVariable extends Variable {
	
	
	protected NumberVariable(String name) {
		super(name);
	}
	
	
	protected void init(int value) {
		this.numberValue = value;
	}
	
	
	/**
	 * NUMBER
	 */
	private int numberValue = 0;
	
	
	public int getNumberValue() {
		return numberValue;
	}


	public void setNumberValue(int numberValue) {
		this.numberValue = numberValue;
	}	
}