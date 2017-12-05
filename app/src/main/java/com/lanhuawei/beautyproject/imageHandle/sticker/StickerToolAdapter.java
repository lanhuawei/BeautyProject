package com.lanhuawei.beautyproject.imageHandle.sticker;

import android.content.Context;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.adapter.rvadapter.RvCommonAdapter;
import com.lanhuawei.beautyproject.adapter.rvadapter.ViewHolder;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.imageHandle.sticker.bean.Addon;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 贴纸预览适配器
 */

public class StickerToolAdapter extends RvCommonAdapter<Addon> {
    public StickerToolAdapter(Context context, List<Addon> datas) {
        super(context, datas, R.layout.adapter_sticker_tool);
    }
    @Override
    protected void convert(ViewHolder holder, Addon addon, int position) {
        ImageView iv_image = holder.getView(R.id.iv_image);
        UniversalImageLoadTool.disPlay("drawable://" + addon.getId(), new RotateImageViewAware(iv_image, "drawable://" + addon.getId()), R.color.bg_black);
    }
}
