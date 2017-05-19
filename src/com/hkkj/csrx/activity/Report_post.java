package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.Bbs.MyHandler;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.hkkj.csrx.utils.Timechange;
import com.hkkj.server.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Report_post extends Activity {
	String url, alllist, reurl, tid = "",uid;
	int siteid, allindex;
	TextView bbs_choose_class, post_write;
	ArrayList<HashMap<String, String>> allbigarray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> allbsmallarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> allbigmap;
	HashMap<String, String> samllmap;
	ListView bbs_lv_big, bbs_lv_small;
	SimpleAdapter adapter, bigadpater;
	LinearLayout bbs_alllist, bbs_choose_clas;
	EditText bbs_re_tit, bbs_re_info;
	int a = 1;
	Object object;
	location location=null;
	private MyHandler mHandler = null;
	Timechange timechange;
	Dialog dialog;
	ImageView post_h_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_post);
		dialog = GetMyData.createLoadingDialog(Report_post.this, "正在拼命的加载······");
		siteid = PreferencesUtils.getInt(Report_post.this, "cityID");
		bbs_lv_big = (ListView) findViewById(R.id.bbs_lv_big);
		bbs_lv_small = (ListView) findViewById(R.id.bbs_lv_small);
		bbs_alllist = (LinearLayout) findViewById(R.id.bbs_alllist);
		bbs_choose_class = (TextView) findViewById(R.id.bbs_choose_class);
		bbs_choose_clas = (LinearLayout) findViewById(R.id.bbs_choose_clas);
		bbs_re_tit = (EditText) findViewById(R.id.bbs_re_tit);
		bbs_re_info = (EditText) findViewById(R.id.bbs_re_info);
		post_write = (TextView) findViewById(R.id.post_write);
		post_h_back=(ImageView)findViewById(R.id.post_h_back);
		location = (location) getApplication();
		mHandler = location.getHandler();
		uid = PreferencesUtils.getString(Report_post.this, "uid");
		url = Constant.url+"forum/groupall?siteid="
				+ siteid;
		info(1, url,null);
		bbs_choose_clas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (a == 1) {
					bbs_alllist.setVisibility(View.VISIBLE);
					a = 2;
				} else {
					bbs_alllist.setVisibility(View.GONE);
					a = 1;
				}

			}
		});
		post_h_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		bbs_lv_big.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				allindex = arg2;
				allbsmallarray.clear();
				try {
					samlllist(alllist);
					allsamlllist();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		bbs_lv_small.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tid = allbsmallarray.get(arg2).get("fid");
				bbs_choose_class.setText(allbsmallarray.get(arg2).get("name"));
				bbs_alllist.setVisibility(View.GONE);
			}
		});
		post_write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				timechange=new Timechange();
				if (tid.length() == 0) {
					Toast.makeText(Report_post.this, "请选择版块",
							Toast.LENGTH_SHORT).show();
				} else if (bbs_re_info.getText().toString().trim().length() == 0||Timechange.getWordCount(bbs_re_info.getText().toString().trim())<10) {
					Toast.makeText(Report_post.this, "请输入内容,或内容过短！",
							Toast.LENGTH_SHORT).show();
				} else if (bbs_re_tit.getText().toString().trim().length() == 0
						|| bbs_re_tit.length() > 30) {
					Toast.makeText(Report_post.this, "请输入合法的标题",
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.show();
					String Str = "uid="+uid+"&fid=" + tid + "&title="
							+ bbs_re_tit.getText().toString()
							+ "&context="
							+ bbs_re_info.getText().toString()+"&siteid="+siteid;
					reurl=Constant.url+"forum/p?";
					info(2, reurl,Str);
				}

			}
		});
	}

	public void info(final int what, final String url,final String str) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				if(what==1){
					HttpRequest httpRequest = new HttpRequest();
					alllist = httpRequest.doGet(url, Report_post.this);
				}else{
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, str, "POST");
					System.out.println(url);
					System.out.println(str);
				}
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();
			};
		}.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				try {
					allbig(alllist);
					allbiglist();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				if (object == null) {
					dialog.dismiss();
					Toast.makeText(Report_post.this, "响应失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							Toast.makeText(Report_post.this, "发表成功",
									Toast.LENGTH_SHORT).show();
							mHandler.sendEmptyMessage(5);
							dialog.dismiss();
							finish();
						} else {
							dialog.dismiss();
							Toast.makeText(Report_post.this, "发表失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3:

				break;
			default:
				break;
			}

		};
	};

	public void allbig(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		allbigarray.clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
			allbigmap = new HashMap<String, String>();
			allbigmap.put("name", jsonObject2.getString("name"));
			allbigarray.add(allbigmap);
		}
	}

	public void samlllist(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		JSONObject jsonObject2 = (JSONObject) jsonArray.get(allindex);
		JSONArray jsonArray3 = jsonObject2.getJSONArray("item");
		for (int i = 0; i < jsonArray3.length(); i++) {
			samllmap = new HashMap<String, String>();
			JSONObject jsonObject4 = (JSONObject) jsonArray3.get(i);
			samllmap.put("fid", jsonObject4.getString("fid"));
			samllmap.put("name", jsonObject4.getString("name"));
			allbsmallarray.add(samllmap);
		}

	}

	public void allsamlllist() {
		System.out.println(allbsmallarray.size());
		adapter = new SimpleAdapter(Report_post.this, allbsmallarray,
				R.layout.quanit_txt, new String[] { "name" },
				new int[] { R.id.quantext });
		bbs_lv_small.setAdapter(adapter);
	}

	public void allbiglist() {
		bigadpater = new SimpleAdapter(Report_post.this, allbigarray,
				R.layout.parent_list_item, new String[] { "name" },
				new int[] { R.id.txt_praent_item });
		bbs_lv_big.setAdapter(bigadpater);
	}

}
