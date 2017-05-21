package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 应用首页
 * @author wgs
 * @version 1.0
 */
public class MainActivity extends Activity {

	TextView titleView;
	TextView position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		titleView=(TextView) findViewById(R.id.title);
		position=(TextView) findViewById(R.id.position);
		Intent intent = getIntent();            // getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
		Bundle bundle = intent.getExtras();     // .getExtras()得到intent所附带的额外数据
		String str = bundle.getString("city");   // getString()返回指定key的值
		titleView.setText(str+getString(R.string.title).toString());
	}

}
