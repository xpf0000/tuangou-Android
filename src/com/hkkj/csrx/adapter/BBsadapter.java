package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.Timechange;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BBsadapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	Timechange timechange;

	public BBsadapter(ArrayList<HashMap<String, String>> abscure_list,
			Context context) {
		this.abscure_list = abscure_list;
		this.context = context;
		timechange = new Timechange();
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
		abscure = new Abscure();
		arg1 = LayoutInflater.from(context).inflate(R.layout.bbs_list_item,
				arg2, false);
		abscure.bbs_h_tit = (TextView) arg1.findViewById(R.id.bbs_h_tit);
		abscure.bs_h_author = (TextView) arg1.findViewById(R.id.bs_h_author);
		abscure.bs_h_time = (TextView) arg1.findViewById(R.id.bs_h_time);
		abscure.bs_h_reply = (TextView) arg1.findViewById(R.id.bs_h_reply);
		abscure.bs_h_view = (TextView) arg1.findViewById(R.id.bs_h_view);
		abscure.bbs_photo = (ImageView) arg1.findViewById(R.id.bbs_photo);
		abscure.bbs_hot = (ImageView) arg1.findViewById(R.id.bbs_hot);
		abscure.bbs_h_tit.setText(abscure_list.get(arg0).get("subject"));
		abscure.bs_h_author.setText(abscure_list.get(arg0).get("author"));
		abscure.bs_h_reply.setText(abscure_list.get(arg0).get("replies"));
		abscure.bs_h_view.setText(abscure_list.get(arg0).get("views"));
		abscure.bs_h_time.setText(timechange.Time(abscure_list.get(arg0).get(
				"dateline")));
		String photo = abscure_list.get(arg0).get("attachment");
		String hot = abscure_list.get(arg0).get("heats");
		if (photo.equals("2")) {
			abscure.bbs_photo.setVisibility(View.VISIBLE);
		} else if (hot.equals("1")) {
			abscure.bbs_hot.setVisibility(View.VISIBLE);
		}
		return arg1;
	}

	class Abscure {
		TextView bbs_h_tit, bs_h_author, bs_h_time, bs_h_reply, bs_h_view;
		ImageView bbs_photo, bbs_hot;
	}

}
