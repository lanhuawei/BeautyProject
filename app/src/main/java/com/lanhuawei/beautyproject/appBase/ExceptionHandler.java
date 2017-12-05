package com.lanhuawei.beautyproject.appBase;

import android.content.Context;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/8.
 * 应用全局异常捕获
 * 未完
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler instance = new ExceptionHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    /**
     * 保证只有一个实例
     */
    public ExceptionHandler() {
    }

    public static ExceptionHandler getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }
}
