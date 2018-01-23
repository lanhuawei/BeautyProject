package com.lanhuawei.beautyproject.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 拍照、取图工具类
 */

public class CameraUtil {
//    file:///storage/emulated/0/lanhuawei/Camera/
    public static String cameraDir = getSDCardPath() + "lanhuawei/Camera/";
    private static StringBuilder fileName = new StringBuilder();
    /**
     * 判断SD卡是否可用
     * @return
     */
    public static boolean sdCardIsExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得SD卡的路径
     * @return
     */
    private static String getSDCardPath() {
        if (sdCardIsExist()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
    }


    /**
     * 获取拍照的Intent android N 以下
     * @return
     */
    public static Intent getCameraIntent() {
        resetStringBuilder();
        fileName.append(getCurrentTime());
        fileName.append(".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        createDir(cameraDir);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraDir, fileName.toString())));
        return intent;


    }

    /**
     * 获取拍照的Intent android N 以上
     * 代码适配Android N以上版本。
     * 由于在Android7.0上,google使用了新的权限机制,所以导致在调用相机的时候,如果传递的URI为”file://”类型,的系统会抛出异常
     *
     */
    public static Intent androidNUriFromFile(Activity activity) {
        resetStringBuilder();
        fileName.append(getCurrentTime());
        fileName.append(".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        createDir(cameraDir);
        File file = new File(cameraDir, fileName.toString());
        Uri imageUri = FileProvider.getUriForFile(activity, "com.lanhuawei.beautyproject.fileProvider", file);
        Log.e("Uri", imageUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }


    /**
     * 重置fileName
     */
    private static void resetStringBuilder() {
        if (fileName.length() > 0) {
            fileName.delete(0, fileName.length());
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    private static CharSequence getCurrentTime() {
        return DateFormat.format("yyyy-MM-dd-hh-mm-ss", new Date());
    }

    /**
     * 创建文件夹
     * @param dirPath
     * @return
     */
    private static String createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    /**
     * 获取图片的真实路径
     * @return
     */
    public static String getRealFilePath() {
        return cameraDir + fileName.toString();
    }



}
