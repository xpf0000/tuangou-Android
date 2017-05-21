package com.X.tcbj.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SemPhNews extends BaseAdapter{
	private ArrayList<HashMap<String, Object>> abscure_list;
	private Context context;
	DisplayMetrics dm;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.X.tcbj.utils.ImageUtils ImageUtils ;
	ImageLoadingListener animateFirstListener;
	public SemPhNews(ArrayList<HashMap<String, Object>> abscure_list,
			Context context) {
		this.abscure_list = abscure_list;
		this.context = context;
		dm = new DisplayMetrics();
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
		Abscure abscure = null;
		abscure = new Abscure();
		convertView = LayoutInflater.from(context).inflate(R.layout.meetingpe,
				parent, false);
		abscure.metphead = (ImageView) convertView.findViewById(R.id.metphead);
		abscure.metpname=(TextView)convertView.findViewById(R.id.metpname);
		abscure.photo_txt=(TextView)convertView.findViewById(R.id.photo_txt);
		String url = abscure_list.get(position).get("picture").toString();
		abscure.metpname.setVisibility(View.GONE);
		abscure.photo_txt.setText(abscure_list.get(position).get("title").toString());
//		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
//				 dm.widthPixels/4);
//		 abscure.metphead.setLayoutParams(params);
          options=ImageUtils.setcenterOptions();
		ImageLoader.displayImage(url, abscure.metphead, options,
				animateFirstListener);
		return convertView;
	}
	class Abscure {
		TextView photo_txt,metpname;
		ImageView metphead;
	}
}
