package com.lanhuawei.beautyproject.videoHandle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.appBase.MyApplication;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoSerializable;
import com.lanhuawei.beautyproject.videoHandle.fragment.VideoFolderFragment;
import com.lanhuawei.beautyproject.videoHandle.fragment.VideoFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 选择视频
 */

public class SelectVideoActivity extends FragmentActivity implements VideoFolderFragment.OnPageLoadingClickListener,
        VideoFragment.OnVideoSelectClickListener{

    private Context mContext = SelectVideoActivity.this;
    private VideoFolderFragment mVideoFolderFragment;
    private VideoFragment mVideoFragment;
    private Fragment nowFragment;

    private TextView video_tv_back,video_title, video_tv_right;
    public static List<VideoInfo> hasList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int backInt = 0;
    private int count;//已选择视频数量
    private String comment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());
        count = getIntent().getIntExtra("count", 0);
        if (getIntent().hasExtra("comment")) {
            comment = getIntent().getStringExtra("comment");
        }
        mFragmentManager = getSupportFragmentManager();
        video_tv_back = (TextView) findViewById(R.id.video_tv_back);
        video_title = (TextView) findViewById(R.id.video_tv_title);
        video_tv_right = (TextView) findViewById(R.id.video_tv_right);

        video_tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backInt == 0) {
                    finish();
                } else if (backInt == 1) {
                    backInt--;
                    video_title.setText(R.string.choose_video_album);
                    switchContent(nowFragment, mVideoFolderFragment);
                }
            }
        });
        video_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasList.size() > 0) {
                    setResult(20);
                    finish();
                } else {
                    Toast.makeText(mContext, "至少选择一个视频", Toast.LENGTH_SHORT).show();
                }
            }
        });

        video_title.setText(R.string.choose_video_album);
        mVideoFolderFragment = new VideoFolderFragment();
        nowFragment = mVideoFolderFragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.video_body, mVideoFolderFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 切换Fragment
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (nowFragment != to) {
            nowFragment = to;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {//先判断是否被添加过
                fragmentTransaction.hide(from).add(R.id.video_body, to).commit();// 没被添加，隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public void onPageLoadingClick(List<VideoInfo> videoInfos) {
        video_title.setText("已经选择了" + hasList.size() + "个");
        mVideoFragment = new VideoFragment();
        Bundle args = new Bundle();
        VideoSerializable videoSerializable = new VideoSerializable();
        for (VideoInfo videoInfoBean : videoInfos) {
            videoInfoBean.setChoose(false);
        }
        videoSerializable.setVideoInfos(videoInfos);
        args.putInt("count", count);
        args.putSerializable("videoList", videoSerializable);
        mVideoFragment.setArguments(args);
        mVideoFragment.setOnVideoSelectClickListener(this);

        switchContent(nowFragment, mVideoFragment);
        backInt++;
    }

    /**
     * 视频文件点击
     * @param videoInfo
     */
    @Override
    public void onVideoSelectClick(VideoInfo videoInfo) {
        if (videoInfo.isChoose()) {
            hasList.add(videoInfo);
        } else {
            for (int i = 0; i < hasList.size(); i++) {
                VideoInfo videoInfoItem = hasList.get(i);
                if (videoInfo.getVideo_path_absolute().equals(videoInfoItem.getVideo_path_absolute())) {
                    hasList.remove(i);
                }
            }
        }
        video_title.setText("已选择" + hasList.size() + "个");
    }

//    @Override
//    public void onBackPressed() {
//        if (backInt == 0) {
//            super.onBackPressed();
//        } else if (backInt == 1) {
//            backInt--;
//            video_title.setText(R.string.choose_video_album);
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.show(mVideoFolderFragment).commit();
//            mFragmentManager.popBackStack(0, 0);
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
            backInt--;
            video_title.setText(R.string.choose_video_album);
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.show(mVideoFolderFragment).commit();
//            mFragmentManager.popBackStack(0, 0);
            switchContent(nowFragment, mVideoFolderFragment);
        }
        return false;
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
        if (nowFragment != mVideoFolderFragment) {
            switchContent(nowFragment, mVideoFolderFragment);
            backInt--;
            video_title.setText(R.string.choose_video_album);
        }
    }



}
