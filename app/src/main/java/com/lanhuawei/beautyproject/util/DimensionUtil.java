package com.lanhuawei.beautyproject.util;

import android.content.Context;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 单位之间的转换类
 * dp的单位转成为 px(像素)
 * px(像素)的单位转成为 dp
 */

public class DimensionUtil {
    /**
     * 根据手机的分辨率从 dp的单位转成为 px(像素)
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 05f);
    }
}
