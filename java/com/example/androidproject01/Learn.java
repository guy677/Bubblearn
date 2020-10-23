package com.example.androidproject01;

import androidx.annotation.RequiresApi;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.ContextThemeWrapper;
import android.view.Gravity;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.DB_learn;
import com.example.androidproject01.models.DB_model;
import com.example.androidproject01.models.LearnToDB;

import com.example.androidproject01.models.retrieveDataRealTime;
import com.example.androidproject01.utils.baseView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Learn extends baseView {
    private AuthViewModel authViewModel;
    private DB_model db_model;
    private String userID;
    private HashMap<Integer, Integer> questionsID;
    private DB_learn db_learn;
    private HashMap<Integer,Integer> cardViews = new HashMap<>();
    private ArrayList<Integer> questionToRemove = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initAuthViewModel();
        this.HideUI();
        ConstraintLayout main = findViewById(R.id.mainLayOut);
        main.setBackgroundColor(Color.parseColor("#0B3148"));

        int[] imageResources = new int[]{
                R.drawable.main,
                R.drawable.logout,
        };
        HashMap<Integer,String[]> menuStrings = new HashMap<>();
        String[] MenuHeadString = new String[]{
                getText(R.string.backToMenu).toString(),
                getText(R.string.LogOut).toString()
        };
        String[] MenuSubString = new String[]{
                getText(R.string.backToMenuSUB).toString(),
                getText(R.string.LogOutSub).toString()
        };
        List<Intent> menuIntent = new LinkedList<>();
        menuIntent.add(new Intent(this, SelectCategoryActivity.class));
        menuIntent.add(new Intent(this, LogIn.class));

        this.SetMenu(MenuHeadString,menuStrings,MenuSubString,imageResources,menuIntent);
        db_model = new ViewModelProvider(this).get(DB_model.class);
        db_learn = new ViewModelProvider(this).get(DB_learn.class);
        userID = authViewModel.getUser().getUid();
        db_model.getUserLearningList(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                questionsID = (HashMap<Integer,Integer>) data;
                for(Map.Entry<Integer, Integer> entry : questionsID.entrySet()){
                    cardViews.put(entry.getValue(), entry.getKey());
                }
                if(questionsID!= null){
                    getCards();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    private void getCards() {
        LinearLayout cardsLayout = findViewById(R.id.cardsLayout);
        cardsLayout.setBackgroundColor(Color.parseColor("#0B3148"));
        for(Integer id:questionsID.keySet()){
            db_learn.getQuestionFromLearn(questionsID.get(id), new retrieveDataRealTime() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Object data) {
                    if(data!= null) {
                        LearnToDB learn = (LearnToDB) data;
                        CardView card = new CardView(cardsLayout.getContext());
                        card.setClickable(true);
                        LinearLayout cardInner = new LinearLayout(new ContextThemeWrapper(cardsLayout.getContext(), ContextThemeWrapper.BIND_AUTO_CREATE));
                        cardInner.setOrientation(LinearLayout.VERTICAL);

                        TextView staticText = new TextView(card.getContext());
                        TextView staticAnswer = new TextView(card.getContext());
                        TextView staticUrl = new TextView(card.getContext());

                        staticText.setGravity(Gravity.CENTER_HORIZONTAL);
                        staticUrl.setGravity(Gravity.CENTER_HORIZONTAL);
                        staticAnswer.setGravity(Gravity.CENTER_HORIZONTAL);

                        staticText.setText(R.string.staticText);
                        staticText.setTextSize(20);
                        staticText.setTextColor(Color.BLACK);

                        staticAnswer.setText(R.string.staticAnswer);
                        staticAnswer.setTextSize(20);
                        staticAnswer.setTextColor(Color.BLACK);


                        staticUrl.setText(R.string.staticLink);
                        staticUrl.setTextSize(20);
                        staticUrl.setTextColor(Color.BLACK);


                        TextView title = new TextView(card.getContext());
                        TextView Answer = new TextView(card.getContext());
                        TextView Url = new TextView(card.getContext());
                        title.setTextSize(18);
                        Answer.setTextSize(18);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(40, 20, 40, 20);

                        card.setLayoutParams(params);
                        title.setLayoutParams(params);
                        Answer.setLayoutParams(params);
                        Url.setLayoutParams(params);

                        title.setText(learn.getQuestion());
                        Answer.setText(learn.getShortAnswer());
                        Url.setText(learn.getUrlToLearnMore());
                        Linkify.addLinks(Url,Linkify.WEB_URLS);

                        ImageView checked = new ImageView(card.getContext());
                        checked.setImageResource(R.drawable.check_learn);

                        cardInner.addView(staticText);
                        cardInner.addView(title);
                        cardInner.addView(staticAnswer);
                        cardInner.addView(Answer);
                        cardInner.addView(staticUrl);
                        cardInner.addView(Url);
                        card.addView(cardInner);

                        card.setOnClickListener(v -> {
                            Handler delay = new Handler();
                            int[] arr = new int[]{1,-1};
                            int whichWay = new Random().nextInt(arr.length);
                            card.animate().translationX(arr[whichWay]*2000);
                            card.addView(checked);
                            LinearLayout lyout = (LinearLayout)card.getChildAt(0);
                            TextView question = (TextView) lyout.getChildAt(1);
                            String q = question.getText().toString();
                            Integer questionTo = cardViews.get(q.hashCode());
                            questionToRemove.add(questionTo);
                            delay.postDelayed(()->cardsLayout.removeView(card),1000);
                        });
                        cardsLayout.addView(card);

                    }
                }

                @Override
                public void onFailed(DatabaseError databaseError) {

                }
            });
        }
    }
    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db_model.removeFromLearnUser(userID,questionToRemove);
    }
}