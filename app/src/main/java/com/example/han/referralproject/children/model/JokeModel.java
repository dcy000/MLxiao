package com.example.han.referralproject.children.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afirez on 2018/3/8.
 */

public class JokeModel {
    private String content;

    public JokeModel() {
    }

    public JokeModel(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static List<JokeModel> parseJokes(String jokeJson) {
        try {
            JSONArray jokeArray = new JSONArray(jokeJson);
            return parseJokes(jokeArray);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<JokeModel> parseJokes(JSONArray jokeArray) {
        if (jokeArray == null) {
            return null;
        }
        try {
            JSONObject jokeObj;
            List<JokeModel> models = new ArrayList<>();
            for (int i = 0, length = jokeArray.length(); i < length; i++) {
                jokeObj = jokeArray.getJSONObject(i);
                String content = jokeObj.optString("content");
                if (!TextUtils.isEmpty(content)) {
                    models.add(new JokeModel(content));
                }
            }
            return models;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
