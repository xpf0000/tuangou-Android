package com.X.server;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;

import java.io.IOException;
import java.util.List;

import Decoder.BASE64Decoder;

/**
 * HTTP请求库，使用httpClient模拟get POST 文件上传等
 * @author Create By   2014.12.02
 *
 */
public class HttpRequest {

	
	/**
	 * GET方法  type 0: 返回字符串 1: 返回byte[]
	 * @param url 请求URL 已设置编码为UTF-8 可以直接传递中文 无需转码
	 * @return 字符串  
	 */
	 public <T> T doGet(String url,int type){
		 CloseableHttpResponse httpReponse=null;
		 T result=null;

         return result;
 } 
	
	 /**
	  * Post方法  中文无需转码
	  * @param url  请求路径
	  * @param params  传递参数
	  * List<NameValuePair> params=new ArrayList<>(NameValuePair);
	  * params.add(new BasicNameValuePair("userName", "xpf1234"));
	  * 
	  * @return 字符串
	  */
	 public String doPost(String url,List<Object> params){

		 CloseableHttpResponse httpReponse=null;
		 HttpPostHC4 httpPost=null;
		 String result="";

         return result;
 } 
	 

	/**
	 * base64 解码
	 * @param imageData 文件数据流
	 * @return byte[]数据
	 * @throws java.io.IOException
	 */
		public byte[] decode(String imageData) throws IOException{ 
		BASE64Decoder decoder = new BASE64Decoder(); 
		byte[] data = decoder.decodeBuffer(imageData); 
		for(int i=0;i<data.length;++i) 
		{ 
		if(data[i]<0) 
		{ 
		//调整异常数据 
		data[i]+=256; 
		} 
		} 
		// 
		return data; 
		} 
	
		
//		public static void main(String[] args) 
//		{
//			//System.out.println(doGet("http://192.168.2.113/test.php?a=今天天气不错"));
//			HttpRequest temp=new HttpRequest();
////			Header[] header=temp.getGetHead(Constant.url+"test/t");
////			for(Header header2 : header)
////			{
////				System.out.println(header2.getName()+" | "+header2.getValue() );
////			}
//			
//	        String url = "http://api.rexian.cn/login/UserLogin";
//	      
//	        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//	        params.add(new BasicNameValuePair("userName", "xpf1234"));
//	        params.add(new BasicNameValuePair("password", "000000"));
//	        params.add(new BasicNameValuePair("redirect_uri",
//					"http://http://www.luoyang.rexian.cn/"));
//	        params.add(new BasicNameValuePair("client_id", "HK_Guest"));
//	        params.add(new BasicNameValuePair("response_type", "token"));
//	        params.add(new BasicNameValuePair("scope", "Guest"));
//	        
//	        for(Header header: temp.getPostHead(url, params))
//	        {
//	        	//System.out.println(header.getName()+" | "+header.getValue());
//	        }
//			
//			
//		}
	
}
