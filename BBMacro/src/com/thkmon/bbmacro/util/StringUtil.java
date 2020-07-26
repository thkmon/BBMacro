package com.thkmon.bbmacro.util;

public class StringUtil {
	
	
	/**
	 * 쌍따옴표로 둘러싸여 있는 문자열인지 검사
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkSurroundDoubleQuotes(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		if (str.substring(0, 1).equals("\"") && str.substring(str.length() - 1).equals("\"")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 쌍따옴표로 둘러싸여 있는 문자열인 경우, 둘러싼 쌍따옴표를 제거한 후 문자열 가져오기
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringAfterRemovingSurroundedDoubleQuotes(String str) {
		if (checkSurroundDoubleQuotes(str)) {
			return str.substring(1, str.length() - 1);
		}
		
		return str;
	}
	
	
	/**
	 * 숫자로만 이루어진 문자열인지 검사
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkOnlyNumbers(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		if (str.matches("[0-9]*")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 오류 없이 문자열을 숫자로 변환
	 * 
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj) {
		return parseInt(obj, 0);
	}
	
	
	/**
	 * 오류 없이 문자열을 숫자로 변환
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		
		int result = defaultValue;
		
		try {
			String str = obj.toString();
			if (str != null && str.length() > 0) {
				result = Integer.parseInt(str);
			}
			
		} catch (Exception e) {
			return defaultValue;
		}
		
		return result;
	}
	
	
	/**
	 * 쌍따옴표를 고려한 split 처리.
	 * 쌍따옴표 안에 공백을 넣는 경우를 고려하여 작성함.
	 * 
	 * @param str
	 * @return
	 */
	public static String[] splitConsideringDoubleQuotes(String str) {
		
		// 배열 일부러 10칸 정도로 넉넉히 만든다. 일일히 널포인터 예외처리하는 비용보다 이게 싸다고 생각함.
		// 10칸까지는 안심하고 접근 가능.
		int limitCount = 10;
		String[] resultArr = new String[limitCount];
		for (int i=0; i<limitCount; i++) {
			resultArr[i] = "";
		}
		
		if (str == null || str.length() == 0) {
			return resultArr;
		}
		
		boolean bInDoubleQuote = false;
		
		int maxIndex = limitCount - 1;
		int curIndex = 0;
		int axisIndex = 0;
		
		String oneChar = "";
		int len = str.length();
		for (int i=0; i<len; i++) {
			// 한계값을 넘어가면 빠져나간다. ArrayIndexOutOfBoundsException 방지
			if (curIndex > maxIndex) {
				break;
			}
			
			oneChar = str.substring(i, i+1);
			
			if (!bInDoubleQuote && (oneChar.equals(" ") || oneChar.equals("\t"))) {
				String temp = str.substring(axisIndex, i);
				temp = temp.trim();
				if (temp.length() > 0) {
					resultArr[curIndex] = temp;
					curIndex++;
					axisIndex = i + 1;
				}
				
			} else if (oneChar.equals("\"")) {
				bInDoubleQuote = !bInDoubleQuote;
				continue;
			}
		}
		
		// 마지막 슬라이스 처리
		String temp = str.substring(axisIndex);
		temp = temp.trim();
		if (temp.length() > 0) {
			resultArr[curIndex] = temp;
		}
		
		return resultArr;
	}
	
	
	/**
	 * 쌍따옴표를 고려한 indexOf. 쌍따옴표 바깥의 문자열만 찾는다.
	 * 
	 * @param str
	 * @return
	 */
	public static int indexOfConsideringDoubleQuotes(String str, String sliceToFind, boolean bIgnoreCase) {
		
		if (str == null || str.length() == 0) {
			return -1;
		}
		
		if (sliceToFind == null || sliceToFind.length() == 0) {
			return -1;
		}
		
		int lenToFind = sliceToFind.length();
		if (bIgnoreCase) {
			sliceToFind = sliceToFind.toLowerCase();
		}
		
		boolean bInDoubleQuote = false;
		
		String oneChar = "";
		int len = str.length();
		int strLastIndex = len - 1;
		for (int i=0; i<len; i++) {
			oneChar = str.substring(i, i+1);
			
			if (oneChar.equals("\"")) {
				bInDoubleQuote = !bInDoubleQuote;
				continue;
			}
			
			// substring 오류 방지
			if ((i + lenToFind > strLastIndex)) {
				continue;
			}
			
			if (!bInDoubleQuote) {
				if (bIgnoreCase) {
					String slice = str.substring(i, i + lenToFind).toLowerCase();
					if (slice.equals(sliceToFind)) {
						return i;
					}
				} else {
					String slice = str.substring(i, i + lenToFind);
					if (slice.equals(sliceToFind)) {
						return i;
					}
				}
			}
		}
		
		
		return -1;
	}
	
	/**
	 * 스트링 배열을 스트링으로 변환. 출력용 메서드임.
	 * 
	 * @param strArr
	 * @return
	 */
	public static String toStringFromArray(String[] strArr) {
		if (strArr == null) {
			return "null";
		}
		
		if (strArr.length == 0) {
			return "empty";
		}
		
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		
		int len = strArr.length;
		for (int i=0; i<len; i++) {
			if (buff.length() > 1) {
				buff.append(", ");
			}
			
			if (strArr[i] != null) {
				buff.append("\"");
				buff.append(strArr[i]);
				buff.append("\"");
				
			} else {
				buff.append("null");
			}
			
		}
		
		buff.append("]");
		return buff.toString();
	}
	
	
	public static boolean checkSurroundText(String str, String mark1, String mark2, boolean bIgnoreCase) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		if (mark1 == null) {
			mark1 = "";
		}
		
		if (mark2 == null) {
			mark2 = "";
		}
		
		boolean bLeftMatch = false;
		boolean bRightMatch = false;
		
		try {
			String leftSlice = str.substring(0, mark1.length());
			if (bIgnoreCase && mark1.equalsIgnoreCase(leftSlice)) {
				bLeftMatch = true;
				
			} else if (!bIgnoreCase && mark1.equals(leftSlice)) {
				bLeftMatch = true;
				
			} else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		
		try {
			String rightSlice = str.substring(str.length() - mark2.length());
			if (bIgnoreCase && mark2.equalsIgnoreCase(rightSlice)) {
				bRightMatch = true;
				
			} else if (!bIgnoreCase && mark2.equals(rightSlice)) {
				bRightMatch = true;
				
			} else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		
		if (bLeftMatch && bRightMatch) {
			return true;
		}
		
		return false;
	}
	
	
	public static String getStringAfterRemovingSurroundedText(String str, String mark1, String mark2, boolean bIgnoreCase) {
		if (str == null || str.length() == 0) {
			return str;
		}
		
		if (mark1 == null) {
			mark1 = "";
		}
		
		if (mark2 == null) {
			mark2 = "";
		}
		
		if (!checkSurroundText(str, mark1, mark2, bIgnoreCase)) {
			return str;
		}
		
		String result = str;
		
		try {
			result = result.substring(mark1.length(), result.length() - mark2.length());
			
		} catch (Exception e) {
			return str;
		}
		
		return result;
	}
	
	
}