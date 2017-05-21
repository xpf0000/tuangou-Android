package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 商家详情中用户评论BaseAdapter
 * 
 * @author zmz
 * 
 */
public class BusinessDetailsAdapter extends BaseAdapter {
	ArrayList<HashMap<String, String>> list;
	Context context;
	private AsyncImageLoader ImageLoader;
	public BusinessDetailsAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		ImageLoader = new AsyncImageLoader();
		System.out.println(list);
	}

	/**
	 * 获取数组长度
	 */
	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		MyHolder myHolder = null;
		if (arg1 == null) {
			myHolder = new MyHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.activity_shop_discuss_list_item, null);
			// 每创建一个view就同时创建一些特定的控件
			myHolder.txt_name = (TextView) arg1
					.findViewById(R.id.txt_business_item_name);
			myHolder.txt_time = (TextView) arg1
					.findViewById(R.id.txt_business_item_time);
			myHolder.txt_comment = (TextView) arg1
					.findViewById(R.id.txt_business_item_comment);
			myHolder.bar_user = (RatingBar) arg1
					.findViewById(R.id.rating_item_user_comment);
			myHolder.img_picture = (ImageView) arg1
					.findViewById(R.id.img_comment_list);
			arg1.setTag(myHolder);// 把myHolder对象放到convertView中
		} else {
			// 若convertView不为空，那么就对存在的控件进行操作
			myHolder = (MyHolder) arg1.getTag();
		}
		// 为控件赋值
		String imgPath = list.get(arg0).get("PicUrl");
		String url = "0";
		JSONArray array = JSONArray.parseArray(imgPath);
		try {
			if (array.size() == 0) {
				myHolder.img_picture.setVisibility(View.GONE);
			}else{
				for (int i = 0; i < 1; i++) {
					myHolder.img_picture.setVisibility(View.VISIBLE);
					JSONObject jsonObject=array.getJSONObject(0);
					url=jsonObject.getString("picUrl");
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			myHolder.img_picture.setVisibility(View.GONE);
			
		}
		myHolder.txt_name.setText(list.get(arg0).get("user"));
		myHolder.txt_time.setText(list.get(arg0).get("date"));
		myHolder.txt_comment.setText(list.get(arg0).get("text"));
		int startNum = Integer
				.parseInt(list.get(arg0).get("number").toString());
		myHolder.bar_user.setRating(startNum);// 为评分条设置值
		int photoInt = PreferencesUtils.getInt(context, "photo");
		if (photoInt == 1) {
			// 加载图片
			if(url.equals("0")){
				
			}else{
				Bitmap bitmap = ImageLoader.loadBitmap(myHolder.img_picture, url
						, new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						bitmap=resizeImage(bitmap, 320, 240);
						BitmapDrawable drawable = new BitmapDrawable(bitmap);
						imageView.setBackgroundDrawable(drawable);

					}

				});
				if (bitmap == null) {
					myHolder.img_picture.setBackgroundResource(R.drawable.head);
				} else {
					bitmap=resizeImage(bitmap, 320, 240);
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					myHolder.img_picture.setBackgroundDrawable(drawable);

				}
			}
			
		} else {
			myHolder.img_picture.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.head));

		}

		
		return arg1;
	}

	final class MyHolder {
		// 放入控件
		TextView txt_name, txt_time, txt_comment;
		RatingBar bar_user;
		ImageView img_picture;

	}
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

}
