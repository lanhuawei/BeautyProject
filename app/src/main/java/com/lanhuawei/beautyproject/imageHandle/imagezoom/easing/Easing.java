package com.lanhuawei.beautyproject.imageHandle.imagezoom.easing;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 主要的缓动函数
 */

public interface Easing {
    double easeOut(double time, double start, double end, double duration);

    double easeIn(double time, double start, double end, double duration);

    double easeInOut(double time, double start, double end, double duration);
}
