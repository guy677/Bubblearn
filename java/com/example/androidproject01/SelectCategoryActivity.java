package com.example.androidproject01;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.DB_model;
import com.example.androidproject01.models.User;
import com.example.androidproject01.models.retrieveDataRealTime;
import com.example.androidproject01.utils.MusicService;
import com.example.androidproject01.utils.baseView;
import com.example.androidproject01.utils.MyHelpers;
import com.google.firebase.database.DatabaseError;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;



import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;




public class SelectCategoryActivity extends baseView {
    public final static String EXTRA_QuestionsList = "com.example.androidproject01.SelectCategoryActivity.QuestionList";
    private ArrayList<String> id =new ArrayList<String>();
    private BubblePicker picker;
    private HashMap<String,Integer> categoryList;
    //private MediaPlayer song;
    private TextView btnStart ;
    private DB_model db_model;
    private int[] bubblePickers = new int[]{
            Color.parseColor("#1E8395"),
            Color.parseColor("#F8AE17"),
            Color.parseColor("#31799F"),
            Color.parseColor("#E49753"),
            Color.parseColor("#BE5900")};
    private String numberOfQuestions;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        /*
        *hide system bar and show full screen
         */
        db_model = new ViewModelProvider(this).get(DB_model.class);
        db_model.setRealTimeRepository("Members");
        this.HideUI();
        /*
        Menu Setting!!
         */
        //Menu Setting!!!
        Button lang = findViewById(R.id.btnLanguage);
        setLanguageState(lang.getText().toString());
        int[] imageResources = new int[]{
                R.drawable.learn,
                R.drawable.ic_pofile,
                R.drawable.ic_stats,
                R.drawable.logout,
        };
        HashMap<Integer,String[]> menuStrings = new HashMap<>();
        String[] MenuHeadString = new String[]{
                getText(R.string.Learn).toString(),
                getText(R.string.Profile).toString(),
                getText(R.string.Stats).toString(),
                getText(R.string.LogOut).toString(),
        };
        String[] MenuSubString = new String[]{
                getText(R.string.LearnSub).toString(),
                getText(R.string.ProfileSub).toString(),
                getText(R.string.StatsSub).toString(),
                getText(R.string.LogOutSub).toString(),
        };
        List<Intent> menuIntent = new LinkedList<>();
        menuIntent.add(new Intent(this,Learn .class));
        menuIntent.add(new Intent(this,ProfileActivity .class));
        menuIntent.add(new Intent(this,StatsActivity .class));
        menuIntent.add(new Intent(this,LogIn .class));
        /*End Of Menu Setting */
        initAuthViewModel();
        btnStart = findViewById(R.id.btnStart);
        TextView helloUser = findViewById(R.id.txtHello);
        db_model.getUserSetting(authViewModel.getUser().getUid(), new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                User user = (User) data;
                helloUser.setText(helloUser.getText().toString()+ " " + user.getName());
                numberOfQuestions = user.getPreferNumberOfQuestions();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        picker = findViewById(R.id.picker);
        picker.setBubbleSize(75);
        picker.setBackground(Color.parseColor("#0B3148"));
        picker.setZOrderOnTop(false);
        this.SetMenu(MenuHeadString, menuStrings, MenuSubString, imageResources, menuIntent);
        setBubbles();
    }

    private void setBubbles() {
        if(this.getLanguageState().equals("עב")) {
            categoryList = MyHelpers.categoryList();
        }
        else{
            categoryList = MyHelpers.categoryListHe();
        }
        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return categoryList.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item= new PickerItem();
                item.setTitle((String)categoryList.keySet().toArray()[i]);
                Random rnd = new Random();
                int randomIndex = rnd.nextInt(bubblePickers.length);
                item.setColor(bubblePickers[randomIndex]);
                item.setTextColor(Color.WHITE);
                return item;
            }
        });
        picker.setListener(new BubblePickerListener() {

            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                if(id.size()>5){
                    id.add(categoryList.get(pickerItem.getTitle()).toString());
                    Toast.makeText(SelectCategoryActivity.this,R.string.MoreThan5,Toast.LENGTH_SHORT).show();
                }
                else {
                    id.add(Objects.requireNonNull(categoryList.get(pickerItem.getTitle())).toString());
                    popStartTriviaButton();
                }
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {
                id.remove(Objects.requireNonNull(Objects.requireNonNull(categoryList.get(pickerItem.getTitle())).toString()));
                if(id.size()<=5){
                    unSelectBubble();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void unSelectBubble() {
        if(id.size()<5) {
            btnStart.setText(id.size() + "/5");
        }
    }


    @SuppressLint("SetTextI18n")
    private void popStartTriviaButton() {
        btnStart.setVisibility(View.VISIBLE);
        if(id.size()<5) {
            btnStart.setText(id.size() + "/5");
        }
        else{
            btnStart.setText(R.string.LetsBegin);
        }

    }

    public void StartTrivia(View view) {
        if(id.size()>5){
            Toast.makeText(SelectCategoryActivity.this,R.string.MoreThan5,Toast.LENGTH_SHORT).show();
        }
        else{
            if(id.size()==0) {
                Toast.makeText(SelectCategoryActivity.this,R.string.chooseatleastone,Toast.LENGTH_SHORT).show(); }
            else {
                if(Integer.parseInt(numberOfQuestions)/id.size()>10){
                    Toast.makeText(SelectCategoryActivity.this,R.string.needMore,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent goToTrivia = new Intent(this, TriviaActivity.class);
                    goToTrivia.putExtra(EXTRA_QuestionsList, id);
                    startActivityForResult(goToTrivia, 1);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        id.clear();
        btnStart.setVisibility(View.INVISIBLE);
        picker.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db_model.getUserSetting(authViewModel.getUser().getUid(), new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                User user = (User) data;
                if(user.getMusicOn().equals("true")) {
                    startService(new Intent(SelectCategoryActivity.this, MusicService.class));}
                else stopService(new Intent(SelectCategoryActivity.this,MusicService.class));
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        setBubbles();
    }

    @Override
    public void onOptionsItemSelected(View view) {
        super.onOptionsItemSelected(view);
        finish();
        startActivity(getIntent());

    }
    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
}