package com.X.tcbj.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Comment_adpater;
import com.X.tcbj.adapter.Information_adpater;
import com.X.tcbj.adapter.SemPhNews;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.tcbj.utils.StringtoJson;
import com.X.server.HKService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class SeminarFragment extends Fragment {
	View view;
	String templateId, content, commenstr;
	WebView semweb;
	Information_adpater newsadapter;
	ListView newslistView, newscolistView;
	String infostr;
	ArrayList<HashMap<String, Object>> infoarray = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> infohashMap;
	StringtoJson stringtoJson;
	String title, id, typeId, cimstr;
	int c = 0, areaId = 0, page = 1,  site = 0,iscanvote;
	GridView meetingpops;
	SemPhNews sempadapter;
	Comment_adpater comadapter;
	VoteAdapter voteadapter;
	String canvote;
	EditText comment_ed;
	Button comment_btn;
	String commenturl = Constant.url+"topics/addComment?";
	String userId, myinfo,utlstr;
	Dialog dialog;
	int type=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dialog = GetMyData.createLoadingDialog(getActivity(),
				"正在获取投票权限..");
		Intent intent = new Intent(getActivity(), HKService.class);
		getActivity().startService(intent);
		IntentFilter filter = new IntentFilter(HKService.action);
		getActivity().registerReceiver(broadcastReceiver, filter);
		Bundle mBundle = getArguments();
		templateId = mBundle.getString("templateId");
		title = mBundle.getString("arg");
		stringtoJson = new StringtoJson();
		areaId = PreferencesUtils.getInt(getActivity(), "cityID");
		typeId = mBundle.getString("id");
		String newsinfo = PreferencesUtils.getString(getActivity(), title);
		id = mBundle.getString("topicId");
		if (templateId==null){
			templateId = mBundle.getString("templateId");
		}
		if (Integer.parseInt(templateId) == 11) {
			view = inflater.inflate(R.layout.seminar_info, container, false);
			content = mBundle.getString("content");
			semweb = (WebView) view.findViewById(R.id.semweb);
			semweb.getSettings().setJavaScriptEnabled(true);
			semweb.setVerticalScrollBarEnabled(false); // 垂直不显示
			semweb.setWebChromeClient(new WebChromeClient());
			semweb.loadDataWithBaseURL(null, content, "text/html", "utf-8",
					null);
		} else if (Integer.parseInt(templateId) <= 7) {
			view = inflater.inflate(R.layout.sem_newslist, container, false);
			newslistView = (ListView) view.findViewById(R.id.newslist);

			setnewslistView();

			if (newsinfo != null) {
				// System.out.println("bbbb");
				// getNewsList(newsinfo);
				c = 1;
				infostr = newsinfo;
				handler.sendEmptyMessage(1);
				setNewsList();

			} else {
				String url = Constant.url+"topics/news?id="
						+ id
						+ "&areaId="
						+ areaId
						+ "&page="
						+ page
						+ "&pagesize=10&typeId=" + typeId;
				getlist(url);
				setNewsList();
			}
		} else if (Integer.parseInt(templateId) > 7
				&& Integer.parseInt(templateId) <= 10) {
			view = inflater.inflate(R.layout.semphnews, container, false);
			meetingpops = (GridView) view.findViewById(R.id.meetingpops);
			setnewsgrid();
			if (newsinfo != null) {
				infostr = newsinfo;
				handler.sendEmptyMessage(1);
				setphnews();
			} else {
				String url = Constant.url+"topics/picNews?id="
						+ id
						+ "&areaId="
						+ areaId
						+ "&page="
						+ page
						+ "&pagesize=10&typeId=" + typeId;

				getlist(url);
				setphnews();
			}
		} else if (Integer.parseInt(templateId) == 12) {
			view = inflater.inflate(R.layout.semcomment, container, false);
			newscolistView = (ListView) view.findViewById(R.id.semcom_list);
			int Logn = PreferencesUtils.getInt(getActivity(), "logn");
			getmorecom();
			if (newsinfo != null) {
				infostr = newsinfo;
				handler.sendEmptyMessage(1);
				setcoment();
			} else {
				String url;
				if (Logn == 0) {
					url = Constant.url+"topics/comment?id="
							+ id + "&userid=0&page=" + page + "&pagesize=10";
				} else {
					userId = PreferencesUtils
							.getString(getActivity(), "userid");
					url = Constant.url+"topics/comment?id="
							+ id
							+ "&userid="
							+ userId
							+ "&page="
							+ page
							+ "&pagesize=10";
				}
				getlist(url);
				setcoment();
			}
		} else if (Integer.parseInt(templateId) == 14) {
			view = inflater.inflate(R.layout.semphnews, container, false);
			meetingpops = (GridView) view.findViewById(R.id.meetingpops);
			setvoteclick();
			if (newsinfo != null) {
				infostr = newsinfo;
				handler.sendEmptyMessage(1);
				setVote();
			} else {
				int Logn = PreferencesUtils.getInt(getActivity(), "logn");
				if (Logn == 0) {
					userId="0";
				}else {
					userId = PreferencesUtils
							.getString(getActivity(), "userid");
				}
				String url = Constant.url+"topics/voteNew?id="
						+ id
						+ "&type="
						+ typeId
						+ "&page="
						+ page
						+ "&pagesize=10&userId="+userId;
				getlist(url);
				setVote();
			}
		}
		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (Integer.parseInt(templateId) > 7
						&& Integer.parseInt(templateId) <= 10) {
					getPhNews(infostr);
					sempadapter.notifyDataSetChanged();
				} else if (Integer.parseInt(templateId) == 12) {
					getcomlist(infostr);
					try {
						comadapter.notifyDataSetChanged();
					} catch (Exception e) {
						Toast.makeText(getActivity(), "慢点点", Toast.LENGTH_SHORT).show();
					}
					
				} else if (Integer.parseInt(templateId) == 14) {
					try {
						getVotelist(infostr);
						dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					voteadapter.notifyDataSetChanged();
					dialog.dismiss();

				} else {
					getNewsList(infostr);
					newsadapter.notifyDataSetChanged();
				}

				break;
			case 2:
				try{
					Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 3:
				try {
					Toast.makeText(getActivity(), "暂无更多内容", Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 4:
				Constant.stite=0;
				site = 1;
				userId = PreferencesUtils
						.getString(getActivity(), "userid");
				String Strurl = Constant.url+"topics/addLike?";
						utlstr="userid="
						+ userId + "&commentid=" + Constant.commentId;
				getlist(Strurl);
				break;
			case 5:
				infohashMap = new HashMap<String, Object>();
				infohashMap.put("canVote", "0");
				infohashMap.put("id", infoarray.get(Constant.Votepo).get("id").toString());
				infohashMap.put("title",
						infoarray.get(Constant.Votepo).get("title"));
				int a = Integer.parseInt(infoarray.get(Constant.Votepo).get(
						"voteCount").toString().toString()) + 1;
				infohashMap.put("voteCount", a + "");
				infohashMap.put("url", infoarray.get(Constant.Votepo)
						.get("url"));
				infohashMap.put("value",
						infoarray.get(Constant.Votepo).get("value"));
				infohashMap.put("topicMenuId", infoarray.get(Constant.Votepo)
						.get("topicMenuId"));
				infohashMap.put("topicId",
						infoarray.get(Constant.Votepo).get("topicId"));
				infohashMap.put("picture",
						infoarray.get(Constant.Votepo).get("picture"));
				infohashMap.put("names",
						infoarray.get(Constant.Votepo).get("names"));
				infoarray.set(Constant.Votepo, infohashMap);
				voteadapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "投票成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case 6:
				Toast.makeText(getActivity(), "投票失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 7:
				dialog.dismiss();
				JSONObject jsonObject = JSONObject.parseObject(commenstr);
				int a1 = jsonObject.getIntValue("status");
				if (a1 == 0) {
					Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT)
							.show();
					String userId = PreferencesUtils.getString(getActivity(),
							"userid");
					comment_ed.setText("");
					type=1;
					// String userId="1606";
					String url = Constant.url+"topics/comment?id="
							+ id
							+ "&userid="
							+ userId
							+ "&page="
							+ page
							+ "&pagesize=10";
					

					getlist(url);
					// setcoment();
				} else {
					Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 8:
				try {
					JSONObject jsonObject1 = JSONObject.parseObject(myinfo);
					int sug = jsonObject1.getIntValue("status");
					if (sug == 0) {
						Constant.stite=1;
						Toast.makeText(getActivity(), "赞",
								Toast.LENGTH_SHORT).show();
						String userId = PreferencesUtils.getString(getActivity(),
								"userid");
						infoarray.clear();

						// String userId="1606";
						String url = Constant.url+"topics/comment?id="
								+ id
								+ "&userid="
								+ userId
								+ "&page="
								+ page
								+ "&pagesize=10";

						getlist(url);
					} else {
						Constant.stite=1;
						Toast.makeText(getActivity(), "您已经赞过啦",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(), "失败",
							Toast.LENGTH_SHORT).show();
				}
					

				
				break;

			default:
				break;
			}
		};
	};

	void getlist(final String url) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				System.out.println(url);
				if (url.equals(commenturl)) {
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					System.out.println(cimstr);
					Object object = myhttpRequest.request(url, cimstr, "POST");
					if (object == null) {
						handler.sendEmptyMessage(2);
					} else {
						commenstr = object.toString();
						handler.sendEmptyMessage(7);
					}

				} else if (site == 1) {
					Looper.prepare();
					
					MyhttpRequest myhttpRequest = new MyhttpRequest();
					Object object = myhttpRequest.request(url, utlstr, "POST");
					if (object == null) {
						handler.sendEmptyMessage(2);
						
					} else {
						myinfo = object.toString();
						handler.sendEmptyMessage(8);
					}
					site=0;
					
					
					Looper.loop();
				}

				else {

					Looper.prepare();
					HttpRequest httpRequest = new HttpRequest();
					infostr = httpRequest.doGet(url, getActivity());
//					if (Integer.parseInt(templateId) == 14) {
//						String url = Constant.url+"topics/isCanVote?topicsId="
//								+ id + "&menuId=" + typeId + "&areaId="+areaId;
//						canvote = httpRequest.doGet(url, getActivity());
//						JSONObject jsonObject = JSONObject.parseObject(canvote);
////						System.out.println("canvote:" + canvote);
////						System.out.println("url:" + url);
//						int a = jsonObject.getIntValue("status");
//						if (a == 23) {
//							iscanvote = 0;
//						} else {
//							iscanvote = 1;
//						}
//						try {
//							PreferencesUtils.putInt(getActivity(), title + "iscanvote", iscanvote);
//						}catch (Exception e){
//							PreferencesUtils.putInt(getActivity(), title + "iscanvote", iscanvote);
//						}
//					}
					if (infostr.equals("网络超时")) {
						handler.sendEmptyMessage(2);
						Looper.loop();
					} else {
						handler.sendEmptyMessage(1);
						Looper.loop();
					}
				}
			}
		}.start();

	}

	// 解析资讯数据
	void getNewsList(String str) {
		JSONObject jsonObject = JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 0) {
			JSONArray array = jsonObject.getJSONArray("list");
			if (array.size() == 0) {
				handler.sendEmptyMessage(3);
				if (page - 1 != 0) {
					page = page - 1;
				}
			} else {

				for (int i = 0; i < array.size(); i++) {
					infohashMap = new HashMap<String, Object>();
					JSONObject jsonObject2 = array.getJSONObject(i);
					infohashMap.put("title",
							jsonObject2.getString("title") == null ? ""
									: jsonObject2.getString("title"));
					infohashMap.put("description", jsonObject2
							.getString("description") == null ? ""
							: jsonObject2.getString("description"));
					infohashMap.put("clickNum",
							jsonObject2.getString("clickNum") == null ? ""
									: jsonObject2.getString("clickNum"));
					if (c == 1) {
						infohashMap.put("picId",
								jsonObject2.getString("picId") == null ? ""
										: jsonObject2.getString("picId"));
					} else {
						infohashMap.put("picId", jsonObject2
								.getString("picture") == null ? ""
								: jsonObject2.getString("picture"));
					}
					infohashMap.put("content",
							jsonObject2.getString("content") == null ? ""
									: jsonObject2.getString("content"));
					infoarray.add(infohashMap);
				}
				String myjson = stringtoJson.getJson(infoarray, 0);
				try {
					PreferencesUtils.putString(getActivity(), title, myjson);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} else {
			handler.sendEmptyMessage(3);
		}

	}

	// 装在咨询数据
	void setNewsList() {
		newsadapter = new Information_adpater(getActivity(), infoarray);
		newslistView.setAdapter(newsadapter);
	}

	// newslistView监听
	void setnewslistView() {
		newslistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("content", infoarray.get(arg2).get("content").toString());
				intent.setClass(getActivity(), SemNewsInfo.class);
				getActivity().startActivity(intent);

			}
		});
		newslistView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case OnScrollListener.SCROLL_STATE_IDLE:
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							page++;
							String url = Constant.url + "topics/news?id="
									+ id
									+ "&areaId="
									+ areaId
									+ "&page="
									+ page
									+ "&pagesize=10&typeId=" + typeId;

							getlist(url);
						}
						break;

					default:
						break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	// 图片资讯
	void setphnews() {
		sempadapter = new SemPhNews(infoarray, getActivity());
		meetingpops.setAdapter(sempadapter);
	}

	// 解析图片资讯数据
	void getPhNews(String str) {
		JSONObject jsonObjec = JSONObject.parseObject(str);
		int a = jsonObjec.getIntValue("status");
		if (a == 0) {
			JSONArray array = jsonObjec.getJSONArray("list");
			if (array.size() == 0) {
				handler.sendEmptyMessage(3);
				if (page - 1 != 0) {
					page = page - 1;
				}
			} else {
				for (int i = 0; i < array.size(); i++) {
					infohashMap = new HashMap<String, Object>();
					JSONObject jsonObject = array.getJSONObject(i);
					infohashMap.put("picture",
							jsonObject.getString("picture") == null ? ""
									: jsonObject.getString("picture"));
					infohashMap.put("title",
							jsonObject.getString("title") == null ? ""
									: jsonObject.getString("title"));
					infohashMap.put("sortTitle",
							jsonObject.getString("sortTitle") == null ? ""
									: jsonObject.getString("sortTitle"));
					infohashMap.put("id",
							jsonObject.getString("id") == null ? ""
									: jsonObject.getString("id"));
					infohashMap.put("url",
							jsonObject.getString("url") == null ? ""
									: jsonObject.getString("url"));
					infoarray.add(infohashMap);
				}
				String myjson = stringtoJson.getJson(infoarray, 0);
				try {
					PreferencesUtils.putString(getActivity(), title, myjson);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} else {
			handler.sendEmptyMessage(3);
		}

	}

	void setnewsgrid() {
		meetingpops.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				System.out.println("aaaa："+infoarray.get(arg2).get("url").toString().length());
				if (infoarray.get(arg2).get("url").toString().length()!=0){
					intent.putExtra("url", infoarray.get(arg2).get("url").toString());
					intent.setClass(getActivity(), webiew.class);
					getActivity().startActivity(intent);
				}else {

					intent.putExtra("iD", infoarray.get(arg2).get("id").toString());
					intent.setClass(getActivity(), ImgNewsInfo.class);
					getActivity().startActivity(intent);
				}


			}
		});
		meetingpops.setOnScrollListener(new OnScrollListener() {
			boolean isLastRow = false;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isLastRow
						&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					page++;
					String url = Constant.url + "topics/picNews?id="
							+ id
							+ "&areaId="
							+ areaId
							+ "&page="
							+ page
							+ "&pagesize=10&typeId=" + typeId;
					getlist(url);
				}
				isLastRow = false;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0) {

					isLastRow = true;
				} else {

				}

			}
		});
	}

	void setcoment() {
		comadapter = new Comment_adpater(infoarray, getActivity(), handler);
		newscolistView.setAdapter(comadapter);
	}

	// 解析评论数据
	void getcomlist(String str) {
		JSONObject jsonObject = JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 0) {
			JSONArray array = jsonObject.getJSONArray("list");
			if(type==1){
				infoarray.clear();
			}
			type=0;
			if (array.size() == 0) {
				handler.sendEmptyMessage(3);
				if (page - 1 != 0) {
					page = page - 1;
				}
			} else {
				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonObject2 = array.getJSONObject(i);
					infohashMap = new HashMap<String, Object>();
					infohashMap.put("userPicID",
							jsonObject2.getString("userPicID") == null ? ""
									: jsonObject2.getString("userPicID"));
					infohashMap.put("likeNum",
							jsonObject2.getString("likeNum") == null ? ""
									: jsonObject2.getString("likeNum"));
					infohashMap.put("addTime",
							jsonObject2.getString("addTime") == null ? ""
									: jsonObject2.getString("addTime"));
					infohashMap.put("nickName",
							jsonObject2.getString("nickName") == null ? ""
									: jsonObject2.getString("nickName"));
					infohashMap.put("contents",
							jsonObject2.getString("contents") == null ? ""
									: jsonObject2.getString("contents"));
					infohashMap.put("id", jsonObject2.getString("id"));
					System.out.println(jsonObject2.getString("id"));
					infoarray.add(infohashMap);
				}
				String myjson = stringtoJson.getJson(infoarray, 0);
				try {
					PreferencesUtils.putString(getActivity(), title, myjson);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} else {
			handler.sendEmptyMessage(3);
		}

	}

	// 获取更多评论
	void getmorecom() {
		newscolistView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						page++;
						int Logn = PreferencesUtils.getInt(getActivity(),
								"logn");
						String url;
						if (Logn == 0) {
							url = Constant.url+"topics/comment?id="
									+ id
									+ "&userid=0&page="
									+ page
									+ "&pagesize=10";
						} else {
							String userId = PreferencesUtils.getString(
									getActivity(), "userid");
							url = Constant.url+"topics/comment?id="
									+ id
									+ "&userid="
									+ userId
									+ "&page="
									+ page + "&pagesize=10";
						}
						getlist(url);
					}
					break;

				default:
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		comment_ed = (EditText) view.findViewById(R.id.comment_ed);
		comment_btn = (Button) view.findViewById(R.id.comment_btn);
		comment_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String contents = comment_ed.getText().toString();
				if (contents.trim().length() == 0) {
					Toast.makeText(getActivity(), "请输入评论内容", Toast.LENGTH_SHORT)
							.show();
				} else {
					int Logn = PreferencesUtils.getInt(getActivity(), "logn");
					if (Logn == 0) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), LoginActivity.class);
						getActivity().startActivity(intent);
					} else {
						String userid = PreferencesUtils.getString(
								getActivity(), "userid");
						// String userid="1606";
						int areaID = PreferencesUtils.getInt(getActivity(),
								"cityID");
						cimstr = "userid=" + userid + "&topicsId=" + id
								+ "&areaId=" + areaID + "&context=" + contents;
						dialog= GetMyData.createLoadingDialog(getActivity(),
								"正在提交..");
						dialog.show();
						getlist(commenturl);
					}

				}

			}
		});
	}

	void getVotelist(String str) throws JSONException {
//		JSONObject jsonObject = JSONObject.parseObject(str);
		org.json.JSONObject jsonObject=new org.json.JSONObject(str);
//		int a = jsonObject.getIntValue("status");
		int a=jsonObject.getInt("status");
		if (a == 0) {
//			JSONArray array = jsonObject.getJSONArray("list");
			org.json.JSONArray array=jsonObject.getJSONArray("list");
			try {
				iscanvote=jsonObject.getInt("isCanVote");
				PreferencesUtils.putInt(getActivity(), title + "iscanvote", iscanvote);
			}catch (Exception e){
				iscanvote=PreferencesUtils.getInt(getActivity(),title + "iscanvote");
			}
			if (array.length() == 0) {
				handler.sendEmptyMessage(3);
				if (page - 1 != 0) {
					page = page - 1;
				}
			} else {
//				iscanvote=PreferencesUtils.getInt(getActivity(),title+"iscanvote");
				for (int i = 0; i < array.length(); i++) {
//					JSONObject jsonObject2 = array.getJSONObject(i);
					org.json.JSONObject  jsonObject2=array.getJSONObject(i);
					infohashMap = new HashMap<String, Object>();
					infohashMap.put("id",
							jsonObject2.getString("id") == null ? ""
									: jsonObject2.getString("id"));
					// infohashMap.put("title",
					// jsonObject2.getString("title") == null ? ""
					// : jsonObject2.getString("title"));
					infohashMap.put("value",
							jsonObject2.getString("value") == null ? ""
									: jsonObject2.getString("value"));
					infohashMap.put("picture",
							jsonObject2.getString("picture") == null ? ""
									: jsonObject2.getString("picture"));
					infohashMap.put("title",
							jsonObject2.getString("title") == null ? ""
									: jsonObject2.getString("title"));
					
//					System.out.println("jsonObject2.getString(names):"+jsonObject2.getString("names"));
//					
//					JSONArray array2=JSONObject.parseArray(jsonObject2.getString("names"));
					try {
						org.json.JSONArray array2=jsonObject2.getJSONArray("names");
						for (int j = 0; j < 1; j++) {
							
							infohashMap.put("names",array2.getString(0));
							
						}
					} catch (Exception e) {
						infohashMap.put("names",jsonObject2.getString("names"));
					}
					
					
					infohashMap.put("name",
							jsonObject2.getString("name") == null ? ""
									: jsonObject2.getString("name"));
					infohashMap.put("voteCount",
							jsonObject2.getString("voteCount") == null ? ""
									: jsonObject2.getString("voteCount"));
					infohashMap.put("url",
							jsonObject2.getString("url") == null ? ""
									: jsonObject2.getString("url"));
//					infohashMap.put("canVote",
//							jsonObject2.getString("canVote") == null ? ""
//									: jsonObject2.getString("canVote"));
					if (iscanvote != 0) {
						infohashMap.put("canVote", "113");
					} else {
						infohashMap.put("canVote", jsonObject2
								.getString("canVote") == null ? ""
								: jsonObject2.getString("canVote"));
					}
					infohashMap.put("topicMenuId", jsonObject2
							.getString("topicMenuId") == null ? ""
							: jsonObject2.getString("topicMenuId"));
					infohashMap.put("topicId",
							jsonObject2.getString("topicId") == null ? ""
									: jsonObject2.getString("topicId"));
					infoarray.add(infohashMap);
				}
				String myjson = stringtoJson.getJson(infoarray, 0);
				try {
					PreferencesUtils.putString(getActivity(), title, myjson);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} else {
			handler.sendEmptyMessage(3);
		}
	}

	void setVote() {
		voteadapter = new VoteAdapter(infoarray, getActivity(), handler);
		meetingpops.setAdapter(voteadapter);

	}

	void setvoteclick() {
		meetingpops.setOnScrollListener(new OnScrollListener() {
			boolean isLastRow = false;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isLastRow
						&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					page++;
					String url = Constant.url+"topics/vote?id="
							+ id
							+ "&type="
							+ typeId
							+ "&page="
							+ page
							+ "&pagesize=10";
					getlist(url);
				}
				isLastRow = false;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0) {

					isLastRow = true;
				} else {

				}

			}
		});
	}
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// textView.setText(intent.getExtras().getString("data"));
			if (Integer.parseInt(templateId) == 14){
				dialog.show();
				int Logn = PreferencesUtils.getInt(getActivity(), "logn");
				if (Logn == 0) {
					userId="0";
				}else {
					userId = PreferencesUtils
							.getString(getActivity(), "userid");
				}
				String url = Constant.url+"topics/voteNew?id="
						+ id
						+ "&type="
						+ typeId
						+ "&page="
						+ page
						+ "&pagesize=10&userId="+userId;
				infoarray.clear();
				getlist(url);
			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}
}
