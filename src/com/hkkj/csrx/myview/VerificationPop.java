package com.hkkj.csrx.myview;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.Constant;

/**
 * Created by admins on 2016/3/8.
 */
public class VerificationPop {
    View popView;
    PopupWindow popupWindow;
   EditText code;
    private MyPopwindowsListener mListViewListener;
    public void showpop(Context context){
        popView =((Activity)context).getLayoutInflater().inflate(
                R.layout.verpop, null);
//        WindowManager windowManager = activity.getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WebView webview=(WebView)popView.findViewById(R.id.webview);
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(Constant.url + "makeCertPic.jsp?id="+getMath());
        code =(EditText)popView.findViewById(R.id.code);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadMore();
                popupWindow.dismiss();
            }
        });
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                webview.loadUrl(Constant.url + "makeCertPic.jsp?id="+getMath());
                return false;
            }
        });
    }
    public void setMyPopwindowswListener(MyPopwindowsListener l) {
        mListViewListener = l;
    }
    public interface MyPopwindowsListener {
        public void onRefresh(String str);
    }
    private void startLoadMore() {

        mListViewListener.onRefresh(code.getText().toString());

    }
    private double getMath(){
        double math=Math.random();
        return  math;
    }
}
