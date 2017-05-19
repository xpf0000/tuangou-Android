package com.hkkj.csrx.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.csrx.data.PreferencesUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 各种公用方法类
 * 
 * @author zmz 2014-07-24
 * 
 */
public class Tools {
	/**
	 * 设置listView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + 40
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 双击退出函数
	 */
	public static Boolean isExit = false;

	public static void exitBy2Click(Context context) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			PreferencesUtils.putString(context, "privilekey", "全部区域");
			PreferencesUtils.putString(context, "privilekeys", "默认排序");
			// finish();
			System.exit(0);
		}
	}
}
