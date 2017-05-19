package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Commentadpater extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public Commentadpater(ArrayList<HashMap<String, String>> items,
			Context context) {
		this.abscure_list = items;
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		Abscure abscure = null;
		if (arg1 == null) {
			abscure = new Abscure();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.mycollect_text, arg2, false);
			abscure.shop_name = (TextView) arg1.findViewById(R.id.commentt_txt);
			abscure.zhanwei=(TextView)arg1.findViewById(R.id.meiyong1);
			abscure.zhanwei2=(TextView)arg1.findViewById(R.id.meiyong2);
			abscure.box = (CheckBox) arg1.findViewById(R.id.mycollect_check);
			abscure.comment_time = (TextView) arg1
					.findViewById(R.id.mycomment_time);
			abscure.comment = (TextView) arg1.findViewById(R.id.comment_xtx);
			abscure.ratingbar_comment = (RatingBar) arg1
					.findViewById(R.id.rating_commentt);
			abscure.comment_vip = (ImageView) arg1
					.findViewById(R.id.commentt_vip);
			abscure.auth = (ImageView) arg1
					.findViewById(R.id.auth);
			abscure.img_shop_item_ren = (ImageView) arg1
					.findViewById(R.id.img_shop_item_ren);
			abscure.comment_tui = (ImageView) arg1
					.findViewById(R.id.commentt_tui);
			abscure.comment_card = (ImageView) arg1
					.findViewById(R.id.commentt_card);
			arg1.setTag(abscure);
		} else {
			abscure = (Abscure) arg1.getTag();
		}
		abscure.comment_time.setText(abscure_list.get(arg0).get("AddTime"));
		abscure.shop_name.setText(abscure_list.get(arg0).get("StoreName"));
		abscure.comment.setText(abscure_list.get(arg0).get("CommentContents"));
		String vip = abscure_list.get(arg0).get("StoreOrVip");
		String card = abscure_list.get(arg0).get("StoreOrCard");
		String star=abscure_list.get(arg0).get("CommentStar");
		int mystar=Integer.parseInt(star);
		String tui = abscure_list.get(arg0).get("storeisrec");
		String auth=abscure_list.get(arg0).get("StoreOrAuthentication");
		if(mystar<6){
			abscure.ratingbar_comment.setRating(mystar);
		}else{
			abscure.ratingbar_comment.setRating(5);
		}
		if (vip.endsWith("2")) {
			abscure.comment_vip.setVisibility(View.VISIBLE);
		} else {
			abscure.comment_vip.setVisibility(View.GONE);
		}
		if (card.endsWith("2")) {
			abscure.comment_card.setVisibility(View.VISIBLE);
		} else {
			abscure.comment_card.setVisibility(View.GONE);
		}
		if (tui.endsWith("1")) {
			abscure.comment_tui.setVisibility(View.VISIBLE);
		} else {
			abscure.comment_tui.setVisibility(View.GONE);
		}
		if (auth.endsWith("2")) {
			abscure.auth.setVisibility(View.VISIBLE);
		} else {
			abscure.auth.setVisibility(View.GONE);
		}
		if (abscure_list.get(arg0).get("a").toString().equals("0")) {
			abscure.box.setVisibility(View.GONE);
			abscure.zhanwei.setVisibility(View.GONE);
			abscure.zhanwei2.setVisibility(View.GONE);

		} else {
			abscure.box.setVisibility(View.VISIBLE);
			abscure.zhanwei.setVisibility(View.VISIBLE);
			abscure.zhanwei2.setVisibility(View.VISIBLE);
		}
		abscure.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					state.put(arg0, isChecked);
				} else {
					state.remove(arg0);
				}

			}
		});
		abscure.box.setChecked((state.get(arg0) == null ? false : true));
		return arg1;
	}

	class Abscure {
		TextView shop_name, comment_time, comment,zhanwei,zhanwei2;
		ImageView comment_vip, comment_tui, comment_card,img_shop_item_ren,auth;
		RatingBar ratingbar_comment;
		CheckBox box;
	}

}
