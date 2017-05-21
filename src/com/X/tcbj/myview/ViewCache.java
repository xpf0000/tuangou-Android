package com.X.tcbj.myview;

import com.X.tcbj.activity.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {

    private View baseView;
    private TextView textView;
    private ImageView imageView;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }

    public TextView getTextView() {
        if (textView == null) {
            textView = (TextView) baseView.findViewById(R.id.photo_txt);
        }
        return textView;
    }

    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.photonews);
        }
        return imageView;
    }

}