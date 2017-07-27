package com.zhanming.sample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhanming.bannerview.BannerAction;
import com.zhanming.bannerview.BannerItem;
import com.zhanming.bannerview.BannerView;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.bannerView);

        bannerView.addItem(new BannerItem<Cat>(R.mipmap.timg, new BannerAction() {
            @Override
            public void onAction(BannerItem item) {
                Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Cat("美短", 5)))
                .addItem(new BannerItem<Cat>(R.mipmap.timg1, new BannerAction() {
                    @Override
                    public void onAction(BannerItem item) {
                        Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Cat("英短", 6)))
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


    }
}
