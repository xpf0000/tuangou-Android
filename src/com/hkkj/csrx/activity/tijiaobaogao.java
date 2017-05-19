package com.hkkj.csrx.activity;

/**
 * Created by Administrator on 2015/12/23.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.myview.MyGridView;
import com.hkkj.csrx.myview.MyPopwindows;
import com.hkkj.csrx.utils.CompressPictures;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.ImageUtils;
import com.hkkj.csrx.utils.SelectPicPopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

;

public class tijiaobaogao extends Activity
{
    ArrayList<HashMap<String,Bitmap>> abscure_list=new ArrayList<HashMap<String,Bitmap>>();
    MyGridView gridView;
    ArrayList<HashMap<String,String>> wenjian=new ArrayList<HashMap<String,String>>();
    HashMap<String,String>hashMap1;
    HashMap<String,Bitmap>hashMap;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    Gridviewadapter gridviewadapter;
//    private GetPhotos getPhotos;
//    File tempFile,file;
    private TextView tijiao;
    private EditText biaoti,neiron;

//   private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
//    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//    private static final int PHOTO_REQUEST_CUT = 3;// 结果
   private String url;
    private String title,content;
    private String str,str1;
    private String freeId;
    private  String userId;
//    UpdatePhotos updatePhotos;
    private ImageView back;
    SelectPicPopupWindow menuWindow;
    private String photoName;
    String path;
    Bitmap photo;
    String fileName;
    CompressPictures compressor;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Toast.makeText(tijiaobaogao.this,"网络超时",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(tijiaobaogao.this, "网络超时上传失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject1=new JSONObject(str1);
        if(jsonObject1.getInt("status")==1)
        {
            Toast.makeText(tijiaobaogao.this,jsonObject1.getString("statusMsg"),Toast.LENGTH_SHORT).show();

        }
        if(jsonObject1.getInt("status")==0)

        {
            Toast.makeText(tijiaobaogao.this,"提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tijiaobaogao);
      init();
    }

    private void init()
    {
        compressor = new CompressPictures();
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userId= PreferencesUtils.getString(tijiaobaogao.this, "userid");
        freeId=getIntent().getStringExtra("id");
        biaoti=(EditText)findViewById(R.id.biaoti);
        neiron=(EditText)findViewById(R.id.neiron);
        url= Constant.url +"/free/addFreeReport";
//        updatePhotos = new UpdatePhotos();
//        getPhotos=new GetPhotos();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() +"/happydrive/Cache";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
//        tempFile = new File(file,getPhotos.getPhotoFileName());

        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        hashMap=new HashMap<String, Bitmap>();
        Bitmap bitmap=null;
        hashMap.put("tu", bitmap);
        abscure_list.add(hashMap);
        ImageLoader.init(ImageLoaderConfiguration.createDefault(tijiaobaogao.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        gridView=(MyGridView)findViewById(R.id.comimglist1);
        gridviewadapter=new Gridviewadapter();
        tijiao=(TextView)findViewById(R.id.tijiao);
        tijiao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(biaoti.getText().toString().equals(""))
                {
                    Toast.makeText(tijiaobaogao.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                if(biaoti.getText().toString().length()>50)
                    {
                        Toast.makeText(tijiaobaogao.this,"标题不能超过50个字",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    title=biaoti.getText().toString().trim();
                }
                if(neiron.getText().toString().equals(""))
                {
                    Toast.makeText(tijiaobaogao.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    if(neiron.getText().toString().trim().length()<15)
                    {
                        Toast.makeText(tijiaobaogao.this,"内容不能低于15个字",Toast.LENGTH_SHORT).show();
                        return;
                    }
               content=neiron.getText().toString().trim();
                }
                tijiao();

            }
        });
        gridView.setAdapter(gridviewadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if(abscure_list.get(position).get("tu")==null){
                    menuWindow = new SelectPicPopupWindow(tijiaobaogao.this, itemsOnClick);
                    // 显示窗口
                    menuWindow.showAtLocation(
                            tijiaobaogao.this.findViewById(R.id.comimglist1), Gravity.BOTTOM
                                    | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                }
                else
                {
                    MyPopwindows my=new MyPopwindows();
                    my.showpop(tijiaobaogao.this, "确定要删除吗?");
                    my.setMyPopwindowswListener(new MyPopwindows.MyPopwindowsListener() {
                        @Override
                        public void onRefresh() {
                            abscure_list.remove(position);
                            wenjian.remove(position);
                            for (int i=0;i<abscure_list.size();i++)
                            {
                                if(abscure_list.get(i).get("tu")==null)
                                {
                                    abscure_list.remove(i);
                                }
                            }
                            if(abscure_list.size()<9){
                                hashMap=new HashMap<String, Bitmap>();
                                Bitmap bitmap1=null;
                                hashMap.put("tu", bitmap1);
                                abscure_list.add(hashMap);}

                            gridviewadapter.notifyDataSetChanged();

                        }
                    });


                }


            }
        });

    }
    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
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
                    Intent intent = new Intent(Intent.ACTION_PICK, null);

                    /**
                     * 下面这句话，与其它方式写是一样的效果，如果： intent.setData(MediaStore.Images
                     * .Media.EXTERNAL_CONTENT_URI);
                     * intent.setType(""image/*");设置数据类型 如果朋友们要限制上传到服务器的图片类型时可以直接写如
                     * ："image/jpeg 、 image/png等的类型"
                     */
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                    startActivityForResult(intent, 1);
                    break;
                default:
                    break;
            }
        }
    };

    private void tijiao()
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                url=Constant.url +"/free/addFreeReport";
                UpdatePhotos updatePhotos=new UpdatePhotos();
                int areaid=PreferencesUtils.getInt(tijiaobaogao.this,"cityID");
             str=updatePhotos.uploadFile(url,title,content, userId,areaid,freeId);
                try {
                    str1 = new String(str.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(str1.equals(""))
                {
                   handler.sendEmptyMessage(2);
                }else
                {

           handler.sendEmptyMessage(1);
                }
            }
        }.start();

    }

    public class Gridviewadapter extends BaseAdapter
    {

        @Override
        public int getCount() {

            return abscure_list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final getItemView getItemView = new getItemView();
            convertView = LayoutInflater.from(tijiaobaogao.this).inflate(R.layout.tijiao_baogao_item, null);
            getItemView.comment_img = (ImageView)convertView.findViewById(R.id.tu);
            if(abscure_list.get(position).get("tu")==null)
            {
             getItemView.comment_img.setBackgroundResource(R.drawable.upload);
            }
            else
            {
                getItemView.comment_img.setImageBitmap(abscure_list.get(position).get("tu"));

            }
            return convertView;

        }
    }
    private class getItemView {
        ImageView comment_img;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

//        switch (requestCode) {
//            case PHOTO_REQUEST_TAKEPHOTO:
//                getPhotos. startPhotoZoom(Uri.fromFile(tempFile), 150,tijiaobaogao.this);
//                file = tempFile;
//                break;
//
//            case PHOTO_REQUEST_GALLERY:
//                if (data != null)
//                //       getPhotos.startPhotoZoom(data.getData(), 150, LeHui_FabuXinxiActivicty.this);
//                {
//                    File files = getPhotos.Getalbum(data,tijiaobaogao.this);
//                    file = files;
//                    Uri uri = Uri.fromFile(files);
//                    hashMap1=new HashMap<String, String>();
//                    hashMap1.put("wenjian", file.toString());
//                    wenjian.add(hashMap1);
//                    ContentResolver cr = this.getContentResolver();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
//                        hashMap=new HashMap<String, Bitmap>();
//                        hashMap.put("tu", bitmap);
//                        abscure_list.add(hashMap);
//                       for (int i=0;i<abscure_list.size();i++)
//                       {
//                           if(abscure_list.get(i).get("tu")==null)
//                           {
//                               abscure_list.remove(i);
//                           }
//                       }
//                        if(abscure_list.size()<9){
//                            hashMap=new HashMap<String, Bitmap>();
//                            Bitmap bitmap1=null;
//                            hashMap.put("tu", bitmap1);
//                            abscure_list.add(hashMap);}
//
//                      gridviewadapter.notifyDataSetChanged();
////                        getUserinfo();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    ;
//                }
//                break;

//            case PHOTO_REQUEST_CUT:
//                System.out.println(data);
//                if (data != null)
//                    System.out.println(data);
//                System.out.println(data+"wowodfn");
//                try{
//                   Drawable drawable=getPhotos.setPicToView(data);
//                    BitmapDrawable bd =(BitmapDrawable)drawable;
//                    Bitmap bm = bd.getBitmap();
//                    hashMap=new HashMap<String, Bitmap>();
//                    hashMap.put("tu",bm);
//                    abscure_list.add(hashMap);
//                    hashMap1.put("wenjian", file.toString());
//                    wenjian.add(hashMap1);
//                    for (int i=0;i<abscure_list.size();i++)
//                    {
//                        if(abscure_list.get(i).get("tu")==null)
//                        {
//                            abscure_list.remove(i);
//                        }
//                    }
//                    if(abscure_list.size()<9){
//                    hashMap=new HashMap<String, Bitmap>();
//                    Bitmap bitmap1=null;
//                    hashMap.put("tu", bitmap1);
//                    abscure_list.add(hashMap);}
//
//                    gridviewadapter.notifyDataSetChanged();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Uri uri = Uri.fromFile(tempFile);
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
////                    getUserinfo();
//                    hashMap=new HashMap<String, Bitmap>();
//                    hashMap.put("tu", bitmap);
//                    abscure_list.add(hashMap);
//                    hashMap1.put("wenjian", file.toString());
//                    wenjian.add(hashMap1);
//                    for (int i=0;i<abscure_list.size();i++)
//                    {
//                        if(abscure_list.get(i).get("tu")==null)
//                        {
//                            abscure_list.remove(i);
//                        }
//                    }
//                    if(abscure_list.size()<9){
//                        hashMap=new HashMap<String, Bitmap>();
//                        Bitmap bitmap1=null;
//                        hashMap.put("tu", bitmap1);
//                        abscure_list.add(hashMap);}
//                    gridviewadapter.notifyDataSetChanged();
//                }
//
//                break;
        try {

            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    String[] proj = { MediaStore.Images.Media.DATA };
                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
                    Uri originalUri = data.getData();
                    Cursor cursor = managedQuery(originalUri, proj, null, null,
                            null);
                    // 按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    path = cursor.getString(column_index);
                    Bitmap bit = BitmapFactory.decodeFile(path);
                    float pc = (float) 100 / (float) bit.getWidth();
                    Bitmap bitmap = compressor.resize_img(bit, pc);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    hashMap=new HashMap<String, Bitmap>();
                        hashMap.put("tu", bitmap);
                        abscure_list.add(hashMap);
                       for (int i=0;i<abscure_list.size();i++)
                       {
                           if(abscure_list.get(i).get("tu")==null)
                           {
                               abscure_list.remove(i);
                           }
                       }
                        if(abscure_list.size()<9){
                            hashMap=new HashMap<String, Bitmap>();
                            Bitmap bitmap1=null;
                            hashMap.put("tu", bitmap1);
                            abscure_list.add(hashMap);}

                      gridviewadapter.notifyDataSetChanged();

                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    File file = saveMyBitmap(filename, bitmap);
                    path = file.toString();
                    hashMap1=new HashMap<String, String>();
                   hashMap1.put("wenjian",path);
                    wenjian.add(hashMap1);
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
//                        setPicToView(data);
                    }
                    break;
                case 4:
                    Uri uri = data.getData();
                    if (uri != null) {
                        photo = BitmapFactory.decodeFile(uri.getPath());
                    }
                    if(photo!=null)
                    {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            photo = (Bitmap) bundle.get("data");
                            hashMap=new HashMap<String, Bitmap>();
                            hashMap.put("tu",photo);
                            abscure_list.add(hashMap);
                            for (int i=0;i<abscure_list.size();i++)
                            {
                                if(abscure_list.get(i).get("tu")==null)
                                {
                                    abscure_list.remove(i);
                                }
                            }
                            if(abscure_list.size()<9){
                                hashMap=new HashMap<String, Bitmap>();
                                Bitmap bitmap1=null;
                                hashMap.put("tu", bitmap1);
                                abscure_list.add(hashMap);}

                            gridviewadapter.notifyDataSetChanged();

                            savePicInLocal(photo);// 保存到本地
                            // 图片显示到指定地方
                            setPicToView(data);
                        } else {
                            Toast.makeText(tijiaobaogao.this, "未拍摄照片", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    if (photo == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            photo = (Bitmap) bundle.get("data");
                            hashMap=new HashMap<String, Bitmap>();
                            hashMap.put("tu",photo);
                            abscure_list.add(hashMap);
                            for (int i=0;i<abscure_list.size();i++)
                            {
                                if(abscure_list.get(i).get("tu")==null)
                                {
                                    abscure_list.remove(i);
                                }
                            }
                            if(abscure_list.size()<9){
                                hashMap=new HashMap<String, Bitmap>();
                                Bitmap bitmap1=null;
                                hashMap.put("tu", bitmap1);
                                abscure_list.add(hashMap);}

                            gridviewadapter.notifyDataSetChanged();

                            savePicInLocal(photo);// 保存到本地
                            // 图片显示到指定地方
                           setPicToView(data);
                        } else {
                            Toast.makeText(tijiaobaogao.this, "未拍摄照片", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(tijiaobaogao.this, "放弃选择 ", Toast.LENGTH_SHORT).show();
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
            String aa="/data/data/com.hkkj.csrx.activity/cache/head/head.png";
            String filename = aa.substring(aa.lastIndexOf("/") + 1);
            File file2 = saveMyBitmap(filename, bitmap2);
            path = file2.toString();
//			System.out.println(path);


            hashMap1=new HashMap<String, String>();
            hashMap1.put("wenjian",path);
            wenjian.add(hashMap1);
        }
    }

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


    public class UpdatePhotos {
        public String uploadFile(String Url, String title, String content, String userId, int areaId, String freeId) {
            StringBuilder sb2 = new StringBuilder();
            String BOUNDARY = "ARCFormBoundaryp859n1863ri19k9";
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
                sb.append("Content-Disposition: form-data; name=\"" + "userId"
                        + "\"" + LINEND);
                sb.append(LINEND);
                sb.append(userId);
                sb.append(LINEND);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);


                sb.append("Content-Disposition: form-data; name=\"" + "areaId"
                        + "\"" + LINEND);
                sb.append(LINEND);
                sb.append(areaId);
                sb.append(LINEND);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);


                sb.append("Content-Disposition: form-data; name=\"" + "freeId"
                        + "\"" + LINEND);
                sb.append(LINEND);
                sb.append(freeId);
                sb.append(LINEND);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);

                sb.append("Content-Disposition: form-data; name=\"" + "title" + "\""
                        + LINEND);
                sb.append(LINEND);
                sb.append(title);
                sb.append(LINEND);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);

                sb.append("Content-Disposition: form-data; name=\"" + "content"
                        + "\"" + LINEND);
                sb.append(LINEND);
                sb.append(content);
                sb.append(LINEND);


                outStream.write(sb.toString().getBytes());

                for (int i = 0; i < wenjian.size(); i++) {
                    String filePath = wenjian.get(i).get("wenjian");
//                    String fileName = String.valueOf(System.currentTimeMillis());
                    File file = new File(filePath);
                    filePath = file.toString();
                    String name = "imgFile" + i;
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
                            + "123.jpg" + "\"" + LINEND);
                    sb1.append("Content-Type: image/jpg; charset="
                            + CHARSET + LINEND);
                    sb1.append(LINEND);
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
                if (wenjian.size() == 0) {

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
//                handler.sendEmptyMessage(3);
            }
            return sb2.toString();
        }
    }
}
