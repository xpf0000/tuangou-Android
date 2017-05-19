package com.hkkj.csrx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SemNewsInfo extends Activity{
	WebView semweb;
	String content;
	LinearLayout info_info_layout;
	ImageView info_info_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seminar_info);
		semweb = (WebView)findViewById(R.id.semweb);
		info_info_layout=(LinearLayout)findViewById(R.id.info_info_layout);
		info_info_layout.setVisibility(View.VISIBLE);
		info_info_back=(ImageView)findViewById(R.id.info_info_back);
		semweb.getSettings().setJavaScriptEnabled(true);
		semweb.setVerticalScrollBarEnabled(false); // 垂直不显示
		semweb.setWebChromeClient(new WebChromeClient());
		content=getIntent().getStringExtra("content");
		semweb.loadDataWithBaseURL(null, content, "text/html", "utf-8",
				null);
		info_info_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

}
