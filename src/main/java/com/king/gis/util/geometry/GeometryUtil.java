package com.king.gis.util.geometry;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.douglas.Point;
import com.king.gis.util.BeanUtil;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * @Title: GeometryUtil.java
 * @Package com.king.gis.util
 * @Description: 三点夹角计算
 * @author hdwmp123@163.com
 * @date 2014-12-3 下午3:55:52
 * @version V1.0
 */
public class GeometryUtil {
	private static Logger LOGGER = BeanUtil.getLogger(GeometryUtil.class);
	//
	public static Double LIMIT_ANGLE = 170D;// 夹角限定,小于此夹角则进行抽稀

	//
	/**
	 * 计算夹角
	 * 
	 * @param trip
	 *            行程
	 * @param tindex
	 *            索引
	 * @return
	 */
	public static Double calculateTheAngle(List<Point> trip, int tindex) {
		Double angle = null;
		Point pointA = null;
		Point pointB = null;
		Point pointC = null;

		pointA = trip.get(tindex - 2);
		pointB = trip.get(tindex - 1);
		pointC = trip.get(tindex);

		angle = getAngle(pointA, pointB, pointC);
		return angle;
	}

	/**
	 * 计算 线段ab与 bc的夹角
	 * 
	 * @param pointA
	 * @param pointB
	 * @param pointC
	 * @return
	 */
	private static Double getAngle(Point pointA, Point pointB, Point pointC) {
		Double angle = null;
		Double aL = null;
		Double bL = null;
		Double cL = null;

		aL = getDistance(pointB, pointC);
		bL = getDistance(pointC, pointA);
		cL = getDistance(pointA, pointB);

		angle = (aL * aL + cL * cL - bL * bL) / (2 * aL * cL);
		LOGGER.info(String.format("角度1:%s", angle));
		angle = Math.acos(angle) * 180 / Math.PI;
		LOGGER.info(String.format("角度2:%s", angle));
		return angle;
	}

	private static Coordinate getCoordinate(Point point) {
		return new Coordinate(point.getX(), point.getY(), 0);
	}

	/**
	 * 获取起始点到目标点直线距离
	 * 
	 * @param start
	 *            起始点
	 * @param end
	 *            目标点
	 * @return
	 */
	private static Double getDistance(Point start, Point end) {
		Coordinate startC = getCoordinate(start);
		Coordinate endC = getCoordinate(end);
		return startC.distance(endC);
	}

	/**
	 * 获取C点到到线段AB的最短距离
	 * 
	 * @param pointA
	 * @param pointB
	 * @param pointC
	 * @return
	 */
	public static Double getLength(Point pointA, Point pointB, Point pointC) {
		Double length = null;

		Double bL = getDistance(pointA, pointC);
		Double cL = getDistance(pointB, pointC);
		Double aL = getDistance(pointA, pointB);

		if (cL + bL == aL) {// 点在线段上
			length = (double) 0;
		} else if (cL * cL >= aL * aL + bL * bL) {// 钝角三角形，投影在pointA延长线上，
			length = bL;
		} else if (bL * bL >= aL * aL + cL * cL) {// 钝角三角形，投影在pointB延长线上，
			length = cL;
		} else {
			// 组成锐角三角形，则求三角形的高
			double p = (aL + bL + cL) / 2;// 半周长
			double s = Math.sqrt(p * (p - aL) * (p - bL) * (p - cL));// 海伦公式求面积
			length = 2 * s / aL;// 返回点到线的距离（利用三角形面积公式求高）
		}

		return length;
	}

	public static void main(String[] args) {
		Double length = null;
		Double angle = null;
		Point pointA = null;
		Point pointB = null;
		Point pointC = null;
		//
		pointA = new Point(0, 3);
		pointB = new Point(4, 0);
		pointC = new Point(0, 0);
		length = getLength(pointA, pointB, pointC);// 勾股定理
		angle = getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("勾股定理,距离:%s,角度:%s", length, angle));
		//
		pointA = new Point(0, 3);
		pointB = new Point(3, 0);
		pointC = new Point(0, 0);
		length = getLength(pointA, pointB, pointC);// 等腰
		angle = getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("等腰,距离:%s,角度:%s", length, angle));
		//
		pointA = new Point(3, 3);
		pointB = new Point(4, 0);
		pointC = new Point(0, 0);
		length = getLength(pointA, pointB, pointC);// 锐角
		angle = getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("锐角,距离 :%s,角度:%s", length, angle));
		//
		pointA = new Point(3, 1);
		pointB = new Point(10, 0);
		pointC = new Point(0, 0);
		length = getLength(pointA, pointB, pointC);// 钝角
		angle = getAngle(pointA, pointB, pointC);
		LOGGER.info(String.format("钝角 ,距离:%s,角度:%s", length, angle));
	}

}
