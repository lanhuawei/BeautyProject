package com.lanhuawei.beautyproject.adapter.rvadapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/14.
 * RecyclerView常用适配器
 */

public abstract class RvCommonAdapter<T> extends MultiItemTypeAdapter<T> {

    protected Context mContext;
    private int mLayoutId;
    private List<T> mDatas;
    private LayoutInflater mInflater;

    public RvCommonAdapter(final Context context, List<T> datas, final int layoutId) {
        super(context, datas);
        mContext = context;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                RvCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);



}
