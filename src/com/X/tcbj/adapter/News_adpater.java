package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.X.tcbj.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class News_adpater extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	public News_adpater(ArrayList<HashMap<String, String>> abscure_list,
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return abscure_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Abscure abscure = null;
		abscure = new Abscure();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.news_ad, parent, false);
		abscure.att_title=(Button)convertView.findViewById(R.id.myattbt);
		abscure.att_title.setText(abscure_list.get(position).get("className"));
		return convertView;
	}
	class Abscure {
		Button att_title;
	}

}
