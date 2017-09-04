package com.zhanming.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
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
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
    public static final int GRAVITY_CENTER = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRAVITY_LEFT, GRAVITY_RIGHT, GRAVITY_CENTER})
    public @interface Gravity {

    }

    public static final int MODE_NOTITLE_NUM = 1;
    public static final int MODE_NOTITLE_INDICATOR = 2;
    public static final int MODE_TITLE_NUM = 3;
    public static final int MODE_TITLE_INDICATORS = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_NOTITLE_NUM, MODE_NOTITLE_INDICATOR, MODE_TITLE_NUM, MODE_TITLE_INDICATORS})
    public @interface Mode {

    }

    //控件成员
    private ViewPager mPager;
    private LinearLayout mIndicatorContainer1;
    private TextView tv_page1;
    private FrameLayout container_titleNum;
    private TextView tv_pageState2;
    private TextView tv_title1;
    private FrameLayout container_titleIndicator;
    private TextView tv_title2;
    private LinearLayout mIndicatorContainer2;

    private List<BannerItem> items;
    private List<IndicatorView> indicators;
    //临时items，负责二次加载刷新ViewPager
    private List<BannerItem> tempItems;

    private int defaultActiveColor = Color.RED;
    private int indicatorActiveColor = -1;
    private int defaultInActiveColor = Color.GREEN;
    private int indicatorInactiveColor = -1;
    private int indicatorsGravity;
    private static final int DEFAULT_PEROID = 1000;
    private int mChangePeroid;
    private boolean canLoop;
    private boolean isLooping;
    private boolean hasInitialized = false;
    private Timer mLoopTimer;
    private BannerAdapter mBannerAdapter;
    private ImageLoader mLoader;

    private int mode;

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
        mIndicatorContainer1 = (LinearLayout) parentView.findViewById(R.id.indicators);
        tv_page1 = (TextView) parentView.findViewById(R.id.container_modeWithNoTitleNum);
        container_titleNum = (FrameLayout) parentView.findViewById(R.id.container_modeWithTitleNum);
        tv_pageState2 = (TextView) parentView.findViewById(R.id.pageNum);
        tv_title1 = (TextView) parentView.findViewById(R.id.bannerTitle);
        container_titleIndicator = (FrameLayout) parentView.findViewById(R.id.container_modeWithTitleIndicators);
        mIndicatorContainer2 = (LinearLayout) parentView.findViewById(R.id.indicators2);
        tv_title2 = (TextView) parentView.findViewById(R.id.bannerTitle2);
        indicators = new ArrayList<>();
        items = new ArrayList<>();
        tempItems = new ArrayList<>();
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "position = " + position + ",positionOffset = " + positionOffset + ",positionOffsetPixels = " + positionOffsetPixels);
                //可加入指示器动画
            }

            @Override
            public void onPageSelected(int position) {
                //设置指示器
                mCurrentPosition = position % items.size();
                changePage();
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPager.addOnPageChangeListener(pageChangeListener);
    }


    //可以适当加点动画
    private void changePage() {
        int size = items.size();
        switch (mode) {
            case MODE_NOTITLE_NUM:
                tv_page1.setText((mCurrentPosition + 1) + "/" + size);
                break;
            case MODE_NOTITLE_INDICATOR:
                for (int i = 0; i < size; i++) {
                    if (mCurrentPosition == i) {
                        indicators.get(i).setState(IndicatorView.ACTIVE);
                    } else {
                        indicators.get(i).setState(IndicatorView.INACTIVE);
                    }
                }
                break;
            case MODE_TITLE_NUM:
                tv_pageState2.setText((mCurrentPosition + 1) + "/" + size);
                String title = items.get(mCurrentPosition).getTitle();
                if (title == null) {
                    title = "页面" + mCurrentPosition;
                }
                tv_title1.setText(title);
                break;
            case MODE_TITLE_INDICATORS:
                String title1 = items.get(mCurrentPosition).getTitle();
                if (title1 == null) {
                    title1 = "页面" + mCurrentPosition;
                }
                tv_title2.setText(title1);
                for (int i = 0; i < size; i++) {
                    if (mCurrentPosition == i) {
                        indicators.get(i).setState(IndicatorView.ACTIVE);
                    } else {
                        indicators.get(i).setState(IndicatorView.INACTIVE);
                    }
                }
                break;
        }
    }


    private void initWithMode() {
        int size = items.size();
        switch (mode) {
            case MODE_NOTITLE_NUM:
                tv_page1.setVisibility(View.VISIBLE);
                mIndicatorContainer1.setVisibility(View.GONE);
                container_titleNum.setVisibility(View.GONE);
                container_titleIndicator.setVisibility(View.GONE);
                break;
            case MODE_NOTITLE_INDICATOR:
                mIndicatorContainer1.setVisibility(View.VISIBLE);
                tv_page1.setVisibility(View.GONE);
                container_titleNum.setVisibility(View.GONE);
                container_titleIndicator.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    addIndicatorToContainer(i);
                }
                break;
            case MODE_TITLE_NUM:
                container_titleNum.setVisibility(View.VISIBLE);
                tv_page1.setVisibility(View.GONE);
                container_titleIndicator.setVisibility(View.GONE);
                mIndicatorContainer1.setVisibility(View.GONE);
                break;
            case MODE_TITLE_INDICATORS:
                container_titleIndicator.setVisibility(View.VISIBLE);
                mIndicatorContainer1.setVisibility(View.GONE);
                tv_page1.setVisibility(View.GONE);
                container_titleNum.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    addIndicatorToContainer(i);
                }
                break;
        }
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
        indicatorsGravity = typedArray.getInt(R.styleable.BannerView_banner_indicatorsGravity, GRAVITY_CENTER);
        mode = typedArray.getInt(R.styleable.BannerView_banner_mode, MODE_NOTITLE_NUM);
        typedArray.recycle();
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
        if (mode == MODE_NOTITLE_INDICATOR) {
            mIndicatorContainer1.addView(indicator);
        } else if (mode == MODE_TITLE_INDICATORS) {
            mIndicatorContainer2.addView(indicator);
        }
        indicators.add(indicator);
    }

    /**
     * 开始轮播
     */
    private void beginLoop() {
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


    private void resetBanner() {
        cancelLooping();
        indicators.clear();
        mIndicatorContainer1.removeAllViews();
        mIndicatorContainer2.removeAllViews();
        this.items.clear();
        mBannerAdapter.notifyDataSetChanged();
    }


    public void initialize() throws IllegalAccessException {
        if (items.size() == 0) {
            throw new IllegalAccessException("Invoke addItem method before initialize.");
        }
        if (items.size() != 0) {
            mBannerAdapter.setItems(items);
        }
        initWithMode();
        //实现左右循环
        mPager.setCurrentItem(mCurrentPosition);
        changePage();
        beginLoop();
        hasInitialized = true;
    }


    /**
     * 刷新Banner
     */
    public void refresh() {
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
        changePage();
        beginLoop();
    }


    public BannerView setImageLoader(ImageLoader loader) {
        this.mLoader = loader;
        return this;
    }

    public BannerView addItem(BannerItem item) {
        if (hasInitialized) {
            tempItems.add(item);
        } else {
            items.add(item);
        }
        return this;
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
                    if(mode==MODE_NOTITLE_INDICATOR){
                        ((IndicatorView) mIndicatorContainer1.getChildAt(i)).setActiveColor(color);
                    }else{
                        ((IndicatorView) mIndicatorContainer2.getChildAt(i)).setActiveColor(color);
                    }
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
                    if(mode==MODE_NOTITLE_INDICATOR){
                        ((IndicatorView) mIndicatorContainer1.getChildAt(i)).setActiveColor(color);
                    }else{
                        ((IndicatorView) mIndicatorContainer2.getChildAt(i)).setActiveColor(color);
                    }
                }
            }
        }
        return this;
    }

    /**
     * 设置是否可轮播
     */
    public BannerView setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        if (hasInitialized) {
            if (canLoop) {
                beginLoop();
            } else {
                cancelLooping();
            }
        }
        return this;
    }


    public List<BannerItem> getItems() {
        return this.items;
    }


    public void setMode(@Mode int mode) {
        this.mode = mode;
        if(hasInitialized){
            mIndicatorContainer1.removeAllViews();
            mIndicatorContainer2.removeAllViews();
            indicators.clear();
            initWithMode();
            changePage();
        }
    }

    public int getMode() {
        return mode;
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


    //解决点击滑动手动切页问题
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP) {
            if (canLoop) {
                beginLoop();
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            cancelLooping();
        }
        return super.dispatchTouchEvent(ev);
    }

    private class BannerAdapter extends PagerAdapter {

        private List<BannerItem> datas;

        public BannerAdapter() {
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
            if (datas == null || datas.size() == 0) {
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

        //通過BannerItem返回Banner显示的View
        private View getBannerItemView(BannerItem item) {
            View bannerItemView = null;
            if (item.getView() != null) {
                bannerItemView = item.getView();
            } else if (item.getDrawable() != -1) {
                ImageView view = new ImageView(getContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (mLoader != null) {
                    mLoader.displayImage(getContext(), item.getDrawable(), view);
                } else {
                    view.setImageResource(item.getDrawable());
                }
                bannerItemView = view;
            } else if (item.getImgUrl() != null) {
                ImageView view = new ImageView(getContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (mLoader != null) {
                    mLoader.displayImage(getContext(), item.getImgUrl(), view);
                }
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

