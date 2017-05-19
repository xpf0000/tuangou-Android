package com.hkkj.csrx.activity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
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

public class Bussainfo extends Activity implements OnClickListener{
	TextView bainfotitle, bainforenzheng, bainfotime, bainfopeople,mybutxt;
	String url,urlstr,siteid, attid, poststr,delect;
	HashMap<String, String> hashMap;
	Button bainfo_guanzhu;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	Dialog dialog;
	Object object;
	ImageView business_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bussainfo);
		dialog = GetMyData.createLoadingDialog(Bussainfo.this,
				"正在拼命的加载······");
		dialog.show();
		bainfotitle=(TextView)findViewById(R.id.bainfotitle);
		bainforenzheng=(TextView)findViewById(R.id.bainforenzheng);
		bainfotime=(TextView)findViewById(R.id.bainfotime);
		bainfopeople=(TextView)findViewById(R.id.bainfopeople);
		bainfo_guanzhu=(Button)findViewById(R.id.bainfo_guanzhu);
		business_back=(ImageView)findViewById(R.id.business_back);
		business_back.setOnClickListener(this);
		bainfo_guanzhu.setOnClickListener(this);
		mybutxt=(TextView)findViewById(R.id.mybutxt);
		bainfotitle.setText(getIntent().getStringExtra("title"));
		bainforenzheng.setText(getIntent().getStringExtra("review"));
		bainfotime.setText(getIntent().getStringExtra("addTime"));
		bainfopeople.setText(getIntent().getStringExtra("counts"));
		attid = getIntent().getStringExtra("attid");
		siteid=getIntent().getStringExtra("siteid");
		Intent intent = new Intent(this,HKService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		registerReceiver(broadcastReceiver, filter);
		if (attid.equals("0")) {
			bainfo_guanzhu.setBackgroundResource(R.drawable.com_plus);
		} else {
			bainfo_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		url= Constant.url+"/club/aboutClub?siteid="+siteid;
		getmessage(url, 1);
	}
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			textView.setText(intent.getExtras().getString("data"));
			if(intent.getExtras().getString("attid").equals("0")){
				bainfo_guanzhu
				.setBackgroundResource(R.drawable.com_plus);
				attid=intent.getExtras().getString("attid");
			}else{
				bainfo_guanzhu
				.setBackgroundResource(R.drawable.com_yes);
				attid=intent.getExtras().getString("attid");
			}
		}
	};
	public void getmessage(final String url, final int what) {
		new Thread() {
			public void run() {
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				if(what==5){
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					object = myhttpRequest.request(url, poststr, "POST");
				}else if(what==7){
					delect = httpRequest.doGet(url, Bussainfo.this);
				}else{
					urlstr = httpRequest.doGet(url, Bussainfo.this);
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
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				try {
					name(urlstr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mybutxt.setText(array.get(0).get("content"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(Bussainfo.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(Bussainfo.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				String userId = PreferencesUtils.getString(Bussainfo.this,
						"userid");
				int areaId = PreferencesUtils.getInt(Bussainfo.this, "cityID");
				poststr = "userId=" + userId + "&typeId=1&AttentionId="
						+ siteid + "&areaId=" + areaId;
				String posturl = Constant.url+"attention/addAttention";
				getmessage(posturl, 5);
				break;
			case 5:
				if (object == null) {
					Toast.makeText(Bussainfo.this, "响应失败",
							Toast.LENGTH_SHORT).show();
				} else {
					String str = object.toString();
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							attid=jsonObject.getString("id");
							Toast.makeText(Bussainfo.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							bainfo_guanzhu
									.setBackgroundResource(R.drawable.com_yes);
							Intent intent = new Intent();
							intent.setAction("com.servicedemo4");
							intent.putExtra("attid", attid);
							intent.putExtra("refrech", "0");
							sendBroadcast(intent);
						} else {
							Toast.makeText(Bussainfo.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 6:
				String url = Constant.url+"attention/deleteAttention?id="
						+ attid;
				getmessage(url, 7);
				break;
			case 7:
				try {
					JSONObject jsonObject=new JSONObject(delect);
					int stat=jsonObject.getInt("status");
					if(stat==0){
						attid="0";
						Toast.makeText(Bussainfo.this, "已取消关注",
								Toast.LENGTH_SHORT).show();
						bainfo_guanzhu
						.setBackgroundResource(R.drawable.com_plus);
						Intent intent = new Intent();
						intent.setAction("com.servicedemo4");
						intent.putExtra("attid", attid);
						intent.putExtra("refrech", "0");
						sendBroadcast(intent);
						
					}else{
						
						Toast.makeText(Bussainfo.this, "取消失败",
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
	public void name(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			handler.sendEmptyMessage(2);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				hashMap.put("content",
						jsonObject2.getString("content"));
				array.add(hashMap);
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bainfo_guanzhu:
			if (attid.equals("0")) {
				handler.sendEmptyMessage(4);
			} else {
				handler.sendEmptyMessage(6);
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
