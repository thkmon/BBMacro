package com.thkmon.bbmacro.prototype.label;

import java.util.HashMap;

public class LabelMap extends HashMap<String, Integer> {
	
	
	public boolean addLabel(String labelName, int labelLineNumber) {
		if (labelLineNumber > -1) {
			this.put(labelName, labelLineNumber);
			return true;
		}
		
		return false;
	}
	
	
	public int getLabelLineNumber(String labelName) throws Exception {
		Object result = this.get(labelName);
		if (result == null) {
			throw new Exception("Label not found. labelName == [" + labelName + "]");
			// return -1;
		}
		
		return Integer.parseInt(String.valueOf(result));
	}
}