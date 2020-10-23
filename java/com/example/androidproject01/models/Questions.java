package com.example.androidproject01.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;


public class Questions {
    private String Question;
    private List<String> WrongAnswers;
    private String CorrectAnswer;
    private String Category;

    public String getQuestion() {
        return Question;
    }

    public List<String> getWrongAnswers() {
        return WrongAnswers;
    }

    public void setQuestion(String question) {
            byte[] decodedQuestion = Base64.getDecoder().decode(question);
            Question  = new String(decodedQuestion);
    }

    public void setCategory(String category) {
        byte[] decodedQuestion = Base64.getDecoder().decode(category);
        Category  = new String(decodedQuestion);
    }

    public String getCategory(){
        return this.Category;
    }

    public void addWrongAnswer(String wrongAnswer){
        this.WrongAnswers.add(wrongAnswer);
    }
    public void setWrongAnswers(JSONArray wrongAnswers) {
        for(int i=0;i<wrongAnswers.length();i++){
            try {
                byte[] decodedAnswer = Base64.getDecoder().decode(wrongAnswers.getString(i));
                String wrongAnswer = new String(decodedAnswer);
                addWrongAnswer(wrongAnswer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCorrectAnswer(String correctAnswer) {
        byte[] decodedQuestion = Base64.getDecoder().decode(correctAnswer);
        this.CorrectAnswer = new String(decodedQuestion);
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public Questions(String question, JSONArray wrongAnswers, String correctAnswer , String category ) {
        setQuestion(question);
        setCategory(category);
        WrongAnswers = new LinkedList<>();
        setWrongAnswers(wrongAnswers);
        setCorrectAnswer(correctAnswer);
    }
}
