package com.X.tcbj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ThematicDetails extends FragmentActivity {
	private static String[] TITLE = new String[] {};
	ArrayList<HashMap<String, String>> imgarray ;
	HashMap<String, String> imghashMap;
	TextView diy_title;
	ViewPager pager;
	TabPageIndicatorAdapter adapter;
	TabPageIndicator indicator;
	String url, ColumnStr,picture;
	String id,title;
	ImageView informa_search, diyinfo_back;
	LinearLayout poplay, wifi_err;
	TextView txt_buiness_share;
	int a=0;
	String leixing,ID,topic;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		ShareSDK.initSDK(this);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		diy_title = (TextView) findViewById(R.id.diy_title);
		txt_buiness_share=(TextView) findViewById(R.id.txt_buiness_share);
		txt_buiness_share.setVisibility(View.VISIBLE);
		informa_search = (ImageView) findViewById(R.id.informa_search);
		informa_search.setVisibility(View.GONE);
		diyinfo_back = (ImageView) findViewById(R.id.diyinfo_back);
		poplay = (LinearLayout) findViewById(R.id.poplay);
		wifi_err = (LinearLayout) findViewById(R.id.wifi_err);
		wifi_err.setVisibility(View.GONE);
		poplay.setVisibility(View.GONE);
		indicator.setViewPager(pager);
		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");
		picture=getIntent().getStringExtra("picture");
		// 设置边距
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		lp.setMargins(0, 0, 0, 0);
		indicator.setLayoutParams(lp);
		url = Constant.url+"topics/topicsColumn?id=" + id;
		txt_buiness_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnekeyShare oks = new OnekeyShare();
				oks.disableSSOWhenAuthorize();// 分享前要先授权
				// 分享时Notification的图标和文字
//				oks.setNotification(R.drawable.ic_launcher,
//						getString(R.string.app_name));
//
				if (picture==null){
					picture="http://image.rexian.cn/images/applogo.png";
				}
					oks.setImageUrl(picture);
					int cityID = PreferencesUtils.getInt(ThematicDetails.this, "cityID");
				String cityName=PreferencesUtils.getString(ThematicDetails.this, "cityName");
				oks.setTitle(title + cityName.substring(0, cityName.length()) + "城市热线");
				String city=PreferencesUtils.getString(ThematicDetails.this, "cityNamepl");
				oks.setTitleUrl("http://m.rexian.cn/topic/TopicsColumn/"+leixing+"/"+ID+"/"+cityID+"/"+"0"+"/"+"0"+"/"+topic);//商家地址分享
				oks.setText(title + "\r\n点击查看更多" +"http://m.rexian.cn/topic/TopicsColumn/"+leixing+"/"+ID+"/"+cityID+"/"+"0"+"/"+"0"+"/"+topic);
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite("新闻");
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setUrl("http://m.rexian.cn/topic/TopicsColumn/"+leixing+"/"+ID+"/"+cityID+"/"+"0"+"/"+"0"+"/"+topic);
				oks.setSiteUrl("luoyang.rexian.cn");
				oks.show(ThematicDetails.this);
				
			}
		});
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// Toast.makeText(ThematicDetails.this, TITLE[arg0],
				// 1000).show();
				diy_title.setText(TITLE[arg0]);
				if (imgarray.get(arg0).get("url")==null||imgarray.get(arg0).get("url").length()==0){

				}else {
					Uri uri = Uri.parse(imgarray.get(arg0).get("url"));
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		diyinfo_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TITLE = new String[] {};
				adapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
				finish();

			}
		});
		wifi_err.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getColumn();

			}
		});
		getColumn();
	}

	class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
		private int mChildCount = 0;

		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			Fragment fragment = new SeminarFragment();
			Bundle args = new Bundle();
				try{
					args.putString("arg", TITLE[position]);
					args.putString("content", imgarray.get(position).get("content"));
					args.putString("templateId",
							imgarray.get(position).get("templateId"));
					leixing=imgarray.get(position).get("templateId");
					args.putString("id",
							imgarray.get(position).get("id"));
					ID=imgarray.get(position).get("id");
					args.putString("topicId",
							imgarray.get(position).get("topicId"));
					topic=imgarray.get(position).get("topicId");
				}catch (Exception e){
					e.printStackTrace();
				}
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

	void getColumn() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				HttpRequest httpRequest = new HttpRequest();
				ColumnStr = httpRequest.doGet(url, ThematicDetails.this);
				if (ColumnStr.equals("网络超时")) {
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				setmap(ColumnStr);
				try {
					diy_title.setText(TITLE[0]);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				adapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
				wifi_err.setVisibility(View.GONE);
				a=1;
				break;
			case 2:
				a=1;
				Toast.makeText(ThematicDetails.this, "网络超时", Toast.LENGTH_SHORT)
						.show();
				wifi_err.setVisibility(View.VISIBLE);
				break;
			case 3:
				a=1;
				Toast.makeText(ThematicDetails.this, "服务器挂了",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	void setmap(String str) {
		JSONObject jsonObject = JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		imgarray=new ArrayList<HashMap<String, String>>();
		if (a == 0) {
			JSONArray array = jsonObject.getJSONArray("list");
			TITLE = new String[array.size()];
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject2 = array.getJSONObject(i);
				TITLE[i] = jsonObject2.getString("name")+" ";
				imghashMap=new HashMap<String, String>();
				imghashMap.put("templateId",
						jsonObject2.getString("templateId")== null ? ""
								: jsonObject2.getString("templateId"));
				imghashMap.put("content", jsonObject2.getString("content")== null ? ""
						: jsonObject2.getString("content"));
				imghashMap.put("id", jsonObject2.getString("id")== null ? ""
						: jsonObject2.getString("id"));
				imghashMap.put("topicId", jsonObject2.getString("id")== null ? ""
						: jsonObject2.getString("topicId"));
				imghashMap.put("url", jsonObject2.getString("url")== null ? ""
						: jsonObject2.getString("url"));
				imgarray.add(imghashMap);
			}

		} else {
			handler.sendEmptyMessage(3);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (a==1){
				try{
					if(TITLE.length==0&&(!ColumnStr.equals("网络超时"))){

					}else{
						for (int i = 0; i < TITLE.length; i++) {
							PreferencesUtils.putString(ThematicDetails.this, TITLE[i], null);
						}
						TITLE = new String[] {};
						pager.removeAllViews();
						adapter.notifyDataSetChanged();
						indicator.notifyDataSetChanged();
						finish();
					}
				}catch (Exception e){
					finish();
				}
			}else {

			}

		}
		return false;

	}

}
