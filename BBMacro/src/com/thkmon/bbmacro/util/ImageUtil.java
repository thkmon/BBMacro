package com.thkmon.bbmacro.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.thkmon.bbmacro.prototype.Rect;
import com.thkmon.bbmacro.prototype.Screenshot;
import com.thkmon.bbmacro.prototype.ScreenshotList;

public class ImageUtil {
	
	public static Rect findImageRectFromScreen(File fileObj) throws Exception {
		Rect result = null;
		
		// 그림1을 얻어옴
		BufferedImage imgObj = ImageIO.read(fileObj);

		// 스크린샷 찍음
		ScreenshotList screenImageList = screenShotForMultiScreen();
		if (screenImageList == null || screenImageList.size() < 2) {
			screenImageList = screenShotForSingleScreen();
		}
		
		if (screenImageList != null && screenImageList.size() > 0) {
			int screenCount = screenImageList.size();
			for (int i=0; i<screenCount; i++) {
				Screenshot screenshot = screenImageList.get(i);
				BufferedImage screenImgObj = screenshot.getImageObj();
				Rect rect = findImageAndGetRect(i, screenImgObj, imgObj, false);
				if (rect != null) {
					result = new Rect(screenshot.getX() + rect.getX(), screenshot.getY() + rect.getY(), rect.getWidth(), rect.getHeight());
					break;
				}
			}
		}

		return result;
	}

	
	private static ScreenshotList screenShotForSingleScreen() throws Exception {
		ScreenshotList screenshotList = new ScreenshotList();
		
		// 스크린샷하기
		BufferedImage screenImgObj = null;
		
		try {
			// 해상도 구하기
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// 스크린샷 찍기
			Robot robot = new Robot();
			screenImgObj = robot.createScreenCapture(new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()));

			// 스크린샷 파일화
			writeImage(screenImgObj, "screen", "bmp");

			Screenshot screenshot = new Screenshot();
			screenshot.setX(0);
			screenshot.setY(0);
			screenshot.setWidth((int) screenSize.getWidth());
			screenshot.setHeight((int) screenSize.getHeight());
			screenshot.setImageObj(screenImgObj);
			
			screenshotList.add(screenshot);
			
		} catch (Exception e) {
			throw e;
		}
		
		return screenshotList;
	}
	
	
	private static ScreenshotList screenShotForMultiScreen() throws Exception {
		
		ScreenshotList screenshotList = new ScreenshotList();
		
		// 스크린샷하기
		BufferedImage screenImgObj = null;
		
		int imageIndex = 0;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (GraphicsDevice curGs : gs) {
		      GraphicsConfiguration[] gc = curGs.getConfigurations();
		      for (GraphicsConfiguration curGc : gc) {
		            Rectangle bounds = curGc.getBounds();
		            
		            try {
		    			// 스크린샷 찍기
		    			Robot robot = new Robot();
		    			screenImgObj = robot.createScreenCapture(new Rectangle((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight()));
		    			
		    			if (screenImgObj != null) {
		    				// 스크린샷 파일화
		    				// writeImage(screenImgObj, "screen" + imageIndex, "bmp");
		    				
		    				imageIndex++;
		    				
		    				Screenshot screenshot = new Screenshot();
		    				screenshot.setX((int) bounds.getX());
		    				screenshot.setY((int) bounds.getY());
		    				screenshot.setWidth((int) bounds.getWidth());
		    				screenshot.setHeight((int) bounds.getHeight());
		    				screenshot.setImageObj(screenImgObj);
		    				
		    				screenshotList.add(screenshot);
		    			}
		    			
		    		} catch (Exception e) {
		    			throw e;
		    		}
		      }
		 }
		
		return screenshotList;
	}
	
	
	private static Rect findImageAndGetRect(int screenIndex, BufferedImage bigImg, BufferedImage smImg, boolean bCheckBlackOnly) {
		Rect rect = null;
		
		int bigWidth = bigImg.getWidth();
		int bigHeight = bigImg.getHeight();
		
		int smallWidth = smImg.getWidth();
		int smallHeight = smImg.getHeight();
		
		LogUtil.debug("screenIndex : " + screenIndex + " / bigWidth : " + bigWidth + " / bigHeight : " + bigHeight + " / smallWidth : " + smallWidth + " / smallHeight : " + smallHeight);
		
		boolean bFound = false;

		for (int xs = 0; xs < bigWidth - smallWidth; xs++) {
			for (int ys = 0; ys < bigHeight - smallHeight; ys++) {

				for (int i = 0; i < smallWidth; i++) {
					for (int j = 0; j < smallHeight; j++) {
						int bigImgRgb = bigImg.getRGB(xs + i, ys + j);
						int smallImgRgb = smImg.getRGB(i, j);
						
						Color bigColor = new Color(bigImgRgb);
						Color smallColor = new Color(smallImgRgb);
						
						if (bCheckBlackOnly) {
							boolean bBlack1 = (bigColor.getRed() == 0 && bigColor.getGreen() == 0 && bigColor.getBlue() == 0);
							boolean bBlack2 = (smallColor.getRed() == 0 && smallColor.getGreen() == 0 && smallColor.getBlue() == 0);
							
							if (bBlack1 && bBlack2) {
								bFound = true;
								continue;
							} else if (bBlack1 || bBlack2) {
								bFound = false;
								break;
							} else {
								bFound = true;
								continue;
							}
							
						} else {
							// 색상이 같은지 체크
							if (checkColorSame(bigColor, smallColor)) {
								bFound = true;
								continue;
								
							} else {
								bFound = false;
								break;
							}
						}
					}
					
					if (bFound == false) {
						break;
					}
				}
				
				if (bFound == true) {
					LogUtil.debug("discovered! x : " + xs + " / y : " + ys + " / w : " + smImg.getWidth() + " / h : " + smImg.getHeight());
					rect = new Rect(xs, ys, smallWidth, smallHeight);
					
//					try {
//						BufferedImage cropImgObj = bigImg.getSubimage(xs, ys, smallWidth, smallHeight);
//						writeImage(cropImgObj, "toclick", "bmp");
//					} catch (Exception e) {}
					
					break;
				}
			}
			
			if (bFound == true) {
				break;
			}
		}
		
		return rect;
	}
	
	
	private static void writeImage(BufferedImage screenImgObj, String fileName, String ext) {
		// 스크린샷 파일화
		if (ext == null || ext.length() == 0) {
			ext = "bmp";
		}
		
		try {
			File screenshotFile = new File(fileName + "." + ext);
			if (screenshotFile.exists()) {
				screenshotFile.delete();
			}
			
			ImageIO.write(screenImgObj, ext, screenshotFile);
		} catch (Exception e) {
			// 무시
		}
	}
	
	
	/**
	 * 색상이 같은지 체크
	 * 
	 * @param bigColor
	 * @param smallColor
	 * @return
	 */
	private static boolean checkColorSame(Color bigColor, Color smallColor) {
		if (bigColor == null || smallColor == null) {
			return false;
		}
		
		if (bigColor.getRed() == smallColor.getRed() && bigColor.getGreen() == smallColor.getGreen() && bigColor.getBlue() == smallColor.getBlue()) {
			return true;
		}
		
		return false;
	}
}