package com.X.tcbj.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.tcbj.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	ArrayList<HashMap<String, String>> groupArray ;
	ArrayList<ArrayList<HashMap<String, String>>> childArray;
	LayoutInflater inflater;
    Handler handler=new Handler();
	public ExpandableAdapter(Activity a, ArrayList<HashMap<String, String>> groupArray,
			ArrayList<ArrayList<HashMap<String, String>>> childArray, Context context,Handler handler) {

		this.groupArray = groupArray;
		this.childArray = childArray;
		this.context = context;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.childitem, null);
		TextView mytxt = (TextView) convertView
				.findViewById(R.id.child_item_txt);
		Button button=(Button)convertView.findViewById(R.id.bbs_guanzhu);
		int a=childPosition+1;
		TextView post=(TextView)convertView.findViewById(R.id.childtxt);
		mytxt.setText(a+"F:"+childArray.get(groupPosition).get(childPosition).get("childname"));
		post.setText(childArray.get(groupPosition).get(childPosition).get("posts"));
//		final String att=childArray.get(groupPosition).get(childPosition).get("attid");
//		if(att.equals("0")){
//			button.setBackgroundResource(R.drawable.com_plus);
//		}else{
//			button.setBackgroundResource(R.drawable.com_yes);
//		}
//		button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				int Logn=PreferencesUtils.getInt(context, "logn");
//				Constant.groupagr=groupPosition;
//				Constant.childagr=childPosition;
//				  if (Logn==0)  {
//					Intent intent = new Intent();
//					intent.setClass(context, LoginActivity.class);
//					context.startActivity(intent);
//				} else if(att.equals("0")){
//					Constant.fatheragr=childArray.get(groupPosition).get(childPosition).get("fid");
//					handler.sendEmptyMessage(3);
//				}else{
//					handler.sendEmptyMessage(5);
//				}
//
//			}
//		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childArray.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.bbs_class_item, null);
		ImageView imageView = (ImageView) layout.findViewById(R.id.groupview);
		TextView textView = (TextView) layout.findViewById(R.id.mygrouptxt);
		TextView today=(TextView)layout.findViewById(R.id.mygrouptxt3);
		if (isExpanded) {
			imageView.setImageResource(R.drawable.bbs_arrow2);
		} else {
			imageView.setImageResource(R.drawable.bbs_arrow);
		}
		textView.setText(groupArray.get(groupPosition).get("name"));
		today.setText(groupArray.get(groupPosition).get("todayTotal"));
		return layout;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}