package com.hkkj.csrx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

public class Pri_info extends Activity {
	WebView myview;
	ImageView prii_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pri_info);
		String info=getIntent().getStringExtra("contes");
		myview=(WebView)findViewById(R.id.myview);
		prii_back=(ImageView)findViewById(R.id.prii_back);
		myview.setVerticalScrollBarEnabled(false); //垂直不显示
		myview.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
		myview.getSettings().setJavaScriptEnabled(true);  
		myview.setWebChromeClient(new WebChromeClient());
		prii_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	}

}
