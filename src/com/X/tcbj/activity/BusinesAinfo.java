package com.X.tcbj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.BBsadapter;
import com.X.tcbj.adapter.ImageTextListAdapter;
import com.X.tcbj.adapter.Myviewpageadapater;
import com.X.tcbj.myview.ImageAndText;
import com.X.tcbj.myview.MyListView;
import com.X.tcbj.myview.My_GridView;
import com.X.tcbj.utils.AsyncImageLoader;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BusinesAinfo extends Activity implements OnClickListener {
	String newurl, countsurl, hoturl, bbsurl, newlist, countlist, hotlist,
			siteid, delect, poststr, attid, bbsdomain,bbslist;
	TextView batitle, barenzheng, batime, bapeople, bus_p_more, bus_news_more;
	Button jianjie, dongtai, danwei, jiaoliu, ba_guanzhu;
	int areaId;
	ViewPager myPager; // 图片容器
	LinearLayout ovalLayout; // 圆点容器
	private List<View> listViews; // 图片组
	AsyncImageLoader ImageLoader;// 异步加载图片
	ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> imghashMap;
	ArrayList<HashMap<String, String>> infoarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> infohashMap;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> bbshashMap;
	ArrayList<HashMap<String, String>> bbsarray = new ArrayList<HashMap<String, String>>();
	String[] urls;
	Myviewpageadapater myviewpageadapater;
	private View item;
	private LayoutInflater inflater;
	private ImageView[] indicator_imgs;
	MyListView shangxielist;
	SimpleAdapter adapter;
	String[] text, time;
	private ImageTextListAdapter gmadapter;
	private List<ImageAndText> listImageAndText;
	private My_GridView gridview;
	DisplayMetrics dm;
	Dialog dialog;
	Object object;
	ImageView business_back;
	BBsadapter bBsadapter;
	MyListView bbs_lit;
	LinearLayout dongtais;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businesainfo);
		dialog = GetMyData.createLoadingDialog(BusinesAinfo.this,
				"正在拼命的加载······");
		dialog.show();
		siteid = getIntent().getStringExtra("siteid");
		bbsdomain = getIntent().getStringExtra("bbsdomain");
		areaId = PreferencesUtils.getInt(BusinesAinfo.this, "cityID");
		newurl = Constant.url+"club/getClubNews?siteid="
				+ siteid + "&count=5";
		countsurl = Constant.url+"club/getHomeMembers?siteid="
				+ siteid + "&count=8";
		hoturl = Constant.url+"club/getHotPic?siteid="
				+ siteid + "&count=3";
		bbsurl = Constant.url+"forum/glist?id="
				+ bbsdomain + "&page=1&pagesize=5&order=0&siteid=" + areaId;
		myPager = (ViewPager) findViewById(R.id.ba_view_pager);
		ovalLayout = (LinearLayout) findViewById(R.id.ba_indicator);
		shangxielist = (MyListView) findViewById(R.id.shangxielist);
		gridview = (My_GridView) findViewById(R.id.ba_gridview);
		business_back = (ImageView) findViewById(R.id.business_back);
		dongtais=(LinearLayout)findViewById(R.id.dongtais);
		bbs_lit=(MyListView)findViewById(R.id.shangxielist2);
		business_back.setOnClickListener(this);
		bus_news_more = (TextView) findViewById(R.id.bus_news_more);
		bus_news_more.setOnClickListener(this);
		bus_p_more = (TextView) findViewById(R.id.bus_p_more);
		bus_p_more.setOnClickListener(this);
		ba_guanzhu = (Button) findViewById(R.id.ba_guanzhu);
		ba_guanzhu.setOnClickListener(this);
		jianjie = (Button) findViewById(R.id.jianjie);
		jianjie.setOnClickListener(this);
		dongtai = (Button) findViewById(R.id.dongtai);
		dongtai.setOnClickListener(this);
		danwei = (Button) findViewById(R.id.danwei);
		danwei.setOnClickListener(this);
		jiaoliu = (Button) findViewById(R.id.jiaoliu);
		jiaoliu.setOnClickListener(this);
		listImageAndText = new ArrayList<ImageAndText>();
		batitle = (TextView) findViewById(R.id.batitle);
		batitle.setText(getIntent().getStringExtra("title"));
		dm = new DisplayMetrics();
		BusinesAinfo.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		attid = getIntent().getStringExtra("attid");
		Intent intent = new Intent(this, HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		if (attid.equals("0")) {
			ba_guanzhu.setBackgroundResource(R.drawable.com_plus);
		} else {
			ba_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		barenzheng = (TextView) findViewById(R.id.barenzheng);

		if (getIntent().getStringExtra("review").equals("true")) {
			barenzheng.setText("已认证");
		} else {
			barenzheng.setText("未认证");
		}
		batime = (TextView) findViewById(R.id.batime);
		batime.setText("认证时间："
				+ getIntent().getStringExtra("addTime").substring(0, 10));
		bapeople = (TextView) findViewById(R.id.bapeople);
		bapeople.setText("成员数：" + getIntent().getStringExtra("counts"));
		inflater = LayoutInflater.from(this);
		listViews = new ArrayList<View>();
		getmessage(newurl, 1);
		shangxielist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				if (infoarray.get(arg2).get("orPpt").equals("true")) {
					intent.putExtra("iD", infoarray.get(arg2).get("iD"));
					intent.putExtra("newsClassID",
							infoarray.get(arg2).get("newsClassID"));
					intent.setClass(BusinesAinfo.this, Photo_newsinfo.class);
					BusinesAinfo.this.startActivity(intent);
				} else {

					intent.putExtra("id", infoarray.get(arg2).get("iD"));
					intent.putExtra("newsClassID",
							infoarray.get(arg2).get("newsClassID"));
					intent.setClass(BusinesAinfo.this, Info_info.class);
					BusinesAinfo.this.startActivity(intent);
				}

			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PreferencesUtils.putString(BusinesAinfo.this, "storeID", array.get(arg2).get("storeId"));
				Constant.SHOP_ID = array.get(arg2).get("storeId");
				Intent intent = new Intent();
				intent.setClass(BusinesAinfo.this, ShopDetailsActivity.class);
				BusinesAinfo.this.startActivity(intent);

			}
		});
		bbs_lit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent();
				intent.setClass(BusinesAinfo.this, Post_info.class);
				intent.putExtra("tid", bbsarray.get(arg2).get("tid"));
				BusinesAinfo.this.startActivity(intent);
				
			}
		});
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// textView.setText(intent.getExtras().getString("data"));
			if (intent.getExtras().getString("attid").equals("0")) {
				ba_guanzhu.setBackgroundResource(R.drawable.com_plus);
				attid = intent.getExtras().getString("attid");
			} else {
				ba_guanzhu.setBackgroundResource(R.drawable.com_yes);
				attid = intent.getExtras().getString("attid");
			}
		}
	};

	public void getmessage(final String url, final int what) {
		new Thread() {
			public void run() {
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if(what==9){
					delect = httpRequest.doGet(url, BusinesAinfo.this);
				}else if(what==7){
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
					handler.sendEmptyMessage(7);
				}else{
					if(bbsdomain.equals("0")){
						
					}else{
						bbslist=httpRequest.doGet(bbsurl, BusinesAinfo.this);
					}
					hotlist = httpRequest.doGet(hoturl, BusinesAinfo.this);
					newlist = httpRequest.doGet(newurl, BusinesAinfo.this);
					countlist = httpRequest.doGet(countsurl, BusinesAinfo.this);
					if (hotlist.equals("网络超时") || newlist.equals("网络超时")
							|| countlist.equals("网络超时")) {
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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				try {
					name(hotlist);
					myPager.setOnPageChangeListener(new MyListener());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					for (int i = 0; i < urls.length; i++) {
						item = inflater.inflate(R.layout.item, null);
						((TextView) item.findViewById(R.id.infor_title))
								.setText(imgarray.get(i).get("description"));
						listViews.add(item);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				float a = (float) (10.0 / 16.0);
				LayoutParams params = new LayoutParams(dm.widthPixels,
						(int) (dm.widthPixels * (a)));
				myPager.setLayoutParams(params);
				myviewpageadapater = new Myviewpageadapater(listViews,
						BusinesAinfo.this, urls, imgarray,2);

				myPager.setAdapter(myviewpageadapater);
				try {
					initIndicator();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				handler.sendEmptyMessage(2);
				break;
			case 2:
				try {
					infomlist(newlist);
					setlist();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(4);
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(BusinesAinfo.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				try {
					setmap(countlist);
					name();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(10);
				break;
			case 5:
				dialog.dismiss();
				Toast.makeText(BusinesAinfo.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 6:
				String userId = PreferencesUtils.getString(BusinesAinfo.this,
						"userid");
				int areaId = PreferencesUtils.getInt(BusinesAinfo.this,
						"cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ siteid + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				getmessage(posturl, 7);
				break;
			case 7:
				if (object == null) {
					Toast.makeText(BusinesAinfo.this, "响应失败",
							Toast.LENGTH_SHORT).show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							attid = jsonObject.getString("id");
							Toast.makeText(BusinesAinfo.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							ba_guanzhu
									.setBackgroundResource(R.drawable.com_yes);
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", attid);
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(BusinesAinfo.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 8:
				String url = Constant.url+"attention/deleteAttention?id="
						+ attid;
				getmessage(url, 9);
				break;
			case 9:
				try {
					JSONObject jsonObject = new JSONObject(delect);
					int stat = jsonObject.getInt("status");
					if (stat == 0) {
						attid = "0";
						Toast.makeText(BusinesAinfo.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						ba_guanzhu.setBackgroundResource(R.drawable.com_plus);
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", attid);
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);

					} else {

						Toast.makeText(BusinesAinfo.this, "取消失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 10:
				if(bbsdomain.equals("0")){
					dongtais.setVisibility(View.GONE);
					jiaoliu.setVisibility(View.INVISIBLE);
				}else{
					dongtais.setVisibility(View.VISIBLE);
					jiaoliu.setVisibility(View.VISIBLE);
					try {
						bbs_listinfo(bbslist);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setbbslust();
				}
				break;
			default:
				break;
			}
		};
	};

	public void name(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			urls = new String[jsonArray.length()];
			indicator_imgs = new ImageView[urls.length];
			for (int i = 0; i < jsonArray.length(); i++) {
				imghashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				imghashMap.put("description",
						jsonObject2.getString("description"));
				imghashMap.put("picId", jsonObject2.getString("picId"));
				imghashMap.put("orPpt", jsonObject2.getString("orPpt"));
				imghashMap.put("iD", jsonObject2.getString("id"));
				imghashMap.put("classID", jsonObject2.getString("newsClassId"));
				urls[i] = jsonObject2.getString("picId");
				imgarray.add(imghashMap);
			}
		}

	}

	public void infomlist(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			text = new String[jsonArray.length()];
			time = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				infohashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				infohashMap.put("iD", jsonObject2.getString("id"));
				infohashMap.put("newsClassID",
						jsonObject2.getString("newsClassId"));
				infohashMap.put("title", jsonObject2.getString("title"));
				infohashMap.put("orPpt", jsonObject2.getString("orPpt"));
				infohashMap.put("addTime", jsonObject2.getString("addTime")
						.substring(0, 10));
				text[i] = jsonObject2.getString("title");
				time[i] = jsonObject2.getString("addTime").substring(0, 10);
				infoarray.add(infohashMap);
			}
		}

	}

	public void setmap(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");

		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				hashMap = new HashMap<String, String>();
				hashMap.put("storeId", jsonObject2.getString("storeId"));
				array.add(hashMap);
				ImageAndText iat = new ImageAndText(
						jsonObject2.getString("logoPicID"),
						jsonObject2.getString("storeName"));
				listImageAndText.add(iat);
			}

		}

	}
	public void bbs_listinfo(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 5;
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				bbshashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				bbshashMap.put("attachment", jsonObject2.getString("attachment"));
				bbshashMap.put("author", jsonObject2.getString("author"));
				bbshashMap.put("authorid", jsonObject2.getString("authorid"));
				bbshashMap.put("dateline", jsonObject2.getString("dateline"));
				bbshashMap.put("digest", jsonObject2.getString("digest"));
				bbshashMap.put("displayorder",
						jsonObject2.getString("displayorder"));
				bbshashMap.put("fid", jsonObject2.getString("fid"));
				bbshashMap.put("heats", jsonObject2.getString("heats"));
				bbshashMap.put("icon", jsonObject2.getString("icon"));
				bbshashMap.put("lastpost", jsonObject2.getString("lastpost"));
				bbshashMap.put("lastposter", jsonObject2.getString("lastposter"));
				bbshashMap.put("replies", jsonObject2.getString("replies"));
				bbshashMap.put("special", jsonObject2.getString("special"));
				bbshashMap.put("stamp", jsonObject2.getString("stamp"));
				bbshashMap.put("subject", jsonObject2.getString("subject"));
				bbshashMap.put("tid", jsonObject2.getString("tid"));
				bbshashMap.put("views", jsonObject2.getString("views"));
				bbsarray.add(bbshashMap);
			}

		}
	}
	public void setbbslust() {
		bBsadapter = new BBsadapter(bbsarray, BusinesAinfo.this);
		bbs_lit.setAdapter(bBsadapter);

	}

	public void name() {

		gmadapter = new ImageTextListAdapter(BusinesAinfo.this,
				listImageAndText, gridview);
		gridview.setAdapter(gmadapter);
	}

	public void setlist() {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		for (int i = 0; i < text.length; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("imageItem", text[i]);
			item.put("textItem", time[i]);
			items.add(item);
		}
		adapter = new SimpleAdapter(BusinesAinfo.this, items,
				R.layout.busa_new_item,
				new String[] { "imageItem", "textItem" }, new int[] {
						R.id.ba_new_tit, R.id.ba_new_time });
		shangxielist.setAdapter(adapter);
	}

	private void initIndicator() {

		ImageView imgView;
		View v = findViewById(R.id.ba_indicator);// 线性水平布局，负责动态调整导航图标

		for (int i = 0; i < urls.length; i++) {
			imgView = new ImageView(this);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 10, 7, 10);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;
			if (i == 0) { // 初始化第一个为选中状态

				indicator_imgs[i]
						.setBackgroundResource(R.drawable.indicator_focused);
			} else {
				indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
			}
			((ViewGroup) v).addView(indicator_imgs[i]);

		}

	}

	private class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			if (state == 0) {
				// new MyAdapter(null).notifyDataSetChanged();
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			try {
				// 改变所有导航的背景图片为：未选中

				for (int i = 0; i < indicator_imgs.length; i++) {

					indicator_imgs[i]
							.setBackgroundResource(R.drawable.indicator);
					System.out.println(i);

				}
				// 改变当前背景图片为：选中
				indicator_imgs[position]
						.setBackgroundResource(R.drawable.indicator_focused);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.jianjie:
			Intent intent = new Intent();
			intent.putExtra("review", barenzheng.getText().toString());
			intent.putExtra("attid", attid);
			intent.putExtra("addTime", batime.getText().toString());
			intent.putExtra("counts", bapeople.getText().toString());
			intent.putExtra("title", batitle.getText().toString());
			intent.putExtra("siteid", siteid);
			intent.setClass(BusinesAinfo.this, Bussainfo.class);
			BusinesAinfo.this.startActivity(intent);
			break;

		case R.id.dongtai:
			Intent intent2 = new Intent();
			intent2.putExtra("attid", attid);
			intent2.putExtra("review", barenzheng.getText().toString());
			intent2.putExtra("addTime", batime.getText().toString());
			intent2.putExtra("counts", bapeople.getText().toString());
			intent2.putExtra("title", batitle.getText().toString());
			intent2.putExtra("siteid", siteid);
			intent2.setClass(BusinesAinfo.this, Bussanews.class);
			BusinesAinfo.this.startActivity(intent2);
			break;
		case R.id.danwei:
			Intent intent3 = new Intent();
			intent3.putExtra("attid", attid);
			intent3.putExtra("review", barenzheng.getText().toString());
			intent3.putExtra("addTime", batime.getText().toString());
			intent3.putExtra("counts", bapeople.getText().toString());
			intent3.putExtra("title", batitle.getText().toString());
			intent3.putExtra("siteid", siteid);
			intent3.setClass(BusinesAinfo.this, Bamember.class);
			BusinesAinfo.this.startActivity(intent3);
			break;
		case R.id.jiaoliu:
			Intent intent9 = new Intent();
			intent9.putExtra("attid", attid);
			intent9.putExtra("review", barenzheng.getText().toString());
			intent9.putExtra("addTime", batime.getText().toString());
			intent9.putExtra("counts", bapeople.getText().toString());
			intent9.putExtra("title", batitle.getText().toString());
			intent9.putExtra("bbsdomain", bbsdomain);
			intent9.putExtra("siteid", siteid);
			intent9.setClass(BusinesAinfo.this, Bussexchange.class);
			BusinesAinfo.this.startActivity(intent9);
			break;
		case R.id.ba_guanzhu:
			if (attid.equals("0")) {
				handler.sendEmptyMessage(6);
			} else {
				handler.sendEmptyMessage(8);
			}

			break;
		case R.id.bus_p_more:
			Intent intent5 = new Intent();
			intent5.putExtra("attid", attid);
			intent5.putExtra("review", barenzheng.getText().toString());
			intent5.putExtra("addTime", batime.getText().toString());
			intent5.putExtra("counts", bapeople.getText().toString());
			intent5.putExtra("title", batitle.getText().toString());
			intent5.putExtra("siteid", siteid);
			intent5.setClass(BusinesAinfo.this, Bamember.class);
			BusinesAinfo.this.startActivity(intent5);
			break;
		case R.id.bus_news_more:
			Intent intent6 = new Intent();
			intent6.putExtra("attid", attid);
			intent6.putExtra("review", barenzheng.getText().toString());
			intent6.putExtra("addTime", batime.getText().toString());
			intent6.putExtra("counts", bapeople.getText().toString());
			intent6.putExtra("title", batitle.getText().toString());
			intent6.putExtra("siteid", siteid);
			intent6.setClass(BusinesAinfo.this, Bussanews.class);
			BusinesAinfo.this.startActivity(intent6);
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
