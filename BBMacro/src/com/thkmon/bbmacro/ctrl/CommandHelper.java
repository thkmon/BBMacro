package com.thkmon.bbmacro.ctrl;

import com.thkmon.bbmacro.prototype.MsgException;
import com.thkmon.bbmacro.prototype.var.FileVariable;
import com.thkmon.bbmacro.prototype.var.ForVariable;
import com.thkmon.bbmacro.prototype.var.NumberVariable;
import com.thkmon.bbmacro.prototype.var.TextVariable;
import com.thkmon.bbmacro.prototype.var.Variable;
import com.thkmon.bbmacro.util.StringUtil;

public class CommandHelper {
	
	
	public boolean checkIsVariableText(String variableValue) {
		if (variableValue == null || variableValue.length() == 0) {
			return false;
		}
		
		if (StringUtil.checkSurroundDoubleQuotes(variableValue)) {
			return true;
		}
		
		int leftBigBracket = variableValue.indexOf("[", 1); // 1부터 자르는 이유는 변수명이 있어야 하기 때문이다.
		int rightBigBracket = variableValue.indexOf("]", leftBigBracket + 1);
		if (leftBigBracket > -1 && rightBigBracket > -1) {
			// FILE 변수일 경우
			return true;
		} else {
			// TEXT 변수일 경우
			Variable varObj = CommandController.varMap.get(variableValue);
			if (varObj != null && varObj instanceof TextVariable) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public boolean checkIsVariableNumber(String variableValue) {
		if (variableValue == null || variableValue.length() == 0) {
			return false;
		}
		
		boolean bResult = false;
		
		if (StringUtil.checkSurroundText(variableValue, "LEN(", ")", true)) {
			bResult = true;
			
		} else if (StringUtil.checkOnlyNumbers(variableValue)) {
			bResult = true;
			
		} else {
			// NUMBER 변수일 경우
			Variable indexVarObj = CommandController.varMap.get(variableValue);
			if (indexVarObj != null) {
				if (indexVarObj instanceof NumberVariable) {
					bResult = true;
				} else if (indexVarObj instanceof ForVariable) {
					bResult = true;
				}
			}
		}
		
		return bResult;
	}
	
	
	public int getVariablePureNumber(String variableValue) throws Exception {
		if (variableValue == null || variableValue.length() == 0) {
			return 0;
		}
		
		int iResult = 0;
		
		if (StringUtil.checkSurroundText(variableValue, "LEN(", ")", true)) {
			String variableName = StringUtil.getStringAfterRemovingSurroundedText(variableValue, "LEN(", ")", true);
			Variable varObj = CommandController.varMap.get(variableName);
			
			if (varObj != null && varObj instanceof FileVariable) {
				FileVariable fileVarObj = (FileVariable) varObj;
				if (fileVarObj.getFileContent() != null && fileVarObj.getFileContent().size() > 0) {
					iResult = fileVarObj.getFileContent().size();
				} else {
					iResult = 0;
					
				}
			} else {
				throw new MsgException("[" + variableName + "] is undefined variable.");
			}
			
		} else if (StringUtil.checkOnlyNumbers(variableValue)) {
			return StringUtil.parseInt(variableValue);
			
		} else {
			// 넘버변수일 경우
			Variable indexVarObj = CommandController.varMap.get(variableValue);
			if (indexVarObj != null) {
				if (indexVarObj instanceof NumberVariable) {
					NumberVariable numberVarObj = (NumberVariable) indexVarObj;
					iResult = numberVarObj.getNumberValue();
				} else if (indexVarObj instanceof ForVariable) {
					ForVariable forVarObj = (ForVariable) indexVarObj;
					iResult = forVarObj.getForCurrentValue();
				}
			}
		}
		
		return iResult;
	}
	
	
	public String getVariablePureText(String variableValue, int lineNumber) throws Exception {
		if (variableValue == null || variableValue.length() == 0) {
			return "";
		}
		
		String resultValue = "";
		
		// 오직 숫자로 이뤄졌을 경우 변수가 아니다.
		if (StringUtil.checkOnlyNumbers(variableValue)) {
			return variableValue;
		}
		
		// 쌍따옴표로 둘러싸여 있는 문자열의 경우
		if (StringUtil.checkSurroundDoubleQuotes(variableValue)) {
			String pureText = StringUtil.getStringAfterRemovingSurroundedDoubleQuotes(variableValue);
			if (pureText != null && pureText.length() > 0) {
				resultValue = pureText;
			}
		} else {
			// 대괄호(FILE의 인덱스값)가 포함되어 있는 경우
			int leftBigBracket = variableValue.indexOf("[", 1); // 1부터 자르는 이유는 변수명이 있어야 하기 때문이다.
			int rightBigBracket = variableValue.indexOf("]", leftBigBracket + 1);
			if (leftBigBracket > -1 && rightBigBracket > -1) {
				String targetVariableName = variableValue.substring(0, leftBigBracket).trim();
				String targetIndexText = variableValue.substring(leftBigBracket + 1, rightBigBracket).trim();
				
				if (targetVariableName != null && targetVariableName.length() > 0) {
					if (targetIndexText != null && targetIndexText.length() > 0) {
						
						Variable varObj = CommandController.varMap.get(targetVariableName);
						if (varObj != null && varObj instanceof FileVariable) {
							FileVariable fileVarObj = (FileVariable) varObj;
							if (fileVarObj.getFileContent() != null && fileVarObj.getFileContent().size() > 0) {
								
								// 인덱스를 숫자로 바꿔준다.
								int iTargetIndexText = this.getVariablePureNumber(targetIndexText);
								
								int variableIndex = StringUtil.parseInt(iTargetIndexText);
								int lastIndex = fileVarObj.getFileContent().size() - 1;
								if (variableIndex > lastIndex) {
									throw new MsgException("The variable's index is out of bounds. variableName == [" + targetVariableName + "] / variableIndex == [" + variableIndex + "] / lastIndex == [" + lastIndex + "]");
								}
								
								String pureText = fileVarObj.getFileContent().get(variableIndex);
								if (pureText != null && pureText.length() > 0) {
									resultValue = pureText;
								}
								
							} else {
								// 타겟변수가 비어있다.
							}
						} else {
							// 타겟변수를 찾지 못했다.
						}
						
					}
				}
			} else {
				// 일반 변수로 판단한다.
				Variable varObj = CommandController.varMap.get(variableValue);
				if (varObj != null && varObj instanceof TextVariable) {
					TextVariable textVarObj = (TextVariable) varObj;
					String pureText = textVarObj.getTextValue();
					if (pureText != null && pureText.length() > 0) {
						resultValue = pureText;
					}
				}
			}
		}
		
		return resultValue;
	}
	
	
	
	public String reviseInputKeyCode(String originKeyCode) {
		String inputKeyCode = originKeyCode;
		
		// 쌍따옴표로 둘러싸여 있고 플러스 기호(+)가 포함되어 있는 경우
		// 문자열을 재조립한다.
		// [AS-IS] "ALT+F4"
		// [TO-BE] "ALT"+"F4"
		if (StringUtil.checkSurroundDoubleQuotes(originKeyCode)) {
			if (originKeyCode.indexOf("+") > -1) {
				String pureText = StringUtil.getStringAfterRemovingSurroundedDoubleQuotes(originKeyCode);
				if (pureText != null && pureText.length() > 0) {
					pureText = pureText.replace("\"", "");
					if (pureText != null && pureText.length() > 0) {
						StringBuffer buff = new StringBuffer();
						String[] arr = pureText.split("\\+");
						int arrCount = arr.length;
						for (int p=0; p<arrCount; p++) {
							if (arr[p] == null || arr[p].length() == 0) {
								continue;
							} else {
								arr[p] = arr[p].trim();
							}
							
							if (buff.length() > 0) {
								buff.append("+");
							}
							
							buff.append("\"");
							buff.append(arr[p]);
							buff.append("\"");
						}
						
						if (buff.length() > 0) {
							inputKeyCode = buff.toString();
						}
					}
				}
			}
		}
		
		return inputKeyCode;
	}
}