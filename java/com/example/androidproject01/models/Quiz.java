package com.example.androidproject01.models;

import com.example.androidproject01.Tasks.RetrieveQuestions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Quiz {
    private int UserAmountOF_Questions;
    private List<Questions> questionsList;
    private Date timeOfQuiz;
    private int correctAnswers;
    private int currentQuestionNumber;

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

    private ArrayList<String> answers;

    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public Date getTimeOfQuiz() {
        return timeOfQuiz;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public String getCurrentCategory(){return questionsList.get(currentQuestionNumber).getCategory();}

    public Quiz(int amount, List<String> CategoriesID, String difficulty) {
        UserAmountOF_Questions= amount;
        this.questionsList = new LinkedList<>();
        int amount_for_each_category = (amount+1)/CategoriesID.size();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(amount_for_each_category,amount_for_each_category,10, TimeUnit.NANOSECONDS, new LinkedBlockingDeque<>());
        List<Future<List<Questions>>> listFuture = new LinkedList<>();
        for(int i=0;i<CategoriesID.size();i++) {
//            String diff = difficulty.equals("any") ? "" : "&difficulty=" + difficulty;
            listFuture.add(threadPoolExecutor.submit(new RetrieveQuestions(Math.max(amount_for_each_category, 5), CategoriesID.get(i))));
        }
        for(Future<List<Questions>> future : listFuture){
            try {
                questionsList.addAll(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collections.shuffle(questionsList);
        this.timeOfQuiz = Calendar.getInstance().getTime();
        this.correctAnswers =0;
        this.currentQuestionNumber = 0;
    }

    public String getCurrentQuestion() {
        return questionsList.get(currentQuestionNumber).getQuestion();
    }


    public void shuffleAnswers() {
        answers = new ArrayList<>(questionsList.get(currentQuestionNumber).getWrongAnswers());
        answers.add(questionsList.get(currentQuestionNumber).getCorrectAnswer());
        Collections.shuffle(answers);
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getCurrentCorrectAnswer() {
        return questionsList.get(currentQuestionNumber).getCorrectAnswer();
    }

    public void increaseQuestionNumber() {
        currentQuestionNumber++;
    }


    public boolean finish() {
        return currentQuestionNumber == UserAmountOF_Questions;
    }

}
