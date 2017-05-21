package com.X.tcbj.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.tcbj.utils.AsyncImageLoader;
import com.X.tcbj.utils.AsyncImageLoadersdcard;
import com.X.tcbj.utils.GestureListener;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/24.
 */
public class Tupian_xiangqing  extends Activity implements OnClickListener
{
    String shuju;
    ImageView back, share, image;
    TextView view, next, last, info, title;
    HashMap<String, String> hashMap;
    HttpRequest httpRequest;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    AsyncImageLoader ImageLoader;
    private AsyncImageLoadersdcard asyncImageLoadersdcard;
    int i = 0;
    LinearLayout layout, linearLayout;
    DisplayMetrics dm;
    Handler handler = new Handler() {
        @SuppressLint("NewApi")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    next.setText("/" + array.size() + "");
                    int a = i + 1;
                    last.setText(a + "");
                    final String url = array.get(i).get("ComPicID");
                    // 得到图片的宽高

                    ImageView imageView = image;
                    asyncImageLoadersdcard = new AsyncImageLoadersdcard();
                    Bitmap bitmap1 = asyncImageLoadersdcard.loadBitmap(imageView,
                            url, new AsyncImageLoadersdcard.ImageCallBack2() {

                                @Override
                                public void imageLoad(ImageView imageView,
                                                      Bitmap bitmap) {
                                    if (imageView.getTag() != null
                                            && imageView.getTag().equals(url)) {
//									int w = bitmap.getWidth();
//									int h = bitmap.getHeight();
                                        int weight = bitmap.getWidth();
                                        int height = bitmap.getHeight();
                                        float scale = ((float) (height)) / ((float) (weight));

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.FILL_PARENT,
                                                (int) (dm.widthPixels * scale));
                                        image.setLayoutParams(params);
                                        BitmapDrawable drawable = new BitmapDrawable(
                                                bitmap);
                                        imageView.setBackgroundDrawable(drawable);
                                    }
                                }
                            });
                    if (bitmap1 == null) {
                        ImageLoader = new AsyncImageLoader();
                        Bitmap bitmap = ImageLoader.loadBitmap(imageView, url,
                                new AsyncImageLoader.ImageCallBack() {

                                    @Override
                                    public void imageLoad(ImageView imageView,
                                                          Bitmap bitmap) {
                                        int weight = bitmap.getWidth();
                                        int height = bitmap.getHeight();
                                        float scale = ((float) (height)) / ((float) (weight));

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.FILL_PARENT,
                                                (int) (dm.widthPixels * scale));
                                        image.setLayoutParams(params);
                                        BitmapDrawable bd = new BitmapDrawable(
                                                bitmap);
                                        imageView.setBackgroundDrawable(bd);
                                    }

                                });
                        if (bitmap == null) {
                            imageView.setBackgroundResource(R.drawable.head);
                        } else {
                            int weight = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            float scale = ((float) (height)) / ((float) (weight));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.FILL_PARENT,
                                    (int) (dm.widthPixels * scale));
                            image.setLayoutParams(params);
                            BitmapDrawable bd = new BitmapDrawable(bitmap);
                            imageView.setBackgroundDrawable(bd);
                        }
                    } else {
                        int weight = bitmap1.getWidth();
                        int height = bitmap1.getHeight();
                        float scale = ((float) (height)) / ((float) (weight));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.FILL_PARENT,
                                (int) (dm.widthPixels * scale));
                        image.setLayoutParams(params);
                        BitmapDrawable bd = new BitmapDrawable(bitmap1);
                        imageView.setBackgroundDrawable(bd);
                    }

                    break;
                case 2:
                    Toast.makeText(Tupian_xiangqing.this, "网络异常", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    Toast.makeText(Tupian_xiangqing.this, "您请求的数据已不存在",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tupian_xiangqing);
        init();
        try {
            jiexi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jiexi()  throws JSONException
    {
        if(shuju==null)
        {
            handler.sendEmptyMessage(2);
        }
        JSONArray jsonArray = new JSONArray(shuju);
           for (int i=0;i<jsonArray.length();i++)
           {
               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
               hashMap=new HashMap<String, String>();
               hashMap.put("ComPicID",jsonObject.getString("ComPicID"));
               array.add(hashMap);
           }
        handler.sendEmptyMessage(1);
    }

    private void init()
    {
        shuju=getIntent().getStringExtra("tupian");
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        linearLayout = (LinearLayout) findViewById(R.id.mygodview);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        float a = (float) (3.0 / 5.0);
        params.setMargins(0, (int) (dm.heightPixels * (a)), 0, 0);
        linearLayout.setLayoutParams(params);
        back = (ImageView) findViewById(R.id.photo_back);
        back.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.photo_img);
        view = (TextView) findViewById(R.id.photo_views);
        next = (TextView) findViewById(R.id.photo_next);
        next.setOnClickListener(this);
        last = (TextView) findViewById(R.id.photo_last);
        last.setOnClickListener(this);
        info = (TextView) findViewById(R.id.photo_info);
        title = (TextView) findViewById(R.id.photo_title);
        layout = (LinearLayout) findViewById(R.id.photo_lay);
        image.setLongClickable(true);
        image.setOnTouchListener(new MyGestureListener(this));

    }
    private class MyGestureListener extends GestureListener {
        public MyGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            i = i + 1;
            if (i >= array.size()) {
                i = array.size() - 1;
                Toast.makeText(Tupian_xiangqing.this, "已经是最后一张了",
                        Toast.LENGTH_SHORT).show();

            } else {
                handler.sendEmptyMessage(1);
            }
            return super.left();
        }

        @Override
        public boolean right() {
            i = i - 1;
            if (i < 0) {
                i = 0;
                Toast.makeText(Tupian_xiangqing.this, "已经是第一张了",
                        Toast.LENGTH_SHORT).show();

            } else {
                handler.sendEmptyMessage(1);
            }
            return super.right();
        }
    }

    @Override
    public void onClick(View v)
    {


    }
}
