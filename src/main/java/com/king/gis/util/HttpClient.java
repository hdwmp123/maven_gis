package com.king.gis.util;

import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class HttpClient {
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HttpClient.class);

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, JSONObject params) {
		if (!BeanUtil.checkStr(url)) {
			LOGGER.error("url不能为空");
			return null;
		}
		MultivaluedMap<String, String> mparams = new MultivaluedMapImpl();
		if (params != null) {
			@SuppressWarnings("unchecked")
			Set<Entry<String, String>> entries = params.entrySet();
			for (Entry<String, String> entry : entries) {
				mparams.add(entry.getKey(), entry.getValue());
			}
		}

		Client client = Client.create();
		WebResource resource = client.resource(url);
		client.setReadTimeout(30000);
		ClientResponse response = null;
		String result = null;
		try {
			response = resource.queryParams(mparams).get(ClientResponse.class);
			LOGGER.debug("请求信息" + resource.toString());
			LOGGER.debug("参数" + mparams.toString());
			result = response.getEntity(String.class);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				LOGGER.debug("get成功" + result);
			} else {
				LOGGER.error("get 失败" + result);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
			LOGGER.error("get 失败(异常):" + ex.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return result;
	}
}
