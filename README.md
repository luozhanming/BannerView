# BannerView
This Android library helps user to use ViewPager with carousel.

## Features
* The BannerView wraps the ViewPager and provides the simple method to use.
* The BannerView can automatically change the page in the regular period which you can set.
* Show the indicators to let you know which item is showing.
* Customize the indicators color or use the app theme color by default.
* Wrap your entity object to the BannerItem.
* Provide the interface when you click the item to handle the item click event.

## Usage
1.Add the BannerView on the layout file.
```xml
 <com.zhanming.bannerview.BannerView
        android:id="@+id/bannerView"
        android:layout_width="match_parent"
        android:layout_height="160dp"
        />
```

2.Initialize the View on the code.
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

3.Refresh the Banner when you have loaded new datas.
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
