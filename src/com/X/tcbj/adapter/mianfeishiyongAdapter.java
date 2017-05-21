package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.Mianfei_jieshu;
import com.X.tcbj.activity.Mianfei_tijiaobaogao;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.Shiyong_shenqing;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/16.
 */
public class mianfeishiyongAdapter extends BaseAdapter
{
 private ArrayList<HashMap<String,String>> mianfei;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    String id;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    int kuan,gao;
 int logn;
    public mianfeishiyongAdapter(ArrayList<HashMap<String,String>> mianfei,Context context,int kuan,int gao)
    {

        this.mianfei=mianfei;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
      this.kuan=kuan;
        this.gao=gao;
    }
    @Override
    public int getCount() {
        return mianfei.size();
    }

    @Override
    public Object getItem(int position) {
        return mianfei.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewhoid viewhoid=null;
        if(convertView!=null)
        {
            viewhoid=(Viewhoid)convertView.getTag();
        }else
        {
            viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(context).inflate(R.layout.mianfeishiyong_item1,null);
            viewhoid.biaoti=(TextView)convertView.findViewById(R.id.biaoti);
            viewhoid.shuliang=(TextView)convertView.findViewById(R.id.shu);
            viewhoid.renshu=(TextView)convertView.findViewById(R.id.renshu);
            viewhoid.tu=(ImageView)convertView.findViewById(R.id.tu);
            viewhoid.tu1=(ImageView)convertView.findViewById(R.id.jinxing);
            viewhoid.anniu=(TextView)convertView.findViewById(R.id.dianji);
            viewhoid.tian=(TextView)convertView.findViewById(R.id.tian);
            viewhoid.juli=(TextView)convertView.findViewById(R.id.juli);
            convertView.setTag(viewhoid);
        }
        String url=mianfei.get(position).get("PicID");
        options=ImageUtils.setOptions();
        ViewGroup.LayoutParams layoutParams=viewhoid.tu.getLayoutParams();
        layoutParams.width=kuan;
        layoutParams.height=Integer.parseInt(kuan*2/3+"");

        viewhoid.tu.setLayoutParams(layoutParams);
//        int o=  viewhoid.tu.getWidth();
//        int p=   viewhoid.tu.getHeight();
//        ViewTreeObserver vto = viewhoid.tu.getViewTreeObserver();
//        final Viewhoid finalViewhoid = viewhoid;
//        final Viewhoid finalViewhoid1 = viewhoid;
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                int height = finalViewhoid.tu.getMeasuredHeight();
//                int width = finalViewhoid1.tu.getMeasuredWidth();
//
//                return true;
//            }
//        });
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);

        viewhoid.biaoti.setText(mianfei.get(position).get("Title"));
       viewhoid.renshu.setText(mianfei.get(position).get("SubmitNum"));
        viewhoid.shuliang.setText(mianfei.get(position).get("FreeNum"));
        int a=Integer.parseInt(mianfei.get(position).get("State"));

        if(a==1)
        {
            viewhoid.tu1.setBackgroundResource(R.drawable.trialstate2);
            viewhoid.anniu.setText("点击回顾");
            viewhoid.anniu.setBackgroundResource(R.drawable.trialbutton);
            viewhoid.juli.setText("已结束");
            viewhoid.tian.setVisibility(View.GONE);
            viewhoid.anniu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setClass(context,Mianfei_jieshu.class);
                    intent.putExtra("ID", mianfei.get(position).get("ID"));
                    context.startActivity(intent);

                }
            });
        }
        if(a==2)
        {
            viewhoid.tu1.setBackgroundResource(R.drawable.trialstate);
            viewhoid.anniu.setBackgroundResource(R.drawable.trialbutton);
            viewhoid.anniu.setText("申请试用");
            String tian=mianfei.get(position).get("SurplusTime");
            viewhoid.juli.setText("距离结束");
            String[] aa=tian.split(":");
            if(!aa[0].equals("0"))
            {
                int w=Integer.parseInt(aa[0])+1;
            viewhoid.tian.setVisibility(View.VISIBLE);
            viewhoid.tian.setText("<"+w+"天");}
            else
            {
                if(!aa[1].equals("0"))
                {
                    int w=Integer.parseInt(aa[1])+1;
                viewhoid.tian.setVisibility(View.VISIBLE);
                viewhoid.tian.setText("<"+w+"小时");
                }
                else
                {
                    int w=Integer.parseInt(aa[2])+1;
                    viewhoid.tian.setVisibility(View.VISIBLE);
                    viewhoid.tian.setText("<"+w+"分");
                }
            }
            viewhoid.anniu.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    logn= PreferencesUtils.getInt(context, "logn");
                    if(logn==0)
                    {
                        Intent intent = new Intent();
                        intent.setClass(context, LoginActivity.class);
                        context.startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent();
                        intent.setClass(context,Shiyong_shenqing.class);
                        intent.putExtra("freeId",mianfei.get(position).get("ID"));
                        context.startActivity(intent);
                    }
                }
            });
        }
        if(a==3)
        {
            viewhoid.tu1.setBackgroundResource(R.drawable.trialstate1);
            viewhoid.anniu.setBackgroundResource(R.drawable.trialbutton);
            viewhoid.anniu.setText("提交报告");
            String tian=mianfei.get(position).get("SurplusTime");
            viewhoid.juli.setText("距离结束");
            String[] aa=tian.split(":");
            if(!aa[0].equals("0"))
            {
                int w=Integer.parseInt(aa[0])+1;
                viewhoid.tian.setVisibility(View.VISIBLE);
                viewhoid.tian.setText("<"+w+"天");}
            else
            {
                if(!aa[1].equals("0"))
                {
                    int w=Integer.parseInt(aa[1])+1;
                viewhoid.tian.setVisibility(View.VISIBLE);
                viewhoid.tian.setText("<"+w+"小时");
                }else
                {
                    int w=Integer.parseInt(aa[2])+1;
                    viewhoid.tian.setVisibility(View.VISIBLE);
                    viewhoid.tian.setText("<"+w+"分");
                }
            }
            viewhoid.anniu.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                        Intent intent = new Intent();
                        intent.setClass(context,Mianfei_tijiaobaogao.class);
                        intent.putExtra("ID",mianfei.get(position).get("ID"));
                        context.startActivity(intent);

                }
            });
        }
        if(a==4)
        {
            viewhoid.tu1.setBackgroundResource(R.drawable.trialstar);
            viewhoid.anniu.setBackgroundResource(R.drawable.trialbutton);
            viewhoid.anniu.setText("即将开始");
            String tian=mianfei.get(position).get("SurplusTime");
            viewhoid.juli.setText("距离开始");
            String[] aa=tian.split(":");
            if(!aa[0].equals("0")){
                int w=Integer.parseInt(aa[0])+1;
                viewhoid.tian.setVisibility(View.VISIBLE);
                viewhoid.tian.setText("<"+w+"天");}
            else
            {
                if(!aa[1].equals("0")){
                    int w=Integer.parseInt(aa[1])+1;
                viewhoid.tian.setVisibility(View.VISIBLE);
                viewhoid.tian.setText("<"+w+"小时");}
                else
                {
                    int w=Integer.parseInt(aa[2])+1;
                    viewhoid.tian.setVisibility(View.VISIBLE);
                    viewhoid.tian.setText("<"+w+"分");
                }
            }
        }

        return convertView;
    }
    public class Viewhoid
    {
        ImageView tu,tu1;
        TextView biaoti,renshu,shuliang,anniu,tian,juli;


    }
}
