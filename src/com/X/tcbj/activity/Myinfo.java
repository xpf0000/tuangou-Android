package com.X.tcbj.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

import static com.X.server.location.APPService;

/**
 * 信息
 *
 * @author zpp
 * @version1.0
 */
public class Myinfo extends BaseActivity {

    TextView telTV,nameTV,idsTV;
    ImageView headIV;
    AlertView alertView;
    Bitmap headBitmap;

    private int position = -1;

    @Override
    protected void setupUi() {
        setContentView(R.layout.myinfo);

        telTV = (TextView) findViewById(R.id.myinfo_tel);
        nameTV = (TextView) findViewById(R.id.myinfo_name);
        idsTV = (TextView) findViewById(R.id.myinfo_ids);

        headIV = (ImageView) findViewById(R.id.myinfo_head);


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
                    getTakePhoto().onPickFromGallery();

                }
            }
        });

        initUI();
    }

    @Override
    protected void setupData() {

    }


    public void initUI()
    {
        telTV.setText(DataCache.getInstance().user.getUser_name());
        nameTV.setText(DataCache.getInstance().user.getReal_name());
        idsTV.setText(DataCache.getInstance().user.getId_number());

        String url = DataCache.getInstance().user.getAvatar();
        if(url.indexOf("http://") < 0 && url.indexOf("https://") < 0)
        {
            url = "http://tg01.sssvip.net/"+DataCache.getInstance().user.getAvatar();
        }

        ImageLoader.getInstance().displayImage(url,headIV);
    }

    public void do_save(View v)
    {
        if(headBitmap == null)
        {
            XActivityindicator.showToast("请先选择头像");
            return;
        }

        XActivityindicator.create().show();

        Map<String , RequestBody> params = new HashMap<>();
        params.put("uid", XAPPUtil.createBody(DataCache.getInstance().user.getId()+""));

        params.put("file\"; filename=\"xtest.jpg",XAPPUtil.createBody(headBitmap));

        XNetUtil.Handle(APPService.app_upload_avatar(params), "头像上传成功！", null, new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    DataCache.getInstance().getUinfo();
                }
            }
        });

    }

    public void chooseImg(View v)
    {
        alertView.show();
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        String path = result.getImages().get(0).getOriginalPath();
        headBitmap = BitmapFactory.decodeFile(path);

        headIV.setImageBitmap(headBitmap);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);

        XActivityindicator.showToast(msg);
    }



    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }


    protected void onDestroy() {
        super.onDestroy();

    }

}
