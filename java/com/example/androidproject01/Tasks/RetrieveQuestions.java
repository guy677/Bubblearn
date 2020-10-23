package com.example.androidproject01.Tasks;



import com.example.androidproject01.models.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.LinkedList;
import java.util.List;

public class RetrieveQuestions extends RetrieveTask<List<Questions>> {

    public RetrieveQuestions(int amount,String category) {
        super("https://opentdb.com/api.php?amount="+amount +"&category="+category+"&encode=base64");
    }

    @Override
    protected List<Questions> parseJason(JSONObject json) {
        List<Questions> questionsList= new LinkedList<>();
        try {
            if((int)json.get("response_code") == 0){
                JSONArray stringList = (JSONArray) json.get("results");
                for(int i=0;i<stringList.length();i++){
                    JSONObject jsonObject = stringList.getJSONObject(i);
                    String currentCategory = (String) jsonObject.get("category");
                    String currentQuestion = (String) jsonObject.get("question");
                    JSONArray incorrect = (JSONArray) jsonObject.get("incorrect_answers");
                    String correct = jsonObject.getString("correct_answer");
                    Questions question = new Questions(currentQuestion,
                            incorrect,
                            correct ,currentCategory);
                    questionsList.add(question);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionsList;
    }



}
