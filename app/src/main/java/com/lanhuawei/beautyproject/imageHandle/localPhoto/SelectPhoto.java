package com.lanhuawei.beautyproject.imageHandle.localPhoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.util.CameraUtil;
import com.lanhuawei.beautyproject.videoHandle.SelectVideoActivity;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 选择图片
 * 未完
 */

public class SelectPhoto {
    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_PHOTO = 1;

    public static final int REQEST_CODE_ALBUM = 101; // 相册
    public static final int REQEST_CODE_CROP = 102; // 剪裁
    public static final int REQEST_CODE_CROP_RESULT = 103;// 剪裁结果

    public static final int CODE_CAMERA = 21;
    public static final int CODE_PHOTO = 22;
    public static final int CODE_VIDEO = 23;
    public static final int DELETE_IMAGE = 30;

    private static SelectPhoto sSelectPhoto;
    private Context mContext;

    public SelectPhoto(Context context) {
        mContext = context;
    }

    public static SelectPhoto getInstance(Context context) {
        if (sSelectPhoto == null) {
            synchronized (SelectPhoto.class) {
                if (sSelectPhoto == null) {
                    sSelectPhoto = new SelectPhoto(context);
                }
            }
        }
        return sSelectPhoto;
    }


    /**
     * 选择图片方式，跳转多图选择
     * @param activity
     * @param sdCardIsExit
     * @param isNeedCoreAndSticker
     */
    public void selectPictureStyle(final Activity activity, final boolean sdCardIsExit, final boolean isNeedCoreAndSticker) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        String[] photoArr = mContext.getResources().getStringArray(R.array.select_photos);
        dialog.setItems(photoArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                switch (i) {
                    case 0:
                        if (sdCardIsExit) {
//                            activity.startActivityForResult(CameraUtil.getCameraIntent(), CODE_CAMERA);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                activity.startActivityForResult(CameraUtil.androidNUriFromFile(activity), CODE_CAMERA);
                            } else {
                                activity.startActivityForResult(CameraUtil.getCameraIntent(), CODE_CAMERA);
                            }

                        } else {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_sdCard), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        intent = new Intent(mContext, SelectPhotoActivity.class);
                        intent.putExtra("isNeedCoreAndSticker", isNeedCoreAndSticker);
                        activity.startActivityForResult(intent, CODE_PHOTO);
                        break;
                    case 2:
                        intent = new Intent(mContext, SelectVideoActivity.class);
                        activity.startActivityForResult(intent, CODE_VIDEO);
                        break;
                    case 3:
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.show();

    }


}
