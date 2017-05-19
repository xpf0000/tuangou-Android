package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.AsyncImageLoader.ImageCallBack;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard.ImageCallBack2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class MeetingPe extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	DisplayMetrics dm;
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	public MeetingPe(ArrayList<HashMap<String, String>> abscure_list,
			Context context) {
		this.abscure_list = abscure_list;
		this.context = context;
		dm = new DisplayMetrics();
		ImageLoader=new AsyncImageLoader();
		asyncImageLoadersdcard=new AsyncImageLoadersdcard();
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
		abscure.metpname = (TextView) convertView.findViewById(R.id.metpname);
		abscure.photo_txt=(TextView)convertView.findViewById(R.id.photo_txt);
		String url = abscure_list.get(position).get("disImg");
		abscure.photo_txt.setText("截止："+abscure_list.get(position).get("endTime").substring(0, 10));
		abscure.metpname.setText(abscure_list.get(position).get("title"));
//		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
//				 dm.widthPixels/4);
//		 abscure.metphead.setLayoutParams(params);
		if(url==null||url.equals("")){
			abscure.metphead.setImageResource(R.drawable.ic_launcher);
		}else{
			System.out.println(url);
			ImageView imageView=abscure.metphead;
			Bitmap bitmap = asyncImageLoadersdcard.loadBitmap(imageView, url,
					new ImageCallBack2() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if(bitmap==null){
								imageView.setImageResource(R.drawable.ic_launcher);;
							}else{
								BitmapDrawable bitmapDrawable=new BitmapDrawable(bitmap);
								imageView.setBackgroundDrawable(bitmapDrawable);
							}

						}
					});
			if(bitmap==null){
				Bitmap bitmap2=ImageLoader.loadBitmap(imageView, url,
						new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if(bitmap==null){
							imageView.setImageResource(R.drawable.ic_launcher);;
						}else{
							BitmapDrawable bitmapDrawable=new BitmapDrawable(bitmap);
							imageView.setBackgroundDrawable(bitmapDrawable);
						}

					}
				});
				if(bitmap2==null){
					abscure.metphead.setBackgroundResource(R.drawable.ic_launcher);
				}else{
					BitmapDrawable bitmapDrawable=new BitmapDrawable(bitmap2);
					abscure.metphead.setBackgroundDrawable(bitmapDrawable);
				}
			}else{
				BitmapDrawable bitmapDrawable=new BitmapDrawable(bitmap);
				abscure.metphead.setBackgroundDrawable(bitmapDrawable);
			}
		}
		return convertView;
	}

	class Abscure {
		TextView metpname,photo_txt;
		ImageView metphead;
	}
}
