package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class webiew extends Activity {
	WebView myview;
	ImageView prii_back;
	String url;
	TextView mytest;
	ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pri_info);
		myview = (WebView) findViewById(R.id.myview);
		mytest = (TextView) findViewById(R.id.mytest);
		bar=(ProgressBar)findViewById(R.id.seekbar);
//		mytest.setText("便民");
		prii_back = (ImageView) findViewById(R.id.prii_back);
		url = getIntent().getStringExtra("url");
		WebSettings webSettings = myview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myview.loadUrl(url);
		myview.setWebViewClient(new WebViewClient());
		myview.setWebChromeClient(new WebChromeClient() {
			//
			public void onProgressChanged(WebView view, int progress) {

				bar.setVisibility(View.VISIBLE);
				bar.setProgress(progress);
				if (progress == 100) {
					int a=myview.getContentHeight();
					bar.setVisibility(View.GONE);
					
				}
			}
		});
		prii_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
	}

}
