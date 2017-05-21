package com.X.tcbj.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.myview.ZViewPager;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2016/5/13.
 */
public class ImgNewsInfo extends FragmentActivity implements View.OnClickListener{
    ZViewPager viewpage;
    ImageView back, share;
    TextView title, page, content;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> contentarray = new ArrayList<HashMap<String, String>>();
    List<Map<String, Object>> popList = new ArrayList<Map<String, Object>>();
    String newsID, url, urlstr;
    private String[] orderStr = new String[] { "评论", "分享" };
    private PopupWindow popupWindow;
    HashMap<String, Object> map;
    View popView;
    LinearLayout conlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imginfo);
        intview();
        newsID = getIntent().getStringExtra("iD");
        url = Constant.url + "news/newsPicDetail?newsID="
                + newsID;
        getUrlstr();
    }

    private void intview() {
        conlay=(LinearLayout)findViewById(R.id.conlay);
        viewpage = (ZViewPager) findViewById(R.id.viewpager);
        back = (ImageView) findViewById(R.id.photo_back);
        share = (ImageView) findViewById(R.id.photo_top_more);
        title = (TextView) findViewById(R.id.title);
        page = (TextView) findViewById(R.id.page);
        content = (TextView) findViewById(R.id.content);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        content.setOnClickListener(this);
        viewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page.setText(((position+1)+"/"+(parray.size())));
                content.setText(parray.get(position).get("content"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getUrlstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, ImgNewsInfo.this);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                   try {
                       setarray(urlstr);
                       ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), parray);
                       viewpage.setAdapter(mAdapter);
                       title.setText(contentarray.get(0).get("description"));
                       page.setText(("1/"+(parray.size())));
                       content.setText(parray.get(0).get("content"));
                   }catch (Exception e){

                   }
                    break;
                case 2:
                    Toast.makeText(ImgNewsInfo.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setarray(String str) {
        JSONObject jsonObject= JSON.parseObject(str);
        int a=jsonObject.getIntValue("status");
        if (a==0){
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("description", jsonObject1.getString("title"));
                hashMap.put("clickNum", jsonObject1.getString("clickNum"));
                hashMap.put("commentCount",
                        jsonObject1.getString("commentCount"));
                hashMap.put("distinctCount",
                        jsonObject1.getString("distinctCount"));
                contentarray.add(hashMap);
                JSONArray jsonArray1=jsonObject1.getJSONArray("newsPPt");
                for (int j=0;j<jsonArray1.size();j++){
                    hashMap=new HashMap<String, String>();
                    JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                    hashMap.put("PicUrl", jsonObject2.getString("picID"));
                    hashMap.put("content", jsonObject2.getString("content"));
                    hashMap.put("weight", jsonObject2.getString("weight"));
                    hashMap.put("height", jsonObject2.getString("height"));
                    parray.add(hashMap);
                }
            }
        }else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo_back:
                finish();
                break;
            case R.id.photo_top_more:
                showOrderPopView();
                break;
//            case R.id.content:
//                Animation animation= (Animation) getResources().getAnimation(R.anim.imgnewsaim);
//                conlay.startAnimation();
//                conlay.setVisibility(View.GONE);
//                break;
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<HashMap<String, String>> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position).get("PicUrl");
            return ImageDetailFragment.newInstance(url);
        }
    }
    public void showOrderPopView() {
        popView = ImgNewsInfo.this.getLayoutInflater().inflate(
                R.layout.news_share, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = ImgNewsInfo.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 使其聚焦
        popupWindow.setFocusable(true);
        // 设置不允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 刷新状态
        popupWindow.update();
        ListView listview = (ListView) popView
                .findViewById(R.id.new_share_list);

        popList.clear();
        int[] imageId = new int[] { R.drawable.news_top_share,
                R.drawable.news_top_comment };
        for (int i = 0; i < 2; i++) {
            map = new HashMap<String, Object>();
            map.put("img", imageId[i]);
            map.put("item", orderStr[i]);
            popList.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(ImgNewsInfo.this, popList,
                R.layout.news_share_txt, new String[] { "img", "item" },
                new int[] { R.id.share_img, R.id.share_txt });
        listview.setAdapter(adapter);

        popupWindow.showAsDropDown(share);// 显示在筛选区域条件下
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(contentarray.size()==0){

                }else{
                    if (arg2 == 1) {
                        int cityID = PreferencesUtils.getInt(ImgNewsInfo.this, "cityID");
                        System.out.println("http://192.168.2.177:8080/wap/news/newsPicDetail?msg="+newsID+","+cityID);
                        String city=PreferencesUtils.getString(ImgNewsInfo.this, "cityNamepl");
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();// 分享前要先授权
                        // 分享时Notification的图标和文字
//					oks.setNotification(R.drawable.ic_launcher,
//							getString(R.string.app_name));
                        String cityName=PreferencesUtils.getString(ImgNewsInfo.this, "cityName");
                        oks.setTitle(title.getText()+"  "+cityName+"城市热线");
                        oks.setImageUrl(parray.get(0).get("PicUrl"));
                        oks.setTitleUrl("http://m.rexian.cn/news/newsPicDetail?msg="+newsID + ","+cityID);// 商家地址分享
                        oks.setText(parray.get(0).get("content")+"\r\n点击查看更多"+":http://m.rexian.cn/news/newsPicDetail?msg="+newsID + ","+cityID);
                        // site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSite("新闻");
                        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                        oks.setUrl("http://m.rexian.cn/news/newsPicDetail?msg="+newsID + ","+cityID);
                        oks.setSiteUrl("luoyang.rexian.cn");
                        oks.show(ImgNewsInfo.this);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("newsID", newsID);

                        intent.putExtra("title", contentarray.get(0).get("description"));
                        intent.putExtra("commentCount",
                                contentarray.get(0).get("commentCount"));
                        intent.putExtra("distinctCount",
                                contentarray.get(0).get("distinctCount"));
                        intent.setClass(ImgNewsInfo.this, News_omment.class);
                        ImgNewsInfo.this.startActivity(intent);

                    }
                }
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

    }
}
