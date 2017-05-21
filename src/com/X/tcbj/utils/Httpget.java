package com.X.tcbj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.widget.Toast;



public class Httpget {
	public String doGet(String urlStr,Context context)  {
		String state = "";
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			// 设置发送请求的方式
			conn.setRequestMethod("GET");
			// 设置返回信息的格式类型
			conn.connect();
			// 正常时返回的状态码为200
			if (conn.getResponseCode() == 200) {
				// 获取返回的内容
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
				// 输出返回的信息
				while ((line = br.readLine()) != null) {
					state += line;
					
				}
				br.close();
			} else {
				Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			state="网络超时";
			Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
		}
		
		return state;
	}

}
