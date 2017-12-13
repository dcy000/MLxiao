package com.example.han.referralproject.bean;

/**
 * Created by gzq on 2017/12/11.
 */

public class DiseaseResult {
    public String sports;
    public String bname;
    public String review;
    public String eat;
    public String suggest;

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getEat() {
        return eat;
    }

    public void setEat(String eat) {
        this.eat = eat;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public DiseaseResult() {
    }

    public DiseaseResult(String sports, String bname, String review, String eat, String suggest) {
        this.sports = sports;
        this.bname = bname;
        this.review = review;
        this.eat = eat;
        this.suggest = suggest;
    }
}
