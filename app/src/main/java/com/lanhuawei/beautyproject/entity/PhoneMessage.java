package com.lanhuawei.beautyproject.entity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.SystemTools;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/8.
 * 手机信息bean
 * 未完
 */

public class PhoneMessage {
    private static final String TAG = PhoneMessage.class.getName();

    /**
     * 手机屏幕的宽
     */
    public static int ScreenWidth;
    /**
     * 手机屏幕的高
     */
    public static int ScreenHeight;
    /**
     * 手机状态栏的高
     */
    public static int ScreenStatusHeight;

    /**
     * 手机屏幕密度
     */
    public static float density;

    public static float densityDpi;

    /**
     * 手机厂商
     */
    public static String productor = "";
    /**
     * 手机型号
     */
    public static String model = "";
    /**
     * 手机IMEI
     */
    public static String imei = "";
    /**
     * 设备版本号（platform）
     */
    public static String platform_version = "";
    /**
     * API Level
     */
    public static int sdk_version;

    /**
     * app版本号/显示给用户，可以写1.1.0 , 1.2.0等等版本
     */
    public static String versionName = "";
    /**
     * app版本号/设备程序识别版本(升级)用的
     */
    public static int versionCode;

    /**
     * channel_id
     */
    public static String channel_id = "";

    /**
     * 初始化手机信息
     * @param context
     */
    public static void getPhotoInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        PhoneMessage.ScreenWidth = displayMetrics.widthPixels;
        PhoneMessage.ScreenHeight = displayMetrics.heightPixels;
        PhoneMessage.density = displayMetrics.density;
        PhoneMessage.densityDpi = displayMetrics.densityDpi;
        getSysInfo(context);//获得手机信息
        getAppInfo(context);//获取所装应用版本号
        channel_id = SystemTools.getUmengChannel(context);//获取友盟渠道
        LogUtil.w(TAG, "ScreenWidth=" + PhoneMessage.ScreenWidth
                + ",ScreenHeight=" + PhoneMessage.ScreenHeight + ",density="
                + PhoneMessage.density + ",densityDpi="
                + PhoneMessage.densityDpi);

    }

    /**
     * 获得手机信息
     * @param context
     */
    public static void getSysInfo(Context context) {
//        PhoneMessage.imei = getImei(context);
        PhoneMessage.productor = Build.MANUFACTURER;
        PhoneMessage.model = Build.MODEL;
        PhoneMessage.platform_version = Build.VERSION.RELEASE;
        PhoneMessage.sdk_version = Build.VERSION.SDK_INT;
        LogUtil.d(TAG, "imei=" + PhoneMessage.imei + ",model="
                + PhoneMessage.model + ",productor=" + PhoneMessage.productor
                + ",platform_version=" + PhoneMessage.platform_version
                + ",sdk_version=" + PhoneMessage.sdk_version);
    }

    /**
     * 获取所装应用版本号
     * @param context
     */
    public static void getAppInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            PhoneMessage.versionCode = info.versionCode;
            PhoneMessage.versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "versionName=" + PhoneMessage.versionName
                + ",versionCode=" + PhoneMessage.versionCode);
    }

    /**
     * 获取手机设备号IMEI
     * @param context
     * @return  MD5校验值（32位字符串）
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mImei = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            mImei = telephonyManager.getDeviceId();
        } else {
            mImei = telephonyManager.getDeviceId();
        }
        if (TextUtils.isEmpty(mImei)) {
            mImei = "0000000000000000";
        }
        return mImei;
    }

}
