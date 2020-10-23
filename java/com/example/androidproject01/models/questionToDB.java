package com.example.androidproject01.models;

/**
 * Class which defines the struct in the DB
 */
public class questionToDB{
    private String category;
    private String question;
    private int timeToAnswer;

    public questionToDB(String category, String question,int timeToAnswer) {
        this.category = category;
        this.question = question;
        this.timeToAnswer = timeToAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public int getTimeToAnswer() { return timeToAnswer; }
}
