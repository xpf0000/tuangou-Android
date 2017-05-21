package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 商家列表BaseAdapter
 * 
 * @author Administrator
 * 
 */
public class MerchantListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> list;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.X.tcbj.utils.ImageUtils ImageUtils ;
	ImageLoadingListener animateFirstListener;

	public MerchantListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
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
//		if (arg1 == null) {
			myHolder = new MyHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.activity_shops_list_item, null);
			myHolder.img_shop = (ImageView) arg1
					.findViewById(R.id.img_shop_item_picture);
			myHolder.img_recommend = (ImageView) arg1
					.findViewById(R.id.img_shop_item_recommend);
			myHolder.img_vip = (ImageView) arg1
					.findViewById(R.id.img_shop_item_vip);
		myHolder.img_shop_item_ren = (ImageView) arg1
				.findViewById(R.id.img_shop_item_ren);
			myHolder.img_card = (ImageView) arg1
					.findViewById(R.id.img_shop_item_card);

			myHolder.rating_shop_list = (RatingBar) arg1
					.findViewById(R.id.rating_shop_item);

			myHolder.txt_id = (TextView) arg1
					.findViewById(R.id.txt_shop_item_id);
			myHolder.txt_shop_name = (TextView) arg1
					.findViewById(R.id.txt_shop_item_name);
			myHolder.txt_address = (TextView) arg1
					.findViewById(R.id.txt_shop_item_address);
			myHolder.txt_area = (TextView) arg1
					.findViewById(R.id.txt_shop_item_area);
			arg1.setTag(myHolder);
			myHolder.txt_shop_item_appe = (TextView) arg1
					.findViewById(R.id.txt_shop_item_appe);
//		} else {
//			myHolder = (MyHolder) arg1.getTag();
//		}
		// 显示商家图片
		if (list.size() >= 0) {
			String imgPath = null ;
			if(list.get(arg0).get("imgURL")==null){
				imgPath="";
			}else{
				imgPath = list.get(arg0).get("imgURL").toString();// 获取图片地址
			}
			options=ImageUtils.setnoOptions();
			int photoInt = PreferencesUtils.getInt(context, "photo");
			if (photoInt==1){
				ImageLoader.displayImage(imgPath, myHolder.img_shop, options,
						animateFirstListener);
			}else {
				String urls=  ImageLoader.getDiscCache().get(imgPath).getPath();
				boolean bloo= ImageUtils.fileIsExists(urls);
				if (bloo){
					ImageLoader.displayImage(imgPath, myHolder.img_shop, options,
							animateFirstListener);
				}else {
					final String finalImgPath = imgPath;
					final MyHolder finalMyHolder = myHolder;
					myHolder.img_shop.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ImageLoader.displayImage(finalImgPath, finalMyHolder.img_shop, options,
									animateFirstListener);
							finalMyHolder.img_shop.setClickable(false);
						}
					});
				}
			}
			try {
				// 判断是否显示VIP图标
				if (list.get(arg0).get("isvip").toString().equals("2")) {

					myHolder.img_vip.setVisibility(View.VISIBLE);
				} else {
					myHolder.img_vip.setVisibility(View.GONE);
				}
				if (list.get(arg0).get("isrec").toString().equals("1")) {

					myHolder.img_recommend.setVisibility(View.VISIBLE);
				} else {
					myHolder.img_recommend.setVisibility(View.GONE);
				}
				// 判断是否显示card图标
				if (list.get(arg0).get("iscard").toString().equals("2")) {

					myHolder.img_card.setVisibility(View.VISIBLE);
				} else {
					myHolder.img_card.setVisibility(View.GONE);
				}
				// 判断是否显示认证图标
				if (list.get(arg0).get("isauth").toString().equals("2")) {
					myHolder.img_shop_item_ren.setVisibility(View.VISIBLE);
				} else {

					myHolder.img_shop_item_ren.setVisibility(View.GONE);
				}
				myHolder.txt_shop_name.setText(list.get(arg0).get("name")
						.toString());// 商家名称
				int startNum = Integer.parseInt(list.get(arg0).get("starnum")
						.toString());
				if (startNum > 5) {
					myHolder.rating_shop_list.setRating(5);
				} else {
					myHolder.rating_shop_list.setRating(startNum);
				}
				// 评分条显示

				// myHolder.txt_address.setVisibility(View.GONE);// 把商家商圈设为不显示
				myHolder.txt_address.setText(list.get(arg0).get("AreaCircle")
						.toString());
				myHolder.txt_area.setText(list.get(arg0).get("ClassName")
						.toString());// 商家所属分类
				if (list.get(arg0).get("Map_Longitude").equals("")
						|| (Double) list.get(arg0).get("longitude") == 0.0) {

				} else {
					double Map_Longitude = Double.parseDouble(list.get(arg0)
							.get("Map_Longitude").toString());
					double Map_Latitude = Double.parseDouble(list.get(arg0)
							.get("Map_Latitude").toString());
					double longitude = (Double) list.get(arg0).get("longitude");
					double latitude = (Double) list.get(arg0).get("latitude");
					LatLng startLatlng = new LatLng(latitude, longitude);
					LatLng endLatlng = new LatLng(Map_Latitude, Map_Longitude);
					float a = (AMapUtils.calculateLineDistance(startLatlng,
							endLatlng)) / 1000;
					String b = a + "";
					myHolder.txt_shop_item_appe.setText(b.subSequence(0, 4)
							+ "千米");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return arg1;
	}

	public class MyHolder {
		ImageView img_shop;
		ImageView img_recommend;
		ImageView img_vip;
		ImageView img_card,img_shop_item_ren;

		RatingBar rating_shop_list;

		TextView txt_id, txt_shop_item_appe;
		TextView txt_shop_name;
		TextView txt_address;
		TextView txt_area;
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
