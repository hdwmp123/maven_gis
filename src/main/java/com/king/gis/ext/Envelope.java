package com.king.gis.ext;

public class Envelope {

	double up;

	double down;

	double left;

	double right;

	public double getUp() {
		return this.up;
	}

	public void setUp(double up) {
		this.up = up;
	}

	public double getDown() {
		return this.down;
	}

	public void setDown(double down) {
		this.down = down;
	}

	public double getLeft() {
		return this.left;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getRight() {
		return this.right;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public Envelope(double u, double d, double l, double r) {
		this.up = u;
		this.down = d;
		this.left = l;
		this.right = r;
	}

	public Envelope() {
	}

}
