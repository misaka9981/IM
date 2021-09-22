package com.cxy.im4cxy.adapter;


public interface OnRecyclerViewListener {
    /**
     * 单机选项
     *
     * @param position
     */
    void onItemClick(int position);

    /**
     * 长按选项
     *
     * @param position
     * @return
     */
    boolean onItemLongClick(int position);
}
