package com.thkmon.bbmacro.ctrl;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.thkmon.bblogger.BBLogger;
import com.thkmon.bbmacro.common.CommonConst;
import com.thkmon.bbmacro.handle.HandleFinder;
import com.thkmon.bbmacro.prototype.FileContent;
import com.thkmon.bbmacro.prototype.ForNextException;
import com.thkmon.bbmacro.prototype.MsgException;
import com.thkmon.bbmacro.prototype.Rect;
import com.thkmon.bbmacro.prototype.label.LabelMap;
import com.thkmon.bbmacro.prototype.var.ForVariable;
import com.thkmon.bbmacro.prototype.var.Variable;
import com.thkmon.bbmacro.prototype.var.VariableMap;
import com.thkmon.bbmacro.prototype.var.WindowVariable;
import com.thkmon.bbmacro.util.ClipboardUtil;
import com.thkmon.bbmacro.util.DateUtil;
import com.thkmon.bbmacro.util.FileUtil;
import com.thkmon.bbmacro.util.HwndUtil;
import com.thkmon.bbmacro.util.ImageUtil;
import com.thkmon.bbmacro.util.KeyConvertUtil;
import com.thkmon.bbmacro.util.LogUtil;
import com.thkmon.bbmacro.util.ProcessBuilderUtil;
import com.thkmon.bbmacro.util.PropertiesUtil;
import com.thkmon.bbmacro.util.RobotUtil;
import com.thkmon.bbmacro.util.StringUtil;

public class CommandController {
	
	
	public static VariableMap varMap = null; // 변수 맵
	public static LabelMap labelMap = null; // 레이블 맵
	public CommandHelper commandHelper = new CommandHelper();
	
	
	public boolean runCommand() {
		// option.properties 파일 읽기
		loadOptionProperties();
		
		// 변수 맵 초기화
		varMap = null;
		varMap = new VariableMap();
		
		// 레이블 맵 초기화
		labelMap = null;
		labelMap = new LabelMap();
		
		// 로봇 객체 초기화
		try {
			RobotUtil.initRobot();
		} catch (Exception e) {
			LogUtil.error("initRobot Error. e == [" + e.getMessage() + "]");
		}
		
		// 로그 객체 초기화
		if (CommonConst.logger == null) {
			String logFileName = "log_" + DateUtil.getTodayDateTime();
			CommonConst.logger = new BBLogger("log", logFileName);
		}
		
		File commandFile = new File(CommonConst.commandFileName);
		if (!commandFile.exists()) {
			LogUtil.error("Command File Not Found. Path == [" + commandFile.getAbsolutePath() + "]" );
			return false;
		}
		
		
		FileContent fileContent = null;
		
		try {
			fileContent = FileUtil.readFile(commandFile, "UTF-8");
			// LogUtil.debug(fileContent);
			
		} catch (Exception e) {
			LogUtil.error(e);
		}
		
		if (fileContent == null || fileContent.size() == 0) {
			LogUtil.error("fileContent is empty. Path == [" + commandFile.getAbsolutePath() + "]" );
			return false;
		}
		
		/**
		 * 시작 전 레이블 위치 수집
		 */
		{
			String oneLine = "";
			int lineCount = fileContent.size();
			for (int i=0; i<lineCount; i++) {
				oneLine = fileContent.get(i);
				
				String trimLine = oneLine.trim();
				if (!trimLine.startsWith("//") && trimLine.endsWith(":") && trimLine.indexOf(" ") < 0 /*띄어쓰기없고*/ && trimLine.indexOf("	") < 0 /*탭없고*/) {
					String labelName = trimLine.substring(0, trimLine.length() - 1);
					labelMap.addLabel(labelName, i);
					continue;
				}
			}
		}
		
		boolean oneResult = false;
		String oneLine = "";
		int lineCount = fileContent.size();
		for (int i=0; i<lineCount; i++) {
			oneLine = fileContent.get(i);
			try {
				
				/**
				 * GOTO문 구현
				 */
				String trimLine = oneLine.trim();
				if (trimLine.startsWith("GOTO ")) {
					String labelName = trimLine.substring(4).trim();
					int labelLineNumber = -1;
					try {
						labelLineNumber = labelMap.getLabelLineNumber(labelName);
					} catch (Exception e) {
						labelLineNumber = -1;
					}
					
					if (labelLineNumber == -1) {
						LogUtil.error("Label not found. labelName == [" + labelName + "]" );
						return false;
					} else {
						i = labelLineNumber;
						LogUtil.debug("Go to label. labelName == [" + labelName + "] / labelLineNumber == [" + labelLineNumber + "]");
						continue;
					}
				}
				
				/**
				 * 명령어 수행
				 */
				oneResult = runSingleCommand(oneLine, i);
				
			} catch (ForNextException e) {
				i = e.getForLineNumber();
				continue;
			}
			
			if (!oneResult) {
				break;
			}
		}
		
		return true;
	}
	
	
	/**
	 * option.properties 파일 읽기
	 */
	private void loadOptionProperties() {
		HashMap<String, String> optionProperties = null;
		try {
			optionProperties = PropertiesUtil.readPropertiesFile("option.properties");
			if (optionProperties != null) {
				// 명령어 파일명 가져오기 (ex : command.txt)
				String commandFileName = optionProperties.get("commandFileName");
				if (commandFileName != null && commandFileName.length() > 0) {
					CommonConst.commandFileName = commandFileName;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean runSingleCommand(String command, int lineNumber) throws ForNextException {
		if (command == null || command.trim().length() == 0) {
			return true;
		}
		
		// 좌우의 공백과 탭을 제거
		command = command.trim();
		
		// 슬래시 2개로 시작하면 주석으로 보고 스킵한다.
		if (command.startsWith("//")) {
			return true;
		}
		
		int indexOfDoubleSlash = StringUtil.indexOfConsideringDoubleQuotes(command, "//", false);
		if (indexOfDoubleSlash > -1) {
			command = command.substring(0, indexOfDoubleSlash);
		}
		
		String[] commandArr = StringUtil.splitConsideringDoubleQuotes(command);
		// LogUtil.debug(StringUtil.toStringFromArray(commandArr));
		
		if (commandArr != null && commandArr.length > 0) {
			if (commandArr[0] != null && commandArr[0].length() > 0) {
				String firstSlice = commandArr[0];
				
				try {
					// 명령어 실행
					if (firstSlice.equalsIgnoreCase("DIM")) {
						// DIM
						return commandDim(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("FILE")) {
						// FILE
						return commandFile(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("FOR")) {
						// FOR
						return commandFor(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("NEXT")) {
						// NEXT
						return commandNext(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("WINDOW")) {
						// WINDOW
						return commandWindow(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("FOCUSWIN")) {
						// FOCUSWIN
						return commandFocusWin(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("CLICKIMG")) {
						// CLICKIMG
						return commandClickImg(command, commandArr, lineNumber, true);
						
					} else if (firstSlice.equalsIgnoreCase("MOVEMOUSETOIMG")) {
						// MOVEMOUSETOIMG
						return commandClickImg(command, commandArr, lineNumber, false);
					
					} else if (firstSlice.equalsIgnoreCase("MOVEMOUSE")) {
						// MOVEMOUSE
						return commandMoveMouse(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("SHIFTMOUSE")) {
						// SHIFTMOUSE
						return commandShiftMouse(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("CLICK") || firstSlice.equalsIgnoreCase("DBCLICK")) {
						// CLICK, DBCLICK
						return commandClick(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("FINDIMG")) {
						// FINDIMG
						return commandFindImg(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("CHECKFILE")) {
						// CHECKFILE
						return commandCheckFile(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("PASTE")) {
						// PASTE
						return commandPaste(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("KEY")) {
						// KEY
						return commandKey(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("DELAY")) {
						// DELAY
						return commandDelay(command, commandArr, lineNumber);
						
					} else if (firstSlice.equalsIgnoreCase("END")) {
						// END
						boolean bEnd = commandEnd(command, commandArr, lineNumber);
						return !bEnd;
						
					} else if (firstSlice.equalsIgnoreCase("DEBUG")) {
						// DEBUG
						return commandDebug(command, commandArr, lineNumber);
						
					} else if (firstSlice != null && varMap.get(firstSlice.trim()) != null) {
						// 변수 계산 및 대입
						return commandVariable(command, commandArr, lineNumber);
						
					} else {
						LogUtil.error("Line " + lineNumber + " : [" + firstSlice + "] Unknown command. originCommand == [" + command + "]");
						return false;
					}
				
				} catch (ForNextException e) {
					throw e;
					
				} catch (MsgException e) {
					String commandName = firstSlice;
					LogUtil.error(commandName + " Exception! " + "Line " + lineNumber + " : [" + commandName + "] " + e.getMessage());
					return false;
					
				} catch (Exception e) {
					String commandName = firstSlice;
					LogUtil.error(commandName + " Exception! " + "Line " + lineNumber + " : [" + commandName + "] " + e.getClass().getName() + " " + e.getMessage());
					LogUtil.error(e);
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * DIM
	 * ex) DIM outputDirPath = "C:\bbmacro\output"
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandDim(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "DIM";
	
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		if (variableName.substring(0, 1).matches("[0-9]")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variable name cannot begin with a number.");
			return false;
		}
		
		String equalMark = commandArr[2].trim();
		if (!equalMark.equalsIgnoreCase("=")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need equal mark surrounded spaces(\" = \").");
			return false;
		}
		
		if (commandArr[4] != null && commandArr[4].trim().length() > 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format. originCommand == [" + originCommand + "]");
			return false;
		}
		
		boolean bValidCommand = false;
		
		String variableValue = commandArr[3].trim();
		if (commandHelper.checkIsVariableNumber(variableValue)) {
			int pureNumber = commandHelper.getVariablePureNumber(variableValue);
			varMap.addNumberVariable(variableName, pureNumber);
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / pureNumber == [" + pureNumber + "]");
			
			bValidCommand = true;
			
		} else if (commandHelper.checkIsVariableText(variableValue)) {
			String pureText = commandHelper.getVariablePureText(variableValue, lineNumber);
			varMap.addTextVariable(variableName, pureText);
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / pureText == [" + pureText + "]");
			
			bValidCommand = true;
		}
		
		if (!bValidCommand) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format. originCommand == [" + originCommand + "]");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * FILE
	 * ex) FILE pathList FROM "C:\bbmacro\pathList.txt" (UTF-8)
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandFile(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "FILE";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		String from = commandArr[2].trim();
		if (!from.equalsIgnoreCase("FROM")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need text \"FROM\".");
			return false;
		}
		
		String filePath = commandArr[3].trim();
		if (filePath != null && filePath.length() > 0) {
			filePath = commandHelper.getVariablePureText(filePath, lineNumber);
			
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] filePath is empty.");
			return false;
		}
		
		File fileObj = new File(filePath);
		if (fileObj == null || !fileObj.exists()) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The file does not exists. filePath == [" + fileObj.getAbsolutePath() + "]");
			return false;
		}
		
		String encode = "";
		try {
			encode = commandArr[4].trim();
			if (encode.indexOf("(") > -1) {
				encode = encode.replace("(", "");
			}
			
			if (encode.indexOf(")") > -1) {
				encode = encode.replace(")", "");
			}
		} catch (Exception e) {
		}
		
		// 기본인코딩은 UTF-8
		if (encode == null || encode.length() == 0) {
			encode = "UTF-8";
		}
		
		FileContent fileContent = FileUtil.readFile(fileObj, encode);
		if (fileContent == null || fileContent.size() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] fileContent is empty. filePath == [" + fileObj.getAbsolutePath() + "]");
			return false;
		}
		
		varMap.addFileVariable(variableName, fileContent);
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / fileContent.size == [" + fileContent.size() + "]");
		
		return true;
	}
	
	
	/**
	 * FOR
	 * ex) FOR i = 0 to lastIndex
	 *     (중략)
	 *     NEXT i
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandFor(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "FOR";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		String equalMark = commandArr[2].trim();
		if (!equalMark.equalsIgnoreCase("=")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need equal mark surrounded spaces(\" = \").");
			return false;
		}
		
		String beginValue = commandArr[3].trim();
		int iBeginValue = commandHelper.getVariablePureNumber(beginValue);
		
		String to = commandArr[4].trim();
		if (!to.equalsIgnoreCase("TO")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need text \"TO\".");
			return false;
		}
		
		String endValue = commandArr[5].trim();
		int iEndValue = commandHelper.getVariablePureNumber(endValue);
		
		int forLineNumber = lineNumber;
		varMap.addForVariable(variableName, iBeginValue, iBeginValue, iEndValue, forLineNumber);
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / iBeginValue == [" + iBeginValue + "] / iEndValue == [" + iEndValue + "] / forLineNumber == [" + forLineNumber + "]");
		
		return true;
	}
	
	
	/**
	 * NEXT
	 * ex) FOR i = 0 to lastIndex
	 *     (중략)
	 *     NEXT i
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandNext(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "NEXT";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		Variable varObj = varMap.get(variableName);
		if (varObj == null) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is undefined. variableName == [" + variableName + "]");
			return false;
		}
	
		if (!(varObj instanceof ForVariable)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is not type of FOR. variableName == [" + variableName + "]");
			return false;
		}
		
		ForVariable forVarObj = (ForVariable) varObj;
		forVarObj.setForCurrentValue(forVarObj.getForCurrentValue() + 1);
		if (forVarObj.getForCurrentValue() <= forVarObj.getForEndValue()) {
			int iBeginValue = forVarObj.getForBeginValue();
			int iEndValue = forVarObj.getForEndValue();
			int forLineNumber = forVarObj.getForLineNumber();
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / iBeginValue == [" + iBeginValue + "] / iEndValue == [" + iEndValue + "] / forLineNumber == [" + forLineNumber + "]");
			
			throw new ForNextException(forLineNumber);
		}
		
		// FOR문의 끝. 계속 다음 라인을 수행한다.
		return true;
	}
	
	
	/**
	 * WINDOW
	 * ex) WINDOW win AS "HwpApp"
	 *     또는
	 *     WINDOW win AS "HwpApp" WITH "C:\Program Files (x86)\Hnc\Hwp80\Hwp.exe"
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandWindow(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "WINDOW";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		String from = commandArr[2].trim();
		if (!from.equalsIgnoreCase("AS")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need text \"AS\".");
			return false;
		}
		
		String handleNameOrClass = commandArr[3].trim();
		if (handleNameOrClass == null || handleNameOrClass.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] handleNameOrClass is empty.");
			return false;
		}
		
		handleNameOrClass = commandHelper.getVariablePureText(handleNameOrClass, lineNumber);
		
		boolean bWithProgramPath = false;
		String with = commandArr[4].trim();
		String programPath = "";
		if (with.equalsIgnoreCase("WITH")) {
			programPath = commandArr[5].trim();
			if (programPath != null && programPath.length() > 0) {
				programPath = commandHelper.getVariablePureText(programPath, lineNumber);
			}
			
			if (programPath != null && programPath.length() > 0) {
				bWithProgramPath = true;
				
			} else {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] programPath is empty.");
				return false;
			}
		}
		
		if (bWithProgramPath) {
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to find the window [" + handleNameOrClass + "] WITH [" + programPath + "]");
		} else {
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to find the window [" + handleNameOrClass + "]");
		}
		
		HandleFinder handleFinder = new HandleFinder();
		HWND handle = handleFinder.getWindowHandle(handleNameOrClass);
		
		if (handle != null) {
			Thread.sleep(CommonConst.defaultDelay);
		} else {
			// 못찾았을 경우 exe를 실행한다. exe 실행조건이 있는 경우(WITH 조건이 있는 경우), 못찾았으면 찾을 때까지 돌린다.
			if (bWithProgramPath) {
				ProcessBuilderUtil.execute(programPath, true, lineNumber, commandName);
				
				while (handle == null) {
					handle = handleFinder.getWindowHandle(handleNameOrClass);
					Thread.sleep(CommonConst.defaultDelay);
				}
			}
		}
		
		varMap.addWindowVariable(variableName, handle, handleNameOrClass);
		
		String handleText = "NOTFOUND";
		String handleClassName = "NOTFOUND";
		if (handle != null) {
			handleText = HwndUtil.getHandleText(handle);
			handleClassName = HwndUtil.getHandleClassName(handle);
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / handleText == [" + handleText + "] / handleClassName == [" + handleClassName + "]");
	
		return true;
	}
	
	
	/**
	 * FOCUSWIN
	 * ex) FOCUSWIN win
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandFocusWin(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "FOCUSWIN";
	
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		Variable varObj = varMap.get(variableName);
		if (varObj == null) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is undefined. variableName == [" + variableName + "]");
			
			return false;
		}
		
		if (!(varObj instanceof WindowVariable)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is not type of WINDOW. variableName == [" + variableName + "]");
			return false;
		}
		
		WindowVariable winVarObj = (WindowVariable) varObj;
		
		HandleFinder handleFinder = new HandleFinder();
		HWND handle = winVarObj.getWindowHwnd();
		String handleNameOrClass = winVarObj.getWindowTextToFind();
		if (handleNameOrClass == null || handleNameOrClass.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The handleNameOrClass is empty. originCommand == [" + originCommand + "]");
			return false;
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to focus the window [" + handleNameOrClass + "]");
		
		// 못찾았으면 찾을 때까지 돌린다.
		while (handle == null) {
			handle = handleFinder.getWindowHandle(handleNameOrClass);
			Thread.sleep(CommonConst.defaultDelay);
		}
		
		HwndUtil.setFocusHandle(handle);
		Thread.sleep(CommonConst.defaultDelay);
		
		String handleText = "NOTFOUND";
		String handleClassName = "NOTFOUND";
		if (handle != null) {
			handleText = HwndUtil.getHandleText(handle);
			handleClassName = HwndUtil.getHandleClassName(handle);
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / handleText == [" + handleText + "] / handleClassName == [" + handleClassName + "]");
		
		return true;
	}
	
	
	/**
	 * CLICKIMG
	 * ex) CLICKIMG "img\button_open.bmp"
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandClickImg(String originCommand, String[] commandArr, int lineNumber, boolean doClick) throws Exception {
		String commandName = "CLICKIMG";
		if (doClick) {
			commandName = "CLICKIMG";
		} else {
			commandName = "MOVEMOUSETOIMG";	
		}
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String originImagePath = commandArr[1].trim();
		if (originImagePath != null && originImagePath.length() > 0) {
			originImagePath = commandHelper.getVariablePureText(originImagePath, lineNumber);
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The imagePath is empty.");
			return false;
		}
		
		String mouseButton = "";
		try {
			mouseButton = commandArr[2].trim();
			if (mouseButton.indexOf("(") > -1) {
				mouseButton = mouseButton.replace("(", "");
			}
			
			if (mouseButton.indexOf(")") > -1) {
				mouseButton = mouseButton.replace(")", "");
			}
		} catch (Exception e) {
		}
		
		// 기본은 마우스 좌클릭
		if (mouseButton == null || mouseButton.length() == 0) {
			mouseButton = "LEFT";
		}
		
		// 구분자 파이프로 split
		String[] imagePathArr = originImagePath.split("\\|");
		int arrCount = imagePathArr.length;
		
		for (int i=0; i<arrCount; i++) {
			String imagePath = imagePathArr[i];
			if (imagePath == null || imagePath.length() == 0) {
				continue;
			} else {
				imagePath = imagePath.trim();
			}
			
			// 이미지 존재하는지 확인
			File imgFileObj = new File(imagePath);
			if (!imgFileObj.exists()) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The file does not exists. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
				return false;
			}
			
			// 이미지 bmp 만 허용. jpg, png 등 확장자의 이미지 손상압축으로 인해 이미지 찾아낼 수 없는 오류 방지
			String fileExt = FileUtil.getFileExtention(imgFileObj);
			if (fileExt == null || !fileExt.equalsIgnoreCase("bmp")) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Only \"bmp\" is allowed as the file extension. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
				return false;
			}
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to find the image. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
		}
		
		Rect imgRect = null;
		while (imgRect == null) {
			for (int i=0; i<arrCount; i++) {
				String imagePath = imagePathArr[i];
				if (imagePath == null || imagePath.length() == 0) {
					continue;
				} else {
					imagePath = imagePath.trim();
				}
				
				// 이미지 존재하는지 확인
				File imgFileObj = new File(imagePath);
				if (!imgFileObj.exists()) {
					LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The file does not exists. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
					return false;
				}
				
				// 이미지 bmp 만 허용. jpg, png 등 확장자의 이미지 손상압축으로 인해 이미지 찾아낼 수 없는 오류 방지
				String fileExt = FileUtil.getFileExtention(imgFileObj);
				if (fileExt == null || !fileExt.equalsIgnoreCase("bmp")) {
					LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Only \"bmp\" is allowed as the file extension. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
					return false;
				}
				
				imgRect = ImageUtil.findImageRectFromScreen(imgFileObj);
				if (imgRect != null) {
					break;
				}
			}
			
			if (imgRect != null) {
				break;
			}
		}
		
		Thread.sleep(CommonConst.defaultDelay);
		
		int mx = imgRect.getMiddleX();
		int my = imgRect.getMiddleY();
		
		if (doClick) {
			if (mouseButton != null && mouseButton.equals("RIGHT")) {
				RobotUtil.clickMouseRight(mx, my);
			} else {
				RobotUtil.clickMouseLeft(mx, my);
			}
		} else {
			RobotUtil.moveMouse(mx, my);
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. mx == [" + mx + "] / my == [" + my + "] / mouseButton == [" + mouseButton + "]");
		
		return true;
	}
	
	
	/**
	 * MOVEMOUSE
	 * ex) MOVEMOUSE 100,100
	 * 
	 * 마우스 이동하기 (현재위치와 관계없이)
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandMoveMouse(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "MOVEMOUSE";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String value = commandArr[1].trim();
		
		// 구분자 파이프로 split
		if (value == null || value.indexOf(",") < 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
			return false;
		}
		
		String[] valueArr = value.split(",");
		int arrCount = valueArr.length;
		if (arrCount < 2) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
			return false;
		}
		
		int mx = 0;
		int my = 0;
		if (valueArr[0] != null && valueArr[0].length() > 0) {
			mx = commandHelper.getVariablePureNumber(valueArr[0]);
		}
		
		if (valueArr[1] != null && valueArr[1].length() > 0) {
			my = commandHelper.getVariablePureNumber(valueArr[1]);
		}
		
		RobotUtil.moveMouse(mx, my);
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. mx == [" + mx + "] / my == [" + my + "]");
		
		return true;
	}
	
	
	/**
	 * SHIFTMOUSE
	 * ex) SHIFTMOUSE 100,100
	 * 
	 * 마우스 현재위치 기준에서 이동하기
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandShiftMouse(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "SHIFTMOUSE";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String value = commandArr[1].trim();
		
		// 구분자 파이프로 split
		if (value == null || value.indexOf(",") < 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
			return false;
		}
		
		String[] valueArr = value.split(",");
		int arrCount = valueArr.length;
		if (arrCount < 2) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
			return false;
		}
		
		int mx = 0;
		int my = 0;
		if (valueArr[0] != null && valueArr[0].length() > 0) {
			mx = commandHelper.getVariablePureNumber(valueArr[0]);
		}
		
		if (valueArr[1] != null && valueArr[1].length() > 0) {
			my = commandHelper.getVariablePureNumber(valueArr[1]);
		}
		
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		int currentX = (int)pointerInfo.getLocation().getX();
		int currentY = (int)pointerInfo.getLocation().getY();
			
		RobotUtil.moveMouse(currentX + mx, currentY + my);
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. mx == [" + mx + "] / my == [" + my + "]");
		
		return true;
	}
	
	
	/**
	 * CLICK
	 * ex) CLICK
	 * 또는 CLICK 100,100
	 * 또는 CLICK 100,100 (LEFT)
	 * 또는 CLICK (LEFT)
	 * 
	 * DBCLICK
	 * ex) DBCLICK
	 * 또는 DBCLICK 100,100
	 * 또는 DBCLICK 100,100 (LEFT)
	 * 또는 DBCLICK (LEFT)
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandClick(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "CLICK";
		String strDbClick = "DBCLICK";
		boolean bDoubleClick = false;
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(strDbClick)) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
				return false;
			} else {
				commandName = strDbClick;
				bDoubleClick = true;
			}
		}
		
		boolean bUsePixel = false;
		String value = commandArr[1].trim();
		String[] valueArr = null;
		
		if (value != null && value.length() > 0 && value.indexOf("(") < 0 && value.indexOf(")") < 0) {
			// 구분자 파이프로 split
			if (value.indexOf(",") < 0) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
				return false;
			}
			
			valueArr = value.split(",");
			int arrCount = valueArr.length;
			if (arrCount < 2) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format.");
				return false;
			}
			
			bUsePixel = true;
		}
		
		String mouseButton = "";
		try {
			if (bUsePixel) {
				mouseButton = commandArr[2].trim();
			} else {
				mouseButton = commandArr[1].trim();
			}
			
			if (mouseButton.indexOf("(") > -1) {
				mouseButton = mouseButton.replace("(", "");
			}
			
			if (mouseButton.indexOf(")") > -1) {
				mouseButton = mouseButton.replace(")", "");
			}
		} catch (Exception e) {
		}
		
		// 기본은 마우스 좌클릭
		if (mouseButton == null || mouseButton.length() == 0) {
			mouseButton = "LEFT";
		}
		
		if (bUsePixel) {
			int mx = 0;
			int my = 0;
			if (valueArr[0] != null && valueArr[0].length() > 0) {
				mx = commandHelper.getVariablePureNumber(valueArr[0]);
			}
			
			if (valueArr[1] != null && valueArr[1].length() > 0) {
				my = commandHelper.getVariablePureNumber(valueArr[1]);
			}
			
			if (mouseButton != null && mouseButton.equals("RIGHT")) {
				RobotUtil.clickMouseRight(mx, my);
				if (bDoubleClick) {
					Thread.sleep(CommonConst.doubleClickDelay);
					RobotUtil.clickMouseRight(mx, my);
				}
			} else {
				RobotUtil.clickMouseLeft(mx, my);
				if (bDoubleClick) {
					Thread.sleep(CommonConst.doubleClickDelay);
					RobotUtil.clickMouseLeft(mx, my);
				}
			}
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. mx == [" + mx + "] / my == [" + my + "] / mouseButton == [" + mouseButton + "]");
			
		} else {
			if (mouseButton != null && mouseButton.equals("RIGHT")) {
				RobotUtil.clickMouseRight();
				if (bDoubleClick) {
					Thread.sleep(CommonConst.doubleClickDelay);
					RobotUtil.clickMouseRight();
				}
			} else {
				RobotUtil.clickMouseLeft();
				if (bDoubleClick) {
					Thread.sleep(CommonConst.doubleClickDelay);
					RobotUtil.clickMouseLeft();
				}
			}
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. mouseButton == [" + mouseButton + "]");
		}
		
		return true;
	}
	
	
	/**
	 * FINDIMG
	 * ex) FINDIMG "img\img_pdf_success.bmp"
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandFindImg(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "FINDIMG";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String originImagePath = commandArr[1].trim();
		if (originImagePath != null && originImagePath.length() > 0) {
			originImagePath = commandHelper.getVariablePureText(originImagePath, lineNumber);
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The imagePath is empty.");
			return false;
		}
		
		
		/**
		 * FINDIMG 내에 일종의 GOTO 문 구현 (ex : FINDIMG "경로" A:B 식으로 사용하면, 이미지 찾으면 A 로 이동, 이미지 찾지 못하면 B 로 이동
		 */
		boolean isFindImgOnce = false;
		String leftLabelName = "";
		String rightLabelName = "";
		
		if (commandArr.length > 2) {
			String lastCommand = commandArr[2];
			if (lastCommand != null && lastCommand.length() > 0) {
				int idxColon = lastCommand.indexOf(":");
				if (idxColon > -1) {
					leftLabelName = lastCommand.substring(0, idxColon);
					rightLabelName = lastCommand.substring(idxColon + 1);
					if (leftLabelName.length() > 0 && rightLabelName.length() > 0) {
						isFindImgOnce = true;
					} else {
						leftLabelName = "";
						rightLabelName = "";
					}
				}
			}
		}
		
		
		// 구분자 파이프로 split
		String[] imagePathArr = originImagePath.split("\\|");
		int arrCount = imagePathArr.length;
		
		for (int i=0; i<arrCount; i++) {
			String imagePath = imagePathArr[i];
			if (imagePath == null || imagePath.length() == 0) {
				continue;
			} else {
				imagePath = imagePath.trim();
			}
			
			// 이미지 존재하는지 확인
			File imgFileObj = new File(imagePath);
			if (!imgFileObj.exists()) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The file does not exists. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
				return false;
			}
			
			// 이미지 bmp 만 허용. jpg, png 등 확장자의 이미지 손상압축으로 인해 이미지 찾아낼 수 없는 오류 방지
			String fileExt = FileUtil.getFileExtention(imgFileObj);
			if (fileExt == null || !fileExt.equalsIgnoreCase("bmp")) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Only \"bmp\" is allowed as the file extension. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
				return false;
			}
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to find the image. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
		}
		
		Rect imgRect = null;
		while (imgRect == null) {
			for (int i=0; i<arrCount; i++) {
				String imagePath = imagePathArr[i];
				if (imagePath == null || imagePath.length() == 0) {
					continue;
				} else {
					imagePath = imagePath.trim();
				}
				
				// 이미지 존재하는지 확인
				File imgFileObj = new File(imagePath);
				if (!imgFileObj.exists()) {
					LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The file does not exists. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
					return false;
				}
				
				// 이미지 bmp 만 허용. jpg, png 등 확장자의 이미지 손상압축으로 인해 이미지 찾아낼 수 없는 오류 방지
				String fileExt = FileUtil.getFileExtention(imgFileObj);
				if (fileExt == null || !fileExt.equalsIgnoreCase("bmp")) {
					LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Only \"bmp\" is allowed as the file extension. imagePath == [" + imgFileObj.getAbsolutePath() + "]");
					return false;
				}
				
				imgRect = ImageUtil.findImageRectFromScreen(imgFileObj);
				if (isFindImgOnce) {
					break;
				}
				
				if (imgRect != null) {
					break;
				}
			}
			
			if (isFindImgOnce) {
				break;
			}
			
			if (imgRect != null) {
				break;
			}
		}
		
		Thread.sleep(CommonConst.defaultDelay);
		
		if (imgRect != null) {
			int ix = imgRect.getX();
			int iy = imgRect.getY();
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. ix == [" + ix + "] / iy == [" + iy + "]");
			
			if (isFindImgOnce) {
				int labelLineNumber = labelMap.getLabelLineNumber(leftLabelName);
				throw new ForNextException(labelLineNumber);
			}
		} else {
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] NOT FOUND.");
			if (isFindImgOnce) {
				int labelLineNumber = labelMap.getLabelLineNumber(rightLabelName);
				throw new ForNextException(labelLineNumber);
			}
		}
		
		return true;
	}
	
	
	
	/**
	 * CHECKFILE
	 * ex) CHECKFILE oneResult
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandCheckFile(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "CHECKFILE";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String filePath = commandArr[1].trim();
		if (filePath != null && filePath.length() > 0) {
			filePath = commandHelper.getVariablePureText(filePath, lineNumber);
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The filePath is empty.");
			return false;
		}
		

		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] Try to check the file. filePath == [" + filePath + "]");
		
		boolean bFileExists = false;
		File fileObj = null;
		
		while (!bFileExists) {
			fileObj = new File(filePath);
			bFileExists = fileObj.exists();
			Thread.sleep(CommonConst.defaultDelay);
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. filePath == [" + filePath + "]");
		
		return true;
	}
	
	
	/**
	 * PASTE
	 * ex) PASTE path
	 *     또는
	 *     PASTE
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandPaste(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "PASTE";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String variableName = commandArr[1].trim();
		if (variableName != null && variableName.length() > 0) {
			variableName = commandHelper.getVariablePureText(variableName, lineNumber);
			ClipboardUtil.setClipboard(variableName);
		}
		
		RobotUtil.pressKeyCtrlV();
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS.");
		
		return true;
	}
	
	
	/**
	 * KEY
	 * ex) KEY ENTER
	 *     또는
	 *     KEY 13
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandKey(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "KEY";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String originKeyCode = commandArr[1].trim();
		if (originKeyCode == null || originKeyCode.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] keyCode is empty.");
			return false;
		}
		
		// 쌍따옴표로 둘러싸여 있고 플러스 기호(+)가 포함되어 있는 경우
		// 문자열을 재조립한다.
		// [AS-IS] "ALT+F4"
		// [TO-BE] "ALT"+"F4"
		String inputKeyCode = commandHelper.reviseInputKeyCode(originKeyCode);
		
		int keyCode1 = -1;
		int keyCode2 = -1;
		int keyCode3 = -1;
		
		String[] keyCodeArr = inputKeyCode.split("\\+");
		int keyCodeCount = keyCodeArr.length;
		if (keyCodeCount >= 4) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Up to 3 keys can be entered. inputKeyCode == [" + inputKeyCode + "]");
			return false;
		}
		
		for (int i=0; i<keyCodeCount; i++) {
			String strKey = keyCodeArr[i];
			int iKeycode = -1;
			if (StringUtil.checkOnlyNumbers(strKey)) {
				iKeycode = StringUtil.parseInt(strKey, -1);
				
			} else {
				if (strKey != null && strKey.length() > 0) {
					strKey = commandHelper.getVariablePureText(strKey, lineNumber);
					if (strKey != null && strKey.length() > 0) {
						iKeycode = KeyConvertUtil.getKeycode(strKey);
					}
				}
			}
			
			if (iKeycode > -1) {
				if (keyCode1 < 0) {
					keyCode1 = iKeycode;
				} else if (keyCode2 < 0) {
					keyCode2 = iKeycode;
				} else if (keyCode3 < 0) {
					keyCode3 = iKeycode;
					break;
				}
			}
		}
		
		if (keyCode1 > -1 && keyCode2 > -1 && keyCode3 > -1) {
			// 키입력 3개 눌림
			if (keyCode1 == KeyEvent.VK_CONTROL && keyCode2 == KeyEvent.VK_ALT && keyCode3 == KeyEvent.VK_DELETE) {
				// CTRL + ALT + DELETE : 작업관리자
				try {
					ProcessBuilderUtil.execute("Taskmgr", true, lineNumber, commandName);
					LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. inputKeyCode == [" + inputKeyCode + "] / keycode == [taskmgr]");
				} catch (Exception e) {
					LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Please run the program with administrator privileges. inputKeyCode == [" + inputKeyCode + "] / keycode == [taskmgr]");
				}
				
			} else {
				RobotUtil.pressKey(keyCode1, keyCode2, keyCode3);
				LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. inputKeyCode == [" + inputKeyCode + "] / keycode == [" + keyCode1 + ", " + keyCode2 + ", " + keyCode3 + "]");
			}
			
		} else if (keyCode1 > -1 && keyCode2 > -1) {
			// 키입력 2개 눌림
			if (keyCode1 == KeyEvent.VK_WINDOWS && keyCode2 == KeyEvent.VK_L) {
				// WINDOWS + L : 윈도우 락 (로그아웃)
				Runtime.getRuntime().exec("rundll32 user32.dll,LockWorkStation");
				LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. inputKeyCode == [" + inputKeyCode + "] / keycode == [LockWorkStation]");
				
			} else {
				RobotUtil.pressKey(keyCode1, keyCode2);
				LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. inputKeyCode == [" + inputKeyCode + "] / keycode == [" + keyCode1 + ", " + keyCode2 + "]");
			}
			
		} else if (keyCode1 > -1) {
			// 키입력 1개 눌림
			RobotUtil.pressKey(keyCode1);
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. inputKeyCode == [" + inputKeyCode + "] / keycode == [" + keyCode1 + "]");
			
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] unknown key. inputKeyCode == [" + inputKeyCode + "]");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * DELAY
	 * ex) DELAY 1000
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandDelay(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "DELAY";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String time = commandArr[1].trim();
		if (time == null || time.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] time is empty.");
			return false;
		}
		
		if (commandHelper.checkIsVariableNumber(time)) {
			int iTime = commandHelper.getVariablePureNumber(time);
			
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. time == [" + iTime + "]");
			
			if (iTime > 0) {
				Thread.sleep(iTime);
			}
			
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format. originCommand == [" + originCommand + "]");
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * END
	 * ex) END
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandEnd(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "END";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS.");
		
		return true;
	}
	
	
	/**
	 * DEBUG
	 * ex) DEBUG lastIndex
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandDebug(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		String commandName = "DEBUG";
		
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		if (commandArr[0] == null || !commandArr[0].equalsIgnoreCase(commandName)) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] commandName is invalid.");
			return false;
		}
		
		String element = commandArr[1].trim();
		if (element == null || element.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] element is empty.");
			return false;
		}
		
		String resultText = "";
		
		String pureText = "";
		if (StringUtil.checkSurroundDoubleQuotes(element)) {
			pureText = StringUtil.getStringAfterRemovingSurroundedDoubleQuotes(element);
			
			resultText = pureText;
		
		} else if (StringUtil.checkOnlyNumbers(element)) {
			pureText = String.valueOf(element);
			
			resultText = pureText;
			
		} else if (commandHelper.checkIsVariableNumber(element)) {
			pureText = String.valueOf(commandHelper.getVariablePureNumber(element));
			
			resultText = element + " == [" + pureText + "]";
			
		} else if (commandHelper.checkIsVariableText(element)) {
			pureText = commandHelper.getVariablePureText(element, lineNumber);
			
			resultText = element + " == [" + pureText + "]";
			
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is undefined. variableName == [" + element + "]");
			return false;
		}
		
		LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] " + resultText);
		
		return true;
	}
	
	
	/**
	 * VARIABLE
	 * ex) lastIndex = lastIndex - 1
	 * 
	 * @param originCommand
	 * @param commandArr
	 * @param lineNumber
	 * @return
	 */
	private boolean commandVariable(String originCommand, String[] commandArr, int lineNumber) throws Exception {
		if (commandArr == null || commandArr.length == 0) {
			return false;
		}
		
		String commandName = "VARIABLE";
		
		String variableName = commandArr[0].trim();
		if (variableName == null || variableName.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] variableName is empty.");
			return false;
		}
		
		if (varMap.get(variableName) == null) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] The variable is undefined. variableName == [" + variableName + "]");
			return false;
		}
		
		String equalMark = commandArr[1].trim();
		if (!equalMark.equalsIgnoreCase("=")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] This command need equal mark surrounded spaces(\" = \").");
			return false;
		}
		
		String element1 = commandArr[2].trim();
		if (element1 == null || element1.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] element1 is empty.");
			return false;
		}
		
		String operator = commandArr[3].trim();
		if (operator == null || operator.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] operator is empty.");
			return false;
		} else if (!operator.equals("+") && !operator.equals("-") && !operator.equals("/") && !operator.equals("*") && !operator.equals("%")) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] operator is invalid. (+, -, /, *, %)");
			return false;
		}
		
		String element2 = commandArr[4].trim();
		if (element2 == null || element2.length() == 0) {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] element2 is empty.");
			return false;
		}
		
		if (commandHelper.checkIsVariableNumber(element1) && commandHelper.checkIsVariableNumber(element2)) {
			int number1 = commandHelper.getVariablePureNumber(element1);
			int number2 = commandHelper.getVariablePureNumber(element2);
			int pureNumber = 0;
			if (operator.equals("+")) {
				pureNumber = number1 + number2;
			} else if (operator.equals("-")) {
				pureNumber = number1 - number2;
			} else if (operator.equals("/")) {
				pureNumber = number1 / number2;
			} else if (operator.equals("*")) {
				pureNumber = number1 * number2;
			} else if (operator.equals("%")) {
				pureNumber = number1 % number2;
			}
			
			varMap.addNumberVariable(variableName, pureNumber);
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / pureNumber == [" + pureNumber + "]");
			
		} else if (commandHelper.checkIsVariableText(element1) || commandHelper.checkIsVariableText(element2)) {
			if (!operator.equals("+")) {
				LogUtil.error("Line " + lineNumber + " : [" + commandName + "] operator is invalid. (+)");
				return false;
			}
			
			String text1 = "";
			if (commandHelper.checkIsVariableNumber(element1)) {
				text1 = String.valueOf(commandHelper.getVariablePureNumber(element1));
			} else {
				text1 = commandHelper.getVariablePureText(element1, lineNumber);
			}
			
			String text2 = "";
			if (commandHelper.checkIsVariableNumber(element2)) {
				text2 = String.valueOf(commandHelper.getVariablePureNumber(element2));
			} else {
				text2 = commandHelper.getVariablePureText(element2, lineNumber);
			}
			
			String pureText = text1 + "" + text2;
			
			varMap.addTextVariable(variableName, pureText);
			LogUtil.debug("Line " + lineNumber + " : [" + commandName + "] SUCCESS. variableName == [" + variableName + "] / pureText == [" + pureText + "]");
			
		} else {
			LogUtil.error("Line " + lineNumber + " : [" + commandName + "] Unknown format. originCommand == [" + originCommand + "]");
			return false;
		}
		
		return true;
	}
}