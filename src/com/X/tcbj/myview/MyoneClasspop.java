package com.X.tcbj.myview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.X.tcbj.activity.R;
//import com.X.tcbj.adapter.ClassAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/7/7.
 */
public class MyoneClasspop {
    View popView;
    public PopupWindow popupWindow;
    //ClassAdapter adapter;
    int positions, getPositions;
    private MyPopwindowsListener mListViewListener;
    public void showclasspop(final ArrayList<HashMap<String, String>> classList, Context context, View view) {
        popView = LayoutInflater.from(context).inflate(R.layout.classonepop, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.update();
        popupWindow.showAsDropDown(view);
        ListView bigclass = (ListView) popView.findViewById(R.id.bigclass);
        //adapter = new ClassAdapter(classList, context);
        //bigclass.setAdapter(adapter);
//        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                classList.get(positions).put("check", "false");
//                positions = position;
//                startLoadMore();
//                classList.get(position).put("check", "true");
//                adapter.notifyDataSetChanged();
//            }
//        });
    }
    public void setMyPopwindowswListener(MyPopwindowsListener l) {
        mListViewListener = l;
    }
    public interface MyPopwindowsListener {
        public void onRefresh(int position);
    }

    private void startLoadMore() {

        mListViewListener.onRefresh(positions);

    }
}
