package com.lanhuawei.beautyproject.videoHandle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频文件显示适配器
 */

public class VideoAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<VideoInfo> mVideoInfos;
    private GridView mGridView;
    private ViewHolder mViewHolder;
    private int width;

    public VideoAdapter(Context context, List<VideoInfo> videoInfos, GridView gridView) {
        mLayoutInflater = LayoutInflater.from(context);
        mVideoInfos = videoInfos;
        mGridView = gridView;
        width = PhoneMessage.ScreenWidth / 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adapter_video, null);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.video_image_view);
            mViewHolder.selectImage = (ImageView) convertView.findViewById(R.id.video_select_image);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        VideoInfo videoInfo = mVideoInfos.get(position);
        ViewGroup.LayoutParams layoutParams = mViewHolder.mImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        mViewHolder.mImageView.setLayoutParams(layoutParams);
        if (videoInfo != null) {
            UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapGetHashValue(videoInfo.getVideo_id(), videoInfo.getVideo_path_file()),
                    new RotateImageViewAware(mViewHolder.mImageView, videoInfo.getVideo_path_absolute()), R.color.default_pic);
        }
        if (videoInfo.isChoose()) {
            mViewHolder.selectImage.setImageResource(R.mipmap.gou_selected);
        } else {
            mViewHolder.selectImage.setImageResource(R.mipmap.gou_normal);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView mImageView;
        ImageView selectImage;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mVideoInfos.get(position);
    }

    @Override
    public int getCount() {
        return mVideoInfos.size();
    }

    /**
     * 刷新View
     * @param newList
     * @param index
     */
    public void refreshView(List<VideoInfo> newList, int index) {
        this.mVideoInfos = newList;
        int visiblePos = mGridView.getFirstVisiblePosition();
        View view = mGridView.getChildAt(index - visiblePos);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (mVideoInfos.get(index).isChoose()) {
            holder.selectImage.setImageResource(R.mipmap.gou_selected);
        } else {
            holder.selectImage.setImageResource(R.mipmap.gou_normal);
        }
    }
}
