package com.king.gis.util;

import java.util.List;

import org.slf4j.Logger;

import com.king.gis.douglas.Point;
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
		Double angle;
		Point aP = null;
		Point bP = null;
		Point cP = null;

		Double aL;
		Double bL;
		Double cL;
		aP = trip.get(tindex - 2);
		bP = trip.get(tindex - 1);
		cP = trip.get(tindex);

		aL = distance(bP, cP);
		bL = distance(cP, aP);
		cL = distance(aP, bP);

		angle = (aL * aL + cL * cL - bL * bL) / (2 * aL * cL);
		LOGGER.debug("夹角1:" + angle);
		angle = Math.acos(angle) * 180 / Math.PI;
		LOGGER.debug("夹角2:" + angle);
		return angle;
	}

	private static Coordinate getCoordinate(Point point) {
		return new Coordinate(point.getX(), point.getY(), 0);
	}

	/**
	 * 获取起始点到目标点距离
	 * 
	 * @param start
	 *            起始点
	 * @param end
	 *            目标点
	 * @return
	 */
	private static Double distance(Point start, Point end) {
		Coordinate startC = getCoordinate(start);
		Coordinate endC = getCoordinate(end);
		return startC.distance(endC);
	}

	// ########################################################
	/**
	 * 获取两个坐标之间的距离
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return
	 */
	private static Double getLenWithPoints(double p1x, double p1y, double p2x,
			double p2y) {
		Double length = null;
		length = Math.sqrt(Math.pow(p2x - p1x, 2) + Math.pow(p2y - p1y, 2));
		return length;
	}

	/**
	 * 获取点到线段的最短距离
	 * 
	 * @param lx1
	 * @param ly1
	 * @param lx2
	 * @param ly2
	 * @param px
	 * @param py
	 * @return
	 */
	public static Double getLength(double lx1, double ly1, double lx2,
			double ly2, double px, double py) {
		Double length = null;
		double b = getLenWithPoints(lx1, ly1, px, py);
		double c = getLenWithPoints(lx2, ly2, px, py);
		double a = getLenWithPoints(lx1, ly1, lx2, ly2);

		if (c + b == a) {// 点在线段上
			length = (double) 0;
		} else if (c * c >= a * a + b * b) {// 钝角三角形，投影在point1延长线上，
			length = b;
		} else if (b * b >= a * a + c * c) {// 钝角三角形，投影在point2延长线上，
			length = c;
		} else {
			// 组成锐角三角形，则求三角形的高
			double p = (a + b + c) / 2;// 半周长
			double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
			length = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		}

		return length;
	}

	public static void main(String[] args) {
		System.out.println(getLength(0, 3, 4, 0, 0, 0));// 勾股定理
		System.out.println(getLength(3, 3, 4, 0, 0, 0));// 锐角
		System.out.println(getLength(3, 1, 10, 0, 0, 0));// 钝角
	}

}
