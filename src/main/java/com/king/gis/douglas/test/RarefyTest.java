package com.king.gis.douglas.test;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.douglas.Point;
import com.king.gis.douglas.Rarefy;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.data.Data;
import com.king.gis.util.data.StaticData;

public class RarefyTest {
	public static Logger LOGGER = BeanUtil.getLogger(RarefyTest.class);
	public static Data data = new StaticData(); 
	
	public static void main(String[] args) {
		List<Point> trip = data.getTrip(null);
		LOGGER.info("原始坐标数:" + trip.size());
		List<Point> newTrip = Rarefy.tripRarefy(trip);
		LOGGER.info("抽稀后坐标数:" + newTrip.size());
	}

}
