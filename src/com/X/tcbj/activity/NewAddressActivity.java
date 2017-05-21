package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.loopj.android.http.RequestParams;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by Administrator on 2016/1/11.
 */
public class NewAddressActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView back;
    private EditText et_name;
    private EditText et_phone;
    private TextView tv_addre;
    private EditText et_address_detail;
    private EditText et_postal_code;
    private TextView tv_keep;
    private String sheng,shi,xian,cityCode,getcityCode;
    private String name,phone,address_detail,postal_code,addressarea;
    private String addre;
    private CheckBox cb;
    private String upid,upacceptName,upphone,upzipCode,upcityCode,upaddress,type,uparea,isdefult;
    private boolean checked=false;
    private LinearLayout ll_defult;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaddress);
        userid = PreferencesUtils.getString(NewAddressActivity.this, "userid");
        initView();

    }
    public void initView(){
        ll_defult=(LinearLayout)findViewById(R.id.ll_defult);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        tv_addre=(TextView)findViewById(R.id.tv_addre);
        tv_addre.setOnClickListener(this);
        et_address_detail=(EditText)findViewById(R.id.et_address_detail);
        et_postal_code=(EditText)findViewById(R.id.et_postal_code);
        tv_keep=(TextView)findViewById(R.id.tv_keep);
        tv_keep.setOnClickListener(this);
        cb=(CheckBox)findViewById(R.id.cb_defult);
        cb.setOnCheckedChangeListener(this);

        upid=getIntent().getStringExtra("id");
        upacceptName=getIntent().getStringExtra("acceptName");
        upphone=getIntent().getStringExtra("phone");
        upzipCode=getIntent().getStringExtra("zipCode");
        upcityCode=getIntent().getStringExtra("cityCode");
        upaddress=getIntent().getStringExtra("address");
        uparea=getIntent().getStringExtra("area");
        type=getIntent().getStringExtra("type");
        isdefult=getIntent().getStringExtra("isdefult");
        if (type.equals("up")){
            et_name.setText(upacceptName);
            et_phone.setText(upphone);
            et_address_detail.setText(upaddress);
            tv_addre.setText(uparea);
            et_address_detail.setText(upaddress);
            et_postal_code.setText(upzipCode);
        }
        if (type.equals("up")&&isdefult.equals(String.valueOf(0))){
            ll_defult.setVisibility(View.VISIBLE);
        }else {
            ll_defult.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                NewAddressActivity.this.finish();
                break;
            case R.id.tv_keep:
                    name = et_name.getText().toString();
                    phone = et_phone.getText().toString();
                    address_detail = et_address_detail.getText().toString();
                    postal_code = et_postal_code.getText().toString();
                    addressarea=tv_addre.getText().toString();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address_detail)||TextUtils.isEmpty(addressarea)) {
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(NewAddressActivity.this, "请正确填写姓名", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(phone)) {
                            Toast.makeText(NewAddressActivity.this, "请正确填写电话", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(address_detail)) {
                            Toast.makeText(NewAddressActivity.this, "请正确填写地区", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(addressarea)) {
                            Toast.makeText(NewAddressActivity.this, "请正确填写街道地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else if (!phone.matches("^(1)[0-9]{10}$")){
                        Toast.makeText(NewAddressActivity.this,"请正确填写手机号",Toast.LENGTH_SHORT).show();
                    }else if (!postal_code.matches("^\\d{6}$")) {
                        Toast.makeText(NewAddressActivity.this, "邮编格式不正确", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(type.equals("up")){
                            upData();
                            if (checked==true){
                                updIsDefault();
                            }
                        }else if (type.equals("new")) {
                            postData();

                        }
                    }

                break;
            case R.id.tv_addre:
                Intent intent=new Intent(NewAddressActivity.this,MyChooseAddreActivity.class);

                startActivityForResult(intent,100);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==200){
            cityCode=data.getExtras().getString("cityCode");
            addre=data.getExtras().getString("area");
            tv_addre.setText(addre);
//            Log.i("......",addre);
        }
    }

    public void postData(){
        String url= Constant.url+"userAddress/addUserAddress";
        RequestParams params=new RequestParams();
        params.put("userId",userid);
        params.put("acceptName",name);
        params.put("phone",phone);
        params.put("zipCode",postal_code);
        cityCode=  cityCode.substring(1,cityCode.length()-1);
        params.put("cityCode",cityCode);
        params.put("address",address_detail);
//        Log.e("acceptName",name);
//        AsyncHttpHelper.postAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
////                Toast.makeText(NewAddressActivity.this,s,Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject object = new JSONObject(s);
//                    int status = object.optInt("status");
//                    if (status == 0) {
//                        Toast.makeText(NewAddressActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
//                        NewAddressActivity.this.finish();
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
    }
    public void upData(){
        String urlup=Constant.url+"userAddress/updUserAddress";
        RequestParams params=new RequestParams();
        params.put("id",Integer.parseInt(upid));
        params.put("acceptName",name);
        params.put("phone",phone);
        params.put("zipCode", postal_code);
        if (TextUtils.isEmpty(cityCode)){
            getcityCode=upcityCode;
        }else {
            getcityCode=cityCode;
        }
        params.put("cityCode",getcityCode);
        params.put("address", address_detail);

//        AsyncHttpHelper.postAbsoluteUrl(urlup, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
////                Log.e("........", s);
//
//                Toast.makeText(NewAddressActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                NewAddressActivity.this.finish();
//            }
//        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b==true){
            checked=true;
        }else {
            checked=false;
        }
    }
    public void updIsDefault(){

        String upisurl=Constant.url+"userAddress/updIsDefault";
        RequestParams params=new RequestParams();
        params.put("userId",userid);
        params.put("id",Integer.parseInt(upid));
//        AsyncHttpHelper.getAbsoluteUrl(upisurl, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
////                Log.i("设置默认地址成功", s);
//            }
//        });
    }
    /**
     * 手机号验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}
