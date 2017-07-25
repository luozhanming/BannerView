# BannerView
This Android library helps user to use ViewPager with carousel.

## Features
* The BannerView work like a ViewPager.
* The BannerView wraps the ViewPager and provides the simple method to use.
* The BannerView can automatically change the page in the regular period which you can set.
* Show the indicators to let you know which item is showing.
* Customize the indicators color or use the app theme color by default.

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

        bannerView.addItem(new BannerItem(R.mipmap.ic_launcher))
                .addItem(new BannerItem(android.R.mipmap.sym_def_app_icon))
                .addItem(new BannerItem(android.R.drawable.dialog_frame))
                .setIndicatorActiveColor(Color.Red)
                .setIndicatorIncactiveColor(Color.Green)
                .setChangePeroid(5000)
                .canLoop(true)
                .initialize();
```

