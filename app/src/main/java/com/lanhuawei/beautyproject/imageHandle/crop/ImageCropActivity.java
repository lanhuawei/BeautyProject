package com.lanhuawei.beautyproject.imageHandle.crop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lanhuawei.beautyproject.imageHandle.sticker.util.FileUtils;
import com.lanhuawei.beautyproject.imageHandle.sticker.util.ImageUtils;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.TimeUtil;
import com.lanhuawei.beautyproject.view.LoadingDialogShow;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 图片裁剪页面
 */

public class ImageCropActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext = ImageCropActivity.this;
    private TextView tv_freedom, tv_one_than_one, tv_third_than_fourth, tv_fourth_than_third;
    private CropImageView mCropImageView;
    private Bitmap currentBitmap;
    private int aspectRatioX = 1;//纵横比x
    private int aspectRatioY = 1;//纵横比y
    private TextView tv_rest;
    private boolean isFixedAspectRatio = false;
    private Dialog mProgressDlg;// 弹出等待对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        initView();
        initCrop();
        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                if (result != null) {
                    currentBitmap = result;
                    mCropImageView.setImageBitmap(currentBitmap);
                } else {
                    Toast.makeText(mContext, "图片格式有误，请退出重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        tv_freedom = (TextView) findViewById(R.id.tv_freedom);
        tv_one_than_one = (TextView) findViewById(R.id.tv_one_than_one);
        tv_third_than_fourth = (TextView) findViewById(R.id.tv_third_than_fourth);
        tv_fourth_than_third = (TextView) findViewById(R.id.tv_fourth_than_third);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        tv_rest = (TextView) findViewById(R.id.tv_rest);
        findViewById(R.id.tv_confirm).setOnClickListener(this);

        tv_freedom.setOnClickListener(this);
        tv_one_than_one.setOnClickListener(this);
        tv_third_than_fourth.setOnClickListener(this);
        tv_fourth_than_third.setOnClickListener(this);
        tv_rest.setOnClickListener(this);
        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
        mCropImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tv_rest.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void initCrop() {
        mCropImageView.setFixedAspectRatio(isFixedAspectRatio);//固定长宽比
        mCropImageView.setGuidelines(0);//设置是引导线 0 无 1按下的时候  2 始终有
        mCropImageView.setAspectRatio(aspectRatioX,aspectRatioX);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm://完成:
                final Bitmap croppedImage = mCropImageView.getCroppedImage();
                new SavePicToFileTask().execute(croppedImage);
                break;
            case R.id.tv_rest://还原
                tv_rest.setVisibility(View.GONE);
                mCropImageView.setFixedAspectRatio(isFixedAspectRatio);//固定长宽比
                mCropImageView.setAspectRatio(aspectRatioX, aspectRatioX);
                mCropImageView.setSelected(false);
                mCropImageView.setSelected(true);
                break;
            case R.id.tv_freedom://自由裁剪:
                tv_freedom.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.freedom_select_icon, 0, 0);
                tv_freedom.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff507f));
                tv_one_than_one.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.one_one_icon, 0, 0);
                tv_one_than_one.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_third_than_fourth.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.third_fourth_icon, 0, 0);
                tv_third_than_fourth.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_fourth_than_third.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.fourth_third_icon, 0, 0);
                tv_fourth_than_third.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));

                isFixedAspectRatio = false;
                mCropImageView.setFixedAspectRatio(isFixedAspectRatio);
                mCropImageView.setAspectRatio(aspectRatioX, aspectRatioX);
                mCropImageView.setSelected(false);
                mCropImageView.setSelected(true);
                break;
            case R.id.tv_one_than_one:

                tv_freedom.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.freedom_select_icon, 0, 0);
                tv_freedom.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_one_than_one.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.one_one_icon, 0, 0);
                tv_one_than_one.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff507f));
                tv_third_than_fourth.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.third_fourth_icon, 0, 0);
                tv_third_than_fourth.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_fourth_than_third.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.fourth_third_icon, 0, 0);
                tv_fourth_than_third.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));

                isFixedAspectRatio = true;
                mCropImageView.setFixedAspectRatio(isFixedAspectRatio);//固定长宽比
                aspectRatioX = 1;
                aspectRatioY = 1;
                mCropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
                mCropImageView.setSelected(false);
                mCropImageView.setSelected(true);
                break;

            case R.id.tv_third_than_fourth:
                tv_freedom.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.freedom_select_icon, 0, 0);
                tv_freedom.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_one_than_one.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.one_one_icon, 0, 0);
                tv_one_than_one.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_third_than_fourth.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.third_fourth_icon, 0, 0);
                tv_third_than_fourth.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff507f));
                tv_fourth_than_third.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.fourth_third_icon, 0, 0);
                tv_fourth_than_third.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                isFixedAspectRatio = true;
                mCropImageView.setFixedAspectRatio(isFixedAspectRatio);//固定长宽比
                aspectRatioX = 3;
                aspectRatioY = 4;
                mCropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
                mCropImageView.setSelected(false);
                mCropImageView.setSelected(true);
                break;

            case R.id.tv_fourth_than_third:
                tv_freedom.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.freedom_select_icon, 0, 0);
                tv_freedom.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_one_than_one.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.one_one_icon, 0, 0);
                tv_one_than_one.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_third_than_fourth.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.third_fourth_icon, 0, 0);
                tv_third_than_fourth.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_fourth_than_third.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.fourth_third_icon, 0, 0);
                tv_fourth_than_third.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff507f));
                isFixedAspectRatio = true;
                mCropImageView.setFixedAspectRatio(isFixedAspectRatio);//固定长宽比
                aspectRatioX = 4;
                aspectRatioY = 3;
                mCropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
                mCropImageView.setSelected(false);
                mCropImageView.setSelected(true);
                break;

        }
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            String s = null;
            bitmap = bitmaps[0];
            String picName = TimeUtil.dtFormat(new Date(), "yyyyMMddHHmmss");
            try {
                s = ImageUtils.saveToFile(mContext, FileUtils.getInst().getSystemPhotoPath() + "/" + picName + ".jpg", false, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "图片处理错误，请退出重试", Toast.LENGTH_SHORT).show();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent();
            intent.putExtra("CROP_IMAGE", s);
            setResult(RESULT_OK, intent);
            finish();
            dismissProgressDialog();
        }
    }

    /**
     * 显示进度对话框
     */
    public void showProgressDialog() {
        if (mProgressDlg == null) {
            mProgressDlg = LoadingDialogShow.loading(this, "处理中...", false);
            mProgressDlg.setCanceledOnTouchOutside(false);
            mProgressDlg.setCancelable(false);
        }
    }

    /**
     * 取消进度对话框
     */
    public void dismissProgressDialog() {
        if (null != mProgressDlg && mProgressDlg.isShowing()) {
            mProgressDlg.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            // 回收并且置为null
            currentBitmap.recycle();
            currentBitmap = null;
        }
    }
}
