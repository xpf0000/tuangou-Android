package com.hkkj.csrx.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;


import com.hkkj.csrx.fragment.CollectProductFragment;
import com.hkkj.csrx.fragment.CollectShopFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgh on 2016/1/4.
 */
public class MyCollectActivity extends FragmentActivity {
    private RadioButton radioButton_left;
    private RadioButton radioButton_right;
    private Map<String, Fragment> cache = new HashMap<String, Fragment>();
    private FragmentTransaction tx;
    private ImageView comment_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mycollect);
        initview();
        initFragment();
    }
    public void initview(){
        comment_back=(ImageView)findViewById(R.id.comment_back);
        comment_back.setOnClickListener(new MyClick());
        radioButton_left = (RadioButton) this.findViewById(R.id.rb_left);
        radioButton_right = (RadioButton) this.findViewById(R.id.rb_right);
        radioButton_left.setText("商品");
        radioButton_left.setTextColor(Color.RED);
        radioButton_right.setText("店铺");
        radioButton_right.setTextColor(Color.GRAY);
        radioButton_left.setOnClickListener(new MyClick());
        radioButton_right.setOnClickListener(new MyClick());
        radioButton_left.setChecked(true);
    }
    /**
     * 初始化fragment 展示首页隐藏其他界面
     */
    public void initFragment(){
        cache.put("productFragment", new CollectProductFragment());
        cache.put("shopFragment", new CollectShopFragment());

        tx=getSupportFragmentManager().beginTransaction();
        tx.add(R.id.fragment_container, cache.get("productFragment"));
        tx.add(R.id.fragment_container, cache.get("shopFragment"));

        tx.hide(cache.get("shopFragment"));

        tx.commit();
    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_left:

                    radioButton_left.setTextColor(Color.RED);
                    radioButton_right.setTextColor(Color.GRAY);
                    showFragment("productFragment");
                    break;
                case R.id.rb_right:
                    showFragment("shopFragment");
                    radioButton_left.setTextColor(Color.GRAY);
                    radioButton_right.setTextColor(Color.RED);
                    break;
                case R.id.comment_back:
                    MyCollectActivity.this.finish();
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
