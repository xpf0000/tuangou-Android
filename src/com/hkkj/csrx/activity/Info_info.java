package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.AsyncImageLoader;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.hkkj.csrx.utils.Timechange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class Info_info extends Activity implements OnClickListener {
    TextView year_new, author_new, seer_new, title_new, speak_new, author;
    String info, nextstr, commenturl, cimstr, imgurl, distinctCount;
    ImageView back, more;
    ArrayList<HashMap<String, String>> infoarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> infohashMap;
    ArrayList<HashMap<String, String>> nxtarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> nexthashMap;
    String newsID, newsClassID, nexturl;
    int areaID;
    Button next, last, send, info_freshen;
    LinearLayout info_info_layout;
    EditText editText;
    Object object;
    private String[] orderStr = new String[]{"评论", "分享"};
    private PopupWindow popupWindow;
    List<Map<String, Object>> popList = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> map;
    View popView;
    LinearLayout layout;
    private AsyncImageLoader ImageLoader;
    LinearLayout info_layout, info_shuaxin;
    Timechange timechange;
    Dialog dialog;
    DisplayMetrics dm;
    WebView web;
    String newurl, newsstr;
    String content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_info);
        ShareSDK.initSDK(this);
        web = (WebView) findViewById(R.id.web);
        web.setVerticalScrollBarEnabled(false); //垂直不显示
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        author = (TextView) findViewById(R.id.author);
        year_new = (TextView) findViewById(R.id.year_new);
        author_new = (TextView) findViewById(R.id.author_new);
        layout = (LinearLayout) findViewById(R.id.myonfoview);
        seer_new = (TextView) findViewById(R.id.seer_new);
        title_new = (TextView) findViewById(R.id.title_new);
        speak_new = (TextView) findViewById(R.id.speak_new);
        info_info_layout = (LinearLayout) findViewById(R.id.info_info_layout);
        last = (Button) findViewById(R.id.news_last);
        last.setOnClickListener(this);
        next = (Button) findViewById(R.id.news_next);
        next.setOnClickListener(this);
        newsID = getIntent().getStringExtra("id");
        back = (ImageView) findViewById(R.id.info_info_back);
        back.setOnClickListener(this);
        more = (ImageView) findViewById(R.id.news_top_more);
        more.setOnClickListener(this);
        send = (Button) findViewById(R.id.sen_btn);
        send.setOnClickListener(this);
        info_freshen = (Button) findViewById(R.id.info_freshen);
        editText = (EditText) findViewById(R.id.info_ed);
        info_layout = (LinearLayout) findViewById(R.id.info_layout);
        info_shuaxin = (LinearLayout) findViewById(R.id.info_shuaxin);
        newsClassID = getIntent().getStringExtra("newsClassID");
        imgurl=getIntent().getStringExtra("picId");
        areaID = PreferencesUtils.getInt(Info_info.this, "cityID");
        info_layout.setVisibility(View.GONE);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialog = GetMyData.createLoadingDialog(Info_info.this, "正在拼命的加载······");
        dialog.show();
//		infourl = Constant.url+"news/newsDetail?newsID="
//				+ newsID;
        nexturl = Constant.url + "news/newsPreNext?areaID="
                + areaID + "&newsID=" + newsID + "&classID=" + newsClassID;
        commenturl = Constant.url + "news/addNewsComment?";
        newurl = Constant.url + "news/newsDetailNew?newsID=" + newsID;
        info(1, newurl);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
//				if (infoarray.size() == 0) {
//					handler.sendEmptyMessage(5);
//				} else {
//					title_new.setText(infoarray.get(0).get("sortTitle"));
//					year_new.setText(infoarray.get(0).get("addTime"));
//					author_new.setText("来源:"+infoarray.get(0).get("newsFrom"));
//					seer_new.setText("浏览" + infoarray.get(0).get("clickNum"));
//					speak_new.setText("评论"
//							+ infoarray.get(0).get("commentCount"));
                    handler.sendEmptyMessage(2);
//				}
                    break;
                case 2:
//				new AsyncImageLoadersdcard();
//				ImageLoader = new AsyncImageLoader();
//				String a = infoarray.get(0).get("contentList");
//				try {
//					JSONArray jsonArray = new JSONArray(a);
//					for (int i = 0; i < jsonArray.length(); i++) {
//						JSONObject object = jsonArray.getJSONObject(i);
//						int b = object.getInt("type");
//						final String detail = object.getString("info");
//
//						if (b == 1) {
//							imgurl=detail;
//							int h = object.getInt("height");
//							int w = object.getInt("width");
//							float x = (float) ((float) h / (float) w);
//							ImageView imageView = new ImageView(Info_info.this);
//							LayoutParams params = new LayoutParams(
//									dm.widthPixels, (int) (dm.widthPixels * x));
//							imageView.setId(1);
//							imageView.setLayoutParams(params);
//							Bitmap bitmap = ImageLoader.loadBitmap(imageView,
//									detail, new ImageCallBack() {
//
//										@Override
//										public void imageLoad(
//												ImageView imageView,
//												Bitmap bitmap) {
//											BitmapDrawable drawable = new BitmapDrawable(
//													bitmap);
//											imageView
//													.setBackgroundDrawable(drawable);
//										}
//
//									});
//							if (bitmap == null) {
//								imageView
//										.setBackgroundResource(R.drawable.head);
//							} else {
//								BitmapDrawable drawable = new BitmapDrawable(
//										bitmap);
//								imageView.setBackgroundDrawable(drawable);
//
//							}
//
//							layout.addView(imageView);
//						} else if (b == 0) {
//							detail.replaceAll("\t", "");
//							String[] ars = detail.split("\r\n");
//							String t = "";
//							for (String tt : ars) {
//								if ((tt.replace(" ", "")).length() > 0) {
//
//									if (t.length() == 0) {
//										t = t + tt;
//									} else {
//										t = t + "\r\n" + tt;
//									}
//								}
//
//							}
//
//							TextView textView = new TextView(Info_info.this);
//							textView.setId(1);
//							textView.setText(t);
//							textView.setTextSize(16);
//							textView.setLineSpacing((float )1.5, (float )1.5);
//							layout.addView(textView);
//						}
//					}
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
                    try {
                        nextstr(nextstr);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    info_layout.setVisibility(View.VISIBLE);
                    info_shuaxin.setVisibility(View.GONE);
                    com.alibaba.fastjson.JSONObject jsonObjects = JSON.parseObject(newsstr);
                    com.alibaba.fastjson.JSONArray jsonArrays = jsonObjects.getJSONArray("list");
                    for (int i = 0; i < jsonArrays.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonObjecta = jsonArrays.getJSONObject(i);
//                        content = "<html>\r\n\t"
//                                + "<head>\r\n"
//                                + "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
//                                + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
//                                + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
//                                + "<style>\r\n\t "
//                                + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
//                                + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
//                                + "img {width:100%}\r\n" + "</style>\r\n\t"
//                                + "</head>\r\n" + "<body style=\"width:[width]\">\r\n"
//                                + jsonObjecta.getString("content") + "\r\n</body>" + "</html>";
                        //鹏飞修改后
                        content=jsonObjecta.getString("content") ;

                        title_new.setText(jsonObjecta.getString("title"));
                        year_new.setText(jsonObjecta.getString("addTime"));
                        if (jsonObjecta.getString("author")==null||jsonObjecta.getString("author").length()==0){
                            author_new.setText("来源:" + jsonObjecta.getString("newsFrom"));
                        }else {
                            author_new.setText("作者:" + jsonObjecta.getString("author"));
                        }
                        seer_new.setText("浏览" + jsonObjecta.getString("clickNum"));
                        speak_new.setText("评论"
                                + jsonObjecta.getString("commentCount"));
                        distinctCount = jsonObjecta.getString("distinctCount");
                    }
                    String input = "(<(\\/)?p>)|\\s";
                    Pattern p = Pattern.compile(input);
//                    Matcher m = p.matcher(content);
//                    content = m.replaceAll("\r\n");
                    web.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

                    break;
                case 3:
                    if (object == null) {
                        Toast.makeText(Info_info.this, "响应失败", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        String str = object.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            String statusMsg = jsonObject.getString("statusMsg");
                            Toast.makeText(Info_info.this,
                                    statusMsg, Toast.LENGTH_SHORT)
                                    .show();
                            if (jsonObject.getInt("status") == 0) {
                                editText.setText("");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    dialog.dismiss();
                    Toast.makeText(Info_info.this, "没有数据", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 5:
                    dialog.dismiss();
                    info_shuaxin.setVisibility(View.VISIBLE);
                    Toast.makeText(Info_info.this, "网络超时", Toast.LENGTH_SHORT)
                            .show();

                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void info(final int what, final String url) {
        new Thread() {
            public void run() {
                Looper.prepare();
                if (what == 3) {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    object = myhttpRequest.request(url, cimstr, "POST");
                    handler.sendEmptyMessage(3);
                } else {
                    HttpRequest httpRequest = new HttpRequest();
//					info = httpRequest.doGet(url, Info_info.this);
                    nextstr = httpRequest.doGet(nexturl, Info_info.this);
                    newsstr = httpRequest.doGet(newurl, Info_info.this);
                    if (newsstr.equals("网络超时")) {
                        handler.sendEmptyMessage(5);
                    } else {
//						try {
////							imgurl(info);
//
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
                        Message message = new Message();
                        message.what = what;
                        handler.sendEmptyMessage(what);
                        Looper.loop();
                    }

                }

            }

            ;
        }.start();

    }

    @Override
    protected void onDestroy() {
        content="https://www.baidu.com/";
//        web.loadDataWithBaseURL(content);
        web.loadUrl(content);
        super.onDestroy();
    }


//	public void imgurl(String str) throws JSONException {
//		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
//				.parseObject(str);
//		int a = jsonObject.getIntValue("status");
//		if (a == 1) {
//			Message message = new Message();
//			message.what = 3;
//			handler.sendEmptyMessage(4);
//		} else if (str.equals("网络超时")) {
//			handler.sendEmptyMessage(5);
//		} else {
//			com.alibaba.fastjson.JSONArray jsonArray = jsonObject
//					.getJSONArray("list");
//			for (int i = 0; i < jsonArray.size(); i++) {
//				infohashMap = new HashMap<String, String>();
//				com.alibaba.fastjson.JSONObject jsonObject2 = jsonArray
//						.getJSONObject(i);
//				infohashMap.put("addTime",
//						jsonObject2.getString("addTime") == null ? ""
//								: jsonObject2.getString("addTime"));
//				infohashMap.put("areaId",
//						jsonObject2.getString("areaId") == null ? ""
//								: jsonObject2.getString("areaId"));
//				infohashMap.put("author",
//						jsonObject2.getString("author") == null ? ""
//								: jsonObject2.getString("author"));
//				infohashMap.put("newsFrom",
//						jsonObject2.getString("newsFrom") == null ? ""
//								: jsonObject2.getString("newsFrom"));
//				infohashMap.put("clickNum",
//						jsonObject2.getString("clickNum") == null ? ""
//								: jsonObject2.getString("clickNum"));
//				infohashMap.put("sortTitle",
//						jsonObject2.getString("title") == null ? ""
//								: jsonObject2.getString("title"));
//				infohashMap.put("commentCount",
//						jsonObject2.getString("commentCount"));
//				infohashMap.put("distinctCount",
//						jsonObject2.getString("distinctCount"));
//				infohashMap.put("contentList",
//						jsonObject2.getString("contentList"));
//				infoarray.add(infohashMap);
//			}
//
//		}
//	}

    public void nextstr(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        int a = jsonObject.getInt("status");
        if (a == 1) {
            Message message = new Message();
            message.what = 4;
            handler.sendEmptyMessage(4);
        } else if (jsonObject.getString("status").equals("网络超时")) {
            handler.sendEmptyMessage(5);
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                nexthashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                if (jsonObject2.getString("title").equals("news")) {
                    nexthashMap.put("title", "没有了");
                } else {
                    nexthashMap.put("title", jsonObject2.getString("title"));
                    nexthashMap.put("iD", jsonObject2.getString("id"));
                    nexthashMap.put("newsClassID",
                            jsonObject2.getString("newsClassId"));
                }

                nxtarray.add(nexthashMap);

            }

            if (nxtarray.size() == 0) {
                next.setText("下一篇：没有了");
                last.setText("上一篇：没有了");
            } else {
                try {
                    next.setText("下一篇： " + nxtarray.get(0).get("title"));
                    last.setText("上一篇： " + nxtarray.get(1).get("title"));

                } catch (Exception e) {
                    last.setText("上一篇：没有了");
                }
            }


        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.info_info_back:
                finish();
                break;
            case R.id.news_top_more:
                showOrderPopView();
                break;
            case R.id.news_last:
                if (last.getText().toString().equals("上一篇：没有了")) {
                    Toast.makeText(Info_info.this, "已经是第一篇的", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", nxtarray.get(1).get("iD"));
                    intent.putExtra("newsClassID",
                            nxtarray.get(1).get("newsClassID"));
                    intent.putExtra("picId",
                            "");
                    intent.setClass(Info_info.this, Info_info.class);
                    Info_info.this.startActivity(intent);
                }
                break;
            case R.id.news_next:
                if (next.getText().toString().equals("下一篇：没有了")) {
                    Toast.makeText(Info_info.this, "已经是最后的", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", nxtarray.get(0).get("iD"));
                    intent.putExtra("newsClassID",
                            nxtarray.get(0).get("newsClassID"));
                    intent.putExtra("picId",
                            "");
                    intent.setClass(Info_info.this, Info_info.class);
                    Info_info.this.startActivity(intent);
                }
                break;
            case R.id.sen_btn:
                String contents = editText.getText().toString();
                if (contents.trim().length() == 0) {
                    Toast.makeText(Info_info.this, "请输入评论内容", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    int Logn = PreferencesUtils.getInt(Info_info.this, "logn");
                    if (Logn == 0) {
                        Intent intent = new Intent();
                        intent.setClass(Info_info.this, LoginActivity.class);
                        Info_info.this.startActivity(intent);
                    } else {
                        String userid = PreferencesUtils.getString(Info_info.this,
                                "userid");
                        cimstr = "userID=" + userid + "&newsID=" + newsID
                                + "&areaID=" + areaID + "&contents=" + contents;
                        info(3, commenturl);
                    }

                }

                break;
            case R.id.info_freshen:
                dialog = GetMyData.createLoadingDialog(Info_info.this,
                        "正在拼命的加载······");
                dialog.show();
                info(1, newurl);
                break;
            default:
                break;
        }

    }

    public void showOrderPopView() {
        popView = Info_info.this.getLayoutInflater().inflate(
                R.layout.news_share, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = Info_info.this.getWindowManager();
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
        int[] imageId = new int[]{R.drawable.news_top_share,
                R.drawable.news_top_comment};
        for (int i = 0; i < 2; i++) {
            map = new HashMap<String, Object>();
            map.put("img", imageId[i]);
            map.put("item", orderStr[i]);
            popList.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(Info_info.this, popList,
                R.layout.news_share_txt, new String[]{"img", "item"},
                new int[]{R.id.share_img, R.id.share_txt});
        listview.setAdapter(adapter);

        popupWindow.showAsDropDown(more);// 显示在筛选区域条件下
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 1) {
                    int cityID = PreferencesUtils.getInt(Info_info.this, "cityID");
                    OnekeyShare oks = new OnekeyShare();
                    oks.disableSSOWhenAuthorize();// 分享前要先授权
                    // 分享时Notification的图标和文字
//					oks.setNotification(R.drawable.ic_launcher,
//							getString(R.string.app_name));
                    if (imgurl==null||imgurl.length()==0) {
                        oks.setImageUrl("http://image.rexian.cn/images/applogo.png");
                    } else {
                        oks.setImageUrl(imgurl);
                    }
                    System.out.println("imgurl:" + imgurl);
                    String cityName = PreferencesUtils.getString(Info_info.this, "cityName");
                    oks.setTitle(title_new.getText() + "  " + cityName + "城市热线");
                    String city = PreferencesUtils.getString(Info_info.this, "cityNamepl");
                    oks.setTitleUrl("http://m.rexian.cn/news/newsDetail?msg=" + newsID + "," + newsClassID + "," + cityID);//商家地址分享
                    oks.setText(title_new.getText() + "\r\n点击查看更多" + "http://m.rexian.cn/news/newsDetail?msg=" + newsID + "," + newsClassID + "," + cityID);
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite("新闻");
                    oks.setUrl("http://m.rexian.cn/news/newsDetail?msg=" + newsID + "," + newsClassID + "," + cityID);
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl("luoyang.rexian.cn");
                    Resources res = getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
//					oks.setCustomerLogo(bmp, "城市热线", new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							Toast.makeText(Info_info.this, "拉拉", Toast.LENGTH_SHORT).show();
//							
//						}
//					});
                    oks.show(Info_info.this);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("newsID", newsID);
                    intent.putExtra("title", title_new.getText());
                    intent.putExtra("commentCount",
                            speak_new.getText());
                    intent.putExtra("distinctCount",
                            distinctCount);
                    intent.setClass(Info_info.this, News_omment.class);
                    intent.putExtra("zixun", "zixun");
                    Info_info.this.startActivity(intent);

                }

                popupWindow.dismiss();
                popupWindow = null;
            }
        });
    }
}
