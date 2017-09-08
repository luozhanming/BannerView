# BannerView
Android实用广告轮播控件

<img src="https://github.com/luozhanming/BannerView/blob/master/mygif.gif" width="240" height="360" />

## 特性
* 支持以特定时间间隔自动循环轮播的功能。
* 多种广告展示模式。
* 提供二次加载方法。
* 支持包装实体类。
* 支持为广告提供点击动作。
* 广告类型支持View类，图片等

## Usage
1.在layout文件中添加控件
```xml
 <com.zhanming.bannerview.BannerView
        android:id="@+id/bannerView"
        android:layout_width="match_parent"
        android:layout_height="160dp"
        />
```

2.在代码中初始化控件
```Java
        bannerView = (BannerView) findViewById(R.id.bannerView);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("This is a textViewPage");

        bannerView.addItem(new BannerItem<Cat>(R.mipmap.timg, new BannerAction() {
            @Override
            public void onAction(BannerItem item) {
                Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Cat("美短", 5)))
                .addItem(new BannerItem<Cat>(textView, null, null))
                .addItem(new BannerItem<Cat>(R.mipmap.timg2, new BannerAction() {
                    @Override
                    public void onAction(BannerItem item) {
                        Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Cat("中华田园", 7)))
                .setChangePeroid(3000)
                //    .setIndicatorActiveColor(Color.BLUE)
                //     .setIndicatorInactiveColor(Color.RED)
                .initialize();
```

3.当有新的广告加载时，请参考以下代码
```Java
        bannerView.addItem(new BannerItem<Cat>(R.mipmap.timg, new BannerAction() {
            @Override
            public void onAction(BannerItem item) {
                Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Cat("美短", 5)))
                .addItem(new BannerItem<Cat>(textView, null, null))
                .addItem(new BannerItem<Cat>(R.mipmap.timg2, new BannerAction() {
                    @Override
                    public void onAction(BannerItem item) {
                        Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Cat("中华田园", 7)))
                .refresh();
```
