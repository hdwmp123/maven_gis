package com.king.gis.ext.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.king.gis.ext.Point;
import com.king.gis.ext.RoadExt;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.Write2File;

public class RoadExtTest {
	public static void main(String[] args) {
		String path = "C:/Users/cwhl-0045/Desktop/File/polyline/data/%s";
		String pointStr = "116.355049,39.942688;116.355049,39.942383;116.355042,39.942173;116.355049,39.941597;116.355049,39.941593;116.355049,39.941593;116.355148,39.941345;116.355179,39.941132;116.355255,39.939831;116.355278,39.939423;116.355316,39.938828;116.355362,39.93774;116.355415,39.936741;116.355469,39.935616;116.355507,39.934971;116.35553,39.934376;116.355606,39.932449;116.35611,39.932316;116.35611,39.932316;116.357307,39.932323;116.359146,39.932323;116.359184,39.932323;116.360573,39.932323;116.36274,39.932312;116.364464,39.932339;116.364616,39.932343;116.364738,39.932335;116.367966,39.932335;116.368118,39.932331;116.370552,39.93248;116.371407,39.932529;116.373001,39.932598;116.375519,39.932678;116.376854,39.932766;116.377579,39.932781;116.378906,39.932865;116.379547,39.932903;116.380035,39.932934;116.380211,39.932938;116.381958,39.932999;116.384148,39.933067;116.384636,39.933086;116.387032,39.933178;116.387154,39.933182;116.389702,39.933304;116.392097,39.933403;116.392685,39.933426;116.393715,39.933418;116.395607,39.93338;116.396301,39.93338;116.396477,39.93338;116.40049,39.933403;116.403107,39.933414;116.405869,39.933407;116.405869,39.933403;116.405869,39.933403;116.405823,39.932465;116.40583,39.931599;116.405899,39.930412;116.40593,39.930035;116.405983,39.929207;116.406052,39.928009;116.406097,39.927238;116.406128,39.926548;116.406181,39.925594;116.40625,39.924534;116.406204,39.924427;116.406212,39.924309;116.406319,39.924263;116.406319,39.924263;116.406746,39.924271;116.407654,39.924297;116.409981,39.924351;116.410408,39.924335;116.410767,39.924297;116.410866,39.924255;116.411919,39.924248;116.415108,39.924274;116.416039,39.924282;116.417099,39.924297;116.41729,39.924297";
		String[] points = pointStr.split(";");
		String[] point = null;
		List<Point> trip = new ArrayList<Point>();
		Point tempPoint = null;
		List<Point> newTrip = null;
		for (int i = 0; i < points.length; i++) {
			point = points[i].split(",");
			tempPoint = new Point();
			tempPoint.setX(Double.parseDouble(point[0]));
			tempPoint.setY(Double.parseDouble(point[1]));
			trip.add(tempPoint);
		}
		//
		System.out.println(trip.size());
		newTrip = RoadExt.ext(trip);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "enve", "z" });
		Write2File.write2File(
				String.format(path, "polyline-road-source1.json"), JSONArray
						.fromObject(trip, config).toString());
		//
		System.out.println(newTrip.size());
		Iterator<Point> iterator = newTrip.iterator();
		while (iterator.hasNext()) {
			tempPoint = iterator.next();
			if (!BeanUtil.checkDouble(tempPoint.getX())
					|| !BeanUtil.checkDouble(tempPoint.getY())) {
				iterator.remove();
			}
		}
		System.out.println(newTrip.size());
		newTrip.add(newTrip.get(0));
		Write2File.write2File(String.format(path, "polyline-road-ext1.json"),
				JSONArray.fromObject(newTrip).toString());
	}
}
