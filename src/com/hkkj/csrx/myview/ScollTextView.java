package com.hkkj.csrx.myview;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import java.util.List;

/**
 * 作者：liuwanlin
 * 创建日期：16/7/1
 */
public class ScollTextView extends BaseScollTextView<SpannableStringBuilder> {
    public ScollTextView(Context context) {
        super(context);
    }

    public ScollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public SpannableStringBuilder getItemText(List<SpannableStringBuilder> data, int postion) {
        return data.get(postion);
    }
}
