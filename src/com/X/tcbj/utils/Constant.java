package com.X.tcbj.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant {

	public static String CITY_DATA;// 获取网络的城市列表数据

	public static String CITY_POSITION;// 定位到的城市

	public static String AREA_ID;// 站点ID

	public static String MERCHANT_SORT_ID = "0";// 商家一级分类id
	public static String MERCHANT_CLASSIFY_ID = "0";// 商家二级分类ID
	public static String MERCHANT_CLASSIFY_NAME = "全部分类";// 商家二级分类名称

	public static String SHOP_SORT_ID = "0";// 商家Activity一级分类id
	public static String SHOP_CLASSIFY_ID = "0";// 商家Activity二级分类ID
	public static String SHOP_CLASSIFY_NAME = "全部分类";// 商家二级分类名称（activity）

	public static String PRIVILEGE_CLASSIFY_ID = "0";// 优惠一级分类ID
	public static String PRIVILEGE_SORT_ID = "0";// 优惠二级分类ID
	public static String PRIVILEGE_CLASSIFY_NAME = "全部分类";// 优惠列表二级分类名称

	public static String SHOP_ID;// 商家详情页对应的id
	public static String SHOP_IMG;// 商家详情页对应的图片地址

	public static String KEY = null;// 搜索词

	// 地图显示所需要的相关参数
	public static String SHOP_NAME;// 商家详情页对应的店名
	public static String SHOP_ADDRESS;// 商家地址
	public static double SHOP_LONGITUDE;// 商家经度
	public static double SHOP_LATITUDE;// 商家纬度
	public static String commentId;
	public static String siteid;
	public static int attarg2;
	public static String fatheragr;
	public static int groupagr;
	public static int childagr;
	public static int Votepo;
	public static String title;
	public static int stite=1;
	public static int status=0;


		public static String url="http://webapi.rexian.cn//HKCityApi/";

	public static String  productClassId;
	public static String  name;
	public static int position;
	public static	ArrayList<HashMap<String, String>> imgarray ;
	public static ArrayList<HashMap<String, Object>> array=new ArrayList<HashMap<String, Object>>();
	public static boolean  all=false;
	public static String  productClassId1;

	// 商户收款账号
	public static final String SELLER = "2088212688715226";
	public static String APP_ID="wxec4f19cde6fa4597";
	public static String webview="wx2fbb20c41b2d3e3d";
	public static String  citystr;

}
