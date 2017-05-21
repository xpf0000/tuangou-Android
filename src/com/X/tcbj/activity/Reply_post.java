package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Reply_post extends Activity implements OnClickListener {
	TextView bbs_re_sub;
	ImageView bbs_re_back;
	EditText bbs_re_edt;
	String url, uid, tid;
	Object object;
	int siteid;
	String stat,head;
	Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_post);
		dialog = GetMyData.createLoadingDialog(Reply_post.this, "正在拼命的加载······");
		bbs_re_sub = (TextView) findViewById(R.id.bbs_re_sub);
		bbs_re_sub.setOnClickListener(this);
		bbs_re_back = (ImageView) findViewById(R.id.bbs_re_back);
		bbs_re_back.setOnClickListener(this);
		bbs_re_edt = (EditText) findViewById(R.id.bbs_re_edt);
		uid = PreferencesUtils.getString(Reply_post.this, "uid");
		siteid = PreferencesUtils.getInt(Reply_post.this, "cityID");
		tid = getIntent().getStringExtra("tid");
		stat= getIntent().getStringExtra("stat");
		if(stat.equals("2")){
			head=getIntent().getStringExtra("yinyong");
		}else{
			head="";
		}
		url = Constant.url+"forum/r?";
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (object == null) {
					dialog.dismiss();
					Toast.makeText(Reply_post.this, "响应失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					String str = object.toString();
					System.out.println(object);
					try {
						JSONObject jsonObject = new JSONObject(str);
						int statusMsg = jsonObject.getInt("status");
						if (statusMsg == 0) {
							Toast.makeText(Reply_post.this, "回复成功",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent();
							intent.setClass(Reply_post.this, Post_info.class);
							intent.putExtra("tid", tid);
							Reply_post.this.startActivity(intent);
							dialog.dismiss();
							finish();
						} else {
							dialog.dismiss();
							Toast.makeText(Reply_post.this,jsonObject.getString("statusMsg"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		};
	};

	public void submit(final String str, final int what) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				MyhttpRequest myhttpRequest = new MyhttpRequest();
				object = myhttpRequest.request(url, str, "POST");
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
			}
		}.start();

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bbs_re_back:
			finish();
			break;
		case R.id.bbs_re_sub:
			 try {
				head=URLEncoder.encode(head,"UTF-8");
				//head=head.replace("", "");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bbs_re_edt.getText().length() < 10) {
				Toast.makeText(Reply_post.this, "请输入正确的内容", Toast.LENGTH_SHORT)
						.show();
			} else {
				dialog.show();
				String str = "uid=" + uid + "&tid=" + tid + "&context="
						+ head+bbs_re_edt.getText() + "&siteid=" + siteid;
				submit(str, 1);
				
			}
			break;

		default:
			break;
		}

	}

}
