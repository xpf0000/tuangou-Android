package com.hkkj.csrx.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

;

public class isexist {

	public static boolean exist(String imageURL) {
		String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
		File cacheDir = new File("/data/data/com.hkkj.csrx.activity/files/");
		File[] cacheFiles = cacheDir.listFiles();
		int i = 0;
		if (null != cacheFiles) {
			for (; i < cacheFiles.length; i++) {
				if (bitmapName.equals(cacheFiles[i].getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public static void saveImage(final Context context, final String imageURL,final commonCallBack callback) {

		new Thread() {
			public void run() {
				String ur = context.getFilesDir().getPath() + "/"
						+ imageURL.substring(imageURL.lastIndexOf("/") + 1);
				File file = new File(ur);
				if (file.exists()) {
					file.delete();
				}

				try {
					// 从网络上获取图片
					URL url = new URL(imageURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					if (conn.getResponseCode() == 200) {

						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						is.close();
						fos.close();
						callback.doSameThing();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};

		}.start();
	}



}

