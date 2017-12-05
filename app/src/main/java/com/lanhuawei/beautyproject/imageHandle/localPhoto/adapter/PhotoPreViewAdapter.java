package com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.adapter.rvadapter.RvCommonAdapter;
import com.lanhuawei.beautyproject.adapter.rvadapter.ViewHolder;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 图片裁剪和贴签预览图
 */

public abstract class PhotoPreViewAdapter extends RvCommonAdapter<PhotoInfo> {
    private List<PhotoInfo> mDatas;

    public PhotoPreViewAdapter(Context context, List<PhotoInfo> datas) {
        super(context, datas, R.layout.adapter_photo_pre_view);
    }

    @Override
    protected void convert(ViewHolder holder, PhotoInfo photoInfo, final int position) {
        ImageView iv_isSelect = holder.getView(R.id.iv_isSelect);
        ImageView iv_image = holder.getView(R.id.iv_image);
        ImageView iv_delete = holder.getView(R.id.iv_delete);

        if (photoInfo.isSelect()) {
            iv_isSelect.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_isSelect.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
        }
        UniversalImageLoadTool.disPlay(photoInfo.getPath_file(),
                new RotateImageViewAware(iv_image, photoInfo.getPath_absolute()), R.color.bg_black);

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(position);
            }
        });
    }

    public abstract void deleteImage(int position);
}
