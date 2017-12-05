package com.lanhuawei.beautyproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lanhuawei.beautyproject.util.NetworkUtil;

/**
 * Created by Ivan.L LanHuaWei
 * 监听网络的状态改变广播,用NetworkUtil判断网络类型
 * on 2017/11/7.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
//    在极光推送中接收  cn.jpush.android.service.PushReceiver
    public static final String intentFilter = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            NetworkUtil.CheckNetWorkStatus(context);
        }
    }
}
