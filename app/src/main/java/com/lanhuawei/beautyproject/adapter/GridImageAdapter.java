package com.lanhuawei.beautyproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.SelectPhotoActivity;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware.RotateImageViewAware;
import com.lanhuawei.beautyproject.util.DimensionUtil;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * MyGirdView的适配器
 */

public class GridImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoInfo> mPhotoInfoList;
    private List<VideoInfo> mVideoInfoList;
    private int width;
    private String from;
    private int[] resId = new int[]{R.mipmap.add_video, R.mipmap.add_pic_new};

    public GridImageAdapter(Context context, List<PhotoInfo> photoInfoList) {
        mContext = context;
        mPhotoInfoList = photoInfoList;
        this.width = (PhoneMessage.ScreenWidth - DimensionUtil.dp2px(mContext, 20)) / 3;
    }

    public GridImageAdapter(Context mContext, List<PhotoInfo> mPhotoInfoList, List<VideoInfo> mVideoInfoList) {
        this.mContext = mContext;
        this.mPhotoInfoList = mPhotoInfoList;
        this.mVideoInfoList = mVideoInfoList;
        this.width = (PhoneMessage.ScreenWidth - DimensionUtil.dp2px(mContext, 20)) / 3;
    }

    public GridImageAdapter(Context context, List<PhotoInfo> photoInfoList, String from) {
        mContext = context;
        mPhotoInfoList = photoInfoList;
        this.from = from;
        this.width = (PhoneMessage.ScreenWidth - DimensionUtil.dp2px(mContext, 20)) / 3;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.adapter_grid_image_layout, null);
            holder.mImageView = (ImageView) view.findViewById(R.id.iv_row_gridView_imageView);
            holder.iv_cancle = (ImageView) view.findViewById(R.id.iv_lable_cancel);
            holder.iv_videoPlay = (ImageView) view.findViewById(R.id.video_play);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                DimensionUtil.dp2px(mContext, 110), DimensionUtil.dp2px(mContext, 110));
        view.setLayoutParams(layoutParams);

        final int realPosition = i - 1;

        if (mPhotoInfoList.size() == 0 && mVideoInfoList.size() == 0) {
            holder.mImageView.setImageResource(resId[i]);
            holder.iv_cancle.setVisibility(View.GONE);
        }
        if (mVideoInfoList.size() == 0 && mPhotoInfoList.size() > 0) {
//            LogUtil.e("Test", "test1");
            holder.iv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPhotoInfoList.remove(realPosition);
                    SelectPhotoActivity.hasList.remove(realPosition);
                    notifyDataSetChanged();
                }
            });

            if (i == mPhotoInfoList.size() + 1) {
//                LogUtil.e("Test", "test");
                holder.mImageView.setImageResource(R.mipmap.add_pic_new);
                holder.iv_cancle.setVisibility(View.GONE);
                if (i == 10) {
                    holder.mImageView.setVisibility(View.GONE);
                }
            }

            else {
                if (i == 0) {
                    holder.mImageView.setImageResource(R.mipmap.add_video);
                    holder.iv_cancle.setVisibility(View.GONE);
                }
                else {
                    LogUtil.e("i", String.valueOf(i));
//                    int realPosition = i - 1;
                    UniversalImageLoadTool.disPlay(mPhotoInfoList.get(realPosition).getPath_file(),
                            new RotateImageViewAware(holder.mImageView, mPhotoInfoList.get(realPosition).getPath_absolute()),
                            R.color.default_pic);
                    holder.iv_cancle.setVisibility(View.VISIBLE);
                }

            }
        }
        if (mVideoInfoList.size() == 1 && mPhotoInfoList.size() >= 0) {
            LogUtil.e("3", "test1");
            if (i == mPhotoInfoList.size() + 1) {
                holder.mImageView.setImageResource(R.mipmap.add_pic_new);
                holder.iv_cancle.setVisibility(View.GONE);
                LogUtil.e("3", "test2");
                if (i == 10) {
                    holder.mImageView.setVisibility(View.GONE);
                }
            } else {
                if (i == 0) {
                    UniversalImageLoadTool.disPlay(mVideoInfoList.get(0).getVideo_path_file(),
                            new RotateImageViewAware(holder.mImageView, mVideoInfoList.get(0).getVideo_path_absolute()), R.color.default_pic);
                    holder.iv_cancle.setVisibility(View.VISIBLE);
                    holder.iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mVideoInfoList.remove(0);
                            SelectVideoActivity.hasList.remove(0);
                            notifyDataSetChanged();
                        }
                    });
                    holder.iv_videoPlay.setVisibility(View.VISIBLE);
                    LogUtil.e("3", "test3");
                } else {
                    holder.iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPhotoInfoList.remove(realPosition);
                            SelectPhotoActivity.hasList.remove(realPosition);
                            notifyDataSetChanged();
                        }
                    });
                    LogUtil.e("3", "test4");
//                    int realPosition = i - 1;
                    UniversalImageLoadTool.disPlay(mPhotoInfoList.get(realPosition).getPath_file(),
                            new RotateImageViewAware(holder.mImageView, mPhotoInfoList.get(realPosition).getPath_absolute()),
                            R.color.default_pic);
                    holder.iv_cancle.setVisibility(View.VISIBLE);
                }
            }

        }

        return view;

    }

    private class ViewHolder {
        private ImageView mImageView;
        private ImageView iv_cancle;
        private ImageView iv_videoPlay;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return mPhotoInfoList.size() >= 9 ? 10 : (mPhotoInfoList.size() + 2);
    }

}
