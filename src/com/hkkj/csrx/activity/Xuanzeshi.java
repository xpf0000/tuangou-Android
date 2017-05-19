package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hkkj.csrx.adapter.xuanzechengshiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/19.
 */
public class Xuanzeshi extends Activity
{
    private String jsonArray;
    JSONArray jsonArray1;
    String shen;
    private String mingzi,address,dianhua,youbian,freeId;
    private ArrayList<HashMap<String,String>> quyu;
    HashMap<String,String>hashMap;
    private xuanzechengshiAdapter adapter;
    private ListView lv;
    private String type;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuanzhesheng);
        init();
    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        freeId=getIntent().getStringExtra("freeId");
        quyu=new ArrayList<HashMap<String, String>>();
        adapter=new xuanzechengshiAdapter(quyu,Xuanzeshi.this);
        jsonArray=getIntent().getStringExtra("js");
        shen=getIntent().getStringExtra("shen");
        mingzi=getIntent().getStringExtra("mingzi");
        address=getIntent().getStringExtra("address");
        dianhua=getIntent().getStringExtra("dianhua");
        youbian=getIntent().getStringExtra("youbian");
        type=getIntent().getStringExtra("type");
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(adapter);
        try {
            jiexi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent();
                intent.setClass(Xuanzeshi.this,Xuanzexian.class);
                if(mingzi!=null)
                {
                    intent.putExtra("mingzi",mingzi);
                }
                if(dianhua!=null)
                {
                    intent.putExtra("dianhua",dianhua);
                }
                if(address!=null)
                {
                    intent.putExtra("address",address);
                }
                if(youbian!=null)
                {
                    intent.putExtra("youbian",youbian);
                }
                intent.putExtra("shi",quyu.get(position).get("Name"));
                intent.putExtra("shen",shen);
                intent.putExtra("type",type);
                try {
                    intent.putExtra("js",jsonArray1.get(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("freeId",freeId);
                Xuanzeshi.this.startActivity(intent);
                finish();

            }
        });
    }

    private void jiexi() throws JSONException
    {
     JSONObject jsonObject=new JSONObject(jsonArray);
         jsonArray1=jsonObject.getJSONArray("Citys");
        for (int i=0;i<jsonArray1.length();i++) {
            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
            hashMap = new HashMap<String, String>();
            hashMap.put("Name", jsonObject1.getString("Name"));
            quyu.add(hashMap);
        }
        adapter.notifyDataSetChanged();

    }

}
