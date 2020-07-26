package com.thkmon.bbmacro.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.thkmon.bbmacro.common.CommonConst;

public class RobotUtil {
	
	
	private static Robot robot = null;
	
	
	public static boolean moveMouse(int x, int y) throws AWTException {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.mouseMove(x, y);
		return true;
	}
	
	
	public static boolean clickMouseLeft(int x, int y) throws AWTException {
		moveMouse(x, y);
		return clickMouseLeft();
	}
	
	
	public static boolean clickMouseRight(int x, int y) throws AWTException {
		moveMouse(x, y);
		return clickMouseRight();
	}
	
	
	public static boolean clickMouseLeft() throws AWTException {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		return true;
	}
	
	
	public static boolean clickMouseRight() throws AWTException {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
		return true;
	}
	
	
	public static boolean pressKey(int keycode) throws AWTException {
		if (robot == null) {
			robot = new Robot();
		}
		
		robot.keyPress(keycode);
		
		robot.keyRelease(keycode);
		robot.delay(CommonConst.defaultDelay);
		return true;
	}
	
	
	public static boolean pressKey(int keycode1, int keycode2) throws AWTException {
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
	
	
	public static boolean pressKey(int keycode1, int keycode2, int keycode3) throws AWTException {
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
	
	
	public static boolean pressKeyCtrlV() throws AWTException {
		return pressKey(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
	}
	
	
	public static boolean pressKeyAltF4(int delay) throws AWTException {
		return pressKey(KeyEvent.VK_ALT, KeyEvent.VK_F4);
	}
}
