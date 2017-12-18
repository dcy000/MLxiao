package com.example.han.referralproject.recyclerview;

/**
 * Created by han on 2017/12/14.
 */

public class OnlineTime {

    public String time;


    public OnlineTime() {

    }

    public OnlineTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OnlineTime{" +
                "time='" + time + '\'' +
                '}';
    }
}
