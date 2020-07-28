package com.thkmon.bbmacro.prototype.var;

import com.thkmon.bbmacro.prototype.FileContent;

public class FileVariable extends Variable {
	
	
	protected FileVariable(String name) {
		super(name);
	}


	protected void init(FileContent fileContent) {
		this.fileContent = fileContent;
	}
	
	
	/**
	 * FILE
	 */
	private FileContent fileContent = null;
	
	
	public FileContent getFileContent() {
		return fileContent;
	}


	public void setFileContent(FileContent fileContent) {
		this.fileContent = fileContent;
	}
}