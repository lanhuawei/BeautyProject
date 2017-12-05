package com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.AlbumInfo;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/10.
 * 选择照片时的相册文件夹适配器
 */

public class PhotoFolderAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<AlbumInfo> mAlbumInfos;
    private ViewHolder mViewHolder;

    public PhotoFolderAdapter(Context context, List<AlbumInfo> albumInfos) {
        mLayoutInflater = LayoutInflater.from(context);
        mAlbumInfos = albumInfos;
    }

    @Override
    public int getCount() {
        return mAlbumInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mAlbumInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.adapter_photo_folder, null);
            mViewHolder.mImageView = (ImageView) view.findViewById(R.id.imageView);
            mViewHolder.mTextView = (TextView) view.findViewById(R.id.info);
            mViewHolder.number = (TextView) view.findViewById(R.id.num);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        final AlbumInfo albumInfo = mAlbumInfos.get(i);
        UniversalImageLoadTool.disPlay(
                ThumbnailsUtil.MapGetHashValue(albumInfo.getImage_id(), albumInfo.getPath_file()),
                new RotateImageViewAware(mViewHolder.mImageView, albumInfo.getPath_absolute()), R.color.default_pic);
        mViewHolder.mTextView.setText(albumInfo.getAlbum_name());
        LogUtil.e("Name", albumInfo.getAlbum_name());
        mViewHolder.number.setText("(" + mAlbumInfos.get(i).getPhotoInfoList().size() + "张)");
        return view;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public TextView number;
    }

}
