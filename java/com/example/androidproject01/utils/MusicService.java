package com.example.androidproject01.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import android.widget.Toast;

import com.example.androidproject01.R;

public class MusicService extends Service {
    private MediaPlayer myPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Music On", Toast.LENGTH_LONG).show();
        myPlayer = MediaPlayer.create(this, R.raw.sound);
    }
    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Music On", Toast.LENGTH_LONG).show();
        if (!myPlayer.isPlaying())
            myPlayer.start();
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Music Off", Toast.LENGTH_LONG).show();
        myPlayer.stop();
        myPlayer.release();
    }
    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        int x;
        if (!myPlayer.isPlaying()) {myPlayer.start();
         x =1;}
        else  x=0;
        return x;
    }



}

