package com.hkkj.csrx.utils;

import com.hkkj.csrx.activity.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;
                   
    public URLDrawable(Context context) {
        this.setBounds(SystemInfoUtils.getDefaultImageBounds(context));
                       
        drawable = context.getResources().getDrawable(R.drawable.head);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable  
                .getIntrinsicHeight());
    }
                   
    @Override
    public void draw(Canvas canvas) {
        Log.d("test", "this=" + this.getBounds());
        if (drawable != null) {
            Log.d("test", "draw=" + drawable.getBounds());
            drawable.draw(canvas);
        }
    }
                   
}
