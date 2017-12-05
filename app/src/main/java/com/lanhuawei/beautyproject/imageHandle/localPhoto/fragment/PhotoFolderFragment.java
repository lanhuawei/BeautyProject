package com.lanhuawei.beautyproject.imageHandle.localPhoto.fragment;

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
import com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter.PhotoFolderAdapter;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.AlbumInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.ThumbnailsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 图片文件夹显示碎片
 */

public class PhotoFolderFragment extends Fragment {

    public interface OnPageloadingClickListener {
        void onPageLoadingClick(List<PhotoInfo> list);
    }

    private OnPageloadingClickListener mOnPageloadingClickListener;
    private ListView mListView;
    private ContentResolver mContentResolver;
    private List<AlbumInfo> mAlbumInfos = new ArrayList<>();//文件夹
    private PhotoFolderAdapter mPhotoFolderAdapter;//图片文件夹显示适配器
    private LinearLayout loadingLay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_folder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView =(ListView) getView().findViewById(R.id.listView);
        loadingLay = (LinearLayout) getView().findViewById(R.id.loadingLay);

        if (mOnPageloadingClickListener == null) {
            mOnPageloadingClickListener = (OnPageloadingClickListener) getActivity();
        }
        mContentResolver = getActivity().getContentResolver();
        mAlbumInfos.clear();

        new ImageAsyncTask().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mOnPageloadingClickListener.onPageLoadingClick(mAlbumInfos.get(i).getPhotoInfoList());
            }
        });

    }

    /**
     * 获取原图和缩略图
     */
    private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... voids) {
//            获取缩略图
            ThumbnailsUtil.clear();
            String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA};
            Cursor cursor = mContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColum = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColum = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    image_id = cursor.getInt(image_idColum);
                    image_path = cursor.getString(dataColum);
                    ThumbnailsUtil.put(image_id, "file://" + image_path);
                } while (cursor.moveToNext());
                cursor.close();
            }


//            获取原图
            Cursor cursor1 = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, "date_modified DESC");//降序排列
            String _path = "_data";
            String _album = "bucket_display_name";

            HashMap<String, AlbumInfo> stringAlbumInfoHashMap = new HashMap<>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;
            if (cursor1 != null && cursor1.moveToFirst()) {
                do {
                    int index = 0;
                    int _id = cursor1.getInt(cursor1.getColumnIndex("_id"));
                    String path = cursor1.getString(cursor1.getColumnIndex(_path));
                    String album = cursor1.getString(cursor1.getColumnIndex(_album));
                    List<PhotoInfo> photoInfos = new ArrayList<>();
                    photoInfo = new PhotoInfo();
                    if (stringAlbumInfoHashMap.containsKey(album)) {
                        albumInfo = stringAlbumInfoHashMap.remove(album);
                        if (mAlbumInfos.contains(albumInfo)) {
                            index = mAlbumInfos.indexOf(albumInfo);
                        }
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://" + path);
                        photoInfo.setPath_absolute(path);
                        albumInfo.getPhotoInfoList().add(photoInfo);
                        mAlbumInfos.set(index, albumInfo);
                        stringAlbumInfoHashMap.put(album, albumInfo);
                    } else {
                        albumInfo = new AlbumInfo();
                        photoInfos.clear();

                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://" + path);
                        photoInfo.setPath_absolute(path);
                        photoInfos.add(photoInfo);

                        albumInfo.setImage_id(_id);
                        albumInfo.setPath_file("file://" + path);
                        albumInfo.setPath_absolute(path);
                        albumInfo.setAlbum_name(album);//相册名字
                        albumInfo.setPhotoInfoList(photoInfos);

                        mAlbumInfos.add(albumInfo);
                        stringAlbumInfoHashMap.put(album, albumInfo);
                    }

                } while (cursor1.moveToNext());
                cursor1.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            loadingLay.setVisibility(View.GONE);
            if (getActivity() != null) {
                mPhotoFolderAdapter = new PhotoFolderAdapter(getActivity(), mAlbumInfos);
//                LogUtil.e("AlbumInfosSize", String.valueOf(mAlbumInfos.get(1).getAlbum_name()));
                mListView.setAdapter(mPhotoFolderAdapter);
            }
        }
    }

//    /**
//     * 获取视频原图和缩略图
//     */
//    private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {
//        @Override
//        protected Object doInBackground(Void... voids) {
////            获取缩略图
//            ThumbnailsUtil.clear();
//            String[] projection = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.VIDEO_ID,
//                    MediaStore.Video.Thumbnails.DATA};
//            Cursor cursor = mContentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int image_id;
//                String image_path;
//                int image_idColum = cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID);
//                int dataColum = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
//                do {
//                    image_id = cursor.getInt(image_idColum);
//                    image_path = cursor.getString(dataColum);
//                    ThumbnailsUtil.put(image_id, "file://" + image_path);
//                } while (cursor.moveToNext());
//                cursor.close();
//            }
////            获取视频
//            Cursor cursor1 = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    null, null, null, "date_modified DESC");//降序排列
//            String _path = "_data";
//            String _album = "bucket_display_name";
//
//            HashMap<String, AlbumInfo> stringAlbumInfoHashMap = new HashMap<>();
//            AlbumInfo albumInfo = null;
//            PhotoInfo photoInfo = null;
//            if (cursor1 != null && cursor1.moveToFirst()) {
//                do {
//                    int index = 0;
//                    int _id = cursor1.getInt(cursor1.getColumnIndex("_id"));
//                    String path = cursor1.getString(cursor1.getColumnIndex(_path));
//                    String album = cursor1.getString(cursor1.getColumnIndex(_album));
//                    List<PhotoInfo> photoInfos = new ArrayList<>();
//                    photoInfo = new PhotoInfo();
//                    if (stringAlbumInfoHashMap.containsKey(album)) {
//                        albumInfo = stringAlbumInfoHashMap.remove(album);
//                        if (mAlbumInfos.contains(albumInfo)) {
//                            index = mAlbumInfos.indexOf(albumInfo);
//                        }
//                        photoInfo.setImage_id(_id);
//                        photoInfo.setPath_file("file://" + path);
//                        photoInfo.setPath_absolute(path);
//                        albumInfo.getPhotoInfoList().add(photoInfo);
//                        mAlbumInfos.set(index, albumInfo);
//                        stringAlbumInfoHashMap.put(album, albumInfo);
//                    } else {
//                        albumInfo = new AlbumInfo();
//                        photoInfos.clear();
//
//                        photoInfo.setImage_id(_id);
//                        photoInfo.setPath_file("file://" + path);
//                        photoInfo.setPath_absolute(path);
//                        photoInfos.add(photoInfo);
//
//                        albumInfo.setImage_id(_id);
//                        albumInfo.setPath_file("file://" + path);
//                        albumInfo.setPath_absolute(path);
//                        albumInfo.setAlbum_name(album);//视频文件夹
//                        albumInfo.setPhotoInfoList(photoInfos);
//
//                        mAlbumInfos.add(albumInfo);
//                        stringAlbumInfoHashMap.put(album, albumInfo);
//                    }
//
//                } while (cursor1.moveToNext());
//                cursor1.close();
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            loadingLay.setVisibility(View.GONE);
//            if (getActivity() != null) {
//                mPhotoFolderAdapter = new PhotoFolderAdapter(getActivity(), mAlbumInfos);
////                LogUtil.e("AlbumInfosSize", String.valueOf(mAlbumInfos.get(1).getAlbum_name()));
//                mListView.setAdapter(mPhotoFolderAdapter);
//            }
//        }
//    }

}
