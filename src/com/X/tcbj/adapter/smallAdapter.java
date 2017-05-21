package com.X.tcbj.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/23.
 */
public class smallAdapter extends BaseAdapter
{
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    Handler handler=new Handler();
    public smallAdapter(Context context, ArrayList<HashMap<String, String>> list,Handler handler) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.small_list, null);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.title.setText(abscure_list.get(position).get("Name"));
//        String string=abscure_list.get(position).get("ThreeLevel");
//        setArray(string);
//        int color = context.getResources().getColor(R.color.grarytxt);
        getItemView.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.productClassId1=abscure_list.get(position).get("ClassList");
//                Constant.name1=getItemView.title.getText().toString();
                handler.sendEmptyMessage(5);
            }
        });
//        for (int i=0;i<array.size();i++){
//            TextView btnLesson =new TextView(context);
//            btnLesson.setTextColor(color);
//            btnLesson.setPadding(0,0,getItemView.title.getPaddingBottom(),getItemView.title.getPaddingBottom());
//            btnLesson.setId(i);
//            btnLesson.setText(array.get(i).get("Name"));
//            btnLesson.setOnClickListener(new btnListener(btnLesson,position));
//            getItemView.threeclass.addView(btnLesson);
//        }
        return convertView;
    }

    private class getItemView {
        TextView title;

    }
}
