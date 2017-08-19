package com.zhanming.bannerview;

import android.content.Context;
import android.widget.ImageView;

/**
 * 使用图片加载库加载图片接口
 * Created by zhanming on 2017/8/3.
 */

public abstract class ImageLoader {
    public abstract void  displayImage(Context context, Object path, ImageView imageView);
}
