package com.thkmon.bbmacro.main;

import com.thkmon.bbmacro.common.CommonConst;
import com.thkmon.bbmacro.ctrl.CommandController;
import com.thkmon.bbmacro.util.LogUtil;

public class BBMacro {
	
	public static void main(String[] args) {
		LogUtil.debug("BBMacro_" + CommonConst.version);
		LogUtil.debug("");
		
		CommandController commCtrl = new CommandController();
		commCtrl.runCommand();
		
		// 최종 메시지를 읽을 수 있도록 1분을 기다린 후 종료한다.
		try {
			Thread.sleep(60000);
		} catch (Exception e) {}
	}
}