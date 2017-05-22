package com.X.tcbj.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.X.model.UserCollectModel;
import com.X.server.DataCache;
import com.X.tcbj.adapter.MyCollectProductAdapter;
import com.X.xnet.XNetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by lgh on 2016/1/4.
 */
public class MyCollectActivity extends Activity {

    private SwipeRefreshLayout swipe_container;
    private ListView listview;
    private MyCollectProductAdapter adapter;
    private List<UserCollectModel.GoodsListBean> productList = new ArrayList<>();

    private View footer;
    private ProgressBar listview_foot_progress;
    private TextView listview_foot_more;

    private int pageNow = 1;
    private int lastItem;
    private int toalPage;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mycollect);


        initview();
        loadData();
    }
    public void initview(){

        swipe_container=(SwipeRefreshLayout)findViewById(R.id.swipe_container);
        listview=(ListView)findViewById(R.id.mycollect_list);


        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNow = 1;
                        loadData();
                        listview_foot_more.setVisibility(View.INVISIBLE);
                        swipe_container.setRefreshing(false);
                    }
                }, 1000);

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                XNetUtil.APPPrintln("position: "+position);

            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (productList.isEmpty()) {//数据为空
                    return;
                }
                //判断是否滚动到底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItem == adapter.getCount()) {
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

        //初始化底部视图
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        listview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        listview.setFooterDividersEnabled(false);

        adapter=new MyCollectProductAdapter(productList,this);

        listview.setAdapter(adapter);




    }


    public void loadData(){

        String uid = DataCache.getInstance().user.getId()+"";
        XNetUtil.Handle(APPService.user_collectlist(uid, pageNow + ""), new XNetUtil.OnHttpResult<UserCollectModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(UserCollectModel model) {

                toalPage = model.getPage().getPage_total();
                if (pageNow==1){
                        productList.clear();
                }

                productList.addAll(model.getGoods_list());
                adapter.notifyDataSetChanged();

            }
        });


    }

    public void back(View v)
    {
        finish();
    }
}
