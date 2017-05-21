package com.X.tcbj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.X.server.DataCache;
import com.X.tcbj.activity.UserRenzhengVC;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.MyAddressActivity;
import com.X.tcbj.activity.MyCollect;
import com.X.tcbj.activity.MyCollectActivity;
import com.X.tcbj.activity.MyOrderActivity;
import com.X.tcbj.activity.MyTryOutActivity;
import com.X.tcbj.activity.Myinfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.Zhanghaoguanl;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.myview.HeaderGridView;
import com.X.tcbj.utils.CircleImageView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的空间
 *
 * @author zpp
 * @version1.0
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;

    RoundedImageView head;
    TextView nameTV;

    int[] ids = {R.id.mine_layout0,R.id.mine_layout1,R.id.mine_layout2,R.id.mine_layout3,
            R.id.mine_layout4,R.id.mine_layout5,R.id.mine_layout6,R.id.mine_layout7};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        intview();

        return view;
    }

    private void intview() {

        for(int id : ids)
        {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(id);
            linearLayout.setOnClickListener(this);

        }

        head = (RoundedImageView) view.findViewById(R.id.mine_head);
        nameTV = (TextView) view.findViewById(R.id.mine_name);
    }



    @Override
    public void onClick(View v) {

        if(DataCache.getInstance().user == null)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), UserRenzhengVC.class);
            getActivity().startActivity(intent);
        }



//        int Logn = PreferencesUtils.getInt(getActivity(), "logn");
//        Intent intent = new Intent();
//        if (Logn == 0) {
//            intent.setClass(getActivity(), LoginActivity.class);
//            getActivity().startActivity(intent);
//        } else {
//            switch (v.getId()) {
//                case R.id.order:
//                    Constant.ordertype = 0;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.nomoney:
//                    Constant.ordertype = 1;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.nodeliver:
//                    Constant.ordertype = 2;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.noreceipt:
//                    Constant.ordertype = 3;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.nocomment:
//                    Constant.ordertype = 4;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.returngood:
//                    Constant.ordertype = 11;
//                    intent.setClass(getActivity(), MyOrderActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.myredpaper:
//                    intent.setClass(getActivity(), Zhanghaoguanl.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.mycollect:
//                    intent.setClass(getActivity(), MyCollectActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.mycomment:
//                    intent.setClass(getActivity(), MyCollect.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.mytry:
//                    intent.setClass(getActivity(), MyTryOutActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.myaddress:
//                    intent.setClass(getActivity(), MyAddressActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.loginmore:
//                    intent.setClass(getActivity(), Myinfo.class);
//                    getActivity().startActivity(intent);
//                    break;
//            }
//        }


    }


    @Override
    public void onResume() {
        super.onResume();
        int Logn = PreferencesUtils.getInt(getActivity(), "logn");
        if (Logn != 0) {
//            getCount();
//            getUserinfo();
        } else {
//            user_head.setImageResource(R.drawable.user_face);
//            number.setText("请登录");
//            userinfo.setVisibility(View.GONE);
//            nomoneycount.setText("0");
//            nodelivercount.setText("0");
//            receiptcount.setText("0");
//            nocommentcount.setText("0");
//            returngoodcount.setText("0");
//            number.setCompoundDrawables(null, null, null, null);
        }
    }
}
