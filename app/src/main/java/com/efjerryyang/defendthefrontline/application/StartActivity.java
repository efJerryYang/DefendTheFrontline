package com.efjerryyang.defendthefrontline.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.efjerryyang.defendthefrontline.R;

public class StartActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "StartActivity";
    public static int screenWidth;
    public static int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenSize();
        setContentView(R.layout.activity_start);
        Button singleButton = findViewById(R.id.singleButton);
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
            }
        });
        Button onlineButton = findViewById(R.id.onlineButton);
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });

    }

    // ppt 第二讲
    public void getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        Log.i(TAG, "screenWidth: " + screenWidth);
        screenHeight = dm.heightPixels;
        Log.i(TAG, "screenHeight: " + screenHeight);
    }
}
