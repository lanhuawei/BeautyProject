package com.lanhuawei.beautyproject.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.appBase.BaseActivity;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;
import com.lanhuawei.beautyproject.view.CustomDialog;
import com.lanhuawei.beautyproject.view.CustomProgressDialog;

import java.io.IOException;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/12/5.
 * 视频播放页面
 */

public class TextureVideoActivity extends BaseActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private String TAG = TextureVideoActivity.class.getName();
    private Context mContext = TextureVideoActivity.this;
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path = "";
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private CustomProgressDialog progressDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_texture_video;
    }
    @Override
    protected void initView() {
        setTitleVisible(true);
        mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        startProgressDialog();
        findViewById(R.id.tv_left_back).setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.video_delete).setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                deleteVideo();
            }
        });
    }

    /**
     * 删除视频
     */
    private void deleteVideo() {
        CustomDialog.showConfirm(this, "", "确定删除这个视频？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectVideoActivity.hasList.remove(0);
                finish();
            }
        });
    }

    /**
     * 开始加载框
     */
    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在加载中...");
        }
        progressDialog.show();
    }



    @Override
    protected void initData() {
        path = getIntent().getStringExtra("path");
        Log.e("path", path);
    }

    @Override
    protected void doAfterReConnectNewWork() {

    }

    /**
     * MediaPlayer.OnBufferingUpdateListener
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.d(TAG, "onBufferingUpdate percent:" + String.valueOf(percent));
    }

    /**
     * MediaPlayer.OnCompletionListener
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.release();
        finish();
    }

    /**
     * MediaPlayer.OnPreparedListener
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        stopProgressDialog();
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    /**
     * MediaPlayer.OnVideoSizeChangedListener
     * @param mp
     * @param width
     * @param height
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        LogUtil.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            LogUtil.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }
    private void startVideoPlayback() {
        LogUtil.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG, "surfaceDestroyed called");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d(TAG, "surfaceChanged called" + format + "  " + width + "   " + height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (TextUtils.isEmpty(path)) {
            finish();
            return;
        }
        PlayVideo();
    }

    /**
     * 播放视频
     */
    private void PlayVideo() {
        doCleanUp();

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
        } catch (IOException e) {
            LogUtil.e("ERROR" + e.getMessage(), String.valueOf(e));
        }

    }

    /**
     * 默认设置
     */
    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
        stopProgressDialog();
    }

    /**
     * 释放视频播放器
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    /**
     * 暂停加载框
     */
    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }





}
