package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 论坛版块内容
 * @author zpp
 *
 */
public class Bbs_new_post extends Activity implements OnClickListener {
	LinearLayout newreply, newpost,bbs_post_shuaxin;
	TextView newreplytxt, newposttxt, tv_load_more;
	ImageView bbs_new_back,new_post;
	ListView newreply_list;
	BBsadapter bBsadapter;
	private View loadMoreView;
	String url, poetlist, id;
	int page = 1, pagesize = 10, totalRecord = 0, c, order = 0,siteid=1,ver=0;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
	Button bbs__post_freshen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_new_post);
		siteid = PreferencesUtils.getInt(Bbs_new_post.this, "cityID");
		int color = getResources().getColor(R.color.mygrey);
		newreply = (LinearLayout) findViewById(R.id.newreply);
		new_post=(ImageView)findViewById(R.id.new_post);
		new_post.setOnClickListener(this);
		newreply.setOnClickListener(this);
		newpost = (LinearLayout) findViewById(R.id.newpost);
		newpost.setOnClickListener(this);
		newreplytxt = (TextView) findViewById(R.id.newreplytxt);
		newposttxt = (TextView) findViewById(R.id.newposttxt);
		bbs_new_back = (ImageView) findViewById(R.id.bbs_new_back);
		bbs_new_back.setOnClickListener(this);
		bbs_post_shuaxin = (LinearLayout) findViewById(R.id.bbs_post_shuaxin);
		bbs__post_freshen=(Button)findViewById(R.id.bbs__post_freshen);
		bbs__post_freshen.setOnClickListener(this);
		newreply_list = (ListView) findViewById(R.id.newreply_list);
		loadMoreView = LayoutInflater.from(Bbs_new_post.this).inflate(
				R.layout.load_more, null);
		newreply_list.addFooterView(loadMoreView, newreplytxt, false);
		tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
		id = getIntent().getStringExtra("id");
		url = Constant.url+"forum/glist?id=" + id
				+ "&siteid="+siteid+"&page=" + page + "&pagesize=10&order=" + order;
		BBlist(url, 1);
		setbbslust();
		newreply_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
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
							if(ver==0){
								page++;
								url = "http://116.255.204.113:8080/HKCityApi/forum/glist?id="
										+ id
										+ "&siteid="+siteid+"&page="
										+ page
										+ "&pagesize=10&order=" + order;
								System.out.println(url);
								BBlist(url, 1);	
								ver=1;
							}
							
						}

					}
					break;
				}

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});
		newreply_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent();
				intent.setClass(Bbs_new_post.this, Post_info.class);
				intent.putExtra("tid", array.get(arg2).get("tid"));
				Bbs_new_post.this.startActivity(intent);
				
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		int color = getResources().getColor(R.color.mygrey);
		switch (arg0.getId()) {
		case R.id.newreply:
			order = 1;
			page = 1;
			newpost.setBackgroundColor(color);
			newreplytxt.setTextColor(Color.RED);
			newreply.setBackgroundColor(Color.WHITE);
			newposttxt.setTextColor(Color.BLACK);
			url = Constant.url+"forum/glist?id=" + id
					+ "&siteid="+siteid+"&page=" + page + "&pagesize=10&order=" + order;
			array.clear();
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.newpost:
			order = 0;
			page = 1;
			newreply.setBackgroundColor(color);
			newposttxt.setTextColor(Color.RED);
			newpost.setBackgroundColor(Color.WHITE);
			newreplytxt.setTextColor(Color.BLACK);
			url = Constant.url+"forum/glist?id=" + id
					+ "&siteid="+siteid+"&page=" + page + "&pagesize=10&order=" + order;
			array.clear();
			BBlist(url, 1);
			setbbslust();
			break;
		case R.id.bbs_new_back:
			finish();
			break;
		case R.id.new_post:
			int Logn=PreferencesUtils.getInt(Bbs_new_post.this, "logn");
			  if (Logn==0)  {
				Intent inten3 = new Intent();
				inten3.setClass(Bbs_new_post.this, LoginActivity.class);
				Bbs_new_post.this.startActivity(inten3);
			} else {
				Intent intent2=new Intent();
				intent2.setClass(Bbs_new_post.this, Report_post.class);
				Bbs_new_post.this.startActivity(intent2);
			}
			break;
		default:
			break;
		}

	}

	public void BBlist(final String url, final int what) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				HttpRequest httpRequest = new HttpRequest();
				poetlist = httpRequest.doGet(url, Bbs_new_post.this);
				try {
					bbs_listinfo(poetlist);
					Message message = new Message();
					message.what = what;
					handler.sendEmptyMessage(what);
					Looper.loop();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();
					message.what = 4;
					handler.sendEmptyMessage(4);
					Looper.loop();
				}

			};
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
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
				if(page==c){
					setLoadMoreText(R.string.loading_all);
				}else{
					setLoadMoreText(R.string.loading_more);
				}
				bbs_post_shuaxin.setVisibility(View.GONE);
				loadMoreView.setVisibility(View.VISIBLE);
				ver=0;
				break;
			case 2:
				setLoadMoreText(R.string.loading_all);
				Toast.makeText(Bbs_new_post.this, "数据加载完毕", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				setLoadMoreText(R.string.nostring);
				Toast.makeText(Bbs_new_post.this, "没有数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				bbs_post_shuaxin.setVisibility(View.VISIBLE);
				loadMoreView.setVisibility(View.GONE);
				Toast.makeText(Bbs_new_post.this, "网络访问超时", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}

		};
	};

	public void setLoadMoreText(int some) {
		tv_load_more.setText(some);
	}

	public void bbs_listinfo(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		int a = jsonObject.getInt("status");

		if (a == 1) {
			Message message = new Message();
			message.what = 3;
			handler.sendEmptyMessage(3);
		} else {
			totalRecord = jsonObject.getInt("totalRecord");
			JSONArray jsonArray = jsonObject.getJSONArray("list");
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
		bBsadapter = new BBsadapter(array, Bbs_new_post.this);
		newreply_list.setAdapter(bBsadapter);

	}

}
