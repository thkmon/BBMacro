package com.thkmon.bbmacro.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.thkmon.bbmacro.common.CommonConst;

public class RobotUtil {
	
	
	private static Robot robot = null;
	
	
	/**
	 * 로봇 객체 초기화
	 * 
	 * @throws AWTException
	 * @throws Exception
	 */
	public static void initRobot() throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
	}
	
	
	/**
	 * 마우스 이동
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean moveMouse(int x, int y) throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		PointerInfo pointerInfo = null;
		int realX = 0;
		int realY = 0;
		
		// 컴퓨터에 따라 마우스가 이동하지 않는 경우가 발생. 최대 10초까지 마우스 이동여부 검사
		for (int i=0; i<100; i++) {
			robot.mouseMove(x, y);
			
			pointerInfo = MouseInfo.getPointerInfo();
			realX = (int)pointerInfo.getLocation().getX();
			realY = (int)pointerInfo.getLocation().getY();
			
			// 마우스 이동 성공 시 break
			if (x == realX && y == realY) {
				break;
			}
			
			try {
				Thread.sleep(100);
			} catch (Exception e) {}
		}
		
		return true;
	}
	
	
	/**
	 * 마우스 이동 및 마우스 좌클릭
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean clickMouseLeft(int x, int y) throws AWTException, Exception {
		moveMouse(x, y);
		return clickMouseLeft();
	}
	
	
	/**
	 * 마우스 이동 및 마우스 우클릭
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean clickMouseRight(int x, int y) throws AWTException, Exception {
		moveMouse(x, y);
		return clickMouseRight();
	}
	
	
	/**
	 * 마우스 좌클릭
	 * 
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean clickMouseLeft() throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		return true;
	}
	
	
	/**
	 * 마우스 우클릭
	 * 
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean clickMouseRight() throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
		return true;
	}
	
	
	/**
	 * 키입력
	 * 
	 * @param keycode
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean pressKey(int keycode) throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.keyPress(keycode);
		
		robot.keyRelease(keycode);
		robot.delay(CommonConst.defaultDelay);
		return true;
	}
	
	
	/**
	 * 키입력. 2개 동시입력
	 * 
	 * @param keycode1
	 * @param keycode2
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean pressKey(int keycode1, int keycode2) throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.keyPress(keycode1);
		robot.keyPress(keycode2);
		
		robot.keyRelease(keycode2);
		robot.delay(CommonConst.defaultDelay);
		
		robot.keyRelease(keycode1);
		robot.delay(CommonConst.defaultDelay);
		return true;
	}
	
	
	/**
	 * 키입력. 3개 동시입력
	 * 
	 * @param keycode1
	 * @param keycode2
	 * @param keycode3
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean pressKey(int keycode1, int keycode2, int keycode3) throws AWTException, Exception {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.keyPress(keycode1);
		robot.keyPress(keycode2);
		robot.keyPress(keycode3);
		
		robot.keyRelease(keycode3);
		robot.delay(CommonConst.defaultDelay);
		
		robot.keyRelease(keycode2);
		robot.delay(CommonConst.defaultDelay);
		
		robot.keyRelease(keycode1);
		robot.delay(CommonConst.defaultDelay);
		return true;
	}
	
	
	/**
	 * 붙여넣기(Ctrl+v)
	 * 
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean pressKeyCtrlV() throws AWTException, Exception {
		return pressKey(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
	}
	
	
	/**
	 * 종료(Alt+F4)
	 * 
	 * @param delay
	 * @return
	 * @throws AWTException
	 * @throws Exception
	 */
	public static boolean pressKeyAltF4(int delay) throws AWTException, Exception {
		return pressKey(KeyEvent.VK_ALT, KeyEvent.VK_F4);
	}
}