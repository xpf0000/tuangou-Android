package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import com.alibaba.fastjson.JSONObject;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.Emailtest;
import com.X.tcbj.utils.MyhttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admins on 2016/4/18.
 */
public class BackMoney extends Activity implements View.OnClickListener {
    EditText name, phonenumber, bankname, banknumber, txt;
    TextView backname, banktxt;
    View popView;
    PopupWindow popupWindow;
    LinearLayout banknamelay;
    Button submit;
    ImageView abut_img;
    String orderNumber, userName, bankName, bankNumber, content, telphone, url, urlstr, postr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backmoney);
        orderNumber = getIntent().getStringExtra("ordernumber");
        intview();
    }

    private void intview() {
        abut_img = (ImageView) findViewById(R.id.abut_img);
        submit = (Button) findViewById(R.id.submit);
        banktxt = (TextView) findViewById(R.id.banktxt);
        banknamelay = (LinearLayout) findViewById(R.id.banknamelay);
        name = (EditText) findViewById(R.id.name);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        bankname = (EditText) findViewById(R.id.bankname);
        banknumber = (EditText) findViewById(R.id.banknumber);
        banknumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt = (EditText) findViewById(R.id.txt);
        backname = (TextView) findViewById(R.id.backname);
        submit.setOnClickListener(this);
        backname.setOnClickListener(this);
        abut_img.setOnClickListener(this);
    }

    private void showpop() {
        popView = getLayoutInflater().inflate(
                R.layout.setprogramme, null);
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        ListView addprolist = (ListView) popView.findViewById(R.id.programlist);
        Button canle = (Button) popView.findViewById(R.id.canle);
        final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        Map<String, String> map;
//        map = new HashMap<String, String>();
//        map.put("title", "银行卡");
//        items.add(map);
        map = new HashMap<String, String>();
        map.put("title", "支付宝");
        items.add(map);
        SimpleAdapter romadapter = new SimpleAdapter(BackMoney.this, items,
                R.layout.programtext, new String[]{"title"},
                new int[]{R.id.programomtxt});
        addprolist.setAdapter(romadapter);
        canle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        addprolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                backname.setText(items.get(position).get("title"));
//                if (position == 0) {
//                    banktxt.setText("银行卡号");
//                    banknumber.setHint("请填写银行卡号");
//                    banknamelay.setVisibility(View.VISIBLE);
//                    banknumber.setText("");
//                } else {
                    bankName = "支付宝";
                    banktxt.setText("退款帐号");
                    banknumber.setHint("请填写退款账号");
                    banknamelay.setVisibility(View.GONE);
                    banknumber.setInputType(InputType.TYPE_CLASS_TEXT);
                    banknumber.setText("");
//                }
                popupWindow.dismiss();
            }
        });
    }

    private void postStr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url, postr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(2);
                } else {
                    urlstr = object.toString();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    if (jsonObject.getIntValue("status") == 0) {
                        Toast.makeText(BackMoney.this, "提交成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("refrech", "5");
                        sendBroadcast(intent);
                        finish();
                    } else {
                        Toast.makeText(BackMoney.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(BackMoney.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backname:
                showpop();
                break;
            case R.id.submit:
                if(name.getText().toString().trim().isEmpty()){
                    Toast.makeText(BackMoney.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phonenumber.getText().toString().trim().isEmpty() || !Emailtest.isMobile(phonenumber.getText().toString().trim())){
                    Toast.makeText(BackMoney.this, "电话不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(backname.getText().toString().equals("银行卡")){
                    if (bankname.getText().toString().trim().isEmpty()) {
                        Toast.makeText(BackMoney.this, "银行名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        bankName = bankname.getText().toString().trim();
                    }
                }
                if(banknumber.getText().toString().trim().isEmpty()){
                    if(banknamelay.getVisibility() == View.GONE){
                            Toast.makeText(BackMoney.this, "退款支付宝账号不不能为空"+banknumber.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    }else{
                        if(banknumber.getText().toString().trim().isEmpty()){
                            Toast.makeText(BackMoney.this, "退款银行账号不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;
                }else{
                    if(banknamelay.getVisibility() == View.GONE){
                        if (!Emailtest.checkEmail(banknumber.getText().toString().trim()) && !Emailtest.isMobile(banknumber.getText().toString().trim())) {
                            Toast.makeText(BackMoney.this, "退款支付宝账号不合法",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else{

                    }
                }



                if(txt.getText().toString().trim().isEmpty()){
                    Toast.makeText(BackMoney.this, "退款理由不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                userName = name.getText().toString().trim();
                bankNumber = banknumber.getText().toString().trim();
                content = txt.getText().toString().trim();
                telphone = phonenumber.getText().toString().trim();
//                    bankName;
                url = Constant.url + "order/addMoneyBack?";
                postr = "orderNumber=" + orderNumber + "&userName=" + userName + "&bankName=" + bankName + "&bankNumber=" + bankNumber + "&content=" + content + "&telphone=" + telphone;
                postStr();
                break;
            case R.id.abut_img:
                finish();
                break;
        }
    }
//    private boolean checkNumber(String number){
//        boolean check=true;
//        if (!Emailtest.isMobile(number)||!Emailtest.isPhone(number)){
//            check=true;
//        }else {
//            check=false;
//        }
//        return check;
//    }
}
