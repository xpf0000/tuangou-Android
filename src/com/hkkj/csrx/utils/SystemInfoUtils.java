package com.hkkj.csrx.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Display;

public class SystemInfoUtils {

	public static Rect getDefaultImageBounds(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		int width =(int) display.getWidth()*5/4;
		int height = width/2;
		System.out.println("width:"+width+"height"+height);

		Rect bounds = new Rect(0, 0, width, height);
		return bounds;
	}

}
