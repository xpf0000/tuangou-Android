package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.Emailtest;
import com.hkkj.csrx.utils.GetMyData;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录信息
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends Activity {
	EditText name, powd, surepowd, emaill;
	Button sure;
	CheckBox box;
	ImageView back;
	String areaid;
	Dialog dialog;
	TextView xieyi;
	String x;
	String userid,uid;
	WebView view;
	String msgs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		name = (EditText) findViewById(R.id.register_name);
		powd = (EditText) findViewById(R.id.register_powd);
		surepowd = (EditText) findViewById(R.id.register_word);
		emaill = (EditText) findViewById(R.id.register_email);
		box = (CheckBox) findViewById(R.id.register_check);
		xieyi = (TextView) findViewById(R.id.register_xieyi);
		sure = (Button) findViewById(R.id.register);
		view=(WebView)findViewById(R.id.mywebview);
		back = (ImageView) findViewById(R.id.regist_back);
		view.getSettings().setJavaScriptEnabled(true);
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (x != null) {
			x = tm.getLine1Number();
			name.setText(x.subSequence(3, 14));
		}

		SpannableString sp = new SpannableString("《城市热线注册协议》");
		sp.setSpan(new URLSpan(
				"http://luoyang.rexian.cn/user/UserLogin/UserAgreement"), 0,
				10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		xieyi.setText(sp);
		// 设置TextView可点击
		xieyi.setMovementMethod(LinkMovementMethod.getInstance());
		areaid = Integer.toString(PreferencesUtils.getInt(RegistActivity.this,
				"cityID"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean a=Emailtest.checkEmail(emaill.getText().toString().trim());
				System.out.println(a);
				dialog = GetMyData.createLoadingDialog(RegistActivity.this,
						"请稍等");
				dialog.show();
				 if (name.getText().toString().length() < 6) {
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "用户名过短，请输入6位以上16位以下字符",
							Toast.LENGTH_SHORT).show();

				}
				else if (powd.getText().toString().length() < 6) {
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "密码过短，请输入6位以上16位以下字符",
							Toast.LENGTH_SHORT).show();

				} 
				else if (!powd.getText().toString().trim()
						.equals(surepowd.getText().toString())) {
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "两次密码不统一",
							Toast.LENGTH_SHORT).show();

				}else if (checkString(powd.getText().toString())) {
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "密码不能包含空格",
							Toast.LENGTH_SHORT).show();

				} else if(Emailtest.checkEmail(emaill.getText().toString().trim())==false){
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "邮箱不合法", Toast.LENGTH_SHORT).show();
				}
				else if (!box.isChecked()) {
					dialog.dismiss();
					Toast.makeText(RegistActivity.this, "请勾选城市热线用户协议",
							Toast.LENGTH_SHORT).show();

				}
				else {
					String username = name.getText().toString().trim();
					String password = powd.getText().toString().trim();
					String email = emaill.getText().toString().trim();
					info(username, password, email, areaid);
				}

			}
		});
	}

	public boolean checkString(String s) {
		return s.matches("^(/s)^");
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "用戶已存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "成功", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			case -1:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "用户名中有危险字符",
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "邮件格式不合法",
						Toast.LENGTH_SHORT).show();
				break;
			case -3:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this,msgs,Toast.LENGTH_SHORT).show();
				break;
			case -4:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "包含不允许注册的词语！",
						Toast.LENGTH_SHORT).show();
				break;
			case -5:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "用户名已经存在！",
						Toast.LENGTH_SHORT).show();
				break;
			case -6:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "Email 格式有误！",
						Toast.LENGTH_SHORT).show();
				break;
			case -7:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "Email 不允许注册！",
						Toast.LENGTH_SHORT).show();
				break;
			case -8:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "该 Email 已经被注册！",
						Toast.LENGTH_SHORT).show();
				break;
			case -9:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "同步论坛失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case 500:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "服务器处理错误",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "服务器处理错误",
						Toast.LENGTH_SHORT).show();
				break;
			case 11:
				dialog.dismiss();
				Toast.makeText(RegistActivity.this, "网络访问超时",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	public void info(final String userName, final String password,
			final String email, final String areaid) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				int statusMsg = 0;
				String path = Constant.url+ "login/register?";
				loginHttpClientByPost(path, userName, password, email, areaid);
				try {
					if (userid.equals("000")) {
						handler.sendEmptyMessage(-3);
						dialog.dismiss();
					} else {
						statusMsg = 0;
						PreferencesUtils.putString(RegistActivity.this,
								"userid", userid);
						PreferencesUtils.putString(RegistActivity.this,
								"userName", userName);
						PreferencesUtils.putInt(RegistActivity.this, "logn", 1);
						Message message = new Message();
						message.what = statusMsg;
						handler.sendEmptyMessage(statusMsg);
						Looper.loop();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	public boolean loginHttpClientByPost(String path, String userName,
			String password, String email, String areaId) {
		// 1 得到浏览器
		String state = "";


		return false;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}
}
