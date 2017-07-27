package com.zhanming.bannerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.util.TypedValue;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhanming on 2017/7/22.
 */

public class Utils {

    public static final int COLOR_PRIMARY = 1;
    public static final int COLOR_PRIMARY_DARK = 2;
    public static final int COLOR_ACCENT = 3;

    @IntDef({COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACCENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorType {

    }

    public static int dp2px(Context contetxt, int dip) {
        float density = contetxt.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static int getThemeColor(Context ctx, @ColorType int type) {
        TypedValue value = new TypedValue();
        if (type == COLOR_PRIMARY) {
            ctx.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        } else if (type == COLOR_PRIMARY_DARK) {
            ctx.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        } else if (type == COLOR_ACCENT) {
            ctx.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        }
        return value.data;
    }




}
