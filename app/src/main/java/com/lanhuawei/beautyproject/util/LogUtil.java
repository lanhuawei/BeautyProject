package com.lanhuawei.beautyproject.util;

import android.text.TextUtils;
import android.util.Log;

import com.lanhuawei.beautyproject.BuildConfig;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/7.
 * 日志统一管理类
 */

public class LogUtil {
    private static boolean LOG_DEBUG = BuildConfig.DEBUG;

    public static int v(String tag, String msg) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                return Log.v(tag, msg);
            }
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)){
                return Log.d(tag, msg);
            }
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                return Log.i(tag, msg);
            }
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                return Log.w(tag, msg);
            }
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                return Log.e(tag, msg);
            }
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable throwable) {
        if (LOG_DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                return Log.e(tag, msg, throwable);
            }
        }
        return 0;
    }
}
