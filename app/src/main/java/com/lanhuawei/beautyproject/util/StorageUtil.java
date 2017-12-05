package com.lanhuawei.beautyproject.util;

import android.os.Environment;

import com.lanhuawei.beautyproject.appBase.AllConfig;

import java.io.File;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/8.
 * 存储工具类
 * 未完
 */

public class StorageUtil {

    /**
     * 外部储存目录,文件名
     */
    public static String sdDir = Environment.getExternalStorageDirectory().getPath();

    /**
     * 获取缓存路径
     * @return
     */
    public static File getImgDirector() {
        File saveDir = null;
        saveDir = new File(sdDir, AllConfig.cacheDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }
}
