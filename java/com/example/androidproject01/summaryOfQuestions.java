package com.example.androidproject01;

import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.widget.TextView;
import android.widget.VideoView;

import com.example.androidproject01.utils.baseView;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class summaryOfQuestions extends baseView {
    private TextView correctNum;
    private TextView toatalNum;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_questions);
        VideoView videoView = findViewById(R.id.videoView);
        this.HideUI();
        String path = "android.resource://" + getPackageName() + "/" + R.raw.background;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setBackgroundColor(Color.parseColor("#0B3148") );
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
            Handler handler =new Handler();
            handler.postDelayed(()->videoView.setBackgroundColor(Color.TRANSPARENT),500);

        });
        //Menu Setting!!!
        int[] imageResources = new int[]{
                R.drawable.main,
                R.drawable.learn,
                R.drawable.ic_stats,
                R.drawable.logout,
        };
        HashMap<Integer,String[]> menuStrings = new HashMap<>();
        String[] MenuHeadString = new String[]{
                getText(R.string.backToMenu).toString(),
                getText(R.string.Learn).toString(),
                getText(R.string.Stats).toString(),
                getText(R.string.LogOut).toString()
        };
        String[] MenuSubString = new String[]{
                getText(R.string.backToMenuSUB).toString(),
                getText(R.string.LearnSub).toString(),
                getText(R.string.StatsSub).toString(),
                getText(R.string.LogOutSub).toString()
        };
        List<Intent> menuIntent = new LinkedList<>();
        menuIntent.add(new Intent(this, SelectCategoryActivity.class));
        menuIntent.add(new Intent(this, Learn.class));
        menuIntent.add(new Intent(this, StatsActivity.class));
        menuIntent.add(new Intent(this, LogIn.class));
        /*End Of Menu Setting */
        correctNum = findViewById(R.id.txtCorrect);
        toatalNum = findViewById(R.id.txtTotal);
        Intent intent = getIntent();
        int numOfCorrect = Integer.parseInt(intent.getStringExtra(TriviaActivity.EXTRA_NUBMER_OF_CORRECT));
        int numOfWrong = Integer.parseInt(intent.getStringExtra(TriviaActivity.EXTRA_NUBMER_OF_WRONG));
        correctNum.setText(Integer.toString(numOfCorrect));
        toatalNum.setText(Integer.toString(numOfCorrect+numOfWrong));
        this.SetMenu(MenuHeadString, menuStrings, MenuSubString, imageResources, menuIntent);
    }

}