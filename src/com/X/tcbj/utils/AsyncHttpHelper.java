/**
 *  ClassName: AsyncHttpHelper.java
 *  created on 2013-11-14
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.X.tcbj.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * 异步HTTP请求工具类，是对android-async-http-1.4.5.jar包中AsyncHttpClient的封装
 * @author lgh
 * @version 1.0
 */
public class AsyncHttpHelper {
	/** 基准URL路径 */

	public static final String BASE_URL = "http://192.168.1.110:8080/web-question";
	public static AsyncHttpClient client = new AsyncHttpClient();
	
	/** Android客户端信息 */
	private static String appUserAgent;

	static {
		client.setUserAgent(getUserAgent());
		client.setMaxRetriesAndTimeout(4, 10 * 1000);
	}

	private static String getUserAgent() {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder sb = new StringBuilder("bcguys");
			sb.append("|Android");// 手机系统平台
			sb.append("|" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			sb.append("|" + android.os.Build.MODEL); // 手机型号
			appUserAgent = sb.toString();
		}
		return appUserAgent;
	}

	/**
	 * 根据相对路径获取它的绝对路径
	 * @param relativeUrl
	 * @return
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void getAbsoluteUrl(String absoluteUrl, AsyncHttpResponseHandler responseHandler) {
		client.get(absoluteUrl, responseHandler);
	}
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), responseHandler);
	}
	
	public static void getAbsoluteUrl(String absoluteUrl, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(absoluteUrl, params, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void postAbsoluteUrl(String absoluteUrl, AsyncHttpResponseHandler responseHandler) {
		client.post(absoluteUrl, responseHandler);
	}

	public static void postAbsoluteUrl(String absoluteUrl, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(absoluteUrl, params, responseHandler);
	}

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
}
