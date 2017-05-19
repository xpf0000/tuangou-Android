package com.hkkj.csrx.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant {
	public static String ADDRESS_ALL = "http://api.rexian.cn/api/v2/";
	public static String CITY_DATA;// 获取网络的城市列表数据

	public static String CITY_POSITION;// 定位到的城市
	public static String CITY_ID;// 定位到城市所属的id

	public static String AREA_ID;// 站点ID
	public static String MERCHANT_LIST;// 商家列表网址

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
	public static int POSITION = 0;// 定位判断
	// public static boolean NET_STATIC;// 网络链接状态

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
	public static String buttonname;
	//测试接口
//	public static String url="http://api.rexian.co/HKCityApi/";
	//线上接口
		public static String url="http://webapi.rexian.cn//HKCityApi/";
//	public static String url="http://appapi.rexian.cn:8080/HKCityApi/";
//	public static String url="http://appapi01.rexian.cn/HKCityApi/";
//  public static String url="http://192.168.2.28:8080/HKCityApi/";
//	public static String url="http://123.57.147.214:8080/HKCityApi/";
	public static String  productClassId;
	public static String  name;
	public static int position;
	public static	ArrayList<HashMap<String, String>> imgarray ;
	public static ArrayList<HashMap<String, Object>> array=new ArrayList<HashMap<String, Object>>();
	public static boolean  all=false;
	public static String  productClassId1;
	public static final String PARTNER = "2088212688715226";
	public static int ordertype;
	// 商户收款账号
	public static final String SELLER = "2088212688715226";
	public static String APP_ID="wx2fbb20c41b2d3e3d";
	public static String webview="wx2fbb20c41b2d3e3d";
	public static String  citystr;
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJpI9JTl2vSkE66AK7Xudq9FR4Ee/ExuOyjjVLjlxtWehzeAXdFLLKvLeF9sX+wyltHTBHBgy1yYT1B6lfRRsFscc1xrsaRv+p9DBtYelQCcU8JKTz854nEEkkaRDTuP34ua6Bvd4RWfiJCwf5YZqGVO6WbDVYwnbG3z9O5w2EkZAgMBAAECgYBYqtTlPbQ4JJOrFb4JMKLE1+vgKQ6WXkNhnER0fplm61SKcRt5zNo/YRAsxvvZorlKnRtP+lDlRiNO/SRw5QGUdqNkTRVPHaycJxVrU4N5nNUTcGmT+BDgkN60TmSEp6PwmyjILWWvF8xSK63EB3178au1EQUloa997KA8pP5gAQJBAMvkaTPCWsu88QGIENCIJLflyjygWnwf7enbnssnVUmGawQq2/JwlXSMR3xczaATxFLoI2n63XE/18p/cUv4inkCQQDBtwE+gNKJ88WcugYPTxueS0aIz0WEvcb0AMnU9Fe6b5UtFAKmnVR3k9EyZXMR0mBIg4BFmdJV5QfxI21yxguhAkEAv78aFLwl6U7SFE+jCInQhkWLkMJ49hyNAQ4yYvmlopTQFcY8vN6WH1pBbDpWgsG0MwfvLh7nnDafOOfrY7fkeQJAPfQpkbPhof001ksoZP0H+Dha9qZTtYB8gM0/v7vl0tqc+y3LKz5mJVhBL70CcFC4OnjULW5kttPpFlYeSvbMAQJAJZIOb37CFJrsArd4btBP4O4sF99lIFfBM7WVm69WAAolzm1JP4j+adMhrxgRD5FOJurAZdhayCYFJMvoa6mjZg==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
