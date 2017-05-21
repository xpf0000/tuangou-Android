package com.X.tcbj.utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

@SuppressLint("SdCardPath")
public class AsyncImageLoadersdcard  
{  
    /** 
     * 内存图片软引用缓�?
     */  
    private HashMap<String, SoftReference<Bitmap>> imageCache = null; 
      
    public AsyncImageLoadersdcard()  
    {  
        imageCache = new HashMap<String, SoftReference<Bitmap>>();  
    }  
      
    public Bitmap loadBitmap(final ImageView imageView, final String imageURL, final ImageCallBack2 imageCallBack)  
    {  
        if(imageCache.containsKey(imageURL))  
        {  
            SoftReference<Bitmap> reference = imageCache.get(imageURL);  
            Bitmap bitmap = reference.get();  
            if(bitmap != null)  
            {  
                return bitmap;  
            }  
        }  
        else  
        {  
            /** 
             * 加上�?��对本地缓存的查找 
             */  
            String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);  
            File cacheDir = new File("/data/data/com.hkkj.csrx.activity/cache/");  
            File[] cacheFiles = cacheDir.listFiles();  
            int i = 0;  
            if(null!=cacheFiles){
            for(; i<cacheFiles.length; i++)  
            {  
                if(bitmapName.equals(cacheFiles[i].getName()))  
                {  
                    break;  
                }  
            }  
              
            if(i < cacheFiles.length)  
            {  
                return BitmapFactory.decodeFile("/data/data/com.hkkj.csrx.activity/cache/" + bitmapName);  
            }
            }
        }  
        return null;  
    }  

    public interface ImageCallBack2  
    {  
        public void imageLoad(ImageView imageView, Bitmap bitmap);  
    }  
}