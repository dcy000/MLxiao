package com.example.han.referralproject.children.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afirez on 2018/3/9.
 */

public class StoryModel implements Parcelable {

    private String name;
    private String playUrl;

    public StoryModel() {
    }

    public StoryModel(String name, String playUrl) {
        this.name = name;
        this.playUrl = playUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    @Override
    public String toString() {
        return "StoryModel{" +
                "name='" + name + '\'' +
                ", playUrl='" + playUrl + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.playUrl);
    }

    protected StoryModel(Parcel in) {
        this.name = in.readString();
        this.playUrl = in.readString();
    }

    public static final Parcelable.Creator<StoryModel> CREATOR = new Parcelable.Creator<StoryModel>() {
        @Override
        public StoryModel createFromParcel(Parcel source) {
            return new StoryModel(source);
        }

        @Override
        public StoryModel[] newArray(int size) {
            return new StoryModel[size];
        }
    };

    public static List<StoryModel> parseStories(String storiesJson) {
        try {
            JSONArray storyArray = new JSONArray(storiesJson);
            return parseStories(storyArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<StoryModel> parseStories(JSONArray storyArray) {
        if (storyArray == null) {
            return null;
        }
        try {
            ArrayList<StoryModel> models = new ArrayList<>();
            for (int i = 0, length = storyArray.length(); i < length; i++) {
                JSONObject storyObj;
                storyObj = storyArray.getJSONObject(i);
                if (storyObj == null) {
                    continue;
                }
                models.add(new StoryModel(
                        storyObj.optString("name"),
                        storyObj.optString("playUrl")
                ));
            }
            return models;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
