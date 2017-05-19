package com.hkkj.csrx.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hkkj.csrx.adapter.ShopPrivilegeBaseAdapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.umeng.analytics.MobclickAgent;

/**
 * 商家优惠列表
 * 
 * @author zmz
 * 
 */
public class ShopPrivilegeActivity extends Activity implements
		OnItemClickListener, OnScrollListener {
	ImageView img_back;
	ListView lv_privilege;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;
	ShopPrivilegeBaseAdapter privilegeBaseAdapter;

	int pages = 1;
	int pageItems = 10;
	String count;
	int tmp;
	String totalPage;

	String path =Constant.url+"GetStoresPromotionListandstores?storeId="
			+ Constant.SHOP_ID + "&page="+pages+"&pageSize="+pageItems;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_privilege);
		lv_privilege = (ListView) findViewById(R.id.lv_shop_privilege);
		img_back = (ImageView) findViewById(R.id.img_privilege_activity_back);
		img_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ShopPrivilegeActivity.this.finish();// 关闭页面
			}
		});
		myInfo(1, path);
	}

	/**
	 * 为listView赋值
	 */
	public void setList() {
		privilegeBaseAdapter = new ShopPrivilegeBaseAdapter(
				ShopPrivilegeActivity.this, list);
		lv_privilege.setAdapter(privilegeBaseAdapter);
		lv_privilege.setOnItemClickListener(this);
		lv_privilege.setOnScrollListener(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			// msg.what=1时，数据初次加载
			// msg.what=2时，数据更新
			// msg.what=3时，数据加载失败
			switch (msg.what) {
			case 1:
				setList();
				break;
			case 2:
				Toast.makeText(ShopPrivilegeActivity.this, "数据加载完毕",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	/**
	 * 加载数据时开启一个新线程
	 */
	public void myInfo(final int what, final String path) {
		new Thread() {
			public void run() {
				getPrivilegeJson(GetMyData.getURLString(path));// 获得解析的数据
				Message msg = new Message();
				msg.what = what;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 解析商家优惠数据
	 * 
	 * @param str
	 */
	public void getPrivilegeJson(String str) {

		try {
			// list.clear();// 清空之前的数据
			JSONObject jo = new JSONObject(str);
			JSONArray ja = jo.getJSONArray("list");
			count = jo.getString("totalRecord");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo2 = (JSONObject) ja.get(i);
				map = new HashMap<String, String>();
				map.put("ID", jo2.getString("ID"));
				map.put("Title", jo2.getString("Title"));
				map.put("PicID", jo2.getString("PicID"));
				map.put("Address", jo2.getString("Address"));
				map.put("EndTime", jo2.getString("EndTime"));
				list.add(map);
			}
			int i1 = Integer.parseInt(count);

			if (i1 % 10 == 0) {
				tmp = i1 / 10;
			} else {
				tmp = i1 / 10 + 1;
			}
			totalPage = tmp + "";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 进入优惠详情界面
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("StoreID", Constant.SHOP_ID);
		intent.putExtra("ID", list.get(arg2).get("ID"));
		intent.setClass(ShopPrivilegeActivity.this, Privileinfo.class);
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
		// 当不滚动时
		case OnScrollListener.SCROLL_STATE_IDLE:
			// 判断滚动到底部
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				if (pages == tmp) {
					handler.sendEmptyMessage(2);
				} else {
					pages++;
					path = Constant.url+"GetStoresPromotionListandstores?storeId="
							+ Constant.SHOP_ID + "&page="+pages+"&pageSize="+pageItems;
					myInfo(1, path);
				}
			}
			break;
		}
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
