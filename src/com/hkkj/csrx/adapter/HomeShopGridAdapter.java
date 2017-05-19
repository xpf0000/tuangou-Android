package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard;
import com.hkkj.csrx.utils.AsyncImageLoader.ImageCallBack;
import com.hkkj.csrx.utils.AsyncImageLoadersdcard.ImageCallBack2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeShopGridAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> list;
	private Context context;
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;

	public HomeShopGridAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		ImageLoader = new AsyncImageLoader();
		asyncImageLoadersdcard = new AsyncImageLoadersdcard();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyHolder myHolder = null;
		if (convertView == null) {
			myHolder = new MyHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item_shop, null);
			myHolder.img_shop = (ImageView) convertView
					.findViewById(R.id.shop_img);
			myHolder.txt_mane = (TextView) convertView
					.findViewById(R.id.shop_name);
			myHolder.txt_type = (TextView) convertView
					.findViewById(R.id.shop_type);
			convertView.setTag(myHolder);
		} else {
			// 如果convertView不为空 那么就对存在的控件进行操作
			myHolder = (MyHolder) convertView.getTag();
		}
		// 为控件赋值
		if (list.size() >= 0) {
			final String imgPath = list.get(position).get("PicUrl").toString();// 获取图片地址
			// System.out.println("商家图片地址：" + imgPath);
			int photoInt = PreferencesUtils.getInt(context, "photo");
			myHolder.img_shop.setTag(list.get(position).get("PicUrl"));

			Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(
					myHolder.img_shop, list.get(position).get("PicUrl")
							.toString(), new ImageCallBack2() {
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (imageView.getTag() != null
									&& imageView.getTag().equals(imgPath)) {
								BitmapDrawable drawable = new BitmapDrawable(
										bitmap);
								imageView.setBackgroundDrawable(drawable);
							}
						}
					});
			if (bitmap1 == null) {
				if (photoInt == 1) {
					// 加载图片
					myHolder.img_shop.setTag(list.get(position).get("PicUrl"));
					Bitmap bitmap = ImageLoader.loadBitmap(myHolder.img_shop,
							list.get(position).get("PicUrl").toString(),
							new ImageCallBack() {

								@Override
								public void imageLoad(ImageView imageView,
										Bitmap bitmap) {
									if (imageView.getTag() != null
											&& imageView.getTag().equals(
													imgPath)) {
										BitmapDrawable drawable = new BitmapDrawable(
												bitmap);
										imageView
												.setBackgroundDrawable(drawable);
										// imageView.setImageBitmap(bitmap);
									}
								}

							});
					if (bitmap == null) {
						myHolder.img_shop
								.setBackgroundResource(R.drawable.head);
					} else {
						BitmapDrawable drawable = new BitmapDrawable(bitmap);
						myHolder.img_shop.setBackgroundDrawable(drawable);

					}
				} else {
					myHolder.img_shop.setBackgroundDrawable(context
							.getResources().getDrawable(R.drawable.head));
					myHolder.img_shop.setTag(list.get(position).get("PicUrl"));
					final ImageView imageView = myHolder.img_shop;
					myHolder.img_shop.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bitmap bitmap = ImageLoader
									.loadBitmap(imageView, list.get(position)
											.get("PicUrl").toString(),
											new ImageCallBack() {
												public void imageLoad(
														ImageView imageView,
														Bitmap bitmap) {
													if (imageView.getTag() != null
															&& imageView
																	.getTag()
																	.equals(imgPath)) {
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
								imageView.setBackgroundDrawable(drawable);

							}

						}
					});

				}
			} else {
				BitmapDrawable drawable = new BitmapDrawable(bitmap1);
				myHolder.img_shop.setBackgroundDrawable(drawable);
			}
			myHolder.txt_mane.setText(list.get(position).get("Name"));
			myHolder.txt_type.setText(list.get(position).get("SubClassName"));
		}

		return convertView;
	}

	final class MyHolder {
		// 放入控件
		ImageView img_shop;
		TextView txt_mane, txt_type;
	}

}
