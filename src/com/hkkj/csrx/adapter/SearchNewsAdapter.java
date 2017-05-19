package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/8/12.
 */
public class SearchNewsAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    private Context context;

    public SearchNewsAdapter(Context context,
                               ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        MyHolder myHolder = null;
        if (arg1 == null) {
            myHolder = new MyHolder();
            arg1 = LayoutInflater.from(context).inflate(
                    R.layout.informationtxt, null);
            myHolder.text_title = (TextView) arg1
                    .findViewById(R.id.infor_list_txt);
            myHolder.info = (TextView) arg1
                    .findViewById(R.id.infor_list_txtadd);
            myHolder.see = (TextView) arg1.findViewById(R.id.infor_list_see);
            myHolder.info_image = (ImageView) arg1
                    .findViewById(R.id.infor_list_img);
            arg1.setTag(myHolder);
        } else {
            myHolder = (MyHolder) arg1.getTag();
        }
        myHolder.text_title.setText(list.get(arg0).get("title").toString());
        myHolder.info.setText(list.get(arg0).get("description").toString());
        myHolder.see.setText(list.get(arg0).get("clickNum").toString());
        int a = PreferencesUtils.getInt(context, "photo");
        String path = null;
        if(list.get(arg0).get("picId").toString().length()==0){
            myHolder.info_image.setVisibility(View.GONE);
        }else{
            myHolder.info_image.setVisibility(View.VISIBLE);
            System.out.println("list.get(arg0):"+list.get(arg0).get("picId"));
            path = list.get(arg0).get("picId").toString();
            options=ImageUtils.setcenterOptions();
            ImageLoader.displayImage(path, myHolder.info_image, options,
                    animateFirstListener);
        }

        myHolder.info_image.setTag(list.get(arg0).get("picId"));

        return arg1;
    }

    public class MyHolder {
        TextView text_title, info, see;
        ImageView info_image;
    }
}
