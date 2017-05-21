package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.X.tcbj.activity.Info_info;
import com.X.tcbj.activity.Photo_newsinfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.webiew;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;
import com.X.tcbj.utils.AsyncImageLoadersdcard;
import com.X.tcbj.utils.AsyncImageLoadersdcard.ImageCallBack2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Myviewpageadapater extends PagerAdapter {

	private List<View> mList;
//	private AsyncImageLoaderview asyncImageLoader;
	private Context context;
	ImageView image;
	String[] urls;
	ArrayList<HashMap<String, String>> imgarray;
	int a = 0;
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	public Myviewpageadapater(List<View> list, Context context, String[] urls,
			ArrayList<HashMap<String, String>> imgarray, int stat) {
		this.mList = list;
		this.context = context;
		this.urls = urls;
		this.imgarray = imgarray;
		this.a = stat;
//		asyncImageLoader = new AsyncImageLoaderview();
		ImageLoader=new AsyncImageLoader();
		asyncImageLoadersdcard=new AsyncImageLoadersdcard();
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/**
	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position) This method was deprecated
	 * in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mList.get(position));

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
//		Drawable cachedImage = asyncImageLoader.loadDrawable(urls[position],
//				new ImageCallback() {
//
//					public void imageLoaded(Drawable imageDrawable,
//							String imageUrl) {
//
//						View view = mList.get(position);
//						image = (ImageView) view.findViewById(R.id.viewimage);
//						image.setBackgroundDrawable(imageDrawable);
//						container.removeView(mList.get(position));
//						container.addView(mList.get(position));
//						// adapter.notifyDataSetChanged();
//
//					}
//				});
		Bitmap bitmap=asyncImageLoadersdcard.loadBitmap(image, urls[position], new ImageCallBack2() {

			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				View view = mList.get(position);
				image = (ImageView) view.findViewById(R.id.viewimage);
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				image.setBackgroundDrawable(drawable);
				container.removeView(mList.get(position));
				container.addView(mList.get(position));
			}
		});
		if(bitmap==null){
			Bitmap bitmap2=ImageLoader.loadBitmap(image, urls[position], new ImageCallBack() {

				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					View view = mList.get(position);
					image = (ImageView) view.findViewById(R.id.viewimage);
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					image.setBackgroundDrawable(drawable);
					container.removeView(mList.get(position));
					container.addView(mList.get(position));

				}
			});
		}
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		View view = mList.get(position);
		image = ((ImageView) view.findViewById(R.id.viewimage));
		image.setBackgroundDrawable(drawable);
		if (a == 1) {
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!imgarray.get(position).get("url").equals("#")){
						Intent intent = new Intent();
						intent.setClass(context, webiew.class);
						intent.putExtra("url", imgarray.get(position).get("url").toString());
						context.startActivity(intent);
					}
				}
			});
		} else {
			mList.get(position).findViewById(R.id.viewimage).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if (imgarray.get(position).get("orPpt").equals("true")) {
						intent.putExtra("iD", imgarray.get(position).get("iD"));
						intent.putExtra("newsClassID", imgarray.get(position)
								.get("newsClassID"));
						intent.setClass(context, Photo_newsinfo.class);
						context.startActivity(intent);
					} else {
						intent.putExtra("id", imgarray.get(position).get("iD"));
						intent.putExtra("newsClassID", imgarray.get(position)
								.get("classID"));
						intent.setClass(context, Info_info.class);
						context.startActivity(intent);
					}
				}
			});
		}
		container.removeView(mList.get(position));
		container.addView(mList.get(position));


		return mList.get(position);

	}

}
