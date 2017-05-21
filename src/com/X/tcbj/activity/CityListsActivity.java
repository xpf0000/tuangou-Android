package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.csrx.util.AbAppUtil;
import com.csrx.util.CharacterParser;
import com.X.tcbj.adapter.SortAdapter;
import com.X.tcbj.domain.RecommendCity;
import com.X.tcbj.fragment.HomeFragment.HomeHandler;
import com.X.tcbj.myview.ClearEditText;
import com.X.tcbj.myview.SideBar;
import com.X.tcbj.myview.SideBar.OnTouchingLetterChangedListener;
import com.X.tcbj.utils.CityListToHomePage;
import com.X.tcbj.utils.CommonField;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.Tools;
import com.X.model.CityModel;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.server.PinyinComparator;
import com.X.server.location;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.X.server.location.APPService;

/**
 * 城市列表
 * 
 * @author zmz
 * @change 20141115
 * @version 1.4
 */
public class CityListsActivity extends Activity implements OnClickListener {

	private CityListToHomePage onCityToHome = null;

	public CityListToHomePage getChanged() {
		return onCityToHome;
	}

	public void setChanged(CityListToHomePage changed) {
		onCityToHome = changed;
	}

	private ListView sortListView;
	private ImageView img_back;
	private ImageView img_refresh;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private LocationClient mLocClient;
	private TextView autoCity;

	private List<RecommendCity.ObjectEntity> objectEntityList=new ArrayList<RecommendCity.ObjectEntity>();

	Map<String, String> map=new HashMap<String, String>();
	private String [] myData={"heh","heh","yionhdf"};
	// 城市列表的url地址
	String urlString = Constant.url+"admin/getAllCity?pageRecord=10000&currentPage=1";
//	String urlString = "http://192.168.2.113/rxcity.json";
	CityModel cityModel;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	private boolean netStatic;// 网络连接状态
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private Context cityCxt;
	Dialog dialogd;
	public static String cityPosition;
	String data;

	private SharedPreferences spn;
	boolean first;
	public static final String PREFS_NAME = "prefs";
	public static final String FIRST_START = "first";
    int vrtson,thisvrtson;
    private HomeHandler myhandler = null;
    location location=null;
	boolean ordian=false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_list);

		EventBus.getDefault().register(this);

		cityCxt = CityListsActivity.this;
		location = (location) getApplication();
		myhandler = location.getHandler1();
		try {
			vrtson=getLocalVersionCode(CityListsActivity.this);
			thisvrtson=PreferencesUtils.getInt(CityListsActivity.this, "thisvrtson");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img_back = (ImageView) findViewById(R.id.cityimage_back);
		// 第一次进入时隐藏返回键
		if (PreferencesUtils.getBoolean(CityListsActivity.this, "firststart")) {
			img_back.setVisibility(View.GONE);
		}

		netStatic = AbAppUtil.isNetworkAvailable(cityCxt);// 判断联网状态
		/**
		 * 判断网络连接状况
		 */
		if (netStatic) {
			dialogd = GetMyData.createLoadingDialog(cityCxt, "正在拼命的加载······");
			dialogd.show();
			// 读取数据
			myInfo(1);
		} else {
			Toast.makeText(cityCxt, "无网络连接,请先检查网络状况", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				ConnectivityManager connectMgr = (ConnectivityManager)cityCxt
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectMgr.getActiveNetworkInfo();
//				Toast.makeText(CityListsActivity.this,"info:"+info.getType()+"  info.name"+info.getTypeName()+"  sourceDateList.size():"+CommonField.sourceDateList.size()+"  urlString:"+urlString+"  data:"+data,Toast.LENGTH_LONG).show();
				dialogd.dismiss();
				initViews();
				break;
			}
		};
	};

	/**
	 * 从网络获取城市开通站点的城市
	 * 
	 * @param what
	 */
	public void myInfo(final int what) {


		XNetUtil.Handle(APPService.city_app_index(), new XNetUtil.OnHttpResult<CityModel>() {
			@Override
			public void onError(Throwable e) {

				XNetUtil.APPPrintln(e.getMessage());

			}

			@Override
			public void onSuccess(CityModel cityModel) {

				if(cityModel != null)
				{
					XNetUtil.APPPrintln("^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
					CityListsActivity.this.cityModel = cityModel;
					handler.sendEmptyMessage(1);
				}
				else
				{
					XNetUtil.APPPrintln("&&&&&&&&&&&&&&&&&&&&&&&&&&");
				}
			}
		});

	}

	/**
	 * 为控件赋值和设置监听事件
	 */
	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);

		img_refresh = (ImageView) findViewById(R.id.cityimage_refresh);
		img_back.setOnClickListener(this);
		img_refresh.setOnClickListener(this);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		// 加载list的头部信息
		LayoutInflater inflater = (LayoutInflater) cityCxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View headerView = inflater.inflate(R.layout.city_header, null);
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					ordian=true;
					if(adapter.getItemViewType(position-1) == 1)
					{
						onClickCity(adapter.getCityName(position-1));
					}
				}
			}
		});
		autoCity = (TextView) headerView.findViewById(R.id.position);//为listview添加header
		mLocClient = ((location) getApplication()).mLocationClient;
		if (autoCity.getText().toString().trim().equals("")
				|| autoCity.getText().toString().trim() == null) {
			autoCity.setText("定位中···");
//			GetMyData.setLocationOption(mLocClient);
			mLocClient.start();
			mLocClient.requestLocation();
			((location) getApplication()).mTv = autoCity;
			Constant.CITY_POSITION=autoCity.getText().toString();
		} else {
			mLocClient.stop();
		}
		cityPosition = location.mData;// 获取定位到的城市
//		String loca_city=location.mData;
		autoCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				onClickCity(location.mData);
				onClickCity(autoCity.getText().toString());
			}
		});

		// 根据a-z进行排序源数据
		Collections.sort(CommonField.sourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, cityModel.getCity_list());
		sortListView.addHeaderView(headerView);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private CityModel.CityListBean.ItemsBean isOpen(String name)
	{
		for (CityModel.CityListBean items : cityModel.getCity_list()) {
			for(CityModel.CityListBean.ItemsBean item : items.getItems())
			{
				String n = item.getName();
				if (n.indexOf(name) != -1 || name.indexOf(n) != -1) {
					return item;
				}

			}
		}

		return null;
	}

	/**
	 * 城市选择点击事件
	 * 
	 * @param cityName
	 */
	private void onClickCity(String cityName) {

		CityModel.CityListBean.ItemsBean city = isOpen(cityName);

		PreferencesUtils.putBoolean(CityListsActivity.this, "firststart", true);
		if (city == null) {
			Toast.makeText(cityCxt, "该城市暂未开通城市热线站点，请选择其他城市", Toast.LENGTH_SHORT)
					.show();
		} else {

			DataCache.getInstance().nowCity = city;
			XAPPUtil.saveAPPCache("NowCity",city);
			doChangeCity(city);


		}
	}


	public void doChangeCity(final CityModel.CityListBean.ItemsBean city)
	{
		XNetUtil.Handle(APPService.city_city_change(city.getId()), new XNetUtil.OnHttpResult<Object>() {
			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onSuccess(Object obj) {

				try {
					myhandler.sendEmptyMessage(6);
				} catch (Exception e) {
					// TODO: handle exception
				}
				spn = getSharedPreferences(PREFS_NAME, 0);
				first = spn.getBoolean(FIRST_START, true);
				SharedPreferences.Editor editor = spn.edit();
				editor.putBoolean(FIRST_START, false);
				editor.commit();

				Constant.AREA_ID = city.getId() + "";
				Intent tomain = new Intent(cityCxt, HomePageActivity.class);
//			String string="autoCity.getText():"+autoCity.getText().toString()+"cityID:"+city.getCityID()+"cityName:"+city.getCityName();
				String string="定位的城市:"+autoCity.getText().toString()+"传进来的城市:"+city.getName()+"城市id:"+"是否点击了列表:"+ordian;
				Constant.citystr=string;
//			Toast.makeText(cityCxt,"autoCity.getText():"+autoCity.getText().toString()+"cityID:"+city.getCityID()+"cityName:"+city.getCityName(),Toast.LENGTH_LONG).show();
				if (PreferencesUtils.getBoolean(CityListsActivity.this,
						"firststart")) {
					PreferencesUtils.putBoolean(cityCxt, "firststart", false);
					PreferencesUtils.putInt(cityCxt, "thisvrtson", vrtson);
					startActivity(tomain);
					finish();

				}
//			else if(thisvrtson<vrtson){
//				PreferencesUtils.putInt(cityCxt, "thisvrtson", vrtson);
//				startActivity(toGuide);
//				finish();
//			}
				else {



					tomain.putExtra("cityList", "no");
					startActivity(tomain);
				}

				((Activity) cityCxt).finish();

			}
		});
	}

	public static int getLocalVersionCode(Context context)
			throws NameNotFoundException {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packageInfo.versionCode;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 *            输入的搜索词
	 */
	private void filterData(String filterStr) {
		List<CityModel.CityListBean> filterDateList = new ArrayList<CityModel.CityListBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = cityModel.getCity_list();
		} else {
			filterDateList.clear();
			for (CityModel.CityListBean items : cityModel.getCity_list()) {

				CityModel.CityListBean cityList = new CityModel.CityListBean();
				cityList.setLetter(items.getLetter());
				cityList.setItems(new ArrayList<CityModel.CityListBean.ItemsBean>());

				for(CityModel.CityListBean.ItemsBean item : items.getItems())
				{
					String name = item.getName();
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
							filterStr.toString())) {

						cityList.getItems().add(item);
					}

				}

				if(cityList.getItems().size() > 0)
				{
					filterDateList.add(cityList);
				}

			}
		}
		// 根据a-z进行排序
		adapter.updateListView(filterDateList);
	}

	/**
	 * 该页面所对应的点击事件操作
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cityimage_back:
			CityListsActivity.this.finish();
			break;
		case R.id.cityimage_refresh:
			Toast.makeText(cityCxt, "重新定位······", Toast.LENGTH_SHORT).show();
			mLocClient = ((location) getApplication()).mLocationClient;
			autoCity.setText("定位中···");
//			GetMyData.setLocationOption(mLocClient);
			mLocClient.start();
			mLocClient.requestLocation();
			((location) getApplication()).mTv = autoCity;
			break;
		default:
			break;
		}
	}

	/**
	 * 点击手机返回键所执行的操作
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (PreferencesUtils.getBoolean(CityListsActivity.this,
					"firststart")) {
				Tools.exitBy2Click(cityCxt);
			} else {
				CityListsActivity.this.finish();
			}

		}
		return false;

	}

	@Subscribe
	public void getEventmsg(MyEventBus myEventBus) {
		if (myEventBus.getMsg().equals("Location")) {
			BDLocation location = (BDLocation) myEventBus.getInfo();
			String cityname = location.getCity();
			autoCity.setText(cityname);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(CityListsActivity.this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(CityListsActivity.this);
	}
}