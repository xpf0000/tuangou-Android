package com.hkkj.csrx.activity;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hkkj.csrx.myview.KeywordsFlow;
import com.hkkj.csrx.utils.CommonField;
import com.hkkj.csrx.utils.Constant;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页和商家搜索页面
 * 
 * @author zmz
 * 
 */
public class HotSearch extends Activity implements OnClickListener {

	private KeywordsFlow keywordsFlow;
	private TextView quxiao;
	AutoCompleteTextView mytext;
	ImageView cancel;

	// String searchKey;//搜索关键词

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		quxiao = (TextView) findViewById(R.id.quxiao);
		cancel = (ImageView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		mytext = (AutoCompleteTextView) findViewById(R.id.shopSearch);
		// 设置软键盘的相关操作
		mytext.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					search();
					return true;
				}
				return false;
			}
		});
		ArrayAdapter<String> adr = new ArrayAdapter<String>(this,
				R.layout.search_drow, R.id.contentTextView,
				CommonField.keywords);
		mytext.setAdapter(adr);
		quxiao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				search();
			}
		});
		keywordsFlow = (KeywordsFlow) findViewById(R.id.keywordsflow);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		// 添加
		feedKeywordsFlow(keywordsFlow, CommonField.keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
	}

	private void feedKeywordsFlow(KeywordsFlow keywordsFlow, List<String> arr) {
		int showText = 0;
		if (arr.size() < KeywordsFlow.MAX) {
			showText = arr.size();
		} else {
			showText = KeywordsFlow.MAX;
		}

		int ram[] = random2(showText);
		for (int i : ram) {
			String tmp = arr.get(i);
			keywordsFlow.feedKeyword(tmp);
		}
	}

	/**
	 * 产生不重复的随机数
	 * 
	 * @param
	 * @return 随机数组
	 */
	public int[] random2(int max) {
		int send[] = new int[max];
		for (int i = 0; i < max; i++) {
			send[i] = i;
		}
		int temp1, temp2, temp3;
		Random r = new Random();
		for (int i = 0; i < send.length; i++) // 随机交换send.length次
		{
			if ((max - 1) == 0) {
				temp1 = Math.abs(r.nextInt()) % (max); // 随机产生一个位置
				temp2 = Math.abs(r.nextInt()) % (max); // 随机产生另一个位置
			} else {
				temp1 = Math.abs(r.nextInt()) % (max - 1); // 随机产生一个位置
				temp2 = Math.abs(r.nextInt()) % (max - 1); // 随机产生另一个位置
			}

			if (temp1 != temp2) {
				temp3 = send[temp1];
				send[temp1] = send[temp2];
				send[temp2] = temp3;
			}
		}
		return send;
	}

	/**
	 * 进入搜索
	 */
	public void search() {
		String searchstr = mytext.getText().toString();
		if (searchstr.equals("")) {
			Toast.makeText(HotSearch.this, "搜索内容不能为空！", Toast.LENGTH_SHORT)
					.show();
		} else {
			Intent toShop = new Intent(HotSearch.this, ShangjiasActivicty.class);
			Constant.MERCHANT_SORT_ID = "0";
			Constant.MERCHANT_CLASSIFY_ID = "0";
			Constant.KEY = searchstr;
			// System.out.println("搜索框搜索词："+Constant.KEY);
			// 向商家列表传递参数
			toShop.putExtra("activity", "search");
			startActivity(toShop);
			// HotSearch.this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			Intent toShop = new Intent(HotSearch.this, ShangjiasActivicty.class);
			Constant.KEY = keyword;
			Constant.MERCHANT_SORT_ID = "0";
			Constant.MERCHANT_CLASSIFY_ID = "0";
			// System.out.println("搜索词："+keyword);
			// 向商家列表传递参数
			toShop.putExtra("activity", "search");
			startActivity(toShop);
		} else if (v instanceof ImageView) {
			mytext.setText("");
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}