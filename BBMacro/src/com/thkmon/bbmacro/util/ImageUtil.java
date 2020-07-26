package com.thkmon.bbmacro.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
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
		if (screenImageList != null && screenImageList.size() > 0) {
			int screenCount = screenImageList.size();
			for (int i=0; i<screenCount; i++) {
				Screenshot screenshot = screenImageList.get(i);
				BufferedImage screenImgObj = screenshot.getImageObj();
				Rect rect = findImageAndGetRect(screenImgObj, imgObj);
				if (rect != null) {
					result = new Rect(screenshot.getX() + rect.getX(), screenshot.getY() + rect.getY(), rect.getWidth(), rect.getHeight());
					break;
				}
			}
		}

		return result;
	}

	
	/*
	private static BufferedImage screenShot() {
		// 스크린샷하기
		BufferedImage screenImgObj = null; // 버퍼드이미지 선언
		
		try {
			// 해상도 구하기
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// 스크린샷 찍기
			Robot robot = new Robot();
			screenImgObj = robot.createScreenCapture(new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()));

			// 스크린샷 파일화
			ImageIO.write(screenImgObj, "bmp", new File("c:/a/res.bmp"));

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return screenImgObj;
	}
	*/
	
	
	private static ScreenshotList screenShotForMultiScreen() throws Exception {
		
		ScreenshotList screenshotList = new ScreenshotList();
		
		// 스크린샷하기
		BufferedImage screenImgObj = null; // 버퍼드이미지 선언
		
		// int imageIndex = 0;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for(GraphicsDevice curGs : gs) {
		      GraphicsConfiguration[] gc = curGs.getConfigurations();
		      for(GraphicsConfiguration curGc : gc)
		      {
		            Rectangle bounds = curGc.getBounds();
		           //  System.out.println(bounds.getX() + "," + bounds.getY() + " " + bounds.getWidth() + "x" + bounds.getHeight());
		            
		            try {
		    			// 해상도 구하기
		    			// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		    			// 스크린샷 찍기
		    			Robot robot = new Robot();
		    			// screenImgObj = robot.createScreenCapture(new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()));
		    			screenImgObj = robot.createScreenCapture(new Rectangle((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight()));
		    			
		    			if (screenImgObj != null) {
			    			// 스크린샷 파일화
			    			// ImageIO.write(screenImgObj, "bmp", new File("c:/a/res" + (imageIndex++) + ".bmp"));
		    				
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
	
	
	private static Rect findImageAndGetRect(BufferedImage bigImg, BufferedImage smImg) {
		Rect rect = null;
		
		int bigWidth = bigImg.getWidth();
		int bigHeight = bigImg.getHeight();
		
		int smallWidth = smImg.getWidth();
		int smallHeight = smImg.getHeight();
		
		boolean bFound = false;

		for (int xs = 0; xs < bigWidth - smallWidth; xs++) {
			for (int ys = 0; ys < bigHeight - smallHeight; ys++) {

				// System.out.println(xs+" "+ys+"를 검사");
				for (int i = xs; i < xs + smallWidth; i++) {
					for (int j = ys; j < ys + smallHeight; j++) {
						// System.out.println(i+" "+j);
						// 같으면
						int bigRGB = bigImg.getRGB(i, j);
						int smallRGB = smImg.getRGB(i - xs, j - ys);

						if (bigRGB == smallRGB) {
							bFound = true;
							continue;
						} else {
							bFound = false;
							break;
						}
					}
					
					if (bFound == false) {
						break;
					}
				}
				
				if (bFound == true) {
					// System.out.println("발견좌표 x : " + xs + " / y : " + ys + " / w : " + smImg.getWidth() + " / h : " + smImg.getHeight());
					rect = new Rect(xs, ys, smallWidth, smallHeight);
					break;
				}
			}
			
			if (bFound == true) {
				break;
			}
		}
		
		return rect;
	}
}
