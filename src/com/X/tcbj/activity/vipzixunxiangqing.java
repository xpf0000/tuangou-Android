package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/7/30.
 */
public class vipzixunxiangqing extends Activity implements View.OnClickListener {

    Button send;
    View popView;
    Object object;
    private PopupWindow popupWindow;
    HashMap<String, Object> map;
    private String[] orderStr = new String[]{"评论", "分享"};
    List<Map<String, Object>> popList = new ArrayList<Map<String, Object>>();
    String imgurl, cimstr;
    EditText editText;
    private TextView title, data, liulan;
    private WebView webiew;
    private String url, str, id;
    HashMap<String, String> hashMap;
    private ImageView back, more;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(vipzixunxiangqing.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    jiexi();
                    break;
                case 3:
                    title.setText(hashMap.get("title"));
                    data.setText(hashMap.get("date"));
                    liulan.setText("浏览" + hashMap.get("clicknum"));
                    webiew.loadDataWithBaseURL(null, hashMap.get("content"), "text/html", "utf-8", null);
                    break;
                case 4:
                    if (object == null) {
                        Toast.makeText(vipzixunxiangqing.this, "响应失败", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        String str = object.toString();
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(str);
                            String statusMsg = jsonObject.getString("statusMsg");
                            Toast.makeText(vipzixunxiangqing.this,
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
            }
        }
    };

    private void jiexi() {
        JSONObject jsonObject2 = JSON.parseObject(str);
        int a = jsonObject2.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            hashMap = new HashMap<String, String>();
            hashMap.put("clicknum", jsonObject.getString("clicknum") == null ? "" : jsonObject.getString("clicknum"));
            hashMap.put("content", jsonObject.getString("content") == null ? "" : jsonObject.getString("content"));
            hashMap.put("title", jsonObject.getString("title") == null ? "" : jsonObject.getString("title"));
            hashMap.put("date", jsonObject.getString("date") == null ? "" : jsonObject.getString("date"));
            handler.sendEmptyMessage(3);
            imgurl = jsonObject.getString("picID");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipzixunxiang);
        id = getIntent().getStringExtra("id");
        url = Constant.url + "getVipStoreNewsInfo?" + "&newsId=" + id;
        init();
        lianwang();
    }

    private void lianwang() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                str = httpRequest.doGet(url, vipzixunxiangqing.this);
                if (str.equals("网络超时")) {
                    handler.sendEmptyMessage(1);
                } else {

                    handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webiew = (WebView) findViewById(R.id.web);
        webiew.setVerticalScrollBarEnabled(false); // 垂直不显示
        webiew.getSettings().setJavaScriptEnabled(true);
        webiew.setWebChromeClient(new WebChromeClient());
        title = (TextView) findViewById(R.id.title);
        data = (TextView) findViewById(R.id.data);
        liulan = (TextView) findViewById(R.id.liulan);
        editText = (EditText) findViewById(R.id.info_ed);
        more = (ImageView) findViewById(R.id.news_top_more);
        more.setOnClickListener(this);
        send = (Button) findViewById(R.id.sen_btn);
        send.setOnClickListener(this);
    }

    //发表评论
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sen_btn:
                String contents = editText.getText().toString();
                if (contents.trim().length() == 0) {
                    Toast.makeText(vipzixunxiangqing.this, "请输入评论内容", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    int Logn = PreferencesUtils.getInt(vipzixunxiangqing.this, "logn");
                    if (Logn == 0) {
                        Intent intent = new Intent();
                        intent.setClass(vipzixunxiangqing.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (contents.length() > 500) {
                        Toast.makeText(this, "评论内容不能多于500字符", Toast.LENGTH_SHORT).show();
                    } else {
                        String userid = PreferencesUtils.getString(vipzixunxiangqing.this,
                                "userid");
                        int areaID = PreferencesUtils.getInt(vipzixunxiangqing.this, "cityID");
                        cimstr = "userID=" + userid + "&newsID=" + id
                                + "&areaID=" + areaID + "&contents=" + contents;
                        final String curl = Constant.url + "news/addInforMationComment";
                        info(curl);
                    }
                }
                break;
            case R.id.news_top_more:
                showOrderPopView();
                break;
            default:
                break;
        }
    }

    public void showOrderPopView() {
        popView = vipzixunxiangqing.this.getLayoutInflater().inflate(
                R.layout.news_share, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = vipzixunxiangqing.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
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

        SimpleAdapter adapter = new SimpleAdapter(vipzixunxiangqing.this, popList,
                R.layout.news_share_txt, new String[]{"img", "item"},
                new int[]{R.id.share_img, R.id.share_txt});
        listview.setAdapter(adapter);

        popupWindow.showAsDropDown(more);// 显示在筛选区域条件下
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 1) {
                    int cityID = PreferencesUtils.getInt(vipzixunxiangqing.this, "cityID");
                    OnekeyShare oks = new OnekeyShare();
                    oks.disableSSOWhenAuthorize();// 分享前要先授权
                    if (imgurl == null || imgurl.trim().length() == 0) {
                        oks.setImageUrl("http://image.rexian.cn/images/applogo.png");
                    } else {
                        oks.setImageUrl(imgurl);
                    }
                    String cityName = PreferencesUtils.getString(vipzixunxiangqing.this, "cityName");
                    oks.setTitle(title.getText() + "  " + cityName + "城市热线");
                    String city = PreferencesUtils.getString(vipzixunxiangqing.this, "cityNamepl");
                    oks.setTitleUrl("http://m.rexian.cn/Store/getVipStoreNewsInfo?newsId=" + id + "&areaId=" + cityID);//商家地址分享
                    oks.setText(title.getText() + "\r\n点击查看更多" + "http://m.rexian.cn/Store/getVipStoreNewsInfo?newsId=" + id + "&areaId=" + cityID);
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite("新闻");
                    oks.setUrl("http://m.rexian.cn/Store/getVipStoreNewsInfo?newsId=" + id + "&areaId=" + cityID);
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl("luoyang.rexian.cn");
//                  Resources res = getResources();
//                  Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
//					oks.setCustomerLogo(bmp, "城市热线", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							Toast.makeText(Info_info.this, "拉拉", Toast.LENGTH_SHORT).show();
//
//						}
//					});
                    oks.show(vipzixunxiangqing.this);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("newsID", id);
                    intent.putExtra("title", title.getText());
                    intent.setClass(vipzixunxiangqing.this, News_omment.class);
                    intent.putExtra("zixun", "shangjia");
                    startActivity(intent);
                }
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
    }

    public void info(final String curl) {
        new Thread() {
            public void run() {
                Looper.prepare();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                object = myhttpRequest.request(curl, cimstr, "POST");
                handler.sendEmptyMessage(4);
            }

            ;
        }.start();
    }

}
