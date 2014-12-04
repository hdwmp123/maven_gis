package com.king.gis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil {
	/**
	 * 获取日志
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Logger getLogger(Class<T> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	public static boolean checkDouble(double num) {
		if (Double.isInfinite(num) || Double.isNaN(num)) {
			return false;
		}
		return true;
	}

	/**
	 * 检测是否为null或""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkStr(Object str) {
		if (str instanceof String) {
			return str != null && !"".equals(str.toString().trim());
		} else {
			return str != null;
		}
	}
}
