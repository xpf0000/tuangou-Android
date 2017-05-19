package com.hkkj.csrx.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class CompressPictures {
	public Bitmap resize_img(Bitmap bitmap, float pc) {
	    Matrix matrix = new Matrix();
	    Log.i("mylog2", "缩放比例--" + pc);
	    matrix.postScale(pc, pc); // 长和宽放大缩小的比例
	    Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
	            bitmap.getHeight(), matrix, true);
	    
//	    bitmap.recycle();
//	    bitmap = null;
//	    System.gc();
	     
	    int width = resizeBmp.getWidth();
	    int height = resizeBmp.getHeight();
	    Log.i("mylog2", "按比例缩小后宽度--" + width);
	    Log.i("mylog2", "按比例缩小后高度--" + height);
	 
	    return resizeBmp;
	}

}
