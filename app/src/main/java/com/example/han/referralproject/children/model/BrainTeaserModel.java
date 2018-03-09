package com.example.han.referralproject.children.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 脑经急转弯
 * Created by afirez on 2018/3/9.
 */

public class BrainTeaserModel implements Parcelable {
    private String question;

    private String answer;

    public BrainTeaserModel() {
    }

    public BrainTeaserModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "BrainTeaserModel{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.answer);
    }

    protected BrainTeaserModel(Parcel in) {
        this.question = in.readString();
        this.answer = in.readString();
    }

    public static final Parcelable.Creator<BrainTeaserModel> CREATOR = new Parcelable.Creator<BrainTeaserModel>() {
        @Override
        public BrainTeaserModel createFromParcel(Parcel source) {
            return new BrainTeaserModel(source);
        }

        @Override
        public BrainTeaserModel[] newArray(int size) {
            return new BrainTeaserModel[size];
        }
    };

    public BrainTeaserModel parseBrainTeaser(String parseBrain) {
        if (TextUtils.isEmpty(parseBrain)) {
            return null;
        }
        return null;
    }
}
