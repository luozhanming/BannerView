package com.zhanming.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 还差切换回调和定时切换功能没有写
 * <p>
 * 更新BadgeItem的逻辑没写
 * Created by zhanming on 2017/7/17.
 */

public class BannerView extends FrameLayout {

    private static final String TAG = "BannerView";

    private ViewPager mPager;
    private LinearLayout mIndicatorContainer;

    private List<BannerItem> items;
    private List<IndicatorView> indicators;

    private int defaultActiveColor = Color.RED;
    private int indicatorActiveColor = -1;
    private int defaultInActiveColor = Color.GREEN;
    private int indicatorInactiveColor = -1;
    private static final int DEFAULT_DURATION = 1000;
    private int changeDuration;
    private boolean canLoop;
    private boolean isLooping;
    private Timer mLoopTimer;

    private static final int DEFAULT_INDICATOR_SIZE = 20;
    private int bannerIndicatorSize;

    private int currentPosition = 0;


    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init();
    }

    private void init() {
        //findViewById一定要调用根布局的
        View parentView = LayoutInflater.from(getContext()).inflate(R.layout.container_banner, this, true);
        mPager = (ViewPager) parentView.findViewById(R.id.pager);
        mIndicatorContainer = (LinearLayout) parentView.findViewById(R.id.indicators);
        items = new ArrayList<>();
        indicators = new ArrayList<>();
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "position = " + position + ",positionOffset = " + positionOffset + ",positionOffsetPixels = " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //设置指示器
                currentPosition = position % items.size();
                int size = indicators.size();
                for (int i = 0; i < size; i++) {
                    if (currentPosition == i) {
                        indicators.get(i).setState(IndicatorView.ACTIVE);
                    } else {
                        indicators.get(i).setState(IndicatorView.INACTIVE);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPager.addOnPageChangeListener(pageChangeListener);
        mPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    cancelLooping();
                }else if(action == MotionEvent.ACTION_UP){
                    beginLoop();
                }
                return true;
            }
        });
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        defaultActiveColor = Utils.getThemeColor(context, Utils.COLOR_PRIMARY);
        defaultInActiveColor = Utils.getThemeColor(context, Utils.COLOR_PRIMARY_DARK);
        if (defaultActiveColor == 0) {
            defaultActiveColor = Color.RED;
        }
        if (defaultInActiveColor == 0) {
            defaultInActiveColor = Color.GREEN;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);

        indicatorActiveColor = typedArray.getColor(R.styleable.BannerView_banner_indicatorActiveColor, defaultActiveColor);
        indicatorInactiveColor = typedArray.getColor(R.styleable.BannerView_banner_indicatorInactiveColor, defaultInActiveColor);
        changeDuration = typedArray.getInteger(R.styleable.BannerView_banner_changeDuration, DEFAULT_DURATION);
        bannerIndicatorSize = typedArray.getInteger(R.styleable.BannerView_banner_indicatorSize, DEFAULT_INDICATOR_SIZE);
        canLoop = typedArray.getBoolean(R.styleable.BannerView_banner_loopable, true);
        typedArray.recycle();
    }

    public void initialize() {
        BannerAdapter adapter = new BannerAdapter();
        if (items.size() != 0) {
            adapter.setItems(items);
        }
        int size = items.size();
        for (int i = 0; i < size; i++) {
            IndicatorView indicator = new IndicatorView(getContext());
            indicator.setSize(bannerIndicatorSize);
            indicator.setActiveColor(indicatorActiveColor);
            indicator.setInactiveColor(indicatorInactiveColor);
            if (i == currentPosition) {
                indicator.setState(IndicatorView.ACTIVE);
            } else {
                indicator.setState(IndicatorView.INACTIVE);
            }
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mIndicatorContainer.addView(indicator);
            indicators.add(indicator);
        }
        mPager.setAdapter(adapter);
        //实现左右循环
        mPager.setCurrentItem(Integer.MAX_VALUE / 2);
        if (canLoop) {
            beginLoop();
        }
    }

    public void beginLoop() {
        if (isLooping) {
            return;
        }
        mLoopTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                });
            }
        };
        mLoopTimer.schedule(task, changeDuration, changeDuration);
        isLooping = true;
    }

    public void cancelLooping() {
        if (mLoopTimer != null && isLooping) {
            mLoopTimer.cancel();
        }
        isLooping = false;
    }


    public BannerView addItem(BannerItem item) {
        items.add(item);
        return this;
    }

    public BannerView setChangeDuration(int duration) {
        this.changeDuration = duration;
        return this;
    }

    public void removeBannerItem(int position) {
        BannerAdapter adapter = (BannerAdapter) mPager.getAdapter();
        items.remove(position);
        indicators.remove(position);
        mIndicatorContainer.removeViewAt(position);
        adapter.setDatas(items);
        mPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }


    public void setIndicatorActiveColor(@ColorInt int color) {
        this.indicatorActiveColor = color;
        if (indicators != null && indicators.size() > 0) {
            indicators.get(currentPosition).setActiveColor(color);
        }
    }

    public void setIndicatorInactiveColor(@ColorInt int color) {
        this.indicatorInactiveColor = color;
        int size = indicators.size();
        if (indicators != null && size > 0) {
            for (int i = 0; i < size; i++) {
                if (i != currentPosition) {
                    indicators.get(i).setInactiveColor(color);
                }
            }
        }
    }



    class BannerAdapter extends PagerAdapter {

        private List<BannerItem> datas;

        public BannerAdapter() {
            this.datas = datas;
        }

        public void setItems(List<BannerItem> items) {
            this.datas = items;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int pos = position % datas.size();
            BannerItem item = datas.get(pos);
            View bannerItemView = getBannerItemView(item);
            container.addView(bannerItemView);
            Log.d(TAG, "instantiateItem:" + position);
            return bannerItemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private View getBannerItemView(BannerItem item) {
            View bannerItemView = null;
            if (item.getItem() != null) {
                bannerItemView = item.getItem();
            } else if (item.getDrawable() != -1) {
                ImageView view = new ImageView(getContext());
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setImageResource(item.getDrawable());
                bannerItemView = view;
            } else {
                throw new IllegalArgumentException("The BannerItem doesn't have content.");
            }
            return bannerItemView;
        }


        public void setDatas(List<BannerItem> items) {
            this.datas = items;
            notifyDataSetChanged();
        }

    }

}

