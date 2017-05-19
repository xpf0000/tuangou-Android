package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/29.
 */
public class yuediancheng extends Activity implements View.OnClickListener
{
    private TextView jieguo,shouye,dianpu,liebiao,name;
    private ImageView back;
    private String name1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuediancheng);
        init();
    }

    private void init()
    {
        liebiao=(TextView)findViewById(R.id.liebiao);
        liebiao.setOnClickListener(this);
        dianpu=(TextView)findViewById(R.id.dianpu);
        dianpu.setOnClickListener(this);
        shouye=(TextView)findViewById(R.id.shouye);
        shouye.setOnClickListener(this);
        name=(TextView)findViewById(R.id.name);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
       jieguo=(TextView)findViewById(R.id.jieguo);
                name1= getIntent().getStringExtra("name");
        if(name1.equals("1"))
        {
        name.setText("约店");}else
        {
           name.setText("在线预订");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
         case R.id.shouye:
             Intent intent=new Intent();
             intent.setClass(yuediancheng.this,HomePageActivity.class);
             startActivity(intent);
             finish();
             break;
         case R.id.dianpu:
             finish();
             break;
         case R.id.liebiao:
             Intent intent1=new Intent();
             intent1.setClass(yuediancheng.this,ShangjiaActivicty.class);
             startActivity(intent1);
             finish();
             break;
        }

    }
}
