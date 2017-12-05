package com.lanhuawei.beautyproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanhuawei.beautyproject.R;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/12/5.
 * 视频加载对话框
 */

public class CustomProgressDialog extends Dialog {
    private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {

        // TODO Auto-generated method stub
        super.dismiss();
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context,
                R.style.CustomProgressDialog);

        customProgressDialog.setContentView(R.layout.custom_progress_dialog_layout);

        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }

        ImageView imageView = (ImageView) customProgressDialog
                .findViewById(R.id.iv_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();

    }

    /**
     * [Summary] setTitile ����
     *
     * @param strTitle
     * @return
     */
    public CustomProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    /**
     * [Summary] setMessage ��ʾ����
     *
     * @param strMessage
     * @return
     */
    public CustomProgressDialog setMessage(String strMessage) {

        TextView tvMsg = (TextView) customProgressDialog
                .findViewById(R.id.tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

}
