package com.X.tcbj.myview;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.tcbj.activity.R;


/**
 * Created by lgh on 2015-05-11
 */

public class SearchView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 返回按钮
     */
    private Button btnBack;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    private Handler handler=new Handler();
    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener,Handler handler) {
        mListener = listener;
        this.handler=handler;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (Button) findViewById(R.id.search_btn_back);

        ivDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
            } else {
                ivDelete.setVisibility(GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
            case R.id.search_btn_back:
//                ((Activity) mContext).finish();

                handler.sendEmptyMessage(2);
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {
        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

    }

}


