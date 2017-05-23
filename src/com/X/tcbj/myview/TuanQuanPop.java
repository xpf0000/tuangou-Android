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

import com.X.model.TuanQuanModel;
import com.X.tcbj.activity.R;

import com.X.tcbj.adapter.QuanAdapter;
import com.X.tcbj.adapter.QuanSmallAdapter;

import java.util.List;

/**
 * Created by admins on 2016/7/6.
 */
public class TuanQuanPop {
    View popView;
    public PopupWindow popupWindow;
    QuanAdapter bigAdapter;

    QuanSmallAdapter smallAdapter;

    public int positions=0, getPositions=0;

    MyQuanChooseListener myPopwindowsmListener;

    public void showclasspop(final List<TuanQuanModel> classList,Context context, View view) {
        popView = LayoutInflater.from(context).inflate(R.layout.myclass, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.update();
        popupWindow.showAsDropDown(view);
        ListView bigclass = (ListView) popView.findViewById(R.id.bigclass);
        ListView smallclass = (ListView) popView.findViewById(R.id.smallclass);

        bigAdapter = new QuanAdapter(classList, context);
        bigclass.setAdapter(bigAdapter);

        smallAdapter = new QuanSmallAdapter(classList.get(positions).getQuan_sub(), context);
        smallclass.setAdapter(smallAdapter);

        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bigAdapter.classList.get(positions).setChecked(false);
                positions = position;
                startLoadMore();
                bigAdapter.classList.get(position).setChecked(true);
                bigAdapter.notifyDataSetChanged();
            }
        });

        smallclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPositions=position;
                smallAdapter.classList.get(position).setChecked(true);
                smallAdapter.notifyDataSetChanged();
                startUrlLoadMore();
            }
        });
    }

    public void setMyQuanChooseListener(MyQuanChooseListener l) {
        myPopwindowsmListener = l;
    }

    public interface MyQuanChooseListener {

        public void onChoose(TuanQuanModel.QuanSubBean item);

    }
    private void startLoadMore() {

        smallAdapter.classList = bigAdapter.classList.get(positions).getQuan_sub();
        smallAdapter.notifyDataSetChanged();

    }

    private void startUrlLoadMore() {

        myPopwindowsmListener.onChoose(smallAdapter.classList.get(getPositions));

    }
}
