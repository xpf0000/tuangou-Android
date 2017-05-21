package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.X.tcbj.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Attbbsadapter extends BaseAdapter{
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	public Attbbsadapter(ArrayList<HashMap<String, String>> abscure_list,
			Context context) {
		this.abscure_list = abscure_list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return abscure_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return abscure_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		Abscure abscure = null;
		abscure = new Abscure();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.attgrid, parent, false);
		abscure.att_count=(TextView)convertView.findViewById(R.id.att_count);
		abscure.att_title=(TextView)convertView.findViewById(R.id.att_title);
		abscure.layout=(LinearLayout)convertView.findViewById(R.id.attlayout);
		if(arg0==abscure_list.size()-1){
			abscure.layout.removeAllViews();
			abscure.layout.setBackgroundResource(R.drawable.att_bbs_bg2);
		}else{
			abscure.att_title.setText(abscure_list.get(arg0).get("name"));
			abscure.att_count.setText(abscure_list.get(arg0).get("todayposts"));	
		}
		
		return convertView;
	}
	class Abscure {
		TextView att_title, att_count;
		ImageView img;
		LinearLayout layout;
	}
}
