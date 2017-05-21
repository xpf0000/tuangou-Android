package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 蒙版页2
 * 
 * @author zmz
 * 
 */
public class GuideTwoActivity extends Activity {
	RelativeLayout layout;
	TextView txt_next;
	int width;// 控件横坐标
	int height;// 控件竖坐标

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_two);
		layout = (RelativeLayout) findViewById(R.id.rl_two);
		// 动态添加textView
		Display display = this.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();// 屏幕的宽
		int height = display.getHeight();// 屏幕的高

//		System.out.println("屏幕的宽" + width);
//		System.out.println("屏幕的高" + height);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width / 3, height / 10);
		// 设置TextView在屏幕中的显示位置
		params.alignWithParent = true;
		params.leftMargin = (int) (width * (6.0/10.0));
		params.topMargin = (int) (height * (10.0/20.0));

		txt_next = new TextView(this);
//		txt_next.setBackgroundColor(Color.RED);
		txt_next.setLayoutParams(params);
		txt_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GuideTwoActivity.this,
						GuideThreeActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				GuideTwoActivity.this.finish();
			}
		});
		layout.addView(txt_next);
	}
}
