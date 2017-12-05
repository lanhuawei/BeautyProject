package com.lanhuawei.beautyproject.imageHandle.sticker.bean;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 本地简单使用,实际项目中与贴纸相关的属性可以添加到此类中
 */

public class Addon {
    private int    id;
    public Addon(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
