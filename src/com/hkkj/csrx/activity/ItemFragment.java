package com.hkkj.csrx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.ImgNewsAdapter;
import com.hkkj.csrx.adapter.Information_adpater;
import com.hkkj.csrx.adapter.Myviewpageadapater;
import com.hkkj.csrx.myview.ImageAndText;
import com.hkkj.csrx.myview.XListView;
import com.hkkj.csrx.myview.XListView.IXListViewListener;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.StringtoJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ItemFragment extends Fragment {

	String url;
	ViewPager myPager; // 图片容器
	LinearLayout ovalLayout, info_shuaxin; // 圆点容器
	private List<View> listViews; // 图片组
	AsyncImageLoader ImageLoader;// 异步加载图片
	String imagleurl, imgstr, infomlist, infomurl;
	int areaID, pageIndex = 1, a = 1, everyinfo = 0, c, ver = 0;
	ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> imghashMap;
	ArrayList<HashMap<String, Object>> infoarray = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> infohashMap;
	String[] urls;
	private ImageView[] indicator_imgs;
	Myviewpageadapater myviewpageadapater;
	Information_adpater information_adpater;
	private LayoutInflater inflater;
	private View item;
	XListView listView;
	private View myviewlay;
	// -------------
	private ImgNewsAdapter adapter;
	private List<ImageAndText> listImageAndText = new ArrayList<ImageAndText>();;
	private GridView gridview;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	String allinfo;// 数据源
	TextView more;
	private LinearLayout.LayoutParams params;
	int totalRecord = 0, page = 1;
	ImageView photo_n_back;
	HttpRequest httpRequest;
	String classID;
	View contextView;
	String title;
	StringtoJson stringtoJson;
	int mark = 0;
	Button info_freshen;
	int po = 0;
	final Handler handlers = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 动态找到布局文件，再从这个布局中find出TextView对象
		contextView = inflater
				.inflate(R.layout.fragment_item, container, false);
		info_shuaxin = (LinearLayout) contextView
				.findViewById(R.id.info_shuaxin);
		info_freshen = (Button) contextView.findViewById(R.id.info_freshen);
		// 获取Activity传递过来的参数
		Bundle mBundle = getArguments();
		title = mBundle.getString("arg");
		classID = mBundle.getString("classID");
		stringtoJson = new StringtoJson();
		String newsinfo = PreferencesUtils.getString(getActivity(), title);
		if (classID.endsWith("图片")) {
			if (newsinfo != null) {
				photo();
				try {
					setmap(newsinfo);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
				name();

			} else {
				photo();
				url = Constant.url+"news/newsPicList?areaID="
						+ areaID + "&pageSize=10&pageIndex=" + page;
				info(1, url);
				name();
			}

		} else {
			if (classID.endsWith("焦点")) {
				if (newsinfo != null) {
					jiaodian();
					infomlist = newsinfo;
					imgstr = PreferencesUtils.getString(getActivity(),
							"imagehead");
					handler.sendEmptyMessage(1);
					setlist();

				} else {
					jiaodian();
					infomurl = Constant.url+"news/newsFocusList?areaID="
							+ areaID + "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);
					setlist();
				}
			} else if (classID.endsWith("本地")) {
				if (newsinfo != null) {
					jiaodian();
					infomlist = newsinfo;
					imgstr = PreferencesUtils.getString(getActivity(),
							"imagehead");
					handler.sendEmptyMessage(1);
					setlist();
				} else {
					jiaodian();
					infomurl = Constant.url+"news/newsLocalList?areaID="
							+ areaID + "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);
					setlist();
				}

			} else {
				if (newsinfo != null) {
					jiaodian();
					infomlist = newsinfo;
					imgstr = PreferencesUtils.getString(getActivity(),
							"imagehead");
					handler.sendEmptyMessage(1);
					setlist();
				} else {
					jiaodian();
					infomurl = Constant.url+"news/classNewsList?areaID="
							+ areaID
							+ "&classID="
							+ classID
							+ "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);
					setlist();
				}
			}

		}

		return contextView;
	}

	void jiaodian() {
		listViews = new ArrayList<View>();
		listView = (XListView) contextView.findViewById(R.id.zixunlist);
		areaID = PreferencesUtils.getInt(getActivity(), "cityID");
		inflater = LayoutInflater.from(getActivity());

		myviewlay = LayoutInflater.from(getActivity()).inflate(
				R.layout.myviewpagelay, null);
		myPager = (ViewPager) myviewlay.findViewById(R.id.view_pager);

		ovalLayout = (LinearLayout) myviewlay.findViewById(R.id.indicator);

		listView.addHeaderView(myviewlay);
		handler.postDelayed(task, 8000);
		imagleurl = Constant.url+"news/adsGroup?areaID="
				+ areaID;
		listView.setPullLoadEnable(true);
		listView.setVisibility(View.GONE);
		listView.setRefreshTime("刚刚");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (infoarray.get(arg2-2).get("orPpt").equals("false")){
					Intent intent = new Intent();
					intent.putExtra("id", infoarray.get(arg2 - 2).get("id").toString());
					intent.putExtra("newsClassID",
							infoarray.get(arg2 - 2).get("newsClassId").toString());
					intent.putExtra("picId",
							infoarray.get(arg2 - 2).get("picId").toString());
					intent.setClass(getActivity(), Info_info.class);
					getActivity().startActivity(intent);
				}else {
					Intent intent = new Intent();
					intent.putExtra("iD", infoarray.get(arg2-2).get("id").toString());
					intent.setClass(getActivity(), ImgNewsInfo.class);
					getActivity().startActivity(intent);
				}


			}
		});
//		myPager.setOnSingleTouchListener(new Myviewpage.OnSingleTouchListener() {
//			@Override
//			public void onSingleTouch() {
//				Toast.makeText(getActivity(),"aaa", Toast.LENGTH_SHORT).show();
//			}
//		});
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				mark = 1;
				pageIndex = 1;
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String str = formatter.format(curDate);
				listView.setRefreshTime(str);
				PreferencesUtils.putInt(getActivity(), title + "my", pageIndex);
				PreferencesUtils.putString(getActivity(), title, null);
				if (classID.endsWith("焦点")) {
					infomurl = Constant.url+"news/newsFocusList?areaID="
							+ areaID + "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);
				} else if (classID.endsWith("本地")) {
					infomurl = Constant.url+"news/newsLocalList?areaID="
							+ areaID + "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);
				} else {

					infomurl = Constant.url+"news/classNewsList?areaID="
							+ areaID
							+ "&classID="
							+ classID
							+ "&pageSize=10&pageIndex=" + pageIndex;
					Informationlist(imagleurl, 1);

				}

			}

			@Override
			public void onLoadMore() {
				int a = everyinfo;

				int b = 10;
				c = a % b;
				if (c != 0) {
					c = a / b + 1;

				} else {
					c = a / b;

				}
				pageIndex = PreferencesUtils
						.getInt(getActivity(), title + "my");
				if (pageIndex == -1) {
					pageIndex = 1;
				}
				if (pageIndex == c) {
					handler.sendEmptyMessage(5);
				} else {
					if (ver == 0) {
						ver = 2;
						pageIndex++;
						PreferencesUtils.putInt(getActivity(), title + "my",
								pageIndex);
						if (classID.endsWith("焦点")) {
							infomurl = Constant.url+"news/newsFocusList?areaID="
									+ areaID
									+ "&pageSize=10&pageIndex="
									+ pageIndex;
							Informationlist(imagleurl, 1);
						} else if (classID.endsWith("本地")) {
							infomurl = Constant.url+"news/newsLocalList?areaID="
									+ areaID
									+ "&pageSize=10&pageIndex="
									+ pageIndex;
							Informationlist(imagleurl, 1);
						} else {

							infomurl = Constant.url+"news/classNewsList?areaID="
									+ areaID
									+ "&classID="
									+ classID
									+ "&pageSize=10&pageIndex=" + pageIndex;
							Informationlist(imagleurl, 1);

						}

					}

				}

			}
		});
		info_freshen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Informationlist(imagleurl, 1);

			}
		});
	}

	void photo() {

		areaID = PreferencesUtils.getInt(getActivity(), "cityID");
		gridview = (GridView) contextView.findViewById(R.id.photo_gridview);
		more = (TextView) contextView.findViewById(R.id.button);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		gridview.setOnScrollListener(new XListView.OnXScrollListener() {
			@Override
			public void onXScrolling(View view) {

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							int v = 1, c = 0;
							if (v == 1) {
								int a = totalRecord;
								int b = 10;

								c = a % b;
								if (c != 0) {
									c = a / b + 1;
								} else {
									c = a / b;

								}

							}

							if (page == c) {
								handler.sendEmptyMessage(2);
							} else {
								more.setText("加载中...");
								page++;
								url = Constant.url+"news/newsPicList?areaID="
										+ areaID + "&pageSize=10&pageIndex=" + page;
								info(1, url);
							}
						}
						break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("iD", array.get(arg2).get("id"));
				intent.setClass(getActivity(), ImgNewsInfo.class);
				getActivity().startActivity(intent);

			}
		});
		info_freshen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				info(1, url);

			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// 非图片新闻
	public void Informationlist(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				imgstr = httpRequest.doGet(url, getActivity());
				infomlist = httpRequest.doGet(infomurl, getActivity());
				if (infomlist.equals("网络超时")) {
					handler.sendEmptyMessage(4);
				} else {
					Message message = new Message();
					message.what = what;
					handler.sendEmptyMessage(what);
					Looper.loop();
				}

			};
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				if (classID.endsWith("图片")) {
					more.setText("数据加载完毕");
					Toast.makeText(getActivity(), "数据加载完毕", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (imgstr != null) {
						try {
							imgurl(imgstr);

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						for (int i = 0; i < urls.length; i++) {
							item = inflater.inflate(R.layout.item, null);
							((TextView) item.findViewById(R.id.infor_title))
									.setText(imgarray.get(i).get("name"));
							listViews.add(item);
						}
						myviewpageadapater = new Myviewpageadapater(listViews,
								getActivity(), urls, imgarray, 1);

						myPager.setAdapter(myviewpageadapater);
						// 绑定动作监听器：如翻页的动画
						try {
							initIndicator();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else{
//						handler.sendEmptyMessage(4);
					}

				}
				break;
			case 1:
				info_shuaxin.setVisibility(View.GONE);
				if (classID.endsWith("图片")) {
					adapter.notifyDataSetChanged();
				} else {
					listView.setVisibility(View.VISIBLE);
					listView.stopLoadMore();
					listView.stopRefresh();
					if (mark == 1) {
						try {
							PreferencesUtils.putInt(getActivity(),
									title + "my", 1);
							PreferencesUtils.putString(getActivity(), title,
									null);
							infoarray.clear();
							information_adpater.notifyDataSetChanged();
							mark = 0;
						} catch (Exception e) {
							// TODO: handle exception
						}

					}

					try {
						infomlist(infomlist);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listView.requestLayout();
					information_adpater.notifyDataSetChanged();
					myPager.setOnPageChangeListener(new MyListener());
					if (a == 1)
						handler.sendEmptyMessage(2);
					a = 2;
					ver = 0;
				}
				break;
			case 3:

				try {
					listView.setPullLoadEnable(false);
					more.setText("暂时没有数据");
					more.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO: handle exception
				}

				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT)
						.show();

				break;
			case 4:
				mark = 0;
				info_shuaxin.setVisibility(View.VISIBLE);
				try {
					listView.setPullLoadEnable(false);
					Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT)
					.show();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			case 5:
				listView.setPullLoadEnable(false);
				Toast.makeText(getActivity(), "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}

		};
	};

	public void imgurl(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == -1) {
			Message message = new Message();
			message.what = 5;
			handler.sendEmptyMessage(5);
		} else if (jsonObject.getString("status").equals("网络超时")) {
			Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			urls = new String[jsonArray.length()];
			indicator_imgs = new ImageView[urls.length];
			for (int i = 0; i < jsonArray.length(); i++) {
				imghashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				imghashMap.put("iD", jsonObject2.getString("iD"));
				imghashMap.put("picID", jsonObject2.getString("picID"));
				urls[i] = jsonObject2.getString("picID");
				imghashMap.put("name", jsonObject2.getString("name"));
				imghashMap.put("url", jsonObject2.getString("url"));
				imgarray.add(imghashMap);
			}
			String myjson = stringtoJson.getJson(imgarray, 0);
			try {
				PreferencesUtils.putString(getActivity(), "imagehead", myjson);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void infomlist(String str) throws JSONException {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
				.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == -1) {
			Message message = new Message();
			message.what = 5;
			handler.sendEmptyMessage(5);
		} else {
			com.alibaba.fastjson.JSONArray jsonArray = jsonObject
					.getJSONArray("list");
			everyinfo = jsonObject.getIntValue("totalRecord");
			for (int i = 0; i < jsonArray.size(); i++) {
				infohashMap = new HashMap<String, Object>();
				com.alibaba.fastjson.JSONObject jsonObject2 = jsonArray
						.getJSONObject(i);
				infohashMap.put("picId",
						jsonObject2.getString("picId") == null ? ""
								: jsonObject2.getString("picId"));
				infohashMap.put("id", jsonObject2.getString("id"));
				infohashMap.put("clickNum", jsonObject2.getString("clickNum"));
				infohashMap.put("newsClassId",
						jsonObject2.getString("newsClassId"));

				infohashMap.put("title", jsonObject2.getString("title"));
				infohashMap.put("description",
						jsonObject2.getString("description"));
				infohashMap.put("orPpt",
						jsonObject2.getString("orPpt"));
				infoarray.add(infohashMap);
			}
			String myjson = stringtoJson.getJson(infoarray, everyinfo);
			try {
				PreferencesUtils.putString(getActivity(), title, myjson);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	private void initIndicator() {

		ImageView imgView;
		View v = contextView.findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标
		for (int i = 0; i < urls.length; i++) {
			imgView = new ImageView(getActivity());
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 10, 7, 10);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;

			if (i == 0) { // 初始化第一个为选中状态

				indicator_imgs[i]
						.setBackgroundResource(R.drawable.shopdian3);
			} else {
				indicator_imgs[i].setBackgroundResource(R.drawable.shopdian4);
			}
			((ViewGroup) v).addView(indicator_imgs[i]);

		}

	}

	public class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			if (state == 0) {
				// new MyAdapter(null).notifyDataSetChanged();
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			try {
				// 改变所有导航的背景图片为：未选中

				for (int i = 0; i < indicator_imgs.length; i++) {

					indicator_imgs[i]
							.setBackgroundResource(R.drawable.shopdian3);

				}
				// 改变当前背景图片为：选中

				indicator_imgs[position]
						.setBackgroundResource(R.drawable.shopdian4);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public void setlist() {
		information_adpater = new Information_adpater(getActivity(), infoarray);
		listView.setAdapter(information_adpater);
	}

	// -图片新闻
	public void info(final int what, final String url) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Looper.prepare();
				httpRequest = new HttpRequest();
				allinfo = httpRequest.doGet(url, getActivity());
				System.out.println("allinfo:"+allinfo);
				try {
					setmap(allinfo);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = new Message();
				message.what = what;
				handler.sendEmptyMessage(what);
				Looper.loop();
			};
		}.start();
	}

	public void setmap(String str) throws JSONException {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		

		if (a == -1) {
			Message message = new Message();
			message.what =5;
			handler.sendEmptyMessage(5);
		} else {
			com.alibaba.fastjson.JSONArray jsonArray = jsonObject.getJSONArray("list");
			totalRecord = jsonObject.getIntValue("totalRecord");
			for (int i = 0; i < jsonArray.size(); i++) {
				com.alibaba.fastjson.JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				hashMap = new HashMap<String, String>();
				hashMap.put("id", jsonObject2.getString("id")==null?"":jsonObject2.getString("id"));
				hashMap.put("picId", jsonObject2.getString("picId")==null?"":jsonObject2.getString("picId"));
				hashMap.put("title", jsonObject2.getString("title")==null?"":jsonObject2.getString("title"));
				array.add(hashMap);
				ImageAndText iat = new ImageAndText(
						jsonObject2.getString("picId")==null?"":jsonObject2.getString("picId"),
						jsonObject2.getString("title")==null?"":jsonObject2.getString("title"));
				listImageAndText.add(iat);
			}
			String myjson = stringtoJson.getJson(array, totalRecord);
			try {
				PreferencesUtils.putString(getActivity(), title, myjson);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
	Runnable task = new Runnable() {
		private boolean run = true;

		public void run() {
			// TODO Auto-generated method stub

			if (run) {
				handlers.postDelayed(this, 5000);
				if (po < imgarray.size()) {
					po++;
				} else {
					po = 0;
				}
			}
			myPager.setCurrentItem(po);
		}
	};
	public void name() {
		adapter = new ImgNewsAdapter(listImageAndText,getActivity());
		gridview.setAdapter(adapter);
	}

}
