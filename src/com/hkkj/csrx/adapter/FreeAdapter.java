package com.hkkj.csrx.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.LoginActivity;
import com.hkkj.csrx.activity.Mianfei_jieshu;
import com.hkkj.csrx.activity.Mianfei_jijiangkaishi;
import com.hkkj.csrx.activity.Mianfei_tijiaobaogao;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.activity.Shiyong_shenqing;
import com.hkkj.csrx.activity.tijiaobaogao;
import com.hkkj.csrx.utils.ImageUtils;
import com.hkkj.csrx.utils.TimeDeviation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/4.
 */
public class FreeAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    TimeDeviation timeDeviation;

    public FreeAdapter(ArrayList<HashMap<String, String>> abscure_list,
                       Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        timeDeviation = new TimeDeviation();
    }

    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.free_item, null);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.time = (TextView) convertView.findViewById(R.id.time);
        getItemView.shenqing = (Button) convertView.findViewById(R.id.shenqing);
        getItemView.salimg = (ImageView) convertView.findViewById(R.id.salimg);
        getItemView.statimg = (ImageView) convertView.findViewById(R.id.statimg);
        getItemView.riqi = (TextView) convertView.findViewById(R.id.riqi);
        getItemView.stat = (TextView) convertView.findViewById(R.id.stat);
        getItemView.title.setText(abscure_list.get(position).get("Title"));
        int stat = Integer.parseInt(abscure_list.get(position).get("State"));
        if (stat == 1) {
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateend);
            getItemView.time.setVisibility(View.INVISIBLE);
            getItemView.riqi.setVisibility(View.INVISIBLE);
            getItemView.stat.setVisibility(View.VISIBLE);
            getItemView.stat.setText("已结束");
            getItemView.shenqing.setVisibility(View.VISIBLE);
            getItemView.shenqing.setText("点击回顾");
        } else if (stat == 2) {
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateing);
//            String time = timeDeviation.Time(abscure_list.get(position).get("EndTime"));
//            int i = time.indexOf("加");
//            getItemView.time.setText(time.substring(0, i));
//            getItemView.riqi.setText(time.substring(i + 1, time.length()));
            String tian=abscure_list.get(position).get("SurplusTime");
            String[] aa=tian.split(":");
            getItemView.time.setVisibility(View.VISIBLE);
            getItemView.riqi.setVisibility(View.VISIBLE);
            getItemView.shenqing.setVisibility(View.VISIBLE);
            getItemView.shenqing.setText("申请试用");
            if(!aa[0].equals("0"))
            {
                getItemView.time.setText("<" + (Integer.parseInt(aa[0])+1 ));
                getItemView.riqi.setText("天");
            }else
            {
                if(!aa[1].equals("0"))
                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[1])+1 ));
                    getItemView.riqi.setText("小时");
                }
                else

                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[2])+1 ));
                    getItemView.riqi.setText("分");
                }

            }


        } else if (stat == 3) {
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateput);
            String time = timeDeviation.Time(abscure_list.get(position).get("EndTime"));
//            int i = time.indexOf("加");
//            getItemView.time.setText(time.substring(0, i));
//            getItemView.riqi.setText(time.substring(i + 1, time.length()));
            getItemView.shenqing.setVisibility(View.VISIBLE);
            getItemView.shenqing.setText("提交报告");

            String tian=abscure_list.get(position).get("SurplusTime");
            getItemView.time.setVisibility(View.VISIBLE);
            getItemView.riqi.setVisibility(View.VISIBLE);
            String[] aa=tian.split(":");
            if(!aa[0].equals("0"))
            {
                getItemView.time.setText("<" + (Integer.parseInt(aa[0])+1 ) );
                getItemView.riqi.setText("天");
            }else
            {
                if(!aa[1].equals("0"))
                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[1])+1 ));
                    getItemView.riqi.setText("小时");
                }
                else

                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[2])+1 ));
                    getItemView.riqi.setText("分");
                }

            }

        } else if (stat == 4) {
            String time = timeDeviation.Time(abscure_list.get(position).get("StartTime"));
            getItemView.statimg.setBackgroundResource(R.drawable.indexstatestar);
//            int i = time.indexOf("加");
            getItemView.stat.setText("距离开始");
//            getItemView.time.setText(time.substring(0, i));
            getItemView.shenqing.setText("即将开始");
//            getItemView.riqi.setText(time.substring(i + 1, time.length()));
            String tian=abscure_list.get(position).get("SurplusTime");
            getItemView.time.setVisibility(View.VISIBLE);
            getItemView.riqi.setVisibility(View.VISIBLE);
            String[] aa=tian.split(":");
            if(!aa[0].equals("0"))
            {
                getItemView.time.setText("<" + (Integer.parseInt(aa[0])+1 ) );
                getItemView.riqi.setText("天");
            }else
            {
                if(!aa[1].equals("0"))
                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[1])+1 ));
                    getItemView.riqi.setText("小时");
                }
                else

                {
                    getItemView.time.setText("<"+(Integer.parseInt(aa[2])+1 ));
                    getItemView.riqi.setText("分");
                }

            }
        }
        final String url = abscure_list.get(position).get("PicID");
        getItemView.shenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stat = Integer.parseInt(abscure_list.get(position).get("State"));
                Intent intent = new Intent();
                if (stat == 1) {
                    intent.setClass(context, Mianfei_jieshu.class);
                    intent.putExtra("ID", abscure_list.get(position).get("ID"));
                    context.startActivity(intent);
                } else if (stat == 4) {
                    intent.setClass(context, Mianfei_jijiangkaishi.class);
                    intent.putExtra("ID", abscure_list.get(position).get("ID"));
                    context.startActivity(intent);

                }else if(stat==3)
                {

                    intent.setClass(context, Mianfei_tijiaobaogao.class);
                    intent.putExtra("ID", abscure_list.get(position).get("ID"));
                    context.startActivity(intent);
                }

                else {

                    int Logn = PreferencesUtils.getInt(context, "logn");
                    if (Logn == 0) {

                        intent.setClass(context, LoginActivity.class);
                        context.startActivity(intent);


                    } else {
                        if (stat == 2) {
                            intent.setClass(context, Shiyong_shenqing.class);
                            intent.putExtra("freeId", abscure_list.get(position).get("ID"));
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });
        options = ImageUtils.setOptions();
        int a = PreferencesUtils.getInt(context, "photo");
        if (a == 1) {
            ImageLoader.displayImage(url, getItemView.salimg, options,
                    animateFirstListener);
        } else {
            String urls = ImageLoader.getDiscCache().get(url).getPath();
            boolean bloo = ImageUtils.fileIsExists(urls);
            if (bloo) {
                ImageLoader.displayImage(url, getItemView.salimg, options,
                        animateFirstListener);
            } else {
                getItemView.salimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoader.displayImage(url, getItemView.salimg, options,
                                animateFirstListener);
                        getItemView.salimg.setClickable(false);
                    }
                });
            }
        }
        return convertView;
    }

    private class getItemView {
        ImageView statimg, salimg;
        TextView title, time, riqi, stat;
        Button shenqing;
    }
}
