package com.X.tcbj.activity;

import java.util.ArrayList;
import java.util.List;

import com.csrx.data.PreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 引导页--简介
 * 
 * @author wgs
 * @version 1.0 手势向左滑动进入下一页，第二次进入程序，引导页不再显示
 */
public class GuideActivity extends Activity implements OnTouchListener,
		OnPageChangeListener {
	List bits = new ArrayList();
	String[] qidongs;
	ViewGroup group;
	int wid = 0;
	int he = 0;
	ViewPager viewpager;
	private int lastX = 0;
	int a = 0;
	ArrayList<View> views;
	int currentIndex;
	ArrayList<View> listpage = new ArrayList<View>();

	private SharedPreferences spn;
	private ImageView imageView;

	private int count = 0;
	Intent intent;
	Boolean isFirstIn = false, qidong;
	public static final String QIDONG = "qidong";
	public static final String PREFS_NAME = "prefs";
	private int[] resIds = new int[] { R.drawable.app01,
			R.drawable.app02, R.drawable.app03 };
	ImageView[] imageViews = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		group = (ViewGroup) this.findViewById(R.id.group);
		spn = getSharedPreferences(PREFS_NAME, 0);

		qidong = PreferencesUtils.getBoolean(GuideActivity.this, "qidong");

		int piclen = PreferencesUtils.getInt(GuideActivity.this, "piclen");
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		views = new ArrayList<View>();

		if (!qidong) {
			for (int i = 0; i < resIds.length; i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(layoutParams);
				iv.setBackgroundResource(resIds[i]);

				listpage.add(iv);
			}
		} else {
			imageViews = new ImageView[piclen];
			for (int i = 0; i < piclen; i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(layoutParams);
				Bitmap bitmap = null;
				String name = PreferencesUtils.getString(GuideActivity.this,
						"pic" + i);
				bits.add("/data/data/com.hkkj.csrx.activity/files/" + name);
				try {
					bitmap.recycle();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bitmap = BitmapFactory.decodeFile(bits.get(i).toString());

				Drawable drawable = new BitmapDrawable(bitmap);
				iv.setBackgroundDrawable(drawable);

				listpage.add(iv);
				ImageView imageView = new ImageView(this);

				imageViews[i] = imageView;
				if (i == 0) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
				group.addView(imageViews[i]);
			}
		}

		MyPageAdapter adapter = new MyPageAdapter();
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);

		viewpager.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			lastX = (int) event.getX();
			if (viewpager.getCurrentItem() == 2) {
				if (a == 0) {
					if (!qidong) {
						Intent intent = new Intent(this,
								CityListsActivity.class);
						startActivity(intent);

					} else {
						PreferencesUtils.putBoolean(this, "qidong", false);
						Intent intent = new Intent(this, HomePageActivity.class);
						startActivity(intent);
					}
					a++;
				}
				this.finish();
			}

			break;
		case MotionEvent.ACTION_MOVE:

			if ((lastX - event.getX()) > 100
					&& (currentIndex == listpage.size() - 1)) {
				this.finish();
				if (a == 0) {
					if (!qidong) {
						Intent intent = new Intent(this,
								CityListsActivity.class);
						startActivity(intent);

					} else {
						PreferencesUtils.putBoolean(this, "qidong", false);
						Intent intent = new Intent(this, HomePageActivity.class);
						startActivity(intent);
					}
					a++;
				}
				this.finish();
			}
			break;

		}
		return false;
	}

	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return listpage.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// currentIndex = position;
			container.removeView(listpage.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			container.addView(listpage.get(position));

			return listpage.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		currentIndex = arg0;
	}

	@Override
	public void onPageSelected(int arg0) {
		if (qidong) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}

		}
	}
}
