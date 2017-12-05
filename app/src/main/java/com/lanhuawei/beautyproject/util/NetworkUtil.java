package com.lanhuawei.beautyproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/7.
 * 判断网络类型 2G,3G,4G,WIFI
 * MyApplication 中调用即可 NetworkUtil.checkNetworkStatus(mContext);
 */

public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getName();

    /**
     * 网络类型为WIFI
     */
    public static final int NET_TYPE_WIFI = 1;
    /**
     * 网络类型为2G
     */
    public static final int NET_TYPE_2G = 2;
    /**
     * 网络类型为3G
     */
    public static final int NET_TYPE_3G = 3;
    /**
     * 网络类型为4G
     */
    public static final int NET_TYPE_4G = 4;
    /**
     * 未知网络类型
     */
    public static final int NET_TYPE_UNKNOW = -1;
    /**
     * 当前网络状态。默认-1
     */
    public static int CURRENT_NET_TYPE = NET_TYPE_UNKNOW;

    /**
     * 返回网络的状态
     * 1为成功WiFi已连接，2为2G，3为3G，4为4G， -1为网络未连接
     * @param context
     * @return
     */
    public static int CheckNetWorkStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return (CURRENT_NET_TYPE = NET_TYPE_WIFI);
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()) {
                    /** (2.5G)移动和联通,sdk1.0 */
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        /** (2.75G)2.5G到3G的过渡,移动和联通,sdk1.0 */
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        /** (2G) 电信,sdk1.6 */
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        /** (2G),sdk1.6 */
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        /** (2G),sdk2.2 */
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return (CURRENT_NET_TYPE = NET_TYPE_2G);//2G

                    /** (3G)联通,sdk1.0 */
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        /** (3G)电信,sdk1.6 */
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        /** (3.5G)属于3G过渡,sdk1.6 */
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        /** (3.5G),sdk2.0 */
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        /** (3.5G),sdk2.0 */
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        /** (3G)联通,sdk2.0 */
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        /** 3G-3.5G,sdk2.3 */
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        /** 3G(3G到4G的升级产物),sdk3.0 */
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        /** (3G),sdk3.2 */
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return (CURRENT_NET_TYPE = NET_TYPE_3G);//3G

                    /** (4G),sdk3.0 */
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return (CURRENT_NET_TYPE = NET_TYPE_4G);
                    /** sdk1.0 */
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return (CURRENT_NET_TYPE = NET_TYPE_UNKNOW);
                    default:
                        return (CURRENT_NET_TYPE = NET_TYPE_UNKNOW);
                }
            }
        } else {
            return (CURRENT_NET_TYPE = NET_TYPE_UNKNOW);
        }

    }

}
