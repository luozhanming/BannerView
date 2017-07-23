package com.zhanming.bannerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhanming on 2017/7/17.
 */

public class IndicatorView extends View {


    private int activeColor = Color.RED;
    private int inactiveColor = Color.GREEN;
    @State
    private int state = INACTIVE;
    private int size;
    private int padding;

    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;


    @IntDef({ACTIVE, INACTIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }


    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //限制指示器大小
        float maxSize = getResources().getDimension(R.dimen.indicator_max_size);
        float minSize = getResources().getDimension(R.dimen.indicator_min_size);
        if (size > maxSize) {
            size = (int) maxSize;
        } else if (size < minSize) {
            size = (int) minSize;
        }
        this.padding = size/4;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (state == INACTIVE) {
            paint.setColor(inactiveColor);
        } else {
            paint.setColor(activeColor);
        }
        int radius = (size)/2-padding;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }

    public int getState() {
        return this.state;
    }

    public void setState(@State int state) {
        this.state = state;
        invalidate();
    }

    public void setSize(int size) {
        int s = Utils.dp2px(getContext(), size);
        this.size = s;
        this.padding = s/4;
        invalidate();
    }

    public int getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
        invalidate();
    }

    public int getInactiveColor() {
        return inactiveColor;

    }

    public void setInactiveColor(int inactiveColor) {
        this.inactiveColor = inactiveColor;
        invalidate();
    }
}
