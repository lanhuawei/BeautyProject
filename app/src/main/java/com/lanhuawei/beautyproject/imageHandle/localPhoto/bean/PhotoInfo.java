package com.lanhuawei.beautyproject.imageHandle.localPhoto.bean;

import java.io.Serializable;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 本地相册图片bean
 * {@link #image_id}图片
 * {@link #path_absolute}绝对路径
 * {@link #choose}是否被选中
 */

public class PhotoInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int image_id;
    private String path_file;
    private String path_absolute;
    private boolean choose = false;
    private boolean isSelect = false;

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setPath_file(String path_file) {
        this.path_file = path_file;
    }

    public void setPath_absolute(String path_absolute) {
        this.path_absolute = path_absolute;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getPath_file() {
        return path_file;
    }

    public String getPath_absolute() {
        return path_absolute;
    }

    public boolean isChoose() {
        return choose;
    }

    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "image_id=" + image_id +
                ", path_file='" + path_file + '\'' +
                ", path_absolute='" + path_absolute + '\'' +
                ", choose=" + choose +
                ", isSelect=" + isSelect +
                '}';
    }
}
