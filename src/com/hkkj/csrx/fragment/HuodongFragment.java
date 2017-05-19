package com.hkkj.csrx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.Constant;

/**
 * Created by Administrator on 2016/5/21.
 */
public class HuodongFragment extends Fragment implements View.OnClickListener
{
private  View view;
    private WebView textView;
   ScrollView listView;
    String str;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.huodong, container, false);
        textView=(WebView)view.findViewById(R.id.huodongguize);
        if (Constant.webview==null){
            textView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
        }else {
            textView.loadDataWithBaseURL(null, Constant.webview, "text/html", "utf-8", null);
        }
       str= Constant.webview;
        Constant.webview=null;
        listView=(ScrollView) getActivity().findViewById(R.id.fatview);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    listView.requestDisallowInterceptTouchEvent(false);
                }else {
                    listView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onClick(View v)
    {


    }
}
