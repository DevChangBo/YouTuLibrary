package com.example.youtulibrary.common;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ================================================================
 * 创建时间：2018/6/11 10:34
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class FileUtil {
    private static final  String TAG = "FileUtil";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = "youtu";

    private static String initPath(){
        if(storagePath.equals("")){
            storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdir();
            }
        }
        return storagePath;
    }

    public static String saveBitmap(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpegName;
    }
}
