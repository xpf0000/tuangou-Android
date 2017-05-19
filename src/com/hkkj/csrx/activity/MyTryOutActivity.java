package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.MyTryOutAdapter;
import com.hkkj.csrx.domain.MyTryOut;
import com.hkkj.csrx.myview.SearchView;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.Constant;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgh on 2016/1/4.
 */
public class MyTryOutActivity extends Activity implements View.OnClickListener,SearchView.SearchViewListener{
    private SwipeRefreshLayout swipe_container;
    private ListView listview;

    private MyTryOutAdapter adapter;
    private List<MyTryOut.TryOut> tryOutList = new ArrayList<MyTryOut.TryOut>();
    private String userid;
    private int pageNow = 1;

    private int lastItem;
    private View footer;
    private ProgressBar listview_foot_progress;
    private TextView listview_foot_more;
    private int toalPage;

    private Button btn_search_try;
    private ImageView comment_back;
    View popView;
    PopupWindow popupWindow;
    private int search;
    private String searchString;
    /**
     * 搜索view
     */
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mytryout);
        userid = PreferencesUtils.getString(MyTryOutActivity.this, "userid");
        initView();
        loadData();
        refresher();
        loader();
        doEvent();
    }
    public void initView() {
        swipe_container=(SwipeRefreshLayout)findViewById(R.id.swipe_container_mytry);
        listview = (ListView)findViewById(R.id.myTryOutListview);
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        listview.setFooterDividersEnabled(false);
        adapter = new MyTryOutAdapter(MyTryOutActivity.this, tryOutList);
        listview.setAdapter(adapter);

        comment_back = (ImageView) findViewById(R.id.comment_back);
        comment_back.setOnClickListener(this);
        btn_search_try=(Button)findViewById(R.id.btn_search_try);
        btn_search_try.setOnClickListener(this);
    }
    public void refresher(){
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pageNow = 1;
                        loadData();
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                        swipe_container.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }

    //上拉加载
    public void loader() {
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (tryOutList.isEmpty()) {//数据为空
                    return;
                }
                //判断是否滚动到底部
                if (lastItem == adapter.getCount()) {

                    if (pageNow < toalPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listview_foot_progress.setVisibility(View.VISIBLE);
                                listview_foot_more.setVisibility(View.INVISIBLE);
                                pageNow++;
                                loadData();
                            }
                        }, 1000);
                    } else {
                        listview_foot_progress.setVisibility(View.INVISIBLE);
                        listview_foot_more.setVisibility(View.VISIBLE);
                        listview_foot_more.setText("已加载全部");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    public void doEvent() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                swipe_container.setRefreshing(false);
//                Log.e("state",tryOutList.get(i).getState()+"");
//                Toast.makeText(MyTryOutActivity.this, tryOutList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                int g=tryOutList.get(i).getFreeState();
                if (tryOutList.get(i).getFreeState() == 1) {
                    Intent intent = new Intent(MyTryOutActivity.this, Mianfei_jieshu.class);
                    intent.putExtra("ID", String.valueOf(tryOutList.get(i).getFreeID()));
                    startActivity(intent);
                } else if (tryOutList.get(i).getFreeState() == 2) {
                    Intent intent = new Intent(MyTryOutActivity.this, Mianfei_shenqing.class);
                    intent.putExtra("ID", String.valueOf(tryOutList.get(i).getFreeID()));
                    startActivity(intent);
                } else if (tryOutList.get(i).getFreeState() == 3) {
                    Intent intent = new Intent(MyTryOutActivity.this, Mianfei_tijiaobaogao.class);
                    intent.putExtra("ID", String.valueOf(tryOutList.get(i).getFreeID()));
                    startActivity(intent);
                } else if (tryOutList.get(i).getFreeState() == 4) {
                    Intent intent = new Intent(MyTryOutActivity.this, Mianfei_jijiangkaishi.class);
                    intent.putExtra("ID", String.valueOf(tryOutList.get(i).getFreeID()));
                    startActivity(intent);
                }
            }
        });
    }
    //解析数据
    public void loadData() {
        String url = Constant.url+"free/getAllUserFreeNew";
        RequestParams params = new RequestParams();
        params.put("userId",userid);
        params.put("page",pageNow);
        params.put("pageSize", 10);
        if (search==1){
            params.put("key",searchString);
        }
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyTryOut myTryOut = JSONObject.parseObject(s, MyTryOut.class);
////                Toast.makeText(MyTryOutActivity.this,myTryOut.getList().get(1).getTitle(),Toast.LENGTH_SHORT).show();
//
//                if (myTryOut.getList() != null) {
//                    if (pageNow == 1) {
//                        tryOutList.clear();
//                    }
//                    tryOutList.addAll(myTryOut.getList());
//                    adapter.notifyDataSetChanged();
//                }
//
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_back:
                MyTryOutActivity.this.finish();
                break;
            case R.id.btn_search_try:
                showSearch();
                break;
        }
    }
    public void showSearch(){
            popView = MyTryOutActivity.this.getLayoutInflater().inflate(R.layout.pop_search, null);
            popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.update();
            popupWindow.setAnimationStyle(R.style.PopupAnimation_right);
            popupWindow.showAtLocation(popView, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, getStatusBarHeight());
//            popupWindow.showAsDropDown(getWindow().findViewById(Window.FEATURE_OPTIONS_PANEL));
            searchView = (SearchView)popView.findViewById(R.id.searchview);
            //设置监听
            searchView.setSearchViewListener(this, handler);
    }

    //搜索框
    @Override
    public void onSearch(String text) {
        search=1;
        searchString=text;
        tryOutList.clear();
        loadData();
    }
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==2){
                popupWindow.dismiss();
            }
        }
    };
    private int getStatusBarHeight(){
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
