package com.lanhuawei.beautyproject.imageHandle.sticker.util;

import android.os.Environment;

import com.lanhuawei.beautyproject.appBase.MyApplication;

import java.io.File;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 文件工具类
 */

public class FileUtils {
    private static String BASE_PATH;
    private static String STICKER_BASE_PATH;

    private static FileUtils mInstance;

    public static FileUtils getInst() {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils();
                }
            }
        }
        return mInstance;
    }

    private FileUtils() {
        // initStickerPaht();
    }

    //自定义的贴图和裁剪路径
    public String getPhotoSavedPath() {
        return BASE_PATH + "stickercamera";
    }

    //系统图库路径
    public String getSystemPhotoPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    }

    private void initStickerPaht() {
        String sdcardState = Environment.getExternalStorageState();
        //如果没SD卡则放缓存
        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stickercamera/";
        } else {
            BASE_PATH = MyApplication.getInstance().getCacheDir().getAbsolutePath();
        }
        STICKER_BASE_PATH = BASE_PATH + "/stickers/";
    }


    public boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }
}
