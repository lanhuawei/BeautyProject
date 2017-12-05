package com.lanhuawei.beautyproject.videoHandle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频序列化
 */

public class VideoSerializable implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<VideoInfo> mVideoInfos;

    public void setVideoInfos(List<VideoInfo> videoInfos) {
        mVideoInfos = videoInfos;
    }

    public List<VideoInfo> getVideoInfos() {
        return mVideoInfos;
    }
}
