package com.hkkj.csrx.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hkkj.csrx.myview.ZViewPager;
import com.hkkj.csrx.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/31.
 */
public class PhotoLook extends FragmentActivity {
    ZViewPager viewpage;
    ImageView back;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> parray=new ArrayList<HashMap<String, String>>();
    LinearLayout lool;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolook);
        pos = getIntent().getIntExtra("position",0);
        viewpage = (ZViewPager) findViewById(R.id.viewpager);
        lool = (LinearLayout) findViewById(R.id.lool);
        parray=Constant.imgarray;
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), parray);
        viewpage.setAdapter(mAdapter);
        viewpage.setCurrentItem(pos);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<HashMap<String, String>> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position).get("url");
            return ImageDetailFragment.newInstance(url);
        }
    }
}