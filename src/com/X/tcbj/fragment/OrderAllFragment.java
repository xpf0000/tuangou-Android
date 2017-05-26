package com.X.tcbj.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.CouponModel;
import com.X.model.NearbyModel;
import com.X.model.OrderModel;
import com.X.model.TuanModel;
import com.X.server.DataCache;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.UserRenzhengVC;
import com.X.tcbj.utils.XHorizontalBaseFragment;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class OrderAllFragment  extends XHorizontalBaseFragment {
    View view;

    SwipeRefreshLayout swipe_refresh;
    ListView mainList;

    OrderAdapter adapter;

    int page = 1;
    boolean end = false;

    OrderModel orderModel = new OrderModel();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        XNetUtil.APPPrintln("OrderAllFragment is onCreateView !!!!!!");

        if(view != null)
        {
            return view;
        }

        view = inflater.inflate(R.layout.order_fragment, container, false);

        swipe_refresh = (SwipeRefreshLayout)view.findViewById(R.id.order_fragment_refresh);
        mainList = (ListView) view.findViewById(R.id.order_fragment_list);

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        XNetUtil.APPPrintln("OrderAllFragment is onDestroy#############");
    }

    private void getData()
    {
        if(DataCache.getInstance().user == null)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(),LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            return;
        }
        else
        {
            if(DataCache.getInstance().user.getIs_effect() != 1)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(),UserRenzhengVC.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                return;
            }
        }

        String uid = DataCache.getInstance().user.getId()+"";

        XNetUtil.Handle(APPService.user_orderlist(uid, "1",page+""), new XNetUtil.OnHttpResult<OrderModel>() {
            @Override
            public void onError(Throwable e) {
                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(OrderModel model) {
                swipe_refresh.setRefreshing(false);
                if(model != null)
                {
                    if(page == 1)
                    {
                        orderModel.getItem().clear();
                    }

                    if(model.getItem() != null)
                    {
                        if(model.getItem().size() > 0)
                        {
                            page++;
                            orderModel.getItem().addAll(model.getItem());
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void lazyLoad() {
        System.out.println("OrderAllFragment--->lazyLoad !!!");

        adapter = new OrderAdapter(getActivity());

        if(mainList != null)
        {
            mainList.setAdapter(adapter);
        }

        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }





    class OrderAdapter extends BaseAdapter {
        Context context;

        public OrderAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return orderModel.getItem().size();
        }

        @Override
        public Object getItem(int position) {
            return orderModel.getItem().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            getItemView bundle ;

            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_cell, null);
                bundle = new getItemView();
                bundle.work=(TextView)convertView.findViewById(R.id.order_cell_name);
                convertView.setTag(bundle);
            }
            else
            {
                bundle = (getItemView) convertView.getTag();
            }

            //String str = DataCache.getInstance().searchKeys.getSearchKeys().get(position);

            //bundle.work.setText(str);

            return convertView;
        }

        private class getItemView {
            TextView work;
        }
    }



}