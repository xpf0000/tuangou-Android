package com.hkkj.csrx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ViewHolder")
public class SemAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.hkkj.csrx.utils.ImageUtils ImageUtils ;
	ImageLoadingListener animateFirstListener;	private Context context;
//	DisplayMetrics dm;
	public SemAdapter(ArrayList<HashMap<String, String>> items,
			Context context) {
		this.abscure_list = items;
		this.context = context;
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return abscure_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return abscure_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		dm = new DisplayMetrics();
//		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		 final Abscure abscure = new Abscure();
		 convertView=LayoutInflater.from(context).inflate(R.layout.sem_item,
				 parent, false);
		 abscure.semimg=(ImageView)convertView.findViewById(R.id.semimg);
		 abscure.semtime=(TextView)convertView.findViewById(R.id.semtime);
		 abscure.semtit=(TextView)convertView.findViewById(R.id.semtit);
		 abscure.semtime.setText(abscure_list.get(position).get("addTime").substring(0, 10));
		 abscure.semtit.setText(abscure_list.get(position).get("title"));
		 final String path = abscure_list.get(position).get("picture").toString();
		WindowManager wm = (WindowManager)context
				.getSystemService(Context.WINDOW_SERVICE);
		double whs = (wm.getDefaultDisplay().getWidth())*(0.95);
		double hts = whs * (3.0 / 10.0);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)whs, (int) hts);
		abscure.semimg.setLayoutParams(layoutParams);
		options=ImageUtils.setcenterOptions();
		int a= PreferencesUtils.getInt(context, "photo");
		if (a==1){
			ImageLoader.displayImage(path, abscure.semimg, options,
					animateFirstListener);
		}else {
			String urls=  ImageLoader.getDiscCache().get(path).getPath();
			boolean bloo= ImageUtils.fileIsExists(urls);
			if (bloo){
				ImageLoader.displayImage(path, abscure.semimg, options,
						animateFirstListener);
			}else {
				abscure.semimg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageLoader.displayImage(path, abscure.semimg, options,
								animateFirstListener);
						abscure.semimg.setClickable(false);
					}
				});
			}
		}
		return convertView;
	}

	class Abscure {
		TextView semtit, semtime;
		ImageView semimg;

	}
}
