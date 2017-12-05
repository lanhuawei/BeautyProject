package com.lanhuawei.beautyproject.videoHandle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoAlbumInfo;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频文件夹适配器
 *
 */

public class VideoFolderAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<VideoAlbumInfo> mVideoAlbumInfos;
    private ViewHolder mViewHolder;

    public VideoFolderAdapter(Context context, List<VideoAlbumInfo> videoAlbumInfos) {
        mLayoutInflater = LayoutInflater.from(context);
        mVideoAlbumInfos = videoAlbumInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adapter_video_folder, null);
            mViewHolder.video_mImageView = (ImageView) convertView.findViewById(R.id.video_imageView);
            mViewHolder.video_info = (TextView) convertView.findViewById(R.id.video_info);
            mViewHolder.video_num = (TextView) convertView.findViewById(R.id.video_num);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final VideoAlbumInfo videoAlbumInfo = mVideoAlbumInfos.get(position);
        UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapGetHashValue(videoAlbumInfo.getVideo_id(), videoAlbumInfo.getVideo_path_file()),
                new RotateImageViewAware(mViewHolder.video_mImageView, videoAlbumInfo.getVideo_path_absolute()), R.color.default_pic);
        mViewHolder.video_info.setText(videoAlbumInfo.getVideo_folder_name());
        mViewHolder.video_num.setText("(" + mVideoAlbumInfos.get(position).getVideoInfos().size() + "个)");
        return convertView;
    }

    public class ViewHolder {
        public ImageView video_mImageView;
        public TextView video_info;
        public TextView video_num;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mVideoAlbumInfos.get(position);
    }

    @Override
    public int getCount() {
        return mVideoAlbumInfos.size();
    }
}
