package com.X.tcbj.adapter;

import java.util.ArrayList;
import java.util.List;

import com.X.tcbj.activity.R;
import com.X.model.CityModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 城市列表adapter
 * @author wgs
 * @date 20140421
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer{

	private class AdapterModel{
		int type = 0;
		String letter = "";
		String name = "";
		String id = "";
		String isopen = "";
	}

	private List<AdapterModel> datalist = new ArrayList<>();
	private List<CityModel.CityListBean> list = null;
	private Context mContext;

	public List<CityModel.CityListBean> getList() {
		return list;
	}

	public void setList(List<CityModel.CityListBean> list) {
		this.list = list;
		datalist.clear();
		for(CityModel.CityListBean items : list)
		{
			AdapterModel model = new AdapterModel();
			model.type = 0;
			model.letter = items.getLetter();
			datalist.add(model);
			model = null;
			for(CityModel.CityListBean.ItemsBean item : items.getItems() )
			{
				AdapterModel model1 = new AdapterModel();
				model1.type = 1;
				model1.letter = items.getLetter();
				model1.name = item.getName();
				model1.id = item.getId();
				model1.isopen = item.getIs_open();
				datalist.add(model1);
				model1 = null;
			}
		}

	}

	public SortAdapter(Context mContext) {
		this.mContext = mContext;
	}

	public SortAdapter(Context mContext, List<CityModel.CityListBean> list) {
		this.mContext = mContext;
		setList(list);
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CityModel.CityListBean> list){
		setList(list);
		notifyDataSetChanged();
	}

	public int getCount() {
		return datalist.size();
	}

	public Object getItem(int position) {
		return datalist.get(position);

	}

	public String getCityName(int p)
	{
		if(getItemViewType(p) == 0){return "";}
		return datalist.get(p).name;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return datalist.get(position).type;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		HeaderView headerView = null;
		ItemView itemView = null;

		if(getItemViewType(position) == 0)
		{
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.city_list_header, null);

				headerView = new HeaderView();
				headerView.title = (TextView) view.findViewById(R.id.city_list_header_letter);
				view.setTag(headerView);

			}
			else
			{
				headerView = (HeaderView) view.getTag();
			}


			headerView.title.setText(datalist.get(position).letter);

		}
		else
		{
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.city_list_item, null);

				itemView = new ItemView();
				itemView.title = (TextView) view.findViewById(R.id.city_list_item_title);
				view.setTag(itemView);

			}
			else
			{
				itemView = (ItemView) view.getTag();
			}


			itemView.title.setText(datalist.get(position).name);
		}

		
		return view;

	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return datalist.get(position).letter.charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = datalist.get(i).letter;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}


	private class HeaderView {
		TextView title;
	}

	private class ItemView {
		TextView title;
	}


}