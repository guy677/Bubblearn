package com.example.androidproject01.Controller;


import androidx.annotation.NonNull;

import com.example.androidproject01.models.LearnToDB;
import com.example.androidproject01.models.QuizToDB;
import com.example.androidproject01.models.User;
import com.example.androidproject01.models.UserStats;
import com.example.androidproject01.models.retrieveDataRealTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealTimeRepository {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef;

    public RealTimeRepository(String childToExcess){
        dataRef = database.getReference().child(childToExcess);
    }

    public void addQuizToMember(String userID, QuizToDB quiz) {
        dataRef.child(userID)
                .child("Quizzes")
                .child(quiz.getQuizID())
                .child("Correct")
                .setValue(quiz.getCorrect());
        dataRef.child(userID)
                .child("Quizzes")
                .child(quiz.getQuizID())
                .child("Wrong Answers")
                .setValue(quiz.getWrongQuestions());

    }

    public void getUserQuizzes(String Uid,retrieveDataRealTime r){
        DatabaseReference userRef = dataRef.child(Uid).child("Quizzes");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Object> list = (Map<String,Object>) snapshot.getValue();

                r.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void addCategories(HashMap<String, Integer> categoryList) {
        dataRef.setValue(categoryList);
    }

    public void getCategories(retrieveDataRealTime r) {
        r.onStart();
        dataRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        // Get user value

                        r.onSuccess((User) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        r.onFailed(error);
                    }
                });
    }

    public void addLearning(LearnToDB learn) {
        dataRef.child(Integer.toString(learn.getID())).setValue(learn);
    }

    public void getUserSetting(String Uid,retrieveDataRealTime r) {
        dataRef.child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        // Get user value
                        HashMap<String,HashMap<String,String>> getSettings = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                        HashMap<String,String> useHash = getSettings.get("Settings");
                        User user = new User(
                                useHash.get("uid"),
                                useHash.get("name"),
                                useHash.get("email"));
                        user.setDifficulty(useHash.get("difficulty"));
                        user.setSeconds(useHash.get("seconds"));
                        user.setPreferNumberOfQuestions((useHash.get("preferNumberOfQuestions")));
                        user.setMusicOn(useHash.get("musicOn"));
                        r.onSuccess(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        r.onFailed(error);
                    }
                });

    }

    public void updateUser(User user) {
        dataRef.child(user.getUid())
                .child("Settings")
                .setValue(user);
    }

    public void addStatsToMember(String userID, UserStats stats) {
        dataRef.child(userID)
                .child("Stats")
                .setValue(stats);
    }

    public void getQuestionFromLearn(int qID,retrieveDataRealTime r){
        DatabaseReference questionRef = dataRef.child(String.valueOf(qID));
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                r.onSuccess(snapshot.getValue(LearnToDB.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUserLearningList(String userID,retrieveDataRealTime r){
        DatabaseReference userRef = dataRef.child(userID).child("Learn");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Long> data = (ArrayList<Long>) snapshot.getValue();
                HashMap<Integer,Integer> LearningList = new HashMap<>();;
                if(data!= null){
                    for(Long i :data){
                        if(i!=null) {
                            LearningList.put(data.indexOf(i),Math.toIntExact(i));
                        }
                    }
                }
                r.onSuccess(LearningList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addLearningToUser(String userID, ArrayList<Integer> questionIdToLEARN) {
        dataRef.child(userID)
                .child("Learn")
                .setValue(questionIdToLEARN);
    }

    public void getUserStats(String userID, retrieveDataRealTime r) {
        DatabaseReference userRef = dataRef.child(userID).child("Stats");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                r.onSuccess(snapshot.getValue(UserStats.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeFromLearnUser(String userID, ArrayList<Integer> questionToRemove) {
        for(Integer i: questionToRemove) {
            dataRef.child(userID)
                    .child("Learn")
                    .child(String.valueOf(i))
                    .setValue(null);
        }
    }
}
