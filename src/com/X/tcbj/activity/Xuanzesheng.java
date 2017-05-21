package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.X.tcbj.adapter.xuanzechengshiAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/19.
 */
public class Xuanzesheng extends Activity
{
    private ListView lv;
    private ArrayList<HashMap<String,String>> quyu;
    private String url;
    private String str;
    private String type;
    private String mingzi,address,dianhua,youbian;
    JSONArray jsonArray;
    private ImageView back;
    Dialog dialog;
    HashMap<String,String>hashMap;
    private xuanzechengshiAdapter adapter;
    private  String freeId;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Xuanzesheng.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(Xuanzesheng.this,"请求失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
                   dialog.dismiss();
                    break;
            }
        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
            if(jsonObject.get("statusMsg").equals("请求成功"))
        {
              jsonArray= jsonObject.getJSONArray("list");
            for ( int i=0;i<jsonArray.length();i++)
            {
                hashMap=new HashMap<String, String>();
                JSONObject jsonObject1=(JSONObject)jsonArray.get(i);
                   hashMap.put("Name", jsonObject1.getString("Name"));
                quyu.add(hashMap);
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
        setContentView(R.layout.xuanzhesheng);
        init();
    }

    private void init()
    {

        freeId=getIntent().getStringExtra("freeId");
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url= Constant.url +"/area/getAllArea";
       lv=(ListView)findViewById(R.id.lv);
      mingzi=getIntent().getStringExtra("mingzi");
        address=getIntent().getStringExtra("address");
      dianhua=getIntent().getStringExtra("dianhua");
        youbian=getIntent().getStringExtra("youbian");
        type=getIntent().getStringExtra("type");
        quyu=new ArrayList<HashMap<String, String>>();
        adapter=new xuanzechengshiAdapter(quyu,Xuanzesheng.this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(Xuanzesheng.this, Xuanzeshi.class);
                if (mingzi != null) {
                    intent.putExtra("mingzi", mingzi);
                }
                if (dianhua != null) {
                    intent.putExtra("dianhua", dianhua);
                }
                if (address != null) {
                    intent.putExtra("address", address);
                }
                if (youbian != null) {
                    intent.putExtra("youbian", youbian);
                }
                intent.putExtra("shen", quyu.get(position).get("Name"));
                intent.putExtra("type", type);
                try {
                    intent.putExtra("js", jsonArray.get(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("freeId", freeId);
                Xuanzesheng.this.startActivity(intent);
                finish();


            }
        });
        dialog = GetMyData.createLoadingDialog(Xuanzesheng.this,
                "正在拼命的加载······");
        dialog.show();
        lianwang(url);

    }

    private void lianwang(final String url)
    {
        new Thread() {

            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                str=httpRequest.doGet(url,Xuanzesheng.this);
                if(str.equals("网络超时"))
                {
                    handler.sendEmptyMessage(1);
                }

                else

                {
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }
}
