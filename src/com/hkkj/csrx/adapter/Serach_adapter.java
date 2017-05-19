package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.Keyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Serach_adapter extends BaseAdapter{
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	String keyword;
	public Serach_adapter(ArrayList<HashMap<String, String>> abscure_list,
			Context context,String keyword) {
		this.abscure_list = abscure_list;
		this.context = context;
		this.keyword=keyword;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Abscure abscure = new Abscure();
		if (arg1 == null) {
			abscure = new Abscure();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.news_s_item, arg2, false);
			abscure.add = (TextView) arg1
					.findViewById(R.id.new_s_tit);
			abscure.tim = (TextView) arg1
					.findViewById(R.id.new_s_tim);
			arg1.setTag(abscure);
		} else {
			abscure = (Abscure) arg1.getTag();
		}
		Keyword keywords=new Keyword();
		
		abscure.tim.setText(abscure_list.get(arg0).get("addTime"));
		abscure.add.setText(keywords.putstr(keyword, abscure_list.get(arg0).get("title")));
		return arg1;
	}
	class Abscure {
		TextView add, tim;
	}

}
