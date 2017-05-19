package com.hkkj.csrx.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.ImageAndText;
import com.hkkj.csrx.myview.ViewCache;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {

	private static String TAG = "diaoliang";
	private static int URLCOUNT = 0;
	private GridView gridView;
	// private AsyncImageLoader_photo asyncImageLoader;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.hkkj.csrx.utils.ImageUtils ImageUtils ;
	ImageLoadingListener animateFirstListener;
	DisplayMetrics dm;

	public ImageAndTextListAdapter(Activity activity,
			List<ImageAndText> imageAndTexts, GridView gridView1) {
		super(activity, 0, imageAndTexts);
		this.gridView = gridView1;
		// asyncImageLoader = new AsyncImageLoader_photo();
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
			rowView = inflater.inflate(R.layout.photo_news_item, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ImageAndText imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		final String imageUrl = imageAndText.getImageUrl();
		ImageView imageView = viewCache.getImageView();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams params = new LayoutParams(dm.widthPixels / 2,
				dm.heightPixels / 4);
		imageView.setLayoutParams(params);
		// 设置标签
		String Tag = imageUrl + URLCOUNT;
		imageView.setTag(Tag);
		URLCOUNT++;
		options=ImageUtils.setcenterOptions();
		ImageLoader.displayImage(imageUrl,imageView, options,
				animateFirstListener);
		// Set the text on the TextView
		TextView textView = viewCache.getTextView();
		textView.setText(imageAndText.getText());
		return rowView;
	}
}