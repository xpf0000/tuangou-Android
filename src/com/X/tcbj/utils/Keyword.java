package com.X.tcbj.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class Keyword {
	public static SpannableStringBuilder putstr(String keyword,String strtext){
		
		
		  String docInfo=strtext;

		int keywordIndex = strtext.indexOf(keyword);

	    SpannableStringBuilder style=new SpannableStringBuilder(docInfo);
	    while(keywordIndex!=-1)
	    {
	    	/**
	    	 *	背景色改变    	
	    	 */
//	      style.setSpan(new BackgroundColorSpan(Color.RED),keywordIndex,keywordIndex+keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    	/**
	    	 *	关键字颜色改变    	
	    	 */	      
	    	style.setSpan(new ForegroundColorSpan(Color.RED),keywordIndex,keywordIndex+keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	     
	    	int tempkeywordTempIndex = keywordIndex+keyword.length();
	    	strtext = docInfo.substring(tempkeywordTempIndex,docInfo.length());
	      keywordIndex = strtext.indexOf(keyword);
	      if(keywordIndex!=-1)
	      {
	        keywordIndex = keywordIndex+tempkeywordTempIndex;
	      }
	    }
	    
		return style;
		 
	}
}
