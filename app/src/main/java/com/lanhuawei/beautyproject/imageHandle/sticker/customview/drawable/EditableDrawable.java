package com.lanhuawei.beautyproject.imageHandle.sticker.customview.drawable;

import android.graphics.Paint;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 *
 */

public interface EditableDrawable {
    int CURSOR_BLINK_TIME = 400;

    void setOnSizeChangeListener(OnSizeChange paramOnSizeChange);

    void beginEdit();

    void endEdit();

    boolean isEditing();

    CharSequence getText();

    void setText(CharSequence paramCharSequence);

    void setText(String paramString);

    void setTextHint(CharSequence paramCharSequence);

    void setTextHint(String paramString);

    boolean isTextHint();

    void setBounds(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

    void setTextColor(int paramInt);

    int getTextColor();

    float getTextSize();

    float getFontMetrics(Paint.FontMetrics paramFontMetrics);

    void setTextStrokeColor(int paramInt);

    int getTextStrokeColor();

    void setStrokeEnabled(boolean paramBoolean);

    boolean getStrokeEnabled();

    int getNumLines();

    interface OnSizeChange {
        public void onSizeChanged(EditableDrawable paramEditableDrawable, float paramFloat1,
                                  float paramFloat2, float paramFloat3, float paramFloat4);
    }
}
