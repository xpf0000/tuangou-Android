package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Mycollectadpater extends BaseAdapter {
	private ArrayList<Map<String, String>> abscure_list;
	private Context context;
	private AsyncImageLoader ImageLoader;
	private ListView listView;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public Mycollectadpater(ArrayList<Map<String, String>> items,
			Context context, ListView listView) {
		this.abscure_list = items;
		this.context = context;
		this.listView = listView;
		ImageLoader = new AsyncImageLoader();
		asyncImageLoadersdcard = new AsyncImageLoadersdcard();
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		Abscure abscure = null;
		if (arg1 == null) {
		abscure = new Abscure();
		arg1 = LayoutInflater.from(context).inflate(R.layout.mycomment_txt,
				arg2, false);
		abscure.shop_name = (TextView) arg1.findViewById(R.id.comment_txt);
		abscure.box = (CheckBox) arg1.findViewById(R.id.mycommect_check);
		abscure.shop_img = (ImageView) arg1.findViewById(R.id.comment_img);
		abscure.shop_v = (ImageView) arg1.findViewById(R.id.shop_vip);
		abscure.shop_card = (ImageView) arg1.findViewById(R.id.shop_card);
		abscure.shop_tui = (ImageView) arg1.findViewById(R.id.shop_tui);
		abscure.collect_time = (TextView) arg1.findViewById(R.id.comment_time);
		arg1.setTag(abscure);
		}else {
			abscure = (Abscure) arg1.getTag();
		}
		abscure.shop_name.setText(abscure_list.get(arg0).get("StoreName"));
		abscure.collect_time.setText(abscure_list.get(arg0).get(
				"CollectAddTime"));
		String StoreOrAuthentication = abscure_list.get(arg0).get(
				"StoreOrAuthentication");
		String StoreOrVip = abscure_list.get(arg0).get("StoreOrVip");
		String StoreOrCard = abscure_list.get(arg0).get("StoreOrCard");
		int a = PreferencesUtils.getInt(context, "photo");
		final String path = abscure_list.get(arg0).get("StoreLogoPicUrl").toString();
		final ImageView imageView = abscure.shop_img;
		imageView.setTag(path);
		Bitmap bitmap1=asyncImageLoadersdcard.loadBitmap(imageView, path, new ImageCallBack2() {
			
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
	        	imageView.setBackgroundDrawable(drawable);
				
			}
		});
		if(bitmap1==null){
			if(a==1){
				Bitmap bitmap=ImageLoader.loadBitmap(imageView, path, new ImageCallBack(){

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						BitmapDrawable drawable = new BitmapDrawable(bitmap);
			        	imageView.setBackgroundDrawable(drawable); 
						
					}
					
				});
				if(bitmap == null)    
		        {    

		        	imageView.setBackgroundResource(R.drawable.head);    
		        }    
		        else    
		        {    
		        	BitmapDrawable drawable = new BitmapDrawable(bitmap);
		        	imageView.setBackgroundDrawable(drawable);    
		        }    
			}else{
				abscure.shop_img.setBackgroundResource(R.drawable.head);
				abscure.shop_img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Bitmap bitmap=ImageLoader.loadBitmap(imageView, path, new ImageCallBack(){

							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap) {
								BitmapDrawable drawable = new BitmapDrawable(bitmap);
					        	imageView.setBackgroundDrawable(drawable);   
								
							}
							
						});
						if(bitmap == null)    
				        {    
							imageView.setBackgroundResource(R.drawable.head);
				        }    
				        else    
				        {    
				        	BitmapDrawable drawable = new BitmapDrawable(bitmap);
				        	imageView.setBackgroundDrawable(drawable);   
				        }    
						
					}
				});
			}
		}else{
			BitmapDrawable drawable = new BitmapDrawable(bitmap1);
        	imageView.setBackgroundDrawable(drawable);
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
		if (abscure_list.get(arg0).get("a").toString().equals("0")) {
			abscure.box.setVisibility(View.GONE);

		} else {
			abscure.box.setVisibility(View.VISIBLE);
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

	public final class Abscure {
		TextView shop_name;
		TextView collect_time;
		ImageView shop_img;
		ImageView shop_v;
		ImageView shop_card;
		ImageView shop_tui;
		public CheckBox box;
	}

}
