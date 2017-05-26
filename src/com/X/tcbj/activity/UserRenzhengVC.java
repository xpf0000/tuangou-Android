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
import android.widget.TextView;

import com.X.model.RenzhengModel;
import com.X.model.UserModel;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.utils.DensityUtil;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.robin.lazy.cache.CacheLoaderManager;

import org.greenrobot.eventbus.EventBus;

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
    ImageView idsIV,idsbackIV;
    AlertView alertView;
    Bitmap idsBitmap,idsbackBitmap;
    RenzhengModel renzhenginfo;
    TextView statusTV;
    int uid = 0;
    int nowid = 0;

    private int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_renzheng);
        nameET = (EditText) findViewById(R.id.real_renzheng_name);
        idsET = (EditText) findViewById(R.id.real_renzheng_ids);
        idsIV = (ImageView) findViewById(R.id.real_renzheng_pic);
        idsbackIV = (ImageView) findViewById(R.id.real_renzheng_pic1);

        statusTV = (TextView) findViewById(R.id.real_renzheng_status);

        ViewGroup.LayoutParams layoutParams = idsIV.getLayoutParams();

        int w = SW - DensityUtil.dip2px(this,18*3);
        int h = (int)(w*7.0/16.0);

        layoutParams.height = h;
        idsIV.setLayoutParams(layoutParams);
        idsbackIV.setLayoutParams(layoutParams);

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

            if(renzhenginfo.getStatus().equals("0"))
            {
                statusTV.setText("审核中");
                statusTV.setVisibility(View.VISIBLE);
            }
            else if(renzhenginfo.getStatus().equals("2"))
            {
                statusTV.setText("审核未通过，请修改后重新提交审核");
                statusTV.setVisibility(View.VISIBLE);
            }

            ImageLoader.getInstance().displayImage("http://tg01.sssvip.net/"+renzhenginfo.getId_url(),idsIV);
            ImageLoader.getInstance().displayImage("http://tg01.sssvip.net/"+renzhenginfo.getId_url_back(),idsbackIV);
        }
    }

    public void back(View v)
    {
        finish();
        overridePendingTransition(R.anim.pop_up_out,R.anim.pop_up_in);
    }

    public void chooseImg(View v)
    {
        nowid = v.getId();
        alertView.show();
    }


    public void do_renzheng(View v)
    {
        if(renzhenginfo == null && (idsBitmap == null || idsbackBitmap == null))
        {
            XActivityindicator.showToast("请上传身份证照片");
            return;
        }

        String name = nameET.getText().toString().trim();
        String ids = idsET.getText().toString().trim();

        if(name.length() == 0 || ids.length() == 0)
        {
            XActivityindicator.showToast("请完善身份信息");
            return;
        }

        XActivityindicator.create(this).show();

        Map<String , RequestBody> params = new HashMap<>();
        params.put("uid", XAPPUtil.createBody(uid+""));
        params.put("name", XAPPUtil.createBody(name));
        params.put("ids", XAPPUtil.createBody(ids));

        if(renzhenginfo == null)
        {
            params.put("file\"; filename=\"xtest.jpg",XAPPUtil.createBody(idsBitmap));
            params.put("file1\"; filename=\"xtest1.jpg",XAPPUtil.createBody(idsbackBitmap));
        }

        XNetUtil.Handle(APPService.user_do_renzheng(params), "提交成功,请等待审核", null, new XNetUtil.OnHttpResult<Boolean>() {
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
                            finish();
                            overridePendingTransition(R.anim.pop_up_out,R.anim.pop_up_in);
                        }
                    });
                }

            }
        });

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        String path = result.getImages().get(0).getOriginalPath();

        if(nowid == R.id.real_renzheng_pic)
        {
            idsBitmap = BitmapFactory.decodeFile(path);
            idsIV.setImageBitmap(idsBitmap);
        }
        else
        {
            idsbackBitmap = BitmapFactory.decodeFile(path);
            idsbackIV.setImageBitmap(idsbackBitmap);
        }



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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pop_up_out,R.anim.pop_up_in);
    }
}

