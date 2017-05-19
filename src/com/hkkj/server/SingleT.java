package com.hkkj.server;

import android.annotation.SuppressLint;

/**
 * java 单例模式
 * @author 
 *
 */
public class SingleT {

    @SuppressLint("SdCardPath")
	private String FileDir="/data/data/com.hkkj.csrx.activity/cache/";   //存储图片缓存的文件夹
    private Boolean showImage=true;  //是否显示图片

	 private SingleT() { }

	 private static class SingletonHolder {
	      private final static SingleT INSTANCE = new SingleT();
	   }

	 public static SingleT getInstance() {
	      return SingletonHolder.INSTANCE;
	   }


    public String getFileDir() {
        return FileDir;
    }

    public void setFileDir(String fileDir) {
        FileDir = fileDir;
    }

    public Boolean getShowImage() {
        return showImage;
    }

    public void setShowImage(Boolean showImage) {
        this.showImage = showImage;
    }
}
