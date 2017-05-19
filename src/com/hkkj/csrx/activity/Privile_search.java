package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.PrivilelistAdpater;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Privile_search extends Activity {
	ListView listView;
	AutoCompleteTextView mytext;
	TextView textView;
	String url;
	int page = 1;
	 String areaid ;
	int c;
	int everyinfo = 0;
	HashMap<String, String> hashMap;
	PrivilelistAdpater adpater;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	String info;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pivile_search);
		listView = (ListView) findViewById(R.id.search_listView1);
		mytext = (AutoCompleteTextView) findViewById(R.id.privile_search);
		textView = (TextView) findViewById(R.id.privile_quxiao);
		areaid=Integer.toString(PreferencesUtils.getInt(Privile_search.this, "cityID"));
		mytext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				String title = mytext.getText().toString();
				if(title.trim().equals("")||title==null){
					array.clear();
					adpater.notifyDataSetChanged();
					
				}else{
					array.clear();
					url = Constant.url+"GetListPmSelect?areaid="+areaid+"&page="
							+ page
							+ "&pagesize=20&title="
							+ java.net.URLEncoder.encode(title);
					info(1, url);
					setlist();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				
				
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("StoreID", array.get(arg2).get("StoreID"));
				intent.putExtra("ID", array.get(arg2).get("ID"));
				intent.setClass(Privile_search.this, Privileinfo.class);
				Privile_search.this.startActivity(intent);
				
			}
		});
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {

							page++;
							String title = mytext.getText().toString();
							url = Constant.url+"GetListPmSelect?areaid="+areaid+"&page="
									+ page
									+ "&pagesize=20&title="
									+ java.net.URLEncoder.encode(title);
							info(1, url);

					}
					break;
				}
				
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					setmap(info);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adpater.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Privile_search.this, "数据加载完毕", Toast.LENGTH_SHORT)
				.show();
				break;
			case 3:
				array.clear();
				Toast.makeText(Privile_search.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				Toast.makeText(Privile_search.this, "网络超时", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}

		};
	};

	public void info(final int what, final String path) {
		new Thread() {
			public void run() {
				HttpRequest httpRequest = new HttpRequest();
				try {
					info = httpRequest.doGet(path, Privile_search.this);
					System.out.println(info);
					
					Message message = new Message();
					message.what = what;
					handler.sendEmptyMessage(what);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();
					message.what = 4;
					handler.sendEmptyMessage(4);

				}
			};

		}.start();
	}

	public void setmap(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 0) {

			JSONArray jsonArray = jsonObject.getJSONArray("list");
			a = jsonArray.length();
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				hashMap.put("Title", jsonObject2.getString("Title"));
				hashMap.put("Address", jsonObject2.getString("Address"));
				hashMap.put("StartTime", jsonObject2.getString("StartTime")
						.substring(0, 10));
				hashMap.put("EndTime", jsonObject2.getString("EndTime")
						.substring(0, 10));
				hashMap.put("PicUrl", jsonObject2.getString("PicUrl"));
				hashMap.put("StoreID", jsonObject2.getString("StoreID"));
				hashMap.put("ID", jsonObject2.getString("ID"));
				array.add(hashMap);

			}

		} else {
			Message message = new Message();
			message.what = 2;
			handler.sendEmptyMessage(2);
		}

	}

	public void setlist() {
		adpater = new PrivilelistAdpater(array, Privile_search.this, listView);
		listView.setAdapter(adpater);
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen");
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
		}

}