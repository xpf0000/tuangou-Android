package com.X.tcbj.myview;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.X.tcbj.activity.R;

/**
 * Created by admins on 2016/8/2.
 */
public class UpdatePop {
    View popView;
    PopupWindow popupWindow;
    private MyPopwindowsListener mListViewListener;
    public void showpop(Context activity, String txt, final Handler handler){
        popView = LayoutInflater.from(activity).inflate(
                R.layout.updatepop, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView textView=(TextView)popView.findViewById(R.id.popedttxt);
        textView.setText(Html.fromHtml(txt.toString()));
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                handler.sendEmptyMessage(1);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadMore();
                popupWindow.dismiss();
            }
        });
    }
    public void setMyPopwindowswListener(MyPopwindowsListener l) {
        mListViewListener = l;
    }
    public interface MyPopwindowsListener {
        public void onRefresh();
    }
    private void startLoadMore() {

        mListViewListener.onRefresh();

    }
}
