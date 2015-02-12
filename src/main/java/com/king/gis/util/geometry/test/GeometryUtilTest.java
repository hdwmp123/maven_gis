package com.king.gis.util.geometry.test;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.douglas.Point;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.data.FromDbData;
import com.king.gis.util.data.abs.Data;
import com.king.gis.util.geometry.GeometryUtil;

public class GeometryUtilTest {
	private static Logger LOGGER = BeanUtil.getLogger(GeometryUtilTest.class);
	
	public static Data data = new FromDbData();

	public static void main(String[] args) {
		// testAngle();
		// testDistance();
		testPoint2Line();
	}

	private static void testPoint2Line() {
		Point point = new Point(116.450262, 39.990378);
		List<Point> line = data.getTrip("select * from sc_mnt_trip a limit 0,1");
		Double d = GeometryUtil.getPoint2Line(point, line);
	}

	private static void testDistance() {
		Point start = new Point(10, 20);
		Point end = new Point(30, 50);
		Double d = GeometryUtil.getDistance(start, end);
		LOGGER.info(String.format("距离%s", d));
	}

	private static void testAngle() {
		Double length = null;
		Double angle = null;
		Point pointA = null;
		Point pointB = null;
		Point pointC = null;
		//
		pointA = new Point(0, 3);
		pointB = new Point(4, 0);
		pointC = new Point(0, 0);
		length = GeometryUtil.getLength(pointA, pointB, pointC);// 勾股定理
		angle = GeometryUtil.getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("勾股定理,距离:%s,角度:%s", length, angle));
		//
		pointA = new Point(0, 3);
		pointB = new Point(3, 0);
		pointC = new Point(0, 0);
		length = GeometryUtil.getLength(pointA, pointB, pointC);// 等腰
		angle = GeometryUtil.getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("等腰,距离:%s,角度:%s", length, angle));
		//
		pointA = new Point(3, 3);
		pointB = new Point(4, 0);
		pointC = new Point(0, 0);
		length = GeometryUtil.getLength(pointA, pointB, pointC);// 锐角
		angle = GeometryUtil.getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("锐角,距离 :%s,角度:%s", length, angle));
		//
		pointA = new Point(3, 1);
		pointB = new Point(10, 0);
		pointC = new Point(0, 0);
		length = GeometryUtil.getLength(pointA, pointB, pointC);// 钝角
		angle = GeometryUtil.getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("钝角 ,距离:%s,角度:%s", length, angle));
	}
}
