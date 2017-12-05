package com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 图片裁剪和贴签大图
 */

public class PhotoCropAndStickerViewAdapter extends PagerAdapter {
    private Context mContext;
    private List<PhotoInfo> mPhotoInfos;

    public PhotoCropAndStickerViewAdapter(Context context, List<PhotoInfo> photoInfos) {
        mContext = context;
        mPhotoInfos = photoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        mPhotoInfos = photoInfos;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        return mPhotoInfos.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        if (view != null) {
            container.removeView(view);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        UniversalImageLoadTool.disPlayTrue(mPhotoInfos.get(position).getPath_file(), imageView, R.color.bg_black);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        container.addView(imageView);
        return imageView;
    }
}
