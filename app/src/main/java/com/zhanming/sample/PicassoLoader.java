package com.zhanming.sample;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhanming.bannerview.ImageLoader;

/**
 * Created by zhanming on 2017/8/19.
 */

public class PicassoLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if(path instanceof Integer){
            Picasso.with(context).load((int)path).into(imageView);
        }else if(path instanceof String){
            Picasso.with(context).load((String)path).into(imageView);
        }
    }
}
