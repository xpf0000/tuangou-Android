package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class vipyouhuiAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> abscure_list;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;


    public  vipyouhuiAdapter(ArrayList<HashMap<String, String>> abscure_list,
                              Context context) {
        this.abscure_list = abscure_list;
        this.context = context;

        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    public int getCount() {
        return abscure_list.size();
    }

    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Abscure abscure = new Abscure();
        if (convertView == null) {
            abscure = new Abscure();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.privilege_list_txt, parent, false);
            abscure.txt = (TextView) convertView
                    .findViewById(R.id.privile_list_txt);
            abscure.add = (TextView) convertView
                    .findViewById(R.id.privile_list_txtadd);
            abscure.tim = (TextView) convertView
                    .findViewById(R.id.privile_list_txttim);
            abscure.storeimg = (ImageView) convertView
                    .findViewById(R.id.privile_list_img);
            convertView.setTag(abscure);
        } else {
            abscure = (Abscure) convertView.getTag();
        }
        abscure.txt.setText(abscure_list.get(position).get("Title"));
        abscure.add.setText(abscure_list.get(position).get("address"));
        abscure.tim.setText(abscure_list.get(position).get("EndTime") + "前");
        int a = PreferencesUtils.getInt(context, "photo");
        final String imgPath = abscure_list.get(position).get("PicID").toString();
        abscure.storeimg.setTag(abscure_list.get(position).get("PicID"));
        abscure.storeimg.setBackgroundResource(R.drawable.head);
        int photoInt = PreferencesUtils.getInt(context, "photo");
        options=ImageUtils.setcenterOptions();
        if (photoInt==1){
            ImageLoader.displayImage(imgPath, abscure.storeimg, options,
                    animateFirstListener);
        }else {
            String urls=  ImageLoader.getDiscCache().get(imgPath).getPath();
            boolean bloo= ImageUtils.fileIsExists(urls);
            if (bloo){
                ImageLoader.displayImage(imgPath, abscure.storeimg, options,
                        animateFirstListener);
            }else {
                final String finalImgPath = imgPath;
                final Abscure finalMyHolder = abscure;
                abscure.storeimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoader.displayImage(finalImgPath, finalMyHolder.storeimg, options,
                                animateFirstListener);
                        finalMyHolder.storeimg.setClickable(false);
                    }
                });
            }
        }

        return convertView;
    }

    class Abscure {
        TextView add, tim, txt;
        ImageView storeimg;
    }
}