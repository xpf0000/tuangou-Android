package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Privileadpater extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.X.tcbj.utils.ImageUtils ImageUtils;
	ImageLoadingListener animateFirstListener;
	public Privileadpater(ArrayList<HashMap<String, String>> abscure_list,
			Context context) {
		this.abscure_list = abscure_list;
		this.context = context;
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
	}

	public int getCount() {
		return abscure_list.size();
	}

	public Object getItem(int position) {
		return abscure_list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Abscure abscure = null;

		abscure = new Abscure();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.baseadpater, parent, false);
		abscure.goods_name = (TextView) convertView
				.findViewById(R.id.privile_txt);
		abscure.goods_number = (RatingBar) convertView
				.findViewById(R.id.app_ratingbar);
		abscure.txt_msg = (TextView) convertView
				.findViewById(R.id.privile_txt_msg);
		abscure.iscard = (ImageView) convertView.findViewById(R.id.iscard);
		abscure.isvip = (ImageView) convertView.findViewById(R.id.isvip);
		abscure.isisauth = (ImageView) convertView.findViewById(R.id.istui);
		abscure.storeimg=(ImageView)convertView.findViewById(R.id.img);
		abscure.renzheng=(ImageView)convertView.findViewById(R.id.renzheng);
		convertView.setTag(abscure);
		abscure = (Abscure) convertView.getTag();
		String vip = abscure_list.get(position).get("isvip");
		String card = abscure_list.get(position).get("iscard");
		String isauth = abscure_list.get(position).get("isauth");
         String jian=abscure_list.get(position).get("isrec");
		String url = abscure_list.get(position).get("imgurl").toString();
		abscure.goods_name.setText(abscure_list.get(position).get("name"));
		abscure.txt_msg.setText(abscure_list.get(position).get("className")+" "+abscure_list.get(position).get("quan"));
		ImageView imageView = abscure.storeimg;
		imageView.setTag(url);
		options=ImageUtils.setnoOptions();
		ImageLoader.displayImage(url, abscure.storeimg, options,
				animateFirstListener);
//		if(bitmap == null)
//        {
//			imageView.setImageResource(R.drawable.ic_launcher);
//        }
//        else
//        {
//        	imageView.setImageBitmap(bitmap);
//
//        }
		abscure.goods_number.setRating(Float.parseFloat(abscure_list.get(position).get("starnum")));
		if (vip.equals("2")) {
			abscure.isvip.setVisibility(View.VISIBLE);
		} else {
			abscure.isvip.setVisibility(View.GONE);
		}
		if (isauth.equals("2")) {
			abscure.renzheng.setVisibility(View.VISIBLE);
		} else {
			abscure.renzheng.setVisibility(View.GONE);
		}
		if (card.equals("2")) {
			abscure.iscard.setVisibility(View.VISIBLE);
		} else {
			abscure.iscard.setVisibility(View.GONE);
		}
		if(jian.equals("1"))
		{
			abscure.isisauth.setVisibility(View.VISIBLE);
		}else
		{
			abscure.isisauth.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Abscure {
		TextView goods_name, txt_msg;
		ImageView isvip, iscard, isisauth,storeimg,renzheng;
		RatingBar goods_number;
	}
}
