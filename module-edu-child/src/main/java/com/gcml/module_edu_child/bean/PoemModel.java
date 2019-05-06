package com.gcml.module_edu_child.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/8.
 */

public class PoemModel implements Parcelable {

    private int id;

    private String dynasty;
    private String author;
    private String title;
    private String showContent;
    private String content;
    private String category;
    private int status;

    public PoemModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PoemModel{" +
                "id=" + id +
                ", dynasty='" + dynasty + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", showContent='" + showContent + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.dynasty);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.showContent);
        dest.writeString(this.content);
        dest.writeString(this.category);
        dest.writeInt(this.status);
    }

    protected PoemModel(Parcel in) {
        this.id = in.readInt();
        this.dynasty = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.showContent = in.readString();
        this.content = in.readString();
        this.category = in.readString();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<PoemModel> CREATOR = new Parcelable.Creator<PoemModel>() {
        @Override
        public PoemModel createFromParcel(Parcel source) {
            return new PoemModel(source);
        }

        @Override
        public PoemModel[] newArray(int size) {
            return new PoemModel[size];
        }
    };

    public static List<PoemModel> parsePoems(String poemsJson) {
        try {
            JSONArray poemArray = new JSONArray(poemsJson);
            return parsePoems(poemArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PoemModel> parsePoems(JSONArray poemArray) {
        if (poemArray == null || poemArray.length() == 0) {
            return null;
        }
        try {
            List<PoemModel> models = new ArrayList<>();
            JSONObject poemObj;
            PoemModel model;
            for (int i = 0, length = poemArray.length(); i < length; i++) {
                poemObj = poemArray.getJSONObject(i);
                if (poemObj == null) {
                    continue;
                }
                model = new PoemModel();
                model.setId(poemObj.optInt("id"));
                model.setDynasty(poemObj.optString("dynasty"));
                model.setAuthor(poemObj.optString("author"));
                model.setTitle(poemObj.optString("title"));
                model.setShowContent(poemObj.optString("showContent"));
                model.setContent(poemObj.optString("content"));
                model.setCategory(poemObj.optString("category"));
                model.setStatus(poemObj.optInt("status"));
                models.add(model);
            }
            return models;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
