package com.king.gis.ext;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;

import com.king.gis.util.BeanUtil;

public class RoadExt {
	public static Logger LOGGER = BeanUtil.getLogger(RoadExt.class);
	static Map map = new Map();
	static Path path = new Path();
	static Line line = null;
	static Point fp = null;
	static Point tp = null;
	static double distance = 0.0001;// 扩展距离

	public static void main(String[] args) {
		//
		Point ptp = null;
		for (int i = 0; i < 10; i++) {
			tp = new Point();
			tp.setX(Math.random() * 10);
			tp.setY(Math.random() * 10);
			if (fp == null) {
				fp = new Point();
				fp.setX(Math.random() * 10);
				fp.setY(Math.random() * 10);
			} else {
				fp = ptp;
			}
			ptp = tp;
			line = new Line(fp, tp);
			path.add(line);
		}
		map.add(path);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "enve", "paraA", "paraB", "paraC" });
		System.out.println(JSONObject.fromObject(path, config));
		//
		List<Point> ring = new ArrayList<Point>();
		for (Geometry geo : map.getGeos()) {
			if (geo instanceof Path) {
				Path pa = (Path) geo;
				ring = bufferofPath(pa, distance);
			}
		}
		ring.clear();

	}

	private static List<Point> bufferofPath(Path pa, double distance) {
		List<Point> ring = new ArrayList<Point>();
		List<Point> lpath = new ArrayList<Point>();
		List<Point> rpath = new ArrayList<Point>();

		Point lfpt = new Point();
		Point rfpt = new Point();
		twoParallelPoint(pa.get(0), pa.getFrompt(), distance, lfpt, rfpt);
		lpath.add(lfpt);
		rpath.add(rfpt);
		int n = pa.getPath().size() - 1;
		for (int i = 0; i < n; i++) {
			VectorLine fvl = new VectorLine(pa.get(i).getTopt(), pa.get(i)
					.getFrompt());
			VectorLine svl = new VectorLine(pa.get(i + 1).getFrompt(), pa.get(
					i + 1).getTopt());
			VectorLine cvl = angleBisector(fvl, svl);
			VectorLine lvl = new VectorLine();
			VectorLine rvl = new VectorLine();
			twoParallelLine(fvl, distance, lvl, rvl);
			Point lp, rp;
			lp = intersect(lvl, cvl);
			lpath.add(lp);
			rp = intersect(rvl, cvl);
			rpath.add(rp);
		}
		Point ltpt = new Point();
		Point rtpt = new Point();
		twoParallelPoint(pa.get(0), pa.getTopt(), distance, ltpt, rtpt);
		lpath.add(ltpt);
		rpath.add(rtpt);

		for (Point pt : lpath) {
			ring.add(pt);
		}
		for (int i = rpath.size() - 1; i >= 0; i--) {
			ring.add(rpath.get(i));
		}
		return ring;
	}

	// 首末两点求平行点
	private static void twoParallelPoint(Line line, Point pt, double distance,
			Point lp, Point rp) {
		double x;
		double y;
		if (line.getParaA() == 0) {
			x = 0;
			y = distance * (-line.getParaB() / Math.abs(line.getParaB()));
		} else if (line.getParaB() == 0) {
			x = distance * (-line.getParaA() / Math.abs(line.getParaA()));
			y = 0;
		} else {
			x = distance
					* (-line.getParaA() / Math.sqrt(line.getParaA()
							* line.getParaA() + line.getParaB()
							* line.getParaB()));
			y = distance
					* (-line.getParaB() / Math.sqrt(line.getParaA()
							* line.getParaA() + line.getParaB()
							* line.getParaB()));
		}
		lp.setX(pt.getX() + x);
		lp.setY(pt.getY() + y);
		rp.setX(pt.getX() - x);
		rp.setY(pt.getY() - y);
	}

	// 角平分线（参数为矢量线参数之和）
	public static VectorLine angleBisector(VectorLine fvl, VectorLine svl) {
		double fpara = Math.sqrt(fvl.getParaA() * fvl.getParaA()
				+ fvl.getParaB() * fvl.getParaB());
		double spara = Math.sqrt(svl.getParaA() * svl.getParaA()
				+ svl.getParaB() * svl.getParaB());
		double fpa = fvl.getParaA() / fpara;
		double fpb = fvl.getParaB() / fpara;
		double fpc = fvl.getParaC() / fpara;
		double spa = svl.getParaA() / spara;
		double spb = svl.getParaB() / spara;
		double spc = svl.getParaC() / spara;
		VectorLine bvl = new VectorLine(fpa + spa, fpb + spb, fpc + spc);
		return bvl;
	}

	// 推求左右俩平行线
	public static void twoParallelLine(VectorLine vline, double distance,
			VectorLine lvl, VectorLine rvl) {
		double lpc = distance
				* Math.sqrt(vline.getParaA() * vline.getParaA()
						+ vline.getParaB() * vline.getParaB());
		// double rpc = vline.getParaC() - distance * Math.Sqrt(vline.getParaA()
		// * vline.getParaA() + vline.getParaB() *
		// vline.getParaB());
		lvl.setParaA(vline.getParaA());
		lvl.setParaB(vline.getParaB());
		lvl.setParaC(vline.getParaC() + lpc);// = new
												// VectorLine(vline.getParaA(),
												// vline.getParaB(),
												// vline.getParaC()
												// + distance);
		rvl.setParaA(vline.getParaA());
		rvl.setParaB(vline.getParaB());
		rvl.setParaC(vline.getParaC() - lpc);// = new
												// VectorLine(vline.getParaA(),
												// vline.getParaB(),
												// vline.getParaC()
												// - distance);
	}

	// 求缓冲区的左右两点，以lp，rp输出
	public static Point intersect(VectorLine vl1, VectorLine vl2) {
		Point pt = new Point();
		if (Math.abs(vl1.getParaA()) == 0) { // 直线平行x轴
			pt.setY((-vl1.getParaC()) / vl1.getParaB());
			pt.setX(((-vl2.getParaC()) + (-vl2.getParaB()) * pt.getY())
					/ vl2.getParaA());
		} else if (Math.abs(vl1.getParaB()) == 0) { // 直线平行y轴
			pt.setX((-vl1.getParaC()) / vl1.getParaA());
			pt.setY(((-vl2.getParaC()) + (-vl2.getParaA()) * pt.getX())
					/ vl2.getParaB());
		} else if (Math.abs(vl2.getParaA()) == 0) {// 直线平行x轴
			pt.setY((-vl2.getParaC()) / vl2.getParaB());
			pt.setX(((-vl1.getParaC()) + (-vl1.getParaB()) * pt.getY())
					/ vl1.getParaA());
		} else if (Math.abs(vl2.getParaB()) == 0) {// 直线平行y轴
			pt.setX((-vl2.getParaC()) / vl2.getParaA());
			pt.setY(((-vl1.getParaC()) + (-vl1.getParaA()) * pt.getX())
					/ vl1.getParaB());
		} else { // 两直线都不为零
			// 解直线方程得：x = (B1C2 - B2C1)/()，y = (A2C1 - A1C2)/(A1B2 - A2B1)
			pt.setX((vl2.getParaC() * vl1.getParaB() - vl2.getParaB()
					* vl1.getParaC())
					/ (vl2.getParaB() * vl1.getParaA() - vl2.getParaA()
							* vl1.getParaB()));
			pt.setY((vl1.getParaC() * vl2.getParaA() - vl1.getParaA()
					* vl2.getParaC())
					/ (vl2.getParaB() * vl1.getParaA() - vl2.getParaA()
							* vl1.getParaB()));
		}
		return pt;
	}

	//
	/**
	 * 
	 * @param trip
	 * @return
	 */
	public static List<Point> ext(List<Point> trip) {
		if (trip == null || trip.size() == 0) {
			return null;
		}
		//
		for (int index = 0; index < trip.size() - 1; index++) {
			fp = trip.get(index);
			tp = trip.get(index + 1);
			line = new Line(fp, tp);
			path.add(line);
		}
		List<Point> ring = bufferofPath(path, distance);
		if (ring == null || ring.size() == 0) {
			return null;
		}
		List<Point> newTrip = new ArrayList<Point>();
		Point temp;
		for (int i = 0; i < ring.size(); i++) {
			temp = ring.get(i);
			if (temp == null) {
				LOGGER.info("数据为空，过滤");
				continue;
			}
			if (!BeanUtil.checkDouble(temp.getX())
					|| !BeanUtil.checkDouble(temp.getY())) {
				LOGGER.info("数据不正常，过滤");
				continue;
			}
			newTrip.add(temp);
		}
		newTrip.add(newTrip.get(0));// 接口缝合
		return newTrip;
	}
}
