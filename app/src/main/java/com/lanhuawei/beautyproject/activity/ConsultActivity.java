package com.lanhuawei.beautyproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.adapter.GridImageAdapter;
import com.lanhuawei.beautyproject.appBase.BaseActivity;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.ImageCropAndStickerActivity;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.SelectPhoto;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.SelectPhotoActivity;
import com.lanhuawei.beautyproject.imageHandle.localPhoto.bean.PhotoInfo;
import com.lanhuawei.beautyproject.util.CameraUtil;
import com.lanhuawei.beautyproject.util.LogUtil;
import com.lanhuawei.beautyproject.util.SharePreferenceUtil;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;
import com.lanhuawei.beautyproject.videoHandle.bean.VideoInfo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 测试。圈子发新帖
 */

public class ConsultActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = ConsultActivity.class.getName();
    private Context mContext = ConsultActivity.this;
    private EditText content;//编辑的内容
    private GridView mGridView;

    private List<String> imageUrl = new ArrayList<>();//图片路径
    private SharePreferenceUtil mSharePreferenceUtil;

    private List<PhotoInfo> mPhotoInfos;
    private List<VideoInfo> mVideoInfos;

    private GridImageAdapter mGridImageAdapter;
//    private GridVideoAdapter mGridVideoAdapter;

    private String circleid;
    private String lableIds = "";
    private String picurls;
    private TextView tv_label_content;
    private RelativeLayout rl_add_label;
    private String labelName = "";
    private int index = 0;//上传图片索引
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response;
            switch (msg.what) {
                case 3004://未完
                    response = (String) msg.obj;
                    break;
                case 200:
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < imageUrl.size(); i++) {
                        if (builder.toString().length() == 0) {
                            builder.append(imageUrl.get(i));
                        } else {
                            builder.append(",").append(imageUrl.get(i));
                        }
                    }
                    picurls = builder.toString();



                    break;
                case 400:
                    String text = (String) msg.obj;
                    dismissProcessDialog();
                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                    break;
                case 0000:
                    EventBus.getDefault().post("PostOK:Consult");
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_consult;
    }

    @Override
    protected void initView() {
        setCenterTitleText(R.string.new_post);
        setRightTitleText(R.string.consult_release);
        setOnRightViewClickListener(new OnRightViewClickListener() {
            @Override
            public void OnClick() {
//                未写
            }
        });

        mSharePreferenceUtil = new SharePreferenceUtil(this);
        content = (EditText) findViewById(R.id.consult_content);
        mGridView = (GridView) findViewById(R.id.consult_gridView);

//        mGridView = (GridView) findViewById(R.id.consult_gridViewOne);
//        mGridViewTwo = (GridView) findViewById(R.id.consult_gridViewTwo);

        tv_label_content = (TextView) findViewById(R.id.tv_lable_content);
        rl_add_label = (RelativeLayout) findViewById(R.id.rl_add_lable);//添加咨询标签

        // 清除选择图片的集合类
        SelectPhotoActivity.hasList.clear();
        SelectVideoActivity.hasList.clear();

        mPhotoInfos = new ArrayList<>();
        mVideoInfos = new ArrayList<>();

//        mGridImageAdapter = new GridImageAdapter(mContext, mPhotoInfos, 1);
        mGridImageAdapter = new GridImageAdapter(mContext, mPhotoInfos, mVideoInfos);
//        mGridVideoAdapter = new GridVideoAdapter(mContext, mVideoInfos);

        mGridView.setAdapter(mGridImageAdapter);
//        mGridViewTwo.setAdapter(mGridVideoAdapter);

        rl_add_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        picurls = "";
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(this);


        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (i == mPhotoInfos.size() && mVideoInfos.size() == 0 || i == mVideoInfos.size() && mPhotoInfos.size() == 0) {
//            verifyStoragePermissions(mContext);
////            SelectPhoto.getInstance(this).selectPictureStyle(this, CameraUtil.sdCardIsExist(), true);
//        }

        if (i == 0 && mVideoInfos.size() == 0 || i == mPhotoInfos.size() + 1) {
            verifyStoragePermissions(mContext);
        } else {
            ArrayList<String> imageUrls = new ArrayList<>();
            for (int j = 0; j < mPhotoInfos.size(); j++) {
                imageUrls.add(mPhotoInfos.get(j).getPath_file());
            }
            for (int j = 0; j < mVideoInfos.size(); j++) {
                imageUrls.add(mVideoInfos.get(j).getVideo_path_file());
            }
            Intent intent = new Intent(mContext, PictureDeleteAndPreviewActivity.class);
            intent.putStringArrayListExtra(PictureDeleteAndPreviewActivity.EXTRA_IMAGEURLS, imageUrls);
            intent.putExtra(PictureDeleteAndPreviewActivity.EXTRA_CURRENT, i);
            startActivityForResult(intent, SelectPhoto.DELETE_IMAGE);
        }
    }

    public void onEventMainThread(String event) {
        if (event.indexOf(":") > 0) {

            if ("PostLables".equals(event.split(":")[0])) {
                lableIds = event.split(":")[1];
                labelName = event.split(":")[2];
                if (lableIds.equals("0")) {
                    lableIds = "";
                    labelName = "";
                    tv_label_content.setText("");
                } else {
                    tv_label_content.setText(labelName);
                }

            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectPhoto.CODE_PHOTO) {//图片选择返回
            mPhotoInfos.clear();
            List<PhotoInfo> photoes = SelectPhotoActivity.hasList;
            Collection<PhotoInfo> collection = photoes;
            mPhotoInfos.addAll(collection);
//            mGridImageAdapter = new GridImageAdapter(mContext, mPhotoInfos);
            mGridImageAdapter = new GridImageAdapter(mContext, mPhotoInfos, mVideoInfos);
            mGridView.setAdapter(mGridImageAdapter);
        } else if (resultCode == RESULT_OK && requestCode == SelectPhoto.CODE_CAMERA) {//拍照
            String fileName = CameraUtil.getRealFilePath();
            if (new File(fileName).exists()) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPath_file("file://" + fileName);
                photoInfo.setPath_absolute(fileName);
                mPhotoInfos.add(photoInfo);
                SelectPhotoActivity.hasList.add(photoInfo);
                Intent intent = new Intent(mContext, ImageCropAndStickerActivity.class);
                startActivityForResult(intent, SelectPhoto.CODE_PHOTO);
            }
        }
        if (requestCode == SelectPhoto.DELETE_IMAGE) {//图片预览和删除
            mPhotoInfos.clear();
            List<PhotoInfo> photoInfos = SelectPhotoActivity.hasList;
            Collection<PhotoInfo> collection = photoInfos;
            mPhotoInfos.addAll(collection);
            mGridImageAdapter.notifyDataSetChanged();
            PictureDeleteAndPreviewActivity.isChange = false;
        }
        if (requestCode == SelectPhoto.CODE_VIDEO) {//视频

            mPhotoInfos.clear();
            List<PhotoInfo> photoes = SelectPhotoActivity.hasList;
            Collection<PhotoInfo> collection = photoes;
            mPhotoInfos.addAll(collection);

            mVideoInfos.clear();
            List<VideoInfo> videoInfos = SelectVideoActivity.hasList;

            LogUtil.e("size", String.valueOf(SelectVideoActivity.hasList.size()));

            Collection<VideoInfo> videoCollection = videoInfos;
            mVideoInfos.addAll(videoCollection);
//            mGridVideoAdapter = new GridVideoAdapter(mContext, mVideoInfos);
            mGridImageAdapter = new GridImageAdapter(mContext, mPhotoInfos, mVideoInfos);
//            mGridView.setAdapter(mGridVideoAdapter);
            mGridView.setAdapter(mGridImageAdapter);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void doAfterReConnectNewWork() {

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};

    /**
     * Android 6.0权限检查
     */
    private static void verifyStoragePermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                return;
            } else {
                SelectPhoto.getInstance(context).selectPictureStyle((Activity) context, CameraUtil.sdCardIsExist(), true);
            }
        } else {
            SelectPhoto.getInstance(context).selectPictureStyle((Activity) context, CameraUtil.sdCardIsExist(), true);
        }
    }

    /**
     * 权限申请返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        SelectPhoto.getInstance(mContext).selectPictureStyle((Activity) mContext, CameraUtil.sdCardIsExist(), true);
                        return;
                    } else {
                        Toast.makeText(mContext, "部分权限未能获取，部分功能可能无法使用!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


}
