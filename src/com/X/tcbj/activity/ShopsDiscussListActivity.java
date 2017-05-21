package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.BusinessDetailsAdapter;
import com.X.tcbj.utils.GetMyData;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 商家评论列表
 * 
 * @author zmz
 * 
 */
public class ShopsDiscussListActivity extends Activity implements
		OnClickListener {
	ImageView img_back;// 返回原来页面
	ImageView img_makeComment;// 点击进入评论页面
	ListView lv_comment;// 评论列表
	TextView txt_pinglun;// 评论提示
	Dialog dialog;
	int logn;// 用户登录状态
	private String statusComment;// 商家评论 判断是否返回有数据：1：有数据，-1：无数据
//	String path = Constant.url+"storecomment?storeId="
//			+ Constant.SHOP_ID + "&page=1&pageSize="+ShopDetailsActivity.commentTotal;
	ArrayList<HashMap<String, String>> listComment = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> mapComment;

	BusinessDetailsAdapter detailsAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shops_discuss_list);
		dialog = GetMyData.createLoadingDialog(ShopsDiscussListActivity.this,
				"正在拼命的加载······");
		dialog.show();
		init();
		myInfo(1);
	}

	public void init() {
		img_back = (ImageView) findViewById(R.id.img_comment_list_back);
		img_makeComment = (ImageView) findViewById(R.id.img_comment_list_comment);
		txt_pinglun = (TextView) findViewById(R.id.txt_comment_list_pinglun);
		img_back.setOnClickListener(this);
		img_makeComment.setOnClickListener(this);
		txt_pinglun.setOnClickListener(this);
		lv_comment = (ListView) findViewById(R.id.lv_comment_list);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			// msg.what=1时，数据初次加载
			// msg.what=2时，数据更新
			// msg.what=3时，数据加载失败
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				setData();
				break;
			case 2:
				break;
			}
		};
	};

	/**
	 * 加载数据时开启一个新线程
	 */
	public void myInfo(final int what) {
		new Thread() {
			public void run() {
//				getCommentJson(GetMyData.getURLString(path));
//				System.out.println("path:"+path);
				Message msg = new Message();
				msg.what = what;
				handler.sendMessage(msg);
			};
		}.start();
	}

	public void setData() {
		detailsAdapter = new BusinessDetailsAdapter(
				ShopsDiscussListActivity.this, listComment);
		lv_comment.setAdapter(detailsAdapter);
	}

	/**
	 * 解析商家详情评论数据
	 * 
	 * @param str
	 */
	public void getCommentJson(String str) {
		try {
			listComment.clear();// 清空之前的数据
			JSONObject jo = new JSONObject(str);
			statusComment = jo.getString("status");
			// 有数据再解析
			if (statusComment.equals("0")) {
				JSONArray ja = jo.getJSONArray("list");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo2 = (JSONObject) ja.get(i);
					mapComment = new HashMap<String, String>();
					mapComment.put("user", jo2.getString("user"));
					mapComment.put("userid", jo2.getString("userid"));
					mapComment.put("number", jo2.getString("number"));
					mapComment.put("text", jo2.getString("text"));
					mapComment.put("date", jo2.getString("date"));
					mapComment.put("PicUrl", jo2.getString("PicUrlList"));
					listComment.add(mapComment);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		logn = PreferencesUtils.getInt(ShopsDiscussListActivity.this, "logn");
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_comment_list_back:
			ShopsDiscussListActivity.this.finish();
			break;
		case R.id.img_comment_list_comment:
			Intent intent = new Intent(ShopsDiscussListActivity.this,
					ShopsDiscussActivity.class);
			startActivity(intent);
			break;
		case R.id.txt_comment_list_pinglun:
			if (logn == 0) {
				Intent intent2 = new Intent(ShopsDiscussListActivity.this,
						LoginActivity.class);
				startActivity(intent2);
			} else {
				Intent intent2 = new Intent(ShopsDiscussListActivity.this,
						ShopsDiscussActivity.class);
				startActivity(intent2);
			}

			break;
		default:
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
