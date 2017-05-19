package com.hkkj.csrx.myview;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by admins on 2016/8/17.
 */
public class MyWebview extends WebView {
    public MyWebview(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
