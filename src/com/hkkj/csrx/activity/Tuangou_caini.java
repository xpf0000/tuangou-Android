package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/5.
 */
public class Tuangou_caini extends Activity
{
    private ListView lv;
    private String order;
    private String smallAreaID;
    private String smallClassID;
    private String url;
    private String str;
    private myadapter adapter;
    private ArrayList<HashMap<String,String>> caini=new ArrayList<HashMap<String, String>>();
    private HashMap<String,String>hashMap;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Tuangou_caini.this,"网络超时",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(Tuangou_caini.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
                case  4:
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    private void jiexi() throws org.json.JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
          if(jsonObject.getString("statusMsg").equals("请求成功"))
          {
              JSONArray jsonArray=jsonObject.getJSONArray("list");
              for(int i=0;i<jsonArray.length();i++)
              {
                  JSONObject jsonObject1=(JSONObject)jsonArray.get(i);
                        hashMap=new HashMap<String, String>();
                  hashMap.put("ID",jsonObject1.getString("ID"));
                  hashMap.put("Intro",jsonObject1.getString("Intro"));
                  hashMap.put("Map",jsonObject1.getString("Map"));
                  hashMap.put("OriginalPrice",jsonObject1.getString("OriginalPrice"));
                  hashMap.put("PeopleCount",jsonObject1.getString("PeopleCount"));
                  hashMap.put("PicID",jsonObject1.getString("PicID"));
                  hashMap.put("Price",jsonObject1.getString("Price"));
                  hashMap.put("Title",jsonObject1.getString("Title"));
                  caini.add(hashMap);
              }
              handler.sendEmptyMessage(4);

          }
          else
          {
              handler.sendEmptyMessage(3);
          }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuangoucaini);
        init();
    }

    private void init()
    {
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Tuangou_caini.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
     order=getIntent().getStringExtra("order");
        smallAreaID=getIntent().getStringExtra("smallAreaID");
        smallClassID=getIntent().getStringExtra("smallClassID");
        url="http://192.168.2.28:8080/HKCityApi/tuan/getYouOrLove?siteId=1&order="+order+"&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
       lv=(ListView)findViewById(R.id.lv);
        adapter=new myadapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent();
                intent.putExtra("id", caini.get(position).get("ID").toString());
                intent.putExtra("smallAreaID",smallAreaID);
                intent.putExtra("smallClassID",smallClassID);
                intent.putExtra("order",order);
                intent.setClass(Tuangou_caini.this, TuangouXiangqing.class);
                Tuangou_caini.this.startActivity(intent);

            }
        });
        lianwang();

    }

    private void lianwang()
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
                str=httpRequest.doGet(url,Tuangou_caini.this);
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

    public class myadapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {

            return caini.size();
        }

        @Override
        public Object getItem(int position) {
            return caini.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final   Viewhoid viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(Tuangou_caini.this).inflate(R.layout.tuangou_item,null);
            viewhoid.tu=(ImageView)convertView.findViewById(R.id.caitu);
            viewhoid.dianname=(TextView)convertView.findViewById(R.id.dianname);
            viewhoid.miaoshu=(TextView)convertView.findViewById(R.id.miaoshu);
            viewhoid.jiage=(TextView)convertView.findViewById(R.id.zhenshijia);
            viewhoid.tuanjia=(TextView)convertView.findViewById(R.id.huodongjia);
            viewhoid.shouchu=(TextView)convertView.findViewById(R.id.maishu);
            String url=caini.get(position).get("PicID").toString();
            options=ImageUtils.setOptions();
            ImageLoader.displayImage(url, viewhoid.tu, options,
                    animateFirstListener);
            viewhoid.dianname.setText(caini.get(position).get("Title").toString());
            viewhoid.miaoshu.setText(caini.get(position).get("Intro").toString());
            viewhoid.tuanjia.setText("￥" + caini.get(position).get("Price").toString());
            viewhoid.jiage.setText("￥" + caini.get(position).get("OriginalPrice").toString());
            viewhoid.jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            viewhoid.shouchu.setText(caini.get(position).get("PeopleCount").toString());
            return convertView;
        }
    }
    public class Viewhoid
    {
        ImageView tu;
        TextView dianname,miaoshu,jiage,tuanjia,shouchu;


    }
}
