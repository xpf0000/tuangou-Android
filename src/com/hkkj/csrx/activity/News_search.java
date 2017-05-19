package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Serach_adapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class News_search extends Activity {
	ListView listView;
	AutoCompleteTextView mytext;
	ImageView imageView,newse_h_back;
	TextView textView;
	String url;
	int page = 1;
	String areaid;
	int c;
	int everyinfo = 0;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	Serach_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.news_search);
		listView = (ListView) findViewById(R.id.new_s_list);
		mytext = (AutoCompleteTextView) findViewById(R.id.news_search);
		textView = (TextView) findViewById(R.id.newsou);
		imageView = (ImageView) findViewById(R.id.newse_h_back);
		areaid = Integer.toString(PreferencesUtils.getInt(News_search.this,
				"cityID"));
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String title = mytext.getText().toString();
				if (title.trim().equals("") || title == null) {
					Toast.makeText(News_search.this, "请输入内容", Toast.LENGTH_SHORT).show();
				} else {
					array.clear();
					try {
						title=URLEncoder.encode(title,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					url = Constant.url+"news/searchNewsList?areaID="
							+ areaid
							+ "&pageSize=20&pageIndex="
							+ page
							+ "&searchKey=" + title;
					info(1, url);
					setlist();
				}

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("id", array.get(arg2).get("iD"));
				intent.putExtra("newsClassID",array.get(arg2).get("newsClassID"));
				intent.putExtra("picId",
						array.get(arg2).get("picId").toString());
				intent.setClass(News_search.this, Info_info.class);
				News_search.this.startActivity(intent);

			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				int a = everyinfo;
				int b = 20;
				c = a % b;
				if (c != 0) {
					c = a / b + 1;

				} else {
					c = a / b;
				}
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
						if (page == c) {
							handler.sendEmptyMessage(2);
						} else {
							page++;
							String title = mytext.getText().toString();
							try {
								title=URLEncoder.encode(title,"UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							url = Constant.url+"news/searchNewsList?areaID="
									+ areaid
									+ "&pageSize=20&pageIndex="
									+ page
									+ "&searchKey=" + title;
							info(1, url);
						}
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
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(News_search.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				array.clear();
				Toast.makeText(News_search.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				Toast.makeText(News_search.this, "网络超时", Toast.LENGTH_SHORT)
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
					String info = httpRequest.doGet(path, News_search.this);
					setmap(info);
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
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			everyinfo = jsonObject.getInt("totalRecord");
			System.out.println(everyinfo);
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				hashMap.put("addTime", jsonObject2.getString("addTime"));
				hashMap.put("title", jsonObject2.getString("title"));
				hashMap.put("iD", jsonObject2.getString("id"));
				hashMap.put("newsClassID", jsonObject2.getString("newsClassId"));
				hashMap.put("picId",
						jsonObject2.getString("picId") == null ? ""
								: jsonObject2.getString("picId"));
				array.add(hashMap);

			}
		}

	}

	public void setlist() {
		adapter = new Serach_adapter(array, News_search.this, mytext.getText()
				.toString());
		listView.setAdapter(adapter);
	}
}
