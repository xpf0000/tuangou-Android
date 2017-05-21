package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Mycommentadpater extends BaseAdapter {
	private ArrayList<Map<String, String>> abscure_list;
	private Context context;
	private AsyncImageLoader ImageLoader;
	private ListView listView;

	public Mycommentadpater(ArrayList<Map<String, String>> items,
			Context context,ListView listView) {
		this.abscure_list = items;
		this.context = context;
		this.listView=listView;
		ImageLoader=new AsyncImageLoader();
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
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Abscure abscure = null;
//		if (arg1 == null) {
			abscure = new Abscure();
			arg1 = LayoutInflater.from(context).inflate(R.layout.mycomment_txt,
					arg2, false);
			abscure.shop_name = (TextView) arg1.findViewById(R.id.comment_txt);
			Abscure.shop_img = (ImageView) arg1.findViewById(R.id.comment_img);
			abscure.shop_v = (ImageView) arg1.findViewById(R.id.shop_vip);
			abscure.shop_card = (ImageView) arg1.findViewById(R.id.shop_card);
			abscure.shop_tui = (ImageView) arg1.findViewById(R.id.shop_tui);
			abscure.collect_time = (TextView) arg1
					.findViewById(R.id.comment_time);
			arg1.setTag(abscure);

//		} else {
			abscure = (Abscure) arg1.getTag();
//		}
		abscure.shop_name.setText(abscure_list.get(arg0).get("StoreName"));
		abscure.collect_time.setText(abscure_list.get(arg0).get(
				"CollectAddTime"));
		String StoreOrAuthentication = abscure_list.get(arg0).get(
				"StoreOrAuthentication");
		String StoreOrVip = abscure_list.get(arg0).get("StoreOrVip");
		String StoreOrCard = abscure_list.get(arg0).get("StoreOrCard");
		String path = abscure_list.get(arg0).get("StoreLogoPicUrl").toString();
		ImageView imageView = Abscure.shop_img;
		imageView.setTag(path);
		Bitmap bitmap=ImageLoader.loadBitmap(imageView, path, new ImageCallBack(){

			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);  
				
			}
			
		});
		if(bitmap == null)    
        {    
			imageView.setImageResource(R.drawable.ic_launcher);    
        }    
        else    
        {    
        	imageView.setImageBitmap(bitmap);    
        }    
		if (StoreOrAuthentication.equals("2")) {
			abscure.shop_v.setVisibility(View.VISIBLE);
		} else {
			abscure.shop_v.setVisibility(View.GONE);
		}
		if (StoreOrVip.equals("2")) {
			abscure.shop_tui.setVisibility(View.VISIBLE);
		} else {
			abscure.shop_tui.setVisibility(View.GONE);
		}
		if (StoreOrCard.equals("2")) {
			abscure.shop_card.setVisibility(View.VISIBLE);
		} else {
			abscure.shop_card.setVisibility(View.GONE);
		}
		return arg1;
	}

	public static class Abscure {
		TextView shop_name;
		TextView collect_time;
		static ImageView shop_img;
		ImageView shop_v;
		ImageView shop_card;
		ImageView shop_tui;
	}

}
