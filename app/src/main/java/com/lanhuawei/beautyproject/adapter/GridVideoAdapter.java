package com.lanhuawei.beautyproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.util.DimensionUtil;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/22.
 * MyGirdView   Video的适配器
 */

public class GridVideoAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoInfo> mVideoInfos;
    private int width;
    private String from;


    public GridVideoAdapter(Context context, List<VideoInfo> videoInfos) {
        mContext = context;
        mVideoInfos = videoInfos;
        this.width = (PhoneMessage.ScreenWidth - DimensionUtil.dp2px(mContext, 20)) / 3;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adapter_grid_image_layout, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_row_gridView_imageView);
            viewHolder.iv_cancle = (ImageView) convertView.findViewById(R.id.iv_lable_cancel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                DimensionUtil.dp2px(mContext, 110), DimensionUtil.dp2px(mContext, 110));
        convertView.setLayoutParams(layoutParams);
        viewHolder.iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoInfos.remove(position);
                SelectVideoActivity.hasList.remove(position);
                notifyDataSetChanged();
            }
        });
        LogUtil.e("position", String.valueOf(position));
        if (position == mVideoInfos.size()) {
            viewHolder.mImageView.setImageResource(R.mipmap.add_video);
            viewHolder.iv_cancle.setVisibility(View.GONE);

            if (position == 1) {
                viewHolder.mImageView.setVisibility(View.GONE);
            }
        } else {
            UniversalImageLoadTool.disPlay(mVideoInfos.get(position).getVideo_path_file(),
                    new RotateImageViewAware(viewHolder.mImageView, mVideoInfos.get(position).getVideo_path_absolute()), R.color.default_pic);
            viewHolder.iv_cancle.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        private ImageView mImageView;
        private ImageView iv_cancle;
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
        return mVideoInfos.size() >= 1 ? 1 : (mVideoInfos.size() + 1);
    }
}
