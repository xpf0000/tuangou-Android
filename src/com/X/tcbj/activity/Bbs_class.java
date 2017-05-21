package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.ExpandableAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 论坛版块
 * 
 * @author zpp
 * 
 */
public class Bbs_class extends Activity {
	String alllist, allurl, poststr, atturl, userid, attstr, delect;
	int siteid, attID = 0;
	ArrayList<HashMap<String, String>> groupArray;
	ArrayList<ArrayList<HashMap<String, String>>> childArray;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> attarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> atthashMap;
	ExpandableListView expandableListView;
	ArrayList<HashMap<String, String>> childMap;
	HashMap<String, String> mychildMap;
	ImageView bbs_class_back, bbs_cl_re;
	Dialog dialog;
	Object object;
	ExpandableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_class);
		expandableListView = (ExpandableListView) findViewById(R.id.myExpandableListView);
		bbs_cl_re = (ImageView) findViewById(R.id.bbs_cl_re);
		bbs_class_back = (ImageView) findViewById(R.id.bbs_class_back);
		siteid = PreferencesUtils.getInt(Bbs_class.this, "cityID");
		dialog = GetMyData.createLoadingDialog(Bbs_class.this, "正在拼命的加载······");
		dialog.show();
		Intent intent = new Intent(this, HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		userid = PreferencesUtils.getString(Bbs_class.this, "userid");
		allurl = Constant.url+"forum/groupall?siteid="
				+ siteid;
		System.out.println(allurl);
		int Logn = PreferencesUtils.getInt(Bbs_class.this, "logn");
		if (Logn == 0) {
			atturl = Constant.url+"attention/getAttentionList?userId=0&typeId=100&areaId="
					+ siteid;
		} else {
			atturl = Constant.url+"attention/getAttentionList?userId="
					+ userid + "&typeId=2&areaId=" + siteid;
		}

		System.out.println(atturl);
		info(1, allurl);
		bbs_cl_re.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int Logn=PreferencesUtils.getInt(Bbs_class.this, "logn");
					  if (Logn==0)  {
						Intent intent = new Intent();
						intent.setClass(Bbs_class.this, LoginActivity.class);
						Bbs_class.this.startActivity(intent);	
					} else {
						Intent intent2 = new Intent();
						intent2.setClass(Bbs_class.this, Report_post.class);
						Bbs_class.this.startActivity(intent2);	
					}
				

			}
		});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				Intent intent = new Intent();
				intent.putExtra("id", childArray.get(arg2).get(arg3).get("fid"));
				intent.setClass(Bbs_class.this, Bbs_new_post.class);
				Bbs_class.this.startActivity(intent);
				return false;
			}
		});
		bbs_class_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
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
				try {
//					attstr(attstr);
					allbig(alllist);
					ok(alllist);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:

				break;
			case 3:
				String userId = PreferencesUtils.getString(Bbs_class.this,
						"userid");
				int areaId = PreferencesUtils.getInt(Bbs_class.this, "cityID");
				poststr = "userId=" + userId + "&typeId=2&AttentionId="
						+ Constant.fatheragr + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				info(4, posturl);

				break;
			case 4:

				if (object == null) {
					Toast.makeText(Bbs_class.this, "响应失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							Toast.makeText(Bbs_class.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							mychildMap = new HashMap<String, String>();
							mychildMap.put("attid", jsonObject.getString("id"));
							mychildMap.put(
									"childname",
									childArray.get(Constant.groupagr)
											.get(Constant.childagr)
											.get("childname"));
							mychildMap.put(
									"posts",
									childArray.get(Constant.groupagr)
											.get(Constant.childagr)
											.get("posts"));
							mychildMap.put(
									"fup",
									childArray.get(Constant.groupagr)
											.get(Constant.childagr).get("fup"));
							mychildMap.put(
									"fid",
									childArray.get(Constant.groupagr)
											.get(Constant.childagr).get("fid"));
							childArray.get(Constant.groupagr).set(
									Constant.childagr, mychildMap);
							adapter.notifyDataSetChanged();
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("refrech", "1");
							sendBroadcast(intent);
						} else {
							System.out.println();
							Toast.makeText(Bbs_class.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 5:
				String id = childArray.get(Constant.groupagr)
						.get(Constant.childagr).get("attid");
				String url = Constant.url+"attention/deleteAttention?id="
						+ id;
				info(6, url);

				break;
			case 6:

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(delect);
					int stat = jsonObject.getInt("status");
					if (stat == 0) {
						Toast.makeText(Bbs_class.this, "已取消关注",
								Toast.LENGTH_SHORT).show();

						mychildMap = new HashMap<String, String>();
						mychildMap.put("attid", "0");
						mychildMap.put(
								"childname",
								childArray.get(Constant.groupagr)
										.get(Constant.childagr)
										.get("childname"));
						mychildMap.put(
								"posts",
								childArray.get(Constant.groupagr)
										.get(Constant.childagr).get("posts"));
						mychildMap.put("fup", childArray.get(Constant.groupagr)
								.get(Constant.childagr).get("fup"));
						mychildMap.put("fid", childArray.get(Constant.groupagr)
								.get(Constant.childagr).get("fid"));
						childArray.get(Constant.groupagr).set(
								Constant.childagr, mychildMap);
						adapter.notifyDataSetChanged();
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("refrech", "1");
						sendBroadcast(intent);
					} else {
						Toast.makeText(Bbs_class.this, "取消失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 6) {
					delect = httpRequest.doGet(url, Bbs_class.this);
				} else if (what == 4) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
				} else {
					alllist = httpRequest.doGet(url, Bbs_class.this);
					attstr = httpRequest.doGet(atturl, Bbs_class.this);
				}
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();
			};
		}.start();
	}

	public void allbig(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		int x = jsonArray.length();
		groupArray = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < x; i++) {
			JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
			hashMap = new HashMap<String, String>();
			hashMap.put("name", jsonObject2.getString("name"));
			hashMap.put("todayTotal", jsonObject2.getString("todayTotal"));
			groupArray.add(hashMap);

		}
	}

	public void ok(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		childArray = new ArrayList<ArrayList<HashMap<String, String>>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
			JSONArray jsonArray3 = jsonObject2.getJSONArray("item");
			childMap = new ArrayList<HashMap<String, String>>();
			for (int j = 0; j < jsonArray3.length(); j++) {
				mychildMap = new HashMap<String, String>();
				JSONObject jsonObject4 = (JSONObject) jsonArray3.get(j);
				mychildMap.put("childname", jsonObject4.getString("name"));
				mychildMap.put("posts", jsonObject4.getString("posts"));
				mychildMap.put("fup", jsonObject4.getString("fup"));
				mychildMap.put("fid", jsonObject4.getString("fid"));
//				if (attID == 0) {
//					for (int k = 0; k < attarray.size(); k++) {
//						if (jsonObject4.getString("fid").equals(
//								attarray.get(k).get("AttentionId"))) {
//							mychildMap.put("attid", attarray.get(k).get("id"));
//							break;
//						} else {
//
//							mychildMap.put("attid", "0");
//						}
//					}
//				} else {
//					mychildMap.put("attid", "0");
//				}
				childMap.add(mychildMap);

			}
			childArray.add(childMap);
		}
		expandableListView.setGroupIndicator(null);
		adapter = new ExpandableAdapter(this, groupArray, childArray,
				Bbs_class.this, handler);
		expandableListView.setAdapter(adapter);
	}

	public void attstr(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a != 0) {
			attID = 1;
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				atthashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				atthashMap.put("AttentionId",
						jsonObject2.getString("AttentionId"));
				atthashMap.put("id", jsonObject2.getString("id"));
				attarray.add(atthashMap);
			}

		}
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};
}
