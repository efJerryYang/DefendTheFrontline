package com.efjerryyang.defendthefrontline.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.efjerryyang.defendthefrontline.R;

import java.io.IOException;

public class OnlineActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "OnlineActivity";
    public static int screenWidth;
    public static int screenHeight;
    public static GameClient gameClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenSize();
        setContentView(R.layout.activity_online);
        Button simpleButton = findViewById(R.id.simpleButtonOnline);

        simpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineActivity.this, OnlineGameActivity.class);
                intent.putExtra("game_index", 1);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        gameClient = new GameClient(Config.host, Config.port);
                    }
                };
                t.start();
                startActivity(intent);
            }
        });
        Button mediumButton = findViewById(R.id.mediumButtonOnline);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineActivity.this, OnlineGameActivity.class);
                intent.putExtra("game_index", 2);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        gameClient = new GameClient(Config.host, Config.port);
                    }
                };
                t.start();
                startActivity(intent);
            }
        });

        Button difficultButton = findViewById(R.id.difficultButtonOnline);
        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineActivity.this, OnlineGameActivity.class);
                intent.putExtra("game_index", 4);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        gameClient = new GameClient(Config.host, Config.port);
                    }
                };
                t.start();
                startActivity(intent);
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch audioSwitch = findViewById(R.id.audioSwitch);
        audioSwitch.setChecked(false);
        Config.setEnableAudio(false);
        audioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "audioOn: " + isChecked);
                Config.setEnableAudio(isChecked);
            }
        });

    }

    public void getScreenSize() {
        screenWidth = Config.screenWidth;
        screenHeight = Config.screenHeight;
    }
}