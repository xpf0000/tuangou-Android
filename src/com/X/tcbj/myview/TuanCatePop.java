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

import com.X.model.TuanCateModel;
import com.X.tcbj.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanCatePop {
    View popView;
    public PopupWindow popupWindow;
    CateAdapter bigAdapter;

    CateSmallAdapter smallAdapter;

    public int positions=0, getPositions=0;

    MyCateChooseListener myPopwindowsmListener;

    public String big_id = "";

    public void showclasspop(final List<TuanCateModel> classList, Context context, View view) {
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

        bigAdapter = new CateAdapter(classList, context);
        bigclass.setAdapter(bigAdapter);

        smallAdapter = new CateSmallAdapter(classList.get(positions).getBcate_type(), context);
        smallclass.setAdapter(smallAdapter);

        if(!big_id.equals(""))
        {
            int i = 0;
            for(TuanCateModel m : bigAdapter.classList)
            {
                if(big_id.equals(m.getId()+""))
                {
                    positions = i;
                }

                i++;
            }

            startLoadMore();
            bigAdapter.classList.get(positions).setChecked(true);
            bigAdapter.notifyDataSetChanged();

        }



        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bigAdapter.classList.get(positions).setChecked(false);
                positions = position;
                startLoadMore();
                big_id = bigAdapter.classList.get(position).getId()+"";
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

    public void setMyCateChooseListener(MyCateChooseListener l) {
        myPopwindowsmListener = l;
    }

    public interface MyCateChooseListener {

        public void onChoose(TuanCateModel.BcateTypeBean item);

    }
    private void startLoadMore() {

        smallAdapter.classList = bigAdapter.classList.get(positions).getBcate_type();
        smallAdapter.notifyDataSetChanged();

    }

    private void startUrlLoadMore() {

        myPopwindowsmListener.onChoose(smallAdapter.classList.get(getPositions));

    }


    class CateAdapter extends BaseAdapter {

        public List<TuanCateModel> classList;
        Context context;

        public CateAdapter(List<TuanCateModel> classList, Context context) {
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



    class CateSmallAdapter extends BaseAdapter {

        public List<TuanCateModel.BcateTypeBean> classList;
        Context context;

        public CateSmallAdapter(List<TuanCateModel.BcateTypeBean> classList, Context context) {
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

