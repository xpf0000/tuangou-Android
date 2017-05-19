package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.MymianfeiAdapter;
import com.hkkj.csrx.adapter.mianfeishiyongAdapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/16.
 */
public class MianfeiShiyongActivicty extends Activity implements MyItemClickListener,SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener,View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String,String>> biaoti;
    private ArrayList<HashMap<String,String>> biaoti1;
    private ArrayList<HashMap<String,String>> mianfei;
    private MymianfeiAdapter mAdapter;
    private mianfeishiyongAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    String biaotiurl,str,str1;
    ListView lv;
    String id;
    String url;
    Boolean shua=true;
    RelativeLayout dianjizaijia;
    HashMap<String,String>hashMap;
    HashMap<String,String>hashMap1;
    int nextpage=1;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private View loadMoreView;
    private TextView gengduo;
    private ImageView back;
    private   int   zonyeshu;
    private int areaid;
    private  int width2,height2;

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(MianfeiShiyongActivicty.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    if (lv.getFooterViewsCount() > 0) {
                        lv.removeFooterView(loadMoreView);
                    }

                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(MianfeiShiyongActivicty.this,"请求失败",Toast.LENGTH_SHORT).show();
                      break;
                case 4:
                    mAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    adapter.notifyDataSetChanged();
                    break;
                case 7:
                    Toast.makeText(MianfeiShiyongActivicty.this,"无内容",Toast.LENGTH_SHORT).show();
                    if (lv.getFooterViewsCount() > 0) {
                        lv.removeFooterView(loadMoreView);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 8:
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }
    };

    private void jiexi1()  throws JSONException
    {
        if(shua)
        {
            mianfei.clear();
        }
         JSONObject jsonObject = new JSONObject(str1);
        if(jsonObject.get("statusMsg").equals("请求成功"))
        {
            JSONArray jsonArray=jsonObject.getJSONArray("list");
                  zonyeshu= Integer.parseInt(jsonObject.getString("totalPage"));
            if(zonyeshu==1)
            {
                if (lv.getFooterViewsCount() > 0) {
                    lv.removeFooterView(loadMoreView);
                }
            }
            for(int q=0;q<jsonArray.length();q++)
            {
                JSONObject jsonObject1=(JSONObject)jsonArray.get(q);
                hashMap1=new HashMap<String, String>();
                hashMap1.put("SubmitNum",jsonObject1.getString("SubmitNum"));
                hashMap1.put("FreeNum",jsonObject1.getString("FreeNum"));
                hashMap1.put("SurplusTime", jsonObject1.getString("SurplusTime"));
                hashMap1.put("State",jsonObject1.getString("State"));
                hashMap1.put("PicID",jsonObject1.getString("PicID"));
                hashMap1.put("Title", jsonObject1.getString("Title"));
                hashMap1.put("ID",jsonObject1.getString("ID"));
                mianfei.add(hashMap1);
            }
            handler.sendEmptyMessage(6);
        }else
        {
            handler.sendEmptyMessage(7);
        }

    }

    private void jiexi() throws JSONException
    {

        JSONObject jsonObject = new JSONObject(str);
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
                handler.sendEmptyMessage(4);
        }
        else
        {
         handler.sendEmptyMessage(3);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mianfeishiyong);
        areaid = PreferencesUtils.getInt(MianfeiShiyongActivicty.this, "cityID");
        biaotiurl=Constant.url +"free/getAllFreeClass?siteId="+areaid;
        initView();

    }

    private void initView()
    {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
         width2 = outMetrics.widthPixels;
         height2 = outMetrics.heightPixels;

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        loadMoreView = getLayoutInflater().inflate(R.layout.footer, null);
        gengduo = (TextView) loadMoreView.findViewById(R.id.gengduo);
        dianjizaijia=(RelativeLayout)loadMoreView.findViewById(R.id.zaijia);
        dianjizaijia.setOnClickListener(this);
        id="0";//默认为全选
        url= Constant.url +"free/getAllFree?siteId="+areaid+"&page=1&pageSize=5&classId="+id;
        biaoti=new ArrayList<HashMap<String, String>>();
        biaoti1=new ArrayList<HashMap<String, String>>();
        mianfei=new ArrayList<HashMap<String, String>>();
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv=(ListView)findViewById(R.id.lv);
        lv.addFooterView(loadMoreView);
        adapter=new mianfeishiyongAdapter(mianfei,MianfeiShiyongActivicty.this,width2,height2);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(Integer.parseInt(mianfei.get(position).get("State"))==1)
                {
                    Intent intent=new Intent();
                    intent.setClass(MianfeiShiyongActivicty.this,Mianfei_jieshu.class);
                    intent.putExtra("ID",mianfei.get(position).get("ID"));
                    MianfeiShiyongActivicty.this.startActivity(intent);
                }
                if(Integer.parseInt(mianfei.get(position).get("State"))==2)
                {
                    Intent intent=new Intent();
                    intent.setClass(MianfeiShiyongActivicty.this,Mianfei_shenqing.class);
                    intent.putExtra("ID",mianfei.get(position).get("ID"));
                    MianfeiShiyongActivicty.this.startActivity(intent);
                }
                if(Integer.parseInt(mianfei.get(position).get("State"))==3)
                {
                    Intent intent=new Intent();
                    intent.setClass(MianfeiShiyongActivicty.this,Mianfei_tijiaobaogao.class);
                    intent.putExtra("ID",mianfei.get(position).get("ID"));
                    MianfeiShiyongActivicty.this.startActivity(intent);
                }
                if(Integer.parseInt(mianfei.get(position).get("State"))==4)
                {
                    Intent intent=new Intent();
                    intent.setClass(MianfeiShiyongActivicty.this,Mianfei_jijiangkaishi.class);
                    intent.putExtra("ID",mianfei.get(position).get("ID"));
                    MianfeiShiyongActivicty.this.startActivity(intent);
              }



            }
        });
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()) ;
        this.mAdapter = new MymianfeiAdapter(biaoti);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mAdapter.setOnItemClickListener(this);
        initData(biaotiurl);
      init(url,1);
    }

    private void init(final String url, final int type)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
                str1=httpRequest.doGet(url,MianfeiShiyongActivicty.this);
                if(str1.equals("网络超时"))
                {
                    handler.sendEmptyMessage(1);
                }else
                {
                    if(type==1){
                    handler.sendEmptyMessage(5);}
                    else
                    {
                        handler.sendEmptyMessage(8);
                    }
                }

            }
        }.start();

    }


    private void initData(final String url)
    {
  new Thread()
   {
    @Override
    public void run()
    {
        super.run();
        HttpRequest httpRequest=new HttpRequest();
        str=httpRequest.doGet(url,MianfeiShiyongActivicty.this);
        if(str.equals("网络超时"))
        {
          handler.sendEmptyMessage(1);
        }else
        {
            handler.sendEmptyMessage(2);
        }

    }
}.start();


    }

    @Override
    public void onItemClick(View view, int postion)
    {
        biaoti1.clear();
        for (int i = 0; i < biaoti.size(); i++) {
            hashMap= new HashMap<String,String>();
            hashMap.put("Name", biaoti.get(i).get("Name"));
            hashMap.put("ID", biaoti.get(i).get("ID"));
            hashMap.put("select", "0");
            biaoti1.add(hashMap);
        }
        biaoti.clear();

        if (biaoti1.get(postion).get("select").equals("0"))
        {
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
            }
        } else {
            for (int i = 0; i < biaoti1.size(); i++) {//我再这里吧biao改为biao1不知道对不，
                hashMap= new HashMap<String,String>();
                hashMap.put("Name", biaoti1.get(i).get("Name"));
                hashMap.put("ID", biaoti1.get(i).get("ID"));
                hashMap.put("select", "0");
                biaoti.add(hashMap);
            }
        }

        mAdapter.notifyDataSetChanged();
        nextpage=1;
        shua=true;
        if (lv.getFooterViewsCount()==0) {
            lv.addFooterView(loadMoreView);
        }

        id=biaoti.get(postion).get("ID");
        url=Constant.url +"free/getAllFree?siteId="+areaid+"&page="+nextpage+"&pageSize=5&classId="+id;
        init(url, 1);
    }

    @Override
    public void onRefresh()
    {
        nextpage=1;
        shua=true;
        if (lv.getFooterViewsCount() > 0) {
            lv.removeFooterView(loadMoreView);
        }
        lv.addFooterView(loadMoreView);

        url=Constant.url +"free/getAllFree?siteId="+areaid+"&page="+nextpage+"&pageSize=5&classId="+id;
        init(url,1);
        // 更新完后调用该方法结束刷新
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.zaijia:
                nextpage++;
                if(nextpage<=zonyeshu)
                {
                    url=Constant.url +"free/getAllFree?siteId="+areaid+"&page="+nextpage+"&pageSize=5&classId="+id;
                    shua=false;
                //    lv.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
                    init(url,2);
                } else
                {
                    Toast.makeText(MianfeiShiyongActivicty.this,"没有数据啦",Toast.LENGTH_SHORT).show();
                   lv.removeFooterView(loadMoreView);
                }
                break;
            case R.id.back:
               finish();

                break;
        }

    }
}

