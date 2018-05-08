package com.example.han.referralproject.bean;

/**
 * Created by Administrator on 2018/5/4.
 */

public class QuestionChoosed {
    private String question;
    private boolean isChoosed;

    public String getQuestion() {
        return question;
    }

    public QuestionChoosed() {
    }

    public QuestionChoosed(String question, boolean isChoosed) {
        this.question = question;
        this.isChoosed = isChoosed;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}
