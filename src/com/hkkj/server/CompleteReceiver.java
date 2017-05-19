package com.hkkj.server;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.hkkj.csrx.myview.MyPopwindows;

import java.io.File;

/**
 * Created by admins on 2016/7/23.
 */
public class CompleteReceiver extends BroadcastReceiver {
    private DownloadManager downloadManager;
    MyPopwindows myPopwindows;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);                                                                                      //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            int columnCount = cursor.getColumnCount();
            String path = null;                                                                                                                                       //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
            while (cursor.moveToNext()) {
                for (int j = 0; j < columnCount; j++) {
                    String columnName = cursor.getColumnName(j);
                    String string = cursor.getString(j);
                    if (columnName.equals("local_uri")) {
                        path = string;
                    }
                    if (path != null) {
                        path = path.substring(6);
                        File file = new File(path);
                        if (file.exists()) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        } else {
//                            Toast.makeText(context, "获取安装包失败", Toast.LENGTH_SHORT).show();
                        }

                    } else {
//                        Toast.makeText(context, "SD卡不可用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            cursor.close();
//            如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理                                                   if(path.startsWith("content:")) {
//            cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);
//            columnCount = cursor.getColumnCount();
//            while (cursor.moveToNext()) {
//                for (int j = 0; j < columnCount; j++) {
//                    String columnName = cursor.getColumnName(j);
//                    String string = cursor.getString(j);
//                    if (string != null) {
//                        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
//                        System.out.println(columnName + ": " + string);
//                    } else {
//                        System.out.println(columnName + ": null");
//                    }
//                }
//            }
//            cursor.close();
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Toast.makeText(context, "点击了....", Toast.LENGTH_LONG).show();
        }

    }
//    private void setapk(Context context,String finalPath){
//        Intent intent;
//        File file=new File(finalPath);
//        intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
}
