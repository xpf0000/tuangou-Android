package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoader.ImageCallBack;
import com.X.tcbj.utils.AsyncImageLoadersdcard;
import com.X.tcbj.utils.AsyncImageLoadersdcard.ImageCallBack2;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GestureListener;
import com.X.tcbj.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class Photo_newsinfo extends Activity implements OnClickListener {
	ImageView back, share, image;
	TextView view, next, last, info, title;
	HashMap<String, String> hashMap;
	HttpRequest httpRequest;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> array2 = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> hashMap2;
	AsyncImageLoader ImageLoader;
	private AsyncImageLoadersdcard asyncImageLoadersdcard;
	String allinfo, url, newsID;// 数据源
	int i = 0;
	private PopupWindow popupWindow;
	List<Map<String, Object>> popList = new ArrayList<Map<String, Object>>();
	HashMap<String, Object> map;
	View popView;
	LinearLayout layout, linearLayout;
	private String[] orderStr = new String[] { "评论", "分享" };
	DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.photonew_info);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		linearLayout = (LinearLayout) findViewById(R.id.mygodview);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		float a = (float) (3.0 / 5.0);
		params.setMargins(0, (int) (dm.heightPixels * (a)), 0, 0);
		linearLayout.setLayoutParams(params);
		back = (ImageView) findViewById(R.id.photo_back);
		back.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.photo_top_more);
		share.setOnClickListener(this);
		image = (ImageView) findViewById(R.id.photo_img);
		view = (TextView) findViewById(R.id.photo_views);
		next = (TextView) findViewById(R.id.photo_next);
		next.setOnClickListener(this);
		last = (TextView) findViewById(R.id.photo_last);
		last.setOnClickListener(this);
		info = (TextView) findViewById(R.id.photo_info);
		title = (TextView) findViewById(R.id.photo_title);
		layout = (LinearLayout) findViewById(R.id.photo_lay);
		image.setLongClickable(true);
		image.setOnTouchListener(new MyGestureListener(this));
		newsID = getIntent().getStringExtra("iD");
		url = Constant.url+"news/newsPicDetail?newsID="
				+ newsID;
		info(1, url);
	}

	private class MyGestureListener extends GestureListener {
		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			i = i + 1;
			if (i >= array.size()) {
				i = array.size() - 1;
				Toast.makeText(Photo_newsinfo.this, "已经是最后一张了",
						Toast.LENGTH_SHORT).show();

			} else {
				handler.sendEmptyMessage(1);
			}
			return super.left();
		}

		@Override
		public boolean right() {
			i = i - 1;
			if (i < 0) {
				i = 0;
				Toast.makeText(Photo_newsinfo.this, "已经是第一张了",
						Toast.LENGTH_SHORT).show();

			} else {
				handler.sendEmptyMessage(1);
			}
			return super.right();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.photo_back:
			finish();
			break;
		case R.id.photo_top_more:
			showOrderPopView();
			break;
		case R.id.photo_next:

			break;
		case R.id.photo_last:

			break;

		default:
			break;
		}

	}

	Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				info.setText(array.get(i).get("content"));
				next.setText("/" + array.size() + "");
				view.setText(array2.get(0).get("clickNum"));
				title.setText(array2.get(0).get("description"));
				int a = i + 1;
				last.setText(a + "");
				final String url = array.get(i).get("PicUrl");
				// 得到图片的宽高
				
				ImageView imageView = image;
				asyncImageLoadersdcard = new AsyncImageLoadersdcard();
				Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(imageView,
						url, new ImageCallBack2() {

							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (imageView.getTag() != null
										&& imageView.getTag().equals(url)) {
//									int w = bitmap.getWidth();
//									int h = bitmap.getHeight();
									int weight = bitmap.getWidth();
									int height = bitmap.getHeight();
									float scale = ((float) (height)) / ((float) (weight));

									LayoutParams params = new LayoutParams(
											LayoutParams.FILL_PARENT,
											(int) (dm.widthPixels * scale));
									image.setLayoutParams(params);
									BitmapDrawable drawable = new BitmapDrawable(
											bitmap);
									imageView.setBackgroundDrawable(drawable);
								}
							}
						});
				if (bitmap1 == null) {
					ImageLoader = new AsyncImageLoader();
					Bitmap bitmap = ImageLoader.loadBitmap(imageView, url,
							new ImageCallBack() {

								@Override
								public void imageLoad(ImageView imageView,
										Bitmap bitmap) {
									int weight = bitmap.getWidth();
									int height = bitmap.getHeight();
									float scale = ((float) (height)) / ((float) (weight));

									LayoutParams params = new LayoutParams(
											LayoutParams.FILL_PARENT,
											(int) (dm.widthPixels * scale));
									image.setLayoutParams(params);
									BitmapDrawable bd = new BitmapDrawable(
											bitmap);
									imageView.setBackgroundDrawable(bd);
								}

							});
					if (bitmap == null) {
						imageView.setBackgroundResource(R.drawable.head);
					} else {
						int weight = bitmap.getWidth();
						int height = bitmap.getHeight();
						float scale = ((float) (height)) / ((float) (weight));

						LayoutParams params = new LayoutParams(
								LayoutParams.FILL_PARENT,
								(int) (dm.widthPixels * scale));
						image.setLayoutParams(params);
						BitmapDrawable bd = new BitmapDrawable(bitmap);
						imageView.setBackgroundDrawable(bd);
					}
				} else {
					int weight = bitmap1.getWidth();
					int height = bitmap1.getHeight();
					float scale = ((float) (height)) / ((float) (weight));

					LayoutParams params = new LayoutParams(
							LayoutParams.FILL_PARENT,
							(int) (dm.widthPixels * scale));
					image.setLayoutParams(params);
					BitmapDrawable bd = new BitmapDrawable(bitmap1);
					imageView.setBackgroundDrawable(bd);
				}

				break;
			case 2:
				Toast.makeText(Photo_newsinfo.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				Toast.makeText(Photo_newsinfo.this, "您请求的数据已不存在",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		};
	};

	public void info(final int what, final String url) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				httpRequest = new HttpRequest();
				allinfo = httpRequest.doGet(url, Photo_newsinfo.this);
				if (allinfo.equals("网络超时")) {
					handler.sendEmptyMessage(2);
				} else {
					try {
						setmap(allinfo);
						Message message = new Message();
						message.what = what;
						handler.sendEmptyMessage(what);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Looper.loop();
				}
			};
		}.start();
	}

	// 装入数据
	public void setmap(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == -1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap2 = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				JSONArray jsonArray3 = jsonObject2.getJSONArray("newsPPt");
				hashMap2.put("description", jsonObject2.getString("title"));
				hashMap2.put("clickNum", jsonObject2.getString("clickNum"));
				hashMap2.put("commentCount",
						jsonObject2.getString("commentCount"));
				hashMap2.put("distinctCount",
						jsonObject2.getString("distinctCount"));
				array2.add(hashMap2);
				for (int j = 0; j < jsonArray3.length(); j++) {
					hashMap = new HashMap<String, String>();
					JSONObject jsonObject4 = (JSONObject) jsonArray3.get(j);
					hashMap.put("PicUrl", jsonObject4.getString("picID"));
					hashMap.put("content", jsonObject4.getString("content"));
					hashMap.put("weight", jsonObject4.getString("weight"));
					hashMap.put("height", jsonObject4.getString("height"));
					array.add(hashMap);
				}

			}

		}

	}

	public void showOrderPopView() {
		popView = Photo_newsinfo.this.getLayoutInflater().inflate(
				R.layout.news_share, null);
		// 获取手机屏幕的宽和高
		WindowManager windowManager = Photo_newsinfo.this.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int frameHeith = display.getHeight();
		int frameWith = display.getWidth();
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 使其聚焦
		popupWindow.setFocusable(true);
		// 设置不允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 刷新状态
		popupWindow.update();
		ListView listview = (ListView) popView
				.findViewById(R.id.new_share_list);

		popList.clear();
		int[] imageId = new int[] { R.drawable.news_top_share,
				R.drawable.news_top_comment };
		for (int i = 0; i < 2; i++) {
			map = new HashMap<String, Object>();
			map.put("img", imageId[i]);
			map.put("item", orderStr[i]);
			popList.add(map);

		}

		SimpleAdapter adapter = new SimpleAdapter(Photo_newsinfo.this, popList,
				R.layout.news_share_txt, new String[] { "img", "item" },
				new int[] { R.id.share_img, R.id.share_txt });
		listview.setAdapter(adapter);

		popupWindow.showAsDropDown(share);// 显示在筛选区域条件下
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(array2.size()==0){
					
				}else{
				if (arg2 == 1) {
					int cityID = PreferencesUtils.getInt(Photo_newsinfo.this, "cityID");
					System.out.println("http://192.168.2.177:8080/wap/news/newsPicDetail?msg="+newsID+","+cityID);
					String city=PreferencesUtils.getString(Photo_newsinfo.this, "cityNamepl");
					OnekeyShare oks = new OnekeyShare();
					oks.disableSSOWhenAuthorize();// 分享前要先授权
					// 分享时Notification的图标和文字
//					oks.setNotification(R.drawable.ic_launcher,
//							getString(R.string.app_name));
					String cityName=PreferencesUtils.getString(Photo_newsinfo.this, "cityName");
					oks.setTitle(cityName.substring(0, cityName.length() - 1) + "城市热线");
					oks.setImageUrl(array.get(0).get("PicUrl"));
					oks.setTitleUrl("http://m.rexian.cn/news/newsPicDetail?msg="+newsID + ","+cityID);// 商家地址分享
					String u="http://m.rexian.cn/news/newsPicDetail?msg="+newsID +","+cityID;
					oks.setText(array.get(0).get("content") + "\r\n点击查看更多" + ":http://m.rexian.cn/news/newsPicDetail?msg="+newsID+","+cityID);
					// site是分享此内容的网站名称，仅在QQ空间使用
					oks.setSite("新闻");
					// siteUrl是分享此内容的网站地址，仅在QQ空间使用
					oks.setUrl("http://m.rexian.cn/news/newsPicDetail?msg="+newsID+","+cityID);
					oks.setSiteUrl("luoyang.rexian.cn");
					oks.show(Photo_newsinfo.this);
				} else {
					Intent intent = new Intent();
					intent.putExtra("newsID", newsID);
					
						intent.putExtra("title", array2.get(0).get("description"));
						intent.putExtra("commentCount",
								array2.get(0).get("commentCount"));
						intent.putExtra("distinctCount",
								array2.get(0).get("distinctCount"));
						intent.setClass(Photo_newsinfo.this, News_omment.class);
						Photo_newsinfo.this.startActivity(intent);
					
				}
				}
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

}
