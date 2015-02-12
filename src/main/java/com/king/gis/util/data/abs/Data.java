package com.king.gis.util.data.abs;

import java.util.ArrayList;
import java.util.List;

import com.king.gis.douglas.Point;

public abstract class Data {
	public abstract List<Point> getTrip(String sql);

	public abstract List<com.king.gis.ext.Point> getTrip2(String sql);

	public static List<com.king.gis.ext.Point> convert(List<Point> fromTrip) {
		List<com.king.gis.ext.Point> trip = null;
		com.king.gis.ext.Point point = null;
		//
		List<Point> tripTemp = fromTrip;
		Point tempPoint = null;
		if (tripTemp == null || tripTemp.size() == 0) {
			return null;
		}
		trip = new ArrayList<com.king.gis.ext.Point>();
		for (int j = 0; j < tripTemp.size(); j++) {
			tempPoint = tripTemp.get(j);
			point = new com.king.gis.ext.Point();
			point.setX(tempPoint.getX());
			point.setY(tempPoint.getY());
			trip.add(point);
		}
		return trip;
	}
}
