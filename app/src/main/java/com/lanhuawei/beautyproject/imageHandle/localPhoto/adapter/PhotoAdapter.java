package com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/13.
 * 相册显示相片的适配器
 * PhotoFragment
 */

public class PhotoAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> mPhotoInfoList;
    private ViewHolder mViewHolder;
    private GridView mGridView;
    private int width;

    public PhotoAdapter(Context context, List<PhotoInfo> photoInfoList, GridView gridView) {
        mLayoutInflater = LayoutInflater.from(context);
        mPhotoInfoList = photoInfoList;
        mGridView = gridView;
        width = PhoneMessage.ScreenWidth / 3;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.adapter_photo, null);
            mViewHolder.mImageView = (ImageView) view.findViewById(R.id.imageView);
            mViewHolder.selectImage = (ImageView) view.findViewById(R.id.selectImage);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        PhotoInfo photoInfo = mPhotoInfoList.get(i);
        ViewGroup.LayoutParams layoutParams = mViewHolder.mImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        mViewHolder.mImageView.setLayoutParams(layoutParams);
        if (photoInfo != null) {
            UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapGetHashValue(photoInfo.getImage_id(), photoInfo.getPath_file()),
                    new RotateImageViewAware(mViewHolder.mImageView, photoInfo.getPath_absolute()), R.color.default_pic);
        }
        if (photoInfo.isChoose()) {
            mViewHolder.selectImage.setImageResource(R.mipmap.gou_selected);
        } else {
            mViewHolder.selectImage.setImageResource(R.mipmap.gou_normal);
        }
        return view;
    }

    /**
     * 刷新View
     * @param newList
     * @param index
     * 在PhotoFragment用到
     */
    public void refreshView(List<PhotoInfo> newList, int index) {
        this.mPhotoInfoList = newList;
        int visiblePos = mGridView.getFirstVisiblePosition();
        View view = mGridView.getChildAt(index - visiblePos);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (mPhotoInfoList.get(index).isChoose()) {
            holder.selectImage.setImageResource(R.mipmap.gou_selected);
        } else {
            holder.selectImage.setImageResource(R.mipmap.gou_normal);
        }
    }

    public class ViewHolder {
        public ImageView mImageView;
        public ImageView selectImage;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return mPhotoInfoList.get(i);
    }

    @Override
    public int getCount() {
        return mPhotoInfoList.size();
    }
}
