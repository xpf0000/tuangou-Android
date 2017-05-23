package com.X.tcbj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.X.tcbj.SmoothListView.SmoothListView;
import com.X.tcbj.activity.CityListsActivity;
import com.X.tcbj.activity.Mall;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.SearchActivity;
import com.X.tcbj.activity.testnews;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.HomeClassAdapter;
import com.X.tcbj.adapter.HotBigAdapter;
import com.X.tcbj.adapter.Information_adpater;
import com.X.tcbj.adapter.smallAdapter;
import com.X.tcbj.domain.CityModel;
import com.X.tcbj.domain.HomeClassMod;
import com.X.tcbj.myview.My_GridView;
import com.X.tcbj.myview.UpdatePop;
import com.X.tcbj.utils.CommonField;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetVersion;
import com.X.model.HomeModel;
import com.X.server.DataCache;
import com.X.server.location;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XNetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * 首页展示
 *
 * @author zpp
 * @date 201501204
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    View view, classview, shopview, popView;
    SmoothListView smoothListView;
    smallAdapter myAdapter;
    private int adViewTopSpace = 0; // 广告视图距离顶部的距离
    RelativeLayout rlBar;
    private int adViewHeight = 180; // 广告视图的高度
    private int titleViewHeight = 50; // 标题栏的高度
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    View viewActionMoreBg;
    location location = null;
    //    View viewTitleBg;
    private int filterViewPosition = 4; // 筛选视图的位置
    private View itemHeaderAdView; // 从ListView获取的广告子View
    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private HomeHandler handler = null;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    String ver;

    List<HomeModel.DealListBean> hotmallarray = new ArrayList<>();
    My_GridView big_class, maill, head_class;

    HomeAdapter homeAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    HotBigAdapter hotBigAdapter;
    HomeClassAdapter homeClassAdapter;
    TextView view_action_more_bg, city_name;
    ArrayList<HomeClassMod.ListBean> biglist = new ArrayList<>();
    ArrayList<HomeClassMod.ListBean> smallist = new ArrayList<>();
    UpdatePop myPopwindows;
    private PopupWindow popupWindow;

    HomeModel homeModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ver = GetVersion.getAppVersionName(getActivity());

        intview();
        getBanner();

        Information_adpater information_adpater = new Information_adpater(getActivity(), new ArrayList<HashMap<String, Object>>());
        smoothListView.setAdapter(information_adpater);

        return view;
    }

    private void intview() {
        myPopwindows = new UpdatePop();
        handler = new HomeHandler();
        location = (location) getActivity().getApplication();
        view_action_more_bg = (TextView) view.findViewById(R.id.view_action_more_bg);
        city_name = (TextView) view.findViewById(R.id.city_name);
        city_name.setOnClickListener(this);
        city_name.setText(DataCache.getInstance().nowCity.getName());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        smoothListView = (SmoothListView) view.findViewById(R.id.listView);
        rlBar = (RelativeLayout) view.findViewById(R.id.rl_bar);
        viewActionMoreBg = (View) view.findViewById(R.id.view_action_more_bg);
        smoothListView.setLoadMoreEnable(false);
        smoothListView.setRefreshEnable(false);
        filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
//
        shopview = LayoutInflater.from(getActivity()).inflate(R.layout.shophead, null);
        classview = LayoutInflater.from(getActivity()).inflate(R.layout.headview, null);
//
        big_class = (My_GridView) shopview.findViewById(R.id.big_class);
        maill = (My_GridView) shopview.findViewById(R.id.maill);
//
        head_class = (My_GridView) classview.findViewById(R.id.head_class);
//
        smoothListView.addHeaderView(classview);
        smoothListView.addHeaderView(shopview);
        view_action_more_bg.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBanner();
                getSmlHot();
            }
        });
        big_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (biglist.get(position).getLevel() == 1) {
                    Intent intent = new Intent(getActivity(), Mall.class);
                    intent.putExtra("id", biglist.get(position).getFirstLevel() + "");
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Mall.class);
                    intent.putExtra("id", biglist.get(position).getSecondLevel() + "");
                    getActivity().startActivity(intent);
                }
            }
        });

        smoothListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                rlBar.setAlpha(1f);
//                rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(MainActivity.this, fraction, R.color.transparent, R.color.orange));
//                if (isScrollIdle && adViewTopSpace < 0) return;
//                if (itemHeaderAdView == null) {
//                    itemHeaderAdView = smoothListView.getChildAt(1 - firstVisibleItem);
//                }
//                if (itemHeaderAdView != null) {
//                    adViewTopSpace = DensityUtil.px2dip(getActivity(), itemHeaderAdView.getTop());
//                    adViewHeight = DensityUtil.px2dip(getActivity(), itemHeaderAdView.getHeight());
//                }
//                if (itemHeaderFilterView == null) {
//                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
//                }
//                if (itemHeaderFilterView != null) {
//                    adViewTopSpace = DensityUtil.px2dip(getActivity(), itemHeaderAdView.getTop());
//                    adViewHeight = DensityUtil.px2dip(getActivity(), itemHeaderAdView.getHeight());
//                }
//                if (itemHeaderFilterView == null) {
//                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
//                }
//                handleTitleBarColorEvaluate();

            }

            @Override
            public void onSmoothScrolling(View view) {

            }
        });
        smoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (infoarray.get(position - smoothListView.getHeaderViewsCount()).get("orPpt").equals("false")) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", infoarray.get(position - smoothListView.getHeaderViewsCount()).get("id").toString());
//                    intent.putExtra("newsClassID",
//                            infoarray.get(position - smoothListView.getHeaderViewsCount()).get("newsClassId").toString());
//                    intent.putExtra("picId",
//                            infoarray.get(position - smoothListView.getHeaderViewsCount()).get("picId").toString());
//                    intent.setClass(getActivity(), Info_info.class);
//                    getActivity().startActivity(intent);
//                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra("iD", infoarray.get(position - smoothListView.getHeaderViewsCount()).get("id").toString());
//                    intent.setClass(getActivity(), ImgNewsInfo.class);
//                    getActivity().startActivity(intent);
//                }
            }
        });

        maill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", hotmallarray.get(position).getId());
                intent.setClass(getActivity(), MallInfo.class);
                getActivity().startActivity(intent);
            }
        });

        head_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent;
//                switch (homeclass.get(position).getId()) {
//                    case 1:
//                        intent = new Intent(getActivity(), Mall.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 2:
//                        intent = new Intent(getActivity(), ShangjiaActivicty.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 3:
//                        intent = new Intent(getActivity(), Privilegelist.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 4:
//                        intent = new Intent(getActivity(), MiaoShaActivicty.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 5:
//                        intent = new Intent(getActivity(), MianfeiShiyongActivicty.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 6:
//                        intent = new Intent(getActivity(), SeminarActivity.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 7:
//                        intent = new Intent(getActivity(), testnews.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 8:
//                        Constant.KEY = null;
//                        intent = new Intent(getActivity(), ShangjiasActivicty.class);
//                        intent.putExtra("id", "1");
//                        getActivity().startActivity(intent);
//                        break;
//                    case 9:
//                        Constant.KEY = null;
//                        intent = new Intent(getActivity(), ShangjiasActivicty.class);
//                        intent.putExtra("id", "94");
//                        getActivity().startActivity(intent);
//                        break;
//                    case 10:
//                        intent = new Intent(getActivity(), Facilitate.class);
//                        getActivity().startActivity(intent);
//                        break;
//                    case 11:
//                        intent = new Intent(getActivity(), BBSActivity.class);
//                        getActivity().startActivity(intent);
//                        break;
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.view_action_more_bg:
                intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.city_name:
                location.setHandler(handler);
                intent = new Intent(getActivity(), CityListsActivity.class);
                startActivity(intent);
                break;
            case R.id.malllay:
                intent = new Intent(getActivity(), Mall.class);
                startActivity(intent);
                break;
            case R.id.newslay:
                intent = new Intent(getActivity(), testnews.class);
                startActivity(intent);
                break;
        }
    }

    public final class HomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isCityPosition();
                    break;
                case 6:
                    try {
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        getActivity().finish();
                    }
                    break;
            }
        }
    }

    //首页banner图
    private void getBanner() {

        double x = 0.0,y=0.0;

        if(XPostion.getInstance().getPostion() != null)
        {
            x = XPostion.getInstance().getPostion().getLongitude();
            y = XPostion.getInstance().getPostion().getLatitude();
        }


        XNetUtil.Handle(APPService.app_index(DataCache.getInstance().nowCity.getId(),x,y), new XNetUtil.OnHttpResult<HomeModel>() {
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onSuccess(HomeModel homeModel) {
                HomeFragment.this.homeModel = homeModel;
                XNetUtil.APPPrintln(homeModel.getIndexs().toString());
                homeClassAdapter = new HomeClassAdapter(homeModel.getIndexs(), getActivity());
                head_class.setAdapter(homeClassAdapter);

                homeAdapter = new HomeAdapter(homeModel.getDeal_list(), getActivity());
                maill.setAdapter(homeAdapter);

                hotBigAdapter = new HotBigAdapter(homeModel.getZt_html(), getActivity());
                big_class.setAdapter(hotBigAdapter);

                XNetUtil.APPPrintln("%%%%%%%%%%: "+homeModel.getZt_html().size());

                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    //热门分类
    private void getSmlHot() {
//        AsyncHttpHelper.getAbsoluteUrl(ProductsClass, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int x, Header[] headers, String s) {
//                homeClassMod = JSON.parseObject(s, HomeClassMod.class);
//                if (homeClassMod.getStatus() == 0) {
//                    biglist = new ArrayList<HomeClassMod.ListBean>();
//                    smallist = new ArrayList<HomeClassMod.ListBean>();
//                    for (int i = 0; i < homeClassMod.getList().size(); i++) {
//                        if (i < 4) {
//                            biglist.add(homeClassMod.getList().get(i));
//                        } else {
//                            smallist.add(homeClassMod.getList().get(i));
//                        }
//                    }
//                }
//                hotBigAdapter = new HotBigAdapter(biglist, getActivity());
//                big_class.setAdapter(hotBigAdapter);
//                hotSmall = new HotSmall(smallist, getActivity());
//                sm_class.setAdapter(hotSmall);
//            }
//        });

    }

    //更新
    private void getNewVer() {
//        AsyncHttpHelper.getAbsoluteUrl(verurl, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                isCityPosition();
//            }
//
//            @Override
//            public void onSuccess(int x, Header[] headers, String s) {
//                JSONObject jsonObject = JSON.parseObject(s);
//                if (jsonObject.getIntValue("status") == 0) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("list");
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        if (jsonObject1.getBoolean("OrUp")) {
//                            String update = jsonObject1.getString("Content");
//                            final String url = jsonObject1.getString("Url");
//                            myPopwindows.showpop(getActivity(), update, handler);
//                            myPopwindows.setMyPopwindowswListener(new UpdatePop.MyPopwindowsListener() {
//                                @Override
//                                public void onRefresh() {
//                                    Uri uri = Uri.parse(url);
//                                    DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
//                                    DownloadManager.Request request = new DownloadManager.Request(uri);
//                                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//                                    request.setDestinationInExternalPublicDir("appupdate", "update.apk");
//                                    request.setVisibleInDownloadsUi(true);
//                                    long downLoadId = downloadManager.enqueue(request);
//                                }
//                            });
//                        } else {
//                            isCityPosition();
//                        }
//                    }
//                }
//            }
//        });
    }

    //切换城市
    public void isCityPosition() {
        // 1、先判断定位到的城市是否开通站点
//		Constant.CITY_POSITION="北京";
//        cityPosition = Constant.CITY_POSITION;
        String pocityname = location.mData;
        if (CommonField.isCity(location.mData)) {
            // 2、若开通则判断是否与当前显示的城市相同
            System.out.println("bollen" + city_name.getText().equals(Constant.CITY_POSITION));
            if (city_name.getText().equals(pocityname)) {
                Constant.status = 1;
            } else {
                // 3、若与当前的不同则弹出切换城市的提示
                System.out.println(pocityname);
                showPop();
            }
        } else {
            Constant.status = 1;
        }
    }

    public void showPop() {
//        stat = 1;
//        popView = getActivity().getLayoutInflater().inflate(
//                R.layout.pop_city_select, null);
//
//        popupWindow = new PopupWindow(popView, ViewPager.LayoutParams.FILL_PARENT,
//                ViewPager.LayoutParams.FILL_PARENT);
//        // 使其聚焦
//        popupWindow.setFocusable(true);
//        // 设置不允许在外点击消失
//        popupWindow.setOutsideTouchable(false);
//        // 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
//        // popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        // 刷新状态
//        popupWindow.update();
//
//        Button btn_yes = (Button) popView.findViewById(R.id.btn_pop_city_yes);
//        Button btn_no = (Button) popView.findViewById(R.id.btn_pop_city_no);
//        TextView txt_remind = (TextView) popView
//                .findViewById(R.id.txt_pop_city_remind);
//        TextView shoudong = (TextView) popView
//                .findViewById(R.id.shoudong);
//        txt_remind.setText("系统定位到您在" + location.mData + ",需要切换至" + location.mData + "吗？");
//        shoudong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                location.setHandler(handler);
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), CityListsActivity.class);
//                getActivity().startActivity(intent);
//                popupWindow.dismiss();
//                Constant.status = 1;
//            }
//        });
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                cityID = CommonField.isOpen(location.mData).getCityID();
//                // 把城市id存起来用于后边内容显示
//                PreferencesUtils.putInt(getActivity(), "cityID", cityID);
//                PreferencesUtils.putString(getActivity(), "cityNamepl",
//                        CommonField.isOpen(location.mData).getCityNamepl());
//                areaid = PreferencesUtils.getInt(getActivity(), "cityID");
//
//                PreferencesUtils.putString(getActivity(), "cityName",
//                        location.mData);
//                city_name.setText(PreferencesUtils.getString(getActivity(),
//                        "cityName"));
//                Constant.status = 1;
//                areaid = PreferencesUtils.getInt(getActivity(), "cityID");
//
//                getBanner();
//                tody();
//                getHomeClass();
//                gerHotMall();
//                getSmlHot();
//                newsList();
//                popupWindow.dismiss();
//            }
//        });
//        btn_no.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();// 关闭popupWindow
//                Constant.status = 1;
//            }
//        });
//        try {
//            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
//                    Gravity.CENTER, 0, 0);// 显示在屏幕中间
//        } catch (Exception e) {
//
//        }


    }

    private List<CityModel> getjsonParse(String data) {
        if (CommonField.sourceDateList.size() > 0) {
            CommonField.sourceDateList.clear();// 二次进去防止数据多次加载
        }
        try {

            org.json.JSONObject jo = new org.json.JSONObject(data);
            org.json.JSONArray ja = jo.getJSONArray("list");
            for (int i = 0; i < data.length(); i++) {
                org.json.JSONObject jsonCity = ja.getJSONObject(i);
                CityModel city = new CityModel();
                city.setCityID(Integer.parseInt(jsonCity.getString("areaId")
                        .toString()));
                city.setCityName(jsonCity.getString("cityName"));
//				city.setCityName(jsonCity.getString("domain"));
//				System.out.println(jsonCity.getString("domain").substring(0,1));
//				city.setNameSort(PinYin2Abbreviation.getPinYinHeadChar(
//						jsonCity.getString("cityName")).substring(0, 1));
                city.setNameSort(jsonCity.getString("domain").substring(0, 1).toUpperCase());
                city.setCityNamepl(jsonCity.getString("domain"));
                CommonField.sourceDateList.add(city);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return CommonField.sourceDateList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
