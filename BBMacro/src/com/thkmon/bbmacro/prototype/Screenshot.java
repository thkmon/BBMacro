package com.thkmon.bbmacro.prototype;

import java.awt.image.BufferedImage;

public class Screenshot {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage imageObj = null;
	
	
	public int getX() {
		return x;
	}
	
	
	public void setX(int x) {
		this.x = x;
	}
	
	
	public int getY() {
		return y;
	}
	
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public BufferedImage getImageObj() {
		return imageObj;
	}
	
	
	public void setImageObj(BufferedImage imageObj) {
		this.imageObj = imageObj;
	}
}