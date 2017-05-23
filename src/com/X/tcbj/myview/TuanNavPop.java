package com.X.tcbj.myview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.X.model.TuanNavModel;
import com.X.model.TuanQuanModel;
import com.X.tcbj.activity.R;
import com.X.tcbj.adapter.QuanSmallAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanNavPop {
    View popView;
    public PopupWindow popupWindow;
    TuanNavAdapter adapter;
    int positions;
    private MyNavChooseListener mListViewListener;

    public void showclasspop(List<TuanNavModel> classList, Context context, View view) {
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

        adapter = new TuanNavAdapter(classList, context);
        bigclass.setAdapter(adapter);

        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.classList.get(positions).setChecked(false);
                positions = position;
                adapter.classList.get(position).setChecked(true);
                adapter.notifyDataSetChanged();
                startLoadMore();
            }
        });
    }
    public void setMyNavChooseListener(MyNavChooseListener l) {
        mListViewListener = l;
    }
    public interface MyNavChooseListener {
        public void onChoose(TuanNavModel item);
    }

    private void startLoadMore() {

        mListViewListener.onChoose(adapter.classList.get(positions));

    }



    class TuanNavAdapter extends BaseAdapter {

        public List<TuanNavModel> classList;
        Context context;

        public TuanNavAdapter(List<TuanNavModel> classList, Context context) {
            this.classList = classList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return classList.size();
        }

        @Override
        public Object getItem(int position) {
            return classList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.class_item, null);
            getItem getItem=new getItem();
            getItem.name=(TextView)convertView.findViewById(R.id.name);
            getItem.name.setText(classList.get(position).getName());
            if (classList.get(position).isChecked()){
                getItem.name.setTextColor(Color.parseColor("#ff0000"));
                convertView.setBackgroundColor(Color.parseColor("#ffc5c5c5"));
            }
            return convertView;
        }

        private class getItem {
            TextView name;
        }
    }

}