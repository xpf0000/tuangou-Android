package com.hkkj.csrx.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.LoginActivity;
import com.hkkj.csrx.activity.MallInfo;
import com.hkkj.csrx.activity.MyAddressActivity;
import com.hkkj.csrx.activity.MyCollect;
import com.hkkj.csrx.activity.MyCollectActivity;
import com.hkkj.csrx.activity.MyOrderActivity;
import com.hkkj.csrx.activity.MyTryOutActivity;
import com.hkkj.csrx.activity.Myinfo;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.activity.Zhanghaoguanl;
import com.hkkj.csrx.adapter.HomeAdapter;
import com.hkkj.csrx.domain.Count;
import com.hkkj.csrx.domain.UserInfo;
import com.hkkj.csrx.myview.HeaderGridView;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.CircleImageView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.ImageUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的空间
 *
 * @author zpp
 * @version1.0
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view, headview;
    HeaderGridView homeGid;
    HomeAdapter homeAdapter;
    CircleImageView user_head;
    RelativeLayout nomoney, nodeliver, noreceipt, nocomment, returngood;
    TextView nomoneycount, nodelivercount, receiptcount, nocommentcount, returngoodcount, number, userinfo;
    LinearLayout myredpaper, mycollect, mycomment, mytry, myaddress, order, loginmore;
    ArrayList<HashMap<String, String>> hotmallarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    int page = 1;
    int a = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        intview();
        setHomeGid();
        getYoulike();
        return view;
    }

    private void intview() {
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.myhead, null);
        order = (LinearLayout) headview.findViewById(R.id.order);
        order.setOnClickListener(this);
        user_head = (CircleImageView) headview.findViewById(R.id.user_head);
        userinfo = (TextView) headview.findViewById(R.id.userinfo);
//        userinfo.setOnClickListener(this);
        loginmore = (LinearLayout) headview.findViewById(R.id.loginmore);
        loginmore.setOnClickListener(this);
        nomoney = (RelativeLayout) headview.findViewById(R.id.nomoney);
        nomoney.setOnClickListener(this);
        nodeliver = (RelativeLayout) headview.findViewById(R.id.nodeliver);
        nodeliver.setOnClickListener(this);
        noreceipt = (RelativeLayout) headview.findViewById(R.id.noreceipt);
        noreceipt.setOnClickListener(this);
        nocomment = (RelativeLayout) headview.findViewById(R.id.nocomment);
        nocomment.setOnClickListener(this);
        returngood = (RelativeLayout) headview.findViewById(R.id.returngood);
        returngood.setOnClickListener(this);
        number = (TextView) headview.findViewById(R.id.number);
        nomoneycount = (TextView) headview.findViewById(R.id.nomoneycount);
        nodelivercount = (TextView) headview.findViewById(R.id.nodelivercount);
        receiptcount = (TextView) headview.findViewById(R.id.receiptcount);
        nocommentcount = (TextView) headview.findViewById(R.id.nocommentcount);
        returngoodcount = (TextView) headview.findViewById(R.id.returngoodcount);
        myredpaper = (LinearLayout) headview.findViewById(R.id.myredpaper);
        myredpaper.setOnClickListener(this);
        mycollect = (LinearLayout) headview.findViewById(R.id.mycollect);
        mycollect.setOnClickListener(this);
        mycomment = (LinearLayout) headview.findViewById(R.id.mycomment);
        mycomment.setOnClickListener(this);
        mytry = (LinearLayout) headview.findViewById(R.id.mytry);
        mytry.setOnClickListener(this);
        myaddress = (LinearLayout) headview.findViewById(R.id.myaddress);
        myaddress.setOnClickListener(this);
        homeGid = (HeaderGridView) view.findViewById(R.id.homeGid);
        homeGid.addHeaderView(headview, null, false);
        int Logn = PreferencesUtils.getInt(getActivity(), "logn");
        if (Logn != 0) {
            getCount();
            getUserinfo();
        }
        homeGid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (a == 0) {
                                a = 1;
                                page++;
                                getYoulike();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        homeGid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", hotmallarray.get(position - homeGid.getHeaderViewCount() - 1).get("ID"));
                intent.setClass(getActivity(), MallInfo.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setHomeGid() {
        //homeAdapter = new HomeAdapter(hotmallarray, getActivity());
        //homeGid.setAdapter(homeAdapter);
    }

    @Override
    public void onClick(View v) {
        int Logn = PreferencesUtils.getInt(getActivity(), "logn");
        Intent intent = new Intent();
        if (Logn == 0) {
            intent.setClass(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
        } else {
            switch (v.getId()) {
                case R.id.order:
                    Constant.ordertype = 0;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.nomoney:
                    Constant.ordertype = 1;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.nodeliver:
                    Constant.ordertype = 2;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.noreceipt:
                    Constant.ordertype = 3;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.nocomment:
                    Constant.ordertype = 4;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.returngood:
                    Constant.ordertype = 11;
                    intent.setClass(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.myredpaper:
                    intent.setClass(getActivity(), Zhanghaoguanl.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.mycollect:
                    intent.setClass(getActivity(), MyCollectActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.mycomment:
                    intent.setClass(getActivity(), MyCollect.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.mytry:
                    intent.setClass(getActivity(), MyTryOutActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.myaddress:
                    intent.setClass(getActivity(), MyAddressActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.loginmore:
                    intent.setClass(getActivity(), Myinfo.class);
                    getActivity().startActivity(intent);
                    break;
            }
        }
    }

    private void getCount() {
        String userid = PreferencesUtils.getString(getActivity(), "userid");
        String url = Constant.url + "order/GetUserAllOrdersStateCount/" + userid;
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                Count count = JSON.parseObject(s, Count.class);
//                if (count.getStatus() == 0) {
//                    nomoneycount.setText(count.getMap().getWaitPay() + "");
//                    nodelivercount.setText(count.getMap().getWaitSend() + "");
//                    receiptcount.setText(count.getMap().getWaitRecevie() + "");
//                    nocommentcount.setText(count.getMap().getWaitComment() + "");
//                    returngoodcount.setText(count.getMap().getRefundAndCs() + "");
//                }
//            }
//        });
    }

    private void getUserinfo() {
        String userid = PreferencesUtils.getString(getActivity(), "userid");
        String url = Constant.url + "userinfo/getUserInfoByUserId/" + userid;
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                UserInfo userInfo = JSON.parseObject(s, UserInfo.class);
//                if (userInfo.getStatus() == 0) {
//                    if (userInfo.getUserInfo().getNickName().trim().length() > 0) {
//                        number.setText(userInfo.getUserInfo().getNickName());
//                    } else {
//                        if (userInfo.getUserInfo().getUserName().trim().length() > 0) {
//                            number.setText(userInfo.getUserInfo().getUserName());
//                        } else {
//                            if (userInfo.getUserInfo().getPhone().trim().length() > 0) {
//                                number.setText(userInfo.getUserInfo().getPhone());
//                            } else {
//                                number.setText(userInfo.getUserInfo().getEmail());
//                            }
//                        }
//                    }
//
//                    userinfo.setText("个人信息>");
//                    userinfo.setVisibility(View.VISIBLE);
//                    options = ImageUtils.setcenterOptions();
//                    if (userInfo.getUserInfo().getPicUrl() == null || userInfo.getUserInfo().getPicUrl().trim().isEmpty()) {
//                        user_head.setImageResource(R.drawable.user_face);
//                    } else {
//                        ImageLoader.displayImage(userInfo.getUserInfo().getPicUrl(), user_head, options, animateFirstListener);
//                    }
//                    Drawable nav_up;
//                    switch (userInfo.getUserInfo().getLevel()) {
//                        case "LV1":
//                            nav_up = getResources().getDrawable(R.drawable.level1);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV2":
//                            nav_up = getResources().getDrawable(R.drawable.level2);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//
//                            break;
//                        case "LV3":
//                            nav_up = getResources().getDrawable(R.drawable.level3);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//
//                            break;
//                        case "LV4":
//                            nav_up = getResources().getDrawable(R.drawable.level4);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//
//                            break;
//                        case "LV5":
//                            nav_up = getResources().getDrawable(R.drawable.level5);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV6":
//                            nav_up = getResources().getDrawable(R.drawable.level6);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV7":
//                            nav_up = getResources().getDrawable(R.drawable.level7);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV8":
//                            nav_up = getResources().getDrawable(R.drawable.level8);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV9":
//                            nav_up = getResources().getDrawable(R.drawable.level9);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                        case "LV0":
//                            nav_up = getResources().getDrawable(R.drawable.level);
//                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                            number.setCompoundDrawables(null, null, nav_up, null);
//                            break;
//                    }
//                }
//            }
//        });
    }

    private void getYoulike() {
        int areaid = PreferencesUtils.getInt(getActivity(), "cityID");
        String url = Constant.url + "shop/guessYouLike/" + page + "/10/" + areaid;
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                a = 0;
//                if (page != 1) {
//                    page--;
//                }
//            }
//
//            @Override
//            public void onSuccess(int x, Header[] headers, String s) {
//                JSONObject jsonObject = JSON.parseObject(s);
//                int a = jsonObject.getIntValue("status");
//                if (a == 0) {
////                    hotmallarray.clear();
//                    JSONArray jsonArray = jsonObject.getJSONArray("list");
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        hashMap = new HashMap<String, String>();
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
//                        hashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
//                        hashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
//                        hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
//                        hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
//                        hashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
//                        hashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
//                        hotmallarray.add(hashMap);
//                    }
//                    homeAdapter.notifyDataSetChanged();
//                    a = 0;
//                } else {
//                    a = 0;
//                    if (page != 1) {
//                        page--;
//                    }
//                    Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int Logn = PreferencesUtils.getInt(getActivity(), "logn");
        if (Logn != 0) {
            getCount();
            getUserinfo();
        } else {
            user_head.setImageResource(R.drawable.user_face);
            number.setText("请登录");
            userinfo.setVisibility(View.GONE);
            nomoneycount.setText("0");
            nodelivercount.setText("0");
            receiptcount.setText("0");
            nocommentcount.setText("0");
            returngoodcount.setText("0");
            number.setCompoundDrawables(null, null, null, null);
        }
    }
}
