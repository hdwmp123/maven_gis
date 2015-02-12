package com.king.gis.util.data;

import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.gis.douglas.Point;
import com.king.gis.util.SqlConnect;
import com.king.gis.util.data.abs.Data;

/**
 * @Title: ReadTrack.java
 * @Package com.king.gis.util
 * @Description: 读取行程数据
 * @author hdwmp123@163.com
 * @date 2014-12-3 下午3:58:44
 * @version V1.0
 */
public class FromDbData extends Data {
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(FromDbData.class);

	/**
	 * 读取行程数据
	 * 
	 * @param sql
	 * @return
	 */
	public List<Point> getTrip(String sql) {
		SqlConnect connect = new SqlConnect();
		connect.query(sql, false);
		ResultSet resultSet = connect.getResultSet();
		Blob trackBlob = null;
		byte[] bytes = null;
		List<Point> trip = null;
		try {
			while (resultSet.next()) {
				trackBlob = resultSet.getBlob("track");
				bytes = trackBlob.getBytes(1, (int) trackBlob.length());
				String trackJsonStr = new String(bytes);
				LOGGER.debug(trackJsonStr);
				//
				if (trackJsonStr == null || "".equals(trackJsonStr)) {
					continue;
				}
				trip = new ArrayList<Point>();
				JSONArray tracks = JSONArray.fromObject(trackJsonStr);
				Point tempP;
				JSONObject tempJ = null;
				for (int i = 0; i < tracks.size(); i++) {
					tempJ = tracks.getJSONObject(i);
					tempP = new Point();
					tempP.setX(tempJ.getDouble("longitude"));
					tempP.setY(tempJ.getDouble("latitude"));
					trip.add(tempP);
				}
				//
				if (trip.size() == 0) {
					continue;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			connect.closeAll();
		}
		return trip;
	}

	public List<com.king.gis.ext.Point> getTrip2(String sql) {
		List<com.king.gis.ext.Point> trip = null;
		com.king.gis.ext.Point point = null;
		//
		List<Point> tripTemp = getTrip(sql);
		Point tempPoint = null;
		if (tripTemp == null || tripTemp.size() == 0) {
			return null;
		}
		trip = new ArrayList<com.king.gis.ext.Point>();
		for (int j = 0; j < tripTemp.size(); j++) {
			tempPoint = tripTemp.get(j);
			point = new com.king.gis.ext.Point();
			point.setX(tempPoint.getX());
			point.setY(tempPoint.getY());
			trip.add(point);
		}
		return trip;
	}
}
