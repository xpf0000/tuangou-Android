package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.myview.VerificationPop;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.MyhttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VoteAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> abscure_list;
    private Context context;
    DisplayMetrics dm;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler = new Handler();
    VerificationPop verificationPop;

    public VoteAdapter(ArrayList<HashMap<String, Object>> abscure_list,
                       Context context, Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        this.handler = handler;
        dm = new DisplayMetrics();
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        verificationPop = new VerificationPop();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Abscure abscure = null;
        abscure = new Abscure();
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        convertView = LayoutInflater.from(context).inflate(R.layout.vote,
                parent, false);
        abscure.metphead = (ImageView) convertView.findViewById(R.id.head);
        abscure.declaration = (TextView) convertView
                .findViewById(R.id.declaration);
        abscure.name = (TextView) convertView.findViewById(R.id.name);

        // abscure.number=(TextView)convertView.findViewById(R.id.number);
        abscure.vote = (Button) convertView.findViewById(R.id.vote);
        abscure.piaonumber = (TextView) convertView
                .findViewById(R.id.piaonumber);

        abscure.name.setText(abscure_list.get(position).get("title").toString());
        // abscure.number .setText(abscure_list.get(position).get("voteCount"));
        abscure.piaonumber.setText(abscure_list.get(position).get("voteCount").toString());
        String names = abscure_list.get(position).get("names").toString();
        abscure.declaration.setText(names + ":" + abscure_list.get(position).get("value"));
        String url = abscure_list.get(position).get("picture").toString();
        if (abscure_list.get(position).get("canVote").equals("0")) {
            abscure.vote.setBackgroundResource(R.drawable.bbs_glpage_button);
            abscure.vote.setText("今日已经投过票");
        } else if (abscure_list.get(position).get("canVote").equals("113")) {
            abscure.vote.setBackgroundResource(R.drawable.bbs_glpage_button);
            abscure.vote.setText("已过期");
        } else if (abscure_list.get(position).get("canVote").equals("24")) {
            abscure.vote.setBackgroundResource(R.drawable.bbs_glpage_button);
            abscure.vote.setText("投票未开始");
        }
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                dm.heightPixels / 6);
        abscure.metphead.setLayoutParams(params);
        options = ImageUtils.setcenterOptions();
        ImageLoader.displayImage(url, abscure.metphead, options,
                animateFirstListener);
        abscure.metphead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (abscure_list.get(position).get("url").toString().length() != 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, webiew.class);
                    intent.putExtra("url", abscure_list.get(position).get("url").toString());
                    context.startActivity(intent);
                }
            }
        });
        abscure.vote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // handler.sendEmptyMessage(5);
                if (abscure_list.get(position).get("canVote").equals("0")
                        && abscure_list.get(position).get("canVote")
                        .equals("3")) {

                } else {
//                    verificationPop.showpop(context);
//                    verificationPop.setMyPopwindowswListener(new VerificationPop.MyPopwindowsListener() {
//                        @Override
//                        public void onRefresh(String str) {
                    int Logn = PreferencesUtils.getInt(context, "logn");
                    if (Logn == 0) {
                        Intent intent = new Intent();
                        intent.setClass(context, LoginActivity.class);
                        context.startActivity(intent);

                    } else {
                        VerThred("111", position);
                    }
//                        }
//                    });
                }
            }
        });
        return convertView;
    }

    class Abscure {
        TextView name, piaonumber, declaration;
        ImageView metphead;
        Button vote;
    }

    private void VerThred(final String code, final int position) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                Looper.prepare();
                String userId = userId = PreferencesUtils
                        .getString(context, "userid");
                String id = abscure_list.get(position).get("id").toString();
                String menuId = abscure_list.get(position).get(
                        "topicMenuId").toString();
                String topicsId = abscure_list.get(position).get(
                        "topicId").toString();
                int areaId = PreferencesUtils.getInt(context,
                        "cityID");
                String url = Constant.url + "topics/doVoteNew?";
                String dataStr = "id=" + id + "&menuId=" + menuId
                        + "&topicsId=" + topicsId + "&areaId="
                        + areaId + "&userId=" + userId;
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url, dataStr,
                        "POST");

                if (object == null) {
                    handler.sendEmptyMessage(2);
                    Looper.loop();
                } else {
                    String str = object.toString();
                    JSONObject jsonObject = JSONObject
                            .parseObject(str);
                    int a = jsonObject.getIntValue("status");
                    if (a == 0) {
                        Constant.Votepo = position;
                        handler.sendEmptyMessage(5);
                    } else {
                        handler.sendEmptyMessage(6);
                    }
                }
            }
        }.start();
    }
}
