package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Comment_adpater;
import com.hkkj.csrx.myview.MyListView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.MyhttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class News_omment extends Activity {
    ListView new_colist;
    MyListView hot_colist;
    private View loadMoreView, myviewlay;
    TextView commentp, commentc, tv_load_more, pn_txtle;
    String listurl, hoturl;
    ArrayList<HashMap<String, Object>> infoarray = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> infohashMap;
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> hashMap;
    String hotinfo, info, myinfo, newsID, title, commentCount, distinctCount,
            userID, cimstr, commenturl;
    Comment_adpater adpater, listadpater;
    DisplayMetrics dm;
    int pageIndex = 1, totalRecord = 0, c, areaID;
    LinearLayout mylistciew, comment_shuaxin, send_commeent;
    Button comment_freshen, comment_btn;
    EditText comment_ed;
    Object object;
    ImageView info_info_back;
    String zanstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_comment);
        newsID = getIntent().getStringExtra("newsID");
        title = getIntent().getStringExtra("title");
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        commentCount = getIntent().getStringExtra("commentCount");
        distinctCount = getIntent().getStringExtra("distinctCount");
        comment_shuaxin = (LinearLayout) findViewById(R.id.comment_shuaxin);
        mylistciew = (LinearLayout) findViewById(R.id.mylistciew);
        comment_freshen = (Button) findViewById(R.id.comment_freshen);
        comment_btn = (Button) findViewById(R.id.comment_btn);
        comment_ed = (EditText) findViewById(R.id.comment_ed);
        new_colist = (ListView) findViewById(R.id.new_colist);
        send_commeent = (LinearLayout) findViewById(R.id.send_commeent);
        info_info_back = (ImageView) findViewById(R.id.info_info_back);
        pn_txtle = (TextView) findViewById(R.id.pn_txtle);
        areaID = PreferencesUtils.getInt(News_omment.this, "cityID");
        loadMoreView = LayoutInflater.from(News_omment.this).inflate(
                R.layout.load_more, null);
        new_colist.addFooterView(loadMoreView, null, false);
        myviewlay = LayoutInflater.from(News_omment.this).inflate(
                R.layout.hot_comment, null);
        hot_colist = (MyListView) myviewlay.findViewById(R.id.hot_colist);
        commentp = (TextView) myviewlay.findViewById(R.id.commentp);
        commentc = (TextView) myviewlay.findViewById(R.id.commentc);
        try {
            commentp.setText(commentCount.substring(2));
        } catch (Exception e) {
            commentp.setText(commentCount);
        }
        commentc.setText(distinctCount);
        pn_txtle.setText(title);
        int Logn = PreferencesUtils.getInt(News_omment.this, "logn");
        if (Logn == 0) {
            userID = "0";
        } else {
            userID = PreferencesUtils.getString(News_omment.this, "userid");
        }
        tv_load_more = (TextView) loadMoreView.findViewById(R.id.tv_load_more);
        hoturl = Constant.url + "news/hotcommentList?newsID="
                + newsID + "&num=3&userId=" + userID;
        if (getIntent().getStringExtra("zixun").equals("shangjia")) {
            pn_txtle.setVisibility(View.GONE);
            listurl = Constant.url + "news/GetInforMationCommendList?userID="
                    + userID
                    + "&newsID="
                    + newsID
                    + "&pageSize=10&pageIndex="
                    + pageIndex;
            commenturl = Constant.url + "news/addInforMationComment?";
        } else {
            pn_txtle.setVisibility(View.VISIBLE);
            new_colist.addHeaderView(myviewlay);
            listurl = Constant.url + "news/commentList?userID="
                    + userID
                    + "&newsID="
                    + newsID
                    + "&pageSize=10&pageIndex="
                    + pageIndex;
            commenturl = Constant.url + "news/addNewsComment?";
        }
        System.out.println("listurl:" + listurl);
        info(1, hoturl);
        setlust();
        new_colist.setOnScrollListener(new OnScrollListener() {

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
                            if (pageIndex == c) {
                                handler.sendEmptyMessage(2);
                            } else {
                                pageIndex++;
                                if (getIntent().getStringExtra("zixun").equals("shangjia")) {
                                    listurl = Constant.url + "news/GetInforMationCommendList?userID="
                                            + userID
                                            + "&newsID="
                                            + newsID
                                            + "&pageSize=10&pageIndex="
                                            + pageIndex;
                                } else {
                                    listurl = Constant.url + "news/commentList?userID="
                                            + userID
                                            + "&newsID="
                                            + newsID
                                            + "&pageSize=10&pageIndex=" + pageIndex;
                                }
                                info(1, hoturl);
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
        comment_freshen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                info(1, hoturl);

            }
        });
        comment_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String contents = comment_ed.getText().toString();
                if (contents.trim().length() == 0) {
                    Toast.makeText(News_omment.this, "请输入评论内容",
                            Toast.LENGTH_SHORT).show();
                } else {
                    int Logn = PreferencesUtils
                            .getInt(News_omment.this, "logn");
                    if (Logn == 0) {
                        Intent intent = new Intent();
                        intent.setClass(News_omment.this, LoginActivity.class);
                        News_omment.this.startActivity(intent);
                    } else if (contents.length() > 500) {
                        Toast.makeText(News_omment.this, "评论内容不能多于500字符", Toast.LENGTH_SHORT).show();
                    } else {
                        String userid = PreferencesUtils.getString(
                                News_omment.this, "userid");
                        cimstr = "userID=" + userid + "&newsID=" + newsID
                                + "&areaID=" + areaID + "&contents=" + contents;
                        info(6, commenturl);
                    }

                }

            }
        });
        info_info_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int b = 10;
                    c = totalRecord % b;
                    if (c != 0) {
                        c = totalRecord / b + 1;
                    } else {
                        c = totalRecord / b;
                        if (c == 0) {
                            c = 1;
                        } else {
                            c = totalRecord / b;
                        }

                    }
                    try {
                        array(info);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    listadpater.notifyDataSetChanged();
                    if (pageIndex == c) {
                        setLoadMoreText(R.string.loading_all);
                    } else {
                        setLoadMoreText(R.string.loading_more);
                    }
                    comment_shuaxin.setVisibility(View.GONE);
                    mylistciew.setVisibility(View.VISIBLE);
                    loadMoreView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(2);

                    break;
                case 2:
                    sethotlust();
                    break;
                case 3:
                    comment_shuaxin.setVisibility(View.GONE);
                    mylistciew.setVisibility(View.GONE);
                    Toast.makeText(News_omment.this, "网络异常", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 4:
                    String Strurl = "";
                    if (getIntent().getStringExtra("zixun").equals("shangjia")) {
                        Strurl = Constant.url + "news/comment/addInforMationComment";
                    } else {
                        Strurl = Constant.url + "news/comment/Like?";
                    }
                    zanstr = "userID=" + userID + "&ID=" + Constant.commentId;
//				Constant.url+"news/comment/Like?userID="
//						+ userID
//						+ "&newsID="
//						+ newsID
//						+ "&ID=3&commentUserID="
//						+ Constant.commentId;
                    info(5, Strurl);

                    break;
                case 5:
                    try {
                        myinfo = object.toString();
                        JSONObject jsonObject = new JSONObject(myinfo);
                        int sug = jsonObject.getInt("status");
                        if (sug == 0) {
                            Toast.makeText(News_omment.this, "赞",
                                    Toast.LENGTH_SHORT).show();
                            infoarray.clear();
                            array.clear();
                            if (getIntent().getStringExtra("zixun").equals("shangjia")) {
                                infoz();
                            } else {
                                info(8, hoturl);
                            }
                        } else {
                            Toast.makeText(News_omment.this, "您已经赞过啦",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    if (object == null) {
                        Toast.makeText(News_omment.this, "响应失败", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        String str = object.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            String statusMsg = jsonObject.getString("statusMsg");
                            Toast.makeText(News_omment.this,
                                    statusMsg, Toast.LENGTH_SHORT)
                                    .show();
                            if (jsonObject.getInt("status") == 0) {
                                View view = getWindow().peekDecorView();
                                if (view != null) {
                                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputmanger.hideSoftInputFromWindow(
                                            view.getWindowToken(), 0);
                                }
                                comment_ed.setText("");
                                infoarray.clear();
                                array.clear();
                                if (getIntent().getStringExtra("zixun").equals("shangjia")) {
                                    infoz();
                                } else {
                                    info(8, hoturl);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case 7:
                    Toast.makeText(News_omment.this, "暂无评论！", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 8:
                    int b1 = 10;
                    c = totalRecord % b1;
                    if (c != 0) {
                        c = totalRecord / b1 + 1;
                    } else {
                        c = totalRecord / b1;
                        if (c == 0) {
                            c = 1;
                        } else {
                            c = totalRecord / b1;
                        }

                    }
                    try {
                        array(info);
                        setlust();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (pageIndex == c) {
                        setLoadMoreText(R.string.loading_all);
                    } else {
                        setLoadMoreText(R.string.loading_more);
                    }
                    comment_shuaxin.setVisibility(View.GONE);
                    mylistciew.setVisibility(View.VISIBLE);
                    loadMoreView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(2);
                    break;
                case 9:
                    listadpater.notifyDataSetChanged();
                    comment_shuaxin.setVisibility(View.GONE);
                    loadMoreView.setVisibility(View.VISIBLE);
                    mylistciew.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void setLoadMoreText(int some) {
        tv_load_more.setText(some);
    }

    public void info(final int what, final String url) {
        new Thread() {
            public void run() {
                Looper.prepare();
                HttpRequest httpRequest = new HttpRequest();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                if (what == 5) {
                    object = myhttpRequest.request(url, zanstr, "POST");
                    handler.sendEmptyMessage(5);
                } else if (what == 6) {
                    object = myhttpRequest.request(url, cimstr, "POST");
                    handler.sendEmptyMessage(6);
                } else {
                    hotinfo = httpRequest.doGet(url, News_omment.this);
                    info = httpRequest.doGet(listurl, News_omment.this);
                    if (info.equals("网络超时")) {
                        handler.sendEmptyMessage(3);
                    } else {
                        try {
                            hotarray(hotinfo);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
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

    public void infoz() {
        new Thread() {
            public void run() {
                Looper.prepare();
                HttpRequest httpRequest = new HttpRequest();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                info = httpRequest.doGet(listurl, News_omment.this);
                if (info.equals("网络超时")) {
                    handler.sendEmptyMessage(3);
                } else {
                    try {
                        array(info);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//                        Message message = new Message();
//                        message.what = 9;
                    handler.sendEmptyMessage(9);
                    Looper.loop();
                }
            }

            ;
        }.start();
    }

    public void hotarray(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        int a = jsonObject.getInt("status");
        if (a == 1) {
            // Message message = new Message();
            // message.what = 7;
            // handler.sendEmptyMessage(7);
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                infohashMap = new HashMap<String, Object>();
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                infohashMap.put("addTime", jsonObject2.getString("addTime"));
                infohashMap
                        .put("commentId", jsonObject2.getString("commentId"));
                infohashMap.put("contents", jsonObject2.getString("contents"));
                infohashMap.put("nickName", jsonObject2.getString("nickName"));
                infohashMap.put("id", jsonObject2.getString("id"));
                infohashMap.put("likeNum", jsonObject2.getString("likeNum"));
                infohashMap
                        .put("userPicID", jsonObject2.getString("userPicID"));
                infoarray.add(infohashMap);
            }
        }
    }

    public void array(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        int a = jsonObject.getInt("status");
        if (a == 1) {
            Message message = new Message();
            message.what = 7;
            handler.sendEmptyMessage(7);
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            totalRecord = jsonObject.getInt("totalRecord");
            for (int i = 0; i < jsonArray.length(); i++) {
                hashMap = new HashMap<String, Object>();
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                hashMap.put("addTime", jsonObject2.getString("addTime"));
                hashMap.put("commentId", jsonObject2.getString("commentId"));
                hashMap.put("contents", jsonObject2.getString("contents"));
                hashMap.put("nickName", jsonObject2.getString("nickName"));
                hashMap.put("id", jsonObject2.getString("id"));
                hashMap.put("likeNum", jsonObject2.getString("likeNum"));
                hashMap.put("userPicID", jsonObject2.getString("userPicID"));
                array.add(hashMap);
            }
        }
    }

    public void sethotlust() {
        adpater = new Comment_adpater(infoarray, News_omment.this, handler);
        hot_colist.setAdapter(adpater);
    }

    public void setlust() {
        listadpater = new Comment_adpater(array, News_omment.this, handler);
        new_colist.setAdapter(listadpater);
    }

}
