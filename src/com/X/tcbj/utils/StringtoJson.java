package com.X.tcbj.utils;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

public class StringtoJson {
	public String getJson(Object object,int totalRecord) {
		HashMap<String ,Object> hashMap=new HashMap<String, Object>();
		hashMap.put("status", 0);
		hashMap.put("totalRecord",totalRecord);
		hashMap.put("list", object);
		String myjson=JSON.toJSONString(hashMap);
		return myjson;
		
	}

}
