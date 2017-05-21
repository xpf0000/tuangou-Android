package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoadersdcard;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;
import com.X.tcbj.utils.AsyncImageLoadersdcard.ImageCallBack2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Homestoreadapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> list;
	private Context context;
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	public Homestoreadapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.list = list;
		this.context = context;
		ImageLoader = new AsyncImageLoader();
		asyncImageLoadersdcard = new AsyncImageLoadersdcard();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		MyHolder myHolder = null;
		if (arg1 == null) {
			myHolder = new MyHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.homes_item, null);
			myHolder.h_s_img = (ImageView) arg1
					.findViewById(R.id.h_s_img);
			myHolder.h_s_adss=(TextView)arg1.findViewById(R.id.h_s_adss);
			myHolder.h_s_com=(TextView)arg1.findViewById(R.id.h_s_com);
			myHolder.h_s_phone=(TextView)arg1.findViewById(R.id.h_s_phone);
			myHolder.h_s_tag=(TextView)arg1.findViewById(R.id.h_s_tag);
			myHolder.h_title=(TextView)arg1.findViewById(R.id.h_title);
			myHolder.h_steat=(RatingBar)arg1.findViewById(R.id.h_steat);
			arg1.setTag(myHolder);
		} else {
			myHolder = (MyHolder) arg1.getTag();
		}
		myHolder.h_s_adss.setText( list.get(arg0).get("areaCircle"));
		myHolder.h_title.setText(list.get(arg0).get("appName"));
		myHolder.h_s_com.setText(list.get(arg0).get("comment")+"人次");
		myHolder.h_s_phone.setText(list.get(arg0).get("telephone"));
		int s=Integer.parseInt(list.get(arg0).get("starNum"));
		myHolder.h_s_tag.setText(list.get(arg0).get("tag"));
		if(s>5){
			myHolder.h_steat.setRating(5);
		}else{
			myHolder.h_steat.setRating(s);
		}
				
		final String path = list.get(arg0).get("logoPicId").toString();
		int a = PreferencesUtils.getInt(context, "photo");
		myHolder.h_s_img.setBackgroundResource(R.drawable.head);
		Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(
				myHolder.h_s_img, list.get(arg0).get("logoPicId"),
				new ImageCallBack2() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (imageView.getTag() != null
								&& imageView.getTag().equals(path)) {
							BitmapDrawable drawable = new BitmapDrawable(
									bitmap);
							imageView.setBackgroundDrawable(drawable);
						}
					}
				});
		if (bitmap1 == null) {
			if (a == 1) {
				myHolder.h_s_img.setTag(list.get(arg0).get("logoPicId"));
				myHolder.h_s_img.setBackgroundResource(R.drawable.head);
				Bitmap bitmap = ImageLoader.loadBitmap(myHolder.h_s_img,
						list.get(arg0).get("logoPicId"), new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (imageView.getTag() != null
										&& imageView.getTag().equals(path)) {
									
									if(bitmap==null){
										imageView.setBackgroundResource(R.drawable.head);
									}else{
										BitmapDrawable drawable = new BitmapDrawable(
												bitmap);
										imageView
										.setBackgroundDrawable(drawable);
									}
									
									
								}
							}

						});
				if (bitmap == null) {
					myHolder.h_s_img
							.setBackgroundResource(R.drawable.head);
				} else {
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
//					myHolder.h_s_img.setBackgroundDrawable(drawable);
					myHolder.h_s_img.setBackgroundResource(R.drawable.head);
				}
			}else {
				myHolder.h_s_img.setBackgroundResource(R.drawable.head);
				myHolder.h_s_img.setTag(list.get(arg0).get("logoPicId"));
				final ImageView imageView = myHolder.h_s_img;
				myHolder.h_s_img
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap bitmap = ImageLoader.loadBitmap(
								imageView,
								list.get(arg0).get("logoPicId"),
								new ImageCallBack() {

									@Override
									public void imageLoad(
											ImageView imageView,
											Bitmap bitmap) {
										if (imageView.getTag() != null
												&& imageView
														.getTag()
														.equals(path)) {
											BitmapDrawable drawable = new BitmapDrawable(
													bitmap);
											imageView
													.setBackgroundDrawable(drawable);
										}
									}

								});
						if (bitmap == null) {
							imageView
									.setBackgroundResource(R.drawable.head);
						} else {
							BitmapDrawable drawable = new BitmapDrawable(
									bitmap);
							imageView
									.setBackgroundDrawable(drawable);

						}

					}
				});
			}
		}else {
			BitmapDrawable drawable = new BitmapDrawable(bitmap1);
			myHolder.h_s_img.setBackgroundDrawable(drawable);
			
		}
		
		return arg1;
	}
	public class MyHolder {
		ImageView h_s_img;
		TextView h_s_com,h_s_tag,h_s_phone,h_s_adss,h_title;
		RatingBar h_steat;
	}

}
