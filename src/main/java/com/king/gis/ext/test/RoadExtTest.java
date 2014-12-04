package com.king.gis.ext.test;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.ext.Point;
import com.king.gis.ext.RoadExt;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.data.Data;
import com.king.gis.util.data.StaticData;

public class RoadExtTest {
	public static Logger LOGGER = BeanUtil.getLogger(RoadExtTest.class);
	public static Data data = new StaticData(); 
	public static void main(String[] args) {
		List<Point> trip = data.getTrip2(null);
		List<Point> newTrip = null;
		LOGGER.info("原始坐标数:" + trip.size());
		newTrip = RoadExt.ext(trip);
		LOGGER.info("扩展坐标数:" + newTrip.size());
	}
}
