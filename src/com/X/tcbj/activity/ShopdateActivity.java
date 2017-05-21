package com.X.tcbj.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class ShopdateActivity extends Activity {
	WebView shopdateweb;
	String info, url;
	ImageView cityimage_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopdate);
		shopdateweb = (WebView) findViewById(R.id.shopdateweb);
		cityimage_back=(ImageView)findViewById(R.id.cityimage_back);
		shopdateweb.setVerticalScrollBarEnabled(false); // 垂直不显示
		shopdateweb.getSettings().setJavaScriptEnabled(true);
		shopdateweb.setWebChromeClient(new WebChromeClient());
		url = Constant.url+"StoresContent?id="
				+ Constant.SHOP_ID;
		System.out.println("url:"+url);
		getview(1, url);
		cityimage_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	public void getview(final int what, final String url) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				HttpRequest httpRequest = new HttpRequest();
				info = httpRequest.doGet(url, ShopdateActivity.this);
				if (info.equals("网络超时")) {
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(what);
				}

			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String web = null;
				JSONObject jsonObject = JSONObject.parseObject(info);
				if (jsonObject.getIntValue("status") == 0) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						JSONObject jsonObject2 = array.getJSONObject(0);
						web = jsonObject2.getString("content") == null ? ""
								: jsonObject2.getString("content");
					}
					shopdateweb.loadDataWithBaseURL(null, web, "text/html",
							"utf-8", null);
				} else {
					handler.sendEmptyMessage(3);
				}
				break;
			case 2:
				Toast.makeText(ShopdateActivity.this, "网络超时",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(ShopdateActivity.this, "暂无详情",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
}
