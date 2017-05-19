package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Commentadpater;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 收藏
 * 
 * @author zpp
 * @version1.0
 */
public class MyCollect extends Activity {
	ListView listView;
	ImageView collect_back;
	String processURL;
	ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
	Commentadpater adapter;
	HashMap<String, String> item;
	Button delete, freshen_collect;
	int c = 1, b = 0;
	String id = "";
	Dialog dialog;
	TextView textView, textView2, tv_load_more;
	String collectid = "";
	LinearLayout layout;
	int page = 1;
	int everyinfo = 0;
	String userid;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycollect);
		userid = PreferencesUtils.getString(MyCollect.this, "userid");
		processURL = Constant.url+"comment/getUserCommentList?userId="
				+ userid + "&page=" + page + "&pageSize=10";
		listView = (ListView) findViewById(R.id.mycollect_list);
		layout = (LinearLayout) findViewById(R.id.shuaxin_collect);
		freshen_collect = (Button) findViewById(R.id.freshen_collect);
		delete = (Button) findViewById(R.id.delete_colllect);
		collect_back = (ImageView) findViewById(R.id.collect_back);
		loadMoreView = LayoutInflater.from(this).inflate(R.layout.load_more,
				null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		listView.addFooterView(loadMoreView, tv_load_more, false);
		dialog = GetMyData.createLoadingDialog(MyCollect.this, "正在拼命的加载······");
		dialog.show();
		setlist();
		info(1, processURL);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String storid = items.get(arg2).get("StoreID");
				if (items.get(arg2).get("StoreOrVip").equals("2")) {
					Constant.SHOP_ID = storid;
					PreferencesUtils.putString(MyCollect.this, "storeID", storid);
					Intent intent = new Intent();

					intent.setClass(MyCollect.this, shangjiavip.class);
					MyCollect.this.startActivity(intent);
				} else if (items.get(arg2).get("StoreOrAuthentication").equals("2")) {

					Constant.SHOP_ID = storid;
					PreferencesUtils.putString(MyCollect.this, "storeID", storid);
					Intent intent = new Intent();

					intent.setClass(MyCollect.this, ShopVipInfo.class);
					MyCollect.this.startActivity(intent);
				}
				else{

					Constant.SHOP_ID = storid;
					PreferencesUtils.putString(MyCollect.this, "storeID", storid);
					Intent intent = new Intent();

					intent.setClass(MyCollect.this, ShopDetailsActivity.class);
					MyCollect.this.startActivity(intent);
				}

			}
		});
		collect_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
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
							Toast.makeText(MyCollect.this, "请先操作",
									Toast.LENGTH_SHORT).show();
						} else {
							page++;
							processURL = Constant.url+"comment/getUserCommentList?userId="
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
		// 刷新操作
		freshen_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog = GetMyData.createLoadingDialog(MyCollect.this,
						"正在拼命的加载······");
				items.clear();
				dialog.show();
				setlist();
				info(1, processURL);

			}
		});
		// 删除操作
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c == 1) {
					delete.setText("删除");
					c = 2;
					b = items.size();
					for (int i = 0; i < b; i++) {
						items.get(i).put("a", "1");
						adapter.notifyDataSetChanged();
					}
				} else if (c == 2) {
					dialog = GetMyData.createLoadingDialog(MyCollect.this,
							"正在拼命的加载······");
					dialog.show();
					HashMap<Integer, Boolean> state = adapter.state;
					for (int j = 0; j < adapter.getCount(); j++) {
						if (state.get(j) != null) {
							@SuppressWarnings("unchecked")
							HashMap<String, Object> map = (HashMap<String, Object>) adapter
									.getItem(j);

							id += map.get("CommentID").toString().trim() + ",";
							collectid = id.substring(0, id.length() - 1);

						}
					}
					handler.sendEmptyMessage(5);
				}
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
				adapter.notifyDataSetChanged();
				break;
			case 2:
				dialog.dismiss();
				layout.setVisibility(View.VISIBLE);
				Toast.makeText(MyCollect.this, "网络访问超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				listView.removeFooterView(loadMoreView);
				Toast.makeText(MyCollect.this, "还没有点评", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				listView.removeFooterView(loadMoreView);
				Toast.makeText(MyCollect.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				String url = Constant.url+"comment/upDateUserComment?userId="+userid+"&commentId="+collectid;
				try {
					HttpRequest httpRequest = new HttpRequest();
					String a = httpRequest.doGet(url,
							MyCollect.this);
					if (collectid.equals("") || (collectid == null)) {
						Toast.makeText(getApplicationContext(), "未选择删除的点评",
								Toast.LENGTH_LONG).show();
					} else {
						if (a.equals("网络超时")) {
							Toast.makeText(getApplicationContext(), "超时，请检查网络",
									Toast.LENGTH_LONG).show();
							dialog.dismiss();
						} else {
							JSONObject jsonObject = new JSONObject(a);
							int stat = jsonObject.getInt("status");
							if (stat == 0) {
								dialog.dismiss();
								Toast.makeText(getApplicationContext(), "删除成功",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getApplicationContext(), "删除失败",
										Toast.LENGTH_LONG).show();
								dialog.dismiss();
							}
						}
						

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.show();
				items.clear();
				info(1, processURL);
				setlist();
				b = items.size();
				for (int i = 0; i < b; i++) {
					items.get(i).put("a", "0");
					adapter.notifyDataSetChanged();
				}
				id = "";
				collectid = "";
				delete.setText("编辑");
				c = 1;
				break;
			default:
				break;
			}
		};
	};

	public void info(final int what, final String url) {
		new Thread() {
			public void run() {
				HttpRequest httpRequest = new HttpRequest();
				try {
					String list = httpRequest.doGet(url, MyCollect.this);
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
			};

		}.start();

	}

	public void setmap(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int x = jsonObject.getInt("status");
		if (x  == 0) {
			everyinfo = jsonObject.getInt("totalRecord");
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				item = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				item.put("AddTime", jsonObject2.getString("AddTime").toString().substring(0, 10));
				item.put("StoreName", jsonObject2.getString("storeName"));
				item.put("StoreID", jsonObject2.getString("StoreID"));
				item.put("CommentStar", jsonObject2.getString("starNum"));
				item.put("CommentContents", jsonObject2.getString("Contents"));
				item.put("StoreOrVip", jsonObject2.getString("storeisvip"));
				item.put("StoreOrCard", jsonObject2.getString("storeiscard"));
				item.put("StoreOrAuthentication",
						jsonObject2.getString("storeisauth"));
				item.put("storeisrec",
						jsonObject2.getString("storeisrec"));
				item.put("CommentID", jsonObject2.getString("CommentID"));
				item.put("a", "0");
				items.add(item);
			}
		} else {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		}
	}

	public void setlist() {
		adapter = new Commentadpater(items, this);
		listView.setAdapter(adapter);
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
