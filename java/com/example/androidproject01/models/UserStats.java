package com.example.androidproject01.models;



import java.util.HashMap;


public class UserStats {

    private HashMap<String,HashMap<String,Long>> data;

    public UserStats(){
        data = new HashMap<>();
    }
    public UserStats(HashMap<String, HashMap<String, Long>> data) {
        this.data = data;
    }

    public HashMap<String, HashMap<String, Long>> getData() {
        return data;
    }

    public void setData(HashMap<String, HashMap<String, Long>> data) {
        this.data = data;
    }

    public void increaseCategory(String c , String s){

        if(!data.containsKey(c)){
            data.put(c,new HashMap<String,Long>());
            data.get(c).put("Correct", (long) 0);
            data.get(c).put("Wrong", (long) 0);
        }
        Long current = data.get(c).get(s);
        current++;
        data.get(c).put(s,current);

    }
}
