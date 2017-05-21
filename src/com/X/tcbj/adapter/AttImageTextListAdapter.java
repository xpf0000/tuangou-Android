package com.X.tcbj.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.myview.ImageAndText;
import com.X.tcbj.myview.ViewCache;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class AttImageTextListAdapter extends ArrayAdapter<ImageAndText> {

	private static String TAG = "diaoliang";
	private static int URLCOUNT = 0;
    private GridView gridView;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    DisplayMetrics dm;
    public AttImageTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, GridView gridView1) {
        super(activity, 0, imageAndTexts);
        this.gridView = gridView1;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dm = new DisplayMetrics();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        View rowView = convertView;
        ViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.att_gm_item, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        ImageAndText imageAndText = getItem(position);

        // Load the image and set it on the ImageView
        String imageUrl = imageAndText.getImageUrl();
//        MyCircleImageView imageView = (MyCircleImageView)rowView.findViewById(R.id.att_photonews);
//        imageView.setRect(false, R.color.black);
//        int color = activity.getResources().getColor(R.color.greyy);
//        imageView.setRoundCorner(true, color);
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//    	LayoutParams params = new LayoutParams(
//    			(int) (dm.widthPixels/(4.5)), dm.heightPixels/8);
//    	imageView.setLayoutParams(params);
//        //设置标签
//		String Tag = imageUrl+ URLCOUNT;
//        imageView.setTag(Tag);
//        URLCOUNT++;
//        if(imageUrl.equals("空")){
//        	 imageView.setBackgroundResource(R.drawable.att_com_add);
//        }else{
//            options=ImageUtils.setcenterOptions();
//            ImageLoader.displayImage(imageUrl, imageView, options,
//                    animateFirstListener);
//        }
        // Set the text on the TextView
        TextView textView = (TextView)rowView.findViewById(R.id.att_photo_txt);
        textView.setText(imageAndText.getText());
        return rowView;
    }

}