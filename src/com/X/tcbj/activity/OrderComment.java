package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.AddImgAdapter;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.FileUtils;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admins on 2016/6/20.
 */
public class OrderComment extends Activity implements View.OnClickListener {
    ImageView prologo, logn_img;
    TextView title, money;
    EditText edcon;
    RatingBar ratingbar;
    MyGridView photos;
    Button login;
    String orderNumber, PicID, ProductTitle, str;
    Double TruePrice;
    int ProductID;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    AddImgAdapter adapter;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordercomment);
        orderNumber = getIntent().getStringExtra("orderNumber");
        ProductID = getIntent().getIntExtra("ProductID", 0);
        PicID = getIntent().getStringExtra("PicID");
        ProductTitle = getIntent().getStringExtra("ProductTitle");
        TruePrice = getIntent().getDoubleExtra("TruePrice", 0.0);
        intview();
        setHashMap();
    }

    private void intview() {
        dialog = GetMyData.createLoadingDialog(this,
                "正在提交······");
//        MultiImageSelector.create(this)
//                .start(this, REQUEST_IMAGE);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        prologo = (ImageView) findViewById(R.id.prologo);
        logn_img = (ImageView) findViewById(R.id.logn_img);
        logn_img.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        money = (TextView) findViewById(R.id.money);
        edcon = (EditText) findViewById(R.id.edcon);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        photos = (MyGridView) findViewById(R.id.photos);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        options = ImageUtils.setcenterOptions();
        title.setText(ProductTitle);
        ImageLoader.displayImage(PicID, prologo, options, animateFirstListener);
        money.setText("￥" + TruePrice);
    }

    public void postComm() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String Url = Constant.url + "order/addOrderComment";
                int siteId = PreferencesUtils.getInt(OrderComment.this, "cityID");
                String userId = PreferencesUtils.getString(OrderComment.this, "userid");
                float starNum = ratingbar.getRating();
                String contents = edcon.getText().toString();
                str = uploadFile(Url, siteId, userId, ProductID, starNum, orderNumber, contents);
                if (str.equals("失败") || str.isEmpty()) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(3);
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
//                    int num;
//                    if (mSelectPath.size() == 1 || mSelectPath.get(0) == null) {
//                        num = 6;
//                    } else {
//                        num = 5;
//                    }
//                    int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
//                    Intent intent = new Intent(OrderComment.this, MultiImageSelectorActivity.class);
//                    // 是否显示拍摄图片
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//                    // 最大可选择图片数量
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, num);
//                    // 选择模式
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
//                    // 默认选择
//                    if (mSelectPath != null && mSelectPath.size() > 0) {
//                        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
//                    }
//                    startActivityForResult(intent, REQUEST_IMAGE);
                    break;
                case 2:
                    Toast.makeText(OrderComment.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dialog.dismiss();
                    JSONObject jsonObject = JSON.parseObject(str);
                    if (jsonObject.getIntValue("status") == 0) {
                        Toast.makeText(OrderComment.this,"评论成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(OrderComment.this,"评论失败",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    private void setHashMap() {
        mSelectPath = new ArrayList<String>();
        mSelectPath.add(null);
        adapter = new AddImgAdapter(mSelectPath, OrderComment.this, handler);
        photos.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == OrderComment.RESULT_OK) {
//                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                if (mSelectPath.size() == 6) {
//                    mSelectPath.remove(0);
//                } else if (mSelectPath.size() == 0) {
//                    mSelectPath.add(null);
//                }
                adapter = new AddImgAdapter(mSelectPath, OrderComment.this, handler);
                photos.setAdapter(adapter);
            }
        }
    }

    public String uploadFile(String Url, int siteId, String userId, int productId, float starNum, String orderNumber, String contents) {
        StringBuilder sb2 = new StringBuilder();
        String BOUNDARY = "ARCFormBoundarymmd8a874lsor";
        String PREFIX = "--", LINEND = "\r\n";
        // String MULTIPART_FROM_DATA = "multipart/form-data";
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志
        String CHARSET = "UTF-8";
        URL httpurl;
        try {
            httpurl = new URL(Url);
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
            sb.append("Content-Disposition: form-data; name=\"" + "siteId"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(siteId);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "productId" + "\""
                    + LINEND);
            sb.append(LINEND);
            sb.append(productId);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "userId" + "\""
                    + LINEND);
            sb.append(LINEND);
            sb.append(userId);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "starNum"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(starNum);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "orderNumber"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(orderNumber);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "contents"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(contents);
            sb.append(LINEND);

            outStream.write(sb.toString().getBytes());
            if (mSelectPath.size() == 1 && mSelectPath.get(0) == null) {

            } else {
                for (int i = 0; i < mSelectPath.size(); i++) {
                    String filePath;
                     filePath = mSelectPath.get(i);
                    if (filePath==null){
                         filePath = mSelectPath.get(i+1);
                        i=i+1;
                    }
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = BitmapFactory.decodeFile(filePath.toString());
                    File file = FileUtils.saveBitmap(bm, fileName, filePath.toString());
                    filePath = file.toString();
                    String name = "imgFile" + i;
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append("--");
                    sb1.append(BOUNDARY);
                    sb1.append("\r\n");
                    sb1.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
                            + "123.jpg" + "\"" + LINEND);
                    sb1.append("Content-Type: image/jpg; charset="
                            + CHARSET + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                    }


            }
            outStream.write(endline.getBytes());
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
            String str = "失败";
            return str;
        }
        return sb2.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (edcon.getText().toString().trim().length() == 0) {
                    Toast.makeText(OrderComment.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    postComm();
                }
                break;
            case R.id.logn_img:
                finish();
                break;
        }
    }
}
