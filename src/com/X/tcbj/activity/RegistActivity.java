package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.X.model.HomeModel;
import com.X.model.UserModel;
import com.X.server.DataCache;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.robin.lazy.cache.CacheLoaderManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.X.server.location.APPService;

/**
 * 登录信息
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends Activity {
	EditText telET,codeET,passET,pass1ET,tjET;
	CheckBox checkBox;
	Button  getcode;
	TimerTask task;
	private int recLen = 60;
	Timer timer = new Timer();

	String code = "123456789";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		if(getIntent().getStringExtra("tj_code") != null){code = getIntent().getStringExtra("tj_code");}

		telET = (EditText) findViewById(R.id.regist_tel);

		codeET = (EditText) findViewById(R.id.regist_code);
		passET = (EditText) findViewById(R.id.regist_pass);
		pass1ET = (EditText) findViewById(R.id.regist_pass1);
		tjET = (EditText) findViewById(R.id.regist_tj);


		checkBox = (CheckBox) findViewById(	R.id.regist_step1_check);
		getcode = (Button) findViewById(R.id.regist_code_btn);

		tjET.setText(code);

	}


	public void back(View v)
	{
		finish();
	}

	public void do_send_code(View v)
	{
		final String tel = telET.getText().toString().trim();

		if(tel.equals(""))
		{
			XActivityindicator.showToast("请输入手机号");
			return;
		}

		if(!XAPPUtil.isPhone(tel))
		{
			XActivityindicator.showToast("手机号码格式有误");
			return;
		}

		XNetUtil.Handle(APPService.sms_send_code(tel), "验证码发送成功", null, new XNetUtil.OnHttpResult<Boolean>() {
			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onSuccess(Boolean aBoolean) {

				if(aBoolean)
				{
					XAPPUtil.saveAPPCache("SMSTIME",((new Date()).getTime()/1000)+"");
                    getcode.setEnabled(false);
					gettime();
				}
			}
		});
	}

	public void do_scan(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, QCScanVC.class);
		startActivity(intent);
		finish();

	}

	public void do_regist(View v)
	{
		final String tel = telET.getText().toString().trim();

		String code = codeET.getText().toString().trim();
		String pass = passET.getText().toString().trim();
		String pass1 = pass1ET.getText().toString().trim();
		String tj = tjET.getText().toString().trim();

		if(tel.equals("") || code.equals("") || pass.equals("") || pass1.equals("") || tj.equals(""))
		{
			XActivityindicator.showToast("请完善信息");
			return;
		}

		if(!XAPPUtil.isPhone(tel))
		{
			XActivityindicator.showToast("手机号码格式有误");
			return;
		}

		if(pass.length() < 6 || pass.length() > 18)
		{
			XActivityindicator.showToast("密码长度为6-18位");
			return;
		}

		if(!pass.equals(pass1))
		{
			XActivityindicator.showToast("密码和确认密码不一致");
			return;
		}

		XNetUtil.HandleBool(APPService.user_doregist(tel, pass, code, tj), null, null, new XNetUtil.OnHttpResult<XNetUtil.Result<UserModel>>() {
			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onSuccess(XNetUtil.Result<UserModel> res) {

				if(res.isSuccess())
				{
                    DataCache.getInstance().user = res.getHttpResult().getData();
                    XAPPUtil.saveAPPCache("User",res.getHttpResult().getData());

                    Intent intent = new Intent();
                    intent.setClass(RegistActivity.this,UserRenzhengVC.class);
                    startActivity(intent);
                    finish();
				}

			}
		});



	}

	public void to_protocol(View v)
	{

	}


	public void gettime() {

        if(task != null)
        {
            return;
        }

		task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						recLen--;
						getcode.setText(recLen + "秒后再获取");
						if (recLen < 0) {
							getcode.setText("获取验证码");
							recLen = 60;
							getcode.setClickable(true);
                            task.cancel();
                            task = null;
						}
					}
				});
			}
		};
		timer.schedule(task, 1000, 1000);
	}

	public void onResume() {
		super.onResume();
		String t = CacheLoaderManager.getInstance().loadString("SMSTIME");
		if(t != null)
		{
			long a = (new Date()).getTime()/1000-Long.parseLong(t);
			a = 60 - a;

			if(a > 0)
			{
				recLen = (int) a;
				getcode.setEnabled(false);
				gettime();
			}

		}


	}

	public void onPause() {
		super.onPause();
	}
}
