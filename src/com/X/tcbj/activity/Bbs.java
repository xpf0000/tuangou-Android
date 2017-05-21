package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.BBsadapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.server.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 论坛首页
 * 
 * @author zpp
 * 
 */
public class Bbs extends Activity implements OnClickListener {
	ListView bbs_lit;
	String url, bbslist;
	int page = 1, pagesize = 10, totalRecord = 0, c, order = 0, v = 1, ver = 0,
			siteid = 1;
	LinearLayout bbs_new_post, bbs_new_reply, bbs_top_post, bbs_class_show,
			bbs_shuaxin;
	TextView tv_load_more;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	BBsadapter bBsadapter;
	private View loadMoreView;
	ImageView back, bbs_new_post_img, bbs_new_reply_img, bbs_top_post_img,
			bbs_re;
	Button bbs_freshen;
	private MyHandler handler = null;
	location location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs);
		siteid = PreferencesUtils.getInt(Bbs.this, "cityID");
		bbs_lit = (ListView) findViewById(R.id.bbs_list);
		back = (ImageView) findViewById(R.id.bbs_h_back);
		back.setOnClickListener(this);
		bbs_new_post = (LinearLayout) findViewById(R.id.bbs_new_post);
		bbs_new_reply = (LinearLayout) findViewById(R.id.bbs_new_reply);
		bbs_top_post = (LinearLayout) findViewById(R.id.bbs_top_post);
		bbs_class_show = (LinearLayout) findViewById(R.id.bbs_class_show);
		bbs_new_post_img = (ImageView) findViewById(R.id.bbs_new_post_img);
		bbs_new_reply_img = (ImageView) findViewById(R.id.bbs_new_reply_img);
		bbs_top_post_img = (ImageView) findViewById(R.id.bbs_top_post_img);
		bbs_shuaxin = (LinearLayout) findViewById(R.id.bbs_shuaxin);
		bbs_freshen = (Button) findViewById(R.id.bbs_freshen);
		bbs_re = (ImageView) findViewById(R.id.bbs_re);
		bbs_re.setOnClickListener(this);
		bbs_freshen.setOnClickListener(this);
		bbs_new_post.setOnClickListener(this);
		bbs_new_reply.setOnClickListener(this);
		bbs_top_post.setOnClickListener(this);
		bbs_class_show.setOnClickListener(this);
		location = (location) getApplication();
		handler = new MyHandler();
		loadMoreView = LayoutInflater.from(Bbs.this).inflate(
				R.layout.load_more, null);
		bbs_lit.addFooterView(loadMoreView, tv_load_more, false);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		url = Constant.url+ "forum/index?page=" + page
				+ "&pagesize=10&order=" + order + "&siteid=" + siteid;
		BBlist(url, 1);
		setbbslust();
		bbs_lit.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if (v == 1) {
					int a = totalRecord;
					int b = 10;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;

					}
					switch (arg1) {
					// 当不滚动时
					case OnScrollListener.SCROLL_STATE_IDLE:
						// 判断滚动到底部
						if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
							if (page == c) {
								handler.sendEmptyMessage(2);
							} else {
								if (ver == 0) {
									page++;
									url = Constant.url+ "forum/index?page="
											+ page
											+ "&pagesize=10&order="
											+ order + "&siteid=" + siteid;
									BBlist(url, 1);
									ver = 1;
								}

							}

						}
						break;
					}
				} else {
					int a = totalRecord;
					int b = 10;
					c = a % b;
					if (c != 0) {
						c = a / b + 1;
					} else {
						c = a / b;

					}
					switch (arg1) {
					// 当不滚动时
					case OnScrollListener.SCROLL_STATE_IDLE:
						// 判断滚动到底部
						if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
							if (page == c) {
								handler.sendEmptyMessage(2);
							} else {
								page++;
								url = Constant.url+ "forum/top?page="
										+ page
										+ "&pagesize=20&siteid="
										+ siteid;
								BBlist(url, 1);
							}

						}
						break;
					}
				}

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});
		bbs_lit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(Bbs.this, Post_info.class);
				intent.putExtra("tid", array.get(arg2).get("tid"));
				Bbs.this.startActivity(intent);

			}
		});
	}

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	public void BBlist(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				bbslist = httpRequest.doGet(url, Bbs.this);
				System.out.println(bbslist);
//				try {

					Message message = new Message();
					message.what = what;
					handler.sendEmptyMessage(what);
					Looper.loop();

//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Message message = new Message();
//					message.what = 4;
//					handler.sendEmptyMessage(4);
//					Looper.loop();
//				}

			};
		}.start();

	}

	public final class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					bbs_listinfo(bbslist);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				int b = 10;
				c = totalRecord % b;
				if (c != 0) {
					c = totalRecord / b + 1;
				} else {
					c = totalRecord / b;

				}
				bBsadapter.notifyDataSetChanged();
				if (array.size() == 0) {
					setLoadMoreText(R.string.loading_all);
				} else {
					setLoadMoreText(R.string.loading_more);
				}
				if (page == c) {
					setLoadMoreText(R.string.loading_all);
				} else {
					setLoadMoreText(R.string.loading_more);
				}
				loadMoreView.setVisibility(View.VISIBLE);
				bbs_shuaxin.setVisibility(View.GONE);
				ver = 0;
				break;
			case 2:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Bbs.this, "数据加载完毕", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Bbs.this, "没有数据", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				bbs_shuaxin.setVisibility(View.VISIBLE);
				loadMoreView.setVisibility(View.GONE);
				Toast.makeText(Bbs.this, "网络访问超时", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				BBlist(url, 6);
				break;
			case 6:
				array.clear();
				try {
					bbs_listinfo(bbslist);
					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	}

	/**
	 * 
	 * @param str
	 * @throws JSONException
	 *             I tid; 帖子ID fid; 板块ID author; 发帖人 authorid; 发帖人ID subject;
	 *             帖子标题 dateline; 发表时间 String lastpost; 最后回复时间 String
	 *             lastposter; 最后回复人 Integer views; 浏览量 Integer replies; 回复量
	 *             Integer displayorder; 是否置顶 Integer digest; 是否精华帖 Integer
	 *             special; 特殊主题,1:投票;2:商品;3:悬赏;4:活动;5:辩论贴;127:插件相关 Integer
	 *             attachment; 附件,0无附件 1普通附件 2有图片附件 Integer heats; 主题热度值 决定是否是热贴
	 *             一般大于某个值才是热贴 Short stamp; 主题图章 Short icon; 主题图标
	 */
	public void bbs_listinfo(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");
		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			totalRecord = jsonObject.getInt("totalRecord");
			for (int i = 0; i < jsonArray.length(); i++) {
				hashMap = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);

				hashMap.put("attachment", jsonObject2.getString("attachment"));
				hashMap.put("author", jsonObject2.getString("author"));
				hashMap.put("authorid", jsonObject2.getString("authorid"));
				hashMap.put("dateline", jsonObject2.getString("dateline"));
				hashMap.put("digest", jsonObject2.getString("digest"));
				hashMap.put("displayorder",
						jsonObject2.getString("displayorder"));
				hashMap.put("fid", jsonObject2.getString("fid"));
				hashMap.put("heats", jsonObject2.getString("heats"));
				hashMap.put("icon", jsonObject2.getString("icon"));
				hashMap.put("lastpost", jsonObject2.getString("lastpost"));
				hashMap.put("lastposter", jsonObject2.getString("lastposter"));
				hashMap.put("replies", jsonObject2.getString("replies"));
				hashMap.put("special", jsonObject2.getString("special"));
				hashMap.put("stamp", jsonObject2.getString("stamp"));
				hashMap.put("subject", jsonObject2.getString("subject"));
				hashMap.put("tid", jsonObject2.getString("tid"));
				hashMap.put("views", jsonObject2.getString("views"));
				array.add(hashMap);
			}

		}
	}

	public void setbbslust() {
		bBsadapter = new BBsadapter(array, Bbs.this);
		bbs_lit.setAdapter(bBsadapter);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bbs_h_back:
			finish();
			break;
		case R.id.bbs_new_post:
			bbs_new_post_img.setImageResource(R.drawable.bbs_new_post2);
			bbs_new_reply_img.setImageResource(R.drawable.bbs_new_reply);
			bbs_top_post_img.setImageResource(R.drawable.bbs_top_post);
			order = 0;
			v = 1;
			page = 1;
			array.clear();
			url = Constant.url+ "forum/index?page=" + page
					+ "&pagesize=10&order=" + order + "&siteid=" + siteid;
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.bbs_new_reply:
			bbs_new_post_img.setImageResource(R.drawable.bbs_new_post);
			bbs_new_reply_img.setImageResource(R.drawable.bbs_new_reply2);
			bbs_top_post_img.setImageResource(R.drawable.bbs_top_post);
			order = 1;
			page = 1;
			v = 1;
			array.clear();
			url = Constant.url+ "forum/index?page=" + page
					+ "&pagesize=10&order=" + order + "&siteid=" + siteid;
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.bbs_top_post:
			bbs_new_post_img.setImageResource(R.drawable.bbs_new_post);
			bbs_new_reply_img.setImageResource(R.drawable.bbs_new_reply);
			bbs_top_post_img.setImageResource(R.drawable.bbs_top_post2);
			v = 2;
			page = 1;
			array.clear();
			url = Constant.url+ "forum/top?page=" + page
					+ "&pagesize=20&siteid=" + siteid;
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.bbs_class_show:
			Intent intent = new Intent();
			intent.setClass(Bbs.this, Bbs_class.class);
			Bbs.this.startActivity(intent);
			break;
		case R.id.bbs_freshen:
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.bbs_re:
			int Logn = PreferencesUtils.getInt(Bbs.this, "logn");
			if (Logn == 0) {
				Intent inten3 = new Intent();
				inten3.setClass(Bbs.this, LoginActivity.class);
				Bbs.this.startActivity(inten3);
			} else {
				location.setHandler(handler);
				Intent intent2 = new Intent();
				intent2.setClass(Bbs.this, Report_post.class);
				Bbs.this.startActivity(intent2);
			}

			break;
		default:
			break;
		}

	}

}
