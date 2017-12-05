package com.lanhuawei.beautyproject.appBase;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.kit.track.TrackBaseActivity;
import com.lanhuawei.beautyproject.R;
import com.lanhuawei.beautyproject.view.LoadingDialogShow;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/7.
 * TrackBaseActivity是导入千牛SDK牵牛SDK：IMCore-2.0.2.aar和IMKit-2.0.2.aar.才有的
 * 是集成阿里百川即时通讯
 * TrackBaseActivity
 */

public abstract class BaseActivity extends TrackBaseActivity {
    public static final int REQUEST_CODE = 0x0001;
    private RelativeLayout rootView, title_root;
    private View contentView;
    private TextView tv_left,tv_right, tv_center;//title
    private ImageView iv_right;//最右边的图片用于显示分享按钮或者其他
    private FrameLayout titleBar;//整个title
    private OnRightViewClickListener mOnRightViewClickListener;//右侧图标事件点击接口
    private OnLeftViewClickListener mOnLeftViewClickListener;//左侧图标事件点击接口
    protected Activity mActivity;
    private Dialog mDialog;//弹出的等待对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        mActivity = BaseActivity.this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication.getInstance().pushActivity(this);
        rootView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        setContentView(rootView);

        titleBar =(FrameLayout) findViewById(R.id.title_bar);
        int titleId = titleBar.getId();
        title_root =(RelativeLayout) findViewById(R.id.title_root);
        tv_left =(TextView) findViewById(R.id.tv_left);
        tv_center =(TextView) findViewById(R.id.tv_center);
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_right = (ImageView) findViewById(R.id.iv_right);

        tv_left.setOnClickListener(onTitleClickListener);
        tv_right.setOnClickListener(onTitleClickListener);
        iv_right.setOnClickListener(onTitleClickListener);

        contentView = LayoutInflater.from(getApplicationContext()).inflate(getLayoutId(), null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, titleId);
        rootView.addView(contentView, layoutParams);
        initView();
        initData();
    }

    /**
     * 隐藏左侧文字
     */
    public void setLiftTitleGone() {
        tv_left.setVisibility(View.GONE);
    }

    /**
     * 设置标题可见性
     * @param visible true：不可见，false：可见。默认可见
     */
    public void setTitleVisible(boolean visible) {
        if (!visible) {
            titleBar.setVisibility(View.VISIBLE);
        } else {
            titleBar.setVisibility(View.GONE);
        }
    }

    /**
     * 获得标题布局
     * @return
     */
    public View getTitleView() {
        return titleBar;
    }

    /**
     * 设置头部背景
     * @param resId
     */
    public void setTitleBg(int resId) {
        title_root.setBackgroundColor(getResources().getColor(resId));
    }

    /**
     * 设置整个页面的背景色
     * @param resId
     */
    public void setColorBackground(int resId) {
        rootView.setBackgroundColor(getResources().getColor(resId));
    }

    /**
     * 设置标题中间的文字
     * @param title
     */
    public void setCenterTitleText(String title) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(title);
    }

    /**
     *
     * @param resId
     */
    public void setCenterTitleText(int resId) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(resId);
    }

    /**
     * 设置标题中间文字
     * @param resId
     */
    public void setCenterTitleColor(int resId) {
        tv_center.setBackgroundColor(getResources().getColor(resId));
    }

    /**
     * 设置标题右侧文字
     * @param text
     */
    public void setRightTitleText(String text) {
        iv_right.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(text);
    }

    /**
     * 设置标题右侧文字
     * @param resId
     */
    public void setRightTitleText(int resId) {
        iv_right.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(resId);
    }

    /**
     * 设置右边文字颜色
     * @param resId
     */
    public void setRightTitleTextColor(int resId) {
        tv_right.setTextColor(getResources().getColor(resId));
    }

    /**
     * 设置标题左侧图标
     * @param resId
     */
    public void setLeftTitleDrawable(int resId) {
        tv_left.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    /**
     * 设置标题右侧图标
     * @param resId
     */
    public void setRightTitleImg(int resId) {
        iv_right.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
        iv_right.setImageResource(resId);
    }

    /**
     * 返回内容View布局
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected abstract void doAfterReConnectNewWork();

    /**
     * 设置title右边事件监听
     * @param onRightViewClickListener
     */
    public void setOnRightViewClickListener(OnRightViewClickListener onRightViewClickListener) {
        this.mOnRightViewClickListener = onRightViewClickListener;
    }

    /**
     * 设置title左边事件监听
     * @param onLeftViewClickListener
     */

    public void setOnLeftViewClickListener(OnLeftViewClickListener onLeftViewClickListener) {
        this.mOnLeftViewClickListener = onLeftViewClickListener;
    }

    /**
     * 右侧图标事件点击接口
     */
    public interface OnRightViewClickListener {
        void OnClick();
    }

    /**
     * 左侧图标事件点击接口
     */
    public interface OnLeftViewClickListener {
        void OnClick();
    }



    /**
     * 标题栏左右点击触发
     */
    private View.OnClickListener onTitleClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_left:
                    if (mOnLeftViewClickListener != null) {
                        mOnLeftViewClickListener.OnClick();
                    } else {
                        BaseActivity.this.finish();
                    }
                    break;
                case R.id.tv_right:
                case R.id.iv_right:
                    if (mOnRightViewClickListener != null) {
                        mOnRightViewClickListener.OnClick();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 显示加载对话框
     */
    public void showProgressDialog() {
        showProgressDialog("");
    }

    /**
     * 显示加载对话框
     * @param msg
     */
    public void showProgressDialog(Object msg) {
        if (mDialog == null) {
            if (TextUtils.isEmpty(String.valueOf(msg))) {
                msg = getString(R.string.common_loading);//数据加载，请稍候...
            }
            mDialog = LoadingDialogShow.loading(mActivity, msg, false);
            mDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            mDialog.setCancelable(false);
        }
    }

    /**
     * 取消对话框
     */
    public void dismissProcessDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();//配置
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}
