package com.lanhuawei.beautyproject.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lanhuawei.beautyproject.R;

import java.util.HashMap;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/12/5.
 * 自定义对话框
 * *
 * 可修改标题、提示内容
 * 可修改"确定"和"取消"按钮上的文字
 * 可自定义"确定"和"取消"按钮行为
 * 注:自定义"确定"按钮行为，记得在自己方法里调用 CustomDialog.dismissAlert(activity);取消对话框显示
 * 否则，按返回键返回上一个Activity时，对话框还存在。用户体验不好。
 *
 * 弹出对话框选择
 */

public class CustomDialog {

    private static HashMap<String, Dialog> dialogs = new HashMap<String, Dialog>();
    /**
     * 自定义对话框,可改变标题和提示内容<br/>
     * 可自定义确定按钮行为
     *
     * @param activity
     * @param title        标题
     * @param sMessage     内容
     * @param confirmClick 点确定动作行为
     * @return
     */
    public static Dialog showConfirm(final Activity activity, String title,
                                     String sMessage, final View.OnClickListener confirmClick) {
        dismissAlert(activity);

        Dialog dialog = createBasicDialog(activity);
        dialogs.put(activity.toString(), dialog);
        TextView dialogTitleName = (TextView) dialog
                .findViewById(R.id.dialog_title_name);
        TextView dialogContent = (TextView) dialog
                .findViewById(R.id.dialog_content);
        Button sureBtn = (Button) dialog.findViewById(R.id.dialog_btn_sure);
        Button cancleBtn = (Button) dialog.findViewById(R.id.dialog_btn_cancle);

        if (TextUtils.isEmpty(title)) {
            title = getString(activity, R.string.dialog_title_default);
        }
        if (TextUtils.isEmpty(sMessage)) {
            sMessage = "";
        }
        dialogTitleName.setText(title);
        dialogContent.setText(sMessage);
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissAlert(activity);
                if (confirmClick != null) {
                    confirmClick.onClick(v);
                }
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissAlert(activity);
            }
        });
        dialog.show();

        return dialog;
    }
    /**
     * 取消对话框
     *
     * @param activity
     */
    public static void dismissAlert(Activity activity) {
        Dialog dialog = (Dialog) dialogs.get(activity.toString());
        if ((dialog != null) && (dialog.isShowing())) {
            dialog.dismiss();
            dialogs.remove(activity.toString());
        }
    }
    /**
     * 创建一个基于风格为R.style.dialog_basic、内容为R.layout.dialog_basic的空对话框
     *
     * @param activity
     * @return
     */
    private static Dialog createBasicDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.dialog_basic);
        dialog.setContentView(R.layout.dialog_basic_layout);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static String getString(Activity activity, int resId) {
        return activity.getResources().getString(resId);
    }




}
