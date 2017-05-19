package com.hkkj.csrx.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 二级菜单
 * 
 * @author zmz
 * 
 */
public class FollowListView extends GridView {

	public FollowListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
