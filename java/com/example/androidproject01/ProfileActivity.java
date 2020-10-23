package com.example.androidproject01;


import androidx.annotation.RequiresApi;

import androidx.lifecycle.ViewModelProvider;


import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import android.widget.Toast;

import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.DB_model;
import com.example.androidproject01.models.User;
import com.example.androidproject01.models.retrieveDataRealTime;
import com.example.androidproject01.utils.baseView;

import com.google.firebase.database.DatabaseError;


import static android.content.ContentValues.TAG;
import static android.widget.Toast.*;

public class ProfileActivity extends baseView {

    private AuthViewModel authViewModel;
    private DB_model db_model;
    private User user;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);
        initAuthViewModel();
        this.HideUI();
        db_model = new ViewModelProvider(this).get(DB_model.class);
        db_model.setRealTimeRepository("Members");
        db_model.getUserSetting(authViewModel.getUser().getUid(), new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                user = (User) data;
                EditText textView = findViewById(R.id.editTextUserName);
                textView.setText(user.getName());
                EditText numOfQuestions = findViewById(R.id.num);
                numOfQuestions.setText(user.getPreferNumberOfQuestions());
                Switch music = findViewById(R.id.music_switch);
                if(user.getMusicOn().equals("true")){
                    music.setChecked(true);
                }
                else {
                    music.setChecked(false);
                }
                checkButtons();

            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });


    }

    private void checkButtons() {
        RadioGroup check = findViewById(R.id.SecondsRDIO);
        switch (user.getSeconds()){
            case "15":
            {
                check.check(R.id.rdio15SC);
                break;
            }
            case "30":
            {
                check.check(R.id.rdio30SC);
                break;
            }
        }
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    private void validateNumOfQuestions(){
        EditText numOfQuestions = findViewById(R.id.num);
        String str = numOfQuestions.getText().toString();
        boolean flag= true;
        int currentNum;
        if(str!=null){
            try {
                 currentNum = Integer.parseInt(str);
            }catch (Exception e){
                currentNum = 15;
                flag = false;
                Log.d(TAG,e.toString());
                Toast.makeText(getApplicationContext(),"Empty Filed/ Wrong input - default is 15", LENGTH_SHORT).show();
                numOfQuestions.setText("15");
            }
            if(currentNum < 5 && flag){
                Toast.makeText(getApplicationContext(),"Minimum Allowed number Of Questions is 5", LENGTH_SHORT).show();
                numOfQuestions.setText("5");
            }
            else if (currentNum >50){
                Toast.makeText(getApplicationContext(),"Maximum Allowed number Of Questions is 50", LENGTH_SHORT).show();
                numOfQuestions.setText("50");}
        }




    }

    public void saveData(View view) {
        validateNumOfQuestions();
        EditText name = findViewById(R.id.editTextUserName);
        EditText numOfQuestions = findViewById(R.id.num);
        Switch music = findViewById(R.id.music_switch);
        user.setName(name.getText().toString());
        user.setPreferNumberOfQuestions(numOfQuestions.getText().toString());
        user.setMusicOn(music.isChecked()?"true":"false");
        setDiffandSec();
        db_model.updateUser(user);
        Intent goBack = new Intent(this,SelectCategoryActivity.class);
        startActivity(goBack);
        finish();
    }

    private void setDiffandSec() {
        RadioGroup seconds = findViewById(R.id.SecondsRDIO);
        RadioButton check =(RadioButton) findViewById(seconds.getCheckedRadioButtonId());
        user.setSeconds(check.getText().toString());

    }
}