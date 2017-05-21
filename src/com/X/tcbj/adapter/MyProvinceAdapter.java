package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.domain.Area;

import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MyProvinceAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<Area.Province> provinceList;
    public MyProvinceAdapter(Context ctx,List<Area.Province> provinceList){
        this.ctx=ctx;
        this.provinceList=provinceList;
        inflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Object getItem(int i) {
        return provinceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        if (view==null){
            view=inflater.inflate(R.layout.xuanzechengshi_item,null);
            vh=new ViewHolder();
            vh.tv=(TextView)view.findViewById(R.id.name);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.tv.setText(provinceList.get(i).getName());
        return view;
    }
    public class ViewHolder{
        TextView tv;
    }
}
