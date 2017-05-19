package com.hkkj.csrx.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.widget.TextView;

public class URLImageGetter implements ImageGetter {
    Context context;
    TextView textView;
                  
    public URLImageGetter(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }
                      
    @Override
    public Drawable getDrawable(String paramString) {
        final URLDrawable urlDrawable = new URLDrawable(context);
                          
        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
    }
                      
    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;
                          
        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }
                          
        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;
                                  
                URLImageGetter.this.textView.requestLayout();
            }
        }
                          
        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }
                          
        public Drawable fetchDrawable(String url) {

                              
            return null;
        }

    }
                      
}
