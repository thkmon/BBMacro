package com.thkmon.bbmacro.main;

import com.thkmon.bbmacro.ctrl.CommandController;

public class BBMacro {
	
	public static void main(String[] args) {
		CommandController commCtrl = new CommandController();
		commCtrl.runCommand();
	}
}