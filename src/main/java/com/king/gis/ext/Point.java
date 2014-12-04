package com.king.gis.ext;

public class Point extends Geometry {
	double x;
	double y;
	double z;

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

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Point(double xx, double yy, double zz) {
		this.x = xx;
		this.y = yy;
		this.z = zz;
	}

	public Point(double xx, double yy) {
		this.x = xx;
		this.y = yy;
		this.z = 0;
	}

	public Point() {
	}

	@Override
	public boolean equals(Object obj) {
		Point pt = (Point) obj;
		return (this.x == pt.getX() && this.y == pt.getY());
	}
}
