package com.thkmon.bblogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class BBLogger {
	
	private boolean bConsoleMode = true;
	private int logLevel = 5;
	private File logDirObj = null;
	private String logNameText = null;
	
	
	public boolean isbConsoleMode() {
		return bConsoleMode;
	}

	
	public void setbConsoleMode(boolean bConsoleMode) {
		this.bConsoleMode = bConsoleMode;
	}
	
	
	public int getLogLevel() {
		return logLevel;
	}


	public void setLogLevelDebug() {
		this.logLevel = 5;
	}
	
	public void setLogLevelInfo() {
		this.logLevel = 3;
	}
	
	
	public void setLogLevelError() {
		this.logLevel = 1;
	}
	
	
	public void setLogLevelNone() {
		this.logLevel = 0;
	}


	public BBLogger(String logDirPath, String logName) {
		
		try {
			if (logDirPath == null || logDirPath.length() == 0) {
				System.err.println("BBLogger : logDirPath is null or empty.");
				return;
			}
			
			if (logName == null || logName.length() == 0) {
				System.err.println("BBLogger : logName is null or empty.");
				return;
			}
			
			if (logName.indexOf("/") > -1) {
				logName = logName.replace("/", "");
			}
			
			if (logName.indexOf("\\") > -1) {
				logName = logName.replace("\\", "");
			}
			
			if (logName.indexOf(".") > -1) {
				logName = logName.replace(".", "");
			}
			
			File logDir = new File(logDirPath);
			String dirPath = revisePath(logDir.getAbsolutePath());
			if (!logDir.exists()) {
				System.err.println("BBLogger : The directory does not exists. [" + dirPath + "]");
				return;
			}
			
			if (!logDir.isDirectory()) {
				System.err.println("BBLogger : The path is not directory. [" + dirPath + "]");
				return;
			}
			
			this.logNameText = logName;
			this.logDirObj = logDir;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			this.logNameText = null;
			this.logDirObj = null;
		}
	}
	
	
	private File getLogFile() throws Exception {
		if (logDirObj == null) {
			throw new Exception("BBLogger getLogFile : logDirObj is null");
		}
		
		if (logNameText == null || logNameText.length() == 0) {
			throw new Exception("BBLogger getLogFile : logNameText is null");
		}
		
		String todayDate = getTodayDate();
		
		String inDirPath = revisePath(logDirObj.getAbsolutePath() + "/" + todayDate + "/" + logNameText);
		File inDir = new File(inDirPath);
		if (inDir.exists()) {
			if (!inDir.isDirectory()) {
				inDirPath = revisePath(inDir.getAbsolutePath());
				throw new Exception("BBLogger : this path is not directory [" + inDirPath + "]");
			}
		} else {
			inDir.mkdirs();
		}
		
		String logFilePath = revisePath(inDirPath + "/" + todayDate + ".log");
		File logFileObj = new File(logFilePath);
		return logFileObj;
	}
	
	
	private String revisePath(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		
		if (path.indexOf("\\") > -1) {
			path = path.replace("\\", "/");
		}
		
		while (path.indexOf("//") > -1) {
			path = path.replace("//", "/");
		}
		
		return path;
	}
	
	
	public void debug(Object obj) {
		
		try {
			if (this.logLevel < 5) {
				return;
			}
			
			String str = null;
			
			if (obj == null) {
				str = "null";
				
			} else if (obj instanceof Throwable) {
				str = getStackTraceString((Throwable) obj);
				
			} else {
				str = obj.toString();
			}
			
			str = "[debug]" + getTimeForLog() + obj.toString();
			
			if (bConsoleMode) {
				System.out.println(str);
			}
			
			writeToFile(str);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void info(Object obj) {
		
		try {
			if (this.logLevel < 3) {
				return;
			}
	
			String str = null;
			
			if (obj == null) {
				str = "null";
				
			} else if (obj instanceof Throwable) {
				str = getStackTraceString((Throwable) obj);
				
			} else {
				str = obj.toString();
			}
			
			str = "[info]" + getTimeForLog() + str;
			
			if (bConsoleMode) {
				System.out.println(str);
			}
			
			writeToFile(str);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void error(Object obj) {
		
		try {
			if (this.logLevel < 1) {
				return;
			}
	
			String str = null;
			
			if (obj == null) {
				str = "null";
				
			} else if (obj instanceof Throwable) {
				str = getStackTraceString((Throwable) obj);
				
			} else {
				str = obj.toString();
			}
			
			str = "[error]" + getTimeForLog() + str;
			
			if (bConsoleMode) {
				System.err.println(str);
			}
			
			writeToFile(str);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private String getStackTraceString(Throwable throwable) {
		if (throwable == null) {
			return "null";
		}
		
		StackTraceElement[] elems = throwable.getStackTrace();
		if (elems == null || elems.length == 0) {
			return "null";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(throwable.getClass().getName() + ": " + throwable.getMessage());
		
		int elemCount = elems.length;
		for (int i=0; i<elemCount; i++) {
			if (elems[i] != null) {
				builder.append("\n  at " + elems[i].toString());
			}
		}
		
		return builder.toString();
	}
	
	
	private void writeToFile(String str) throws Exception {
		if (this.logDirObj == null) {
			return;
		}
		
		if (str == null || str.length() == 0) {
			return;
		}
		
		File logFileObj = getLogFile();
		
		ArrayList<String> strList = new ArrayList<String>();
		strList.add(str);
		
		writeFile(logFileObj, strList);
	}
	
	
	private synchronized void writeFile(File file, ArrayList<String> strList) throws Exception {
		if (file == null) {
			return;
		}
		
		if (strList == null || strList.size() == 0) {
			return;
		}
		
		FileOutputStream outStream = null;
		OutputStreamWriter outStreamWriter = null;
		BufferedWriter bufferdWriter = null;
		
		try {
			outStream = new FileOutputStream(file, true);
			outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
			bufferdWriter = new BufferedWriter(outStreamWriter);

			String singleStr = null;
			int count = strList.size();
			for (int i=0; i<count; i++) {
				singleStr = strList.get(i);
				bufferdWriter.write(singleStr, 0, singleStr.length());
				bufferdWriter.newLine();
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {
				if (bufferdWriter != null) {
					bufferdWriter.close();
				}
				
			} catch (Exception e) {
				bufferdWriter = null;
				
			} finally {
				bufferdWriter = null;
			}
			
			try {
				if (outStreamWriter != null) {
					outStreamWriter.close();
				}
				
			} catch (Exception e) {
				outStreamWriter = null;
				
			} finally {
				outStreamWriter = null;
			}
			
			try {
				if (outStream != null) {
					outStream.close();
				}
				
			} catch (Exception e) {
				outStream = null;
				
			} finally {
				outStream = null;
			}
		}
	}

	
	private String getTimeForLog() {
		Calendar cal = Calendar.getInstance();
		StringBuffer dateTime = new StringBuffer();
		dateTime.append("[");
		dateTime.append(String.format("%04d", cal.get(Calendar.YEAR)));
		dateTime.append("/");
		dateTime.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		dateTime.append("/");
		dateTime.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		dateTime.append("_");
		dateTime.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		dateTime.append(":");
		dateTime.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		dateTime.append(":");
		dateTime.append(String.format("%02d", cal.get(Calendar.SECOND)));
		dateTime.append(":");
		dateTime.append(String.format("%03d", cal.get(Calendar.MILLISECOND)));
		dateTime.append("]");
		return dateTime.toString();
	}
	
	
	private String getTodayDate() {
		Calendar cal = Calendar.getInstance();
		StringBuffer dateTime = new StringBuffer();
		dateTime.append(String.format("%04d", cal.get(Calendar.YEAR)));
		dateTime.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		dateTime.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		return dateTime.toString();
	}
}