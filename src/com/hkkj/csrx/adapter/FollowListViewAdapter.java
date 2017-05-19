package com.hkkj.csrx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hkkj.csrx.activity.HomePageActivity;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.activity.ShopsListActivity;
import com.hkkj.csrx.myview.FollowListView;
import com.hkkj.csrx.utils.Constant;

/**
 * 二级菜单
 * 
 * @author zmz
 * 
 */
public class FollowListViewAdapter extends BaseExpandableListAdapter implements
		OnItemClickListener {

	private FollowListView toolbarGrid;
	private List<TreeNode> treeNodes = new ArrayList<TreeNode>();

	// 二级分类
	ArrayList<HashMap<String, Object>> data;
	HashMap<String, Object> map;

	private Context parentContext;

	private LayoutInflater layoutInflater;

	private SimpleAdapter simpleAdapter;

	private int group;// 所属的一级分类坐标

	String activityName;// 将要跳转到哪个页面

	static public class TreeNode {
		public String ID;
		public Object parent;
		public Integer parentImg;
		public List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		// public List<Map<String, Object>> childsName = new
		// ArrayList<Map<String, Object>>();
	}

	public FollowListViewAdapter(Context context, String activityName) {
		parentContext = context;
		this.activityName = activityName;
	}

	/**
	 * 获取TreeNodes
	 * 
	 * @return
	 */

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	/**
	 * 更新TreeNodes中的数据
	 * 
	 * @return
	 */
	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	/**
	 * 清除TreeNode中的数据
	 */
	public void RemoveAll() {
		treeNodes.clear();
	}

	/**
	 * 获取组员名
	 */
	public Object getChild(int arg0, int arg1) {
		return treeNodes.get(arg0).childs.get(arg1);
	}

	/**
	 * 获取组员id
	 */
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	/**
	 * 自定义组员
	 */
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		layoutInflater = (LayoutInflater) parentContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arg3 = layoutInflater.inflate(R.layout.merchant_follow_view, null);
		toolbarGrid = (FollowListView) arg3.findViewById(R.id.grid_bar);
		toolbarGrid.setAdapter(getMenuAdapter(arg0));// 设置菜单Adapter
		group = arg0;
		toolbarGrid.setOnItemClickListener(this);// 点击跳转事件
		return arg3;
	}

	/**
	 * 返回值必须是1，否则会重复数据
	 */
	public int getChildrenCount(int arg0) {
		return 1;
	}

	/**
	 * 获取组名
	 */
	public Object getGroup(int arg0) {
		return treeNodes.get(arg0).parent;
	}

	/**
	 * 统计组的长度
	 */
	public int getGroupCount() {
		return treeNodes.size();
	}

	/**
	 * 获取组id
	 */
	public long getGroupId(int arg0) {
		return arg0;
	}

	/**
	 * 自定义组
	 */
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		MyHolder myHolder = null;
		if (arg2 == null) {
			myHolder = new MyHolder();
			arg2 = layoutInflater.from(parentContext).inflate(
					R.layout.merchant_group_item, null);
			myHolder.img_log = (ImageView) arg2.findViewById(R.id.img_log);
			myHolder.txt_title = (TextView) arg2.findViewById(R.id.txt_title);
			myHolder.img_img = (ImageView) arg2.findViewById(R.id.img_img);
			arg2.setTag(myHolder);
		} else {
			myHolder = (MyHolder) arg2.getTag();
		}
		// 为控件赋值
		myHolder.img_log.setImageResource(getGroupImg(arg0));
		myHolder.txt_title.setText(getGroup(arg0).toString());
		// 收缩/展开，改变图片
		if (arg1) {
			myHolder.img_img.setBackgroundResource(R.drawable.arrowl_down);
		} else {
			myHolder.img_img.setBackgroundResource(R.drawable.arrowl);
		}
		return arg2;
	}

	public boolean hasStableIds() {
		return true;
	}

	public Integer getGroupImg(int groupPosition) {
		return treeNodes.get(groupPosition).parentImg;
	}

	/**
	 * 定义组员是否可选
	 */
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	private SimpleAdapter getMenuAdapter(int groupPosition) {
		data = new ArrayList<HashMap<String, Object>>();
		int length = treeNodes.get(groupPosition).childs.size();
		for (int i = 0; i < length; i++) {
			map = new HashMap<String, Object>();
			map.put("classify",
					treeNodes.get(groupPosition).childs.get(i).get("Name"));
			map.put("ID", treeNodes.get(groupPosition).childs.get(i).get("ID"));
			data.add(map);
		}
		simpleAdapter = new SimpleAdapter(parentContext, data,
				R.layout.merchant_follow_view_item,
				new String[] { "classify" },
				new int[] { R.id.txt_merchant_follow_item });
		return simpleAdapter;
	}

	final class MyHolder {
		// 放入控件
		ImageView img_log, img_img;
		TextView txt_title;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Intent intent = new Intent();
		if (activityName.equals("PrivilegeFragment")) {
			// 跳转到优惠列表
			Constant.PRIVILEGE_CLASSIFY_ID = treeNodes.get(group).childs
					.get(arg2).get("ID").toString();
			Constant.PRIVILEGE_SORT_ID = treeNodes.get(group).ID;
			Constant.KEY = null;
			Constant.PRIVILEGE_CLASSIFY_NAME = data.get(arg2).get("classify")
					.toString();
			intent.setClass(parentContext, HomePageActivity.class);
			intent.putExtra("activity", "PrivilegeFragment");
		} else if (activityName.equals("MerchantFragment")
				|| activityName == null) {
			// 跳转到商品列表
			Constant.MERCHANT_CLASSIFY_ID = treeNodes.get(group).childs
					.get(arg2).get("ID").toString();
			Constant.MERCHANT_SORT_ID = treeNodes.get(group).ID;
			Constant.KEY = null;
			Constant.MERCHANT_CLASSIFY_NAME = data.get(arg2).get("classify")
					.toString();
			intent.setClass(parentContext, HomePageActivity.class);
			intent.putExtra("activity", "MerchantFragment");
		} else if (activityName.equals("ShopsListActivity")) {
			// 跳转到商品列表Activity
			Constant.SHOP_CLASSIFY_ID = treeNodes.get(group).childs.get(arg2)
					.get("ID").toString();
			Constant.SHOP_SORT_ID = treeNodes.get(group).ID;
			Constant.KEY = null;
			Constant.SHOP_CLASSIFY_NAME = data.get(arg2).get("classify")
					.toString();
			intent.setClass(parentContext, ShopsListActivity.class);
			intent.putExtra("activity", "ShopsListActivity");
		} else if (activityName.equals("HomeFragment")) {
			// 跳转到商家列表
			// Constant.MERCHANT_CLASSIFY_ID = treeNodes.get(group).childs
			// .get(arg2).get("ID").toString();
			// Constant.MERCHANT_SORT_ID = treeNodes.get(group).ID;
			Constant.SHOP_CLASSIFY_ID = treeNodes.get(group).childs.get(arg2)
					.get("ID").toString();
			Constant.SHOP_SORT_ID = treeNodes.get(group).ID;
			Constant.KEY = null;
			Constant.SHOP_CLASSIFY_NAME = data.get(arg2).get("classify")
					.toString();
			intent.setClass(parentContext, ShopsListActivity.class);
			intent.putExtra("activity", "ShopsListActivity");
		}

		toolbarGrid.getContext().startActivity(intent);
		((Activity) parentContext).finish();
	}
}
