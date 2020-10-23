package com.example.androidproject01.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidproject01.Controller.RealTimeRepository;

public class DB_learn extends AndroidViewModel {

    private RealTimeRepository realTimeRepository;

    public DB_learn(@NonNull Application application) {
        super(application);
        realTimeRepository = new RealTimeRepository("Learn");
    }

    public void getQuestionFromLearn(int qID, retrieveDataRealTime r){
        realTimeRepository.getQuestionFromLearn(qID,r);
    }


    public void addLearning(LearnToDB learn) {
        realTimeRepository.addLearning(learn);
    }
}
