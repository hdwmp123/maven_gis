package com.king.gis.douglas;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.king.gis.util.BeanUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * @Title: Douglas.java
 * @Package com.king.gis.douglas
 * @Description: 线路坐标抽稀算法
 * @author hdwmp123@163.com
 * @date 2014-12-3 下午4:02:28
 * @version V1.0
 */
public class Douglas {
	private static Logger LOGGER = BeanUtil.getLogger(Douglas.class);
	/**
	 * 存储采样点数据的链表
	 */
	public List<Point> points = new ArrayList<Point>();
	/**
	 * 控制数据压缩精度的极差
	 */
	private static final double D = 0.00009;
	int totalCount;

	// ##########################################################################
	/**
	 * 对矢量曲线进行压缩(核心算法)
	 * 
	 * @param from
	 *            曲线的起始点
	 * @param to
	 *            曲线的终止点
	 */
	public void compress(Point from, Point to) {

		/**
		 * 压缩算法的开关量
		 */
		boolean switchvalue = false;

		/**
		 * 由起始点和终止点构成的直线方程一般式的系数
		 */
		double A = (from.getY() - to.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));

		/**
		 * 由起始点和终止点构成的直线方程一般式的系数
		 */
		double B = (to.getX() - from.getX())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));

		/**
		 * 由起始点和终止点构成的直线方程一般式的系数
		 */
		double C = (from.getX() * to.getY() - to.getX() * from.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));

		double d = 0;
		double dmax = 0;
		int m = this.points.indexOf(from);
		int n = this.points.indexOf(to);
		if (n == m + 1) {
			return;
		}
		Point middle = null;
		List<Double> distance = new ArrayList<Double>();
		for (int i = m + 1; i < n; i++) {
			d = Math.abs(A * (this.points.get(i).getX()) + B
					* (this.points.get(i).getY()) + C)
					/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
			distance.add(d);
		}
		dmax = distance.get(0);
		for (int j = 1; j < distance.size(); j++) {
			if (distance.get(j) > dmax) {
				dmax = distance.get(j);
			}
		}
		if (dmax > D) {
			switchvalue = true;
		} else {
			switchvalue = false;
		}
		if (!switchvalue) {
			// 删除Points(m,n)内的坐标
			for (int i = m + 1; i < n; i++) {
				this.points.get(i).setIndex(-1);
			}

		} else {
			for (int i = m + 1; i < n; i++) {
				if ((Math.abs(A * (this.points.get(i).getX()) + B
						* (this.points.get(i).getY()) + C)
						/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)) == dmax)) {
					middle = this.points.get(i);
				}
			}
			compress(from, middle);
			compress(middle, to);
		}
	}

	/**
	 * 设置点集合
	 * 
	 * @param posList
	 * @param startIndex
	 * @param endIndex
	 */
	public void setPoints(List<Point> posList, int startIndex, int endIndex) {
		Point positionVo;
		int index = 0;
		for (int i = startIndex; i < endIndex; i++) {
			positionVo = posList.get(i);
			Point p = new Point(positionVo.getX(), positionVo.getY(), index++);
			this.points.add(p);
		}
	}

	/**
	 * 抽稀
	 * 
	 * @param posList
	 *            需要抽稀的坐标集合
	 * @param startIndex
	 *            开始索引
	 * @param endIndex
	 *            结束索引
	 * @return
	 */
	public List<Point> rarefy(List<Point> posList, int startIndex, int endIndex) {
		this.points = new ArrayList<Point>();
		setPoints(posList, startIndex, endIndex);
		this.totalCount = this.totalCount + this.points.size();
		LOGGER.info("总和：" + this.totalCount);
		LOGGER.info("抽稀前：" + this.points.size());
		if (this.points.size() > 1) {
			compress(this.points.get(0),
					this.points.get(this.points.size() - 1));
		}
		List<Point> returnPosList = new ArrayList<Point>();
		for (int i = 0; i < this.points.size(); i++) {
			Point p = this.points.get(i);
			if (p.getIndex() > -1) {
				returnPosList.add(p);
			}
		}
		LOGGER.info("抽稀后：" + returnPosList.size());
		LOGGER.info("-------------------------------");
		return returnPosList;
	}

	// ##########################################################################
	// 测试
	private WKTReader reader;

	/**
	 * 构造Geometry
	 * 
	 * @param str
	 * @return
	 */
	public Geometry buildGeo(String str) {
		try {
			if (this.reader == null) {
				this.reader = new WKTReader();
			}
			return this.reader.read(str);
		} catch (ParseException e) {
			throw new RuntimeException("buildGeometry Error", e);
		}
	}

	/**
	 * 读取采样点
	 */
	public void readPointDemo() {
		LOGGER.info("readPointDemo");
		Geometry g = buildGeo("LINESTRING (1 4,2 3,4 2,6 6,7 7,8 6,9 5,10 10)");
		Coordinate[] coords = g.getCoordinates();
		for (int i = 0; i < coords.length; i++) {
			Point p = new Point(coords[i].x, coords[i].y, i);
			this.points.add(p);
		}
	}

	public static void main(String[] args) {
		Douglas d = new Douglas();
		d.readPointDemo();
		d.compress(d.points.get(0), d.points.get(d.points.size() - 1));
		for (int i = 0; i < d.points.size(); i++) {
			Point p = d.points.get(i);
			if (p.getIndex() > -1) {
				System.out.println(p.getX() + " " + p.getY() + ",");
			}
		}
	}
	// ##########################################################################
}
