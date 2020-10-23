package com.example.androidproject01;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.androidproject01.Tasks.getDataFromGoogle;
import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.DB_learn;
import com.example.androidproject01.models.DB_model;
import com.example.androidproject01.models.LearnToDB;
import com.example.androidproject01.models.MyBounceInterpolator;
import com.example.androidproject01.models.Quiz;
import com.example.androidproject01.models.QuizToDB;
import com.example.androidproject01.models.User;
import com.example.androidproject01.models.UserStats;
import com.example.androidproject01.models.questionToDB;
import com.example.androidproject01.models.retrieveDataRealTime;
import com.example.androidproject01.utils.baseView;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TriviaActivity extends baseView {


    private Quiz triviaQuiz;
    private int CorrectButtonID;
    private LinkedList<Integer> ButtonsIds;
    private AuthViewModel  authViewModel;
    private DB_model db_model;
    public static final String EXTRA_NUBMER_OF_CORRECT = "com.example.androidproject01.EXTRA_CORRECT";
    public static final String EXTRA_NUBMER_OF_WRONG = "com.example.androidproject01.EXTRA_WRONG";
    private String numOfTotalQuestions;
    private QuizToDB quizToDB;
    private String userID;

    /*Save for Learning variables*/
    private List<String> urlsForLearning = new LinkedList<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,10, TimeUnit.NANOSECONDS,new LinkedBlockingDeque<>());
    private List<String> wrongs = new LinkedList<>();
    private List<String> wrongsCategories = new LinkedList<>();
    private DB_learn db_LEARN;
    private ArrayList<Integer> UserCurrentQuestionIdToLEARN;

    //Timer Variables
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private double currentTime;
    private ProgressBar progressBarTimer;


    //User Stats Variables
    private UserStats userStats;
    private int seconds;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        this.HideUI();
        initAuthViewModel();

        Intent QuestionsIntent = getIntent();
        userID =authViewModel.getUser().getUid();
        ArrayList<String> CategoryNumbers = QuestionsIntent.getStringArrayListExtra(SelectCategoryActivity.EXTRA_QuestionsList);


        db_model = new ViewModelProvider(this).get(DB_model.class);
        db_model.setRealTimeRepository("Members");
        assert CategoryNumbers != null;
        db_model.getUserSetting(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                User user = (User) data;
                numOfTotalQuestions = Integer.toString(Integer.parseInt(user.getPreferNumberOfQuestions())-1);
                triviaQuiz = new Quiz(Integer.parseInt(numOfTotalQuestions),CategoryNumbers,user.getDifficulty());
                numOfTotalQuestions = user.getPreferNumberOfQuestions();
                quizToDB = new QuizToDB(Integer.parseInt(numOfTotalQuestions));
                seconds = Integer.parseInt(user.getSeconds()) *1000;
                UpdateQuestions();
                startTimer();
            }
            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        db_model.getUserLearningList(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                HashMap<Integer,Integer> learn = (HashMap<Integer, Integer>) data;
                UserCurrentQuestionIdToLEARN = new ArrayList<>();
                for(Integer i: learn.values()){
                    UserCurrentQuestionIdToLEARN.add(i);
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        db_model.getUsersStats(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                userStats = (UserStats) data;
                if(userStats == null){
                    userStats = new UserStats();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        ButtonsIds = new LinkedList<>();
        ButtonsIds.add(R.id.btnAnswr1);
        ButtonsIds.add(R.id.btnAnswr2);
        ButtonsIds.add(R.id.btnAnswr3);
        ButtonsIds.add(R.id.btnAnswr4);
        timerTextView =findViewById(R.id.txtTimer);
        progressBarTimer = findViewById(R.id.progressBar);
    }

    public void startTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
        else {
            countDownTimer = new CountDownTimer(seconds, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long l) {
                    currentTime = (double) l / 1000;
                    double pstatus = ((16-currentTime)/15)*100;
                    progressBarTimer.setProgress((int)(pstatus));
                    timerTextView.setText(Integer.toString((int) l / 1000));
                }

                @Override
                public void onFinish() {
                    int toSeconds = seconds/1000;
                    showCorrectOne();
                    quizToDB.addWrongQuestion(new questionToDB(triviaQuiz.getCurrentCategory(), triviaQuiz.getCurrentQuestion(), toSeconds-(int)currentTime));
                    Handler delay = new Handler();
                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NextQuestion();
                        }

                    }, 1000); // 5000ms delay
                }


            };
        }
    }

    private void showCorrectOne() {
        Button correctOne = findViewById(CorrectButtonID);
        correctOne.setBackgroundResource(R.drawable.correct_answer_button);
        correctOne.setTextColor(Color.WHITE);

    }

    @SuppressLint("ResourceAsColor")
    private void UpdateQuestions() {
        SetClickAble(true);
        TextView Question = findViewById(R.id.txtQuestion);
        Question.setText(triviaQuiz.getCurrentQuestion());
        TextView Category = findViewById(R.id.txtCategory);
        Category.setText(triviaQuiz.getCurrentCategory());
        TextView currentPosition = findViewById(R.id.msg_current_possition);

        /*
          SETTING DEFAULT COLOR AND VISIBILITY
         */
        @SuppressLint("UseCompatLoadingForDrawables") Drawable answerButton = getDrawable(R.drawable.answer_button);
        ButtonsIds.forEach(id-> {
            Button button = findViewById(id);
            button.setBackground(answerButton);
            button.setVisibility(View.INVISIBLE);
        });

        triviaQuiz.shuffleAnswers();
        int i =0;
        for(String answer : triviaQuiz.getAnswers()){
            Button questionAnswer = findViewById(ButtonsIds.get(i));
            if(answer.equals(triviaQuiz.getCurrentCorrectAnswer()))
            {
                CorrectButtonID = questionAnswer.getId();
            }
            questionAnswer.setText(answer);
            questionAnswer.setTextColor(Color.parseColor("#3F51B5"));
            questionAnswer.setVisibility(View.VISIBLE);
            i++;
        }
        currentPosition.setText(triviaQuiz.getCurrentQuestionNumber()+1+"/"+(Integer.parseInt(numOfTotalQuestions)));
        startTimer();
    }


    @SuppressLint("SetTextI18n")
    public void checkAnswer(View view) {
        SetClickAble(false);
        Button chosenAnswer = (Button)view;
        Bounce((Button) view);
        if(triviaQuiz.getQuestionsList()!=null){
            if(view.getId() == CorrectButtonID){
                chosenAnswer.setBackgroundResource(R.drawable.correct_answer_button);
                chosenAnswer.setTextColor(Color.WHITE);
                userStats.increaseCategory(triviaQuiz.getCurrentCategory(),"Correct");
            }
            else {
                userStats.increaseCategory(triviaQuiz.getCurrentCategory(),"Wrong");
                chosenAnswer.setBackgroundResource(R.drawable.wrong_answer_button);
                chosenAnswer.setTextColor(Color.WHITE);
                Button correctButton = findViewById(CorrectButtonID);
                correctButton.setTextColor(Color.WHITE);
                correctButton.setBackgroundResource(R.drawable.correct_answer_button);
                quizToDB.addWrongQuestion(new questionToDB(triviaQuiz.getCurrentCategory(), triviaQuiz.getCurrentQuestion(), (int) currentTime));
                wrongs.add(triviaQuiz.getCurrentQuestion());
                wrongsCategories.add(triviaQuiz.getCurrentCategory());
                String key = "AIzaSyBOwHAuv9gO5CADDSJWFdIZ8FOGnWJP1Vc";
                String url = "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=013036536707430787589:_pqjad5hr1a&q=" + triviaQuiz.getCurrentQuestion() + "&alt=json";
                urlsForLearning.add(url);
            }
            Handler delay = new Handler();
            delay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timerTextView.setText(Integer.toString((int)currentTime));
                    NextQuestion();
                }

            }, 2000); // 5000ms delay
        }

    }

    private void SetClickAble(boolean bool) {
        for(int buttonID :ButtonsIds){
            Button answer = findViewById(buttonID);
            answer.setClickable(bool);
        }
    }

    public void NextQuestion() {

        if(triviaQuiz.finish())
        {
            /* Create New Thread for Learning*/
            Thread learningThread = new Thread(() -> {
                try {
                    saveLearning();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            learningThread.start();
            /*-----------------------------------------------------*/
            countDownTimer.cancel();
            quizToDB.SetCorrect();
            writeStatsToDB();
            Intent goToSummary = new Intent(this, summaryOfQuestions.class);
            goToSummary.putExtra(EXTRA_NUBMER_OF_CORRECT,Integer.toString(quizToDB.getCorrect()));
            goToSummary.putExtra(EXTRA_NUBMER_OF_WRONG,Integer.toString(quizToDB.getWrongQuestionsSize()));
            startActivity(goToSummary);
            finish();
        }
        else {
            triviaQuiz.increaseQuestionNumber();
            UpdateQuestions();
        }
    }

    private void saveLearning() throws InterruptedException {
        db_LEARN = new ViewModelProvider(this).get(DB_learn.class);
        int i =0;
        for (String url : urlsForLearning) {
            String question = wrongs.get(i);
            String category = wrongsCategories.get(i);
            if(!UserCurrentQuestionIdToLEARN.contains(question.hashCode())){
                UserCurrentQuestionIdToLEARN.add(question.hashCode());
            }
            db_LEARN.getQuestionFromLearn(question.hashCode(), new retrieveDataRealTime() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        if(data == null) {
                            getDataFromGoogle get = new getDataFromGoogle(url);
                            List<String> learnToDB = null;
                            Future<List<String>> l = threadPoolExecutor.submit(get);
                            try {
                                learnToDB = (List<String>) l.get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                LearnToDB learn = new LearnToDB(
                                        category,
                                        question,
                                        learnToDB.get(0),
                                        learnToDB.get(1));
                                db_LEARN.addLearning(learn);
                            }
                            catch(NullPointerException e){

                            }
                        }
                    }

                    @Override
                    public void onFailed(DatabaseError databaseError) {

                    }
                });
                Thread.sleep(2000);
                i++;
        }
        db_model.addLearningToUser(userID, UserCurrentQuestionIdToLEARN);
    }

    private void Bounce(Button button) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
    }



    private void writeStatsToDB(){
        db_model.addQuizToMember(userID,quizToDB);
        db_model.addStatsToMember(userID,userStats);
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

}