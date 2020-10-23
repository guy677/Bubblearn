package com.example.androidproject01.Tasks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.concurrent.Callable;

public abstract class RetrieveTask<R> implements Callable<R> {
    private  String[] urls;
    private HttpURLConnection urlConnection;

    public RetrieveTask(String... urls) {
        this.urls = urls;
    }


    @Override
    public R call(){
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(this.urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJason(jsonObject);
    }

    protected abstract R parseJason(JSONObject jsonObject);
}
