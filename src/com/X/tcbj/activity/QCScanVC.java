package com.X.tcbj.activity;

import android.os.Bundle;
import android.view.View;

import com.X.server.BaseActivity;
import com.X.xnet.XActivityindicator;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Administrator on 2017/6/1 0001.
 */

public class QCScanVC extends BaseActivity {

    private boolean mFlash;
    private ZXingScannerView mScannerView;


    private ZXingScannerView.ResultHandler mResultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            Bundle bundle = new Bundle();
            bundle.putString("tj_code",result.getText());
            presentVC(RegistActivity.class,bundle);
            finish();
        }
    };

    @Override
    protected void setupUi() {
        setContentView(R.layout.qcscan);
        mScannerView = (ZXingScannerView) findViewById(R.id.scannerView);
        mScannerView.setResultHandler(mResultHandler);

        findViewById(R.id.light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFlash();
            }
        });

    }

    @Override
    protected void setupData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(mResultHandler);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void toggleFlash() {
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
    }
}
