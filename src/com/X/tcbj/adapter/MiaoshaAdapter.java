package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by admins on 2015/12/7.
 */
public class MiaoshaAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler=new Handler();
    String stat;
    public MiaoshaAdapter(ArrayList<HashMap<String, String>> abscure_list,
                          Context context,Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        this.handler=handler;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
        getItemView.content = (TextView) convertView.findViewById(R.id.content);
        getItemView.kong = (TextView) convertView.findViewById(R.id.kong);
        getItemView.cv_countdownViewTest2 = (CountdownView) convertView.findViewById(R.id.cv_countdownViewTest2);
        getItemView.contents = (TextView) convertView.findViewById(R.id.contents);
        getItemView.shop_img = (ImageView) convertView.findViewById(R.id.shop_img);
        getItemView.statimg = (ImageView) convertView.findViewById(R.id.statimg);
        getItemView.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        getItemView.content.setText("￥" + abscure_list.get(position).get("Price"));
        getItemView.contents.setText("￥" + abscure_list.get(position).get("TruePrice"));
        final String url = abscure_list.get(position).get("PicID");
       final String State= abscure_list.get(position).get("State");
        stat=State;
        getItemView.cv_countdownViewTest2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                if (abscure_list.get(position).get("State").equals("1")) {
                    getItemView.statimg.setVisibility(View.GONE);
                    abscure_list.get(position).put("State","2");
                    long time = Long.parseLong(abscure_list.get(position).get("EndSurplusTime"));
                    getItemView.cv_countdownViewTest2.start((time * 1000));
                    Constant.position=position;
//                    handler.sendEmptyMessage(7);
                } else {
                    getItemView.statimg.setVisibility(View.VISIBLE);
                    getItemView.statimg.setBackgroundResource(R.drawable.indexstateend);
            }

            }
        });
        int a = PreferencesUtils.getInt(context, "photo");

        if (State.equals("1")) {
            long time = Long.parseLong(abscure_list.get(position).get("SurplusTime"));
            getItemView.cv_countdownViewTest2.start((time * 1000));
            getItemView.statimg.setBackgroundResource(R.drawable.indexstatestar);
        } else if (State.equals("4")) {
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateend);
        } else if (State.equals("2")) {
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateing);
            long time = Long.parseLong(abscure_list.get(position).get("SurplusTime"));
            getItemView.cv_countdownViewTest2.start((time * 1000));
        } else {
            getItemView.cv_countdownViewTest2.setVisibility(View.GONE);
            getItemView.kong.setVisibility(View.VISIBLE);
            getItemView.statimg.setBackgroundResource(R.drawable.indexstateout);
        }
        options = ImageUtils.setOptions();
        if (a == 1) {
            ImageLoader.displayImage(url, getItemView.shop_img, options,
                    animateFirstListener);
        } else {
            String urls = ImageLoader.getDiscCache().get(url).getPath();
            boolean bloo = ImageUtils.fileIsExists(urls);
            if (bloo) {
                ImageLoader.displayImage(url, getItemView.shop_img, options,
                        animateFirstListener);
            } else {
                getItemView.shop_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoader.displayImage(url, getItemView.shop_img, options,
                                animateFirstListener);
                        getItemView.shop_img.setClickable(false);
                    }
                });
            }
        }
        return convertView;
    }

    private class getItemView {
        ImageView shop_img, statimg;
        TextView content, contents,kong;
        CountdownView cv_countdownViewTest2;
    }

}
