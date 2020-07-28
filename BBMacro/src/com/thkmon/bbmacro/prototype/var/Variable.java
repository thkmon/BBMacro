package com.thkmon.bbmacro.prototype.var;

public class Variable {

	
	protected Variable(String name) {
		if (name == null || name.length() == 0) {
			return;
		}
		
		this.name = name;
	}
	
	
	private String name = "";
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
}