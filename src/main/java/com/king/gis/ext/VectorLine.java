package com.king.gis.ext;

public class VectorLine {

	double paraA;

	double paraB;

	double paraC;

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

	public VectorLine() {
	}

	public VectorLine(Point fp, Point tp) {
		this.paraA = fp.getY() - tp.getY();
		this.paraB = tp.getX() - fp.getX();
		this.paraC = fp.getX() * tp.getY() - fp.getY() * tp.getX();
	}

	public VectorLine(double pa, double pb, double pc) {
		this.paraA = pa;
		this.paraB = pb;
		this.paraC = pc;
	}
}
