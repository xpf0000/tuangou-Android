package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.PhotoLook;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.webiew;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Addadpter extends PagerAdapter {
    private List<View> mList;
    private Context context;
    ImageView image;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> imgarray;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    public Addadpter(List<View> list, Context context,
                     ArrayList<HashMap<String, String>> imgarray) {
        this.mList = list;
        this.context = context;
        this.imgarray = imgarray;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    /**
     * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
     * instantiateItem(View container, int position) This method was deprecated
     * in API level . Use instantiateItem(ViewGroup, int)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mList.get(position));

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    /**
     * Create the page for the given position.
     */
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        options = ImageUtils.setnoOptions();
        final View view = mList.get(position);
        image = (ImageView) view.findViewById(R.id.viewimage);
        final String url = imgarray.get(position).get("picurl");
        final int a = PreferencesUtils.getInt(context, "photo");
//		if (a==1){
        options = ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, image, options,
                animateFirstListener);
//		}else {
//			String urls=  ImageLoader.getDiscCache().get(url).getPath();
//			boolean bloo= ImageUtils.fileIsExists(urls);
//			if (bloo){
//				ImageLoader.displayImage(url, image, options,
//						animateFirstListener);
//			}else {
        try {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context.getClass().equals(MallInfo.class)) {
                        if (imgarray.size() > 0) {
                            getimgarrays();
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            intent.setClass(context, PhotoLook.class);
                            context.startActivity(intent);
                        }
                    } else {
                        if (imgarray.get(position).get("Url") != null) {
                            if (imgarray.get(position).get("Url").length() != 0 && !(imgarray.get(position).get("Url").equals("#"))) {
                                Intent intent = new Intent();
                                intent.setClass(context, webiew.class);
                                intent.putExtra("url", imgarray.get(position).get("Url"));
                                context.startActivity(intent);
                            }
                        }
                    }

//						ImageLoader.displayImage(url,image, options,
//								animateFirstListener);
//						image.setClickable(false);
//                Intent intent=new Intent();
//                intent.putExtra("id",imgarray.get(position).get("id"));
//                intent.setClass(context, NewsInfoActivity.class);
//                context.startActivity(intent);
                }
            });
        } catch (Exception e) {

        }
////			}
//		}
        container.removeView(mList.get(position));
        container.addView(mList.get(position));
        return mList.get(position);
    }

    private void getimgarrays() {
        ArrayList<HashMap<String, String>> imgarray1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < imgarray.size(); i++) {
            hashMap = new HashMap<String, String>();
            hashMap.put("url", imgarray.get(i).get("picurl") == null ? "" : imgarray.get(i).get("picurl"));
            imgarray1.add(hashMap);
        }
        Constant.imgarray = imgarray1;
    }

}