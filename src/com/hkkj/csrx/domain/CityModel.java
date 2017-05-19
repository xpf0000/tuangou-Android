package com.hkkj.csrx.domain;


/**
 * 城市属性实体类
 * @author wgs
 */
public class CityModel
{
	private int CityID;
	
	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}

	private String CityName; //城市名字
	private String NameSort; //城市首字母
	private String CityNamepl; //城市pinyin
	
	public String getCityNamepl() {
		return CityNamepl;
	}

	public void setCityNamepl(String cityNamepl) {
		CityNamepl = cityNamepl;
	}

	public String getCityName()
	{
		return CityName;
	}

	public void setCityName(String cityName)
	{
		CityName = cityName;
	}

	public String getNameSort()
	{
		return NameSort;
	}

	public void setNameSort(String nameSort)
	{
		NameSort = nameSort;
	}

}
