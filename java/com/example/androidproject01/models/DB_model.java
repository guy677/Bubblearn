package com.example.androidproject01.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidproject01.Controller.RealTimeRepository;


import java.util.ArrayList;
import java.util.HashMap;


public class DB_model extends AndroidViewModel {

    private RealTimeRepository realTimeRepository;

    public DB_model(@NonNull Application application) {
        super(application);
        realTimeRepository = new RealTimeRepository("Members");
    }
    public void setRealTimeRepository(String child){
        realTimeRepository = new RealTimeRepository(child);
    }

    public void addQuizToMember(String userID, QuizToDB quiz){
        this.realTimeRepository.addQuizToMember(userID,quiz);
    }
    public void getUserQuizzes(String userID,retrieveDataRealTime r)
    {
        realTimeRepository.getUserQuizzes(userID,r);
    }

    public void addStatsToMember(String userID, UserStats stats){
        this.realTimeRepository.addStatsToMember(userID,stats);
    }

    public void getUsersStats(String userID ,retrieveDataRealTime r){ realTimeRepository.getUserStats(userID,r);}

    public void addCategories(HashMap<String, Integer> categoryList){
        this.realTimeRepository.addCategories(categoryList);
    }

    public void getCategories(retrieveDataRealTime r){
        realTimeRepository.getCategories(r);
    }


    public void getUserSetting(String uid, retrieveDataRealTime retrieveDataRealTime) {
        realTimeRepository.getUserSetting(uid,retrieveDataRealTime);
    }

    public void updateUser(User user) {
        realTimeRepository.updateUser(user);
    }



    public void addLearningToUser(String userID, ArrayList<Integer> questionIdToLEARN) {
        realTimeRepository.addLearningToUser(userID,questionIdToLEARN);
    }

    public void getUserLearningList(String userID, retrieveDataRealTime r) {
        realTimeRepository.getUserLearningList(userID,r);
    }

    public void removeFromLearnUser(String userID, ArrayList<Integer> questionToRemove) {
        realTimeRepository.removeFromLearnUser(userID,questionToRemove);
    }


    //For testing purposes
    //public DatabaseReference gerRoot(){return realTimeRepository.getUsersRef();}

}
