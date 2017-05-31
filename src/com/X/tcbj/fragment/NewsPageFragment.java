package com.X.tcbj.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.NewsModel;
import com.X.server.DataCache;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.XHorizontalBaseFragment;
import com.X.tcbj.utils.XHtmlVC;
import com.X.tcbj.utils.XPostion;
import com.X.tcbj.xinterface.XRecyclerViewItemClick;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class NewsPageFragment extends XHorizontalBaseFragment {
    View view;

    SwipeRefreshLayout swipe_refresh;
    ListView mainList;

    NewsPageAdapter adapter;

    int page = 1;
    boolean end = false;
    String id = "0";

    List<NewsModel> list = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = (String) getArguments().get("id");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        XNetUtil.APPPrintln("OrderAllFragment is onCreateView !!!!!!");

        if(view != null)
        {
            return view;
        }

        view = inflater.inflate(R.layout.news_page_fragment, container, false);

        swipe_refresh = (SwipeRefreshLayout)view.findViewById(R.id.news_fragment_refresh);
        mainList = (ListView) view.findViewById(R.id.news_fragment_list);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                end = false;
                getData();
            }
        });


        mainList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if(!end)
                            {
                                getData();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return view;
    }


    private void to_info(int p)
    {
        String id = list.get(p).getId();

        String url = "http://tg01.sssvip.net/wap/index.php?ctl=news&act=app_newinfo&id="+id;

        Intent intent = new Intent();
        intent.setClass(getActivity(), XHtmlVC.class);
        intent.putExtra("url",url);
        intent.putExtra("title","详情");
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        XNetUtil.APPPrintln("OrderAllFragment is onDestroy#############");
    }

    private void getData()
    {

        XNetUtil.Handle(APPService.news_list(id,page+""), new XNetUtil.OnHttpResult<List<NewsModel>>() {
            @Override
            public void onError(Throwable e) {
                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NewsModel> arrs) {
                swipe_refresh.setRefreshing(false);
                if(arrs != null)
                {
                    if(page == 1)
                    {
                        list.clear();
                    }

                    if(arrs.size() > 0)
                    {
                        page++;
                        list.addAll(arrs);
                    }
                    else
                    {
                        end = true;
                        XActivityindicator.showToast("已全部加载完毕！");
                    }

                    if(adapter != null)
                    {
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void lazyLoad() {
        System.out.println("OrderAllFragment--->lazyLoad !!!");

        adapter = new NewsPageAdapter(getActivity());

        adapter.setOnItemClick(new XRecyclerViewItemClick() {
            @Override
            public void ItemClickListener(View view, int postion) {
                to_info(postion);
            }
        });

        if(mainList != null)
        {
            mainList.setAdapter(adapter);

//            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    to_info(position);
//
//                }
//            });
        }

        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    class NewsPageAdapter extends BaseAdapter {
        Context context;

        XRecyclerViewItemClick itemClick;

        public void setOnItemClick(XRecyclerViewItemClick a)
        {
            itemClick = a;
        }

        public NewsPageAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            getItemView bundle ;

            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.news_cell, null);
                bundle = new getItemView();
                bundle.name=(TextView)convertView.findViewById(R.id.news_cell_title);
                bundle.sub_name=(TextView)convertView.findViewById(R.id.news_cell_subtitle);

                convertView.setTag(bundle);
            }
            else
            {
                bundle = (getItemView) convertView.getTag();
            }

            NewsModel item = list.get(position);

            bundle.name.setText(item.getTitle());
            bundle.sub_name.setText(item.getCreate_time());

            if(itemClick != null)
            {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        itemClick.ItemClickListener(null,position);
                    }
                });
            }

            return convertView;
        }

        private class getItemView {
            TextView name,sub_name;
        }
    }



}