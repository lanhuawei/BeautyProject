package com.lanhuawei.beautyproject.appBase;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.alibaba.wxlib.util.SysUtil;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.receiver.NetworkChangeReceiver;
import com.lanhuawei.beautyproject.util.SharePreferenceUtil;
import com.lanhuawei.beautyproject.util.SystemTools;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.LinkedList;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/6.
 * 未完
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getName();
    private static MyApplication instance;
    private static DisplayMetrics dm = new DisplayMetrics();
    // 存储生成的每个activity 以便对activity的管理 用于退出程序调用
    private LinkedList<BaseActivity> activities = new LinkedList<>();
    //    高德地图
    private AMapLocationClient mAMapLocationClient = null;
    private AMapLocationClientOption mAMapLocationClientOption = null;
    //    监听网络改变
    private NetworkChangeReceiver mNetworkChangeReceiver = null;//
    private Context mContext = null;
    SharePreferenceUtil mSharePreferenceUtil = null;//数据库工具类

    //    消息
    private MessageReceiver mMessageReceiver;
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String MESSAGE_RECEIVED_ACTION = "com.dfhon.MESSAGE_RECEIVED_ACTION";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        mSharePreferenceUtil = new SharePreferenceUtil(mContext);
        //     必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存;
        //     千牛
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }

        RegisterNetWorkChangeReceiver(new NetworkChangeReceiver());// 注册网络状态/初始化网络状态
//        获取当前进程名
        String processName = SystemTools.getProcessName(this, Process.myPid());
        String packageName = mContext.getPackageName();
        if (processName != null) {
            boolean defaultProcess = processName.equals(packageName);
            if (defaultProcess) {
                //必要的初始化资源操作
                ExceptionHandler.getInstance().init(mContext);// 初始化捕获异常类
                PhoneMessage.getPhotoInfo(mContext);// 初始化手机信息
//                RegisterMessageReceiver();
                UniversalImageLoadTool.initImageLoader(mContext);


            }
        }


    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.unregisterReceiver(mMessageReceiver);
        unRegisterNetWorkReceiver(mNetworkChangeReceiver);

    }

    /**
     * 注册网络监听
     * @param networkChangeReceiver
     */
    private void RegisterNetWorkChangeReceiver(NetworkChangeReceiver networkChangeReceiver) {
        this.mNetworkChangeReceiver = networkChangeReceiver;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetworkChangeReceiver.intentFilter);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    /**
     * 取消网络监听
     */
    private void unRegisterNetWorkReceiver(NetworkChangeReceiver networkChangeReceiver) {
        this.unregisterReceiver(networkChangeReceiver);
    }



    /**
     * 注册消息监听
     */
    public void RegisterMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, intentFilter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String message = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + ":" + message + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + ":" + extras + "\n");
                }
            }

        }
    }

    /**
     * 将新生成的activity放到栈里面以便管理
     * @param baseActivity
     */
    public void pushActivity(BaseActivity baseActivity) {
        activities.add(baseActivity);
    }

    /**
     * 该activity结束的时候移除该activity
     * @param baseActivity
     */
    public void removeActivity(BaseActivity baseActivity) {
        activities.remove(baseActivity);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return dm;
    }
}

