package com.thkmon.bbmacro.util;

import java.io.File;
import java.util.ArrayList;

public class ProcessBuilderUtil {
	
	
//	/**
//	 * 익스플로러를 실행한다.
//	 * 
//	 * @throws Exception
//	 */
//	public static void executeIEProcess() throws Exception {
//		
//		ArrayList list = new ArrayList();
//		// list.add("C:\\Program Files\\Internet Explorer\\iexplore.exe");
//		//////// list.add("http://en.wikipedia.org/");
//
//		ProcessBuilder pb = new ProcessBuilder(list);
//		pb.start();
//	}
	
	public static boolean execute(String path, boolean bWriteLog, int lineNumber, String commandName) throws Exception {
		if (path == null || path.length() == 0) {
			if (bWriteLog) {
				
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The program cannot be run. path is empty.");
			}
			return false;
		}
		
		// 환경변수에 속한 프로그램일 수도 있으므로 파일패스 점검은 지운다.
//		File fileObj = new File(path);
//		if (!fileObj.exists()) {
//			if (bWriteLog) {
//				LogUtil.error("The program cannot be run. The file does not exists. filePath == [" + fileObj.getAbsolutePath() + "]");
//			}
//			return false;
//		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(path);
		
		ProcessBuilder pb = new ProcessBuilder(list);
		pb.start();
		
		if (bWriteLog) {
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] The program has been executed. path == [" + path + "]");
		}
		return true;
	}
}
