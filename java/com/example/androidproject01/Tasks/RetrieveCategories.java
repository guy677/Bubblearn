package com.example.androidproject01.Tasks;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RetrieveCategories extends RetrieveTask<HashMap<String, Integer>> {
    public RetrieveCategories(String ...urls) {
        super(urls);
    }

    @Override
    protected HashMap<String, Integer> parseJason(JSONObject json) {
        HashMap<String,Integer> CategoryList= new HashMap<>();
        try {
               JSONArray stringList = (JSONArray) json.get("trivia_categories");
               for(int i=0;i<stringList.length();i++){
                   JSONObject jsonObject = stringList.getJSONObject(i);
                   String name = (String) jsonObject.get("name");
                   int id = (int) jsonObject.get("id");
                   CategoryList.put(name,id);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CategoryList;
    }
}
