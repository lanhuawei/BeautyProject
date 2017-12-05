package com.lanhuawei.beautyproject.imageHandle.localPhoto.util;

import android.annotation.SuppressLint;

import java.util.HashMap;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/10.
 * 缩略图工具类
 * 保存缩略图的的绝对路径
 * PhotoFolderAdapter用到
 */

public class ThumbnailsUtil {
    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, String> sIntegerStringHashMap = new HashMap<>();

    public static String MapGetHashValue(int key, String defalt) {
        if (sIntegerStringHashMap == null || !sIntegerStringHashMap.containsKey(key)) {//控制显示了缩略图
            return defalt;
        }
        return sIntegerStringHashMap.get(key);
    }

    public static void put(int key, String value) {
        sIntegerStringHashMap.put(key, value);
    }

    public static void clear() {
        sIntegerStringHashMap.clear();
    }

}
