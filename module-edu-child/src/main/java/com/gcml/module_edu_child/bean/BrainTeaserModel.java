package com.gcml.module_edu_child.bean;

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

    public static BrainTeaserModel parseBrainTeaser(String brainTeaser) {
        if (TextUtils.isEmpty(brainTeaser)) {
            return null;
        }
        if (!brainTeaser.contains("问题") || !brainTeaser.contains("答案")) {
            return null;
        }
        int index1 = brainTeaser.indexOf("问题：");
        int index2 = brainTeaser.indexOf("答案：");
        if (index1 == -1 || index2 == -1) {
            return null;
        }
        String question = brainTeaser.substring(index1 + 3, index2);
        String answer = brainTeaser.substring(index2 + 3, brainTeaser.length() - 5);
        if (TextUtils.isEmpty(question) || TextUtils.isEmpty(answer)) {
            return null;
        }
        return new BrainTeaserModel(question, answer);
    }
}
