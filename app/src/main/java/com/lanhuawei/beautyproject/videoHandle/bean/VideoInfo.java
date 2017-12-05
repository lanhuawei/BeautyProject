package com.lanhuawei.beautyproject.videoHandle.bean;

import java.io.Serializable;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 本地视频文件信息bean
 */

public class VideoInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int video_id;
    private String video_path_file;
    private String video_path_absolute;
    private boolean choose;
    private boolean isSelect;

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public void setVideo_path_file(String video_path_file) {
        this.video_path_file = video_path_file;
    }

    public void setVideo_path_absolute(String video_path_absolute) {
        this.video_path_absolute = video_path_absolute;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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

    public boolean isChoose() {
        return choose;
    }

    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "video_id=" + video_id +
                ", video_path_file='" + video_path_file + '\'' +
                ", video_path_absolute='" + video_path_absolute + '\'' +
                ", choose=" + choose +
                ", isSelect=" + isSelect +
                '}';
    }
}
