package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hkkj.csrx.activity.R;

/**
 * 区域列表
 * 
 * @author zmz
 * 
 */
public class ParentListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> list;
	private Context context;

	// private LayoutInflater inflater;

	public ParentListAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.list = list;
		this.context = context;
		// this.inflater = LayoutInflater.from(context);
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
					R.layout.parent_list_item, null);
			myHolder.text = (TextView) convertView
					.findViewById(R.id.txt_praent_item);
			convertView.setTag(myHolder);
		} else {
			myHolder = (MyHolder) convertView.getTag();
		}
		myHolder.text.setText((CharSequence) list.get(position).get("name"));
		if (Boolean.parseBoolean(list.get(position).get("check"))) {
			myHolder.text.setBackgroundResource(R.drawable.parentitembc1);
		} else {
			myHolder.text.setBackgroundResource(R.drawable.childitembtnbg1);
		}
		return convertView;
	}

	public class MyHolder {
		TextView text;
	}
}
