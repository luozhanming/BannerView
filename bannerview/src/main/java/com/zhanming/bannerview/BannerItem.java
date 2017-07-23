package com.zhanming.bannerview;

import android.view.View;

/**
 * Created by zhanming on 2017/7/17.
 */

public class BannerItem {

    private View item;
    private int drawable = -1;
    private String imgUrl;

    public BannerItem(View item) {
        this.item = item;
    }

    public BannerItem(int drawable) {
        this.drawable = drawable;
    }

    public BannerItem(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public View getItem() {
        return item;
    }

    public int getDrawable() {
        return drawable;
    }
}
