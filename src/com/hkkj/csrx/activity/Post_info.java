package com.hkkj.csrx.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Post_adapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Post_info extends Activity {
	TextView post_txtle, post_reply, post_Read, tv_load_more;
	ImageView post_write, post_h_back;
	ListView listView;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> titarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> titMap;
	String url, postinfo, tiestr, tid, titurl, collect,title;
	Post_adapter adapter;
	int siteid, page = 1, totalRecord = 0;
	
	private View loadMoreView, headview;
	public static Myhandler handler = null;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_info);
		listView = (ListView) findViewById(R.id.post_list);
		post_write = (ImageView) findViewById(R.id.post_write);
		post_h_back = (ImageView) findViewById(R.id.post_h_back);
		
		siteid = PreferencesUtils.getInt(Post_info.this, "cityID");
		dialog = GetMyData.createLoadingDialog(Post_info.this, "正在拼命的加载······");
		dialog.show();
		handler = new Myhandler();
		headview = LayoutInflater.from(Post_info.this).inflate(
				R.layout.post_head, null);
		loadMoreView = LayoutInflater.from(Post_info.this).inflate(
				R.layout.load_more, null);
		loadMoreView.setVisibility(View.GONE);
		post_txtle = (TextView) headview.findViewById(R.id.post_txtle);
		post_reply = (TextView) headview.findViewById(R.id.post_reply);
		post_Read = (TextView) headview.findViewById(R.id.post_Read);

		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		listView.addFooterView(loadMoreView, null, false);
		listView.addHeaderView(headview, null, false);
		tid = getIntent().getStringExtra("tid");
		url = Constant.url+"forum/info?tid=" + tid
				+ "&page=" + page + "&pagesize=20&siteid=" + siteid;
		System.out.println(url);
		titurl = Constant.url+"forum/title?id=" + tid
				+ "&siteid=" + siteid;
		post_info(url, 1);
		setlist();
		post_h_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		post_write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int Logn = PreferencesUtils.getInt(Post_info.this, "logn");
				if (Logn == 0) {
					Intent intent = new Intent();
					intent.setClass(Post_info.this, LoginActivity.class);
					Post_info.this.startActivity(intent);
				} else {
					Intent intent2 = new Intent();
					intent2.setClass(Post_info.this, Report_post.class);
					Post_info.this.startActivity(intent2);
				}
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int arg1) {
								int v = 1, c = 0;
				if (v == 1) {
					int a = totalRecord;
					int b = 20;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;

					}

				}
				switch (arg1) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 判断滚动到底部
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						if (page == c) {
							handler.sendEmptyMessage(2);
						} else {
							page++;
							url = Constant.url+"forum/info?tid="
									+ tid
									+ "&page="
									+ page
									+ "&pagesize=20&siteid=" + siteid;
							post_info(url, 1);

						}

					}

					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if (arg1 == 0) {

					
				}

			}
		});
	}

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	public final class Myhandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				loadMoreView.setVisibility(View.VISIBLE);
				try {
					jsinfo(postinfo);
					title(tiestr);
					post_txtle.setText(titarray.get(0).get("subject"));
					Constant.title=titarray.get(0).get("subject");
					post_reply.setText("回复：" + titarray.get(0).get("replies"));
					post_Read.setText("阅读：" + titarray.get(0).get("views"));
					listView.requestLayout();
					adapter.notifyDataSetChanged();
					int a = totalRecord, c;
					int b = 20;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;

					}
					if (page == c) {
						setLoadMoreText(R.string.loading_all);
					} else {
						setLoadMoreText(R.string.loading_more);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
				break;
			case 2:
				dialog.dismiss();
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Post_info.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				dialog.dismiss();
				setLoadMoreText(R.string.nostring);
				Toast.makeText(Post_info.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				dialog.show();
				page = 1;
				String louurl = Constant.url+"forum/see?tid="
						+ tid
						+ "&page="
						+ page
						+ "&pagesize=20&wid="
						+ array.get(0).get("authorid") + "&siteid=" + siteid;
				post_info(louurl, 5);
				break;
			case 5:
				dialog.dismiss();
				try {
					array.clear();
					jsinfo(postinfo);
					listView.requestLayout();
					adapter.notifyDataSetChanged();
					int a = totalRecord, c;
					int b = 20;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;

					}
					if (page == c) {
						setLoadMoreText(R.string.loading_all);
					} else {
						setLoadMoreText(R.string.loading_more);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 6:
				dialog.show();
				array.clear();
				post_info(url, 1);
				break;
			case 7:
				String url = Constant.url+"forum/favposts?uid=10&tid="
						+ tid + "&siteid=" + siteid;
				post_info(url, 8);
				break;
			case 8:
				if(collect!=null){
					try {
						JSONObject jsonObject = new JSONObject(collect);
						int a = jsonObject.getInt("status");
						if (a == 0) {
							Toast.makeText(Post_info.this, "收藏成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Post_info.this,
									jsonObject.getString("statusMsg"),
									Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Toast.makeText(Post_info.this,
							"收藏失败",
							Toast.LENGTH_SHORT).show();
				}
				
				break;
			default:
				break;
			}
		}
	}

	public void post_info(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 8) {
					collect = httpRequest.doGet(url, Post_info.this);
				} else {

					postinfo = httpRequest.doGet(url, Post_info.this);
					tiestr = httpRequest.doGet(titurl, Post_info.this);
				}

				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();
			}
		}.start();

	}

	public void jsinfo(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");

		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			totalRecord = jsonObject.getInt("totalRecord");
			int count = jsonArray.length();
			for (int i = 0; i < count; i++) {
				hashMap = new HashMap<String, String>();
				JSONObject object = jsonArray.getJSONObject(i);
				hashMap.put("message", object.getString("message"));
				hashMap.put("authorid", object.getString("authorid"));
				hashMap.put("author", object.getString("author"));
				hashMap.put("dateline", object.getString("dateline"));
				hashMap.put("position", object.getString("position"));
				hashMap.put("pid", object.getString("pid"));
				array.add(hashMap);
			}
		}

	}

	public void title(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		JSONObject object = jsonArray.getJSONObject(0);
		titMap = new HashMap<String, String>();
		titMap.put("replies", object.getString("replies"));
		titMap.put("subject", object.getString("subject"));
		titMap.put("views", object.getString("views"));
		titarray.add(titMap);

	}

	public void setlist() {
		adapter = new Post_adapter(array, Post_info.this, listView, tid,
				handler);
		listView.setAdapter(adapter);
	}

}
