package com.lanhuawei.beautyproject.adapter.rvadapter;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/14.
 * 子视图代表
 */

public interface ItemViewDelegate<T> {
    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
