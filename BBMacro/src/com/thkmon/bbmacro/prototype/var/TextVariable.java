package com.thkmon.bbmacro.prototype.var;

public class TextVariable extends Variable {
	
	
	protected TextVariable(String name) {
		super(name);
	}
	
	
	protected void init(String value) {
		if (value == null) {
			value = "";
		}
		
		this.textValue = value;
	}

	
	/**
	 * TEXT
	 */
	private String textValue = "";
	
	public String getTextValue() {
		return textValue;
	}


	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
}
