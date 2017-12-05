package com.lanhuawei.beautyproject.imageHandle.crop.util;

import android.content.res.Resources;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.lanhuawei.beautyproject.appBase.MyApplication;
import com.lanhuawei.beautyproject.R;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * Utility class for handling all of the Paint used to draw the CropOverlayView.
 */

public class PaintUtil {
    // Public Methods

    /**
     * Creates the Paint object for drawing the crop window border.
     */
    public static Paint newBorderPaint(@NonNull Resources resources) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R.dimen.border_thickness));
        paint.setColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.border));

        return paint;
    }

    /**
     * Creates the Paint object for drawing the crop window guidelines.
     */
    public static Paint newGuidelinePaint(@NonNull Resources resources) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R.dimen.guideline_thickness));
        paint.setColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.guideline));

        return paint;
    }

    /**
     * Creates the Paint object for drawing the translucent overlay outside the crop window.
     *
     * @return the new Paint object
     */
    public static Paint newSurroundingAreaOverlayPaint(@NonNull Resources resources) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.surrounding_area));

        return paint;
    }

    /**
     * Creates the Paint object for drawing the corners of the border
     */
    public static Paint newCornerPaint(@NonNull Resources resources) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R.dimen.corner_thickness));
        paint.setColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.corner));

        return paint;
    }
}
