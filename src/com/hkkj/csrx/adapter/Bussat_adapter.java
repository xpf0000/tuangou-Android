package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.LoginActivity;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.AsyncImageLoader.ImageCallBack;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard.ImageCallBack2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Bussat_adapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	Handler handler = new Handler();
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;

	public Bussat_adapter(ArrayList<HashMap<String, String>> items,
			Context context, Handler handler) {
		this.abscure_list = items;
		this.context = context;
		this.handler = handler;
		asyncImageLoadersdcard = new AsyncImageLoadersdcard();
		ImageLoader = new AsyncImageLoader();
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
		final Abscure abscure = new Abscure();
		arg1 = LayoutInflater.from(context).inflate(R.layout.bassa_item, arg2,
				false);
		abscure.buss_title = (TextView) arg1.findViewById(R.id.buss_title);
		abscure.buss_renzheng = (TextView) arg1
				.findViewById(R.id.buss_renzheng);
		abscure.buss_time = (TextView) arg1.findViewById(R.id.buss_time);
		abscure.buss_pople = (TextView) arg1.findViewById(R.id.buss_pople);
		abscure.buss_guanzhu = (Button) arg1.findViewById(R.id.buss_guanzhu);
		abscure.imageView = (ImageView) arg1.findViewById(R.id.busa_logo);
		abscure.buss_time.setText(abscure_list.get(arg0).get("addTime"));
		String a = abscure_list.get(arg0).get("review");
		if (a.equals("true")) {
			a = "已认证";
		} else {
			a = "未认证";
		}
		final String path = abscure_list.get(arg0).get("logoId");
		abscure.buss_renzheng.setText(a);
		abscure.buss_title.setText(abscure_list.get(arg0).get("title"));
		abscure.buss_pople.setText("成员数:"
				+ abscure_list.get(arg0).get("counts"));
		final String attid = abscure_list.get(arg0).get("attid");
		if (attid.endsWith("0")) {

			abscure.buss_guanzhu.setBackgroundResource(R.drawable.com_plus);

		} else {

			abscure.buss_guanzhu.setBackgroundResource(R.drawable.com_yes);
		}
		ImageView imageView = abscure.imageView;
		Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(imageView,
				abscure_list.get(arg0).get("logoId"), new ImageCallBack2() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (imageView.getTag() != null
								&& imageView.getTag().equals(path)) {
							BitmapDrawable drawable = new BitmapDrawable(bitmap);
							abscure.imageView.setImageDrawable(drawable);
						}
					}
				});
		if (bitmap1 == null) {
			abscure.imageView.setTag(abscure_list.get(arg0).get("logoId"));
			abscure.imageView.setImageResource(R.drawable.head);
			Bitmap bitmap = ImageLoader.loadBitmap(imageView,
					abscure_list.get(arg0).get("logoId"), new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (imageView.getTag() != null
									&& imageView.getTag().equals(path)) {
								BitmapDrawable drawable = new BitmapDrawable(
										bitmap);
								abscure.imageView.setImageDrawable(drawable);
							}
						}

					});
			if (bitmap == null) {
				abscure.imageView.setImageResource(R.drawable.head);
			} else {
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				abscure.imageView.setImageDrawable(drawable);

			}
		} else {
			BitmapDrawable drawable = new BitmapDrawable(bitmap1);
			abscure.imageView.setImageDrawable(drawable);
		}
		abscure.buss_guanzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constant.attarg2=arg0;
				int Logn=PreferencesUtils.getInt(context, "logn");
				  if (Logn==0)  {
					  Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						context.startActivity(intent);
				  }else{
					  if (attid.equals("0")) {
							Constant.siteid = abscure_list.get(arg0).get("id");
							handler.sendEmptyMessage(5);
						}else{
							handler.sendEmptyMessage(7);
						}
				  }
				

			}
		});
		return arg1;
	}

	class Abscure {
		Button buss_guanzhu;
		TextView buss_title, buss_renzheng, buss_time, buss_pople;
		ImageView imageView;
	}

}
