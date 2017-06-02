package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.ChildListAdapter;
import com.X.tcbj.adapter.MerchantListAdapter;
import com.X.tcbj.adapter.ParentListAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.server.location;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 商家列表
 * 
 * @author zmz
 * 
 */
public class ShopsListActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	Intent intent;
	ImageView img_back;// 返回图标
	ImageView img_seach;// 搜索图标
	TextView txt_area;// 全部区域
	TextView txt_classify;// 全部分类
	TextView txt_order;// 排序
	ListView shopLv;// 商家显示列表
	LinearLayout ly_pop_top;

	MerchantListAdapter merchantListAdapter;

	String totalComment;
	String totalPage;
	int tmp;
	private int page;
	private int areaId;// 站点id
	private String pageSize;// 分页大小
	private String order = "default";// 排序依据
	private String bigAreaID = "0";// 一级区域
	private String smallAreaID = "0";// 二级区域
	private String bigClassID = "0";// 一级分类
	private String smallClassID = "0";// 二级分类
	private String key = "empty";// 搜索词
	private String path;

	// 区域地址
	private String areaPath;
	// 分类地址
	private String classPath;

	ArrayList<HashMap<String, String>> popList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;

	ArrayList<HashMap<String, Object>> areaList = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> areaMap;

	ArrayList<HashMap<String, Object>> shopList = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> shopMap;
	String sendMsg;// 页面跳转传递过来的
	/* popupWindow布局控件 */
	private PopupWindow popupWindow;
	View popView;
	private String[] orderStr = new String[] { "默认排序", "人气排序", "等级排序", "入驻时间" };
	Dialog dialog;

	TextView txt_foot;
	int count;
	int lastItem;
	View footView;
	// 商圈显示
	private LinearLayout ll_list_none;
	private ListView parentListView, childListView;
	private ArrayList<HashMap<String, String>> parentList = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> parentMap;
	private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> childMap;
	private String HttpRes;// 区域和商圈获取的数据
	private ParentListAdapter parentAdapter;
	private ChildListAdapter childAdapter;
	private int parentTagPosition = -1;
	private String sta;
	int myarg1 = 0;
	private LinearLayout layout;
	private Button refresh;

	private ArrayList<HashMap<String, String>> listClass = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> mapClass;// 一级分类设置
	private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> mapItem;// 二级分类设置
	private LinearLayout ll_list_class;
	private ListView lvClass, lvItem;
	int myarg2 = 0;
	private int parentTagPosition2 = -1;
	private String HttpRes2;
	private LocationClient mLocClient;
	public double longitude, latitude;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
				"正在拼命的加载······");
		dialog.show();
		setContentView(R.layout.activity_shops_list);
		areaId = PreferencesUtils.getInt(ShopsListActivity.this, "cityID");
		intent = getIntent();
		String sendMsg = intent.getStringExtra("activity");
		init();
		if (sendMsg.equals("search")) {
			img_seach.setVisibility(View.GONE);
			txt_classify.setText("全部分类");
		} else if (sendMsg == null || sendMsg.equals("ShopsListActivity")) {
			img_seach = (ImageView) findViewById(R.id.imgview_shops_list_search);
			img_seach.setOnClickListener(this);
		}

	}

	public void init() {
		img_back = (ImageView) findViewById(R.id.imgview_shops_list_back);
		img_seach = (ImageView) findViewById(R.id.imgview_shops_list_search);
		txt_area = (TextView) findViewById(R.id.txt_shop_area);
		txt_classify = (TextView) findViewById(R.id.txt_shop_all);
		txt_classify.setText(Constant.SHOP_CLASSIFY_NAME);
		txt_order = (TextView) findViewById(R.id.txt_shop_order);
		txt_order.setText("默认排序");
		layout = (LinearLayout) findViewById(R.id.shuaxin_shops);
		refresh = (Button) findViewById(R.id.freshen_shops);
		shopLv = (ListView) findViewById(R.id.list_shop);
		ly_pop_top = (LinearLayout) findViewById(R.id.ll_shops_list_pop);

		ll_list_none = (LinearLayout) findViewById(R.id.ll_list_below);
		ll_list_none.setOnClickListener(this);
		parentListView = (ListView) findViewById(R.id.lv_parent);
		parentListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		childListView = (ListView) findViewById(R.id.lv_child);
		childListView.setOnItemClickListener(new ChildItemOnclickListener());
		areaPath = Constant.url+"GetTown?areaId=" + areaId;

		ll_list_class = (LinearLayout) findViewById(R.id.ll_list_class);
		ll_list_class.setOnClickListener(this);
		lvClass = (ListView) findViewById(R.id.lv_parent_class);
		lvClass.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvItem = (ListView) findViewById(R.id.lv_child_class);
		lvItem.setOnItemClickListener(new ChildClassItemOnclickListener());
		classPath = Constant.url+"industrylist?areaId="
				+ areaId;

		getParentData();
		getClassify();

		img_back.setOnClickListener(this);
		img_seach.setOnClickListener(this);
		txt_area.setOnClickListener(this);
		txt_classify.setOnClickListener(this);
		txt_order.setOnClickListener(this);
		refresh.setOnClickListener(this);

		if (Constant.KEY != null) {
			String keyStr = Constant.KEY;
			try {
				key = URLEncoder.encode(keyStr, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		page = 1;
		pageSize = "10";
		bigClassID = Constant.SHOP_SORT_ID;
		smallClassID = Constant.SHOP_CLASSIFY_ID;
		path = Constant.url+"storelist2?areaId=" + areaId
				+ "&page=" + page
				+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
				+ bigClassID + "&smallClassId=" + smallClassID + "&bigAreaId="
				+ bigAreaID + "&smallAreaId=" + smallAreaID + "&key=" + key
				+ "&order=" + order;

		setList();
		myInfo(1, path);

		getChildData();
		getClassifyItem();
	}

	/**
	 * 区域数据添加
	 */
	private void getParentData() {
		parentList.clear();
		parentListView.setOnItemClickListener(new ParentItemOnclickListener());
		parentAdapter = new ParentListAdapter(ShopsListActivity.this,
				parentList);
		parentListView.setAdapter(parentAdapter);
	}

	/**
	 * 商圈数据
	 * 
	 * @param
	 */
	private void getChildData() {
		getShangQuanJson(HttpRes);
		childAdapter = new ChildListAdapter(childList, ShopsListActivity.this);
		childListView.setAdapter(childAdapter);
	}

	/**
	 * 一级分类数据
	 */
	private void getClassify() {
		listClass.clear();
		lvClass.setOnItemClickListener(new ParentClassItemOnclickListener());
		parentAdapter = new ParentListAdapter(ShopsListActivity.this, listClass);
		lvClass.setAdapter(parentAdapter);

	}

	/**
	 * 二级分类数据
	 */
	private void getClassifyItem() {
		getChildJson(HttpRes2);
		childAdapter = new ChildListAdapter(listItem, ShopsListActivity.this);
		lvItem.setAdapter(childAdapter);
	}

	/**
	 * 区域item点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class ParentItemOnclickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			myarg1 = position;
			if (position == 0) {
				txt_area.setText(parentList.get(position).get("name")
						.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListText", txt_area.getText().toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListId", "0");
				ll_list_none.setVisibility(View.GONE);
				parentAdapter.notifyDataSetChanged();
				shopList.clear();

				footView.setVisibility(View.VISIBLE);
				page = 1;
				bigAreaID = "0";
				smallAreaID = "0";
				pageSize = "10";

				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListId", bigAreaID);
				PreferencesUtils.putString(ShopsListActivity.this,
						"shangQuanListId", smallAreaID);
				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;

				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				myInfo(1, path);

			} else {
				childListView.setVisibility(View.VISIBLE);
				myarg1 = position - 1;
				bigAreaID = parentList.get(position).get("id").toString();
				// 把用户的选择记录下来，下次进入时保留记录
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListName", parentList.get(position).get("name")
								.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListId", bigAreaID);
				if (position != parentTagPosition) {
					parentList.get(position).put("check", "true");
					if (parentTagPosition != -1) {
						parentList.get(parentTagPosition).put("check", "false");
					}
					parentTagPosition = position;
				} else {
					parentList.get(position).put("check", "true");

				}
				parentAdapter.notifyDataSetChanged();
				getChildData();// 商圈跟区域对应
			}

		}

	}

	/**
	 * 商圈点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class ChildItemOnclickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				txt_area.setText(PreferencesUtils.getString(
						ShopsListActivity.this, "areaListName"));
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListText", txt_area.getText().toString());
				ll_list_none.setVisibility(View.GONE);
				shopList.clear();

				footView.setVisibility(View.VISIBLE);
				bigAreaID = PreferencesUtils.getString(ShopsListActivity.this,
						"areaListId");
				page = 1;
				pageSize = "10";
				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;
				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				myInfo(1, path);
				parentList.clear();
			} else {
				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				// 把用户的选择记录下来，下次进入时保留记录
				smallAreaID = childList.get(position).get("quanId");
				PreferencesUtils.putString(ShopsListActivity.this,
						"shangQuanListName", childList.get(position)
								.get("quan").toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"shangQuanListId", smallAreaID);
				shopList.clear();

				footView.setVisibility(View.VISIBLE);
				txt_area.setText(childList.get(position).get("quan").toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListText", txt_area.getText().toString());
				page = 1;
				pageSize = "10";
				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;

				myInfo(1, path);
				ll_list_none.setVisibility(View.GONE);
				parentList.clear();
			}

		}
	}

	/**
	 * 一级分类item点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class ParentClassItemOnclickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			myarg2 = position;
			if (position == 0) {
				txt_classify.setText(listClass.get(position).get("name")
						.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"classListText", txt_classify.getText().toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"areaListId", "0");

				ll_list_class.setVisibility(View.GONE);
				parentAdapter.notifyDataSetChanged();
				shopList.clear();

				footView.setVisibility(View.VISIBLE);
				page = 1;
				pageSize = "10";

				bigClassID = "0";
				smallClassID = "0";
				PreferencesUtils.putString(ShopsListActivity.this,
						"bigClassListID", bigClassID);
				PreferencesUtils.putString(ShopsListActivity.this,
						"smallClassListID", smallClassID);

				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;

				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				myInfo(1, path);
				listClass.clear();

			} else {
				lvItem.setVisibility(View.VISIBLE);
				myarg2 = position - 1;
				bigClassID = listClass.get(position).get("ID").toString();
				// 把用户的选择记录下来，下次进入时保留记录
				PreferencesUtils.putString(ShopsListActivity.this,
						"classListName", listClass.get(position).get("name")
								.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"bigClassListID", bigClassID);
				if (position != parentTagPosition2) {
					listClass.get(position).put("check", "true");
					if (parentTagPosition2 != -1) {
						listClass.get(parentTagPosition2).put("check", "false");
					}
					parentTagPosition2 = position;
				} else {
					listClass.get(position).put("check", "true");

				}
				parentAdapter.notifyDataSetChanged();
				getClassifyItem();// 二级菜单跟一级菜单对应
			}
		}
	}

	/**
	 * 二级菜单点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class ChildClassItemOnclickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				txt_classify.setText(PreferencesUtils.getString(
						ShopsListActivity.this, "classListName"));
				PreferencesUtils.putString(ShopsListActivity.this,
						"classListText", txt_classify.getText().toString());
				ll_list_class.setVisibility(View.GONE);
				shopList.clear();

				footView.setVisibility(View.VISIBLE);

				bigClassID = PreferencesUtils.getString(ShopsListActivity.this,
						"bigClassListID");
				smallClassID = "0";
				page = 1;
				pageSize = "10";
				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;

				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				myInfo(1, path);
				listClass.clear();
			} else {
				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				// 把用户的选择记录下来，下次进入时保留记录
				smallClassID = listItem.get(position).get("ID");

				PreferencesUtils.putString(ShopsListActivity.this,
						"classItemListName", listItem.get(position).get("quan")
								.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"smallClassListId", smallClassID);
				shopList.clear();

				footView.setVisibility(View.VISIBLE);
				txt_classify.setText(listItem.get(position).get("quan")
						.toString());
				PreferencesUtils.putString(ShopsListActivity.this,
						"classListText", txt_classify.getText().toString());
				page = 1;
				pageSize = "10";
				path = Constant.url+"storelist2?areaId="
						+ areaId + "&page=" + page
						+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
						+ bigClassID + "&smallClassId=" + smallClassID
						+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
						+ smallAreaID + "&key=" + key + "&order=" + order;

				myInfo(1, path);

				ll_list_class.setVisibility(View.GONE);
				listClass.clear();
			}
		}
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imgview_shops_list_back:
			ll_list_none.setVisibility(View.GONE);
			ShopsListActivity.this.finish();
			break;
		case R.id.imgview_shops_list_search:
			ll_list_none.setVisibility(View.GONE);
			intent = new Intent(ShopsListActivity.this, HotSearch.class);
			startActivity(intent);
			finish();
			break;
		case R.id.txt_shop_area:
			ll_list_class.setVisibility(View.GONE);

			ll_list_none.setVisibility(View.VISIBLE);
			parentListView.setVisibility(View.VISIBLE);
			break;
		case R.id.ll_list_below:
			ll_list_none.setVisibility(View.GONE);
			break;

		case R.id.ll_list_class:
			ll_list_class.setVisibility(View.GONE);
			break;

		case R.id.txt_shop_all:
			ll_list_none.setVisibility(View.GONE);

			ll_list_class.setVisibility(View.VISIBLE);
			lvClass.setVisibility(View.VISIBLE);
			break;
		case R.id.txt_shop_order:
			ll_list_none.setVisibility(View.GONE);
			ll_list_class.setVisibility(View.GONE);
			showOrderPopView();
			break;
		case R.id.freshen_shops:
			dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
					"正在拼命的加载······");
			dialog.show();
			myInfo(1, path);
			break;
		default:
			break;
		}
	}

	/**
	 * 筛选条件
	 */
	public void showOrderPopView() {
		popView = getLayoutInflater()
				.inflate(R.layout.activity_shops_pop, null);
		// 获取手机屏幕的宽和高
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		// 没有用到屏幕的高
		// int frameHeith = display.getHeight();
		int frameWith = display.getWidth();
		popupWindow = new PopupWindow(popView, frameWith,
				LayoutParams.FILL_PARENT);
		// 使其聚焦
		popupWindow.setFocusable(true);
		// 设置不允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		// 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 刷新状态
		popupWindow.update();
		ListView listView = (ListView) popView.findViewById(R.id.lv_shop_pop);
		popList.clear();
		for (int i = 0; i < 4; i++) {
			map = new HashMap<String, String>();
			map.put("item", orderStr[i]);
			popList.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(ShopsListActivity.this,
				popList, R.layout.activity_shops_pop_item,
				new String[] { "item" }, new int[] { R.id.txt_shop_pop_item });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				dialog = GetMyData.createLoadingDialog(ShopsListActivity.this,
						"正在拼命的加载······");
				dialog.show();
				page = 0;
				pageSize = totalComment;
				if (arg2 == 0) {
					order = "default";
					txt_order.setText("默认排序");
					page = 1;
					shopList.clear();
					path = Constant.url+"storelist2?areaId="
							+ areaId
							+ "&page="
							+ page
							+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
							+ bigClassID + "&smallClassId=" + smallClassID
							+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
							+ smallAreaID + "&key=" + key + "&order=" + order;
					// 把用户的选择记录下来，下次进入时保留记录
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderNameList", "默认排序");
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderList", "default");
					myInfo(1, path);
				} else if (arg2 == 1) {
					order = "pop";
					txt_order.setText("人气排序");
					page = 1;
					shopList.clear();
					path = Constant.url+"storelist2?areaId="
							+ areaId
							+ "&page="
							+ page
							+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
							+ bigClassID + "&smallClassId=" + smallClassID
							+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
							+ smallAreaID + "&key=" + key + "&order=" + order;
					// 把用户的选择记录下来，下次进入时保留记录
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderNameList", "人气排序");
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderList", "pop");
					myInfo(1, path);
				} else if (arg2 == 2) {
					order = "class";
					txt_order.setText("等级排序");
					shopList.clear();
					page = 1;
					path = Constant.url+"storelist2?areaId="
							+ areaId
							+ "&page="
							+ page
							+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
							+ bigClassID + "&smallClassId=" + smallClassID
							+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
							+ smallAreaID + "&key=" + key + "&order=" + order;

					PreferencesUtils.putString(ShopsListActivity.this,
							"orderNameList", "等级排序");
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderList", "class");
					myInfo(1, path);
				} else if (arg2 == 3) {
					order = "time";
					txt_order.setText("入驻时间");
					shopList.clear();
					page = 1;
					path = Constant.url+"storelist2?areaId="
							+ areaId
							+ "&page="
							+ page
							+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
							+ bigClassID + "&smallClassId=" + smallClassID
							+ "&bigAreaId=" + bigAreaID + "&smallAreaId="
							+ smallAreaID + "&key=" + key + "&order=" + order;
					// 把用户的选择记录下来，下次进入时保留记录
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderNameList", "入驻时间");
					PreferencesUtils.putString(ShopsListActivity.this,
							"orderList", "time");
					myInfo(1, path);
				}
				popupWindow.dismiss();// 关闭popupWindow
			}
		});
		popupWindow.showAsDropDown(ly_pop_top);// 显示在筛选区域条件下
	}

	/**
	 * 为商家列表赋值
	 */
	public void setList() {
		count = shopList.size();
		merchantListAdapter = new MerchantListAdapter(ShopsListActivity.this,
				shopList);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footView = inflater.inflate(R.layout.view_list_foot, null);
		shopLv.addFooterView(footView, null, false);
		txt_foot = (TextView) footView.findViewById(R.id.txt_view_list_foot);

		shopLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				count = merchantListAdapter.getCount();
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
						if (count < 10) {
							Toast.makeText(ShopsListActivity.this,
									"暂无商家信息，您可以选择其他分类信息浏览", Toast.LENGTH_SHORT)
									.show();
							footView.setVisibility(View.GONE);
						}
						page++;
						path = Constant.url+"storelist2?areaId="
								+ areaId
								+ "&page="
								+ page
								+ "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
								+ bigClassID
								+ "&smallClassId="
								+ smallClassID
								+ "&bigAreaId="
								+ bigAreaID
								+ "&smallAreaId="
								+ smallAreaID
								+ "&key="
								+ key
								+ "&order="
								+ order;
						if (page == tmp) {
							footView.setVisibility(View.GONE);
						}
						myInfo(1, path);
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

		shopLv.setAdapter(merchantListAdapter);
		merchantListAdapter.notifyDataSetChanged();
		shopLv.setOnItemClickListener(this);// 点击进入商家详情
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				merchantListAdapter.notifyDataSetChanged();
				layout.setVisibility(View.GONE);
				dialog.dismiss();
				break;
			case 2:
				Toast.makeText(ShopsListActivity.this, "数据加载完毕",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case 3:
				dialog.dismiss();
				layout.setVisibility(View.VISIBLE);
				footView.setVisibility(View.GONE);
				Toast.makeText(ShopsListActivity.this, "网络访问超时",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				txt_foot.setText("数据加载完毕");
				break;
			case 5:
				txt_foot.setText("正在加载中");
				break;
			}
		};
	};

	/**
	 * 加载数据时开启一个新线程
	 */
	public void myInfo(final int what, final String url) {
		new Thread() {
			public void run() {
				String res = GetMyData.getURLString(path);
				if (res == null || res.equals("网络超时")) {
					Message message = new Message();
					message.what = 3;
					handler.sendEmptyMessage(3);
				} else {
					getMerchantJson(res);// 获得解析的数据
					HttpRes = GetMyData.getURLString(areaPath);
					HttpRes2 = GetMyData.getURLString(classPath);
					getAreaJson(HttpRes);// 获得解析的数据
					getGroupJson(HttpRes2);// 获得分类解析数据
					Message msg = new Message();
					msg.what = what;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 解析商家数据
	 * 
	 * @param str
	 */
	public void getMerchantJson(String str) {
		try {
			// shopList.clear();// 清空之前的数据
			com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject
					.parseObject(str);
			sta = jo.getString("status");
			if (sta.equals("1")) {
				Message msg = new Message();
				msg.what = 4;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 5;
				handler.sendMessage(msg);
			}
			com.alibaba.fastjson.JSONArray ja = jo.getJSONArray("list");
			totalComment = jo.getString("totalRecord");
			//longitude = ((location) getApplication()).longitude;
			//latitude = ((location) getApplication()).latitude;
			for (int i = 0; i < ja.size(); i++) {
				com.alibaba.fastjson.JSONObject jo2 = ja.getJSONObject(i);
				if (ja.size() < 10) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				}
				shopMap = new HashMap<String, Object>();
				shopMap.put("id", jo2.getString("id"));
				shopMap.put(
						"imgURL",
						jo2.getString("url") == null ? "" : jo2
								.getString("url"));
				shopMap.put(
						"name",
						jo2.getString("name") == null ? "" : jo2
								.getString("name"));
				shopMap.put(
						"type",
						jo2.getString("type") == null ? "" : jo2
								.getString("type"));
				shopMap.put(
						"area",
						jo2.getString("area") == null ? "" : jo2
								.getString("area"));
				shopMap.put(
						"isvip",
						jo2.getString("isvip") == null ? "" : jo2
								.getString("isvip"));
				shopMap.put("iscard", jo2.getString("iscard") == null ? ""
						: jo2.getString("iscard"));
				shopMap.put("isauth", jo2.getString("isauth") == null ? ""
						: jo2.getString("isauth"));
				shopMap.put("isrec", jo2.getString("isrec") == null ? ""
						: jo2.getString("isrec"));
				shopMap.put("starnum", jo2.getString("starnum") == null ? ""
						: jo2.getString("starnum"));
				shopMap.put(
						"FatherClass",
						jo2.getString("FatherClass") == null ? "" : jo2
								.getString("FatherClass"));
				shopMap.put("SubClass", jo2.getString("SubClass") == null ? ""
						: jo2.getString("SubClass"));
				shopMap.put(
						"ClassName",
						jo2.getString("ClassName") == null ? "" : jo2
								.getString("ClassName"));
				shopMap.put("AreaCircle", jo2.get("AreaCircle") == null ? ""
						: jo2.getString("AreaCircle"));
				shopMap.put("Map_Longitude", jo2.getString("Map_Longitude") == null ? ""
						: jo2.getString("Map_Longitude"));
				shopMap.put("Map_Latitude", jo2.getString("Map_Latitude") == null ? ""
						: jo2.getString("Map_Latitude"));
				shopMap.put("longitude", longitude);
				shopMap.put("latitude", latitude);
				shopList.add(shopMap);

			}
			int i1 = Integer.parseInt(totalComment);

			if (i1 % 10 == 0) {
				tmp = i1 / 10;
			} else {
				tmp = i1 / 10 + 1;
			}

			totalPage = tmp + "";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析城市二级区域数据
	 * 
	 * @param str
	 */
	public void getAreaJson(String str) {
		parentList.clear();
		try {
			JSONObject jo = new JSONObject(str);
			JSONArray ja = jo.getJSONArray("list");
			parentMap = new HashMap<String, String>();
			parentMap.put("id", "0");
			parentMap.put("name", "全部区域");
			parentMap.put("check", "false");
			parentList.add(parentMap);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo2 = (JSONObject) ja.get(i);
				parentMap = new HashMap<String, String>();
				parentMap.put("id", jo2.getString("id"));
				parentMap.put("name", jo2.getString("name"));
				parentMap.put("check", "false");
				parentList.add(parentMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析城市商圈数据
	 * 
	 * @param str
	 */
	public void getShangQuanJson(String str) {
		childList.clear();
		try {
			JSONObject jo = new JSONObject(str);
			JSONArray ja = jo.getJSONArray("list");
			JSONObject jo2 = (JSONObject) ja.get(myarg1);
			JSONArray ja2 = jo2.getJSONArray("QuanList");
			childMap = new HashMap<String, String>();
			childMap.put("quanId", "0");
			childMap.put("quan", "全部商圈");
			childMap.put("fatherId", "0");
			childList.add(childMap);
			for (int j = 0; j < ja2.length(); j++) {
				JSONObject jo3 = (JSONObject) ja2.get(j);
				childMap = new HashMap<String, String>();
				childMap.put("quanId", jo3.getString("quanId"));
				childMap.put("quan", jo3.getString("quan"));
				childMap.put("fatherId", jo3.getString("fatherId"));
				childList.add(childMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析一级分类数据
	 * 
	 * @param str
	 */
	public void getGroupJson(String str) {
		listClass.clear();// 清空之前的数据
		try {
			JSONObject joNo = new JSONObject(str);
			JSONArray jaNo = joNo.getJSONArray("list");
			mapClass = new HashMap<String, String>();
			mapClass.put("id", "0");
			mapClass.put("ID", "0");
			mapClass.put("name", "全部分类");
			mapClass.put("CatgorysList", "");
			mapClass.put("check", "false");
			listClass.add(mapClass);
			for (int i = 0; i < jaNo.length(); i++) {
				JSONObject jo2 = (JSONObject) jaNo.get(i);
				mapClass = new HashMap<String, String>();
				mapClass.put("id", i + 1 + "");
				mapClass.put("ID", jo2.getString("ID"));
				mapClass.put("name", jo2.getString("CatgoryName"));
				mapClass.put("CatgorysList", jo2.getString("CatgorysList"));
				mapClass.put("check", "false");
				listClass.add(mapClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析二级分类数据
	 * 
	 * @param str
	 */
	public void getChildJson(String str) {
		listItem.clear();// 清空之前的数据
		try {
			JSONObject jo = new JSONObject(str);
			JSONArray ja = jo.getJSONArray("list");
			JSONObject jo2 = (JSONObject) ja.get(myarg2);
			JSONArray ja2 = jo2.getJSONArray("CatgorysList");

			mapItem = new HashMap<String, String>();
			mapItem.put("quan", "全部");
			mapItem.put("ID", "0");
			listItem.add(mapItem);
			for (int i = 0; i < ja2.length(); i++) {
				JSONObject jo3 = (JSONObject) ja2.get(i);
				mapItem = new HashMap<String, String>();
				mapItem.put("quan", jo3.getString("Name"));
				mapItem.put("ID", jo3.getString("ID"));
				listItem.add(mapItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 点击进入商家详情页
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		Constant.SHOP_ID = shopList.get(arg2).get("id").toString();// 记录商家id
//		// 把商铺ID写入xml文件中
//		PreferencesUtils.putString(ShopsListActivity.this, "storeID", shopList
//				.get(arg2).get("id").toString());
//		Intent intent = new Intent(ShopsListActivity.this,
//				ShopDetailsActivity.class);
//		startActivity(intent);
		Constant.SHOP_ID = shopList.get(arg2).get("id").toString();// 记录商家id
		// 把商铺ID写入xml文件中
		PreferencesUtils.putString(ShopsListActivity.this, "storeID", shopList
				.get(arg2).get("id").toString());
		if (shopList.get(arg2).get("isvip").equals("2")){
			Intent intent = new Intent(ShopsListActivity.this,
					ShopVipInfo.class);
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude", latitude);
			intent.putExtra("FatherClass", shopList.get(arg2).get("FatherClass").toString());
			intent.putExtra("SubClass", shopList.get(arg2).get("SubClass").toString());
			intent.putExtra("area", shopList.get(arg2).get("area").toString());
			startActivity(intent);
		}else {
			Intent intent = new Intent(ShopsListActivity.this,
					ShopDetailsActivity.class);
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude", latitude);
			intent.putExtra("FatherClass", shopList.get(arg2).get("FatherClass").toString());
			intent.putExtra("SubClass", shopList.get(arg2).get("SubClass").toString());
			intent.putExtra("area", shopList.get(arg2).get("area").toString());
			startActivity(intent);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
