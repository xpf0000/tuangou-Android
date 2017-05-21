package com.X.tcbj.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.X.tcbj.activity.R;

/**
 * 商家列表的虚拟数据
 * 
 * @author zmz
 * 
 */
public class Data {
	public static List<Map<String, Object>> groups = new ArrayList<Map<String, Object>>();
	public static Map<String, List<Map<String, Object>>> childs = new HashMap<String, List<Map<String, Object>>>();
	public static List<Integer> gcategory = new ArrayList<Integer>();

	// 一级菜单标题
	public List<Map<String, Object>> initGroups() {
		groups.clear();
		Map<String, Object> group = new HashMap<String, Object>();
		group.put("name", "全部分类");
		group.put("id", 1);
		group.put("iv", R.drawable.bs01);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "餐饮美食");
		group.put("id", 2);
		group.put("iv", R.drawable.bs1);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "娱乐休闲");
		group.put("id", 3);
		group.put("iv", R.drawable.bs94);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "教育培训");
		group.put("id", 4);
		group.put("iv", R.drawable.bs9);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "便民服务");
		group.put("id", 5);
		group.put("iv", R.drawable.bs3);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "购物服务");
		group.put("id", 6);
		group.put("iv", R.drawable.bs95);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "美容保健");
		group.put("id", 7);
		group.put("iv", R.drawable.bs5);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "旅游酒店");
		group.put("id", 8);
		group.put("iv", R.drawable.bs6);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "婚庆服务");
		group.put("id", 9);
		group.put("iv", R.drawable.bs7);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "数码家电");
		group.put("id", 10);
		group.put("iv", R.drawable.bs8);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "母婴亲子");
		group.put("id", 11);
		group.put("iv", R.drawable.bs10);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "金融服务");
		group.put("id", 12);
		group.put("iv", R.drawable.bs11);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "汽车服务");
		group.put("id", 13);
		group.put("iv", R.drawable.bs12);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "家具服务");
		group.put("id", 14);
		group.put("iv", R.drawable.bs175);
		groups.add(group);
		group = new HashMap<String, Object>();
		group.put("name", "房产服务");
		group.put("id", 15);
		group.put("iv", R.drawable.bs174);
		groups.add(group);
		return groups;
	}

	// 二级菜单信息
	public Map<String, List<Map<String, Object>>> initChilds() {
		childs.clear();
		List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("id", 11);
		map.put("name", "大米");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 12);
		map.put("name", "小麦");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 13);
		map.put("name", "玉米");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 14);
		map.put("name", "黄豆");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 15);
		map.put("name", "绿豆");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 16);
		map.put("name", "胡豆");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 17);
		map.put("name", "赤豆");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 18);
		map.put("name", "土豆");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 19);
		map.put("name", "玉米油");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 20);
		map.put("name", "花生油");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 21);
		map.put("name", "大豆油");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 22);
		map.put("name", "芝麻油");
		child.add(map);
		childs.put("1", child);
		// /////////////////////////////////////////////////////////////
		child = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		// map.put("id", 23);
		map.put("name", "红酒");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 24);
		map.put("name", "白酒");
		child.add(map);
		childs.put("2", child);
		// /////////////////////////////////////////////////////////////
		child = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		// map.put("id", 25);
		map.put("name", "红酒");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 26);
		map.put("name", "白酒");
		child.add(map);
		childs.put("3", child);
		// //////////////////////
		child = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		// map.put("id", 27);
		map.put("name", "红酒");
		child.add(map);
		map = new HashMap<String, Object>();
		// map.put("id", 28);
		map.put("name", "白酒");
		child.add(map);
		childs.put("4", child);
		return childs;

	}
}
