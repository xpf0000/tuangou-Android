package com.X.server;
import java.security.MessageDigest;
/**
 * MD5加密 
 * @author Create By  2014.12.02
 *
 */
public class MD5Util {
	/**
	 * MD5加密
	 * @param s  输入字符串
	 * @return 输出MD5加密字符串
	 */
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        System.out.println(MD5Util.MD5("20121221"));
        System.out.println(MD5Util.MD5("0.510000000000000020141014150320579455http://180.168.71.178:10090/payment/order-payment-test.htm?link=orderPayTestRsthttp://180.168.71.178:10090/payment/order-payment-test.htm?link=orderPayTestRst31234567"));
        
        System.out.println("C5D20C1CDACEB58D952B38018B0FFCA7");
    }
}