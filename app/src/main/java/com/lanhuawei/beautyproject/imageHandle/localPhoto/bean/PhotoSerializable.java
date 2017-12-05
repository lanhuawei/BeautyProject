package com.lanhuawei.beautyproject.imageHandle.localPhoto.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/13.
 * 将照片序列化
 */

public class PhotoSerializable implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PhotoInfo> mPhotoInfos;

    public List<PhotoInfo> getPhotoInfos() {
        return mPhotoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        mPhotoInfos = photoInfos;
    }
}
