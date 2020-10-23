package com.example.androidproject01.models;


public class LearnToDB {
    private int ID;
    private String category;
    private String question;
    private String shortAnswer;
    private String urlToLearnMore;


    public LearnToDB(){}

    public LearnToDB(String category, String question, String shortAnswer, String urlToLearnMore)  {
        this.category = category;
        this.ID = question.hashCode();
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.urlToLearnMore = urlToLearnMore;
    }

    public String getCategory() { return category; }

    public String getQuestion() {
        return question;
    }

    public String getShortAnswer() {
        return shortAnswer;
    }

    public String getUrlToLearnMore() {
        return urlToLearnMore;
    }

    public int getID() { return ID; }

    public void setID(Long ID)  { this.ID = Math.toIntExact(ID); }

}
