package com.king.gis.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Path extends Geometry {
	protected List<Line> path = new ArrayList<Line>();
	protected Point frompt, topt;

	public List<Line> getPath() {
		return this.path;
	}

	public void setPath(List<Line> path) {
		this.path = path;
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

	public Path(Line line) {
		this.path.add(line);
		this.frompt = line.getFrompt();
		this.topt = line.getTopt();
	}

	public Path() {
	}

	public double Length() {
		double len = 0;
		for (Line li : this.path) {
			len += li.Length();
		}
		return len;
	}

	public void add(Line line) {
		if (this.path.size() == 0) {
			this.frompt = line.getFrompt();
		}
		this.path.add(line);
		this.topt = line.getTopt();

	}

	@Override
	public Envelope getEnve() {
		List<Double> xs = new ArrayList<Double>();
		List<Double> ys = new ArrayList<Double>();
		for (Line li : this.path) {
			xs.add(li.getFrompt().getX());
			ys.add(li.getFrompt().getY());
		}
		xs.add(this.topt.getX());
		ys.add(this.topt.getY());
		// xs.sort();
		Collections.sort(xs, new Comparator<Double>() {
			@Override
			public int compare(Double a, Double b) {
				return a > b ? 0 : 1;
			}
		});
		// ys.sort();
		Collections.sort(ys, new Comparator<Double>() {
			@Override
			public int compare(Double a, Double b) {
				return a > b ? 0 : 1;
			}
		});
		this.enve.setLeft(xs.get(0));
		this.enve.setRight(xs.get(xs.size() - 1));
		this.enve.setDown(ys.get(0));
		this.enve.setUp(ys.get(ys.size() - 1));
		return this.enve;
	}

	public Line get(int index) {
		return this.path.get(index);
	}
}
