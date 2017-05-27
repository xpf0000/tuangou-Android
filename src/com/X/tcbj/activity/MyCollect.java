package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.X.model.UserCollectModel;
import com.X.model.UserCommentModel;
import com.X.server.DataCache;
import com.X.tcbj.adapter.CommentAdapter;
import com.X.tcbj.adapter.MyCollectProductAdapter;
import com.X.tcbj.utils.Bimp;
import com.X.xnet.XNetUtil;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Commentadpater;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * 收藏
 * 
 * @author zpp
 * @version1.0
 */
public class MyCollect extends Activity {

	private SwipeRefreshLayout swipe_container;
	private ListView listview;
	private CommentAdapter adapter;
	private List<UserCommentModel.ItemBean> productList = new ArrayList<>();

	private View footer;
	private ProgressBar listview_foot_progress;
	private TextView listview_foot_more;

	private int pageNow = 1;
	private int lastItem;
	private int toalPage;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mycollect);


		initview();
		loadData();
	}
	public void initview(){

		swipe_container=(SwipeRefreshLayout)findViewById(R.id.swipe_container);
		listview=(ListView)findViewById(R.id.mycollect_list);


		swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						pageNow = 1;
						loadData();
						listview_foot_more.setVisibility(View.INVISIBLE);
						swipe_container.setRefreshing(false);
					}
				}, 1000);

			}
		});


		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				XNetUtil.APPPrintln("position: "+position);

			}
		});

		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				if (productList.isEmpty()) {//数据为空
					return;
				}
				//判断是否滚动到底部
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
						&& lastItem == adapter.getCount()) {
					if (pageNow < toalPage) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								listview_foot_progress.setVisibility(View.VISIBLE);
								listview_foot_more.setVisibility(View.INVISIBLE);
								pageNow++;
								loadData();
							}
						}, 1000);

					} else {
						listview_foot_progress.setVisibility(View.INVISIBLE);
						listview_foot_more.setVisibility(View.VISIBLE);
						listview_foot_more.setText("已加载全部");
					}
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount - 1;
			}
		});

		//初始化底部视图
		footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
		listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
		listview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
		listview.setFooterDividersEnabled(false);

		adapter=new CommentAdapter(productList,this);

		listview.setAdapter(adapter);


	}


	public void loadData(){

		String uid = DataCache.getInstance().user.getId()+"";
		XNetUtil.Handle(APPService.user_commentlist(uid, pageNow + ""), new XNetUtil.OnHttpResult<UserCommentModel>() {
			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onSuccess(UserCommentModel model) {

				toalPage = model.getPage().getPage_total();
				if (pageNow==1){
					productList.clear();
				}

				productList.addAll(model.getItem());
				adapter.notifyDataSetChanged();

			}
		});


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Bimp.clear();
	}

	public void back(View v)
	{
		finish();
	}
}
