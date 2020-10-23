package com.example.androidproject01.Tasks;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.LinkedList;
import java.util.List;

public class getDataFromGoogle extends RetrieveTask {

    public getDataFromGoogle(String url) {
        super(url);
    }

    @Override
    protected  List<String>  parseJason(JSONObject json) {
        List<String> answerAndUrl = new LinkedList<>();
        try {
                JSONArray stringList = (JSONArray) json.get("items");
                    JSONObject jsonObject = stringList.getJSONObject(0);
                    answerAndUrl.add((String) jsonObject.get("snippet"));
                    answerAndUrl.add((String) jsonObject.get("formattedUrl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answerAndUrl;
    }

}
