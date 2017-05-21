package com.X.tcbj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.fragment.OrderProductFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MyOrderActivity extends FragmentActivity{
    private RadioButton radioButton_left;
    private RadioButton radioButton_right;
    private Map<String, Fragment> cache = new HashMap<String, Fragment>();
    private FragmentTransaction tx;
    private ImageView comment_back;
    private TextView tv_title_name;
    private Button comment_delec;
    private String userid;
    private Button btn;
    private LinearLayout ll_radiobtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myorder);
        userid = PreferencesUtils.getString(MyOrderActivity.this, "userid");
        initview();
        initFragment();
    }

    public void initview(){
        tv_title_name=(TextView)findViewById(R.id.tv_title_name);
        tv_title_name.setText("我的订单");
        comment_delec=(Button)findViewById(R.id.comment_delec);

        comment_back=(ImageView)findViewById(R.id.comment_back);
        comment_back.setOnClickListener(new MyClick());
        radioButton_left = (RadioButton) this.findViewById(R.id.rb_left);
        radioButton_right = (RadioButton) this.findViewById(R.id.rb_right);
        radioButton_left.setText("商品");
        radioButton_left.setTextColor(Color.RED);

        ll_radiobtn=(LinearLayout)findViewById(R.id.ll_radiobtn);
        ll_radiobtn.setVisibility(View.GONE);
//        radioButton_right.setText("团购");

        radioButton_right.setTextColor(Color.GRAY);
        radioButton_left.setOnClickListener(new MyClick());
        radioButton_right.setOnClickListener(new MyClick());
        radioButton_left.setChecked(true);
        btn=(Button)findViewById(R.id.btn_search_order);
        btn.setOnClickListener(new MyClick());
    }
    /**
     * 初始化fragment 展示首页隐藏其他界面
     */
    public void initFragment(){
        cache.put("orderProductFragment", new OrderProductFragment());
//        cache.put("ordergroupFragment", new OrderGroupFragment());

        tx=getSupportFragmentManager().beginTransaction();
        tx.add(R.id.fragment_container, cache.get("orderProductFragment"));
//        tx.add(R.id.fragment_container, cache.get("ordergroupFragment"));

//        tx.hide(cache.get("ordergroupFragment"));

        tx.commit();
    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_left:

                    radioButton_left.setTextColor(Color.RED);
//                    radioButton_right.setTextColor(Color.GRAY);
                    showFragment("orderProductFragment");
                    break;
//                case R.id.rb_right:
//                    showFragment("ordergroupFragment");
//                    radioButton_left.setTextColor(Color.GRAY);
//                    radioButton_right.setTextColor(Color.RED);
//                    break;
                case R.id.comment_back:
                    MyOrderActivity.this.finish();
                    break;
                case R.id.btn_search_order:
                    Intent intent=new Intent(MyOrderActivity.this,OrderProSearchActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
    /**
     *
     * @param tag show界面，隐藏其他界面
     */
    public void showFragment(String tag){
        tx = getSupportFragmentManager().beginTransaction();
        for(Map.Entry<String, Fragment> entry : cache.entrySet()){
            tx.hide(entry.getValue());
        }
        tx.show(cache.get(tag));
        tx.commit();
    }


}
