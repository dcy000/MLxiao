package com.example.han.referralproject.intelligent_diagnosis;

/**
 * Created by Administrator on 2018/5/16.
 */

public class LifeTherapy {

    /**
     * userId : 100001
     * startTime : 1525622400000
     * endTime : 1526227200000
     * naSalt : 0.0
     * naSaltTarget : 42.0
     * sportTime : 0
     * sportTimeTarget : 90
     * bmi : null
     * weight : 0.0
     * height : 100.0
     * weightTarget : 47.352941
     * wineDrink : 0
     * wineDrinkTarget : 175
     * completion : 0
     */

    private int userId;
    private String startTime;
    private String endTime;
    private double naSalt;
    private double naSaltTarget;
    private double sportTime;
    private double sportTimeTarget;
    private Object bmi;
    private double weight;
    private double height;
    private double weightTarget;
    private double wineDrink;
    private double wineDrinkTarget;
    private int completion;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getNaSalt() {
        return naSalt;
    }

    public void setNaSalt(double naSalt) {
        this.naSalt = naSalt;
    }

    public double getNaSaltTarget() {
        return naSaltTarget;
    }

    public void setNaSaltTarget(double naSaltTarget) {
        this.naSaltTarget = naSaltTarget;
    }

    public double getSportTime() {
        return sportTime;
    }

    public void setSportTime(int sportTime) {
        this.sportTime = sportTime;
    }

    public double getSportTimeTarget() {
        return sportTimeTarget;
    }

    public void setSportTimeTarget(int sportTimeTarget) {
        this.sportTimeTarget = sportTimeTarget;
    }

    public Object getBmi() {
        return bmi;
    }

    public void setBmi(Object bmi) {
        this.bmi = bmi;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public double getWineDrink() {
        return wineDrink;
    }

    public void setWineDrink(int wineDrink) {
        this.wineDrink = wineDrink;
    }

    public double getWineDrinkTarget() {
        return wineDrinkTarget;
    }

    public void setWineDrinkTarget(int wineDrinkTarget) {
        this.wineDrinkTarget = wineDrinkTarget;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }
}
