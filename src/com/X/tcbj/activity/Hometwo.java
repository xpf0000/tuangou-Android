package com.X.tcbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MeetingPe;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class Hometwo extends Fragment {
	View view;
	GridView homepl;
	String url, plstr;
	int cityID = 1;
	ArrayList<HashMap<String, String>> priearray = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> prihashMap;
	MeetingPe peadapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.plone, null);
		homepl = (GridView) view.findViewById(R.id.homepl);
		cityID = PreferencesUtils.getInt(getActivity(), "cityID");
		url = Constant.url+"discountMessage?areaId="
				+ cityID;
		getpl(1);
		homepl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("StoreID", priearray.get(arg2).get("storeId"));
				intent.putExtra("ID", priearray.get(arg2).get("disID"));
				intent.setClass(getActivity(), Privileinfo.class);
				getActivity().startActivity(intent);

			}
		});
		return view;
	}

	void getpl(int what) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				HttpRequest httpRequest = new HttpRequest();
				plstr = httpRequest.doGet(url, getActivity());
				if (plstr.equals("网络超时")) {
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					primap(plstr);
					stepl();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				Toast.makeText(getActivity(), "暂无优惠数据", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}

		};
	};

	void primap(String str) throws JSONException {
		JSONObject jsonObject = JSONObject.parseObject(str);
		int a = jsonObject.getIntValue("status");
		if (a == 1) {
			handler.sendEmptyMessage(3);
		} else {
			int stat = 0;

			JSONArray jsonArray = jsonObject.getJSONArray("list");
			if (jsonArray.size() > 2) {
				for (int i = 2; i < jsonArray.size(); i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					prihashMap = new HashMap<String, String>();
					prihashMap.put("disImg",
							jsonObject2.getString("disImg") == null ? ""
									: jsonObject2.getString("disImg"));
					prihashMap.put("title",
							jsonObject2.getString("title") == null ? ""
									: jsonObject2.getString("title"));
					prihashMap.put("storeId",
							jsonObject2.getString("storeId") == null ? ""
									: jsonObject2.getString("storeId"));
					prihashMap.put("disID",
							jsonObject2.getString("disID") == null ? ""
									: jsonObject2.getString("disID"));
					prihashMap.put("endTime",
							jsonObject2.getString("endTime") == null ? ""
									: jsonObject2.getString("endTime"));
					prihashMap.put("clickNum",
							jsonObject2.getString("clickNum") == null ? ""
									: jsonObject2.getString("clickNum"));
					priearray.add(prihashMap);
				}
			} else {
				
			}
			
		}
	}

	void stepl() {
		peadapter = new MeetingPe(priearray, getActivity());
		homepl.setAdapter(peadapter);
	}
}
