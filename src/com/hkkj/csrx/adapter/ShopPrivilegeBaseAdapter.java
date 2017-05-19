package com.hkkj.csrx.adapter;

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
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.AsyncImageLoader.ImageCallBack;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard.ImageCallBack2;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 商家优惠列表
 * 
 * @author zmz
 * 
 */
public class ShopPrivilegeBaseAdapter extends BaseAdapter {
	ArrayList<HashMap<String, String>> list;
	Context context;
	ImageLoader imageLoader;
	ImageUtils imageUtils;
	DisplayImageOptions options;
	ImageLoadingListener animateFirstListener;

	public ShopPrivilegeBaseAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		imageUtils = new ImageUtils();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		MyHolder myHolder = null;
		if (arg1 == null) {
			myHolder = new MyHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.privilege_list_txt, null);
			// 每创建一个view就同时创建一些特定的控件
			myHolder.img_picture = (ImageView) arg1
					.findViewById(R.id.privile_list_img);
			myHolder.txt_text = (TextView) arg1
					.findViewById(R.id.privile_list_txt);
			myHolder.txt_address = (TextView) arg1
					.findViewById(R.id.privile_list_txtadd);
			myHolder.txt_data = (TextView) arg1
					.findViewById(R.id.privile_list_txttim);
			arg1.setTag(myHolder);// 把myHolder对象放到convertView中
		} else {
			// 若convertView不为空，那么就对存在的控件进行操作
			myHolder = (MyHolder) arg1.getTag();
		}
		// 为控件赋值
		final String imgUrl = list.get(arg0).get("PicID");
		int photoInt = PreferencesUtils.getInt(context, "photo");// 无图模式状态值

		options = imageUtils.setnoOptions();
		imageLoader.displayImage(imgUrl, myHolder.img_picture, options, animateFirstListener);
		// myHolder.img_picture.setBackgroundDrawable(GetMyData.getImageDownload(
		// context, imgUrl));
		myHolder.txt_text.setText(list.get(arg0).get("Title"));
		myHolder.txt_address.setText(list.get(arg0).get("Address"));
		myHolder.txt_data.setText(list.get(arg0).get("EndTime").substring(0,10)+"前");
		return arg1;
	}

	final class MyHolder {
		// 放入控件
		ImageView img_picture;
		TextView txt_text;
		TextView txt_address;
		TextView txt_data;
	}
}
