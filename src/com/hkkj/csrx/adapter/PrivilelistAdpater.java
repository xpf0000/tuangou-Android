package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PrivilelistAdpater extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.hkkj.csrx.utils.ImageUtils ImageUtils;
	ImageLoadingListener animateFirstListener;
	private ListView listView;

	public PrivilelistAdpater(ArrayList<HashMap<String, String>> abscure_list,
			Context context, ListView listView) {
		this.abscure_list = abscure_list;
		this.context = context;
		this.listView = listView;
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		Abscure abscure = new Abscure();
		if (convertView == null) {
			abscure = new Abscure();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.privilege_list_txt, parent, false);
			abscure.txt = (TextView) convertView
					.findViewById(R.id.privile_list_txt);
			abscure.add = (TextView) convertView
					.findViewById(R.id.privile_list_txtadd);
			abscure.tim = (TextView) convertView
					.findViewById(R.id.privile_list_txttim);
			abscure.storeimg = (ImageView) convertView
					.findViewById(R.id.privile_list_img);
			abscure.txt_shop_item_appe = (TextView) convertView
					.findViewById(R.id.txt_shop_item_appe);
			convertView.setTag(abscure);
		} else {
			abscure = (Abscure) convertView.getTag();
		}
		abscure.txt.setText(abscure_list.get(position).get("Title"));
		abscure.add.setText(abscure_list.get(position).get("Address"));
		abscure.tim.setText(abscure_list.get(position).get("StartTime").substring(0,10) + "~"
				+ abscure_list.get(position).get("EndTime").substring(0,10));
		int a = PreferencesUtils.getInt(context, "photo");
		final String imgPath = abscure_list.get(position).get("PicUrl").toString();
		abscure.storeimg.setTag(abscure_list.get(position).get("PicUrl"));
		abscure.storeimg.setBackgroundResource(R.drawable.head);
		int photoInt = PreferencesUtils.getInt(context, "photo");
	try {
		double Map_Longitude = Double.parseDouble(abscure_list.get(position)
				.get("Map_Longitude").toString());
		double Map_Latitude = Double.parseDouble(abscure_list.get(position)
				.get("Map_Latitude").toString());
		double longitude = Double.parseDouble(abscure_list.get(position).get("longitude"));
		double latitude = Double.parseDouble(abscure_list.get(position).get("latitude"));
		LatLng startLatlng = new LatLng(latitude, longitude);
		LatLng endLatlng = new LatLng(Map_Latitude, Map_Longitude);
		float x = (AMapUtils.calculateLineDistance(startLatlng,
				endLatlng)) / 1000;
		String b = x + "";
		abscure.txt_shop_item_appe.setText(b.subSequence(0, 4)
				+ "千米");
	}catch (Exception e){
		abscure.txt_shop_item_appe.setVisibility(View.GONE);
	}
		options=ImageUtils.setnoOptions();
		if (photoInt==1){
			ImageLoader.displayImage(imgPath, abscure.storeimg, options,
					animateFirstListener);
		}else {
			String urls=  ImageLoader.getDiscCache().get(imgPath).getPath();
			boolean bloo= ImageUtils.fileIsExists(urls);
			if (bloo){
				ImageLoader.displayImage(imgPath, abscure.storeimg, options,
						animateFirstListener);
			}else {
				final String finalImgPath = imgPath;
				final Abscure finalMyHolder = abscure;
				abscure.storeimg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageLoader.displayImage(finalImgPath, finalMyHolder.storeimg, options,
								animateFirstListener);
						finalMyHolder.storeimg.setClickable(false);
					}
				});
			}
		}

		return convertView;
	}

	class Abscure {
		TextView add, tim, txt,txt_shop_item_appe;
		ImageView storeimg;
	}
}