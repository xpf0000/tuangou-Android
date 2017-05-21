package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;

/**
 * 首页热门商家
 * 
 * @author zmz
 * 
 */
public class HomeHotPrivilegeAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	private AsyncImageLoader ImageLoader;
	 ListView listView;

	public HomeHotPrivilegeAdapter(
			ArrayList<HashMap<String, String>> abscure_list, Context context,
			ListView listView) {
		this.abscure_list = abscure_list;
		this.context = context;
		this.listView = listView;
		ImageLoader = new AsyncImageLoader();
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
			convertView.setTag(abscure);
		} else {
			abscure = (Abscure) convertView.getTag();
		}
		if (abscure_list.size() > 0) {

			abscure.txt.setText(abscure_list.get(position).get("Name"));
			abscure.add.setText(abscure_list.get(position).get("StoreName"));
			abscure.tim.setText(abscure_list.get(position).get("StartTime")
					+ "~~" + abscure_list.get(position).get("EndTime"));
			int a = PreferencesUtils.getInt(context, "photo");
			final String path = abscure_list.get(position).get("PicUrl")
					.toString();
			if (a == 1) {
				abscure.storeimg.setTag(abscure_list.get(position)
						.get("PicUrl"));
				abscure.storeimg.setBackgroundResource(R.drawable.head);
				Bitmap bitmap = ImageLoader.loadBitmap(abscure.storeimg,
						abscure_list.get(position).get("PicUrl"),
						new ImageCallBack() {
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (imageView.getTag() != null
										&& imageView.getTag().equals(path)) {
									BitmapDrawable drawable = new BitmapDrawable(
											bitmap);
									imageView.setBackgroundDrawable(drawable);
								}
							}
						});
				if (bitmap == null) {
					abscure.storeimg.setBackgroundResource(R.drawable.head);
				} else {
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					abscure.storeimg.setBackgroundDrawable(drawable);
				}
			} else {
				abscure.storeimg.setBackgroundResource(R.drawable.head);
				abscure.storeimg.setTag(abscure_list.get(position)
						.get("PicUrl"));
				final ImageView imageView = abscure.storeimg;
				abscure.storeimg.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						Bitmap bitmap = ImageLoader.loadBitmap(imageView,
								abscure_list.get(position).get("PicUrl"),
								new ImageCallBack() {
									public void imageLoad(ImageView imageView,
											Bitmap bitmap) {
										if (imageView.getTag() != null
												&& imageView.getTag().equals(
														path)) {
											imageView.setImageBitmap(bitmap);
										}
									}
								});
						if (bitmap == null) {
							imageView.setBackgroundResource(R.drawable.head);
						} else {
							BitmapDrawable drawable = new BitmapDrawable(bitmap);
							imageView.setBackgroundDrawable(drawable);
						}
					}
				});
			}
		}
		return convertView;
	}

	class Abscure {
		TextView add, tim, txt;
		ImageView storeimg;
	}
}
