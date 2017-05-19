package com.hkkj.csrx.activity;

import android.annotation.SuppressLint;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.ImageTextListAdapter;
import com.hkkj.csrx.myview.ImageAndText;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.hkkj.server.HKService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bamember extends Activity implements OnClickListener{
	TextView banewstitle, bainforenzheng, bainfotime, bainfopeople,more;
	String siteid, url, urlstr, attid, poststr,delect;
	GridView gridView;
	int page=1,totalRecord = 0;
	private List<ImageAndText> listImageAndText;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	private ImageTextListAdapter adapter;
	HttpRequest httpRequest;
	private LinearLayout.LayoutParams params;
	LinearLayout bam_shuaxin,mygridview;
	Button bam_freshen,bamember_guanzhu;
	Dialog dialog;
	Object object;
	ImageView business_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bamember);
		dialog = GetMyData.createLoadingDialog(Bamember.this,
				"正在拼命的加载····");
		dialog.show();
		banewstitle = (TextView) findViewById(R.id.bamembertitle);
		bainforenzheng = (TextView) findViewById(R.id.bamemberrenzheng);
		bainfotime = (TextView) findViewById(R.id.bamembertime);
		bainfopeople = (TextView) findViewById(R.id.bamemberpeople);
		bam_shuaxin=(LinearLayout)findViewById(R.id.bam_shuaxin);
		business_back=(ImageView)findViewById(R.id.business_back);
		business_back.setOnClickListener(this);
		bamember_guanzhu=(Button)findViewById(R.id.bamember_guanzhu);
		bamember_guanzhu.setOnClickListener(this);
		mygridview=(LinearLayout)findViewById(R.id.mygridview);
		bam_freshen=(Button)findViewById(R.id.bam_freshen);
		gridView=(GridView)findViewById(R.id.bam_gridview);
		more = (TextView) findViewById(R.id.bambutton);
		listImageAndText = new ArrayList<ImageAndText>();
		banewstitle.setText(getIntent().getStringExtra("title"));
		bainforenzheng.setText(getIntent().getStringExtra("review"));
		bainfotime.setText(getIntent().getStringExtra("addTime"));
		bainfopeople.setText(getIntent().getStringExtra("counts"));
		siteid = getIntent().getStringExtra("siteid");
		attid = getIntent().getStringExtra("attid");
		Intent intent = new Intent(this,HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		if (attid.equals("0")) {
			bamember_guanzhu.setBackgroundResource(R.drawable.com_plus);
		} else {
			bamember_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		url=Constant.url+ "club/getMembers?siteid="+siteid+"&pagesize=10&page="+page;
		info(1, url);
		name();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PreferencesUtils.putString(Bamember.this, "storeID", array.get(arg2).get("storeId"));
				Constant.SHOP_ID = array.get(arg2).get("storeId");
				Intent intent = new Intent();
				intent.setClass(Bamember.this, ShopDetailsActivity.class);
				Bamember.this.startActivity(intent);
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {
			boolean isLastRow = false;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (isLastRow
						&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					// 添加底部组件
					params.weight = 1;
					gridView.setLayoutParams(params);
					more.setVisibility(View.VISIBLE);
					isLastRow = false;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0) {

					isLastRow = true;
				} else {
					params.weight = 0;
					gridView.setLayoutParams(params);
					more.setVisibility(View.GONE);
					more.setText("点击加载更多");
				}
			}
		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int v = 1, c = 0;
				if (v == 1) {
					int a = totalRecord;
					int b = 10;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;
						if (c == 0) {
							c = 1;
						} else {
							c = totalRecord / b;
						}
					}

				}

				if (page == c) {
					handler.sendEmptyMessage(2);
				} else {
					more.setText("加载中...");
					page++;
					url=Constant.url+ "club/getMembers?siteid="+siteid+"&pagesize=10&page="+page;
					info(1, url);
				}
			}
		});
		bam_freshen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				info(1, url);
				dialog.show();
			}
		});
	}
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			textView.setText(intent.getExtras().getString("attid"));
			if(intent.getExtras().getString("attid").equals("0")){
				bamember_guanzhu
				.setBackgroundResource(R.drawable.com_plus);
				attid=intent.getExtras().getString("attid");
			}else{
				bamember_guanzhu
				.setBackgroundResource(R.drawable.com_yes);
				attid=intent.getExtras().getString("attid");
			}
		}
	};
	public void info(final int what, final String url) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				httpRequest = new HttpRequest();
				if(what==6){
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
				}else if(what==8){
					delect = httpRequest.doGet(url, Bamember.this);
				}else{
					urlstr = httpRequest.doGet(url, Bamember.this);
					if(urlstr.equals("网络超时")){
						handler.sendEmptyMessage(4);
					}else{
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
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				bam_shuaxin.setVisibility(View.GONE);
				mygridview.setVisibility(View.VISIBLE);
				try {
					setmap(urlstr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				break;
			case 2:	
				more.setText("数据加载完毕");
				Toast.makeText(Bamember.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:	
				dialog.dismiss();
				more.setText("暂时没有数据");
				Toast.makeText(Bamember.this, "暂时没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				dialog.dismiss();
				bam_shuaxin.setVisibility(View.VISIBLE);
				mygridview.setVisibility(View.GONE);
				break;
			case 5:
				String userId = PreferencesUtils.getString(Bamember.this,
						"userid");
				int areaId = PreferencesUtils.getInt(Bamember.this, "cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ siteid + "&areaId=" + areaId;
				String posturl = Constant.url+ "attention/addAttention";
				info(6,posturl);
				break;
			case 6:
				if (object == null) {
					Toast.makeText(Bamember.this, "响应失败",
							Toast.LENGTH_SHORT).show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							attid=jsonObject.getString("id");
							Toast.makeText(Bamember.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							bamember_guanzhu
									.setBackgroundResource(R.drawable.com_yes);
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", attid);
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(Bamember.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 7:
				String url = Constant.url+ "attention/deleteAttention?id="
						+ attid;
				info(8,url);
				break;
			case 8:
				try {
					JSONObject jsonObject=new JSONObject(delect);
					int stat=jsonObject.getInt("status");
					if(stat==0){
						attid="0";
						Toast.makeText(Bamember.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						bamember_guanzhu
						.setBackgroundResource(R.drawable.com_plus);
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", attid);
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);
						
					}else{
						
						Toast.makeText(Bamember.this, "取消失败",
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
	public void setmap(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(5);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
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
	public void name() {
		adapter = new ImageTextListAdapter(Bamember.this, listImageAndText,
				gridView);
		gridView.setAdapter(adapter);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bamember_guanzhu:
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
