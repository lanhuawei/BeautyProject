package com.lanhuawei.beautyproject.imageHandle.sticker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanhuawei.beautyproject.adapter.rvadapter.MultiItemTypeAdapter;
import com.lanhuawei.beautyproject.entity.PhoneMessage;
import com.lanhuawei.beautyproject.imageHandle.imagezoom.ImageViewTouch;
import com.lanhuawei.beautyproject.imageHandle.sticker.bean.Addon;
import com.lanhuawei.beautyproject.imageHandle.sticker.customview.LabelView;
import com.lanhuawei.beautyproject.imageHandle.sticker.customview.MyHighlightView;
import com.lanhuawei.beautyproject.imageHandle.sticker.customview.MyImageViewDrawableOverlay;
import com.lanhuawei.beautyproject.imageHandle.sticker.util.EffectUtil;
import com.lanhuawei.beautyproject.imageHandle.sticker.util.FileUtils;
import com.lanhuawei.beautyproject.imageHandle.sticker.util.ImageUtils;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.TimeUtil;
import com.lanhuawei.beautyproject.view.LoadingDialogShow;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 照片贴纸类
 */

public class PhotoProcessActivity extends FragmentActivity implements View.OnClickListener {
    private Context mContext = PhotoProcessActivity.this;
    private RecyclerView rv_stickcer;
    private ImageView sticker_image;
    private ViewGroup drawArea;
    private MyImageViewDrawableOverlay mMyImageViewDrawableOverlay;
    private StickerToolAdapter mStickerToolAdapter;
    private int realImgShowWidth, realImgShowHeight;
    //当前图片
    private Bitmap mCurrentBitmap;
    private View overlay;
    private Dialog mProgressDlg;// 弹出等待对话框
    private int mImageLeft;
    private int mImageTop;
    private TextView tv_clear;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_process);
        EffectUtil.clear();
        initView();
        initEven();
        initStickerToolBar();
        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                if (result != null) {
                    mCurrentBitmap = result;
                    sticker_image.setImageBitmap(mCurrentBitmap);
                    sticker_image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                sticker_image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                sticker_image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            realImgShowWidth = sticker_image.getWidth();
                            realImgShowHeight = sticker_image.getHeight();
                            mImageLeft = sticker_image.getLeft();
                            mImageTop = sticker_image.getTop();
                        }
                    });
                } else {
                    Toast.makeText(mContext, "图片格式有误，请退出重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        sticker_image =(ImageView) findViewById(R.id.sticker_image);
        drawArea =(ViewGroup) findViewById(R.id.drawing_view_container);
        rv_stickcer =(RecyclerView) findViewById(R.id.rv_sticker);//贴纸的recyclerView
        tv_clear =(TextView) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
//        添加贴水印纸的画布
        overlay = LayoutInflater.from(mContext).inflate(R.layout.activity_photo_process_view_drawable_overlay, null);
        mMyImageViewDrawableOverlay =(MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(PhoneMessage.ScreenWidth, PhoneMessage.ScreenHeight);
        mMyImageViewDrawableOverlay.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);
    }

    private void initEven() {
        mMyImageViewDrawableOverlay.setOnDrawableEventListener(mOnDrawableEventListener);
        mMyImageViewDrawableOverlay.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                drawArea.postInvalidate();
            }
        });
    }

    /**
     * 初始化贴图相册
     */
    private void initStickerToolBar() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.HORIZONTAL);
        rv_stickcer.setLayoutManager(manager);
        mStickerToolAdapter = new StickerToolAdapter(mContext, EffectUtil.addonList);
        rv_stickcer.setAdapter(mStickerToolAdapter);
        mStickerToolAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (tv_clear.getVisibility() == View.GONE) {
                    tv_clear.setVisibility(View.VISIBLE);
                }
                Addon sticker = EffectUtil.addonList.get(position);
                int stickerSize = EffectUtil.getStickerSize();
                if (stickerSize < 9) {
                    EffectUtil.addStickerImage(mMyImageViewDrawableOverlay, mContext, sticker, new EffectUtil.StickerCallback() {
                        @Override
                        public void onRemoveSticker(Addon sticker) {
                            if (EffectUtil.getStickerSize() <= 0) {
                                if (tv_clear.getVisibility() == View.VISIBLE) {
                                    tv_clear.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "最多选择9张贴纸", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean OnItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_clear:
                EffectUtil.clearStickerImage(mMyImageViewDrawableOverlay);
                tv_clear.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                savePicture();
                break;
        }
    }

    /**
     * 保存图片
     */
    private void savePicture() {
//          这样设置会有黑边
        final Bitmap newBitmap = Bitmap.createBitmap(mMyImageViewDrawableOverlay.getWidth(), mMyImageViewDrawableOverlay.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(mImageLeft, mImageTop, mImageLeft + realImgShowWidth, mImageTop + realImgShowHeight);
        cv.drawBitmap(mCurrentBitmap, null, dst, null);
//        加贴纸水印
        EffectUtil.applyOnSave(cv, mMyImageViewDrawableOverlay);
//        加上这个去除黑边
        Bitmap bitmap = Bitmap.createBitmap(newBitmap, mImageLeft, mImageTop, realImgShowWidth, realImgShowHeight);
        new SavePicToFileTask().execute(bitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, String> {
        Bitmap mBitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            String fileName = null;
            mBitmap = bitmaps[0];
            String picName = TimeUtil.dtFormat(new Date(), "yyyyMMddHHmmss");
            try {
                fileName = ImageUtils.saveToFile(mContext, FileUtils.getInst().getSystemPhotoPath() + "/" + picName + ".jpg", false, mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "图片处理错误，请退出重试", Toast.LENGTH_SHORT).show();
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent();
            intent.putExtra("STICKER_IMAGE", s);
            setResult(RESULT_OK, intent);
            finish();
            dismissProgressDialog();
        }
    }

    public void showProgressDialog() {
        if (mProgressDlg == null) {
            mProgressDlg = LoadingDialogShow.loading(this, "处理中...", false);
            mProgressDlg.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            mProgressDlg.setCancelable(false);
        }
    }

    public void dismissProgressDialog() {
        if (null != mProgressDlg && mProgressDlg.isShowing()) {
            mProgressDlg.dismiss();
        }
    }

    private MyImageViewDrawableOverlay.OnDrawableEventListener mOnDrawableEventListener=new MyImageViewDrawableOverlay.OnDrawableEventListener() {
        @Override
        public void onFocusChange(MyHighlightView newFocus, MyHighlightView oldFocus) {

        }

        @Override
        public void onDown(MyHighlightView view) {

        }

        @Override
        public void onMove(MyHighlightView view) {

        }

        @Override
        public void onClick(MyHighlightView view) {

        }

        @Override
        public void onClick(LabelView label) {

        }
    };

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
