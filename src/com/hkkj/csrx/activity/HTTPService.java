
package com.hkkj.csrx.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HTTPService {
	private static final String TAG = HTTPService.class.getSimpleName();
	private static HTTPService instance = null;
	private static final int CONNECTION_TIMEOUT = 30000;

	private HTTPService() {
	}

	public static HTTPService getInstance() {
		if (instance == null) {
			instance = new HTTPService();
		}

		return instance;
	}

	/**
	 * 是否有可用网络
	 */
	public boolean hasActiveNet(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

	public static int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null) {
			return networkInfo.getType();
		}

		return -1000;
	}

	public static boolean hasWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}

		return false;
	}

	public InputStream getStream(String url) {


		return null;
	}

	/**
	 * Get 请求
	 * 
	 * @param url
	 */
	public String[] getRequest(String url, String cookie) {


		return null;
	}

	/**
	 * Post 请求
	 */
	public String[] postRequest(String url, Map<String, String> headers,
			Map<String, String> params, String cookie) {


		return null;
	}

	/**
	 * Put 请求
	 */
	public String[] putRequest(String url, Map<String, String> headers,
			Map<String, String> params, String cookie) {

		return null;
	}

	/**
	 * 获取 access token
	 */
	public String[] getAccessToken(String url, Map<String, String> params) {


		return null;
	}

	/**
	 * Delete 请求
	 */
	public String[] deleteRequest(String url, Map<String, String> headers,
			String cookie) {

		return null;
	}

}
