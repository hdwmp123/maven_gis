package com.king.gis.match.test;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.douglas.Point;
import com.king.gis.match.RoadMatch;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.data.StaticData;
import com.king.gis.util.data.abs.Data;

public class RoadMatchTest {
	public static Logger LOGGER = BeanUtil.getLogger(RoadMatchTest.class);
	public static Data data = new StaticData();

	public static void main(String[] args) {
		List<Point> trip = data.getTrip(null);
		LOGGER.info("原始坐标数:" + trip.size());
		List<Point> newTrip = RoadMatch.roadMatch(trip);
		LOGGER.info("匹配后坐标数:" + newTrip.size());
	}
}
