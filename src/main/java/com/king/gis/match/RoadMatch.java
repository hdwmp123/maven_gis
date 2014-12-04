package com.king.gis.match;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.gis.douglas.Point;
import com.king.gis.util.BeanUtil;
import com.king.gis.util.HttpClient;

/**
 * 
 * @Title: RoadMatch.java
 * @Package com.king.gis.match
 * @Description: 道路匹配
 * @author hdwmp123@163.com
 * @date 2014-12-4 下午3:45:20
 * @version V1.0
 */
public class RoadMatch {
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(RoadMatch.class);
	// 查询开车路线
	/**
	 * origin:116.379018,39.865026 <br>
	 * destination:116.321139,39.896028<br>
	 * key:608d75903d29ad471362f8c58c550daf <br>
	 * s:rsv3 <br>
	 * 
	 * strategy:0<br>
	 * extensions: <br>
	 */
	// 查询步行路线
	/**
	 * origin:116.480355,39.989783 <br>
	 * destination:116.469766,39.998731<br>
	 * key:608d75903d29ad471362f8c58c550daf<br>
	 * s:rsv3 <br>
	 * 
	 * multipath:0 <br>
	 */
	// 途经路线查询
	/**
	 * origin:116.303843,39.983412 <br>
	 * destination:116.407012,39.992093 <br>
	 * key:608d75903d29ad471362f8c58c550daf <br>
	 * s:rsv3 <br>
	 * 
	 * strategy:1 <br>
	 * extensions:base <br>
	 * output:json <br>
	 * waypoints:116.321354,39.896436 途经点<br>
	 */
	static String url = "http://restapi.amap.com/v3/direction/driving";
	// static String url = "http://restapi.amap.com/v3/direction/walking";
	static String key = "8ab59d433b3bafb3591af69fd047a6bb";

	/**
	 * 根据起始坐标查询道路数据
	 * 
	 * @param origin
	 *            起点坐标
	 * @param destination
	 *            终点坐标
	 * @param waypoints
	 *            途经点
	 * @return
	 */
	public static JSONObject search(Point origin, Point destination,
			List<Point> waypoints) {
		JSONObject result = null;
		//
		JSONObject params = new JSONObject();
		params.put("origin", origin.getX() + "," + origin.getY());
		params.put("destination", destination.getX() + "," + destination.getY());
		params.put("key", key);
		params.put("s", "rsv3");
		//
		// 查询开车路线
		// params.put("strategy", "0");// 策略
		// params.put("extensions", "");
		// 查询步行路线
		// params.put("multipath", "0");
		// 途经路线查询
		String waypointsStr = getWaypoints(waypoints);
		if (BeanUtil.checkStr(waypointsStr)) {
			params.put("waypoints", waypointsStr);
			params.put("strategy", "1");// 策略
			params.put("extensions", "base");
			params.put("output", "json");
		} else {
			params.put("strategy", "0");// 策略
		}
		//
		String response = HttpClient.get(url, params);
		if (!BeanUtil.checkStr(response)) {
			return null;
		}
		result = JSONObject.fromObject(response);
		//
		return result;
	}

	private static String getWaypoints(List<Point> waypoints) {
		if (waypoints == null || waypoints.size() == 0) {
			return null;
		}
		StringBuffer waypointsStr = new StringBuffer();
		Point point = null;
		for (int i = 0; i < waypoints.size(); i++) {
			point = waypoints.get(i);
			waypointsStr.append(point.getX()).append(",").append(point.getY())
					.append(";");
		}
		LOGGER.debug("waypointsStr=" + waypointsStr.toString());
		return waypointsStr.toString();
	}

	/**
	 * 根据起始坐标查询道路数据坐标点
	 * 
	 * @param origin
	 * @param destination
	 * @param waypoints
	 * @return
	 */
	private static List<Point> getRoadMatchLine(Point origin, Point destination,
			List<Point> waypoints) {
		JSONObject roadMatch = search(origin, destination, waypoints);
		if (roadMatch == null) {
			return null;
		}
		if (!roadMatch.containsKey("route")) {
			return null;
		}
		JSONArray paths = roadMatch.getJSONObject("route")
				.getJSONArray("paths");
		if (paths == null || paths.size() == 0) {
			return null;
		}
		List<Point> result = new ArrayList<Point>();
		JSONArray steps = null;
		String polylineStr = null;
		String[] polylineArr = null;
		String[] point = null;
		Point positionVo = null;
		for (int i = 0; i < paths.size(); i++) {
			steps = paths.getJSONObject(i).getJSONArray("steps");
			if (steps == null || steps.size() == 0) {
				continue;
			}
			for (int j = 0; j < steps.size(); j++) {
				polylineStr = steps.getJSONObject(j).getString("polyline");
				if (!BeanUtil.checkStr(polylineStr)) {
					continue;
				}
				polylineArr = polylineStr.split(";");
				if (polylineArr == null || polylineArr.length == 0) {
					continue;
				}
				for (int k = 0; k < polylineArr.length; k++) {
					point = polylineArr[k].split(",");
					positionVo = new Point();
					positionVo.setX(Double.parseDouble(point[0]));
					positionVo.setY(Double.parseDouble(point[1]));
					result.add(positionVo);
				}
			}
		}
		return result;
	}

	public static List<Point> roadMatch(List<Point> trip) {
		if (trip == null || trip.size() == 0) {
			return null;
		}
		List<Point> roadMatchLine = null;
		Point origin, destination;
		int startIndex = 0;
		int step = 4;
		List<Point> newTrip = new ArrayList<Point>();
		for (int endIndex = step; endIndex < trip.size(); endIndex += step) {
			if (endIndex > trip.size() - 1) {
				endIndex = trip.size() - 1;
			}
			LOGGER.debug("开始：" + startIndex + " 结束：" + endIndex);
			origin = trip.get(startIndex);
			destination = trip.get(endIndex);
			roadMatchLine = getRoadMatchLine(origin, destination,
					getWaypoints(trip, startIndex, endIndex));
			startIndex = endIndex;
			if (roadMatchLine == null || roadMatchLine.size() == 0) {
				continue;
			}
			LOGGER.debug(String.format("roadMatchLine=%s", roadMatchLine.size()));
			newTrip.addAll(roadMatchLine);
			newTrip.remove(newTrip.size() - 1);
			LOGGER.debug(String.format("newTrip.size=%s", newTrip.size()));
			LOGGER.debug("---------------------------------------------");
		}
		return newTrip;
	}

	private static List<Point> getWaypoints(List<Point> trip, int start, int end) {
		if ((end - start) < 2) {
			return null;
		}
		List<Point> waypoints = new ArrayList<Point>();
		for (int i = start + 1; i < end; i++) {
			waypoints.add(trip.get(i));
		}
		return waypoints;
	}

	public static void main(String[] args) {
		Point origin = new Point(116.379018, 39.865026);
		Point destination = new Point(116.321139, 39.896028);
		LOGGER.info(JSONArray.fromObject(
				getRoadMatchLine(origin, destination, null)).toString());
		List<Point> trip = new ArrayList<Point>();
		trip.add(origin);
		trip.add(destination);
		LOGGER.info(JSONArray.fromObject(roadMatch(trip)).toString());

	}
}
