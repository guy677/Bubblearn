package com.example.androidproject01.models;


import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String preferNumberOfQuestions;
    private String MusicOn;
    private String difficulty = "";
    private String seconds = "15";
    private String lang;
    public User() {}

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.preferNumberOfQuestions = "15";
        this.MusicOn = "true";
    }

    public void setDifficulty(String diff) {
        difficulty =diff;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail(){return this.email;}

    public String getPreferNumberOfQuestions(){return this.preferNumberOfQuestions;}
    public String getMusicOn(){return this.MusicOn;}
    public void setPreferNumberOfQuestions(String preferNumberOfQuestions){this.preferNumberOfQuestions = preferNumberOfQuestions;}
    public void setMusicOn(String MusicOn){ this.MusicOn= MusicOn;}


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static User toUser(@NotNull FirebaseUser firebaseUser){
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email_user = firebaseUser.getEmail();
        return new User(uid, name, email_user);
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String i) {
        seconds = i;
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}

