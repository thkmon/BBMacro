package com.thkmon.bbmacro.main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class MainClass {

	// 한글 암호입력창일 경우 닫기 옵션일 경우
	public static boolean optionCloseHwpPwd = false;
	
	// 로봇 객체
	public static Robot robot = null;

	
	/**
	 * 메인 함수. IE Checker 를 실행한다.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		double mx = 1000;
		double my = 1000;

		int targetHandleCount = 1;
		int sleepMilSecond = 200;
		
		try {
			System.out.println("IE Checker Run.");
			
			targetHandleCount = getArgumentInt(args, "handleCount");
			if (targetHandleCount < 1) {
				targetHandleCount = 1;
			}
			
			sleepMilSecond = getArgumentInt(args, "sleep");
			if (sleepMilSecond < 1) {
				sleepMilSecond = 200;
			}
			
			int closeHwpPwd = getArgumentInt(args, "closeHwpPwd");
			if (closeHwpPwd == 1) {
				optionCloseHwpPwd = true;
			}

			
			System.out.println("handleCount : " + targetHandleCount);
			System.out.println("sleep : " + sleepMilSecond);
			System.out.println("closeHwpPwd : " + optionCloseHwpPwd);
			
			robot = new Robot();

			System.out.println("Loop Begin.");
			
			// 마우스가 화면 좌상단 10, 10 이내이면 프로그램 종료.
			while (mx > 10 || my > 10) {
				Point point = MouseInfo.getPointerInfo().getLocation();
				mx = point.getLocation().getX();
				my = point.getLocation().getY();

				// printAllHandles();
				
				if (targetHandleCount > checkHandleCount("IEFrame")) {
					executeIEProcess();
				}
				
				Thread.sleep(sleepMilSecond);
			}
			
			System.out.println("Loop End.");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("IE Checker End.");
	}
	
	
	/**
	 * 아규먼트 배열에서 특정한 키값으로 이퀄(=) 우측의 숫자 값을 얻는다.
	 * 
	 * @param args
	 * @param nameToFind
	 * @return
	 */
	public static int getArgumentInt(String[] args, String nameToFind) {
		String str = getArgumentString(args, nameToFind);
		if (str == null || str.length() == 0) {
			return 0;
		}
		
		int result = 0;
		try {
			result = Integer.parseInt(str);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	/**
	 * 아규먼트 배열에서 특정한 키값으로 이퀄(=) 우측의 문자열 값을 얻는다.
	 * 
	 * @param args
	 * @param nameToFind
	 * @return
	 */
	public static String getArgumentString(String[] args, String nameToFind) {
		if (args == null || args.length == 0) {
			return "";
		}
		
		if (nameToFind == null || nameToFind.length() == 0) {
			return "";
		}
		
		String oneArg = "";
		int cnt = args.length;
		for (int i=0; i<cnt; i++) {
			oneArg = args[i];
			if (oneArg == null || oneArg.length() == 0) {
				continue;
			}
			
			if (oneArg.toLowerCase().startsWith(nameToFind.toLowerCase() + "=")) {
				int eqIdx = oneArg.indexOf("=");
				String result = oneArg.substring(eqIdx + 1);
				return result;
			}
		}
		
		return "";
	}

	
	/**
	 * 익스플로러를 실행한다.
	 * 
	 * @throws Exception
	 */
	public static void executeIEProcess() throws Exception {
		
		ArrayList list = new ArrayList();
		list.add("C:\\Program Files\\Internet Explorer\\iexplore.exe");
		// list.add("http://en.wikipedia.org/");

		ProcessBuilder pb = new ProcessBuilder(list);
		pb.start();
	}
	
	
	/**
	 * 현재 실행중인 모든 핸들의 정보를 출력한다. (로그출력용)
	 */
	/*
	public static void printAllHandles() {

		try {
			User32.INSTANCE.EnumWindows(new WNDENUMPROC() {

				public boolean callback(HWND hWnd, Pointer arg1) {
					char[] windowText = new char[512];
					User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
					String wText = Native.toString(windowText);
					RECT rectangle = new RECT();
					User32.INSTANCE.GetWindowRect(hWnd, rectangle);

					if (wText.isEmpty() ||
						!(User32.INSTANCE.IsWindowVisible(hWnd) &&
							rectangle.left > -32000)) {
						return true;
					}

					int count = 0;
					
					char[] c = new char[512];
					User32.INSTANCE.GetClassName(hWnd, c, 512);
					String clsName = String.valueOf(c).trim();

					StringBuffer buff = new StringBuffer();

					buff.append("번호:" + (++count));
					buff.append(",텍스트:" + wText);
					buff.append("," + "위치:(");
					buff.append(rectangle.left + "," + rectangle.top + ")~(");
					buff.append(rectangle.right + "," + rectangle.bottom);
					buff.append(")," + "클래스네임:" + clsName);

					System.out.println(buff.toString());

					return true;
				}
			}, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	/**
	 * 현재 실행 중인 핸들의 개수를 얻는다.
	 * 
	 * @param targetHandleName
	 * @return
	 */
	public static int checkHandleCount(String targetHandleName) {
		
		if (targetHandleName == null || targetHandleName.length() == 0) {
			return -1;
		}
		
		final String finHandleName = targetHandleName;
		
		final int[] handleCount = new int[1];
		handleCount[0] = 0;
		
		try {
			User32.INSTANCE.EnumWindows(new WNDENUMPROC() {
				public boolean callback(HWND hWnd, Pointer arg1) {
					char[] windowText = new char[512];
					User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
					String wText = Native.toString(windowText);
					RECT rectangle = new RECT();
					User32.INSTANCE.GetWindowRect(hWnd, rectangle);

					if (wText.isEmpty() ||
						!(User32.INSTANCE.IsWindowVisible(hWnd) &&
							rectangle.left > -32000)) {
						return true;
					}

					int count = 0;
					
					char[] c = new char[512];
					User32.INSTANCE.GetClassName(hWnd, c, 512);
					String clsName = String.valueOf(c).trim();

//					StringBuffer buff = new StringBuffer();
//
//					buff.append("번호:" + (++count));
//					buff.append(",텍스트:" + wText);
//					buff.append("," + "위치:(");
//					buff.append(rectangle.left + "," + rectangle.top + ")~(");
//					buff.append(rectangle.right + "," + rectangle.bottom);
//					buff.append(")," + "클래스네임:" + clsName);
//
//					System.out.println(buff.toString());
					
					if (clsName != null) {
						if (clsName.equals(finHandleName)) {
							handleCount[0]++;
						}
						
						// 한글 암호입력창일 경우 닫기 옵션일 경우
						if (optionCloseHwpPwd) {
							// 한글 암호입력창일 경우 닫기 시도한다.
							if (clsName.equals("HNC_DIALOG")) {
								// 셋포커스(잘 동작하지 않지만 그래도.)
								User32.INSTANCE.SetForegroundWindow(hWnd);
								
								// 현재 포커스된 창 얻기
								HWND curHwnd = User32.INSTANCE.GetForegroundWindow();
								if (curHwnd != null) {
									String curWinName = getClassNameFromHandle(curHwnd);
									
									// 현재 포커스된 창이 한글 암호입력 창이라면 종료
									if (curWinName != null && curWinName.equals("HNC_DIALOG")) {
										// 종료
										AltF4(200);
										return true;
									}
								}
							}
						}
					}

					return true;
				}
			}, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return handleCount[0];
	}
	
	
	/**
	 * 핸들의 이름을 얻는다.
	 * 
	 * @param hWnd
	 * @return
	 * @throws Exception
	 */
	public static String getClassNameFromHandle(HWND hWnd) {
		if (hWnd == null) {
			return "";
		}
		
		String clsName = "";
		
		try {
			// 핸들의 클래스 네임 얻기
			char[] c = new char[512];
			User32.INSTANCE.GetClassName(hWnd, c, 512);
			clsName = String.valueOf(c).trim();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clsName;
	}
	
	
	public static HWND castHwnd(Object obj) {
		try {
			return (HWND) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void AltF4(int delay) {
		try {
			if (robot == null) {
				robot = new Robot();
			}
			
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_F4);
			robot.keyRelease(KeyEvent.VK_F4);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.delay(delay);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
