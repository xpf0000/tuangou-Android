package com.X.tcbj.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.News_adpater;
import com.X.tcbj.myview.My_GridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class testnews extends FragmentActivity {
	/**
	 * Tab标题
	 */
	private static String[] TITLE = new String[] { "本地", "图片", "焦点" };
	private static String[] TITLE2 = new String[] { "本地", "图片", "焦点"  };
	LinearLayout layout,wifi_err;
	PopupWindow popupWindow;
	View popView;
	//TabPageIndicator indicator;
	ArrayList<HashMap<String, String>> Attentionarray=new ArrayList<>();
	HashMap<String, String> AttentionhashMap;
	ArrayList<HashMap<String, String>> notAttentionarray=new ArrayList<>();
	HashMap<String, String> notAttentionhashMap;
	String Attentionstr, navId, userId, url, classId, deleteNavigationurl,
			object, addNavigationurl;
	int areaId;
	My_GridView attgr, noatter;
	News_adpater adpater, noadpater;
	String str;
	Object addNavigationst;
	int a = 0, select = 0;
	String tie = "本地";
	ViewPager pager;
	TabPageIndicatorAdapter adapter;
	ImageView diyinfo_back, informa_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		deleteNavigationurl = Constant.url+"navigation/deleteNavigation?navId="
				+ navId;
		// 实例化ViewPager， 然后给ViewPager设置Adapter
		layout = (LinearLayout) findViewById(R.id.poplay);
		wifi_err=(LinearLayout)findViewById(R.id.wifi_err);
		wifi_err.setVisibility(View.GONE);
		pager = (ViewPager) findViewById(R.id.pager);
		diyinfo_back = (ImageView) findViewById(R.id.diyinfo_back);
		informa_search = (ImageView) findViewById(R.id.informa_search);
		adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		int Logn = PreferencesUtils.getInt(testnews.this, "logn");
		areaId = PreferencesUtils.getInt(testnews.this, "cityID");
		if (Logn == 0) {
			url = Constant.url+"navigation/getNavigationList?userId="
					+ 0 + "&areaId=" + areaId;
			attention(url, 1);
		} else {
			userId = PreferencesUtils.getString(testnews.this, "userid");
			url = Constant.url+"navigation/getNavigationList?userId="
					+ userId + "&areaId=" + areaId;
			attention(url, 1);
		}
//		// 实例化TabPageIndicator，然后与ViewPager绑在一起（核心步骤）
//		indicator = (TabPageIndicator) findViewById(R.id.indicator);
//		indicator.setViewPager(pager);
//		// 如果要设置监听ViewPager中包含的Fragment的改变(滑动切换页面)，使用OnPageChangeListener为它指定一个监听器，那么不能像之前那样直接设置在ViewPager上了，而要设置在Indicator上，
//		indicator.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				try {
//					tie = TITLE[arg0];
//					a = arg0;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});
		wifi_err.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				attention(url, 1);
			}
		});
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int Logn = PreferencesUtils.getInt(testnews.this, "logn");
				if (Logn == 0) {
					Toast.makeText(testnews.this, "还没登陆，先登录吧",
							Toast.LENGTH_SHORT).show();
					// Intent intent = new Intent();
					// intent.setClass(testnews.this, LoginActivity.class);
					// testnews.this.startActivity(intent);
				} else {
					showpop();
				}

			}
		});
		diyinfo_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < TITLE.length; i++) {
					PreferencesUtils.putString(testnews.this, TITLE[i], null);
					PreferencesUtils.putInt(testnews.this, TITLE[i] + "my", -1);
				}
				PreferencesUtils.putString(testnews.this, "imagehead", null);
				finish();

			}
		});
		informa_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(testnews.this, News_search.class);
				testnews.this.startActivity(intent);

			}
		});
	}

	/**
	 * 定义ViewPager的适配器
	 */
	class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
		private int mChildCount = 0;

		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			Fragment fragment = new ItemFragment();
			Bundle args = new Bundle();
			args.putString("arg", TITLE[position]);
			args.putString("classID", TITLE2[position]);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}
	}

	void showpop() {
		popView = testnews.this.getLayoutInflater().inflate(R.layout.news_pop,
				null);
		popupWindow = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.update();
		//popupWindow.showAsDropDown(indicator);
		attgr = (My_GridView) popView.findViewById(R.id.att_gridview);
		noatter = (My_GridView) popView.findViewById(R.id.noatt_gridview);
		attgr();
		noattgr();
		attgr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(Attentionarray.get(arg2).get("className").equals(tie)){
					Toast.makeText(testnews.this, "此选项为当前浏览页无法删除", Toast.LENGTH_SHORT).show();
				}else{
					
				
				select = arg2;
				navId = Attentionarray.get(arg2).get("Id");
				deleteNavigationurl = Constant.url+"navigation/deleteNavigation?navId="
						+ navId;
				attention(deleteNavigationurl, 3);
				PreferencesUtils.putString(testnews.this, Attentionarray.get(arg2).get("className"), null);
				}
			}
		});
		noatter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				classId = notAttentionarray.get(arg2).get("classId");
				str = "userId=" + userId + "&areaId=" + areaId + "&classId="
						+ classId;
				addNavigationurl = Constant.url+"navigation/addNavigation?"
						+ classId;
				attention(addNavigationurl, 4);
			}
		});
	}

	public void attention(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if (what == 4) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					addNavigationst = myhttpRequest.request(url, str, "POST");
					handler.sendEmptyMessage(4);
				} else if (what == 3) {
					object = httpRequest.doGet(url, testnews.this);
					handler.sendEmptyMessage(3);
				} else {
					Attentionstr = httpRequest.doGet(url, testnews.this);
					if (Attentionstr.equals("网络超时")) {
						handler.sendEmptyMessage(5);
					}else{
						handler.sendEmptyMessage(what);
					}
				}

				Looper.loop();
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				wifi_err.setVisibility(View.GONE);
				try {
					Attentionlist(Attentionstr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TITLE = new String[Attentionarray.size() + 3];
				TITLE[0] = "本地";
				TITLE[1] = "图片";
				TITLE[2] = "焦点";
				TITLE2 = new String[Attentionarray.size() + 3];
				TITLE2[0] = "本地";
				TITLE2[1] = "图片";
				TITLE2[2] = "焦点";
				for (int i = 0; i < Attentionarray.size(); i++) {
					TITLE[3 + i] = Attentionarray.get(i).get("className");
					TITLE2[3 + i] = Attentionarray.get(i).get("classId");
				}
				//indicator.notifyDataSetChanged();

				if (a == (select + 3)) {
					//indicator.onPageSelected(a);
					pager.setCurrentItem(a);

				} else {
					System.out.println(a + "****" + select);
					for (int j = 0; j < TITLE.length; j++) {
						if (tie.equals(TITLE[j])) {
							a = j;
							break;
						}
					}
					adapter.notifyDataSetChanged();
					//indicator.onPageSelected(a);
					// pager.setCurrentItem(a);
				}
				
				handler.sendEmptyMessage(2);
				break;
			case 2:
				try {
					attgr();
					noattgr();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("第一次进入");
				}
				break;
			case 3:
				if (object == null) {

					Toast.makeText(testnews.this, "删除失败，网络超时！",
							Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject jsonObject = new JSONObject(object);

						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {

							Toast.makeText(testnews.this, "删除成功",
									Toast.LENGTH_SHORT).show();
							attention(url, 1);

						} else {

							Toast.makeText(testnews.this, "删除失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
			case 4:
				if (addNavigationst == null) {
					Toast.makeText(testnews.this, "添加失败，网络超时！",
							Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject jsonObject = new JSONObject(
								addNavigationst.toString());
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							;
							Toast.makeText(testnews.this, "添加成功",
									Toast.LENGTH_SHORT).show();
							attention(url, 1);
						} else {

							Toast.makeText(testnews.this, "添加失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 5:
				wifi_err.setVisibility(View.VISIBLE);
				Toast.makeText(testnews.this, "网络异常！！"
						+ "", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	public void Attentionlist(String str) throws JSONException {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else if (jsonObject.getString("status").equals("网络超时")) {
			handler.sendEmptyMessage(5);
		} else {
			com.alibaba.fastjson.JSONArray jsonArray = jsonObject.getJSONArray("list");
			com.alibaba.fastjson.JSONObject jsonObject2 =  jsonArray.getJSONObject(0);
			com.alibaba.fastjson.JSONArray jsonArray3 = jsonObject2.getJSONArray("listYes");
			Attentionarray = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < jsonArray3.size(); i++) {
				AttentionhashMap = new HashMap<String, String>();
				com.alibaba.fastjson.JSONObject jsonObject3 =  jsonArray3.getJSONObject(i);
				AttentionhashMap.put("className",
						jsonObject3.getString("className"));
				AttentionhashMap.put("classId",
						jsonObject3.getString("classId"));

				AttentionhashMap.put("Id", jsonObject3.getString("Id")== null ? ""
						: jsonObject3.getString("Id"));
				Attentionarray.add(AttentionhashMap);
			}
			notAttentionarray = new ArrayList<HashMap<String, String>>();
			com.alibaba.fastjson.JSONObject jsonObject3 = jsonArray.getJSONObject(1);
			com.alibaba.fastjson.JSONArray jsonArray4 = jsonObject3.getJSONArray("listNo");
			for (int i = 0; i < jsonArray4.size(); i++) {
				notAttentionhashMap = new HashMap<String, String>();
				com.alibaba.fastjson.JSONObject jsonObject4 =  jsonArray4.getJSONObject(i);
				notAttentionhashMap.put("className",
						jsonObject4.getString("className"));
				notAttentionhashMap.put("classId",
						jsonObject4.getString("classId"));
				notAttentionarray.add(notAttentionhashMap);
			}
		}
	}

	void attgr() {
		adpater = new News_adpater(Attentionarray, testnews.this);
		attgr.setAdapter(adpater);
	}

	void noattgr() {
		noadpater = new News_adpater(notAttentionarray, testnews.this);
		noatter.setAdapter(noadpater);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < TITLE.length; i++) {
				PreferencesUtils.putString(testnews.this, TITLE[i], null);
				PreferencesUtils.putInt(testnews.this, TITLE[i] + "my", -1);
			}
			PreferencesUtils.putString(testnews.this, "imagehead", null);
			finish();
		}
		return false;

	}
}
