package com.efjerryyang.defendthefrontline.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.efjerryyang.defendthefrontline.R;
import com.efjerryyang.defendthefrontline.game.AbstractGame;
import com.efjerryyang.defendthefrontline.game.DifficultGame;
import com.efjerryyang.defendthefrontline.game.MediumGame;
import com.efjerryyang.defendthefrontline.game.SimpleGame;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private AbstractGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImages();
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        int gameIndex = intent.getIntExtra("game_index", 0);
        Log.d(TAG, "gameIndex: " + gameIndex);
        Config.setGameLevel(gameIndex);

        switch (gameIndex) {
            case 1:
                game = new SimpleGame(this, Config.getGameLevel(), Config.getEnableAudio());
                break;
            case 2:
                game = new MediumGame(this, Config.getGameLevel(), Config.getEnableAudio());
                break;
            case 4:
                game = new DifficultGame(this, Config.getGameLevel(), Config.getEnableAudio());
                break;
            default:
                game = new SimpleGame(this, 1, true);
                break;
        }
        setContentView(game);

    }

    public void loadImages() {
        ImageManager.BACKGROUND_IMAGE_LEVEL1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg1);
        ImageManager.BACKGROUND_IMAGE_LEVEL2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        ImageManager.BACKGROUND_IMAGE_LEVEL3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        ImageManager.BACKGROUND_IMAGE_LEVEL4 = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);
        ImageManager.BACKGROUND_IMAGE_LEVEL5 = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.BULLET_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);
        ImageManager.SHIELD_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.shield);

    }


}