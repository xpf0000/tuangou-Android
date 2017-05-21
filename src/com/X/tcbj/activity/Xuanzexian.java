package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.X.tcbj.adapter.xuanzechengshiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/21.
 */
public class Xuanzexian extends Activity
{
    String shen;
    String shi;
    private String jsonArray;
    private String mingzi,address,dianhua,youbian;
    private ArrayList<HashMap<String,String>> quyu;
    HashMap<String,String>hashMap;
    private xuanzechengshiAdapter adapter;
    private ListView lv;
    private String type;
    private String freeId;
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
        adapter=new xuanzechengshiAdapter(quyu,Xuanzexian.this);
        jsonArray=getIntent().getStringExtra("js");
        shen=getIntent().getStringExtra("shen");
        shi=getIntent().getStringExtra("shi");
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                if (type.equals("one")){
                    intent.setClass(Xuanzexian.this,NewAddressActivity.class);
                }else if (type.equals("two")){
                    intent.setClass(Xuanzexian.this,Shiyong_shenqing.class);
                }

                intent.putExtra("shen",shen);
                intent.putExtra("shi",shi);
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
                intent.putExtra("xian", quyu.get(position).get("Name"));
                String citycode=quyu.get(position).get("ClassList");
                String c=citycode.substring(1,citycode.length()-1);
                intent.putExtra("cityCode",c);
                intent.putExtra("freeId",freeId);
                Xuanzexian.this.startActivity(intent);
                finish();

            }
        });
    }

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(jsonArray);
      JSONArray   jsonArray1=jsonObject.getJSONArray("Countys");
        for (int i=0;i<jsonArray1.length();i++) {
            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
            hashMap = new HashMap<String, String>();
            hashMap.put("Name", jsonObject1.getString("Name"));
            hashMap.put("ClassList",jsonObject1.getString("ClassList"));
            quyu.add(hashMap);
        }
        adapter.notifyDataSetChanged();

    }
}
