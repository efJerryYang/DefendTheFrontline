package com.efjerryyang.defendthefrontline.application;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.efjerryyang.defendthefrontline.R;

public class MusicService extends Service {

    private MediaPlayer bgm;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        playMusic();
        return super.onStartCommand(intent,flags,startId);
    }

    public void playMusic(){
        if(bgm == null){
            bgm = MediaPlayer.create(this, R.raw.bgm);
        }
        bgm.start();
    }

    public void pauseMusic(){
        if(bgm!=null && bgm.isPlaying()){
            bgm.pause();
        }
    }

    public void stopMusic(){
        if(bgm!=null){
            bgm.stop();
            bgm.reset();
            bgm.release();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        stopMusic();
    }

    @Override
    public IBinder onBind(Intent intent){
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
