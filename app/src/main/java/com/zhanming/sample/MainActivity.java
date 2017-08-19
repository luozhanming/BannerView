package com.zhanming.sample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhanming.bannerview.BannerAction;
import com.zhanming.bannerview.BannerItem;
import com.zhanming.bannerview.BannerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BannerView bannerView;
    private Button  btn2, btn3, btn4, btn5;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.bannerView);

        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("This is a textViewPage");

        try {
            bannerView.addItem(new BannerItem<Cat>(R.mipmap.timg, new BannerAction() {
                @Override
                public void onAction(BannerItem item) {
                    Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();

                }
            }, new Cat("美短", 5)))
                    .addItem(new BannerItem<Cat>("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2247692397,1189743173&fm=5", null, new Cat("布偶猫",5)))
                    .addItem(new BannerItem<Cat>(R.mipmap.timg2, new BannerAction() {
                        @Override
                        public void onAction(BannerItem item) {
                            Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Cat("中华田园", 7)))
                    .setChangePeroid(3000)
                    .setImageLoader(new PicassoLoader())
                    //    .setIndicatorActiveColor(Color.BLUE)
                    //     .setIndicatorInactiveColor(Color.RED)
                    .initialize();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    boolean colorFlag;


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn2:
                bannerView.addItem(new BannerItem<Cat>(R.mipmap.timg2, new BannerAction() {
                    @Override
                    public void onAction(BannerItem item) {
                        Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Cat("中华田园", 7)))
                        .addItem(new BannerItem<Cat>(R.mipmap.timg, new BannerAction() {
                            @Override
                            public void onAction(BannerItem item) {
                                Toast.makeText(MainActivity.this, ((Cat) item.getContent()).toString(), Toast.LENGTH_SHORT).show();

                            }
                        }, new Cat("美短", 5)))
                        .refresh();
                break;
            case R.id.btn3:
                if (colorFlag) {
                    bannerView.setIndicatorInactiveColor(Color.RED);
                    bannerView.setIndicatorActiveColor(Color.GREEN);
                    colorFlag = false;
                } else {
                    bannerView.setIndicatorInactiveColor(Color.GREEN);
                    bannerView.setIndicatorActiveColor(Color.RED);
                    colorFlag = true;
                }
                break;
            case R.id.btn4:
                String s = editText.getText().toString();
                int duration = Integer.valueOf(s);
                bannerView.setChangePeroid(duration);
                break;
            case R.id.btn5:
                bannerView.setCanLoop(false);
                break;
        }
    }
}
