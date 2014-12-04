package com.king.gis.douglas;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.king.gis.util.BeanUtil;
import com.king.gis.util.GeometryUtil;

/**
 * 
 * @Title: Rarefy.java
 * @Package com.king.gis.douglas
 * @Description: 行程抽稀
 * @author hdwmp123@163.com
 * @date 2014-12-3 下午4:02:10
 * @version V1.0
 */
public class Rarefy {
	private static transient final Logger LOGGER = BeanUtil
			.getLogger(Rarefy.class);

	/**
	 * 对行程进行抽稀
	 * 
	 * @param trip
	 * @return
	 */
	public static List<Point> tripRarefy(List<Point> trip) {
		//
		Double angle;// 两个线段的夹角
		Double limitAngle = GeometryUtil.LIMIT_ANGLE;// 夹角限定,小于此夹角则进行抽稀
		List<Point> newTrip = new ArrayList<Point>();
		int startIndex = 0;
		Douglas douglas = new Douglas();
		for (int index = 2; index < trip.size(); index++) {
			angle = GeometryUtil.calculateTheAngle(trip, index);
			if (angle == Double.NaN || angle <= limitAngle) {
				// 对拐角前的数据进行抽稀 所以是 从 startIndex到index-1
				LOGGER.info("开始：" + startIndex + " 结束：" + (index - 1));
				newTrip.addAll(douglas.rarefy(trip, startIndex, index - 1));
				startIndex = index - 1;
			}
		}
		return newTrip;
	}

}
