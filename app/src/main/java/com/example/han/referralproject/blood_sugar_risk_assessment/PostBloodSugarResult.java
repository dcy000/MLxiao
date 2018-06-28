package com.example.han.referralproject.blood_sugar_risk_assessment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/10.
 */

public class PostBloodSugarResult implements Parcelable{

    /**
     * hmQuestionnaireAssessId : 39fc7ed9-0968-4947-8755-7332a9ff6d32
     * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
     * minScore : 0
     * maxScore : 6
     * score : 5
     * criticality : 低
     * diseaseProbability : 1
     * assessInfo : null
     * assessSuggest : 您目前地糖尿病的风险不高，还请您继续保持良好的生活习惯
     */

    private String hmQuestionnaireAssessId;
    private String hmQuestionnaireId;
    private int minScore;
    private int maxScore;
    private int score;
    private String criticality;
    private String diseaseProbability;
    private String assessSuggest;

    protected PostBloodSugarResult(Parcel in) {
        hmQuestionnaireAssessId = in.readString();
        hmQuestionnaireId = in.readString();
        minScore = in.readInt();
        maxScore = in.readInt();
        score = in.readInt();
        criticality = in.readString();
        diseaseProbability = in.readString();
        assessSuggest = in.readString();
    }

    public static final Creator<PostBloodSugarResult> CREATOR = new Creator<PostBloodSugarResult>() {
        @Override
        public PostBloodSugarResult createFromParcel(Parcel in) {
            return new PostBloodSugarResult(in);
        }

        @Override
        public PostBloodSugarResult[] newArray(int size) {
            return new PostBloodSugarResult[size];
        }
    };

    public String getHmQuestionnaireAssessId() {
        return hmQuestionnaireAssessId;
    }

    public void setHmQuestionnaireAssessId(String hmQuestionnaireAssessId) {
        this.hmQuestionnaireAssessId = hmQuestionnaireAssessId;
    }

    public String getHmQuestionnaireId() {
        return hmQuestionnaireId;
    }

    public void setHmQuestionnaireId(String hmQuestionnaireId) {
        this.hmQuestionnaireId = hmQuestionnaireId;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public String getDiseaseProbability() {
        return diseaseProbability;
    }

    public void setDiseaseProbability(String diseaseProbability) {
        this.diseaseProbability = diseaseProbability;
    }

    public String getAssessSuggest() {
        return assessSuggest;
    }

    public void setAssessSuggest(String assessSuggest) {
        this.assessSuggest = assessSuggest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hmQuestionnaireAssessId);
        dest.writeString(hmQuestionnaireId);
        dest.writeInt(minScore);
        dest.writeInt(maxScore);
        dest.writeInt(score);
        dest.writeString(criticality);
        dest.writeString(diseaseProbability);
        dest.writeString(assessSuggest);
    }
}
