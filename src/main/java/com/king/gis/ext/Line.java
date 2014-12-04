package com.king.gis.ext;

public class Line extends Geometry {
	double paraA, paraB, paraC;
	Point frompt;
	Point topt;

	public double getParaA() {
		return this.paraA;
	}

	public void setParaA(double paraA) {
		this.paraA = paraA;
	}

	public double getParaB() {
		return this.paraB;
	}

	public void setParaB(double paraB) {
		this.paraB = paraB;
	}

	public double getParaC() {
		return this.paraC;
	}

	public void setParaC(double paraC) {
		this.paraC = paraC;
	}

	public Point getFrompt() {
		return this.frompt;
	}

	public void setFrompt(Point frompt) {
		this.frompt = frompt;
	}

	public Point getTopt() {
		return this.topt;
	}

	public void setTopt(Point topt) {
		this.topt = topt;
	}

	@Override
	public Envelope getEnve() {
		this.enve.setDown(Math.min(this.frompt.getY(), this.topt.getY()));
		this.enve.setUp(Math.max(this.frompt.getY(), this.topt.getY()));
		this.enve.setLeft(Math.min(this.frompt.getX(), this.topt.getX()));
		this.enve.setRight(Math.max(this.frompt.getX(), this.topt.getX()));
		return this.enve;
	}

	@Override
	public void setEnve(Envelope enve) {
		this.enve = enve;
	}

	public Line(Point fp, Point tp) {
		this.frompt = fp;
		this.topt = tp;
		this.paraA = tp.getY() - fp.getY();
		this.paraB = fp.getX() - tp.getX();
		this.paraC = tp.getX() * fp.getY() - fp.getX() * tp.getY();
	}

	public Line() {
	}

	public double Length() {
		double len = Math.sqrt((this.frompt.getX() - this.topt.getX())
				* (this.frompt.getX() - this.topt.getX())
				+ (this.frompt.getY() - this.topt.getY())
				* (this.frompt.getY() - this.topt.getY()));
		return len;
	}
}
