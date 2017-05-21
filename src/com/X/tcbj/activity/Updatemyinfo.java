package com.X.tcbj.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.server.HKService;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Updatemyinfo extends Activity {
    String likename, ename, sex, time, love, actionUrl;
    EditText liname, name;
    TextView shengri;
    ImageView imageView;
    RadioGroup groupsex, grouplove;
    int marriage, mysex;
    private final static int DATE_DIALOG = 0;
    private Calendar c = null;
    Button sureup;
    RadioButton man, women, suerlove, nolove, orlove;
    Dialog dialog;
    int picid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatemyinfo);
        liname = (EditText) findViewById(R.id.updatelikename);
        groupsex = (RadioGroup) findViewById(R.id.upsex);
        man = (RadioButton) findViewById(R.id.man);
        women = (RadioButton) findViewById(R.id.women);
        suerlove = (RadioButton) findViewById(R.id.surelove);
        nolove = (RadioButton) findViewById(R.id.nolove);
        grouplove = (RadioGroup) findViewById(R.id.uplove);
        orlove = (RadioButton) findViewById(R.id.orlove);
        imageView = (ImageView) findViewById(R.id.upinfo_back);
        name = (EditText) findViewById(R.id.upname);
        sureup = (Button) findViewById(R.id.sureup);
        shengri = (TextView) findViewById(R.id.shengri);
        likename = getIntent().getStringExtra("likename");
        time = getIntent().getStringExtra("time");
        ename = getIntent().getStringExtra("ename");
        mysex = getIntent().getExtras().getInt("sex");
        marriage = getIntent().getExtras().getInt("love");
        actionUrl = Constant.url + "userinfo/upDateUserInfo";
        Intent intent = new Intent(this, HKService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(HKService.action);
        registerReceiver(broadcastReceiver, filter);
        if (marriage == 0) {
            nolove.setChecked(true);
        } else if (marriage==1){
orlove.setChecked(true);
        }
        else {
            suerlove.setChecked(true);
        }
        if (mysex == 1) {
            man.setChecked(true);
        } else {
            women.setChecked(true);
        }
        shengri.setText(time);
        liname.setText(likename);
        name.setText(ename);
        shengri.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG);

            }
        });
        groupsex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) Updatemyinfo.this
                        .findViewById(radioButtonId);
                if (rb.getText().equals("男")) {
                    mysex = 1;
                } else {
                    mysex = 0;
                }

            }
        });
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(Updatemyinfo.this, Myinfo.class);
                Updatemyinfo.this.startActivity(intent);
                finish();

            }
        });
        grouplove.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (nolove.isChecked()) {
                    marriage = 0;

                } else if (orlove.isChecked()) {
                    marriage = 1;

                } else {
                    marriage = 2;

                }

            }
        });
        sureup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String userid = PreferencesUtils.getString(Updatemyinfo.this,
                        "userid");
                String nickname = liname.getText().toString();
                String truename = name.getText().toString();
                String birthday = shengri.getText().toString();
                dialog = GetMyData
                        .createLoadingDialog(Updatemyinfo.this, "请稍等");
                dialog.show();
                info(userid, nickname, truename, mysex, birthday, marriage,
                        Updatemyinfo.this);

            }
        });

    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                shengri.setText(year + "-" + (month + 1) + "-"
                                        + dayOfMonth);
                            }
                        }, c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
        }
        return dialog;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    Toast.makeText(Updatemyinfo.this, "成功", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent1 = new Intent();
                    intent1.setAction("com.servicedemo4");
                    intent1.putExtra("refrech", "10");
                    sendBroadcast(intent1);
                    Intent intent = new Intent();
                    intent.setClass(Updatemyinfo.this, Myinfo.class);
                    Updatemyinfo.this.startActivity(intent);
                    finish();
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(Updatemyinfo.this, "失败", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 11:
                    dialog.dismiss();
                    Toast.makeText(Updatemyinfo.this, "网络超时", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void info(final String userid, final String nickname,
                     final String truename, final int sex, final String birthday,
                     final int marriage, final Context context) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Looper.prepare();
                    String a = uploadFile(actionUrl, userid, nickname, truename, sex,
                            birthday, marriage);
                    if (a.equals("")) {
                        handler.sendEmptyMessage(11);
                    } else {
                        JSONObject jsonObject = new JSONObject(a);
                        ;
                        int status = jsonObject.getInt("status");
                        Message message = new Message();
                        message.what = status;
                        handler.sendEmptyMessage(status);
                    }
                    Looper.loop();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }.start();

    }

    public String uploadFile(String Url, final String userid,
                             final String nickname, final String truename, final int sex,
                             final String birthday, final int marriage) {
        StringBuilder sb2 = new StringBuilder();
        String BOUNDARY = "ARCFormBoundaryp859n1863ri19k9";
        String PREFIX = "--", LINEND = "\r\n";
        // String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL httpurl;
        try {
            httpurl = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) httpurl
                    .openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());

            StringBuilder sb = new StringBuilder();

            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "userId"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(userid);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "nickName"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(nickname);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "trueName"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(truename);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "birthday"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(birthday);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "sex" + "\""
                    + LINEND);
            sb.append(LINEND);
            sb.append(sex);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "marriage"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(marriage);
            sb.append(LINEND);

            outStream.write(sb.toString().getBytes());

            StringBuilder sb1 = new StringBuilder();
            sb1.append(PREFIX);
            sb1.append(BOUNDARY);
            sb1.append(LINEND);
            sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
                    + "123.jpg" + "\"" + LINEND);
            sb1.append("Content-Type: image/jpg; charset=" + CHARSET + LINEND);
            sb1.append(LINEND);
            sb1.append(LINEND);
            outStream.write(sb1.toString().getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            int res = conn.getResponseCode();
            InputStream in = null;
            if (res == 201 || res == 200) {
                in = conn.getInputStream();
                int ch;

                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(Updatemyinfo.this, "图片上传操作失败", Toast.LENGTH_SHORT)
                    .show();
        }
        return sb2.toString();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }
}
