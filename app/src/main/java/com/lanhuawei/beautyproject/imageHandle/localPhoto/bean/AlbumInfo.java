package com.lanhuawei.beautyproject.imageHandle.localPhoto.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 相册文件夹bean
 */

public class AlbumInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int image_id;
    private String path_absolute;//绝对路径
    private String path_file;//文件路径
    private String album_name;//文件夹名称
    private List<PhotoInfo> mPhotoInfoList;//照片

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setPath_absolute(String path_absolute) {
        this.path_absolute = path_absolute;
    }

    public void setPath_file(String path_file) {
        this.path_file = path_file;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public void setPhotoInfoList(List<PhotoInfo> photoInfoList) {
        mPhotoInfoList = photoInfoList;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getPath_absolute() {
        return path_absolute;
    }

    public String getPath_file() {
        return path_file;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public List<PhotoInfo> getPhotoInfoList() {
        return mPhotoInfoList;
    }
}
