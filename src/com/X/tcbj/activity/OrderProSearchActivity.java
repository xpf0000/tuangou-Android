package com.X.tcbj.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MyOrderProductAdapter;
import com.X.tcbj.domain.MyOrderProduct;
import com.X.tcbj.myview.MyPopwindows;
import com.X.tcbj.myview.SearchView;
import com.X.tcbj.utils.AilupayApi;
import com.X.tcbj.utils.Constant;
import com.X.server.HKService;
import com.X.server.MD5Util;
import com.loopj.android.http.RequestParams;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/19.
 */
public class OrderProSearchActivity extends Activity implements SearchView.SearchViewListener {
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

    private int payType = 1;
    View popView;
    PopupWindow popupWindow;
    private String payurl, urlstr;
    MyPopwindows myPopwindows;
    AilupayApi ailupayApi;
    private String paynumbers, prices, validateMsg;
    private int orderId;

    private int search;
    private String searchString;

    private Button search_btn_back;
    private ImageView iv_back;
    /**
     * 搜索view
     */
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpro_search);
        userid = PreferencesUtils.getString(this, "userid");

        Intent intent = new Intent(this, HKService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(HKService.action);
        registerReceiver(broadcastReceiver, filter);
//

        initView();
        search = 2;
//        loadData();
        onSearch("");
        loadData();
        refresher();
        loader();
    }

    public void initView() {
        swipe_container_orderpro = (SwipeRefreshLayout) findViewById(R.id.swipe_container_orderpro);
        myListview = (ListView) findViewById(R.id.lv_order_product);

        //初始化底部视图
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        myListview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        myListview.setFooterDividersEnabled(false);
        adapter = new MyOrderProductAdapter(this, myList, handler);
        myListview.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.search_orderpro);
        //设置监听
        searchView.setSearchViewListener(this, handler);
        search_btn_back = (Button) findViewById(R.id.search_btn_back);
        search_btn_back.setVisibility(View.GONE);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        loadData();
                        swipe_container_orderpro.setRefreshing(false);
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                    }
                }, 1000);

            }
        });
    }

    //上拉加载
    public void loader() {
        myListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (myList.isEmpty()) {//数据为空
                    return;
                }
                //判断是否滚动到底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItem == adapter.getCount()) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pageNow < toalPage) {
                                listview_foot_progress.setVisibility(View.VISIBLE);
                                listview_foot_more.setVisibility(View.INVISIBLE);
                                pageNow++;
                                loadData();
                            } else {
                                listview_foot_progress.setVisibility(View.INVISIBLE);
                                listview_foot_more.setVisibility(View.VISIBLE);
                                listview_foot_more.setText("已加载全部");
                            }
                        }
                    }, 1000);

                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    public void doEvent() {
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(OrderProSearchActivity.this, myList.get(i).getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData() {
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("page", pageNow);
        params.put("pageSize", 10);
        if (search == 1) {
            params.put("key", searchString);
        }
        String url = Constant.url + "order/getAllUserOrders";
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
//                if (myOrderProductAll.getStatus() == -1) {
//                    adapter.getMyOrderProductListList().clear();
//                    adapter.notifyDataSetChanged();
//                }
//                if (myOrderProductAll.getStatus() == 0) {
//                    if (myOrderProductAll.getList().size() != 0) {
//                        if (pageNow == 1) {
//                            myList.clear();
//                        }
//                        adapter.setMyOrderProductListList(myOrderProductAll.getList());
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//            }
//        });
    }

    private void showpop() {
        popView=getLayoutInflater().inflate(R.layout.setprogramme, null);
        WindowManager windowManager = getWindowManager();
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
        map.put("title", "银联");
        items.add(map);
        SimpleAdapter romadapter = new SimpleAdapter(this, items,
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
                    payType = 1;
                } else {
                    payType = 2;
                }
                Intent intent = new Intent(OrderProSearchActivity.this, SuccesActivity.class);
                intent.putExtra("paynumber", paynumbers);
                intent.putExtra("price", prices);
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
                case 4:
                    int position=msg.getData().getInt("position");
                    adapter.getMyOrderProductListList().remove(position);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    prices = msg.getData().getString("prices");
                    paynumbers = msg.getData().getString("paynumbers");

                    orderId = msg.getData().getInt("orderId");
                    validateMsg = msg.getData().getString("validateMsg");
                    Log.i("........", prices + paynumbers);
                    showpop();
                    break;

                case 1:
                    popupWindow.dismiss();
                    break;
                case 2:
                    OrderProSearchActivity.this.finish();
                    break;
            }
        }
    };

    public void changeOrderState() {
        String url = Constant.url + "order/updOrderState";
        RequestParams params = new RequestParams();
        params.put("state", 6);
        params.put("id", orderId);
        params.put("userId", userid);
//                    Log.i("..md5....", myOrderProductListList.get(i).getID() + userName + "....." + MD5Util.MD5(validateMsg).toUpperCase());
        params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
        String ssss = url + "?state=6" + "id=" + orderId + "userId=" + userid + "validateMsg=" + MD5Util.MD5(validateMsg).toUpperCase();
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
                changeOrderState();

            }
        }
    };

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onSearch(String text) {
        search = 1;
        searchString = text;
        myList.clear();
        loadData();
    }
}
