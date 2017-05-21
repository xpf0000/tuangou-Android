package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/29.
 */
public class Yuediansb extends Activity implements View.OnClickListener
{
    private TextView textView,dianpu,liebiao,shouye,name;
    private String type,name1;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuediansb);
        type=getIntent().getStringExtra("type").toString();
        name1=getIntent().getStringExtra("name");
        init();


    }

    private void init()
    {
        dianpu=(TextView)findViewById(R.id.dianpu);
        liebiao=(TextView)findViewById(R.id.liebiao);
        shouye=(TextView)findViewById(R.id.shouye);
        dianpu.setOnClickListener(this);
        name=(TextView)findViewById(R.id.name);
        if(name1.equals("1"))
        {
           name.setText("约店");
        }else
        {
           name.setText("在线预订");
        }
        liebiao.setOnClickListener(this);
        shouye.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.jieguo);
        if(type.equals("t"))
        {
            textView.setText("您的预约太快,\n服务器忙不过来了,\n稍后再约吧.");

        }else
        {
            textView.setText("您的预约太多,\n一定累了吧,\n明天再约.");
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
                intent.setClass(Yuediansb.this,HomePageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.dianpu:
                finish();
                break;
            case R.id.liebiao:
                Intent intent1=new Intent();
                intent1.setClass(Yuediansb.this,ShangjiaActivicty.class);
                startActivity(intent1);
                finish();
                break;
        }

    }
}
