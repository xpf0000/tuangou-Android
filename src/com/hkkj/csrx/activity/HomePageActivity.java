package com.hkkj.csrx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.csrx.http.AbHttpUtil;
import com.csrx.http.AbStringHttpResponseListener;
import com.csrx.util.AbAppUtil;
import com.hkkj.csrx.fragment.AttentionFragment;
import com.hkkj.csrx.fragment.HomeFragment;
import com.hkkj.csrx.fragment.MineFragment;
import com.hkkj.csrx.fragment.MoreFragment;
import com.hkkj.csrx.utils.CommonField;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.commonCallBack;
import com.hkkj.csrx.utils.isexist;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 引导页--简介
 * 
 * @author zmz
 * @version 1.0
 */
public class HomePageActivity extends FragmentActivity implements
		OnCheckedChangeListener {
	int state = 0;
	String statu;
	String starurl;
	private boolean qidong, startexit = true;
	public static final String QIDONG = "qidong";
	String[] qidongs = null;
	String qidongdata;
	int wid = 0;
	int he = 0;
	// 960
	String qidongurl = Constant.url+"pic/getpic?width="
			+ wid + "&height=" + he + "&stats=1";

	private Intent intent;
	
	String activityStyle;
	private RadioButton home, merchant, mine, more;
	private FragmentTransaction transaction;
	public HomeFragment homeFragment;
	public AttentionFragment merchantFragment;
	public MineFragment mineFragment;
	public MoreFragment moreFragment;
	private boolean isNet;

	ImageView imageview;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0x01) {
				// 更改启动图显示状态
				PreferencesUtils.putBoolean(HomePageActivity.this, "staqidong",
						true);
			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		wid = dm.widthPixels;
		he = dm.heightPixels;
		isNet = AbAppUtil.isNetworkAvailable(this);// 网络连接状态
		// Constant.NET_STATIC=isNet;
		initComponents();
		intent = getIntent();
		activityStyle = intent.getStringExtra("activity");
		//getHotSearch();
		if (activityStyle == null) {

			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				transaction.add(R.id.ll_content, homeFragment);
				transaction.commit();
			}

		} else if (activityStyle.equals("MerchantFragment")) {
			if (merchantFragment == null) {
				merchantFragment = new AttentionFragment();
				transaction.add(R.id.ll_content, merchantFragment);
				transaction.commit();
				merchant.setChecked(true);
			}
		}

		final HttpRequest httpRequest = new HttpRequest();
		try {


		new Thread() {
			public void run() {
				Looper.prepare();
				qidongdata = httpRequest
						.doGet(qidongurl, HomePageActivity.this);

				if (!qidongdata.equals("网络超时") && qidongdata != null) {
					statu = getstate(qidongdata);
					if (statu.equals("1")) {

						starurl = geturl(qidongdata);
						if (starurl != null) {
							startexit = isexist.exist(starurl);
							if (!startexit && !qidongdata.equals("网络超时")){
									//&& qidongdata != null)
								PreferencesUtils.putString(
										HomePageActivity.this, "stapic",
										starurl.substring(starurl
												.lastIndexOf("/") + 1));
								isexist.saveImage(HomePageActivity.this,
										starurl, new commonCallBack() {

											@Override
											public void doSameThing() {

												handler.sendEmptyMessage(0x01);
											}
										});

							}
						}

					}

				}
				Looper.loop();
			};

		}.start();
		}catch (Exception e){

		}
	}

	public void initComponents() {
		home = (RadioButton) findViewById(R.id.rb_home);
		merchant = (RadioButton) findViewById(R.id.rb_merchant);
		mine = (RadioButton) findViewById(R.id.rb_mine);
		more = (RadioButton) findViewById(R.id.rb_more);
		home.setOnCheckedChangeListener(this);
		merchant.setOnCheckedChangeListener(this);
		mine.setOnCheckedChangeListener(this);
		more.setOnCheckedChangeListener(this);
		transaction = getSupportFragmentManager().beginTransaction();

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			transaction = getSupportFragmentManager().beginTransaction();
			switch (buttonView.getId()) {
			case R.id.rb_home:

				if (homeFragment == null) {
					homeFragment = new HomeFragment();
					transaction.add(R.id.ll_content, homeFragment);
				}
				if (merchantFragment != null) {
					transaction.hide(merchantFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				if (moreFragment != null) {
					transaction.hide(moreFragment);
				}
				transaction.show(homeFragment);
				break;
			case R.id.rb_merchant:
				if (merchantFragment == null) {
					merchantFragment = new AttentionFragment();
					transaction.add(R.id.ll_content, merchantFragment);
				}
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				if (moreFragment != null) {
					transaction.hide(moreFragment);
				}
				transaction.show(merchantFragment);
				break;

			case R.id.rb_mine:
				if (mineFragment == null) {
					mineFragment = new MineFragment();
					transaction.add(R.id.ll_content, mineFragment);
				}
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (merchantFragment != null) {
					transaction.hide(merchantFragment);
				}
				if (moreFragment != null) {
					transaction.hide(moreFragment);
				}
				transaction.show(mineFragment);
				break;
			case R.id.rb_more:
				if (moreFragment == null) {
					moreFragment = new MoreFragment();
					transaction.add(R.id.ll_content, moreFragment);
				}
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				if (merchantFragment != null) {
					transaction.hide(merchantFragment);
				}
				transaction.show(moreFragment);
				break;
			}
			transaction.commit();
		}
	}

	/**
	 * 
	 */
	public void getHotSearch() {
		// 获取Http工具类
		AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(HomePageActivity.this);
		// 获取当前城市信息
		int cityID = PreferencesUtils.getInt(HomePageActivity.this, "cityID");
		// 城市列表的url地址
		String urlString = Constant.url+"getAppBadWord?pageRecord=10&currentPage=1&areaID="
				+ cityID;
		mAbHttpUtil.get(urlString, new AbStringHttpResponseListener() {
			// 获取数据成功会调用这里v
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject object = new JSONObject(content);
					String status = object.getString("status");
					if (status.equals("1")) {
						JSONArray json = object.getJSONArray("list");
						for (int i = 0; i < json.length(); i++) {
							JSONObject hot = json.getJSONObject(i);
							CommonField.keywords.add(hot.getString("badWord"));
							System.out.println(hot.getString("badWord"));
						}
					} else {
						CommonField.keywords.clear();
						CommonField.keywords.add("");
						System.out.println("获取网络数据失败或者没有热词");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			// 失败，调用
			public void onFailure(int statusCode, String content,
					Throwable error) {
				System.out.println(error.getMessage());
			}
		});
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			// 提示放屏幕中间
			// Toast toast;
			// toast = Toast.makeText(getApplicationContext(), "再按一次退出程序",
			// Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.show();

			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			PreferencesUtils.putString(HomePageActivity.this, "selectadress", null);
			PreferencesUtils.putInt(HomePageActivity.this, "add", 1);
			PreferencesUtils.putString(HomePageActivity.this, "privilekey",
					"全部区域");
			PreferencesUtils.putString(HomePageActivity.this, "privilekeys",
					"默认排序");
			finish();
			System.exit(0);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public String getstate(String data) {
		String state = null;
		try {
			com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject
					.parseObject(data);
			state=jo.getString("status");
//			JSONObject jo = new JSONObject(data);
//			state = jo.getString("status");
		} catch (Exception e) {
			state="0";
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return state;
	}

	public String geturl(String data) {
		String starturl = null;
		try {
			com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject
					.parseObject(data);

			com.alibaba.fastjson.JSONObject ja = jo.getJSONObject("startpic");
				if(ja!=null)
				{
					starturl = ja.getString("url");
				}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return starturl;
	}

}
