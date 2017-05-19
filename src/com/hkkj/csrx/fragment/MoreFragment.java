package com.hkkj.csrx.fragment;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.About;
import com.hkkj.csrx.activity.Feedback;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.UpdatePop;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.CheckSwitchButton;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetVersion;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多
 * 
 * @author zpp
 * @version1.0
 */
public class MoreFragment extends Fragment {
	private View view;
	ListView listView1, listView2;
	int a = 0, b = 1;
	CheckSwitchButton button;
	File cacheDir;
	String dd="0";
	SimpleAdapter adapter2 ;
	SimpleAdapter adapter;
	String huancun;
	String ver, verstr, verurl;
	UpdatePop myPopwindows;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ver = GetVersion.getAppVersionName(getActivity());
		myPopwindows=new UpdatePop();
		verurl = Constant.url + "Version/GetVersion?Ver=" + ver;
		view = inflater.inflate(R.layout.fragment_more, container, false);
		a = PreferencesUtils.getInt(getActivity(), "photo");
		listView1 = (ListView) view.findViewById(R.id.more_list1);
		listView2 = (ListView) view.findViewById(R.id.more_list2);
		cacheDir = new File("/data/data/com.hkkj.csrx.activity/cache/");
		button = (CheckSwitchButton) view.findViewById(R.id.mCheckSwithcButton);
		b = PreferencesUtils.getInt(getActivity(), "photo");
		if (b == 2) {
			button.setChecked(false);
		} else {
			button.setChecked(true);
		}
		// 清除缓存
		final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		Map<String, String> item = new HashMap<String, String>();
		huancun="清除缓存";
		item.put("txt", huancun);
		items.add(item);
		adapter = new SimpleAdapter(getActivity(), items,
				R.layout.moreset_txt, new String[] { "txt" },
				new int[] {R.id.eeeeeeeee});
		listView1.setAdapter(adapter);
		// 更新 以及关于
		List<Map<String, String>> item1 = new ArrayList<Map<String, String>>();
		final String[] text = new String[] { "软件更新", "意见反馈", "关于" };
		for (int i = 0; i < 3; i++) {
			Map<String, String> item2 = new HashMap<String, String>();
			item2.put("textItems", text[i]);
			item1.add(item2);
		}
		adapter2= new SimpleAdapter(getActivity(), item1,
				R.layout.moreabout_txt, new String[] { "textItems" },
				new int[] { R.id.about });
		listView2.setAdapter(adapter2);
		// 清除缓存
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					getFolderSize(cacheDir);
					DecimalFormat fnum = new DecimalFormat("##0.0");
					dd = fnum.format(getFolderSize(cacheDir));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delete(cacheDir);
				Toast.makeText(getActivity(), "清理"+dd+"M缓存", Toast.LENGTH_SHORT)
						.show();
			}
		});
		// 更新以及关于
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					getupdate();
				} else if (arg2 == 1) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), Feedback.class);
					MoreFragment.this.startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), About.class);
					MoreFragment.this.startActivity(intent);
				}
			}
		});
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					PreferencesUtils.putInt(getActivity(), "photo", 1);
					Toast.makeText(getActivity(), "无图模式已关闭", Toast.LENGTH_SHORT)
							.show();
					;
				} else {
					PreferencesUtils.putInt(getActivity(), "photo", 2);
					Toast.makeText(getActivity(), "无图模式已打开", Toast.LENGTH_SHORT)
							.show();
					;
				}

			}
		});
		return view;

	}

	public static float getFolderSize(File file) throws Exception {
		float size = 0;
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainScreen"); 
	}
	private void getupdate(){
//		AsyncHttpHelper.getAbsoluteUrl(verurl, new TextHttpResponseHandler() {
//			@Override
//			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//				Toast.makeText(getActivity(), "网络似乎有问题了", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onSuccess(int y, Header[] headers, String s) {
//				JSONObject jsonObject = JSON.parseObject(s);
//				if (jsonObject.getIntValue("status") == 0) {
//					JSONArray jsonArray = jsonObject.getJSONArray("list");
//					for (int i=0;i<jsonArray.size();i++){
//						JSONObject jsonObject1=jsonArray.getJSONObject(i);
//						if (jsonObject1.getBoolean("OrUp")) {
//							String update = jsonObject1.getString("Content");
//							final String url=jsonObject1.getString("Url");
//							myPopwindows.showpop(getActivity(), update,handler);
//							myPopwindows.setMyPopwindowswListener(new UpdatePop.MyPopwindowsListener() {
//								@Override
//								public void onRefresh() {
//									Uri uri = Uri.parse(url);
//									DownloadManager downloadManager = (DownloadManager)getActivity(). getSystemService(getActivity().DOWNLOAD_SERVICE);
//									DownloadManager.Request request = new DownloadManager.Request(uri);
//									request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
//									request.setDestinationInExternalPublicDir("appupdate","update.apk");
//									request.setVisibleInDownloadsUi(true);
//									long downLoadId = downloadManager.enqueue(request);
//								}
//							});
//						}
//					}
//				}
//			}
//		});
	}
}
