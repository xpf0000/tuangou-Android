package com.X.tcbj.activity;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.BBsadapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Bussexchange extends Activity implements OnClickListener {
	TextView banewstitle, bainforenzheng, bainfotime, bainfopeople, more,
			tv_load_more;
	LinearLayout bam_shuaxin;
	String siteid, bbsurl, urlstr, attid, poststr, delect, bbsdomain, bbslist;
	int page = 1, totalRecord = 0, c, areaId,ver = 0;
	Button banews_guanzhu,bam_freshen;
	ImageView business_back;
	private View loadMoreView;
	ListView listView;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	BBsadapter bBsadapter;
	Object object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bussexchange);
		banewstitle = (TextView) findViewById(R.id.baexrtitle);
		bainforenzheng = (TextView) findViewById(R.id.baexrenzheng);
		bainfotime = (TextView) findViewById(R.id.baexrtime);
		bainfopeople = (TextView) findViewById(R.id.baexpeople);
		bam_shuaxin = (LinearLayout) findViewById(R.id.bam_shuaxin);
		business_back = (ImageView) findViewById(R.id.business_back);
		banews_guanzhu = (Button) findViewById(R.id.baexr_guanzhu);
		bam_freshen=(Button)findViewById(R.id.bam_freshen);
		bam_freshen.setOnClickListener(this);
		business_back.setOnClickListener(this);
		banews_guanzhu.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.baex_list);
		banewstitle.setText(getIntent().getStringExtra("title"));
		bainforenzheng.setText(getIntent().getStringExtra("review"));
		bainfotime.setText(getIntent().getStringExtra("addTime"));
		bainfopeople.setText(getIntent().getStringExtra("counts"));
		attid = getIntent().getStringExtra("attid");
		bbsdomain = getIntent().getStringExtra("bbsdomain");
		siteid = getIntent().getStringExtra("siteid");
		areaId = PreferencesUtils.getInt(Bussexchange.this, "cityID");
		Intent intent = new Intent(this, HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		if (attid.equals("0")) {
			banews_guanzhu.setBackgroundResource(R.drawable.com_plus);
		} else {
			banews_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		loadMoreView = LayoutInflater.from(Bussexchange.this).inflate(
				R.layout.load_more, null);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		listView.addFooterView(loadMoreView, null, false);
		bbsurl = Constant.url+"forum/glist?id="
				+ bbsdomain + "&siteid=" + areaId
				+ "&page=1&pagesize=10&order=0";
		BBlist(bbsurl, 1);
		setbbslust();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(Bussexchange.this, Post_info.class);
				intent.putExtra("tid", array.get(arg2).get("tid"));
				Bussexchange.this.startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

				int a = totalRecord;
				int b = 10;
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
							if(ver==0){
								page++;
								bbsurl = Constant.url+"forum/glist?id="
										+ bbsdomain
										+ "&siteid="
										+ areaId
										+ "&page=" + page + "&pagesize=10&order=0";
								BBlist(bbsurl, 1);
								ver=1;
							}
							
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.business_back:
			finish();
			break;
		case R.id.baexr_guanzhu:
			if (attid.equals("0")) {
				handler.sendEmptyMessage(5);
			} else {
				handler.sendEmptyMessage(7);
			}
			break;
		case R.id.bam_freshen:
		BBlist(bbsurl, 1);
		setbbslust();
		break;
		default:
			break;
		}

	}

	public void BBlist(final String url, final int what) {
		new Thread() {
			public void run() {
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 6) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
					handler.sendEmptyMessage(6);
				} else if (what == 8) {
					delect = httpRequest.doGet(url, Bussexchange.this);
					handler.sendEmptyMessage(8);
				} else {
					bbslist = httpRequest.doGet(url, Bussexchange.this);
					if (bbslist.equals("网络超时")) {
						handler.sendEmptyMessage(4);
					} else {
						Message message = new Message();
						message.what = what;
						handler.sendEmptyMessage(what);
					}
				}
				
				Looper.loop();
			};
		}.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int b = 10;
				c = totalRecord % b;
				if (c != 0) {
					c = totalRecord / b + 1;
				} else {
					c = totalRecord / b;

				}
				try {
					bbs_listinfo(bbslist);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bBsadapter.notifyDataSetChanged();
				if (array.size() == 0) {
					setLoadMoreText(R.string.loading_all);
				} else {
					setLoadMoreText(R.string.loading_more);
				}
				if(page==c){
					setLoadMoreText(R.string.loading_all);
				}else{
					setLoadMoreText(R.string.loading_more);
				}
				loadMoreView.setVisibility(View.VISIBLE);
				bam_shuaxin.setVisibility(View.GONE);
				ver=0;
				break;
			case 2:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Bussexchange.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Bussexchange.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				bam_shuaxin.setVisibility(View.VISIBLE);
				loadMoreView.setVisibility(View.GONE);
				Toast.makeText(Bussexchange.this, "网络访问超时", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				String userId = PreferencesUtils.getString(Bussexchange.this,
						"userid");
				int areaId = PreferencesUtils.getInt(Bussexchange.this,
						"cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ siteid + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				BBlist(posturl, 6);
				break;
			case 6:
				if (object == null) {
					Toast.makeText(Bussexchange.this, "响应失败",
							Toast.LENGTH_SHORT).show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							attid = jsonObject.getString("id");
							Toast.makeText(Bussexchange.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							banews_guanzhu
									.setBackgroundResource(R.drawable.com_yes);
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", attid);
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(Bussexchange.this, "关注失败",
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
				BBlist(url, 8);
				break;
			case 8:
				try {
					JSONObject jsonObject = new JSONObject(delect);
					int stat = jsonObject.getInt("status");
					if (stat == 0) {
						attid = "0";
						Toast.makeText(Bussexchange.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						banews_guanzhu
								.setBackgroundResource(R.drawable.com_plus);
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", attid);
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);

					} else {

						Toast.makeText(Bussexchange.this, "取消失败",
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

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	public void bbs_listinfo(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			totalRecord = jsonObject.getInt("totalRecord");
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				hashMap.put("attachment", jsonObject2.getString("attachment"));
				hashMap.put("author", jsonObject2.getString("author"));
				hashMap.put("authorid", jsonObject2.getString("authorid"));
				hashMap.put("dateline", jsonObject2.getString("dateline"));
				hashMap.put("digest", jsonObject2.getString("digest"));
				hashMap.put("displayorder",
						jsonObject2.getString("displayorder"));
				hashMap.put("fid", jsonObject2.getString("fid"));
				hashMap.put("heats", jsonObject2.getString("heats"));
				hashMap.put("icon", jsonObject2.getString("icon"));
				hashMap.put("lastpost", jsonObject2.getString("lastpost"));
				hashMap.put("lastposter", jsonObject2.getString("lastposter"));
				hashMap.put("replies", jsonObject2.getString("replies"));
				hashMap.put("special", jsonObject2.getString("special"));
				hashMap.put("stamp", jsonObject2.getString("stamp"));
				hashMap.put("subject", jsonObject2.getString("subject"));
				hashMap.put("tid", jsonObject2.getString("tid"));
				hashMap.put("views", jsonObject2.getString("views"));
				array.add(hashMap);
			}

		}
	}

	public void setbbslust() {
		bBsadapter = new BBsadapter(array, Bussexchange.this);
		listView.setAdapter(bBsadapter);

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

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};

}
