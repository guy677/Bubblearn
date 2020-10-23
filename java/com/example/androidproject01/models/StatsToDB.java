package com.example.androidproject01.models;

public class StatsToDB {

    private String statsID;
    private Long correctAnswers;
    private Long wrongAnswers;
    private UserStats userStats;

    public StatsToDB(){ }
    public StatsToDB(Long correctAnswers,Long wrongAnswers, String ID , UserStats userStats){
        this.statsID = ID;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.userStats = userStats;
    }


    public Long getCorrectAnswers() {
        return correctAnswers;
    }

    public Long getWrongAnswers() {
        return wrongAnswers;
    }

    public String getStatsID() {
        return statsID;
    }

    public UserStats getUserStats() {
        return userStats;
    }
}
