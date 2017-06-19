package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.NearbyModel;
import com.X.model.StoresListModel;
import com.X.model.StoresModel;
import com.X.model.TuanModel;
import com.X.server.DataCache;
import com.X.tcbj.adapter.SearchLishiAdapter;
import com.X.tcbj.adapter.StoresAdapter;
import com.X.tcbj.adapter.TuanAdapter;
import com.X.tcbj.utils.XHtmlVC;
import com.X.tcbj.utils.XPostion;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class StoresSearchVC extends Activity implements View.OnClickListener {

    SwipeRefreshLayout swipe_refresh;
    private TextView quxiao;
    EditText mytext;
    ImageView cancel;
    ListView lishiList,mainList;
    LinearLayout lishiLayout,mainLayout;
    SearchLishiAdapter lishiAdapter;
    int page = 1;
    boolean end = false;
    String lastKey = "";

    List<StoresModel> tuanList = new ArrayList<>();
    private StoresAdapter tuanApater;

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
        mytext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        quxiao.setOnClickListener(new View.OnClickListener() {
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

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.setClass(StoresSearchVC.this, XHtmlVC.class);
                intent.putExtra("url","http://www.tcbjpt.com/wap/index.php?ctl=store&act=app_index&data_id="+tuanList.get(position).getId());
                intent.putExtra("hideNavBar",true);
                startActivity(intent);

            }
        });

        lishiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = lishiAdapter.getKeys().get(position);
                mytext.setText(str);
                search();

            }
        });

        tuanApater=new StoresAdapter(tuanList,this);
        mainList.setAdapter(tuanApater);

        lishiAdapter = new SearchLishiAdapter(this,DataCache.getInstance().storesSearchKeys.getSearchKeys());
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

        XNetUtil.Handle(APPService.stores_search(page+"", city_id,key, x, y), new XNetUtil.OnHttpResult<StoresListModel>() {
            @Override
            public void onError(Throwable e) {
                swipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(StoresListModel model) {
                swipe_refresh.setRefreshing(false);
                if(model != null)
                {
                    List<StoresModel> list = model.getItem();

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

        //int index = DataCache.getInstance().storesSearchKeys.getSearchKeys().indexOf(searchstr);
        DataCache.getInstance().storesSearchKeys.getSearchKeys().remove(searchstr);
        DataCache.getInstance().storesSearchKeys.getSearchKeys().add(0,searchstr);

        XAPPUtil.saveAPPCache("StoresSearchKeys",DataCache.getInstance().storesSearchKeys);

        lishiAdapter.setKeys(DataCache.getInstance().storesSearchKeys.getSearchKeys());

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
        DataCache.getInstance().storesSearchKeys.getSearchKeys().clear();
        XAPPUtil.saveAPPCache("StoresSearchKeys",DataCache.getInstance().storesSearchKeys);
        lishiAdapter.setKeys(DataCache.getInstance().storesSearchKeys.getSearchKeys());
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
