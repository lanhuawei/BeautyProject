package com.lanhuawei.beautyproject.imageHandle.localPhoto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.lanhuawei.beautyproject.adapter.rvadapter.MultiItemTypeAdapter;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter.PhotoCropAndStickerViewAdapter;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter.PhotoPreViewAdapter;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.crop.ImageCropActivity;
import com.lanhuawei.beautyproject.imageHandle.sticker.PhotoProcessActivity;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/13.
 * 图片裁剪和贴纸类
 */

public class ImageCropAndStickerActivity extends FragmentActivity implements View.OnClickListener {
    private final static String TAG = ImageCropAndStickerActivity.class.getName();
    private Context mContext = ImageCropAndStickerActivity.this;
    private ViewPager mViewPager;
    private RecyclerView mRv_imageList;
    private List<PhotoInfo> mPhotoInfos = new ArrayList<>();

    private PhotoPreViewAdapter mPhotoPreViewAdapter;//缩略图
    private PhotoCropAndStickerViewAdapter mPhotoCropAndStickerViewAdapter;//大图
    private int mCurrentPosition = 0;
    private int TO_STICKER_CODE = 0x1456;//贴纸
    private int TO_CROP_CODE = 0x1457;//裁剪



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_core_and_sticker);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.vp_image);
        mRv_imageList = (RecyclerView) findViewById(R.id.rc_imageList);
        findViewById(R.id.tv_crop).setOnClickListener(this);//裁剪
        findViewById(R.id.tv_sticker).setOnClickListener(this);//贴纸

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.HORIZONTAL);
        mRv_imageList.setLayoutManager(manager);
    }

    private void initData() {
        mPhotoInfos.clear();
        for (int i = 0; i < SelectPhotoActivity.hasList.size(); i++) {
            PhotoInfo photoInfo = SelectPhotoActivity.hasList.get(i);
            PhotoInfo data = new PhotoInfo();
            data.setPath_file(photoInfo.getPath_file());
            data.setPath_absolute(photoInfo.getPath_absolute());
            data.setChoose(photoInfo.isChoose());
            if (i == 0) {
                data.setSelect(true);
            } else {
                data.setSelect(false);
            }
            mPhotoInfos.add(data);
        }
        mPhotoCropAndStickerViewAdapter = new PhotoCropAndStickerViewAdapter(mContext, mPhotoInfos);
        mViewPager.setAdapter(mPhotoCropAndStickerViewAdapter);
        mPhotoPreViewAdapter=new PhotoPreViewAdapter(mContext, mPhotoInfos) {
            @Override
            public void deleteImage(int position) {
                mPhotoInfos.remove(position);
                SelectPhotoActivity.hasList.remove(position);
                if (mPhotoInfos.size() == 0) {
                    finish();
                    return;
                }
                if (position == 0) {
                    mCurrentPosition = 0;
                } else {
                    mCurrentPosition = position - 1;
                }
                mPhotoInfos.get(mCurrentPosition).setSelect(true);
                mPhotoCropAndStickerViewAdapter.setPhotoInfos(mPhotoInfos);
                mViewPager.setCurrentItem(mCurrentPosition);
                mPhotoPreViewAdapter.notifyDataSetChanged();
            }
        };
        mRv_imageList.setAdapter(mPhotoPreViewAdapter);//缩略图
        mPhotoPreViewAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (int i = 0; i < mPhotoInfos.size(); i++) {
                    if (i == position) {
                        mPhotoInfos.get(i).setSelect(true);
                    } else {
                        mPhotoInfos.get(i).setSelect(false);
                    }
                }
                mViewPager.setCurrentItem(position);
                mCurrentPosition = position;
                mPhotoPreViewAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean OnItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRv_imageList.smoothScrollToPosition(position);
                int size = mPhotoInfos.size();
                for (int i = 0; i < size; i++) {
                    if (i == position) {
                        mPhotoInfos.get(i).setSelect(true);
                    } else {
                        mPhotoInfos.get(i).setSelect(false);
                    }
                }
                mCurrentPosition = position;
                mPhotoPreViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                SelectPhotoActivity.hasList.clear();
                SelectPhotoActivity.hasList.addAll(mPhotoInfos);
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.tv_back:
                finish();
                break;
//            裁剪
            case R.id.tv_crop:
                Intent cropIntent = new Intent(this, ImageCropActivity.class);
                cropIntent.setData(Uri.parse(mPhotoInfos.get(mCurrentPosition).getPath_file()));
                startActivityForResult(cropIntent, TO_CROP_CODE);
                break;
//            贴纸
            case R.id.tv_sticker:
                Intent stickerIntent = new Intent(this, PhotoProcessActivity.class);
                stickerIntent.setData(Uri.parse(mPhotoInfos.get(mCurrentPosition).getPath_file()));
                startActivityForResult(stickerIntent, TO_STICKER_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_STICKER_CODE && resultCode == RESULT_OK) {//贴纸
            String stickerFile = data.getStringExtra("STICKER_IMAGE");
            mPhotoInfos.get(mCurrentPosition).setPath_absolute(stickerFile);
            mPhotoInfos.get(mCurrentPosition).setPath_file("file://" + stickerFile);
            mViewPager.setCurrentItem(mCurrentPosition);
            mPhotoCropAndStickerViewAdapter.notifyDataSetChanged();
            mPhotoPreViewAdapter.notifyDataSetChanged();
        }
        if (requestCode == TO_CROP_CODE && resultCode == RESULT_OK) {//裁剪
            String cropFile = data.getStringExtra("CROP_IMAGE");

            LogUtil.e("CropFile", cropFile);

            mPhotoInfos.get(mCurrentPosition).setPath_absolute(cropFile);
            mPhotoInfos.get(mCurrentPosition).setPath_file("file://" + cropFile);
            mViewPager.setCurrentItem(mCurrentPosition);
            mPhotoCropAndStickerViewAdapter.notifyDataSetChanged();
            mPhotoPreViewAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
