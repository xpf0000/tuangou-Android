package com.X.tcbj.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.X.model.NewsModel;
import com.X.model.NoticeModel;
import com.X.server.BaseActivity;
import com.X.tcbj.adapter.NoticeAdapter;
import com.X.tcbj.utils.XHtmlVC;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class NoticeListVC extends BaseActivity {

    ListView prilist;

    SwipeRefreshLayout swipe_refresh;

    int page = 1;
    boolean end = false;
    List<NewsModel> tuanList = new ArrayList<>();

    private NoticeAdapter adapter;

    @Override
    protected void setupUi() {
        setContentView(R.layout.notice_list);

        intview();
        setPrilist();
        getPrilist();

    }

    @Override
    protected void setupData() {

    }

    private void intview()
    {

        prilist = (ListView) findViewById(R.id.notice_list_listview);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.notice_list_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                end = false;
                getPrilist();
            }
        });

        prilist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if(!end)
                            {
                                getPrilist();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        prilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XNetUtil.APPPrintln("prilist.setOnItemClickListener !!!!!!!!!!!!!!");
                to_info(position);
            }
        });


    }

    private void to_info(int p)
    {
        String data_id = tuanList.get(p).getId();
        Bundle bundle = new Bundle();
        bundle.putString("url","http://tg01.sssvip.net/wap/index.php?ctl=notice&act=app_index&data_id="+data_id);
        bundle.putString("title","详情");
        pushVC(XHtmlVC.class,bundle);
    }

    private void getPrilist()
    {

        XNetUtil.Handle(APPService.notice_list(page+""), new XNetUtil.OnHttpResult<NoticeModel>() {
            @Override
            public void onError(Throwable e) {
                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(NoticeModel model) {
                swipe_refresh.setRefreshing(false);
                if(model != null)
                {
                    List<NewsModel> list = model.getList();

                    if(page == 1)
                    {
                        tuanList.clear();
                    }

                    if(list != null)
                    {
                        if(list.size() > 0)
                        {
                            page++;
                            tuanList.addAll(list);
                        }
                        else
                        {
                            end = true;
                            XActivityindicator.showToast("已全部加载完毕！");
                        }
                    }

                    adapter.setList(tuanList);
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

    private void setPrilist()
    {
        adapter=new NoticeAdapter(this,tuanList);
        prilist.setAdapter(adapter);
    }


}