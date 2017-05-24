package com.X.tcbj.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.X.model.HomeModel;
import com.X.model.NearbyModel;
import com.X.model.TuanModel;
import com.X.server.DataCache;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.TuanAdapter;
import com.X.tcbj.myview.KeywordsFlow;
import com.X.tcbj.utils.CommonField;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.csrx.data.PreferencesUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import static com.X.server.location.APPService;

/**
 * 首页和商家搜索页面
 * 
 * @author zmz
 * 
 */
public class HotSearch extends Activity implements OnClickListener {

	SwipeRefreshLayout swipe_refresh;
	private TextView quxiao;
	EditText mytext;
	ImageView cancel;
	ListView lishiList,mainList;
	LinearLayout lishiLayout,mainLayout;
	SearchLishiAdapter lishiAdapter;
	// String searchKey;//搜索关键词
	int page = 1;
	boolean end = false;
	String lastKey = "";

	List<TuanModel> tuanList = new ArrayList<>();
	private TuanAdapter tuanApater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		lishiLayout = (LinearLayout) findViewById(R.id.search_lishi);
		mainLayout = (LinearLayout) findViewById(R.id.search_main_layout);
		lishiList = (ListView) findViewById(R.id.search_lishi_list);
		mainList = (ListView) findViewById(R.id.search_list);


		quxiao = (TextView) findViewById(R.id.quxiao);
		cancel = (ImageView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		mytext = (EditText) findViewById(R.id.shopSearch);

		mytext.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if(s.toString().trim().equals(""))
				{
					lastKey = "";
					quxiao.setText("取消");
					tuanList.clear();
					tuanApater.notifyDataSetChanged();
					lishiLayout.setVisibility(View.VISIBLE);
					mainLayout.setVisibility(View.GONE);
				}
				else
				{
					quxiao.setText("搜索");
				}

			}
		});

		// 设置软键盘的相关操作
		mytext.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

					if(!mytext.getText().toString().trim().equals(""))
					{
						search();
					}

					return true;
				}
				return false;
			}
		});

		quxiao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if(mytext.getText().toString().trim().equals(""))
				{
					finish();
				}
				else
				{
					search();
				}

			}
		});


		swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
		swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				end = false;
				getPrilist();
			}
		});

		mainList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							if(!end)
							{
								getPrilist();
							}
						}
						break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});

		lishiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String str = DataCache.getInstance().searchKeys.getSearchKeys().get(position);
				mytext.setText(str);
				search();

			}
		});

		tuanApater=new TuanAdapter(tuanList,this);
		mainList.setAdapter(tuanApater);

		lishiAdapter = new SearchLishiAdapter(this);
		lishiList.setAdapter(lishiAdapter);

	}

	private void getPrilist()
	{
		String city_id = DataCache.getInstance().nowCity.getId();

		double x = 0.0,y=0.0;

		if(XPostion.getInstance().getPostion() != null)
		{
			x = XPostion.getInstance().getPostion().getLongitude();
			y = XPostion.getInstance().getPostion().getLatitude();
		}

		String key = mytext.getText().toString().trim();

		XNetUtil.Handle(APPService.tuan_search(page+"", city_id,key, x, y), new XNetUtil.OnHttpResult<NearbyModel>() {
			@Override
			public void onError(Throwable e) {
				swipe_refresh.setRefreshing(false);
			}

			@Override
			public void onSuccess(NearbyModel model) {
				swipe_refresh.setRefreshing(false);
				if(model != null)
				{
					List<TuanModel> list = model.getItem();

					if(page == 1)
					{
						tuanList.clear();
					}

					if(list != null)
					{
						if(list.size() > 0)
						{
							page++;
							tuanList.addAll(list);
						}
						else
						{
							end = true;
							XActivityindicator.showToast("已全部加载完毕！");
						}
					}

					tuanApater.notifyDataSetChanged();

				}
			}
		});

	}


	/**
	 * 进入搜索
	 */
	public void search() {

		String searchstr = mytext.getText().toString().trim();

		if(lastKey.equals(searchstr))
		{
			return;
		}

		lastKey = searchstr;

		//int index = DataCache.getInstance().searchKeys.getSearchKeys().indexOf(searchstr);
		DataCache.getInstance().searchKeys.getSearchKeys().remove(searchstr);
		DataCache.getInstance().searchKeys.getSearchKeys().add(0,searchstr);

		XAPPUtil.saveAPPCache("SearchKeys",DataCache.getInstance().searchKeys);

		lishiAdapter.notifyDataSetChanged();

		page = 1;
		end = false;
		getPrilist();

		lishiLayout.setVisibility(View.GONE);
		mainLayout.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			getPrilist();
		} else if (v instanceof ImageView) {

			lishiLayout.setVisibility(View.VISIBLE);
			mainLayout.setVisibility(View.GONE);
			lastKey = "";
			mytext.setText("");
			quxiao.setText("取消");
		}
	}

	public void do_clean(View v)
	{
		DataCache.getInstance().searchKeys.getSearchKeys().clear();
		XAPPUtil.saveAPPCache("SearchKeys",DataCache.getInstance().searchKeys);
		lishiAdapter.notifyDataSetChanged();
	}


	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}







	class SearchLishiAdapter extends BaseAdapter {
		Context context;

		public SearchLishiAdapter(Context context) {
			this.context = context;

		}

		@Override
		public int getCount() {
			return DataCache.getInstance().searchKeys.getSearchKeys().size();
		}

		@Override
		public Object getItem(int position) {
			return DataCache.getInstance().searchKeys.getSearchKeys().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			getItemView bundle ;

			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.serach_lishi_cell, null);
				bundle = new getItemView();
				bundle.work=(TextView)convertView.findViewById(R.id.search_lishi_cell_word);
				convertView.setTag(bundle);
			}
			else
			{
				bundle = (getItemView) convertView.getTag();
			}

			String str = DataCache.getInstance().searchKeys.getSearchKeys().get(position);

			bundle.work.setText(str);

			return convertView;
		}

		private class getItemView {
			TextView work;
		}
	}




}