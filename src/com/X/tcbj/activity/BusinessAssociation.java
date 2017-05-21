package com.X.tcbj.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Bussat_adapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * 商协
 * 
 * @author zpp
 * @version 2014/12/11
 */
public class BusinessAssociation extends Activity implements OnClickListener {
	LinearLayout chengyuanshu, renzhengshijian;
	TextView chengyuantxt, renzhengtxt, tv_load_more;
	ImageView buss_back;
	ListView listView;
	ArrayList<HashMap<String, String>> infoarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> infohashMap;
	ArrayList<HashMap<String, String>> attarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> atthashMap;
	String info, infourl, atturl, userId, attstr, poststr, delect;
	Bussat_adapter adapter;
	private View loadMoreView;
	int pageIndex = 1, c, everyinfo = 0, ver = 0;
	int a = 1, areaId, attID = 0;
	Dialog dialog;
	Object object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_association);
		dialog = GetMyData.createLoadingDialog(BusinessAssociation.this,
				"正在拼命的加载······");
		dialog.show();
		chengyuanshu = (LinearLayout) findViewById(R.id.chengyuanshu);
		chengyuanshu.setOnClickListener(this);
		renzhengshijian = (LinearLayout) findViewById(R.id.renzhengshijian);
		renzhengshijian.setOnClickListener(this);
		chengyuantxt = (TextView) findViewById(R.id.chengyuantxt);
		renzhengtxt = (TextView) findViewById(R.id.rezhengtxt);
		buss_back = (ImageView) findViewById(R.id.business_back);
		listView = (ListView) findViewById(R.id.bossat);
		buss_back.setOnClickListener(this);
		loadMoreView = LayoutInflater.from(BusinessAssociation.this).inflate(
				R.layout.load_more, null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		loadMoreView.setVisibility(View.GONE);
		listView.addFooterView(loadMoreView, null, false);
		userId = PreferencesUtils.getString(BusinessAssociation.this, "userid");
		areaId = PreferencesUtils.getInt(BusinessAssociation.this, "cityID");
		infourl = Constant.url+"club/getClubList?pagesize=10&page="
				+ pageIndex + "&orderKey=time&siteid=" + areaId;
		int Logn = PreferencesUtils.getInt(BusinessAssociation.this, "logn");
		if (Logn == 0) {
			atturl = Constant.url+"attention/getAttentionList?userId=0&typeId=100&areaId="
					+ areaId;
		} else {
			atturl = Constant.url+"attention/getAttentionList?userId="
					+ userId + "&typeId=1&areaId=" + areaId;
		}

		Intent intent = new Intent(this, HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		info(1, infourl);
		setlist();
		listView.setOnScrollListener(new OnScrollListener() {

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
							if (ver == 0) {
								pageIndex++;
								if (a == 1) {
									infourl = Constant.url+"club/getClubList?pagesize=10&page="
											+ pageIndex
											+ "&orderKey=time&siteid=" + areaId;
								} else {
									infourl = Constant.url+"club/getClubList?pagesize=10&page="
											+ pageIndex
											+ "&orderKey=num&siteid=" + areaId;
								}

								info(1, infourl);
								ver = 1;
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Constant.attarg2 = arg2;
				Intent intent = new Intent();
				intent.putExtra("siteid", infoarray.get(arg2).get("id"));
				intent.putExtra("addTime", infoarray.get(arg2).get("addTime"));
				intent.putExtra("title", infoarray.get(arg2).get("title"));
				intent.putExtra("review", infoarray.get(arg2).get("review"));
				intent.putExtra("counts", infoarray.get(arg2).get("counts"));
				intent.putExtra("attid", infoarray.get(arg2).get("attid"));
				intent.putExtra("bbsdomain",
						infoarray.get(arg2).get("bbsdomain"));
				intent.setClass(BusinessAssociation.this, BusinesAinfo.class);
				BusinessAssociation.this.startActivity(intent);

			}
		});
	}

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	public void info(final int what, final String url) {
		new Thread() {
			public void run() {
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 8) {
					delect = httpRequest.doGet(url, BusinessAssociation.this);
				} else if (what == 6) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
				} else {
					info = httpRequest.doGet(url, BusinessAssociation.this);
					attstr = httpRequest
							.doGet(atturl, BusinessAssociation.this);

					if (info.equals("网络超时")) {
						handler.sendEmptyMessage(2);
					} else {
						Message message = new Message();
						message.what = what;
						handler.sendEmptyMessage(what);
						Looper.loop();
					}
				}
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();

			};
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				try {
					attstr(attstr);
					imgurl(info);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int b = 10;
				c = everyinfo % b;
				if (c != 0) {
					c = everyinfo / b + 1;
				} else {
					c = everyinfo / b;

				}
				if (pageIndex == c) {
					setLoadMoreText(R.string.loading_all);
				} else {
					setLoadMoreText(R.string.loading_more);
				}
				loadMoreView.setVisibility(View.VISIBLE);
				ver = 0;
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(BusinessAssociation.this, "网络异常",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				dialog.dismiss();
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(BusinessAssociation.this, "没有数据",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(BusinessAssociation.this, "数据加载完毕",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				String userId = PreferencesUtils.getString(
						BusinessAssociation.this, "userid");
				int areaId = PreferencesUtils.getInt(BusinessAssociation.this,
						"cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ Constant.siteid + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				info(6, posturl);
				break;
			case 6:
				if (object == null) {
					Toast.makeText(BusinessAssociation.this, "响应失败",
							Toast.LENGTH_SHORT).show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							Toast.makeText(BusinessAssociation.this, "关注成功",
									Toast.LENGTH_SHORT).show();

							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", jsonObject.getString("id"));
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(BusinessAssociation.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 7:
				String id = infoarray.get(Constant.attarg2).get("attid");
				String url = Constant.url+"attention/deleteAttention?id="
						+ id;
				info(8, url);
				break;
			case 8:
				try {
					JSONObject jsonObject = new JSONObject(delect);
					int stat = jsonObject.getInt("status");
					if (stat == 0) {
						Toast.makeText(BusinessAssociation.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", "0");
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);
					} else {
						Toast.makeText(BusinessAssociation.this, "取消失败",
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

	public void imgurl(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(4);
		} else if (str.equals("网络超时")) {
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			everyinfo = jsonObject.getInt("totalRecord");
			for (int i = 0; i < jsonArray.length(); i++) {
				infohashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				infohashMap.put("addTime", jsonObject2.getString("addTime"));
				infohashMap.put("id", jsonObject2.getString("id"));

				if (attID == 0) {
					for (int j = 0; j < attarray.size(); j++) {
						if (jsonObject2.getString("id").equals(
								attarray.get(j).get("AttentionId"))) {
							infohashMap.put("attid", attarray.get(j).get("id"));
							break;
						} else {
							infohashMap.put("attid", "0");
						}
					}
				} else {
					infohashMap.put("attid", "0");
				}
				infohashMap.put("review", jsonObject2.getString("review"));
				infohashMap.put("logoId", jsonObject2.getString("logoId"));
				infohashMap.put("title", jsonObject2.getString("title"));
				infohashMap.put("counts", jsonObject2.getString("counts"));
				infohashMap
						.put("bbsdomain", jsonObject2.getString("bbsdomain"));
				infoarray.add(infohashMap);
			}
		}
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

	public void setlist() {
		adapter = new Bussat_adapter(infoarray, BusinessAssociation.this,
				handler);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		int color = getResources().getColor(R.color.mygrey);
		switch (arg0.getId()) {
		case R.id.renzhengshijian:
			if (a == 1) {

			} else {
				chengyuanshu.setBackgroundColor(color);
				renzhengtxt.setTextColor(Color.RED);
				renzhengshijian.setBackgroundColor(Color.WHITE);
				chengyuantxt.setTextColor(Color.BLACK);
				pageIndex = 1;
				infourl = Constant.url+"club/getClubList?pagesize=10&page="
						+ pageIndex + "&orderKey=time&siteid=" + areaId;
				infoarray.clear();
				info(1, infourl);
				setlist();
				a = 1;
			}

			break;
		case R.id.chengyuanshu:
			if (a == 1) {
				renzhengshijian.setBackgroundColor(color);
				chengyuantxt.setTextColor(Color.RED);
				chengyuanshu.setBackgroundColor(Color.WHITE);
				renzhengtxt.setTextColor(Color.BLACK);
				pageIndex = 1;
				infourl = Constant.url+"club/getClubList?pagesize=10&page="
						+ pageIndex + "&orderKey=num&siteid=" + areaId;
				infoarray.clear();
				info(1, infourl);
				setlist();
				a = 2;
			} else {

			}

			break;
		case R.id.business_back:
			finish();
			break;
		default:
			break;
		}
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (intent.getExtras().getString("attid").equals("0")) {
					infohashMap = new HashMap<String, String>();
					infohashMap.put("attid", "0");
					infohashMap.put("addTime", infoarray.get(Constant.attarg2)
							.get("addTime"));
					infohashMap.put("id",
							infoarray.get(Constant.attarg2).get("id"));
					infohashMap.put("logoId", infoarray.get(Constant.attarg2)
							.get("logoId"));
					infohashMap.put("review", infoarray.get(Constant.attarg2)
							.get("review"));
					infohashMap.put("title", infoarray.get(Constant.attarg2)
							.get("title"));
					infohashMap.put("counts", infoarray.get(Constant.attarg2)
							.get("counts"));
					infohashMap.put("bbsdomain", infoarray
							.get(Constant.attarg2).get("bbsdomain"));
					infoarray.set(Constant.attarg2, infohashMap);
					adapter.notifyDataSetChanged();
				} else {
					infohashMap = new HashMap<String, String>();
					infohashMap.put("attid",
							intent.getExtras().getString("attid"));
					infohashMap.put("addTime", infoarray.get(Constant.attarg2)
							.get("addTime"));
					infohashMap.put("id",
							infoarray.get(Constant.attarg2).get("id"));
					infohashMap.put("logoId", infoarray.get(Constant.attarg2)
							.get("logoId"));
					infohashMap.put("review", infoarray.get(Constant.attarg2)
							.get("review"));
					infohashMap.put("title", infoarray.get(Constant.attarg2)
							.get("title"));
					infohashMap.put("counts", infoarray.get(Constant.attarg2)
							.get("counts"));
					infohashMap.put("bbsdomain", infoarray
							.get(Constant.attarg2).get("bbsdomain"));

				}
				infoarray.set(Constant.attarg2, infohashMap);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};
}
