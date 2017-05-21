package com.X.tcbj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.SuccesActivity;
import com.X.tcbj.adapter.MyOrderProductAdapter;
import com.X.tcbj.domain.MyOrderProduct;
import com.X.tcbj.myview.LoadingDialog;
import com.X.tcbj.myview.MyPopwindows;
import com.X.tcbj.utils.AilupayApi;
import com.X.tcbj.utils.Constant;
import com.X.server.MD5Util;
import com.loopj.android.http.RequestParams;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgh on 2016/1/7.
 */
public class NewsFragment extends Fragment{
    private View view;
    private MyOrderProductAdapter adapter;
    private ListView myListview;
    private SwipeRefreshLayout swipe_container_orderpro;
    private List<MyOrderProduct.MyOrderProductList> myList = new ArrayList<MyOrderProduct.MyOrderProductList>();
    private int pageNow = 1;
    private int lastItem;
    private View footer;
    private ProgressBar listview_foot_progress;
    private TextView listview_foot_more;
    private int toalPage;
    private String userid;

    private int payType=1;
    View popView;
    PopupWindow popupWindow;
    private String payurl,urlstr;
    MyPopwindows myPopwindows;
    AilupayApi ailupayApi;
    private String paynumbers,prices,validateMsg;
    private int orderId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderproduct, null);
        userid = PreferencesUtils.getString(getActivity(), "userid");

//        Intent intent = new Intent(getActivity(), HKService.class);
//        getActivity().startService(intent);
//        IntentFilter filter = new IntentFilter(HKService.action);
//        getActivity().registerReceiver(broadcastReceiver, filter);
        initView();
//        loadData(0);
        refresher();
//        doEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != adapter){
            loadPageData(0,10*pageNow);
        }
    }

    public void initView() {
        swipe_container_orderpro = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_orderpro);
        myListview = (ListView) view.findViewById(R.id.lv_order_product);

        //初始化底部视图
        footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        myListview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        myListview.setFooterDividersEnabled(false);
        adapter =  new MyOrderProductAdapter(getActivity(), myList,handler);
        myListview.setAdapter(adapter);
        myListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            pageNow++;
                            loadData(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void refresher() {
        swipe_container_orderpro.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNow = 1;
                        loadData(1);
                        swipe_container_orderpro.setRefreshing(false);
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                    }
                }, 1000);

            }
        });
    }

    //上拉加载


    public void doEvent() {
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), myList.get(i).getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(final int type) {
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("page", pageNow);
        params.put("pageSize", 10);
        params.put("state", Constant.ordertype);
        params.put("key", "");
        String url = Constant.url+"order/GetUserAllOrders";
        System.out.println(url+"userId="+userid+"page="+pageNow+"pageSize=10");
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyOrderProduct myOrderProductAll = JSONObject.parseObject(s, MyOrderProduct.class);
////                List<MyOrderProduct.MyOrderProductList.OrderProducts> listSon=new ArrayList<MyOrderProduct.MyOrderProductList.OrderProducts>();
//                toalPage = myOrderProductAll.getTotalPage();
//                if (myOrderProductAll.getStatus() == 0) {
//                    if (myOrderProductAll.getList().size() != 0) {
//                        if (type==1){
//                           myList.clear();
//                        }
//                        myList.addAll(myOrderProductAll.getList());
////                        adapter.setMyOrderProductListList(myOrderProductAll.getList());
//
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    private void showpop() {
        if(null == getActivity()){
            return;
        }
        popView = getActivity().getLayoutInflater().inflate(
                R.layout.setprogramme, null);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        ListView addprolist = (ListView) popView.findViewById(R.id.programlist);
        Button canle = (Button) popView.findViewById(R.id.canle);
        final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        map = new HashMap<String, String>();
        map.put("title", "支付宝");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("title", "微信");
        items.add(map);
        SimpleAdapter romadapter = new SimpleAdapter(getActivity(), items,
                R.layout.programtext, new String[]{"title"},
                new int[]{R.id.programomtxt});
        addprolist.setAdapter(romadapter);
        canle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        addprolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    payType = 1;//支付宝
                } else {
                    payType = 4;//微信支付
                }
                Intent intent =new Intent(getActivity(), SuccesActivity.class);
                intent.putExtra("paynumber",paynumbers);
                intent.putExtra("price",prices);
                intent.putExtra("paytype", payType);
                startActivity(intent);

                popupWindow.dismiss();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    popupWindow.dismiss();
                    break;
                case 3:
                    prices=msg.getData().getString("prices");
                    paynumbers=msg.getData().getString("paynumbers");

                    orderId=msg.getData().getInt("orderId");
                    validateMsg=msg.getData().getString("validateMsg");
                    Log.i("........",prices+paynumbers);
                    int pay=msg.getData().getInt("paytype");
                    Intent intent =new Intent(getActivity(), SuccesActivity.class);
                    intent.putExtra("paynumber",paynumbers);
                    intent.putExtra("price",prices);
                    intent.putExtra("paytype", pay);
                    startActivity(intent);
//                    showpop();
//                    payType=1;
//                    Intent intent =new Intent(getActivity(), SuccesActivity.class);
//                    intent.putExtra("paynumber",paynumbers);
//                    intent.putExtra("price",prices);
//                    intent.putExtra("paytype", payType);
//                    startActivity(intent);
                    break;
                case 4://删除
                    int position=msg.getData().getInt("position");
                    adapter.getMyOrderProductListList().remove(position);
                    adapter.notifyDataSetChanged();
                    LoadingDialog.closeDialog();
                    adapter.notifyDataSetChanged();
                    break;
                case 5://取消

//                    pageNow=1;
//                    loadData(2);
//                    listview_foot_more.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getActivity(), "取消订单", Toast.LENGTH_SHORT).show();
                    int position_cancel=msg.getData().getInt("position");
                    loadPageData(position_cancel,((position_cancel/10)+1)*10);
                    LoadingDialog.closeDialog();
                    break;
                case 6:
                    if(null != adapter){
                        loadPageData(0,10*pageNow);
                    }
                    break;
            }
        }
    };
    public void loadPageData(final int position,final int pageSize) {
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("page", 1);
        params.put("pageSize", pageSize);
        params.put("state", Constant.ordertype);
        params.put("key", "");
        String url = Constant.url+"order/GetUserAllOrders";
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyOrderProduct myOrderProductAll = JSONObject.parseObject(s, MyOrderProduct.class);
////                List<MyOrderProduct.MyOrderProductList.OrderProducts> listSon=new ArrayList<MyOrderProduct.MyOrderProductList.OrderProducts>();
//                toalPage = myOrderProductAll.getTotalPage();
//                if (myOrderProductAll.getStatus() == 0) {
//                    if (myOrderProductAll.getList().size() != 0) {
////                        adapter.getMyOrderProductListList().clear();
////                        myList.addAll(myOrderProductAll.getList());
//                        myList.clear();
//                        myList.addAll(myOrderProductAll.getList());
////                        adapter.setMyOrderProductListList(myOrderProductAll.getList());
//                        adapter.notifyDataSetChanged();
////                        myListview.setSelection(position);
//                    }
//                }else {
//                    myList.clear();
//                    adapter.notifyDataSetChanged();
//                    listview_foot_more.setText("已加载全部");
//                    pageNow--;
//                }
//
//            }
//        });
    }
    public void changeOrderState(){
        String url=Constant.url+"order/updOrderState";
        RequestParams params=new RequestParams();
        params.put("state",6);
        params.put("id",orderId);
        params.put("userId", userid);
//                    Log.i("..md5....", myOrderProductListList.get(i).getID() + userName + "....." + MD5Util.MD5(validateMsg).toUpperCase());
        params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
////                Toast.makeText(getActivity(), "确认付款", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getStringExtra("referch");
            if (a.equals("4")) {
//                changeOrderState();

            }else if (a.equals("4")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pageNow = 1;
                        loadData(1);
                        swipe_container_orderpro.setRefreshing(false);
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                    }
                }, 1000);
            }
        }
    };
    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
