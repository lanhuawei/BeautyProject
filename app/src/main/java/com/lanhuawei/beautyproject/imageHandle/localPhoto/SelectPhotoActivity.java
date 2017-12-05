package com.lanhuawei.beautyproject.imageHandle.localPhoto;

import android.content.Context;
import android.content.Intent;
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
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoSerializable;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.fragment.PhotoFolderFragment;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.fragment.PhotoFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 点击选择相册
 */

public class SelectPhotoActivity extends FragmentActivity implements
        PhotoFolderFragment.OnPageloadingClickListener,PhotoFragment.OnPhotoSelectClickListener{

    private Context mContext = SelectPhotoActivity.this;
    private PhotoFolderFragment mPhotoFolderFragment;//相册碎片
    private PhotoFragment mPhotoFragment;//相片碎片
    private Fragment nowFragment;

    private TextView tv_back, tv_title,tv_right;
    public static List<PhotoInfo> hasList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int backInt = 0;//处理照片状态
    private int count;//已选择图片数量
    private String comment;

    //    标志位，来判断是否需要进入图片裁剪和贴标签
    private boolean isNeedCoreAndSticker;
    public static int CORE_AND_STICKER_CODE = 0x123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());
        count = getIntent().getIntExtra("count", 0);
        isNeedCoreAndSticker = getIntent().getBooleanExtra("isNeedCoreAndSticker", false);
        if (getIntent().hasExtra("comment")) {
            comment = getIntent().getStringExtra("comment");
        }
        mFragmentManager = getSupportFragmentManager();
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backInt == 0) {
                    finish();//不处理照片改变状态
                } else if (backInt == 1) {
                    backInt--;
                    tv_title.setText(R.string.choose_album);
                    switchContent(nowFragment, mPhotoFolderFragment);
                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasList.size() > 0) {
                    if (!SelectPhotoActivity.this.isNeedCoreAndSticker) {
                        setResult(20);
                        finish();
                    } else {
                        Intent intent = new Intent(mContext, ImageCropAndStickerActivity.class);
                        startActivityForResult(intent, CORE_AND_STICKER_CODE);
                    }
                } else {
                    Toast.makeText(mContext, "至少选择一张图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_title.setText(R.string.choose_album);
        mPhotoFolderFragment = new PhotoFolderFragment();
        nowFragment = mPhotoFolderFragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.body, mPhotoFolderFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 选择Fragment
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (nowFragment != to) {//to不是当前页
            nowFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 先判断是否被添加过
                transaction.hide(from).add(R.id.body, to).commit();// 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit();// 隐藏当前的fragment，显示下一个
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CORE_AND_STICKER_CODE && resultCode == RESULT_OK) {
            setResult(20);
            finish();
        }

    }

    @Override
    public void onPageLoadingClick(List<PhotoInfo> list) {
        tv_title.setText("已选择了" + hasList.size() + "张");
        mPhotoFragment = new PhotoFragment();
        Bundle args = new Bundle();
        PhotoSerializable photoSerializable = new PhotoSerializable();
        for (PhotoInfo photoInfoBean : list) {
            photoInfoBean.setChoose(false);
        }
        photoSerializable.setPhotoInfos(list);
        args.putInt("count", count);
        args.putSerializable("list", photoSerializable);
        mPhotoFragment.setArguments(args);
        mPhotoFragment.setOnPhotoSelectClickListener(this);
        if (getIntent().hasExtra("comment") && "comment".equals(comment)) {
            mPhotoFragment.isFrom(1);
        } else if (getIntent().hasExtra("comment")&&"comment".equals("shareCreate")){
            mPhotoFragment.isFrom(2);
        }
        switchContent(nowFragment, mPhotoFragment);
        backInt++;
    }

    @Override
    public void onPhotoSelectClick(PhotoInfo photoInfo) {
        if (photoInfo.isChoose()) {
            hasList.add(photoInfo);
        } else {
            for (int i = 0; i < hasList.size(); i++) {
                PhotoInfo photoItem = hasList.get(i);
                if (photoInfo.getPath_absolute().equals(photoItem.getPath_absolute())) {
                    hasList.remove(i);
                }
            }
        }
        tv_title.setText("已选择" + hasList.size() + "张");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
            backInt--;
            tv_title.setText(R.string.choose_album);
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.show(mPhotoFolderFragment).commit();
//            mFragmentManager.popBackStack(0, 0);
            switchContent(nowFragment, mPhotoFolderFragment);
        }
        return false;
    }

//    //    onKeyDown或者这个   错的
//    private boolean backFlag = false;
//    @Override
//    public void onBackPressed() {
//        if (backFlag && backInt == 0) {
//            super.onBackPressed();
//        } else  {
//            if (backFlag && backInt == 1) {
//                backInt--;
//                tv_title.setText(R.string.choose_album);
//                FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                transaction.show(mPhotoFolderFragment).commit();
//                mFragmentManager.popBackStack(0, 0);
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (nowFragment != mPhotoFolderFragment) {
            switchContent(nowFragment, mPhotoFolderFragment);
            backInt--;
            tv_title.setText(R.string.choose_album);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static final String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE"};
//
//    /**
//     * Android 6.0权限检查
//     */
//    private static void verifyStoragePermissions(Context context) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
//            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//                return;
//            } else {
//
//            }
//        }
//    }

}
