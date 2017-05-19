package com.hkkj.csrx.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.CompressPictures;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.FileUtils;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.ImageUtils;
import com.hkkj.csrx.utils.SelectPicPopupWindow;
import com.hkkj.server.HKService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 信息
 *
 * @author zpp
 * @version1.0
 */
@SuppressLint("SdCardPath")
public class Myinfo extends Activity {
    ListView listView;
    Bitmap photo;
    ImageView back, head;
    String processURL;
    Button button, shangchuan;
    int picIDs;
    private String photoName;
    ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;
    String img, userid;
    String userinfo[];
    int Sex;
    String path;
    int mylove;
    String fileName;
    SelectPicPopupWindow menuWindow;
    Dialog dialog;
    String res;
    private String actionUrl = Constant.url + "userinfo/upDateUserInfo";
    CompressPictures compressor;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private ArrayList<String> mSelectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        ShareSDK.initSDK(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Myinfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        userid = PreferencesUtils.getString(Myinfo.this, "userid");
        processURL = Constant.url + "userinfo/getUserInfoByUserId?userId=" + userid;
        dialog = GetMyData.createLoadingDialog(Myinfo.this, "请稍等");
        dialog.show();
        compressor = new CompressPictures();
        listView = (ListView) findViewById(R.id.infolist);
        shangchuan = (Button) findViewById(R.id.shangchuanhead);
        back = (ImageView) findViewById(R.id.info_back);
        head = (ImageView) findViewById(R.id.myhead);
        button = (Button) findViewById(R.id.quit);
        Intent intent = new Intent(this, HKService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(HKService.action);
        registerReceiver(broadcastReceiver, filter);
        info(1, processURL);
        shangchuan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog = GetMyData.createLoadingDialog(Myinfo.this, "请稍等");
                dialog.show();
                handler.sendEmptyMessage(2);
            }
        });
        head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(Myinfo.this, itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(
                        Myinfo.this.findViewById(R.id.myhead), Gravity.BOTTOM
                                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

            }
        });
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
             Intent intent1=new Intent();
                intent1.setAction("com.servicedemo4");
                intent1.putExtra("refrech", "11");
                sendBroadcast(intent1);

                Platform QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
                QQ.SSOSetting(true);
                if(QQ.isAuthValid() ){
                    QQ.removeAccount();
                    ShareSDK.removeCookieOnAuthorize(true);
                }

                Platform WX = ShareSDK.getPlatform(Wechat.NAME);
                WX.SSOSetting(true);
                if(WX.isAuthValid() ){
                    WX.removeAccount();
                    ShareSDK.removeCookieOnAuthorize(true);;
                }

                PreferencesUtils.putString(Myinfo.this, "selectadress", null);
                PreferencesUtils.putString(Myinfo.this, "adress", null);
                PreferencesUtils.putInt(Myinfo.this, "logn", 0);
                Intent intent2 = new Intent();
                intent2.setAction("com.servicedemo4");
                intent2.putExtra("refrech", "2");
                sendBroadcast(intent2);
                Intent intent = new Intent();
                intent.setClass(Myinfo.this, LoginActivity.class);
                Myinfo.this.startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("likename", userinfo[0]);
                intent.putExtra("ename", userinfo[1]);
                intent.putExtra("sex", Sex);
                intent.putExtra("time", userinfo[3]);
                intent.putExtra("love", mylove);
                intent.setClass(Myinfo.this, Updatemyinfo.class);
                Myinfo.this.startActivity(intent);
                finish();
            }
        });

    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent intentCamera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {// 如果存储卡可用
                        startActivityForResult(intentCamera, 4);
                    } else {
                        long time = Calendar.getInstance().getTimeInMillis();
                        photoName = time + ".jpg";
                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + "/DCIM/" + photoName)));
                        startActivityForResult(intentCamera, 4);
                    }

                    break;
                case R.id.btn_pick_photo:
                    int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                    Intent intent = new Intent(Myinfo.this, MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                    startActivityForResult(intent, 1);
                    break;
                default:
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    setlist();
                    options = ImageUtils.setnoOptions();
                    ImageLoader.displayImage(img, head, options,
                            animateFirstListener);
//				head.setBackgroundDrawable(GetMyData.getImageDownload(
//						Myinfo.this, img));
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    break;
                case 2:
                    info(4, processURL);
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(Myinfo.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    JSONObject jo1;
                    try {
                        System.out.println(res);
                        jo1 = new JSONObject(res);
                        System.out.println(res);
                        picIDs = jo1.getInt("status");
                        if (picIDs == 0) {
                            Toast.makeText(Myinfo.this, "修改成功！",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "10");
                            sendBroadcast(intent);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Myinfo.this, "修改失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:

                    break;
            }
        }

        ;
    };

    public void info(final int what, final String url) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                HttpRequest httpRequest = new HttpRequest();
                if (what == 4) {
                    res = uploadFile(actionUrl, path);
                    handler.sendEmptyMessage(what);
                } else {
                    try {
                        String str = httpRequest.doGet(url, Myinfo.this);
                        if (str.equals("网络超时")) {
                            handler.sendEmptyMessage(3);
                            Looper.loop();
                        } else {
                            setmap(str);
                            Message msg = new Message();
                            msg.what = what;
                            handler.sendEmptyMessage(what);
                            Looper.loop();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }.start();

    }

    public void setmap(String str) throws JSONException {
        String sex, love;
        final String[] text = new String[]{"昵称", "姓名", "性别", "生日", "情感状况"};
        JSONObject jsonObject = new JSONObject(str);
        String userInfo = jsonObject.getString("userInfo");
        com.alibaba.fastjson.JSONObject jsonObject2 = JSON.parseObject(userInfo);
//		JSONObject jsonObject2=new JSONObject(userInfo);
        img = jsonObject2.getString("picUrl");
        Sex = jsonObject2.getIntValue("sex");
        picIDs = jsonObject2.getIntValue("picId");
        if (jsonObject2.getString("sex").equals("0")) {
            sex = "女";
        } else {
            sex = "男";
        }

        if (jsonObject2.getString("marriage") != null) {
            mylove = jsonObject2.getIntValue("marriage");
        } else {
            mylove = 0;
        }

        if (jsonObject2.getIntValue("marriage") == 0) {
            love = "单身";
        } else if (jsonObject2.getIntValue("marriage") == 1) {
            love = "热恋";
        } else {
            love = "已婚";
        }
        userinfo = new String[]{jsonObject2.getString("nickName"),
                jsonObject2.getString("trueName") == null ? "" : jsonObject2.getString("trueName"), sex,
                (String) jsonObject2.getString("birthday").subSequence(0, 10),
                love};
        items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 5; i++) {

            Map<String, Object> item = new HashMap<String, Object>();

            item.put("myinfo", text[i]);

            item.put("info", userinfo[i]);

            items.add(item);

        }

    }

    public void setlist() {
        adapter = new SimpleAdapter(Myinfo.this, items, R.layout.info_text,
                new String[]{"myinfo", "info"}, new int[]{R.id.info_text1,
                R.id.info_text2});
        listView.setAdapter(adapter);
    }

    public String uploadFile(String Url, String filePath) {
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

            sb.append("Content-Disposition: form-data; name=\"" + "nickName" + "\""
                    + LINEND);
            sb.append(LINEND);
            sb.append(userinfo[0]);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "trueName"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(userinfo[1]);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "birthday"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(userinfo[3]);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\"" + "sex"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(Sex);
            sb.append(LINEND);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);


            sb.append("Content-Disposition: form-data; name=\"" + "marriage"
                    + "\"" + LINEND);
            sb.append(LINEND);
            sb.append(mylove);
            sb.append(LINEND);

            outStream.write(sb.toString().getBytes());

            File files = new File(filePath);
            if (files != null) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
                        + "123.jpg" + "\"" + LINEND);
                sb1.append("Content-Type: image/jpg; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(files);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());

            }
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
            Toast.makeText(Myinfo.this, "图片上传操作失败", Toast.LENGTH_SHORT).show();
        }
        return sb2.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
//                    String[] proj = {MediaStore.Images.Media.DATA};
//                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
//                    Uri originalUri = data.getData();
//                    Cursor cursor = managedQuery(originalUri, proj, null, null,
//                            null);
//                    // 按我个人理解 这个是获得用户选择的图片的索引值
//                    int column_index = cursor
//                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
//                    cursor.moveToFirst();
//                    // 最后根据索引值获取图片路径
//                    path = cursor.getString(column_index);
//                    Bitmap bit = BitmapFactory.decodeFile(path);
//                    float pc = (float) 100 / (float) bit.getWidth();
//                    Bitmap bitmap = compressor.resize_img(bit, pc);
//                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
////				head.setBackgroundDrawable(bitmapDrawable);
//                    head.setImageDrawable(bitmapDrawable);
//                    String filename = path.substring(path.lastIndexOf("/") + 1);
//                    File file = saveMyBitmap(filename, bitmap);
//                    path = file.toString();
                    try {
                        mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        for (int i = 0; i < mSelectPath.size(); i++) {
                            if (mSelectPath.get(i) != null) {
                                String fileNames = String.valueOf(System.currentTimeMillis());
                                Bitmap bms = BitmapFactory.decodeFile(mSelectPath.get(i));
                                File file = FileUtils.saveBitmap(bms, fileNames, mSelectPath.get(i));
                                head.setImageBitmap(bms);
//                        bms.recycle();
                                path=file.getPath();
//                                filemap.put(i + ".jpg", file);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(Myinfo.this, "放弃选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 如果是调用相机拍照时
                case 2:
                    break;
                // 取得裁剪后的图片
                case 3:
                    /**
                     * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                     * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                     * &nbsp;
                     */

                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                case 4:
                    Uri uri = data.getData();
                    if (uri != null) {
                        photo = BitmapFactory.decodeFile(uri.getPath());
                    }

                    if (photo == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            photo = (Bitmap) bundle.get("data");
                            savePicInLocal(photo);// 保存到本地
                            // 图片显示到指定地方
                            setPicToView(data);
                        } else {
                            Toast.makeText(Myinfo.this, "未拍摄照片", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(Myinfo.this, "放弃选择 ", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 保存拍摄的照片到手机的sd卡
    private void savePicInLocal(Bitmap bitmap) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/liangPic";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir(); // 创建文件夹
            }
            fileName = saveDir + "/" + System.currentTimeMillis() + ".JPEG";
            File file = new File(fileName);
            file.delete();
            if (!file.exists()) {
                file.createNewFile();// 创建文件
            }
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据 &nbsp;
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);// 得到要显示的图片资源
            float pc = (float) 100 / (float) photo.getWidth();
//			System.out.println( photo.getWidth());
            Bitmap bitmap2 = compressor.resize_img(photo, pc);
            String aa = "/data/data/com.hkkj.csrx.activity/cache/head/head.png";
            String filename = aa.substring(aa.lastIndexOf("/") + 1);
            File file2 = saveMyBitmap(filename, bitmap2);
            path = file2.toString();
//			System.out.println(path);

            head.setImageDrawable(drawable);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    @SuppressLint("SdCardPath")
    public File saveMyBitmap(String filename, Bitmap bit) {
        File dir = new File("/data/data/com.hkkj.csrx.activity/cache/head/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File("/data/data/com.hkkj.csrx.activity/cache/head/" + filename);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            f = null;
            e1.printStackTrace();
        }

        return f;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    ;
}
