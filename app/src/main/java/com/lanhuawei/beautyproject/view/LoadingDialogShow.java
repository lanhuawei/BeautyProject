package com.lanhuawei.beautyproject.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.entity.PhoneMessage;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/9.
 * 正在加载的加载效果
 */

public class LoadingDialogShow {
    /**
     * 加载dialog 默认文字 返回不能消失
     * @param activity
     * @return
     */
    public static Dialog loading(Activity activity) {
        return loading(activity, activity.getResources().getString(R.string.loading), false);
    }

    /**
     * 加载dialog 返回不能消失
     * @param activity
     * @param text
     *          类型为int/String
     * @return
     */
    public static Dialog loading(Activity activity, Object text) {
        return loading(activity, text, false);
    }

    /**
     * 加载dialog 默认文字
     * @param activity
     * @param cancleable
     *              返回选择是否消失
     * @return
     */
    public static Dialog loading(Activity activity, boolean cancleable) {
        return loading(activity, activity.getResources().getString(R.string.loading), cancleable);
    }

    /**
     * 加载dialog
     * @param activity
     * @param text
     *              类型为int/String 提示文字
     * @param cancelable
     *              返回是否消失
     * @return
     */
    public static Dialog loading(Activity activity, Object text, boolean cancelable) {
        Dialog dialog = new Dialog(activity, R.style.loading_dialog);
        dialog.setCancelable(cancelable);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        View dialogView = View.inflate(activity, R.layout.view_dialog_loading, null);
        TextView tvText = (TextView) dialogView.findViewById(R.id.loadingDialog_tv_text);
        if (text instanceof Integer) {
            tvText.setText(Integer.parseInt(text.toString()));
        } else if (text instanceof String) {
            tvText.setText(text.toString());
        }
        dialog.setContentView(dialogView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = PhoneMessage.ScreenWidth / 5 * 2;
        dialogWindow.setAttributes(layoutParams);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }
}
