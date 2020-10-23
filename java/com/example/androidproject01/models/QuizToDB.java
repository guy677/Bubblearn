package com.example.androidproject01.models;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class QuizToDB {
    private String quizID;
    private List<questionToDB> wrongQuestions;
    private int total;
    private int correct;

    public QuizToDB(){};
    public QuizToDB(int total) {
        this.quizID = Calendar.getInstance().getTime().toString();
        this.wrongQuestions = new LinkedList<>();
        this.total = total;
    }

    public void setCorrect(int i){this.correct= i; }
    public void setWrongQuestions(List<questionToDB> l){this.wrongQuestions = l;}

    public void addWrongQuestion(questionToDB question){
        wrongQuestions.add(question);
    }

    public int getWrongQuestionsSize() {
        return wrongQuestions.size();
    }

    public void SetCorrect(){
        correct = total - getWrongQuestionsSize();
    }

    public int getCorrect() {
        return correct;
    }

    public List<questionToDB> getWrongQuestions() {
        return wrongQuestions;
    }

    public String getQuizID() {
        return quizID;
    }
}
