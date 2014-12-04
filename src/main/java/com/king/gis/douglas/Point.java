package com.king.gis.douglas;

/**
 * 
 * @Title: Point.java
 * @Package com.king.gis.douglas
 * @Description: 坐标定义
 * @author hdwmp123@163.com
 * @date 2014-12-3 下午4:02:41
 * @version V1.0
 */
public class Point {
	/**
	 * 点的X坐标
	 */
	private double x = 0F;
	/**
	 * 点的Y坐标
	 */
	private double y = 0F;

	/**
	 * 点所属的曲线的索引
	 */
	private int index = 0;

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 点数据的构造方法
	 * 
	 * @param x
	 *            点的X坐标
	 * @param y
	 *            点的Y坐标
	 * @param index点所属的曲线的索引
	 */
	public Point(double x, double y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
	}

	@Override
	public String toString() {
		return "x= " + this.x + "  y= " + this.y;
	}
}
