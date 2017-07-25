# BannerView
This Android library helps user to use ViewPager with carousel.

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
                .initialize();
```


