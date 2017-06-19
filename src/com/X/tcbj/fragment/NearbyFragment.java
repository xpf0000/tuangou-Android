package com.X.tcbj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.NearbyModel;
import com.X.model.TuanCateModel;
import com.X.model.TuanModel;
import com.X.model.TuanNavModel;
import com.X.model.TuanQuanModel;
import com.X.server.DataCache;
import com.X.tcbj.activity.HotSearch;
import com.X.tcbj.activity.R;
import com.X.tcbj.adapter.TuanAdapter;
import com.X.tcbj.myview.TuanCatePop;
import com.X.tcbj.myview.TuanNavPop;
import com.X.tcbj.myview.TuanQuanPop;
import com.X.tcbj.utils.XHtmlVC;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class NearbyFragment extends Fragment
{
    private  View view;
    TextView allqu, allmall, allsort;
    ListView prilist;
    SwipeRefreshLayout swipe_refresh;

    TuanQuanPop myQuanPop;
    TuanCatePop myCatePop;
    TuanNavPop myNavPop;

    LinearLayout layout;
    ImageView search;

    int page = 1;
    boolean end = false;

    List<TuanQuanModel> quanList = new ArrayList<>();
    List<TuanCateModel> cateList = new ArrayList<>();
    List<TuanNavModel> navList = new ArrayList<>();

    List<TuanModel> tuanList = new ArrayList<>();

    private TuanAdapter tuanApater;


    private TuanQuanModel.QuanSubBean nowQuan = new TuanQuanModel.QuanSubBean();
    private TuanCateModel.BcateTypeBean nowCate = new TuanCateModel.BcateTypeBean();
    private TuanNavModel nowOrder = new TuanNavModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.shangjia, container, false);

        intview();
        setOrderList();
        setPrilist();
        getclass();
        getPrilist();
        getarea();

        return view;
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

                    tuanApater.notifyDataSetChanged();

                }
            }
        });

    }
    /**
     * 获取区域数据
     */
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

    private void setPrilist()
    {
        tuanApater=new TuanAdapter(tuanList,getActivity());
        prilist.setAdapter(tuanApater);

    }

    private void setOrderList()
    {
        XNetUtil.Handle(APPService.tuan_nav_list(), new XNetUtil.OnHttpResult<List<TuanNavModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<TuanNavModel> arrs) {
                if(arrs != null)
                {
                    navList = arrs;
                }
            }
        });

    }

    private void intview()
    {
        myQuanPop = new TuanQuanPop();
        myCatePop = new TuanCatePop();
        myNavPop = new TuanNavPop();

        layout = (LinearLayout)view.findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);
        search = (ImageView) view.findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), HotSearch.class);
                getActivity().startActivity(intent);
            }
        });

        allqu = (TextView) view.findViewById(R.id.allqu);
        allqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuanpop();
            }
        });


        allmall = (TextView) view.findViewById(R.id.allmall);
        allmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCatepop();
            }
        });


        allsort = (TextView) view.findViewById(R.id.allsort);
        allsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavPop();
            }
        });


        prilist = (ListView) view.findViewById(R.id.prilist);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
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

    }

    private void to_info(int p)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), XHtmlVC.class);
        intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=deal&" +
                "act=app_index&data_id="+tuanList.get(p).getId()+
                "&city_id="+DataCache.getInstance().nowCity.getId());
        intent.putExtra("id",tuanList.get(p).getId());
        intent.putExtra("tuanModel",tuanList.get(p));
        intent.putExtra("hideNavBar",true);
        getActivity().startActivity(intent);
    }


    /**
     * 获取分类数据
     */
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
                }
            }
        });

    }


    /**
     * 筛选条件监听
     */
    private void setQuanpop() {

        myQuanPop.showclasspop(quanList, getActivity(), layout);
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

        myCatePop.showclasspop(cateList, getActivity(), layout);
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


    private void setNavPop() {

        myNavPop.showclasspop(navList, getActivity(), layout);

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

}
