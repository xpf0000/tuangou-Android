package com.hkkj.csrx.myview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.hkkj.csrx.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/9/5.
 */
public class MyExPop{
    View popView;
    public PopupWindow popupWindow;
    private MyPopwindowsListener mListViewListener;
    public void showclasspop(final ArrayList<HashMap<String, String>> classList, Context context) {
        popView = LayoutInflater.from(context).inflate(R.layout.setprogramme, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.update();
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        ListView bigclass = (ListView) popView.findViewById(R.id.programlist);
        Button canle=(Button)popView.findViewById(R.id.canle);
        SimpleAdapter romadapter = new SimpleAdapter(context, classList,
                R.layout.ex_item, new String[]{"OutTypesName","exprice"},
                new int[]{R.id.name,R.id.money});
        bigclass.setAdapter(romadapter);
        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startUrlLoadMore(position);
                popupWindow.dismiss();
            }
        });
        canle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    public void setMyPopwindowswListener(MyPopwindowsListener l) {
        mListViewListener = l;
    }
    public interface MyPopwindowsListener {
        public void onRefresh(int positions);
    }
    private void startUrlLoadMore(int getPositions) {

        mListViewListener.onRefresh(getPositions);

    }
}
