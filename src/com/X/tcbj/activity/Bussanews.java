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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Information_adpater;
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

public class Bussanews extends Activity implements OnClickListener {
	TextView banewstitle, bainforenzheng, bainfotime, bainfopeople,
			tv_load_more;
	String siteid, url, urlstr, attid, poststr, delect;
	ListView listView;
	int page = 1, everyinfo, c, ver = 0;
	Button banews_guanzhu;
	ArrayList<HashMap<String, Object>> infoarray = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> infohashMap;
	Information_adpater information_adpater;
	private View loadMoreView;
	Dialog dialog;
	Object object;
	ImageView business_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bassnews);
		dialog = GetMyData.createLoadingDialog(Bussanews.this, "正在拼命的加载······");
		dialog.show();
		banewstitle = (TextView) findViewById(R.id.banewstitle);
		bainforenzheng = (TextView) findViewById(R.id.banewsrenzheng);
		bainfotime = (TextView) findViewById(R.id.banewstime);
		bainfopeople = (TextView) findViewById(R.id.banewspeople);
		banews_guanzhu = (Button) findViewById(R.id.banews_guanzhu);
		business_back = (ImageView) findViewById(R.id.business_back);
		business_back.setOnClickListener(this);
		banews_guanzhu.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.balist);
		banewstitle.setText(getIntent().getStringExtra("title"));
		bainforenzheng.setText(getIntent().getStringExtra("review"));
		bainfotime.setText(getIntent().getStringExtra("addTime"));
		bainfopeople.setText(getIntent().getStringExtra("counts"));
		attid = getIntent().getStringExtra("attid");
		Intent intent = new Intent(this, HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		if (attid.equals("0")) {
			banews_guanzhu.setBackgroundResource(R.drawable.com_plus);
		} else {
			banews_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		loadMoreView = LayoutInflater.from(Bussanews.this).inflate(
				R.layout.load_more, null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		listView.addFooterView(loadMoreView, null, false);
		siteid = getIntent().getStringExtra("siteid");
		url = Constant.url+"club/getClubNewsList?siteid="
				+ siteid + "&pagesize=10&page=" + page;
		getmessage(url, 1);
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
						if (page == c) {
							handler.sendEmptyMessage(4);
						} else {
							if (ver == 0) {
								page++;
								url = Constant.url+"club/getClubNewsList?siteid="
										+ siteid + "&pagesize=10&page=" + page;
								getmessage(url, 1);
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
				Intent intent = new Intent();
				if (infoarray.get(arg2).get("orPpt").equals("true")) {
					intent.putExtra("iD", infoarray.get(arg2).get("iD").toString());
					intent.putExtra("newsClassID",
							infoarray.get(arg2).get("newsClassID").toString());
					intent.setClass(Bussanews.this, Photo_newsinfo.class);
					Bussanews.this.startActivity(intent);
				} else {
					intent.putExtra("id", infoarray.get(arg2).get("iD").toString());
					intent.putExtra("newsClassID",
							infoarray.get(arg2).get("newsClassID").toString());
					intent.setClass(Bussanews.this, Info_info.class);
					Bussanews.this.startActivity(intent);
				}

			}
		});

	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// textView.setText(intent.getExtras().getString("data"));
			if (intent.getExtras().getString("attid").equals("0")) {
				banews_guanzhu.setBackgroundResource(R.drawable.com_plus);
				attid = intent.getExtras().getString("attid");
			} else {
				banews_guanzhu.setBackgroundResource(R.drawable.com_yes);
				attid = intent.getExtras().getString("attid");
			}
		}
	};

	public void getmessage(final String url, final int what) {
		new Thread() {
			public void run() {
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 6) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
				} else if (what == 8) {
					delect = httpRequest.doGet(url, Bussanews.this);
				} else {

					urlstr = httpRequest.doGet(url, Bussanews.this);
					if (urlstr.equals("网络超时")) {
						handler.sendEmptyMessage(3);
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

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				try {
					infomlist(urlstr);
					information_adpater.notifyDataSetChanged();
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
				if (page == c) {
					setLoadMoreText(R.string.loading_all);
				} else {
					setLoadMoreText(R.string.loading_more);
				}
				loadMoreView.setVisibility(View.VISIBLE);
				ver = 0;
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(Bussanews.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(Bussanews.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				String userId = PreferencesUtils.getString(Bussanews.this,
						"userid");
				int areaId = PreferencesUtils.getInt(Bussanews.this, "cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ siteid + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				getmessage(posturl, 6);
				break;
			case 6:
				if (object == null) {
					Toast.makeText(Bussanews.this, "响应失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							attid = jsonObject.getString("id");
							Toast.makeText(Bussanews.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							banews_guanzhu
									.setBackgroundResource(R.drawable.com_yes);
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", attid);
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(Bussanews.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 7:
				String url = Constant.url+"attention/deleteAttention?id="
						+ attid;
				getmessage(url, 8);
				break;
			case 8:
				try {
					JSONObject jsonObject = new JSONObject(delect);
					int stat = jsonObject.getInt("status");
					if (stat == 0) {
						attid = "0";
						Toast.makeText(Bussanews.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						banews_guanzhu
								.setBackgroundResource(R.drawable.com_plus);
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", attid);
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);

					} else {

						Toast.makeText(Bussanews.this, "取消失败",
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

	public void infomlist(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(2);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			everyinfo = jsonObject.getInt("totalRecord");
			for (int i = 0; i < jsonArray.length(); i++) {
				infohashMap = new HashMap<String, Object>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				infohashMap.put("picID", "");
				infohashMap.put("iD", jsonObject2.getString("id"));
				infohashMap.put("orPpt", jsonObject2.getString("orPpt"));
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
		information_adpater = new Information_adpater(Bussanews.this, infoarray);
		listView.setAdapter(information_adpater);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.banews_guanzhu:
			if (attid.equals("0")) {
				handler.sendEmptyMessage(5);
			} else {
				handler.sendEmptyMessage(7);
			}
			break;
		case R.id.business_back:
			finish();
			break;
		default:
			break;
		}

	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};
}
