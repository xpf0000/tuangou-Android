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

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/8/3.
 */
public class vipxpinglun extends Activity implements View.OnClickListener
{
    MyGridView photos;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    AddImgAdapter adapter;
    Button button;
    Dialog dialog;
    String str,storeId;
    String  startNum;
    RatingBar ratingBar;// 商家星级评定
    private EditText edcon;
    ImageView img_shop,fanhui;// 店家图片
    TextView txt_name;// 商家名称
    TextView txt_address;// 商圈
    TextView txt_class;// 分类
String className,address;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipxpinglun);
        storeId = PreferencesUtils.getString(vipxpinglun.this,
                "storeID");
       className= getIntent().getStringExtra("className");
        address=getIntent().getStringExtra("address");
        intview();
        setHashMap();

    }

    private void setHashMap()
    {
        mSelectPath = new ArrayList<String>();
        mSelectPath.add(null);
        adapter = new AddImgAdapter(mSelectPath,vipxpinglun.this, handler);
        photos.setAdapter(adapter);

    }
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    int num;
                    if (mSelectPath.size() == 1 || mSelectPath.get(0) == null) {
                        num = 6;
                    } else {
                        num = 5;
                    }
                    int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                    Intent intent = new Intent(vipxpinglun.this, MultiImageSelectorActivity.class);
                    // 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    // 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, num);
                    // 选择模式
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                    // 默认选择
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                    }
                    startActivityForResult(intent,REQUEST_IMAGE);
                    break;
                case 2:
                    Toast.makeText(vipxpinglun.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dialog.dismiss();
                    JSONObject jsonObject = JSON.parseObject(str);
                    if (jsonObject.getIntValue("status") == 0) {
                        Toast.makeText(vipxpinglun.this,"评论成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(vipxpinglun.this,"评论失败",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == OrderComment.RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (mSelectPath.size() == 6) {
                    mSelectPath.remove(0);
                } else if (mSelectPath.size() == 0) {
                    mSelectPath.add(null);
                }
                adapter = new AddImgAdapter(mSelectPath,vipxpinglun.this, handler);
                photos.setAdapter(adapter);
            }
        }
    }
    public String uploadFile(String Url, int siteId, String userId, String storeId, String starNum, String contents) {
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
            sb.append("Content-Disposition: form-data; name=\"" + "areaId"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(siteId);
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
            sb.append("Content-Disposition: form-data; name=\"" + "storeId" + "\""
                    + LINEND);
            sb.append(LINEND);
            sb.append(storeId);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);


            sb.append("Content-Disposition: form-data; name=\"" + "title"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append("88888");
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "startNum"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(startNum);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

//            sb.append("Content-Disposition: form-data; name=\"" + "orderNumber"
//                    + "\"" + LINEND);
//            sb.append(LINEND);
//            sb.append(orderNumber);
//            sb.append(LINEND);
//            sb.append(PREFIX);
//            sb.append(BOUNDARY);
//            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "content"
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
                    bm.recycle();
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

    private void intview()
    {
        dialog = GetMyData.createLoadingDialog(this,
                "正在提交······");
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        img_shop = (ImageView) findViewById(R.id.img_shops_discuss_picture);
        txt_name = (TextView) findViewById(R.id.txt_shops_discuss_name);
        fanhui=(ImageView)findViewById(R.id.imgview_shops_discuss_back);
        fanhui.setOnClickListener(this);
        txt_address = (TextView) findViewById(R.id.txt_shops_discuss_address);
        txt_class = (TextView) findViewById(R.id.txt_shops_discuss_class);
        ImageLoader.displayImage(Constant.SHOP_IMG, img_shop, options,
                animateFirstListener);
        txt_name.setText(Constant.SHOP_NAME);// 店家名称
        txt_address.setText(address);
        txt_class.setText(className);
        photos = (MyGridView) findViewById(R.id.photos);
        button=(Button)findViewById(R.id.btn_shops_discuss_submit);
        edcon=(EditText)findViewById(R.id.et_shops_discuss_context);
        button.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

    }

    @Override
    public void onClick(View v)
    {
         switch (v.getId())
         {
             case R.id.btn_shops_discuss_submit:
                 int starNumber = ((int) ratingBar.getRating()) * 2;
                 // System.out.println("实际评星：" + ratingBar.getRating());
                 // 确定商家星级
                 startNum = String.valueOf(starNumber);
                 if (edcon.getText().toString().trim().length()<2||edcon.getText().toString().trim().length()>500) {
                     Toast.makeText(vipxpinglun.this, "字数限定2到500字", Toast.LENGTH_SHORT).show();
                 } else if(ratingBar.getRating()==0)
                 {
                     Toast.makeText(vipxpinglun.this, "请评分，最小为1星",
                             Toast.LENGTH_SHORT).show();
                 }

                 else {
                     dialog.show();
                     postComm();
                 }


                 break;
             case R.id.imgview_shops_discuss_back:
                 finish();
                 break;

         }

    }

    private void postComm()
    {

        new Thread() {
            @Override
            public void run() {
                super.run();

                String Url = Constant.url+"store/storecomment";// 图片上传地址
                int siteId = PreferencesUtils.getInt(vipxpinglun.this, "cityID");
                String userId = PreferencesUtils.getString(vipxpinglun.this, "userid");

                String contents = edcon.getText().toString();
                str = uploadFile(Url,siteId, userId,storeId,startNum,contents);
                if (str.equals("失败") || str.isEmpty()) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(3);
                }
            }
        }.start();

    }
}
