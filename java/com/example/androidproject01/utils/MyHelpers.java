package com.example.androidproject01.utils;


import java.util.HashMap;


public class MyHelpers {
    public static HashMap<String,Integer> categoryList() {
        return new HashMap<String, Integer>() {{
            put("Entertainment: Film", 11);
            put("Geography", 22);
            put("Entertainment: Cartoon & Animations", 32);
            put("Art", 25);
            put("Entertainment: Music", 12);
            put("Entertainment: Books", 10);
            put("Science: Computers", 18);
            put("Science & Nature", 17);
            put("History", 23);
            put("Entertainment: Comics", 29);
            put("Vehicles", 28);
            put("General Knowledge" , 9);
            put("Entertainment: Board Games", 16);
            put("Celebrities", 26);
            put("Entertainment: Japanese Anime & Manga", 31);
            put("Science: Mathematics", 19);
            put("Animals", 27);
            put("Entertainment: Television", 14);
            put("Mythology", 20);
            put("Entertainment: Musicals & Theatres", 13);
            put("Politics", 24);
            put("Science: Gadgets", 30);
            put("Entertainment: Video Games", 15);
            put("Sports", 21);
        }};
    }

    public static HashMap<String,Integer> categoryListHe() {
        return new HashMap<String, Integer>() {{
            put("בידור: סרטים",11);
            put("גֵאוֹגרַפיָה",22);
            put("בידור: סרטים מצוירים ואנימציות",32);
            put("אומנות",25);
            put("בידור: מוסיקה",12);
            put("בידור: ספרים",10);
            put("מדע: מחשבים",18);
            put("מדע וטבע",17);
            put("הִיסטוֹרִיָה",23);
            put("בידור: קומיקס",29);
            put("כלי רכב",28);
            put("ידע כללי", 9);
            put("בידור: משחקי קופסא",16);
            put("ידוענים",26);
            put("בידור: אנימה יפנית ומנגה",31);
            put("מדע: מתמטיקה",19);
            put("בעלי חיים",27);
            put("בידור: טלוויזיה",14);
            put("מִיתוֹלוֹגִיָה",20);
            put("בידור: מחזות זמר ותיאטראות",13);
            put("פּוֹלִיטִיקָה",24);
            put("מדע: גאדג'טים",30);
            put("בידור: משחקי וידאו",15);
            put("ספורט",21);
        }};
    }

}
