package com.king.gis.ext;

import java.util.ArrayList;
import java.util.List;

public class Map {
	List<Geometry> geos = new ArrayList<Geometry>();

	public List<Geometry> getGeos() {
		return this.geos;
	}

	public void setGeos(List<Geometry> geos) {
		this.geos = geos;
	}

	public void add(Geometry geo) {
		this.geos.add(geo);
	}

	public Geometry get(int idex) {
		return this.geos.get(idex);
	}

	public Map() {
	}
}
