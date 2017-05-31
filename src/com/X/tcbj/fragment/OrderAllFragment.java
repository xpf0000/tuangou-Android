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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.OrderItemModel;
import com.X.model.OrderModel;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.activity.CommentSubmitVC;
import com.X.tcbj.activity.CouponCodeVC;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.UCOrderPayVC;
import com.X.tcbj.activity.UserRenzhengVC;
import com.X.tcbj.utils.XHorizontalBaseFragment;
import com.X.tcbj.utils.XHtmlVC;
import com.X.tcbj.utils.XPostion;
import com.X.tcbj.xinterface.XRecyclerViewItemClick;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    String status = "0";

    OrderModel orderModel = new OrderModel();

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {

        if (myEventBus.getMsg().equals("AddCommentSuccess")) {
            if(status.equals("0") || status.equals("3"))
            {
                page = 1;
                end = false;
                getData();
            }
        }

        if (myEventBus.getMsg().equals("PaySuccess")) {
            if(status.equals("0") || status.equals("1") || status.equals("2"))
            {
                page = 1;
                end = false;
                getData();
            }
        }

        if (myEventBus.getMsg().equals("PayCancel")) {
            if(status.equals("0") || status.equals("1"))
            {
                page = 1;
                end = false;
                getData();
            }
        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = (String) getArguments().get("status");
        EventBus.getDefault().register(this);
    }

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


    private void to_info(int p)
    {
        String id = orderModel.getItem().get(p).getId();
        String uid = DataCache.getInstance().user.getId()+"";

        double x = 0.0,y=0.0;

        if(XPostion.getInstance().getPostion() != null)
        {
            x = XPostion.getInstance().getPostion().getLongitude();
            y = XPostion.getInstance().getPostion().getLatitude();
        }


        String url = "http://tg01.sssvip.net/wap/index.php?ctl=uc_order&act=app_order_info&id="+
                id+"&uid="+uid+"&xpoint="+x+"&ypoint="+y;

        Intent intent = new Intent();
        intent.setClass(getActivity(), XHtmlVC.class);
        intent.putExtra("url",url);
        intent.putExtra("title","订单详情");
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

        XNetUtil.Handle(APPService.user_orderlist(uid, status,page+""), new XNetUtil.OnHttpResult<OrderModel>() {
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

        adapter = new OrderAdapter(getActivity());

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

    public void btn_click(int p,String flag)
    {
        XNetUtil.APPPrintln("p: "+p+" | flag: "+flag);

        if(flag.equals("查看券码"))
        {
            String name = orderModel.getItem().get(p).getSub_name();
            String oid = orderModel.getItem().get(p).getId();

            Intent intent = new Intent();
            intent.putExtra("name",name);
            intent.putExtra("oid",oid);
            intent.setClass(getActivity(), CouponCodeVC.class);
            getActivity().startActivity(intent);

        }

        if(flag.equals("评价"))
        {
            String did = orderModel.getItem().get(p).getDeal_id();
            String oid = orderModel.getItem().get(p).getId();

            Intent intent = new Intent();
            intent.putExtra("did",did);
            intent.putExtra("oid",oid);
            intent.setClass(getActivity(), CommentSubmitVC.class);
            getActivity().startActivity(intent);

        }

        if(flag.equals("付款"))
        {
            String name = orderModel.getItem().get(p).getSub_name();
            Integer oid = Integer.parseInt(orderModel.getItem().get(p).getId()) ;
            Integer paytype = orderModel.getItem().get(p).getPayment_id();
            Double tprice = orderModel.getItem().get(p).getTotal_price();
            Double cprice = orderModel.getItem().get(p).getAccount_money();
            Double nprice = orderModel.getItem().get(p).getNeed_pay_price();

            Intent intent = new Intent();
            intent.putExtra("oid",oid);
            intent.putExtra("name",name);
            intent.putExtra("paytype",paytype);
            intent.putExtra("tprice",tprice);
            intent.putExtra("cprice",cprice);
            intent.putExtra("nprice",nprice);

            intent.setClass(getActivity(), UCOrderPayVC.class);
            getActivity().startActivity(intent);

        }

    }

    class OrderAdapter extends BaseAdapter {
        Context context;

        XRecyclerViewItemClick itemClick;

        public void setOnItemClick(XRecyclerViewItemClick a)
        {
            itemClick = a;
        }

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            getItemView bundle ;

            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_cell, null);
                bundle = new getItemView();
                bundle.name=(TextView)convertView.findViewById(R.id.order_cell_name);
                bundle.status=(TextView)convertView.findViewById(R.id.order_cell_status);
                bundle.time=(TextView)convertView.findViewById(R.id.order_cell_time);
                bundle.price=(TextView)convertView.findViewById(R.id.order_cell_price);
                bundle.btn=(TextView)convertView.findViewById(R.id.order_cell_btn);

                bundle.img=(ImageView)convertView.findViewById(R.id.order_cell_img);

                convertView.setTag(bundle);
            }
            else
            {
                bundle = (getItemView) convertView.getTag();
            }

            OrderItemModel item = orderModel.getItem().get(position);

            bundle.name.setText(item.getSub_name());
            bundle.time.setText("下单时间："+item.getCreate_time());
            bundle.price.setText("￥"+item.getTotal_price());

            ImageLoader.getInstance().displayImage(item.getDeal_icon(),bundle.img);

            if(!item.getPay_status().equals("2"))
            {
                bundle.status.setText("未付款");
                bundle.btn.setText("付款");
                bundle.btn.setVisibility(View.VISIBLE);
            }
            else
            {
                if(item.getOrder_status().equals("1"))
                {

                    if(item.getDp_id() > 0)
                    {
                        bundle.status.setText("已点评");
                        bundle.btn.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        bundle.status.setText("已消费");
                        bundle.btn.setText("评价");
                        bundle.btn.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    bundle.status.setText("未使用");
                    bundle.btn.setText("查看券码");
                    bundle.btn.setVisibility(View.VISIBLE);
                }

            }

            if(item.getRefund_status() == 1)
            {
                bundle.status.setText("退款中");
                bundle.btn.setVisibility(View.INVISIBLE);
            }
            else if(item.getRefund_status() == 2)
            {
                bundle.status.setText("已退款");
                bundle.btn.setVisibility(View.INVISIBLE);
            }
            else if(item.getRefund_status() == 3)
            {
                bundle.status.setText("拒绝退款");
                bundle.btn.setVisibility(View.INVISIBLE);
            }
            final String str = bundle.btn.getText().toString();
            bundle.btn.setOnClickListener(null);
            bundle.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btn_click(position,str);
                }
            });

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
            TextView name,status,time,price,btn;
            ImageView img;
        }
    }



}