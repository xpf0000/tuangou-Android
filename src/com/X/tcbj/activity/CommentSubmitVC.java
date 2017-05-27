package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.utils.Bimp;
import com.X.tcbj.utils.ImageItem;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class CommentSubmitVC extends TakePhotoActivity {

    private MyGridView noScrollgridview;
    private GridAdapter adapter;
    public static Bitmap bimap;
    EditText content;
    AlertView alertView;
    private int position = -1;
    RatingBar starBar;

    String did = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        did = getIntent().getStringExtra("did");
        setupUi();

    }

    private void setupUi() {
        setContentView(R.layout.comment_submit);

        starBar = (RatingBar) findViewById(R.id.comment_submit_star);

        alertView = new AlertView("选择图片", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener(){
            public void onItemClick(Object o,int p){

                position = p;

            }
        });

        File f = new File(getExternalFilesDir(""), "temp.jpg");
        final Uri uri = Uri.fromFile(f);

        alertView.setOnDismissListener(new com.bigkoo.alertview.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if(position == 0)
                {
                    getTakePhoto().onPickFromCapture(uri);
                }
                else if(position == 1)
                {
                    getTakePhoto().onPickMultiple(9-Bimp.tempSelectBitmap.size());
                }
            }
        });

        content = (EditText) findViewById(R.id.comment_submit_content);

        noScrollgridview = (MyGridView) findViewById(R.id.comment_submit_pics);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");

                    alertView.show();

                } else {
                    Intent intent = new Intent(CommentSubmitVC.this,
                            PhotoPreView.class);
                    intent.putExtra("index", arg2);
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        if (Bimp.tempSelectBitmap.size() < 9 ) {
            try {
                for(TImage img : result.getImages())
                {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;

                    String path = img.getOriginalPath();
                    Bitmap bitmap= BitmapFactory.decodeFile(path, opts);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(path);
                    takePhoto.setBitmap(bitmap);
                    Bimp.tempSelectBitmap.add(takePhoto);

                    bitmap = null;

                }

            } catch (Exception e) {
                XActivityindicator.showToast("手机可运行内存不足，照片保存失败，请清理后操作");
            }

        }


    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        XActivityindicator.showToast(msg);
    }


    public void do_submit(View v)
    {
        String info = content.getText().toString().trim();
        String num = starBar.getNumStars()+"";
        String uid = DataCache.getInstance().user.getId()+"";

        if(starBar.getNumStars() == 0)
        {
            XActivityindicator.showToast("请先给商家打分");
            return;
        }

        XActivityindicator.create(this).show();

        Map<String , RequestBody> params = new HashMap<>();
        params.put("uid", XAPPUtil.createBody(uid+""));
        params.put("data_id", XAPPUtil.createBody(did));
        params.put("point", XAPPUtil.createBody(num));
        params.put("content", XAPPUtil.createBody(info));

        int k = 0;
        for(ImageItem item : Bimp.tempSelectBitmap)
        {
            params.put("file[]\"; filename=\"xtest"+k+".jpg",XAPPUtil.createBody(item.getBitmap()));
            k++;
        }

        XNetUtil.Handle(APPService.user_add_dp(params), "评价提交成功", null, new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(SVProgressHUD hud) {
                            EventBus.getDefault().post(new MyEventBus("AddCommentSuccess"));
                            finish();
                        }
                    });
                }

            }
        });

    }





    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.clear();
    }

    public void back(View v)
    {
        finish();
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
                        .getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

}
