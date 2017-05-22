package com.X.tcbj.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.X.model.RenzhengModel;
import com.X.model.UserModel;
import com.X.server.DataCache;
import com.X.tcbj.utils.DensityUtil;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.robin.lazy.cache.CacheLoaderManager;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;

import static com.X.server.location.APPService;
import static com.X.server.location.SW;

/**
 * Created by X on 2017/5/21.
 */

public class UserRenzhengVC extends TakePhotoActivity {
    EditText nameET,idsET;
    ImageView idsIV;
    AlertView alertView;

    Bitmap idsBitmap;

    RenzhengModel renzhenginfo;

    int uid = 0;

    private int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_renzheng);
        nameET = (EditText) findViewById(R.id.real_renzheng_name);
        idsET = (EditText) findViewById(R.id.real_renzheng_ids);
        idsIV = (ImageView) findViewById(R.id.real_renzheng_pic);

        ViewGroup.LayoutParams layoutParams = idsIV.getLayoutParams();

        int w = SW - DensityUtil.dip2px(this,36);
        int h = (int)(w*7.0/16.0);

        layoutParams.height = h;
        idsIV.setLayoutParams(layoutParams);

        uid = DataCache.getInstance().user.getId();

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

        getinfo();

    }

    public void getinfo()
    {
        String uid = DataCache.getInstance().user.getId()+"";
        XNetUtil.Handle(APPService.user_getRenzhenginfo(uid), new XNetUtil.OnHttpResult<RenzhengModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(RenzhengModel model) {

                renzhenginfo = model;
                initUI();

            }
        });
    }

    public void initUI()
    {
        if(renzhenginfo != null)
        {
            nameET.setText(renzhenginfo.getReal_name());
            idsET.setText(renzhenginfo.getId_number());
            ImageLoader.getInstance().displayImage(renzhenginfo.getId_url(),idsIV);
        }
    }

    public void back(View v)
    {
        finish();
    }

    public void chooseImg(View v)
    {
        alertView.show();
    }


    public void do_renzheng(View v)
    {
        if(renzhenginfo == null && idsBitmap == null)
        {
            XActivityindicator.showToast("请上传身份证照片");
            return;
        }

        String name = nameET.getText().toString().trim();
        String ids = idsET.getText().toString().trim();

        Map<String , RequestBody> params = new HashMap<>();
        params.put("uid", XAPPUtil.createBody(uid+""));
        params.put("name", XAPPUtil.createBody(name));
        params.put("ids", XAPPUtil.createBody(ids));

        if(renzhenginfo == null)
        {
            params.put("file\"; filename=\"xtest.jpg",XAPPUtil.createBody(idsBitmap));
        }

        XNetUtil.Handle(APPService.user_do_renzheng(params), "提交成功,请等待审核", null, new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    finish();
                }

            }
        });

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        String path = result.getImages().get(0).getOriginalPath();
        idsBitmap = BitmapFactory.decodeFile(path);

        idsIV.setImageBitmap(idsBitmap);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);

        XActivityindicator.showToast(msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}

