package com.hkkj.csrx.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.FlowLayout;
import com.hkkj.csrx.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/22.
 */
public class MallsmalAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    Handler handler=new Handler();
    public MallsmalAdapter(Context context, ArrayList<HashMap<String, String>> list,Handler handler) {
        this.abscure_list = list;
        this.context = context;
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
        array=new ArrayList<HashMap<String, String>>();
        convertView = LayoutInflater.from(context).inflate(R.layout.samllclass, null);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.threeclass=(FlowLayout)convertView.findViewById(R.id.threeclass);
        getItemView.title.setText(abscure_list.get(position).get("Name"));
        String string=abscure_list.get(position).get("ThreeLevel");
        setArray(string);
        int color = context.getResources().getColor(R.color.grarytxt);
        getItemView.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.productClassId=abscure_list.get(position).get("ID");
                Constant.name=getItemView.title.getText().toString();
                handler.sendEmptyMessage(5);
            }
        });
        for (int i=0;i<array.size();i++){
            TextView btnLesson =new TextView(context);
            btnLesson.setTextColor(color);
            btnLesson.setPadding(0,0,getItemView.title.getPaddingBottom(),getItemView.title.getPaddingBottom());
            btnLesson.setId(i);
            btnLesson.setText(array.get(i).get("Name"));
            btnLesson.setOnClickListener(new btnListener(btnLesson,position));
            getItemView.threeclass.addView(btnLesson);
        }
        return convertView;
    }

    private class getItemView {
        TextView title;
        FlowLayout threeclass;
    }
    private void setArray(String str){
        JSONArray jsonArray= JSON.parseArray(str);
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            hashMap=new HashMap<String, String>();
            hashMap.put("ID", jsonObject.getString("ID") == null ? "" : jsonObject.getString("ID"));
            hashMap.put("Name", jsonObject.getString("Name") == null ? "" : jsonObject.getString("Name"));
            array.add(hashMap);
        }
    }
    private class btnListener implements View.OnClickListener{
        private TextView btn;
        int position;
        private btnListener(TextView btn,int position)
        {
            this.btn = btn;//将引用变量传递给实体变量
            this.position=position;
        }
        @Override
        public void onClick(View v) {
          int a=v.getId();
            array.clear();
            String string=abscure_list.get(position).get("ThreeLevel");
            setArray(string);
           Constant.productClassId=  array.get(a).get("ID");
            Constant.name=btn.getText().toString();
            handler.sendEmptyMessage(5);
        }
    }


}