package com.zhanming.bannerview;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zhanming on 2017/7/17.
 */

public class BannerItem<T> {

    private T mContent;
    private View item;
    private int drawable = -1;
    private String imgUrl;
    private BannerAction mAction;


    public BannerItem(View item, @Nullable BannerAction action,@Nullable T content) {
        this.item = item;
        this.mContent = content;
        this.mAction =action;
    }

    public BannerItem(int drawable,@Nullable BannerAction action,@Nullable T content) {
        this.drawable = drawable;
        this.mContent = content;
        this.mAction =action;
    }

    public BannerItem(String imgUrl,@Nullable BannerAction action,@Nullable T content){
        this.imgUrl = imgUrl;
        this.mContent = content;
        this.mAction =action;
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

    public BannerAction getAction() {
        return mAction;
    }

    public void setAction(BannerAction mAction) {
        this.mAction = mAction;
    }

    public T getContent() {
        return mContent;
    }

    public void setContent(T content) {
        this.mContent = mContent;
    }
}
