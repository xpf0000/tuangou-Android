package com.X.tcbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.NearbyModel;
import com.X.model.StoresListModel;
import com.X.model.StoresModel;
import com.X.model.TuanCateModel;
import com.X.model.TuanModel;
import com.X.model.TuanNavModel;
import com.X.model.TuanQuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.tcbj.adapter.StoresAdapter;
import com.X.tcbj.adapter.TuanAdapter;
import com.X.tcbj.myview.TuanCatePop;
import com.X.tcbj.myview.TuanNavPop;
import com.X.tcbj.myview.TuanQuanPop;
import com.X.tcbj.utils.XHtmlVC;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class TuanListVC extends BaseActivity {

    TextView allqu, allmall, allsort,title;
    ListView prilist;
    SwipeRefreshLayout swipe_refresh;

    TuanQuanPop myQuanPop;
    TuanCatePop myCatePop;
    TuanNavPop myNavPop;

    LinearLayout layout;

    int page = 1;
    boolean end = false;

    List<TuanQuanModel> quanList = new ArrayList<>();
    List<TuanCateModel> cateList = new ArrayList<>();
    List<TuanNavModel> navList = new ArrayList<>();

    List<TuanModel> tuanList = new ArrayList<>();

    private TuanAdapter adapter;


    private TuanQuanModel.QuanSubBean nowQuan = new TuanQuanModel.QuanSubBean();
    private TuanCateModel.BcateTypeBean nowCate = new TuanCateModel.BcateTypeBean();
    private TuanNavModel nowOrder = new TuanNavModel();

    String cate_id="0";

    @Override
    protected void setupUi() {
        setContentView(R.layout.stores_list);

        String id = getIntent().getStringExtra("cate_id");
        if(id != null) cate_id = id;

        nowCate.setCate_id(Integer.parseInt(cate_id));
        nowCate.setId(0);

        intview();
        setOrderList();
        setPrilist();
        getclass();
        getPrilist();
        getarea();

    }

    @Override
    protected void setupData() {

    }

    public void to_seach(View v)
    {
        pushVC(HotSearch.class);
    }

    private void intview()
    {
        myQuanPop = new TuanQuanPop();
        myCatePop = new TuanCatePop();
        myNavPop = new TuanNavPop();

        myCatePop.big_id = cate_id;

        layout = (LinearLayout)findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);

        title = (TextView) findViewById(R.id.title);
        title.setText("团购列表");

        allqu = (TextView) findViewById(R.id.allqu);
        allqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuanpop();
            }
        });


        allmall = (TextView) findViewById(R.id.allmall);
        allmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCatepop();
            }
        });


        allsort = (TextView) findViewById(R.id.allsort);
        allsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavPop();
            }
        });


        prilist = (ListView) findViewById(R.id.prilist);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
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

                to_info(position);

            }
        });


        myCatePop.setMyCateChooseListener(new TuanCatePop.MyCateChooseListener() {
            @Override
            public void onChoose(TuanCateModel.BcateTypeBean item) {
                nowCate = item;

                if(item.getName().equals("全部"))
                {
                    allmall.setText(cateList.get(myCatePop.positions).getName());
                }
                else
                {
                    allmall.setText(item.getName());
                }

                myCatePop.popupWindow.dismiss();
                page = 1;
                end = false;
                getPrilist();
            }
        });


    }




    private void to_info(int p)
    {
        Bundle bundle = new Bundle();

        bundle.putString("url","http://www.tcbjpt.com/wap/index.php?ctl=deal&" +
                "act=app_index&data_id="+tuanList.get(p).getId()+
                "&city_id="+DataCache.getInstance().nowCity.getId());
        bundle.putString("id",tuanList.get(p).getId());
        bundle.putSerializable("tuanModel",tuanList.get(p));
        bundle.putBoolean("hideNavBar",true);

        pushVC(XHtmlVC.class,bundle);

    }



    /**
     * 筛选条件监听
     */
    private void setQuanpop() {

        myQuanPop.showclasspop(quanList, this, layout);
        myQuanPop.setMyQuanChooseListener(new TuanQuanPop.MyQuanChooseListener() {
            @Override
            public void onChoose(TuanQuanModel.QuanSubBean item) {
                nowQuan = item;

                if(item.getName().equals("全部"))
                {
                    allqu.setText(quanList.get(myQuanPop.positions).getName());
                }
                else
                {
                    allqu.setText(item.getName());
                }

                myQuanPop.popupWindow.dismiss();
                page = 1;
                end = false;
                getPrilist();
            }
        });

    }

    private void setCatepop() {
        myCatePop.showclasspop(cateList, this, layout);
    }


    private void setNavPop() {

        myNavPop.showclasspop(navList, this, layout);

        myNavPop.setMyNavChooseListener(new TuanNavPop.MyNavChooseListener() {
            @Override
            public void onChoose(TuanNavModel item) {
                nowOrder = item;
                allsort.setText(item.getName());
                myNavPop.popupWindow.dismiss();
                page = 1;
                end = false;
                getPrilist();
            }
        });


    }


    private void getPrilist()
    {
        String city_id = DataCache.getInstance().nowCity.getId();

        double x = 0.0,y=0.0;

        if(XPostion.getInstance().getPostion() != null)
        {
            x = XPostion.getInstance().getPostion().getLongitude();
            y = XPostion.getInstance().getPostion().getLatitude();
        }

        XNetUtil.Handle(APPService.tuan_index(page+"", city_id,nowCate.getCate_id()+"",nowCate.getId()+"",nowQuan.getId()+"",nowOrder.getCode(), x, y), new XNetUtil.OnHttpResult<NearbyModel>() {
            @Override
            public void onError(Throwable e) {
                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(NearbyModel model) {
                swipe_refresh.setRefreshing(false);
                if(model != null)
                {
                    List<TuanModel> list = model.getItem();

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

                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

    private void setPrilist()
    {
        adapter=new TuanAdapter(tuanList,this);
        prilist.setAdapter(adapter);

    }


    private void setOrderList()
    {
        TuanNavModel m = new TuanNavModel();
        m.setName("默认");
        m.setCode("default");

        navList.add(m);

        TuanNavModel m1 = new TuanNavModel();
        m1.setName("好评");
        m1.setCode("avg_point");

        navList.add(m1);

        TuanNavModel m2 = new TuanNavModel();
        m2.setName("最新");
        m2.setCode("newest");

        navList.add(m2);

    }


    private void getarea() {

        String city_id = DataCache.getInstance().nowCity.getId();
        XNetUtil.Handle(APPService.tuan_quan_list(city_id), new XNetUtil.OnHttpResult<List<TuanQuanModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<TuanQuanModel> arrs) {
                if(arrs != null)
                {
                    quanList = arrs;
                }
            }
        });

    }


    private void getclass() {

        XNetUtil.Handle(APPService.tuan_cate_list(), new XNetUtil.OnHttpResult<List<TuanCateModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<TuanCateModel> arrs) {
                if(arrs != null)
                {
                    cateList = arrs;

                    for(int i=0;i<arrs.size();i++)
                    {
                        TuanCateModel m = arrs.get(i);

                        if(cate_id.equals(m.getId()+""))
                        {
                            allmall.setText(m.getName());
                        }

                    }

                }
            }
        });

    }

}
