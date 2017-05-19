package com.hkkj.csrx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Mycollectadpater;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.umeng.analytics.MobclickAgent;

/**
 * 点评
 * 
 * @author zpp
 * @version1.0
 */
public class MyComments extends Activity {
	ListView listView;
	int everyinfo = 0;
	ImageView back;
	ArrayList<Map<String, String>> items = new ArrayList<Map<String, String>>();
	Map<String, String> item;
	Mycollectadpater mycommentadpater;
	LinearLayout layout;
	Button delcet, freshen_comment;
	TextView tv_load_more;
	int b = 0, c = 1;
	String id = "", a = "0";
	String collectid = "";
	String processURL;
	Dialog dialog;
	int page = 1;
	String userid=String.valueOf(13137);
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycomments);
//		userid = PreferencesUtils.getString(MyComments.this, "userid");
		processURL = "http://192.168.2.28:8080/HKCityApi/collect/getUserCollectList?userId="
				+ userid + "&page=" + page + "&pageSize=10";
		listView = (ListView) findViewById(R.id.mycomment_list);
		freshen_comment = (Button) findViewById(R.id.freshen_comment);
		layout = (LinearLayout) findViewById(R.id.shuaxin_comment);
		back = (ImageView) findViewById(R.id.comment_back);
		delcet = (Button) findViewById(R.id.comment_delec);
		loadMoreView = LayoutInflater.from(this).inflate(R.layout.load_more,
				null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		listView.addFooterView(loadMoreView, tv_load_more, false);
		dialog = GetMyData
				.createLoadingDialog(MyComments.this, "正在拼命的加载······");
		dialog.show();
		info(1, processURL);
		setlist();
		freshen_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog = GetMyData.createLoadingDialog(MyComments.this,
						"正在拼命的加载······");
				items.clear();
				dialog.show();
				setlist();
				info(1, processURL);

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Constant.SHOP_ID = items.get(arg2).get("StoreID");
				Intent intent = new Intent();
				intent.setClass(MyComments.this, ShopDetailsActivity.class);
				MyComments.this.startActivity(intent);

			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				int a = everyinfo;
				int b = 10;
				int x = a % b;
				if (x != 0) {
					x = a / b + 1;

				} else {
					x = a / b;
				}
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
						if (page == x) {
							handler.sendEmptyMessage(4);
						} else if (c == 2) {
							Toast.makeText(MyComments.this, "请先操作",
									Toast.LENGTH_SHORT).show();
						} else {
							page++;
							processURL = Constant.url+"collect/getUserCollectList?userId="
									+ userid + "&page=" + page + "&pageSize=10";
							info(1, processURL);
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
		delcet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (c == 1) {
					delcet.setText("删除");
					c = 2;
					b = items.size();
					for (int i = 0; i < b; i++) {
						items.get(i).put("a", "1");
						mycommentadpater.notifyDataSetChanged();
					}
				} else if (c == 2) {
					// 执行删除操作
					dialog = GetMyData.createLoadingDialog(MyComments.this,
							"正在拼命的加载······");
					dialog.show();
					HashMap<Integer, Boolean> state = mycommentadpater.state;
					for (int j = 0; j < mycommentadpater.getCount(); j++) {
						if (state.get(j) != null) {
							@SuppressWarnings("unchecked")
							HashMap<String, Object> map = (HashMap<String, Object>) mycommentadpater
									.getItem(j);
							id += map.get("CollectID").toString().trim() + ",";
							collectid = id.substring(0, id.length() - 1);
						}
					}
					handler.sendEmptyMessage(5);
				}
			}

		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				layout.setVisibility(View.GONE);
				mycommentadpater.notifyDataSetChanged();
				break;
			case 2:
				dialog.dismiss();
				layout.setVisibility(View.VISIBLE);
				Toast.makeText(MyComments.this, "网络访问超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				tv_load_more.setText("还没有收藏");
				Toast.makeText(MyComments.this, "还没有收藏", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				listView.removeFooterView(loadMoreView);
				tv_load_more.setText("加载完毕");
				Toast.makeText(MyComments.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				String url = Constant.url+"collect/upDateUserCollect?userId="
						+ userid + "&collectId=" + collectid;
				try {
					HttpRequest httpRequest = new HttpRequest();
					String a = httpRequest.doGet(url, MyComments.this);
					if (a.equals("网络超时")) {
						Toast.makeText(getApplicationContext(), "超时，请检查网络",
								Toast.LENGTH_LONG).show();
						dialog.dismiss();
					} else {
						JSONObject jsonObject = new JSONObject(a);
						int stat = jsonObject.getInt("status");
						if (stat == 0) {
							Toast.makeText(getApplicationContext(), "删除成功",
									Toast.LENGTH_LONG).show();
							dialog.dismiss();
						} else {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "删除失败",
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				items.clear();
				info(1, processURL);
				setlist();
				b = items.size();
				for (int i = 0; i < b; i++) {
					items.get(i).put("a", "0");
					mycommentadpater.notifyDataSetChanged();
				}
				id = "";
				collectid = "";
				delcet.setText("编辑");
				c = 1;
				break;
			default:
				break;
			}
		};
	};

	public void info(final int what, final String url) {
		new Thread() {
			@Override
			public void run() {
				HttpRequest httpRequest = new HttpRequest();
				try {
					String list = httpRequest.doGet(url, MyComments.this);
					setmap(list);
					Message msg = new Message();
					msg.what = what;
					handler.sendEmptyMessage(what);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();
					message.what = 2;
					handler.sendEmptyMessage(2);
				}
			}
		}.start();
	}

	public void setmap(String str) throws JSONException {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
				.parseObject(str);
		int x = jsonObject.getIntValue("status");
		if (x == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			everyinfo = jsonObject.getIntValue("totalRecord");
			com.alibaba.fastjson.JSONArray jsonArray = jsonObject
					.getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				com.alibaba.fastjson.JSONObject jsonObject2 = jsonArray
						.getJSONObject(i);
				item = new HashMap<String, String>();
				item.put("StoreName",
						jsonObject2.getString("storeName") == null ? ""
								: jsonObject2.getString("storeName"));
				item.put("CollectID",
						jsonObject2.getString("collectID") == null ? ""
								: jsonObject2.getString("collectID"));
				item.put("StoreOrAuthentication",
						jsonObject2.getString("storeisauth") == null ? ""
								: jsonObject2.getString("storeisauth"));
				item.put("StoreOrVip",
						jsonObject2.getString("storeisvip") == null ? ""
								: jsonObject2.getString("storeisvip"));
				item.put("StoreOrCard",
						jsonObject2.getString("storeiscard") == null ? ""
								: jsonObject2.getString("storeiscard"));
				item.put("StoreLogoPicUrl",
						jsonObject2.getString("storeLogoPicid") == null ? ""
								: jsonObject2.getString("storeLogoPicid"));
				item.put("StoreID",
						jsonObject2.getString("storeId") == null ? ""
								: jsonObject2.getString("storeId"));
				String time = jsonObject2.getString("addTime").toString() == null ? ""
						: jsonObject2.getString("addTime");
				item.put("CollectAddTime", time.substring(0, 10));
				item.put("a", a);
				items.add(item);
			}
		}
	}
	public void setlist() {
		mycommentadpater = new Mycollectadpater(items, this, listView);
		listView.setAdapter(mycommentadpater);
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
