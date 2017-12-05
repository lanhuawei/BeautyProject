package com.lanhuawei.beautyproject.videoHandle.bean;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频文件夹bean
 */

public class VideoAlbumInfo {
    private static final long serialVersionUID = 1L;
    private int video_id;
    private String video_path_file;
    private String video_path_absolute;
    private String video_folder_name;//视频文件夹名称
    private List<VideoInfo> mVideoInfos;

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public void setVideo_path_file(String video_path_file) {
        this.video_path_file = video_path_file;
    }

    public void setVideo_path_absolute(String video_path_absolute) {
        this.video_path_absolute = video_path_absolute;
    }

    public void setVideo_folder_name(String video_folder_name) {
        this.video_folder_name = video_folder_name;
    }

    public void setVideoInfos(List<VideoInfo> videoInfos) {
        mVideoInfos = videoInfos;
    }

    public int getVideo_id() {
        return video_id;
    }

    public String getVideo_path_file() {
        return video_path_file;
    }

    public String getVideo_path_absolute() {
        return video_path_absolute;
    }

    public String getVideo_folder_name() {
        return video_folder_name;
    }

    public List<VideoInfo> getVideoInfos() {
        return mVideoInfos;
    }

    @Override
    public String toString() {
        return "VideoAlbumInfo{" +
                "video_id=" + video_id +
                ", video_path_file='" + video_path_file + '\'' +
                ", video_path_absolute='" + video_path_absolute + '\'' +
                ", video_folder_name='" + video_folder_name + '\'' +
                ", mVideoInfos=" + mVideoInfos +
                '}';
    }
}
