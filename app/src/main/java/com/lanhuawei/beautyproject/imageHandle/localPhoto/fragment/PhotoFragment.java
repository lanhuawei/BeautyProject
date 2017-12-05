package com.lanhuawei.beautyproject.imageHandle.localPhoto.fragment;

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

import com.lanhuawei.beautyproject.imageHandle.localPhoto.SelectPhotoActivity;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.adapter.PhotoAdapter;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoSerializable;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.UniversalImageLoadTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/13.
 * 每个相册文件夹中照片一页。碎片
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = PhotoFragment.class.getName();

    /**
     * 接口实现照片点击
     */
    public interface OnPhotoSelectClickListener {
        void onPhotoSelectClick(PhotoInfo photoInfo);
    }

    private OnPhotoSelectClickListener mOnPhotoSelectClickListener;
    private GridView mGridView;
    private PhotoAdapter mPhotoAdapter;

    private List<PhotoInfo> mList;
    private int isFrom;
    private int hasSelect = 1;//已经选择了几个

    public void isFrom(int s) {
        isFrom = s;
    }

    public void setOnPhotoSelectClickListener(OnPhotoSelectClickListener onPhotoSelectClickListener) {
        mOnPhotoSelectClickListener = onPhotoSelectClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mOnPhotoSelectClickListener == null) {
            mOnPhotoSelectClickListener = (OnPhotoSelectClickListener) getActivity();
        }
        mGridView = (GridView) getView().findViewById(R.id.grid_view);
        Bundle args = getArguments();
//        照片序列化
        PhotoSerializable photoSerializable = (PhotoSerializable) args.getSerializable("list");
        mList = new ArrayList<>();
        mList.addAll(photoSerializable.getPhotoInfos());
        for (int i = 0; i < mList.size(); i++) {
            PhotoInfo photoInfo = mList.get(i);
            if (isInSelectDataList(photoInfo.getPath_absolute())) {
                photoInfo.setChoose(true);
            } else {
                photoInfo.setChoose(false);
            }
        }
        hasSelect = SelectPhotoActivity.hasList.size();
        mPhotoAdapter = new PhotoAdapter(getActivity(), mList, mGridView);
        mGridView.setAdapter(mPhotoAdapter);
//        相册照片点击处理
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtil.i(TAG, "相册选择：" + mList.get(i).isChoose() + " " + i);
                if (isFrom == 1) {
                    if (mList.get(i).isChoose()) {
                        mList.get(i).setChoose(false);
                        hasSelect--;
                    } else if (hasSelect < 3) {
                        mList.get(i).setChoose(true);
                        hasSelect++;
                    } else {
//                        之后要自定义Toast
                        Toast.makeText(getActivity(), "最多选择3张图片！", Toast.LENGTH_SHORT).show();
                    }
                } else if (isFrom == 2) {
                    if (mList.get(i).isChoose()) {
                        mList.get(i).setChoose(false);
                        hasSelect--;
                    } else if (hasSelect < 4) {
                        mList.get(i).setChoose(true);
                        hasSelect++;
                    } else {
                        Toast.makeText(getActivity(), "最多选择4张图片", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mList.get(i).isChoose()) {
                        mList.get(i).setChoose(false);
                        hasSelect--;
                    } else if (hasSelect < 9) {
                        mList.get(i).setChoose(true);
                        hasSelect++;
                    } else {
                        Toast.makeText(getActivity(), "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    }
                }
                LogUtil.e(TAG, "size" + hasSelect + " ");
                LogUtil.i(TAG, "相册选择2" + mList.get(i).isChoose() + " " + i);

                mPhotoAdapter.refreshView(mList, i);
                mOnPhotoSelectClickListener.onPhotoSelectClick(mList.get(i));

            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    UniversalImageLoadTool.resume();
                } else {
                    UniversalImageLoadTool.pause();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

    }
    /**
     * 是否被选中了
     * @param selectedString
     * @return
     */
    private boolean isInSelectDataList(String selectedString) {
        if (SelectPhotoActivity.hasList != null) {
            List<PhotoInfo> hasList = SelectPhotoActivity.hasList;
            for (int i = 0; i < hasList.size(); i++) {
                if (selectedString.equals(hasList.get(i).getPath_absolute())) {
                    return true;
                }
            }
        }
        return false;
    }


}
