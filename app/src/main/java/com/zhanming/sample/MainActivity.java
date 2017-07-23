package com.zhanming.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhanming.bannerview.BannerItem;
import com.zhanming.bannerview.BannerView;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.bannerView);

        bannerView.addItem(new BannerItem(R.mipmap.ic_launcher))
                .addItem(new BannerItem(android.R.mipmap.sym_def_app_icon))
                .addItem(new BannerItem(android.R.drawable.dialog_frame))
                .initialize();
    }
}
