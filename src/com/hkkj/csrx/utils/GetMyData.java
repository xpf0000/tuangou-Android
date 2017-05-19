package com.hkkj.csrx.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;

/**
 * 获取网络数据
 * 
 * @author zmz
 * 
 */
public class GetMyData {
	/**
	 * 链接接口获取数据
	 * 
	 * @param path
	 *            接口地址
	 * @return
	 */
	public static String getURLString(String path) {

		return "网络超时";
	}

	public static String getSeachJson(String path, List<Object> params) {
		String strResult = null;

		return strResult;

	}

	/**
	 * 图片下载
	 * 
	 * @param fileURL
	 *            图片地址
	 * @return
	 */
	public static Drawable getImageDownload(Context context, String fileURL) {
		URL url;
		try {
			if (fileURL.equals("null")) {
				return context.getResources().getDrawable(R.drawable.head);
			} else {
				url = new URL(fileURL);
				HttpURLConnection http = (HttpURLConnection) url
						.openConnection();
				InputStream inputStream = http.getInputStream();
				return Drawable.createFromStream(inputStream, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return context.getResources().getDrawable(R.drawable.head);
		}
	}

	/**
	 * 评分条星级计算
	 * 
	 * @param num
	 *            接口返回数量
	 * @return
	 */
	public static int getMyRating(int num) {
		int startNum = num / 2;
		return startNum;
	}

	/**
	 * 访问数据库并返回JSON数据字符串
	 * 
	 * @param params
	 *            向服务器端传的参数
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doPost(List<Object> params, String url)
			throws Exception {
		String result = null;

		return result;
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(true);// 用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	/**
	 * 接收百度sdk返回的数据
	 */
//	public static void setLocationOption(LocationClient mLocClient) {
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true); // 打开gps
//		option.setCoorType("bd009ll"); // 设置坐标类型
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(true);
//		option.setAddrType("all");
//		option.setScanSpan(3000);
//		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
//		option.setPoiNumber(10);
//		option.disableCache(true);
//		mLocClient.setLocOption(option);
//	}

}
