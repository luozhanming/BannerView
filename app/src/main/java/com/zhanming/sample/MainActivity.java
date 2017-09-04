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
    private Button btn1,btn2, btn3, btn4, btn5;
    private EditText editText;
    private int[] modes = {BannerView.MODE_NOTITLE_NUM,BannerView.MODE_NOTITLE_INDICATOR,BannerView.MODE_TITLE_NUM,BannerView.MODE_TITLE_INDICATORS};
    private  int currentMode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.bannerView);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("This is a textViewPage");

        try {
           bannerView.addItem(new BannerItem(R.mipmap.timg1,null,new Cat("中华猫",5),"中华猫"))
                   .addItem(new BannerItem("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1569462993,172008204&fm=5", new BannerAction() {
                       @Override
                       public void onAction(BannerItem item) {
                           Toast.makeText(MainActivity.this,((Cat)item.getContent()).toString(),Toast.LENGTH_SHORT).show();
                       }
                   }, new Cat("日本猫", 4), "日本猫"))
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
            case R.id.btn1:
                currentMode=(currentMode+1)%4;
                bannerView.setMode(modes[currentMode]);
                break;
            case R.id.btn2:
                bannerView.addItem(new BannerItem(R.mipmap.timg1,null,new Cat("中华猫",5),"中华猫"))
                        .addItem(new BannerItem("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1569462993,172008204&fm=5", new BannerAction() {
                            @Override
                            public void onAction(BannerItem item) {
                                Toast.makeText(MainActivity.this,((Cat)item.getContent()).toString(),Toast.LENGTH_SHORT).show();
                            }
                        }, new Cat("日本猫", 4), "日本猫"))
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
