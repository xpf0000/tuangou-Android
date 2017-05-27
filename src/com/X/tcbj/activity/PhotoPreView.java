package com.X.tcbj.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.tcbj.myview.HackyViewPager;
import com.X.tcbj.utils.Bimp;
import com.X.tcbj.utils.ImageItem;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.ref.WeakReference;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
/**
 * Created by X on 2017/1/19.
 */

public class PhotoPreView extends FragmentActivity {
    HackyViewPager viewpage;
    ImageView back,del;
    TextView num;
    LinearLayout lool;
    int index;
    ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview);
        index = getIntent().getIntExtra("index", 0);
        back = (ImageView) findViewById(R.id.back);
        del = (ImageView) findViewById(R.id.del);
        num = (TextView) findViewById(R.id.num);
        viewpage = (HackyViewPager) findViewById(R.id.viewpager);
        lool = (LinearLayout) findViewById(R.id.lool);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        viewpage.setAdapter(mAdapter);
        viewpage.setCurrentItem(index);

        boolean hidedel = getIntent().getBooleanExtra("hidedel",false);
        if(hidedel)
        {
            del.setVisibility(View.GONE);
        }

        num.setText((index+1)+"/"+ Bimp.tempSelectBitmap.size());

        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                XNetUtil.APPPrintln("onPageScrolled !!!!!!!");

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                num.setText((index+1)+"/"+Bimp.tempSelectBitmap.size());

                XNetUtil.APPPrintln("onPageSelected !!!!!!!");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                XNetUtil.APPPrintln("onPageScrollStateChanged: "+state+" !!!!!!!");

                del.setEnabled(state == 0);
                del.setClickable(state == 0);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bimp.tempSelectBitmap.remove(index);
                if(Bimp.tempSelectBitmap.size() == 0)
                {
                    finish();
                    return;
                }
                index = index - 1 < 0 ? 0 : index - 1;
                mAdapter.notifyDataSetChanged();
                viewpage.setCurrentItem(index);
                num.setText((index+1)+"/"+Bimp.tempSelectBitmap.size());
            }
        });



    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return Bimp.tempSelectBitmap.size();
        }

        @Override
        public Fragment getItem(int position) {
            ImageItem item = Bimp.tempSelectBitmap.get(position);
            return new PhotoPreViewFragment(item);
        }

        @Override
        public int getItemPosition(Object object) {
            //return super.getItemPosition(object);
            return PagerAdapter.POSITION_NONE;
        }
    }


    public static class PhotoPreViewFragment extends Fragment {
        private PhotoView mImageView;
        WeakReference<ImageItem> item;
        PhotoViewAttacher mAttacher;
        View view;

        public PhotoPreViewFragment(ImageItem item) {
            this.item = new WeakReference<ImageItem>(item);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            if(view != null)
            {
                return view;
            }

            view = inflater.inflate(R.layout.photopreviewfragment, container, false);
            mImageView = (PhotoView) view.findViewById(R.id.image);
            mAttacher = new PhotoViewAttacher(mImageView);

            mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float v, float v1) {

                    XNetUtil.APPPrintln("000 v: "+v+" | v1: "+v1);
                    getActivity().finish();
                }
            });

            mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    XNetUtil.APPPrintln("111 v: "+v+" | v1: "+v1);

                    getActivity().finish();
                }
            });


            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            if(item == null)
            {
                return;
            }

            if(item.get() == null)
            {
                return;
            }

            if(item.get().getUrl() != null)
            {
                ImageLoader.getInstance().displayImage(item.get().getUrl(), mImageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mAttacher.update();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
            else if(item.get().getBitmap() != null)
            {
                mImageView.setImageBitmap(item.get().getBitmap());
            }

            mAttacher.update();

        }


    }


}
