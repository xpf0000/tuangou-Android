package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class Updatepowd extends Activity {
	EditText powd, newpowd, surepowd;
	Button sure;
	ImageView back;
	 Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upadtepassword);
		powd = (EditText) findViewById(R.id.updatepowd_powd);
		newpowd = (EditText) findViewById(R.id.updatepowd_newpowd);
		surepowd = (EditText) findViewById(R.id.updatepowd_sureword);
		sure = (Button) findViewById(R.id.sureupdate);
		back = (ImageView) findViewById(R.id.update_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!newpowd.getText().toString().trim()
						.equals(surepowd.getText().toString().trim())) {
					Toast.makeText(Updatepowd.this, "两次密码不统一",
							Toast.LENGTH_SHORT).show();
				} else if (newpowd.getText().toString().trim().equals("")
						|| newpowd.getText().toString().trim() == "null") {
					Toast.makeText(Updatepowd.this, "请输入正确的内容",
							Toast.LENGTH_SHORT).show();

				} else {
					dialog = GetMyData.createLoadingDialog(Updatepowd.this,
							"请稍等");
					dialog.show();
					String mypowd = powd.getText().toString();
					String mynewpowd = newpowd.getText().toString();
					String userid = PreferencesUtils.getString(Updatepowd.this,
							"userid");
					info(userid, mypowd, mynewpowd);
				}

			}
		});
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				Toast.makeText(Updatepowd.this, "成功", Toast.LENGTH_SHORT)
				.show();
				finish();
				break;
			case 1:
				dialog.dismiss();
				Toast.makeText(Updatepowd.this, "失败", Toast.LENGTH_SHORT)
				.show();
			break;
			case 3:
				dialog.dismiss();
				Toast.makeText(Updatepowd.this, "服务器处理错误",
						Toast.LENGTH_SHORT).show();
			break;
			case 11:
				dialog.dismiss();
				Toast.makeText(Updatepowd.this, "网络访问超时",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	public void info(final String userid, final String password, final String newpassword){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare(); 
				int statusMsg=0;
				String address = Constant.url+"login/userUpdatePassword?";
				MyhttpRequest myhttpRequest = new MyhttpRequest();
				String cimstr="userId="+userid+"&oldPassWord="+password+"&newPassWord="+newpassword;
				Object object = myhttpRequest.request(address, cimstr, "POST");
				try {
					if (object == null) {
						dialog.dismiss();
						statusMsg=11;
					} else {
						String str = object.toString();
						JSONObject jsonObject = new JSONObject(str);
						statusMsg = jsonObject.getInt("status");
						if (statusMsg==0) {
							statusMsg=0;
						}else{
							statusMsg=1;
						}
					}
					Message message=new Message();
					message.what=statusMsg;
					handler.sendEmptyMessage(statusMsg);
					 Looper.loop(); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
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
