package com.lanhuawei.beautyproject.videoHandle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;
import com.lanhuawei.beautyproject.videoHandle.adapter.VideoAdapter;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频文件Fragment
 */

public class VideoFragment extends Fragment {
    public interface OnVideoSelectClickListener {
        void onVideoSelectClick(VideoInfo videoInfo);
    }

    private OnVideoSelectClickListener mOnVideoSelectClickListener;
    private GridView mGridView;
    private VideoAdapter mVideoAdapter;

    private List<VideoInfo> mVideoInfos;
//    private int isFrom;
    private int hasSelect = 1;//已经选择了几个

//    public void isFrom(int s) {
//        isFrom = s;
//    }
    public void setOnVideoSelectClickListener(OnVideoSelectClickListener onVideoSelectClickListener) {
        mOnVideoSelectClickListener = onVideoSelectClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mOnVideoSelectClickListener == null) {
            mOnVideoSelectClickListener = (OnVideoSelectClickListener) getActivity();
        }
        mGridView = (GridView) getView().findViewById(R.id.video_grid_view);
        Bundle args = getArguments();

        VideoSerializable videoSerializable = (VideoSerializable) args.getSerializable("videoList");
        mVideoInfos = new ArrayList<>();
        mVideoInfos.addAll(videoSerializable.getVideoInfos());

        for (int i = 0; i < mVideoInfos.size(); i++) {
            VideoInfo videoinfo = mVideoInfos.get(i);
            if (isInSelectDataList(videoinfo.getVideo_path_absolute())) {
                videoinfo.setChoose(true);
            } else {
                videoinfo.setChoose(false);
            }
        }
        hasSelect = SelectVideoActivity.hasList.size();
        mVideoAdapter = new VideoAdapter(getActivity(), mVideoInfos, mGridView);
        mGridView.setAdapter(mVideoAdapter);

//        照片点击事件处理
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mVideoInfos.get(position).isChoose()) {
                    mVideoInfos.get(position).setChoose(false);
                    hasSelect--;
                } else if (hasSelect < 1) {
                    mVideoInfos.get(position).setChoose(true);
                    hasSelect++;
                } else {
                    Toast.makeText(getActivity(), "最多选择1个视频", Toast.LENGTH_SHORT).show();
                }

                mVideoAdapter.refreshView(mVideoInfos, position);
                mOnVideoSelectClickListener.onVideoSelectClick(mVideoInfos.get(position));
            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    UniversalImageLoadTool.resume();
                } else {
                    UniversalImageLoadTool.pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    /**
     * 是否被选中了
     * @param selectedString
     * @return
     */
    private boolean isInSelectDataList(String selectedString) {
        if (SelectVideoActivity.hasList != null) {
            List<VideoInfo> hasList = SelectVideoActivity.hasList;
            for (int i = 0; i < hasList.size(); i++) {
                if (selectedString.equals(hasList.get(i).getVideo_path_absolute())) {
                    return true;
                }
            }
        }
        return false;
    }

}
