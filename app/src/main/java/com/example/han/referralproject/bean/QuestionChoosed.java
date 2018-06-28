package com.example.han.referralproject.bean;

/**
 * Created by Administrator on 2018/5/4.
 */

public class QuestionChoosed {
    private String question;
    private int isChoosed;
    private int orderId;
    public String getQuestion() {
        return question;
    }

    public QuestionChoosed() {
    }

    public QuestionChoosed(String question, int isChoosed) {
        this.question = question;
        this.isChoosed = isChoosed;
    }

    public QuestionChoosed(String question, int isChoosed, int orderId) {
        this.question = question;
        this.isChoosed = isChoosed;
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int isChoosed() {
        return isChoosed;
    }

    public void setChoosed(int choosed) {
        isChoosed = choosed;
    }
}
