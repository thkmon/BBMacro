package com.thkmon.bbmacro.prototype;

public class Rect {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	
	
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public int getMiddleX() {
		if (this.width == 0) {
			return this.x;
		}
		
		int halfWidth = 0;
		if (this.width % 2 == 0) {
			halfWidth = this.width / 2;
		} else {
			halfWidth = (this.width + 1) / 2;
		}
		
		return this.x + halfWidth;
	}
	
	
	public int getMiddleY() {
		if (this.height == 0) {
			return this.y;
		}
		
		int halfHeight = 0;
		if (this.height % 2 == 0) {
			halfHeight = this.height / 2;
		} else {
			halfHeight = (this.height + 1) / 2;
		}
		
		return this.y + halfHeight;
	}
	
	
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
}