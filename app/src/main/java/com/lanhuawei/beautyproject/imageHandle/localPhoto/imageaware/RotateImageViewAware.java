package com.lanhuawei.beautyproject.imageHandle.localPhoto.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanhuawei.beautyproject.imageHandle.localPhoto.util.BitmapUtil;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/10.
 * Wrapper for Android {@link android.widget.ImageView ImageView}. Keeps weak reference of ImageView to prevent memory leaks.防止内存溢出
 * PhotoFolderAdapter
 */

public class RotateImageViewAware implements ImageAware {

    protected Reference<ImageView> mImageViewReference;
    protected boolean checkActualViewSize;
    private String path;

    /**
     * Constructor. <br />
     * References {@link #(android.widget.ImageView, boolean) ImageViewAware(imageView, true)}.
     * @param imageView {@link android.widget.ImageView ImageView} to work with
     * @param imageView
     * @param path
     */
    public RotateImageViewAware(ImageView imageView, String path) {
        this(imageView, true);
        this.path = path;
    }

    /**
     *  {@link android.widget.ImageView ImageView} to work with
     * @param checkActualViewSize <b>true</b> - then {@link #getWidth()} and {@link #getHeight()} will check actual
     *                            size of ImageView. It can cause known issues like
     *                            <a href="https://github.com/nostra13/Android-Universal-Image-Loader/issues/376">this</a>.
     *                            But it helps to save memory because memory cache keeps bitmaps of actual (less in
     *                            general) size.
     *                            <p/>
     *                            <b>false</b> - then {@link #getWidth()} and {@link #getHeight()} will <b>NOT</b>
     *                            consider actual size of ImageView, just layout parameters. <br /> If you set 'false'
     *                            it's recommended 'android:layout_width' and 'android:layout_height' (or
     *                            'android:maxWidth' and 'android:maxHeight') are set with concrete values. It helps to
     *                            save memory.
     *                            <p/>
     * @param imageView
     * @param checkActualViewSize
     */
    public RotateImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        this.mImageViewReference = new WeakReference<>(imageView);
        this.checkActualViewSize = checkActualViewSize;
    }

    /**
     * Width is defined by target {@link ImageView view} parameters, configuration
     * parameters or device display dimensions.
     * Size computing algorithm:
     * 1) Get the actual drawn <b>getWidth()</b> of the View. If view haven't drawn yet then go
     * to step #2.<br />
     * 2) Get <b>layout_width</b>. If it hasn't exact value then go to step #3.<br />
     * 3) Get <b>maxWidth</b>.
     * @return
     */
    @Override
    public int getWidth() {
        ImageView imageView = mImageViewReference.get();
        if (imageView != null) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = 0;
            if (checkActualViewSize && params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth();// Get actual image width
            }
            if (width <= 0 && params != null) {
                width = params.width;// Get layout width parameter
            }
            if (width <= 0) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");// Check maxWidth parameter
            }
            return width;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Height is defined by target {@link ImageView view} parameters, configuration
     * parameters or device display dimensions.<br />
     * Size computing algorithm:<br />
     * 1) Get the actual drawn <b>getHeight()</b> of the View. If view haven't drawn yet then go
     * to step #2.<br />
     * 2) Get <b>layout_height</b>. If it hasn't exact value then go to step #3.<br />
     * 3) Get <b>maxHeight</b>.
     * @return
     */
    @Override
    public int getHeight() {
        ImageView imageView = mImageViewReference.get();
        if (imageView != null) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int height = 0;
            if (checkActualViewSize && params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight(); // Get actual image height
            }
            if (height <= 0 && params != null) {
                height = params.height;// Get layout height parameter
            }
            if (height <= 0) {
                height = getImageViewFieldValue(imageView, "mMaxHeight");// Check maxHeight parameter
            }
            return height;
        }
        return 0;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {//获取字段值
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;

    }

    @Override
    public ViewScaleType getScaleType() {
        ImageView imageView = mImageViewReference.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return null;
    }

    @Override
    public View getWrappedView() {
        return mImageViewReference.get();
    }

    @Override
    public boolean isCollected() {
        return mImageViewReference.get() == null;
    }

    @Override
    public int getId() {
        ImageView imageView = mImageViewReference.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    @Override
    public boolean setImageDrawable(Drawable drawable) {
        ImageView imageView = mImageViewReference.get();
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
            return true;
        }
        return false;
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        ImageView imageView = mImageViewReference.get();
        if (imageView != null) {
            imageView.setImageBitmap(BitmapUtil.reviewPicRotate(bitmap, path));
        }
        return false;
    }
}
