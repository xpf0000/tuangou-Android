package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.Reply_post;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;
import com.X.tcbj.utils.AsyncImageLoadersdcard;
import com.X.tcbj.utils.AsyncImageLoadersdcard.ImageCallBack2;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.Expressions;
import com.X.tcbj.utils.Timechange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class Post_adapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> abscure_list;
	private Context context;
	HashMap<String, String> hashMap;
	private AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	Timechange timechange;
	String tid,url;
	int z=1;
	Handler handler=new Handler() ;
	public Post_adapter(ArrayList<HashMap<String, String>> items,
			Context context, ListView listView, String tid,Handler handler) {
		this.abscure_list = items;
		this.context = context;
		this.tid = tid;
		this.handler=handler;
		asyncImageLoadersdcard = new AsyncImageLoadersdcard();
		ImageLoader = new AsyncImageLoader();
		timechange = new Timechange();
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Abscure abscure = new Abscure();
		ShareSDK.initSDK(context);
		arg1 = LayoutInflater.from(context).inflate(R.layout.post_list_item,
				arg2, false);
		abscure.comment = (TextView) arg1.findViewById(R.id.post_collect_txt);
		abscure.share = (TextView) arg1.findViewById(R.id.post_share_txt);
		abscure.reply = (TextView) arg1.findViewById(R.id.reply);
		abscure.only = (TextView) arg1.findViewById(R.id.post_only_txt);
		abscure.cengreply = (TextView) arg1.findViewById(R.id.cengreply);
		abscure.post_author_txt = (TextView) arg1
				.findViewById(R.id.post_author_txt);
		abscure.post_lou_txt = (TextView) arg1.findViewById(R.id.post_lou_txt);
		abscure.post_only_txt=(TextView)arg1.findViewById(R.id.post_only_txt);
		abscure.post_collect_txt=(TextView)arg1.findViewById(R.id.post_collect_txt);
		abscure.post_time_txt = (TextView) arg1
				.findViewById(R.id.post_time_txt);
		LinearLayout layout, layout2, layout3;
		layout = (LinearLayout) arg1.findViewById(R.id.mypostview);
		layout2 = (LinearLayout) arg1.findViewById(R.id.fristau);
		layout3 = (LinearLayout) arg1.findViewById(R.id.cengview);
		abscure.post_author_txt.setText(abscure_list.get(arg0).get("author"));
		if (arg0 == 0) {
			abscure.post_lou_txt.setText("楼主");
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.GONE);
		} else if (arg0 == 1) {
			abscure.post_lou_txt.setText("沙发");
		} else if (arg0 == 2) {
			abscure.post_lou_txt.setText("板凳");
		} else {
			abscure.post_lou_txt.setText(abscure_list.get(arg0).get("position")
					+ "楼");
		}
		abscure.post_time_txt.setText(timechange.Time(abscure_list.get(arg0)
				.get("dateline")));
		if(z==1){
			abscure.post_only_txt.setText("只看楼主");	
		}else{
			abscure.post_only_txt.setText("取消只看楼主");
			
		}
		String a = abscure_list.get(arg0).get("message");
		try {
			JSONArray jsonArray = new JSONArray(a);
			int temp = -1;
			int index = -1;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				int b = object.getInt("type");
				final String detail = object.getString("info");

				if (b == 2) {

					if (temp != 2) {
						if (index < 0)
							index = i + arg0;
						LinearLayout layout4 = new LinearLayout(
								arg2.getContext());
						layout4.setId(2222 + index);
						layout4.setOrientation(LinearLayout.HORIZONTAL);
						LayoutParams params = new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT);
						layout4.setLayoutParams(params);
						GifView gifView = new GifView(arg2.getContext());
						// LayoutParams params1 = new LayoutParams(
						// 50, 50);
						// gifView.setLayoutParams(params1);
						gifView.setId(i);
						String detail1 = "a" + detail;
						for (int j = 0; j < Expressions.expressionImgNames1.length; j++) {

							if (detail1
									.equals(Expressions.expressionImgNames1[j])) {
								gifView.setShowDimension(300, 300);
								gifView.setGifImage(Expressions.expressionImgs1[j]);
							}
						}
						for (int j = 0; j < Expressions.expressionImgNames2.length; j++) {
							if (detail1
									.equals(Expressions.expressionImgNames2[j])) {
								gifView.setGifImage(Expressions.expressionImgs2[j]);
							}

						}
						for (int j = 0; j < Expressions.expressionImgNames3.length; j++) {
							if (detail1
									.equals(Expressions.expressionImgNames3[j])) {
								gifView.setGifImage(Expressions.expressionImgs3[j]);
							}

						}
						gifView.setGifImageType(GifImageType.COVER);
						layout4.addView(gifView);
						layout.addView(layout4);
						temp = 2;
					} else {

						LinearLayout layout4 = (LinearLayout) layout
								.findViewById(2222 + index);
						String detail1 = "a" + detail;
						GifView gifView = new GifView(arg2.getContext());
						// LayoutParams params1 = new LayoutParams(
						// 30, 30);
						// gifView.setLayoutParams(params1);
						gifView.setId(i);
						for (int j = 0; j < Expressions.expressionImgNames1.length; j++) {
							if (detail1
									.equals(Expressions.expressionImgNames1[j])) {

								gifView.setGifImage(Expressions.expressionImgs1[j]);

							}
						}
						for (int j = 0; j < Expressions.expressionImgNames2.length; j++) {
							if (detail1
									.equals(Expressions.expressionImgNames2[j])) {
								gifView.setGifImage(Expressions.expressionImgs2[j]);

							}

						}
						for (int j = 0; j < Expressions.expressionImgNames3.length; j++) {
							if (detail1
									.equals(Expressions.expressionImgNames3[j])) {
								gifView.setGifImage(Expressions.expressionImgs3[j]);

							}

						}
						gifView.setGifImageType(GifImageType.COVER);
						layout4.addView(gifView);
					}

				} else if (b == 1) {
					temp = 1;
					index = -1;
					url=detail;
					ImageView imageView = new ImageView(arg2.getContext());
					LayoutParams params = new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 5, 0, 5);
					imageView.setId(arg0);
					
					imageView.setLayoutParams(params);
					Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(
							imageView, detail, new ImageCallBack2() {

								@Override
								public void imageLoad(ImageView imageView,
										Bitmap bitmap) {
									if (imageView.getTag() != null
											&& imageView.getTag()
													.equals(detail)) {
										BitmapDrawable drawable = new BitmapDrawable(
												bitmap);
										imageView.setImageDrawable(drawable);
									}
								}
							});

					if (bitmap1 == null) {
						Bitmap bitmap = ImageLoader.loadBitmap(imageView,
								detail, new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView,
											Bitmap bitmap) {
										imageView.setImageBitmap(bitmap);

									}

								});
						if (bitmap == null) {
							imageView.setImageResource(R.drawable.head);
						} else {
							imageView.setImageBitmap(bitmap);

						}
					} else {
						imageView.setImageBitmap(bitmap1);
					}
					layout.addView(imageView);

				} else if(b==3){
					temp = 0;
					index = -1;
					TextView textView = new TextView(arg2.getContext());
					textView.setId(arg0);
					textView.setTextSize(16);
					textView.setText(detail);
					layout.addView(textView);
					textView.setBackgroundColor(Color.parseColor("#EEEEEE"));
				}
				else {
					temp = 0;
					index = -1;
					TextView textView = new TextView(arg2.getContext());
					textView.setId(arg0);
					textView.setTextSize(16);
					textView.setText(detail);
					layout.addView(textView);
				}
				abscure.share.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						OnekeyShare oks = new OnekeyShare();
						int cityID = PreferencesUtils.getInt(context, "cityID");
						oks.disableSSOWhenAuthorize();// 分享前要先授权

						// 分享时Notification的图标和文字
//						oks.setNotification(R.drawable.ic_launcher,
//								context.getString(R.string.app_name));
						System.out.println("detail:"+url);
						if(url==null){
							oks.setImageUrl("http://image.rexian.cn/images/applogo.png");
						}else{
							oks.setImageUrl(url);
						}
						String cityName=PreferencesUtils.getString(context, "cityName");
						oks.setTitle(Constant.title+"  "+cityName.substring(0,cityName.length()-1)+"城市热线");
						oks.setTitleUrl("http://m.rexian.cn/Forum/Info?msg="+tid+",1,"+cityID);// 商家地址分享
						oks.setText(Constant.title+"\r\n点击查看更多"+":http://m.rexian.cn/Forum/Info?msg="+tid+",1,"+cityID);
						// site是分享此内容的网站名称，仅在QQ空间使用
						oks.setSite(Constant.title+":http://m.rexian.cn/Forum/Info?msg="+tid+",1,"+cityID);
						// siteUrl是分享此内容的网站地址，仅在QQ空间使用
						oks.setUrl("http://m.rexian.cn/Forum/Info?msg="+tid+",1,"+cityID);
						oks.setSiteUrl("http://m.rexian.cn/Forum/Info?msg="+tid+",1,"+cityID);
						oks.show(context);

					}
				});
				abscure.reply.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int Logn=PreferencesUtils.getInt(context, "logn");
						  if (Logn==0)  {
							Intent intent = new Intent();
							intent.setClass(context, LoginActivity.class);
							context.startActivity(intent);	
						} else {
							Intent intent = new Intent();
							intent.setClass(context, Reply_post.class);
							intent.putExtra("stat", "1");
							intent.putExtra("tid", tid);
							context.startActivity(intent);
						}
						
					}
				});
				abscure.cengreply.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int Logn=PreferencesUtils.getInt(context, "logn");
						  if (Logn==0)  {
							Intent intent = new Intent();
							intent.setClass(context, LoginActivity.class);
							context.startActivity(intent);	
						} else {
							Intent intent = new Intent();
							intent.setClass(context, Reply_post.class);
							intent.putExtra("stat", "2");
							intent.putExtra("tid", tid);
							intent.putExtra(
									"yinyong",
									"[quote][size=2][color=#999999]"
											+ abscure_list.get(arg0).get("author")
											+ "发表于"
											+ abscure_list.get(arg0)
													.get("dateline")
											+ "[/color][url=forum.php?mod=redirect&goto=findpost&pid="
											+ abscure_list.get(arg0)
													.get("position")
											+ "&ptid="
											+ tid
											+ "][img]static/image/common/back.gif[/img][/url][/size]\r\n"+detail+"[/quote]");
							context.startActivity(intent);
						}	
					}

				});
				abscure.post_only_txt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(z==1){
							handler.sendEmptyMessage(4);
							abscure.post_only_txt.setText("取消只看楼主");
							z=2;
						}else{
							handler.sendEmptyMessage(6);
							abscure.post_only_txt.setText("只看楼主");
							z=1;
						}
						
						
					}
				});
				abscure.post_collect_txt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						handler.sendEmptyMessage(7);
						
					}
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arg1;
	}

	class Abscure {
		TextView share, comment, reply, only, post_author_txt, post_lou_txt,
				post_time_txt, cengreply,post_only_txt,post_collect_txt;
	}

}
