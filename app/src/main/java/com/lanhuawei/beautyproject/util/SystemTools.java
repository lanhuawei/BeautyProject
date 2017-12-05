package com.lanhuawei.beautyproject.util;

import android.app.ActivityManager;
import android.content.Context;

import com.umeng.analytics.AnalyticsConfig;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/8.
 * 常用工具方法
 * 未完
 */

public class SystemTools {




    /**
     * 获取友盟渠道
     * @param context
     * @return
     */
    public static String getUmengChannel(Context context) {
        return AnalyticsConfig.getChannel(context);
    }

    /**
     * 获取当期的进程名
     * @param context
     * @param pid
     * @return
     */
    public static String getProcessName(Context context, int pid) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
