package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.X.tcbj.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 商圈列表
 * 
 * @author zmz
 * 
 */
public class ChildListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> list;
	private Context context;

	public ChildListAdapter(ArrayList<HashMap<String, String>> list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHolder myHolder = null;
		if (convertView == null) {
			myHolder = new MyHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.child_list_item, null, false);
			myHolder.text = (TextView) convertView
					.findViewById(R.id.txt_child_item);
			convertView.setTag(myHolder);

		} else {
			myHolder = (MyHolder) convertView.getTag();
		}
		myHolder.text.setText((CharSequence) list.get(position).get("quan"));
		if (Boolean.parseBoolean(list.get(position).get("check"))) {
			myHolder.text.setTextColor(context.getResources().getColor(
					R.color.text_pressed));
		} else {
			myHolder.text.setTextColor(context.getResources().getColor(
					R.color.text_unpressed));
		}
		return convertView;

	}

	public class MyHolder {
		TextView text;
	}
}
