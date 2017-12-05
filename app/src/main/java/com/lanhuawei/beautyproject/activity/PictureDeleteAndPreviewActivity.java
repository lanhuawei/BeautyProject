package com.lanhuawei.beautyproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lanhuawei.beautyproject.R;

import java.util.ArrayList;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/16.
 * 图片删除页面
 *
 */

public class PictureDeleteAndPreviewActivity extends Activity implements View.OnClickListener {

    public static boolean isChange;

    public static final String EXTRA_CURRENT = "currentIndex";
    public static final String EXTRA_IMAGEURLS = "imageUrls";
    public static final String IS_LOCKED_ARG = "isLocked";

    private ViewPager delete_preview_viewPage;
    private int currentIndex;
    private ArrayList<String> imageUrls;
    private TextView tv_delete_preview_indicate;
    private Context mContext = PictureDeleteAndPreviewActivity.this;
    private SimplePagerAdapter mSimplePagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_delete);
    }

    @Override
    public void onClick(View view) {

    }

    private class SimplePagerAdapter extends PagerAdapter {


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

}
