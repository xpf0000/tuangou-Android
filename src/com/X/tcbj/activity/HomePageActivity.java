package com.X.tcbj.activity;

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

import com.X.server.DataCache;
import com.X.tcbj.fragment.NearbyFragment;
import com.X.tcbj.fragment.NewsFragment;
import com.X.tcbj.utils.XPostion;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.csrx.data.PreferencesUtils;
import com.csrx.http.AbHttpUtil;
import com.csrx.http.AbStringHttpResponseListener;
import com.csrx.util.AbAppUtil;
import com.X.tcbj.fragment.AttentionFragment;
import com.X.tcbj.fragment.HomeFragment;
import com.X.tcbj.fragment.MineFragment;
import com.X.tcbj.fragment.MoreFragment;
import com.X.tcbj.utils.CommonField;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.commonCallBack;
import com.X.tcbj.utils.isexist;
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
	private RadioButton home, near, news, order, mine;
	private FragmentTransaction transaction;
	public HomeFragment homeFragment;
	public NearbyFragment nearFragment;
	public MineFragment mineFragment;
	public MoreFragment orderFragment;
	public NewsFragment newsFragment;
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
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
			transaction.add(R.id.ll_content, homeFragment);
			transaction.commit();
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
		near = (RadioButton) findViewById(R.id.rb_near);
		mine = (RadioButton) findViewById(R.id.rb_mine);
		news = (RadioButton) findViewById(R.id.rb_news);
		order = (RadioButton) findViewById(R.id.rb_order);

		home.setOnCheckedChangeListener(this);
		near.setOnCheckedChangeListener(this);
		mine.setOnCheckedChangeListener(this);
		news.setOnCheckedChangeListener(this);
		order.setOnCheckedChangeListener(this);
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
				if (nearFragment != null) {
					transaction.hide(nearFragment);
				}

				if (newsFragment != null) {
					transaction.hide(newsFragment);
				}

				if (orderFragment != null) {
					transaction.hide(orderFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}

				transaction.show(homeFragment);
				break;
			case R.id.rb_near:
				if (nearFragment == null) {
					nearFragment = new NearbyFragment();
					transaction.add(R.id.ll_content, nearFragment);
				}
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (newsFragment != null) {
					transaction.hide(newsFragment);
				}

				if (orderFragment != null) {
					transaction.hide(orderFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}

				transaction.show(nearFragment);
				break;

			case R.id.rb_news:

				if (newsFragment == null) {
					newsFragment = new NewsFragment();
					transaction.add(R.id.ll_content, newsFragment);
				}

				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (nearFragment != null) {
					transaction.hide(nearFragment);
				}

				if (orderFragment != null) {
					transaction.hide(orderFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}

				transaction.show(newsFragment);
				break;
			case R.id.rb_order:
				if (orderFragment == null) {
					orderFragment = new MoreFragment();
					transaction.add(R.id.ll_content, orderFragment);
				}
				if (nearFragment != null) {
					transaction.hide(nearFragment);
				}

				if (newsFragment != null) {
					transaction.hide(newsFragment);
				}

				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				transaction.show(orderFragment);
				break;

				case R.id.rb_mine:
					DataCache.getInstance().getUinfo();
					if (mineFragment == null) {
						mineFragment = new MineFragment();
						transaction.add(R.id.ll_content, mineFragment);
					}

					if (homeFragment != null) {
						transaction.hide(homeFragment);
					}

					if (nearFragment != null) {
						transaction.hide(nearFragment);
					}

					if (newsFragment != null) {
						transaction.hide(newsFragment);
					}

					if (orderFragment != null) {
						transaction.hide(orderFragment);
					}

					transaction.show(mineFragment);
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
        XPostion.getInstance().stop();
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
