package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MymianfeiAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Administrator on 2015/12/2.
 */
public class MiaoShaActivicty extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,AbsListView.OnScrollListener, MyItemClickListener
{
 private RecyclerView mRecyclerView;

  private MiaoshaAdapert adapert;
    private ListView lv;
    private TextView gengduo;
    private ImageView xia;
    private ArrayList<HashMap<String,String>>tuangou;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url;
    private String str;
    private View footer;
    private RelativeLayout dianjizaijia;
    private Boolean shua=true;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
     int nextpage=1;
    String fenid;
    HashMap<String,String>hashMap;
    HashMap<String,String>hashMap1;
    private  ImageView back;
    private String biaotiurl;
    private MymianfeiAdapter mAdapter;
    private ArrayList<HashMap<String,String>> biaoti;
    private ArrayList<HashMap<String,String>> biaoti1;
    String str1;//标题
int zonyeshu;
    long zui,zui1,zui2;
    private int areaid;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    private Handler handler=new Handler()
{
    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
        switch (msg.what)
        {
            case 1:
             //   if (lv.getFooterViewsCount() > 0) {
               //     lv.removeFooterView(footer);
                //}

                Toast.makeText(MiaoShaActivicty.this,"无内容",Toast.LENGTH_SHORT).show();

                if (lv.getFooterViewsCount() > 0) {

                    lv.removeFooterView(footer);

                }

                adapert.notifyDataSetChanged();
              break;
            case 2:
                try {
                    System.out.println("14");
                    jiexi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                Toast.makeText(MiaoShaActivicty.this,"网络超时",Toast.LENGTH_SHORT).show();
                if (lv.getFooterViewsCount() > 0) {
                    lv.removeFooterView(footer);
                }
                break;
            case 4:
                try {
                    jiexi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 5:
                Toast.makeText(MiaoShaActivicty.this,"网络超时",Toast.LENGTH_SHORT).show();
                break;
            case 6:
                try {
                    jiexi1();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                mAdapter.notifyDataSetChanged();
                break;
            case 8:
                adapert.notifyDataSetChanged();
                break;
            case 9:
                Toast.makeText(MiaoShaActivicty.this,"请求失败",Toast.LENGTH_SHORT).show();
                break;
            case 10:
                adapert.notifyDataSetChanged();
                break;


        }
    }
};

    private void jiexi1() throws JSONException
    {
        JSONObject jsonObject = new JSONObject(str1);
        if(jsonObject.get("statusMsg").equals("请求成功"))
        {
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            hashMap=new HashMap<String, String>();
            hashMap.put("Name","全部");
            hashMap.put("select", "1");
            hashMap.put("ID","0");
            biaoti.add(0,hashMap);
            for(int a=0;a<jsonArray.length();a++)
            {
                JSONObject jsonObject1=(JSONObject)jsonArray.get(a);
                hashMap=new HashMap<String, String>();
                hashMap.put("Name",jsonObject1.getString("Name"));
                hashMap.put("ID",jsonObject1.getString("ID"));
                hashMap.put("select", "0");
                biaoti.add(hashMap);
            }
            handler.sendEmptyMessage(7);
            System.out.println("666");
        }
        else
        {
            System.out.println("777");
            handler.sendEmptyMessage(9);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miaosha);
        init();
    }

    private void init()
    {
        areaid = PreferencesUtils.getInt(MiaoShaActivicty.this, "cityID");
        biaotiurl= Constant.url + "kill/getAllKillClass?siteId="+areaid;
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(MiaoShaActivicty.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        biaoti=new ArrayList<HashMap<String, String>>();
        biaoti1=new ArrayList<HashMap<String, String>>();
        fenid="0";//默认分类为0
        footer = View.inflate(this, R.layout.footer, null);
        gengduo=(TextView)footer.findViewById(R.id.gengduo);
        xia=(ImageView)footer.findViewById(R.id.xia);
        dianjizaijia=(RelativeLayout)footer.findViewById(R.id.zaijia);
        dianjizaijia.setOnClickListener(this);
        tuangou=new ArrayList<HashMap<String,String>>();
        adapert=new MiaoshaAdapert();
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
             swipeRefreshLayout.setOnRefreshListener(this);
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.mAdapter = new MymianfeiAdapter(biaoti);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mAdapter.setOnItemClickListener(this);
        shuju(biaotiurl, 3);
        lv=(ListView)findViewById(R.id.lv);
        // 在增加listview的页脚之前，需要提前设置一次
        lv.addFooterView(footer);
        lv.setAdapter(adapert);
        // 滚动监听事件
        lv.setOnScrollListener(this);
        url=Constant.url +"kill/getAllKillProducts?page=1&pageSize=5&siteId="+areaid+"&classId="+fenid;
        shuju(url, 1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            if(tuangou.get(position).get("State").equals("2"))
            {
            Intent intent=new Intent();
            intent.putExtra("ID",tuangou.get(position).get("ID"));
            intent.putExtra("StoreID",tuangou.get(position).get("StoreID"));
                intent.putExtra("ProductID",tuangou.get(position).get("ProductID"));
            intent.setClass(MiaoShaActivicty.this,MiaoShaXiangQingActivicty.class);
            MiaoShaActivicty.this.startActivity(intent);
            }
            if(tuangou.get(position).get("State").equals("1"))
            {
                Intent intent=new Intent();
                intent.putExtra("ID",tuangou.get(position).get("ID"));
                intent.putExtra("StoreID",tuangou.get(position).get("StoreID"));
                intent.putExtra("ProductID",tuangou.get(position).get("ProductID"));
                intent.setClass(MiaoShaActivicty.this,MiaoShaXiangQingActivicty.class);
                MiaoShaActivicty.this.startActivity(intent);
            }
            if(tuangou.get(position).get("State").equals("3"))
            {
                Intent intent=new Intent();
                intent.putExtra("id", tuangou.get(position).get("ProductID"));
                intent.setClass(MiaoShaActivicty.this,MallInfo.class);
                MiaoShaActivicty.this.startActivity(intent);
            }
            if(tuangou.get(position).get("State").equals("4"))
            {
                Intent intent=new Intent();
                intent.putExtra("id", tuangou.get(position).get("ProductID"));
                intent.setClass(MiaoShaActivicty.this,MallInfo.class);
                MiaoShaActivicty.this.startActivity(intent);
            }


        }
    });
    }

//    private void initdata(final String urlw)
//    {
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//                super.run();
//                HttpRequest httpRequest=new HttpRequest();
//                str1=httpRequest.doGet(urlw,MiaoShaActivicty.this);
//                if(str1.equals("网络超时"))
//                {
//                    handler.sendEmptyMessage(5);
//                }else
//                {
//                    handler.sendEmptyMessage(6);
//                }
//
//            }
//        }.start();
//    }

    private void shuju(final String url, final int type)
    {

          new Thread()
          {
              @Override
              public void run()
              {
                  super.run();
                  HttpRequest lian=new HttpRequest();
                  if(type==3)
                  {
                      str1=lian.doGet(url,MiaoShaActivicty.this);
                      if(str1.equals("网络超时"))
                      {
                          handler.sendEmptyMessage(5);
                      }else
                      {
                          handler.sendEmptyMessage(6);
                      }
                  }else
                  {
                      str=lian.doGet(url,MiaoShaActivicty.this);
                      if (str.equals("网络超时"))
                      {
                          handler.sendEmptyMessage(3);
                      } else {
                          if (type == 1) {
                              System.out.println("13");
                              handler.sendEmptyMessage(2);
                          } else
                          {
                              handler.sendEmptyMessage(4);
                          }
                      }
                  }
              }
          }.start();


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        int itemsLastIndex = adapert.getCount() - 1;    //数据集最后一项的索引
        int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex)
        {

            //如果是自动加载,可以在这里放置异步加载数据的代码
            Log.i("LOADMORE", "loading...");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }


    private void jiexi() throws JSONException//1为正常加载和刷新，2为加载更多
    {
        if(shua)
        {
            System.out.println("15");
            if(tuangou.size()>0)
            { System.out.println("16");
                tuangou.clear();}
        }
        JSONObject jsonObject = new JSONObject(str);
        if(jsonObject.get("statusMsg").equals("请求成功"))
        {
            System.out.println("17");
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            zonyeshu=jsonObject.getInt("totalPage");
            if(zonyeshu==1)
            {
                System.out.println("18");
                if (lv.getFooterViewsCount() > 0) {
                    lv.removeFooterView(footer);
                }
            }
            for (int i = 0; i < jsonArray.length(); i++)
            {
                hashMap1 = new HashMap<String, String>();
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                hashMap1.put("PicID", jsonObject2.getString("PicID"));
                hashMap1.put("ID",jsonObject2.getString("ID"));
                hashMap1.put("StoreID",jsonObject2.getString("StoreID"));
                hashMap1.put("Title", jsonObject2.getString("Title"));
                hashMap1.put("StartTime", jsonObject2.getString("StartTime"));
                hashMap1.put("EndTime", jsonObject2.getString("EndTime"));
                hashMap1.put("Price", jsonObject2.getString("Price"));
                hashMap1.put("State",jsonObject2.getString("State"));
                hashMap1.put("TruePrice", jsonObject2.getString("TruePrice"));
                hashMap1.put("EndSurplusTime",jsonObject2.getString("EndSurplusTime"));
                hashMap1.put("SurplusTime",jsonObject2.getString("SurplusTime"));
                hashMap1.put("ProductID",jsonObject2.getString("ProductID"));
                hashMap1.put("bian","a");
                hashMap1.put("dao","o");
                tuangou.add(hashMap1);
            }
            System.out.println("19");
            handler.sendEmptyMessage(8);

        }
        else
        {
            Toast.makeText(MiaoShaActivicty.this,"无内容",Toast.LENGTH_SHORT).show();
            if (lv.getFooterViewsCount() > 0) {
                lv.removeFooterView(footer);
            }
            adapert.notifyDataSetChanged();

        }


    }


    @Override
    public void onClick(View v)
    {
     switch (v.getId())
     {
         case R.id.zaijia:
             nextpage++;
             if(nextpage<=zonyeshu){
                 url=Constant.url +"kill/getAllKillProducts?page="+nextpage+"&pageSize=5&siteId="+areaid+"&classId="+fenid;
//             gengduo.setText("loading...");
             shua=false;
             shuju(url,2);
             }
             else
             {
                 Toast.makeText(MiaoShaActivicty.this,"没有数据啦",Toast.LENGTH_SHORT).show();
                 lv.removeFooterView(footer);
             }
             break;
         case R.id.back:
             finish();
             break;
     }

    }

    @Override
    public void onRefresh()
    {

        shua=true;
        nextpage=1;

        if (lv.getFooterViewsCount() > 0) {
            lv.removeFooterView(footer);
        }
        lv.addFooterView(footer);
url=Constant.url +"kill/getAllKillProducts?page=1&pageSize=5&siteId="+areaid+"&classId="+fenid;
        shuju(url,1);
        // 更新完后调用该方法结束刷新
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int postion)
    {
        System.out.println("1");
        biaoti1.clear();
        System.out.println("2");
        for (int i = 0; i < biaoti.size(); i++) {
            hashMap= new HashMap<String,String>();
            hashMap.put("Name", biaoti.get(i).get("Name"));
            hashMap.put("ID", biaoti.get(i).get("ID"));
            hashMap.put("select", "0");
            biaoti1.add(hashMap);
        }
        biaoti.clear();
        System.out.println("3");
        if (biaoti1.get(postion).get("select").equals("0"))
        {
            System.out.println("4");
            for (int i = 0; i < biaoti1.size(); i++) {
                hashMap = new HashMap<String,String>();
                hashMap.put("Name", biaoti1.get(i).get("Name"));
                hashMap.put("ID", biaoti1.get(i).get("ID"));
                if (i == postion) {
                    hashMap.put("select", "1");
                } else {
                    hashMap.put("select", "0");
                }
                biaoti.add(hashMap);
                System.out.println("5");
            }
        } else {
            for (int i = 0; i < biaoti1.size(); i++) {//我再这里吧biao改为biao1不知道对不，
                hashMap= new HashMap<String,String>();
                hashMap.put("Name", biaoti1.get(i).get("Name"));
                hashMap.put("ID", biaoti1.get(i).get("ID"));
                hashMap.put("select", "0");
                biaoti.add(hashMap);
            }
            System.out.println("6");
        }
        mAdapter.notifyDataSetChanged();
        System.out.println("7");
        nextpage=1;
        System.out.println("8");
        shua=true;
        System.out.println("9");
        if (lv.getFooterViewsCount()==0) {
            lv.addFooterView(footer);
            System.out.println("10");
        }

        fenid=biaoti.get(postion).get("ID");
        System.out.println("11");
        url=Constant.url +"kill/getAllKillProducts?page=1&pageSize=5&siteId="+areaid+"&classId="+fenid;
        System.out.println("12");
       shuju(url,1);

    }
    public class MiaoshaAdapert extends BaseAdapter {

        @Override
        public int getCount() {
            return tuangou.size();
        }

        @Override
        public Object getItem(int position) {
            return tuangou.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Viewhoid viewhoid = new Viewhoid ();
            convertView = LayoutInflater.from(MiaoShaActivicty.this).inflate(R.layout.miaosha_item, null);
                viewhoid.biaoti = (TextView) convertView.findViewById(R.id.biaoti);
                viewhoid.miaoshajia = (TextView) convertView.findViewById(R.id.miaojiege);
                viewhoid.zhenshijia = (TextView) convertView.findViewById(R.id.shichangjiege);
                viewhoid.jijiang=(TextView)convertView.findViewById(R.id.jijiang);
                viewhoid.tu = (ImageView) convertView.findViewById(R.id.tu);
                viewhoid.cv_countdownViewTest2 = (CountdownView) convertView.findViewById(R.id.tian);
                viewhoid.dianji = (TextView) convertView.findViewById(R.id.qiang);
            String url = tuangou.get(position).get("PicID");
            options = ImageUtils.setOptions();
            ImageLoader.displayImage(url, viewhoid.tu, options,
                    animateFirstListener);
            viewhoid.biaoti.setText(tuangou.get(position).get("Title"));
            viewhoid.zhenshijia.setText("￥"+tuangou.get(position).get("TruePrice"));
            viewhoid.miaoshajia.setText("￥"+tuangou.get(position).get("Price"));
            viewhoid.zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    String s=tuangou.get(position).get("State");
            int w=Integer.parseInt(s);
            if (w==2)
            {
                viewhoid.dianji.setText("立即抢购");
                viewhoid.dianji.setBackgroundResource(R.drawable.seckillbutton);
                 viewhoid.jijiang.setText("距离结束");
                if(tuangou.get(position).get("bian").equals("a")){
                long time = Long.parseLong(tuangou.get(position).get("SurplusTime"));
                    long curTime = System.currentTimeMillis()/1000;
                    if(tuangou.get(position).get("dao").equals("o"))
                    {
                    zui=curTime+time;
                        tuangou.get(position).put("dao","p");
                    }
                    else
                    {
                        zui=time;
                    }
                    String shijian=String.valueOf(zui);
                    long dao=zui-System.currentTimeMillis()/1000;
                viewhoid.cv_countdownViewTest2.start((dao * 1000));
                 tuangou.get(position).put("SurplusTime",shijian);
                }
                else
                {
                    long time = Long.parseLong(tuangou.get(position).get("EndSurplusTime"));
                    long curTime = System.currentTimeMillis()/1000;
                    if(tuangou.get(position).get("dao").equals("o"))
                    {
                        zui2=curTime+time;
                        tuangou.get(position).put("dao","p");
                    }
                    else
                    {
                        zui2=time;
                    }
                    String shijian=String.valueOf(zui2);
                    long dao=zui2-System.currentTimeMillis()/1000;
                    viewhoid.cv_countdownViewTest2.start((dao * 1000));
                    tuangou.get(position).put("EndSurplusTime", shijian);
                }
                viewhoid.cv_countdownViewTest2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        HashMap<String, String> hashMap = tuangou.get(position);
                        hashMap.put("State", "4");
                        handler.sendEmptyMessage(10);
                    }
                });
            }
           else if(w==4)
            {
                viewhoid.jijiang.setText("距离结束");
                viewhoid.dianji.setText("秒杀结束");
                viewhoid.dianji.setBackgroundResource(R.drawable.seckillbutton1);
                long time=Long.parseLong(tuangou.get(position).get("SurplusTime"));
                viewhoid.cv_countdownViewTest2.start((0));
            }
         else if(w==3)
            {
                viewhoid.jijiang.setText("距离结束");
                viewhoid.dianji.setText("抢光啦");
                long time=Long.parseLong(tuangou.get(position).get("SurplusTime"));
                viewhoid.cv_countdownViewTest2.start(0);
                viewhoid.dianji.setBackgroundResource(R.drawable.seckillbutton1);
            }
         else if(w==1)
            {
                viewhoid.jijiang.setText("距离开始");
                viewhoid.dianji.setText("敬请期待");
                viewhoid.dianji.setBackgroundResource(R.drawable.seckillbutton1);
                long time=Long.parseLong(tuangou.get(position).get("SurplusTime"));
                long curTime = System.currentTimeMillis()/1000;
                if(tuangou.get(position).get("dao").equals("o"))
                {
                    zui1=curTime+time;
                    tuangou.get(position).put("dao","p");
                }
                else
                {
                    zui1=time;
                }
                String shijian=String.valueOf(zui1);
                long dao=zui1-System.currentTimeMillis()/1000;
                viewhoid.cv_countdownViewTest2.start((dao * 1000));
                tuangou.get(position).put("SurplusTime", shijian);
                viewhoid.cv_countdownViewTest2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv)
                    {
                        HashMap<String, String> hashMap = tuangou.get(position);
                        hashMap.put("State", "2");
                        hashMap.put("bian","b");
                        hashMap.put("dao","o");
                        handler.sendEmptyMessage(10);
                    }
                });

            }
            return convertView;
        }

        public class Viewhoid {
            ImageView tu;
            TextView biaoti, miaoshajia, zhenshijia, dianji,jijiang;
            CountdownView cv_countdownViewTest2;

        }
    }

}
