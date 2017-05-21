package com.X.tcbj.test;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.csrx.http.AbHttpUtil;
import com.csrx.http.AbStringHttpResponseListener;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.CityModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestHttpGetJson extends Activity {
		private TextView resultView;
		private Button queryButton,getButton;
		private AbHttpUtil mAbHttpUtil = null;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.testser);
			final TestData test=new TestData(TestHttpGetJson.this);
			// 获取Http工具类
			mAbHttpUtil = AbHttpUtil.getInstance(this);
			resultView = (TextView) findViewById(R.id.textView1);
			queryButton = (Button) findViewById(R.id.button1);
			getButton= (Button) findViewById(R.id.button2);
			
			getButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<CityModel> list=test.getCityNames();
					resultView.setText("获取城市列表："+list);
				}
			});

			queryButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 一个菜谱的url地址
					String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10";
					mAbHttpUtil.get(urlString, new AbStringHttpResponseListener() {
						// 获取数据成功会调用这里
						@Override
						public void onSuccess(int statusCode, String content) {
							resultView.setText(content);
							
							try {
								test.syncCityToLocal((new JSONObject(content)).getJSONArray("list"));
//								System.out.println("没有异常");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// 失败，调用
						@Override
						public void onFailure(int statusCode, String content,
								Throwable error) {
							resultView.setText(error.getMessage());
						}
						// 开始执行前
						@Override
						public void onStart() {
							// 显示进度框
							// showProgressDialog();
						}
						// 完成后调用，失败，成功
						@Override
						public void onFinish() {
							// 移除进度框
							// removeProgressDialog();
						};

					});

				}
			});
		}
}
