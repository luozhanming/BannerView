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
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhanming on 2017/7/17.
 */

public class BannerView extends FrameLayout {

    private static final String TAG = "BannerView";

    private ViewPager mPager;
    private LinearLayout mIndicatorContainer;

    private List<BannerItem> items;
    private List<IndicatorView> indicators;
    //临时items，负责二次加载刷新ViewPager
    private List<BannerItem> tempItems;

    private int defaultActiveColor = Color.RED;
    private int indicatorActiveColor = -1;
    private int defaultInActiveColor = Color.GREEN;
    private int indicatorInactiveColor = -1;
    private static final int DEFAULT_PEROID = 1000;
    private int mChangePeroid;
    private boolean canLoop;
    private boolean isLooping;
    private boolean hasInitialized = false;
    private Timer mLoopTimer;
    private BannerAdapter mBannerAdapter;

    private static final int DEFAULT_INDICATOR_SIZE = 20;
    private int bannerIndicatorSize;

    private int mCurrentPosition = 0;


    public BannerView(Context context) {
        super(context);
        init();
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
        mBannerAdapter = new BannerAdapter();
        mPager.setAdapter(mBannerAdapter);
        mIndicatorContainer = (LinearLayout) parentView.findViewById(R.id.indicators);
        items = new ArrayList<>();
        tempItems = new ArrayList<>();
        indicators = new ArrayList<>();
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "position = " + position + ",positionOffset = " + positionOffset + ",positionOffsetPixels = " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //设置指示器
                mCurrentPosition = position % items.size();
                int size = indicators.size();
                for (int i = 0; i < size; i++) {
                    if (mCurrentPosition == i) {
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
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        defaultActiveColor = Utils.getThemeColor(context, Utils.COLOR_ACCENT);
        defaultInActiveColor = Utils.getThemeColor(context, Utils.COLOR_PRIMARY);
        if (defaultActiveColor == 0) {
            defaultActiveColor = Color.RED;
        }
        if (defaultInActiveColor == 0) {
            defaultInActiveColor = Color.GREEN;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        indicatorActiveColor = typedArray.getColor(R.styleable.BannerView_banner_indicatorActiveColor, defaultActiveColor);
        indicatorInactiveColor = typedArray.getColor(R.styleable.BannerView_banner_indicatorInactiveColor, defaultInActiveColor);
        mChangePeroid = typedArray.getInteger(R.styleable.BannerView_banner_changeDuration, DEFAULT_PEROID);
        bannerIndicatorSize = typedArray.getInteger(R.styleable.BannerView_banner_indicatorSize, DEFAULT_INDICATOR_SIZE);
        canLoop = typedArray.getBoolean(R.styleable.BannerView_banner_loopable, true);
        typedArray.recycle();
    }

    public void initialize() throws IllegalAccessException {
        if(items.size()==0){
            throw new IllegalAccessException("Invoke addItem method before initialize.");
        }
        if (items.size() != 0) {
            mBannerAdapter.setItems(items);
        }
        //设置指示器
        int size = items.size();
        for (int i = 0; i < size; i++) {
            addIndicatorToContainer(i);
        }
        //实现左右循环
        mPager.setCurrentItem(mCurrentPosition);
        beginLoop();
        hasInitialized = true;
    }


    private void addIndicatorToContainer(int position) {
        IndicatorView indicator = new IndicatorView(getContext());
        indicator.setSize(bannerIndicatorSize);
        indicator.setActiveColor(indicatorActiveColor);
        indicator.setInactiveColor(indicatorInactiveColor);
        if (position == mCurrentPosition) {
            indicator.setState(IndicatorView.ACTIVE);
        } else {
            indicator.setState(IndicatorView.INACTIVE);
        }
        indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mIndicatorContainer.addView(indicator);
        indicators.add(indicator);
    }

    /**
     * 开始轮播
     */
    public void beginLoop() {
        if (isLooping || !canLoop) {
            return;
        }
        mLoopTimer = new Timer();
        WeakReference<Timer> wr = new WeakReference<>(mLoopTimer);
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
        mLoopTimer.schedule(task, mChangePeroid, mChangePeroid);
        isLooping = true;
    }

    /**
     * 取消轮播
     */
    public void cancelLooping() {
        if (mLoopTimer != null && isLooping) {
            mLoopTimer.cancel();
        }
        isLooping = false;
    }


    /**
     * 刷新Banner
     */
    public void refresh() {
        cancelLooping();
        resetBanner();
        mBannerAdapter = new BannerAdapter();
        items.addAll(tempItems);
        mBannerAdapter.setItems(items);
        tempItems.clear();
        mPager.setAdapter(mBannerAdapter);
        //设置指示器
        int size = items.size();
        mCurrentPosition = 0;
        for (int i = 0; i < size; i++) {
            addIndicatorToContainer(i);
        }
        //实现左右循环
        mPager.setCurrentItem(mCurrentPosition);
        beginLoop();
    }

    private void resetBanner() {
        cancelLooping();
        indicators.clear();
        mIndicatorContainer.removeAllViews();
        this.items.clear();
        mBannerAdapter.notifyDataSetChanged();
    }


    public BannerView addItem(BannerItem item) {
        if (hasInitialized) {
            tempItems.add(item);
        } else {
            items.add(item);
        }
        return this;
    }

    public List<BannerItem> getItems(){
        return this.items;
    }

    public BannerView setChangePeroid(int peroid) {
        this.mChangePeroid = peroid;
        if (isLooping) {
            cancelLooping();
            beginLoop();
        }
        return this;
    }


    public BannerView setIndicatorActiveColor(@ColorInt int color) {
        this.indicatorActiveColor = color;
        if (indicators != null && indicators.size() > 0) {
            int size = items.size();
            for (int i = 0; i < size; i++) {
                indicators.get(i).setActiveColor(color);
                if (hasInitialized) {
                    ((IndicatorView) mIndicatorContainer.getChildAt(i)).setActiveColor(color);
                }
            }
        }
        return this;
    }

    public BannerView setIndicatorInactiveColor(@ColorInt int color) {
        this.indicatorInactiveColor = color;
        int size = indicators.size();
        if (indicators != null && size > 0) {
            for (int i = 0; i < size; i++) {
                indicators.get(i).setInactiveColor(color);
                //已初始化
                if (hasInitialized) {
                    ((IndicatorView) mIndicatorContainer.getChildAt(i)).setInactiveColor(color);
                }
            }
        }
        return this;
    }


    //解决点击滑动手动切页问题
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP) {
            beginLoop();
        } else if (action == MotionEvent.ACTION_DOWN) {
            cancelLooping();
        }
        return super.dispatchTouchEvent(ev);
    }

    class BannerAdapter extends PagerAdapter {

        private List<BannerItem> datas;

        public BannerAdapter() {
            this.datas = datas;
        }

        public void setItems(List<BannerItem> items) {
            this.datas = items;
            notifyDataSetChanged();
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
            if (datas.size() == 0) {
                return null;
            }
            int pos = position % datas.size();
            final BannerItem item = datas.get(pos);
            View bannerItemView = getBannerItemView(item);
            if (bannerItemView.getParent() == null) {
                container.addView(bannerItemView);
                if (item.getAction() != null) {
                    bannerItemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.getAction().onAction(item);
                        }
                    });
                }
            }
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
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setImageResource(item.getDrawable());
                bannerItemView = view;
            } else {
                throw new IllegalArgumentException("The BannerItem doesn't have content.");
            }
            return bannerItemView;
        }

        public void addItem(BannerItem item) {
            datas.add(item);
            notifyDataSetChanged();
        }

    }
}

