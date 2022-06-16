package com.efjerryyang.defendthefrontline.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.efjerryyang.defendthefrontline.R;

import java.util.ArrayList;
import java.util.List;

public class FinishActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "FinishActivity";
    public static int screenWidth;
    public static int screenHeight;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenSize();
        setContentView(R.layout.activity_finish);
//        data.add("abc");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ListView listView = findViewById(R.id.rank);
        listView.setAdapter(new MyAdapter(data,this));
        Button replayButton = findViewById(R.id.replay);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
        Button quitButton = findViewById(R.id.quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:游戏退出
            }
        });


    }

    // ppt 第二讲
    public void getScreenSize() {
        screenHeight = Config.screenHeight;
        screenWidth = Config.screenWidth;
    }
}
