package com.lanhuawei.beautyproject.videoHandle.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.videoHandle.adapter.VideoFolderAdapter;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoAlbumInfo;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/21.
 * 视频文件夹Fragment
 */

public class VideoFolderFragment extends Fragment {
    public interface OnPageLoadingClickListener {
        void onPageLoadingClick(List<VideoInfo> videoInfos);
    }

    private OnPageLoadingClickListener mOnPageLoadingClickListener;
    private ListView mListView;
    private ContentResolver mContentResolver;
    private List<VideoAlbumInfo> mVideoAlbumInfos = new ArrayList<>();
    private VideoFolderAdapter mVideoFolderAdapter;
    private LinearLayout video_loadingLay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videofolder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView) getView().findViewById(R.id.mListView);
        video_loadingLay = (LinearLayout) getView().findViewById(R.id.video_loadingLay);
        if (mOnPageLoadingClickListener == null) {
            mOnPageLoadingClickListener = (OnPageLoadingClickListener) getActivity();
        }
        mContentResolver = getActivity().getContentResolver();
        mVideoAlbumInfos.clear();

        new VideoAsyncTask().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnPageLoadingClickListener.onPageLoadingClick(mVideoAlbumInfos.get(position).getVideoInfos());
            }
        });
    }

    private class VideoAsyncTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... params) {
//             获取视频缩略图
            ThumbnailsUtil.clear();
            String[] projection = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.VIDEO_ID,
                    MediaStore.Video.Thumbnails.DATA};
            Cursor cursor = mContentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);
            if (cursor != null&&cursor.moveToFirst()) {
                int video_id;
                String video_path;
                int video_id_column = cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID);
                int data_column = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);

                do {
                    video_id = cursor.getInt(video_id_column);
                    video_path = cursor.getString(data_column);
                    ThumbnailsUtil.put(video_id, "file://" + video_path);
                } while (cursor.moveToNext());
                cursor.close();
            }
//             获取视频
            Cursor cursor1 = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, "date_modified DESC");//降序排列
            String _path = "_data";
            String _album = "bucket_display_name";

            HashMap<String, VideoAlbumInfo> stringVideoAlbumInfoHashMap = new HashMap<>();
            VideoAlbumInfo videoAlbumInfo = null;
            VideoInfo videoInfo = null;
            try {
                if (cursor1 != null && cursor1.moveToFirst()) {
                    do {
                        int index = 0;
                        int _id = cursor1.getInt(cursor1.getColumnIndex("_id"));
                        String path = cursor1.getString(cursor1.getColumnIndex(_path));
                        String album = cursor1.getString(cursor1.getColumnIndex(_album));

                        long time = cursor1.getLong(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));//视频时长
                        formatTimeLong(time);
//                        LogUtil.e("time", formatTimeLong(time));
                        if (time <= 15 * 1000) {//低于十五秒的视频
                            List<VideoInfo> videoInfos = new ArrayList<>();
                            videoInfo = new VideoInfo();

                            LogUtil.e("time", formatTimeLong(time));


                            if (stringVideoAlbumInfoHashMap.containsKey(album)) {
                                videoAlbumInfo = stringVideoAlbumInfoHashMap.remove(album);
                                if (mVideoAlbumInfos.contains(videoAlbumInfo)) {
                                    index = mVideoAlbumInfos.indexOf(videoAlbumInfo);
                                }
                                videoInfo.setVideo_id(_id);
                                videoInfo.setVideo_path_file("file://" + path);
                                videoInfo.setVideo_path_absolute(path);
                                videoAlbumInfo.getVideoInfos().add(videoInfo);
                                mVideoAlbumInfos.set(index, videoAlbumInfo);
                                stringVideoAlbumInfoHashMap.put(album, videoAlbumInfo);
                            } else {
                                videoAlbumInfo = new VideoAlbumInfo();
                                videoInfos.clear();

                                videoInfo.setVideo_id(_id);
                                videoInfo.setVideo_path_file("file://" + path);
                                videoInfo.setVideo_path_absolute(path);
                                videoInfos.add(videoInfo);

                                videoAlbumInfo.setVideo_id(_id);
                                videoAlbumInfo.setVideo_path_file("file://" + path);
                                videoAlbumInfo.setVideo_path_absolute(path);
                                videoAlbumInfo.setVideo_folder_name(album);//视频文件夹
                                videoAlbumInfo.setVideoInfos(videoInfos);

                                mVideoAlbumInfos.add(videoAlbumInfo);
                                stringVideoAlbumInfoHashMap.put(album, videoAlbumInfo);
                            }
                        }

//                        List<VideoInfo> videoInfos = new ArrayList<>();
//                        videoInfo = new VideoInfo();
//
//                        if (stringVideoAlbumInfoHashMap.containsKey(album)) {
//                            videoAlbumInfo = stringVideoAlbumInfoHashMap.remove(album);
//                            if (mVideoAlbumInfos.contains(videoAlbumInfo)) {
//                                index = mVideoAlbumInfos.indexOf(videoAlbumInfo);
//                            }
//                            videoInfo.setVideo_id(_id);
//                            videoInfo.setVideo_path_file("file://" + path);
//                            videoInfo.setVideo_path_absolute(path);
//                            videoAlbumInfo.getVideoInfos().add(videoInfo);
//                            mVideoAlbumInfos.set(index, videoAlbumInfo);
//                            stringVideoAlbumInfoHashMap.put(album, videoAlbumInfo);
//                        } else {
//                            videoAlbumInfo = new VideoAlbumInfo();
//                            videoInfos.clear();
//
//                            videoInfo.setVideo_id(_id);
//                            videoInfo.setVideo_path_file("file://" + path);
//                            videoInfo.setVideo_path_absolute(path);
//                            videoInfos.add(videoInfo);
//
//                            videoAlbumInfo.setVideo_id(_id);
//                            videoAlbumInfo.setVideo_path_file("file://" + path);
//                            videoAlbumInfo.setVideo_path_absolute(path);
//                            videoAlbumInfo.setVideo_folder_name(album);//视频文件夹
//                            videoAlbumInfo.setVideoInfos(videoInfos);
//
//                            mVideoAlbumInfos.add(videoAlbumInfo);
//                            stringVideoAlbumInfoHashMap.put(album, videoAlbumInfo);
//                        }
                    } while (cursor1.moveToNext());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (cursor1 != null) {
                    cursor1.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            video_loadingLay.setVisibility(View.GONE);
            if (getActivity() != null) {
                mVideoFolderAdapter = new VideoFolderAdapter(getActivity(), mVideoAlbumInfos);
                mListView.setAdapter(mVideoFolderAdapter);
            }
        }
    }

    /**
     * 格式化时间
     * 将毫秒转换为分:秒格式
     * @param time
     * @return
     */
    private String formatTimeLong(long time) {
        String hour = time / (1000 * 60 * 60) + "";
        String min = time / (1000 * 60) + "";
        String second = time % (1000 * 60) + "";
        if (hour.length() < 2) {
            hour = "0" + time / (1000 * 60 * 60) + "";
        } else {
            hour = time / (1000 * 60 * 60) + "";
        }
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (second.length() == 4) {
            second = "0" + (time % (1000 * 60)) + "";
        } else if (second.length() == 3) {
            second = "00" + (time % (1000 * 60)) + "";
        } else if (second.length() == 2) {
            second = "000" + (time % (1000 * 60)) + "";
        } else if (second.length() == 1) {
            second = "0000" + (time % (1000 * 60)) + "";
        }
        return hour + ":" + min + ":" + second.trim().substring(0, 2);

    }

}
