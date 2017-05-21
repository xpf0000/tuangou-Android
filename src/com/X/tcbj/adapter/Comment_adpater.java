package com.X.tcbj.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.CircleImageView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

public class Comment_adpater extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> abscure_list;
	private Context context;
	Handler handler = new Handler();
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.X.tcbj.utils.ImageUtils ImageUtils ;
	ImageLoadingListener animateFirstListener;
	private android.view.animation.Animation animation;

	public Comment_adpater(ArrayList<HashMap<String, Object>> items,
			Context context, Handler handler) {
		this.abscure_list = items;
		this.context = context;
		this.handler = handler;
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
	}

	@Override
	public int getCount() {

		return abscure_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub

		return abscure_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Abscure abscure = new Abscure();
		ShareSDK.initSDK(context);
		arg1 = LayoutInflater.from(context).inflate(R.layout.news_comment_item,
				arg2, false);
		abscure.comment_author = (TextView) arg1
				.findViewById(R.id.comment_author);
		abscure.comment_head = (CircleImageView) arg1
				.findViewById(R.id.comment_head);
		abscure.math = (TextView) arg1.findViewById(R.id.math);
		abscure.comment_time = (TextView) arg1.findViewById(R.id.comment_time);
		abscure.likejia1 = (ImageView) arg1.findViewById(R.id.likejia1);
		abscure.info_comment = (TextView) arg1.findViewById(R.id.info_comment);
		abscure.zanlay = (LinearLayout) arg1.findViewById(R.id.zanlay);
		abscure.tv_one = (TextView) arg1.findViewById(R.id.tv_one);
		abscure.comment_author.setText(abscure_list.get(arg0).get("nickName").toString());
		abscure.comment_time.setText(abscure_list.get(arg0).get("addTime").toString());
		abscure.math.setText(abscure_list.get(arg0).get("likeNum").toString());
		abscure.info_comment.setText(abscure_list.get(arg0).get("contents").toString());
		final String path = abscure_list.get(arg0).get("userPicID").toString();
		options=ImageUtils.setcenterOptions();
		ImageLoader.displayImage(path,abscure.comment_head, options,
				animateFirstListener);
		abscure.zanlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constant.stite == 0) {

				} else {
					int Logn = PreferencesUtils.getInt(context, "logn");
					if (Logn == 0) {
						Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
								.show();
					} else {
						try {
							Constant.commentId = abscure_list.get(arg0).get("id").toString();
						} catch (Exception e) {
						}
//						Constant.commentId = abscure_list.get(arg0).get("id").toString();
						handler.sendEmptyMessage(4);
						// abscure.tv_one.setVisibility(View.VISIBLE);
						// abscure.tv_one.startAnimation(animation);
						// new Handler().postDelayed(new Runnable() {
						// public void run() {
						// abscure.tv_one.setVisibility(View.GONE);
						// abscure.math.setText(Integer.parseInt(abscure_list.get(arg0).get("likeNum"))+1+"");
						// }
						// }, 1000);
					}
				}

			}
		});
		return arg1;
	}

	class Abscure {
		TextView comment_author, comment_time, math, info_comment, tv_one;
		CircleImageView comment_head;
		ImageView likejia1;
		LinearLayout zanlay;
	}

}
