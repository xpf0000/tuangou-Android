package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.SemAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class SeminarActivity extends Activity {
	ListView seminar_list;
	String url, seminarstr;
	int cityID = 1, page = 1;
	ArrayList<HashMap<String, String>> semarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> semhashMap;
	SemAdapter semadapter;
	ImageView seminar_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seminar);
		seminar_list = (ListView) findViewById(R.id.seminar_list);
		cityID = PreferencesUtils.getInt(SeminarActivity.this, "cityID");
		url = Constant.url+"topics/home?areaId=" + cityID
				+ "&page=" + page + "&pagesize=10";
		seminar_back=(ImageView)findViewById(R.id.seminar_back);
		seminar_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("id", semarray.get(arg2).get("id"));
				intent.putExtra("title", semarray.get(arg2).get("title"));
				intent.putExtra("picture", semarray.get(arg2).get("picture"));
				intent.setClass(SeminarActivity.this, ThematicDetails.class);
				SeminarActivity.this.startActivity(intent);

			}
		});
		seminar_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						page++;
						url = Constant.url+"topics/home?areaId="
								+ cityID + "&page=" + page + "&pagesize=10";
						getsestr();
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		seminar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		getsestr();
		setsem();
	}

	void getsestr() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				HttpRequest httpRequest = new HttpRequest();
				seminarstr = httpRequest.doGet(url, SeminarActivity.this);
				if (seminarstr.equals("网络超时")) {
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				getseminarstr(seminarstr);
				semadapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(SeminarActivity.this, "网络超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				Toast.makeText(SeminarActivity.this, "暂无更多内容",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	void getseminarstr(String str) {
		JSONObject jsonObject = JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 0) {
			JSONArray array = jsonObject.getJSONArray("list");
			if (array.size() == 0) {
				handler.sendEmptyMessage(3);
				if (page - 1 != 0) {
					page = page - 1;
				}
			} else {

				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonObject2 = array.getJSONObject(i);
					semhashMap = new HashMap<String, String>();
					semhashMap.put("addTime",
							jsonObject2.getString("addTime") == null ? ""
									: jsonObject2.getString("addTime"));
					semhashMap.put("topicClassId", jsonObject2
							.getString("topicClassId") == null ? ""
							: jsonObject2.getString("bannerId"));
					semhashMap.put("id",
							jsonObject2.getString("id") == null ? ""
									: jsonObject2.getString("id"));
					semhashMap.put("title",
							jsonObject2.getString("title") == null ? ""
									: jsonObject2.getString("title"));
					semhashMap.put("picture",
							jsonObject2.getString("picture") == null ? ""
									: jsonObject2.getString("picture"));
					semarray.add(semhashMap);
				}

			}
		} else {
			handler.sendEmptyMessage(3);

		}
	}

	void setsem() {
		semadapter = new SemAdapter(semarray, SeminarActivity.this);
		seminar_list.setAdapter(semadapter);
	}
}
