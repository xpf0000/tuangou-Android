package com.X.tcbj.adapter;

import java.util.List;
import com.X.tcbj.activity.R;
import com.X.tcbj.myview.ImageAndText;
import com.X.tcbj.myview.ViewCache;
import com.X.tcbj.utils.AsyncImageLoader_photo;
import com.X.tcbj.utils.AsyncImageLoader_photo.ImageCallback;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ImageTextListAdapter extends ArrayAdapter<ImageAndText> {

	private static String TAG = "diaoliang";
	private static int URLCOUNT = 0;
    private GridView gridView;
    private AsyncImageLoader_photo asyncImageLoader;
    DisplayMetrics dm;
    public ImageTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, GridView gridView1) {
        super(activity, 0, imageAndTexts);
        this.gridView = gridView1;
        asyncImageLoader = new AsyncImageLoader_photo();
        dm = new DisplayMetrics();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        View rowView = convertView;
        ViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.photo_news_items, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        ImageAndText imageAndText = getItem(position);

        // Load the image and set it on the ImageView
        String imageUrl = imageAndText.getImageUrl();
        ImageView imageView = (ImageView)rowView.findViewById(R.id.ba_photonews);
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
       
    	LayoutParams params = new LayoutParams(
    			dm.widthPixels/4, dm.heightPixels/8);
    	imageView.setLayoutParams(params);
        //设置标签 
		String Tag = imageUrl+ URLCOUNT;
        imageView.setTag(Tag);
        URLCOUNT++;
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,Tag,new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl, String Tag) {
                ImageView imageViewByTag = (ImageView) gridView.findViewWithTag(Tag);
                if (imageViewByTag != null) {
                    imageViewByTag.setBackgroundDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            imageView.setBackgroundResource(R.drawable.nopic2);
        }else{
            imageView.setBackgroundDrawable(cachedImage);
        }
        // Set the text on the TextView
        TextView textView = (TextView)rowView.findViewById(R.id.ba_photo_txt);
        textView.setText(imageAndText.getText());
        return rowView;
    }

}