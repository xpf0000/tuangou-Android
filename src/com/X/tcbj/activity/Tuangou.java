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
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.X.tcbj.adapter.ChildListAdapter1;
import com.X.tcbj.adapter.ParentListAdapter1;
import com.X.tcbj.adapter.tuangouAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.server.location;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/25.
 */
public class Tuangou extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener

{
    Intent intent;
    ImageView img_back;// 返回图标
    ImageView img_seach;// 搜索图标
    TextView txt_area;// 全部区域
    TextView txt_classify;// 全部分类
    TextView txt_order;// 排序
    ListView shopLv;// 商家显示列表
    LinearLayout ly_pop_top;
    tuangouAdapter merchantListAdapter;
    String totalComment;
    String totalPage;
    int tmp;
    private int page;
    private int areaId;// 站点id
    private String pageSize;// 分页大小
    private String order = "0";// 排序依据
    private String bigAreaID = "0";// 一级区域
    private String smallAreaID = "0";// 二级区域
    private String bigClassID = "0";// 一级分类
    private String smallClassID = "0";// 二级分类
    private String path;// 商家列表地址
    String res;
    // 区域地址
    private String areaPath;
    // 分类地址
    private String classPath;
    private LinearLayout layout;
    private Button refresh;
    ArrayList<HashMap<String, String>> popList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    ArrayList<HashMap<String, Object>> areaList = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> areaMap;
    ArrayList<HashMap<String, Object>> shopList = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> shopMap;
    private ArrayList<HashMap<String, String>> childList01 = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> childMap01;
    /* popupWindow布局控件 */
    private PopupWindow popupWindow;
    View popView;
    Dialog dialog;
    // private boolean netStatisc;// 网络连接状态
    TextView txt_foot;
    int count, ver = 0;
    int lastItem;
    View footView;
    // 商圈显示
    private LinearLayout ll_list_none;
    private ListView parentListView, childListView;
    private ArrayList<HashMap<String, String>> parentList = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> parentMap;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> childMap;
    int myarg1 = 0;
    private String sta;
    private ParentListAdapter1 parentAdapter;
    private ChildListAdapter1 childAdapter;
    private int parentTagPosition = -1;
    private String HttpRes;// 区域和商圈获取的数据
    private ArrayList<HashMap<String, String>> listClass = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> mapClass;// 一级分类设置
    private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> mapItem;// 二级分类设置
    private ArrayList<HashMap<String, String>> listItem01 = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> mapItem01;// 二级分类设置
    private LinearLayout ll_list_class;
    private ListView lvClass, lvItem;
    int myarg2 = 0;
    private int parentTagPosition2 = -1, biaoshi01 = 1, biaoshi02 = 1;
    private String HttpRes2;
    private String[] orderStr = new String[] { "默认排序", "最新排序", " 销售排序", "评分排行","价格降序","价格升序" };
    private LocationClient mLocClient;
    public double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuangou);
        areaId =1;
        dialog = GetMyData.createLoadingDialog(Tuangou.this,
                "正在拼命的加载······");
        dialog.show();
        //mLocClient = ((location) getApplication()).mLocationClient;
//        GetMyData.setLocationOption(mLocClient);
        mLocClient.start();
        mLocClient.requestLocation();
        String str;
        bigClassID="0";
            str="全部分类";


//		new Handler().postDelayed(new Thread() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				super.run();
//
//			}
//		}, 300);
        init();
        txt_classify.setText(str);
    }

    public void init() {
        img_back = (ImageView) findViewById(R.id.imgview_shops_list_back);
        img_seach = (ImageView) findViewById(R.id.imgview_shops_list_search);
        txt_area = (TextView) findViewById(R.id.txt_shop_area);
        txt_classify = (TextView) findViewById(R.id.txt_shop_all);
        txt_classify.setText(Constant.MERCHANT_CLASSIFY_NAME);
        txt_order = (TextView) findViewById(R.id.txt_shop_order);
        layout = (LinearLayout) findViewById(R.id.shuaxin_shops);
        shopLv = (ListView) findViewById(R.id.list_shop);
        ly_pop_top = (LinearLayout) findViewById(R.id.ll_shops_list_pop);
        refresh = (Button) findViewById(R.id.freshen_shops);

        ll_list_none = (LinearLayout) findViewById(R.id.ll_list_below);
        ll_list_none.setOnClickListener(this);
        parentListView = (ListView) findViewById(R.id.lv_parent);
        parentListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        childListView = (ListView) findViewById(R.id.lv_child);
        childListView.setOnItemClickListener(new ChildItemOnclickListener());
        firstSetData();
        areaPath = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponAreas?siteId="+areaId;
        ll_list_class = (LinearLayout) findViewById(R.id.ll_list_class);
        ll_list_class.setOnClickListener(this);
        lvClass = (ListView) findViewById(R.id.lv_parent_class);
        lvClass.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvItem = (ListView) findViewById(R.id.lv_child_class);
        lvItem.setOnItemClickListener(new ChildClassItemOnclickListener());
        classPath = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponClass?siteId="
                + areaId;
        getParentData();
        getClassify();

        img_back.setOnClickListener(this);
        img_seach.setOnClickListener(this);
        txt_area.setOnClickListener(this);
        txt_classify.setOnClickListener(this);
        txt_order.setOnClickListener(this);
        refresh.setOnClickListener(this);

        getChildData();
        getClassifyItem();
    }

    public void firstSetData() {

        txt_area.setText("全部区域");
        smallAreaID = "0";
        txt_area.setText("全部区域");
        txt_classify.setText("全部分类");
        smallClassID = "0";
        txt_classify.setText("全部分类");
        order = "0";
        page = 1;
        pageSize = "10";
        path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
        System.out.println("path:" + path);
        setList();
        myInfo(1, path);
    }

    /**
     * 区域数据添加
     */
    private void getParentData() {
        parentList.clear();
        parentListView.setOnItemClickListener(new ParentItemOnclickListener());
        parentAdapter = new ParentListAdapter1(Tuangou.this, parentList);
        parentListView.setAdapter(parentAdapter);
    }

    /**
     * 商圈数据
     *
     * @param
     */
    private void getChildData() {
        getShangQuanJson(HttpRes);
        childAdapter = new ChildListAdapter1(childList, Tuangou.this);
        childListView.setAdapter(childAdapter);
    }

    /**
     * 一级分类数据
     */
    private void getClassify() {
        listClass.clear();
        lvClass.setOnItemClickListener(new ParentClassItemOnclickListener());
        parentAdapter = new ParentListAdapter1(Tuangou.this, listClass);
        lvClass.setAdapter(parentAdapter);

    }

    /**
     * 二级分类数据
     */
    private void getClassifyItem() {
        getChildJson(HttpRes2);
        childAdapter = new ChildListAdapter1(listItem, Tuangou.this);
        lvItem.setAdapter(childAdapter);
    }

    /**
     * 区域item点击事件
     *
     * @author Administrator
     *
     */
    private class ParentItemOnclickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            myarg1 = position;
            if (position == 0) {
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
                biaoshi01 = 1;
                txt_area.setText(parentList.get(position).get("AreasName")
                        .toString());
                PreferencesUtils.putString(Tuangou.this, "areaText",
                        txt_area.getText().toString());

                shopList.clear();
                parentAdapter.notifyDataSetChanged();
                footView.setVisibility(View.VISIBLE);
                page = 1;
                bigAreaID = "0";
                pageSize = "10";
                PreferencesUtils.putString(Tuangou.this, "areaId",
                        bigAreaID);
                childList.clear();
                if (childAdapter == null) {
                    childAdapter = new ChildListAdapter1(childList01,
                            Tuangou.this);
                    childListView.setAdapter(childAdapter);

                } else {
                    childAdapter.notifyDataSetChanged();
                    childAdapter = new ChildListAdapter1(childList01,
                            Tuangou.this);
                    childListView.setAdapter(childAdapter);
                }
                childListView.setVisibility(View.VISIBLE);

            } else {
                biaoshi01 = 2;
                childListView.setVisibility(View.VISIBLE);
                myarg1 = position - 1;
                bigAreaID = parentList.get(position).get("ID").toString();
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
    private class ChildItemOnclickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position == 0) {
                txt_area.setText("全部商圈");
                PreferencesUtils.putString(Tuangou.this, "areaText",
                        txt_area.getText().toString());
                ll_list_none.setVisibility(View.GONE);
                shopList.clear();
                footView.setVisibility(View.VISIBLE);
//				bigAreaID = "0";
                smallAreaID = "0";
                page = 1;
                pageSize = "10";
                path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                        "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                dialog = GetMyData.createLoadingDialog(Tuangou.this,
                        "正在拼命的加载······");
                dialog.show();
                childList01.clear();
                childAdapter.notifyDataSetChanged();
                myInfo(1, path);

            } else {
                dialog = GetMyData.createLoadingDialog(Tuangou.this,
                        "正在拼命的加载······");
                dialog.show();
                // 把用户的选择记录下来，下次进入时保留记录
                if (biaoshi01 == 1) {

                    txt_area.setText(childList01.get(position).get("AreasName")
                            .toString());
                } else {
                    smallAreaID = childList.get(position).get("ID");
                    txt_area.setText(childList.get(position).get("AreasName")
                            .toString());
                }
                shopList.clear();
                footView.setVisibility(View.VISIBLE);
                page = 1;
                pageSize = "10";
                path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                        "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                childList01.clear();
                childAdapter.notifyDataSetChanged();
                myInfo(1, path);
                ll_list_none.setVisibility(View.GONE);

            }
        }
    }

    /**
     * 一级分类item点击事件
     *
     * @author Administrator
     *
     */
    private class ParentClassItemOnclickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            myarg2 = position;
            if (position == 0) {
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
                biaoshi02 = 1;
                txt_classify.setText(listClass.get(position).get("AreasName")
                        .toString());
                shopList.clear();
                parentAdapter.notifyDataSetChanged();

                footView.setVisibility(View.VISIBLE);
                page = 1;
                pageSize = "10";
                bigClassID = "0";

//                PreferencesUtils.putString(Tuangou.this, "bigClassID", bigClassID);
                listItem.clear();
                if (childAdapter == null) {
                    childAdapter = new ChildListAdapter1(listItem01,
                            Tuangou.this);
                    lvItem.setAdapter(childAdapter);
                } else {
                    childAdapter.notifyDataSetChanged();
                    childAdapter = new ChildListAdapter1(listItem01,
                            Tuangou.this);
                    lvItem.setAdapter(childAdapter);
                }

            } else {
                biaoshi02 = 2;
                lvItem.setVisibility(View.VISIBLE);
                myarg2 = position - 1;
//                bigClassID = listClass.get(position).get("ID").toString();
//                PreferencesUtils.putString(Tuangou.this, "bigClassID",
//                        bigClassID);
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
    private class ChildClassItemOnclickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position == 0) {

                txt_classify.setText("全部分类");
                PreferencesUtils.putString(Tuangou.this, "classText",
                        txt_classify.getText().toString());
                ll_list_class.setVisibility(View.GONE);
                shopList.clear();

                footView.setVisibility(View.VISIBLE);

//                bigClassID = PreferencesUtils.getString(Tuangou.this,
//                        "bigClassID");
                smallClassID = "0";
                page = 1;
                pageSize = "10";

                path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                        "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";

                dialog = GetMyData.createLoadingDialog(Tuangou.this,
                        "正在拼命的加载······");
                dialog.show();
                myInfo(1, path);
                listClass.clear();
            } else {
                dialog = GetMyData.createLoadingDialog(Tuangou.this,
                        "正在拼命的加载······");
                dialog.show();
                // 把用户的选择记录下来，下次进入时保留记录
                if (biaoshi02 == 1) {
                    smallClassID = listItem01.get(position).get("ID");
                    txt_classify.setText(listItem01.get(position).get("AreasName")
                            .toString());
                } else {
                    smallClassID = listItem.get(position).get("ID");
                    txt_classify.setText(listItem.get(position).get("AreasName")
                            .toString());
                }
                PreferencesUtils.putString(Tuangou.this,
                        "smallClassId", smallClassID);
                shopList.clear();
                footView.setVisibility(View.VISIBLE);
                PreferencesUtils.putString(Tuangou.this, "classText",
                        txt_classify.getText().toString());
                page = 1;
                pageSize = "10";
                path ="http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                        "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                listItem01.clear();
                childAdapter.notifyDataSetChanged();
                myInfo(1, path);
                ll_list_class.setVisibility(View.GONE);
                listClass.clear();
            }
        }
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.imgview_shops_list_back:
                finish();
                break;
            case R.id.imgview_shops_list_search:
                ll_list_none.setVisibility(View.GONE);
                intent = new Intent(Tuangou.this, HotSearch.class);
                startActivity(intent);
                break;
            case R.id.txt_shop_area:
                // 点击区域分类时
                // showAreaPopView();
                childAdapter = new ChildListAdapter1(childList01,
                        Tuangou.this);
                childListView.setAdapter(childAdapter);
                ll_list_class.setVisibility(View.GONE);

                ll_list_none.setVisibility(View.VISIBLE);
                parentListView.setVisibility(View.VISIBLE);

                break;
            case R.id.txt_shop_all:
                ll_list_none.setVisibility(View.GONE);
                childAdapter = new ChildListAdapter1(listItem01,
                        Tuangou.this);
                lvItem.setAdapter(childAdapter);
                ll_list_class.setVisibility(View.VISIBLE);
                lvClass.setVisibility(View.VISIBLE);

                break;
            case R.id.txt_shop_order:
                ll_list_none.setVisibility(View.GONE);
                ll_list_class.setVisibility(View.GONE);
                showOrderPopView();
                break;
            case R.id.ll_list_below:
                ll_list_none.setVisibility(View.GONE);
                break;

            case R.id.ll_list_class:
                ll_list_class.setVisibility(View.GONE);
                break;

            case R.id.freshen_shops:
                dialog = GetMyData.createLoadingDialog(Tuangou.this,
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
        popView = Tuangou.this.getLayoutInflater().inflate(
                R.layout.activity_shops_pop, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = Tuangou.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        // int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, frameWith,
                ViewPager.LayoutParams.FILL_PARENT);
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
        for (int i = 0; i < 6; i++) {
            map = new HashMap<String, String>();
            map.put("item", orderStr[i]);
            popList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(Tuangou.this,
                popList, R.layout.activity_shops_pop_item,
                new String[] { "item" }, new int[] { R.id.txt_shop_pop_item });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                dialog = GetMyData.createLoadingDialog(Tuangou.this,
                        "正在拼命的加载······");
                dialog.show();
                page = 0;
                pageSize = "10";
                if (arg2 == 0) {
                    order = "0";
                    page = 1;
                    txt_order.setText("默认排序");
                    shopList.clear();
                    path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                    // 把用户的选择记录下来，下次进入时保留记录
                    PreferencesUtils.putString(Tuangou.this,
                            "orderName", "默认排序");
                    PreferencesUtils.putString(Tuangou.this, "order",
                            "0");

                    myInfo(1, path);
                } else if (arg2 == 1) {
                    order = "1";
                    page = 1;
                    pageSize="10";
                    txt_order.setText("最新排序");
                    shopList.clear();
                    path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                    // 把用户的选择记录下来，下次进入时保留记录
                    // PreferencesUtils.putString(ShopsDiscussList.this,
                    // "orderName", "人气排序");
                    // PreferencesUtils.putString(ShopsDiscussList.this,
                    // "order",
                    // "pop");
                    myInfo(1, path);
                } else if (arg2 == 2) {
                    order = "2";
                    txt_order.setText("销售排序");
                    page = 1;
                    pageSize="10";
                    shopList.clear();
                    path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";

                    // 把用户的选择记录下来，下次进入时保留记录
                    myInfo(1, path);
                } else if (arg2 == 3) {
                    order = "3";
                    txt_order.setText("评分排行");
                    page = 1;
                    pageSize="10";
                    shopList.clear();
                    path ="http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";

                    // 把用户的选择记录下来，下次进入时保留记录
                    myInfo(1, path);
                }else if (arg2 == 4) {
                    order = "4";
                    txt_order.setText("价格降序");
                    page = 1;
                    pageSize="10";
                    shopList.clear();
                    path ="http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
                    // 把用户的选择记录下来，下次进入时保留记录
                    myInfo(1, path);
                }
                else if (arg2 == 5) {
                    order = "5";
                    txt_order.setText("价格升序");
                    page = 1;
                    pageSize="10";
                    shopList.clear();
                    path ="http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                            "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";

                    // 把用户的选择记录下来，下次进入时保留记录
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

        merchantListAdapter = new tuangouAdapter(shopList,Tuangou.this
                );
        LayoutInflater inflater = (LayoutInflater) Tuangou.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footView = inflater.inflate(R.layout.view_list_foot, null);
        shopLv.addFooterView(footView, null, false);
        txt_foot = (TextView) footView.findViewById(R.id.txt_view_list_foot);

        shopLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                count = merchantListAdapter.getCount();
                switch (arg1) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
                            if (count < 10) {
                                Toast.makeText(Tuangou.this,
                                        "暂无商家信息，您可以选择其他分类信息浏览", Toast.LENGTH_SHORT)
                                        .show();
                                footView.setVisibility(View.GONE);
                            }
                            page++;
                            path = "http://192.168.2.28:8080/HKCityApi/tuan/getAllGrouponProduct?siteId="+areaId+"&order="+order+"&page="+page+"&pageSize="+pageSize+
                                    "&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
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
                // TODO Auto-generated method stub

            }
        });

        shopLv.setAdapter(merchantListAdapter);
        merchantListAdapter.notifyDataSetChanged();
        shopLv.setOnItemClickListener(this);// 点击进入商家详情
        // shopLv.setOnScrollListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getMerchantJson(res);// 获得商家的数据
                    getAreaJson(HttpRes);// 获得区域解析的数据
                    getGroupJson(HttpRes2);// 获得分类解析数据
                    merchantListAdapter.notifyDataSetChanged();
                    layout.setVisibility(View.GONE);
                    dialog.dismiss();
                    break;
                case 2:
                    Toast.makeText(Tuangou.this, "数据加载完毕",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 3:
                    dialog.dismiss();
                    layout.setVisibility(View.VISIBLE);
                    footView.setVisibility(View.GONE);
                    Toast.makeText(Tuangou.this, "网络访问超时",
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
                res = GetMyData.getURLString(path);
                System.out.println("url:"+url);
                if (res == null || res.equals("网络超时")) {
                    Message message = new Message();
                    message.what = 3;
                    handler.sendEmptyMessage(3);
                } else {

                    HttpRes = GetMyData.getURLString(areaPath);
                    HttpRes2 = GetMyData.getURLString(classPath);

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
               // shopMap.put("id", jo2.getString("id"));
               shopMap.put(
                       "PicID",
                       jo2.getString("PicID") == null ? "" : jo2
                               .getString("PicID"));
                shopMap.put(
                        "Title",
                        jo2.getString("Title") == null ? "" : jo2
                                .getString("Title"));
                shopMap.put(
                        "Intro",
                        jo2.getString("Intro") == null ? "" : jo2
                                .getString("Intro"));
                shopMap.put(
                        "OriginalPrice",
                        jo2.getString("OriginalPrice") == null ? "" : jo2
                                .getString("OriginalPrice"));
                shopMap.put(
                        "Price",
                        jo2.getString("Price") == null ? "" : jo2
                                .getString("Price"));
                shopMap.put("PeopleCount", jo2.getString("PeopleCount") == null ? ""
                        : jo2.getString("PeopleCount"));
                shopMap.put("ID", jo2.getString("ID") == null ? ""
                        : jo2.getString("ID"));
              //  shopMap.put("starnum", jo2.getString("starnum") == null ? ""
                        //: jo2.getString("starnum"));
              //  shopMap.put(
                      //  "FatherClass",
                       // jo2.getString("FatherClass") == null ? "" : jo2
                         //       .getString("FatherClass"));
              //  shopMap.put("SubClass", jo2.getString("SubClass") == null ? ""
                       // : jo2.getString("SubClass"));
             //   shopMap.put(
                       // "ClassName",
                      //  jo2.getString("ClassName") == null ? "" : jo2
                           //     .getString("ClassName"));
              //  shopMap.put("AreaCircle", jo2.get("AreaCircle") == null ? ""
                      //  : jo2.getString("AreaCircle"));
              //  shopMap.put("Map_Longitude", jo2.getString("Map_Longitude") == null ? ""
                      //  : jo2.getString("Map_Longitude"));
              //  shopMap.put("Map_Latitude", jo2.getString("Map_Latitude") == null ? ""
                       // : jo2.getString("Map_Latitude"));
              //  shopMap.put("longitude", longitude);
              //  shopMap.put("latitude", latitude);
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
     * 解析城市区域数据
     *
     * @param str
     */
    public void getAreaJson(String str) {
        parentList.clear();// 防止下拉加载时，数据再次加载
        childList01.clear();
        try {
            JSONObject jo = new JSONObject(str);
            JSONArray ja = jo.getJSONArray("list");
            parentMap = new HashMap<String, String>();
            parentMap.put("ID", "0");
            parentMap.put("AreasName", "全部区域");
            parentMap.put("check", "false");
            parentList.add(parentMap);
            childMap01 = new HashMap<String, String>();
            childMap01.put("ID", "0");
            childMap01.put("AreasName","全部商圈");
            childMap01.put("ParentID", "0");
            childList01.add(childMap01);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo2 = (JSONObject) ja.get(i);
                parentMap = new HashMap<String, String>();
                parentMap.put("ID", jo2.getString("ID"));
                parentMap.put("AreasName", jo2.getString("AreasName"));
                parentMap.put("check", "false");
                JSONArray ja2 = jo2.getJSONArray("TwoAreas");
                for (int j = 0; j < ja2.length(); j++) {
                    JSONObject jo3 = (JSONObject) ja2.get(j);
                    childMap01 = new HashMap<String, String>();
                    childMap01.put("ID", jo3.getString("ID"));
                    childMap01.put("AreasName", jo3.getString("AreasName"));
                    childMap01.put("ParentID", jo3.getString("ParentID"));
                    childList01.add(childMap01);
                }
                parentList.add(parentMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析城市区域数据
     *
     * @param str
     */
    public void getShangQuanJson(String str) {
        childList.clear();
        try {
            JSONObject jo = new JSONObject(str);
            JSONArray ja = jo.getJSONArray("list");
            JSONObject jo2 = (JSONObject) ja.get(myarg1);
            JSONArray ja2 = jo2.getJSONArray("TwoAreas");
            childMap = new HashMap<String, String>();
            childMap.put("ID", "0");
            childMap.put("AreasName", "全部商圈");
            childMap.put("ParentID", "0");
            childList.add(childMap);
            for (int j = 0; j < ja2.length(); j++) {
                JSONObject jo3 = (JSONObject) ja2.get(j);
                childMap = new HashMap<String, String>();
                childMap.put("ID", jo3.getString("ID"));
                childMap.put("AreasName", jo3.getString("AreasName"));
                childMap.put("ParentID", jo3.getString("ParentID"));
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
        listItem01.clear();
        try {
            JSONObject joNo = new JSONObject(str);
            int a = joNo.getInt("status");
            if (a != 0) {
                mapClass = new HashMap<String, String>();
                mapClass.put("ParentID", "0");
                mapClass.put("ID", "0");
                mapClass.put("AreasName", "全部分类");
                mapClass.put("TwoClass", "");
                mapClass.put("check", "false");
                listClass.add(mapClass);
            } else {

                JSONArray jaNo = joNo.getJSONArray("list");
                mapClass = new HashMap<String, String>();
                mapClass.put("ParentID", "0");
                mapClass.put("ID", "0");
                mapClass.put("AreasName", "全部分类");
                mapClass.put("TwoClass", "");
                mapClass.put("check", "false");
                listClass.add(mapClass);
                mapItem01 = new HashMap<String, String>();
                mapItem01.put("AreasName", "全部");
                mapItem01.put("ID", "0");
                listItem01.add(mapItem01);
                for (int i = 0; i < jaNo.length(); i++) {
                    JSONObject jo2 = (JSONObject) jaNo.get(i);
                    mapClass = new HashMap<String, String>();
                    mapClass.put("ParentID", i + 1 + "");
                    mapClass.put("ID", jo2.getString("ID"));
                    mapClass.put("AreasName", jo2.getString("ClassName"));
                    mapClass.put("check", "false");
                    mapClass.put("TwoClass", jo2.getString("TwoClass"));
                    JSONArray ja2 = jo2.getJSONArray("TwoClass");
                    for (int j = 0; j < ja2.length(); j++) {
                        JSONObject jo3 = (JSONObject) ja2.get(j);
                        mapItem01 = new HashMap<String, String>();
                        mapItem01.put("AreasName", jo3.getString("ClassName"));
                        mapItem01.put("ID", jo3.getString("ID"));
                        listItem01.add(mapItem01);
                    }
                    listClass.add(mapClass);
                }
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
            JSONArray ja2 = jo2.getJSONArray("TwoClass");

            mapItem = new HashMap<String, String>();
            mapItem.put("AreasName", "全部");
            mapItem.put("ID", "0");
            listItem.add(mapItem);
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject jo3 = (JSONObject) ja2.get(i);
                mapItem = new HashMap<String, String>();
                mapItem.put("AreasName", jo3.getString("ClassName"));
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
//        Constant.SHOP_ID = shopList.get(arg2).get("ID").toString();// 记录商家id
//        // 把商铺ID写入xml文件中
//        PreferencesUtils.putString(Tuangou.this, "storeID", shopList
//                .get(arg2).get("id").toString());
//        Intent intent = new Intent(Tuangou.this,
//                ShopDetailsActivity.class);
//        intent.putExtra("longitude", longitude);
//        intent.putExtra("latitude", latitude);
//        intent.putExtra("FatherClass", shopList.get(arg2).get("FatherClass").toString());
//        intent.putExtra("SubClass", shopList.get(arg2).get("SubClass").toString());
//        intent.putExtra("area", shopList.get(arg2).get("area").toString());
//        startActivity(intent);
        Intent intent=new Intent();
        intent.putExtra("id",shopList.get(arg2).get("ID").toString());
        intent.putExtra("smallAreaID",smallAreaID);
        intent.putExtra("smallClassID",smallClassID);
        intent.putExtra("order",order);
       intent.setClass(Tuangou.this, TuangouXiangqing.class);
        Tuangou.this.startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

}
