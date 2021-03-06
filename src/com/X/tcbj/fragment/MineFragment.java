package com.X.tcbj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.activity.APPConfig;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.MyCollect;
import com.X.tcbj.activity.MyCollectActivity;
import com.X.tcbj.activity.Myinfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.UserFenhongVC;
import com.X.tcbj.activity.UserRenzhengVC;
import com.X.tcbj.activity.UserUnitsVC;
import com.X.tcbj.utils.XHtmlVC;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
            R.id.mine_layout4,R.id.mine_layout5,R.id.mine_layout6,R.id.mine_layout7,R.id.mine_layout8,R.id.mine_layout9};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);

        EventBus.getDefault().register(this);

        intview();

        initUI();

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

    private void initUI()
    {
        if(DataCache.getInstance().user == null)
        {
            head.setImageResource(R.mipmap.user_head_big);
            nameTV.setText("尚未登陆");
        }
        else
        {
            String url = DataCache.getInstance().user.getAvatar();
            if(url.indexOf("http://") < 0 && url.indexOf("https://") < 0)
            {
                url = "http://www.tcbjpt.com/"+DataCache.getInstance().user.getAvatar();
            }

            ImageLoader.getInstance().displayImage(url,head);

            if(DataCache.getInstance().user.getIs_effect() == 1)
            {
                nameTV.setText(DataCache.getInstance().user.getReal_name());
            }
            else
            {
                if(DataCache.getInstance().user.isRezhenging())
                {
                    nameTV.setText("会员审核中");
                }
                else
                {
                    nameTV.setText("请提交认证");
                }

            }
        }

    }


    @Override
    public void onClick(View v) {

        if(v.getId() != R.id.mine_layout7)
        {
            if(DataCache.getInstance().user == null)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                intent.putExtra("isPush",false);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                return;
            }
            else
            {
                if(DataCache.getInstance().user.getIs_effect() != 1)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), UserRenzhengVC.class);
                    intent.putExtra("isPush",false);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                    return;
                }

            }
        }

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.mine_layout0:
                intent.setClass(getActivity(), Myinfo.class);
                getActivity().startActivity(intent);
                break;
            case R.id.mine_layout1:

                intent.setClass(getActivity(), XHtmlVC.class);
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=user&act=xf_danyuan&isapp=true");
                intent.putExtra("title","消费单元");
                getActivity().startActivity(intent);

//                intent.setClass(getActivity(), UserUnitsVC.class);
//                getActivity().startActivity(intent);
                break;
            case R.id.mine_layout2:
                intent.setClass(getActivity(), UserFenhongVC.class);
                getActivity().startActivity(intent);
                break;
            case R.id.mine_layout3:
                intent.setClass(getActivity(), MyCollectActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.mine_layout4:
                intent.setClass(getActivity(), MyCollect.class);
                getActivity().startActivity(intent);
                break;
            case R.id.mine_layout5:


                String uname = DataCache.getInstance().user.getUser_name();
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=user_center&act=app_qrcode&code="+uname);
                intent.putExtra("title","我的邀请码");

                intent.setClass(getActivity(), XHtmlVC.class);

                getActivity().startActivity(intent);


                break;
            case R.id.mine_layout6:

                intent.setClass(getActivity(), XHtmlVC.class);
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=yaoqing&act=app_index");
                getActivity().startActivity(intent);

                break;
            case R.id.mine_layout7:
                intent.setClass(getActivity(), APPConfig.class);
                getActivity().startActivity(intent);
                break;

            case R.id.mine_layout8:

                intent.setClass(getActivity(), XHtmlVC.class);
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=user&act=app_user_bank");
                intent.putExtra("title","绑定银行卡");
                getActivity().startActivity(intent);

                break;

            case R.id.mine_layout9:

                intent.setClass(getActivity(), XHtmlVC.class);
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=user&act=app_tx_user");
                intent.putExtra("title","我的提现");
                getActivity().startActivity(intent);

                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        DataCache.getInstance().getUinfo();
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("UserAccountChange")) {

            initUI();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
