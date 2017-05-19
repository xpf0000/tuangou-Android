package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Information_adpater;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class Loca_news extends Activity {
	ListView locallist;
	Information_adpater adpater;
	TextView tv_load_more;
	ArrayList<HashMap<String, Object>> infoarray = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> infohashMap;
	int areaID, pageIndex = 1, everyinfo, c = 0,ver=0;
	String url, infomlist;
	private View loadMoreView;
	ImageView local_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_news);
		locallist = (ListView) findViewById(R.id.local_list);
		areaID = PreferencesUtils.getInt(Loca_news.this, "cityID");
		local_back=(ImageView)findViewById(R.id.local_back);
		loadMoreView = LayoutInflater.from(Loca_news.this).inflate(
				R.layout.load_more, null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		locallist.addFooterView(loadMoreView, null, false);
		url = Constant.url+"news/newsLocalList?areaID="
				+ areaID + "&pageSize=10&pageIndex=" + pageIndex;
		Informationlist(url, 1);
		setlist();
		local_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		locallist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				int a = everyinfo;

				int b = 10;
				c = a % b;
				if (c != 0) {
					c = a / b + 1;

				} else {
					c = a / b;

				}
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 判断滚动到底部
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						if (pageIndex == c) {
							handler.sendEmptyMessage(4);
						} else {
							if(ver==0){
								pageIndex++;
								url = Constant.url+"news/newsLocalList?areaID="
										+ areaID
										+ "&pageSize=10&pageIndex="
										+ pageIndex;
								Informationlist(url, 1);
								ver=1;
							}
							
						}

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
		locallist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("id", infoarray.get(arg2).get("iD").toString());
				intent.putExtra("newsClassID",
						infoarray.get(arg2).get("newsClassID").toString());
				intent.setClass(Loca_news.this, Info_info.class);
				Loca_news.this.startActivity(intent);

			}
		});
	}

	public void Informationlist(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				infomlist = httpRequest.doGet(url, Loca_news.this);
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();

			};
		}.start();

	}

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				break;
			case 1:
				try {
					infomlist(infomlist);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adpater.notifyDataSetChanged();
				ver=0;
				break;
			case 3:
				Toast.makeText(Loca_news.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Loca_news.this, "数据加载完成", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}

		};
	};

	public void infomlist(String str) throws JSONException {
		com.alibaba.fastjson.JSONObject jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			com.alibaba.fastjson.JSONArray jsonArray=jsonObject.getJSONArray("list");
			everyinfo = jsonObject.getIntValue("totalRecord");
			for (int i = 0; i < jsonArray.size(); i++) {
				infohashMap = new HashMap<String, Object>();
				com.alibaba.fastjson.JSONObject jsonObject2=jsonArray.getJSONObject(i);
				infohashMap.put("picID", jsonObject2.getString("picId")==null?"":jsonObject2.getString("picId"));
				infohashMap.put("iD", jsonObject2.getString("id"));
				infohashMap.put("clickNum", jsonObject2.getString("clickNum"));
				infohashMap.put("newsClassID",
						jsonObject2.getString("newsClassId"));
				infohashMap.put("title", jsonObject2.getString("title"));
				infohashMap.put("description",
						jsonObject2.getString("description"));
				infoarray.add(infohashMap);
			}
		}

	}

	public void setlist() {
		adpater = new Information_adpater(Loca_news.this, infoarray);
		locallist.setAdapter(adpater);
	}

}
